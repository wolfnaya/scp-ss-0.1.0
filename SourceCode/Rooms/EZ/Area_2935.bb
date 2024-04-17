Function FillRoom_Area_2935_Entrance(r.Rooms)
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x, 0.0, r\z + 270.0 * RoomScale, 0, r, False, DOOR_CONTAINMENT, KEY_CARD_5)
	
	r\Objects[0] = CreatePivot()
	PositionEntity r\Objects[0],r\x+20*RoomScale,1.0,r\z+1047*RoomScale,True; ~ 1st tram
	EntityParent r\Objects[0], r\obj
	
	r\Objects[2] = CreatePivot()
	PositionEntity r\Objects[2],r\x,r\y-3130*RoomScale,r\z+925*RoomScale,True; ~ arrival 1
	EntityParent r\Objects[2], r\obj
	
	r\Objects[3] = CreatePivot()
	PositionEntity r\Objects[3],r\x-12*RoomScale,r\y-3130*RoomScale,r\z+174*RoomScale,True; ~ 2nd tram
	EntityParent r\Objects[3], r\obj
	
	r\Objects[4] = CreatePivot()
	PositionEntity r\Objects[4],r\x,1.0,r\z,True; ~ arrival 2
	EntityParent r\Objects[4], r\obj
	
	r\Objects[5] = CreatePivot()
	PositionEntity r\Objects[5],r\x+26*RoomScale,r\y-3130*RoomScale,r\z+2391*RoomScale,True; ~ Entering point
	EntityParent r\Objects[5], r\obj
	
	CreateDarkSprite(r, 1)
	
End Function

Function UpdateEvent_Area_2935_Entrance(e.Events)
	
	If PlayerRoom = e\room Then
		
		If gopt\GameMode <> GAMEMODE_NTF Then
		
			If e\EventState[0] = 0 Then
				e\EventState[1] = 0
			EndIf
			If e\EventState[2] = 0 Then
				e\EventState[3] = 0
			EndIf
			
			If EntityDistanceSquared(Collider, e\room\Objects[0])<PowTwo(1.0) Then
				e\EventState[0] = 1
			EndIf
			
			If e\EventState[0] = 1 Then
				e\EventState[1] = e\EventState[1] + (FPSfactor*0.01)
				EntityAlpha e\room\Objects[1],Min(e\EventState[1],1.0)
			EndIf
			
			If e\EventState[1] > 1.05 Then
				TeleportEntity(Collider, EntityX(e\room\Objects[2], True), EntityY(e\room\Objects[2], True), EntityZ(e\room\Objects[2], True))
				e\EventState[0] = 0
				EntityAlpha e\room\Objects[1],0
			EndIf
			
			If EntityDistanceSquared(Collider, e\room\Objects[3])<PowTwo(1.0) Then
				e\EventState[2] = 1
			EndIf
			
			If e\EventState[2] = 1 Then
				e\EventState[3] = e\EventState[3] + (FPSfactor*0.01)
				EntityAlpha e\room\Objects[1],Min(e\EventState[3],1.0)
			EndIf
			
			If e\EventState[3] > 1.05 Then
				TeleportEntity(Collider, EntityX(e\room\Objects[4], True), EntityY(e\room\Objects[4], True), EntityZ(e\room\Objects[4], True))
				e\EventState[2] = 0
				EntityAlpha e\room\Objects[1],0
			EndIf
			
			; ~ Event with Entrance to SCP itself
			
			If EntityDistanceSquared(Collider, e\room\Objects[5])<PowTwo(1.0) Then
				e\EventState[3] = 1
			EndIf
			
			If e\EventState[3] = 1 Then
				e\EventState[4] = e\EventState[4] + (FPSfactor*0.01)
				EntityAlpha e\room\Objects[1],Min(e\EventState[4],1.0)
			EndIf
			
			If e\EventState[4] > 1.05 Then ; ~ TODO: Add Area 2935! :)
				
				SelectedEnding = "scp-2935"
				psp\IsShowingHUD = False
				RemoveEvent(e)
				Return
				
	;			SaveGame(SavePath + CurrSave\Name + "\")
	;			
	;			gopt\CurrZone = AREA_2935
	;			
	;			ResetControllerSelections()
	;			DropSpeed = 0
	;			NullGame(True,False)
	;			LoadEntities()
	;			LoadAllSounds()
	;			Local zonecache% = gopt\CurrZone
	;			If FileType(SavePath + CurrSave\Name + "\" + gopt\CurrZone + ".sav") = 1 Then
	;				LoadGame(SavePath + CurrSave\Name + "\", gopt\CurrZone)
	;				InitLoadGame()
	;			Else
	;				InitNewGame()
	;				LoadDataForZones(SavePath + CurrSave\Name + "\")
	;			EndIf
	;			gopt\CurrZone = zonecache
	;			MainMenuOpen = False
	;			FlushKeys()
	;			FlushMouse()
	;			FlushJoy()
	;			ResetInput()
	;			
	;			SaveGame(SavePath + CurrSave\Name + "\")
	;			Return
			EndIf
			
		EndIf
		
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D