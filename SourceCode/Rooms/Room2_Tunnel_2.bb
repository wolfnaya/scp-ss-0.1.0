
Function FillRoom_Room2_Tunnel_2(r.Rooms)
	
End Function

Function UpdateEvent_Room2_Tunnel_2(e.Events)
	
	Select e\EventState[0]
		Case 0 ; Wait for player to come in
			If PlayerRoom = e\room Then
				If (Curr173 <> Null And Curr173\Idle = SCP173_ACTIVE) Then
					If DistanceSquared(EntityX(Collider), EntityX(e\room\obj), EntityZ(Collider), EntityZ(e\room\obj)) < 12.0 Then
						PlaySound_Strict(LightSFX)
						LoadEventSound(e,"SFX\SCP\173\VentRun.ogg")
						e\EventState[0] = 1 ; Begin event
					EndIf
				EndIf
			EndIf
		Case 1 ; The lights go off
			If (Not ChannelPlaying(e\SoundCHN[0])) Then
				e\SoundCHN[0] = PlaySound_Strict(e\Sound[0])
			EndIf
			UpdateSoundOrigin(e\SoundCHN[0], Camera, e\room\obj, 10, 2.0)
			e\EventState[1] = Min(e\EventState[1] + FPSfactor, 200)
			If e\EventState[1] >= 50 Then
				IsCutscene = True
				BlinkTimer = -10
				IsCutscene = False
			Else
				LightBlink = Rnd(0.0,1.0)*(e\EventState[1]/50)
			EndIf
			If e\EventState[1] >= 200 Then
				e\EventState[0] = 2
			EndIf
		Case 2 ; Teleport 173 to the room
			StopChannel(e\SoundCHN[0])
			PlaySound_Strict(LoadTempSound("SFX\ambient\general\ambient3.ogg"))
			PositionEntity(Curr173\Collider, EntityX(e\room\obj), 0.6, EntityZ(e\room\obj))
			ResetEntity(Curr173\Collider)					
			Curr173\Idle = SCP173_STATIONARY
			e\EventState[0] = 3
		Case 3 ; Bring back the lights
			IsCutscene = True
			BlinkTimer = -10
			IsCutscene = False
			PointEntity(Collider,Curr173\Collider)
			e\EventState[1] = Max(e\EventState[1] - FPSfactor*2, 0)
			If e\EventState[1] <= 0 Then
				LightBlink = 1.0
				BlinkTimer = BLINKFREQ
				Curr173\Idle = SCP173_ACTIVE
				RemoveEvent(e)
			EndIf
	End Select
	
End Function

Function UpdateEvent_Room2_Tunnel_2_Smoke(e.Events)
	Local em.Emitters
	Local i%
	
	If PlayerRoom = e\room Then
		If e\room\dist < 3.5 Then
			PlaySound_Strict(BurstSFX) 
			For i = -1 To 1 Step 2
				em.Emitters = CreateEmitter(EntityX(e\room\obj,True), 544.0 * RoomScale, EntityZ(e\room\obj,True) + (512.0 * RoomScale) * i, 0)
				TurnEntity(em\Obj, 90, 0, 0, True)
				EntityParent(em\Obj, e\room\obj)
				em\Size = 0.05
				em\RandAngle = 10
				em\Speed = 0.06
				em\SizeChange = 0.007
			Next
			RemoveEvent(e)
		EndIf					
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D