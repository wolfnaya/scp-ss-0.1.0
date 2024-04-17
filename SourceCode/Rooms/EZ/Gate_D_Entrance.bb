
Function FillRoom_Gate_D_Entrance(r.Rooms)
	
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x+1048.0*RoomScale, 0, r\z+656.0*RoomScale, True)
	EntityParent r\Objects[0], r\obj
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x, 0, r\z + 1008.0 * RoomScale, 0, r, False, DOOR_CONTAINMENT, KEY_CARD_5)
	r\RoomDoors[1]\dir = 1 : r\RoomDoors[1]\AutoClose = False
	PositionEntity(r\RoomDoors[1]\buttons[1], r\x+416*RoomScale, r\y + 0.7, r\z+1200.0*RoomScale, True)
	RotateEntity r\RoomDoors[1]\buttons[1],0,r\angle-90,0,True
	r\RoomDoors[1]\buttons[0] = FreeEntity_Strict(r\RoomDoors[1]\buttons[0])
	r\RoomDoors[1]\MTFClose = False
	
	r\Objects[2] = CreateButton(r\x,r\y+160*RoomScale,r\z+1760*RoomScale,0,0,0)
	EntityParent r\Objects[2], r\obj
	
	CreateDarkSprite(r, 3)
	
	r\RoomDoors[2] = CreateDoor(r\zone,r\x-688*RoomScale,r\y,r\z-404.0*RoomScale,270,r,False,DOOR_OFFICE)
	
	r\RoomDoors[3] = CreateDoor(r\zone,r\x+688*RoomScale,r\y,r\z+528.0*RoomScale,90,r,False,DOOR_OFFICE)
	r\RoomDoors[3]\locked = True
	r\RoomDoors[4] = CreateDoor(r\zone,r\x-1787*RoomScale,r\y,r\z-602.0*RoomScale,0,r,False,DOOR_OFFICE)
	r\RoomDoors[4]\locked = True
	
End Function

Function UpdateEvent_Gate_D_Entrance(e.Events)
	Local i%, tex%
	Local n.NPCs
	If gopt\GameMode <> GAMEMODE_NTF Then
		If PlayerRoom = e\room
			If e\EventState[2] = 0 Then
				e\EventState[2] = 1
			EndIf
			If e\EventState[2] = 1 And e\room\RoomDoors[1]\open = True Then
				e\SoundCHN[0] = PlaySound2(LoadTempSound("SFX\Door\BigDoorStartsOpenning.ogg"),Camera,e\room\RoomDoors[1]\frameobj,10,2.5)
				StopChannel(e\room\RoomDoors[1]\SoundCHN)
				e\EventState[2] = 2
			ElseIf e\EventState[2] = 2 Then
				UpdateSoundOrigin(e\SoundCHN[0],Camera,e\room\RoomDoors[1]\frameobj,10,2.5)
				e\room\RoomDoors[1]\openstate = 0.01
				e\room\RoomDoors[1]\open = False	
			EndIf
			If e\EventState[2] = 2 And (Not ChannelPlaying(e\SoundCHN[0])) Then
				OpenCloseDoor(e\room\RoomDoors[1])
				e\EventState[2] = 3
			EndIf
			
			If ecst\UnlockedGateDoors Then
				e\room\RoomDoors[1]\locked = False : e\room\RoomDoors[1]\open = True
			Else
				e\room\RoomDoors[1]\locked = True
			EndIf
			
			If e\EventState[1] = 0 Then
				If TaskExists(TASK_GET_TOPSIDE) And (Not ecst\UnlockedGateDoors) Then
					FailTask(TASK_GET_TOPSIDE)
					BeginTask(TASK_FIND_ROOM1_O5)
					e\EventState[1] = 1
				EndIf
			EndIf
			
			If e\EventState[3] = 0 Then
				UpdateButton(e\room\Objects[2])
				If d_I\ClosestButton = e\room\Objects[2] Then
					If keyhituse Then
						e\EventState[4] = 1.0
					EndIf
				EndIf
			EndIf
			
			If e\EventState[4] > 0 Then
				e\EventState[3] = e\EventState[3] + (FPSfactor*0.01)
			EndIf
			
			EntityAlpha e\room\Objects[3],Min(e\EventState[3],1.0)
			
			If e\EventState[3] > 1.05 Then
				SelectedEnding = "d1"
				psp\IsShowingHUD = False
				RemoveEvent(e)
				Return
			EndIf
			
		EndIf
		
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D