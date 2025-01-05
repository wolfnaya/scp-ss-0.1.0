
Function FillRoom_Personnel_Offices(r.Rooms)
	Local it.Items,sc.SecurityCams
	Local d.Doors
	
	CreateDarkSprite(r, 2)
	
	r\Objects[3] = LoadAnimMesh_Strict("GFX\Map\Props\Aquarium.b3d")
	PositionEntity r\Objects[3],r\x-630*RoomScale,r\y+226*RoomScale,r\z+622*RoomScale,True
	RotateEntity r\Objects[3], 0, -90, 0
	ScaleEntity r\Objects[3],RoomScale*300,RoomScale*300,RoomScale*300
	EntityParent r\Objects[3], r\obj
	
	r\Objects[4] = CreatePivot()
	PositionEntity r\Objects[4],r\x-2243*RoomScale,r\y+518*RoomScale,r\z+907*RoomScale,True
	EntityParent r\Objects[4], r\obj
	
	r\Objects[5] = LoadMesh_Strict("GFX\Map\rooms\personnel_offices\personnel_offices_screen.b3d")
	PositionEntity r\Objects[5],r\x,r\y,r\z,True
	ScaleEntity r\Objects[5],RoomScale,RoomScale,RoomScale
	EntityParent r\Objects[5],r\obj
	HideEntity r\Objects[5]
	
	EntityTexture r\Objects[5], SCP_963_2_Screen[0]
	
	; ~ [DOORS]
	
	; ~ [Wolfnaya's Door #1]
	r\RoomDoors[0] = CreateDoor(r\zone, r\x +1056 * RoomScale, r\y +384.0 * RoomScale, r\z +733.0 * RoomScale, 90, r, True, DOOR_OFFICE, KEY_CARD_5)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
	; ~ [Wolfnaya's Door #2]
	r\RoomDoors[1] = CreateDoor(r\zone, r\x+1424* RoomScale, r\y +384.0 * RoomScale, r\z +723 * RoomScale, 90, r, True, DOOR_ONE_SIDED, -1, "2109")
	r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False : r\RoomDoors[1]\locked = True
	; ~ [SCP-963-2's Door]
	r\RoomDoors[2] = CreateDoor(r\zone, r\x -1159 * RoomScale, r\y +384.0 * RoomScale, r\z +637.0 * RoomScale, 270, r, True, DOOR_OFFICE_2, KEY_CARD_5)
	r\RoomDoors[2]\AutoClose = False : r\RoomDoors[2]\open = False
	; ~ [SCP-963-1's Door]
	r\RoomDoors[3] = CreateDoor(r\zone, r\x -1159.7 * RoomScale, r\y +384.0 * RoomScale, r\z +130 * RoomScale, 270, r, True, DOOR_OFFICE_2, KEY_CARD_4)
	r\RoomDoors[3]\AutoClose = False : r\RoomDoors[3]\open = False
	PositionEntity r\RoomDoors[3]\buttons[0],EntityX(r\RoomDoors[3]\buttons[0])-1251*RoomScale,EntityY(r\RoomDoors[3]\buttons[0])+559*RoomScale,EntityZ(r\RoomDoors[3]\buttons[0])-68*RoomScale
	RotateEntity r\RoomDoors[3]\buttons[0],EntityPitch(r\RoomDoors[3]\buttons[0]),EntityYaw(r\RoomDoors[3]\buttons[0])-145,EntityRoll(r\RoomDoors[3]\buttons[0])
	; ~ [Captain Sanders' Door]
	r\RoomDoors[4] = CreateDoor(r\zone, r\x -1159 * RoomScale, r\y +384.0 * RoomScale, r\z -388 * RoomScale, 270, r, True, DOOR_OFFICE_2, KEY_CARD_4)
	r\RoomDoors[4]\AutoClose = False : r\RoomDoors[4]\open = False
	; ~ [Bejamin's Door]
	r\RoomDoors[5] = CreateDoor(r\zone, r\x +1056 * RoomScale, r\y +384.0 * RoomScale, r\z -290 * RoomScale, 90, r, True, DOOR_OFFICE_2, KEY_CARD_4)
	r\RoomDoors[5]\AutoClose = False : r\RoomDoors[5]\open = False
	FreeEntity r\RoomDoors[5]\buttons[0] : r\RoomDoors[5]\buttons[0] = 0
	; ~ [WC Door]
	d = CreateDoor(r\zone, r\x+1056.0*RoomScale, r\y+384.0*RoomScale, r\z+234*RoomScale, 90, r, 0,DOOR_OFFICE_2, KEY_CARD_1)
	d\open = False : d\AutoClose = False
	FreeEntity d\buttons[0] : d\buttons[0] = 0
	; ~ [Door to Vane and ENDSHN]
	r\RoomDoors[6] = CreateDoor(r\zone, r\x +776 * RoomScale, r\y +384.0 * RoomScale, r\z +1358 * RoomScale, 180, r, False, DOOR_OFFICE)
	r\RoomDoors[6]\AutoClose = False
	; ~ [MTF Commander's Door]
	r\RoomDoors[7] = CreateDoor(r\zone, r\x -934 * RoomScale, r\y +384.0 * RoomScale, r\z +1358 * RoomScale, 180, r, False, DOOR_OFFICE_2, KEY_CARD_5)
	r\RoomDoors[7]\AutoClose = False
	; ~ [ENDSHN's Door]
	r\RoomDoors[8] = CreateDoor(r\zone, r\x +514 * RoomScale, r\y +800.0 * RoomScale, r\z +2438 * RoomScale, 180, r, False, DOOR_OFFICE_2, KEY_CARD_5)
	r\RoomDoors[8]\AutoClose = False
	; ~ [Vane's Door]
	r\RoomDoors[9] = CreateDoor(r\zone, r\x +1078 * RoomScale, r\y +800.0 * RoomScale, r\z +2438 * RoomScale, 0, r, False, DOOR_OFFICE_2, KEY_CARD_5)
	r\RoomDoors[9]\AutoClose = False
	
	; ~ [END]
	
	; ~ [ITEMS]
	
	it = CreateItem(GetLocalString("Item Names","badge_benjamin"), "badge", r\x +1632.0 * RoomScale, r\y +510.0 * RoomScale, r\z -267.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","key_4"), "key4", r\x +1632.0 * RoomScale, r\y +510.0 * RoomScale, r\z -267.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("FN P90", "p90", r\x -103.0 * RoomScale, r\y +957.0 * RoomScale, r\z +2642.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	it\state2 = 150 : it\state = 50
	
	it = CreateItem("Wolf's Tokarev TT-33", "tt33", r\x +1998.0 * RoomScale, r\y +553.0 * RoomScale, r\z +447.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	it\state2 = 54 : it\state = 9
	
	it = CreateItem("SCP-268", "scp268", r\x +1980.0 * RoomScale, r\y +550.0 * RoomScale, r\z + 787.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","v_coin"), "vanecoin", r\x +1998.0 * RoomScale, r\y +881.0 * RoomScale, r\z + 2486.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	;it = CreateItem(GetLocalString("Item Names","m_u"), "mui", r\x -2410.0 * RoomScale, r\y +600.0 * RoomScale, r\z + 610.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	; ~ [END]
	
	sc.SecurityCams = CreateSecurityCam(r\x-1111.0*RoomScale, r\y+800.0*RoomScale, r\z+1302.0*RoomScale, r)
	sc\angle = 180
	TurnEntity(sc\CameraObj, 20, 0, 20)
	
	InitFluLight(0,FLU_STATE_OFF,r)
	InitFluLight(1,FLU_STATE_ON,r)
	
End Function

;Global SCP_963_2_Music% = LoadSound_Strict("SFX\Music\Misc\PC_music.ogg")

Function UpdateEvent_Personnel_Offices(e.Events)
	Local pvt%
	
	If PlayerRoom = e\room Then
		
		ShowEntity e\room\Objects[3]
		Animate2(e\room\Objects[3], AnimTime(e\room\Objects[3]),1,200,0.3)
		
		ShouldPlay = MUS_NULL
		
		If gopt\GameMode = GAMEMODE_DEFAULT Then
			
			ShowEntity e\room\Objects[5]
			
			If e\EventState[3] = 0 Then
				If TaskExists(TASK_FIND_PERSONNEL_OFFICES) Then
					If (Not TaskExists(TASK_SEARCH_PERSONNEL_OFFICES)) Then
						BeginTask(TASK_SEARCH_PERSONNEL_OFFICES)
					EndIf
					EndTask(TASK_FIND_PERSONNEL_OFFICES)
				EndIf
				e\EventState[3] = 1
			EndIf
			
			If e\EventState[3] = 1 Then
				If ecst\WasInO5Again Then
					If InteractWithObject(e\room\Objects[4],1) Then
						PlaySound_Strict(LoadTempSound("SFX\General\PC_typing.ogg"))
						e\EventState[3] = 2
					EndIf
				EndIf
			EndIf
			
			If e\EventState[3] = 2 Then
				e\EventState[4] = e\EventState[4] + FPSfactor
			EndIf
			
			If e\EventState[4] >= 70*5 Then
				EntityTexture e\room\Objects[5], SCP_963_2_Screen[1], Floor(((e\EventState[4]-70*5)/70) Mod 4.0)
			EndIf
			
			If e\EventState[4] >= 70*5 And e\EventState[4] < 70*6 Then
				If (Not TaskExists(TASK_GET_TOPSIDE)) Then
					BeginTask(TASK_GET_TOPSIDE)
				EndIf
				If (Not TaskExists(TASK_LAUNCH_ROCKET)) Then
					BeginTask(TASK_LAUNCH_ROCKET)
				EndIf
				If TaskExists(TASK_SEARCH_PERSONNEL_OFFICES) Then
					EndTask(TASK_SEARCH_PERSONNEL_OFFICES)
				EndIf
				ecst\UnlockedGateDoors = True
				e\EventState[3] = 3
			EndIf
			
			If e\EventState[3] = 3 Then
				e\EventState[4] = e\EventState[4] + FPSfactor
				e\EventState[5] = e\EventState[5] + FPSfactor
			EndIf
			
			If e\EventState[5] > 0 And e\EventState[5] < 70*0.02 Then
				PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\finalchoice")
				If TaskExists(TASK_SEARCH_PERSONNEL_OFFICES) Then
					EndTask(TASK_SEARCH_PERSONNEL_OFFICES)
				EndIf
			EndIf
			
			If e\EventState[5] > 0 And e\EventState[5] < 70*32 Then
				psp\NoMove = True
				psp\NoRotation = True
			EndIf
			
			If e\EventState[5] >= 70*32 Then
				psp\NoMove = False
				psp\NoRotation = False
				ecst\WasInPO = True
				If (Not TaskExists(TASK_HELP_GUARD)) Then
					BeginTask(TASK_HELP_GUARD)
				EndIf
			EndIf
			
			If ecst\UnlockedWolfnaya Then
				e\room\RoomDoors[1]\locked = False
			EndIf
			
			If e\room\RoomDoors[1]\open And ecst\UnlockedWolfnaya Then
				GiveAchievement(AchvWolfnaya)
			EndIf
			
		EndIf
	Else
		HideEntity e\room\Objects[3]
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS