
Function FillRoom_Lockroom_1(r.Rooms)
	Local d.Doors,d2.Doors,sc.SecurityCams
	
	d = CreateDoor(r\zone, r\x - 736.0 * RoomScale, 0, r\z - 104.0 * RoomScale, 0, r, True)
	d\timer = 70 * 5 : d\AutoClose = False : d\open = False
	
	EntityParent(d\buttons[0], 0)
	PositionEntity(d\buttons[0], r\x - 288.0 * RoomScale, 0.7, r\z - 640.0 * RoomScale)
	EntityParent(d\buttons[0], r\obj)
	
	d\buttons[1] = FreeEntity_Strict(d\buttons[1])
	
	d2 = CreateDoor(r\zone, r\x + 104.0 * RoomScale, 0, r\z + 736.0 * RoomScale, 270, r, True)
	d2\timer = 70 * 5 : d2\AutoClose = False: d2\open = False
	EntityParent(d2\buttons[0], 0)
	PositionEntity(d2\buttons[0], r\x + 640.0 * RoomScale, 0.7, r\z + 288.0 * RoomScale)
	RotateEntity (d2\buttons[0], 0, 90, 0)
	EntityParent(d2\buttons[0], r\obj)
	
	d2\buttons[1] = FreeEntity_Strict(d2\buttons[1])
	
	d\LinkedDoor = d2
	d2\LinkedDoor = d
	
	sc.SecurityCams = CreateSecurityCam(r\x - 688.0 * RoomScale, r\y + 384 * RoomScale, r\z + 688.0 * RoomScale, r, True)
	sc\angle = 45 + 180
	sc\turn = 45
	sc\ScrTexture = 1
	EntityTexture sc\ScrObj, ScreenTexs[sc\ScrTexture]
	
	TurnEntity(sc\CameraObj, 40, 0, 0)
	EntityParent(sc\obj, r\obj)
	
	PositionEntity(sc\ScrObj, r\x + 668 * RoomScale, 1.1, r\z - 96.0 * RoomScale)
	TurnEntity(sc\ScrObj, 0, 90, 0)
	EntityParent(sc\ScrObj, r\obj)
	
	sc.SecurityCams = CreateSecurityCam(r\x - 112.0 * RoomScale, r\y + 384 * RoomScale, r\z + 112.0 * RoomScale, r, True)
	sc\angle = 45
	sc\turn = 45
	sc\ScrTexture = 1
	EntityTexture sc\ScrObj, ScreenTexs[sc\ScrTexture]
	
	TurnEntity(sc\CameraObj, 40, 0, 0)
	EntityParent(sc\obj, r\obj)				
	
	PositionEntity(sc\ScrObj, r\x + 96.0 * RoomScale, 1.1, r\z - 668.0 * RoomScale)
	EntityParent(sc\ScrObj, r\obj)
	
	Local em.Emitters = CreateEmitter(r\x - 175.0 * RoomScale, 370.0 * RoomScale, r\z + 656.0 * RoomScale, 0)
	TurnEntity(em\Obj, 90, 0, 0, True)
	EntityParent(em\Obj, r\obj)
	em\RandAngle = 20
	em\Speed = 0.05
	em\SizeChange = 0.007
	em\Achange = -0.006
	em\Gravity = -0.24
	
	em.Emitters = CreateEmitter(r\x - 655.0 * RoomScale, 370.0 * RoomScale, r\z + 240.0 * RoomScale, 0)
	TurnEntity(em\Obj, 90, 0, 0, True)
	EntityParent(em\Obj, r\obj)
	em\RandAngle = 20
	em\Speed = 0.05
	em\SizeChange = 0.007
	em\Achange = -0.006
	em\Gravity = -0.24
	
End Function

Function UpdateEvent_Lockroom_1(e.Events)
	
	If e\room\dist < 6.0 And e\room\dist > 0 Then
		If Curr173 = Null Lor (Curr173 <> Null And Curr173\Idle <> SCP173_ACTIVE) Then
			RemoveEvent(e)
			Return
		EndIf
		If (Not EntityInView(Curr173\Collider, Camera)) Lor EntityDistanceSquared(Curr173\Collider, Collider) > PowTwo(15.0) Then 
			PositionEntity(Curr173\Collider, e\room\x + Cos(225-90 + e\room\angle) * 2, 0.6, e\room\z + Sin(225-90 + e\room\angle) * 2)
			ResetEntity(Curr173\Collider)
			RemoveEvent(e)
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D