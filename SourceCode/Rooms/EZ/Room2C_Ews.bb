Function FillRoom_Room2C_Ews(r.Rooms)
	Local it.Items
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x + 64.0 * RoomScale, 0.0, r\z + 368.0 * RoomScale, 180, r, False, False, KEY_CARD_3)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
	
	it = CreateItem("H&K MP5", "mp5",r\x-1399*RoomScale,r\y+1060*RoomScale,r\z-210*RoomScale)
	it\state = 30 : it\state2 = 90
	EntityParent it\collider, r\obj
	
	it = CreateItem("Glock 20-C","glock",r\x-1399*RoomScale,r\y+1060*RoomScale,r\z-210*RoomScale)
	it\state = 15 : it\state2 = 90
	EntityParent it\collider, r\obj
	
	it = CreateItem(GetLocalString("Item Names","match"),"match",r\x-2111*RoomScale,r\y+1060*RoomScale,r\z+48*RoomScale)
	EntityParent it\collider, r\obj
	it = CreateItem(GetLocalString("Item Names","red_dot"),"reddot",r\x-2111*RoomScale,r\y+1060*RoomScale,r\z+48*RoomScale)
	EntityParent it\collider, r\obj
	
	it = CreateItem(GetLocalString("Item Names","eotech"),"eotech",r\x-2058*RoomScale,r\y+1060*RoomScale,r\z+634*RoomScale)
	EntityParent it\collider, r\obj
	
	If gopt\GameMode <> GAMEMODE_NTF Then
		it = CreateItem(GetLocalString("Item Names","ntf_helmet"), "ntf_helmet",r\x-2058*RoomScale,r\y+1060*RoomScale,r\z+634*RoomScale)
		EntityParent it\collider, r\obj
	EndIf
	
	it = CreateItem(GetLocalString("Item Names","ammo"), "ammocrate", r\x-848.0*RoomScale, r\y+900.0*RoomScale, r\z+582.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem(GetLocalString("Item Names","ammo"), "ammocrate", r\x-848.0*RoomScale, r\y+900.0*RoomScale, r\z+582.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","ammo_big"), "bigammocrate", r\x-2111.0*RoomScale, r\y+900.0*RoomScale, r\z+352.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem(GetLocalString("Item Names","ammo"), "ammocrate", r\x-2111.0*RoomScale, r\y+900.0*RoomScale, r\z+352.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem(GetLocalString("Item Names","ammo"), "ammocrate", r\x-2111.0*RoomScale, r\y+900.0*RoomScale, r\z+352.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
End Function

Function UpdateEvent_Room2C_Ews(e.Events)
	
	If PlayerRoom = e\room
		If e\room\RoomDoors[0]\open Then
			GiveAchievement(AchvEWS)
			ecst\WasInEWS = True
		EndIf
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D