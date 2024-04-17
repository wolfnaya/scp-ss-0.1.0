
Function FillRoom_Cont_173(r.Rooms)
	Local d.Doors,de.Decals,sc.SecurityCams
	Local i
	
	;the containment doors
	r\RoomDoors[1] = CreateDoor(r\zone, r\x,r\y, r\z + 1510.0 * RoomScale, 0, r, True, True)
	r\RoomDoors[1]\locked = False : r\RoomDoors[1]\AutoClose = False
	r\RoomDoors[1]\dir = 1 : r\RoomDoors[1]\open = True 
	FreeEntity(r\RoomDoors[1]\buttons[0]) : r\RoomDoors[1]\buttons[0] = 0
	FreeEntity(r\RoomDoors[1]\buttons[1]) : r\RoomDoors[1]\buttons[1] = 0
	r\RoomDoors[1]\MTFClose = False
	
	r\Objects[0] = LoadMesh_Strict("GFX\map\rooms\cont_173\cont_173_center.b3d",r\obj)
	EntityPickMode r\Objects[0],2
	EntityType r\Objects[0],HIT_MAP
	EntityFX r\Objects[0], 2
	LightMesh r\Objects[0],-120,-120,-120
	
	r\Objects[1] = LoadMesh_Strict("GFX\map\Props\alarm_cylinder.b3d");8 568.77263 1437.5494 ;450 90 270
	ScaleEntity r\Objects[1],RoomScale,RoomScale,RoomScale
	PositionEntity r\Objects[1],r\x+8*RoomScale,r\y+568.0*RoomScale,r\z+1437.0*RoomScale,True
	RotateEntity r\Objects[1],450,90,270
	EntityParent r\Objects[1],r\obj
	
	r\Objects[2] = LoadMesh_Strict("GFX\map\Props\alarm_rotor.b3d")
	ScaleEntity r\Objects[2],RoomScale,RoomScale,RoomScale
	PositionEntity r\Objects[2],r\x+8*RoomScale,r\y+568.0*RoomScale,r\z+1437.0*RoomScale,True
	RotateEntity r\Objects[2],450,90,270
	EntityParent r\Objects[2],r\obj
	
	r\Objects[3] = CreatePivot()
	PositionEntity r\Objects[3],r\x,r\y+5*RoomScale,r\z+2246*RoomScale,True
	EntityParent r\Objects[3],r\obj
	
	r\AlarmRotor[0] = r\Objects[2]
	r\AlarmRotorLight[0] = CreateLight(3,r\Objects[2])
	MoveEntity r\AlarmRotorLight[0],0,0,0.001
	LightRange r\AlarmRotorLight[0],1.5
	LightColor r\AlarmRotorLight[0],255*3,100*3,0
	RotateEntity r\AlarmRotorLight[0],0,0,45
	LightConeAngles r\AlarmRotorLight[0],0,75
	
End Function

Function UpdateEvent_Cont_173(e.Events)
	Local n.NPCs,i,angle#
	
	If PlayerRoom = e\room Lor IsRoomAdjacent(e\room,PlayerRoom) Then
		If e\EventState[3] <> 2 Then
			
			UpdateAlarmRotor(e\room\AlarmRotor[0],4)
			ShowEntity e\room\AlarmRotor[0]
			
			If e\SoundCHN[0] = 0 Then
				e\SoundCHN[0] = PlaySound_Strict(AlarmSFX[12])
			Else
				If Not ChannelPlaying(e\SoundCHN[0]) Then e\SoundCHN[0] = PlaySound_Strict(AlarmSFX[12])
			EndIf
		Else
			HideEntity e\room\AlarmRotor[0]
		EndIf
	Else
		HideEntity e\room\AlarmRotor[0]
	EndIf
	
	If gopt\GameMode = GAMEMODE_NTF Then
		Select e\EventState[3]
			Case 0
				;If Curr173\Idle = SCP173_BOXED And EntityDistanceSquared(Curr173\Collider, e\room\Objects[3]) < PowTwo(1.5) Then
				If EntityDistanceSquared(Curr173\Collider, e\room\Objects[3]) < PowTwo(1.5) Then
					PlayPlayerSPVoiceLine("SFX\Player\Voice\Sanders\scp173contained" + Rand(1, 2))
					Curr173\Idle = SCP173_CONTAINED
					Curr173\Contained = True
					e\EventState[3] = 1
				EndIf
			Case 1
				If EntityDistanceSquared(Collider, e\room\Objects[3]) > PowTwo(8.0) Then
					UseDoor(e\room\RoomDoors[1], False)
					PlayAnnouncement("SFX\Intercom\MTF\NTF\Announc173Contain.ogg")
					EndTask(TASK_NTF_173_TO_CHAMBER)
					GiveAchievement(Achv173Cont)
					If opt\SteamEnabled Then Steam_Achieve(ACHV_173_CONTAINED)
					SaveGame(SavePath + CurrSave\Name + "\", True)
					BeginTask(TASK_NTF_GO_TO_ZONE)
					e\EventState[3] = 2
				EndIf
		End Select
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D