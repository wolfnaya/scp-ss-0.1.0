
Function Fillroom_Room3_Tunnel_2(r.Rooms)
	Local i
	
	r\Objects[0] = CreatePivot()
	PositionEntity (r\Objects[0], r\x, 1.0, r\z, True)
	
	r\Objects[1] = CreatePivot()
	PositionEntity (r\Objects[1], r\x, r\y+167*RoomScale, r\z+1000*RoomScale, True)
	
	r\Objects[2] = CreatePivot()
	PositionEntity (r\Objects[2], r\x-173*RoomScale, r\y+443*RoomScale, r\z+62*RoomScale, True)
	
	For i = 0 To 2
		EntityParent r\Objects[i],r\obj
	Next
	
End Function

Function UpdateEvent_Room3_Tunnel_2(e.Events)
	Local it.Items,n.NPCs
	
	If PlayerRoom = e\room Then
		
		PlayerFallingPickDistance = 0.0
		
		If EntityY(Collider) < -2000*RoomScale And KillTimer => 0 Then
			m_msg\DeathTxt=""
			PlaySound_Strict LoadTempSound("SFX\Room\PocketDimension\Impact.ogg")
			KillTimer=-1.0
		EndIf
		
		If e\EventState[0] = 0 Then
			If EntityDistanceSquared(Collider,e\room\Objects[0])<PowTwo(0.6) Then
				
				n = CreateNPC(NPC_scp_1048,EntityX(e\room\Objects[2],True),EntityY(e\room\Objects[2],True),EntityZ(e\room\Objects[2],True))
				n\State[0] = 1
				
				e\EventState[0] = 1
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D