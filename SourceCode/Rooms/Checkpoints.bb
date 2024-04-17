
Const CHECKPOINT_ELEVATOR_ID% = 0
Const CHECKPOINT_ELEVATOR_FAKE_DOOR_ID% = 1
Const CHECKPOINT_COUNTERWEIGHT_ID% = 2
Const CHECKPOINT_DARK_SPRITE_ID% = 3

Const CHECKPOINT_ELEVATOR_DOOR_ID% = 0
Const CHECKPOINT_AIRLOCK_DOOR1_ID% = 1
Const CHECKPOINT_AIRLOCK_DOOR2_ID% = 2

Function CreateCheckpointElevator(r.Rooms, ElevatorID%, ElevatorX#, ElevatorY#, ElevatorZ#, ElevatorYaw#, doorID%, DoorX#, DoorY#, DoorZ#, DoorYaw#)
	
	;Elevator mesh
	r\Objects[ElevatorID] = LoadRMesh("GFX\map\elevator_cabin.rmesh", Null)
	ScaleEntity(r\Objects[ElevatorID], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[ElevatorID], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[ElevatorID], 2), 2)
	PositionEntity(r\Objects[ElevatorID], r\x + ElevatorX * RoomScale, r\y + ElevatorY * RoomScale, r\z + ElevatorZ * RoomScale)
	RotateEntity(r\Objects[ElevatorID], 0, ElevatorYaw, 0)
	EntityParent(r\Objects[ElevatorID], r\obj)
	
	;Elevator door
	r\RoomDoors[doorID] = CreateDoor(r\zone, r\x + DoorX * RoomScale, r\y + DoorY * RoomScale, r\z + DoorZ * RoomScale, DoorYaw, r, True, 5)
	r\RoomDoors[doorID]\AutoClose = False
	r\RoomDoors[doorID]\DisableWaypoint = True
	MoveEntity(r\RoomDoors[doorID]\buttons[0], -25, 0, 2.5)
	
End Function

Function FillRoom_Checkpoints(r.Rooms)
	Local i%
	
	;Airlock doors
	r\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID] = CreateDoor(r\zone,r\x,r\y,r\z - 400.0*RoomScale,180,r,True,DOOR_WINDOWED)
	r\RoomDoors[CHECKPOINT_AIRLOCK_DOOR2_ID] = CreateDoor(r\zone,r\x,r\y,r\z + 400.0*RoomScale,0,r,True,DOOR_WINDOWED)
	For i = 0 To 1
		r\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID]\buttons[i] = FreeEntity_Strict(r\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID]\buttons[i])
		r\RoomDoors[CHECKPOINT_AIRLOCK_DOOR2_ID]\buttons[i] = FreeEntity_Strict(r\RoomDoors[CHECKPOINT_AIRLOCK_DOOR2_ID]\buttons[i])
	Next
	
	;Elevator itself
	Select NTF_CurrZone
		Case LCZ
			CreateCheckpointElevator(r, CHECKPOINT_ELEVATOR_ID, 224, 0, 1926, 0, CHECKPOINT_ELEVATOR_DOOR_ID, 224, 0, 1644, 180)
			CreateCheckpointElevatorCounterWeight(r, CHECKPOINT_COUNTERWEIGHT_ID, 224, 0, 1926, 0)
			PositionEntity(r\RoomDoors[CHECKPOINT_ELEVATOR_DOOR_ID]\buttons[1], r\x, r\y + 0.7, r\z + 1604 * RoomScale, True)
			CreateNewElevator(r\Objects[CHECKPOINT_ELEVATOR_ID], 2, r\RoomDoors[CHECKPOINT_ELEVATOR_DOOR_ID], 1, r, -4096.0, 0.0, 4096.0)
			CreateCheckpointFakeDoor(r, CHECKPOINT_ELEVATOR_FAKE_DOOR_ID, 224, 0, 1644, 0)
			r\Objects[4] = CreatePivot()
			PositionEntity r\Objects[4], r\x - 352.0 * RoomScale, r\y + 256.0 * RoomScale, r\z + 1700.0 * RoomScale
			EntityParent r\Objects[4], r\obj
		Case HCZ
			CreateCheckpointElevator(r, CHECKPOINT_ELEVATOR_ID, 224, 0, 2312, 0, CHECKPOINT_ELEVATOR_DOOR_ID, 224, 0, 2030, 180)
			PositionEntity(r\RoomDoors[CHECKPOINT_ELEVATOR_DOOR_ID]\buttons[1], r\x, r\y + 0.7, r\z + 1990 * RoomScale, True)
			CreateNewElevator(r\Objects[CHECKPOINT_ELEVATOR_ID], 1, r\RoomDoors[CHECKPOINT_ELEVATOR_DOOR_ID], 1, r, 0.0, 4096.0, 8192.0)
			CreateCheckpointFakeDoor(r, CHECKPOINT_ELEVATOR_FAKE_DOOR_ID, 224, 0, 2030, 0)
	End Select
	
	CreateDarkSprite(r, CHECKPOINT_DARK_SPRITE_ID)
	
End Function

Function UpdateEvent_Checkpoints(e.Events)
	Local ne.NewElevator,r.Rooms,e2.Events,n.NPCs
	Local playerElev%,prevZone%
	
	If PlayerRoom = e\room Then
		If e\room\RoomTemplate\Name = "checkpoint_lcz" Then
			If e\Sound[0] = 0 Then
				e\Sound[0] = LoadTempSound("SFX\Room\Checkpoint\elevator_callingdispatch.ogg")
			EndIf
			e\SoundCHN[0] = LoopSound2(e\Sound[0],e\SoundCHN[0],Camera,e\room\Objects[4],6.0)
		EndIf
		
		If TaskExists(TASK_CHECKPOINT) Then
			EndTask(TASK_CHECKPOINT)
			BeginTask(TASK_GOTOZONE)
		EndIf
		
		If NTF_CurrZone <> EZ And TaskExists(TASK_GOTOZONE) And e\room\RoomDoors[CHECKPOINT_ELEVATOR_DOOR_ID]\open Then
			EndTask(TASK_GOTOZONE)
			BeginTask(TASK_CONTAIN106)
			BeginTask(TASK_CONTAIN173)
		EndIf
		
		If e\room\Objects[CHECKPOINT_COUNTERWEIGHT_ID] <> 0 Then
			PositionEntity e\room\Objects[CHECKPOINT_COUNTERWEIGHT_ID],EntityX(e\room\Objects[CHECKPOINT_COUNTERWEIGHT_ID]),-EntityY(e\room\Objects[CHECKPOINT_ELEVATOR_ID]) - (7300 * (NTF_CurrZone = EZ)),EntityZ(e\room\Objects[CHECKPOINT_COUNTERWEIGHT_ID])
		EndIf
		If e\room\RoomDoors[CHECKPOINT_ELEVATOR_DOOR_ID]\openstate = 0.0 And (EntityHidden(e\room\Objects[CHECKPOINT_ELEVATOR_FAKE_DOOR_ID])) Then
			ShowEntity(e\room\Objects[CHECKPOINT_ELEVATOR_FAKE_DOOR_ID])
		ElseIf e\room\RoomDoors[CHECKPOINT_ELEVATOR_DOOR_ID]\openstate <> 0.0 And (Not EntityHidden(e\room\Objects[CHECKPOINT_ELEVATOR_FAKE_DOOR_ID])) Then
			HideEntity(e\room\Objects[CHECKPOINT_ELEVATOR_FAKE_DOOR_ID])
		EndIf
		If e\EventState[1] = 0 And EntityY(Collider) > 2800.0*RoomScale Lor EntityY(Collider) < -2800.0*RoomScale Then
			e\EventState[0] = e\EventState[0] + (0.01*FPSfactor)
			EntityAlpha e\room\Objects[CHECKPOINT_DARK_SPRITE_ID],Min(e\EventState[0],1.0)
			If e\EventState[0] > 1.05 Then
				SaveGame(SavePath + CurrSave\Name + "\")
				prevZone = NTF_CurrZone
				For ne = Each NewElevator
					If PlayerNewElevator = ne\ID And ne\room = e\room Then
						Select ne\tofloor
							Case 3
								;SaveGame(SavePath + CurrSave\Name + "\", EZ)
								NTF_CurrZone = EZ
							Case 2
								;SaveGame(SavePath + CurrSave\Name + "\", LCZ)
								NTF_CurrZone = LCZ
							Case 1
								;SaveGame(SavePath + CurrSave\Name + "\", HCZ)
								NTF_CurrZone = HCZ
						End Select
						Exit
					EndIf
				Next
				If RandomSeed = "" Then
					RandomSeed = Abs(MilliSecs())
				EndIf
				SeedRnd GenerateSeedNumber(RandomSeed)
				ResetControllerSelections()
				DropSpeed = 0
				playerElev = PlayerNewElevator
				NullGame(True,False)
				LoadEntities()
				LoadAllSounds()
				Local zonecache% = NTF_CurrZone
				If FileType(SavePath + CurrSave\Name + "\" + NTF_CurrZone + ".ntf") = 1 Then
					LoadGame(SavePath + CurrSave\Name + "\", NTF_CurrZone)
					InitLoadGame()
				Else
					InitNewGame()
					LoadDataForZones(SavePath + CurrSave\Name + "\")
				EndIf
				NTF_CurrZone = zonecache
				MainMenuOpen = False
				FlushKeys()
				FlushMouse()
				FlushJoy()
				ResetInput()
				If PlayerRoom\RoomTemplate\Name = "gate_a_entrance" Then
					For r.Rooms = Each Rooms
						If r\RoomTemplate\Name = "checkpoint_ez" Then
							PlayerRoom = r
							UpdateRooms()
							UpdateDoors()
							Exit
						EndIf
					Next
				EndIf
				For ne = Each NewElevator
					If playerElev = ne\ID And ne\room = PlayerRoom Then
						PositionEntity ne\obj, EntityX(ne\obj), 0.0, EntityZ(ne\obj)
						Local translation# = 2700.0
						Select prevZone
							Case EZ
								TranslateEntity ne\obj, 0, translation, 0
							Case LCZ
								If NTF_CurrZone = EZ Then
									TranslateEntity ne\obj, 0, -translation, 0
								Else
									TranslateEntity ne\obj, 0, translation, 0
								EndIf
							Case HCZ
								TranslateEntity ne\obj, 0, -translation, 0
						End Select
						Select NTF_CurrZone
							Case EZ
								ne\tofloor = 3
								ne\currfloor = 2
							Case LCZ
								ne\tofloor = 2
								If prevZone = EZ Then
									ne\currfloor = 3
								Else
									ne\currfloor = 1
								EndIf
							Case HCZ
								ne\tofloor = 1
								ne\currfloor = 2
						End Select
						RotateEntity Collider,0,180,0
						TeleportEntity(Collider,EntityX(ne\obj,True),EntityY(ne\obj,True)+0.5,EntityZ(ne\obj,True),0.3,True)
						;For now as a temporary solution
						For n = Each NPCs
							If (n\NPCtype = NPCtypeNTF And (Not n\IsDead)) Lor (n\NPCtype = NPCtype173 And n\Idle = SCP173_BOXED) Then
								TeleportEntity(n\Collider, EntityX(Collider), EntityY(Collider) + 0.1, EntityZ(Collider) + 0.2, n\CollRadius)
								If Curr173 <> Null And n\NPCtype = NPCtype173 And Curr173 <> n Then
									RemoveNPC(Curr173)
									Curr173 = n
								EndIf
							EndIf
						Next
						StopStream_Strict(ne\soundchn)
						ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Loop.ogg",opt\SFXVolume,Mode)
						ne\currsound = 2
						ne\state = 200
						ne\door\open = False
						ne\door\openstate = 0.0
						PlayerInNewElevator = True
						PlayerNewElevator = ne\ID
						Exit
					EndIf
				Next
				For e2 = Each Events
					If e2\room = PlayerRoom Then
						e2\EventState[0] = 1.05
						e2\EventState[1] = 1
						Exit
					EndIf
				Next
				SaveGame(SavePath + CurrSave\Name + "\")
				Return
			EndIf
		Else
			e\EventState[0] = Max(e\EventState[0] - (0.01*FPSfactor), 0.0)
			EntityAlpha e\room\Objects[CHECKPOINT_DARK_SPRITE_ID],Min(e\EventState[0],1.0)
			If e\room\RoomDoors[CHECKPOINT_ELEVATOR_DOOR_ID]\open Then
				e\EventState[1] = 0
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D