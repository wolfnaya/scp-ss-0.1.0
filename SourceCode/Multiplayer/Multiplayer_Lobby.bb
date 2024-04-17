
Function UpdateLobby()
	Local x#,y#,width#,height#,i%,j%,buttontext$
	Local allReady%, plAmount%
	
	If InLobby() Then
		width = 800.0 * MenuScale
		height = 800.0 * MenuScale
		x = opt\GraphicWidth / 2.0 - (width / 2.0)
		y = opt\GraphicHeight / 2.0 - (height / 2.0)
		
		mp_I\CurrChatMSG = InputBox(x, y + (height - 30.0 * MenuScale), width - 330.0 * MenuScale, 30.0 * MenuScale, mp_I\CurrChatMSG, 1, 50)
		If DrawButton(x + (width - 331.0 * MenuScale), y + (height - 30.0 * MenuScale), 30.0 * MenuScale, 30.0 * MenuScale, Chr(62), False, False, True) Lor (SelectedInputBox=1 And KeyHit(28)) Then
			If mp_I\CurrChatMSG <> "" Then
				CreateChatMSG()
				SelectedInputBox = 1
			EndIf
		EndIf
		
		If DrawButton(x, y + height + 10.0 * MenuScale, 195.0 * MenuScale, 60.0 * MenuScale, GetLocalString("Menu", "disconnect"), 3) Then
			LeaveMPGame(True)
			Return
		EndIf
		;TODO: When in team selection, all the buttons below should not be clickable anymore
		If mp_I\PlayState = GAME_SERVER Then
			allReady = True
			For i = 0 To (mp_I\MaxPlayers-1)
				If Players[i]<>Null Then
					If (Not Players[i]\IsReady) Then
						allReady = False
						Exit
					EndIf
				EndIf
			Next
			If allReady Then
				mp_I\ReadyTimer = Max(mp_I\ReadyTimer - FPSfactor, 0.0)
			Else
				mp_I\ReadyTimer = 70*5
			EndIf
			
			If DrawButton(x + 201.667 * MenuScale, y + height + 10.0 * MenuScale, 195.0 * MenuScale, 60.0 * MenuScale, GetLocalString("Lobby", "force_start"), 3) Then
				mp_I\ReadyTimer = 0.0
			EndIf
			
			If mp_I\ReadyTimer = 0.0 Then
				ResetControllerSelections()
				ResetInput()
				If mp_I\Gamemode\ID = Gamemode_EAF Then
					PlayAnnouncement("SFX\Multiplayer\Intercom\Announc_CI.ogg")
				Else
					If mp_O\OtherTeams Then
						If mp_I\Gamemode\ID = Gamemode_Deathmatch Then
							PlayAnnouncement("SFX\Multiplayer\Intercom\Announc_RRH.ogg")
						Else
							PlayAnnouncement("SFX\Multiplayer\Intercom\Announc_SNE.ogg")
						EndIf
					Else
						PlayAnnouncement("SFX\Multiplayer\Intercom\Announc_NTF.ogg")
					EndIf
				EndIf
			EndIf
		EndIf
		If mp_I\IsReady Then
			buttontext = GetLocalString("Lobby", "notready")
		Else
			buttontext = GetLocalString("Lobby", "ready")
		EndIf
		If mp_I\Gamemode\ID <> Gamemode_Deathmatch Lor Players[mp_I\PlayerID]\Team > Team_Unknown Then
			If DrawButton(x + 403.333 * MenuScale, y + height + 10.0 * MenuScale, 195.0 * MenuScale, 60.0 * MenuScale, buttontext, 3) Then
				mp_I\IsReady = (Not mp_I\IsReady)
			EndIf
		EndIf
		If mp_I\Gamemode\ID = Gamemode_Deathmatch Then
			If DrawButton(x + 605.0 * MenuScale, y + height + 10.0 * MenuScale, 195.0 * MenuScale, 60.0 * MenuScale, GetLocalString("Lobby", "change_team"), 3) Then
				If mp_I\PlayState = GAME_SERVER Then
					Players[mp_I\PlayerID]\Team = Team_Unknown
				Else
					Players[mp_I\PlayerID]\WantsTeam = Team_Unknown
				EndIf
				mp_I\IsReady = False
				DeleteMenuGadgets()
			EndIf
		EndIf
		
		If ((mp_I\PlayState = GAME_CLIENT And Players[mp_I\PlayerID]\WantsTeam = Team_Unknown) Lor (mp_I\PlayState = GAME_SERVER And Players[mp_I\PlayerID]\Team = Team_Unknown)) Then
			y = y + (height / 4.0) + 100.0 * MenuScale
			For i = 0 To MaxPlayerTeams-1
				x = (opt\GraphicWidth / 2.0) - (width / 3.2) + 15.0 * MenuScale + ((ImageWidth(mp_I\Gamemode\img[i]) + 10.0 * MenuScale) * i)
				If MouseHit1 And MouseOn(x, y, ImageWidth(mp_I\Gamemode\img[i]), ImageHeight(mp_I\Gamemode\img[i])) Then
					plAmount = 0
					For j = 0 To (mp_I\MaxPlayers-1)
						If Players[j]<>Null Then
							If Players[j]\Team = (i+1) Then
								plAmount = plAmount + 1
							EndIf
						EndIf
					Next
					If plAmount < Ceil(mp_I\MaxPlayers/2) Then
						If mp_I\PlayState = GAME_SERVER Then
							Players[mp_I\PlayerID]\Team = Team_MTF + i
							SetupTeam(mp_I\PlayerID)
						Else
							Players[mp_I\PlayerID]\WantsTeam = Team_MTF + i
						EndIf
					EndIf
				EndIf
			Next
		EndIf
		
		CountdownBeep(mp_I\ReadyTimer, 3)
		
		If mp_I\ReadyTimer = 0.0 And ((mp_I\PlayState = GAME_CLIENT And Players[mp_I\PlayerID]\WantsTeam > Team_Unknown) Lor (mp_I\PlayState = GAME_SERVER And Players[mp_I\PlayerID]\Team > Team_Unknown)) Then
			ShouldDeleteGadgets = True
			DeleteMenuGadgets()
		EndIf
	EndIf
	
End Function

Function RenderLobby()
	Local x#,y#,width#,height#,i%,j%
	Local plAmount%,plAmount2%
	Local temp#
	Local cmsg.ChatMSG
	Local ChatMSGAmount% = 0
	Local ChatBaseY# = 0.0, ChatExtraSpace# = 0.0
	
	If InLobby() Then
		width = 800.0 * MenuScale
		height = 800.0 * MenuScale
		x = opt\GraphicWidth / 2.0 - (width / 2.0)
		y = opt\GraphicHeight / 2.0 - (height / 2.0)
		
		DrawFrame(x, y, width - 300.0 * MenuScale, height - 30.0 * MenuScale)
		
		SetFont fo\Font[Font_Default]
		
		For cmsg = Each ChatMSG
			ChatMSGAmount = ChatMSGAmount + 1
		Next
		
		;Temporary, will probably be removed/tweaked once a scrollbar exists
		If ChatMSGAmount > 11
			Delete First ChatMSG
		EndIf
		
		ChatBaseY = y + (height - 40.0 * MenuScale) - (30.0 * MenuScale) * (ChatMSGAmount * 2) - (10.0 * MenuScale) * (ChatMSGAmount - 1)
		ChatMSGAmount = 1
		For cmsg = Each ChatMSG
			Color 200,200,200
			If cmsg\PlayerID = 0 Then Color 100,100,255
			If cmsg\IsServerMSG > SERVER_MSG_NO Then Color 255,255,0
			If cmsg\IsServerMSG = SERVER_MSG_NO Then
				Text x + 10.0 * MenuScale, (ChatBaseY + (30.0 * MenuScale) * (ChatMSGAmount - 1)) + ChatExtraSpace, cmsg\PlayerName + ":"
			Else
				Text x + 10.0 * MenuScale, (ChatBaseY + (30.0 * MenuScale) * (ChatMSGAmount - 1)) + ChatExtraSpace, GetLocalString("Chat", "server") + ":"
			EndIf
			
			Color 255,255,255
			If cmsg\IsServerMSG > SERVER_MSG_NO Then Color 255,255,0
			Text x + 10.0 * MenuScale, (ChatBaseY + (30.0 * MenuScale) * ChatMSGAmount) + ChatExtraSpace, Steam_FilterText(cmsg\SteamIDUpper, cmsg\SteamIDLower, AssembleChatMSG(cmsg))
			ChatMSGAmount = ChatMSGAmount + 2
			ChatExtraSpace = ChatExtraSpace + 10.0 * MenuScale
		Next
		
		Color 255,255,255
		SetFont fo\Font[Font_Default_Large]
		Text opt\GraphicWidth / 2.0, y - 50.0 * MenuScale, mp_I\Gamemode\name + " - " + mp_I\MapInList\Name, True
		
		For i = 0 To 1
			DrawFrame(x + width - 280.0 * MenuScale, y + (i * (height / 2.0 + 10.0 * MenuScale)), 280.0 * MenuScale, 60.0 * MenuScale)
			DrawFrame(x + width - 280.0 * MenuScale, (y + (59.0 * MenuScale)) + (i * (height / 2.0 + 10.0 * MenuScale)), 280.0 * MenuScale, (height / 2.0) - 70.0 * MenuScale)
		Next
		
		SetFont fo\Font[Font_Default_Medium]
		If mp_I\ReadyTimer < 70*5 Then
			TextWithAlign opt\GraphicWidth - 10.0 * MenuScale, 10.0 * MenuScale, GetLocalStringR("Multiplayer","starting_in",TurnIntoSeconds(mp_I\ReadyTimer)), 2
		EndIf
		Select mp_I\Gamemode\ID
			Case Gamemode_Deathmatch
				SetFont fo\Font[Font_Default]
				For i = 0 To (mp_I\MaxPlayers-1)
					If Players[i]<>Null Then
						temp = 0
						Select Players[i]\Team
							Case Team_MTF
								plAmount = plAmount + 1
								temp = y + (60.0 * MenuScale) + ((plAmount - 1) * (55.0 * MenuScale))
							Case Team_CI
								plAmount2 = plAmount2 + 1
								temp = y + (60.0 * MenuScale) + (height / 2.0 + 10.0 * MenuScale) + ((plAmount2 - 1) * (55.0 * MenuScale))
						End Select
						If temp>0 Then
							DrawFrame(x + width - 280.0 * MenuScale, temp, 55.0 * MenuScale, 55.0 * MenuScale)
							DrawFrame(x + width - 226.0 * MenuScale, temp, 225.0 * MenuScale, 55.0 * MenuScale)
							Text x + width - 216.0 * MenuScale, temp + 27.5 * MenuScale, LimitText(Players[i]\Name,20), False, True
							If Players[i]\IsReady Then
								DrawImage(mp_I\TickIcon, x + width - 280.0 * MenuScale, temp)
							EndIf
						EndIf
					EndIf
				Next
				If mp_O\OtherTeams Then
					Text x + width - 140.0 * MenuScale, y + 30.0 * MenuScale, GetLocalString("Multiplayer", "rrh")+": "+plAmount, True, True
					Text x + width - 140.0 * MenuScale, y + (height / 2.0 + 40.0 * MenuScale), GetLocalString("Multiplayer", "sh")+": "+plAmount2, True, True
				Else
					Text x + width - 140.0 * MenuScale, y + 30.0 * MenuScale, GetLocalString("Multiplayer", "ntf")+": "+plAmount, True, True
					Text x + width - 140.0 * MenuScale, y + (height / 2.0 + 40.0 * MenuScale), GetLocalString("Multiplayer", "ci")+": "+plAmount2, True, True
				EndIf
			Default
				SetFont fo\Font[Font_Default]
				For i = 0 To (mp_I\MaxPlayers-1)
					If Players[i]<>Null Then
						plAmount = plAmount + 1
						temp = y + (60.0 * MenuScale) + ((plAmount - 1) * (55.0 * MenuScale))
						DrawFrame(x + width - 280.0 * MenuScale, temp, 55.0 * MenuScale, 55.0 * MenuScale)
						DrawFrame(x + width - 226.0 * MenuScale, temp, 225.0 * MenuScale, 55.0 * MenuScale)
						Text x + width - 216.0 * MenuScale, temp + 27.5 * MenuScale, LimitText(Players[i]\Name,20), False, True
						If Players[i]\IsReady Then
							DrawImage(mp_I\TickIcon, x + width - 280.0 * MenuScale, temp)
						EndIf
					EndIf
				Next
				
				If mp_I\Gamemode\ID = Gamemode_EAF Then
					If mp_O\OtherTeams Then
						Text x + width - 140.0 * MenuScale, y + 30.0 * MenuScale, GetLocalString("Multiplayer", "sh")+": "+plAmount, True, True
					Else
						Text x + width - 140.0 * MenuScale, y + 30.0 * MenuScale, GetLocalString("Multiplayer", "ci")+": "+plAmount, True, True
					EndIf
				Else
					If mp_O\OtherTeams Then
						Text x + width - 140.0 * MenuScale, y + 30.0 * MenuScale, GetLocalString("Multiplayer", "sne")+": "+plAmount, True, True
					Else
						Text x + width - 140.0 * MenuScale, y + 30.0 * MenuScale, GetLocalString("Multiplayer", "ntf")+": "+plAmount, True, True
					EndIf
				EndIf
				
				Text x + width - 140.0 * MenuScale, y + (height / 2.0 + 40.0 * MenuScale), GetLocalString("Multiplayer", "info"), True, True
				
				Local tempstr$
				If mp_I\Gamemode\Difficulty = MP_DIFFICULTY_KETER Then
					tempstr = GetLocalString("Difficulty", "keter")
				ElseIf mp_I\Gamemode\Difficulty = MP_DIFFICULTY_EUCLID Then
					tempstr = GetLocalString("Difficulty", "euclid")
				Else
					tempstr = GetLocalString("Difficulty", "safe")
				EndIf
				Text x + width - 260.0 * MenuScale, y + (height / 2.0 + 100.0 * MenuScale), GetLocalString("Menu", "difficulty")+": "+tempstr, False, True
				Text x + width - 260.0 * MenuScale, y + (height / 2.0 + 120.0 * MenuScale), GetLocalString("Multiplayer", "wave_amount")+": "+mp_I\Gamemode\MaxPhase, False, True
				
				If mp_I\Gamemode\ID = Gamemode_Waves Then
					
					Text x + width - 260.0 * MenuScale, y + (height / 2.0 + 140.0 * MenuScale), GetLocalString("Multiplayer", "scp_to_contain")+": "+mp_I\MapInList\BossNPC, False, True
					
					Local hardcorstatus$
					If mp_I\HardcoreMP Then	
						Color 255,0,0
						hardcorstatus$ = GetLocalString("Menu", "true")
					Else
						Color 0,255,0
						hardcorstatus$ = GetLocalString("Menu", "false")
					EndIf
					Text x + width - 260.0 * MenuScale, y + (height / 2.0 + 160.0 * MenuScale), GetLocalString("Multiplayer", "hardcore_enabled")+": "+hardcorstatus$, False, True
				EndIf
				
				Color 255,255,255
		End Select
		
		If ((mp_I\PlayState = GAME_CLIENT And Players[mp_I\PlayerID]\WantsTeam = Team_Unknown) Lor (mp_I\PlayState = GAME_SERVER And Players[mp_I\PlayerID]\Team = Team_Unknown)) Then
			DrawFrame((opt\GraphicWidth / 2.0) - (width / 3.2), (opt\GraphicHeight / 2.0) - (height / 5.0), width / 1.6, height / 2.5)
			SetFont fo\Font[Font_Default_Large]
			Text opt\GraphicWidth / 2.0, y + (height / 4.0) + 45.0 * MenuScale, GetLocalString("Multiplayer", "select"), True, False
			y = y + (height / 4.0) + 100.0 * MenuScale
			For i = 0 To 1
				x = (opt\GraphicWidth / 2.0) - (width / 3.2) + 15.0 * MenuScale + ((ImageWidth(mp_I\Gamemode\img[i]) + 10.0 * MenuScale) * i)
				If MouseOn(x, y, ImageWidth(mp_I\Gamemode\img[i]), ImageHeight(mp_I\Gamemode\img[i])) Then
					;TODO: Add check when team is full
					Color 0,255,0
					Rect x - 3.0 * MenuScale, y - 3.0 * MenuScale, ImageWidth(mp_I\Gamemode\img[i]) + 6.0 *MenuScale, ImageHeight(mp_I\Gamemode\img[i]) + 6.0 * MenuScale, True
					Color 255,255,255
				EndIf
				DrawImage mp_I\Gamemode\img[i], x, y
			Next
		EndIf
		
		DrawAllMenuButtons()
		DrawAllMenuInputBoxes()
		
	EndIf
	
End Function

Function InLobby()
	
	If mp_I\ReadyTimer > 0.0 Lor (mp_I\PlayState = GAME_CLIENT And Players[mp_I\PlayerID]\WantsTeam = Team_Unknown) Lor (mp_I\PlayState = GAME_SERVER And Players[mp_I\PlayerID]\Team = Team_Unknown) Then
		Return True
	EndIf
	Return False
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D