
Function FillRoom_Room3_HCZ(r.Rooms)
	Local d.Doors
	
	d = CreateDoor(r\zone, r\x,r\y, r\z+286*RoomScale, 0, r, False, DOOR_DEFAULT)
	d\AutoClose = False : d\open = False
	
	d = CreateDoor(r\zone, r\x-288*RoomScale,r\y, r\z-736*RoomScale, 90, r, False, False, DOOR_DEFAULT, "NULL")
	d\AutoClose = False : d\open = False : d\locked = True
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D