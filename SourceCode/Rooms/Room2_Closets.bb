
Function FillRoom_Room2_Closets(r.Rooms)
	Local it.Items,d.Doors,sc.SecurityCams
	
	it = CreateItem("Document SCP-1048", "paper", r\x + 736.0 * RoomScale, r\y + 176.0 * RoomScale, r\z + 736.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Gas Mask", "gasmask", r\x + 736.0 * RoomScale, r\y + 176.0 * RoomScale, r\z + 544.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("9V Battery", "bat", r\x + 736.0 * RoomScale, r\y + 176.0 * RoomScale, r\z - 448.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	If Rand(2) = 1 Then
		it = CreateItem("9V Battery", "bat", r\x + 730.0 * RoomScale, r\y + 176.0 * RoomScale, r\z - 496.0 * RoomScale)
		EntityParent(it\collider, r\obj)
	EndIf
	If Rand(2) = 1 Then
		it = CreateItem("9V Battery", "bat", r\x + 740.0 * RoomScale, r\y + 176.0 * RoomScale, r\z - 560.0 * RoomScale)
		EntityParent(it\collider, r\obj)
	EndIf
	
	it = CreateItem("Level 1 Key Card", "key1", r\x + 736.0 * RoomScale, r\y + 240.0 * RoomScale, r\z + 752.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	Local clipboard.Items = CreateItem("Clipboard","clipboard",r\x + 736.0 * RoomScale, r\y + 224.0 * RoomScale, r\z -720.0 * RoomScale)
	EntityParent(clipboard\collider, r\obj)
	
	it = CreateItem("Incident Report SCP-1048-A", "paper",r\x + 736.0 * RoomScale, r\y + 224.0 * RoomScale, r\z -720.0 * RoomScale)
	it\Picked = True
	it\Dropped = -1
	clipboard\SecondInv[0] = it
	SetAnimTime clipboard\model, 0.0
	clipboard\invimg = clipboard\itemtemplate\invimg
	HideEntity(it\collider)
	
	r\Objects[0]=CreatePivot(r\obj)
	PositionEntity r\Objects[0], r\x-1120*RoomScale, -256*RoomScale, r\z+896*RoomScale, True
	r\Objects[1]=CreatePivot(r\obj)
	PositionEntity r\Objects[1], r\x-1232*RoomScale, -256*RoomScale, r\z-160*RoomScale, True
	
	d.Doors = CreateDoor(0, r\x - 240.0 * RoomScale, 0.0, r\z, 90, r, False)
	PositionEntity(d\buttons[0], r\x - 230.0 * RoomScale, EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
	PositionEntity(d\buttons[1], r\x - 250.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
	d\open = False : d\AutoClose = False
	
	sc.SecurityCams = CreateSecurityCam(r\x, r\y + 704*RoomScale, r\z + 863*RoomScale, r)
	sc\angle = 180
	TurnEntity(sc\CameraObj, 20, 0, 0)
	sc\ID = 0
	
End Function

Function UpdateEvent_Room2_Closets(e.Events)
	
	If e\EventState[0] = 0 Then
		If PlayerRoom = e\room And Curr173 <> Null And Curr173\Idle = SCP173_ACTIVE Then
			If e\EventStr = "" And QuickLoadPercent = -1
				QuickLoadPercent = 0
				QuickLoad_CurrEvent = e
				e\EventStr = "load0"
			EndIf
		EndIf
	Else
		e\EventState[0]=e\EventState[0]+FPSfactor
		If e\EventState[0] < 70*3.5 Then
			RotateEntity(e\room\NPC[1]\Collider,0,CurveAngle(e\room\angle+90,EntityYaw(e\room\NPC[1]\Collider),100.0),0,True)
			
			e\room\NPC[0]\State[0]=1
			If e\EventState[0] > 70*3.2 And e\EventState[0]-FPSfactor =< 70*3.2 Then PlaySound2(IntroSFX[4],Camera,e\room\obj,15.0)
		ElseIf e\EventState[0] < 70*6.5
			If e\EventState[0]-FPSfactor < 70*3.5 Then
				e\room\NPC[0]\State[0]=0
				e\room\NPC[1]\SoundChn = PlaySound2(e\room\NPC[1]\Sound, Camera, e\room\NPC[1]\Collider,12.0)
			EndIf
			
			If e\EventState[0] > 70*4.5 Then
				PointEntity e\room\NPC[0]\obj, e\room\obj
				RotateEntity(e\room\NPC[0]\Collider,0,CurveAngle(EntityYaw(e\room\NPC[0]\obj),EntityYaw(e\room\NPC[0]\Collider),30.0),0,True)
			EndIf
			PointEntity e\room\NPC[1]\obj, e\room\obj
			TurnEntity e\room\NPC[1]\obj, 0, Sin(e\EventState[0])*25, 0
			RotateEntity(e\room\NPC[1]\Collider,0,CurveAngle(EntityYaw(e\room\NPC[1]\obj),EntityYaw(e\room\NPC[1]\Collider),30.0),0,True)
		Else
			If e\EventState[0]-FPSfactor < 70*6.5 Then 
				PlaySound_Strict (HorrorSFX[0])
				PlaySound_Strict (LightSFX)
			EndIf
			BlinkTimer = Max((70*6.5-e\EventState[0])/5.0 - Rnd(0.0,2.0),-10)
			If BlinkTimer =-10 Then
				If e\EventState[0] > 70*7.5 And e\EventState[0]-FPSfactor =< 70*7.5 Then PlaySound2(NeckSnapSFX[0],Camera,e\room\NPC[0]\Collider,8.0)
				If e\EventState[0] > 70*8.0 And e\EventState[0]-FPSfactor =< 70*8.0 Then PlaySound2(NeckSnapSFX[1],Camera,e\room\NPC[1]\Collider,8.0)
				SetNPCFrame e\room\NPC[0], 60
				e\room\NPC[0]\State[0]=8
				
				SetNPCFrame e\room\NPC[1], 19
				e\room\NPC[1]\State[0] = 6
			EndIf
			
			If e\EventState[0] > 70*8.5 Then
				PositionEntity Curr173\Collider, (EntityX(e\room\Objects[0],True)+EntityX(e\room\Objects[1],True))/2,EntityY(e\room\Objects[0],True),(EntityZ(e\room\Objects[0],True)+EntityZ(e\room\Objects[1],True))/2
				PointEntity Curr173\Collider, Collider
				ResetEntity Curr173\Collider
				RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D