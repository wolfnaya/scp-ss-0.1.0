
Function FillRoom_Room3_Window(r.Rooms)
	Local d.Doors
	
	d = CreateDoor(r\zone,r\x+416.0*RoomScale, 0.0, r\z+256.0*RoomScale,0,r,False,False,False,"NOPE")
	d\locked = True : d\open = False : d\AutoClose = False : d\MTFClose = False : d\DisableWaypoint = True
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D