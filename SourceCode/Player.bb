
; ~ Player constants
Global Designation$
Const MaxStepSounds% = 8
Const MaxMaterialSounds% = 3

; ~ Wheel constants
Const WHEEL_CLOSED = 0
Const WHEEL_COMMAND = 1
Const WHEEL_SOCIAL = 2
Const WHEEL_MAX = 9
Const WHEEL_OUTPUT_UNKNOWN = 254 ;Will change it back once that bug is fixed where 255 seems to create another byte

; ~ Command wheel constants
Const COMMAND_OVERHERE = 0
Const COMMAND_HELPME = 2
Const COMMAND_WAITHERE = 3
Const COMMAND_TESLA = 4 ;Singleplayer Only
Const COMMAND_CANCEL = 5
Const COMMAND_CAMERA = 6 ;Singleplayer Only
Const COMMAND_FOLLOWME = 7
Const COMMAND_COVERME = 8

; ~ Social wheel constants
Const SOCIAL_LETSGO = 0
Const SOCIAL_ACTRUDE = 2
Const SOCIAL_NEGATIVE = 3
Const SOCIAL_SORRY = 4
Const SOCIAL_CANCEL = 5
Const SOCIAL_THANKYOU = 6
Const SOCIAL_AFFIRMATIVE = 7
Const SOCIAL_GOODJOB = 8

; ~ Command wheel look here constants
Const ICON_LOOK_DEFAULT = 0
Const ICON_LOOK_AMMO = 1
Const ICON_LOOK_GUN = 2
Const ICON_LOOK_ENEMY = 3
Const WHEEL_MAX_LOOK_ICONS = 4

; ~ Dialogue constants
Const MTF_DIALOGUE_TIMER_MIN = 70*45
Const MTF_DIALOGUE_TIMER_MAX = 70*120
Const MTF_DIALOGUE_MAX = 5
Const MTF_DIALOGUE_NUM_OF_BITS = 2

Type MainPlayer
	Field Camera
	Field CameraPivot%
	Field StepSoundWalk%[MaxStepSounds*MaxMaterialSounds]
	Field StepSoundRun%[MaxStepSounds*MaxMaterialSounds]
	Field HasNTFGasmask%
	Field NightVisionEnabled%
	Field SlotsDisplayTimer#
	; ~ Player Model
	Field Model
	Field ModelVest
	Field ModelHazmat
	Field ModelHDS
	Field ModelNTF
	Field ModelD
	Field ModelCollider
	Field ShowPlayerModel
	Field ArmsGuard
	Field ArmsNTF
	Field ArmsD
	; ~ Communication and Social wheel
	Field WheelOpened%
	Field WheelSprite%
	Field WheelMiddle%
	Field WheelSelect%
	Field WheelOutput%[WHEEL_MAX]
	Field WheelSelectedOutput%
	Field WheelCurrentMouseXSpeed#
	Field WheelCurrentMouseYSpeed#
	Field WheelLookHereIcons%[WHEEL_MAX_LOOK_ICONS]
	Field WheelLookHereSelectedIcon%
	Field HealthIcon
	Field KevlarIcon
	Field HelmetIcon
	Field DamageOverlay
	Field DamageTimer#
	Field OverHerePosition.Vector3D
	Field OverHereCommand%
	Field OverHereSprite%
	Field OverHereSpriteTime#
End Type

Type PlayerSP
	Field SharedPlayer.MainPlayer
	Field Pos.Vector3D
	Field Rot.Vector3D
	Field PlayerRoom.Rooms
	Field Collider
	Field DropSpeed#
	Field DeployState#
	Field ShootState#
	Field ShootState2#
	Field ReloadState#
	Field SoundCHN%
	Field Kevlar#
	Field Helmet#
	Field Health#
	Field Checkpoint106Passed%
	Field NoMove%
	Field NoRotation%
	Field IsShowingHUD%
End Type

Function CreateMainPlayer()
	Local i,temp#
	
	temp# = (0.033 / 2.5)
	
	; ~ [PLAYER TYPE]
	
	mpl = New MainPlayer
	
	; ~ [STEP SOUNDS]
	
	For i = 0 To (MaxStepSounds-1)
		mpl\StepSoundWalk[i] = LoadSound_Strict("SFX\Player\StepSounds\Concrete_Walk"+(i+1)+".ogg")
		mpl\StepSoundRun[i] = LoadSound_Strict("SFX\Player\StepSounds\Concrete_Run"+(i+1)+".ogg")
		mpl\StepSoundWalk[i+MaxStepSounds] = LoadSound_Strict("SFX\Player\StepSounds\Metal_Walk"+(i+1)+".ogg")
		mpl\StepSoundRun[i+MaxStepSounds] = LoadSound_Strict("SFX\Player\StepSounds\Metal_Run"+(i+1)+".ogg")
		mpl\StepSoundWalk[i+MaxStepSounds*2] = LoadSound_Strict("SFX\Player\StepSounds\Water_Walk"+(i+1)+".ogg")
		mpl\StepSoundRun[i+MaxStepSounds*2] = LoadSound_Strict("SFX\Player\StepSounds\Water_Run"+(i+1)+".ogg")
	Next
	
	; ~ [BODY MODELS]
	
	mpl\ModelCollider = CreatePivot()
	
	If gopt\GameMode = GAMEMODE_NTF Lor gopt\GameMode = GAMEMODE_MULTIPLAYER Then
		; ~ NTF
		mpl\ModelNTF = LoadAnimMesh_Strict("GFX\Player\NTF_Playermodel.b3d", mpl\ModelCollider)
		MeshCullBox (mpl\ModelNTF, -MeshWidth(mpl\ModelNTF), -MeshHeight(mpl\ModelNTF), -MeshDepth(mpl\ModelNTF)*5, MeshWidth(mpl\ModelNTF)*2, MeshHeight(mpl\ModelNTF)*2, MeshDepth(mpl\ModelNTF)*10)
		ScaleEntity mpl\ModelNTF, temp, temp, temp
		PositionEntity mpl\ModelNTF,0,0,-0.1
		HideEntity(mpl\ModelNTF)
	ElseIf gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
		If gopt\GameMode = GAMEMODE_DEFAULT Then
			; ~ Default
			mpl\Model = LoadAnimMesh_Strict("GFX\Player\Guard_Playermodel.b3d", mpl\ModelCollider)
			MeshCullBox (mpl\Model, -MeshWidth(mpl\Model), -MeshHeight(mpl\Model), -MeshDepth(mpl\Model)*5, MeshWidth(mpl\Model)*2, MeshHeight(mpl\Model)*2, MeshDepth(mpl\Model)*10)
			ScaleEntity mpl\Model, temp, temp, temp
			PositionEntity mpl\Model,0,0,-0.1
			HideEntity(mpl\Model)
			; ~ Vest
			mpl\ModelVest = LoadAnimMesh_Strict("GFX\Player\Armored_Guard_Playermodel.b3d", mpl\ModelCollider)
			MeshCullBox (mpl\ModelVest, -MeshWidth(mpl\ModelVest), -MeshHeight(mpl\ModelVest), -MeshDepth(mpl\ModelVest)*5, MeshWidth(mpl\ModelVest)*2, MeshHeight(mpl\ModelVest)*2, MeshDepth(mpl\ModelVest)*10)
			ScaleEntity mpl\ModelVest, temp, temp, temp
			PositionEntity mpl\ModelVest,0,0,-0.1
			HideEntity(mpl\ModelVest)
		EndIf
		If gopt\GameMode = GAMEMODE_CLASS_D Then
			; ~ Class-D
			mpl\ModelD = LoadAnimMesh_Strict("GFX\Player\D_Playermodel.b3d", mpl\ModelCollider)
			MeshCullBox (mpl\ModelD, -MeshWidth(mpl\ModelD), -MeshHeight(mpl\ModelD), -MeshDepth(mpl\ModelD)*5, MeshWidth(mpl\ModelD)*2, MeshHeight(mpl\ModelD)*2, MeshDepth(mpl\ModelD)*10)
			ScaleEntity mpl\ModelD, temp, temp, temp
			PositionEntity mpl\ModelD,0,0,-0.1
			HideEntity(mpl\ModelD)
		EndIf
		; ~ HDS
		mpl\ModelHDS = LoadAnimMesh_Strict("GFX\Player\HDS_Playermodel.b3d", mpl\ModelCollider)
		MeshCullBox (mpl\ModelHDS, -MeshWidth(mpl\ModelHDS), -MeshHeight(mpl\ModelHDS), -MeshDepth(mpl\ModelHDS)*5, MeshWidth(mpl\ModelHDS)*2, MeshHeight(mpl\ModelHDS)*2, MeshDepth(mpl\ModelHDS)*10)
		ScaleEntity mpl\ModelHDS, temp, temp, temp
		PositionEntity mpl\ModelHDS,0,0,-0.1
		HideEntity(mpl\ModelHDS)
		; ~ Hazmat
		mpl\ModelHazmat = LoadAnimMesh_Strict("GFX\Player\Hazmat_Playermodel.b3d", mpl\ModelCollider)
		MeshCullBox (mpl\ModelHazmat, -MeshWidth(mpl\ModelHazmat), -MeshHeight(mpl\ModelHazmat), -MeshDepth(mpl\ModelHazmat)*5, MeshWidth(mpl\ModelHazmat)*2, MeshHeight(mpl\ModelHazmat)*2, MeshDepth(mpl\ModelHazmat)*10)
		ScaleEntity mpl\ModelHazmat, temp, temp, temp
		PositionEntity mpl\ModelHazmat,0,0,-0.1
		HideEntity(mpl\ModelHazmat)
	EndIf
	
	; ~ [OTHER]
	
	mpl\CameraPivot = CreatePivot()
	mpl\HealthIcon = LoadImage_Strict("GFX\hp_icon.png")
	mpl\KevlarIcon = LoadImage_Strict("GFX\kevlar_Icon.png")
	mpl\HelmetIcon = LoadImage_Strict("GFX\helmet_Icon.png")
	
	If gopt\GameMode = GAMEMODE_NTF Lor gopt\GameMode = GAMEMODE_MULTIPLAYER Then
		mpl\HasNTFGasmask = True
	EndIf
	
	; ~ [END]
	
End Function

Function UpdatePlayerModel()
	Local model, health#
	Local g.Guns, armsStr$
	
	If opt\PlayerModelEnabled Then
		mpl\ShowPlayerModel = True
	Else
		mpl\ShowPlayerModel = False
	EndIf
	
	If gopt\GameMode = GAMEMODE_DEFAULT Then
		armsStr$= ""
		If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname,4) = "vest" Then
			model = mpl\ModelVest
			If (Not EntityHidden(mpl\Model)) Then HideEntity mpl\Model
			If (Not EntityHidden(mpl\ModelHazmat)) Then HideEntity mpl\ModelHazmat
			If (Not EntityHidden(mpl\ModelHDS)) Then HideEntity mpl\ModelHDS
		ElseIf Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat" Then
			model = mpl\ModelHazmat
			If (Not EntityHidden(mpl\ModelVest)) Then HideEntity mpl\ModelVest
			If (Not EntityHidden(mpl\Model)) Then HideEntity mpl\Model
			If (Not EntityHidden(mpl\ModelHDS)) Then HideEntity mpl\ModelHDS
		ElseIf Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" Then
			model = mpl\ModelHDS
			If (Not EntityHidden(mpl\ModelVest)) Then HideEntity mpl\ModelVest
			If (Not EntityHidden(mpl\ModelHazmat)) Then HideEntity mpl\ModelHazmat
			If (Not EntityHidden(mpl\Model)) Then HideEntity mpl\Model
		Else
			model = mpl\Model
			If (Not EntityHidden(mpl\ModelVest)) Then HideEntity mpl\ModelVest
			If (Not EntityHidden(mpl\ModelHazmat)) Then HideEntity mpl\ModelHazmat
			If (Not EntityHidden(mpl\ModelHDS)) Then HideEntity mpl\ModelHDS
		EndIf
	ElseIf gopt\GameMode = GAMEMODE_CLASS_D Then
		armsStr$ = "_d"
		model = mpl\ModelD
	Else
		armsStr$ = "_ntf"
		model = mpl\ModelNTF
	EndIf
	
	If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
		health = Players[mp_I\PlayerID]\CurrHP
	Else
		health = psp\Health
	EndIf
	
	If mpl\ShowPlayerModel And health > 0 Then
		If EntityHidden(model) Then
			ShowEntity(model)
			If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
				For g = Each Guns
					If g\IsSeparate Then
						ReplaceTextureByMaterial("GFX\weapons\models\hands\hands"+armsStr$+".png",g\HandsObj,"hands",5)
					Else
						ReplaceTextureByMaterial("GFX\weapons\models\hands\hands"+armsStr$+".png",g\obj,"hands",5)
					EndIf
				Next
			EndIf
		EndIf
		PositionEntity mpl\ModelCollider, EntityX(Camera), EntityY(Camera)-0.9, EntityZ(Camera)
		RotateEntity mpl\ModelCollider, 0,EntityYaw(Camera), 0
		
		If CurrSpeed > 0 And (Not IsPlayerSprinting) And (Not Crouch) Then
			If KeyDown(KEY_UP) Then															; ~ Walk North
				Animate2(model,AnimTime(model),2,32,0.45)
			ElseIf KeyDown(KEY_DOWN) Then													; ~ Walk South
				Animate2(model,AnimTime(model),33,61,0.45)
			ElseIf KeyDown(KEY_RIGHT) Then													; ~ Walk East
				Animate2(model,AnimTime(model),62,88,0.45)
			ElseIf KeyDown(KEY_LEFT) Then													; ~ Walk West
				Animate2(model,AnimTime(model),89,115,0.45)
			EndIf
		ElseIf CurrSpeed > 0 And IsPlayerSprinting And (Not Crouch) Then
			If KeyDown(KEY_UP) Then															; ~ Run North
				Animate2(model,AnimTime(model),2,32,0.7)
			ElseIf KeyDown(KEY_DOWN) Then													; ~ Run South
				Animate2(model,AnimTime(model),33,61,0.7)
			ElseIf KeyDown(KEY_RIGHT) Then													; ~ Run East
				Animate2(model,AnimTime(model),62,88,0.7)
			ElseIf KeyDown(KEY_LEFT) Then													; ~ Run West
				Animate2(model,AnimTime(model),89,115,0.7)
			EndIf
		ElseIf CurrSpeed < 1 And (Not IsPlayerSprinting) And (Not Crouch) Then				; ~ Idle
			SetAnimTime(model,1)
		ElseIf CurrSpeed < 1 And Crouch Then												; ~ Crouch
			Animate2(model,AnimTime(model),116,135,0.5,False)
		ElseIf CurrSpeed > 0 And Crouch Then
			If KeyDown(KEY_UP) Then															; ~ Crouch Walk North
				Animate2(model,AnimTime(model),136,166,0.24)
			ElseIf KeyDown(KEY_DOWN) Then													; ~ Crouch Walk South
				Animate2(model,AnimTime(model),167,197,0.24)
			ElseIf KeyDown(KEY_RIGHT) Then													; ~ Crouch Walk East
				Animate2(model,AnimTime(model),198,228,0.24)
			ElseIf KeyDown(KEY_LEFT) Then													; ~ Crouch Walk West
				Animate2(model,AnimTime(model),229,259,0.24)
			EndIf
		ElseIf CurrSpeed < 1 And (Not Crouch) Then											; ~ (Should be standup from crouch, but apparently doesn't work)
			Animate2(model,AnimTime(model),135,116,-0.5,False)
		EndIf
	Else
		If (Not EntityHidden(model)) Then HideEntity(model)
	EndIf
	
End Function

Function UpdateHazardousDefenceSuit()
	Local isTimeForLogo%,isTimeForLogo2%,n.NPCs,itm%
	Local Red#,Green#,Blue#
	Local g.Guns, j%, sf%, b%, t1%, name$, tex%, i%
	
	; ~ Suit effects
	
	If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" Then
		BlinkEffect = 0.2
		BlinkEffectTimer = 1
		StaminaEffect = 0.4
		StaminaEffectTimer = 1
	EndIf
	
	; ~ Explosion
	
	If hds\Health < 0 Lor hds\Health > 100 Then
		hds\isBroken = True
	Else
		hds\isBroken = False
	EndIf
	
	If hds\isBroken Then
		CurrSpeed = CurveValue(0.0, CurrSpeed, 5.0)
		If hds\ExplodeTimer <> -1 Then
			hds\ExplodeTimer = hds\ExplodeTimer + FPSfactor
		EndIf
	EndIf
	
	If hds\ExplodeTimer > 0 And hds\ExplodeTimer < 70*0.025 Then
		If ChannelPlaying(hds\SoundCHN) Then StopChannel(hds\SoundCHN)
		If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[24])
	ElseIf hds\ExplodeTimer >= 70*4.2 And hds\ExplodeTimer < 70*4.3 Then
		psp\Health = Max(psp\Health - 25,0)
		BigCameraShake = 15
		CreateOverHereParticle(EntityX(Camera),EntityY(Camera),EntityZ(Camera),18)
		hds\ExplodeTimer = -1
		If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" Then
			RemoveItem(Inventory[SLOT_TORSO])
		EndIf
		For n = Each NPCs
			If EntityDistanceSquared(n\Collider,Collider) < PowTwo(2.5) Then
				n\HP = n\HP - (80+(EntityDistanceSquared(n\Collider,Collider)*2))
			EndIf
		Next
	EndIf
	
	; ~ Suit boot up interface
	
	Red# = 10
	Green# = 150
	Blue# = 200
	
	If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" Then
		
		If hds\BootUpTimer >= 0 And hds\BootUpTimer < 70*15.01 Then
			hds\BootUpTimer# = hds\BootUpTimer# + FPSfactor
		Else
			hds\BootUpTimer = -1
		EndIf
		
		If hds\BootUpTimer > 0 And hds\BootUpTimer < 70*0.2 Then
			IsCutscene = True
			BlinkTimer = -10
			IsCutscene = False
		EndIf
		
		If hds\BootUpTimer >= 70*0.2 And hds\BootUpTimer < 70*0.21 Then
			hds\CantWear = True
			isTimeForLogo = True
			If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[0])
		ElseIf hds\BootUpTimer >= 70*4 And hds\BootUpTimer < 70*4.01 Then
			isTimeForLogo2 = True
		ElseIf hds\BootUpTimer >= 70*11 And hds\BootUpTimer < 70*11.01 Then
			hds\CantWear = False
		EndIf
		
		If isTimeForLogo Then
			
			If opt\MusicVol <> 0 Then
				PlaySound_Strict(LoadTempSound("sfx\music\misc\hds_pickup.ogg"))
			EndIf
			CreateSplashText(GetLocalString("Hazardous Suit","welcome"),opt\GraphicWidth/2,opt\GraphicHeight/2,10,2,Font_Menu,True,False,Red,Green,Blue)
			
			CreateSplashText(GetLocalString("Hazardous Suit","boot_1"),20,opt\GraphicHeight-1000,100,2,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_2"),20,opt\GraphicHeight-980,100,2,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_3"),20,opt\GraphicHeight-960,80,2,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_4"),20,opt\GraphicHeight-940,80,2,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_5"),20,opt\GraphicHeight-920,80,2,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_6"),20,opt\GraphicHeight-900,80,2,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_7"),20,opt\GraphicHeight-880,70,2,Font_Digital_Small,False,False,Red,Green,Blue)
			
		EndIf
		If isTimeForLogo2 Then
			
			CreateSplashText(GetLocalString("Hazardous Suit","boot_8"),20,opt\GraphicHeight-860,120,2,Font_Digital_Small,False,False,Red,Green,Blue)
			
			CreateSplashText(GetLocalString("Hazardous Suit","boot_9"),20,opt\GraphicHeight-840,150,3,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_10"),20,opt\GraphicHeight-820,150,3,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_11"),20,opt\GraphicHeight-800,150,3,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_12"),20,opt\GraphicHeight-780,150,3,Font_Digital_Small,False,False,Red,Green,Blue)
			CreateSplashText(GetLocalString("Hazardous Suit","boot_13"),20,opt\GraphicHeight-760,150,3,Font_Digital_Small,False,False,Red,Green,Blue)
			
			CreateSplashText(GetLocalString("Hazardous Suit","boot_14"),20,opt\GraphicHeight-740,120,2,Font_Digital_Small,False,False,Red,Green,Blue)
		EndIf
		
	EndIf
	
End Function

Function CreateDarkSprite(r.Rooms, DarkSpriteID%)
	
	r\Objects[DarkSpriteID] = CreateSprite(ark_blur_cam)
	ScaleSprite(r\Objects[DarkSpriteID], Max(opt\GraphicWidth / 1240.0, 1.0), Max(opt\GraphicHeight / 960.0 * 0.8, 0.8))
	EntityTexture(r\Objects[DarkSpriteID], DarkTexture)
	EntityBlend (r\Objects[DarkSpriteID], 1)
	EntityOrder r\Objects[DarkSpriteID], -1002
	MoveEntity(r\Objects[DarkSpriteID], 0, 0, 1.0)
	EntityAlpha r\Objects[DarkSpriteID], 0.0
	
End Function

Function CreateCommunicationAndSocialWheel()
	Local i%
	
	mpl\WheelOpened% = WHEEL_CLOSED
	mpl\WheelSelectedOutput = WHEEL_OUTPUT_UNKNOWN
	For i = 0 To (WHEEL_MAX-1)
		mpl\WheelOutput[i] = WHEEL_OUTPUT_UNKNOWN
	Next
	If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
		Players[mp_I\PlayerID]\VoiceLine = WHEEL_OUTPUT_UNKNOWN
	EndIf
	
	mpl\WheelSprite% = LoadSprite("GFX\HUD\communication_wheel\com_wheel.png", 1+2, ark_blur_cam)
	MoveEntity mpl\WheelSprite,0,0,1
	ScaleSprite mpl\WheelSprite,0.5,0.5
	EntityOrder mpl\WheelSprite,-5001
	HideEntity mpl\WheelSprite
	
	mpl\WheelMiddle% = LoadSprite("GFX\HUD\communication_wheel\com_wheel_center.png", 1+2, mpl\WheelSprite)
	ScaleSprite mpl\WheelMiddle,0.5,0.5
	EntityOrder mpl\WheelMiddle,-5000
	
	mpl\WheelSelect% = LoadSprite("GFX\HUD\communication_wheel\com_wheel_selection.png", 1+2, mpl\WheelSprite)
	ScaleSprite mpl\WheelSelect,0.5,0.5
	EntityOrder mpl\WheelSelect,-5000
	
	mpl\WheelLookHereIcons[ICON_LOOK_DEFAULT] = LoadTexture_Strict("GFX\HUD\communication_wheel\look_default.png",1+2,1)
	mpl\WheelLookHereIcons[ICON_LOOK_AMMO] = LoadTexture_Strict("GFX\HUD\communication_wheel\look_ammo.png",1+2,1)
	mpl\WheelLookHereIcons[ICON_LOOK_GUN] = LoadTexture_Strict("GFX\HUD\communication_wheel\look_guns.png",1+2,1)
	mpl\WheelLookHereIcons[ICON_LOOK_ENEMY] = LoadTexture_Strict("GFX\HUD\communication_wheel\look_enemy.png",1+2,1)
	
	If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
		mpl\OverHereSprite = CreateSprite()
		ScaleSprite mpl\OverHereSprite,0.125,0.125
		EntityOrder mpl\OverHereSprite,-2
		EntityTexture mpl\OverHereSprite,mpl\WheelLookHereIcons[ICON_LOOK_DEFAULT]
		EntityFX mpl\OverHereSprite,1+8
		HideEntity mpl\OverHereSprite
	EndIf
	
End Function

Function UpdateCommunicationAndSocialWheel()
	Local it.Items, n.NPCs, p.Particles
	Local closeWheel% = False
	Local i%
	Local direction%, prevSelectedOutput%
	Local goesUp% = False
	Local voiceLine%, voiceLineNumber%
	Local voiceLineStr$, commandLineID%
	
	If ((gopt\GameMode = GAMEMODE_MULTIPLAYER And (Not InLobby()) And (Not mp_I\ChatOpen) And (Not IsSpectator(mp_I\PlayerID)) And Players[mp_I\PlayerID]\CurrHP > 0) Lor (gopt\GameMode <> GAMEMODE_MULTIPLAYER And EndingTimer >= 0 And KillTimer >= 0 And (Not InvOpen) And (Not MenuOpen))) And (Not AttachmentMenuOpen) And (Not ConsoleOpen) Then
		If KeyDown(kb\CommandWheelKey) Then
			If mpl\WheelOpened = WHEEL_CLOSED Then
				MoveMouse Viewport_Center_X, Viewport_Center_Y
				ResetInput()
				mpl\WheelOpened = WHEEL_COMMAND
				mpl\WheelSelectedOutput = COMMAND_OVERHERE
			EndIf
		ElseIf KeyDown(kb\SocialWheelKey) And gopt\GameMode = GAMEMODE_MULTIPLAYER Then
			If mpl\WheelOpened = WHEEL_CLOSED Then
				MoveMouse Viewport_Center_X, Viewport_Center_Y
				ResetInput()
				mpl\WheelOpened = WHEEL_SOCIAL
				mpl\WheelSelectedOutput = SOCIAL_LETSGO
			EndIf
		ElseIf mpl\WheelOpened <> WHEEL_CLOSED Then
			closeWheel = True
		EndIf
	Else
		closeWheel = True
		mpl\WheelOpened = WHEEL_CLOSED
	EndIf
	
	If closeWheel And mpl\WheelOpened <> WHEEL_CLOSED Then
		voiceLine% = mpl\WheelSelectedOutput+(WHEEL_MAX*(mpl\WheelOpened = WHEEL_SOCIAL))
		voiceLineNumber% = Rand(1,4)
		If voiceLine = COMMAND_OVERHERE Then
			For it = Each Items
				EntityRadius it\collider,0.2
			Next
			For n = Each NPCs
				ShowNPCHitBoxes(n)
			Next
			For i = 0 To (mp_I\MaxPlayers-1)
				If Players[i]<>Null And i<>mp_I\PlayerID Then
					ShowPlayerHitBoxes(i)
				EndIf
			Next
			CameraPick(Camera,opt\GraphicWidth/2,opt\GraphicHeight/2)
			For it = Each Items
				EntityRadius it\collider,0.01
			Next
			For n = Each NPCs
				HideNPCHitBoxes(n)
			Next
			For i = 0 To (mp_I\MaxPlayers-1)
				If Players[i]<>Null And i<>mp_I\PlayerID Then
					HidePlayerHitBoxes(i)
				EndIf
			Next
			If PickedEntity()<>0 Then
				CreateOverHereParticle(PickedX(), PickedY(), PickedZ())
				If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
					Players[mp_I\PlayerID]\OverHerePosition = CreateVector3D(PickedX(), PickedY(), PickedZ())
				EndIf
			EndIf
			
			commandLineID = GetPlayerCommandLineCategory()
			voiceLineStr = "SFX\Player\Voice\Chat\"
			Select commandLineID
				Case ICON_LOOK_DEFAULT
					voiceLineStr = voiceLineStr + "Look"
					mp_I\CurrChatMSG = "wheel_look"
				Case ICON_LOOK_ENEMY
					voiceLineStr = voiceLineStr + "Enemy"
					mp_I\CurrChatMSG = "wheel_enemy"
				Case ICON_LOOK_AMMO
					voiceLineStr = voiceLineStr + "Ammo"
					mp_I\CurrChatMSG = "wheel_ammo"
				Case ICON_LOOK_GUN
					voiceLineStr = voiceLineStr + "Gun"
					mp_I\CurrChatMSG = "wheel_gun"
			End Select
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				CreateChatMSG(True)
			EndIf
			voiceLineStr = voiceLineStr + "Here" + voiceLineNumber + ".ogg"
			mpl\WheelLookHereSelectedIcon = commandLineID
		Else
			voiceLineStr = GetPlayerVoiceLine(voiceLine, voiceLineNumber, mp_I\PlayerID)
		EndIf
		If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
			If voiceLine <> COMMAND_CANCEL And voiceLine <> (SOCIAL_CANCEL+WHEEL_MAX) Then
				If ChannelPlaying(Players[mp_I\PlayerID]\Sound_CHN) Then
					StopChannel(Players[mp_I\PlayerID]\Sound_CHN)
				EndIf
				Players[mp_I\PlayerID]\Sound_CHN = PlaySound_Strict(LoadTempSound(voiceLineStr))
				ChannelVolume(Players[mp_I\PlayerID]\Sound_CHN,opt\VoiceVol*opt\MasterVol)
				Players[mp_I\PlayerID]\VoiceLine = voiceLine
				Players[mp_I\PlayerID]\VoiceLineNumber = voiceLineNumber
			EndIf
			If voiceLine = COMMAND_OVERHERE Then
				EntityTexture Players[mp_I\PlayerID]\OverHereSprite,mpl\WheelLookHereIcons[commandLineID]
				ShowEntity Players[mp_I\PlayerID]\OverHereSprite
				PositionEntity Players[mp_I\PlayerID]\OverHereSprite,PickedX(),PickedY(),PickedZ()
				Players[mp_I\PlayerID]\OverHereSpriteTime = 70*5
			EndIf
		Else
			If voiceLine <> COMMAND_CANCEL And voiceLine <> (SOCIAL_CANCEL+WHEEL_MAX) Then
				If ChannelPlaying(psp\SoundCHN) Then
					StopChannel(psp\SoundCHN)
				EndIf
				psp\SoundCHN = PlaySound_Strict(LoadTempSound(voiceLineStr))
				SingleplayerVoiceActions(voiceLine)
				If voiceLine = COMMAND_OVERHERE Then
					EntityTexture mpl\OverHereSprite,mpl\WheelLookHereIcons[commandLineID]
					ShowEntity mpl\OverHereSprite
					PositionEntity mpl\OverHereSprite,PickedX(),PickedY(),PickedZ()
					mpl\OverHereSpriteTime = 70*5
				EndIf
			EndIf	
		EndIf
		mpl\WheelOpened = WHEEL_CLOSED
		ResetInput()
	EndIf
	
	If mpl\WheelOpened <> WHEEL_CLOSED Then
		ShowEntity mpl\WheelSprite
		
		If mpl\WheelOpened = WHEEL_COMMAND Then
			mpl\WheelOutput[0] = COMMAND_OVERHERE
			mpl\WheelOutput[2] = COMMAND_COVERME
			mpl\WheelOutput[3] = COMMAND_FOLLOWME
			mpl\WheelOutput[5] = COMMAND_CANCEL
			mpl\WheelOutput[7] = COMMAND_WAITHERE
			mpl\WheelOutput[8] = COMMAND_HELPME
			If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
				mpl\WheelOutput[4] = COMMAND_TESLA
				mpl\WheelOutput[6] = COMMAND_CAMERA
			EndIf
		ElseIf mpl\WheelOpened = WHEEL_SOCIAL Then
			mpl\WheelOutput[0] = SOCIAL_LETSGO
			mpl\WheelOutput[2] = SOCIAL_GOODJOB
			mpl\WheelOutput[3] = SOCIAL_AFFIRMATIVE
			mpl\WheelOutput[4] = SOCIAL_THANKYOU
			mpl\WheelOutput[5] = SOCIAL_CANCEL
			mpl\WheelOutput[6] = SOCIAL_SORRY
			mpl\WheelOutput[7] = SOCIAL_NEGATIVE
			mpl\WheelOutput[8] = SOCIAL_ACTRUDE
		EndIf
		
		direction = -1
		If (MousePosY < Mouse_Top_Limit) Then
			direction = 0
			goesUp = True
		EndIf
		If (MousePosY > Mouse_Bottom_Limit) Then
			direction = 180
			goesUp = True
		EndIf
		If goesUp Then
			If (MousePosX > (opt\GraphicWidth/2.0) + ((opt\GraphicWidth-Mouse_Right_Limit) / 2.0)) Then
				direction = direction - 45 + (90*(direction=180))
			ElseIf (MousePosX < (opt\GraphicWidth/2.0) - (Mouse_Left_Limit / 2.0)) Then
				direction = direction + 45 - (90*(direction=180))
			EndIf
			
			If direction = -45 Then
				direction = 315
			EndIf
		EndIf
		If direction = -1 Then
			If (MousePosX > Mouse_Right_Limit) Then
				direction = 270
			EndIf
			If (MousePosX < Mouse_Left_Limit) Then
				direction = 90
			EndIf
		EndIf
		
		If direction > -1 Then
			prevSelectedOutput = mpl\WheelSelectedOutput
			If mpl\WheelSelectedOutput = 0 Then
				mpl\WheelSelectedOutput = (direction/45)+1
			Else
				If Abs(((mpl\WheelSelectedOutput-1)*45.0) - direction) >= 135.0 Then
					mpl\WheelSelectedOutput = 0
				Else
					mpl\WheelSelectedOutput = (direction/45)+1
				EndIf
			EndIf
			If mpl\WheelOutput[mpl\WheelSelectedOutput] = WHEEL_OUTPUT_UNKNOWN Then
				mpl\WheelSelectedOutput = prevSelectedOutput
			EndIf
		EndIf
		
		If (MousePosX > Mouse_Right_Limit) Lor (MousePosX < Mouse_Left_Limit) Lor (MousePosY > Mouse_Bottom_Limit) Lor (MousePosY < Mouse_Top_Limit) Then
			MoveMouse Viewport_Center_X, Viewport_Center_Y
		EndIf
		
		If mpl\WheelSelectedOutput = 0 Then
			HideEntity mpl\WheelSelect
			ShowEntity mpl\WheelMiddle
		Else
			ShowEntity mpl\WheelSelect
			HideEntity mpl\WheelMiddle
			RotateSprite mpl\WheelSelect,(mpl\WheelSelectedOutput-1)*45.0
		EndIf
	Else
		HideEntity mpl\WheelSprite
		HideEntity mpl\WheelSelect
		ShowEntity mpl\WheelMiddle
		mpl\WheelSelectedOutput = WHEEL_OUTPUT_UNKNOWN
		For i = 0 To (WHEEL_MAX-1)
			mpl\WheelOutput[i] = WHEEL_OUTPUT_UNKNOWN
		Next
		mpl\WheelCurrentMouseXSpeed = 0.0
		mpl\WheelCurrentMouseYSpeed = 0.0
	EndIf
	
	If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
		For i = 0 To (mp_I\MaxPlayers-1)
			If Players[i]<>Null Then
				Players[i]\OverHereSpriteTime = Max(Players[i]\OverHereSpriteTime - FPSfactor, 0.0)
				If Players[i]\OverHereSpriteTime > 0.0 Then
					ShowEntity Players[i]\OverHereSprite
				Else
					HideEntity Players[i]\OverHereSprite
				EndIf
			EndIf
		Next
	Else
		mpl\OverHereSpriteTime = Max(mpl\OverHereSpriteTime - FPSfactor, 0.0)
		If mpl\OverHereSpriteTime > 0.0 Then
			ShowEntity mpl\OverHereSprite
		Else
			HideEntity mpl\OverHereSprite
		EndIf
	EndIf
	
End Function

Function GetPlayerVoiceLine$(voiceLine%, voiceLineNumber%, playerID%=-1)
	Local voice$
	Local n.NPCs
	
	voice = "SFX\Player\Voice\Chat\"
		
	Select voiceLine
		;Case COMMAND_OVERHERE
		;	voice = voice + "Look"
		;	If playerID >= 0 Then
		;		mp_I\CurrChatMSG = "wheel_look"
		;	EndIf
		Case COMMAND_HELPME
			voice = voice + "HelpMe"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_helpme"
			EndIf
		Case COMMAND_WAITHERE
			voice = voice + "Wait"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_wait"
			EndIf
		Case COMMAND_FOLLOWME
			voice = voice + "FollowMe"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_followme"
			EndIf
		Case COMMAND_COVERME
			voice = voice + "CoverMe"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_coverme"
			EndIf
		Case COMMAND_TESLA
			voice = voice + "Tesla"
		Case COMMAND_CAMERA
			voice = voice + "Camera"
		Case SOCIAL_LETSGO+WHEEL_MAX
			voice = voice + "LetsGo"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_letsgo"
			EndIf
		Case SOCIAL_ACTRUDE+WHEEL_MAX
			voice = voice + "Rude"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_actsrude"
			EndIf
		Case SOCIAL_NEGATIVE+WHEEL_MAX
			voice = voice + "Negative"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_negative"
			EndIf
		Case SOCIAL_SORRY+WHEEL_MAX
			voice = voice + "Sorry"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_sorry"
			EndIf
		Case SOCIAL_THANKYOU+WHEEL_MAX
			voice = voice + "Thanks"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_thanks"
			EndIf
		Case SOCIAL_AFFIRMATIVE+WHEEL_MAX
			voice = voice + "Affirmative"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_affirmative"
			EndIf
		Case SOCIAL_GOODJOB+WHEEL_MAX
			voice = voice + "GoodJob"
			If playerID >= 0 Then
				mp_I\CurrChatMSG = "wheel_goodjob"
			EndIf
	End Select
	
	If playerID >= 0 Then
		CreateChatMSG(True)
	EndIf
	
	voice = voice + voiceLineNumber + ".ogg"
	
	If gopt\GameMode <> GAMEMODE_MULTIPLAYER
		
		voice = "SFX\Player\Voice\Sanders\"
		
		Select voiceLine
			Case COMMAND_TESLA
				voice = voice + "TeslaGateDeactivation" + Rand(1, 2) + ".ogg"
			Case COMMAND_CAMERA
				voice = voice + "Camerafeed" + Rand(1, 2) + ".ogg"
			Case COMMAND_MEDICAL
				voice = voice + "Playerneedsmedicalattention" + Rand(1, 2) + ".ogg"
			Case COMMAND_OVERHERE
				For n = Each NPCs
					If n<>Null Then
						If EntityDistanceSquared(Collider, n\Collider) < PowTwo(7) Then
							If EntityVisible(Collider, n\Collider) Then
								Select n\NPCtype
									Case NPC_Class_D
										voice = voice + "Class-D Spotted STOP" + Rand(1, 3) + ".ogg"
									Case NPC_Zombie, NPC_Zombie_Armed
										voice = voice + "SCP-049-2Spotted" + Rand(1, 2) + ".ogg"
									Case NPC_SCP_173
										voice = voice + "SCP-173Spotted" + Rand(1, 3) + ".ogg"
									Case NPC_SCP_106
										voice = voice + "SCP-106Spotted" + Rand(1, 3) + ".ogg"
									Case NPC_SCP_049
										voice = voice + "SCP-049Spotted" + Rand(1, 3) + ".ogg"
									Case NPC_SCP_096
										voice = voice + "SCP-096Spotted" + Rand(1, 3) + ".ogg"
								End Select
							Else
								If n\NPCtype = NPC_Class_D Then
									voice = voice + "Class-D Detected" + Rand(1, 2) + ".ogg"
								EndIf
								Exit
							EndIf
						EndIf
					EndIf
				Next
			Case COMMAND_HELPME
				voice = voice + "HelptoTeam" + Rand(1, 6) + ".ogg"
			Case COMMAND_COVERME
				voice = voice + "StopFollowingtoTeam" + Rand(1, 4) + ".ogg"
		End Select
	EndIf
	
	Return voice
End Function

Function GetPlayerCommandLineCategory$()
	Local n.NPCs, p.Player, it.Items
	Local i%
	
	;Check for the correct voice line to be played
	;First: Check the NPCs and players (aka enemy)
	For n = Each NPCs
		For i = 0 To (MaxHitBoxes-1)
			If n\HP > 0 Then
				If n\HitBox1[i]<>0 And n\HitBox1[i] = PickedEntity() Then
					Return ICON_LOOK_ENEMY
				ElseIf n\HitBox2[i]<>0 And n\HitBox2[i] = PickedEntity() Then
					Return ICON_LOOK_ENEMY
				ElseIf n\HitBox3[i]<>0 And n\HitBox3[i] = PickedEntity() Then
					Return ICON_LOOK_ENEMY
				EndIf
			EndIf
		Next
		
		If n\obj = PickedEntity() Then
			Return ICON_LOOK_ENEMY
		EndIf
	Next
	;Check if a player is an enemy
	For p = Each Player
		If p\Team <> Players[mp_I\PlayerID]\Team Then
			For i = 0 To (MaxHitBoxes-1)
				If p\HitBox1[i]<>0 And p\HitBox1[i] = PickedEntity() Then
					Return ICON_LOOK_ENEMY
				ElseIf p\HitBox2[i]<>0 And p\HitBox2[i] = PickedEntity() Then
					Return ICON_LOOK_ENEMY
				ElseIf p\HitBox3[i]<>0 And p\HitBox3[i] = PickedEntity() Then
					Return ICON_LOOK_ENEMY
				EndIf
			Next
		EndIf
	Next
	;Check for ammo crate or weapon items
	For it = Each Items
		If it\collider = PickedEntity() Then
			Select it\itemtemplate\tempname
				Case "ammocrate","bigammocrate"
					Return ICON_LOOK_AMMO
				Default
					If it\itemtemplate\isGun Then
						Return ICON_LOOK_GUN
					EndIf
			End Select
		EndIf
	Next
	;Last resort: No targetted object found, use default
	Return ICON_LOOK_DEFAULT
	
End Function

Function CreateOverHereParticle(x#, y#, z#, image%=10)
	Local p.Particles
	
	p.Particles = CreateParticle(x,y,z,image,0.05,0.0,500)
	p\SizeChange = 0.01
	p\Achange = -0.01
	SpriteViewMode p\obj,1
	EntityOrder p\obj,-1
	;RotateEntity p\obj,90,0,0
	
End Function

Function RenderCommunicationAndSocialWheel()
	Local x# = opt\GraphicWidth / 2.0
	Local y# = opt\GraphicHeight / 2.0
	Local i%
	Local namespace$
	
	If mpl\WheelOpened <> WHEEL_CLOSED Then
		Color 255,255,255
		SetFont fo\Font[Font_Default_Medium]
		If mpl\WheelOpened = WHEEL_COMMAND Then
			namespace = "command"
		Else
			namespace = "social"
		EndIf
		For i = 0 To (WHEEL_MAX-1)
			If mpl\WheelOutput[i] <> WHEEL_OUTPUT_UNKNOWN Then
				If i > 0 Then
					x = (opt\GraphicWidth/2.0) + 300.0 * MenuScale * Sin((mpl\WheelOutput[i]-1) * 45.0)
					y = (opt\GraphicHeight/2.0) - 300.0 * MenuScale * Cos((mpl\WheelOutput[i]-1) * 45.0)
				EndIf
				Text x,y,GetLocalString("Chatwheel", namespace+i),True,True
			EndIf
		Next
	EndIf
	
End Function

Function SingleplayerVoiceActions(voiceLine%)
	
End Function

Function DestroyMainPlayer()
	Local i,n
	
	For i = 0 To (MaxStepSounds-1)
		For n = 0 To (MaxMaterialSounds-1)
			FreeSound_Strict(mpl\StepSoundWalk[i+(n*MaxStepSounds)])
			FreeSound_Strict(mpl\StepSoundRun[i+(n*MaxStepSounds)])
		Next
	Next
	FreeImage mpl\HealthIcon
	FreeImage mpl\KevlarIcon
	FreeImage mpl\HelmetIcon
	
	Delete mpl
	
End Function

Function UpdateNightVision()
	Local dist#
	
	;CHECK FOR IMPLEMENTATION
	If (gopt\GameMode = GAMEMODE_MULTIPLAYER And IsSpectator(mp_I\PlayerID)) Lor (Not mpl\HasNTFGasmask) Then
		If mpl\NightVisionEnabled Then
			TurnNVOff()
		EndIf
		mpl\NightVisionEnabled = False
		Return
	EndIf
	
	;TODO: Change this to InteractHit to add the controller key as well!
	
	If KeyHit(kb\NVToggleKey) Then
		mpl\NightVisionEnabled = Not mpl\NightVisionEnabled
		If mpl\NightVisionEnabled Then
			PlaySound_Strict LoadTempSound("SFX\Interact\NVGOn.ogg")
			TurnNVOn()
		Else
			PlaySound_Strict LoadTempSound("SFX\Interact\NVGOff.ogg")
			TurnNVOff()
		EndIf
	EndIf
	
End Function

Function TurnNVOn()
	
	AmbientLightRooms(100)
	If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
		If Players[mp_I\PlayerID]\Team = Team_CI Then
			EntityColor GasMaskOverlay2,220,30,30
		Else
			EntityColor GasMaskOverlay2,30,220,30
		EndIf
		If mp_O\Gamemode\ID = Gamemode_Deathmatch Then
			CameraFogRange(Camera,CameraFogFar*4,CameraFogFar*5)
		ElseIf mp_O\Gamemode\ID = Gamemode_Waves Lor mp_O\Gamemode\ID = Gamemode_EAF Then
			CameraFogRange(Camera,CameraFogFar*2,CameraFogFar*3)
		Else
			CameraFogRange(Camera,CameraFogFar,CameraFogFar)
		EndIf
	Else
		EntityColor GasMaskOverlay2,30,220,30
		StoredCameraFogFar = CameraFogFar
		CameraFogFar = 30
		CameraFogRange(Camera,CameraFogFar,CameraFogFar)
	EndIf
	CameraRange(Camera,0.01,GetCameraFogRangeFar(Camera)*2.0)
	
End Function

Function TurnNVOff()
	
	AmbientLightRooms(0)
	EntityColor GasMaskOverlay2,255,255,255
	If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
		If mp_O\Gamemode\ID = Gamemode_Deathmatch Then
			CameraFogRange(Camera,CameraFogNear,CameraFogFar*5)
		ElseIf mp_O\Gamemode\ID = Gamemode_Waves Lor mp_O\Gamemode\ID = Gamemode_EAF Then
			CameraFogRange(Camera,CameraFogNear,CameraFogFar*3)
		Else
			CameraFogRange(Camera,CameraFogNear,CameraFogFar)
		EndIf
	Else
		CameraFogFar = StoredCameraFogFar
		CameraFogRange(Camera,CameraFogFar,CameraFogFar)
	EndIf
	CameraRange(Camera,0.01,GetCameraFogRangeFar(Camera)*2.0)
	
End Function

Function CreateDamageOverlay()
	
	mpl\DamageOverlay = LoadSprite("GFX\Bloodoverlay.jpg",1+2,ark_blur_cam)
	ScaleSprite mpl\DamageOverlay,1.0,Float(opt\GraphicHeight)/Float(opt\GraphicWidth)
	EntityFX(mpl\DamageOverlay, 1)
	EntityOrder(mpl\DamageOverlay, -2001)
	MoveEntity(mpl\DamageOverlay, 0, 0, 1.0)
	HideEntity(mpl\DamageOverlay)
	
End Function

Function UpdateDamageOverlay()
	
	If mpl\DamageTimer > 0.0 Then
		ShowEntity mpl\DamageOverlay
		EntityAlpha mpl\DamageOverlay,Clamp(mpl\DamageTimer / 70.0, 0.0, 1.0)
		mpl\DamageTimer = Max(mpl\DamageTimer - FPSfactor, 0)
	Else
		HideEntity mpl\DamageOverlay
	EndIf
	
End Function

Function CreateSPPlayer()
	
	psp = New PlayerSP
	psp\Health = 100
	If gopt\GameMode = GAMEMODE_NTF Then
		psp\Kevlar = 100
		psp\Helmet = 100
		Designation$ = GetLocalString("Singleplayer", "designation_ntf")
	ElseIf gopt\GameMode = GAMEMODE_CLASS_D Then
		Designation$ = GetLocalStringR("Singleplayer", "designation_d",ClassDNumber)
	Else
		Designation$ = GetLocalString("Singleplayer", "designation")
	EndIf
	
	mtfd = New MTFDialogue
	mtfd\CurrentDialogue = -1
	mtfd\Timer = Rand(MTF_DIALOGUE_TIMER_MIN, MTF_DIALOGUE_TIMER_MAX) ;Starting timer
	mtfd\Dialogues[0] = %10
	mtfd\Dialogues[1] = %111011
	mtfd\Dialogues[2] = %111011
	mtfd\Dialogues[3] = %1011
	mtfd\Dialogues[4] = %111011
	
End Function

Function DestroySPPlayer()
	
	Delete psp
	
	Delete mtfd
	
End Function

Function UpdateSPPlayer()
	Local n.NPCs
	Local contain173% = False
	
	If Curr173 <> Null And Curr173\Idle = SCP173_STATIONARY And Curr173\IdleTimer > 0.0 Then
		;Check if at least one MTF unit is alive and looking at 173
		For n = Each NPCs
			If n\NPCtype = NPC_NTF And (Not n\IsDead) And n\State[0] = MTF_CONTAIN173 Then
				contain173 = True
				Exit
			EndIf
		Next
		
		If contain173 Then
			If (BlinkTimer <= -6 And (BlinkTimer + FPSfactor) > -6) Then
				PlayPlayerSPVoiceLine("SFX\Player\Voice\Sanders\Blinking" + Rand(1, 2))
			EndIf
		Else
			Curr173\Idle = SCP173_ACTIVE
			Curr173\IdleTimer = 0.0
		EndIf
	EndIf
	
End Function

Function IsSPPlayerAlive()
	
	If psp\Health > 0 Then Return True
	Return False
	
End Function

Function DamageSPPlayer(amount#, only_health%=False, kevlar_protect_factor#=4.0, helmet_protect_factor#=6.0)
	
	If (Not GodMode) Then
		If (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
			If only_health Lor psp\Kevlar = 0.0 Then
				psp\Health = Max(psp\Health - amount, 0.0)
			Else
				psp\Kevlar = Max(Inventory[SLOT_TORSO]\state - amount, 0.0)
				psp\Helmet = Max(Inventory[SLOT_HEAD]\state - amount*1.2, 0.0)
				If kevlar_protect_factor > 0.0 And Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 4) = "vest" Then
					psp\Health = Max(psp\Health - amount / kevlar_protect_factor, 0.0)
				EndIf
				If helmet_protect_factor > 0.0 And Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet" Lor mpl\HasNTFGasmask Then
					psp\Health = Max(psp\Health - amount / helmet_protect_factor, 0.0)
				EndIf
			EndIf
		ElseIf Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" And hds\Health >= 35 Then
			hds\Health = Max(Inventory[SLOT_TORSO]\state - amount/6, -1);7
			psp\Health = Max(psp\Health - amount/8, 0.0);10
			If hds\Health <= 90 And hds\Health > 89 Lor hds\Health <= 76 And hds\Health > 75 Then
				If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[1])
			EndIf
			If hds\Health <= 50 And hds\Health > 49 Lor hds\Health <= 36 And hds\Health > 35 Then
				If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[2])
			EndIf
			If hds\Health <= 35 And hds\Health > 34 Lor hds\Health <= 30 And hds\Health > 29 Then
				If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[3])
			EndIf
			If hds\Health <= 20 And hds\Health > 19 Lor hds\Health <= 15 And hds\Health > 14 Then
				If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[4])
			EndIf
		Else
			hds\Health = Max(Inventory[SLOT_TORSO]\state - amount/3, -1)
			psp\Health = Max(psp\Health - amount/7, 0.0)
			Select Rand(100)
				Case 0
					If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[(Rand(8,21))])
			End Select
		EndIf
	EndIf
	
	If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 4) = "vest" And psp\Kevlar < 1 Then
		RemoveItem(Inventory[SLOT_TORSO])
		CreateMsg(GetLocalString("Items","vest_destroyed"))
	EndIf
	
	If Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet" And psp\Helmet < 1 Then
		RemoveItem(Inventory[SLOT_HEAD])
		CreateMsg(GetLocalString("Items","helmet_destroyed"))
	EndIf
	
	If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" Then
		If hds\Health <= 10 And psp\Health <= 10 Then
			If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[5])
		EndIf
		
		If hds\Health <= 5 And psp\Health <= 5 And (Not hds\isBroken) Then
			If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[6])
		ElseIf hds\isBroken Then
			If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[(Rand(8,21))])
		EndIf
		
		If psp\Health < 1 Then
			If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[7])
		EndIf
	EndIf
	
End Function

Function HealSPPlayer(amount#)
	
	psp\Health = Min(psp\Health + amount, 100.0)
	
End Function

Type MTFDialogue
	Field Enabled%
	Field Timer#
	Field IsPlaying%
	Field CurrentDialogue%
	Field Dialogues%[MTF_DIALOGUE_MAX]
	Field CurrentSequence%
	Field CurrentProgress%
	Field CurrentChannel%
	Field EntityReference%
	Field PrevDialogue%
End Type

Function UpdateMTFDialogue()
	Local value%, n.NPCs, suffix$
	
	If (mtfd\Enabled And psp\Health > 0) Then
		mtfd\Timer = mtfd\Timer - FPSfactor
	EndIf
	
	If mtfd\Timer <= 0.0 Then
		If (Not mtfd\IsPlaying) Then
			mtfd\CurrentProgress = 0
			mtfd\CurrentChannel = 0
			mtfd\PrevDialogue = mtfd\CurrentDialogue
			While mtfd\CurrentDialogue = mtfd\PrevDialogue
				mtfd\CurrentDialogue = Rand(0, MTF_DIALOGUE_MAX-1)
			Wend
			mtfd\CurrentSequence = mtfd\Dialogues[mtfd\CurrentDialogue]
			mtfd\IsPlaying = True
		EndIf
		
		If (Not mtfd\CurrentChannel) Lor (Not ChannelPlaying(mtfd\CurrentChannel)) Then
			value = ((mtfd\CurrentSequence Shr mtfd\CurrentProgress*MTF_DIALOGUE_NUM_OF_BITS) Mod (2^MTF_DIALOGUE_NUM_OF_BITS))
			
			Select value
				Case 1
					suffix = "player"
				Case 2
					suffix = "regular"
				Case 3
					suffix = "medic"
			End Select
			
			If value > 0 Then
				If gopt\GameMode = GAMEMODE_NTF Then
					mtfd\CurrentChannel = PlaySound_Strict(LoadTempSound("SFX\Player\Dialogue\NTF\line_" + (mtfd\CurrentDialogue+1) + "_" + (mtfd\CurrentProgress+1) + "_" + suffix + ".ogg"))
					
					If value = 1 Then
						;Player
						psp\SoundCHN = mtfd\CurrentChannel
					Else
						;NPC
						For n = Each NPCs
							If n\NPCtype = NPC_NTF And n\PrevState = (value-1) Then
								n\SoundChn = mtfd\CurrentChannel
								mtfd\EntityReference = n\Collider
								Exit
							EndIf
						Next
					EndIf
				ElseIf gopt\GameMode = GAMEMODE_DEFAULT Then
					mtfd\CurrentChannel = PlaySound_Strict(LoadTempSound("SFX\Player\Dialogue\line_" + (mtfd\CurrentDialogue+1) + "_" + (mtfd\CurrentProgress+1) + "_player.ogg"))
					
					psp\SoundCHN = mtfd\CurrentChannel
				Else
					mtfd\CurrentChannel = PlaySound_Strict(LoadTempSound("SFX\Player\Dialogue\D\line_" + (mtfd\CurrentDialogue+1) + "_" + (mtfd\CurrentProgress+1) + "_player.ogg"))
					
					psp\SoundCHN = mtfd\CurrentChannel
				EndIf
			Else
				mtfd\Timer = Rand(MTF_DIALOGUE_TIMER_MIN, MTF_DIALOGUE_TIMER_MAX)
				mtfd\IsPlaying = False
			EndIf
			
			mtfd\CurrentProgress = mtfd\CurrentProgress + 1
		EndIf
		
		If ChannelPlaying(mtfd\CurrentChannel) And mtfd\CurrentChannel <> psp\SoundCHN Then
			UpdateSoundOrigin(mtfd\CurrentChannel, Camera, mtfd\EntityReference,10,opt\MasterVol*opt\VoiceVol)
		Else
			ChannelVolume(psp\SoundCHN,opt\MasterVol*opt\VoiceVol)
		EndIf
		
	EndIf
	
End Function

Function PlayNewDialogue(id%, sequence%)
	
	mtfd\Timer = 0.0
	mtfd\CurrentProgress = 0
	mtfd\IsPlaying = True
	mtfd\CurrentDialogue = (id-1)
	mtfd\CurrentSequence = sequence
	
End Function

Function PlayPlayerSPVoiceLine(voiceLine$)
	
	If ChannelPlaying(psp\SoundCHN) Then
		StopChannel(psp\SoundCHN)
	EndIf
	If I_Loc\Localized And FileType(I_Loc\LangPath + voiceLine + ".ogg")=1 Then
		psp\SoundCHN = PlaySound_Strict(LoadTempSound(I_Loc\LangPath + voiceLine + ".ogg"))
	Else
		psp\SoundCHN = PlaySound_Strict(LoadTempSound(voiceLine + ".ogg"))
	EndIf
	
End Function

;Inlcuding the PlayerAnimations file
Include "SourceCode\PlayerAnimations.bb"

;~IDEal Editor Parameters:
;~C#Blitz3D_TSS