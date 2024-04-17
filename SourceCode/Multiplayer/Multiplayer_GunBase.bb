
; ~ [Player gun sound channel constants]
;[Block]
Const MaxGunChannels = 2
Const GUN_CHANNEL_SHOT = 0
Const GUN_CHANNEL_OTHER = 1
;[End Block]

Function UpdatePlayerGunsServer()
	Local i%
	
	For i = 0 To (mp_I\MaxPlayers-1)
		If Players[i]<>Null Then
			If (Not IsSpectator(i)) Then ;CHECK FOR IMPLEMENTATION
				UpdatePlayerGun(i)
			EndIf
		EndIf
	Next
	
End Function

Function UpdatePlayerGun(playerID%)
	Local g.Guns, n.NPCs
	
	Local DeployTime#,Accuracy#,DamageOnEntity%,RateOfFire#,MaxAmmo%,ReloadEmptyTime#,ReloadTime#,ReloadStartTime#
	Local GunType%,ShootAmount%,AttackDelay#,Range#,NPCsKilled%
	
	Local i%
	
	If Players[playerID]\CurrHP > 0 Then
		
		Players[playerID]\PrevShootState = Players[playerID]\ShootState
		
		For g = Each Guns
			If g\ID = Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot] Then
				DeployTime = g\Deploy_Time
				Accuracy = g\Accuracy
				DamageOnEntity = g\DamageOnEntity
				RateOfFire = g\Rate_Of_Fire
				MaxAmmo = g\MaxCurrAmmo
				ReloadEmptyTime = g\Reload_Empty_Time
				ReloadTime = g\Reload_Time
				ReloadStartTime = g\Reload_Start_Time
				GunType = g\GunType
				ShootAmount = g\Amount_Of_Bullets
				AttackDelay = g\ShootDelay
				Range = g\Range
				Exit
			EndIf
		Next
		
		If Players[playerID]\DeployState < DeployTime Then
			Players[playerID]\DeployState = Players[playerID]\DeployState + FPSfactor
		Else
			If Players[playerID]\IsPlayerSprinting And GunType <> GUNTYPE_MELEE Then
				Players[playerID]\PressMouse1 = False
			EndIf
			
			If GunType = GUNTYPE_SHOTGUN Then
				If Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot]=0 Then
					Players[playerID]\PressReload = False
				EndIf
				
				If (Players[playerID]\PressMouse1 And Players[playerID]\ShootState = 0.0) Then
					If Players[playerID]\Ammo[Players[playerID]\SelectedSlot] > 0 Then
						For i=1 To ShootAmount
							ShootGunMP(playerID,Accuracy,DamageOnEntity)
						Next
						If playerID = mp_I\PlayerID Then
							NPCsKilled = 0
							For n = Each NPCs
								If n\KilledBy = mp_I\PlayerID + 1 Then
									NPCsKilled = NPCsKilled + 1
									If NPCsKilled > 1 Then
										Exit
									EndIf
								EndIf
							Next
							If NPCsKilled > 1 Then
								Steam_Achieve(STAT_ONE_SHOT)
							EndIf
						EndIf
						If mp_I\PlayState = GAME_SERVER Then
							Players[playerID]\Ammo[Players[playerID]\SelectedSlot] = Players[playerID]\Ammo[Players[playerID]\SelectedSlot] - 1
						EndIf
						If playerID = mp_I\PlayerID Then
							mp_I\LocalAmmo = mp_I\LocalAmmo - 1
						EndIf
						Players[playerID]\ShootState = FPSfactor
						Players[playerID]\ReloadState = 0.0
						Players[playerID]\PressReload = False
					EndIf
				EndIf
				If Players[playerID]\ShootState > 0.0 And Players[playerID]\ShootState < RateOfFire Then
					Players[playerID]\ShootState = Players[playerID]\ShootState + FPSfactor
					Players[playerID]\ReloadState = 0.0
					Players[playerID]\PressReload = False
				Else
					Players[playerID]\ShootState = 0.0
				EndIf
				If Players[playerID]\Ammo[Players[playerID]\SelectedSlot] = MaxAmmo Lor Players[playerID]\ReloadState > 0.0 Then
					Players[playerID]\PressReload = False
				EndIf
				If Players[playerID]\PressReload Then
					Players[playerID]\ReloadState = FPSfactor
				EndIf
				
				If Players[playerID]\ReloadState > 0.0 And Players[playerID]\ReloadState < ReloadStartTime Then
					Players[playerID]\ShootState = 0.0
					Players[playerID]\ReloadState = Players[playerID]\ReloadState + FPSfactor
					If Players[playerID]\ReloadState >= ReloadStartTime Then
						If mp_I\PlayState = GAME_SERVER Then
							Players[playerID]\Ammo[Players[playerID]\SelectedSlot] = Players[playerID]\Ammo[Players[playerID]\SelectedSlot] + 1
							Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot] = Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot] - 1
						EndIf
						If playerID = mp_I\PlayerID Then
							mp_I\LocalAmmo = mp_I\LocalAmmo + 1
						EndIf
					EndIf
				ElseIf Players[playerID]\ReloadState >= ReloadStartTime And Players[playerID]\ReloadState < (ReloadStartTime+ReloadEmptyTime) Then
					Players[playerID]\ReloadState = Players[playerID]\ReloadState + FPSfactor
					If Players[playerID]\ReloadState >= (ReloadStartTime+ReloadEmptyTime) Then
						If Players[playerID]\Ammo[Players[playerID]\SelectedSlot] < MaxAmmo And Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot] > 0 Then
							Players[playerID]\ReloadState = ReloadStartTime
							If mp_I\PlayState = GAME_SERVER Then
								Players[playerID]\Ammo[Players[playerID]\SelectedSlot] = Players[playerID]\Ammo[Players[playerID]\SelectedSlot] + 1
								Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot] = Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot] - 1
							EndIf
							If playerID = mp_I\PlayerID Then
								mp_I\LocalAmmo = mp_I\LocalAmmo + 1
							EndIf
						EndIf
					EndIf
				Else
					Players[playerID]\ReloadState = 0.0
				EndIf
			ElseIf GunType = GUNTYPE_MELEE Then
				Players[playerID]\PressReload = False
				
				If (Players[playerID]\PressMouse1 And Players[playerID]\ShootState = 0.0) Then
					Players[playerID]\ShootState = FPSfactor
				EndIf
				If Players[playerID]\ShootState > 0.0 And Players[playerID]\ShootState < RateOfFire Then
					Players[playerID]\ShootState = Players[playerID]\ShootState + FPSfactor
					If Players[playerID]\ShootState >= AttackDelay And Players[playerID]\ShootState <= AttackDelay+FPSfactor Then
						For i=1 To ShootAmount
							ShootGunMP(playerID,Accuracy,DamageOnEntity,Range)
						Next
					EndIf
				Else
					Players[playerID]\ShootState = 0.0
				EndIf
			Else
				If Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot]=0 Then
					Players[playerID]\PressReload = False
				EndIf
				
				If Players[playerID]\ReloadState = 0.0 Then
					If Players[playerID]\PressMouse1 Lor Players[playerID]\ShootState = -1.0 Then
						If Players[playerID]\ShootState = 0.0 Lor Players[playerID]\ShootState = -1.0 Then
							If Players[playerID]\Ammo[Players[playerID]\SelectedSlot] > 0 Then
								For i=1 To ShootAmount
									ShootGunMP(playerID,Accuracy,DamageOnEntity)
								Next
								If mp_I\PlayState = GAME_SERVER Then
									Players[playerID]\Ammo[Players[playerID]\SelectedSlot] = Players[playerID]\Ammo[Players[playerID]\SelectedSlot] - 1
								EndIf
								If playerID = mp_I\PlayerID Then
									mp_I\LocalAmmo = mp_I\LocalAmmo - 1
								EndIf
								Players[playerID]\ShootState = FPSfactor
							EndIf
						EndIf
					EndIf
					If Players[playerID]\ShootState > 0.0 And Players[playerID]\ShootState < RateOfFire Then
						Players[playerID]\ShootState = Players[playerID]\ShootState + FPSfactor
					Else
						Players[playerID]\ShootState = 0.0
					EndIf
					If Players[playerID]\Ammo[Players[playerID]\SelectedSlot] > MaxAmmo Lor Players[playerID]\ReloadState > 0.0 Then
						Players[playerID]\PressReload = False
					EndIf
					If Players[playerID]\PressReload Then
						Players[playerID]\ReloadState = FPSfactor
					EndIf
				ElseIf Players[playerID]\ReloadState > 0.0 And Players[playerID]\ReloadState <= ReloadEmptyTime And Players[playerID]\Ammo[Players[playerID]\SelectedSlot] = 0 Then
					Players[playerID]\ShootState = 0.0
					Players[playerID]\ReloadState = Players[playerID]\ReloadState + FPSfactor
					If mp_I\PlayState = GAME_SERVER Then
						If GunType = GUNTYPE_SHOTGUN Then
							Players[playerID]\Ammo[Players[playerID]\SelectedSlot] = 0
						EndIf
					EndIf
					If playerID = mp_I\PlayerID Then
						mp_I\LocalAmmo = 0
					EndIf
				ElseIf Players[playerID]\ReloadState > 0.0 And Players[playerID]\ReloadState < ReloadTime And Players[playerID]\Ammo[Players[playerID]\SelectedSlot] > 0 Then
					Players[playerID]\ShootState = 0.0
					Players[playerID]\ReloadState = Players[playerID]\ReloadState + FPSfactor
					If mp_I\PlayState = GAME_SERVER Then
						If GunType = GUNTYPE_SHOTGUN Then
							Players[playerID]\Ammo[Players[playerID]\SelectedSlot] = 0
						EndIf
					EndIf
					If playerID = mp_I\PlayerID Then
						mp_I\LocalAmmo = 0
					EndIf
				Else
					Players[playerID]\ReloadState = 0.0
					If mp_I\PlayState = GAME_SERVER Then
						Local pAmmoDelta% = g\MaxCurrAmmo - Players[playerID]\Ammo[Players[playerID]\SelectedSlot]
						If Players[playerID]\Ammo[Players[playerID]\SelectedSlot] = 0 Then
							Players[playerID]\Ammo[Players[playerID]\SelectedSlot] = Players[playerID]\Ammo[Players[playerID]\SelectedSlot] + Min(pAmmoDelta, Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot])
						Else
							Players[playerID]\Ammo[Players[playerID]\SelectedSlot] = Players[playerID]\Ammo[Players[playerID]\SelectedSlot] + Min(pAmmoDelta, Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot]) + 1
							Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot] = Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot] - 1
						EndIf
						Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot] = Max(Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot] - pAmmoDelta, 0)
					EndIf
					If playerID = mp_I\PlayerID Then
						mp_I\LocalAmmo = MaxAmmo
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

Function PlayGunSoundsMP()
	CatchErrors("PlayGunSoundsMP")
	
	CatchErrors("Uncaught (PlayGunSoundsMP)")
End Function

Function ShootGunMP(playerID%,Accuracy#,DamageOnEntity%,Range#=0.0)
	Local temp,n.NPCs,p.Particles,j,de.Decals,ent_pick%,g.Guns,cmsg.ChatMSG
	Local i%, headshot%
	Local prevHP#, currHP#
	Local pID% = -1
	
	For n.NPCs = Each NPCs
		ShowNPCHitBoxes(n)
	Next
	For i = 0 To (mp_I\MaxPlayers-1)
		If Players[i]<>Null Then
			If i<>playerID Then
				ShowPlayerHitBoxes(i)
			EndIf
		EndIf
	Next
	EntityPickMode GetChild(mp_I\Map\obj,RMESH_INVISBLE),0
	If mp_I\MapInList\ChunkEnd > 0 Then
		For i = mp_I\MapInList\ChunkStart To mp_I\MapInList\ChunkEnd
			EntityPickMode GetChild(mp_I\Map\Chunks[i-mp_I\MapInList\ChunkStart],RMESH_INVISBLE),0
		Next
	EndIf
	Local pitch# = EntityPitch(Players[mp_I\PlayerID]\GunPivot)
	Local yaw# = EntityYaw(Players[mp_I\PlayerID]\GunPivot)
	TurnEntity Players[playerID]\GunPivot,Rnd(-Accuracy,Accuracy)/(1.0+(3.0*Players[playerID]\IronSight)),Rnd(-Accuracy,Accuracy)/(1.0+(3.0*Players[playerID]\IronSight)),0
	If Range<=0.0 Then
		EntityPick Players[playerID]\GunPivot,20.0
	Else
		EntityPick Players[playerID]\GunPivot,Range
	EndIf
	temp = 0
	ent_pick% = PickedEntity()
	If ent_pick%<>0 Then
		temp = 1
		For n.NPCs = Each NPCs
			prevHP = n\HP
			currHP = prevHP
			For j = 0 To 24
				If ent_pick% = n\HitBox1[j] ;Head has been shot
					n\GotHit = DamageOnEntity*4
					currHP = n\HP - n\GotHit
					If mp_I\PlayState = GAME_SERVER Then
						n\HP = n\HP - n\GotHit
					EndIf
					temp = 2
					Exit
				EndIf
				If ent_pick% = n\HitBox2[j] ;Body has been shot, doing damage with g\DamageOnEntity
					n\GotHit = DamageOnEntity
					currHP = n\HP - n\GotHit
					If mp_I\PlayState = GAME_SERVER Then
						n\HP = n\HP - n\GotHit
					EndIf
					temp = 2
					Exit
				EndIf
				If ent_pick% = n\HitBox3[j] ;Arms or legs have been shot, doing half as much damage as with the body
					n\GotHit = (DamageOnEntity/2)
					currHP = n\HP - n\GotHit
					If mp_I\PlayState = GAME_SERVER Then
						n\HP = n\HP - n\GotHit
					EndIf
					temp = 2
					Exit
				EndIf
			Next
			If currHP <= 0 And prevHP > 0 Then
				n\KilledBy = playerID + 1
				Players[playerID]\Kills = Players[playerID]\Kills + 1
				If playerID = mp_I\PlayerID Then
					If n\NPCtype = NPC_Zombie Lor n\NPCtype = NPC_Zombie_Armed Then
						Steam_Achieve(STAT_ZOMBIES)
					ElseIf n\NPCtype = NPC_SCP_939 Then
						For g = Each Guns
							If g\ID = Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot] Then
								If g\GunType = GUNTYPE_MELEE Then
									Steam_Achieve(STAT_939)
									Exit
								EndIf	
							EndIf
						Next
					ElseIf n\NPCtype = NPC_SCP_1048 Then
						If n\State[0] = MP1048a_STATE_ATTACK Then
							Steam_Achieve(STAT_1048)
						EndIf
					EndIf
				EndIf
			EndIf
			If temp = 2 Then
				Exit
			EndIf
		Next
		For i = 0 To (mp_I\MaxPlayers-1)
			If Players[i]<>Null Then
				prevHP = Players[i]\CurrHP
				currHP = prevHP
				For j = 0 To 24
					If ent_pick% = Players[i]\HitBox1[j] Then ;Head has been shot
						If Players[i]\Team<>Players[playerID]\Team Then
							DamagePlayer(i, DamageOnEntity*4, DamageOnEntity/2)
							currHP = Players[i]\CurrHP
							If mp_I\PlayState = GAME_CLIENT Then
								Players[i]\CurrHP = prevHP
							EndIf
							temp = 2
							headshot = True
						Else
							temp = 0
						EndIf
						pID = i
						Exit
					ElseIf ent_pick% = Players[i]\HitBox2[j] Then ;Body has been shot, doing damage with g\DamageOnEntity
						If Players[i]\Team<>Players[playerID]\Team Then
							DamagePlayer(i, DamageOnEntity/2, DamageOnEntity)
							currHP = Players[i]\CurrHP
							If mp_I\PlayState = GAME_CLIENT Then
								Players[i]\CurrHP = prevHP
							EndIf
							temp = 2
						Else
							temp = 0
						EndIf
						pID = i
						Exit
					ElseIf ent_pick% = Players[i]\HitBox3[j] Then ;Arms or legs have been shot, doing half as much damage as with the body
						If Players[i]\Team<>Players[playerID]\Team Then
							DamagePlayer(i, DamageOnEntity/4, DamageOnEntity/2)
							currHP = Players[i]\CurrHP
							If mp_I\PlayState = GAME_CLIENT Then
								Players[i]\CurrHP = prevHP
							EndIf
							temp = 2
						Else
							temp = 0
						EndIf
						pID = i
						Exit
					EndIf
				Next
				If currHP <= 0 And prevHP > 0 Then
					Players[playerID]\Kills = Players[playerID]\Kills + 1
					Players[i]\Deaths = Players[i]\Deaths + 1
					For g = Each Guns
						If g\ID = Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot] Then
							If headshot Then
								cmsg = AddChatMSG("death_headshot", 0, SERVER_MSG_IS, CHATMSG_TYPE_THREEPARAM_TRANSLATE)
								If playerID = mp_I\PlayerID Then
									Steam_Achieve(STAT_LOBOTOMY)
								EndIf
							Else	
								cmsg = AddChatMSG("death_killed1", 0, SERVER_MSG_IS, CHATMSG_TYPE_THREEPARAM_TRANSLATE)
							EndIf
							cmsg\Msg[1] = Players[playerID]\Name
							cmsg\Msg[2] = Players[i]\Name
							cmsg\Msg[3] = "death_killed2"
							cmsg\Msg[4] = g\DisplayName
							Exit
						EndIf
					Next		
				EndIf
				If temp = 2 Then
					Exit
				EndIf
			EndIf
		Next
	EndIf
	Local pmp.ParticleMP
	If mp_I\PlayState = GAME_SERVER Then
		If temp = 2 Then
			pmp = CreateParticleMP(PARTICLEMP_SHOT,PickedX(),PickedY(),PickedZ(),0,0,0,0,0,playerID)
			If pID = 0 And mp_I\PlayState = GAME_SERVER Then
				mpl\DamageTimer = 70
				PlaySound_Strict BullethitSFX
			ElseIf pID > 0 Then
				Players[pID]\InjuryType = INJURY_BULLET
			EndIf
		ElseIf temp = 1 Then
			pmp = CreateParticleMP(PARTICLEMP_WALL,PickedX(),PickedY(),PickedZ(),EntityPitch(Players[playerID]\GunPivot),EntityYaw(Players[playerID]\GunPivot),-PickedNX(),-PickedNY(),-PickedNZ(),playerID)
		EndIf
	EndIf
	
	If (Not Players[playerID]\IronSight) Then
		RotateEntity Players[mp_I\PlayerID]\GunPivot,pitch,yaw,0
	EndIf
	EntityPickMode GetChild(mp_I\Map\obj,RMESH_INVISBLE),2
	If mp_I\MapInList\ChunkEnd > 0 Then
		For i = mp_I\MapInList\ChunkStart To mp_I\MapInList\ChunkEnd
			EntityPickMode GetChild(mp_I\Map\Chunks[i-mp_I\MapInList\ChunkStart],RMESH_INVISBLE),2
		Next
	EndIf
	For n.NPCs = Each NPCs
		HideNPCHitBoxes(n)
	Next
	For i = 0 To (mp_I\MaxPlayers-1)
		If Players[i]<>Null Then
			HidePlayerHitBoxes(i)
		EndIf
	Next
	
End Function

Function AnimateGunsServer()
	
	If (Not g_I\GunAnimFLAG) And Players[mp_I\PlayerID]\CurrSpeed=0.0 And (Not Players[mp_I\PlayerID]\IsPlayerSprinting) And (Not Players[mp_I\PlayerID]\IronSight)
		If GunPivot_YSide%=0
			If GunPivot_Y# > -0.005
				GunPivot_Y# = GunPivot_Y# - (0.00005*FPSfactor)
			Else
				GunPivot_Y# = -0.005
				GunPivot_YSide% = 1
			EndIf
		Else
			If GunPivot_Y# < 0.0
				GunPivot_Y# = GunPivot_Y# + (0.00005*FPSfactor)
			Else
				GunPivot_Y# = 0.0
				GunPivot_YSide% = 0
			EndIf
		EndIf
		
		If GunPivot_X# < 0.0
			GunPivot_X# = Min(GunPivot_X#+(0.0001*FPSfactor),0.0)
		ElseIf GunPivot_X# > 0.0
			GunPivot_X# = Max(GunPivot_X#-(0.0001*FPSfactor),0.0)
		EndIf
	ElseIf (Not g_I\GunAnimFLAG) And Players[mp_I\PlayerID]\CurrSpeed<>0.0 And (Not Players[mp_I\PlayerID]\IsPlayerSprinting%) And (Not Players[mp_I\PlayerID]\IronSight)
        If GunPivot_XSide%=0
            If GunPivot_X# > -0.0025
                GunPivot_X# = GunPivot_X# - (0.000075 / (1.0 + (Players[mp_I\PlayerID]\Crouch)) * FPSfactor)
                If GunPivot_X# > -0.00125
                    GunPivot_Y# = Min(GunPivot_Y#+(0.000125 / (1.0 + (Players[mp_I\PlayerID]\Crouch)) * FPSfactor),0.001)
                Else
                    GunPivot_Y# = Max(GunPivot_Y#-(0.000125 / (1.0 + (Players[mp_I\PlayerID]\Crouch)) * FPSfactor),0.0)
                EndIf
            Else
                GunPivot_X# = -0.0025
                GunPivot_Y# = 0.0
                GunPivot_XSide% = 1
            EndIf
        Else
            If GunPivot_X# < 0.0
                GunPivot_X# = GunPivot_X# + (0.000075 / (1.0 + (Players[mp_I\PlayerID]\Crouch)) * FPSfactor)
                If GunPivot_X# < -0.00125
                    GunPivot_Y# = Min(GunPivot_Y#+(0.000125 / (1.0 + (Players[mp_I\PlayerID]\Crouch)) * FPSfactor),0.001)
                Else
                    GunPivot_Y# = Max(GunPivot_Y#-(0.000125 / (1.0 + (Players[mp_I\PlayerID]\Crouch)) * FPSfactor),0.0)
                EndIf
            Else
                GunPivot_X# = 0.0
                GunPivot_Y# = 0.0
                GunPivot_XSide% = 0
            EndIf
        EndIf
    Else
		If GunPivot_Y# < 0.0
			GunPivot_Y# = Max(GunPivot_Y#+(0.0001*FPSfactor),0.0)
		Else
			GunPivot_Y# = 0.0
		EndIf
		
		If GunPivot_X# < 0.0
			GunPivot_X# = Min(GunPivot_X#+(0.0001*FPSfactor),0.0)
		ElseIf GunPivot_X# > 0.0
			GunPivot_X# = Max(GunPivot_X#-(0.0001*FPSfactor),0.0)
		EndIf
	EndIf
	
	PositionEntity g_I\GunPivot,EntityX(mpl\CameraPivot), EntityY(mpl\CameraPivot)+GunPivot_Y#, EntityZ(mpl\CameraPivot)
	MoveEntity g_I\GunPivot,GunPivot_X#,0,0
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D