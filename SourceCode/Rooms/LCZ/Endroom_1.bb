
Function FillRoom_Endroom_1(r.Rooms)
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x, 0, r\z + 1136 * RoomScale, 0, r, False, True, KEY_CARD_OMNI)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
	FreeEntity r\RoomDoors[0]\buttons[0] : r\RoomDoors[0]\buttons[0]=0
	FreeEntity r\RoomDoors[0]\buttons[1] : r\RoomDoors[0]\buttons[1]=0
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D