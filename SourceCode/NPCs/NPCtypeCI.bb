
; ~ CI Constants

;[Block]
Const CI_WONDERING = 8
Const CI_TAKE_COVER = 7
Const CI_GO_TO_ATTACK = 6
Const CI_THROW_GRENADE = 5
Const CI_RELOAD = 4
Const CI_GO_AFTER = 3
Const CI_GO_TO_RELOAD = 2
Const CI_ATTACK = 1
Const CI_IDLE = 0
;[End Block]

Function CreateNPCtypeCI(n.NPCs)
	Local temp#, random%
	
	n\NPCName = "Chaos Insurgency"
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.25, 0.32
	EntityType n\Collider, HIT_PLAYER
	
	n\obj = CopyEntity(NPCModel[Model_CI])
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "CI", "speed") / 100.0)
	temp# = (GetINIFloat("DATA\NPCs.ini", "CI", "scale") / 2.5)
	ScaleEntity n\obj, temp, temp, temp
	MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj)*5, MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*10)
	
	random% = Rand(1, 105)
	If random <= 42 Then
		SwitchNPCGun%(n, GUN_P90)
	ElseIf random > 42 And random <= 84 Then	
		SwitchNPCGun%(n, GUN_MP7)	
	ElseIf random > 84 And random <= 90 Then
		SwitchNPCGun%(n, GUN_MP5)
	ElseIf random > 90 And random <= 96 Then
		SwitchNPCGun%(n, GUN_M4A1)
	ElseIf random > 96 And random <= 100 Then
		SwitchNPCGun%(n, GUN_RPK16)
	ElseIf random > 100 
		SwitchNPCGun%(n, GUN_M870)
	EndIf
	
	n\Gun\Ammo = n\Gun\MaxAmmo
	
	n\CollRadius = 0.16
	
	n\HP = 180+(20*SelectedDifficulty\OtherFactors)
	
	n\BlinkTimer = 70.0*Rnd(5,8)
	
	CopyHitBoxes(n)
End Function

Global CICHN%

Function UpdateNPCtypeCI(n.NPCs)
	Local n2.NPCs, w.WayPoints, g.Guns, it.Items, r.Rooms, v3d.Vector3D
	Local prevFrame#, temp2%, deathFrame#, bone%, dist#, gr.Grenades
	
	prevFrame = n\Frame
	
	If n\IsDead = False Then
		
		If n\State[0] = CI_IDLE Lor n\State[0] = CI_GO_AFTER Then
			For n2.NPCs = Each NPCs
				If n2\HP > 0 Then
					If n2\NPCtype = NPC_NTF Then
						If NPCSeesEntity(n, n2\Collider) Then
							n\Target = n2
							If n\State[0] = CI_IDLE Then 
								If (Not n\IsDead) Then
									If (Not ChannelPlaying(CICHN)) Then CICHN = PlayNPCSound(n, LoadTempSound("SFX\Character\CI\Spotted_"+Rand(1, 4)+".ogg"))
								Else
									StopChannel(CICHN)
								EndIf
							EndIf	
							n\State[0] = CI_GO_TO_ATTACK
							Exit
						EndIf
					EndIf
				EndIf
			Next
			If (I_268\Using = 0 Lor I_268\Timer =< 0.0) And psp\Health > 0 Then
				If IsPlayerOutside() Then
					If NPCSeesEntity(n, Camera, True) And (I_268\Using = 0 Lor I_268\Timer =< 0.0) And psp\Health > 0 Then
						If n\State[0] = CI_IDLE Then 
							If (Not n\IsDead) Then
								If (Not ChannelPlaying(CICHN)) Then CICHN = PlayNPCSound(n, LoadTempSound("SFX\Character\CI\Spotted_"+Rand(1, 7)+".ogg"))
							Else
								StopChannel(CICHN)
							EndIf
						EndIf
						n\State[0] = CI_GO_TO_ATTACK
					EndIf
				Else
					If NPCSeesEntity(n, Camera) And (I_268\Using = 0 Lor I_268\Timer =< 0.0) And psp\Health > 0 Then
						If n\State[0] = CI_IDLE Then 
							If (Not n\IsDead) Then
								If (Not ChannelPlaying(CICHN)) Then CICHN = PlayNPCSound(n, LoadTempSound("SFX\Character\CI\Spotted_"+Rand(1, 7)+".ogg"))
							Else
								StopChannel(CICHN)
							EndIf
						EndIf
						n\State[0] = CI_GO_TO_ATTACK
					EndIf
				EndIf
			EndIf
		EndIf
		
		Select n\State[0]
			Case CI_IDLE
				;[Block]
				Local roomfound% = False
				While roomfound = False
					If n\NPCRoom=Null Then
						GetNPCRoom(n)
					EndIf
					For r.Rooms = Each Rooms
						If Rand(2)=1 Then
							roomfound = True
							Exit
						EndIf
					Next
				Wend
				
				NPC_GoTo(n, FindNPCAnimation(n\NPCtype, "idle"), FindNPCAnimation(n\NPCtype, "walk"), r\obj, 0.3)
				
				;[End Block]
			Case CI_GO_TO_ATTACK
				;[Block]
				If Rand(0,10) = 9 Then
					n\State[0] = CI_THROW_GRENADE
				Else
					n\State[0] = CI_ATTACK
				EndIf
				;[End Block]
			Case CI_ATTACK
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
									If IsPlayerOutside() Then
										If NPCSeesEntity(n, n\Target\Collider, True)
											n\Gun\Ammo = n\Gun\Ammo - 1
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
									Else
										If NPCSeesEntity(n, n\Target\Collider)
											n\Gun\Ammo = n\Gun\Ammo - 1
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
							EndIf
						Else
							n\Target = Null
							n\State[0] = CI_IDLE
						EndIf
					Else
						If psp\Health <= 0 Then
							If (Not ChannelPlaying(CICHN)) Then CICHN = PlayNPCSound(n, LoadTempSound("SFX\Character\CI\Terminated_"+Rand(1, 11)+".ogg"))
							m_msg\DeathTxt = GetLocalStringR("Singleplayer","death_ci",Designation)
							n\State[0] = CI_IDLE
						Else
							n\CurrSpeed = 0.0
							n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,Collider)
							RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
							If n\Reload = 0 Then
								If Abs(DeltaYaw(n\Collider,Collider))<45.0 Then
									If IsPlayerOutside() Then
										If NPCSeesEntity(n, Camera, True)
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
											n\State[0] = CI_GO_AFTER
										EndIf
									Else
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
											n\State[0] = CI_GO_AFTER
										EndIf
									EndIf
								EndIf
							EndIf
						EndIf
						
						If n\Gun\Ammo <= 0 Then
							If (Not ChannelPlaying(CICHN)) Then CICHN = PlayNPCSound(n, LoadTempSound("SFX\Character\CI\CoverMe_"+Rand(1, 4)+".ogg"))
							n\State[0] = CI_GO_TO_RELOAD
						EndIf
					EndIf
					
					v3d = FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_idle")
					AnimateNPC(n, v3d\x, v3d\y, v3d\z)
				EndIf
				;[End Block]
			Case CI_GO_TO_RELOAD
				;[Block]
				If n\Gun\Ammo <= 0 Then
					If n\Target<>Null Then
						temp2 = NPC_GoToCover(n, FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_walk"), n\Target\Collider, 0.8)
					Else
						temp2 = NPC_GoToCover(n, FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_walk"), Collider, 0.8)
					EndIf
				EndIf
				If temp2 Then
					n\State[0] = CI_RELOAD
				EndIf
				;[End Block]
			Case CI_GO_AFTER
				;[Block]
				NPC_GoTo(n, FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_idle"), FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_walk"), Collider, 0.8)
				If n\IdleTimer = 0.0 Then
					n\State[0] = CI_IDLE
				EndIf
				;[End Block]
			Case CI_RELOAD
				;[Block]
				v3d = FindNPCAnimation(n\NPCtype, GetNPCWeaponAnim(n\Gun\AnimType) + "_reload")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False)
				If n\Frame >= v3d\y Then
					n\Gun\Ammo = n\Gun\MaxAmmo
					n\State[0] = CI_GO_AFTER
				EndIf
				;[End Block]
			Case CI_THROW_GRENADE
				;[Block]
				Local Throwframe# = GetINIInt("Data\NPCAnims.ini", n\NPCName, "throw_grenade_frame")
				
				PointEntity(n\Collider, Collider)
				v3d = FindNPCAnimation(n\NPCtype,"throw_grenade")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False)
				If n\Frame >= Throwframe Then
					CreateGrenade(EntityX(n\Collider),EntityY(n\Collider),EntityZ(n\Collider), EntityPitch(n\Collider), EntityYaw(n\Collider),Grenade_RGD5)
					;n\IsNPCThrownGrenade% = True
					For gr = Each Grenades
						gr\Speed = gr\Speed*1.7
					Next
					n\State[0] = CI_GO_AFTER
				EndIf
				;[End Block]
			Case STATE_SCRIPT
				;[Block]
				
				;[End Block]
		End Select
		n\IdleTimer = Max(0.0, n\IdleTimer - FPSfactor)
		n\Reload = Max(0, n\Reload - FPSfactor)
		
		;Play step sounds
		If n\CurrSpeed > 0.01 Then
			If prevFrame > 500 And n\Frame<495 Then
				Local sfxstep = GetStepSound(n\Collider,n\CollRadius)
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
		
	Else
		
		Select n\State[1]
			Case 0.0
				v3d = FindNPCAnimation(n\NPCtype, "death_2")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False) ;from front
			Case 1.0
				v3d = FindNPCAnimation(n\NPCtype, "death_2")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False) ;from left
			Case 2.0
				v3d = FindNPCAnimation(n\NPCtype, "death_3")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False) ;from back
			Case 3.0
				v3d = FindNPCAnimation(n\NPCtype, "death_1")
				AnimateNPC(n, v3d\x, v3d\y, v3d\z, False) ;from right
		End Select
		n\LastSeen = 0.0
		n\Reload = 0.0
		If (Not IsPlayerOutside()) Then
			If n\Frame >= v3d\y-0.5
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
		If n\Gun <> Null And n\Frame >= v3d\y-0.5 Then
			Local grenadebone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "grenade_hand_bonename"))
			
			If Rand(0,1) = 1 Then
				it = CreateItem(GetLocalString("Item names","grenade_rgd5"), "rgd5", EntityX(grenadebone%, True), EntityY(grenadebone%, True) + 0.025, EntityZ(grenadebone%, True))
				EntityType it\collider, HIT_ITEM
				it\Dropped = 1
			EndIf
			
			bone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "weapon_hand_bonename"))
			
			For g = Each Guns
				If g\ID = n\Gun\ID Then
					it = CreateItem(g\DisplayName, g\name, EntityX(bone%, True), EntityY(bone%, True) + 0.025, EntityZ(bone%, True))
					EntityType it\collider, HIT_ITEM
					it\state = n\Gun\Ammo
					it\state2 = Rand(g\MaxCurrAmmo,g\MaxCurrAmmo*3)
					it\Dropped = 1
					Exit
				EndIf
			Next
			RemoveNPCGun(n)
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
;~C#Blitz3D TSS