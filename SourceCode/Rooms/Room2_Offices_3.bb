
Function FillRoom_Room2_Offices_3(r.Rooms)
	Local it.Items
	Local i
	
	If Rand(2)=1 Then 
		it = CreateItem("Mobile Task Forces", "paper", r\x + 744.0 * RoomScale, r\y +240.0 * RoomScale, r\z + 944.0 * RoomScale)
		EntityParent(it\collider, r\obj)	
	Else
		it = CreateItem("Security Clearance Levels", "paper", r\x + 680.0 * RoomScale, r\y +240.0 * RoomScale, r\z + 944.0 * RoomScale)
		EntityParent(it\collider, r\obj)			
	EndIf
	
	it = CreateItem("Object Classes", "paper", r\x + 160.0 * RoomScale, r\y +240.0 * RoomScale, r\z + 568.0 * RoomScale)
	EntityParent(it\collider, r\obj)	
	
	it = CreateItem("Document", "paper", r\x -1440.0 * RoomScale, r\y +624.0 * RoomScale, r\z + 152.0 * RoomScale)
	EntityParent(it\collider, r\obj)	
	
	it = CreateItem("Radio Transceiver", "radio", r\x - 1184.0 * RoomScale, r\y + 480.0 * RoomScale, r\z - 800.0 * RoomScale)
	EntityParent(it\collider, r\obj)				
	
	For i = 0 To Rand(0,1)
		it = CreateItem("ReVision Eyedrops", "eyedrops", r\x - 1529.0*RoomScale, r\y + 563.0 * RoomScale, r\z - 572.0*RoomScale + i*0.05)
		EntityParent(it\collider, r\obj)				
	Next
	
	it = CreateItem("9V Battery", "bat", r\x - 1545.0 * RoomScale, r\y + 603.0 * RoomScale, r\z - 372.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	If Rand(2) = 1 Then
		it = CreateItem("9V Battery", "bat", r\x - 1540.0 * RoomScale, r\y + 603.0 * RoomScale, r\z - 340.0 * RoomScale)
		EntityParent(it\collider, r\obj)
	EndIf
	If Rand(2) = 1 Then
		it = CreateItem("9V Battery", "bat", r\x - 1529.0 * RoomScale, r\y + 603.0 * RoomScale, r\z - 308.0 * RoomScale)
		EntityParent(it\collider, r\obj)
	EndIf
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x - 1056.0 * RoomScale, 384.0*RoomScale, r\z + 290.0 * RoomScale, 90, r, True)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = True
	PositionEntity r\RoomDoors[0]\buttons[0], EntityX(r\RoomDoors[0]\buttons[0],True),EntityY(r\RoomDoors[0]\buttons[0],True),r\z + 161.0 * RoomScale,True
	PositionEntity r\RoomDoors[0]\buttons[1], EntityX(r\RoomDoors[0]\buttons[1],True),EntityY(r\RoomDoors[0]\buttons[1],True),r\z + 161.0 * RoomScale,True
	
End Function

Function UpdateEvent_Room2_Offices_3(e.Events)
	
	If PlayerRoom = e\room Then
		e\EventState[0] = e\EventState[0]+FPSfactor
		If e\EventState[0] > 700 Then
			If EntityDistanceSquared(e\room\RoomDoors[0]\obj, Collider)>PowTwo(0.5) Then 
				If EntityInView(e\room\RoomDoors[0]\obj, Camera)=False Then
					DebugLog "%@@= \ {2E6C2=FD gi`h]c"
					e\room\RoomDoors[0]\open = False
					RemoveEvent(e)
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D