
Function UpdateEvent_682_Roar(e.Events)
	
	If e\EventState[0] = 0 Then
		If PlayerRoom = e\room Then e\EventState[0] = 70 * Rand(300,1000)
	ElseIf PlayerRoom\RoomTemplate\Name <> "pocketdimension" And PlayerRoom\RoomTemplate\Name <> "testroom_860" Then
		e\EventState[0] = e\EventState[0]-FPSfactor
		
		If e\EventState[0] < 17*70 Then
			If	e\EventState[0]+FPSfactor => 17*70 Then LoadEventSound(e,"SFX\SCP\682\Roar.ogg") : e\SoundCHN[0] = PlaySound_Strict(e\Sound[0])
			If e\EventState[0] > 17*70 - 3*70 Then CameraShake = 0.5
			If e\EventState[0] < 17*70 - 7.5*70 And e\EventState[0] > 17*70 - 11*70 Then CameraShake = 2.0				
			If e\EventState[0] < 70 Then 
				If e\Sound[0]<>0 Then FreeSound_Strict (e\Sound[0]) 
				RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D