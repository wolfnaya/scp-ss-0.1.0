
Const CHECKPOINT_EZ_WAYPOINT1_ID% = 4
Const CHECKPOINT_EZ_WAYPOINT2_ID% = 5
Const CHECKPOINT_EZ_FUSEBOX_ID% = 6
Const CHECKPOINT_EZ_BODY_POS_ID% = 7
Const CHECKPOINT_EZ_106_SPAWN% = 8
Const CHECKPOINT_EZ_WAYPOINT_AFTER0_ID% = 9
Const CHECKPOINT_EZ_WAYPOINT_AFTER1_ID% = 10
Const CHECKPOINT_EZ_WAYPOINT_AFTER2_ID% = 11
Const CHECKPOINT_EZ_DOOR_OBJ_ID% = 12
Const CHECKPOINT_EZ_DOOR_SPARKS_ID% = 13

Const CHECKPOINT_EZ_KEYCARD_DOOR_ID% = 3

Const CHECKPOINT_EZ_NPC1_ID% = 0
Const CHECKPOINT_EZ_NPC2_ID% = 1

Function FillRoom_Checkpoint_EZ(r.Rooms)
	Local d.Doors, fb.FuseBox, it.Items
	Local i%
	
	CreateCheckpointElevator(r, CHECKPOINT_ELEVATOR_ID, 224, 0, 2760, 0, CHECKPOINT_ELEVATOR_DOOR_ID, 224, 0, 2478, 180)
	CreateCheckpointElevatorCounterWeight(r, CHECKPOINT_COUNTERWEIGHT_ID, 224, -7300, 2760, 0)
	PositionEntity(r\RoomDoors[CHECKPOINT_ELEVATOR_DOOR_ID]\buttons[1], r\x, r\y + 0.7, r\z + 2438 * RoomScale, True)
	CreateNewElevator(r\Objects[CHECKPOINT_ELEVATOR_ID], 3, r\RoomDoors[CHECKPOINT_ELEVATOR_DOOR_ID], 1, r, -8192.0, -4096.0, 0.0)
	CreateCheckpointFakeDoor(r, CHECKPOINT_ELEVATOR_FAKE_DOOR_ID, 224, 0, 2478, 0)
	
	fb = CreateFuseBox("Fusebox.b3d", CreateVector3D(r\x - 1356.0 * RoomScale, r\y + 632.0 * RoomScale, r\z + 3062.0 * RoomScale), CreateVector3D(0, 180, 0), CreateVector3D(0.4 * RoomScale, 0.4 * RoomScale, 0.4 * RoomScale))
	EntityParent fb\obj, r\obj
	If gopt\GameMode <> GAMEMODE_CLASSIC Then
		fb\fuses = 2
		it = CreateItem("Fuse", "fuse", r\x - 1075.0 * RoomScale, r\y + 480.0 * RoomScale, r\z + 2800.0 * RoomScale)
		EntityParent(it\collider, r\obj)
	Else
		fb\fuses = 3
	EndIf
	
	r\Objects[CHECKPOINT_EZ_DOOR_SPARKS_ID] = CreatePivot()
	PositionEntity r\Objects[CHECKPOINT_EZ_DOOR_SPARKS_ID], r\x - 100.0 * RoomScale, r\y + 5.0 * RoomScale, r\z - 400.0 * RoomScale, True
	EntityParent r\Objects[CHECKPOINT_EZ_DOOR_SPARKS_ID], r\obj
	
	;Airlock doors
	r\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID] = CreateDoor(r\zone,r\x,r\y,r\z - 400.0*RoomScale,180,r,True,DOOR_WINDOWED)
	r\Objects[CHECKPOINT_EZ_DOOR_OBJ_ID] = CopyEntity(r\RoomDoors[1]\obj,r\obj)
	ScaleEntity r\Objects[CHECKPOINT_EZ_DOOR_OBJ_ID], EntityScaleX(r\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID]\obj), EntityScaleY(r\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID]\obj), EntityScaleZ(r\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID]\obj)
	MoveEntity r\Objects[CHECKPOINT_EZ_DOOR_OBJ_ID], 140.0, 0.0, 0.0
	EntityType r\Objects[CHECKPOINT_EZ_DOOR_OBJ_ID], HIT_MAP
	EntityPickMode r\Objects[CHECKPOINT_EZ_DOOR_OBJ_ID], 2
	r\RoomDoors[CHECKPOINT_AIRLOCK_DOOR2_ID] = CreateDoor(r\zone,r\x,r\y,r\z + 400.0*RoomScale,0,r,True,DOOR_WINDOWED)
	For i = 0 To 1
		r\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID]\buttons[i] = FreeEntity_Strict(r\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID]\buttons[i])
		r\RoomDoors[CHECKPOINT_AIRLOCK_DOOR2_ID]\buttons[i] = FreeEntity_Strict(r\RoomDoors[CHECKPOINT_AIRLOCK_DOOR2_ID]\buttons[i])
	Next
	
	;Level 3 keycard door
	r\RoomDoors[CHECKPOINT_EZ_KEYCARD_DOOR_ID] = CreateDoor(r\zone,r\x - 768.0*RoomScale,r\y,r\z + 672.0*RoomScale,90,r,False,DOOR_DEFAULT,3)
	
	For i = CHECKPOINT_EZ_WAYPOINT1_ID To CHECKPOINT_EZ_WAYPOINT2_ID
		r\Objects[i] = CreatePivot()
		PositionEntity r\Objects[i], r\x + (192.0 - (384.0 * (i - CHECKPOINT_EZ_WAYPOINT1_ID))) * RoomScale, r\y, r\z + 1800.0 * RoomScale, True
		EntityParent r\Objects[i], r\obj
	Next
	
	r\Objects[CHECKPOINT_EZ_FUSEBOX_ID] = fb\obj
	
	r\Objects[CHECKPOINT_EZ_BODY_POS_ID] = CreatePivot()
	PositionEntity r\Objects[CHECKPOINT_EZ_BODY_POS_ID], r\x - 1312.0 * RoomScale, r\y + 600.0 * RoomScale, r\z + 2912.0 * RoomScale
	EntityParent r\Objects[CHECKPOINT_EZ_BODY_POS_ID], r\obj
	
	r\Objects[CHECKPOINT_EZ_WAYPOINT_AFTER0_ID] = CreatePivot()
	PositionEntity r\Objects[CHECKPOINT_EZ_WAYPOINT_AFTER0_ID], r\x - 10.0 * RoomScale, r\y, r\z + 2024.0 * RoomScale
	EntityParent r\Objects[CHECKPOINT_EZ_WAYPOINT_AFTER0_ID], r\obj
	
	r\Objects[CHECKPOINT_EZ_WAYPOINT_AFTER1_ID] = CreatePivot()
	PositionEntity r\Objects[CHECKPOINT_EZ_WAYPOINT_AFTER1_ID], r\x + 192.0 * RoomScale, r\y, r\z + 1896.0 * RoomScale
	EntityParent r\Objects[CHECKPOINT_EZ_WAYPOINT_AFTER1_ID], r\obj
	
	r\Objects[CHECKPOINT_EZ_WAYPOINT_AFTER2_ID] = CreatePivot()
	PositionEntity r\Objects[CHECKPOINT_EZ_WAYPOINT_AFTER2_ID], r\x, r\y, r\z + 1352.0 * RoomScale
	EntityParent r\Objects[CHECKPOINT_EZ_WAYPOINT_AFTER2_ID], r\obj
	
	r\Objects[CHECKPOINT_EZ_106_SPAWN] = CreatePivot()
	PositionEntity r\Objects[CHECKPOINT_EZ_106_SPAWN], r\x + 104.0 * RoomScale, r\y, r\z + 1608.0 * RoomScale
	EntityParent r\Objects[CHECKPOINT_EZ_106_SPAWN], r\obj
	
	InitFluLight(1, FLU_STATE_ON, r)
	
	CreateCheckpointDarkSprite(r, CHECKPOINT_DARK_SPRITE_ID)
	
End Function

Function UpdateEvent_Checkpoint_EZ_106(e.Events)
	Local p.Particles, n.NPCs, fb.FuseBox, de.Decals
	Local i%, tex%
	
	If psp\Checkpoint106Passed Lor gopt\GameMode = GAMEMODE_CLASSIC Then
		e\room\RoomDoors[CHECKPOINT_ELEVATOR_DOOR_ID]\locked = False
		e\room\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID]\open = True
		e\room\RoomDoors[CHECKPOINT_AIRLOCK_DOOR2_ID]\open = True
		e\room\Objects[CHECKPOINT_EZ_DOOR_OBJ_ID] = FreeEntity_Strict(e\room\Objects[CHECKPOINT_EZ_DOOR_OBJ_ID])
		RemoveEvent(e)
		CreateEvent("checkpoints", "checkpoint_ez", 0, 1.0)
		Return
	EndIf
	
	e\room\RoomDoors[CHECKPOINT_ELEVATOR_DOOR_ID]\locked = True
	e\room\RoomDoors[CHECKPOINT_ELEVATOR_DOOR_ID]\open = False
	
	If PlayerRoom = e\room Then
		LightVolume = Min(LightVolume+0.01,TempLightVolume*1.5)
		Select e\EventState
			Case 0 ;Room hasn't been entered before
				For i = CHECKPOINT_EZ_NPC1_ID To CHECKPOINT_EZ_NPC2_ID
					If e\room\NPC[i] = Null Then
						For n = Each NPCs
							If n\NPCtype = NPCtypeMTF And i = (n\PrevState - 1) Then
								If EntityDistanceSquared(n\Collider, e\room\RoomDoors[CHECKPOINT_AIRLOCK_DOOR2_ID]\frameobj) < PowTwo(0.4) Then
									e\room\NPC[i] = n
									e\room\NPC[i]\State = MTF_TOTARGET
									e\room\NPC[i]\EnemyX = EntityX(e\room\Objects[i + CHECKPOINT_EZ_WAYPOINT1_ID], True)
									e\room\NPC[i]\EnemyY = EntityY(e\room\Objects[i + CHECKPOINT_EZ_WAYPOINT1_ID], True)
									e\room\NPC[i]\EnemyZ = EntityZ(e\room\Objects[i + CHECKPOINT_EZ_WAYPOINT1_ID], True)
									Exit
								EndIf
							EndIf
						Next
					EndIf
				Next
				
				If e\room\NPC[CHECKPOINT_EZ_NPC1_ID] <> Null And e\room\NPC[CHECKPOINT_EZ_NPC2_ID] <> Null Then
					;TODO: Play audio of MTF units going to the elevator doors
					PlayNewDialogue(100,%011101)
					mtfd\Enabled = False
					n.NPCs = CreateNPC(NPCtypeD,EntityX(e\room\Objects[CHECKPOINT_EZ_BODY_POS_ID],True),EntityY(e\room\Objects[CHECKPOINT_EZ_BODY_POS_ID],True),EntityZ(e\room\Objects[CHECKPOINT_EZ_BODY_POS_ID],True))
					RotateEntity n\Collider,0,e\room\angle-90,0
					n\State = 3
					ChangeNPCTextureID(n,9)
					SetNPCFrame(n,558)
					n\IsDead = True
					e\EventState = 1
					EndTask(TASK_CHECKPOINT)
				EndIf
				
				;The door is open, but not all of the 2 MTF units have arrived, teleport them into the gateway
				If e\room\RoomDoors[CHECKPOINT_EZ_KEYCARD_DOOR_ID]\open Then
					For n = Each NPCs
						If n\NPCtype = NPCtypeMTF And n\State <> MTF_TOTARGET And EntityDistanceSquared(n\Collider, e\room\RoomDoors[CHECKPOINT_AIRLOCK_DOOR2_ID]\frameobj) > PowTwo(4.0) Then
							PositionEntity n\Collider, e\room\x, e\room\y + 0.5, e\room\z
							ResetEntity n\Collider
							n\PathStatus = 0
							n\PathLocation = 0
							n\PathTimer = 0
							n\State = MTF_FOLLOWPLAYER ;Set to default state to use pathfinding until the gateway has been passed through the doors with the sparks
						EndIf
					Next
				EndIf
			Case 1 ;MTF units go to elevator doors and player goes up to place the fuse
				If (Not TaskExists(TASK_FIXELEVATOR)) And (Not mtfd\IsPlaying) Then
					PlayNewDialogue(101,%1110)
					BeginTask(TASK_FIXELEVATOR)
				EndIf
				For fb = Each FuseBox
					If fb\obj = e\room\Objects[CHECKPOINT_EZ_FUSEBOX_ID] Then
						If fb\fuses = 3 Then
							For i = CHECKPOINT_EZ_WAYPOINT_AFTER0_ID To CHECKPOINT_EZ_WAYPOINT_AFTER2_ID
								de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[i], True), e\room\y + 0.01, EntityZ(e\room\Objects[i], True), 90, Rand(360), 0)
								de\Size = 0.1 : de\SizeChange = 0.01 : de\MaxSize = 1.25 : EntityAlpha(de\obj, 1.0)
							Next
							de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[CHECKPOINT_EZ_WAYPOINT_AFTER2_ID], True), e\room\y + 0.01, EntityZ(e\room\Objects[CHECKPOINT_EZ_WAYPOINT_AFTER2_ID], True), 90, Rand(360), 0)
							de\Size = 0.1 : de\SizeChange = 0.01 : de\MaxSize = 1.75 : EntityAlpha(de\obj, 1.0)
							e\room\NPC[CHECKPOINT_EZ_NPC1_ID]\State = STATE_SCRIPT
							e\room\NPC[CHECKPOINT_EZ_NPC2_ID]\State = STATE_SCRIPT
							If e\Sound <> 0 Then FreeSound_Strict(e\Sound) : e\Sound = 0
							e\Sound = LoadSound_Strict("SFX\Room\Checkpoint\MuffledScream.ogg")
							e\SoundCHN = PlaySound_Strict(e\Sound)
							e\SoundCHN2 = StreamSound_Strict("SFX\Room\Checkpoint\RadioScream.ogg", opt\VoiceVol, 0)
							e\SoundCHN2_isStream = True
							StopChannel(mtfd\CurrentChannel)
							mtfd\CurrentProgress = 0
							mtfd\CurrentSequence = 0
							mtfd\CurrentDialogue = 0
							EndTask(TASK_FIXELEVATOR)
							e\EventState = 2
						EndIf
						Exit
					EndIf
				Next
				If e\EventState2 = 0 Then
					If EntityDistanceSquared(Collider, e\room\Objects[CHECKPOINT_EZ_BODY_POS_ID]) < 5.0 Then
						e\EventState2 = 1
					EndIf	
				ElseIf e\EventState2 = 1 Then
					If EntityDistanceSquared(Collider, e\room\RoomDoors[CHECKPOINT_EZ_KEYCARD_DOOR_ID]\frameobj) < PowTwo(0.5) Then
						;PlayNewDialogue(101,%11)
						;TODO: Dialogue for forgor to put fuse in LOL
						e\EventState2 = 2
					EndIf
				EndIf
				If e\EventState3 = 0 Then
					For i = 0 To MaxItemAmount-1
						If Inventory[i] <> Null Then
							If Inventory[i]\itemtemplate\tempname = "fuse" Then
								PlaySound_Strict LoadTempSound("SFX\SCP\106\Laugh.ogg")
								e\EventState3 = 1
								Exit
							EndIf
						EndIf
					Next
				EndIf	
			Case 2 ;Fuse has been placed, 106 sound appears with MTF screams and door below locks
				If ChannelPlaying(e\SoundCHN)
					UpdateSoundOrigin(e\SoundCHN,Camera,e\room\NPC[CHECKPOINT_EZ_NPC2_ID]\obj,10,1.5)
				EndIf
				If EntityDistanceSquared(Collider, e\room\RoomDoors[CHECKPOINT_EZ_KEYCARD_DOOR_ID]\frameobj) < PowTwo(0.5) Then
					e\EventState = 3
					e\EventState3 = 2
					;Spawn 106 between both MTF units
					PositionEntity(Curr106\Collider, EntityX(e\room\Objects[CHECKPOINT_EZ_106_SPAWN], True), e\room\y + 0.15, EntityZ(e\room\Objects[CHECKPOINT_EZ_106_SPAWN], True), True)
					ResetEntity(Curr106\Collider)
					PositionEntity(e\room\NPC[CHECKPOINT_EZ_NPC1_ID]\Collider, EntityX(e\room\Objects[CHECKPOINT_EZ_WAYPOINT_AFTER1_ID], True), EntityY(e\room\NPC[CHECKPOINT_EZ_NPC1_ID]\Collider, True), EntityZ(e\room\Objects[CHECKPOINT_EZ_WAYPOINT_AFTER1_ID], True), True)
					PositionEntity(e\room\NPC[CHECKPOINT_EZ_NPC2_ID]\Collider, EntityX(e\room\Objects[CHECKPOINT_EZ_WAYPOINT_AFTER2_ID], True), EntityY(e\room\NPC[CHECKPOINT_EZ_NPC2_ID]\Collider, True), EntityZ(e\room\Objects[CHECKPOINT_EZ_WAYPOINT_AFTER2_ID], True), True)
					RotateEntity e\room\NPC[CHECKPOINT_EZ_NPC1_ID]\Collider, 0, 180, 0
					RotateEntity e\room\NPC[CHECKPOINT_EZ_NPC2_ID]\Collider, 0, 180, 0
					ResetEntity e\room\NPC[CHECKPOINT_EZ_NPC1_ID]\Collider
					ResetEntity e\room\NPC[CHECKPOINT_EZ_NPC2_ID]\Collider
					e\SoundCHN2 = StreamSound_Strict("SFX\Music\SuspenseEnd.ogg", opt\MusicVol, 0)
					e\SoundCHN2_isStream = True
					PlayNewDialogue(102,%11)
				ElseIf EntityDistanceSquared(Collider, e\room\RoomDoors[3]\frameobj) < PowTwo(4.0) Then
					If ChannelPlaying(e\SoundCHN) Then
						StopChannel(e\SoundCHN)
						StopStream_Strict(e\SoundCHN2)
						e\SoundCHN2 = StreamSound_Strict("SFX\Room\Checkpoint\RadioCutoff.ogg", opt\VoiceVol, 0)
						e\SoundCHN2_isStream = True
					EndIf	
				EndIf
			Case 3 ;Player is downstairs, so MTF units would sink in and 106 appears
				If EntityDistanceSquared(Collider, e\room\obj) < 0.5 Then
					ShowEntity e\room\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID]\obj
					HideEntity e\room\Objects[CHECKPOINT_EZ_DOOR_OBJ_ID]
					For i = CHECKPOINT_AIRLOCK_DOOR1_ID To CHECKPOINT_AIRLOCK_DOOR2_ID
						e\room\RoomDoors[i]\open = False
						e\room\RoomDoors[i]\fastopen = True
					Next
					If e\room\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID]\openstate > 123.0 Then
						e\room\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID]\openstate = 123.0
						PositionEntity e\room\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID]\obj, EntityX(e\room\Objects[CHECKPOINT_EZ_DOOR_OBJ_ID],True), EntityY(e\room\Objects[CHECKPOINT_EZ_DOOR_OBJ_ID],True), EntityZ(e\room\Objects[CHECKPOINT_EZ_DOOR_OBJ_ID],True)
					EndIf
					
					If e\Sound <> 0 Then FreeSound_Strict(e\Sound) : e\Sound = 0
					e\Sound = LoadSound_Strict("SFX\Door\DoorSparks.ogg")
					e\SoundCHN = PlaySound2(e\Sound,Camera,e\room\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID]\obj,5)
					PlaySound_Strict(AlarmSFX[3])
					PlaySound_Strict LoadTempSound("SFX\Door\DoorClose079.ogg")
					
					e\EventState = 4
				EndIf
		End Select
		If e\EventState3 = 2 Then
			AnimateNPC(e\room\NPC[CHECKPOINT_EZ_NPC1_ID],2021,2055,0.1,False)
			AnimateNPC(e\room\NPC[CHECKPOINT_EZ_NPC2_ID],1910,2020,0.4,False)
			RemoveNPCGun(e\room\NPC[CHECKPOINT_EZ_NPC2_ID])
			If AnimTime(e\room\NPC[CHECKPOINT_EZ_NPC1_ID]\obj) >= 2055 Then
				Curr106\Idle = False
				Curr106\State = -10
				PlaySound_Strict LoadTempSound("SFX\General\WaveStart.ogg")
				RemoveNPC(e\room\NPC[CHECKPOINT_EZ_NPC1_ID])
				RemoveNPC(e\room\NPC[CHECKPOINT_EZ_NPC2_ID])
				e\EventState3 = 3
			Else
				PointEntity(Curr106\Collider, Camera)
				PositionEntity(Curr106\obj, EntityX(Curr106\Collider), EntityY(Curr106\Collider) - 0.15, EntityZ(Curr106\Collider))
				RotateEntity Curr106\obj, 0, EntityYaw(Curr106\Collider), 0
				AnimateNPC(Curr106, 334, 420, -0.25)
			EndIf
		EndIf
		If ChannelPlaying(e\SoundCHN)
			UpdateSoundOrigin(e\SoundCHN,Camera,e\room\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID]\obj,5)
		EndIf
	EndIf
	If e\EventState < 4 Then
		HideEntity e\room\RoomDoors[CHECKPOINT_AIRLOCK_DOOR1_ID]\obj
		ShowEntity e\room\Objects[CHECKPOINT_EZ_DOOR_OBJ_ID]
		For i = CHECKPOINT_AIRLOCK_DOOR1_ID To CHECKPOINT_AIRLOCK_DOOR2_ID
			e\room\RoomDoors[i]\open = True
		Next
		If ParticleAmount > 0 Then
			If Rand(10)=1 Then
				Local pvt% = CreatePivot()
				PositionEntity(pvt, EntityX(e\room\Objects[CHECKPOINT_EZ_DOOR_SPARKS_ID],True), EntityY(e\room\Objects[CHECKPOINT_EZ_DOOR_SPARKS_ID],True)+Rnd(0.0,0.05), EntityZ(e\room\Objects[CHECKPOINT_EZ_DOOR_SPARKS_ID],True))
				RotateEntity(pvt, 0, EntityYaw(e\room\Objects[CHECKPOINT_EZ_DOOR_SPARKS_ID],True)+270, 0)
				MoveEntity pvt,0,0,0.2
				
				For i = 0 To (1+(2*(ParticleAmount-1)))
					p.Particles = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 7, 0.002, 0, 25)
					p\speed = Rnd(0.01,0.05)
					RotateEntity(p\pvt, Rnd(-45,0), EntityYaw(pvt)+Rnd(-10.0,10.0), 0)
					p\size = 0.0075
					ScaleSprite p\obj,p\size,p\size
					p\Achange = -0.05
				Next
				
				pvt = FreeEntity_Strict(pvt)
			EndIf
		EndIf
		
		If e\EventState < 3 Then
			Curr106\Idle = True
		EndIf
		If e\EventState > 1 And e\EventState3 < 3 Then
			ShouldPlay = 38
		EndIf
	EndIf
	
	If FallTimer < -250.0 Then
		Curr106\State = 70 * 60 * Rand(12,17)
		Curr173\Idle = SCP173_ACTIVE
		SaveGame(SavePath + CurrSave\Name + "\", EZ)
		NTF_CurrZone = HCZ
		If RandomSeed = "" Then
			RandomSeed = Abs(MilliSecs())
		EndIf
		SeedRnd GenerateSeedNumber(RandomSeed)
		ResetControllerSelections()
		DropSpeed = 0
		NullGame(True,False)
		LoadEntities()
		LoadAllSounds()
		InitNewGame()
		MainMenuOpen = False
		FlushKeys()
		FlushMouse()
		FlushJoy()
		ResetInput()
		MoveToPocketDimension()
		psp\Checkpoint106Passed = True
		SaveGame(SavePath + CurrSave\Name + "\", HCZ)
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D