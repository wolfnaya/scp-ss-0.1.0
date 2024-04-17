
Function FillRoom_Room3_EZ(r.Rooms)
	Local sc.SecurityCams,r2.Rooms
	Local firstRoom% = True
	
	For r2 = Each Rooms
		If r2\RoomTemplate\Name = r\RoomTemplate\Name And r2 <> r Then
			firstRoom = False
			Exit
		EndIf
	Next
	
	sc.SecurityCams = CreateSecurityCam(r\x-320.0*RoomScale, r\y+384.0*RoomScale, r\z+512.25*RoomScale, r)
	sc\angle = 225
	TurnEntity(sc\CameraObj, 20, 0, 0)
	sc\ID = 2
	
	If firstRoom Then
		InitFluLight(0,FLU_STATE_ON,r)
		InitFluLight(1,FLU_STATE_FLICKER,r)
	Else
		InitFluLight(0,FLU_STATE_ON,r)
		InitFluLight(1,FLU_STATE_OFF,r)
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D