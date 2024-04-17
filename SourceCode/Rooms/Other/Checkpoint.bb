
Function CreateCheckpointElevator(r.Rooms, ElevatorID%, ElevatorX#, ElevatorY#, ElevatorZ#, ElevatorYaw#, doorID%, DoorX#, DoorY#, DoorZ#, DoorYaw#)
	
	; ~ Elevator mesh
	r\Objects[ElevatorID] = LoadRMesh("GFX\map\Elevators\elevator_cabin_5.rmesh", Null)
	ScaleEntity(r\Objects[ElevatorID], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[ElevatorID], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[ElevatorID], 2), 2)
	PositionEntity(r\Objects[ElevatorID], r\x + ElevatorX * RoomScale, r\y + ElevatorY * RoomScale, r\z + ElevatorZ * RoomScale)
	RotateEntity(r\Objects[ElevatorID], 0, ElevatorYaw, 0)
	EntityParent(r\Objects[ElevatorID], r\obj)
	
	; ~ Elevator door
	r\RoomDoors[doorID] = CreateDoor(r\zone, r\x + DoorX * RoomScale, r\y + DoorY * RoomScale, r\z + DoorZ * RoomScale, DoorYaw, r, True, 5)
	r\RoomDoors[doorID]\open = True
	r\RoomDoors[doorID]\AutoClose = False
	r\RoomDoors[doorID]\DisableWaypoint = True
	MoveEntity(r\RoomDoors[doorID]\buttons[0], -25, 0, 2.5)
	
End Function

Function FillRoom_Checkpoints(r.Rooms)
	Local d.Doors,d2.Doors,it.Items
	Local i%,ne.NewElevator,fb.FuseBox
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x,r\y,r\z - 400.0*RoomScale,180,r,True,DOOR_WINDOWED,False,"1234")
	r\RoomDoors[2] = CreateDoor(r\zone,r\x,r\y,r\z + 400.0*RoomScale,0,r,False,DOOR_WINDOWED,False,"1234")
	For i = 0 To 1
		FreeEntity r\RoomDoors[1]\buttons[i] : r\RoomDoors[1]\buttons[i] = 0
		FreeEntity r\RoomDoors[2]\buttons[i] : r\RoomDoors[2]\buttons[i] = 0
	Next
	
	If gopt\CurrZone = HCZ Then
		r\RoomDoors[1]\open = True
		r\RoomDoors[2]\open = False
	Else
		r\RoomDoors[1]\open = False
		r\RoomDoors[2]\open = True
	EndIf
	
	r\Objects[11] = CreateButton(r\x - 125.0 * RoomScale, r\y + 134 * RoomScale, r\z, 430, 90, 0)
	EntityParent(r\Objects[11],r\obj)
	
	r\Objects[12] = CreateButton(r\x - 276.0 * RoomScale, r\y + 111 * RoomScale, r\z+97*RoomScale, 440, 270, 0)
	EntityParent(r\Objects[12],r\obj)
	
	If gopt\CurrZone = HCZ Then
		r\Objects[13] = CreateButton(r\x + 156.0 * RoomScale, r\y + 186 * RoomScale, r\z+410*RoomScale, 0, 180, 0)
	Else
		r\Objects[13] = CreateButton(r\x - 156.0 * RoomScale, r\y + 186 * RoomScale, r\z-410*RoomScale, 0, 0, 0)
	EndIf
	EntityParent(r\Objects[13],r\obj)
	
	Select gopt\CurrZone
		Case RCZ,BCZ
			r\RoomDoors[7] = CreateDoor(r\zone,r\x-662*RoomScale,r\y,r\z-673.0*RoomScale,90,r,False,DOOR_OFFICE_2)
			r\RoomDoors[7]\locked = True
		Case HCZ
			r\RoomDoors[11] = CreateDoor(r\zone,r\x+753*RoomScale,r\y,r\z -673.0*RoomScale,90,r,False,DOOR_OFFICE_2)
			r\RoomDoors[11]\locked = True
	End Select
	
	Select gopt\CurrZone
		Case BCZ
			r\RoomDoors[9] = CreateDoor(r\zone,r\x - 768.0*RoomScale,r\y,r\z - 158.0*RoomScale,90,r,False,DOOR_OFFICE,KEY_CARD_3)
		Case HCZ
			r\RoomDoors[12] = CreateDoor(r\zone,r\x - 768.0*RoomScale,r\y,r\z + 672.0*RoomScale,90,r,False,DOOR_OFFICE_2,KEY_CARD_3)
	End Select
	
	r\Levers[7] = CreateLever(r, r\x -200.0 * RoomScale, r\y +211.0 * RoomScale, r\z +318 * RoomScale,270,False)
	
	r\Objects[10] = CreatePivot()
	PositionEntity r\Objects[10],r\x,128*RoomScale,r\z
	EntityParent r\Objects[10],r\obj
	
	Select gopt\CurrZone
		Case RCZ
			CreateCheckpointElevator(r, 0, 224, 0, 2312, 0, 0, 224, 0, 2030, 180)
			PositionEntity(r\RoomDoors[0]\buttons[1], r\x, r\y + 0.7, r\z + 1990 * RoomScale, True)
			CreateNewElevator(r\Objects[0], 1, r\RoomDoors[0], 1, r, 0.0, 4096.0, 8192.0)
			r\RoomDoors[3] = CreateDoor(r\zone,r\x-416*RoomScale,r\y,r\z +400.0*RoomScale,0,r,False,DOOR_OFFICE)
		Case HCZ
			CreateCheckpointElevator(r, 0, 224, 0, 2760, 0, 0, 224, 0, 2478, 180)
			CreateCheckpointElevatorCounterWeight(r, 1, 224, -7300, 2760, 0)
			PositionEntity(r\RoomDoors[0]\buttons[1], r\x, r\y + 0.7, r\z + 2438 * RoomScale, True)
			CreateNewElevator(r\Objects[0], 2, r\RoomDoors[0], 1, r, -4096.0, 0.0, 4096.0)
			CreateCheckpointFakeDoor(r, 14, 224, 0, 2478, 0)
			r\RoomDoors[5] = CreateDoor(r\zone,r\x+1265*RoomScale,r\y,r\z+1392.0*RoomScale,90,r,False,DOOR_OFFICE_2)
			r\RoomDoors[5]\locked = True
			r\RoomDoors[6] = CreateDoor(r\zone,r\x+1265*RoomScale,r\y,r\z+944.0*RoomScale,270,r,False,DOOR_OFFICE_2)
			r\RoomDoors[6]\locked = True
			fb = CreateFuseBox("Fusebox.b3d", CreateVector3D(r\x - 3798.0 * RoomScale, r\y + 632.0 * RoomScale, r\z + 852.0 * RoomScale), CreateVector3D(0, 270, 0), CreateVector3D(0.4 * RoomScale, 0.4 * RoomScale, 0.4 * RoomScale))
			EntityParent fb\obj, r\obj
			If psp\Checkpoint106Passed Then
				fb\fuses = 3
			Else
				fb\fuses = 2
				
				it = CreateItem(GetLocalString("Item Names","fuse"),"fuse",r\x-3699*RoomScale,r\y+499*RoomScale,r\z+1063*RoomScale)
				EntityType it\collider, HIT_ITEM
				EntityParent it\collider, r\obj
			EndIf
			
			r\RoomDoors[10] = CreateDoor(r\zone,r\x-480*RoomScale,r\y,r\z -400.0*RoomScale,0,r,False,DOOR_OFFICE)
			
			r\Objects[14] = CreatePivot();BODY
			PositionEntity r\Objects[14],r\x-3589+RoomScale,r\y+500*RoomScale,r\z+1108*RoomScale,True
			EntityParent r\Objects[14],r\obj
			
			r\Objects[15] = fb\obj;FB
			
			r\Objects[16] = CreatePivot(); WP
			PositionEntity r\Objects[16], r\x - 10.0 * RoomScale, r\y, r\z + 2024.0 * RoomScale
			EntityParent r\Objects[16], r\obj
			
			r\Objects[17] = CreatePivot(); WP
			PositionEntity r\Objects[17], r\x + 192.0 * RoomScale, r\y, r\z + 1896.0 * RoomScale
			EntityParent r\Objects[17], r\obj
			
			r\Objects[18] = CreatePivot() ; WP
			PositionEntity r\Objects[18], r\x, r\y, r\z + 1352.0 * RoomScale
			EntityParent r\Objects[18], r\obj
			
			r\Objects[19] = CreatePivot() ; 106
			PositionEntity r\Objects[19], r\x + 104.0 * RoomScale, r\y, r\z + 1608.0 * RoomScale
			EntityParent r\Objects[19], r\obj
			
			r\Objects[20] = CreatePivot();spark
			PositionEntity r\Objects[20], r\x - 100.0 * RoomScale, r\y + 5.0 * RoomScale, r\z - 400.0 * RoomScale, True
			EntityParent r\Objects[20], r\obj
			
			r\Objects[21] = CopyEntity(r\RoomDoors[1]\obj,r\obj);Door
			ScaleEntity r\Objects[21], EntityScaleX(r\RoomDoors[1]\obj), EntityScaleY(r\RoomDoors[1]\obj), EntityScaleZ(r\RoomDoors[1]\obj)
			MoveEntity r\Objects[21], 140.0, 0.0, 0.0
			EntityType r\Objects[21], HIT_MAP
			EntityPickMode r\Objects[21], 2
			HideEntity r\Objects[21]
			
			For i = 22 To 23; WP
				r\Objects[i] = CreatePivot()
				PositionEntity r\Objects[i], r\x + (192.0 - (384.0 * (i - 22))) * RoomScale, r\y, r\z + 1800.0 * RoomScale, True
				EntityParent r\Objects[i], r\obj
			Next
			
		Case BCZ
			CreateCheckpointElevator(r, 0, 224, 0, 1926, 0, 0, 224, 0, 1644, 180)
			CreateCheckpointElevatorCounterWeight(r, 1, 224, 0, 1926, 0)
			PositionEntity(r\RoomDoors[0]\buttons[1], r\x, r\y + 0.7, r\z + 1604 * RoomScale, True)
			CreateNewElevator(r\Objects[0], 3, r\RoomDoors[0], 1, r, -8192.0, -4096.0, 0.0)
			CreateCheckpointFakeDoor(r, 14, 224, 0, 1644, 0)
			fb = CreateFuseBox("Fusebox.b3d", CreateVector3D(r\x - 1356.0 * RoomScale, r\y + 632.0 * RoomScale, r\z + 2233.0 * RoomScale), CreateVector3D(0, 180, 0), CreateVector3D(0.4 * RoomScale, 0.4 * RoomScale, 0.4 * RoomScale))
			EntityParent fb\obj, r\obj
			fb\fuses = 3
			
			r\RoomDoors[8] = CreateDoor(r\zone,r\x-400*RoomScale,r\y,r\z +640.0*RoomScale,90,r,False,DOOR_OFFICE)
			
			r\Objects[9] = LoadMesh_Strict("GFX\Map\rooms\Core_ez\core_ez_monitor.b3d")
			PositionEntity r\Objects[9],r\x,r\y,r\z,True
			ScaleEntity r\Objects[9],RoomScale,RoomScale,RoomScale
			EntityParent r\Objects[9],r\obj
			HideEntity r\Objects[9]
			
			EntityTexture r\Objects[9], Checkpoint_Screen[1]
			
			it = CreateItem(GetLocalString("Item Names","hazmat"), "hazmat", r\x+292.0*RoomScale,r\y+17.0*RoomScale,r\z)
			RotateEntity it\collider,0,-90,0
			EntityParent(it\collider, r\obj)
			
			r\Objects[9] = LoadMesh_Strict("GFX\Map\rooms\Core_ez\core_ez_monitor.b3d")
			PositionEntity r\Objects[9],r\x,r\y,r\z,True
			ScaleEntity r\Objects[9],RoomScale,RoomScale,RoomScale
			EntityParent r\Objects[9],r\obj
			HideEntity r\Objects[9]
			
	End Select
	
	CreateDarkSprite(r, 2)
	
End Function

Function UpdateEvent_Checkpoints(e.Events)
	Local ne.NewElevator,r.Rooms,e2.Events
	Local playerElev%,prevZone%, pvt%, pvt2%,n.NPCs,de.Decals,i,it.Items
	Local fb.FuseBox
	
	If gopt\GameMode = GAMEMODE_DEFAULT Then
		
		; ~ Plot Events
		
		Select gopt\CurrZone
			Case BCZ
				
				If ecst\WasInBCZ Then
					
					If e\EventState[5] = 0 Then
						it = CreateItem("SCP-500","scp500",EntityX(e\room\Objects[10],True),0.05,EntityZ(e\room\Objects[10],True))
						EntityParent it\collider, e\room\obj
						EntityType it\collider, HIT_MAP
						
						it = CreateItem("Unknown Note", "paper",EntityX(e\room\Objects[10],True),0.05,EntityZ(e\room\Objects[10],True))
						EntityParent it\collider, e\room\obj
						EntityType it\collider, HIT_MAP
						
						e\EventState[5] = 1
					EndIf
					
					I_008\Timer = 0
					I_409\Timer = 0
				EndIf
				
				If I_008\Timer > 0 Lor I_409\Timer > 0 Then
					CanSave = False
				Else
					CanSave = True
				EndIf
				If (Not PlayerInNewElevator) Then
					If PlayerRoom\RoomTemplate\Name <> "checkpoint_bcz" And PlayerRoom\RoomTemplate\Name <> "cont_409" Then
						If (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Lor (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat") Then
							I_008\Timer = 1
							CanSave = False
						Else
							CanSave = True
						EndIf
					EndIf
				EndIf
				
		End Select
		
		If PlayerRoom = e\room Then
			
			Select gopt\CurrZone
				Case BCZ
					If PlayerRoom = e\room Then
						
						If (Not PlayerInNewElevator) Then
							If TaskExists(TASK_GO_TO_BCZ) Then
								EndTask(TASK_GO_TO_BCZ)
								If (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
									If (Not TaskExists(TASK_SEARCH_FOR_HAZMAT)) Then
										BeginTask(TASK_SEARCH_FOR_HAZMAT)
									EndIf
								Else
									If (Not TaskExists(TASK_FIND_008)) Then
										BeginTask(TASK_FIND_008)
									EndIf
									If (Not TaskExists(TASK_FIND_409)) Then
										BeginTask(TASK_FIND_409)
									EndIf
								EndIf
							EndIf
							If TaskExists(TASK_SEARCH_FOR_HAZMAT) Then
								For i = 0 To MaxItemAmount-1
									If Inventory[i] <> Null Then
										If Inventory[i]\itemtemplate\tempname = "hazmat" Then
											EndTask(TASK_SEARCH_FOR_HAZMAT)
											If (Not TaskExists(TASK_FIND_008)) Then
												BeginTask(TASK_FIND_008)
											EndIf
											If (Not TaskExists(TASK_FIND_409)) Then
												BeginTask(TASK_FIND_409)
											EndIf
										EndIf
									EndIf
								Next
							EndIf
						EndIf
						If ecst\WasInO5Again Then
							If e\EventState[8] = 0 Then
								If (Not PlayerInNewElevator) Then
									
									StartChapter("chapter_8", 8)
									If opt\MusicVol > 0 Then
										If (Not ChannelPlaying(e\SoundCHN[0])) Then e\SoundCHN[0] = PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Biological_Containment_Entrance.ogg"))
									EndIf
									e\EventState[8] = 1
								EndIf
							EndIf
						EndIf
						
						ShowEntity e\room\Objects[9]
						
						If ecst\WasInO5Again Then
							e\EventState[7] = 0
						ElseIf (Not ecst\WasInO5Again) Then
							e\EventState[7] = e\EventState[7] + FPSfactor
						EndIf
						
						If e\EventState[7] > 70*5 Then
							EntityTexture e\room\Objects[9], Checkpoint_Screen[2], Floor(((e\EventState[7]-70*5)/70) Mod 4.0)
						EndIf
					EndIf
				Case RCZ
					If PlayerRoom = e\room Then
						If TaskExists(TASK_FIND_AREA_076) Then
							If ecst\WasInO5 Then
								If e\EventState[6] = 0 Then
									If (Not PlayerInNewElevator) Then
										
										StartChapter("chapter_5", 5)
										If opt\MusicVol > 0 Then
											If (Not ChannelPlaying(e\SoundCHN[0])) Then e\SoundCHN[0] = PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Reinforced_Containment_Entrance.ogg"))
										EndIf
										e\EventState[6] = 1
									EndIf
								EndIf
							EndIf
						EndIf
					EndIf
			End Select
			
		EndIf
	ElseIf gopt\GameMode = GAMEMODE_NTF Then
		
		; ~ Every SCP has been contained
		
		If ecst\Contained008 And ecst\Contained049 And ecst\Contained409 And Curr173\Contained And Curr106\Contained Then
			If e\EventState[2] = 1 Then
				If (Not TaskExists(TASK_NTF_GO_TO_EXIT)) Then
					BeginTask(TASK_NTF_GO_TO_EXIT)
				EndIf
				e\EventState[2] = 2
			EndIf
		EndIf
		
		If psp\Checkpoint106Passed Then
			Select gopt\CurrZone
				Case BCZ
					If TaskExists(TASK_NTF_GO_TO_ZONE) Then
						If (Not ecst\Contained008) Then
							If (Not TaskExists(TASK_NTF_SEARCH_008)) Then
								BeginTask(TASK_NTF_SEARCH_008)
							EndIf
						EndIf
						If (Not ecst\Contained409) Then
							If (Not TaskExists(TASK_NTF_SEARCH_409)) Then
								BeginTask(TASK_NTF_SEARCH_409)
							EndIf
							If (Not ecst\Contained008) Lor (Not ecst\Contained409) Then
								If PlayerRoom = e\room Then
									EndTask(TASK_NTF_GO_TO_ZONE)
								EndIf
							EndIf
						EndIf
					EndIf
				Case RCZ
					If (Not Curr106\Contained) Then
						If e\EventState[2] = 0 Then
							If TaskExists(TASK_NTF_SEARCH_106) Then
								UpdateTask(TASK_NTF_SEARCH_106)
								e\EventState[2] = 1
							EndIf
						EndIf
					EndIf
			End Select
		EndIf
	EndIf
	
	If PlayerRoom = e\room Then
		
		; ~ Actual checkpoint functionality
		
		If PlayerInNewElevator Then
			
			If e\room\Objects[1] <> 0 Then
				PositionEntity e\room\Objects[1],EntityX(e\room\Objects[1]),-EntityY(e\room\Objects[0]) - (7300 * (gopt\CurrZone = BCZ)),EntityZ(e\room\Objects[1])
			EndIf
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
									gopt\CurrZone = BCZ
								Case 2
									gopt\CurrZone = HCZ
								Case 1
									gopt\CurrZone = RCZ
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
								Case BCZ
									TranslateEntity ne\obj, 0, translation, 0
								Case HCZ
									If gopt\CurrZone = BCZ Then
										TranslateEntity ne\obj, 0, -translation, 0
									Else
										TranslateEntity ne\obj, 0, translation, 0
									EndIf
								Case RCZ
									TranslateEntity ne\obj, 0, -translation, 0
							End Select
							Select gopt\CurrZone
								Case BCZ
									ne\tofloor = 3
									ne\currfloor = 2
								Case HCZ
									ne\tofloor = 2
									If prevZone = BCZ Then
										ne\currfloor = 3
									Else
										ne\currfloor = 1
									EndIf
								Case RCZ
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
							ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Checkpoint\Loop.ogg",opt\SFXVolume,Mode)
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
			
		EndIf
		
		Local zoneStr$,pn
		
		If e\room\RoomTemplate\Name = "checkpoint_bcz" Then
			zoneStr$ = "_BCZ"
		ElseIf e\room\RoomTemplate\Name = "checkpoint_hcz" Then
			zoneStr$ = "_HCZ"
		Else
			zoneStr$ = "_RCZ"
		EndIf
		
		If e\EventState[12] = 0.0
			
			If gopt\CurrZone = BCZ Then
				If (Not ecst\WasInBCZ) Then
					If (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
						If (Not TaskExists(TASK_SEARCH_FOR_HAZMAT)) And Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat" Then
							For pn = 11 To 13
								UpdateButton(e\room\Objects[pn])
							Next
						EndIf
					Else
						For pn = 11 To 13
							UpdateButton(e\room\Objects[pn])
						Next
					EndIf
				Else
					For pn = 11 To 13
						UpdateButton(e\room\Objects[pn])
					Next
				EndIf 
			Else
				For pn = 11 To 13
					UpdateButton(e\room\Objects[pn])
				Next
			EndIf			
			
			If d_I\ClosestButton = e\room\Objects[11] Lor d_I\ClosestButton = e\room\Objects[12] Lor d_I\ClosestButton = e\room\Objects[13] Then
				If KeyHitUse Then
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
					
					PlaySound_Strict(AlarmSFX[6])
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
						
						Local p.Particles = CreateParticle(EntityX(pvt,True), EntityY(pvt,True), EntityZ(pvt,True),  6, 0.6, 0, 50)
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
						If e\Sound[0] = 0 Then LoadEventSound(e,"SFX\Alarm\Airlock\Decont_Checkpoint.ogg")
						If (Not ChannelPlaying(e\SoundCHN[2])) Then e\SoundCHN[2] = PlaySound2(e\Sound[0],Camera,e\room\RoomDoors[2]\obj)
						e\Sound[0] = 0
					Else
						UseDoor(e\room\RoomDoors[1])
						If e\Sound[0] = 0 Then LoadEventSound(e,"SFX\Alarm\Airlock\Decont"+zoneStr+".ogg")
						If (Not ChannelPlaying(e\SoundCHN[2])) Then e\SoundCHN[2] = PlaySound2(e\Sound[0],Camera,e\room\RoomDoors[1]\obj)
						e\Sound[0] = 0
					EndIf
					
					e\EventState[10] = 0.0
					
					For pn = 11 To 13
						UpdateButton(e\room\Objects[pn])
					Next
					
				EndIf
			EndIf
		EndIf
		
		If ChannelPlaying(e\SoundCHN[1])
			UpdateSoundOrigin(e\SoundCHN[1],Camera,e\room\Objects[10],5)
		EndIf
	Else
		e\EventState[10] = 0.0
	EndIf
	
End Function

Function UpdateEvent_Checkpoint_HCZ_106(e.Events)
	Local p.Particles, n.NPCs, fb.FuseBox, de.Decals
	Local i%, tex%, np.NPCs, teamDead% = False
	
	For np = Each NPCs
		If np\NPCtype = NPC_NTF And np\HP =< 0 Then
			teamDead = True
		;Else
		;	teamDead = False
		EndIf
	Next
	
	If psp\Checkpoint106Passed And gopt\GameMode = GAMEMODE_NTF Then
		e\room\RoomDoors[0]\locked = False
		e\room\RoomDoors[0]\open = True
		e\room\RoomDoors[1]\open = True
		e\room\RoomDoors[2]\open = False
		e\room\Objects[21] = FreeEntity_Strict(e\room\Objects[21])
		RemoveEvent(e)
		CreateEvent("checkpoints", "checkpoint_hcz", 0, 1.0)
		Return
	EndIf
	
	If EntityHidden(e\room\Objects[21]) Then ShowEntity e\room\Objects[21]
	
	e\room\RoomDoors[0]\locked = True
	e\room\RoomDoors[0]\open = False
	
	If Curr173\Contained And ecst\Contained049 Then
		
		If PlayerRoom = e\room Then
			LightVolume = Min(LightVolume+0.01,TempLightVolume*1.5)
			If teamDead Then
				Select e\EventState[0]
					Case 0 ;Room hasn't been entered before
						PlayNewDialogue(100,%011101)
						mtfd\Enabled = False
						n.NPCs = CreateNPC(NPC_Human,EntityX(e\room\Objects[14],True),EntityY(e\room\Objects[14],True)+0.5,EntityZ(e\room\Objects[14],True))
						RotateEntity n\Collider,0,e\room\angle-90,0
						n\State[0] = 3
						ChangeNPCTexture(n,"GFX\NPCs\Scientist.jpg")
						SetNPCFrame(n,558)
						n\IsDead = True
						e\EventState[0] = 1
						EndTask(TASK_NTF_CORE)
					Case 1 ;MTF units go to elevator doors and player goes up to place the fuse
						If (Not TaskExists(TASK_NTF_FIX_ELEVATOR)) And (Not mtfd\IsPlaying) Then
							BeginTask(TASK_NTF_FIX_ELEVATOR)
						EndIf
						For fb = Each FuseBox
							If fb\obj = e\room\Objects[15] Then
								If fb\fuses = 3 Then
									For i = 16 To 18
										de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[i], True), e\room\y + 0.01, EntityZ(e\room\Objects[i], True), 90, Rand(360), 0)
										de\Size = 0.1 : de\SizeChange = 0.01 : de\MaxSize = 1.25 : EntityAlpha(de\obj, 1.0)
									Next
									de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[18], True), e\room\y + 0.01, EntityZ(e\room\Objects[18], True), 90, Rand(360), 0)
									de\Size = 0.1 : de\SizeChange = 0.01 : de\MaxSize = 1.75 : EntityAlpha(de\obj, 1.0)
									mtfd\CurrentProgress = 0
									mtfd\CurrentSequence = 0
									mtfd\CurrentDialogue = 0
									EndTask(TASK_NTF_FIX_ELEVATOR)
									e\EventState[0] = 2
								EndIf
								Exit
							EndIf
						Next
						If e\EventState[1] = 0 Then
							If EntityDistanceSquared(Collider, e\room\Objects[14]) < 5.0 Then
								e\EventState[1] = 1
							EndIf	
						ElseIf e\EventState[1] = 1 Then
							If EntityDistanceSquared(Collider, e\room\RoomDoors[12]\frameobj) < PowTwo(0.5) Then
								;PlayNewDialogue(101,%11)
								;TODO: Dialogue for forgor to put fuse in LOL
								e\EventState[1] = 2
							EndIf
						EndIf
						If e\EventState[2] = 0 Then
							For i = 0 To MaxItemAmount-1
								If Inventory[i] <> Null Then
									If Inventory[i]\itemtemplate\tempname = "fuse" Then
										PlaySound_Strict LoadTempSound("SFX\SCP\106\Laugh.ogg")
										e\EventState[2] = 1
										Exit
									EndIf
								EndIf
							Next
						EndIf	
					Case 2 ;Fuse has been placed, 106 sound appears with MTF screams and door below locks
						If EntityDistanceSquared(Collider, e\room\RoomDoors[12]\frameobj) < PowTwo(0.5) Then
							e\EventState[0] = 3
							e\EventState[2] = 2
								;Spawn 106 between both MTF units
							PositionEntity(Curr106\Collider, EntityX(e\room\Objects[19], True), e\room\y + 0.15, EntityZ(e\room\Objects[19], True), True)
							ResetEntity(Curr106\Collider)
							e\SoundCHN[1] = StreamSound_Strict("SFX\Music\SuspenseEnd.ogg", opt\MusicVol, 0)
							e\SoundCHN_isStream[1] = True
							PlayNewDialogue(102,%11)
						EndIf
					Case 3 ;Player is downstairs, so MTF units would sink in and 106 appears
						If EntityDistanceSquared(Collider, e\room\obj) < 0.5 Then
							ShowEntity e\room\RoomDoors[1]\obj
							HideEntity e\room\Objects[21]
							For i = 1 To 2
								e\room\RoomDoors[i]\open = False
								e\room\RoomDoors[i]\fastopen = True
							Next
							If e\room\RoomDoors[1]\openstate > 123.0 Then
								e\room\RoomDoors[1]\openstate = 123.0
								PositionEntity e\room\RoomDoors[1]\obj, EntityX(e\room\Objects[21],True), EntityY(e\room\Objects[21],True), EntityZ(e\room\Objects[21],True)
							EndIf
							
							If e\Sound[0] <> 0 Then FreeSound_Strict(e\Sound[0]) : e\Sound[0] = 0
							e\Sound[0] = LoadSound_Strict("SFX\Door\DoorSparks.ogg")
							e\SoundCHN[0] = PlaySound2(e\Sound[0],Camera,e\room\RoomDoors[1]\obj,5)
							PlaySound_Strict(AlarmSFX[6])
							PlaySound_Strict LoadTempSound("SFX\Door\DoorClose079.ogg")
							
							e\EventState[0] = 4
						EndIf
				End Select
				If e\EventState[2] = 2 Then
					Curr106\Idle = False
					Curr106\State[0] = -10
					PlaySound_Strict LoadTempSound("SFX\General\WaveStart.ogg")
					RemoveNPC(e\room\NPC[0])
					RemoveNPC(e\room\NPC[1])
					e\EventState[2] = 3
				EndIf
				If ChannelPlaying(e\SoundCHN[0])
					UpdateSoundOrigin(e\SoundCHN[0],Camera,e\room\RoomDoors[1]\obj,5)
				EndIf;---------------------------------------------------------------------
			Else
				Select e\EventState[0]
					Case 0 ;Room hasn't been entered before
						For i = 0 To 1
							If e\room\NPC[i] = Null Then
								For n = Each NPCs
									If n\NPCtype = NPC_NTF And i = (n\PrevState - 1) Then
										If EntityDistanceSquared(n\Collider, e\room\RoomDoors[2]\frameobj) < PowTwo(0.4) Then
											e\room\NPC[i] = n
											e\room\NPC[i]\State[0] = MTF_TOTARGET
											e\room\NPC[i]\EnemyX = EntityX(e\room\Objects[i + 22], True)
											e\room\NPC[i]\EnemyY = EntityY(e\room\Objects[i + 22], True)
											e\room\NPC[i]\EnemyZ = EntityZ(e\room\Objects[i + 22], True)
											Exit
										EndIf
									EndIf
								Next
							EndIf
						Next
						
						If e\room\NPC[0] <> Null And e\room\NPC[1] <> Null Then
							;TODO: Play audio of MTF units going to the elevator doors
							PlayNewDialogue(100,%011101)
							mtfd\Enabled = False
							n.NPCs = CreateNPC(NPC_Human,EntityX(e\room\Objects[14],True),EntityY(e\room\Objects[14],True),EntityZ(e\room\Objects[14],True))
							RotateEntity n\Collider,0,e\room\angle-90,0
							n\State[0] = 3
							;ChangeNPCTextureID(n,9)
							SetNPCFrame(n,558)
							n\IsDead = True
							e\EventState[0] = 1
							EndTask(TASK_NTF_CORE)
						EndIf
						
						;The door is open, but not all of the 2 MTF units have arrived, teleport them into the gateway
						If e\room\RoomDoors[12]\open Then
							For n = Each NPCs
								If n\NPCtype = NPC_NTF And n\State[0] <> MTF_TOTARGET And EntityDistanceSquared(n\Collider, e\room\RoomDoors[2]\frameobj) > PowTwo(4.0) Then
									PositionEntity n\Collider, e\room\x, e\room\y + 0.5, e\room\z
									ResetEntity n\Collider
									n\PathStatus = 0
									n\PathLocation = 0
									n\PathTimer = 0
									n\State[0] = MTF_FOLLOWPLAYER ;Set to default state to use pathfinding until the gateway has been passed through the doors with the sparks
								EndIf
							Next
						EndIf
					Case 1 ;MTF units go to elevator doors and player goes up to place the fuse
						If (Not TaskExists(TASK_NTF_FIX_ELEVATOR)) And (Not mtfd\IsPlaying) Then
							PlayNewDialogue(101,%1110)
							BeginTask(TASK_NTF_FIX_ELEVATOR)
						EndIf
						For fb = Each FuseBox
							If fb\obj = e\room\Objects[15] Then
								If fb\fuses = 3 Then
									For i = 16 To 18
										de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[i], True), e\room\y + 0.01, EntityZ(e\room\Objects[i], True), 90, Rand(360), 0)
										de\Size = 0.1 : de\SizeChange = 0.01 : de\MaxSize = 1.25 : EntityAlpha(de\obj, 1.0)
									Next
									de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[18], True), e\room\y + 0.01, EntityZ(e\room\Objects[18], True), 90, Rand(360), 0)
									de\Size = 0.1 : de\SizeChange = 0.01 : de\MaxSize = 1.75 : EntityAlpha(de\obj, 1.0)
									e\room\NPC[0]\State[0] = STATE_SCRIPT
									e\room\NPC[1]\State[0] = STATE_SCRIPT
									If e\Sound[0] <> 0 Then FreeSound_Strict(e\Sound[0]) : e\Sound[0] = 0
									e\Sound[0] = LoadSound_Strict("SFX\Room\Checkpoint\MuffledScream.ogg")
									e\SoundCHN[0] = PlaySound_Strict(e\Sound[0])
									e\SoundCHN[1] = StreamSound_Strict("SFX\Room\Checkpoint\RadioScream.ogg", opt\VoiceVol, 0)
									e\SoundCHN_isStream[1] = True
									StopChannel(mtfd\CurrentChannel)
									mtfd\CurrentProgress = 0
									mtfd\CurrentSequence = 0
									mtfd\CurrentDialogue = 0
									EndTask(TASK_NTF_FIX_ELEVATOR)
									e\EventState[0] = 2
								EndIf
								Exit
							EndIf
						Next
						If e\EventState[1] = 0 Then
							If EntityDistanceSquared(Collider, e\room\Objects[14]) < 5.0 Then
								e\EventState[1] = 1
							EndIf	
						ElseIf e\EventState[1] = 1 Then
							If EntityDistanceSquared(Collider, e\room\RoomDoors[12]\frameobj) < PowTwo(0.5) Then
								;PlayNewDialogue(101,%11)
								;TODO: Dialogue for forgor to put fuse in LOL
								e\EventState[1] = 2
							EndIf
						EndIf
						If e\EventState[2] = 0 Then
							For i = 0 To MaxItemAmount-1
								If Inventory[i] <> Null Then
									If Inventory[i]\itemtemplate\tempname = "fuse" Then
										PlaySound_Strict LoadTempSound("SFX\SCP\106\Laugh.ogg")
										e\EventState[2] = 1
										Exit
									EndIf
								EndIf
							Next
						EndIf	
					Case 2 ;Fuse has been placed, 106 sound appears with MTF screams and door below locks
						If ChannelPlaying(e\SoundCHN[0])
							UpdateSoundOrigin(e\SoundCHN[0],Camera,e\room\NPC[1]\obj,10,1.5)
						EndIf
						If EntityDistanceSquared(Collider, e\room\RoomDoors[12]\frameobj) < PowTwo(0.5) Then
							e\EventState[0] = 3
							e\EventState[2] = 2
							;Spawn 106 between both MTF units
							PositionEntity(Curr106\Collider, EntityX(e\room\Objects[19], True), e\room\y + 0.15, EntityZ(e\room\Objects[19], True), True)
							ResetEntity(Curr106\Collider)
							PositionEntity(e\room\NPC[0]\Collider, EntityX(e\room\Objects[17], True), EntityY(e\room\NPC[0]\Collider, True), EntityZ(e\room\Objects[17], True), True)
							PositionEntity(e\room\NPC[1]\Collider, EntityX(e\room\Objects[18], True), EntityY(e\room\NPC[1]\Collider, True), EntityZ(e\room\Objects[18], True), True)
							RotateEntity e\room\NPC[0]\Collider, 0, 180, 0
							RotateEntity e\room\NPC[1]\Collider, 0, 180, 0
							ResetEntity e\room\NPC[0]\Collider
							ResetEntity e\room\NPC[1]\Collider
							e\SoundCHN[1] = StreamSound_Strict("SFX\Music\SuspenseEnd.ogg", opt\MusicVol, 0)
							e\SoundCHN_isStream[1] = True
							PlayNewDialogue(102,%11)
						ElseIf EntityDistanceSquared(Collider, e\room\RoomDoors[12]\frameobj) < PowTwo(4.0) Then
							If ChannelPlaying(e\SoundCHN[0]) Then
								StopChannel(e\SoundCHN[0])
								StopStream_Strict(e\SoundCHN[1])
								e\SoundCHN[1] = StreamSound_Strict("SFX\Room\Checkpoint\RadioCutoff.ogg", opt\VoiceVol, 0)
								e\SoundCHN_isStream[1] = True
							EndIf	
						EndIf
					Case 3 ;Player is downstairs, so MTF units would sink in and 106 appears
						If EntityDistanceSquared(Collider, e\room\obj) < 0.5 Then
							ShowEntity e\room\RoomDoors[1]\obj
							HideEntity e\room\Objects[21]
							For i = 1 To 2
								e\room\RoomDoors[i]\open = False
								e\room\RoomDoors[i]\fastopen = True
							Next
							If e\room\RoomDoors[1]\openstate > 123.0 Then
								e\room\RoomDoors[1]\openstate = 123.0
								PositionEntity e\room\RoomDoors[1]\obj, EntityX(e\room\Objects[21],True), EntityY(e\room\Objects[21],True), EntityZ(e\room\Objects[21],True)
							EndIf
							
							If e\Sound[0] <> 0 Then FreeSound_Strict(e\Sound[0]) : e\Sound[0] = 0
							e\Sound[0] = LoadSound_Strict("SFX\Door\DoorSparks.ogg")
							e\SoundCHN[0] = PlaySound2(e\Sound[0],Camera,e\room\RoomDoors[1]\obj,5)
							PlaySound_Strict(AlarmSFX[6])
							PlaySound_Strict LoadTempSound("SFX\Door\DoorClose079.ogg")
							
							e\EventState[0] = 4
						EndIf
				End Select
				If e\EventState[2] = 2 Then
					AnimateNPC(e\room\NPC[0],2021,2055,0.1,False)
					AnimateNPC(e\room\NPC[1],1910,2020,0.4,False)
					RemoveNPCGun(e\room\NPC[1])
					If AnimTime(e\room\NPC[0]\obj) >= 2055 Then
						Curr106\Idle = False
						Curr106\State[0] = -10
						PlaySound_Strict LoadTempSound("SFX\General\WaveStart.ogg")
						RemoveNPC(e\room\NPC[0])
						RemoveNPC(e\room\NPC[1])
						e\EventState[2] = 3
					Else
						PointEntity(Curr106\Collider, Camera)
						PositionEntity(Curr106\obj, EntityX(Curr106\Collider), EntityY(Curr106\Collider) - 0.15, EntityZ(Curr106\Collider))
						RotateEntity Curr106\obj, 0, EntityYaw(Curr106\Collider), 0
						AnimateNPC(Curr106, 334, 420, -0.25)
					EndIf
				EndIf
				If ChannelPlaying(e\SoundCHN[0])
					UpdateSoundOrigin(e\SoundCHN[0],Camera,e\room\RoomDoors[1]\obj,5)
				EndIf
			EndIf
		EndIf
			;-------------------------------------------
		If e\EventState[0] < 4 Then
			HideEntity e\room\RoomDoors[1]\obj
			ShowEntity e\room\Objects[21]
			For i = 1 To 2
				e\room\RoomDoors[i]\open = True
			Next
			If ParticleAmount > 0 Then
				If Rand(10)=1 Then
					Local pvt% = CreatePivot()
					PositionEntity(pvt, EntityX(e\room\Objects[20],True), EntityY(e\room\Objects[20],True)+Rnd(0.0,0.05), EntityZ(e\room\Objects[20],True))
					RotateEntity(pvt, 0, EntityYaw(e\room\Objects[20],True)+270, 0)
					MoveEntity pvt,0,0,0.2
					
					For i = 0 To (1+(2*(ParticleAmount-1)))
						p.Particles = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 7, 0.002, 0, 25)
						p\speed = Rnd(0.01,0.05)
						RotateEntity(p\pvt, Rnd(-45,0), EntityYaw(pvt)+Rnd(-10.0,10.0), 0)
						p\size = 0.0075
						ScaleSprite p\obj,p\size,p\size
						p\Achange = -0.05
					Next
					
					pvt = FreeEntity_Strict(pvt)
				EndIf
			EndIf
			
			If e\EventState[0] < 3 Then
				Curr106\Idle = True
			EndIf
			If e\EventState[0] > 1 And e\EventState[2] < 3 Then
				ShouldPlay = MUS_SUSPENSE
			EndIf
		EndIf
		
		Local itb
		
		If FallTimer < -250.0 Then
			Curr106\State[0] = 70 * 60 * Rand(12,17)
			If (Not Curr173\Contained) Then
				Curr173\Idle = SCP173_ACTIVE
			EndIf
			ResetControllerSelections()
			DropSpeed = 0
			FlushKeys()
			FlushMouse()
			FlushJoy()
			ResetInput()
			MoveToPocketDimension()
			psp\Checkpoint106Passed = True
			SaveGame(SavePath + CurrSave\Name + "\", True)
			ClearInventory()
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D