
; ~ SCP-035 Boss multiplayer constants

;[Block]
Const MP035_STATE_FREEZE = -1
Const MP035_STATE_WANDER = 0
Const MP035_STATE_DETECTED = 1
Const MP035_STATE_ATTACK = 2
Const MP035_STATE_STUNNED = 3
Const MP035_STATE_RUN = 4
;[End Block]

Function CreateNPCtype035MP(n.NPCs)
	Local temp#,tex%,random%
	
	n\NVName = "SCP-035"
	n\Collider = CreatePivot()
	n\CollRadius = 0.32
	EntityRadius n\Collider, n\CollRadius
	EntityType n\Collider, HIT_NPC_MP
	
	n\obj = CopyEntity(mp_I\BossModel)
	
	temp# = 0.5 / MeshWidth(n\obj)
	ScaleEntity n\obj, temp, temp, temp
	
	Select Rand(0,2)
		Case 0
			n\texture = "GFX\npcs\035.jpg"
		Case 1
			n\texture = "GFX\npcs\035_2.jpg"
		Case 2
			n\texture = "GFX\npcs\035_3.jpg"
	End Select
	
	tex = LoadTexture_Strict(n\texture, 0, 2)
	TextureBlend(tex,5)
	EntityTexture(n\obj, tex)
	
	ScaleEntity(FindChild(n\obj,"mask_sad"), 0, 0, 0)
	ScaleEntity(FindChild(n\obj,"mask_smile"), 1, 1, 1)
	
	n\Speed = 2.0 / 100
	
	MeshCullBox (n\obj, -MeshWidth(mp_I\BossModel), -MeshHeight(mp_I\BossModel), -MeshDepth(mp_I\BossModel), MeshWidth(mp_I\BossModel)*2, MeshHeight(mp_I\BossModel)*2, MeshDepth(mp_I\BossModel)*2)
	
	n\HP = 12000
	mp_I\MaxBossHealth = n\HP
	mp_I\BossNPC = n
	
	CopyHitBoxes(n)
	
End Function

Function UpdateNPCtype035MP(n.NPCs)
	Local prevFrame#,sfxstep%,dist#,yaw#
	Local n2.NPCs,de.Decals,pmp.ParticleMP,cmsg.ChatMSG
	Local i%
	
	If mp_I\PlayState = GAME_SERVER Then RotateEntity(n\Collider, 0, EntityYaw(n\Collider), EntityRoll(n\Collider), True)
	
	prevFrame = n\Frame
	
	Local agitateHealth% = mp_I\MaxBossHealth / 2
	
	If (Not n\IsDead) Then
		
		ShouldPlay = MUS_035_FIGHT
		
		Select n\State[0]
			Case MP035_STATE_FREEZE
				;[Block]
				;do nothing
				n\CurrSpeed = 0
				;[End Block]
			Case MP035_STATE_WANDER
				;[Block]
				If n\PathStatus=1 Then
					While n\Path[n\PathLocation]=Null
						If n\PathLocation > 19 Then
							n\PathLocation = 0 : n\PathStatus = 0 : n\PathTimer = 0.0 : Exit
						Else
							n\PathLocation = n\PathLocation + 1
						EndIf
					Wend
					If n\PathStatus=1 Then
						PointEntity n\obj, n\Path[n\PathLocation]\obj
						If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
						
						n\CurrSpeed = CurveValue(0.015 + (0.005*(n\HP<=agitateHealth)), n\CurrSpeed, 5.0)
						AnimateNPC(n, 236, 260, n\CurrSpeed * 18)
						
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
						If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
						
						n\CurrSpeed = CurveValue(0.015 + (0.005*(n\HP<=agitateHealth)), n\CurrSpeed, 5.0)
						AnimateNPC(n, 236, 260, n\CurrSpeed * 18)
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					If EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider)<56.25 Then
						If EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider) Then
							n\State[0] = MP035_STATE_DETECTED
						EndIf
					EndIf
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
			Case MP035_STATE_DETECTED
				;[Block]
				PointEntity n\obj, Players[n\ClosestPlayer]\Collider
				If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
				
				If n\LastSeen > 0 Then
					n\CurrSpeed = CurveValue(0.03 + (0.005*(n\HP<=agitateHealth)), n\CurrSpeed, 5.0)
					AnimateNPC(n, 301, 319, n\CurrSpeed * 18)
				Else
					n\CurrSpeed = CurveValue(0.015 + (0.005*(n\HP<=agitateHealth)), n\CurrSpeed, 5.0)
					AnimateNPC(n, 236, 260, n\CurrSpeed * 18)
				EndIf
				
				dist = EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider)
				If dist<0.5625 Then
					If (Abs(DeltaYaw(n\Collider,Players[n\ClosestPlayer]\Collider))<=60.0) Then
						n\State[0] = MP035_STATE_ATTACK
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					If dist>56.25 Lor (Not EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider)) Then
						n\State[0] = MP035_STATE_WANDER
						n\PathTimer = 0.0
						n\PathStatus = 0
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSfactor
				EndIf
				
				If n\LastSeen > 0 Then
					If n\CurrSpeed > 0.01 Then
						If prevFrame < 309 And AnimTime(n\obj)=>309 Then
							sfxstep = GetStepSound(n\Collider,n\CollRadius)
							PlaySound2(StepSFX(sfxstep,1,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
						ElseIf prevFrame =< 319 And AnimTime(n\obj)=<301 Then
							sfxstep = GetStepSound(n\Collider,n\CollRadius)
							PlaySound2(StepSFX(sfxstep,1,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
						EndIf
					EndIf
				EndIf
				;[End Block]
			Case MP035_STATE_ATTACK
				;[Block]
				Local shouldFrame#
				Local restartFrame#
				Local finalFrame#
				AnimateNPC(n, 631, 670, 0.45 + (0.15*(n\LastSeen>0.0)), False)
				shouldFrame = 651
				restartFrame = 631
				finalFrame = 669.5
				dist = EntityDistanceSquared(n\Collider,Players[n\ClosestPlayer]\Collider)
				yaw = Abs(DeltaYaw(n\Collider,Players[n\ClosestPlayer]\Collider))
				If (dist<1.21) Then
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
								DamagePlayer(n\ClosestPlayer,Rand(15,21),Rand(26,33),5)
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
						n\State[0] = MP035_STATE_WANDER
					Else
						If dist>1.0 Then
							n\State[0] = MP035_STATE_DETECTED
						EndIf
						If (yaw>60.0) Then
							n\State[0] = MP035_STATE_DETECTED
						EndIf
					EndIf
					If n\State[0] = MP035_STATE_ATTACK Then
						SetNPCFrame(n,restartFrame)
					EndIf
				EndIf
				
				n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
				;[End Block]
			Case MP035_STATE_STUNNED
				;[Block]
				AnimateNPC(n, 555, 630, 0.3, False)
				
				n\LastSeen = 70*10
				If prevFrame < 629.5 And n\Frame >= 629.5 Lor prevFrame = 630.0 And n\Frame = 630.0 Then
					If EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider)<56.25 Then
						If EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider) Then
							n\State[0] = MP035_STATE_DETECTED
						EndIf
					EndIf
					
					If n\State[0] <> MP035_STATE_DETECTED Then
						n\State[0] = MP035_STATE_WANDER
					EndIf
				EndIf
				
				n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
				;[End Block]
		End Select
		
		If n\State[0] = MP035_STATE_WANDER Lor n\LastSeen <= 0 Then
			If n\CurrSpeed > 0.01 Then
				If prevFrame < 244 And AnimTime(n\obj)=>244 Then
					sfxstep = GetStepSound(n\Collider,n\CollRadius)
					PlaySound2(StepSFX(sfxstep,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
				ElseIf prevFrame < 256 And AnimTime(n\obj)=>256 Then
					sfxstep = GetStepSound(n\Collider,n\CollRadius)
					PlaySound2(StepSFX(sfxstep,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
				EndIf
			EndIf
		EndIf
		
		If mp_I\PlayState = GAME_SERVER Then
			If n\State[0] > MP035_STATE_FREEZE And n\State[0] < MP035_STATE_ATTACK Then
				If Abs(n\DropSpeed) < 0.1 Then
					If n\State[2] <= 0.0 Then
						If Rand(5) = 1 Then
							n2 = CreateNPC(NPC_SCP_035_Tentacle, EntityX(n\Collider), EntityY(n\Collider) - (n\CollRadius/2.0) - 0.05 , EntityZ(n\Collider))
							pmp = CreateParticleMP(PARTICLEMP_035,EntityX(n\Collider),EntityY(n\Collider) - (n\CollRadius/2.0) - 0.15,EntityZ(n\Collider),0,0,0,0,0,0)
						EndIf
						n\State[2] = 70*10
					Else
						n\State[2] = n\State[2] - FPSfactor
					EndIf
				EndIf
			EndIf
		EndIf
		
		If n\PrevState = 0 Then
			If n\HP <= agitateHealth Then
				Local temprot# = EntityYaw(n\obj)
				RotateEntity n\obj,0,0,0
				Local tempframe# = AnimTime(n\obj)
				SetAnimTime n\obj,0
				ScaleEntity(FindChild(n\obj,"mask_sad"), 1, 1, 1)
				ScaleEntity(FindChild(n\obj,"mask_smile"), 0, 0, 0)
				For i = 0 To 1
					n2 = CreateNPC(NPC_SCP_035_Tentacle, 0, 0, 0)
					n2\Target = n
					ScaleEntity n2\obj, 0.025, 0.025, 0.025
					SetNPCFrame(n2, 286)
					PositionEntity n2\Collider, EntityX(n\obj) + (0.06 - (0.12*i)), EntityY(n\obj) + 0.8, EntityZ(n\obj) + 0.04
					ResetEntity n2\Collider
					RotateEntity n2\Collider, -80, 0, -10 + (20*i)
					EntityParent n2\Collider,FindChild(n\obj,"Bip01_Spine2")
					PositionEntity n2\obj, EntityX(n\obj) + (0.06 - (0.12*i)), EntityY(n\obj) + 0.8, EntityZ(n\obj) + 0.04
					RotateEntity n2\obj, -80, 0, -10 + (20*i)
					EntityParent n2\obj,FindChild(n\obj,"Bip01_Spine2")
				Next
				n\PrevState = 1
				RotateEntity n\obj,0,temprot,0
				SetAnimTime n\obj,tempframe
			EndIf
		EndIf
		
		If n\LastSeen <= 0.0 Then
			If n\GotHit Then
				n\State[0] = MP035_STATE_STUNNED
			EndIf
		Else
			n\LastSeen = n\LastSeen - FPSfactor
		EndIf
		
		MoveEntity(n\Collider, 0, 0, n\CurrSpeed * FPSfactor)
		
		If n\HP<=0 Then
			n\IsDead=True
			EntityType n\Collider,HIT_DEAD
			SetNPCFrame(n, 0)
			MoveEntity n\Collider,0,0.01,0
			If (mp_I\Gamemode\Phase/2) = Waves_Long And mp_I\Gamemode\Difficulty = KETER And mp_I\Gamemode\CanSurviveAllWaves = True Then
				If opt\SteamEnabled Then Steam_Achieve(ACHV_MASK)
				GiveAchievement(Achv035)
			EndIf	
		Else
			mp_I\BossNPC = n
		EndIf
	Else
		If n\SoundChn <> 0 Then
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		Local dieFrame# = 19.5
		AnimateNPC(n, 0, 20, 0.5, False)
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
	EndIf
	
	PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - n\CollRadius, EntityZ(n\Collider))
	RotateEntity n\obj, EntityPitch(n\Collider), EntityYaw(n\Collider)-180.0, 0
	
End Function

Function CreateNPCtype035(n.NPCs)
	Local temp#,tex%,random%
	
	n\BossName = "SCP-035"
	n\NVName = "SCP-035"
	n\Collider = CreatePivot()
	n\CollRadius = 0.32
	EntityRadius n\Collider, n\CollRadius
	EntityType n\Collider, HIT_PLAYER
	
	n\obj = CopyEntity(NPCModel[Model_035])
	
	temp# = 0.5 / MeshWidth(n\obj)
	ScaleEntity n\obj, temp, temp, temp
	Select Rand(0,2)
		Case 0
			n\texture = "GFX\npcs\035.jpg"
		Case 1
			n\texture = "GFX\npcs\035_2.jpg"
		Case 2
			n\texture = "GFX\npcs\035_3.jpg"
	End Select
	
	tex = LoadTexture_Strict(n\texture, 0, 2)
	TextureBlend(tex,5)
	EntityTexture(n\obj, tex)
	
	ScaleEntity(FindChild(n\obj,"mask_sad"), 0, 0, 0)
	ScaleEntity(FindChild(n\obj,"mask_smile"), 1, 1, 1)
	
	n\Speed = 2.0 / 100
	
	MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	
	n\HP = 12000
	n\MaxBossHealth = n\HP
	n\Boss = n
	
	CopyHitBoxes(n)
	
End Function

Function UpdateNPCtype035(n.NPCs)
	Local prevFrame#,sfxstep%,dist#,yaw#
	Local n2.NPCs,de.Decals,p.Particles
	Local i%
	
	RotateEntity(n\Collider, 0, EntityYaw(n\Collider), EntityRoll(n\Collider), True)
	
	prevFrame = n\Frame
	
	Local agitateHealth% = n\MaxBossHealth / 2
	
	If (Not n\IsDead) Then
		
		ShouldPlay = MUS_035_FIGHT
		
		Select n\State[0]
			Case MP035_STATE_FREEZE
				;[Block]
				;do nothing
				n\CurrSpeed = 0
				;[End Block]
			Case MP035_STATE_WANDER
				;[Block]
				If n\PathStatus=1 Then
					While n\Path[n\PathLocation]=Null
						If n\PathLocation > 19 Then
							n\PathLocation = 0 : n\PathStatus = 0 : n\PathTimer = 0.0 : Exit
						Else
							n\PathLocation = n\PathLocation + 1
						EndIf
					Wend
					If n\PathStatus=1 Then
						PointEntity n\obj, n\Path[n\PathLocation]\obj
						RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
						
						n\CurrSpeed = CurveValue(0.015 + (0.005*(n\HP<=agitateHealth)), n\CurrSpeed, 5.0)
						AnimateNPC(n, 236, 260, n\CurrSpeed * 18)
						
						dist = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
						If dist < 0.09 Then
							n\PathLocation = n\PathLocation + 1
						EndIf
					EndIf
				Else
					If n\PathTimer <= 0.0 Then
						n\EnemyX = EntityX(Collider)
						n\EnemyY = EntityY(Collider)
						n\EnemyZ = EntityZ(Collider)
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
						
						PointEntity n\obj, Collider
						RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
						
						n\CurrSpeed = CurveValue(0.015 + (0.005*(n\HP<=agitateHealth)), n\CurrSpeed, 5.0)
						AnimateNPC(n, 236, 260, n\CurrSpeed * 18)
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					If EntityDistanceSquared(n\obj,Collider)<56.25 Then
						If EntityVisible(n\obj,Collider) Then
							n\State[0] = MP035_STATE_DETECTED
						EndIf
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSfactor
				EndIf
				;[End Block]
			Case MP035_STATE_DETECTED
				;[Block]
				PointEntity n\obj, Collider
				RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
				
				If n\LastSeen > 0 Then
					n\State[0] = MP035_STATE_RUN
				Else
					n\CurrSpeed = CurveValue(0.015 + (0.005*(n\HP<=agitateHealth)), n\CurrSpeed, 5.0)
					AnimateNPC(n, 236, 260, n\CurrSpeed * 18)
				EndIf
				
				dist = EntityDistanceSquared(n\obj,Collider)
				If dist<0.5625 Then
					If (Abs(DeltaYaw(n\Collider,Collider))<=60.0) Then
						n\State[0] = MP035_STATE_ATTACK
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					If dist>56.25 Lor (Not EntityVisible(n\obj,Collider)) Then
						n\State[0] = MP035_STATE_WANDER
						n\PathTimer = 0.0
						n\PathStatus = 0
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSfactor
				EndIf
				
				If n\LastSeen > 0 Then
					If n\CurrSpeed > 0.01 Then
						If prevFrame < 309 And AnimTime(n\obj)=>309 Then
							sfxstep = GetStepSound(n\Collider,n\CollRadius)
							PlaySound2(StepSFX(sfxstep,1,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
						ElseIf prevFrame =< 319 And AnimTime(n\obj)=<301 Then
							sfxstep = GetStepSound(n\Collider,n\CollRadius)
							PlaySound2(StepSFX(sfxstep,1,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
						EndIf
					EndIf
				EndIf
			Case MP035_STATE_RUN
				PointEntity n\obj, Collider
				RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
				
				n\CurrSpeed = CurveValue(0.03 + (0.005*(n\HP<=agitateHealth)), n\CurrSpeed, 5.0)
				AnimateNPC(n, 301, 319, n\CurrSpeed * 18)
				
				dist = EntityDistanceSquared(n\obj,Collider)
				If dist<0.5625 Then
					If (Abs(DeltaYaw(n\Collider,Collider))<=60.0) Then
						n\State[0] = MP035_STATE_ATTACK
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					If dist>56.25 Lor (Not EntityVisible(n\obj,Collider)) Then
						n\State[0] = MP035_STATE_WANDER
						n\PathTimer = 0.0
						n\PathStatus = 0
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSfactor
				EndIf
				
				If n\LastSeen > 0 Then
					If n\CurrSpeed > 0.01 Then
						If prevFrame < 309 And AnimTime(n\obj)=>309 Then
							sfxstep = GetStepSound(n\Collider,n\CollRadius)
							PlaySound2(StepSFX(sfxstep,1,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
						ElseIf prevFrame =< 319 And AnimTime(n\obj)=<301 Then
							sfxstep = GetStepSound(n\Collider,n\CollRadius)
							PlaySound2(StepSFX(sfxstep,1,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
						EndIf
					EndIf
				EndIf
				
				n\GotHit = False
			Case MP035_STATE_ATTACK
				;[Block]
				Local shouldFrame#
				Local restartFrame#
				Local finalFrame#
				AnimateNPC(n, 631, 670, 0.45 + (0.15*(n\LastSeen>0.0)), False)
				shouldFrame = 651
				restartFrame = 631
				finalFrame = 669.5
				n\GotHit = False
				dist = EntityDistanceSquared(n\Collider,Collider)
				yaw = Abs(DeltaYaw(n\Collider,Collider))
				If (dist<1.21) Then
					If (yaw<=60.0) Then
						If prevFrame < shouldFrame And n\Frame => shouldFrame Then
							PlaySound2(DamageSFX[Rand(5,8)],Camera,n\Collider)
							If psp\Kevlar > 0 Then
								PlaySound_Strict(NTF_PainSFX[Rand(0,7)])
							Else
								PlaySound_Strict(NTF_PainWeakSFX[Rand(0,1)])
							EndIf
							If psp\Health > 0 Then
								DamageSPPlayer(Rand(15,21),False,5)
							Else
								m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp035_death",Designation)
							EndIf	
						EndIf
					EndIf
				EndIf
				If n\Frame => finalFrame Then
					If Collider=0 Lor psp\Health<=0 Then
						n\State[0] = MP035_STATE_WANDER
						n\GotHit = False
					Else
						If dist>1.0 Then
							n\State[0] = MP035_STATE_DETECTED
							n\GotHit = False
						EndIf
						If (yaw>60.0) Then
							n\State[0] = MP035_STATE_DETECTED
							n\GotHit = False
						EndIf
					EndIf
					If n\State[0] = MP035_STATE_ATTACK Then
						SetNPCFrame(n,restartFrame)
						n\GotHit = False
					EndIf
				EndIf
				
				n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
				;[End Block]
			Case MP035_STATE_STUNNED
				;[Block]
				AnimateNPC(n, 555, 630, 0.3, False)
				n\GotHit = False
				n\LastSeen = 70*10
				If prevFrame < 629.5 And n\Frame >= 629.5 Lor prevFrame = 630.0 And n\Frame = 630.0 Then
					If EntityDistanceSquared(n\obj,Collider)<56.25 Then
						If EntityVisible(n\obj,Collider) Then
							n\State[0] = MP035_STATE_DETECTED
							n\GotHit = False
						EndIf
					EndIf
					
					If n\State[0] <> MP035_STATE_DETECTED Then
						n\State[0] = MP035_STATE_WANDER
						n\GotHit = False
					EndIf
				EndIf
				
				n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
				;[End Block]
		End Select
		
		If n\State[0] = MP035_STATE_WANDER Lor n\LastSeen <= 0 Then
			If n\CurrSpeed > 0.01 Then
				If prevFrame < 244 And AnimTime(n\obj)=>244 Then
					sfxstep = GetStepSound(n\Collider,n\CollRadius)
					PlaySound2(StepSFX(sfxstep,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
				ElseIf prevFrame < 256 And AnimTime(n\obj)=>256 Then
					sfxstep = GetStepSound(n\Collider,n\CollRadius)
					PlaySound2(StepSFX(sfxstep,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
				EndIf
			EndIf
		EndIf
		
		If n\PrevState = 0 Then
			If n\HP <= agitateHealth Then
				Local temprot# = EntityYaw(n\obj)
				RotateEntity n\obj,0,0,0
				Local tempframe# = AnimTime(n\obj)
				SetAnimTime n\obj,0
				ScaleEntity(FindChild(n\obj,"mask_sad"), 1, 1, 1)
				ScaleEntity(FindChild(n\obj,"mask_smile"), 0, 0, 0)
				For i = 0 To 1
					n2 = CreateNPC(NPC_SCP_035_Tentacle, 0, 0, 0)
					n2\Target = n
					ScaleEntity n2\obj, 0.025, 0.025, 0.025
					SetNPCFrame(n2, 286)
					PositionEntity n2\Collider, EntityX(n\obj) + (0.06 - (0.12*i)), EntityY(n\obj) + 0.8, EntityZ(n\obj) + 0.04
					ResetEntity n2\Collider
					RotateEntity n2\Collider, -80, 0, -10 + (20*i)
					EntityParent n2\Collider,FindChild(n\obj,"Bip01_Spine2")
					PositionEntity n2\obj, EntityX(n\obj) + (0.06 - (0.12*i)), EntityY(n\obj) + 0.8, EntityZ(n\obj) + 0.04
					RotateEntity n2\obj, -80, 0, -10 + (20*i)
					EntityParent n2\obj,FindChild(n\obj,"Bip01_Spine2")
				Next
				n\PrevState = 1
				RotateEntity n\obj,0,temprot,0
				SetAnimTime n\obj,tempframe
			EndIf
		EndIf
		
		If n\LastSeen <= 0.0 Then
			If n\GotHit Then
				n\State[0] = MP035_STATE_STUNNED
				n\GotHit = False
			EndIf
		Else
			n\LastSeen = n\LastSeen - FPSfactor
		EndIf
		
		MoveEntity(n\Collider, 0, 0, n\CurrSpeed * FPSfactor)
		
		If n\HP<=0 Then
			n\IsDead=True
			EntityType n\Collider,HIT_DEAD
			SetNPCFrame(n, 0)
			MoveEntity n\Collider,0,0.01,0
			GiveAchievement(Achv035)
		Else
			n\Boss = n
		EndIf
	Else
		If n\SoundChn <> 0 Then
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		Local dieFrame# = 19.5
		AnimateNPC(n, 0, 20, 0.5, False)
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
	EndIf
	
	PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - n\CollRadius, EntityZ(n\Collider))
	RotateEntity n\obj, EntityPitch(n\Collider), EntityYaw(n\Collider)-180.0, 0
	
End Function

; ~ SCP-035 Tentacle constants

;[Block]

Const TENTACLE_FREEZE = -1
Const TENTACLE_SPAWN = 0
Const TENTACLE_IDLE = 1
Const TENTACLE_ATTACK = 2

;[End Block]

Function CreateNPCtypeTentacleMP(n.NPCs)
	
	n\Collider = CreatePivot()
	
	n\obj = CopyEntity(mp_I\TentacleModel)
	ScaleEntity n\obj, 0.065,0.065,0.065
	
	n\HP = 100
	n\NVName = "SCP-035 tentacle"
	
	SetNPCFrame(n, 180)
	
	CopyHitBoxes(n)
	
End Function

Function UpdateNPCtypeTentacleMP(n.NPCs)
	Local cmsg.ChatMSG
	
	If n\Target = Null Then
		If (Not n\IsDead) Then
			Select n\State[0] 
				Case TENTACLE_FREEZE
					;[Block]
					;do nothing
					;[End Block]
				Case TENTACLE_SPAWN
					;[Block]
					If n\Frame>180 Then
						PointEntity n\obj, Players[n\ClosestPlayer]\Collider
						If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),25.0), 0
						
						AnimateNPC(n, 180, 285, 0.3, False)
						
						If n\Frame>=284.5 Then
							n\State[0] = TENTACLE_IDLE
							FreeSound_Strict n\Sound2 : n\Sound2=0
						EndIf
					Else
						SetNPCFrame(n, 181)
						n\Sound2 = LoadSound_Strict("SFX\Room\035Chamber\TentacleSpawn.ogg")
						n\SoundChn2 = PlaySound2(n\Sound2, Camera, n\Collider)
						mp_I\Gamemode\EnemyCount = mp_I\Gamemode\EnemyCount + 1
					EndIf
					;[End Block]
				Case TENTACLE_IDLE
					;[Block]
					PointEntity n\obj, Players[n\ClosestPlayer]\Collider
					If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),25.0), 0
					
					AnimateNPC(n, 33, 174, 0.3, True)
					
					If Players[n\ClosestPlayer]\CurrHP > 0 Then
						If EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider)<3.24 Then
							If Abs(DeltaYaw(n\Collider, Players[n\ClosestPlayer]\Collider))<20 Then 
								If EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider) Then
									n\State[0] = TENTACLE_ATTACK
									If n\Sound<>0 Then FreeSound_Strict n\Sound : n\Sound = 0 
									If n\Sound2<>0 Then FreeSound_Strict n\Sound2 : n\Sound2 = 0 
								EndIf
							EndIf
						EndIf
					EndIf
					;[End Block]
				Case TENTACLE_ATTACK
					;[Block]
					If n\Frame>33 And n\Frame<174 Then
						AnimateNPC(n, 33, 174, 2.0, False)
					Else
						PointEntity n\obj, Players[n\ClosestPlayer]\Collider
						If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0), 0							
						
						If n\Frame>33 Then
							n\Frame = 2
							If n\Sound <> 0 Then
								FreeSound_Strict n\Sound : n\Sound=0
								StopChannel n\SoundChn
							EndIf
							n\Sound = LoadSound_Strict("SFX\Room\035Chamber\TentacleAttack"+Rand(1,2)+".ogg")
							n\SoundChn = PlaySound2(n\Sound, Camera, n\Collider)
						EndIf
						AnimateNPC(n, 2, 32, 0.3, False)
						
						If n\Frame>=5 And n\Frame<6 Then
							If EntityDistanceSquared(n\Collider, Players[n\ClosestPlayer]\Collider) < 3.24 Then
								If Abs(DeltaYaw(n\Collider, Players[n\ClosestPlayer]\Collider))<20 Then 
									PlaySound2(DamageSFX[Rand(2,3)],Camera,n\Collider)
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
									DamagePlayer(n\ClosestPlayer,Rand(19,28),Rand(30,41),4)
									If Players[n\ClosestPlayer]\CurrHP <= 0 Then
										cmsg = AddChatMSG("death_killedby", 0, SERVER_MSG_IS, CHATMSG_TYPE_TWOPARAM_TRANSLATE)
										cmsg\Msg[1] = Players[n\ClosestPlayer]\Name
										cmsg\Msg[2] = n\NVName
										Players[n\ClosestPlayer]\Deaths = Players[n\ClosestPlayer]\Deaths + 1
									EndIf
								EndIf
							EndIf
							
							n\Frame = 6
						ElseIf n\Frame=32
							n\State[0] = TENTACLE_IDLE
							n\Frame = 173
						EndIf
					EndIf
					;[End Block]
			End Select
			
			If n\HP<=0 Then
				n\IsDead=True
				SetNPCFrame(n, 285)
			EndIf
			
			UpdateSoundOrigin(n\SoundChn, Camera, n\Collider)
			If n\State[0] <> TENTACLE_SPAWN Then
				If n\Sound2=0 Then
					n\Sound2 = LoadSound_Strict("SFX\Room\035Chamber\TentacleIdle.ogg")
				EndIf
				n\SoundChn2 = LoopSound2(n\Sound2,n\SoundChn2,Camera,n\Collider)
			Else
				UpdateSoundOrigin(n\SoundChn2, Camera, n\Collider)
			EndIf
		Else
			If n\SoundChn <> 0 Then
				StopChannel n\SoundChn
				n\SoundChn = 0
				FreeSound_Strict n\Sound
				n\Sound = 0
			EndIf
			If n\SoundChn2 <> 0 Then
				StopChannel n\SoundChn2
				n\SoundChn2 = 0
				FreeSound_Strict n\Sound2
				n\Sound2 = 0
			EndIf
			Local dieFrame# = 180.5
			AnimateNPC(n, 285, 180, -0.8, False)
			If n\Frame <= dieFrame
				RemoveNPC(n)
				Return
			EndIf
		EndIf
		
		PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider), EntityZ(n\Collider))
		RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider)-180, EntityRoll(n\Collider), True
	ElseIf n\State[0] = TENTACLE_FREEZE Then
		; ~ Nothing
	Else
		If (Not n\Target\IsDead) Then
			If n\State[0] = TENTACLE_SPAWN Then
				If n\Frame>286 Then
					AnimateNPC(n, 286, 357, 0.3, False)
					
					If n\Frame>=356.5 Then
						n\State[0] = TENTACLE_IDLE
						FreeSound_Strict n\Sound2 : n\Sound2=0
					EndIf
				Else
					SetNPCFrame(n, 287)
					n\Sound2 = LoadSound_Strict("SFX\Room\035Chamber\TentacleSpawn.ogg")
					n\SoundChn2 = PlaySound2(n\Sound2, Camera, n\Target\Collider)
				EndIf
			Else
				If n\State[0] = TENTACLE_IDLE Then
					AnimateNPC(n, 33, 174, Rnd(0.1,0.5), True)
					If n\Target\State[0] = MP035_STATE_ATTACK Then
						n\State[0] = TENTACLE_ATTACK
					EndIf
				Else
					If n\Frame>33 And n\Frame<174 Then
						AnimateNPC(n, 33, 174, Rnd(2.0,4.0), False)
					Else
						If n\Frame>33 Then
							n\Frame = 2
							If n\Sound <> 0 Then
								FreeSound_Strict n\Sound : n\Sound=0
								StopChannel n\SoundChn
							EndIf
							n\Sound = LoadSound_Strict("SFX\Room\035Chamber\TentacleAttack"+Rand(1,2)+".ogg")
							n\SoundChn = PlaySound2(n\Sound, Camera, n\Target\Collider)
						EndIf
						AnimateNPC(n, 2, 32, 0.3, False)
						
						If n\Frame>=5 And n\Frame<6 Then
							If EntityDistanceSquared(n\Target\Collider, Players[n\Target\ClosestPlayer]\Collider) < 3.24 Then
								If Abs(DeltaYaw(n\Target\Collider, Players[n\Target\ClosestPlayer]\Collider))<20 Then 
									PlaySound2(DamageSFX[Rand(2,3)],Camera,n\Target\Collider)
									If n\Target\ClosestPlayer = mp_I\PlayerID Then
										If Players[n\Target\ClosestPlayer]\CurrKevlar>0 Then
											PlaySound_Strict(NTF_PainSFX[Rand(0,7)])
										Else
											PlaySound_Strict(NTF_PainWeakSFX[Rand(0,1)])
										EndIf
									Else
										If Players[n\Target\ClosestPlayer]\CurrKevlar>0 Then
											PlaySound2(NTF_PainSFX[Rand(0,7)],Camera,Players[n\Target\ClosestPlayer]\Collider)
										Else
											PlaySound2(NTF_PainWeakSFX[Rand(0,1)],Camera,Players[n\Target\ClosestPlayer]\Collider)
										EndIf
									EndIf
									DamagePlayer(n\Target\ClosestPlayer,Rand(19,28),Rand(30,41),4)
									If Players[n\ClosestPlayer]\CurrHP <= 0 Then
										cmsg = AddChatMSG("death_killedby", 0, SERVER_MSG_IS, CHATMSG_TYPE_TWOPARAM_TRANSLATE)
										cmsg\Msg[1] = Players[n\ClosestPlayer]\Name
										cmsg\Msg[2] = n\NVName
									EndIf
								EndIf
							EndIf
							
							n\Frame = 6
						ElseIf n\Frame=32
							n\State[0] = TENTACLE_IDLE
							n\Frame = 173
						EndIf
					EndIf
				EndIf
			EndIf
			
			If n\GotHit Then
				n\Target\HP = n\Target\HP - n\GotHit
			EndIf
			
			UpdateSoundOrigin(n\SoundChn, Camera, n\Collider)
			If n\State[0] <> TENTACLE_SPAWN Then
				If n\Sound2=0 Then
					n\Sound2 = LoadSound_Strict("SFX\Room\035Chamber\TentacleIdle.ogg")
				EndIf
				n\SoundChn2 = LoopSound2(n\Sound2,n\SoundChn2,Camera,n\Collider)
			Else
				UpdateSoundOrigin(n\SoundChn2, Camera, n\Collider)
			EndIf
		Else
			If n\SoundChn <> 0 Then
				StopChannel n\SoundChn
				n\SoundChn = 0
				FreeSound_Strict n\Sound
				n\Sound = 0
			EndIf
			If n\SoundChn2 <> 0 Then
				StopChannel n\SoundChn2
				n\SoundChn2 = 0
				FreeSound_Strict n\Sound2
				n\Sound2 = 0
			EndIf
			
			If n\Frame <= 285.0 Then
				SetNPCFrame(n, 357)
			EndIf
			
			AnimateNPC(n, 357, 286, -0.8, False)
			If n\Frame <= 286.5 And n\Frame > 285.0 Then
				RemoveNPC(n)
				Return
			EndIf
		EndIf
	EndIf
	
	n\DropSpeed = 0
	
	ResetEntity n\Collider
	
End Function

Function CreateNPCtypeTentacle(n.NPCs)
	
	n\Collider = CreatePivot()
	
	n\obj = LoadAnimMesh_Strict("GFX\NPCs\SCPs\035\tentacle.b3d")
	ScaleEntity n\obj, 0.065,0.065,0.065
	
	n\HP = 100
	n\NVName = "SCP-035 tentacle"
	
	SetNPCFrame(n, 180)
	
	CopyHitBoxes(n)
	
End Function

Function UpdateNPCtypeTentacle(n.NPCs)
	
	If n\Target = Null Then
		If (Not n\IsDead) Then
			Select n\State[0] 
				Case TENTACLE_FREEZE
					;[Block]
					;do nothing
					;[End Block]
				Case TENTACLE_SPAWN
					;[Block]
					If n\Frame>180 Then
						PointEntity n\obj, Collider
						RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),25.0), 0
						
						AnimateNPC(n, 180, 285, 0.3, False)
						
						If n\Frame>=284.5 Then
							n\State[0] = TENTACLE_IDLE
							FreeSound_Strict n\Sound2 : n\Sound2=0
						EndIf
					Else
						SetNPCFrame(n, 181)
						n\Sound2 = LoadSound_Strict("SFX\Room\035Chamber\TentacleSpawn.ogg")
						n\SoundChn2 = PlaySound2(n\Sound2, Camera, n\Collider)
					EndIf
					;[End Block]
				Case TENTACLE_IDLE
					;[Block]
					PointEntity n\obj, Collider
					RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),25.0), 0
					
					AnimateNPC(n, 33, 174, 0.3, True)
					
					If psp\Health > 0 Then
						If EntityDistanceSquared(n\obj,Collider)<3.24 Then
							If Abs(DeltaYaw(n\Collider, Collider))<20 Then 
								If EntityVisible(n\obj,Collider) Then
									n\State[0] = TENTACLE_ATTACK
									If n\Sound<>0 Then FreeSound_Strict n\Sound : n\Sound = 0 
									If n\Sound2<>0 Then FreeSound_Strict n\Sound2 : n\Sound2 = 0 
								EndIf
							EndIf
						EndIf
					EndIf
					;[End Block]
				Case TENTACLE_ATTACK
					;[Block]
					If n\Frame>33 And n\Frame<174 Then
						AnimateNPC(n, 33, 174, 2.0, False)
					Else
						PointEntity n\obj, Collider
						RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0), 0							
						
						If n\Frame>33 Then
							n\Frame = 2
							If n\Sound <> 0 Then
								FreeSound_Strict n\Sound : n\Sound=0
								StopChannel n\SoundChn
							EndIf
							n\Sound = LoadSound_Strict("SFX\Room\035Chamber\TentacleAttack"+Rand(1,2)+".ogg")
							n\SoundChn = PlaySound2(n\Sound, Camera, n\Collider)
						EndIf
						AnimateNPC(n, 2, 32, 0.3, False)
						
						If n\Frame>=5 And n\Frame<6 Then
							If EntityDistanceSquared(n\Collider,Collider) < 3.24 Then
								If Abs(DeltaYaw(n\Collider,Collider))<20 Then 
									PlaySound2(DamageSFX[Rand(2,3)],Camera,n\Collider)
									If psp\Kevlar>0 Then
										PlaySound_Strict(NTF_PainSFX[Rand(0,7)])
									Else
										PlaySound_Strict(NTF_PainWeakSFX[Rand(0,1)])
									EndIf
									DamageSPPlayer(Rand(19,28),False)
									If psp\Health <= 0 Then
										m_msg\DeathTxt = GetLocalString("Singleplayer","contact_lost")
									EndIf
								EndIf
							EndIf
							
							n\Frame = 6
						ElseIf n\Frame=32
							n\State[0] = TENTACLE_IDLE
							n\Frame = 173
						EndIf
					EndIf
					;[End Block]
			End Select
			
			If n\HP<=0 Then
				n\IsDead=True
				SetNPCFrame(n, 285)
			EndIf
			
			UpdateSoundOrigin(n\SoundChn, Camera, n\Collider)
			If n\State[0] <> TENTACLE_SPAWN Then
				If n\Sound2=0 Then
					n\Sound2 = LoadSound_Strict("SFX\Room\035Chamber\TentacleIdle.ogg")
				EndIf
				n\SoundChn2 = LoopSound2(n\Sound2,n\SoundChn2,Camera,n\Collider)
			Else
				UpdateSoundOrigin(n\SoundChn2, Camera, n\Collider)
			EndIf
		Else
			If n\SoundChn <> 0 Then
				StopChannel n\SoundChn
				n\SoundChn = 0
				FreeSound_Strict n\Sound
				n\Sound = 0
			EndIf
			If n\SoundChn2 <> 0 Then
				StopChannel n\SoundChn2
				n\SoundChn2 = 0
				FreeSound_Strict n\Sound2
				n\Sound2 = 0
			EndIf
			Local dieFrame# = 180.5
			AnimateNPC(n, 285, 180, -0.8, False)
			If n\Frame <= dieFrame
				RemoveNPC(n)
				Return
			EndIf
		EndIf
		
		PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider), EntityZ(n\Collider))
		RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider)-180, EntityRoll(n\Collider), True
	ElseIf n\State[0] = TENTACLE_FREEZE Then
		; ~ Nothing
	Else
		If (Not n\Target\IsDead) Then
			If n\State[0] = TENTACLE_SPAWN Then
				If n\Frame>286 Then
					AnimateNPC(n, 286, 357, 0.3, False)
					
					If n\Frame>=356.5 Then
						n\State[0] = TENTACLE_IDLE
						FreeSound_Strict n\Sound2 : n\Sound2=0
					EndIf
				Else
					SetNPCFrame(n, 287)
					n\Sound2 = LoadSound_Strict("SFX\Room\035Chamber\TentacleSpawn.ogg")
					n\SoundChn2 = PlaySound2(n\Sound2, Camera, n\Target\Collider)
				EndIf
			Else
				If n\State[0] = TENTACLE_IDLE Then
					AnimateNPC(n, 33, 174, Rnd(0.1,0.5), True)
					If n\Target\State[0] = MP035_STATE_ATTACK Then
						n\State[0] = TENTACLE_ATTACK
					EndIf
				Else
					If n\Frame>33 And n\Frame<174 Then
						AnimateNPC(n, 33, 174, Rnd(2.0,4.0), False)
					Else
						If n\Frame>33 Then
							n\Frame = 2
							If n\Sound <> 0 Then
								FreeSound_Strict n\Sound : n\Sound=0
								StopChannel n\SoundChn
							EndIf
							n\Sound = LoadSound_Strict("SFX\Room\035Chamber\TentacleAttack"+Rand(1,2)+".ogg")
							n\SoundChn = PlaySound2(n\Sound, Camera, n\Target\Collider)
						EndIf
						AnimateNPC(n, 2, 32, 0.3, False)
						
						If n\Frame>=5 And n\Frame<6 Then
							If EntityDistanceSquared(n\Target\Collider,Collider) < 3.24 Then
								If Abs(DeltaYaw(n\Target\Collider,Collider))<20 Then 
									PlaySound2(DamageSFX[Rand(2,3)],Camera,n\Target\Collider)
									If psp\Kevlar>0 Then
										PlaySound_Strict(NTF_PainSFX[Rand(0,7)])
									Else
										PlaySound_Strict(NTF_PainWeakSFX[Rand(0,1)])
									EndIf
									DamageSPPlayer(Rand(19,28),False)
									If psp\Health <= 0 Then
										m_msg\DeathTxt = GetLocalString("Singleplayer","contact_lost")
									EndIf
								EndIf
							EndIf
							
							n\Frame = 6
						ElseIf n\Frame=32
							n\State[0] = TENTACLE_IDLE
							n\Frame = 173
						EndIf
					EndIf
				EndIf
			EndIf
			
			If n\GotHit Then
				n\Target\HP = n\Target\HP - n\GotHit
			EndIf
			
			UpdateSoundOrigin(n\SoundChn, Camera, n\Collider)
			If n\State[0] <> TENTACLE_SPAWN Then
				If n\Sound2=0 Then
					n\Sound2 = LoadSound_Strict("SFX\Room\035Chamber\TentacleIdle.ogg")
				EndIf
				n\SoundChn2 = LoopSound2(n\Sound2,n\SoundChn2,Camera,n\Collider)
			Else
				UpdateSoundOrigin(n\SoundChn2, Camera, n\Collider)
			EndIf
		Else
			If n\SoundChn <> 0 Then
				StopChannel n\SoundChn
				n\SoundChn = 0
				FreeSound_Strict n\Sound
				n\Sound = 0
			EndIf
			If n\SoundChn2 <> 0 Then
				StopChannel n\SoundChn2
				n\SoundChn2 = 0
				FreeSound_Strict n\Sound2
				n\Sound2 = 0
			EndIf
			
			If n\Frame <= 285.0 Then
				SetNPCFrame(n, 357)
			EndIf
			
			AnimateNPC(n, 357, 286, -0.8, False)
			If n\Frame <= 286.5 And n\Frame > 285.0 Then
				RemoveNPC(n)
				Return
			EndIf
		EndIf
	EndIf
	
	n\DropSpeed = 0
	
	ResetEntity n\Collider
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D