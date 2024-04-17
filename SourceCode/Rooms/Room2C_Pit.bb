
Function FillRoom_Room2C_Pit(r.Rooms)
	Local em.Emitters,d.Doors,it.Items
	
	em.Emitters = CreateEmitter(r\x + 512.0 * RoomScale, -76 * RoomScale, r\z - 688 * RoomScale, 0)
	TurnEntity(em\Obj, -90, 0, 0)
	EntityParent(em\Obj, r\obj)
	em\RandAngle = 55
	em\Speed = 0.0005
	em\Achange = -0.015
	em\SizeChange = 0.007
	
	d = CreateDoor(r\zone,r\x-256.0*RoomScale, 0.0, r\z-752.0*RoomScale,90,r,False,2,3)
	d\locked = True : d\open = False : d\AutoClose = False : d\MTFClose = False : d\DisableWaypoint = True
	PositionEntity d\buttons[0],r\x-240.0*RoomScale,EntityY(d\buttons[0],True),EntityZ(d\buttons[0],True),True
	
	it = CreateItem("Dr L's Note", "paper", r\x - 160.0 * RoomScale, 32.0 * RoomScale, r\z - 353.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D