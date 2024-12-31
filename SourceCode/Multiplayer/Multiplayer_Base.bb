
;General constants
Const GAME_SERVER = 1
Const GAME_CLIENT = 2

;Packet constants
Const PACKET_PING = 0
Const PACKET_PLAYER = 1
Const PACKET_ITEM = 2
Const PACKET_NPC = 3
Const PACKET_EFFECT = 4
Const PACKET_QUIT = 5
Const PACKET_LOAD = 6
Const PACKET_KICK = 7
Const PACKET_AUTHORIZE = 8
Const PACKET_CHATMSG = 9
Const PACKET_GAMEMODEUTIL = 10
Const PACKET_RELOAD = 11
Const PACKET_KEY = 12

;Server message IDs for the server list
Const SERVER_MSG_NONE = 0
Const SERVER_MSG_CONNECT = 1
Const SERVER_MSG_OFFLINE = 2
Const SERVER_MSG_RETRIES = 3
Const SERVER_MSG_PASSWORD = 4
Const SERVER_MSG_PWAIT = 5
Const SERVER_MSG_QUIT = 6
Const SERVER_MSG_TIMEOUT = 7
Const SERVER_MSG_KICK_MANYPLAYERS = 8
Const SERVER_MSG_KICK_PASSWORD = 9
Const SERVER_MSG_KICK_ENCRYPTION = 10
Const SERVER_MSG_KICK_VERSION = 11
Const SERVER_MSG_KICK_KICKED = 12
Const SERVER_MSG_KICK_BANNED = 13

;Server check values
Const SERVER_CHECK_POSITION# = 1.0

;Other constants
Const BANLIST_SEPARATOR$ = "	"
Const MAX_RETRIES% = 4
Const SERVER_LIST_SORT_CATEGORY_MAX% = 4
Const SERVER_LIST_SORT_NAME% = 0
Const SERVER_LIST_SORT_GAMEMODE% = 2
Const SERVER_LIST_SORT_MAP% = 4
Const SERVER_LIST_SORT_PLAYERS% = 6
Const MAX_VOTED_MAPS% = 3

Type MultiplayerInstance
	Field PlayState%
	Field PlayerCount% ;Needed?
	Field PlayerID%
	Field IsReady%
	Field ReadyTimer#
	Field CountdownSec%
	Field TimeOut%
	Field LastPingMillisecs%
	Field PingTimer#
	Field MaxPlayers%
	Field CurrChatMSG$
	Field SendChatMSG$
	Field ShouldSendMSG%
	Field ChatMSGID%
	Field PlayerModel_Lower[MaxPlayerTeams]
	Field PlayerModel_Upper[MaxPlayerTeams]
	Field Map.MultiplayerMap
	Field MapInList.MapForList
	Field Gamemode.MultiplayerGameMode
	Field ZombieModel[MaxZombieTypes]
	Field GuardZombieModel[MaxGuardZombieTypes]
	Field SCP939Model
	Field SCP1048aModel
	Field TentacleModel
	Field BossModel
	Field HardcoreMP%
	Field OtherTeams%
	Field CurrSync% ;Needed?
	Field ConnectAddress$
	Field ConnectPort%
	Field HasRefreshed%
	Field ServerName$
	Field ServerListPage%
	Field ServerListAmount%
	Field SelectedListServer%
	Field ServerListSort%
	Field CurrNPCID% ;Needed?
	Field Offline%
	Field SpectatePlayer%
	Field PositionSyncTimer#
	Field ServerIcon%
	Field ConnectPassword$
	Field PasswordVisible%
	Field PasswordIcon%
	Field BossNPC.NPCs
	Field MaxBossHealth%
	Field DeathChunk%
	Field MuzzleFlash%
	Field TickIcon%
	Field ServerMSG%
	Field KickReason$
	Field ResetGame%
	Field ChatOpen%
	Field ServerListUpdateTimer#
	Field ConnectionIcons%
	Field ConnectionTime#
	Field ConnectionRetries%
	Field LocalAmmo%
	Field MapsToVote$[MAX_VOTED_MAPS]
	Field VotedMap%
	Field NoMapImage%
End Type

;Unused Field variables in the MultiplayerInstance Type
;[Block]
;Field Server%
;Field Stream%
;Field PlayerName$
;Field ServerPort%
;Field CurrLoadPercent%
;Field SprayIMGPath$
;Field EnableSprays%
;Field LocalServer%
;Field PingTimer#
;[End Block]

Type MultiplayerOptions
	Field PlayerName$
	Field MaxPlayers%
	Field TimeOut%
	Field LocalServer%
	Field ServerName$
	Field Password$
	Field MapInList.MapForList
	Field Gamemode.MultiplayerGameMode
	Field BanFile$
	Field HardcoreMP%
	Field OtherTeams%
End Type

Type SharedSecrets
	Field SteamIDLower%
	Field SteamIDUpper%
	Field Secret$
End Type

Function CreateSharedSecret$(IDUpper%, IDLower%, Secret$)
	Local ss.SharedSecrets
	Local shared_secret$ = FindSharedSecret(IDUpper, IDLower)
	
	If shared_secret <> "" Then Return shared_secret
	
	ss = New SharedSecrets
	ss\SteamIDUpper = IDUpper
	ss\SteamIDLower = IDLower
	ss\Secret = Secret
	
	Return ss\Secret
End Function

Function FindSharedSecret$(IDUpper%, IDLower%)
	Local ss.SharedSecrets, value$
	
	For ss = Each SharedSecrets
		If ss\SteamIDUpper = IDUpper And ss\SteamIDLower = IDLower Then
			value = ss\Secret
			Return value
		EndIf
	Next
	
	Return ""
End Function

Function DeleteSharedSecret(IDUpper%, IDLower%)
	Local ss.SharedSecrets, value$
	
	For ss = Each SharedSecrets
		If ss\SteamIDUpper = IDUpper And ss\SteamIDLower = IDLower Then
			Delete ss
		EndIf
	Next
	
End Function

Type Bans
	Field SteamIDLower%
	Field SteamIDUpper%
	Field Reason$
End Type

Function ReadBanList()
	Local ba.Bans, f%, temp$, temp2$
	
	If FileType(mp_O\BanFile) <> 1 Then
		WriteFile(mp_O\BanFile)
		Return
	EndIf
	
	f = ReadFile(mp_O\BanFile)
	
	While (Not Eof(f))
		temp = ReadLine(f)
		If temp <> "" Then
			ba = New Bans
			temp2 = Piece(temp, 1, BANLIST_SEPARATOR)
			ba\SteamIDLower = Steam_StringToIDLower(temp2)
			ba\SteamIDUpper = Steam_StringToIDUpper(temp2)
			ba\Reason = Piece(temp, 2, BANLIST_SEPARATOR)
		EndIf
	Wend
	
	CloseFile f
End Function

Function AddBan(IDUpper%, IDLower%, Reason$)
	Local ba.Bans, f%, temp$
	
	If CheckForBan(IDUpper, IDLower) Then
		Return
	EndIf
	
	ba = New Bans
	ba\SteamIDLower = IDLower
	ba\SteamIDUpper = IDUpper
	ba\Reason = Reason
	
	f = OpenFile(mp_O\BanFile)
	
	temp = "string"
	While (temp <> "")
		temp = ReadLine(f)
	Wend
	WriteLine(f, Steam_IDToString(IDUpper, IDLower) + BANLIST_SEPARATOR + ba\Reason)
	
	CloseFile f
End Function

Function RemoveBan(IDUpper%, IDLower%)
	Local ba.Bans, f%, temp$
	
	For ba = Each Bans
		If ba\SteamIDLower = IDLower And ba\SteamIDUpper = IDUpper Then
			Delete ba
			f = OpenFile(mp_O\BanFile)
			
			While (Not Eof(f))
				temp = ReadLine(f)
				If Piece(temp, 1, BANLIST_SEPARATOR) = Steam_IDToString(IDUpper, IDLower) Then
					WriteLine(f, "")
				EndIf
			Wend
			
			CloseFile f
			Exit
		EndIf
	Next
	
End Function

Function CheckForBan%(IDUpper%, IDLower%)
	Local ba.Bans
	
	For ba = Each Bans
		If ba\SteamIDLower = IDLower And ba\SteamIDUpper = IDUpper Then
			Return True
		EndIf
	Next
	
	Return False
End Function

Function GetBanReason$(IDUpper%, IDLower%)
	Local ba.Bans
	
	For ba = Each Bans
		If ba\SteamIDLower = IDLower And ba\SteamIDUpper = IDUpper Then
			Return ba\Reason
		EndIf
	Next
	
	Return ""
End Function

Include "SourceCode\Multiplayer\Multiplayer_ServerList.bb"
Include "SourceCode\Multiplayer\Multiplayer_PlayerBase.bb"
Include "SourceCode\Multiplayer\Multiplayer_Chat.bb"
Include "SourceCode\Multiplayer\Multiplayer_GamemodesAndMaps.bb"
Include "SourceCode\Multiplayer\Multiplayer_Lobby.bb"
Include "SourceCode\Multiplayer\Multiplayer_Loading.bb"
Include "SourceCode\Multiplayer\Multiplayer_GUI.bb"
Include "SourceCode\Multiplayer\Multiplayer_NPCBase.bb"
Include "SourceCode\Multiplayer\Multiplayer_ItemBase.bb"
Include "SourceCode\Multiplayer\Multiplayer_GunBase.bb"

Function InitMultiplayer()
	mp_I.MultiplayerInstance = New MultiplayerInstance
	mp_O.MultiplayerOptions = New MultiplayerOptions
	
	mp_O\PlayerName = Steam_GetPlayerName()
	mp_O\MaxPlayers = GetINIInt(gv\OptionFile,"server","max players",4)
	mp_O\TimeOut = GetINIInt(gv\OptionFile,"server","timeout",5)
	mp_O\LocalServer = GetINIInt(gv\OptionFile,"server","local",0)
	mp_O\ServerName = GetINIString(gv\OptionFile,"server","name")
	mp_O\Password = GetINIString(gv\OptionFile,"server","password")
	mp_O\HardcoreMP = GetINIString(gv\OptionFile,"server","hardcore mp", 0)
	mp_O\OtherTeams = GetINIString(gv\OptionFile,"server","other team", 0)
	mp_O\BanFile = GetEnv("AppData")+"\scp-security-stories\server_bans.txt"
	ReadBanList()
	
	LoadMultiplayerMenuResources()
	
End Function

Function LoadMultiplayerMenuResources()
	
	mp_I\ServerIcon = LoadAnimImage("GFX\menu\Multiplayer\server_icons.png",64,64,0,2)
	MaskImage mp_I\ServerIcon,0,0,0
	ResizeImage(mp_I\ServerIcon, (ImageWidth(mp_I\ServerIcon) * MenuScale)/2.4, (ImageHeight(mp_I\ServerIcon) * MenuScale)/2.4)
	
	mp_I\PasswordIcon = LoadAnimImage("GFX\menu\Multiplayer\password_icons.png",30,30,0,2)
	MaskImage mp_I\PasswordIcon,0,0,0
	ResizeImage(mp_I\PasswordIcon, (ImageWidth(mp_I\PasswordIcon) * MenuScale), (ImageHeight(mp_I\PasswordIcon) * MenuScale))
	
	mp_I\TickIcon = LoadImage("GFX\menu\Multiplayer\tick.png");
	MaskImage mp_I\TickIcon,0,0,0
	ResizeImage(mp_I\TickIcon, (ImageWidth(mp_I\TickIcon) * MenuScale), (ImageHeight(mp_I\TickIcon) * MenuScale))
	
	mp_I\ConnectionIcons = LoadAnimImage("GFX\menu\Multiplayer\connection_icons.png",30,30,0,5)
	MaskImage mp_I\ConnectionIcons,0,0,0
	ResizeImage(mp_I\ConnectionIcons, (ImageWidth(mp_I\ConnectionIcons) * MenuScale)/1.125, (ImageHeight(mp_I\ConnectionIcons) * MenuScale)/1.125)
	
End Function

If opt\SteamEnabled Then 
	InitMultiplayer()
	LoadMPMaps()
	LoadMPGameModes()
EndIf

Function CreateServer()
	
	mp_I\PlayState=GAME_SERVER
	;debuglog "---Created Server---"
	;debuglog "Username: "+mp_O\PlayerName$
	CreateHostPlayer()
	mp_I\PlayerID = 0
	mp_I\PlayerCount = 1
	mp_I\MaxPlayers = mp_O\MaxPlayers
	mp_I\TimeOut = mp_O\TimeOut
	mp_I\ServerName = mp_O\ServerName
	mp_I\MapInList = mp_O\MapInList
	mp_I\Gamemode = mp_O\Gamemode
	mp_I\HardcoreMP = mp_O\HardcoreMP
	mp_I\OtherTeams = mp_O\OtherTeams
	Steam_CreateLobby(2-mp_O\LocalServer,mp_O\MaxPlayers)
End Function

Function CreateHostPlayer()
	
	Players[0] = New Player
	Players[0]\Name = mp_O\PlayerName ;this is the host's name
	Players[0]\LastMsgTime = MilliSecs() ;this sets a timer to detect if the connection with the server is still established
	;Players[0]\Connected = True
	
End Function

Function CreateHostPlayerAsClient(IDUpper%, IDLower%)
	
	Players[0] = New Player
	Players[0]\SteamIDUpper = IDUpper
	Players[0]\SteamIDLower = IDLower
	Players[0]\LastMsgTime = MilliSecs()
	
End Function

Function CreateClientPlayer(id%, IDUpper%, IDLower%, name$)
	
	If Players[id] = Null Then
		Players[id] = New Player
	EndIf
	Players[id]\SteamIDUpper = IDUpper
	Players[id]\SteamIDLower = IDLower
	Players[id]\LastMsgTime=MilliSecs()
	Players[id]\Name = name
	
	If mp_I\Gamemode\ID = Gamemode_Deathmatch Then
		Players[id]\Team = Team_Unknown
	EndIf
	
End Function

Function Connect(UserIDUpper%, UserIDLower%)
	CatchErrors("Connect(" + UserIDUpper + ", " + UserIDLower + ")")
	Local waitForCo% = MilliSecs()+10000
	Local getconn%
	Local retries% = 0
	Local x% = 640*MenuScale, y% = 376*MenuScale
	Steam_JoinLobby(UserIDUpper%, UserIDLower%)
	ConnectWithNoPassword(UserIDUpper%, UserIDLower%)
	mp_I\ServerMSG = SERVER_MSG_CONNECT
	mp_I\ConnectionTime = 0.0
	mp_I\ConnectionRetries = 0
	
	CatchErrors("Uncaught (Connect)")
End Function

Function ConnectFinal()
	CatchErrors("ConnectFinal")
	Local mfl.MapForList,mgm.MultiplayerGameMode
	Local currMSGSync%,ingame%
	Local i%,exists%
	Local mapname$, gamemode%
	Local secret$, getconn%
	
	;currMSGSync = ReadByte(mp_I\Server)
	currMSGSync = Steam_PullByte()
	
	If currMSGSync = PACKET_KICK Then
		;The client got kicked from the server
		;CloseUDPStream(mp_I\Server)
		mp_I\ServerMSG = Steam_PullByte()
		If mp_I\ServerMSG = SERVER_MSG_KICK_KICKED Lor mp_I\ServerMSG = SERVER_MSG_KICK_BANNED Then
			mp_I\KickReason = Steam_PullString()
		EndIf
		Steam_CloseConnection(Players[0]\SteamIDUpper, Players[0]\SteamIDLower)
		Steam_LeaveLobby()
		DeleteSharedSecret(Players[0]\SteamIDUpper, Players[0]\SteamIDLower)
		mp_I\PlayerCount = 0
		;mp_I\Server = 0
		mp_I\ChatMSGID = 0
		Delete Each Player
		ShouldDeleteGadgets = True
		MainMenuOpen = True
		CatchErrors("Uncaught (ConnectFinal)")
		Return
	ElseIf currMSGSync = PACKET_AUTHORIZE Then
		;The client needs to authorize by a password
		mp_I\ServerMSG = SERVER_MSG_PASSWORD
		mp_I\ConnectPassword = ""
		mp_I\PasswordVisible = False
		ShouldDeleteGadgets = True
		MainMenuOpen = True
		CatchErrors("Uncaught (ConnectFinal)")
		Return
	ElseIf currMSGSync = PACKET_KEY Then
		secret = Steam_PullString()
		
		Steam_PushByte(PACKET_LOAD)
		CreateSharedSecret(Players[0]\SteamIDUpper, Players[0]\SteamIDLower, secret)
		Steam_PushString(Key_Encode(ENCRYPTION_KEY(), secret))
		Steam_PushString(VersionNumber)
		Steam_PushString(mp_O\PlayerName)
		Steam_SendPacketToUser(Players[0]\SteamIDUpper, Players[0]\SteamIDLower, k_EP2PSendUnreliable)
		While (Not getconn)
			getconn = Steam_LoadPacket()
			Flip
		Wend
		ConnectFinal()
		Return
	ElseIf currMSGSync = PACKET_LOAD Then
;		mp_I\PlayerID = ReadByte(mp_I\Server)
;		;debuglog "ID: "+mp_I\PlayerID
;		mp_I\TimeOut = ReadInt(mp_I\Server)
;		mp_I\MaxPlayers = ReadByte(mp_I\Server)
;		Local mapName = ReadLine(mp_I\Server)
;		For mfl = Each MapForList
;			If mfl\Name = mapName Then
;				mp_I\MapInList = mfl
;				Exit
;			EndIf
;		Next
;		Local gamemode = ReadByte(mp_I\Server)
;		For mgm = Each MultiplayerGameMode
;			If mgm\ID = gamemode Then
;				mp_I\Gamemode = mgm
;				Exit
;			EndIf
;		Next
;		;Everything gamemode specific required for other clients to know will be received here
;		Select mp_I\Gamemode\ID
;			Case Gamemode_Waves
;				;[Block]
;				mp_I\Gamemode\MaxPhase = ReadByte(mp_I\Server)
;				mp_I\Gamemode\Difficulty = ReadByte(mp_I\Server)
;				;[End Block]
;		End Select
		mp_I\PlayerID = Steam_PullByte()
		mp_I\TimeOut = Steam_PullInt()
		mp_I\MaxPlayers = Steam_PullByte()
		mapname = Steam_PullString()
		For mfl = Each MapForList
			If mfl\Name = mapname Then
				mp_I\MapInList = mfl
				Exit
			EndIf
		Next
		gamemode = Steam_PullByte()
		For mgm = Each MultiplayerGameMode
			If mgm\ID = gamemode Then
				mp_I\Gamemode = mgm
				Exit
			EndIf
		Next
		;Everything gamemode specific required for other clients to know will be received here
		Select mp_I\Gamemode\ID
			Case Gamemode_Waves,Gamemode_EAF
				;[Block]
				mp_I\Gamemode\Phase = Steam_PullByte()
				mp_I\Gamemode\MaxPhase = Steam_PullByte()
				mp_I\Gamemode\Difficulty = Steam_PullByte()
				;[End Block]
		End Select
		
		DeleteSharedSecret(Players[0]\SteamIDUpper, Players[0]\SteamIDLower)
	Else
		ConnectFinal()
		Return
	EndIf
	mp_I\PlayState=GAME_CLIENT
	
;	If ingame Then
;		mp_I\ReadyTimer = 0
;		ResetControllerSelections()
;		Null3DMenu()
;		MainMenuOpen = False
;		;debuglog "Starting Multiplayer Match!"
;		For i = 1 To (mp_I\MaxPlayers)
;			exists = ReadByte(mp_I\Server)
;			If exists Then
;				Players[i] = New Player
;				Players[i]\Name = ReadLine(mp_I\Server)
;			EndIf
;		Next
;		LoadingClient(True)
;		NTF_GameModeFlag = 3
;	Else
;		mp_I\ReadyTimer = 70*5
;	EndIf
	ResetControllerSelections()
	If MainMenuOpen Then
		Null3DMenu()
	EndIf
	MainMenuOpen = False
	;debuglog "Starting Multiplayer Match!"
	;RuntimeError "Player Client Data: " + currMSGSync + ", " + mp_I\PlayerID + ", " + mp_I\TimeOut + ", " + mp_I\MaxPlayers + ", " + mapname + ", " + gamemode
	Players[mp_I\PlayerID] = New Player
	LoadingClient(True)
	;TODO: Check if it should be here (Game Mode Flag)
	
	CatchErrors("Uncaught (ConnectFinal)")
End Function

Function ConnectWithNoPassword(UserIDUpper%, UserIDLower%)
	
	Steam_PushByte(PACKET_LOAD)
	Steam_PushString("")
	Steam_PushString(VersionNumber)
	Steam_PushString(mp_O\PlayerName)
	Steam_SendPacketToUser(UserIDUpper, UserIDLower, k_EP2PSendUnreliable)
	
End Function

Function ConnectViaPassword()
	
	Steam_PushByte(PACKET_AUTHORIZE)
	Steam_PushString(mp_I\ConnectPassword)
	Steam_PushString(Key_Encode(ENCRYPTION_KEY(), FindSharedSecret(Players[0]\SteamIDUpper, Players[0]\SteamIDLower)))
	Steam_PushString(VersionNumber)
	Steam_PushString(mp_O\PlayerName)
	Steam_SendPacketToUser(Players[0]\SteamIDUpper, Players[0]\SteamIDLower, k_EP2PSendUnreliable)
	
End Function

Function CheckForConnectingPlayer(currMSGSync%)
	CatchErrors("CheckForConnectingPlayer(" + currMSGSync + ")")
	Local giveID%, name$, password$, i%, c_key$, secret$, version$, cmsg.ChatMSG
	Local id% = -1
	
	Local IDUpper% = Steam_GetSenderIDUpper()
	Local IDLower% = Steam_GetSenderIDLower()
	
	For giveID = 1 To mp_I\MaxPlayers-1
		If Players[giveID] <> Null Then
			If Players[giveID]\SteamIDUpper = IDUpper And Players[giveID]\SteamIDLower = IDLower Then
				;debuglog "Player " + giveID + " already exists!"
				CatchErrors("Uncaught (CheckForConnectingPlayer)")
				Return -1
			EndIf
		EndIf
	Next
	
	If giveID >= 0 Then
		For giveID = 1 To mp_I\MaxPlayers
			If giveID >= mp_I\MaxPlayers Then
				giveID = -1
				Exit
			ElseIf (Players[giveID] = Null) Then
				Exit
			EndIf
		Next
	EndIf
	
	If currMSGSync = PACKET_AUTHORIZE Then
		password = Steam_PullString()
	EndIf
	c_key = Steam_PullString()
	version = Steam_PullString()
	name = Steam_PullString()
	Steam_LoadPacket()
	
	If (Not CheckForBan(IDUpper, IDLower)) Then
		If version = VersionNumber Then
			If giveID > 0 Then ;server can accept another player
				If c_key = "" Then
					secret = CreateSharedSecret(IDUpper, IDLower, Key_GenerateSalt())
					Steam_PushByte(PACKET_KEY)
					Steam_PushString(secret)
					Steam_SendPacketToUser(IDUpper, IDLower, k_EP2PSendUnreliable)
				ElseIf c_key <> Key_Encode(ENCRYPTION_KEY(), FindSharedSecret(IDUpper, IDLower)) Then
					Steam_PushByte(PACKET_KICK)
					Steam_PushByte(SERVER_MSG_KICK_ENCRYPTION)
					Steam_SendPacketToUser(IDUpper, IDLower, k_EP2PSendUnreliable)
					DeleteSharedSecret(IDUpper, IDLower)
				Else
					If (mp_O\Password = "" Lor (currMSGSync = PACKET_AUTHORIZE And password = mp_O\Password)) Then
						CreateClientPlayer(giveID, IDUpper, IDLower, name)
						Steam_PushByte(PACKET_LOAD)
						Steam_PushByte(giveID)
						Steam_PushInt(mp_I\TimeOut)
						Steam_PushByte(mp_I\MaxPlayers)
						Steam_PushString(mp_I\MapInList\Name)
						Steam_PushByte(mp_I\Gamemode\ID)
						
						;Everything gamemode specific required for other clients to know will be sent here
						Select mp_I\Gamemode\ID
							Case Gamemode_Waves,Gamemode_EAF
								;[Block]
								Steam_PushByte(mp_I\Gamemode\Phase)
								Steam_PushByte(mp_I\Gamemode\MaxPhase)
								Steam_PushByte(mp_I\Gamemode\Difficulty)
								;[End Block]
						End Select
						
						cmsg = AddChatMSG("user_join", 0, SERVER_MSG_IS, CHATMSG_TYPE_ONEPARAM_TRANSLATE)
						cmsg\Msg[1] = Players[giveID]\Name
						
						Steam_SendPacketToUser(Players[giveID]\SteamIDUpper, Players[giveID]\SteamIDLower, k_EP2PSendUnreliable)
						DeleteSharedSecret(Players[giveID]\SteamIDUpper, Players[giveID]\SteamIDLower)
						mp_I\PlayerCount=mp_I\PlayerCount+1
						UpdateServer(Steam_GetPlayerIDLower(), Steam_GetPlayerIDUpper(), mp_I\PlayerCount)
						id = giveID
					ElseIf currMSGSync <> PACKET_AUTHORIZE Then
						Local skip% = False
						For i = 0 To (mp_I\MaxPlayers-1)
							If Players[i] <> Null Then
								If Players[i]\SteamIDUpper = IDUpper And Players[i]\SteamIDLower = IDLower Then
									skip = True
									Exit
								EndIf
							EndIf
						Next
						If (Not skip) Then
							Steam_PushByte(PACKET_AUTHORIZE)
							Steam_SendPacketToUser(IDUpper, IDLower, k_EP2PSendUnreliable)
						EndIf
					Else
						Steam_PushByte(PACKET_KICK)
						Steam_PushByte(SERVER_MSG_KICK_PASSWORD)
						Steam_SendPacketToUser(IDUpper, IDLower, k_EP2PSendUnreliable)
						DeleteSharedSecret(IDUpper, IDLower)
					EndIf
				EndIf
			Else ;server is full
				Steam_PushByte(PACKET_KICK)
				Steam_PushByte(SERVER_MSG_KICK_MANYPLAYERS)
				Steam_SendPacketToUser(IDUpper, IDLower, k_EP2PSendUnreliable)
				DeleteSharedSecret(IDUpper, IDLower)
			EndIf
		Else
			Steam_PushByte(PACKET_KICK)
			Steam_PushByte(SERVER_MSG_KICK_VERSION)
			Steam_SendPacketToUser(IDUpper, IDLower, k_EP2PSendUnreliable)
			DeleteSharedSecret(IDUpper, IDLower)
		EndIf
	Else
		Steam_PushByte(PACKET_KICK)
		Steam_PushByte(SERVER_MSG_KICK_BANNED)
		Steam_PushString(GetBanReason(IDUpper, IDLower))
		Steam_SendPacketToUser(IDUpper, IDLower, k_EP2PSendUnreliable)
		DeleteSharedSecret(IDUpper, IDLower)
	EndIf
	
	;debuglog "Player that connects to your server: " + id
	CatchErrors("Uncaught (CheckForConnectingPlayer)")
	Return id
	
End Function

Function Disconnect()
	Local i%, prevMilliSecs%
	
	Steam_LeaveLobby()
	If mp_I\PlayState = GAME_SERVER Then
		For i = 1 To (mp_I\MaxPlayers-1)
			If Players[i] <> Null Then
				Steam_PushByte(PACKET_QUIT)
				Steam_SendPacketToUser(Players[i]\SteamIDUpper, Players[i]\SteamIDLower, k_EP2PSendUnreliable)
			EndIf
		Next
	Else
		If Players[0]<>Null Then
			Steam_PushByte(PACKET_QUIT)
			Steam_PushByte(mp_I\PlayerID)
			Steam_SendPacketToUser(Players[0]\SteamIDUpper, Players[0]\SteamIDLower, k_EP2PSendUnreliable)
		EndIf
	EndIf
	
	DrawLoading(0, True,"","Deleting_game")
	prevMilliSecs = MilliSecs()
	While ((MilliSecs() - prevMilliSecs) / 1000.0) < 1.0
		DrawLoading(-1, True,"","Deleting_game")
		Flip
	Wend
	
	If mp_I\PlayState = GAME_SERVER Then
		For i = 1 To (mp_I\MaxPlayers-1)
			If Players[i]<>Null Then
				Steam_CloseConnection(Players[i]\SteamIDUpper, Players[i]\SteamIDLower)
			EndIf
		Next
	Else
		If Players[0]<>Null Then
			Steam_CloseConnection(Players[0]\SteamIDUpper, Players[0]\SteamIDLower)
		EndIf
	EndIf
	
	mp_I\PlayerCount = 0
	mp_I\ChatMSGID = 0
	
End Function

Function SaveMPOptions()
	Local mgm.MultiplayerGameMode
	
	PutINIValue(gv\OptionFile,"server","max players",mp_O\MaxPlayers)
	PutINIValue(gv\OptionFile,"server","timeout",mp_O\TimeOut)
	PutINIValue(gv\OptionFile,"server","local",mp_O\LocalServer)
	PutINIValue(gv\OptionFile,"server","name",mp_O\ServerName)
	PutINIValue(gv\OptionFile,"server","password",mp_O\Password)
	PutINIValue(gv\OptionFile,"server","map",mp_O\MapInList\Name)
	PutINIValue(gv\OptionFile,"server","gamemode",mp_O\Gamemode\name)
	PutINIValue(gv\OptionFile,"server","hardcore mp",mp_O\HardcoreMP)
	PutINIValue(gv\OptionFile,"server","other team",mp_O\OtherTeams)
	
	For mgm = Each MultiplayerGameMode
		Select mgm\ID
			Case Gamemode_Waves,Gamemode_EAF
				;[Block]
				PutINIValue(gv\OptionFile,"server","waves_difficulty",mgm\Difficulty)
				PutINIValue(gv\OptionFile,"server","waves_max",mgm\MaxPhase)
				;[End Block]
		End Select
	Next
	
End Function

Function MPMainLoop()
	Local i%
	Local prevGun[MaxPlayers-1]
	
	While (ft\accumulator>0.0)
		ft\accumulator = ft\accumulator-GetTickDuration()
		If (ft\accumulator<=0.0) Then CaptureWorld()
		
		For i=0 To (mp_I\MaxPlayers-1)
			If Players[i]<>Null Then
				prevGun[i] = Players[i]\WeaponInSlot[Players[i]\SelectedSlot]
			EndIf
		Next
		
		RecvDataServer()
		
		If Input_ResetTime>0
			Input_ResetTime = Max(Input_ResetTime-FPSfactor,0.0)
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
				
				If (Not InLobby()) And (Not MenuOpen) And (Not ConsoleOpen) And (Not mp_I\ChatOpen) And (Not IsModerationOpen()) And (Not IsInVote()) Then
					KeyHitUse = KeyHit(KEY_USE)
					KeyDownUse = KeyDown(KEY_USE)
				Else
					KeyHitUse = False
					KeyDownUse = False
				EndIf
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
				If (Not InLobby()) And (Not MenuOpen) And (Not ConsoleOpen) And (Not mp_I\ChatOpen) And (Not IsModerationOpen()) And (Not IsInVote()) Then
					KeyHitUse = JoyHit(CK_Use)
					KeyDownUse = JoyDown(CK_Use)
				Else
					KeyHitUse = False
					KeyDownUse = False
				EndIf
			EndIf
		EndIf
		
		If (Not KeyDownUse) And (Not KeyHitUse) Then GrabbedEntity = 0
		
		UpdateMusic()
		If opt\EnableSFXRelease Then AutoReleaseSounds()
		
		DrawHandIcon = False
		
		RestoreSanity = True
		ShouldEntitiesFall = True
		
		If mp_I\Gamemode <> Null Then
			Select mp_I\Gamemode\ID
				Case Gamemode_Waves
					If (mp_I\Gamemode\Phase Mod 2)=1 Lor InLobby() Then
						ShouldPlay = MUS_MP_IDLE
					Else
						ShouldPlay = MUS_MP_ACTION	
					EndIf
				Case Gamemode_EAF
					If (mp_I\Gamemode\Phase Mod 2)=1 Lor InLobby() Then
						ShouldPlay = MUS_MP_IDLE
					Else
						ShouldPlay = MUS_MP_ACTION	
					EndIf
				Case Gamemode_Deathmatch,Gamemode_Gungame
					ShouldPlay = MUS_NULL
			End Select
		EndIf
		
		co\PressedButton = JoyHit(CKM_Press)
		co\PressedNext = JoyDown(CKM_Next)
		co\PressedPrev = JoyDown(CKM_Prev)
		If co\PressedNext And co\PressedPrev
			co\PressedNext = False
			co\PressedPrev = False
		EndIf
		
		Local g.Guns
		
		LightVolume = CurveValue(TempLightVolume, LightVolume, 50.0)
		;CameraFogRange(Camera, CameraFogNear*LightVolume,CameraFogFar*LightVolume)
		CameraFogColor(Camera, 0,0,0)
		CameraFogMode Camera,1
		;CameraRange(Camera, 0.01, Min(CameraFogFar*LightVolume*1.5,28))
		
		AmbientLight Brightness, Brightness, Brightness	
		PlayerSoundVolume = CurveValue(0.0, PlayerSoundVolume, 5.0)
		
		If LightFlash > 0 Then
			ShowEntity Light
			EntityAlpha(Light, Max(Min(LightFlash + Rnd(-0.2, 0.2), 1.0), 0.0))
			LightFlash = Max(LightFlash - (FPSfactor / 70.0), 0)
		Else
			HideEntity Light
		EndIf
		
		UpdateGUIMP()
		
		MouseLookServer()
		If (Not IsSpectator(mp_I\PlayerID)) And mpl\HasNTFGasmask Then ;CHECK FOR IMPLEMENTATION
			MovePlayerServer()
			If Players[mp_I\PlayerID]\CurrHP > 0 Then
				ShowEntity GasMaskOverlay2
			Else
				HideEntity GasMaskOverlay2
			EndIf	
		Else
			;UpdateSpectatorPosition(mp_I\PlayerID)
			HideEntity g_I\GunPivot
		EndIf
		UpdatePlayerPositionsServer()
		UpdateItemSpawners()
		UpdateEnemySpawners()
		UpdateMPItemsGravity()
		UpdateMPItems()
		UpdateNPCsServer()
		UpdateLightsMPMap(Camera)
		UpdateFluLights()
		If (Not IsSpectator(mp_I\PlayerID)) Then ;CHECK FOR IMPLEMENTATION
			UpdateGuns()
			UpdateAttachments()
		EndIf
		UpdatePlayerGunsServer()
		PlayGunSoundsMP()
		AnimateGunsServer()
		UpdatePlayerModel()
		If Players[mp_I\PlayerID]\CurrHP > 0 And (Not ConsoleOpen) And (Not AttachmentMenuOpen) And (Not mp_I\ChatOpen) And (Not MenuOpen) And (Not InLobby()) And (Not IsModerationOpen()) And (Not IsInVote()) Then
			If (Not IsPlayerListOpen()) Then
				UpdateIronSight()
			EndIf
			UpdateNightVision()
			For g = Each Guns
				If opt\RenderScope Then
					If g_I\HoldingGun = g\ID And g\HasAttachments[ATT_ACOG_SCOPE] Then
						UpdateScope()
					EndIf
				EndIf
			Next
		EndIf
		UpdateDamageOverlay()
		UpdateChunksMP()
		;PlaceSpray()
		UpdateEmitters()
		UpdateDecals()
		UpdateParticles()
		AnimatePlayerModelsAndSpectate()
		UpdateFuseBoxes()
		UpdateGenerators()
		UpdateButtonGen()
		UpdateLeverGen()
		UpdateDamageBossRadius()
		UpdateParticleGen()
		UpdateSoundEmittors()
		UpdateWorld()
		GetPlayerPositions()
		ManipulateNPCBones()
		ManipulatePlayerModelBones()
		UpdateGamemodeMP()
		
		BlurVolume = Min(CurveValue(0.0, BlurVolume, 20.0),0.95)
		If BlurTimer > 0.0 Then
			BlurVolume = Max(Min(0.95, BlurTimer / 1000.0), BlurVolume)
			BlurTimer = Max(BlurTimer - FPSfactor, 0.0)
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
		
		If KeyHit(KEY_ATTACHMENTS) And (Not InLobby()) And (Not MenuOpen) And (Not ConsoleOpen) And (Not mp_I\ChatOpen) And (Not IsModerationOpen()) And (Not IsInVote()) Then
			For g = Each Guns
				If g_I\HoldingGun = g\ID And g\CanSelectMenuAttachments Then
					AttachmentMenuOpen = (Not AttachmentMenuOpen)
					FlushKeys()
				EndIf
			Next
		EndIf
		
		UpdateChat()
		UpdateLobby()
		UpdateMPMenu()
		
		If MainMenuOpen Then Return
		
		If m_msg\Timer>0
			m_msg\Timer=m_msg\Timer-FPSfactor2
		EndIf
		
		For i = 0 To (mp_I\MaxPlayers-1)
			If Players[i]<>Null Then
				If Players[i]\WeaponInSlot[Players[i]\SelectedSlot] <> prevGun[i] Then
					SwitchPlayerGun(i)
				EndIf
			EndIf
		Next
		
		SyncServer()
		UpdateAchievementMsg()
	Wend
	CameraProjMode ark_blur_cam,0
	CameraProjMode Camera,1
	If mpl\NightVisionEnabled=3
		AmbientLight 255,255,255
	ElseIf PlayerRoom<>Null
		AmbientLight Brightness, Brightness, Brightness
	EndIf
	RenderWorld(Max(0.0,1.0+(ft\Accumulator/ft\TickDuration)))
	CurrTrisAmount = TrisRendered()
	;render sprites
	CameraProjMode ark_blur_cam,2
	CameraProjMode Camera,0
	RenderWorld()
	CameraProjMode ark_blur_cam,0
	
	UpdateBlur(BlurVolume)
	
	DrawGUIMP()
	
	RenderChat()
	RenderLobby()
	
	For g = Each Guns
		If opt\RenderScope Then
			If Players[mp_I\PlayerID]\CurrHP > 0 And g_I\HoldingGun = g\ID And g\HasAttachments[ATT_ACOG_SCOPE]; And ReadyToShowDot Then
				RenderScope()
			EndIf
		EndIf
	Next
	
	DrawMPMenu()
	
	UpdateConsole(3)
	
	UpdateSubtitles()
	
	UpdateMsg()
	
	Color 255, 255, 255
	SetFont fo\ConsoleFont
	If opt\ShowFPS Then
		Text 20, 20, "FPS: " + ft\FPS : SetFont fo\Font[Font_Default]
	EndIf
	
	RenderAchievementMsg()
	RenderSubtitles()
	
End Function

Function MPMainLoopClient()
	Local IsOnTeam%
	Local i%
	Local prevGun[MaxPlayers-1]
	
	While (ft\Accumulator>0.0)
		ft\Accumulator = ft\Accumulator-GetTickDuration()
		If (ft\Accumulator<=0.0) Then CaptureWorld()
		
		For i=0 To (mp_I\MaxPlayers-1)
			If Players[i]<>Null Then
				prevGun[i] = Players[i]\WeaponInSlot[Players[i]\SelectedSlot]
			EndIf
		Next
		
		RecvDataClient()
		If MainMenuOpen Then Return
		
		If Input_ResetTime>0
			Input_ResetTime = Max(Input_ResetTime-FPSfactor,0.0)
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
				KeyHitUse = JoyHit(CK_Use)
				KeyDownUse = JoyDown(CK_Use)
			EndIf
		EndIf
		
		If (Not KeyDownUse) And (Not KeyHitUse) Then GrabbedEntity = 0
		
		UpdateMusic()
		If opt\EnableSFXRelease Then AutoReleaseSounds()
		
		DrawHandIcon = False
		
		RestoreSanity = True
		ShouldEntitiesFall = True
		
		If mp_I\Gamemode <> Null Then
			Select mp_I\Gamemode\ID
				Case Gamemode_Waves
					If (mp_I\Gamemode\Phase Mod 2)=1 Lor InLobby() Then
						ShouldPlay = MUS_MP_IDLE
					Else
						ShouldPlay = MUS_MP_ACTION	
					EndIf
				Case Gamemode_EAF
					If (mp_I\Gamemode\Phase Mod 2)=1 Lor InLobby() Then
						ShouldPlay = MUS_MP_IDLE
					Else
						ShouldPlay = MUS_MP_ACTION	
					EndIf
				Case Gamemode_Deathmatch,Gamemode_Gungame
					ShouldPlay = MUS_NULL
			End Select
		EndIf
		
		co\PressedButton = JoyHit(CKM_Press)
		co\PressedNext = JoyDown(CKM_Next)
		co\PressedPrev = JoyDown(CKM_Prev)
		If co\PressedNext And co\PressedPrev
			co\PressedNext = False
			co\PressedPrev = False
		EndIf
		
		Local g.Guns
		
		LightVolume = CurveValue(TempLightVolume, LightVolume, 50.0)
		;CameraFogRange(Camera, CameraFogNear*LightVolume,CameraFogFar*LightVolume)
		CameraFogColor(Camera, 0,0,0)
		CameraFogMode Camera,1
		;CameraRange(Camera, 0.01, Min(CameraFogFar*LightVolume*1.5,28))
		
		AmbientLight Brightness, Brightness, Brightness	
		PlayerSoundVolume = CurveValue(0.0, PlayerSoundVolume, 5.0)
		
		UpdateGUIMP()
		
		MouseLookServer()
		If (Not IsSpectator(mp_I\PlayerID)) And mpl\HasNTFGasmask Then ;CHECK FOR IMPLEMENTATION
			MovePlayerServer()
			UpdatePlayerPosition(mp_I\PlayerID)
			If Players[mp_I\PlayerID]\CurrHP > 0 Then
				ShowEntity GasMaskOverlay2
			Else
				HideEntity GasMaskOverlay2
			EndIf
		Else
			;UpdateSpectatorPosition(mp_I\PlayerID)
			HideEntity g_I\GunPivot
			HideEntity GasMaskOverlay2
		EndIf
		UpdateMPItems()
		;UpdateNPCsClient()
		UpdateNPCsServer()
		UpdateLightsMPMap(Camera)
		UpdateFluLights()
		If (Not IsSpectator(mp_I\PlayerID)) Then ;CHECK FOR IMPLEMENTATION
			UpdateGuns()
			UpdatePlayerGun(mp_I\PlayerID)
			UpdateAttachments()
		EndIf
		PlayGunSoundsMP()
		AnimateGunsServer()
		UpdatePlayerModel()
		If Players[mp_I\PlayerID]\CurrHP > 0 And (Not ConsoleOpen) And (Not AttachmentMenuOpen) And (Not mp_I\ChatOpen) And (Not MenuOpen) And (Not InLobby()) And (Not IsModerationOpen()) And (Not IsInVote()) Then
			If (Not IsPlayerListOpen()) Then
				UpdateIronSight()
			EndIf
			UpdateNightVision()
			For g = Each Guns
				If opt\RenderScope Then
					If g_I\HoldingGun = g\ID And g\HasAttachments[ATT_ACOG_SCOPE] Then
						UpdateScope()
					EndIf
				EndIf
			Next
		EndIf
		UpdateDamageOverlay()
		;PlaceSpray()
		UpdateChunksMP()
		UpdateEmitters()
		UpdateDecals()
		UpdateParticles()
		AnimatePlayerModelsAndSpectate()
		UpdateFuseBoxes()
		UpdateGenerators()
		UpdateButtonGen()
		UpdateLeverGen()
		UpdateDamageBossRadius()
		UpdateParticleGen()
		UpdateSoundEmittors()
		UpdateWorld()
		GetPlayerPositions()
		ManipulateNPCBones()
		ManipulatePlayerModelBones()
		
		BlurVolume = Min(CurveValue(0.0, BlurVolume, 20.0),0.95)
		If BlurTimer > 0.0 Then
			BlurVolume = Max(Min(0.95, BlurTimer / 1000.0), BlurVolume)
			BlurTimer = Max(BlurTimer - FPSfactor, 0.0)
		EndIf
		
		If KeyHit(KEY_ATTACHMENTS) And (Not InLobby()) And (Not MenuOpen) And (Not ConsoleOpen) And (Not mp_I\ChatOpen) And (Not IsModerationOpen()) And (Not IsInVote()) Then
			For g = Each Guns
				If g_I\HoldingGun = g\ID And g\CanSelectMenuAttachments Then
					AttachmentMenuOpen = (Not AttachmentMenuOpen)
					FlushKeys()
				EndIf
			Next
		EndIf
		
		UpdateChat()
		UpdateLobby()
		UpdateMPMenu()
		
		If MainMenuOpen Then Return
		
		If m_msg\Timer>0
			m_msg\Timer=m_msg\Timer-FPSfactor2
		EndIf
		
		For i = 0 To (mp_I\MaxPlayers-1)
			If Players[i]<>Null Then
				If Players[i]\WeaponInSlot[Players[i]\SelectedSlot] <> prevGun[i] Then
					SwitchPlayerGun(i)
				EndIf
			EndIf
		Next
		
		SyncClient()
		UpdateAchievementMsg()
		If MainMenuOpen Then Return
	Wend
	CameraProjMode ark_blur_cam,0
	CameraProjMode Camera,1
	If mpl\NightVisionEnabled=3
		AmbientLight 255,255,255
	ElseIf PlayerRoom<>Null
		AmbientLight Brightness, Brightness, Brightness
	EndIf
	RenderWorld(Max(0.0,1.0+(ft\Accumulator/ft\TickDuration)))
	CurrTrisAmount = TrisRendered()
	;render sprites
	CameraProjMode ark_blur_cam,2
	CameraProjMode Camera,0
	RenderWorld()
	CameraProjMode ark_blur_cam,0
	
	UpdateBlur(BlurVolume)
	
	DrawGUIMP()
	
	RenderChat()
	RenderLobby()
	
	For g = Each Guns
		If opt\RenderScope Then
			If Players[mp_I\PlayerID]\CurrHP > 0 And g_I\HoldingGun = g\ID And g\HasAttachments[ATT_ACOG_SCOPE] Then
				RenderScope()
			EndIf
		EndIf
	Next
	
	DrawMPMenu()
	
	UpdateSubtitles()
	
	UpdateMsg()
	
	Color 255, 255, 255
	SetFont fo\ConsoleFont
	If opt\ShowFPS Then
		Text 20, 20, "FPS: " + ft\fps : SetFont fo\Font[Font_Default]
	EndIf
	
	RenderAchievementMsg()
	RenderSubtitles()
	
End Function

Function RecvDataServer()
	CatchErrors("RecvDataServer")
	
	Local getconn,TempRead$,TempID,UserID$
	Local hasClosestItem%,ItemInSpecialSlot%,ItemDeleted%,ItemID%,IsItemPicked%,it.Items,p.Particles,fb.FuseBox,lg.LeverGen,bg.ButtonGen,ammo%,g.Guns
	Local wantsteam%, prevusername$
	Local NewMSG$
	Local OverHereX#, OverHereY#, OverHereZ#, voiceLineStr$
	Local X#, Y#, Z#
	
	Local currMSGSync%
	Local pingValue%
	
	;Receiving data from clients
	getconn = Steam_LoadPacket()
	While getconn
;		If Steam_PullInt() <> PACKET_HEADER_INT Then
;			Exit
;		EndIf
		
		currMSGSync = Steam_PullByte()
		
		If currMSGSync = PACKET_LOAD Lor currMSGSync = PACKET_AUTHORIZE Then
			;[Block]
			Local giveID% = CheckForConnectingPlayer(currMSGSync)
			If giveID > -1 Then
				CreatePlayer(giveID)
				RespawnPlayer(giveID)
			EndIf
			;RuntimeError "Player ID Server: " + giveID
			getconn = Steam_LoadPacket()
			;[End Block]
		ElseIf currMSGSync = PACKET_PLAYER Lor currMSGSync = PACKET_QUIT Lor currMSGSync = PACKET_PING Then
			TempID = Steam_PullByte()
			If Players[TempID] = Null Then
				Steam_CloseConnection(Steam_GetPlayerIDUpper(), Steam_GetPlayerIDLower())
				getconn = False
			Else
				If Players[TempID]\FinishedLoading Then
					Select currMSGSync
						Case PACKET_PING
							;[Block]
							Players[TempID]\HasPinged = True
							;[End Block]
						Case PACKET_PLAYER
							;[Block]
							If Players[TempID]<>Null Then
								;Players[TempID]\Connected = True
								
								prevusername = Players[TempID]\Name
								Players[TempID]\Name = Steam_PullString()
								If prevusername <> Players[TempID]\Name Lor Players[TempID]\NameTag = 0 Then
									Players[TempID]\NameTag = FreeEntity_Strict(Players[TempID]\NameTag)
									CreatePlayerTag(TempID)
								EndIf
								If Players[TempID]\obj_lower = 0 Lor Players[TempID]\obj_upper = 0 Then
									FreePlayerHitBoxes(TempID)
									Players[TempID]\obj_upper = FreeEntity_Strict(Players[TempID]\obj_upper)
									Players[TempID]\obj_lower = FreeEntity_Strict(Players[TempID]\obj_lower)
									ChangePlayerModel(TempID, Players[TempID]\Team-1)
								EndIf
								If Players[TempID]\GunModel = 0 Lor Players[TempID]\GunModelMuzzleFlash = 0 Then
									Players[TempID]\GunModelMuzzleFlash = FreeEntity_Strict(Players[TempID]\GunModelMuzzleFlash)
									Players[TempID]\GunModel = FreeEntity_Strict(Players[TempID]\GunModel)
									SwitchPlayerGun(TempID)
								EndIf
								
								Players[TempID]\Yaw = Steam_PullFloat()
								Players[TempID]\Pitch = Steam_PullFloat()
								
								X = Steam_PullFloat()
								Y = Steam_PullFloat()
								Z = Steam_PullFloat()
								If Abs(X - Players[TempID]\X) < SERVER_CHECK_POSITION And Abs(Y - Players[TempID]\Y) < SERVER_CHECK_POSITION And Abs(Z - Players[TempID]\Z) < SERVER_CHECK_POSITION Then
									PositionEntity Players[TempID]\Collider,X,Y,Z
									
									Players[TempID]\X = EntityX(Players[TempID]\Collider)
									Players[TempID]\Y = EntityY(Players[TempID]\Collider)
									Players[TempID]\Z = EntityZ(Players[TempID]\Collider)
									
									UpdatePlayerUtils(TempID)
								EndIf
								
								Players[TempID]\walkangle = ((Steam_PullByte()/255.0)*360.0)
								Local prevSlot = Players[TempID]\SelectedSlot
								Players[TempID]\SelectedSlot = Steam_PullByte()
								
								Local prevWalking = Players[TempID]\walking
								Local byte% = Steam_PullByte()
								Players[TempID]\walking = ((byte Shr 0) Mod 2)
								Players[TempID]\PressSprint = ((byte Shr 1) Mod 2)
								Players[TempID]\Crouch = ((byte Shr 2) Mod 2)
								Players[TempID]\IronSight = ((byte Shr 3) Mod 2)
								Players[TempID]\PressMouse1 = ((byte Shr 4) Mod 2)
								Players[TempID]\PressReload = ((byte Shr 5) Mod 2)
								Players[TempID]\IsReady = ((byte Shr 6) Mod 2)
								
								Local prevVote = Players[TempID]\MapVote
								Players[TempID]\MapVote = Steam_PullByte()
								If (Not IsInVote()) Lor IsVoteCompleted() Then
									Players[TempID]\MapVote = prevVote
								EndIf
								
								ammo = Steam_PullShort()
								
								; ~ Anti-Cheat System (Unnecessary anymore)
								
;								If prevSlot = Players[TempID]\SelectedSlot Then
;									For g = Each Guns
;										If g\ID = Players[TempID]\WeaponInSlot[Players[TempID]\SelectedSlot] Then
;											If (g\GunType <> GUNTYPE_MELEE And g\GunType <> GUNTYPE_SHOTGUN) Then
;												If Players[TempID]\Ammo[Players[TempID]\SelectedSlot] > ammo + 1 And (Players[TempID]\Ammo[Players[TempID]\SelectedSlot] < g\MaxCurrAmmo Lor ammo > 0) Then
;													Players[TempID]\PressMouse1 = True
;												EndIf
;											EndIf
;											Exit
;										EndIf
;									Next
;								EndIf
								
								Players[TempID]\CurrChunk = Steam_PullByte()
								
								byte = Steam_PullByte()
								hasClosestItem = ((byte Shr 0) Mod 2)
								ItemInSpecialSlot = ((byte Shr 1) Mod 2)
								
								If hasClosestItem Then
									ItemID = Steam_PullInt()
									IsItemPicked = Steam_PullByte()
									If IsItemPicked Then
										For it.Items = Each Items
											If it\ID = ItemID
												PickMPItem(it,TempID)
												Exit
											EndIf
										Next
									EndIf
								EndIf
								
								Local fusesPlaced = 0
								For fb = Each FuseBox
									fusesPlaced = Steam_PullByte()
									If fusesPlaced > fb\fuses Then
										;Only add 1 fuse, as only 1 can be placed at a time
										fb\fuses = fb\fuses + 1
									EndIf
								Next
								
								Local lever_roll# = 0.0
								For lg = Each LeverGen
									lever_roll = Steam_PullFloat()
									If lg\closest_player = TempID Then
										RotateEntity(lg\obj_handle, EntityPitch(lg\obj_handle, True), EntityYaw(lg\obj_handle), lever_roll)
									EndIf
								Next
								
								Local button_activated% = False
								For bg = Each ButtonGen
									button_activated = Steam_PullByte()
									If bg\closest_player = TempID Then
										bg\activated = button_activated
									EndIf
								Next
								
								If ItemInSpecialSlot Then
									ItemDeleted = Steam_PullByte()
									If ItemDeleted And Players[TempID]\Item <> Null Then
										Delete Players[TempID]\Item
									EndIf
								EndIf
								
								Players[TempID]\VoiceLine = Steam_PullByte()
								If Players[TempID]\VoiceLine <> WHEEL_OUTPUT_UNKNOWN Then
									Players[TempID]\VoiceLineNumber = Steam_PullByte()
									If Players[TempID]\VoiceLine = COMMAND_OVERHERE Then
										Players[TempID]\OverHereCommand = Steam_PullByte()
										voiceLineStr = "SFX\Player\Voice\Chat\"
										Select Players[TempID]\OverHereCommand
											Case ICON_LOOK_DEFAULT
												voiceLineStr = voiceLineStr + "Look"
											Case ICON_LOOK_ENEMY
												voiceLineStr = voiceLineStr + "Enemy"
											Case ICON_LOOK_AMMO
												voiceLineStr = voiceLineStr + "Ammo"
											Case ICON_LOOK_GUN
												voiceLineStr = voiceLineStr + "Gun"
										End Select
										voiceLineStr = voiceLineStr + "Here" + Players[TempID]\VoiceLineNumber + ".ogg"
										
										OverHereX = Steam_PullFloat()
										OverHereY = Steam_PullFloat()
										OverHereZ = Steam_PullFloat()
	;									p = CreateParticle(OverHereX, OverHereY, OverHereZ, 10, 0.125, 0, 500)
	;									EntityTexture p\obj,mpl\WheelLookHereIcons[Players[TempID]\OverHereCommand]
	;									EntityOrder p\obj,-2
	;									SpriteViewMode p\obj,1
										
										Players[TempID]\OverHerePosition = CreateVector3D(OverHereX, OverHereY, OverHereZ)
										If (Not Players[TempID]\Ignored) And Players[TempID]\Team = Players[mp_I\PlayerID]\Team Then
											CreateOverHereParticle(OverHereX, OverHereY, OverHereZ)
											EntityTexture Players[TempID]\OverHereSprite,mpl\WheelLookHereIcons[Players[TempID]\OverHereCommand]
											ShowEntity Players[TempID]\OverHereSprite
											PositionEntity Players[TempID]\OverHereSprite,OverHereX,OverHereY,OverHereZ
											Players[TempID]\OverHereSpriteTime = 70*5
										EndIf
									Else
										voiceLineStr = GetPlayerVoiceLine(Players[TempID]\VoiceLine, Players[TempID]\VoiceLineNumber)
									EndIf
									If (Not Players[TempID]\Ignored) And Players[TempID]\Team = Players[mp_I\PlayerID]\Team Then
										If ChannelPlaying(Players[TempID]\Sound_CHN) Then
											StopChannel(Players[TempID]\Sound_CHN)
										EndIf
										Players[TempID]\Sound_CHN = PlaySound_Strict(LoadTempSound(voiceLineStr))
									EndIf
								EndIf
								
								;prevTeam = Players[TempID]\Team
								;Players[TempID]\Team = Steam_PullByte()-1
								;If prevTeam <> Players[TempID]\Team Then
								;	SetupTeam(TempID)
								;EndIf
								wantsteam = Steam_PullByte() ;CHECK FOR IMPLEMENTATION
								If wantsteam <> Players[TempID]\Team And Players[TempID]\ForceTeam < Team_Unknown Then
									Players[TempID]\Team = wantsteam
									SetupTeam(TempID)
									If mp_I\Gamemode\PhaseTimer <= 0.0 And mp_I\ReadyTimer <= 0.0 Then
										Players[TempID]\CurrHP = 0
										Players[TempID]\IsSpectator = True
									EndIf
								EndIf
								
								If Players[TempID]\SelectedSlot<>prevSlot Then
									Players[TempID]\DeployState = 0
									Players[TempID]\ReloadState = 0
									Players[TempID]\ShootState = 0
									Players[TempID]\PressMouse1 = False
									Players[TempID]\PressReload = False
								EndIf
								
								If Players[TempID]\walking <> prevWalking And Players[TempID]\walking Then
									UpdatePlayerPosition(TempID)
								EndIf
								
								Players[TempID]\LastMsgTime = MilliSecs()
								Players[TempID]\FinishedLoading = True
								
								NewMSG = Steam_PullString()
								If NewMSG<>"" Then
									Players[TempID]\IsChatMSGTranslated = Steam_PullByte()
									If (Not Players[TempID]\Ignored) And Players[TempID]\Team = Players[mp_I\PlayerID]\Team Then
										AddChatMSG(NewMSG, TempID, SERVER_MSG_NO, Players[TempID]\IsChatMSGTranslated)
									EndIf
									Players[TempID]\SendChatMSG = NewMSG
								EndIf
								
								Players[TempID]\Ping = Steam_PullInt()
							EndIf
							;[End Block]
						Case PACKET_QUIT
							;[Block]
							If Players[TempID] <> Null Then
								DeletePlayerAsServer(Steam_GetPlayerIDUpper(), Steam_GetPlayerIDLower(), TempID, "user_left")
								Steam_CloseConnection(Steam_GetPlayerIDUpper(), Steam_GetPlayerIDLower())
							EndIf
							;[End Block]
					End Select
				Else
					If currMSGSync Then
						Players[TempID]\FinishedLoading = True
						Players[TempID]\LastMsgTime = MilliSecs()
					EndIf
				EndIf
			EndIf
		EndIf
		getconn = Steam_LoadPacket()
	Wend
	
	CatchErrors("Uncaught (RecvDataServer)")
End Function

Function RecvDataClient()
	CatchErrors("RecvDataClient")
	
	Local getconn,TempRead$,i,ttmp,j
	Local temp%
	Local it.Items,hasItem%,deleteitem%,itID%,itX#,itY#,itZ#,itName$,itTempName$,itYaw#,itFound.Items,fb.FuseBox,ge.Generator,dbr.DamageBossRadius,pg.ParticleGen,lg.LeverGen,bg.ButtonGen,g.Guns
	Local n.NPCs,deletenpc%,nID%,nX#,nY#,nZ#,nYaw#,ntype%,nFound.NPCs,nHP%,nCurrPlayer%
	Local gunAmmo1%,gunAmmo2%,reloadAmmo1%,reloadAmmo2%,sslot%,shootstate#,reloadstate#,deploystate#
	Local nState1#,nState2#,nState3#
	Local kevlar#,playDamageSFX%
	Local currsync%
	Local plTeam%
	Local prevWeaponInCurrSlot%
	Local prevTeam%, prevSpectator%, isInSameTeam%
	Local NewMSG$, msgtype%, cmsg.ChatMSG
	Local OverHereX#, OverHereY#, OverHereZ#, p.Particles, voiceLineStr$
	
	Local byte%,x#,y#,z#
	Local pmp.ParticleMP,ptype%
	Local dx#,dy#,dz#,dpitch#,dyaw#,dnx#,dny#,dnz#,dpid%
	
	Local hbX#,hbY#,hbZ#,hbB,hb.HitBox
	
	Local prevUsername$
	
	Local currMSGSync%
	
	getconn = Steam_LoadPacket()
	While getconn ;the server has given you a message
;		If Steam_PullInt() <> PACKET_HEADER_INT Then
;			Exit
;		EndIf
		Players[0]\LastMsgTime = MilliSecs()
		Players[0]\FinishedLoading = True
		currMSGSync = Steam_PullByte()
		Select currMSGSync
			Case PACKET_PING
				;[Block]
				;The packet itself doesn't contain any data, instead it just serves as a trigger to determine the ping value
				Players[mp_I\PlayerID]\Ping = (MilliSecs()-mp_I\LastPingMillisecs); - ft\DeltaTime
				;[End Block]
			Case PACKET_PLAYER
				;[Block]
				For i=0 To (mp_I\MaxPlayers-1)
					ttmp% = Steam_PullByte()
					If ttmp% = 1 Then
						If Players[i]=Null Then
							Players[i] = New Player
							CreatePlayer(i)
							mp_I\PlayerCount = mp_I\PlayerCount + 1
						EndIf
						
						prevTeam = Players[i]\Team
						Players[i]\Team = Steam_PullByte() ;CHECK FOR IMPLEMENTATION
						byte = Steam_PullByte()
						prevSpectator = Players[i]\IsSpectator
						Players[i]\IsSpectator = ((byte Shr 0) Mod 2)
						isInSameTeam = ((byte Shr 1) Mod 2)
						
						If prevTeam <> Players[i]\Team Then
;							If Players[i]\Team >= Team_Spectator Then
;								SetupTeam(i)
;							Else
;								Players[i]\Team = prevTeam
;							EndIf
							Players[i]\WantsTeam = Players[i]\Team
							SetupTeam(i)
						EndIf
						If i=mp_I\PlayerID And Players[i]\IsSpectator And prevSpectator <> Players[i]\IsSpectator Then
							FindPlayerToSpectate(1)
						EndIf
						
						;CHECK FOR IMPLEMENTATION
						Players[i]\PrevShootState = Players[i]\ShootState
						
						If i<>mp_I\PlayerID Then
							Players[i]\Ping = Steam_PullInt()
							prevUsername = Players[i]\Name
							Players[i]\Name = Steam_PullString()
							Players[i]\Yaw = Steam_PullFloat()
							Players[i]\Pitch = Steam_PullFloat()
							Players[i]\CurrSpeed = Steam_PullFloat()
							Players[i]\walkangle = Float((Steam_PullByte()/255.0)*360.0)
							Players[i]\ShootState = Steam_PullFloat()
							Players[i]\ReloadState = Steam_PullFloat()
							Players[i]\DeployState = Steam_PullFloat()
							Players[i]\CrouchState = Steam_PullFloat()
							byte = Steam_PullByte()
							
							RotateEntity Players[i]\Collider,Players[i]\Pitch,Players[i]\Yaw,0
							Players[i]\walking = ((byte Shr 0) Mod 2)
							Players[i]\IsPlayerSprinting = ((byte Shr 1) Mod 2)
							Players[i]\Crouch = ((byte Shr 2) Mod 2)
							Players[i]\IronSight = ((byte Shr 3) Mod 2)
							Players[i]\PressMouse1 = ((byte Shr 4) Mod 2)
							Players[i]\PressReload = ((byte Shr 5) Mod 2)
							Players[i]\IsReady = ((byte Shr 6) Mod 2)
							
							Players[i]\MapVote = Steam_PullByte()
							
							If isInSameTeam Then
								Players[i]\CurrChunk = Steam_PullByte()
								Players[i]\VoiceLine = Steam_PullByte()
								If Players[i]\VoiceLine <> WHEEL_OUTPUT_UNKNOWN Then
									Players[i]\VoiceLineNumber = Steam_PullByte()
									If Players[i]\VoiceLine = COMMAND_OVERHERE Then
										Players[i]\OverHereCommand = Steam_PullByte()
										voiceLineStr = "SFX\Player\Voice\Chat\"
										Select Players[i]\OverHereCommand
											Case ICON_LOOK_DEFAULT
												voiceLineStr = voiceLineStr + "Look"
											Case ICON_LOOK_ENEMY
												voiceLineStr = voiceLineStr + "Enemy"
											Case ICON_LOOK_AMMO
												voiceLineStr = voiceLineStr + "Ammo"
											Case ICON_LOOK_GUN
												voiceLineStr = voiceLineStr + "Gun"
										End Select
										voiceLineStr = voiceLineStr + "Here" + Players[i]\VoiceLineNumber + ".ogg"
										
										OverHereX = Steam_PullFloat()
										OverHereY = Steam_PullFloat()
										OverHereZ = Steam_PullFloat()
	;									p = CreateParticle(OverHereX, OverHereY, OverHereZ, 10, 0.125, 0, 500)
	;									EntityTexture p\obj,mpl\WheelLookHereIcons[commandLineID]
	;									EntityOrder p\obj,-2
	;									SpriteViewMode p\obj,1
										If (Not Players[i]\Ignored) And isInSameTeam Then
											CreateOverHereParticle(OverHereX, OverHereY, OverHereZ)
											EntityTexture Players[i]\OverHereSprite,mpl\WheelLookHereIcons[Players[i]\OverHereCommand]
											ShowEntity Players[i]\OverHereSprite
											PositionEntity Players[i]\OverHereSprite,OverHereX,OverHereY,OverHereZ
											Players[i]\OverHereSpriteTime = 70*5
										EndIf
									Else
										voiceLineStr = GetPlayerVoiceLine(Players[i]\VoiceLine, Players[i]\VoiceLineNumber)
									EndIf
									If (Not Players[i]\Ignored) And isInSameTeam Then
										If ChannelPlaying(Players[i]\Sound_CHN) Then
											StopChannel(Players[i]\Sound_CHN)
										EndIf
										Players[i]\Sound_CHN = PlaySound_Strict(LoadTempSound(voiceLineStr))
									EndIf
								EndIf
							EndIf
							
							If prevUsername <> Players[i]\Name Lor Players[i]\NameTag = 0 Then
								Players[i]\NameTag = FreeEntity_Strict(Players[i]\NameTag)
								CreatePlayerTag(i)
							EndIf
							If Players[i]\obj_lower = 0 Lor Players[i]\obj_upper = 0 Then
								FreePlayerHitBoxes(i)
								Players[i]\obj_upper = FreeEntity_Strict(Players[i]\obj_upper)
								Players[i]\obj_lower = FreeEntity_Strict(Players[i]\obj_lower)
								ChangePlayerModel(i, Players[i]\Team-1)
							EndIf
							If Players[i]\GunModel = 0 Lor Players[i]\GunModelMuzzleFlash = 0 Then
								Players[i]\GunModelMuzzleFlash = FreeEntity_Strict(Players[i]\GunModelMuzzleFlash)
								Players[i]\GunModel = FreeEntity_Strict(Players[i]\GunModel)
								SwitchPlayerGun(i)
							EndIf
						Else
							Local injuryType% = Steam_PullByte()
							If injuryType = INJURY_BULLET Then
								mpl\DamageTimer = 70
								PlaySound_Strict BullethitSFX
							EndIf
						EndIf
						x# = Steam_PullFloat()
						y# = Steam_PullFloat()
						z# = Steam_PullFloat()
						Players[i]\CurrStamina = Steam_PullFloat()
						Players[i]\StaminaEffectTimer = Steam_PullInt()
						Local prevHP# = Players[i]\CurrHP
						If i = mp_I\PlayerID Lor isInSameTeam Then
							Players[i]\CurrHP = Steam_PullFloat()
							Players[i]\CurrKevlar = Steam_PullFloat()
						Else
							byte = Steam_PullByte()
							Players[i]\CurrHP = ((byte Shr 0) Mod 2) * 100.0
							Players[i]\CurrKevlar = ((byte Shr 1) Mod 2) * 100.0
						EndIf
						
						If i<>mp_I\PlayerID Then
							Players[i]\X = x
							Players[i]\Y = y
							Players[i]\Z = z
							PositionEntity Players[i]\Collider,Players[i]\X,Players[i]\Y,Players[i]\Z
							ResetEntity Players[i]\Collider
							
							UpdatePlayerUtils(i)
						Else
;							If Abs(Players[i]\X-x) > 1.5 And Abs(Players[i]\Y-y) > 1.5 And Abs(Players[i]\Z-z) > 1.5 Then
;								If mp_I\PositionSyncTimer <= 0.0 Then
;									If Abs(Players[i]\X-x) > 0.02 Lor Abs(Players[i]\Y-y) > 0.02 Lor Abs(Players[i]\Z-z) > 0.02 Then
;										Players[i]\X = CurveValue(EntityX(Players[i]\Collider), x, 20.0)
;										Players[i]\Y = CurveValue(EntityY(Players[i]\Collider), y, 10.0)
;										Players[i]\Z = CurveValue(EntityZ(Players[i]\Collider), z, 20.0)
;										PositionEntity Players[i]\Collider,Players[i]\X,Players[i]\Y,Players[i]\Z
;										Players[i]\DropSpeed = 0.0
;									EndIf
;									mp_I\PositionSyncTimer = 70*2
;								Else
;									mp_I\PositionSyncTimer = mp_I\PositionSyncTimer - FPSfactor
;								EndIf
;							Else
;								Players[i]\X = x
;								Players[i]\Y = y
;								Players[i]\Z = z
;								PositionEntity Players[i]\Collider,Players[i]\X,Players[i]\Y,Players[i]\Z
;								ResetEntity Players[i]\Collider
;								Players[i]\DropSpeed = 0.0
;							EndIf
							
							If Abs(x - Players[i]\X) >= SERVER_CHECK_POSITION Lor Abs(y - Players[i]\Y) >= SERVER_CHECK_POSITION Lor Abs(z - Players[i]\Z) >= SERVER_CHECK_POSITION Then
								PositionEntity Players[i]\Collider,x,y,z
								ResetEntity Players[i]\Collider
								Players[i]\X = EntityX(Players[i]\Collider)
								Players[i]\Y = EntityY(Players[i]\Collider)
								Players[i]\Z = EntityZ(Players[i]\Collider)
							EndIf
							
							If prevHP <= 0 And Players[i]\CurrHP > 0 Then
								Players[i]\WantsSlot = QUICKSLOT_SECONDARY
								Players[i]\SelectedSlot = QUICKSLOT_SECONDARY
								Players[i]\X = x
								Players[i]\Y = y
								Players[i]\Z = z
								PositionEntity Players[i]\Collider,Players[i]\X,Players[i]\Y,Players[i]\Z
								ResetEntity Players[i]\Collider
								Players[i]\DropSpeed = 0.0
								SetAnimTime Players[i]\obj_lower,0
								SetAnimTime Players[i]\obj_upper,0
								MouseHit(1)
								g_I\GunChangeFLAG = False
								RotateEntity Players[i]\Collider,0,0,0
								RotateEntity Camera,0,0,0
								Players[i]\Pitch = EntityPitch(Camera)
								Players[i]\Yaw = EntityYaw(Players[i]\Collider)
								
								If mp_I\PlayerID = i Then
									If Players[i]\Team = Team_CI Then
										mp_I\Map\CurrChunk = mp_I\MapInList\CISpawn-(mp_I\MapInList\ChunkStart-1)
									Else
										mp_I\Map\CurrChunk = mp_I\MapInList\NTFSpawn-(mp_I\MapInList\ChunkStart-1)
									EndIf
								EndIf
							EndIf
						EndIf
						
						Players[i]\Ammo[QUICKSLOT_PRIMARY] = Steam_PullShort()
						Players[i]\Ammo[QUICKSLOT_SECONDARY] = Steam_PullShort()
						Players[i]\ReloadAmmo[QUICKSLOT_PRIMARY] = Steam_PullByte()
						Players[i]\ReloadAmmo[QUICKSLOT_SECONDARY] = Steam_PullByte()
						Local prevSlot = Players[i]\SelectedSlot
						Players[i]\SelectedSlot = Steam_PullByte()
						For j=0 To MaxSlots-1
							If j = Players[i]\SelectedSlot And i = mp_I\PlayerID Then
								prevWeaponInCurrSlot = Players[i]\WeaponInSlot[j]
							EndIf
							Players[i]\WeaponInSlot[j] = Steam_PullByte()
							If j = Players[i]\SelectedSlot And i = mp_I\PlayerID Then
								If prevWeaponInCurrSlot <> Players[i]\WeaponInSlot[j] Then
									MouseHit(1)
									g_I\GunChangeFLAG = False
								EndIf
								For g = Each Guns
									If g\ID = Players[i]\WeaponInSlot[Players[i]\SelectedSlot] Then
										If (g\GunType <> GUNTYPE_MELEE) Then
											mp_I\LocalAmmo = Players[i]\Ammo[j]
										Else
											mp_I\LocalAmmo = 0
										EndIf
										Exit
									EndIf
								Next
							EndIf
						Next
						If prevSlot<>Players[i]\SelectedSlot And Players[i]\WantsSlot=Players[i]\SelectedSlot Then
							Players[i]\ShootState = 0.0
							Players[i]\ReloadState = 0.0
							Players[i]\DeployState = 0.0
							Players[i]\PressMouse1 = False
							Players[i]\PressReload = False
							If i=mp_I\PlayerID Then
								MouseHit(1)
								g_I\GunChangeFLAG = False
							EndIf
						EndIf
						
						hasItem% = Steam_PullByte()
						If hasItem Then
							Local itemName$ = Steam_PullString()
							Local itemTempName$ = Steam_PullString()
							If Players[i]\Item = Null Lor Players[i]\Item\itemtemplate\name <> itemName Lor Players[i]\Item\itemtemplate\tempname <> itemTempName Then
								If Players[i]\Item <> Null Then
									Delete Players[i]\Item
								EndIf
								it = CreateItem(itemName, itemTempName, 0, 0, 0)
								Players[i]\Item = CreateInventoryItem(it)
								RemoveItem(it)
							EndIf
						ElseIf Players[i]\Item <> Null Then
							Delete Players[i]\Item
						EndIf
						
						Players[i]\Kills = Steam_PullShort()
						Players[i]\Deaths = Steam_PullShort()
					Else
						If Players[i]<>Null Then
							mp_I\PlayerCount=mp_I\PlayerCount-1
							Players[i]\Collider = FreeEntity_Strict(Players[i]\Collider)
							Players[i]\obj_upper = FreeEntity_Strict(Players[i]\obj_upper)
							Players[i]\obj_lower = FreeEntity_Strict(Players[i]\obj_lower)
							Players[i]\NameTag = FreeEntity_Strict(Players[i]\NameTag)
							Delete Players[i]
							Players[i]=Null
							If mp_I\SpectatePlayer = i Then
								FindPlayerToSpectate(1)
							EndIf
						EndIf
					EndIf
				Next
				;[End Block]
			Case PACKET_ITEM
				;[Block]
				For it = Each Items
					it\noDelete = False
				Next
				temp = Steam_PullInt()
				If temp>0
					For i=1 To temp
						itID = Steam_PullInt()
						itName = Steam_PullString()
						itTempName = Steam_PullString()
						itX = Steam_PullFloat()
						itY = Steam_PullFloat()
						itZ = Steam_PullFloat()
						itYaw = Steam_PullFloat()
						
						itFound = Null
						For it = Each Items
							If it\ID = itID
								itFound = it
								Exit
							EndIf
						Next
						
						If itFound <> Null Then
							PositionEntity itFound\collider,CurveValue(itX,EntityX(itFound\collider),10.0),CurveValue(itY,EntityY(itFound\collider),5.0),CurveValue(itZ,EntityZ(itFound\collider),10.0)
							RotateEntity itFound\collider,0,CurveAngle(itYaw,EntityYaw(itFound\collider),10.0),0
							itFound\noDelete = True
						Else
							it = CreateItem(itName,itTempName,itX,itY,itZ)
							RotateEntity it\collider,0,itYaw,0
							it\ID = itID
							it\noDelete = True
						EndIf
					Next
				EndIf
				For it = Each Items
					If (Not it\noDelete) Then
						PlayItemPickSoundMP(it,-1)
						RemoveItem(it)
					EndIf
				Next
				;[End Block]
			Case PACKET_NPC
				;[Block]
				For n = Each NPCs
					If ShouldSyncNPC(n) Then
						n\noDelete = False
					Else
						n\noDelete = True
					EndIf
				Next
				temp = Steam_PullInt()
				If temp>0 Then
					For i=1 To temp
						nID = Steam_PullInt()
						ntype = Steam_PullInt()
						nX = Steam_PullFloat()
						nY = Steam_PullFloat()
						nZ = Steam_PullFloat()
						nYaw = Steam_PullFloat()
						nState1 = Steam_PullFloat()
						nState2 = Steam_PullFloat()
						nState3 = Steam_PullFloat()
						nHP = Steam_PullInt()
						;nDistTimer = Steam_PullFloat()
						nCurrPlayer = Steam_PullByte()
						
						nFound = Null
						For n = Each NPCs
							If n\ID = nID Then
								nFound = n
								Exit
							EndIf
						Next
						
						If nFound <> Null Then
							If Abs(EntityX(nFound\Collider)-nX) > 0.5 Lor Abs(EntityY(nFound\Collider)-nY) > 0.5 Lor Abs(EntityZ(nFound\Collider)-nZ) > 0.5 Then
								PositionEntity nFound\Collider,nX,nY,nZ
								ResetEntity nFound\Collider
							Else
								PositionEntity nFound\Collider,CurveValue(nX,EntityX(nFound\Collider),10.0),CurveValue(nY,EntityY(nFound\Collider),5.0),CurveValue(nZ,EntityZ(nFound\Collider),10.0)
							EndIf
							RotateEntity nFound\Collider,0,CurveAngle(nYaw,EntityYaw(nFound\Collider),10.0),0
							nFound\State[0] = nState1
							nFound\State[1] = nState2
							nFound\State[2] = nState3
							;nFound\CurrSpeed = nCurrSpeed
							nFound\HP = nHP
							nFound\noDelete = True
							;nFound\DistanceTimer = nDistTimer
							nFound\ClosestPlayer = nCurrPlayer
						Else
							n = CreateNPC(ntype,nX,nY,nZ,nState3)
							RotateEntity n\Collider,0,nYaw,0
							n\ID = nID
							n\State[0] = nState1
							n\State[1] = nState2
							n\State[2] = nState3
							;n\CurrSpeed = nCurrSpeed
							n\HP = nHP
							n\noDelete = True
							;n\DistanceTimer = nDistTimer
							n\ClosestPlayer = nCurrPlayer
						EndIf
					Next
				EndIf
				For n = Each NPCs
					If (Not n\noDelete) Then
						RemoveNPC(n)
					EndIf
				Next
				;[End Block]
			Case PACKET_EFFECT
				;[Block]
				temp = Steam_PullInt()
				
				If temp>0 Then
					For i = 1 To temp
						ptype = Steam_PullByte()
						
						dx = Steam_PullFloat()
						dy = Steam_PullFloat()
						dz = Steam_PullFloat()
						dpid = Steam_PullByte()
						Select ptype
							Case PARTICLEMP_WALL
								dpitch = Steam_PullFloat()
								dyaw = Steam_PullFloat()
								dnx = Steam_PullFloat()
								dny = Steam_PullFloat()
								dnz = Steam_PullFloat()
								pmp = CreateParticleMP(ptype,dx,dy,dz,dpitch,dyaw,dnx,dny,dnz,dpid)
							Default
								pmp = CreateParticleMP(ptype,dx,dy,dz,0,0,0,0,0,dpid)
						End Select
					Next
				EndIf
				
				For pmp = Each ParticleMP
					FreeVector3D(pmp\pos)
					FreeVector2D(pmp\rot)
					FreeVector3D(pmp\npos)
					Delete pmp
				Next
				;[End Block]
			Case PACKET_CHATMSG
				;[Block]
				For i=0 To (mp_I\MaxPlayers-1)
					ttmp% = Steam_PullByte()
					If ttmp% Then
						NewMSG = Steam_PullString()
						If NewMSG <> "" Then
							msgtype = Steam_PullByte()
							If (Not Players[i]\Ignored) Then
								AddChatMSG(NewMSG, i, SERVER_MSG_NO, msgtype)
							EndIf
						EndIf
					EndIf
				Next
				
				temp = Steam_PullByte()
				If temp > 0 Then
					For i = 1 To temp
						msgtype = Steam_PullByte()
						NewMSG = Steam_PullString()
						cmsg = AddChatMSG(NewMSG, 0, SERVER_MSG_IS, msgtype)
						For j = 1 To (MAX_CHATMSG_PARTS - 1)
							cmsg\Msg[j] = Steam_PullString()
						Next
					Next
				EndIf
				;[End Block]
			Case PACKET_GAMEMODEUTIL
				;[Block]
				Local prevEnemyCount = mp_I\Gamemode\EnemyCount
				mp_I\Gamemode\EnemyCount = Steam_PullInt()
				Local prevPhase = mp_I\Gamemode\Phase
				mp_I\Gamemode\Phase = Steam_PullByte()
				mp_I\Gamemode\PhaseTimer = Steam_PullFloat()
				mp_I\Gamemode\DisableMovement = Steam_PullByte()
				If mp_I\Gamemode\ID = Gamemode_Waves Then
					If (mp_I\Gamemode\Phase = Waves_StartGame Or (mp_I\Gamemode\Phase Mod 2) <> 0) And mp_I\Gamemode\PhaseTimer > 0 And mp_I\Gamemode\Phase < GamemodeEnd Then
						CountdownBeep(mp_I\Gamemode\PhaseTimer, 3)
					EndIf	
					If prevEnemyCount=0 And prevEnemyCount<>mp_I\Gamemode\EnemyCount Then
						PlaySound_Strict LoadTempSound("SFX\General\WaveStart.ogg")
					EndIf
				ElseIf mp_I\Gamemode\ID = Gamemode_EAF Then
					If (mp_I\Gamemode\Phase = Waves_StartGame Or (mp_I\Gamemode\Phase Mod 2) <> 0) And mp_I\Gamemode\PhaseTimer > 0 And mp_I\Gamemode\Phase < GamemodeEnd Then
						CountdownBeep(mp_I\Gamemode\PhaseTimer, 3)
					EndIf	
					If prevEnemyCount=0 And prevEnemyCount<>mp_I\Gamemode\EnemyCount Then
						PlaySound_Strict LoadTempSound("SFX\General\WaveStart.ogg")
					EndIf
				ElseIf mp_I\Gamemode\ID = Gamemode_Deathmatch Then
					If prevPhase > Deathmatch_Game And mp_I\Gamemode\Phase = Deathmatch_GameStart Then
						If Players[mp_I\PlayerID]\Team = Team_CI Then
							mp_I\Map\CurrChunk = mp_I\MapInList\CISpawn-(mp_I\MapInList\ChunkStart-1)
						Else
							mp_I\Map\CurrChunk = mp_I\MapInList\NTFSpawn-(mp_I\MapInList\ChunkStart-1)
						EndIf
					EndIf
					If mp_I\Gamemode\Phase = Deathmatch_Game Then
						If mp_I\Gamemode\PhaseTimer > 0 Then
							CountdownBeep(mp_I\Gamemode\PhaseTimer, 3)
						EndIf	
					EndIf	
				EndIf
				
				NoClipSpeed = Steam_PullFloat()
				
				Local prevReadyTimer# = mp_I\ReadyTimer
				mp_I\ReadyTimer = Steam_PullFloat()
				If mp_I\ReadyTimer = 0.0 And prevReadyTimer > mp_I\ReadyTimer Then
					ResetControllerSelections()
					ResetInput()
					ShouldDeleteGadgets = True
					DeleteMenuGadgets()
				EndIf
				
				For i = 0 To MaxPlayerTeams-1
					mp_I\Gamemode\RoundWins[i] = Steam_PullByte()
				Next
				
				;It assumes that the amount of fuseboxes is automatically the same for each client (as it is depending on the map entirely)
				For fb = Each FuseBox
					fb\fuses = Steam_PullByte()
				Next
				For ge = Each Generator
					ge\progress = Steam_PullFloat()
				Next
				For dbr = Each DamageBossRadius
					dbr\timer = Steam_PullFloat()
				Next
				For pg = Each ParticleGen
					pg\activated = Steam_PullByte()
				Next
				Local lever_roll# = 0.0
				For lg = Each LeverGen
					lever_roll = Steam_PullFloat()
					If lg\closest_player <> mp_I\PlayerID Then
						RotateEntity(lg\obj_handle, EntityPitch(lg\obj_handle, True), EntityYaw(lg\obj_handle), lever_roll)
					EndIf
				Next
				Local button_activated% = False
				For bg = Each ButtonGen
					button_activated = Steam_PullByte()
					If bg\closest_player <> mp_I\PlayerID Then
						bg\activated = button_activated
					EndIf
				Next
				
				mp_I\VotedMap = Steam_PullByte()
				For j = 0 To (MAX_VOTED_MAPS - 1)
					mp_I\MapsToVote[j] = Steam_PullString()
				Next
				;[End Block]
			Case PACKET_QUIT, PACKET_KICK
				;[Block]
				If currMSGSync = PACKET_KICK Then
					mp_I\ServerMSG = Steam_PullByte()
					If mp_I\ServerMSG = SERVER_MSG_KICK_KICKED Lor mp_I\ServerMSG = SERVER_MSG_KICK_BANNED Then
						mp_I\KickReason = Steam_PullString()
					EndIf
				EndIf
				
				LeaveMPGame()
				If currMSGSync <> PACKET_KICK Then
					mp_I\ServerMSG = SERVER_MSG_QUIT
				EndIf
				Return
				;[End Block]
			Case PACKET_RELOAD
				;[Block]
				CatchErrors("Uncaught (RecvDataClient)")
				NullMPGame(True,False)
				Return
				;[End Block]
		End Select
		getconn = Steam_LoadPacket()
	Wend
	IsPlayerSprinting = Players[mp_I\PlayerID]\IsPlayerSprinting
	Players[mp_I\PlayerID]\Name = mp_O\PlayerName
	
	CatchErrors("Uncaught (RecvDataClient)")
End Function

Function SyncServer()
	CatchErrors("SyncServer")
	Local getconn,i%,j%,k%,byte%
	Local it.Items,temp%,n.NPCs
	Local g.Guns,ch.ChatMSG,fb.FuseBox,ge.Generator,dbr.DamageBossRadius,pg.ParticleGen,lg.LeverGen,bg.ButtonGen
	Local pmp.ParticleMP
	Local hb.HitBox
	
	Players[mp_I\PlayerID]\Yaw = EntityYaw(mpl\CameraPivot)
	Players[mp_I\PlayerID]\Pitch = EntityPitch(mpl\CameraPivot)
	Players[mp_I\PlayerID]\IronSight = g_I\IronSight
	Players[mp_I\PlayerID]\CurrChunk = mp_I\Map\CurrChunk
	Players[mp_I\PlayerID]\IsReady = mp_I\IsReady
	Players[mp_I\PlayerID]\Name = mp_O\PlayerName
	Players[mp_I\PlayerID]\SendChatMSG = mp_I\SendChatMSG
	;Sending data to clients
	For i=1 To (mp_I\MaxPlayers-1)
		If Players[i]<>Null
			If Players[i]\FinishedLoading Then
				;A packet for determining the ping
				;[Block]
				If Players[i]\HasPinged Then
					Steam_PushByte(PACKET_PING)
					Players[i]\HasPinged = False
					Steam_SendPacketToUser(Players[i]\SteamIDUpper, Players[i]\SteamIDLower, k_EP2PSendUnreliableNoDelay)
				EndIf
				;[End Block]
				
				;Player data package
				;[Block]
				Steam_PushByte(PACKET_PLAYER)
				For j=0 To (mp_I\MaxPlayers-1)
					If Players[j]<>Null Then
						Steam_PushByte(1) ;The user ID is given
						Steam_PushByte(Players[j]\Team) ;CHECK FOR IMPLEMENTATION
						byte = (Players[j]\IsSpectator) + (2*(Players[j]\Team = Players[i]\Team))
						Steam_PushByte(byte)
						
						;CHECK FOR IMPLEMENTATION
						Players[j]\X = EntityX(Players[j]\Collider)
						Players[j]\Y = EntityY(Players[j]\Collider)
						Players[j]\Z = EntityZ(Players[j]\Collider)
						
						;Don't send this data back to the client who already has that data for their own
						If i<>j Then
							Steam_PushInt(Players[j]\Ping)
							Steam_PushString(Players[j]\Name)
							Steam_PushFloat(Players[j]\Yaw)
							Steam_PushFloat(Players[j]\Pitch)
							Steam_PushFloat(Players[j]\CurrSpeed)
							Steam_PushByte(Int((Players[j]\walkangle/360.0)*255.0))
							Steam_PushFloat(Players[j]\ShootState)
							Steam_PushFloat(Players[j]\ReloadState)
							Steam_PushFloat(Players[j]\DeployState)
							Steam_PushFloat(Players[j]\CrouchState)
							byte% = (Players[j]\walking) + (2*Players[j]\IsPlayerSprinting) + (4*Players[j]\Crouch) + (8*Players[j]\IronSight)
							byte = byte + (16*Players[j]\PressMouse1) + (32*Players[j]\PressReload) + (64*Players[j]\IsReady)
							Steam_PushByte(byte)
							Steam_PushByte(Players[j]\MapVote)
							If Players[j]\Team = Players[i]\Team Then
								Steam_PushByte(Players[j]\CurrChunk)
								Steam_PushByte(Players[j]\VoiceLine)
								If Players[j]\VoiceLine <> WHEEL_OUTPUT_UNKNOWN Then
									Steam_PushByte(Players[j]\VoiceLineNumber)
									If Players[j]\VoiceLine = COMMAND_OVERHERE Then
										Steam_PushByte(mpl\WheelLookHereSelectedIcon)
										If Players[j]\OverHerePosition <> Null Then
											Steam_PushFloat(Players[j]\OverHerePosition\x)
											Steam_PushFloat(Players[j]\OverHerePosition\y)
											Steam_PushFloat(Players[j]\OverHerePosition\z)
										Else
											For k = 0 To 2
												;Dummy float
												Steam_PushFloat(2147483647)
											Next
										EndIf
									EndIf
								EndIf
							EndIf
						Else
							Steam_PushByte(Players[j]\InjuryType)
							Players[j]\InjuryType = INJURY_DEFAULT
						EndIf
						Steam_PushFloat(Players[j]\X)
						Steam_PushFloat(Players[j]\Y)
						Steam_PushFloat(Players[j]\Z)
						Steam_PushFloat(Players[j]\CurrStamina)
						Steam_PushInt(Players[j]\StaminaEffectTimer)
						If i = j Lor Players[i]\Team = Players[j]\Team Then
							Steam_PushFloat(Players[j]\CurrHP)
							Steam_PushFloat(Players[j]\CurrKevlar)
						Else
							byte = 0
							If Players[j]\CurrHP > 0.0 Then
								byte = 1
							EndIf
							If Players[j]\CurrKevlar > 0.0 Then
								byte = byte + 2
							EndIf
							Steam_PushByte(byte)
							
;							If Players[j]\CurrHP > 0.0 Then
;								Steam_PushFloat(100.0)
;							Else
;								Steam_PushFloat(0.0)
;							EndIf
;							If Players[j]\CurrKevlar > 0.0 Then
;								Steam_PushFloat(100.0)
;							Else
;								Steam_PushFloat(0.0)
;							EndIf
						EndIf
						
						Steam_PushShort(Players[j]\Ammo[QUICKSLOT_PRIMARY])
						Steam_PushShort(Players[j]\Ammo[QUICKSLOT_SECONDARY])
						Steam_PushByte(Players[j]\ReloadAmmo[QUICKSLOT_PRIMARY])
						Steam_PushByte(Players[j]\ReloadAmmo[QUICKSLOT_SECONDARY])
						Steam_PushByte(Players[j]\SelectedSlot)
						For k=0 To MaxSlots-1
							Steam_PushByte(Players[j]\WeaponInSlot[k])
						Next
						If Players[j]\Item <> Null Then
							Steam_PushByte(1)
							Steam_PushString(Players[j]\Item\itemtemplate\name)
							Steam_PushString(Players[j]\Item\itemtemplate\tempname)
						Else
							Steam_PushByte(0)
						EndIf
						
						Steam_PushShort(Players[j]\Kills)
						Steam_PushShort(Players[j]\Deaths)
					Else
						Steam_PushByte(0) ;The user ID is not given
					EndIf
				Next
				Steam_SendPacketToUser(Players[i]\SteamIDUpper, Players[i]\SteamIDLower, k_EP2PSendUnreliableNoDelay)
				;[End Block]
				
				;Item syncing (package)
				;[Block]
				Steam_PushByte(PACKET_ITEM)
				temp = 0
				For it.Items = Each Items
					temp=temp+1
				Next
				Steam_PushInt(temp)
				For it.Items = Each Items
					Steam_PushInt(it\ID)
					Steam_PushString(it\itemtemplate\name)
					Steam_PushString(it\itemtemplate\tempname)
					Steam_PushFloat(EntityX(it\collider))
					Steam_PushFloat(EntityY(it\collider))
					Steam_PushFloat(EntityZ(it\collider))
					Steam_PushFloat(EntityYaw(it\collider))
				Next
				Steam_SendPacketToUser(Players[i]\SteamIDUpper, Players[i]\SteamIDLower, k_EP2PSendUnreliableNoDelay)
				;[End Block]
				
				;NPC syncing (package)
				;[Block]
				Steam_PushByte(PACKET_NPC)
				temp = 0
				
				For n.NPCs = Each NPCs
					If ShouldSyncNPC(n) Then
						temp=temp+1
					EndIf
				Next
				Steam_PushInt(temp)
				For n.NPCs = Each NPCs
					If ShouldSyncNPC(n) Then
						Steam_PushInt(n\ID)
						Steam_PushInt(n\NPCtype)
						Steam_PushFloat(EntityX(n\Collider))
						Steam_PushFloat(EntityY(n\Collider))
						Steam_PushFloat(EntityZ(n\Collider))
						Steam_PushFloat(EntityYaw(n\Collider))
						Steam_PushFloat(n\State[0])
						Steam_PushFloat(n\State[1])
						Steam_PushFloat(n\State[2])
						Steam_PushInt(n\HP)
						Steam_PushByte(n\ClosestPlayer)
					EndIf
				Next
				Steam_SendPacketToUser(Players[i]\SteamIDUpper, Players[i]\SteamIDLower, k_EP2PSendUnreliableNoDelay)
				;[End Block]
				
				;Effects syncing (package)
				;[Block]
				Steam_PushByte(PACKET_EFFECT)
				temp = 0
				For pmp = Each ParticleMP
					temp=temp+1
				Next
				Steam_PushInt(temp)
				
				For pmp = Each ParticleMP
					Steam_PushByte(pmp\ptype)
					Steam_PushFloat(pmp\pos\x)
					Steam_PushFloat(pmp\pos\y)
					Steam_PushFloat(pmp\pos\z)
					Steam_PushByte(pmp\playerID)
					Select pmp\ptype
						Case PARTICLEMP_WALL
							Steam_PushFloat(pmp\rot\x)
							Steam_PushFloat(pmp\rot\y)
							Steam_PushFloat(pmp\npos\x)
							Steam_PushFloat(pmp\npos\y)
							Steam_PushFloat(pmp\npos\z)
					End Select
				Next
				
				Steam_SendPacketToUser(Players[i]\SteamIDUpper, Players[i]\SteamIDLower, k_EP2PSendUnreliableNoDelay)
				;[End Block]
				
				;Chat message syncing (package)
				;[Block]
				Steam_PushByte(PACKET_CHATMSG)
				
				For j=0 To (mp_I\MaxPlayers-1)
					If Players[j]<>Null And i<>j And Players[j]\Team = Players[i]\Team Then
						Steam_PushByte(1)
						If Players[j]\SendChatMSG<>"" And (j<>mp_I\PlayerID Lor mp_I\ShouldSendMSG) Then
							Steam_PushString(Players[j]\SendChatMSG) ;Think about a sort of compression algorithm if needed, but currently it is not I think - ENDSHN
							Steam_PushByte(Players[j]\IsChatMSGTranslated)
						Else
							Steam_PushString("") ;Seems redundant at first, but that will just send 1 single Byte (a termination symbol), which is equivilant to WriteByte(mp_I\Server,0)
						EndIf
					Else
						Steam_PushByte(0)
					EndIf
				Next
				
				temp = 0
				For ch = Each ChatMSG
					If ch\IsServerMSG = SERVER_MSG_IS Then
						temp = temp + 1
					EndIf
				Next
				Steam_PushByte(temp)
				
				For ch = Each ChatMSG
					If ch\IsServerMSG = SERVER_MSG_IS Then
						Steam_PushByte(ch\MsgType)
						For j = 0 To (MAX_CHATMSG_PARTS - 1)
							Steam_PushString(ch\Msg[j])
						Next
						ch\IsServerMSG = SERVER_MSG_SENT
					EndIf
				Next
				
				Steam_SendPacketToUser(Players[i]\SteamIDUpper, Players[i]\SteamIDLower, k_EP2PSendUnreliableNoDelay)
				;[End Block]
				
				;Gamemode data that will be sent
				;[Block]
				Steam_PushByte(PACKET_GAMEMODEUTIL)
				
				Steam_PushInt(mp_I\Gamemode\EnemyCount)
				Steam_PushByte(mp_I\Gamemode\Phase)
				Steam_PushFloat(mp_I\Gamemode\PhaseTimer)
				Steam_PushByte(mp_I\Gamemode\DisableMovement)
				Steam_PushFloat(NoClipSpeed) ;Will be used for the spectator speed
				Steam_PushFloat(mp_I\ReadyTimer)
				For j = 0 To MaxPlayerTeams-1
					Steam_PushByte(mp_I\Gamemode\RoundWins[j])
				Next
				
				;It assumes that the amount of fuseboxes is automatically the same for each client (as it is depending on the map entirely)
				For fb = Each FuseBox
					Steam_PushByte(fb\fuses)
				Next
				For ge = Each Generator
					Steam_PushFloat(ge\progress)
				Next
				For dbr = Each DamageBossRadius
					Steam_PushFloat(dbr\timer)
				Next
				For pg = Each ParticleGen
					Steam_PushByte(pg\activated)
				Next
				For lg = Each LeverGen
					Steam_PushFloat(EntityRoll(lg\obj_handle, True))
				Next
				For bg = Each ButtonGen
					Steam_PushByte(bg\activated)
				Next
				
				Steam_PushByte(mp_I\VotedMap)
				For j = 0 To (MAX_VOTED_MAPS - 1)
					Steam_PushString(mp_I\MapsToVote[j])
				Next
				Steam_SendPacketToUser(Players[i]\SteamIDUpper, Players[i]\SteamIDLower, k_EP2PSendUnreliableNoDelay)
				;[End Block]
				
				;A packet that will be sent to signal the game has been restarted
				;[Block]
				If mp_I\ResetGame Then
					Steam_PushByte(PACKET_RELOAD)
					
					Steam_SendPacketToUser(Players[i]\SteamIDUpper, Players[i]\SteamIDLower, k_EP2PSendUnreliable)
				EndIf
				;[End Block]
				
				If (MilliSecs()-Players[i]\LastMsgTime>(mp_I\TimeOut*1000)) Then ;remove client after X seconds of inactivity: assume connection was unexpectedly lost
					DeletePlayerAsServer(Steam_GetPlayerIDUpper(), Steam_GetPlayerIDLower(), i, "user_timeout")
					Steam_CloseConnection(Steam_GetPlayerIDUpper(), Steam_GetPlayerIDLower())
				EndIf
			Else
				Players[i]\LastMsgTime = MilliSecs()
			EndIf
		EndIf
	Next
	
	For pmp = Each ParticleMP
		FreeVector3D(pmp\pos)
		FreeVector2D(pmp\rot)
		FreeVector3D(pmp\npos)
		Delete pmp
	Next
	
	For i = 0 To (mp_I\MaxPlayers-1)
		If Players[i] <> Null Then
			If Players[i]\OverHerePosition <> Null Then
				Delete Players[i]\OverHerePosition
			EndIf
		EndIf
	Next
	
	mp_I\SendChatMSG = "" ;Reset chat message (from SERVER)
	For i=0 To (mp_I\MaxPlayers-1)
		If Players[i]<>Null Then
			Players[i]\SendChatMSG = "" ;Reset chat message (from CLIENT)
			Players[i]\VoiceLine = WHEEL_OUTPUT_UNKNOWN
			Players[i]\VoiceLineNumber = 0
			If Players[i]\OverHerePosition <> Null Then
				Delete Players[i]\OverHerePosition
			EndIf
		EndIf
	Next
	
	;Update the serverlist every 10 seconds in order to tell the server list that the server is still alive
	If mp_O\LocalServer=False Then
		mp_I\ServerListUpdateTimer = mp_I\ServerListUpdateTimer + FPSfactor
		If mp_I\ServerListUpdateTimer >= 70*10 Then
			UpdateServer(Steam_GetPlayerIDLower(), Steam_GetPlayerIDUpper(), mp_I\PlayerCount)
		EndIf
	EndIf
	
	If mp_I\ResetGame Then
		CatchErrors("Uncaught (SyncServer)")
		NullMPGame(True,False)
		CreateHostPlayer()
		mp_I\ResetGame = False
		mp_I\Gamemode\EnemyCount = 0
		mp_I\Gamemode\Phase = 0
		mp_I\Gamemode\PhaseTimer = 0.0
		mp_I\Gamemode\TeamSwitched = False
		mp_I\IsReady = False
		mp_I\PlayerCount = 1
		LoadingServer()
		Return
	EndIf
	
	CatchErrors("Uncaught (SyncServer)")
End Function

Function SyncClient()
	CatchErrors("SyncClient")
	Local getconn,TempRead$,i%,k%,ttmp%,yaw#,fb.FuseBox,lg.LeverGen,bg.ButtonGen,ammo%,g.Guns
	
	Players[mp_I\PlayerID]\Yaw = EntityYaw(mpl\CameraPivot)
	Players[mp_I\PlayerID]\Pitch = EntityPitch(mpl\CameraPivot)
	Players[mp_I\PlayerID]\IronSight = g_I\IronSight
	Players[mp_I\PlayerID]\IsReady = mp_I\IsReady
	;[Block]
;	WriteByte mp_I\Server,PACKET_PLAYER
;	WriteByte mp_I\Server,mp_I\PlayerID
;	WriteLine mp_I\Server,Players[mp_I\PlayerID]\Name
;	WriteFloat mp_I\Server,Players[mp_I\PlayerID]\Yaw
;	WriteFloat mp_I\Server,Players[mp_I\PlayerID]\Pitch
;	WriteByte mp_I\Server,Int((Players[mp_I\PlayerID]\walkangle/360.0)*255.0)
;	WriteByte mp_I\Server,Players[mp_I\PlayerID]\WantsSlot
;	
;	Local byte% = (Players[mp_I\PlayerID]\walking) + (2*Players[mp_I\PlayerID]\PressSprint) + (4*Players[mp_I\PlayerID]\Crouch) + (8*Players[mp_I\PlayerID]\IronSight) + (16*Players[mp_I\PlayerID]\PressMouse1)
;	byte = byte + (32*Min(Players[mp_I\PlayerID]\PressReload+(Players[mp_I\PlayerID]\ReloadState > 0.0),1)) + (64*Players[mp_I\PlayerID]\IsReady)
;	WriteByte mp_I\Server,byte
;	
;	WriteByte mp_I\Server,mp_I\Map\CurrChunk
;	
;	;Some item syncing
;	If ClosestItem <> Null
;		WriteByte mp_I\Server,1
;		WriteInt mp_I\Server,ClosestItem\ID
;		WriteByte mp_I\Server,ClosestItem\Picked
;	Else
;		WriteByte mp_I\Server,0
;	EndIf
;	
;	WriteByte mp_I\Server,Players[mp_I\PlayerID]\Team+1
;	
;	If mp_I\SendChatMSG<>"" And mp_I\ShouldSendMSG Then
;		WriteLine mp_I\Server,mp_I\SendChatMSG
;	Else
;		WriteLine mp_I\Server,""
;	EndIf
;	;Reset chat message, so the chat won't be spammed with the same message automatically
;	mp_I\SendChatMSG = ""
;	
;	WriteInt mp_I\Server,Players[mp_I\PlayerID]\PingCheckValue
;	
;	SendUDPMsg(mp_I\Server,Players[0]\IP,Players[0]\Port)
	;[End Block]
	
	If mp_I\PingTimer > 70.0 Then
		;Resend the ping packet, as it assumes that it may have gotten lost
		Steam_PushByte(PACKET_PING)
		Steam_PushByte(mp_I\PlayerID)
		Steam_SendPacketToUser(Players[0]\SteamIDUpper, Players[0]\SteamIDLower, k_EP2PSendUnreliableNoDelay)
		mp_I\LastPingMillisecs = MilliSecs()
		mp_I\PingTimer = 0.0
	EndIf
	mp_I\PingTimer = mp_I\PingTimer + FPSfactor
	
	;Steam_PushInt(PACKET_HEADER_INT)
	Steam_PushByte(PACKET_PLAYER)
	Steam_PushByte(mp_I\PlayerID)
	Steam_PushString(Players[mp_I\PlayerID]\Name)
	Steam_PushFloat(Players[mp_I\PlayerID]\Yaw)
	Steam_PushFloat(Players[mp_I\PlayerID]\Pitch)
	Steam_PushFloat(EntityX(Players[mp_I\PlayerID]\Collider))
	Steam_PushFloat(EntityY(Players[mp_I\PlayerID]\Collider))
	Steam_PushFloat(EntityZ(Players[mp_I\PlayerID]\Collider))
	Steam_PushByte(Int((Players[mp_I\PlayerID]\walkangle/360.0)*255.0))
	Steam_PushByte(Players[mp_I\PlayerID]\WantsSlot)
	
	Local byte% = (Players[mp_I\PlayerID]\walking) + (2*Players[mp_I\PlayerID]\PressSprint) + (4*Players[mp_I\PlayerID]\Crouch) + (8*Players[mp_I\PlayerID]\IronSight)
	byte = byte + (16*Min(Players[mp_I\PlayerID]\PressMouse1+(Players[mp_I\PlayerID]\ShootState > 0.0),1)) + (32*Min(Players[mp_I\PlayerID]\PressReload+(Players[mp_I\PlayerID]\ReloadState > 0.0),1))
	byte = byte + (64*Players[mp_I\PlayerID]\IsReady)
	Steam_PushByte(byte)
	
	Steam_PushByte(Players[mp_I\PlayerID]\MapVote)
	
	ammo = 0
	For g = Each Guns
		If g\ID = Players[mp_I\PlayerID]\WeaponInSlot[Players[mp_I\PlayerID]\SelectedSlot] Then
			If (g\GunType <> GUNTYPE_MELEE) Then
				ammo = mp_I\LocalAmmo
			EndIf
			Exit
		EndIf
	Next
	Steam_PushShort(ammo)
	
	Steam_PushByte(mp_I\Map\CurrChunk)
	
	;Looks like weird code? Because IT IS! (Thank you Blitz3D)
	byte = 0
	If ClosestItem <> Null Then
		byte = 1
	EndIf
	If Players[mp_I\PlayerID]\Item <> Null Then
		byte = byte + 2
	EndIf
	Steam_PushByte(byte)
	
	;Has a general item been picked?
	If ClosestItem <> Null
		Steam_PushInt(ClosestItem\ID)
		Steam_PushByte(ClosestItem\Picked)
	EndIf
	
	;We are sending the data for all those parts, because it can always be possible that a slight miscalculation can happen for the server side when it comes to the closest player assigned
	For fb = Each FuseBox
		Steam_PushByte(fb\fuses)
	Next
	For lg = Each LeverGen
		Steam_PushFloat(EntityRoll(lg\obj_handle, True))
	Next
	For bg = Each ButtonGen
		Steam_PushByte(bg\activated)
	Next
	
	;Item slot syncing (tells server that the item in the slot is deleted)
	If Players[mp_I\PlayerID]\Item <> Null Then
		Steam_PushByte(Players[mp_I\PlayerID]\Item\isDeleted)
	EndIf
	
	Steam_PushByte(Players[mp_I\PlayerID]\VoiceLine)
	If Players[mp_I\PlayerID]\VoiceLine <> WHEEL_OUTPUT_UNKNOWN Then
		Steam_PushByte(Players[mp_I\PlayerID]\VoiceLineNumber)
		If Players[mp_I\PlayerID]\VoiceLine = COMMAND_OVERHERE Then
			Steam_PushByte(mpl\WheelLookHereSelectedIcon)
			If Players[mp_I\PlayerID]\OverHerePosition <> Null Then
				Steam_PushFloat(Players[mp_I\PlayerID]\OverHerePosition\x)
				Steam_PushFloat(Players[mp_I\PlayerID]\OverHerePosition\y)
				Steam_PushFloat(Players[mp_I\PlayerID]\OverHerePosition\z)
			Else
				For k = 0 To 2
					;Dummy float
					Steam_PushFloat(2147483647)
				Next
			EndIf
			Delete Players[mp_I\PlayerID]\OverHerePosition
		EndIf
	EndIf
	Players[mp_I\PlayerID]\VoiceLine = WHEEL_OUTPUT_UNKNOWN
	Players[mp_I\PlayerID]\VoiceLineNumber = 0
	
	;Steam_PushByte(Players[mp_I\PlayerID]\Team+1)
	Steam_PushByte(Players[mp_I\PlayerID]\WantsTeam) ;CHECK FOR IMPLEMENTATION
	
	If mp_I\SendChatMSG<>"" And mp_I\ShouldSendMSG Then
		Steam_PushString(mp_I\SendChatMSG)
		Steam_PushByte(mp_I\ShouldSendMSG - 1)
	Else
		Steam_PushString("")
	EndIf
	;Reset chat message, so the chat won't be spammed with the same message automatically
	mp_I\SendChatMSG = ""
	
	;TODO: Check if this is necessary, as Steam may probably have an own ping system
	Steam_PushInt(Players[mp_I\PlayerID]\Ping)
	
	Steam_SendPacketToUser(Players[0]\SteamIDUpper, Players[0]\SteamIDLower, k_EP2PSendUnreliableNoDelay)
	
	If Players[0]\FinishedLoading Then
		If (MilliSecs()-Players[0]\LastMsgTime>(mp_I\TimeOut*1000)) Then ;disconnect after X seconds of inactivity: assume connection was unexpectedly lost
			LeaveMPGame()
			mp_I\ServerMSG = SERVER_MSG_TIMEOUT
			Return
		Else
			;If (MilliSecs()-Players[0]\LastMsgTime)>(100.0) Then
			;	Color 255,255,0
			;	SetFont fo\ConsoleFont
			;	Text opt\GraphicWidth,20,"WARNING: Timeout in: "+TurnIntoSeconds((mp_I\TimeOut*1000)-(MilliSecs()-Players[0]\LastMsgTime)),2
			;EndIf
		EndIf
	EndIf
	
	CatchErrors("Uncaught (SyncClient)")
End Function

Function CountdownBeep(timer#,startAt%)
	If TurnIntoSeconds(timer) <> mp_I\CountdownSec Then
		If TurnIntoSeconds(timer) <= startAt Then
			PlaySound_Strict LoadTempSound("SFX\General\CountdownBeep.ogg")
			mp_I\CountdownSec = TurnIntoSeconds(timer)
		Else
			mp_I\CountdownSec = 0
		EndIf
	EndIf	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS