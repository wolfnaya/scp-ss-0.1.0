
Function FillRoom_Cont_914(r.Rooms)
	Local d.Doors,it.Items
	Local i
	
	r\RoomDoors[2] = CreateDoor(r\zone,r\x,0,r\z-368.0*RoomScale,0,r,False,True,2)
	r\RoomDoors[2]\dir=1 : r\RoomDoors[2]\AutoClose=False : r\RoomDoors[2]\open=False
	PositionEntity (r\RoomDoors[2]\buttons[0], r\x - 496.0 * RoomScale, 0.7, r\z - 272.0 * RoomScale, True)
	TurnEntity(r\RoomDoors[2]\buttons[0], 0, 90, 0)
	
	r\Objects[0] = LoadMesh_Strict("GFX\map\rooms\cont_914\914key.x")
	r\Objects[1] = LoadMesh_Strict("GFX\map\rooms\cont_914\914knob.x")
	
	For  i% = 0 To 1
		ScaleEntity(r\Objects[i], RoomScale, RoomScale, RoomScale)
		EntityPickMode(r\Objects[i], 2)
	Next
	
	PositionEntity (r\Objects[0], r\x, r\y + 190.0 * RoomScale, r\z + 374.0 * RoomScale)
	PositionEntity (r\Objects[1], r\x, r\y + 230.0 * RoomScale, r\z + 374.0 * RoomScale)
	EntityParent(r\Objects[0], r\obj)
	EntityParent(r\Objects[1], r\obj)
	
	d = CreateDoor(r\zone, r\x - 624.0 * RoomScale, 0.0, r\z + 528.0 * RoomScale, 180, r, True)
	d\obj2 = FreeEntity_Strict(d\obj2)
	For i = 0 To 1
		d\buttons[i] = FreeEntity_Strict(d\buttons[i])
	Next
	r\RoomDoors[0] = d: d\AutoClose = False
	
	d = CreateDoor(r\zone, r\x + 816.0 * RoomScale, 0.0, r\z + 528.0 * RoomScale, 180, r, True)
	d\obj2 = FreeEntity_Strict(d\obj2)
	For i = 0 To 1
		d\buttons[i] = FreeEntity_Strict(d\buttons[i])
	Next
	r\RoomDoors[1] = d : d\AutoClose = False
	
	r\Objects[2] = CreatePivot()
	r\Objects[3] = CreatePivot()
	PositionEntity(r\Objects[2], r\x - 712.0 * RoomScale, 0.5, r\z + 640.0 * RoomScale)
	PositionEntity(r\Objects[3], r\x + 728.0 * RoomScale, 0.5, r\z + 640.0 * RoomScale)
	EntityParent(r\Objects[2], r\obj)
	EntityParent(r\Objects[3], r\obj)
	
	it = CreateItem("Note", "paper", r\x +954.0 * RoomScale, r\y +228.0 * RoomScale, r\z + 127.0 * RoomScale)
	EntityParent(it\collider, r\obj)	
	
	it = CreateItem("First Aid Kit", "firstaid", r\x + 960.0 * RoomScale, r\y + 112.0 * RoomScale, r\z - 40.0 * RoomScale)
	EntityParent(it\collider, r\obj) : RotateEntity(it\collider, 0, 90, 0)
	
	it = CreateItem("Dr. L's Note", "paper", r\x - 928.0 * RoomScale, 160.0 * RoomScale, r\z - 160.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
End Function

Function UpdateEvent_Cont_914(e.Events)
	Local it.Items
	Local angle#
	Local i
	
	If PlayerRoom = e\room Then
		
		If e\room\RoomDoors[2]\open
			GiveAchievement(Achv914)
			e\EventState[1]=1
		EndIf
		
		If e\EventState[1]=1
			ShouldPlay = 22
		EndIf
		
		EntityPick(Camera, 1.0)
		If PickedEntity() = e\room\Objects[0] Then
			DrawHandIcon = True
			If keyhituse Then GrabbedEntity = e\room\Objects[0]
		ElseIf PickedEntity() = e\room\Objects[1]
			DrawHandIcon = True
			If keyhituse Then GrabbedEntity = e\room\Objects[1]
		EndIf
		
		If keydownuse Lor keyhituse Then
			If GrabbedEntity <> 0 Then ;avain
				If GrabbedEntity = e\room\Objects[0] Then
					If e\eventstate[0] = 0 Then
						DrawHandIcon = True
						TurnEntity(GrabbedEntity, 0, 0, -mouse_x_speed_1 * 2.5)
						
						angle = WrapAngle(EntityRoll(e\room\Objects[0]))
						If angle > 181 Then DrawArrowIcon[3] = True
						DrawArrowIcon[1] = True
						
						If angle < 90 Then
							RotateEntity(GrabbedEntity, 0, 0, 361.0)
						ElseIf angle < 180
							RotateEntity(GrabbedEntity, 0, 0, 180)
						EndIf
						
						If angle < 181 And angle > 90 Then
							For it.Items = Each Items
								If it\collider <> 0 And it\Picked = False Then
									If Abs(EntityX(it\collider) - (e\room\x - 712.0 * RoomScale)) < 200.0 Then
										If Abs(EntityY(it\collider) - (e\room\y + 648.0 * RoomScale)) < 104.0 Then
											e\eventstate[0] = 1
											e\SoundCHN[0] = PlaySound2(MachineSFX, Camera, e\room\Objects[1])
											e\room\RoomDoors[1]\SoundCHN = PlaySound2(LoadTempSound("SFX\SCP\914\DoorClose.ogg"), Camera, e\room\RoomDoors[1]\obj)
											Exit
										EndIf
									End If
								End If
							Next
						EndIf
					End If
				ElseIf GrabbedEntity = e\room\Objects[1]
					If e\eventstate[0] = 0 Then
						DrawHandIcon = True
						TurnEntity(GrabbedEntity, 0, 0, -mouse_x_speed_1 * 2.5)
						
						angle# = WrapAngle(EntityRoll(e\room\Objects[1]))
						DrawArrowIcon[3] = True
						DrawArrowIcon[1] = True
						
						If angle > 90 Then
							If angle < 180 Then
								RotateEntity(GrabbedEntity, 0, 0, 90.0)
							ElseIf angle < 270
								RotateEntity(GrabbedEntity, 0, 0, 270)
							EndIf
						EndIf
						
					End If
				End If
			End If
		Else
			GrabbedEntity = 0
		End If
		
		Local setting%
		
		If GrabbedEntity <> e\room\Objects[1] Then
			angle# = WrapAngle(EntityRoll(e\room\Objects[1]))
			If angle < 22.5 Then
				angle = 0
				setting = ONETOONE
			ElseIf angle < 67.5
				angle = 40
				setting = COARSE
			ElseIf angle < 180
				angle = 90
				setting = ROUGH
			ElseIf angle > 337.5
				angle = 359 - 360
				setting = ONETOONE
			ElseIf angle > 292.5
				angle = 320 - 360
				setting = FINE
			Else
				angle = 270 - 360
				setting = VERY_FINE
			End If
			RotateEntity(e\room\Objects[1], 0, 0, CurveValue(angle, EntityRoll(e\room\Objects[1]), 20))
		EndIf
		
		For i% = 0 To 1
			If GrabbedEntity = e\room\Objects[i] Then
				If Not EntityInView(e\room\Objects[i], Camera) Then
					GrabbedEntity = 0
				ElseIf EntityDistanceSquared(e\room\Objects[i], Camera) > PowTwo(1.0)
					GrabbedEntity = 0
				End If
				Exit
			End If
		Next
		
		If e\EventState[0] > 0 Then
			e\EventState[0] = e\EventState[0] + FPSfactor
			
			
			e\room\RoomDoors[1]\open = False
			If e\EventState[0] > 70 * 2 Then
				If e\room\RoomDoors[0]\open=True Then
					e\room\RoomDoors[0]\SoundCHN = PlaySound2(LoadTempSound("SFX\SCP\914\DoorClose.ogg"), Camera, e\room\RoomDoors[0]\obj)
				EndIf
				
				e\room\RoomDoors[0]\open = False
			EndIf
			
			If DistanceSquared(EntityX(Collider), EntityX(e\room\Objects[2], True), EntityZ(Collider), EntityZ(e\room\Objects[2], True)) < PowTwo(170.0 * RoomScale) Then
				
				If setting = ROUGH Lor setting = COARSE Then
					If e\EventState[0] > 70 * 2.6 And e\EventState[0] - FPSfactor2 < 70 * 2.6 Then PlaySound_Strict Death914SFX
				EndIf
				
				If e\EventState[0] > 70 * 3 Then
					Select setting
						Case ROUGH
							KillTimer = Min(-1, KillTimer)
							IsCutscene = True
							BlinkTimer = -10
							IsCutscene = False
							If e\SoundCHN[0] <> 0 Then StopChannel e\SoundCHN[0]
							m_msg\DeathTxt = GetLocalStringR("Singleplayer", "cont_914_death", Designation)
						Case COARSE
							IsCutscene = True
							BlinkTimer = -10
							IsCutscene = False
							If e\EventState[0] - FPSfactor2 < 70 * 3 Then PlaySound_Strict Use914SFX
						Case ONETOONE
							IsCutscene = True
							BlinkTimer = -10
							IsCutscene = False
							If e\EventState[0] - FPSfactor2 < 70 * 3 Then PlaySound_Strict Use914SFX
						Case FINE, VERY_FINE
							IsCutscene = True
							BlinkTimer = -10
							IsCutscene = False
							If e\EventState[0] - FPSfactor2 < 70 * 3 Then PlaySound_Strict Use914SFX	
					End Select
				End If
			EndIf
			
			If e\EventState[0] > (6 * 70) Then	
				RotateEntity(e\room\Objects[0], EntityPitch(e\room\Objects[0]), EntityYaw(e\room\Objects[0]), CurveAngle(0, EntityRoll(e\room\Objects[0]),10.0))
			Else
				RotateEntity(e\room\Objects[0], EntityPitch(e\room\Objects[0]), EntityYaw(e\room\Objects[0]), 180)
			EndIf
			
			If e\EventState[0] > (12 * 70) Then							
				For it.Items = Each Items
					If it\collider <> 0 And it\Picked = False Then
						If DistanceSquared(EntityX(it\collider), EntityX(e\room\Objects[2], True), EntityZ(it\collider), EntityZ(e\room\Objects[2], True)) < PowTwo(180.0 * RoomScale) Then
							Use914(it, setting, EntityX(e\room\Objects[3], True), EntityY(e\room\Objects[3], True), EntityZ(e\room\Objects[3], True))
							
						End If
					End If
				Next
				
				If DistanceSquared(EntityX(Collider), EntityX(e\room\Objects[2], True), EntityZ(Collider), EntityZ(e\room\Objects[2], True)) < PowTwo(160.0 * RoomScale) Then
					Select setting
						Case COARSE
							;Injuries = 4.0
							DamageSPPlayer(90, True)
							CreateMsg(GetLocalString("Singleplayer", "cont_914_1"))
						Case ONETOONE
							InvertMouse = (Not InvertMouse)
						Case FINE, VERY_FINE
							SuperMan = True
					End Select
					BlurTimer = 1000
					PositionEntity(Collider, EntityX(e\room\Objects[3], True), EntityY(e\room\Objects[3], True) + 1.0, EntityZ(e\room\Objects[3], True))
					ResetEntity(Collider)
					DropSpeed = 0
				EndIf								
				
				e\room\RoomDoors[0]\open = True
				e\room\RoomDoors[1]\open = True
				RotateEntity(e\room\Objects[0], 0, 0, 0)
				e\eventstate[0] = 0
				
				Local opensfx914 = LoadTempSound("SFX\SCP\914\DoorOpen.ogg")
				e\room\RoomDoors[0]\SoundCHN = PlaySound2(opensfx914, Camera, e\room\RoomDoors[0]\obj)
				e\room\RoomDoors[1]\SoundCHN = PlaySound2(opensfx914, Camera, e\room\RoomDoors[1]\obj)
			End If
		End If
		
	EndIf
	UpdateSoundOrigin(e\SoundCHN[0],Camera,e\room\Objects[1])
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D