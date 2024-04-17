
Function UpdateEvent_Distant_Explosion(e.Events)
	
	If e\EventState[0] = 0 Then
		If PlayerRoom = e\room Then e\EventState[0] = 70 * Rand(17,20)
	ElseIf PlayerRoom\RoomTemplate\Name <> "pocketdimension" And PlayerRoom\RoomTemplate\Name <> "testroom_860" Then
		e\EventState[0] = e\EventState[0] - FPSfactor
		
		If e\EventState[0] < 17*70 Then
			If e\EventState[0] + FPSfactor => 17*70 Then LoadEventSound(e,"SFX\General\Distant_Explosion.ogg") : e\SoundCHN[0] = PlaySound_Strict(e\Sound[0])
			If e\EventState[0] > 17*70 - 3*70 Then BigCameraShake = 4.0
			If e\EventState[0] < 70 Then 
				If e\Sound[0]<>0 Then FreeSound_Strict (e\Sound[0]) 
				RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D