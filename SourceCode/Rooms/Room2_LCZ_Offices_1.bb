Function FillRoom_Room2_LCZ_Offices_1(r.Rooms)
	Local d.Doors
	
	d.Doors = CreateDoor(r\zone, r\x + 288.0 * RoomScale, r\y + 384.0 * RoomScale, r\z - 656.0 * RoomScale, 90.0, r, False, DOOR_DEFAULT, 3)
	
	InitFluLight(0,FLU_STATE_OFF,r)
	InitFluLight(1,FLU_STATE_FLICKER,r)
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D