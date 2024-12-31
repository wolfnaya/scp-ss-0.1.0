; This is the Key.bb file that is used to verify a connection from one SCP - Nine Tailed Fox instance to another, mainly to prevent people to join a server that has been modified via code.
; For modders: This file is necessary for the key authentication mechanism. In here you can define your own key.
; 
; The server uses Key_GenerateSalt and sends the returned value to the client that wants to connect.
; The client receives the value and does Key_Encode on the key and the salt and then sends the returned hash value to the server.
; The server receives the value and does Key_Encode on the key and the same salt to check if the hash received by the server is matching the one from the client.

;The function that returns the encryption key. DO NOT SHARE THIS PUBLICLY! It is important to maintain the key's value a secret!
Function ENCRYPTION_KEY$()
	
	Return "1112scp-ss19mp78key3948[_,.ss01]"
	
End Function

;The authentication function
Function Key_GenerateSalt$()
	Local temp$ = ""
	Local i%
	
	SeedRnd MilliSecs()
	For i = 0 To 11
		temp = temp + Chr(Rand(32, 127))
	Next
	
	Return temp
End Function

;The key encoding function
Function Key_Encode$(encrypt_key$, secret$)
	
	Return BlitzHashDigestSHA3_256(secret + encrypt_key)
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS