
Const REACTOR_ENTRANCE_DARK_SPRITE% = 1
Const REACTOR_ENTRANCE_BUTTON% = 2

Function FillRoom_Facility_Reactor_Entrance(r.Rooms)
	Local ne.NewElevator
	
	CreateDarkSprite(r, REACTOR_ENTRANCE_DARK_SPRITE)
	
	r\Objects[REACTOR_ENTRANCE_BUTTON] = CreateButton(r\x-1249*RoomScale,r\y+182*RoomScale,r\z+137*RoomScale,0,90,0)
	EntityParent(r\Objects[REACTOR_ENTRANCE_BUTTON],r\obj)
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x -339.0 * RoomScale, r\y, r\z-288*RoomScale, 270, r, False, DOOR_OFFICE_2)
	EntityAlpha r\RoomDoors[1]\frameobj, 0
	r\RoomDoors[2] = CreateDoor(r\zone,r\x -339.0 * RoomScale, r\y, r\z+279*RoomScale, 90, r, False, DOOR_OFFICE_2)
	EntityAlpha r\RoomDoors[2]\frameobj, 0
	
End Function

Function UpdateEvent_Facility_Reactor_Entrance(e.Events)
	Local r.Rooms,e2.Events
	
	If gopt\GameMode = GAMEMODE_DEFAULT Then
		If PlayerRoom = e\room Then
			If (Not ecst\WasInReactor) Then
				
				UpdateButton(e\room\Objects[REACTOR_ENTRANCE_BUTTON])
				
				If TaskExists(TASK_FIND_REACTOR) And (Not TaskExists(TASK_TURN_ON_REACTOR)) Then
					EndTask(TASK_FIND_REACTOR)
					BeginTask(TASK_TURN_ON_REACTOR)
				EndIf
				
				If TaskExists(TASK_TURN_ON_REACTOR) Then
					If d_I\ClosestButton = e\room\Objects[REACTOR_ENTRANCE_BUTTON] Then
						If KeyHitUse Then
							e\EventState[1] = 1
							CreateMsg(GetLocalString("Doors", "elevator_called"), 70*1)
							PlaySound_Strict(ButtonSFX[0])
						EndIf
					EndIf
				EndIf
				
				If e\EventState[1] = 1 Then
					e\EventState[0] = e\EventState[0] + (FPSfactor*0.02)
					EntityAlpha e\room\Objects[REACTOR_ENTRANCE_DARK_SPRITE],Min(e\EventState[0],1.0)
				EndIf
				
				If e\EventState[0] > 2.1 Then
					SaveGame(SavePath + CurrSave\Name + "\", True)
					Local prevZone = gopt\CurrZone
					
					gopt\CurrZone = REACTOR_AREA
					
					ResetControllerSelections()
					DropSpeed = 0
					NullGame(True,False)
					LoadEntities()
					LoadAllSounds()
					Local zonecache% = gopt\CurrZone
					InitNewGame()
					LoadDataForZones(SavePath + CurrSave\Name + "\")
					gopt\CurrZone = zonecache
					MainMenuOpen = False
					FlushKeys()
					FlushMouse()
					FlushJoy()
					ResetInput()
					For e2 = Each Events
						If e2\room = PlayerRoom Then
							e2\EventState[5] = 0
							Exit
						EndIf
					Next
					SaveGame(SavePath + CurrSave\Name + "\", True)
					Return
				EndIf
			Else
				e\EventState[0] = 0
				e\EventState[1] = 0
			EndIf
		EndIf
	EndIf
	
End Function

Const REACTOR_MODEL% = 0
Const REACTOR_PIVOT% = 1
Const REACTOR_TURN_ON_BUTTON% = 2
Const REACTOR_TURN_OFF_BUTTON% = 3
Const REACTOR_EXIT_BUTTON% = 4
Const REACTOR_PART_2% = 5
Const REACTOR_PART_3% = 6
Const REACTOR_PART_4% = 7
Const REACTOR_PART_5% = 8
Const REACTOR_BRIDGES% = 9
Const REACTOR_SHIELD% = 10
Const REACTOR_ELEVATOR% = 11
Const REACTOR_STORAGE_ELEVATOR% = 12
Const REACTOR_CLOSE_TRIGGER% = 13
Const REACTOR_FAKE_DOOR% = 14
Const REACTOR_DARK_SPRITE% = 15
Const REACTOR_WATER% = 16

Const REACTOR_STORAGE_ELEVATOR_DOOR% = 0

Function FillRoom_Facility_Reactor(r.Rooms)
	Local wa.Water, it.Items
	Local ne.NewElevator,ne2.NewElevator
	Local rt.RoomTemplates = New RoomTemplates
	Local lt.LightTemplates, newlt, tw.TempWayPoints, i
	
	r\Objects[REACTOR_EXIT_BUTTON] = CreateButton(-859*RoomScale,180*RoomScale,-9438*RoomScale,0,0)
	EntityParent r\Objects[REACTOR_EXIT_BUTTON], r\obj
	
	Local StorageElevatorOBJ = LoadRMesh("GFX\map\Elevators\storage_elevator.rmesh",Null)
	HideEntity StorageElevatorOBJ
	r\Objects[REACTOR_STORAGE_ELEVATOR] = CopyEntity(StorageElevatorOBJ,r\obj)
	
	Local ElevatorOBJ = LoadRMesh("GFX\map\Elevators\elevator_cabin_3.rmesh",Null)
	HideEntity ElevatorOBJ
	r\Objects[REACTOR_ELEVATOR] = CopyEntity(ElevatorOBJ,r\obj)
	
	PositionEntity(r\Objects[REACTOR_ELEVATOR],-980,0,-9736)
	RotateEntity r\Objects[REACTOR_ELEVATOR],0,270,0
	EntityType r\Objects[REACTOR_ELEVATOR],HIT_MAP
	EntityPickMode r\Objects[REACTOR_ELEVATOR],2
	
	PositionEntity(r\Objects[REACTOR_STORAGE_ELEVATOR],+1283,0,-9736)
	RotateEntity r\Objects[REACTOR_STORAGE_ELEVATOR],0,270,0
	EntityType r\Objects[REACTOR_STORAGE_ELEVATOR],HIT_MAP
	EntityPickMode r\Objects[REACTOR_STORAGE_ELEVATOR],2
	
	it = CreateItem("Franchi SPAS-12", "spas12", r\x+464.0*RoomScale,r\y-1722.0*RoomScale,r\z-5777.0*RoomScale)
	it\state = 6 : it\state2 = 42
	RotateEntity it\collider,0,r\angle+90,r\angle+90,True
	EntityParent(it\collider, r\obj)
	
	CreateDarkSprite(r, REACTOR_DARK_SPRITE)
	
	r\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR] = CreateDoor(r\zone,r\x+1283.0 * RoomScale, r\y, r\z-9540*RoomScale, 0, r, True, DOOR_STORAGE_ELEVATOR, False, "", 1)
	r\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\DisableWaypoint = True
	PositionEntity(r\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[0],r\x+910*RoomScale,EntityY(r\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[0],True),r\z-9747*RoomScale,True)
	RotateEntity r\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[0],0,90,0
	PositionEntity(r\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[1],EntityX(r\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[1],True), EntityY(r\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[1],True), r\z-9406*RoomScale,True)
	
	ne = CreateNewElevator(r\Objects[REACTOR_STORAGE_ELEVATOR],3,r\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR],1,r,-2770.0,-2000.0,0.0,2,True)
	ne\floorlocked[0] = True
	
	wa.Water = CreateWater("GFX\map\rooms\facility_reactor\facility_reactor_water.b3d", "coolant_water", 0, 639*RoomScale, 0, r\obj, (-3228.0*RoomScale))
	EntityAlpha wa\obj, 0.6
	EntityColor wa\obj, 10, 10, 150
	r\Objects[REACTOR_WATER] = LoadTexture_Strict("GFX\map\textures\SLH_water3.png",1,1)
	EntityTexture wa\obj, r\Objects[REACTOR_WATER]
	ScaleTexture r\Objects[REACTOR_WATER], 0.1, 0.1
	TextureBlend r\Objects[REACTOR_WATER], 2
	EntityPickMode wa\obj, 2
	EntityType wa\obj, HIT_MAP
	
	r\Objects[REACTOR_CLOSE_TRIGGER] = CreatePivot()
	PositionEntity r\Objects[REACTOR_CLOSE_TRIGGER],0,-1991*RoomScale, -5017*RoomScale
	EntityParent r\Objects[REACTOR_CLOSE_TRIGGER],r\obj
	
	r\Objects[REACTOR_PIVOT] = CreatePivot()
	PositionEntity r\Objects[REACTOR_PIVOT],0,-1628*RoomScale,0
	EntityParent r\Objects[REACTOR_PIVOT],r\obj
	
	r\Objects[REACTOR_FAKE_DOOR] = LoadMesh_Strict("GFX\map\props\doorhcz.b3d")
	PositionEntity r\Objects[REACTOR_FAKE_DOOR],-3568*RoomScale, -2001*RoomScale, -7009*RoomScale
	RotateEntity r\Objects[REACTOR_FAKE_DOOR],0,90,0
	ScaleEntity r\Objects[REACTOR_FAKE_DOOR],1.35*RoomScale,RoomScale,RoomScale
	EntityPickMode r\Objects[REACTOR_FAKE_DOOR],2
	EntityType r\Objects[REACTOR_FAKE_DOOR], HIT_MAP
	EntityParent r\Objects[REACTOR_FAKE_DOOR],r\obj
	HideEntity r\Objects[REACTOR_FAKE_DOOR]
	
	r\Objects[REACTOR_SHIELD] = LoadAnimMesh_Strict("GFX\Map\rooms\facility_reactor\Reactor_shield.b3d")
	ScaleEntity r\Objects[REACTOR_SHIELD],RoomScale,RoomScale,RoomScale
	PositionEntity r\Objects[REACTOR_SHIELD],r\x,r\y+10639*RoomScale,r\z
	EntityParent(r\Objects[REACTOR_SHIELD],r\obj)
	EntityPickMode(r\Objects[REACTOR_SHIELD],2)
	
	r\Objects[REACTOR_MODEL] = LoadAnimMesh_Strict("GFX\Map\Props\Reactor.b3d")
	ScaleEntity r\Objects[REACTOR_MODEL],47*RoomScale,47*RoomScale,47*RoomScale
	PositionEntity r\Objects[REACTOR_MODEL],r\x,r\y-2466*RoomScale,r\z
	EntityParent(r\Objects[REACTOR_MODEL],r\obj)
	EntityPickMode(r\Objects[REACTOR_MODEL],2)
	
	r\Objects[REACTOR_TURN_ON_BUTTON] = CreateButton(r\x-4378*RoomScale,r\y-1022*RoomScale,r\z-213*RoomScale,443,270,0)
	EntityParent(r\Objects[REACTOR_TURN_ON_BUTTON],r\obj)
	
	r\Objects[REACTOR_TURN_OFF_BUTTON] = CreateButton(r\x-303*RoomScale,r\y-1810*RoomScale,r\z,450,0,270)
	EntityParent(r\Objects[REACTOR_TURN_OFF_BUTTON],r\obj)
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x, r\y - 2001.0 * RoomScale, r\z -4743.0 * RoomScale, 0, r, False, DOOR_DEFAULT)
	PositionEntity r\RoomDoors[1]\buttons[0],r\x+135*RoomScale, r\y - 1829.0 * RoomScale, r\z -4838.0 * RoomScale,True
	RotateEntity r\RoomDoors[1]\buttons[0],0,270,0
	
	r\RoomDoors[2] = CreateDoor(r\zone, r\x, r\y - 2001.0 * RoomScale, r\z + 4743.0 * RoomScale, 0.0, r, True, DOOR_DEFAULT)
	
	rt\objPath = "GFX\map\rooms\facility_reactor\facility_reactor_2.rmesh"
	rt\obj = LoadRMesh(rt\objPath,rt)
	r\Objects[REACTOR_PART_2] = rt\obj
	ScaleEntity(r\Objects[REACTOR_PART_2], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[REACTOR_PART_2], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[REACTOR_PART_2], 2), 2)
	PositionEntity(r\Objects[REACTOR_PART_2],r\x,r\y,r\z)
	EntityParent r\Objects[REACTOR_PART_2],r\obj
	For lt.LightTemplates = Each LightTemplates
		If lt\roomtemplate = rt Then
			newlt = AddLight(r, r\x+lt\x, r\y+lt\y, r\z+lt\z, lt\ltype, lt\range, lt\r, lt\g, lt\b, r\Objects[REACTOR_PART_2])
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
			r\SoundEmitterObj[i]=CreatePivot(r\Objects[REACTOR_PART_2])
			PositionEntity r\SoundEmitterObj[i], r\x+rt\TempSoundEmitterX[i],r\y+rt\TempSoundEmitterY[i],r\z+rt\TempSoundEmitterZ[i],True
			EntityParent(r\SoundEmitterObj[i],r\Objects[REACTOR_PART_2])
			
			r\SoundEmitter[i] = r\RoomTemplate\TempSoundEmitter[i]
			r\SoundEmitterRange[i] = r\RoomTemplate\TempSoundEmitterRange[i]
		EndIf
	Next
	Delete rt
	HideEntity(r\Objects[REACTOR_PART_2])
	
	rt.RoomTemplates = New RoomTemplates
	
	rt\objPath = "GFX\map\rooms\facility_reactor\facility_reactor_3.rmesh"
	rt\obj = LoadRMesh(rt\objPath,rt)
	r\Objects[REACTOR_PART_3] = rt\obj
	ScaleEntity(r\Objects[REACTOR_PART_3], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[REACTOR_PART_3], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[REACTOR_PART_3], 2), 2)
	PositionEntity(r\Objects[REACTOR_PART_3],r\x,r\y,r\z)
	EntityParent r\Objects[REACTOR_PART_3],r\obj
	For lt.LightTemplates = Each LightTemplates
		If lt\roomtemplate = rt Then
			newlt = AddLight(r, r\x+lt\x, r\y+lt\y, r\z+lt\z, lt\ltype, lt\range, lt\r, lt\g, lt\b, r\Objects[REACTOR_PART_3])
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
			r\SoundEmitterObj[i]=CreatePivot(r\Objects[REACTOR_PART_3])
			PositionEntity r\SoundEmitterObj[i], r\x+rt\TempSoundEmitterX[i],r\y+rt\TempSoundEmitterY[i],r\z+rt\TempSoundEmitterZ[i],True
			EntityParent(r\SoundEmitterObj[i],r\Objects[REACTOR_PART_3])
			
			r\SoundEmitter[i] = r\RoomTemplate\TempSoundEmitter[i]
			r\SoundEmitterRange[i] = r\RoomTemplate\TempSoundEmitterRange[i]
		EndIf
	Next
	Delete rt
	HideEntity(r\Objects[REACTOR_PART_3])
	
	rt.RoomTemplates = New RoomTemplates
	
	rt\objPath = "GFX\map\rooms\facility_reactor\facility_reactor_4.rmesh"
	rt\obj = LoadRMesh(rt\objPath,rt)
	r\Objects[REACTOR_PART_4] = rt\obj
	ScaleEntity(r\Objects[REACTOR_PART_4], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[REACTOR_PART_4], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[REACTOR_PART_4], 2), 2)
	PositionEntity(r\Objects[REACTOR_PART_4],r\x,r\y,r\z)
	EntityParent r\Objects[REACTOR_PART_4],r\obj
	For lt.LightTemplates = Each LightTemplates
		If lt\roomtemplate = rt Then
			newlt = AddLight(r, r\x+lt\x, r\y+lt\y, r\z+lt\z, lt\ltype, lt\range, lt\r, lt\g, lt\b, r\Objects[REACTOR_PART_4])
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
			r\SoundEmitterObj[i]=CreatePivot(r\Objects[REACTOR_PART_4])
			PositionEntity r\SoundEmitterObj[i], r\x+rt\TempSoundEmitterX[i],r\y+rt\TempSoundEmitterY[i],r\z+rt\TempSoundEmitterZ[i],True
			EntityParent(r\SoundEmitterObj[i],r\Objects[REACTOR_PART_4])
			
			r\SoundEmitter[i] = r\RoomTemplate\TempSoundEmitter[i]
			r\SoundEmitterRange[i] = r\RoomTemplate\TempSoundEmitterRange[i]
		EndIf
	Next
	Delete rt
	HideEntity(r\Objects[REACTOR_PART_4])
	
	rt.RoomTemplates = New RoomTemplates
	
	rt\objPath = "GFX\map\rooms\facility_reactor\facility_reactor_5.rmesh"
	rt\obj = LoadRMesh(rt\objPath,rt)
	r\Objects[REACTOR_PART_5] = rt\obj
	ScaleEntity(r\Objects[REACTOR_PART_5], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[REACTOR_PART_5], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[REACTOR_PART_5], 2), 2)
	PositionEntity(r\Objects[REACTOR_PART_5],r\x,r\y,r\z)
	EntityParent r\Objects[REACTOR_PART_5],r\obj
	For lt.LightTemplates = Each LightTemplates
		If lt\roomtemplate = rt Then
			newlt = AddLight(r, r\x+lt\x, r\y+lt\y, r\z+lt\z, lt\ltype, lt\range, lt\r, lt\g, lt\b, r\Objects[REACTOR_PART_5])
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
			r\SoundEmitterObj[i]=CreatePivot(r\Objects[REACTOR_PART_5])
			PositionEntity r\SoundEmitterObj[i], r\x+rt\TempSoundEmitterX[i],r\y+rt\TempSoundEmitterY[i],r\z+rt\TempSoundEmitterZ[i],True
			EntityParent(r\SoundEmitterObj[i],r\Objects[REACTOR_PART_5])
			
			r\SoundEmitter[i] = r\RoomTemplate\TempSoundEmitter[i]
			r\SoundEmitterRange[i] = r\RoomTemplate\TempSoundEmitterRange[i]
		EndIf
	Next
	Delete rt
	HideEntity(r\Objects[REACTOR_PART_5])
	
	rt.RoomTemplates = New RoomTemplates
	
	rt\objPath = "GFX\map\rooms\facility_reactor\facility_reactor_bridges.rmesh"
	rt\obj = LoadRMesh(rt\objPath,rt)
	r\Objects[REACTOR_BRIDGES] = rt\obj
	ScaleEntity(r\Objects[REACTOR_BRIDGES], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[REACTOR_BRIDGES], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[REACTOR_BRIDGES], 2), 2)
	PositionEntity(r\Objects[REACTOR_BRIDGES],r\x,r\y,r\z)
	EntityParent r\Objects[REACTOR_BRIDGES],r\obj
	For lt.LightTemplates = Each LightTemplates
		If lt\roomtemplate = rt Then
			newlt = AddLight(r, r\x+lt\x, r\y+lt\y, r\z+lt\z, lt\ltype, lt\range, lt\r, lt\g, lt\b, r\Objects[REACTOR_BRIDGES])
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
			r\SoundEmitterObj[i]=CreatePivot(r\Objects[REACTOR_BRIDGES])
			PositionEntity r\SoundEmitterObj[i], r\x+rt\TempSoundEmitterX[i],r\y+rt\TempSoundEmitterY[i],r\z+rt\TempSoundEmitterZ[i],True
			EntityParent(r\SoundEmitterObj[i],r\Objects[REACTOR_BRIDGES])
			
			r\SoundEmitter[i] = r\RoomTemplate\TempSoundEmitter[i]
			r\SoundEmitterRange[i] = r\RoomTemplate\TempSoundEmitterRange[i]
		EndIf
	Next
	Delete rt
	HideEntity(r\Objects[REACTOR_BRIDGES])
	
End Function

Function UpdateEvent_Facility_Reactor(e.Events)
	Local p.Particles,i
	Local n.NPCs,e2.Events
	Local xtemp#,ztemp#,temp%,angle#,dist#,pvt%,r.Rooms
	
	If gopt\GameMode = GAMEMODE_DEFAULT Then
		
		If gopt\CurrZone = REACTOR_AREA Then
			
			If gopt\CurrZone = REACTOR_AREA Then
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
			EndIf
			
			If PlayerRoom = e\room
				
			; ~ Actual Event
				
				Local ne.NewElevator
				
				If EntityY(Collider)<-1000.0*RoomScale ; ~ Reactor
					
					If (Not PlayerInNewElevator)
						PositionEntity e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\frameobj,EntityX(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\frameobj),-2000.0*RoomScale,EntityZ(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\frameobj)
						PositionEntity e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[0],EntityX(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[0]),-2000.0*RoomScale+0.6,EntityZ(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[0])
						PositionEntity e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[1],EntityX(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[1]),-2000.0*RoomScale+0.7,EntityZ(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[1])
						PositionEntity e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\obj,EntityX(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\obj),-2000.0*RoomScale,EntityZ(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\obj)
						PositionEntity e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\obj2,EntityX(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\obj2),-2000.0*RoomScale,EntityZ(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\obj2)
					EndIf
					
				ElseIf EntityY(Collider)>-100.0*RoomScale ; ~ Entrance
					
					If (Not PlayerInNewElevator)
						PositionEntity e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\frameobj,EntityX(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\frameobj),0,EntityZ(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\frameobj)
						PositionEntity e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[0],EntityX(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[0]),0.6,EntityZ(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[0])
						PositionEntity e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[1],EntityX(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[1]),0.7,EntityZ(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\buttons[1])						PositionEntity e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\obj,EntityX(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\obj),0.0,EntityZ(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\obj)
						PositionEntity e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\obj2,EntityX(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\obj2),0.0,EntityZ(e\room\RoomDoors[REACTOR_STORAGE_ELEVATOR_DOOR]\obj2)
					EndIf
					
				EndIf
				
				ShouldPlay = MUS_NULL
				
				If EntityY(Collider)<=-3228*RoomScale And KillTimer=>0 Then
					m_msg\DeathTxt=""
					PlaySound_Strict LoadTempSound("SFX\General\Water_Splash.ogg")
					EntityAlpha e\room\Objects[REACTOR_DARK_SPRITE],1
					KillTimer=-1.0
				Else
					EntityAlpha e\room\Objects[REACTOR_DARK_SPRITE],0
				EndIf
				PlayerFallingPickDistance = 10.0
				
				If e\EventState[4] = 0 Then
					If (Not TaskExists(TASK_TURN_ON_REACTOR)) Then
						BeginTask(TASK_TURN_ON_REACTOR)
					EndIf
					If ChannelPlaying(e\SoundCHN[0])=False Then
						If opt\MusicVol > 0 Then
							e\SoundCHN[0] = PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Reactor_Core.ogg"))
						EndIf
					EndIf
					
					StartChapter("chapter_3", 3)
					e\EventState[4] = 1
				EndIf
				
				For n.NPCs = Each NPCs
					If n <> Curr106 And n <> Curr173
						RemoveNPC(n)
					EndIf
				Next
				Curr173\Idle = True
				Curr106\Idle = True
				Curr106\Contained = True
				
				CameraFogMode(Camera, 0)
				SecondaryLightOn = True
				
				HideDistance = 35.0
				ShowEntity Fog
				
				CameraFogRange Camera, 5,50
				
				angle = Max(Sin(EntityYaw(Collider)+90),0.0)
				CameraFogColor (Camera,190+(angle*40),190+(angle*20),190)
				CameraClsColor (Camera,190+(angle*40),190+(angle*20),190)
				CameraRange(Camera, 0.01, 65)
				
				AmbientLight (190, 190, 190)
				
				ShowEntity e\room\Objects[REACTOR_PART_2]
				ShowEntity e\room\Objects[REACTOR_PART_3]
				ShowEntity e\room\Objects[REACTOR_PART_5]
				
				If e\room\RoomDoors[1]\open Then
					ShowEntity e\room\Objects[REACTOR_PART_4]
					ShowEntity e\room\Objects[REACTOR_BRIDGES]
					ShowEntity e\room\Objects[REACTOR_MODEL]
					ShowEntity e\room\Objects[REACTOR_SHIELD]
				ElseIf e\room\RoomDoors[1]\openstate >= 170 Then
					HideEntity e\room\Objects[REACTOR_PART_4]
					HideEntity e\room\Objects[REACTOR_BRIDGES]
					HideEntity e\room\Objects[REACTOR_MODEL]
					HideEntity e\room\Objects[REACTOR_SHIELD]
				EndIf
				
				e\EventState[3] = e\EventState[3] + FPSfactor
				
				PositionTexture e\room\Objects[REACTOR_WATER], 0, e\EventState[3] * 0.005
				ShouldUpdateWater = "coolant_water"
				
				If e\EventState[0] = 0 And e\EventState[7] = 0 Then
					Animate2(e\room\Objects[REACTOR_MODEL], AnimTime(e\room\Objects[REACTOR_MODEL]), 1.0, 500.0, 0.3,True)
				ElseIf e\EventState[7] > 0 Then
					Animate2(e\room\Objects[REACTOR_MODEL], AnimTime(e\room\Objects[REACTOR_MODEL]), 501.0, 1390.0, 0.3,False)
				EndIf
				
				If EntityDistanceSquared(Collider, e\room\Objects[REACTOR_MODEL]) < PowTwo(15.0) Then
					If e\EventState[2] = 0 Then
						If e\EventState[0] = 0 Then
							If ChannelPlaying(e\SoundCHN[1])=False Then
								e\SoundCHN[1] = PlaySound_Strict(LoadTempSound("SFX\General\Reactor_Idle.ogg"))
							EndIf
						EndIf
					EndIf
				EndIf
				If e\EventState[0] = 3 Then
					If ChannelPlaying(e\SoundCHN[1]) Then
						StopChannel(e\SoundCHN[1])
					EndIf
				EndIf
				
				e\Sound[0] = LoadSound_Strict("SFX\General\Spark_Short.ogg")
				
				If e\EventState[6] = 1 Then
					e\EventState[7] = e\EventState[7] + FPSfactor
					PointEntity(Camera, e\room\Objects[REACTOR_PIVOT])
				EndIf
				
				If TaskExists(TASK_TURN_ON_REACTOR) Then
					If e\EventState[0] = 0 Then
						If e\EventState[7] = 0 Then UpdateButton(e\room\Objects[REACTOR_TURN_ON_BUTTON])
						If d_I\ClosestButton = e\room\Objects[REACTOR_TURN_ON_BUTTON] And KeyHitUse Then
							PlaySound_Strict ButtonSFX[3]
							e\EventState[6] = 1
							psp\NoMove = True
							psp\NoRotation = True
							psp\IsShowingHUD = False
							If ChannelPlaying(e\SoundCHN[1]) Then
								StopChannel(e\SoundCHN[1])
							EndIf
							If (Not ChannelPlaying(e\SoundCHN[1])) Then
								e\SoundCHN[1] = PlaySound_Strict(LoadTempSound("SFX\General\Reactor_Power_Up.ogg"))
							EndIf
							SaveGame(SavePath + CurrSave\Name + "\", True)
							HolsterGun()
							CanPlayerUseGuns = False
						EndIf
						; ~ Reactor Cutscene
						If e\EventState[7] >= 70*29.1 And e\EventState[7] < 70*30 Then
							p.Particles = CreateParticle(EntityX(e\room\Objects[REACTOR_PIVOT],True), EntityY(e\room\Objects[REACTOR_PIVOT],True)+Rnd(0.0,0.05), EntityZ(e\room\Objects[REACTOR_PIVOT],True),2, 1.1, -0.2, 250.0)
							p\speed = Rnd(0.05, 0.03) : p\size = Rnd(1.1, 1.15) : p\Achange = -0.1
							RotateEntity(p\pvt, Rnd(-20.0, 0.0), e\room\angle, 0.0)
							ScaleSprite(p\obj, p\size, p\size)
							
							psp\NoMove = False
							psp\NoRotation = False
							If HUDenabled Then psp\IsShowingHUD = True
							e\EventState[6] = 2
							e\EventState[7] = -10
							e\EventState[0] = 1
							e\EventState[1] = e\EventState[1] + FPSfactor
							e\room\RoomDoors[1]\open = True
							If TaskExists(TASK_TURN_ON_REACTOR) And (Not TaskExists(TASK_STOP_REACTOR)) Then
								FailTask(TASK_TURN_ON_REACTOR)
								BeginTask(TASK_STOP_REACTOR)
							EndIf
							CanPlayerUseGuns = True
							SaveGame(SavePath + CurrSave\Name + "\", True)
							ShouldPlay = MUS_NULL
							If e\EventState[2] = 0 Then
								If ChannelPlaying(e\SoundCHN[0]) Then
									StopChannel(e\SoundCHN[0])
								EndIf
								If (Not ChannelPlaying(e\SoundCHN[0])) Then
									If opt\MusicVol > 0 Then
										e\SoundCHN[0] = PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Reactor_Explosion.ogg"))
									EndIf
								EndIf
							ElseIf e\EventState[2] <> 0 Then
								If ChannelPlaying(e\SoundCHN[0]) Then
									StopChannel(e\SoundCHN[0])
								EndIf
								If ChannelPlaying(e\SoundCHN[0])=False Then
									e\SoundCHN[0] = PlaySound_Strict(LoadTempSound("SFX\General\Reactor_Stop.ogg"))
								EndIf
							EndIf
						ElseIf e\EventState[7] > 0 And e\EventState[7] < 70*8 Then
							PositionEntity Camera,-1777*RoomScale, -1146*RoomScale+(e\EventState[7]/500), 13*RoomScale-(e\EventState[7]/200)
							PointEntity(Camera,e\room\Objects[REACTOR_PIVOT])
;							MoveEntity(e\room\Objects[REACTOR_WATER], 0, -10, 0)
						ElseIf e\EventState[7] > 70*8 And e\EventState[7] < 70*15 Then
							;RotateEntity Camera,0,-40,0
							PositionEntity Camera,-591*RoomScale, -1877*RoomScale-(e\EventState[7]/800), -472*RoomScale-(e\EventState[7]/500)
							PointEntity(Camera,e\room\Objects[REACTOR_PIVOT])
						ElseIf e\EventState[7] > 70*15 And e\EventState[7] < 70*22 Then
							;RotateEntity Camera,0,200,0
							PositionEntity Camera,-58*RoomScale, 242*RoomScale-(e\EventState[7]/800), 3792*RoomScale
							PointEntity(Camera,e\room\Objects[REACTOR_PIVOT])
						ElseIf e\EventState[7] > 70*22 And e\EventState[7] < 70*30 Then
							
							CameraShake = 0.5
							
							If Rand(50) = 1 Then
								PlaySound2(e\Sound[0], Camera, e\room\Objects[REACTOR_TURN_OFF_BUTTON], 3.0, 0.4)
								If ParticleAmount > 0 Then
									For i = 0 To (2 + (1 * (ParticleAmount - 1)))
										p.Particles = CreateParticle(EntityX(e\room\Objects[REACTOR_TURN_OFF_BUTTON],True), EntityY(e\room\Objects[REACTOR_TURN_OFF_BUTTON],True)+Rnd(0.0,0.05), EntityZ(e\room\Objects[REACTOR_TURN_OFF_BUTTON],True),7, 0.002, 0.0, 25.0)
										p\speed = Rnd(0.005, 0.03) : p\size = Rnd(0.005, 0.0075) : p\Achange = -0.05
										RotateEntity(p\pvt, 0.0, Rnd(-20.0, 0.0), 0.0)
										ScaleSprite(p\obj, p\size, p\size)
									Next
								EndIf	
							EndIf
							
							p.Particles = CreateParticle(EntityX(e\room\Objects[REACTOR_PIVOT],True), EntityY(e\room\Objects[REACTOR_PIVOT],True)+Rnd(0.0,0.05), EntityZ(e\room\Objects[REACTOR_PIVOT],True),2, 1.1, -0.2, 250.0)
							p\speed = Rnd(0.05, 0.03) : p\size = Rnd(1.1, 1.15) : p\Achange = -0.1
							RotateEntity(p\pvt, 0.0, Rnd(-20.0, 0.0), 0.0)
							ScaleSprite(p\obj, p\size, p\size)
							
							;RotateEntity Camera,0,235,0
							PointEntity(Camera,e\room\Objects[REACTOR_PIVOT])
							
							PositionEntity Camera,-578*RoomScale, -1614*RoomScale, 338*RoomScale
						EndIf
					EndIf
				EndIf
				
				If e\EventState[0] <> 1 Then
					SetAnimTime(e\room\Objects[REACTOR_SHIELD],1)
				EndIf
				
				If e\EventState[0] = 1 Then
					Animate2(e\room\Objects[REACTOR_SHIELD], AnimTime(e\room\Objects[REACTOR_SHIELD]), 1.0, 20.0, 0.13,False)
				Else
					SetAnimTime(e\room\Objects[REACTOR_SHIELD],1)
				EndIf
				
				If e\EventState[0] = 1 Then
					If EntityHidden(e\room\Objects[REACTOR_PART_4]) Then ShowEntity e\room\Objects[REACTOR_PART_4]
					InfiniteStamina = True
				Else
					InfiniteStamina = False
				EndIf
				
				If e\room\RoomDoors[2]\open = False And e\room\RoomDoors[2]\locked = True Then
					HideEntity e\room\Objects[REACTOR_PART_2]
					HideEntity e\room\Objects[REACTOR_PART_3]
					HideEntity e\room\Objects[REACTOR_PART_5]
					ShowEntity e\room\Objects[REACTOR_FAKE_DOOR]
				EndIf
				
				If TaskExists(TASK_STOP_REACTOR) Then
					If e\EventState[0] = 1 Then
						UpdateButton(e\room\Objects[REACTOR_TURN_OFF_BUTTON])
						If d_I\ClosestButton = e\room\Objects[REACTOR_TURN_OFF_BUTTON] And KeyHitUse Then
							PlaySound_Strict ButtonSFX[3]
							e\EventState[0] = 2
;							SaveGame(SavePath + CurrSave\Name + "\", True)
							SetAnimTime(e\room\Objects[REACTOR_SHIELD],1)
						EndIf
					EndIf
					
					If e\EventState[0] <> 0 Then
						e\EventState[1] = e\EventState[1] + FPSfactor
						ShouldPlay = MUS_NULL
					EndIf
					
					If e\EventState[1] > 0 And e\EventState[1] < 70*50 Then
						If Rand(50) = 1 Then
							PlaySound2(e\Sound[0], Camera, e\room\Objects[REACTOR_TURN_ON_BUTTON], 3.0, 0.4)
							If ParticleAmount > 0 Then
								For i = 0 To (2 + (1 * (ParticleAmount - 1)))
									p.Particles = CreateParticle(EntityX(e\room\Objects[REACTOR_TURN_ON_BUTTON],True), EntityY(e\room\Objects[REACTOR_TURN_ON_BUTTON],True)+Rnd(0.0,0.05), EntityZ(e\room\Objects[REACTOR_TURN_ON_BUTTON],True),7, 0.002, 0.0, 25.0)
									p\speed = Rnd(0.005, 0.03) : p\size = Rnd(0.005, 0.0075) : p\Achange = -0.05
									RotateEntity(p\pvt, 0.0, Rnd(-20.0, 0.0), 0.0)
									ScaleSprite(p\obj, p\size, p\size)
								Next
							EndIf	
						EndIf
					EndIf
					
					If e\EventState[0] <> 2 Then
						If e\EventState[1] >= 70*49.9 And e\EventState[1] < 70*50 Then
							If TaskExists(TASK_STOP_REACTOR) Then
								FailTask(TASK_STOP_REACTOR)
							EndIf
							CanSave = False
							ExplosionTimer = 1
							m_msg\DeathTxt=GetLocalString("Singleplayer","reactor_explosion")
						Else
							CanSave = True
						EndIf
					Else
						e\EventState[2] = e\EventState[2] + FPSfactor
						If e\EventState[2] > 0 And e\EventState[2] < 70*5 Then
							SetAnimTime(e\room\Objects[REACTOR_SHIELD],1)
							e\room\RoomDoors[2]\open = False : e\room\RoomDoors[2]\locked = True
							e\EventState[0] = 3
							If e\EventState[0] = 3 Then
								If ChannelPlaying(e\SoundCHN[1]) Then
									StopChannel(e\SoundCHN[1])									
								EndIf
							EndIf
							If ChannelPlaying(e\SoundCHN[0]) Then
								StopChannel(e\SoundCHN[0])
								If opt\MusicVol > 0 Then
									e\SoundCHN[1] = PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Reactor_Explosion_End.ogg"))
									e\SoundCHN_isStream[1] = True
								EndIf
							EndIf
							e\EventState[1] = -10
							e\EventState[0] = 4
							BigCameraShake = 10
							If TaskExists(TASK_STOP_REACTOR) And (Not TaskExists(TASK_COME_BACK_TO_GUARD_2)) Then
								EndTask(TASK_STOP_REACTOR)
								If (Not ecst\KilledGuard) Then
									BeginTask(TASK_COME_BACK_TO_GUARD_2)
								EndIf
								If ChannelPlaying(e\SoundCHN[0])=False Then
									If opt\MusicVol > 0 Then
										e\SoundCHN[0] = PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Reactor_Core_2.ogg"))
									EndIf
								EndIf
							EndIf
						EndIf
					EndIf
				EndIf
				
				If e\EventState[0] = 4 Then
					If EntityDistanceSquared(Collider,e\room\Objects[REACTOR_CLOSE_TRIGGER])<PowTwo(0.6) Then
						e\room\RoomDoors[1]\open = False
						e\room\RoomDoors[1]\locked = True
					EndIf
					UpdateButton(e\room\Objects[REACTOR_EXIT_BUTTON])
					If d_I\ClosestButton = e\room\Objects[REACTOR_EXIT_BUTTON] Then
						If KeyHitUse Then
							e\EventState[5] = 0.1
							PlaySound_Strict(ButtonSFX[0])
						EndIf
					EndIf
				EndIf
					
				If e\EventState[5] > 0 Then
					e\EventState[5] = e\EventState[5] + (0.01*FPSfactor)
					EntityAlpha e\room\Objects[REACTOR_DARK_SPRITE],Min(e\EventState[5],1.0)
				EndIf
				
				If e\EventState[5] > 1.05 Then
					
					ecst\WasInReactor = True
					
					e\room\Objects[REACTOR_MODEL] = FreeEntity_Strict(e\room\Objects[REACTOR_MODEL])
					e\room\Objects[REACTOR_PART_2] = FreeEntity_Strict(e\room\Objects[REACTOR_PART_2])
					e\room\Objects[REACTOR_PART_3] = FreeEntity_Strict(e\room\Objects[REACTOR_PART_3])
					e\room\Objects[REACTOR_PART_4] = FreeEntity_Strict(e\room\Objects[REACTOR_PART_4])
					e\room\Objects[REACTOR_PART_5] = FreeEntity_Strict(e\room\Objects[REACTOR_PART_5])
					e\room\Objects[REACTOR_SHIELD] = FreeEntity_Strict(e\room\Objects[REACTOR_SHIELD])
					e\room\Objects[REACTOR_BRIDGES] = FreeEntity_Strict(e\room\Objects[REACTOR_BRIDGES])
					e\room\Objects[REACTOR_FAKE_DOOR] = FreeEntity_Strict(e\room\Objects[REACTOR_FAKE_DOOR])
					
					SaveGame(SavePath + CurrSave\Name + "\", True)
					Local prevZone = gopt\CurrZone
					
					gopt\CurrZone = HCZ
					
					ResetControllerSelections()
					DropSpeed = 0
					NullGame(True,False)
					LoadEntities()
					LoadAllSounds()
					Local zonecache% = gopt\CurrZone
					If FileType(SavePath + CurrSave\Name + "\" + gopt\CurrZone + ".sav") = 1 Then
						LoadGame(SavePath + CurrSave\Name + "\", gopt\CurrZone)
						InitLoadGame()
					Else
						InitNewGame()
						LoadDataForZones(SavePath + CurrSave\Name + "\")
					EndIf
					gopt\CurrZone = zonecache
					MainMenuOpen = False
					FlushKeys()
					FlushMouse()
					FlushJoy()
					ResetInput()
					For e2 = Each Events
						If e2\room = PlayerRoom Then
							e2\EventState[0] = 0
							TeleportEntity(Collider, EntityX(e2\room\obj), 1.0, EntityZ(e2\room\obj), 0.3, True)
							RotateEntity(Collider, 0, e2\room\angle, 0)
							Exit
						EndIf
					Next
					SaveGame(SavePath + CurrSave\Name + "\", True)
					Return
				EndIf
				
			Else
				e\room\Objects[REACTOR_MODEL] = FreeEntity_Strict(e\room\Objects[REACTOR_MODEL])
				e\room\Objects[REACTOR_PART_2] = FreeEntity_Strict(e\room\Objects[REACTOR_PART_2])
				e\room\Objects[REACTOR_PART_3] = FreeEntity_Strict(e\room\Objects[REACTOR_PART_3])
				e\room\Objects[REACTOR_PART_4] = FreeEntity_Strict(e\room\Objects[REACTOR_PART_4])
				e\room\Objects[REACTOR_PART_5] = FreeEntity_Strict(e\room\Objects[REACTOR_PART_5])
				e\room\Objects[REACTOR_SHIELD] = FreeEntity_Strict(e\room\Objects[REACTOR_SHIELD])
				e\room\Objects[REACTOR_BRIDGES] = FreeEntity_Strict(e\room\Objects[REACTOR_BRIDGES])
				e\room\Objects[REACTOR_FAKE_DOOR] = FreeEntity_Strict(e\room\Objects[REACTOR_FAKE_DOOR])
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D