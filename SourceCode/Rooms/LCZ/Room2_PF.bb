Function FillRoom_Room2_PF(r.Rooms)
	Local it.Items
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x+273.0*RoomScale,0,r\z+576.0*RoomScale,90,r,False,False,KEY_CARD_0)
	PositionEntity (r\RoomDoors[0]\buttons[1], r\x + 228.0 * RoomScale,  EntityY(r\RoomDoors[0]\buttons[1],True), r\z +432.0 * RoomScale, True)
	PositionEntity (r\RoomDoors[0]\buttons[0], r\x + 315.0 * RoomScale,  EntityY(r\RoomDoors[0]\buttons[0],True), r\z +713.0 * RoomScale, True)
	
	it.Items = CreateItem(GetLocalString("Item Names", "key_1"), "key1", r\x + 640 * RoomScale, r\y + 66.0 * RoomScale, r\z + 508.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	;461.27596 199 916
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D