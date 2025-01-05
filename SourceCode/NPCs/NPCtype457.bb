
; ~ SCP-457 Constants

;[Block]
Const SCP457_WALK = 0
Const SCP457_ATTACK = 1
Const SCP457_STATE_FREEZE = -1
Const SCP457_STATE_WANDER = 0
Const SCP457_STATE_DETECTED = 1
Const SCP457_STATE_ATTACK = 2
Const SCP457_STATE_STUNNED = 3
;[End Block]

Function CreateNPCtype457(n.NPCs)
	Local temp#
	
	n\BossName = "SCP-457"
	n\NVName = "SCP-457"
	n\Collider = CreatePivot()
	n\CollRadius = 0.15
	EntityRadius n\Collider, n\CollRadius, 0.2
	EntityType n\Collider, HIT_NPC_MP
	
	n\obj = CopyEntity(NPCModel[Model_457])
	
	temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-106", "scale") / 2.2)		
	ScaleEntity n\obj, temp, temp, temp
	
	n\Speed = (1.6 / 100.0)
	
	n\HP = 4500
	n\MaxBossHealth = n\HP
	n\Boss = n
	
	n\PathTimer = 70*5
	
	n\Sound = LoadSound_Strict("SFX\SCP\457\FireLoop.ogg")
	n\Sound2 = LoadSound_Strict("SFX\SCP\457\Alert.ogg")
	
End Function

Function UpdateNPCtype457(n.NPCs)
	Local p.Particles, cmsg.ChatMSG
	Local dist#,prevFrame#,yaw#
	Local i,j
	
	prevFrame = n\Frame
	
	If (Not n\IsDead)
		
		ShouldPlay = MUS_457_FIGHT
		
		Select n\State[0]
			Case SCP457_STATE_FREEZE
				;[Block]
				;do nothing
				;[End Block]
			Case SCP457_STATE_WANDER ;Wandering around
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
						RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
						
						AnimateNPC(n, 7, 56, n\CurrSpeed)
						n\CurrSpeed = CurveValue(n\Speed*0.7*n\State[1], n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
						
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
						n\PathTimer = n\PathTimer - FPSFACTOR
						
						PointEntity n\obj, Collider
						RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
						
						AnimateNPC(n, 7, 56, n\CurrSpeed)
						n\CurrSpeed = CurveValue(n\Speed*0.7*n\State[1], n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					If EntityDistanceSquared(n\obj,Collider) < PowTwo(7.5) Then
						If EntityVisible(n\obj,Collider) Then
							n\State[0] = SCP457_STATE_DETECTED
						EndIf
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSfactor
				EndIf
				;[End Block]
			Case SCP457_STATE_DETECTED ;Player detected
				;[Block]
				PointEntity n\obj, Collider
				RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
				
				AnimateNPC(n, 7, 56, n\CurrSpeed)
				n\CurrSpeed = CurveValue(n\Speed*1.5*n\State[1], n\CurrSpeed, 20.0)
				MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
				
				dist = EntityDistanceSquared(n\obj,Collider)
				If dist<0.5625 Then
					If (Abs(DeltaYaw(n\Collider,Collider))<=60.0) Then
						n\State[0] = SCP457_STATE_ATTACK
					EndIf
				EndIf
				
				If n\LastSeen = 0 Then
					If EntityInView(n\obj, Camera) Then
						PlaySound_Strict(n\Sound2)
						n\LastSeen = 1
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					If dist>56.25 Lor (Not EntityVisible(n\obj,Collider)) Then
						n\State[0] = SCP457_STATE_WANDER
						n\PathTimer = 0.0
						n\PathStatus = 0
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSfactor
				EndIf
				;[End Block]
			Case SCP457_STATE_ATTACK ;Attacking
				;[Block]
				Local restartFrame# = 0
				Local finalFrame# = 5.5
				AnimateNPC(n, 0, 6, 0.5, False)
				If n\LastSeen = 0.0 Then
					If EntityDistanceSquared(Collider,n\Collider) < PowTwo(2.0) Then
						If psp\Health > 0 And (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
							psp\Health = Max(psp\Health - 2, 0)
							If psp\Health <= 0 Then
								;PlaySound_Strict(LoadTempSound("SFX\SCP\294\burn.ogg"))
								m_msg\DeathTxt = ""
							EndIf
						ElseIf Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" And hds\Health > 35 Then
							hds\Health = Max(hds\Health - 1, 0)
						ElseIf Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" And hds\Health <= 35 Then
							psp\Health = Max(psp\Health - 2, 0)
							hds\Health = Max(hds\Health - 1, 0)
						EndIf
					EndIf
					n\LastSeen = 4
				Else
					n\LastSeen = Max(n\LastSeen-FPSFACTOR,0.0)
				EndIf
				If n\Frame => finalFrame Then
					If Collider=0 Lor psp\Health<=0 Then
						n\State[0] = SCP457_STATE_WANDER
					Else
						If EntityDistanceSquared(n\Collider,Collider)>1.0 Then
							n\State[0] = SCP457_STATE_DETECTED
						EndIf
					EndIf
					If n\State[0] = SCP457_STATE_ATTACK Then
						SetNPCFrame(n,restartFrame)
					EndIf
				EndIf
				;[End Block]
			Case SCP457_STATE_STUNNED ;Stunned
				;[Block]
				AnimateNPC(n, 57, 218, 0.4, False)
				If n\State[2] = 0 Then
					n\State[2] = Rand(1,9)
				EndIf
				If prevFrame < 64.0 And n\Frame >= 64.0 Then
					If ChannelPlaying(n\SoundChn2) Then
						StopChannel(n\SoundChn2)
					EndIf
					If n\Sound2<>0 Then
						FreeSound_Strict n\Sound2
						n\Sound2 = 0
					EndIf
					n\Sound2 = LoadSound_Strict("SFX\SCP\457\Scream"+(Int(Ceil(Int(n\State[2]+2)/3)))+".ogg")
					n\SoundChn2 = PlaySound2(n\Sound2, Camera, n\Collider, 40, 4.0)
				EndIf
				p.Particles = CreateParticle(EntityX(n\Collider), EntityY(n\Collider) - 0.25, EntityZ(n\Collider), 6, 0.5, -0.75, 30)
				p\Achange = -0.015
				If n\Frame >= 217.5 Then
					n\State[0] = SCP457_STATE_DETECTED
					n\State[1] = 5.0
					n\State[2] = 0
				EndIf
				;[End Block]
		End Select
		
		dist = EntityDistanceSquared(n\Collider,Collider)
		If dist < PowTwo(GetCameraFogRangeFar(Camera)) Then
			If Rand(3)=1 Then
				CreateParticle(EntityX(n\Collider), EntityY(n\Collider) - 0.25, EntityZ(n\Collider), 13, 0.75/(1+(n\State[0]=SCP457_STATE_STUNNED)), -1.0, 25)
				CreateParticle(EntityX(n\Collider), EntityY(n\Collider) + 0.95, EntityZ(n\Collider), 0, 0.75, -1.0, 10)
			EndIf
			EntityAlpha n\obj,Rnd(0.2,0.3)
		Else
			EntityAlpha n\obj,0.0
		EndIf
		n\SoundChn = LoopSound2(n\Sound,n\SoundChn,Camera,n\Collider,5)
		
		If n\HP<=0 Then
			n\IsDead=True
			EntityType n\Collider,HIT_DEAD
			MoveEntity n\Collider,0,0.01,0
			EntityAlpha n\obj,0.0
		Else
			n\Boss = n
		EndIf
		
		n\State[1] = Max((n\State[1] - (FPSfactor / 150.0)),1.0) ;Use it as an "Aggression" state for SCP-457
	Else
		n\State[3] = n\State[3] + FPSfactor
		If n\State[3] > 0 And n\State[3] < 70*10 Then
			If Rand(2)=1 Then
				CreateParticle(EntityX(n\Collider), EntityY(n\Collider) - 0.25, EntityZ(n\Collider), 13, 0.3, -1.0, 20)
			EndIf
			n\SoundChn = LoopSound2(n\Sound,n\SoundChn,Camera,n\Collider,5)
		EndIf
	EndIf
	
	RotateEntity n\obj,0,EntityYaw(n\Collider),0
	PositionEntity n\obj,EntityX(n\Collider),EntityY(n\Collider)-0.2,EntityZ(n\Collider)
	
End Function

Function CreateNPCtype457MP(n.NPCs)
	Local temp#
	
	n\NVName = "SCP-457"
	n\Collider = CreatePivot()
	n\CollRadius = 0.15
	EntityRadius n\Collider, n\CollRadius, 0.2
	EntityType n\Collider, HIT_NPC_MP
	
	n\obj = CopyEntity(mp_I\BossModel)
	temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-106", "scale") / 2.2)		
	ScaleEntity n\obj, temp, temp, temp
	
	n\Speed = (1.6 / 100.0)
	
	n\HP = 4500
	mp_I\MaxBossHealth = n\HP
	mp_I\BossNPC = n
	
	n\PathTimer = 70*5
	
	n\Sound = LoadSound_Strict("SFX\SCP\457\FireLoop.ogg")
	
End Function

Function UpdateNPCtype457MP(n.NPCs)
	Local p.Particles, cmsg.ChatMSG
	Local dist#,prevFrame#,yaw#
	Local i,j
	
	prevFrame = n\Frame
	
	If (Not n\IsDead)
		
		ShouldPlay = MUS_457_FIGHT
		
		Select n\State[0]
			Case SCP457_STATE_FREEZE
				;[Block]
				;do nothing
				;[End Block]
			Case SCP457_STATE_WANDER ;Wandering around
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
						If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
						
						AnimateNPC(n, 7, 56, n\CurrSpeed)
						n\CurrSpeed = CurveValue(n\Speed*0.7*n\State[1], n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSFACTOR
						
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
						n\PathTimer = n\PathTimer - FPSFACTOR
						
						PointEntity n\obj, Players[n\ClosestPlayer]\Collider
						If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
						
						AnimateNPC(n, 7, 56, n\CurrSpeed)
						n\CurrSpeed = CurveValue(n\Speed*0.7*n\State[1], n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSFACTOR
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					If EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider) < PowTwo(7.5) Then
						If EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider) Then
							n\State[0] = SCP457_STATE_DETECTED
						EndIf
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSFACTOR
				EndIf
				
				If mp_I\Gamemode\EnemyCount <= MinEnemyLeft Then
					If n\BlinkTimer <= 0.0 Then
						CreateOverHereParticle(EntityX(n\Collider),EntityY(n\Collider)+0.5,EntityZ(n\Collider))
						n\BlinkTimer = 70*5
					Else
						n\BlinkTimer = n\BlinkTimer - FPSFACTOR
					EndIf
				EndIf
				;[End Block]
			Case SCP457_STATE_DETECTED ;Player detected
				;[Block]
				PointEntity n\obj, Players[n\ClosestPlayer]\Collider
				If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
				
				AnimateNPC(n, 7, 56, n\CurrSpeed)
				n\CurrSpeed = CurveValue(n\Speed*1.5*n\State[1], n\CurrSpeed, 20.0)
				MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSFACTOR
				
				dist = EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider)
				If dist<0.5625 Then
					If (Abs(DeltaYaw(n\Collider,Players[n\ClosestPlayer]\Collider))<=60.0) Then
						n\State[0] = SCP457_STATE_ATTACK
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					If dist>56.25 Lor (Not EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider)) Then
						n\State[0] = SCP457_STATE_WANDER
						n\PathTimer = 0.0
						n\PathStatus = 0
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSFACTOR
				EndIf
				;[End Block]
			Case SCP457_STATE_ATTACK ;Attacking
				;[Block]
				Local restartFrame# = 0
				Local finalFrame# = 5.5
				AnimateNPC(n, 0, 6, 0.5, False)
				If n\LastSeen = 0.0 Then
					If mp_I\PlayState = GAME_SERVER Then
						For i = 0 To (mp_I\MaxPlayers-1)
							If Players[i]<>Null Then
								If EntityDistanceSquared(Players[i]\Collider,n\Collider) < PowTwo(2.0) Then
									If Players[i]\CurrHP > 0 Then
										Players[i]\CurrHP = Max(Players[i]\CurrHP - 2, 0)
										If Players[i]\CurrHP <= 0 Then
											cmsg = AddChatMSG("death_killedby", 0, SERVER_MSG_IS, CHATMSG_TYPE_TWOPARAM_TRANSLATE)
											cmsg\Msg[1] = Players[n\ClosestPlayer]\Name
											cmsg\Msg[2] = n\NVName
											Players[i]\Deaths = Players[i]\Deaths + 1
										EndIf
									EndIf
								EndIf
							EndIf
						Next
					EndIf
					n\LastSeen = 4
				Else
					n\LastSeen = Max(n\LastSeen-FPSfactor,0.0)
				EndIf
				If n\Frame => finalFrame Then
					If Players[n\ClosestPlayer]\Collider=0 Lor Players[n\ClosestPlayer]\CurrHP<=0 Then
						n\State[0] = SCP457_STATE_WANDER
					Else
						If EntityDistanceSquared(n\Collider,Players[n\ClosestPlayer]\Collider)>1.0 Then
							n\State[0] = SCP457_STATE_DETECTED
						EndIf
					EndIf
					If n\State[0] = SCP457_STATE_ATTACK Then
						SetNPCFrame(n,restartFrame)
					EndIf
				EndIf
				;[End Block]
			Case SCP457_STATE_STUNNED ;Stunned
				;[Block]
				AnimateNPC(n, 57, 218, 0.4, False)
				If n\State[2] = 0 Then
					n\State[2] = Rand(1,9)
				EndIf
				If prevFrame < 64.0 And n\Frame >= 64.0 Then
					If ChannelPlaying(n\SoundChn2) Then
						StopChannel(n\SoundChn2)
					EndIf
					If n\Sound2<>0 Then
						FreeSound_Strict n\Sound2
						n\Sound2 = 0
					EndIf
					n\Sound2 = LoadSound_Strict("SFX\SCP\457\Scream"+(Int(Ceil(Int(n\State[2]+2)/3)))+".ogg")
					n\SoundChn2 = PlaySound2(n\Sound2, Camera, n\Collider, 40, 4.0)
				EndIf
				p.Particles = CreateParticle(EntityX(n\Collider), EntityY(n\Collider) - 0.25, EntityZ(n\Collider), 6, 0.5, -0.75, 30)
				p\Achange = -0.015
				If n\Frame >= 217.5 Then
					n\State[0] = SCP457_STATE_DETECTED
					n\State[1] = 5.0
					n\State[2] = 0
				EndIf
				;[End Block]
		End Select
		
		dist = EntityDistanceSquared(n\Collider,Players[n\ClosestPlayer]\Collider)
		If dist < PowTwo(GetCameraFogRangeFar(Camera)) Then
			If Rand(3)=1 Then
				CreateParticle(EntityX(n\Collider), EntityY(n\Collider) - 0.25, EntityZ(n\Collider), 13, 0.75/(1+(n\State[0]=SCP457_STATE_STUNNED)), -1.0, 25) ;EntityY(n\Collider) - 0.45
				;CreateParticle(EntityX(n\Collider), EntityY(n\Collider) + 0.95, EntityZ(n\Collider), 0, 0.75, -1.0, 10) ;EntityY(n\Collider) + 0.75
			EndIf
			EntityAlpha n\obj,Rnd(0.2,0.3)
		Else
			EntityAlpha n\obj,0.0
		EndIf
		n\SoundChn = LoopSound2(n\Sound,n\SoundChn,Camera,n\Collider,5)
		
		If n\HP<=0 Then
			n\IsDead=True
			EntityType n\Collider,HIT_DEAD
			MoveEntity n\Collider,0,0.01,0
			EntityAlpha n\obj,0.0
			If (mp_I\Gamemode\Phase/2) = Waves_Long And mp_I\Gamemode\Difficulty = KETER And mp_I\Gamemode\CanSurviveAllWaves = True Then
				If opt\SteamEnabled Then Steam_Achieve(ACHV_BURNING_MAN)
				GiveAchievement(Achv457)
			EndIf	
		Else
			mp_I\BossNPC = n
		EndIf
		
		n\State[1] = Max((n\State[1] - (FPSfactor / 150.0)),1.0) ;Use it as an "Aggression" state for SCP-457
	Else
		If Rand(2)=1 Then
			CreateParticle(EntityX(n\Collider), EntityY(n\Collider) - 0.25, EntityZ(n\Collider), 13, 0.3, -1.0, 20)
		EndIf
		n\SoundChn = LoopSound2(n\Sound,n\SoundChn,Camera,n\Collider,5)
	EndIf
	
	RotateEntity n\obj,0,EntityYaw(n\Collider),0
	PositionEntity n\obj,EntityX(n\Collider),EntityY(n\Collider)-0.2,EntityZ(n\Collider)
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS