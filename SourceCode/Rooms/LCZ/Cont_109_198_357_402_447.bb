Function FillRoom_Cont_109_198_357_402_447(r.Rooms)
	Local d.Doors,it.Items,sc.SecurityCams,de.Decals
	Local i,n.NPCs
	
	r\Objects[0] = CreatePivot()
	PositionEntity r\Objects[0],r\x-854*RoomScale,0.0,r\z+257*RoomScale,True
	EntityParent r\Objects[0], r\obj
	
	d.Doors = CreateDoor(r\zone, r\x + 264.0 * RoomScale, 0, r\z, 90, r, True, False, KEY_CARD_3)
	d\AutoClose = False : d\open = False
	PositionEntity(d\buttons[0], r\x + 320.0 * RoomScale, EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
	PositionEntity(d\buttons[1], r\x + 224.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
	
	d.Doors = CreateDoor(r\zone, r\x - 264.0 * RoomScale, 0, r\z, 270, r, True, False, KEY_CARD_3)
	d\AutoClose = False : d\open = False
	PositionEntity(d\buttons[0], r\x - 320.0 * RoomScale, EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
	PositionEntity(d\buttons[1], r\x - 224.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x+881.0 * RoomScale, 0, r\z, 90, r, False, DOOR_OFFICE)						; ~ SCP-109
	r\RoomDoors[1]\AutoClose = False
	
	r\RoomDoors[2] = CreateDoor(r\zone, r\x + 560.0 * RoomScale, 0, r\z + 272.0 * RoomScale, 0, r, False, DOOR_OFFICE)	; ~ SCP-198
	r\RoomDoors[2]\AutoClose = False
	
	r\RoomDoors[3] = CreateDoor(r\zone, r\x - 560.0 * RoomScale, 0, r\z + 306.0 * RoomScale, 0, r, False, DOOR_OFFICE)	; ~ SCP-357
	r\RoomDoors[3]\AutoClose = False
	
	r\RoomDoors[4] = CreateDoor(r\zone, r\x-560.0 * RoomScale, 0, r\z - 240.0 * RoomScale, 0, r, False, DOOR_OFFICE)	; ~ SCP-402
	r\RoomDoors[4]\AutoClose = False
	
	r\RoomDoors[5] = CreateDoor(r\zone, r\x+560.0 * RoomScale, 0, r\z - 274.0 * RoomScale, 0, r, False, DOOR_OFFICE)	; ~ SCP-447
	r\RoomDoors[5]\AutoClose = False
	
	sc.SecurityCams = CreateSecurityCam(r\x + 560.0 * RoomScale, r\y + 386 * RoomScale, r\z - 416.0 * RoomScale, r)
	sc\angle = 180 : sc\turn = 30
	TurnEntity(sc\CameraObj, 30, 0, 0)	
	EntityParent(sc\obj, r\obj)
	
	sc.SecurityCams = CreateSecurityCam(r\x - 560.0 * RoomScale, r\y + 386 * RoomScale, r\z + 480.0 * RoomScale, r)
	sc\angle = 0 : sc\turn = 30
	TurnEntity(sc\CameraObj, 30, 0, 0)
	EntityParent(sc\obj, r\obj)
	
	it.Items = CreateItem("SCP-109", "scp109", r\x + 1231.0 * RoomScale, r\y + 185.0 * RoomScale, r\z)
	RotateEntity(it\collider, 0, 180, 0)
	EntityParent(it\collider, r\obj)
	
	it.Items = CreateItem("SCP-198", "scp198", r\x + 811.0 * RoomScale, r\y + 185.0 * RoomScale, r\z + 913.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	;it.Items = CreateItem("SCP-357", "scp357", r\x - 559.0 * RoomScale, r\y + 168.0 * RoomScale, r\z + 790.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	;it.Items = CreateItem("SCP-402", "scp402", r\x -633.0 * RoomScale, r\y + 185.0 * RoomScale, r\z -887.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	;it.Items = CreateItem("SCP-447", "scp447", r\x +566.0 * RoomScale, r\y + 226.0 * RoomScale, r\z -755.0 * RoomScale)
	;EntityParent(it\Collider, r\obj)
	
	;it.Items = CreateItem(GetLocalString("Item Names","doc_447"), "paper", r\x + 754.0 * RoomScale, r\y + 10.0 * RoomScale, r\z -677.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	;it.Items = CreateItem(GetLocalString("Item Names","doc_109"), "paper", r\x +1209.0 * RoomScale, r\y + 10.0 * RoomScale, r\z - 158.0 * RoomScale)
	;RotateEntity(it\collider, 0.0, r\angle, 0.0)
	;EntityParent(it\collider, r\obj)
	
	;it.Items = CreateItem(GetLocalString("Item Names","doc_198"), "paper", r\x + 1245.0 * RoomScale, r\y + 160.0 * RoomScale, r\z + 919.0 * RoomScale)
	;RotateEntity(it\collider, 0.0, r\angle, 0.0)
	;EntityParent(it\collider, r\obj)
	
	;it.Items = CreateItem(GetLocalString("Item Names","doc_357"), "paper", r\x -416.0 * RoomScale, r\y + 10.0 * RoomScale, r\z + 721.0 * RoomScale)
	;RotateEntity(it\collider, 0.0, r\angle, 0.0)
	;EntityParent(it\collider, r\obj)
	
	;it.Items = CreateItem(GetLocalString("Item Names","doc_402"), "paper", r\x - 445.0 * RoomScale, r\y + 105.0 * RoomScale, r\z - 902.0 * RoomScale)
	;RotateEntity(it\collider, 0.0, r\angle, 0.0)
	;EntityParent(it\collider, r\obj)
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D