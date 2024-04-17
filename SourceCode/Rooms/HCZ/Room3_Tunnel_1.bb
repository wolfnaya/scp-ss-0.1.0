
Function Fillroom_Room3_Tunnel_1(r.Rooms)
	Local d.Doors
	
	r\Objects[0] = CreatePivot(r\obj)
	PositionEntity (r\Objects[0], r\x +7.0*RoomScale, 4.0*RoomScale, r\z+511.0*RoomScale, True)
	
	d = CreateDoor(r\zone, r\x,r\y, r\z+286*RoomScale, 180, r, False, DOOR_ONE_SIDED)
	d\AutoClose = False : d\open = False
	
End Function

Function UpdateEvent_Room3_Tunnel_1(e.Events)
	Local it.Items
	
	If e\EventState[0] = 0 Then
		e\room\NPC[0]=CreateNPC(NPC_Guard, EntityX(e\room\Objects[0],True), EntityY(e\room\Objects[0],True)+0.5, EntityZ(e\room\Objects[0],True))
		PointEntity e\room\NPC[0]\Collider, e\room\obj
		RotateEntity e\room\NPC[0]\Collider, 0, EntityYaw(e\room\NPC[0]\Collider)+Rnd(-20,20),0, True
		SetNPCFrame (e\room\NPC[0], 288)
		e\room\NPC[0]\State[0] = 8
		;RemoveNPCGun(e\room\NPC[0])
		
		Local bone% = FindChild(e\room\NPC[0]\obj,"Hand.L")
		it = CreateItem("M9 beretta", "beretta",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
		RotateEntity it\collider,EntityPitch(it\collider,True),e\room\angle+45,EntityRoll(it\collider,True),True
		MoveEntity it\model, 0,-0.025,0
		it\state = Rand(7,16)
		it\state2 = 45
		EntityType it\collider, HIT_ITEM
		
		e\EventState[0] = 1
		RemoveEvent(e)
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D