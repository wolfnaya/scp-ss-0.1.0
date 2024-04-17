
Function FillRoom_Room2_Offices_5(r.Rooms)
	Local fll.FluLight,it.Items
	
	InitFluLight(0,FLU_STATE_OFF,r)
	InitFluLight(1,FLU_STATE_ON,r)
	InitFluLight(2,FLU_STATE_FLICKER,r)
	
	r\Objects[0] = LoadMesh_Strict("GFX\map\rooms\room2_offices_5\room2_offices_5_projector_wall.b3d",r\obj)
	EntityPickMode(r\Objects[0],2)
	
	If I_005\ChanceToSpawn = 2 Then
		it.Items = CreateItem("SCP-005", "scp005", r\x +684.0 * RoomScale, r\y +183.0 * RoomScale, r\z + 507.0 * RoomScale)
		EntityParent(it\collider, r\obj)
	EndIf
	
	;it = CreateItem("Mysterious Note", "paper", r\x + 856.0 * RoomScale, r\y + 130.0 * RoomScale, r\z +433.0 * RoomScale)
	;EntityParent(it\collider, r\obj)	
	it = CreateItem(GetLocalString("Item Names","vest"), "vest", r\x + 306 * RoomScale, r\y + 130.0 * RoomScale, r\z +739.0 * RoomScale)
	EntityParent(it\collider, r\obj) : RotateEntity(it\collider, 0, 90, 0)
	
	;it = CreateItem("Incident Report SCP-106-0204", "paper", r\x + 695.0 * RoomScale, r\y + 130.0 * RoomScale, r\z - 584.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	;it = CreateItem("Journal Page", "paper", r\x + 300.0 * RoomScale, r\y + 130.0 * RoomScale, r\z -665.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	it = CreateItem(GetLocalString("Item Names","first_aid"), "firstaid", r\x + 630.0 * RoomScale, r\y + 140.0 * RoomScale, r\z + 48.0 * RoomScale)
	EntityParent(it\collider, r\obj) : RotateEntity(it\collider, 0, 90, 0)
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x + 240.0 * RoomScale, 0.0, r\z-128*RoomScale, 90, r, False,DOOR_OFFICE,False,"7816")
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x + 240.0 * RoomScale, 0.0, r\z+128*RoomScale, 90, r, False,DOOR_OFFICE,False,AccessCode[0])
	PositionEntity r\RoomDoors[1]\buttons[1], r\x+224*RoomScale,r\y+180*RoomScale,r\z,True
	
	r\RoomDoors[2] = CreateDoor(r\zone, r\x - 544.0 * RoomScale, 0.0, r\z+272*RoomScale, 0, r, False,DOOR_OFFICE)
	
	r\RoomDoors[3] = CreateDoor(r\zone, r\x - 768.0 * RoomScale, 0.0, r\z-272*RoomScale, 0, r, False,DOOR_OFFICE)
	r\RoomDoors[3]\locked = True
	
	r\RoomDoors[4] = CreateDoor(r\zone, r\x - 1024.0 * RoomScale, 0.0, r\z-273*RoomScale, 0, r, False,DOOR_OFFICE)
	r\RoomDoors[4]\locked = True
	
	r\RoomDoors[5] = CreateDoor(r\zone, r\x - 1546.0 * RoomScale, 0.0, r\z+3*RoomScale, -90, r, False,DOOR_OFFICE_2)
	r\RoomDoors[5]\locked = True
	
	r\Objects[0] = CreatePivot()
	PositionEntity r\Objects[0],r\x+684.0*RoomScale,r\y+120.0*RoomScale,r\z-569.0*RoomScale
	EntityParent r\Objects[0],r\obj
	
End Function

Function UpdateEvent_Room2_Offices_5(e.Events)
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D