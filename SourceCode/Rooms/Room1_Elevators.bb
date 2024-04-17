
Function FillRoom_Room1_Elevators(r.Rooms)
	Local sc.SecurityCams,w.WayPoints
	
	r\Objects[0] = CreateButton(r\x + 96.0*RoomScale, r\y + 160.0 * RoomScale, r\z + 64.0 * RoomScale, 0,0,0)
	EntityParent (r\Objects[0],r\obj)
	r\Objects[1] = CreateButton(r\x - 96.0*RoomScale, r\y + 160.0 * RoomScale, r\z + 64.0 * RoomScale, 0,0,0)
	EntityParent (r\Objects[1],r\obj)
	
	sc.SecurityCams = CreateSecurityCam(r\x+384.0*RoomScale, r\y+(448-64)*RoomScale, r\z-960.0*RoomScale, r)
	sc\angle = 45
	sc\room = r
	TurnEntity(sc\CameraObj, 20, 0, 0)
	EntityParent(sc\obj, r\obj)
	sc\ID = 3
	
	w.WayPoints = CreateWaypoint(r\x, r\y + 66.0 * RoomScale, r\z, Null, r)
	
End Function

Function UpdateEvent_Room1_Elevators(e.Events)
	Local i
	
	If PlayerRoom=e\room Then
		For i = 0 To 1
			UpdateButton(e\room\Objects[i])
			If d_I\ClosestButton = e\room\Objects[i] And keyhituse Then
				CreateMsg(GetLocalString("Doors", "elevator_broken"))
				PlaySound2(ButtonSFX[1], Camera, e\room\Objects[i])
				keyhituse=0
			EndIf
		Next
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D