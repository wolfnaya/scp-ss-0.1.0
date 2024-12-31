
Function FillRoom_Room2_Nuke(r.Rooms)
	Local d.Doors,it.Items,sc.SecurityCams
	Local i,n;,em.Emitters
	
;	em.Emitters = CreateEmitter(r\x - 54.0 * RoomScale, r\y+716.0*RoomScale, r\z +405* RoomScale, 1)
;	EntityParent(em\Obj, r\obj) : em\Room = r
;	em\Speed = 0.001
;	em\SizeChange = 0.001 : em\Achange = -0.006
;	em\Gravity = -0.05
;	
;	em.Emitters = CreateEmitter(r\x + 298.0 * RoomScale, r\y+716.0*RoomScale, r\z, 1)
;	EntityParent(em\Obj, r\obj) : em\Room = r
;	em\Speed = 0.001
;	em\SizeChange = 0.001 : em\Achange = -0.006
;	em\Gravity = -0.05
;	
;	em.Emitters = CreateEmitter(r\x -54.0 * RoomScale, r\y+716.0*RoomScale, r\z -432* RoomScale, 1)
;	EntityParent(em\Obj, r\obj) : em\Room = r
;	em\Speed = 0.001
;	em\SizeChange = 0.001 : em\Achange = -0.006
;	em\Gravity = -0.05
;	
;	em.Emitters = CreateEmitter(r\x - 398.0 * RoomScale, r\y+716.0*RoomScale, r\z, 1)
;	EntityParent(em\Obj, r\obj) : em\Room = r
;	em\Speed = 0.001
;	em\SizeChange = 0.001 : em\Achange = -0.006
;	em\Gravity = -0.05
	
	d = CreateDoor(r\zone, r\x + 576.0 * RoomScale, 0.0, r\z + 152.0 * RoomScale, 90, r, False, DOOR_WINDOWED, KEY_CARD_5)
	d\AutoClose = False : d\open = False
	PositionEntity(d\buttons[0], r\x + 602.0 * RoomScale, EntityY(d\buttons[0],True), r\z + 20.0 * RoomScale,True)
	PositionEntity(d\buttons[1], r\x + 550.0 * RoomScale, EntityY(d\buttons[1],True), r\z + 20.0 * RoomScale,True)
	
	d = CreateDoor(r\zone, r\x - 567.0 * RoomScale, 1504.0*RoomScale, r\z + 692.0 * RoomScale, 90, r, False, False, KEY_CARD_5)
	d\AutoClose = False : d\open = False	
	;PositionEntity(d\buttons[0], EntityX(d\buttons[0],True), EntityY(d\buttons[0],True), r\z + 608.0 * RoomScale,True)
	;PositionEntity(d\buttons[1], EntityX(d\buttons[1],True), EntityY(d\buttons[1],True), r\z + 608.0 * RoomScale,True)
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x + 1200.0 * RoomScale, 0.0, r\z, -90, r, True, 3)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = True
	
	r\Objects[4] = CreatePivot()
	PositionEntity(r\Objects[4], r\x + 1496.0 * RoomScale, 240.0 * RoomScale, r\z)
	EntityParent(r\Objects[4], r\obj)
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x + 680.0 * RoomScale, 1504.0 * RoomScale, r\z, -90, r, False, 3)
	r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False
	
	r\Objects[5] = CreatePivot()
	PositionEntity(r\Objects[5], r\x + 984.0 * RoomScale, 1744.0 * RoomScale, r\z)
	EntityParent(r\Objects[5], r\obj)
	
	r\Levers[0] = CreateLever(r,r\x - 1029.0 * RoomScale, r\y + 1712.0 * RoomScale, r\z - 553.0 * RoomScale,90,True)
	r\Levers[1] = CreateLever(r,r\x - 1029.0 * RoomScale, r\y + 1712.0 * RoomScale, r\z - (553.0-132.0) * RoomScale,90,True)
	
	;it = CreateItem("Nuclear Device Document", "paper", r\x - 768.0 * RoomScale, r\y + 1684.0 * RoomScale, r\z - 768.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","vest"), "vest", r\x - 944.0 * RoomScale, r\y + 1652.0 * RoomScale, r\z - 656.0 * RoomScale)
	EntityParent(it\collider, r\obj) : RotateEntity(it\collider, 0, -90, 0)
	
	sc.SecurityCams = CreateSecurityCam(r\x+624.0*RoomScale, r\y+1888.0*RoomScale, r\z-312.0*RoomScale, r)
	sc\angle = 90
	sc\turn = 45
	TurnEntity(sc\CameraObj, 20, 0, 0)
	
	r\Objects[6] = CreatePivot()
	PositionEntity r\Objects[6],r\x+1110.0*RoomScale,r\y+36.0*RoomScale,r\z-208.0*RoomScale
	EntityParent r\Objects[6],r\obj
	
	it = CreateItem("Glock 20-C", "glock",r\x+1067*RoomScale,1.0,r\z-142*RoomScale)
	it\state = 14 : it\state2 = 45
	EntityParent it\collider, r\obj
	
	it = CreateItem(GetLocalString("Item Names","scramble"), "scramble",r\x+1077*RoomScale,1.0,r\z-70*RoomScale)
	EntityParent it\collider, r\obj
	
End Function

Function UpdateEvent_Room2_Nuke(e.Events)
	Local n.NPCs,it.Items
	
	If PlayerRoom = e\room Then
		e\EventState[1] = UpdateElevators(e\EventState[1], e\room\RoomDoors[0], e\room\RoomDoors[1], e\room\Objects[4], e\room\Objects[5], e)
		
		e\EventState[0] = UpdateLever(e\room\Levers[0]\obj)
		UpdateLever(e\room\Levers[1]\obj)
	EndIf
	
	If e\EventState[2] = 0 Then
		n.NPCs = CreateNPC(NPC_Human,EntityX(e\room\Objects[6],True),0.5,EntityZ(e\room\Objects[6],True))
		RotateEntity n\Collider,0,e\room\angle+90,0
		n\State[0] = STATE_SCRIPT
		SetNPCFrame(n,555)
		n\IsDead = True
		ChangeNPCTexture(n,"GFX\npcs\body2.jpg")
		e\EventState[2] = 1
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS