
Function FillRoom_Room2_Upper_Office(r.Rooms)
	Local d.Doors,it.Items
	
	d = CreateDoor(r\zone, r\x + 1440.0 * RoomScale, 224.0 * RoomScale, r\z + 32.0 * RoomScale, 90, r, False, False, 4)
	d\AutoClose = False : d\open = False
	
	it = CreateItem("Some SCP-420-J", "420", r\x + 1776.0 * RoomScale, r\y + 400.0 * RoomScale, r\z + 427.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Some SCP-420-J", "420", r\x + 1808.0 * RoomScale, r\y + 400.0 * RoomScale, r\z + 435.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Level 5 Key Card", "key5", r\x + 2232.0 * RoomScale, r\y + 392.0 * RoomScale, r\z + 387.0 * RoomScale)
	RotateEntity it\collider, 0, r\angle, 0, True
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Nuclear Device Document", "paper", r\x + 2248.0 * RoomScale, r\y + 440.0 * RoomScale, r\z + 372.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Radio Transceiver", "radio", r\x + 2240.0 * RoomScale, r\y + 320.0 * RoomScale, r\z + 128.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D