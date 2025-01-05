
Function CreateCoreElevator(r.Rooms, ElevatorID%, ElevatorX#, ElevatorY#, ElevatorZ#, ElevatorYaw#, doorID%, DoorX#, DoorY#, DoorZ#, DoorYaw#)
	
	; ~ Elevator mesh
	r\Objects[ElevatorID] = LoadRMesh("GFX\map\Elevators\core_elevator.rmesh", Null)
	ScaleEntity(r\Objects[ElevatorID], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[ElevatorID], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[ElevatorID], 2), 2)
	PositionEntity(r\Objects[ElevatorID], r\x + ElevatorX * RoomScale, r\y + ElevatorY * RoomScale, r\z + ElevatorZ * RoomScale)
	RotateEntity(r\Objects[ElevatorID], 0, ElevatorYaw, 0)
	EntityParent(r\Objects[ElevatorID], r\obj)
	
	; ~ Elevator door
	r\RoomDoors[doorID] = CreateDoor(r\zone, r\x + DoorX * RoomScale, r\y + DoorY * RoomScale, r\z + DoorZ * RoomScale, DoorYaw, r, True, DOOR_ELEVATOR_3FLOOR)
	r\RoomDoors[doorID]\open = True
	r\RoomDoors[doorID]\AutoClose = False
	r\RoomDoors[doorID]\DisableWaypoint = True
	MoveEntity(r\RoomDoors[doorID]\buttons[0], -25, 0, 2.5)
	
End Function

Function CreateCheckpointElevatorCounterWeight(r.Rooms, WeightID%, WeightX#, WeightY#, WeightZ#, WeightYaw#)
	
	; ~ Weight object
	r\Objects[WeightID] = LoadRMesh("GFX\map\Elevators\elevator_counterweight.rmesh", Null)
	ScaleEntity(r\Objects[WeightID], RoomScale, RoomScale, RoomScale)
	PositionEntity(r\Objects[WeightID], r\x + WeightX * RoomScale, r\y + WeightY * RoomScale, r\z + WeightZ * RoomScale)
	RotateEntity(r\Objects[WeightID], 0, WeightYaw, 0)
	EntityParent(r\Objects[WeightID], r\obj)
	
End Function

Function CreateCheckpointFakeDoor(r.Rooms, FakeDoorID%, FakeDoorX#, FakeDoorY#, FakeDoorZ#, FakeDoorYaw#)
	
	; ~ Fake Door
	r\Objects[FakeDoorID] = LoadMesh_Strict("GFX\map\props\door_elevator_dummy.b3d")
	ScaleEntity(r\Objects[FakeDoorID], RoomScale, RoomScale, RoomScale)
	PositionEntity(r\Objects[FakeDoorID], r\x + FakeDoorX * RoomScale, r\y + FakeDoorY * RoomScale, r\z + FakeDoorZ * RoomScale)
	RotateEntity(r\Objects[FakeDoorID], 0, FakeDoorYaw, 0)
	EntityParent(r\Objects[FakeDoorID], r\obj)
	EntityType(r\Objects[FakeDoorID], HIT_MAP)
	HideEntity(r\Objects[FakeDoorID])
	
End Function

Function FillRoom_Cores(r.Rooms)
	Local d.Doors,it.Items
	Local i%,ne.NewElevator
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x,r\y,r\z - 400.0*RoomScale,180,r,True,DOOR_WINDOWED,-1,"1234")
	r\RoomDoors[2] = CreateDoor(r\zone,r\x,r\y,r\z + 400.0*RoomScale,0,r,False,DOOR_WINDOWED,-1,"1234")
	For i = 0 To 1
		FreeEntity r\RoomDoors[1]\buttons[i] : r\RoomDoors[1]\buttons[i] = 0
		FreeEntity r\RoomDoors[2]\buttons[i] : r\RoomDoors[2]\buttons[i] = 0
	Next
	
	If gopt\GameMode <> GAMEMODE_NTF Then
		If gopt\CurrZone = LCZ Then
			r\RoomDoors[1]\open = True
			r\RoomDoors[2]\open = False
		Else
			r\RoomDoors[1]\open = False
			r\RoomDoors[2]\open = True
		EndIf
	Else
		If gopt\CurrZone = EZ Then
			r\RoomDoors[1]\open = True
			r\RoomDoors[2]\open = False
		Else
			r\RoomDoors[1]\open = False
			r\RoomDoors[2]\open = True
		EndIf
	EndIf
	
	r\Objects[11] = CreateButton(r\x - 125.0 * RoomScale, r\y + 134 * RoomScale, r\z, 430, 90, 0)
	EntityParent(r\Objects[11],r\obj)
	
	r\Objects[12] = CreateButton(r\x - 276.0 * RoomScale, r\y + 111 * RoomScale, r\z+97*RoomScale, 440, 270, 0)
	EntityParent(r\Objects[12],r\obj)
	
	If gopt\CurrZone = EZ Then
		r\Objects[13] = CreateButton(r\x + 156.0 * RoomScale, r\y + 186 * RoomScale, r\z+410*RoomScale, 0, 180, 0)
	Else
		r\Objects[13] = CreateButton(r\x - 156.0 * RoomScale, r\y + 186 * RoomScale, r\z-410*RoomScale, 0, 0, 0)
	EndIf
	EntityParent(r\Objects[13],r\obj)
	
	r\Levers[7] = CreateLever(r, r\x -200.0 * RoomScale, r\y +211.0 * RoomScale, r\z +318 * RoomScale,270,False)
	
	r\RoomDoors[5] = CreateDoor(r\zone,r\x-224*RoomScale,r\y,r\z+2446.0*RoomScale,0,r,False,DOOR_OFFICE_2)
	r\RoomDoors[5]\locked = True
	r\RoomDoors[6] = CreateDoor(r\zone,r\x+224*RoomScale,r\y,r\z+2446.0*RoomScale,180,r,False,DOOR_OFFICE_2)
	r\RoomDoors[6]\locked = True
	r\RoomDoors[7] = CreateDoor(r\zone,r\x+753*RoomScale,r\y,r\z-673.0*RoomScale,90,r,False,DOOR_OFFICE_2)
	r\RoomDoors[7]\locked = True
	
	r\Objects[10] = CreatePivot()
	PositionEntity r\Objects[10],r\x,r\y+128.0*RoomScale,r\z,True
	EntityParent r\Objects[10],r\obj
	
	Select gopt\CurrZone
		Case LCZ
			
			CreateCoreElevator(r, 0, 0, 0, 1456, 0, 0, 0, 0, 1180, 180)
			CreateCheckpointElevatorCounterWeight(r, 1, 0, 0, 1456, 0)
			PositionEntity(r\RoomDoors[0]\buttons[1], r\x + 132 * RoomScale, r\y + 192 * RoomScale, r\z + 1120 * RoomScale, True)
			CreateNewElevator(r\Objects[0], 2, r\RoomDoors[0], 1, r, -4096.0, 0.0, 4096.0)
			
			r\RoomDoors[3] = CreateDoor(r\zone,r\x-1187*RoomScale,r\y,r\z +955.0*RoomScale,96,r,False,DOOR_OFFICE)
			r\RoomDoors[4] = CreateDoor(r\zone,r\x-958*RoomScale,r\y,r\z + 503.0*RoomScale,144,r,False,DOOR_OFFICE)
			
			it = CreateItem(GetLocalString("Item Names","beer"), "beer", r\x-2027.0*RoomScale,r\y+130.0*RoomScale,r\z+1250.0*RoomScale)
			EntityParent(it\collider, r\obj)
			
		Case HCZ
			
			CreateCoreElevator(r, 0, 0, 0, 1456, 0, 0, 0, 0, 1180, 180)
			CreateCheckpointElevatorCounterWeight(r, 1, 0, 0, 1456, 0)
			PositionEntity(r\RoomDoors[0]\buttons[1], r\x + 132 * RoomScale, r\y + 192 * RoomScale, r\z + 1120 * RoomScale, True)
			CreateNewElevator(r\Objects[0], 1, r\RoomDoors[0], 1, r, 0.0, 4096.0, 8192.0)
			
			r\Objects[4] = CreatePivot()
			PositionEntity(r\Objects[4], r\x,r\y+64.0*RoomScale, r\z-712.0*RoomScale, True)
			EntityParent r\Objects[4], r\obj
			
			r\Objects[5] = CreatePivot()
			PositionEntity(r\Objects[5], r\x,r\y+64.0*RoomScale, r\z-944.0*RoomScale, True)
			EntityParent r\Objects[5], r\obj
			
			r\Objects[6] =	CreatePivot()
			PositionEntity(r\Objects[6], r\x+700*RoomScale,r\y, r\z-679.0*RoomScale, True)
			EntityParent r\Objects[6], r\obj
			
			it = CreateItem(GetLocalString("Item Names","first_aid"), "firstaid", r\x-407.0*RoomScale,r\y+10.0*RoomScale,r\z+984.0*RoomScale)
			EntityParent(it\collider, r\obj)
			
			it = CreateItem(GetLocalString("Item Names","first_aid_small"), "finefirstaid", r\x,r\y+10.0*RoomScale,r\z-1300.0*RoomScale)
			EntityParent(it\collider, r\obj)
			
			it = CreateItem(GetLocalString("Item Names","mag"), "extmag", r\x-242.0*RoomScale,r\y+10.0*RoomScale,r\z-290.0*RoomScale)
			EntityParent(it\collider, r\obj)
			
		Case EZ
			
			CreateCoreElevator(r, 0, 0, 0, 1456, 0, 0, 0, 0, 1180, 180)
			CreateCheckpointElevatorCounterWeight(r, 1, 0, 0, 1456, 0)
			PositionEntity(r\RoomDoors[0]\buttons[1], r\x + 132 * RoomScale, r\y + 192 * RoomScale, r\z + 1120 * RoomScale, True)
			CreateNewElevator(r\Objects[0], 3, r\RoomDoors[0], 1, r, -8192.0, -4096.0, 0.0)
			
			r\Objects[3] = CreatePivot()
			PositionEntity(r\Objects[3], r\x,r\y+64.0*RoomScale, r\z+968.0*RoomScale, True)
			EntityParent r\Objects[3], r\obj
			
			r\RoomDoors[8] = CreateDoor(r\zone,r\x-480*RoomScale,r\y,r\z -400.0*RoomScale,0,r,False,DOOR_OFFICE)
			
			r\Objects[9] = LoadMesh_Strict("GFX\Map\rooms\Core_ez\core_ez_monitor.b3d")
			PositionEntity r\Objects[9],r\x,r\y,r\z,True
			ScaleEntity r\Objects[9],RoomScale,RoomScale,RoomScale
			EntityParent r\Objects[9],r\obj
			
			EntityTexture r\Objects[9], Checkpoint_Screen[1]
			
			If ecst\ChanceToSpawnWolfNote = 2 Then
				it = CreateItem("Wolfnaya's Room Note", "paper", r\x-996.0*RoomScale, r\y+160.0*RoomScale, r\z+2192.0*RoomScale)
				EntityParent(it\collider, r\obj)
			EndIf
			
	End Select
	
	CreateDarkSprite(r, 2)
	
End Function

Function UpdateEvent_Cores(e.Events)
	Local ne.NewElevator,r.Rooms,e2.Events
	Local playerElev%,prevZone%, pvt%, pvt2%,n.NPCs,de.Decals
	
;! ~ Plot events
	
	If PlayerRoom = e\room Then
		If gopt\GameMode = GAMEMODE_DEFAULT Then
			
			Select gopt\CurrZone
				Case EZ
					
					If e\EventState[7] > 70*5 Then
						EntityTexture e\room\Objects[9], Checkpoint_Screen[0], Floor(((e\EventState[7]-70*5)/70) Mod 4.0)
					EndIf
					
					If ecst\EzDoorOpened Then
						e\EventState[7] = 0
					ElseIf (Not ecst\EzDoorOpened) Then
						e\EventState[7] = e\EventState[7] + FPSfactor
					EndIf
					
					If EntityDistanceSquared(e\room\Objects[3], Collider) < PowTwo(1.0) Then
						If (Not ecst\WasInHCZ) Then
							If (Not TaskExists(TASK_FIND_ROOM3_CT) Lor TaskExists(TASK_FIND_ROOM3_CT_FUSEBOXES)) Then
								If (Not TaskExists(TASK_FINDWAY_EZDOOR)) And (Not mtfd\IsPlaying) Then
									If (Not TaskExists(TASK_LAUNCH_ROCKET)) Then
										PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\ezdoorlocked")
										BeginTask(TASK_FINDWAY_EZDOOR)
									EndIf
								EndIf
							Else
								If (Not TaskExists(TASK_FINDWAY_EZDOOR_ALT)) And (Not mtfd\IsPlaying) Then
									If (Not TaskExists(TASK_LAUNCH_ROCKET)) Then
										BeginTask(TASK_FINDWAY_EZDOOR_ALT)
									EndIf
								EndIf
							EndIf
						Else
							If TaskExists(TASK_FINDWAY_EZDOOR) Then
								EndTask(TASK_FINDWAY_EZDOOR)
							EndIf
							If TaskExists(TASK_FINDWAY_EZDOOR_ALT) Then
								EndTask(TASK_FINDWAY_EZDOOR_ALT)
							EndIf
						EndIf
					EndIf
				Case HCZ
					
					If gopt\GameMode = GAMEMODE_DEFAULT And cpt\Current = 1 Then
						If PlayerInNewElevator And e\EventState[8] = 0 Then
							PlaySound_Strict(LoadTempSound("SFX\Room\Vincent_Wound.ogg"))
							e\EventState[8] = 1
						EndIf
					EndIf
					
					If (Not ecst\WasInO5) Then
						If e\room\NPC[0] = Null Then
							de.Decals = CreateDecal(DECAL_BLOODPOOL, EntityX(e\room\Objects[6],True), 0.002, EntityZ(e\room\Objects[6],True),90,Rnd(360),0)
							de\Size = 0.5
							e\room\NPC[0] = CreateNPC(NPC_Vincent,EntityX(e\room\Objects[6],True),EntityY(e\room\Objects[6],True)+0.2,EntityZ(e\room\Objects[6],True))
							e\room\NPC[0]\State[0] = 0
							RotateEntity(e\room\NPC[0]\Collider,0,e\room\angle-45,0)
						EndIf
						
						Local g.Guns
						
						If cpt\Current <> 4 Then
							
							If TaskExists(TASK_COME_BACK_TO_GUARD) Then
								If EntityDistanceSquared(e\room\Objects[5], Collider) < PowTwo(0.3) Then
									e\EventState[5] = e\EventState[5] + FPSfactor/2
									If e\EventState[5] <> 0.0 And e\EventState[5] < 70*8.0 Then
										BlurTimer = 0
										psp\NoMove = False
										psp\NoRotation = True
										
										HolsterGun()
										
									EndIf
								EndIf
							EndIf
							
							If TaskExists(TASK_COME_BACK_TO_GUARD) Then
								
								If e\EventState[5] > 0 And e\EventState[5] < 70*0.01 Then ; ~ Return after Room3_CT
									PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\didyouturnthefuse")
								EndIf
								
								If e\EventState[5] < 70*8.0 Then
									If EntityDistanceSquared(e\room\Objects[5], Collider) < PowTwo(0.3) Then
										
										pvt% = CreatePivot()
										PositionEntity pvt, EntityX(Camera), EntityY(e\room\Objects[6],True)-0.05, EntityZ(Camera)
										PointEntity(pvt, e\room\Objects[6])
										RotateEntity(Collider, EntityPitch(Collider), CurveAngle(EntityYaw(pvt), EntityYaw(Collider), 80-(e\EventState[13]/200.0)), 0)
										
										TurnEntity(pvt, 70, 0, 0)
										user_camera_pitch = CurveAngle(EntityPitch(pvt)+25, user_camera_pitch + 90.0, 80-(e\EventState[13]/200.0))
										user_camera_pitch=user_camera_pitch-90
										
										psp\NoMove = True
									EndIf
								ElseIf e\EventState[5] >= 70*8.0 And e\EventState[5] < 70*8.1 Then
									psp\NoMove = False
									psp\NoRotation = False
									FreeEntity pvt
									EndTask(TASK_COME_BACK_TO_GUARD)
									BeginTask(TASK_FIND_REACTOR)
								EndIf
								If e\EventState[5] > 0 And e\EventState[5] < 70*8.0 Then
									PointEntity pvt, e\room\Objects[6]
									RotateEntity Collider, 0, CurveAngle(EntityYaw(pvt),EntityYaw(Collider),100.0), 0
									psp\NoMove = True
									psp\NoRotation = True
								EndIf
							EndIf
							
							If TaskExists(TASK_COME_BACK_TO_GUARD_2) Then
								If EntityDistanceSquared(e\room\Objects[5], Collider) < PowTwo(0.3) Then
									e\EventState[6] = e\EventState[6] + FPSfactor/2
									If e\EventState[6] <> 0.0 And e\EventState[6] < 70*5.0 Then
										BlurTimer = 0
										psp\NoMove = False
										psp\NoRotation = True
										
										HolsterGun()
										
									EndIf
								EndIf
							EndIf
							
							If TaskExists(TASK_COME_BACK_TO_GUARD_2) Then
								
								If e\EventState[6] > 0 And e\EventState[6] < 70*0.01 Then ; ~ Going to EZ
									PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\launchrocket")
								EndIf
								
								If e\EventState[6] < 70*5.0 Then
									If EntityDistanceSquared(e\room\Objects[5], Collider) < PowTwo(0.3) Then
										
										pvt% = CreatePivot()
										PositionEntity pvt, EntityX(Camera), EntityY(e\room\Objects[6],True)-0.05, EntityZ(Camera)
										PointEntity(pvt, e\room\Objects[6])
										RotateEntity(Collider, EntityPitch(Collider), CurveAngle(EntityYaw(pvt), EntityYaw(Collider), 80-(e\EventState[14]/200.0)), 0)
										
										TurnEntity(pvt, 70, 0, 0)
										user_camera_pitch = CurveAngle(EntityPitch(pvt)+25, user_camera_pitch + 90.0, 80-(e\EventState[14]/200.0))
										user_camera_pitch=user_camera_pitch-90
										
										psp\NoMove = True
									EndIf
								ElseIf e\EventState[6] >= 70*5.0 And e\EventState[6] < 70*5.005 Then
									psp\NoMove = False
									psp\NoRotation = False
									FreeEntity pvt
									EndTask(TASK_COME_BACK_TO_GUARD_2)
									
									StartChapter("chapter_4", 4)
									
									ecst\EzDoorOpened = True
									
									ecst\WasInHCZ = True
									ecst\NTFArrived = True
									ecst\UnlockedHDS = True
									If (Not TaskExists(TASK_LAUNCH_ROCKET)) Then
										BeginTask(TASK_LAUNCH_ROCKET)
									EndIf
									If (Not TaskExists(TASK_GET_TOPSIDE)) Then
										BeginTask(TASK_GET_TOPSIDE)
									EndIf
								EndIf
								If e\EventState[6] > 0 And e\EventState[6] < 70*5.0 Then
									PointEntity pvt, e\room\Objects[6]
									RotateEntity Collider, 0, CurveAngle(EntityYaw(pvt),EntityYaw(Collider),100.0), 0
									psp\NoMove = True
									psp\NoRotation = True
								EndIf
							EndIf
							
							If cpt\Current < 3 Then
								
								If EntityDistanceSquared(e\room\Objects[4], Collider) < PowTwo(1.1) Then ; ~ First encounter with guard.
									e\EventState[2] = e\EventState[2] + FPSfactor
									If e\EventState[2] > 0 And e\EventState[2] < 70*0.1 Then
										PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\vincentencounter")
									ElseIf e\EventState[2] > 0.0 And e\EventState[2] < 70*15.0 Then
										BlurTimer = 0
										psp\NoMove = False
										psp\NoRotation = True
										
										HolsterGun()
										
									EndIf
								EndIf
								
								If (Not TaskExists(TASK_FIND_MEDKIT)) Then
									If e\EventState[2] < 70*15.0 Then
										If EntityDistanceSquared(e\room\Objects[4], Collider) < PowTwo(1.1) Then
											
											pvt% = CreatePivot()
											PositionEntity pvt, EntityX(Camera), EntityY(e\room\Objects[6],True)-0.05, EntityZ(Camera)
											PointEntity(pvt, e\room\Objects[6])
											RotateEntity(Collider, EntityPitch(Collider), CurveAngle(EntityYaw(pvt), EntityYaw(Collider), 80-(e\EventState[15]/200.0)), 0)
											
											TurnEntity(pvt, 70, 0, 0)
											user_camera_pitch = CurveAngle(EntityPitch(pvt)+25, user_camera_pitch + 90.0, 80-(e\EventState[15]/200.0))
											user_camera_pitch=user_camera_pitch-90
											
											psp\NoMove = True
										EndIf
									ElseIf e\EventState[2] >= 70*15.0 And e\EventState[2] < 70*15.01 Then
										psp\NoMove = False
										psp\NoRotation = False
										FreeEntity pvt
										
										StartChapter("chapter_2", 2)
									EndIf
									If e\EventState[2] > 0 And e\EventState[2] < 70*15.0 Then
										PointEntity pvt, e\room\Objects[6]
										RotateEntity Collider, 0, CurveAngle(EntityYaw(pvt),EntityYaw(Collider),100.0), 0
										psp\NoMove = True
										psp\NoRotation = True
									EndIf
									If e\EventState[2] > 70*15.1 And e\EventState[4] = 0 And (Not ChannelPlaying(mtfd\CurrentChannel)) Then
										BeginTask(TASK_FIND_MEDKIT)
									EndIf
								EndIf
								If TaskExists(TASK_FIND_MEDKIT) Then
									
									If ChannelPlaying(mtfd\CurrentChannel) And e\EventState[3] = 0 Then
										StopChannel(mtfd\CurrentChannel)
									EndIf
									
									Local i
									
									If e\EventState[3] <> 1.0 Then
										If Abs(EntityY(Collider)-EntityY(e\room\Objects[6],True))<1.0 Then
											If DistanceSquared(EntityX(Collider),EntityX(e\room\Objects[6],True),EntityZ(Collider),EntityZ(e\room\Objects[6],True)) < PowTwo(0.8) Then
												DrawHandIcon = True
												If KeyHitUse Then
													For i = 0 To MaxItemAmount-1
														If Inventory[i] <> Null Then
															If Inventory[i]\itemtemplate\tempname = "firstaid" Lor Inventory[i]\itemtemplate\tempname = "firstaid2" Lor Inventory[i]\itemtemplate\tempname = "finefirstaid" Lor Inventory[i]\itemtemplate\tempname = "veryfinefirstaid" Then
																RemoveItem(Inventory[i])
																e\EventState[3] = 1.0
																e\EventState[4] = e\EventState[4] + FPSfactor
																DrawHandIcon = False ; ~ Ryan helped the guard
																
																PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\thanksryan")
																
																EndTask(TASK_FIND_MEDKIT)
																psp\NoMove = True
																psp\NoRotation = True
																Exit
															EndIf
														EndIf
													Next
												EndIf
											EndIf
										EndIf
									EndIf
								EndIf
								If e\EventState[3] = 1.0 Then
									e\EventState[4] = e\EventState[4] + FPSfactor
								EndIf
								If e\EventState[4] >= 70*19 And e\EventState[4] < 70*19.1 Then
									psp\NoMove = False
									psp\NoRotation = False
									If (Not TaskExists(TASK_FIND_ROOM3_CT)) Then
										BeginTask(TASK_FIND_ROOM3_CT)
									EndIf
								EndIf
								
							EndIf
							
						EndIf
					Else
						If e\room\NPC[0] <> Null Then
							RemoveNPC(e\room\NPC[0])
						EndIf
					EndIf
				Case LCZ
					If PlayerRoom = e\room Then
						
						ecst\WasInLCZCore = True
						
						If TaskExists(TASK_COME_BACK_TO_CORE) Then
							EndTask(TASK_COME_BACK_TO_CORE)
						EndIf
						If TaskExists(TASK_FINDWAY_START) Lor TaskExists(TASK_FINDWAY_START_KEY) Then
							CancelTask(TASK_FINDWAY_START)
							CancelTask(TASK_FINDWAY_START_KEY)
						EndIf
						If ecst\WasInRoom2_SL Then
							If TaskExists(TASK_FIND_ROOM2_SL) Lor TaskExists(TASK_TURN_ON_ROOM2_SL) Then
								CancelTask(TASK_FIND_ROOM2_SL)
								CancelTask(TASK_TURN_ON_ROOM2_SL)
							EndIf
						EndIf
					EndIf
			End Select
			
		ElseIf gopt\GameMode = GAMEMODE_CLASS_D Then
			Select gopt\CurrZone	
				Case LCZ
					If PlayerRoom = e\room Then
						
						ecst\WasInLCZCore = True
						
						If TaskExists(TASK_COME_BACK_TO_CORE) Then
							EndTask(TASK_COME_BACK_TO_CORE)
						EndIf
						If TaskExists(TASK_FINDWAY_START) Lor TaskExists(TASK_FINDWAY_START_KEY) Then
							CancelTask(TASK_FINDWAY_START)
							CancelTask(TASK_FINDWAY_START_KEY)
						EndIf
						If ecst\WasInRoom2_SL Then
							If TaskExists(TASK_FIND_ROOM2_SL) Lor TaskExists(TASK_TURN_ON_ROOM2_SL) Then
								CancelTask(TASK_FIND_ROOM2_SL)
								CancelTask(TASK_TURN_ON_ROOM2_SL)
							EndIf
						EndIf
					EndIf
			End Select
			
		ElseIf gopt\GameMode = GAMEMODE_NTF Then
			
			ecst\UnlockedAirlock = True
			
			Select gopt\CurrZone
				Case EZ
					If TaskExists(TASK_NTF_CORE) Then
						If PlayerRoom = e\room Then
							EndTask(TASK_NTF_CORE)						
							BeginTask(TASK_NTF_GO_TO_ZONE)
						EndIf
					EndIf
				Case LCZ
					If TaskExists(TASK_NTF_GO_TO_ZONE) Then
						If (Not Curr173\Contained) Then
							If (Not TaskExists(TASK_NTF_SEARCH_173)) Then
								BeginTask(TASK_NTF_SEARCH_173)
							EndIf
							If PlayerRoom = e\room Then
								EndTask(TASK_NTF_GO_TO_ZONE)
							EndIf
						EndIf
					EndIf
				Case HCZ
					If TaskExists(TASK_NTF_GO_TO_ZONE) Then
						If (Not ecst\Contained049) Then
							If (Not TaskExists(TASK_NTF_SEARCH_049)) Then
								BeginTask(TASK_NTF_SEARCH_049)
							EndIf
							If PlayerRoom = e\room Then
								EndTask(TASK_NTF_GO_TO_ZONE)
							EndIf
						EndIf
					EndIf
					
					If e\EventState[2] >= 70*5 And e\EventState[2] < 70*5.02 Then
						PlaySound_Strict(OldManSFX[3])
						PlaySound_Strict(LoadTempSound("SFX\Room\PocketDimension\Kneel.ogg"))
					EndIf
					
					If e\EventState[2] = 0 Then
						If TaskExists(TASK_NTF_GO_TO_EXIT) Then
							FailTask(TASK_NTF_GO_TO_EXIT)
							e\EventState[2] = 1
						EndIf
					ElseIf e\EventState[2] > 0 And e\EventState[2] < 70*8 Then
						
						e\EventState[2] = e\EventState[2] + FPSfactor
						
						CameraShake = 0.6
						
						psp\NoMove = True
						
					ElseIf e\EventState[2] >= 70*8 And e\EventState[2] < 70*8.02 Then
						ShouldPlay = MUS_NULL
						
						SaveGame(SavePath + CurrSave\Name + "\", True)
						gopt\CurrZone = AREA_106_ESCAPE
						
						ResetControllerSelections()
						DropSpeed = 0
						
						NullGame(True, False)
						LoadEntities()
						LoadAllSounds()
						
						If FileType(SavePath + CurrSave\Name + "\" + gopt\CurrZone + ".sav") = 1 Then
							LoadGame(SavePath + CurrSave\Name + "\", gopt\CurrZone)
							InitLoadGame()
						Else
							InitNewGame()
							LoadDataForZones(SavePath + CurrSave\Name + "\")
						EndIf
						gopt\CurrZone = AREA_106_ESCAPE
						
						MainMenuOpen = False
						FlushKeys()
						FlushMouse()
						FlushJoy()
						ResetInput()
						
						SaveGame(SavePath + CurrSave\Name + "\", True)
						Return
					EndIf
			End Select
			
		EndIf
		
;! ~ Actual Core Functionality
		
		;If PlayerInNewElevator Then
			
		If PlayerInNewElevator Then IsZombie = False : ForceMove = 0.0
			
			;If e\room\Objects[1] <> 0 Then
			;	PositionEntity e\room\Objects[1],EntityX(e\room\Objects[1]),-EntityY(e\room\Objects[0]) - (7300 * (gopt\CurrZone = EZ)),EntityZ(e\room\Objects[1])
			;EndIf
			If e\EventState[1] = 0 And EntityY(Collider) > 2800.0*RoomScale Lor EntityY(Collider) <- 2800.0*RoomScale Then
				e\EventState[0] = e\EventState[0] + (0.01*FPSfactor)
				EntityAlpha e\room\Objects[2],Min(e\EventState[0],1.0)
				If e\EventState[0] > 1.05 Then
					SaveGame(SavePath + CurrSave\Name + "\", True)
					prevZone = gopt\CurrZone
					For ne = Each NewElevator
						If PlayerNewElevator = ne\ID And ne\room = e\room Then
							Select ne\tofloor
								Case 3
									gopt\CurrZone = EZ
								Case 2
									gopt\CurrZone = LCZ
								Case 1
									gopt\CurrZone = HCZ
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
					For ne = Each NewElevator
						If playerElev = ne\ID And ne\room = PlayerRoom Then
							PositionEntity ne\obj, EntityX(ne\obj), 0.0, EntityZ(ne\obj)
							Local translation# = 2700.0
							Select prevZone
								Case EZ
									TranslateEntity ne\obj, 0, translation, 0
								Case LCZ
									If gopt\CurrZone = EZ Then
										TranslateEntity ne\obj, 0, -translation, 0
									Else
										TranslateEntity ne\obj, 0, translation, 0
									EndIf
								Case HCZ
									TranslateEntity ne\obj, 0, -translation, 0
							End Select
							Select gopt\CurrZone
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
							If gopt\GameMode = GAMEMODE_NTF Then
								;For now as a temporary solution
								For n = Each NPCs
									If (n\NPCtype = NPC_NTF And (Not n\IsDead)) Lor (n\NPCtype = NPC_SCP_173 And n\Idle = SCP173_BOXED) Then
										TeleportEntity(n\Collider, EntityX(Collider), EntityY(Collider) + 0.1, EntityZ(Collider) + 0.2, n\CollRadius)
										If Curr173 <> Null And n\NPCtype = NPC_SCP_173 And Curr173 <> n Then
											RemoveNPC(Curr173)
											Curr173 = n
										EndIf
									EndIf
								Next
							EndIf
							StopStream_Strict(ne\soundchn)
							ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Loop"+Rand(1,3)+".ogg",opt\SFXVolume,Mode)
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
					SaveGame(SavePath + CurrSave\Name + "\", True)
					Return
				EndIf
			Else
				e\EventState[0] = Max(e\EventState[0] - (0.01*FPSfactor), 0.0)
				EntityAlpha e\room\Objects[2],Min(e\EventState[0],1.0)
				If e\room\RoomDoors[0]\open Then
					e\EventState[1] = 0
				EndIf
			EndIf
			
		;EndIf
		
		Local p.Particles
		
		If gopt\GameMode = GAMEMODE_DEFAULT Then
			Select gopt\CurrZone
				Case EZ
					If ecst\EzDoorOpened Then
						ecst\UnlockedAirlock = True
					Else
						ecst\UnlockedAirlock = False
					EndIf
				Case LCZ
					If ecst\WasInRoom2_SL Then
						ecst\UnlockedAirlock = True
					Else
						ecst\UnlockedAirlock = False
					EndIf
				Default
					ecst\UnlockedAirlock = True
			End Select
		EndIf
		
		Local zoneStr$
		
		If e\room\RoomTemplate\Name = "core_ez" Then
			zoneStr$ = "_EZ"
		ElseIf e\room\RoomTemplate\Name = "core_hcz" Then
			zoneStr$ = "_HCZ"
		Else
			zoneStr$ = "_LCZ"
		EndIf
		
		If e\EventState[12] = 0.0
			
			UpdateButton(e\room\Objects[11])
			UpdateButton(e\room\Objects[12])
			UpdateButton(e\room\Objects[13])
			
			If d_I\ClosestButton = e\room\Objects[11] Lor d_I\ClosestButton = e\room\Objects[12] Lor d_I\ClosestButton = e\room\Objects[13] Then
				If KeyHitUse Then
					If ecst\UnlockedAirlock Then
						PlaySound_Strict(ButtonSFX[0])
						e\EventState[12] = 1.0
						StopChannel e\SoundCHN[1]
						e\SoundCHN[1] = 0
						e\room\RoomDoors[1]\locked = False
						e\room\RoomDoors[2]\locked = False
						
						If e\room\RoomDoors[1]\open Then
							e\EventState[9] = 0
						EndIf
						If e\room\RoomDoors[2]\open Then
							e\EventState[9] = 1
						EndIf
						
						If e\EventState[9] = 0.0 Then
							UseDoor(e\room\RoomDoors[1])
						Else
							UseDoor(e\room\RoomDoors[2])
						EndIf
						
						PlaySound_Strict(AlarmSFX[4])
					Else
						PlaySound_Strict(ButtonSFX[0])
						; ~ For Ryan's story
						If gopt\CurrZone = LCZ Then
							If e\EventState[2] = 0 Then
								If PlayerRoom = e\room Then
									If (Not TaskExists(TASK_FIND_ROOM2_SL)) Then
										BeginTask(TASK_FIND_ROOM2_SL)
									EndIf
									If gopt\GameMode = GAMEMODE_DEFAULT Then PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\lczdoorlocked")
									e\EventState[2] = 1
								EndIf
							EndIf
						EndIf
						; ~ End
						If e\Sound[0] = 0 Then LoadEventSound(e,"SFX\Alarm\Airlock\Decont_Error.ogg")
						If (Not ChannelPlaying(e\SoundCHN[2])) Then e\SoundCHN[2] = PlaySound2(e\Sound[0],Camera,e\room\RoomDoors[2]\obj)
						e\Sound[0] = 0
						
					EndIf
				EndIf
			EndIf
		Else
			If e\EventState[11] < 70*7
				e\EventState[11] = e\EventState[11] + FPSfactor
				e\room\RoomDoors[1]\open = False
				e\room\RoomDoors[2]\open = False
				If e\EventState[11] < 70*1
					
					
				ElseIf e\EventState[11] > 70*3 And e\EventState[9] < 70*5.5
					pvt% = CreatePivot(e\room\obj)								
					For i = 0 To 3
						
						If i = 0
							PositionEntity pvt%,-96.0,318.0,176.0,False
						ElseIf i = 1
							PositionEntity pvt%,96.0,318.0,176.0,False
						ElseIf i = 2
							PositionEntity pvt%,-96.0,318.0,-176.0,False
						Else
							PositionEntity pvt%,96.0,318.0,-176.0,False
						EndIf
						
						p.Particles = CreateParticle(EntityX(pvt,True), EntityY(pvt,True), EntityZ(pvt,True),  6, 0.6, 0, 50)
						p\speed = 0.025
						RotateEntity(p\pvt, 90, 0, 0)
						
						p\Achange = -0.02
					Next
					
					FreeEntity pvt
					If e\SoundCHN[1] = 0 Then e\SoundCHN[1] = PlaySound2(AirlockSFX[1],Camera,e\room\Objects[10],5)
				EndIf
			Else
				
				e\EventState[12] = 0.0
				e\EventState[11] = 0.0
				e\EventState[10] = 1.0
				If e\room\RoomDoors[1]\open = False
					e\room\RoomDoors[1]\locked = False
					e\room\RoomDoors[2]\locked = False
					
					If e\EventState[9] = 0.0 Then
						UseDoor(e\room\RoomDoors[2])
						If e\Sound[0] = 0 Then LoadEventSound(e,"SFX\Alarm\Airlock\Decont_Core.ogg")
						If (Not ChannelPlaying(e\SoundCHN[2])) Then e\SoundCHN[2] = PlaySound2(e\Sound[0],Camera,e\room\RoomDoors[2]\obj)
						e\Sound[0] = 0
					Else
						UseDoor(e\room\RoomDoors[1])
						If e\Sound[0] = 0 Then LoadEventSound(e,"SFX\Alarm\Airlock\Decont"+zoneStr+".ogg")
						If (Not ChannelPlaying(e\SoundCHN[2])) Then e\SoundCHN[2] = PlaySound2(e\Sound[0],Camera,e\room\RoomDoors[1]\obj)
						e\Sound[0] = 0
					EndIf
					
					e\EventState[10] = 0.0
					
					UpdateButton(e\room\Objects[11])
					UpdateButton(e\room\Objects[12])
					UpdateButton(e\room\Objects[13])
					
				EndIf
			EndIf
		EndIf
		
		If ChannelPlaying(e\SoundCHN[1])
			UpdateSoundOrigin(e\SoundCHN[1],Camera,e\room\Objects[10],5)
		EndIf
	Else
		e\EventState[10] = 0.0
	EndIf
	
;! ~ End
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS