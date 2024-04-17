
Function FillRoom_Surveil_Room(r.Rooms)
	Local d.Doors,sc.SecurityCams
	Local i
	
	Local scale# = RoomScale * 4.5 * 0.4
	Local screen%
	
	r\Textures[0] = LoadAnimTexture("GFX\SL_monitors_checkpoint.jpg",1,512,512,0,4)
	r\Textures[1] = LoadAnimTexture("GFX\Sl_monitors.jpg",1,256,256,0,8)
	
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
						EntityTexture screen,r\Textures[1],0
					Case 2
						EntityTexture screen,r\Textures[1],2
					Case 3
						EntityTexture screen,r\Textures[1],1
					Case 8
						EntityTexture screen,r\Textures[1],4
					Case 9
						EntityTexture screen,r\Textures[1],5
					Case 10
						EntityTexture screen,r\Textures[1],3
					Case 11
						EntityTexture screen,r\Textures[1],7
					Default
						EntityTexture screen,r\Textures[0],3
				End Select
				EntityParent screen,r\Objects[i]
			ElseIf i = 4 Then
				r\Objects[20] = CreateSprite()
				EntityFX r\Objects[20],17
				SpriteViewMode r\Objects[20],2
				ScaleSprite(r\Objects[20], MeshWidth(Monitor) * scale * 0.95 * 0.5, MeshHeight(Monitor) * scale * 0.95 * 0.5)
				EntityTexture r\Objects[20],r\Textures[0],2
				EntityParent r\Objects[20],r\Objects[i]
			Else
				r\Objects[21] = CreateSprite()
				EntityFX r\Objects[21],17
				SpriteViewMode r\Objects[21],2
				ScaleSprite(r\Objects[21], MeshWidth(Monitor) * scale * 0.95 * 0.5, MeshHeight(Monitor) * scale * 0.95 * 0.5)
				EntityTexture r\Objects[21],r\Textures[1],6
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
	r\RoomDoors[0] = CreateDoor(r\zone,r\x+480.0*RoomScale,r\y,r\z-640.0*RoomScale,90,r,False,False,3)
	r\RoomDoors[0]\AutoClose = False
	PositionEntity r\RoomDoors[0]\buttons[0],r\x+576.0*RoomScale,EntityY(r\RoomDoors[0]\buttons[0],True),r\z-480*RoomScale,True
	RotateEntity r\RoomDoors[0]\buttons[0],0,270,0
	r\RoomDoors[1] = CreateDoor(r\zone,r\x+544.0*RoomScale,r\y+480.0*RoomScale,r\z+256.0*RoomScale,270,r,False,False,3)
	r\RoomDoors[1]\AutoClose = False
	r\RoomDoors[1]\obj2 = FreeEntity_Strict(r\RoomDoors[1]\obj2)
	d = CreateDoor(r\zone,r\x+1504.0*RoomScale,r\y+480.0*RoomScale,r\z+960.0*RoomScale,0,r)
	d\AutoClose = False : d\locked = True
	
	;PathPoint 1 for SCP-049
	r\Objects[7] = CreatePivot()
	PositionEntity r\Objects[7],r\x,r\y+100.0*RoomScale,r\z-800.0*RoomScale,True
	EntityParent r\Objects[7],r\obj
	
	;PathPoints for SCP-049
	r\Objects[15] = CreatePivot()
	PositionEntity r\Objects[15],r\x+700.0*RoomScale,r\y+700.0*RoomScale,r\z+256.0*RoomScale,True
	EntityParent r\Objects[15],r\obj
	r\Objects[16] = CreatePivot()
	PositionEntity r\Objects[16],r\x-60.0*RoomScale,r\y+700.0*RoomScale,r\z+200.0*RoomScale,True
	EntityParent r\Objects[16],r\obj
	r\Objects[17] = CreatePivot()
	PositionEntity r\Objects[17],r\x-48.0*RoomScale,r\y+540.0*RoomScale,r\z+656.0*RoomScale,True
	EntityParent r\Objects[17],r\obj
	
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
	
	;e\eventstate[0]: Determines if the player already entered the room or not (0 = No, 1 = Yes)
	;e\EventState[1]: Variable used for the SCP-049 event
	;e\EventState[2]: Checks if Lever is activated or not
	
	;Camera-Spawning Code + SCP-049-Spawning (will now be loaded in a pointer)
	;[Block]
	If PlayerRoom = e\room
		If e\EventStr = "" And QuickLoadPercent = -1
			QuickLoadPercent = 0
			QuickLoad_CurrEvent = e
			e\EventStr = 0
		EndIf
	EndIf
	;[End Block]
	
	;SCP-049
	;[Block]
	If e\eventstate[0] = 1
		If e\EventState[1] < 0
			If e\EventState[1] = -(70*5)
				For sc.SecurityCams = Each SecurityCams
					If sc\room = e\room
						If EntityDistanceSquared(sc\ScrObj,Camera)<PowTwo(5.0)
							If EntityVisible(sc\ScrObj,Camera)
								e\EventState[1] = Min(e\EventState[1]+FPSfactor,0)
								Exit
							EndIf
						EndIf
					EndIf
				Next
			Else
				e\EventState[1] = Min(e\EventState[1]+FPSfactor,0)
			EndIf
		ElseIf e\EventState[1] = 0
			If e\room\NPC[0] <> Null
				Local AdjDist1#
				Local AdjDist2#
				Local Adj1% = -1
				Local Adj2% = -1
				For i = 0 To 3
					If e\room\AdjDoor[i]<>Null
						If Adj1 = -1
							AdjDist1 = EntityDistanceSquared(e\room\Objects[7],e\room\AdjDoor[i]\frameobj)
							Adj1 = i
						Else
							AdjDist2 = EntityDistanceSquared(e\room\Objects[7],e\room\AdjDoor[i]\frameobj)
							Adj2 = i
						EndIf
					EndIf
				Next
				If AdjDist1 > AdjDist2
					PositionEntity e\room\NPC[0]\Collider,EntityX(e\room\AdjDoor[Adj1]\frameobj),EntityY(e\room\Objects[7],True),EntityZ(e\room\AdjDoor[Adj1]\frameobj)
				Else
					PositionEntity e\room\NPC[0]\Collider,EntityX(e\room\AdjDoor[Adj2]\frameobj),EntityY(e\room\Objects[7],True),EntityZ(e\room\AdjDoor[Adj2]\frameobj)
				EndIf
				PointEntity e\room\NPC[0]\Collider,e\room\obj
				MoveEntity e\room\NPC[0]\Collider,0,0,-1
				ResetEntity e\room\NPC[0]\Collider
				e\room\NPC[0]\HideFromNVG = False
				e\room\NPC[0]\PathX = EntityX(e\room\NPC[0]\Collider)
				e\room\NPC[0]\PathZ = EntityZ(e\room\NPC[0]\Collider)
				e\room\NPC[0]\State = 5
				DebugLog "aaaaaaaaa"
				e\EventState[1] = 1
			EndIf
		ElseIf e\EventState[1] = 1
			If e\room\NPC[0]\PathStatus <> 1
				e\room\NPC[0]\PathStatus = FindPath(e\room\NPC[0],EntityX(e\room\Objects[15],True),EntityY(e\room\Objects[15],True),EntityZ(e\room\Objects[15],True))
			Else
				DebugLog "bbbbbbbbb"
				e\EventState[1] = 2
			EndIf
		ElseIf e\EventState[1] = 2
			If e\room\NPC[0]\PathStatus <> 1
				e\room\NPC[0]\State3 = 1.0
				e\EventState[1] = 3
				e\room\NPC[0]\PathTimer# = 0.0
				DebugLog "ccccccccc"
			Else
				If EntityDistanceSquared(e\room\NPC[0]\Collider,e\room\RoomDoors[0]\frameobj) < PowTwo(5.0)
					e\room\RoomDoors[0]\locked = True
					e\room\RoomDoors[1]\locked = True
					If e\room\NPC[0]\Reload = 0
						PlaySound_Strict LoadTempSound("SFX\Door\DoorOpen079.ogg")
						DebugLog "079 - OPEN DOORS IN ROOM2SL"
						e\room\NPC[0]\Reload = 1
					EndIf
					If (Not e\room\RoomDoors[0]\open)
						e\room\RoomDoors[0]\open = True
						sound=Rand(0, 2)
						PlaySound2(OpenDoorSFX[0 * 3 + sound],Camera,e\room\RoomDoors[0]\obj)
					EndIf
					If (Not e\room\RoomDoors[1]\open)
						e\room\RoomDoors[1]\open = True
						sound=Rand(0, 2)
						PlaySound2(OpenDoorSFX[0 * 3 + sound],Camera,e\room\RoomDoors[1]\obj)
					EndIf
				EndIf
				If e\room\NPC[0]\Reload = 1
					e\room\NPC[0]\DropSpeed = 0
				EndIf
			EndIf
			
			If e\room\NPC[0]\State <> 5
				e\EventState[1] = 7
				DebugLog "fffffffff"
			EndIf
		ElseIf e\EventState[1] = 3
			If e\room\NPC[0]\State <> 5
				e\EventState[1] = 7
				DebugLog "fffffffff"
			EndIf
			
			If MeNPCSeesPlayer(e\room\NPC[0],True)=2
				e\EventState[1] = 4
				DebugLog "ddddddddd"
			EndIf
			
			If e\room\NPC[0]\PathStatus <> 1
				If e\room\NPC[0]\PathTimer# = 0.0
					If e\room\NPC[0]\PrevState = 1 Then
						If (e\room\NPC[0]\SoundChn2 = 0) Then
							e\room\NPC[0]\Sound2 = LoadSound_Strict("SFX\SCP\049\Room2SLEnter.ogg")
							e\room\NPC[0]\SoundChn2 = PlaySound2(e\room\NPC[0]\Sound2, Camera, e\room\NPC[0]\Collider)
						Else
							If (Not ChannelPlaying(e\room\NPC[0]\SoundChn2))
								e\room\NPC[0]\PathTimer# = 1.0
							EndIf
						EndIf
					ElseIf e\room\NPC[0]\PrevState = 2
						If e\room\NPC[0]\Frame >= 1118
							e\room\NPC[0]\PathTimer# = 1.0
						EndIf
					EndIf
				Else
					Select e\room\NPC[0]\State3
						Case 1
							e\room\NPC[0]\PathStatus = FindPath(e\room\NPC[0],EntityX(e\room\Objects[16],True),EntityY(e\room\Objects[16],True),EntityZ(e\room\Objects[16],True))
							e\room\NPC[0]\PrevState = 1
							DebugLog "Path1"
						Case 2
							e\room\NPC[0]\PathStatus = FindPath(e\room\NPC[0],EntityX(e\room\Objects[15],True),EntityY(e\room\Objects[15],True),EntityZ(e\room\Objects[15],True))
							e\room\NPC[0]\PrevState = 2
							DebugLog "Path2"
						Case 3
							e\room\NPC[0]\PathStatus = FindPath(e\room\NPC[0],e\room\NPC[0]\PathX,0.1,e\room\NPC[0]\PathZ)
							e\room\NPC[0]\PrevState = 1
							DebugLog "Path3"
						Case 4
							e\EventState[1] = 5
					End Select
					e\room\NPC[0]\PathTimer# = 0.0
					e\room\NPC[0]\State3 = e\room\NPC[0]\State3 + 1
				EndIf
			EndIf
		ElseIf e\EventState[1] = 4
			If e\room\NPC[0]\State <> 5
				e\EventState[1] = 7
				e\room\NPC[0]\State3 = 5.0
				DebugLog "fffffffff"
			EndIf
		ElseIf e\EventState[1] = 5
			DebugLog "ddddddddd"
			e\room\NPC[0]\State = 2
			For r.Rooms = Each Rooms
				If r <> PlayerRoom
					If (EntityDistanceSquared(r\obj,e\room\NPC[0]\Collider)<PowTwo(HideDistance*2) And EntityDistanceSquared(r\obj,e\room\NPC[0]\Collider)>PowTwo(HideDistance))
						e\room\NPC[0]\PathStatus = FindPath(e\room\NPC[0],EntityX(r\obj),EntityY(r\obj),EntityZ(r\obj))
						e\room\NPC[0]\PathTimer = 0.0
						If e\room\NPC[0]\PathStatus = 1 Then e\EventState[1] = 6
						Exit
					EndIf
				EndIf
			Next
		ElseIf e\EventState[1] = 6
			If MeNPCSeesPlayer(e\room\NPC[0],True) Lor e\room\NPC[0]\State2 > 0 Lor e\room\NPC[0]\LastSeen > 0
				DebugLog "fffffffff"
				e\EventState[1] = 7
			Else
				ShouldPlay = 20
				If e\room\NPC[0]\PathStatus<>1
					e\room\NPC[0]\Idle = 70*60 ;(Making SCP-049 idle for one minute (twice as fast for aggressive NPCs = True))
					PositionEntity e\room\NPC[0]\Collider,0,500,0
					ResetEntity e\room\NPC[0]\Collider
					DebugLog "eeeeeeeee"
					e\EventState[1] = 7
				EndIf
			EndIf
		EndIf
		
		If e\room\NPC[0]<>Null
			If e\EventState[1] < 7
				If e\EventState[1] > 2
					If Abs(EntityY(e\room\RoomDoors[0]\frameobj)-EntityY(e\room\NPC[0]\Collider))>1.0
						If Abs(EntityY(e\room\RoomDoors[0]\frameobj)-EntityY(Collider))<1.0
							If e\room\RoomDoors[0]\open
								e\room\RoomDoors[0]\open = False
								e\room\RoomDoors[0]\fastopen = 1
								PlaySound_Strict LoadTempSound("SFX\Door\DoorClose079.ogg")
								DebugLog "079 - CLOSE DOOR AT HALLWAY IN ROOM2SL"
							EndIf
						EndIf
					Else
						If e\room\RoomDoors[0]\open = False
							e\room\RoomDoors[0]\fastopen = 0
							e\room\RoomDoors[0]\open = True
							sound=Rand(0, 2)
							PlaySound2(OpenDoorSFX[0 * 3 + sound],Camera,e\room\RoomDoors[0]\obj)
							PlaySound_Strict LoadTempSound("SFX\Door\DoorOpen079.ogg")
							DebugLog "079 - OPEN DOOR AT HALLWAY IN ROOM2SL"
						EndIf
					EndIf
				EndIf
				
				If e\EventState[1] > 0 Then CanSave% = False
			Else
				If e\room\RoomDoors[0]\open = False
					e\room\RoomDoors[0]\fastopen = 0
					e\room\RoomDoors[0]\open = True
					sound=Rand(0, 2)
					PlaySound2(OpenDoorSFX[0 * 3 + sound],Camera,e\room\RoomDoors[0]\obj)
					PlaySound_Strict LoadTempSound("SFX\Door\DoorOpen079.ogg")
					DebugLog "079 - OPEN DOOR AT HALLWAY IN ROOM2SL"
				EndIf
			EndIf
		EndIf
	EndIf
	;[End Block]
	
	;Lever for checkpoint locking (might have a function in the future for the case if the checkpoint needs to be locked again)
	If PlayerRoom = e\room
		e\EventState[2] = UpdateLever(e\room\Levers[0]\obj)
		If e\EventState[2] = 1 Then
			;UpdateCheckpointMonitors(0)
		Else
			;TurnCheckpointMonitorsOff(0)
		EndIf
	EndIf
	
	;Checking if the monitors and such should be rendered or not
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
	
End Function

;~IDEal Editor Parameters:
;~F#8C
;~C#Blitz3D