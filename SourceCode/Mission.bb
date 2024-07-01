CreateMission(0)
CreateMission(1)

Type MissionRoom
	Field RData$
	Field Angle$
	Field Event$
End Type

Global MissionRooms.MissionRoom[MapWidth * MapHeight]

Function CreateMissionRoom(x%, y%, RData$, Angle%=0, Event$="")
	Local I_ROM.MissionRoom = New MissionRoom
	
	I_ROM\RData = RData
	I_ROM\Angle = Angle
	I_ROM\Event = Event
	MissionRooms[((MapWidth/2-1) + x) * MapWidth + (MapHeight + y)] = I_ROM
	
End Function

mi_I.MissionInstance = New MissionInstance

Type MissionInstance
	Field EndingTimer#
	Field IsEnding%
	Field FadeEntity%
	Field currID%
	Field EndSFX%
	Field EndSFX_Vol#
End Type

Type Mission
	Field ID%
	Field txt$
	Field startroom$
	Field musicname$
	Field InvDataName$[10]
	Field InvDataTempName$[10]
	Field TaskAmount%
	Field TaskText$[5]
	Field GunInHand%
	Field GunUpgraded%[10]
	Field Zone%
	Field ItemDataName$[16]
	Field ItemDataTempName$[16]
	Field ItemDataX#[16]
	Field ItemDataY#[16]
	Field ItemDataZ#[16]
	Field ItemDataYaw#[16]
	Field ItemDataRoomX#[16]
	Field ItemDataRoomZ#[16]
	Field Spawn173%=True
	Field Spawn106%=True
End Type

Function CreateMission.Mission(id%)
	Local mi.Mission = New Mission
	
	mi\ID = id
	Select id
		Case 0
			mi\txt = "Test"
			mi\TaskAmount = 1
			mi\TaskText[0] = "Find a FN P90"
			mi\TaskText[1] = "Go to a checkpoint room"
		Case 1
			mi\txt = "Test"
			mi\TaskAmount = 3
			mi\TaskText[0] = "Kill a guard"
			mi\TaskText[1] = "Get a Level 3 Key Card"
			mi\TaskText[2] = "Steal SCP-173's document"
			mi\TaskText[3] = "Escape via an elevator"
	End Select
	
	Return mi
End Function

Function InitMission(id%)
	Local mi.Mission,currmi.Mission
	Local r.Rooms, ta.Task
	Local I_ROM.MissionRoom
	
	For mi = Each Mission
		If mi\ID = id
			currmi = mi
			Exit
		EndIf
	Next
	mi = Null
	
	For I_ROM.MissionRoom = Each MissionRoom
		Delete I_ROM
	Next
	
	Select id
		Case 0
			;[Block]
			CreateTask(currmi\TaskText[0],0,"FN P90","p90")
			CreateTask(currmi\TaskText[1],1,"checkpoint_lcz")
			
			currmi\InvDataName[0] = "H&K USP"
			currmi\InvDataTempName[0] = "usp"
			currmi\InvDataName[1] = GetLocalString("Item Names","key_5")
			currmi\InvDataTempName[1] = "key5"
			currmi\ItemDataName[0] = "FN P90"
			currmi\ItemDataTempName[0] = "p90"
			currmi\ItemDataX[0] = -670.0
			currmi\ItemDataY[0] = 155.0
			currmi\ItemDataZ[0] = 460.0
			currmi\ItemDataYaw[0] = 0.0
			;Might need to be changed when the map algorithm changes - ENDSHN
			currmi\ItemDataRoomX#[0] = x-2
			currmi\ItemDataRoomZ#[0] = y-6
			
			currmi\musicname = ""
			currmi\startroom = "cont_173"
			
			currmi\GunInHand = 1
			gopt\CurrZone = LCZ
			currmi\Zone = gopt\CurrZone
			
			currmi\Spawn106 = True
			currmi\Spawn173 = True
			
			CreateMissionRoom(0, 0, "cont_173", 180)
			;CreateMissionRoom(0, -1, "room3_1")
			;CreateMissionRoom(1, -1, "room2_1", 90)
			;CreateMissionRoom(2, -1, "lockroom", 180, "lockroom173")
			;CreateMissionRoom(2, -2, "room2_2", 0, "room2fan")
			
			; When we reimplement missions, convert all this
			
			;+2,-3
			; RoomData(x+2,y-3) = "room4"
			; RoomDataEvent(x+2,y-3) = "106sinkhole"
			; ;+3,-3
			; RoomData(x+3,y-3) = "room2gw"
			; RoomDataAngle(x+3,y-3) = 270
			; RoomDataEvent(x+3,y-3) = "room_gw"
			; ;+4,-3
			; RoomData(x+4,y-3) = "endroom"
			; RoomDataAngle(x+4,y-3) = 90
			; RoomDataEvent(x+4,y-3) = "endroom106"
			; ;+1,-3
			; RoomData(x+1,y-3) = "room205"
			; RoomDataAngle(x+1,y-3) = 270
			; RoomDataEvent(x+1,y-3) = "room205"
			; ;+2,-4
			; RoomData(x+2,y-4) = "room2_4"
			; RoomDataEvent(x+2,y-4) = "room2pit"
			; ;+2,-5
			; RoomData(x+2,y-5) = "room2elevator"
			; RoomDataEvent(x+2,y-5) = "room2elevator2"
			; ;+2,-6
			; RoomData(x+2,y-6) = "lockroom3"
			; RoomDataAngle(x+2,y-6) = 90
			; ;+1,-6
			; RoomData(x+1,y-6) = "room2doors"
			; RoomDataAngle(x+1,y-6) = 90
			; RoomDataEvent(x+1,y-6) = "room2doors173"
			; ;-1,-1
			; RoomData(x-1,y-1) = "room2doors"
			; RoomDataAngle(x-1,y-1) = 270
			; ;-2,-1
			; RoomData(x-2,y-1) = "room3_2"
			; RoomDataAngle(x-2,y-1) = 180
			; RoomDataEvent(x-2,y-1) = "106victim"
			; ;-3,-1
			; RoomData(x-3,y-1) = "room2c2"
			; RoomDataAngle(x-3,y-1) = 270
			; RoomDataEvent(x-3,y-1) = "1048a"
			; ;-3,-2
			; RoomData(x-3,y-2) = "room2scps"
			; RoomDataAngle(x-3,y-2) = 180
			; ;-3,-3
			; RoomData(x-3,y-3) = "room3"
			; RoomDataAngle(x-3,y-3) = 90
			; ;-4,-3
			; RoomData(x-4,y-3) = "room2testroom2"
			; RoomDataAngle(x-4,y-3) = 90
			; RoomDataEvent(x-4,y-3) = "tunnel106"
			; ;-5,-3
			; RoomData(x-5,y-3) = "checkpoint1_new"
			; RoomDataAngle(x-5,y-3) = 90
			; ;-3,-4
			; RoomData(x-3,y-4) = "room1123"
			; RoomDataEvent(x-3,y-4) = "room1123"
			; ;-3,-5
			; RoomData(x-3,y-5) = "room1archive"
			; ;-2,-2
			; RoomData(x-2,y-2) = "room2"
			; RoomDataEvent(x-2,y-2) = "room2trick"
			; ;-2,-3
			; RoomData(x-2,y-3) = "room012"
			; RoomDataAngle(x-2,y-3) = 180
			; RoomDataEvent(x-2,y-3) = "room012"
			; ;-2,-4
			; RoomData(x-2,y-4) = "room1162"
			; RoomDataEvent(x-2,y-4) = "room1162"
			; ;-1,-4
			; RoomData(x-1,y-4) = "room2storage"
			; RoomDataAngle(x-1,y-4) = 90
			; RoomDataEvent(x-1,y-4) = "room2storage"
			; ;0,-4
			; RoomData(x,y-4) = "room3"
			; RoomDataAngle(x,y-4) = 90
			; RoomDataEvent(x,y-4) = "106sinkhole"
			; ;0,-3
			; RoomData(x,y-3) = "endroom"
			; RoomDataAngle(x,y-3) = 180
			; ;0,-5
			; RoomData(x,y-5) = "room2closets"
			; RoomDataAngle(x,y-5) = 180
			; RoomDataEvent(x,y-5) = "room2closets"
			; ;0,-6
			; RoomData(x,y-6) = "room4"
			; RoomDataAngle(x,y-6) = 270
			; ;0,-7
			; RoomData(x,y-7) = "914"
			; RoomDataEvent(x,y-7) = "914"
			; ;-1,-6
			; RoomData(x-1,y-6) = "room2_3"
			; RoomDataAngle(x-1,y-6) = 270
			; RoomDataEvent(x-1,y-6) = "1048a"
			; ;-2,-6
			; RoomData(x-2,y-6) = "room1archive"
			; RoomDataAngle(x-2,y-6) = 270
			;[End Block]
		Case 1
			;[Block]
			CreateTask(currmi\TaskText[2],0,"Document SCP-173","paper")
			;ta = CreateTask(currmi\TaskText[3],2,448,0,0,False,"room2_elevator")
			;ta\chaintask = CreateTask("",3,752,240,0,280,"room2_elevator")
			;CreateMissionEvent(0,448,0,0,False,"room2_elevator")
			
			currmi\InvDataName[0] = "H&K USP"
			currmi\InvDataTempName[0] = "usp"
			
			currmi\musicname = "Mission2Test"
			currmi\startroom = "checkpoint_lcz"
			
			currmi\GunInHand = 1
			gopt\CurrZone = 1
			currmi\GunUpgraded[1] = 1
			currmi\Zone = gopt\CurrZone
			
			currmi\Spawn106 = False
			currmi\Spawn173 = False
			
			; Same here
			
			; ;0,-8
			; RoomData(x,y-8) = "checkpoint1_new"			; RoomDataAngle(x,y-8) = 180
			; ;0,-7
			; RoomData(x,y-7) = "room4"
			; ;1,-7
			; RoomData(x+1,y-7) = "room2"
			; RoomDataAngle(x+1,y-7) = 90
			; ;2,-7
			; RoomData(x+2,y-7) = "room3"
			; ;2,-6
			; RoomData(x+2,y-6) = "endroom"
			; RoomDataAngle(x+2,y-6) = 180
			; ;3,-7
			; RoomData(x+3,y-7) = "room3"
			; RoomDataAngle(x+3,y-7) = 90
			; ;-3,-8
			; RoomData(x-3,y-8) = "endroom"
			; ;RoomDataAngle(x-3,y-8) = 180
			; ;3,-6
			; RoomData(x+3,y-6) = "room2"
			; ;3,-5
			; RoomData(x+3,y-5) = "room3"
			; RoomDataAngle(x+3,y-5) = 90
			; ;3,-4
			; RoomData(x+3,y-4) = "room2c"
			; RoomDataAngle(x+3,y-4) = 180
			; ;-1,-7
			; RoomData(x-1,y-7) = "room2"
			; RoomDataAngle(x-1,y-7) = 90
			; ;-2,-7
			; RoomData(x-2,y-7) = "endroom"
			; RoomDataAngle(x-2,y-7) = -90
			; ;0,-6
			; RoomData(x,y-6) = "room3"
			; RoomDataAngle(x,y-6) = 90
			; ;0,-5
			; RoomData(x,y-5) = "room3"
			; RoomDataAngle(x,y-5) = 270
			; ;-1,-5
			; RoomData(x-1,y-5) = "room2elevator"
			; ;RoomDataAngle(x-1,y-5) = 90
			; ;-2,-5
			; RoomData(x-2,y-5) = "room3"
			; RoomDataAngle(x-2,y-5) = 90
			; ;-2,-6
			; RoomData(x-2,y-6) = "endroom"
			; RoomDataAngle(x-2,y-6) = -90
			; ;-2,-4
			; RoomData(x-2,y-4) = "room2"
			; ;0,-4
			; RoomData(x,y-4) = "room3"
			; RoomDataAngle(x,y-4) = 180
			; ;-1,-4
			; RoomData(x-1,y-4) = "room2c"
			; ;-1,-3
			; RoomData(x-1,y-3) = "room4"
			; ;0,-3
			; RoomData(x,y-3) = "endroom"
			; ;-2,-3
			; RoomData(x-2,y-3) = "room4"
			; ;-3,-3
			; RoomData(x-3,y-3) = "endroom"
			; ;RoomDataAngle(x-3,y-3) = 0
			; ;-1,-2
			; RoomData(x-1,y-2) = "room2closets"
			; ;-1,-1
			; RoomData(x-1,y-1) = "room2c"
			; RoomDataAngle(x-1,y-1) = 180
			; ;-2,-1
			; RoomData(x-2,y-1) = "room2c"
			; RoomDataAngle(x-2,y-1) = 270
			; ;-2,-2
			; RoomData(x-2,y-2) = "room3"
			; RoomDataAngle(x-2,y-2) = 90
			; ;-3,-2
			; RoomData(x-3,y-2) = "endroom"
			; RoomDataAngle(x-3,y-2) = 270
			;[End block]
		Default
			
	End Select
	currmi = Null
	
End Function

Function CreateMissionMap(id%)
	CatchErrors("Uncaught (CreateMissionMap)")
	
	Local mi.Mission,currmi.Mission
	Local r.Rooms,r2.Rooms,rt.RoomTemplates,i%
	
	For mi = Each Mission
		If mi\ID = id
			currmi = mi
			Exit
		EndIf
	Next
	mi = Null
	
	Local f%, x%, y%, name$, angle%, prob#, e.Events
	
	For x = 0 To MapWidth-1
		For y = 0 To MapHeight-1
			
			If MissionRooms[x * MapWidth + y] <> Null Then
				Local I_ROM.MissionRoom = MissionRooms[x * MapWidth + y]
				name$ = I_ROM\RData
				
				If name$ <> ""
					angle = I_ROM\Angle
					
					;debuglog x+", "+y+": "+name
					;debuglog "angle: "+angle
					
					For rt.RoomTemplates=Each RoomTemplates
						If Lower(rt\Name) = name Then
							
							r.Rooms = CreateRoom(0, rt\Shape, x * 8.0, 0, y * 8.0, name)
							;debuglog "createroom"
							
							r\angle = angle
							If rt\Shape = ROOM2C Then r\angle = r\angle+90 Else r\angle = r\angle-180
							
							TurnEntity(r\obj, 0, r\angle, 0)
							
							MapTemp[x * MapWidth + y]=1
							
							Exit
						EndIf
					Next
					
					If r<>Null Then
						If I_ROM\Event<>""
							e.Events = New Events
							e\EventName = I_ROM\Event
							e\room = r
						EndIf
					EndIf
				EndIf
			EndIf
		Next
	Next
	
	Local temp = 0
	Local spacing# = 8.0
	Local zone%,d.Doors
	For y = MapHeight - 1 To 1 Step - 1
		
		If y < MapHeight/3+1 Then
			zone=3
		ElseIf y < MapHeight*(2.0/3.0)-1
			zone=2
		Else
			zone=1
		EndIf
		
		For x = 1 To MapWidth - 2
			If MapTemp[x * MapWidth + y] > 0 Then
				If gopt\CurrZone = HCZ Then temp=2 Else temp=0
                
                For r.Rooms = Each Rooms
					If Int(r\x/8.0)=x And Int(r\z/8.0)=y Then
						If MapTemp[(x + 1) * MapWidth + y] > 0 Then
							d.Doors = CreateDoor(r\zone, Float(x) * spacing + spacing / 2.0, 0, Float(y) * spacing, 90, r, Max(Rand(-3, 1), 0), temp)
							r\AdjDoor[0] = d
						EndIf
						
						If MapTemp[x * MapWidth + y + 1] > 0 Then
							d.Doors = CreateDoor(r\zone, Float(x) * spacing, 0, Float(y) * spacing + spacing / 2.0, 0, r, Max(Rand(-3, 1), 0), temp)
							r\AdjDoor[3] = d
						EndIf
						
						Exit
					EndIf
                Next
                
			End If
			
		Next
	Next   
	
	r = CreateRoom(0, ROOM1, (MapWidth-1) * 8, 0, (MapHeight-1) * 8, "pocketdimension")
	
	CreateEvent("pocketdimension", "pocketdimension", 0)
	
	For r.Rooms = Each Rooms
		r\Adjacent[0]=Null
		r\Adjacent[1]=Null
		r\Adjacent[2]=Null
		r\Adjacent[3]=Null
		For r2.Rooms = Each Rooms
			If r<>r2 Then
				If r2\z=r\z Then
					If (r2\x)=(r\x+8.0) Then
						r\Adjacent[0]=r2
						If r\AdjDoor[0] = Null Then r\AdjDoor[0] = r2\AdjDoor[2]
					ElseIf (r2\x)=(r\x-8.0)
						r\Adjacent[2]=r2
						If r\AdjDoor[2] = Null Then r\AdjDoor[2] = r2\AdjDoor[0]
					EndIf
				ElseIf r2\x=r\x Then
					If (r2\z)=(r\z-8.0) Then
						r\Adjacent[1]=r2
						If r\AdjDoor[1] = Null Then r\AdjDoor[1] = r2\AdjDoor[3]
					ElseIf (r2\z)=(r\z+8.0)
						r\Adjacent[3]=r2
						If r\AdjDoor[3] = Null Then r\AdjDoor[3] = r2\AdjDoor[1]
					EndIf
				EndIf
			EndIf
			If (r\Adjacent[0]<>Null) And (r\Adjacent[1]<>Null) And (r\Adjacent[2]<>Null) And (r\Adjacent[3]<>Null) Then Exit
		Next
	Next
	
	currmi = Null
	
	CatchErrors("CreateMissionMap")
End Function

Function InitMissionGameMode(id%)
	CatchErrors("Uncaught (InitNewGame)")
	Local i%, de.Decals, d.Doors, it.Items, r.Rooms, sc.SecurityCams, e.Events, g.Guns
	Local currMission.Mission, mi.Mission
	For mi = Each Mission
		If mi\ID = id
			currMission = mi
		EndIf
	Next
	Local x#,y#,z#
	
	mi_I\FadeEntity = CreateSprite(Camera)
	EntityAlpha mi_I\FadeEntity,0.0
	ScaleSprite mi_I\FadeEntity,3,3
	MoveEntity mi_I\FadeEntity,0,0,1
	EntityColor mi_I\FadeEntity,0,0,0
	EntityFX mi_I\FadeEntity,1
	EntityOrder mi_I\FadeEntity,-9000
	
	mi_I\currID = id%
	
	DrawLoading(45,False,"","Creating_missions")
	
	HideDistance# = 15.0
	
	HeartBeatRate = 70
	
	Local strtemp$ = ""
	For i = 1 To Len(RandomSeed)
		strtemp = strtemp+Asc(Mid(RandomSeed,i,1))
	Next
	SeedRnd Abs(Int(strtemp))
	
	For i = 0 To 3 
		AccessCode[i] = 0
		AccessCode[i] = Rand(1000,9999)
	Next
	
	CreateMissionMap(currMission\ID)
	
	InitWayPoints()
	
	DrawLoading(79,False,"","NPCs")
	
	If currMission\Spawn173
		Curr173 = CreateNPC(NPC_scP_173, 0, -30.0, 0)
	EndIf
	If currMission\Spawn106
		Curr106 = CreateNPC(NPC_SCP_106, 0, -30.0, 0)
		Curr106\State[0] = 70 * 60 * Rand(12,17)
	EndIf
	
	For d.Doors = Each Doors
		EntityParent(d\obj, 0)
		If d\obj2 <> 0 Then EntityParent(d\obj2, 0)
		If d\frameobj <> 0 Then EntityParent(d\frameobj, 0)
		If d\buttons[0] <> 0 Then EntityParent(d\buttons[0], 0)
		If d\buttons[1] <> 0 Then EntityParent(d\buttons[1], 0)
		
		If d\obj2 <> 0 And d\dir = 0 Then
			MoveEntity(d\obj, 0, 0, 8.0 * RoomScale)
			MoveEntity(d\obj2, 0, 0, 8.0 * RoomScale)
		EndIf	
	Next
	
	For it.Items = Each Items
		EntityType (it\collider, HIT_ITEM)
		EntityParent(it\collider, 0)
	Next
	
	DrawLoading(80,False,"","Rooms")
	For sc.SecurityCams= Each SecurityCams
		sc\angle = EntityYaw(sc\obj) + sc\angle
		EntityParent(sc\obj, 0)
	Next	
	
	For r.Rooms = Each Rooms
		For i = 0 To MaxRoomLights-1
			If r\Lights[i]<>0 Then EntityParent(r\Lights[i],0)
		Next
		
		If (Not r\RoomTemplate\DisableDecals) Then
			If Rand(4) = 1 Then
				de.Decals = CreateDecal(GetRandomDecalID(DECAL_TYPE_BLOODSPLAT), EntityX(r\obj)+Rnd(- 2,2), 0.003, EntityZ(r\obj)+Rnd(-2,2), 90, Rand(360), 0)
				de\Size = Rnd(0.1, 0.4) : ScaleSprite(de\obj, de\Size, de\Size)
				EntityAlpha(de\obj, Rnd(0.85, 0.95))
			EndIf
			
			If Rand(4) = 1 Then
				de.Decals = CreateDecal(DECAL_DECAY, EntityX(r\obj)+Rnd(- 2,2), 0.003, EntityZ(r\obj)+Rnd(-2,2), 90, Rand(360), 0)
				de\Size = Rnd(0.5, 0.7) : EntityAlpha(de\obj, 0.7) : de\ID = 1 : ScaleSprite(de\obj, de\Size, de\Size)
				EntityAlpha(de\obj, Rnd(0.7, 0.85))
			EndIf
		EndIf
		
		If r\RoomTemplate\Name = currMission\startroom Then
			PositionEntity (Collider, EntityX(r\obj), 0.5, EntityZ(r\obj))
			PlayerRoom = r
		EndIf
	Next
	
	For i = 0 To MaxItemAmount-1
		If currMission\InvDataName[i]<>"" And currMission\InvDataTempName[i]<>""
			it = CreateItem(currMission\InvDataName[i],currMission\InvDataTempName[i],1,1,1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			Inventory[i] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
		EndIf
	Next
	g_I\HoldingGun = currMission\GunInHand
	For i = 0 To 15
		If currMission\ItemDataName[i]<>"" And currMission\ItemDataTempName[i]<>""
			Local room.Rooms
			For r = Each Rooms
				;If r\x = currMission\ItemDataRoomX[i]*8 And r\z = currMission\ItemDataRoomZ[i]*8
					room = r
					Exit
				;EndIf
			Next
			Local pvt% = CreatePivot(room\obj)
			PositionEntity pvt,currMission\ItemDataX[i],currMission\ItemDataY[i],currMission\ItemDataZ[i],False
			it = CreateItem(currMission\ItemDataName[i],currMission\ItemDataTempName[i],EntityX(pvt,True),EntityY(pvt,True),EntityZ(pvt,True))
			TurnEntity it\collider,0,currMission\ItemDataYaw[i],0
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			pvt = FreeEntity_Strict(pvt)
			;debuglog EntityX(it\collider)+"|"+EntityY(it\collider)+"|"+EntityZ(it\collider)
		EndIf
	Next
	
	Local rt.RoomTemplates
	For rt.RoomTemplates = Each RoomTemplates
		rt\obj = FreeEntity_Strict(rt\obj)
	Next	
	
	Local tw.TempWayPoints
	For tw.TempWayPoints = Each TempWayPoints
		Delete tw
	Next
	
	Local tfll.TempFluLight
	For tfll = Each TempFluLight
		Delete tfll\position
		Delete tfll\rotation
		Delete tfll
	Next
	
	;TurnEntity(Collider, 0, Rand(160, 200), 0)
	
	ResetEntity Collider
	
	;If SelectedMap = "" Then InitEvents()
	
	For e.Events = Each Events
		If e\EventName = "room2_nuke"
			e\EventState[0] = 1
			;debuglog "room2_nuke"
		EndIf
		If e\EventName = "cont_106"
			e\EventState[1] = 1
			;debuglog "cont_106"
		EndIf	
		If e\EventName = "surveil_room"
			e\EventState[2] = 1
			;debuglog "surveil_room"
		EndIf
	Next
	
	MoveMouse Viewport_Center_X,Viewport_Center_Y;320, 240
	
	SetFont fo\Font[Font_Default]
	
	HidePointer()
	
	BlinkTimer = -10
	BlurTimer = 100
	Stamina = 100
	
	For i% = 0 To 70
		FPSfactor = 1.0
		FlushKeys()
		MovePlayer()
		UpdateDoors()
		UpdateNPCs()
		UpdateWorld()
		;Cls
		If (Int(Float(i)*0.27)<>Int(Float(i-1)*0.27)) Then
			DrawLoading(80+Int(Float(i)*0.27),False,"","Updating_world")
		EndIf
	Next
	
	DeleteTextureEntriesFromCache(0)
	DrawLoading(100,False,"","Done")
	
	FlushKeys
	FlushMouse
	
	DropSpeed = 0
	
	PrevTime = MilliSecs()
	CatchErrors("InitNewGame")
End Function

Type Task
	Field txt$
	Field trigger%
	Field triggerparam$
	Field triggerparam2$
	Field triggerparam3$
	Field triggerparam4$
	Field triggerparam5$
	Field done%
	Field chaintask.Task
End Type

Function CreateTask.Task(txt$,trigger%,triggerparam$,triggerparam2$="",triggerparam3$="",triggerparam4$="",triggerparam5$="")
	Local ta.Task = New Task
	
	ta\txt$ = txt$
	ta\trigger% = trigger%
	ta\triggerparam$ = triggerparam$
	ta\triggerparam2$ = triggerparam2$
	ta\triggerparam3$ = triggerparam3$
	ta\triggerparam4$ = triggerparam4$
	ta\triggerparam5$ = triggerparam5$
	
	Return ta
End Function

Function Tasks()
	Local ta.Task,ta2.Task,temp%,i%
	Local x%,y%,width%,height%
	Local scale# = opt\GraphicHeight/768.0
	Local it.Items,d.Doors,pvt,r.Rooms
	
	width% = 264*scale
	height% = 24*scale
	For ta = Each Task
		Local noChainTask% = True
		For ta2 = Each Task
			If ta <> ta2
				If ta = ta2\chaintask
					noChainTask = False
					Exit
				EndIf
			EndIf
		Next
		If noChainTask Then height% = height% + 22*scale
	Next
	x% = opt\GraphicWidth-width
	y% = (opt\GraphicHeight/2)-(height/2)
	SetFont fo\Font[Font_Digital_Small]
	Color 255,255,255
	DrawFrame(x,y,width,height)
	Text x+4*scale,y+2*scale,"Tasks:"
	For ta = Each Task
		ta\done = False
		Select ta\trigger
			Case 0 ;Picking up a specific item
				;[Block]
				temp = False
				For i = 0 To MaxItemAmount-1
					If Inventory[i]<>Null
						If Inventory[i]\itemtemplate\name = ta\triggerparam
							If Inventory[i]\itemtemplate\tempname = ta\triggerparam2
								temp = True
								Exit
							EndIf
						EndIf
					EndIf
				Next
				If temp
					ta\done = True
				EndIf
				Local FailTask% = True
				For it = Each Items
					If it\itemtemplate\name = ta\triggerparam
						If it\itemtemplate\tempname = ta\triggerparam2
							FailTask% = False
							Exit
						EndIf
					EndIf
				Next
				If FailTask
					ta\done = 2
				EndIf
				;[End Block]
			Case 1 ;Going to a room with a certain name
				;[Block]
				If PlayerRoom\RoomTemplate\Name = ta\triggerparam
					ta\done = True
				EndIf
				;[End Block]
			Case 2 ;Opening / Closing a certain door in a room
				;[Block]
				temp = False
				For d = Each Doors
					If d\room\RoomTemplate\Name = ta\triggerparam5$
						pvt = CreatePivot(d\room\obj)
						PositionEntity pvt,Float(ta\triggerparam),Float(ta\triggerparam2),Float(ta\triggerparam3),False
						If EntityX(pvt,True)=EntityX(d\frameobj)
							If EntityY(pvt,True)=EntityY(d\frameobj)
								If EntityZ(pvt,True)=EntityZ(d\frameobj)
									If Int(ta\triggerparam4)<>False
										If d\open Then temp = True
									Else
										If (Not d\open) Then temp = True
									EndIf
									pvt = FreeEntity_Strict(pvt)
									Exit
								EndIf
							EndIf
						EndIf
						pvt = FreeEntity_Strict(pvt)
					EndIf
				Next
				If temp
					ta\done = True
				EndIf
				;[End Block]
			Case 3 ;Being in a specific square area
				;[Block]
				temp = False
				For r = Each Rooms
					If r\RoomTemplate\Name = ta\triggerparam5$
						pvt = CreatePivot(r\obj)
						PositionEntity pvt,Float(ta\triggerparam),Float(ta\triggerparam2),Float(ta\triggerparam3),False
						If Abs(EntityX(Collider)-EntityX(pvt,True))<Float(ta\triggerparam4)*RoomScale
							If Abs(EntityY(Collider)-EntityY(pvt,True))<Float(ta\triggerparam4)*RoomScale
								If Abs(EntityZ(Collider)-EntityZ(pvt,True))<Float(ta\triggerparam4)*RoomScale
									temp = True
									pvt = FreeEntity_Strict(pvt)
									Exit
								EndIf
							EndIf
						EndIf
						pvt = FreeEntity_Strict(pvt)
					EndIf
				Next
				If temp
					ta\done = True
				EndIf
				;[End Block]
		End Select
		
		noChainTask% = True
		For ta2 = Each Task
			If ta <> ta2
				If ta = ta2\chaintask
					noChainTask = False
					Exit
				EndIf
			EndIf
		Next
		If noChainTask
			If ta\chaintask<>Null
				If ta\chaintask\done=0
					ta\done = 0
				ElseIf ta\chaintask\done=2
					ta\done = 2
				EndIf
			EndIf
			y% = y% + 22*scale
			If ta\done=True
				Color 0,255,0
			ElseIf ta\done = 2
				Color 255,0,0
			Else
				Color 255,255,255
			EndIf
			Text x+4*scale,y+2*scale,ta\txt
		EndIf
	Next
End Function

Type MissionEvent
	Field trigger%
	Field triggerparam$
	Field triggerparam2$
	Field triggerparam3$
	Field triggerparam4$
	Field triggerparam5$
End Type

Function CreateMissionEvent.MissionEvent(trigger%,triggerparam$,triggerparam2$="",triggerparam3$="",triggerparam4$="",triggerparam5$="")
	Local me.MissionEvent = New MissionEvent
	
	me\trigger% = trigger%
	me\triggerparam$ = triggerparam$
	me\triggerparam2$ = triggerparam2$
	me\triggerparam3$ = triggerparam3$
	me\triggerparam4$ = triggerparam4$
	me\triggerparam5$ = triggerparam5$
	
	Return me
End Function

Function UpdateMissionEvents()
	Local me.MissionEvent, d.Doors
	Local pvt
	
	Local currMission.Mission, mi.Mission
	For mi = Each Mission
		If mi\ID = mi_I\currID
			currMission = mi
			Exit
		EndIf
	Next
	
	If currMission <> Null Then
		If currMission\musicname <> "" Then
			ShouldPlay = MUS_INTRODUCTION
			;Music[65] = currMission\musicname
		EndIf
	EndIf
	
	For me = Each MissionEvent
		Select me\trigger
			Case 0 ;Locking / Unlocking a door (triggerparam4), then delete the event
				For d = Each Doors
					If d\room\RoomTemplate\Name = me\triggerparam5$
						pvt = CreatePivot(d\room\obj)
						PositionEntity pvt,Float(me\triggerparam),Float(me\triggerparam2),Float(me\triggerparam3),False
						If EntityX(pvt,True)=EntityX(d\frameobj)
							If EntityY(pvt,True)=EntityY(d\frameobj)
								If EntityZ(pvt,True)=EntityZ(d\frameobj)
									;debuglog EntityZ(pvt,True)+"|"+EntityZ(d\frameobj)
									If Int(me\triggerparam4)<>False
										d\locked = True
									Else
										d\locked = False
									EndIf
									pvt = FreeEntity_Strict(pvt)
									Exit
								EndIf
							EndIf
						EndIf
						pvt = FreeEntity_Strict(pvt)
					EndIf
				Next
				DeleteMissionEvent(me)
			Case 1 ;Playing a sound once, then delete the event
				If Int(me\triggerparam3)<=1
					PlaySound_Strict(LoadTempSound(me\triggerparam+"."+me\triggerparam4))
				Else
					PlaySound_Strict(LoadTempSound(me\triggerparam+Rand(Int(me\triggerparam2),Int(me\triggerparam3))+"."+me\triggerparam4))
				EndIf
				DeleteMissionEvent(me)
		End Select
	Next
	
End Function

Function DeleteMissionEvent(me.MissionEvent)
	
	Delete me
	
End Function

Function DeleteMission()
	
	Delete Each Task
	mi_I\IsEnding = False
	mi_I\EndingTimer = 0.0
	mi_I\currID = 0
	;If mi_I\EndSFX<>0
	;	StopStream_Strict(mi_I\EndSFX)
	;	mi_I\EndSFX = 0
	;EndIf
	
End Function

Function UpdateMissionEnding()
	Local ta.Task,allTasksDone%=True
	
	If KillTimer < 0
		If mi_I\EndingTimer = 0
			Local txt$ = "MISSION FAILED"
			CreateSplashText(txt$,(opt\GraphicWidth/2)-(StringWidth(txt$)/2),(opt\GraphicHeight/2)-(StringHeight(txt$)/2),40)
			mi_I\EndingTimer = FPSfactor
			EntityAlpha mi_I\FadeEntity,Min((mi_I\EndingTimer*0.01),1.0)
			If mi_I\EndSFX=0
				mi_I\EndSFX = StreamSound_Strict("SFX\Music\MissionModeFailed_Test.ogg",opt\MusicVol)
				mi_I\EndSFX_Vol = 1.0
			EndIf
		ElseIf mi_I\EndingTimer > 0 And mi_I\EndingTimer < 70*8
			mi_I\EndingTimer = mi_I\EndingTimer + FPSfactor
			EntityAlpha mi_I\FadeEntity,Min((mi_I\EndingTimer*0.01),1.0)
		Else
			mi_I\IsEnding = True
		EndIf
	Else
		For ta = Each Task
			If ta\done = False
				allTasksDone = False
				Exit
			ElseIf ta\done = 2
				allTasksDone = 2
				Exit
			EndIf
		Next
		If allTasksDone
			If mi_I\EndingTimer = 0
				UnableToMove = True
				NoTarget = True
				GodMode = True
				If allTasksDone=1
					Select mi_I\currID
						Case 1
							CreateMissionEvent(1,"SFX\General\Elevator\Moving",1,1,"ogg")
					End Select
					txt$ = "MISSION ACCOMPLISHED"
					If mi_I\EndSFX=0
						;mi_I\EndSFX = StreamSound_Strict("Unused\SFX\Music\MissionModeSuccess.ogg",MusicVolume)
						;mi_I\EndSFX_Vol = 1.0
					EndIf
				Else
					txt$ = "MISSION FAILED"
					If mi_I\EndSFX=0
						mi_I\EndSFX = StreamSound_Strict("SFX\Music\MissionModeFailed_Test.ogg",opt\MusicVol)
						mi_I\EndSFX_Vol = 1.0
					EndIf
				EndIf
				CreateSplashText(txt$,(opt\GraphicWidth/2)-(StringWidth(txt$)/2),(opt\GraphicHeight/2)-(StringHeight(txt$)/2),40)
				mi_I\EndingTimer = FPSfactor
				EntityAlpha mi_I\FadeEntity,Min((mi_I\EndingTimer*0.01),1.0)
			ElseIf mi_I\EndingTimer > 0 And mi_I\EndingTimer < 70*8
				mi_I\EndingTimer = mi_I\EndingTimer + FPSfactor
				EntityAlpha mi_I\FadeEntity,Min((mi_I\EndingTimer*0.01),1.0)
				KillTimer = 0
			Else
				If allTasksDone=1
					mi_I\IsEnding = True
				Else
					mi_I\IsEnding = 2
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

Function DrawMissionEnding()
	Local width,height,x,y
	
	If gopt\GameMode <> GAMEMODE_UNKNOWN Then Return
	
	If mi_I\IsEnding Then
		UpdateMenuControllerSelection(1+(1*(KillTimer<0 Lor mi_I\IsEnding=2)),0,0)
		
		Color 0,0,0
		Rect 0,0,opt\GraphicWidth,opt\GraphicHeight
		width = ImageWidth(PauseMenuIMG[0])
		height = ImageHeight(PauseMenuIMG[0])
		x = opt\GraphicWidth / 2 - width / 2
		y = opt\GraphicHeight / 2 - height / 2
		
		DrawImage PauseMenuIMG[0], x, y
		
		Color(255, 255, 255)
		SetFont fo\Font[Font_Menu]
		If KillTimer >= 0 And mi_I\IsEnding=1 Then
			Text(x + width / 2 + 40*MenuScale, y + 20*MenuScale, "SUCCESS!", True)
		Else
			Text(x + width / 2 + 40*MenuScale, y + 20*MenuScale, "FAILURE!", True)
		EndIf
		SetFont fo\Font[Font_Default]
		x = x+132*MenuScale
		y = y+122*MenuScale
		
		;Other stuff...
		
		x = opt\GraphicWidth / 2 - width / 2
		y = opt\GraphicHeight / 2 - height / 2
		x = x+width/2
		y = y+height-100*MenuScale
		
		If KillTimer < 0 Lor mi_I\IsEnding=2
			If DrawButton(x-145*MenuScale,y-200*MenuScale,390*MenuScale,60*MenuScale,"RESTART", True,False,True,0,0,0) Then
				ResetControllerSelections()
				MainMenuOpen = False
				Local id% = mi_I\currID
				NullGame(True)
				MenuOpen = False
				CurrSave = Null
				InitMission(id)
				LoadEntities()
				gopt\GameMode = GAMEMODE_UNKNOWN
				InitMissionGameMode(id)
				FlushKeys()
				FlushMouse()
			EndIf
		EndIf
		
		If DrawButton(x-145*MenuScale,y-100*MenuScale,390*MenuScale,60*MenuScale,"MAIN MENU", True,False,True,1*(KillTimer<0 Lor mi_I\IsEnding=2),0,0) Then
			ResetControllerSelections()
			MainMenuOpen = True
			NullGame()
			MenuOpen = False
			MainMenuTab = 0
			CurrSave = Null
			FlushKeys()
		EndIf
		
	EndIf
	
	DrawAllMenuButtons()
	
End Function






;~IDEal Editor Parameters:
;~C#Blitz3D