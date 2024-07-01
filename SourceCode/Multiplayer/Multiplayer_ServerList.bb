
Const ServerAddress$ = "thirdsubdivision.com"
Const PhpAddress$ = "serverlist.php"
Const Protocol$ = "http"
Const UserAgent$ = "SCPSS"

Type Server
	Field ID%
	Field id_lower%
	Field id_upper%
	Field region$
	Field name$
	Field password%
	Field gamemode$
	Field map$
	Field pl_on%
	Field pl_max%
End Type

Function RequestServer(command$="")
	
	mp_I\Offline = False
	Local tcp = OpenTCPStream(ServerAddress, GetPortForProtocol(Protocol))
	If tcp<>0 Then
		WriteLine tcp, "GET "+Protocol+"://"+ServerAddress$+"/"+PhpAddress$+"/?action="+command+" HTTP/1.1"
		WriteLine tcp, "Host: "+ServerAddress
		WriteLine tcp, "User-Agent: "+UserAgent+"/"+VersionNumber
		WriteLine tcp, "Connection: Close"
		WriteLine tcp, ""
		
		CloseTCPStream(tcp)
	Else
		mp_I\Offline = True
		mp_I\ServerMSG = SERVER_MSG_OFFLINE
	EndIf
	
End Function

Function ListServers()
	Local se.Server
	Local l$ = ""
	Local category$, order$ = ""
	
	Delete Each Server
	mp_I\ServerListAmount = 1
	
	mp_I\Offline = False
	Local tcp = OpenTCPStream(ServerAddress, GetPortForProtocol(Protocol))
	If tcp<>0 Then
		Select (mp_I\ServerListSort - (mp_I\ServerListSort Mod 2))
			Case SERVER_LIST_SORT_NAME
				category = "name"
			Case SERVER_LIST_SORT_GAMEMODE
				category = "gamemode"
			Case SERVER_LIST_SORT_MAP
				category = "map"
			Case SERVER_LIST_SORT_PLAYERS
				category = "pl_on"
		End Select
		If (mp_I\ServerListSort Mod 2) = 1 Then
			order = "&desc"
		EndIf
		
		WriteLine tcp, "GET "+Protocol+"://"+ServerAddress$+"/"+PhpAddress$+"/?action=listservers&sort="+category+order+" HTTP/1.1"
		WriteLine tcp, "Host: "+ServerAddress
		WriteLine tcp, "User-Agent: "+UserAgent+"/"+VersionNumber
		WriteLine tcp, "Connection: Close"
		WriteLine tcp, ""
		
		While (Not Eof(tcp))
			l = ReadLine(tcp)
			If Piece(l,1,"|")=UserAgent+"/"+VersionNumber Then
				se = New Server
				se\ID = mp_I\ServerListAmount
				mp_I\ServerListAmount = mp_I\ServerListAmount + 1
				se\id_lower = Piece(l,2,"|")
				se\id_upper = Piece(l,3,"|")
				se\region = Piece(l,4,"|")
				
				se\name = Piece(l,5,"|")
				se\password = Piece(l,6,"|")
				se\gamemode = Piece(l,7,"|")
				se\map = Piece(l,8,"|")
				se\pl_on = Piece(l,9,"|")
				se\pl_max = Piece(l,10,"|")
				;debuglog se\id_lower
				;debuglog se\id_upper
				;debuglog se\region
				;debuglog se\name
				;debuglog se\password
				;debuglog se\gamemode
				;debuglog se\map
				;debuglog se\pl_on
				;debuglog se\pl_max
			EndIf
		Wend
		
		CloseTCPStream(tcp)
	Else
		mp_I\ServerMSG = SERVER_MSG_OFFLINE
		mp_I\Offline = True
	EndIf
	
End Function

Function AddServer(version$,id_lower%,id_upper%,region$,name$,password%,gamemode$,map$,pl_on%,pl_max%)
	If mp_O\LocalServer Then Return
	
	name = Replace(name, " ", "%20")
	password = Replace(password, " ", "%20")
	map = Replace(map, " ", "%20")
	gamemode = Replace(gamemode, " ", "%20")
	If password = "" Then password = "0"
	If name = "" Then name = "Unnamed"
	If map = "" Then map = "Unknown"
	If gamemode = "" Then gamemode = "Unknown"
	
	RequestServer("addserver&version="+UserAgent+"/"+version+"&id_lower="+id_lower+"&id_upper="+id_upper+"&region="+region+"&name="+name+"&password="+password+"&gamemode="+gamemode+"&map="+map+"&pl_on="+pl_on+"&pl_max="+pl_max)
	
End Function

Function RemoveServer(id_lower%,id_upper%)
	
	If mp_O\LocalServer Lor mp_I\PlayState = GAME_CLIENT Then Return
	RequestServer("removeserver&id_lower="+id_lower+"&id_upper="+id_upper)
	
End Function

Function UpdateServer(id_lower%,id_upper%,pl_on%) ;id_lower%,id_upper%,gamemode%,map$,pl_on%
	
	If mp_O\LocalServer Lor mp_I\PlayState = GAME_CLIENT Then Return
	;RequestServer("updateserver&id_lower="+id_lower+"&id_upper="+id_upper+"&gamemode="+gamemode+"&map="+map+"&pl_on="+pl_on)
	RequestServer("updateserver&id_lower="+id_lower+"&id_upper="+id_upper+"&pl_on="+pl_on)
	mp_I\ServerListUpdateTimer = 0.0
	
End Function

Function GetPortForProtocol(proto$)
	
	Select proto
		Case "ftp"
			Return 21
		Case "ssh"
			Return 22
		Case "http"
			Return 80
		Case "https"
			Return 443
		Default
			Return -1 ;Wrong protocol / protocol not found
	End Select
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D