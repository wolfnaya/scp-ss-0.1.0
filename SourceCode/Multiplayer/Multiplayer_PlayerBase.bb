
Const MaxSlots = 3
Const SlotsWithNoAmmo = 1

Const INJURY_DEFAULT = 0
Const INJURY_BULLET = 1

Type Player
	;Unused
	;Field IP%,Port%
	;Field Connected%
	;Field CurrLoadPercent%
	;Field MuzzleFlashSprite%
	
	;Used
	Field Name$
	Field FinishedLoading%
	Field LastMsgTime
	Field IsReady%
	Field SendChatMSG$
	Field IsChatMSGTranslated%
	Field CurrHP#
	Field CurrStamina#
	Field StaminaEffectTimer%
	Field X#,Y#,Z#
	Field Pitch#,Yaw#
	Field DropSpeed#,CurrSpeed#
	Field Collider%,obj_lower%,obj_upper%
	Field walkangle#
	Field walking%
	Field Crouch%,CrouchState#
	Field IsPlayerSprinting%
	Field SelectedSlot%
	Field WeaponInSlot%[MaxSlots]
	Field Ammo%[MaxSlots - SlotsWithNoAmmo]
	Field ReloadAmmo%[MaxSlots - SlotsWithNoAmmo]
	Field PressMouse1
	Field PressReload
	Field PressSprint
	Field GunPivot
	Field ReloadState#
	Field ShootState#
	Field PrevShootState#
	Field DeployState#
	Field GunSFXChannel%[MaxGunChannels]
	Field IronSight%
	Field CurrKevlar#
	Field Team%
	Field ForceTeam%
	Field Item.InventoryItem
	;Used to determine the ping
	Field Ping%
	Field HasPinged%
	;Name tags
	Field NameTag%
	Field CurrChunk%
	Field HitBox1[MaxHitBoxes]
	Field HitBox2[MaxHitBoxes]
	Field HitBox3[MaxHitBoxes]
	Field Kills%
	Field Deaths%
	Field GunModel%
	Field GunModelMuzzleFlash%
	Field AnimState_Lower%
	Field AnimState_Upper%
	Field Sound_CHN%
	Field VoiceLine%
	Field VoiceLineNumber%
	Field OverHerePosition.Vector3D
	Field OverHereCommand%
	Field OverHereSprite%
	Field OverHereSpriteTime#
	Field InjuryType%
	Field Ignored%
	Field MapVote%
	;Steam Stuff
	Field SteamIDLower%
	Field SteamIDUpper%
	Field SteamLocationString$
	Field IsSpectator%
	
	;Check for those
	Field WantsSlot% ;Only for client!
	Field WantsTeam% ;Only for client!
End Type

Const MaxPlayers = 12
Global Players.Player[MaxPlayers]

Function CreatePlayer(playerID%)
	Local temp#
	
	Players[playerID]\Collider = CreatePivot()
	EntityRadius Players[playerID]\Collider,0.15,0.30
	EntityType Players[playerID]\Collider,HIT_PLAYER_MP
	
	;CHECK FOR IMPLEMENTATION
	ChangePlayerModel(playerID, Team_MTF-1)
	
	Local g.Guns, n%
	
	For g = Each Guns
		If g\IsSeparate Then
			ReplaceTextureByMaterial("GFX\weapons\models\hands\hands_ntf.png",g\HandsObj,"hands",5)
		Else
			ReplaceTextureByMaterial("GFX\weapons\models\hands\hands_ntf.png",g\obj,"hands",5)
		EndIf
		
		For n = 0 To MaxAttachments - 1
			g\HasPickedAttachments[n] = True
		Next
	Next
	
	If mp_I\PlayState = GAME_SERVER Then
		Players[playerID]\CurrHP = 100
		Players[playerID]\CurrKevlar = 100
		Players[playerID]\CurrStamina = 100.0
		Players[playerID]\StaminaEffectTimer = 0
		Players[playerID]\SelectedSlot = QUICKSLOT_SECONDARY
		Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot] = GUN_BERETTA
		Players[playerID]\WeaponInSlot[QUICKSLOT_HOLSTER] = GUN_KNIFE
		Players[playerID]\Ammo[Players[playerID]\SelectedSlot] = GetWeaponMaxCurrAmmo(Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot])
		Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot] = 45
	EndIf
	
	Players[playerID]\OverHereSprite = CreateSprite()
	ScaleSprite Players[playerID]\OverHereSprite,0.125,0.125
	EntityOrder Players[playerID]\OverHereSprite,-2
	EntityTexture Players[playerID]\OverHereSprite,mpl\WheelLookHereIcons[ICON_LOOK_DEFAULT]
	EntityFX Players[playerID]\OverHereSprite,1+8
	HideEntity Players[playerID]\OverHereSprite
	
	Players[playerID]\GunPivot = CreatePivot()
	
	CreatePlayerTag(playerID)
	
	HideEntity Players[playerID]\obj_lower
	HideEntity Players[playerID]\obj_upper
	HideEntity Players[playerID]\NameTag
	TeleportEntity Players[playerID]\Collider,0,-10000,0
	
	Players[playerID]\ForceTeam = Team_Unknown - 1
	
End Function

Function CreatePlayerTag(playerID%)
	CatchErrors("CreatePlayerTag(" + playerID + ")")
	
	Players[playerID]\NameTag = CreateSprite()
	SpriteViewMode Players[playerID]\NameTag,1
	SetFont fo\Font[Font_Default_Large]
	ScaleSprite Players[playerID]\NameTag,0.0006*(StringWidth(Players[playerID]\Name)),0.025
	EntityFX Players[playerID]\NameTag,1+8
	EntityOrder Players[playerID]\NameTag,-1
	Local tex% = CreateTexture(1024,64,256+4)
	SetBuffer TextureBuffer(tex)
	ClsColor 0,0,0
	Cls
	Color 255,255,255
	Text(1,2,Players[playerID]\Name,False,False)
	SetBuffer BackBuffer()
	MaskTexture(tex,0,0,0)
	ScaleTexture tex,1024.0/StringWidth(Players[playerID]\Name),1.4
	PositionTexture tex,0.0,0.01
	EntityTexture Players[playerID]\NameTag,tex
	DeleteSingleTextureEntryFromCache tex ;TODO I replaced this, because of similar coding causing hard to diagnose crashes with Debugger enabled, I have no idea how multiplayer works, if FreeTexture works fine here instead, revert this
	
	CatchErrors("Uncaught (CreatePlayerTag(" + playerID + "))")
End Function

Function SwitchPlayerGun(playerID%)
	CatchErrors("SwitchPlayerGun(" + playerID + ")")
	Local g.Guns
	Local VectorString$
	Local bonename$
	
	;CHECK FOR IMPLEMENTATION
	
	If Players[playerID]\GunModel <> 0 Then
		EntityParent Players[playerID]\GunModel,0
		Players[playerID]\GunModelMuzzleFlash = FreeEntity_Strict(Players[playerID]\GunModelMuzzleFlash)
		Players[playerID]\GunModel = FreeEntity_Strict(Players[playerID]\GunModel)
	EndIf
	
	Local prevYaw# = EntityYaw(Players[playerID]\obj_lower)
	Local prevX# = EntityX(Players[playerID]\obj_lower)
	Local prevY# = EntityY(Players[playerID]\obj_lower)
	Local prevZ# = EntityZ(Players[playerID]\obj_lower)
	
	RotateEntity Players[playerID]\obj_lower,0,0,0
	PositionEntity Players[playerID]\obj_lower,0,0,0
	
	Local bone = FindChild(Players[playerID]\obj_upper,GetINIString("Data\PlayerBones.ini", "Player", "weapon_hand_bonename"))
	
	Local gunname$ = ""
	For g = Each Guns
		If g\ID = Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot] Then
			Players[playerID]\GunModel = CopyEntity(g\PlayerModel,bone)
			gunname = g\name
			Exit
		EndIf
	Next
	
	If gunname <> "" Then
		Local scale# = GetINIFloat(gv\WeaponFile,gunname,"world scale",0.02) / (0.29 / 2.5)
		ScaleEntity Players[playerID]\GunModel,scale,scale,scale
		
		VectorString = GetINIString(gv\WeaponFile,gunname,"mp_player_model_offset","")
		If VectorString<>"" Then
			PositionEntity Players[playerID]\GunModel,Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|")
		EndIf
		VectorString = GetINIString(gv\WeaponFile,gunname,"mp_player_model_rotation","")
		If VectorString<>"" Then
			RotateEntity Players[playerID]\GunModel,Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|")
		EndIf
		
		bone = FindChild(Players[playerID]\GunModel,"Muzzle")
		Players[playerID]\GunModelMuzzleFlash = CopyEntity(mp_I\MuzzleFlash,bone)
		SpriteViewMode Players[playerID]\GunModelMuzzleFlash,3
		EntityFX Players[playerID]\GunModelMuzzleFlash,1
		HideEntity Players[playerID]\GunModelMuzzleFlash
		
		If playerID = mp_I\PlayerID Then
			HideEntity Players[playerID]\GunModel
		EndIf
	EndIf
	
	RotateEntity Players[playerID]\obj_lower,0,prevYaw,0
	PositionEntity Players[playerID]\obj_lower,prevX,prevY,prevZ
	
	If mp_I\PlayerID<>playerID Then
		If Players[playerID]\CurrHP > 0 Then
			bonename$ = "chest"
			If bonename <> "" Then
				bone% = FindChild(Players[playerID]\obj_upper,bonename$)
				If bone <> 0 Then
					RotateEntity bone%,EntityPitch(bone),-Players[playerID]\Pitch,EntityRoll(bone)
				EndIf
			EndIf
		EndIf
	EndIf
	
	CatchErrors("Uncaught (SwitchPlayerGun(" + playerID + "))")
End Function

Function MovePlayerServer()
	CatchErrors("Uncaught (MovePlayerServer)")
	Local Sprint# = 1.0, Speed# = 0.018, i%, angle#
	Local temp#
	
	If (Not MenuOpen) And (Not ConsoleOpen) And (Not InLobby()) And (Not mp_I\ChatOpen) And Players[mp_I\PlayerID]\CurrHP>0 And (Not mp_I\Gamemode\DisableMovement) And (Not IsModerationOpen()) And (Not IsInVote()) Then
		If KeyDown(KEY_SPRINT) And (Not KeyDown(KEY_CROUCH)) Then
			If Players[mp_I\PlayerID]\CurrStamina < 5 Then
				If ChannelPlaying(BreathCHN)=False Then BreathCHN = PlaySound_Strict(BreathSFX[0])
			ElseIf Players[mp_I\PlayerID]\CurrStamina < 50
				If BreathCHN=0 Then
					BreathCHN = PlaySound_Strict(BreathSFX[Rand(1,3)])
					ChannelVolume BreathCHN, Min((70.0-Players[mp_I\PlayerID]\CurrStamina)/70.0,1.0)*opt\SFXVolume
				Else
					If ChannelPlaying(BreathCHN)=False Then
						BreathCHN = PlaySound_Strict(BreathSFX[Rand(1,3)])
						ChannelVolume BreathCHN, Min((70.0-Players[mp_I\PlayerID]\CurrStamina)/70.0,1.0)*opt\SFXVolume			
					EndIf
				EndIf
			EndIf
			
			If Players[mp_I\PlayerID]\CurrStamina > 0 And (Not Players[mp_I\PlayerID]\Crouch) And (Players[mp_I\PlayerID]\IronSight = False) Then
				Sprint = 2.5
			EndIf
		EndIf
		
		Sprint = (Sprint / (1.0+Players[mp_I\PlayerID]\Crouch))
		
		;[CONTROLLER]
		If co\Enabled
			Local case1% = 0
			Local case2% = ((GetLeftAnalogStickPitch()<>0 Lor GetLeftAnalogStickYaw()<>0) And Playable)
		Else
			case1% = ((KeyDown(KEY_DOWN) Xor KeyDown(KEY_UP)) Lor (KeyDown(KEY_RIGHT) Xor KeyDown(KEY_LEFT)) And Playable)
			case2% = 0
		EndIf
		If case1% Lor case2% Then
			
			;[CONTROLLER]
			Local SprintKeyAssigned = False
			If (Not co\Enabled) Then
				If KeyDown(KEY_SPRINT) Then SprintKeyAssigned = True
			Else
				If Players[mp_I\PlayerID]\IsPlayerSprinting Then
					If JoyHit(CK_Sprint)
						SprintKeyAssigned = 0
					Else
						SprintKeyAssigned = 1
					EndIf
				Else
					If JoyHit(CK_Sprint)
						SprintKeyAssigned = 1
					Else
						SprintKeyAssigned = 0
					EndIf
				EndIf
			EndIf
			
			If SprintKeyAssigned Then
				Players[mp_I\PlayerID]\PressSprint = True
			Else
				Players[mp_I\PlayerID]\PressSprint = False
			EndIf
			
			temp# = (Shake Mod 360)
			Local tempchn%
			If (Not UnableToMove%) Then Shake# = (Shake + FPSfactor * Min(Sprint, 1.7) * 10) Mod 720
			If temp < 180 And (Shake Mod 360) >= 180 And KillTimer>=0 Then
				If CurrStepSFX=0 Then
					temp = GetStepSound(Players[mp_I\PlayerID]\Collider)
					If Sprint = 1.0 Then
						PlayerSoundVolume = Max(4.0,PlayerSoundVolume)
						tempchn% = PlaySound_Strict(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
						ChannelVolume tempchn, (1.0-(Players[mp_I\PlayerID]\Crouch*0.6))*(opt\SFXVolume#*opt\MasterVol)
					Else
						PlayerSoundVolume = Max(2.5-(Players[mp_I\PlayerID]\Crouch*0.6),PlayerSoundVolume)
						tempchn% = PlaySound_Strict(mpl\StepSoundRun[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
						ChannelVolume tempchn, (1.0-(Players[mp_I\PlayerID]\Crouch*0.6))*(opt\SFXVolume#*opt\MasterVol)
					EndIf
				EndIf
			EndIf
		EndIf
		
		;[CONTROLLER]
		If opt\HoldToCrouch Then
			If Playable Then
				If (Not co\Enabled) Then
					Players[mp_I\PlayerID]\Crouch = KeyDown(KEY_CROUCH)
				Else
					Players[mp_I\PlayerID]\Crouch = JoyDown(CK_Crouch)
				EndIf
			EndIf
		Else
			If (Not co\Enabled) Then
				If KeyHit(KEY_CROUCH) And Playable Then Players[mp_I\PlayerID]\Crouch = (Not Players[mp_I\PlayerID]\Crouch)
			Else
				If JoyHit(CK_Crouch) And Playable Then Players[mp_I\PlayerID]\Crouch = (Not Players[mp_I\PlayerID]\Crouch)
			EndIf
		EndIf
		
		temp = False
		If Playable Then
			If (Not co\Enabled)
				If KeyDown(KEY_DOWN) And (Not KeyDown(KEY_UP)) Then
					temp = True
					angle = 180
					If KeyDown(KEY_LEFT) And (Not KeyDown(KEY_RIGHT)) Then angle = 135 
					If KeyDown(KEY_RIGHT) And (Not KeyDown(KEY_LEFT)) Then angle = -135
				ElseIf KeyDown(KEY_UP) And (Not KeyDown(KEY_DOWN)) Then
					temp = True
					angle = 0
					If KeyDown(KEY_LEFT) And (Not KeyDown(KEY_RIGHT)) Then angle = 45
					If KeyDown(KEY_RIGHT) And (Not KeyDown(KEY_LEFT)) Then angle = -45
				Else
					If KeyDown(KEY_LEFT) And (Not KeyDown(KEY_RIGHT)) Then angle = 90 : temp = True
					If KeyDown(KEY_RIGHT) And (Not KeyDown(KEY_LEFT)) Then angle = -90 : temp = True 
				EndIf
			Else
				;[CONTROLLER]
				If GetLeftAnalogStickPitch()<0.0
					temp = True
					angle = 180
					If GetLeftAnalogStickYaw(True)<>0.0
						angle = GetLeftAnalogStickYaw(True,True)*(180.0-(45.0*Abs(GetLeftAnalogStickYaw())))
					EndIf
				ElseIf GetLeftAnalogStickPitch()>0.0
					temp = True
					angle = 0
					If GetLeftAnalogStickYaw(True)<>0.0
						angle = GetLeftAnalogStickYaw(True,True)*(45.0*Abs(GetLeftAnalogStickYaw()))
					EndIf
				Else
					If GetLeftAnalogStickYaw(True)<>0.0
						angle = GetLeftAnalogStickYaw(True,True)*90.0
						temp = True
					EndIf
				EndIf
			EndIf
		EndIf
		
		If temp Then
			Players[mp_I\PlayerID]\walking = True
			Players[mp_I\PlayerID]\walkangle = angle
		Else
			Players[mp_I\PlayerID]\walking = False
		EndIf
	Else
		Players[mp_I\PlayerID]\walking = False
		Players[mp_I\PlayerID]\PressSprint = False
		Players[mp_I\PlayerID]\walkangle = 0
	EndIf
	
	CatchErrors("MovePlayerServer")
End Function

Function UpdatePlayerPositionsServer()
	CatchErrors("UpdatePlayerPositionsServer")
	Local i%
	
	For i = 0 To (mp_I\MaxPlayers-1)
		If Players[i]<>Null Then
			If (Not IsSpectator(i)) Then ;CHECK FOR IMPLEMENTATION
				If Players[i]\StaminaEffectTimer > 0 Then
					Players[i]\CurrStamina = Min(Players[i]\CurrStamina + FPSfactor, 100.0)
				Else
					If Players[i]\CurrSpeed > 0 Then
						Players[i]\CurrStamina = Min(Players[i]\CurrStamina + 0.15 * FPSfactor/1.25, 100.0)
					Else
						Players[i]\CurrStamina = Min(Players[i]\CurrStamina + 0.15 * FPSfactor*1.25, 100.0)
					EndIf
				EndIf	
				
				Players[i]\StaminaEffectTimer = Max(0,Players[i]\StaminaEffectTimer - FPSfactor)
				
				UpdatePlayerPosition(i)
				
				If i<>mp_I\PlayerID Then
					Local campitch# = Players[i]\Pitch+180
					Local camyaw# = Players[i]\Yaw
					Local gpivotpitch# = EntityPitch(Players[i]\GunPivot)+180
					Local gpivotyaw# = EntityYaw(Players[i]\GunPivot)
					Local pitch# = Clamp(CurveAngle(campitch, gpivotpitch, 10.0), campitch-2.5, campitch+2.5)
					Local yaw# = CurveAngle(camyaw, gpivotyaw, 10.0)
					
					yaw = ClampAngle(yaw, camyaw, 2.5)
					
					RotateEntity Players[i]\GunPivot,pitch-180,yaw,0
				EndIf
			EndIf
		EndIf
	Next
	
	CatchErrors("Uncaught (UpdatePlayerPositionsServer)")
End Function

Function UpdatePlayerPosition(playerID%)
	CatchErrors("UpdatePlayerPosition (" + playerID + ")")
	Local Sprint# = 1.0, Speed# = 0.018, angle#, j%
	Local temp#
	
	If Abs(Players[playerID]\CrouchState-Players[playerID]\Crouch)<0.001 Then 
		Players[playerID]\CrouchState = Players[playerID]\Crouch
	Else
		Players[playerID]\CrouchState = CurveValue(Players[playerID]\Crouch, Players[playerID]\CrouchState, 10.0)
	EndIf
	
	If Players[playerID]\walking Then
		
		Players[playerID]\IsPlayerSprinting% = False
		
		If Players[playerID]\Crouch = 0 And (Players[playerID]\PressSprint) And Players[playerID]\CurrStamina > 0.0 And Players[playerID]\IronSight = 0 Then
			Sprint = 2.5
			Players[playerID]\IsPlayerSprinting% = True
			Players[playerID]\CurrStamina = Players[playerID]\CurrStamina - FPSfactor * 0.25
			If Players[playerID]\CurrStamina <= 0 Then Players[playerID]\CurrStamina = -20.0
		EndIf
	Else
		Players[playerID]\IsPlayerSprinting% = False
	EndIf
	
	Local temp2# = (Speed * Sprint) / (1.0+Players[playerID]\CrouchState)
	
	temp = False
	If Players[playerID]\walking Then
		temp = True
		angle = Players[playerID]\walkangle
	EndIf
	
	angle = WrapAngle(Players[playerID]\Yaw+angle+90.0)
	
	If temp Then 
		Players[playerID]\CurrSpeed = CurveValue(temp2, Players[playerID]\CurrSpeed, 20.0)
	Else
		Players[playerID]\CurrSpeed = Max(CurveValue(0.0, Players[playerID]\CurrSpeed-0.1, 1.0),0.0)
	EndIf
	
	TranslateEntity Players[playerID]\Collider, Cos(angle)*Players[playerID]\CurrSpeed * FPSfactor, 0, Sin(angle)*Players[playerID]\CurrSpeed * FPSfactor, True
	
	Local CollidedFloor% = False
	For j = 1 To CountCollisions(Players[playerID]\Collider)
		If CollisionY(Players[playerID]\Collider, j) < EntityY(Players[playerID]\Collider) - 0.25 Then CollidedFloor = True
	Next
	
	If CollidedFloor = True Then
		If Players[playerID]\DropSpeed# < - 0.07 Then 
			If CurrStepSFX=0 Then
				PlaySound_Strict(StepSFX(GetStepSound(Players[playerID]\Collider), 0, Rand(0, 7)))
			EndIf
			PlayerSoundVolume = Max(3.0,PlayerSoundVolume)
		EndIf
		Players[playerID]\DropSpeed# = 0
	Else
		Players[playerID]\DropSpeed# = Min(Max(Players[playerID]\DropSpeed - 0.006 * FPSfactor, -2.0), 0.0)
	EndIf
	PlayerFallingPickDistance# = 10.0
	
	TranslateEntity Players[playerID]\Collider, 0, Players[playerID]\DropSpeed * FPSfactor, 0
	
	UpdatePlayerUtils(playerID)
	PositionEntity Players[playerID]\GunPivot,Players[playerID]\X,Players[playerID]\Y+0.6+(Players[playerID]\CrouchState*-0.3),Players[playerID]\Z,True
	
	CatchErrors("Uncaught (UpdatePlayerPosition (" + playerID + "))")
End Function

Function UpdateSpectatorPosition(playerID%)
	Local temp#, speed# = 0.018
	
	If (Not co\Enabled)
		If (KeyDown(KEY_SPRINT)) Then 
			speed = speed * 2.5
		ElseIf KeyDown(KEY_CROUCH)
			speed = speed * 0.5
		EndIf
	Else
		If Players[playerID]\IsPlayerSprinting
			If JoyDown(CK_Sprint)
				speed = speed * 2.5
			ElseIf JoyDown(CK_Crouch)
				speed = speed * 0.5
			EndIf
		EndIf
	EndIf
	
	RotateEntity Players[playerID]\Collider, WrapAngle(EntityPitch(mpl\CameraPivot)), WrapAngle(EntityYaw(mpl\CameraPivot)), 0
	
	temp = speed * NoClipSpeed
	
	;[CONTROLLER]
	If (Not co\Enabled)
		If KeyDown(KEY_DOWN) Then MoveEntity Players[playerID]\Collider, 0, 0, -temp*FPSfactor
		If KeyDown(KEY_UP) Then MoveEntity Players[playerID]\Collider, 0, 0, temp*FPSfactor
		
		If KeyDown(KEY_LEFT) Then MoveEntity Players[playerID]\Collider, -temp*FPSfactor, 0, 0
		If KeyDown(KEY_RIGHT) Then MoveEntity Players[playerID]\Collider, temp*FPSfactor, 0, 0
	Else
		If GetLeftAnalogStickPitch()<0.0
			MoveEntity Players[playerID]\Collider, 0, 0, -temp*FPSfactor
		EndIf
		If GetLeftAnalogStickPitch()>0.0
			MoveEntity Players[playerID]\Collider, 0, 0, temp*FPSfactor
		EndIf
		If GetLeftAnalogStickYaw(True)<0.0
			MoveEntity Players[playerID]\Collider, -temp*FPSfactor, 0, 0
		EndIf
		If GetLeftAnalogStickYaw(True)>0.0
			MoveEntity Players[playerID]\Collider, temp*FPSfactor, 0, 0
		EndIf
	EndIf
	
	ResetEntity Players[playerID]\Collider
	
End Function

Function UpdatePlayerUtils(playerID%)
	
	If Players[playerID]\obj_lower = 0 Then
		Return
	EndIf
	
	PositionEntity Players[playerID]\obj_lower,Players[playerID]\X,Players[playerID]\Y+0.1,Players[playerID]\Z,True
	If Players[playerID]\CurrHP > 0 Then
		RotateEntity Players[playerID]\obj_lower,0,Players[playerID]\Yaw,0,True
	EndIf
	If playerID	<> mp_I\PlayerID Then
		If HUDenabled And Players[playerID]\Team = Players[mp_I\PlayerID]\Team And (Not IsSpectator(playerID)) Then ;CHECK FOR IMPLEMENTATION
			PositionEntity Players[playerID]\NameTag,Players[playerID]\X,Players[playerID]\Y+0.8+(Players[playerID]\CrouchState*-0.3),Players[playerID]\Z,True
			ShowEntity Players[playerID]\NameTag
			If EntityVisible(Camera,Players[playerID]\Collider) Then
				EntityAlpha(Players[playerID]\NameTag,1.0)
			Else	
				EntityAlpha(Players[playerID]\NameTag,0.5)
			EndIf	
		Else
			HideEntity Players[playerID]\NameTag
		EndIf
	Else
		HideEntity Players[playerID]\NameTag
	EndIf
	
End Function

Function GetPlayerPositions()
	Local i%
	
	For i = 0 To (mp_I\MaxPlayers-1)
		If Players[i]<>Null Then
			Players[i]\X = EntityX(Players[i]\Collider)
			Players[i]\Y = EntityY(Players[i]\Collider)
			Players[i]\Z = EntityZ(Players[i]\Collider)
		EndIf
	Next
	
End Function

Function MouseLookServer()
	Local i%
	Local g.Guns,currGun.Guns
	
	CameraShake = Max(CameraShake - (FPSfactor / 10), 0)
	
	For g.Guns = Each Guns
		If g\ID = g_I\HoldingGun Then
			If g\GunType<>GUNTYPE_MELEE Then
				currGun = g
			EndIf
			Exit
		EndIf
	Next
	
	Local IronSight_AddFOV# = 0.0
	If currGun <> Null Then
		If KeyDown(KEY_SPRINT) And g_I\IronSight Then
			IronSight_AddFOV = Abs(EntityX(IronSightPivot2%)/currGun\IronSightCoords\x)*0.8
		Else
			IronSight_AddFOV = Abs(EntityX(IronSightPivot2%)/currGun\IronSightCoords\x)*0.5
		EndIf
	EndIf
	
	CameraZoom(Camera, (Min(1.0+(CurrCameraZoom/400.0),1.1) + IronSight_AddFOV) / (Tan((2*ATan(Tan(Float(FOV)/2)*(Float(RealGraphicWidth)/Float(RealGraphicHeight))))/2.0)))
	CurrCameraZoom = Max(CurrCameraZoom - FPSfactor, 0)
	
;	If IsNaN(EntityX(Players(mp_I\PlayerID)\Collider)) Then
;		
;		PositionEntity Players(mp_I\PlayerID)\Collider, EntityX(Camera, True), EntityY(Camera, True) - 0.5, EntityZ(Camera, True), True
;		CreateMSG("EntityX(Players(mp_I\PlayerID)\Collider) = NaN, RESETTING COORDINATES    -    New coordinates: "+EntityX(Players(mp_I\PlayerID)\Collider)
;		m_msg\Timer = 300
;	EndIf
	
	Local up# = (Sin(Shake) / (20.0+Players[mp_I\PlayerID]\CrouchState*20.0))*0.6	
	Local roll# = Max(Min(Sin(Shake/2)*0.625,8.0),-8.0)
	
	;PositionEntity Camera, EntityX(Players[mp_I\PlayerID]\Collider),EntityY(Players[mp_I\PlayerID]\Collider),EntityZ(Players[mp_I\PlayerID]\Collider)
	;RotateEntity Camera, 0, EntityYaw(Players[mp_I\PlayerID]\Collider), (roll*0.5)*0.5
	;
	;MoveEntity Camera, side, (up*0.5) + 0.6 + Players[mp_I\PlayerID]\CrouchState * -0.3, 0
	
	If (Players[mp_I\PlayerID]\CurrHP > 0) Lor (mp_I\SpectatePlayer = -1) Then
		PositionEntity mpl\CameraPivot, EntityX(Players[mp_I\PlayerID]\Collider),EntityY(Players[mp_I\PlayerID]\Collider),EntityZ(Players[mp_I\PlayerID]\Collider)
		RotateEntity mpl\CameraPivot, 0, EntityYaw(Players[mp_I\PlayerID]\Collider), (roll*0.5)*0.5
		MoveEntity mpl\CameraPivot, side, (up*0.5) + 0.6 + Players[mp_I\PlayerID]\CrouchState * -0.3, 0
	Else
		PositionEntity mpl\CameraPivot, EntityX(Players[mp_I\SpectatePlayer]\Collider),EntityY(Players[mp_I\SpectatePlayer]\Collider)+0.5,EntityZ(Players[mp_I\SpectatePlayer]\Collider)
		RotateEntity mpl\CameraPivot, 0, EntityYaw(Players[mp_I\PlayerID]\Collider), 0
	EndIf
	
	If (Not MenuOpen) And (Not ConsoleOpen) And (Not InLobby()) And (Not mp_I\ChatOpen) And (mpl\WheelOpened=WHEEL_CLOSED) And (Not IsPlayerListOpen()) And (Not IsModerationOpen()) And (Not IsInVote()) Then ;And (Not Players[mp_I\PlayerID]\Disable) Then
		; -- Update the smoothing que To smooth the movement of the mouse.
		If (Not co\Enabled) Then
			Mouse_X_Speed_1# = CurveValue(MouseXSpeed() * (MouseSens + 0.6) , Mouse_X_Speed_1, (6.0 / (MouseSens + 1.0))*opt\MouseSmooth)
		Else
			;[CONTROLLER]
			If GetRightAnalogStickYaw(True)<>0.0 Then
				mouse_x_speed_1# = CurveValue(GetRightAnalogStickYaw() * ((co\Sensitivity+0.6)*10*FPSfactor), mouse_x_speed_1, 6.0 / ((co\Sensitivity+1.0)*10*FPSfactor))
			Else
				mouse_x_speed_1# = CurveValue(0.0, mouse_x_speed_1, 6.0 / ((co\Sensitivity+1.0)*10*FPSfactor))
			EndIf
		EndIf
		If IsNaN(Mouse_X_Speed_1) Then Mouse_X_Speed_1 = 0
		
		If (Not co\Enabled) Then
			If InvertMouse Then
				Mouse_Y_Speed_1# = CurveValue(-MouseYSpeed() * (MouseSens + 0.6), Mouse_Y_Speed_1, (6.0/(MouseSens+1.0))*opt\MouseSmooth) 
			Else
				Mouse_Y_Speed_1# = CurveValue(MouseYSpeed () * (MouseSens + 0.6), Mouse_Y_Speed_1, (6.0/(MouseSens+1.0))*opt\MouseSmooth) 
			EndIf
		Else
			;[CONTROLLER]
			If Int(GetRightAnalogStickPitch(True))<>0 Then
				Mouse_Y_Speed_1# = CurveValue(GetRightAnalogStickPitch(False,InvertMouse) * ((co\Sensitivity+0.6)*10*FPSfactor), Mouse_Y_Speed_1, 6.0/((co\Sensitivity+1.0)*10*FPSfactor))
			Else
				Mouse_Y_Speed_1# = CurveValue(0.0, Mouse_Y_Speed_1, 6.0/((co\Sensitivity+1.0)*10*FPSfactor))
			EndIf
		EndIf
		If IsNaN(Mouse_Y_Speed_1) Then Mouse_Y_Speed_1 = 0
		
		Local the_yaw# = ((Mouse_X_Speed_1#)) * MouseLook_X_Inc#
		Local the_pitch# = ((Mouse_Y_Speed_1#)) * MouseLook_Y_Inc#
		
		TurnEntity Players[mp_I\PlayerID]\Collider, 0.0, -the_yaw#, 0.0 ; Turn the user on the Y (yaw) axis.
		
		user_camera_pitch# = user_camera_pitch# + the_pitch#
		; -- Limit the user;s camera To within 180 degrees of pitch rotation. ;EntityPitch(); returns useless values so we need To use a variable To keep track of the camera pitch.
		If user_camera_pitch# > 70.0 Then user_camera_pitch# = 70.0
		If user_camera_pitch# < - 70.0 Then user_camera_pitch# = -70.0
	EndIf
	
	;RotateEntity Camera, WrapAngle(user_camera_pitch + Rnd(-CameraShake, CameraShake)),WrapAngle(EntityYaw(Players[mp_I\PlayerID]\Collider) + Rnd(-CameraShake, CameraShake)),roll ; Pitch the user;s camera up And down.
	RotateEntity mpl\CameraPivot, WrapAngle(user_camera_pitch),WrapAngle(EntityYaw(Players[mp_I\PlayerID]\Collider)),roll
	RotateEntity Camera,Rnd(-CameraShake, CameraShake),Rnd(-CameraShake, CameraShake),0.0
	
	If ParticleAmount=2 Then
		If Rand(35) = 1 Then
			Local pvt% = CreatePivot()
			PositionEntity(pvt, EntityX(mpl\CameraPivot, True), EntityY(mpl\CameraPivot, True), EntityZ(mpl\CameraPivot, True))
			RotateEntity(pvt, 0, Rnd(360), 0)
			If Rand(2) = 1 Then
				MoveEntity(pvt, 0, Rnd(-0.5, 0.5), Rnd(0.5, 1.0))
			Else
				MoveEntity(pvt, 0, Rnd(-0.5, 0.5), Rnd(0.5, 1.0))
			EndIf
			
			Local p.Particles = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 2, 0.002, 0, 300)
			p\speed = 0.001
			RotateEntity(p\pvt, Rnd(-20, 20), Rnd(360), 0)
			
			p\SizeChange = -0.00001
			
			pvt = FreeEntity_Strict(pvt)
		EndIf
	EndIf
	
	; -- Limit the mouse;s movement. Using this method produces smoother mouselook movement than centering the mouse Each loop.
	If (Not MenuOpen) And (Not ConsoleOpen) And (Not InLobby()) And (Not mp_I\ChatOpen) And (Not IsPlayerListOpen()) And (Not IsModerationOpen()) And (Not IsInVote()) Then
		If (MousePosX > Mouse_Right_Limit) Lor (MousePosX < Mouse_Left_Limit) Lor (MousePosY > Mouse_Bottom_Limit) Lor (MousePosY < Mouse_Top_Limit) Then
			MoveMouse Viewport_Center_X, Viewport_Center_Y
		EndIf
	EndIf
	
End Function

Function AnimatePlayerModelsAndSpectate()
	CatchErrors("AnimatePlayerModelsAndSpectate")
	
	Local i%,prevFrame#,j%,bone%
	Local animspeed_multiply# = 1.0
	Local prevPlayerAnimationLower%, prevPlayerAnimationUpper%
	Local forceUpperRestart%
	Local g.Guns, it.Items
	
	For i = 0 To (mp_I\MaxPlayers-1)
		If Players[i]<>Null Then
			animspeed_multiply# = 1.0
			;Will be required for animation transitioning!
			prevPlayerAnimationLower = Players[i]\AnimState_Lower
			prevPlayerAnimationUpper = Players[i]\AnimState_Upper
			forceUpperRestart = False
			
			If Players[i]\CurrHP <= 0 Then ;CHECK FOR IMPLEMENTATION
				If (Not IsSpectator(i) And Players[i]\Team > Team_Unknown) Then
					If Players[i]\GunModelMuzzleFlash <> 0 Then
						HideEntity Players[i]\GunModelMuzzleFlash
					EndIf
					Players[i]\AnimState_Lower = STATE_BODY_LOWER_DIE
					For g = Each Guns
						If g\ID = Players[i]\WeaponInSlot[Players[i]\SelectedSlot] Then
							Select g\PlayerModelAnim
								Case GUNANIM_SMG
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_SMG_DIE
								Case GUNANIM_RIFLE
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_RIFLE_DIE
								Case GUNANIM_PISTOL
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_PISTOL_DIE
								Case GUNANIM_MP5K
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_MP5K_DIE
								Case GUNANIM_SHOTGUN
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_SHOTGUN_DIE
								Case GUNANIM_MELEE
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_MELEE_DIE
							End Select
							If Players[i]\GunModel<>0 Then
								If (Not Animating(Players[i]\obj_upper)) Then
									If (Not EntityHidden(Players[i]\GunModel)) Then
										HideEntity Players[i]\GunModel
										If mp_I\PlayState = GAME_SERVER Then
											bone% = FindChild(Players[i]\obj_upper,"hand_R")
											it = CreateItem(g\DisplayName,g\name,EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
											RotateEntity it\collider, 180, EntityYaw(bone%,True)-90, 0
											If g\GunType <> GUNTYPE_MELEE Then
												it\state = Players[i]\Ammo[Players[i]\SelectedSlot]
											EndIf
											EntityType it\collider, HIT_ITEM
											it\Dropped = 1
										EndIf
										If Players[i]\Item <> Null And Players[i]\Item\itemtemplate\tempname = "fuse" Then
											If mp_I\PlayState = GAME_SERVER Then
												Delete Players[i]\Item
												bone% = FindChild(Players[i]\obj_upper,"hand_L")
												it = CreateItem(GetLocalString("Item Names","fuse"),"fuse",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
												EntityType it\collider, HIT_ITEM
												it\Dropped = 1
											Else
												Players[i]\Item\isDeleted = True
											EndIf
										EndIf
									EndIf
								Else
									ShowEntity Players[i]\GunModel
								EndIf
							EndIf	
							Exit
						EndIf
					Next
				EndIf
				
				Players[i]\Crouch = 0
				Players[i]\CrouchState = 0.0
				
				If mp_I\DeathChunk = -1 Then
					mp_I\DeathChunk = mp_I\Map\CurrChunk
				EndIf
				
				If ChannelPlaying(Players[i]\Sound_CHN) Then
					StopChannel(Players[i]\Sound_CHN)
				EndIf
				
				Players[i]\IronSight = False
				If i = mp_I\PlayerID Then
					;CHECK FOR IMPLEMENTATION
					EntityAlpha Players[i]\obj_lower,1.0
					EntityAlpha Players[i]\obj_upper,1.0
					If (EntityHidden(Players[i]\obj_lower)) Then
						ShowEntity Players[i]\obj_lower
						ShowEntity Players[i]\obj_upper
					EndIf
					
					TurnEntity mpl\CameraPivot,0,180,0
					Local pick = EntityPick(mpl\CameraPivot,3.0)
					Local pivot = CreatePivot()
					If PickedEntity() <> 0 Then
						PositionEntity pivot,PickedX(),PickedY(),PickedZ(),True
						PositionEntity Camera,0,0,-Min(Max(EntityDistance(mpl\CameraPivot,pivot)-0.5,0.0),2.0)
					Else
						PositionEntity Camera,0,0,-2.0
					EndIf
					TurnEntity mpl\CameraPivot,0,-180,0
					pivot = FreeEntity_Strict(pivot)
					
					HideEntity g_I\GunPivot
					
					g_I\IronSight = False
					
					If mp_I\SpectatePlayer = -1 Then
						mp_I\SpectatePlayer = mp_I\PlayerID
					EndIf
					
					If (Not MenuOpen) And (Not ConsoleOpen) And (Not InLobby()) And (Not IsModerationOpen()) And (Not IsInVote()) Then
						If MouseHit1 Then
							FindPlayerToSpectate(1)
						EndIf
						If MouseHit2 Then
							FindPlayerToSpectate(-1)
						EndIf
					EndIf
					mp_I\Gamemode\CanSurviveAllWaves = False
				EndIf
			Else
				;Upper body animation
				For g = Each Guns
					If g\ID = Players[i]\WeaponInSlot[Players[i]\SelectedSlot] Then
						If Players[i]\GunModelMuzzleFlash<>0 Then
							HideEntity Players[i]\GunModelMuzzleFlash
						EndIf
						If Players[i]\ReloadState > 0.0 Then
							Select g\PlayerModelAnim
								Case GUNANIM_SMG
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_SMG_RELOAD
								Case GUNANIM_RIFLE
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_RIFLE_RELOAD
								Case GUNANIM_PISTOL
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_PISTOL_RELOAD
								Case GUNANIM_MP5K
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_MP5K_RELOAD
								Case GUNANIM_SHOTGUN
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_SHOTGUN_RELOAD
							End Select
							If i<>mp_I\PlayerID Then
								If Players[i]\AnimState_Upper <> prevPlayerAnimationUpper Then
									If g\MaxReloadSounds = 1 Then
										Players[i]\GunSFXChannel[GUN_CHANNEL_OTHER] = PlaySound2(LoadTempSound("SFX\Guns\"+g\name$+"\reload.ogg"),Camera,Players[i]\Collider,20)
									Else
										Players[i]\GunSFXChannel[GUN_CHANNEL_OTHER] = PlaySound2(LoadTempSound("SFX\Guns\"+g\name$+"\reload"+Rand(1,g\MaxReloadSounds)+".ogg"),Camera,Players[i]\Collider,20)
									EndIf
								EndIf
							EndIf
						ElseIf Players[i]\ShootState = 0.0 Then
							Select g\PlayerModelAnim
								Case GUNANIM_SMG
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_SMG_IDLE
								Case GUNANIM_RIFLE
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_RIFLE_IDLE
								Case GUNANIM_PISTOL
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_PISTOL_IDLE
								Case GUNANIM_MP5K
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_MP5K_IDLE
								Case GUNANIM_SHOTGUN
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_SHOTGUN_IDLE
								Case GUNANIM_MELEE
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_MELEE_IDLE
							End Select
						Else
							Select g\PlayerModelAnim
								Case GUNANIM_SMG
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_SMG_SHOT
								Case GUNANIM_RIFLE
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_RIFLE_SHOT
								Case GUNANIM_PISTOL
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_PISTOL_SHOT
								Case GUNANIM_MP5K
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_MP5K_SHOT
								Case GUNANIM_SHOTGUN
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_SHOTGUN_SHOT
								Case GUNANIM_MELEE
									Players[i]\AnimState_Upper = STATE_BODY_UPPER_MELEE_STAB
							End Select
							If Players[i]\ShootState > 0.0 And (Players[i]\PrevShootState = 0.0 Lor Players[i]\PrevShootState > Players[i]\ShootState) Then
								forceUpperRestart = True
								If i<>mp_I\PlayerID Then
									If Players[i]\PressMouse1 Then
										If Players[i]\ShootState > 0.0 And (Players[i]\PrevShootState = 0.0 Lor Players[i]\PrevShootState > Players[i]\ShootState) Then
											If (Players[i]\SelectedSlot < (MaxSlots-SlotsWithNoAmmo) And Players[i]\Ammo[Players[i]\SelectedSlot] > 0) Then
												Players[i]\GunSFXChannel[GUN_CHANNEL_SHOT] = PlaySound2(g\ShootSounds[Rand(0,g\MaxShootSounds-1)],Camera,Players[i]\Collider,20)
												If mp_I\PlayState = GAME_CLIENT Then
													Players[i]\PressMouse1 = False
												EndIf
												ShowEntity Players[i]\GunModelMuzzleFlash
												ScaleSprite Players[i]\GunModelMuzzleFlash,Rnd(0.125,0.15),Rnd(0.125,0.15)
												TurnEntity Players[i]\GunModelMuzzleFlash,0,0,Rnd(360)
											ElseIf Players[i]\SelectedSlot >= (MaxSlots-SlotsWithNoAmmo) Then
												Players[i]\GunSFXChannel[GUN_CHANNEL_OTHER] = PlaySound2(LoadTempSound("SFX\Guns\"+g\name$+"\miss.ogg"),Camera,Players[i]\Collider,5)
												If mp_I\PlayState = GAME_CLIENT Then
													Players[i]\PressMouse1 = False
												EndIf
											EndIf
										EndIf
									EndIf
								EndIf
							EndIf
						EndIf
						For j = 0 To MaxGunChannels-1
							UpdateSoundOrigin(Players[i]\GunSFXChannel[j],Camera,Players[i]\Collider,5 + (15*(Players[i]\SelectedSlot < (MaxSlots-SlotsWithNoAmmo))))
						Next
						Exit
					EndIf
				Next
				
				;Lower body animation
				If Players[i]\CurrSpeed > 0.0 Then
					Players[i]\walkangle = WrapAngle(Players[i]\walkangle)
					;Maybe some rotation stuff needs to be reworked
					If Players[i]\IsPlayerSprinting Then
						;Temporary
						Players[i]\AnimState_Lower = STATE_BODY_LOWER_RUN
					ElseIf Players[i]\Crouch = False Then
						If Players[i]\walkangle <= 45 Lor Players[i]\walkangle >= 315 Then
							Players[i]\AnimState_Lower = STATE_BODY_LOWER_WALK_FORWARD
						ElseIf Players[i]\walkangle > 45 And Players[i]\walkangle <= 135 Then
							Players[i]\AnimState_Lower = STATE_BODY_LOWER_WALK_LEFT
						Else If Players[i]\walkangle >= 255 And Players[i]\walkangle < 315 Then
							Players[i]\AnimState_Lower = STATE_BODY_LOWER_WALK_RIGHT
						Else
							Players[i]\AnimState_Lower = STATE_BODY_LOWER_WALK_FORWARD
							animspeed_multiply# = -1.0
						EndIf
					Else
						If Players[i]\walkangle <= 45 Lor Players[i]\walkangle >= 315 Then
							Players[i]\AnimState_Lower = STATE_BODY_LOWER_CROUCH_FORWARD
						ElseIf Players[i]\walkangle > 45 And Players[i]\walkangle <= 135 Then
							Players[i]\AnimState_Lower = STATE_BODY_LOWER_CROUCH_LEFT
						Else If Players[i]\walkangle >= 255 And Players[i]\walkangle < 315 Then
							Players[i]\AnimState_Lower = STATE_BODY_LOWER_CROUCH_RIGHT
						Else
							Players[i]\AnimState_Lower = STATE_BODY_LOWER_CROUCH_FORWARD
							animspeed_multiply# = -1.0
						EndIf
					EndIf
					
					;Stepsounds will come into the animation function itself
;					If mp_I\PlayerID<>i Then
;						If prevFrame < 506 And AnimTime(Players[i]\obj_lower) >= 506 Then
;							PlaySound2(StepSFX(GetStepSound(Players[i]\Collider),0,Rand(0,7)),Camera,Players[i]\Collider,5,1.0)
;						ElseIf prevFrame => 521 And AnimTime(Players[i]\obj_lower) < 489 Then
;							PlaySound2(StepSFX(GetStepSound(Players[i]\Collider),0,Rand(0,7)),Camera,Players[i]\Collider,5,1.0)
;						EndIf
;					EndIf
				Else
					If Players[i]\Crouch = False Then
						Players[i]\AnimState_Lower = STATE_BODY_LOWER_IDLE
					Else
						Players[i]\AnimState_Lower = STATE_BODY_LOWER_CROUCH_IDLE
					EndIf
				EndIf
				
				If i = mp_I\PlayerID Then
					;CHECK FOR IMPLEMENTATION
					EntityAlpha Players[i]\obj_lower,0.0
					EntityAlpha Players[i]\obj_upper,0.0
					PositionEntity Camera,0,0,0
					mp_I\SpectatePlayer = -1
					If Players[i]\GunModel <> 0 Then
						HideEntity Players[i]\GunModel
					EndIf
					If mp_I\Gamemode\ID = Gamemode_Deathmatch And mp_I\Gamemode\Phase > Deathmatch_Game Then
						If Players[i]\CurrHP < 5.0 Then
							Steam_Achieve(ACHV_TIS_BUT_A_SCRATCH)
						EndIf
					EndIf
				EndIf
			EndIf
			
			If (Not IsSpectator(i)) And (Players[i]\Team > Team_Unknown) Then ;CHECK FOR IMPLEMENTATION
				If (Players[i]\obj_lower <> 0 And EntityHidden(Players[i]\obj_lower)) Then
					ShowEntity Players[i]\obj_lower
					ShowEntity Players[i]\obj_upper
					If Players[i]\GunModel <> 0 Then
						ShowEntity Players[i]\GunModel
					EndIf
					If Players[i]\GunModelMuzzleFlash <> 0 Then
						ShowEntity Players[i]\GunModelMuzzleFlash
					EndIf
				EndIf
				If Players[i]\AnimState_Lower <> prevPlayerAnimationLower Then
					AnimatePlayerModelLow(Players[i]\obj_lower, Players[i]\AnimState_Lower, animspeed_multiply)
				EndIf
				
				If Players[i]\AnimState_Upper <> prevPlayerAnimationUpper Lor forceUpperRestart Then
					AnimatePlayerModelUp(Players[i]\obj_upper, Players[i]\AnimState_Upper)
				EndIf
				
				If i <> mp_I\PlayerID Then
					PlayPlayerStepSounds(i, Players[i]\obj_lower, Players[i]\AnimState_Lower)
				EndIf
			Else
				If Players[i]\obj_lower <> 0 And (Not EntityHidden(Players[i]\obj_lower)) Then
					TeleportEntity Players[i]\Collider,0,-10000,0
					HideEntity Players[i]\obj_lower
					HideEntity Players[i]\obj_upper
					If Players[i]\GunModel <> 0 Then
						HideEntity Players[i]\GunModel
					EndIf
					If Players[i]\GunModelMuzzleFlash <> 0 Then
						HideEntity Players[i]\GunModelMuzzleFlash
					EndIf
				EndIf
			EndIf
		EndIf
	Next
	
	CatchErrors("Uncaught (AnimatePlayerModelsAndSpectate)")
End Function

Function FindPlayerToSpectate(iterator%)
	
	Local prevSpectatePlayer = mp_I\SpectatePlayer
	If prevSpectatePlayer = -1 Then
		prevSpectatePlayer = mp_I\PlayerID
	EndIf
	Repeat
		mp_I\SpectatePlayer = mp_I\SpectatePlayer + iterator
		If mp_I\SpectatePlayer > (mp_I\MaxPlayers-1) Then
			mp_I\SpectatePlayer = 0
		ElseIf mp_I\SpectatePlayer < 0 Then
			mp_I\SpectatePlayer = mp_I\MaxPlayers-1
		EndIf
		
		If Players[mp_I\SpectatePlayer] <> Null Then
			If (Players[mp_I\SpectatePlayer]\Team = Players[mp_I\PlayerID]\Team) And (Not IsSpectator(mp_I\SpectatePlayer)) Then ;CHECK FOR IMPLEMENTATION
				mp_I\Map\CurrChunk = Players[mp_I\SpectatePlayer]\CurrChunk
				Return
			EndIf
		EndIf
		If mp_I\SpectatePlayer = prevSpectatePlayer Then
			Exit
		EndIf
	Forever
	If mp_I\SpectatePlayer < 0 Lor prevSpectatePlayer = mp_I\PlayerID Lor Players[mp_I\SpectatePlayer] = Null Then
		mp_I\SpectatePlayer = mp_I\PlayerID
		mp_I\Map\CurrChunk = Players[mp_I\SpectatePlayer]\CurrChunk
	EndIf
	
End Function

Function ManipulatePlayerModelBones()
	CatchErrors("ManipulatePlayerModelBones")
	
	Local i%,bonename$,bone%
	
	For i = 0 To (mp_I\MaxPlayers-1)
		If Players[i]<>Null Then
			If mp_I\PlayerID<>i Then
				If Players[i]\CurrHP > 0 And (Not IsSpectator(i)) Then ;CHECK FOR IMPLEMENTATION
					bonename$ = "chest"
					If bonename <> "" Then
						bone% = FindChild(Players[i]\obj_upper,bonename$)
						If bone <> 0
							RotateEntity bone%,EntityPitch(bone),-Players[i]\Pitch,EntityRoll(bone)
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf
	Next
	
	CatchErrors("Uncaught (ManipulatePlayerModelBones)")
End Function

Function DamagePlayer(playerid%,hpdamage%,kevlardamage%,kevlarprotectionrate#=5.0)
	
	If mp_I\PlayState = GAME_CLIENT Then Return
	
	If Players[playerid]\CurrKevlar>0 Then
		Players[playerid]\CurrKevlar = Max(Players[playerid]\CurrKevlar-kevlardamage,0)
		Players[playerid]\CurrHP = Max(Players[playerid]\CurrHP-(hpdamage/Max(kevlarprotectionrate#,1.0)),0)
	Else
		Players[playerid]\CurrHP = Max(Players[playerid]\CurrHP-hpdamage,0)
	EndIf
	
End Function

Type PlayerSpawner
	Field yaw#
	Field team%
	Field obj%
	Field hasPlayerSpawned%
End Type

Function CreatePlayerSpawner.PlayerSpawner(x#, y#, z#, yaw#, team%)
	Local ps.PlayerSpawner = New PlayerSpawner
	
	ps\obj = CreatePivot()
	PositionEntity ps\obj,x#,y#,z#,True
	ps\yaw = yaw
	ps\team = team
	
	Return ps
End Function

Function RespawnPlayers()
	Local ps.PlayerSpawner
	
	Local i%
	
	For i = 0 To (mp_I\MaxPlayers-1)
		If Players[i]<>Null Then
			If Players[i]\CurrHP <= 0 Lor mp_I\Gamemode\Phase <= Waves_StartGame Then
				RespawnPlayer(i)
			EndIf
		EndIf
	Next
	
End Function

Function RespawnPlayer(playerID%)
	Local ps.PlayerSpawner
	Local i%
	Local mmt.MultiplayerMapTemplate
	
	TeleportEntity(Players[playerID]\Collider,0,3,0,0.3,True,2.0,0)
	
	Local found% = False
	While (Not found)
		For ps = Each PlayerSpawner
;			If Rand(1,3)=1 And ps\team = Max(Players[playerID]\Team-1,0) And (Not ps\hasPlayerSpawned) Then
;				TeleportEntity(Players[playerID]\Collider,EntityX(ps\obj),EntityY(ps\obj)+0.1,EntityZ(ps\obj),0.3,True,2.0,0)
;				found = True
;				ps\hasPlayerSpawned = True
;				Exit
;			EndIf
			If Rand(1,3)=1 And ps\team = Max(Players[playerID]\Team-1,0) Then
				TeleportEntity(Players[playerID]\Collider,EntityX(ps\obj),EntityY(ps\obj)+0.1,EntityZ(ps\obj),0.3,True,2.0,0)
				found = True
				ps\hasPlayerSpawned = True
				Exit
			EndIf
		Next
	Wend
	
	Players[playerID]\X = EntityX(Players[playerID]\Collider)
	Players[playerID]\Y = EntityY(Players[playerID]\Collider)
	Players[playerID]\Z = EntityZ(Players[playerID]\Collider)
	
	Local beforeWeapon% = (Players[playerID]\WeaponInSlot[QUICKSLOT_PRIMARY] <> GUN_UNARMED Lor Players[playerID]\SelectedSlot <> QUICKSLOT_PRIMARY)
	If Players[playerID]\CurrHP <= 0 Then
		Players[playerID]\CurrHP = 100
		Players[playerID]\CurrKevlar = 100
		Players[playerID]\CurrStamina = 100.0
		Players[playerID]\WantsSlot = QUICKSLOT_SECONDARY
		Players[playerID]\SelectedSlot = QUICKSLOT_SECONDARY
		Players[playerID]\WeaponInSlot[QUICKSLOT_PRIMARY] = GUN_UNARMED
		Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot] = GUN_BERETTA
		Players[playerID]\WeaponInSlot[QUICKSLOT_HOLSTER] = GUN_KNIFE
		Players[playerID]\Ammo[Players[playerID]\SelectedSlot] = GetWeaponMaxCurrAmmo(Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot])
		Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot] = 45
		Players[playerID]\IronSight = False
	ElseIf mp_I\Gamemode\ID = Gamemode_Deathmatch Then
		Players[playerID]\CurrHP = 100
		Players[playerID]\CurrKevlar = 100
		Players[playerID]\CurrStamina = 100.0
		
		Players[playerID]\SelectedSlot = QUICKSLOT_SECONDARY
		Players[playerID]\Ammo[Players[playerID]\SelectedSlot] = GetWeaponMaxCurrAmmo(Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot])
		If Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot] < Floor(GetWeaponMaxReloadAmmo(Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot])/2.0) Then
			Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot] = Floor(GetWeaponMaxReloadAmmo(Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot])/2.0)
		EndIf
		
		If Players[playerID]\WeaponInSlot[QUICKSLOT_PRIMARY] <> GUN_UNARMED Then
			Players[playerID]\SelectedSlot = QUICKSLOT_PRIMARY
			Players[playerID]\Ammo[Players[playerID]\SelectedSlot] = GetWeaponMaxCurrAmmo(Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot])
			If Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot] < Floor(GetWeaponMaxReloadAmmo(Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot])/2.0) Then
				Players[playerID]\ReloadAmmo[Players[playerID]\SelectedSlot] = Floor(GetWeaponMaxReloadAmmo(Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot])/2.0)
			EndIf
		EndIf
		
		Players[playerID]\IronSight = False
	EndIf
	If playerID = mp_I\PlayerID Then
		DeselectIronSight()
		If beforeWeapon Then
			g_I\GunChangeFLAG = False
		EndIf
	EndIf
	
	If playerID = mp_I\PlayerID Then
		If Players[playerID]\Team = Team_CI Then
			mp_I\Map\CurrChunk = mp_I\MapInList\CISpawn-(mp_I\MapInList\ChunkStart-1)
		Else
			mp_I\Map\CurrChunk = mp_I\MapInList\NTFSpawn-(mp_I\MapInList\ChunkStart-1)
		EndIf
	EndIf
	
	SwitchPlayerGun(playerID)
	
	Players[playerID]\IsSpectator = False
	;ShowEntity Players[playerID]\Collider
	ShowEntity Players[playerID]\obj_lower
	ShowEntity Players[playerID]\obj_upper
	
End Function

Function ApplyPlayerHitBoxes.HitBox()
	Local hb.HitBox = New HitBox
	hb\NPCtype = -1
	
	Local i%,htype%,bonename$
	Local scaleX#,scaleY#,scaleZ#,posX#,posY#,posZ#
	Local file$ = "Data\PlayerBones.ini"
	
	;If NTF_GameModeFlag=3 And mp_I\PlayState=GAME_CLIENT Then Return
	
	For i = 0 To GetINIInt(file$,"Player","hitbox_amount")-1
		htype% = GetINIInt(file$,"Player","hitbox"+(i+1)+"_type")
		bonename$ = GetINIString(file$,"Player","hitbox"+(i+1)+"_parent")
		hb\BoneName[i] = bonename
		If htype = 0
			hb\HitBox1[i] = CreateCube()
			scaleX# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_scaleX",1.0)
			scaleY# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_scaleY",1.0)
			scaleZ# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_scaleZ",1.0)
			posX# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_posX",0.0)
			posY# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_posY",0.0)
			posZ# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_posZ",0.0)
			ScaleEntity hb\HitBox1[i],scaleX,scaleY,scaleZ
			PositionEntity hb\HitBox1[i],posX,posY,posZ
			EntityPickMode hb\HitBox1[i],2
			EntityAlpha hb\HitBox1[i],0.0
			HideEntity hb\HitBox1[i]
		ElseIf htype = 1
			hb\HitBox2[i] = CreateCube()
			scaleX# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_scaleX",1.0)
			scaleY# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_scaleY",1.0)
			scaleZ# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_scaleZ",1.0)
			posX# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_posX",0.0)
			posY# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_posY",0.0)
			posZ# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_posZ",0.0)
			ScaleEntity hb\HitBox2[i],scaleX,scaleY,scaleZ
			PositionEntity hb\HitBox2[i],posX,posY,posZ
			EntityPickMode hb\HitBox2[i],2
			EntityAlpha hb\HitBox2[i],0.0
			HideEntity hb\HitBox2[i]
		Else
			hb\HitBox3[i] = CreateCube()
			scaleX# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_scaleX",1.0)
			scaleY# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_scaleY",1.0)
			scaleZ# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_scaleZ",1.0)
			posX# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_posX",0.0)
			posY# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_posY",0.0)
			posZ# = GetINIFloat(file$,"Player","hitbox"+(i+1)+"_posZ",0.0)
			ScaleEntity hb\HitBox3[i],scaleX,scaleY,scaleZ
			PositionEntity hb\HitBox3[i],posX,posY,posZ
			EntityPickMode hb\HitBox3[i],2
			EntityAlpha hb\HitBox3[i],0.0
			HideEntity hb\HitBox3[i]
		EndIf
		hb\HitBoxPosX[i]=posX
		hb\HitBoxPosY[i]=posY
		hb\HitBoxPosZ[i]=posZ
	Next
	
	Return hb
End Function

Function CopyPlayerHitBoxes(playerid%)
	Local hb.HitBox,bone%,i%
	Local HitBoxName$
	
	;If mp_I\PlayState = GAME_CLIENT Then Return
	
	For hb = Each HitBox
		If hb\NPCtype = -1 Then
			For i = 0 To 24
				If hb\BoneName[i]<>"" Then
					;n\BoneName[i]=hb\BoneName[i]
					HitBoxName = hb\BoneName[i]
					bone = FindChild(Players[playerid]\obj_lower,HitBoxName)
					If bone = 0 Then
						bone = FindChild(Players[playerid]\obj_upper,HitBoxName)
					EndIf
				EndIf
				If hb\HitBox1[i]<>0 Then
					Players[playerid]\HitBox1[i] = CopyEntity(hb\HitBox1[i],bone)
					PositionEntity Players[playerid]\HitBox1[i],hb\HitBoxPosX[i],hb\HitBoxPosY[i],hb\HitBoxPosZ[i]
				EndIf
				If hb\HitBox2[i]<>0 Then
					Players[playerid]\HitBox2[i] = CopyEntity(hb\HitBox2[i],bone)
					PositionEntity Players[playerid]\HitBox2[i],hb\HitBoxPosX[i],hb\HitBoxPosY[i],hb\HitBoxPosZ[i]
				EndIf
				If hb\HitBox3[i]<>0 Then
					Players[playerid]\HitBox3[i] = CopyEntity(hb\HitBox3[i],bone)
					PositionEntity Players[playerid]\HitBox3[i],hb\HitBoxPosX[i],hb\HitBoxPosY[i],hb\HitBoxPosZ[i]
				EndIf
			Next
			Exit
		EndIf
	Next
	
End Function

Function HidePlayerHitBoxes(playerid%)
	Local i%
	
	;If mp_I\PlayState = GAME_CLIENT Then Return
	
	For i = 0 To 24
		If Players[playerid]\HitBox1[i]<>0 Then HideEntity Players[playerid]\HitBox1[i]
		If Players[playerid]\HitBox2[i]<>0 Then HideEntity Players[playerid]\HitBox2[i]
		If Players[playerid]\HitBox3[i]<>0 Then HideEntity Players[playerid]\HitBox3[i]
	Next
	
End Function

Function ShowPlayerHitBoxes(playerid%)
	Local i%
	
	;If mp_I\PlayState = GAME_CLIENT Then Return
	
	For i = 0 To 24
		If Players[playerid]\HitBox1[i]<>0 Then ShowEntity Players[playerid]\HitBox1[i]
		If Players[playerid]\HitBox2[i]<>0 Then ShowEntity Players[playerid]\HitBox2[i]
		If Players[playerid]\HitBox3[i]<>0 Then ShowEntity Players[playerid]\HitBox3[i]
	Next
	
End Function

Function FreePlayerHitBoxes(playerid%)
	Local i%
	
	For i = 0 To 24
		Players[playerid]\HitBox1[i] = FreeEntity_Strict(Players[playerid]\HitBox1[i])
		Players[playerid]\HitBox2[i] = FreeEntity_Strict(Players[playerid]\HitBox2[i])
		Players[playerid]\HitBox3[i] = FreeEntity_Strict(Players[playerid]\HitBox3[i])
	Next
	
End Function

Function SetupTeam(playerid%)
	CatchErrors("SetupTeam(" + playerid + ")")
	Local g.Guns
	Local temp#
	Local i%, j%, sf%, b%, t1%, name$, tex%, teamSTR$
	
	If mp_I\PlayerID = playerid Then
		If mp_O\OtherTeams Then
			If Players[mp_I\PlayerID]\Team = Team_CI Then
				teamSTR = "_sh.png"
			Else
				If mp_I\Gamemode\ID = Gamemode_Deathmatch Then
					teamSTR = "_rrh.png"
				ElseIf mp_I\Gamemode\ID = Gamemode_EAF Then
					teamSTR = "_sh.png"
				Else
					teamSTR = "_sne.png"
				EndIf
			EndIf
		Else
			If mp_I\Gamemode\ID = Gamemode_EAF Then
				teamSTR = "_ci.png"
			Else
				If Players[mp_I\PlayerID]\Team = Team_CI Then
					teamSTR = "_ci.png"
				Else
					teamSTR = "_ntf.png"
				EndIf
			EndIf
		EndIf
		
		For g = Each Guns
			If g\IsSeparate Then
				ReplaceTextureByMaterial("GFX\weapons\models\hands\hands" + teamSTR,g\HandsObj,"hands",5)
			Else
				ReplaceTextureByMaterial("GFX\weapons\models\hands\hands" + teamSTR,g\obj,"hands",5)
			EndIf
		Next
		
	EndIf
	
	If Players[playerid]\GunModel <> 0 Lor Players[playerid]\obj_upper <> 0 Lor Players[playerid]\obj_lower <> 0 Then
		FreePlayerHitBoxes(playerid)
		Players[playerid]\GunModelMuzzleFlash = FreeEntity_Strict(Players[playerid]\GunModelMuzzleFlash)
		Players[playerid]\GunModel = FreeEntity_Strict(Players[playerid]\GunModel)
		Players[playerid]\obj_upper = FreeEntity_Strict(Players[playerid]\obj_upper)
		Players[playerid]\obj_lower = FreeEntity_Strict(Players[playerid]\obj_lower)
	EndIf
	
	;CHECK FOR IMPLEMENTATION
	ChangePlayerModel(playerid, Players[playerid]\Team-1)
	
	If mp_I\PlayState = GAME_SERVER Then
		RespawnPlayer(playerid)
	Else
		If mp_I\PlayerID = playerid Then
			If Players[playerid]\Team = Team_CI Then
				mp_I\Map\CurrChunk = mp_I\MapInList\CISpawn-(mp_I\MapInList\ChunkStart-1)
			Else
				mp_I\Map\CurrChunk = mp_I\MapInList\NTFSpawn-(mp_I\MapInList\ChunkStart-1)
			EndIf
		EndIf
	EndIf
	
	SwitchPlayerGun(playerid)
	
	CatchErrors("Uncaught (SetupTeam(" + playerid + "))")
End Function

Function ChangePlayerModel(playerid%, team%)
	Local temp#
	
	team = Clamp(team, Team_Unknown, MaxPlayerTeams-1)
	
	Players[playerid]\obj_lower = CopyEntity(mp_I\PlayerModel_Lower[team])
	Players[playerid]\obj_upper = CopyEntity(mp_I\PlayerModel_Upper[team])
	temp# = (0.29 / 2.5)
	ScaleEntity Players[playerid]\obj_lower, temp, temp, temp
	MeshCullBox (Players[playerid]\obj_lower, -MeshWidth(Players[playerid]\obj_lower), -MeshHeight(Players[playerid]\obj_lower), -MeshDepth(Players[playerid]\obj_lower), MeshWidth(Players[playerid]\obj_lower)*2, MeshHeight(Players[playerid]\obj_lower)*2, MeshDepth(Players[playerid]\obj_lower)*2)
	ScaleEntity Players[playerid]\obj_upper, temp, temp, temp
	PositionEntity Players[playerid]\obj_upper,0.0,-0.05,-0.06
	EntityParent Players[playerid]\obj_upper,FindChild(Players[playerid]\obj_lower,"hips")
	MeshCullBox (Players[playerid]\obj_upper, -MeshWidth(Players[playerid]\obj_upper), -MeshHeight(Players[playerid]\obj_upper), -MeshDepth(Players[playerid]\obj_upper), MeshWidth(Players[playerid]\obj_upper)*2, MeshHeight(Players[playerid]\obj_upper)*2, MeshDepth(Players[playerid]\obj_upper)*2)
	
	Players[playerid]\AnimState_Lower = -1
	Players[playerid]\AnimState_Upper = -1
	
	CopyPlayerHitBoxes(playerid)
	HidePlayerHitBoxes(playerid)
	
End Function

Function DeletePlayerAsServer(IDUpper%, IDLower%, ID%, reason$)
	Local cmsg.ChatMSG
	
	cmsg = AddChatMSG(reason, 0, SERVER_MSG_IS, CHATMSG_TYPE_ONEPARAM_TRANSLATE)
	cmsg\Msg[1] = Players[ID]\Name
	mp_I\PlayerCount=mp_I\PlayerCount-1
	UpdateServer(IDLower, IDUpper, mp_I\PlayerCount)
	Players[ID]\Collider = FreeEntity_Strict(Players[ID]\Collider)
	Players[ID]\obj_upper = FreeEntity_Strict(Players[ID]\obj_upper)
	Players[ID]\obj_lower = FreeEntity_Strict(Players[ID]\obj_lower)
	Players[ID]\NameTag = FreeEntity_Strict(Players[ID]\NameTag)
	Players[ID]\OverHereSprite = FreeEntity_Strict(Players[ID]\OverHereSprite)
	If Players[ID]\OverHerePosition<>Null Then
		Delete Players[ID]\OverHerePosition
	EndIf
	Delete Players[ID]
	Players[ID]=Null
	If mp_I\SpectatePlayer = ID Then
		FindPlayerToSpectate(1)
	EndIf
	
End Function

Function IsSpectator(playerid%)
	
	If Players[playerid]\Team > Team_Unknown And (Not Players[playerid]\IsSpectator) Then
		Return False
	EndIf
	Return True
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D