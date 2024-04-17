
Function FillRoom_Room2_GS(r.Rooms)
	Local it.Items
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x + 256.0*RoomScale,r\y,r\z - 512.0*RoomScale,90,r,False,False,3)
	r\RoomDoors[1] = CreateDoor(r\zone,r\x - 256.0*RoomScale,r\y,r\z + 512.0*RoomScale,90,r,False,False,3)
	r\RoomDoors[2] = CreateDoor(r\zone,r\x - 1568.0*RoomScale,r\y,r\z - 512.0*RoomScale,90,r,False,False,4)
	it = CreateItem("FN P90", "p90",r\x + 720.0*RoomScale,r\y + 200.0*RoomScale ,r\z + 532.0*RoomScale)
	it\state2 = 1
	EntityParent(it\collider,r\obj)
	r\Objects[0]=CreatePivot()
	PositionEntity(r\Objects[0], r\x - 600.0*RoomScale, r\y, r\z + 512.0*RoomScale, True)
	EntityParent r\Objects[0], r\obj
	r\Objects[1]=CreatePivot()
	PositionEntity(r\Objects[1], r\x + 600.0*RoomScale, r\y, r\z + 512.0*RoomScale, True)
	EntityParent r\Objects[1], r\obj
	r\Objects[2]=CreatePivot()
	PositionEntity(r\Objects[2], r\x - 540.0*RoomScale, r\y, r\z + 512.0*RoomScale, True)
	EntityParent r\Objects[2], r\obj
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D