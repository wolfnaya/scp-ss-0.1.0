
Const DOOR_DEFAULT = 0
Const DOOR_CONTAINMENT = 1
Const DOOR_HCZ = 2
Const DOOR_ELEVATOR = 3
Const DOOR_914 = 4
Const DOOR_ELEVATOR_3FLOOR = 5
Const DOOR_WINDOWED = 6
Const DOOR_WINDOWED_2 = 7
Const DOOR_OFFICE = 8
Const DOOR_OFFICE_2 = 9
Const DOOR_LCZ = 10
Const DOOR_EZ = 11
Const DOOR_ONE_SIDED = 12
Const DOOR_CLASSIC = 13
Const DOOR_CELL = 14
Const DOOR_RCZ = 15
Const DOOR_087B = 16
Const DOOR_STORAGE_ELEVATOR = 17

Const MaxBigDoorOBJ = 2
Const FirstDoor = 0
Const SecondDoor = 1

Const MaxButtonTypes = 5
Const MaxElevatorPanelTextureIDAmount% = 3
Const BUTTON_NORMAL = 0
Const BUTTON_KEYCARD = 1
Const BUTTON_KEYPAD = 2
Const BUTTON_SCANNER = 3
Const BUTTON_ELEVATOR_3FLOOR = 4

Const KEY_CARD_CAVE_1% = 98
Const KEY_CARD_CAVE_2% = 99
Const KEY_CARD_0% = 0
Const KEY_CARD_1% = 1
Const KEY_CARD_2% = 2
Const KEY_CARD_3% = 3
Const KEY_CARD_4% = 4
Const KEY_CARD_5% = 5
Const KEY_CARD_OMNI% = 6
Const SCP_005% = 7

Const SEVERED_HAND% = -2
Const SEVERED_HAND_2% = -3
Const SEVERED_HAND_3% = -4

Const ELEVATOR_PANEL_UP% = 0
Const ELEVATOR_PANEL_DOWN% = 1
Const ELEVATOR_PANEL_IDLE% = 2


Type DoorInstance
	Field ClosestButton%
	Field ClosestDoor.Doors
	Field SelectedDoor.Doors
	Field UpdateDoorsTimer#
	Field DoorTempID%
	Field DoorOBJ%
	Field DoorframeOBJ%
	Field DoorColl%
	Field BigDoorOBJ%[MaxBigDoorOBJ]
	Field ButtonOBJ%[MaxButtonTypes]
	Field ButtonTexture%
	Field ButtonTextureLocked%
End Type

Function LoadDoors()
	d_I.DoorInstance = New DoorInstance
	
	d_I\DoorOBJ = LoadMesh_Strict("GFX\map\props\door01.x")
	HideEntity d_I\DoorOBJ
	d_I\DoorframeOBJ = LoadMesh_Strict("GFX\map\props\doorframe.x")
	HideEntity d_I\DoorframeOBJ
	
	d_I\DoorColl = LoadMesh_Strict("GFX\map\props\doorcoll.x")
	HideEntity d_I\DoorColl
	
	d_I\ButtonOBJ[BUTTON_NORMAL] = LoadMesh_Strict("GFX\map\buttons\Button.b3d")
	HideEntity d_I\ButtonOBJ[BUTTON_NORMAL]
	d_I\ButtonOBJ[BUTTON_KEYCARD] = LoadMesh_Strict("GFX\map\buttons\ButtonKeycard.b3d")
	HideEntity d_I\ButtonOBJ[BUTTON_KEYCARD]
	d_I\ButtonOBJ[BUTTON_KEYPAD] = LoadMesh_Strict("GFX\map\buttons\ButtonCode.b3d")
	HideEntity d_I\ButtonOBJ[BUTTON_KEYPAD]	
	d_I\ButtonOBJ[BUTTON_SCANNER] = LoadMesh_Strict("GFX\map\buttons\ButtonScanner.b3d")
	HideEntity d_I\ButtonOBJ[BUTTON_SCANNER]
	d_I\ButtonOBJ[BUTTON_ELEVATOR_3FLOOR] = LoadMesh_Strict("GFX\map\buttons\elevatorbutton.b3d")
	HideEntity d_I\ButtonOBJ[BUTTON_ELEVATOR_3FLOOR]
	
	d_I\BigDoorOBJ[FirstDoor] = LoadMesh_Strict("GFX\map\props\ContDoorLeft.x")
	HideEntity d_I\BigDoorOBJ[FirstDoor]
	d_I\BigDoorOBJ[SecondDoor] = LoadMesh_Strict("GFX\map\props\ContDoorRight.x")
	HideEntity d_I\BigDoorOBJ[SecondDoor]
	
	d_I\ButtonTexture = LoadTextureCheckingIfInCache("GFX\map\textures\KeyPad.jpg", True)
	d_I\ButtonTextureLocked = LoadTextureCheckingIfInCache("GFX\map\textures\KeyPadLocked.jpg", True)
	
End Function

Type Doors
	Field obj%, obj2%, frameobj%, buttons%[2]
	Field locked%, lockedupdated%, open%, angle%, openstate#, fastopen%
	Field dir%
	Field timer%, timerstate#
	Field KeyCard%
	Field room.Rooms
	Field DisableWaypoint%
	Field dist#
	Field SoundCHN%
	Field Code$
	Field ID%
	Field Level%
	Field LevelDest%
	Field AutoClose%
	Field LinkedDoor.Doors
	Field IsElevatorDoor% = False
	Field MTFClose% = True
	Field NPCCalledElevator% = False
	Field DoorHitOBJ%
End Type

Function CreateDoor.Doors(lvl, x#, y#, z#, angle#, room.Rooms, dopen% = False,  big% = False, keycard% = -1, code$="", elevator_type%=1)
	Local d.Doors, parent, i%
	If room <> Null Then parent = room\obj
	Local d2.Doors
	
	d.Doors = New Doors
	If big=DOOR_CONTAINMENT Then
		d\obj = CopyEntity(d_I\BigDoorOBJ[FirstDoor])
		ScaleEntity(d\obj, 55 * RoomScale, 55 * RoomScale, 55 * RoomScale)
		d\obj2 = CopyEntity(d_I\BigDoorOBJ[SecondDoor])
		ScaleEntity(d\obj2, 55 * RoomScale, 55 * RoomScale, 55 * RoomScale)
		
		d\frameobj = CopyEntity(d_I\DoorColl)				
		ScaleEntity(d\frameobj, RoomScale, RoomScale, RoomScale)
		EntityType d\frameobj, HIT_MAP
		EntityAlpha d\frameobj, 0.0
	ElseIf big=DOOR_HCZ Then
		For d2 = Each Doors
			If d2 <> d And d2\dir = DOOR_HCZ Then
				d\obj = CopyEntity(d2\obj)
				ScaleEntity d\obj, RoomScale, RoomScale, RoomScale
				Exit
			EndIf
		Next
		If d\obj = 0 Then
			d\obj = LoadAnimMesh_Strict("GFX\map\props\DoorHCZ.b3d")
			ScaleEntity d\obj, RoomScale, RoomScale, RoomScale
		EndIf
		d\frameobj = LoadMesh_Strict("GFX\map\props\DoorFrameHCZ.b3d")
	ElseIf big=DOOR_RCZ Then
		If d\obj=0 Then
			d\obj = LoadAnimMesh_Strict("GFX\map\props\DoorRCZ1.b3d")
			d\obj2 = LoadAnimMesh_Strict("GFX\map\props\DoorRCZ2.b3d")
			ScaleEntity d\obj, RoomScale, RoomScale, RoomScale
			ScaleEntity d\obj2, RoomScale, RoomScale, RoomScale
		EndIf
		d\frameobj = LoadAnimMesh_Strict("GFX\map\props\DoorFrameRCZ.b3d")
	ElseIf big=DOOR_ELEVATOR Lor big=DOOR_ELEVATOR_3FLOOR Then
		For d2 = Each Doors
			If d2 <> d And d2\dir = DOOR_ELEVATOR Then
				d\obj = CopyEntity(d2\obj)
				d\obj2 = CopyEntity(d2\obj2)
				ScaleEntity d\obj, RoomScale, RoomScale, RoomScale
				ScaleEntity d\obj2, RoomScale, RoomScale, RoomScale
				Exit
			EndIf
		Next
		If d\obj=0 Then
			d\obj = LoadMesh_Strict("GFX\map\props\ElevatorDoor.b3d")
			d\obj2 = CopyEntity(d\obj)
			ScaleEntity d\obj, RoomScale, RoomScale, RoomScale
			ScaleEntity d\obj2, RoomScale, RoomScale, RoomScale
		EndIf
		d\frameobj = CopyEntity(d_I\DoorframeOBJ)
	ElseIf big=DOOR_STORAGE_ELEVATOR Then
		If d\obj=0 Then
			d\obj = LoadMesh_Strict("GFX\map\props\StorageElevatorDoor.b3d")
			d\obj2 = CopyEntity(d\obj)
			ScaleEntity d\obj, RoomScale, RoomScale, RoomScale
			ScaleEntity d\obj2, RoomScale, RoomScale, RoomScale
		EndIf
		d\frameobj = LoadMesh_Strict("GFX\map\props\StorageElevatorDoorFrame.b3d")
	ElseIf big=DOOR_LCZ Then
		For d2 = Each Doors
			If d2 <> d And d2\dir = DOOR_LCZ Then
				d\obj = CopyEntity(d2\obj)
				ScaleEntity(d\obj, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
				Exit
			EndIf
		Next
		If d\obj = 0 Then
			d\obj = LoadMesh_Strict("GFX\map\props\DoorLCZ.b3d")
			ScaleEntity(d\obj, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
		EndIf
		d\frameobj = CopyEntity(d_I\DoorframeOBJ)
	ElseIf big=DOOR_ONE_SIDED Then
		For d2 = Each Doors
			If d2 <> d And d2\dir = DOOR_ONE_SIDED Then
				d\obj = CopyEntity(d2\obj)
				ScaleEntity(d\obj, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
				Exit
			EndIf
		Next
		If d\obj = 0 Then
			d\obj = LoadMesh_Strict("GFX\map\props\DoorOneSided.b3d")
			ScaleEntity(d\obj, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
		EndIf
		d\frameobj = CopyEntity(d_I\DoorframeOBJ)
	ElseIf big=DOOR_CLASSIC Then
		For d2 = Each Doors
			If d2 <> d And d2\dir = DOOR_CLASSIC Then
				d\obj = CopyEntity(d2\obj)
				ScaleEntity(d\obj, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
				Exit
			EndIf
		Next
		If d\obj = 0 Then
			d\obj = LoadMesh_Strict("GFX\map\props\DoorOneSided2.b3d")
			ScaleEntity(d\obj, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
		EndIf
		d\frameobj = CopyEntity(d_I\DoorframeOBJ)
	ElseIf big=DOOR_CELL
		For d2 = Each Doors
			If d2 <> d And d2\dir = DOOR_CELL Then
				d\obj = CopyEntity(d2\obj)
				ScaleEntity(d\obj, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
				Exit
			EndIf
		Next
		If d\obj = 0 Then
			d\obj = LoadMesh_Strict("GFX\map\props\DoorCell.b3d")
			ScaleEntity(d\obj, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
		EndIf
		d\frameobj = CopyEntity(d_I\DoorframeOBJ)
	ElseIf big=DOOR_EZ Then
		For d2 = Each Doors
			If d2 <> d And d2\dir = DOOR_EZ Then
				d\obj = CopyEntity(d2\obj)
				ScaleEntity(d\obj, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
				Exit
			EndIf
		Next
		If d\obj = 0 Then
			d\obj = LoadMesh_Strict("GFX\map\props\DoorEZ.b3d")
			ScaleEntity(d\obj, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
		EndIf
		d\frameobj = CopyEntity(d_I\DoorframeOBJ)
	ElseIf big=DOOR_WINDOWED Then
		For d2 = Each Doors
			If d2 <> d And d2\dir = DOOR_WINDOWED Then
				d\obj = CopyEntity(d2\obj)
				ScaleEntity d\obj, RoomScale, RoomScale, RoomScale
				Exit
			EndIf
		Next
		If d\obj = 0 Then
			d\obj = LoadMesh_Strict("GFX\map\props\WindowedDoor.b3d")
			ScaleEntity d\obj, RoomScale, RoomScale, RoomScale
		EndIf
		d\frameobj = CopyEntity(d_I\DoorframeOBJ)
	ElseIf big=DOOR_WINDOWED_2 Then
		For d2 = Each Doors
			If d2 <> d And d2\dir = DOOR_WINDOWED_2 Then
				d\obj = CopyEntity(d2\obj)
				ScaleEntity d\obj, RoomScale, RoomScale, RoomScale
				Exit
			EndIf
		Next
		If d\obj = 0 Then
			d\obj = LoadMesh_Strict("GFX\map\props\WindowedDoor2.b3d")
			ScaleEntity d\obj, RoomScale, RoomScale, RoomScale
		EndIf
		d\frameobj = CopyEntity(d_I\DoorframeOBJ)
	ElseIf big=DOOR_OFFICE Then
		For d2 = Each Doors
			If d2 <> d And d2\dir = DOOR_OFFICE Then
				d\obj = CopyEntity(d2\obj)
				ScaleEntity d\obj, RoomScale, RoomScale, RoomScale
				Exit
			EndIf
		Next
		If d\obj = 0 Then
			d\obj = LoadAnimMesh_Strict("GFX\map\props\OfficeDoor.b3d")
			ScaleEntity d\obj, RoomScale, RoomScale, RoomScale
		EndIf
		d\frameobj = LoadMesh_Strict("GFX\map\props\OfficeDoorFrame.b3d")
	ElseIf big=DOOR_OFFICE_2 Then
		For d2 = Each Doors
			If d2 <> d And d2\dir = DOOR_OFFICE_2 Then
				d\obj = CopyEntity(d2\obj)
				ScaleEntity d\obj, RoomScale, RoomScale, RoomScale
				Exit
			EndIf
		Next
		If d\obj = 0 Then
			d\obj = LoadAnimMesh_Strict("GFX\map\props\OfficeDoor2.b3d")
			ScaleEntity d\obj, RoomScale, RoomScale, RoomScale
		EndIf
		d\frameobj = LoadMesh_Strict("GFX\map\props\OfficeDoorFrame.b3d")
	ElseIf big=DOOR_087B Then
		For d2 = Each Doors
			If d2 <> d And d2\dir = DOOR_087B Then
				d\obj = CopyEntity(d2\obj)
				ScaleEntity(d\obj, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
				Exit
			EndIf
		Next
		If d\obj = 0 Then
			d\obj = LoadMesh_Strict("GFX\map\props\Door087B.b3d")
			ScaleEntity(d\obj, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
		EndIf
		d\frameobj = CopyEntity(d_I\DoorframeOBJ)
	Else
		d\obj = CopyEntity(d_I\DoorOBJ)
		ScaleEntity(d\obj, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
		
		d\frameobj = CopyEntity(d_I\DoorframeOBJ)
		d\obj2 = CopyEntity(d_I\DoorOBJ)
		
		ScaleEntity(d\obj2, (204.0 * RoomScale) / MeshWidth(d\obj), 312.0 * RoomScale / MeshHeight(d\obj), 16.0 * RoomScale / MeshDepth(d\obj))
	EndIf
	
	PositionEntity d\frameobj, x, y, z	
	ScaleEntity(d\frameobj, RoomScale, RoomScale, RoomScale)
	EntityType d\obj, HIT_MAP
	If d\obj2 <> 0 Then EntityType d\obj2, HIT_MAP
	
	d\ID = d_I\DoorTempID
	d_I\DoorTempID=d_I\DoorTempID+1
	
	d\KeyCard = keycard
	d\Code = code
	
	d\Level = lvl
	d\LevelDest = 66
	
	If (big<>DOOR_ELEVATOR_3FLOOR And big<>DOOR_STORAGE_ELEVATOR) Then
		For i% = 0 To 1
			If code <> "" Then 
				d\buttons[i]= CopyEntity(d_I\ButtonOBJ[BUTTON_KEYPAD])
				EntityFX(d\buttons[i], 1)
			Else
				If keycard > -1 Then
					d\buttons[i] = CopyEntity(d_I\ButtonOBJ[BUTTON_KEYCARD])
				ElseIf keycard < -1
					d\buttons[i]= CopyEntity(d_I\ButtonOBJ[BUTTON_SCANNER])	
				Else
					If big <> DOOR_OFFICE And big <> DOOR_OFFICE_2 Then
						d\buttons[i] = CopyEntity(d_I\ButtonOBJ[BUTTON_NORMAL])
					Else
						d\buttons[i] = CreatePivot()
					EndIf
				EndIf
			EndIf
			
			ScaleEntity(d\buttons[i], 0.03, 0.03, 0.03)
		Next
	Else
		d\buttons[0] = CopyEntity(d_I\ButtonOBJ[BUTTON_ELEVATOR_3FLOOR])
		d\buttons[1] = CopyEntity(d_I\ButtonOBJ[BUTTON_NORMAL])
		EntityFX d\buttons[0],1
		ScaleEntity d\buttons[0],RoomScale*0.25,RoomScale*0.25,RoomScale*0.25
		ScaleEntity d\buttons[1],0.03,0.03,0.03
	EndIf
	
	If big = DOOR_CONTAINMENT Then
		PositionEntity d\buttons[0], x - 432.0 * RoomScale, y + 0.7, z + 192.0 * RoomScale
		PositionEntity d\buttons[1], x + 432.0 * RoomScale, y + 0.7, z - 192.0 * RoomScale
		RotateEntity d\buttons[0], 0, 90, 0
		RotateEntity d\buttons[1], 0, 270, 0
	ElseIf big = DOOR_ELEVATOR_3FLOOR Then
		PositionEntity d\buttons[0], x + 0.6, y + 0.6, z - 0.13
		PositionEntity d\buttons[1], x - 0.6, y + 0.6, z + 0.1
		RotateEntity d\buttons[1], 0, 180, 0
	ElseIf big = DOOR_STORAGE_ELEVATOR Then
		PositionEntity d\buttons[0], x + 0.6, y + 0.6, z - 0.13
		PositionEntity d\buttons[1], x - 0.6, y + 0.6, z + 0.1
		RotateEntity d\buttons[1], 0, 180, 0
	ElseIf big = DOOR_OFFICE And big = DOOR_OFFICE_2
		If code <> "" Lor keycard <> -1 Then
			PositionEntity d\buttons[0], x + 0.6, y + 0.7, z - 0.1
			PositionEntity d\buttons[1], x - 0.6, y + 0.7, z + 0.1
			RotateEntity d\buttons[1], 0, 180, 0
		Else
			For i = 0 To 1
				PositionEntity d\buttons[i], EntityX(FindChild(d\obj,"handle_"+(i+1)),True), EntityY(FindChild(d\obj,"handle_"+(i+1)),True), EntityZ(FindChild(d\obj,"handle_"+(i+1)),True)
			Next
		EndIf
	Else
		PositionEntity d\buttons[0], x + 0.6, y + 0.7, z - 0.1
		PositionEntity d\buttons[1], x - 0.6, y + 0.7, z + 0.1
		RotateEntity d\buttons[1], 0, 180, 0
	End If
	
	If big <> DOOR_OFFICE And big <> DOOR_OFFICE_2 Then
		For i = 0 To 1
			If d\buttons[i] <> 0 Then
				EntityParent(d\buttons[i], d\frameobj)
				EntityPickMode(d\buttons[i], 2)
			EndIf
		Next
	Else
		If code <> "" Lor keycard <> -1 Then
			For i = 0 To 1
				If d\buttons[i] <> 0 Then
					EntityParent(d\buttons[i], d\frameobj)
					EntityPickMode(d\buttons[i], 2)
				EndIf
			Next
		Else
			For i = 0 To 1
				EntityParent(d\buttons[i], FindChild(d\obj,"handle_"+(i+1)))
			Next
			For i = 0 To 1
				EntityRadius(d\buttons[i], 0.03)
				EntityPickMode(d\buttons[i], 1)
			Next
		EndIf
	EndIf

	PositionEntity d\obj, x, y, z
	
	RotateEntity d\obj, 0, angle, 0
	RotateEntity d\frameobj, 0, angle, 0
	
	If d\obj2 <> 0 Then
		PositionEntity d\obj2, x, y, z
		If big = DOOR_CONTAINMENT Then
			RotateEntity(d\obj2, 0, angle, 0)
		Else
			RotateEntity(d\obj2, 0, angle + 180, 0)
		EndIf
		EntityParent(d\obj2, parent)
	EndIf
	
	EntityParent(d\frameobj, parent)
	EntityParent(d\obj, parent)
	
	d\angle = angle
	d\open = dopen		
	
	EntityPickMode(d\obj, 3)
	MakeCollBox(d\obj)
	If d\obj2 <> 0 Then
		EntityPickMode(d\obj2, 3)
		MakeCollBox(d\obj2)
	End If
	
	EntityPickMode d\frameobj,2
	
	If d\open And big = False And Rand(8) = 1 Then d\AutoClose = True
	d\dir=big
	d\room=room
	
	d\MTFClose = True
	
	Return d
	
End Function

Function UpdateDoors()
	Local i%, d.Doors, x#, z#
	If d_I\UpdateDoorsTimer =< 0 Then
		For d.Doors = Each Doors
			
			d\dist = DistanceSquared(EntityX(Collider),EntityX(d\obj,True),EntityZ(Collider),EntityZ(d\obj,True))
			
			If d\dist > PowTwo(HideDistance*2) And d\IsElevatorDoor = 0 Then
				If d\obj <> 0 Then HideEntity d\obj
				If d\frameobj <> 0 Then HideEntity d\frameobj
				If d\obj2 <> 0 Then HideEntity d\obj2
				If d\buttons[0] <> 0 Then HideEntity d\buttons[0]
				If d\buttons[1] <> 0 Then HideEntity d\buttons[1]				
			Else
				If d\obj <> 0 Then ShowEntity d\obj
				If d\frameobj <> 0 Then ShowEntity d\frameobj
				If d\obj2 <> 0 Then ShowEntity d\obj2
				If d\buttons[0] <> 0 Then ShowEntity d\buttons[0]
				If d\buttons[1] <> 0 Then ShowEntity d\buttons[1]
			EndIf
		Next
		
		d_I\UpdateDoorsTimer = 30
	Else
		d_I\UpdateDoorsTimer = Max(d_I\UpdateDoorsTimer-FPSfactor,0)
	EndIf
	
	d_I\ClosestButton = 0
	d_I\ClosestDoor = Null
	
	For d.Doors = Each Doors
		
		If d\dist <= PowTwo(HideDistance*2) Lor d\IsElevatorDoor>0 Then ;Make elevator doors update everytime because if not, this can cause a bug where the elevators suddenly won't work, most noticeable in room2tunnel - ENDSHN
			
			If (d\openstate >= 180 Lor d\openstate <= 0) And GrabbedEntity = 0 Then
				For i% = 0 To 1
					If d\buttons[i] <> 0 Then
						If Abs(EntityX(Collider)-EntityX(d\buttons[i],True)) < 1.0 Then 
							If Abs(EntityZ(Collider)-EntityZ(d\buttons[i],True)) < 1.0 Then 
								Local dist# = DistanceSquared(EntityX(Collider, True), EntityX(d\buttons[i], True), EntityZ(Collider, True), EntityZ(d\buttons[i], True))
								If dist < PowTwo(0.7) Then
									Local temp% = CreatePivot()
									PositionEntity temp, EntityX(Camera), EntityY(Camera), EntityZ(Camera)
									PointEntity temp,d\buttons[i]
									
									If EntityPick(temp, 0.6) = d\buttons[i] Then
										If d_I\ClosestButton = 0 Then
											d_I\ClosestButton = d\buttons[i]
											d_I\ClosestDoor = d
										Else
											If dist < EntityDistanceSquared(Collider, d_I\ClosestButton) Then d_I\ClosestButton = d\buttons[i] : d_I\ClosestDoor = d
										EndIf							
									EndIf
									
									temp = FreeEntity_Strict(temp)
								EndIf							
							EndIf
						EndIf
						
					EndIf
				Next
			EndIf
			
			If d\open Then
				If d\openstate < 180 Then
					Select d\dir
						Case DOOR_DEFAULT, DOOR_WINDOWED, DOOR_ONE_SIDED, DOOR_EZ, DOOR_HCZ, DOOR_LCZ, DOOR_CLASSIC, DOOR_087B
							d\openstate = Min(180, d\openstate + FPSfactor * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * (d\fastopen*2+1) * FPSfactor / 80.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate)* (d\fastopen+1) * FPSfactor / 80.0, 0, 0)	
							If d\dir = DOOR_HCZ Then
								Animate2(d\obj, AnimTime(d\obj), 1, 20, 0.7, False)
							EndIf
						Case DOOR_CELL
							d\openstate = Min(180, d\openstate + FPSfactor * 1.2 * (d\fastopen+1))
							MoveEntity(d\obj, 0, Sin(d\openstate) * (d\fastopen*2+1) * FPSfactor / 80.0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, 0, Sin(d\openstate)* (d\fastopen+1) * FPSfactor / 80.0, 0)
						Case DOOR_RCZ
							d\openstate = Min(180.0, d\openstate + (FPSfactor * 1.2 * (d\fastopen + 1)))
							MoveEntity(d\obj, 0.0, Sin(d\openstate) * (d\fastopen + 1) * FPSfactor / 82.0, 0.0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, 0.0, -(Sin(d\openstate) * (d\fastopen + 1) * FPSfactor / 82.0), 0.0)
							Animate2(d\frameobj,AnimTime(d\frameobj), 1, 20, 0.15, False)
						Case DOOR_CONTAINMENT
							d\openstate = Min(180, d\openstate + FPSfactor * 0.8)
							MoveEntity(d\obj, Sin(d\openstate) * FPSfactor / 180.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, -Sin(d\openstate) * FPSfactor / 180.0, 0, 0)
						Case DOOR_ELEVATOR,DOOR_ELEVATOR_3FLOOR
							d\openstate = Min(180, d\openstate + FPSfactor * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * (d\fastopen*2+1) * FPSfactor / 162.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate)* (d\fastopen*2+1) * FPSfactor / 162.0, 0, 0)
						Case DOOR_STORAGE_ELEVATOR
							d\openstate = Min(180, d\openstate + FPSfactor * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * (d\fastopen+1) * FPSfactor / 85.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate)* (d\fastopen*2+1) * FPSfactor / 120.0, 0, 0)
						Case DOOR_914
							d\openstate = Min(180, d\openstate + FPSfactor * 1.4)
							MoveEntity(d\obj, Sin(d\openstate) * FPSfactor / 114.0, 0, 0)
						Case DOOR_OFFICE, DOOR_OFFICE_2
							d\openstate = Min(180, d\openstate + FPSfactor * 2)
							RotateEntity(d\obj, 0, PlayerRoom\angle + d\openstate/2 + d\angle, 0)
							Animate2(d\obj,AnimTime(d\obj),1,20,0.75,False)
					End Select
				Else
					If d\dir = DOOR_RCZ Then
						SetAnimTime(d\frameobj, 21)
					ElseIf d\dir = DOOR_HCZ Then
						SetAnimTime(d\obj, 21)
					EndIf
					d\fastopen = 0
					ResetEntity(d\obj)
					If d\obj2 <> 0 Then ResetEntity(d\obj2)
					If d\timerstate > 0 Then
						d\timerstate = Max(0, d\timerstate - FPSfactor)
						If d\timerstate + FPSfactor > 110 And d\timerstate <= 110 Then d\SoundCHN = PlaySound2(CautionSFX, Camera, d\obj)
						Local sound%
						If d\dir = 1 Then sound% = Rand(0, 1) Else sound% = Rand(0, 2)
						If d\timerstate = 0 Then d\open = (Not d\open) : d\SoundCHN = PlaySound2(CloseDoorSFX[d\dir * 3 + sound], Camera, d\obj)
					EndIf
					If d\AutoClose And RemoteDoorOn = True Then
						If EntityDistanceSquared(Camera, d\obj) < PowTwo(2.1) Then
							If (Not I_714\Using) Then PlaySound_Strict HorrorSFX[7]
							d\open = False : d\SoundCHN = PlaySound2(CloseDoorSFX[Min(d\dir, 1) * 3 + Rand(0, 2)], Camera, d\obj) : d\AutoClose = False
						EndIf
					EndIf				
				EndIf
			Else
				If d\openstate > 0 Then
					Select d\dir
						Case DOOR_DEFAULT, DOOR_WINDOWED, DOOR_ONE_SIDED, DOOR_EZ, DOOR_HCZ, DOOR_LCZ, DOOR_CLASSIC, DOOR_087B
							d\openstate = Max(0, d\openstate - FPSfactor * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * -FPSfactor * (d\fastopen+1) / 80.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate) * (d\fastopen+1) * -FPSfactor / 80.0, 0, 0)
							If d\dir = DOOR_HCZ Then
								Animate2(d\obj, AnimTime(d\obj), 21, 50, 0.7, False)
							EndIf
						Case DOOR_CELL
							d\openstate = Max(0, d\openstate - FPSfactor * 1.2 * (d\fastopen+1))
							MoveEntity(d\obj, 0, Sin(d\openstate) * -FPSfactor * (d\fastopen+1) / 80.0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, 0, Sin(d\openstate) * (d\fastopen+1) * -FPSfactor / 80.0, 0)
						Case DOOR_RCZ
							d\openstate = Max(0.0, d\openstate - (FPSfactor * 1.2 * (d\fastopen + 1)))
							MoveEntity(d\obj, 0.0, Sin(d\openstate) * (-FPSfactor) * (d\fastopen + 1) / 82.0, 0.0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, 0.0, -(Sin(d\openstate) * (-FPSfactor) * (d\fastopen + 1) / 82.0), 0.0)
							Animate2(d\frameobj,AnimTime(d\frameobj), 21, 40, 0.45, False)
						Case DOOR_CONTAINMENT
							d\openstate = Max(0, d\openstate - FPSfactor*0.8)
							MoveEntity(d\obj, Sin(d\openstate) * -FPSfactor / 180.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate) * FPSfactor / 180.0, 0, 0)
							If d\openstate < 15 And d\openstate+FPSfactor => 15
								If ParticleAmount=2
									For i = 0 To Rand(75,99)
										Local pvt% = CreatePivot()
										PositionEntity(pvt, EntityX(d\frameobj,True)+Rnd(-0.2,0.2), EntityY(d\frameobj,True)+Rnd(0.0,1.2), EntityZ(d\frameobj,True)+Rnd(-0.2,0.2))
										RotateEntity(pvt, 0, Rnd(360), 0)
										
										Local p.Particles = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 2, 0.002, 0, 300)
										p\speed = 0.005
										RotateEntity(p\pvt, Rnd(-20, 20), Rnd(360), 0)
										
										p\SizeChange = -0.00001
										p\size = 0.01
										ScaleSprite p\obj,p\size,p\size
										
										p\Achange = -0.01
										
										EntityOrder p\obj,-1
										
										pvt = FreeEntity_Strict(pvt)
									Next
								EndIf
							EndIf
						Case DOOR_ELEVATOR,DOOR_ELEVATOR_3FLOOR
							d\openstate = Max(0, d\openstate - FPSfactor * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * -FPSfactor * (d\fastopen+1) / 162.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate) * (d\fastopen+1) * -FPSfactor / 162.0, 0, 0)
						Case DOOR_STORAGE_ELEVATOR
							d\openstate = Max(0, d\openstate - FPSfactor * 2 * (d\fastopen+1))
							MoveEntity(d\obj, Sin(d\openstate) * -FPSfactor * (d\fastopen+1) / 85.0, 0, 0)
							If d\obj2 <> 0 Then MoveEntity(d\obj2, Sin(d\openstate) * (d\fastopen+1) * -FPSfactor / 120.0, 0, 0)
						Case DOOR_914
							d\openstate = Min(180, d\openstate - FPSfactor * 1.4)
							MoveEntity(d\obj, Sin(d\openstate) * -FPSfactor / 114.0, 0, 0)
						Case DOOR_OFFICE, DOOR_OFFICE_2
							d\openstate = Min(180, d\openstate - FPSfactor * 2)
							RotateEntity(d\obj, 0, PlayerRoom\angle + d\openstate/2 + d\angle, 0)
							Animate2(d\obj,AnimTime(d\obj),20,1,-0.75,False)
					End Select
					
					If d\angle = 0 Lor d\angle=180 Then
						If Abs(EntityZ(d\frameobj, True)-EntityZ(Collider))<0.15 Then
							If Abs(EntityX(d\frameobj, True)-EntityX(Collider))<0.7*(d\dir*2+1) Then
								z# = CurveValue(EntityZ(d\frameobj,True)+0.15*Sgn(EntityZ(Collider)-EntityZ(d\frameobj, True)), EntityZ(Collider), 5)
								PositionEntity Collider, EntityX(Collider), EntityY(Collider), z
							EndIf
						EndIf
					Else
						If Abs(EntityX(d\frameobj, True)-EntityX(Collider))<0.15 Then	
							If Abs(EntityZ(d\frameobj, True)-EntityZ(Collider))<0.7*(d\dir*2+1) Then
								x# = CurveValue(EntityX(d\frameobj,True)+0.15*Sgn(EntityX(Collider)-EntityX(d\frameobj, True)), EntityX(Collider), 5)
								PositionEntity Collider, x, EntityY(Collider), EntityZ(Collider)
							EndIf
						EndIf
					EndIf
					
					If d\DoorHitOBJ <> 0 Then
						ShowEntity d\DoorHitOBJ
					EndIf
				Else
					If d\dir = DOOR_RCZ Then
						SetAnimTime(d\frameobj, 1)
					ElseIf d\dir = DOOR_HCZ Then
						SetAnimTime(d\obj, 1)
					EndIf
					
					d\fastopen = 0
					PositionEntity(d\obj, EntityX(d\frameobj, True), EntityY(d\frameobj, True), EntityZ(d\frameobj, True))
					If d\obj2 <> 0 Then PositionEntity(d\obj2, EntityX(d\frameobj, True), EntityY(d\frameobj, True), EntityZ(d\frameobj, True))
					If d\obj2 <> 0 And d\dir = 0 Then
						MoveEntity(d\obj, 0, 0, 8.0 * RoomScale)
						MoveEntity(d\obj2, 0, 0, 8.0 * RoomScale)
					EndIf
					If d\dir = DOOR_OFFICE
						MoveEntity(d\obj, (((d\dir = DOOR_OFFICE) * - 96.0))* RoomScale, 0.0, 0.0)
					EndIf
					If d\dir = DOOR_OFFICE_2
						MoveEntity(d\obj, (((d\dir = DOOR_OFFICE_2) * - 96.0))* RoomScale, 0.0, 0.0)
					EndIf
					If d\DoorHitOBJ <> 0 Then
						HideEntity d\DoorHitOBJ
					EndIf
				EndIf
			EndIf
			
			If d\dir <> DOOR_OFFICE And d\dir <> DOOR_OFFICE_2 Then
				If d\locked <> d\lockedupdated Then
					If d\locked Then
						For i% = 0 To 1
							If (d\dir <> DOOR_ELEVATOR_3FLOOR And d\dir <> DOOR_STORAGE_ELEVATOR) Lor i = 1 Then
								If d\IsElevatorDoor > 0 Then
									If d\buttons[i] <> 0 Then EntityTexture(d\buttons[i], d_I\ButtonTexture)
								Else
									If d\buttons[i] <> 0 Then EntityTexture(d\buttons[i], d_I\ButtonTextureLocked)
								EndIf
							EndIf
						Next
					Else
						For i% = 0 To 1
							If (d\dir <> DOOR_ELEVATOR_3FLOOR And d\dir <> DOOR_STORAGE_ELEVATOR) Lor i = 1 Then
								If d\buttons[i] <> 0 Then EntityTexture(d\buttons[i], d_I\ButtonTexture)
							EndIf
						Next
					EndIf
					d\lockedupdated = d\locked
				EndIf
			EndIf
			
		EndIf
		
		If d\dir = DOOR_OFFICE And d\Code = "" And d\KeyCard = -1 Then
			For i = 0 To 1
				PositionEntity d\buttons[i], EntityX(FindChild(d\obj,"handle_"+(i+1)),True), EntityY(FindChild(d\obj,"handle_"+(i+1)),True), EntityZ(FindChild(d\obj,"handle_"+(i+1)),True)
			Next
		ElseIf d\dir = DOOR_OFFICE_2 And d\Code = "" And d\KeyCard = -1 Then
			For i = 0 To 1
				PositionEntity d\buttons[i], EntityX(FindChild(d\obj,"handle_"+(i+1)),True), EntityY(FindChild(d\obj,"handle_"+(i+1)),True), EntityZ(FindChild(d\obj,"handle_"+(i+1)),True)
			Next
		EndIf
		
		UpdateSoundOrigin(d\SoundCHN,Camera,d\frameobj)
		
		If d\DoorHitOBJ<>0 Then
			If DebugHUD Then
				EntityAlpha d\DoorHitOBJ,0.5
			Else
				EntityAlpha d\DoorHitOBJ,0.0
			EndIf
		EndIf
	Next
End Function

Function UseDoor(d.Doors, showmsg%=True, playsfx%=True)
	Local temp% = 0
	
	If d\KeyCard > -1 Then
		If SelectedItem = Null Then
			If showmsg = True Then
				If (Instr(m_msg\Txt,GetLocalString("Doors", "keycard_inserted"))=0 And (Instr(m_msg\Txt,GetLocalString("Doors", "keycard_nothing"))=0 And Instr(m_msg\Txt,GetLocalStringR("Doors", "keycard_required2", d\KeyCard))=0)) Lor (m_msg\Timer<70*3) Then
					CreateMsg(GetLocalString("Doors", "keycard_required"))
				EndIf
			EndIf
			Return
		Else
			Select SelectedItem\itemtemplate\tempname
				Case "key_cave"
					temp = KEY_CARD_CAVE_1
				Case "key_cave2"
					temp = KEY_CARD_CAVE_2
				Case "key0"
					temp = KEY_CARD_0
				Case "key1"
					temp = KEY_CARD_1
				Case "key2"
					temp = KEY_CARD_2
				Case "key3"
					temp = KEY_CARD_3
				Case "key4"
					temp = KEY_CARD_4
				Case "key5"
					temp = KEY_CARD_5
				Case "key6"
					temp = KEY_CARD_OMNI
				Case "scp005"
					temp = SCP_005
				Default 
					temp = -1
			End Select
			
			If d\Code <> ""
				If SelectedItem = Null Then
					If (Not d\locked)
						If (d\Code <> "GEAR") And (d\Code = KeypadInput) Then
							PlaySound2(ScannerSFX1, Camera, d_I\ClosestButton)
						Else
							PlaySound2(ScannerSFX2, Camera, d_I\ClosestButton)
							Return
						EndIf
					Else
						PlaySound2(ScannerSFX2, Camera, d_I\ClosestButton)
						Return
					EndIf
				Else
					If temp = SCP_005 Then
						If d\dir = DOOR_OFFICE And d\dir = DOOR_OFFICE_2 Then
							If d\locked = True Then
								PlaySound2(DoorBudgeSFX, Camera, d_I\ClosestButton)
								If d\open Then
									CreateMsg(GetLocalString("Doors", "door_office_nothing"))
								Else    
									CreateMsg(GetLocalString("Doors", "door_office_locked"))
								EndIf
							EndIf
						Else
							If d\locked = True Then
								CreateMsg(GetLocalString("Items", "scp005_5"))
							Else
								CreateMsg(GetLocalString("Items", "scp005_6"))
							EndIf
						EndIf
					EndIf
					SelectedItem = Null
					
					If (Not d\locked) Then
						If (d\Code <> "GEAR") And (temp = SCP_005) Then
							PlaySound2(ScannerSFX1, Camera, d_I\ClosestButton)
						Else
							PlaySound2(ScannerSFX2, Camera, d_I\ClosestButton)
							Return
						EndIf
					Else
						PlaySound2(ScannerSFX2, Camera, d_I\ClosestButton)
						Return
					EndIf
				EndIf
			EndIf
			
			If temp = -1 Then 
				If showmsg = True Then
					If (Instr(m_msg\Txt,GetLocalString("Doors", "keycard_inserted"))=0 And (Instr(m_msg\Txt,GetLocalString("Doors", "keycard_nothing"))=0 And Instr(m_msg\Txt,GetLocalStringR("Doors", "keycard_required2", d\KeyCard))=-1)) Lor (m_msg\Timer<70*3) Then
						CreateMsg(GetLocalString("Doors", "keycard_required"))
					EndIf
				EndIf
				Return
			ElseIf temp >= d\KeyCard And ((temp <> KEY_CARD_CAVE_1 And temp <> KEY_CARD_CAVE_2) Lor d\KeyCard = temp) Then
				If showmsg = True Then
					If d\dir = DOOR_OFFICE And d\dir = DOOR_OFFICE_2 Then
						If d\locked = True Then
							PlaySound2(DoorBudgeSFX, Camera, d_I\ClosestButton)
							PlaySound_Strict KeyCardSFX2
							If d\open Then
								CreateMsg(GetLocalString("Doors", "door_office_nothing"))
							Else    
								CreateMsg(GetLocalString("Doors", "door_office_locked"))
							EndIf
						Else
							PlaySound_Strict KeyCardSFX1
						EndIf
					Else
						If d\locked Then
							If temp = SCP_005 Then
								CreateMsg(GetLocalString("Items", "scp005_1"))
							Else
								PlaySound_Strict KeyCardSFX2
								CreateMsg(GetLocalString("Doors", "keycard_nothing"))
							EndIf
							Return
						Else
							PlaySound_Strict KeyCardSFX1
							If temp = SCP_005 Then
								CreateMsg(GetLocalString("Items", "scp005_2"))
							Else
								CreateMsg(GetLocalString("Doors", "keycard_inserted"))
							EndIf
						EndIf
					EndIf
				EndIf
			Else
				If showmsg = True Then
					PlaySound_Strict KeyCardSFX2
					If d\dir = DOOR_OFFICE And d\dir = DOOR_OFFICE_2 Then
						If d\locked = True Then
							PlaySound2(DoorBudgeSFX, Camera, d_I\ClosestButton)
							If d\open Then
								CreateMsg(GetLocalString("Doors", "door_office_nothing"))
							Else    
								CreateMsg(GetLocalString("Doors", "door_office_locked"))
							EndIf
						EndIf
					Else
						If d\locked Then
							If temp = SCP_005 Then
								CreateMsg(GetLocalString("Items", "scp005_1"))
							Else
								CreateMsg(GetLocalString("Doors", "keycard_nothing"))
							EndIf
						ElseIf d\KeyCard >= KEY_CARD_CAVE_1 And d\KeyCard <> SCP_005 Then
							If temp <> SCP_005 Then
								CreateMsg(GetLocalString("Doors", "keycard_required3"))
							Else
								CreateMsg(GetLocalString("Items", "scp005_2"))
							EndIf
						Else
							CreateMsg(GetLocalStringR("Doors", "keycard_required2", d\KeyCard))
						EndIf
					EndIf
				EndIf
				Return
			EndIf	
		EndIf	
	ElseIf d\KeyCard  = SEVERED_HAND
		If SelectedItem <> Null And (SelectedItem\itemtemplate\tempname = "hand" Lor SelectedItem\itemtemplate\tempname = "scp005")
			PlaySound_Strict ScannerSFX1
			If temp <> SCP_005 Then
				If (Instr(m_msg\Txt,GetLocalString("Doors", "scanner_denied"))=0) Lor (m_msg\Timer < 70*3) Then
					CreateMsg(GetLocalString("Doors", "scanner_granted"))
				EndIf
			Else
				If (Instr(m_msg\Txt,GetLocalString("Items", "scp005_4"))=0) Lor (m_msg\Timer < 70*3) Then
					CreateMsg(GetLocalString("Items", "scp005_3"))
				EndIf
			EndIf
		Else
			If showmsg = True Then 
				PlaySound_Strict ScannerSFX2
				If temp <> SCP_005 Then
					CreateMsg(GetLocalString("Doors", "scanner_denied"))
				Else
					CreateMsg(GetLocalString("Items", "scp005_4"))
				EndIf
			EndIf
			Return
		EndIf
	ElseIf d\KeyCard  = SEVERED_HAND_2
		If SelectedItem <> Null And (SelectedItem\itemtemplate\tempname = "hand2" Lor SelectedItem\itemtemplate\tempname = "scp005")
			PlaySound_Strict ScannerSFX1
			If temp <> SCP_005 Then
				If (Instr(m_msg\Txt,GetLocalString("Doors", "scanner_denied"))=0) Lor (m_msg\Timer < 70*3) Then
					CreateMsg(GetLocalString("Doors", "scanner_granted"))
				EndIf
			Else
				If (Instr(m_msg\Txt,GetLocalString("Items", "scp005_4"))=0) Lor (m_msg\Timer < 70*3) Then
					CreateMsg(GetLocalString("Items", "scp005_3"))
				EndIf
			EndIf
		Else
			If showmsg = True Then 
				PlaySound_Strict ScannerSFX2
				If temp <> SCP_005 Then
					CreateMsg(GetLocalString("Doors", "scanner_denied"))
				Else
					CreateMsg(GetLocalString("Items", "scp005_4"))
				EndIf
			EndIf
			Return
		EndIf
	ElseIf d\KeyCard  = SEVERED_HAND_3
		If SelectedItem <> Null And (SelectedItem\itemtemplate\tempname = "hand3" Lor SelectedItem\itemtemplate\tempname = "scp005")
			PlaySound_Strict ScannerSFX1
			If temp <> SCP_005 Then
				If (Instr(m_msg\Txt,GetLocalString("Doors", "scanner_denied"))=0) Lor (m_msg\Timer < 70*3) Then
					CreateMsg(GetLocalString("Doors", "scanner_granted"))
				EndIf
			Else
				If (Instr(m_msg\Txt,GetLocalString("Items", "scp005_4"))=0) Lor (m_msg\Timer < 70*3) Then
					CreateMsg(GetLocalString("Items", "scp005_3"))
				EndIf
			EndIf
		Else
			If showmsg = True Then 
				PlaySound_Strict ScannerSFX2
				If temp <> SCP_005 Then
					CreateMsg(GetLocalString("Doors", "scanner_denied"))
				Else
					CreateMsg(GetLocalString("Items", "scp005_4"))
				EndIf
			EndIf
			Return
		EndIf
	Else
		If d\locked Then
			If showmsg = True Then 
				If Not (d\IsElevatorDoor>0) Then
					If d\dir <> DOOR_OFFICE And d\dir <> DOOR_OFFICE_2 Then
						PlaySound_Strict ButtonSFX[1]
						If PlayerRoom\RoomTemplate\Name <> "room2_elevator_1" Then
							If d\open Then
								CreateMsg(GetLocalString("Doors", "door_nothing"))
							Else    
								CreateMsg(GetLocalString("Doors", "door_locked"))
							EndIf    
						Else
							CreateMsg(GetLocalString("Doors", "elevator_broken"))
						EndIf
					Else
						PlaySound2(DoorBudgeSFX, Camera, d_I\ClosestButton)
						If d\open Then
							CreateMsg(GetLocalString("Doors", "door_office_nothing"))
						Else    
							CreateMsg(GetLocalString("Doors", "door_office_locked"))
						EndIf
					EndIf
				Else
					If d\IsElevatorDoor = 1 Then
						CreateMsg(GetLocalString("Doors", "elevator_called"))
					ElseIf d\IsElevatorDoor = 3 Then
						CreateMsg(GetLocalString("Doors", "elevator_on_floor"))
					ElseIf (m_msg\Txt<>GetLocalString("Doors", "elevator_called"))
						If (CreateMsg(GetLocalString("Doors", "elevator_called2"))) Lor (m_msg\Timer < 70*3)	
							Select Rand(10)
								Case 1
									CreateMsg(GetLocalString("Doors", "elevator_rand_1"))
								Case 2
									CreateMsg(GetLocalString("Doors", "elevator_rand_2"))
								Case 3
									CreateMsg(GetLocalString("Doors", "elevator_rand_3"))
								Default
									CreateMsg(GetLocalString("Doors", "elevator_called2"))
							End Select
						EndIf
					Else
						CreateMsg(GetLocalString("Doors", "elevator_called2"))
					EndIf
				EndIf
				
			EndIf
			Return
		EndIf	
	EndIf
	
	OpenCloseDoor(d.Doors, playsfx)
End Function

Function UseDoorNPC(d.Doors, n.NPCs, playsfx%=True)
	If d\locked Lor d\KeyCard > n\Clearance Lor d\KeyCard < -1 Then Return
	OpenCloseDoor(d.Doors, playsfx)
End Function

Function OpenCloseDoor(d.Doors, playsfx%=True)	
	d\open = (Not d\open)
	If d\LinkedDoor <> Null Then d\LinkedDoor\open = (Not d\LinkedDoor\open)
	
	Local sound = 0
	If d\dir = DOOR_CONTAINMENT Then sound=Rand(0, 1) Else sound=Rand(0, 2)
	
	Local dir = d\dir
	If d\dir = DOOR_ELEVATOR_3FLOOR Lor d\dir = DOOR_STORAGE_ELEVATOR Then
		dir = DOOR_ELEVATOR
	EndIf
	If d\dir = DOOR_ONE_SIDED Lor d\dir = DOOR_LCZ Lor d\dir = DOOR_087B Then
		dir = DOOR_DEFAULT
	EndIf	
	
	If d\dir = DOOR_WINDOWED Lor d\dir = DOOR_WINDOWED_2 Then
		If playsfx=True Then
			If d\open Then
				If d\LinkedDoor <> Null Then d\LinkedDoor\timerstate = d\LinkedDoor\timer
				d\timerstate = d\timer
				d\SoundCHN = PlaySound2(OpenGlassDoorSFX, Camera, d\obj)
			Else
				d\SoundCHN = PlaySound2(CloseGlassDoorSFX, Camera, d\obj)
			EndIf
			UpdateSoundOrigin(d\SoundCHN,Camera,d\obj)
		Else
			If d\open Then
				If d\LinkedDoor <> Null Then d\LinkedDoor\timerstate = d\LinkedDoor\timer
				d\timerstate = d\timer
			EndIf
		EndIf
	ElseIf d\dir = DOOR_CLASSIC Lor d\dir = DOOR_CELL Then
		If playsfx=True Then
			If d\open Then
				If d\LinkedDoor <> Null Then d\LinkedDoor\timerstate = d\LinkedDoor\timer
				d\timerstate = d\timer
				d\SoundCHN = PlaySound2(OpenClassicDoorSFX, Camera, d\obj)
			Else
				d\SoundCHN = PlaySound2(CloseClassicDoorSFX, Camera, d\obj)
			EndIf
			UpdateSoundOrigin(d\SoundCHN,Camera,d\obj)
		Else
			If d\open Then
				If d\LinkedDoor <> Null Then d\LinkedDoor\timerstate = d\LinkedDoor\timer
				d\timerstate = d\timer
			EndIf
		EndIf
	ElseIf d\dir = DOOR_OFFICE Lor d\dir = DOOR_OFFICE_2 Then
		If playsfx=True Then
			If d\open Then
				If d\LinkedDoor <> Null Then d\LinkedDoor\timerstate = d\LinkedDoor\timer
				d\timerstate = d\timer
				d\SoundCHN = PlaySound2(OpenOfficeDoorSFX, Camera, d\obj)
			Else
				d\SoundCHN = PlaySound2(CloseOfficeDoorSFX, Camera, d\obj)
			EndIf
			UpdateSoundOrigin(d\SoundCHN,Camera,d\obj)
		Else
			If d\open Then
				If d\LinkedDoor <> Null Then d\LinkedDoor\timerstate = d\LinkedDoor\timer
				d\timerstate = d\timer
			EndIf
		EndIf
	ElseIf d\dir = DOOR_EZ
		If playsfx=True Then
			If d\open Then
				If d\LinkedDoor <> Null Then d\LinkedDoor\timerstate = d\LinkedDoor\timer
				d\timerstate = d\timer
				d\SoundCHN = PlaySound2(OpenEzDoorSFX, Camera, d\obj)
			Else
				d\SoundCHN = PlaySound2(CloseEzDoorSFX, Camera, d\obj)
			EndIf
			UpdateSoundOrigin(d\SoundCHN,Camera,d\obj)
		Else
			If d\open Then
				If d\LinkedDoor <> Null Then d\LinkedDoor\timerstate = d\LinkedDoor\timer
				d\timerstate = d\timer
			EndIf
		EndIf
	ElseIf d\dir = DOOR_HCZ Lor d\dir = DOOR_RCZ
		If playsfx=True Then
			If d\open Then
				If d\LinkedDoor <> Null Then d\LinkedDoor\timerstate = d\LinkedDoor\timer
				d\timerstate = d\timer
				d\SoundCHN = PlaySound2(OpenHCzDoorSFX, Camera, d\obj)
			Else
				d\SoundCHN = PlaySound2(CloseHCzDoorSFX, Camera, d\obj)
			EndIf
			UpdateSoundOrigin(d\SoundCHN,Camera,d\obj)
		Else
			If d\open Then
				If d\LinkedDoor <> Null Then d\LinkedDoor\timerstate = d\LinkedDoor\timer
				d\timerstate = d\timer
			EndIf
		EndIf
	Else
		If playsfx=True Then
			If d\open Then
				If d\LinkedDoor <> Null Then d\LinkedDoor\timerstate = d\LinkedDoor\timer
				d\timerstate = d\timer
				d\SoundCHN = PlaySound2(OpenDoorSFX[dir * 3 + sound], Camera, d\obj)
			Else
				d\SoundCHN = PlaySound2(CloseDoorSFX[dir * 3 + sound], Camera, d\obj)
			EndIf
			UpdateSoundOrigin(d\SoundCHN,Camera,d\obj)
		Else
			If d\open Then
				If d\LinkedDoor <> Null Then d\LinkedDoor\timerstate = d\LinkedDoor\timer
				d\timerstate = d\timer
			EndIf
		EndIf
	EndIf
	
End Function

Function RemoveDoor(d.Doors)
	Local i%
	
	If d\buttons[0] <> 0 Then EntityParent d\buttons[0], 0
	If d\buttons[1] <> 0 Then EntityParent d\buttons[1], 0	
	
	d\obj = FreeEntity_Strict(d\obj)
	d\obj2 = FreeEntity_Strict(d\obj2)
	d\frameobj = FreeEntity_Strict(d\frameobj)
	For i = 0 To 1
		d\buttons[i] = FreeEntity_Strict(d\buttons[i])
	Next	
	
	Delete d
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS