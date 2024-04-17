
Function FillRoom_Lockroom_3(r.Rooms)
	Local d.Doors,d2.Doors
	Local scale#
	
	d = CreateDoor(r\zone, r\x - 736.0 * RoomScale, 0, r\z - 104.0 * RoomScale, 0, r, True)
	d\timer = 70 * 5 : d\AutoClose = False : d\open = False : d\locked = True
	
	EntityParent(d\buttons[0], 0)
	PositionEntity(d\buttons[0], r\x - 288.0 * RoomScale, 0.7, r\z - 640.0 * RoomScale)
	EntityParent(d\buttons[0], r\obj)
	
	d\buttons[1] = FreeEntity_Strict(d\buttons[1])
	
	d2 = CreateDoor(r\zone, r\x + 104.0 * RoomScale, 0, r\z + 736.0 * RoomScale, 270, r, True)
	d2\timer = 70 * 5 : d2\AutoClose = False: d2\open = False : d2\locked = True
	EntityParent(d2\buttons[0], 0)
	PositionEntity(d2\buttons[0], r\x + 640.0 * RoomScale, 0.7, r\z + 288.0 * RoomScale)
	RotateEntity (d2\buttons[0], 0, 90, 0)
	EntityParent(d2\buttons[0], r\obj)
	
	d2\buttons[1] = FreeEntity_Strict(d2\buttons[1])
	
	d\LinkedDoor = d2
	d2\LinkedDoor = d
	
	scale# = RoomScale * 4.5 * 0.4
	
	r\Objects[0] = CopyEntity(Monitor)
	ScaleEntity r\Objects[0],scale#,scale#,scale#
	PositionEntity r\Objects[0],r\x+668*RoomScale,1.1,r\z-96.0*RoomScale,True
	RotateEntity r\Objects[0],0,90,0
	EntityParent r\Objects[0],r\obj
	
	r\Objects[1] = CopyEntity(Monitor)
	ScaleEntity r\Objects[1],scale#,scale#,scale#
	PositionEntity r\Objects[1],r\x+96.0*RoomScale,1.1,r\z-668.0*RoomScale,True
	EntityParent r\Objects[1],r\obj
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D