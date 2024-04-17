Function FillRoom_Room2_Generators_LCZ(r.Rooms)
	Local r2.Rooms
	Local firstRoom% = True
	
	For r2 = Each Rooms
		If r2\RoomTemplate\Name = r\RoomTemplate\Name And r2 <> r Then
			firstRoom = False
			Exit
		EndIf
	Next
	
	r\Objects[1] = CreatePivot()
	PositionEntity r\Objects[1],r\x+7*RoomScale,0.5,r\y-980*RoomScale,True
	EntityParent r\Objects[1], r\obj
	
	r\Objects[2] = CreatePivot()
	PositionEntity r\Objects[2],r\x-7*RoomScale,0.5,r\y+980*RoomScale,True
	EntityParent r\Objects[2], r\obj
	
	If firstRoom Then
		r\Objects[0] = LoadRMesh("GFX\map\rooms\room2_generators_lcz\room2_generators_lcz_obj_2.rmesh",Null)
		
		ScaleEntity (r\Objects[0],RoomScale,RoomScale,RoomScale)
		EntityType GetChild(r\Objects[0],2), HIT_MAP
		EntityPickMode GetChild(r\Objects[0],2), 2
		PositionEntity(r\Objects[0],r\x,r\y,r\z,True)
		EntityParent(r\Objects[0], r\obj)
	Else
		r\Objects[0] = LoadRMesh("GFX\map\rooms\room2_generators_lcz\room2_generators_lcz_obj_1.rmesh",Null)
		
		ScaleEntity (r\Objects[0],RoomScale,RoomScale,RoomScale)
		EntityType GetChild(r\Objects[0],2), HIT_MAP
		EntityPickMode GetChild(r\Objects[0],2), 2
		PositionEntity(r\Objects[0],r\x,r\y,r\z,True)
		EntityParent(r\Objects[0], r\obj)
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D