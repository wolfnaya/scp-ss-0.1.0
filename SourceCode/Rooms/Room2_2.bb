
Function FillRoom_Room2_2(r.Rooms)
	Local r2.Rooms
	
	For r2.Rooms = Each Rooms
		If r2<>r Then
			If r2\RoomTemplate\Name = "room2_2" Then
				r\Objects[0] = CopyEntity(r2\Objects[0]) ;don't load the mesh again
				Exit
			EndIf
		EndIf
	Next
	If r\Objects[0]=0 Then r\Objects[0] = LoadRMesh("GFX\map\rooms\room2_2\fan_opt.rmesh",Null)
	ScaleEntity r\Objects[0], RoomScale, RoomScale, RoomScale
	PositionEntity(r\Objects[0], r\x - 248 * RoomScale, 528 * RoomScale, r\z, 0)
	EntityParent(r\Objects[0], r\obj)
	
End Function

Function UpdateEvent_Room2_2(e.Events)
	Local temp%
	
	;eventstate1 = timer for turning the fan on/off
	;eventstate2 = fan on/off
	;eventstate3 = the speed of the fan
	If PlayerRoom = e\room Then
		TurnEntity (e\room\Objects[0], e\EventState[2]*FPSfactor, 0, 0)
		If e\EventState[2] > 0.01 Then
			e\room\SoundCHN = LoopSound2 (RoomAmbience[9], e\room\SoundCHN, Camera, e\room\Objects[0], 5.0, (e\EventState[2]/4.0))
		EndIf
		e\EventState[2] = CurveValue(e\EventState[1]*5, e\EventState[2], 150.0)			
	EndIf
	
	If e\room\dist < 16.0 Then 
		If e\EventState[0] < 0 Then
			e\EventState[0] = Rand(15,30)*70
			temp = e\EventState[1]
			e\EventState[1] = Rand(0,1)
			If PlayerRoom<>e\room Then
				e\EventState[2] = e\EventState[1]*5
			Else
				If temp = 0 And e\EventState[1] = 1.0 Then ;turn on the fan
					PlaySound2 (LoadTempSound("SFX\ambient\Room ambience\FanOn.ogg"), Camera, e\room\Objects[0], 8.0)
				ElseIf temp = 1 And e\EventState[1] = 0.0 ;turn off the fan
					PlaySound2 (LoadTempSound("SFX\ambient\Room ambience\FanOff.ogg"), Camera, e\room\Objects[0], 8.0)
				EndIf
			EndIf
		Else
			e\EventState[0] = e\EventState[0]-FPSfactor
		EndIf					
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D