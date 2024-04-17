Function FillRoom_Room2_Ws(r.Rooms)
	Local it.Items
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x + 264.0 * RoomScale, 0, r\z-640*RoomScale, 270, r, True, False, KEY_CARD_3)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x - 264.0 * RoomScale, 0, r\z+640*RoomScale, 90, r, True, False, KEY_CARD_3)
	r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False
	
	it = CreateItem("FN P90","p90",r\x+716*RoomScale,r\y+160*RoomScale,r\z-96*RoomScale)
	it\state = 50 : it\state2 = 300
	EntityParent it\collider, r\obj
	
	it = CreateItem(GetLocalString("Item Names","vest"),"vest",r\x+716*RoomScale,r\y+160*RoomScale,r\z-96*RoomScale)
	EntityParent it\collider, r\obj
	
	it = CreateItem(GetLocalString("Item Names", "grenade_m67"),"m67",r\x-723*RoomScale,r\y+180*RoomScale,r\z-682*RoomScale)
	EntityParent it\collider, r\obj
	it = CreateItem(GetLocalString("Item Names", "grenade_m67"),"m67",r\x-723*RoomScale,r\y+180*RoomScale,r\z-682*RoomScale)
	EntityParent it\collider, r\obj
	it = CreateItem(GetLocalString("Item Names", "grenade_m67"),"m67",r\x-723*RoomScale,r\y+180*RoomScale,r\z-682*RoomScale)
	EntityParent it\collider, r\obj
	
	it = CreateItem(GetLocalString("Item Names","ammo"), "ammocrate", r\x-778.0*RoomScale, r\y+10.0*RoomScale, r\z+274.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem(GetLocalString("Item Names","ammo"), "ammocrate", r\x-778.0*RoomScale, r\y+10.0*RoomScale, r\z+274.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","ammo_big"), "bigammocrate", r\x+778.0*RoomScale, r\y+10.0*RoomScale, r\z-274.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem(GetLocalString("Item Names","ammo"), "ammocrate", r\x+778.0*RoomScale, r\y+10.0*RoomScale, r\z-274.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem(GetLocalString("Item Names","ammo"), "ammocrate", r\x+778.0*RoomScale, r\y+10.0*RoomScale, r\z-274.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
End Function

Function UpdateEvent_Room2_Ws(e.Events)
	Local it.Items,g.Guns
	
	If PlayerRoom = e\room
		If e\room\RoomDoors[0]\open Lor e\room\RoomDoors[1]\open Then
			GiveAchievement(AchvWS)
			ecst\WasInWS = True
		EndIf
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D