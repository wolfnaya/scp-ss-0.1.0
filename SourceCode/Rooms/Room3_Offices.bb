
Function FillRoom_Room3_Offices(r.Rooms)
	Local d.Doors
	
	d.Doors = CreateDoor(r\zone, r\x + 736.0 * RoomScale, 0.0, r\z + 240.0 * RoomScale, 0, r, False, DOOR_WINDOWED, 3)
	PositionEntity(d\buttons[0], r\x + 892.0 * RoomScale, EntityY(d\buttons[0],True), r\z + 224.0 * RoomScale, True)
	PositionEntity(d\buttons[1], r\x + 892.0 * RoomScale, EntityY(d\buttons[1],True), r\z + 255.0 * RoomScale, True)
	
	r\Objects[0] = LoadMesh_Strict("GFX\map\rooms\room3_offices\room3offices_hb.b3d",r\obj)
	EntityPickMode r\Objects[0],2
	EntityType r\Objects[0],HIT_MAP
	EntityAlpha r\Objects[0],0.0
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D