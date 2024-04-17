
Function FillRoom_Cont_427_714_860_1025(r.Rooms)
	Local d.Doors,it.Items,sc.SecurityCams,de.Decals
	Local i,n.NPCs
	
	r\Objects[0] = CreatePivot()
	PositionEntity r\Objects[0],r\x-854*RoomScale,0.0,r\z+257*RoomScale,True
	EntityParent r\Objects[0], r\obj
	
	d.Doors = CreateDoor(r\zone, r\x + 264.0 * RoomScale, 0, r\z, 90, r, True, False, KEY_CARD_3)
	d\AutoClose = False : d\open = False
	PositionEntity(d\buttons[0], r\x + 320.0 * RoomScale, EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
	PositionEntity(d\buttons[1], r\x + 224.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
	
	d.Doors = CreateDoor(r\zone, r\x - 264.0 * RoomScale, 0, r\z, 270, r, True, False, KEY_CARD_3)
	d\AutoClose = False : d\open = False
	PositionEntity(d\buttons[0], r\x - 320.0 * RoomScale, EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
	PositionEntity(d\buttons[1], r\x - 224.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x-560.0 * RoomScale, 0, r\z - 257.0 * RoomScale, 0, r, False, DOOR_OFFICE)		; ~ SCP-714
	r\RoomDoors[1]\AutoClose = False
	
	r\RoomDoors[2] = CreateDoor(r\zone, r\x + 560.0 * RoomScale, 0, r\z - 257.0 * RoomScale, 180, r, False, DOOR_OFFICE)	; ~ SCP-1025
	r\RoomDoors[2]\AutoClose = False
	
	r\RoomDoors[3] = CreateDoor(r\zone, r\x + 560.0 * RoomScale, 0, r\z + 257.0 * RoomScale, 180, r, False, DOOR_OFFICE)	; ~ SCP-860
	r\RoomDoors[3]\AutoClose = False
	
	r\RoomDoors[4] = CreateDoor(r\zone, r\x-560.0 * RoomScale, 0, r\z + 257.0 * RoomScale, 0, r, False, DOOR_OFFICE)		; ~ SCP-427
	r\RoomDoors[4]\AutoClose = False
	
	sc.SecurityCams = CreateSecurityCam(r\x + 560.0 * RoomScale, r\y + 386 * RoomScale, r\z - 416.0 * RoomScale, r)
	sc\angle = 180 : sc\turn = 30
	TurnEntity(sc\CameraObj, 30, 0, 0)
	EntityParent(sc\obj, r\obj)
	
	sc.SecurityCams = CreateSecurityCam(r\x - 560.0 * RoomScale, r\y + 386 * RoomScale, r\z + 480.0 * RoomScale, r)
	sc\angle = 0 : sc\turn = 30
	TurnEntity(sc\CameraObj, 30, 0, 0)
	EntityParent(sc\obj, r\obj)
	
	it.Items = CreateItem("SCP-714", "scp714", r\x - 560.0 * RoomScale, r\y + 185.0 * RoomScale, r\z - 760.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	;it.Items = CreateItem("SCP-1025", "scp1025", r\x + 560.0 * RoomScale, r\y + 185.0 * RoomScale, r\z - 760.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	it.Items = CreateItem("SCP-860", "scp860", r\x + 560.0 * RoomScale, r\y + 185.0 * RoomScale, r\z + 765.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	;it.Items = CreateItem("Document SCP-427", "paper", r\x - 608.0 * RoomScale, r\y + 185.0 * RoomScale, r\z + 636.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	;it.Items = CreateItem("Document SCP-714", "paper", r\x - 801.0 * RoomScale, r\y + 10.0 * RoomScale, r\z - 584.0 * RoomScale)
	;RotateEntity(it\collider, 0.0, r\angle, 0.0)
	;EntityParent(it\collider, r\obj)
	
	;it.Items = CreateItem("Document SCP-860", "paper", r\x + 875.0 * RoomScale, r\y + 222.0 * RoomScale, r\z + 797.0 * RoomScale)
	;RotateEntity(it\collider, 0.0, r\angle, 0.0)
	;EntityParent(it\collider, r\obj)
	
	Local dx#,dz#
	For i = 0 To 14
		Select i
			Case 0
				dx# = -64.0
				dz# = -516.0
			Case 1
				dx# = -96.0
				dz# = -388.0
			Case 2
				dx# = -128.0
				dz# = -292.0
			Case 3
				dx# = -128.0
				dz# = -132.0
			Case 4
				dx# = -160.0
				dz# = -36.0
			Case 5
				dx# = -192.0
				dz# = 28.0
			Case 6
				dx# = -384.0
				dz# = 28.0
			Case 7
				dx# = -448.0
				dz# = 92.0
			Case 8
				dx# = -480.0
				dz# = 124.0
			Case 9
				dx# = -512.0
				dz# = 156.0
			Case 10
				dx# = -544.0
				dz# = 220.0
			Case 11
				dx# = -544.0
				dz# = 380.0
			Case 12
				dx# = -544.0
				dz# = 476.0
			Case 13
				dx# = -544.0
				dz# = 572.0
			Case 14
				dx# = -544.0
				dz# = 636.0
		End Select
		de.Decals = CreateDecal(GetRandomDecalID(DECAL_TYPE_BLOODDROP),r\x+dx#*RoomScale,0.005,r\z+dz#*RoomScale,90,Rand(360),0)
		If i > 10 Then
			de\Size = Rnd(0.2,0.25)
		Else
			de\Size = Rnd(0.1,0.17)
		EndIf
		EntityAlpha(de\obj, 1.0) : ScaleSprite(de\obj,de\Size,de\Size)
		EntityParent de\obj, r\obj
	Next
	
	de.Decals = CreateDecal(DECAL_BLOODPOOL,EntityX(r\Objects[0],True),0.005,EntityZ(r\Objects[0],True),90,Rand(360),0)
	de\Size = Rnd(0.4,0.45)
	EntityAlpha(de\obj, 1.0) : ScaleSprite(de\obj,de\Size,de\Size)
	EntityParent de\obj, r\obj
	
End Function

Function UpdateEvent_Cont_427_714_860_1025(e.Events)
	
	If e\room\NPC[0] = Null Then
		e\room\NPC[0] = CreateNPC(NPC_Human,EntityX(e\room\Objects[0],True),1.0,EntityZ(e\room\Objects[0],True))
		SetNPCFrame e\room\NPC[0], 559
		e\room\NPC[0]\State[0]=3
		e\room\NPC[0]\IsDead=True
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D