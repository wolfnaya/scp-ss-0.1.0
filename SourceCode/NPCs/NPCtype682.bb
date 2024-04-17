
; ~ SCP-682 Constants

;[Block]
Const SCP682_IDLE = 0
Const SCP682_WALK = 1
Const SCP682_ATTACK = 2
;[End Block]

Function CreateNPCtype682(n.NPCs)
	Local temp#
	Local tex%
	
	n\BossName = "SCP-682"
	n\NPCName = "SCP-682"
	n\NVName = "Undentified"
	n\Collider = CreatePivot()
	n\GravityMult = 0.0
	n\MaxGravity = 0.0
	EntityRadius n\Collider, 0.2
	EntityType n\Collider, HIT_PLAYER
	n\obj = CopyEntity(NPCModel[Model_682])
	
	If PlayerRoom\RoomTemplate\Name <> "gate_b_topside" Then
		temp# = 1.4 / MeshWidth(n\obj)
	Else
		temp# = 2.4 / MeshWidth(n\obj)
	EndIf
	ScaleEntity n\obj, temp, temp, temp
	
	MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-939", "speed") / 100.0)
	
	n\HP = 5000000
	n\MaxBossHealth = n\HP
	n\Boss = n
	
	;CopyHitBoxes(n)
	
End Function

Function UpdateNPCtype682(n.NPCs)
	Local w.WayPoints,n2.NPCs
	Local dist#,prevFrame#,yaw#,temp%,angle#
	Local AttackSound%
	Local i,j
	
	If (Not n\IsDead) Then
		If PlayerRoom\RoomTemplate\Name <> "gate_b_topside" Then
			ShouldPlay = MUS_682_FIGHT
		;Else
		;	ShouldPlay = MUS_CYBER_CHASE_4
		EndIf
	
		Select n\State[0]
			Case SCP682_IDLE
				;[Block]
				AnimateNPC(n, 76, 295, 0.3)
				PointEntity n\Collider, Collider
				RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 30.0), 0
				If EntityVisible(Collider, n\Collider) And (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then
					n\State[0] = SCP682_WALK
				EndIf
				;[End Block]
			Case SCP682_WALK
				;[Block]
				AnimateNPC(n, 296, 329, 14*n\CurrSpeed)
				PointEntity n\Collider, Collider
				RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 30.0), 0
				n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
				MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
				If EntityDistanceSquared(n\Collider,Collider)<PowTwo(2.1)
					n\State[0] = SCP682_ATTACK
				EndIf
				;[End Block]
			Case SCP682_ATTACK
				;[Block]
				prevFrame = n\Frame
				
				PointEntity n\Collider, Collider
				If (Not GodMode) Then
					AnimateNPC(n, 1, 75, 0.7, False)
					If n\Frame >= 36 And prevFrame < 36 Then
						If EntityDistanceSquared(n\Collider,Collider)<PowTwo(2.1)
							If AttackSound% = 0 Then AttackSound% = LoadSound_Strict("SFX\General\Slash"+(Rand(1,2))+".ogg")
							PlaySound_Strict AttackSound%
							If IsSPPlayerAlive() Then
								PlaySound2(NTF_PainSFX[Rand(0,7)],Camera,Collider)
								DamageSPPlayer(Rnd(60.0,70.0))
							Else
								m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp682_death",Designation)
							EndIf
						EndIf
					EndIf
					If n\Frame = 75 Then
						n\State[0] = SCP682_IDLE
					EndIf
				EndIf
				;[End Block]
		End Select
		
	Else
		If n\SoundChn <> 0
			StopChannel n\SoundChn
			n\SoundChn = 0
			FreeSound_Strict n\Sound
			n\Sound = 0
		EndIf
		AnimateNPC(n, 330, 375, 0.5, False)
	EndIf
	
	If n\HP <= 0 Then
		n\IsDead = True
		If n\Frame > 375 Then
			SetNPCFrame(n, 330)
		EndIf
	EndIf
	
	;PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.65, EntityZ(n\Collider))
	
	PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.35 , EntityZ(n\Collider))
	RotateEntity n\obj, 0, EntityYaw(n\Collider), 0
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D