Function FillRoom_Room2C_Office_Ez(r.Rooms)
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x+190*RoomScale,1.0,r\z-822*RoomScale,90,r,False,DOOR_OFFICE,KEY_CARD_2)
	
	InitFluLight(0,FLU_STATE_ON,r)
	InitFluLight(1,FLU_STATE_ON,r)
		
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D