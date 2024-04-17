Function FillRoom_Room2_Lws(r.Rooms)
	Local it.Items,g.Guns
	
	it = CreateItem("H&K USP", "usp", r\x -1006.0 * RoomScale, r\y +155.0 * RoomScale, r\z -49.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	it\state2 = 36 : it\state = 12
	
	it = CreateItem("M9 Beretta", "beretta", r\x +985.0 * RoomScale, r\y +140.0 * RoomScale, r\z +49.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	it\state2 = 60 : it\state = 15
	
	it = CreateItem(GetLocalString("Item Names","red_dot"), "reddot", r\x -619.0 * RoomScale, r\y +140.0 * RoomScale, r\z +427.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","suppressor"), "suppressor", r\x -617.0 * RoomScale, r\y +210.0 * RoomScale, r\z +432.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","match"), "match", r\x -583.0 * RoomScale, r\y +126.0 * RoomScale, r\z -411.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","scope_bat"), "scopebat", r\x +866.0 * RoomScale, r\y +140.0 * RoomScale, r\z +405.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x + 264.0 * RoomScale, 0, r\z, 90, r, True, False, KEY_CARD_3)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
	PositionEntity(r\RoomDoors[0]\buttons[0], r\x + 320.0 * RoomScale, EntityY(r\RoomDoors[0]\buttons[0],True), EntityZ(r\RoomDoors[0]\buttons[0],True), True)
	PositionEntity(r\RoomDoors[0]\buttons[1], r\x + 224.0 * RoomScale, EntityY(r\RoomDoors[0]\buttons[1],True), EntityZ(r\RoomDoors[0]\buttons[1],True), True)
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x - 264.0 * RoomScale, 0, r\z, 270, r, True, False, KEY_CARD_3)
	r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False
	PositionEntity(r\RoomDoors[1]\buttons[0], r\x - 320.0 * RoomScale, EntityY(r\RoomDoors[1]\buttons[0],True), EntityZ(r\RoomDoors[1]\buttons[0],True), True)
	PositionEntity(r\RoomDoors[1]\buttons[1], r\x - 224.0 * RoomScale, EntityY(r\RoomDoors[1]\buttons[1],True), EntityZ(r\RoomDoors[1]\buttons[1],True), True)
	
End Function

Function UpdateEvent_Room2_Lws(e.Events)
	
	If PlayerRoom = e\room
		If e\room\RoomDoors[0]\open Lor e\room\RoomDoors[1]\open Then
			GiveAchievement(AchvLWS)
			ecst\WasInLWS = True
		EndIf
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D