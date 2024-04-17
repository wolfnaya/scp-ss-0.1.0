
Function FillRoom_Cont_966(r.Rooms)
	Local d.Doors,sc.SecurityCams,it.Items
	
	d = CreateDoor(r\zone, r\x - 400.0 * RoomScale, 0, r\z, -90, r, False, False, KEY_CARD_3)
	d = CreateDoor(r\zone, r\x, 0, r\z - 480.0 * RoomScale, 180, r, False, False, KEY_CARD_3)
	d = CreateDoor(r\zone, r\x,r\y,r\z+277*RoomScale,0,r,True,DOOR_ONE_SIDED,KEY_CARD_3)
	
	sc.SecurityCams = CreateSecurityCam(r\x-312.0 * RoomScale, r\y + 450*RoomScale, r\z + 656*RoomScale, r)
	sc\angle = 225
	sc\turn = 45
	TurnEntity(sc\CameraObj, 20, 0, 0)
	sc\ID = 9
	
	r\Objects[0] = CreatePivot(r\obj)
	PositionEntity(r\Objects[0], r\x, 0.5, r\z + 512.0 * RoomScale, True)
	
	r\Objects[1] = CreatePivot(r\obj)
	PositionEntity(r\Objects[1], r\x + 64.0 * RoomScale, 0.5, r\z - 640.0 * RoomScale, True)
	
	r\Objects[2] = CreatePivot(r\obj)
	PositionEntity(r\Objects[2], r\x, 0.5, r\z, True)
	
	r\Objects[3] = CreatePivot(r\obj)
	PositionEntity(r\Objects[3], r\x + 320.0 * RoomScale, 0.5, r\z + 704.0 * RoomScale, True)
	
	it = CreateItem(GetLocalString("Item Names","nvg"), "nvg", r\x + 320.0 * RoomScale, 0.5, r\z + 704.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
End Function

Function UpdateEvent_Cont_966(e.Events)
	
	If PlayerRoom = e\room Then
		Select e\EventState[0]
			Case 0
				;a dirty workaround to hide the pause when loading 966 model
				If QuickLoadPercent = -1
					e\EventState[0] = 1
					QuickLoadPercent = 0
					QuickLoad_CurrEvent = e
				EndIf
			Case 2
				e\EventState[0] = 2
				RemoveEvent(e)
		End Select
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D