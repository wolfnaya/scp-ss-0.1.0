Function FillRoom_Room2_Rocky(r.Rooms)
	
	r\Objects[0]=LoadMesh_Strict("gfx\map\rooms\room2_rocky\rocks_mesh.b3d")
	PositionEntity r\Objects[0],r\x,r\y,r\z
	ScaleEntity r\Objects[0],RoomScale,RoomScale,RoomScale
	EntityParent r\Objects[0],r\obj
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D