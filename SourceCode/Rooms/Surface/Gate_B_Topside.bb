Function FillRoom_Gate_B_Topside(r.Rooms)
	
	r\Objects[0] = CreatePivot()
	PositionEntity r\Objects[0], r\x+10242*RoomScale,r\y+206.28705*RoomScale,r\z-6340.62992*RoomScale,True
	EntityParent r\Objects[0],r\obj
	
	r\Objects[1] = CreatePivot()
	PositionEntity r\Objects[1], r\x+5925*RoomScale,1.0,r\z-847*RoomScale,True
	EntityParent r\Objects[1],r\obj
	
	r\Objects[2] = LoadRMesh("GFX\map\rooms\gate_b_topside\gate_b_topside_hitbox.rmesh",Null)
	PositionEntity r\Objects[2], r\x,r\y,r\z,True
	EntityParent r\Objects[2],r\obj
	HideEntity r\Objects[2]
	
	CreateDarkSprite(r,3)
	
	r\Objects[4] = CreatePivot()
	PositionEntity r\Objects[4], r\x-2588*RoomScale,-510*RoomScale,r\z-1739*RoomScale,True
	EntityParent r\Objects[4],r\obj
	
	splashframes = 20
	splashtex=CreateSplashTexture(500,64,splashframes)
	CreateSplashes3D(10,splashtex,splashframes)
	
	ringframes = 20
	ringtex=CreateRingTexture(50,128,ringframes)
	CreateRings3D(10,ringtex,ringframes)
	
	raintex=CreateRainTexture(50,128) 
	CreateRain3D(90,raintex)
	
End Function

Function UpdateEvent_Gate_B_Topside(e.Events)
	Local r.Rooms,n.NPCs,de.Decals,p.Particles
	Local xtemp#,ztemp#,temp%,angle#,dist#,pvt%
	Local i
	
	If PlayerRoom = e\room Then
		
		ShouldPlay = MUS_NULL
		
		If e\EventState[0] = 0 Then
			StartChapter("chapter_9", 9)
			;AddShopPoints(200)
		EndIf
		
		If TaskExists(TASK_GET_TOPSIDE) Then
			EndTask(TASK_GET_TOPSIDE)
		EndIf
		
		CanSave = False
		
		For r.Rooms = Each Rooms
			HideEntity r\obj
		Next					
		ShowEntity e\room\obj
		
		If e\EventState[0] = 0 Then
			
			DrawLoading(0, False, "")
			
			DrawLoading(30, False, "", "SKY")
			
			For i = 0 To e\room\MaxLights
				If e\room\LightSprites[i]<>0 Then 
					EntityFX e\room\LightSprites[i], 1+8
				EndIf
			Next
			
			For n.NPCs = Each NPCs
				If n <> Curr106 And n <> Curr173
					RemoveNPC(n)
				EndIf
			Next
			Curr173\Idle = True
			Curr106\Idle = True
			Curr106\Contained = True
			
			CameraFogMode(Camera, 0)
			SecondaryLightOn = True
			
			HideDistance = 35.0
			
			TranslateEntity(e\room\obj, 0,12000.0*RoomScale,0)
			TranslateEntity(Collider, 0,12000.0*RoomScale,0)
			
			Sky = sky_CreateSky("GFX\map\sky\sky_foggy")
			RotateEntity Sky,0,e\room\angle,0
			
			DrawLoading(60, False, "", "PLAYER")
			
			ResetEntity Collider
			
			;PlayAnnouncement("SFX\Character\MTF2\Announc.ogg")
			
			e\EventState[0] = 1
			
			RotateEntity Collider,0,EntityYaw(Collider)+(e\room\angle+180),0
			
			DrawLoading(100, False, "", "LOADING_DONE")
		Else
			
			If Sky = 0 Then
				Sky = sky_CreateSky("GFX\map\sky\sky_foggy")
				RotateEntity Sky,0,e\room\angle,0
			EndIf
			
			e\EventState[0] = e\EventState[0] + FPSfactor
			
			ShowEntity Fog
			CameraFogRange Camera, 5,50
			
			angle = Max(Sin(EntityYaw(Collider)+90),0.0)
			If e <> Null Then
				CameraFogColor (Camera,190+(angle*40),190+(angle*20),190)
				CameraClsColor (Camera,190+(angle*40),190+(angle*20),190)
			EndIf
			CameraRange(Camera, 0.01, 65)
			
			AmbientLight (190, 190, 190)
			
			UpdateSky(Sky)
			
			If e\EventState[1] > 0 Then
				UpdateSplashes3D()
				UpdateRings3D()
				UpdateRain3D()
			EndIf
			
			If e\EventState[1] = 0 Then
				
				If ChannelPlaying(e\SoundCHN[0])=False Then
					e\SoundCHN[0] = PlaySound_Strict(LoadTempSound("SFX\General\Rain.ogg"))
				EndIf
				
				If EntityDistanceSquared(Collider,e\room\Objects[1])<PowTwo(1.5) Then
					PlayAnnouncement("SFX\Intercom\MTF\NTF\AnnouncExposure.ogg")
					e\EventState[1] = 1
					;PlaySound_Strict(LoadTempSound("SFX\Ending\GateB\682Battle.ogg"))
				EndIf
			EndIf
			
			If e\EventState[1] = 1 Then
				If ChannelPlaying(e\SoundCHN[0]) Then StopChannel(e\SoundCHN[0])
				If ChannelPlaying(e\SoundCHN[1])=False Then
					e\SoundCHN[1] = PlaySound_Strict(LoadTempSound("SFX\General\Rain_2.ogg"))
				EndIf
				
				If e\room\NPC[0] = Null Then
					e\room\NPC[0] = CreateNPC(NPC_SCP_682,EntityX(e\room\Objects[4],True),EntityY(e\room\Objects[4],True),EntityZ(e\room\Objects[4],True))
					;e\room\NPC[0]\State = SCP682_WALK
				EndIf
				
				ShowEntity e\room\Objects[2]
				EntityAlpha e\room\Objects[2],0
				If EntityDistanceSquared(Collider,e\room\Objects[0])<PowTwo(2) Then
					e\SoundCHN[0] = LoopSound2(TruckSFX, e\SoundCHN[0], Camera, e\room\Objects[0], 10.0, 1)
					DrawHandIcon = True
					If KeyHitUse Then
						e\EventState[1] = 2
					EndIf
				EndIf
			EndIf
			
			If e\EventState[1] = 2 Then
				e\EventState[2] = e\EventState[2] + (FPSfactor*0.01)
				EntityAlpha e\room\Objects[3],Min(e\EventState[2],1)
			EndIf
			
			If e\EventState[2] >= 1 Then
				If e\room\NPC[0] <> Null Then
					RemoveNPC(e\room\NPC[0])
				EndIf
				If ChannelPlaying(e\SoundCHN[1]) Then StopChannel(e\SoundCHN[1])
				SelectedEnding = "b1"
				psp\Health = 1000
				psp\IsShowingHUD = False
				RemoveEvent(e)
				Return
			EndIf
			
		EndIf
	Else
		HideEntity e\room\obj
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D