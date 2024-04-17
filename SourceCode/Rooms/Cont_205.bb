
Function FillRoom_Cont_205(r.Rooms)
	Local sc.SecurityCams
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x + 128.0 * RoomScale, 0, r\z + 640.0 *RoomScale, 90, r, True, False, 3)
	r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x - 1392.0 * RoomScale, -128.0 * RoomScale, r\z - 384*RoomScale, 0, r, True, False, 3)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
	r\RoomDoors[0]\buttons[0] = FreeEntity_Strict(r\RoomDoors[0]\buttons[0])
	r\RoomDoors[0]\buttons[1] = FreeEntity_Strict(r\RoomDoors[0]\buttons[1])
	
	sc.SecurityCams = CreateSecurityCam(r\x - 1152.0 * RoomScale, r\y + 900.0 * RoomScale, r\z + 176.0 * RoomScale, r, True)
	sc\angle = 90 : sc\turn = 0
	EntityParent(sc\obj, r\obj)
	
	sc\AllowSaving = False
	sc\RenderInterval = 0
	
	EntityParent(sc\ScrObj, 0)
	PositionEntity(sc\ScrObj, r\x - 1716.0 * RoomScale, r\y + 160.0 * RoomScale, r\z + 176.0 * RoomScale, True)
	TurnEntity sc\ScrObj, 0, 90, 0
	ScaleSprite sc\ScrObj, 896.0*0.5*RoomScale, 896.0*0.5*RoomScale
	
	EntityParent(sc\ScrObj, r\obj)
	
	CameraZoom (sc\Cam, 1.5)
	
	HideEntity sc\ScrOverlay
	HideEntity sc\MonitorObj
	
	r\Objects[0] = CreatePivot(r\obj)
	PositionEntity r\Objects[0], r\x - 1536.0 * RoomScale, r\y + 730.0 * RoomScale, r\z + 192.0 * RoomScale, True
	RotateEntity r\Objects[0], 0,-90,0,True
	
	r\Objects[1] = sc\ScrObj
	
End Function

Function UpdateEvent_Cont_205(e.Events)
	
	If PlayerRoom = e\room Then
		If e\EventState[0]=0 Lor e\room\Objects[0]=0 Then
			If e\EventStr = "" And QuickLoadPercent = -1
				QuickLoadPercent = 0
				QuickLoad_CurrEvent = e
				e\EventStr = "load0"
			EndIf
			
			If e\room\Objects[3]<>0
				HideEntity(e\room\Objects[3])
				HideEntity(e\room\Objects[4])
				HideEntity(e\room\Objects[5])
				HideEntity(e\room\Objects[6])
			EndIf
			
			If e\room\RoomDoors[1]\open = True
				e\EventState[0] = 1
				GiveAchievement(Achv205)
			EndIf
		Else
			ShouldPlay = 16
			If (e\EventState[0]<65) Then
				If (DistanceSquared(EntityX(Collider), EntityX(e\room\Objects[0],True), EntityZ(Collider), EntityZ(e\room\Objects[0],True))<PowTwo(2.0)) And (Not NoTarget) Then
					PlaySound_Strict(LoadTempSound("SFX\SCP\205\Enter.ogg"))
					
					e\eventstate[0] = Max(e\eventstate[0], 65)
					
					ShowEntity(e\room\Objects[3])
					ShowEntity(e\room\Objects[4])
					ShowEntity(e\room\Objects[5])
					HideEntity(e\room\Objects[6])
					
					SetAnimTime(e\room\Objects[3], 492)
					SetAnimTime(e\room\Objects[4], 434)
					SetAnimTime(e\room\Objects[5], 434)
					
					e\room\RoomDoors[0]\open = False
				EndIf
				
				If e\eventstate[0]>7 Then
					If (Rand(0,300)=1) Then
						e\room\RoomDoors[0]\open = Not e\room\RoomDoors[0]\open
					EndIf
				EndIf 
				
				e\EventState[1] = e\EventState[1] + FPSfactor							
			EndIf
			
			Select e\eventstate[0]
				Case 1
					ShowEntity e\room\Objects[1]
					HideEntity(e\room\Objects[5])
					HideEntity(e\room\Objects[4])
					HideEntity(e\room\Objects[3])
					;sitting
					ShowEntity(e\room\Objects[6])
					Animate2(e\room\Objects[6], AnimTime(e\room\Objects[6]), 526, 530, 0.2)
					If e\EventState[1] > 20*70 Then e\eventstate[0] = e\eventstate[0]+1
				Case 3
					ShowEntity e\room\Objects[1]
					HideEntity(e\room\Objects[5])
					HideEntity(e\room\Objects[4])
					HideEntity(e\room\Objects[3])
					;laying down
					ShowEntity(e\room\Objects[6])
					Animate2(e\room\Objects[6], AnimTime(e\room\Objects[6]), 377, 525, 0.2)
					If e\EventState[1] > 30*70 Then e\eventstate[0] = e\eventstate[0]+1
				Case 5
					ShowEntity e\room\Objects[1]
					HideEntity(e\room\Objects[5])
					HideEntity(e\room\Objects[4])
					HideEntity(e\room\Objects[3])
					;standing
					ShowEntity(e\room\Objects[6])
					Animate2(e\room\Objects[6], AnimTime(e\room\Objects[6]), 228, 376, 0.2)
					If e\EventState[1] > 40*70 Then 
						e\eventstate[0] = e\eventstate[0]+1
						PlaySound2(LoadTempSound("SFX\SCP\205\Horror.ogg"), Camera, e\room\Objects[6], 10, 0.3)
					EndIf	
				Case 7
					ShowEntity e\room\Objects[1]
					ShowEntity(e\room\Objects[6])
					HideEntity(e\room\Objects[4])
					HideEntity(e\room\Objects[3])
					;first demon appears
					ShowEntity(e\room\Objects[5])
					;le sexy demon pose
					Animate2(e\room\Objects[5], AnimTime(e\room\Objects[5]), 500, 648, 0.2)
					If e\EventState[1] > 60*70 Then 
						e\eventstate[0] = e\eventstate[0]+1
						PlaySound2(LoadTempSound("SFX\SCP\205\Horror.ogg"), Camera, e\room\Objects[6], 10, 0.5)
					EndIf
				Case 9
					ShowEntity e\room\Objects[1]
					ShowEntity(e\room\Objects[6])
					ShowEntity(e\room\Objects[5])
					HideEntity(e\room\Objects[3])
					;second demon appears
					ShowEntity(e\room\Objects[4])
					;idle
					Animate2(e\room\Objects[4], AnimTime(e\room\Objects[4]), 2, 200, 0.2)
					Animate2(e\room\Objects[5], AnimTime(e\room\Objects[5]), 4, 125, 0.2)
					
					If e\EventState[1] > 80*70 Then 
						e\eventstate[0] = e\eventstate[0]+1
						PlaySound_Strict(LoadTempSound("SFX\SCP\205\Horror.ogg"))
					EndIf
				Case 11
					ShowEntity e\room\Objects[1]
					ShowEntity(e\room\Objects[6])
					ShowEntity(e\room\Objects[5])
					ShowEntity(e\room\Objects[4])
					;third demon
					ShowEntity(e\room\Objects[3])
					;idle
					Animate2(e\room\Objects[3], AnimTime(e\room\Objects[3]), 2, 226, 0.2)
					Animate2(e\room\Objects[4], AnimTime(e\room\Objects[4]), 2, 200, 0.2)
					Animate2(e\room\Objects[5], AnimTime(e\room\Objects[5]), 4, 125, 0.2)
					
					If e\EventState[1] > 85*70 Then e\eventstate[0] = e\eventstate[0]+1
				Case 13
					ShowEntity e\room\Objects[1]
					ShowEntity(e\room\Objects[6])
					ShowEntity(e\room\Objects[5])
					ShowEntity(e\room\Objects[4])
					ShowEntity(e\room\Objects[3])
					If (AnimTime(e\room\Objects[6])<>227) Then SetAnimTime(e\room\Objects[6], 227)
					
					Animate2(e\room\Objects[3], AnimTime(e\room\Objects[3]), 2, 491, 0.05)
					Animate2(e\room\Objects[4], AnimTime(e\room\Objects[4]), 197, 433, 0.05)
					Animate2(e\room\Objects[5], AnimTime(e\room\Objects[5]), 2, 433, 0.05)
				Case 66
					ShowEntity e\room\Objects[1]
					Animate2(e\room\Objects[3], AnimTime(e\room\Objects[3]), 492, 534, 0.1, False)
					Animate2(e\room\Objects[4], AnimTime(e\room\Objects[4]), 434, 466, 0.1, False)
					Animate2(e\room\Objects[5], AnimTime(e\room\Objects[5]), 434, 494, 0.1, False)
					
					If AnimTime(e\room\Objects[3])>515 Then
						If AnimTime(e\room\Objects[3])>533 Then 
							e\eventstate[0] = 67
							e\EventState[1] = 0										
							e\EventState[2] = 0
							HideEntity e\room\Objects[1]
						EndIf
					EndIf
				Case 67
					If (Rand(150)=1) Then
						If (Not NoTarget) Then
							m_msg\DeathTxt = GetLocalStringR("Singleplayer", "cont_205_death", Designation)
							
							DamageSPPlayer(Rnd(10,20))
							PlaySound_Strict DamageSFX[Rand(2,3)]
							CameraShake = 0.5
						
							e\EventState[1] = Rnd(-0.1, 0.1)
							e\EventState[2] = Rnd(-0.1, 0.1)
						
							TranslateEntity(Collider, e\EventState[1],0,e\EventState[2])
							e\EventState[1] = CurveValue(e\EventState[1], 0, 10.0)								
							e\EventState[2] = CurveValue(e\EventState[2], 0, 10.0)
						EndIf
					EndIf
				Default
					If (Rand(3)=1) Then
						HideEntity e\room\Objects[1]
					Else
						ShowEntity e\room\Objects[1]
					EndIf
					
					e\EventState[2] = e\EventState[2] + FPSfactor
					If (e\EventState[2]>50) Then
						ShowEntity e\room\Objects[1]
						e\eventstate[0] = e\eventstate[0]+1
						e\EventState[2]=0
					EndIf
				End Select
			EndIf
		ElseIf (e\room\Objects[3]<>0) Then
			HideEntity(e\room\Objects[3])
			HideEntity(e\room\Objects[4])
			HideEntity(e\room\Objects[5])
			HideEntity(e\room\Objects[6])
		Else
			e\eventstate[0] = 0
			e\EventStr = ""
		EndIf
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D