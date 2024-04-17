
; ~ SCP-076-2 Singleplayer constants

;[Block]
Const SCP076_FREEZE = -1
Const SCP076_WANDER = 0
Const SCP076_EVENT_WANDER = 1
Const SCP076_DETECTED = 2
Const SCP076_ATTACK = 3
Const SCP076_STUNNED = 4
Const SCP076_INTRO = 5
Const SCP076_RUN = 6
Const SCP076_RUN_2 = 7
;[End Block]

Function CreateNPCtype076(n.NPCs)
	Local temp#
	
	n\BossName = "SCP-076-2"
	n\NPCName = "SCP-076-2"
	n\NVName = "Human"
	n\Collider = CreatePivot()
	n\CollRadius = 0.32
	EntityRadius n\Collider, n\CollRadius
	EntityType n\Collider, HIT_PLAYER
	
	n\obj = CopyEntity(NPCModel[Model_076])
	
	n\texture = "GFX\npcs\scp_076_2.png"
	
	temp# = 0.5 / MeshWidth(n\obj)
	ScaleEntity n\obj, temp*2, temp*2, temp*2
	
	n\Speed = 3.0 / 100
	
	MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	
	If SelectedDifficulty\Name = "Keter" Lor SelectedDifficulty\Name = "Thaumiel" Lor SelectedDifficulty\Name = "Appolyon" Then
		n\HP = 22000
	Else
		n\HP = 18000
	EndIf
	n\MaxBossHealth = n\HP
	n\Boss = n
	
	CopyHitBoxes(n)
	
End Function

Function UpdateNPCtype076(n.NPCs)
	Local prevFrame#,sfxstep%,dist#,yaw#
	Local n2.NPCs,de.Decals,p.Particles
	Local i%
	
	RotateEntity(n\Collider, 0, EntityYaw(n\Collider), EntityRoll(n\Collider), True)
	
	prevFrame = n\Frame
	
	Local agitateHealth% = n\MaxBossHealth / 2
	
	If (Not n\IsDead) Then
		
		If (Not n\State[0] = SCP076_EVENT_WANDER) Then
			ShouldPlay = MUS_076_FIGHT; ~ Case Music
			n\Pause = False
		EndIf
		
		If (Not n\State[0] = SCP076_INTRO) Then
			If EntityDistanceSquared(Collider, n\Collider)>PowTwo(8.5) And (Not n\State[0] = SCP076_INTRO Lor n\State[0] = SCP076_EVENT_WANDER) Then
				n\State[0] = SCP076_WANDER
				ShouldPlay = MUS_076_PAUSE; ~ Pause Music
				n\Pause = True
			EndIf
		EndIf
		
		If n\State[0] = SCP076_WANDER And (I_268\Using > 0 And I_268\Timer > 0.0 ) Then
			ShouldPlay = MUS_076_PAUSE; ~ Pause Music
			n\Pause = True
		EndIf
		
		If (I_268\Using > 0 And I_268\Timer > 0.0) Then
			n\State[0] = SCP076_FREEZE
		EndIf
		
		Select n\State[0]
			Case SCP076_FREEZE
				;[Block]
				;do nothing
				n\CurrSpeed = 0
				;[End Block]
			Case SCP076_INTRO
				;[Block]
				n\State[0] = SCP076_WANDER
				
				If PlayerRoom\RoomTemplate\Name = "area_076" Then
					If EntityVisible(n\obj, Collider) Then
						If (Not TaskExists(TASK_DEFEAT_076)) Then
							BeginTask(TASK_DEFEAT_076)
						EndIf
					EndIf
				EndIf
				;[End Block]
			Case SCP076_EVENT_WANDER
				;[Block]
				If n\DistanceTimer <= 0.0 Then
					If EntityDistanceSquared(n\obj,Collider)<56.25 Then
						If EntityVisible(n\obj,Collider) Then
							n\State[0] = SCP076_INTRO
						EndIf
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSfactor
				EndIf
				;[End Block]
			Case SCP076_WANDER
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
							n\State[0] = SCP076_DETECTED
						EndIf
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSfactor
				EndIf
				;[End Block]
			Case SCP076_DETECTED
				;[Block]
				PointEntity n\obj, Collider
				RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
				
				If n\LastSeen > 0 Then
					n\State[0] = SCP076_RUN
				Else
					n\CurrSpeed = CurveValue(0.015 + (0.005*(n\HP<=agitateHealth)), n\CurrSpeed, 5.0)
					AnimateNPC(n, 236, 260, n\CurrSpeed * 18)
				EndIf
				
				dist = EntityDistanceSquared(n\obj,Collider)
				If dist<0.5625 Then
					If (Abs(DeltaYaw(n\Collider,Collider))<=60.0) Then
						n\State[0] = SCP076_ATTACK
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					If dist>56.25 Lor (Not EntityVisible(n\obj,Collider)) Then
						n\State[0] = SCP076_WANDER
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
			Case SCP076_RUN
				PointEntity n\obj, Collider
				RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
				
				If n\HP <= agitateHealth Then
					n\CurrSpeed = CurveValue(0.033 + (0.005*(n\HP<=agitateHealth)), n\CurrSpeed, 5.0)
				Else
					n\CurrSpeed = CurveValue(0.03 + (0.005*(n\HP<=agitateHealth)), n\CurrSpeed, 5.0)
				EndIf
				AnimateNPC(n, 301, 319, n\CurrSpeed * 18)
				
				dist = EntityDistanceSquared(n\obj,Collider)
				If dist<0.5625 Then
					If (Abs(DeltaYaw(n\Collider,Collider))<=60.0) Then
						n\State[0] = SCP076_ATTACK
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					If dist>56.25 Lor (Not EntityVisible(n\obj,Collider)) Then
						n\State[0] = SCP076_WANDER
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
			Case SCP076_ATTACK
				;[Block]
				Local shouldFrame#
				Local restartFrame#
				Local finalFrame#
				AnimateNPC(n, 845, 870, 0.45 + (0.15*(n\LastSeen>0.0)), False)
				n\GotHit = False
				shouldFrame = 859
				restartFrame = 845
				finalFrame = 869.5
				dist = EntityDistanceSquared(n\Collider,Collider)
				yaw = Abs(DeltaYaw(n\Collider,Collider))
				If (dist<1.21) Then
					If (yaw<=60.0) Then
						If prevFrame < shouldFrame And n\Frame => shouldFrame Then
							PlaySound2(DamageSFX[Rand(5,8)],Camera,n\Collider)
							PlaySound_Strict(NTF_PainWeakSFX[Rand(0,1)])
							DamageSPPlayer(Rnd(25.0,30.0))
							m_msg\DeathTxt = Chr(34)+GetLocalString("Singleplayer","scp076_death_1")
							m_msg\DeathTxt = m_msg\DeathTxt+GetLocalStringR("Singleplayer","scp076_death_2",Designation)
							m_msg\DeathTxt = m_msg\DeathTxt+GetLocalString("Singleplayer","scp076_death_3")+Chr(34)
							If psp\Health =< 0 Then
								If TaskExists(TASK_DEFEAT_076) Then
									FailTask(TASK_DEFEAT_076)
								EndIf
							EndIf
						EndIf
					EndIf
				EndIf
				If n\Frame => finalFrame Then
					If Collider=0 Then
						n\State[0] = SCP076_WANDER
						n\GotHit = False
					Else
						If dist>1.0 Then
							If (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then
								n\State[0] = SCP076_DETECTED
							Else
								n\State[0] = SCP076_FREEZE
							EndIf
							n\GotHit = False
						EndIf
						If (yaw>60.0) Then
							If (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then
								n\State[0] = SCP076_DETECTED
							Else
								n\State[0] = SCP076_FREEZE
							EndIf
							n\GotHit = False
						EndIf
					EndIf
					If n\State[0] = SCP076_ATTACK Then
						SetNPCFrame(n, restartFrame)
						n\GotHit = False
					EndIf
				EndIf
				
				n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
				;[End Block]
			Case SCP076_STUNNED
				;[Block]
				AnimateNPC(n, 555, 630, 0.3, False)
				n\GotHit = False
				n\LastSeen = 70*10
				If prevFrame < 629.5 And n\Frame >= 629.5 Lor prevFrame = 630.0 And n\Frame = 630.0 Then
					If EntityDistanceSquared(n\obj,Collider)<56.25 Then
						If EntityVisible(n\obj,Collider) Then
							n\State[0] = SCP076_DETECTED
							n\GotHit = False
						EndIf
					EndIf
					
					If n\State[0] <> SCP076_DETECTED Then
						n\State[0] = SCP076_WANDER
						n\GotHit = False
					EndIf
				EndIf
				
				n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
				;[End Block]
		End Select
		
		If n\State[0] = SCP076_WANDER Lor n\State[0] = SCP076_RUN Lor n\LastSeen <= 0 Then
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
				
				n\State[0] = SCP076_STUNNED
				
				CreateOverHereParticle(EntityX(FindChild(n\obj,"Bip01_Spine"),True),EntityY(FindChild(n\obj,"Bip01_Spine"),True),EntityZ(FindChild(n\obj,"Bip01_Spine"),True),18)
				
				Local tex%, tex2%
				
				If tex = 0 Then
					Local swordtex$ = "GFX\NPCS\scp_076_sword_2.png"
					tex = LoadTexture_Strict(n\texture, 0, 2)
					tex2 = LoadTexture_Strict(swordtex$, 0, 2)
					TextureBlend(tex,5)
					TextureBlend(tex2,5)
					ReplaceTextureByMaterial(n\texture,n\obj,"scp_076",7)
					ReplaceTextureByMaterial(swordtex$,n\obj,"scp_076_sword",13)
				EndIf
				
				n\PrevState = 1
				RotateEntity n\obj,0,temprot,0
				SetAnimTime n\obj,tempframe
			EndIf
		EndIf
		
		If n\LastSeen <= 0.0 Then
			If n\GotHit Then
				n\State[0] = SCP076_STUNNED
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
			If TaskExists(TASK_DEFEAT_076) Then
				EndTask(TASK_DEFEAT_076)
			EndIf
			MoveEntity n\Collider,0,0.01,0
			GiveAchievement(Achv076)
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
		Local dieFrame# = 1261.5
		AnimateNPC(n, 1019, 1262, 0.5, False)
		If n\Frame >= dieFrame
			If n\State[1] < 70*5 Then
				n\State[1] = n\State[1] + FPSfactor
			Else
				If n\State[1] >= 70*5 And n\State[1] < 1000 Then
					n\State[1] = 1000
				ElseIf n\State[1] >= 1000 And n\State[1] < 2000 Then
					
					CreateParticle(EntityX(FindChild(n\obj,"Bip01_Head"),True),EntityY(FindChild(n\obj,"Bip01_Head"),True),EntityZ(FindChild(n\obj,"Bip01_Head"),True),0,0.05,-0.05)
					CreateParticle(EntityX(FindChild(n\obj,"Bip01_Spine"),True),EntityY(FindChild(n\obj,"Bip01_Spine"),True),EntityZ(FindChild(n\obj,"Bip01_Spine"),True),0,0.05,-0.05)
					CreateParticle(EntityX(FindChild(n\obj,"Bip01_L_Calf"),True),EntityY(FindChild(n\obj,"Bip01_L_Calf"),True),EntityZ(FindChild(n\obj,"Bip01_L_Calf"),True),0,0.05,-0.05)
					CreateParticle(EntityX(FindChild(n\obj,"Bip01_R_Calf"),True),EntityY(FindChild(n\obj,"Bip01_R_Calf"),True),EntityZ(FindChild(n\obj,"Bip01_R_Calf"),True),0,0.05,-0.05)
					CreateParticle(EntityX(FindChild(n\obj,"Bip01_L_Forearm"),True),EntityY(FindChild(n\obj,"Bip01_L_Forearm"),True),EntityZ(FindChild(n\obj,"Bip01_L_Forearm"),True),0,0.05,-0.05)
					CreateParticle(EntityX(FindChild(n\obj,"Bip01_R_Forearm"),True),EntityY(FindChild(n\obj,"Bip01_R_Forearm"),True),EntityZ(FindChild(n\obj,"Bip01_R_Forearm"),True),0,0.05,-0.05)
					
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

Function CreateNPCType076MP(n.NPCs)
	Local temp#
	
	n\NVName = "SCP-076-2"
	n\Collider = CreatePivot()
	n\CollRadius = 0.32
	EntityRadius n\Collider, n\CollRadius
	EntityType n\Collider, HIT_NPC_MP
	
	n\obj = CopyEntity(mp_I\BossModel)
	
	temp# = 1.0 / MeshWidth(n\obj)
	ScaleEntity n\obj, temp, temp, temp
	
	n\Speed = 2.0 / 100
	
	MeshCullBox (n\obj, -MeshWidth(mp_I\BossModel), -MeshHeight(mp_I\BossModel), -MeshDepth(mp_I\BossModel), MeshWidth(mp_I\BossModel)*2, MeshHeight(mp_I\BossModel)*2, MeshDepth(mp_I\BossModel)*2)
	
	n\HP = 15000
	mp_I\MaxBossHealth = n\HP
	mp_I\BossNPC = n
	
	CopyHitBoxes(n)
	
End Function

Function UpdateNPCtype076MP(n.NPCs)
	Local prevFrame#,sfxstep%,dist#,yaw#
	Local n2.NPCs,de.Decals,pmp.ParticleMP,cmsg.ChatMSG
	Local i%
	
	If mp_I\PlayState = GAME_SERVER Then RotateEntity(n\Collider, 0, EntityYaw(n\Collider), EntityRoll(n\Collider), True)
	
	prevFrame = n\Frame
	
	Local agitateHealth% = mp_I\MaxBossHealth / 2
	
	If (Not n\IsDead) Then
		
		ShouldPlay = MUS_076_FIGHT; ~ Case Music
		
		Select n\State[0]
			Case SCP076_FREEZE
				;[Block]
				;do nothing
				n\CurrSpeed = 0
				;[End Block]
			Case SCP076_WANDER
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
							n\State[0] = SCP076_DETECTED
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
			Case SCP076_DETECTED
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
						n\State[0] = SCP076_ATTACK
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					If dist>56.25 Lor (Not EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider)) Then
						n\State[0] = SCP076_WANDER
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
			Case SCP076_ATTACK
				;[Block]
				Local shouldFrame#
				Local restartFrame#
				Local finalFrame#
				AnimateNPC(n, 845, 870, 0.45 + (0.15*(n\LastSeen>0.0)), False)
				shouldFrame = 859
				restartFrame = 845
				finalFrame = 869.5
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
						n\State[0] = SCP076_WANDER
					Else
						If dist>1.0 Then
							n\State[0] = SCP076_DETECTED
						EndIf
						If (yaw>60.0) Then
							n\State[0] = SCP076_DETECTED
						EndIf
					EndIf
					If n\State[0] = SCP076_ATTACK Then
						SetNPCFrame(n,restartFrame)
					EndIf
				EndIf
				
				n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
				;[End Block]
			Case SCP076_STUNNED
				;[Block]
				AnimateNPC(n, 555, 630, 0.3, False)
				
				n\LastSeen = 70*10
				If prevFrame < 629.5 And n\Frame >= 629.5 Lor prevFrame = 630.0 And n\Frame = 630.0 Then
					If EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider)<56.25 Then
						If EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider) Then
							n\State[0] = SCP076_DETECTED
						EndIf
					EndIf
					
					If n\State[0] <> SCP076_DETECTED Then
						n\State[0] = SCP076_WANDER
					EndIf
				EndIf
				
				n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
				;[End Block]
		End Select
		
		If n\State[0] = SCP076_WANDER Lor n\LastSeen <= 0 Then
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
			If n\State[0] > SCP076_FREEZE And n\State[0] < SCP076_ATTACK Then
				If Abs(n\DropSpeed) < 0.1 Then
					If n\State[2] <= 0.0 Then
						n\State[2] = 70*10
					Else
						n\State[2] = n\State[2] - FPSfactor
					EndIf
				EndIf
			EndIf
		EndIf
		
		Local tex%, tex2%
		
		If n\PrevState = 0 Then
			If n\HP <= agitateHealth Then
				Local temprot# = EntityYaw(n\obj)
				RotateEntity n\obj,0,0,0
				Local tempframe# = AnimTime(n\obj)
				SetAnimTime n\obj,0
				
				If tex = 0 Then
					Local swordtex$ = "GFX\NPCS\scp_076_sword_2.png"
					tex = LoadTexture_Strict(n\texture, 0, 2)
					tex2 = LoadTexture_Strict(swordtex$, 0, 2)
					TextureBlend(tex,5)
					TextureBlend(tex2,5)
					ReplaceTextureByMaterial(n\texture,n\obj,"scp_076",7)
					ReplaceTextureByMaterial(swordtex$,n\obj,"scp_076_sword",13)
				EndIf
				
				n\PrevState = 1
				RotateEntity n\obj,0,temprot,0
				SetAnimTime n\obj,tempframe
			EndIf
		EndIf
		
		If n\LastSeen <= 0.0 Then
			If n\GotHit Then
				n\State[0] = SCP076_STUNNED
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
				GiveAchievement(Achv076)
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
		Local dieFrame# = 1261.5
		AnimateNPC(n, 1019, 1262, 0.5, False)
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
;~IDEal Editor Parameters:
;~C#Blitz3D