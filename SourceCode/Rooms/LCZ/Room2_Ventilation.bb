
Function FillRoom_Room2_Ventilation(r.Rooms)
	Local r2.Rooms
	
	For r2.Rooms = Each Rooms
		If r2<>r Then
			If r2\RoomTemplate\Name = "room2_ventilation" Then
				r\Objects[0] = CopyEntity(r2\Objects[0]) ;don't load the mesh again
				r\Objects[1] = CopyEntity(r2\Objects[1]) ;don't load the mesh again
				r\Objects[2] = CopyEntity(r2\Objects[2]) ;don't load the mesh again
				Exit
			EndIf
		EndIf
	Next
	If r\Objects[0]=0 Then r\Objects[0] = LoadRMesh("GFX\map\rooms\room2_ventilation\room2_ventilation_fan.rmesh",Null)
	ScaleEntity r\Objects[0], RoomScale, RoomScale, RoomScale
	PositionEntity(r\Objects[0], r\x, r\y, r\z)
	EntityParent(r\Objects[0], r\obj)
	EntityPickMode(r\Objects[0], 2)
	If r\Objects[1]=0 Then r\Objects[1] = LoadRMesh("GFX\map\rooms\room2_ventilation\room2_ventilation_smallfan_1.rmesh",Null)
	ScaleEntity r\Objects[1], RoomScale, RoomScale, RoomScale
	PositionEntity(r\Objects[1], r\x, r\y, r\z)
	EntityParent(r\Objects[1], r\obj)
	EntityPickMode(r\Objects[1], 2)
	If r\Objects[2]=0 Then r\Objects[2] = LoadRMesh("GFX\map\rooms\room2_ventilation\room2_ventilation_smallfan_2.rmesh",Null)
	ScaleEntity r\Objects[2], RoomScale, RoomScale, RoomScale
	PositionEntity(r\Objects[2], r\x, r\y, r\z)
	EntityParent(r\Objects[2], r\obj)
	EntityPickMode(r\Objects[2], 2)
	
End Function

Function UpdateEvent_Room2_Ventilation(e.Events)
	Local temp%
	; ~ Eventstate1 = timer for turning the fan on/off
	; ~ Eventstate2 = fan on/off
	; ~ Eventstate3 = the speed of the fan
	If PlayerRoom = e\room Then
		TurnEntity (e\room\Objects[0], 0,e\EventState[2]*FPSfactor, 0)
		;TurnEntity (e\room\Objects[1],e\EventState[2]*n\State[2], 0, 0)
		;TurnEntity (e\room\Objects[2], 0,e\EventState[2]*n\State[2], 0)
		If e\EventState[2] > 0.01 Then
			e\room\SoundCHN = LoopSound2 (RoomAmbience[9], e\room\SoundCHN, Camera, e\room\Objects[0], 5.0, (e\EventState[2]/4.0))
			;e\room\SoundCHN = LoopSound2 (RoomAmbience[9], e\room\SoundCHN, Camera, e\room\Objects[1], 5.0, (e\EventState[2]/4.0))
			;e\room\SoundCHN = LoopSound2 (RoomAmbience[9], e\room\SoundCHN, Camera, e\room\Objects[2], 5.0, (e\EventState[2]/4.0))
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
;~C#Blitz3D