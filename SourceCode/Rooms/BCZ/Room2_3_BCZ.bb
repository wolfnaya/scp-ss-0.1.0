
Function FillRoom_Room2_3_BCZ(r.Rooms)
	Local r2.Rooms
	Local firstRoom% = True
	
	For r2 = Each Rooms
		If r2\RoomTemplate\Name = r\RoomTemplate\Name And r2 <> r Then
			firstRoom = False
			Exit
		EndIf
	Next
	
	If firstRoom Then
		InitFluLight(0,FLU_STATE_OFF,r)
		InitFluLight(1,FLU_STATE_OFF,r)
	Else
		InitFluLight(0,FLU_STATE_ON,r)
		InitFluLight(1,FLU_STATE_ON,r)
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D