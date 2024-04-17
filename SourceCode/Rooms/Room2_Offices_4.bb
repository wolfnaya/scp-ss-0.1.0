
Function FillRoom_Room2_Offices_4(r.Rooms)
	Local d.Doors
	
	d.Doors = CreateDoor(0, r\x - 240.0 * RoomScale, 0.0, r\z, 90, r, False)
	PositionEntity(d\buttons[0], r\x - 230.0 * RoomScale, EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
	PositionEntity(d\buttons[1], r\x - 250.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
	d\open = False : d\AutoClose = False 
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D