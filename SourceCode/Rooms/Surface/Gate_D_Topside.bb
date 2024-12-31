
Const GATE_D_SURFACE_EZ_Elevator_Obj = 0
Const GATE_D_SURFACE_LCZ_Elevator_Obj = 1
Const GATE_D_SURFACE_Tram_Obj = 2
Const GATE_D_SURFACE_Dark_Sprite = 3

Const GATE_D_SURFACE_LCZ_Elevator_Door = 0
Const GATE_D_SURFACE_HCZ_Elevator_Door = 1
Const GATE_D_SURFACE_BCZ_Elevator_Door = 2
Const GATE_D_SURFACE_EZ_Elevator_Door = 3

Function FillRoom_Gate_D_Topside(r.Rooms)
	Local ne.NewElevator,it.Items,i
	
	Local ElevOBJ% = LoadRMesh("GFX\map\Elevators\elevator_cabin_2.rmesh",Null)
	HideEntity ElevOBJ%
	r\Objects[GATE_D_SURFACE_EZ_Elevator_Obj] = CopyEntity(ElevOBJ%,r\obj)
	r\Objects[GATE_D_SURFACE_LCZ_Elevator_Obj] = CopyEntity(ElevOBJ%,r\obj)
	
	RotateEntity r\Objects[GATE_D_SURFACE_EZ_Elevator_Obj],0,270,0
	PositionEntity(r\Objects[GATE_D_SURFACE_EZ_Elevator_Obj],6705,222,-2491)
	EntityType r\Objects[GATE_D_SURFACE_EZ_Elevator_Obj],HIT_MAP
	EntityPickMode r\Objects[GATE_D_SURFACE_EZ_Elevator_Obj],2
	
	RotateEntity r\Objects[GATE_D_SURFACE_LCZ_Elevator_Obj],0,270,0
	PositionEntity(r\Objects[GATE_D_SURFACE_LCZ_Elevator_Obj],6705,222,2491)
	EntityType r\Objects[GATE_D_SURFACE_LCZ_Elevator_Obj],HIT_MAP
	EntityPickMode r\Objects[GATE_D_SURFACE_LCZ_Elevator_Obj],2
	
	r\RoomDoors[GATE_D_SURFACE_EZ_Elevator_Door] = CreateDoor(r\zone,r\x + 6422.0 * RoomScale, r\y+222*RoomScale, r\z-2491*RoomScale, 90, r, True, DOOR_ELEVATOR_3FLOOR, False, "", 2)
	MoveEntity(r\RoomDoors[GATE_D_SURFACE_EZ_Elevator_Door]\buttons[0], -25, 0, 0)
	r\RoomDoors[GATE_D_SURFACE_EZ_Elevator_Door]\DisableWaypoint = True
	
	ne = CreateNewElevator(r\Objects[GATE_D_SURFACE_EZ_Elevator_Obj],3,r\RoomDoors[GATE_D_SURFACE_EZ_Elevator_Door],2,r,-2800.0,0.0,222.0)
	For i = 0 To 2
		ne\floorlocked[i] = True
	Next
	
	r\RoomDoors[GATE_D_SURFACE_LCZ_Elevator_Door] = CreateDoor(r\zone,r\x + 6422.0 * RoomScale, r\y+222*RoomScale, r\z+2491*RoomScale, 90, r, True, DOOR_ELEVATOR_3FLOOR, False, "", 1)
	MoveEntity(r\RoomDoors[GATE_D_SURFACE_LCZ_Elevator_Door]\buttons[0], -25, 0, 0)
	r\RoomDoors[GATE_D_SURFACE_LCZ_Elevator_Door]\DisableWaypoint = True
	
	ne = CreateNewElevator(r\Objects[GATE_D_SURFACE_LCZ_Elevator_Obj],3,r\RoomDoors[GATE_D_SURFACE_LCZ_Elevator_Door],1,r,-2800.0,0.0,222.0)
	ne\floorlocked[1] = True
	
	CreateDarkSprite(r,GATE_D_SURFACE_Dark_Sprite)
	
	r\RoomDoors[GATE_D_SURFACE_BCZ_Elevator_Door] = CreateDoor(r\zone,r\x + 6422.0 * RoomScale, r\y+222*RoomScale, r\z-1955*RoomScale, 90, r, False, DOOR_ELEVATOR)
	r\RoomDoors[GATE_D_SURFACE_BCZ_Elevator_Door]\DisableWaypoint = True : r\RoomDoors[GATE_D_SURFACE_BCZ_Elevator_Door]\locked = True
	
	r\RoomDoors[GATE_D_SURFACE_HCZ_Elevator_Door] = CreateDoor(r\zone,r\x + 6422.0 * RoomScale, r\y+222*RoomScale, r\z+1955*RoomScale, 90, r, False, DOOR_ELEVATOR)
	r\RoomDoors[GATE_D_SURFACE_HCZ_Elevator_Door]\DisableWaypoint = True : r\RoomDoors[GATE_D_SURFACE_HCZ_Elevator_Door]\locked = True
	
	r\Objects[GATE_D_SURFACE_Tram_Obj] = LoadAnimMesh_Strict("GFX\Map\props\Tram.b3d")
	EntityPickMode(r\Objects[GATE_D_SURFACE_Tram_Obj], 2)
	EntityType(r\Objects[GATE_D_SURFACE_Tram_Obj], HIT_MAP)
	ScaleEntity r\Objects[GATE_D_SURFACE_Tram_Obj], RoomScale*4,RoomScale*4,RoomScale*4
	PositionEntity(r\Objects[GATE_D_SURFACE_Tram_Obj],r\x-3697*RoomScale,r\y-79*RoomScale,r\z+928*RoomScale)
	RotateEntity(r\Objects[GATE_D_SURFACE_Tram_Obj],EntityPitch(r\Objects[GATE_D_SURFACE_Tram_Obj]),EntityYaw(r\Objects[GATE_D_SURFACE_Tram_Obj])-90,EntityRoll(r\Objects[GATE_D_SURFACE_Tram_Obj]))
	EntityParent r\Objects[GATE_D_SURFACE_Tram_Obj], r\obj
	
End Function

Function UpdateEvent_Gate_D_Topside(e.Events)
	Local r.Rooms,n.NPCs,de.Decals,p.Particles
	Local xtemp#,ztemp#,temp%,angle#,dist#,pvt%
	Local i, ne.NewElevator
	
	If PlayerRoom = e\room Then
		
		If gopt\GameMode = GAMEMODE_DEFAULT And opt\IntroEnabled Then
			
			; ~ [Introduction]
			
			If e\EventState[0] = 0 Then
				CreateSplashText("SCP - SECURITY STORIES",opt\GraphicWidth/2,opt\GraphicHeight/2,100,20,Font_Menu_Medium,True,False)
				psp\IsShowingHUD = False
				psp\NoMove = True
				psp\NoRotation = True
				psp\Kevlar = 100
				EntityAlpha e\room\Objects[GATE_D_SURFACE_Dark_Sprite],1
				e\EventState[0] = 1
			EndIf
			
			If e\EventState[0] > 0 Then
				e\EventState[0] = e\EventState[0] + FPSfactor
			EndIf
			
			If e\EventState[0] >= 70*10 Then
				e\EventState[1] = e\EventState[1] - (0.01*FPSfactor)
				EntityAlpha e\room\Objects[GATE_D_SURFACE_Dark_Sprite],Min(1,e\EventState[1]+3)
			EndIf
			
			If e\EventState[0] > 70*10 And e\EventState[0] < 70*10.1 Then
				;If HUDenabled Then
				psp\IsShowingHUD = True
				;EndIf
				psp\NoMove = False
				psp\NoRotation = False
				If (Not TaskExists(TASK_GO_TO_EZ)) Then
					StartChapter("chapter_0", 0)
					BeginTask(TASK_GO_TO_EZ)
				EndIf
			ElseIf e\EventState[0] < 70*10
				psp\IsShowingHUD = False
			EndIf
			
			; ~ [Music]
			
			If e\EventState[0] >= 70*8 Then
				ShouldPlay = MUS_SURFACE
				If e\EventState[4] < 2
					If ChannelPlaying(e\SoundCHN[0])=False Then
						e\SoundCHN[0] = PlaySound_Strict(LoadTempSound("SFX\General\Summer_Ambience.ogg"))
					EndIf
				EndIf
			Else
				ShouldPlay = MUS_NULL
				If e\Sound[0] = 0 Then
					e\Sound[0] = PlaySound_Strict(LoadTempSound("SFX\General\Introduction.ogg"))
				EndIf
			EndIf
			
		Else
			
			r\RoomDoors[GATE_D_SURFACE_LCZ_Elevator_Door]\locked = True
			r\RoomDoors[GATE_D_SURFACE_EZ_Elevator_Door]\locked = True
			
			If e\EventState[0] = 0 Then
				StartChapter("chapter_9", 9)
				e\EventState[0] = 1
			EndIf
			
			If TaskExists(TASK_GET_TOPSIDE) Then
				EndTask(TASK_GET_TOPSIDE)
			EndIf
			
		EndIf
		
		; ~ [Event]
		
		For r.Rooms = Each Rooms
			HideEntity r\obj
		Next					
		ShowEntity e\room\obj
		
		CanSave = False
		
		If e\EventState[2] = 0 Then
			
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
			
			CameraFogMode(Camera, 0)
			SecondaryLightOn = True
			
			HideDistance = 35.0
			
			e\EventState[1] = 0.0
			
			CreateConsoleMsg("WARNING! Teleporting away from this area may cause bugs or crashing.")
			
			If Sky = 0 Then
				If gopt\GameMode = GAMEMODE_DEFAULT And opt\IntroEnabled Then
					Sky = sky_CreateSky("GFX\map\sky\sky_morning")
				Else
					Sky = sky_CreateSky("GFX\map\sky\sky_day")
				EndIf
				RotateEntity Sky,0,e\room\angle,0
			EndIf
			
			DrawLoading(60, False, "", "PLAYER")
			
			If e\room\NPC[0] = Null Then
				e\room\NPC[0] = CreateNPC(NPC_SM4Nn, e\room\x+6110*RoomScale, e\room\y+300*RoomScale, e\room\z-2500*RoomScale)
				RotateEntity(e\room\NPC[0]\Collider,0,e\room\angle-90,0)
				e\room\NPC[0]\State[0] = 0
			EndIf
			
			ResetEntity Collider
			e\EventState[2] = 1.0
			
			DrawLoading(100, False, "", "LOADING_DONE")
			
		Else
			
			If EntityY(Collider) < -200.0*RoomScale
				If TaskExists(TASK_GO_TO_EZ) Then
					CancelTask(TASK_GO_TO_EZ)
					BeginTask(TASK_GO_TO_LCZ)
				EndIf
			EndIf
			If EntityY(Collider) < -2700.0*RoomScale
				
				e\EventState[5] = e\EventState[5] + (0.01*FPSfactor)
				EntityAlpha e\room\Objects[GATE_D_SURFACE_Dark_Sprite],Min(e\EventState[5],1.0)
				
				If e\EventState[5] > 3.0 Then
					gopt\CurrZone = LCZ
					DropSpeed = 0
					NullGame(True,False)
					LoadEntities()
					LoadAllSounds()
					InitNewGame()
					gopt\CurrZone = LCZ
					MainMenuOpen = False
					FlushKeys()
					FlushMouse()
					FlushJoy()
					ResetInput()
					SaveGame(SavePath + CurrSave\Name + "\", True)
					Return
				EndIf
			EndIf
			
			e\EventState[2] = e\EventState[2] + FPSfactor
			HideEntity Fog
			CameraFogRange Camera, 5,50
			
			angle = Max(Sin(EntityYaw(Collider)+90),0.0)
			CameraFogColor(Camera,200+(angle*40),200+(angle*20),200)
			CameraClsColor(Camera,200+(angle*40),200+(angle*20),200)
			CameraRange(Camera, 0.01, 65)
			
			AmbientLight (140, 140, 140)
			
			UpdateSky(Sky)
		EndIf
		
		If e\EventState[4] = 0 Then
			
			; ~ (S|-^|a1Nn)
			
			If EntityDistanceSquared(e\room\NPC[0]\Collider, Collider) < PowTwo(7.0) Then
				If e\room\NPC[0] <> Null Then
					e\room\NPC[0]\State[0] = 1
				EndIf
			EndIf
			
			For ne = Each NewElevator
				If ne\ID = 2 Then
					ne\floorlocked[0] = False
					ne\floorlocked[1] = True
					ne\floorlocked[2] = False
					If Abs(EntityX(e\room\NPC[0]\Collider)-EntityX(ne\obj,True))<=210.0*RoomScale+(0.015*FPSfactor)
						If Abs(EntityZ(e\room\NPC[0]\Collider)-EntityZ(ne\obj,True))<=210.0*RoomScale+(0.015*FPSfactor)
							OpenCloseDoor(e\room\RoomDoors[GATE_D_SURFACE_EZ_Elevator_Door])
							e\room\NPC[0]\State[0] = 0
							PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\fukensmangated")
							If TaskExists(TASK_GO_TO_EZ) Then
								CancelTask(TASK_GO_TO_EZ)
								BeginTask(TASK_GO_TO_LCZ)
							EndIf
							e\EventState[4] = 1
						EndIf
					EndIf
				EndIf
			Next
		ElseIf e\EventState[4] = 1 Then
			If PlayerInNewElevator Then
				e\EventState[4] = 2
				If ChannelPlaying(e\SoundCHN[0]) Then StopChannel(e\SoundCHN[0])
			EndIf
		EndIf
	Else
		HideEntity e\room\obj
	EndIf
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D