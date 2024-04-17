Function Graphics3DExt%(width%,height%,depth%=32,mode%=2)
	
	Graphics3D width,height,depth,mode
	SetGfxDriver(CountGfxDrivers())
	TextureFilter "", 8192 ;This turns on Anisotropic filtering for textures. Use TextureAnisotropic to change anisotropic level.
	InitFastResize()
	
End Function

Function ResizeImage2(image%,width%,height%)
	Local img%, oldWidth%, oldHeight%
	
    img% = CreateImage(width,height)
	
	oldWidth% = ImageWidth(image)
	oldHeight% = ImageHeight(image)
	CopyRect 0,0,oldWidth,oldHeight,2048-oldWidth/2,2048-oldHeight/2,ImageBuffer(image),TextureBuffer(fresize_texture)
	SetBuffer BackBuffer()
	ScaleRender(0,0,4096.0 / Float(RealGraphicWidth) * Float(width) / Float(oldWidth), 4096.0 / Float(RealGraphicWidth) * Float(height) / Float(oldHeight))
	;might want to replace Float(opt\GraphicWidth) with Max(opt\GraphicWidth,opt\GraphicHeight) if portrait sizes cause issues
	;everyone uses landscape so it's probably a non-issue
	CopyRect RealGraphicWidth/2-width/2,RealGraphicHeight/2-height/2,width,height,0,0,BackBuffer(),ImageBuffer(img)
	
    FreeImage image
    Return img
End Function

Const NVBrightness# = 30.0

Function RenderWorld2%(Tween#)
	Local np.NPCs
	Local i%, k%, l%
	
	CameraProjMode(ark_blur_cam, 0)
	CameraProjMode(Camera, 1)
	
	If mpl\NightVisionEnabled Then
		AmbientLight(Min(NVBrightness * 2.0, 255.0), Min(NVBrightness * 2.0, 255.0), Min(NVBrightness * 2.0, 255.0))
	EndIf
	
	If Inventory[SLOT_HEAD] <> Null Then
		If Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg" Lor Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg2" Then
			AmbientLight Min(Brightness*2,255), Min(Brightness*2,255), Min(Brightness*2,255)
		ElseIf Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg3"
			AmbientLight 255,255,255
		ElseIf PlayerRoom<>Null
			If (PlayerRoom\RoomTemplate\Name<>"gate_a_topside") And (PlayerRoom\RoomTemplate\Name<>"gate_b_topside") And (PlayerRoom\RoomTemplate\Name<>"gate_c_topside") Then
				AmbientLight Brightness, Brightness, Brightness
			EndIf
		EndIf
	ElseIf PlayerRoom<>Null
		If (PlayerRoom\RoomTemplate\Name<>"gate_a_topside") And (PlayerRoom\RoomTemplate\Name<>"gate_b_topside") And (PlayerRoom\RoomTemplate\Name<>"gate_c_topside") Then
			AmbientLight Brightness, Brightness, Brightness
		EndIf
	EndIf
	
	CameraViewport(Camera, 0, 0, opt\GraphicWidth, opt\GraphicHeight)
	
	Local HasBattery%
	Local Power%
	
	If Inventory[SLOT_HEAD] <> Null Then
		If Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg" Lor Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg2" Lor Inventory[SLOT_HEAD]\itemtemplate\tempname = "scramble" Then
			Power = Int(Inventory[SLOT_HEAD]\state)
			If Power = 0 Then ; ~ This NVG or SCRAMBLE can't be used
				HasBattery = 0
			ElseIf Power <= 100
				HasBattery = 1
			Else
				HasBattery = 2
			EndIf
		EndIf
	EndIf
	
	If (Not IsNVGBlinking) Then RenderWorld(Tween)
	
	CurrTrisAmount = TrisRendered()
	
	If mpl\HasNTFGasmask = 2 And mpl\NightVisionEnabled Then ; ~ Show a HUD
		Color(255, 255, 255)
		
		SetFont fo\Font[Font_Digital_Large]
		
		Local PlusY% = 0
		
		PlusY = 40
		
		Text opt\GraphicWidth/2,(20+PlusY)*MenuScale,GetLocalString("Devices","nvg_refresh_1"),True,False
		Text opt\GraphicWidth/2,(60+PlusY)*MenuScale,Max(f2s(wbl\NVGTimer/60.0,1),0.0),True,False
		Text opt\GraphicWidth/2,(100+PlusY)*MenuScale,GetLocalString("Devices","nvg_refresh_2"),True,False
		
		Local Temp% = CreatePivot()
		Local Temp2% = CreatePivot()
		
		PositionEntity(Temp, EntityX(Collider), EntityY(Collider), EntityZ(Collider))
		
		Color(255, 255, 255)
		
		For np.NPCs = Each NPCs
			SetFont fo\Font[Font_Digital_Medium]
			If np\NVName <> "" And (Not np\HideFromNVG) Then ; ~ Don't waste your time if the string is empty
				PositionEntity(Temp2, np\NVX, np\NVY, np\NVZ)
				
				Local Dist# = EntityDistanceSquared(Temp2, Collider)
				
				If Dist < 552.25 Then ; ~ Don't draw text if the NPC is too far away
					PointEntity(Temp, Temp2)
					
					Local YawValue# = WrapAngle(EntityYaw(Camera) - EntityYaw(Temp))
					Local xValue# = 0.0
					
					If YawValue > 90.0 And YawValue <= 180.0 Then
						xValue = Sin(90.0) / 90.0 * YawValue
					ElseIf YawValue > 180 And YawValue < 270.0
						xValue = Sin(270.0) / YawValue * 270.0
					Else
						xValue = Sin(YawValue)
					EndIf
					
					Local PitchValue# = WrapAngle(EntityPitch(Camera) - EntityPitch(Temp))
					Local yValue# = 0.0
					
					If PitchValue > 90.0 And PitchValue <= 180.0 Then
						yValue = Sin(90.0) / 90.0 * PitchValue
					ElseIf PitchValue > 180.0 And PitchValue < 270
						yValue = Sin(270.0) / PitchValue * 270.0
					Else
						yValue = Sin(PitchValue)
					EndIf
					
					If (Not IsNVGBlinking) Then
						Text opt\GraphicWidth / 2 + xValue * (opt\GraphicWidth / 2),opt\GraphicHeight / 2 - yValue * (opt\GraphicHeight / 2),np\NVName,True,True
						Text opt\GraphicWidth / 2 + xValue * (opt\GraphicWidth / 2),opt\GraphicHeight / 2 - yValue * (opt\GraphicHeight / 2) + 30.0 * MenuScale,f2s(Dist,1)+" m",True,True
					EndIf
				EndIf
			EndIf
		Next
		
		FreeEntity(Temp)
		FreeEntity(Temp2)
		
		Color 0, 0, 55
	EndIf
	
	If HasBattery > 0 Then
		If Inventory[SLOT_HEAD] <> Null Then
			If Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg2" Then ; ~ Show a HUD
				Color(255, 255, 255)
				
				SetFont fo\Font[Font_Digital_Large]
				
				PlusY% = 0
				
				If HasBattery = 1 Then PlusY = 40
				
				Text opt\GraphicWidth/2,(20+PlusY)*MenuScale,GetLocalString("Devices","nvg_refresh_1"),True,False
				Text opt\GraphicWidth/2,(60+PlusY)*MenuScale,Max(f2s(wbl\NVGTimer/60.0,1),0.0),True,False
				Text opt\GraphicWidth/2,(100+PlusY)*MenuScale,GetLocalString("Devices","nvg_refresh_2"),True,False
				
				Temp% = CreatePivot()
				Temp2% = CreatePivot()
				
				PositionEntity(Temp, EntityX(Collider), EntityY(Collider), EntityZ(Collider))
				
				Color(255, 255, 255)
				
				For np.NPCs = Each NPCs
					SetFont fo\Font[Font_Digital_Medium]
					If np\NVName <> "" And (Not np\HideFromNVG) Then ; ~ Don't waste your time if the string is empty
						PositionEntity(Temp2, np\NVX, np\NVY, np\NVZ)
						
						Dist# = EntityDistanceSquared(Temp2, Collider)
						
						If Dist < 552.25 Then ; ~ Don't draw text if the NPC is too far away
							PointEntity(Temp, Temp2)
							
							YawValue# = WrapAngle(EntityYaw(Camera) - EntityYaw(Temp))
							xValue# = 0.0
							
							If YawValue > 90.0 And YawValue <= 180.0 Then
								xValue = Sin(90.0) / 90.0 * YawValue
							ElseIf YawValue > 180 And YawValue < 270.0
								xValue = Sin(270.0) / YawValue * 270.0
							Else
								xValue = Sin(YawValue)
							EndIf
							
							PitchValue# = WrapAngle(EntityPitch(Camera) - EntityPitch(Temp))
							yValue# = 0.0
							
							If PitchValue > 90.0 And PitchValue <= 180.0 Then
								yValue = Sin(90.0) / 90.0 * PitchValue
							ElseIf PitchValue > 180.0 And PitchValue < 270
								yValue = Sin(270.0) / PitchValue * 270.0
							Else
								yValue = Sin(PitchValue)
							EndIf
							
							If (Not IsNVGBlinking) Then
								Text opt\GraphicWidth / 2 + xValue * (opt\GraphicWidth / 2),opt\GraphicHeight / 2 - yValue * (opt\GraphicHeight / 2),np\NVName,True,True
								Text opt\GraphicWidth / 2 + xValue * (opt\GraphicWidth / 2),opt\GraphicHeight / 2 - yValue * (opt\GraphicHeight / 2) + 30.0 * MenuScale,f2s(Dist,1)+" m",True,True
							EndIf
						EndIf
					EndIf
				Next
				
				FreeEntity(Temp)
				FreeEntity(Temp2)
				
				Color 0, 0, 55
			ElseIf Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg" Then
				Color 0, 55, 0
			ElseIf Inventory[SLOT_HEAD]\itemtemplate\tempname = "scramble" Then
				Color 55, 55, 55
			EndIf
			For k = 0 To 10
				Rect 45,opt\GraphicHeight*0.5-(k*20),54,10,True
			Next
			If Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg2" Then
				Color 0, 0, 255
			ElseIf Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg" Then
				Color 0, 255, 0
			ElseIf Inventory[SLOT_HEAD]\itemtemplate\tempname = "scramble" Then
				Color 255, 255, 255
			EndIf
			For l = 0 To Min(Floor((Power + 50) * 0.01), 11)
				Rect 45,opt\GraphicHeight*0.5-(l*20),54,10,True
			Next
			DrawImage NVGImages,40,opt\GraphicHeight*0.5+30,0
		EndIf
	EndIf
	
	; ~ Render sprites
	CameraProjMode ark_blur_cam,2
	CameraProjMode Camera,0
	RenderWorld()
	CameraProjMode ark_blur_cam,0
	
	If FPSfactor > 0.0 Then
		If HasBattery = 1 And ((MilliSecs() Mod 800) < 400)
			Color 255, 0, 0
			SetFont fo\Font[Font_Digital_Large]
			
			Text opt\GraphicWidth/2,20*MenuScale,GetLocalString("Devices","nvg_low_bat"),True,False
			Color 255,255,255
		EndIf
	EndIf
End Function

Function UpdateWorld2%()
	Local np.NPCs
	Local i%
	
	IsNVGBlinking = False
	
	Local HasBattery%
	Local Power%
	
	If Inventory[SLOT_HEAD] <> Null Then
		
		If Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg" Lor Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg2" Lor Inventory[SLOT_HEAD]\itemtemplate\tempname = "scramble" Then
			
			If Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg" Lor Inventory[SLOT_HEAD]\itemtemplate\tempname = "scramble" Then
				Inventory[SLOT_HEAD]\state = Max(0.0, Inventory[SLOT_HEAD]\state - (FPSfactor * (0.02) + (0.15)))
			ElseIf Inventory[SLOT_HEAD] <> Null And Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg2" Then
				Inventory[SLOT_HEAD]\state = Max(0.0, Inventory[SLOT_HEAD]\state - (FPSfactor * (0.02 * 2) + (0.15)))
			EndIf
			
			Power = Int(Inventory[SLOT_HEAD]\state)
			If Power = 0 Then ; ~ This NVG or SCRAMBLE can't be used
				HasBattery = 0
				If Inventory[SLOT_HEAD]\itemtemplate\tempname = "scramble" Then
					CreateMsg(GetLocalString("Items","scramble_died"))
				ElseIf Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 3) = "nvg" Then
					CreateMsg(GetLocalString("Items","nvg_died"))
				EndIf
				;IsNVGBlinking = True
			ElseIf Power <= 100
				HasBattery = 1
			Else
				HasBattery = 2
			EndIf
			
			If Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg2" Then
				If wbl\NVGTimer <= 0.0 Then
					For np.NPCs = Each NPCs
						np\NVX = EntityX(np\Collider, True)
						np\NVY = EntityY(np\Collider, True)
						np\NVZ = EntityZ(np\Collider, True)
					Next
					If wbl\NVGTimer <= -10.0 Then wbl\NVGTimer = 600.0
					;IsNVGBlinking = True
				EndIf
				wbl\NVGTimer = wbl\NVGTimer - FPSfactor
			EndIf
			
		EndIf
		
	EndIf
	
	If mpl\HasNTFGasmask = 2 And mpl\NightVisionEnabled Then
		
		If wbl\NVGTimer <= 0.0 Then
			For np.NPCs = Each NPCs
				np\NVX = EntityX(np\Collider, True)
				np\NVY = EntityY(np\Collider, True)
				np\NVZ = EntityZ(np\Collider, True)
			Next
			If wbl\NVGTimer <= -10.0 Then wbl\NVGTimer = 600.0
			;IsNVGBlinking = True
		EndIf
		wbl\NVGTimer = wbl\NVGTimer - FPSfactor
		
	EndIf
	
	If Inventory[SLOT_HEAD] <> Null Then
		If Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 8) = "scramble" Then
			If Inventory[SLOT_HEAD]\itemtemplate\tempname = "scramble" Then
				If HasBattery <> 0 Then
					If (Not ChannelPlaying(wbl\SCRAMBLECHN)) Then
						wbl\SCRAMBLECHN = PlaySound_Strict(wbl\SCRAMBLESFX)
					EndIf
				Else
					StopChannel(wbl\SCRAMBLECHN)
				EndIf
			Else
				If (Not ChannelPlaying(wbl\SCRAMBLECHN)) Then
					wbl\SCRAMBLECHN = PlaySound_Strict(wbl\SCRAMBLESFX)
				EndIf
			EndIf
		Else
			StopChannel(wbl\SCRAMBLECHN)
		EndIf
	Else
		If ChannelPlaying(wbl\SCRAMBLECHN) Then StopChannel(wbl\SCRAMBLECHN)
	EndIf
	
	If FPSfactor > 0.0 Then
		If HasBattery = 1 And ((MilliSecs() Mod 800) < 200) Then
			If (Not ChannelPlaying(LowBatteryCHN[1])) Then
				LowBatteryCHN[1] = PlaySound_Strict(LowBatterySFX[1])
			ElseIf (Not ChannelPlaying(LowBatteryCHN[1])) Then
				LowBatteryCHN[1] = PlaySound_Strict(LowBatterySFX[1])
			EndIf
		EndIf
	EndIf
	
End Function

Function ScaleRender(x#,y#,hscale#=1.0,vscale#=1.0)
	If Camera<>0 Then HideEntity Camera
	WireFrame 0
	ShowEntity fresize_image
	ScaleEntity fresize_image,hscale,vscale,1.0
	PositionEntity fresize_image, x, y, 1.0001
	ShowEntity fresize_cam
	RenderWorld()
	HideEntity fresize_cam
	HideEntity fresize_image
	WireFrame WireframeState
	If Camera<>0 Then ShowEntity Camera
End Function

Function InitFastResize()
    ;Create Camera
	Local cam% = CreateCamera()
	CameraProjMode cam, 2
	CameraZoom cam, 0.1
	CameraClsMode cam, 0, 0
	CameraRange cam, 0.1, 1.5
	MoveEntity cam, 0, 0, -10000
	
	fresize_cam = cam
	
    ;ark_sw = GraphicsWidth()
    ;ark_sh = GraphicsHeight()
	
    ;Create sprite
	Local spr% = CreateMesh(cam)
	Local sf% = CreateSurface(spr)
	AddVertex sf, -1, 1, 0, 0, 0
	AddVertex sf, 1, 1, 0, 1, 0
	AddVertex sf, -1, -1, 0, 0, 1
	AddVertex sf, 1, -1, 0, 1, 1
	AddTriangle sf, 0, 1, 2
	AddTriangle sf, 3, 2, 1
	EntityFX spr, 17
	ScaleEntity spr, 2048.0 / Float(RealGraphicWidth), 2048.0 / Float(RealGraphicHeight), 1
	PositionEntity spr, 0, 0, 1.0001
	EntityOrder spr, -100001
	EntityBlend spr, 1
	fresize_image = spr
	
    ;Create texture
	fresize_texture = CreateTexture(4096, 4096, 1+256)
	fresize_texture2 = CreateTexture(4096, 4096, 1+256)
	TextureBlend fresize_texture2,3
	If fresize_texture2<>0 Then
		SetBuffer(TextureBuffer(fresize_texture2))
	EndIf
	ClsColor 0,0,0
	Cls
	SetBuffer(BackBuffer())
	;TextureAnisotropy(fresize_texture)
	EntityTexture spr, fresize_texture,0,0
	EntityTexture spr, fresize_texture2,0,1
	
	HideEntity fresize_cam
End Function

Function GammaUpdate()
	
	If opt\DisplayMode=1 Then
		If (RealGraphicWidth<>opt\GraphicWidth) Lor (RealGraphicHeight<>opt\GraphicHeight) Then
			SetBuffer TextureBuffer(fresize_texture)
			ClsColor 0,0,0 : Cls
			CopyRect 0,0,opt\GraphicWidth,opt\GraphicHeight,2048-opt\GraphicWidth/2,2048-opt\GraphicHeight/2,BackBuffer(),TextureBuffer(fresize_texture)
			SetBuffer BackBuffer()
			ClsColor 0,0,0 : Cls
			ScaleRender(0,0,4096.0 / Float(opt\GraphicWidth) * AspectRatioRatio, 4096.0 / Float(opt\GraphicWidth) * AspectRatioRatio)
		EndIf
	EndIf
	If ScreenGamma>=1.0 Then
		CopyRect 0,0,RealGraphicWidth,RealGraphicHeight,2048-RealGraphicWidth/2,2048-RealGraphicHeight/2,BackBuffer(),TextureBuffer(fresize_texture)
		EntityBlend fresize_image,1
		ClsColor 0,0,0 : Cls
		ScaleRender(-1.0/Float(RealGraphicWidth),1.0/Float(RealGraphicWidth),4096.0 / Float(RealGraphicWidth),4096.0 / Float(RealGraphicWidth))
		EntityFX fresize_image,1+32
		EntityBlend fresize_image,3
		EntityAlpha fresize_image,ScreenGamma-1.0
		ScaleRender(-1.0/Float(RealGraphicWidth),1.0/Float(RealGraphicWidth),4096.0 / Float(RealGraphicWidth),4096.0 / Float(RealGraphicWidth))
	ElseIf ScreenGamma<1.0 Then
		CopyRect 0,0,RealGraphicWidth,RealGraphicHeight,2048-RealGraphicWidth/2,2048-RealGraphicHeight/2,BackBuffer(),TextureBuffer(fresize_texture)
		EntityBlend fresize_image,1
		ClsColor 0,0,0 : Cls
		ScaleRender(-1.0/Float(RealGraphicWidth),1.0/Float(RealGraphicWidth),4096.0 / Float(RealGraphicWidth),4096.0 / Float(RealGraphicWidth))
		EntityFX fresize_image,1+32
		EntityBlend fresize_image,2
		EntityAlpha fresize_image,1.0
		SetBuffer TextureBuffer(fresize_texture2)
		ClsColor 255*ScreenGamma,255*ScreenGamma,255*ScreenGamma
		Cls
		SetBuffer BackBuffer()
		ScaleRender(-1.0/Float(RealGraphicWidth),1.0/Float(RealGraphicWidth),4096.0 / Float(RealGraphicWidth),4096.0 / Float(RealGraphicWidth))
		SetBuffer(TextureBuffer(fresize_texture2))
		ClsColor 0,0,0
		Cls
		SetBuffer(BackBuffer())
	EndIf
	EntityFX fresize_image,1
	EntityBlend fresize_image,1
	EntityAlpha fresize_image,1.0
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D