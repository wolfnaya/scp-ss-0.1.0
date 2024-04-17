
Function FillRoom_Gate_B_Entrance(r.Rooms)
	
	
	
End Function

Function UpdateEvent_Gate_B_Entrance(e.Events)
	
	If RemoteDoorOn=False Then
		e\room\RoomDoors[4]\locked=True
	ElseIf RemoteDoorOn And e\EventState[2]=0
		e\room\RoomDoors[4]\locked=False
		If e\room\RoomDoors[4]\open Then 
			If e\room\RoomDoors[4]\openstate > 50 Lor EntityDistanceSquared(Collider, e\room\RoomDoors[4]\frameobj)<PowTwo(0.5) Then
				e\room\RoomDoors[4]\openstate = Min(e\room\RoomDoors[4]\openstate,50)
				e\room\RoomDoors[4]\open = False
				PlaySound2 (LoadTempSound("SFX\Door\DoorError.ogg"), Camera, e\room\RoomDoors[4]\frameobj)
			EndIf							
		EndIf
	Else
		e\room\RoomDoors[4]\locked=False
		
		e\EventState[1] = UpdateElevators(e\EventState[1], e\room\RoomDoors[0], e\room\RoomDoors[1], e\room\Objects[8], e\room\Objects[9], e)
		
		EntityAlpha Fog, 1.0						
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D