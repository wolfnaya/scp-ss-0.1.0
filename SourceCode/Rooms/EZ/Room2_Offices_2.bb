
Function FillRoom_Room2_Offices_2(r.Rooms)
	Local it.Items
	
	it = CreateItem(GetLocalString("Item Names","key_1"), "key1", r\x + 603.0 * RoomScale, r\y + 184.0 * RoomScale, r\z -959.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	;it = CreateItem("Document SCP-895", "paper", r\x + 602.0 * RoomScale, r\y + 178.0 * RoomScale, r\z + 958.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	;If Rand(2) = 1 Then
	;	it = CreateItem("Document SCP-860", "paper", r\x +296.0 * RoomScale, r\y + 166.0 * RoomScale, r\z + 697.0 * RoomScale)
	;Else
	;	it = CreateItem("SCP-093 Recovered Materials", "paper", r\x +296.0 * RoomScale, r\y + 166.0 * RoomScale, r\z + 697.0 * RoomScale)
	;EndIf
	;EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","nav_300"), "nav", r\x + 516.0 * RoomScale, r\y + 184.0 * RoomScale, r\z -959.0 * RoomScale)
	it\state = 28 : EntityParent(it\collider, r\obj)		
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x+232*RoomScale,r\y,r\z,90,r,False,DOOR_OFFICE)
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D