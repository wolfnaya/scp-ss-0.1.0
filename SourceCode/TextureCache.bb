
;TextureCache constants
;[Block]
Const MapTexturesFolder$ = "GFX\map\textures\"
Const DeleteMapTextures = 0
Const DeleteTexturesAfterSZL = 1
Const DeleteAllTextures = 2
;[End Block]

Type TextureInCache
	Field tex%
	Field texname$
	Field texdeletetype%
End Type

Function LoadTextureCheckingIfInCache(texname$,deletetype%=DeleteMapTextures,texflags%=1)
	Local tic.TextureInCache,texture%,currpath$
	Local mat.Materials
	
	For tic = Each TextureInCache
		If tic\texname <> "CreateTexture" Then
			If StripPath(texname) = tic\texname Then
				If tic\texdeletetype < deletetype Then
					tic\texdeletetype = deletetype
				EndIf
				Return tic\tex
			EndIf
		EndIf
	Next
	
	currpath$ = texname$
	tic = New TextureInCache
	tic\texname = StripPath(texname$)
	tic\texdeletetype = deletetype
	If I_Loc\Localized And FileType(I_Loc\LangPath + currpath$)=1 Then
		tic\tex = LoadTexture(I_Loc\LangPath + currpath,texflags%);+(256*(SaveTexturesInVRam<>0)))
	EndIf
	If tic\tex = 0 Then
		tic\tex = LoadTexture(currpath,texflags%);+(256*(SaveTexturesInVRam<>0)))
	EndIf
	For mat = Each Materials
		If mat\name = tic\texname Then
			ScaleTexture tic\tex,mat\UScale,mat\VScale
			Exit
		EndIf
	Next
	If tic\tex <> 0 And TextureBuffer(tic\tex) <> 0 Then BufferDirty TextureBuffer(tic\tex)
	Return tic\tex
	
End Function

Function DeleteTextureEntriesFromCache(deletetype%)
	Local tic.TextureInCache,mat.Materials
	
	DebugLog "--------------------------------------------"
	DebugLog "Cache Delete:"
	
	For tic = Each TextureInCache
		If tic\texdeletetype<=deletetype
			If tic\tex<>0 Then FreeTexture tic\tex : tic\tex=0
			DebugLog "Deleted texture "+tic\texname+" from cache."
			Delete tic
		EndIf
	Next
	For mat = Each Materials
		mat\Diff = 0
		mat\Bump = 0
	Next
	
	DebugLog "--------------------------------------------"
	
End Function

Function DeleteSingleTextureEntryFromCache(texture%)
	Local tic.TextureInCache
	
	For tic = Each TextureInCache
		If tic\tex=texture
			If tic\tex<>0 Then FreeTexture tic\tex : tic\tex=0
			DebugLog "Deleted texture "+tic\texname+" from cache."
			Delete tic
		EndIf
	Next
	
End Function

Function CreateTextureUsingCacheSystem(width%,height%,texflags%=1,frames%=1,deletetype%=DeleteAllTextures)
	Local tic.TextureInCache
	
	tic = New TextureInCache
	tic\texname = "CreateTexture"
	tic\texdeletetype = deletetype
	tic\tex = CreateTexture(width%,height%,texflags%+256);(256*(SaveTexturesInVRam<>0)),frames%)
	DebugLog "CreateTexture using Cache system (handle): "+tic\tex
	Return tic\tex
	
End Function

Function isTexAlpha%(tex%,name$="") ;detect transparency in textures
	Local temp1s$
	Local temp%,temp2%,temp3%
	Local mat.Materials
	
	If name$="" Then
		temp1s$=StripPath(TextureName(tex))
	Else
		temp1s$=name$
	EndIf
	
	If Instr(temp1s,"_lm")<>0 Then ;texture is a lightmap
		Return 2
	EndIf
	
	For mat = Each Materials
		If mat\name = temp1s Then
			temp = mat\isDiffuseAlpha
			temp2 = mat\useMask
			temp3 = mat\sphereReflection
			Exit
		EndIf
	Next
	
	DebugLog "***************************************************"
	DebugLog "Texture "+temp1s+" init with flags:"
	DebugLog "Color: 1"
	DebugLog "Alpha: "+temp
	DebugLog "Masked: "+temp2
	DebugLog "Spherical Reflection Map: "+temp3
	DebugLog "***************************************************"
	
	Return 1+(2*(temp<>0))+(4*(temp2<>0)+(64*(temp3<>0)))
End Function

;This is supposed to be the only texture that will be outside the TextureCache system
Global MissingTexture%

Function LoadMissingTexture()
	
	MissingTexture% = CreateTexture(2,2,1)
	TextureBlend MissingTexture,3
	SetBuffer TextureBuffer(MissingTexture)
	ClsColor 0,0,0
	Cls
	SetBuffer BackBuffer()
	
End Function

Function CheckForTexture(tex%,texflags%=1)
	Local name$ = ""
	Local texture
	
	If FileType(TextureName(tex))=1 Then ;Check if texture is existing in original path
		name$ = TextureName(tex)
	ElseIf FileType(MapTexturesFolder+StripPath(TextureName(tex)))=1 Then ;If not, check the MapTexturesFolder
		name$ = MapTexturesFolder+StripPath(TextureName(tex))
	EndIf
	texture = LoadTextureCheckingIfInCache(name,0,texflags)
	If texture <> 0 Then
		If ((texflags Shr 1) Mod 2)=0 Then
			TextureBlend texture,5
		Else
			TextureBlend texture,1
			DebugLog TextureName(texture)+" has Alpha flag!"
		EndIf
	EndIf
	Return texture
	
End Function





;~IDEal Editor Parameters:
;~F#2#9
;~C#Blitz3D