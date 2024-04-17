
Function FillRoom_Room2_4(r.Rooms)
	
	r\Objects[1] = CreatePivot()
	PositionEntity(r\Objects[1], r\x + 640.0 * RoomScale, 8.0 * RoomScale, r\z - 896.0 * RoomScale)
	EntityParent(r\Objects[1], r\obj)
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D