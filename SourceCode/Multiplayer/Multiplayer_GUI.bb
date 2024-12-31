
Function DrawGUIMP()
	CatchErrors("DrawGUIMP")
	Local i%,x%,y%,j%
	Local yawvalue#,pitchvalue#
	Local g.Guns, fb.FuseBox, ge.Generator
	Local actualtime#
	
	If MenuOpen Lor ConsoleOpen Lor InLobby() Lor mp_I\ChatOpen Lor IsPlayerListOpen() Lor IsModerationOpen() Lor IsInVote() Then
		ShowPointer()
	Else
		HidePointer()
	EndIf
	
	Local playerid% = mp_I\PlayerID
	If Players[mp_I\PlayerID]\CurrHP <= 0.0 And mp_I\SpectatePlayer >= 0 Then
		playerid = mp_I\SpectatePlayer
	EndIf
	
	Local plAmount%
	If HUDenabled And (Not InLobby()) And Players[playerid]\Team > Team_Unknown Then ;CHECK FOR IMPLEMENTATION
		
		RenderInterfaceIcons()
		
		Local width% = 204, height% = 20
		
		SetFont fo\Font%[Font_Digital_Medium]
		x = 80
		y = opt\GraphicHeight - 55
		
		; ~ Health
		Color 0,0,0
		Rect(x - 10, y - 50, 40, 40)
		
		If Players[playerid]\CurrHP > 20 Then
			Color 255,255,255
		Else
			Color 255,0,0
		EndIf
		Rect(x - 11, y - 51, 40 + 2, 40 + 2, False)
		DrawImage mpl\HealthIcon, x - 10, y - 50
		
		If Players[playerid]\CurrHP > 20
			Color 0,255,0
		Else
			Color 255,0,0
		EndIf
		TextWithAlign x + 80, y - 40, Int(Players[playerid]\CurrHP), 2
		; ~ Helmet
		Color 0,0,0
		Rect(x+40, y, 30, 30)
		
		If Players[playerid]\CurrKevlar/1.2 > 20 Then
			Color 255,255,255
		Else
			Color 255,0,0
		EndIf
		Rect(x+40 - 1, y - 1, 30 + 2, 30 + 2, False)
		DrawImage mpl\HelmetIcon, x+40, y
		
		If Players[playerid]\CurrKevlar/1.2 > 20
			Color 0,255,0
		Else
			Color 255,0,0
		EndIf
		TextWithAlign x + 120, y + 5, Int(Players[playerid]\CurrKevlar/1.2), 2
		; ~ Armor
		Color 0,0,0
		Rect(x - 50, y, 30, 30)
		
		If Players[playerid]\CurrKevlar > 20 Then
			Color 255,255,255
		Else
			Color 255,0,0
		EndIf
		Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
		DrawImage mpl\KevlarIcon, x - 50, y
		
		If Players[playerid]\CurrKevlar > 20
			Color 0,255,0
		Else
			Color 255,0,0
		EndIf
		TextWithAlign x + 30, y + 5, Int(Players[playerid]\CurrKevlar), 2
		; ~ Stamina Bar
		If Players[playerid]\CurrStamina < 100.0 Then
			y = opt\GraphicHeight - 55
			x = (opt\GraphicWidth / 2) - (width / 2) + 20
			If Players[playerid]\StaminaEffectTimer > 0 Then
				Color 0, 255, 0
			ElseIf Players[playerid]\CurrStamina <= 20.0 Then
				Color 255, 0, 0	
			Else
				Color 255, 255, 255
			EndIf
			Rect (x, y, width, height, False)
			If Players[playerid]\CurrStamina < 30 Then
				Color 55, 0, 0
			Else
				Color 55, 55, 55
			EndIf
			Rect(x + 3, y + 3, Float(Players[playerid]\CurrStamina * (width - 6) / 100), 14)
			
			Color 0, 0, 0
			Rect(x - 50, y, 30, 30)
			
			If Players[playerid]\StaminaEffectTimer > 0 Then
				Color 0, 255, 0
			ElseIf Players[playerid]\CurrStamina <= 0.0 Then
				Color 255, 0, 0
			Else
				Color 255, 255, 255
			EndIf
			Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
			If Players[playerid]\Crouch Then
				DrawImage CrouchIcon, x - 50, y
			Else
				DrawImage SprintIcon, x - 50, y
			EndIf
		EndIf
		
		If Players[mp_I\PlayerID]\CurrHP <= 0.0 And mp_I\SpectatePlayer >= 0 Then
			Color 255, 255, 255
			SetFont fo\Font%[Font_Default_Medium]
			Text opt\GraphicWidth / 2.0, y - 50, GetLocalStringR("Multiplayer", "spectating", Players[playerid]\Name), True, False
		EndIf
		
		;Gun Stuff
		DrawMPGunsInHud(playerid)
		
		x = 55
		y = 55
		width = 64
		height = 64
		If mpl\SlotsDisplayTimer > 0.0 Then
			For i = 0 To MaxSlots-1
				DrawFrame((x-3)+(128*i),y-3,width+6,height+6)
				If i = Players[mp_I\PlayerID]\SelectedSlot Then
					Color 0,255,0
					Rect (x-3)+(128*i),y-3,width+6,height+6,True
				EndIf
				If Players[mp_I\PlayerID]\WeaponInSlot[i]<>GUN_UNARMED Then
					For g = Each Guns
						If g\ID = Players[mp_I\PlayerID]\WeaponInSlot[i] Then
							DrawImage g\IMG,x+(128*i),y
							Color 255,255,255
							If i = Players[mp_I\PlayerID]\SelectedSlot Then
								SetFont fo\Font[Font_Default]
								Text(x+(width/2)+(128*i),y+height+10,g\DisplayName,True,False)
							EndIf
							Exit
						EndIf
					Next
				EndIf
			Next
		EndIf
		
		; ~ Boss HP bar
		If mp_I\BossNPC <> Null Then
			If mp_I\BossNPC\HP > 0 Then
				x = (opt\GraphicWidth / 2) - 202
				y = 50
				width = 404
				height = 20
				
				Color 255,255,255
				Rect(x, y, width, height, False)
				
				Color 255,0,0
				SetFont fo\Font[Font_Digital_Medium]
				Text opt\GraphicWidth/2,15,mp_I\BossNPC\NVName,True,False
				Color 255,255,255
				
				If mp_I\BossNPC\HP =< mp_I\MaxBossHealth/6 Then
					Color 255, 0, 0
				ElseIf mp_I\BossNPC\HP =< mp_I\MaxBossHealth/4 Then
					Color 255, 106, 0
				ElseIf mp_I\BossNPC\HP =< mp_I\MaxBossHealth/2 Then
					Color 255, 216, 0
				Else
					Color 0, 127, 14
				EndIf
				Rect(x + 2, y + 3, Int(((width - 2) * (mp_I\BossNPC\HP / Float(mp_I\MaxBossHealth)))), 14)
			EndIf
		EndIf
		
		;Extra item display (for the fuses)
		If Players[playerid]\Item <> Null Then
			x = opt\GraphicWidth - 100
			y = opt\GraphicHeight - 250
			width = 64
			height = 64
			
			DrawFrame((x-3),y-3,width+6,height+6)
			DrawImage Players[playerid]\Item\itemtemplate\invimg,x,y
		EndIf
		
		Select mp_I\Gamemode\ID
			Case Gamemode_Waves	
				;[Block]
				x = opt\GraphicWidth - 50
				y = 50
				If (mp_I\Gamemode\Phase Mod 2) = 1 Then
					Color 0,255,0
					SetFont fo\Font[Font_Digital_Medium]
					actualtime = mp_I\Gamemode\PhaseTimer
					If mp_I\Gamemode\Phase = GamemodeEnd Then
						If (Not IsInVote()) Then
							actualtime = mp_I\Gamemode\PhaseTimer - GamemodeEndTimeTotal + GamemodeEndTime
						ElseIf (Not IsVoteCompleted())
							actualtime = mp_I\Gamemode\PhaseTimer - GamemodeEndTimeAfterVote
						EndIf
					EndIf
					If TurnIntoSeconds(actualtime) < 10 Then
						TextWithAlign x,y,"0:0"+TurnIntoSeconds(actualtime),2,0
					Else
						TextWithAlign x,y,"0:"+TurnIntoSeconds(actualtime),2,0
					EndIf	
				ElseIf (mp_I\Gamemode\Phase > 0) Then
					If mp_I\Gamemode\PhaseTimer > 0 Then
						Color 0,Min(mp_I\Gamemode\PhaseTimer,127.5)*2,0
						SetFont fo\Font[Font_Digital_Large]
						Text opt\GraphicWidth/2,y*2,GetLocalString("Multiplayer", "wave")+" "+(mp_I\Gamemode\Phase/2),1,0
					EndIf
					Color 0,255,0
					SetFont fo\Font[Font_Digital_Medium]
					If mp_I\BossNPC <> Null Then
						TextWithAlign x,y,"X ???",2,1
					Else
					TextWithAlign x,y,"X "+mp_I\Gamemode\EnemyCount,2,1
					EndIf	
					DrawImage mp_I\Gamemode\img[0],x-100,y
					TextWithAlign x,y+60,GetLocalString("Multiplayer", "wave")+" "+(mp_I\Gamemode\Phase/2)+"/"+(mp_I\Gamemode\MaxPhase),2,1
				EndIf
				
				y = y + 120
				Local FusesAmount% = 0
				Local FusesActivatedAmount% = 0
				For fb = Each FuseBox
					FusesAmount% = FusesAmount + MaxFuseAmount
					FusesActivatedAmount = FusesActivatedAmount + fb\fuses
				Next
				If FusesActivatedAmount < FusesAmount Then
					TextWithAlign x,y,GetLocalString("Multiplayer", "fuses")+": " + FusesActivatedAmount + "/" + FusesAmount,2,1
				Else
					i = 1
					For ge = Each Generator
						TextWithAlign x,y+(60 * (i - 1)),GetLocalStringR("Multiplayer","generator",i)+": " + Int(ge\progress / GEN_CHARGE_TIME * 100.0) + "%/100%", 2, 1
						i = i + 1
					Next
				EndIf
				If mp_I\Gamemode\Phase = GamemodeEnd Then
					Local maxPeopleOnTeamMTF = 0
					Local peopleOnTeamMTFDead = 0
					For i = 0 To (mp_I\MaxPlayers-1)
						If Players[i]<>Null Then
							maxPeopleOnTeamMTF = maxPeopleOnTeamMTF + 1
							If Players[i]\CurrHP <= 0 Then
								peopleOnTeamMTFDead = peopleOnTeamMTFDead + 1
							EndIf
						EndIf
					Next
					x = opt\GraphicWidth / 2
					y = opt\GraphicHeight / 2
					Color 255,255,255
					If maxPeopleOnTeamMTF > 0 And peopleOnTeamMTFDead = maxPeopleOnTeamMTF Then
						SetFont fo\Font[Font_Default_Large]
						Text x,y,GetLocalString("Multiplayer", "waves_gameover_1"),True,True
						SetFont fo\Font[Font_Default_Medium]
						If mp_O\OtherTeams Then
							Text x,y+50*MenuScale,GetLocalString("Multiplayer", "contact_sne_lost"),True,True
						Else
							Text x,y+50*MenuScale,GetLocalString("Multiplayer", "contact_lost"),True,True
						EndIf
					Else
						SetFont fo\Font[Font_Default_Large]
						Text x,y,GetLocalStringR("Multiplayer","waves_win_1",mp_I\MapInList\BossNPC),True,True
						SetFont fo\Font[Font_Default_Medium]
						Text x,y+50*MenuScale,GetLocalString("Multiplayer", "waves_win_2"),True,True
					EndIf
				EndIf
				;[End Block]
			Case Gamemode_EAF	
				;[Block]
				x = opt\GraphicWidth - 50
				y = 50
				If (mp_I\Gamemode\Phase Mod 2) = 1 Then
					Color 0,255,0
					SetFont fo\Font[Font_Digital_Medium]
					actualtime = mp_I\Gamemode\PhaseTimer
					If mp_I\Gamemode\Phase = GamemodeEnd Then
						If (Not IsInVote()) Then
							actualtime = mp_I\Gamemode\PhaseTimer - GamemodeEndTimeTotal + GamemodeEndTime
						ElseIf (Not IsVoteCompleted())
							actualtime = mp_I\Gamemode\PhaseTimer - GamemodeEndTimeAfterVote
						EndIf
					EndIf
					If TurnIntoSeconds(actualtime) < 10 Then
						TextWithAlign x,y,"0:0"+TurnIntoSeconds(actualtime),2,0
					Else
						TextWithAlign x,y,"0:"+TurnIntoSeconds(actualtime),2,0
					EndIf	
				ElseIf (mp_I\Gamemode\Phase > 0) Then
					If mp_I\Gamemode\PhaseTimer > 0 Then
						Color 0,Min(mp_I\Gamemode\PhaseTimer,127.5)*2,0
						SetFont fo\Font[Font_Digital_Large]
						Text opt\GraphicWidth/2,y*2,GetLocalString("Multiplayer", "wave")+" "+(mp_I\Gamemode\Phase/2),1,0
					EndIf
					Color 0,255,0
					SetFont fo\Font[Font_Digital_Medium]
					If mp_I\BossNPC <> Null Then
						TextWithAlign x,y,"X ???",2,1
					Else
						TextWithAlign x,y,"X "+mp_I\Gamemode\EnemyCount,2,1
					EndIf	
					DrawImage mp_I\Gamemode\img[0],x-100,y
					TextWithAlign x,y+60,GetLocalString("Multiplayer", "wave")+" "+(mp_I\Gamemode\Phase/2)+"/"+(mp_I\Gamemode\MaxPhase),2,1
				EndIf
				
				y = y + 120
				FusesAmount% = 0
				FusesActivatedAmount% = 0
				For fb = Each FuseBox
					FusesAmount% = FusesAmount + MaxFuseAmount
					FusesActivatedAmount = FusesActivatedAmount + fb\fuses
				Next
				If FusesActivatedAmount < FusesAmount Then
					TextWithAlign x,y,GetLocalString("Multiplayer", "fuses")+": " + FusesActivatedAmount + "/" + FusesAmount,2,1
				Else
					i = 1
					For ge = Each Generator
						TextWithAlign x,y+(60 * (i - 1)),GetLocalStringR("Multiplayer","generator",i)+": " + Int(ge\progress / GEN_CHARGE_TIME * 100.0) + "%/100%", 2, 1
						i = i + 1
					Next
				EndIf
				
				If mp_I\Gamemode\Phase = GamemodeEnd Then
					For i = 0 To (mp_I\MaxPlayers-1)
						If Players[i]<>Null Then
							maxPeopleOnTeamMTF = maxPeopleOnTeamMTF + 1
							If Players[i]\CurrHP <= 0 Then
								peopleOnTeamMTFDead = peopleOnTeamMTFDead + 1
							EndIf
						EndIf
					Next
					x = opt\GraphicWidth / 2
					y = opt\GraphicHeight / 2
					Color 255,255,255
					If maxPeopleOnTeamMTF > 0 And peopleOnTeamMTFDead = maxPeopleOnTeamMTF Then
						SetFont fo\Font[Font_Default_Large]
						Text x,y,GetLocalString("Multiplayer", "waves_gameover_1"),True,True
						SetFont fo\Font[Font_Default_Medium]
						If mp_O\OtherTeams Then
							Text x,y+50*MenuScale,GetLocalString("Multiplayer", "contact_sh_lost"),True,True
						Else
							Text x,y+50*MenuScale,GetLocalString("Multiplayer", "contact_ci_lost"),True,True
						EndIf
					Else
						SetFont fo\Font[Font_Default_Large]
						Text x,y,GetLocalString("Multiplayer","waves_win_3"),True,True
						SetFont fo\Font[Font_Default_Medium]
						Text x,y+50*MenuScale,GetLocalString("Multiplayer", "waves_win_4"),True,True
					EndIf
				EndIf
				;[End Block]
			Case Gamemode_Deathmatch
				;[Block]
				x = opt\GraphicWidth / 2
				y = opt\GraphicHeight / 2
				If mp_I\Gamemode\Phase > Deathmatch_Game Then
					Color 255,255,255
					SetFont fo\Font[Font_Default_Large]
					Select mp_I\Gamemode\Phase
						Case Deathmatch_MTFLost
							If mp_O\OtherTeams Then
								Text x,y,GetLocalString("Multiplayer", "deathmatch_sh_win"),True,True
							Else
								Text x,y,GetLocalString("Multiplayer", "deathmatch_ci_win"),True,True
							EndIf
						Case Deathmatch_CILost
							If mp_O\OtherTeams Then
								Text x,y,GetLocalString("Multiplayer", "deathmatch_rrh_win"),True,True
							Else
								Text x,y,GetLocalString("Multiplayer", "deathmatch_ntf_win"),True,True
							EndIf
						Case Deathmatch_TeamSwitch
							Text x,y,GetLocalString("Multiplayer", "switching"),True,True
							SetFont fo\Font[Font_Default_Medium]
							If Players[mp_I\PlayerID]\Team = Team_MTF Then
								If mp_O\OtherTeams Then
									Text x,y+50*MenuScale,GetLocalString("Multiplayer", "deathmatch_to_sh"),True,True
								Else
									Text x,y+50*MenuScale,GetLocalString("Multiplayer", "deathmatch_to_ci"),True,True
								EndIf
							Else
								If mp_O\OtherTeams Then
									Text x,y+50*MenuScale,GetLocalString("Multiplayer", "deathmatch_to_rrh"),True,True
								Else
									Text x,y+50*MenuScale,GetLocalString("Multiplayer", "deathmatch_to_ntf"),True,True
								EndIf
							EndIf
						Case GamemodeEnd
							If mp_I\Gamemode\RoundWins[Team_MTF-1] >= 16 Then
								If mp_O\OtherTeams Then
									Text x,y,GetLocalString("Multiplayer", "deathmatch_rrh_win"),True,True
								Else
									Text x,y,GetLocalString("Multiplayer", "deathmatch_ntf_win"),True,True
								EndIf
								SetFont fo\Font[Font_Default_Medium]
								If mp_O\OtherTeams Then
									Text x,y+50*MenuScale,GetLocalString("Multiplayer", "sh_neutralized"),True,True
								Else
									Text x,y+50*MenuScale,GetLocalString("Multiplayer", "ci_neutralized"),True,True
								EndIf
							ElseIf mp_I\Gamemode\RoundWins[Team_CI-1] >= 16 Then
								If mp_O\OtherTeams Then
									Text x,y,GetLocalString("Multiplayer", "deathmatch_sh_win"),True,True
								Else
									Text x,y,GetLocalString("Multiplayer", "deathmatch_ci_win"),True,True
								EndIf
								SetFont fo\Font[Font_Default_Medium]
								If mp_O\OtherTeams Then
									Text x,y+50*MenuScale,GetLocalString("Multiplayer", "contact_rrh_lost"),True,True
								Else
									Text x,y+50*MenuScale,GetLocalString("Multiplayer", "contact_lost"),True,True
								EndIf
							EndIf	
					End Select		
					SetFont fo\Font[Font_Default_Medium]
					If mp_O\OtherTeams Then
						Text x,100,GetLocalString("Multiplayer", "rrh")+"  "+mp_I\Gamemode\RoundWins[Team_MTF-1]+"  -  "+mp_I\Gamemode\RoundWins[Team_CI-1]+"  "+GetLocalString("Multiplayer", "sh"),True,True
					Else
						Text x,100,GetLocalString("Multiplayer", "ntf")+"  "+mp_I\Gamemode\RoundWins[Team_MTF-1]+"  -  "+mp_I\Gamemode\RoundWins[Team_CI-1]+"  "+GetLocalString("Multiplayer", "ci"),True,True
					EndIf
				EndIf
				x = opt\GraphicWidth - 50
				y = 50
				If mp_I\Gamemode\PhaseTimer > 0 Then
					Color 0,255,0
					SetFont fo\Font[Font_Digital_Medium]
					actualtime = mp_I\Gamemode\PhaseTimer
					If mp_I\Gamemode\Phase = GamemodeEnd Then
						If (Not IsInVote()) Then
							actualtime = mp_I\Gamemode\PhaseTimer - GamemodeEndTimeTotal + GamemodeEndTime
						ElseIf (Not IsVoteCompleted())
							actualtime = mp_I\Gamemode\PhaseTimer - GamemodeEndTimeAfterVote
						EndIf
					EndIf
					If TurnIntoSeconds(actualtime) < 10 Then
						TextWithAlign x,y,"0:0"+TurnIntoSeconds(actualtime),2,0
					Else
						TextWithAlign x,y,"0:"+TurnIntoSeconds(actualtime),2,0
					EndIf
					If mp_I\Gamemode\Phase = Deathmatch_Game Then
						Color 0,Min(mp_I\Gamemode\PhaseTimer,127.5)*2,0
						SetFont fo\Font[Font_Digital_Large]
						Text opt\GraphicWidth/2,y*2,GetLocalString("Multiplayer", "round")+" "+(mp_I\Gamemode\RoundWins[Team_MTF-1]+mp_I\Gamemode\RoundWins[Team_CI-1]+1),1,0
					EndIf	
				EndIf
				;[End Block]
		End Select
		
		If DebugHUD Then
			Color 255, 255, 255
			SetFont fo\ConsoleFont
			
			Text 50, 20, "Delta time: "+ft\DeltaTime
			Text 50, 50, "Player Position: (" + f2s(EntityX(Players[mp_I\PlayerID]\Collider), 3) + ", " + f2s(EntityY(Players[mp_I\PlayerID]\Collider), 3) + ", " + f2s(EntityZ(Players[mp_I\PlayerID]\Collider), 3) + ")"
			Text 50, 70, "Camera Position: (" + f2s(EntityX(mpl\CameraPivot), 3) + ", " + f2s(EntityY(mpl\CameraPivot), 3) + ", " + f2s(EntityZ(mpl\CameraPivot), 3) + ")"
			Text 50, 100, "Player Rotation: (" + f2s(EntityPitch(Players[mp_I\PlayerID]\Collider), 3) + ", " + f2s(EntityYaw(Players[mp_I\PlayerID]\Collider), 3) + ", " + f2s(EntityRoll(Players[mp_I\PlayerID]\Collider), 3) + ")"
			Text 50, 120, "Camera Rotation: (" + f2s(EntityPitch(mpl\CameraPivot), 3) + ", " + f2s(EntityYaw(mpl\CameraPivot), 3) +", " + f2s(EntityRoll(mpl\CameraPivot), 3) + ")"
			
			Text 50, 300, "Stamina: " + f2s(Players[mp_I\PlayerID]\CurrStamina, 3)
			
			Text 400, 100, "Triangles rendered: "+CurrTrisAmount
			Text 400, 120, "Active textures: "+ActiveTextures()
			
			SetFont fo\Font[Font_Default]
		EndIf
	EndIf
	
	Color 255, 255, 255
	If AttachmentMenuOpen Then
		RenderAttachments()
	EndIf
	
	RenderCommunicationAndSocialWheel()
	
	DrawVoting()
	
	DrawPlayerList()
	
	CatchErrors("Uncaught (DrawGUIMP)")
End Function

Function UpdateGUIMP()
	Local i%,g.Guns
	Local plAmount%
	
	If (Not AttachmentMenuOpen) Then
		If Players[mp_I\PlayerID]\CurrHP > 0.0 Then
			For i = 0 To MaxSlots-1
				If KeyHit(i+2) Then
					If (Not InLobby()) And (Not AttachmentMenuOpen) And (Not ConsoleOpen) And (Not mp_I\ChatOpen) Then
						If Players[mp_I\PlayerID]\WeaponInSlot[i]<>GUN_UNARMED Then
							If i<>Players[mp_I\PlayerID]\SelectedSlot Then
								If mp_I\PlayState = GAME_SERVER Then
									Players[mp_I\PlayerID]\SelectedSlot = i
									g_I\GunChangeFLAG = False
								Else
									Players[mp_I\PlayerID]\WantsSlot = i
								EndIf
								mpl\SlotsDisplayTimer = 70*3
							EndIf
						EndIf
					EndIf
				EndIf
			Next
			For g = Each Guns
				If g_I\HoldingGun <> g\ID Then
					If g\IsSeparate Then
						HideEntity g\HandsObj
					EndIf
				EndIf
			Next
			UpdateCommunicationAndSocialWheel()
		Else
			If (Not EntityHidden(mpl\WheelSprite)) Then
				UpdateCommunicationAndSocialWheel()
			EndIf
		EndIf
	EndIf
	mpl\SlotsDisplayTimer = Max(mpl\SlotsDisplayTimer-FPSfactor,0.0)
	
	UpdateVoting()
	
	UpdatePlayerList()
	
	If (((Not mp_I\ChatOpen) And InteractHit(1,CK_Pause)) Lor ((Not InFocus()) And (Not MenuOpen)) Lor (Steam_GetOverlayUpdated() = 1 And (Not MenuOpen))) And EndingTimer = 0 And (Not InLobby()) And (Not IsModerationOpen()) Then
		If MenuOpen Then
			If OptionsMenu <> 0 Then SaveOptionsINI()
			DeleteMenuGadgets()
			ResetInput()
		EndIf
		MenuOpen = (Not MenuOpen)
		
		AchievementsMenu = 0
		OptionsMenu = 0
		QuitMSG = 0
		mp_I\ChatOpen = False
	EndIf
	
End Function

Function DrawMPGunsInHud(playerid%)
	Local g.Guns
	Local x# = 50, x2# = 150
	Local y# = 50
	Local width#=64,height#=64
	
	Local width2%
	Local i%
	width# = 204
	height# = 20
	
	x# = opt\GraphicWidth - 60
	y# = opt\GraphicHeight - 55
	
	For g = Each Guns
		
		If g\ID = Players[playerid]\WeaponInSlot[Players[playerid]\SelectedSlot] Then
			If (g\GunType <> GUNTYPE_MELEE) Then
				
				If g\MaxJams > 0 Then
					
					Color 255,0,0
					Rect(x - 50 - 1 - 30, y - 80, Float(g\JamAmount * (width - 6) / (g\MaxJams*1.6)), 15, True)
					
					If g\JamAmount < g\MaxJams Then
						Color 255,255,255
						TextWithAlign x - 40, y - 110, GetLocalString("Weapons","heat")
					Else
						Color 255,0,0
						TextWithAlign x - 70, y - 110, GetLocalString("Weapons","overheat")
					EndIf
					Rect(x - 50 - 1 - 30, y - 81, 125, 15 + 2, False)
				EndIf
				
				Color 255,255,255
				
				If g\GunType <> GUNTYPE_HANDGUN And g\GunType <> GUNTYPE_SHOTGUN Then
					If g\FireMode = 0 Then
						DrawImage WeaponFireModeIcons[0], x - 50 - 30, y - 50
						TextWithAlign x - 40, y - 45, GetLocalString("Weapons","auto")
					ElseIf g\FireMode = 1 Then
						DrawImage WeaponFireModeIcons[1], x - 50 - 30, y - 50
						TextWithAlign x - 40, y - 45, GetLocalString("Weapons","semi")
					EndIf
				ElseIf g\GunType = GUNTYPE_SHOTGUN
					If g_I\HoldingGun = GUN_SPAS12 Then
						If g\FireMode = 0 Then
							DrawImage WeaponFireModeIcons[0], x - 50 - 30, y - 50
							TextWithAlign x - 40, y - 45, GetLocalString("Weapons","auto")
						ElseIf g\FireMode = 1 Then
							DrawImage WeaponFireModeIcons[1], x - 50 - 30, y - 50
							TextWithAlign x - 40, y - 45, GetLocalString("Weapons","pump")
						EndIf
					Else
						If g\FireMode = 0 Then
							DrawImage WeaponFireModeIcons[0], x - 50 - 30, y - 50
							TextWithAlign x - 40, y - 45, GetLocalString("Weapons","semi")
						EndIf
					EndIf
				Else
					If g\FireMode = 0 Then
						DrawImage WeaponFireModeIcons[0], x - 50 - 30, y - 50
						TextWithAlign x - 40, y - 45, GetLocalString("Weapons","semi")
					EndIf
				EndIf
				
				If Players[playerid]\Ammo[Players[playerid]\SelectedSlot] > 0 Then
					Color 255,255,255
				Else
					Color 255,0,0
				EndIf
				Rect(x - 50 - 1 - 30, y - 1, 30 + 2, 30 + 2, False)
				DrawImage BulletIcon, x - 50 - 30, y
				
				SetFont fo\Font%[Font_Digital_Medium]
				If Players[playerid]\Ammo[Players[playerid]\SelectedSlot] > g\MaxCurrAmmo/5
					Color 0,255,0
				Else
					Color 255,0,0
				EndIf
				TextWithAlign x,y + 5,Players[playerid]\Ammo[Players[playerid]\SelectedSlot],2
				Color 0,255,0
				Text x,y + 5,"/"
				width2% = StringWidth("/")
				If Players[playerid]\ReloadAmmo[Players[playerid]\SelectedSlot] > 0
					Color 0,255,0
				Else
					Color 255,0,0
				EndIf
				Text x+width2,y + 5,Players[playerid]\ReloadAmmo[Players[playerid]\SelectedSlot]
			EndIf
			Exit
		EndIf
	Next
	
	Color 255,255,255
	
End Function

Function DrawMPMenu()
	CatchErrors("Uncaught (DrawMenu)")
	
	Local x%, y%, width%, height%
	Local i%
	
	If MenuOpen Then
		CameraProjMode Camera, 0
		CameraProjMode m_I\Cam, 1
		PositionEntity m_I\Cam,0,-1000,0
		ShowEntity m_I\MenuLogo\logo
		ShowEntity m_I\MenuLogo\gradient
		RenderWorld
		CameraProjMode Camera, 1
		CameraProjMode m_I\Cam, 0
		HideEntity m_I\MenuLogo\logo
		HideEntity m_I\MenuLogo\gradient
		
		Color 255, 255, 255
		SetFont fo\ConsoleFont
		Text 20, opt\GraphicHeight-70, "Username: "+mp_O\PlayerName
		Text 20, opt\GraphicHeight-50, "Server Name: "+mp_I\ServerName
		Text 20, opt\GraphicHeight-30, "v"+VersionNumber
		
		RenderMainMenu()
	EndIf
	
	SetFont fo\Font[Font_Default]
	
	CatchErrors("DrawMenu")
End Function

Function UpdateMPMenu()
	CatchErrors("Uncaught (UpdateMenu)")
	
	If MenuOpen
		ShowEntity m_I\Sprite
		UpdateMainMenu()
	Else
		HideEntity m_I\Sprite
	EndIf
	
	CatchErrors("UpdateMenu")
End Function

Type PlayerList
	Field IsOpen%
	Field Icons%
	Field Moderation%
	Field Reason$
End Type

Function LoadPlayerList()
	
	pll = New PlayerList
	pll\Icons = LoadAnimImage("GFX\menu\Multiplayer\playerlist_icons.png", 30, 30, 0, 5)
	ResizeImage(pll\Icons, 30 * MenuScale, 30 * MenuScale)
	pll\Moderation = -1
	
End Function

Function UnLoadPlayerList()
	
	Delete pll
	
End Function

Function DrawPlayerList()
	Local x%, y%, width%, height%
	Local i%, j%, k%, temp$, tempheight%, lastindex%, previndex%, isfriend%
	Local drawfriendbutton%, drawmodbutton%
	
	If IsPlayerListOpen() Then
		Color 255,255,255
		
		width = opt\GraphicHeight / 1.125
		height = 40.0 * MenuScale
		
		x = (opt\GraphicWidth / 2) - (width / 2)
		y = height * 4
		If mp_I\Gamemode\ID <> Gamemode_Deathmatch Then
			y = y + height * 4
		EndIf
		
		DrawFrame(x, y, width, height * 1.5)
		SetFont fo\Font[Font_Default_Medium]
		Text(opt\GraphicWidth / 2, y + (height * 1.5 / 2), mp_I\Gamemode\name + " - " + mp_I\MapInList\Name, True, True)
		
		For i = 0 To ((MaxPlayerTeams - 1) And (mp_I\Gamemode\ID = Gamemode_Deathmatch))
			SetFont fo\Font[Font_Default_Medium]
			y = y + height * 2
			DrawFrame(x, y, width, height * 1.5)
			If mp_O\OtherTeams Then
				If i = 0 Then
					If mp_I\Gamemode\ID = Gamemode_Deathmatch Then
						temp = GetLocalString("Multiplayer", "rrh")
					ElseIf mp_I\Gamemode\ID = Gamemode_EAF Then
						temp = GetLocalString("Multiplayer", "sh")
					Else
						temp = GetLocalString("Multiplayer", "sne")
					EndIf
				Else
					temp = GetLocalString("Multiplayer", "sh")
				EndIf
			Else
				If i = 0 Then
					If mp_I\Gamemode\ID = Gamemode_EAF Then
						temp = GetLocalString("Multiplayer", "ci")
					Else
						temp = GetLocalString("Multiplayer", "ntf")
					EndIf
				Else
					temp = GetLocalString("Multiplayer", "ci")
				EndIf
			EndIf
			Text(opt\GraphicWidth / 2, y + (height * 1.5 / 2), temp + " - " + mp_I\Gamemode\RoundWins[i], True, True)
			
			SetFont fo\Font[Font_Default]
			y = y + height * 1.5
			lastindex = -1
			For j = 0 To 6
				If j > 0 And lastindex > -2 Then
					previndex = lastindex
					For k = (lastindex + 1) To (mp_I\MaxPlayers - 1)
						If Players[k] <> Null Then
							If Players[k]\Team = (i+1) Then
								lastindex = k
								Exit
							EndIf
						EndIf
					Next
					If previndex = lastindex Then
						lastindex = -2
					EndIf
				EndIf
				
				tempheight = height * (1.0 - 0.4 * (j = 0))
				
				;width: 90 + 90 + 110 + 70 + 100 = 470
				DrawFrame(x, y, width - 470.0 * MenuScale, tempheight)
				If lastindex = mp_I\PlayerID Then
					Color 40,40,40
					Rect x + 2.0 * MenuScale, y + 2.0 * MenuScale, (width - 470.0 * MenuScale) - 4.0 * MenuScale, tempheight - 4.0 * MenuScale, True
					Color 255,255,255
				EndIf
				If j = 0 Then
					temp = GetLocalString("Multiplayer", "pl_players")
				ElseIf lastindex >= 0 Then
					temp = Players[lastindex]\Name
					If Players[lastindex]\CurrHP <= 0 Then
						DrawImage(pll\Icons, x + (width - 470.0 * MenuScale) - (5.0 * MenuScale) - (ImageWidth(pll\Icons)), y + 5.0 * MenuScale, 4)
						Color 100,100,100
					EndIf
				Else
					temp = ""
				EndIf
				Text(x + ((10.0 * MenuScale) * (j > 0)) + (((width - 470.0 * MenuScale) / 2) * (j = 0)), y + tempheight / 2, temp, (j = 0), True)
				x = x + (width - 470.0 * MenuScale)
				
				For k = 0 To 1
					DrawFrame(x, y, 90.0 * MenuScale, tempheight)
					If lastindex = mp_I\PlayerID Then
						Color 40,40,40
						Rect x + 2.0 * MenuScale, y + 2.0 * MenuScale, (90.0 * MenuScale) - 4.0 * MenuScale, tempheight - 4.0 * MenuScale, True
						Color 255,255,255
					EndIf
					If k = 0 Then
						If j = 0 Then
							temp = GetLocalString("Multiplayer", "pl_kills")
						ElseIf lastindex >= 0 Then
							temp = Players[lastindex]\Kills
						Else
							temp = ""
						EndIf
					Else
						If j = 0 Then
							temp = GetLocalString("Multiplayer", "pl_deaths")
						ElseIf lastindex >= 0 Then
							temp = Players[lastindex]\Deaths
						Else
							temp = ""
						EndIf
					EndIf
					Text(x + (90.0 * MenuScale / 2), y + tempheight / 2, temp, True, True)
					x = x + 90.0 * MenuScale
				Next
				
				DrawFrame(x, y, 110.0 * MenuScale, tempheight)
				If lastindex = mp_I\PlayerID Then
					Color 40,40,40
					Rect x + 2.0 * MenuScale, y + 2.0 * MenuScale, (110.0 * MenuScale) - 4.0 * MenuScale, tempheight - 4.0 * MenuScale, True
					Color 255,255,255
				EndIf
				If j = 0 Then
					temp = GetLocalString("Multiplayer", "pl_score")
				ElseIf lastindex >= 0 Then
					temp = Str(Int(500.0 * (Float(Players[lastindex]\Kills) / Float(Max(Players[lastindex]\Deaths, 1)))))
				Else
					temp = ""
				EndIf
				Text(x + (110.0 * MenuScale / 2), y + tempheight / 2, temp, True, True)
				x = x + 110.0 * MenuScale
				
				DrawFrame(x, y, 70.0 * MenuScale, tempheight)
				If lastindex = mp_I\PlayerID Then
					Color 40,40,40
					Rect x + 2.0 * MenuScale, y + 2.0 * MenuScale, (70.0 * MenuScale) - 4.0 * MenuScale, tempheight - 4.0 * MenuScale, True
					Color 255,255,255
				EndIf
				If j = 0 Then
					temp = GetLocalString("Multiplayer", "pl_ping")
				ElseIf lastindex >= 0 Then
					temp = Players[lastindex]\Ping
				Else
					temp = ""
				EndIf
				Text(x + (70.0 * MenuScale / 2), y + tempheight / 2, temp, True, True)
				x = x + 70.0 * MenuScale
				
				DrawFrame(x, y, 110.0 * MenuScale, tempheight)
				If lastindex = mp_I\PlayerID Then
					Color 40,40,40
					Rect x + 2.0 * MenuScale, y + 2.0 * MenuScale, (110.0 * MenuScale) - 4.0 * MenuScale, tempheight - 4.0 * MenuScale, True
					Color 255,255,255
				EndIf
				
				drawfriendbutton = True
				drawmodbutton = True
				If lastindex >= 0 And lastindex <> mp_I\PlayerID Then
					If mp_I\PlayState = GAME_CLIENT Then
						drawmodbutton = False
						x = x + (2 * MenuScale) + (ImageWidth(pll\Icons) / 2)
					EndIf
					
					isfriend = Steam_GetFriendRelationship(Players[lastindex]\SteamIDUpper, Players[lastindex]\SteamIDLower)
					If isfriend <> k_EFriendRelationshipNone And isfriend <> k_EFriendRelationshipBlocked And isfriend <> k_EFriendRelationshipIgnoredFriend Then
						drawfriendbutton = False
						x = x + (2 * MenuScale) + (ImageWidth(pll\Icons) / 2)
					EndIf
					
					DrawImage pll\Icons, x + 5.0 * MenuScale, y + 5.0 * MenuScale, Players[lastindex]\Ignored
					If MouseOn(x + 5.0 * MenuScale, y + 5.0 * MenuScale, ImageWidth(pll\Icons), ImageHeight(pll\Icons)) And (pll\Moderation < 0) Then
						Color 150,150,150
					Else
						Color 255,255,255
					EndIf
					Rect x + 5.0 * MenuScale, y + 5.0 * MenuScale, ImageWidth(pll\Icons), ImageHeight(pll\Icons), False
					
					If drawfriendbutton Then
						x = x + 5.0 * MenuScale + ImageWidth(pll\Icons)
						DrawImage pll\Icons, x + 5.0 * MenuScale, y + 5.0 * MenuScale, 2
						If MouseOn(x + 5.0 * MenuScale, y + 5.0 * MenuScale, ImageWidth(pll\Icons), ImageHeight(pll\Icons)) And (pll\Moderation < 0) Then
							Color 150,150,150
						Else
							Color 255,255,255
						EndIf
						Rect x + 5.0 * MenuScale, y + 5.0 * MenuScale, ImageWidth(pll\Icons), ImageHeight(pll\Icons), False
					EndIf
					
					If drawmodbutton Then
						x = x + 5.0 * MenuScale + ImageWidth(pll\Icons)
						DrawImage pll\Icons, x + 5.0 * MenuScale, y + 5.0 * MenuScale, 3
						If MouseOn(x + 5.0 * MenuScale, y + 5.0 * MenuScale, ImageWidth(pll\Icons), ImageHeight(pll\Icons)) And (pll\Moderation < 0) Then
							Color 150,150,150
						Else
							Color 255,255,255
						EndIf
						Rect x + 5.0 * MenuScale, y + 5.0 * MenuScale, ImageWidth(pll\Icons), ImageHeight(pll\Icons), False
					EndIf
				EndIf
				
				If j < 6 Then
					y = y + tempheight
				EndIf
				x = (opt\GraphicWidth / 2) - (width / 2)
			Next
		Next
		
		If (pll\Moderation >= 0) Then
			width = opt\GraphicHeight / 1.3
			height = 150.0 * MenuScale
			x = (opt\GraphicWidth / 2) - (width / 2)
			y = (opt\GraphicHeight / 2) - (height / 2)
			
			DrawFrame(x, y, width, height)
			
			Color 255,255,255
			SetFont(fo\Font[Font_Default_Medium])
			Text(opt\GraphicWidth / 2, y + 15.0 * MenuScale, GetLocalString("Multiplayer", "kick")+"/"+GetLocalString("Multiplayer", "ban")+" "+ Players[pll\Moderation]\Name + "?", True)
			
			SetFont(fo\Font[Font_Default])
			Text(x + 15.0 * MenuScale, y + (height / 2) - 25.0 * MenuScale, GetLocalString("Multiplayer", "reason")+":")
		EndIf
	EndIf
	
	DrawAllMenuButtons()
	DrawAllMenuInputBoxes()
	
End Function

Function UpdatePlayerList()
	Local x%, y%, width%, height%
	Local i%, j%, k%, tempheight%, lastindex%, previndex%, isfriend%
	Local drawfriendbutton%, drawmodbutton%
	
	If KeyHit(KEY_INV) Then
		pll\IsOpen = Not pll\IsOpen
		pll\Moderation = -1
		pll\Reason = ""
		mp_I\ChatOpen = False
		ShouldDeleteGadgets = True
		DeleteMenuGadgets()
		ResetInput()
	EndIf
	
	If (MenuOpen Lor InLobby()) And (pll\IsOpen) Then
		pll\IsOpen = False
		pll\Moderation = -1
		pll\Reason = ""
		ShouldDeleteGadgets = True
		DeleteMenuGadgets()
	EndIf
	
	If IsPlayerListOpen() Then
		Color 255,255,255
		
		width = opt\GraphicHeight / 1.125
		height = 40.0 * MenuScale
		
		x = (opt\GraphicWidth / 2) - (width / 2)
		y = height * 4
		If mp_I\Gamemode\ID <> Gamemode_Deathmatch Then
			y = y + height * 4
		EndIf
		
		For i = 0 To ((MaxPlayerTeams - 1) And (mp_I\Gamemode\ID = Gamemode_Deathmatch))
			y = y + height * 2
			
			y = y + height * 1.5
			lastindex = -1
			For j = 0 To 6
				If j > 0 And lastindex > -2 Then
					previndex = lastindex
					For k = (lastindex + 1) To (mp_I\MaxPlayers - 1)
						If Players[k] <> Null Then
							If Players[k]\Team = (i+1) Then
								lastindex = k
								Exit
							EndIf
						EndIf
					Next
					If previndex = lastindex Then
						lastindex = -2
					EndIf
				EndIf
				
				tempheight = height * (1.0 - 0.4 * (j = 0))
				
				x = x + (width - 470.0 * MenuScale) + (90.0 * MenuScale * 2) + (110.0 * MenuScale) + (70.0 * MenuScale)
				
				drawfriendbutton = True
				drawmodbutton = True
				If lastindex >= 0 And lastindex <> mp_I\PlayerID Then
					If mp_I\PlayState = GAME_CLIENT Then
						drawmodbutton = False
						x = x + (2 * MenuScale) + (ImageWidth(pll\Icons) / 2)
					EndIf
					
					isfriend = Steam_GetFriendRelationship(Players[lastindex]\SteamIDUpper, Players[lastindex]\SteamIDLower)
					If isfriend <> k_EFriendRelationshipNone And isfriend <> k_EFriendRelationshipBlocked And isfriend <> k_EFriendRelationshipIgnoredFriend Then
						drawfriendbutton = False
						x = x + (2 * MenuScale) + (ImageWidth(pll\Icons) / 2)
					EndIf
					
					If MouseHit1 Then
						If MouseOn(x + 5.0 * MenuScale, y + 5.0 * MenuScale, ImageWidth(pll\Icons), ImageHeight(pll\Icons)) And (pll\Moderation < 0) Then
							PlaySound_Strict ButtonSFX[0]
							Players[lastindex]\Ignored = Not Players[lastindex]\Ignored
						EndIf
					EndIf
					
					If drawfriendbutton Then
						x = x + 5.0 * MenuScale + ImageWidth(pll\Icons)
						If MouseHit1 Then
							If MouseOn(x + 5.0 * MenuScale, y + 5.0 * MenuScale, ImageWidth(pll\Icons), ImageHeight(pll\Icons)) And (pll\Moderation < 0) Then
								PlaySound_Strict ButtonSFX[0]
								Steam_ActivateOverlayToUser("friendadd", Players[lastindex]\SteamIDUpper, Players[lastindex]\SteamIDLower)
							EndIf
						EndIf
					EndIf
					
					If drawmodbutton Then
						x = x + 5.0 * MenuScale + ImageWidth(pll\Icons)
						If MouseHit1 Then
							If MouseOn(x + 5.0 * MenuScale, y + 5.0 * MenuScale, ImageWidth(pll\Icons), ImageHeight(pll\Icons)) And (pll\Moderation < 0) Then
								PlaySound_Strict ButtonSFX[0]
								pll\Moderation = lastindex
							EndIf
						EndIf
					EndIf
				EndIf
				
				If j < 6 Then
					y = y + tempheight
				EndIf
				x = (opt\GraphicWidth / 2) - (width / 2)
			Next
		Next
		
		If (pll\Moderation >= 0) Then
			width = opt\GraphicHeight / 1.3
			height = 150.0 * MenuScale
			x = (opt\GraphicWidth / 2) - (width / 2)
			y = (opt\GraphicHeight / 2) - (height / 2)
			
			pll\Reason = InputBox(x + 15.0 * MenuScale, y + (height / 2) - 10.0 * MenuScale, width - 30.0 * MenuScale, 20.0 * MenuScale, pll\Reason)
			
			If DrawButton(x + width - 45.0 * MenuScale, y + 15.0 * MenuScale, 30.0 * MenuScale, 30.0 * MenuScale, "X", False) Then
				pll\Moderation = -1
			EndIf
			
			If DrawButton(x + (width / 2) - 200.0 * MenuScale, y + height - 45.0 * MenuScale, 150.0 * MenuScale, 30.0 * MenuScale, GetLocalString("Multiplayer", "kick"), False) Then
				Steam_PushByte(PACKET_KICK)
				Steam_PushByte(SERVER_MSG_KICK_KICKED)
				Steam_PushString(pll\Reason)
				Steam_SendPacketToUser(Players[pll\Moderation]\SteamIDUpper, Players[pll\Moderation]\SteamIDLower, k_EP2PSendUnreliable)
				DeletePlayerAsServer(Steam_GetPlayerIDUpper(), Steam_GetPlayerIDLower(), pll\Moderation, "user_kicked")
				pll\Moderation = -1
			EndIf
			
			If DrawButton(x + (width / 2) + 50.0 * MenuScale, y + height - 45.0 * MenuScale, 150.0 * MenuScale, 30.0 * MenuScale, GetLocalString("Multiplayer", "ban"), False) Then
				Steam_PushByte(PACKET_KICK)
				Steam_PushByte(SERVER_MSG_KICK_BANNED)
				Steam_PushString(pll\Reason)
				Steam_SendPacketToUser(Players[pll\Moderation]\SteamIDUpper, Players[pll\Moderation]\SteamIDLower, k_EP2PSendUnreliable)
				AddBan(Players[pll\Moderation]\SteamIDUpper, Players[pll\Moderation]\SteamIDLower, pll\Reason)
				DeletePlayerAsServer(Steam_GetPlayerIDUpper(), Steam_GetPlayerIDLower(), pll\Moderation, "user_banned")
				pll\Moderation = -1
			EndIf
			
			If pll\Moderation = -1 Then
				ShouldDeleteGadgets = True
				DeleteMenuGadgets()
			EndIf
		EndIf
	EndIf
	
End Function

Function IsPlayerListOpen%()
	
	If (pll = Null) Then
		Return False
	EndIf
	Return pll\IsOpen
	
End Function

Function IsModerationOpen%()
	
	If (pll = Null) Then
		Return False
	EndIf
	Return (pll\Moderation >= 0)
	
End Function

Function UpdateVoting()
	Local x%, y%, width%, height%
	Local mfl.MapForList
	Local i%, found%
	
	If (Not IsInVote()) Then
		Return
	EndIf
	
	If (Not IsVoteCompleted()) Then
		width = ImageWidth(mp_I\NoMapImage) * 3 + 60 * MenuScale
		height = ImageHeight(mp_I\NoMapImage) + 110 * MenuScale
		x = opt\GraphicWidth / 2 - width / 2
		y = opt\GraphicHeight / 2 - height / 2
		
		x = x + 20 * MenuScale
		For i = 0 To (MAX_VOTED_MAPS - 1)
			found = False
			For mfl = Each MapForList
				If mfl\Name = mp_I\MapsToVote[i] Then
					found = True
					Exit
				EndIf
			Next
			If MouseOn(x, y + 70 * MenuScale, ImageWidth(mp_I\NoMapImage), ImageHeight(mp_I\NoMapImage)) And MouseHit1 Then
				If found Then
					Players[mp_I\PlayerID]\MapVote = (i + 1)
					PlaySound_Strict(ButtonSFX[0])
				Else
					PlaySound_Strict(ButtonSFx[1])
				EndIf
				Exit
			EndIf
			
			x = x + ImageWidth(mp_I\NoMapImage) + 10 * MenuScale
		Next
	EndIf
	
End Function

Function DrawVoting()
	Local x%, y%, width%, height%
	Local mfl.MapForList
	Local i%, j%, found%, playerCount%, drawRect%
	
	If (Not IsInVote()) Then
		Return
	EndIf
	
	width = ImageWidth(mp_I\NoMapImage) * 3 + 60 * MenuScale
	height = ImageHeight(mp_I\NoMapImage) + 110 * MenuScale
	x = opt\GraphicWidth / 2 - width / 2
	y = opt\GraphicHeight / 2 - height / 2
	
	DrawFrame(x, y, width, height)
	
	SetFont fo\Font[Font_Default_Medium]
	Text opt\GraphicWidth / 2, y + 20 * MenuScale, Upper(GetLocalString("Multiplayer", "vote")), True
	
	x = x + 20 * MenuScale
	For i = 0 To (MAX_VOTED_MAPS - 1)
		drawRect = False
		If IsVoteCompleted() And i = (mp_I\VotedMap - 1) Then
			Color 0, 255, 0
			drawRect = True
		ElseIf i = (Players[mp_I\PlayerID]\MapVote - 1) Then
			Color 255, 255, 255
			drawRect = True
		ElseIf MouseOn(x, y + 70 * MenuScale, ImageWidth(mp_I\NoMapImage), ImageHeight(mp_I\NoMapImage)) And (Not IsVoteCompleted()) Then
			Color 100, 100, 100
			drawRect = True
		EndIf
		
		If drawRect Then
			Rect x - FRAME_THICK * MenuScale, y + 70 * MenuScale - FRAME_THICK * MenuScale, ImageWidth(mp_I\NoMapImage) + 2 * FRAME_THICK * MenuScale, ImageHeight(mp_I\NoMapImage) + 2 * FRAME_THICK * MenuScale, True
		EndIf
		Color 255, 255, 255
		
		found = False
		For mfl = Each MapForList
			If mfl\Name = mp_I\MapsToVote[i] Then
				found = True
				DrawBlock mfl\Image, x, y + 70 * MenuScale
				SetFont fo\Font[Font_Default]
				Text x + ImageWidth(mfl\Image) / 2, y + 50 * MenuScale, mfl\Name, True
				playerCount = 0
				For j = 0 To (mp_I\MaxPlayers - 1)
					If Players[j] <> Null Then
						If Players[j]\MapVote = (i + 1) Then
							playerCount = playerCount + 1
						EndIf
					EndIf
				Next
				SetFont fo\Font[Font_Default_Medium]
				Text x + ImageWidth(mfl\Image) / 2, y + height - 30 * MenuScale, playerCount, True
				Exit
			EndIf
		Next
		If (Not found) Then
			DrawBlock mp_I\NoMapImage, x, y + 70 * MenuScale
		EndIf
		
		x = x + ImageWidth(mp_I\NoMapImage) + 10 * MenuScale
	Next
	
End Function

Function IsInVote%()
	
	If mp_I\Gamemode\Phase = GamemodeEnd And mp_I\Gamemode\PhaseTimer <= GamemodeEndTimeTotal - GamemodeEndTime Then
		Return True
	EndIf
	Return False
	
End Function

Function IsVoteCompleted%()
	
	If IsInVote() And mp_I\Gamemode\PhaseTimer <= GamemodeEndTimeAfterVote Then
		Return True
	EndIf
	Return False
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS