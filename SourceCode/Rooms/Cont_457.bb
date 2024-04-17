
Function FillRoom_Cont_457(r.Rooms)
	Local d.Doors
	Local i
	
	;WIP
	;Some doors just for decoration
	d.Doors = CreateDoor(r\zone,r\x,r\y-7680.0*RoomScale,r\z,0,r,False,2,0,"ABCD")
	d\locked = True
	d\DisableWaypoint = True
	CreateDoor(r\zone,r\x-2688.0*RoomScale,r\y-7264.0*RoomScale,r\z-1248.0*RoomScale,0,r,False,2)
	CreateDoor(r\zone,r\x-3872.0*RoomScale,r\y-7264.0*RoomScale,r\z-1920.0*RoomScale,90,r,False,0,3)
	d.Doors = CreateDoor(r\zone,r\x-4704.0*RoomScale,r\y-7264.0*RoomScale,r\z-1920.0*RoomScale,90,r)
	d\locked = True
	d\DisableWaypoint = True
	d.Doors = CreateDoor(r\zone,r\x-4704.0*RoomScale,r\y-7264.0*RoomScale,r\z-2688.0*RoomScale,90,r)
	d\locked = True
	d\DisableWaypoint = True
	d.Doors = CreateDoor(r\zone,r\x-384.0*RoomScale,r\y-7264.0*RoomScale,r\z-1248.0*RoomScale,0,r)
	d\locked = True
	d\DisableWaypoint = True
	d.Doors = CreateDoor(r\zone,r\x-2688.0*RoomScale,r\y-7264.0*RoomScale,r\z-3360.0*RoomScale,0,r,False,2)
	d\locked = True
	d\DisableWaypoint = True
	CreateDoor(r\zone,r\x-384.0*RoomScale,r\y-7264.0*RoomScale,r\z-3360.0*RoomScale,0,r,False,2)
	CreateDoor(r\zone,r\x-384.0*RoomScale,r\y-7264.0*RoomScale,r\z-5152.0*RoomScale,0,r,False,2)
	d.Doors = CreateDoor(r\zone,r\x-2064.0*RoomScale,r\y-7680.0*RoomScale,r\z-4224.0*RoomScale,90,r,False,2)
	d\locked = True
	For i = 0 To 1
		d\buttons[i] = FreeEntity_Strict(d\buttons[i])
	Next
	d\DisableWaypoint = True
	d.Doors = CreateDoor(r\zone,r\x-4224.0*RoomScale,r\y-7680.0*RoomScale,r\z-5152.0*RoomScale,0,r,False,2)
	d\locked = True
	d\DisableWaypoint = True
	d.Doors = CreateDoor(r\zone,r\x-4640.0*RoomScale,r\y-7680.0*RoomScale,r\z-4736.0*RoomScale,90,r,False,2)
	d\locked = True
	d\DisableWaypoint = True
	d.Doors = CreateDoor(r\zone,r\x-4224.0*RoomScale,r\y-7680.0*RoomScale,r\z-2528.0*RoomScale,0,r,False,2)
	d\locked = True
	d\DisableWaypoint = True
	CreateDoor(r\zone,r\x-4640.0*RoomScale,r\y-7680.0*RoomScale,r\z-2944.0*RoomScale,90,r,False,2)
	d.Doors = CreateDoor(r\zone,r\x-5920.0*RoomScale,r\y-7680.0*RoomScale,r\z-3200.0*RoomScale,90,r,False,2)
	d\locked = True
	d\DisableWaypoint = True
	CreateDoor(r\zone,r\x-5504.0*RoomScale,r\y-7680.0*RoomScale,r\z-2272.0*RoomScale,0,r)
	d.Doors = CreateDoor(r\zone,r\x-4848.0*RoomScale,r\y-7680.0*RoomScale,r\z-1920.0*RoomScale,90,r,False,2)
	d\locked = True
	For i = 0 To 1
		d\buttons[i] = FreeEntity_Strict(d\buttons[i])
	Next
	d\DisableWaypoint = True
	CreateDoor(r\zone,r\x-5632.0*RoomScale,r\y-7680.0*RoomScale,r\z+544.0*RoomScale,0,r)
	d.Doors = CreateDoor(r\zone,r\x-5904.0*RoomScale,r\y-7680.0*RoomScale,r\z+256.0*RoomScale,90,r,False,2)
	d\locked = True
	For i = 0 To 1
		d\buttons[i] = FreeEntity_Strict(d\buttons[i])
	Next
	d\DisableWaypoint = True
	d.Doors = CreateDoor(r\zone,r\x-6176.0*RoomScale,r\y-7680.0*RoomScale,r\z+896.0*RoomScale,90,r,False,2)
	d\locked = True
	d\DisableWaypoint = True
	CreateDoor(r\zone,r\x-3072.0*RoomScale,r\y-7680.0*RoomScale,r\z+896.0*RoomScale,90,r,False,2)
	d.Doors = CreateDoor(r\zone,r\x-2688.0*RoomScale,r\y-7680.0*RoomScale,r\z+1280.0*RoomScale,0,r,False,2)
	d\locked = True
	d\DisableWaypoint = True
	d.Doors = CreateDoor(r\zone,r\x-2688.0*RoomScale,r\y-7680.0*RoomScale,r\z+512.0*RoomScale,0,r,False,2)
	d\locked = True
	d\DisableWaypoint = True
	CreateDoor(r\zone,r\x-2304.0*RoomScale,r\y-7680.0*RoomScale,r\z+896.0*RoomScale,90,r,False,2)
	d.Doors = CreateDoor(r\zone,r\x,r\y-7680.0*RoomScale,r\z+1824.0*RoomScale,0,r,True,0)
	d\locked = True
	d\DisableWaypoint = True
	d.Doors = CreateDoor(r\zone,r\x+896.0*RoomScale,r\y-7680.0*RoomScale,r\z+1808.0*RoomScale,0,r,False,2)
	d\locked = True
	For i = 0 To 1
		d\buttons[i] = FreeEntity_Strict(d\buttons[i])
	Next
	d\DisableWaypoint = True
	d.Doors = CreateDoor(r\zone,r\x+1504.0*RoomScale,r\y-7680.0*RoomScale,r\z+640.0*RoomScale,90,r)
	d\locked = True
	d\DisableWaypoint = True
	CreateDoor(r\zone,r\x+2080.0*RoomScale,r\y-7680.0*RoomScale,r\z+640.0*RoomScale,90,r)
	d.Doors = CreateDoor(r\zone,r\x+1664.0*RoomScale,r\y-7680.0*RoomScale,r\z-2320.0*RoomScale,0,r,False,2)
	d\locked = True
	For i = 0 To 1
		d\buttons[i] = FreeEntity_Strict(d\buttons[i])
	Next
	d\DisableWaypoint = True
	
	;Big door
	r\RoomDoors[0] = CreateDoor(r\zone,r\x+352.0*RoomScale,r\y-7680.0*RoomScale,r\z-1792.0*RoomScale,90,r,False,1)
	r\RoomDoors[0]\locked = True
	For i = 0 To 1
		r\RoomDoors[0]\buttons[i] = FreeEntity_Strict(r\RoomDoors[0]\buttons[i])
	Next
	
	;Doors leading to catwalk (both sides)
	r\RoomDoors[1] = CreateDoor(r\zone,r\x-1536.0*RoomScale,r\y-7680.0*RoomScale,r\z-1248.0*RoomScale,0,r,False,2,3)
	r\RoomDoors[1]\locked = True
	r\RoomDoors[2] = CreateDoor(r\zone,r\x-1536.0*RoomScale,r\y-7680.0*RoomScale,r\z-5152.0*RoomScale,0,r,False,2,3)
	r\RoomDoors[2]\locked = True
	
End Function

Function UpdateEvent_Cont_457(e.Events)
	Local r.Rooms
	
	If PlayerRoom = e\room
		If EntityY(Collider)<-1500.0*RoomScale
			For r.Rooms = Each Rooms
				HideEntity r\obj
			Next
			ShowEntity e\room\obj
			CameraFogRange Camera,1,5
			CameraRange Camera,0.01,6
			ShouldPlay = 30
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#69
;~C#Blitz3D