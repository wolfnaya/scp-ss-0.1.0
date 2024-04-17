
Function FillRoom_Room3_EZ(r.Rooms)
	Local sc.SecurityCams
	
	sc.SecurityCams = CreateSecurityCam(r\x-320.0*RoomScale, r\y+384.0*RoomScale, r\z+512.25*RoomScale, r)
	sc\angle = 225
	TurnEntity(sc\CameraObj, 20, 0, 0)
	sc\ID = 2
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D