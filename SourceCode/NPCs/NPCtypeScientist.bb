
Function CreateNPCtypeScientist(n.NPCs)
	Local temp#,tex%,random%
	
	n\NPCName = "Class-D"
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.175, 0.2
	EntityType n\Collider, HIT_PLAYER
	n\obj = CopyEntity(NPCModel[Model_Class_D_Armed])
	
	n\Speed = 3.5 / 100
	
	temp# = 0.5 / MeshWidth(n\obj)
	ScaleEntity n\obj, temp, temp, temp
	MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	
	Select Rand(1)
		Case 0
			n\texture = "GFX\npcs\scientist.jpg"
		Case 1
			n\texture = "GFX\npcs\scientist2.jpg"
	End Select
	
	tex = LoadTexture_Strict(n\texture, 0, 2)
	TextureBlend(tex,5)
	EntityTexture(n\obj, tex)
	
	random% = Rand(1, 100)
	
	If random > 0 And random < 20 Then
		SwitchNPCGun%(n, GUN_BERETTA)
	ElseIf random > 20 And random < 50 Then
		SwitchNPCGun%(n, GUN_USP)
	Else
		SwitchNPCGun%(n, GUN_FIVESEVEN)
	EndIf
	n\Gun\Ammo = n\Gun\MaxAmmo
	n\Gun\ReloadAmmo = 3
	
	n\HP = 80
	
	CopyHitBoxes(n)
	
End Function

Function UpdateNPCtypeScientist(n.NPCs)
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
						
						AnimateNPC(n, 236, 260, n\CurrSpeed*30)
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
						
						AnimateNPC(n, 236, 260, n\CurrSpeed*30)
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
						AnimateNPC(n, 683, 732, n\CurrSpeed*60,False)
						If n\Frame >= 731 And n\Frame < 731.5 Then
							If n\Gun\MaxReloadSounds = 1 Then
								n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\reload_empty.ogg"), Camera, n\Collider, 25)
							Else
								n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\reload_empty.ogg"), Camera, n\Collider, 25)
							EndIf
						EndIf
						If n\Frame >=731.5 Then 
							n\Gun\Ammo = n\Gun\MaxAmmo
						EndIf
					Else
						AnimateNPC(n, 664, 682, n\CurrSpeed*60)
					EndIf
				Else
					AnimateNPC(n, 236, 260, n\CurrSpeed*30)
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
		If n\Frame < 21 Then
			AnimateNPC(n, 1, 20, 0.5, False)
			dieFrame = 19.5
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
				bone% = FindChild(n\obj,"Bip01_Spine")
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

Function UpdateNPCTypeHuman(n.NPCs)
	Local prevFrame, sfxstep
	
	RotateEntity(n\Collider, 0, EntityYaw(n\Collider), EntityRoll(n\Collider), True)
	
	prevFrame = AnimTime(n\obj)
	
	Select n\State[0]
		Case 0 ;idle
			n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
			Animate2(n\obj, AnimTime(n\obj), 210, 235, 0.1)
		Case 1 ;walking
			If n\State[1] = 1.0
				n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
			Else
				n\CurrSpeed = CurveValue(0.015, n\CurrSpeed, 5.0)
			EndIf
			Animate2(n\obj, AnimTime(n\obj), 236, 260, n\CurrSpeed * 18)
		Case 2 ;running
			n\CurrSpeed = CurveValue(0.03, n\CurrSpeed, 5.0)
			Animate2(n\obj, AnimTime(n\obj), 301, 319, n\CurrSpeed * 18)
		Case 3 ;Looking at object
			Animate2(n\obj, AnimTime(n\obj), 210, 235, 0.1)
			n\BoneToManipulate = "Bip01_Head"
			n\ManipulateBone = True
			n\ManipulationType = 1
			n\Angle = EntityYaw(n\Collider)
		Case STATE_SCRIPT
			
	End Select
	
	If n\State[1] <> 2
		If n\State[0] = 1
			If n\CurrSpeed > 0.01 Then
				If prevFrame < 244 And AnimTime(n\obj)=>244 Then
					sfxstep = GetStepSound(n\Collider,n\CollRadius)
					PlaySound2(StepSFX(sfxstep,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
				ElseIf prevFrame < 256 And AnimTime(n\obj)=>256
					sfxstep = GetStepSound(n\Collider,n\CollRadius)
					PlaySound2(StepSFX(sfxstep,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
				EndIf
			EndIf
		ElseIf n\State[0] = 2
			If n\CurrSpeed > 0.01 Then
				If prevFrame < 309 And AnimTime(n\obj)=>309
					sfxstep = GetStepSound(n\Collider,n\CollRadius)
					PlaySound2(StepSFX(sfxstep,1,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
				ElseIf prevFrame =< 319 And AnimTime(n\obj)=<301
					sfxstep = GetStepSound(n\Collider,n\CollRadius)
					PlaySound2(StepSFX(sfxstep,1,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
				EndIf
			EndIf
		EndIf
	EndIf
	
	If n\Frame = 19 Lor n\Frame = 60
		n\IsDead = True
	EndIf
	If AnimTime(n\obj)=19 Lor AnimTime(n\obj)=60
		n\IsDead = True
	EndIf
	
	MoveEntity(n\Collider, 0, 0, n\CurrSpeed * FPSfactor)
	
	PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.32, EntityZ(n\Collider))
	
	RotateEntity n\obj, EntityPitch(n\Collider), EntityYaw(n\Collider)-180.0, 0
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS