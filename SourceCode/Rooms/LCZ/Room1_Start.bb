
Function FillRoom_Room1_Start(r.Rooms)
	Local em.Emitters,it.Items,de.Decals,d.Doors,ne.NewElevator
	Local lt.LightTemplates, tw.TempWayPoints
	Local newlt%, i%, n%
	Local x#,z#
	
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x +26.0 * RoomScale, r\y +941.0 * RoomScale, r\z +1201.0 * RoomScale)
	EntityParent(r\Objects[0], r\obj)
	
	r\Objects[1] = CreatePivot()
	PositionEntity(r\Objects[1], r\x +62.0 * RoomScale, r\y +90.0* RoomScale, r\z +2015.0 * RoomScale)
	EntityParent(r\Objects[1], r\obj)
	
	r\Objects[15] = CreatePivot()
	PositionEntity(r\Objects[15], r\x -32.0 * RoomScale, r\y +66.0 * RoomScale, r\z +302.0 * RoomScale)
	EntityParent(r\Objects[15], r\obj)
	
	r\Objects[16] = CreatePivot()
	PositionEntity(r\Objects[16], r\x +802.0 * RoomScale, r\y +66.0 * RoomScale, r\z -595.0 * RoomScale)
	EntityParent(r\Objects[16], r\obj)
	
	r\Objects[17] = CreatePivot()
	PositionEntity(r\Objects[17], r\x -4650.0 * RoomScale, r\y -1369.0 * RoomScale, r\z + 7787.0 * RoomScale)
	EntityParent(r\Objects[17], r\obj)
	
	r\Objects[14] = LoadRMesh("GFX\Map\Rooms\Room1_Start\room1_start_lightcone.rmesh",Null)
	PositionEntity(r\Objects[14],r\x,r\y,r\z,True)
	ScaleEntity(r\Objects[14],RoomScale,RoomScale,RoomScale)
	EntityParent(r\Objects[14], r\obj)
	
	CreateDarkSprite(r, 22)
	
	; ~ Teleportation
	
	r\Objects[18] = CreatePivot()
	PositionEntity(r\Objects[18], r\x -8422.0 * RoomScale, r\y -3272.0 * RoomScale, r\z +11100.0 * RoomScale)
	EntityParent(r\Objects[18], r\obj)
	
	r\Objects[19] = CreatePivot()
	PositionEntity(r\Objects[19], r\x -3221.0 * RoomScale, r\y -4665.0 * RoomScale, r\z -2637.0 * RoomScale)
	EntityParent(r\Objects[19], r\obj)
	
	r\Objects[20] = CreatePivot()
	PositionEntity(r\Objects[20], r\x -2790.0 * RoomScale, r\y -4842.0 * RoomScale, r\z -2287.0 * RoomScale)
	EntityParent(r\Objects[20], r\obj)
	
	r\Objects[21] = CreatePivot()
	PositionEntity(r\Objects[21], r\x -8443.0 * RoomScale, r\y -3425.0 * RoomScale, r\z +10315.0 * RoomScale)
	EntityParent(r\Objects[21], r\obj)
	
	; ~ End
	
	em.Emitters = CreateEmitter(r\x -18.0 * RoomScale, +834.0*RoomScale, r\z +967* RoomScale, 1)
	TurnEntity(em\Obj, 20, 45, 0, True)
	EntityParent(em\Obj, r\obj) : em\Room = r
	em\RandAngle = 15 : em\Speed = 0.03
	em\SizeChange = 0.01 : em\Achange = -0.006
	em\Gravity = 0.05
	em\Speed = 0.005
	Select Rand(3)
		Case 1
			x# = 2312
			z#=-952
		Case 2
			x# = 3032
			z#=1288
		Case 3
			x# = 2824
			z#=2808
	End Select
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x+13.0*RoomScale,0.0,r\z+438.0*RoomScale,0,r,False,DOOR_DEFAULT)
	r\RoomDoors[0]\open = True
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x-702.0*RoomScale,r\y-2746*RoomScale,r\z-2300.0*RoomScale,90,r,False,DOOR_ONE_SIDED, KEY_CARD_CAVE_1)
	r\RoomDoors[1]\open = True
	r\RoomDoors[1]\buttons[1] = FreeEntity_Strict(r\RoomDoors[1]\buttons[1])
	
	r\RoomDoors[2] = CreateDoor(r\zone,r\x-1542.0*RoomScale,r\y-2746*RoomScale,r\z-2300.0*RoomScale,90,r,False,DOOR_ONE_SIDED,KEY_CARD_CAVE_1)
	r\RoomDoors[2]\open = False
	r\RoomDoors[2]\buttons[0] = FreeEntity_Strict(r\RoomDoors[2]\buttons[0])
	
	r\Objects[12] = CreatePivot()
	PositionEntity(r\Objects[12], r\x -1125.0 * RoomScale, r\y -2690.0 * RoomScale, r\z -2300.0 * RoomScale)
	EntityParent(r\Objects[12], r\obj)
	
	r\Objects[13] = CreatePivot()
	PositionEntity(r\Objects[13], r\x +225.0 * RoomScale, r\y -2670.0 * RoomScale, r\z +98.0 * RoomScale,True)
	EntityParent(r\Objects[13], r\obj)
	
	r\Objects[2] = CreatePivot()
	PositionEntity(r\Objects[2], r\x +802.0 * RoomScale, r\y +90.0 * RoomScale, r\z +111.0 * RoomScale)
	EntityParent(r\Objects[2], r\obj)
	r\Objects[3] = CreatePivot()
	PositionEntity(r\Objects[3], r\x +802.0 * RoomScale, r\y -2650.0 * RoomScale, r\z +111.0 * RoomScale)
	EntityParent(r\Objects[3], r\obj)
	r\RoomDoors[3] = CreateDoor(r\zone,r\x+499.0*RoomScale,r\y,r\z+100.0*RoomScale,-90,r,False,DOOR_ELEVATOR)
	r\RoomDoors[3]\AutoClose = False : r\RoomDoors[3]\open = True
	r\RoomDoors[4] = CreateDoor(r\zone,r\x+499.0*RoomScale,r\y-2747*RoomScale,r\z+100.0*RoomScale,-90,r,False,DOOR_ELEVATOR)
	
	r\Objects[4] = CreatePivot()
	PositionEntity(r\Objects[4], r\x +802.0 * RoomScale, r\y +90.0 * RoomScale, r\z-595.0 * RoomScale)
	EntityParent(r\Objects[4], r\obj)
	r\Objects[5] = CreatePivot()
	PositionEntity(r\Objects[5], r\x +1840.0 * RoomScale, r\y -2650.0 * RoomScale, r\z -2258.0 * RoomScale)
	EntityParent(r\Objects[5], r\obj)
	r\RoomDoors[5] = CreateDoor(r\zone,r\x+499.0*RoomScale,r\y,r\z-607.0*RoomScale,-90,r,False,DOOR_ELEVATOR)
	r\RoomDoors[5]\AutoClose = False : r\RoomDoors[5]\open = True
	r\RoomDoors[6] = CreateDoor(r\zone,r\x+1551.0*RoomScale,r\y-2747*RoomScale,r\z-2270.0*RoomScale,-90,r,False,DOOR_ELEVATOR)
	
	Local ElevatorObj = LoadRMesh("GFX\map\elevators\elevator_cabin_2.rmesh",Null)
	EntityParent ElevatorObj,r\obj
	HideEntity ElevatorObj
	
	r\Objects[6] = CopyEntity(ElevatorObj)
	ScaleEntity r\Objects[6],RoomScale,RoomScale,RoomScale
	PositionEntity(r\Objects[6], r\x -2743.0 * RoomScale, r\y -4847.0 * RoomScale, r\z-1502.0 * RoomScale)
	EntityParent r\Objects[6],r\obj
	
	r\RoomDoors[7] = CreateDoor(r\zone,r\x-2744.0*RoomScale,r\y-4847.0*RoomScale,r\z-1786.0*RoomScale,180,r,True,DOOR_ELEVATOR_3FLOOR,False,"",1)
	MoveEntity(r\RoomDoors[7]\buttons[0], -25, 0, 0)
	
	ne = CreateNewElevator(r\Objects[6],1,r\RoomDoors[7],1,r,-4847,-2747)
	ne\floorlocked[2] = True
	
	r\RoomDoors[9] = CreateDoor(r\zone,r\x+12.0*RoomScale,0.0,r\z+2939.0*RoomScale,0,r,False,DOOR_WINDOWED)
	r\RoomDoors[9]\open = False : r\RoomDoors[9]\locked = True
	
	r\RoomDoors[10] = CreateDoor(r\zone,r\x-133.0*RoomScale,r\y-2746*RoomScale,r\z+106.0*RoomScale,90,r,False,DOOR_CONTAINMENT, KEY_CARD_0)
	r\RoomDoors[10]\open = False
	PositionEntity r\RoomDoors[10]\buttons[0],r\x+25.0*RoomScale,r\y-2539*RoomScale,r\z+416*RoomScale,True
	RotateEntity r\RoomDoors[10]\buttons[0],EntityPitch(r\RoomDoors[10]\buttons[0]),EntityYaw(r\RoomDoors[10]\buttons[0])+180,EntityRoll(r\RoomDoors[10]\buttons[0])
	PositionEntity r\RoomDoors[10]\buttons[1],r\x-277.0*RoomScale,r\y-2547*RoomScale,r\z-191*RoomScale,True
	RotateEntity r\RoomDoors[10]\buttons[1],EntityPitch(r\RoomDoors[10]\buttons[1]),EntityYaw(r\RoomDoors[10]\buttons[1])+180,EntityRoll(r\RoomDoors[10]\buttons[1])
	
	r\Objects[8] = CreatePivot()
	PositionEntity(r\Objects[8], r\x -4647.0 * RoomScale, r\y -3140.0 * RoomScale, r\z+7775.0 * RoomScale)
	EntityParent(r\Objects[8], r\obj)
	r\Objects[9] = CreatePivot()
	PositionEntity(r\Objects[9], r\x -4647.0 * RoomScale, r\y -1140.0 * RoomScale, r\z+7775.0 * RoomScale)
	EntityParent(r\Objects[9], r\obj)
	r\RoomDoors[11] = CreateDoor(r\zone,r\x-4950.0*RoomScale,r\y-3436*RoomScale,r\z+7776.0*RoomScale,-90,r,True,DOOR_ELEVATOR)
	r\RoomDoors[11]\AutoClose = False
	r\RoomDoors[12] = CreateDoor(r\zone,r\x-4950.0*RoomScale,r\y-1435.0*RoomScale,r\z+7776.0*RoomScale,-90,r,False,DOOR_ELEVATOR)
	
	r\RoomDoors[13] = CreateDoor(r\zone,r\x-5535.0*RoomScale,r\y-1425*RoomScale,r\z+7773.0*RoomScale,90,r,False,DOOR_WINDOWED,KEY_CARD_CAVE_2)
	
	r\Objects[11] = r\obj
	
	Local rt.RoomTemplates = New RoomTemplates
	rt\objPath = "GFX\Map\Rooms\Room1_Start\room1_start_2.rmesh"
	rt\obj = LoadRMesh(rt\objPath,rt)
	r\Objects[10] = rt\obj
	ScaleEntity(r\Objects[10], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[10], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[10], 2), 2)
	PositionEntity(r\Objects[10],r\x,r\y,r\z)
	EntityParent r\Objects[10],r\obj
	For lt.LightTemplates = Each LightTemplates
		If lt\roomtemplate = rt Then
			newlt = AddLight(r, r\x+lt\x, r\y+lt\y, r\z+lt\z, lt\ltype, lt\range, lt\r, lt\g, lt\b, r\Objects[10])
			If newlt <> 0 Then
				If lt\ltype = 3 Then
					LightConeAngles(newlt, lt\innerconeangle, lt\outerconeangle)
					RotateEntity(newlt, lt\pitch, lt\yaw, 0)
				EndIf
			EndIf
		EndIf
	Next
	For tw.TempWayPoints = Each TempWayPoints
		If tw\roomtemplate = rt Then
			CreateWaypoint(r\x+tw\x, r\y+tw\y, r\z+tw\z, Null, r)
		EndIf
	Next
	For i = 0 To MaxRoomEmitters-1
		If r\RoomTemplate\TempSoundEmitter[i]<>0 Then
			r\SoundEmitterObj[i]=CreatePivot(r\Objects[10])
			PositionEntity r\SoundEmitterObj[i], r\x+rt\TempSoundEmitterX[i],r\y+rt\TempSoundEmitterY[i],r\z+rt\TempSoundEmitterZ[i],True
			EntityParent(r\SoundEmitterObj[i],r\Objects[10])
			
			r\SoundEmitter[i] = r\RoomTemplate\TempSoundEmitter[i]
			r\SoundEmitterRange[i] = r\RoomTemplate\TempSoundEmitterRange[i]
		EndIf
	Next
	Delete rt
	HideEntity(r\Objects[10])
	
	Select Rand(0,2)
		Case 0
			it = CreateItem(GetLocalString("Item Names","key_cave"), "key_cave", r\x-3388.0*RoomScale, r\y-3342.0*RoomScale, r\z+4306.0*RoomScale)
			EntityParent(it\collider, r\Objects[10])
		Case 1
			it = CreateItem(GetLocalString("Item Names","key_cave"), "key_cave", r\x-11730.0*RoomScale, r\y-3326.0*RoomScale, r\z+2124.0*RoomScale)
			EntityParent(it\collider, r\Objects[10])
		Case 2
			it = CreateItem(GetLocalString("Item Names","key_cave"), "key_cave", r\x-9014.0*RoomScale, r\y-2796.0*RoomScale, r\z+7780.0*RoomScale)
			EntityParent(it\collider, r\Objects[10])
	End Select
	
	it = CreateItem(GetLocalString("Item Names","key_cave_2"), "key_cave2", r\x-5259.0*RoomScale, r\y-1425.0*RoomScale, r\z+7750.0*RoomScale)
	EntityParent(it\collider, r\Objects[10])
	
	it = CreateItem("O5 Council Room Note", "paper", r\x-6472.0*RoomScale, r\y-1296.0*RoomScale, r\z+8760.0*RoomScale)
	EntityParent(it\collider, r\Objects[10])
	
	it = CreateItem(GetLocalString("Item Names","key_0"), "key0", r\x+255.0*RoomScale, r\y-2736.0*RoomScale, r\z+98.0*RoomScale)
	EntityParent(it\collider, r\Objects[11])
	
	If gopt\GameMode = GAMEMODE_DEFAULT And (cpt\Current = 1 Lor cpt\Current = 0) Then
		it = CreateItem(GetLocalString("Item Names","crowbar"), "crowbar", r\x+276.0*RoomScale, r\y+66.0*RoomScale, r\z-622.0*RoomScale)
		EntityParent(it\collider, r\Objects[11])
	EndIf
	
End Function

Function UpdateEvent_Room1_Start(e.Events)
	Local r.Rooms, ne.NewElevator, fb.FuseBox, de.Decals
	Local i%
	
	If gopt\GameMode = GAMEMODE_DEFAULT Then
		If (Not EntityHidden(e\room\Objects[10])) Then HideEntity(e\room\Objects[10])
		If PlayerRoom <> e\room Then
			If TaskExists(TASK_FINDWAY_START) Lor TaskExists(TASK_FINDWAY_START_KEY) Then
				CancelTask(TASK_FINDWAY_START)
				CancelTask(TASK_FINDWAY_START_KEY)
			EndIf
		EndIf
		
		If PlayerRoom = e\room Then
			
			If (Not Curr173\Contained) Then
				If gopt\GameMode <> GAMEMODE_NTF Then
					If PlayerInNewElevator Then
						Curr173\Idle = SCP173_DISABLED
						HideEntity Curr173\obj
						HideEntity Curr173\obj2
						HideEntity Curr173\Collider
					Else
						Curr173\Idle = SCP173_ACTIVE
						If EntityHidden(Curr173\obj) Then ShowEntity Curr173\obj
						If EntityHidden(Curr173\obj2) Then ShowEntity Curr173\obj2
						If EntityHidden(Curr173\Collider) Then ShowEntity Curr173\Collider
					EndIf
				EndIf
			EndIf
			
			If EntityY(Collider)<-4700.0*RoomScale ; ~ Cave
				
				If (Not PlayerInNewElevator)
					PositionEntity e\room\RoomDoors[7]\frameobj,EntityX(e\room\RoomDoors[7]\frameobj),-4847.0*RoomScale,EntityZ(e\room\RoomDoors[7]\frameobj)
					PositionEntity e\room\RoomDoors[7]\buttons[0],EntityX(e\room\RoomDoors[7]\buttons[0]),-4847.0*RoomScale+0.6,EntityZ(e\room\RoomDoors[7]\buttons[0])
					PositionEntity e\room\RoomDoors[7]\buttons[1],EntityX(e\room\RoomDoors[7]\buttons[1]),-4847.0*RoomScale+0.7,EntityZ(e\room\RoomDoors[7]\buttons[1])
					For ne = Each NewElevator
						If ne\door = e\room\RoomDoors[7]
							If ne\currfloor = 1 And ne\state = 0.0
								e\room\RoomDoors[7]\open = True
							Else
								e\room\RoomDoors[7]\open = False
							EndIf
						EndIf
					Next
				EndIf
				
			ElseIf EntityY(Collider)<-2500.0*RoomScale ; ~ Entrance
				
				If (Not PlayerInNewElevator)
					PositionEntity e\room\RoomDoors[7]\frameobj,EntityX(e\room\RoomDoors[7]\frameobj),-2747.0*RoomScale,EntityZ(e\room\RoomDoors[7]\frameobj)
					PositionEntity e\room\RoomDoors[7]\buttons[0],EntityX(e\room\RoomDoors[7]\buttons[0]),-2747.0*RoomScale+0.6,EntityZ(e\room\RoomDoors[7]\buttons[0])
					PositionEntity e\room\RoomDoors[7]\buttons[1],EntityX(e\room\RoomDoors[7]\buttons[1]),-2747.0*RoomScale+0.7,EntityZ(e\room\RoomDoors[7]\buttons[1])
					For ne = Each NewElevator
						If ne\door = e\room\RoomDoors[7]
							If ne\currfloor = 2 And ne\state = 0.0
								e\room\RoomDoors[7]\open = True
							Else
								e\room\RoomDoors[7]\open = False
							EndIf
						EndIf
					Next
				EndIf
				
			EndIf
			
			If EntityHidden(e\room\Objects[10]) Then ShowEntity(e\room\Objects[10])
			
			If (Not ecst\NewCavesEvent) Then
				
				If e\EventState[0] = 6 And EntityY(Collider) < (-2000.0) * RoomScale Then
					If SelectedDifficulty\SaveType = SAVEONSCREENS Then
						CreateHintMsg(GetLocalString("Menu","hint_saveonscreens"))
					ElseIf SelectedDifficulty\SaveType = SAVEONQUIT Then
						CreateHintMsg(GetLocalString("Menu","hint_saveonquit"))
					Else
						CreateHintMsg(GetLocalStringR("Menu","hint_saveanywhere",KeyName[KEY_SAVE]))
					EndIf
					e\EventState[0] = 7
				EndIf
				
				If e\EventState[7] = 0 Then
					e\EventState[8] = 0
				EndIf
				If e\EventState[9] = 0 Then
					e\EventState[3] = 0
				EndIf
				
				If EntityDistanceSquared(Collider, e\room\Objects[18])<PowTwo(1.0) Then
					DrawHandIcon = True
					If KeyHitUse Then
						e\EventState[7] = 1
					EndIf
				EndIf
				
				If e\EventState[7] = 1 Then
					e\EventState[8] = e\EventState[8] + (FPSfactor*0.01)
					EntityAlpha e\room\Objects[22],Min(e\EventState[8],1.0)
				EndIf
				
				If e\EventState[8] > 1.05 Then; ~ Exit
					TeleportEntity(Collider, EntityX(e\room\Objects[20], True), EntityY(e\room\Objects[20], True), EntityZ(e\room\Objects[20], True))
					e\EventState[7] = 0
					EntityAlpha e\room\Objects[22],0
				EndIf
				
				If EntityDistanceSquared(Collider, e\room\Objects[19])<PowTwo(1.0) Then
					DrawHandIcon = True; ~ To cave
					If KeyHitUse Then
						e\EventState[9] = 1
					EndIf
				EndIf
				
				If e\EventState[9] = 1 Then
					e\EventState[3] = e\EventState[3] + (FPSfactor*0.01)
					EntityAlpha e\room\Objects[22],Min(e\EventState[3],1.0)
				EndIf
				
				If e\EventState[3] > 1.05 Then
					TeleportEntity(Collider, EntityX(e\room\Objects[21], True), EntityY(e\room\Objects[21], True), EntityZ(e\room\Objects[21], True))
					e\EventState[9] = 0
					EntityAlpha e\room\Objects[22],0
				EndIf
				
				If e\room\RoomDoors[2]\open = True Then
					If TaskExists(TASK_FINDWAY_START_KEY) Then
						EndTask(TASK_FINDWAY_START_KEY)
					EndIf
				EndIf
				
				e\room\RoomDoors[11]\locked = True
				e\room\RoomDoors[12]\locked = True
				
				If EntityY(Collider)<-6000*RoomScale And KillTimer => 0 Then
					m_msg\DeathTxt=""
					PlaySound_Strict LoadTempSound("SFX\Room\PocketDimension\Impact.ogg")
					KillTimer=-1.0
				EndIf
				
				If EntityDistanceSquared(e\room\RoomDoors[6]\buttons[0], Camera) < PowTwo(1.0) Then
					If e\EventState[11] = 0 Then
						If e\room\NPC[0] = Null Then
							e\room\NPC[0]=CreateNPC(NPC_Zombie, EntityX(e\room\Objects[12],True),EntityY(e\room\Objects[12],True), EntityZ(e\room\Objects[12],True),Topless_Zombie)
							e\room\NPC[0]\State[0] = 2
							If opt\MusicVol > 0 Then
								PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Unpleasant_Feeling.ogg"))
							EndIf
							PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\timetogo")
						EndIf
						e\EventState[11] = 1
					EndIf
				EndIf
				
				If e\EventState[0] < 8 Then
					If EntityDistanceSquared(e\room\RoomDoors[2]\obj, Collider) < PowTwo(2.0) And e\room\RoomDoors[2]\open = False Then
						If (Not TaskExists(TASK_FINDWAY_START_KEY))
							BeginTask(TASK_FINDWAY_START_KEY)
						EndIf
					EndIf
				EndIf
				
				If EntityDistanceSquared(e\room\Objects[15], Collider) < PowTwo(1.0) Then
					If e\room\NPC[1] = Null Then
						e\room\NPC[1]=CreateNPC(NPC_Human, EntityX(e\room\Objects[13],True),EntityY(e\room\Objects[13],True), EntityZ(e\room\Objects[13],True))
						e\room\NPC[1]\texture = "GFX\npcs\body1.jpg"
						Local tex = LoadTexture_Strict(e\room\NPC[1]\texture, 1, 2)
						TextureBlend(tex,5)
						EntityTexture(e\room\NPC[1]\obj, tex)
						DeleteSingleTextureEntryFromCache tex
						SetNPCFrame(e\room\NPC[1],560)
						e\room\NPC[1]\IsDead = True
						e\room\NPC[1]\State[0] = 0
						PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\cantgetthrough")
						BeginTask(TASK_FINDWAY_START)
					EndIf
				EndIf
				
				If EntityDistanceSquared(e\room\Objects[16], Collider) < PowTwo(1.5) Then
					EndTask(TASK_FINDWAY_START)
					If (Not TaskExists(TASK_GET_TOPSIDE))
						BeginTask(TASK_GET_TOPSIDE)
					EndIf
				EndIf
				
				If EntityY(Collider) < (-2000.0) * RoomScale Then
					; ~ Cave Music
					ShouldPlay = MUS_CAVE_AREAS
					; ~ Room Optimization
					ShowEntity e\room\Objects[10]
					ShowEntity e\room\Objects[14]
					For r.Rooms = Each Rooms
						HideEntity r\obj
					Next
					ShowEntity PlayerRoom\obj
					EntityAlpha(GetChild(PlayerRoom\obj,2),0.0)
					PlayerFallingPickDistance = 0.0
				Else
					ShowEntity e\room\Objects[11]
					HideEntity e\room\Objects[10]
					HideEntity e\room\Objects[14]
					PlayerFallingPickDistance = 10.0
				EndIf
				
				e\EventState[1] = UpdateElevators(e\EventState[1], e\room\RoomDoors[3], e\room\RoomDoors[4], e\room\Objects[2], e\room\Objects[3], e)
				e\EventState[2] = UpdateElevators(e\EventState[2], e\room\RoomDoors[5], e\room\RoomDoors[6], e\room\Objects[4], e\room\Objects[5], e)
				
				If e\EventState[0] < 5 Then
					If e\EventState[0] < 2 Then
						HideEntity Collider
					EndIf
					Crouch = True
					psp\NoMove = True
					psp\NoRotation = True
					psp\IsShowingHUD = False
				EndIf
				Select e\EventState[0]
					Case 0
						If opt\IntroEnabled Then
							CreateSplashText(GetLocalString("Singleplayer","5_hours_later"),opt\GraphicWidth/2,opt\GraphicHeight/2,280,1,Font_Default_Large,True)
						Else
							CreateSplashText(GetLocalString("Singleplayer","5_hours_after_breach"),opt\GraphicWidth/2,opt\GraphicHeight/2,280,1,Font_Default_Large,True)
						EndIf
						BlurTimer = 0
						IsCutscene = True
						BlinkTimer = -10.0
						PositionEntity Collider, EntityX(e\room\Objects[1], True), EntityY(e\room\Objects[1], True), EntityZ(e\room\Objects[1], True)
						user_camera_pitch = 89
						CrouchState = 1.0
						RotateEntity Collider, 0, e\room\angle - 90, 0
						ResetEntity Collider
						DropSpeed = 0
						de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[0], True), EntityY(e\room\Objects[0], True) - 0.01, EntityZ(e\room\Objects[0], True), -90, Rand(360), 0)
						de\Size = 0.1 : de\SizeChange = 0.01 : de\MaxSize = 1.75 : EntityAlpha(de\obj, 1.0)
						
						CanPlayerUseGuns = True
						
						e\EventState[0] = 1
						
						ClearInventory()
						
						psp\IsShowingHUD = False
					Case 1
						IsCutscene = False
						If EntityY(Camera) < EntityY(e\room\Objects[1], True) Then
							RotateEntity Collider, EntityPitch(Collider),EntityYaw(Collider)+270,EntityRoll(Collider)
							PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\breathingstart")
							psp\Health = 70
							psp\Kevlar = 0
							BlurTimer = 1200
							e\EventState[0] = 2
						EndIf
					Case 7
						If TaskExists(TASK_FINDWAY_START_KEY) Then
							For i = 0 To MaxItemAmount-1
								If Inventory[i] <> Null Then
									If Inventory[i]\itemtemplate\tempname = "key_cave" Then
										e\EventState[0] = 8
										PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\wellthereitis")
										Exit
									EndIf
								EndIf
							Next
						Else
							For i = 0 To MaxItemAmount-1
								If Inventory[i] <> Null Then
									If Inventory[i]\itemtemplate\tempname = "key_cave" Then
										e\EventState[0] = 8
										Exit
									EndIf
								EndIf
							Next
						EndIf
				End Select
				
				If e\EventState[0] >= 2.0 And e\EventState[0] < 3.0 Then
					IsCutscene = True
					BlinkTimer = -10.0
					PositionEntity Collider, EntityX(e\room\Objects[1], True), EntityY(e\room\Objects[1], True), EntityZ(e\room\Objects[1], True)
					ResetEntity Collider
					e\EventState[0] = e\EventState[0] + (FPSfactor / 70.0)/10
					user_camera_pitch = -89
					If e\EventState[0] >= 3.0 Then
						e\EventState[0] = 3
						ShowEntity Collider
					EndIf
					DropSpeed = 0
				ElseIf e\EventState[0] >= 3.0 And e\EventState[0] < 5.0 Then
					IsCutscene = False
					e\EventState[0] = e\EventState[0] + (FPSfactor / 210.0)
					If e\EventState[0] >= 4.0 Then
						user_camera_pitch = 89.0 * (e\EventState[0] - 5.0)
						CrouchState = (5.0 - e\EventState[0])
						RotateEntity Camera, EntityPitch(Camera), EntityYaw(Camera), 20.0 - Abs(Min((4.5 - e\EventState[0]) * 40.0, 20.0))
						If e\EventState[0] >= 5.0 Then
							psp\NoMove = False
							psp\NoRotation = False
							If HUDenabled Then
								psp\IsShowingHUD = True
							EndIf
							ResetInput()
							Crouch = False
							CanSave = True
							CanPlayerUseGuns = True
							SaveGame(SavePath + CurrSave\Name + "\", True)
							e\EventState[0] = 6
							PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\ohmyhead")
							StartChapter("chapter_1", 1)
						EndIf
					EndIf
				EndIf
				
			Else
				
				If e\EventState[7] = 0 Then
					e\EventState[8] = 0
				EndIf
				If e\EventState[9] = 0 Then
					e\EventState[4] = 0
				EndIf
				
				If EntityDistanceSquared(Collider, e\room\Objects[18])<PowTwo(1.0) Then
					DrawHandIcon = True
					If KeyHitUse Then
						e\EventState[7] = 1
					EndIf
				EndIf
				
				If e\EventState[7] = 1 Then
					e\EventState[8] = e\EventState[8] + (FPSfactor*0.01)
					EntityAlpha e\room\Objects[22],Min(e\EventState[8],1.0)
				EndIf
				
				If e\EventState[8] > 1.05 Then
					TeleportEntity(Collider, EntityX(e\room\Objects[20], True), EntityY(e\room\Objects[20], True), EntityZ(e\room\Objects[20], True))
					e\EventState[7] = 0
					EntityAlpha e\room\Objects[22],0
				EndIf
				
				If EntityDistanceSquared(Collider, e\room\Objects[19])<PowTwo(1.0) Then
					DrawHandIcon = True
					If KeyHitUse Then
						e\EventState[9] = 1
					EndIf
				EndIf
				
				If e\EventState[9] = 1 Then
					e\EventState[4] = e\EventState[4] + (FPSfactor*0.01)
					EntityAlpha e\room\Objects[22],Min(e\EventState[4],1.0)
				EndIf
				
				If e\EventState[4] > 1.05 Then
					TeleportEntity(Collider, EntityX(e\room\Objects[21], True), EntityY(e\room\Objects[21], True), EntityZ(e\room\Objects[21], True))
					e\EventState[9] = 0
					EntityAlpha e\room\Objects[22],0
				EndIf
				
			; ~ New Event
				
				e\room\RoomDoors[11]\locked = False
				e\room\RoomDoors[12]\locked = False
				
				e\room\RoomDoors[1]\open = True
				e\room\RoomDoors[2]\open = True
				
				If EntityY(Collider) < (-1000.0) * RoomScale Then
					; ~ Cave Music
					ShouldPlay = MUS_CAVE_AREAS
					; ~ Room Optimization
					ShowEntity e\room\Objects[10]
					ShowEntity e\room\Objects[14]
					For r.Rooms = Each Rooms
						HideEntity r\obj
					Next
					ShowEntity PlayerRoom\obj
					EntityAlpha(GetChild(PlayerRoom\obj,2),0.0)
					PlayerFallingPickDistance = 0.0
				Else
					ShowEntity e\room\Objects[11]
					HideEntity e\room\Objects[10]
					HideEntity e\room\Objects[14]
					PlayerFallingPickDistance = 10.0
				EndIf
				
				If TaskExists(TASK_FIND_CAVES) Then
					EndTask(TASK_FIND_CAVES)
				EndIf
				If (Not TaskExists(TASK_SEARCH_CAVES)) Then
					If e\EventState[6] = 0 Then
						StartChapter("chapter_7", 7)
						BeginTask(TASK_SEARCH_CAVES)
						e\EventState[6] = 1
					EndIf
				EndIf
				
				If EntityDistanceSquared(Collider, e\room\Objects[17])<PowTwo(1.0) Then
					If TaskExists(TASK_SEARCH_CAVES) Then
						EndTask(TASK_SEARCH_CAVES)
						BeginTask(TASK_FIND_WAY_CAVES)
					EndIf
				EndIf
				
				If TaskExists(TASK_FIND_WAY_CAVES) Then
					If e\EventState[5] = 0 Then
						For i = 0 To MaxItemAmount-1
							If Inventory[i] <> Null Then
								If Inventory[i]\itemtemplate\name = "O5 Council Room Note" Then
									PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\vincentradio")
									EndTask(TASK_FIND_WAY_CAVES)
									If (Not TaskExists(TASK_COME_BACK_TO_O5)) Then
										BeginTask(TASK_COME_BACK_TO_O5)
									EndIf
									ecst\WasInCaves = True
									ecst\CIArrived = True
									e\EventState[5] = 1
									Exit
								EndIf
							EndIf
						Next
					EndIf
				EndIf
				
				e\EventState[0] = UpdateElevators(e\EventState[0], e\room\RoomDoors[5], e\room\RoomDoors[6], e\room\Objects[4], e\room\Objects[5], e)
				e\EventState[2] = UpdateElevators(e\EventState[2], e\room\RoomDoors[11], e\room\RoomDoors[12], e\room\Objects[8], e\room\Objects[9], e)
				e\EventState[3] = UpdateElevators(e\EventState[3], e\room\RoomDoors[3], e\room\RoomDoors[4], e\room\Objects[2], e\room\Objects[3], e)
				
			EndIf
			
		EndIf
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D