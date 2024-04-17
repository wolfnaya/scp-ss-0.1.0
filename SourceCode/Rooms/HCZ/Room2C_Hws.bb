Function FillRoom_Room2C_Hws(r.Rooms)
	Local it.Items
	
	it = CreateItem("H&K MP5", "mp5", r\x +665.0 * RoomScale, r\y +200.0 * RoomScale, r\z +309.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	it\state2 = 90 : it\state = 30
	
	it = CreateItem("FN P90", "p90", r\x -190.0 * RoomScale, r\y +135.0 * RoomScale, r\z +969.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	it\state2 = 100 : it\state = 50
	
	it = CreateItem("Colt M4A1", "m4a1", r\x -788.0 * RoomScale, r\y +250.0 * RoomScale, r\z -959.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	RotateEntity it\collider,90,0,0
	it\state2 = 60 : it\state = 30
	
	it = CreateItem(GetLocalString("Item Names","acog"), "acog", r\x -569.0 * RoomScale, r\y +125.0 * RoomScale, r\z -371.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("M9 Beretta", "beretta", r\x -871.0 * RoomScale, r\y +135.0 * RoomScale, r\z +805.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	it\state2 = 30 : it\state = 15
	
	it = CreateItem("SCP-127", "scp127", r\x +970.0 * RoomScale, r\y +165.0 * RoomScale, r\z -664.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	it\state = 60
	
	;it = CreateItem(GetLocalString("Item Names","doc_127"), "paper", r\x +714.0 * RoomScale, r\y +55.0 * RoomScale, r\z -630.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","ammo"), "ammocrate", r\x+255.0*RoomScale, r\y+10.0*RoomScale, r\z+786.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem(GetLocalString("Item Names","ammo"), "ammocrate", r\x+255.0*RoomScale, r\y+10.0*RoomScale, r\z+786.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","ammo_big"), "bigammocrate", r\x-918.0*RoomScale, r\y+10.0*RoomScale, r\z-366.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem(GetLocalString("Item Names","ammo"), "ammocrate", r\x-918.0*RoomScale, r\y+10.0*RoomScale, r\z-366.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem(GetLocalString("Item Names","ammo"), "ammocrate", r\x-918.0*RoomScale, r\y+10.0*RoomScale, r\z-366.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x,r\y, r\z-62.0*RoomScale,0,r,False,DOOR_HCZ,KEY_CARD_4)
	r\RoomDoors[0]\dir = 1
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x+339*RoomScale,r\y,r\z-733.0*RoomScale,90,r,False,DOOR_ONE_SIDED,KEY_CARD_4)
	
	r\Objects[0] = CreateButton(r\x-650*RoomScale,r\y+210*RoomScale,r\z+97*RoomScale,0,270,0)
	EntityParent(r\Objects[0],r\obj)
	
End Function

Function UpdateEvent_Room2C_Hws(e.Events)
	
	If PlayerRoom = e\room
		
		If e\room\RoomDoors[0]\open Then
			GiveAchievement(AchvHWS)
			ecst\WasInHWS = True
		EndIf
		If e\room\RoomDoors[1]\open Then
			GiveAchievement(Achv127)
		EndIf
		
		If e\EventState[0] = 0 Then
			UpdateButton(e\room\Objects[0])
			If d_I\ClosestButton = e\room\Objects[0] Then
				If keyhituse Then
					PlaySound_Strict(ButtonSFX[0])
					e\EventState[0] = 1.0
				EndIf
			EndIf
		EndIf
		
		If e\EventState[0] = 1 Then
			e\room\RoomDoors[1]\locked = False
		Else
			e\room\RoomDoors[1]\locked = True
		EndIf
		
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D