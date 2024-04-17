
; ~ Multiplayer SCP-939 constants

;[Block]
Const MP939_STATE_FREEZE = -1
Const MP939_STATE_WANDER = 0
Const MP939_STATE_DETECTED = 1
Const MP939_STATE_ATTACK = 2
Const MP939_STATE_VOCAL = 3
;[End Block]

Function CreateNPCtype939(n.NPCs)
	Local n2.NPCs
	Local temp#
	Local i
	
	Local amount939% = 0
	For n2.NPCs = Each NPCs
		If (n\NPCtype = n2\NPCtype) And (n<>n2)
			amount939% = amount939% + 1
		EndIf
	Next
	If amount939% = 0 Then i = 53
	If amount939% = 1 Then i = 89
	If amount939% = 2 Then i = 96
	n\NVName = "SCP-939-"+i
	
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.3
	EntityType n\Collider, HIT_PLAYER
	For n2.NPCs = Each NPCs
		If n\NPCtype = n2\NPCtype And n<>n2 Then
			n\obj = CopyEntity (n2\obj)
			Exit
		EndIf
	Next
	
	n\HP = 200
	
	If n\obj = 0 Then 
		n\obj = LoadAnimMesh_Strict("GFX\NPCs\scp-939.b3d")
		
		temp# = GetINIFloat("DATA\NPCs.ini", "SCP-939", "scale")/2.5
		ScaleEntity n\obj, temp, temp, temp		
	EndIf
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-939", "speed") / 100.0)
	
	n\CollRadius = 0.3
	
	CopyHitBoxes(n)
	
End Function

Function CreateNPCtype939MP(n.NPCs)
	
	n\CollRadius = 0.3
	n\Collider = CreatePivot()
	EntityRadius n\Collider, n\CollRadius
	EntityType n\Collider, HIT_NPC_MP
	
	n\obj = CopyEntity(mp_I\SCP939Model)
	
	Local temp# = GetINIFloat("DATA\NPCs.ini", "SCP-939", "scale")/2.5
	ScaleEntity n\obj, temp, temp, temp	
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-939", "speed") / 150.0)
	n\HP = 250
	n\PathTimer = 70*5
	n\NVName = "SCP-939"
	
	CopyHitBoxes(n)
	
End Function

Function UpdateNPCtype939(n.NPCs)
	Local dist#,prevFrame#,temp%,angle#
	
	If (Not n\IsDead) Then
		Select n\State[0]
			Case 0
				AnimateNPC(n, 290,405,0.1)
			Case 1
				If n\Frame=>644 And n\Frame<683 Then ;finish the walking animation
					n\CurrSpeed = CurveValue(n\Speed*0.05, n\CurrSpeed, 10.0)
					AnimateNPC(n, 644,683,28*n\CurrSpeed*4,False)
					If n\Frame=>682 Then n\Frame =175
				Else
					n\CurrSpeed = CurveValue(0, n\CurrSpeed, 5.0)
					AnimateNPC(n, 175,297,0.22,False)
					If n\Frame=>296 Then n\State[0] = 2
				EndIf
				
				n\LastSeen = 0
				
				MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor						
				
			Case 2
				n\State[1] = Max(n\State[1], (n\PrevState-3))
				
				If PlayerRoom\RoomTemplate\Name = "room3_storage" Then
					dist = EntityDistance(n\Collider, PlayerRoom\Objects[n\State[1]])
				Else
					dist = EntityDistance(n\Collider, Collider)
				EndIf
				
				n\CurrSpeed = CurveValue(n\Speed*0.3*Min(dist,1.0), n\CurrSpeed, 10.0)
				MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor 
				
				prevFrame = n\Frame
				AnimateNPC(n, 644,683,28*n\CurrSpeed) ;walk
				
				If (prevFrame<664 And n\Frame=>664) Lor (prevFrame>673 And n\Frame<654) Then
					PlaySound2(StepSFX(4, 0, Rand(0,3)), Camera, n\Collider, 12.0)
					If Rand(10)=1 Then
						temp = False
						If n\SoundChn = 0 Then 
							temp = True
						ElseIf Not ChannelPlaying(n\SoundChn)
							temp = True
						EndIf
						If temp Then
							If n\Sound <> 0 Then FreeSound_Strict n\Sound : n\Sound = 0
							n\Sound = LoadSound_Strict("SFX\SCP\939\"+(n\ID Mod 3)+"Lure"+Rand(1,10)+".ogg")
							PlayNPCSound(n, n\Sound)
						EndIf
					EndIf
				EndIf
				
				
				If PlayerRoom\RoomTemplate\Name = "room3_storage" Then
					PointEntity n\obj, PlayerRoom\Objects[n\State[1]]
				Else
					PointEntity n\obj, n\Collider
				EndIf
				RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),20.0), 0
				
				If dist<0.4 Then
					n\State[1] = n\State[1] + 1
					If n\State[1] > n\PrevState Then n\State[1] = (n\PrevState-3)
					n\State[0] = 1
				EndIf
				
			Case 3
				If EntityVisible(Collider, n\Collider) Then
					If n\Sound2 = 0 Then n\Sound2 = LoadSound_Strict("SFX\General\Slash1.ogg")
					
					n\EnemyX = EntityX(Collider)
					n\EnemyZ = EntityZ(Collider)
					n\LastSeen = 10*7
				EndIf
					
				If n\LastSeen > 0 And (Not NoTarget) Then
					prevFrame = n\Frame
					
					If (n\Frame=>18.0 And n\Frame<68.0) Then
						n\CurrSpeed = CurveValue(0, n\CurrSpeed, 5.0)
						AnimateNPC(n, 18,68,0.5,True)
						
						;hasn't hit
						temp = False
						
						If prevFrame < 24 And n\Frame>=24 Then
							temp = True
						ElseIf prevFrame < 57 And n\Frame>=57
							temp = True
						EndIf
						
						If temp Then
							If DistanceSquared(n\EnemyX, EntityX(n\Collider), n\EnemyZ, EntityZ(n\Collider))<PowTwo(1.5) Then
								PlayNPCSound(n, n\Sound2)
								;Injuries = Injuries + Rnd(1.5, 2.5)-wbl\Vest*0.5
								If (Not GodMode) Then
									DamageSPPlayer(Rand(20,30))
									BlurTimer = 500
								EndIf
							Else
								n\Frame	 = 449
							EndIf
						EndIf
						
						If (Not IsSPPlayerAlive()) Then 
							m_msg\DeathTxt=Chr(34)+"All four (4) escaped SCP-939 specimens have been captured and recontained successfully. "
							m_msg\DeathTxt=m_msg\DeathTxt+"Three (3) of them made quite a mess at Storage Area 6. A cleaning team has been dispatched."+Chr(34)
							Kill()
							If (Not GodMode) Then n\State[0] = 5
						EndIf								
					Else
						If n\LastSeen = 10*7 Then 
							n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
								
							AnimateNPC(n, 449,464,6*n\CurrSpeed) ;run
								
							If (prevFrame<452 And n\Frame=>452) Lor (prevFrame<459 And n\Frame=>459) Then
								PlaySound2(StepSFX(1, 1, Rand(0,7)), Camera, n\Collider, 12.0)
							EndIf
								
							If DistanceSquared(n\EnemyX, EntityX(n\Collider), n\EnemyZ, EntityZ(n\Collider))<PowTwo(1.1) Then ;player is visible
								n\Frame = 18
							EndIf
						Else
							n\CurrSpeed = CurveValue(0, n\CurrSpeed, 5.0)
							AnimateNPC(n, 175,297,5*n\CurrSpeed,True)
						EndIf
							
					EndIf
						
					angle = VectorYaw(n\EnemyX-EntityX(n\Collider), 0.0, n\EnemyZ-EntityZ(n\Collider))
					RotateEntity n\Collider, 0, CurveAngle(angle,EntityYaw(n\Collider),15.0), 0									
						
					MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor							
						
					n\LastSeen = n\LastSeen - FPSfactor
				Else
					n\State[0] = 2
				EndIf
			Case 5
				If n\Frame<68 Then
					AnimateNPC(n, 18,68,0.5,False) ;finish the attack animation
				Else
					AnimateNPC(n, 464,473,0.5,False) ;attack to idle
				EndIf
					
		End Select
	Else
		If Rand(1,2)=1
			If n\Frame => 174
				SetNPCFrame(n, 174)
			Else
				AnimateNPC(n, 165, 174, 0.5, False)
			EndIf
		Else
			If n\Frame => 164
				SetNPCFrame(n, 174)
			Else
				AnimateNPC(n, 73, 164, 0.5, False)
			EndIf
		EndIf
	EndIf
		
	If n\HP <= 0
		n\IsDead = True
	EndIf
		
	If n\State[0] < 3 And (Not NoTarget) And (Not n\IgnorePlayer) Then
		dist = EntityDistance(n\Collider, Collider)
			
		If dist < 4.0 Then dist = dist - EntityVisible(Collider, n\Collider) ;TODO wtf is this?
			If PlayerSoundVolume*1.2>dist Lor dist < 1.5 Then
				If n\State[2] = 0 Then
					If n\Sound <> 0 Then FreeSound_Strict n\Sound : n\Sound = 0
					n\Sound = LoadSound_Strict("SFX\SCP\939\"+(n\ID Mod 3)+"Attack"+Rand(1,3)+".ogg")
					PlayNPCSound(n, n\Sound)										
					
					PlayNPCSound(n, LoadTempSound("SFX\SCP\939\attack.ogg"))
					n\State[2] = 1
				EndIf
				
				n\State[0] = 3
			ElseIf PlayerSoundVolume*1.6>dist
				If n\State[0]<>1 And n\Reload <= 0 Then
					If n\Sound <> 0 Then FreeSound_Strict n\Sound : n\Sound = 0
					n\Sound = LoadSound_Strict("SFX\SCP\939\"+(n\ID Mod 3)+"Alert"+Rand(1,3)+".ogg")
					PlayNPCSound(n, n\Sound)	
					
					n\Frame = 175
					n\Reload = 70 * 3	
				EndIf
				
				n\State[0] = 1
			
			EndIf
			
		n\Reload = n\Reload - FPSfactor
			
	EndIf				
		
	RotateEntity n\Collider, 0, EntityYaw(n\Collider), 0, True	
		
	PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider)-0.28, EntityZ(n\Collider))
	RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider), EntityRoll(n\Collider), True					
	
End Function

Function UpdateNPCtype939MP(n.NPCs)
	
	Local w.WayPoints,n2.NPCs, cmsg.ChatMSG
	Local dist#,prevFrame#,yaw#,shouldTime%
	Local i,j
	
	prevFrame = n\Frame
	
	If (Not n\IsDead)
		Select n\State[0]
			Case MP939_STATE_FREEZE
				;[Block]
				;do nothing
				;[End Block]
			Case MP939_STATE_WANDER ;Wandering around
				;[Block]
				If n\PathStatus=1 Then
					While n\Path[n\PathLocation]=Null
						If n\PathLocation >= 19
							n\PathLocation = 0 : n\PathStatus = 0 : Exit
						Else
							n\PathLocation = n\PathLocation + 1
						EndIf
					Wend
					If n\PathStatus=1 Then
						PointEntity n\obj, n\Path[n\PathLocation]\obj
						If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
						
						AnimateNPC(n,1,41,n\CurrSpeed*30)
						n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
						
						dist = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
						If dist < 0.49 Then
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
						
						AnimateNPC(n,1,41,n\CurrSpeed*30)
						n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
					EndIf
				EndIf
				
				If (prevFrame<21 And n\Frame=>21) Lor (prevFrame>n\Frame And n\Frame<2) Then
					PlaySound2(StepSFX(4, 0, Rand(0,3)), Camera, n\Collider, 10.0)
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					If EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider)<(25.0/(1+Players[n\ClosestPlayer]\CrouchState)) Then
						If EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider) Then
							n\State[0] = MP939_STATE_DETECTED
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
			Case MP939_STATE_DETECTED ;Player detected
				;[Block]
				PointEntity n\obj, Players[n\ClosestPlayer]\Collider
				If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
				
				AnimateNPC(n, 42, 56, n\CurrSpeed*10)
				n\CurrSpeed = CurveValue(n\Speed*1.4, n\CurrSpeed, 20.0)
				MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
				
				If (prevFrame<45 And n\Frame=>45) Lor (prevFrame<52 And n\Frame=>52) Then
					PlaySound2(StepSFX(4, 0, Rand(0,3)), Camera, n\Collider, 10.0)
				EndIf
				
				dist = EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider)
				If dist<1.69 Then
					If (Abs(DeltaYaw(n\Collider,Players[n\ClosestPlayer]\Collider))<=60.0) Then
						n\State[0] = MP939_STATE_ATTACK
						If Rand(2)=1 Then
							SetNPCFrame(n,57)
						Else
							SetNPCFrame(n,86)
						EndIf
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0
					If dist>56.25 Lor (Not EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider)) Then
						n\State[0] = MP939_STATE_WANDER
						n\PathTimer = 0.0
						n\PathStatus = 0
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSfactor
				EndIf
				
				If mp_I\Gamemode\Timer939 < TIMER_939_MAX Then
					If n\ClosestPlayer = mp_I\PlayerID Then
						shouldTime = True
						For n2 = Each NPCs
							If n2 <> n And n2\NPCtype = n\NPCtype And n2\State[0] = n\State[0] And n2\ClosestPlayer = n\ClosestPlayer Then
								If n2\ID < n\ID Then
									shouldTime = False
								EndIf
								Exit
							EndIf
						Next
						If shouldTime Then
							mp_I\Gamemode\Timer939 = mp_I\Gamemode\Timer939 + FPSfactor
							If mp_I\Gamemode\Timer939 >= TIMER_939_MAX Then
								If opt\SteamEnabled Then Steam_Achieve(ACHV_939_5MIN)
							EndIf
						EndIf
					EndIf
				EndIf
				;[End Block]
			Case MP939_STATE_ATTACK ;Attacking
				;[Block]
				Local shouldFrame#
				Local restartFrame#
				Local finalFrame#
				If n\Frame < 86 Then
					AnimateNPC(n, 57, 85, 0.6, False)
					shouldFrame = 62
					finalFrame = 84.5
				Else
					AnimateNPC(n, 86, 108, 0.6, False)
					shouldFrame = 92
					finalFrame = 107.5
				EndIf
				dist = EntityDistanceSquared(n\Collider,Players[n\ClosestPlayer]\Collider)
				yaw = Abs(DeltaYaw(n\Collider,Players[n\ClosestPlayer]\Collider))
				If (dist<2.56) Then
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
								DamagePlayer(n\ClosestPlayer,Rand(20+5*mp_I\Gamemode\Difficulty,30+5*mp_I\Gamemode\Difficulty),Rand(25+5*mp_I\Gamemode\Difficulty,35+5*mp_I\Gamemode\Difficulty),5)
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
						n\State[0] = MP939_STATE_VOCAL
					Else
						If dist>2.25 Then
							n\State[0] = MP939_STATE_VOCAL
						EndIf
						If (yaw>60.0) Then
							n\State[0] = MP939_STATE_VOCAL
						EndIf
					EndIf
					If n\State[0] = MP939_STATE_ATTACK Then
						If Rand(2)=1 Then
							restartFrame = 57
						Else
							restartFrame = 86
						EndIf
						SetNPCFrame(n,restartFrame)
					EndIf
				EndIf
				;[End Block]
			Case MP939_STATE_VOCAL ;Vocalize screams
				;[Block]
				AnimateNPC(n, 109, 279, 0.5, False)
				If n\State[2] = 0 Then
					n\State[2] = Rand(1,9)
				EndIf
				If prevFrame < 136 And n\Frame >= 136 Then
					If ChannelPlaying(n\SoundChn) Then
						StopChannel(n\SoundChn)
					EndIf
					If n\Sound<>0 Then
						FreeSound_Strict n\Sound
						n\Sound = 0
					EndIf
					n\Sound = LoadSound_Strict("SFX\SCP\939\"+((Int(n\State[2])-1) Mod 3)+"Attack"+(Int(Ceil(Int(n\State[2]+2)/3)))+".ogg")
					n\SoundChn = PlaySound2(n\Sound, Camera, n\Collider, 20)
				EndIf
				If n\Frame >= 278.5 Then
					n\State[0] = MP939_STATE_DETECTED
					n\State[2] = 0
				EndIf
				;[End Block]
		End Select
		
		UpdateSoundOrigin(n\SoundChn, Camera, n\Collider, 20)
		
		If n\HP<=0 Then
			n\IsDead=True
			EntityType n\Collider,HIT_DEAD
			If Rand(2)=1 Then
				SetNPCFrame(n,279)
			Else
				SetNPCFrame(n,373)
			EndIf
			MoveEntity n\Collider,0,0.01,0
		EndIf
	Else
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		Local dieFrame#
		If n\Frame < 373 Then
			AnimateNPC(n, 279, 372, 0.5, False)
			dieFrame = 288.5
		Else
			AnimateNPC(n, 373, 382, 0.5, False)
			dieFrame = 381.5
		EndIf
		If n\Frame >= dieFrame
			If n\State[1] < 70*5
				n\State[1] = n\State[1] + FPSfactor
			Else
				If n\State[1] >= 70*5 And n\State[1] < 1000
					n\State[1] = 1000
				ElseIf n\State[1] >= 1000 And n\State[1] < 2000
					EntityAlpha n\obj,Inverse((n\State[1]-1000.0)/1000.0)
					n\State[1] = n\State[1] + 2*FPSfactor
				Else
					RemoveNPC(n)
					Return
				EndIf
			EndIf
		EndIf
	EndIf
	
	PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider)-0.28, EntityZ(n\Collider))
	RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider), EntityRoll(n\Collider), True
	
	EntityAutoFade(n\obj,GetCameraFogRangeFar(Camera)-0.5,GetCameraFogRangeFar(Camera)+0.5)
	
End Function






;~IDEal Editor Parameters:
;~C#Blitz3D