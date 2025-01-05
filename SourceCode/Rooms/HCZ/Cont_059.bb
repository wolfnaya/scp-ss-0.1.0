
Function FillRoom_Cont_059(r.Rooms)
	Local d.Doors,d2.Doors,em.Emitters,it.Items
	Local i,n
	
	d = CreateDoor(r\zone, r\x, 0.0, r\z +896.0 * RoomScale, 0, r, False, 2,KEY_CARD_3)
	d\AutoClose = False : d\MTFClose = False : d\DisableWaypoint = True
	
	d = CreateDoor(r\zone, r\x, 0.0, r\z +416.0 * RoomScale, 0, r, True, 2,KEY_CARD_3)
	d\AutoClose = False : d\MTFClose = False : d\DisableWaypoint = True
	
	;fake doors
	d = CreateDoor(r\zone, r\x +704.0 * RoomScale, -672.0* RoomScale, r\z +608.0 * RoomScale, 90, r, False, False, KEY_CARD_2)
	d\locked = True : d\AutoClose = False : d\MTFClose = False 
	
	d = CreateDoor(r\zone, r\x +96.0 * RoomScale, -672.0* RoomScale, r\z +1184.0 * RoomScale, 0, r, False, False, KEY_CARD_2)
	d\locked = True : d\AutoClose = False : d\MTFClose = False 
	
	;main door.
	r\RoomDoors[0] = CreateDoor(r\zone, r\x +448.0 * RoomScale, -672.0* RoomScale, r\z +384.0 * RoomScale, 0, r, False, DOOR_ONE_SIDED, KEY_CARD_3)
	r\RoomDoors[0]\dir = 1 : r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\MTFClose = False 
	
	it = CreateItem(GetLocalString("Item Names","key_3"), "key3", r\x -96.0 * RoomScale, r\y-656 * RoomScale, r\z-160.0* RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","severed_hand_black"), "hand2", r\x -96.0 * RoomScale, r\y-656 * RoomScale, r\z-160.0* RoomScale)
	EntityParent(it\collider, r\obj)
	
	;it = CreateItem("SCP-059 Sample", "scp059", r\x -32.0 * RoomScale, r\y-656 * RoomScale, r\z-650.0* RoomScale)
	;EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","radio"), "radio", r\x -192.0 * RoomScale, -656.0* RoomScale, r\z+96.0* RoomScale, True)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("SCP-500-01", "scp500pill", r\x -192.0 * RoomScale, -656.0* RoomScale, r\z+96.0* RoomScale)
	EntityParent(it\collider, r\obj)
	
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D TSS