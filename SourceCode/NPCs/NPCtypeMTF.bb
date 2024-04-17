
; ~ MTF Unit constants

;[Block]
Const MTF_FOLLOWPLAYER = 0
Const MTF_INTRO = 1
Const MTF_CONTAIN173 = 2
Const MTF_CONTACT = 3
Const MTF_FLEE = 4
Const MTF_SPLIT_UP = 5
Const MTF_SEARCH = 6
Const MTF_HEAL = 7
Const MTF_TOTARGET = 8
Const MTF_TESLA = 9
Const MTF_TARGET_PLAYER = 10

; ~ Containment constants

Const MTF_GOTOSCP = 1

; ~ MTF Designation constants

Const MTF_UNIT_DEFAULT = 0
Const MTF_UNIT_REGULAR = 1
Const MTF_UNIT_MEDIC = 2
Const MTF_UNIT_COMMANDER = 3
;[End Block]

Function CreateNPCtypeNTF(n.NPCs)
	Local temp#, random%
	
	n\NPCName = "MTF"
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.175, 0.2
	EntityType n\Collider, HIT_PLAYER
	n\obj = CopyEntity(NPCModel[Model_NTF])
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "MTF", "speed") / 100.0)
	temp# = (GetINIFloat("DATA\NPCs.ini", "MTF", "scale") / 2.5)
	ScaleEntity n\obj, temp, temp, temp
	MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2) 
	
	random = Rand(0,100)
	
	If random > 0 And random < 50 Then
		SwitchNPCGun%(n, GUN_P90)
	ElseIf random > 50 And random < 75 Then
		SwitchNPCGun%(n, GUN_MP5)
	ElseIf random > 75 Then
		SwitchNPCGun%(n, GUN_MP7)
	EndIf
	
	n\HP = 550-(75*SelectedDifficulty\OtherFactors)
	
	n\BlinkTimer = 70.0*Rnd(5,8)
	
	n\Clearance = 3
End Function

Function UpdateNPCtypeNTF(n.NPCs)
	Local n2.NPCs,w.WayPoints,e.Events,r.Rooms
	Local prevFrame# = n\Frame
	Local SeePlayer% = False
	Local SeeNPC%, VoiceLine$, SmallestNPCDist#, CurrentNPCDist#
	Local PlayerDistSquared# = EntityDistanceSquared(n\Collider,Collider)
	Local WayPDist#
	Local WaypointDist# = 0.0
	Local temp% = False
	Local sfxstep% = 0
	Local IsPushable% = False
	Local i%
	Local prevX#, prevZ#
	Local pvt%
	Local v3d_1.Vector3D, v3d_2.Vector3D
	
	If PlayerRoom\RoomTemplate\Name = "pocketdimension" Lor (IsZombie And n\State[0] <> MTF_TARGET_PLAYER) Then
		PositionEntity n\Collider, 0, -500, 0
		ResetEntity n\Collider
		Return
	EndIf
	
	n\BlinkTimer = Max(n\BlinkTimer-FPSfactor,0.0)
	If n\BlinkTimer = 0.0 Then
		If n\State[0] = MTF_CONTAIN173 Then
			If n\PrevState = MTF_UNIT_MEDIC Then
				PlayMTFSound(LoadTempSound("SFX\Character\MTF\173\Medic_Blinking"+Rand(1,3)+".ogg"),n,True)
			ElseIf n\PrevState = MTF_UNIT_REGULAR Then
				PlayMTFSound(LoadTempSound("SFX\Character\MTF\173\Regular_Blinking"+Rand(1,3)+".ogg"),n,True)
			Else
				PlayMTFSound(LoadTempSound("SFX\Character\MTF\173\Commander_Blinking.ogg"),n,True)
			EndIf
		EndIf
		n\BlinkTimer = 70.0*Rnd(5,8)
	EndIf
	
	If IsNPCStuck(n, 70.0*5) Then
		n\PathStatus = 0
		If n\State[0] = MTF_FOLLOWPLAYER Then
			v3d_1 = CreateVector3D(962, 1259, 0.3)
			v3d_2 = CreateVector3D(1496, 1524, 0.3)
			NPC_GoTo(n, v3d_1, v3d_2, Collider, 0.7)
			Delete v3d_1
			Delete v3d_2
		ElseIf n\State[0] = MTF_SPLIT_UP Then
			v3d_1 = CreateVector3D(962, 1259, 0.3)
			v3d_2 = CreateVector3D(1496, 1524, 0.3)
			NPC_GoToRoom(n, v3d_1, v3d_2, 0.7)
			Delete v3d_1
			Delete v3d_2
		EndIf
	EndIf
	
	n\Reload = Max(n\Reload-FPSfactor,0.0)
	n\PathTimer = Max(n\PathTimer-FPSfactor,0.0)
	
	If n\HP > 0 Then
		If n\State[0] = MTF_FOLLOWPLAYER Lor n\State[0] = MTF_HEAL Lor n\State[0] = MTF_SEARCH Then
			;Spotting other NPCs
			SmallestNPCDist# = 0.0 : CurrentNPCDist# = 0.0
			n\Target = Null
			For n2 = Each NPCs
				If n2\NPCtype <> n\NPCtype And n2\HP > 0 And IsTarget(n, n2) Then
					SeeNPC = OtherNPCSeesMeNPC(n2, n)
					If SeeNPC And EntityVisible(n2\Collider, n\Collider) Then
						CurrentNPCDist = EntityDistanceSquared(n\Collider, n2\Collider)
						If SmallestNPCDist = 0.0 Lor SmallestNPCDist > CurrentNPCDist Then
							temp = True
							
							If n2\NPCtype = NPC_SCP_173 And Curr173\Idle > SCP173_STATIONARY Then
								temp = False
							EndIf
							
							If temp Then
								SmallestNPCDist = CurrentNPCDist
								n\Target = n2
							EndIf
						EndIf
					EndIf
				EndIf
			Next
			
			If n\Target <> Null Then
				If n\State[0] = MTF_SEARCH Then
					SetNPCFrame(n, 1383)
				EndIf
				n\State[0] = MTF_CONTACT
				VoiceLine = ""
				Select n\Target\NPCtype
					Case NPC_Class_D
						If n\PrevState = MTF_UNIT_MEDIC Then
							VoiceLine = "D-Class\Medic_Spotted" + Rand(1,6)
						ElseIf n\PrevState = MTF_UNIT_REGULAR Then
							VoiceLine = "D-Class\Regular_Spotted" + Rand(1,6)
						Else
							VoiceLine = "D-Class\Commander_Spotted" + Rand(1,5)
						EndIf
					Case NPC_Zombie, NPC_Zombie_Armed
						If n\PrevState = MTF_UNIT_MEDIC Then
							VoiceLine = "Zombie\Medic_049-2"
						ElseIf n\PrevState = MTF_UNIT_REGULAR Then
							VoiceLine = "Zombie\Regular_049-2"
						Else
							
						EndIf
					Case NPC_SCP_939
						;VoiceLine = "939\Spotted" ;Todo: Voice line required
					Case NPC_SCP_106
						If n\PrevState = MTF_UNIT_MEDIC Then
							If Rand(1,10) = 1 Then
								VoiceLine = "106\Medic_Extra1"
							Else	
								VoiceLine = "106\Medic_Spotted" + Rand(1,4)
							EndIf	
						ElseIf n\PrevState = MTF_UNIT_REGULAR Then
							VoiceLine = "106\Regular_Spotted" + Rand(1,4)
						Else
							VoiceLine = "106\Commander_Spotted" + Rand(1,4)
						EndIf
						n\State[0] = MTF_FLEE
					Case NPC_SCP_173
						If Curr173\Idle = SCP173_ACTIVE And Curr173\IdleTimer <= 0.0 Then
							If n\PrevState = MTF_UNIT_MEDIC Then
								VoiceLine = "173\Medic_Spotted" + Rand(1,4)
							ElseIf n\PrevState = MTF_UNIT_REGULAR Then
								VoiceLine = "173\Regular_Spotted" + Rand(1,4)
							Else
								VoiceLine = "173\Commander_Spotted" + Rand(1,3)
							EndIf
							Curr173\Idle = SCP173_STATIONARY
							Curr173\IdleTimer = 70*30
						EndIf
						n\State[0] = MTF_CONTAIN173
					Case NPC_SCP_049
						If n\PrevState = MTF_UNIT_MEDIC Then
							VoiceLine = "049\Medic_Spotted" + Rand(1,4)
						ElseIf n\PrevState = MTF_UNIT_REGULAR Then
							VoiceLine = "049\Regular_Spotted" + Rand(1,4)
						Else
							VoiceLine = "049\Commander_Spotted" + Rand(1,5)
						EndIf
						n\State[0] = MTF_FLEE
					Case NPC_SCP_096
						If n\PrevState = MTF_UNIT_MEDIC Then
							VoiceLine = "096\Medic_Spotted" + Rand(1,4)
						ElseIf n\PrevState = MTF_UNIT_REGULAR Then
							VoiceLine = "096\Regular_Spotted" + Rand(1,4)
						Else
							VoiceLine = "096\Commander_Spotted"
						EndIf
						n\State[0] = MTF_FLEE
				End Select
				If VoiceLine <> "" Then
					PlayMTFSound(LoadTempSound("SFX\Character\MTF\" + VoiceLine + ".ogg"), n, True)
				EndIf
			EndIf
		EndIf
		
		Select n\State[0]
			Case MTF_FOLLOWPLAYER, MTF_HEAL
				;[Block]
				If n\State[0] = MTF_HEAL And (psp\Health = 100 Lor n\PrevState <> MTF_UNIT_MEDIC) Then
					n\State[0] = MTF_FOLLOWPLAYER
				EndIf
				
				;Player can push MTF unit in this state
				IsPushable = True
				
				;Following the player
				If PlayerDistSquared <= PowTwo(8.0) Then
					If EntityVisible(n\Collider, Collider) Then
						SeePlayer = True
					EndIf
				EndIf
				If SeePlayer Then
					n\PathStatus = 0
					n\PathLocation = 0
					n\PathTimer = 0.0
					
					n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj, Collider)
					RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
					If EntityDistanceSquared(n\Collider, Collider) < PowTwo(1.0 - (0.5 * n\State[0] = MTF_HEAL)) Then
						If n\CurrSpeed <= 0.01 Then
							n\CurrSpeed = 0.0
							AnimateNPC(n, 962, 1259, 0.3)
							psp\Health = 100
							n\State[0] = MTF_FOLLOWPLAYER
						Else
							n\CurrSpeed = CurveValue(0.0,n\CurrSpeed,20.0)
							AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
						EndIf
					Else
						n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
						AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
					EndIf
				Else
					v3d_1 = CreateVector3D(962, 1259, 0.3)
					v3d_2 = CreateVector3D(1496, 1524, 0.3)
					NPC_GoTo(n, v3d_1, v3d_2, Collider, 0.7)
					Delete v3d_1
					Delete v3d_2
				EndIf
				
				If PlayerDistSquared > PowTwo(16) Then
					Local shortestDist# = 1000000.0
					Local shortestDistRoom% = -1
					For i = 0 To 3
						If PlayerRoom\Adjacent[i] <> Null Then
							Local currDist# = EntityDistanceSquared(n\Collider, PlayerRoom\Adjacent[i]\obj)
							If currDist < shortestDist And (Not EntityInView(PlayerRoom\Adjacent[i]\obj, Camera)) Then
								shortestDist = currDist
								shortestDistRoom = i
							EndIf
						EndIf
					Next
					
					If shortestDistRoom >= 0 Then
						TeleportEntity(n\Collider, PlayerRoom\Adjacent[shortestDistRoom]\x, PlayerRoom\Adjacent[shortestDistRoom]\y + 0.1, PlayerRoom\Adjacent[shortestDistRoom]\z, n\CollRadius)
						n\PathStatus = 0
						n\PathLocation = 0
						n\PathTimer = 0.0
					EndIf
				EndIf
				;[End Block]
			Case MTF_INTRO
				;[Block]
				If n\State[1] = 0 ;sitting
					AnimateNPC(n, 1525, 1623, 0.5)
				ElseIf n\State[1] = 1 ;aiming
					AnimateNPC(n, 346, 351, 0.2)
				ElseIf n\State[1] = 2 ;idle
					If Rand(400) = 1 Then
						n\Angle = n\Angle + Rnd(-30, 30)
					EndIf
					AnimateNPC(n, 962, 1259, 0.3)
					RotateEntity(n\Collider, 0, CurveAngle(n\Angle,EntityYaw(n\Collider),10.0), 0, True)
				EndIf
				;[End Block]
			Case MTF_CONTAIN173
				;[Block]
				AnimateNPC(n, 962, 1259, 0.3)
				
				n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj, Curr173\obj)
				RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
				
				mtfd\Enabled = False
				
				If Curr173\Idle <> SCP173_STATIONARY Then
					n\State[0] = MTF_FOLLOWPLAYER
				EndIf
				
				If Curr173\IdleTimer > 0.0 Then
					Curr173\Idle = SCP173_STATIONARY
					;Yes, the player's collider, LOL
					If EntityDistance(Curr173\Collider, Collider) < 15.0 And (BlinkTimer < - 16 Lor BlinkTimer > - 6) And (EntityInView(Curr173\obj, Camera) Lor EntityInView(Curr173\obj2, Camera)) Then
						Curr173\IdleTimer = Max(Curr173\IdleTimer - FPSfactor, 0)
						If Curr173\IdleTimer <= 0.0 Then
							mtfd\Enabled = True
							Curr173\Idle = SCP173_BOXED
							EndTask(TASK_NTF_CONTAIN_173)
							BeginTask(TASK_NTF_173_TO_CHAMBER)
							PlayPlayerSPVoiceLine("SFX\Player\Voice\Sanders\scp173containmentbox" + Rand(1, 2))
						EndIf
					Else
						Local isLooking% = False
						For n2 = Each NPCs
							If n2\NPCtype = n\NPCtype And n2\State[0] = MTF_CONTAIN173 And n2\BlinkTimer > 10.0 Then
								isLooking = True
								Exit
							EndIf
						Next
						If (Not isLooking) Then
							Curr173\Idle = SCP173_ACTIVE
							mtfd\Enabled = True
						EndIf
					EndIf
				EndIf
				;[End Block]
			Case MTF_CONTACT
				;[Block]
				If n\Target <> Null And n\Target\HP > 0 And n\Gun <> Null Then
					CurrentNPCDist = EntityDistanceSquared(n\Collider, n\Target\Collider)
					If CurrentNPCDist < PowTwo(6) ;And EntityVisible(n\Collider, n\Target\Collider) Then
						n\CurrSpeed = 0.0
						n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,n\Target\obj)
						RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
						If n\Frame = 1383 Then
							If n\Reload = 0 Then
								If n\Gun\MaxGunshotSounds = 1 Then
									PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"))
								Else
									PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"))
								EndIf
								pvt% = CreatePivot()
								
								RotateEntity(pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
								PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
								MoveEntity (pvt,0.8*0.079, 10.0*0.079, 6.0*0.079)
								
								ShootTarget(EntityX(pvt), EntityY(pvt), EntityZ(pvt), n, Rnd(0.4,0.8))
								n\Reload = 4.67 ;900 RPM
							EndIf
						Else
							If n\Frame > 1383 Then
								SetNPCFrame(n, 1375)
							EndIf
							AnimateNPC(n,1375,1383,0.2,False)
						EndIf
					Else
						v3d_1 = CreateVector3D(79, 309, 0.3)
						v3d_2 = CreateVector3D(488, 522, 0.3)
						NPC_GoTo(n, v3d_1, v3d_2, n\Target\Collider, 0.7)
						Delete v3d_1
						Delete v3d_2
					EndIf
				Else
					pvt = FreeEntity_Strict(pvt)
					n\IdleTimer = 70*10
					n\State[0] = MTF_SEARCH
					n\Target = Null
				EndIf
				;[End block]
			Case MTF_FLEE
				;[Block]
				If n\Target <> Null Then
					mtfd\Enabled = False
					CurrentNPCDist = EntityDistanceSquared(n\Collider, n\Target\Collider)
					If CurrentNPCDist < 70.0 Then
						If CurrentNPCDist < 4 And n\State[1] <= 0 Then
							n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
							MoveEntity n\Collider, 0, 0, -n\CurrSpeed * FPSfactor
							n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,n\Target\obj)
							RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
							AnimateNPC(n,488,522,-0.5)
						ElseIf CurrentNPCDist => 4 And n\State[1] <= 0 Then
							n\State[1] = 70*5
						Else
							n\State[1] = Max(n\State[1] - FPSfactor, 0)
							v3d_1 = CreateVector3D(79, 309, 0.3)
							v3d_2 = CreateVector3D(488, 522, 0.3)
							NPC_GoToRoom(n, v3d_1, v3d_2, 1.0)
							Delete v3d_1
							Delete v3d_2
						EndIf
					Else
						mtfd\Enabled = True
						n\State[0] = MTF_FOLLOWPLAYER
					EndIf
				Else
					mtfd\Enabled = True
					n\State[0] = MTF_FOLLOWPLAYER
				EndIf	
				;[End block]
			Case MTF_SEARCH	
				;[Block]
				v3d_1 = CreateVector3D(79, 309, 0.3)
				v3d_2 = CreateVector3D(488, 522, 0.3)
				NPC_GoToRoom(n, v3d_1, v3d_2, 0.5)
				Delete v3d_1
				Delete v3d_2
				
				If n\IdleTimer = 0.0 Then
					If n\PrevState = MTF_UNIT_MEDIC Then
						PlayMTFSound(LoadTempSound("SFX\Character\MTF\Medic_SearchComplete"+Rand(1,4)+".ogg"),n,True)
					Else
						PlayMTFSound(LoadTempSound("SFX\Character\MTF\Regular_SearchComplete"+Rand(1,4)+".ogg"),n,True)
					EndIf
					n\State[0] = MTF_FOLLOWPLAYER
				EndIf
				;[End Block]
			Case MTF_SPLIT_UP
				;[Block]
				v3d_1 = CreateVector3D(962, 1259, 0.3)
				v3d_2 = CreateVector3D(1496, 1524, 0.3)
				NPC_GoToRoom(n, v3d_1, v3d_2, 0.7)
				Delete v3d_1
				Delete v3d_2
				;[End block]
			Case MTF_TOTARGET
				;[Block]
				pvt = CreatePivot()
				PositionEntity pvt, n\EnemyX, n\EnemyY, n\EnemyZ
				If EntityDistanceSquared(n\Collider, pvt) < PowTwo(0.3) Then
					If n\CurrSpeed <= 0.01 Then
						n\CurrSpeed = 0.0
						AnimateNPC(n, 962, 1259, 0.3)
					Else
						n\CurrSpeed = CurveValue(0.0,n\CurrSpeed,20.0)
						AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
					EndIf
				Else
					n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,pvt)
					RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
					n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
					AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
					MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
				EndIf
				pvt = FreeEntity_Strict(pvt)
				;[End Block]
			Case MTF_TESLA
				;[Block]
				AnimateNPC(n, 1260, 1375, 0.25, False)
				If n\Frame = 1375 Then
					n\State[0] = MTF_FOLLOWPLAYER
				EndIf
				;[End Block]
			Case MTF_TARGET_PLAYER
				;[Block]
				If psp\Health > 0 And n\Gun <> Null Then
					n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,Collider)
					RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
					If n\Frame = 1383 Then
						If n\Reload = 0 Then
							If n\Gun\MaxGunshotSounds = 1 Then
								PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"))
							Else
								PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"))
							EndIf
							pvt% = CreatePivot()
							
							RotateEntity(pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
							PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
							MoveEntity (pvt,0.8*0.079, 10.0*0.079, 6.0*0.079)
							
							ShootPlayer(EntityX(pvt), EntityY(pvt), EntityZ(pvt), Rnd(0.4,0.8))
							n\Reload = 4.67 ;900 RPM
						EndIf
					Else
						If n\Frame > 1383 Then
							SetNPCFrame(n, 1375)
						EndIf
						AnimateNPC(n,1375,1383,0.2,False)
					EndIf
				Else
					AnimateNPC(n, 79, 309, 0.3)
				EndIf
				;[End Block]
			Case STATE_SCRIPT
				;[Block]
				
				;[End Block]
		End Select
		n\IdleTimer = Max(0.0, n\IdleTimer-FPSfactor)
	ElseIf n\HP = -1 Then
		EntityType n\Collider, HIT_DEAD
		If n\Frame > 21 Then
			SetNPCFrame(n, 27)
		Else
			AnimateNPC(n, 2, 27, 0.3)
		EndIf
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
	Else
		EntityType n\Collider, HIT_DEAD
		If n\Frame = 1407 Then
			SetNPCFrame(n, 1407)
		Else
			AnimateNPC(n, 1383, 1407, 0.05, False)
		EndIf
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
	EndIf
	
	If n\HP <= 0 Then
		mtfd\Enabled = False
	EndIf
	
	;Play step sounds
	If n\CurrSpeed > 0.01 Then
		If prevFrame > 500 And n\Frame<495 Then
			sfxstep = GetStepSound(n\Collider,n\CollRadius)
			PlaySound2(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(sfxstep*MaxStepSounds)], Camera, n\Collider, 8.0, Rnd(0.5,0.7))
		ElseIf prevFrame < 505 And n\Frame=>505
			sfxstep = GetStepSound(n\Collider,n\CollRadius)
			PlaySound2(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(sfxstep*MaxStepSounds)], Camera, n\Collider, 8.0, Rnd(0.5,0.7))
		ElseIf prevFrame < 1509 And n\Frame=>1509
			sfxstep = GetStepSound(n\Collider,n\CollRadius)
			PlaySound2(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(sfxstep*MaxStepSounds)], Camera, n\Collider, 8.0, Rnd(0.5,0.7))
		ElseIf prevFrame < 1522 And n\Frame=>1522
			sfxstep = GetStepSound(n\Collider,n\CollRadius)
			PlaySound2(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(sfxstep*MaxStepSounds)], Camera, n\Collider, 8.0, Rnd(0.5,0.7))
		EndIf
	EndIf
	
	If n\State[0] <> STATE_SCRIPT Then
		If n\State[0] <> MTF_TOTARGET Then
			For n2.NPCs = Each NPCs
				If n2\NPCtype = NPC_NTF Then
					If n2\ID > n\ID Then
						If EntityDistanceSquared(n2\Collider,n\Collider)<PowTwo(0.5) Then
							TranslateEntity n2\Collider, Cos(EntityYaw(n2\Collider,True)+45)* n\Speed*0.7 * FPSfactor, 0, Sin(EntityYaw(n2\Collider,True)+45)* 0.005 * FPSfactor, True
						EndIf
					EndIf
				EndIf
			Next
		EndIf
		
		;Push MTF unit aside when player is close (only works in certain states)
		If IsPushable Then
			If PlayerDistSquared<PowTwo(0.7) Then
				n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,Collider)
				RotateEntity n\Collider,0.0,CurveAngle(n\Angle,EntityYaw(n\Collider,True),20.0),0.0,True
				TranslateEntity n\Collider, Cos(EntityYaw(n\Collider,True)-45)* 0.005 * FPSfactor, 0, Sin(EntityYaw(n\Collider,True)-45)* 0.005 * FPSfactor, True
			EndIf
			If Abs(DeltaYaw(Collider,n\Collider))<80.0 Then
				If PlayerDistSquared<PowTwo(0.7) Then
					TranslateEntity n\Collider, Cos(EntityYaw(Collider,True)+90)* 0.005 * FPSfactor, 0, Sin(EntityYaw(Collider,True)+90)* 0.005 * FPSfactor, True
				EndIf
			EndIf
		EndIf
	EndIf
	
	;Position the NPC's model (although not if the MTF unit is sitting in the helicopter)
	If Int(n\State[0])<>MTF_INTRO Lor Int(n\State[1])<>0 Then
		RotateEntity n\obj,0.0,EntityYaw(n\Collider,True),0.0,True
		PositionEntity n\obj,EntityX(n\Collider,True),EntityY(n\Collider,True)-n\CollRadius,EntityZ(n\Collider,True),True
	EndIf
	
End Function

Function CreateNPCtypeGuardMP(n.NPCs)
	Local temp#,tex%,random%,g.Guns
	
	n\NPCName = "MTF"
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.175, 0.2
	EntityType n\Collider, HIT_PLAYER
	n\obj = CopyEntity(NPCModel[Model_Guard])
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "MTF", "speed") / 100.0)
	temp# = (GetINIFloat("DATA\NPCs.ini", "MTF", "scale") / 2.5)
	ScaleEntity n\obj, temp, temp, temp
	MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	
	random% = Rand(1, 150)
	
	If random > 0 And random < 60 Then
		SwitchNPCGun%(n, GUN_P90)
	ElseIf random > 60 And random < 80 Then
		SwitchNPCGun%(n, GUN_MP5)
	Else
		SwitchNPCGun%(n, GUN_MP7)
	EndIf
	n\Gun\Ammo = n\Gun\MaxAmmo
	n\Gun\ReloadAmmo = 3
	
	n\HP = 250
	
	CopyHitBoxes(n)
	
End Function

Function CreateNPCtypeNTFMP(n.NPCs)
	Local temp#,tex%,random%
	
	n\NPCName = "MTF"
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.175, 0.2
	EntityType n\Collider, HIT_PLAYER
	n\obj = CopyEntity(NPCModel[Model_NTF])
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "MTF", "speed") / 100.0)
	temp# = (GetINIFloat("DATA\NPCs.ini", "MTF", "scale") / 2.5)
	ScaleEntity n\obj, temp, temp, temp
	MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	
	If n\Sound = 0 Then
		n\Sound = LoadSound_Strict("SFX\Character\MTF\Breath.ogg")
	EndIf
	
	random% = Rand(1, 200)
	
	If random > 0 And random < 20 Then
		SwitchNPCGun%(n, GUN_P90)
	ElseIf random > 20 And random < 50 Then
		SwitchNPCGun%(n, GUN_MP5)
	ElseIf random > 50 And random < 100 Then
		SwitchNPCGun%(n, GUN_MP7)
	Else
		SwitchNPCGun%(n, GUN_M4A1)
	EndIf
	n\Gun\Ammo = n\Gun\MaxAmmo
	n\Gun\ReloadAmmo = 3
	
	n\HP = 350
	
	CopyHitBoxes(n)
	
End Function

Function CreateNPCtypeNTFHDSMP(n.NPCs)
	Local temp#,tex%,random%
	
	n\NPCName = "MTF"
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.175, 0.2
	EntityType n\Collider, HIT_PLAYER
	n\obj = LoadAnimMesh_Strict("GFX\NPCs\MTF\NTF\NineTailedFox_HDS.b3d")
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "MTF", "speed") / 100.0)
	temp# = (GetINIFloat("DATA\NPCs.ini", "MTF", "scale") / 2.5)
	ScaleEntity n\obj, temp, temp, temp
	MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	
	If n\Sound = 0 Then
		n\Sound = LoadSound_Strict("SFX\Character\MTF\Breath.ogg")
	EndIf
	If n\Sound2 = 0 Then
		n\Sound2 = LoadSound_Strict("SFX\HDS\Explode.ogg")
	EndIf
	
	n\State[5] = 1
	
	random% = Rand(1, 200)
	
	If random > 0 And random < 20 Then
		SwitchNPCGun%(n, GUN_M4A1)
	ElseIf random > 20 And random < 50 Then
		SwitchNPCGun%(n, GUN_RPK16)
	ElseIf random > 50 And random < 100 Then
		SwitchNPCGun%(n, GUN_P90)
	Else
		SwitchNPCGun%(n, GUN_MP7)
	EndIf
	n\Gun\Ammo = n\Gun\MaxAmmo
	n\Gun\ReloadAmmo = 3
	
	n\HP = 750
	
	CopyHitBoxes(n)
	
End Function

Function CreateNPCtypeHDSBoss(n.NPCs)
	Local temp#,tex%,random%
	
	n\NVName = "Nine-Tailed Fox Trooper"
	n\NPCName = "MTF"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.175, 0.2
	EntityType n\Collider, HIT_PLAYER
	n\obj = CopyEntity(mp_I\BossModel)
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "MTF", "speed") / 100.0)
	temp# = (GetINIFloat("DATA\NPCs.ini", "MTF", "scale") / 2.5)
	ScaleEntity n\obj, temp, temp, temp
	MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	
	If n\Sound = 0 Then
		n\Sound = LoadSound_Strict("SFX\Character\MTF\Breath.ogg")
	EndIf
	If n\Sound2 = 0 Then
		n\Sound2 = LoadSound_Strict("SFX\HDS\Explode.ogg")
	EndIf
	
	n\State[5] = 1
	
	random% = Rand(1, 200)
	
	If random > 0 And random < 20 Then
		SwitchNPCGun%(n, GUN_M4A1)
	ElseIf random > 20 And random < 50 Then
		SwitchNPCGun%(n, GUN_RPK16)
	ElseIf random > 50 And random < 100 Then
		SwitchNPCGun%(n, GUN_P90);G36
	Else
		SwitchNPCGun%(n, GUN_M4A1);M249
	EndIf
	n\Gun\Ammo = n\Gun\MaxAmmo
	n\Gun\ReloadAmmo = 3
	
	n\HP = 50000
	mp_I\MaxBossHealth = n\HP
	mp_I\BossNPC = n
	
	CopyHitBoxes(n)
	
End Function

Function UpdateNPCtypeNTFMP(n.NPCs)
	Local cmsg.ChatMSG
	Local dist#,prevFrame#,yaw#,g.Guns
	
	prevFrame = n\Frame
	
	If n\Gun <> Null Then
		n\Reload = Max(0, n\Reload - FPSfactor)
	EndIf
	
	Local it.Items, Random%, bone%
	
	If (Not n\IsDead)
		Select n\State[0]
			Case MPZ_STATE_FREEZE ; ~ Frozen in place
				;[Block]
				;do nothing
				;[End Block]
			Case MPZ_STATE_WANDER ; ~ Wandering around
				;[Block]
				If n\PathStatus=1 Then
					While n\Path[n\PathLocation]=Null
						If n\PathLocation >= 19 Then
							n\PathLocation = 0 : n\PathStatus = 0 : n\PathTimer = 0.0 : Exit
						Else
							n\PathLocation = n\PathLocation + 1
						EndIf
					Wend
					If n\PathStatus=1 Then
						PointEntity n\obj, n\Path[n\PathLocation]\obj
						If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
						
						AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
						n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
						
						dist = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
						If dist < 0.09 Then
							n\PathLocation = n\PathLocation + 1
						EndIf
					EndIf
				Else
					If n\PathTimer <= 0.0 Then
						n\EnemyX = EntityX(Players[n\ClosestPlayer]\Collider)
						n\EnemyY = EntityY(Players[n\ClosestPlayer]\Collider)
						n\EnemyZ = EntityZ(Players[n\ClosestPlayer]\Collider)
						n\PathStatus = FindPath(n,n\EnemyX,n\EnemyY,n\EnemyZ)
						
						If n\PathStatus = 1 Then
							If n\Path[1]<>Null Then
								If n\Path[2]=Null And EntityDistanceSquared(n\Path[1]\obj,n\Collider)<0.16 Then
									n\PathLocation = 0
									n\PathStatus = 0
								EndIf
							EndIf
							If n\Path[0]<>Null And n\Path[1]=Null Then
								n\PathLocation = 0
								n\PathStatus = 0
							EndIf
						EndIf
						
						If n\PathStatus<>1 Then
							n\PathTimer = 70*5
						EndIf
					Else
						n\PathTimer = n\PathTimer - FPSfactor
						
						PointEntity n\obj, Players[n\ClosestPlayer]\Collider
						If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
						
						AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
						n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					;If EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider)<56.25 Then
						If EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider) Then
							n\State[0] = MPZ_STATE_DETECTED
						EndIf
					;EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSfactor
				EndIf
				
				If mp_I\Gamemode\EnemyCount <= MinEnemyLeft Then
					If n\BlinkTimer <= 0.0 Then
						CreateOverHereParticle(EntityX(n\Collider),EntityY(n\Collider)+0.5,EntityZ(n\Collider))
						n\BlinkTimer = 70*5
					Else
						n\BlinkTimer = n\BlinkTimer - FPSfactor
					EndIf
				EndIf
				;[End Block]
			Case MPZ_STATE_DETECTED ; ~ Player detected
				;[Block]
				PointEntity n\obj, Players[n\ClosestPlayer]\Collider
				If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
				
					; ~ Reloading
				If n\Gun <> Null Then
						If n\Gun\Ammo = 0 Then
							n\Reload = 210
							AnimateNPC(n, 1624, 1674, n\CurrSpeed*60,False)
							If n\Frame >= 1625 And n\Frame < 1625.5 Then
								If n\Gun\MaxReloadSounds = 1 Then
									n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\reload_empty.ogg"), Camera, n\Collider, 25)
								Else
									n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\reload_empty.ogg"), Camera, n\Collider, 25)
								EndIf
							EndIf
							If n\Frame >=1673.5 Then 
								n\Gun\Ammo = n\Gun\MaxAmmo
							EndIf
						Else
							AnimateNPC(n, 488, 522, n\CurrSpeed*60)
						EndIf
					Else
						AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
					EndIf
					
				n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
				MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
				
				If n\Gun <> Null Then
					; ~ Shooting
					If n\Gun <> Null Then
						If n\Gun\Ammo > 0 Then
							If n\Reload = 0 Then
								If EntityVisible(Players[n\ClosestPlayer]\Collider, n\Collider) And Players[n\ClosestPlayer]\CurrHP > 0 Then
									n\Gun\Ammo = n\Gun\Ammo - 1
									Local flashpvt% = CreatePivot()
									
									RotateEntity(flashpvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
									PositionEntity(flashpvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
									MoveEntity (flashpvt,2*0.079, 8.8*0.079, 6.0*0.079)
									
									If Rand(0,2) = 0 Then
										Local p.Particles = CreateParticle(EntityX(flashpvt),EntityY(flashpvt),EntityZ(flashpvt), 1, Rnd(0.08,0.1), 0.0, 5)
										TurnEntity p\obj, 0,0,Rnd(360)
										DamagePlayer(mp_I\PlayerID, n\Gun\Damage / Rand(5, 6.25), n\Gun\Damage / Rand(7, 8.25)/2)
										If Rand(0,5) = 0 Then
											If Players[n\ClosestPlayer]\CurrKevlar>0 Then
												PlaySound_Strict(NTF_PainSFX[Rand(0,7)])
											Else
												PlaySound_Strict(NTF_PainWeakSFX[Rand(0,1)])
											EndIf
										EndIf
										mpl\DamageTimer = 70.0*1.0
									EndIf
									If n\Gun\MaxGunshotSounds = 1 Then
										n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"), Camera, n\Collider, 25)
									Else
										n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"), Camera, n\Collider, 25)
									EndIf
									If n\Gun\GunType = GUNTYPE_HANDGUN Then
										n\Reload = Rand(11,17)
									Else
										n\Reload = Rand(5,10)
									EndIf
								EndIf
							EndIf
						EndIf
					EndIf
				EndIf
				
;				dist = EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider)
;				If dist<=0.6 Then
;					If (Abs(DeltaYaw(n\Collider,Players[n\ClosestPlayer]\Collider))<=60.0) Then
;						n\State[0] = MPZ_STATE_ATTACK
;						If n\NPCtype = NPCtypeGuardZombieMP Then
;							If n\Gun <> Null Then
;								If Rand(4)=1 Then
;									SetNPCFrame(n,255)
;								Else
;									SetNPCFrame(n,306)
;								EndIf
;							Else
;								If Rand(2)=1 Then
;									SetNPCFrame(n,255)
;								Else
;									SetNPCFrame(n,306)
;								EndIf
;							EndIf
;						EndIf
;					EndIf
;				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					;If dist>56.25 Lor (Not EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider)) Then
					If (Not EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider)) Then
						n\State[0] = MPZ_STATE_WANDER
						n\PathTimer = 0.0
						n\PathStatus = 0
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSfactor
				EndIf
				;[End Block]
			Case MPZ_STATE_ATTACK ; ~ Attacking
				;[Block]
				Local shouldFrame#
				Local restartFrame#
				Local finalFrame#
				If n\NPCtype = NPC_Zombie Then
					AnimateNPC(n, 126, 165, 0.4, False)
					shouldFrame = 146
					restartFrame = 126
					finalFrame = 164.5
				Else
					If n\Frame < 306 Then
						AnimateNPC(n, 255, 305, 0.7, False)
						shouldFrame = 276
						finalFrame = 304.5
					Else
						AnimateNPC(n, 306, 356, 0.7, False)
						shouldFrame = 331
						finalFrame = 355.5
					EndIf
				EndIf
				dist = EntityDistanceSquared(n\Collider,Players[n\ClosestPlayer]\Collider)
				yaw = Abs(DeltaYaw(n\Collider,Players[n\ClosestPlayer]\Collider))
				If (dist<=1.2) Then
					If (yaw<=60.0) Then
						If prevFrame < shouldFrame And n\Frame => shouldFrame Then
							PlaySound2(DamageSFX[Rand(5,8)],Camera,n\Collider)
							If n\ClosestPlayer = mp_I\PlayerID Then
								If Players[n\ClosestPlayer]\CurrKevlar>0 Then
									PlaySound_Strict(NTF_PainSFX[Rand(0,7)])
								Else
									PlaySound_Strict(NTF_PainWeakSFX[Rand(0,1)])
								EndIf
							Else
								If Players[n\ClosestPlayer]\CurrKevlar>0 Then
									PlaySound2(NTF_PainSFX[Rand(0,7)],Camera,Players[n\ClosestPlayer]\Collider)
								Else
									PlaySound2(NTF_PainWeakSFX[Rand(0,1)],Camera,Players[n\ClosestPlayer]\Collider)
								EndIf
							EndIf
							If Players[n\ClosestPlayer]\CurrHP > 0 Then
								DamagePlayer(n\ClosestPlayer,Rand(6+2*mp_I\Gamemode\Difficulty,10+2*mp_I\Gamemode\Difficulty),Rand(10+4*mp_I\Gamemode\Difficulty,14+4*mp_I\Gamemode\Difficulty),5)
								If Players[n\ClosestPlayer]\CurrHP <= 0 Then
									cmsg = AddChatMSG("death_killedby", 0, SERVER_MSG_IS, CHATMSG_TYPE_TWOPARAM_TRANSLATE)
									cmsg\Msg[1] = Players[n\ClosestPlayer]\Name
									cmsg\Msg[2] = n\NVName
									Players[n\ClosestPlayer]\Deaths = Players[n\ClosestPlayer]\Deaths + 1
								EndIf
							EndIf	
						EndIf
					EndIf
				EndIf
				If n\Frame => finalFrame Then
					If Players[n\ClosestPlayer]\Collider=0 Lor Players[n\ClosestPlayer]\CurrHP<=0 Then
						n\State[0] = MPZ_STATE_WANDER
					Else
						If dist>1.2 Then
							n\State[0] = MPZ_STATE_DETECTED
						EndIf
						If (yaw>60.0) Then
							n\State[0] = MPZ_STATE_DETECTED
						EndIf
					EndIf
					If n\State[0] = MPZ_STATE_ATTACK Then
						If n\NPCtype = NPC_Zombie_Armed Then
							If Rand(2)=1 Then
								restartFrame = 255
							Else
								restartFrame = 306
							EndIf
						EndIf
						SetNPCFrame(n,restartFrame)
					EndIf
				EndIf
				;[End Block]
		End Select
		
		If n\HP<=0 Then
			n\IsDead=True
			EntityType n\Collider,HIT_DEAD
			SetNPCFrame(n,2)
			MoveEntity n\Collider,0,0.01,0
		EndIf
		If n\Sound <> 0 Then
			n\SoundChn = LoopSound2(n\Sound,n\SoundChn,Camera,n\Collider,5)
		EndIf
	Else
		If n\SoundChn <> 0 Then
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		Local dieFrame#
		If n\State[5] = 1 Then
			n\State[6] = n\State[6] + FPSfactor
			
			If n\State[6] > 0 And n\State[6] < 70*0.1 Then
				If n\Sound2 <> 0 Then
					n\SoundChn = PlaySound_Strict(n\Sound2)
				EndIf
			EndIf
			
			If n\State[6] > 0 And n\State[6] < 70*5 Then
				SetNPCFrame(n,2)
				n\State[5] = 0
			EndIf
		Else
			n\State[6] = 0
			If n\Frame < 28 Then
				AnimateNPC(n, 2, 27, 0.5, False)
				dieFrame = 26.5
			EndIf
		EndIf
		
		If n\Gun <> Null And n\Frame >= dieFrame-0.5 Then
			For g = Each Guns
				If g\ID = n\Gun\ID Then
					bone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "weapon_hand_bonename"))
					it = CreateItem(g\DisplayName, g\name, EntityX(bone%, True), EntityY(bone%, True) + 0.025, EntityZ(bone%, True))
					EntityType it\collider, HIT_ITEM
					it\state = n\Gun\Ammo
					Select Rand(2)
						Case 0
							it\state2 = 0
						Case 1
							it\state2 = n\Gun\MaxAmmo
						Case 2
							it\state2 = n\Gun\MaxAmmo+n\Gun\MaxAmmo
					End Select
					it\Dropped = 1
					Exit
				EndIf
			Next
			RemoveNPCGun(n)
		EndIf
		
		If n\Frame >= dieFrame
			If n\State[1] < 70*5 Then
				n\State[1] = n\State[1] + FPSfactor
			Else
				If n\State[1] >= 70*5 And n\State[1] < 1000 Then
					n\State[1] = 1000
				ElseIf n\State[1] >= 1000 And n\State[1] < 2000 Then
					EntityAlpha n\obj,Inverse((n\State[1]-1000.0)/1000.0)
					n\State[1] = n\State[1] + 2*FPSfactor
				Else
					RemoveNPC(n)
					Return
				EndIf
			EndIf
		EndIf
		If mp_I\PlayState = GAME_SERVER Then
			If prevFrame < 26.5 And n\Frame => 26.5 Lor prevFrame < 26.5 And n\Frame => 26.5 Then
				bone% = FindChild(n\obj,"chest")
				Random = Rand(0,3)
				Select Random
					Case 0
						it = CreateItem(GetLocalString("Item Names","vest"),"vest",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
					Case 1	
						it = CreateItem(GetLocalString("Item Names","ammo"),"ammocrate",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
					Case 2
						it = CreateItem(GetLocalString("Item Names","vest_heavy"),"heavyvest",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
					Case 3	
						it = CreateItem(GetLocalString("Item Names","ammo_big"),"bigammocrate",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
				End Select
				If it <> Null Then
					EntityType it\collider, HIT_ITEM
					it\Dropped = 1
				EndIf
			EndIf
		EndIf
	EndIf
	
	RotateEntity n\obj,0,EntityYaw(n\Collider),0
	PositionEntity n\obj,EntityX(n\Collider),EntityY(n\Collider)-0.2,EntityZ(n\Collider)
	
	EntityAutoFade(n\obj,GetCameraFogRangeFar(Camera)-0.5,GetCameraFogRangeFar(Camera)+0.5)
	
End Function

Function UpdateNPCtypeHDSBoss(n.NPCs)
	Local cmsg.ChatMSG
	Local dist#,prevFrame#,yaw#,g.Guns
	
	prevFrame = n\Frame
	
	If n\Gun <> Null Then
		n\Reload = Max(0, n\Reload - FPSfactor)
	EndIf
	
	Local AgitateHealth% = mp_I\MaxBossHealth / 2
	
	Local QuarterHealth% = mp_I\MaxBossHealth / 4
	
	Local it.Items, Random%, bone%
	
	If (Not n\IsDead)
		Select n\State[0]
			Case MPZ_STATE_FREEZE ; ~ Frozen in place
				;[Block]
				;do nothing
				;[End Block]
			Case MPZ_STATE_WANDER ; ~ Wandering around
				;[Block]
				If n\PathStatus=1 Then
					While n\Path[n\PathLocation]=Null
						If n\PathLocation >= 19 Then
							n\PathLocation = 0 : n\PathStatus = 0 : n\PathTimer = 0.0 : Exit
						Else
							n\PathLocation = n\PathLocation + 1
						EndIf
					Wend
					If n\PathStatus=1 Then
						PointEntity n\obj, n\Path[n\PathLocation]\obj
						If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
						
						AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
						n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
						
						dist = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
						If dist < 0.09 Then
							n\PathLocation = n\PathLocation + 1
						EndIf
					EndIf
				Else
					If n\PathTimer <= 0.0 Then
						n\EnemyX = EntityX(Players[n\ClosestPlayer]\Collider)
						n\EnemyY = EntityY(Players[n\ClosestPlayer]\Collider)
						n\EnemyZ = EntityZ(Players[n\ClosestPlayer]\Collider)
						n\PathStatus = FindPath(n,n\EnemyX,n\EnemyY,n\EnemyZ)
						
						If n\PathStatus = 1 Then
							If n\Path[1]<>Null Then
								If n\Path[2]=Null And EntityDistanceSquared(n\Path[1]\obj,n\Collider)<0.16 Then
									n\PathLocation = 0
									n\PathStatus = 0
								EndIf
							EndIf
							If n\Path[0]<>Null And n\Path[1]=Null Then
								n\PathLocation = 0
								n\PathStatus = 0
							EndIf
						EndIf
						
						If n\PathStatus<>1 Then
							n\PathTimer = 70*5
						EndIf
					Else
						n\PathTimer = n\PathTimer - FPSfactor
						
						PointEntity n\obj, Players[n\ClosestPlayer]\Collider
						If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
						
						AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
						n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					;If EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider)<56.25 Then
					If EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider) Then
						n\State[0] = MPZ_STATE_DETECTED
					EndIf
					;EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSfactor
				EndIf
				
				If mp_I\Gamemode\EnemyCount <= MinEnemyLeft Then
					If n\BlinkTimer <= 0.0 Then
						CreateOverHereParticle(EntityX(n\Collider),EntityY(n\Collider)+0.5,EntityZ(n\Collider))
						n\BlinkTimer = 70*5
					Else
						n\BlinkTimer = n\BlinkTimer - FPSfactor
					EndIf
				EndIf
				;[End Block]
			Case MPZ_STATE_DETECTED ; ~ Player detected
				;[Block]
				PointEntity n\obj, Players[n\ClosestPlayer]\Collider
				If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
				
					; ~ Reloading
				If n\Gun <> Null Then
					If n\Gun\Ammo = 0 Then
						n\Reload = 210
						AnimateNPC(n, 1624, 1674, n\CurrSpeed*60,False)
						If n\Frame >= 1625 And n\Frame < 1625.5 Then
							If n\Gun\MaxReloadSounds = 1 Then
								n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\reload_empty.ogg"), Camera, n\Collider, 25)
							Else
								n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\reload_empty.ogg"), Camera, n\Collider, 25)
							EndIf
						EndIf
						If n\Frame >=1673.5 Then 
							n\Gun\Ammo = n\Gun\MaxAmmo
						EndIf
					Else
						AnimateNPC(n, 488, 522, n\CurrSpeed*60)
					EndIf
				Else
					AnimateNPC(n, 1496, 1524, n\CurrSpeed*30)
				EndIf
				
				n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
				MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
				
				If n\Gun <> Null Then
					; ~ Shooting
					If n\State[5] = 1 Then
						If n\Gun <> Null Then
							If n\Gun\Ammo > 0 Then
								If n\Reload = 0 Then
									If EntityVisible(Players[n\ClosestPlayer]\Collider, n\Collider) And Players[n\ClosestPlayer]\CurrHP > 0 Then
										n\Gun\Ammo = n\Gun\Ammo - 1
										Local flashpvt% = CreatePivot()
										
										RotateEntity(flashpvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
										PositionEntity(flashpvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
										MoveEntity (flashpvt,2*0.079, 8.8*0.079, 6.0*0.079)
										
										If Rand(0,2) = 0 Then
											Local p.Particles = CreateParticle(EntityX(flashpvt),EntityY(flashpvt),EntityZ(flashpvt), 1, Rnd(0.08,0.1), 0.0, 5)
											TurnEntity p\obj, 0,0,Rnd(360)
											DamagePlayer(mp_I\PlayerID, n\Gun\Damage / Rand(5, 6.25), n\Gun\Damage / Rand(7, 8.25)/2)
											If Rand(0,5) = 0 Then
												If Players[n\ClosestPlayer]\CurrKevlar>0 Then
													PlaySound_Strict(NTF_PainSFX[Rand(0,7)])
												Else
													PlaySound_Strict(NTF_PainWeakSFX[Rand(0,1)])
												EndIf
											EndIf
											mpl\DamageTimer = 70.0*1.0
										EndIf
										If n\Gun\MaxGunshotSounds = 1 Then
											n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"), Camera, n\Collider, 25)
										Else
											n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"), Camera, n\Collider, 25)
										EndIf
										If n\Gun\GunType = GUNTYPE_HANDGUN Then
											n\Reload = Rand(11,17)
										Else
											n\Reload = Rand(5,10)
										EndIf
									EndIf
								EndIf
							EndIf
						EndIf
					EndIf
				EndIf
				
;				dist = EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider)
;				If dist<=0.6 Then
;					If (Abs(DeltaYaw(n\Collider,Players[n\ClosestPlayer]\Collider))<=60.0) Then
;						n\State[0] = MPZ_STATE_ATTACK
;						If n\NPCtype = NPCtypeGuardZombieMP Then
;							If n\Gun <> Null Then
;								If Rand(4)=1 Then
;									SetNPCFrame(n,255)
;								Else
;									SetNPCFrame(n,306)
;								EndIf
;							Else
;								If Rand(2)=1 Then
;									SetNPCFrame(n,255)
;								Else
;									SetNPCFrame(n,306)
;								EndIf
;							EndIf
;						EndIf
;					EndIf
;				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					;If dist>56.25 Lor (Not EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider)) Then
					If (Not EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider)) Then
						n\State[0] = MPZ_STATE_WANDER
						n\PathTimer = 0.0
						n\PathStatus = 0
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSfactor
				EndIf
				;[End Block]
			Case MPZ_STATE_ATTACK ; ~ Attacking
				;[Block]
				Local shouldFrame#
				Local restartFrame#
				Local finalFrame#
				If n\NPCtype = NPC_Zombie Then
					AnimateNPC(n, 126, 165, 0.4, False)
					shouldFrame = 146
					restartFrame = 126
					finalFrame = 164.5
				Else
					If n\Frame < 306 Then
						AnimateNPC(n, 255, 305, 0.7, False)
						shouldFrame = 276
						finalFrame = 304.5
					Else
						AnimateNPC(n, 306, 356, 0.7, False)
						shouldFrame = 331
						finalFrame = 355.5
					EndIf
				EndIf
				dist = EntityDistanceSquared(n\Collider,Players[n\ClosestPlayer]\Collider)
				yaw = Abs(DeltaYaw(n\Collider,Players[n\ClosestPlayer]\Collider))
				If (dist<=1.2) Then
					If (yaw<=60.0) Then
						If prevFrame < shouldFrame And n\Frame => shouldFrame Then
							PlaySound2(DamageSFX[Rand(5,8)],Camera,n\Collider)
							If n\ClosestPlayer = mp_I\PlayerID Then
								If Players[n\ClosestPlayer]\CurrKevlar>0 Then
									PlaySound_Strict(NTF_PainSFX[Rand(0,7)])
								Else
									PlaySound_Strict(NTF_PainWeakSFX[Rand(0,1)])
								EndIf
							Else
								If Players[n\ClosestPlayer]\CurrKevlar>0 Then
									PlaySound2(NTF_PainSFX[Rand(0,7)],Camera,Players[n\ClosestPlayer]\Collider)
								Else
									PlaySound2(NTF_PainWeakSFX[Rand(0,1)],Camera,Players[n\ClosestPlayer]\Collider)
								EndIf
							EndIf
							If Players[n\ClosestPlayer]\CurrHP > 0 Then
								DamagePlayer(n\ClosestPlayer,Rand(6+2*mp_I\Gamemode\Difficulty,10+2*mp_I\Gamemode\Difficulty),Rand(10+4*mp_I\Gamemode\Difficulty,14+4*mp_I\Gamemode\Difficulty),5)
								If Players[n\ClosestPlayer]\CurrHP <= 0 Then
									cmsg = AddChatMSG("death_killedby", 0, SERVER_MSG_IS, CHATMSG_TYPE_TWOPARAM_TRANSLATE)
									cmsg\Msg[1] = Players[n\ClosestPlayer]\Name
									cmsg\Msg[2] = n\NVName
									Players[n\ClosestPlayer]\Deaths = Players[n\ClosestPlayer]\Deaths + 1
								EndIf
							EndIf	
						EndIf
					EndIf
				EndIf
				If n\Frame => finalFrame Then
					If Players[n\ClosestPlayer]\Collider=0 Lor Players[n\ClosestPlayer]\CurrHP<=0 Then
						n\State[0] = MPZ_STATE_WANDER
					Else
						If dist>1.2 Then
							n\State[0] = MPZ_STATE_DETECTED
						EndIf
						If (yaw>60.0) Then
							n\State[0] = MPZ_STATE_DETECTED
						EndIf
					EndIf
					If n\State[0] = MPZ_STATE_ATTACK Then
						If n\NPCtype = NPC_Zombie_Armed Then
							If Rand(2)=1 Then
								restartFrame = 255
							Else
								restartFrame = 306
							EndIf
						EndIf
						SetNPCFrame(n,restartFrame)
					EndIf
				EndIf
				;[End Block]
		End Select
		
;		If n\PrevState = 0 Then
;			If n\HP <= agitateHealth Then
;				Local temprot# = EntityYaw(n\obj)
;				RotateEntity n\obj,0,0,0
;				Local tempframe# = AnimTime(n\obj)
;				SetAnimTime n\obj,0
;				
;				; ~ Half of HP is gone
;				
;				n\PrevState = 1
;				RotateEntity n\obj,0,temprot,0
;				SetAnimTime n\obj,tempframe
;			ElseIf n\HP <= QuarterHealth Then
;				temprot# = EntityYaw(n\obj)
;				RotateEntity n\obj,0,0,0
;				tempframe# = AnimTime(n\obj)
;				SetAnimTime n\obj,0
;				
;				; ~ Quarter of HP is gone
;				
;				n\PrevState = 1
;				RotateEntity n\obj,0,temprot,0
;				SetAnimTime n\obj,tempframe
;			EndIf
;		EndIf
		
		If n\HP<=0 Then
			n\IsDead=True
			EntityType n\Collider,HIT_DEAD
			SetNPCFrame(n,2)
			MoveEntity n\Collider,0,0.01,0
		EndIf
		If n\Sound <> 0 Then
			n\SoundChn = LoopSound2(n\Sound,n\SoundChn,Camera,n\Collider,5)
		EndIf
	Else
		If n\SoundChn <> 0 Then
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		Local dieFrame#
		If n\State[5] = 1 Then
			n\State[6] = n\State[6] + FPSfactor
			
			If n\State[6] > 0 And n\State[6] < 70*0.1 Then
				If n\Sound2 <> 0 Then
					n\SoundChn = PlaySound_Strict(n\Sound2)
				EndIf
			EndIf
			
			If n\State[6] > 0 And n\State[6] < 70*5 Then
				SetNPCFrame(n,2)
				n\State[5] = 0
			EndIf
		Else
			n\State[6] = 0
			If n\Frame < 28 Then
				AnimateNPC(n, 2, 27, 0.5, False)
				dieFrame = 26.5
			EndIf
		EndIf
		
		If n\Gun <> Null And n\Frame >= dieFrame-0.5 Then
			For g = Each Guns
				If g\ID = n\Gun\ID Then
					bone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "weapon_hand_bonename"))
					it = CreateItem(g\DisplayName, g\name, EntityX(bone%, True), EntityY(bone%, True) + 0.025, EntityZ(bone%, True))
					EntityType it\collider, HIT_ITEM
					it\state = n\Gun\Ammo
					Select Rand(2)
						Case 0
							it\state2 = 0
						Case 1
							it\state2 = n\Gun\MaxAmmo
						Case 2
							it\state2 = n\Gun\MaxAmmo+n\Gun\MaxAmmo
					End Select
					it\Dropped = 1
					Exit
				EndIf
			Next
			RemoveNPCGun(n)
		EndIf
		
		If n\Frame >= dieFrame
			If n\State[1] < 70*5 Then
				n\State[1] = n\State[1] + FPSfactor
			Else
				If n\State[1] >= 70*5 And n\State[1] < 1000 Then
					n\State[1] = 1000
				ElseIf n\State[1] >= 1000 And n\State[1] < 2000 Then
					EntityAlpha n\obj,Inverse((n\State[1]-1000.0)/1000.0)
					n\State[1] = n\State[1] + 2*FPSfactor
				Else
					RemoveNPC(n)
					Return
				EndIf
			EndIf
		EndIf
		If mp_I\PlayState = GAME_SERVER Then
			If prevFrame < 26.5 And n\Frame => 26.5 Lor prevFrame < 26.5 And n\Frame => 26.5 Then
				bone% = FindChild(n\obj,"chest")
				Random = Rand(0,3)
				Select Random
					Case 0
						it = CreateItem(GetLocalString("Item Names","vest"),"vest",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
					Case 1	
						it = CreateItem(GetLocalString("Item Names","ammo"),"ammocrate",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
					Case 2
						it = CreateItem(GetLocalString("Item Names","vest_heavy"),"heavyvest",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
					Case 3	
						it = CreateItem(GetLocalString("Item Names","ammo_big"),"bigammocrate",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
				End Select
				If it <> Null Then
					EntityType it\collider, HIT_ITEM
					it\Dropped = 1
				EndIf
			EndIf
		EndIf
	EndIf
	
	RotateEntity n\obj,0,EntityYaw(n\Collider),0
	PositionEntity n\obj,EntityX(n\Collider),EntityY(n\Collider)-0.2,EntityZ(n\Collider)
	
	EntityAutoFade(n\obj,GetCameraFogRangeFar(Camera)-0.5,GetCameraFogRangeFar(Camera)+0.5)
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D