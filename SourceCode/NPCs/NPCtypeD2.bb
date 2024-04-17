
; ~ Class-D Constants

;[Block]
Const D2_FOLLOW = 6
Const D2_TRICK = 5
Const D2_RELOAD = 4
Const D2_GO_AFTER = 3
Const D2_FIND_COVER = 2
Const D2_ATTACK = 1
Const D2_IDLE = 0
;[End Block]

Function CreateNPCTypeD2(n.NPCs, model=-1)
	Local temp#, tex%
	
	If model = 0 Then
		
		n\NPCName = "Subject D-9341"
		n\BossName = "Subject D-9341"
		
		n\HP = 260+(45*SelectedDifficulty\OtherFactors)
		n\MaxBossHealth = n\HP
		n\Boss = n
	Else
		n\NPCName = "Class-D"
		n\HP = 60+(15*SelectedDifficulty\OtherFactors)
	EndIf
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.25, 0.32
	EntityType n\Collider, HIT_PLAYER
	
	n\obj = CopyEntity(NPCModel[Model_Class_D_Armed])
	
	temp# = 0.5 / MeshWidth(n\obj)
	ScaleEntity n\obj, temp, temp, temp
	
	Local random% = Rand(0,4)
	
	If n\NPCName = "Subject D-9341" Then
		n\texture = "GFX\npcs\d9341.jpg"
	Else
		Select random%
			Case 0
				n\texture = "GFX\npcs\classd1.jpg"
			Case 1
				n\texture = "GFX\npcs\classd2.jpg"
			Case 2
				n\texture = "GFX\npcs\classd4.jpg"
			Case 3
				n\texture = "GFX\npcs\classd5.jpg"
			Case 4
				n\texture = "GFX\npcs\classd6.jpg"
		End Select
	EndIf
	
	tex = LoadTexture_Strict(n\texture, 0, 2)
	TextureBlend(tex,5)
	EntityTexture(n\obj, tex)
	
	n\Speed = 4.0 / 100
	n\CollRadius = 0.16
	
	MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj)*5, MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*10)
	
	If n\NPCName = "Subject D-9341" Then
		random% = Rand(1, 150)
		If random <= 42 Then
			SwitchNPCGun%(n, GUN_P90)
		ElseIf random > 42 And random <= 84 Then	
			SwitchNPCGun%(n, GUN_MP5)
		ElseIf random > 84 And random <= 90 Then
			SwitchNPCGun%(n, GUN_RPK16)
		ElseIf random > 90 And random <= 96 Then
			SwitchNPCGun%(n, GUN_MP7)
		ElseIf random > 96 And random <= 100 Then
			SwitchNPCGun%(n, GUN_M4A1)
		ElseIf random > 100 And random <= 112 Then
			SwitchNPCGun%(n, GUN_M870)
		ElseIf random > 112 Then
			SwitchNPCGun%(n, GUN_SPAS12)
		EndIf
	Else
		random% = Rand(1, 100)
		If random <= 32 Then
			SwitchNPCGun%(n, GUN_USP)
			If Rand(1,10) = 1 Then
				n\State[2] = n\HP
			EndIf	
		ElseIf random > 32 And random <= 64 Then	
			SwitchNPCGun%(n, GUN_BERETTA)
			If Rand(1,8) = 1 Then
				n\State[2] = n\HP
			EndIf	
		ElseIf random > 62 And random <= 74 Then	
			SwitchNPCGun%(n, GUN_FIVESEVEN)
			If Rand(1,5) = 1 Then
				n\State[2] = n\HP
			EndIf
		ElseIf random > 74 And random <= 80 Then
			SwitchNPCGun%(n, GUN_P90)
		ElseIf random > 80 And random <= 90 Then
			SwitchNPCGun%(n, GUN_MP5)
		ElseIf random > 90 And random <= 96 Then
			SwitchNPCGun%(n, GUN_MP7)
		ElseIf random > 96 Then
			SwitchNPCGun%(n, GUN_SPAS12)
		EndIf
	EndIf
	
	CopyHitBoxes(n)
End Function

Function UpdateNPCTypeD2(n.NPCs)
	Local n2.NPCs, w.WayPoints, g.Guns, it.Items, r.Rooms, v3d.Vector3D
	Local prevFrame#, temp2%, deathFrame#, bone%, dist#, i%
	
	Local SeePlayer% = False
	Local PlayerDistSquared# = EntityDistanceSquared(n\Collider,Collider)
	Local IsPushable% = False
	
	prevFrame = n\Frame
	
	If n\IsDead = False Then
		If (n\State[0] = D2_IDLE Lor n\State[0] = D2_GO_AFTER) And n\State[0] <> D2_TRICK Then
			For n2.NPCs = Each NPCs
				If n2\HP > 0 Then
					If n2\NPCtype = NPC_NTF Then
						If NPCSeesEntity(n, n2\Collider) Then
							n\Target = n2
							If n\NPCName <> "Subject D-9341" Then
								If n\State[0] = D2_IDLE Then 
									PlayNPCSound(n, LoadTempSound("SFX\Character\D-Class\MTF_Spotted"+Rand(1, 5)+".ogg"))
								EndIf
							EndIf
							n\State[0] = D2_ATTACK
							Exit
						EndIf
					EndIf
				EndIf
			Next
			If (I_268\Using = 0 Lor I_268\Timer =< 0.0) And psp\Health > 0 Then
				If NPCSeesEntity(n, Camera) Then
					If gopt\GameMode <> GAMEMODE_CLASS_D Then
						If n\State[2] = n\HP Then
							n\State[0] = D2_TRICK
							PlayNPCSound(n, LoadTempSound("SFX\Character\D-Class\Unarmed"+Rand(1, 3)+".ogg"))
						Else
							If n\NPCName <> "Subject D-9341" Then
								If n\State[0] = D2_IDLE Then
									PlayNPCSound(n, LoadTempSound("SFX\Character\D-Class\MTF_Spotted"+Rand(1, 5)+".ogg"))
								EndIf
							EndIf
							n\State[0] = D2_ATTACK
						EndIf
					Else
						If n\GotHit Then
							If n\NPCName <> "Subject D-9341" Then
								If n\State[0] = D2_FOLLOW Then
									PlayNPCSound(n, LoadTempSound("SFX\Character\D-Class\MTF_Spotted"+Rand(1, 5)+".ogg"))
								EndIf
							EndIf
							n\State[0] = D2_ATTACK
						Else
							n\State[0] = D2_FOLLOW
						EndIf	
					EndIf
				EndIf
			EndIf
		EndIf
		
		Select n\State[0]
			Case D2_IDLE
				;[Block]
				Local roomfound% = False
				While roomfound = False
					If n\NPCRoom=Null Then
						GetNPCRoom(n)
					EndIf
					For r.Rooms = Each Rooms
						If Rand(5)=1 Then
							roomfound = True
							Exit
						EndIf
					Next
				Wend
				
				NPC_GoTo(n, FindNPCAnimation("Class-D-Armed", "idle"), FindNPCAnimation("Class-D-Armed", "walk"), r\obj, 0.3)
				;AnimateNPC(n, 212, 235, 0.1)
				;[End Block]
			Case D2_ATTACK
				;[Block]
				dist = EntityDistanceSquared(n\Collider, Collider)
				If (I_268\Using = 0 Lor I_268\Timer =< 0.0) And psp\Health > 0 Then
					If n\Target <> Null Then
						If n\Target\HP > 0 Then
							n\CurrSpeed = 0.0
							n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,n\Target\obj)
							RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
							If n\Reload = 0 Then
								If Abs(DeltaYaw(n\Collider,n\Target\Collider))<45.0 Then
									If NPCSeesEntity(n, n\Target\Collider)
										n\Gun\Ammo = n\Gun\Ammo - 1
										;SetNPCFrame(n,575)
										ShootTarget(0, 0, 0, n, Clamp(2 / dist, 0.0, 0.65), True, n\Gun\Damage * Rand(1, n\Gun\BulletsPerShot))
										If n\Gun\MaxGunshotSounds = 1 Then
											PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"))
										Else
											PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"))
										EndIf
										If GetNPCWeaponAnim(n\Gun\AnimType) = "pistol" Then
											n\Reload = Rand(25,50)
										Else
											n\Reload = n\Gun\ShootFrequency
										EndIf
									EndIf
								EndIf
							EndIf
						Else
							n\Target = Null
							n\State[0] = D2_IDLE
						EndIf
					Else
						If psp\Health <= 0 Then
							If n\NPCName <> "Subject D-9341" Then
								PlayNPCSound(n, LoadTempSound("SFX\Character\D-Class\Player_Kill"+Rand(1, 3)+".ogg"))
							EndIf
							m_msg\DeathTxt = GetLocalStringR("Singleplayer","d_death",Designation)
							n\State[0] = D2_IDLE
						Else
							n\CurrSpeed = 0.0
							n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,Collider)
							RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
							If n\Reload = 0 Then
								If Abs(DeltaYaw(n\Collider,Collider))<45.0 Then
									If NPCSeesEntity(n, Camera)
										If n\Gun\MaxGunshotSounds = 1 Then
											PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot.ogg"))
										Else
											PlaySound_Strict(LoadTempSound("SFX\Guns\" + n\Gun\Name + "\shoot" + Rand(1, n\Gun\MaxGunshotSounds) + ".ogg"))
										EndIf
										ShootPlayer(0, 0, 0, Clamp(2 / dist, 0.0, 0.65), True, n\Gun\Damage * Rand(1, n\Gun\BulletsPerShot))
										n\Gun\Ammo = n\Gun\Ammo - 1
										If GetNPCWeaponAnim(n\Gun\AnimType) = "pistol" Then
											n\Reload = Rand(25,50)
										Else
											n\Reload = n\Gun\ShootFrequency
										EndIf
									Else
										n\LastSeen = Collider
										n\IdleTimer = 70*8
										n\State[0] = D2_GO_AFTER
									EndIf
								EndIf
							EndIf
						EndIf
						
						If n\Gun\Ammo <= 0 Then
							If n\NPCName <> "Subject D-9341" Then
								PlayNPCSound(n, LoadTempSound("SFX\Character\D-Class\Reload"+Rand(1, 4)+".ogg"))
							EndIf
							n\State[0] = D2_FIND_COVER
						EndIf
					EndIf
					
					v3d = FindNPCAnimation("Class-D-Armed", GetNPCWeaponAnim(n\Gun\AnimType) + "_idle")
					AnimateNPC(n, v3d\x, v3d\y, v3d\z)
				EndIf
				;[End Block]
			Case D2_FIND_COVER
				;[Block]
				If n\Gun\Ammo <= 0 Then
					If n\Target<>Null Then
						temp2 = NPC_GoToCover(n, FindNPCAnimation("Class-D-Armed", GetNPCWeaponAnim(n\Gun\AnimType) + "_walk"), n\Target\Collider, 0.8)
					Else
						temp2 = NPC_GoToCover(n, FindNPCAnimation("Class-D-Armed", GetNPCWeaponAnim(n\Gun\AnimType) + "_walk"), Collider, 0.8)
					EndIf
				EndIf
				If temp2 Then
					n\State[0] = D2_RELOAD
				EndIf
				;[End Block]
			Case D2_GO_AFTER
				;[Block]
				NPC_GoTo(n, FindNPCAnimation("Class-D-Armed", GetNPCWeaponAnim(n\Gun\AnimType) + "_idle"), FindNPCAnimation("Class-D-Armed", GetNPCWeaponAnim(n\Gun\AnimType) + "_walk"), Collider, 0.8)
				If n\IdleTimer = 0.0 Then
					n\State[0] = D2_IDLE
				EndIf
				;[End Block]
			Case D2_RELOAD
				;[Block]
				v3d = FindNPCAnimation("Class-D-Armed", GetNPCWeaponAnim(n\Gun\AnimType) + "_reload")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False)
				If n\Frame >= v3d\y Then
					n\Gun\Ammo = n\Gun\MaxAmmo
					n\State[0] = D2_GO_AFTER
				EndIf
				;[End Block]
			Case D2_TRICK
				;[Block]
				dist = EntityDistanceSquared(n\Collider, Collider)
				AnimateNPC(n,357,381,0.2)
				HideEntity(n\Gun\obj)
				PointEntity n\obj, Collider
				RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
				If n\HP <> n\State[2] Lor dist <= 10.0 Then
					If n\NPCName <> "Subject D-9341" Then
						PlayNPCSound(n, LoadTempSound("SFX\Character\D-Class\MTF_Spotted"+Rand(1, 5)+".ogg"))
					EndIf
					n\State[0] = D2_ATTACK
					n\State[2] = 0
					ShowEntity(n\Gun\obj)
				ElseIf dist > 100.0 Then
					n\State[0] = D2_IDLE
					ShowEntity(n\Gun\obj)
				EndIf	
				;[End block]
			Case D2_FOLLOW
				;[Block]
				If (Not n\GotHit) Then
					;Player can push D in this state
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
						If EntityDistanceSquared(n\Collider, Collider) < PowTwo(0.5) Then
							If n\CurrSpeed <= 0.01 Then
								n\CurrSpeed = 0.0
								AnimateNPC(n,212,235,0.3)
								n\State[0] = D2_FOLLOW
							Else
								n\CurrSpeed = CurveValue(0.0,n\CurrSpeed,20.0)
								AnimateNPC(n,236,260,0.3, n\CurrSpeed*30)
								MoveEntity n\Collider, 0, 0, n\CurrSpeed/2 * FPSfactor
							EndIf
						Else
							n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
							AnimateNPC(n,236,260,0.3, n\CurrSpeed*30)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed/2 * FPSfactor
						EndIf
					Else
						Local v3d_1.Vector3D, v3d_2.Vector3D
						v3d_1 = CreateVector3D(212,235,0.3); ~ Idle
						v3d_2 = CreateVector3D(236,260,0.3); ~ Walk
						NPC_GoTo(n, v3d_1, v3d_2, Collider, 0.1)
						Delete v3d_1
						Delete v3d_2
					EndIf
					
					If PlayerDistSquared > PowTwo(16) Then
						Local shortestDist# = 10000.0
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
				Else
					n\State[0] = D2_ATTACK
				EndIf
				;[End Block]
			Case STATE_SCRIPT
				;[Block]
				
				;[End Block]
		End Select
		n\IdleTimer = Max(0.0, n\IdleTimer - FPSfactor)
		n\Reload = Max(0, n\Reload - FPSfactor)
		
		;TODO: Commented out, need to find a good solution for this
;		If n\State[0] = D2_IDLE Then
;			If n\CurrSpeed > 0.01 Then
;				If prevFrame < 244 And AnimTime(n\obj)=>244 Then
;					sfxstep = GetStepSound(n\Collider,n\CollRadius)
;					PlaySound2(StepSFX(sfxstep,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
;				ElseIf prevFrame < 256 And AnimTime(n\obj)=>256
;					sfxstep = GetStepSound(n\Collider,n\CollRadius)
;					PlaySound2(StepSFX(sfxstep,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
;				EndIf
;			EndIf
;		ElseIf n\State[0] = D2_FIND_COVER Lor D2_GO_AFTER Then
;			If n\CurrSpeed > 0.01 Then
;				If prevFrame < 603 And AnimTime(n\obj)=>603
;					sfxstep = GetStepSound(n\Collider,n\CollRadius)
;					PlaySound2(StepSFX(sfxstep,1,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
;				ElseIf prevFrame =< 612 And AnimTime(n\obj)=<612
;					sfxstep = GetStepSound(n\Collider,n\CollRadius)
;					PlaySound2(StepSFX(sfxstep,1,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
;				EndIf
;			EndIf
;		EndIf
		
	Else
		
		Select n\State[1]
			Case 0.0
				v3d = FindNPCAnimation("Class-D-Armed", "death_front")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False) ;from front
			Case 1.0
				v3d = FindNPCAnimation("Class-D-Armed", "death_left")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False) ;from left
			Case 2.0
				v3d = FindNPCAnimation("Class-D-Armed", "death_back")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False) ;from back
			Case 3.0
				v3d = FindNPCAnimation("Class-D-Armed", "death_right")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False) ;from right
		End Select
		n\LastSeen = 0.0
		n\Reload = 0.0
		If n\Gun <> Null And n\Frame >= v3d\y-0.5 Then
			bone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", "Class-D", "weapon_hand_bonename"))
			For g = Each Guns
				If g\ID = n\Gun\ID Then
					it = CreateItem(g\DisplayName, g\name, EntityX(bone%, True), EntityY(bone%, True) + 0.025, EntityZ(bone%, True))
					EntityType it\collider, HIT_ITEM
					it\state = n\Gun\Ammo
					it\state2 = Rand(g\MaxCurrAmmo,g\MaxCurrAmmo*2)
					it\Dropped = 1
					Exit
				EndIf
			Next
			RemoveNPCGun(n)
		EndIf
	EndIf
	
	If n\IsDead Then
		If n\NPCName = "Subject D-9341" Then
			If n\State[4] < 70*25 Then
				n\State[4] = n\State[4] + FPSfactor * 2
			Else
				If n\State[4] >= 70*25 And n\State[4] < 1000 Then
					n\State[4] = 1000
				ElseIf n\State[4] >= 1000 And n\State[4] < 2000 Then
					EntityAlpha n\obj,Inverse((n\State[4]-1000.0)/1000.0)
					n\State[4] = n\State[4] + 2 * FPSfactor
				Else
					LoadGameQuick(SavePath + CurrSave\Name + "\")
					RemoveNPC(n)
					Return
				EndIf
			EndIf
		Else
			If n\State[4] < 70*25 Then
				n\State[4] = n\State[4] + FPSfactor
			Else
				If n\State[4] >= 70*25 And n\State[4] < 1000 Then
					n\State[4] = 1000
				ElseIf n\State[4] >= 1000 And n\State[4] < 2000 Then
					EntityAlpha n\obj,Inverse((n\State[4]-1000.0)/1000.0)
					n\State[4] = n\State[4] + 2*FPSfactor
				Else
					RemoveNPC(n)
					Return
				EndIf
			EndIf
		EndIf
	EndIf
	
	;Is the NPC dead?
	If n\HP <= 0 And (Not n\IsDead) Then
		n\IsDead = True
		;This needs to be rewritten!
		Local temp% = (EntityYaw(Camera) - EntityYaw(n\obj) + 45 + 180) Mod 360
		n\State[1] = 0.0
		If temp > 90 Then
			n\State[1] = 1.0
			If temp > 180 Then
				n\State[1] = 2.0
				If temp > 270 Then
					n\State[1] = 3.0
				EndIf
			EndIf
		EndIf
		SetNPCFrame(n, 0)
	EndIf
	
	PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.32, EntityZ(n\Collider))
	
	RotateEntity n\obj, EntityPitch(n\Collider), EntityYaw(n\Collider), 0
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D