
; ~ [MATHEMATICS]

Include "SourceCode\Math.bb"

Type FixedTimesteps
	Field TickDuration#
	Field Accumulator#
	Field PrevTime%
	Field CurrTime%
	Field FPS%
	Field TempFPS%
	Field FPSGoal%
	Field DeltaTime%
End Type

Function SetTickrate(Tickrate%)
	ft\TickDuration = 70.0/Float(Tickrate)
End Function

Function AddToTimingAccumulator(Milliseconds%)
	If (Milliseconds < 1 Lor Milliseconds > 500) Then
		Return
	EndIf
	ft\Accumulator = ft\Accumulator+Max(0,Float(Milliseconds)*70.0/1000.0)
End Function

Function ResetTimingAccumulator()
	ft\Accumulator = 0.0
End Function

Function SetCurrTime(Time%)
	ft\CurrTime = Time%
End Function

Function SetPrevTime(Time%)
	ft\PrevTime = Time%
End Function

Function GetCurrTime%()
	Return ft\CurrTime
End Function

Function GetPrevTime%()
	Return ft\PrevTime
End Function

Function GetTickDuration#()
	Return ft\TickDuration
End Function

SetTickrate(60)

; ~ [LANGUAGE]

Type Loc
	Field Lang$
	Field LangPath$
	Field Localized%
End Type

Global I_Loc.Loc = New Loc

Type LocalString
	Field Section$
	Field Parameter$
	Field Value$
End Type

Global Language$ = "English"

Function InitLanguage()
	
	Select opt\LanguageVal
		Case 0
			Language$ = "English"
		Case 1
			Language$ = "Russian"
	End Select
	
End Function

Function UpdateLang(Lang$)
	Local l.LocalString
	
	If I_Loc\LangPath <> "" Then
		DeleteINIFile(I_Loc\LangPath + "Data\local.ini")
	EndIf
	If Lang = "English" Then
		I_Loc\Lang = ""
		I_Loc\LangPath = ""
		I_Loc\Localized = False
	Else
		I_Loc\Lang = Lang
		I_Loc\LangPath = "Localization\" + Lang + "\"
		I_Loc\Localized = True
	EndIf
	For l.LocalString = Each LocalString
		Delete l
	Next
	
	InitFonts()
	
End Function

InitLanguage()

UpdateLang(Language$)

Function SetLocalString(Section$, Parameter$)
	
	Local l.LocalString = New LocalString
	l\Value = GetLocalString(Section, Parameter)
	l\Section = Section
	l\Parameter = Parameter
	
End Function

Function GetLocalString$(Section$, Parameter$)
	Local l.LocalString
	
	For l.LocalString = Each LocalString
		If l\Section = Section And l\Parameter = Parameter Then
			Return l\Value
		EndIf
	Next
	
	Local temp$
	
	If I_Loc\Localized And FileType(I_Loc\LangPath + "Data\local.ini") = 1 Then
		temp=GetINIString(I_Loc\LangPath + "Data\local.ini", Section, Parameter)
		If temp <> "" Then
			l.LocalString = New LocalString
            l\Section = Section
            l\Parameter = Parameter
            l\Value = temp
			Return temp
		EndIf
	EndIf
	
	temp = GetINIString("Data\Local.ini", Section, Parameter)
	If temp <> "" Then
		l.LocalString = New LocalString
		l\Section = Section
		l\Parameter = Parameter
		l\Value = temp
		Return temp
	EndIf
	
	Return Section + "." + Parameter
	
End Function

Function GetLocalStringR$(Section$, Parameter$, Replace$)
	
	Return Replace(GetLocalString(Section, Parameter), "%s", Replace)
	
End Function

Include "SourceCode\StrictLoads.bb"
Include "SourceCode\KeyName.bb"
Include "SourceCode\KeyBinds.bb"

LoadKeyBinds()

; ~ [FONT]

;[Block]
Const MaxFontAmount = 10
Const Font_Default = 0
Const Font_Menu = 1
Const Font_Digital_Small = 2
Const Font_Digital_Large = 3
Const Font_Journal = 4
Const Font_Default_Large = 5
Const Font_Menu_Medium = 6
Const Font_Menu_Small = 7
Const Font_Digital_Medium = 8
Const Font_Default_Medium = 9
;[End Block]

Type Fonts
	Field UpdaterFont
	Field ConsoleFont
	Field Font[MaxFontAmount]
End Type

InitController()

Const VersionNumber$ = "0.1.2"
Const CompatibleNumber$ = "0.1.1"

Global ButtonSFX[4]

ButtonSFX[0] = LoadSound_Strict("SFX\Interact\Button.ogg")
ButtonSFX[1] = LoadSound_Strict("SFX\Interact\Button2.ogg")
ButtonSFX[2] = LoadSound_Strict("SFX\Interact\Button3.ogg")
ButtonSFX[3] = LoadSound_Strict("SFX\Interact\Button4.ogg")

;[Block]

Global LauncherIMG%
Global fresize_image%, fresize_texture%, fresize_texture2%
Global fresize_cam%
Global WireframeState
Global HalloweenTex
Global RealGraphicWidth%,RealGraphicHeight%
Global AspectRatioRatio#
Global TextureFloat#
Select opt\TextureDetails%
	Case 0
		TextureFloat# = 0.8
	Case 1
		TextureFloat# = 0.0
	Case 2
		TextureFloat# = -0.8
End Select

Global AchvIni$ = "Data\Achievements.ini"
Global Data294$ = "Data\SCP-294.ini"

AspectRatioRatio = 1.0
UpdateLauncher()
Delete Each Resolution

If opt\DisplayMode=1 Then
	Graphics3DExt(DesktopWidth(), DesktopHeight(), 0, 4)
	
	RealGraphicWidth = DesktopWidth()
	RealGraphicHeight = DesktopHeight()
	
	AspectRatioRatio = (Float(opt\GraphicWidth)/Float(opt\GraphicHeight))/(Float(RealGraphicWidth)/Float(RealGraphicHeight))
Else
	Graphics3DExt(opt\GraphicWidth, opt\GraphicHeight, 0, 2)
	
	RealGraphicWidth = opt\GraphicWidth
	RealGraphicHeight = opt\GraphicHeight
EndIf
If FileType(I_Loc\LangPath + Data294) = 1 Then
	Data294 = I_Loc\LangPath + Data294
EndIf
If FileType(I_Loc\LangPath + AchvIni) = 1 Then
	AchvIni = I_Loc\LangPath + AchvIni
EndIf

Global MenuScale# = (opt\GraphicHeight / 1024.0)

LoadMenuImages()

SetBuffer(BackBuffer())

Global CurTime%, PrevTime%, LoopDelay%, FPSfactor#, FPSfactor2#
Global Framelimit% = GetINIInt(gv\OptionFile, "options", "framelimit", 120)
Global Vsync% = GetINIInt(gv\OptionFile, "options", "vsync")
Global CurrFrameLimit# = (Framelimit%-29)/100.0
TextureAnisotropic 2^(opt\TextureFiltering+1)
TextureLodBias TextureFloat#
Global ScreenGamma# = GetINIFloat(gv\OptionFile, "options", "screengamma", 1.0)

SeedRnd MilliSecs()

;[End block]

Global GameSaved%
Global CanSave% = True

AppTitle "SCP - Security Stories v"+VersionNumber

Delay 100

;[Block]

Global CursorIMG% = LoadImage_Strict("GFX\Menu\cursor.png")

Include "SourceCode\LoadingScreens.bb"

InitFonts()

Global CreditsFont%,CreditsFont2%

DrawLoading(0, True,"","Key_Values")

; ~ [KEYS]

Global Viewport_Center_X% = opt\GraphicWidth / 2, Viewport_Center_Y% = opt\GraphicHeight / 2
Global MouseLook_X_Inc# = 0.3
Global MouseLook_Y_Inc# = 0.3
Global Mouse_Left_Limit% = 250 * MenuScale, Mouse_Right_Limit% = opt\GraphicWidth - Mouse_Left_Limit
Global Mouse_Top_Limit% = 150 * MenuScale, Mouse_Bottom_Limit% = opt\GraphicHeight - Mouse_Top_Limit
Global Mouse_X_Speed_1#, Mouse_Y_Speed_1#

Global Mesh_MinX#, Mesh_MinY#, Mesh_MinZ#
Global Mesh_MaxX#, Mesh_MaxY#, Mesh_MaxZ#
Global Mesh_MagX#, Mesh_MagY#, Mesh_MagZ#

Global KEY_RIGHT = GetINIInt(gv\OptionFile, "binds", "Right key", 32)
Global KEY_LEFT = GetINIInt(gv\OptionFile, "binds", "Left key", 30)
Global KEY_UP = GetINIInt(gv\OptionFile, "binds", "Up key", 17)
Global KEY_DOWN = GetINIInt(gv\OptionFile, "binds", "Down key", 31)
Global KEY_BLINK = GetINIInt(gv\OptionFile, "binds", "Blink key", 57)
Global KEY_SPRINT = GetINIInt(gv\OptionFile, "binds", "Sprint key", 42)
Global KEY_INV = GetINIInt(gv\OptionFile, "binds", "Inventory key", 15)
Global KEY_CROUCH = GetINIInt(gv\OptionFile, "binds", "Crouch key", 29)
Global KEY_SAVE = GetINIInt(gv\OptionFile, "binds", "Save key", 63)
Global KEY_LOAD = GetINIInt(gv\OptionFile, "binds", "Load key", 65)
Global KEY_CONSOLE = GetINIInt(gv\OptionFile, "binds", "Console key", 61)
Global KEY_RELOAD = GetINIInt(gv\OptionFile, "binds", "Reload key", 19)
Global KEY_HOLSTERGUN = GetINIInt(gv\OptionFile, "binds", "Holstergun key", 16)
Global KEY_FIREMODE = GetINIInt(gv\OptionFile, "binds", "Firemode key", 48)
Global KEY_ATTACHMENTS = GetINIInt(gv\OptionFile, "binds", "Attachment menu key", 45)
Global KEY_ALT_LOOK = GetINIInt(gv\OptionFile, "binds", "Alt Look key", 56)

; ~ [PLAYER]

Global KillTimer#, KillAnim%, FallTimer#, DeathTimer#
Global Sanity#, ForceMove#, ForceAngle#
Global RestoreSanity%
Global Playable% = True
Global BLINKFREQ#
Global BlinkTimer#, EyeIrritation#, EyeStuck#, BlinkEffect# = 1.0, BlinkEffectTimer#
Global Stamina#, StaminaEffect#=1.0, StaminaEffectTimer#
Global CameraShakeTimer#, Vomit%, VomitTimer#, Regurgitate%
;Global SCP1025state#[6]
Global HeartBeatRate#, HeartBeatTimer#, HeartBeatVolume#
Global SuperMan%, SuperManTimer#
Global HealTimer#
Global RefinedItems%

Global MousePosX#, MousePosY#

Global DropSpeed#, HeadDropSpeed#, CurrSpeed#
Global user_camera_pitch#, side#
Global Crouch%, CrouchState#
Global PlayerZone%, PlayerRoom.Rooms
Global GrabbedEntity%
Global InvertMouse% = GetINIInt(gv\OptionFile, "options", "invert mouse y")
Global MouseHit1%, MouseDown1%, MouseHit2%, MouseDown2%, MouseHit3%, MouseDown3%, DoubleClick%, LastMouseHit1%, MouseUp1%
Global GodMode%, NoClip%, NoClipSpeed# = 2.0
;Global CoffinDistance#
Global PlayerSoundVolume#

Global Shake#
Global ExplosionTimer#
Global LightsOn% = True
Global SoundTransmission%

Global MainMenuOpen%, MenuOpen%, StopHidingTimer#, InvOpen%
Global OtherOpen.Items = Null
Global SelectedEnding$, EndingScreen%, EndingTimer#
Global KeypadInput$, KeypadTimer#, KeypadMSG$
Global DrawHandIcon%
Global DrawArrowIcon%[4]
Global AccessCode%[4]

Global IsCutscene% = False

; ~ [MISC]

Global MTFtimer#, MTFrooms.Rooms[10], MTFroomState%[10]
Global CITimer#
Global RadioState#[9]
Global RadioState3%[7]
Global RadioState4%[9]
Global RadioCHN%[8]
Global PlayTime%
Global ConsoleFlush%
Global ConsoleFlushSnd% = 0, ConsoleMusFlush% = 0, ConsoleMusPlay% = 0
Global InfiniteStamina% = False
Global NVBlink%
Global IsNVGBlinking% = False
;[End block]

Include "SourceCode\Achievements.bb"
Include "SourceCode\Difficulty.bb"
Include "SourceCode\SCPFunctions.bb"

; ~ [MENUS]

Global AttachmentMenuOpen%

; ~ [CONSOLE]

Global ConsoleOpen%, ConsoleInput$
Global ConsoleScroll#,ConsoleScrollDragging%
Global ConsoleMouseMem%
Global ConsoleReissue.ConsoleMsg = Null
Global ConsoleR% = 255,ConsoleG% = 255,ConsoleB% = 255

Type Cheats
	Field CDScream%
	Field Mini173%
	Field OwO%
End Type

Function InitCheats()
	Local Cheat.Cheats = New Cheats
End Function

InitCheats()

Global DebugHUD%
Global BlurVolume#, BlurTimer#
Global LightBlink#, LightFlash#
Global Camera%, CameraShake#, BigCameraShake#, CurrCameraZoom#

Global BumpEnabled% = GetINIInt(gv\OptionFile, "options", "bump mapping enabled", 1)
Global HUDenabled% = GetINIInt(gv\OptionFile, "options", "HUD enabled", 1)
Global Brightness% = GetINIFloat(gv\OptionFile, "options", "brightness", 20)
Global CameraFogNear# = GetINIFloat(gv\OptionFile, "options", "camera fog near", 0.5)
Global CameraFogFar# = GetINIFloat(gv\OptionFile, "options", "camera fog far", 6.0)
Global StoredCameraFogFar# = CameraFogFar
Global MouseSens# = GetINIFloat(gv\OptionFile, "options", "mouse sensitivity")

Include "SourceCode\DreamFilter.bb"

DrawLoading(25, True,"","Sounds")

Include "SourceCode\Sounds.bb"

Include "SourceCode\Subtitles.bb"

DrawLoading(30, True,"","Sounds")

;[End block]

;[Block]

Global PlayCustomMusic% = False, CustomMusic% = 0

;Global Monitor2, Monitor3, MonitorTexture2, MonitorTexture3, MonitorTexture4, MonitorTextureOff
;Global MonitorTimer# = 0.0, MonitorTimer2# = 0.0, UpdateCheckpoint1%, UpdateCheckpoint2%
Global PlayerDetected%
Global NoTarget% = False
Global AmbientLightRoomTex%, AmbientLightRoomVal%

Global EnableUserTracks% = GetINIInt(gv\OptionFile, "audio", "enable user tracks")
Global UserTrackMode% = GetINIInt(gv\OptionFile, "audio", "user track setting")
Global UserTrackCheck% = 0, UserTrackCheck2% = 0
Global UserTrackMusicAmount% = 0, CurrUserTrack%, UserTrackFlag% = False
Global UserTrackName$[64]

Global OptionsMenu% = 0
Global QuitMSG% = 0
Global InFacility% = True
Global PrevMusicVolume# = opt\MusicVol
Global PrevSFXVolume# = opt\SFXVolume#
Global DeafPlayer% = False
Global DeafTimer# = 0.0
Global IsZombie% = False
Global room2gw_brokendoor% = False
Global room2gw_x# = 0.0
Global room2gw_z# = 0.0
;Global Menu_TestIMG
Global menuroomscale# = 8.0 / 2048.0
Global CurrMenu_TestIMG$ = ""

Global ParticleAmount% = GetINIInt(gv\OptionFile,"options","particle amount", 2)

Global NavBG
Global WeaponFireModeIcons%[2]
Global LightConeModel
Global ParticleEffect[10]

Global NVGImages = LoadAnimImage("GFX\battery.png",64,64,0,2)
MaskImage NVGImages,255,0,255

;Global NPC049OBJ, NPC0492OBJ
;Global ClerkOBJ
Global IntercomStreamCHN%
Global ForestNPC,ForestNPCTex,ForestNPCData#[3]
;[End Block]

;! ~ [IMAGES]

Global O5_Screen[3]
Global SCP_963_2_Screen[2]
Global Surveil_Room_Textures[2]
Global Checkpoint_Screen[3]
Global Class_D_Screen[3]
Global Satellite_Screen[2]
Global Tram_Screen[2]

Global PauseMenuIMG%[2]

Global inv_character_img%

Global SprintIcon%
Global BlinkIcon[2]
Global CrouchIcon%

Global StaminaMeterIMG%

Global KeypadHUD

Global Panel294, Using294%, Input294$

;! ~ [ITEMS]

DrawLoading(35, True,"","Items")

Include "SourceCode\Items.bb"

;! ~ [PARTICLES]

DrawLoading(40, True,"","Particles")

Include "SourceCode\Particles.bb"

Include "SourceCode\Rain.bb"

;! ~ [MAP]

;gopt\CurrZone% = 0

DrawLoading(45, True,"","Doors")

Include "SourceCode\Doors.bb"

DrawLoading(50,True,"","Materials")

Include "SourceCode\Materials.bb"

Include "SourceCode\MapSystem.bb"

Include "SourceCode\SZL.bb"

DrawLoading(55, True,"","Rooms")

LoadRoomTemplates("Data\rooms.ini")

;! ~ [WEAPONS]

Include "SourceCode\MTF.bb"

DrawLoading(60, True,"","Weapons")

Include "SourceCode\Guns.bb"

DrawLoading(65, True,"","Textures")

Include "SourceCode\TextureCache.bb"

DrawLoading(70, True,"","NPCs")

Include "SourceCode\Mission.bb"

;! ~ [NPCS]

DrawLoading(75,True,"","NPCs")

Include "SourceCode\NPCs.bb"

DrawLoading(80, True,"","Events")

;! ~ [EVENTS]

Include "SourceCode\Events.bb"

; ~ Collision constants
;[Block]
Const HIT_MAP% = 1
Const HIT_PLAYER% = 2
Const HIT_ITEM% = 3
Const HIT_DEAD% = 4
Const HIT_RAIN% = 5
Const HIT_GRENADE% = 6
; ~ Multiplayer
Const HIT_PLAYER_MP% = 7
Const HIT_NPC_MP% = 8
;[End Block]

Collisions HIT_PLAYER, HIT_MAP, 2, 2
Collisions HIT_PLAYER, HIT_PLAYER, 1, 2
Collisions HIT_ITEM, HIT_MAP, 2, 2
Collisions HIT_GRENADE, HIT_MAP, 2, 2
Collisions HIT_RAIN, HIT_MAP, 2, 2
Collisions HIT_DEAD, HIT_MAP, 2, 2

Collisions HIT_PLAYER_MP, HIT_MAP, 2, 2
Collisions HIT_PLAYER_MP, HIT_NPC_MP, 1, 2
Collisions HIT_NPC_MP, HIT_MAP, 2, 2

Type AudioControl
	Field MasterVol#
	Field MusicVol#
	Field VoiceVol#
	Field EnviromentVol#
End Type

DrawLoading(90, True,"","Meshes")

;! ~ [MESHES & TEXTURES]

; ~ [NPC Model Constants]

;[Block]
Const MaxNPCModels = 22
Const ZombieTypes = 6
Const GuardZombieTypes = 3

Const Model_035 = 0
Const Model_049 = 1
Const Model_076 = 2
Const Model_096 = 3
Const Model_106 = 4
Const Model_173 = 5
Const Model_173_Head = 6
Const Model_372 = 7
Const Model_457 = 8
Const Model_513 = 9
Const Model_682 = 10
Const Model_860 = 11
Const Model_966 = 12
Const Model_1048 = 13

Const Model_Guard = 14
Const Model_Vincent = 15
Const Model_NTF = 16
Const Model_CI = 17
Const Model_Class_D = 18
Const Model_Class_D_Armed = 19

Const Model_Tentacle = 20

Const Model_SMAN = 21
;[End Block]

; ~ Other Constants

;[Block]
Const NTF_Texture_Medic = 0
Const NTF_Texture_Commander = 1
;[End Block]

; ~ Globals

;[Block]
Global NPCModel[MaxNPCModels]
Global ZombieModel[ZombieTypes]
Global GuardZombieModel[GuardZombieTypes]
Global NTFTexture[2]

Global FogTexture%, Fog%
Global InfectTexture%, InfectOverlay%
Global SCP268Overlay%, SCP268Texture%
Global SCP268Icon%, SCP1033RUIcon%, SCP207Icon%, SCP198Icon%, HDSIcon%
Global SCP409Overlay%, SCP409Texture%
Global HelmetOverlay%, HelmetTexture%
Global GasMaskOverlay%, GasMaskTexture%
Global HazmatOverlay%, HazmatTexture%
Global DarkTexture%, Dark%
Global Collider%, Head%

Global FogNVTexture%
Global NVTexture%, NVOverlay%
Global TeslaTexture%
Global LightTexture%, Light%
Global LightSpriteTex%[4]
Global LeverOBJ%, LeverBaseOBJ%
Global Monitor%, MonitorTexture%
Global CamBaseOBJ%, CamOBJ%
Global LiquidObj%
Global ApacheObj%,ApacheRotorObj%

Global UnableToMove% = False
Global ShouldEntitiesFall% = True
Global PlayerFallingPickDistance# = 10.0
;[End Block]

;! ~ [MULTIPLAYER]

Include "SourceCode\Multiplayer\Multiplayer_Base.bb"

;! ~ [PlAYER & MENUS]

Include "SourceCode\Player.bb"

Include "SourceCode\Menu.bb"
Include "SourceCode\Menu3D.bb"
Include "SourceCode\Console.bb"

DrawLoading(95, True,"","Starting")

MainMenuOpen = True

InitConsole(2)

Load3DMenu()

;! ~ [MENU]

FlushKeys()
FlushMouse()
FlushJoy()

DrawLoading(100, True,"","Done")

If opt\PlayStartupVideos Then PlayStartupVideos()

CurrMusicVolume# = 0.01

LoopDelay = MilliSecs()

Global CurrTrisAmount%

Global Input_ResetTime# = 0.0

;! ~ [TYPE INSTANCES]

;[Block]

; ~ [PLAYER]

Type Wearible
	Field NVGTimer#
	Field SCRAMBLECHN%
	Field SCRAMBLESFX%
End Type

Type HazardousDefenceSuit
	Field BootUpTimer#
	Field Timer#
	Field ExplodeTimer#
	Field isBroken%
	Field Health#
	Field CantWear%
	
	Field Sound[25]
	Field SoundCHN%
End Type

Type Messages
	Field Txt$
	Field HintTxt$
	Field HintTxt_Y#
	Field HintTimer#
	Field DeathTxt$
	Field Timer#
	Field isSplash%
	Field R#,G#,B#
End Type

Type SplashMsg
	Field Timer#
	Field Speed#
	Field ShowTime#
	Field CurrentLength%
	Field DisplayAmount#
	Field X#, Y#
	Field Txt$
	Field Centered%
	Field R#,G#,B#
End Type

Type AutoSaveMessage
	Field Icon%
	Field Camera%
	Field Txt_Y#
	Field Timer#
End Type

; ~ [EVENTS&GAME]

Type Chapters
	Field Unlocked#
	Field Current#
	Field NTFUnlocked#
	Field NTFCurrent#
	Field DUnlocked#
	Field DCurrent#
	Field SurfaceEnding%
	Field SoundCHN[1]
	Field Sound[1]
End Type

Type EventConstants
	Field SuccessRocketLaunch%
	Field EzDoorOpened%
	Field WasInHCZ%
	Field NewCavesEvent%
	Field CIArrived%
	Field WasInRoom2_SL%
	Field WasInLCZCore%
	Field UnlockedGateDoors%
	Field KilledGuard%
	Field NTFArrived%
	Field WasInO5%
	Field WasIn076%
	Field After076Timer#
	Field WasInCaves%
	Field WasInO5Again%
	Field WasInPO%
	Field WasInReactor%
	Field WasInBCZ%
	Field Contained008%
	Field Contained049%
	Field Contained409%
	
	Field UnlockedAirlock%
	
	Field UnlockedWolfnaya%
	Field ChanceToSpawnWolfNote
	
	Field FusesAmount
	
	Field UnlockedEMRP%
	Field UnlockedHDS%
	
	Field WasInLWS%
	Field WasInWS%
	Field WasInEWS%
	Field WasInHWS%
	Field WasInSWS%
	Field WasInAllSupplies%
	
	Field IntercomEnabled%
	Field IntercomIsReady%
	Field IntercomTimer#
	
	Field OmegaWarheadActivated%
	Field OmegaWarheadDetonate%
	Field OmegaWarheadTimer#
End Type

; ~ [SCPs]

Type SCP005
	Field ChanceToSpawn%
End Type

Type SCP008
	Field Timer#
End Type

Type SCP016
	Field Timer#
End Type

Type SCP035
	Field Possessed%
End Type

;Type SCP059
;	Field Timer#
;	Field Using%
;End Type

Type SCP109
    Field Timer#
    Field Used%
    Field Sound%[1]
	Field Vomit%
	Field VomitTimer#
End Type

Type SCP127
	Field RestoreTimer#
End Type

Type SCP198
	Field Timer#
	Field DeathTimer#
	Field Injuries#
	Field Vomit%
	Field VomitTimer#
End Type

Type SCP207
    Field Timer#
	Field DeathTimer#
	Field Factor%
	Field Limit%
End Type

Type SCP268
    Field Using%
    Field Timer#
    Field Sound%[2]
    Field SoundCHN%[1]
End Type

Type SCP330
	Field Taken#
	Field Timer#
End Type

;Type SCP357
;	Field Timer#
;	Field Using%
;End Type
;
;Type SCP402
;    Field Using%
;    Field Timer#
;End Type

Type SCP409
	Field Timer#
	Field Revert%
End Type 

Type SCP427
	Field Using%
	Field Timer#
	Field Sound[2]
	Field SoundCHN[2]
End Type

Type SCP500
    Field Limit%
End Type

Type SCP714
	Field Using%
End Type

;Type SCP1025
;	Field State#[6]
;End Type

Type SCP1033RU
    Field HP%
    Field DHP%
    Field Using%
    Field Sound%[3]
    Field Sound2%[1]
End Type

;Type SCP1079
;    Field Foam#
;    Field Trigger%
;    Field Take%
;    Field Limit%
;End Type

;Type SCP1102RU
;	Field IsInside%
;	Field State#
;	Field Sound[1]
;End Type

;[End Block]

InitErrorMsgs(9)
SetErrorMsg(0, GetLocalString("Errors","error_occured")+VersionNumber+Chr(10)+GetLocalString("Errors","save_compatible")+CompatibleNumber+GetLocalString("Errors","engine_version")+SystemProperty("blitzversion"))
SetErrorMsg(1, "OS: "+SystemProperty("os")+" "+gv\OSBit+" bit (Build: "+SystemProperty("osbuild")+")")
SetErrorMsg(2, "CPU: "+Trim(SystemProperty("cpuname"))+" (Arch: "+SystemProperty("cpuarch")+", "+GetEnv("NUMBER_OF_PROCESSORS")+" Threads)")
SetErrorMsg(8, Chr(10)+GetLocalString("Errors","dont_report"))

;----------------------------------------------------------------------------------------------------------------------------------------------------
;----------------------------------------------       		MAIN LOOP                 ---------------------------------------------------------------
;----------------------------------------------------------------------------------------------------------------------------------------------------

GlobalGameLoop()
If opt\SteamEnabled Then Steam_Shutdown()

Function GlobalGameLoop()
	
	Repeat
		Local CurrDelta% = MilliSecs()
		
		SetErrorMsg(3, "GPU: "+GfxDriverName(CountGfxDrivers())+" ("+((TotalVidMem()/1024)-(AvailVidMem()/1024))+" MB/"+(TotalVidMem()/1024)+" MB)")
		SetErrorMsg(4, "Triangles rendered: "+CurrTrisAmount+", Active textures: "+ActiveTextures()+Chr(10))
		If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
			If PlayerRoom <> Null Then
				SetErrorMsg(5, "Map seed: "+RandomSeed + ", Room: " + PlayerRoom\RoomTemplate\Name+" (" + Floor(EntityX(PlayerRoom\obj) / 8.0 + 0.5) + ", " + Floor(EntityZ(PlayerRoom\obj) / 8.0 + 0.5) + ", angle: "+PlayerRoom\angle + ")")
				
				Local ev.Events
				
				For ev.Events = Each Events
					If ev\room = PlayerRoom Then
						SetErrorMsg(6, "Room event: "+ev\EventName+" (" +ev\EventState[0]+", "+ev\EventState[1]+", "+ev\EventState[2]+")"+Chr(10))
						Exit
					EndIf
				Next
			EndIf
		ElseIf gopt\GameMode = GAMEMODE_MULTIPLAYER Then
			SetErrorMsg(5, "Map: "+mp_I\MapInList\Name)
			SetErrorMsg(6, "Gamemode: "+mp_I\Gamemode\name+Chr(10))
		EndIf
		
		CatchErrors("Global main loop")
		Cls
		
		Local elapsedMilliseconds%
		SetCurrTime(MilliSecs())
		elapsedMilliseconds = GetCurrTime()-GetPrevTime()
		AddToTimingAccumulator(elapsedMilliseconds)
		SetPrevTime(GetCurrTime())
		
		If Framelimit > 0 Then
			;Framelimit
			Local WaitingTime% = (1000.0 / Framelimit) - (MilliSecs() - LoopDelay)
			Delay WaitingTime%
			
			LoopDelay = MilliSecs()
		EndIf
		
		FPSfactor = GetTickDuration()
		FPSfactor2 = FPSfactor
		
		MousePosX = MouseX()
		MousePosY = MouseY()
		
		If MainMenuOpen Then
			MainLoopMenu()
		Else
			If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
				MainLoop()
			Else
				If mp_I\PlayState=GAME_SERVER
					MPMainLoop()
				Else
					MPMainLoopClient()
				EndIf
			EndIf
		EndIf
		
		GammaUpdate()
		
		If opt\ShowFPS Then
			If ft\fpsgoal < MilliSecs() Then
				ft\fps = ft\tempfps
				ft\tempfps = 0
				ft\fpsgoal = MilliSecs() + 1000
			Else
				ft\tempfps = ft\tempfps + 1
			EndIf
		EndIf
		
		;Text 700, 90, SystemProperty("os")+" (Build: "+SystemProperty("osbuild")+"), CPU: "+GetEnv("PROCESSOR_IDENTIFIER")+" (Arch: "+GetEnv("PROCESSOR_ARCHITECTURE")+", "+GetEnv("NUMBER_OF_PROCESSORS")+" Threads)"
		;Text 700, 110, "Phys. Memory: "+((TotalPhys()/1024)-(AvailPhys()/1024))+" MB/"+(TotalPhys()/1024)+" MB ("+(TotalPhys()-AvailPhys())+" KB/"+TotalPhys()+" KB). CPU Usage: "+MemoryLoad()+"%"
		;Text 700, 130, "Virtual Memory: "+((TotalVirtual()/1024)-(AvailVirtual()/1024))+" MB/"+(TotalVirtual()/1024)+" MB ("+(TotalVirtual()-AvailVirtual())+" KB/"+TotalVirtual()+" KB)"
		;Text 700, 150, "Video Memory: "+((TotalVidMem()/1024)-(AvailVidMem()/1024))+" MB/"+(TotalVidMem()/1024)+" MB ("+(TotalVidMem()-AvailVidMem())+" KB/"+TotalVidMem()+" KB)"
		
		If opt\SteamEnabled Then Steam_Update()
		UpdateRichPresence()
		
		Flip Vsync
		
		ft\DeltaTime = MilliSecs() - CurrDelta
		
		CatchErrors("Global main loop / uncaught")
	Forever
	
End Function

Function MainLoop()
	Local r.Rooms,e.Events
	Local i%
	
	While (ft\accumulator>0.0)
		ft\accumulator = ft\accumulator-GetTickDuration()
		If (ft\accumulator<=0.0) Then CaptureWorld()
		
		If MenuOpen Lor InvOpen Lor OtherOpen<>Null Lor ConsoleOpen Lor SelectedScreen <> Null Lor Using294 Then FPSfactor = 0
		If d_I <> Null Then 
			If d_I\SelectedDoor <> Null Then
				FPSfactor = 0
			EndIf
		EndIf	
		If mi_I\IsEnding Then FPSfactor = 0
		If ConsoleOpen Lor InvOpen
			FPSfactor=0.0
		EndIf
		
		If Input_ResetTime > 0 And FPSfactor > 0.0 Then
			Input_ResetTime = Max(Input_ResetTime - FPSfactor, 0.0)
		Else
			DoubleClick = False
			If (Not co\Enabled)
				MouseHit1 = MouseHit(1)
				If MouseHit1
					If MilliSecs() - LastMouseHit1 < 800 Then DoubleClick = True
					LastMouseHit1 = MilliSecs()
				EndIf
				Local prevmousedown1 = MouseDown1
				MouseDown1 = MouseDown(1)
				If prevmousedown1 = True And MouseDown1=False Then MouseUp1 = True Else MouseUp1 = False
				
				MouseHit2 = MouseHit(2)
				MouseDown2 = MouseDown(2)
				
				MouseHit3 = MouseHit(3)
				MouseDown3 = MouseDown(3)
				
				KeyHitUse = KeyHit(KEY_USE)
				KeyDownUse = KeyDown(KEY_USE)
			Else
				;[CONTROLLER]
				MouseHit1 = JoyHit(CK_LMouse)
				If MouseHit1 Then
					If MilliSecs() - LastMouseHit1 < 800 Then DoubleClick = True
					LastMouseHit1 = MilliSecs()
				EndIf
				prevmousedown1 = MouseDown1
				MouseDown1 = JoyDown(CK_LMouse)
				If prevmousedown1 = True And MouseDown1=False Then MouseUp1 = True Else MouseUp1 = False
				MouseHit2 = JoyHit(CK_RMouse)
				MouseDown2 = JoyDown(CK_RMouse)
				MouseHit3 = JoyHit(CK_MMouse)
				MouseDown3 = JoyDown(CK_MMouse)
				KeyHitUse = JoyHit(CK_Use)
				KeyDownUse = JoyDown(CK_Use)
			EndIf
		EndIf
		
		;If (Not MouseDown1) And (Not MouseHit1) Then GrabbedEntity = 0
		If (Not KeyDownUse) And (Not KeyHitUse) Then GrabbedEntity = 0
		
		UpdateMusic()
		If opt\EnableSFXRelease Then AutoReleaseSounds()
		If mi_I\EndSFX<>0
			If mi_I\EndingTimer=0.0
				If mi_I\EndSFX_Vol>0.0
					mi_I\EndSFX_Vol = Max(mi_I\EndSFX_Vol-0.01*FPSfactor2,0)
					SetStreamVolume_Strict(mi_I\EndSFX,(opt\MusicVol)*mi_I\EndSFX_Vol)
				Else
					StopStream_Strict(mi_I\EndSFX)
					mi_I\EndSFX = 0
				EndIf
			EndIf
		EndIf
		
		UpdateStreamSounds()
		
		DrawHandIcon = False
		For i = 0 To 3
			DrawArrowIcon[i] = False
		Next
		
		RestoreSanity = True
		ShouldEntitiesFall = True
		
		ShouldUpdateWater = ""
		WaterRender_IgnoreObject = 0
		
		If FPSfactor > 0 Then UpdateSecurityCams()
		
		If (Not MenuOpen) And (Not InvOpen) And (OtherOpen=Null) And (d_I\SelectedDoor = Null) And (ConsoleOpen = False) And (Using294 = False) And (SelectedScreen = Null) And EndingTimer=>0 Then
			If (Not PlayerInNewElevator) Then
				Select gopt\CurrZone
					Case LCZ
						ShouldPlay = MUS_LCZ
					Case HCZ
						ShouldPlay = MUS_HCZ
					Case EZ
						ShouldPlay = MUS_EZ
					Case RCZ
						ShouldPlay = MUS_RCZ
					Case BCZ
						ShouldPlay = MUS_BCZ
					Case REACTOR_AREA, GATE_A_ROAD
						ShouldPlay = MUS_NULL
					Case GATE_A_INTRO
						ShouldPlay = Rand(MUS_INTRODUCTION_NTF,MUS_INTRODUCTION_NTF_2)
					Case GATE_A_TOPSIDE, GATE_B_TOPSIDE, GATE_C_TOPSIDE, GATE_D_TOPSIDE
						ShouldPlay = MUS_SURFACE
					Case AREA_076
						ShouldPlay = MUS_CONT_076
					Case CLASSD_CELLS
						ShouldPlay = MUS_LCZ_CLASSIC
					Case AREA_106_ESCAPE
						ShouldPlay = MUS_THE_UNKNOWN
				End Select
			EndIf
		EndIf
		
		If FPSfactor>0 Then
			;If (Not InvOpen) Then CurrOverhaul=0
		Else
			co\PressedButton = JoyHit(CKM_Press)
			co\PressedNext = JoyDown(CKM_Next)
			co\PressedPrev = JoyDown(CKM_Prev)
			If co\PressedNext And co\PressedPrev
				co\PressedNext = False
				co\PressedPrev = False
			EndIf
		EndIf
		
		If PlayerRoom\RoomTemplate\Name <> "pocketdimension" And PlayerRoom\RoomTemplate\Name <> "gate_a_topside" And PlayerRoom\RoomTemplate\Name <> "gate_b_topside" And (Not MenuOpen) And (Not ConsoleOpen) And (Not InvOpen) Then 
			
			If mi_I\EndingTimer = 0.0
				If PlayerRoom\RoomTemplate\Name <> "gate_a_intro" And PlayerRoom\RoomTemplate\Name <> "area_076" And PlayerRoom\RoomTemplate\Name <> "facility_reactor" Then
					If Rand(1500) = 1 Then
						If NTF_AmbienceSFX=0
							For i = 0 To 5
								If AmbientSFX[i * 15 + CurrAmbientSFX]<>0 Then
									If ChannelPlaying(AmbientSFXCHN)=0 Then FreeSound_Strict AmbientSFX[i * 15 + CurrAmbientSFX] : AmbientSFX[i * 15 + CurrAmbientSFX] = 0
								EndIf			
							Next
						EndIf
						If ChannelPlaying(NTF_AmbienceCHN)=0 Then FreeSound_Strict NTF_AmbienceSFX : NTF_AmbienceSFX = 0
						
						;PositionEntity (SoundEmitter, EntityX(Camera) + Rnd(-1.0, 1.0), 0.0, EntityZ(Camera) + Rnd(-1.0, 1.0))
						PositionEntity (SoundEmitter, EntityX(Camera) + Rnd(-1.0, 1.0), EntityY(Camera) + Rnd(-1.0, 1.0), EntityZ(Camera) + Rnd(-1.0, 1.0))
						
						;If Rand(3)=1 Then PlayerZone = 3
						;If Rand(3)=1 Then PlayerZone = 6
						
						PlayerZone = gopt\CurrZone-1
						
						Select Rand(0,3)
							Case 0
								PlayerZone = 3
							Case 1
								PlayerZone = 6
							Case 2
								PlayerZone = 7
							Default
								PlayerZone = gopt\CurrZone-1
						End Select
						
						If PlayerRoom\RoomTemplate\Name = "gate_a_intro" Then 
							PlayerZone = 4
						ElseIf PlayerRoom\RoomTemplate\Name = "cont_860"
							For e.Events = Each Events
								If e\EventName = "cont_860"
									If e\EventState[0] = 1.0
										PlayerZone = 5
										PositionEntity (SoundEmitter, EntityX(SoundEmitter), 30.0, EntityZ(SoundEmitter))
									EndIf
									
									Exit
								EndIf
							Next
						ElseIf PlayerRoom\RoomTemplate\Name = "room2_maintenance"
							If EntityY(Collider)<-3500.0*RoomScale
								PlayerZone = 8
							EndIf
						EndIf
						
						If PlayerZone < 6
							CurrAmbientSFX = Rand(0,AmbientSFXAmount[PlayerZone]-1)
						ElseIf PlayerZone=6
							CurrAmbientSFX = Rand(0,NTF_MaxAmbienceSFX-1)
						ElseIf PlayerZone = 7
							Select gopt\CurrZone
								Case 1
									CurrAmbientSFX = Rand(0,NTF_MaxLCZAmbience-1)
								Case 2
									CurrAmbientSFX = Rand(0,NTF_MaxHCZAmbience-1)
								Case 3
									CurrAmbientSFX = Rand(0,NTF_MaxEZAmbience-1)
							End Select
						Else
							CurrAmbientSFX = Rand(0,NTF_MaxSewerAmbienceSFX-1)
						EndIf
						
						Select PlayerZone
							Case 0,1,2
								If AmbientSFX[PlayerZone * 15 + CurrAmbientSFX]=0 Then AmbientSFX[PlayerZone * 15 + CurrAmbientSFX]=LoadSound_Strict("SFX\Ambient\Zone"+(PlayerZone+1)+"\ambient"+(CurrAmbientSFX+1)+".ogg")
							Case 3
								If AmbientSFX[PlayerZone * 15 + CurrAmbientSFX]=0 Then AmbientSFX[PlayerZone * 15 + CurrAmbientSFX]=LoadSound_Strict("SFX\Ambient\General\ambient"+(CurrAmbientSFX+1)+".ogg")
							Case 4
								If AmbientSFX[PlayerZone * 15 + CurrAmbientSFX]=0 Then AmbientSFX[PlayerZone * 15 + CurrAmbientSFX]=LoadSound_Strict("SFX\Ambient\Pre-breach\ambient"+(CurrAmbientSFX+1)+".ogg")
							Case 5
								If AmbientSFX[PlayerZone * 15 + CurrAmbientSFX]=0 Then AmbientSFX[PlayerZone * 15 + CurrAmbientSFX]=LoadSound_Strict("SFX\Ambient\Forest\ambient"+(CurrAmbientSFX+1)+".ogg")
							Case 6
								If NTF_AmbienceSFX=0 Then NTF_AmbienceSFX=LoadSound_Strict("SFX\Ambience\"+NTF_AmbienceStrings[CurrAmbientSFX]+".ogg")
							Case 7
								Select gopt\CurrZone
									Case 1
										If NTF_AmbienceSFX=0 Then NTF_AmbienceSFX=LoadSound_Strict("SFX\Ambience\LCZ\"+NTF_LCZAmbienceStrings[CurrAmbientSFX]+".ogg")
									Case 2
										If NTF_AmbienceSFX=0 Then NTF_AmbienceSFX=LoadSound_Strict("SFX\Ambience\HCZ\"+NTF_HCZAmbienceStrings[CurrAmbientSFX]+".ogg")
									Case 3
										If NTF_AmbienceSFX=0 Then NTF_AmbienceSFX=LoadSound_Strict("SFX\Ambience\EZ\"+NTF_EZAmbienceStrings[CurrAmbientSFX]+".ogg")
								End Select
							Case 8
								If NTF_AmbienceSFX=0 Then NTF_AmbienceSFX=LoadSound_Strict("SFX\Ambience\rooms\sewers\"+NTF_SewerAmbienceStrings[CurrAmbientSFX]+".ogg")
						End Select
						
						If PlayerZone < 6
							AmbientSFXCHN = PlaySound2(AmbientSFX[PlayerZone * 15 + CurrAmbientSFX], Camera, SoundEmitter)
						Else
							NTF_AmbienceCHN = PlaySound2(NTF_AmbienceSFX, Camera, SoundEmitter)
						EndIf
						UpdateSoundOrigin(AmbientSFXCHN,Camera, SoundEmitter)
						UpdateSoundOrigin(NTF_AmbienceCHN,Camera, SoundEmitter)
					EndIf
					
					If Rand(50000) = 3 Then
						Local RN$ = PlayerRoom\RoomTemplate\Name$
						If RN$ <> "cont_860" And RN$ <> "room1_intro" And RN$ <> "gate_a_intro" And RN$ <> "class_d_cells" And RN$ <> "facility_reactor" And RN$ <> "area_076" Then
							If FPSfactor > 0 Then LightBlink = Rnd(1.0,2.0)
							PlaySound_Strict  LoadTempSound("SFX\SCP\079\Broadcast"+Rand(1,7)+".ogg")
						EndIf 
					EndIf
				ElseIf PlayerRoom\RoomTemplate\Name = "gate_a_intro" Then
					If Rand(1500) = 1 Then
						If ChannelPlaying(NTF_AmbienceCHN)=0 Then FreeSound_Strict NTF_AmbienceSFX : NTF_AmbienceSFX = 0
						
						PositionEntity (SoundEmitter, EntityX(Camera) + Rnd(-1.0, 1.0), 0.0, EntityZ(Camera) + Rnd(-1.0, 1.0))
						
						CurrAmbientSFX = Rand(0,NTF_MaxIntroAmbienceSFX-1)
						
						If NTF_AmbienceSFX=0 Then NTF_AmbienceSFX=LoadSound_Strict("SFX\Ambience\Intro\"+NTF_IntroAmbienceStrings[CurrAmbientSFX]+".ogg")
						
						NTF_AmbienceCHN = PlaySound2(NTF_AmbienceSFX, Camera, SoundEmitter)
					EndIf
					UpdateSoundOrigin(NTF_AmbienceCHN,Camera, SoundEmitter)
				EndIf
			EndIf
		EndIf
		
		If (Not MenuOpen) And (Not InvOpen) And (OtherOpen=Null) And (d_I\SelectedDoor = Null) And (ConsoleOpen = False) And (Using294 = False) And (SelectedScreen = Null) And EndingTimer=>0 And (Not mi_I\IsEnding) Then
			LightVolume = CurveValue(TempLightVolume, LightVolume, 50.0)
			CameraFogRange(Camera, CameraFogNear*LightVolume,CameraFogFar*LightVolume)
			CameraFogMode Camera,1
			CameraRange(Camera, 0.01, Min(CameraFogFar*LightVolume*1.5,28))
			For r.Rooms = Each Rooms
				For i = 0 To r\MaxLights%
					If r\Lights%[i]<>0
						EntityAutoFade r\LightSprites%[i],CameraFogNear*LightVolume,CameraFogFar*LightVolume
					EndIf
				Next
			Next
			
			Local g.Guns
			
			AmbientLight Brightness, Brightness, Brightness	
			PlayerSoundVolume = CurveValue(0.0, PlayerSoundVolume, 5.0)
			
			CanSave% = True
			If psp\Health = 0 And KillTimer >= 0 Then
				Kill()
			EndIf
			For g = Each Guns
				If opt\RenderScope Then
					If IsSPPlayerAlive() And g_I\HoldingGun = g\ID And g\HasAttachments[ATT_ACOG_SCOPE] Then
						UpdateScope()
					EndIf
				EndIf
			Next
			UpdateDeafPlayer()
			UpdateEmitters()
			UpdateNewElevators()
			If mpl\HasNTFGasmask Then
				ShowEntity GasMaskOverlay2
			Else
				HideEntity GasMaskOverlay2
			EndIf
			MouseLook()
			MovePlayer()
			UpdateNightVision()
			InFacility = CheckForPlayerInFacility()
			UpdateDoors()
			UpdateScreens()
			Update109()
			Update127()
			Update198()
			Update207()
			Update268()
			Update294()
			;Update357()
			;Update402()
			If QuickLoadPercent = -1 Lor QuickLoadPercent = 100
				UpdateEvents()
			EndIf
			UpdateRoomLights(Camera)
			UpdateFluLights()
			UpdateFuseBoxes()
			If ShouldUpdateWater<>"" Then
				UpdateWater(ShouldUpdateWater)
			EndIf
			UpdatePlayerModel()
			UpdateGrenades()
			If KillTimer >= 0 Then
				If CanPlayerUseGuns% Then
					UpdateGuns()
					AnimateGuns()
					If (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
						UpdateIronSight()
					EndIf
					If AttachmentMenuOpen Then
						UpdateAttachments()
					EndIf
				EndIf
				UpdateSPPlayer()
				If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" Then
					UpdateHazardousDefenceSuit()
				EndIf
			Else
				If ChannelPlaying(ChatSFXCHN) Then StopChannel(ChatSFXCHN)
				HideEntity g_I\GunPivot
			EndIf
			UpdateDecals()
			UpdateMTFDialogue()
			UpdateIntercomSystem()	
			UpdateOmegaWarhead()
			UpdateMTF()
			UpdateCI()
			UpdateEventValues()
			UpdateNPCs()
			UpdateItems()
			UpdateParticles()
			UpdateDust()
			UpdateDamageOverlay()
			UpdateItemInSlotProperties()
			If I_427\Using Then
				Use427()
			Else
				If I_427\Timer >= 70*360 Then
					If Rnd(200)<2.0 Then
						Local pvt = CreatePivot()
						PositionEntity pvt, EntityX(Collider)+Rnd(-0.05,0.05),EntityY(Collider)-0.05,EntityZ(Collider)+Rnd(-0.05,0.05)
						TurnEntity pvt, 90, 0, 0
						EntityPick(pvt,0.3)
						Local de.Decals = CreateDecal(DECAL_FOAM, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
						de\Size = Rnd(0.03,0.08)*2.0 : EntityAlpha(de\obj, 1.0) : ScaleSprite de\obj, de\Size, de\Size
						Local tempchn% = PlaySound_Strict (DripSFX[Rand(0,3)])
						ChannelVolume tempchn, Rnd(0.0,0.8)*(opt\SFXVolume*opt\MasterVol)
						ChannelPitch tempchn, Rand(20000,30000)
						pvt = FreeEntity_Strict(pvt)
						BlurTimer = 800
					EndIf
				EndIf
				If I_427\Timer >= 70*390 Then
					Crouch = True
				EndIf
			EndIf
		Else
			KeyHitUse = False
			KeyDownUse = False
		EndIf
		
		Local CurrFogColor$ = ""
		If PlayerRoom <> Null Then
			If PlayerRoom\RoomTemplate\Name = "room2_maintenance" And EntityY(Collider)<-3500.0*RoomScale Then
				CurrFogColor = FogColor_Sewers
			ElseIf PlayerRoom\RoomTemplate\Name = "gate_a_intro" Lor PlayerRoom\RoomTemplate\Name = "gate_a_topside" Lor PlayerRoom\RoomTemplate\Name = "gate_b_topside" Lor PlayerRoom\RoomTemplate\Name = "gate_d_topside"Then
				If e <> Null Then CurrFogColor = FogColor_Outside
			ElseIf PlayerRoom\RoomTemplate\Name = "gate_c_topside"
				If e <> Null Then CurrFogColor = FogColor_Outside_Night
			ElseIf PlayerRoom\RoomTemplate\Name = "pocketdimension" Then
				CurrFogColor = FogColor_PD
			EndIf
		EndIf
		If CurrFogColor = "" Then
			Select gopt\CurrZone
				Case LCZ, CLASSD_CELLS
					CurrFogColor = FogColor_LCZ
				Case HCZ
					CurrFogColor = FogColor_HCZ
				Case EZ
					CurrFogColor = FogColor_EZ
				Case RCZ
					CurrFogColor = FogColor_RCZ
				Case BCZ
					CurrFogColor = FogColor_BCZ
				Case AREA_076
					CurrFogColor = FogColor_Area_076
				Case AREA_106_ESCAPE
					CurrFogColor = FogColor_Area_106_Escape
			End Select
		EndIf
		Local FogColorR% = Left(CurrFogColor,3)
		Local FogColorG% = Mid(CurrFogColor,4,3)
		Local FogColorB% = Right(CurrFogColor,3)
		CameraFogColor Camera,FogColorR,FogColorG,FogColorB
		CameraClsColor Camera,FogColorR,FogColorG,FogColorB
		
		If InfiniteStamina% Then Stamina = Min(100, Stamina + (100.0-Stamina)*0.01*FPSfactor)
		
		If gopt\GameMode = GAMEMODE_UNKNOWN Then
			UpdateMissionEvents()
			UpdateMissionEnding()
		EndIf
		If FPSfactor = 0 Then
			UpdateWorld(0)
		Else
			UpdateWorld()
			ManipulateNPCBones()
			If NTF_SmallHead Then UpdateSmallHeadMode()
		EndIf
		
		If MTF_CameraCheckTimer>0.0 And MTF_CameraCheckTimer<70*90
			MTF_CameraCheckTimer=MTF_CameraCheckTimer+FPSfactor
		ElseIf MTF_CameraCheckTimer>=70*90
			MTF_CameraCheckTimer=0.0
		EndIf
		
		;[Block]
		If (Not MenuOpen)  Then
			BlurVolume = Min(CurveValue(0.0, BlurVolume, 20.0),0.95)
			If BlurTimer > 0.0 Then
				BlurVolume = Max(Min(0.95, BlurTimer / 1000.0), BlurVolume)
				BlurTimer = Max(BlurTimer - FPSfactor, 0.0)
			End If
			
			Local darkA# = 0.0
			
			If Sanity < 0 Then
				If RestoreSanity Then Sanity = Min(Sanity + FPSfactor, 0.0)
				If Sanity < (-200) Then 
					darkA = Max(Min((-Sanity - 200) / 700.0, 0.6), darkA)
					If KillTimer => 0 Then 
						HeartBeatVolume = Min(Abs(Sanity+200)/500.0,1.0)
						HeartBeatRate = Max(70 + Abs(Sanity+200)/6.0,HeartBeatRate)
					EndIf
				EndIf
			End If
			
			If EyeStuck > 0 Then 
				BlinkTimer = BLINKFREQ
				EyeStuck = Max(EyeStuck-FPSfactor,0)
				
				If EyeStuck < 9000 Then BlurTimer = Max(BlurTimer, (9000-EyeStuck)*0.5)
				If EyeStuck < 6000 Then darkA = Min(Max(darkA, (6000-EyeStuck)/5000.0),1.0)
				If EyeStuck < 9000 And EyeStuck+FPSfactor =>9000 Then 
					CreateMsg("The eyedrops are causing your eyes to tear up.")
				EndIf
			EndIf
			
			If BlinkEffectTimer > 0 Then
				BlinkEffectTimer = BlinkEffectTimer - (FPSfactor/70)
			Else
				If BlinkEffect <> 1.0 Then BlinkEffect = 1.0
			EndIf
			
			; ~ Blinking Logic
			;[Block]
			
;			If (Not (EntityVisible(Camera, Curr173\Collider)) Lor IsCutscene) Then
;				If BlinkTimer < BLINKFREQ And BlinkTimer > -3 Then BlinkTimer = BlinkTimer + FPSfactor * 0.6 * BlinkEffect
;			EndIf
			
			If BlinkTimer < 0 Then
				If BlinkTimer > - 5 Then
					darkA = Max(darkA, Sin(Abs(BlinkTimer * 18.0)))
				ElseIf BlinkTimer > - 15
					darkA = 1.0
				Else
					darkA = Max(darkA, Abs(Sin(BlinkTimer * 18.0)))
				EndIf
				
				If BlinkTimer <= - 20 Then
					;Randomizes the frequency of blinking. Scales with difficulty.
					Select SelectedDifficulty\OtherFactors
						Case EASY
							BLINKFREQ = Rnd(490,700)
						Case NORMAL
							BLINKFREQ = Rnd(455,665)
						Case HARD
							BLINKFREQ = Rnd(420,630)
						Case HARDER
							BLINKFREQ = Rnd(385,595)
						Case IMPOSSIBLE
							BLINKFREQ = Rnd(350,555)
					End Select 
					BlinkTimer = BLINKFREQ
				EndIf
				
				BlinkTimer = BlinkTimer - FPSfactor
			Else
				BlinkTimer = BlinkTimer - FPSfactor * 0.6 * BlinkEffect
				If EyeIrritation > 0 Then BlinkTimer=BlinkTimer-Min(EyeIrritation / 100.0 + 1.0, 5.0) * FPSfactor
				
				darkA = Max(darkA, 0.0)
			EndIf
			;[End Block]
			
			LightBlink = Max(LightBlink - (FPSfactor / 35.0), 0)
			If LightBlink > 0 Then darkA = Min(Max(darkA, LightBlink * Rnd(0.3, 0.8)), 1.0)
			
			If Using294 Then darkA=1.0
			
			If (Not mpl\NightVisionEnabled) Lor (Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 3) <> "nvg") Then
				darkA = Max((1.0-SecondaryLightOn)*0.9, darkA)
			EndIf
			
			If KillTimer < 0 Then
				InvOpen = False
				SelectedItem = Null
				SelectedScreen = Null
				SelectedMonitor = Null
				BlurTimer = Abs(KillTimer*5)
				KillTimer=KillTimer-(FPSfactor*0.8)
				If KillTimer < - 360 And (gopt\GameMode <> GAMEMODE_UNKNOWN) Then 
					MenuOpen = True
				EndIf
				darkA = Max(darkA, Min(Abs(KillTimer / 400.0), 1.0))
			EndIf
			
			If SelectedEnding <> "" Then
				EndingTimer = EndingTimer - (FPSfactor*0.8)
			EndIf
			
			If FallTimer < 0 Then
				InvOpen = False
				SelectedItem = Null
				SelectedScreen = Null
				SelectedMonitor = Null
				BlurTimer = Abs(FallTimer*10)
				FallTimer = FallTimer-FPSfactor
				darkA = Max(darkA, Min(Abs(FallTimer / 400.0), 1.0))				
			EndIf
			
			If LightFlash > 0 Then
				ShowEntity Light
				EntityAlpha(Light, Max(Min(LightFlash + Rnd(-0.2, 0.2), 1.0), 0.0))
				LightFlash = Max(LightFlash - (FPSfactor / 70.0), 0)
			Else
				HideEntity Light
				;EntityAlpha(Light, LightFlash)
			EndIf
			
			If SelectedScreen <> Null Then darkA = Max(darkA, 0.5)
			
			If darkA <> 0.0 Then
				ShowEntity(Dark)
				EntityAlpha(Dark, darkA)
			Else
				HideEntity(Dark)
			EndIf	
		EndIf
		;[End block]
		
		;[CONTROLLER]
		If InteractHit(KEY_INV,CK_Inv) And VomitTimer >= 0 And (Not MenuOpen) Then 
			If InvOpen Then
				ResumeSounds()
				MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
			Else
				PauseSounds()
			EndIf
			InvOpen = Not InvOpen
			If OtherOpen<>Null Then OtherOpen=Null
			SelectedItem = Null 
		EndIf
		
		UpdateWorld2()
		
		UpdateGUI()
		
		If gopt\GameMode <> GAMEMODE_UNKNOWN Then
			If InteractHit(KEY_SAVE,CK_Save); Then CreateHintMsg("Sorry! Quick saving is disabled.")
			If SelectedDifficulty\SaveType = SAVEANYWHERE Then
				RN$ = PlayerRoom\RoomTemplate\Name$
				If (Not CanSave) Lor QuickLoadPercent > -1
					CreateHintMsg("You cannot save at this moment.")
					If QuickLoadPercent > -1
						m_msg\HintTxt = m_msg\HintTxt + " (game is loading)"
					EndIf
				Else
					SaveGame(SavePath + CurrSave\Name + "\")
				EndIf
			ElseIf SelectedDifficulty\SaveType = SAVEONSCREENS
				If SelectedScreen=Null And SelectedMonitor=Null Then
					CreateHintMsg("You cannot save in this location.")
				Else
					RN$ = PlayerRoom\RoomTemplate\Name$
					If (Not CanSave) Lor QuickLoadPercent > -1
						CreateHintMsg("You cannot save at this moment.")
						If QuickLoadPercent > -1
							m_msg\HintTxt = m_msg\HintTxt + " (game is loading)"
						EndIf
					Else
						If SelectedScreen<>Null
							GameSaved = False
							Playable = True
							DropSpeed = 0
						EndIf
						SaveGame(SavePath + CurrSave\Name + "\")
					EndIf
				EndIf
			Else
				CreateHintMsg("Quick saving is disabled.")
			EndIf
		Else If SelectedDifficulty\SaveType = SAVEONSCREENS And (SelectedScreen<>Null Lor SelectedMonitor<>Null)
			If (m_msg\HintTxt<>"Game progress saved." And m_msg\HintTxt<>"You cannot save in this location." And m_msg\HintTxt<>"You cannot save at this moment.") Lor m_msg\HintTimer<=0
				CreateHintMsg("Press "+KeyName[KEY_SAVE]+" to save.")
			EndIf
			
			If MouseHit2 Then SelectedMonitor = Null
		EndIf
			
			If KeyHit(KEY_ATTACHMENTS) And (Not MenuOpen) And (Not ConsoleOpen) And (OtherOpen = Null) Then
				For g = Each Guns
					If g_I\HoldingGun = g\ID And g\CanSelectMenuAttachments Then
						AttachmentMenuOpen = (Not AttachmentMenuOpen)
						FlushKeys()
					EndIf
				Next
			EndIf
			
			If KeyHit(KEY_LOAD); Then CreateHintMsg("Sorry! Quick loading is disabled.")
				If GameSaved And (Not SelectedDifficulty\PermaDeath) Then
					
					DeleteMenuGadgets()
					MenuOpen = False
					LoadGameQuick(SavePath + CurrSave\Name + "\")
					
					MoveMouse Viewport_Center_X,Viewport_Center_Y
					HidePointer()
					
					KillSounds()
					
					Playable=True
					
					UpdateRooms()
					UpdateDoors()
					
					For r.Rooms = Each Rooms
						Local x = Abs(EntityX(Collider) - EntityX(r\obj))
						Local z = Abs(EntityZ(Collider) - EntityZ(r\obj))
						
						If x < 12.0 And z < 12.0 Then
							MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)] = Max(MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)], 1)
							If x < 4.0 And z < 4.0 Then
								If Abs(EntityY(Collider) - EntityY(r\obj)) < 1.5 Then PlayerRoom = r
								MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)] = 1
							EndIf
						EndIf
					Next
					
					PlaySound_Strict LoadTempSound(("SFX\Horror\Horror8.ogg"))
					
					DropSpeed=0
					
					UpdateWorld 0.0
					
					PrevTime = MilliSecs()
					FPSfactor = 0
					
					ResetInput()
					
					ResumeSounds()
					Return
				EndIf
			EndIf
			
			If KeyHit(KEY_CONSOLE) Then
				If opt\ConsoleEnabled
					If ConsoleOpen Then
						UsedConsole = True
						ResumeSounds()
						MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
					Else
						PauseSounds()
					EndIf
					ConsoleOpen = (Not ConsoleOpen)
					FlushKeys()
				EndIf
			EndIf
		EndIf
		
		If EndingTimer < 0
			If SelectedEnding <> "" Then UpdateEnding()
		Else
			If mi_I\EndingTimer=0.0 Then UpdateMenu()
		EndIf
		If mi_I\EndingTimer > 0.0
			InvOpen = False
			AttachmentMenuOpen = False
			SelectedItem = Null
			SelectedScreen = Null
			SelectedMonitor = Null
			MenuOpen = False
			ShouldPlay = MUS_NULL
		EndIf
		
		If m_msg\Timer > 0
			m_msg\Timer = m_msg\Timer - FPSfactor
		EndIf
		
		UpdateAchievementMsg()
		UpdateHintMsg()
		UpdateAutoSaveMsg()
		UpdateSubtitles()
	Wend
	
	;Go out of function immediately if the game has been quit
	If MainMenuOpen Then Return
	
	If ShouldUpdateWater<>"" Then
		RenderWater(ShouldUpdateWater)
	EndIf
	
	RenderMainLoop()
	
	UpdateMsg()
	
	Color 255, 255, 255
	SetFont fo\ConsoleFont
	If opt\ShowFPS Then
		Text 20, 20, "FPS: " + ft\FPS : SetFont fo\Font[Font_Default]
	EndIf
	
End Function

Function RenderMainLoop%()
	Local g.Guns
	
	If FPSfactor > 0 Then RenderSecurityCams()
	
	For g = Each Guns
		If opt\RenderScope Then
			If IsSPPlayerAlive() And g_I\HoldingGun = g\ID And g\HasAttachments[ATT_ACOG_SCOPE]; And ReadyToShowDot Then
				RenderScope()
			ElseIf IsSPPlayerAlive() And g\ID = GUN_EMRP And g_I\HoldingGun = g\ID Then
				RenderScope()
			EndIf
		EndIf
	Next
	
	If ShouldUpdateWater<>"" Then
		RenderWater(ShouldUpdateWater)
	EndIf
	
	RenderWorld2(Max(0.0,1.0+(ft\Accumulator/ft\TickDuration)))
	
	UpdateBlur(BlurVolume)
	
	DrawGUI()
	
	If EndingTimer < 0 Then
		If SelectedEnding <> "" Then DrawEnding()
	Else
		If mi_I\EndingTimer=0.0 Then DrawMenu()
	EndIf
	
	DrawMissionEnding()
	
	UpdateConsole(1)
	
	DrawQuickLoading()
	
	RenderAchievementMsg()
	
	DrawHintMSG()
	DrawAutoSaveMSG()
	RenderSubtitles()
	
End Function

;----------------------------------------------------------------------------------------------------------------------------------------------------
;----------------------------------------------------------------------------------------------------------------------------------------------------
;----------------------------------------------------------------------------------------------------------------------------------------------------

Function ReplaceTextureByMaterial(Texture$,Obj%,TexAndMatname$,LetterNumber)
	CatchErrors("ReplaceTexturebyMaterial")
	
	Local temp#
	Local i%, j%, sf%, b%, t1%, name$, tex%
	
	tex = LoadTexture_Strict(Texture)
	
	For i = 1 To CountSurfaces(Obj)
		sf = GetSurface(Obj,i)
		b = GetSurfaceBrush(sf)
		If b<>0 Then
			For j = 0 To 7
				t1 = GetBrushTexture(b,j)
				If t1<>0 Then
					name$ = StripPath(TextureName(t1))
					If Left(Lower(name),LetterNumber) = TexAndMatname$ Then
						BrushTexture b, tex, 0, j
						PaintSurface sf,b
						DeleteSingleTextureEntryFromCache t1
						Exit
					EndIf
					If name<>"" Then DeleteSingleTextureEntryFromCache t1
				EndIf
			Next
			FreeBrush b
		EndIf
	Next
	
	DeleteSingleTextureEntryFromCache tex
	
	CatchErrors("Uncaught (ReplaceTexturebyMaterial)")
End Function

Function StartChapter(CptName$="", Chapter%)
	CatchErrors("StartChapters")
	
	If HUDenabled And psp\IsShowingHUD Then
		CreateSplashText(GetLocalString("Singleplayer",CptName$),opt\GraphicWidth/2,opt\GraphicHeight/2,100,5,Font_Default_Large,True,False)
		If (Not ChannelPlaying(cpt\SoundCHN[0])) Then cpt\SoundCHN[0] = PlaySound_Strict(cpt\Sound[0])
	EndIf
	
	If gopt\GameMode = GAMEMODE_DEFAULT Then
		cpt\Current = Chapter
		If Chapter > 1 Then cpt\Unlocked = Chapter
	ElseIf gopt\GameMode = GAMEMODE_NTF Then 
		cpt\NTFCurrent = Chapter
		If Chapter > 1 Then cpt\NTFUnlocked = Chapter
	Else
		cpt\DCurrent = Chapter
		If Chapter > 1 Then cpt\DUnlocked = Chapter
	EndIf
	
	CatchErrors("Uncaught : StartChapters")
End Function

Function Kill()
	If GodMode Then Return
	
	psp\Health = 0
	
	If BreathCHN <> 0 Then
		If ChannelPlaying(BreathCHN) Then StopChannel(BreathCHN)
	EndIf
	
	If KillTimer >= 0 Then
		KillAnim = Rand(0,1)
		PlaySound_Strict(DamageSFX[0])
		If SelectedDifficulty\PermaDeath Then
			DeleteGame(CurrSave)
			LoadSaveGames()
		EndIf
		
		KillTimer = Min(-1, KillTimer)
		ShowEntity Head
		PositionEntity(Head, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True), True)
		ResetEntity (Head)
		RotateEntity(Head, 0, EntityYaw(Camera), 0)		
	EndIf
End Function

Function DrawEnding()
	Local i
	
	ShowPointer()
	
	Local x,y,width,height, temp
	Local itt.ItemTemplates, r.Rooms
	
	ClsColor 0,0,0
	
	Cls
	
	If EndingTimer<-200 Then
		If EndingTimer > -700 Then
			If Rand(1,150)<Min((Abs(EndingTimer)-200),155) Then
				DrawImage EndingScreen, opt\GraphicWidth/2-400, opt\GraphicHeight/2-400
			Else
				Color 0,0,0
				Rect 100,100,opt\GraphicWidth-200,opt\GraphicHeight-200
				Color 255,255,255
			EndIf
			
		Else
			If EndingTimer > -2000 Then
				DrawImage EndingScreen, opt\GraphicWidth/2-400, opt\GraphicHeight/2-400
			EndIf
			
			If EndingTimer < -1000 And EndingTimer > -2000 Then
				width = ImageWidth(PauseMenuIMG[1])
				height = ImageHeight(PauseMenuIMG[1])
				x = opt\GraphicWidth / 2 - width / 2
				y = opt\GraphicHeight / 2 - height / 2
				
				DrawImage PauseMenuIMG[1], x, y
				
				Color(255, 255, 255)
				
				SetFont fo\Font[Font_Menu]
				
				Text(x + width / 2, y + 20*MenuScale, GetLocalString("Menu","the_end"), True)
				SetFont fo\Font[Font_Default]
				
				If AchievementsMenu=0 Then 
					x = x+42*MenuScale
					y = y+122*MenuScale
					
					Local roomamount = 0, roomsfound = 0
					For r.Rooms = Each Rooms
						roomamount = roomamount + 1
						roomsfound = roomsfound + r\found
					Next
					
					Local achievementsUnlocked =0
					For i = 0 To MAXACHIEVEMENTS-1
						achievementsUnlocked = achievementsUnlocked + achv\Achievement[i]
					Next
					
					Local secretsfound = 0
					
					If IsVaneCoinDropped Then secretsfound = secretsfound + 1
					
					Text x+100*MenuScale, y+20*MenuScale, GetLocalString("Menu","achievements_unlocked") + achievementsUnlocked+"/"+MAXACHIEVEMENTS
					Text x+100*MenuScale, y+50*MenuScale, GetLocalString("Menu","rooms_found") + roomsfound
					Text x+100*MenuScale, y+80*MenuScale, GetLocalString("Menu","secrets_found") + secretsfound
					
					x = opt\GraphicWidth / 2 - width / 2
					y = opt\GraphicHeight / 2 - height / 2
					x = x+width/2
					y = y+height-100*MenuScale
					
				Else
					DrawMenu()
				EndIf
				
			; ~ Credits
				
			ElseIf EndingTimer<=-2000 Then
				DrawCredits()
			EndIf
			
		EndIf
		
	EndIf
	
	SetFont fo\Font[Font_Default]
	
	DrawAllMenuButtons()
	
End Function

Function UpdateEnding()
	Local i
	
	FPSfactor = 0
	If EndingTimer>-2000
		EndingTimer=Max(EndingTimer-FPSfactor2,-1111)
	Else
		EndingTimer=EndingTimer-FPSfactor2*(opt\GraphicHeight/800.0)
	EndIf
	
	GiveAchievement(Achv055)
	If (Not UsedConsole) Then GiveAchievement(AchvConsole)
	If SelectedDifficulty\Name = "Keter" Then GiveAchievement(AchvKeter)
	If SelectedDifficulty\Name = "Thaumiel" Then 
		GiveAchievement(AchvThaumiel)
		GiveAchievement(AchvKeter)
	EndIf
	If SelectedDifficulty\Name = "Appolyon" Then
		GiveAchievement(AchvAppolyon)
		GiveAchievement(AchvThaumiel)
		GiveAchievement(AchvKeter)
	EndIf
	Local x,y,width,height, temp
	Local itt.ItemTemplates, r.Rooms
	
	ShouldPlay = MUS_NULL
	
	If EndingTimer<-200 Then
		
		If BreathCHN <> 0 Then
			If ChannelPlaying(BreathCHN) Then StopChannel BreathCHN : Stamina = 100
		EndIf
		If EndingScreen = 0 Then
			EndingScreen = LoadImage_Strict("GFX\ending_screen.png")
			PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Bells_far.ogg"))
			PlaySound_Strict LightSFX
		EndIf
		
		If EndingTimer > -700 Then
;			If EndingTimer+fps\Factor[1] > -450 And EndingTimer <= -450 Then
;				Select Lower(SelectedEnding)
;					Case "a1", "a2"
;						PlaySound_Strict LoadTempSound("SFX\Ending\GateA\Ending"+SelectedEnding+".ogg")
;					Case "b1", "b2", "b3"
;						PlaySound_Strict LoadTempSound("SFX\Ending\GateB\Ending"+SelectedEnding+".ogg")
;					Case "c1", "c2"
;						PlaySound_Strict LoadTempSound("SFX\Ending\GateC\Ending"+SelectedEnding+".ogg")
;					Case "d1"
;						PlaySound_Strict LoadTempSound("SFX\Ending\GateD\Ending"+SelectedEnding+".ogg")
;				End Select
;			EndIf			
		Else
			
			If EndingTimer < -1000 And EndingTimer > -2000
				
				width = ImageWidth(PauseMenuIMG[1])
				height = ImageHeight(PauseMenuIMG[1])
				x = opt\GraphicWidth / 2 - width / 2
				y = opt\GraphicHeight / 2 - height / 2
				
				If AchievementsMenu=0 Then 
					x = x+132*MenuScale
					y = y+122*MenuScale
					
					x = opt\GraphicWidth / 2 - width / 2
					y = opt\GraphicHeight / 2 - height / 2
					x = x+width/2
					y = y+height-100*MenuScale
					
					Local achievementsUnlocked =0
					For i = 0 To MAXACHIEVEMENTS-1
						achievementsUnlocked = achievementsUnlocked + achv\Achievement[i]
					Next
					
					If DrawButton(x-175*MenuScale,y-150*MenuScale,390*MenuScale,60*MenuScale,GetLocalString("Menu","main_menu"), True)
						LoadCredits()
					EndIf
				Else
					UpdateMenu()
				EndIf
			ElseIf EndingTimer<=-2000
				UpdateCredits()
			EndIf
		EndIf
	EndIf
	
End Function

Type CreditsLine
	Field txt$
	Field id%
	Field stay%
End Type

Const MaxCreditScreens% = 3

Global CreditsTimer# = 0.0
Global CreditsScreen[MaxCreditScreens]
Global CreditsScreenCurrent% = 0
Global CreditsScreenNext% = 0

Function LoadCredits()
	Local i%
	
	If SelectedEnding = "scp-035" Then
		ShouldPlay = MUS_TURN_AROUND
	ElseIf SelectedEnding = "scp-2935" Then
		ShouldPlay = MUS_SCP_2935
	ElseIf SelectedEnding = "c1" Then
		ShouldPlay = MUS_GOOD_KARMA
	ElseIf SelectedEnding = "d1" Then
		ShouldPlay = MUS_CREDITS;TODO
	Else
		ShouldPlay = MUS_CREDITS
	EndIf
	
	NowPlaying = ShouldPlay
	For i = 0 To 9
		If TempSounds[i] <> 0 Then FreeSound_Strict TempSounds[i] : TempSounds[i]=0
	Next
	StopStream_Strict(MusicCHN)
	MusicCHN = StreamSound_Strict("SFX\Music\"+Music[NowPlaying]+".ogg",0.0,Mode)
	SetStreamVolume_Strict(MusicCHN,1.0*aud\MusicVol)
	FlushKeys()
	EndingTimer=-2000
	
	DeleteMenuGadgets()
	InitCredits()
	
End Function

Function InitCredits()
	Local cl.CreditsLine, d.Doors
	Local file%
	Local l$, i%, scr
	
	file% = OpenFile(I_Loc\LangPath+"Credits.txt")
	
	CreditsFont% = LoadFont_Strict("GFX\font\Courier New.ttf", Int(21 * (opt\GraphicHeight / 1024.0)))
	CreditsFont2% = LoadFont_Strict("GFX\font\Courier New BD.ttf", Int(35 * (opt\GraphicHeight / 1024.0)))
	
	CreditsScreen[0] = LoadRMesh("GFX\map\rooms\room2_tunnel_1\tunnel_opt.rmesh", Null)
	CreditsScreen[1] = LoadRMesh("GFX\map\rooms\room2_tunnel_2\tunnel2_opt.rmesh", Null)
	CreditsScreen[2] = LoadRMesh("GFX\map\rooms\room2_tunnel_3\tunnel3.rmesh", Null)
	For i = 0 To (MaxCreditScreens-1)
		ScaleEntity CreditsScreen[i], RoomScale, RoomScale, RoomScale
		PositionEntity CreditsScreen[i], 0, 100 - 0.75, 2048.0 * RoomScale
		HideEntity CreditsScreen[i]
	Next
	
	CreditsScreenCurrent = Rand(0, (MaxCreditScreens-1))
	CreditsScreenNext = CreditsScreenCurrent
	While CreditsScreenCurrent = CreditsScreenNext
		CreditsScreenNext = Rand(0, (MaxCreditScreens-1))
	Wend
	ShowEntity CreditsScreen[CreditsScreenCurrent]
	ShowEntity CreditsScreen[CreditsScreenNext]
	PositionEntity CreditsScreen[CreditsScreenNext], 0, 100 - 0.75, 4096.0 * RoomScale
	FreeEntity Camera
	Camera = CreateCamera()
	CameraFogMode Camera, 1
	CameraRange Camera, 0.01, 5
	CameraFogRange Camera, 0.5, 2.5
	CameraFogColor Camera,0,0,0
	CameraClsColor Camera,0,0,0
	PositionEntity Camera, 0, 100, 1024.0 * RoomScale
	
	d = CreateDoor(0, 0.0, 100 - 0.75, 3072.0 * RoomScale, 0, Null, False, DOOR_LCZ)
	If EntityDistanceSquared(Camera,d\obj)<PowTwo(2.0) Then
		d\open = True
	Else
		d\open = False
	EndIf
	MoveEntity d\obj, 0, -10.0, 0
	
	Repeat
		l = ReadLine(file)
		cl = New CreditsLine
		cl\txt = l
	Until Eof(file)
	
	Delete First CreditsLine
	CreditsTimer = 0
	
End Function

Function DrawCredits()
    Local credits_Y# = (EndingTimer+2000)/2+(opt\GraphicHeight+10)
    Local cl.CreditsLine
    Local id%
    Local endlinesamount%
	Local LastCreditLine.CreditsLine
	
	CameraProjMode Camera, 1
	RenderWorld(Max(0.0,1.0+(ft\accumulator/ft\tickDuration)))
	
	id = 0
	endlinesamount = 0
	LastCreditLine = Null
	Color 255,255,255
	For cl = Each CreditsLine
		cl\id = id
		If Left(cl\txt,1) = "*" Then
			SetFont CreditsFont2
			If (Not cl\stay) Then
				Text opt\GraphicWidth/2,credits_Y+(24*cl\id*MenuScale),Right(cl\txt,Len(cl\txt)-1),True
			EndIf
		ElseIf Left(cl\txt,1) = "/" Then
			LastCreditLine = Before(cl)
		Else
			SetFont CreditsFont
			If cl\stay = False Then
				Text opt\GraphicWidth/2,credits_Y+(24*cl\id*MenuScale),cl\txt,True
			EndIf
		EndIf
		If LastCreditLine<>Null Then
			If cl\id>LastCreditLine\id Then
				cl\stay = True
			EndIf
		EndIf
		If cl\stay Then
			endlinesamount = endlinesamount + 1
		EndIf
		id = id + 1
	Next
	If (credits_Y+(24*LastCreditLine\id*MenuScale)) < -StringHeight(LastCreditLine\txt) Then
		If CreditsTimer >= 0.0 And CreditsTimer < 255.0 Then
			Color Max(Min(CreditsTimer,255),0),Max(Min(CreditsTimer,255),0),Max(Min(CreditsTimer,255),0)
		ElseIf CreditsTimer >= 255.0 Then
			Color 255,255,255
		Else
			Color Max(Min(-CreditsTimer,255),0),Max(Min(-CreditsTimer,255),0),Max(Min(-CreditsTimer,255),0)
		EndIf
	Else
		Color 0,0,0
	EndIf
	If CreditsTimer <> 0.0 Then
		For cl = Each CreditsLine
			If cl\stay Then
				SetFont CreditsFont
				If Left(cl\txt,1) = "/" Then
					Text opt\GraphicWidth/2,(opt\GraphicHeight/2)+(endlinesamount/2)+(24*cl\id*MenuScale),Right(cl\txt,Len(cl\txt)-1),True
				Else
					Text opt\GraphicWidth/2,(opt\GraphicHeight/2)+(24*(cl\id-LastCreditLine\id)*MenuScale)-((endlinesamount/2)*24*MenuScale),cl\txt,True
				EndIf
			EndIf
		Next
	EndIf
    
End Function

Function UpdateCredits()
	Local credits_Y# = (EndingTimer+2000)/2+(opt\GraphicHeight+10)
    Local cl.CreditsLine
    Local id%, i%
    Local endlinesamount%
	Local LastCreditLine.CreditsLine
	
	If SelectedEnding = "scp-035" Then
		ShouldPlay = MUS_TURN_AROUND
	ElseIf SelectedEnding = "scp-2935" Then
		ShouldPlay = MUS_SCP_2935
	ElseIf SelectedEnding = "c1" Then
		ShouldPlay = MUS_GOOD_KARMA
	ElseIf SelectedEnding = "d1" Then
		ShouldPlay = MUS_CREDITS
	Else
		ShouldPlay = MUS_CREDITS
	EndIf
	
	MoveEntity Camera, 0, 0, 0.008*FPSfactor2
	If EntityZ(Camera) > 3072.0 * RoomScale Then
		CreditsScreenCurrent = CreditsScreenNext
		While CreditsScreenCurrent = CreditsScreenNext
			CreditsScreenNext = Rand(0, (MaxCreditScreens-1))
		Wend
		For i = 0 To (MaxCreditScreens-1)
			HideEntity CreditsScreen[i]
		Next
		ShowEntity CreditsScreen[CreditsScreenCurrent]
		ShowEntity CreditsScreen[CreditsScreenNext]
		PositionEntity CreditsScreen[CreditsScreenCurrent], 0, 100 - 0.75, 2048.0 * RoomScale
		PositionEntity CreditsScreen[CreditsScreenNext], 0, 100 - 0.75, 4096.0 * RoomScale
		PositionEntity Camera, EntityX(Camera), EntityY(Camera), EntityZ(Camera) - 2048.0 * RoomScale
		CaptureWorld()
	EndIf
	
	id = 0
	endlinesamount = 0
	LastCreditLine = Null
	
	For cl = Each CreditsLine
		cl\id = id
		If Left(cl\txt,1) = "/" Then
			LastCreditLine = Before(cl)
		EndIf
		If LastCreditLine <> Null Then
			If cl\id>LastCreditLine\id Then
				cl\stay = True
			EndIf
		EndIf
		If cl\stay Then
			endlinesamount = endlinesamount + 1
		EndIf
		id = id + 1
	Next
	If (credits_Y+(24*LastCreditLine\id*MenuScale)) < -StringHeight(LastCreditLine\txt) Then
		CreditsTimer=CreditsTimer+(0.5*FPSfactor2)
		If CreditsTimer>=255.0 Then
			If CreditsTimer > 500.0 Then
				CreditsTimer = -255.0
			EndIf
		ElseIf CreditsTimer < 0.0 Then
			If CreditsTimer >= -1.0 Then
				CreditsTimer = -1.0
			EndIf
		EndIf
	EndIf
	
	If GetKey() Then CreditsTimer = -1
	
	If CreditsTimer = -1 Then
		FreeFont CreditsFont
		FreeFont CreditsFont2
		For i = 0 To (MaxCreditScreens-1)
			CreditsScreen[i] = 0
		Next
		CreditsScreenCurrent = 0
		CreditsScreenNext = 0
		FreeImage EndingScreen
		EndingScreen = 0
		Delete Each CreditsLine
		Local prevMainMenuOpen = MainMenuOpen
		If (Not prevMainMenuOpen) Then
			MainMenuOpen = True
			NullGame(False, False)
		Else
			EndingTimer = 0
			Reload()
		EndIf
		If (Not prevMainMenuOpen) Then
			StopStream_Strict(MusicCHN)
			PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Bells.ogg"))
		EndIf
		MenuOpen = False
        MainMenuTab = 0
        CurrSave = Null
        FlushKeys()
	EndIf
	
End Function

Function MovePlayer()
	CatchErrors("Uncaught (MovePlayer)")
	Local Sprint# = 1.0, Speed# = 0.018, i%, angle#
	
	If SuperMan Then
		
		Speed = Speed * 3
		
		SuperManTimer=SuperManTimer+FPSfactor
		
		CameraShake = Sin(SuperManTimer / 5.0) * (SuperManTimer / 1500.0)
		
		If SuperManTimer > 70 * 50 Then
			m_msg\DeathTxt = GetLocalString("Singleplayer","superman_death_1")
			m_msg\DeathTxt = m_msg\DeathTxt + GetLocalString("Singleplayer","superman_death_2")
			Kill()
			ShowEntity Fog
		Else
			BlurTimer = 500		
			HideEntity Fog
		EndIf
		
	EndIf
	
	If DeathTimer > 0 Then
		DeathTimer=DeathTimer-FPSfactor
		If DeathTimer < 1 Then DeathTimer = -1.0
	ElseIf DeathTimer < 0 
		Kill()
	EndIf
	
	If CurrSpeed > 0 Then
        Stamina = Min(Stamina + 0.15 * FPSfactor/1.25, 100.0)
    Else
        Stamina = Min(Stamina + 0.15 * FPSfactor*1.25, 100.0)
    EndIf
	
	If StaminaEffectTimer > 0 Then
		StaminaEffectTimer = StaminaEffectTimer - (FPSfactor/70)
	Else
		If StaminaEffect <> 1.0 Then StaminaEffect = 1.0
	EndIf
	
	Local temp#
	
	If PlayerRoom\RoomTemplate\Name <> "pocketdimension" And (Not InvOpen) And OtherOpen = Null Then
		If KeyDown(KEY_SPRINT) Then
			If Stamina < 5 Then
				temp = 0
				
				If Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask" Then temp = 1
				
				If ChannelPlaying(BreathCHN)=False Then BreathCHN = PlaySound_Strict(BreathSFX[temp * 5])
			ElseIf Stamina < 50
				If BreathCHN=0 Then
					temp = 0
					
					If Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask" Then temp=1
					
					BreathCHN = PlaySound_Strict(BreathSFX[temp * 5 + Rand(1,3)])
					ChannelVolume BreathCHN, Min((70.0-Stamina)/70.0,1.0)*opt\SFXVolume*opt\MasterVol
				Else
					If ChannelPlaying(BreathCHN)=False Then
						temp = 0
						
						If Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask" Then temp=1
						
						BreathCHN = PlaySound_Strict(BreathSFX[temp * 5 + Rand(1,3)])
						ChannelVolume BreathCHN, Min((70.0-Stamina)/70.0,1.0)*opt\SFXVolume*opt\MasterVol		
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
	If I_714\Using > 0 Then 
		Stamina = Min(Stamina, 10)
		Sanity = Max(-850, Sanity)
	EndIf
	
	If IsZombie Then Crouch = False
	
	If (Not psp\NoMove) Then
		If Abs(CrouchState-Crouch)<0.001 Then 
			CrouchState = Crouch
		Else
			CrouchState = CurveValue(Crouch, CrouchState, 10.0)
		EndIf
		If (Not NoClip) Then
			
			; ~ [CONTROLLER]
			
			If co\Enabled
				Local case1% = 0
				Local case2% = ((GetLeftAnalogStickPitch()<>0 Lor GetLeftAnalogStickYaw()<>0) And Playable)
			Else
				case1% = ((KeyDown(KEY_DOWN) Xor KeyDown(KEY_UP)) Lor (KeyDown(KEY_RIGHT) Xor KeyDown(KEY_LEFT)) And Playable)
				case2% = 0
			EndIf
			If case1% Lor case2% Lor ForceMove>0
				
				; ~ [CONTROLLER]
				
				Local SprintKeyAssigned = False
				If (Not co\Enabled)
					If KeyDown(KEY_SPRINT) Then SprintKeyAssigned = True
				Else
					If IsPlayerSprinting
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
				
				IsPlayerSprinting% = False
				
				If (Not Crouch) And (SprintKeyAssigned) And Stamina > 0.0 And (Not IsZombie) And g_I\IronSight = 0 And (Not InvOpen) And OtherOpen = Null Then
					Sprint = 2.5
					IsPlayerSprinting% = True
					Stamina = Stamina - FPSfactor * 0.25 * StaminaEffect
					If Stamina <= 0 Then Stamina = -20.0
				EndIf
				If PlayerRoom\RoomTemplate\Name = "pocketdimension" Then 
					If EntityY(Collider)<2000*RoomScale Lor EntityY(Collider)>2608*RoomScale Then
						Stamina = 0
						IsPlayerSprinting% = False
						Speed = 0.015
						Sprint = 1.0					
					EndIf
				EndIf
				
				If InvOpen Lor OtherOpen <> Null Then Speed = 0.009
				
				If ForceMove > 0 Then Speed = Speed * ForceMove
				
				If SelectedItem<>Null Then
					If SelectedItem\itemtemplate\tempname = "firstaid" Lor SelectedItem\itemtemplate\tempname = "finefirstaid" Lor SelectedItem\itemtemplate\tempname = "firstaid2" Then 
						Sprint = 0
						IsPlayerSprinting% = False
					EndIf
				EndIf
				
				Sprint = (Sprint / (1.0+Crouch))
				
				temp# = (Shake Mod 360)
				Local tempchn%
				Local auxchn%
				If (Not UnableToMove%) Then Shake# = (Shake + FPSfactor * Min(Sprint, 1.7) * 10) Mod 720
				If temp < 180 And (Shake Mod 360) >= 180 And KillTimer>=0 Then
					If CurrStepSFX=0 Then
						temp = GetStepSound(Collider)
						If Sprint = 1.0 Then
							PlayerSoundVolume = Max(4.0,PlayerSoundVolume)
							tempchn% = PlaySound_Strict(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
							ChannelVolume tempchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 4) = "vest" Then
								auxchn% = PlaySound_Strict(EquipmentSFX[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
								ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							ElseIf Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" Then
								auxchn% = PlaySound_Strict(HDSWalkSFX[Rand(0,3)])
								ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							EndIf
						Else
							PlayerSoundVolume = Max(2.5-(Crouch*0.6),PlayerSoundVolume)
							tempchn% = PlaySound_Strict(mpl\StepSoundRun[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
							ChannelVolume tempchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 4) = "vest" Then
								auxchn% = PlaySound_Strict(EquipmentSFX[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
								ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							ElseIf Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" Then
								auxchn% = PlaySound_Strict(HDSWalkSFX[Rand(0,3)])
								ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							EndIf
						EndIf
					ElseIf CurrStepSFX=1 Then
						tempchn% = PlaySound_Strict(Step2SFX[Rand(0, 2)])
						ChannelVolume tempchn, (1.0-(Crouch*0.4))*(opt\SFXVolume*opt\MasterVol)
						If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 4) = "vest" Then
							auxchn% = PlaySound_Strict(EquipmentSFX[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
							ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
						ElseIf Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" Then
							auxchn% = PlaySound_Strict(HDSWalkSFX[Rand(0,3)])
							ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
						EndIf
					ElseIf CurrStepSFX=2 Then
						tempchn% = PlaySound_Strict(Step2SFX[Rand(3,5)])
						ChannelVolume tempchn, (1.0-(Crouch*0.4))*(opt\SFXVolume*opt\MasterVol)
						If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 4) = "vest" Then
							auxchn% = PlaySound_Strict(EquipmentSFX[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
							ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
						ElseIf Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" Then
							auxchn% = PlaySound_Strict(HDSWalkSFX[Rand(0,3)])
							ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
						EndIf
					ElseIf CurrStepSFX=3 Then
						If Sprint = 1.0 Then
							PlayerSoundVolume = Max(4.0,PlayerSoundVolume)
							tempchn% = PlaySound_Strict(StepSFX(0, 0, Rand(0, 7)))
							ChannelVolume tempchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 4) = "vest" Then
								auxchn% = PlaySound_Strict(EquipmentSFX[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
								ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							ElseIf Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" Then
								auxchn% = PlaySound_Strict(HDSWalkSFX[Rand(0,3)])
								ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							EndIf
						Else
							PlayerSoundVolume = Max(2.5-(Crouch*0.6),PlayerSoundVolume)
							tempchn% = PlaySound_Strict(StepSFX(0, 1, Rand(0, 7)))
							ChannelVolume tempchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 4) = "vest" Then
								auxchn% = PlaySound_Strict(EquipmentSFX[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)])
								ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							ElseIf Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" Then
								auxchn% = PlaySound_Strict(HDSWalkSFX[Rand(0,3)])
								ChannelVolume auxchn, (1.0-(Crouch*0.6))*(opt\SFXVolume*opt\MasterVol)
							EndIf
						EndIf
					EndIf
				EndIf
				
				Sprint = (Sprint * (1.0+Crouch))
			Else
				IsPlayerSprinting% = False
			EndIf
		Else ; ~ Noclip on
			
			; ~ [CONTROLLER]
			
			If (Not co\Enabled)
				If (KeyDown(KEY_SPRINT) And (Not InvOpen) And OtherOpen = Null) Then 
					Sprint = 2.5
				ElseIf KeyDown(KEY_CROUCH)
					Sprint = 0.5
				EndIf
			Else
				If IsPlayerSprinting
					If JoyDown(CK_Sprint And (Not InvOpen) And OtherOpen = Null)
						Sprint = 2.5
					ElseIf JoyDown(CK_Crouch)
						Sprint = 0.5
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
	; ~ [CONTROLLER]
	
	If opt\HoldToCrouch Then
		If Playable Then
			If (Not co\Enabled) Then
				Crouch = KeyDown(KEY_CROUCH)
			Else
				Crouch = JoyDown(CK_Crouch)
			EndIf
		EndIf
	Else
		If (Not co\Enabled) Then
			If KeyHit(KEY_CROUCH) And Playable Then Crouch = (Not Crouch)
		Else
			If JoyHit(CK_Crouch) And Playable Then Crouch = (Not Crouch)
		EndIf
	EndIf
	
	Local temp2# = (Speed * Sprint) / (1.0+CrouchState)
	
	If NoClip Then 
		Shake = 0
		CurrSpeed = 0
		CrouchState = 0
		Crouch = 0
		
		RotateEntity Collider, WrapAngle(EntityPitch(Camera)), WrapAngle(EntityYaw(Camera)), 0
		
		temp2 = temp2 * NoClipSpeed
		
		; ~ [CONTROLLER]
		
		If (Not co\Enabled)
			If KeyDown(KEY_DOWN) Then MoveEntity Collider, 0, 0, -temp2*FPSfactor
			If KeyDown(KEY_UP) Then MoveEntity Collider, 0, 0, temp2*FPSfactor
			
			If KeyDown(KEY_LEFT) Then MoveEntity Collider, -temp2*FPSfactor, 0, 0
			If KeyDown(KEY_RIGHT) Then MoveEntity Collider, temp2*FPSfactor, 0, 0
		Else
			If GetLeftAnalogStickPitch()<0.0
				MoveEntity Collider, 0, 0, -temp2*FPSfactor
			EndIf
			If GetLeftAnalogStickPitch()>0.0
				MoveEntity Collider, 0, 0, temp2*FPSfactor
			EndIf
			If GetLeftAnalogStickYaw(True)<0.0
				MoveEntity Collider, -temp2*FPSfactor, 0, 0
			EndIf
			If GetLeftAnalogStickYaw(True)>0.0
				MoveEntity Collider, temp2*FPSfactor, 0, 0
			EndIf
		EndIf
		
		ResetEntity Collider
	Else
		If (Not psp\NoMove) Then
			temp = False
			If (Not IsZombie%)
				If (Not co\Enabled)
					If KeyDown(KEY_DOWN) And (Not KeyDown(KEY_UP)) And Playable Then
						temp = True 
						angle = 180
						If KeyDown(KEY_LEFT) And (Not KeyDown(KEY_RIGHT)) Then angle = 135 
						If KeyDown(KEY_RIGHT) And (Not KeyDown(KEY_LEFT)) Then angle = -135
					ElseIf KeyDown(KEY_UP) And (Not KeyDown(KEY_DOWN)) And Playable Then
						temp = True
						angle = 0
						If KeyDown(KEY_LEFT) And (Not KeyDown(KEY_RIGHT)) Then angle = 45
						If KeyDown(KEY_RIGHT) And (Not KeyDown(KEY_LEFT)) Then angle = -45
					ElseIf ForceMove>0 Then
						temp=True
						angle = ForceAngle
					Else If Playable Then
						If KeyDown(KEY_LEFT) And (Not KeyDown(KEY_RIGHT)) Then angle = 90 : temp = True
						If KeyDown(KEY_RIGHT) And (Not KeyDown(KEY_LEFT)) Then angle = -90 : temp = True 
					EndIf
				Else
					
					; ~ [CONTROLLER]
					
					If GetLeftAnalogStickPitch()<0.0 And Playable
						temp = True
						angle = 180
						If GetLeftAnalogStickYaw(True)<>0.0
							angle = GetLeftAnalogStickYaw(True,True)*(180.0-(45.0*Abs(GetLeftAnalogStickYaw())))
						EndIf
					ElseIf GetLeftAnalogStickPitch()>0.0 And Playable
						temp = True
						angle = 0
						If GetLeftAnalogStickYaw(True)<>0.0
							angle = GetLeftAnalogStickYaw(True,True)*(45.0*Abs(GetLeftAnalogStickYaw()))
						EndIf
					ElseIf ForceMove>0
						temp = True
						angle = ForceAngle
					ElseIf Playable
						If GetLeftAnalogStickYaw(True)<>0.0
							angle = GetLeftAnalogStickYaw(True,True)*90.0
							temp = True
						EndIf
					EndIf
				EndIf
			Else
				temp=True
				angle = ForceAngle
			EndIf
			
			angle = WrapAngle(EntityYaw(Collider,True)+angle+90.0)
			
			If temp Then 
				CurrSpeed = CurveValue(temp2, CurrSpeed, 20.0)
			Else
				CurrSpeed = Max(CurveValue(0.0, CurrSpeed-0.1, 1.0),0.0)
			EndIf
			
			If (Not UnableToMove%) Then TranslateEntity Collider, Cos(angle)*CurrSpeed * FPSfactor, 0, Sin(angle)*CurrSpeed * FPSfactor, True
		EndIf
		
		Local CollidedFloor% = False
		For i = 1 To CountCollisions(Collider)
			If CollisionY(Collider, i) < EntityY(Collider) - 0.25 Then CollidedFloor = True
		Next
		
		If CollidedFloor = True Then
			If DropSpeed# < - 0.07 Then
				If CurrStepSFX=0 Then
					PlaySound_Strict(StepSFX(GetStepSound(Collider), 0, Rand(0, 7)))
				ElseIf CurrStepSFX=1
					PlaySound_Strict(Step2SFX[Rand(0, 2)])
				ElseIf CurrStepSFX=2
					PlaySound_Strict(Step2SFX[Rand(3, 5)])
				ElseIf CurrStepSFX=3
					PlaySound_Strict(StepSFX(0, 0, Rand(0, 7)))
				EndIf
				PlayerSoundVolume = Max(3.0,PlayerSoundVolume)
			EndIf
			DropSpeed# = 0
		Else
			If PlayerFallingPickDistance#<>0.0
				Local pick = LinePick(EntityX(Collider),EntityY(Collider),EntityZ(Collider),0,-PlayerFallingPickDistance,0)
				If pick
					DropSpeed# = Min(Max(DropSpeed - 0.006 * FPSfactor, -2.0), 0.0)
				Else
					DropSpeed# = 0
				EndIf
			Else
				;DropSpeed# = Min(Max(DropSpeed - 0.006 * FPSFactor, -2.0), 0.0)
				DropSpeed# = Min(Max(DropSpeed - 0.003 * FPSfactor, -2.0), 0.0)
			EndIf
		EndIf
		PlayerFallingPickDistance# = 10.0
		
		If (Not UnableToMove%) And ShouldEntitiesFall Then TranslateEntity Collider, 0, DropSpeed * FPSfactor, 0
	EndIf
	
	ForceMove = False
	
	Update008()
	Update016()
	;Update059()
	Update409()
	
	If HealTimer > 0 Then
		;debuglog HealTimer
		HealTimer = HealTimer - (FPSfactor / 70)
		HealSPPlayer(0.01 * FPSfactor)
	EndIf
	If Playable Then
		If (Not co\Enabled)
			;If (EntityVisible(Camera, Curr173\Collider)) Lor IsCutscene Then
				If KeyHit(KEY_BLINK) Then BlinkTimer = 0 : BlurTimer = BlurTimer - 5
				If KeyDown(KEY_BLINK) And BlinkTimer < - 10 Then BlinkTimer = -10
			;EndIf
		Else
			
			; ~ [CONTROLLER]
			;If (EntityVisible(Camera, Curr173\Collider)) Lor IsCutscene Then
				If JoyHit(CK_Blink) Then BlinkTimer = 0 : BlurTimer = BlurTimer - 5
				If JoyDown(CK_Blink) And BlinkTimer < - 10 Then BlinkTimer = -10
			;EndIf
		EndIf
	EndIf
	If HeartBeatVolume > 0 Then
		If HeartBeatTimer <= 0 Then
			tempchn = PlaySound_Strict (HeartBeatSFX)
			ChannelVolume tempchn, HeartBeatVolume*(opt\SFXVolume*opt\MasterVol)
			HeartBeatTimer = 70.0*(60.0/Max(HeartBeatRate,1.0))
		Else
			HeartBeatTimer = HeartBeatTimer - FPSfactor
		EndIf
		HeartBeatVolume = Max(HeartBeatVolume - FPSfactor*0.001, 0)
	EndIf
	
	CatchErrors("MovePlayer")
	
End Function

Function MouseLook()
	Local i%, g.Guns, currGun.Guns
	
	CameraShake = Max(CameraShake - (FPSfactor / 10), 0)
	BigCameraShake = Max(BigCameraShake - (FPSfactor / 10), 0)
	
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
	
	If KillTimer >= 0 And FallTimer >=0 Then
		
		HeadDropSpeed = 0
		
		Local up# = (Sin(Shake) / (20.0+CrouchState*20.0))*0.6
		Local roll# = Max(Min(Sin(Shake/2)*0.625,8.0),-8.0)
		
		PositionEntity Camera, EntityX(Collider), EntityY(Collider), EntityZ(Collider)
		
		If (Not psp\NoRotation) Then
			RotateEntity Camera, 0, EntityYaw(Collider), (roll*0.5)*0.5
			MoveEntity Camera, side, (up*0.5) + 0.6 + CrouchState * -0.3, 0
			If mpl\WheelOpened=WHEEL_CLOSED Then
				If (Not co\Enabled)
					Mouse_X_Speed_1# = CurveValue(MouseXSpeed() * (MouseSens + 0.6) , Mouse_X_Speed_1, (6.0 / (MouseSens + 1.0))*opt\MouseSmooth)
				Else
					
					; ~ [CONTROLLER]
					
					If GetRightAnalogStickYaw(True)<>0.0
						Mouse_X_Speed_1# = CurveValue(GetRightAnalogStickYaw() * ((co\Sensitivity+0.6)*10*FPSfactor), Mouse_X_Speed_1, 6.0 / ((co\Sensitivity+1.0)*10*FPSfactor))
					Else
						Mouse_X_Speed_1# = CurveValue(0.0, Mouse_X_Speed_1, 6.0 / ((co\Sensitivity+1.0)*10*FPSfactor))
					EndIf
				EndIf
				If IsNaN(Mouse_X_Speed_1) Then Mouse_X_Speed_1 = 0
				
				If (Not co\Enabled)
					If InvertMouse Then
						Mouse_Y_Speed_1# = CurveValue(-MouseYSpeed() * (MouseSens + 0.6), Mouse_Y_Speed_1, (6.0/(MouseSens+1.0))*opt\MouseSmooth)
					Else
						Mouse_Y_Speed_1# = CurveValue(MouseYSpeed () * (MouseSens + 0.6), Mouse_Y_Speed_1, (6.0/(MouseSens+1.0))*opt\MouseSmooth) 
					EndIf
				Else
					
					; ~ [CONTROLLER]
					
					If Int(GetRightAnalogStickPitch(True))<>0
						Mouse_Y_Speed_1# = CurveValue(GetRightAnalogStickPitch(False,InvertMouse) * ((co\Sensitivity+0.6)*10*FPSfactor), Mouse_Y_Speed_1, 6.0/((co\Sensitivity+1.0)*10*FPSfactor))
					Else
						Mouse_Y_Speed_1# = CurveValue(0.0, Mouse_Y_Speed_1, 6.0/((co\Sensitivity+1.0)*10*FPSfactor))
					EndIf
				EndIf
				If IsNaN(Mouse_Y_Speed_1) Then Mouse_Y_Speed_1 = 0
				
				Local the_yaw# = ((Mouse_X_Speed_1#)) * MouseLook_X_Inc# / (1.0);+wbl\Vest)
				Local the_pitch# = ((Mouse_Y_Speed_1#)) * MouseLook_Y_Inc# / (1.0);+wbl\Vest)
				
				TurnEntity Collider, 0.0, -the_yaw#, 0.0
				If UnableToMove = 2
					RotateEntity Collider,0.0,Max(Min(EntityYaw(Collider),70),-70),0.0
				EndIf
				user_camera_pitch# = user_camera_pitch# + the_pitch#
				If user_camera_pitch# > 70.0 Then user_camera_pitch# = 70.0
				If user_camera_pitch# < - 70.0 Then user_camera_pitch# = -70.0
			EndIf
		Else
			RotateEntity Camera, 0, EntityYaw(Collider), 0
			Shake = 0
			MoveEntity Camera, 0, 0.6 + CrouchState * -0.3, 0
		EndIf
		
		If (Not NoClip)
			
		Else
			
		EndIf
		
		Local ShakeTimer# = CameraShake + BigCameraShake
		
		RotateEntity Camera, WrapAngle(user_camera_pitch + Rnd(-ShakeTimer, ShakeTimer)), WrapAngle(EntityYaw(Collider) + Rnd(-ShakeTimer, ShakeTimer)), roll
		
		If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
			If PlayerRoom\RoomTemplate\Name = "pocketdimension" Then
				If EntityY(Collider)<2000*RoomScale Lor EntityY(Collider)>2608*RoomScale Then
					RotateEntity Camera, WrapAngle(EntityPitch(Camera)),WrapAngle(EntityYaw(Camera)), roll+WrapAngle(Sin(MilliSecs()/150.0)*30.0)
				EndIf
			EndIf
		EndIf
	Else
		HideEntity Collider
		PositionEntity Camera, EntityX(Head), EntityY(Head), EntityZ(Head)
		
		Local CollidedFloor% = False
		
		For i = 1 To CountCollisions(Head)
			If CollisionY(Head, i) < EntityY(Head) - 0.01 Then CollidedFloor = True
		Next
		
		If CollidedFloor = True Then
			HeadDropSpeed# = 0
		Else
			If KillAnim = 0 Then 
				MoveEntity Head, 0, 0, HeadDropSpeed
				RotateEntity(Head, CurveAngle(-90.0, EntityPitch(Head), 20.0), EntityYaw(Head), EntityRoll(Head))
				RotateEntity(Camera, CurveAngle(EntityPitch(Head) - 40.0, EntityPitch(Camera), 40.0), EntityYaw(Camera), EntityRoll(Camera))
			Else
				MoveEntity Head, 0, 0, -HeadDropSpeed
				RotateEntity(Head, CurveAngle(90.0, EntityPitch(Head), 20.0), EntityYaw(Head), EntityRoll(Head))
				RotateEntity(Camera, CurveAngle(EntityPitch(Head) + 40.0, EntityPitch(Camera), 40.0), EntityYaw(Camera), EntityRoll(Camera))
			EndIf
			
			HeadDropSpeed# = HeadDropSpeed - 0.002 * FPSfactor
		EndIf
		
		If (Not co\Enabled)
			If InvertMouse Then
				TurnEntity (Camera, -MouseYSpeed() * 0.05 * FPSfactor, -MouseXSpeed() * 0.15 * FPSfactor, 0)
			Else
				TurnEntity (Camera, MouseYSpeed() * 0.05 * FPSfactor, -MouseXSpeed() * 0.15 * FPSfactor, 0)
			EndIf
		Else
			
			; ~ [CONTROLLER]
			
			TurnEntity (Camera, GetRightAnalogStickPitch(False,InvertMouse) * 0.05 * FPSfactor, GetRightAnalogStickYaw(False,True) * 0.15 * FPSfactor, 0)
		EndIf
		
	EndIf
	If ParticleAmount=2
		If Rand(35) = 1 Then
			Local pvt% = CreatePivot()
			PositionEntity(pvt, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True))
			RotateEntity(pvt, 0, Rnd(360), 0)
			If Rand(2) = 1 Then
				MoveEntity(pvt, 0, Rnd(-0.5, 0.5), Rnd(0.5, 1.0))
			Else
				MoveEntity(pvt, 0, Rnd(-0.5, 0.5), Rnd(0.5, 1.0))
			End If
			
			Local p.Particles = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 2, 0.002, 0, 300)
			p\speed = 0.001
			RotateEntity(p\pvt, Rnd(-20, 20), Rnd(360), 0)
			
			p\SizeChange = -0.00001
			
			FreeEntity pvt
		End If
	EndIf
	If mpl\WheelOpened=WHEEL_CLOSED Then
		If (ScaledMouseX() > Mouse_Right_Limit) Lor (ScaledMouseX() < Mouse_Left_Limit) Lor (ScaledMouseY() > Mouse_Bottom_Limit) Lor (ScaledMouseY() < Mouse_Top_Limit)
			MoveMouse Viewport_Center_X, Viewport_Center_Y
		EndIf
	EndIf
	
	If Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask" Then
		ShowEntity(GasMaskOverlay)
	Else
		HideEntity(GasMaskOverlay)
	EndIf
	
	If Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet" Then
		ShowEntity(HelmetOverlay)
	Else
		HideEntity(HelmetOverlay)
	EndIf
	
	If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat" Then
		ShowEntity(HazmatOverlay)
	Else
		HideEntity(HazmatOverlay)
	EndIf
	
	If (Not I_714\Using) Then
		If Inventory[SLOT_HEAD] <> Null And Inventory[SLOT_HEAD]\itemtemplate\tempname = "gasmask2" Lor Inventory[SLOT_TORSO] <> Null And Inventory[SLOT_TORSO]\itemtemplate\tempname = "hazmat2" Then
			Stamina = Min(100, Stamina + (100.0-Stamina)*0.01*FPSfactor)
		EndIf
	EndIf
	If Inventory[SLOT_TORSO] <> Null And Inventory[SLOT_TORSO]\itemtemplate\tempname = "hazmat" Then
		Stamina = Min(60, Stamina)
	EndIf
	
	If I_109\Used > 0 Then 
		I_109\Timer = Max(I_109\Timer - FPSfactor / 1.5, 0)
		
		If I_109\Timer = 0 Then 
			I_109\Timer = 70 * 10
			I_109\Used = I_109\Used - 1
		EndIf
	EndIf
	
	If Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "scp268" Then
		ShowEntity(SCP268Overlay)
		ShouldPlay = MUS_NULL
	Else
		HideEntity(SCP268Overlay)
	EndIf
	If I_268\Timer < 1 Then
		HideEntity(SCP268Overlay)
	EndIf
	
	If I_1033RU\Using = 1 Then
		I_1033RU\HP = 100 - I_1033RU\DHP
	ElseIf I_1033RU\Using = 2
		I_1033RU\HP = 200 - I_1033RU\DHP
	Else
		I_1033RU\HP = 0
	EndIf
	
	If I_1033RU\Using > 0 And I_1033RU\HP > 0 Then
		ShouldPlay = MUS_SCP_1033RU
	EndIf
	
	If Inventory[SLOT_HEAD] <> Null And (Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 3) = "nvg" Lor Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 8) = "scramble") Then
		
		ShowEntity(NVOverlay)
		
		If Inventory[SLOT_HEAD]\state > 0 Then
			
			If Inventory[SLOT_HEAD] <> Null And Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg2" Then
				EntityColor(NVOverlay, 0,100,255)
				AmbientLightRooms(15)
			ElseIf Inventory[SLOT_HEAD] <> Null And Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg3" Then
				EntityColor(NVOverlay, 255,0,0)
				AmbientLightRooms(15)
			ElseIf Inventory[SLOT_HEAD] <> Null And Inventory[SLOT_HEAD]\itemtemplate\tempname = "nvg" Then
				EntityColor(NVOverlay, 0.0, 255.0, 0.0)
				AmbientLightRooms(15)
			Else
				EntityColor(NVOverlay, 200.0, 200.0, 200.0)
				AmbientLightRooms(0)
			EndIf
			If NVTexture <> 0 Then EntityTexture(Fog, NVTexture)
			
		EndIf
	Else
		AmbientLightRooms(0)
		HideEntity(NVOverlay)
		EntityTexture(Fog, FogTexture)
	EndIf
	
End Function

;! ~ [GUI]

Include "SourceCode\TaskSystem.bb"
Include "SourceCode\Inventory.bb"
Include "SourceCode\GUI.bb"

;! ~ [LOADING]

Function LoadEntities()
	CatchErrors("LoadEntities")
	DrawLoading(0, False, "","Creating_Player")
	
	CreateSPPlayer()
	
	LoadMissingTexture()
	
	InitConsole(1)
	
	Local i%
	
	For i=0 To 9
		TempSounds[i]=0
	Next
	
	MainMenuTab = MenuTab_Default
	
	PauseMenuIMG[0] = LoadImage_Strict("GFX\menu\pausemenu.jpg")
	PauseMenuIMG[1] = LoadImage_Strict("GFX\menu\pausemenu (2).jpg")
	For i = 0 To 1
		MaskImage PauseMenuIMG[i], 255,0,255
		ScaleImage PauseMenuIMG[i],MenuScale,MenuScale
	Next
	
	InitSubtitlesAssets()
	
	O5_Screen[0] = LoadTexture_Strict("GFX\Map\Textures\world_map.png",1,2)
	O5_Screen[1] = LoadAnimTexture("GFX\Map\Textures\po_screen.png",1,1024,512,0,2)
	O5_Screen[2] = LoadAnimTexture("GFX\Map\Textures\bcz_screen.png",1,1024,512,0,2)
	
	SCP_963_2_Screen[0] = LoadTexture_Strict("GFX\Map\Textures\963-2_screen.jpg",1,2)
	SCP_963_2_Screen[1] = LoadAnimTexture("GFX\Map\Textures\963-2_screen_2.jpg",1,1024,512,0,4)
	
	Checkpoint_Screen[0] = LoadAnimTexture("GFX\Map\Textures\Checkpoint_screen.jpg",1,512,256,0,4)
	Checkpoint_Screen[2] = LoadAnimTexture("GFX\Map\Textures\Checkpoint_screen_2.jpg",1,512,256,0,4)
	Checkpoint_Screen[1] = LoadTexture_Strict("GFX\Map\Textures\Checkpoint_screen_main.jpg",1,2)
	
	Class_D_Screen[0] = LoadAnimTexture("GFX\Map\Textures\class_d_screen.png",1,1024,512,0,4)
	Class_D_Screen[1] = LoadAnimTexture("GFX\Map\Textures\class_d_screen_2.png",1,1024,512,0,4)
	Class_D_Screen[2] = LoadTexture_Strict("GFX\Map\Textures\class_d_screen_3.png",1,2)
	
	Surveil_Room_Textures[0] = LoadAnimTexture("GFX\Map\Textures\SL_monitors_checkpoint.jpg",1,512,512,0,4)
	Surveil_Room_Textures[1] = LoadAnimTexture("GFX\Map\Textures\Sl_monitors.jpg",1,256,256,0,8)
	
	Satellite_Screen[0] = LoadAnimTexture("GFX\Map\Textures\satellite_ready.png",1,1024,512,0,4)
	Satellite_Screen[1] = LoadAnimTexture("GFX\Map\Textures\satellite_not_ready.png",1,1024,512,0,4)
	
	Tram_Screen[0] = LoadAnimTexture("GFX\Map\Textures\tram_station_screen.png",1,1024,512,0,2)
	Tram_Screen[1] = LoadAnimTexture("GFX\Map\Textures\tram_station_screen_2.png",1,1024,512,0,2)
	
	SprintIcon% = LoadImage_Strict("GFX\sprint_icon.png")
	BlinkIcon[0] = LoadImage_Strict("GFX\blink_icon.png")
	BlinkIcon[1] = LoadImage_Strict("GFX\blink_icon_2.png")
	CrouchIcon% = LoadImage_Strict("GFX\sneak_icon.png")
	
	StaminaMeterIMG% = LoadImage_Strict("GFX\stamina_meter.jpg")
	
	inv_character_img% = LoadImage_Strict("GFX\inv_character_img_"+(gopt\GameMode+1)+".png")
	MidHandle inv_character_img
	ResizeImage(inv_character_img, 512, 512)
	
	SCP198Icon = LoadImage_Strict("GFX\198_icon.png")
	SCP268Icon = LoadImage_Strict("GFX\268_icon.png")
	SCP207Icon = LoadImage_Strict("GFX\207_icon.png")
	SCP1033RUIcon = LoadImage_Strict("GFX\1033ru_icon.png")
	
	HDSIcon = LoadImage_Strict("GFX\hds_icon.png")
	
	WeaponFireModeIcons[0] = LoadImage_Strict("GFX\wpn_auto_mode.png")
	WeaponFireModeIcons[1] = LoadImage_Strict("GFX\wpn_semi_mode.png")
	
	DrawLoading(3,False,"","Materials")
	
	KeypadHUD =  LoadImage_Strict("GFX\keypadhud.jpg")
	MaskImage(KeypadHUD, 255,0,255)
	
	Panel294 = LoadImage_Strict("GFX\294panel.jpg")
	MaskImage(Panel294, 255,0,255)
	Brightness% = GetINIFloat(gv\OptionFile, "options", "brightness", 20)
	CameraFogNear# = GetINIFloat(gv\OptionFile, "options", "camera fog near", 0.5)
	CameraFogFar# = GetINIFloat(gv\OptionFile, "options", "camera fog far", 6.0)
	StoredCameraFogFar# = CameraFogFar
	
	;TextureLodBias
	
	LoadMaterials("Data\materials.ini")
	
	AmbientLightRoomTex% = CreateTextureUsingCacheSystem(2,2,1)
	TextureBlend AmbientLightRoomTex,2
	SetBuffer(TextureBuffer(AmbientLightRoomTex))
	ClsColor 0,0,0
	Cls
	SetBuffer BackBuffer()
	AmbientLightRoomVal = 0
	
	SoundEmitter = CreatePivot()
	
	Camera = CreateCamera()
	CameraViewport Camera,0,0,opt\GraphicWidth,opt\GraphicHeight
	CameraRange(Camera, 0.01, CameraFogFar)
	CameraFogMode (Camera, 1)
	CameraFogRange (Camera, CameraFogNear, CameraFogFar)
	CameraFogColor (Camera, GetINIInt(gv\OptionFile, "options", "fog r"), GetINIInt(gv\OptionFile, "options", "fog g"), GetINIInt(gv\OptionFile, "options", "fog b"))
	AmbientLight Brightness, Brightness, Brightness
	
	m_I\Cam = CreateCamera(Camera)
	CameraRange m_I\Cam,0.01,20
	CameraFogRange m_I\Cam,CameraFogNear,CameraFogFar
	CameraFogColor m_I\Cam,0,0,0
	CameraFogMode m_I\Cam,1
	CameraClsMode m_I\Cam,0,1
	CameraProjMode m_I\Cam,0
	m_I\MenuLogo = CreateMenuLogo(m_I\Cam)
	m_I\Sprite = CreateSprite(m_I\Cam)
	ScaleSprite m_I\Sprite,3,3
	EntityColor m_I\Sprite,0,0,0
	EntityFX m_I\Sprite,1
	EntityOrder m_I\Sprite,-1
	EntityAlpha m_I\Sprite,0.0
	m_I\SpriteAlpha = 0.0
	MoveEntity m_I\Sprite,0,0,1
	
	ScreenTexs[0] = CreateTextureUsingCacheSystem(512, 512, 1)
	ScreenTexs[1] = CreateTextureUsingCacheSystem(512, 512, 1)
	
	CreateBlurImage()
	CameraProjMode ark_blur_cam,0
	
	FogTexture = LoadTexture_Strict("GFX\fog.jpg",1,2)
	
	Fog = CreateSprite(ark_blur_cam)
	ScaleSprite(Fog, 1.0, Float(opt\GraphicHeight)/Float(opt\GraphicWidth))
	EntityTexture(Fog, FogTexture)
	EntityBlend (Fog, 2)
	EntityOrder Fog, -1000
	MoveEntity(Fog, 0, 0, 1.0)
	
	HazmatTexture = LoadTexture_Strict("GFX\HazmatOverlay.jpg", 1)
	HazmatOverlay = CreateSprite(ark_blur_cam)
	ScaleSprite(HazmatOverlay, Max(opt\GraphicWidth / 1024.0, 1.0), Max(opt\GraphicHeight / 1024.0 * 0.8, 0.8))
	EntityTexture(HazmatOverlay, HazmatTexture)
	EntityBlend (HazmatOverlay, 2)
	EntityFX(HazmatOverlay, 1)
	EntityOrder HazmatOverlay, -1003
	MoveEntity(HazmatOverlay, 0, 0, 1.0)
	HideEntity(HazmatOverlay)
	
	GasMaskTexture = LoadTexture_Strict("GFX\Gasmask_Overlay_2.jpg", 1)
	GasMaskOverlay = CreateSprite(ark_blur_cam)
	ScaleSprite(GasMaskOverlay, Max(opt\GraphicWidth / 1024.0, 1.0), Max(opt\GraphicHeight / 1024.0 * 0.8, 0.8))
	EntityTexture(GasMaskOverlay, GasMaskTexture)
	EntityBlend (GasMaskOverlay, 2)
	EntityFX(GasMaskOverlay, 1)
	EntityOrder GasMaskOverlay, -1003
	MoveEntity(GasMaskOverlay, 0, 0, 1.0)
	HideEntity(GasMaskOverlay)
	
	InfectTexture = LoadTexture_Strict("GFX\InfectOverlay.jpg",1,2)
	InfectOverlay = CreateSprite(ark_blur_cam)
	ScaleSprite(InfectOverlay, 1.0,Float(opt\GraphicHeight)/Float(opt\GraphicWidth))
	EntityTexture(InfectOverlay, InfectTexture)
	EntityBlend (InfectOverlay, 3)
	EntityFX(InfectOverlay, 1)
	EntityOrder InfectOverlay, -1003
	MoveEntity(InfectOverlay, 0, 0, 1.0)
	HideEntity(InfectOverlay)
	
	NVTexture = LoadTexture_Strict("GFX\NightVisionOverlay.jpg", 1)
	NVOverlay = CreateSprite(ark_blur_cam)
	ScaleSprite(NVOverlay, Max(opt\GraphicWidth / 1024.0, 1.0), Max(opt\GraphicHeight / 1024.0 * 0.8, 0.8))
	EntityTexture(NVOverlay, NVTexture)
	EntityBlend (NVOverlay, 2)
	EntityFX(NVOverlay, 1)
	EntityOrder NVOverlay, -1003
	MoveEntity(NVOverlay, 0, 0, 1.0)
	HideEntity(NVOverlay)
	NVBlink = CreateSprite(ark_blur_cam)
	ScaleSprite(NVBlink, Max(opt\GraphicWidth / 1024.0, 1.0), Max(opt\GraphicHeight / 1024.0 * 0.8, 0.8))
	EntityColor(NVBlink,0,0,0)
	EntityFX(NVBlink, 1)
	EntityOrder NVBlink, -1005
	MoveEntity(NVBlink, 0, 0, 1.0)
	HideEntity(NVBlink)
	
	HelmetTexture = LoadTexture_Strict("GFX\Helmet_Overlay.png", 1)
	HelmetOverlay = CreateSprite(ark_blur_cam)
	ScaleSprite(HelmetOverlay, Max(opt\GraphicWidth / 1024.0, 1.0), Max(opt\GraphicHeight / 1024.0 * 0.8, 0.8))
	EntityTexture(HelmetOverlay, HelmetTexture)
	EntityBlend (HelmetOverlay, 2)
	EntityFX(HelmetOverlay, 1)
	EntityOrder HelmetOverlay, -1003
	MoveEntity(HelmetOverlay, 0, 0, 1.0)
	HideEntity(HelmetOverlay)
	
	SCP268Texture = LoadTexture_Strict("GFX\SCP_268_Overlay.png",1)
	SCP268Overlay = CreateSprite(ark_blur_cam)
	ScaleSprite(SCP268Overlay, Max(opt\GraphicWidth / 1024.0, 1.0), Max(opt\GraphicHeight / 1024.0 * 0.8, 0.8))
	EntityTexture(SCP268Overlay, SCP268Texture)
	EntityBlend (SCP268Overlay, 2)
	EntityFX(SCP268Overlay, 1)
	EntityOrder SCP268Overlay, -1003
	MoveEntity(SCP268Overlay, 0, 0, 1.0)
	HideEntity(SCP268Overlay)
	
	SCP409Texture = LoadTexture_Strict("GFX\SCP_409_Overlay.png",1)
	SCP409Overlay = CreateSprite(ark_blur_cam)
	ScaleSprite(SCP409Overlay, 1.0,Float(opt\GraphicHeight)/Float(opt\GraphicWidth))
	EntityTexture(SCP409Overlay, SCP409Texture)
	EntityBlend (SCP409Overlay, 3)
	EntityFX(SCP409Overlay, 1)
	EntityOrder SCP409Overlay, -1003
	MoveEntity(SCP409Overlay, 0, 0, 1.0)
	HideEntity(SCP409Overlay)
	
	FogNVTexture = LoadTexture_Strict("GFX\fogNV.jpg", 1, 2)
	
	DrawLoading(5,False,"","Materials")
	
	TeslaTexture = LoadTexture_Strict("GFX\map\tesla.jpg",1+2,2)
	
	DarkTexture = CreateTextureUsingCacheSystem(1024, 1024, 1 + 2)
	SetBuffer TextureBuffer(DarkTexture)
	Cls
	SetBuffer BackBuffer()
	Dark = CreateSprite(ark_blur_cam)
	ScaleSprite(Dark, 1.0, Float(opt\GraphicHeight)/Float(opt\GraphicWidth))
	EntityTexture(Dark, DarkTexture)
	EntityBlend (Dark, 1)
	EntityOrder Dark, -1002
	MoveEntity(Dark, 0, 0, 1.0)
	EntityAlpha Dark, 0.0
	HideEntity Dark
	
	LightTexture = CreateTextureUsingCacheSystem(1024, 1024, 1 + 2)
	SetBuffer TextureBuffer(LightTexture)
	ClsColor 255, 255, 255
	Cls
	ClsColor 0, 0, 0
	SetBuffer BackBuffer()
	Light = CreateSprite(ark_blur_cam)
	ScaleSprite(Light, 1.0, Float(opt\GraphicHeight)/Float(opt\GraphicWidth))
	EntityTexture(Light, LightTexture)
	EntityBlend (Light, 1)
	EntityOrder Light, -1002
	MoveEntity(Light, 0, 0, 1.0)
	HideEntity Light
	
	Collider = CreatePivot()
	EntityRadius Collider, 0.15, 0.30
	EntityType Collider, HIT_PLAYER
	
	Head = CreatePivot()
	EntityRadius Head, 0.15
	EntityType Head, HIT_PLAYER
	
	LiquidObj = LoadMesh_Strict("GFX\items\Consumable\cup_liquid.x")
	HideEntity LiquidObj
	
	DrawLoading(9,False,"","Meshes")
	
	NPCModel[Model_035] = LoadAnimMesh_Strict("GFX\npcs\035.b3d")
	NPCModel[Model_049] = LoadAnimMesh_Strict("GFX\npcs\049.b3d")
	NPCModel[Model_076] = LoadAnimMesh_Strict("GFX\npcs\076.b3d")
	NPCModel[Model_096] = LoadAnimMesh_Strict("GFX\npcs\096.b3d")
	
	DrawLoading(10,False,"","Meshes")
	
	NPCModel[Model_106] = LoadAnimMesh_Strict("GFX\npcs\106.b3d")
	NPCModel[Model_173] = LoadAnimMesh_Strict("GFX\npcs\173\173body.b3d")
	NPCModel[Model_173_Head] = LoadAnimMesh_Strict("GFX\npcs\173\173head.b3d")
	
	NPCModel[Model_Guard] = LoadAnimMesh_Strict("GFX\npcs\Guards\FacilityGuard.b3d")
	NPCModel[Model_Vincent] = LoadAnimMesh_Strict("GFX\npcs\Guards\Wounded_FacilityGuard.b3d")
	
	DrawLoading(11,False,"","Meshes")
	
	NPCModel[Model_372] = LoadAnimMesh_Strict("GFX\npcs\372.b3d")
	NPCModel[Model_457] = LoadAnimMesh_Strict("GFX\npcs\457.b3d")
	NPCModel[Model_513] = LoadAnimMesh_Strict("GFX\npcs\513.b3d")
	NPCModel[Model_682] = LoadAnimMesh_Strict("GFX\npcs\682.b3d")
	NPCModel[Model_860] = LoadAnimMesh_Strict("GFX\npcs\860.b3d")
	
	NPCModel[Model_NTF] = LoadAnimMesh_Strict("GFX\npcs\MTF\NTF\Ninetailedfox.b3d")
	NPCModel[Model_CI] = LoadAnimMesh_Strict("GFX\npcs\CI\ChaosInsurgency.b3d")
	
	DrawLoading(12,False,"","Meshes")
	
	NPCModel[Model_966] = LoadAnimMesh_Strict("GFX\npcs\966.b3d")
	NPCModel[Model_1048] = LoadAnimMesh_Strict("GFX\npcs\1048a.b3d")
	
	NPCModel[Model_Class_D] = LoadAnimMesh_Strict("GFX\npcs\classd.b3d")
	NPCModel[Model_Class_D_Armed] = LoadAnimMesh_Strict("GFX\npcs\classd2.b3d")
	
	NPCModel[Model_Tentacle] = LoadAnimMesh_Strict("GFX\npcs\035tentacle.b3d")
	
	NPCModel[Model_SMAN] = LoadAnimMesh_Strict("GFX\npcs\sman.b3d")
	
	For i = Model_035 To MaxNPCModels - 1
		HideEntity NPCModel[i]
	Next
	
	DrawLoading(13,False,"","Meshes")
	
	; ~ Zombie models
	
	ZombieModel[Topless_Zombie] = LoadAnimMesh_Strict("GFX\npcs\zombies\zombie1.b3d")
	ZombieModel[Hazmat_Zombie] = LoadAnimMesh_Strict("GFX\npcs\zombies\zombie2.b3d")
	ZombieModel[ClassD_Zombie] = LoadAnimMesh_Strict("GFX\npcs\zombies\Zombieclassd.b3d")
	ZombieModel[Clerk_Zombie] = LoadAnimMesh_Strict("GFX\npcs\zombies\zombieclerk.b3d")
	ZombieModel[Surgeon_Zombie] = LoadAnimMesh_Strict("GFX\npcs\zombies\zombiesurgeon.b3d")
	ZombieModel[Worker_Zombie] = LoadAnimMesh_Strict("GFX\npcs\zombies\zombieworker.b3d")
	For i = Topless_Zombie To ZombieTypes-1
		HideEntity ZombieModel[i]
	Next
	
	; ~ Guard zombie models
	
	GuardZombieModel[Guard_Zombie] = LoadAnimMesh_Strict("GFX\npcs\zombies\guardzombie.b3d")
	GuardZombieModel[NTF_Zombie] = LoadAnimMesh_Strict("GFX\npcs\zombies\MTFzombie.b3d")
	GuardZombieModel[CI_Zombie] = LoadAnimMesh_Strict("GFX\npcs\zombies\CIzombie.b3d")
	
	For i = Guard_Zombie To GuardZombieTypes-1
		HideEntity GuardZombieModel[i]
	Next
	
	DrawLoading(14,False,"","Meshes")
	
	;[End Block]
	
	NTFTexture[NTF_Texture_Medic] = LoadTexture_Strict("GFX\NPCs\MTF\NTF\NineTailedFox_Medic.png",1,2)
	NTFTexture[NTF_Texture_Commander] = LoadTexture_Strict("GFX\NPCs\MTF\NTF\NineTailedFox_Commander.png",1,2)
	
	DrawLoading(15,False,"","Meshes")
	
	LightSpriteTex[0] = LoadTexture_Strict("GFX\light1.jpg",1,2)
	LightSpriteTex[1] = LoadTexture_Strict("GFX\light2.jpg",1,2)
	LightSpriteTex[2] = LoadTexture_Strict("GFX\lightsprite.jpg",1,2)
	LightSpriteTex[3] = LoadTexture_Strict("GFX\light3.jpg",1,2)
	
	LoadDoors()
	
	DrawLoading(18,False,"","Meshes")
	
	LeverBaseOBJ = LoadMesh_Strict("GFX\map\leverbase.x")
	HideEntity LeverBaseOBJ
	LeverOBJ = LoadMesh_Strict("GFX\map\leverhandle.x")
	HideEntity LeverOBJ
	
	DrawLoading(20,False,"","Decals")
	
	LoadDecals()
	
	DrawLoading(25,False,"","Meshes")
	
	Monitor = LoadMesh_Strict("GFX\map\monitor.b3d")
	HideEntity Monitor
	
	CamBaseOBJ = LoadMesh_Strict("GFX\map\cambase.x")
	HideEntity(CamBaseOBJ)
	CamOBJ = LoadMesh_Strict("GFX\map\CamHead.b3d")
	HideEntity(CamOBJ)
	
	LightConeModel = LoadMesh_Strict("GFX\lightcone.b3d")
	HideEntity LightConeModel
	
	DrawLoading(26,False,"","Textures")
	
	InitItemTemplates()
	
	NavBG = CreateImage(opt\GraphicWidth,opt\GraphicHeight)
	
	DrawLoading(27,False,"","Textures")
	
	ParticleTextures[0] = LoadTexture_Strict("GFX\smoke.png",1+2,2)
	ParticleTextures[1] = LoadTexture_Strict("GFX\flash.jpg",1+2,2)
	ParticleTextures[2] = LoadTexture_Strict("GFX\dust.jpg",1+2,2)
	ParticleTextures[3] = LoadAnimTexture("GFX\flash_rifle.png",1+2,256,256,0,4)
	ParticleTextures[4] = LoadAnimTexture("GFX\flash_suppressed.png",1+2,256,256,0,4)
	ParticleTextures[5] = LoadTexture_Strict("GFX\bloodsprite.png",1+2,2)
	ParticleTextures[6] = LoadTexture_Strict("GFX\smoke2.png",1+2,2)
	ParticleTextures[7] = LoadTexture_Strict("GFX\spark.jpg",1+2,2)
	ParticleTextures[9] = LoadAnimTexture("GFX\fog_textures.png",1+2,256,256,0,4)
	ParticleTextures[12] = LoadTexture_Strict("GFX\WaterParticle3.png",1+2,2)
	ParticleTextures[13] = LoadTexture_Strict("GFX\fire_particle.png",1+2,2)
	ParticleTextures[14] = LoadAnimTexture("GFX\electrical_flash.png",1+2,256,256,0,4)
	ParticleTextures[15] = LoadAnimTexture("GFX\flash_gun.png",1+2,256,256,0,4)
	ParticleTextures[17] = LoadTexture_Strict("GFX\Explosions\Explosion_Flash_1.png",1+2,2)
	ParticleTextures[18] = LoadTexture_Strict("GFX\Explosions\Explosion_Wave.png",1+2,2)
	
	TextureLodBias TextureFloat#
	
	DrawLoading(28,False,"","Weapons")
	
	LoadModStuff()
	
	; ~ Test
	
	CreateCommunicationAndSocialWheel()
	
	ApplyHitBoxes(NPC_Guard,"Guard")
	ApplyHitBoxes(NPC_Zombie,"Zombie2")
	ApplyHitBoxes(NPC_Zombie_Armed,"Zombie")
	ApplyHitBoxes(NPC_SCP_049,"SCP-049")
	ApplyHitBoxes(NPC_CI,"Chaos Insurgency")
	ApplyHitBoxes(NPC_Human,"Class-D")
	ApplyHitBoxes(NPC_Class_D,"Class-D")
	ApplyHitBoxes(NPC_SCP_076,"Zombie2")
	ApplyHitBoxes(NPC_SCP_939,"939")
	ApplyHitBoxes(NPC_SCP_035_Tentacle,"Tentacle")
	ApplyHitBoxes(NPC_SCP_1048,"1048A")
	ApplyHitBoxes(NPC_Elias,"Headless_Zombie")
	
	DrawLoading(30,False,"","Type_variables")
	
	;! ~ Type Instances Variables
	
	wbl = New Wearible
	hds = New HazardousDefenceSuit
	ecst = New EventConstants
	
	I_005 = New SCP005
	I_008 = New SCP008
	I_016 = New SCP016
	I_035 = New SCP035
	;I_059 = New SCP059
	I_109 = New SCP109
	I_127 = New SCP127
	I_198 = New SCP198
	I_207 = New SCP207
	I_268 = New SCP268
	I_330 = New SCP330
	;I_357 = New SCP357
	;I_402 = New SCP402
	I_409 = New SCP409
	I_427 = New SCP427
	I_500 = New SCP500
	I_714 = New SCP714
	;I_1025 = New SCP1025
	I_1033RU = New SCP1033RU
	;I_1079 = New SCP1079
	;I_1102RU = New SCP1102RU
	
	CatchErrors("Uncaught LoadEntities")
End Function

Function AddGearToPlayer(Slot%, ItemName$, Item$, State, State2)
	Local it.Items
	
	;[Block]
	it = CreateItem(ItemName$, Item$, 1, 1, 1)
	it\Picked = True
	it\Dropped = -1
	it\itemtemplate\found=True
	it\state = State
	it\state2 = State2
	Inventory[Slot] = it
	HideEntity(it\collider)
	EntityType (it\collider, HIT_ITEM)
	EntityParent(it\collider, 0)
	ItemAmount = ItemAmount + 1
	;[End Block]
	
End Function

Function InitChapters()
	CatchErrors("InitChapters")
	Local it.Items, g.Guns, i%, randm% = Rand(0, 12)
	
	; ~ Yeah, I know it's messy, but I didn't have time to make it better. - Wolfnaya
	Select gopt\GameMode
		Case GAMEMODE_DEFAULT
			
			Select cpt\Current
				Case 0
					;[Block]
					If gopt\CurrZone = LCZ Then
						AddGearToPlayer(SLOT_HOLSTER, "M9 Beretta", "beretta", 15, 30)
						AddGearToPlayer(SLOT_HEAD, GetLocalString("Item Names", "helmet"), "helmet", 100, 0)
						AddGearToPlayer(SLOT_SCABBARD, GetLocalString("Item Names", "knife"), "knife", 0, 0)
						AddGearToPlayer(0, GetLocalString("Item Names", "key_2"), "key2", 0, 0)
						AddGearToPlayer(1, GetLocalString("Item Names", "radio"), "radio", 1000, 0)
					EndIf
					;[End Block]
				Case 2
					;[Block]
					ecst\WasInLCZCore = True
					ecst\WasInRoom2_SL = True
					ecst\UnlockedAirlock = True
					;[End Block]
					
					;[Block]
					If (Not TaskExists(TASK_FINDWAY_EZDOOR)) Then
						BeginTask(TASK_FINDWAY_EZDOOR)
					EndIf
					If (Not TaskExists(TASK_GET_TOPSIDE)) Then
						BeginTask(TASK_GET_TOPSIDE)
					EndIf
					;[End Block]
					
					;[Block]
					AddGearToPlayer(SLOT_HOLSTER, "FN Five-Seven", "fiveseven", 20, 60)
					AddGearToPlayer(SLOT_SCABBARD, GetLocalString("Item Names", "crowbar"), "crowbar", 0, 0)
					AddGearToPlayer(0, GetLocalString("Item Names", "key_3"), "key3", 0, 0)
					;[End Block]
				Case 3
					;[Block]
					ecst\WasInLCZCore = True
					ecst\WasInRoom2_SL = True
					ecst\UnlockedAirlock = True
					;[End Block]
					
					;[Block]
					If (Not TaskExists(TASK_GET_TOPSIDE)) Then
						BeginTask(TASK_GET_TOPSIDE)
					EndIf
					If (Not TaskExists(TASK_TURN_ON_REACTOR)) Then
						BeginTask(TASK_TURN_ON_REACTOR)
					EndIf
					If TaskExists(TASK_FIND_REACTOR) Then
						EndTask(TASK_FIND_REACTOR)
					EndIf
					;[End Block]
					
					;[Block]
					AddGearToPlayer(SLOT_HOLSTER, "FN Five-Seven", "fiveseven", 20, 60)
					AddGearToPlayer(SLOT_PRIMARY, "FN P90", "p90", 50, 150)
					AddGearToPlayer(SLOT_SCABBARD, GetLocalString("Item Names", "crowbar"), "crowbar", 0, 0)
					AddGearToPlayer(0, GetLocalString("Item Names", "key_4"), "key4", 0, 0)
					;[End Block]
				Case 4
					;[Block]
					ecst\WasInLCZCore = True
					ecst\WasInRoom2_SL = True
					ecst\UnlockedAirlock = True
					
					ecst\EzDoorOpened = True
					ecst\WasInHCZ = True
					ecst\NTFArrived = True
					ecst\UnlockedHDS = True
					ecst\WasInReactor = True
					;[End Block]
					
					;[Block]
					If (Not TaskExists(TASK_LAUNCH_ROCKET)) Then
						BeginTask(TASK_LAUNCH_ROCKET)
					EndIf
					If (Not TaskExists(TASK_GET_TOPSIDE)) Then
						BeginTask(TASK_GET_TOPSIDE)
					EndIf
					If TaskExists(TASK_COME_BACK_TO_GUARD) Then
						EndTask(TASK_COME_BACK_TO_GUARD)
					EndIf
					;[End Block]
					
					;[Block]
					AddGearToPlayer(SLOT_HOLSTER, "FN Five-Seven", "fiveseven", 20, 60)
					AddGearToPlayer(SLOT_PRIMARY, "FN P90", "p90", 50, 150)
					AddGearToPlayer(SLOT_SECONDARY, "Franchi SPAS-12", "spas12", 6, 42)
					AddGearToPlayer(SLOT_SCABBARD, GetLocalString("Item Names", "crowbar"), "crowbar", 0, 0)
					AddGearToPlayer(0, GetLocalString("Item Names", "key_4"), "key4", 0, 0)
					;[End Block]
				Case 5
					;[Block]
					ecst\WasInLCZCore = True
					ecst\WasInRoom2_SL = True
					ecst\UnlockedAirlock = True
					
					ecst\EzDoorOpened = True
					ecst\WasInHCZ = True
					ecst\NTFArrived = True
					ecst\UnlockedHDS = True
					ecst\WasInReactor = True
					
					ecst\WasInO5 = True
					;[End Block]
					
					;[Block]
					If (Not TaskExists(TASK_FIND_AREA_076)) Then
						BeginTask(TASK_FIND_AREA_076)
					EndIf
					If (Not TaskExists(TASK_LAUNCH_ROCKET)) Then
						BeginTask(TASK_LAUNCH_ROCKET)
					EndIf
					If (Not TaskExists(TASK_GET_TOPSIDE)) Then
						BeginTask(TASK_GET_TOPSIDE)
					EndIf
					;[End Block]
					
					;[Block]
					AddGearToPlayer(SLOT_HOLSTER, "FN Five-Seven", "fiveseven", 20, 60)
					AddGearToPlayer(SLOT_PRIMARY, "FN P90", "p90", 50, 150)
					AddGearToPlayer(SLOT_SECONDARY, "Franchi SPAS-12", "spas12", 6, 42)
					AddGearToPlayer(SLOT_SCABBARD, GetLocalString("Item Names", "crowbar"), "crowbar", 0, 0)
					AddGearToPlayer(0, GetLocalString("Item Names", "key_5"), "key5", 0, 0)
					;[End Block]
				Case 6
					;[Block]
					ecst\WasInLCZCore = True
					ecst\WasInRoom2_SL = True
					ecst\UnlockedAirlock = True
					
					ecst\EzDoorOpened = True
					ecst\WasInHCZ = True
					ecst\NTFArrived = True
					ecst\UnlockedHDS = True
					ecst\WasInReactor = True
					
					ecst\WasInO5 = True
					;[End Block]
					
					;[Block]
					If (Not TaskExists(TASK_FIND_KEY_IN_076)) Then
						BeginTask(TASK_FIND_KEY_IN_076)
					EndIf
					If (Not TaskExists(TASK_LAUNCH_ROCKET)) Then
						BeginTask(TASK_LAUNCH_ROCKET)
					EndIf
					If (Not TaskExists(TASK_GET_TOPSIDE)) Then
						BeginTask(TASK_GET_TOPSIDE)
					EndIf
					;[End Block]
					
					;[Block]
					AddGearToPlayer(SLOT_HOLSTER, "FN Five-Seven", "fiveseven", 20, 60)
					AddGearToPlayer(SLOT_PRIMARY, "FN P90", "p90", 50, 150)
					AddGearToPlayer(SLOT_SECONDARY, "Franchi SPAS-12", "spas12", 6, 42)
					AddGearToPlayer(SLOT_SCABBARD, GetLocalString("Item Names", "crowbar"), "crowbar", 0, 0)
					AddGearToPlayer(0, GetLocalString("Item Names", "key_5"), "key5", 0, 0)
					;[End Block]
				Case 7
					;[Block]
					ecst\WasInLCZCore = True
					ecst\WasInRoom2_SL = True
					ecst\UnlockedAirlock = True
					
					ecst\EzDoorOpened = True
					ecst\WasInHCZ = True
					ecst\NTFArrived = True
					ecst\UnlockedHDS = True
					ecst\WasInReactor = True
					
					ecst\WasInO5 = True
					
					ecst\WasIn076 = True
					ecst\NewCavesEvent = True
					;[End Block]
					
					;[Block]
					If (Not TaskExists(TASK_FIND_CAVES)) Then
						BeginTask(TASK_FIND_CAVES)
					EndIf
					If (Not TaskExists(TASK_LAUNCH_ROCKET)) Then
						BeginTask(TASK_LAUNCH_ROCKET)
					EndIf
					If (Not TaskExists(TASK_GET_TOPSIDE)) Then
						BeginTask(TASK_GET_TOPSIDE)
					EndIf
					;[End Block]
					
					;[Block]
					AddGearToPlayer(SLOT_HOLSTER, "FN Five-Seven", "fiveseven", 20, 60)
					AddGearToPlayer(SLOT_PRIMARY, "Colt M4A1", "m4a1", 30, 90)
					AddGearToPlayer(SLOT_SECONDARY, "Franchi SPAS-12", "spas12", 6, 42)
					AddGearToPlayer(SLOT_SCABBARD, GetLocalString("Item Names", "crowbar"), "crowbar", 0, 0)
					AddGearToPlayer(0, GetLocalString("Item Names", "key_5"), "key5", 0, 0)
					AddGearToPlayer(1, "FN P90", "p90", 50, 150)
					;[End Block]
				Case 8
					;[Block]
					ecst\WasInLCZCore = True
					ecst\WasInRoom2_SL = True
					ecst\UnlockedAirlock = True
					
					ecst\EzDoorOpened = True
					ecst\WasInHCZ = True
					ecst\NTFArrived = True
					ecst\UnlockedHDS = True
					ecst\WasInReactor = True
					
					ecst\WasInO5 = True
					
					ecst\WasIn076 = True
					ecst\NewCavesEvent = True
					
					IsStartingFromMenu = False
					;[End Block]
					
					;[Block]
					If (Not TaskExists(TASK_GO_TO_BCZ)) Then
						BeginTask(TASK_GO_TO_BCZ)
					EndIf
					If (Not TaskExists(TASK_LAUNCH_ROCKET)) Then
						BeginTask(TASK_LAUNCH_ROCKET)
					EndIf
					If (Not TaskExists(TASK_GET_TOPSIDE)) Then
						BeginTask(TASK_GET_TOPSIDE)
					EndIf
					;[End Block]
					
					;[Block]
					AddGearToPlayer(SLOT_HOLSTER, "FN Five-Seven", "fiveseven", 20, 60)
					AddGearToPlayer(SLOT_PRIMARY, "Colt M4A1", "m4a1", 30, 90)
					AddGearToPlayer(SLOT_SECONDARY, "Franchi SPAS-12", "spas12", 6, 42)
					AddGearToPlayer(SLOT_SCABBARD, GetLocalString("Item Names", "crowbar"), "crowbar", 0, 0)
					AddGearToPlayer(0, GetLocalString("Item Names", "key_5"), "key5", 0, 0)
					AddGearToPlayer(1, "FN P90", "p90", 50, 150)
					;[End Block]
				Case 9
					;[Block]
					ecst\WasInLCZCore = True
					ecst\WasInRoom2_SL = True
					ecst\UnlockedAirlock = True
					
					ecst\EzDoorOpened = True
					ecst\WasInHCZ = True
					ecst\NTFArrived = True
					ecst\UnlockedHDS = True
					ecst\WasInReactor = True
					
					ecst\WasInO5 = True
					
					ecst\WasIn076 = True
					ecst\NewCavesEvent = True
					;[End Block]
					
					;[Block]
					If TaskExists(TASK_GET_TOPSIDE) Then
						EndTask(TASK_GET_TOPSIDE)
					EndIf
					If gopt\CurrZone <> GATE_C_TOPSIDE Then
						If TaskExists(TASK_LAUNCH_ROCKET) Then
							CancelTask(TASK_LAUNCH_ROCKET)
						EndIf
					Else
						If (Not TaskExists(TASK_LAUNCH_ROCKET)) Then
							BeginTask(TASK_LAUNCH_ROCKET)
						EndIf
					EndIf
					;[End Block]
					
					;[Block]
					AddGearToPlayer(SLOT_HOLSTER, "FN Five-Seven", "fiveseven", 20, 60)
					AddGearToPlayer(SLOT_PRIMARY, "Colt M4A1", "m4a1", 30, 90)
					AddGearToPlayer(SLOT_SECONDARY, "Franchi SPAS-12", "spas12", 6, 42)
					AddGearToPlayer(SLOT_SCABBARD, GetLocalString("Item Names", "crowbar"), "crowbar", 0, 0)
					AddGearToPlayer(0, GetLocalString("Item Names", "key_5"), "key5", 0, 0)
					AddGearToPlayer(1, "FN P90", "p90", 50, 150)
					;[End Block]
			End Select
			
		Case GAMEMODE_NTF
			
			Select cpt\NTFCurrent
				Case 0
					;[Block]
					For g = Each Guns
						For i = 0 To MaxAttachments - 1
							g\HasPickedAttachments[i] = True
						Next
						Select randm
							Case 0, 1, 2
								AddGearToPlayer(SLOT_PRIMARY, "FN P90", "p90", 50, 200)
								AddGearToPlayer(SLOT_HOLSTER, "FN Five-Seven", "fiveseven", 20, 60)
								
								g_I\Weapon_InSlot[QUICKSLOT_PRIMARY] = "p90"
								g_I\Weapon_InSlot[QUICKSLOT_HOLSTER] = "fiveseven"
								
								g_I\HoldingGun = GUN_P90
							Case 3, 4, 5
								AddGearToPlayer(SLOT_PRIMARY, "H&K MP5", "mp5", 30, 210)
								AddGearToPlayer(SLOT_HOLSTER, "M9 Beretta", "beretta", 15, 45)
								
								g_I\Weapon_InSlot[QUICKSLOT_PRIMARY] = "mp5"
								g_I\Weapon_InSlot[QUICKSLOT_HOLSTER] = "beretta"
								
								g_I\HoldingGun = GUN_MP5
							Case 6
								AddGearToPlayer(SLOT_PRIMARY, "H&K MP7", "mp7", 40, 280)
								AddGearToPlayer(SLOT_HOLSTER, "H&K USP", "usp", 12, 36)
								
								g_I\Weapon_InSlot[QUICKSLOT_PRIMARY] = "mp7"
								g_I\Weapon_InSlot[QUICKSLOT_HOLSTER] = "usp"
								
								g_I\HoldingGun = GUN_MP7
							Case 7, 8
								AddGearToPlayer(SLOT_PRIMARY, "Franchi SPAS-12", "spas12", 6, 42)
								AddGearToPlayer(SLOT_HOLSTER, "H&K USP", "usp", 12, 36)
								AddGearToPlayer(3, GetLocalString("Item Names", "grenade_m67"), "m67", 0, 0)
								
								g_I\Weapon_InSlot[QUICKSLOT_PRIMARY] = "spas12"
								g_I\Weapon_InSlot[QUICKSLOT_HOLSTER] = "usp"
								
								g_I\HoldingGun = GUN_SPAS12
							Case 9
								AddGearToPlayer(SLOT_PRIMARY, "Remington M870", "m870", 8, 64)
								AddGearToPlayer(SLOT_HOLSTER, "H&K USP", "usp", 12, 36)
								AddGearToPlayer(3, GetLocalString("Item Names", "grenade_m67"), "m67", 0, 0)
								
								g_I\Weapon_InSlot[QUICKSLOT_PRIMARY] = "m870"
								g_I\Weapon_InSlot[QUICKSLOT_HOLSTER] = "usp"
								
								g_I\HoldingGun = GUN_M870
							Case 10, 11, 12
								AddGearToPlayer(SLOT_PRIMARY, "Colt M4A1", "m4a1", 30, 210)
								AddGearToPlayer(SLOT_HOLSTER, "FN Five-Seven", "fiveseven", 20, 60)
								
								g_I\Weapon_InSlot[QUICKSLOT_PRIMARY] = "m4a1"
								g_I\Weapon_InSlot[QUICKSLOT_HOLSTER] = "fiveseven"
								
								g_I\HoldingGun = GUN_M4A1
						End Select
					Next
					
					AddGearToPlayer(SLOT_SCABBARD, GetLocalString("Item Names", "knife"), "knife", 0, 0)
					AddGearToPlayer(SLOT_HEAD, GetLocalString("Item Names", "ntf_helmet"), "ntf_helmet", 150, 0)
					AddGearToPlayer(SLOT_TORSO, GetLocalString("Item Names", "vest"), "vest", 150, 0)
					AddGearToPlayer(SLOT_BACKPACK, GetLocalString("Item Names", "backpack"), "backpack", 0, 0)
					AddGearToPlayer(0, GetLocalString("Item Names", "key_5"), "key5", 0, 0)
					AddGearToPlayer(1, GetLocalString("Item Names", "radio"), "radio", 1000, 0)
					AddGearToPlayer(2, GetLocalString("Item Names", "nav_310"), "nav", 1000, 0)
					
					psp\Helmet = 150
					psp\Kevlar = 150
					
					g_I\Weapon_InSlot[QUICKSLOT_SCABBARD] = "knife"
					g_I\Weapon_CurrSlot% = QUICKSLOT_PRIMARY+1
					
					;[End Block]
				Case 1
					;[Block]
					If (Not opt\IntroEnabled) Then
						
						For g = Each Guns
							For i = 0 To MaxAttachments - 1
								g\HasPickedAttachments[i] = True
							Next
							Select randm
								Case 0, 1, 2
									AddGearToPlayer(SLOT_PRIMARY, "FN P90", "p90", 50, 200)
									AddGearToPlayer(SLOT_HOLSTER, "FN Five-Seven", "fiveseven", 20, 60)
									
									g_I\Weapon_InSlot[QUICKSLOT_PRIMARY] = "p90"
									g_I\Weapon_InSlot[QUICKSLOT_HOLSTER] = "fiveseven"
									
									g_I\HoldingGun = GUN_P90
								Case 3, 4, 5
									AddGearToPlayer(SLOT_PRIMARY, "H&K MP5", "mp5", 30, 210)
									AddGearToPlayer(SLOT_HOLSTER, "M9 Beretta", "beretta", 15, 45)
									
									g_I\Weapon_InSlot[QUICKSLOT_PRIMARY] = "mp5"
									g_I\Weapon_InSlot[QUICKSLOT_HOLSTER] = "beretta"
									
									g_I\HoldingGun = GUN_MP5
								Case 6
									AddGearToPlayer(SLOT_PRIMARY, "H&K MP7", "mp7", 40, 280)
									AddGearToPlayer(SLOT_HOLSTER, "H&K USP", "usp", 12, 36)
									
									g_I\Weapon_InSlot[QUICKSLOT_PRIMARY] = "mp7"
									g_I\Weapon_InSlot[QUICKSLOT_HOLSTER] = "usp"
									
									g_I\HoldingGun = GUN_MP7
								Case 7, 8
									AddGearToPlayer(SLOT_PRIMARY, "Franchi SPAS-12", "spas12", 6, 42)
									AddGearToPlayer(SLOT_HOLSTER, "H&K USP", "usp", 12, 36)
									AddGearToPlayer(3, GetLocalString("Item Names", "grenade_m67"), "m67", 0, 0)
									
									g_I\Weapon_InSlot[QUICKSLOT_PRIMARY] = "spas12"
									g_I\Weapon_InSlot[QUICKSLOT_HOLSTER] = "usp"
									
									g_I\HoldingGun = GUN_SPAS12
								Case 9
									AddGearToPlayer(SLOT_PRIMARY, "Remington M870", "m870", 8, 64)
									AddGearToPlayer(SLOT_HOLSTER, "H&K USP", "usp", 12, 36)
									AddGearToPlayer(3, GetLocalString("Item Names", "grenade_m67"), "m67", 0, 0)
									
									g_I\Weapon_InSlot[QUICKSLOT_PRIMARY] = "m870"
									g_I\Weapon_InSlot[QUICKSLOT_HOLSTER] = "usp"
									
									g_I\HoldingGun = GUN_M870
								Case 10, 11, 12
									AddGearToPlayer(SLOT_PRIMARY, "Colt M4A1", "m4a1", 30, 210)
									AddGearToPlayer(SLOT_HOLSTER, "FN Five-Seven", "fiveseven", 20, 60)
									
									g_I\Weapon_InSlot[QUICKSLOT_PRIMARY] = "m4a1"
									g_I\Weapon_InSlot[QUICKSLOT_HOLSTER] = "fiveseven"
									
									g_I\HoldingGun = GUN_M4A1
							End Select
						Next
						
						AddGearToPlayer(SLOT_SCABBARD, GetLocalString("Item Names", "knife"), "knife", 0, 0)
						AddGearToPlayer(SLOT_HEAD, GetLocalString("Item Names", "ntf_helmet"), "ntf_helmet", 150, 0)
						AddGearToPlayer(SLOT_TORSO, GetLocalString("Item Names", "vest"), "vest", 150, 0)
						AddGearToPlayer(SLOT_BACKPACK, GetLocalString("Item Names", "backpack"), "backpack", 0, 0)
						AddGearToPlayer(0, GetLocalString("Item Names", "key_5"), "key5", 0, 0)
						AddGearToPlayer(1, GetLocalString("Item Names", "radio"), "radio", 1000, 0)
						AddGearToPlayer(2, GetLocalString("Item Names", "nav_310"), "nav", 1000, 0)
						
						psp\Helmet = 150
						psp\Kevlar = 150
						
						g_I\Weapon_InSlot[QUICKSLOT_SCABBARD] = "knife"
						g_I\Weapon_CurrSlot% = QUICKSLOT_PRIMARY+1
						
					EndIf
					;[End Block]
				Case 2
					;[Block]
					psp\Checkpoint106Passed = True
					WasInPD = True
					;[End Block]
					
					;[Block]
					If (Not TaskExists(TASK_NTF_GO_TO_ZONE)) Then
						BeginTask(TASK_NTF_GO_TO_ZONE)
					EndIf
					;[End Block]
			End Select
			
		Case GAMEMODE_CLASS_D
			
			Select cpt\DCurrent
				Case 1
					;[Block]
					AddGearToPlayer(0, GetLocalString("Item Names", "key_class_d"), "key_class_d", 0, 0)
					;[End Block]
			End Select
			
	End Select
	
	CatchErrors("Uncaught : InitChapters")
End Function

Function InitNewGame()
	CatchErrors("InitNewGame")
	Local i%, de.Decals, d.Doors, it.Items, r.Rooms, sc.SecurityCams, e.Events, g.Guns
	
	IsStartingFromMenu = False
	
	DrawLoading(45,False,"","Creating_new_game")
	
	HideDistance# = 15.0
	
	HeartBeatRate = 70
	
	MaxItemAmount = 16
	
	I_005\ChanceToSpawn = Rand(3)
	
	ecst\ChanceToSpawnWolfNote = Rand(5)
	
	psp\IsShowingHUD = True
	
	CanPlayerUseGuns = True
	
	SaveChaptersValueFile()
	
	InitChapters()
	
	Local strtemp$ = ""
	For i = 1 To Len(RandomSeed)
		strtemp = strtemp+Asc(Mid(RandomSeed,i,1))
	Next
	SeedRnd Abs(Int(strtemp))
	
	For i = 0 To 3 
		AccessCode[i] = 0
		AccessCode[i] = Rand(1000,9999)
	Next
	
	CreateMap()
		
	For r.Rooms = Each Rooms
		If r\RoomTemplate\Name = "room2_maintenance" Then
			ShowEntity r\Objects[0]
			ShowEntity r\Objects[5]
		EndIf
	Next
	InitWayPoints()
	ShowEntity Collider
	For r.Rooms = Each Rooms
		If r <> PlayerRoom
			If r\RoomTemplate\Name = "room2_maintenance" Then
				HideEntity r\Objects[0]
				HideEntity r\Objects[5]
			EndIf
		EndIf
	Next
	
	DrawLoading(79,False,"","NPCs")
	
	Curr173 = CreateNPC(NPC_SCP_173, 0, -30.0, 0)
	Curr106 = CreateNPC(NPC_SCP_106, 0, -30.0, 0)
	Curr106\State[0] = 70 * 60 * Rand(12,17)
	DrawLoading(80,False,"","Zones")
	For d.Doors = Each Doors
		EntityParent(d\obj, 0)
		If d\obj2 <> 0 Then EntityParent(d\obj2, 0)
		If d\frameobj <> 0 Then EntityParent(d\frameobj, 0)
		If d\buttons[0] <> 0 Then EntityParent(d\buttons[0], 0)
		If d\buttons[1] <> 0 Then EntityParent(d\buttons[1], 0)
		
		If d\obj2 <> 0 And d\dir = 0 Then
			MoveEntity(d\obj, 0, 0, 8.0 * RoomScale)
			MoveEntity(d\obj2, 0, 0, 8.0 * RoomScale)
		EndIf	
	Next
	
	For it.Items = Each Items
		EntityType (it\collider, HIT_ITEM)
		EntityParent(it\collider, 0)
	Next
	
	DrawLoading(85,False,"","Zones")
	
	For sc.SecurityCams= Each SecurityCams
		sc\angle = EntityYaw(sc\obj) + sc\angle
		EntityParent(sc\obj, 0)
	Next	
	
	For r.Rooms = Each Rooms
		For i = 0 To MaxRoomLights-1
			If r\Lights[i]<>0 Then EntityParent(r\Lights[i],0)
		Next
		
		If (Not r\RoomTemplate\DisableDecals) Then
			If Rand(4) = 1 Then
				de.Decals = CreateDecal(GetRandomDecalID(DECAL_TYPE_BLOODSPLAT), EntityX(r\obj)+Rnd(- 2,2), 0.003, EntityZ(r\obj)+Rnd(-2,2), 90, Rand(360), 0)
				de\Size = Rnd(0.1, 0.4) : ScaleSprite(de\obj, de\Size, de\Size)
				EntityAlpha(de\obj, Rnd(0.85, 0.95))
			EndIf
			
			If Rand(4) = 1 Then
				de.Decals = CreateDecal(DECAL_DECAY, EntityX(r\obj)+Rnd(- 2,2), 0.003, EntityZ(r\obj)+Rnd(-2,2), 90, Rand(360), 0)
				de\Size = Rnd(0.5, 0.7) : EntityAlpha(de\obj, 0.7) : de\ID = 1 : ScaleSprite(de\obj, de\Size, de\Size)
				EntityAlpha(de\obj, Rnd(0.7, 0.85))
			EndIf
		EndIf
		
		DrawLoading(-1,False,"","Zones")
		
		Select gopt\CurrZone
			Case GATE_A_INTRO
				If r\RoomTemplate\Name = "gate_a_intro"
					PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
					PlayerRoom = r
				EndIf
			Case GATE_A_ROAD
				If r\RoomTemplate\Name = "gate_a_road"
					PositionEntity (Collider, EntityX(r\obj)-609*RoomScale, EntityY(r\obj)+288*RoomScale, EntityZ(r\obj)-1198*RoomScale)
					PlayerRoom = r
				EndIf
			Case GATE_A_TOPSIDE
				If r\RoomTemplate\Name = "gate_a_topside"
					PositionEntity (Collider, EntityX(r\obj)+1847*RoomScale, 0.5, EntityZ(r\obj)+728*RoomScale)
					RotateEntity (Collider,0,r\angle+270,0)
					PlayerRoom = r
				EndIf
			Case GATE_B_TOPSIDE
				If r\RoomTemplate\Name = "gate_b_topside"
					PositionEntity (Collider, EntityX(r\obj), 0.5, EntityZ(r\obj))
					PlayerRoom = r
				EndIf
			Case GATE_C_TOPSIDE
				If r\RoomTemplate\Name = "gate_c_topside"
					PositionEntity (Collider, EntityX(r\obj), 0.5, EntityZ(r\obj))
					PlayerRoom = r
				EndIf
			Case GATE_D_TOPSIDE
				If r\RoomTemplate\Name = "gate_d_topside"
					If gopt\GameMode = GAMEMODE_DEFAULT And opt\IntroEnabled Then
						PositionEntity (Collider, EntityX(r\obj)-3697*RoomScale, EntityY(r\obj)+240*RoomScale, EntityZ(r\obj)+928*RoomScale)
						RotateEntity (Collider,0,r\angle-90,0)
						PlayerRoom = r
					Else
						PositionEntity (Collider, EntityX(r\obj)+6705*RoomScale, EntityY(r\obj)+240*RoomScale, EntityZ(r\obj)-2491*RoomScale)
						RotateEntity (Collider,0,r\angle+180,0)
						PlayerRoom = r
					EndIf
				EndIf
			Case EZ
				If gopt\GameMode = GAMEMODE_NTF Then
					If cpt\NTFCurrent = 1 Then
						If r\RoomTemplate\Name = "gate_a_entrance"
							PositionEntity (Collider, EntityX(r\obj), 0.5, EntityZ(r\obj)-1450.0*RoomScale)
							RotateEntity (Collider,0,r\angle+180,0)
							PlayerRoom = r
						EndIf
					EndIf
				Else
					If cpt\Current > 8 Then
						If r\RoomTemplate\Name = "gate_d_entrance"
							PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
							RotateEntity (Collider,0,r\angle,0)
							PlayerRoom = r
						EndIf
					Else
						If r\RoomTemplate\Name = "core_ez"
							PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
							RotateEntity (Collider,0,r\angle,0)
							PlayerRoom = r
						EndIf
					EndIf
				EndIf
			Case BCZ
				If r\RoomTemplate\Name = "checkpoint_bcz"
					PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
					RotateEntity (Collider,0,r\angle,0)
					PlayerRoom = r
				EndIf
			Case LCZ
				If gopt\GameMode = GAMEMODE_DEFAULT Then
					If opt\IntroEnabled = True Then
						If r\RoomTemplate\Name = "room1_intro"
							;PositionEntity (Collider, EntityX(r\obj)+735*RoomScale, EntityY(r\obj)+2770*RoomScale, EntityZ(r\obj)+7718*RoomScale)
							PositionEntity (Collider, EntityX(r\obj)+5298*RoomScale, EntityY(r\obj)+1900*RoomScale, EntityZ(r\obj)+1227*RoomScale)
							RotateEntity (Collider,0,r\angle-90,0)
							PlayerRoom = r
						EndIf
					Else
						;If IsStartingFromMenu Then
							If cpt\Current = 1 Lor cpt\Current = 7 Then
								If r\RoomTemplate\Name = "room1_start"
									PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
									RotateEntity (Collider,0,r\angle,0)
									PlayerRoom = r
								EndIf
							Else
								If r\RoomTemplate\Name = "core_lcz"
									PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
									RotateEntity (Collider,0,r\angle,0)
									PlayerRoom = r
								EndIf
							EndIf
;						Else
;							If r\RoomTemplate\Name = "core_lcz"
;								PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
;								RotateEntity (Collider,0,r\angle,0)
;								PlayerRoom = r
;							EndIf
;						EndIf
					EndIf
				ElseIf gopt\GameMode = GAMEMODE_CLASS_D Then
					If (Not opt\IntroEnabled) Then
						If r\RoomTemplate\Name = "class_d_cells_entrance"
							PositionEntity (Collider, EntityX(r\obj),1.0, EntityZ(r\obj))
							RotateEntity (Collider,0,r\angle,0)
							PlayerRoom = r
						EndIf
					EndIf
				Else
					If r\RoomTemplate\Name = "core_lcz"
						PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
						RotateEntity (Collider,0,r\angle,0)
						PlayerRoom = r
					EndIf
				EndIf
			Case HCZ
				If gopt\GameMode <> GAMEMODE_NTF Then
					If cpt\Current = 3 Then
						If r\RoomTemplate\Name = "facility_reactor_entrance"
							PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
							RotateEntity (Collider,0,r\angle,0)
							PlayerRoom = r
						EndIf
					ElseIf cpt\Current = 5 Lor cpt\Current = 6 Lor cpt\Current = 8 Then
						If r\RoomTemplate\Name = "checkpoint_hcz"
							PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
							RotateEntity (Collider,0,r\angle,0)
							PlayerRoom = r
						EndIf
					Else
						If r\RoomTemplate\Name = "core_hcz"
							PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
							RotateEntity (Collider,0,r\angle,0)
							PlayerRoom = r
						EndIf
					EndIf
				Else
					If cpt\NTFCurrent = 2 Then
						If r\RoomTemplate\Name = "room1_sewers"
							PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
							RotateEntity (Collider,0,r\angle,0)
							PlayerRoom = r
						EndIf
					Else
						If r\RoomTemplate\Name = "core_hcz"
							PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
							RotateEntity (Collider,0,r\angle,0)
							PlayerRoom = r
						EndIf
					EndIf
				EndIf
			Case REACTOR_AREA
				If r\RoomTemplate\Name = "facility_reactor"
					PositionEntity (Collider, EntityX(r\obj)-989*RoomScale, 1.0, EntityZ(r\obj)-9736*RoomScale)
					PlayerRoom = r
				EndIf
			Case RCZ
				If cpt\Current > 5 Then
					If r\RoomTemplate\Name = "area_076_entrance"
						PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
						RotateEntity (Collider,0,r\angle,0)
						PlayerRoom = r
					EndIf
				Else
					If r\RoomTemplate\Name = "checkpoint_rcz"
						PositionEntity (Collider, EntityX(r\obj), 1.0, EntityZ(r\obj))
						RotateEntity (Collider,0,r\angle,0)
						PlayerRoom = r
					EndIf
				EndIf
			Case CLASSD_CELLS
				If gopt\GameMode = GAMEMODE_CLASS_D And opt\IntroEnabled Then
					If r\RoomTemplate\Name = "class_d_cells"
						PositionEntity (Collider, EntityX(r\obj)+14746*RoomScale, EntityY(r\obj)+4274*RoomScale, EntityZ(r\obj)-717*RoomScale)
						RotateEntity (Collider,0,r\angle-90,0)
						PlayerRoom = r
					EndIf
				Else
					If r\RoomTemplate\Name = "class_d_cells"
						PositionEntity (Collider, EntityX(r\obj)-1073*RoomScale, EntityY(r\obj)+4260*RoomScale, EntityZ(r\obj)+5041*RoomScale)
						RotateEntity (Collider,0,r\angle-90,0)
						PlayerRoom = r
					EndIf
				EndIf
			Case AREA_076
				If r\RoomTemplate\Name = "area_076"
					PositionEntity (Collider, EntityX(r\obj), 0.5, EntityZ(r\obj))
					PlayerRoom = r
				EndIf
			Case AREA_106_ESCAPE
				If r\RoomTemplate\Name = "area_106_escape"
					PositionEntity (Collider, EntityX(r\obj)-173*RoomScale, 0.5, EntityZ(r\obj)-17140*RoomScale)
					PlayerRoom = r
				EndIf
		End Select
	Next
	
	DrawLoading(90,False,"","Events")
	
	Local rt.RoomTemplates
	For rt.RoomTemplates = Each RoomTemplates
		rt\obj = FreeEntity_Strict(rt\obj)
	Next
	
	Delete Each TempWayPoints
	Delete Each TempScreens
	
	Local tfll.TempFluLight
	For tfll = Each TempFluLight
		Delete tfll\position
		Delete tfll\rotation
		Delete tfll
	Next
	
	ResetEntity Collider
	
	InitEvents()
	
	MoveMouse Viewport_Center_X,Viewport_Center_Y
	
	SetFont fo\Font[Font_Default]
	
	HidePointer()
	
	BlinkTimer = -10
	BlurTimer = 100
	Stamina = 100
	
	FPSfactor = 1.0
	
	DrawLoading(95,False,"","Player_movement")
	
	MovePlayer()
	
	DrawLoading(96,False,"","Updating_doors")
	
	UpdateDoors()
	
	DrawLoading(97,False,"","Updating_NPCs")
	
	UpdateNPCs()
	
	DrawLoading(98,False,"","Updating_world")
	
	UpdateWorld()
	
	DrawLoading(99,False,"","Updating_game")
	
	DeleteTextureEntriesFromCache(0)
	
	DrawLoading(100,False,"","Done")
	
	ResetInput()
	
	DropSpeed = 0
	
	CatchErrors("Uncaught InitNewGame")
End Function

Function InitLoadGame()
	CatchErrors("InitLoadGame")
	Local d.Doors, sc.SecurityCams, rt.RoomTemplates, e.Events
	
	IsStartingFromMenu = False
	
	DrawLoading(80,False,"","Loading_game")
	
	For d.Doors = Each Doors
		EntityParent(d\obj, 0)
		If d\obj2 <> 0 Then EntityParent(d\obj2, 0)
		If d\frameobj <> 0 Then EntityParent(d\frameobj, 0)
		If d\buttons[0] <> 0 Then EntityParent(d\buttons[0], 0)
		If d\buttons[1] <> 0 Then EntityParent(d\buttons[1], 0)
	Next
	
	For sc.SecurityCams = Each SecurityCams
		sc\angle = EntityYaw(sc\obj) + sc\angle
		EntityParent(sc\obj, 0)
	Next
	
	ResetEntity Collider
	
	DrawLoading(90,False,"","Updating_mouse")
	
	MoveMouse Viewport_Center_X,Viewport_Center_Y
	
	SetFont fo\Font[Font_Default]
	
	HidePointer()
	
	BlinkTimer = BLINKFREQ
	Stamina = 100
	
	Delete Each TempWayPoints
	Delete Each TempScreens
	
	Local tfll.TempFluLight
	For tfll = Each TempFluLight
		Delete tfll\position
		Delete tfll\rotation
		Delete tfll
	Next
	
	For rt.RoomTemplates = Each RoomTemplates
		rt\obj = FreeEntity_Strict(rt\obj)
	Next
	
	DropSpeed = 0.0
	
	;FreeTextureCache
	DeleteTextureEntriesFromCache(0)
	
	CatchErrors("Uncaught InitLoadGame")
	
	DrawLoading(100,False,"","Done")
	
	PrevTime = MilliSecs()
	FPSfactor = 0
	ResetInput()
	
End Function

Function NullGame(nomenuload%=False,playbuttonsfx%=True)
	CatchErrors("Uncaught (NullGame)")
	
	Local i%, x%, y%, lvl
	Local itt.ItemTemplates, s.Screens
	Local rt.RoomTemplates
	Local PlayerRoomName$ = PlayerRoom\RoomTemplate\Name
	Local PlayerRoomZone = gopt\CurrZone
	Local n.NPCs
	
	KillSounds()
	
	If playbuttonsfx Then PlaySound_Strict ButtonSFX[0]
	
	DeleteNewElevators()
	
	DeleteTextureEntriesFromCache(2)
	
	DestroySPPlayer()
	
	SaveChaptersValueFile()
	
	DeInitSubtitlesAssets()
	
	If Sky <> 0 Then Sky = FreeEntity_Strict(Sky)
	
	UnableToMove% = False
	
	QuickLoadPercent = -1
	QuickLoadPercent_DisplayTimer# = 0
	QuickLoad_CurrEvent = Null
	
	m_msg\DeathTxt$=""
	
	UsedConsole = False
	
	RoomTempID = 0
	
	GameSaved = 0
	
	HideDistance# = 15.0
	
	For lvl = 0 To 0
		For x = 0 To MapWidth - 1
			For y = 0 To MapHeight - 1
				MapTemp[x * MapWidth + y] = 0
				MapFound[x * MapWidth + y] = 0
			Next
		Next
	Next
	For itt.ItemTemplates = Each ItemTemplates
		itt\found = False
	Next
	
	DropSpeed = 0
	Shake = 0
	CurrSpeed = 0
	DeathTimer=0
	HeartBeatVolume = 0
	StaminaEffect = 1.0
	StaminaEffectTimer = 0
	BlinkEffect = 1.0
	BlinkEffectTimer = 0
	SelectedEnding = ""
	EndingTimer = 0
	ExplosionTimer = 0
	CameraShake = 0
	BigCameraShake = 0
	Shake = 0
	LightFlash = 0
	GodMode = 0
	NoClip = 0
	WireframeState = 0
	WireFrame 0
	CameraFogFar = StoredCameraFogFar
	If mpl\NightVisionEnabled Then
		CameraFogFar = StoredCameraFogFar
		mpl\NightVisionEnabled = 0
	EndIf
	ForceMove = 0.0
	ForceAngle = 0.0	
	Playable = True
	
	cpt\Current = 0
	cpt\NTFCurrent = 0
	cpt\DCurrent = 0
	
	ecst\ChanceToSpawnWolfNote = 0
	
	I_005\ChanceToSpawn = 0
	
	I_008\Timer = 0
	
	I_016\Timer = 0
	
	I_035\Possessed = 0
	
	;I_059\Timer = 0
	;I_059\Using = False
	
	I_109\Used = 0
	I_109\Timer = 0
	
	I_127\RestoreTimer = 0
	
	I_198\Timer = 0
	I_198\DeathTimer = 0
	I_198\Vomit = 0
	I_198\VomitTimer = 0
	I_198\Injuries = 0
	
	I_207\Timer = 0
	I_207\DeathTimer = 0
	I_207\Factor = 0
	I_207\Limit = 0
	
	I_268\Using = 0
    I_268\Timer = 0
	
	I_330\Taken = 0
	I_330\Timer = 0
	
	;I_357\Timer = 0
	;I_357\Using = False
	
	;I_402\Timer = 0
	;I_402\Using = 0
	
	I_500\Limit = 0
	
	I_714\Using = 0
	
;	For i = 0 To 5
;		I_1025\State[i] = 0
;	Next
	
	I_1033RU\Using = 0
	I_1033RU\HP = 0
	I_1033RU\DHP = 0
	
	;I_1079\Foam = 0
	;I_1079\Trigger = 0
	;I_1079\Limit = 0
	;I_1079\Take = 0
	
	;I_1102RU\IsInside = False
	;I_1102RU\State = 0
	
	ecst\FusesAmount = 0
	
	If Curr173 <> Null Then Curr173\Idle = False
	
	MTFtimer = 0
	CITimer = 0
	For i = 0 To 9
		MTFrooms[i]=Null
		MTFroomState[i]=0
	Next
	
	For s.Screens = Each Screens
		If s\img <> 0 Then FreeImage s\img
		Delete s
	Next
	
	RefinedItems = 0
	ConsoleInput = ""
	ConsoleOpen = False
	EyeIrritation = 0
	EyeStuck = 0
	ShouldPlay = 0
	KillTimer = 0
	FallTimer = 0
	Stamina = 100
	BlurTimer = 0
	SuperMan = False
	SuperManTimer = 0
	InfiniteStamina% = False
	m_msg\Txt = ""
	m_msg\Timer = 0
	SelectedItem = Null
	
	For i = 0 To MaxItemAmount - 1
		Inventory[i] = Null
	Next
	
	SelectedItem = Null
	
	Delete Each Doors
	Delete Each LightTemplates
	Delete Each Materials
	Delete Each WayPoints
	Delete Each TempWayPoints	
	Delete Each Rooms
	Delete Each ItemTemplates
	Delete Each Items
	Delete Each Props
	Delete Each Decals
	Delete Each NPCs
	Delete Each NPCGun
	Delete Each NPCAnim
	Delete Each TempFluLight
	Delete Each Lever
	Delete Each MenuLogo
	Delete Each TempScreens
	Delete Each Water
	Delete Each FuseBox
	Delete Each Generator
	Delete Each SoundEmittor
	Delete Each ButtonGen
	Delete Each LeverGen
	Delete Each ParticleGen
	Delete Each DamageBossRadius
	Delete Each TextureInCache
	Delete Each SplashText
	
	Delete wbl
	
	Delete ecst
	
	Delete I_005
	Delete I_008
	Delete I_016
	;Delete I_059
	Delete I_109
	Delete I_127
	Delete I_198
	Delete I_207
	Delete I_268
	Delete I_330
	;Delete I_357
	;Delete I_402
	Delete I_427
	Delete I_500
	Delete I_714
	;Delete I_1025
	Delete I_1033RU
	;Delete I_1079
	;Delete I_1102RU
	
	Curr173 = Null
	Curr106 = Null
	Curr096 = Null
	For i = 0 To 6
		MTFrooms[i]=Null
	Next
	ForestNPC = 0
	ForestNPCTex = 0
	
	Local e.Events
	
	For e.Events = Each Events
		For i = 0 To 1
			If e\Sound[i]<>0 Then FreeSound_Strict e\Sound[i]
		Next
		Delete e
	Next
	
	Delete Each SecurityCams
	Delete Each Emitters
	Delete Each Particles
	
	For rt.RoomTemplates = Each RoomTemplates
		rt\obj = 0
	Next
	
	For i = 0 To 5
		If ChannelPlaying(RadioCHN[i]) Then StopChannel(RadioCHN[i])
	Next
	
	Delete Each ConsoleMsg
	
	Delete Each NewTask
	
	DeleteElevatorObjects()
	
	OptionsMenu% = -1
	QuitMSG% = -1
	AchievementsMenu% = -1
	DeafPlayer% = False
	DeafTimer# = 0.0
	IsZombie% = False
	
	DeleteModStuff()
	
	If gopt\GameMode = GAMEMODE_UNKNOWN
		DeleteMission()
	EndIf
	
	Delete Each AchievementMsg
	CurrAchvMSGID = 0
	
	Delete Each DoorInstance
	Delete Each GunInstance
	Delete Each Grenades
	
	DeleteVectors2D()
	DeleteVectors3D()
	
	Delete Each FluLight
	
	ClearWorld
	
	ResetTimingAccumulator()
	
	Camera = 0
	ark_blur_cam = 0
	m_I\Cam = 0
	
	InitFastResize()
	
	If (Not nomenuload)
		Local entry$
		If PlayerRoomZone = LCZ
			If PlayerRoomName = "room1_intro" Then
				entry = "intro"
			ElseIf PlayerRoomName = "room1_start" Then
				entry = "beginning"
			Else
				entry = "lcz"
			EndIf
		ElseIf PlayerRoomZone = HCZ
			entry = "hcz"
		ElseIf PlayerRoomZone = EZ
			entry = "ez"
		ElseIf PlayerRoomZone = RCZ
			entry = "rcz"
		ElseIf PlayerRoomZone = BCZ
			entry = "bcz"
		ElseIf PlayerRoomZone = AREA_076
			entry = "area_076"
		ElseIf PlayerRoomZone = CLASSD_CELLS
			entry = "class_d_cells"
		ElseIf PlayerRoomZone = GATE_A_TOPSIDE Lor PlayerRoomZone = GATE_B_TOPSIDE Lor PlayerRoomZone = GATE_C_TOPSIDE Lor PlayerRoomZone = GATE_D_TOPSIDE Then
			entry = "surface"
		ElseIf PlayerRoomZone = GATE_A_INTRO Then
			entry = "ntf_intro"
		ElseIf PlayerRoomZone = REACTOR_AREA
			entry = "reactor"
		EndIf
		If entry = ""
			If PlayerRoomName = "poketdimension"
				entry = "pd"
			Else
				entry = PlayerRoomName
			EndIf
		EndIf
		PutINIValue(gv\OptionFile,"options","progress",entry)
		InitConsole(2)
		Delete Each Menu3DInstance
		Load3DMenu(entry)
	EndIf
	
	DeleteMenuGadgets()
	
	CatchErrors("NullGame")
End Function

Global BatMsgTimer#

Function UpdateBatteryTimer%()
	
	BatMsgTimer = BatMsgTimer + FPSfactor
	If BatMsgTimer >= 70.0 * 1.5 Then BatMsgTimer = 0.0
	
End Function

Include "SourceCode\Save.bb"

;--------------------------------------- random -------------------------------------------------------

Function f2s$(n#, count%)
	Return Left(n, Len(Int(Str(n)))+count+1)
End Function

Function AnimateNPC(n.NPCs, start#, quit#, speed#, loop=True)
	Local newTime#
	
	If speed > 0.0 Then 
		If n\Frame > quit Then n\Frame = start
		newTime = Max(Min(n\Frame + speed * FPSfactor,quit),start)
		
		If loop And newTime => quit Then
			newTime = start
		EndIf
	Else
		If start < quit Then
			temp% = start
			start = quit
			quit = temp
		EndIf
		
		If loop Then
			newTime = n\Frame + speed * FPSfactor
			
			If newTime < quit Then 
				newTime = start
			Else If newTime > start 
				newTime = quit
			EndIf
		Else
			If n\Frame > start Then n\Frame = quit
			newTime = Max(Min(n\Frame + speed * FPSfactor,start),quit)
		EndIf
	EndIf
	SetNPCFrame(n, newTime)
	
End Function

Function SetNPCFrame(n.NPCs, frame#)
	If (Abs(n\Frame-frame)<0.001) Then Return
	
	SetAnimTime n\obj, frame
	
	n\Frame = frame
End Function

Function Animate2#(entity%, curr#, start%, quit%, speed#, loop=True)
	
	Local newTime#
	
	If speed > 0.0 Then 
		newTime = Max(Min(curr + speed * FPSfactor,quit),start)
		
		If loop Then
			If newTime => quit Then
				newTime = start
			Else
				
			EndIf
		Else
			
		EndIf
	Else
		If start < quit Then
			Local temp% = start
			start = quit
			quit = temp
		EndIf
		
		If loop Then
			newTime = curr + speed * FPSfactor
			
			If newTime < quit Then newTime = start
			If newTime > start Then newTime = quit
			
		Else
			newTime = Max(Min(curr + speed * FPSfactor,start),quit)
		EndIf
	EndIf
	
	SetAnimTime entity, newTime
	Return newTime
	
End Function

Global NTF_Group_Arrived% = False
Global CI_Group_Arrived% = False

Function UpdateMTF()
	
	If ecst\NTFArrived Then
		If PlayerRoom\RoomTemplate\Name = "gate_a_entrance" Then Return
		
		Local r.Rooms, n.NPCs
		Local dist#, i%
		Local entrance.Rooms = Null
		If (Not NTF_Group_Arrived%) Then
			For r.Rooms = Each Rooms
				If Lower(r\RoomTemplate\Name) = "gate_a_entrance" Then entrance = r : Exit
			Next
			
			If entrance <> Null Then 
				If PlayerRoom\RoomTemplate\Name = "core_ez" Then
					If (Not PlayerInNewElevator) Then
						PlayAnnouncement("SFX\Intercom\MTF\NTF\Announc_music.ogg")
						
						n.NPCs = CreateNPC(NPC_NTF, EntityX(entrance\obj)+0.3*(0),1.0,EntityZ(entrance\obj))
						
						n\State[0] = MTF_SPLIT_UP
						;n\State[0] = MTF_WANDERING
						n\PrevX = 0
						n\EnemyX = EntityX(n\Collider)
						n\EnemyY = 0.25
						n\EnemyZ = EntityZ(n\Collider)
						
						n.NPCs = CreateNPC(NPC_NTF, EntityX(entrance\obj)+0.3*(1),1.0,EntityZ(entrance\obj))
						n\PrevState = MTF_UNIT_MEDIC
						
						TextureBlend(NTFTexture[NTF_Texture_Medic],5)
						EntityTexture(n\obj, NTFTexture[NTF_Texture_Medic])
						DeleteSingleTextureEntryFromCache NTFTexture[NTF_Texture_Medic]
						
						n\State[0] = MTF_SPLIT_UP
						;n\State[0] = MTF_WANDERING
						n\PrevX = 1
						n\EnemyX = EntityX(n\Collider)
						n\EnemyY = 0.25
						n\EnemyZ = EntityZ(n\Collider)
						
						n.NPCs = CreateNPC(NPC_NTF, EntityX(entrance\obj)+0.3*(2),1.0,EntityZ(entrance\obj))
						n\PrevState = MTF_UNIT_COMMANDER
						
						TextureBlend(NTFTexture[NTF_Texture_Commander],5)
						EntityTexture(n\obj, NTFTexture[NTF_Texture_Commander])
						DeleteSingleTextureEntryFromCache NTFTexture[NTF_Texture_Commander]
						
						n\State[0] = MTF_SPLIT_UP
						;n\State[0] = MTF_WANDERING
						n\PrevX = 2
						n\EnemyX = EntityX(n\Collider)
						n\EnemyY = 0.25
						n\EnemyZ = EntityZ(n\Collider)
						
						MTFtimer = 1.0
						
						NTF_Group_Arrived% = True
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

Function UpdateCI%()
	Local r.Rooms, n.NPCs
	Local dist#, i%
	Local entrance.Rooms = Null
	
	If ecst\CIArrived Then
		
		If PlayerRoom\RoomTemplate\Name = "gate_b_entrance" Then Return
		
		For r.Rooms = Each Rooms
			If Lower(r\RoomTemplate\Name) = "gate_b_entrance" Then entrance = r : Exit
		Next
		
		If (Not CI_Group_Arrived%) Then
			
			If entrance <> Null Then
				If Abs(EntityZ(entrance\obj)-EntityZ(Collider))<30.0 Then
					
					PlayAnnouncement("SFX\Intercom\CI\Announc.ogg")
					
					CITimer = 1.0
					
					For i = 0 To 2
						n.NPCs = CreateNPC(NPC_CI, EntityX(entrance\obj)+0.3*(i-1),1.5,EntityZ(entrance\obj))
						
						n\PrevX = i
						n\EnemyX = EntityX(n\Collider)
						n\EnemyY = 0.25
						n\EnemyZ = EntityZ(n\Collider)
					Next
					
					CI_Group_Arrived% = True
				EndIf
			EndIf
			
		EndIf
		
		If CI_Group_Arrived Then
			CITimer = CITimer + FPSfactor
		EndIf
		
		If CITimer > 0 Then
			If CITimer > 70*120 And CITimer < 70*120.1
				If PlayerInReachableRoom()
					PlayAnnouncement("SFX\General\Scary_Announcement.ogg")
					For i = 0 To 2
						n.NPCs = CreateNPC(NPC_CI, EntityX(entrance\obj)+0.3*(i-1),1.5,EntityZ(entrance\obj))
						
						n\PrevX = i
						n\EnemyX = EntityX(n\Collider)
						n\EnemyY = 0.25
						n\EnemyZ = EntityZ(n\Collider)
					Next
				EndIf
			ElseIf CITimer > 70*640 And CITimer < 70*640.1
				If PlayerInReachableRoom()
					PlayAnnouncement("SFX\General\Scary_Announcement.ogg")
					For i = 0 To 2
						n.NPCs = CreateNPC(NPC_CI, EntityX(entrance\obj)+0.3*(i-1),1.5,EntityZ(entrance\obj))
						
						n\PrevX = i
						n\EnemyX = EntityX(n\Collider)
						n\EnemyY = 0.25
						n\EnemyZ = EntityZ(n\Collider)
					Next
				EndIf
			EndIf
		EndIf
		
	EndIf
	
End Function

Function UpdateIntercomSystem()
	
	If ecst\IntercomEnabled Then
		If (Not IsPlayerOutside()) Lor (Not ecst\IntercomIsReady) Then
			ecst\IntercomTimer = ecst\IntercomTimer + (FPSfactor/1.5)
		Else
			ecst\IntercomTimer = 0
		EndIf
	EndIf
	
	If ecst\IntercomTimer > 0 Then
		If ecst\IntercomTimer >= 70*190 And ecst\IntercomTimer < 70*190.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\hostiles_detected.ogg")
		ElseIf ecst\IntercomTimer >= 70*400 And ecst\IntercomTimer < 70*400.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\systems_report_status.ogg")
		ElseIf ecst\IntercomTimer >= 70*590 And ecst\IntercomTimer < 70*590.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\report_fuel_status.ogg")
		ElseIf ecst\IntercomTimer >= 70*800 And ecst\IntercomTimer < 70*800.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\check_fuel_status.ogg")
		ElseIf ecst\IntercomTimer >= 70*990 And ecst\IntercomTimer < 70*990.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\check_fuel_status_"+Rand(1,2)+".ogg")
		ElseIf ecst\IntercomTimer >= 70*1200 And ecst\IntercomTimer < 70*1200.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\fuel_engaged.ogg")
		ElseIf ecst\IntercomTimer >= 70*1390 And ecst\IntercomTimer < 70*1390.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\check_power_status.ogg")
		ElseIf ecst\IntercomTimer >= 70*1600 And ecst\IntercomTimer < 70*1600.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\report_power_status.ogg")
		ElseIf ecst\IntercomTimer >= 70*1790 And ecst\IntercomTimer < 70*1790.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\report_power_status_"+Rand(1,2)+".ogg")
		ElseIf ecst\IntercomTimer >= 70*2000 And ecst\IntercomTimer < 70*2000.1 Then
			PlayAnnouncement("SFX\Intercom\Rocket\power_engaged.ogg")
		EndIf
	EndIf
	
	If MTFtimer > 0 Then
		If MTFtimer <= 70*120
			MTFtimer = MTFtimer + FPSfactor
		ElseIf MTFtimer > 70*120 And MTFtimer < 10000
			If PlayerInReachableRoom()
				PlayAnnouncement("SFX\Intercom\MTF\NTF\AnnouncAfter1.ogg")
			EndIf
			MTFtimer = 10000
		ElseIf MTFtimer >= 10000 And MTFtimer <= 10000+(70*120)
			MTFtimer = MTFtimer + FPSfactor
		ElseIf MTFtimer > 10000+(70*120) And MTFtimer < 20000
			If PlayerInReachableRoom()
				PlayAnnouncement("SFX\Intercom\MTF\NTF\AnnouncAfter2.ogg")
			EndIf
			MTFtimer = 20000
		ElseIf MTFtimer >= 20000 And MTFtimer <= 20000+(70*60)
			MTFtimer = MTFtimer + FPSfactor
		ElseIf MTFtimer > 20000+(70*60) And MTFtimer < 25000
			If PlayerInReachableRoom()
				If CurrD9341 <> Null Then
					If (Not CurrD9341\IsDead) Then
						If Rand(1,7)=1
							PlayAnnouncement("SFX\Intercom\MTF\NTF\ThreatAnnouncPossession.ogg")
						Else
							PlayAnnouncement("SFX\Intercom\MTF\NTF\ThreatAnnounc"+Rand(1,3)+".ogg")
						EndIf
					EndIf
				EndIf
			EndIf
			MTFtimer = 25000
		ElseIf MTFtimer >= 25000 And MTFtimer <= 25000+(70*60)
			MTFtimer = MTFtimer + FPSfactor
		ElseIf MTFtimer > 25000+(70*60) And MTFtimer < 30000
			If PlayerInReachableRoom()
				PlayAnnouncement("SFX\Intercom\MTF\NTF\AnnouncCameraCheck.ogg")
			EndIf
			MTFtimer = 30000
		ElseIf MTFtimer >= 30000 And MTFtimer <= 30000+(70*60)
			MTFtimer = MTFtimer + FPSfactor
		ElseIf MTFtimer > 30000+(70*60) And MTFtimer < 35000
			If PlayerInReachableRoom()
				If Rand(5) = 1 Then
					PlayAnnouncement("SFX\Intercom\MTF\NTF\AnnouncCameraFound"+Rand(1, 2)+".ogg")
				Else
					PlayAnnouncement("SFX\Intercom\MTF\NTF\AnnouncCameraNoFound.ogg")
				EndIf
			EndIf
			MTFtimer = 35000
		ElseIf MTFtimer >= 35000 And MTFtimer <= 35000+(70*60)
			MTFtimer = MTFtimer + FPSfactor
		ElseIf MTFtimer > 40000+(70*60) And MTFtimer < 45000
			If PlayerInReachableRoom()
				If Rand(5) = 1 Then
					PlayAnnouncement("SFX\Intercom\MTF\NTF\ThreatAnnouncFinal.ogg")
				Else
					PlayAnnouncement("SFX\Intercom\MTF\NTF\ThreatAnnouncObjective.ogg")
				EndIf
			EndIf
			MTFtimer = 40000
		ElseIf MTFtimer > 45000 And MTFtimer < 45000
			If PlayerInReachableRoom()
				If Curr106\Contained And Curr173\Contained Then
					PlayAnnouncement("SFX\Intercom\MTF\NTF\ThreatAnnouncExposure.ogg")
				EndIf
			EndIf
			MTFtimer = 45000
		EndIf
	EndIf
	
End Function

Function UpdateOmegaWarhead()
	
	If ecst\OmegaWarheadActivated And ecst\OmegaWarheadDetonate Then
		ecst\OmegaWarheadTimer = ecst\OmegaWarheadTimer + FPSfactor
	EndIf
	If ecst\OmegaWarheadTimer > 0 Then
		If (Not IsPlayerOutside()) Then
			
			ShouldPlay = MUS_NULL
			
			If DebugHUD Then
				If ecst\OmegaWarheadTimer >=5*70 And ecst\OmegaWarheadTimer < 5.1*70 Then
					If (ChannelPlaying(OmegaWarheadCHN[0])) Then StopChannel(OmegaWarheadCHN[0])
					If (Not ChannelPlaying(OmegaWarheadCHN[0])) Then
						If opt\MusicVol > 0 Then
							OmegaWarheadCHN[0] = PlaySound_Strict(OmegaWarheadSFX[0])
						EndIf
					EndIf
				ElseIf ecst\OmegaWarheadTimer > 10*70 And ecst\OmegaWarheadTimer < 10.05*70 Then
					If (ChannelPlaying(OmegaWarheadCHN[1])) Then StopChannel(OmegaWarheadCHN[1])
					If (Not ChannelPlaying(OmegaWarheadCHN[1])) Then
						OmegaWarheadCHN[1] = PlaySound_Strict(OmegaWarheadSFX[1])
					EndIf
				ElseIf ecst\OmegaWarheadTimer >= 18*70 And ecst\OmegaWarheadTimer < 18.05*70 Then
					ExplosionTimer = 1
				EndIf
			Else
				If ecst\OmegaWarheadTimer >=55*70 And ecst\OmegaWarheadTimer < 55.1*70 Then
					If (ChannelPlaying(OmegaWarheadCHN[0])) Then StopChannel(OmegaWarheadCHN[0])
					If (Not ChannelPlaying(OmegaWarheadCHN[0])) Then
						If opt\MusicVol > 0 Then
							OmegaWarheadCHN[0] = PlaySound_Strict(OmegaWarheadSFX[0])
						EndIf
					EndIf
				ElseIf ecst\OmegaWarheadTimer > 300*70 And ecst\OmegaWarheadTimer < 300.05*70 Then
					If (ChannelPlaying(OmegaWarheadCHN[1])) Then StopChannel(OmegaWarheadCHN[1])
					If (Not ChannelPlaying(OmegaWarheadCHN[1])) Then
						OmegaWarheadCHN[1] = PlaySound_Strict(OmegaWarheadSFX[1])
					EndIf
				ElseIf ecst\OmegaWarheadTimer >= 308*70 And ecst\OmegaWarheadTimer < 308.05*70 Then
					ExplosionTimer = 1
				EndIf
			EndIf
		Else
			If ecst\OmegaWarheadTimer > 309*70 And ecst\OmegaWarheadTimer < 309.05*70 Then
				If (ChannelPlaying(OmegaWarheadCHN[1])) Then StopChannel(OmegaWarheadCHN[1])
				If (Not ChannelPlaying(OmegaWarheadCHN[1])) Then
					OmegaWarheadCHN[1] = PlaySound_Strict(LoadTempSound("SFX\Ending\GateB\Nuke1.ogg"))
				EndIf
			EndIf
			If ecst\OmegaWarheadTimer > 309*70 Then
				ecst\OmegaWarheadActivated = False
				ecst\OmegaWarheadDetonate = False
				ecst\OmegaWarheadTimer = 0
			EndIf
		EndIf
	EndIf
	
End Function

;[End Block]

; ~ [DECALS]

Include "SourceCode/Decals.bb"

; ~ [INI - FUNCTIONS]

Type INIFile
	Field name$
	Field bank%
	Field bankOffset% = 0
	Field size%
End Type

Function ReadINILine$(file.INIFile)
	Local rdbyte%
	Local firstbyte% = True
	Local offset% = file\bankOffset
	Local bank% = file\bank
	Local retStr$ = ""
	rdbyte = PeekByte(bank,offset)
	While ((firstbyte) Lor ((rdbyte<>13) And (rdbyte<>10))) And (offset<file\size)
		rdbyte = PeekByte(bank,offset)
		If ((rdbyte<>13) And (rdbyte<>10)) Then
			firstbyte = False
			retStr=retStr+Chr(rdbyte)
		EndIf
		offset=offset+1
	Wend
	file\bankOffset = offset
	Return retStr
End Function

Function UpdateINIFile$(filename$)
	Local file.INIFile = Null
	Local k.INIFile
	
	For k.INIFile = Each INIFile
		If k\name = Lower(filename) Then
			file = k
			Exit
		EndIf
	Next
	
	If file=Null Then Return
	
	If file\bank<>0 Then FreeBank file\bank
	Local f% = ReadFile(file\name)
	Local fleSize% = 1
	While fleSize<FileSize(file\name)
		fleSize=fleSize*2
	Wend
	file\bank = CreateBank(fleSize)
	file\size = 0
	While Not Eof(f)
		PokeByte(file\bank,file\size,ReadByte(f))
		file\size=file\size+1
	Wend
	
	CloseFile(f)
End Function

Function DeleteINIFile(filename$)
	If FileType(filename) <> 0 Then
		Local file.INIFile = Null
		Local k.INIFile
		
		For k.INIFile = Each INIFile
			If k\name = Lower(filename) Then
				file = k
				Exit
			EndIf
		Next
		If file <> Null Then
			FreeBank file\bank
			;debuglog "FREED BANK FOR "+filename
			Delete file
			Return
		EndIf
	EndIf
	;debuglog "COULD NOT FREE BANK FOR "+filename+": INI FILE IS NOT LOADED"
End Function

Function GetINIString$(file$, section$, parameter$, defaultvalue$="")
	Local TemporaryString$ = ""
	Local lfile.INIFile = Null
	Local k.INIFile
	
	For k.INIFile = Each INIFile
		If k\name = Lower(file) Then
			lfile = k
			Exit
		EndIf
	Next
	
	If lfile = Null Then
		;debuglog "CREATE BANK FOR "+file
		lfile = New INIFile
		lfile\name = Lower(file)
		lfile\bank = 0
		UpdateINIFile(lfile\name)
	EndIf
	
	lfile\bankOffset = 0
	
	section = Lower(section)
	
	While lfile\bankOffset<lfile\size
		Local strtemp$ = ReadINILine(lfile)
		If Left(strtemp,1) = "[" Then
			strtemp$ = Lower(strtemp)
			If Mid(strtemp, 2, Len(strtemp)-2)=section Then
				Repeat
					TemporaryString = ReadINILine(lfile)
					If Lower(Trim(Left(TemporaryString, Max(Instr(TemporaryString, "=") - 1, 0)))) = Lower(parameter) Then
						Return Trim( Right(TemporaryString,Len(TemporaryString)-Instr(TemporaryString,"=")) )
					EndIf
				Until (Left(TemporaryString, 1) = "[") Lor (lfile\bankOffset>=lfile\size)
				
				Return defaultvalue
			EndIf
		EndIf
	Wend
	
	Return defaultvalue
End Function

Function GetINIInt%(file$, section$, parameter$, defaultvalue% = 0)
	Local txt$ = GetINIString(file$, section$, parameter$, defaultvalue)
	
	If Lower(txt) = "true" Then
		Return 1
	ElseIf Lower(txt) = "false"
		Return 0
	Else
		Return Int(txt)
	EndIf
	
End Function

Function GetINIFloat#(file$, section$, parameter$, defaultvalue# = 0.0)
	Return Float(GetINIString(file$, section$, parameter$, defaultvalue))
End Function

Function GetINIString2$(file$, start%, parameter$, defaultvalue$="")
	Local TemporaryString$ = ""
	Local f% = ReadFile(file)
	
	Local n%=0
	While Not Eof(f)
		Local strtemp$ = ReadLine(f)
		n=n+1
		If n=start Then 
			Repeat
				TemporaryString = ReadLine(f)
				If Lower(Trim(Left(TemporaryString, Max(Instr(TemporaryString, "=") - 1, 0)))) = Lower(parameter) Then
					CloseFile f
					Return Trim( Right(TemporaryString,Len(TemporaryString)-Instr(TemporaryString,"=")) )
				EndIf
			Until Left(TemporaryString, 1) = "[" Lor Eof(f)
			CloseFile f
			Return defaultvalue
		EndIf
	Wend
	
	CloseFile f	
	
	Return defaultvalue
End Function

Function GetINIInt2%(file$, start%, parameter$, defaultvalue$="")
	Local txt$ = GetINIString2(file$, start%, parameter$, defaultvalue$)
	
	If Lower(txt) = "true" Then
		Return 1
	ElseIf Lower(txt) = "false"
		Return 0
	Else
		Return Int(txt)
	EndIf
	
End Function

Function GetINISectionLocation%(file$, section$)
	Local n%=0
	Local Temp%
	Local f% = ReadFile(file)
	
	section = Lower(section)
	
	While Not Eof(f)
		Local strtemp$ = ReadLine(f)
		n=n+1
		If Left(strtemp,1) = "[" Then
			strtemp$ = Lower(strtemp)
			Temp = Instr(strtemp, section)
			If Temp>0 Then
				If (Mid(strtemp, Temp-1, 1)="[" Lor Mid(strtemp, Temp-1, 1)="|") And (Mid(strtemp, Temp+Len(section), 1)="]" Lor Mid(strtemp, Temp+Len(section), 1)="|") Then
					CloseFile f
					Return n
				EndIf
			EndIf
		EndIf
	Wend
	
	CloseFile f
End Function

Function PutINIValue%(file$, INI_sSection$, INI_sKey$, INI_sValue$)
	INI_sSection = "[" + Trim$(INI_sSection) + "]"
	Local INI_sUpperSection$ = Upper$(INI_sSection)
	INI_sKey = Trim$(INI_sKey)
	INI_sValue = Trim$(INI_sValue)
	Local INI_sFilename$ = file$
	
	Local INI_sContents$ = INI_FileToString(INI_sFilename)
	
	Local INI_bWrittenKey% = False
	Local INI_bSectionFound% = False
	Local INI_sCurrentSection$ = ""
	
	Local INI_lFileHandle% = WriteFile(INI_sFilename)
	If INI_lFileHandle = 0 Then Return False
	
	Local INI_lOldPos% = 1
	Local INI_lPos% = Instr(INI_sContents, Chr$(0))
	
	While (INI_lPos <> 0)
		
		Local INI_sTemp$ = Mid$(INI_sContents, INI_lOldPos, (INI_lPos - INI_lOldPos))
		
		If (INI_sTemp <> "") Then
			
			If Left$(INI_sTemp, 1) = "[" And Right$(INI_sTemp, 1) = "]" Then
				
				If (INI_sCurrentSection = INI_sUpperSection) And (INI_bWrittenKey = False) Then
					INI_bWrittenKey = INI_CreateKey(INI_lFileHandle, INI_sKey, INI_sValue)
				End If
				INI_sCurrentSection = Upper$(INI_CreateSection(INI_lFileHandle, INI_sTemp))
				If (INI_sCurrentSection = INI_sUpperSection) Then INI_bSectionFound = True
				
			Else
				If Left(INI_sTemp, 1) = ":" Then
					WriteLine INI_lFileHandle, INI_sTemp
				Else
					Local lEqualsPos% = Instr(INI_sTemp, "=")
					If (lEqualsPos <> 0) Then
						If (INI_sCurrentSection = INI_sUpperSection) And (Upper$(Trim$(Left$(INI_sTemp, (lEqualsPos - 1)))) = Upper$(INI_sKey)) Then
							If (INI_sValue <> "") Then INI_CreateKey INI_lFileHandle, INI_sKey, INI_sValue
							INI_bWrittenKey = True
						Else
							WriteLine INI_lFileHandle, INI_sTemp
						End If
					End If
				EndIf
				
			End If
			
		End If
		
		INI_lOldPos = INI_lPos + 1
		INI_lPos% = Instr(INI_sContents, Chr$(0), INI_lOldPos)
		
	Wend
	
	If (INI_bWrittenKey = False) Then
		If (INI_bSectionFound = False) Then INI_CreateSection INI_lFileHandle, INI_sSection
		INI_CreateKey INI_lFileHandle, INI_sKey, INI_sValue
	End If
	
	CloseFile INI_lFileHandle
	
	Return True
	
End Function

Function INI_FileToString$(INI_sFilename$)
	
	Local INI_sString$ = ""
	Local INI_lFileHandle%= ReadFile(INI_sFilename)
	If INI_lFileHandle <> 0 Then
		While Not(Eof(INI_lFileHandle))
			INI_sString = INI_sString + ReadLine$(INI_lFileHandle) + Chr$(0)
		Wend
		CloseFile INI_lFileHandle
	End If
	Return INI_sString
	
End Function

Function INI_CreateSection$(INI_lFileHandle%, INI_sNewSection$)
	
	If FilePos(INI_lFileHandle) <> 0 Then WriteLine INI_lFileHandle, ""
	WriteLine INI_lFileHandle, INI_sNewSection
	Return INI_sNewSection
	
End Function

Function INI_CreateKey%(INI_lFileHandle%, INI_sKey$, INI_sValue$)
	
	WriteLine INI_lFileHandle, INI_sKey + " = " + INI_sValue
	Return True
	
End Function

Function SaveOptionsINI()
	
	PutINIValue(gv\OptionFile, "options", "mouse sensitivity", MouseSens)
	PutINIValue(gv\OptionFile, "options", "mouse smoothing", opt\MouseSmooth)
	PutINIValue(gv\OptionFile, "options", "invert mouse y", InvertMouse)
	PutINIValue(gv\OptionFile, "options", "hold to aim", opt\HoldToAim)
	PutINIValue(gv\OptionFile, "options", "hold to crouch", opt\HoldToCrouch)
	PutINIValue(gv\OptionFile, "options", "bump mapping enabled", BumpEnabled)	
	PutINIValue(gv\OptionFile, "options", "play startup videos", opt\PlayStartupVideos)
	PutINIValue(gv\OptionFile, "options", "HUD enabled", HUDenabled)
	PutINIValue(gv\OptionFile, "options", "screengamma", ScreenGamma)
	PutINIValue(gv\OptionFile, "options", "vsync", Vsync)
	PutINIValue(gv\OptionFile, "options", "show FPS", opt\ShowFPS)
	PutINIValue(gv\OptionFile, "options", "framelimit", Framelimit%) 
	;PutINIValue(gv\OptionFile, "options", "achievement popup enabled", opt\AchvMSGenabled%)
	PutINIValue(gv\OptionFile, "options", "room lights enabled", opt\EnableRoomLights%)
	PutINIValue(gv\OptionFile, "options", "texture details", opt\TextureDetails%)
	PutINIValue(gv\OptionFile, "options", "texture filtering", opt\TextureFiltering%)
	PutINIValue(gv\OptionFile, "options", "particle amount", ParticleAmount)
	PutINIValue(gv\OptionFile, "options", "enable vram", SaveTexturesInVRam)
	PutINIValue(gv\OptionFile, "options", "cubemaps", opt\RenderCubeMapMode)
	PutINIValue(gv\OptionFile, "options", "enable player model", opt\PlayerModelEnabled)
	PutINIValue(gv\OptionFile, "options", "fov", FOV)
	PutINIValue(gv\OptionFile, "options", "render scope", opt\RenderScope)
	PutINIValue(gv\OptionFile, "options", "enabled 3d menu", opt\Menu3DEnabled)
	PutINIValue(gv\OptionFile, "options", "steam enabled", opt\SteamEnabled)
	
	PutINIValue(gv\OptionFile, "options", "intro enabled", opt\IntroEnabled)
	PutINIValue(gv\OptionFile, "options", "language", opt\LanguageVal)
	
	PutINIValue(gv\OptionFile, "console", "enabled", opt\ConsoleEnabled%)
	PutINIValue(gv\OptionFile, "console", "auto opening", opt\ConsoleOpening%)
	
	PutINIValue(gv\OptionFile, "audio", "master volume", opt\MasterVol)
	PutINIValue(gv\OptionFile, "audio", "music volume", opt\MusicVol)
	PutINIValue(gv\OptionFile, "audio", "sound volume", opt\SFXVolume)
	PutINIValue(gv\OptionFile, "audio", "voice volume", opt\VoiceVol)
	PutINIValue(gv\OptionFile, "audio", "sfx release", opt\EnableSFXRelease)
	PutINIValue(gv\OptionFile, "audio", "enable user tracks", EnableUserTracks%)
	PutINIValue(gv\OptionFile, "audio", "user track setting", UserTrackMode%)
	PutINIValue(gv\OptionFile, "audio", "enable elevator music", opt\ElevatorMusicEnabled)
	PutINIValue(gv\OptionFile, "audio", "mainmenu music", opt\MainMenuMusic)
	PutINIValue(gv\OptionFile, "audio", "enable subtitles", opt\EnableSubtitles)
	
	PutINIValue(gv\OptionFile, "binds", "Right key", KEY_RIGHT)
	PutINIValue(gv\OptionFile, "binds", "Left key", KEY_LEFT)
	PutINIValue(gv\OptionFile, "binds", "Up key", KEY_UP)
	PutINIValue(gv\OptionFile, "binds", "Down key", KEY_DOWN)
	PutINIValue(gv\OptionFile, "binds", "Blink key", KEY_BLINK)
	PutINIValue(gv\OptionFile, "binds", "Sprint key", KEY_SPRINT)
	PutINIValue(gv\OptionFile, "binds", "Inventory key", KEY_INV)
	PutINIValue(gv\OptionFile, "binds", "Crouch key", KEY_CROUCH)
	PutINIValue(gv\OptionFile, "binds", "Save key", KEY_SAVE)
	PutINIValue(gv\OptionFile, "binds", "Console key", KEY_CONSOLE)
	PutINIValue(gv\OptionFile, "binds", "Reload key", KEY_RELOAD)
	PutINIValue(gv\OptionFile, "binds", "Holstergun key", KEY_HOLSTERGUN)
	PutINIValue(gv\OptionFile, "binds", "Firemode key", KEY_FIREMODE)
	PutINIValue(gv\OptionFile, "binds", "Attachment key", KEY_ATTACHMENTS)
	;PutINIValue(gv\OptionFile, "binds", "Scramble key", KEY_SCRAMBLE)
	PutINIValue(gv\OptionFile, "binds", "Radiotoggle key", KEY_RADIOTOGGLE)
	PutINIValue(gv\OptionFile, "binds", "Use key", KEY_USE)
	PutINIValue(gv\OptionFile, "binds", "Load key", KEY_LOAD)
	SaveController()
	SaveKeyBinds()
	
	PutINIValue(gv\OptionFile, "game options", "game mode", gopt\SingleplayerGameMode)
	
End Function

Include "SourceCode\Graphics.bb"

Function CheckForPlayerInFacility()
	
	If EntityY(Collider)>100.0
		Return False
	EndIf
	If EntityY(Collider)< -10.0
		Return 2
	EndIf
	If EntityY(Collider)> 7.0 And EntityY(Collider)<=100.0
		Return 2
	EndIf
	
	Return True
End Function

Function ControlSoundVolume()
	Local snd.Sound,i
	
	For snd.Sound = Each Sound
		For i=0 To MaxChannelsAmount - 1
			ChannelVolume snd\channels[i],opt\SFXVolume#
		Next
	Next
	
End Function

Function UpdateDeafPlayer()
	
	If DeafTimer > 0
		DeafTimer = DeafTimer-FPSfactor
		opt\SFXVolume# = 0.0
		If opt\SFXVolume# > 0.0
			ControlSoundVolume()
		EndIf
		;debuglog DeafTimer
	Else
		DeafTimer = 0
		If DeafPlayer Then ControlSoundVolume()
		DeafPlayer = False
	EndIf
	
End Function

Function ScaledMouseX#()
	Return Float(MousePosX-(RealGraphicWidth*0.5*(1.0-AspectRatioRatio)))*Float(opt\GraphicWidth)/Float(RealGraphicWidth*AspectRatioRatio)
End Function

Function ScaledMouseY#()
	Return Float(MousePosY)*Float(opt\GraphicHeight)/Float(RealGraphicHeight)
End Function

Function CatchErrors(location$)
	SetErrorMsg(7, "Error located in: "+location)
	;[Block]
	
	;[End Block]
End Function

Function PlayAnnouncement(file$)
	
	If IntercomStreamCHN <> 0 Then
		StopStream_Strict(IntercomStreamCHN)
		IntercomStreamCHN = 0
	EndIf
	
	IntercomStreamCHN = StreamSound_Strict(file$,opt\SFXVolume*opt\MasterVol,0)
	
End Function

Function UpdateStreamSounds()
	Local e.Events
	
	If FPSfactor > 0
		If IntercomStreamCHN <> 0
			SetStreamVolume_Strict(IntercomStreamCHN,opt\SFXVolume*opt\MasterVol)
		EndIf
		For e = Each Events
			If e\SoundCHN[0]<>0
				If e\SoundCHN_isStream[0]
					SetStreamVolume_Strict(e\SoundCHN[0],opt\SFXVolume*opt\MasterVol)
				EndIf
			EndIf
			If e\SoundCHN[1]<>0
				If e\SoundCHN_isStream[1]
					SetStreamVolume_Strict(e\SoundCHN[1],opt\SFXVolume*opt\MasterVol)
				EndIf
			EndIf
		Next
	EndIf
	
	If (Not PlayerInReachableRoom())
		If PlayerRoom\RoomTemplate\Name <> "exit1" And PlayerRoom\RoomTemplate\Name <> "gatea"
			If IntercomStreamCHN <> 0
				StopStream_Strict(IntercomStreamCHN)
				IntercomStreamCHN = 0
			EndIf
			For e = Each Events
				If e\SoundCHN[0]<>0 And e\SoundCHN_isStream[0]
					StopStream_Strict(e\SoundCHN[0])
					e\SoundCHN[0] = 0
					e\SoundCHN_isStream[0] = 0
				EndIf
				If e\SoundCHN[1]<>0 And e\SoundCHN_isStream[1]
					StopStream_Strict(e\SoundCHN[1])
					e\SoundCHN[1] = 0
					e\SoundCHN_isStream[1] = 0
				EndIf
			Next
		EndIf
	EndIf
	
End Function

Function InitFonts()
	Local txt$
	
	fo\Font[Font_Default] = LoadFont("GFX\font\Courier New.ttf", Int(16 * (opt\GraphicHeight / 1024.0)))
	fo\Font[Font_Default_Medium] = LoadFont("GFX\font\Courier New.ttf", Int(28 * (opt\GraphicHeight / 1024.0)))
	fo\Font[Font_Default_Large] = LoadFont("GFX\font\Courier New.ttf", Int(46 * (opt\GraphicHeight / 1024.0)))
	
	If I_Loc\Localized Then
		fo\Font[Font_Menu] = LoadFont("GFX\font\Capture it.ttf", Int(56 * (opt\GraphicHeight / 1024.0)))
		fo\Font[Font_Menu_Small] = LoadFont("GFX\font\Capture it.ttf",Int(23 * (opt\GraphicHeight / 1024.0)))
		fo\Font[Font_Menu_Medium] = LoadFont("GFX\font\Capture it.ttf",Int(42 * (opt\GraphicHeight / 1024.0)))
	Else
		fo\Font[Font_Menu_Small] = LoadFont("GFX\font\ITC Bauhaus LT Demi.ttf",Int(23 * (opt\GraphicHeight / 1024.0)))
		fo\Font[Font_Menu_Medium] = LoadFont("GFX\font\ITC Bauhaus LT Demi.ttf",Int(42 * (opt\GraphicHeight / 1024.0)))
		fo\Font[Font_Menu] = LoadFont("GFX\font\ITC Bauhaus LT Demi.ttf", Int(56 * (opt\GraphicHeight / 1024.0)))
	EndIf
	
	fo\Font[Font_Digital_Small] = LoadFont("GFX\font\LCDNovaRus.ttf", Int(17 * (opt\GraphicHeight / 1024.0)))
	fo\Font[Font_Digital_Medium] = LoadFont("GFX\font\LCDNovaRus.ttf", Int(24 * (opt\GraphicHeight / 1024.0)))
	fo\Font[Font_Digital_Large] = LoadFont("GFX\font\LCDNovaRus.ttf", Int(47 * (opt\GraphicHeight / 1024.0)))
	
	fo\Font[Font_Journal] = LoadFont("GFX\font\Journal.ttf", Int(56 * (opt\GraphicHeight / 1024.0)))
	
	If I_Loc\Localized Then
		fo\ConsoleFont% = LoadFont("GFX\font\Courier New.ttf", Int(16 * (opt\GraphicHeight / 1024.0)))
	Else
		fo\ConsoleFont% = LoadFont("GFX\font\Minimal5x7.ttf", Int(28 * (opt\GraphicHeight / 1024.0)))
	EndIf
	
	SetFont fo\Font[Font_Menu]
	
End Function

Function SetMsgColor(r%, g%, b%)
	Local a# = Min(m_msg\Timer / 2, 255)/255.0
	Color a * r, a * g, a * b
End Function

Function CreateMsg(Txt$,Timer=70*6,isSplash%=True,R#=255,G#=255,B#=255)
	
	If MainMenuOpen Lor (HUDenabled And psp\IsShowingHUD) Then
		
		m_msg\isSplash = isSplash
		m_msg\R = R
		m_msg\G = G
		m_msg\B = B
		
		If isSplash Then
			Delete Each SplashMsg
			CreateMsgSplash(Txt,(opt\GraphicWidth / 2),(opt\GraphicHeight / 2) + 200,Timer/2,30,True,R,G,B)
		Else
			m_msg\Txt = Txt
			m_msg\Timer = Timer
		EndIf
	EndIf
	
End Function

Function UpdateMsg()
	
	If HUDenabled Then
		If m_msg\Timer > 0 Then
			If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
				If (Not MainMenuOpen) Then
					If psp\IsShowingHUD Then
						Local temp% = False
						If (Not InvOpen%) Then
							If SelectedItem <> Null Then
								If SelectedItem\itemtemplate\tempname = "paper" Lor SelectedItem\itemtemplate\tempname = "oldpaper" Then
									temp% = True
								EndIf
							EndIf
						EndIf
						If (Not temp%) Then
							If (Not m_msg\isSplash) Then
								SetMsgColor(0, 0, 0)
								Text((opt\GraphicWidth / 2)+1, (opt\GraphicHeight / 2) + 201, m_msg\Txt, True, False)
								SetMsgColor(255, 255, 255)
								Text((opt\GraphicWidth / 2), (opt\GraphicHeight / 2) + 200, m_msg\Txt, True, False)
							EndIf
						Else
							SetMsgColor(0, 0, 0)
							Text((opt\GraphicWidth / 2)+1, (opt\GraphicHeight * 0.94) + 1, m_msg\Txt, True, False)
							SetMsgColor(255, 255, 255)
							Text((opt\GraphicWidth / 2), (opt\GraphicHeight * 0.94), m_msg\Txt, True, False)
						EndIf
					EndIf
				Else
					SetMsgColor(0, 0, 0)
					Text((opt\GraphicWidth / 2)+1, (opt\GraphicHeight / 2) + 201, m_msg\Txt, True, False)
					SetMsgColor(255, 255, 255)
					If Left(m_msg\Txt,20)="Loaded resource pack" Then
						SetMsgColor(0, 255, 0)
					ElseIf Left(m_msg\Txt,14)="Blitz3D Error!" Then
						SetMsgColor(255, 0, 0)
					EndIf
					Text((opt\GraphicWidth / 2), (opt\GraphicHeight / 2) + 200, m_msg\Txt, True, False)
				EndIf
			Else
				SetFont fo\Font[Font_Default]
				SetMsgColor(0, 0, 0)
				Text((opt\GraphicWidth / 2)+1, (opt\GraphicHeight / 2) + 201, m_msg\Txt, True, False)
				SetMsgColor(255, 255, 255)
				If Left(m_msg\Txt,14)="Blitz3D Error!" Then
					SetMsgColor(255, 0, 0)
				EndIf
				Text((opt\GraphicWidth / 2), (opt\GraphicHeight / 2) + 200, m_msg\Txt, True, False)
			EndIf
		EndIf
	EndIf
	
End Function

Function CreateMsgSplash.SplashMsg(txt$,x#,y#,displayamount#,speed#=1,centered%=False,r#=255,g#=255,b#=255)
	Local m_spl.SplashMsg = New SplashMsg
	
	m_spl\Txt = txt
	m_spl\X = x
	m_spl\Y = y
	m_spl\DisplayAmount = displayamount
	m_spl\Centered = centered
	m_spl\Speed = speed
	m_spl\R# = r
	m_spl\G# = g
	m_spl\B# = b
	
	Return m_spl
End Function

Function UpdateSplashMsg()
	Local m_spl.SplashMsg
	
	For m_spl = Each SplashMsg
		If m_spl\CurrentLength < Len(m_spl\Txt) Then
			If m_spl\Timer < 10.0
				m_spl\Timer = m_spl\Timer + FPSfactor2 * m_spl\Speed
			Else
				m_spl\CurrentLength = m_spl\CurrentLength + 1
				m_spl\Timer = 0.0
			EndIf
		Else
			If m_spl\Timer < m_spl\DisplayAmount+255.0 Then
				m_spl\Timer = Min(m_spl\Timer+FPSfactor2,m_spl\DisplayAmount+255.0)
			Else
				Delete m_spl
			EndIf
		EndIf
	Next
	
End Function

Function DrawSplashMsg()
	Local m_spl.SplashMsg
	Local amount% = 0
	
	For m_spl = Each SplashMsg
		SetFont fo\Font[Font_Default]
		
		If m_spl\CurrentLength < Len(m_spl\Txt)
			m_spl\R = m_spl\R
			m_spl\G = m_spl\G
			m_spl\B = m_spl\B
		Else
			m_spl\R = Min((m_spl\DisplayAmount+255.0)-m_spl\Timer,m_spl\R)
			m_spl\G = Min((m_spl\DisplayAmount+255.0)-m_spl\Timer,m_spl\G)
			m_spl\B = Min((m_spl\DisplayAmount+255.0)-m_spl\Timer,m_spl\B)
		EndIf
		
		Color m_spl\R,m_spl\G,m_spl\B
		
		If (Not m_spl\Centered) Then
			Text m_spl\X,m_spl\Y+(32*amount),Left(m_spl\Txt,m_spl\CurrentLength)
		Else
			Text m_spl\X-StringWidth(m_spl\Txt)/2,m_spl\Y+(32*amount)-StringHeight(m_spl\Txt)/2,Left(m_spl\Txt,m_spl\CurrentLength)
		EndIf
		
		amount = amount + 1
	Next
	
End Function

Function CreateHintMsg(txt$)
	
	m_msg\HintTxt = txt
	m_msg\HintTimer = 0.0
	m_msg\HintTxt_Y = 0.0
	
End Function

Function UpdateHintMsg()
	Local scale# = opt\GraphicHeight/768.0
	Local width = StringWidth(m_msg\HintTxt)+20*scale
	Local height% = 30*scale
	Local x% = (opt\GraphicWidth/2)-(width/2)
	Local y% = (-height)+m_msg\HintTxt_Y
	
	If m_msg\HintTxt <> ""
		If m_msg\HintTimer < 70*5
			If m_msg\HintTxt_Y < height
				m_msg\HintTxt_Y = Min(m_msg\HintTxt_Y+2*FPSfactor2,height)
			Else
				m_msg\HintTxt_Y = height
			EndIf
			m_msg\HintTimer = m_msg\HintTimer + FPSfactor2
		Else
			If m_msg\HintTxt_Y > 0
				m_msg\HintTxt_Y = Max(m_msg\HintTxt_Y-2*FPSfactor2,0)
			Else
				CreateHintMsg("")
			EndIf
		EndIf
	EndIf
	
End Function

Function DrawHintMSG()
	Local scale# = opt\GraphicHeight/768.0
	Local width = StringWidth(m_msg\HintTxt)+20*scale
	Local height% = 30*scale
	Local x% = (opt\GraphicWidth/2)-(width/2)
	Local y% = (-height)+m_msg\HintTxt_Y
	
	If m_msg\HintTxt <> ""
		DrawFrame(x,y,width,height)
		Color 255,255,255
		SetFont fo\Font[Font_Default]
		Text(opt\GraphicWidth/2,y+(height/2),m_msg\HintTxt,True,True)
	EndIf
	
End Function

Function CreateAutoSaveMsg()
	
	as_msg\Timer = 0.0
	as_msg\Txt_Y = 0.0
	
End Function

Function UpdateAutoSaveMsg()
	Local scale# = opt\GraphicHeight/768.0
	Local width = StringWidth(GetLocalString("Menu", "autosave")) + 20 * scale
	Local height% = 30*scale
	Local x% = (opt\GraphicWidth)-(StringWidth(GetLocalString("Menu", "autosave")) + 20)
	Local y% = (-height)+as_msg\Txt_Y
	
	If as_msg\Timer < 70*5
		If as_msg\Txt_Y < height
			as_msg\Txt_Y = Min(as_msg\Txt_Y+2*FPSfactor2,height)
		Else
			as_msg\Txt_Y = height
		EndIf
		as_msg\Timer = as_msg\Timer + FPSfactor2
	Else
		If as_msg\Txt_Y > 0
			as_msg\Txt_Y = Max(as_msg\Txt_Y-2*FPSfactor2,0)
		EndIf
	EndIf
	
End Function

Function DrawAutoSaveMSG()
	Local scale# = opt\GraphicHeight/768.0
	Local width = StringWidth(GetLocalString("Menu", "autosave")) + 20*scale
	Local height% = 30*scale
	Local x% = (opt\GraphicWidth)-(StringWidth(GetLocalString("Menu", "autosave")) + 20)
	Local y% = (-height)+as_msg\Txt_Y
	
	RenderLoadingText(x, y + (height / 2), GetLocalString("Menu", "autosave"), True, True)
	
End Function

Function TeleportEntity(entity%,x#,y#,z#,customradius#=0.3,isglobal%=False,pickrange#=2.0,dir%=0)
	Local pvt,pick
	;dir = 0 - towards the floor (default)
	;dir = 1 - towrads the ceiling (mostly for PD decal after leaving dimension)
	
	pvt = CreatePivot()
	PositionEntity(pvt, x,y+0.05,z,isglobal)
	If dir%=0
		RotateEntity pvt,90,0,0
	Else
		RotateEntity pvt,-90,0,0
	EndIf
	pick = EntityPick(pvt,pickrange)
	If pick<>0
		If dir%=0
			PositionEntity(entity, x,PickedY()+customradius#+0.02,z,isglobal)
		Else
			PositionEntity(entity, x,PickedY()+customradius#-0.02,z,isglobal)
		EndIf
		;debuglog "Entity teleported successfully"
	Else
		PositionEntity(entity,x,y,z,isglobal)
		;debuglog "Warning: no ground found when teleporting an entity"
	EndIf
	pvt = FreeEntity_Strict(pvt)
	ResetEntity entity
	;debuglog "Teleported entity to: "+EntityX(entity)+"/"+EntityY(entity)+"/"+EntityZ(entity)
	
End Function

Function PlayStartupVideos()
	
	If (Not opt\PlayStartupVideos) Then Return
	
	HidePointer()
	
	Local ScaledGraphicHeight%,SplashScreenVideo
	Local Ratio# = Float(RealGraphicWidth)/Float(RealGraphicHeight)
	If Ratio>1.76 And Ratio<1.78
		ScaledGraphicHeight = RealGraphicHeight
		;debuglog "Not Scaled"
	Else
		ScaledGraphicHeight% = Float(RealGraphicWidth)/(16.0/9.0)
		;debuglog "Scaled: "+ScaledGraphicHeight
	EndIf
	
	Local i, moviefile$
	For i = 0 To 2
		Select i
			Case 0
				moviefile$ = "GFX\menu\Videos\startup_NWI"
			Case 1
				moviefile$ = "GFX\menu\Videos\startup_TSS"
			Case 2
				moviefile$ = "GFX\menu\Videos\startup_NTF"
		End Select
		
		SplashScreenVideo = BlitzMovie_OpenD3D(moviefile$+".wmv", SystemProperty("Direct3DDevice7"), SystemProperty("DirectDraw7"))
			
		If SplashScreenVideo = 0 Then
			PutINIValue(gv\OptionFile, "options", "play startup video", "false")
			Return
		EndIf
		
		SplashScreenVideo = BlitzMovie_Play()
		Local SplashScreenAudio = StreamSound_Strict(moviefile$+".ogg",opt\SFXVolume,0)
		
		Repeat
			Cls
			BlitzMovie_DrawD3D(0, (RealGraphicHeight/2-ScaledGraphicHeight/2), RealGraphicWidth, ScaledGraphicHeight)
			Flip 1
			Delay 10
		Until (GetKey() Lor (Not IsStreamPlaying_Strict(SplashScreenAudio)))
		
		StopStream_Strict(SplashScreenAudio)
		BlitzMovie_Stop()
		BlitzMovie_Close()
		Cls
		Flip 1
	Next
	
	ShowPointer()
	
End Function

Function ProjectImage(img, w#, h#, Quad%, Texture%)
    Local img_w# = ImageWidth(img)
    Local img_h# = ImageHeight(img)
    If img_w > 4096 Then img_w = 4096
    If img_h > 4096 Then img_h = 4096
    If img_w < 1 Then img_w = 1
    If img_h < 1 Then img_h = 1
    
    If w > 4096 Then w = 4096
    If h > 4096 Then h = 4096
    If w < 1 Then w = 1
    If h < 1 Then h = 1
    
    Local w_rel# = w# / img_w#
    Local h_rel# = h# / img_h#
    Local g_rel# = 2048.0 / Float(RealGraphicWidth)
    Local dst_x = 1024 - (img_w / 2.0)
    Local dst_y = 1024 - (img_h / 2.0)
    CopyRect 0, 0, img_w, img_h, dst_x, dst_y, ImageBuffer(img), TextureBuffer(Texture)
    ScaleEntity Quad, w_rel * g_rel, h_rel * g_rel, 0.0001
    RenderWorld()
	
End Function

Function CreateQuad()
	Local mesh%,surf%,v0%,v1%,v2%,v3%
	
	mesh = CreateMesh()
	surf = CreateSurface(mesh)
	v0 = AddVertex(surf,-1.0, 1.0, 0, 0, 0)
	v1 = AddVertex(surf, 1.0, 1.0, 0, 1, 0)
	v2 = AddVertex(surf, 1.0,-1.0, 0, 1, 1)
	v3 = AddVertex(surf,-1.0,-1.0, 0, 0, 1)
	AddTriangle(surf, v0, v1, v2)
	AddTriangle(surf, v0, v2, v3)
	UpdateNormals mesh
	Return mesh
	
End Function

Function ResetInput()
	
	MouseXSpeed()
	MouseYSpeed()
	MouseZSpeed()
	Mouse_X_Speed_1#=0.0
	Mouse_Y_Speed_1#=0.0
	
	FlushKeys()
	FlushMouse()
	FlushJoy()
	MouseHit1 = 0
	MouseHit2 = 0
	MouseDown1 = 0
	MouseUp1 = 0
	MouseDown2 = 0
	MouseHit3 = 0
	MouseDown3 = 0
	MouseHit(1)
	MouseHit(2)
	MouseDown(1)
	GrabbedEntity = 0
	Input_ResetTime# = 10.0
	
	KeyHitUse = 0
	KeyDownUse = 0
	MouseHit(3)
	
End Function

Function UpdateRichPresence()
	If opt\SteamEnabled Then
		If gv\RichPresenceTimer <= 0.0 Then
			If MainMenuOpen Then
				Steam_SetRichPresence("steam_display", "#Status_InMainMenu")
				BlitzcordSetLargeImage("logo")
				BlitzcordSetSmallImage("")
				BlitzcordSetActivityDetails("In Main Menu")
				BlitzcordSetActivityState("")
			ElseIf gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				Select mp_I\Gamemode\ID
					Case Gamemode_Waves, Gamemode_EAF
						Steam_SetRichPresence("map", mp_I\MapInList\Name)
						Steam_SetRichPresence("difficulty", mp_I\Gamemode\Difficulty)
						Steam_SetRichPresence("currWave", mp_I\Gamemode\Phase/2)
						Steam_SetRichPresence("maxWaves", mp_I\Gamemode\MaxPhase)
						Steam_SetRichPresence("steam_display", "#Status_Waves")
						If mp_I\Gamemode\ID = Gamemode_Waves
							BlitzcordSetLargeImage("waves")
						Else
							BlitzcordSetLargeImage("eaf")
						EndIf
					Case Gamemode_Deathmatch
						Steam_SetRichPresence("map", mp_I\MapInList\Name)
						Steam_SetRichPresence("steam_display", "#Status_TeamDeathmatch")
						BlitzcordSetLargeImage("tdm")
				End Select
				BlitzcordSetSmallImage("logo")
				BlitzcordSetActivityDetails(mp_I\Gamemode\name+" ("+mp_I\PlayerCount+" of "+mp_I\MaxPlayers+" players)")
				BlitzcordSetActivityState(mp_I\MapInList\Name)
			Else
				Steam_SetRichPresence("difficulty", SelectedDifficulty\Name)
				Steam_SetRichPresence("seed", RandomSeed)
				Steam_SetRichPresence("steam_display", "#Status_Singleplayer")
				BlitzcordSetLargeImage("singleplayer")
				BlitzcordSetSmallImage("logo")
				BlitzcordSetActivityDetails("Singleplayer")
				BlitzcordSetActivityState("Difficulty: "+SelectedDifficulty\Name+" | Seed: "+RandomSeed)
			EndIf
			BlitzcordUpdateActivity()
			BlitzcordRunCallbacks()
			gv\RichPresenceTimer = 5*70
		Else
			gv\RichPresenceTimer = gv\RichPresenceTimer - FPSfactor
		EndIf
	EndIf
End Function	
;~IDEal Editor Parameters:
;~C#Blitz3D TSS