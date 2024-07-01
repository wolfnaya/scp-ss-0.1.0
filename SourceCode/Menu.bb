
Global QuickLoadIcon% = LoadImage_Strict("GFX\menu\Quick_Loading.png")
ResizeImage(QuickLoadIcon, ImageWidth(QuickLoadIcon) * MenuScale, ImageHeight(QuickLoadIcon) * MenuScale)
Global MenuMeterIMG% = LoadImage_Strict("GFX\menu\menu_meter.jpg")

Global RandomSeed$

Global ClassDNumber
Global IsStartingFromMenu% = False

Global GamemodeName$

Global MPClick%

Global MenuBlinkTimer%[2], MenuBlinkDuration%[2]
MenuBlinkTimer[0] = 1
MenuBlinkTimer[1] = 1

Global MenuStr$, MenuStrX%, MenuStrY%, MenuNews

Global MainMenuTab%
Global PrevMainMenuTab%
Global ShouldDeleteGadgets%

Global SelectedInputBox%

LoadSaveGames()

Global CurrLoadGamePage% = 0

;Menu constants
;[Block]
Const MenuTab_Default = 0
Const MenuTab_Singleplayer = 1
Const MenuTab_Serverlist = 2
Const MenuTab_Extras = 3
Const MenuTab_NewGame = 4
Const MenuTab_LoadGame = 5
Const MenuTab_LoadMap = 6
Const MenuTab_Options_Graphics = 7
Const MenuTab_Options_Audio = 8
Const MenuTab_Options_Controls = 9
Const MenuTab_Options_Advanced = 10
Const MenuTab_Options_Controller = 11
Const MenuTab_Options_ControlsBinding = 12
Const MenuTab_HostServer = 13
Const MenuTab_MissionMode = 14
Const MenuTab_ChallengeMode = 15
Const MenuTab_Achievements = 16
Const MenuTab_Continue = 17
Const MenuTab_Lobby = 19
Const MenuTab_SelectMPMap = 20
Const MenuTab_SelectMPGamemode = 21
Const MenuTab_MPGamemodeSettings = 22
Const MenuTab_Gamemodes = 23
Const MenuTab_Chapters = 24
;[End Block]

Type MenuInstance
	Field MenuLogo.MenuLogo
	Field Cam%
	Field Sprite%
	Field SpriteAlpha#
	Field CurrentSave$
End Type

Type MenuSecrets
	Field WolfnayaButtonPressed%
	Field MPButtonPressed%
End Type

Function UpdateMainMenu()
	Local x%, y%, z%, width%, height%, temp%, i%, n%, j%
	Local se.Server
	Local mgm.MultiplayerGameMode, mfl.MapForList, sa.Save, r.Rooms
	Local category$
	
	ShowPointer()
	
	If (cpt\Current < 2 And gopt\GameMode = GAMEMODE_DEFAULT) Lor (cpt\NTFCurrent < 2 And gopt\GameMode = GAMEMODE_NTF) Lor (cpt\DCurrent < 2 And gopt\GameMode = GAMEMODE_CLASS_D) Then
		If opt\IntroEnabled Then
			If gopt\GameMode = GAMEMODE_NTF Then
				cpt\NTFCurrent = 0
			ElseIf gopt\GameMode = GAMEMODE_DEFAULT
				cpt\Current = 0
			Else
				cpt\DCurrent = 0
			EndIf
		Else
			If gopt\GameMode = GAMEMODE_NTF Then
				cpt\NTFCurrent = 1
			ElseIf gopt\GameMode = GAMEMODE_DEFAULT
				cpt\Current = 1
			Else
				cpt\DCurrent = 1
			EndIf
		EndIf
	EndIf
	
	If MouseOn(20,opt\GraphicHeight-80,175*MenuScale,20*MenuScale) And MainMenuTab = MenuTab_Default Then
		If MouseHit1 Then
			mns\WolfnayaButtonPressed = True
		EndIf
	EndIf
	
	If MainMenuOpen Then
		
		If mns\WolfnayaButtonPressed Lor Instr((CurrentDate$()), "19 Apr") Then
			ShouldPlay = MUS_SAVE_ME_FROM
		Else
			Select opt\MainMenuMusic
				Case 0
					ShouldPlay = MUS_MENU
				Case 1
					ShouldPlay = MUS_NTF
				Case 2
					ShouldPlay = MUS_UE
				Case 3
					ShouldPlay = MUS_CB
			End Select
		EndIf
		
	EndIf
	
	If Rand(300) = 1 Then
		MenuBlinkTimer[0] = Rand(4000, 8000)
		MenuBlinkDuration[0] = Rand(200, 500)
	EndIf
	
	MenuBlinkTimer[1]=MenuBlinkTimer[1]-FPSfactor
	If MenuBlinkTimer[1] < MenuBlinkDuration[1]
		If MenuBlinkTimer[1] < 0
			MenuBlinkTimer[1] = Rand(700, 800)
			MenuBlinkDuration[1] = Rand(10, 35)
		EndIf
	EndIf
	
	If (Not MouseDown1)
		OnSliderID = 0
	EndIf
	
	If PrevMainMenuTab<>MainMenuTab
		DeleteMenuGadgets()
	EndIf
	If ShouldDeleteGadgets
		DeleteMenuGadgets()
	EndIf
	PrevMainMenuTab = MainMenuTab
	ShouldDeleteGadgets = False
	
	If MainMenuTab = MenuTab_Default Then
		Local menu2dx%
		If opt\Menu3DEnabled Then
			menu2dx = 0
		Else
			menu2dx = 600 * MenuScale
		EndIf
		x = 59 * MenuScale
		y = 286 * MenuScale
		width = 400 * MenuScale
		height = 70 * MenuScale
		If MainMenuOpen Then
			;Main Menu Opened
			If DrawButtonMenu3D(x + menu2dx, y, width, height, Upper(GetLocalString("Menu", "singleplayer")), True, False, True, i) Then
				MainMenuTab = MenuTab_Singleplayer
				LoadSaveGames()
			EndIf
			If opt\SteamEnabled Then
				If DrawButtonMenu3D(x + menu2dx, y + 100 * MenuScale, width, height, Upper(GetLocalString("Menu", "multiplayer")), True, False, True, i) Then
					MainMenuTab = MenuTab_Serverlist
				EndIf
			Else
				If DrawButtonMenu3D(x + menu2dx, y + 100 * MenuScale, width, height, Upper(GetLocalString("Menu", "multiplayer")), True, False, True, i) Then
					MPClick = MPClick + 1
					If MPClick < 10 Then
						PlaySound_Strict(LoadTempSound("SFX\Player\Voice\Chat\Negative"+(Rand(1,4))+".ogg"))
					Else
						PlaySound_Strict(LoadTempSound("SFX\Player\Voice\Chat\Rude"+(Rand(1,4))+".ogg"))
						MPClick = 0
					EndIf
				EndIf
			EndIf
			If DrawButtonMenu3D(x + menu2dx, y + 200 * MenuScale, width, height, Upper(GetLocalString("Menu", "options")), True, False, True, i) Then
				MainMenuTab = MenuTab_Options_Graphics
			EndIf
			If DrawButtonMenu3D(x + menu2dx, y + 300 * MenuScale, width, height, Upper(GetLocalString("Menu", "extra")), True, False, True, i) Then
				MainMenuTab = MenuTab_Extras
			EndIf
			If DrawButtonMenu3D(x + menu2dx, y + 400 * MenuScale, width, height, Upper(GetLocalString("Menu", "quit")), True, False, True, i) Then
				StopStream_Strict(MusicCHN)
				If opt\SteamEnabled Then Steam_Shutdown()
				End
			EndIf
		ElseIf gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
			;Singleplayer Pause Menu Opened
			If DrawButtonMenu3D(x, y, width, height, Upper(GetLocalString("Menu", "resume")), True, False, True, i) Then
				MenuOpen = False
				ResumeSounds()
				DeleteMenuGadgets()
				ResetInput()
				Return
			EndIf
			If DrawButtonMenu3D(x, y + 100 * MenuScale, width, height, Upper(GetLocalString("Menu", "loadgame")), True, False, True, i) Then
				If GameSaved And (Not SelectedDifficulty\PermaDeath) Then
					DrawLoading(0)
					
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
						x = Abs(EntityX(Collider) - EntityX(r\obj))
						z = Abs(EntityZ(Collider) - EntityZ(r\obj))
						
						If x < 12.0 And z < 12.0 Then
							MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)] = Max(MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)], 1)
							If x < 4.0 And z < 4.0 Then
								If Abs(EntityY(Collider) - EntityY(r\obj)) < 1.5 Then PlayerRoom = r
								MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)] = 1
							EndIf
						EndIf
					Next
					
					DrawLoading(100,True)
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
			If DrawButtonMenu3D(x, y + 200 * MenuScale, width, height, Upper(GetLocalString("Menu", "options")), True, False, True, i) Then
				MainMenuTab = MenuTab_Options_Graphics
			EndIf
			If DrawButtonMenu3D(x, y + 300 * MenuScale, width, height, Upper(GetLocalString("Menu", "achievements")), True, False, True, i) Then
				MainMenuTab = MenuTab_Achievements
			EndIf
			If DrawButtonMenu3D(x, y + 400 * MenuScale, width, height, Upper(GetLocalString("Menu", "quit_to_menu")), True, False, True, i) Then ;y + 400
				MainMenuOpen = True
				
				LoadChaptersValueFile()
				
				NullGame()
				MenuOpen = False
				MainMenuTab = 0
				CurrSave = Null
				ResetInput()
				Return
			EndIf
		Else
			;Multiplayer Pause Menu Opened
			If DrawButtonMenu3D(x, y, width, height, Upper(GetLocalString("Menu", "resume")), True, False, True, i) Then
				MenuOpen = False
				ResumeSounds()
				DeleteMenuGadgets()
				ResetInput()
				Return
			EndIf
			If DrawButtonMenu3D(x, y + 100 * MenuScale, width, height, Upper(GetLocalString("Menu", "options")), True, False, True, i) Then
				MainMenuTab = MenuTab_Options_Graphics
			EndIf
			If DrawButtonMenu3D(x, y + 200 * MenuScale, width, height, Upper(GetLocalString("Menu", "achievements")), True, False, True, i) Then
				MainMenuTab = MenuTab_Achievements
			EndIf
			If DrawButtonMenu3D(x, y + 300 * MenuScale, width, height, Upper(GetLocalString("Menu", "disconnect")), True, False, True, i) Then
				LeaveMPGame(True)
				Return
			EndIf
		EndIf
		
		;UpdateMenuControllerSelection(6,0,0)
	Else
		
		x = 59 * MenuScale
		y = 286 * MenuScale
		
		width = 400 * MenuScale
		height = 70 * MenuScale
		
		Local pressedbutton%=False
		If MainMenuTab = MenuTab_NewGame Then
			If DrawButton(x + width + 20 * MenuScale, y, 580 * MenuScale - width - 20 * MenuScale, height, Upper(GetLocalString("Menu", "back")), False, False, True, 0, MainMenuTab, 0) Then
				pressedbutton = True
			EndIf
		ElseIf MainMenuTab = MenuTab_Lobby Then
			If DrawButton(x + width + 20 * MenuScale, y, 580 * MenuScale - width - 20 * MenuScale, height, Upper(GetLocalString("Menu", "disconnect")), False, False, True, 0, MainMenuTab, co\CurrButtonSub[MainMenuTab]) Then
				pressedbutton = True
				Disconnect()
			EndIf
		ElseIf MainMenuTab = MenuTab_Serverlist Then
			If mp_I\ServerMSG = SERVER_MSG_NONE Lor mp_I\ServerMSG = SERVER_MSG_OFFLINE Then
				If DrawButton(x + width + 20 * MenuScale, y, 580 * MenuScale - width - 20 * MenuScale, height, Upper(GetLocalString("Menu", "back")), False, False, True, 0, MainMenuTab, co\CurrButtonSub[MainMenuTab]) Then
					pressedbutton = True
				EndIf
			EndIf
		Else
			If DrawButton(x + width + 20 * MenuScale, y, 580 * MenuScale - width - 20 * MenuScale, height, Upper(GetLocalString("Menu", "back")), False, False, True, 0, MainMenuTab, co\CurrButtonSub[MainMenuTab]) Then
				pressedbutton = True
			EndIf
		EndIf
		If co\Enabled Then
			If JoyHit(CKM_Back) Then
				pressedbutton = True
				PlaySound_Strict ButtonSFX[2]
			EndIf
		EndIf
		
		If pressedbutton Then
			co\CurrButtonSub[MainMenuTab] = 0
			Select MainMenuTab
				Case MenuTab_NewGame
					SaveOptionsINI()
					MainMenuTab = MenuTab_Gamemodes
					LoadSaveGames()
				Case MenuTab_Gamemodes
					SaveOptionsINI()
					MainMenuTab = MenuTab_Singleplayer
					LoadSaveGames()
				Case MenuTab_Options_Graphics,MenuTab_Options_Audio,MenuTab_Options_Controls,MenuTab_Options_Advanced;,MenuTab_Options_Multiplayer ;save the options
					SaveOptionsINI()
					
					UserTrackCheck% = 0
					UserTrackCheck2% = 0
					
					MainMenuTab = MenuTab_Default
				Case MenuTab_Options_ControlsBinding
					SaveOptionsINI()
					MainMenuTab = MenuTab_Options_Controls
				Case MenuTab_Chapters
					MainMenuTab = MenuTab_NewGame
					MouseHit1 = False
				Case MenuTab_Achievements
					If (MainMenuTab <> MenuTab_Achievements) Lor MainMenuOpen Then
						MainMenuTab = MenuTab_Extras
					Else
						MainMenuTab = MenuTab_Default
					EndIf
					ScrollMenuHeight = 0
					ScrollBarY = 0
					co\ScrollBarY = 0
					CurrLoadGamePage = 0
				Case MenuTab_Options_Controller
					MainMenuTab = MenuTab_Options_Controls
				Case MenuTab_LoadGame,MenuTab_MissionMode,MenuTab_ChallengeMode
					MainMenuTab = MenuTab_Singleplayer
					CurrLoadGamePage = 0
					LoadSaveGames()
				Case MenuTab_Lobby
					If mp_I\PlayState = GAME_SERVER Then
						MainMenuTab = MenuTab_HostServer
						;If mp_O\LocalServer=False Then
						;	RemoveServer(Steam_GetPlayerIDLower(), Steam_GetPlayerIDUpper())
						;EndIf
					Else
						MainMenuTab = MenuTab_Serverlist
					EndIf
					mp_I\PlayState = 0
					mp_I\HasRefreshed = False
				Case MenuTab_HostServer
					mp_I\PasswordVisible = False
					MainMenuTab = MenuTab_Serverlist
				Case MenuTab_Serverlist
					DelSave = Null
					SaveMPOptions()
					mp_I\HasRefreshed = False
					mp_I\PasswordVisible = False
					MainMenuTab = MenuTab_Default
				Case MenuTab_SelectMPMap,MenuTab_SelectMPGamemode,MenuTab_MPGamemodeSettings
					MainMenuTab = MenuTab_HostServer
				Default
					MainMenuTab = MenuTab_Default
			End Select
			co\PressedButton = False
		EndIf
		
		Select MainMenuTab
			Case MenuTab_Continue
				;[Block]
				For sa = Each Save
					If sa\Name = m_I\CurrentSave Then
						CurrSave = sa
						MainMenuOpen = False
						ResetControllerSelections()
						Null3DMenu()
						gopt\GameMode = sa\Gamemode
						ClassDNumber = sa\DNumber
						gopt\CurrZoneString = sa\ZoneString
						IsStartingFromMenu = True
						LoadEntities()
						LoadAllSounds()
						LoadGame(SavePath + CurrSave\Name + "\")
						InitLoadGame()
						Exit
					EndIf
				Next
				;[End Block]
			Case MenuTab_NewGame
				;[Block]
				If SelectedDifficulty\Customizable And co\CurrButton[1]=7 And co\CurrButtonSub[1]=0
					UpdateMenuControllerSelection(9,1,2,2)
				ElseIf SelectedDifficulty\Customizable And co\CurrButtonSub[1]=1
					UpdateMenuControllerSelection(4,1,2,2)
					If co\CurrButton[1]=7 Then co\CurrButton[1]=0
					If co\CurrButtonSub[1]=0 Then co\CurrButton[1]=7
				ElseIf co\CurrButton[1]=8
					UpdateMenuControllerSelection(9,1,2,2)
					If co\CurrButton[1]<>8 Then co\CurrButtonSub[1]=0
				Else
					Local prev = co\CurrButton[1]
					UpdateMenuControllerSelection(9,1,0)
					If co\CurrButton[1]=8 And prev>0
						co\CurrButtonSub[1]=1
					EndIf
				EndIf
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 330 * MenuScale
				
				IsStartingFromMenu = True
				
				CurrSave\Name = InputBox(x + 150 * MenuScale, y + 15 * MenuScale, 200 * MenuScale, 30 * MenuScale, CurrSave\Name, 1, 15, 1, MainMenuTab)
				If SelectedInputBox = 1 Then
					CurrSave\Name = Replace(CurrSave\Name,":","")
					CurrSave\Name = Replace(CurrSave\Name,".","")
					CurrSave\Name = Replace(CurrSave\Name,"/","")
					CurrSave\Name = Replace(CurrSave\Name,"\","")
					CurrSave\Name = Replace(CurrSave\Name,">","")
					CurrSave\Name = Replace(CurrSave\Name,"<","")
					CurrSave\Name = Replace(CurrSave\Name,"|","")
					CurrSave\Name = Replace(CurrSave\Name,Chr(34),"")
					CurrSave\Name = Replace(CurrSave\Name,"*","")
					CurrSave\Name = Replace(CurrSave\Name,"?","")
					CursorPos = Min(CursorPos, Len(CurrSave\Name))
				EndIf
				
				RandomSeed = InputBox(x+150*MenuScale, y+55*MenuScale, 200*MenuScale, 30*MenuScale, RandomSeed, 3, 15, 2, MainMenuTab)
					
				opt\IntroEnabled = DrawTick(x + 280 * MenuScale, y + 110 * MenuScale, opt\IntroEnabled, False, 3, MainMenuTab)
				
				For i = SAFE To ESOTERIC
					Local PrevSelectedDifficulty.Difficulty = SelectedDifficulty
					If DrawTick(x + 20 * MenuScale, y + (180+30*i) * MenuScale, (SelectedDifficulty = difficulties[i]),False,(i+4),MainMenuTab) Then SelectedDifficulty = difficulties[i]
					If PrevSelectedDifficulty<>SelectedDifficulty
						If PrevSelectedDifficulty = difficulties[ESOTERIC]
							ShouldDeleteGadgets=True
						EndIf
					EndIf
				Next
				
				If SelectedDifficulty\Customizable
					SelectedDifficulty\PermaDeath =  DrawTick(x + 160 * MenuScale, y + 165 * MenuScale, (SelectedDifficulty\PermaDeath), False, 0, MainMenuTab, 1)
					
					If DrawTick(x + 160 * MenuScale, y + 195 * MenuScale, SelectedDifficulty\SaveType = SAVEANYWHERE And (Not SelectedDifficulty\PermaDeath), SelectedDifficulty\PermaDeath, 1, MainMenuTab, 1) Then 
						SelectedDifficulty\SaveType = SAVEANYWHERE
					Else
						SelectedDifficulty\SaveType = SAVEONSCREENS
					EndIf
					
					SelectedDifficulty\AggressiveNPCs =  DrawTick(x + 160 * MenuScale, y + 225 * MenuScale, SelectedDifficulty\AggressiveNPCs, False, 2, MainMenuTab, 1)
					
					;Other factor's difficulty
					If (Not co\Enabled)
						If MouseHit1
							If ImageRectOverlap(I_MIG\ArrowIMG[1],x + 155 * MenuScale, y+251*MenuScale, ScaledMouseX(),ScaledMouseY(),0,0)
								If SelectedDifficulty\OtherFactors < IMPOSSIBLE
									SelectedDifficulty\OtherFactors = SelectedDifficulty\OtherFactors + 1
								Else
									SelectedDifficulty\OtherFactors = EASY
								EndIf
								PlaySound_Strict(ButtonSFX[2])
							EndIf
						EndIf
					Else
						If co\CurrButton[MainMenuTab]=3 And co\CurrButtonSub[MainMenuTab]=1
							If co\PressedButton
								If SelectedDifficulty\OtherFactors < IMPOSSIBLE
									SelectedDifficulty\OtherFactors = SelectedDifficulty\OtherFactors + 1
								Else
									SelectedDifficulty\OtherFactors = EASY
								EndIf
								PlaySound_Strict(ButtonSFX[2])
							EndIf
						EndIf
					EndIf
				EndIf
				
				If DrawButton(x + 420 * MenuScale, y + height + 45 * MenuScale, 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "start")), False, False, True, 8, MainMenuTab, 0) Then
					If CurrSave\Name = "" Then CurrSave\Name = "untitled"
					
					If RandomSeed = "" Then
						RandomSeed = Abs(MilliSecs())
					EndIf
					
					IsStartingFromMenu = True
					
					SeedRnd GenerateSeedNumber(RandomSeed)
					
					MainMenuOpen = False
					
					Local SameFound% = 0
					Local LowestPossible% = 2
					
					Local I_SAV.Save
					
					For I_SAV.Save = Each Save
						If (CurrSave <> I_SAV And CurrSave\Name = I_SAV\Name) Then SameFound = 1 : Exit
					Next
					
					While SameFound = 1
						SameFound = 2
						For I_SAV.Save = Each Save
							If (I_SAV\Name = (CurrSave\Name + " (" + LowestPossible + ")")) Then LowestPossible = LowestPossible + 1 : SameFound = True : Exit
						Next
					Wend
					
					If SameFound = 2 Then CurrSave\Name = CurrSave\Name + " (" + LowestPossible + ")"
					
					ResetControllerSelections()
					Null3DMenu()
					
					If gopt\GameMode = GAMEMODE_DEFAULT Then
						
						If cpt\Current = 0 Then
							gopt\CurrZone = GATE_D_TOPSIDE
						ElseIf cpt\Current = 1 Lor cpt\Current = 7 Then
							gopt\CurrZone = LCZ
						ElseIf cpt\Current = 2 Lor cpt\Current = 3 Lor cpt\Current = 4 Lor cpt\Current = 5 Lor cpt\Current = 6 Lor cpt\Current = 8 Then
							gopt\CurrZone = HCZ
						ElseIf cpt\Current = 9 Then
							If cpt\SurfaceEnding = GATE_A_TOPSIDE Then
								gopt\CurrZone = GATE_A_TOPSIDE
							ElseIf cpt\SurfaceEnding = GATE_B_TOPSIDE Then
								gopt\CurrZone = GATE_B_TOPSIDE
							ElseIf cpt\SurfaceEnding = GATE_C_TOPSIDE Then
								gopt\CurrZone = GATE_C_TOPSIDE
							ElseIf cpt\SurfaceEnding = GATE_D_TOPSIDE Then
								gopt\CurrZone = GATE_D_TOPSIDE
							Else
								gopt\CurrZone = EZ
							EndIf
						EndIf
					ElseIf gopt\GameMode = GAMEMODE_NTF Then
						If opt\IntroEnabled
							gopt\CurrZone% = GATE_A_INTRO
						Else
							If cpt\NTFCurrent = 1 Then
								gopt\CurrZone = EZ
							ElseIf cpt\NTFCurrent = 2 Then
								gopt\CurrZone = HCZ
							EndIf
						EndIf
					Else
						If opt\IntroEnabled
							gopt\CurrZone = CLASSD_CELLS
						Else
							If cpt\DCurrent = 1 Then
								gopt\CurrZone = LCZ
							EndIf
						EndIf
					EndIf
					
					LoadEntities()
					LoadAllSounds()
					InitNewGame()
					FlushKeys()
					FlushMouse()
					FlushJoy()
					
					SaveOptionsINI()
				EndIf
				
				If DrawButton(x, y + height + 45 * MenuScale, 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "chapters")), False, False, True, 8, MainMenuTab, 0) Then MainMenuTab = MenuTab_Chapters
				;[End Block]
			Case MenuTab_LoadGame
				;[Block]
				If co\CurrButton[2]=0
					;co\CurrButtonSub[2]=0
					UpdateMenuControllerSelection(1+SaveGameAmount,2,0)
				Else
					If DelSave = Null
						UpdateMenuControllerSelection(1+SaveGameAmount,2,2,2)
					Else
						UpdateMenuControllerSelection(1+SaveGameAmount,2,3,2)
					EndIf
				EndIf
				
				y = 286 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 296 * MenuScale
				
				If CurrLoadGamePage < Ceil(Float(SaveGameAmount)/6.0)-1 And DelSave = Null Then 
					If DrawButton(x+530*MenuScale, y + 510*MenuScale, 51*MenuScale, 55*MenuScale, ">", 2) Then
						CurrLoadGamePage = CurrLoadGamePage+1
						ShouldDeleteGadgets = True
					EndIf
				EndIf
				If CurrLoadGamePage > 0 And DelSave = Null Then
					If DrawButton(x, y + 510*MenuScale, 51*MenuScale, 55*MenuScale, "<", 2) Then
						CurrLoadGamePage = CurrLoadGamePage-1
						ShouldDeleteGadgets = True
					EndIf
				EndIf
				
				If CurrLoadGamePage > Ceil(Float(SaveGameAmount)/6.0)-1 Then
					CurrLoadGamePage = CurrLoadGamePage - 1
					ShouldDeleteGadgets = True
				EndIf
				
				If SaveGameAmount = 0 Then
					
				Else
					x = x + 20 * MenuScale
					y = y + 20 * MenuScale
					
					CurrSave = First Save
					
					For i% = 0 To 5+(6*CurrLoadGamePage)
						If i > 0 Then CurrSave = After CurrSave
						If CurrSave = Null Then Exit
						If i >= (6*CurrLoadGamePage) Then
							If DelSave = Null Then
								If CurrSave\Version <> CompatibleNumber Then
									
								Else
									If DrawButton(x + 280 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale, GetLocalString("Menu", "load"), False) Then
										MainMenuOpen = False
										ResetControllerSelections()
										Null3DMenu()
										gopt\GameMode = CurrSave\Gamemode
										ClassDNumber = CurrSave\DNumber
										gopt\CurrZoneString = CurrSave\ZoneString
										IsStartingFromMenu = True
										LoadEntities()
										LoadAllSounds()
										LoadGame(SavePath + CurrSave\Name + "\")
										InitLoadGame()
										Exit
									EndIf
								EndIf
								
								If DrawButton(x + 400 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale, GetLocalString("Menu", "delete"), False) Then
									DelSave = CurrSave
									Exit
								EndIf
							EndIf
							
							If CurrSave = Last Save Then
								Exit
							EndIf
							
							y = y + 80 * MenuScale
						EndIf
					Next
					
					If DelSave <> Null
						x = 640 * MenuScale
						y = 376 * MenuScale
						DrawFrame(x, y, 420 * MenuScale, 200 * MenuScale)
						If DrawButton(x + 50 * MenuScale, y + 150 * MenuScale, 100 * MenuScale, 30 * MenuScale, GetLocalString("Menu", "yes"), False) Then
							
							DeleteGame(DelSave)
							DelSave = Null
							LoadSaveGames()
							ShouldDeleteGadgets = True
						EndIf
						If DrawButton(x + 250 * MenuScale, y + 150 * MenuScale, 100 * MenuScale, 30 * MenuScale, GetLocalString("Menu", "no"), False) Then
							DelSave = Null
							ShouldDeleteGadgets = True
						EndIf
					EndIf
				EndIf
				;[End Block]
			Case MenuTab_Options_Graphics,MenuTab_Options_Audio,MenuTab_Options_Controls,MenuTab_Options_Advanced,MenuTab_Options_Controller,MenuTab_Options_ControlsBinding;,MenuTab_Options_Multiplayer
				;[Block]
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				If MainMenuTab<>MenuTab_Options_Controller And MainMenuTab<>MenuTab_Options_ControlsBinding Then
					x = 60 * MenuScale
					y = y + height + 20 * MenuScale
					width = 580 * MenuScale
					height = 60 * MenuScale
					Local prevButton = co\CurrButton[MainMenuTab]
					Local prevButtonSub = co\CurrButtonSub[MainMenuTab]
					
					If DrawButton(x+20*MenuScale,y+15*MenuScale,width/5,height/2, Upper(GetLocalString("Options", "graphics")), False,False,True,1,MainMenuTab,0) Then MainMenuTab = MenuTab_Options_Graphics : co\CurrButtonSub[MainMenuTab]=0 : co\CurrButton[MainMenuTab]=1
					If DrawButton(x+160*MenuScale,y+15*MenuScale,width/5,height/2, Upper(GetLocalString("Options", "audio")), False,False,True,1,MainMenuTab,1) Then MainMenuTab = MenuTab_Options_Audio : co\CurrButtonSub[MainMenuTab]=1 : co\CurrButton[MainMenuTab]=1
					If DrawButton(x+300*MenuScale,y+15*MenuScale,width/5,height/2, Upper(GetLocalString("Options", "controls")), False,False,True,1,MainMenuTab,2) Then MainMenuTab = MenuTab_Options_Controls : co\CurrButtonSub[MainMenuTab]=2 : co\CurrButton[MainMenuTab]=1
					If DrawButton(x+440*MenuScale,y+15*MenuScale,width/5,height/2, Upper(GetLocalString("Options", "advanced")), False,False,True,1,MainMenuTab,3) Then MainMenuTab = MenuTab_Options_Advanced : co\CurrButtonSub[MainMenuTab]=3 : co\CurrButton[MainMenuTab]=1
					
					y = y + 70 * MenuScale
				Else
					UpdateMenuControllerSelection(1,3,0)
					
					x = 60 * MenuScale
					y = y + height + 20 * MenuScale
					width = 580 * MenuScale
					height = 330 * MenuScale
					y = y + 30 * MenuScale
				EndIf
				
				If MainMenuTab <> MenuTab_Options_Audio Then
					UserTrackCheck% = 0
					UserTrackCheck2% = 0
				EndIf
				
				Local tx# = x+width
				Local ty# = y
				Local tw# = 400*MenuScale
				Local th# = 150*MenuScale
				
				If MainMenuTab = MenuTab_Options_Graphics Then
					;[Block]
					height = 390 * MenuScale
					
					y=y+25*MenuScale
					ScreenGamma = (SlideBar(x + 310*MenuScale, y, 150*MenuScale, (ScreenGamma-0.5)*100.0,2,MainMenuTab,0,Upper(GetLocalString("Options", "low")),Upper(GetLocalString("Options", "high")))/100.0)+0.5
					
					y=y+35*MenuScale
                    FOV = (SlideBar(x + 310*MenuScale, y,150*MenuScale, (FOV-40)*2.0,3,MainMenuTab,0,40,90)/2.0)+40
					
					y=y+35*MenuScale
					Local hasFrameLimit = (CurrFrameLimit>0.0)
					
					If DrawTick(x + (215+(160*(Not hasFrameLimit))) * MenuScale, y, CurrFrameLimit > 0.0) Then
						CurrFrameLimit = Max((SlideBar(x + 310*MenuScale, y, 150*MenuScale, CurrFrameLimit#*87,4,MainMenuTab,0,30,144)/87), 0.01)
						Framelimit = 29 + (CurrFrameLimit * 100.0)
					Else
						CurrFrameLimit = 0.0
						Framelimit = 0
					EndIf
					
					If hasFrameLimit And hasFrameLimit<>CurrFrameLimit Then
						ShouldDeleteGadgets = True
					EndIf
					
					y=y+35*MenuScale
					Vsync% = DrawTick(x + 375 * MenuScale, y + MenuScale, Vsync%, False, 5, MainMenuTab, 0)
					
					y=y+35*MenuScale
					opt\TextureDetails = DrawDropdown(x+315*MenuScale,y,opt\TextureDetails,6,Upper(GetLocalString("Options", "low")),Upper(GetLocalString("Options", "medium")),Upper(GetLocalString("Options", "high")))
					Select opt\TextureDetails%
						Case 0
							TextureFloat# = 0.8
						Case 1
							TextureFloat# = 0.0
						Case 2
							TextureFloat# = -0.8
					End Select
					TextureLodBias TextureFloat
					
					y=y+35*MenuScale
					opt\TextureFiltering = DrawDropdown(x+315*MenuScale,y,opt\TextureFiltering,7,"2x","4x","8x","16x")
					TextureAnisotropic 2^(opt\TextureFiltering+1)
					
					y=y+35*MenuScale
					ParticleAmount = DrawDropdown(x+315*MenuScale,y,ParticleAmount,8,Upper(GetLocalString("Options", "low")),Upper(GetLocalString("Options", "medium")),Upper(GetLocalString("Options", "high")))
					
					y=y+35*MenuScale
					opt\RenderCubeMapMode = DrawDropdown(x+315*MenuScale,y,opt\RenderCubeMapMode,9,Upper(GetLocalString("Options", "off")),Upper(GetLocalString("Options", "low")),Upper(GetLocalString("Options", "high")))
					
					y=y+45*MenuScale
					Local CurrBumpEnabled = BumpEnabled
					BumpEnabled = DrawTick(x + 375 * MenuScale, y, BumpEnabled, (Not MainMenuOpen), 10, MainMenuTab, 0)
					If CurrBumpEnabled<>BumpEnabled Then
						Reload()
					EndIf
					
					y=y+35*MenuScale
					opt\EnableRoomLights = DrawTick(x + 375 * MenuScale, y, opt\EnableRoomLights, False, 11, MainMenuTab, 0)
					
					;[End Block]
				ElseIf MainMenuTab = MenuTab_Options_Audio Then
					;[Block]
					height = 260 * MenuScale
					
					y = y + 25*MenuScale
					
					aud\MasterVol = (SlideBar(x + 310*MenuScale, y, 150*MenuScale, opt\MasterVol*100.0,2,MainMenuTab,0)/100.0)
					opt\MasterVol = aud\MasterVol
					
					y = y + 35*MenuScale
					
					aud\MusicVol = (SlideBar(x + 310*MenuScale, y, 150*MenuScale, opt\MusicVol*100.0,3,MainMenuTab,0)/100.0)
					opt\MusicVol = aud\MusicVol
					
					y = y + 35*MenuScale
					
					aud\EnviromentVol = (SlideBar(x + 310*MenuScale, y, 150*MenuScale, opt\SFXVolume*100.0,4,MainMenuTab,0)/100.0)
					opt\SFXVolume = aud\EnviromentVol
					
					y = y + 35*MenuScale
					
					aud\VoiceVol = (SlideBar(x + 310*MenuScale, y, 150*MenuScale, opt\VoiceVol*100.0,5,MainMenuTab,0)/100.0)
					opt\VoiceVol = aud\VoiceVol
					
					y = y + 35*MenuScale
					
					opt\EnableSFXRelease = DrawTick(x + 375 * MenuScale, y + MenuScale, opt\EnableSFXRelease, False, 6, MainMenuTab, 0)
					If opt\EnableSFXRelease_Prev% <> opt\EnableSFXRelease
						If opt\EnableSFXRelease%
							Local snd.Sound
							For snd.Sound = Each Sound
								For i = 0 To MaxChannelsAmount - 1
									If snd\channels[i]<>0 Then
										If ChannelPlaying(snd\channels[i]) Then
											StopChannel(snd\channels[i])
										EndIf
									EndIf
								Next
								If snd\internalHandle<>0 Then
									FreeSound snd\internalHandle
									snd\internalHandle = 0
								EndIf
								snd\releaseTime = 0
							Next
						Else
							For snd.Sound = Each Sound
								If snd\internalHandle = 0 Then snd\internalHandle = LoadSound(snd\name)
							Next
						EndIf
						opt\EnableSFXRelease_Prev% = opt\EnableSFXRelease
					EndIf
					
					y = y + 35*MenuScale
					
					Local PrevEnableSubtitles% = opt\EnableSubtitles
					;Local PrevOverrideSubColor% = opt\OverrideSubColor
					
					opt\EnableSubtitles = DrawTick(x + 375 * MenuScale, y, opt\EnableSubtitles, (Not MainMenuOpen), 10, MainMenuTab, 0)
					If PrevEnableSubtitles <> opt\EnableSubtitles
						If opt\EnableSubtitles Then ClearSubtitles()
					EndIf
					
;					If opt\EnableSubtitles
;						y = y + (30 * MenuScale)
;						
;						opt\OverrideSubColor = UpdateMenuTick(x, y, opt\OverrideSubColor)
;					EndIf
;					
;					If PrevEnableSubtitles Lor PrevOverrideSubColor Then ShouldDeleteGadgets = (PrevEnableSubtitles <> opt\EnableSubtitles) Lor (PrevOverrideSubColor <> opt\OverrideSubColor)
;					
;					If opt\EnableSubtitles And opt\OverrideSubColor
;						y = y + (35 * MenuScale)
;						
;						UpdateMenuPalette(x - (43 * MenuScale), y + (15 * MenuScale))
;						
;						y = y + (30 * MenuScale)
;						
;						opt\SubColorR = Min(UpdateMenuInputBox(x - (115 * MenuScale), y, 40 * MenuScale, 20 * MenuScale, Str(opt\SubColorR), Font_Default, 14, 3), 255.0)
;						
;						y = y + (30 * MenuScale)
;						
;						opt\SubColorG = Min(UpdateMenuInputBox(x - (115 * MenuScale), y, 40 * MenuScale, 20 * MenuScale, Str(opt\SubColorG), Font_Default, 15, 3), 255.0)
;						
;						y = y + (30 * MenuScale)
;						
;						opt\SubColorB = Min(UpdateMenuInputBox(x - (115 * MenuScale), y, 40 * MenuScale, 20 * MenuScale, Str(opt\SubColorB), Font_Default, 16, 3), 255.0)
;					EndIf
					;[End Block]
				ElseIf MainMenuTab = MenuTab_Options_Controls Then
					;[Block]
					
					height = 320 * MenuScale
					
					y = y + 25*MenuScale
					MouseSens = (SlideBar(x + 310*MenuScale, y, 150*MenuScale, (MouseSens+0.5)*100.0)/100.0)-0.5
					
					y = y + 35*MenuScale
					opt\MouseSmooth = (SlideBar(x + 310*MenuScale, y, 150*MenuScale, (opt\MouseSmooth)*100.0)/100.0)
					
					y = y + 35*MenuScale
					InvertMouse = DrawTick(x + 375 * MenuScale, y, InvertMouse)
					
					y = y + 35*MenuScale
					opt\HoldToAim = DrawTick(x + 375 * MenuScale, y, opt\HoldToAim)
					
					y = y + 35*MenuScale
					opt\HoldToCrouch = DrawTick(x + 375 * MenuScale, y, opt\HoldToCrouch)
					
					y = y + 50*MenuScale
					
					If DrawButton(x+20*MenuScale,y,220*MenuScale,30*MenuScale,GetLocalString("Options","control_configuration"),False) Then
						MainMenuTab = MenuTab_Options_ControlsBinding
					EndIf	
						
					;[End Block]
				ElseIf MainMenuTab = MenuTab_Options_ControlsBinding Then
					;[Block]
					InputBox(x + 320 * MenuScale, y + 20 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_UP,210)],5)
					InputBox(x + 320 * MenuScale, y + 40 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_DOWN,210)],6)
					InputBox(x + 320 * MenuScale, y + 60 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_LEFT,210)],3)
					InputBox(x + 320 * MenuScale, y + 80 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_RIGHT,210)],4)
					InputBox(x + 320 * MenuScale, y + 100 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_CROUCH,210)],10)
					InputBox(x + 320 * MenuScale, y + 120 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_SPRINT,210)],8)
					
					InputBox(x + 320 * MenuScale, y + 160 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_HOLSTERGUN,210)],17)
					InputBox(x + 320 * MenuScale, y + 180 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_RELOAD,210)],13)
					InputBox(x + 320 * MenuScale, y + 200 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_ATTACHMENTS,210)],22)
					
					InputBox(x + 320 * MenuScale, y + 240 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_BLINK,210)],7)
					InputBox(x + 320 * MenuScale, y + 260 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_INV,210)],9)
					InputBox(x + 320 * MenuScale, y + 280 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_USE,210)],19)
					
					InputBox(x + 320 * MenuScale, y + 320 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(kb\ChatKey,210)],21)
					InputBox(x + 320 * MenuScale, y + 340 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(kb\CommandWheelKey,210)],14)
					InputBox(x + 320 * MenuScale, y + 360 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(kb\SocialWheelKey,210)],15)
					
					InputBox(x + 320 * MenuScale, y + 400 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_CONSOLE,210)],12)
					InputBox(x + 320 * MenuScale, y + 420 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_SAVE,210)],11)
					InputBox(x + 320 * MenuScale, y + 440 * MenuScale,140*MenuScale,20*MenuScale,KeyName[Min(KEY_LOAD,210)],23)
					
					Local KEY%
					If SelectedInputBox <> 0 Then
						For i = 0 To 227
							If KeyHit(i) Then KEY = i : Exit
						Next
					EndIf	
					If KEY<>0 Then
						Select SelectedInputBox
							Case 3
								KEY_LEFT = KEY
							Case 4
								KEY_RIGHT = KEY
							Case 5
								KEY_UP = KEY
							Case 6
								KEY_DOWN = KEY
							Case 7
								KEY_BLINK = KEY
							Case 8
								KEY_SPRINT = KEY
							Case 9
								KEY_INV = KEY
							Case 10
								KEY_CROUCH = KEY
							Case 11
								KEY_SAVE = KEY
							Case 12
								KEY_CONSOLE = KEY
							Case 13
								KEY_RELOAD = KEY
							Case 14
								kb\CommandWheelKey = KEY
							Case 15
								kb\SocialWheelKey = KEY
							Case 16
								kb\NVToggleKey = KEY
							Case 17
								KEY_HOLSTERGUN = KEY
							Case 18
								KEY_RADIOTOGGLE = KEY
							Case 19
								KEY_USE = KEY
							Case 21
								kb\ChatKey = KEY
							Case 22
								KEY_ATTACHMENTS = KEY
							Case 23
								KEY_LOAD = KEY
						End Select
						SelectedInputBox = 0
						KEY = 0
					EndIf
					;[End block]
				ElseIf MainMenuTab = MenuTab_Options_Advanced Then
					;[Block]
					height = 390 * MenuScale
					
					y = y + 25*MenuScale
					HUDenabled = DrawTick(x + 375 * MenuScale, y, HUDenabled,False,2,MainMenuTab,0)
					
					y = y + 35*MenuScale
					opt\ShowFPS% = DrawTick(x + 375 * MenuScale, y, opt\ShowFPS%,False,6,MainMenuTab,0)
					
					y = y + 35*MenuScale
					opt\ConsoleEnabled = DrawTick(x + 375 * MenuScale, y, opt\ConsoleEnabled,False,3,MainMenuTab,0)
					
					;[End Block]
				EndIf
				;[End Block]
			Case MenuTab_Chapters
				;[Block]
				
				y = 286 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 296 * MenuScale
				
				If CurrLoadGamePage < 9 Then
					If DrawButton(x+530*MenuScale, y + 490*MenuScale, 51*MenuScale, 55*MenuScale, ">") Then
						CurrLoadGamePage = CurrLoadGamePage+1
						ShouldDeleteGadgets = True
					EndIf
				EndIf
				If CurrLoadGamePage > 0 Then
					If DrawButton(x, y + 490*MenuScale, 51*MenuScale, 55*MenuScale, "<") Then
						CurrLoadGamePage = CurrLoadGamePage-1
						ShouldDeleteGadgets = True
					EndIf
				EndIf
				
				If CurrLoadGamePage > 9 Then
					CurrLoadGamePage = CurrLoadGamePage - 1
					ShouldDeleteGadgets = True
				EndIf
				
				If DrawButton(x + 820 * MenuScale, y + 120 * MenuScale , 360 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "reset_chapter_saves")), False, False, True, 8, MainMenuTab, 0) Then
					cpt\Unlocked = 0
					cpt\NTFUnlocked = 0
					cpt\DUnlocked = 0
				EndIf
				
				Select CurrLoadGamePage
					Case 0
						If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
							If gopt\GameMode = GAMEMODE_NTF Then
								cpt\NTFCurrent = 0
							ElseIf gopt\GameMode = GAMEMODE_DEFAULT
								cpt\Current = 0
							Else
								cpt\DCurrent = 0
							EndIf
							opt\IntroEnabled = True
							MainMenuTab = MenuTab_NewGame
						EndIf
					Case 1
						If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
							If gopt\GameMode = GAMEMODE_NTF Then
								cpt\NTFCurrent = 1
							ElseIf gopt\GameMode = GAMEMODE_DEFAULT
								cpt\Current = 1
							Else
								cpt\DCurrent = 1
							EndIf
							opt\IntroEnabled = False
							MainMenuTab = MenuTab_NewGame
						EndIf
					Case 2
						If (cpt\Unlocked > 1 And gopt\GameMode = GAMEMODE_DEFAULT) Lor (cpt\NTFUnlocked > 1 And gopt\GameMode = GAMEMODE_NTF) Lor (cpt\DUnlocked > 1 And gopt\GameMode = GAMEMODE_CLASS_D) Then
							If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
								If gopt\GameMode = GAMEMODE_NTF Then
									cpt\NTFCurrent = 2
								ElseIf gopt\GameMode = GAMEMODE_DEFAULT
									cpt\Current = 2
								Else
									cpt\DCurrent = 2
								EndIf
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_NewGame
							EndIf
						Else
							DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 420 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "play_chapter_1")), False, False, True, 8, MainMenuTab, 0,True)
						EndIf
					Case 3
						If (cpt\Unlocked > 2 And gopt\GameMode = GAMEMODE_DEFAULT) Lor (cpt\NTFUnlocked > 2 And gopt\GameMode = GAMEMODE_NTF) Lor (cpt\DUnlocked > 2 And gopt\GameMode = GAMEMODE_CLASS_D) Then
							If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
								If gopt\GameMode = GAMEMODE_NTF Then
									cpt\NTFCurrent = 3
								ElseIf gopt\GameMode = GAMEMODE_DEFAULT
									cpt\Current = 3
								Else
									cpt\DCurrent = 3
								EndIf
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_NewGame
							EndIf
						Else
							DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 420 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "play_chapter_2")), False, False, True, 8, MainMenuTab, 0,True)
						EndIf
					Case 4
						If (cpt\Unlocked > 3 And gopt\GameMode = GAMEMODE_DEFAULT) Lor (cpt\NTFUnlocked > 3 And gopt\GameMode = GAMEMODE_NTF) Lor (cpt\DUnlocked > 3 And gopt\GameMode = GAMEMODE_CLASS_D) Then
							If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
								If gopt\GameMode = GAMEMODE_NTF Then
									cpt\NTFCurrent = 4
								ElseIf gopt\GameMode = GAMEMODE_DEFAULT
									cpt\Current = 4
								Else
									cpt\DCurrent = 4
								EndIf
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_NewGame
							EndIf
						Else
							DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 420 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "play_chapter_3")), False, False, True, 8, MainMenuTab, 0,True)
						EndIf
					Case 5
						If (cpt\Unlocked > 4 And gopt\GameMode = GAMEMODE_DEFAULT) Lor (cpt\NTFUnlocked > 4 And gopt\GameMode = GAMEMODE_NTF) Lor (cpt\DUnlocked > 4 And gopt\GameMode = GAMEMODE_CLASS_D) Then
							If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
								If gopt\GameMode = GAMEMODE_NTF Then
									cpt\NTFCurrent = 5
								ElseIf gopt\GameMode = GAMEMODE_DEFAULT
									cpt\Current = 5
								Else
									cpt\DCurrent = 5
								EndIf
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_NewGame
							EndIf
						Else
							DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 420 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "play_chapter_4")), False, False, True, 8, MainMenuTab, 0,True)
						EndIf
					Case 6
						If (cpt\Unlocked > 5 And gopt\GameMode = GAMEMODE_DEFAULT) Lor (cpt\NTFUnlocked > 5 And gopt\GameMode = GAMEMODE_NTF) Lor (cpt\DUnlocked > 5 And gopt\GameMode = GAMEMODE_CLASS_D) Then
							If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
								If gopt\GameMode = GAMEMODE_NTF Then
									cpt\NTFCurrent = 6
								ElseIf gopt\GameMode = GAMEMODE_DEFAULT
									cpt\Current = 6
								Else
									cpt\DCurrent = 6
								EndIf
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_NewGame
							EndIf
						Else
							DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 420 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "play_chapter_5")), False, False, True, 8, MainMenuTab, 0,True)
						EndIf
					Case 7
						If (cpt\Unlocked > 6 And gopt\GameMode = GAMEMODE_DEFAULT) Lor (cpt\NTFUnlocked > 6 And gopt\GameMode = GAMEMODE_NTF) Lor (cpt\DUnlocked > 6 And gopt\GameMode = GAMEMODE_CLASS_D) Then
							If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
								If gopt\GameMode = GAMEMODE_NTF Then
									cpt\NTFCurrent = 7
								ElseIf gopt\GameMode = GAMEMODE_DEFAULT
									cpt\Current = 7
								Else
									cpt\DCurrent = 7
								EndIf
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_NewGame
							EndIf
						Else
							DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 420 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "play_chapter_6")), False, False, True, 8, MainMenuTab, 0,True)
						EndIf
					Case 8
						If (cpt\Unlocked > 7 And gopt\GameMode = GAMEMODE_DEFAULT) Lor (cpt\NTFUnlocked > 7 And gopt\GameMode = GAMEMODE_NTF) Lor (cpt\DUnlocked > 7 And gopt\GameMode = GAMEMODE_CLASS_D) Then
							If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
								If gopt\GameMode = GAMEMODE_NTF Then
									cpt\NTFCurrent = 8
								ElseIf gopt\GameMode = GAMEMODE_DEFAULT
									cpt\Current = 8
								Else
									cpt\DCurrent = 8
								EndIf
								opt\IntroEnabled = False
								MainMenuTab = MenuTab_NewGame
							EndIf
						Else
							DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 420 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "play_chapter_7")), False, False, True, 8, MainMenuTab, 0,True)
						EndIf
					Case 9
						If (cpt\Unlocked > 8 And gopt\GameMode = GAMEMODE_DEFAULT) Lor (cpt\NTFUnlocked > 8 And gopt\GameMode = GAMEMODE_NTF) Lor (cpt\DUnlocked > 8 And gopt\GameMode = GAMEMODE_CLASS_D) Then
							If gopt\GameMode = GAMEMODE_NTF Then
								If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "select")), False, False, True, 8, MainMenuTab, 0) Then
									If gopt\GameMode = GAMEMODE_NTF Then
										cpt\NTFCurrent = 9
									ElseIf gopt\GameMode = GAMEMODE_DEFAULT
										cpt\Current = 9
									Else
										cpt\DCurrent = 9
									EndIf
									opt\IntroEnabled = False
									MainMenuTab = MenuTab_NewGame
								EndIf
							Else
								If DrawButton(x + 820 * MenuScale, y + 20 * MenuScale, 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "chapter_gate_a")), False, False, True, 8, MainMenuTab, 0) Then
									If gopt\GameMode = GAMEMODE_NTF Then
										cpt\NTFCurrent = 9
									ElseIf gopt\GameMode = GAMEMODE_DEFAULT
										cpt\Current = 9
									Else
										cpt\DCurrent = 9
									EndIf
									opt\IntroEnabled = False
									MainMenuTab = MenuTab_NewGame
									cpt\SurfaceEnding = GATE_A_TOPSIDE
								EndIf
								If DrawButton(x + 820 * MenuScale, y + 120 * MenuScale, 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "chapter_gate_b")), False, False, True, 8, MainMenuTab, 0) Then
									If gopt\GameMode = GAMEMODE_NTF Then
										cpt\NTFCurrent = 9
									ElseIf gopt\GameMode = GAMEMODE_DEFAULT
										cpt\Current = 9
									Else
										cpt\DCurrent = 9
									EndIf
									opt\IntroEnabled = False
									MainMenuTab = MenuTab_NewGame
									cpt\SurfaceEnding = GATE_B_TOPSIDE
								EndIf
								If DrawButton(x + 820 * MenuScale, y + 220 * MenuScale, 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "chapter_gate_c")), False, False, True, 8, MainMenuTab, 0) Then
									If gopt\GameMode = GAMEMODE_NTF Then
										cpt\NTFCurrent = 9
									ElseIf gopt\GameMode = GAMEMODE_DEFAULT
										cpt\Current = 9
									Else
										cpt\DCurrent = 9
									EndIf
									opt\IntroEnabled = False
									MainMenuTab = MenuTab_NewGame
									cpt\SurfaceEnding = GATE_C_TOPSIDE
								EndIf
								If DrawButton(x + 820 * MenuScale, y + 320 * MenuScale, 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "chapter_gate_d")), False, False, True, 8, MainMenuTab, 0) Then
									If gopt\GameMode = GAMEMODE_NTF Then
										cpt\NTFCurrent = 9
									ElseIf gopt\GameMode = GAMEMODE_DEFAULT
										cpt\Current = 9
									Else
										cpt\DCurrent = 9
									EndIf
									opt\IntroEnabled = False
									MainMenuTab = MenuTab_NewGame
									cpt\SurfaceEnding = GATE_D_TOPSIDE
								EndIf
							EndIf
						Else
							DrawButton(x + 820 * MenuScale, y + 20 * MenuScale , 420 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "play_chapter_8")), False, False, True, 8, MainMenuTab, 0,True)
						EndIf
				End Select
				;[End Block]
			Case MenuTab_Extras
				;[Block]
				y = 286 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 190 * MenuScale
				
				If DrawButton(x+20*MenuScale,y+20*MenuScale,width-40*MenuScale,70*MenuScale,GetLocalString("Menu","credits"),2) Then
					LightSpriteTex[0] = LoadTexture_Strict("GFX\light1.jpg",1,0)
					LightSpriteTex[2] = LoadTexture_Strict("GFX\lightsprite.jpg",1,0)
					LoadMaterials("Data\materials.ini")
					LoadCredits()
				EndIf
				If DrawButton(x+20*MenuScale,y+100*MenuScale,width-40*MenuScale,70*MenuScale,GetLocalString("Menu","achievements"),2) Then MainMenuTab = MenuTab_Achievements
				;[End Block]
			Case MenuTab_Achievements
				;[Block]
				y = 286 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 296 * MenuScale
				
				If CurrLoadGamePage < Ceil(Float(MAXACHIEVEMENTS)/4.0)-1 Then
					If DrawButton(x+530*MenuScale, y + 470*MenuScale, 51*MenuScale, 55*MenuScale, ">") Then
						CurrLoadGamePage = CurrLoadGamePage+1
						ShouldDeleteGadgets = True
					EndIf
				EndIf
				If CurrLoadGamePage > 0 Then
					If DrawButton(x, y + 470*MenuScale, 51*MenuScale, 55*MenuScale, "<") Then
						CurrLoadGamePage = CurrLoadGamePage-1
						ShouldDeleteGadgets = True
					EndIf
				EndIf
				
				If CurrLoadGamePage > Ceil(Float(MAXACHIEVEMENTS)/4.0)-1 Then
					CurrLoadGamePage = CurrLoadGamePage - 1
					ShouldDeleteGadgets = True
				EndIf
				;[End Block]
			Case MenuTab_Singleplayer
				;[Block]
				y = 286 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				
				temp = 0
				For sa = Each Save
					If sa\Name = m_I\CurrentSave Then
						If sa\Version = CompatibleNumber Then
							If DrawButton(x+20*MenuScale,y+20*MenuScale,width-40*MenuScale,70*MenuScale, Upper(GetLocalString("Menu", "continue")), 2) Then
								MainMenuTab = MenuTab_Continue
							EndIf
							temp = 1
						EndIf
						Exit
					EndIf
				Next
				If DrawButton(x+20*MenuScale,y+(20 + (80 * temp))*MenuScale,width-40*MenuScale,70*MenuScale, Upper(GetLocalString("Menu", "newgame")), 2) Then
					RandomSeed = ""
					If Rand(15)=1 Then 
						Select Rand(15)
							Case 1 
								RandomSeed = "NIL"
							Case 2
								RandomSeed = "NO"
							Case 3
								RandomSeed = "d9341"
							Case 4
								RandomSeed = "Ryan"
							Case 5
								RandomSeed = "Awoo"
							Case 6
								RandomSeed = "CRUNCH"
							Case 7
								RandomSeed = "Vincent"
							Case 8
								RandomSeed = "HTAED"
							Case 9
								RandomSeed = "ENDSHN"
							Case 10
								RandomSeed = "larry"
							Case 11
								RandomSeed = "JORGE"
							Case 12
								RandomSeed = "9632"
							Case 13
								RandomSeed = "Wolfnaya"
							Case 14
								RandomSeed = "ChuckNorris"
							Case 15
								RandomSeed = "666"
						End Select
					Else
						n = Rand(4,8)
						For j = 1 To n
							If Rand(3)=1 Then
								RandomSeed = RandomSeed + Rand(0,9)
							Else
								RandomSeed = RandomSeed + Chr(Rand(97,122))
							EndIf
						Next
					EndIf
					LoadSaveGames()
					CurrSave = New Save
					MainMenuTab = MenuTab_Gamemodes
				EndIf
				If DrawButton(x+20*MenuScale,y+(100 + (80 * temp))*MenuScale,width-40*MenuScale,70*MenuScale, Upper(GetLocalString("Menu", "loadgame")),2) Then
					LoadSaveGames()
					MainMenuTab = MenuTab_LoadGame
				EndIf
				;[End Block]
			Case MenuTab_Gamemodes
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				If DrawButton(x + 400 * MenuScale, y + height + 20 * MenuScale, 170 * MenuScale, 170 * MenuScale, "", False, False, True, 1, MainMenuTab, 0)
					MainMenuTab = MenuTab_NewGame
					
					If cpt\NTFCurrent > 1 Then
						cpt\NTFCurrent = 1
					EndIf
					
					CurrSave = New Save
					gopt\GameMode = GAMEMODE_NTF
				EndIf
				If DrawButton(x + 200 * MenuScale, y + height + 20 * MenuScale, 170 * MenuScale, 170 * MenuScale, "", False, False, True, 1, MainMenuTab, 0)
					MainMenuTab = MenuTab_NewGame
					
					If cpt\DCurrent > 1 Then
						cpt\DCurrent = 1
					EndIf
					
					ClassDNumber = Rand(1000,99999)
					
					CurrSave = New Save
					gopt\GameMode = GAMEMODE_CLASS_D
				EndIf
				If DrawButton(x, y + height + 20 * MenuScale, 170 * MenuScale, 170 * MenuScale, "", False, False, True, 1, MainMenuTab, 0)
					MainMenuTab = MenuTab_NewGame
					
					If cpt\Current > 1 Then
						cpt\Current = 1
					EndIf
					
					CurrSave = New Save
					gopt\GameMode = GAMEMODE_DEFAULT
				EndIf
				
				;[End Block]
			Case MenuTab_MissionMode
				;[Block]
				If co\CurrButton[15]=1
					UpdateMenuControllerSelection(2,15,2,2)
				Else
					UpdateMenuControllerSelection(2,15,0)
					co\CurrButtonSub[15]=0
				EndIf
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 330 * MenuScale
				
				Local mi.Mission
				For mi = Each Mission
					
				Next
				
				If DrawButton(x + 420 * MenuScale, y + height + 20 * MenuScale, 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Menu", "start")), False, False, True, 1, MainMenuTab, 0)
					MainMenuOpen = False
					ResetControllerSelections()
					Null3DMenu()
					InitMission(0)
					LoadEntities()
					LoadAllSounds()
					gopt\GameMode = GAMEMODE_UNKNOWN
					InitMissionGameMode(0)
					FlushKeys()
					FlushMouse()
				EndIf
				;[End Block]
			Case MenuTab_ChallengeMode
				;[Block]
				
				;[End Block]
			Case MenuTab_Lobby
				;[Block]
;				x = 59 * MenuScale
;				y = 286 * MenuScale
;				
;				width = 400 * MenuScale
;				height = 70 * MenuScale
;				
;				x = 60 * MenuScale
;				y = y + height + 20 * MenuScale
;				width = 580 * MenuScale
;				height = 330 * MenuScale
;				
;				mp_I\CurrChatMSG = InputBox(x + width + 20 * MenuScale, y + 330 *  MenuScale, 400 * MenuScale, 30 * MenuScale, mp_I\CurrChatMSG, 1, 41, 1, MainMenuTab)
;				
;				If DrawButton(x + width + 420 * MenuScale, y + 330 * MenuScale, 75 * MenuScale, 30 * MenuScale, "Send", False, False, True, 1, MainMenuTab, 0) Lor (SelectedInputBox=1 And KeyHit(28)) Then
;					If mp_I\CurrChatMSG <> "" Then
;						CreateChatMSG()
;						SelectedInputBox = 1
;					EndIf
;				EndIf
;				
;				y = 286 * MenuScale
;				y = y + (70*MenuScale) + 20 * MenuScale
;				
;				y = 286 * MenuScale
;				y = y + (70*MenuScale) + 20 * MenuScale
;				
;				If (Not mp_I\IsReady) Then
;					If DrawButton(x + 420 * MenuScale, y + height + 20 * MenuScale, 160 * MenuScale, 70 * MenuScale, "READY", False, False, True, 1, MainMenuTab, 0) Then
;						mp_I\IsReady = True
;					EndIf
;				Else
;					If DrawButton(x + 420 * MenuScale, y + height + 20 * MenuScale, 160 * MenuScale, 70 * MenuScale, "NOT READY", False, False, True, 1, MainMenuTab, 0) Then
;						mp_I\IsReady = False
;					EndIf
;				EndIf
;				
;				If mp_I\PlayState=GAME_SERVER Then
;					UpdateLobby()
;				Else
;					UpdateLobbyClient()
;				EndIf
;				
;				If mp_I\ReadyTimer = 0 Then
;					ResetControllerSelections()
;					Null3DMenu()
;					MainMenuOpen = False
;					;debuglog "Starting Multiplayer Match!"
;					If mp_I\PlayState=GAME_SERVER Then
;						LoadingServer()
;					Else
;						LoadingClient()
;					EndIf
;					Return
;				EndIf
				;[End Block]
			Case MenuTab_Serverlist
				;[Block]
				If (Not mp_I\HasRefreshed) Then
					mp_I\ServerListAmount = 1
					ListServers()
					mp_I\ServerListPage = 0
					mp_I\ServerListSort = 0
					mp_I\HasRefreshed = True
				EndIf
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				
				width = 40 * MenuScale
				height = 40 * MenuScale
				
				For i = 0 To (SERVER_LIST_SORT_CATEGORY_MAX-1)
					x = x + width
					Select i
						Case 0
							;Server name
							width = 500 * MenuScale
							category = "servers"
						Case 1
							;Server gamemode
							width = 200 * MenuScale
							category = "gamemode"
						Case 2
							;Server map
							width = 200 * MenuScale
							category = "map"
						Case 3
							;Player amount
							width = 150 * MenuScale
							category = "players"
					End Select
					If DrawButton(x, y, width, height, GetLocalString("Serverlist", category), False) Then
						If Floor(mp_I\ServerListSort / 2.0) = i Then
							mp_I\ServerListSort = mp_I\ServerListSort - ((mp_I\ServerListSort Mod 2) * 2 - 1)
						Else
							mp_I\ServerListSort = i * 2
						EndIf
						ListServers()
					EndIf
				Next
				
				x = 60 * MenuScale
				width = 1130 * MenuScale
				height = 560 * MenuScale
				
				If mp_I\ServerMSG = SERVER_MSG_NONE Then
					If DrawButton(x + 10 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale, GetLocalString("Serverlist", "host_server"), False, False, True) Then
						MainMenuTab = MenuTab_HostServer
						mp_I\SelectedListServer = 0
					EndIf
					
					If DrawButton(x + width*0.25 + 10 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale, GetLocalString("Serverlist", "refresh_list"), False, False, True) Then
						mp_I\SelectedListServer = 0
						mp_I\HasRefreshed = False
						ShouldDeleteGadgets = True
					EndIf
					
					j=1
					For i = (1+(16*mp_I\ServerListPage)) To 16+(16*mp_I\ServerListPage)
						If MouseOn(x,(y+10*MenuScale)+(30*j)*MenuScale,1130*MenuScale,30*MenuScale)
							If MouseHit1 Then
								For se = Each Server
									If se\ID = i Then
										mp_I\SelectedListServer = i
										Exit
									EndIf
								Next
							EndIf
						EndIf
						j=j+1
					Next
					
					If DrawButton(x + width*0.55 + 10 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale, GetLocalString("Serverlist", "join_friend"), False, False, True) Then
						Steam_ActivateOverlay("friends")
					EndIf
					
					If mp_I\SelectedListServer > 0 Then
						If DrawButton(x + width - 210 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale, GetLocalString("Serverlist", "join_server"), False, False, True) Then
							Delete Each MenuButton
							For se = Each Server
								If se\ID = mp_I\SelectedListServer Then
									Connect(se\id_upper, se\id_lower)
									Exit
								EndIf
							Next
							SaveMPOptions()
							If mp_I\PlayState = GAME_CLIENT Then
								Return
							EndIf
						EndIf
					EndIf
					
					x = 59 * MenuScale
					y = 286 * MenuScale
					height = 70 * MenuScale
					
					If mp_I\ServerListAmount > 1 Then
						If mp_I\ServerListPage < Ceil(Float(mp_I\ServerListAmount-1)/16.0)-1 Then
							If DrawButton(x+width-height,y, height, height, ">", 2, False, True, 0, MainMenuTab, 0)
								mp_I\ServerListPage = mp_I\ServerListPage+1
								ShouldDeleteGadgets = True
								mp_I\SelectedListServer = 0
							EndIf
						EndIf
						If mp_I\ServerListPage > 0 Then
							If DrawButton(x+width-height-280*MenuScale,y, height, height, "<", 2, False, True, 0, MainMenuTab, 0)
								mp_I\ServerListPage = mp_I\ServerListPage-1
								ShouldDeleteGadgets = True
								mp_I\SelectedListServer = 0
							EndIf
						EndIf
					EndIf
				ElseIf mp_I\ServerMSG = SERVER_MSG_OFFLINE Then
					If DrawButton(x + width * 0.3 + 20 * MenuScale, y + 300 * MenuScale, 150 * MenuScale, 40 * MenuScale, GetLocalString("Menu", "cancel"), False, False, True) Then
						mp_I\ServerMSG = SERVER_MSG_NONE
						ShouldDeleteGadgets=True
					EndIf
					
					If DrawButton(x + width * 0.3 + 280 * MenuScale, y + 300 * MenuScale, 150 * MenuScale, 40 * MenuScale, GetLocalString("Menu", "retry"), False, False, True) Then
						mp_I\SelectedListServer = 0
						mp_I\HasRefreshed = False
						ShouldDeleteGadgets = True
						mp_I\ServerMSG = SERVER_MSG_NONE
					EndIf
				ElseIf mp_I\ServerMSG = SERVER_MSG_PASSWORD Then
					mp_I\ConnectPassword = InputBox(x + width * 0.3 + 230*MenuScale, y+190*MenuScale, 200*MenuScale, 30*MenuScale, mp_I\ConnectPassword, 1, 30, 2, MainMenuTab, 0, (Not mp_I\PasswordVisible))
					
					If DrawButton(x + width * 0.3 + 20 * MenuScale, y + 300 * MenuScale, 150 * MenuScale, 40 * MenuScale, GetLocalString("Menu", "cancel"), False, False, True) Then
						mp_I\ServerMSG = SERVER_MSG_NONE
						ShouldDeleteGadgets=True
						;CloseUDPStream(mp_I\Server)
						Steam_CloseConnection(Players[0]\SteamIDUpper, Players[0]\SteamIDLower)
						mp_I\PlayerCount = 0
						mp_I\ChatMSGID = 0
					EndIf
					
					If mp_I\ConnectPassword <> "" Then
						If DrawButton(x + width * 0.3 + 280 * MenuScale, y + 300 * MenuScale, 150 * MenuScale, 40 * MenuScale, GetLocalString("Menu", "continue"), False, False, True) Then
							;WriteByte mp_I\Server,PACKET_AUTHORIZE
							;WriteLine mp_I\Server,mp_I\ConnectPassword
							;WriteLine mp_I\Server,mp_I\PlayerName
							;SendUDPMsg mp_I\Server,Players[0]\IP,Players[0]\Port
							ConnectViaPassword()
							mp_I\ServerMSG = SERVER_MSG_PWAIT
							ShouldDeleteGadgets = True
							mp_I\PasswordVisible = False
						EndIf
					Else
						ShouldDeleteGadgets = True
					EndIf
				ElseIf mp_I\ServerMSG = SERVER_MSG_PWAIT Then
					Local getconn = Steam_LoadPacket()
					While getconn
						ConnectFinal()
						If mp_I\PlayState = GAME_CLIENT Then
							mp_I\ServerMSG = SERVER_MSG_NONE
							SaveMPOptions()
							Return
						EndIf
						getconn = Steam_LoadPacket()
					Wend
				ElseIf mp_I\ServerMSG = SERVER_MSG_CONNECT Then
					getconn = Steam_LoadPacket()
					If getconn Then
						;Necessary here so that the client knows the host's information regarding where authorization packages will be sent to
						CreateHostPlayerAsClient(Steam_GetSenderIDUpper(), Steam_GetSenderIDLower())
						
						mp_I\ServerMSG = SERVER_MSG_NONE
						ConnectFinal()
						
						If m3d = Null And MainMenuOpen Then
							MainMenuTab = MenuTab_Serverlist
							Delete Each Menu3DInstance
							InitConsole(2)
							Load3DMenu()
						EndIf
						Steam_LeaveLobby()
						Steam_FlushLobbyID()
						Return
					Else
						mp_I\ConnectionTime = mp_I\ConnectionTime + FPSfactor
						
						If mp_I\ConnectionTime > 70*10 Then
							ConnectWithNoPassword(Steam_GetSenderIDUpper(), Steam_GetSenderIDLower())
							mp_I\ConnectionTime = 0.0
							mp_I\ConnectionRetries = mp_I\ConnectionRetries + 1
						EndIf
						
						If DrawButton(x + width * 0.3 + 280 * MenuScale, y + 300 * MenuScale, 150 * MenuScale, 40 * MenuScale, GetLocalString("Menu", "cancel"), False, False, True) Lor mp_I\ConnectionRetries >= MAX_RETRIES Then
							DeleteMenuGadgets()
							Steam_LeaveLobby()
							Steam_FlushLobbyID()
							Steam_CloseConnection(Steam_GetSenderIDUpper(), Steam_GetSenderIDLower()) ;GetSenderID may not work???
							If (Not MainMenuOpen) Then
								Delete Each Menu3DInstance
								MainMenuOpen = True
								InitConsole(2)
								Load3DMenu()
							EndIf
							mp_I\ServerMSG = SERVER_MSG_NONE
							If mp_I\ConnectionRetries >= MAX_RETRIES Then
								mp_I\ServerMSG = SERVER_MSG_RETRIES
							EndIf
							Return
						EndIf
					EndIf
				Else
					If DrawButton(x + width * 0.3 + 280 * MenuScale, y + 300 * MenuScale, 150 * MenuScale, 40 * MenuScale, GetLocalString("Menu", "close"), False, False, True) Then
						SaveMPOptions()
						mp_I\ServerMSG = SERVER_MSG_NONE
						ShouldDeleteGadgets = True
					EndIf
				EndIf
				;[End Block]
			Case MenuTab_HostServer
				;[Block]
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				prevButton = co\CurrButton[MainMenuTab]
				prevButtonSub = co\CurrButtonSub[MainMenuTab]
				
				If co\CurrButton[MainMenuTab]=1
					UpdateMenuControllerSelection(7,MainMenuTab,2,2)
				Else
					UpdateMenuControllerSelection(7,MainMenuTab,0,2)
				EndIf
				If prevButton > 1
					co\CurrButtonSub[MainMenuTab]=0
				EndIf
				If co\CurrButton[MainMenuTab]>1
					co\CurrButtonSub[MainMenuTab]=0
				EndIf
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 360 * MenuScale
				
				mp_O\ServerName = InputBox(x+215*MenuScale, y+15*MenuScale, 340*MenuScale, 30*MenuScale, mp_O\ServerName, 6, 32, 6, MainMenuTab)
				mp_O\ServerName = Replace(mp_O\ServerName,"|","")
				
				mp_O\Password = InputBox(x+300*MenuScale, y+55*MenuScale, 170*MenuScale, 30*MenuScale, mp_O\Password, 7, 16, 7, MainMenuTab, 0, (Not mp_I\PasswordVisible))
				
				If MouseHit1 Then
					If MouseOn(x + 470 * MenuScale, y + 55 * MenuScale, 30 * MenuScale, 30 * MenuScale) Then
						PlaySound_Strict ButtonSFX[2]
						mp_I\PasswordVisible = Not mp_I\PasswordVisible
					EndIf
				EndIf
				
				If DrawButton(x+300*MenuScale, y+95*MenuScale, 170*MenuScale, 30*MenuScale, mp_O\Gamemode\name, False) Then
					MainMenuTab = MenuTab_SelectMPGamemode
				EndIf
				
				If mp_O\Gamemode\ID = Gamemode_Waves Lor mp_O\Gamemode\ID = Gamemode_EAF Then
					If DrawButton(x + 470 * MenuScale, y + 95 * MenuScale, 30*MenuScale, 30*MenuScale, "...",False,False,True,3,MainMenuTab,1) Then
						MainMenuTab = MenuTab_MPGamemodeSettings
					EndIf
				EndIf
				
				If DrawButton(x+300*MenuScale, y+135*MenuScale, 170*MenuScale, 30*MenuScale, mp_O\MapInList\Name, False) Then
					MainMenuTab = MenuTab_SelectMPMap
				EndIf
				
				Local TempInputString$ = mp_O\MaxPlayers
				If mp_O\MaxPlayers = 0 Then
					TempInputString = ""
				EndIf
				mp_O\MaxPlayers = InputBox(x+335*MenuScale, y+175*MenuScale, 100*MenuScale, 30*MenuScale, TempInputString, 4, 2, MainMenuTab)
				If SelectedInputBox <> 4 Then
					If mp_O\MaxPlayers < 2 Then mp_O\MaxPlayers = 2
					If mp_O\MaxPlayers > mp_O\Gamemode\MaxPlayersAllowed Then mp_O\MaxPlayers = mp_O\Gamemode\MaxPlayersAllowed
				EndIf
				
				TempInputString$ = mp_O\TimeOut
				If mp_O\TimeOut = 0 Then
					TempInputString = ""
				EndIf
				mp_O\TimeOut = InputBox(x+335*MenuScale, y+215*MenuScale, 100*MenuScale, 30*MenuScale, TempInputString, 5, 5, MainMenuTab)
				If SelectedInputBox <> 5 Then
					If mp_O\TimeOut < 1 Then mp_O\TimeOut = 1
					If mp_O\TimeOut > 30 Then mp_O\TimeOut = 30
				EndIf
				
				mp_O\LocalServer = DrawTick(x + 375 * MenuScale, y + 260 * MenuScale, mp_O\LocalServer, False, 2, MainMenuTab, 1)
				
				mp_O\OtherTeams = DrawTick(x + 375 * MenuScale, y + 295 * MenuScale, mp_O\OtherTeams, False, 2, MainMenuTab, 1)
				
				If mp_O\Gamemode\ID = Gamemode_Waves Then
					mp_O\HardcoreMP = DrawTick(x + 375 * MenuScale, y + 330 * MenuScale, mp_O\HardcoreMP, False, 2, MainMenuTab, 1)
				EndIf
					
				If DrawButton(x + 420 * MenuScale, y + height + 65 * MenuScale, 160 * MenuScale, 70 * MenuScale, Upper(GetLocalString("Serverlist","create_server")),False,False,True,6,MainMenuTab,1) Then
					If mp_O\MaxPlayers < 2 Then mp_O\MaxPlayers = 2
					If mp_O\MaxPlayers > mp_O\Gamemode\MaxPlayersAllowed Then mp_O\MaxPlayers = mp_O\Gamemode\MaxPlayersAllowed
					If mp_O\TimeOut < 1 Then mp_O\TimeOut = 1
					If mp_O\TimeOut > 30 Then mp_O\TimeOut = 30
					CreateServer()
					gopt\GameMode = GAMEMODE_MULTIPLAYER ;TEST
					SaveMPOptions()
					
					mp_I\IsReady = False
					mp_I\PasswordVisible = False
					
					ResetControllerSelections()
					Null3DMenu()
					MainMenuOpen = False
					;debuglog "Starting Multiplayer Match!"
					LoadingServer()
					Return
				EndIf
				;[End block]
			Case MenuTab_SelectMPMap
				;[Block]
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 350 * MenuScale
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				
				x = x + 10 * MenuScale
				y = y + 10 * MenuScale
				
				i = 0
				Local mgmAmount = 0
				For mgm = Each MultiplayerGameMode
					mgmAmount = mgmAmount + 1
				Next
				
				For mfl = Each MapForList
					Local mapHasGM = False
					For j = 1 To mgmAmount
						If mp_O\Gamemode\name = Piece(mfl\Gamemodes,j,"|") Then
							mapHasGM = True
							Exit
						EndIf
					Next
					
					If mapHasGM Then
						If DrawButton(x, y, 170*MenuScale, 25*MenuScale, mfl\Name, False, False, True, i+1, MainMenuTab) Then
							mp_O\MapInList = mfl
						EndIf
						y=y+30*MenuScale
						i=i+1
					EndIf
				Next
				;[End Block]
			Case MenuTab_SelectMPGamemode
				;[Block]
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 350 * MenuScale
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				
				x = x + 10 * MenuScale
				y = y + 10 * MenuScale
				
				mgmAmount = 0
				For mgm = Each MultiplayerGameMode
					mgmAmount = mgmAmount + 1
				Next
				
				i = 0
				For mgm = Each MultiplayerGameMode
					mapHasGM = False
					For mfl = Each MapForList
						For j = 1 To mgmAmount
							If mgm\name = Piece(mfl\Gamemodes,j,"|") Then
								mapHasGM = True
								Exit
							EndIf
						Next
						If mapHasGM Then
							Exit
						EndIf
					Next
					If mapHasGM Then
						If DrawButton(x, y, 170*MenuScale, 25*MenuScale, mgm\name, False, False, True, i+1, MainMenuTab) Then
							mp_O\Gamemode = mgm
							mapHasGM = False
							For j = 1 To mgmAmount
								If mgm\name = Piece(mp_O\MapInList\Gamemodes,j,"|") Then
									mapHasGM = True
									Exit
								EndIf
							Next
							If (Not mapHasGM) Then
								For mfl = Each MapForList
									For j = 1 To mgmAmount
										If mgm\name = Piece(mfl\Gamemodes,j,"|") Then
											mp_O\MapInList = mfl
											mapHasGM = True
											Exit
										EndIf
									Next
									If mapHasGM Then
										Exit
									EndIf
								Next
							EndIf
						EndIf
						y=y+30*MenuScale
						i=i+1
					EndIf
				Next
				;[End Block]
			Case MenuTab_MPGamemodeSettings
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				
				Select mp_O\Gamemode\ID
					Case Gamemode_Waves
						;[Block]
						y=y+25*MenuScale
						
						mp_O\Gamemode\Difficulty = Slider3(x+310*MenuScale,y+6*MenuScale,150*MenuScale,mp_O\Gamemode\Difficulty,1,GetLocalString("Multiplayer","safe"),GetLocalString("Multiplayer","euclid"),GetLocalString("Multiplayer","keter"),1,MainMenuTab,0)
						
						y=y+50*MenuScale
						
						mp_O\Gamemode\MaxPhase = Waves_Short+(3*Slider3(x+310*MenuScale,y+6*MenuScale,150*MenuScale,Int(mp_O\Gamemode\MaxPhase/4-1),2,Waves_Short+GetLocalString("Multiplayer","wave_short"),Waves_Medium+GetLocalString("Multiplayer","wave_medium"),Waves_Long+GetLocalString("Multiplayer","wave_long"),2,MainMenuTab,0))
						;[End Block]
				End Select
				;[End Block]
		End Select
	EndIf
	
	If opt\Menu3DEnabled Then
		Select MainMenuTab
			Case MenuTab_Default, MenuTab_Options_Graphics
				m_I\SpriteAlpha = Max(0.0,m_I\SpriteAlpha-0.01*FPSfactor2)
			Case MenuTab_NewGame,MenuTab_LoadGame,MenuTab_Options_Controller,MenuTab_HostServer,MenuTab_MissionMode,MenuTab_ChallengeMode,MenuTab_Achievements,MenuTab_Gamemodes
				If m_I\SpriteAlpha > 0.7
					m_I\SpriteAlpha = Max(0.7,m_I\SpriteAlpha-0.01*FPSfactor2)
				Else
					m_I\SpriteAlpha = Min(0.7,m_I\SpriteAlpha+0.01*FPSfactor2)
				EndIf
			Default
				If m_I\SpriteAlpha > 0.5
					m_I\SpriteAlpha = Max(0.5,m_I\SpriteAlpha-0.01*FPSfactor2)
				Else
					m_I\SpriteAlpha = Min(0.5,m_I\SpriteAlpha+0.01*FPSfactor2)
				EndIf
		End Select
		
		EntityAlpha m_I\Sprite,m_I\SpriteAlpha
	EndIf
	
	If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
		If opt\SteamEnabled Then
			If Steam_SeekLobbyUpper() <> 0 Then
				If mp_O\PlayerName = "" Then mp_O\PlayerName = "Player"
				Delete Each MenuButton
				Connect(Steam_SeekLobbyUpper(),Steam_SeekLobbyLower())
				Steam_FlushLobbyID()
				SaveMPOptions()
				MainMenuTab = MenuTab_Serverlist
			EndIf
		EndIf
	EndIf
	
End Function

Function RenderMainMenu()
	Local x%, y%, width%, height%, temp%, i%, x2%
	Local se.Server
	Local mgm.MultiplayerGameMode, mfl.MapForList, sa.Save
	
	Color 0,0,0
	
	SetFont fo\Font[Font_Default]
	
	If (Not opt\Menu3DEnabled) Then
		If MainMenuOpen Then
			DrawImage(I_MIG\MenuIMG[1], Viewport_Center_X - ImageWidth(I_MIG\MenuIMG[1]) / 2, opt\GraphicHeight - (40 * MenuScale) - ImageHeight(I_MIG\MenuIMG[1]))
			DrawImage(I_MIG\MenuIMG[0], opt\GraphicWidth - ImageWidth(I_MIG\MenuIMG[0]), opt\GraphicHeight - ImageHeight(I_MIG\MenuIMG[0]))
		EndIf
	EndIf
	
	If MenuBlinkTimer[1] < MenuBlinkDuration[1] Then
		Color(50, 50, 50)
		Text(MenuStrX + Rand(-5, 5), MenuStrY + Rand(-5, 5), MenuStr, True)
		If MenuBlinkTimer[1] < 0
			MenuStrX = Rand(700, 1000) * MenuScale
			MenuStrY = Rand(100, 600) * MenuScale
			
			Select Rand(0, 23)
				Case 0, 2, 3
					MenuStr = "DON'T BLINK"
				Case 4, 5
					MenuStr = "Secure. Contain. Protect."
				Case 6, 7, 8
					MenuStr = "You want happy endings? Fuck you."
				Case 9, 10, 11
					MenuStr = "Sometimes we would have had time to scream."
				Case 12, 19
					MenuStr = "NIL"
				Case 13
					MenuStr = "NO"
				Case 14
					MenuStr = "black white black white black white gray"
				Case 15
					MenuStr = "Stone does not care"
				Case 16
					MenuStr = "9341"
				Case 17
					MenuStr = "It controls the doors"
				Case 18
					MenuStr = "e8m106]af173o+079m895w914"
				Case 20
					MenuStr = "It has taken over everything"
				Case 21
					MenuStr = "The spiral is growing"
				Case 22
					MenuStr = Chr(34)+"Some kind of gestalt effect due to massive reality damage."+Chr(34)
				Case 23
					MenuStr = "Scheisen"
			End Select
		EndIf
	EndIf
	
	SetFont fo\Font[Font_Menu]
	
	If MainMenuTab<>MenuTab_Default Then
		x = 59 * MenuScale
		y = 286 * MenuScale
		
		width = 400 * MenuScale
		height = 70 * MenuScale
		
		DrawFrame(x, y, width, height)
		
		If MainMenuTab = MenuTab_Serverlist And mp_I\ServerMSG <> SERVER_MSG_NONE And mp_I\ServerMSG <> SERVER_MSG_OFFLINE Then
			DrawFrame(x + width + 20 * MenuScale, y, 580 * MenuScale - width - 20 * MenuScale, height)
			Color(100, 100, 100)
			SetFont fo\Font[Font_Default]
			Text((x + width + 20 * MenuScale) + (580 * MenuScale - width - 20 * MenuScale) / 2, y + height / 2, Upper(GetLocalString("Menu", "back")), True, True)
		EndIf
		
		Select MainMenuTab
			Case MenuTab_NewGame
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, Upper(GetLocalString("Menu", "newgame")), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 370 * MenuScale
				
				DrawFrame(x, y, width, height)				
				
				SetFont fo\Font[Font_Default]
				
				Text (x + 20 * MenuScale, y + 20 * MenuScale, GetLocalString("Menu","name")+":")
				
				Color 255,255,255
				
				Text (x + 20 * MenuScale, y + 60 * MenuScale, GetLocalString("Menu","map_seed")+":")
					
				Text(x + 20 * MenuScale, y + 110 * MenuScale, GetLocalString("Menu","enable_intro")+":")
				;Text(x + 20 * MenuScale, y + 110 * MenuScale, GetLocalString("Menu","classic_mode")+":")
				If MouseOn(x + 280 * MenuScale, y + 110 * MenuScale, 20 * MenuScale, 20 * MenuScale)
					DrawOptionsTooltip("intro_txt")
				EndIf
				
				Text (x + 20 * MenuScale, y + 150 * MenuScale, GetLocalString("Menu","difficulty")+":")
				For i = SAFE To ESOTERIC
					Color(difficulties[i]\r,difficulties[i]\g,difficulties[i]\b)
					Text(x + 60 * MenuScale, y + (180+30*i) * MenuScale, difficulties[i]\Name)
				Next
				
				Color(255, 255, 255)
				DrawFrame(x + 150 * MenuScale,y + 155 * MenuScale, 410*MenuScale, 150*MenuScale)
				
				If SelectedDifficulty\Customizable
					SelectedDifficulty\PermaDeath =  DrawTick(x + 160 * MenuScale, y + 165 * MenuScale, (SelectedDifficulty\PermaDeath), False, 0, MainMenuTab, 1)
					Text(x + 200 * MenuScale, y + 165 * MenuScale, GetLocalString("Menu","permadeath"))
					
					Text(x + 200 * MenuScale, y + 195 * MenuScale, GetLocalString("Menu","save_anywhere"))
					
					Text(x + 200 * MenuScale, y + 225 * MenuScale, GetLocalString("Menu","aggressive_npcs"))
					
				;Other factor's difficulty
					Color 255,255,255
					If co\Enabled
						If co\CurrButton[MainMenuTab]=3 And co\CurrButtonSub[MainMenuTab]=1
							Color 255,255,255
							Rect x+160*MenuScale,y+251*MenuScale,ImageWidth(I_MIG\ArrowIMG[1])-5*MenuScale,ImageHeight(I_MIG\ArrowIMG[1])
						EndIf
					EndIf
					DrawImage I_MIG\ArrowIMG[1],x + 155 * MenuScale, y+251*MenuScale
					Color 255,255,255
					Select SelectedDifficulty\OtherFactors
						Case EASY
							Text(x + 200 * MenuScale, y + 255 * MenuScale, GetLocalString("Menu","odf")+": "+GetLocalString("Menu","easy"))
						Case NORMAL
							Text(x + 200 * MenuScale, y + 255 * MenuScale, GetLocalString("Menu","odf")+": "+GetLocalString("Menu","normal"))
						Case HARD
							Text(x + 200 * MenuScale, y + 255 * MenuScale, GetLocalString("Menu","odf")+": "+GetLocalString("Menu","hard"))
					End Select
				Else
					RowText(SelectedDifficulty\Description, x+160*MenuScale, y+160*MenuScale, (410-20)*MenuScale, 200)					
				EndIf
				
				; ~ Game info
				
				DrawFrame(x + width, y, width, height)
				
				Local ModeStr$,IntroStr$
				
				If gopt\GameMode = GAMEMODE_NTF Then
					Color 0,40,255
					ModeStr$ = GetLocalString("Menu","gm_ntf")
					;DrawImage(I_MIG\ClassicIMG[CLASSIC_IMG_NTF], x + 20 + width + 80 * MenuScale, y + 100 * MenuScale)
				ElseIf gopt\GameMode = GAMEMODE_CLASS_D Then
					Color 255,106,0
					ModeStr$ = GetLocalStringR("Menu","gm_d",ClassDNumber)
					;DrawImage(I_MIG\ClassicIMG[CLASSIC_IMG_D], x + 20 + width + 80 * MenuScale, y + 100 * MenuScale)
				Else
					ModeStr$ = GetLocalString("Menu","gm_r")
					;DrawImage(I_MIG\ClassicIMG[CLASSIC_IMG_GUARD], x + 20 + width + 80 * MenuScale, y + 100 * MenuScale)
				EndIf
				If opt\IntroEnabled Then
					IntroStr$ = "True"
				Else
					IntroStr$ = "False"
				EndIf
				
				;Color 255,255,255
				Text (x + 20 + width * MenuScale, y + 20 * MenuScale, GetLocalString("Menu","selected_gamemode")+ModeStr$)
				Color 255,255,255
				If gopt\GameMode = GAMEMODE_NTF Then
					Text (x + 20 + width * MenuScale, y + 40 * MenuScale, GetLocalString("Menu","selected_chapter")+Int(cpt\NTFCurrent))
				ElseIf gopt\GameMode = GAMEMODE_DEFAULT Then
					Text (x + 20 + width * MenuScale, y + 40 * MenuScale, GetLocalString("Menu","selected_chapter")+Int(cpt\Current))
				Else
					Text (x + 20 + width * MenuScale, y + 40 * MenuScale, GetLocalString("Menu","selected_chapter")+Int(cpt\DCurrent))
				EndIf
				Color 255,255,255
				Text (x + 20 + width * MenuScale, y + 60 * MenuScale, GetLocalString("Menu","intro_enabled")+GetLocalString("Menu",IntroStr$))
				
				; ~ End
				
				SetFont fo\Font[Font_Menu]
				;[End Block]
			Case MenuTab_LoadGame
				;[Block]
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 510 * MenuScale
				
				DrawFrame(x, y, width, height)
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, Upper(GetLocalString("Menu","loadgame")), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 296 * MenuScale	
				
				DrawFrame(x+50*MenuScale,y+510*MenuScale,width-100*MenuScale,55*MenuScale)
				
				SetFont fo\Font[Font_Default_Large]
				If CurrLoadGamePage < Ceil(Float(SaveGameAmount)/6.0)-1 And DelSave = Null Then 
					
				Else
					DrawFrame(x+530*MenuScale, y + 510*MenuScale, 51*MenuScale, 55*MenuScale)
					Color(100, 100, 100)
					Text(x+555*MenuScale, y + 537*MenuScale, ">", True, True)
				EndIf
				If CurrLoadGamePage > 0 And DelSave = Null Then
					
				Else
					DrawFrame(x, y + 510*MenuScale, 51*MenuScale, 55*MenuScale)
					Color(100, 100, 100)
					Text(x+25*MenuScale, y + 537*MenuScale, "<", True, True)
				EndIf
				SetFont fo\Font[Font_Menu]
				
				Color 255,255,255
				Text(x+(width/2.0),y+536*MenuScale,GetLocalString("Menu","page")+" "+Int(Max((CurrLoadGamePage+1),1))+"/"+Int(Max((Int(Ceil(Float(SaveGameAmount)/6.0))),1)),True,True)
				
				SetFont fo\Font[Font_Default]
				
				If SaveGameAmount = 0 Then
					Text (x + 20 * MenuScale, y + 20 * MenuScale, GetLocalString("Menu","no_saved"))
				Else
					x = x + 20 * MenuScale
					y = y + 20 * MenuScale
					
					CurrSave = First Save
					
					For i% = 0 To 5+(6*CurrLoadGamePage)
						If i > 0 Then CurrSave = After CurrSave
						If CurrSave = Null Then Exit
						If i >= (0+(6*CurrLoadGamePage))
							DrawFrame(x,y,540* MenuScale, 70* MenuScale)
							
							If CurrSave\Version <> CompatibleNumber Then
								Color 255,0,0
							Else
								Color 255,255,255
							EndIf
							
							;Text(x + 200 * MenuScale, y + 10 * MenuScale, CurrSave\PlayTime) ~ TODO Play Time
							Text(x + 20 * MenuScale, y + 10 * MenuScale, CurrSave\Name)
							Text(x + 20 * MenuScale, y + (10+18) * MenuScale, CurrSave\Time)
							Text(x + 120 * MenuScale, y + (10+18) * MenuScale, CurrSave\Date)
							Text(x + 20 * MenuScale, y + (10+36) * MenuScale, "v"+CurrSave\Version)
							
							Text(x + 85 * MenuScale, y + (10+36) * MenuScale, " | "+GetLocalString("Menu","save_gamemode"))
							Local gamemodestr$
							
							If CurrSave\Gamemode = GAMEMODE_DEFAULT Then
								gamemodestr$ = GetLocalString("Menu","save_gamemode_r")
								Color 255,255,255
							ElseIf CurrSave\Gamemode = GAMEMODE_CLASS_D Then
								gamemodestr$ = GetLocalString("Menu","save_gamemode_d")
								Color 255,106,0
							ElseIf CurrSave\Gamemode = GAMEMODE_NTF Then
								gamemodestr$ = GetLocalString("Menu","save_gamemode_ntf")
								Color 0,40,255
							Else
								gamemodestr$ = "..."
								Color 255,0,0
							EndIf
							Text(x + 210 * MenuScale, y + (10+36) * MenuScale, gamemodestr$)
							
							Color 255,255,255
							
							If DelSave = Null Then
								If CurrSave\Version <> CompatibleNumber Then
									DrawFrame(x + 280 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale)
									Color(255, 0, 0)
									Text(x + 330 * MenuScale, y + 34 * MenuScale, GetLocalString("Menu","load"), True, True)
								EndIf
							Else
								DrawFrame(x + 280 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale)
								If CurrSave\Version <> CompatibleNumber Then
									Color(255, 0, 0)
								Else
									Color(100, 100, 100)
								EndIf
								Text(x + 330 * MenuScale, y + 34 * MenuScale, GetLocalString("Menu","load"), True, True)
								
								DrawFrame(x + 400 * MenuScale, y + 20 * MenuScale, 100 * MenuScale, 30 * MenuScale)
								Color(100, 100, 100)
								Text(x + 450 * MenuScale, y + 34 * MenuScale, GetLocalString("Menu","delete"), True, True)
							EndIf
							
							If CurrSave = Last Save Then
								Exit
							EndIf
							
							y = y + 80 * MenuScale
						EndIf
					Next
					
					If DelSave <> Null
						x = 640 * MenuScale
						y = 376 * MenuScale
						DrawFrame(x, y, 420 * MenuScale, 200 * MenuScale)
						RowText(GetLocalString("Menu","ask_save_delete"), x + 20 * MenuScale, y + 15 * MenuScale, 400 * MenuScale, 200 * MenuScale)
					EndIf
				EndIf
				;[End Block]	
			Case MenuTab_Options_Graphics,MenuTab_Options_Audio,MenuTab_Options_Controls,MenuTab_Options_Advanced,MenuTab_Options_Controller,MenuTab_Options_ControlsBinding
				;[Block]
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, Upper(GetLocalString("Menu", "options")), True, True)
				
				If MainMenuTab<>MenuTab_Options_Controller And MainMenuTab<>MenuTab_Options_ControlsBinding Then
					x = 60 * MenuScale
					y = y + height + 20 * MenuScale
					width = 580 * MenuScale
					height = 60 * MenuScale
					DrawFrame(x, y, width, height)
					Local prevButton = co\CurrButton[MainMenuTab]
					Local prevButtonSub = co\CurrButtonSub[MainMenuTab]
					
					Color 255,255,255
					If MainMenuTab = MenuTab_Options_Graphics Then
						Rect(x+18*MenuScale,y+13*MenuScale,(width/5.3)+10*MenuScale,(height/2.6)+10*MenuScale,False)
					ElseIf MainMenuTab = MenuTab_Options_Audio Then
						Rect(x+158*MenuScale,y+13*MenuScale,(width/5.3)+10*MenuScale,(height/2.6)+10*MenuScale,False)
					ElseIf MainMenuTab = MenuTab_Options_Controls Then
						Rect(x+298*MenuScale,y+13*MenuScale,(width/5.3)+10*MenuScale,(height/2.6)+10*MenuScale,False)
					ElseIf MainMenuTab = MenuTab_Options_Advanced Then
						Rect(x+438*MenuScale,y+13*MenuScale,(width/5.3)+10*MenuScale,(height/2.6)+10*MenuScale,False)
					EndIf
					
					Color 255,255,255
					
					SetFont fo\Font[Font_Default]
					y = y + 70 * MenuScale
				Else
					x = 60 * MenuScale
					y = y + height + 20 * MenuScale
					width = 580 * MenuScale
					If MainMenuTab <> MenuTab_Options_ControlsBinding Then
						height = 480 * MenuScale
					Else
						height = 500 * MenuScale
					EndIf
					DrawFrame(x, y, width, height)
					SetFont fo\Font[Font_Default]
					y = y + 30 * MenuScale
				EndIf
				
				If MainMenuTab = MenuTab_Options_Graphics Then
					;[Block]
					height = 400 * MenuScale
					DrawFrame(x, y, width, height)
					
					y=y+30*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","gamma")+":")
					If MouseAndControllerSelectBox(x+300*MenuScale,y-6*MenuScale,170*MenuScale+14,20,6,MainMenuTab) And OnSliderID=0
						DrawOptionsTooltip("gamma",ScreenGamma)
					EndIf
					
					y=y+35*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","fov")+":")
					If MouseOn(x+300*MenuScale,y-6*MenuScale,170*MenuScale+14,20)
						DrawOptionsTooltip("fov",FOV)
					EndIf
					
					y=y+35*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","framelimit")+":")
					If CurrFrameLimit>0.0
						If MouseAndControllerSelectBox(x+300*MenuScale,y-6*MenuScale,170*MenuScale+14,20*MenuScale,7,MainMenuTab)
							DrawOptionsTooltip("framelimit",Framelimit)
						EndIf
						If MouseAndControllerSelectBox(x+215*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,8,MainMenuTab)
							DrawOptionsTooltip("framelimit",Framelimit)
						EndIf
					EndIf
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,8,MainMenuTab)
						DrawOptionsTooltip("framelimit",Framelimit)
					EndIf
					
					y=y+35*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","vsync")+":")
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,3,MainMenuTab) And OnSliderID=0
						DrawOptionsTooltip("vsync")
					EndIf
					
					y=y+37*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","texquality")+":")
					If (MouseAndControllerSelectBox(x + 315 * MenuScale, y-6*MenuScale, 150*MenuScale+14, 20, 8, MainMenuTab) And OnSliderID=0) Lor OnSliderID=3
						DrawOptionsTooltip("texquality")
					EndIf
					
					y=y+37*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","texfiltering")+":")
					If (MouseAndControllerSelectBox(x + 315 * MenuScale, y-6*MenuScale, 150*MenuScale+14, 20, 8, MainMenuTab) And OnSliderID=0) Lor OnSliderID=3
						DrawOptionsTooltip("texfiltering")
					EndIf
					
					y=y+37*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","particleamount")+":")
					If (MouseAndControllerSelectBox(x + 315 * MenuScale, y-6*MenuScale, 150*MenuScale+14, 20, 7, MainMenuTab) And OnSliderID=0) Lor OnSliderID=2
						DrawOptionsTooltip("particleamount",ParticleAmount)
					EndIf
					
					y=y+37*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","cubemap")+":")
					If (MouseAndControllerSelectBox(x + 315 * MenuScale, y-6*MenuScale, 150*MenuScale+14, 20, 10, MainMenuTab) And OnSliderID=0) Lor OnSliderID=4
						DrawOptionsTooltip("cubemap",opt\RenderCubeMapMode)
					EndIf
					
					y=y+40*MenuScale
					If MainMenuOpen Then
						Color 255,255,255
					Else
						Color 100,100,100
					EndIf
					Text(x + 20 * MenuScale, y, GetLocalString("Options","bumpmap")+":")
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,2,MainMenuTab) And OnSliderID=0
						DrawOptionsTooltip("bump")
					EndIf
					
					y=y+35*MenuScale
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","roomlights")+":")
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,5,MainMenuTab) And OnSliderID=0
						DrawOptionsTooltip("roomlights")
					EndIf
					
					;[End Block]
				ElseIf MainMenuTab = MenuTab_Options_Audio Then
					;[Block]
					height = 250 * MenuScale
					DrawFrame(x, y, width, height)	
					
					y = y + 30*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","masterv")+":")
					If MouseAndControllerSelectBox(x+310*MenuScale,y-6*MenuScale,150*MenuScale+14,20,2,MainMenuTab)
						DrawOptionsTooltip("mastervol",aud\MasterVol)
					EndIf
					
					y = y + 35*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","musicv")+":")
					If MouseAndControllerSelectBox(x+310*MenuScale,y-6*MenuScale,150*MenuScale+14,20,3,MainMenuTab)
						DrawOptionsTooltip("musicvol",aud\MusicVol)
					EndIf
					
					y = y + 35*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","soundv")+":")
					If MouseAndControllerSelectBox(x+310*MenuScale,y-6*MenuScale,150*MenuScale+14,20,3,MainMenuTab)
						DrawOptionsTooltip("soundvol",aud\EnviromentVol)
					EndIf
					
					y = y + 35*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","voicev")+":")
					If MouseAndControllerSelectBox(x+310*MenuScale,y-6*MenuScale,150*MenuScale+14,20,3,MainMenuTab)
						DrawOptionsTooltip("voicevol",aud\VoiceVol)
					EndIf
					
					y = y + 35*MenuScale
					
					Color 255,255,255
					Text x + 20 * MenuScale, y, GetLocalString("Options","sfxautorelease")+":"
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,4,MainMenuTab)
						DrawOptionsTooltip("sfxautorelease")
					EndIf
					
					y = y + 35*MenuScale
					
					Color(255, 255, 255)
					Text(x + 20 * MenuScale, y , GetLocalString("options", "subtitles"))
					If MouseAndControllerSelectBox(x+310*MenuScale,y-6*MenuScale,150*MenuScale+14,20,3,MainMenuTab)
						DrawOptionsTooltip("subtitles")
					EndIf
					
;					If opt\EnableSubtitles
;						y = y + (30 * MenuScale)
;						
;						Text2(x, y + (5 * MenuScale), GetLocalString("options", "subtitles.color"))
;						
;						y = y + (5 * MenuScale)
;						
;						If MouseOn(x + (210 * MenuScale), y, 147 * MenuScale, 147 * MenuScale) Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_SubtitlesColor)
;						
;						If opt\OverrideSubColor
;							y = y + (30 * MenuScale)
;							
;							Text2(x, y + (5 * MenuScale), GetLocalString("options", "subtitles.color.red"))
;							If MouseOn(x + (105 * MenuScale), y, 40 * MenuScale, 20 * MenuScale) Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_SubtitlesColor)
;							
;							y = y + (30 * MenuScale)
;							
;							Text2(x, y + (5 * MenuScale), GetLocalString("options", "subtitles.color.green"))
;							If MouseOn(x + (105 * MenuScale), y, 40 * MenuScale, 20 * MenuScale) Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_SubtitlesColor)
;							
;							y = y + (30 * MenuScale)
;							
;							Text2(x, y + (5 * MenuScale), GetLocalString("options", "subtitles.color.blue"))
;							If MouseOn(x + (105 * MenuScale), y, 40 * MenuScale, 20 * MenuScale) Then RenderOptionsTooltip(tX, tY, tW, tH, Tooltip_SubtitlesColor)
;						EndIf
;					EndIf
					
					y = y + 30*MenuScale
					;[End Block]
				ElseIf MainMenuTab = MenuTab_Options_Controls Then
					;[Block]
					height = 270 * MenuScale
					DrawFrame(x, y, width, height)
					
					y = y + 30*MenuScale
					
					Color(255, 255, 255)
					Text(x + 20 * MenuScale, y, GetLocalString("Options","sensitivity")+":")
					If MouseOn(x+310*MenuScale,y-6*MenuScale,150*MenuScale+14,20)
						DrawOptionsTooltip("mousesensitivity",MouseSens)
					EndIf
					
					y = y + 35*MenuScale
					
					Color(255, 255, 255)
					Text(x + 20 * MenuScale, y, GetLocalString("Options","smoothing")+":")
					If MouseOn(x+310*MenuScale,y-6*MenuScale,150*MenuScale+14,20)
						DrawOptionsTooltip("mousesmoothing",opt\MouseSmooth)
					EndIf
					
					y = y + 35*MenuScale
					
					Color(255, 255, 255)
					Text(x + 20 * MenuScale, y, GetLocalString("Options","invert")+":")
					If MouseOn(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale)
						DrawOptionsTooltip("mouseinvert")
					EndIf
					
					y = y + 35*MenuScale
					
					Color(255, 255, 255)
					Text(x + 20 * MenuScale, y, GetLocalString("Options","holdtoaim")+":")
					If MouseOn(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale)
						DrawOptionsTooltip("holdtoaim")
					EndIf
					
					y = y + 35*MenuScale
					
					Color(255, 255, 255)
					Text(x + 20 * MenuScale, y, GetLocalString("Options","holdtocrouch")+":")
					If MouseOn(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale)
						DrawOptionsTooltip("holdtocrouch")
					EndIf
					
					y = y + 35*MenuScale
					;[End Block]
				ElseIf MainMenuTab = MenuTab_Options_ControlsBinding Then
					;[Block]
					Text(x + 20 * MenuScale, y - 10 * MenuScale, GetLocalString("Options","control_configuration"))
					y = y + 10 * MenuScale
					
					Text(x + 20 * MenuScale, y + 15 * MenuScale, GetLocalString("Options","cont_forward"))
					Text(x + 20 * MenuScale, y + 35 * MenuScale, GetLocalString("Options","cont_back"))
					Text(x + 20 * MenuScale, y + 55 * MenuScale, GetLocalString("Options","cont_left"))
					Text(x + 20 * MenuScale, y + 75 * MenuScale, GetLocalString("Options","cont_right"))
					Text(x + 20 * MenuScale, y + 95 * MenuScale, GetLocalString("Options","cont_crouch"))
					Text(x + 20 * MenuScale, y + 115 * MenuScale, GetLocalString("Options","cont_sprint"))
					
					Text(x + 20 * MenuScale, y + 155 * MenuScale, GetLocalString("Options","cont_holster"))
					Text(x + 20 * MenuScale, y + 175 * MenuScale, GetLocalString("Options","cont_reload"))
					Text(x + 20 * MenuScale, y + 195 * MenuScale, GetLocalString("Options","cont_attachment"))
					
					Text(x + 20 * MenuScale, y + 235 * MenuScale, GetLocalString("Options","cont_blink"))
					Text(x + 20 * MenuScale, y + 255 * MenuScale, GetLocalString("Options","cont_inventory"))
					Text(x + 20 * MenuScale, y + 275 * MenuScale, GetLocalString("Options","cont_interact"))
					
					Text(x + 20 * MenuScale, y + 315 * MenuScale, GetLocalString("Options","cont_chat"))
					Text(x + 20 * MenuScale, y + 335 * MenuScale, GetLocalString("Options","cont_commandwheel"))
					Text(x + 20 * MenuScale, y + 355 * MenuScale, GetLocalString("Options","cont_socialwheel"))
					
					Text(x + 20 * MenuScale, y + 395 * MenuScale, GetLocalString("Options","cont_console"))
					Text(x + 20 * MenuScale, y + 415 * MenuScale, GetLocalString("Options","cont_save"))
					Text(x + 20 * MenuScale, y + 435 * MenuScale, GetLocalString("Options","cont_load"))
					
					If MouseOn(x+20*MenuScale,y,width-40*MenuScale,420*MenuScale)
						DrawOptionsTooltip("controls")
					EndIf
					;[End Block]
				ElseIf MainMenuTab = MenuTab_Options_Advanced Then
					;[Block]
					height = 140 * MenuScale
					DrawFrame(x, y, width, height)	
					
					y = y + 30*MenuScale
					
					Color 255,255,255				
					Text(x + 20 * MenuScale, y, GetLocalString("Options","hud")+":")
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,2,MainMenuTab)
						DrawOptionsTooltip("hud")
					EndIf
					
					y = y + 35*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","showFPS")+":")
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,6,MainMenuTab)
						DrawOptionsTooltip("showfps")
					EndIf
					
					y = y + 35*MenuScale
					
					Color 255,255,255
					Text(x + 20 * MenuScale, y, GetLocalString("Options","consoleenable")+":")
					If MouseAndControllerSelectBox(x+375*MenuScale,y-6*MenuScale,20*MenuScale,20*MenuScale,3,MainMenuTab)
						DrawOptionsTooltip("consoleenable")
					EndIf
					
					;[End Block]
				EndIf
				;[End Block]	
			Case MenuTab_Chapters
				;[Block]
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 470 * MenuScale
				
				DrawFrame(x, y, width, height)
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, GetLocalString("Menu","chapter")+" "+Int(CurrLoadGamePage), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 296 * MenuScale	
				
				SetFont fo\Font[Font_Menu]
				
				DrawFrame(x+50*MenuScale,y+490*MenuScale,width-100*MenuScale,55*MenuScale)
				
				If CurrLoadGamePage < 9 Then 
					
				Else
					DrawFrame(x+530*MenuScale, y + 490*MenuScale, 51*MenuScale, 55*MenuScale)
					Color(100, 100, 100)
					Text(x+555*MenuScale, y + 517*MenuScale, ">", True, True)
				EndIf
				If CurrLoadGamePage > 0 Then
					
				Else
					DrawFrame(x, y + 490*MenuScale, 51*MenuScale, 55*MenuScale)
					Color(100, 100, 100)
					Text(x+25*MenuScale, y + 517*MenuScale, "<", True, True)
				EndIf
				
				Color 255,255,255
				Text(x+(width/2.0),y+516*MenuScale, GetLocalString("Menu","page")+" "+Int(CurrLoadGamePage+1)+"/"+10,True,True)
				
				SetFont fo\Font[Font_Default]
				
				x = x + 20 * MenuScale
				y = y + 20 * MenuScale
				
				SetFont fo\Font[Font_Default]
				;[End Block]
			Case MenuTab_Extras
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, GetLocalString("Menu","extra"), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 190 * MenuScale
				
				DrawFrame(x, y, width, height)
				;[End Block]	
			Case MenuTab_Achievements
				;[Block]
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 470 * MenuScale
				
				DrawFrame(x, y, width, height)
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, GetLocalString("Menu","achievements"), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 296 * MenuScale	
				
				SetFont fo\Font[Font_Menu]
				
				DrawFrame(x+50*MenuScale,y+470*MenuScale,width-100*MenuScale,55*MenuScale)
				
				If CurrLoadGamePage < Ceil(Float(MAXACHIEVEMENTS)/4.0)-1 Then 
					
				Else
					DrawFrame(x+530*MenuScale, y + 470*MenuScale, 51*MenuScale, 55*MenuScale)
					Color(100, 100, 100)
					Text(x+555*MenuScale, y + 497*MenuScale, ">", True, True)
				EndIf
				If CurrLoadGamePage > 0 Then
					
				Else
					DrawFrame(x, y + 470*MenuScale, 51*MenuScale, 55*MenuScale)
					Color(100, 100, 100)
					Text(x+25*MenuScale, y + 497*MenuScale, "<", True, True)
				EndIf
				
				Color 255,255,255
				Text(x+(width/2.0),y+496*MenuScale, GetLocalString("Menu","page")+Int(Max((CurrLoadGamePage+1),1))+"/"+Int(Max((Int(Ceil(Float(MAXACHIEVEMENTS)/4.0))),1)),True,True)
				
				SetFont fo\Font[Font_Default]
				
				x = x + 20 * MenuScale
				y = y + 20 * MenuScale
				
				SetFont fo\Font[Font_Default]
				For i% = (1+(4*CurrLoadGamePage)) To 4+(4*CurrLoadGamePage)
					If i <= MAXACHIEVEMENTS And i>0 Then
						DrawFrame(x,y,540 * MenuScale, 100 * MenuScale)
						
						Color 0,0,0
						Rect x + 3 * MenuScale, y + 3 * MenuScale, ImageWidth(achv\AchvIMG[i - 1]),ImageHeight(achv\AchvIMG[i - 1]) + 7 * MenuScale,True
						
						If achv\Achievement[i - 1] = False Then
							DrawImage achv\AchvLocked,x + 3 * MenuScale, y + 7.5 * MenuScale
							Color 255,0,0
							Text(x + 100 * MenuScale, y + 10 * MenuScale,  GetLocalString("Menu","locked"))
						Else
							DrawImage achv\AchvIMG[i - 1],x + 3 * MenuScale, y + 7.5 * MenuScale
							Color 255,255,255
							Text(x + 100 * MenuScale, y + 10 * MenuScale, achv\AchievementStrings[i - 1])
							Text(x + 100 * MenuScale, y + (10+22) * MenuScale, achv\AchievementDescs[i - 1])
						EndIf
						
						y = y + 110 * MenuScale
					Else
						Exit
					EndIf
				Next
				;[End Block]
			Case MenuTab_Singleplayer
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, Upper(GetLocalString("Menu", "singleplayer")), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				temp = 0
				For sa = Each Save
					If sa\Name = m_I\CurrentSave Then
						If sa\Version = CompatibleNumber Then
							temp = 1
						EndIf
						Exit
					EndIf
				Next
				height = 200 + (80 * temp) * MenuScale
				
				DrawFrame(x, y, width, height)
				;[End Block]
			Case MenuTab_MissionMode
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, Upper(GetLocalString("Menu", "mission")), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 330 * MenuScale
				
				DrawFrame(x, y, width, height)
				
				Color 255,255,255
				
				Local mi.Mission
				For mi = Each Mission
					
				Next
				
				SetFont fo\Font[Font_Menu]
				
				If MainMenuTab=16
					
				Else
					
				EndIf
				;[End Block]
			Case MenuTab_Gamemodes
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				DrawFrame(x, y, width, height)
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, Upper(GetLocalString("Story Modes", "story_mode_descr")), True, True)
				;[End Block]
			Case MenuTab_ChallengeMode
				;[Block]
				
				;[End Block]
			Case MenuTab_Lobby
				;[Block]
				
				;[End Block]	
			Case MenuTab_Serverlist
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, Upper(GetLocalString("Serverlist", "join_server")), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				height = 560 * MenuScale
				width = 1130 * MenuScale
				
				x2 = x
				
				DrawFrame(x, y, width, height)
				
				SetFont fo\Font[Font_Default_Medium]
				If mp_I\ServerListAmount = 1 Then
					Text x + 20 * MenuScale, y + 50 * MenuScale, GetLocalString("Serverlist", "no_servers")
				EndIf
				
				;Lock icon for servers (when the server is set to private)
				width = 40 * MenuScale
				height = 40 * MenuScale
				DrawFrame(x, y, width, height)
				
				SetFont fo\Font[Font_Default]
				
				For i = 0 To (SERVER_LIST_SORT_CATEGORY_MAX-1)
					x = x + width
					Select i
						Case 0
							;Server name
							width = 500 * MenuScale
						Case 1, 2
							;Server gamemode
							;Server map
							width = 200 * MenuScale
						Case 3
							;Player amount
							width = 150 * MenuScale
					End Select
				Next
				
				;Server ping
				x = x + width
				width = 40 * MenuScale
				DrawFrame(x, y, width, height)
				
				y = y + height
				
				;Server list itself
				For i = (1+(16*mp_I\ServerListPage)) To 16+(16*mp_I\ServerListPage)
					If i <= mp_I\ServerListAmount
						For se = Each Server
							If se\ID = i Then
								x = x2
								width = 40 * MenuScale
								height = 30 * MenuScale
								DrawFrame(x+3*MenuScale, y, width-3*MenuScale, height, 0, 0, 1024, 1024, 1)
								Local isSelected% = (se\ID = mp_I\SelectedListServer)
								If (MouseOn(x2,y,1130*MenuScale,height) Lor isSelected) And mp_I\ServerMSG = SERVER_MSG_NONE Then
									Color 30+(30*isSelected),30+(30*isSelected),30+(30*isSelected)
									Rect x+4*MenuScale,y+1*MenuScale,width-6*MenuScale,height-2*MenuScale
								EndIf
								If se\password Then
									DrawImage mp_I\ServerIcon, x+4*MenuScale+(((width-6*MenuScale)/2)-(ImageWidth(mp_I\ServerIcon)/2)), y+2*MenuScale, se\password-1
								EndIf	
								x = x + width
								width = 500*MenuScale
								DrawFrame(x, y, width, height, 0, 0, 1024, 1024, 1)
								If (MouseOn(x2,y,1130*MenuScale,height) Lor isSelected) And mp_I\ServerMSG = SERVER_MSG_NONE Then
									Color 30+(30*isSelected),30+(30*isSelected),30+(30*isSelected)
									Rect x+1*MenuScale,y+1*MenuScale,width-2*MenuScale,height-2*MenuScale
								EndIf
								Color 255,255,255
								Text(x + width / 2, y + height / 2, "[" + se\region + "] " + se\name, True, True)
								x = x + width
								width = 200 * MenuScale
								DrawFrame(x, y, width, height, 0, 0, 1024, 1024, 1)
								If (MouseOn(x2,y,1130*MenuScale,height) Lor isSelected) And mp_I\ServerMSG = SERVER_MSG_NONE Then
									Color 30+(30*isSelected),30+(30*isSelected),30+(30*isSelected)
									Rect x+1*MenuScale,y+1*MenuScale,width-2*MenuScale,height-2*MenuScale
								EndIf
								Color 255,255,255
								Text(x + width / 2, y + height / 2,  se\gamemode, True, True)
								x = x + width
								width = 200 * MenuScale
								DrawFrame(x, y, width, height, 0, 0, 1024, 1024, 1)
								If (MouseOn(x2,y,1130*MenuScale,height) Lor isSelected) And mp_I\ServerMSG = SERVER_MSG_NONE Then
									Color 30+(30*isSelected),30+(30*isSelected),30+(30*isSelected)
									Rect x+1*MenuScale,y+1*MenuScale,width-2*MenuScale,height-2*MenuScale
								EndIf
								Color 255,255,255
								Text(x + width / 2, y + height / 2, se\map, True, True)
								x = x + width
								width = 150 * MenuScale
								DrawFrame(x, y, width, height, 0, 0, 1024, 1024, 1)
								If (MouseOn(x2,y,1130*MenuScale,height) Lor isSelected) And mp_I\ServerMSG = SERVER_MSG_NONE Then
									Color 30+(30*isSelected),30+(30*isSelected),30+(30*isSelected)
									Rect x+1*MenuScale,y+1*MenuScale,width-2*MenuScale,height-2*MenuScale
								EndIf
								Color 255,255,255
								Text(x + width / 2, y + height / 2, se\pl_on+"/"+se\pl_max, True, True)
								x = x + width
								width = 40 * MenuScale
								DrawFrame(x, y, width-3*MenuScale, height, 0, 0, 1024, 1024, 1)
								If (MouseOn(x2,y,1130*MenuScale,height) Lor isSelected) And mp_I\ServerMSG = SERVER_MSG_NONE Then
									Color 30+(30*isSelected),30+(30*isSelected),30+(30*isSelected)
									Rect x+1*MenuScale,y+1*MenuScale,width-6*MenuScale,height-2*MenuScale
								EndIf
								Color 255,255,255
								DrawImage mp_I\ConnectionIcons, x+2*MenuScale+(((width-6*MenuScale)/2)-(ImageWidth(mp_I\ServerIcon)/2)), y+2*MenuScale, Steam_GetConnectionQuality(se\region)
								y=y+height
							EndIf
						Next
					Else
						Exit
					EndIf
				Next
				
				;Buttons and stuff at bottom
				y = 286 * MenuScale
				height = 70 * MenuScale
				y = y + height + 20 * MenuScale
				y = y + 520 * MenuScale
				height = 40 * MenuScale
				x = 60 * MenuScale
				width = 1130 * MenuScale
				DrawFrame(x, y, width, height)
				
				If mp_I\ServerMSG = SERVER_MSG_NONE Then
					If mp_I\SelectedListServer = 0 Then
						DrawFrame(x + width - 210 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale)
						Color(100, 100, 100)
						Text(x + width - 110 * MenuScale, y + height - 21 * MenuScale, GetLocalString("Serverlist", "join_server"), True, True)
					EndIf
					
					x = 59 * MenuScale
					y = 286 * MenuScale
					height = 70 * MenuScale
					SetFont fo\Font[Font_Default_Large]
					If mp_I\ServerListPage >= Ceil(Float(mp_I\ServerListPage)/16.0)-1 Then 
						DrawFrame(x+width-height,y, height, height)
						Color(100, 100, 100)
						Text(x+(1130*MenuScale)-(height/2), y + height/2, "▶", True, True)
					EndIf
					If mp_I\ServerListPage = 0 Then 
						DrawFrame(x+width-height-280*MenuScale,y, height, height)
						Color(100, 100, 100)
						Text(x+(1130*MenuScale)-(height/2)-280*MenuScale, y + height/2, "◀", True, True)
					EndIf
					SetFont fo\Font[Font_Menu]
				Else
					SetFont fo\Font[Font_Default]
					
					DrawFrame(x + 10 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale)
					Color(100, 100, 100)
					Text(x + 110 * MenuScale, y + height - 21 * MenuScale, GetLocalString("Serverlist", "host_server"), True, True)
					
					DrawFrame(x + width*0.25 + 10 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale)
					Color(100, 100, 100)
					Text(x + width*0.25 + 110 * MenuScale, y + height - 21 * MenuScale, GetLocalString("Serverlist", "refresh_list"), True, True)
					
					DrawFrame(x + width*0.55 + 10 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale)
					Color(100, 100, 100)
					Text(x + width*0.55 + 110 * MenuScale, y + height - 21 * MenuScale, GetLocalString("Serverlist", "join_friend"), True, True)
					
					DrawFrame(x + width - 210 * MenuScale, y + height - 35 * MenuScale, 200 * MenuScale, 30 * MenuScale)
					Color(100, 100, 100)
					Text(x + width - 110 * MenuScale, y + height - 21 * MenuScale, GetLocalString("Serverlist", "join_server"), True, True)
					
					DrawFrame(x + width * 0.3 - 20 * MenuScale, y - 350 * MenuScale, 500 * MenuScale, 200 * MenuScale)
					
					x = 59 * MenuScale
					y = 286 * MenuScale
					height = 70 * MenuScale
					SetFont fo\Font[Font_Default_Large]
					DrawFrame(x+width-height,y, height, height)
					Color(100, 100, 100)
					Text(x+(1130*MenuScale)-(height/2), y + height/2, "▶", True, True)
					DrawFrame(x+width-height-280*MenuScale,y, height, height)
					Color(100, 100, 100)
					Text(x+(1130*MenuScale)-(height/2)-280*MenuScale, y + height/2, "◀", True, True)
					
					y = 286 * MenuScale
					height = 70 * MenuScale
					y = y + height + 20 * MenuScale
					y = y + 520 * MenuScale
					height = 40 * MenuScale
					x = 60 * MenuScale
					
					SetFont fo\Font[Font_Default]
					If mp_I\ServerMSG = SERVER_MSG_CONNECT Then
						Color 255,255,255
						Text (x + width * 0.3 + 20 * MenuScale, y - 330 * MenuScale, GetLocalStringR("Serverlist", "waiting_seconds", Str(Int(mp_I\ConnectionTime / 70.0))))
						If mp_I\ConnectionRetries > 0 Then
							Text (x + width * 0.3 + 20 * MenuScale, y - 280 * MenuScale, GetLocalStringR("Serverlist", "no_connection_retries", Str(mp_I\ConnectionRetries)))
						EndIf
					ElseIf mp_I\ServerMSG = SERVER_MSG_OFFLINE Then
						Color(255, 255, 255)
						Text (x + width * 0.3 + 20 * MenuScale, y - 330 * MenuScale, GetLocalString("Serverlist", "no_connect"))
						Text (x + width * 0.3 + 20 * MenuScale, y - 280 * MenuScale, GetLocalString("Serverlist", "no_connect2"))
					ElseIf mp_I\ServerMSG = SERVER_MSG_RETRIES Then
						Color(255, 255, 255)
						Text (x + width * 0.3 + 20 * MenuScale, y - 330 * MenuScale, GetLocalString("Serverlist", "retry_failed"))
						Text (x + width * 0.3 + 20 * MenuScale, y - 280 * MenuScale, GetLocalStringR("Serverlist", "retry_failed2", Str(MAX_RETRIES)))
					ElseIf mp_I\ServerMSG = SERVER_MSG_PASSWORD Then
						Color(255, 255, 255)
						Text (x + width * 0.3 + 20 * MenuScale, y - 330 * MenuScale, GetLocalString("Serverlist", "password") + ":")
						
						If (mp_I\ConnectPassword = "" And mp_I\ServerMSG = SERVER_MSG_PASSWORD) Then
							y = 286 * MenuScale
							height = 70 * MenuScale
							x = 60 * MenuScale
							y = y + height + 20 * MenuScale
							height = 560 * MenuScale
							width = 1130 * MenuScale
							DrawFrame(x + width * 0.3 + 280 * MenuScale, y + 300 * MenuScale, 150 * MenuScale, 40 * MenuScale)
							SetFont fo\Font[Font_Default]
							Color(100, 100, 100)
							Text(x + width * 0.3 + 355 * MenuScale, y + 320 * MenuScale, GetLocalString("Menu", "continue"), True, True)
						EndIf
					ElseIf mp_I\ServerMSG = SERVER_MSG_PWAIT Then
						Color(255, 255, 255)
						Text (x + width * 0.3 + 20 * MenuScale, y - 330 * MenuScale, GetLocalString("Serverlist", "password_check"))
					Else
						Color(255, 255, 255)
						Local message$ = ""
						Select mp_I\ServerMSG
							Case SERVER_MSG_QUIT
								message = "has_quit"
							Case SERVER_MSG_TIMEOUT
								message = "has_timeout"
							Case SERVER_MSG_KICK_MANYPLAYERS
								message = "has_kicked_full"
							Case SERVER_MSG_KICK_PASSWORD
								message = "has_kicked_password"
							Case SERVER_MSG_KICK_ENCRYPTION
								message = "has_kicked_encryption"
							Case SERVER_MSG_KICK_VERSION
								message = "has_kicked_version"
							Case SERVER_MSG_KICK_KICKED
								message = "has_kicked_kicked"
							Case SERVER_MSG_KICK_BANNED
								message = "has_kicked_banned"
						End Select
						Text (x + width * 0.3 + 20 * MenuScale, y - 330 * MenuScale, GetLocalString("Serverlist", message))
						If mp_I\KickReason <> "" Then
							Text (x + width * 0.3 + 20 * MenuScale, y - 280 * MenuScale, mp_I\KickReason)
						EndIf
					EndIf
				EndIf
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				height = 70 * MenuScale
				SetFont fo\Font[Font_Default_Medium]
				DrawFrame(x+width-height-210*MenuScale,y,height+140*MenuScale,height)
				Color 255,255,255
				Text(x+width-height-210*MenuScale+((height+140*MenuScale)/2),y+(height/2),GetLocalString("Menu", "page")+" "+Int(Max((mp_I\ServerListPage+1),1))+"/"+Int(Max((Int(Ceil(Float(mp_I\ServerListAmount-1)/16.0))),1)),True,True)
				
				;[End block]
			Case MenuTab_HostServer
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, GetLocalString("Serverlist", "host_server"), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 360 * MenuScale
				DrawFrame(x, y, width, height)
				
				SetFont fo\Font[Font_Default]
				
				Text (x + 20 * MenuScale, y + 25 * MenuScale, GetLocalString("Serverlist", "server_name"))
				
				Text (x + 20 * MenuScale, y + 65 * MenuScale, GetLocalString("Serverlist", "password"))
				
				DrawImage mp_I\PasswordIcon, x + 470 * MenuScale, y + 55 * MenuScale, (mp_I\PasswordVisible)
				If MouseOn(x + 470 * MenuScale, y + 55 * MenuScale, 30 * MenuScale, 30 * MenuScale) Then
					Color 150,150,150
				Else
					Color 255,255,255
				EndIf
				Rect x + 470 * MenuScale, y + 55 * MenuScale, 30 * MenuScale, 30 * MenuScale, False
				
				Color 255,255,255
				
				Text (x + 20 * MenuScale, y + 105 * MenuScale, GetLocalString("Serverlist", "gamemode"))
				
				Text (x + 20 * MenuScale, y + 145 * MenuScale, GetLocalString("Serverlist", "map"))
				
				Text (x + 20 * MenuScale, y + 185 * MenuScale, GetLocalString("Serverlist", "max_players"))
				
				Text (x + 20 * MenuScale, y + 225 * MenuScale, GetLocalString("Serverlist", "server_timeout"))
				
				Text (x + 20 * MenuScale, y + 265 * MenuScale, GetLocalString("Serverlist", "private"))
				If MouseOn(x+375*MenuScale,y+260*MenuScale,20*MenuScale,20*MenuScale)
					DrawOptionsTooltip("private")
				EndIf
				
				Text (x + 20 * MenuScale, y + 300 * MenuScale, GetLocalString("Serverlist", "otherteam"))
				If MouseOn(x+375*MenuScale,y+295*MenuScale,20*MenuScale,20*MenuScale)
					DrawOptionsTooltip("otherteam")
				EndIf
				
				If mp_O\Gamemode\ID = Gamemode_Waves Then
					Text (x + 20 * MenuScale, y + 335 * MenuScale, GetLocalString("Serverlist", "hardcore"))
					If MouseOn(x+375*MenuScale,y+330*MenuScale,20*MenuScale,20*MenuScale)
						DrawOptionsTooltip("hardcore")
					EndIf
				EndIf
				;[End Block]
			Case MenuTab_SelectMPMap
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu]
				Text(x + width / 2, y + height / 2, GetLocalString("Serverlist", "select_map"), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 45 * MenuScale + ImageHeight(mp_O\MapInList\Image) + FRAME_THICK * MenuScale
				
				DrawFrame(x, y, width, height)
				
				width = width - 190 * MenuScale
				x = x + 190 * MenuScale
				height = 45 * MenuScale
				
				DrawFrame(x, y, width, height)
				SetFont fo\Font[Font_Default_Medium]
				Text x + width / 2, y + height / 2, mp_O\MapInList\Name, True, True
				
				y = y + height - FRAME_THICK * MenuScale
				height = ImageHeight(mp_O\MapInList\Image) + 2 * FRAME_THICK * MenuScale
				
				DrawFrame x, y, width, height
				DrawBlock mp_O\MapInList\Image, x + FRAME_THICK * MenuScale, y + FRAME_THICK * MenuScale
				;[End Block]
			Case MenuTab_SelectMPGamemode
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu_Medium]
				Text(x + width / 2, y + height / 2, GetLocalString("Serverlist", "select_gamemode"), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 45 * MenuScale + ImageHeight(mp_O\Gamemode\Image) + FRAME_THICK * MenuScale + 250 * MenuScale
				
				DrawFrame(x, y, width, height)
				
				width = width - 190 * MenuScale
				x = x + 190 * MenuScale
				height = 45 * MenuScale
				
				DrawFrame(x, y, width, height)
				SetFont fo\Font[Font_Default_Medium]
				Text x + width / 2, y + height / 2, mp_O\Gamemode\name, True, True
				
				y = y + height - FRAME_THICK * MenuScale
				height = ImageHeight(mp_O\Gamemode\Image) + 2 * FRAME_THICK * MenuScale
				
				DrawFrame x, y, width, height
				DrawImage mp_O\Gamemode\Image, x + FRAME_THICK * MenuScale, y + FRAME_THICK * MenuScale
				
				y = y + height - FRAME_THICK * MenuScale
				height = 250 * MenuScale + FRAME_THICK * MenuScale
				DrawFrame(x, y, width, height)
				
				SetFont fo\Font[Font_Default]
				x = x + 2 * FRAME_THICK * MenuScale
				y = y + 2 * FRAME_THICK * MenuScale
				width = width - 4 * FRAME_THICK * MenuScale
				height = height - 4 * FRAME_THICK * MenuScale
				RowText(GetLocalString("Gamemode", "description_" + Lower(mp_O\Gamemode\name)), x, y, width, height)
				y = y + height - 10 * MenuScale
				Text(x, y, GetLocalStringR("Gamemode", "max_player_amount", mp_O\Gamemode\MaxPlayersAllowed))
				;[End Block]
			Case MenuTab_MPGamemodeSettings
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Menu_Medium]
				Text(x + width / 2, y + height / 2, GetLocalString("Serverlist", "gamemode_options"), True, True)
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				
				SetFont fo\Font[Font_Default]
				
				Select mp_O\Gamemode\ID
					Case Gamemode_Waves, Gamemode_EAF
						;[Block]
						height = 120 * MenuScale
						DrawFrame(x, y, width, height)
						
						Text (x + 20 * MenuScale, y + 30 * MenuScale, GetLocalString("Serverlist", "difficulty"))
						
						y=y+50*MenuScale
						
						Text (x + 20 * MenuScale, y + 30 * MenuScale, GetLocalString("Serverlist", "waves"))
						;[End Block]
				End Select
				;[End Block]
		End Select
	EndIf
	
	DrawAllMenuButtons()
	DrawAllMenuTicks()
	DrawAllMenuInputBoxes()
	DrawAllMenuSlideBars()
	DrawAllMenuSliders()
	DrawAllMenuDropdowns()
	
	; ~ Put anything in here that's supposed to be drawn after all the buttons, inputs, etc...
	
	If MainMenuTab<>MenuTab_Default Then
		Select MainMenuTab
			Case MenuTab_Chapters
				;[Block]
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 470 * MenuScale
				
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 580 * MenuScale
				height = 296 * MenuScale	
				
				SetFont fo\Font[Font_Menu]
				
				Color 255,255,255
				
				x = x + 20 * MenuScale
				y = y + 20 * MenuScale
				
				Select CurrLoadGamePage
					Case 0
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						If gopt\GameMode = GAMEMODE_NTF Then
							If I_MIG\ChapterIMG[10] = 0 Then 
								I_MIG\ChapterIMG[10] = LoadImage_Strict("GFX\menu\chapters\NTF\chapter_0.png")
								ResizeImage(I_MIG\ChapterIMG[10], ImageWidth(I_MIG\ChapterIMG[10]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[10]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[10], x , y)
						ElseIf gopt\GameMode = GAMEMODE_DEFAULT Then
							If I_MIG\ChapterIMG[0] = 0 Then 
								I_MIG\ChapterIMG[0] = LoadImage_Strict("GFX\menu\chapters\chapter_0.png")
								ResizeImage(I_MIG\ChapterIMG[0], ImageWidth(I_MIG\ChapterIMG[0]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[0]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[0], x , y)
						ElseIf gopt\GameMode = GAMEMODE_CLASS_D Then
							If I_MIG\ChapterIMG[20] = 0 Then 
								I_MIG\ChapterIMG[20] = LoadImage_Strict("GFX\menu\chapters\D\chapter_0.png")
								ResizeImage(I_MIG\ChapterIMG[20], ImageWidth(I_MIG\ChapterIMG[20]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[20]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[20], x , y)
						EndIf
					Case 1
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						If gopt\GameMode = GAMEMODE_NTF Then
							If I_MIG\ChapterIMG[11] = 0 Then 
								I_MIG\ChapterIMG[11] = LoadImage_Strict("GFX\menu\chapters\NTF\chapter_1.png")
								ResizeImage(I_MIG\ChapterIMG[11], ImageWidth(I_MIG\ChapterIMG[11]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[11]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[11], x , y)
						ElseIf gopt\GameMode = GAMEMODE_DEFAULT Then
							If I_MIG\ChapterIMG[1] = 0 Then 
								I_MIG\ChapterIMG[1] = LoadImage_Strict("GFX\menu\chapters\chapter_1.png")
								ResizeImage(I_MIG\ChapterIMG[1], ImageWidth(I_MIG\ChapterIMG[1]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[1]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[1], x , y)
						ElseIf gopt\GameMode = GAMEMODE_CLASS_D Then
							If I_MIG\ChapterIMG[21] = 0 Then 
								I_MIG\ChapterIMG[21] = LoadImage_Strict("GFX\menu\chapters\D\chapter_1.png")
								ResizeImage(I_MIG\ChapterIMG[21], ImageWidth(I_MIG\ChapterIMG[21]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[21]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[21], x , y)
						EndIf
					Case 2
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						If gopt\GameMode = GAMEMODE_NTF And cpt\NTFUnlocked > 1 Then
							If I_MIG\ChapterIMG[12] = 0 Then 
								I_MIG\ChapterIMG[12] = LoadImage_Strict("GFX\menu\chapters\NTF\chapter_2.png")
								ResizeImage(I_MIG\ChapterIMG[12], ImageWidth(I_MIG\ChapterIMG[12]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[12]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[12], x , y)
						ElseIf gopt\GameMode = GAMEMODE_DEFAULT And cpt\Unlocked > 1 Then
							If I_MIG\ChapterIMG[2] = 0 Then 
								I_MIG\ChapterIMG[2] = LoadImage_Strict("GFX\menu\chapters\chapter_2.png")
								ResizeImage(I_MIG\ChapterIMG[2], ImageWidth(I_MIG\ChapterIMG[2]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[2]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[2], x , y)
						ElseIf gopt\GameMode = GAMEMODE_CLASS_D And cpt\DUnlocked > 1 Then
							If I_MIG\ChapterIMG[22] = 0 Then 
								I_MIG\ChapterIMG[22] = LoadImage_Strict("GFX\menu\chapters\D\chapter_2.png")
								ResizeImage(I_MIG\ChapterIMG[22], ImageWidth(I_MIG\ChapterIMG[22]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[22]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[22], x , y)
						Else
							DrawImage(I_MIG\ChapterIMG[30], x , y)
						EndIf
					Case 3
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						If gopt\GameMode = GAMEMODE_NTF And cpt\NTFUnlocked > 2 Then
							If I_MIG\ChapterIMG[13] = 0 Then 
								I_MIG\ChapterIMG[13] = LoadImage_Strict("GFX\menu\chapters\NTF\chapter_3.png")
								ResizeImage(I_MIG\ChapterIMG[13], ImageWidth(I_MIG\ChapterIMG[13]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[13]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[13], x , y)
						ElseIf gopt\GameMode = GAMEMODE_DEFAULT And cpt\Unlocked > 2 Then
							If I_MIG\ChapterIMG[3] = 0 Then 
								I_MIG\ChapterIMG[3] = LoadImage_Strict("GFX\menu\chapters\chapter_3.png")
								ResizeImage(I_MIG\ChapterIMG[3], ImageWidth(I_MIG\ChapterIMG[3]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[3]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[3], x , y)
						ElseIf gopt\GameMode = GAMEMODE_CLASS_D And cpt\DUnlocked > 2 Then
							If I_MIG\ChapterIMG[23] = 0 Then 
								I_MIG\ChapterIMG[23] = LoadImage_Strict("GFX\menu\chapters\D\chapter_3.png")
								ResizeImage(I_MIG\ChapterIMG[23], ImageWidth(I_MIG\ChapterIMG[23]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[23]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[23], x , y)
						Else
							DrawImage(I_MIG\ChapterIMG[30], x , y)
						EndIf
					Case 4
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						If gopt\GameMode = GAMEMODE_NTF And cpt\NTFUnlocked > 3 Then
							If I_MIG\ChapterIMG[14] = 0 Then 
								I_MIG\ChapterIMG[14] = LoadImage_Strict("GFX\menu\chapters\NTF\chapter_4.png")
								ResizeImage(I_MIG\ChapterIMG[14], ImageWidth(I_MIG\ChapterIMG[14]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[14]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[14], x , y)
						ElseIf gopt\GameMode = GAMEMODE_DEFAULT And cpt\Unlocked > 3 Then
							If I_MIG\ChapterIMG[4] = 0 Then 
								I_MIG\ChapterIMG[4] = LoadImage_Strict("GFX\menu\chapters\chapter_4.png")
								ResizeImage(I_MIG\ChapterIMG[4], ImageWidth(I_MIG\ChapterIMG[4]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[4]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[4], x , y)
						ElseIf gopt\GameMode = GAMEMODE_CLASS_D And cpt\DUnlocked > 3 Then
							If I_MIG\ChapterIMG[24] = 0 Then 
								I_MIG\ChapterIMG[24] = LoadImage_Strict("GFX\menu\chapters\D\chapter_4.png")
								ResizeImage(I_MIG\ChapterIMG[24], ImageWidth(I_MIG\ChapterIMG[24]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[24]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[24], x , y)
						Else
							DrawImage(I_MIG\ChapterIMG[30], x , y)
						EndIf
					Case 5
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						If gopt\GameMode = GAMEMODE_NTF And cpt\NTFUnlocked > 4 Then
							If I_MIG\ChapterIMG[15] = 0 Then 
								I_MIG\ChapterIMG[15] = LoadImage_Strict("GFX\menu\chapters\NTF\chapter_5.png")
								ResizeImage(I_MIG\ChapterIMG[15], ImageWidth(I_MIG\ChapterIMG[15]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[15]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[15], x , y)
						ElseIf gopt\GameMode = GAMEMODE_DEFAULT And cpt\Unlocked > 4 Then
							If I_MIG\ChapterIMG[5] = 0 Then 
								I_MIG\ChapterIMG[5] = LoadImage_Strict("GFX\menu\chapters\chapter_5.png")
								ResizeImage(I_MIG\ChapterIMG[5], ImageWidth(I_MIG\ChapterIMG[5]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[5]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[5], x , y)
						ElseIf gopt\GameMode = GAMEMODE_CLASS_D And cpt\DUnlocked > 4 Then
							If I_MIG\ChapterIMG[25] = 0 Then 
								I_MIG\ChapterIMG[25] = LoadImage_Strict("GFX\menu\chapters\D\chapter_5.png")
								ResizeImage(I_MIG\ChapterIMG[25], ImageWidth(I_MIG\ChapterIMG[25]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[25]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[25], x , y)
						Else
							DrawImage(I_MIG\ChapterIMG[30], x , y)
						EndIf
					Case 6
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						If gopt\GameMode = GAMEMODE_NTF And cpt\NTFUnlocked > 5 Then
							If I_MIG\ChapterIMG[16] = 0 Then 
								I_MIG\ChapterIMG[16] = LoadImage_Strict("GFX\menu\chapters\NTF\chapter_6.png")
								ResizeImage(I_MIG\ChapterIMG[16], ImageWidth(I_MIG\ChapterIMG[16]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[16]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[16], x , y)
						ElseIf gopt\GameMode = GAMEMODE_DEFAULT And cpt\Unlocked > 5 Then
							If I_MIG\ChapterIMG[6] = 0 Then 
								I_MIG\ChapterIMG[6] = LoadImage_Strict("GFX\menu\chapters\chapter_6.png")
								ResizeImage(I_MIG\ChapterIMG[6], ImageWidth(I_MIG\ChapterIMG[6]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[6]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[6], x , y)
						ElseIf gopt\GameMode = GAMEMODE_CLASS_D And cpt\DUnlocked > 5 Then
							If I_MIG\ChapterIMG[26] = 0 Then 
								I_MIG\ChapterIMG[26] = LoadImage_Strict("GFX\menu\chapters\D\chapter_6.png")
								ResizeImage(I_MIG\ChapterIMG[26], ImageWidth(I_MIG\ChapterIMG[26]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[26]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[26], x , y)
						Else
							DrawImage(I_MIG\ChapterIMG[30], x , y)
						EndIf
					Case 7
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						If gopt\GameMode = GAMEMODE_NTF And cpt\NTFUnlocked > 6 Then
							If I_MIG\ChapterIMG[17] = 0 Then 
								I_MIG\ChapterIMG[17] = LoadImage_Strict("GFX\menu\chapters\NTF\chapter_7.png")
								ResizeImage(I_MIG\ChapterIMG[17], ImageWidth(I_MIG\ChapterIMG[17]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[17]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[17], x , y)
						ElseIf gopt\GameMode = GAMEMODE_DEFAULT And cpt\Unlocked > 6 Then
							If I_MIG\ChapterIMG[7] = 0 Then 
								I_MIG\ChapterIMG[7] = LoadImage_Strict("GFX\menu\chapters\chapter_7.png")
								ResizeImage(I_MIG\ChapterIMG[7], ImageWidth(I_MIG\ChapterIMG[7]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[7]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[7], x , y)
						ElseIf gopt\GameMode = GAMEMODE_CLASS_D And cpt\DUnlocked > 6 Then
							If I_MIG\ChapterIMG[27] = 0 Then 
								I_MIG\ChapterIMG[27] = LoadImage_Strict("GFX\menu\chapters\D\chapter_7.png")
								ResizeImage(I_MIG\ChapterIMG[27], ImageWidth(I_MIG\ChapterIMG[27]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[27]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[27], x , y)
						Else
							DrawImage(I_MIG\ChapterIMG[30], x , y)
						EndIf
					Case 8
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						If gopt\GameMode = GAMEMODE_NTF And cpt\NTFUnlocked > 7 Then
							If I_MIG\ChapterIMG[18] = 0 Then 
								I_MIG\ChapterIMG[18] = LoadImage_Strict("GFX\menu\chapters\NTF\chapter_8.png")
								ResizeImage(I_MIG\ChapterIMG[18], ImageWidth(I_MIG\ChapterIMG[18]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[18]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[18], x , y)
						ElseIf gopt\GameMode = GAMEMODE_DEFAULT And cpt\Unlocked > 7 Then
							If I_MIG\ChapterIMG[8] = 0 Then 
								I_MIG\ChapterIMG[8] = LoadImage_Strict("GFX\menu\chapters\chapter_8.png")
								ResizeImage(I_MIG\ChapterIMG[8], ImageWidth(I_MIG\ChapterIMG[8]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[8]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[8], x , y)
						ElseIf gopt\GameMode = GAMEMODE_CLASS_D And cpt\DUnlocked > 7 Then
							If I_MIG\ChapterIMG[28] = 0 Then 
								I_MIG\ChapterIMG[28] = LoadImage_Strict("GFX\menu\chapters\D\chapter_8.png")
								ResizeImage(I_MIG\ChapterIMG[28], ImageWidth(I_MIG\ChapterIMG[28]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[28]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[28], x , y)
						Else
							DrawImage(I_MIG\ChapterIMG[30], x , y)
						EndIf
					Case 9
						y = 286 * MenuScale
						height = 70 * MenuScale
						
						x = 60 * MenuScale
						y = y + height + 20 * MenuScale
						width = 580 * MenuScale
						height = 465 * MenuScale
						
						If gopt\GameMode = GAMEMODE_NTF And cpt\NTFUnlocked > 8 Then
							If I_MIG\ChapterIMG[19] = 0 Then 
								I_MIG\ChapterIMG[19] = LoadImage_Strict("GFX\menu\chapters\NTF\chapter_9.png")
								ResizeImage(I_MIG\ChapterIMG[19], ImageWidth(I_MIG\ChapterIMG[19]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[19]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[19], x , y)
						ElseIf gopt\GameMode = GAMEMODE_DEFAULT And cpt\Unlocked > 8 Then
							If I_MIG\ChapterIMG[9] = 0 Then 
								I_MIG\ChapterIMG[9] = LoadImage_Strict("GFX\menu\chapters\chapter_9.png")
								ResizeImage(I_MIG\ChapterIMG[9], ImageWidth(I_MIG\ChapterIMG[9]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[9]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[9], x , y)
						ElseIf gopt\GameMode = GAMEMODE_CLASS_D And cpt\DUnlocked > 8 Then
							If I_MIG\ChapterIMG[29] = 0 Then 
								I_MIG\ChapterIMG[29] = LoadImage_Strict("GFX\menu\chapters\D\chapter_9.png")
								ResizeImage(I_MIG\ChapterIMG[29], ImageWidth(I_MIG\ChapterIMG[29]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[29]) * MenuScale)
							EndIf
							DrawImage(I_MIG\ChapterIMG[29], x , y)
						Else
							DrawImage(I_MIG\ChapterIMG[30], x , y)
						EndIf
				End Select
			Case MenuTab_Gamemodes
				;[Block]
				x = 59 * MenuScale
				y = 286 * MenuScale
				
				width = 400 * MenuScale
				height = 70 * MenuScale
				
				DrawImage(I_MIG\StoryIMG[STORY_SANDERS_IMG], x + 405 * MenuScale, y +height+ 25 * MenuScale)
				
				DrawImage(I_MIG\StoryIMG[STORY_D_IMG], x + 205 * MenuScale, y +height+ 25 * MenuScale)
				
				DrawImage(I_MIG\StoryIMG[STORY_RYAN_IMG], x + 5 * MenuScale, y +height+ 25 * MenuScale)
				
				If MouseOn(x + 405 * MenuScale, y + height + 25 * MenuScale,ImageWidth(I_MIG\StoryIMG[STORY_SANDERS_IMG]),ImageHeight(I_MIG\StoryIMG[STORY_SANDERS_IMG])) Then
					DrawFrame(x + 605 * MenuScale, y + height + 25 * MenuScale,ImageWidth(I_MIG\StoryIMG[STORY_SANDERS_IMG])*2.4,ImageHeight(I_MIG\StoryIMG[STORY_SANDERS_IMG]))
					Text(x + 608 * MenuScale, y + height + 45 * MenuScale,GetLocalString("Story Modes", "descr_ntf_1"))
					Text(x + 608 * MenuScale, y + height + 65 * MenuScale,GetLocalString("Story Modes", "descr_ntf_2"))
					Text(x + 608 * MenuScale, y + height + 85 * MenuScale,GetLocalString("Story Modes", "descr_ntf_3"))
					Text(x + 608 * MenuScale, y + height + 105 * MenuScale,GetLocalString("Story Modes", "descr_ntf_4"))
					Text(x + 608 * MenuScale, y + height + 125 * MenuScale,GetLocalString("Story Modes", "descr_ntf_5"))
					Text(x + 608 * MenuScale, y + height + 145 * MenuScale,GetLocalString("Story Modes", "descr_ntf_6"))
				EndIf
				If MouseOn(x + 205 * MenuScale, y + height + 25 * MenuScale,ImageWidth(I_MIG\StoryIMG[STORY_D_IMG]),ImageHeight(I_MIG\StoryIMG[STORY_D_IMG])) Then
					DrawFrame(x + 405 * MenuScale, y + height + 25 * MenuScale,ImageWidth(I_MIG\StoryIMG[STORY_D_IMG])*2.4,ImageHeight(I_MIG\StoryIMG[STORY_D_IMG]))
					Text(x + 408 * MenuScale, y + height + 45 * MenuScale,GetLocalString("Story Modes", "descr_d_1"))
					Text(x + 408 * MenuScale, y + height + 65 * MenuScale,GetLocalString("Story Modes", "descr_d_2"))
					Text(x + 408 * MenuScale, y + height + 85 * MenuScale,GetLocalString("Story Modes", "descr_d_3"))
					Text(x + 408 * MenuScale, y + height + 105 * MenuScale,GetLocalString("Story Modes", "descr_d_4"))
				EndIf
				If MouseOn(x + 5 * MenuScale, y + height + 25 * MenuScale,ImageWidth(I_MIG\StoryIMG[STORY_RYAN_IMG]),ImageHeight(I_MIG\StoryIMG[STORY_RYAN_IMG])) Then
					DrawFrame(x + 205 * MenuScale, y + height + 25 * MenuScale,ImageWidth(I_MIG\StoryIMG[STORY_RYAN_IMG])*2.4,ImageHeight(I_MIG\StoryIMG[STORY_RYAN_IMG]))
					Text(x + 208 * MenuScale, y + height + 45 * MenuScale,GetLocalString("Story Modes", "descr_ryan_1"))
					Text(x + 208 * MenuScale, y + height + 65 * MenuScale,GetLocalString("Story Modes", "descr_ryan_2"))
					Text(x + 208 * MenuScale, y + height + 85 * MenuScale,GetLocalString("Story Modes", "descr_ryan_3"))
					Text(x + 208 * MenuScale, y + height + 105 * MenuScale,GetLocalString("Story Modes", "descr_ryan_4"))
					Text(x + 208 * MenuScale, y + height + 125 * MenuScale,GetLocalString("Story Modes", "descr_ryan_5"))
				EndIf
				;[End Block]
			Case MenuTab_Serverlist
				;[Block]
				y = 286 * MenuScale
				height = 70 * MenuScale
				
				x = 60 * MenuScale
				y = y + height + 20 * MenuScale
				width = 40 * MenuScale
				height = 40 * MenuScale
				
				Color(255, 255, 255)
				SetFont fo\Font[Font_Default]
				
				For i = 0 To (SERVER_LIST_SORT_CATEGORY_MAX-1)
					x = x + width
					Select i
						Case 0
							;Server name
							width = 500 * MenuScale
						Case 1, 2
							;Server gamemode
							;Server map
							width = 200 * MenuScale
						Case 3
							;Player amount
							width = 150 * MenuScale
					End Select
					If mp_I\ServerListSort = i * 2 Then
						Text x + width - 20 * MenuScale, y + height / 2, "▲", False, True
					ElseIf mp_I\ServerListSort = i * 2 + 1 Then
						Text x + width - 20 * MenuScale, y + height / 2, "▼", False, True
					EndIf
				Next
				;[End Block]
		End Select
	EndIf
	
	SetFont fo\Font[Font_Default]
	
End Function

Type ChangeLogLines
	Field txt$
End Type

Function UpdateChangelog()
	Local ChangeLogFile
	Local ChangeLogLineAmount% = 0
	Local l$ = ""
	Local chl.ChangeLogLines
	Local canWriteLines% = False
	
	Delete Each ChangeLogLines
	
	InitLanguage()
	UpdateLang(Language$)
	
	ChangeLogFile = OpenFile(I_Loc\LangPath + "changelog.txt")
	
	While Not Eof(ChangeLogFile)
		l$ = ReadLine(ChangeLogFile)
		If Instr(l,"v"+VersionNumber)=1 Then
			canWriteLines = True
		EndIf
		If canWriteLines Then
			If Left(l,5)<>"-----" Then
				chl.ChangeLogLines = New ChangeLogLines
				If Instr(l,"v"+VersionNumber)>0 Then
					chl\txt = GetLocalString("Menu","new_update")+l
				Else
					chl\txt = l
				EndIf
				ChangeLogLineAmount = ChangeLogLineAmount + 1
			Else
				Exit
			EndIf
		EndIf
	Wend
	CloseFile(ChangeLogFile)
	
End Function

Type Resolution
	Field width%
	Field height%
	Field index%
End Type

Function LoadResolutions()
	Local res.Resolution, res2.Resolution, res3.Resolution
	Local isResolutionSelected%, resolutionAmount%, swapWidth%, swapHeight%
	Local i%, n%
	
	For i% = 1 To opt\TotalGFXModes
		Local samefound% = False
		If IsResolutionWidthValid(GfxModeWidth(i)) And IsResolutionHeightValid(GfxModeHeight(i)) Then
			For res = Each Resolution
				If res\width = GfxModeWidth(i) And res\height = GfxModeHeight(i) Then
					samefound = True
					Exit
				EndIf
			Next
			If (Not samefound) Then
				res = New Resolution
				res\width = GfxModeWidth(i)
				res\height = GfxModeHeight(i)
				res\index = resolutionAmount
				resolutionAmount = resolutionAmount + 1
			EndIf
		EndIf
	Next
	
	isResolutionSelected = False
	For res = Each Resolution
		If res\width = DesktopWidth() And res\height = DesktopHeight() Then
			isResolutionSelected% = True
			Exit
		EndIf
	Next
	If (Not isResolutionSelected) Then
		res = New Resolution
		res\width = DesktopWidth()
		res\height = DesktopHeight()
		res\index = resolutionAmount
		resolutionAmount = resolutionAmount + 1
	EndIf
	
	i = 0
	n = 0
	opt\TotalGFXModes = resolutionAmount
	For res = Each Resolution
		swapWidth = res\width
		swapHeight = res\height
		If i > 0 Then
			n = i
			res2 = Before(res)
			res3 = res
			While (n > 0) And (res2\width > swapWidth Lor (res2\width = swapWidth And res2\height > swapHeight))
				res3\width = res2\width
				res3\height = res2\height
				res3 = res2
				res2 = Before(res2)
				n = n - 1
			Wend
			res3\width = swapWidth
			res3\height = swapHeight
		EndIf
		i = i + 1
	Next
	
	isResolutionSelected = False
	For res = Each Resolution
		If opt\GraphicWidth = res\width And opt\GraphicHeight = res\height Then
			isResolutionSelected = True
			Exit
		EndIf
	Next
	If (Not isResolutionSelected) Then
		opt\GraphicWidth = DesktopWidth()
		opt\GraphicHeight = DesktopHeight()
		RealGraphicWidth = opt\GraphicWidth
		RealGraphicHeight = opt\GraphicHeight
	EndIf
	If (Not IsResolutionWidthValid(opt\GraphicWidth)) Lor (Not IsResolutionHeightValid(opt\GraphicHeight)) Then
		res = Last Resolution
		opt\GraphicWidth = res\width
		opt\GraphicHeight = res\height
	EndIf
	
End Function

Const LauncherWidth% = 808
Const LauncherHeight% = 480

Function UpdateLauncher()
	Local res.Resolution
	Local LinesAmount%, isResolutionSelected%
	Local currentWidth%, currentHeight%
	Local i%, n%,chl.ChangeLogLines
	
	MenuScale = 1
	
	;Graphics(640, 480, 0, 4)
	Graphics(LauncherWidth, LauncherHeight, 0, 4)
	
	SetBuffer BackBuffer()
	
	RealGraphicWidth = opt\GraphicWidth
	RealGraphicHeight = opt\GraphicHeight
	
	currentWidth = opt\GraphicWidth
	currentHeight = opt\GraphicHeight
	
	fo\Font[Font_Default] = LoadFont_Strict("GFX\font\Courier New.ttf", 16)
	SetFont fo\Font[Font_Default]	
	LauncherIMG = LoadImage_Strict("GFX\menu\launcher.jpg")
	
	Local LauncherMediaIMG%
	Local LauncherMediaWidth%
	
	LauncherMediaIMG = LoadAnimImage("GFX\menu\launcher_media.png", 64, 64, 0, 3)
	LauncherMediaWidth = ImageWidth(LauncherMediaIMG) / 2
	
	LoadMenuImages(False)
	
	UpdateChangelog()
	
	Local UpdaterIMG% = CreateImage(400,445)
	
	AppTitle GetLocalString("Launcher", "launcher_name")
	
	Repeat
		Local y = 0
		
		Cls
		
		MouseHit1 = MouseHit(1)
		MouseDown1 = MouseDown(1)
		
		MousePosX = MouseX()
		MousePosY = MouseY()
		
		Color 255, 255, 255
		DrawImage(LauncherIMG, 0, 0)
		
		If DrawButton(640 - 32, 0, 32, 32, "X", False, False, False) Then
			PutINIValue(gv\OptionFile, "options", "width", currentWidth)
			PutINIValue(gv\OptionFile, "options", "height", currentHeight)
			PutINIValue(gv\OptionFile, "options", "display mode", opt\DisplayMode)
			PutINIValue(gv\OptionFile, "options", "language", opt\LanguageVal)
			PutINIValue(gv\OptionFile, "options", "enabled 3d menu", opt\Menu3DEnabled)
			PutINIValue(gv\OptionFile, "options", "play startup videos", opt\PlayStartupVideos)
			PutINIValue(gv\OptionFile, "options", "steam enabled", opt\SteamEnabled)
			Steam_Shutdown()
			End
		EndIf
		If DrawButton(640 - 64, 0, 32, 32, "-", False, False, False) Then
			api_ShowWindow(SystemProperty("AppHWND"),6)
		EndIf
		If LinesAmount > 21
			y= 200-(20*ScrollMenuHeight2*ScrollBarY2)
			SetBuffer(ImageBuffer(UpdaterIMG))
			Cls
			LinesAmount%=0
			For chl.ChangeLogLines = Each ChangeLogLines
				Color 255,255,255
				If Instr(chl\txt,"v"+VersionNumber)>0 Then
					Color 200,0,0
				EndIf
				RowText(chl\txt$,5,y-195,390,440)
				y = y+(20*GetLineAmount(chl\txt$,390,440))
				LinesAmount = LinesAmount + (GetLineAmount(chl\txt$,390,440))
			Next
			SetBuffer BackBuffer()
			DrawImage UpdaterIMG,200,45
			ScrollMenuHeight2# = LinesAmount-21
			ScrollBarY2 = DrawScrollBar(620,40,20,440,620,40+(440-Max(440-8*ScrollMenuHeight2,50))*ScrollBarY2,20,Max(440-(8*ScrollMenuHeight2),50),ScrollBarY2,1,SelectedInputBox<>0)
		Else
			y = 200
			LinesAmount%=0
			Color 0,0,0
			For chl.ChangeLogLines = Each ChangeLogLines
				Color 255,255,255
				If Instr(chl\txt,"v"+VersionNumber)>0 Then
					Color 200,0,0
				EndIf
				RowText(chl\txt$,205,y-150,390,440)
				y = y+(20*GetLineAmount(chl\txt$,390,440))
				LinesAmount = LinesAmount + (GetLineAmount(chl\txt$,390,440))
			Next
			ScrollMenuHeight2# = LinesAmount
		EndIf
		
		Color 255,255,255
		Text(670, 10, GetLocalString("Launcher", "resolution") + ":")
		DrawFrame(655, 30, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
		Text(715, 45, currentWidth + "x" + currentHeight, True, True)
		Local frameheight = 0
		y = 0
		Select SelectedInputBox
				
			Case 0 ; ~ Every tab hidden
				
				If DrawButton(770, 30, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 1
				EndIf
				
				Local txt$
				
				Text(665, 360, GetLocalString("Menu", "steam_enabled") + ":")
				DrawFrame(655, 380, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\SteamEnabled
					Case 0
						Color 255,0,0
						txt = GetLocalString("Menu", "startup_off")
					Case 1
						Color 0,255,0
						txt = GetLocalString("Menu", "startup_on")
				End Select
				Text(715, 395, txt, True, True)
				Color 255,255,255
				
				If DrawButton(770, 380, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 5
				EndIf
				
				Text(665, 290, GetLocalString("Menu", "startup_videos") + ":")
				DrawFrame(655, 310, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\PlayStartupVideos
					Case 0
						txt = GetLocalString("Menu", "startup_off")
					Case 1
						txt = GetLocalString("Menu", "startup_on")
				End Select
				Text(715, 325, txt, True, True)
				
				If DrawButton(770, 310, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 4
				EndIf
				
				Text(665, 220, GetLocalString("Menu", "menu_mode") + ":")
				DrawFrame(655, 240, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\Menu3DEnabled
					Case 0
						txt = GetLocalString("Menu", "2d")
					Case 1
						txt = GetLocalString("Menu", "3d")
				End Select
				Text(715, 255, txt, True, True)
				
				If DrawButton(770, 240, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 3
				EndIf
				
				Text(665, 150, GetLocalString("Language", "lang") + ":")
				DrawFrame(655, 170, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\LanguageVal
					Case 0
						txt = GetLocalString("Language", "eng")
					Case 1
						txt = GetLocalString("Language", "rus")
				End Select
				Text(715, 185, txt, True, True)
				
				If DrawButton(770, 170, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 2
				EndIf
				
				Text(665, 80, GetLocalString("Launcher", "mode") + ":")
				DrawFrame(655, 100, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\DisplayMode
					Case 0
						txt = GetLocalString("Launcher", "windowed")
					Case 1
						txt = GetLocalString("Launcher", "fullscreen")
				End Select
				Text(715, 115, txt, True, True)
				
				If DrawButton(770, 100, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = -1
				EndIf
				
			Case -1 ; ~ Display mode selection
				
				If DrawButton(770, 30, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 1
				EndIf
				
				Text(665, 360, GetLocalString("Menu", "steam_enabled") + ":")
				DrawFrame(655, 380, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\SteamEnabled
					Case 0
						Color 255,0,0
						txt = GetLocalString("Menu", "startup_off")
					Case 1
						Color 0,255,0
						txt = GetLocalString("Menu", "startup_on")
				End Select
				Text(715, 395, txt, True, True)
				Color 255,255,255
				
				If DrawButton(770, 380, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 5
				EndIf
				
				Text(665, 290, GetLocalString("Menu", "startup_videos") + ":")
				DrawFrame(655, 310, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\PlayStartupVideos
					Case 0
						txt = GetLocalString("Menu", "startup_off")
					Case 1
						txt = GetLocalString("Menu", "startup_on")
				End Select
				Text(715, 325, txt, True, True)
				
				If DrawButton(770, 310, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 4
				EndIf
				
				Text(665, 220, GetLocalString("Menu", "menu_mode") + ":")
				DrawFrame(655, 240, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\Menu3DEnabled
					Case 0
						txt = GetLocalString("Menu", "2d")
					Case 1
						txt = GetLocalString("Menu", "3d")
				End Select
				Text(715, 255, txt, True, True)
				
				If DrawButton(770, 240, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 3
				EndIf
				
				Text(665, 150, GetLocalString("Language", "lang")+":")
				DrawFrame(655, 170, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				txt$ = ""
				Select opt\LanguageVal
					Case 0
						txt = GetLocalString("Language", "eng")
					Case 1
						txt = GetLocalString("Language", "rus")
				End Select
				Text(715, 185, txt,True,True)
				
				If DrawButton(770, 170, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 2
				EndIf
				
				DrawFrame(655, 117, 145, 70, 0, 0, 1024, 1024, FRAME_THICK, 512)
				For i = 0 To 1
					Select i
						Case 0
							Text(655+(145/2),147+(20*i),GetLocalString("Launcher", "windowed"),True,True)
						Case 1
							Text(655+(145/2),147+(20*i),GetLocalString("Launcher", "fullscreen"),True,True)
					End Select
					If MouseOn(670,137+(20*i),113,20) Then
						Color 100,100,100
						Rect(670,137+(20*i),113,20,False)
						Color 255,255,255
						If MouseHit1 Then
							opt\DisplayMode = i
							SelectedInputBox = 0
							PlaySound_Strict ButtonSFX[2]
							Exit
						EndIf
					EndIf
				Next
				
				Text(665, 80, GetLocalString("Launcher", "mode")+":")
				DrawFrame(655, 100, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				txt$ = ""
				Select opt\DisplayMode
					Case 0
						txt = GetLocalString("Launcher", "windowed")
					Case 1
						txt = GetLocalString("Launcher", "fullscreen")
				End Select
				Text(715, 115, txt,True,True)
				
				If DrawButton(770, 100, 30, 30, "▲", False, False, False) Then
					SelectedInputBox = 0
				EndIf
				
			Case 2 ; ~ Language
				
				If DrawButton(770, 30, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 2
				EndIf
				
				Text(665, 360, GetLocalString("Menu", "steam_enabled") + ":")
				DrawFrame(655, 380, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\SteamEnabled
					Case 0
						Color 255,0,0
						txt = GetLocalString("Menu", "startup_off")
					Case 1
						Color 0,255,0
						txt = GetLocalString("Menu", "startup_on")
				End Select
				Text(715, 395, txt, True, True)
				Color 255,255,255
				
				If DrawButton(770, 380, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 5
				EndIf
				
				Text(665, 290, GetLocalString("Menu", "startup_videos") + ":")
				DrawFrame(655, 310, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\PlayStartupVideos
					Case 0
						txt = GetLocalString("Menu", "startup_off")
					Case 1
						txt = GetLocalString("Menu", "startup_on")
				End Select
				Text(715, 325, txt, True, True)
				
				If DrawButton(770, 310, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 4
				EndIf
				
				Text(665, 220, GetLocalString("Menu", "menu_mode") + ":")
				DrawFrame(655, 240, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\Menu3DEnabled
					Case 0
						txt = GetLocalString("Menu", "2d")
					Case 1
						txt = GetLocalString("Menu", "3d")
				End Select
				Text(715, 255, txt, True, True)
				
				If DrawButton(770, 240, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 3
				EndIf
				
				Text(665, 80, GetLocalString("Launcher", "mode") + ":")
				DrawFrame(655, 100, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\DisplayMode
					Case 0
						txt = GetLocalString("Launcher", "windowed")
					Case 1
						txt = GetLocalString("Launcher", "fullscreen")
				End Select
				Text(715, 115, txt, True, True)
				
				If DrawButton(770, 100, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = -1
				EndIf
				
				DrawFrame(655, 187, 145, 70, 0, 0, 1024, 1024, FRAME_THICK, 512)
				For i = 0 To 1
					Select i
						Case 0
							Text(655+(145/2),217+(20*i),GetLocalString("Language", "eng"),True,True)
						Case 1
							Text(655+(145/2),217+(20*i),GetLocalString("Language", "rus"),True,True)
					End Select
					If MouseOn(670,207+(20*i),113,20) Then
						Color 100,100,100
						Rect(670,207+(20*i),113,20,False)
						Color 255,255,255
						If MouseHit1 Then
							opt\LanguageVal = i
							SelectedInputBox = 0
							UpdateChangelog()
							PlaySound_Strict ButtonSFX[2]
							Exit
						EndIf
					EndIf
				Next
				
				Text(665, 150, GetLocalString("Language", "lang")+":")
				DrawFrame(655, 170, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				txt$ = ""
				Select opt\LanguageVal
					Case 0
						txt = GetLocalString("Language", "eng")
					Case 1
						txt = GetLocalString("Language", "rus")
				End Select
				Text(715, 185, txt,True,True)
				
				If DrawButton(770, 170, 30, 30, "▲", False, False, False) Then
					SelectedInputBox = 0
				EndIf
				
			Case 3 ; ~ Menu mode selection
				
				If DrawButton(770, 30, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 3
				EndIf
				
				Text(665, 360, GetLocalString("Menu", "steam_enabled") + ":")
				DrawFrame(655, 380, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\SteamEnabled
					Case 0
						Color 255,0,0
						txt = GetLocalString("Menu", "startup_off")
					Case 1
						Color 0,255,0
						txt = GetLocalString("Menu", "startup_on")
				End Select
				Text(715, 395, txt, True, True)
				Color 255,255,255
				
				If DrawButton(770, 380, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 5
				EndIf
				
				Text(665, 290, GetLocalString("Menu", "startup_videos") + ":")
				DrawFrame(655, 310, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\PlayStartupVideos
					Case 0
						txt = GetLocalString("Menu", "startup_off")
					Case 1
						txt = GetLocalString("Menu", "startup_on")
				End Select
				Text(715, 325, txt, True, True)
				
				If DrawButton(770, 310, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 4
				EndIf
				
				DrawFrame(655, 257, 145, 70, 0, 0, 1024, 1024, FRAME_THICK, 512)
				For i = 0 To 1
					Select i
						Case 0
							Text(655+(145/2),287+(20*i),GetLocalString("Menu", "2d"),True,True)
						Case 1
							Text(655+(145/2),287+(20*i),GetLocalString("Menu", "3d"),True,True)
					End Select
					If MouseOn(670,277+(20*i),113,20) Then
						Color 100,100,100
						Rect(670,277+(20*i),113,20,False)
						Color 255,255,255
						If MouseHit1 Then
							opt\Menu3DEnabled = i
							SelectedInputBox = 0
							PlaySound_Strict ButtonSFX[2]
							Exit
						EndIf
					EndIf
				Next
				
				Text(665, 150, GetLocalString("Language", "lang") + ":")
				DrawFrame(655, 170, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\LanguageVal
					Case 0
						txt = GetLocalString("Language", "eng")
					Case 1
						txt = GetLocalString("Language", "rus")
				End Select
				Text(715, 185, txt, True, True)
				
				If DrawButton(770, 170, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 2
				EndIf
				
				Text(665, 80, GetLocalString("Launcher", "mode") + ":")
				DrawFrame(655, 100, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\DisplayMode
					Case 0
						txt = GetLocalString("Launcher", "windowed")
					Case 1
						txt = GetLocalString("Launcher", "fullscreen")
				End Select
				Text(715, 115, txt, True, True)
				
				If DrawButton(770, 100, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = -1
				EndIf
				
				Text(665, 220, GetLocalString("Menu", "menu_mode") + ":")
				DrawFrame(655, 240, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\Menu3DEnabled
					Case 0
						txt = GetLocalString("Menu", "2d")
					Case 1
						txt = GetLocalString("Menu", "3d")
				End Select
				Text(715, 255, txt, True, True)
				
				If DrawButton(770, 240, 30, 30, "▲", False, False, False) Then
					SelectedInputBox = 0
				EndIf
				
			Case 4 ; ~ Startup videos selection
				
				If DrawButton(770, 30, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 4
				EndIf
				
				Text(665, 360, GetLocalString("Menu", "steam_enabled") + ":")
				DrawFrame(655, 380, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\SteamEnabled
					Case 0
						Color 255,0,0
						txt = GetLocalString("Menu", "startup_off")
					Case 1
						Color 0,255,0
						txt = GetLocalString("Menu", "startup_on")
				End Select
				Text(715, 395, txt, True, True)
				Color 255,255,255
				
				If DrawButton(770, 380, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 5
				EndIf
				
				DrawFrame(655, 327, 145, 70, 0, 0, 1024, 1024, FRAME_THICK, 512)
				For i = 0 To 1
					Select i
						Case 1
							Text(655+(145/2),357+(20*i),GetLocalString("Menu", "startup_on"),True,True)
						Case 0
							Text(655+(145/2),357+(20*i),GetLocalString("Menu", "startup_off"),True,True)
					End Select
					If MouseOn(670,347+(20*i),113,20) Then
						Color 100,100,100
						Rect(670,347+(20*i),113,20,False)
						Color 255,255,255
						If MouseHit1 Then
							opt\PlayStartupVideos= i
							SelectedInputBox = 0
							PlaySound_Strict ButtonSFX[2]
							Exit
						EndIf
					EndIf
				Next
				
				Text(665, 150, GetLocalString("Language", "lang") + ":")
				DrawFrame(655, 170, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\LanguageVal
					Case 0
						txt = GetLocalString("Language", "eng")
					Case 1
						txt = GetLocalString("Language", "rus")
				End Select
				Text(715, 185, txt, True, True)
				
				If DrawButton(770, 170, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 2
				EndIf
				
				Text(665, 80, GetLocalString("Launcher", "mode") + ":")
				DrawFrame(655, 100, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\DisplayMode
					Case 0
						txt = GetLocalString("Launcher", "windowed")
					Case 1
						txt = GetLocalString("Launcher", "fullscreen")
				End Select
				Text(715, 115, txt, True, True)
				
				If DrawButton(770, 100, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = -1
				EndIf
				
				Text(665, 220, GetLocalString("Menu", "menu_mode") + ":")
				DrawFrame(655, 240, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\Menu3DEnabled
					Case 0
						txt = GetLocalString("Menu", "2d")
					Case 1
						txt = GetLocalString("Menu", "3d")
				End Select
				Text(715, 255, txt, True, True)
				
				If DrawButton(770, 240, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 3
				EndIf
				
				Text(665, 290, GetLocalString("Menu", "startup_videos") + ":")
				DrawFrame(655, 310, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\PlayStartupVideos
					Case 0
						txt = GetLocalString("Menu", "startup_off")
					Case 1
						txt = GetLocalString("Menu", "startup_on")
				End Select
				Text(715, 325, txt, True, True)
				
				If DrawButton(770, 310, 30, 30, "▲", False, False, False) Then
					SelectedInputBox = 0
				EndIf
				
			Case 5 ; ~ Steam
				
				If DrawButton(770, 30, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 2
				EndIf
				
				DrawFrame(655, 397, 145, 70, 0, 0, 1024, 1024, FRAME_THICK, 512)
				For i = 0 To 1
					Select i
						Case 0
							Text(655+(145/2),427+(20*i),GetLocalString("Menu", "startup_off"),True,True)
						Case 1
							Text(655+(145/2),427+(20*i),GetLocalString("Menu", "startup_on"),True,True)
					End Select
					If MouseOn(670,417+(20*i),113,20) Then
						Color 100,100,100
						Rect(670,417+(20*i),113,20,False)
						Color 255,255,255
						If MouseHit1 Then
							opt\SteamEnabled = i
							SelectedInputBox = 0
							PlaySound_Strict ButtonSFX[2]
							Exit
						EndIf
					EndIf
				Next
				
				Text(665, 360, GetLocalString("Menu", "steam_enabled") + ":")
				DrawFrame(655, 380, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\SteamEnabled
					Case 0
						Color 255,0,0
						txt = GetLocalString("Menu", "startup_off")
					Case 1
						Color 0,255,0
						txt = GetLocalString("Menu", "startup_on")
				End Select
				Text(715, 395, txt, True, True)
				Color 255,255,255
				
				Text(665, 290, GetLocalString("Menu", "startup_videos") + ":")
				DrawFrame(655, 310, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\PlayStartupVideos
					Case 0
						txt = GetLocalString("Menu", "startup_off")
					Case 1
						txt = GetLocalString("Menu", "startup_on")
				End Select
				Text(715, 325, txt, True, True)
				
				If DrawButton(770, 310, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 4
				EndIf
				
				Text(665, 220, GetLocalString("Menu", "menu_mode") + ":")
				DrawFrame(655, 240, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\Menu3DEnabled
					Case 0
						txt = GetLocalString("Menu", "2d")
					Case 1
						txt = GetLocalString("Menu", "3d")
				End Select
				Text(715, 255, txt, True, True)
				
				If DrawButton(770, 240, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 3
				EndIf
				
				Text(665, 80, GetLocalString("Launcher", "mode") + ":")
				DrawFrame(655, 100, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\DisplayMode
					Case 0
						txt = GetLocalString("Launcher", "windowed")
					Case 1
						txt = GetLocalString("Launcher", "fullscreen")
				End Select
				Text(715, 115, txt, True, True)
				
				If DrawButton(770, 100, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = -1
				EndIf
				
				Text(665, 150, GetLocalString("Language", "lang") + ":")
				DrawFrame(655, 170, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\LanguageVal
					Case 0
						txt = GetLocalString("Language", "eng")
					Case 1
						txt = GetLocalString("Language", "rus")
				End Select
				Text(715, 185, txt, True, True)
				
				If DrawButton(770, 170, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 2
				EndIf
				
				If DrawButton(770, 380, 30, 30, "▲", False, False, False) Then
					SelectedInputBox = 0
				EndIf
				
			Default ; ~ Resolution tab shown
				
				Text(665, 360, GetLocalString("Menu", "steam_enabled") + ":")
				DrawFrame(655, 380, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\SteamEnabled
					Case 0
						Color 255,0,0
						txt = GetLocalString("Menu", "startup_off")
					Case 1
						Color 0,255,0
						txt = GetLocalString("Menu", "startup_on")
				End Select
				Text(715, 395, txt, True, True)
				Color 255,255,255
				
				If DrawButton(770, 380, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 5
				EndIf
				
				Text(665, 290, GetLocalString("Menu", "startup_videos") + ":")
				DrawFrame(655, 310, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\PlayStartupVideos
					Case 0
						txt = GetLocalString("Menu", "startup_off")
					Case 1
						txt = GetLocalString("Menu", "startup_on")
				End Select
				Text(715, 325, txt, True, True)
				
				If DrawButton(770, 310, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 4
				EndIf
				
				Text(665, 220, GetLocalString("Menu", "menu_mode") + ":")
				DrawFrame(655, 240, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\Menu3DEnabled
					Case 0
						txt = GetLocalString("Menu", "2d")
					Case 1
						txt = GetLocalString("Menu", "3d")
				End Select
				Text(715, 255, txt, True, True)
				
				If DrawButton(770, 240, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 3
				EndIf
				
				Text(665, 150, GetLocalString("Language", "lang") + ":")
				DrawFrame(655, 170, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\LanguageVal
					Case 0
						txt = GetLocalString("Language", "eng")
					Case 1
						txt = GetLocalString("Language", "rus")
				End Select
				Text(715, 185, txt, True, True)
				
				If DrawButton(770, 170, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = 2
				EndIf
				
				Text(665, 80, GetLocalString("Launcher", "mode") + ":")
				DrawFrame(655, 100, 120, 30, 0, 0, 1024, 1024, FRAME_THICK, 512)
				Select opt\DisplayMode
					Case 0
						txt = GetLocalString("Launcher", "windowed")
					Case 1
						txt = GetLocalString("Launcher", "fullscreen")
				End Select
				Text(715, 115, txt, True, True)
				
				If DrawButton(770, 100, 30, 30, "▼", False, False, False) Then
					SelectedInputBox = -1
				EndIf
				
				For i = 0 To Min(opt\TotalGFXModes, 9)
					y = y + 1
				Next
				DrawFrame(655, 57, 145, 20*y)
				frameheight = 20*y-4
				y = 0
				For i = (-1+SelectedInputBox) To (7+SelectedInputBox)
					If i < opt\TotalGFXModes Then
						If (opt\TotalGFXModes - 1) > 8 Then
							ScrollMenuHeight = opt\TotalGFXModes - 9
							SelectedInputBox = 1 + (ScrollBarY * (opt\TotalGFXModes - 9))
						EndIf
						y = y + 1
						If (57+(20*y)) < 57+frameheight Then
							isResolutionSelected = False
							For res = Each Resolution
								If res\index = i Then
									Text(1445 / 2, 57 + (20 * y), res\width + "x" + res\height, True, True)
									If MouseOn(665,47+(20*y),113,20) Then
										Color 100,100,100
										Rect(665,47+(20*y),113,20,False)
										Color 255,255,255
										If MouseHit1 Then
											currentWidth = res\width
											currentHeight = res\height
											SelectedInputBox = 0
											PlaySound_Strict ButtonSFX[2]
											isResolutionSelected = True
										EndIf
									EndIf
									Exit
								EndIf
							Next
							If isResolutionSelected Then
								Exit
							EndIf
						EndIf
					Else
						Exit
					EndIf
				Next
				
				ScrollBarY = DrawScrollBar(777,59,20,frameheight,777,59+(frameheight-Max(frameheight-8*ScrollMenuHeight,50))*ScrollBarY,20,Max(frameheight-(8*ScrollMenuHeight),50),ScrollBarY,1)
				
				If DrawButton(770, 30, 30, 30, "▲", False, False, False) Then
					SelectedInputBox = 0
				EndIf
		End Select
		
		; ~ Media buttons
		If MouseOn(LauncherWidth - 760, LauncherHeight - 336, 64, 64)
			Rect(LauncherWidth - 761, LauncherHeight - 337, 66, 66, False)
			Text(LauncherWidth - 760 + LauncherMediaWidth, LauncherHeight - 266, "DISCORD", True)
			If MouseHit1 Then
				PlaySound_Strict(ButtonSFX[2])
				ExecFile("https://discord.gg/pdMfdtvnkx")
			EndIf
		EndIf
		DrawBlock(LauncherMediaIMG, LauncherWidth - 760, LauncherHeight - 336, 0)
		
		If MouseOn(LauncherWidth - 760, LauncherHeight - 246, 64, 64)
			Rect(LauncherWidth - 761, LauncherHeight - 247, 66, 66, False)
			Text(LauncherWidth - 760 + LauncherMediaWidth, LauncherHeight - 176, "MODDB", True)
			If MouseHit1 Then
				PlaySound_Strict(ButtonSFX[2])
				ExecFile("https://www.moddb.com/mods/scp-security-stories")
			EndIf
		EndIf
		DrawBlock(LauncherMediaIMG, LauncherWidth - 760, LauncherHeight - 246, 1)
		
		If MouseOn(LauncherWidth - 760, LauncherHeight - 156, 64, 64)
			Rect(LauncherWidth - 761, LauncherHeight - 157, 66, 66, False)
			Text(LauncherWidth - 760 + LauncherMediaWidth, LauncherHeight - 86, "YOUTUBE", True)
			If MouseHit1 Then
				PlaySound_Strict(ButtonSFX[2])
				ExecFile("https://www.youtube.com/channel/UCxH8eXDxIv-QrEYECxcgmKA")
			EndIf
		EndIf
		DrawBlock(LauncherMediaIMG, LauncherWidth - 760, LauncherHeight - 156, 2)
		
		If DrawButton(5, 480 - 45, 145, 40, Upper(GetLocalString("Launcher", "launch")), False, False, False) Then
			opt\GraphicWidth = currentWidth
			opt\GraphicHeight = currentHeight
			RealGraphicWidth = opt\GraphicWidth
			RealGraphicHeight = opt\GraphicHeight
			Language$ = opt\LanguageVal
			CheckForDlls()
			Exit
		EndIf
		Flip
	Forever
	
	InitLanguage()
	UpdateLang(Language$)
	
	PutINIValue(gv\OptionFile, "options", "width", opt\GraphicWidth)
	PutINIValue(gv\OptionFile, "options", "height", opt\GraphicHeight)
	PutINIValue(gv\OptionFile, "options", "display mode", opt\DisplayMode)
	PutINIValue(gv\OptionFile, "options", "language", opt\LanguageVal)
	PutINIValue(gv\OptionFile, "options", "enabled 3d menu", opt\Menu3DEnabled)
	PutINIValue(gv\OptionFile, "options", "play startup videos", opt\PlayStartupVideos)
	PutINIValue(gv\OptionFile, "options", "steam enabled", opt\SteamEnabled)
	
	DeleteMenuImages()
	
	FreeImage UpdaterIMG
	FreeImage LauncherIMG
	
	SelectedInputBox = 0
	ScrollBarY = 0
	ScrollBarY2 = 0
	ScrollMenuHeight = 0
	ScrollMenuHeight2 = 0
	
End Function

Function IsResolutionWidthValid%(width%)
	
	If width >= 800 And width <= DesktopWidth() And width <= 4096 Then
		Return True
	EndIf
	Return False
	
End Function

Function IsResolutionHeightValid%(height%)
	
	If height >= 600 And height <= DesktopHeight() And height <= 4096 Then
		Return True
	EndIf
	Return False
	
End Function

Function DrawTiledImageRect(img%, srcX%, srcY%, srcwidth#, srcheight#, x%, y%, width%, height%)
	Local srcwidth_orig = srcwidth
	Local srcheight_orig = srcheight
	
	Local x2% = x
	While x2 < x+width
		Local y2% = y
		While y2 < y+height
			If x2 + srcwidth > x + width Then srcwidth = srcwidth - Max((x2 + srcwidth) - (x + width), 1)
			If y2 + srcheight > y + height Then srcheight = srcheight - Max((y2 + srcheight) - (y + height), 1)
			DrawBlockRect(img, x2, y2, srcX, srcY, srcwidth, srcheight)
			y2 = y2 + srcheight_orig
		Wend
		x2 = x2 + srcwidth_orig
	Wend
	
End Function

Global CursorPos% = -1

Function rInput$(aString$, MaxChr%)
	Local value% = GetKey()
	Local length% = Len(aString$)
	
	If CursorPos = -1 Then CursorPos = length
	
	If KeyDown(29) Then
		If value = 30 Then CursorPos = length
		If value = 31 Then CursorPos = 0
		If value = 127 Then aString = "" : CursorPos = 0
		If value = 22 Then
			aString = Left(aString, CursorPos) + GetClipboardContents() + Right(aString, Max(length - CursorPos,0))
			CursorPos = CursorPos + Len(aString) - length
			If MaxChr > 0 And MaxChr < Len(aString) Then aString = Left(aString, MaxChr) : CursorPos = MaxChr
		EndIf
		Return TextInput(aString)
	EndIf
	
	If value = 8 Then
		If CursorPos > 0 Then
			aString = TextInput(Left(aString, CursorPos)) + Right(aString, Max(length - CursorPos,0))
			CursorPos = CursorPos - 1
		EndIf
	ElseIf value = 30
		CursorPos = Min(CursorPos + 1, length)
	ElseIf value = 31
		CursorPos = Max(CursorPos - 1, 0)
	ElseIf value <> 127 And value >= 32 And value <= 254
		aString = TextInput(Left(aString, CursorPos)) + Right(aString, Max(length - CursorPos,0))
		CursorPos = CursorPos + Len(aString) - length
		If MaxChr > 0 And MaxChr < Len(aString) Then 
			aString = Left(aString, MaxChr)
			CursorPos = MaxChr
		EndIf	
	EndIf
	
	Return aString
End Function

Function InputBox$(x%, y%, width%, height%, Txt$, ID% = 0, MaxChr% = 0, currButton%=-1, currButtonTab%=0, currButtonSub%=0, drawAsPassword%=False, font%=Font_Default)
	Local currInputBox.MenuInputBox
	Local mib.MenuInputBox
	Local buttonexists%=False
	For mib = Each MenuInputBox
		If mib\x=x And mib\y=y And mib\width=width And mib\height=height
			buttonexists=True
			Exit
		EndIf
	Next
	If (Not buttonexists)
		mib = New MenuInputBox
		mib\x = x
		mib\y = y
		mib\width = width
		mib\height = height
		mib\txt = Txt
		mib\id = ID
		mib\currButton = currButton
		mib\currButtonTab = currButtonTab
		mib\currButtonSub = currButtonSub
		mib\drawAsPassword = drawAsPassword
		mib\font = font
	Else
		currInputBox = mib
		currInputBox\txt = Txt
		currInputBox\drawAsPassword = drawAsPassword
	EndIf
	
	Local MouseOnBox% = False
	If (Not co\Enabled)
		If MouseOn(x, y, width, height) Then
			MouseOnBox = True
			If MouseHit1 And SelectedInputBox <> ID Then SelectedInputBox = ID : FlushKeys : CursorPos = -1
		EndIf
	Else
		If co\CurrButton[currButtonTab] = currButton
			If co\CurrButtonSub[currButtonTab] = currButtonSub
				MouseOnBox = True
				If co\PressedButton And SelectedInputBox <> ID Then SelectedInputBox = ID : FlushKeys() : FlushJoy() : CursorPos = -1
			EndIf
		EndIf
	EndIf
	
	If (Not co\Enabled)
		If (Not MouseOnBox) And MouseHit1 And SelectedInputBox = ID Then SelectedInputBox = 0 : CursorPos = -1
	Else
		If (Not MouseOnBox) And co\PressedButton And SelectedInputBox = ID Then SelectedInputBox = 0 : CursorPos = -1
	EndIf
	
	If SelectedInputBox = ID Then
		Txt = rInput(Txt, MaxChr)
	EndIf
	
	Return Txt
End Function

Function InputBoxConsole$(x%, y%, width%, height%, Txt$, ID%, MaxChr%, font%=Font_Default)
	Color (255, 255, 255)
	;DrawTiledImageRect(MenuWhite, (x Mod 256), (y Mod 256), 512, 512, x, y, width, height)
	Rect(x, y, width, height)
	
	Color (0, 0, 0)
	
	Local MouseOnBox% = False
	If MouseOn(x, y, width, height) Then
		Color(50, 50, 50)
		MouseOnBox = True
		If MouseHit1 And SelectedInputBox <> ID Then SelectedInputBox = ID : FlushKeys : CursorPos = -1
	EndIf
	
	Rect(x + 2, y + 2, width - 4, height - 4)
	Color (255, 255, 255)	
	
	If (Not MouseOnBox) And MouseHit1 And SelectedInputBox = ID Then SelectedInputBox = 0 : CursorPos = -1
	
	If SelectedInputBox = ID Then
		Txt = rInput(Txt, MaxChr)
		If (MilliSecs() Mod 800) < 400 Then Rect (x + width / 2 - (StringWidth(Txt)) / 2 + StringWidth(Left(Txt, CursorPos)), y + height / 2 - 5, 2, 12)
	EndIf	
	SetFont fo\Font[font]
	Text(x + width / 2, y + height / 2, Txt, True, True)
	
	Return Txt
End Function

Const FRAME_THICK = 3

Function DrawFrame(x%, y%, width%, height%, xoffset%=0, yoffset%=0, srcwidth% = 1024, srcheight% = 1024, frameoffset%=FRAME_THICK, tile%=256, Locked% = False, colorout=255,inR=10,inG=10,inB=10,frameR=50,frameG=50,frameB=50)
	
	If Locked Then
		Color 250, 0, 0
	Else
		Color frameR, frameG, frameB
	EndIf
	Rect(x, y, width, height)
	If Locked Then
		Color 50, 0, 0
	Else
		Color inR, inG, inB
	EndIf
	Rect(x+frameoffset*MenuScale, y+frameoffset*MenuScale, width-(frameoffset*2)*MenuScale, height-(frameoffset*2)*MenuScale)
	Color colorout, colorout, colorout
	
End Function

Function DrawButton%(x%, y%, width%, height%, txt$, bigfont% = True, waitForMouseUp%=False, usingAA%=True, currButton%=-1, currButtonTab%=0, currButtonSub%=0, locked% = False)
	Local clicked% = False
	
	If usingAA
		Local currMButton.MenuButton
		Local mb.MenuButton
		Local buttonexists%=False
		For mb = Each MenuButton
			If mb\x=x And mb\y=y And mb\width=width And mb\height=height
				buttonexists=True
				Exit
			EndIf
		Next
		If (Not buttonexists)
			mb = New MenuButton
			mb\x = x
			mb\y = y
			mb\width = width
			mb\height = height
			mb\txt = txt
			mb\bigfont = bigfont
			mb\currButton = currButton
			mb\currButtonTab = currButtonTab
			mb\currButtonSub = currButtonSub
			mb\locked = locked
		Else
			currMButton = mb
			currMButton\txt = txt
		EndIf
	EndIf
	
	If (Not co\Enabled)
		If MouseOn(x, y, width, height) Then
			If (Not locked) Then
				If (MouseHit1 And (Not waitForMouseUp)) Lor (MouseUp1 And waitForMouseUp) Then 
					clicked = True
					PlaySound_Strict(ButtonSFX[2])
					If waitForMouseUp Then
						MouseUp1 = False
					Else
						MouseHit1 = False
					EndIf
				EndIf
			Else
				If (MouseHit1 And (Not waitForMouseUp)) Lor (MouseUp1 And waitForMouseUp) Then 
					PlaySound_Strict(ButtonSFX[1])
					If waitForMouseUp Then
						MouseUp1 = False
					Else
						MouseHit1 = False
					EndIf
					clicked = False
				EndIf
			EndIf
		EndIf
	Else
		If co\CurrButton[currButtonTab] = currButton
			If co\CurrButtonSub[currButtonTab] = currButtonSub
				If (co\PressedButton)
					PlaySound_Strict(ButtonSFX[2])
					clicked = True
					FlushJoy()
				EndIf
			EndIf
		EndIf
	EndIf
	
	;Button for launcher
	If (Not usingAA)
		DrawFrame(x,y,width,height)
		
		If (Not co\Enabled)
			If MouseOn(x, y, width, height)
				Color(30, 30, 30)
				Rect(x + 4, y + 4, width - 8, height - 8)	
			Else
				Color(0, 0, 0)
			EndIf
		Else
			If co\CurrButton[currButtonTab] = currButton
				If co\CurrButtonSub[currButtonTab] = currButtonSub
					Color(30, 30, 30)
					Rect(x + 4, y + 4, width - 8, height - 8)
				EndIf
			Else
				Color(0, 0, 0)
			EndIf
		EndIf
		Color (255, 255, 255)
		If bigfont Then 
			SetFont fo\Font[Font_Menu]
		Else 
			SetFont fo\Font[Font_Default]
		EndIf
		Text(x + width / 2, y + height / 2, txt, True, True)
	EndIf
	
	Return clicked
End Function

Function DrawTick%(x%, y%, selected%, locked% = False, currButton%=-1, currButtonTab%=0, currButtonSub%=0)
	Local width% = 20 * MenuScale, height% = 20 * MenuScale
	
	Local currTick.MenuTick
	Local mt.MenuTick
	Local buttonexists%=False
	For mt = Each MenuTick
		If mt\x=x And mt\y=y
			buttonexists=True
			Exit
		EndIf
	Next
	If (Not buttonexists)
		mt = New MenuTick
		mt\x = x
		mt\y = y
		mt\selected = selected
		mt\locked = locked
		mt\currButton = currButton
		mt\currButtonTab = currButtonTab
		mt\currButtonSub = currButtonSub
	Else
		currTick = mt
		mt\selected = selected
		mt\locked = locked
	EndIf
	
	If (Not co\Enabled)
		Local Highlight% = MouseOn(x, y, width, height) And (Not locked)
	Else
		If co\CurrButton[currButtonTab]=currButton And co\CurrButtonSub[currButtonTab]=currButtonSub
			Highlight = True
		Else
			Highlight = False
		EndIf
	EndIf
	
	If Highlight Then
		If (Not locked) Then
			If (Not co\Enabled)
				If MouseHit1 Then selected = (Not selected) : PlaySound_Strict (ButtonSFX[2])
			Else
				If co\PressedButton Then selected = (Not selected) : PlaySound_Strict(ButtonSFX[2])
			EndIf
		Else
			If (Not co\Enabled)
				If MouseHit1 Then selected = (Not selected) : PlaySound_Strict (ButtonSFX[1])
			Else
				If co\PressedButton Then selected = (Not selected) : PlaySound_Strict(ButtonSFX[1])
			EndIf
		EndIf
	EndIf
	
	If locked Then
		Color 250, 0, 0
	Else
		Color 50, 50, 50
	EndIf
	
	Return selected
End Function

Function DrawDropdown%(x%, y%, value%, id%, txt1$, txt2$, txt3$, txt4$="", txt5$="")
	Local currDrop.MenuDropdown
	Local md.MenuDropdown
	Local buttonexists%=False
	Local i%
	
	For md = Each MenuDropdown
		If md\x=x And md\y=y
			buttonexists=True
			Exit
		EndIf
	Next
	If (Not buttonexists)
		md = New MenuDropdown
		md\x = x
		md\y = y
		md\value = value
		md\ID = id
		md\txt1 = txt1
		md\txt2 = txt2
		md\txt3 = txt3
		md\txt4 = txt4
		md\txt5 = txt5
		If txt5 = "" Then
			If txt4 = "" Then
				md\size = 3
			Else
				md\size = 4
			EndIf
		Else
			md\size = 5
		EndIf
	Else
		currDrop = md
		currDrop\value = value
	EndIf
	If SelectedInputBox = md\ID
		If DrawButton(md\x + (120 - FRAME_THICK) * MenuScale, md\y, 30 * MenuScale, 30 * MenuScale, "▲", False) Then
			SelectedInputBox = 0
		EndIf
		For i = 0 To md\size-1
			If MouseOn(md\x + 5.5 * MenuScale, md\y + (20 - FRAME_THICK + 20 + 20 * i) * MenuScale, 137 * MenuScale, 20 * MenuScale) Then
				If MouseHit1 Then
					value = i
					MouseHit1 = False
					SelectedInputBox = 0
					PlaySound_Strict(ButtonSFX[2])
				EndIf
			EndIf	
		Next
	Else
		If DrawButton(md\x + (120 - FRAME_THICK) * MenuScale, md\y, 30 * MenuScale, 30 * MenuScale, "▼", False) Then
			SelectedInputBox = md\ID
		EndIf
	EndIf
	
	Return value
End Function

Function SlideBar#(x%, y%, width%, value#, currButton%=-1, currButtonTab%=0, currButtonSub%=0, txtlow$="LOW", txthigh$="HIGH")
	Local currSlideBar.MenuSlideBar
	Local msb.MenuSlideBar
	Local buttonexists%=False
	For msb = Each MenuSlideBar
		If msb\x=x And msb\y=y And msb\width=width
			buttonexists=True
			Exit
		EndIf
	Next
	If (Not buttonexists)
		msb = New MenuSlideBar
		msb\x = x
		msb\y = y
		msb\width = width
		msb\value = value
		msb\currButton = currButton
		msb\currButtonTab = currButtonTab
		msb\currButtonSub = currButtonSub
		msb\txtlow = txtlow
		msb\txthigh = txthigh
	Else
		currSlideBar = msb
		currSlideBar\value = value
	EndIf
	
	If (Not co\Enabled)
		If MouseDown1 And OnSliderID=0 Then
			If ScaledMouseX() >= x And ScaledMouseX() <= x + width + 14 And ScaledMouseY() >= y And ScaledMouseY() <= y + 20 Then
				value = Min(Max((ScaledMouseX() - x) * 100 / width, 0), 100)
			EndIf
		EndIf
	Else
		If co\CurrButton[currButtonTab]=currButton
			If co\CurrButtonSub[currButtonTab]=currButtonSub
				value = UpdateControllerSideSelection(value,0,100)
			EndIf
		EndIf
	EndIf
	
	Return value
	
End Function


Function RowText(A$, X, Y, W, H, align% = 0, Leading#=1)
	;Display A$ starting at X,Y - no wider than W And no taller than H (all in pixels).
	;Leading is optional extra vertical spacing in pixels
	
	If H<1 Then H=2048
	
	Local LinesShown = 0
	Local Height = StringHeight(A$) + Leading
	Local b$
	
	While Len(A) > 0
		Local space = Instr(A$, " ")
		If space = 0 Then space = Len(A$)
		Local temp$ = Left(A$, space)
		Local trimmed$ = Trim(temp) ;we might ignore a final space 
		Local extra = 0 ;we haven't ignored it yet
		;ignore final space If doing so would make a word fit at End of Line:
		If (StringWidth (b$ + temp$) > W) And (StringWidth (b$ + trimmed$) <= W) Then
			temp = trimmed
			extra = 1
		EndIf
		
		If StringWidth (b$ + temp$) > W Then ;too big, so Print what will fit
			If align Then
				Text(X + W / 2 - (StringWidth(b) / 2), LinesShown * Height + Y, b)
			Else
				Text(X, LinesShown * Height + Y, b)
			EndIf			
			
			LinesShown = LinesShown + 1
			b$=""
		Else ;append it To b$ (which will eventually be printed) And remove it from A$
			b$ = b$ + temp$
			A$ = Right(A$, Len(A$) - (Len(temp$) + extra))
		EndIf
		
		If ((LinesShown + 1) * Height) > H Then Exit ;the Next Line would be too tall, so leave
	Wend
	
	If (b$ <> "") And((LinesShown + 1) <= H) Then
		If align Then
			Text(X + W / 2 - (StringWidth(b) / 2), LinesShown * Height + Y, b) ;Print any remaining Text If it'll fit vertically
		Else
			Text(X, LinesShown * Height + Y, b) ;Print any remaining Text If it'll fit vertically
		EndIf
	EndIf
	
End Function

Function GetLineAmount(A$, W, H, Leading#=1)
	;Display A$ starting at X,Y - no wider than W And no taller than H (all in pixels).
	;Leading is optional extra vertical spacing in pixels
	
	If H<1 Then H=2048
	
	Local LinesShown = 0
	Local Height = StringHeight(A$) + Leading
	Local b$
	
	While Len(A) > 0
		Local space = Instr(A$, " ")
		If space = 0 Then space = Len(A$)
		Local temp$ = Left(A$, space)
		Local trimmed$ = Trim(temp) ;we might ignore a final space 
		Local extra = 0 ;we haven't ignored it yet
		;ignore final space If doing so would make a word fit at End of Line:
		If (StringWidth (b$ + temp$) > W) And (StringWidth (b$ + trimmed$) <= W) Then
			temp = trimmed
			extra = 1
		EndIf
		
		If StringWidth (b$ + temp$) > W Then ;too big, so Print what will fit
			
			LinesShown = LinesShown + 1
			b$=""
		Else ;append it To b$ (which will eventually be printed) And remove it from A$
			b$ = b$ + temp$
			A$ = Right(A$, Len(A$) - (Len(temp$) + extra))
		EndIf
		
		If ((LinesShown + 1) * Height) > H Then Exit ;the Next Line would be too tall, so leave
	Wend
	
	Return LinesShown+1
	
End Function

Function LimitText$(txt$,width%)
	Local TextLength%, UnFitting%, LetterWidth%
	If txt = "" Lor width = 0 Then Return ""
	TextLength = StringWidth(txt)
	UnFitting = TextLength - width*FontWidth()
	If UnFitting <= 0 Then
		Return txt
	Else
		LetterWidth = TextLength / Len(txt)
		Return (Left(txt, Max(Len(txt) - UnFitting / (LetterWidth - 4), 1)) + "...")
	EndIf
End Function

Function DrawTooltip(message$)
	Local scale# = opt\GraphicHeight/768.0
	
	Local width = (StringWidth(message$))+20*MenuScale
	
	Color 25,25,25
	Rect(ScaledMouseX()+20,ScaledMouseY(),width,19*scale,True)
	Color 150,150,150
	Rect(ScaledMouseX()+20,ScaledMouseY(),width,19*scale,False)
	SetFont fo\Font[Font_Default]
	Text(ScaledMouseX()+(20*MenuScale)+(width/2),ScaledMouseY()+(12*MenuScale), message$, True, True)
End Function

Global QuickLoadPercent% = -1
Global QuickLoadPercent_DisplayTimer# = 0
Global QuickLoad_CurrEvent.Events

Function DrawQuickLoading()
	
	If QuickLoadPercent > -1
		MidHandle QuickLoadIcon
		;DrawImage QuickLoadIcon,opt\GraphicWidth-90,opt\GraphicHeight-150
		DrawImage QuickLoadIcon,opt\GraphicWidth-90,opt\GraphicHeight-190
		Color 255,255,255
		SetFont fo\Font[Font_Default]
		;Text opt\GraphicWidth-100,opt\GraphicHeight-90,"LOADING: "+QuickLoadPercent+"%",1
		Text opt\GraphicWidth-100,opt\GraphicHeight-130,Upper(GetLocalString("Menu","loading"))+": "+QuickLoadPercent+"%",1
		If QuickLoadPercent > 99
			If QuickLoadPercent_DisplayTimer < 70
				QuickLoadPercent_DisplayTimer# = Min(QuickLoadPercent_DisplayTimer+FPSfactor,70)
			Else
				QuickLoadPercent = -1
			EndIf
		EndIf
		QuickLoadEvents()
	Else
		QuickLoadPercent = -1
		QuickLoadPercent_DisplayTimer# = 0
		QuickLoad_CurrEvent = Null
	EndIf
	
End Function

Function DrawOptionsTooltip(option$,value#=0,ingame%=False)
	; TODO make these fucking constant
	Local x = (60 + 580) * MenuScale
	Local fx# = x + 6 * MenuScale
	Local y = (286 + 70 + 20 + 70) * MenuScale
	Local fy# = y+6*MenuScale
	Local width = 400 * MenuScale
	Local fw# = width - 12 * MenuScale
	Local fh# = (150-12) * MenuScale
	Local lines% = 0, lines2% = 0
	Local txt$ = ""
	Local txt2$ = "", R% = 0, G% = 0, B% = 0
	Local usetestimg% = False, extraspace% = 0
	
	SetFont fo\Font[Font_Default]
	Color 255,255,255
	Select Lower(option$)
		;Graphic options
			;[Block]
		Case "bump"
			txt = GetLocalString("Options", "bumptxt")
			txt2 = GetLocalString("Options", "cantingame")
			R = 255
		Case "vsync"
			txt = GetLocalString("Options", "vsynctxt")
		Case "roomlights"
			txt = GetLocalString("Options", "roomlightstxt")
		Case "gamma"
			txt = GetLocalString("Options", "gammatxt")
			R = 255
			G = 255
			B = 255
			txt2 = GetLocalStringR("Options", "currentval", Int(value*100))+"% "+GetLocalStringR("Options", "defaultval",Int(100))
		Case "framelimit"
			txt = GetLocalString("Options", "framelimittxt")
			If value > 0 Then
				R = 255
				G = 255
				B = 255
				txt2 = GetLocalStringR("Options", "currentval", Int(value))+" "+GetLocalString("Options", "fps")
			EndIf
		Case "texquality"
			txt = GetLocalString("Options", "texqualitytxt")
		Case "texfiltering"
			txt = GetLocalString("Options", "texfilteringtxt")
		Case "particleamount"
			txt = GetLocalString("Options", "particleamounttxt")
			Select value
				Case 0
					R = 255
					txt2 = GetLocalString("Options", "particleamounttxt_low")
				Case 1
					R = 255
					G = 255
					txt2 = GetLocalString("Options", "particleamounttxt_med")
				Case 2
					G = 255
					txt2 = GetLocalString("Options", "particleamounttxt_high")
			End Select
		Case "vram"
			txt = GetLocalString("Options", "vramtxt") 
			txt2 = GetLocalString("Options", "cantingame")
			R = 255
		Case "fov"
			txt = GetLocalString("Options", "fovtxt")
			R = 255
			G = 255
			B = 255
			txt2 = GetLocalStringR("Options", "currentval", Int(value))+" "+GetLocalString("Options", "fov2")
		Case "cubemap"
			txt = GetLocalString("Options", "cubemaptxt")
			;[End Block]
		;Sound options
			;[Block]
		Case "musicvol"
			txt = GetLocalString("Options", "musicvoltxt")
			R = 255
			G = 255
			B = 255
			txt2 = GetLocalStringR("Options", "currentval", Int(value*100))+"% "+GetLocalStringR("Options", "defaultval",Int(50))
		Case "soundvol"
			txt = GetLocalString("Options", "soundvoltxt")
			R = 255
			G = 255
			B = 255
			txt2 = GetLocalStringR("Options", "currentval", Int(value*100))+"% "+GetLocalStringR("Options", "defaultval",Int(100))
		Case "sfxautorelease"
			txt = GetLocalString("Options", "sfxautoreleasetxt")
		Case "mastervol"
			txt = GetLocalString("Options", "mastervoltxt")
			R = 255
			G = 255
			B = 255
			txt2 = GetLocalStringR("Options", "currentval", Int(value*100))+"% "+GetLocalStringR("Options", "defaultval",Int(50))
		Case "voicevol"
			txt = GetLocalString("Options", "voicevoltxt")
			R = 255
			G = 255
			B = 255
			txt2 = GetLocalStringR("Options", "currentval", Int(value*100))+"% "+GetLocalStringR("Options", "defaultval",Int(100))
		Case "subtitles"
			txt = GetLocalString("Options", "subtitlestxt")
			;[End Block]
		;Control options	
			;[Block]
		Case "mousesensitivity"
			txt = GetLocalString("Options", "sensitivitytxt")
			R = 255
			G = 255
			B = 255
			txt2 = GetLocalStringR("Options", "currentval", Int((0.5+value)*100))+"% "+GetLocalStringR("Options", "defaultval",Int(50))
		Case "mouseinvert"
			txt = GetLocalString("Options", "inverttxt")
		Case "mousesmoothing"
			txt = GetLocalString("Options", "smoothingtxt")
			R = 255
			G = 255
			B = 255
			txt2 = GetLocalStringR("Options", "currentval", Int(value*100))+"% "+GetLocalStringR("Options", "defaultval",Int(100))
		Case "controls"
			txt = GetLocalString("Options", "controlstxt")
;		Case "controller"
;			txt = "Enables/Disables controller support."
;			If value=1
;				txt2 = "No controller found. Please plug in a controller or test if your current controller is supported by Windows. "
;				txt2 = txt2 + "You may need to restart the game in order for it to initialize the controller."
;				R = 255
;			EndIf
;		Case "controllersettings"
;			txt = "Configures the controller control scheme."
		Case "holdtoaim"
			txt = GetLocalString("Options", "holdtoaimtxt")
		Case "holdtocrouch"
			txt = GetLocalString("Options", "holdtocrouchtxt")
			;[End Block]
		;Advanced options	
			;[Block]
		Case "hud"
			txt = GetLocalString("Options", "hudtxt")
		Case "consoleenable"
			txt = GetLocalStringR("Options", "consoleenabletxt", KeyName[KEY_CONSOLE])
		Case "showfps"
			txt = GetLocalString("Options", "showfpstxt")
			;[End Block]
		;Singleplayer options
			;[Block]
		Case "classic_mode_txt"
			txt = GetLocalString("Menu", "classic_mode_txt")
			;[End Block]
		;Multiplayer options
			;[Block]
		Case "private"
			txt = GetLocalString("Options", "privatetxt")
		Case "hardcore"
			txt = GetLocalString("Options", "hardcoretxt")
		Case "otherteam"
			txt = GetLocalString("Options", "otherteamtxt")
			;[End Block]
	End Select
	
	lines% = GetLineAmount(txt,fw,fh)
	If usetestimg
		extraspace = 210*MenuScale
	EndIf
	If txt2$ = ""
		DrawFrame(x,y,width,((StringHeight(txt)*lines)+(10+lines)*MenuScale)+extraspace)
	Else
		lines2% = GetLineAmount(txt2,fw,fh)
		DrawFrame(x,y,width,(((StringHeight(txt)*lines)+(10+lines)*MenuScale)+(StringHeight(txt2)*lines2)+(10+lines2)*MenuScale)+extraspace)
	EndIf
	RowText(txt,fx,fy,fw,fh)
	If txt2$ <> ""
		Color R,G,B
		RowText(txt2,fx,(fy+(StringHeight(txt)*lines)+(5+lines)*MenuScale),fw,fh)
	EndIf
;	If usetestimg
;		MidHandle Menu_TestIMG
;		If txt2$ = ""
;			DrawImage Menu_TestIMG,x+(width/2),y+100*MenuScale+((StringHeight(txt)*lines)+(10+lines)*MenuScale)
;		Else
;			DrawImage Menu_TestIMG,x+(width/2),y+100*MenuScale+(((StringHeight(txt)*lines)+(10+lines)*MenuScale)+(StringHeight(txt2)*lines2)+(10+lines2)*MenuScale)
;		EndIf
;	EndIf
	
End Function

Global OnSliderID% = 0

Function Slider3(x%,y%,width%,value%,ID%,val1$,val2$,val3$,currButton%=-1,currButtonTab%=0,currButtonSub%=0)
	Local currSlider.MenuSlider
	Local ms.MenuSlider
	Local buttonexists%=False
	For ms = Each MenuSlider
		If ms\x=x And ms\y=y And ms\width=width And ms\amount=3
			buttonexists=True
			Exit
		EndIf
	Next
	If (Not buttonexists)
		ms = New MenuSlider
		ms\x = x
		ms\y = y
		ms\width = width
		ms\ID = ID
		ms\value = value
		ms\val1 = val1
		ms\val2 = val2
		ms\val3 = val3
		ms\currButton = currButton
		ms\currButtonTab = currButtonTab
		ms\currButtonSub = currButtonSub
		ms\amount = 3
	Else
		currSlider = ms
		currSlider\value = value
	EndIf
	
	If (Not co\Enabled)
		If MouseDown1 Then
			If (ScaledMouseX() >= x) And (ScaledMouseX() <= x+width+14) And (ScaledMouseY() >= y-8) And (ScaledMouseY() <= y+10)
				OnSliderID = ID
			EndIf
		EndIf
	Else
		If co\CurrButton[currButtonTab]=currButton
			If co\CurrButtonSub[currButtonTab]=currButtonSub
				OnSliderID = ID
			EndIf
		EndIf
	EndIf
	
	If (Not co\Enabled)
		If ID = OnSliderID
			If (ScaledMouseX() <= x+8)
				value = 0
			ElseIf (ScaledMouseX() >= x+width/2) And (ScaledMouseX() <= x+(width/2)+8)
				value = 1
			ElseIf (ScaledMouseX() >= x+width)
				value = 2
			EndIf
		EndIf
	Else
		If ID = OnSliderID
			value = UpdateControllerSideSelection(value,0,2,1)
		EndIf
	EndIf
	
	Return value
	
End Function

Function Slider5(x%,y%,width%,value%,ID%,val1$,val2$,val3$,val4$,val5$,currButton%=-1,currButtonTab%=0,currButtonSub%=0)
	Local currSlider.MenuSlider
	Local ms.MenuSlider
	Local buttonexists%=False
	For ms = Each MenuSlider
		If ms\x=x And ms\y=y And ms\width=width And ms\amount=5
			buttonexists=True
			Exit
		EndIf
	Next
	If (Not buttonexists)
		ms = New MenuSlider
		ms\x = x
		ms\y = y
		ms\width = width
		ms\ID = ID
		ms\value = value
		ms\val1 = val1
		ms\val2 = val2
		ms\val3 = val3
		ms\val4 = val4
		ms\val5 = val5
		ms\currButton = currButton
		ms\currButtonTab = currButtonTab
		ms\currButtonSub = currButtonSub
		ms\amount = 5
	Else
		currSlider = ms
		currSlider\value = value
	EndIf
	
	If (Not co\Enabled)
		If MouseDown1 Then
			If (ScaledMouseX() >= x) And (ScaledMouseX() <= x+width+14) And (ScaledMouseY() >= y-8) And (ScaledMouseY() <= y+10)
				OnSliderID = ID
			EndIf
		EndIf
	Else
		If co\CurrButton[currButtonTab]=currButton
			If co\CurrButtonSub[currButtonTab]=currButtonSub
				OnSliderID = ID
			EndIf
		EndIf
	EndIf
	
	If (Not co\Enabled)
		If ID = OnSliderID
			If (ScaledMouseX() <= x+8)
				value = 0
			ElseIf (ScaledMouseX() >= x+width/4) And (ScaledMouseX() <= x+(width/4)+8)
				value = 1
			ElseIf (ScaledMouseX() >= x+width/2) And (ScaledMouseX() <= x+(width/2)+8)
				value = 2
			ElseIf (ScaledMouseX() >= x+width*0.75) And (ScaledMouseX() <= x+(width*0.75)+8)
				value = 3
			ElseIf (ScaledMouseX() >= x+width)
				value = 4
			EndIf
		EndIf
	Else
		If ID = OnSliderID
			value = UpdateControllerSideSelection(value,0,4,1)
		EndIf
	EndIf
	
	Return value
	
End Function

Global OnBar%
Global ScrollBarY# = 0.0
Global ScrollMenuHeight# = 0.0
Global ScrollBarY2# = 0.0
Global ScrollMenuHeight2# = 0.0

Function DrawScrollBar#(x, y, width, height, barx, bary, barwidth, barheight, bar#, dir = 0, locked = False, speed = 2)
	
	If (Not co\Enabled)
		
		Color(0, 0, 0)
		Button(barx, bary, barwidth, barheight, "", locked)
		
		If dir = 0 Then
			If height > 10 Then
				If (Not locked) Then
					Color 250,250,250
				Else
					Color 150,150,150
				EndIf
				Rect(barx + barwidth / 2, bary + 5*MenuScale, 2*MenuScale, barheight - 10)
				Rect(barx + barwidth / 2 - 3*MenuScale, bary + 5*MenuScale, 2*MenuScale, barheight - 10)
				Rect(barx + barwidth / 2 + 3*MenuScale, bary + 5*MenuScale, 2*MenuScale, barheight - 10)
			EndIf
		Else
			If width > 10 Then
				If (Not locked) Then
					Color 250,250,250
				Else
					Color 150,150,150
				EndIf
				Rect(barx + 4*MenuScale, bary + barheight / 2, barwidth - 10*MenuScale, 2*MenuScale)
				Rect(barx + 4*MenuScale, bary + barheight / 2 - 3*MenuScale, barwidth - 10*MenuScale, 2*MenuScale)
				Rect(barx + 4*MenuScale, bary + barheight / 2 + 3*MenuScale, barwidth - 10*MenuScale, 2*MenuScale)
			EndIf
		EndIf
		
		If (Not locked) Then
			If MousePosX>barx And MousePosX<barx+barwidth
				If MousePosY>bary And MousePosY<bary+barheight
					OnBar = True
				Else
					If (Not MouseDown1)
						OnBar = False
					EndIf
				EndIf
			Else
				If (Not MouseDown1)
					OnBar = False
				EndIf
			EndIf
			
			If MouseDown1
				If OnBar
					If dir = 0
						Return Min(Max(bar + MouseXSpeed() / Float(width - barwidth), 0), 1)
					Else
						Return Min(Max(bar + MouseYSpeed() / Float(height - barheight), 0), 1)
					EndIf
				EndIf
			EndIf
			
			Local MouseSpeedZ = MouseZSpeed()
			
			If MouseSpeedZ<>0 Then ;Only for vertical scroll bars
                Return Min(Max(bar - (MouseSpeedZ*speed) / Float(height - barheight), 0), 1)
            EndIf
		EndIf
	Else
		Color(0, 0, 0)
		Button(barx, bary, barwidth, barheight, "", locked)
		
		If dir = 0 Then 
			If height > 10 Then
				Color 250,250,250
				Rect(barx + barwidth / 2, bary + 5*MenuScale, 2*MenuScale, barheight - 10)
				Rect(barx + barwidth / 2 - 3*MenuScale, bary + 5*MenuScale, 2*MenuScale, barheight - 10)
				Rect(barx + barwidth / 2 + 3*MenuScale, bary + 5*MenuScale, 2*MenuScale, barheight - 10)
			EndIf
		Else
			If width > 10 Then
				Color 250,250,250
				Rect(barx + 4*MenuScale, bary + barheight / 2, barwidth - 10*MenuScale, 2*MenuScale)
				Rect(barx + 4*MenuScale, bary + barheight / 2 - 3*MenuScale, barwidth - 10*MenuScale, 2*MenuScale)
				Rect(barx + 4*MenuScale, bary + barheight / 2 + 3*MenuScale, barwidth - 10*MenuScale, 2*MenuScale)
			EndIf
		EndIf
		
		OnBar = True
		
		If dir = 0
			Return Min(Max(bar / Float(width - barwidth), 0), 1)
		Else
			Return Min(Max(bar / Float(height - barheight), 0), 1)
		EndIf
	EndIf
	
	Return bar
	
End Function

Function Button%(x,y,width,height,txt$, disabled%=False)
	Local Pushed = False
	
	Color 50, 50, 50
	If Not disabled Then
		If (Not co\Enabled)
			If ScaledMouseX() > x And ScaledMouseX() < x+width Then
				If ScaledMouseY() > y And ScaledMouseY() < y+height Then
					If MouseDown1 Then
						Pushed = True
						Color 50*0.6, 50*0.6, 50*0.6
					Else
						Color Min(50*1.2,255),Min(50*1.2,255),Min(50*1.2,255)
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
	If Pushed Then 
		Rect x,y,width,height
		Color 133,130,125
		Rect x+1*MenuScale,y+1*MenuScale,width-1*MenuScale,height-1*MenuScale,False	
		Color 10,10,10
		Rect x,y,width,height,False
		Color 250,250,250
		Line x,y+height-1*MenuScale,x+width-1*MenuScale,y+height-1*MenuScale
		Line x+width-1*MenuScale,y,x+width-1*MenuScale,y+height-1*MenuScale
	Else
		Rect x,y,width,height
		Color 133,130,125
		Rect x,y,width-1*MenuScale,height-1*MenuScale,False	
		Color 250,250,250
		Rect x,y,width,height,False
		Color 10,10,10
		Line x,y+height-1,x+width-1,y+height-1
		Line x+width-1,y,x+width-1,y+height-1		
	EndIf
	
	Color 255,255,255
	If disabled Then Color 70,70,70
	Text x+width/2, y+height/2-1*MenuScale, txt, True, True
	
	Color 0,0,0
	
	If Pushed And MouseHit1 Then Return True
End Function

Type MenuButton
	Field x%,y%,width%,height%
	Field txt$
	Field bigfont%
	Field currButton%=-1
	Field currButtonTab%=0
	Field currButtonSub%=0
	Field Menu3D%
	Field locked%
End Type

Function DrawAllMenuButtons()
	Local mb.MenuButton
	
	For mb = Each MenuButton
		If mb\Menu3D Then
			Color (255, 255, 255)
			If mb\bigfont = True Then
				SetFont fo\Font[Font_Menu]
			ElseIf mb\bigfont = 2 Then
				SetFont fo\Font[Font_Default_Large]
			ElseIf mb\bigfont = 3 Then
				SetFont fo\Font[Font_Default_Medium]
			Else
				SetFont fo\Font[Font_Default]
			EndIf
			Text(mb\x, mb\y + mb\height / 2, mb\txt, False, True)
			If (Not ConsoleOpen) Then
				If (Not co\Enabled) Then
					If MouseOn(mb\x, mb\y, mb\width, mb\height) Then
						If mb\bigfont = True Then
							SetFont fo\Font[Font_Menu]
						ElseIf mb\bigfont = 2 Then
							SetFont fo\Font[Font_Default_Large]
						ElseIf mb\bigfont = 3 Then
							SetFont fo\Font[Font_Default_Medium]
						Else
							SetFont fo\Font[Font_Default]
						EndIf
						If Rand(20)=1 Then
							Color 40,40,40
							Text(mb\x+Rand(-20,20), (mb\y + mb\height / 2)+Rand(-20,20), mb\txt, False, True)
						EndIf
						Color 100,100,100
						Text(mb\x, mb\y + mb\height / 2, mb\txt, False, True)
					EndIf
				Else
					If co\CurrButton[0] = mb\currButton Then
						If mb\bigfont = True Then
							SetFont fo\Font[Font_Menu]
						ElseIf mb\bigfont = 2 Then
							SetFont fo\Font[Font_Default_Large]
						ElseIf mb\bigfont = 3 Then
							SetFont fo\Font[Font_Default_Medium]
						Else
							SetFont fo\Font[Font_Default]
						EndIf
						If Rand(20)=1 Then
							Color 40,40,40
							Text(mb\x+Rand(-20,20), (mb\y + mb\height / 2)+Rand(-20,20), mb\txt, False, True)
						EndIf
						Color 100,100,100
						Text(mb\x, mb\y + mb\height / 2, mb\txt, False, True)
					EndIf
				EndIf
			EndIf
		Else
			DrawFrame (mb\x, mb\y, mb\width, mb\height)
			If (Not co\Enabled) Then
				If MouseOn(mb\x, mb\y, mb\width, mb\height) Then
					Color(30, 30, 30)
					Rect(mb\x + 4, mb\y + 4, mb\width - 8, mb\height - 8)	
				Else
					Color(0, 0, 0)
				EndIf
			Else
				If co\CurrButton[mb\currButtonTab] = mb\currButton Then
					If co\CurrButtonSub[mb\currButtonTab] = mb\currButtonSub Then
						Color(30, 30, 30)
						Rect(mb\x + 4, mb\y + 4, mb\width - 8, mb\height - 8)
					EndIf
				Else
					Color(0, 0, 0)
				EndIf
			EndIf
			
			Color (255, 255, 255)
			If mb\bigfont = True Then
				SetFont fo\Font[Font_Menu]
			ElseIf mb\bigfont = 2 Then
				SetFont fo\Font[Font_Default_Large]
			ElseIf mb\bigfont = 3 Then
				SetFont fo\Font[Font_Default_Medium]
			Else
				SetFont fo\Font[Font_Default]
			EndIf
			
			Text(mb\x + mb\width / 2, mb\y + mb\height / 2, mb\txt, True, True)
		EndIf
	Next
	
End Function

Type MenuTick
	Field x%,y%
	Field selected%
	Field locked%
	Field currButton%=-1
	Field currButtonTab%=0
	Field currButtonSub%=0
End Type

Function DrawAllMenuTicks()
	Local mt.MenuTick
	Local width%, height%
	
	
	For mt = Each MenuTick
		width%=20*MenuScale
		height%=20*MenuScale
		
		If mt\locked Then
			Color 250,0,0
		Else
			Color 50,50,50
		EndIf
		
		;Color (255, 255, 255)
		Rect(mt\x, mt\y, width, height)
		Color (255, 255, 255)
		If (Not co\Enabled) Then
			Local Highlight% = MouseOn(mt\x, mt\y, width, height) And (Not mt\locked)
		Else
			If co\CurrButton[mt\currButtonTab]=mt\currButton And co\CurrButtonSub[mt\currButtonTab]=mt\currButtonSub Then
				Highlight = True
			Else
				Highlight = False
			EndIf
		EndIf
		If Highlight Then
			Color(50, 50, 50)
		Else
			Color(0, 0, 0)		
		EndIf
		If Highlight And co\Enabled And mt\selected Then
			Color 200,200,200
			Rect(mt\x, mt\y, width, height)
			;Color (255, 255, 255)
			If mt\locked Then
				Color 250,0,0
			Else
				Color 50,50,50
			EndIf
			Rect(mt\x, mt\y, width, height)
			Color (255, 255, 255)
		EndIf
		Rect(mt\x + 2, mt\y + 2, width - 4, height - 4)
		If mt\selected Then
			If Highlight Then
				Color 255,255,255
			Else
				Color 200,200,200
			EndIf
			Rect(mt\x + 4, mt\y + 4, width - 8, height - 8)
		EndIf
		Color 255, 255, 255
	Next
	
End Function

Type MenuInputBox
	Field x%,y%,width%,height%
	Field txt$
	Field id%
	Field currButton%=-1
	Field currButtonTab%=0
	Field currButtonSub%=0
	Field drawAsPassword%=False
	Field font%=Font_Default
End Type

Function DrawAllMenuInputBoxes()
	Local mib.MenuInputBox
	Local i%
	
	For mib = Each MenuInputBox
		Color (255, 255, 255)
		;DrawTiledImageRect(MenuWhite, (mib\x Mod 256), (mib\y Mod 256), 512, 512, mib\x, mib\y, mib\width, mib\height)
		Rect(mib\x, mib\y, mib\width, mib\height)
		Color (0, 0, 0)
		SetFont fo\Font[mib\font]
		If (Not co\Enabled) Then
			If MouseOn(mib\x, mib\y, mib\width, mib\height) Then
				Color(50, 50, 50)
			EndIf
		Else
			If co\CurrButton[mib\currButtonTab] = mib\currButton Then
				If co\CurrButtonSub[mib\currButtonTab] = mib\currButtonSub Then
					Color(50, 50, 50)
				EndIf
			EndIf
		EndIf
		Rect(mib\x + 2, mib\y + 2, mib\width - 4, mib\height - 4)
		Color (255, 255, 255)
		If SelectedInputBox = mib\id Then
			If (MilliSecs() Mod 800) < 400 Then
				Rect (mib\x + mib\width / 2 - StringWidth(mib\txt) / 2 + StringWidth(Left(mib\txt, CursorPos)), mib\y + mib\height / 2 - 5, 2, 12)
			EndIf
		EndIf
		If (Not mib\drawAsPassword) Then
			Text(mib\x + mib\width / 2, mib\y + mib\height / 2, mib\txt, True, True)
		Else
			Local PWString$ = ""
			For i = 0 To Len(mib\txt)-1
				PWString = PWString + "*"
			Next
			Text(mib\x + mib\width / 2, mib\y + mib\height / 2, PWString, True, True)
		EndIf
	Next
	
End Function

Type MenuSlideBar
	Field x%,y%,width%
	Field value#
	Field currButton%=-1
	Field currButtonTab%=0
	Field currButtonSub%=0
	Field txtlow$,txthigh$
End Type

Function DrawAllMenuSlideBars()
	Local msb.MenuSlideBar
	
	For msb = Each MenuSlideBar
		If co\Enabled Then
			If co\CurrButton[msb\currButtonTab]=msb\currButton Then
				If co\CurrButtonSub[msb\currButtonTab]=msb\currButtonSub Then
					Color 30,30,30
					Rect(msb\x, msb\y, msb\width + 14, 20,True)
				EndIf
			EndIf
		EndIf
		Color 255,255,255
		Rect(msb\x, msb\y, msb\width + 14, 20,False)
		DrawImage(MenuMeterIMG, msb\x + msb\width * msb\value / 100.0 +3, msb\y+3)
		Color 170,170,170 
		Text (msb\x - 50 * MenuScale, msb\y + 6*MenuScale, msb\txtlow)					
		Text (msb\x + msb\width + 38 * MenuScale, msb\y+6*MenuScale, msb\txthigh)
	Next
	
End Function

Type MenuSlider
	Field x%,y%,width%
	Field value%
	Field ID%
	Field val1$,val2$,val3$,val4$,val5$
	Field currButton%=-1
	Field currButtonTab%=0
	Field currButtonSub%=0
	Field amount%
End Type

Function DrawAllMenuSliders()
	Local ms.MenuSlider
	
	For ms = Each MenuSlider
		If ms\amount=3
			Color 200,200,200
			Rect(ms\x,ms\y,ms\width+14,10,True)
			Rect(ms\x,ms\y-8,4,14,True)
			Rect(ms\x+(ms\width/2)+5,ms\y-8,4,14,True)
			Rect(ms\x+ms\width+10,ms\y-8,4,14,True)
			If (Not co\Enabled)
				If ms\ID = OnSliderID
					Color 0,255,0
					Rect(ms\x,ms\y,ms\width+14,10,True)
				Else
					If (ScaledMouseX() >= ms\x) And (ScaledMouseX() <= ms\x+ms\width+14) And (ScaledMouseY() >= ms\y-8) And (ScaledMouseY() <= ms\y+10)
						Color 0,200,0
						Rect(ms\x,ms\y,ms\width+14,10,False)
					EndIf
				EndIf
			Else
				If ms\ID = OnSliderID
					Color 0,255,0
					Rect(ms\x,ms\y,ms\width+14,10,True)
				EndIf
			EndIf
			If ms\value = 0
				DrawImage(MenuMeterIMG,ms\x,ms\y-8)
			ElseIf ms\value = 1
				DrawImage(MenuMeterIMG,ms\x+(ms\width/2)+3,ms\y-8)
			Else
				DrawImage(MenuMeterIMG,ms\x+ms\width+6,ms\y-8)
			EndIf
			Color 170,170,170
			If ms\value = 0
				Text(ms\x+2,ms\y+10+MenuScale,ms\val1,True)
			ElseIf ms\value = 1
				Text(ms\x+(ms\width/2)+7,ms\y+10+MenuScale,ms\val2,True)
			Else
				Text(ms\x+ms\width+12,ms\y+10+MenuScale,ms\val3,True)
			EndIf
		ElseIf ms\amount=5
			Color 200,200,200
			Rect(ms\x,ms\y,ms\width+14,10,True)
			Rect(ms\x,ms\y-8,4,14,True) ;1
			Rect(ms\x+(ms\width/4)+2.5,ms\y-8,4,14,True) ;2
			Rect(ms\x+(ms\width/2)+5,ms\y-8,4,14,True) ;3
			Rect(ms\x+(ms\width*0.75)+7.5,ms\y-8,4,14,True) ;4
			Rect(ms\x+ms\width+10,ms\y-8,4,14,True) ;5
			If (Not co\Enabled)
				If ms\ID = OnSliderID
					Color 0,255,0
					Rect(ms\x,ms\y,ms\width+14,10,True)
				Else
					If (ScaledMouseX() >= ms\x) And (ScaledMouseX() <= ms\x+ms\width+14) And (ScaledMouseY() >= ms\y-8) And (ScaledMouseY() <= ms\y+10)
						Color 0,255,0
						Rect(ms\x,ms\y,ms\width+14,10,False)
					EndIf
				EndIf
			Else
				If ms\ID = OnSliderID
					Color 0,255,0
					Rect(ms\x,ms\y,ms\width+14,10,True)
				EndIf
			EndIf
			If ms\value = 0
				DrawImage(MenuMeterIMG,ms\x,ms\y-8)
			ElseIf ms\value = 1
				DrawImage(MenuMeterIMG,ms\x+(ms\width/4)+1.5,ms\y-8)
			ElseIf ms\value = 2
				DrawImage(MenuMeterIMG,ms\x+(ms\width/2)+3,ms\y-8)
			ElseIf ms\value = 3
				DrawImage(MenuMeterIMG,ms\x+(ms\width*0.75)+4.5,ms\y-8)
			Else
				DrawImage(MenuMeterIMG,ms\x+ms\width+6,ms\y-8)
			EndIf
			Color 170,170,170
			If ms\value = 0
				Text(ms\x+2,ms\y+10+MenuScale,ms\val1,True)
			ElseIf ms\value = 1
				Text(ms\x+(ms\width/4)+4.5,ms\y+10+MenuScale,ms\val2,True)
			ElseIf ms\value = 2
				Text(ms\x+(ms\width/2)+7,ms\y+10+MenuScale,ms\val3,True)
			ElseIf ms\value = 3
				Text(ms\x+(ms\width*0.75)+9.5,ms\y+10+MenuScale,ms\val4,True)
			Else
				Text(ms\x+ms\width+12,ms\y+10+MenuScale,ms\val5,True)
			EndIf
		EndIf
	Next
	
End Function

Type MenuDropdown
	Field x%,y%
	Field value%
	Field ID%
	Field txt1$,txt2$,txt3$,txt4$,txt5$
	Field size%
End Type	

Function DrawAllMenuDropdowns()
	Local md.MenuDropdown
	Local txt$
	Local i%
	For md = Each MenuDropdown
		DrawFrame(md\x, md\y, 120 * MenuScale, 30 * MenuScale)
		Select md\value
			Case 0
				txt = md\txt1
			Case 1
				txt = md\txt2
			Case 2
				txt = md\txt3
			Case 3
				txt = md\txt4
			Case 4
				txt = md\txt5
		End Select
		Text(md\x + (120/2) * MenuScale, md\y + 15 * MenuScale, txt, True, True)
		Color(255, 255, 255)
	Next
	
	;Drawing the selection box when selected
	For md = Each MenuDropdown
		If SelectedInputBox = md\ID Then
			;Why 30.5? I have no idea!
			DrawFrame(md\x, md\y + (30.5 - FRAME_THICK) * MenuScale, (120 + 30 - FRAME_THICK) * MenuScale, (20 + (20 * md\size)) * MenuScale)
			For i = 0 To md\size-1
				Color(255, 255, 255)
				Select i
					Case 0
						txt = md\txt1
					Case 1
						txt = md\txt2
					Case 2
						txt = md\txt3
					Case 3
						txt = md\txt4
					Case 4
						txt = md\txt5
				End Select
				Text(md\x + ((120 + 30 - FRAME_THICK)/2) * MenuScale, md\y + (30.5 - FRAME_THICK + 20 + 20 * i) * MenuScale, txt, True, True)
				If MouseOn(md\x + 5.5 * MenuScale, md\y + (20 - FRAME_THICK + 20 + 20 * i) * MenuScale, 137 * MenuScale, 20 * MenuScale) Then
					Color(100, 100, 100)
					Rect(md\x + 5.5 * MenuScale, md\y + (20 - FRAME_THICK + 20 + 20 * i) * MenuScale, 137 * MenuScale, 20 * MenuScale, False)
					DrawOptionsTooltip(txt, i)
				EndIf
			Next
			Exit
		EndIf
	Next
	
End Function

Function DeleteMenuGadgets()
	
	Delete Each MenuButton
	Delete Each MenuTick
	Delete Each MenuInputBox
	Delete Each MenuSlideBar
	Delete Each MenuSlider
	Delete Each MenuDropdown
	
End Function

Const STORY_RYAN_IMG = 0
Const STORY_D_IMG = 1
Const STORY_SANDERS_IMG = 2

Const DISCORD_IMG = 0
Const MODDB_IMG = 1
Const YOUTUBE_IMG = 2

Type MenuImages
	Field MenuIMG[2]
	Field StoryIMG[3]
	Field ArrowIMG[4]
	Field ChapterIMG[31]
End Type

Function LoadMenuImages(loadWebsites%=True)
	Local i
	
	If I_MIG <> Null Then
		Return
	EndIf
	
	I_MIG = New MenuImages
	
	For i = 0 To 3
		I_MIG\ArrowIMG[i] = LoadImage_Strict("GFX\menu\arrow.png")
		RotateImage I_MIG\ArrowIMG[i], 90 * i
		HandleImage I_MIG\ArrowIMG[i], 0, 0
	Next
	
	If (Not opt\Menu3DEnabled) Then
		I_MIG\MenuIMG[0] = LoadImage_Strict("GFX\Menu 2d\Ryan_back.jpg")
		I_MIG\MenuIMG[1] = LoadImage_Strict("GFX\Menu 2d\scp_text.png")
		
		For i = 0 To 1
			ResizeImage(I_MIG\MenuIMG[i], ImageWidth(I_MIG\MenuIMG[i]) * MenuScale, ImageHeight(I_MIG\MenuIMG[i]) * MenuScale)
		Next
	EndIf
	
	If loadWebsites Then
		I_MIG\StoryIMG[STORY_RYAN_IMG] = LoadImage_Strict("GFX\menu\Stories\Story_Ryan.png")
		I_MIG\StoryIMG[STORY_D_IMG] = LoadImage_Strict("GFX\menu\Stories\Story_D.png")
		I_MIG\StoryIMG[STORY_SANDERS_IMG] = LoadImage_Strict("GFX\menu\Stories\Story_Sanders.png")
		
		For i = 0 To 2
			I_MIG\StoryIMG[i] = ResizeImage2(I_MIG\StoryIMG[i], 160*MenuScale, 160*MenuScale)
		Next
		
		I_MIG\ChapterIMG[30] = LoadImage_Strict("GFX\menu\chapters\chapter_locked.png")
		ResizeImage(I_MIG\ChapterIMG[30], ImageWidth(I_MIG\ChapterIMG[30]) * MenuScale, ImageHeight(I_MIG\ChapterIMG[30]) * MenuScale)
		
		Cls()
		Flip()
	EndIf
End Function

Function DeleteMenuImages()
	Local i
	
	For i = 0 To 1
		FreeImage I_MIG\MenuIMG[i]
	Next
	
	For i = 0 To 2
		FreeImage I_MIG\StoryIMG[i]
	Next
	
	For i = 0 To 3
		FreeImage I_MIG\ArrowIMG[i]
	Next
	
	For i = 0 To 30
		FreeImage I_MIG\ChapterIMG[i]
	Next
	
	Delete I_MIG
End Function

Function CreateMenuLogo.MenuLogo(parent%)
	Local ml.MenuLogo = New MenuLogo,tex
	
	ml\logo = CreateSprite(parent)
	If (Left(CurrentDate(), 7) = "31 Oct ") Then
		tex = LoadTexture_Strict("GFX\menu\Logos\Menu_3d_logo_haloween.png",1+2,0)
	ElseIf (Left(CurrentDate(), 7) = "31 Dec ") Then
		tex = LoadTexture_Strict("GFX\menu\Logos\Menu_3d_logo_new_year.png",1+2,0)
	ElseIf (Left(CurrentDate(), 7) = "31 Aug ") Then
		tex = LoadTexture_Strict("GFX\menu\Logos\Menu_3d_logo_birthday.png",1+2,0)
	Else
		tex = LoadTexture_Strict("GFX\menu\Logos\Menu_3d_logo.png",1+2,0)
	EndIf
	Local gr_scale# = ((Float(opt\GraphicWidth)/Float(opt\GraphicHeight))/(16.0/9.0))
	ScaleSprite ml\logo,0.29/gr_scale,(0.4/650.0*228.0)/gr_scale
	EntityTexture ml\logo,tex
	EntityFX ml\logo,1
	EntityOrder ml\logo,-3000
	If opt\Menu3DEnabled Lor (Not MainMenuOpen) Then
		MoveEntity ml\logo,-0.65+((0.35/gr_scale)-0.35),0.4/gr_scale,1
	Else
		MoveEntity ml\logo,0.06,0.4/gr_scale,1.0
	EndIf
	
	ml\gradient = CreateSprite(parent)
	Local tex2
	If opt\Menu3DEnabled Then
		tex2 = LoadTexture_Strict("GFX\menu\Menu_3d.png",1+2+16+32,0)
	Else
		tex2 = LoadTexture_Strict("GFX\menu 2d\Menu_2d.jpg",1+2+16+32,0)
	EndIf
	ScaleSprite ml\gradient,0.57/gr_scale,0.57/gr_scale
	EntityTexture ml\gradient,tex2
	EntityFX ml\gradient,1
	EntityOrder ml\gradient,-2999
	MoveEntity ml\gradient,-0.43+((0.57/gr_scale)-0.57),0,1
	
	HideEntity ml\logo
	HideEntity ml\gradient
	
	Return ml
End Function

Function UpdateMenuNews()
	
	MenuBlinkTimer[1] = MenuBlinkTimer[1] - (FPSfactor)*2
	
	Color(50, 50, 50)
	If (MenuBlinkTimer[1]) < 0 Then
		
		If Instr((CurrentDate$()), "19 Apr") <> 0 Lor Instr((CurrentDate$()), "11 Nov") <> 0 Then
			If MenuNews <> 0
				MenuNews = Rand(1, 160)
			EndIf
		Else
			MenuNews = Rand(1, 160)
		EndIf
		
		SetFont(fo\Font[Font_Digital_Small])
		
		Select MenuNews
			Case 0
				If Instr((CurrentDate$()), "19 Apr") <> 0
					Select Rand(1, 3)
						Case 1
							MenuStr = GetLocalString("News","news_wolfnaya_1")
						Case 2
							MenuStr = GetLocalString("News","news_wolfnaya_2")
						Case 3
							MenuStr = GetLocalString("News","news_wolfnaya_3")
					End Select
				ElseIf Instr((CurrentDate$()), "11 Nov") <> 0
					Select Rand(1, 3)
						Case 1
							MenuStr = GetLocalString("News","news_endshn_1")
						Case 2
							MenuStr = GetLocalString("News","news_endshn_2")
						Case 3
							MenuStr = GetLocalString("News","news_endshn_3")
					End Select
				EndIf
				MenuNews = Rand(1, 92)
			Case 1, 2
				MenuStr = GetLocalStringR("News","news_1",VersionNumber)
			Case 3, 4, 5
				MenuStr = GetLocalString("News","news_2")
			Case 6, 7, 8
				MenuStr = GetLocalStringR("News","news_3",Rand(1,50))
			Case 9, 10, 11
				MenuStr = GetLocalStringR("News","news_4",Rand(1,20))
			Case 12, 19
				MenuStr = GetLocalStringR("News","news_5",Rand(1,10))
			Case 13, 14, 15, 16
				MenuStr = GetLocalString("News","news_6")
			Case 17
				MenuStr = GetLocalString("News","news_7")
			Case 18, 20, 21, 22, 23, 24
				MenuStr = GetLocalString("News","news_update")
			Case 25
				MenuStr = GetLocalString("News","news_8")
			Case 26, 27, 28, 29
				MenuStr = GetLocalStringR("News","news_9",Rand(10, 999))
			Case 30, 31, 32, 33, 34, 35, 36
				MenuStr = GetLocalString("News","news_10")
			Case 37, 38, 39, 40, 41, 42
				MenuStr = GetLocalString("News","news_11")
			Case 43, 44, 45, 46, 47, 48, 49, 50, 51
				MenuStr = GetLocalStringR("News","news_12",Rand(14, 17))
			Case 52, 53, 54, 55, 56, 57, 58, 59, 60, 61
				MenuStr = GetLocalString("News","news_13")
			Case 62, 63, 64, 65, 66, 67, 68, 69
				MenuStr = GetLocalString("News","news_14")
			Case 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81
				MenuStr = GetLocalString("News","news_15")
			Case 82, 83, 84, 85, 86
				If Instr((CurrentDate$()), "Dec") <> 0
					If Instr((CurrentDate$()), "31 Dec") <> 0
						Select Rand(1, 3)
							Case 1
								MenuStr = GetLocalString("News","news_new_year_1")
							Case 2
								MenuStr = GetLocalString("News","news_new_year_2")
							Case 3
								MenuStr = GetLocalString("News","news_new_year_3")
						End Select
					Else
						MenuStr = GetLocalString("News","news_new_year_4")
					EndIf
				EndIf
				If Instr((CurrentDate$()), "Oct") <> 0
					If Instr((CurrentDate$()), "31 Oct") <> 0
						Select Rand(1, 4)
							Case 1
								MenuStr = GetLocalString("News","news_haloween_1")
							Case 2
								MenuStr = GetLocalString("News","news_haloween_2")
							Case 3
								MenuStr = GetLocalStringR("News","news_haloween_3",Rand(1,106))
							Case 4
								MenuStr = GetLocalString("News","news_haloween_4")
						End Select
					Else
						MenuStr = GetLocalString("News","news_haloween_5")
					EndIf
				Else
					MenuStr = GetLocalString("News","news_16")
				EndIf
			Case 87
				MenuStr = Chr(34)+GetLocalString("News","news_17")+Chr(34)
			Case 88, 89, 90, 91, 92, 93, 94, 95, 133, 134, 135
				MenuStr = GetLocalString("News","news_18")
			Case 96, 97, 98, 99, 100, 101, 102, 103
				MenuStr = GetLocalString("News","facility_news")+": "+Chr(34)+GetLocalString("News","news_mtf_1")+Chr(34)
			Case 104, 105, 106, 107, 108, 109, 110
				MenuStr = GetLocalString("News","facility_news")+": "+Chr(34)+GetLocalString("News","news_mtf_2")+Chr(34)
			Case 111, 112, 113, 114, 115, 116, 117, 118, 119
				MenuStr = GetLocalString("News","facility_news")+": "+Chr(34)+GetLocalString("News","news_mtf_3")+Chr(34)
			Case  120, 121, 122, 123, 124, 125, 130
				MenuStr = GetLocalString("News","facility_news")+": "+Chr(34)+GetLocalString("News","news_mtf_4")+Chr(34)							
			Case  131, 132, 136, 137, 138, 139, 142
				MenuStr = GetLocalString("News","facility_news")+": "+Chr(34)+GetLocalString("News","news_mtf_5")+Chr(34)									
			Case  143, 144 ,145, 140, 126
				MenuStr = GetLocalString("News","facility_news")+": "+Chr(34)+GetLocalString("News","news_mtf_6")+Chr(34)							
			Case  127, 128, 129, 141, 146
				MenuStr = GetLocalString("News","facility_news")+": "+Chr(34)+GetLocalString("News","news_mtf_7")+Chr(34)
			Case  147, 148, 149, 150, 151
				MenuStr = GetLocalString("News","facility_news")+": "+Chr(34)+GetLocalString("News","news_mtf_8")+Chr(34)
			Case  152, 153, 154, 155
				MenuStr = GetLocalString("News","facility_news")+": "+Chr(34)+GetLocalString("News","news_mtf_9")+Chr(34)
			Case 156, 157, 158, 159
				MenuStr = GetLocalString("News","facility_news")+": "+Chr(34)+GetLocalString("News","news_mtf_10")+Chr(34)
			Default														
				MenuStr = GetLocalString("News","facility_news")+": "+Chr(34)+GetLocalString("News","news_mtf_11")+Chr(34)
		End Select
		
		MenuBlinkTimer[1] = MenuBlinkTimer[1]+GraphicsWidth()*1.5+(Len(MenuStr)*2*Int(25 * (opt\GraphicWidth / 1024.0)))
		
	EndIf
	
	SetFont(fo\Font[Font_Digital_Small])
	
	Local x = (MenuBlinkTimer[1]) * MenuScale
	
	Local width = 200 * MenuScale
	Local height = 50 * MenuScale
	DrawFrame(width, opt\GraphicHeight-height, opt\GraphicWidth-width, height)
	
	SetFont(fo\Font[Font_Default_Medium])
	
	Text(x- Len(MenuStr)*Int(25 * (opt\GraphicWidth / 1024.0)), opt\GraphicHeight-(height/2), MenuStr, False, True)
	
	DrawFrame(0, opt\GraphicHeight-height, width, height)
	
	SetFont(fo\Font[Font_Digital_Small])
	
	Text((width)/2, opt\GraphicHeight-(height/2), GetLocalString("News","facility_news"), True, True)
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D