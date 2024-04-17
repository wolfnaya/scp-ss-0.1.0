Function FillRoom_Room2C_Research(r.Rooms)
	Local d.Doors
	
	d = CreateDoor(r\zone,r\x-244.0*RoomScale, 0.0, r\z-736.0*RoomScale,-90, r, False, DOOR_WINDOWED, 3)
	PositionEntity(d\buttons[0], r\x-264.0 * RoomScale, EntityY(d\buttons[1],True), r\z-870.0 * RoomScale, True)
	PositionEntity(d\buttons[1], r\x-224.0 * RoomScale, EntityY(d\buttons[0],True), r\z-870.0 * RoomScale, True)
	
	d = CreateDoor(r\zone,r\x+736.0*RoomScale, 0.0, r\z+244.0*RoomScale,0, r, False, DOOR_WINDOWED, 3)
	PositionEntity(d\buttons[0], r\x+870.0 * RoomScale, EntityY(d\buttons[0],True), r\z+224.0 * RoomScale, True)
	PositionEntity(d\buttons[1], r\x+870.0 * RoomScale, EntityY(d\buttons[1],True), r\z+264.0 * RoomScale, True)
	
	InitFluLight(0,FLU_STATE_OFF,r)
	InitFluLight(1,FLU_STATE_ON,r)
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D