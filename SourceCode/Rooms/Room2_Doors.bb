
Function FillRoom_Room2_Doors(r.Rooms)
	Local d.Doors,d2.Doors
	
	d = CreateDoor(r\zone, r\x, 0, r\z + 528.0 * RoomScale, 0, r, True)
	d\AutoClose = False
	PositionEntity (d\buttons[0], r\x - 832.0 * RoomScale, 0.7, r\z + 160.0 * RoomScale, True)
	PositionEntity (d\buttons[1], r\x + 160.0 * RoomScale, 0.7, r\z + 536.0 * RoomScale, True)
	
	d2 = CreateDoor(r\zone, r\x, 0, r\z - 528.0 * RoomScale, 180, r, True)
	d2\AutoClose = False
	d2\buttons[0] = FreeEntity_Strict(d2\buttons[0])
	PositionEntity (d2\buttons[1], r\x +160.0 * RoomScale, 0.7, r\z - 536.0 * RoomScale, True)
	
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x - 832.0 * RoomScale, 0.5, r\z)
	EntityParent(r\Objects[0], r\obj)
	
	d2\LinkedDoor = d : d\LinkedDoor = d2
	
	d\open = False : d2\open = True
	
End Function

Function UpdateEvent_Room2_Doors(e.Events)
	
	If PlayerRoom = e\room Then
		If e\EventState[0] = 0 And Curr173 <> Null And Curr173\Idle = SCP173_ACTIVE Then
			If (Not EntityInView(Curr173\obj, Camera)) Then
				e\EventState[0] = 1
				PositionEntity(Curr173\Collider, EntityX(e\room\Objects[0], True), 0.5, EntityZ(e\room\Objects[0], True))
				ResetEntity(Curr173\Collider)
				RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D