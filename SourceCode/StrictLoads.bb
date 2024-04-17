
Function LoadImage_Strict(file$)
	If FileType(file$)<>1 Then RuntimeError "Image " + Chr(34) + file$ + Chr(34) + " missing. "
	
	Local tmp

	If I_Loc\Localized And FileType(I_Loc\LangPath + file$)=1 Then
		tmp = LoadImage(I_Loc\LangPath + file)
	EndIf
	
	If tmp = 0 Then
		tmp = LoadImage(file$)
	EndIf
	
	If tmp = 0 Then
		;if loading default image failed, add an error message to the console and return a black image
		CreateConsoleMsg("Loading image " + Chr(34) + file$ + Chr(34) + " failed.")
		If opt\ConsoleOpening Then
			ConsoleOpen = True
		EndIf
		
		Return CopyImage(MenuMeterIMG);MenuBlack
	EndIf
	BufferDirty ImageBuffer(tmp)
	Return tmp
End Function

Const Mode = 2 ;Looping
Const TwoD = 8192 ;Tells software (not hardware) based sample not to be included in 3d processing.

Const MaxChannelsAmount% = 32

Type Sound
	Field internalHandle%
	Field name$
	Field channels%[MaxChannelsAmount]
	Field releaseTime%
End Type

Function AutoReleaseSounds()
	Local snd.Sound
	For snd.Sound = Each Sound
		Local tryRelease% = True
		For i = 0 To MaxChannelsAmount - 1
			If snd\channels[i] <> 0 Then
				If ChannelPlaying(snd\channels[i]) Then
					tryRelease = False
					snd\releaseTime = MilliSecs()+5000
					Exit
				EndIf
			EndIf
		Next
		If tryRelease Then
			If snd\releaseTime < MilliSecs() Then
				If snd\internalHandle <> 0 Then
					FreeSound snd\internalHandle
					snd\internalHandle = 0
					RemoveSubtitlesToken(snd)
				EndIf
			EndIf
		EndIf
	Next
End Function

Function PlaySound_Strict%(sndHandle%)
	Local snd.Sound = Object.Sound(sndHandle)
	If snd <> Null Then
		Local shouldPlay% = True
		For i = 0 To MaxChannelsAmount - 1
			If snd\channels[i] <> 0 Then
				If Not ChannelPlaying(snd\channels[i]) Then
					If snd\internalHandle = 0 Then
						If FileType(snd\name) <> 1 Then
							CreateConsoleMsg("Sound " + Chr(34) + snd\name + Chr(34) + " not found.")
							If opt\ConsoleOpening
								ConsoleOpen = True
							EndIf
						Else
							If opt\EnableSFXRelease
								If I_Loc\Localized And FileType(I_Loc\LangPath + snd\name)=1 Then
									snd\internalHandle = LoadSound(I_Loc\LangPath + snd\name)
								Else
									snd\internalHandle = LoadSound(snd\name)
								EndIf
								CreateSubtitlesToken(snd\name, snd)
							EndIf
						EndIf
						If snd\internalHandle = 0 Then
							CreateConsoleMsg("Failed to load Sound: " + Chr(34) + snd\name + Chr(34))
							If opt\ConsoleOpening
								ConsoleOpen = True
							EndIf
						EndIf
					EndIf
					If ConsoleFlush Then
						snd\channels[i] = PlaySound(ConsoleFlushSnd)
					Else
						snd\channels[i] = PlaySound(snd\internalHandle)
					EndIf
					ChannelVolume snd\channels[i],opt\SFXVolume#*opt\MasterVol
					snd\releaseTime = MilliSecs()+5000 ;release after 5 seconds
					Return snd\channels[i]
				EndIf
			Else
				If snd\internalHandle = 0 Then
					If FileType(snd\name) <> 1 Then
						CreateConsoleMsg("Sound " + Chr(34) + snd\name + Chr(34) + " not found.")
						If opt\ConsoleOpening
							ConsoleOpen = True
						EndIf
					Else
						If opt\EnableSFXRelease
							If I_Loc\Localized And FileType(I_Loc\LangPath + snd\name)=1 Then
								snd\internalHandle = LoadSound(I_Loc\LangPath + snd\name)
							Else
								snd\internalHandle = LoadSound(snd\name)
							EndIf
							CreateSubtitlesToken(snd\name, snd)
						EndIf
					EndIf
						
					If snd\internalHandle = 0 Then
						CreateConsoleMsg("Failed to load Sound: " + Chr(34) + snd\name + Chr(34))
						If opt\ConsoleOpening
							ConsoleOpen = True
						EndIf
					EndIf
				EndIf
				If ConsoleFlushSnd Then
					snd\channels[i] = PlaySound(ConsoleFlushSnd)
				Else
					snd\channels[i] = PlaySound(snd\internalHandle)
				EndIf
				ChannelVolume snd\channels[i],opt\SFXVolume#*opt\MasterVol
				snd\releaseTime = MilliSecs()+5000 ;release after 5 seconds
				Return snd\channels[i]
			EndIf
		Next
	EndIf
	
	Return 0
End Function

Function LoadSound_Strict(file$)
	Local snd.Sound = New Sound
	snd\name = file
	snd\internalHandle = 0
	snd\releaseTime = 0
	If (Not opt\EnableSFXRelease)
		If I_Loc\Localized And FileType(I_Loc\LangPath + file$)=1 Then
			snd\internalHandle = LoadSound(I_Loc\LangPath + file)
		EndIf
		
		If snd\internalHandle = 0 Then
			snd\internalHandle = LoadSound(file)
			CreateSubtitlesToken(snd\name, snd)
		EndIf
	EndIf
	
	Return Handle(snd)
End Function

Function FreeSound_Strict(sndHandle%)
	Local snd.Sound = Object.Sound(sndHandle)
	If snd <> Null Then
		If snd\internalHandle <> 0 Then
			FreeSound snd\internalHandle
			snd\internalHandle = 0
			RemoveSubtitlesToken(snd)
		EndIf
		Delete snd
	EndIf
End Function

Type Stream
	Field chn%
End Type

Function StreamSound_Strict(file$,volume#=1.0,custommode=Mode)
	Local st.Stream = New Stream
	
	If I_Loc\Localized And FileType(I_Loc\LangPath + file$)=1 Then
		st\chn = PlayMusic(I_Loc\LangPath + file$,custommode+TwoD)
	Else
		If FileType(file$)<>1
			CreateConsoleMsg("Sound " + Chr(34) + file$ + Chr(34) + " not found.")
			If opt\ConsoleOpening
				ConsoleOpen = True
			EndIf
			Return 0
		EndIf
		st\chn = PlayMusic(file$,custommode+TwoD)
	EndIf
	
	If st\chn = -1
		CreateConsoleMsg("Failed to stream Sound (returned -1): " + Chr(34) + file$ + Chr(34))
		If opt\ConsoleOpening
			ConsoleOpen = True
		EndIf
		Return -1
	EndIf
	ChannelVolume(st\chn,volume*1.0)
	
	CreateSubtitlesToken(file, Null)
	
	Return Handle(st)
End Function

Function StopStream_Strict(streamHandle%)
	Local st.Stream = Object.Stream(streamHandle)
	
	If st = Null
		;CreateConsoleMsg("Failed to stop stream Sound: Unknown Stream")
		Return
	EndIf
	If st\chn=0 Lor st\chn=-1
		CreateConsoleMsg("Failed to stop stream Sound: Return value "+st\chn)
		Return
	EndIf
	StopChannel(st\chn)
	Delete st
End Function

Function SetStreamVolume_Strict(streamHandle%,volume#)
	Local st.Stream = Object.Stream(streamHandle)
	
	If st = Null
		;CreateConsoleMsg("Failed to stop stream Sound: Unknown Stream")
		Return
	EndIf
	If st\chn=0 Lor st\chn=-1
		CreateConsoleMsg("Failed to set stream Sound volume: Return value "+st\chn)
		Return
	EndIf
	ChannelVolume(st\chn,volume*1.0)
End Function

Function SetStreamPaused_Strict(streamHandle%,paused%)
	Local st.Stream = Object.Stream(streamHandle)
	
	If st = Null
		;CreateConsoleMsg("Failed to pause/unpause stream Sound: Unknown Stream")
		Return
	EndIf
	If st\chn=0 Lor st\chn=-1
		CreateConsoleMsg("Failed to pause/unpause stream Sound: Return value "+st\chn)
		Return
	EndIf
	If paused Then
		PauseChannel(st\chn)
	Else
		ResumeChannel(st\chn)
	EndIf
End Function

Function SetStreamPan_Strict(streamHandle%,pan#)
	Local st.Stream = Object.Stream(streamHandle)
	
	If st = Null
		;CreateConsoleMsg("Failed to stop stream Sound: Unknown Stream")
		Return
	EndIf
	If st\chn=0 Lor st\chn=-1
		CreateConsoleMsg("Failed to find stream Sound: Return value "+st\chn)
		Return
	EndIf
	
	;-1 = Left
	;0 = Middle
	;1 = Right
	ChannelPan(st\chn,pan#)
	
End Function

Function IsStreamPlaying_Strict(streamHandle%)
	Local st.Stream = Object.Stream(streamHandle)
	
	If st = Null
		;CreateConsoleMsg("Failed to stop stream Sound: Unknown Stream")
		Return
	EndIf
	If st\chn=0 Lor st\chn=-1
		CreateConsoleMsg("Failed to find stream Sound: Return value "+st\chn)
		Return
	EndIf
	
	Return ChannelPlaying(st\chn)
	
End Function

Function UpdateStreamSoundOrigin(streamHandle%,cam%,entity%,range#=10,volume#=1.0)
	;Local st.Stream = Object.Stream(streamHandle)
	range# = Max(range,1.0)
	
	If volume>0 Then
		
		Local dist# = EntityDistance(cam, entity) / range#
		If 1 - dist# > 0 And 1 - dist# < 1 Then
			
			Local panvalue# = Sin(-DeltaYaw(cam,entity))
			SetStreamVolume_Strict(streamHandle,volume#*(1-dist#)*opt\SFXVolume#)
			SetStreamPan_Strict(streamHandle,panvalue)
		Else
			SetStreamVolume_Strict(streamHandle,0.0)
		EndIf
	Else
		If streamHandle <> 0 Then
			SetStreamVolume_Strict(streamHandle,0.0)
		EndIf 
	EndIf
End Function

Function LoadMesh_Strict(File$,parent=0)
	Local tmp%,i%,sf%,b%,t1%,name$,texture%,t2%
	Local texAlpha% = 0
	Local bumptex%,temp$
	Local mat.Materials
	
	DebugLog "Load mesh: "+File
	
	If I_Loc\Localized And FileType(I_Loc\LangPath + File$)=1 Then
		tmp = LoadMesh(I_Loc\LangPath + File, parent)
	EndIf
	
	If tmp = 0 Then
		If FileType(File$) <> 1 Then RuntimeError "3D Mesh " + File$ + " not found."
		tmp = LoadMesh(File$, parent)
		If tmp = 0 Then RuntimeError "Failed to load 3D Mesh: " + File$
	EndIf
	
	For i = 1 To CountSurfaces(tmp)
		sf = GetSurface(tmp,i)
		b = GetSurfaceBrush(sf)
		If b<>0 Then
			texture=0
			name$=""
			t1=0 : t2=0
			t1 = GetBrushTexture(b,0) ;Diffuse or Lightmap
			If t1<>0
				texAlpha% = isTexAlpha(t1)
				If texAlpha<>2
					If BumpEnabled
;						temp$ = StripPath(TextureName(t1))
;						For mat = Each Materials
;							If mat\name = temp$ Then
;								bumptex = mat\Bump
;								Exit
;							EndIf
;						Next
						bumptex=0
					Else
						bumptex=0
					EndIf
					If bumptex=0
						If FileType(TextureName(t1))=1 ;Check if texture is existing in original path
							name$ = TextureName(t1)
							texture = LoadTextureCheckingIfInCache(name,0,texAlpha)
						ElseIf FileType(MapTexturesFolder+StripPath(TextureName(t1)))=1 ;If not, check the MapTexturesFolder
							name$ = MapTexturesFolder+StripPath(TextureName(t1))
							texture = LoadTextureCheckingIfInCache(name,0,texAlpha)
						EndIf
						If texture<>0
							TextureBlend texture,5
							BrushTexture b, texture, 0, 0
						Else
							;Sometimes that error is intentional - such as if the mesh doesn't has a texture applied
							;or an invalid one which gets fixed by something like EntityTexture
							BrushTexture b, MissingTexture, 0, 0
							;DebugLog "Error trying to apply texture "+Chr(34)+name$+Chr(34)
						EndIf
					Else
						If FileType(TextureName(t1))=1 ;Check if texture is existing in original path
							name$ = TextureName(t1)
							texture = LoadTextureCheckingIfInCache(name,0,texAlpha)
						ElseIf FileType(MapTexturesFolder+StripPath(TextureName(t1)))=1 ;If not, check the MapTexturesFolder
							name$ = MapTexturesFolder+StripPath(TextureName(t1))
							texture = LoadTextureCheckingIfInCache(name,0,texAlpha)
						EndIf
						If texture<>0
							TextureBlend texture,5
							BrushTexture b, texture, 0, 1
						Else
							;Sometimes that error is intentional - such as if the mesh doesn't has a texture applied
							;or an invalid one which gets fixed by something like EntityTexture
							BrushTexture b, MissingTexture, 0, 1
							;DebugLog "Error trying to apply texture "+Chr(34)+name$+Chr(34)
						EndIf
						BrushTexture b, bumptex, 0, 0
					EndIf
				Else
					t2 = GetBrushTexture(b,1) ;Diffuse (if lightmap is existing)
					If BumpEnabled
;						temp$ = StripPath(TextureName(t1))
;						For mat = Each Materials
;							If mat\name = temp$ Then
;								bumptex = mat\Bump
;								Exit
;							EndIf
;						Next
						bumptex=0
					Else
						bumptex=0
					EndIf
					If bumptex=0
						If FileType(TextureName(t1))=1 ;Check if texture is existing in original path
							name$ = TextureName(t1)
							texture = LoadTextureCheckingIfInCache(name,0,1)
						ElseIf FileType(MapTexturesFolder+StripPath(TextureName(t1)))=1 ;If not, check the MapTexturesFolder
							name$ = MapTexturesFolder+StripPath(TextureName(t1))
							texture = LoadTextureCheckingIfInCache(name,0,1)
						EndIf
						If texture<>0
							TextureCoords texture,1
							TextureBlend texture,2
							BrushTexture b, texture, 0, 0
						Else
							BrushTexture b, MissingTexture, 0, 0
						EndIf
						
						If FileType(TextureName(t2))=1 ;Check if texture is existing in original path
							name$ = TextureName(t2)
							texture = LoadTextureCheckingIfInCache(name,0,texAlpha)
						ElseIf FileType(MapTexturesFolder+StripPath(TextureName(t2)))=1 ;If not, check the MapTexturesFolder
							name$ = MapTexturesFolder+StripPath(TextureName(t2))
							texture = LoadTextureCheckingIfInCache(name,0,texAlpha)
						EndIf
						If texture<>0
							TextureCoords texture,0
							TextureBlend texture,5
							BrushTexture b, texture, 0, 1
						Else
							BrushTexture b, MissingTexture, 0, 1
						EndIf
					Else
						If FileType(TextureName(t1))=1 ;Check if texture is existing in original path
							name$ = TextureName(t1)
							texture = LoadTextureCheckingIfInCache(name,0,1)
						ElseIf FileType(MapTexturesFolder+StripPath(TextureName(t1)))=1 ;If not, check the MapTexturesFolder
							name$ = MapTexturesFolder+StripPath(TextureName(t1))
							texture = LoadTextureCheckingIfInCache(name,0,1)
						EndIf
						If texture<>0
							TextureCoords texture,1
							TextureBlend texture,2
							BrushTexture b, texture, 0, 0
						Else
							BrushTexture b, MissingTexture, 0, 0
						EndIf
						
						If FileType(TextureName(t2))=1 ;Check if texture is existing in original path
							name$ = TextureName(t2)
							texture = LoadTextureCheckingIfInCache(name,0,texAlpha)
						ElseIf FileType(MapTexturesFolder+StripPath(TextureName(t2)))=1 ;If not, check the MapTexturesFolder
							name$ = MapTexturesFolder+StripPath(TextureName(t2))
							texture = LoadTextureCheckingIfInCache(name,0,texAlpha)
						EndIf
						If texture<>0
							TextureCoords texture,0
							TextureBlend texture,5
							BrushTexture b, texture, 0, 2
						Else
							BrushTexture b, MissingTexture, 0, 2
						EndIf
						BrushTexture b, bumptex, 0, 1
					EndIf
					FreeTexture t2
				EndIf
				PaintSurface sf,b
				FreeTexture t1
			EndIf
			FreeBrush b
		EndIf
	Next
	Return tmp
End Function   

Function LoadAnimMesh_Strict(File$,parent=0)
	Local tmp%,i%,sf%,b%,t1%,name$,texture%,t2%
	Local texAlpha% = 0
	Local refltex%,temp$
	Local mat.Materials
	
	DebugLog "Load animated mesh: "+File
	
	If I_Loc\Localized And FileType(I_Loc\LangPath + File$)=1 Then
		tmp = LoadAnimMesh(I_Loc\LangPath + File, parent)
	EndIf
	
	If tmp = 0 Then
		If FileType(File$) <> 1 Then RuntimeError "3D Animated Mesh " + File$ + " not found."
		tmp = LoadAnimMesh(File$, parent)
		If tmp = 0 Then RuntimeError "Failed to load 3D Animated Mesh: " + File$
	EndIf
	
	For i = 1 To CountSurfaces(tmp)
		sf = GetSurface(tmp,i)
		b = GetSurfaceBrush(sf)
		If b<>0 Then
			texture=0
			name$=""
			refltex=0
			t1=0 : t2=0
			t1 = GetBrushTexture(b,0) ;Diffuse or Lightmap
			If t1<>0 Then
				texAlpha% = isTexAlpha(t1)
				temp$ = StripPath(TextureName(t1))
				For mat = Each Materials
					If mat\name = temp$ Then
						If mat\Reflection <> 0 Then
							refltex = mat\Reflection
						ElseIf mat\DetailTex <> 0 Then
							refltex = mat\DetailTex
						EndIf
						Exit
					EndIf
				Next
				If refltex=0 Then
					texture = CheckForTexture(t1,texAlpha)
					If texture<>0 Then
						BrushTexture b, texture, 0, 0
					Else
						;Sometimes that error is intentional - such as if the mesh doesn't has a texture applied
						;or an invalid one which gets fixed by something like EntityTexture
						BrushTexture b, MissingTexture, 0, 0
					EndIf
				Else
					texture = CheckForTexture(t1,texAlpha)
					If texture<>0 Then
						BrushTexture b, texture, 0, 1
					Else
						;Sometimes that error is intentional - such as if the mesh doesn't has a texture applied
						;or an invalid one which gets fixed by something like EntityTexture
						BrushTexture b, MissingTexture, 0, 1
					EndIf
					TextureBlend refltex,5
					BrushTexture b, refltex, 0, 0
				EndIf
				PaintSurface sf,b
				FreeTexture t1
			EndIf
			FreeBrush b
		EndIf
	Next
	Return tmp
End Function   

Function FreeEntity_Strict%(entity%)
	
	If entity <> 0 Then
		FreeEntity entity
	EndIf
	
	Return 0
End Function

;don't use in LoadRMesh, as Reg does this manually there. If you wanna fuck around with the logic in that function, be my guest 
Function LoadTexture_Strict(File$,flags=1,texdeletetype%=0)
	If FileType(File$) <> 1 Then RuntimeError "Texture " + File$ + " not found."
	
	Local tmp
	
	If FileType(File$) <> 1 Then RuntimeError "Texture " + File$ + " not found."
	tmp = LoadTextureCheckingIfInCache(File$,texdeletetype,flags)
	
	If tmp = 0 Then RuntimeError "Failed to load Texture: "+File$
	Return tmp 
End Function   

Function LoadBrush_Strict(file$,flags,u#=1.0,v#=1.0)
	Local tmp%
	
	If I_Loc\Localized And FileType(I_Loc\LangPath + file$)=1 Then
		tmp = LoadBrush(I_Loc\LangPath + file, flags, u, v)
	EndIf
	
	If tmp = 0 Then
		If FileType(file$)<>1 Then RuntimeError "Brush Texture " + file + "not found."
		tmp = LoadBrush(file, flags, u, v)
		If tmp = 0 Then RuntimeError "Failed to load Brush: " + file
	EndIf
	Return tmp 
End Function 

Function LoadFont_Strict(file$, height%)
	If FileType(file$)<>1 Then RuntimeError "Font " + file$ + " not found."
	
	tmp = LoadFont(file, height)
	
	If tmp = 0 Then RuntimeError "Failed to load Font: " + file$ 
	Return tmp
End Function









;~IDEal Editor Parameters:
;~C#Blitz3D