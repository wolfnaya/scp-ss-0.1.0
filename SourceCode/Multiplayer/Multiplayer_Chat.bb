
Const SERVER_MSG_NO = 0
Const SERVER_MSG_IS = 1
Const SERVER_MSG_SENT = 2

Const MAX_CHATMSG_PARTS = 5

Const CHATMSG_TYPE_DEFAULT = 0
Const CHATMSG_TYPE_TRANSLATED = 1
Const CHATMSG_TYPE_ONEPARAM_TRANSLATE = 2
Const CHATMSG_TYPE_TWOPARAM_TRANSLATE = 3
Const CHATMSG_TYPE_THREEPARAM_TRANSLATE = 4

Type ChatMSG
	Field Msg$[MAX_CHATMSG_PARTS]
	Field MsgType%
	Field SteamIDUpper%
	Field SteamIDLower%
	Field PlayerName$
	Field PlayerID%
	Field MsgID%
	Field IsServerMSG%
	Field Time#
End Type

Function CreateChatMSG(isTranslated% = False)
	Local cmsg.ChatMSG
	
	If mp_I\CurrChatMSG <> "" Then
		mp_I\SendChatMSG = mp_I\CurrChatMSG
		mp_I\CurrChatMSG = ""
		CursorPos = 0
		If Left(mp_I\SendChatMSG,1)="/" ;Player entered a command (or at least tried)
			;Do nothing for now
			mp_I\ShouldSendMSG = False
		Else ;A message has been sent
			mp_I\ShouldSendMSG = (isTranslated + 1)
			AddChatMSG(mp_I\SendChatMSG, mp_I\PlayerID, SERVER_MSG_NO, isTranslated)
		EndIf
	EndIf
	
End Function

Function AddChatMSG.ChatMSG(first_msg$, playerID%, isServerMSG% = SERVER_MSG_NO, msg_type% = CHATMSG_TYPE_DEFAULT)
	Local cmsg.ChatMSG
	
	If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then Return
	
	cmsg = New ChatMSG
	cmsg\PlayerID = playerID
	If Players[playerID] <> Null Then
		cmsg\SteamIDLower = Players[playerID]\SteamIDLower
		cmsg\SteamIDUpper = Players[playerID]\SteamIDUpper
		cmsg\PlayerName = Players[playerID]\Name
	EndIf
	cmsg\Msg[0] = first_msg
	cmsg\MsgType = msg_type
	cmsg\IsServerMSG = isServerMSG
	cmsg\MsgID = mp_I\ChatMSGID
	mp_I\ChatMSGID = mp_I\ChatMSGID + 1
	
	Return cmsg
End Function

Function AssembleChatMSG$(cmsg.ChatMSG)
	
	Select cmsg\MsgType
		Case CHATMSG_TYPE_DEFAULT
			Return cmsg\Msg[0]
		Case CHATMSG_TYPE_TRANSLATED
			Return GetLocalString("Chat", cmsg\Msg[0])
		Case CHATMSG_TYPE_ONEPARAM_TRANSLATE
			Return GetLocalStringR("Chat", cmsg\Msg[0], cmsg\Msg[1])
		Case CHATMSG_TYPE_TWOPARAM_TRANSLATE
			Return cmsg\Msg[1] + " " + GetLocalStringR("Chat", cmsg\Msg[0], cmsg\Msg[2])
		Case CHATMSG_TYPE_THREEPARAM_TRANSLATE
			Return GetLocalStringR("Chat", cmsg\Msg[0], cmsg\Msg[1]) + " " + cmsg\Msg[2] + " " + GetLocalStringR("Chat", cmsg\Msg[3], cmsg\Msg[4])
	End Select
	
	Return ""
End Function

Function UpdateChat()
	Local width# = 500.0 * MenuScale
	Local height# = 30.0 * MenuScale
	Local x# = 30.0 * MenuScale
	Local y# = opt\GraphicHeight - height * 5
	Local cmsg.ChatMSG
	
	If InLobby() Then
		Return
	EndIf
	
	For cmsg = Each ChatMSG
		If cmsg\Time >= 70*10.0 Then
			Delete cmsg
		Else
			cmsg\Time = cmsg\Time + FPSfactor
		EndIf
	Next
	
	If MenuOpen Lor AttachmentMenuOpen Lor ConsoleOpen Lor IsPlayerListOpen() Lor IsModerationOpen() Lor (Not HUDenabled) Then
		Return
	EndIf
	
	If (Not mp_I\ChatOpen) Then
		If KeyHit(kb\ChatKey) Then
			mp_I\ChatOpen = True
			DeleteMenuGadgets()
			ResetInput()
			SelectedInputBox = 1
		EndIf
	Else
		mp_I\CurrChatMSG = InputBox(x, y, width, height, mp_I\CurrChatMSG, 1, 50)
		
		If DrawButton(x + width, y + (height - 30.0 * MenuScale), 30.0 * MenuScale, 30.0 * MenuScale, Chr(62), False, False) Lor (SelectedInputBox=1 And KeyHit(28)) Then
			If mp_I\CurrChatMSG <> "" Then
				CreateChatMSG()
				SelectedInputBox = 1
				mp_I\ChatOpen = False
				DeleteMenuGadgets()
				ResetInput()
			EndIf
		EndIf
		
		If InteractHit(1,CK_Pause) Then
			mp_I\ChatOpen = False
			DeleteMenuGadgets()
			ResetInput()
		EndIf
	EndIf
	
End Function

Function RenderChat()
	Local width# = 500.0 * MenuScale
	Local height# = 30.0 * MenuScale
	Local x# = 30.0 * MenuScale
	Local y# = opt\GraphicHeight - height * 5
	Local cmsg.ChatMSG, ChatMSGAmount%, ChatBaseY#, ChatExtraSpace#
	
	If InLobby() Lor MenuOpen Lor AttachmentMenuOpen Lor ConsoleOpen Lor IsPlayerListOpen() Lor IsModerationOpen() Lor (Not HUDenabled) Then
		Return
	EndIf
	
	SetFont fo\Font[Font_Default]
	
	For cmsg = Each ChatMSG
		ChatMSGAmount = ChatMSGAmount + 1
	Next
	
	;Temporary, will probably be removed/tweaked once a scrollbar exists
	While ChatMSGAmount > 8
		Delete First ChatMSG
		ChatMSGAmount = ChatMSGAmount - 1
	Wend
	
	ChatBaseY = y + (height - 40.0 * MenuScale) - (30.0 * MenuScale) * (ChatMSGAmount * 2) - (10.0 * MenuScale) * (ChatMSGAmount - 1)
	ChatMSGAmount = 1
	For cmsg = Each ChatMSG
		Color 200,200,200
		If cmsg\PlayerID = 0 Then Color 100,100,255
		If cmsg\IsServerMSG > SERVER_MSG_NO Then Color 255,255,0
		If cmsg\IsServerMSG = SERVER_MSG_NO Then
			Text x, (ChatBaseY + (30.0 * MenuScale) * (ChatMSGAmount - 1)) + ChatExtraSpace, cmsg\PlayerName + ":"
		Else
			Text x, (ChatBaseY + (30.0 * MenuScale) * (ChatMSGAmount - 1)) + ChatExtraSpace, GetLocalString("Chat", "server") + ":"
		EndIf
		
		Color 255,255,255
		If cmsg\IsServerMSG > SERVER_MSG_NO Then Color 255,255,0
		Text x, (ChatBaseY + (30.0 * MenuScale) * ChatMSGAmount) + ChatExtraSpace, Steam_FilterText(cmsg\SteamIDUpper, cmsg\SteamIDLower, AssembleChatMSG(cmsg))
		ChatMSGAmount = ChatMSGAmount + 2
		ChatExtraSpace = ChatExtraSpace + 10.0 * MenuScale
	Next
	
End Function

;~IDEal Editor Parameters:
;~F#D
;~C#Blitz3D