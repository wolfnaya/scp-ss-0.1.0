Function FillRoom_ClassD_Cells_Checkpoint(r.Rooms)
	Local d.Doors, ne.NewElevator,it.Items,i
	
	Local ElevatorOBJ = LoadRMesh("GFX\map\Elevators\elevator_cabin.rmesh",Null)
	HideEntity ElevatorOBJ
	r\Objects[0] = CopyEntity(ElevatorOBJ,r\obj)
	
	PositionEntity(r\Objects[0],-1073,1,5041)
	RotateEntity r\Objects[0],0,90,0
	EntityType r\Objects[0],HIT_MAP
	EntityPickMode r\Objects[0],2
	
	CreateDarkSprite(r, 1)
	
	r\Objects[2] = LoadMesh_Strict("GFX\map\rooms\class_d_cells\class_d_cells_entrance_screen.b3d")
	PositionEntity r\Objects[2],r\x,r\y,r\z,True
	ScaleEntity r\Objects[2],RoomScale,RoomScale,RoomScale
	EntityParent r\Objects[2],r\obj
	
	d = CreateDoor(r\zone,r\x-755*RoomScale,r\y,r\z+5808*RoomScale,270,r,False,DOOR_ELEVATOR_3FLOOR)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x-1436*RoomScale,r\y,r\z+1003*RoomScale,270,r,False,DOOR_OFFICE_2)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x-864*RoomScale,r\y,r\z+1398*RoomScale,0,r,False,DOOR_OFFICE)
	
	d = CreateDoor(r\zone,r\x+864*RoomScale,r\y,r\z+3354*RoomScale,180,r,False,DOOR_OFFICE)
	
	d = CreateDoor(r\zone,r\x-874*RoomScale,r\y,r\z+3984*RoomScale,270,r,False,DOOR_OFFICE)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+478*RoomScale,r\y,r\z+7312*RoomScale,0,r,False,DOOR_OFFICE)
	
	d = CreateDoor(r\zone,r\x+1767*RoomScale,r\y+419*RoomScale,r\z-448*RoomScale,90,r,False,DOOR_OFFICE,KEY_CARD_3)
	
	d = CreateDoor(r\zone,r\x+1767*RoomScale,r\y+419*RoomScale,r\z+448*RoomScale,90,r,False,DOOR_OFFICE,KEY_CARD_3)
	
	d = CreateDoor(r\zone,r\x+1447*RoomScale,r\y+419*RoomScale,r\z+880*RoomScale,0,r,False,DOOR_OFFICE_2)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+1447*RoomScale,r\y+419*RoomScale,r\z-960*RoomScale,180,r,False,DOOR_OFFICE_2)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x-271*RoomScale,r\y,r\z-463*RoomScale,270,r,False,DOOR_CLASSIC,KEY_CARD_3)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x-271*RoomScale,r\y,r\z+463*RoomScale,270,r,True,DOOR_CLASSIC,KEY_CARD_3)
	
	d = CreateDoor(r\zone,r\x,r\y,r\z+1024*RoomScale,0,r,False,DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x,r\y,r\z+3728*RoomScale,0,r,False,DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+448*RoomScale,r\y,r\z+3984*RoomScale,90,r,False,DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+1127*RoomScale,r\y+419*RoomScale,r\z,90,r,False,DOOR_CLASSIC)
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x-787.0 * RoomScale, r\y+1*RoomScale, r\z+5040*RoomScale, 270, r, True, DOOR_ELEVATOR_3FLOOR, False, "", 1)
	r\RoomDoors[0]\DisableWaypoint = True
	MoveEntity(r\RoomDoors[0]\buttons[0], -25, 0, -2.25)
	
	ne = CreateNewElevator(r\Objects[0],3,r\RoomDoors[0],1,r,-12000.0,-9000.0,0.0)
	ne\floorlocked[0] = True
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x,r\y,r\z - 256.0*RoomScale,180,r,True,DOOR_WINDOWED,False,"1234")
	r\RoomDoors[2] = CreateDoor(r\zone,r\x,r\y,r\z + 256.0*RoomScale,0,r,False,DOOR_WINDOWED,False,"1234")
	For i = 0 To 1
		FreeEntity r\RoomDoors[1]\buttons[i] : r\RoomDoors[1]\buttons[i] = 0
		FreeEntity r\RoomDoors[2]\buttons[i] : r\RoomDoors[2]\buttons[i] = 0
	Next
	r\RoomDoors[1]\open = True
	r\RoomDoors[2]\open = False
	
	r\Objects[10] = CreatePivot()
	PositionEntity r\Objects[10],r\x,r\y+128.0*RoomScale,r\z,True
	EntityParent r\Objects[10],r\obj
	
	r\Objects[11] = CreateButton(r\x + 172.0 * RoomScale, r\y + 172 * RoomScale, r\z, 0, 270, 0, BUTTON_KEYCARD)
	EntityParent(r\Objects[11],r\obj)
	
	r\Objects[12] = CreateButton(r\x - 357.0 * RoomScale, r\y + 114 * RoomScale, r\z+93*RoomScale, 434, 270, 0, BUTTON_KEYCARD)
	EntityParent(r\Objects[12],r\obj)
	
	r\Objects[13] = CreateButton(r\x + 139.0 * RoomScale, r\y + 198 * RoomScale, r\z-288*RoomScale, 0, 0, 0, BUTTON_KEYCARD)
	EntityParent(r\Objects[13],r\obj)
	
	r\Objects[14] = CreatePivot()
	PositionEntity r\Objects[14],r\x+128*RoomScale,r\y,r\z+2377*RoomScale,True
	EntityParent r\Objects[14],r\obj
	
	r\Objects[15] = CreatePivot()
	PositionEntity r\Objects[15],r\x-128*RoomScale,r\y,r\z+2377*RoomScale,True
	EntityParent r\Objects[15],r\obj
	
End Function

Function UpdateEvent_ClassD_Cells_Checkpoint(e.Events)
	Local i,n,ne.NewElevator,p.Particles,pvt
	
	If PlayerRoom = e\room Then
		
	; ~ [Story event]
		
		If e\EventState[1] = 0 Then
			If TaskExists(TASK_FIND_CLASS_D_ZONE) Then
				EndTask(TASK_FIND_CLASS_D_ZONE)
				BeginTask(TASK_FIND_NOTE)
			EndIf
			e\EventState[1] = 1
		EndIf
		
	; ~ [Screen]
		
		If EntityHidden(e\room\Objects[2]) Then ShowEntity e\room\Objects[2]
		
		e\EventState[0] = e\EventState[0] + FPSfactor
		
		If e\EventState[0] > 0 Then
			EntityTexture e\room\Objects[2], Class_D_Screen[0], Floor(((e\EventState[0]-70*5)/70) Mod 4.0)
		EndIf
		
	; ~ [Metal detector]
		
		For n = 14 To 15
			If g_I\HoldingGun > GUN_UNARMED Then
				If EntityDistanceSquared(e\room\Objects[n], Collider) < PowTwo(0.6) Then
					If e\SoundCHN[1] = 0 Then
						e\SoundCHN[1] = PlaySound_Strict(AlarmSFX[5])
					Else
						If (Not ChannelPlaying(e\SoundCHN[1])) Then e\SoundCHN[1] = PlaySound_Strict(AlarmSFX[5])
					EndIf
				Else
					StopStream_Strict(e\SoundCHN[1]) : e\SoundCHN[1] = 0
				EndIf
			Else
				StopStream_Strict(e\SoundCHN[1]) : e\SoundCHN[1] = 0
			EndIf
		Next
		
	; ~ [Airlock]
		
		If e\EventState[12] = 0.0
			
			UpdateButton(e\room\Objects[11])
			UpdateButton(e\room\Objects[12])
			UpdateButton(e\room\Objects[13])
			
			If d_I\ClosestButton = e\room\Objects[11] Lor d_I\ClosestButton = e\room\Objects[12] Lor d_I\ClosestButton = e\room\Objects[13] Then
				If KeyHitUse Then
					If SelectedItem <> Null And (SelectedItem\itemtemplate\tempname = "key_class_d" Lor SelectedItem\itemtemplate\tempname = "scp005") Then
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
						CreateMsg(GetLocalString("Doors","keycard_required3"))
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
					For i = 0 To 1
						
						If i = 0
							PositionEntity pvt%,0.0,318.0,-160.0,False
						Else
							PositionEntity pvt%,0.0,318.0,160.0,False
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
						PlaySound_Strict(AlarmSFX[11])
					Else
						UseDoor(e\room\RoomDoors[1])
						PlaySound_Strict(AlarmSFX[11])
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
		
		; ~ [Elevator]
		
		Local e2.Events
		
		If e\EventState[14] = 0 And EntityY(Collider) > 6600.0*RoomScale Lor EntityY(Collider) <- 6600.0*RoomScale Then
			e\EventState[15] = e\EventState[15] + (0.01*FPSfactor)
			EntityAlpha e\room\Objects[1],Min(e\EventState[15],1.0)
			If e\EventState[15] > 1.05 Then
				SaveGame(SavePath + CurrSave\Name + "\", True)
				Local prevZone = gopt\CurrZone
				For ne = Each NewElevator
					If PlayerNewElevator = ne\ID And ne\room = e\room Then
						Select ne\tofloor
							Case 3
								gopt\CurrZone = LCZ
							Case 2
								gopt\CurrZone = CLASSD_CELLS
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
				Local playerElev = PlayerNewElevator
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
						Local translation# = 6500.0
						Select prevZone
							Case LCZ
								TranslateEntity ne\obj, 0, translation, 0
							Case CLASSD_CELLS
								If gopt\CurrZone = BCZ Then
									TranslateEntity ne\obj, 0, -translation, 0
								Else
									TranslateEntity ne\obj, 0, translation, 0
								EndIf
						End Select
						Select gopt\CurrZone
							Case LCZ
								ne\tofloor = 3
								ne\currfloor = 2
							Case CLASSD_CELLS
								ne\tofloor = 2
								If prevZone = LCZ Then
									ne\currfloor = 3
								Else
									ne\currfloor = 1
								EndIf
						End Select
						RotateEntity Collider,0,180,0
						TeleportEntity(Collider,EntityX(ne\obj,True),EntityY(ne\obj,True)+0.5,EntityZ(ne\obj,True),0.3,True)
						StopStream_Strict(ne\soundchn)
						;ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Checkpoint\Loop.ogg",opt\SFXVolume,Mode)
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
						e2\EventState[15] = 1.05
						e2\EventState[14] = 1
						Exit
					EndIf
				Next
				SaveGame(SavePath + CurrSave\Name + "\", True)
				Return
			EndIf
		Else
			e\EventState[15] = Max(e\EventState[15] - (0.01*FPSfactor), 0.0)
			EntityAlpha e\room\Objects[1],Min(e\EventState[15],1.0)
			If e\room\RoomDoors[0]\open Then
				e\EventState[14] = 0
			EndIf
		EndIf
		
	Else
		If (Not EntityHidden(e\room\Objects[2])) Then HideEntity e\room\Objects[2]
	EndIf
	
End Function

Const ClassD_Cells_Elevator = 0
Const ClassD_Cells_Dark_Sprite = 1
Const ClassD_Cells_Screen = 2

Const ClassD_Cells_Room_2 = 3
Const ClassD_Cells_Room_3 = 4
Const ClassD_Cells_Room_4 = 5

Const ClassD_Cells_Part2_Pivot_1 = 6
Const ClassD_Cells_Part2_Pivot_2 = 7

Const ClassD_Cells_Part3_Pivot_1 = 8
Const ClassD_Cells_Part3_Pivot_2 = 9

Const ClassD_Cells_Part4_Pivot_1 = 10
Const ClassD_Cells_Part4_Pivot_2 = 11
Const ClassD_Cells_Part4_Pivot_3 = 12
Const ClassD_Cells_Part4_Pivot_4 = 13

Const ClassD_Cells_Part3_Cell_Door = 14
Const ClassD_Cells_Guard_Spawn = 15
Const ClassD_Cells_Guard_Waypoint = 16

Const ClassD_Cells_SCP330 = 17

Const ClassD_Cells_Table = 18

Const ClassD_Cells_Door_Elevator = 0
Const ClassD_Cells_Door_Part_2 = 1
Const ClassD_Cells_Door_Part_3 = 2
Const ClassD_Cells_Door_Part_4 = 3
Const ClassD_Cells_Door_Part_4_2 = 4
Const ClassD_Cells_Cell_Door = 5

Function FillRoom_ClassD_Cells_Zone(r.Rooms)
	Local ne.NewElevator, d.Doors, i%, it.Items
	
	; ~ [Elevator & Misc]
	
	Local ElevatorOBJ = LoadRMesh("GFX\map\Elevators\elevator_cabin_2.rmesh",Null)
	HideEntity ElevatorOBJ
	r\Objects[ClassD_Cells_Elevator] = CopyEntity(ElevatorOBJ,r\obj)
	
	PositionEntity(r\Objects[ClassD_Cells_Elevator],-1073,4127,5041)
	RotateEntity r\Objects[ClassD_Cells_Elevator],0,90,0
	EntityType r\Objects[ClassD_Cells_Elevator],HIT_MAP
	EntityPickMode r\Objects[ClassD_Cells_Elevator],2
	
	CreateDarkSprite(r, ClassD_Cells_Dark_Sprite)
	
	r\Objects[ClassD_Cells_Screen] = LoadMesh_Strict("GFX\map\rooms\class_d_cells\class_d_cells_entrance_screen.b3d")
	PositionEntity r\Objects[ClassD_Cells_Screen],r\x,r\y+4114*RoomScale,r\z,True
	ScaleEntity r\Objects[ClassD_Cells_Screen],RoomScale,RoomScale,RoomScale
	EntityParent r\Objects[ClassD_Cells_Screen],r\obj
	
	r\RoomDoors[ClassD_Cells_Door_Elevator] = CreateDoor(r\zone,r\x-787.0*RoomScale, r\y+4127*RoomScale, r\z+5040*RoomScale, 270, r, True, DOOR_ELEVATOR_3FLOOR, False, "", 1)
	r\RoomDoors[ClassD_Cells_Door_Elevator]\DisableWaypoint = True
	MoveEntity(r\RoomDoors[ClassD_Cells_Door_Elevator]\buttons[0], -25, 0, -2.25)
	
	ne = CreateNewElevator(r\Objects[ClassD_Cells_Elevator],2,r\RoomDoors[ClassD_Cells_Door_Elevator],1,r,0.0,4126.0,9000.0)
	ne\floorlocked[0] = True
	
	r\Objects[ClassD_Cells_SCP330] = CreatePivot()
	PositionEntity r\Objects[ClassD_Cells_SCP330], r\x+3434*RoomScale,r\y+4230*RoomScale,r\z+4215*RoomScale,True
	EntityParent r\Objects[ClassD_Cells_SCP330], r\obj
	
	r\Objects[ClassD_Cells_Table] = CreatePivot()
	PositionEntity r\Objects[ClassD_Cells_Table], r\x+2700*RoomScale,r\y+4258*RoomScale,r\z+7352*RoomScale,True
	EntityParent r\Objects[ClassD_Cells_Table], r\obj
	
	; ~ [Areas of the map]
	
	r\RoomDoors[ClassD_Cells_Door_Part_2] = CreateDoor(r\zone,r\x+1024.0*RoomScale, r\y+4114*RoomScale, r\z+2704*RoomScale, 90, r, False, DOOR_CLASSIC)
	
	r\Objects[ClassD_Cells_Part3_Cell_Door] = LoadRMesh("GFX\map\rooms\class_d_cells\class_d_cells_zone_part_3_cell_door.rmesh", Null)
	ScaleEntity r\Objects[ClassD_Cells_Part3_Cell_Door], RoomScale, RoomScale, RoomScale
	PositionEntity r\Objects[ClassD_Cells_Part3_Cell_Door], r\x,r\y,r\z,True
	EntityParent r\Objects[ClassD_Cells_Part3_Cell_Door], r\obj
	EntityType(GetChild(r\Objects[ClassD_Cells_Part3_Cell_Door], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[ClassD_Cells_Part3_Cell_Door], 2), 2)
	HideEntity r\Objects[ClassD_Cells_Part3_Cell_Door]
	
	r\RoomDoors[ClassD_Cells_Cell_Door] = CreateDoor(r\zone,r\x+14369*RoomScale, r\y+4114*RoomScale, r\z+302*RoomScale, 0, r, False, DOOR_CELL,KEY_CARD_3)
	
	r\Objects[ClassD_Cells_Guard_Spawn] = CreatePivot()
	PositionEntity r\Objects[ClassD_Cells_Guard_Spawn], r\x+13626*RoomScale,r\y+4250*RoomScale,r\z+656*RoomScale,True
	EntityParent r\Objects[ClassD_Cells_Guard_Spawn], r\obj
	
	r\Objects[ClassD_Cells_Guard_Waypoint] = CreatePivot()
	PositionEntity r\Objects[ClassD_Cells_Guard_Waypoint], r\x+14769*RoomScale,r\y+4146*RoomScale,r\z+656*RoomScale,True
	EntityParent r\Objects[ClassD_Cells_Guard_Waypoint], r\obj
	
	Local rt.RoomTemplates = New RoomTemplates
	Local lt.LightTemplates, newlt, tw.TempWayPoints
	
	rt\objPath = "GFX\map\rooms\class_d_cells\class_d_cells_zone_part_2.rmesh"
	rt\obj = LoadRMesh(rt\objPath,rt)
	r\Objects[ClassD_Cells_Room_2] = rt\obj
	ScaleEntity(r\Objects[ClassD_Cells_Room_2], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[ClassD_Cells_Room_2], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[ClassD_Cells_Room_2], 2), 2)
	PositionEntity(r\Objects[ClassD_Cells_Room_2],r\x,r\y,r\z)
	EntityParent r\Objects[ClassD_Cells_Room_2],r\obj
	For lt.LightTemplates = Each LightTemplates
		If lt\roomtemplate = rt Then
			newlt = AddLight(r, r\x+lt\x, r\y+lt\y, r\z+lt\z, lt\ltype, lt\range, lt\r, lt\g, lt\b, r\Objects[ClassD_Cells_Room_2])
			If newlt <> 0 Then
				If lt\ltype = 3 Then
					LightConeAngles(newlt, lt\innerconeangle, lt\outerconeangle)
					RotateEntity(newlt, lt\pitch, lt\yaw, 0)
				EndIf
			EndIf
		EndIf
	Next
	For tw.TempWayPoints = Each TempWayPoints
		If tw\roomtemplate = rt Then
			CreateWaypoint(r\x+tw\x, r\y+tw\y, r\z+tw\z, Null, r)
		EndIf
	Next
	For i = 0 To MaxRoomEmitters-1
		If r\RoomTemplate\TempSoundEmitter[i]<>0 Then
			r\SoundEmitterObj[i]=CreatePivot(r\Objects[ClassD_Cells_Room_2])
			PositionEntity r\SoundEmitterObj[i], r\x+rt\TempSoundEmitterX[i],r\y+rt\TempSoundEmitterY[i],r\z+rt\TempSoundEmitterZ[i],True
			EntityParent(r\SoundEmitterObj[i],r\Objects[ClassD_Cells_Room_2])
			
			r\SoundEmitter[i] = r\RoomTemplate\TempSoundEmitter[i]
			r\SoundEmitterRange[i] = r\RoomTemplate\TempSoundEmitterRange[i]
		EndIf
	Next
	Delete rt
	HideEntity(r\Objects[ClassD_Cells_Room_2])
	
	r\RoomDoors[ClassD_Cells_Door_Part_3] = CreateDoor(r\zone,r\x+5120.0*RoomScale, r\y+4114*RoomScale, r\z+2704*RoomScale, 90, r, False, DOOR_CLASSIC)
	
	rt.RoomTemplates = New RoomTemplates
	
	rt\objPath = "GFX\map\rooms\class_d_cells\class_d_cells_zone_part_3.rmesh"
	rt\obj = LoadRMesh(rt\objPath,rt)
	r\Objects[ClassD_Cells_Room_3] = rt\obj
	ScaleEntity(r\Objects[ClassD_Cells_Room_3], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[ClassD_Cells_Room_3], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[ClassD_Cells_Room_3], 2), 2)
	PositionEntity(r\Objects[ClassD_Cells_Room_3],r\x,r\y,r\z)
	EntityParent r\Objects[ClassD_Cells_Room_3],r\obj
	For lt.LightTemplates = Each LightTemplates
		If lt\roomtemplate = rt Then
			newlt = AddLight(r, r\x+lt\x, r\y+lt\y, r\z+lt\z, lt\ltype, lt\range, lt\r, lt\g, lt\b, r\Objects[ClassD_Cells_Room_3])
			If newlt <> 0 Then
				If lt\ltype = 3 Then
					LightConeAngles(newlt, lt\innerconeangle, lt\outerconeangle)
					RotateEntity(newlt, lt\pitch, lt\yaw, 0)
				EndIf
			EndIf
		EndIf
	Next
	For tw.TempWayPoints = Each TempWayPoints
		If tw\roomtemplate = rt Then
			CreateWaypoint(r\x+tw\x, r\y+tw\y, r\z+tw\z, Null, r)
		EndIf
	Next
	For i = 0 To MaxRoomEmitters-1
		If r\RoomTemplate\TempSoundEmitter[i]<>0 Then
			r\SoundEmitterObj[i]=CreatePivot(r\Objects[ClassD_Cells_Room_3])
			PositionEntity r\SoundEmitterObj[i], r\x+rt\TempSoundEmitterX[i],r\y+rt\TempSoundEmitterY[i],r\z+rt\TempSoundEmitterZ[i],True
			EntityParent(r\SoundEmitterObj[i],r\Objects[ClassD_Cells_Room_3])
			
			r\SoundEmitter[i] = r\RoomTemplate\TempSoundEmitter[i]
			r\SoundEmitterRange[i] = r\RoomTemplate\TempSoundEmitterRange[i]
		EndIf
	Next
	Delete rt
	HideEntity(r\Objects[ClassD_Cells_Room_3])
	
	r\RoomDoors[ClassD_Cells_Door_Part_4] = CreateDoor(r\zone,r\x, r\y+4114*RoomScale, r\z+1681*RoomScale, 0, r, False, DOOR_CLASSIC)
	
	r\RoomDoors[ClassD_Cells_Door_Part_4_2] = CreateDoor(r\zone,r\x+4135*RoomScale, r\y+4114*RoomScale, r\z+656*RoomScale, 90, r, False, DOOR_CLASSIC)
	
	rt.RoomTemplates = New RoomTemplates
	
	rt\objPath = "GFX\map\rooms\class_d_cells\class_d_cells_zone_part_4.rmesh"
	rt\obj = LoadRMesh(rt\objPath,rt)
	r\Objects[ClassD_Cells_Room_4] = rt\obj
	ScaleEntity(r\Objects[ClassD_Cells_Room_4], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[ClassD_Cells_Room_4], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[ClassD_Cells_Room_4], 2), 2)
	PositionEntity(r\Objects[ClassD_Cells_Room_4],r\x,r\y,r\z)
	EntityParent r\Objects[ClassD_Cells_Room_4],r\obj
	For lt.LightTemplates = Each LightTemplates
		If lt\roomtemplate = rt Then
			newlt = AddLight(r, r\x+lt\x, r\y+lt\y, r\z+lt\z, lt\ltype, lt\range, lt\r, lt\g, lt\b, r\Objects[ClassD_Cells_Room_4])
			If newlt <> 0 Then
				If lt\ltype = 3 Then
					LightConeAngles(newlt, lt\innerconeangle, lt\outerconeangle)
					RotateEntity(newlt, lt\pitch, lt\yaw, 0)
				EndIf
			EndIf
		EndIf
	Next
	For tw.TempWayPoints = Each TempWayPoints
		If tw\roomtemplate = rt Then
			CreateWaypoint(r\x+tw\x, r\y+tw\y, r\z+tw\z, Null, r)
		EndIf
	Next
	For i = 0 To MaxRoomEmitters-1
		If r\RoomTemplate\TempSoundEmitter[i]<>0 Then
			r\SoundEmitterObj[i]=CreatePivot(r\Objects[ClassD_Cells_Room_4])
			PositionEntity r\SoundEmitterObj[i], r\x+rt\TempSoundEmitterX[i],r\y+rt\TempSoundEmitterY[i],r\z+rt\TempSoundEmitterZ[i],True
			EntityParent(r\SoundEmitterObj[i],r\Objects[ClassD_Cells_Room_4])
			
			r\SoundEmitter[i] = r\RoomTemplate\TempSoundEmitter[i]
			r\SoundEmitterRange[i] = r\RoomTemplate\TempSoundEmitterRange[i]
		EndIf
	Next
	Delete rt
	HideEntity(r\Objects[ClassD_Cells_Room_4])
	
	; ~ [Zone detection triggers]
	
	r\Objects[ClassD_Cells_Part2_Pivot_1] = CreatePivot() ; ~ Player in Part 1
	PositionEntity r\Objects[ClassD_Cells_Part2_Pivot_1],r\x+889*RoomScale,r\y+4118*RoomScale,r\z+2705*RoomScale,True
	EntityParent r\Objects[ClassD_Cells_Part2_Pivot_1], r\obj
	
	r\Objects[ClassD_Cells_Part2_Pivot_2] = CreatePivot() ; ~ Player in Part 2
	PositionEntity r\Objects[ClassD_Cells_Part2_Pivot_2],r\x+1134*RoomScale,r\y+4118*RoomScale,r\z+2705*RoomScale,True
	EntityParent r\Objects[ClassD_Cells_Part2_Pivot_2], r\obj
	
	r\Objects[ClassD_Cells_Part3_Pivot_1] = CreatePivot() ; ~ Player in Part 2
	PositionEntity r\Objects[ClassD_Cells_Part3_Pivot_1],r\x+4988*RoomScale,r\y+4118*RoomScale,r\z+2705*RoomScale,True
	EntityParent r\Objects[ClassD_Cells_Part3_Pivot_1], r\obj
	
	r\Objects[ClassD_Cells_Part3_Pivot_2] = CreatePivot() ; ~ Player in Part 3
	PositionEntity r\Objects[ClassD_Cells_Part3_Pivot_2],r\x+5245*RoomScale,r\y+4118*RoomScale,r\z+2705*RoomScale,True
	EntityParent r\Objects[ClassD_Cells_Part3_Pivot_2], r\obj
	
	r\Objects[ClassD_Cells_Part4_Pivot_1] = CreatePivot() ; ~ Player in Part 3
	PositionEntity r\Objects[ClassD_Cells_Part4_Pivot_1],r\x+4263*RoomScale,r\y+4118*RoomScale,r\z+655*RoomScale,True
	EntityParent r\Objects[ClassD_Cells_Part4_Pivot_1], r\obj
	
	r\Objects[ClassD_Cells_Part4_Pivot_2] = CreatePivot() ; ~ Player in Part 4
	PositionEntity r\Objects[ClassD_Cells_Part4_Pivot_2],r\x+4019*RoomScale,r\y+4118*RoomScale,r\z+655*RoomScale,True
	EntityParent r\Objects[ClassD_Cells_Part4_Pivot_2], r\obj
	
	r\Objects[ClassD_Cells_Part4_Pivot_3] = CreatePivot() ; ~ Player in Part 1
	PositionEntity r\Objects[ClassD_Cells_Part4_Pivot_3],r\x,r\y+4118*RoomScale,r\z+1817*RoomScale,True
	EntityParent r\Objects[ClassD_Cells_Part4_Pivot_3], r\obj;
	
	r\Objects[ClassD_Cells_Part4_Pivot_4] = CreatePivot() ; ~ Player in Part 4
	PositionEntity r\Objects[ClassD_Cells_Part4_Pivot_4],r\x,r\y+4118*RoomScale,r\z+1528*RoomScale,True
	EntityParent r\Objects[ClassD_Cells_Part4_Pivot_4], r\obj
	
	; ~ [Items]
	
	it = CreateItem("Surveillance Room Password Note", "paper", r\x + 10029.0 * RoomScale, r\y + 4274.0 * RoomScale, r\z + 2757.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","crowbar"), "crowbar", r\x + 5210.0 * RoomScale, r\y + 3684.0 * RoomScale, r\z + 2371.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","bat_9v"), "bat", r\x + 3001.0 * RoomScale, r\y + 4398.0 * RoomScale, r\z - 8239.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","radio"), "radio", r\x + 2815.0 * RoomScale, r\y + 4335.0 * RoomScale, r\z - 8239.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	;it = CreateItem("Document", "paper", r\x + 1278.0 * RoomScale, r\y + 4388.0 * RoomScale, r\z - 6998.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","key_1"), "key1", r\x + 1275.0 * RoomScale, r\y + 4483.0 * RoomScale, r\z - 6461.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","key_2"), "key2", r\x + 3386.0 * RoomScale, r\y + 4387.0 * RoomScale, r\z - 5931.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	;it = CreateItem(GetLocalString("Item Names","doc_330"), "paper", r\x + 3537.0 * RoomScale, r\y + 4271.0 * RoomScale, r\z + 4221.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	;it = CreateItem(GetLocalString("Item Names","doc_es045"), "paper", r\x + 5048.0 * RoomScale, r\y + 4297.0 * RoomScale, r\z + 3331.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","bat_9v"), "bat", r\x + 2911.0 * RoomScale, r\y + 4227.0 * RoomScale, r\z + 5910.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","first_aid_blue"), "firstaid2", r\x + 2911.0 * RoomScale, r\y + 4366.0 * RoomScale, r\z + 5888.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	If Rand(0,2) = 0 Then
		
		it = CreateItem(GetLocalString("Item Names","first_aid_blue"), "firstaid2", r\x + 1371.0 * RoomScale, r\y + 4293.0 * RoomScale, r\z + 6937.0 * RoomScale)
		EntityParent(it\collider, r\obj)
		
		it = CreateItem(GetLocalString("Item Names","first_aid_small"), "finefirstaid", r\x + 1705.0 * RoomScale, r\y + 4281.0 * RoomScale, r\z + 5188.0 * RoomScale)
		EntityParent(it\collider, r\obj)
		
		it = CreateItem(GetLocalString("Item Names","doc_sci_closet"), "paper", r\x + 2081.0 * RoomScale, r\y + 4366.0 * RoomScale, r\z + 7067.0 * RoomScale)
		EntityParent(it\collider, r\obj)
		
	ElseIf Rand(0,2) = 1 Then
		
		it = CreateItem(GetLocalString("Item Names","first_aid"), "firstaid", r\x + 2081.0 * RoomScale, r\y + 4366.0 * RoomScale, r\z + 7067.0 * RoomScale)
		EntityParent(it\collider, r\obj)
		
		it = CreateItem(GetLocalString("Item Names","first_aid_blue"), "firstaid2", r\x + 1371.0 * RoomScale, r\y + 4293.0 * RoomScale, r\z + 6937.0 * RoomScale)
		EntityParent(it\collider, r\obj)
		
		it = CreateItem(GetLocalString("Item Names","doc_sci_closet"), "paper", r\x + 1705.0 * RoomScale, r\y + 4281.0 * RoomScale, r\z + 5188.0 * RoomScale)
		EntityParent(it\collider, r\obj)
		
	Else
		
		it = CreateItem(GetLocalString("Item Names","first_aid"), "firstaid", r\x + 2081.0 * RoomScale, r\y + 4366.0 * RoomScale, r\z + 7067.0 * RoomScale)
		EntityParent(it\collider, r\obj)
		
		it = CreateItem(GetLocalString("Item Names","first_aid_small"), "finefirstaid", r\x + 1705.0 * RoomScale, r\y + 4281.0 * RoomScale, r\z + 5188.0 * RoomScale)
		EntityParent(it\collider, r\obj)
		
		it = CreateItem(GetLocalString("Item Names","doc_sci_closet"), "paper", r\x + 1371.0 * RoomScale, r\y + 4293.0 * RoomScale, r\z + 6937.0 * RoomScale)
		
	EndIf
	
	it = CreateItem(GetLocalString("Item Names","Cup"), "cup", r\x+7863*RoomScale,r\y+3901*RoomScale,r\z+1058.0*RoomScale, 87,62,45)
	EntityParent(it\collider, r\obj) : it\name = "Cup of Coffee"
	
	it = CreateItem(GetLocalString("Item Names","cup"), "cup", r\x+7806*RoomScale,r\y+3901*RoomScale,r\z+1642.0*RoomScale, 87,62,45)
	EntityParent(it\collider, r\obj) : it\name = "Cup of Coffee"
	
	it = CreateItem(GetLocalString("Item Names","bat_9v"), "bat", r\x + 1377.0 * RoomScale, r\y + 4293.0 * RoomScale, r\z + 6583.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","key_2"), "key2", r\x + 1791.0 * RoomScale, r\y + 4226.0 * RoomScale, r\z + 6875.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","nav_300"), "nav", r\x + 2247.0 * RoomScale, r\y + 4293.0 * RoomScale, r\z + 6561.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	;it = CreateItem("Origami", "misc", r\x + 1370.0 * RoomScale, r\y + 4227.0 * RoomScale, r\z + 7288.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","wallet"),"wallet", r\x + 3309.0 * RoomScale, r\y + 4170.0 * RoomScale, r\z + 6133.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","vest"), "vest", r\x + 10399.0 * RoomScale, r\y + 4274.0 * RoomScale, r\z + 1987.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","ammo"), "ammocrate", r\x + 9569.0 * RoomScale, r\y + 4178.0 * RoomScale, r\z + 2603.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","ammo"), "ammocrate", r\x + 9569.0 * RoomScale, r\y + 4178.0 * RoomScale, r\z + 2603.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("H&K MP7", "mp7", r\x + 9574.0 * RoomScale, r\y + 4274.0 * RoomScale, r\z + 2459.0 * RoomScale)
	it\state = 40 : it\state2 = 120
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("H&K MP5", "mp5", r\x + 10512.0 * RoomScale, r\y + 4385.0 * RoomScale, r\z + 2626.0 * RoomScale)
	it\state = 30 : it\state2 = 90
	RotateEntity it\collider,0,r\angle,r\angle+90,True
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","toolbox"), "toolbox", r\x + 1380.0 * RoomScale, r\y + 4318.0 * RoomScale, r\z + 4894.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","key_class_d"), "key_class_d", r\x + 10072.0 * RoomScale, r\y + 4266.0 * RoomScale, r\z + 2014.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	If gopt\GameMode <> GAMEMODE_CLASS_D Then
		it = CreateItem(GetLocalString("Item Names","note_d_ecape"), "paper", r\x + 14759.0 * RoomScale, r\y + 4279.0 * RoomScale, r\z - 718.0 * RoomScale)
		EntityParent(it\collider, r\obj)
	EndIf
	
	it = CreateItem(GetLocalString("Item Names","key_omni"), "key6", r\x + 7085.0 * RoomScale, r\y + 4086.0 * RoomScale, r\z + 1743.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	; ~ [Doors]
	
	d = CreateDoor(r\zone,r\x+478*RoomScale, r\y+4114*RoomScale, r\z+7312*RoomScale, 0, r, False, DOOR_OFFICE)
	
	d = CreateDoor(r\zone,r\x-769*RoomScale, r\y+4114*RoomScale, r\z+5808*RoomScale, 90, r, False, DOOR_ELEVATOR)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x-769*RoomScale, r\y+4114*RoomScale, r\z+6576*RoomScale, 90, r, False, DOOR_ELEVATOR)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x-874*RoomScale, r\y+4114*RoomScale, r\z+3984*RoomScale, 90, r, False, DOOR_OFFICE)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x, r\y+4114*RoomScale, r\z+3728*RoomScale, 0, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+2048*RoomScale, r\y+4114*RoomScale, r\z+3659*RoomScale, 180, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+1785*RoomScale, r\y+4114*RoomScale, r\z+4683*RoomScale, -90, r, False, DOOR_OFFICE)
	
	d = CreateDoor(r\zone,r\x+4096*RoomScale, r\y+4114*RoomScale, r\z+2960*RoomScale, 0, r, False, DOOR_OFFICE)
	
	d = CreateDoor(r\zone,r\x+3824*RoomScale, r\y+4114*RoomScale, r\z+3372*RoomScale, -90, r, False, DOOR_CLASSIC, KEY_CARD_3)
	
	d = CreateDoor(r\zone,r\x+4368*RoomScale, r\y+4114*RoomScale, r\z+3372*RoomScale, 90, r, False, DOOR_CLASSIC, KEY_CARD_2)
	
	d = CreateDoor(r\zone,r\x+4724*RoomScale, r\y+4114*RoomScale, r\z+3696*RoomScale, 0, r, False, DOOR_CLASSIC, KEY_CARD_2)
	
	d = CreateDoor(r\zone,r\x+2048*RoomScale, r\y+4114*RoomScale, r\z+5707*RoomScale, 0, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+2048*RoomScale, r\y+4114*RoomScale, r\z+6218*RoomScale, 0, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+2336*RoomScale, r\y+4114*RoomScale, r\z+7203*RoomScale, 90, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+3104*RoomScale, r\y+4114*RoomScale, r\z+6218*RoomScale, 180, r, False, DOOR_CLASSIC, False, "3984")
	
	d = CreateDoor(r\zone,r\x+7152*RoomScale, r\y+4114*RoomScale, r\z+2704*RoomScale, 90, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+3072*RoomScale, r\y+4114*RoomScale, r\z+2704*RoomScale, 90, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+8034*RoomScale, r\y+4114*RoomScale, r\z+3586*RoomScale, 0, r, False, DOOR_CLASSIC)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+5346*RoomScale, r\y+4114*RoomScale, r\z+2460*RoomScale, 0, r, False, DOOR_OFFICE)
	
	d = CreateDoor(r\zone,r\x+5760*RoomScale, r\y+3730*RoomScale, r\z+3823*RoomScale, 0, r, False, DOOR_OFFICE_2)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+5760*RoomScale, r\y+3730*RoomScale, r\z+1585*RoomScale, 0, r, False, DOOR_OFFICE)
	
	d = CreateDoor(r\zone,r\x+5120*RoomScale, r\y+3730*RoomScale, r\z+1265*RoomScale, 90, r, False, DOOR_CLASSIC)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+5760*RoomScale, r\y+3730*RoomScale, r\z+945*RoomScale, 0, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+5760*RoomScale, r\y+3730*RoomScale, r\z+368*RoomScale, 0, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+7139*RoomScale, r\y+3730*RoomScale, r\z+254*RoomScale, 0, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+7395*RoomScale, r\y+3730*RoomScale, r\z-19*RoomScale, 90, r, False, DOOR_CLASSIC)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+6180*RoomScale, r\y+3730*RoomScale, r\z-19*RoomScale, 90, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+8034*RoomScale, r\y+4114*RoomScale, r\z+1424*RoomScale, 0, r, True, DOOR_CLASSIC)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+5030*RoomScale, r\y+4114*RoomScale, r\z+112*RoomScale, 180, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+6182*RoomScale, r\y+4114*RoomScale, r\z+656*RoomScale, 90, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+8229*RoomScale, r\y+4114*RoomScale, r\z+656*RoomScale, 90, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+8902*RoomScale, r\y+4114*RoomScale, r\z+1202*RoomScale, 0, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+8902*RoomScale, r\y+4114*RoomScale, r\z+110*RoomScale, 180, r, False, DOOR_CLASSIC)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+9509*RoomScale, r\y+4114*RoomScale, r\z+2210*RoomScale, 90, r, False, DOOR_CLASSIC, KEY_CARD_3)
	
	d = CreateDoor(r\zone,r\x+9931*RoomScale, r\y+4114*RoomScale, r\z+1010*RoomScale, 180, r, False, DOOR_CELL, KEY_CARD_3)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+9931*RoomScale, r\y+4114*RoomScale, r\z+302*RoomScale, 0, r, False, DOOR_CELL, KEY_CARD_3)
	If gopt\GameMode = GAMEMODE_CLASS_D And opt\IntroEnabled Then
		d\open = False
	Else
		d\locked = True
	EndIf
	
	d = CreateDoor(r\zone,r\x+11121*RoomScale, r\y+4114*RoomScale, r\z+1010*RoomScale, 180, r, False, DOOR_CELL, KEY_CARD_3)
	If gopt\GameMode = GAMEMODE_CLASS_D And opt\IntroEnabled Then
		d\open = False
	Else
		d\locked = True
	EndIf
	
	d = CreateDoor(r\zone,r\x+11121*RoomScale, r\y+4114*RoomScale, r\z+302*RoomScale, 0, r, True, DOOR_CELL, KEY_CARD_3)
	If gopt\GameMode = GAMEMODE_CLASS_D And opt\IntroEnabled Then
		d\open = False
	Else
		d\locked = True
	EndIf
	
	d = CreateDoor(r\zone,r\x+13179*RoomScale, r\y+4114*RoomScale, r\z+1010*RoomScale, 180, r, True, DOOR_CELL, KEY_CARD_3)
	If gopt\GameMode = GAMEMODE_CLASS_D And opt\IntroEnabled Then
		d\open = False
	Else
		d\locked = True
	EndIf
	
	d = CreateDoor(r\zone,r\x+13179*RoomScale, r\y+4114*RoomScale, r\z+302*RoomScale, 0, r, False, DOOR_CELL, KEY_CARD_3)
	If gopt\GameMode = GAMEMODE_CLASS_D And opt\IntroEnabled Then
		d\open = False
	Else
		d\locked = True
	EndIf
	
	d = CreateDoor(r\zone,r\x+14369*RoomScale, r\y+4114*RoomScale, r\z+1010*RoomScale, 180, r, False, DOOR_CELL, KEY_CARD_3)
	If gopt\GameMode = GAMEMODE_CLASS_D And opt\IntroEnabled Then
		d\open = False
	Else
		d\locked = True
	EndIf
	
	d = CreateDoor(r\zone,r\x+15559*RoomScale, r\y+4114*RoomScale, r\z+1010*RoomScale, 180, r, False, DOOR_CELL, KEY_CARD_3)
	If gopt\GameMode = GAMEMODE_CLASS_D And opt\IntroEnabled Then
		d\open = False
	Else
		d\locked = True
	EndIf
	
	d = CreateDoor(r\zone,r\x+15559*RoomScale, r\y+4114*RoomScale, r\z+302*RoomScale, 0, r, False, DOOR_CELL, KEY_CARD_3)
	If gopt\GameMode = GAMEMODE_CLASS_D And opt\IntroEnabled Then
		d\open = False
	Else
		d\locked = True
	EndIf
	
	d = CreateDoor(r\zone,r\x+16236*RoomScale, r\y+4114*RoomScale, r\z+656*RoomScale, 90, r, False, DOOR_CLASSIC, KEY_CARD_3)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+12150*RoomScale, r\y+4114*RoomScale, r\z+1135*RoomScale, 0, r, False, DOOR_CLASSIC, KEY_CARD_3)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+12150*RoomScale, r\y+4114*RoomScale, r\z+177*RoomScale, 0, r, False, DOOR_CLASSIC, KEY_CARD_3)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+1024*RoomScale, r\y+4114*RoomScale, r\z+656*RoomScale, 90, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x+2579*RoomScale, r\y+4114*RoomScale, r\z+415*RoomScale, 0, r, False, DOOR_OFFICE)
	
	d = CreateDoor(r\zone,r\x, r\y+4114*RoomScale, r\z-368*RoomScale, 0, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x, r\y+4114*RoomScale, r\z-2416*RoomScale, 0, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x-240*RoomScale, r\y+4114*RoomScale, r\z-3440*RoomScale, 90, r, False, DOOR_OFFICE)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x, r\y+4114*RoomScale, r\z-4464*RoomScale, 0, r, False, DOOR_CLASSIC)
	
	d = CreateDoor(r\zone,r\x, r\y+4230*RoomScale, r\z-5268*RoomScale, 0, r, False, DOOR_OFFICE_2)
	
	d = CreateDoor(r\zone,r\x, r\y+4230*RoomScale, r\z-6544*RoomScale, 0, r, False, DOOR_WINDOWED)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+2366*RoomScale, r\y+4230*RoomScale, r\z-7760*RoomScale, 0, r, False, DOOR_CLASSIC)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+3200*RoomScale, r\y+4230*RoomScale, r\z-7760*RoomScale, 0, r, False, DOOR_CLASSIC, KEY_CARD_1)
	
	d = CreateDoor(r\zone,r\x+1684*RoomScale, r\y+4230*RoomScale, r\z-7216*RoomScale, 90, r, False, DOOR_CLASSIC, KEY_CARD_1)
	
	; ~ [End]
	
End Function

Function UpdateEvent_ClassD_Cells_Zone(e.Events)
	Local ne.NewElevator, i
	Local PlayerIn#
	
	If PlayerRoom = e\room Then
		
		; ~ [Scientist spawn]
		
		If e\EventState[10] = 0 Then
			Local n.NPCs = CreateNPC(NPC_Human,3114*RoomScale,4250*RoomScale,5748*RoomScale)
			RotateEntity n\Collider,0,e\room\angle,0
			n\State[0] = STATE_SCRIPT
			SetNPCFrame(n,555)
			n\IsDead = True
			ChangeNPCTexture(n,"GFX\npcs\body2.jpg")
			
			Local bone% = FindChild(n\obj,"Bip01_L_Hand")
			If gopt\GameMode = GAMEMODE_DEFAULT Then
				Local it.Items = CreateItem(GetLocalString("Item Names","key_3"), "key3",EntityX(bone%,True),EntityY(bone%,True)+0.25,EntityZ(bone%,True))
			Else
				it = CreateItem("M9 Beretta", "beretta",EntityX(bone%,True),EntityY(bone%,True)+0.25,EntityZ(bone%,True))
				it\state = 15 : it\state2 = 30
			EndIf
			RotateEntity it\collider,EntityPitch(it\collider,True),e\room\angle+45,EntityRoll(it\collider,True),True
			EntityType it\collider, HIT_ITEM
			
			e\EventState[10] = 1
		EndIf
		
		; ~ [SCP-330 Event]
		
		If InteractWithObject(e\room\Objects[ClassD_Cells_SCP330],0.65) And I_330\Taken < 3 Then
			If e\EventState[11] = 0.0 Then
				CreateMsg(GetLocalString("Items","scp330_1"))
				e\EventState[11] = 1.0 
			Else
				If ItemAmount < MaxItemAmount Then
					For i = 0 To MaxItemAmount - 1
						If Inventory[i] = Null Then
							Select Rand(3)
								Case 1
									;[Block]
									Inventory[i] = CreateItem(GetLocalString("Item Names","candy"), "red_candy", 1.0, 1.0, 1.0)
									;[End Block]
								Case 2
									;[Block]
									Inventory[i] = CreateItem(GetLocalString("Item Names","candy"), "blue_candy", 1.0, 1.0, 1.0)
									;[End Block]
								Case 3
									;[Block]
									Inventory[i] = CreateItem(GetLocalString("Item Names","candy"), "yellow_candy", 1.0, 1.0, 1.0)
									;[End Block]
							End Select
							HideEntity(Inventory[i]\collider)
							Inventory[i]\Picked = True
							Inventory[i]\Dropped = -1
							Inventory[i]\itemtemplate\found = True
							If Inventory[i]\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[Inventory[i]\itemtemplate\sound])
							If I_330\Taken < 2 Then
								CreateMsg(GetLocalString("Items","scp330_2"))
							Else
								CreateMsg(GetLocalString("Items","scp330_3"))
							EndIf
							Exit
						EndIf
					Next
				Else
					CreateMsg(GetLocalString("Items","cannot_carry"))
				EndIf
				I_330\Taken = I_330\Taken + 1
			EndIf
			KeyHitUse = False
		EndIf
		
		If I_330\Taken = 3 Then
			I_330\Taken = 4
			psp\IsShowingHUD = False
			Crouch = True
			PlaySound_Strict(LoadTempSound("SFX\SCP\1162\BodyHorrorExchange" + Rand(1, 4) + ".ogg"))
			LightFlash = 2.0
			BlurTimer = 2000
			Local pvt = CreatePivot()
			PositionEntity(pvt, EntityX(Collider), EntityY(Collider) - 0.05, EntityZ(Collider))
			TurnEntity(pvt, 90.0, 0.0, 0.0)
			EntityPick(pvt, 0.3)
			Local de.Decals = CreateDecal(3, PickedX(), PickedY() + 0.005, PickedZ(), 90.0, Rnd(360.0), 0.0)
			de\Size = 0.75 : ScaleSprite(de\obj, de\Size, de\Size)
			FreeEntity(pvt)
			
			Kill()
			m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp330_death",Designation)
		EndIf
		
		; ~ [Elevator]
		
		Local e2.Events
		
		If e\EventState[14] = 0 And EntityY(Collider) > 6600.0*RoomScale Lor EntityY(Collider) <- 6600.0*RoomScale Then
			e\EventState[15] = e\EventState[15] + (0.01*FPSfactor)
			EntityAlpha e\room\Objects[1],Min(e\EventState[15],1.0)
			If e\EventState[15] > 1.05 Then
				SaveGame(SavePath + CurrSave\Name + "\", True)
				Local prevZone = gopt\CurrZone
				For ne = Each NewElevator
					If PlayerNewElevator = ne\ID And ne\room = e\room Then
						Select ne\tofloor
							Case 3
								gopt\CurrZone = LCZ
							Case 2
								gopt\CurrZone = CLASSD_CELLS
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
				Local playerElev = PlayerNewElevator
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
						Local translation# = 6500.0
						Select prevZone
							Case LCZ
								TranslateEntity ne\obj, 0, translation, 0
							Case CLASSD_CELLS
								If gopt\CurrZone = BCZ Then
									TranslateEntity ne\obj, 0, -translation, 0
								Else
									TranslateEntity ne\obj, 0, translation, 0
								EndIf
						End Select
						Select gopt\CurrZone
							Case LCZ
								ne\tofloor = 3
								ne\currfloor = 2
							Case CLASSD_CELLS
								ne\tofloor = 2
								If prevZone = LCZ Then
									ne\currfloor = 3
								Else
									ne\currfloor = 1
								EndIf
						End Select
						RotateEntity Collider,0,180,0
						TeleportEntity(Collider,EntityX(ne\obj,True),EntityY(ne\obj,True)+0.5,EntityZ(ne\obj,True),0.3,True)
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
						e2\EventState[15] = 1.05
						e2\EventState[14] = 1
						Exit
					EndIf
				Next
				SaveGame(SavePath + CurrSave\Name + "\", True)
				Return
			EndIf
		Else
			e\EventState[15] = Max(e\EventState[15] - (0.01*FPSfactor), 0.0)
			EntityAlpha e\room\Objects[1],Min(e\EventState[15],1.0)
			If e\room\RoomDoors[0]\open Then
				e\EventState[14] = 0
			EndIf
		EndIf
		
	EndIf
	
	If gopt\GameMode = GAMEMODE_CLASS_D And opt\IntroEnabled Then
		
		If PlayerRoom = e\room
			
		; ~ [Cell Door]
			
			If EntityHidden(e\room\Objects[ClassD_Cells_Part3_Cell_Door]) Then ShowEntity e\room\Objects[ClassD_Cells_Part3_Cell_Door]
			
		; ~ [Fog]
			
			CameraFogRange Camera, 5,30
			CameraFogColor (Camera,200,200,200)
			CameraClsColor (Camera,200,200,200)
			CameraRange(Camera, 0.005, 100)
			HideEntity Fog
			
		; ~ [Screen]
			
			If EntityHidden(e\room\Objects[ClassD_Cells_Screen]) Then ShowEntity e\room\Objects[ClassD_Cells_Screen]
			
		; ~ [Event]
			
			Curr106\Idle = True
			Curr173\Idle = True
			
			If e\EventState[8] <> 3 Then
				CanPlayerUseGuns = False
			Else
				CanPlayerUseGuns = True
			EndIf
			
			mpl\HasNTFGasmask = False
			
			If e\EventState[8] <> 3 Then
				If e\EventState[2] >= 70*8 Then
					ShouldPlay = MUS_INTRODUCTION_D
				Else
					ShouldPlay = MUS_NULL
				EndIf
			Else
				ShouldPlay = MUS_LCZ_CLASSIC
			EndIf
			
			ShowEntity e\room\Objects[ClassD_Cells_Room_3]
			
			Select e\EventState[8] ;! ~ [Days themselves]
				Case 0 ; ~ [The first day]
					;[Block]
					
					If e\EventState[2] = 0 Then
						EntityAlpha e\room\Objects[ClassD_Cells_Dark_Sprite],1
						CreateSplashText("SCP - SECURITY STORIES",opt\GraphicWidth/2,opt\GraphicHeight/2,50,20,Font_Menu_Medium,True,False,255,106,0)
						CreateSplashText(GetLocalString("Singleplayer","the_escape"),opt\GraphicWidth/2,opt\GraphicHeight/2,20,5,Font_Menu_Small,True,False,255,106,0)
						psp\IsShowingHUD = False
						psp\NoMove = True
						psp\NoRotation = True
						e\EventState[2] = 1
					EndIf
					
					If e\EventState[2] < 70*8 Then
						If e\Sound[0] = 0 Then
							e\Sound[0] = PlaySound_Strict(LoadTempSound("SFX\General\Introduction_D.ogg"))
						EndIf
					EndIf
					
					If e\EventState[2] <> 0 Then
						e\EventState[2] = e\EventState[2] + FPSfactor
					EndIf
					
					If e\EventState[2] >= 70*6 Then
						e\EventState[3] = e\EventState[3] - (0.01*FPSfactor)
						EntityAlpha e\room\Objects[ClassD_Cells_Dark_Sprite],Min(1,e\EventState[3]+3)
					EndIf
					
					If e\EventState[2] >= 70*10 And e\EventState[2] < 70*10.02 Then
;						If HUDenabled Then
							psp\IsShowingHUD = True
;						EndIf
						psp\NoMove = False
						psp\NoRotation = False
					ElseIf e\EventState[2] < 70*10.0 Then
						psp\IsShowingHUD = False
					EndIf
					
					If e\EventState[2] >= 70*11 Then
						If e\EventState[4] < 3.0
							e\EventState[4] = e\EventState[4] + (FPSfactor / 100.0)
						ElseIf e\EventState[4] < 15.0 Lor e\EventState[4] >= 50.0
							e\EventState[4] = e\EventState[4] + (FPSfactor / 30.0)
						EndIf
					EndIf
					
					If e\EventState[4] < 15.0
						
						If e\EventState[4] - (FPSfactor / 30.0) < 3.7 And e\EventState[4] > 3.7 Then PlaySound_Strict(LoadTempSound("SFX\Room\Intro\Class-D\Ew1.ogg"))
						If e\EventState[4] - (FPSfactor / 30.0) < 9.3 And e\EventState[4] > 9.3 Then PlaySound_Strict(LoadTempSound("SFX\Room\Intro\Class-D\Ew2.ogg"))
						
						If e\EventState[4] < 14.0
							
							If e\EventState[4] - (FPSfactor / 30.0) < 12.0 And e\EventState[4] > 12.0 Then PlaySound2(StepSFX(0, 0, 0), Camera, Collider, 1.0, 0.3)
							
							; ~ Cutscene
							RotateEntity(Camera, (-70.0) + 70.0 * Min(Max((e\EventState[4] - 3.0) / 5.0, 0.0), 1.0) + Sin(e\EventState[4] * 12.857) * 5.0, e\room\angle+90+ (60.0) * Max((e\EventState[4] - 10.0) / 4.0, 0.0), Sin(e\EventState[4] * 25.7) * 8.0)
							TeleportEntity(Camera, 14769.0 * RoomScale, 4260.0 * RoomScale, - 713.0 * RoomScale, 0.3, True)
							If (Not EntityHidden(Collider)) Then HideEntity(Collider)
							TeleportEntity(Collider, 14769.0 * RoomScale, 4260.0 * RoomScale, - 831.0 * RoomScale, 0.3, True)
							DropSpeed = 0.0
						Else
							
							StartChapter("d_chapter_0", 0)
							
							PositionEntity(Collider, EntityX(Collider), EntityY(Collider), EntityZ(Collider))
							ResetEntity(Collider)
							ShowEntity(Collider)
							DropSpeed = 0.0
							Playable = True
							
							e\EventState[4] = 15.0
						EndIf
						user_camera_pitch = 0.0
						RotateEntity(Collider, 0.0, EntityYaw(Camera), 0.0)
					ElseIf e\EventState[4] = 15.0 Then
						e\EventState[5] = e\EventState[5] + FPSfactor
					EndIf
					
					If e\EventState[5] >= 70*15 And e\EventState[5] < 70*15.05 Then
						If e\room\NPC[0] = Null Then
							e\room\NPC[0] = CreateNPC(NPC_Guard,EntityX(e\room\Objects[ClassD_Cells_Guard_Spawn],True),EntityY(e\room\Objects[ClassD_Cells_Guard_Spawn],True),EntityZ(e\room\Objects[ClassD_Cells_Guard_Spawn],True))
						EndIf
					EndIf
					
					If e\room\NPC[0] <> Null Then
						If e\EventState[5] < 70*17 Then
							e\room\NPC[0]\EnemyX = EntityX(e\room\Objects[ClassD_Cells_Guard_Waypoint], True)
							e\room\NPC[0]\EnemyY = EntityY(e\room\Objects[ClassD_Cells_Guard_Waypoint], True)
							e\room\NPC[0]\EnemyZ = EntityZ(e\room\Objects[ClassD_Cells_Guard_Waypoint], True)
							e\room\NPC[0]\State[0] = 5
						ElseIf e\EventState[5] >= 70*17 Then
							e\room\NPC[0]\EnemyX = Collider
							e\room\NPC[0]\EnemyY = Collider
							e\room\NPC[0]\EnemyZ = Collider
							e\room\NPC[0]\State[0] = STATE_SCRIPT
							
							PointEntity(e\room\NPC[0]\Collider,Collider,10)
						EndIf
					EndIf
					
					If e\EventState[5] >= 70*17 And e\EventState[5] < 70*17.01 Then
						PlayNewDialogue(0,%01) ; ~ Request opening the cell
					EndIf
					
					If e\EventState[5] >= 70*24 And e\EventState[5] < 70*24.02 Then
						OpenCloseDoor(e\room\RoomDoors[ClassD_Cells_Cell_Door])
					EndIf
					
					If e\EventState[5] >= 70*26 And e\EventState[5] < 70*26.02 Then
						PlayNewDialogue(1,%01) ; ~ We need you to do stuff
					EndIf
					
					If e\EventState[5] >= 70*39 And e\EventState[5] < 70*41.21 Then
						MoveEntity e\room\Objects[ClassD_Cells_Part3_Cell_Door], EntityX(e\room\Objects[ClassD_Cells_Part3_Cell_Door],True)-260*RoomScale, EntityY(e\room\Objects[ClassD_Cells_Part3_Cell_Door],True), EntityZ(e\room\Objects[ClassD_Cells_Part3_Cell_Door],True)
					EndIf
					If e\EventState[5] >= 70*39 And e\EventState[5] < 70*39.015 Then
						PlaySound_Strict(LoadTempSound("SFX\Door\Door2Open"+(Rand(1,3))+".ogg"))
					EndIf
					
					; ~ Player followed order
					If e\EventState[5] >= 70*40 And e\EventState[5] < 70*100 Then
						If EntityDistanceSquared(Collider,e\room\NPC[0]\Collider) < PowTwo(1.0) And e\EventState[6] = 0 Then
							e\EventState[6] = 1
						EndIf
					EndIf
					
					If e\EventState[6] = 1 Then
						psp\NoMove = True
						e\EventState[7] = e\EventState[7] + FPSfactor
					ElseIf e\EventState[6] = 2 Then
						psp\NoMove = False
						e\EventState[7] = 0
						
						PlayNewDialogue(15,%01) ; ~ Grab tools
						
						EntityAlpha e\room\Objects[ClassD_Cells_Dark_Sprite],0
						e\EventState[6] = 3
					EndIf
					
					Local invitem%
					
					For invitem = 0 To MaxItemAmount - 1
						If Inventory[invitem] <> Null Then
							If Inventory[invitem]\itemtemplate\tempname = "toolbox" Then
								If EntityDistanceSquared(Collider, e\room\Objects[ClassD_Cells_Table])<PowTwo(0.7) Then
									psp\NoMove = True
									If e\EventState[6] > 2 Then
										e\EventState[9] = 1
									EndIf
									RemoveItem(Inventory[invitem])
									Exit
								EndIf
							EndIf
						EndIf
					Next
					If e\EventState[9] > 0 Then
						e\EventState[9] = e\EventState[9] + FPSfactor
					ElseIf e\EventState[9] >= 70*1 And e\EventState[9] < 70*1.01 Then
						it = CreateItem(GetLocalString("Item Names","toolbox"), "toolbox", EntityX(e\room\Objects[ClassD_Cells_Table],True), EntityY(e\room\Objects[ClassD_Cells_Table],True), EntityZ(e\room\Objects[ClassD_Cells_Table],True))
						EntityParent(it\collider, e\room\obj)
						PlayNewDialogue(17,%01) ; ~ Go back to cell
					EndIf
					
					; ~ Transition to 2nd Day
					If e\EventState[9] >= 70*5 And e\EventState[9] < 70*5.02 Then
						e\EventState[2] = 1
						e\EventState[4] = 0
						e\EventState[5] = 0
						e\EventState[6] = 0
						e\EventState[7] = 0
						e\EventState[9] = 0
						
						For invitem = 0 To MaxItemAmount - 1
							If Inventory[invitem] <> Null Then
								If Inventory[invitem]\itemtemplate\tempname = "toolbox" Then
									RemoveItem(Inventory[invitem])
									Exit
								EndIf
							EndIf
						Next
						
						psp\NoMove = False
						EntityAlpha e\room\Objects[ClassD_Cells_Dark_Sprite],1
						If (Not EntityHidden(e\room\Objects[ClassD_Cells_Room_2])) Then HideEntity e\room\Objects[ClassD_Cells_Room_2]
						e\room\RoomDoors[ClassD_Cells_Cell_Door]\open = False
						TeleportEntity(e\room\Objects[ClassD_Cells_Part3_Cell_Door],0,0,0,0.3,True)
						
						e\EventState[8] = 1
					EndIf
					
					If e\EventState[7] > 0 Then
						EntityAlpha e\room\Objects[ClassD_Cells_Dark_Sprite],Min(e\EventState[7]*0.02,1.0)
					EndIf
					
					If e\EventState[7] > 70*1 And e\EventState[7] < 70*1.01 Then
						If ChannelPlaying(mtfd\CurrentChannel) And e\EventState[7] > 70*1 And e\EventState[7] < 70*1.01 Then
							StopChannel(mtfd\CurrentChannel)
						EndIf
						PlayNewDialogue(5,%01) ; ~ I'll put blindfold
						TeleportEntity(Collider, 1540.0 * RoomScale, 4260.0 * RoomScale, 4824.0 * RoomScale, True)
						If EntityHidden(e\room\Objects[ClassD_Cells_Room_2]) Then ShowEntity e\room\Objects[ClassD_Cells_Room_2]
						If (Not EntityHidden(e\room\Objects[ClassD_Cells_Room_3])) Then HideEntity e\room\Objects[ClassD_Cells_Room_3]
					EndIf
					If e\EventState[7] > 70*5 And e\EventState[7] < 70*5.05 Then
						e\EventState[6] = 2
						;PlayNewDialogue(6,%01) ; ~ We're here
					EndIf
					
					; ~ Player didn't obey at first day
					If e\EventState[5] >= 70*53 And e\EventState[5] < 70*53.015 And e\EventState[6] = 0 Then
						PlayNewDialogue(2,%01) ; ~ You have to step out
					EndIf
					
					If e\EventState[5] >= 70*77 And e\EventState[5] < 70*77.02 And e\EventState[6] = 0 Then
						PlayNewDialogue(3,%01) ; ~ You have to step out x2
					EndIf
					
					If e\EventState[5] >= 70*92 And e\EventState[5] < 70*92.015 And e\EventState[6] = 0 Then
						PlayNewDialogue(4,%01) ; ~ I'll shoot you
					EndIf
					
					If e\EventState[5] >= 70*100 And e\EventState[6] = 0 Then
						e\room\NPC[0]\EnemyX = Collider
						e\room\NPC[0]\EnemyY = Collider
						e\room\NPC[0]\EnemyZ = Collider
						e\room\NPC[0]\State[0] = 5
						If EntityDistanceSquared(e\room\NPC[0]\Collider, Collider) < PowTwo(2.0) Then
							e\room\NPC[0]\State[0] = 15
							psp\NoMove = True
						EndIf
					EndIf
					
					;[End Block]
				Case 1 ; ~ [The second day]
					;[Block]
					
					If e\EventState[2] > 0 Then
						e\EventState[2] = e\EventState[2] + FPSfactor
					EndIf
					
					If e\EventState[2] >= 70*2 Then
						e\EventState[3] = e\EventState[3] - (0.01*FPSfactor)
						EntityAlpha e\room\Objects[ClassD_Cells_Dark_Sprite],Min(1,e\EventState[3]+3)
					EndIf
					
					If e\EventState[2] >= 70*4 And e\EventState[2] < 70*4.02 Then
						;If HUDenabled Then
							psp\IsShowingHUD = True
						;EndIf
						psp\NoMove = False
						psp\NoRotation = False
					ElseIf e\EventState[2] < 70*4.0 Then
						psp\IsShowingHUD = False
					EndIf
					
					If e\EventState[2] >= 70*5 Then
						If e\EventState[4] < 3.0
							e\EventState[4] = e\EventState[4] + (FPSfactor / 100.0)
						ElseIf e\EventState[4] < 15.0 Lor e\EventState[4] >= 50.0
							e\EventState[4] = e\EventState[4] + (FPSfactor / 30.0)
						EndIf
					EndIf
					
					If e\EventState[4] < 15.0
						
						If e\EventState[4] - (FPSfactor / 30.0) < 3.7 And e\EventState[4] > 3.7 Then PlaySound_Strict(LoadTempSound("SFX\Room\Intro\Class-D\Ew2.ogg"))
						
						If e\EventState[4] < 14.0
							
							If e\EventState[4] - (FPSfactor / 30.0) < 12.0 And e\EventState[4] > 12.0 Then PlaySound2(StepSFX(0, 0, 0), Camera, Collider, 1.0, 0.3)
							
							; ~ Cutscene
							RotateEntity(Camera, (-70.0) + 70.0 * Min(Max((e\EventState[4] - 3.0) / 5.0, 0.0), 1.0) + Sin(e\EventState[4] * 12.857) * 5.0, e\room\angle+90+ (60.0) * Max((e\EventState[4] - 10.0) / 4.0, 0.0), Sin(e\EventState[4] * 25.7) * 8.0)
							TeleportEntity(Camera, 14769.0 * RoomScale, 4260.0 * RoomScale, - 713.0 * RoomScale, 0.3, True)
							If (Not EntityHidden(Collider)) Then HideEntity(Collider)
							TeleportEntity(Collider, 14769.0 * RoomScale, 4260.0 * RoomScale, - 831.0 * RoomScale, 0.3, True)
							DropSpeed = 0.0
						Else
							
							PositionEntity(Collider, EntityX(Collider), EntityY(Collider), EntityZ(Collider))
							ResetEntity(Collider)
							ShowEntity(Collider)
							DropSpeed = 0.0
							Playable = True
							
							e\EventState[4] = 15.0
						EndIf
						user_camera_pitch = 0.0
						RotateEntity(Collider, 0.0, EntityYaw(Camera), 0.0)
					ElseIf e\EventState[4] = 15.0 Then
						e\EventState[5] = e\EventState[5] + FPSfactor
					EndIf
					
					If e\EventState[5] >= 70*15 And e\EventState[5] < 70*15.05 Then
						If e\room\NPC[0] = Null Then
							e\room\NPC[0] = CreateNPC(NPC_Guard,EntityX(e\room\Objects[ClassD_Cells_Guard_Spawn],True),EntityY(e\room\Objects[ClassD_Cells_Guard_Spawn],True),EntityZ(e\room\Objects[ClassD_Cells_Guard_Spawn],True))
						EndIf
					EndIf
					
					If e\room\NPC[0] <> Null Then
						If e\EventState[5] < 70*17 Then
							e\room\NPC[0]\EnemyX = EntityX(e\room\Objects[ClassD_Cells_Guard_Waypoint], True)
							e\room\NPC[0]\EnemyY = EntityY(e\room\Objects[ClassD_Cells_Guard_Waypoint], True)
							e\room\NPC[0]\EnemyZ = EntityZ(e\room\Objects[ClassD_Cells_Guard_Waypoint], True)
							e\room\NPC[0]\State[0] = 5
						ElseIf e\EventState[5] >= 70*17 Then
							e\room\NPC[0]\EnemyX = Collider
							e\room\NPC[0]\EnemyY = Collider
							e\room\NPC[0]\EnemyZ = Collider
							e\room\NPC[0]\State[0] = STATE_SCRIPT
							
							PointEntity(e\room\NPC[0]\Collider,Collider,10)
						EndIf
					EndIf
					
					If e\EventState[5] >= 70*24 And e\EventState[5] < 70*24.02 Then
						OpenCloseDoor(e\room\RoomDoors[ClassD_Cells_Cell_Door])
					EndIf
					
					If e\EventState[5] >= 70*26 And e\EventState[5] < 70*26.02 Then
						PlayNewDialogue(7,%01) ; ~ We need you to do stuff again
					EndIf
					
					If e\EventState[5] >= 70*39 And e\EventState[5] < 70*41.21 Then
						MoveEntity e\room\Objects[ClassD_Cells_Part3_Cell_Door], EntityX(e\room\Objects[ClassD_Cells_Part3_Cell_Door],True)-260*RoomScale, EntityY(e\room\Objects[ClassD_Cells_Part3_Cell_Door],True), EntityZ(e\room\Objects[ClassD_Cells_Part3_Cell_Door],True)
					EndIf
					If e\EventState[5] >= 70*39 And e\EventState[5] < 70*39.015 Then
						PlaySound_Strict(LoadTempSound("SFX\Door\Door2Open"+(Rand(1,3))+".ogg"))
					EndIf
					
					; ~ Player followed order
					If e\EventState[5] >= 70*40 And e\EventState[5] < 70*82 Then
						If EntityDistanceSquared(Collider,e\room\NPC[0]\Collider) < PowTwo(1.0) And e\EventState[6] = 0 Then
							e\EventState[6] = 1
						EndIf
					EndIf
					
					If e\EventState[6] = 1 Then
						psp\NoMove = True
						e\EventState[7] = e\EventState[7] + FPSfactor
					ElseIf e\EventState[6] = 2 Then
						psp\NoMove = False
						e\EventState[7] = 0
						
						CreateHintMsg(GetLocalString("Singleplayer", "search_for_crates"))
						
						EntityAlpha e\room\Objects[ClassD_Cells_Dark_Sprite],0
						e\EventState[6] = 3
					EndIf
					
					If e\EventState[6] > 2 Then
						e\EventState[9] = e\EventState[9] + FPSfactor
					EndIf
					
					; ~ Transition to 3rd Day
					If e\EventState[9] >= 70*15 And e\EventState[9] < 70*15.02 Then
						e\EventState[2] = 1
						e\EventState[4] = 0
						e\EventState[5] = 0
						e\EventState[6] = 0
						e\EventState[7] = 0
						e\EventState[9] = 0
						
						EntityAlpha e\room\Objects[ClassD_Cells_Dark_Sprite],1
						If (Not EntityHidden(e\room\Objects[ClassD_Cells_Room_2])) Then HideEntity e\room\Objects[ClassD_Cells_Room_2]
						e\room\RoomDoors[ClassD_Cells_Cell_Door]\open = False
						TeleportEntity(e\room\Objects[ClassD_Cells_Part3_Cell_Door],0,0,0,0.3,True)
						
						e\EventState[8] = 2
					EndIf
					
					If e\EventState[7] > 0 Then
						EntityAlpha e\room\Objects[ClassD_Cells_Dark_Sprite],Min(e\EventState[7]*0.02,1.0)
					EndIf
					
					If e\EventState[7] > 70*1 And e\EventState[7] < 70*1.01 Then
						If ChannelPlaying(mtfd\CurrentChannel) And e\EventState[7] > 70*1 And e\EventState[7] < 70*1.01 Then
							StopChannel(mtfd\CurrentChannel)
						EndIf
						TeleportEntity(Collider, 1540.0 * RoomScale, 4260.0 * RoomScale, 4824.0 * RoomScale, True)
						If EntityHidden(e\room\Objects[ClassD_Cells_Room_2]) Then ShowEntity e\room\Objects[ClassD_Cells_Room_2]
						If (Not EntityHidden(e\room\Objects[ClassD_Cells_Room_3])) Then HideEntity e\room\Objects[ClassD_Cells_Room_3]
					EndIf
					If e\EventState[7] > 70*5 And e\EventState[7] < 70*5.05 Then
						e\EventState[6] = 2
					EndIf
					
					; ~ Player didn't obey at first day
					If e\EventState[5] >= 70*53 And e\EventState[5] < 70*53.015 And e\EventState[6] = 0 Then
						PlayNewDialogue(8,%01) ; ~ You have to step out
					EndIf
					
					If e\EventState[5] >= 70*77 And e\EventState[5] < 70*77.02 And e\EventState[6] = 0 Then
						PlayNewDialogue(9,%01) ; ~ You have to step out x2
					EndIf
					
					If e\EventState[5] >= 70*82 And e\EventState[6] = 0 Then
						e\room\NPC[0]\EnemyX = Collider
						e\room\NPC[0]\EnemyY = Collider
						e\room\NPC[0]\EnemyZ = Collider
						e\room\NPC[0]\State[0] = 5
						If EntityDistanceSquared(e\room\NPC[0]\Collider, Collider) < PowTwo(2.0) Then
							e\room\NPC[0]\State[0] = 15
							psp\NoMove = True
						EndIf
					EndIf
					
					;[End Block]
				Case 2 ; ~ [The third day]
					;[Block]
					
					;[End Block]
				Case 3 ; ~ [The final, breach day]
					;[Block]
					
					SecondaryLightOn = False
					
					If e\room\NPC[0] <> Null Then
						RemoveNPC(e\room\NPC[0])
					EndIf
						
					If e\EventState[2] > 0 Then
						e\EventState[2] = e\EventState[2] + FPSfactor
					EndIf
					
					If e\EventState[2] >= 70*2 Then
						e\EventState[3] = e\EventState[3] - (0.01*FPSfactor)
						EntityAlpha e\room\Objects[ClassD_Cells_Dark_Sprite],Min(1,e\EventState[3]+3)
					EndIf
					
					If e\EventState[2] >= 70*4 And e\EventState[2] < 70*4.02 Then
						;If HUDenabled Then
							psp\IsShowingHUD = True
						;EndIf
						psp\NoMove = False
						psp\NoRotation = False
					ElseIf e\EventState[2] < 70*4.0 Then
						psp\IsShowingHUD = False
					EndIf
					
					If e\EventState[2] >= 70*5 Then
						If e\EventState[4] < 3.0
							e\EventState[4] = e\EventState[4] + (FPSfactor / 100.0)
						ElseIf e\EventState[4] < 15.0 Lor e\EventState[4] >= 50.0
							e\EventState[4] = e\EventState[4] + (FPSfactor / 30.0)
						EndIf
					EndIf
					
					If e\EventState[4] < 15.0
						
						If e\EventState[4] < 14.0
							
							If e\EventState[4] - (FPSfactor / 30.0) < 12.0 And e\EventState[4] > 12.0 Then PlaySound2(StepSFX(0, 0, 0), Camera, Collider, 1.0, 0.3)
							
							; ~ Cutscene
							RotateEntity(Camera, (-70.0) + 70.0 * Min(Max((e\EventState[4] - 3.0) / 5.0, 0.0), 1.0) + Sin(e\EventState[4] * 12.857) * 5.0, e\room\angle+90+ (60.0) * Max((e\EventState[4] - 10.0) / 4.0, 0.0), Sin(e\EventState[4] * 25.7) * 8.0)
							TeleportEntity(Camera, 14769.0 * RoomScale, 4260.0 * RoomScale, - 713.0 * RoomScale, 0.3, True)
							If (Not EntityHidden(Collider)) Then HideEntity(Collider)
							TeleportEntity(Collider, 14769.0 * RoomScale, 4260.0 * RoomScale, - 831.0 * RoomScale, 0.3, True)
							DropSpeed = 0.0
						Else
							
							PositionEntity(Collider, EntityX(Collider), EntityY(Collider), EntityZ(Collider))
							ResetEntity(Collider)
							ShowEntity(Collider)
							DropSpeed = 0.0
							Playable = True
							
							e\EventState[4] = 15.0
						EndIf
						user_camera_pitch = 0.0
						RotateEntity(Collider, 0.0, EntityYaw(Camera), 0.0)
					ElseIf e\EventState[4] = 15.0 Then
						e\EventState[5] = e\EventState[5] + FPSfactor
					EndIf
					
					If e\EventState[5] >= 70*1 And e\EventState[5] < 70*1.02 Then
						OpenCloseDoor(e\room\RoomDoors[ClassD_Cells_Cell_Door])
					EndIf
					If e\EventState[5] >= 70*2 And e\EventState[5] < 70*4.21 Then
						MoveEntity e\room\Objects[ClassD_Cells_Part3_Cell_Door], EntityX(e\room\Objects[ClassD_Cells_Part3_Cell_Door],True)-260*RoomScale, EntityY(e\room\Objects[ClassD_Cells_Part3_Cell_Door],True), EntityZ(e\room\Objects[ClassD_Cells_Part3_Cell_Door],True)
					EndIf
					If e\EventState[5] >= 70*2 And e\EventState[5] < 70*2.015 Then
						PlaySound_Strict(LoadTempSound("SFX\Door\Door2Open"+(Rand(1,3))+".ogg"))
					EndIf
					
					If e\EventState[5] >= 70*5 And e\EventState[5] < 70*5.05 Then
						If e\room\NPC[1] = Null Then
							e\room\NPC[1] = CreateNPC(NPC_scp_106,EntityX(e\room\Objects[ClassD_Cells_Guard_Waypoint],True),EntityY(e\room\Objects[ClassD_Cells_Guard_Waypoint],True),EntityZ(e\room\Objects[ClassD_Cells_Guard_Waypoint],True))
						EndIf
					EndIf
					
					If e\room\NPC[1] <> Null Then
						e\room\NPC[1]\EnemyX = Collider
						e\room\NPC[1]\EnemyY = Collider
						e\room\NPC[1]\EnemyZ = Collider
					EndIf
					
					;[End Block]
			End Select
			
		Else
			HideEntity e\room\Objects[ClassD_Cells_Screen]
		EndIf
		
	Else
	
		If PlayerRoom = e\room Then
				
		; ~ [Fog]
			
			CameraRange Camera, 0.01, 100
			CameraFogRange Camera, 0.5, 100
			
		; ~ [Screen]
			
			If EntityHidden(e\room\Objects[ClassD_Cells_Screen]) Then ShowEntity e\room\Objects[ClassD_Cells_Screen]
			
			If EntityDistanceSquared(Collider, e\room\Objects[ClassD_Cells_Screen]) > PowTwo(10.0) Then
				e\EventState[0] = e\EventState[0] + FPSfactor
				
				If e\EventState[0] > 0 And e\EventState[0] < 70*5 Then
					EntityTexture e\room\Objects[ClassD_Cells_Screen], Class_D_Screen[2]
				ElseIf e\EventState[0] > 70*5 Then
					EntityTexture e\room\Objects[ClassD_Cells_Screen], Class_D_Screen[1], Floor(((e\EventState[0]-70*5)/70) Mod 4.0)
				EndIf
			EndIf
			
		; ~ [Optimization]
			
			; ~ Doors
			
			If e\room\RoomDoors[ClassD_Cells_Door_Part_2]\open Then
				ShowEntity(e\room\Objects[ClassD_Cells_Room_2])
			Else
				HideEntity(e\room\Objects[ClassD_Cells_Room_2])
			EndIf
			
			If e\room\RoomDoors[ClassD_Cells_Door_Part_3]\open Then
				ShowEntity(e\room\Objects[ClassD_Cells_Room_3])
			Else
				HideEntity(e\room\Objects[ClassD_Cells_Room_3])
			EndIf
			
			If e\room\RoomDoors[ClassD_Cells_Door_Part_4]\open Lor e\room\RoomDoors[ClassD_Cells_Door_Part_4_2]\open Then
				ShowEntity(e\room\Objects[ClassD_Cells_Room_4])
			Else
				HideEntity(e\room\Objects[ClassD_Cells_Room_4])
			EndIf
			
			If e\room\RoomDoors[ClassD_Cells_Door_Part_3]\open And EntityHidden(e\room\Objects[ClassD_Cells_Room_2]) Then
				ShowEntity(e\room\Objects[ClassD_Cells_Room_2])
			EndIf
			
			If e\room\RoomDoors[ClassD_Cells_Door_Part_4_2]\open And EntityHidden(e\room\Objects[ClassD_Cells_Room_3]) Then
				ShowEntity(e\room\Objects[ClassD_Cells_Room_3])
			EndIf
			
			; ~ Distance
			
			; ~ (Numbers determine area where player is currently in)
			
			If EntityDistanceSquared(Collider, e\room\Objects[ClassD_Cells_Part2_Pivot_1]) < PowTwo(0.7) Then
				PlayerIn# = 1
			ElseIf EntityDistanceSquared(Collider, e\room\Objects[ClassD_Cells_Part2_Pivot_2]) < PowTwo(0.7) Then
				PlayerIn# = 2
			ElseIf EntityDistanceSquared(Collider, e\room\Objects[ClassD_Cells_Part3_Pivot_1]) < PowTwo(0.7) Then
				PlayerIn# = 2
			ElseIf EntityDistanceSquared(Collider, e\room\Objects[ClassD_Cells_Part3_Pivot_2]) < PowTwo(0.7) Then
				PlayerIn# = 3
			ElseIf EntityDistanceSquared(Collider, e\room\Objects[ClassD_Cells_Part4_Pivot_1]) < PowTwo(0.7) Then
				PlayerIn# = 3
			ElseIf EntityDistanceSquared(Collider, e\room\Objects[ClassD_Cells_Part4_Pivot_2]) < PowTwo(0.7) Then
				PlayerIn# = 4
			ElseIf EntityDistanceSquared(Collider, e\room\Objects[ClassD_Cells_Part4_Pivot_3]) < PowTwo(0.7) Then
				PlayerIn# = 1
			ElseIf EntityDistanceSquared(Collider, e\room\Objects[ClassD_Cells_Part4_Pivot_4]) < PowTwo(0.7) Then
				PlayerIn# = 4
			EndIf
			
			If PlayerIn# = 1 Then
				If EntityDistanceSquared(Collider, e\room\RoomDoors[ClassD_Cells_Door_Part_2]\frameobj) > PowTwo(10.0) Then
					e\room\RoomDoors[ClassD_Cells_Door_Part_2]\open = False
				EndIf
				If EntityDistanceSquared(Collider, e\room\RoomDoors[ClassD_Cells_Door_Part_3]\frameobj) > PowTwo(10.0) Then
					e\room\RoomDoors[ClassD_Cells_Door_Part_3]\open = False
				EndIf
				If EntityDistanceSquared(Collider, e\room\RoomDoors[ClassD_Cells_Door_Part_4]\frameobj) > PowTwo(10.0) Then
					e\room\RoomDoors[ClassD_Cells_Door_Part_4]\open = False
				EndIf
				If EntityDistanceSquared(Collider, e\room\RoomDoors[ClassD_Cells_Door_Part_4_2]\frameobj) > PowTwo(10.0) Then
					e\room\RoomDoors[ClassD_Cells_Door_Part_4_2]\open = False
				EndIf
			ElseIf PlayerIn# = 2 Then
				e\room\RoomDoors[ClassD_Cells_Door_Part_2]\open = True
				If EntityDistanceSquared(Collider, e\room\RoomDoors[ClassD_Cells_Door_Part_3]\frameobj) > PowTwo(10.0) Then
					e\room\RoomDoors[ClassD_Cells_Door_Part_3]\open = False
				EndIf
				If EntityDistanceSquared(Collider, e\room\RoomDoors[ClassD_Cells_Door_Part_4]\frameobj) > PowTwo(10.0) Then
					e\room\RoomDoors[ClassD_Cells_Door_Part_4]\open = False
				EndIf
				If EntityDistanceSquared(Collider, e\room\RoomDoors[ClassD_Cells_Door_Part_4_2]\frameobj) > PowTwo(10.0) Then
					e\room\RoomDoors[ClassD_Cells_Door_Part_4_2]\open = False
				EndIf
			ElseIf PlayerIn# = 3 Then
				If EntityDistanceSquared(Collider, e\room\RoomDoors[ClassD_Cells_Door_Part_2]\frameobj) > PowTwo(10.0) Then
					e\room\RoomDoors[ClassD_Cells_Door_Part_2]\open = False
				EndIf
				e\room\RoomDoors[ClassD_Cells_Door_Part_3]\open = True
				If EntityDistanceSquared(Collider, e\room\RoomDoors[ClassD_Cells_Door_Part_4]\frameobj) > PowTwo(10.0) Then
					e\room\RoomDoors[ClassD_Cells_Door_Part_4]\open = False
				EndIf
				If EntityDistanceSquared(Collider, e\room\RoomDoors[ClassD_Cells_Door_Part_4_2]\frameobj) > PowTwo(10.0) Then
					e\room\RoomDoors[ClassD_Cells_Door_Part_4_2]\open = False
				EndIf
			ElseIf PlayerIn# = 4 Then
				If EntityDistanceSquared(Collider, e\room\RoomDoors[ClassD_Cells_Door_Part_2]\frameobj) > PowTwo(10.0) Then
					e\room\RoomDoors[ClassD_Cells_Door_Part_2]\open = False
				EndIf
				If EntityDistanceSquared(Collider, e\room\RoomDoors[ClassD_Cells_Door_Part_3]\frameobj) > PowTwo(10.0) Then
					e\room\RoomDoors[ClassD_Cells_Door_Part_3]\open = False
				EndIf
				e\room\RoomDoors[ClassD_Cells_Door_Part_4]\open = True
				e\room\RoomDoors[ClassD_Cells_Door_Part_4_2]\open = True
			EndIf
			
		Else
			HideEntity e\room\Objects[ClassD_Cells_Screen]
		EndIf
		
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D TSS