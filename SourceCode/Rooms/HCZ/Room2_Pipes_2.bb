
Function FillRoom_Room2_Pipes_2(r.Rooms)
	Local d.Doors,it.Items,r2.Rooms
	Local firstRoom% = True
	
	For r2 = Each Rooms
		If r2\RoomTemplate\Name = r\RoomTemplate\Name And r2 <> r Then
			firstRoom = False
			Exit
		EndIf
	Next
	
	If firstRoom Then
		d = CreateDoor(r\zone, r\x,r\y, r\z, 0, r, False, DOOR_DEFAULT, KEY_CARD_2)
		d\AutoClose = False : d\open = False : d\locked = False
	Else
		d = CreateDoor(r\zone, r\x,r\y, r\z, 0, r, False, DOOR_DEFAULT, KEY_CARD_2)
		d\AutoClose = False : d\open = False : d\locked = True
	EndIf
	
	d = CreateDoor(r\zone, r\x-250*RoomScale,r\y, r\z+288*RoomScale, 90, r, False, DOOR_DEFAULT, -1, "NULL")
	d\AutoClose = False : d\open = False : d\locked = True
	
	d = CreateDoor(r\zone, r\x+256*RoomScale,r\y, r\z+288*RoomScale, 90, r, False, DOOR_HCZ)
	d\AutoClose = False : d\open = False
	
	d = CreateDoor(r\zone, r\x+256*RoomScale,r\y, r\z-288*RoomScale, 90, r, False, DOOR_HCZ)
	d\AutoClose = False : d\open = False
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D TSS