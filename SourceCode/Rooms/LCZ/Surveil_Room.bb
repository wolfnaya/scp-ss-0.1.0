
Function FillRoom_Surveil_Room(r.Rooms)
	Local d.Doors,sc.SecurityCams
	Local i
	
	Local scale# = RoomScale * 4.5 * 0.4
	Local screen%
	
	;Monitor Objects
	For i = 0 To 14
		If i <> 7 Then
			r\Objects[i] = CopyEntity(Monitor)
			ScaleEntity(r\Objects[i], scale, scale, scale)
			If i <> 4 And i <> 13 Then
				screen = CreateSprite()
				EntityFX screen,17
				SpriteViewMode screen,2
				ScaleSprite(screen, MeshWidth(Monitor) * scale * 0.95 * 0.5, MeshHeight(Monitor) * scale * 0.95 * 0.5)
				Select i
					Case 0
						EntityTexture screen,Surveil_Room_Textures[1],0
					Case 2
						EntityTexture screen,Surveil_Room_Textures[1],2
					Case 3
						EntityTexture screen,Surveil_Room_Textures[1],1
					Case 8
						EntityTexture screen,Surveil_Room_Textures[1],4
					Case 9
						EntityTexture screen,Surveil_Room_Textures[1],5
					Case 10
						EntityTexture screen,Surveil_Room_Textures[1],3
					Case 11
						EntityTexture screen,Surveil_Room_Textures[1],7
					Default
						EntityTexture screen,Surveil_Room_Textures[0],3
				End Select
				EntityParent screen,r\Objects[i]
			ElseIf i = 4 Then
				r\Objects[20] = CreateSprite()
				EntityFX r\Objects[20],17
				SpriteViewMode r\Objects[20],2
				ScaleSprite(r\Objects[20], MeshWidth(Monitor) * scale * 0.95 * 0.5, MeshHeight(Monitor) * scale * 0.95 * 0.5)
				EntityTexture r\Objects[20],Surveil_Room_Textures[0],2
				EntityParent r\Objects[20],r\Objects[i]
			Else
				r\Objects[21] = CreateSprite()
				EntityFX r\Objects[21],17
				SpriteViewMode r\Objects[21],2
				ScaleSprite(r\Objects[21], MeshWidth(Monitor) * scale * 0.95 * 0.5, MeshHeight(Monitor) * scale * 0.95 * 0.5)
				EntityTexture r\Objects[21],Surveil_Room_Textures[1],6
				EntityParent r\Objects[21],r\Objects[i]
			EndIf
		EndIf
	Next
	For i = 0 To 2
		PositionEntity r\Objects[i],r\x-207.94*RoomScale,r\y+(648.0+(112*i))*RoomScale,r\z-60.0686*RoomScale
		RotateEntity r\Objects[i],0,105,0
		EntityParent r\Objects[i],r\obj
		DebugLog i
	Next
	For i = 3 To 5
		PositionEntity r\Objects[i],r\x-231.489*RoomScale,r\y+(648.0+(112*(i-3)))*RoomScale,r\z+95.7443*RoomScale
		RotateEntity r\Objects[i],0,90,0
		EntityParent r\Objects[i],r\obj
		DebugLog i
	Next
	For i = 6 To 8 Step 2
		PositionEntity r\Objects[i],r\x-231.489*RoomScale,r\y+(648.0+(112*(i-6)))*RoomScale,r\z+255.744*RoomScale
		RotateEntity r\Objects[i],0,90,0
		EntityParent r\Objects[i],r\obj
		DebugLog i
	Next
	For i = 9 To 11
		PositionEntity r\Objects[i],r\x-231.489*RoomScale,r\y+(648.0+(112*(i-9)))*RoomScale,r\z+415.744*RoomScale
		RotateEntity r\Objects[i],0,90,0
		EntityParent r\Objects[i],r\obj
		DebugLog i
	Next
	For i = 12 To 14
		PositionEntity r\Objects[i],r\x-208.138*RoomScale,r\y+(648.0+(112*(i-12)))*RoomScale,r\z+571.583*RoomScale
		RotateEntity r\Objects[i],0,75,0
		EntityParent r\Objects[i],r\obj
		DebugLog i
	Next
	
	;Doors for room
	r\RoomDoors[0] = CreateDoor(r\zone,r\x+261.0*RoomScale,r\y,r\z-787.0*RoomScale,90,r,False,False,KEY_CARD_3)
	r\RoomDoors[0]\AutoClose = False
	PositionEntity r\RoomDoors[0]\buttons[1],r\x+223.0*RoomScale,EntityY(r\RoomDoors[0]\buttons[1],True),r\z-930*RoomScale,True
	PositionEntity r\RoomDoors[0]\buttons[0],r\x+281.0*RoomScale,EntityY(r\RoomDoors[0]\buttons[0],True),r\z-649*RoomScale,True
	;RotateEntity r\RoomDoors[0]\buttons[1],0,270,0
	r\RoomDoors[1] = CreateDoor(r\zone,r\x+544.0*RoomScale,r\y+480.0*RoomScale,r\z+256.0*RoomScale,270,r,False,DOOR_WINDOWED,KEY_CARD_3)
	r\RoomDoors[1]\AutoClose = False
	;FreeEntity r\RoomDoors[1]\obj2 : r\RoomDoors[1]\obj2 = 0
	d = CreateDoor(r\zone,r\x+1504.0*RoomScale,r\y+480.0*RoomScale,r\z+960.0*RoomScale,0,r)
	d\AutoClose = False : d\locked = True
	r\RoomDoors[2] = CreateDoor(r\zone,r\x+410.0*RoomScale,r\y,r\z-74.0*RoomScale,0,r,False,DOOR_WINDOWED,False,AccessCode[3])
	r\RoomDoors[2]\AutoClose = False
	PositionEntity r\RoomDoors[2]\buttons[1],r\x+535.0*RoomScale,EntityY(r\RoomDoors[2]\buttons[1],True),r\z-95*RoomScale,True
	RotateEntity r\RoomDoors[2]\buttons[1],0,0,0
	PositionEntity r\RoomDoors[2]\buttons[0],r\x+314.0*RoomScale,EntityY(r\RoomDoors[2]\buttons[0],True),r\z+12*RoomScale,True
	RotateEntity r\RoomDoors[2]\buttons[0],0,90,0
	
	r\Levers[0] = CreateLever(r, r\x-49*RoomScale,r\y+689*RoomScale,r\z+912*RoomScale,0,True)
	
	;Camera in the room itself
	sc.SecurityCams = CreateSecurityCam(r\x-159.0*RoomScale, r\y+384.0*RoomScale, r\z-929.0*RoomScale, r, True)
	sc\angle = 315
	sc\room = r
	TurnEntity(sc\CameraObj, 20, 0, 0)
	EntityParent(sc\obj, r\obj)
	
	PositionEntity(sc\ScrObj, r\x-231.489*RoomScale, r\y+760.0*RoomScale, r\z+255.744*RoomScale)
	TurnEntity(sc\ScrObj, 0, 90, 0)
	EntityParent(sc\ScrObj, r\obj)
	
End Function

Function UpdateEvent_Surveil_Room(e.Events)
	Local sc.SecurityCams,r.Rooms
	Local sound%
	Local i
	
	If gopt\GameMode <> GAMEMODE_NTF Then
		
		If PlayerRoom = e\room Then
			If cpt\Current < 2 Then
				If TaskExists(TASK_FIND_ROOM2_SL) And (Not TaskExists(TASK_TURN_ON_ROOM2_SL)) Then
					EndTask(TASK_FIND_ROOM2_SL)
					BeginTask(TASK_TURN_ON_ROOM2_SL)
				EndIf
			EndIf
		EndIf
		
		;! ~ Lever for core locking (might have a function in the future for the case if the checkpoint needs to be locked again)
		If PlayerRoom = e\room
			If cpt\Current < 2 Then
				e\EventState[2] = UpdateLever(e\room\Levers[0]\obj)
				If e\EventState[2] = 1 Then
					ecst\WasInRoom2_SL = False
					If TaskExists(TASK_COME_BACK_TO_CORE) Then
						FailTask(TASK_COME_BACK_TO_CORE)
					EndIf
					If ecst\WasInLCZCore Then
						If (Not TaskExists(TASK_TURN_ON_ROOM2_SL)) Then
							BeginTask(TASK_TURN_ON_ROOM2_SL)
						EndIf
					EndIf
				Else
					ecst\WasInRoom2_SL = True
					If TaskExists(TASK_TURN_ON_ROOM2_SL) Then
						EndTask(TASK_TURN_ON_ROOM2_SL)
						BeginTask(TASK_COME_BACK_TO_CORE)
					EndIf
				EndIf
			EndIf
		EndIf
		;! ~ Checking if the monitors and such should be rendered or not
		If PlayerRoom = e\room And Abs(EntityY(e\room\RoomDoors[0]\frameobj)-EntityY(Collider))>1.0
			For i = 0 To 14
				If e\room\Objects[i]<>0
					ShowEntity e\room\Objects[i]
				EndIf
			Next
			For sc.SecurityCams = Each SecurityCams
				If sc\IsRoom2slCam
					If sc\ScrObj<>0
						ShowEntity sc\ScrObj
					EndIf
					If sc\ScrOverlay<>0
						ShowEntity sc\ScrOverlay
					EndIf
				EndIf
				If sc\room = e\room
					If sc\ScrObj<>0
						ShowEntity sc\ScrObj
					EndIf
					If sc\ScrOverlay<>0
						ShowEntity sc\ScrOverlay
					EndIf
				EndIf
			Next
			For i = 0 To 3
				If PlayerRoom\Adjacent[i]<>Null
					EntityAlpha(GetChild(PlayerRoom\Adjacent[i]\obj,2),0)
				EndIf
			Next
		Else
			For i = 0 To 14
				If e\room\Objects[i]<>0
					HideEntity e\room\Objects[i]
				EndIf
			Next
			For sc.SecurityCams = Each SecurityCams
				If sc\IsRoom2slCam
					If sc\ScrObj<>0
						HideEntity sc\ScrObj
					EndIf
					If sc\ScrOverlay<>0
						HideEntity sc\ScrOverlay
					EndIf
				EndIf
				If sc\room = e\room
					If sc\ScrObj<>0
						HideEntity sc\ScrObj
					EndIf
					If sc\ScrOverlay<>0
						HideEntity sc\ScrOverlay
					EndIf
				EndIf
			Next
		EndIf
		
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D