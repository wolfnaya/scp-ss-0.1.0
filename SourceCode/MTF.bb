Global KeyHitUse,KeyDownUse

Global SwitchFollow$ = "Split Up"
Global ChatSFXCHN
;Global KEY_CHAT = GetINIInt(gv\OptionFile, "binds", "Chat key", 46)
Global ChatSFXOpened% = False
Global ChatSFXOpenedTimer# = 0.0
Global ChatSFXOpenedColor% = 255
Global ChatSFXOpenedColorFloat# = 255.0
Global ChatSFX_On, ChatSFX_Off
Global ChatSFX_CurrSound

Global BloodSpitSprite1,BloodSpitSprite2

Global KEY_USE = GetINIInt(gv\OptionFile, "binds", "Use key", 18)

Global NTF_AimCross% = GetINIInt(gv\OptionFile, "options", "aim cross", 1)

Global ReplaceIMG%,ReplaceTexture%

Global NTF_ErrorAmount% = 0

Global NTF_457Death,NTF_457Flame

Global flame01,Ash

Global NTF_RadioCHN
Global KEY_RADIOTOGGLE = GetINIInt(gv\OptionFile, "binds", "Radiotoggle key", 20)

;Global NTF_BloodOverlay[2 * 2] UNUSUED, needs porting to array

; ~ General Ambience

Const NTF_MaxAmbienceSFX = 105
Global NTF_AmbienceStrings$[NTF_MaxAmbienceSFX]
;[Block]
NTF_AmbienceStrings[0] = "brief_encounter"
NTF_AmbienceStrings[1] = "friendly_fire"
NTF_AmbienceStrings[2] = "int_bursts"
NTF_AmbienceStrings[3] = "panic"
NTF_AmbienceStrings[4] = "indoor_camera_generic_underground_jolt1"
NTF_AmbienceStrings[5] = "indoor_camera_generic_underground_tremor_high1"
NTF_AmbienceStrings[6] = "indoor_dist_generic_door_kick1"
NTF_AmbienceStrings[7] = "indoor_dist_generic_gunfire_chaotic_pistol1"
NTF_AmbienceStrings[8] = "indoor_dist_generic_gunfire_sustained_pistol1"
NTF_AmbienceStrings[9] = "indoor_dist_generic_gunfire_sustained_smg1"
NTF_AmbienceStrings[10] = "indoor_dist_generic_metal_break1"
NTF_AmbienceStrings[11] = "indoor_dist_generic_metal_scrape1"
NTF_AmbienceStrings[12] = "indoor_dist_generic_metal_scrape2"
NTF_AmbienceStrings[13] = "indoor_dist_generic_metal_stress1"
NTF_AmbienceStrings[14] = "indoor_dist_generic_metal_stress2"
NTF_AmbienceStrings[15] = "indoor_dist_generic_metaldoor_kick1"
NTF_AmbienceStrings[16] = "indoor_far_generic_groan1"
NTF_AmbienceStrings[17] = "indoor_far_generic_groan2"
NTF_AmbienceStrings[18] = "indoor_dist_generic_door_kick1"
NTF_AmbienceStrings[19] = "indoor_dist_generic_explosion_bassy1"
NTF_AmbienceStrings[20] = "indoor_dist_generic_explosion_bassy2"
NTF_AmbienceStrings[21] = "indoor_dist_generic_gunfire_chaotic_ar1"
NTF_AmbienceStrings[22] = "indoor_dist_generic_gunfire_chaotic_pistol2"
NTF_AmbienceStrings[23] = "indoor_dist_generic_gunfire_sustained_ar1"
NTF_AmbienceStrings[24] = "indoor_dist_generic_gunfire_sustained_lmg1"
NTF_AmbienceStrings[25] = "indoor_dist_generic_gunfire_sustained_pistol2"
NTF_AmbienceStrings[26] = "indoor_dist_generic_gunfire_sustained_shotgun1"
NTF_AmbienceStrings[27] = "indoor_dist_generic_gunfire_sustained_smg2"
NTF_AmbienceStrings[28] = "indoor_dist_generic_humanscream_long1"
NTF_AmbienceStrings[29] = "indoor_dist_generic_humanscream_long2"
NTF_AmbienceStrings[30] = "indoor_far_generic_explosion_small1"
NTF_AmbienceStrings[31] = "indoor_far_generic_gunfire_chaotic_pistol1"
NTF_AmbienceStrings[32] = "indoor_far_generic_gunfire_chaotic_smg1"
NTF_AmbienceStrings[33] = "indoor_far_generic_gunfire_chaotic_smg2"
NTF_AmbienceStrings[34] = "indoor_far_generic_gunfire_sustained_ar1"
NTF_AmbienceStrings[35] = "indoor_far_generic_gunfire_sustained_ar2"
NTF_AmbienceStrings[36] = "indoor_far_generic_gunfire_sustained_smg1"
NTF_AmbienceStrings[37] = "indoor_far_generic_gunfire_sustained_smg2"
NTF_AmbienceStrings[38] = "alley_lmg"
For i = 0 To 4
	NTF_AmbienceStrings[39+i] = "int_amb"+(i+1)
Next
For i = 0 To 2
	NTF_AmbienceStrings[44+i] = "scream"+(i+1)
Next
For i = 0 To 2
	NTF_AmbienceStrings[47+i] = "indoor_camera_generic_underground_tremor"+(i+1)
Next
For i = 0 To 6
	NTF_AmbienceStrings[50+i] = "indoor_dist_generic_metal_strike"+(i+1)
Next
For i = 0 To 3
	NTF_AmbienceStrings[57+i] = "indoor_dist_generic_metal_squeak"+(i+1)
Next
For i = 0 To 3
	NTF_AmbienceStrings[61+i] = "indoor_dist_generic_metal_strikeshort"+(i+1)
Next
For i = 0 To 2
	NTF_AmbienceStrings[65+i] = "indoor_far_generic_gunfire_sustained_pistol"+(i+1)
Next
For i = 0 To 4
	NTF_AmbienceStrings[68+i] = "indoor_far_generic_moan"+(i+1)
Next
For i = 0 To 4
	NTF_AmbienceStrings[73+i] = "indoor_far_generic_snarl"+(i+1)
Next
For i = 0 To 3
	NTF_AmbienceStrings[78+i] = "indoor_camera_generic_lights_flicker"+(i+1)
Next
For i = 0 To 2
	NTF_AmbienceStrings[81+i] = "indoor_dist_generic_howl"+(i+1)
Next
For i = 0 To 2
	NTF_AmbienceStrings[84+i] = "indoor_dist_generic_humanscream_short"+(i+1)
Next
For i = 0 To 3
	NTF_AmbienceStrings[87+i] = "indoor_dist_generic_roar"+(i+1)
Next
For i = 0 To 4
	NTF_AmbienceStrings[91+i] = "indoor_far_generic_explosion_med"+(i+1)
Next
For i = 0 To 2
	NTF_AmbienceStrings[96+i] = "indoor_far_generic_gunfire_sustained_pistol"+(i+1)
Next
NTF_AmbienceStrings[99] = "2scream"
NTF_AmbienceStrings[100] = "2scream2"
For i = 0 To 2
	NTF_AmbienceStrings[101+i] = "Boom"+(i+1)
Next
NTF_AmbienceStrings[104] = "LowMoan"
;[End Block]

; ~ Entrance Zone Ambience

Const NTF_MaxEZAmbience% = 3
Global NTF_EZAmbienceStrings$[NTF_MaxEZAmbience]
;[Block]
NTF_EZAmbienceStrings[0] = "Chatter4"
NTF_EZAmbienceStrings[1] = "containmentbreachreaction1"
NTF_EZAmbienceStrings[2] = "OhGod"
;[End Block]

; ~ Heavy Containment Zone Ambience

Const NTF_MaxHCZAmbience% = 6
Global NTF_HCZAmbienceStrings$[NTF_MaxHCZAmbience]
;[Block]
NTF_HCZAmbienceStrings[0] = "008death1"
NTF_HCZAmbienceStrings[1] = "008death2"
NTF_HCZAmbienceStrings[2] = "BigDoorClose2"
;NTF_HCZAmbienceStrings[3] = "BigDoorOpen"
NTF_HCZAmbienceStrings[3] = "Cough1"
NTF_HCZAmbienceStrings[4] = "Damage2"
NTF_HCZAmbienceStrings[5] = "Damage5"
;[End Block]

; ~ Light Containment Zone Ambience

Const NTF_MaxLCZAmbience% = 2
Global NTF_LCZAmbienceStrings$[NTF_MaxLCZAmbience]
;[Block]
;NTF_LCZAmbienceStrings[0] = "ElevatorCrash"
NTF_LCZAmbienceStrings[0] = "muffle"
;NTF_LCZAmbienceStrings[1] = "Spooky"
;NTF_LCZAmbienceStrings[2] = "StuckInAirlock2"
NTF_LCZAmbienceStrings[1] = "troublewithdoors"
;[End Block]

;Random Sound Event Ambience TODO UNUSUED RN
Const NTF_RES_Max = 11
Global NTF_RandomEventSound$[NTF_RES_Max]
;[Block]
NTF_RandomEventSound[0] = "7_classD_finished_off.ogg"
NTF_RandomEventSound[1] = "8_guard_is_compromised_by_loud_radio.ogg"
NTF_RandomEventSound[2] = "classD_pretends_tosurrender_but_doesnt.ogg"
NTF_RandomEventSound[3] = "grenadelauncher_panicked_discharge.ogg"
NTF_RandomEventSound[4] = "guard_squads_meet_each_other.ogg"
NTF_RandomEventSound[5] = "guards_accidental_friendly_fire_scientist.ogg"
NTF_RandomEventSound[6] = "guards_squad_attacked_bySCP_one_survivor.ogg"
NTF_RandomEventSound[7] = "guards_use_a_grenade.ogg"
NTF_RandomEventSound[8] = "mtf_breach_door.ogg"
NTF_RandomEventSound[9] = "mtf_finds_bodies.ogg"
NTF_RandomEventSound[10] = "mtf_prepares_to_apprehend_SCP.ogg"
;[End Block]

; ~ Intro Ambience

Const NTF_MaxIntroAmbienceSFX = 41
Global NTF_IntroAmbienceStrings$[NTF_MaxIntroAmbienceSFX]
;[Block]
For i = 0 To 6
	NTF_IntroAmbienceStrings[i] = "outdoor_helo_generic_pass_dist_single"+(i+1)
Next
For i = 0 To 2
	NTF_IntroAmbienceStrings[7+i] = "outdoor_helo_generic_pass_dist_squadron"+(i+1)
Next
For i = 0 To 4
	NTF_IntroAmbienceStrings[10+i] = "outdoor_helo_generic_pass_far_double"+(i+1)
Next
For i = 0 To 8
	NTF_IntroAmbienceStrings[15+i] = "outdoor_helo_generic_pass_far_single"+(i+1)
Next
For i = 0 To 4
	NTF_IntroAmbienceStrings[24+i] = "outdoor_helo_generic_pass_xfar_double"+(i+1)
Next
For i = 0 To 6
	NTF_IntroAmbienceStrings[29+i] = "outdoor_helo_generic_pass_xfar_single"+(i+1)
Next
For i = 0 To 4
	NTF_IntroAmbienceStrings[36+i] = "outdoor_helo_generic_patrol_dist_long"+(i+3)
Next
;[End Block]

; ~ Sewer Ambience

Const NTF_MaxSewerAmbienceSFX = 9
Global NTF_SewerAmbienceStrings$[NTF_MaxSewerAmbienceSFX]
;[Block]
NTF_SewerAmbienceStrings[0] = "BangDoorMetalSCP"
NTF_SewerAmbienceStrings[1] = "Breathe"
NTF_SewerAmbienceStrings[2] = "explosiveprojectilefallin"
NTF_SewerAmbienceStrings[3] = "ImHereSewerScp"
NTF_SewerAmbienceStrings[4] = "NearYou"
NTF_SewerAmbienceStrings[5] = "Sewers1"
NTF_SewerAmbienceStrings[6] = "Sewers2"
NTF_SewerAmbienceStrings[7] = "Tunnels1"
NTF_SewerAmbienceStrings[8] = "Tunnels2"
;[End Block]

Global NTF_GasMaskBlood

Global GunPitchShift% = GetINIInt(gv\OptionFile, "options", "gun sfx pitch", 1)

Global NTF_AchvMenuScroll# = 0.0

Const ClrR = 50, ClrG = 50, ClrB = 50

Global Contain173State% = 0
Global Contain173Timer# = 0.0
Global Contain173_SoundPlayed% = False

Const HIT_INTRO_HELI% = 6

Global IsPlayerSprinting% = False

Global SelectedElevatorFloor% = 0
Global CurrElevatorButtonTex%[3]
Global NewElevatorMoveSFX1, NewElevatorMoveSFX2
Global SelectedElevatorEvent

Global UpdateAlarmLight% = False
Global NTF_DisableLight = False

Global MTF_CameraCheckTimer% = 0
Global MTF_CameraCheckDetected% = False

Global GasMaskOverlay2

Global CheckPointDoorObj

Global EntityMapLoading% = 0

Global WaterParticleTexture%[2]

Global SaveTexturesInVRam = GetINIInt(gv\OptionFile,"options","enable vram",1)

;Cheat Menu Variables
Global NTF_SmallHead% = False
Global NTF_FlipGuns% = False

;Ingame Controls
Global CK_Blink = 2		;Cross
Global CK_Use = 6		;R1
Global CK_LMouse = 8	;R2
Global CK_RMouse = 7	;L2
Global CK_MMouse = 5	;L1
Global CK_Sprint = 11	;L3
Global CK_Crouch = 12	;R3
Global CK_Inv = 14		;Touchpad
Global CK_Pause = 10	;OPTIONS
Global CK_Save = 9		;SHARE
Global CK_Reload = 1	;Square
Global CK_Chat = 3		;Circle
Global CK_Radio = 4		;Triangle
;Menu Controls
Global CKM_Press = 2	;Cross
Global CKM_Back = 3		;Circle
Global CKM_Next = 6		;R1
Global CKM_Prev = 5		;L1

Global FOV% = GetINIInt(gv\OptionFile, "options", "fov", 60)

Type CubeMap
	Field Name$
	Field Texture%
	Field Cam%
	Field CamOverlay%
	Field RenderTimer%
	Field RenderY#
	Field Position#[3]
	Field FollowsCamera%
End Type

Global MapCubeMap.CubeMap

Function LoadModStuff()
	Local temp#,i
	
	CreateMainPlayer()
	CreateDamageOverlay()
	
	ChatSFX_On = LoadSound_Strict("SFX\Player\RadioOn.ogg")
	ChatSFX_Off = LoadSound_Strict("SFX\Player\RadioOff.ogg")
	
	ChatSFX_CurrSound = 0
	
	If gopt\GameMode = GAMEMODE_DEFAULT Then
		For i = 0 To 3
			NTF_PainSFX[i]=LoadSound_Strict("SFX\player\Ryan\pain_"+(i+1)+".ogg")
		Next
		For i = 0 To 1
			NTF_PainWeakSFX[i]=LoadSound_Strict("SFX\player\Ryan\pain_weak_"+(i+1)+".ogg")
		Next
	Else
		For i = 0 To 7
			NTF_PainSFX[i]=LoadSound_Strict("SFX\player\pain"+(i+1)+".ogg")
		Next
		For i = 0 To 1
			NTF_PainWeakSFX[i]=LoadSound_Strict("SFX\player\painweak"+(i+1)+".ogg")
		Next
	EndIf
	
	NTF_RadioCHN = 3
	
	Contain173State% = 0
	Contain173Timer# = 0.0
	
	GasMaskOverlay2 = LoadSprite("GFX\Gasmask_Overlay.jpg",1,ark_blur_cam)
	ScaleSprite GasMaskOverlay2,1.0,Float(opt\GraphicHeight)/Float(opt\GraphicWidth)
	EntityBlend (GasMaskOverlay2, 2)
	EntityFX(GasMaskOverlay2, 1)
	EntityOrder GasMaskOverlay2, -2000
	MoveEntity(GasMaskOverlay2, 0, 0, 1.0)
	ShowEntity(GasMaskOverlay2)
	
	CheckPointDoorObj = LoadMesh_Strict("GFX\checkpointdoors.b3d")
	HideEntity CheckPointDoorObj
	
	MTFSFX=LoadSound_Strict("SFX\Character\MTF\Beep.ogg")
	
	WaterParticleTexture[0] = LoadTexture_Strict("GFX\WaterParticle.png",1+2,2)
	WaterParticleTexture[1] = LoadTexture_Strict("GFX\WaterParticle2.png",1+2,2)
	
	PreloadAllNPCAnimations()
	
	MapCubeMap = CreateCubeMap("MapCubeMap")
	
	NEI = New NewElevatorInstance
	NEI\button_number_tex[0] = LoadAnimTexture("GFX\map\Elevator_HUD.png",1,64,64,0,3)
	NEI\button_number_tex[1] = LoadAnimTexture("GFX\map\Elevator_HUD_Zones.png",1,64,64,0,3)
	NEI\button_number_tex[2] = LoadAnimTexture("GFX\map\Elevator_HUD_Zones_2.png",1,64,64,0,3)
	
	MonitorTexture = LoadTexture_Strict("GFX\MonitorOverlay.jpg",1,1)
	
	InitGuns()
	
	CreateCommunicationAndSocialWheel()
	
End Function

Function DeleteModStuff()
	Local vf.VolumeFog,i
	
	DestroyMainPlayer()
	
	For i = 0 To 7
		FreeSound_Strict NTF_PainSFX[i] : NTF_PainSFX[i] = 0
	Next
	For i = 0 To 1
		FreeSound_Strict NTF_PainWeakSFX[i] : NTF_PainWeakSFX[i] = 0
	Next
	
	If NewElevatorMoveSFX1 <> 0 Then FreeSound_Strict NewElevatorMoveSFX1 : NewElevatorMoveSFX1 = 0
	If NewElevatorMoveSFX2 <> 0 Then FreeSound_Strict NewElevatorMoveSFX2 : NewElevatorMoveSFX2 = 0
	
	Delete Each NewElevator
	Delete Each HitBox
	Delete Each CubeMap
	MapCubeMap = Null
	
	Delete NEI
	
	DeleteGuns()
	
	Delete CurrGrid
	
End Function

Type BloodSpit
	Field obj%
	Field x#,y#,z#
	Field KillTimer#
	Field DirectionRAND
End Type

Function CreateBloodSpit.BloodSpit(x#,y#,z#)
	Local BS.BloodSpit = New BloodSpit
	
	Local random% = Rand(0,1)
	If random%=0
		BS\obj = CopyEntity(BloodSpitSprite1)
	Else
		BS\obj = CopyEntity(BloodSpitSprite2)
	EndIf
	ScaleSprite BS\obj,Rand(0.1,0.25),Rand(0.1,0.25)
	BS\x# = x#
	BS\y# = y#
	BS\z# = z#
	PositionEntity BS\obj,x#,y#,z#
	BS\DirectionRAND = Rand(0,3)
	
	Return BS
End Function

Function UpdateBloodSpit()
	Local BS.BloodSpit
	
	For BS = Each BloodSpit
		If BS\KillTimer# < 2000.0
			BS\KillTimer# = BS\KillTimer# + FPSfactor#
			Select BS\DirectionRAND
				Case 0
					MoveEntity BS\obj,0,0,0.1
				Case 1
					MoveEntity BS\obj,0.1,0,0
				Case 2
					MoveEntity BS\obj,0,0,-0.1
				Case 3
					MoveEntity BS\obj,-0.1,0,0
			End Select
			TranslateEntity BS\obj,0,-0.1*FPSfactor,0
		Else
			BS\obj = FreeEntity_Strict(BS\obj)
			Delete BS
		EndIf
	Next
	
End Function

Type flame
	Field ent
	Field ang#
	Field size#
	Field alph#
	Field dis#
	Field dx#, dy#, dz#
End Type

Type fire
	Field piv
	Field dx#, dy#, dz#
	Field flag$
End Type

Type ash_particle
	Field ent
	Field alpha#
	Field dx#,dy#,dz#
	Field pop
End Type

Function Add_flame(x#,y#,z#,size#=1,dis#=.016,dx#=0,dy#=0.3,dz#=0)
	Local a.flame=New flame
	a\ent=CopyEntity(flame01)
	PositionEntity a\ent,x,y,z
	a\alph=1
	a\size=size
	a\dis=dis
	a\ang=Rnd(360)
	ScaleSprite a\ent,a\size,a\size
	EntityColor a\ent,Rnd(150,255),Rnd(0,100),0
	a\dx=dx
	a\dy=dy
	a\dz=dz
End Function

Function Update_flames()
	Local a.flame
	For a.flame=Each flame
		If a\alph>0.01 Then
			a\alph=a\alph-a\dis
			EntityAlpha a\ent,a\alph
			RotateSprite a\ent,a\ang
			a\ang=a\ang+.2
			MoveEntity a\ent,a\dx,a\dy,a\dz
		Else
			a\ent = FreeEntity_Strict(a\ent)
			Delete a
		EndIf
	Next
End Function

Function Erase_flames()
	Local a.flame
	For a.flame=Each flame
		a\ent = FreeEntity_Strict(a\ent)
	Next
	Delete Each flame
End Function

Function Update_Fires()
	Local a.fire
	For a.fire=Each fire
		Add_flame(EntityX(a\piv)+Rnd(-0.1,0.1),EntityY(a\piv),EntityZ(a\piv)+Rnd(-0.1,0.1),Rnd(0.2,0.6),.04,a\dx,a\dy,a\dz)
		Add_flame(EntityX(a\piv)+Rnd(-0.1,0.1),EntityY(a\piv),EntityZ(a\piv)+Rnd(-0.1,0.1),Rnd(0.2,0.6),.04,a\dx,a\dy,a\dz)
		Add_flame(EntityX(a\piv)+Rnd(-0.1,0.1),EntityY(a\piv),EntityZ(a\piv)+Rnd(-0.1,0.1),Rnd(0.2,0.6),.04,a\dx,a\dy,a\dz)
		Add_flame(EntityX(a\piv)+Rnd(-0.1,0.1),EntityY(a\piv),EntityZ(a\piv)+Rnd(-0.1,0.1),Rnd(0.2,0.6),.04,a\dx,a\dy,a\dz)
		Add_flame(EntityX(a\piv)+Rnd(-0.1,0.1),EntityY(a\piv),EntityZ(a\piv)+Rnd(-0.1,0.1),Rnd(0.2,0.6),.04,a\dx,a\dy,a\dz)
		Add_flame(EntityX(a\piv)+Rnd(-0.1,0.1),EntityY(a\piv),EntityZ(a\piv)+Rnd(-0.1,0.1),Rnd(0.2,0.6),.04,a\dx,a\dy,a\dz)
	Next
	Update_flames()
End Function

Function Erase_Fires()
	Local a.fire
	For a.fire=Each fire
		a\piv = FreeEntity_Strict(a\piv)
	Next
	Delete Each fire
End Function

Function Add_Fire.fire(x#,y#,z#,dx#=0,dy#=.05,dz#=0,flag$="")
	Local a.fire
	a.fire=New fire
	a\piv=CreatePivot()
	PositionEntity a\piv,x,y,z
	a\dx=dx:a\dy=dy:a\dz=dz
	a\flag$ = flag$
	Return a
End Function

Function Add_AshParticle(x#,y#,z#,r=255,g=255,b=255)
	Local a.ash_particle=New ash_particle
	a\ent=CopyEntity(Ash)
	PositionEntity a\ent,x,y,z
	a\dx=Rnd(-.01,.01)
	a\dy=Rnd(0.01,.07)
	a\dz=Rnd(-.01,.01)
	ScaleSprite a\ent,Rnd(.01,.02),Rnd(.01,.02)
	a\alpha=1
	a\pop=False
	EntityColor a\ent,r,g,b
End Function

Function Update_AshParticles()
	Local a.ash_particle
	For a.ash_particle = Each ash_particle
		MoveEntity a\ent,a\dx,a\dy,a\dz
		If EntityY(a\ent)<.3 Then 
			a\dy=-a\dy
			a\dy=a\dy*.62
			a\pop=True
		End If
		a\dy=a\dy-.02
		
		If a\pop Then
			a\alpha=a\alpha-.02
			EntityAlpha a\ent,a\alpha
			If a\alpha<0.05 Then
				a\ent = FreeEntity_Strict(a\ent)
				Delete a
			EndIf
		EndIf
	Next
End Function

Function Erase_Particles()
	Local a.ash_particle
	For a.ash_particle = Each ash_particle
		a\ent = FreeEntity_Strict(a\ent)
	Next
	Delete Each ash_particle
End Function

Type MapProps
	Field obj%
End Type

Function UpdateMapProps()
	Local a.MapProps
	For a.MapProps = Each MapProps
		If a\obj<>0
			If EntityDistanceSquared(a\obj,Camera)<PowTwo(10.0)
				If EntityVisible(a\obj,Camera)
					ShowEntity a\obj
				Else
					HideEntity a\obj
				EndIf
			Else
				HideEntity a\obj
			EndIf
		EndIf
	Next
	
End Function

Type SplashText
	Field Timer#
	Field Speed#
	Field ShowTime#
	Field CurrentLength%
	Field DisplayAmount#
	Field X#, Y#
	Field Txt$
	Field Font%
	Field Centered%
	Field isSound%
	Field R#,G#,B#
End Type

Function CreateSplashText.SplashText(txt$,x#,y#,displayamount#,speed#=1,font%=Font_Digital_Large,centered%=False,issound%=True,r#=255,g#=255,b#=255)
	Local st.SplashText = New SplashText
	
	st\Txt = txt
	st\X = x
	st\Y = y
	st\DisplayAmount = displayamount
	st\Font = font
	st\Centered = centered
	st\isSound = issound
	st\Speed = speed
	st\R# = r
	st\G# = g
	st\B# = b
	
	Return st
End Function

Function UpdateSplashTexts()
	Local st.SplashText
	
	For st = Each SplashText
		If st\CurrentLength < Len(st\Txt) Then
			If st\Timer < 10.0
				st\Timer = st\Timer + FPSfactor * st\Speed
			Else
				st\CurrentLength = st\CurrentLength + 1
				If st\isSound Then
					PlaySound_Strict SplashTextSFX[Rand(0,2)]
				EndIf
				st\Timer = 0.0
			EndIf
		Else
			If st\Timer < st\DisplayAmount+255.0 Then
				st\Timer = Min(st\Timer+FPSfactor,st\DisplayAmount+255.0)
			Else
				Delete st
			EndIf
		EndIf
	Next
	
End Function

Function DrawSplashTexts()
	Local st.SplashText
	Local amount% = 0
	
	For st = Each SplashText
		SetFont fo\Font[st\Font]
		
		If st\CurrentLength < Len(st\Txt)
			st\R = st\R
			st\G = st\G
			st\B = st\B
		Else
			st\R = Min((st\DisplayAmount+255.0)-st\Timer,st\R)
			st\G = Min((st\DisplayAmount+255.0)-st\Timer,st\G)
			st\B = Min((st\DisplayAmount+255.0)-st\Timer,st\B)
		EndIf
		
		Color st\R,st\G,st\B
		
		If (Not st\Centered) Then
			Text st\X,st\Y+(32*amount),Left(st\Txt,st\CurrentLength)
		Else
			Text st\X-StringWidth(st\Txt)/2,st\Y+(32*amount)-StringHeight(st\Txt)/2,Left(st\Txt,st\CurrentLength)
		EndIf
		
		amount = amount + 1
	Next
	
End Function

Type CoordPoints
	Field name$
	Field obj%
End Type

Function CreateCoordPoint.CoordPoints(name$,x#,y#,z#,parent%=0,pos%=False)
	Local cdp.CoordPoints = New CoordPoints
	
	cdp\name$ = name$
	cdp\obj% = CreatePivot(parent%)
	PositionEntity cdp\obj%,x#,y#,z#,pos%
	
	Return cdp
End Function

Function DeleteCoordPoint(cdp.CoordPoints)
	
	cdp\obj = FreeEntity_Strict(cdp\obj)
	Delete cdp
	
End Function

Function Between(a#,value#,b#)
	
	If a# > value#
		Return a#
	ElseIf b# < value#
		Return b#
	Else
		Return value#
	EndIf
	
End Function

Global NTF_SprintPitch# = 0.0
Global NTF_SprintPitchSide% = 0

Function UpdateSprint()
	
	If IsPlayerSprinting%
		If NTF_SprintPitchSide% = 0
			If NTF_SprintPitch# < 0.5
				NTF_SprintPitch# = NTF_SprintPitch + (0.1*FPSfactor)
			Else
				NTF_SprintPitchSide% = 1
			EndIf
		Else
			If NTF_SprintPitch# > -0.5
				NTF_SprintPitch# = NTF_SprintPitch - (0.1*FPSfactor)
			Else
				NTF_SprintPitchSide% = 0
			EndIf
		EndIf
	Else
		If NTF_SprintPitchSide% = 0
			NTF_SprintPitch# = Min(0,NTF_SprintPitch#+(0.1*FPSfactor))
		Else
			NTF_SprintPitch# = Max(0,NTF_SprintPitch#-(0.1*FPSfactor))
		EndIf
	EndIf
	
End Function

Global Credits_LineAmount% = 0
Type Credits_Lines
	Field txt$
End Type

Type VolumeFog
	Field obj%
	Field deadTime#
End Type

Function UpdateAlarmRotor(OBJ,rotation#)
	
	TurnEntity OBJ,0,-rotation*FPSfactor,0
	
End Function

Function RelightRoom(r.Rooms,tex%,oldtex%)
	Local mesh=GetChild(r\obj,2)
	Local surf%,brush%,tex2%,texname$,temp%,temp2%
	Local comparison$ = StripPath(TextureName(oldtex))
	Local i
	temp=(BumpEnabled+1)
	For i=1 To CountSurfaces(mesh)
		temp2=temp
		surf=GetSurface(mesh,i)
		brush=GetSurfaceBrush(surf)
		If brush<>0 Then
			tex2=GetBrushTexture(brush,temp2)
			If tex2=0 And temp2>1 Then tex2=GetBrushTexture(brush,0) : temp2=1
			If tex2<>0 Then
				texname=TextureName(tex2)
				If StripPath(texname)=comparison Then
					BrushTexture brush,tex,0,temp
					PaintSurface surf,brush
				EndIf
				DeleteSingleTextureEntryFromCache tex2
			EndIf
			FreeBrush brush
		EndIf
	Next
End Function

Function CreateCubeMap.CubeMap(Name$,CubeMapMode%=1,FollowsCamera%=True,PosX#=0.0,PosY#=0.0,PosZ#=0.0,RenderY#=0.0,TexSize%=256)
	Local cm.CubeMap = New CubeMap
	
	cm\Name = Name$
	cm\RenderY = RenderY#
	
	cm\Texture=CreateTextureUsingCacheSystem(TexSize,TexSize,128+256)
	TextureBlend cm\Texture,3
	cm\Cam=CreateCamera()
	CameraFogMode cm\Cam,1
	CameraFogRange cm\Cam,5,10
	CameraRange cm\Cam,0.01,20
	CameraClsMode cm\Cam,False,True
	CameraProjMode cm\Cam,0
	CameraViewport cm\Cam,0,0,TexSize,TexSize
	cm\CamOverlay=CreateSprite(cm\Cam)
	EntityFX cm\CamOverlay,1
	EntityColor cm\CamOverlay,0,0,0
	EntityOrder cm\CamOverlay,-1000
	ScaleSprite cm\CamOverlay,5,5
	MoveEntity cm\CamOverlay,0,0,0.1
	EntityAlpha cm\CamOverlay,0.0
	HideEntity cm\CamOverlay
	
	SetCubeMode cm\Texture,CubeMapMode
	
	cm\FollowsCamera = FollowsCamera
	If cm\FollowsCamera = False Then
		cm\Position[Vector_X] = PosX#
		cm\Position[Vector_Y] = PosY#
		cm\Position[Vector_Z] = PosZ#
	EndIf
	
	Return cm
End Function

Function RenderCubeMap(entity%,name$)
	Local cm.CubeMap
	Local tex_sz%
	
	If opt\RenderCubeMapMode=0 Then Return
	
	For cm = Each CubeMap
		If cm\Name = name$ Then
			tex_sz=TextureWidth(cm\Texture)
			CameraProjMode Camera,0
			CameraProjMode cm\Cam,1
			If entity<>0 Then
				HideEntity entity
			EndIf
			HideEntity g_I\GunPivot
			ShowEntity cm\CamOverlay
			EntityAlpha cm\CamOverlay,0.7
			CameraFogRange cm\Cam,0.1,3
			CameraFogColor cm\Cam,5,20,3
			If opt\RenderCubeMapMode = 2 Then
				SetCubeFace cm\Texture,0
				RotateEntity cm\Cam,0,90,0
				RenderWorld
				CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
				SetCubeFace cm\Texture,1
				RotateEntity cm\Cam,0,0,0
				RenderWorld
				CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
				SetCubeFace cm\Texture,2
				RotateEntity cm\Cam,0,-90,0
				RenderWorld
				CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
				SetCubeFace cm\Texture,3
				RotateEntity cm\Cam,0,180,0
				RenderWorld
				CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
				SetCubeFace cm\Texture,4
				RotateEntity cm\Cam,-90,0,0
				RenderWorld
				CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
				SetCubeFace cm\Texture,5
				RotateEntity cm\Cam,90,0,0
				RenderWorld
				CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
			ElseIf opt\RenderCubeMapMode = 1 Then
				If cm\RenderTimer <= 0 Then
					SetCubeFace cm\Texture,0
					RotateEntity cm\Cam,0,90,0
					RenderWorld
					SetCubeFace cm\Texture,1
					RotateEntity cm\Cam,0,0,0
					RenderWorld
					CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
					cm\RenderTimer = 1
				ElseIf cm\RenderTimer = 1 Then
					SetCubeFace cm\Texture,2
					RotateEntity cm\Cam,0,-90,0
					RenderWorld
					CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
					SetCubeFace cm\Texture,3
					RotateEntity cm\Cam,0,180,0
					RenderWorld
					CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
					cm\RenderTimer = 2
				ElseIf cm\RenderTimer = 2 Then
					SetCubeFace cm\Texture,4
					RotateEntity cm\Cam,-90,0,0
					RenderWorld
					CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
					SetCubeFace cm\Texture,5
					RotateEntity cm\Cam,90,0,0
					RenderWorld
					CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(cm\Texture)
					cm\RenderTimer = 0
				EndIf
			EndIf	
			If entity<>0 Then
				ShowEntity entity
			EndIf
			ShowEntity g_I\GunPivot
			EntityAlpha cm\CamOverlay,0.0
			CameraFogRange cm\Cam,5,10
			CameraFogColor cm\Cam,0,0,0
			HideEntity cm\CamOverlay
			CameraProjMode Camera,1
			CameraProjMode cm\Cam,0
			Exit
		EndIf
	Next
	
End Function

Function UpdateCubeMap(entity%,name$)
	Local cm.CubeMap
	Local camoffsety#
	
	If opt\RenderCubeMapMode=0 Then Return
	
	For cm = Each CubeMap
		If cm\Name = name$
			If cm\FollowsCamera Then
				If entity<>0 Then
					camoffsety#=(EntityY(Camera,True)-(EntityY(entity,True)+cm\RenderY))
					PositionEntity cm\Cam,EntityX(Camera,True),(EntityY#(entity,True)+cm\RenderY)-camoffsety,EntityZ(Camera,True)
				Else
					camoffsety#=(EntityY(Camera,True)+cm\RenderY)
					PositionEntity cm\Cam,EntityX(Camera,True),cm\RenderY-camoffsety,EntityZ(Camera,True)
				EndIf
			Else
				PositionEntity cm\Cam,cm\Position[Vector_X],cm\Position[Vector_Y],cm\Position[Vector_Z]
			EndIf
			If entity<>0 Then
				ShowEntity entity
			EndIf
			Exit
		EndIf
	Next
	
End Function

Global PlayerInNewElevator% = False
Global PlayerNewElevator% = 0

Type NewElevatorInstance
	Field button_number_tex[3]
End Type

Global NEI.NewElevatorInstance

Type NewElevator
	Field obj%
	Field state#
	Field currfloor%
	Field tofloor%
	Field door.Doors
	Field floory#[3]
	Field ID#
	Field speed#=6
	Field CurrSpeed#
	Field sound%
	Field soundchn%
	Field currsound%
	Field room.Rooms
	Field button_arrow[2]
	Field button_numbers
	Field floorlocked[3]
	Field isStorage%
End Type

Function CreateNewElevator.NewElevator(Obj%, CurrFloor%, door.Doors, ID#,r.Rooms,floor1y#,floor2y#,floor3y#=0.0,speed#=7.0,isStorage%=False)
	Local tex%
	Local ne.NewElevator = New NewElevator
	Local i
	
	ne\room = r
	ne\obj = Obj
	ne\currfloor = CurrFloor
	ne\tofloor = CurrFloor
	ne\state = 0.0
	ne\door = door
	ne\floory[0] = floor1y
	ne\floory[1] = floor2y
	ne\floory[2] = floor3y
	ne\speed = speed
	ne\ID = ID
	ne\isStorage = isStorage
	tex = LoadTextureCheckingIfInCache("GFX\Map\elev_arrow.png")
	ne\button_arrow[0] = CreateSprite(ne\door\buttons[0])
	ne\button_arrow[1] = CopyEntity(ne\button_arrow[0],ne\door\buttons[0])
	For i = 0 To 1
		ScaleSprite ne\button_arrow[i],10,10
		SpriteViewMode ne\button_arrow[i],2
		EntityTexture ne\button_arrow[i],tex
		EntityFX ne\button_arrow[i],1
	Next
	PositionEntity ne\button_arrow[0],-10,285,0.8
	PositionEntity ne\button_arrow[1],14,285,0
	TurnEntity ne\button_arrow[1],0,0,180
	DeleteSingleTextureEntryFromCache tex
	ne\button_numbers = CreateSprite(ne\door\buttons[0])
	ScaleSprite ne\button_numbers,15,15
	SpriteViewMode ne\button_numbers,2
	PositionEntity ne\button_numbers,2,250,0
	EntityFX ne\button_numbers,1
	
	Return ne
End Function

Function StartNewElevator(door.Doors, Newfloor%)
	Local ne.NewElevator, ne_found.NewElevator
	Local playerinside% = False
	
	For ne = Each NewElevator
		If ne\door = door
			ne_found = ne
			Exit
		EndIf
	Next
	
	If ne\door\locked Then
		CreateMsg(GetLocalString("Doors", "door_locked"))
	ElseIf ne\floorlocked[Newfloor - 1] Then
		CreateMsg(GetLocalString("Doors", "door_nothing"))
	Else
		If Newfloor <> ne\currfloor
			ne\tofloor = Newfloor
			If Abs(EntityX(Collider)-EntityX(ne\obj,True))<=280.0*RoomScale+(0.015*FPSfactor)
				If Abs(EntityZ(Collider)-EntityZ(ne\obj,True))<=280.0*RoomScale+(0.015*FPSfactor)
					;debuglog "In Elevator"
					PlayerInNewElevator = True
					PlayerNewElevator = ne\ID
					playerinside = True
				EndIf
			EndIf
			If (Not playerinside)
				CreateMsg(GetLocalString("Doors", "elevator_called"))
			Else
				UseDoor(ne\door)
			EndIf
		Else
			CreateMsg(GetLocalString("Doors", "elevator_on_floor"))
		EndIf
	EndIf
	
End Function

Function UpdateNewElevators()
	Local ne.NewElevator
	Local n.NPCs,it.Items
	Local i%
	
	For ne = Each NewElevator
		
		If PlayerRoom\RoomTemplate\Name = "checkpoint_rcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_hcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_bcz" Lor PlayerRoom\RoomTemplate\Name = "core_lcz" Lor PlayerRoom\RoomTemplate\Name = "core_hcz" Lor PlayerRoom\RoomTemplate\Name = "core_ez" Then
			If PlayerInNewElevator Then
				If opt\ElevatorMusicEnabled Then
					ShouldPlay = Rand(MUS_ELEVATOR,MUS_ELEVATOR_3)
				Else
					ShouldPlay = MUS_NULL
				EndIf
			EndIf
		EndIf
		
		If ne\tofloor <> ne\currfloor Then
			If ne\state < 200.0 Then
				ne\state = ne\state + FPSfactor
			Else
				If ne\currsound=0 Then
					If ne\soundchn <> 0 Then StopStream_Strict(ne\soundchn)
					
					If (Not PlayerInNewElevator) Then
						
					Else
						If PlayerRoom\RoomTemplate\Name = "checkpoint_rcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_hcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_bcz" Then
							ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Checkpoint\StartAndLoop.ogg",opt\SFXVolume*0.5,0)
						Else
							ne\soundchn = StreamSound_Strict("SFX\General\Elevator\StartAndLoop"+Rand(1,3)+".ogg",opt\SFXVolume*0.5,0)
						EndIf
					EndIf
					ne\currsound = 1
				Else
					If ne\currsound = 1 Lor (ne\currsound = 2 And (Not IsStreamPlaying_Strict(ne\soundchn))) Then
						If (Not IsStreamPlaying_Strict(ne\soundchn))
							StopStream_Strict(ne\soundchn)
							ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Loop"+Rand(1,3)+".ogg",opt\SFXVolume*0.5,Mode)
							ne\currsound = 2
						EndIf
					EndIf
				EndIf
				If ne\tofloor < ne\currfloor Then
					ShowEntity ne\button_arrow[1]
					HideEntity ne\button_arrow[0]
					If PlayerRoom\RoomTemplate\Name = "core_lcz" Lor PlayerRoom\RoomTemplate\Name = "core_hcz" Lor PlayerRoom\RoomTemplate\Name = "core_ez" Then
						If ne\currfloor = 3 And ne\tofloor = 1 Then
							If EntityY(ne\obj) < ne\floory[1] Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[1],1
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[1],2
							EndIf
						Else
							If ne\currfloor = 3 Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[1],2
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[1],1
							EndIf
						EndIf
					ElseIf PlayerRoom\RoomTemplate\Name = "checkpoint_rcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_hcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_bcz" Then
						If ne\currfloor = 3 And ne\tofloor = 1 Then
							If EntityY(ne\obj) < ne\floory[1] Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[2],1
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[2],2
							EndIf
						Else
							If ne\currfloor = 3 Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[2],2
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[2],1
							EndIf
						EndIf
					Else
						If ne\currfloor = 3 And ne\tofloor = 1 Then
							If EntityY(ne\obj) < ne\floory[1] Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[0],1
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[0],2
							EndIf
						Else
							If ne\currfloor = 3 Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[0],2
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[0],1
							EndIf
						EndIf
					EndIf
					If EntityY(ne\obj) > ne\floory[ne\tofloor-1] Then
						If EntityY(ne\obj) > ne\floory[ne\tofloor-1] + (0.6/RoomScale) Then
							ne\CurrSpeed = CurveValue(ne\speed,ne\CurrSpeed,75.0)
						Else
							If ne\isStorage Then
								ne\CurrSpeed = CurveValue(0.05,1.5,25.0)
							Else
								ne\CurrSpeed = CurveValue(0.05,ne\CurrSpeed,25.0)
							EndIf
							If ne\currsound = 1 Lor ne\currsound = 2 Then
								StopStream_Strict(ne\soundchn)
								If PlayerRoom\RoomTemplate\Name = "checkpoint_rcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_hcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_bcz" Then
									ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Checkpoint\Stop.ogg",opt\SFXVolume*0.5,0)
								Else
									ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Stop"+Rand(1,3)+".ogg",opt\SFXVolume*0.5,0)
								EndIf
								ne\currsound = 3
							EndIf
						EndIf
						MoveEntity ne\obj,0,-ne\CurrSpeed*FPSfactor,0
						If ne\room = PlayerRoom And PlayerInNewElevator And PlayerNewElevator = ne\ID Then
							TeleportEntity Collider,EntityX(Collider),EntityY(ne\obj,True)+0.3,EntityZ(Collider)
						EndIf
					Else
						PositionEntity ne\obj,EntityX(ne\obj),ne\floory[ne\tofloor-1],EntityZ(ne\obj)
						ne\currfloor = ne\tofloor
						ne\state = FPSfactor
					EndIf
				Else
					ShowEntity ne\button_arrow[0]
					HideEntity ne\button_arrow[1]
					If PlayerRoom\RoomTemplate\Name = "core_lcz" Lor PlayerRoom\RoomTemplate\Name = "core_hcz" Lor PlayerRoom\RoomTemplate\Name = "core_ez" Then
						If ne\currfloor = 1 And ne\tofloor = 3 Then
							If EntityY(ne\obj)>ne\floory[1]
								EntityTexture ne\button_numbers,NEI\button_number_tex[1],1
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[1],0
							EndIf
						Else
							If ne\currfloor = 1 Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[1],0
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[1],1
							EndIf
						EndIf
					ElseIf PlayerRoom\RoomTemplate\Name = "checkpoint_rcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_hcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_bcz" Then
						If ne\currfloor = 3 And ne\tofloor = 1 Then
							If EntityY(ne\obj) < ne\floory[1] Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[2],1
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[2],0
							EndIf
						Else
							If ne\currfloor = 3 Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[2],0
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[2],1
							EndIf
						EndIf
					Else
						If ne\currfloor = 1 And ne\tofloor = 3 Then
							If EntityY(ne\obj)>ne\floory[1]
								EntityTexture ne\button_numbers,NEI\button_number_tex[0],1
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[0],0
							EndIf
						Else
							If ne\currfloor = 1 Then
								EntityTexture ne\button_numbers,NEI\button_number_tex[0],0
							Else
								EntityTexture ne\button_numbers,NEI\button_number_tex[0],1
							EndIf
						EndIf
					EndIf
					If EntityY(ne\obj) < ne\floory[ne\tofloor-1] Then
						If EntityY(ne\obj) < ne\floory[ne\tofloor-1] - (0.6/RoomScale) Then
							ne\CurrSpeed = CurveValue(ne\speed,ne\CurrSpeed,75.0)
						Else
							If ne\isStorage Then
								ne\CurrSpeed = CurveValue(0.05,1.5,25.0)
							Else
								ne\CurrSpeed = CurveValue(0.05,ne\CurrSpeed,25.0)
							EndIf
							If ne\currsound = 1 Lor ne\currsound = 2 Then
								StopStream_Strict(ne\soundchn)
								If PlayerRoom\RoomTemplate\Name = "checkpoint_rcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_hcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_bcz" Then
									ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Checkpoint\Stop.ogg",opt\SFXVolume*0.5,0)
								Else
									ne\soundchn = StreamSound_Strict("SFX\General\Elevator\Stop"+Rand(1,3)+".ogg",opt\SFXVolume*0.5,0)
								EndIf
								ne\currsound = 3
							EndIf
						EndIf
						MoveEntity ne\obj,0,ne\CurrSpeed*FPSfactor,0
						If ne\room = PlayerRoom And PlayerInNewElevator And PlayerNewElevator = ne\ID Then
							TeleportEntity Collider,EntityX(Collider),EntityY(ne\obj,True)+0.3,EntityZ(Collider)
						EndIf
					Else
						PositionEntity ne\obj,EntityX(ne\obj),ne\floory[ne\tofloor-1],EntityZ(ne\obj)
						ne\currfloor = ne\tofloor
						ne\state = FPSfactor
					EndIf
				EndIf
				If ne\room = PlayerRoom And PlayerInNewElevator And PlayerNewElevator = ne\ID Then
						PositionEntity ne\door\obj,EntityX(ne\door\obj),EntityY(ne\obj,True),EntityZ(ne\door\obj)
						PositionEntity ne\door\obj2,EntityX(ne\door\obj2),EntityY(ne\obj,True),EntityZ(ne\door\obj2)
						PositionEntity ne\door\frameobj,EntityX(ne\door\frameobj),EntityY(ne\obj,True),EntityZ(ne\door\frameobj)
						PositionEntity ne\door\buttons[0],EntityX(ne\door\buttons[0]),EntityY(ne\obj,True)+0.6,EntityZ(ne\door\buttons[0])
						PositionEntity ne\door\buttons[1],EntityX(ne\door\buttons[1]),EntityY(ne\obj,True)+0.7,EntityZ(ne\door\buttons[1])
					PositionEntity Collider,EntityX(Collider),EntityY(ne\obj,True)+0.3,EntityZ(Collider)
					DropSpeed = 0
					CameraShake = Sin(Abs(ne\CurrSpeed*15)/3.0)*0.5
				EndIf
				For n.NPCs = Each NPCs
					If Abs(EntityX(n\Collider)-EntityX(ne\obj,True)) < 280.0*RoomScale+(0.015*FPSfactor) Then
						If Abs(EntityZ(n\Collider)-EntityZ(ne\obj,True)) < 280.0*RoomScale+(0.015*FPSfactor) Then
							PositionEntity n\Collider,EntityX(n\Collider),EntityY(ne\obj,True)+n\CollRadius,EntityZ(n\Collider)
							n\DropSpeed = 0
						EndIf
					EndIf
				Next
				For it.Items = Each Items
					If Abs(EntityX(it\collider)-EntityX(ne\obj,True)) < 280.0*RoomScale+(0.015*FPSfactor) Then
						If Abs(EntityZ(it\collider)-EntityZ(ne\obj,True)) < 280.0*RoomScale+(0.015*FPSfactor) Then
							PositionEntity it\collider,EntityX(it\collider),EntityY(ne\obj,True)+0.01,EntityZ(it\collider)
							it\DropSpeed = 0
						EndIf
					EndIf
				Next
			EndIf
		Else
			ne\CurrSpeed = 0.0
			ne\currsound = 0
			For i = 0 To 1
				HideEntity ne\button_arrow[i]
			Next
			If PlayerRoom\RoomTemplate\Name = "core_lcz" Lor PlayerRoom\RoomTemplate\Name = "core_hcz" Lor PlayerRoom\RoomTemplate\Name = "core_ez" Then
				Select ne\currfloor
					Case 1
						EntityTexture ne\button_numbers,NEI\button_number_tex[1],0
					Case 2
						EntityTexture ne\button_numbers,NEI\button_number_tex[1],1
					Case 3
						EntityTexture ne\button_numbers,NEI\button_number_tex[1],2
				End Select
			ElseIf PlayerRoom\RoomTemplate\Name = "checkpoint_rcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_hcz" Lor PlayerRoom\RoomTemplate\Name = "checkpoint_bcz" Then
				Select ne\currfloor
					Case 1
						EntityTexture ne\button_numbers,NEI\button_number_tex[2],0
					Case 2
						EntityTexture ne\button_numbers,NEI\button_number_tex[2],1
					Case 3
						EntityTexture ne\button_numbers,NEI\button_number_tex[2],2
				End Select
			Else
				Select ne\currfloor
					Case 1
						EntityTexture ne\button_numbers,NEI\button_number_tex[0],0
					Case 2
						EntityTexture ne\button_numbers,NEI\button_number_tex[0],1
					Case 3
						EntityTexture ne\button_numbers,NEI\button_number_tex[0],2
				End Select
			EndIf
			If ne\state > 0.0 And ne\state < 100.0 Then
				ne\state = ne\state + FPSfactor
			ElseIf ne\state >= 100.0 Then
				PlaySound2(ElevatorBeepSFX, Camera, ne\door\frameobj, 6.0)
				UseDoor(ne\door)
				If Left(PlayerRoom\RoomTemplate\Name, 10) = "checkpoint" Lor Left(PlayerRoom\RoomTemplate\Name, 4) = "core" Then SaveGame(SavePath + CurrSave\Name + "\", True)
				PlayerInNewElevator = False
				ne\state = 0.0
			EndIf
		EndIf
		If PlayerInNewElevator Then
			SetStreamVolume_Strict(ne\soundchn,opt\SFXVolume*0.5)
		Else
			UpdateStreamSoundOrigin(ne\soundchn,Camera,ne\obj)
		EndIf
	Next
	
End Function

Function DeleteNewElevators()
	Local ne.NewElevator
	
	For ne = Each NewElevator
		FreeEntity ne\obj : ne\obj=0
		StopStream_Strict(ne\soundchn)
		ne\soundchn = 0
		ne\sound = 0
		Delete ne
	Next
	
End Function

Function UpdateSmallHeadMode()
	Local n.NPCs, bonename$, bone%
	
	For n = Each NPCs
		bonename$ = GetNPCManipulationValue(n\NPCNameInSection,"head","bonename",0)
		If bonename$<>""
			bone% = FindChild(n\obj,bonename$)
			ScaleEntity bone%,0.5,0.5,0.5
		EndIf
		If n\NPCtype = NPC_SCP_049 Then
			bone% = FindChild(n\obj, "Bone_019")
			ScaleEntity bone%,0.5,0.5,0.5
		ElseIf n\NPCtype = NPC_SCP_106 Then
			bone% = FindChild(n\obj, "Bone_022")
			ScaleEntity bone%,0.5,0.5,0.5
		EndIf
	Next
	
End Function

Function ApplyBumpMap(texture%)
	
	TextureBlend texture%,6
	TextureBumpEnvMat texture%,0,0,-0.012
	TextureBumpEnvMat texture%,0,1,-0.012
	TextureBumpEnvMat texture%,1,0,0.012
	TextureBumpEnvMat texture%,1,1,0.012
	TextureBumpEnvOffset texture%,0.5
	TextureBumpEnvScale texture%,1.0
	
End Function

Function PointEntity2(source_ent%,dest_ent%,roll#=0.0,usepitch%=True,useyaw%=True)
	Local pitch#,yaw#
	
	pitch# = EntityPitch(source_ent%)
	If usepitch%
		pitch# = pitch# + DeltaPitch(source_ent%,dest_ent%)
	EndIf
	yaw# = EntityYaw(source_ent%)
	If useyaw%
		yaw# = yaw# + DeltaYaw(source_ent%,dest_ent%)
	EndIf
	RotateEntity source_ent%,pitch#,yaw#,roll#
	
End Function

Type HitBox
	Field HitBox1[25]
	Field HitBox2[25]
	Field HitBox3[25]
	Field BoneName$[25]
	Field HitBoxPosX#[25]
	Field HitBoxPosY#[25]
	Field HitBoxPosZ#[25]
	Field NPCtype%
	Field ID%
End Type

Function ApplyHitBoxes.HitBox(npctype,npcname$)
	Local hb.HitBox = New HitBox
	
	hb\NPCtype = npctype
	
	Local i%,htype%,bonename$
	Local scaleX#,scaleY#,scaleZ#,posX#,posY#,posZ#
	Local file$ = "Data\NPCBones.ini"
	
	For i = 0 To GetINIInt(file$,npcname$,"hitbox_amount")-1
		htype% = GetINIInt(file$,npcname$,"hitbox"+(i+1)+"_type")
		bonename$ = GetINIString(file$,npcname$,"hitbox"+(i+1)+"_parent")
		hb\BoneName[i] = bonename
		If htype = 0
			hb\HitBox1[i] = CreateCube()
			scaleX# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleX",1.0)
			scaleY# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleY",1.0)
			scaleZ# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleZ",1.0)
			posX# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posX",0.0)
			posY# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posY",0.0)
			posZ# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posZ",0.0)
			ScaleEntity hb\HitBox1[i],scaleX,scaleY,scaleZ
			PositionEntity hb\HitBox1[i],posX,posY,posZ
			EntityPickMode hb\HitBox1[i],2
			EntityAlpha hb\HitBox1[i],0.0
			HideEntity hb\HitBox1[i]
		ElseIf htype = 1
			hb\HitBox2[i] = CreateCube()
			scaleX# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleX",1.0)
			scaleY# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleY",1.0)
			scaleZ# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleZ",1.0)
			posX# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posX",0.0)
			posY# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posY",0.0)
			posZ# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posZ",0.0)
			ScaleEntity hb\HitBox2[i],scaleX,scaleY,scaleZ
			PositionEntity hb\HitBox2[i],posX,posY,posZ
			EntityPickMode hb\HitBox2[i],2
			EntityAlpha hb\HitBox2[i],0.0
			HideEntity hb\HitBox2[i]
		Else
			hb\HitBox3[i] = CreateCube()
			scaleX# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleX",1.0)
			scaleY# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleY",1.0)
			scaleZ# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_scaleZ",1.0)
			posX# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posX",0.0)
			posY# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posY",0.0)
			posZ# = GetINIFloat(file$,npcname$,"hitbox"+(i+1)+"_posZ",0.0)
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

Function CopyHitBoxes(n.NPCs)
	Local hb.HitBox,bone%,i%
	
	For hb = Each HitBox
		If hb\NPCtype = n\NPCtype Then
			For i = 0 To 24
				If hb\BoneName[i]<>"" Then
					n\BoneName[i]=hb\BoneName[i]
					bone = FindChild(n\obj,n\BoneName[i])
				EndIf
				If hb\HitBox1[i]<>0 Then
					n\HitBox1[i] = CopyEntity(hb\HitBox1[i],bone)
					PositionEntity n\HitBox1[i],hb\HitBoxPosX[i],hb\HitBoxPosY[i],hb\HitBoxPosZ[i]
				EndIf
				If hb\HitBox2[i]<>0 Then
					n\HitBox2[i] = CopyEntity(hb\HitBox2[i],bone)
					PositionEntity n\HitBox2[i],hb\HitBoxPosX[i],hb\HitBoxPosY[i],hb\HitBoxPosZ[i]
				EndIf
				If hb\HitBox3[i]<>0 Then
					n\HitBox3[i] = CopyEntity(hb\HitBox3[i],bone)
					PositionEntity n\HitBox3[i],hb\HitBoxPosX[i],hb\HitBoxPosY[i],hb\HitBoxPosZ[i]
				EndIf
			Next
			Exit
		EndIf
	Next
	
End Function

Function HideNPCHitBoxes(n.NPCs)
	Local i%
	
	For i = 0 To 24
		If n\HitBox1[i]<>0 Then HideEntity n\HitBox1[i]
		If n\HitBox2[i]<>0 Then HideEntity n\HitBox2[i]
		If n\HitBox3[i]<>0 Then HideEntity n\HitBox3[i]
	Next
	
End Function

Function ShowNPCHitBoxes(n.NPCs)
	Local i%
	
	For i = 0 To 24
		If n\HitBox1[i]<>0 Then ShowEntity n\HitBox1[i]
		If n\HitBox2[i]<>0 Then ShowEntity n\HitBox2[i]
		If n\HitBox3[i]<>0 Then ShowEntity n\HitBox3[i]
	Next
	
End Function

Function FreeNPCHitBoxes(n.NPCs)
	Local i%
	
	For i = 0 To 24
		n\HitBox1[i] = FreeEntity_Strict(n\HitBox1[i])
		n\HitBox2[i] = FreeEntity_Strict(n\HitBox2[i])
		n\HitBox3[i] = FreeEntity_Strict(n\HitBox3[i])
	Next
	
End Function

Function GetMeshExtents2(mesh)
	Local xmax#=-1000000
	Local xmin#= 1000000
	Local ymax#=-1000000
	Local ymin#= 1000000
	Local zmax#=-1000000
	Local zmin#= 1000000
	Local su,s,i,x#,y#,z#
	For su=1 To CountSurfaces(mesh)
		s=GetSurface(mesh,su)
		For i=0 To CountVertices(s)-1
			x#=VertexX(s,i)
			y#=VertexY(s,i)
			z#=VertexZ(s,i)
			TFormPoint x,y,z,mesh,0
			x=TFormedX()
			y=TFormedY()
			z=TFormedZ()
			If x>xmax Then xmax=x
			If x<xmin Then xmin=x
			If y>ymax Then ymax=y
			If y<ymin Then ymin=y
			If z>zmax Then zmax=z
			If z<zmin Then zmin=z
		Next
	Next
	
	Mesh_MinX = xmin
	Mesh_MinY = ymin
	Mesh_MinZ = zmin
	Mesh_MaxX = xmax
	Mesh_MaxY = ymax
	Mesh_MaxZ = zmax
	Mesh_MagX = xmax-xmin
	Mesh_MagY = ymax-ymin
	Mesh_MagZ = zmax-zmin
	
End Function

Function TurnIntoSeconds(number#)
	
	Return Ceil(Int(number)/70.0)
	
End Function

Function GetAnimationSequences(n.NPCs,npcname$)
	Local i%
	Local animstart%,animstop%
	Local file$ = "Data\NPCBones.ini"
	
	For i = 1 To GetINIInt(file$,npcname$,"anim_amount")
		animstart = GetINIInt(file$,npcname$,"anim"+i+"_start")
		animstop = GetINIInt(file$,npcname$,"anim"+i+"_stop")
		ExtractAnimSeq(n\obj,animstart,animstop,0)
	Next
	
End Function

Function GetAnimationSpeed(n.NPCs,npcname$,currsequence%)
	Local file$ = "Data\NPCBones.ini"
	
	Return GetINIFloat(file$,npcname$,"anim"+currsequence%+"_speed",0.5)
End Function

Function ApplyAnimation(n.NPCs,sequence%,speed#,animmode%=1)
	
	If n\CurrAnimSeq<>sequence%
		Animate(n\obj,animmode%,speed#,sequence%,5)
		n\CurrAnimSeq = sequence
	EndIf
	n\Frame = AnimTime(n\obj)
	;debuglog n\Frame
	
End Function

Global ShouldUpdateWater$ = ""
Global WaterRender_IgnoreObject%

Type Water
	Field obj%
	Field VexX#[256]
	Field VexY#[256]
	Field VexZ#[256]
	Field PrevVexY#[256]
	Field name$
	Field timer#
	Field isrendering%
	Field customY#
End Type

Function CreateWater.Water(filepath$,name$,x#,y#,z#,parent%=0,customY#=0.0)
	Local wa.Water = New Water
	
	wa\obj = LoadMesh_Strict(filepath,parent)
	PositionEntity wa\obj,x,y,z
	wa\name = name$
	Local surf=GetSurface(wa\obj,1)
	Local i%
	For i=0 To CountVertices(surf)-1
		wa\VexX#[i]=VertexX#(surf,i)
		wa\VexY#[i]=VertexY#(surf,i)
		wa\VexZ#[i]=VertexZ#(surf,i)
		wa\PrevVexY#[i]=wa\VexY#[i]
	Next
	EntityTexture wa\obj,MapCubeMap\Texture,0,1
	wa\customY=customY
	
	Return wa
End Function

Function UpdateWater(name$)
	Local wa.Water,it.Items
	
	For wa = Each Water
		wa\isrendering=False
		If wa\name = name
			wa\isrendering=True
			wa\timer=wa\timer+2*FPSfactor
			UpdateCubeMap(wa\obj,"MapCubeMap")
		EndIf
	Next
	
End Function

Function RenderWater(name$)
	Local wa.Water,it.Items
	Local i,s
	
	For wa = Each Water
		If wa\name = name Then
			For it = Each Items
				If EntityY(it\collider)<wa\customY Then
					HideEntity(it\model)
				EndIf
			Next
			If WaterRender_IgnoreObject<>0 Then
				HideEntity WaterRender_IgnoreObject
			EndIf
			MapCubeMap\RenderY = wa\customY
			s=GetSurface(wa\obj,1)
			For i=0 To CountVertices(s)-1
				wa\VexY[i]=Sin(wa\timer+wa\VexX[i]*500+wa\VexZ[i]*300)*5.0
				VertexCoords s,i,wa\VexX[i],wa\PrevVexY[i]-wa\VexY[i],wa\VexZ[i]
			Next
			UpdateNormals wa\obj
			RenderCubeMap(wa\obj,"MapCubeMap")
			If WaterRender_IgnoreObject<>0
				ShowEntity WaterRender_IgnoreObject
			EndIf
			For it = Each Items
				If EntityY(it\collider)<wa\customY Then
					ShowEntity(it\model)
				EndIf
			Next
		EndIf
	Next
	
End Function

;Global SZL_CurrLoad% = 0
;Global SZL_NewZone% = 0
;Global SZL_RoomAmount% = 0
;Dim SZL_CurrRoom(MapWidth,MapHeight)
;
;Function SZL(file$,newzone%)
;	Local i%,j%
;	
;	If SZL_CurrLoad = 0
;		QuickLoadPercent = 0
;		SZL_NewZone = newzone%
;		SZL_CurrLoad = 1
;		SZL_RoomAmount = 0
;		For i = 0 To MapWidth
;			For j = 0 To MapHeight
;				SZL_CurrRoom(i,j)=0
;			Next
;		Next
;	EndIf
;	
;End Function
;
;Function UpdateSZL()
;	Local r.Rooms,w.WayPoints,em.Emitters,it.Items,n.NPCs,e.Events,sc.SecurityCams,s.Screens,de.Decals,d.Doors,twp.TempWayPoints,lt.LightTemplates,pr.Props,rt.RoomTemplates
;	
;	If SZL_CurrLoad = 1
;		;SZL_SaveZone(file$)
;		gopt\CurrZone = SZL_NewZone
;		;ClearTextureCache
;		DeleteNewElevators()
;		For r.Rooms = Each Rooms
;			If r<>PlayerRoom
;				If r\AlarmRotorLight[0]<>0
;					FreeEntity r\AlarmRotorLight[0] : r\AlarmRotorLight[0]=0
;				EndIf
;				FreeEntity r\obj
;				Delete r
;			EndIf
;		Next
;		For w.WayPoints = Each WayPoints
;			Delete w
;		Next
;		For d.Doors = Each Doors
;			If d\room<>PlayerRoom
;				Local deletedoor%=True
;				For i=0 To 3
;					If PlayerRoom\AdjDoor[i]<>Null
;						If PlayerRoom\AdjDoor[i]=d
;							deletedoor=False
;							Exit
;						EndIf
;					EndIf
;				Next
;				If deletedoor
;					If d\buttons[0]<>0 Then FreeEntity d\buttons[0]
;					If d\buttons[1]<>0 Then FreeEntity d\buttons[1]
;					FreeEntity d\frameobj
;					FreeEntity d\obj
;					If d\obj2<>0 Then FreeEntity d\obj2
;					Delete d
;				EndIf
;			EndIf
;		Next
;		For em.Emitters = Each Emitters
;			;FreeEntity em\Obj
;			Delete em
;		Next
;		For it.Items = Each Items
;			If (Not it\Picked)
;				RemoveItem(it)
;			EndIf
;		Next
;		For n.NPCs = Each NPCs
;			If n <> Curr173 And n <> Curr106 And n <> Curr096
;				If n\NPCtype <> NPCtype049 And n\NPCtype <> NPCtypeNTF
;					RemoveNPC(n)
;				EndIf
;			EndIf
;		Next
;		For e.Events = Each Events
;			RemoveEvent(e)
;		Next
;		For sc.SecurityCams = Each SecurityCams
;			FreeEntity sc\CameraObj
;			FreeEntity sc\BaseObj
;			FreeEntity sc\obj
;			If sc\Cam<>0 Then FreeEntity sc\Cam
;			;If sc\MonitorObj<>0 Then FreeEntity sc\MonitorObj
;			If sc\Room2slTexs[0]<>0 Then FreeTexture(sc\Room2slTexs[0])
;			If sc\Room2slTexs[1]<>0 Then FreeTexture(sc\Room2slTexs[1])
;			Delete sc
;		Next
;		For s.Screens = Each Screens
;			If s\img <> 0 Then FreeImage s\img
;			Delete s
;		Next
;		For de.Decals = Each Decals
;			Delete de
;		Next
;		For twp.TempWayPoints = Each TempWayPoints
;			Delete twp
;		Next
;		For lt.LightTemplates = Each LightTemplates
;			Delete lt
;		Next
;		For pr.Props = Each Props
;			Delete pr
;		Next
;		For rt.RoomTemplates = Each RoomTemplates
;			rt\obj = 0
;		Next
;		QuickLoadPercent = 3
;		SZL_CurrLoad = 2
;	Else
;		Local strtemp$ = "",i%
;		For i = 1 To Len(RandomSeed)
;			strtemp = strtemp+Asc(Mid(RandomSeed,i,1))
;		Next
;		SeedRnd Abs(Int(strtemp))
;;		AccessCode = 0
;;		For i = 0 To 3
;;			AccessCode = AccessCode + Rand(1,9)*(10^i)
;;		Next	
;		If FileType(gopt\CurrZone + "_zone"+gopt\CurrZone+".szl")=0
;			SZL_CreateZone()
;		Else
;			SZL_LoadZone(gopt\CurrZone)
;		EndIf
;		If SZL_CurrLoad=9999
;			UpdateDoors()
;			;PlaySound_Strict CheckPointSFX
;			For e.Events = Each Events
;				If e\room = PlayerRoom
;					Local zonename$ = ""
;					If gopt\CurrZone = LCZ
;						zonename = "LCZ"
;					ElseIf gopt\CurrZone = ClASS_D_ZONE
;						zonename = "D_ZONE"
;					EndIf
;					e\Sound = LoadEventSound(e,"SFX\Alarm\Checkpoint\Enter"+zonename$+".ogg",0)
;					e\SoundCHN[0] = PlaySound_Strict(e\Sound)
;					e\SoundCHN[1] = PlaySound_Strict(CheckPointSFX)
;				EndIf
;			Next
;			SZL_CurrLoad = 0
;			SZL_RoomAmount = 0
;			QuickLoadPercent = 100
;			SeedRnd MilliSecs()
;			TeleportEntity(Collider,EntityX(Collider),EntityY(Collider)+(0.1*FPSfactor),EntityZ(Collider))
;		EndIf
;	EndIf
;	
;	;There are still some errors regarding the SZL feature
;End Function
;
;Function SZL_SaveZone(zone%)
;	CatchErrors("Uncaught (SZL_SaveZone)")
;	
;	Local x%, y%, i%, temp%
;	Local n.NPCs, r.Rooms, do.Doors
;	
;	;CreateDir(zone)
;	
;	Local f% = WriteFile(zone + "_zone"+NTF_CurrZone+".szl")
;	
;	temp = 0
;	For  n.NPCs = Each NPCs
;		temp = temp +1
;	Next
;	
;	WriteInt f, temp
;	For n.NPCs = Each NPCs
;		If n <> Curr173 And n <> Curr106 And n <> Curr096
;			If n\NPCtype <> NPCtype049 And n\NPCtype <> NPCtypeMTF
;				;debuglog("Saving NPC " +n\NVName+ " (ID "+n\ID+")")
;				
;				WriteByte f, n\NPCtype
;				WriteFloat f, EntityX(n\Collider,True)
;				WriteFloat f, EntityY(n\Collider,True)
;				WriteFloat f, EntityZ(n\Collider,True)
;				
;				WriteFloat f, EntityPitch(n\Collider)
;				WriteFloat f, EntityYaw(n\Collider)
;				WriteFloat f, EntityRoll(n\Collider)
;				
;				WriteFloat f, n\State
;				WriteFloat f, n\State2
;				WriteFloat f, n\State3
;				WriteInt f, n\PrevState
;				
;				WriteByte f, n\Idle
;				WriteFloat f, n\LastDist
;				WriteInt f, n\LastSeen
;				
;				WriteInt f, n\CurrSpeed
;				
;				WriteFloat f, n\Angle
;				
;				WriteFloat f, n\Reload
;				
;				WriteInt f, n\ID
;				If n\Target <> Null Then
;					WriteInt f, n\Target\ID		
;				Else
;					WriteInt f, 0
;				EndIf
;				
;				WriteFloat f, n\EnemyX
;				WriteFloat f, n\EnemyY
;				WriteFloat f, n\EnemyZ
;				
;				WriteString f, n\texture
;				
;				WriteFloat f, AnimTime(n\obj)
;				
;				WriteInt f, n\IsDead
;				WriteFloat f, n\PathX
;				WriteFloat f, n\PathZ
;				WriteInt f, n\HP
;				WriteString f, n\Model
;				WriteFloat f, n\ModelScaleX#
;				WriteFloat f, n\ModelScaleY#
;				WriteFloat f, n\ModelScaleZ#
;				WriteInt f, n\TextureID
;			EndIf
;		EndIf
;	Next
;	
;	WriteInt f, 954
;	
;	temp = 0
;	For do.Doors = Each Doors
;		temp = temp+1	
;	Next	
;	WriteInt f, temp	
;	For do.Doors = Each Doors
;		WriteFloat f, EntityX(do\frameobj,True)
;		WriteFloat f, EntityY(do\frameobj,True)
;		WriteFloat f, EntityZ(do\frameobj,True)
;		WriteByte f, do\open
;		WriteFloat f, do\openstate
;		WriteByte f, do\locked
;		WriteByte f, do\AutoClose
;		
;		WriteFloat f, EntityX(do\obj, True)
;		WriteFloat f, EntityZ(do\obj, True)
;		
;		If do\obj2 <> 0 Then
;			WriteFloat f, EntityX(do\obj2, True)
;			WriteFloat f, EntityZ(do\obj2, True)
;		Else
;			WriteFloat f, 0.0
;			WriteFloat f, 0.0
;		End If
;		
;		WriteFloat f, do\timer
;		WriteFloat f, do\timerstate
;		
;		WriteByte f, do\IsElevatorDoor
;		WriteByte f, do\MTFClose
;	Next
;	
;	WriteInt f, 1845
;	
;	Local d.Decals
;	temp = 0
;	For d.Decals = Each Decals
;		temp = temp+1
;	Next	
;	WriteInt f, temp
;	For d.Decals = Each Decals
;		WriteInt f, d\ID
;		
;		WriteFloat f, d\x
;		WriteFloat f, d\y
;		WriteFloat f, d\z
;		
;		WriteFloat f, d\pitch
;		WriteFloat f, d\yaw
;		WriteFloat f, d\roll
;		
;		WriteByte f, d\blendmode
;		WriteInt f, d\fx
;		
;		WriteFloat f, d\Size
;		WriteFloat f, d\Alpha
;		WriteFloat f, d\AlphaChange
;		WriteFloat f, d\Timer
;		WriteFloat f, d\lifetime
;	Next
;	
;	Local e.Events
;	temp = 0
;	For e.Events = Each Events
;		temp=temp+1
;	Next	
;	WriteInt f, temp
;	For e.Events = Each Events
;		WriteString f, e\EventName
;		WriteFloat f, e\EventState
;		WriteFloat f, e\EventState2	
;		WriteFloat f, e\EventState3	
;		WriteFloat f, EntityX(e\room\obj)
;		WriteFloat f, EntityZ(e\room\obj)
;		WriteString f, e\EventStr
;	Next
;	
;	temp = 0
;	For it.items = Each Items	
;		temp=temp+1
;	Next
;	WriteInt f, temp
;	For it.items = Each Items
;		WriteString f, it\itemtemplate\name
;		WriteString f, it\itemtemplate\tempName
;		
;		WriteString f, it\name
;		
;		WriteFloat f, EntityX(it\collider, True)
;		WriteFloat f, EntityY(it\collider, True)
;		WriteFloat f, EntityZ(it\collider, True)
;		
;		WriteByte f, it\r
;		WriteByte f, it\g
;		WriteByte f, it\b
;		WriteFloat f, it\a
;		
;		WriteFloat f, EntityPitch(it\collider)
;		WriteFloat f, EntityYaw(it\collider)
;		
;		WriteFloat f, it\state
;		WriteByte f, it\Picked
;		
;		If SelectedItem = it Then WriteByte f, 1 Else WriteByte f, 0
;		Local ItemFound% = False
;		For i = 0 To MaxItemAmount - 1
;			If Inventory(i) = it Then ItemFound = True : Exit
;		Next
;		If ItemFound Then WriteByte f, i Else WriteByte f, 66
;		
;		If it\itemtemplate\isAnim<>0 Then
;			WriteFloat f, AnimTime(it\model)
;		EndIf
;		WriteByte f,it\invSlots
;		WriteInt f,it\ID
;		If it\itemtemplate\invimg=it\invimg Then WriteByte f,0 Else WriteByte f,1
;	Next
;	
;	temp=0
;	For it.items = Each Items
;		If it\invSlots>0 Then temp=temp+1
;	Next
;	
;	WriteInt f,temp
;	
;	For it.items = Each Items
;		;OtherInv
;		If it\invSlots>0 Then
;			WriteInt f,it\ID
;			For i=0 To it\invSlots-1
;				If it\SecondInv[i] <> Null Then
;					WriteInt f, it\SecondInv[i]\ID
;				Else
;					WriteInt f, -1
;				EndIf
;			Next
;		EndIf
;		;OtherInv End
;	Next
;	
;	For itt.itemtemplates = Each ItemTemplates
;		WriteByte f, itt\found
;	Next
;	
;	WriteInt f, 994
;	
;	CloseFile f
;	
;	CatchErrors("SZL_SaveZone")
;End Function
;
;Function SZL_CreateZone()
;	Local r.Rooms,r2.Rooms,temp,x,y,zone,i,d.Doors
;	Local it.Items,sc.SecurityCams,de.Decals,e.Events
;	
;	;First, determine the amount of rooms existing
;	If SZL_CurrLoad = 2
;		For y = MapHeight - 1 To 1 Step - 1
;			For x = 1 To MapWidth - 2
;				If MapTemp(x,y)>0
;					SZL_RoomAmount = SZL_RoomAmount + 1
;					SZL_CurrRoom(x,y)=SZL_RoomAmount
;					;debuglog SZL_CurrRoom(x,y)
;				EndIf
;			Next
;		Next
;		QuickLoadPercent = 5
;		SZL_CurrLoad = 3
;	Else
;		temp = 0
;		Local spacing# = 8.0
;		For y = MapHeight - 1 To 1 Step - 1
;			
;			If y < MapHeight/3+1 Then
;				zone=3
;			ElseIf y < MapHeight*(2.0/3.0)
;				zone=2
;			Else
;				zone=1
;			EndIf
;			
;			For x = 1 To MapWidth - 2
;				If MapTemp(x, y) = 255 Then
;					If SZL_CurrRoom(x,y)=SZL_CurrLoad-2
;						If (PlayerRoom\x = x*8) And (PlayerRoom\z = y*8)
;							Local isplayerroom% = False
;							If PlayerRoom\x = x*8
;								If PlayerRoom\z = y*8
;									For i=0 To 1
;										FreeEntity PlayerRoom\RoomDoors[i]\frameobj
;										FreeEntity PlayerRoom\RoomDoors[i]\obj
;										FreeEntity PlayerRoom\RoomDoors[i]\obj2
;										Delete PlayerRoom\RoomDoors[i]
;									Next
;									Local deletedoor%=False
;									For i=0 To 3
;										If PlayerRoom\AdjDoor[i]<>Null
;											If PlayerRoom\AdjDoor[i]\buttons[0]<>0 Then FreeEntity PlayerRoom\AdjDoor[i]\buttons[0]
;											If PlayerRoom\AdjDoor[i]\buttons[1]<>0 Then FreeEntity PlayerRoom\AdjDoor[i]\buttons[1]
;											FreeEntity PlayerRoom\AdjDoor[i]\frameobj
;											FreeEntity PlayerRoom\AdjDoor[i]\obj
;											If PlayerRoom\AdjDoor[i]\obj2<>0 Then FreeEntity PlayerRoom\AdjDoor[i]\obj2
;											Delete PlayerRoom\AdjDoor[i]
;										EndIf
;									Next
;									isplayerroom = True
;									FreeEntity PlayerRoom\obj
;									Delete PlayerRoom
;								EndIf
;							EndIf
;						EndIf
;						If gopt\CurrZone = ClASS_D_ZONE Then
;							r = CreateRoom(gopt\CurrZone, ROOM2, x * 8, 0, y * 8, "checkpoint_class_d_2", 0)
;						ElseIf gopt\CurrZone = LCZ Then
;							r = CreateRoom(gopt\CurrZone, ROOM2, x * 8, 0, y * 8, "checkpoint_class_d_1", 0)
;						EndIf
;						If isplayerroom
;							PlayerRoom = r
;							TeleportEntity(Collider,EntityX(Collider),EntityY(Collider)+(0.1*FPSfactor),EntityZ(Collider))
;							
;							If r\RoomTemplate\Name = "checkpoint_class_d_2"
;								temp = 0
;							ElseIf r\RoomTemplate\Name = "checkpoint_class_d_1"
;								temp = 2
;							EndIf
;							d.Doors = CreateDoor(temp, Float(x) * spacing, 0, Float(y) * spacing + spacing / 2.0, 0, r, Max(Rand(-3, 1), 0), temp)
;							;d\IsSZLAdjacentDoor = True
;							
;							If r\RoomTemplate\Name = "checkpoint_class_d_2"
;								temp = 2
;							ElseIf r\RoomTemplate\Name = "checkpoint_class_d_1"
;								temp = 0
;							EndIf
;							d.Doors = CreateDoor(temp, Float(x) * spacing, 0, Float(y) * spacing - spacing / 2.0, 0, r, Max(Rand(-3, 1), 0), temp)
;							;d\IsSZLAdjacentDoor = True
;							
;						Else
;							If r<>Null
;								HideEntity r\obj
;							EndIf
;						EndIf
;						SZL_CurrLoad=SZL_CurrLoad+1
;						If (SZL_CurrLoad Mod 2)=0
;							QuickLoadPercent = Min(QuickLoadPercent+1,91)
;						EndIf
;						;debuglog SZL_CurrLoad
;						Exit
;					EndIf
;				ElseIf MapTemp(x, y) > 0
;					If SZL_CurrRoom(x,y)=SZL_CurrLoad-2
;						temp = Min(MapTemp(x + 1, y),1) + Min(MapTemp(x - 1, y),1) + Min(MapTemp(x, y + 1),1) + Min(MapTemp(x, y - 1),1)
;						
;						Select temp
;							Case 1
;								If zone = gopt\CurrZone
;									If MapTemp(x, y + 1) Then
;										Local angle = 180
;									ElseIf MapTemp(x - 1, y)
;										angle = 270
;									ElseIf MapTemp(x + 1, y)
;										angle = 90
;									Else 
;										angle = 0
;									EndIf
;									r = CreateRoom(zone, ROOM1, x * 8, 0, y * 8, MapName(x, y), angle)
;									;TurnEntity(r\obj, 0, r\angle, 0)
;									HideEntity r\obj
;								EndIf
;							Case 2
;								If MapTemp(x - 1, y)>0 And MapTemp(x + 1, y)>0 Then
;									If zone = NTF_CurrZone
;										If Rand(2) = 1 Then angle = 90 Else angle = 270
;										r = CreateRoom(zone, ROOM2, x * 8, 0, y * 8, MapName(x, y), angle)
;										;TurnEntity(r\obj, 0, r\angle, 0)
;										HideEntity r\obj
;									EndIf
;								ElseIf MapTemp(x, y - 1)>0 And MapTemp(x, y + 1)>0
;									If zone = NTF_CurrZone
;										If Rand(2) = 1 Then angle = 180 Else angle = 0
;										r = CreateRoom(zone, ROOM2, x * 8, 0, y * 8, MapName(x, y), angle)
;										;TurnEntity(r\obj, 0, r\angle, 0)
;										HideEntity r\obj
;									EndIf
;								Else
;									If MapTemp(x - 1, y)>0 And MapTemp(x, y + 1)>0 Then
;										If zone = NTF_CurrZone
;											angle = 180
;											r = CreateRoom(zone, ROOM2C, x * 8, 0, y * 8, MapName(x, y), angle)
;											;TurnEntity(r\obj, 0, r\angle, 0)
;											HideEntity r\obj
;										EndIf
;									ElseIf MapTemp(x + 1, y)>0 And MapTemp(x, y + 1)>0
;										If zone = NTF_CurrZone
;											angle = 90
;											r = CreateRoom(zone, ROOM2C, x * 8, 0, y * 8, MapName(x, y), angle)
;											;TurnEntity(r\obj, 0, r\angle, 0)
;											HideEntity r\obj
;										EndIf
;									ElseIf MapTemp(x - 1, y)>0 And MapTemp(x, y - 1)>0
;										If zone = NTF_CurrZone
;											angle = 270
;											r = CreateRoom(zone, ROOM2C, x * 8, 0, y * 8, MapName(x, y), angle)
;											;TurnEntity(r\obj, 0, 270, 0)
;											HideEntity r\obj
;										EndIf
;									Else
;										If zone = NTF_CurrZone
;											r = CreateRoom(zone, ROOM2C, x * 8, 0, y * 8, MapName(x, y), 0)
;											HideEntity r\obj
;										EndIf
;									EndIf
;								EndIf
;							Case 3
;								If zone = NTF_CurrZone
;									angle = 0
;									If (Not MapTemp(x, y - 1)) Then
;										angle = 180
;									ElseIf (Not MapTemp(x - 1, y))
;										angle = 90
;									ElseIf (Not MapTemp(x + 1, y))
;										angle = 270
;									EndIf
;									r = CreateRoom(zone, ROOM3, x * 8, 0, y * 8, MapName(x, y), angle)
;									;TurnEntity(r\obj, 0, r\angle, 0)
;									HideEntity r\obj
;								EndIf
;							Case 4
;								If zone = NTF_CurrZone
;									r = CreateRoom(zone, ROOM4, x * 8, 0, y * 8, MapName(x, y), 0)
;									HideEntity r\obj
;								EndIf
;						End Select
;						SZL_CurrLoad=SZL_CurrLoad+1
;						If (SZL_CurrLoad Mod 2)=0
;							QuickLoadPercent = Min(QuickLoadPercent+1,91)
;						EndIf
;						;debuglog SZL_CurrLoad
;						Exit
;					EndIf
;				EndIf
;			Next
;		Next
;		
;		If SZL_CurrLoad=SZL_RoomAmount+3
;			For y = MapHeight - 1 To 1 Step - 1
;				
;				If y < MapHeight/3+1 Then
;					zone=3
;				ElseIf y < MapHeight*(2.0/3.0)
;					zone=2
;				Else
;					zone=1
;				EndIf
;				
;				For x = 1 To MapWidth - 2
;					If MapTemp(x, y)>0 Then
;						If (Floor((x + y) / 2.0) = Ceil((x + y) / 2.0)) Then
;							If zone = 2 Then temp = 2 Else temp=0
;							For d.Doors = Each Doors
;;								If d\IsSZLAdjacentDoor
;;									FreeEntity d\buttons[0]
;;									FreeEntity d\buttons[1]
;;									FreeEntity d\frameobj
;;									FreeEntity d\obj
;;									FreeEntity d\obj2
;;									Delete d
;;									;debuglog "DELETED DOORS"
;;								EndIf
;							Next
;							
;							Local roomfound% = False
;							Local roomname$
;							For r.Rooms = Each Rooms
;								If r\x=(Float(x)*spacing) And r\z=(Float(y)*spacing)
;									roomname$=r\RoomTemplate\Name
;									roomfound% = True
;									Exit
;								EndIf
;							Next
;							If roomfound
;								If MapTemp(x + 1, y) Then
;									d.Doors = CreateDoor(r\zone, Float(x) * spacing + spacing / 2.0, 0, Float(y) * spacing, 90, r, Max(Rand(-3, 1), 0), temp)
;									r\AdjDoor[0] = d
;								EndIf
;								
;								If MapTemp(x - 1, y) Then
;									d.Doors = CreateDoor(r\zone, Float(x) * spacing - spacing / 2.0, 0, Float(y) * spacing, 90, r, Max(Rand(-3, 1), 0), temp)
;									r\AdjDoor[2] = d
;								EndIf
;								
;								If roomname <> "checkpoint1_new" And roomname <> "checkpoint2_new"
;									If MapTemp(x, y + 1) Then
;										d.Doors = CreateDoor(r\zone, Float(x) * spacing, 0, Float(y) * spacing + spacing / 2.0, 0, r, Max(Rand(-3, 1), 0), temp)
;										r\AdjDoor[3] = d
;									EndIf
;									
;									If MapTemp(x, y - 1) Then
;										d.Doors = CreateDoor(r\zone, Float(x) * spacing, 0, Float(y) * spacing - spacing / 2.0, 0, r, Max(Rand(-3, 1), 0), temp)
;										r\AdjDoor[1] = d
;									EndIf
;								Else
;									If roomname = "checkpoint1_new"
;										temp = 0
;										d.Doors = CreateDoor(r\zone, Float(x) * spacing, 0, Float(y) * spacing + spacing / 2.0, 0, r, False, temp)
;										r\AdjDoor[3] = d
;										temp = 2
;										d.Doors = CreateDoor(r\zone, Float(x) * spacing, 0, Float(y) * spacing - spacing / 2.0, 0, r, False, temp)
;										r\AdjDoor[1] = d
;									Else
;										temp = 2
;										d.Doors = CreateDoor(r\zone, Float(x) * spacing, 0, Float(y) * spacing + spacing / 2.0, 0, r, False, temp)
;										r\AdjDoor[3] = d
;										temp = 0
;										d.Doors = CreateDoor(r\zone, Float(x) * spacing, 0, Float(y) * spacing - spacing / 2.0, 0, r, False, temp)
;										r\AdjDoor[1] = d
;									EndIf
;								EndIf
;							EndIf
;						EndIf
;					EndIf
;				Next
;			Next
;			;debuglog "Status 1"
;			QuickLoadPercent = 92
;			SZL_CurrLoad=SZL_CurrLoad+1
;		ElseIf SZL_CurrLoad=SZL_RoomAmount+4
;			;debuglog "Status 2"
;			QuickLoadPercent = 93
;			SZL_CurrLoad=SZL_CurrLoad+1
;		ElseIf SZL_CurrLoad=SZL_RoomAmount+5
;			For r.Rooms = Each Rooms
;				r\Adjacent[0]=Null
;				r\Adjacent[1]=Null
;				r\Adjacent[2]=Null
;				r\Adjacent[3]=Null
;				For r2.Rooms = Each Rooms
;					If r<>r2 Then
;						If r2\z=r\z Then
;							If (r2\x)=(r\x+8.0) Then
;								r\Adjacent[0]=r2
;								If r\AdjDoor[0] = Null Then r\AdjDoor[0] = r2\AdjDoor[2]
;							ElseIf (r2\x)=(r\x-8.0)
;								r\Adjacent[2]=r2
;								If r\AdjDoor[2] = Null Then r\AdjDoor[2] = r2\AdjDoor[0]
;							EndIf
;						ElseIf r2\x=r\x Then
;							If (r2\z)=(r\z-8.0) Then
;								r\Adjacent[1]=r2
;								If r\AdjDoor[1] = Null Then r\AdjDoor[1] = r2\AdjDoor[3]
;							ElseIf (r2\z)=(r\z+8.0)
;								r\Adjacent[3]=r2
;								If r\AdjDoor[3] = Null Then r\AdjDoor[3] = r2\AdjDoor[1]
;							EndIf
;						EndIf
;					EndIf
;					If (r\Adjacent[0]<>Null) And (r\Adjacent[1]<>Null) And (r\Adjacent[2]<>Null) And (r\Adjacent[3]<>Null) Then Exit
;				Next
;			Next
;			;debuglog "Status 3"
;			QuickLoadPercent = 94
;			SZL_CurrLoad=SZL_CurrLoad+1
;		ElseIf SZL_CurrLoad=SZL_RoomAmount+6
;			For d.Doors = Each Doors
;				EntityParent(d\obj, 0)
;				If d\obj2 > 0 Then EntityParent(d\obj2, 0)
;				If d\frameobj > 0 Then EntityParent(d\frameobj, 0)
;				If d\buttons[0] > 0 Then EntityParent(d\buttons[0], 0)
;				If d\buttons[1] > 0 Then EntityParent(d\buttons[1], 0)
;				
;				If d\obj2 <> 0 And d\dir = 0 Then
;					MoveEntity(d\obj, 0, 0, 8.0 * RoomScale)
;					MoveEntity(d\obj2, 0, 0, 8.0 * RoomScale)
;				EndIf	
;			Next
;			For it.Items = Each Items
;				EntityType (it\collider, HIT_ITEM)
;				EntityParent(it\collider, 0)
;			Next
;			For sc.SecurityCams= Each SecurityCams
;				sc\angle = EntityYaw(sc\obj) + sc\angle
;				EntityParent(sc\obj, 0)
;			Next
;			For r.Rooms = Each Rooms
;				If (Not r\RoomTemplate\DisableDecals) Then
;					If Rand(4) = 1 Then
;						de.Decals = CreateDecal(Rand(2, 3), EntityX(r\obj)+Rnd(- 2,2), 0.003, EntityZ(r\obj)+Rnd(-2,2), 90, Rand(360), 0)
;						de\Size = Rnd(0.1, 0.4) : ScaleSprite(de\obj, de\Size, de\Size)
;						EntityAlpha(de\obj, Rnd(0.85, 0.95))
;					EndIf
;					
;					If Rand(4) = 1 Then
;						de.Decals = CreateDecal(0, EntityX(r\obj)+Rnd(- 2,2), 0.003, EntityZ(r\obj)+Rnd(-2,2), 90, Rand(360), 0)
;						de\Size = Rnd(0.5, 0.7) : EntityAlpha(de\obj, 0.7) : de\ID = 1 : ScaleSprite(de\obj, de\Size, de\Size)
;						EntityAlpha(de\obj, Rnd(0.7, 0.85))
;					EndIf
;				EndIf
;			Next
;			Local rt.RoomTemplates
;			For rt.RoomTemplates = Each RoomTemplates
;				FreeEntity (rt\obj)
;			Next
;			Local tw.TempWayPoints
;			For tw.TempWayPoints = Each TempWayPoints
;				Delete tw
;			Next
;			;debuglog "Status 4"
;			QuickLoadPercent = 96
;			SZL_CurrLoad=SZL_CurrLoad+1
;		ElseIf SZL_CurrLoad=SZL_RoomAmount+7
;			InitEvents()
;			;debuglog "Status 5"
;			QuickLoadPercent = 97
;			SZL_CurrLoad=SZL_CurrLoad+1
;		ElseIf SZL_CurrLoad=SZL_RoomAmount+8
;			HideEntity Collider
;			InitWayPoints(0,False)
;			ShowEntity Collider
;			;debuglog "Status 6"
;			QuickLoadPercent = 98
;			SZL_CurrLoad=SZL_CurrLoad+1
;		ElseIf SZL_CurrLoad>SZL_RoomAmount+7
;			UpdateDoors()
;			UpdateRooms()
;			For i=0 To 3
;				If PlayerRoom\AdjDoor[i]<>Null
;					PlayerRoom\AdjDoor[i]\open = False
;					PlayerRoom\AdjDoor[i]\openstate = 0
;				EndIf
;			Next
;			If gopt\CurrZone = ClASS_D_ZONE
;				PlayerRoom\RoomDoors[0]\open = False
;				PlayerRoom\RoomDoors[1]\open = True
;			Else
;				PlayerRoom\RoomDoors[0]\open = True
;				PlayerRoom\RoomDoors[1]\open = False
;			EndIf
;			;FreeTextureCache
;			;debuglog "Status 7"
;			QuickLoadPercent = 99
;			SZL_CurrLoad = 9999
;		EndIf
;	EndIf
;	
;End Function

Function GetLeftAnalogStickPitch#(onlydir%=False,invert%=True)
	
	If (Not co\Enabled) Then Return
	If Abs(JoyY())<0.15 And (Not onlydir) Then Return
	If onlydir
		If invert
			Return -JoyYDir()
		EndIf
		Return JoyYDir()
	EndIf
	If invert Then Return -JoyY()
	Return JoyY()
	
End Function

Function GetLeftAnalogStickYaw#(onlydir%=False,invert%=False)
	
	If (Not co\Enabled) Then Return
	If Abs(JoyX())<0.15 And (Not onlydir) Then Return
	If onlydir
		If invert
			Return -JoyXDir()
		EndIf
		Return JoyXDir()
	EndIf
	If invert Then Return -JoyX()
	Return JoyX()
	
End Function

Function GetRightAnalogStickPitch#(onlydir%=False,invert%=True)
	
	If (Not co\Enabled) Then Return
	If Abs(JoyRoll()/180.0)<0.15 Then Return
	If co\InvertAxis[Controller_YAxis] Then invert = Not invert
	If onlydir
		If invert
			Return -Sgn(JoyRoll()/180.0)
		EndIf
		Return Sgn(JoyRoll()/180.0)
	EndIf
	If invert Then Return -(JoyRoll()/180.0)
	Return JoyRoll()/180.0
	
End Function

Function GetRightAnalogStickYaw#(onlydir%=False,invert%=False)
	
	If (Not co\Enabled) Then Return
	If Abs(JoyZ())<0.15 And (Not onlydir) Then Return
	If onlydir
		If invert
			Return -JoyZDir()
		EndIf
		Return JoyZDir()
	EndIf
	If invert Then Return -JoyZ()
	Return JoyZ()
	
End Function

Function InteractHit(key%,controllerkey%)
	
	If (Not co\Enabled)
		If KeyHit(key%) Then Return True
	Else
		If JoyHit(controllerkey%) Then Return True
	EndIf
	Return False
	
End Function

Function GetDPadButtonPress()
	
	If (Not co\Enabled) Then Return
	Return JoyHat()
	
End Function

Function UpdateMenuControllerSelection(maxbuttons%,currTab%,system%=0,maxcurrbuttons%=1)
	
	If (Not co\Enabled) Then Return
	Select system
		Case 0,2,3
			If co\WaitTimer = 0.0
				If system <> 3
					If GetDPadButtonPress()=0
						co\CurrButton[currTab] = co\CurrButton[currTab] - 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = FPSfactor2
						If co\CurrButton[currTab] < 0
							co\CurrButton[currTab] = maxbuttons-1
						EndIf
					ElseIf GetDPadButtonPress()=180
						co\CurrButton[currTab] = co\CurrButton[currTab] + 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = FPSfactor2
						If co\CurrButton[currTab] > maxbuttons-1
							co\CurrButton[currTab] = 0
						EndIf
					EndIf
					
					If GetLeftAnalogStickPitch(True) > 0.0
						co\CurrButton[currTab] = co\CurrButton[currTab] - 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = FPSfactor2
						If co\CurrButton[currTab] < 0
							co\CurrButton[currTab] = maxbuttons-1
						EndIf
					ElseIf GetLeftAnalogStickPitch(True) < 0.0
						co\CurrButton[currTab] = co\CurrButton[currTab] + 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = FPSfactor2
						If co\CurrButton[currTab] > maxbuttons-1
							co\CurrButton[currTab] = 0
						EndIf
					EndIf
				EndIf
				
				If system = 2 Lor system = 3
					If GetDPadButtonPress()=270
						co\CurrButtonSub[currTab] = co\CurrButtonSub[currTab] - 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = FPSfactor2
						If co\CurrButtonSub[currTab] < 0
							co\CurrButtonSub[currTab] = maxcurrbuttons-1
						EndIf
					ElseIf GetDPadButtonPress()=90
						co\CurrButtonSub[currTab] = co\CurrButtonSub[currTab] + 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = FPSfactor2
						If co\CurrButtonSub[currTab] > maxcurrbuttons-1
							co\CurrButtonSub[currTab] = 0
						EndIf
					EndIf
					
					If GetLeftAnalogStickYaw(True) > 0.0
						co\CurrButtonSub[currTab] = co\CurrButtonSub[currTab] + 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = FPSfactor2
						If co\CurrButtonSub[currTab] > maxcurrbuttons-1
							co\CurrButtonSub[currTab] = 0
						EndIf
					ElseIf GetLeftAnalogStickYaw(True) < 0.0
						co\CurrButtonSub[currTab] = co\CurrButtonSub[currTab] - 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = FPSfactor2
						If co\CurrButtonSub[currTab] < 0
							co\CurrButtonSub[currTab] = maxcurrbuttons-1
						EndIf
					EndIf
					
					If co\PressedPrev
						co\CurrButtonSub[currTab] = co\CurrButtonSub[currTab] - 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = FPSfactor2
						If co\CurrButtonSub[currTab] < 0
							co\CurrButtonSub[currTab] = maxcurrbuttons-1
						EndIf
					EndIf
					If co\PressedNext
						co\CurrButtonSub[currTab] = co\CurrButtonSub[currTab] + 1
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = FPSfactor2
						If co\CurrButtonSub[currTab] > maxcurrbuttons-1
							co\CurrButtonSub[currTab] = 0
						EndIf
					EndIf
				EndIf
			EndIf
		Case 1
			If co\WaitTimer = 0.0
				If GetDPadButtonPress()=0
					If co\ScrollBarY > 0
						co\ScrollBarY = Max(co\ScrollBarY-0.05,0)
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = FPSfactor2
					Else
						co\ScrollBarY = 0
					EndIf
				ElseIf GetDPadButtonPress()=180
					If co\ScrollBarY < 1
						co\ScrollBarY = Min(co\ScrollBarY+0.05,1.0)
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = FPSfactor2
					Else
						co\ScrollBarY = 1
					EndIf
				EndIf
				
				If GetLeftAnalogStickPitch(True) > 0.0
					If co\ScrollBarY > 0
						co\ScrollBarY = Max(co\ScrollBarY-0.05,0)
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = FPSfactor2
					Else
						co\ScrollBarY = 0
					EndIf
				ElseIf GetLeftAnalogStickPitch(True) < 0.0
					If co\ScrollBarY < 1
						co\ScrollBarY = Min(co\ScrollBarY+0.05,1.0)
						PlaySound_Strict co\SelectSFX
						co\WaitTimer = FPSfactor2
					Else
						co\ScrollBarY = 1
					EndIf
				EndIf
			EndIf
			
			co\CurrButtonSub[currTab] = 0
			ScrollBarY = CurveValue(co\ScrollBarY,ScrollBarY,20.0)
	End Select
	
	If co\WaitTimer > 0.0 And co\WaitTimer < 15.0
		co\WaitTimer = co\WaitTimer + FPSfactor2
	ElseIf co\WaitTimer >= 15.0
		co\WaitTimer = 0.0
	EndIf
	
End Function

Function MouseAndControllerSelectBox(x%,y%,width%,height%,currButton%=-1,currButtonTab%=0,currButtonSub%=0)
	
	If (Not co\Enabled)
		If MouseOn(x,y,width,height)
			Return True
		EndIf
	Else
		If co\CurrButton[currButtonTab]=currButton
			If co\CurrButtonSub[currButtonTab]=currButtonSub
				Return True
			EndIf
		EndIf
	EndIf
	Return False
	
End Function

Function UpdateControllerSideSelection#(value#,minvalue#,maxvalue#,valuestep#=2.0)
	
	If (Not co\Enabled) Then Return
	If co\WaitTimer=0
		If GetDPadButtonPress()=270
			If value# > minvalue#
				value# = Max(value#-valuestep#,minvalue#)
				PlaySound_Strict co\SelectSFX
				co\WaitTimer = FPSfactor2
			Else
				value# = minvalue#
			EndIf
		ElseIf GetDPadButtonPress()=90
			If value# < maxvalue#
				value# = Min(value#+valuestep#,maxvalue#)
				PlaySound_Strict co\SelectSFX
				co\WaitTimer = FPSfactor2
			Else
				value# = maxvalue#
			EndIf
		EndIf
		
		If GetLeftAnalogStickYaw(True) > 0.0
			If value# < maxvalue#
				value# = Min(value#+valuestep#,maxvalue#)
				PlaySound_Strict co\SelectSFX
				co\WaitTimer = FPSfactor2
			Else
				value# = maxvalue#
			EndIf
		ElseIf GetLeftAnalogStickYaw(True) < 0.0
			If value# > minvalue#
				value# = Max(value#-valuestep#,minvalue#)
				PlaySound_Strict co\SelectSFX
				co\WaitTimer = FPSfactor2
			Else
				value# = minvalue#
			EndIf
		EndIf
		
		If co\PressedNext
			If value# < maxvalue#
				value# = Min(value#+valuestep#,maxvalue#)
				PlaySound_Strict co\SelectSFX
				co\WaitTimer = FPSfactor2
			Else
				value# = maxvalue#
			EndIf
		EndIf
		If co\PressedPrev
			If value# > minvalue#
				value# = Max(value#-valuestep#,minvalue#)
				PlaySound_Strict co\SelectSFX
				co\WaitTimer = FPSfactor2
			Else
				value# = minvalue#
			EndIf
		EndIf
	EndIf
	
	Return value#
	
End Function

Function ResetControllerSelections()
	Local i%
	
	co\WaitTimer# = 0.0
	co\PressedButton% = False
	For i = 0 To Controller_MaxButtons-1
		co\CurrButton[i] = 0
		co\CurrButtonSub[i] = 0
	Next
	co\ScrollBarY# = 0.0
	co\PressedNext = 0
	co\PressedPrev = 0
	co\KeyPad_CurrButton% = 0
	
End Function

Const MaxFluTextures=3
Const FluState_Off=0
Const FluState_Between=1
Const FluState_On=2
Const MaxFluSounds=7
Const FLU_STATE_OFF=0
Const FLU_STATE_ON=1
Const FLU_STATE_FLICKER=2

Type TempFluLight
	Field position.Vector3D
	Field rotation.Vector3D
	Field roomtemplate.RoomTemplates
	Field id%
End Type

Type FluLight
	Field id%
	Field obj%
	Field tex%[MaxFluTextures]
	Field time#
	Field sfx%[MaxFluSounds]
	Field flashsprite%
	Field lightobj%
	Field room.Rooms
	Field state%
End Type

Function CreateFluLight.FluLight(id%)
	Local fll.FluLight = New FluLight
	Local fll2.FluLight
	Local i
	
	fll\id = id
	For fll2 = Each FluLight
		If fll2 <> fll Then
			EntityParent fll2\flashsprite,0
			EntityParent fll2\lightobj,0
			fll\obj = CopyEntity(fll2\obj)
			EntityParent fll2\flashsprite,fll2\obj
			EntityParent fll2\lightobj,fll2\obj
			For i = 0 To MaxFluTextures-1
				fll\tex[i] = fll2\tex[i]
			Next
			For i = 0 To MaxFluSounds-1
				fll\sfx[i] = fll2\sfx[i]
			Next
			Exit
		EndIf
	Next
	
	If fll\obj=0 Then
		fll\obj = LoadMesh_Strict("GFX\map\Props\light_flu.x")
	EndIf
	ScaleEntity fll\obj,RoomScale,RoomScale,RoomScale
	
	If fll\tex[FluState_Off]=0 Then
		For i = 0 To MaxFluTextures-1
			fll\tex[i] = LoadTexture("GFX\map\textures\light_flu"+(i+1)+".jpg",1)
		Next
	EndIf
	EntityTexture fll\obj,fll\tex[FluState_Off]
	HideEntity fll\obj
	
	If fll\sfx[0]=0 Then
		For i = 0 To MaxFluSounds-1
			fll\sfx[i] = LoadSound_Strict("SFX\Room\FluLight"+(i+1)+".ogg")
		Next
	EndIf
	
	fll\flashsprite = CreateSprite()
	Local tex = LoadTexture_Strict("GFX\particle2.png",1+2)
	SpriteViewMode fll\flashsprite,2
	ScaleSprite fll\flashsprite,1.0,1.0
	EntityFX fll\flashsprite,1
	EntityBlend fll\flashsprite,3
	RotateEntity fll\flashsprite,-90,0,0
	EntityTexture fll\flashsprite,tex
	DeleteSingleTextureEntryFromCache tex
	HideEntity fll\flashsprite
	
	fll\lightobj = CreateLight(2)
	LightColor fll\lightobj,1275,1275,1275
	LightRange fll\lightobj,0.025
	
	Return fll
End Function

Function UpdateFluLights()
	Local fll.FluLight
	
	For fll = Each FluLight
		If fll\room = PlayerRoom Lor IsRoomAdjacent(fll\room,PlayerRoom) Then
			ShowEntity fll\obj
			Select fll\state
				Case FLU_STATE_OFF
					EntityFX fll\obj,0
					HideEntity fll\flashsprite
					HideEntity fll\lightobj
					EntityTexture fll\obj,fll\tex[FluState_Off]
				Case FLU_STATE_ON
					EntityFX fll\obj,1
					ShowEntity fll\flashsprite
					ShowEntity fll\lightobj
					EntityTexture fll\obj,fll\tex[FluState_On]
				Case FLU_STATE_FLICKER
					If fll\time = 0.0 Then
						EntityFX fll\obj,0
						HideEntity fll\flashsprite
						HideEntity fll\lightobj
						EntityTexture fll\obj,fll\tex[FluState_Off]
						If Rand(100)=1 Then
							fll\time = FPSfactor
							PlaySound2(fll\sfx[Rand(0,MaxFluSounds-4)],Camera,fll\obj)
						EndIf
					ElseIf fll\time > 0.0 Then
						EntityFX fll\obj,0
						HideEntity fll\flashsprite
						HideEntity fll\lightobj
						EntityTexture fll\obj,fll\tex[FluState_Between]
						fll\time = fll\time + FPSfactor
						If fll\time > 70*Rnd(1.0,3.0) Then
							fll\time = -70*0.2
							PlaySound2(fll\sfx[Rand(4,MaxFluSounds-1)],Camera,fll\obj)
						EndIf
					Else
						EntityFX fll\obj,1
						ShowEntity fll\flashsprite
						ShowEntity fll\lightobj
						EntityTexture fll\obj,fll\tex[FluState_On]
						fll\time = Min(fll\time + FPSfactor,0.0)
					EndIf
			End Select
		Else
			HideEntity fll\obj
		EndIf
	Next
	
End Function

Function InitFluLight(ID%,state%,room.Rooms)
	Local fll.FluLight
	
	For fll = Each FluLight
		If fll\room = room Then
			If fll\id = ID Then
				fll\state = state
			EndIf
		EndIf
	Next
	
End Function

Function TextWithAlign%(x%, y%, txt$, xAlign% = 0, yAlign% = 0)
	
	Text x-(StringWidth(txt)*(xAlign=2)), y-(StringHeight(txt)*(yAlign=2)), txt, (xAlign=1), (yAlign=1)
	
End Function

Function MaskTexture(Texture%, Red, Green, Blue)
	Local x%,y%,Pixel
    Local MaskColor = (Red Shl 16) Or (Green Shl 8) Or Blue
    Local MaskSizeX% = TextureWidth(Texture)
    Local MaskSizeY% = TextureHeight(Texture)
    Local MaskBuffer = TextureBuffer(Texture)
    LockBuffer(MaskBuffer)
    For x = 0 To MaskSizeX
        For y = 0 To MaskSizeY
            Pixel = ReadPixel(x, y, MaskBuffer) And $00FFFFFF
            If (Pixel = MaskColor) Then
                WritePixel(x, y, Pixel, MaskBuffer)
            EndIf
        Next
    Next
    UnlockBuffer(MaskBuffer)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D