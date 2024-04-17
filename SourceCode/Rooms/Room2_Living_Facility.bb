Function FillRoom_Room2_Living_Facility(r.Rooms)

	r\Objects[0] = CreateButton(r\x - 1000.0*RoomScale, r\y + 160.0 * RoomScale, r\z + 384.0 * RoomScale, 0,90,0)
	EntityParent (r\Objects[0],r\obj)
	
	r\Objects[1] = CreateButton(r\x - 1000.0*RoomScale, r\y + 928.0 * RoomScale, r\z + 384.0 * RoomScale, 0,90,0)
	EntityParent (r\Objects[1],r\obj)
	
	r\Objects[2] = CreateButton(r\x - 1000.0*RoomScale, r\y + 1344.0 * RoomScale, r\z + 384.0 * RoomScale, 0,90,0)
	EntityParent (r\Objects[2],r\obj)
	
	r\Objects[3] = CreatePivot(r\obj)
	PositionEntity (r\Objects[3], r\x + 660.0*RoomScale, r\y, r\z + 496.0*RoomScale, True)
	
	r\Objects[4] = CreatePivot(r\obj)
	PositionEntity (r\Objects[4], r\x + 966.0*RoomScale, r\y, r\z - 925.0*RoomScale, True)
	
End Function

Function UpdateEvent_Room2_Living_Facility(e.Events)
	Local n.NPCs
	
	If PlayerRoom=e\room Then
		LightVolume = Min(LightVolume+0.025,TempLightVolume*2.0)
		UpdateButton(e\room\Objects[0])
		If d_I\ClosestButton = e\room\Objects[0] And keyhituse Then
			CreateMsg(GetLocalString("Doors", "elevator_broken"))
			PlaySound2(ButtonSFX[1], Camera, e\room\Objects[0])
			keyhituse=0
		EndIf
		
		If e\EventState[0] = 0 Then
			For n = Each NPCs
				If n\NPCtype = NPCtypeNTF Then
					If n\PrevState = 2 Then
						e\room\NPC[0] = n
						Exit
					EndIf	
				EndIf
			Next
			e\EventState[0] = 1
		EndIf
		If e\room\NPC[0] <> Null Then
			If e\EventState[1] = 0 Then
				If EntityDistanceSquared(e\room\NPC[0]\Collider,e\room\Objects[3]) < 5.0 Then
					PlayNewDialogue(20,%1011)
					e\EventState[1] = 1
				EndIf
			EndIf
			If e\EventState[2] = 0 Then
				If EntityDistanceSquared(e\room\NPC[0]\Collider,e\room\Objects[4]) < 5.0 Then
					PlayNewDialogue(21,%111011)
					e\EventState[2] = 1
				EndIf
			EndIf
		EndIf	
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D