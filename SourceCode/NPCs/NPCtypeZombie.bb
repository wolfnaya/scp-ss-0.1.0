
; ~ Zombie constants

;[Block]
Const Z_STATE_LYING = 0
Const Z_STATE_STANDUP = 1
Const Z_STATE_WANDER = 2
Const Z_STATE_DETECTED = 3
Const Z_STATE_ATTACK = 4
;[End Block]

; ~ Guard zombies

Function CreateNPCtypeArmedZombie(n.NPCs,model%=-1)
	Local temp#, tex, tex2
	
	n\NPCName = "Zombie"
	n\NVName = "Human"
	n\Collider = CreatePivot()
	n\CollRadius = 0.15
	EntityRadius n\Collider, n\CollRadius, 0.2
	EntityType n\Collider, HIT_PLAYER
	
	tex = LoadTexture_Strict("GFX\npcs\Zombies\NineTailedFox_Zombie.png", 0, 2)
	tex2 = LoadTexture_Strict("GFX\npcs\Zombies\NineTailedFox_Zombie_Medic.png", 0, 2)
	
	If n\obj = 0 Then
		If model = -1 Then
			Local random% = Rand(1,160)
			If random < 70 Then
				n\State[2] = Guard_Zombie
			ElseIf random > 70 And random < 120 Then
				n\State[2] = NTF_Zombie
			Else
				n\State[2] = CI_Zombie
			EndIf
		Else
			n\State[2] = model
		EndIf
		n\obj = CopyEntity(GuardZombieModel[n\State[2]])
		
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
		
		random% = Rand(1, 150)
		
		If random <= 15 Then
			n\State[5] = 0
		Else
			n\State[5] = 1
		EndIf
		
		If n\State[5] = 1 Then
			If random <= 42 Then
				SwitchNPCGun%(n, GUN_BERETTA)
			ElseIf random > 42 And random <= 84 Then	
				SwitchNPCGun%(n, GUN_P90)
			ElseIf random > 84 And random <= 90 Then
				SwitchNPCGun%(n, GUN_FIVESEVEN)
			ElseIf random > 90 Then
				SwitchNPCGun%(n, GUN_MP5)
			EndIf
			
			n\Gun\Ammo = n\Gun\MaxAmmo
			n\Gun\ReloadAmmo = 3
		EndIf
		
		If n\Sound = 0 Then
			n\Sound = LoadSound_Strict("SFX\SCP\049\0492MTFBreath.ogg")
		EndIf
		
		n\HP = 250
	ElseIf n\State[2] = CI_Zombie Then
		
		random% = Rand(1, 150)
		
		If random <= 15 Then
			n\State[5] = 0
		Else
			n\State[5] = 1
		EndIf
		
		If n\State[5] = 1 Then
			If random <= 42 Then
				SwitchNPCGun%(n, GUN_USP)
			ElseIf random > 42 And random <= 84 Then	
				SwitchNPCGun%(n, GUN_MP7)
			ElseIf random > 84 And random <= 90 Then
				SwitchNPCGun%(n, GUN_GLOCK)
			ElseIf random > 90 Then
				SwitchNPCGun%(n, GUN_MP5)
			EndIf
			
			n\Gun\Ammo = n\Gun\MaxAmmo
			n\Gun\ReloadAmmo = 3
		EndIf
		
		If n\Sound = 0 Then
			n\Sound = LoadSound_Strict("SFX\SCP\049\0492Angry.ogg")
		EndIf
		
		n\HP = 210
	Else
		
		random% = Rand(1, 150)
		
		If random <= 15 Then
			n\State[5] = 0
		Else
			n\State[5] = 1
		EndIf
		
		If n\State[5] = 1 Then
			If random <= 42 Then
				SwitchNPCGun%(n, GUN_USP)
			ElseIf random > 42 And random <= 84 Then	
				SwitchNPCGun%(n, GUN_BERETTA)
			ElseIf random > 84 And random <= 90 Then
				SwitchNPCGun%(n, GUN_FIVESEVEN)
			ElseIf random > 90 Then
				SwitchNPCGun%(n, GUN_P90)
			EndIf
			
			n\Gun\Ammo = n\Gun\MaxAmmo
			n\Gun\ReloadAmmo = 2
		EndIf
		
		If n\Sound = 0 Then
			n\Sound = LoadSound_Strict("SFX\SCP\049\0492Breath.ogg")
		EndIf
		
		n\HP = 150
	EndIf
	
	SetAnimTime(n\obj, 107)
	
	n\PathTimer = 70*5
	
	CopyHitBoxes(n)
	
End Function

; ~ Regular zombies

Function CreateNPCtypeZombie(n.NPCs,model%=-1)
	Local temp#
	
	n\NPCName = "Zombie"
	n\Collider = CreatePivot()
	n\CollRadius = 0.15
	EntityRadius n\Collider, n\CollRadius, 0.2
	EntityType n\Collider, HIT_PLAYER
	
	If n\obj = 0 Then
		If n\NPCtype <> NPC_Elias Then
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
			n\obj = CopyEntity(ZombieModel[n\State[2]])
		Else
			n\obj = LoadAnimMesh_Strict("GFX\NPCs\Zombies\Elias.b3d")
			n\State[2] = -2
		EndIf
		
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
	
	If n\Sound = 0 Then
		n\Sound = LoadSound_Strict("SFX\SCP\049\0492Breath.ogg")
	EndIf
	
	n\NVName = "Human"
	n\Speed = (1.6 / 100.0)
	n\HP = 120
	n\PathTimer = 70*5
	
	CopyHitBoxes(n)
	
End Function

Function UpdateNPCtypeZombie(n.NPCs)
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
			Case Z_STATE_LYING
				;[Block]
				
				If n\NPCtype <> NPC_Zombie_Armed Then
					AnimateNPC(n, 10, 11, 0.2, False)
				Else
					AnimateNPC(n, 1, 3, 0.2, False)
				EndIf
				
				If n\NPCtype <> NPC_Zombie_Armed Then
					If n\Frame=10 Then
						If Rand(700)=1 Then
							If EntityDistanceSquared(Collider, n\Collider)<PowTwo(5.0) Then
								n\Frame = 11
							EndIf
						EndIf
					EndIf
				Else
					If n\Frame=3 Then
						If Rand(700)=1 Then
							If EntityDistanceSquared(Collider, n\Collider)<PowTwo(5.0) Then
								n\Frame = 4
							EndIf
						EndIf
					EndIf
				EndIf
				;[End Block]
			Case Z_STATE_STANDUP ;stands up
				;[Block]
				If n\NPCtype <> NPC_Zombie_Armed Then
					AnimateNPC(n, 11, 32, 0.3, False)
					If n\Frame = 32 Then n\State[0] = Z_STATE_WANDER
				Else
					If n\Frame=>4 Then
						AnimateNPC(n, 4, 69, 0.3, False)
						If n\Frame = 69 Then n\State[0] = Z_STATE_WANDER
					Else
						AnimateNPC(n, 4, 69, 1.5, False)
					EndIf
				EndIf
				;[End Block]
			Case Z_STATE_WANDER ; ~ Wandering around
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
						RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
						
						If n\NPCtype = NPC_Zombie Then
							AnimateNPC(n, 64, 93, n\CurrSpeed*30)
						Else
							AnimateNPC(n,194,254,n\CurrSpeed*60)
						EndIf
						n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
						
						dist = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
						;If dist < 0.09 Then
						;	n\PathLocation = n\PathLocation + 1
						;EndIf
						
						; ~ Opens doors in front of him
						
						If dist<PowTwo(0.6) Then
							Local temp = True
							If n\Path[n\PathLocation]\door <> Null Then
								If (Not n\Path[n\PathLocation]\door\IsElevatorDoor)
									If n\Path[n\PathLocation]\door\locked Lor n\Path[n\PathLocation]\door\KeyCard>-1 Lor n\Path[n\PathLocation]\door\Code<>"" Then
										temp = False
									Else
										If n\Path[n\PathLocation]\door\open = False Then UseDoor(n\Path[n\PathLocation]\door, False)
									EndIf
								EndIf
							EndIf
							If dist<PowTwo(0.2) And temp
								n\PathLocation = n\PathLocation + 1
							ElseIf dist<PowTwo(0.5) And (Not temp)
								n\PathStatus = 0
								n\PathTimer# = 0.0
							EndIf
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
						RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
						
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
					If EntityDistanceSquared(n\obj,Collider)<56.25 Then
						If EntityVisible(n\obj,Collider) Then
							n\State[0] = Z_STATE_DETECTED
						EndIf
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSfactor
				EndIf
				
				;[End Block]
			Case Z_STATE_DETECTED ; ~ Player detected
				;[Block]
				PointEntity n\obj, Collider
				RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
				
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
									If EntityVisible(Collider, n\Collider) And IsSPPlayerAlive() Then
										n\Gun\Ammo = n\Gun\Ammo - 1
										Local flashpvt% = CreatePivot()
										
										RotateEntity(flashpvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
										PositionEntity(flashpvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
										MoveEntity (flashpvt,2*0.079, 8.8*0.079, 6.0*0.079)
										If I_1033RU\HP = 0
											ShootPlayer(EntityX(flashpvt), EntityY(flashpvt), EntityZ(flashpvt), Clamp(2 / EntityDistanceSquared(n\Collider, Collider), 0.0, 0.65), True, n\Gun\Damage * Rand(1, 1.25))
										Else
											Damage1033RU(n\Gun\Damage/2 + (5 * SelectedDifficulty\AggressiveNPCs))
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
				
				dist = EntityDistanceSquared(n\obj,Collider)
				If dist<=0.6 Then
					If (Abs(DeltaYaw(n\Collider,Collider))<=60.0) Then
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
					If dist>56.25 Lor (Not EntityVisible(n\obj,Collider)) Then
						n\State[0] = Z_STATE_WANDER
						n\PathTimer = 0.0
						n\PathStatus = 0
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSfactor
				EndIf
				;[End Block]
			Case Z_STATE_WANDER_2 ;following the player
				;[Block]
				If n\State[2] < 0 Then ;check if the player is visible every three seconds
					If EntityDistanceSquared(Collider, n\Collider) < PowTwo(5.0) Then 
						If EntityVisible(Collider, n\Collider) Then n\State[1] = 70*5
					EndIf
					n\State[2]=70*3
				Else
					n\State[2]=n\State[2]-FPSfactor
				EndIf
				
				If n\State[1] > 0 And (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then ;player is visible -> attack
					n\SoundChn = LoopSound2(n\Sound, n\SoundChn, Camera, n\Collider, 6.0, 0.6)
					
					n\PathStatus = 0
					
					PointEntity n\obj, Collider
					RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 30.0), 0
					
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
							If n\Gun\Ammo > 0 Then
								If n\Reload = 0 Then
									If EntityVisible(Collider, n\Collider) And IsSPPlayerAlive() Then
										n\Gun\Ammo = n\Gun\Ammo - 1
										flashpvt% = CreatePivot()
										
										RotateEntity(flashpvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
										PositionEntity(flashpvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
										MoveEntity (flashpvt,2*0.079, 8.8*0.079, 6.0*0.079)
										If I_1033RU\HP = 0
											ShootPlayer(EntityX(flashpvt), EntityY(flashpvt), EntityZ(flashpvt), Clamp(2 / EntityDistanceSquared(n\Collider, Collider), 0.0, 0.65), True, n\Gun\Damage * Rand(1, 1.25))
										Else
											Damage1033RU(n\Gun\Damage/2 + (5 * SelectedDifficulty\AggressiveNPCs))
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
					
					dist = EntityDistanceSquared(n\obj,Collider)
					
					If dist<=0.6 Then
						If (Abs(DeltaYaw(n\Collider,Collider))<=60.0) And (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then
							n\State[0] = Z_STATE_ATTACK
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
					Else
						n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
						MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
						
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
							If n\NPCtype <> NPC_Zombie_Armed Then
								AnimateNPC(n, 64, 93, n\CurrSpeed*30)
							Else
								AnimateNPC(n,194,254,n\CurrSpeed*60)
							EndIf
						EndIf
					EndIf
					
					n\State[1]=n\State[1]-FPSfactor
				Else
					If n\PathStatus = 1 Then ;path found
						If n\Path[n\PathLocation]=Null Then 
							If n\PathLocation > 19 Then 
								n\PathLocation = 0 : n\PathStatus = 0
							Else
								n\PathLocation = n\PathLocation + 1
							EndIf
						Else
							PointEntity n\obj, n\Path[n\PathLocation]\obj
							
							RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 30.0), 0
							n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
							
							If n\NPCtype <> NPC_Zombie_Armed Then
								AnimateNPC(n, 64, 93, n\CurrSpeed*30)
							Else
								AnimateNPC(n,194,254,n\CurrSpeed*60)
							EndIf
							
							; ~ Opens doors in front of him
							
							Local dist2# = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
							If dist2<PowTwo(0.6) Then
								temp = True
								If n\Path[n\PathLocation]\door <> Null Then
									If (Not n\Path[n\PathLocation]\door\IsElevatorDoor)
										If n\Path[n\PathLocation]\door\locked Lor n\Path[n\PathLocation]\door\KeyCard>-1 Lor n\Path[n\PathLocation]\door\Code<>"" Then
											temp = False
										Else
											If n\Path[n\PathLocation]\door\open = False Then UseDoor(n\Path[n\PathLocation]\door, False)
										EndIf
									EndIf
								EndIf
								If dist2<PowTwo(0.2) And temp
									n\PathLocation = n\PathLocation + 1
								ElseIf dist2<PowTwo(0.5) And (Not temp)
									n\PathStatus = 0
									n\PathTimer# = 0.0
								EndIf
							EndIf
						EndIf
					Else ;no path to the player, stands still
						n\CurrSpeed = 0
						If n\NPCtype <> NPC_Zombie_Armed Then
							AnimateNPC(n,64,93,n\CurrSpeed*30)
						Else
							AnimateNPC(n,194,254,n\CurrSpeed*60)
						EndIf
						n\PathTimer = n\PathTimer-FPSfactor
						If n\PathTimer =< 0 Then
							n\PathStatus = FindPath(n, EntityX(Collider),EntityY(Collider)+0.1,EntityZ(Collider))
							n\PathTimer = n\PathTimer+70*5
						EndIf
					EndIf
				EndIf
				
				; ~ Step sounds
				If n\CurrSpeed > 0.005 Then
					If n\Gun <> Null Then
						If (prevFrame < 461 And n\Frame=>461) Lor (prevFrame > 485 And n\Frame<470) Then
							PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
						EndIf
					Else
						If n\NPCtype <> NPC_Zombie_Armed Then
							If (prevFrame < 80 And n\Frame=>80) Lor (prevFrame > 92 And n\Frame<65) Then
								PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
							EndIf
						Else
							If (prevFrame < 231 And n\Frame=>231) Lor (prevFrame > 255 And n\Frame<240) Then
								PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
							EndIf
						EndIf
					EndIf
				EndIf
				;[End Block]
			Case Z_STATE_DETECTED_2 ; ~ Player detected
				;[Block]
				If (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then
					PointEntity n\obj, Collider
					RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
				Else
					n\State[0] = Z_STATE_WANDER
					n\PathStatus = 1
				EndIf
				
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
				EndIf
				; ~ Throwing gun away
				If n\Gun <> Null Then
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
						If n\Gun\Ammo > 0 Then
							If n\Reload = 0 Then
								If EntityVisible(Collider, n\Collider) And IsSPPlayerAlive() Then
									n\Gun\Ammo = n\Gun\Ammo - 1
									flashpvt% = CreatePivot()
									
									RotateEntity(flashpvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
									PositionEntity(flashpvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
									MoveEntity (flashpvt,2*0.079, 8.8*0.079, 6.0*0.079)
									If I_1033RU\HP = 0
										ShootPlayer(EntityX(flashpvt), EntityY(flashpvt), EntityZ(flashpvt), Clamp(2 / EntityDistanceSquared(n\Collider, Collider), 0.0, 0.65), True, n\Gun\Damage * Rand(1, 1.25))
									Else
										Damage1033RU(n\Gun\Damage/2 + (5 * SelectedDifficulty\AggressiveNPCs))
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
				
				dist = EntityDistanceSquared(n\obj,Collider)
				If dist<=0.6 Then
					If (Abs(DeltaYaw(n\Collider,Collider))<=60.0) And (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then
						n\State[0] = Z_STATE_ATTACK
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
					If dist>56.25 Lor (Not EntityVisible(n\obj,Collider)) Lor (I_268\Using > 0 Lor I_268\Timer > 0.0) Then
						n\State[0] = Z_STATE_WANDER
						n\PathTimer = 0.0
						n\PathStatus = 0
					EndIf
					n\DistanceTimer = NPCDistanceCheckTime
				Else
					n\DistanceTimer = n\DistanceTimer - FPSfactor
				EndIf
				;[End Block]
			Case Z_STATE_ATTACK ; ~ Attacking
				;[Block]
				Local shouldFrame#
				Local restartFrame#
				Local finalFrame#
				If n\NPCtype <> NPC_Zombie_Armed Then
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
				dist = EntityDistanceSquared(n\Collider,Collider)
				yaw = Abs(DeltaYaw(n\Collider,Collider))
				If (dist<=1.2) Then
					If (yaw<=60.0) Then
						If prevFrame < shouldFrame And n\Frame => shouldFrame Then
							PlaySound2(DamageSFX[Rand(5,8)],Camera,n\Collider)
							If psp\Kevlar > 0 Lor (Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" And hds\Health > 35) Then
								PlaySound_Strict(NTF_PainSFX[Rand(0,7)])
							Else
								PlaySound_Strict(NTF_PainWeakSFX[Rand(0,1)])
							EndIf
							If psp\Health > 0 Then
								If I_1033RU\HP = 0
									DamageSPPlayer(Rnd(10.0,20.0))
								Else
									Damage1033RU(Rnd(5.0,10.0) + (5 * SelectedDifficulty\AggressiveNPCs))
								EndIf
								If psp\Health <= 0 Then
									m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp049_2_death",Designation)
								EndIf
							EndIf
						EndIf
					EndIf
				EndIf
				If n\Frame => finalFrame Then
					If Collider=0 Lor psp\Health<=0 Then
						n\State[0] = Z_STATE_WANDER
					Else
						If dist>1.2 Then
							n\State[0] = Z_STATE_DETECTED
						EndIf
						If (yaw>60.0) Then
							n\State[0] = Z_STATE_DETECTED
						EndIf
					EndIf
					If n\State[0] = Z_STATE_ATTACK Then
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
			If n\NPCtype <> NPC_Zombie_Armed Then
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
		If n\State[0] <> Z_STATE_LYING Then
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
		If n\NPCtype <> NPC_Zombie_Armed Then
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
					Select g\ID
						Case GUN_P90
							Random = Rand(0, 5)
							Select Random
								Case 0, 1, 4, 5								Case 2
									AddAttachment(g,ATT_SPECIAL_SCOPE)
									g\HasToggledAttachments[ATT_SPECIAL_SCOPE] = True
								Case 3
									AddAttachment(g,ATT_ACOG_SCOPE)
									g\HasToggledAttachments[ATT_ACOG_SCOPE] = True
							End Select
						Case GUN_MP7
							Random = Rand(0, 6)
							Select Random
								Case 0, 4, 5, 6
								Case 1
									AddAttachment(g,ATT_EOTECH)
									AddAttachment(g,ATT_SUPPRESSOR)
									g\HasToggledAttachments[ATT_EOTECH] = True
									g\HasToggledAttachments[ATT_SUPPRESSOR] = True
								Case 2
									AddAttachment(g,ATT_RED_DOT)
									g\HasToggledAttachments[ATT_RED_DOT] = True
								Case 3
									AddAttachment(g,ATT_ACOG_SCOPE)
									g\HasToggledAttachments[ATT_ACOG_SCOPE] = True
							End Select
						Case GUN_MP5
							Random = Rand(0, 5)
							Select Random
								Case 0, 4, 5
								Case 1
									AddAttachment(g,ATT_EOTECH)
									g\HasToggledAttachments[ATT_EOTECH] = True
								Case 2
									AddAttachment(g,ATT_RED_DOT)
									AddAttachment(g,ATT_SUPPRESSOR)
									g\HasToggledAttachments[ATT_RED_DOT] = True
									g\HasToggledAttachments[ATT_SUPPRESSOR] = True
								Case 3
									AddAttachment(g,ATT_ACOG_SCOPE)
									g\HasToggledAttachments[ATT_ACOG_SCOPE] = True
							End Select
						Case GUN_M4A1
							Random = Rand(0, 7)
							Select Random
								Case 0, 4, 5, 6, 7
								Case 1
									AddAttachment(g,ATT_EOTECH)
									g\HasToggledAttachments[ATT_EOTECH] = True
								Case 2
									AddAttachment(g,ATT_RED_DOT)
									g\HasToggledAttachments[ATT_RED_DOT] = True
								Case 3
									AddAttachment(g,ATT_ACOG_SCOPE)
									AddAttachment(g,ATT_SUPPRESSOR)
									g\HasToggledAttachments[ATT_ACOG_SCOPE] = True
									g\HasToggledAttachments[ATT_SUPPRESSOR] = True
							End Select
						Case GUN_USP
							Random = Rand(0, 3)							If Random = 0 Then
								AddAttachment(g,ATT_MATCH)
								g\HasToggledAttachments[ATT_MATCH] = True
							EndIf
					End Select
					Exit
				EndIf
			Next
			RemoveNPCGun(n)
		EndIf
		
		If n\Frame >= dieFrame
			If n\State[1] < 70*20 Then
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
	
	If n\NPCtype <> NPC_Zombie_Armed Then
		RotateEntity n\obj,0,EntityYaw(n\Collider)-180,0
	Else
		RotateEntity n\obj,-90,EntityYaw(n\Collider),0
	EndIf
	PositionEntity n\obj,EntityX(n\Collider),EntityY(n\Collider)-0.2,EntityZ(n\Collider)
	
	EntityAutoFade(n\obj,GetCameraFogRangeFar(Camera)-0.5,GetCameraFogRangeFar(Camera)+0.5)
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS