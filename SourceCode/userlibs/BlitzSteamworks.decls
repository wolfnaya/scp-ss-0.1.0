.lib "BlitzSteamworks.dll"

Steam_Init%():"_Init@0"
Steam_Update():"_Update@0"
Steam_Shutdown():"_Shutdown@0"

Steam_Achieve%(ID$):"_Achieve@4"
Steam_UnAchieve%(ID$):"_UnAchieve@4"
Steam_SetStat%(ID$,stat#):"_BS_SetStat@8"
Steam_IsAchieved%(ID$):"_IsAchieved@4"

Steam_GetOverlayState%():"_GetOverlayState@0"
Steam_GetOverlayUpdated%():"_GetOverlayUpdated@0"
Steam_ActivateOverlay(dialog$):"_ActivateOverlay@4"
Steam_ActivateOverlayInvite(upperID%,lowerID%):"_ActivateOverlayInvite@8"
Steam_ActivateOverlayToUser(dialog$,upperID%,lowerID%):"_ActivateOverlayToUser@12"
Steam_GetFriendRelationship%(upperID%,lowerID%):"_GetFriendRelationship@8"

Steam_StringToIDUpper%(cid$):"_StringToIDUpper@4"
Steam_StringToIDLower%(cid$):"_StringToIDLower@4"

Steam_IDToString$(upperID%, lowerID%):"_IDToString@8"

Steam_GetPlayerIDUpper%():"_GetPlayerIDUpper@0"
Steam_GetPlayerIDLower%():"_GetPlayerIDLower@0"
Steam_GetPlayerName$():"_GetPlayerName@0"
Steam_GetCurrentGameLang$():"_GetCurrentGameLang@0"

Steam_PushByte(b%):"_PushByte@4"
Steam_PushShort(s%):"_PushShort@4"
Steam_PushInt%(i%):"_PushInt@4"
Steam_PushFloat#(f#):"_PushFloat@4"
Steam_PushString$(s$):"_PushString@4"

Steam_PullByte%():"_PullByte@0"
Steam_PullShort%():"_PullShort@0"
Steam_PullInt%():"_PullInt@0"
Steam_PullFloat#():"_PullFloat@0"
Steam_PullString$():"_PullString@0"

Steam_GetSenderIDUpper%():"_GetSenderIDUpper@0"
Steam_GetSenderIDLower%():"_GetSenderIDLower@0"

Steam_LoadPacket%():"_LoadPacket@0"
Steam_SendPacketToUser%(upperID%, lowerID%, flag%):"_SendPacketToUser@12"
Steam_CloseConnection%(upperID%, lowerID%):"_CloseConnection@8"

Steam_CreateLobby(type%,maxMembers%):"_BS_CreateLobby@8"
Steam_JoinLobby(upperID%,lowerID%):"_BS_JoinLobby@8"
Steam_LeaveLobby():"_BS_LeaveLobby@0"
Steam_SeekLobbyUpper%():"_BS_SeekLobbyUpper@0"
Steam_SeekLobbyLower%():"_BS_SeekLobbyLower@0"
Steam_FlushLobbyID():"_BS_FlushLobbyID@0"
Steam_GetIPCountry$():"_BS_GetIPCountry@0"

Steam_GetPingLocation$():"_BS_GetLocalPingLocation@0"
Steam_PingTimeFromHost%(str$):"_BS_EstimatePingTimeFromLocalHost@4"

Steam_GetNumLobbyMembers%():"_BS_GetNumLobbyMembers@0"

Steam_SetRichPresence(key$,value$):"_BS_SetRichPresence@8"

Steam_FilterText$(upperID%,lowerID%,str$):"_BS_FilterText@12"

Steam_GetConnectionQuality%(region$):"_GetConnectionQuality@4"
