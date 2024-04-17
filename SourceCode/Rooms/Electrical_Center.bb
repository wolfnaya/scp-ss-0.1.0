
Function FillRoom_Electrical_Center(r.Rooms)
	Local d.Doors,it.Items,sc.SecurityCams
	Local i,n
	
	d = CreateDoor(r\zone, r\x + 64.0 * RoomScale, 0.0, r\z + 368.0 * RoomScale, 180, r, False, False, 2)
	d\AutoClose = False : d\open = False
	
	it = CreateItem("Note from Daniel", "paper", r\x-400.0*RoomScale,1040.0*RoomScale,r\z+115.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
	r\Levers[0] = CreateLever(r, r\x - 240.0 * RoomScale, r\y + 1104.0 * RoomScale, r\z + 632.0 * RoomScale, -90, True)
	r\Levers[1] = CreateLever(r, r\x - 240.0 * RoomScale, r\y + 1104.0 * RoomScale, r\z + (632.0 - 64.0) * RoomScale, -90, True)
	r\Levers[2] = CreateLever(r, r\x - 240.0 * RoomScale, r\y + 1104.0 * RoomScale, r\z + (632.0 - 128.0) * RoomScale, -90, True)
	
	sc.SecurityCams = CreateSecurityCam(r\x-265.0*RoomScale, r\y+1280.0*RoomScale, r\z+105.0*RoomScale, r)
	sc\angle = 45
	TurnEntity(sc\CameraObj, 20, 0, 0)
	sc\ID = 10
	
End Function

Function UpdateEvent_Electrical_Center(e.Events)
	
	If PlayerRoom = e\room Then
		
		EntityPick(Camera, 1.5)
		
		If PickedEntity() = e\room\Levers[1]\obj
			If e\EventState[0] = 0
				e\EventState[0] = Max(e\EventState[0],1)
				PlaySound_Strict HorrorSFX[7]
				PlaySound_Strict LeverSFX
			EndIf 
		EndIf
		
		;Primary Lighting
		UpdateLever(e\room\Levers[0]\obj)
		
		;Secondary Lighting
		Local prevstate2 = e\EventState[1]
		e\EventState[1] = UpdateLever(e\room\Levers[1]\obj)
		If (prevstate2 <> e\EventState[1]) And e\EventState[0]>0 Then PlaySound2(LightSFX, Camera, e\room\Levers[1]\objBase)
		If e\EventState[1]
			SecondaryLightOn = CurveValue(1.0, SecondaryLightOn, 10.0)
		Else
			SecondaryLightOn = CurveValue(0.0, SecondaryLightOn, 10.0)
		EndIf
		
		;Remote Door Control
		RemoteDoorOn = UpdateLever(e\room\Levers[2]\obj)
		
		If e\EventState[0] > 0 And e\EventState[0] < 200 Then
			e\EventState[0] = e\EventState[0] + FPSfactor
			RotateEntity(e\room\Levers[1]\obj, CurveValue(-80, EntityPitch(e\room\Levers[1]\obj), 5), EntityYaw(e\room\Levers[1]\obj), 0)
		EndIf 
		
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D