Function FillRoom_Cont_409(r.Rooms)
	Local it.Items,de.Decals
	
	; ~ A door to the containment chamber	
	
	r\RoomDoors[2] = CreateDoor(r\zone, r\x + 108.0 * RoomScale, r\y - 2495.0 * RoomScale, r\z + 2958.0 * RoomScale, 0.0, r, False, DOOR_HCZ, KEY_CARD_4)
	
	r\Objects[1] = CreatePivot()
	PositionEntity r\Objects[1],r\x-500*RoomScale,r\y-2538*RoomScale,r\z+3509*RoomScale,True
	EntityParent r\Objects[1],r\obj
	
	r\Objects[2] = CreatePivot()
	PositionEntity r\Objects[2],r\x+179*RoomScale,r\y-2506*RoomScale,r\z+4948*RoomScale,True
	EntityParent r\Objects[2],r\obj
	
	r\Objects[3] = CreatePivot()
	PositionEntity r\Objects[3],r\x+153*RoomScale,r\y-2403*RoomScale,r\z+4142*RoomScale,True ; ~ SCP - 409 
	EntityParent r\Objects[3],r\obj
	
	r\Objects[4] = CreatePivot()
	PositionEntity r\Objects[4],r\x+423*RoomScale,r\y-2517*RoomScale,r\z+4945*RoomScale,True
	EntityParent r\Objects[4],r\obj
	
	r\Objects[5] = LoadAnimMesh_Strict("GFX\map\rooms\cont_409\cont_409_stand.b3d")
	PositionEntity r\Objects[5],r\x+156*RoomScale,r\y-2500*RoomScale,r\z+4186*RoomScale,True ; ~ SCP - 409 Stand
	ScaleEntity r\Objects[5],RoomScale,RoomScale,RoomScale
	RotateEntity r\Objects[5],0,180,0
	EntityParent r\Objects[5],r\obj
	HideEntity r\Objects[5]
	
	r\Objects[6] = CreatePivot()
	PositionEntity r\Objects[6],r\x+182*RoomScale,r\y-2494*RoomScale,r\z+4945*RoomScale,True ; ~ Trigger
	EntityParent r\Objects[6],r\obj
	
	;it.Items = CreateItem(GetLocalString("Item Names","doc_409"), "paper", r\x + 74.0 * RoomScale, r\y - 2469.0 * RoomScale, r\z + 5518.0 * RoomScale)
	;RotateEntity(it\collider, 0.0, 0.0, 0.0)
	;EntityParent(it\collider, r\obj)
	
	;it.Items = CreateItem(GetLocalString("Item Names","doc_409_inf"), "paper", r\x - 340.0 * RoomScale, r\y - 2582.0 * RoomScale, r\z + 3750.0 * RoomScale)
	;RotateEntity(it\collider, 0.0, 0.0, 0.0)
	;EntityParent(it\collider, r\obj)
	
	If I_005\ChanceToSpawn = 3 Then
		it.Items = CreateItem("SCP-005", "scp005", r\x +49.0 * RoomScale, r\y - 2539.0 * RoomScale, r\z + 4050.0 * RoomScale)
		EntityParent(it\collider, r\obj)
	EndIf
	
End Function

Function UpdateEvent_Cont_409(e.Events)
	Local p.Particles,i
	
	If PlayerRoom = e\room And gopt\GameMode = GAMEMODE_DEFAULT Then
		
		ShowEntity e\room\Objects[5]
			
		If TaskExists(TASK_FIND_409) Then
			EndTask(TASK_FIND_409)
			If (Not TaskExists(TASK_CLOSE_409)) Then
				BeginTask(TASK_CLOSE_409)
			EndIf
		EndIf
		
		If TaskExists(TASK_CLOSE_409) Then
			If e\EventState[1] = 0 Then
				If EntityDistanceSquared(Collider, e\room\Objects[6])<PowTwo(1.0) Then
					DrawHandIcon = True
					If KeyHitUse Then
						e\EventState[1] = 1
					EndIf
				EndIf
			EndIf
		EndIf
		
		If e\EventState[1] > 0 Then
			e\EventState[2]=e\EventState[2]+FPSfactor
		EndIf
		
		If e\EventState[2] > 0 And e\EventState[2] < 70*0.03125 Then
			PlaySound_Strict(LoadTempSound("SFX\General\stand_move.ogg"))
		EndIf
		
		If e\EventState[2] > 0 And e\EventState[2] < 70*7 Then
			Animate2(e\room\Objects[5],AnimTime(e\room\Objects[5]),1,140,0.3,False)
		ElseIf e\EventState[2] = 0 Then
			SetAnimTime(e\room\Objects[5],1)
		Else
			SetAnimTime(e\room\Objects[5],140)
			EndTask(TASK_CLOSE_409)
			ecst\Contained409 = True
		EndIf
		
		ShouldPlay = MUS_CONT_409
		
		If e\EventState[0] = 0.0 Then
			
			e\Sound[0] = LoadSound_Strict("SFX\General\Spark_Short.ogg")
			
			If e\room\NPC[0] = Null Then
				e\room\NPC[0] = CreateNPC(NPC_Elias,EntityX(e\room\Objects[1],True),EntityY(e\room\Objects[1],True),EntityZ(e\room\Objects[1],True))
				RotateEntity e\room\NPC[0]\Collider,0,e\room\angle+180,0
				e\room\NPC[0]\State[0] = Z_STATE_LYING
			EndIf
			
			e\EventState[0] = 1.0
		ElseIf e\EventState[0] = 1.0 Then 
			
			If e\room\NPC[0] <> Null Then
				If e\room\NPC[0]\HP > 0 Then
					If EntityDistanceSquared(Collider, e\room\NPC[0]\Collider)<PowTwo(2.0) Then
						If e\room\NPC[0]\State[0] = 0 Then
							e\room\NPC[0]\State[0] = Z_STATE_STANDUP
						EndIf
					EndIf
				EndIf
			EndIf
			
			If I_409\Timer = 0.0 Then
				; ~ Touching the SCP-409
				If e\EventState[2] = 0 Then
					If EntityDistanceSquared(Collider, e\room\Objects[5])<PowTwo(0.64) Then
						DrawHandIcon = True
						If KeyHitUse Then
							CreateMsg(GetLocalString("Items","scp409_1"))
							BlurTimer = 2000.0
							I_409\Timer = 0.001
							GiveAchievement(Achv409)
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf
		
		If Rand(50) = 1 Then
			PlaySound2(e\Sound[0], Camera, e\room\Objects[4], 3.0, 0.4)
			If ParticleAmount > 0 Then
				For i = 0 To (2 + (1 * (ParticleAmount - 1)))
					p.Particles = CreateParticle(EntityX(e\room\Objects[4],True), EntityY(e\room\Objects[4],True)+Rnd(0.0,0.05), EntityZ(e\room\Objects[4],True),7, 0.002, 0.0, 25.0)
					p\speed = Rnd(0.005, 0.03) : p\size = Rnd(0.005, 0.0075) : p\Achange = -0.05
					RotateEntity(p\pvt, Rnd(-20.0, 0.0), e\room\angle, 0.0)
					ScaleSprite(p\obj, p\size, p\size)
				Next
			EndIf	
		EndIf
	Else
		HideEntity e\room\Objects[5]
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D