
Function FillRoom_Gate_C_Entrance(r.Rooms)
	Local ne.NewElevator
	
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x+1048.0*RoomScale, 0, r\z+656.0*RoomScale, True)
	EntityParent r\Objects[0], r\obj
	
	r\Objects[1] = LoadRMesh("GFX\map\Elevators\elevator.rmesh",Null)
	r\Objects[2] = CopyEntity(r\Objects[1],r\obj)
	
	PositionEntity(r\Objects[1],576,0,640)
	EntityType r\Objects[1],HIT_MAP
	EntityPickMode r\Objects[1],2
	
	RotateEntity r\Objects[2],0,90,0
	PositionEntity(r\Objects[2],-6.0,0,4416.0)
	EntityType r\Objects[2],HIT_MAP
	EntityPickMode r\Objects[2],2
	
	r\RoomDoors[2] = CreateDoor(r\zone,r\x-688*RoomScale,r\y,r\z -608.0*RoomScale,270,r,False,DOOR_OFFICE)
	r\RoomDoors[2]\locked = True
	r\RoomDoors[3] = CreateDoor(r\zone,r\x+688*RoomScale,r\y,r\z+324.0*RoomScale,90,r,False,DOOR_OFFICE)
	r\RoomDoors[3]\locked = True
	
	CreateDarkSprite(r,3)
	
	r\Objects[4] = LoadMesh_Strict("GFX\map\rooms\gate_c_entrance\gate_c_entrance.b3d",r\obj)
	EntityPickMode r\Objects[4],2
	EntityType r\Objects[4],HIT_MAP
	EntityFX r\Objects[4], 2
	LightMesh r\Objects[4],-120,-120,-120
	
	r\Objects[5] = LoadMesh_Strict("GFX\map\alarm_siren.b3d")
	ScaleEntity r\Objects[5],RoomScale,RoomScale,RoomScale
	PositionEntity r\Objects[5],r\x,r\y+928.0*RoomScale,r\z-40.0*RoomScale,True
	EntityParent r\Objects[5],r\obj
	
	r\Objects[6] = LoadMesh_Strict("GFX\map\Props\alarm_upsidedown.b3d")
	ScaleEntity r\Objects[6],RoomScale,RoomScale,RoomScale
	PositionEntity r\Objects[6],r\x,r\y+908.0*RoomScale,r\z-40.0*RoomScale,True
	EntityParent r\Objects[6],r\obj
	ScaleEntity r\Objects[6],150,150,150
	
	r\Objects[7] = CreatePivot(); ~ Teleportation Trigger 
	PositionEntity(r\Objects[7], r\x-6.0*RoomScale, r\y+2500*RoomScale, r\z+4416.0*RoomScale, True)
	EntityParent r\Objects[7], r\obj
	
	r\Objects[8] = CreatePivot()
	PositionEntity(r\Objects[8], r\x+18.0*RoomScale, r\y+164*RoomScale, r\z+3547.0*RoomScale, True)
	EntityParent r\Objects[8], r\obj
	
	r\Objects[9]=LoadAnimMesh_Strict("GFX\Map\Props\Satellite_Rocket.b3d"); ~ Rocket Itself
	ScaleEntity r\Objects[9], RoomScale*50,RoomScale*50,RoomScale*50
	PositionEntity(r\Objects[9], r\x, r\y+3800.0*RoomScale, r\z+3005.0*RoomScale, True)
	MeshCullBox (r\Objects[9], -MeshWidth(r\Objects[9]), -MeshHeight(r\Objects[9]), -MeshDepth(r\Objects[9]), MeshWidth(r\Objects[9])*2, MeshHeight(r\Objects[9])*2, MeshDepth(r\Objects[9])*2)
	EntityParent r\Objects[9], r\obj
	
	r\Objects[10]=LoadAnimMesh_Strict("GFX\Map\Props\gate_c_rocket_elevator.b3d"); ~ Rocket Elevator
	ScaleEntity r\Objects[10], RoomScale*50,RoomScale*50,RoomScale*50
	RotateEntity r\Objects[10],0,90,0
	PositionEntity(r\Objects[10], r\x-5*RoomScale, r\y+3800.0*RoomScale, r\z+3005.0*RoomScale, True)
	MeshCullBox (r\Objects[10], -MeshWidth(r\Objects[10]), -MeshHeight(r\Objects[10]), -MeshDepth(r\Objects[10]), MeshWidth(r\Objects[10])*2, MeshHeight(r\Objects[10])*2, MeshDepth(r\Objects[10])*2)
	EntityParent r\Objects[10], r\obj
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x -6.0 * RoomScale, r\y, r\z+4102*RoomScale, 180, r, True, 5, -1, "", 2)
	r\RoomDoors[0]\DisableWaypoint = True
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x, 0, r\z + 1008.0 * RoomScale, 0, r, False, True, KEY_CARD_5)
	PositionEntity(r\RoomDoors[1]\buttons[1], r\x+416*RoomScale, r\y + 0.7, r\z+1200.0*RoomScale, True)
	RotateEntity r\RoomDoors[1]\buttons[0],90,0,0,True
	PositionEntity(r\RoomDoors[1]\buttons[0], r\x, r\y+173*RoomScale, r\z+486*RoomScale, True)
	r\RoomDoors[1]\MTFClose = False
	
	If gopt\GameMode = GAMEMODE_DEFAULT Then
		r\RoomDoors[1]\dir = 1 : r\RoomDoors[1]\AutoClose = False
	Else
		r\RoomDoors[1]\dir = 1 : r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\locked = True
	EndIf
	
	r\AlarmRotor[0] = r\Objects[6]
	r\AlarmRotorLight[0] = CreateLight(3,r\Objects[6])
	MoveEntity r\AlarmRotorLight[0],0,0,0.001
	LightRange r\AlarmRotorLight[0],1.5
	LightColor r\AlarmRotorLight[0],255*3,0,0
	RotateEntity r\AlarmRotorLight[0],45,0,0
	LightConeAngles r\AlarmRotorLight[0],0,75
	
	ne = CreateNewElevator(r\Objects[2],1,r\RoomDoors[0],2,r,0.0,2000.0,3000.0)
	ne\floorlocked[1] = True
	
End Function

Function UpdateEvent_Gate_C_Entrance(e.Events)
	Local i%, tex%
	Local ne.NewElevator,r.Rooms,e2.Events
	Local playerElev%,prevZone%
	
	If gopt\GameMode = GAMEMODE_DEFAULT Then
		If PlayerRoom = e\room Then
			
			If e\EventState[1] = 0 Then
				If TaskExists(TASK_GET_TOPSIDE) And (Not ecst\UnlockedGateDoors) Then
					FailTask(TASK_GET_TOPSIDE)
					BeginTask(TASK_FIND_ROOM1_O5)
					e\EventState[1] = 1
				EndIf
			EndIf
			
			If ecst\UnlockedGateDoors Then
				e\room\RoomDoors[1]\locked = False
			Else
				e\room\RoomDoors[1]\locked = True
			EndIf
			
			If e\EventState[2] = 0 Then
				e\EventState[2] = 1
			EndIf
			If e\EventState[2] = 1 And e\room\RoomDoors[1]\open = True Then
				e\SoundCHN[0] = PlaySound2(LoadTempSound("SFX\Door\BigDoorStartsOpenning.ogg"),Camera,e\room\RoomDoors[1]\frameobj,10,2.5)
				StopChannel(e\room\RoomDoors[1]\SoundCHN)
				e\EventState[2] = 2
			ElseIf e\EventState[2] = 2 Then
				UpdateSoundOrigin(e\SoundCHN[0],Camera,e\room\RoomDoors[1]\frameobj,10,2.5)
				e\room\RoomDoors[1]\openstate = 0.01
				e\room\RoomDoors[1]\open = False	
			EndIf
			If e\EventState[2] = 2 And (Not ChannelPlaying(e\SoundCHN[0])) Then
				OpenCloseDoor(e\room\RoomDoors[1])
				e\EventState[2] = 3
			EndIf
			
			LightVolume = Min(LightVolume+0.05,TempLightVolume*1.25)
			
			If e\EventState[4] = 0 And EntityY(Collider)>2000*RoomScale Then
				e\EventState[0] = e\EventState[0] + (0.01*FPSfactor)
				EntityAlpha e\room\Objects[3],Min(e\EventState[0],1.0)
			Else
				e\EventState[0] = Max(e\EventState[0] - (0.01*FPSfactor), 0.0)
				EntityAlpha e\room\Objects[3],Min(e\EventState[0],1.0)
			EndIf
			
			If Abs(EntityY(Collider)-EntityY(e\room\Objects[8],True))<1.0 Then
				If DistanceSquared(EntityX(Collider),EntityX(e\room\Objects[8],True),EntityZ(Collider),EntityZ(e\room\Objects[8],True)) < PowTwo(0.8) Then
					If e\EventState[3] <> 1.0 Then
						DrawHandIcon = True
						If KeyHitUse Then
							ecst\SuccessRocketLaunch = True
							If (Not ChannelPlaying(e\SoundCHN[1])) Then
								e\SoundCHN[1] = PlaySound_Strict(LoadTempSound("SFX\Ending\GateC\Rocket_Prepare.ogg"))
							EndIf
							e\EventState[3] = 1.0
						EndIf
					EndIf
				EndIf
			EndIf
			
			If e\EventState[5] = 0 Then
				SetAnimTime(e\room\Objects[9],1231)
				SetAnimTime(e\room\Objects[10],1231)
			ElseIf e\EventState[5] >= 70*25 Then
				SetAnimTime(e\room\Objects[9],1597)
				SetAnimTime(e\room\Objects[10],1597)
			EndIf
			
			If e\EventState[5] >= 70*20 And e\EventState[5] < 70*20.05 Then
				ecst\IntercomIsReady = True
				PlayAnnouncement("SFX\Intercom\Rocket\all_systems_go.ogg")
			EndIf
			
			If e\EventState[3] = 1.0 Then
				e\EventState[5] = e\EventState[5] +FPSfactor
				Animate2(e\room\Objects[9],AnimTime(e\room\Objects[9]),1231,1597,0.24,False)
				Animate2(e\room\Objects[10],AnimTime(e\room\Objects[10]),1231,1597,0.24,False)
			EndIf
			
			If e\EventState[5] > 0 And e\EventState[5] < 70*25 Then
				
				Local rocketbone% = FindChild(e\room\Objects[9],"flash")
				
				Local p.Particles = CreateParticle(EntityX(rocketbone,True),EntityY(rocketbone,True),EntityZ(rocketbone,True),6,.85,1.5,50)
				EntityParent p\obj, rocketbone
			EndIf
			
			If e\EventState[0] > 0 Then
				If TaskExists(TASK_HELP_GUARD) Then
					FailTask(TASK_HELP_GUARD)
					;psp\Karma = psp\Karma - (Rand(2,3))
				EndIf
			EndIf
			
			If EntityY(Collider)>0.0*RoomScale ; ~ Surface
				
				If PlayerInNewElevator
					For ne = Each NewElevator
						If PlayerNewElevator = ne\ID
							If EntityDistanceSquared(Collider,e\room\Objects[7])<PowTwo(2.0) Then
								
								If e\EventState[0] > 1.05 Then
									SaveGame(SavePath + CurrSave\Name + "\", True)
									
									gopt\CurrZone = GATE_C_TOPSIDE
									
									ResetControllerSelections()
									DropSpeed = 0
									NullGame(True,False)
									LoadEntities()
									LoadAllSounds()
									Local zonecache% = gopt\CurrZone
									If FileType(SavePath + CurrSave\Name + "\" + gopt\CurrZone + ".sav") = 1 Then
										LoadGame(SavePath + CurrSave\Name + "\", gopt\CurrZone)
										InitLoadGame()
									Else
										InitNewGame()
										LoadDataForZones(SavePath + CurrSave\Name + "\")
									EndIf
									gopt\CurrZone = zonecache
									MainMenuOpen = False
									FlushKeys()
									FlushMouse()
									FlushJoy()
									ResetInput()
									
									SaveGame(SavePath + CurrSave\Name + "\", True)
									Return
								EndIf
							EndIf
						EndIf
					Next
				EndIf
			EndIf
			
		EndIf
	EndIf
	
	If PlayerRoom = e\room Lor IsRoomAdjacent(e\room,PlayerRoom) Then
		UpdateAlarmRotor(e\room\AlarmRotor[0],8)
		ShowEntity e\room\AlarmRotor[0]
	Else
		HideEntity e\room\AlarmRotor[0]
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS