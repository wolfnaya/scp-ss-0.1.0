
; ~ [Multiplayer Zombie constants]

; ~ States
;[Block]
Const MPZ_STATE_FREEZE = -1
Const MPZ_STATE_WANDER = 0
Const MPZ_STATE_DETECTED = 1
Const MPZ_STATE_ATTACK = 2
;[End Block]
; ~ Armed Zombie Constants
;[Block]
Const Guard_Zombie = 0
Const NTF_Zombie = 1
Const CI_Zombie = 2
;[End Block]
; ~ Regular Zombie Constants
;[Block]
Const Topless_Zombie = 0
Const Hazmat_Zombie = 1
Const ClassD_Zombie = 2
Const Clerk_Zombie = 3
Const Surgeon_Zombie = 4
Const Worker_Zombie = 5
;[End Block]

; ~ Guard Zombies

Function CreateNPCtypeArmedZombieMP(n.NPCs, Model% = -1)
	Local temp#, tex, tex2
	Local g.Guns, gunamount = 0
	
	n\NPCName = "Zombie"
	n\Collider = CreatePivot()
	n\CollRadius = 0.15
	EntityRadius n\Collider, n\CollRadius, 0.2
	EntityType n\Collider, HIT_NPC_MP
	
	tex = LoadTexture_Strict("GFX\npcs\Zombies\NineTailedFox_Zombie.png", 0, 2)
	tex2 = LoadTexture_Strict("GFX\npcs\Zombies\NineTailedFox_Zombie_Medic.png", 0, 2)
	
	If n\obj = 0 Then
		If Model = -1 Then
			Local random% = Rand(1,160)
			If random < 70 Then
				n\State[2] = Guard_Zombie
			ElseIf random > 70 And random < 120 Then
				n\State[2] = NTF_Zombie
			Else
				n\State[2] = CI_Zombie
			EndIf
		Else
			n\State[2] = Model
		EndIf
		n\obj = CopyEntity(mp_I\GuardZombieModel[n\State[2]])
		
		temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-049-2", "scale") / 2.5)
		ScaleEntity n\obj, temp, temp, temp
		
		MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	EndIf
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-049-2", "speed") / 50.0)
	
	If n\State[2] = NTF_Zombie Then
		
		TextureBlend(tex,5)
		If Rand(2) = 1 Then
			EntityTexture(n\obj, tex)
		Else
			EntityTexture(n\obj, tex2)
		EndIf
		
		If n\Sound = 0 Then
			n\Sound = LoadSound_Strict("SFX\SCP\049\0492MTFBreath.ogg")
		EndIf
		
		n\NVName = "a NTF zombie"
		
		n\HP = 220
	ElseIf n\State[2] = CI_Zombie Then
		
		If n\Sound = 0 Then
			n\Sound = LoadSound_Strict("SFX\SCP\049\0492Angry.ogg")
		EndIf
		
		n\NVName = "a CI zombie"
		
		n\HP = 200
	Else
		
		If n\Sound = 0 Then
			n\Sound = LoadSound_Strict("SFX\SCP\049\0492Breath.ogg")
		EndIf
		
		n\NVName = "a guard zombie"
		
		n\HP = 150
	EndIf
	
	random% = Rand(1, 150)
	
	If random <= 25 Then
		n\State[5] = 0
	Else
		n\State[5] = 1
	EndIf
	
	For g = Each Guns
		gunamount = gunamount + 1
	Next
	random = Rand(1, gunamount)
	For g = Each Guns
		If g\ID = random Then
			If g\name <> "knife" And g\name <> "emrp" And g\name <> "crowbar" And g\name <> "scp127" And g\name <> "m67" And g\name <> "rgd5" And g\name <> "spas12" And g\name <> "m870" And g\name <> "tt33w" Then
				If n\State[5] = 1 Then
					SwitchNPCGun%(n, g\ID)
					n\Gun\Ammo = n\Gun\MaxAmmo
					n\Gun\ReloadAmmo = 3
				EndIf
			EndIf
		EndIf
	Next
	
	SetAnimTime(n\obj, 107)
	
	n\PathTimer = 70*5
	
	CopyHitBoxes(n)
	
End Function

; ~ Regular Zombies

Function CreateNPCtypeZombieMP(n.NPCs,model%=-1)
	Local temp#
	
	n\NPCName = "Zombie"
	n\Collider = CreatePivot()
	n\CollRadius = 0.15
	EntityRadius n\Collider, n\CollRadius, 0.2
	EntityType n\Collider, HIT_NPC_MP
	
	If n\obj = 0 Then
		If model = -1 Then
			Local random% = Rand(1,100)
			If random < 20 Then
				n\State[2] = Topless_Zombie ; ~ Topless zombie
			ElseIf random >= 20 And random < 25 Then
				n\State[2] = Hazmat_Zombie ; ~ Hazmatsuit zombie
			ElseIf random >= 25 And random < 55 Then
				n\State[2] = ClassD_Zombie ; ~ D-Class zombie
			ElseIf random >= 55 And random < 75 Then
				n\State[2] = Clerk_Zombie ; ~ Clerk zombie
			ElseIf random >= 75 And random < 95 Then
				n\State[2] = Surgeon_Zombie ; ~ Surgeon zombie
			Else
				n\State[2] = Worker_Zombie ; ~ Worker zombie
			EndIf
		Else
			n\State[2] = model
		EndIf
		n\obj = CopyEntity(mp_I\ZombieModel[n\State[2]])
		
		temp# = 0.0135
		ScaleEntity n\obj, temp, temp, temp
		
		MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
		
		If n\State[2] = ClassD_Zombie Then
			Select Rand(0,6)
				Case 0
					n\texture = "GFX\npcs\Zombies\ZombieDINF.jpg"
				Case 1
					n\texture = "GFX\npcs\Zombies\dclass_zombie2.jpg"
				Case 2
					n\texture = "GFX\npcs\Zombies\janitor_zombie.jpg"
				Case 3
					n\texture = "GFX\npcs\Zombies\janitor_zombie2.png"
				Case 4
					n\texture = "GFX\npcs\Zombies\scientist_zombie_1.jpg"
				Case 5
					n\texture = "GFX\npcs\Zombies\scientist_zombie_2.png"
				Case 6
					n\texture = "GFX\npcs\Zombies\maintenance_zombie.png"
			End Select
			
			Local tex = LoadTexture_Strict(n\texture, 0, 2)
			TextureBlend(tex,5)
			EntityTexture(n\obj, tex)
		EndIf
		
	EndIf
	
	n\NVName = "a zombie"
	n\Speed = (1.6 / 100.0)
	n\HP = 70
	n\PathTimer = 70*5
	
	CopyHitBoxes(n)
	
End Function

Function UpdateNPCtypeZombieMP(n.NPCs)
	Local fb.FuseBox, cmsg.ChatMSG
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
						
						If n\NPCtype = NPC_Zombie Then
							AnimateNPC(n, 64, 93, n\CurrSpeed*30)
						Else
							AnimateNPC(n,194,254,n\CurrSpeed*60)
						EndIf
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
						
						If n\NPCtype = NPC_Zombie Then
							AnimateNPC(n,64,93,n\CurrSpeed*30)
						Else
							AnimateNPC(n,194,254,n\CurrSpeed*60)
						EndIf
						n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					If EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider)<56.25 Then
						If EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider) Then
							n\State[0] = MPZ_STATE_DETECTED
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
			Case MPZ_STATE_DETECTED ; ~ Player detected
				;[Block]
				PointEntity n\obj, Players[n\ClosestPlayer]\Collider
				If mp_I\PlayState = GAME_SERVER Then RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
				
				If n\NPCtype <> NPC_Zombie_Armed Then
					AnimateNPC(n, 64, 93, n\CurrSpeed*30)
				Else
					; ~ Reloading
					If n\Gun <> Null And n\State[5] = 1 Then
						If n\Gun\Ammo = 0 And n\Gun\ReloadAmmo > 0 Then
							n\Reload = 210
							AnimateNPC(n, 486, 607, n\CurrSpeed*60,False)
							If n\Frame >= 487 And n\Frame < 487.5 Then
								If n\Gun\MaxReloadSounds = 1 Then
									n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\reload_empty.ogg"), Camera, n\Collider, 25)
								Else
									n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\reload_empty.ogg"), Camera, n\Collider, 25)
								EndIf
							EndIf
							If n\Frame >=606.5 Then 
								n\Gun\Ammo = n\Gun\MaxAmmo
								n\Gun\ReloadAmmo = n\Gun\ReloadAmmo - 1
							EndIf
						Else
							AnimateNPC(n, 425, 485, n\CurrSpeed*60)
						EndIf
					Else
						AnimateNPC(n,194,254,n\CurrSpeed*60)
					EndIf
				EndIf
				n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
				MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
				; ~ Gun has no ammo -> Throwing away
				If n\Gun <> Null Then
					If n\Gun\ReloadAmmo = 0 And n\Gun\Ammo = 0 Then
						n\State[5] = 2
					EndIf
					; ~ Throwing gun away
					If n\State[5] = 2 Then
						For g = Each Guns
							If g\ID = n\Gun\ID Then
								bone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "weapon_hand_bonename"))
								it = CreateItem(g\DisplayName, g\name, EntityX(bone%, True) + 0.025, EntityY(bone%, True), EntityZ(bone%, True))
								EntityType it\collider, HIT_ITEM
								it\state = n\Gun\Ammo
								it\state2 = n\Gun\ReloadAmmo*g\MaxCurrAmmo
								it\Dropped = 1
								Exit
							EndIf
						Next
						RemoveNPCGun(n)
					EndIf
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
										
										If Rand(0,4) = 0 Then
											Local p.Particles = CreateParticle(EntityX(flashpvt),EntityY(flashpvt),EntityZ(flashpvt), 1, Rnd(0.08,0.1), 0.0, 5)
											TurnEntity p\obj, 0,0,Rnd(360)
											DamagePlayer(mp_I\PlayerID, n\Gun\Damage / Rand(1, 1.25), n\Gun\Damage / Rand(2, 2.25)/2)
											If Players[n\ClosestPlayer]\CurrKevlar>0 Then
												PlaySound_Strict(NTF_PainSFX[Rand(0,7)])
											Else
												PlaySound_Strict(NTF_PainWeakSFX[Rand(0,1)])
											EndIf
											mpl\DamageTimer = 70.0*1.0
										EndIf
										If n\Gun\MaxGunshotSounds = 1 Then
											n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"), Camera, n\Collider, 25)
										Else
											n\SoundChn2 = PlaySound2(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"), Camera, n\Collider, 25)
										EndIf
										If n\Gun\GunType = GUNTYPE_HANDGUN Then
											n\Reload = Rand(25,50)
										Else
											n\Reload = Rand(10,15)
										EndIf
									EndIf
								EndIf
							EndIf
						EndIf
					EndIf
				EndIf
				
				dist = EntityDistanceSquared(n\obj,Players[n\ClosestPlayer]\Collider)
				If dist<=0.6 Then
					If (Abs(DeltaYaw(n\Collider,Players[n\ClosestPlayer]\Collider))<=60.0) Then
						n\State[0] = MPZ_STATE_ATTACK
						If n\NPCtype = NPC_Zombie_Armed Then
							If n\Gun <> Null Then
								If Rand(4)=1 Then
									SetNPCFrame(n,255)
								Else
									SetNPCFrame(n,306)
								EndIf
							Else
								If Rand(2)=1 Then
									SetNPCFrame(n,255)
								Else
									SetNPCFrame(n,306)
								EndIf
							EndIf
						EndIf
					EndIf
				EndIf
				
				If n\DistanceTimer <= 0.0 Then
					If dist>56.25 Lor (Not EntityVisible(n\obj,Players[n\ClosestPlayer]\Collider)) Then
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
			If n\NPCtype = NPC_Zombie Then
				Select Rand(3)
					Case 1
						SetNPCFrame(n,204)
					Case 2
						SetNPCFrame(n,590)
					Case 3
						SetNPCFrame(n,642)
				End Select
			Else
				Select Rand(2)
					Case 1
						SetNPCFrame(n,357)
					Case 2
						SetNPCFrame(n,394)
				End Select		
			EndIf
			MoveEntity n\Collider,0,0.01,0
		EndIf
		If n\NPCtype = NPC_Zombie Then
			;woops, not MTF cutie
		Else
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
		If n\NPCtype = NPC_Zombie Then
			If n\Frame < 642 Then
				If n\Frame < 590 Then
					AnimateNPC(n, 204, 349, 0.5, False)
					dieFrame = 348.5
				Else
					AnimateNPC(n, 590, 639, 0.5, False)
					dieFrame = 638.5
				EndIf	
			Else
				AnimateNPC(n, 642, 699, 0.5, False)
				dieFrame = 698.5
			EndIf
		Else
			If n\Frame < 394 Then
				AnimateNPC(n, 357, 393, 0.5, False)
				dieFrame = 392.5
			Else
				AnimateNPC(n, 394, 424, 0.5, False)
				dieFrame = 423.5
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
			If n\NPCtype = NPC_Zombie Then
				If n\State[2] = Surgeon_Zombie Lor n\State[2] = Worker_Zombie Then
					If prevFrame < 348.5 And n\Frame => 348.5 Lor prevFrame < 638.5 And n\Frame => 638.5 Lor prevFrame < 698.5 And n\Frame => 698.5 Then
						bone% = FindChild(n\obj,"Bip01_Spine1")
						If n\State[2] = Surgeon_Zombie Then
							Random = Rand(0,2)
							Select Random
								Case 0
									it = CreateItem(GetLocalString("Item Names","first_aid"),"firstaid",EntityX(bone%,True),EntityY(bone%,True)+0.025,EntityZ(bone%,True))
								Case 1
									it = CreateItem(GetLocalString("Item Names","first_aid_blue"),"firstaid2",EntityX(bone%,True),EntityY(bone%,True)+0.025,EntityZ(bone%,True))
								Case 2
									it = CreateItem(GetLocalString("Item Names","syringe"),"syringe",EntityX(bone%,True),EntityY(bone%,True)+0.025,EntityZ(bone%,True))
							End Select
							EntityType it\collider, HIT_ITEM
							it\Dropped = 1
						Else
							Local FusesAmount% = 0
							Local FusesActivatedAmount% = 0
							For fb = Each FuseBox
								FusesAmount% = FusesAmount + MaxFuseAmount
								FusesActivatedAmount = FusesActivatedAmount + fb\fuses
							Next
							If FusesActivatedAmount < FusesAmount Then
								it = CreateItem(GetLocalString("Item Names","fuse"),"fuse",EntityX(bone%,True),EntityY(bone%,True)+0.025,EntityZ(bone%,True))
								EntityType it\collider, HIT_ITEM
								it\Dropped = 1
							EndIf
						EndIf
					EndIf
				EndIf	
			Else
				If prevFrame < 392.5 And n\Frame => 392.5 Lor prevFrame < 423.5 And n\Frame => 423.5 Then
					bone% = FindChild(n\obj,"chest")
					If mp_O\HardcoreMP Then
						Random = Rand(0,160)
						If Random < 80 Then
							it = CreateItem(GetLocalString("Item Names","vest"),"vest",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
						ElseIf Random > 80 And Random < 140 Then
							it = CreateItem(GetLocalString("Item Names","ammo"),"ammocrate",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
						Else
							For fb = Each FuseBox
								FusesAmount% = FusesAmount + MaxFuseAmount
								FusesActivatedAmount = FusesActivatedAmount + fb\fuses
							Next
							If FusesActivatedAmount < FusesAmount Then
								it = CreateItem(GetLocalString("Item Names","fuse"),"fuse",EntityX(bone%,True),EntityY(bone%,True)+0.025,EntityZ(bone%,True))
							EndIf
						EndIf
						If it <> Null Then
							EntityType it\collider, HIT_ITEM
							it\Dropped = 1
						EndIf
					Else
						Random = Rand(0,8)
						Select Random
							Case 0
								it = CreateItem(GetLocalString("Item Names","vest"),"vest",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
							Case 1	
								it = CreateItem(GetLocalString("Item Names","ammo"),"ammocrate",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
							Case 2
								it = CreateItem(GetLocalString("Item Names","vest_heavy"),"heavyvest",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
							Case 3	
								it = CreateItem(GetLocalString("Item Names","ammo_big"),"bigammocrate",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
							Case 4	
								it = CreateItem(GetLocalString("Item Names","suppressor"),"suppressor",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
							Case 5	
								it = CreateItem(GetLocalString("Item Names","match"),"match",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
							Case 6	
								it = CreateItem(GetLocalString("Item Names","acog"),"acog",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
							Case 7	
								it = CreateItem(GetLocalString("Item Names","eotech"),"eotech",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
							Case 8	
								it = CreateItem(GetLocalString("Item Names","red_dot"),"reddot",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
						End Select
						If it <> Null Then
							EntityType it\collider, HIT_ITEM
							it\Dropped = 1
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
	If n\NPCtype = NPC_Zombie Then
		RotateEntity n\obj,0,EntityYaw(n\Collider)-180,0
	Else
		RotateEntity n\obj,-90,EntityYaw(n\Collider),0
	EndIf
	PositionEntity n\obj,EntityX(n\Collider),EntityY(n\Collider)-0.2,EntityZ(n\Collider)
	
	EntityAutoFade(n\obj,GetCameraFogRangeFar(Camera)-0.5,GetCameraFogRangeFar(Camera)+0.5)
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D TSS