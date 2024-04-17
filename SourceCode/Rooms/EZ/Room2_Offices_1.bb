
Function FillRoom_Room2_Offices_1(r.Rooms)
	Local it.Items,r2.Rooms
	Local firstRoom% = True
	
	For r2 = Each Rooms
		If r2\RoomTemplate\Name = r\RoomTemplate\Name And r2 <> r Then
			firstRoom = False
			Exit
		EndIf
	Next
	
	InitFluLight(0,FLU_STATE_OFF,r)
	InitFluLight(1,FLU_STATE_ON,r)
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x+192*RoomScale,r\y,r\z+160*RoomScale,270,r,False,DOOR_OFFICE)
	r\RoomDoors[2] = CreateDoor(r\zone,r\x-372*RoomScale,r\y,r\z+528*RoomScale,0,r,False,DOOR_OFFICE_2)
	r\RoomDoors[2]\locked = True
	r\RoomDoors[0] = CreateDoor(r\zone,r\x+192*RoomScale,r\y,r\z-160*RoomScale,90,r,False,DOOR_OFFICE)
	
	If firstRoom Then
		r\RoomDoors[0]\locked = False
		r\RoomDoors[1]\locked = False
				CreateScreen(r\x + 385.0 * RoomScale, r\y + 160.0 * RoomScale, r\z - 960.0 * RoomScale, "office.sc", r) ;Manually adding the screen...
;		CreateWaypoint(r\x + 900.0 * RoomScale, r\y + 64.0 * RoomScale, r\z - 200.0 * RoomScale, Null, r) 		;... and waypoints
;		CreateWaypoint(r\x + 900.0 * RoomScale, r\y + 64.0 * RoomScale, r\z - 768.0 * RoomScale, Null, r)
;		CreateWaypoint(r\x + 400.0 * RoomScale, r\y + 64.0 * RoomScale, r\z - 768.0 * RoomScale, Null, r)
		
		;it = CreateItem("Document SCP-106", "paper", r\x + 750.0 * RoomScale, r\y + 150.0 * RoomScale, r\z - 450.0 * RoomScale)
		;EntityParent(it\collider, r\obj)
		
		it = CreateItem(GetLocalString("Item Names","key_2"), "key2", r\x + 750.0 * RoomScale, r\y + 150.0 * RoomScale, r\z - 925.0 * RoomScale)
		EntityParent(it\collider, r\obj)
		
		it = CreateItem(GetLocalString("Item Names","nav_300"), "nav", r\x + 870.0 * RoomScale, r\y + 150.0 * RoomScale, r\z - 50.0 * RoomScale)
		it\state = 25 : EntityParent(it\collider, r\obj)
		
		;it = CreateItem("Notification", "paper", r\x + 550.0 * RoomScale, r\y + 150.0 * RoomScale, r\z - 440.0 * RoomScale)
		;EntityParent(it\collider, r\obj)
		
		InitFluLight(2,FLU_STATE_FLICKER,r)
	Else
		r\RoomDoors[0]\locked = True
		r\RoomDoors[1]\locked = True
		
		InitFluLight(2,FLU_STATE_OFF,r)
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D