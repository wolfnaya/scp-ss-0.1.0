
Function FillRoom_Room2_Elevator_2(r.Rooms)
	Local d.Doors,it.Items,de.Decals
	
	d = CreateDoor(r\zone, r\x + 1552.0 * RoomScale, r\y, r\z + 552.0 * RoomScale, 0, r, False, False)
	PositionEntity(d\buttons[0], EntityX(d\buttons[0],True), EntityY(d\buttons[0],True), r\z + 518.0 * RoomScale, True)
	PositionEntity(d\buttons[1], EntityX(d\buttons[1],True), EntityY(d\buttons[1],True), r\z + 575.0 * RoomScale, True)
	d\AutoClose = False : d\open = False
	
	d = CreateDoor(r\zone, r\x + 256.0 * RoomScale, r\y, r\z + 744.0 * RoomScale, 90, r, False, False, 2)
	d\AutoClose = False : d\open = False
	
	it = CreateItem("Level 3 Key Card", "key3", r\x + 1119.0 * RoomScale, r\y + 233.0 * RoomScale, r\z + 494.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("First Aid Kit", "firstaid", r\x + 1035.0 * RoomScale, r\y + 145.0 * RoomScale, r\z + 56.0 * RoomScale)
	EntityParent(it\collider, r\obj) : RotateEntity(it\collider, 0, 90, 0)
	
	it = CreateItem("9V Battery", "bat", r\x + 1930.0 * RoomScale, r\y + 97.0 * RoomScale, r\z + 256.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem("9V Battery", "bat", r\x + 1061.0 * RoomScale, r\y + 161.0 * RoomScale, r\z + 494.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("ReVision Eyedrops", "eyedrops", r\x + 1930.0*RoomScale, r\y + 225.0 * RoomScale, r\z + 128.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
	;Player's position after leaving the pocket dimension
	r\Objects[0] = CreatePivot(r\obj)
	PositionEntity r\Objects[0],r\x+1560.0*RoomScale,r\y,r\z+250.0*RoomScale,True
	
	r\Objects[1] = CreatePivot(r\obj)
	PositionEntity r\Objects[1],r\x + 1344.0 * RoomScale, -752.0 * RoomScale,r\z - 384.0 * RoomScale,True
	
	de.Decals = CreateDecal(DECAL_BLOODSPLAT2,  r\x + 1334.0*RoomScale, -796.0*RoomScale+0.01, r\z-220.0*RoomScale,90,Rnd(360),0)
	de\Size = 0.25
	ScaleSprite(de\obj, de\Size,de\Size)
	EntityParent de\obj, r\obj
	
	r\Objects[2] = CreateButton(r\x + 1181.0 *RoomScale, r\y + 180.0 * RoomScale, r\z - 512.0 * RoomScale, 0, 270)
	EntityParent (r\Objects[2],r\obj)
	
End Function

Function CreateEvent_Room2_Elevator_2(e.Events)
	
	e\room\NPC[0]=CreateNPC(NPCtypeGuard, EntityX(e\room\Objects[1],True), EntityY(e\room\Objects[1],True)+0.5, EntityZ(e\room\Objects[1],True))
	RotateEntity e\room\NPC[0]\Collider, 0, e\room\angle+180,0, True
	SetNPCFrame (e\room\NPC[0], 286)
	e\room\NPC[0]\State[0] = 8
	
End Function

Function UpdateEvent_Room2_Elevator_2(e.Events)
	
	If PlayerRoom=e\room Then
		UpdateButton(e\room\Objects[2])
		If d_I\ClosestButton = e\room\Objects[2] And keyhituse Then
			CreateMsg(GetLocalString("Doors", "elevator_broken"))
			PlaySound2(ButtonSFX[1], Camera, e\room\Objects[2])
			keyhituse=0
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D