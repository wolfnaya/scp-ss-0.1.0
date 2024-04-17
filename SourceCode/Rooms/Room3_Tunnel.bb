
Function Fillroom_Room3_Tunnel(r.Rooms)
	
	r\Objects[0] = CreatePivot(r\obj)
	PositionEntity (r\Objects[0], r\x - 190.0*RoomScale, 4.0*RoomScale, r\z+190.0*RoomScale, True)
	
End Function

Function UpdateEvent_Room3_Tunnel(e.Events)
	Local it.Items
	
	If e\EventState[0] = 0 Then
		e\room\NPC[0]=CreateNPC(NPCtypeGuard, EntityX(e\room\Objects[0],True), EntityY(e\room\Objects[0],True)+0.5, EntityZ(e\room\Objects[0],True))
		PointEntity e\room\NPC[0]\Collider, e\room\obj
		RotateEntity e\room\NPC[0]\Collider, 0, EntityYaw(e\room\NPC[0]\Collider)+Rnd(-20,20),0, True
		SetNPCFrame (e\room\NPC[0], 288)
		e\room\NPC[0]\State[0] = 8
		RemoveNPCGun(e\room\NPC[0])
		
		it = CreateItem("H&K USP", "usp",EntityX(e\room\Objects[0],True)-0.5,EntityY(e\room\Objects[0],True)+0.5,EntityZ(e\room\Objects[0],True)+0.1)
		RotateEntity it\collider,EntityPitch(it\collider),45,EntityRoll(it\collider),True
		it\state = Rand(6,12)
		it\state2 = 2
		EntityType it\collider, HIT_ITEM
		
		e\EventState[0] = 1
		RemoveEvent(e)
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D