;----------------------------------------------------------------------------------------------------------------------------------------------------
; ~ SCP - Security Stories
;----------------------------------------------------------------------------------------------------------------------------------------------------
; ~ This is a modification of the game "SCP - Containment Breach"
;----------------------------------------------------------------------------------------------------------------------------------------------------
; ~ Version 0.0.1 and onwards are stand-alone versions of the game, which means that all the assets from the original game are
; ~ included within this mod, unless these assets have been removed as they are unused.
;----------------------------------------------------------------------------------------------------------------------------------------------------
; ~ This mod was developed by the "Northern Wolf Industries"
;----------------------------------------------------------------------------------------------------------------------------------------------------
; ~ https://discord.gg/ARgRhhB8ub (DISCORD)
;----------------------------------------------------------------------------------------------------------------------------------------------------
; ~ This mod released under the CC-BY-SA 3.0 license as it is a derivative work based on SCP - Containment Breach and the SCP Foundation.
;----------------------------------------------------------------------------------------------------------------------------------------------------
; ~ This is the main file of the mod, you need to compile this file in order to be able to play the game.
;----------------------------------------------------------------------------------------------------------------------------------------------------

Include "SourceCode/Key.bb"

Include "SourceCode/SteamConstants.bb"

Type GlobalVariables
	Field OptionFile$
	Field WeaponFile$
	Field OSBit%
	Field RichPresenceTimer#
End Type

Type Options
	Field GraphicWidth%
	Field GraphicHeight%
	Field LauncherEnabled%
	Field ShowFPS%
	Field TotalGFXModes%
	Field DisplayMode%
	Field EnableRoomLights%
	Field TextureDetails%
	Field TextureFiltering%
	Field ConsoleOpening%
	Field SFXVolume#
	Field MusicVol#
	Field MasterVol#
	Field VoiceVol#
	Field EnableSFXRelease%
	Field EnableSFXRelease_Prev%
	Field ConsoleEnabled%
	Field RenderCubeMapMode%
	Field MouseSmooth#
	Field HoldToAim%
	Field HoldToCrouch%
	Field IntroEnabled%
	Field MainMenuMusic%
	Field LanguageVal%
	Field Menu3DEnabled%
	Field PlayStartupVideos%
	Field PlayerModelEnabled%
	Field ElevatorMusicEnabled%
	Field RenderScope%
	Field EnableSubtitles%
	Field SteamEnabled%
End Type

Const MaxGamemodes = 5

Const GAMEMODE_UNKNOWN% = -1
Const GAMEMODE_DEFAULT% = 0
Const GAMEMODE_NTF% = 1
Const GAMEMODE_CLASS_D% = 2
Const GAMEMODE_MULTIPLAYER% = 3

Type GameOptions
	Field SingleplayerGameMode%
	Field GameMode%
	Field CurrZone%
	Field CurrZoneString$
End Type

; ~ Include the controls file

Include "SourceCode\Controls.bb"

; ~ Include the global type instance accessors

Include "SourceCode\TypeInstances.bb"

InitGlobalVariables()

; ~ Create the folder in the AppData if it doesn't exist

If (FileType(GetEnv$("AppData")+"\scp-security-stories\")<>2) Then
	CreateDir(GetEnv$("AppData")+"\scp-security-stories")
EndIf

; ~ Create the options.ini file in the AppData folder if it doesn't exist

If (FileType(GetEnv$("AppData")+"\scp-security-stories\options.ini")<>1) Then
	WriteFile(GetEnv$("AppData")+"\scp-security-stories\options.ini")
EndIf

InitOptions()

BlitzcordCreateCore("905859463645376522")

; ~ Include the Main.bb file which is the core of the game

Include "SourceCode\Main.bb"

Function CheckForDlls()
	Local InitErrorStr$ = ""
	
	If FileSize("Blitzcord.dll")=0 Then InitErrorStr=InitErrorStr+ "Blitzcord.dll"+Chr(13)+Chr(10)
	If FileSize("BlitzHash.dll")=0 Then InitErrorStr=InitErrorStr+ "BlitzHash.dll"+Chr(13)+Chr(10)
	If FileSize("BlitzMovie.dll")=0 Then InitErrorStr=InitErrorStr+ "BlitzMovie.dll"+Chr(13)+Chr(10)
	If opt\SteamEnabled Then If FileSize("BlitzSteamworks.dll")=0 Then InitErrorStr=InitErrorStr+ "BlitzSteamworks.dll"+Chr(13)+Chr(10)
	If FileSize("d3dim700.dll")=0 Then InitErrorStr=InitErrorStr+ "d3dim700.dll"+Chr(13)+Chr(10)
	If opt\SteamEnabled Then If FileSize("discord_game_sdk.dll")=0 Then InitErrorStr=InitErrorStr+ "discord_game_sdk.dll"+Chr(13)+Chr(10)
	If FileSize("fmod.dll")=0 Then InitErrorStr=InitErrorStr+ "fmod.dll"+Chr(13)+Chr(10)
	If opt\SteamEnabled Then If FileSize("steam_api.dll")=0 Then InitErrorStr=InitErrorStr+ "steam_api.dll"+Chr(13)+Chr(10)
	
	If Len(InitErrorStr)>0 Then
		RuntimeError "The following DLLs were not found in the game directory:"+Chr(13)+Chr(10)+Chr(13)+Chr(10)+InitErrorStr
	EndIf
	
	If opt\SteamEnabled Then
		Local SteamResultCode% = Steam_Init()
		If SteamResultCode <> 0 Then RuntimeError("Steam API failed to initialize! Error Code: " + SteamResultCode)
	EndIf
	
End Function

Function InitGlobalVariables()
	
	gv\OptionFile$ = GetEnv("AppData")+"\scp-security-stories\options.ini"
	
	gv\WeaponFile$ = "Data\Weapons.ini"
	
	If GetEnv("ProgramFiles(X86)")>0 Then
		gv\OSBit = 64
	Else
		gv\OSBit = 32
	EndIf
	
End Function

Function InitOptions()
	
	opt\EnableSFXRelease% = GetINIInt(gv\OptionFile, "audio", "sfx release", 1)
	opt\EnableSFXRelease_Prev% = opt\EnableSFXRelease%
	opt\ConsoleEnabled% = GetINIInt(gv\OptionFile, "console", "enabled", 0)
	opt\ConsoleOpening% = GetINIInt(gv\OptionFile, "console", "auto opening", 0)
	opt\GraphicWidth% = GetINIInt(gv\OptionFile, "options", "width", DesktopWidth())
	opt\GraphicHeight% = GetINIInt(gv\OptionFile, "options", "height", DesktopHeight())
	opt\ShowFPS = GetINIInt(gv\OptionFile, "options", "show FPS", 0)
	opt\DisplayMode% = GetINIInt(gv\OptionFile, "options", "display mode", 1)
	opt\RenderCubeMapMode% = GetINIInt(gv\OptionFile, "options", "cubemaps", 2)
	opt\EnableRoomLights% = GetINIInt(gv\OptionFile, "options", "room lights enabled", 1)
	opt\TextureDetails% = GetINIInt(gv\OptionFile, "options", "texture details", 2)
	opt\TextureFiltering% = GetINIInt(gv\OptionFile, "options", "texture filtering", 2)
	opt\LauncherEnabled% = GetINIInt(gv\OptionFile, "options", "launcher enabled", 1)
	opt\TotalGFXModes% = CountGfxModes3D()
	opt\SteamEnabled = GetINIInt(gv\OptionFile, "options", "steam enabled", 1)
	
	opt\SFXVolume# = GetINIFloat(gv\OptionFile, "audio", "sound volume", 1.0)
	opt\VoiceVol# = GetINIFloat(gv\OptionFile, "audio", "voice volume", 1.0)
	opt\MasterVol# = GetINIFloat(gv\OptionFile, "audio", "master volume", 1.0)
	opt\MusicVol# = GetINIFloat(gv\OptionFile, "audio", "music volume", 1.0)
	opt\EnableSubtitles% = GetINIInt(gv\OptionFile, "audio", "enable subtitles", 0)
	
	opt\MouseSmooth# = GetINIFloat(gv\OptionFile, "options", "mouse smoothing", 1.0)
	opt\HoldToAim% = GetINIInt(gv\OptionFile, "options", "hold to aim", 1)
	opt\HoldToCrouch% = GetINIInt(gv\OptionFile, "options", "hold to crouch", 1)
	opt\IntroEnabled = GetINIInt(gv\OptionFile, "options", "intro enabled", 1)
	opt\LanguageVal = GetINIInt(gv\OptionFile, "options", "language", 0)
	opt\Menu3DEnabled = GetINIInt(gv\OptionFile, "options", "3d menu enabled", 1)
	opt\PlayStartupVideos = GetINIInt(gv\OptionFile, "options", "play startup videos", 1)
	opt\PlayerModelEnabled = GetINIInt(gv\OptionFile, "options", "enable player model", 1)
	opt\ElevatorMusicEnabled = GetINIInt(gv\OptionFile, "options", "enable elevator music", 1)
	opt\RenderScope = GetINIInt(gv\OptionFile, "options", "render scope", 1)
	
	LoadResolutions()
	
	gopt\SingleplayerGameMode = GetINIInt(gv\OptionFile, "game options", "game mode", GAMEMODE_DEFAULT)
	gopt\GameMode = gopt\SingleplayerGameMode
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D