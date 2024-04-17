
Function FillRoom_Cont_372_914(r.Rooms)
	Local d.Doors,lt.LightTemplates,tw.TempWayPoints
	Local newlt,tex,it.Items
	Local b,i
	
	Local ElevatorOBJ = LoadRMesh("GFX\map\Elevators\elevator.rmesh",Null)
	HideEntity ElevatorOBJ
	r\Objects[6] = CopyEntity(ElevatorOBJ,r\obj)
	
	PositionEntity(r\Objects[6],1582,0,0)
	EntityType r\Objects[6],HIT_MAP
	EntityPickMode r\Objects[6],2
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x +1262.0 * RoomScale, r\y, r\z, 90, r, True, DOOR_ELEVATOR_3FLOOR, False, "", 2)
	r\RoomDoors[0]\DisableWaypoint = True
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x+336.0*RoomScale, 0.0, r\z,90,r,False,DOOR_OFFICE)
	
	CreateNewElevator(r\Objects[6],3,r\RoomDoors[0],2,r,-9784.0,-6784.0,0.0)
	
	Local rt.RoomTemplates = New RoomTemplates
	rt\objPath = "GFX\map\rooms\cont_372_914\cont_914_372_down.rmesh"
	rt\obj = LoadRMesh(rt\objPath,rt)
	r\Objects[4] = rt\obj
	ScaleEntity(r\Objects[4], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[4], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[4], 2), 2)
	
	PositionEntity(r\Objects[4],r\x,r\y,r\z)
	EntityParent r\Objects[4],r\obj
	
	For lt.LightTemplates = Each LightTemplates
		If lt\roomtemplate = rt Then
			newlt = AddLight(r, r\x+lt\x, r\y+lt\y, r\z+lt\z, lt\ltype, lt\range, lt\r, lt\g, lt\b, r\Objects[4])
			If newlt <> 0 Then
				If lt\ltype = 3 Then
					LightConeAngles(newlt, lt\innerconeangle, lt\outerconeangle)
					RotateEntity(newlt, lt\pitch, lt\yaw, 0)
				EndIf
			EndIf
		EndIf
	Next
	Delete rt
	rt.RoomTemplates = New RoomTemplates
	rt\objPath = "GFX\map\rooms\cont_372_914\cont_914_372_down_2.rmesh"
	rt\obj = LoadRMesh(rt\objPath,rt)
	r\Objects[5] = rt\obj
	ScaleEntity(r\Objects[5], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[5], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[5], 2), 2)
	
	PositionEntity(r\Objects[5],r\x,r\y,r\z)
	EntityParent r\Objects[5],r\obj
	
	For lt.LightTemplates = Each LightTemplates
		If lt\roomtemplate = rt Then
			newlt = AddLight(r, r\x+lt\x, r\y+lt\y, r\z+lt\z, lt\ltype, lt\range, lt\r, lt\g, lt\b, r\Objects[5], 1+8)
			If newlt <> 0 Then
				If lt\ltype = 3 Then
					LightConeAngles(newlt, lt\innerconeangle, lt\outerconeangle)
					RotateEntity(newlt, lt\pitch, lt\yaw, 0)
				EndIf
			EndIf
		EndIf
	Next
	Delete rt
	
	; ~ SCP-914
	
	CreateScreen(r\x + 1651.0 * RoomScale, r\y - 6594.0 * RoomScale, r\z + 2506.0 * RoomScale, "914.jpg", r) ;Manually adding the screen
	
	r\RoomDoors[6] = CreateDoor(r\zone,r\x+640.0*RoomScale,r\y-6786.0*RoomScale,r\z+3144*RoomScale,0,r,False,DOOR_CONTAINMENT)
	r\RoomDoors[6]\dir=1 : r\RoomDoors[6]\AutoClose=False : r\RoomDoors[6]\open=False
	FreeEntity r\RoomDoors[6]\buttons[0] : r\RoomDoors[6]\buttons[0] = 0
	FreeEntity r\RoomDoors[6]\buttons[1] : r\RoomDoors[6]\buttons[1] = 0
	
	r\Objects[0] = LoadMesh_Strict("GFX\map\rooms\cont_372_914\914_key.b3d")
	r\Objects[1] = LoadMesh_Strict("GFX\map\rooms\cont_372_914\914_knob.b3d")
	r\Objects[9] = LoadAnimMesh_Strict("GFX\map\rooms\cont_372_914\914_blinds.b3d")
	ScaleEntity r\Objects[9],RoomScale,RoomScale,RoomScale
	MeshCullBox (r\Objects[9], -MeshWidth(r\Objects[9]), -MeshHeight(r\Objects[9]), -MeshDepth(r\Objects[9]), MeshWidth(r\Objects[9])*2, MeshHeight(r\Objects[9])*2, MeshDepth(r\Objects[9])*2) 
	PositionEntity(r\Objects[9], r\x+1340.0 * RoomScale, r\y -6437.0 * RoomScale, r\z + 3206.0 * RoomScale)
	EntityParent r\Objects[9],r\obj
	EntityType r\Objects[9],HIT_MAP
	
	For  i% = 0 To 1
		ScaleEntity(r\Objects[i], RoomScale, RoomScale, RoomScale)
		EntityPickMode(r\Objects[i], 2)
	Next
	
	r\Levers[1] = CreateLever(r, r\x +1484.0 * RoomScale, r\y -6594.0 * RoomScale, r\z +2514 * RoomScale,180,True)
	r\Levers[0] = CreateLever(r, r\x +1771.0 * RoomScale, r\y -6594.0 * RoomScale, r\z +2514 * RoomScale,180,True)
	
	PositionEntity (r\Objects[0], r\x+671.0 * RoomScale, r\y -6596.0 * RoomScale, r\z + 3864.0 * RoomScale)
	PositionEntity (r\Objects[1], r\x+639.0 * RoomScale, r\y -6556.0 * RoomScale, r\z + 3864.0 * RoomScale)
	EntityParent(r\Objects[0], r\obj)
	EntityParent(r\Objects[1], r\obj)
	
	d = CreateDoor(r\zone, r\x +12.0 * RoomScale, r\y -6786 * RoomScale, r\z +4016.0 * RoomScale, 180, r, True)
	FreeEntity d\obj2 : d\obj2 = 0
	FreeEntity d\buttons[0] : d\buttons[0] = 0
	FreeEntity d\buttons[1] : d\buttons[1] = 0
	r\RoomDoors[4] = d: d\AutoClose = False
	
	d = CreateDoor(r\zone, r\x +1455.0 * RoomScale, r\y -6786 * RoomScale, r\z +4016.0 * RoomScale, 180, r, True)
	FreeEntity d\obj2 : d\obj2=0	
	FreeEntity d\buttons[0] : d\buttons[0] = 0
	FreeEntity d\buttons[1] : d\buttons[1] = 0
	r\RoomDoors[5] = d : d\AutoClose = False
	
	d = CreateDoor(r\zone, r\x +1217.0 * RoomScale, r\y -6786 * RoomScale, r\z +2658.0 * RoomScale, 90, r, False,False,KEY_CARD_2)
	PositionEntity (d\buttons[1], r\x + 1100.0 * RoomScale,  EntityY(d\buttons[1],True), r\z +2498.0 * RoomScale, True)
	TurnEntity(d\buttons[1], 0, -90, 0)
	
	r\Objects[2] = CreatePivot()
	r\Objects[3] = CreatePivot()
	PositionEntity(r\Objects[2], r\x -70.0 * RoomScale, r\y -6772 * RoomScale, r\z +4135.0 * RoomScale)
	PositionEntity(r\Objects[3], r\x +1451.0 * RoomScale, r\y -6772 * RoomScale, r\z +4144.0 * RoomScale)
	EntityParent(r\Objects[2], r\obj)
	EntityParent(r\Objects[3], r\obj)
	
	;it = CreateItem("Note", "paper", r\x +1600.0 * RoomScale, r\y -6550.0 * RoomScale, r\z + 3686.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","scope_bat"), "scopebat", r\x +1600.0 * RoomScale, r\y -6550.0 * RoomScale, r\z + 3686.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","first_aid"), "firstaid", r\x + 1607.0 * RoomScale, r\y -6640.0 * RoomScale, r\z + 3597.0 * RoomScale)
	EntityParent(it\collider, r\obj) : RotateEntity(it\collider, 0, 90, 0)
	
	;it = CreateItem("Dr. L's Note", "paper", r\x +1543.0 * RoomScale,r\y -6670.0 * RoomScale, r\z +3468.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	; ~ SCP-372
	
	;it = CreateItem("Document SCP-372", "paper", r\x -325.0 * RoomScale, r\y -9660.0 * RoomScale, r\z + 424.0 * RoomScale)
	;RotateEntity it\collider, 0, r\angle, 0
	;EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","radio"), "radio", r\x -522.0 * RoomScale, r\y -9620.0 * RoomScale, r\z -235.0 * RoomScale)
	it\state = 80.0
	EntityParent(it\collider, r\obj)
	
	d = CreateDoor(r\zone,r\x-128*RoomScale,r\y-9786*RoomScale,r\z+176.0*RoomScale,-90,r,False,True,KEY_CARD_2)
	d\AutoClose = False : d\open = False
	PositionEntity (d\buttons[0], r\x + 152.0 * RoomScale, EntityY(d\buttons[0],True), r\z + 601.0 * RoomScale, True)
	PositionEntity (d\buttons[1], r\x -448.0 * RoomScale, EntityY(d\buttons[1],True), r\z + 84.0 * RoomScale, True)
	TurnEntity(d\buttons[1], 0, -90, 0)
	
End Function

Function UpdateEvent_Cont_372_914(e.Events)
	Local r.Rooms,ne.NewElevator
	Local lt.LightTemplates, newlt
	Local it.Items
	Local angle#
	Local b,i
	
	If PlayerRoom = e\room
		
		If (Not Curr173\Contained) Then
			If gopt\GameMode <> GAMEMODE_NTF Then
				If PlayerInNewElevator Then
					Curr173\Idle = SCP173_DISABLED
					HideEntity Curr173\obj
					HideEntity Curr173\obj2
					HideEntity Curr173\Collider
				Else
					Curr173\Idle = SCP173_ACTIVE
					If EntityHidden(Curr173\obj) Then ShowEntity Curr173\obj
					If EntityHidden(Curr173\obj2) Then ShowEntity Curr173\obj2
					If EntityHidden(Curr173\Collider) Then ShowEntity Curr173\Collider
				EndIf
			EndIf
		EndIf
		
		If EntityY(Collider)<-7500.0*RoomScale ; ~ Containment SCP-372
			
			If (Not PlayerInNewElevator)
				ShouldPlay =MUS_SEWERS
			EndIf
			
			If (Not PlayerInNewElevator)
				PositionEntity e\room\RoomDoors[b]\frameobj,EntityX(e\room\RoomDoors[b]\frameobj),-9784.0*RoomScale,EntityZ(e\room\RoomDoors[b]\frameobj)
				PositionEntity e\room\RoomDoors[b]\obj,EntityX(e\room\RoomDoors[b]\obj),-9784.0*RoomScale,EntityZ(e\room\RoomDoors[b]\obj)
				PositionEntity e\room\RoomDoors[b]\obj2,EntityX(e\room\RoomDoors[b]\obj2),-9784.0*RoomScale,EntityZ(e\room\RoomDoors[b]\obj2)
				PositionEntity e\room\RoomDoors[b]\buttons[0],EntityX(e\room\RoomDoors[b]\buttons[0]),-9784.0*RoomScale+0.6,EntityZ(e\room\RoomDoors[b]\buttons[0])
				PositionEntity e\room\RoomDoors[b]\buttons[1],EntityX(e\room\RoomDoors[b]\buttons[1]),-9784.0*RoomScale+0.7,EntityZ(e\room\RoomDoors[b]\buttons[1])
				For ne = Each NewElevator
					If ne\door = e\room\RoomDoors[b]
						If ne\currfloor = 1 And ne\state = 0.0
							e\room\RoomDoors[b]\open = True
						Else
							e\room\RoomDoors[b]\open = False
						EndIf
					EndIf
				Next
			EndIf
			CameraFogRange Camera,1,6
			CameraFogColor Camera,5,20,3
			CameraClsColor Camera,5,20,3
			CameraRange Camera,0.01,7
			PlayerFallingPickDistance = 10.0
		ElseIf EntityY(Collider)<-5000.0*RoomScale ; ~ Containment SCP-914
			
			If (Not PlayerInNewElevator)
				ShouldPlay = MUS_ROOM_MT
			EndIf
			If (Not PlayerInNewElevator)
				PositionEntity e\room\RoomDoors[b]\frameobj,EntityX(e\room\RoomDoors[b]\frameobj),-6784.0*RoomScale,EntityZ(e\room\RoomDoors[b]\frameobj)
				PositionEntity e\room\RoomDoors[b]\obj,EntityX(e\room\RoomDoors[b]\obj),-6784.0*RoomScale,EntityZ(e\room\RoomDoors[b]\obj)
				PositionEntity e\room\RoomDoors[b]\obj2,EntityX(e\room\RoomDoors[b]\obj2),-6784.0*RoomScale,EntityZ(e\room\RoomDoors[b]\obj2)
				PositionEntity e\room\RoomDoors[b]\buttons[0],EntityX(e\room\RoomDoors[b]\buttons[0]),-6784.0*RoomScale+0.6,EntityZ(e\room\RoomDoors[b]\buttons[0])
				PositionEntity e\room\RoomDoors[b]\buttons[1],EntityX(e\room\RoomDoors[b]\buttons[1]),-6784.0*RoomScale+0.7,EntityZ(e\room\RoomDoors[b]\buttons[1])
				For ne = Each NewElevator
					If ne\door = e\room\RoomDoors[b]
						If ne\currfloor = 2 And ne\state = 0.0
							e\room\RoomDoors[b]\open = True
						Else
							e\room\RoomDoors[b]\open = False
						EndIf
					EndIf
				Next
			EndIf
			
		Else ; ~ Exit
			
			If (Not PlayerInNewElevator)
				PositionEntity e\room\RoomDoors[b]\frameobj,EntityX(e\room\RoomDoors[b]\frameobj),0.0,EntityZ(e\room\RoomDoors[b]\frameobj)
				PositionEntity e\room\RoomDoors[b]\obj,EntityX(e\room\RoomDoors[b]\obj),0.0,EntityZ(e\room\RoomDoors[b]\obj)
				PositionEntity e\room\RoomDoors[b]\obj2,EntityX(e\room\RoomDoors[b]\obj2),0.0,EntityZ(e\room\RoomDoors[b]\obj2)
				PositionEntity e\room\RoomDoors[b]\buttons[0],EntityX(e\room\RoomDoors[b]\buttons[0]),0.0+0.6,EntityZ(e\room\RoomDoors[b]\buttons[0])
				PositionEntity e\room\RoomDoors[b]\buttons[1],EntityX(e\room\RoomDoors[b]\buttons[1]),0.0+0.7,EntityZ(e\room\RoomDoors[b]\buttons[1])
				For ne = Each NewElevator
					If ne\door = e\room\RoomDoors[b]
						If ne\currfloor = 3 And ne\state = 0.0
							e\room\RoomDoors[b]\open = True
						Else
							e\room\RoomDoors[b]\open = False
						EndIf
					EndIf
				Next
			EndIf
		EndIf
	EndIf
	
	; ~ SCP-914 Event
	
	If PlayerRoom = e\room Then
		
		If e\room\RoomDoors[6]\openstate = 0 Lor e\room\RoomDoors[6]\openstate = 180 Then
			UpdateLever(e\room\Levers[0]\obj)
		EndIf
		
		If EntityPitch(e\room\Levers[0]\obj,True) >= 80 Then ; ~ Cont Door Open
			If e\room\RoomDoors[6]\open = True
				UseDoor(e\room\RoomDoors[6])
			EndIf
		ElseIf EntityPitch(e\room\Levers[0]\obj,True) <= -80 Then  ; ~ Cont Door Closed
			If e\room\RoomDoors[6]\open = False
				UseDoor(e\room\RoomDoors[6])
			EndIf
		EndIf
		
		UpdateLever(e\room\Levers[1]\obj)
		
		If EntityPitch(e\room\Levers[1]\obj,True) >= 50 Then ; ~ Blinds Open
			Animate2(e\room\Objects[9],AnimTime(e\room\Objects[9]),1,80,0.5,False)
		ElseIf EntityPitch(e\room\Levers[1]\obj,True) <= -50 Then  ; ~ Blinds Closed
			Animate2(e\room\Objects[9],AnimTime(e\room\Objects[9]),80,1,-0.5,False)
		EndIf
		
		If e\room\RoomDoors[6]\open
			GiveAchievement(Achv914)
			e\EventState[1]=1
		EndIf
		
		If e\EventState[1]=1 And EntityY(Collider)<-5000.0*RoomScale And (Not EntityY(Collider)<-7500.0*RoomScale) Then
			ShouldPlay = MUS_CONT_914
		EndIf
		
		EntityPick(Camera, 1.0)
		If PickedEntity() = e\room\Objects[0] Then
			DrawHandIcon = True
			If KeyHitUse Then GrabbedEntity = e\room\Objects[0]
		ElseIf PickedEntity() = e\room\Objects[1]
			DrawHandIcon = True
			If KeyHitUse Then GrabbedEntity = e\room\Objects[1]
		EndIf
		
		If KeyDownUse Lor KeyHitUse Then
			If GrabbedEntity <> 0 Then ;avain
				If GrabbedEntity = e\room\Objects[0] Then
					If e\EventState[0] = 0 Then
						DrawHandIcon = True
						TurnEntity(GrabbedEntity, 0, 0, -Mouse_X_Speed_1 * 2.5)
						
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
											e\EventState[0] = 1
											e\SoundCHN[0] = PlaySound2(MachineSFX, Camera, e\room\Objects[1])
											e\room\RoomDoors[5]\SoundCHN = PlaySound2(LoadTempSound("SFX\SCP\914\DoorClose.ogg"), Camera, e\room\RoomDoors[5]\obj)
											Exit
										EndIf
									End If
								End If
							Next
						EndIf
					End If
				ElseIf GrabbedEntity = e\room\Objects[1]
					If e\EventState[0] = 0 Then
						DrawHandIcon = True
						TurnEntity(GrabbedEntity, 0, 0, -Mouse_X_Speed_1 * 2.5)
						
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
			End If
		Next
		
		If e\EventState[0] > 0 Then
			e\EventState[0] = e\EventState[0] + FPSfactor
			
			
			e\room\RoomDoors[5]\open = False
			If e\EventState[0] > 70 * 2 Then
				If e\room\RoomDoors[4]\open=True Then
					e\room\RoomDoors[4]\SoundCHN = PlaySound2(LoadTempSound("SFX\SCP\914\DoorClose.ogg"), Camera, e\room\RoomDoors[4]\obj)
				EndIf
				
				e\room\RoomDoors[4]\open = False
			EndIf
			
			If DistanceSquared(EntityX(Collider), EntityX(e\room\Objects[2], True), EntityZ(Collider), EntityZ(e\room\Objects[2], True)) < PowTwo(170.0 * RoomScale) Then
				
				If setting = ROUGH Lor setting = COARSE Then
					If e\EventState[0] > 70 * 2.6 And e\EventState[0] - FPSfactor2 < 70 * 2.6 Then PlaySound_Strict Death914SFX
				EndIf
				
				If e\EventState[0] > 70 * 3 Then
					Select setting
						Case ROUGH
							KillTimer = Min(-1, KillTimer)
							BlinkTimer = -10
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
							DamageSPPlayer(90, True)
							CreateMsg(GetLocalString("Singleplayer", "cont_914_1"))
						Case ONETOONE
							InvertMouse = (Not InvertMouse)
						Case FINE
							SuperMan = True
						Case VERY_FINE
							HealSPPlayer(20)
							SuperMan = True
					End Select
					BlurTimer = 1000
					PositionEntity(Collider, EntityX(e\room\Objects[3], True), EntityY(e\room\Objects[3], True) + 1.0, EntityZ(e\room\Objects[3], True))
					ResetEntity(Collider)
					DropSpeed = 0
				EndIf								
				
				e\room\RoomDoors[4]\open = True
				e\room\RoomDoors[5]\open = True
				RotateEntity(e\room\Objects[0], 0, 0, 0)
				e\EventState[0] = 0
				
				Local opensfx914 = LoadTempSound("SFX\SCP\914\DoorOpen.ogg")
				e\room\RoomDoors[4]\SoundCHN = PlaySound2(opensfx914, Camera, e\room\RoomDoors[4]\obj)
				e\room\RoomDoors[5]\SoundCHN = PlaySound2(opensfx914, Camera, e\room\RoomDoors[5]\obj)
			End If
		End If
		
	EndIf
	UpdateSoundOrigin(e\SoundCHN[0],Camera,e\room\Objects[1])
	
	; ~ SCP-372 Event
	
	If PlayerRoom = e\room Then
		If e\EventState[3] = 0 Then
			If EntityY(Collider)<-7500.0*RoomScale And (Not PlayerInNewElevator) Then
				PlaySound_Strict(RustleSFX[Rand(0,2)])
				CreateNPC(NPC_scp_372, -933*RoomScale, -9700*RoomScale, -187*RoomScale)
				e\EventState[3] = 1
				;RemoveEvent(e)
			EndIf					
		EndIf
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D