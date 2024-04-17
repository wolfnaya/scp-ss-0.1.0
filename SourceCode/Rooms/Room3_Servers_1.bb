
Function FillRoom_Room3_Servers_1(r.Rooms)
	Local it.Items
	Local tex%
	
	it = CreateItem("9V Battery", "bat", r\x - 132.0 * RoomScale, r\y - 368.0 * RoomScale, r\z - 648.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	If Rand(2) = 1 Then
		it = CreateItem("9V Battery", "bat", r\x - 76.0 * RoomScale, r\y - 368.0 * RoomScale, r\z - 648.0 * RoomScale)
		EntityParent(it\collider, r\obj)
	EndIf
	If Rand(2) = 1 Then
		it = CreateItem("9V Battery", "bat", r\x - 196.0 * RoomScale, r\y - 368.0 * RoomScale, r\z - 648.0 * RoomScale)
		EntityParent(it\collider, r\obj)
	EndIf
	
	it = CreateItem("S-NAV 300 Navigator", "nav", r\x + 124.0 * RoomScale, r\y - 368.0 * RoomScale, r\z - 648.0 * RoomScale)
	it\state = 20 : EntityParent(it\collider, r\obj)
	
	r\Objects[0] = CreatePivot(r\obj)
	PositionEntity(r\Objects[0], r\x + 736.0 * RoomScale, -512.0 * RoomScale, r\z - 400.0 * RoomScale, True)
	r\Objects[1] = CreatePivot(r\obj)
	PositionEntity(r\Objects[1], r\x - 552.0 * RoomScale, -512.0 * RoomScale, r\z - 528.0 * RoomScale, True)			
	r\Objects[2] = CreatePivot(r\obj)
	PositionEntity(r\Objects[2], r\x + 736.0 * RoomScale, -512.0 * RoomScale, r\z + 272.0 * RoomScale, True)
	
	r\Objects[3] = LoadMesh_Strict("GFX\npcs\duck_low_res.b3d")
	ScaleEntity(r\Objects[3], 0.07, 0.07, 0.07)
	tex = LoadTexture_Strict("GFX\npcs\duck2.png")
	EntityTexture r\Objects[3], tex
	PositionEntity (r\Objects[3], r\x + 928.0 * RoomScale, -640*RoomScale, r\z + 704.0 * RoomScale)
	
	EntityParent r\Objects[3], r\obj
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D