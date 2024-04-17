
Function FillRoom_Cont_005(r.Rooms)
	Local sc.SecurityCams,it.Items
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x, r\y, r\z - 640.0 * RoomScale, 0.0, r, False, DOOR_DEFAULT, KEY_CARD_4)
	PositionEntity r\RoomDoors[0]\buttons[0], r\x+380*RoomScale,r\y+200*RoomScale,r\z-828*RoomScale,True
	RotateEntity r\RoomDoors[0]\buttons[0],0,270,0
	
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x, r\y + 76.0 * RoomScale, r\z - 210.0 * RoomScale)
	EntityParent(r\Objects[0], r\obj)
	
	r\Objects[1] = CreatePivot()
	PositionEntity(r\Objects[1], r\x, r\y + 188.0 * RoomScale, r\z + 185.0 * RoomScale)			
	EntityParent(r\Objects[1], r\obj)
	
	r\Objects[2] = CreatePivot()
	PositionEntity(r\Objects[2], r\x, r\y + 12.0 * RoomScale, r\z + 240.0 * RoomScale)
	EntityParent(r\Objects[2], r\obj)
	
	sc.SecurityCams = CreateSecurityCam(r\x, r\y + 415.0 * RoomScale, r\z - 556.0 * RoomScale, r)
	sc\angle = 0.0 : sc\turn = 30.0
	TurnEntity(sc\CameraObj, 30.0, 0.0, 0.0)
	
	;it.Items = CreateItem(GetLocalString("Item Names","doc_005"), "paper", r\x + 338.0 * RoomScale, r\y + 152.0 * RoomScale, r\z - 500.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	If I_005\ChanceToSpawn = 1 Then
		it.Items = CreateItem("SCP-005", "scp005", r\x, r\y + 255.0 * RoomScale, r\z - 210.0 * RoomScale)
		EntityParent(it\collider, r\obj)
	ElseIf I_005\ChanceToSpawn = 2
		it.Items = CreateItem("Note from Maynard", "paper", r\x, r\y + 255.0 * RoomScale, r\z - 210.0 * RoomScale)
		EntityParent(it\collider, r\obj)	
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D