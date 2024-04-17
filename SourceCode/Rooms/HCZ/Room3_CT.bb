
Function FillRoom_Room3_CT(r.Rooms)
	Local d.Doors,lt.LightTemplates,tw.TempWayPoints,fb.FuseBox
	Local newlt,tex,it.Items
	Local i,wa.Water,ne.NewElevator
	
	Local ElevatorOBJ% = LoadRMesh("GFX\map\Elevators\elevator.rmesh",Null)
	HideEntity ElevatorOBJ
	r\Objects[3] = CopyEntity(ElevatorOBJ,r\obj)
	r\Objects[4] = CopyEntity(ElevatorOBJ,r\obj)
	r\Objects[5] = CopyEntity(ElevatorOBJ,r\obj)
	
	PositionEntity(r\Objects[3],-320,0,0)
	EntityType r\Objects[3],HIT_MAP
	EntityPickMode r\Objects[3],2
	
	RotateEntity r\Objects[4],0.0,180.0,0.0	
	PositionEntity(r\Objects[4],320,0,0)
	EntityType r\Objects[4],HIT_MAP
	EntityPickMode r\Objects[4],2
	
	RotateEntity r\Objects[5],0.0,-90.0,0.0
	PositionEntity(r\Objects[5],-4368,-4095,484)
	EntityType r\Objects[5],HIT_MAP
	EntityPickMode r\Objects[5],2
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x + 638.0 * RoomScale, r\y, r\z, 270, r, True, 5, False, "", 2)
	r\RoomDoors[1] = CreateDoor(r\zone,r\x - 638.0 * RoomScale, r\y, r\z, 90, r, True, 5, False, "", 1)
	r\RoomDoors[2] = CreateDoor(r\zone,r\x - 4368.0 * RoomScale, r\y - 4096.0 * RoomScale, r\z +793.0 * RoomScale, 0, r, True, 5, False, "", 3)
	r\RoomDoors[0]\DisableWaypoint = True
	r\RoomDoors[1]\DisableWaypoint = True
	r\RoomDoors[2]\DisableWaypoint = True
	
	CreateNewElevator(r\Objects[3],3,r\RoomDoors[1],1,r,-7615.0,-4095.0,0.0)
	CreateNewElevator(r\Objects[4],3,r\RoomDoors[0],2,r,-7615.0,-4095.0,0.0)
	ne = CreateNewElevator(r\Objects[5],2,r\RoomDoors[2],3,r,-7615.0,-4095.0,-3500.0)
	ne\floorlocked[2] = True
	
	r\RoomDoors[3] = CreateDoor(r\zone,r\x - 640.0 * RoomScale, r\y, r\z+512*RoomScale, 270, r, False, DOOR_DEFAULT)
	
	r\RoomDoors[4] = CreateDoor(r\zone,r\x - 640.0 * RoomScale, r\y, r\z-512*RoomScale, 270, r, False, DOOR_DEFAULT)
	r\RoomDoors[4]\locked = True
	
	r\RoomDoors[5] = CreateDoor(r\zone,r\x + 640.0 * RoomScale, r\y, r\z-512*RoomScale, 270, r, False, DOOR_DEFAULT)
	
	r\RoomDoors[6] = CreateDoor(r\zone,r\x + 640.0 * RoomScale, r\y, r\z+512*RoomScale, 270, r, False, DOOR_DEFAULT)
	
	r\Objects[2] = LoadMesh_Strict("GFX\map\rooms\room3_ct\room3_ct_shaft.b3d",r\obj)
	Local rt.RoomTemplates = New RoomTemplates
	rt\objPath = "GFX\map\rooms\room3_ct\room3_ct_down.rmesh"
	rt\obj = LoadRMesh(rt\objPath,rt)
	r\Objects[0] = rt\obj
	ScaleEntity(r\Objects[0], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[0], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[0], 2), 2)
	
	PositionEntity(r\Objects[0],r\x,r\y,r\z)
	EntityParent r\Objects[0],r\obj
	
	Delete rt
	rt.RoomTemplates = New RoomTemplates
	rt\objPath = "GFX\map\rooms\room3_ct\room3_ct_down_2.rmesh"
	rt\obj = LoadRMesh(rt\objPath,rt)
	r\Objects[1] = rt\obj
	ScaleEntity(r\Objects[1], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[1], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[1], 2), 2)
	
	PositionEntity(r\Objects[1],r\x,r\y,r\z)
	EntityParent r\Objects[1],r\obj
	
	For lt.LightTemplates = Each LightTemplates
		If lt\roomtemplate = rt Then
			newlt = AddLight(r, r\x+lt\x, r\y+lt\y, r\z+lt\z, lt\ltype, lt\range, lt\r, lt\g, lt\b, r\Objects[1], 1+8)
			If newlt <> 0 Then
				If lt\ltype = 3 Then
					LightConeAngles(newlt, lt\innerconeangle, lt\outerconeangle)
					RotateEntity(newlt, lt\pitch, lt\yaw, 0)
				EndIf
			EndIf
		EndIf
	Next
	Delete rt
	HideEntity(r\Objects[0])
	HideEntity(r\Objects[1])
	
	it = CreateItem(GetLocalString("Item Names","fuse"), "fuse", r\x + 733.0 * RoomScale, r\y - 3970.0 * RoomScale, r\z +1968.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	EntityType it\collider, HIT_ITEM
	
	it = CreateItem(GetLocalString("Item Names","fuse"), "fuse", r\x -4376.0 * RoomScale, r\y - 4066.0 * RoomScale, r\z +1112.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	EntityType it\collider, HIT_ITEM
	
	it = CreateItem(GetLocalString("Item Names","fuse"), "fuse", r\x -1164.0 * RoomScale, r\y - 2614.0 * RoomScale, r\z +2990.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	EntityType it\collider, HIT_ITEM
	
	it = CreateItem(GetLocalString("Item Names","fuse"), "fuse", r\x -496.0 * RoomScale, r\y +140.0 * RoomScale, r\z +700.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	EntityType it\collider, HIT_ITEM
	
	it = CreateItem(GetLocalString("Item Names","fuse"), "fuse", r\x +1144.0 * RoomScale, r\y-4029.0 * RoomScale, r\z +13.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	EntityType it\collider, HIT_ITEM
	
	it = CreateItem(GetLocalString("Item Names","fuse"), "fuse", r\x +5804.0 * RoomScale, r\y-3959.0 * RoomScale, r\z +5852.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	EntityType it\collider, HIT_ITEM
	
	; ~ Alt fuses (If player won't find all of them)
	
	it = CreateItem(GetLocalString("Item Names","fuse"), "fuse", r\x -1416.0 * RoomScale, r\y -7460.0 * RoomScale, r\z +4574.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	EntityType it\collider, HIT_ITEM
	
	it = CreateItem(GetLocalString("Item Names","fuse"), "fuse", r\x +4708.0 * RoomScale, r\y-3915.0 * RoomScale, r\z +3737.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	EntityType it\collider, HIT_ITEM
	
	it = CreateItem(GetLocalString("Item Names","fuse"), "fuse", r\x +993.0 * RoomScale, r\y-7456.0 * RoomScale, r\z +10.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	EntityType it\collider, HIT_ITEM
	
	it = CreateItem(GetLocalString("Item Names","fuse"), "fuse", r\x -1367.0 * RoomScale, r\y-7456.0 * RoomScale, r\z -10.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	EntityType it\collider, HIT_ITEM
	
	Local g.Guns
	
	it = CreateItem("H&K USP", "usp", r\x -5239.0 * RoomScale, r\y-4055.0 * RoomScale, r\z -29.0 * RoomScale)
	it\state = 12 : it\state2 = 36
	EntityParent(it\collider, r\obj)
	EntityType it\collider, HIT_ITEM
;	For g = Each Guns
;		If g\name = "usp" Then
;			g\HasPickedAttachments[ATT_MATCH] = True
;			g\HasToggledAttachments[ATT_MATCH] = True
;			g\HasAttachments[ATT_MATCH] = True
;			AddAttachment(g,ATT_MATCH)
;		EndIf
;	Next
	
	wa.Water = CreateWater("GFX\map\rooms\room3_ct\room3_ct_water.b3d","sewers_water2",0,0,0,r\obj,(-7936.0*RoomScale))
	EntityAlpha wa\obj,0.8
	EntityColor wa\obj,100,100,100
	EntityPickMode wa\obj,2
	r\Objects[6] = LoadTexture_Strict("GFX\map\textures\SLH_water5.png",1,1)
	EntityTexture wa\obj,r\Objects[6]
	ScaleTexture r\Objects[6],0.2,0.2
	TextureBlend r\Objects[6],2
	
	wa.Water = CreateWater("GFX\map\rooms\room3_ct\room3_ct_water_2.b3d","sewers_water3",0,0,0,r\obj,(-7936.0*RoomScale))
	EntityAlpha wa\obj,0.8
	EntityColor wa\obj,100,100,100
	EntityPickMode wa\obj,2
	r\Objects[7] = LoadTexture_Strict("GFX\map\textures\SLH_water5.png",1,1)
	EntityTexture wa\obj,r\Objects[7]
	ScaleTexture r\Objects[7],0.2,0.2
	TextureBlend r\Objects[7],2
	
	fb = CreateFuseBox("Fusebox.b3d", CreateVector3D(r\x +1207.0 * RoomScale, r\y -3910.0 * RoomScale, r\z +2377.0 * RoomScale), CreateVector3D(0, 90, 0), CreateVector3D(0.4 * RoomScale, 0.4 * RoomScale, 0.4 * RoomScale))
	EntityParent fb\obj, r\obj
	fb\fuses = 2
	r\Objects[8] = fb\obj
	
	fb.FuseBox = CreateFuseBox("Fusebox.b3d", CreateVector3D(r\x - 3999.0 * RoomScale, r\y - 7400.0 * RoomScale, r\z + 3427.0 * RoomScale), CreateVector3D(0, 270, 0), CreateVector3D(0.4 * RoomScale, 0.4 * RoomScale, 0.4 * RoomScale))
	EntityParent fb\obj, r\obj
	fb\fuses = 1
	r\Objects[9] = fb\obj
	
	fb.FuseBox = CreateFuseBox("Fusebox.b3d", CreateVector3D(r\x + 4195.0 * RoomScale, r\y - 3950.0 * RoomScale, r\z -1229.0 * RoomScale), CreateVector3D(0, 270, 0), CreateVector3D(0.4 * RoomScale, 0.4 * RoomScale, 0.4 * RoomScale))
	EntityParent fb\obj, r\obj
	fb\fuses = 2
	r\Objects[10] = fb\obj
	
	fb.FuseBox = CreateFuseBox("Fusebox.b3d", CreateVector3D(r\x - 4670.0 * RoomScale, r\y - 2550.0 * RoomScale, r\z + 1001.0 * RoomScale), CreateVector3D(0, 270, 0), CreateVector3D(0.4 * RoomScale, 0.4 * RoomScale, 0.4 * RoomScale))
	EntityParent fb\obj, r\obj
	fb\fuses = 1
	r\Objects[11] = fb\obj
	
	r\Objects[13] = CreateButton(r\x+5756*RoomScale,r\y-7413*RoomScale,r\z+8394*RoomScale,0,0,0)
	EntityParent(r\Objects[13],r\obj)
	
	r\Objects[14] = CreatePivot()
	PositionEntity r\Objects[14],r\x+5818*RoomScale,r\y-5574*RoomScale,r\z+6436*RoomScale
	EntityParent(r\Objects[14],r\obj)
	
End Function

Function UpdateEvent_Room3_CT(e.Events)
	Local r.Rooms,ne.NewElevator,fb.FuseBox
	Local i,n.NPCs,it.Items
	
	Local lt.LightTemplates, newlt
	
	If gopt\GameMode = GAMEMODE_DEFAULT Then
	
		If PlayerRoom = e\room
			
			If EntityDistanceSquared(Collider,e\room\Objects[14]) < PowTwo(3.0) And KillTimer => 0 Then
				m_msg\DeathTxt=""
				PlaySound_Strict LoadTempSound("SFX\Room\PocketDimension\Impact.ogg")
				KillTimer=-1.0
			EndIf
			
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
			
			If TaskExists(TASK_FIND_ROOM3_CT) And (Not TaskExists(TASK_FIND_MEDKIT)) Then
				EndTask(TASK_FIND_ROOM3_CT)
				BeginTask(TASK_FIND_ROOM3_CT_FUSEBOXES)
			EndIf
			
			If TaskExists(TASK_SEARCH_FOR_ROOM3_CT) Then
				EndTask(TASK_SEARCH_FOR_ROOM3_CT)
				BeginTask(TASK_FIND_ROOM3_CT_FUSEBOXES)
				If TaskExists(TASK_SEARCH_FOR_ROOM2_MT) Then
					CancelTask(TASK_SEARCH_FOR_ROOM2_MT)
				EndIf
				If TaskExists(TASK_SEARCH_FOR_CONT_079) Then
					CancelTask(TASK_SEARCH_FOR_CONT_079)
				EndIf
			EndIf
			
			If PlayerInNewElevator And ecst\FusesAmount = 4 Then
				If TaskExists(TASK_ESCAPE_106) And (Not TaskExists(TASK_COME_BACK_TO_GUARD)) Then
					EndTask(TASK_ESCAPE_106)
					BeginTask(TASK_COME_BACK_TO_GUARD)
					Curr106\Idle = True
					Curr106\State[0] = 70 * 60 * 10
					Curr106\Contained = True
					SaveGame(SavePath + CurrSave\Name + "\", True)
				EndIf
			EndIf
			
			If e\EventState[0] = 0 And ecst\FusesAmount = 4 Then
				UpdateButton(e\room\Objects[13])
			EndIf
			
			If ecst\FusesAmount = 4 Then
				If d_I\ClosestButton = e\room\Objects[13] And KeyHitUse Then
					If TaskExists(TASK_TURN_ON_ROOM3_CT_GENERATOR) Then
						EndTask(TASK_TURN_ON_ROOM3_CT_GENERATOR)
						BeginTask(TASK_ESCAPE_106)
						e\EventState[0] = 1
					EndIf
					If e\room\NPC[0] = Null And Curr106\Idle = False Then
						CreateNPC(NPC_SCP_106,EntityX(e\room\Objects[13]),EntityY(e\room\Objects[13]),EntityZ(e\room\Objects[13]))
					EndIf
					PlaySound_Strict(LoadTempSound("SFX\General\GeneratorOn.ogg"))
					PlaySound_Strict(LoadTempSound("SFX\Room\Blackout.ogg"))
					SaveGame(SavePath + CurrSave\Name + "\", True)
				EndIf
				If TaskExists(TASK_FIND_ROOM3_CT_FUSEBOXES) Then
					EndTask(TASK_FIND_ROOM3_CT_FUSEBOXES)
					BeginTask(TASK_TURN_ON_ROOM3_CT_GENERATOR)
				EndIf
			EndIf
			If ((Int(e\EventState[4])) Mod 2) = 0 Then
				For fb = Each FuseBox
					If fb\obj = e\room\Objects[9] Then
						If fb\fuses = 3 Then
							If TaskExists(TASK_FIND_ROOM3_CT_FUSEBOXES) Then
								UpdateTask(TASK_FIND_ROOM3_CT_FUSEBOXES)
								ecst\FusesAmount = ecst\FusesAmount + 1
							EndIf
							e\EventState[4] = e\EventState[4] + 1
						EndIf
					EndIf
				Next
			EndIf
			If ((Int(e\EventState[4]) Shr 1) Mod 2) = 0 Then
				For fb = Each FuseBox
					If fb\obj = e\room\Objects[10] Then
						If fb\fuses = 3 Then
							If TaskExists(TASK_FIND_ROOM3_CT_FUSEBOXES) Then
								UpdateTask(TASK_FIND_ROOM3_CT_FUSEBOXES)
								ecst\FusesAmount = ecst\FusesAmount + 1
							EndIf
							e\EventState[4] = e\EventState[4] + 2
						EndIf
					EndIf
				Next
			EndIf
			If ((Int(e\EventState[4]) Shr 2) Mod 2) = 0 Then
				For fb = Each FuseBox
					If fb\obj = e\room\Objects[11] Then
						If fb\fuses = 3 Then
							If TaskExists(TASK_FIND_ROOM3_CT_FUSEBOXES) Then
								UpdateTask(TASK_FIND_ROOM3_CT_FUSEBOXES)
								ecst\FusesAmount = ecst\FusesAmount + 1
							EndIf
							e\EventState[4] = e\EventState[4] + 4
						EndIf
					EndIf
				Next
			EndIf
			
			e\EventState[3] = e\EventState[3] + FPSfactor
			
			If EntityY(Collider)<-6000.0*RoomScale ; ~ Sewers
				
				PlayerFallingPickDistance = 10.0
				
				ShowEntity e\room\Objects[1]
				HideEntity e\room\Objects[0]
				For r.Rooms = Each Rooms
					HideEntity r\obj
				Next
				ShowEntity PlayerRoom\obj
				EntityAlpha(GetChild(PlayerRoom\obj,2),0.0)
				EntityAlpha(GetChild(e\room\Objects[1],2),1.0)
				If PlayerInNewElevator
					For ne = Each NewElevator
						If PlayerNewElevator = ne\ID
							If ne\door\openstate = 0
								EntityAlpha(GetChild(e\room\Objects[1],2),0.0)
								Exit
							EndIf
						EndIf
					Next
				EndIf
				If (Not PlayerInNewElevator)
					ShouldPlay =MUS_SEWERS
					
					PositionTexture e\room\Objects[6],0,e\EventState[3]*0.005
					EntityAlpha MapCubeMap\CamOverlay,0.7
					CameraFogRange MapCubeMap\Cam,1,6
					CameraFogColor MapCubeMap\Cam,5,20,3
					ShouldUpdateWater = "sewers_water2"
					
				EndIf
				
				If (Not PlayerInNewElevator)
					PositionEntity e\room\RoomDoors[2]\frameobj,EntityX(e\room\RoomDoors[2]\frameobj),-7615.0*RoomScale,EntityZ(e\room\RoomDoors[2]\frameobj)
					PositionEntity e\room\RoomDoors[2]\obj,EntityX(e\room\RoomDoors[2]\obj),-7615.0*RoomScale,EntityZ(e\room\RoomDoors[2]\obj)
					PositionEntity e\room\RoomDoors[2]\obj2,EntityX(e\room\RoomDoors[2]\obj2),-7615.0*RoomScale,EntityZ(e\room\RoomDoors[2]\obj2)
					PositionEntity e\room\RoomDoors[2]\buttons[0],EntityX(e\room\RoomDoors[2]\buttons[0]),-7615.0*RoomScale+0.6,EntityZ(e\room\RoomDoors[2]\buttons[0])
					PositionEntity e\room\RoomDoors[2]\buttons[1],EntityX(e\room\RoomDoors[2]\buttons[1]),-7615.0*RoomScale+0.7,EntityZ(e\room\RoomDoors[2]\buttons[1])
					For ne = Each NewElevator
						If ne\door = e\room\RoomDoors[2]
							If ne\currfloor = 1 And ne\state = 0.0
								e\room\RoomDoors[2]\open = True
							Else
								e\room\RoomDoors[2]\open = False
							EndIf
						EndIf
					Next
				EndIf
				CameraFogRange Camera,1,6
				CameraFogColor Camera,5,20,3
				CameraClsColor Camera,5,20,3
				CameraRange Camera,0.01,7
				
			ElseIf EntityY(Collider)<-1000.0*RoomScale ; ~ Tunnels
				
				PlayerFallingPickDistance = 0.0
				
				ShowEntity e\room\Objects[0]
				HideEntity e\room\Objects[1]
				For r.Rooms = Each Rooms
					HideEntity r\obj
				Next
				ShowEntity PlayerRoom\obj
				EntityAlpha(GetChild(PlayerRoom\obj,2),0.0)
				EntityAlpha(GetChild(e\room\Objects[0],2),1.0)
				If PlayerInNewElevator
					For ne = Each NewElevator
						If PlayerNewElevator = ne\ID
							If ne\door\openstate = 0
								EntityAlpha(GetChild(e\room\Objects[0],2),0.0)
								Exit
							EndIf
						EndIf
					Next
				EndIf
				If (Not PlayerInNewElevator)
					ShouldPlay = MUS_CONSTRUCTION_TUNNELS
				EndIf
				e\EventState[3] = 0.0
				
				If e\EventState[2] = 0.0 Then
					For fb = Each FuseBox
						If fb\obj = e\room\Objects[8] Then
							If fb\fuses = 3 Then
								e\room\RoomDoors[2]\locked = False
								e\room\RoomDoors[2]\open = False
								UseDoor(e\room\RoomDoors[2], False)
								If TaskExists(TASK_FIND_ROOM3_CT_FUSEBOXES) Then
									ecst\FusesAmount = ecst\FusesAmount + 1
								EndIf
								e\EventState[2] = 1.0
							EndIf
							Exit
						EndIf
					Next
					e\room\RoomDoors[2]\open = False
					e\room\RoomDoors[2]\locked = True
				ElseIf e\EventState[2] = 1.0 Then
					e\room\RoomDoors[2]\locked = False
					e\room\RoomDoors[2]\open = True
					e\EventState[2] = 2.0
				EndIf
				
				If (Not PlayerInNewElevator)
					For i = 0 To 2
						PositionEntity e\room\RoomDoors[i]\frameobj,EntityX(e\room\RoomDoors[i]\frameobj),-4095.0*RoomScale,EntityZ(e\room\RoomDoors[i]\frameobj)
						PositionEntity e\room\RoomDoors[i]\obj,EntityX(e\room\RoomDoors[i]\obj),-4095.0*RoomScale,EntityZ(e\room\RoomDoors[i]\obj)
						PositionEntity e\room\RoomDoors[i]\obj2,EntityX(e\room\RoomDoors[i]\obj2),-4095.0*RoomScale,EntityZ(e\room\RoomDoors[i]\obj2)
						PositionEntity e\room\RoomDoors[i]\buttons[0],EntityX(e\room\RoomDoors[i]\buttons[0]),-4095.0*RoomScale+0.6,EntityZ(e\room\RoomDoors[i]\buttons[0])
						PositionEntity e\room\RoomDoors[i]\buttons[1],EntityX(e\room\RoomDoors[i]\buttons[1]),-4095.0*RoomScale+0.7,EntityZ(e\room\RoomDoors[i]\buttons[1])
						For ne = Each NewElevator
							If ne\door = e\room\RoomDoors[i]
								If ne\currfloor = 2 And ne\state = 0.0
									e\room\RoomDoors[i]\open = True
								Else
									e\room\RoomDoors[i]\open = False
								EndIf
							EndIf
						Next
					Next
				EndIf
			Else ; ~ Entrance
				
				PlayerFallingPickDistance = 10.0
				
				HideEntity e\room\Objects[1]
				HideEntity e\room\Objects[0]
				If PlayerInNewElevator
					For ne = Each NewElevator
						If PlayerNewElevator = ne\ID
							If ne\door\openstate = 0
								EntityAlpha(GetChild(PlayerRoom\obj,2),0.0)
								For i = 0 To 3
									If PlayerRoom\Adjacent[i]<>Null
										EntityAlpha(GetChild(PlayerRoom\Adjacent[i]\obj,2),0.0)
									EndIf
								Next
								Exit
							EndIf
						EndIf
					Next
				EndIf
				e\EventState[3] = 0.0
				If (Not PlayerInNewElevator)
					For i = 0 To 1
						PositionEntity e\room\RoomDoors[i]\frameobj,EntityX(e\room\RoomDoors[i]\frameobj),0.0,EntityZ(e\room\RoomDoors[i]\frameobj)
						PositionEntity e\room\RoomDoors[i]\obj,EntityX(e\room\RoomDoors[i]\obj),0.0,EntityZ(e\room\RoomDoors[i]\obj)
						PositionEntity e\room\RoomDoors[i]\obj2,EntityX(e\room\RoomDoors[i]\obj2),0.0,EntityZ(e\room\RoomDoors[i]\obj2)
						PositionEntity e\room\RoomDoors[i]\buttons[0],EntityX(e\room\RoomDoors[i]\buttons[0]),0.0+0.6,EntityZ(e\room\RoomDoors[i]\buttons[0])
						PositionEntity e\room\RoomDoors[i]\buttons[1],EntityX(e\room\RoomDoors[i]\buttons[1]),0.0+0.7,EntityZ(e\room\RoomDoors[i]\buttons[1])
						For ne = Each NewElevator
							If ne\door = e\room\RoomDoors[i]
								If ne\currfloor = 3 And ne\state = 0.0
									e\room\RoomDoors[i]\open = True
								Else
									e\room\RoomDoors[i]\open = False
								EndIf
							EndIf
						Next
					Next
				EndIf
			EndIf
		Else
			HideEntity e\room\Objects[0]
			HideEntity e\room\Objects[1]
		EndIf
		
	Else
		
		If PlayerRoom = e\room
			
			If EntityY(Collider)<-6000.0*RoomScale ; ~ Sewers
				
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
				
				PlayerFallingPickDistance = 10.0
				
				ShowEntity e\room\Objects[1]
				HideEntity e\room\Objects[0]
				For r.Rooms = Each Rooms
					HideEntity r\obj
				Next
				ShowEntity PlayerRoom\obj
				EntityAlpha(GetChild(PlayerRoom\obj,2),0.0)
				EntityAlpha(GetChild(e\room\Objects[1],2),1.0)
				If PlayerInNewElevator
					For ne = Each NewElevator
						If PlayerNewElevator = ne\ID
							If ne\door\openstate = 0
								EntityAlpha(GetChild(e\room\Objects[1],2),0.0)
								Exit
							EndIf
						EndIf
					Next
				EndIf
				If (Not PlayerInNewElevator)
					ShouldPlay =MUS_SEWERS
					
					e\EventState[3] = e\EventState[3] + FPSfactor
					
					PositionTexture e\room\Objects[6],0,e\EventState[3]*0.005
					EntityAlpha MapCubeMap\CamOverlay,0.7
					CameraFogRange MapCubeMap\Cam,1,6
					CameraFogColor MapCubeMap\Cam,5,20,3
					ShouldUpdateWater = "sewers_water2"
					
				EndIf
				
				If (Not PlayerInNewElevator)
					PositionEntity e\room\RoomDoors[2]\frameobj,EntityX(e\room\RoomDoors[2]\frameobj),-7615.0*RoomScale,EntityZ(e\room\RoomDoors[2]\frameobj)
					PositionEntity e\room\RoomDoors[2]\obj,EntityX(e\room\RoomDoors[2]\obj),-7615.0*RoomScale,EntityZ(e\room\RoomDoors[2]\obj)
					PositionEntity e\room\RoomDoors[2]\obj2,EntityX(e\room\RoomDoors[2]\obj2),-7615.0*RoomScale,EntityZ(e\room\RoomDoors[2]\obj2)
					PositionEntity e\room\RoomDoors[2]\buttons[0],EntityX(e\room\RoomDoors[2]\buttons[0]),-7615.0*RoomScale+0.6,EntityZ(e\room\RoomDoors[2]\buttons[0])
					PositionEntity e\room\RoomDoors[2]\buttons[1],EntityX(e\room\RoomDoors[2]\buttons[1]),-7615.0*RoomScale+0.7,EntityZ(e\room\RoomDoors[2]\buttons[1])
					For ne = Each NewElevator
						If ne\door = e\room\RoomDoors[2]
							If ne\currfloor = 1 And ne\state = 0.0
								e\room\RoomDoors[2]\open = True
							Else
								e\room\RoomDoors[2]\open = False
							EndIf
						EndIf
					Next
				EndIf
				CameraFogRange Camera,1,6
				CameraFogColor Camera,5,20,3
				CameraClsColor Camera,5,20,3
				CameraRange Camera,0.01,7
				
				PlayerFallingPickDistance = 10.0
				
			ElseIf EntityY(Collider)<-1000.0*RoomScale ; ~ Tunnels
				
				PlayerFallingPickDistance = 0.0
				
				ShowEntity e\room\Objects[0]
				HideEntity e\room\Objects[1]
				For r.Rooms = Each Rooms
					HideEntity r\obj
				Next
				ShowEntity PlayerRoom\obj
				EntityAlpha(GetChild(PlayerRoom\obj,2),0.0)
				EntityAlpha(GetChild(e\room\Objects[0],2),1.0)
				If PlayerInNewElevator
					For ne = Each NewElevator
						If PlayerNewElevator = ne\ID
							If ne\door\openstate = 0
								EntityAlpha(GetChild(e\room\Objects[0],2),0.0)
								Exit
							EndIf
						EndIf
					Next
				EndIf
				If (Not PlayerInNewElevator)
					ShouldPlay = MUS_CONSTRUCTION_TUNNELS
				EndIf
				
				If (Not PlayerInNewElevator)
					For i = 0 To 1
						PositionEntity e\room\RoomDoors[i]\frameobj,EntityX(e\room\RoomDoors[i]\frameobj),-4095.0*RoomScale,EntityZ(e\room\RoomDoors[i]\frameobj)
						PositionEntity e\room\RoomDoors[i]\obj,EntityX(e\room\RoomDoors[i]\obj),-4095.0*RoomScale,EntityZ(e\room\RoomDoors[i]\obj)
						PositionEntity e\room\RoomDoors[i]\obj2,EntityX(e\room\RoomDoors[i]\obj2),-4095.0*RoomScale,EntityZ(e\room\RoomDoors[i]\obj2)
						PositionEntity e\room\RoomDoors[i]\buttons[0],EntityX(e\room\RoomDoors[i]\buttons[0]),-4095.0*RoomScale+0.6,EntityZ(e\room\RoomDoors[i]\buttons[0])
						PositionEntity e\room\RoomDoors[i]\buttons[1],EntityX(e\room\RoomDoors[i]\buttons[1]),-4095.0*RoomScale+0.7,EntityZ(e\room\RoomDoors[i]\buttons[1])
						For ne = Each NewElevator
							If ne\door = e\room\RoomDoors[i]
								If ne\currfloor = 2 And ne\state = 0.0
									e\room\RoomDoors[i]\open = True
								Else
									e\room\RoomDoors[i]\open = False
								EndIf
							EndIf
						Next
					Next
				EndIf
			Else ; ~ Entrance
				
				PlayerFallingPickDistance = 10.0
				
				HideEntity e\room\Objects[1]
				HideEntity e\room\Objects[0]
				If PlayerInNewElevator
					For ne = Each NewElevator
						If PlayerNewElevator = ne\ID
							If ne\door\openstate = 0
								EntityAlpha(GetChild(PlayerRoom\obj,2),0.0)
								For i = 0 To 3
									If PlayerRoom\Adjacent[i]<>Null
										EntityAlpha(GetChild(PlayerRoom\Adjacent[i]\obj,2),0.0)
									EndIf
								Next
								Exit
							EndIf
						EndIf
					Next
				EndIf
				e\EventState[3] = 0.0
				If (Not PlayerInNewElevator)
					For i = 0 To 1
						PositionEntity e\room\RoomDoors[i]\frameobj,EntityX(e\room\RoomDoors[i]\frameobj),0.0,EntityZ(e\room\RoomDoors[i]\frameobj)
						PositionEntity e\room\RoomDoors[i]\obj,EntityX(e\room\RoomDoors[i]\obj),0.0,EntityZ(e\room\RoomDoors[i]\obj)
						PositionEntity e\room\RoomDoors[i]\obj2,EntityX(e\room\RoomDoors[i]\obj2),0.0,EntityZ(e\room\RoomDoors[i]\obj2)
						PositionEntity e\room\RoomDoors[i]\buttons[0],EntityX(e\room\RoomDoors[i]\buttons[0]),0.0+0.6,EntityZ(e\room\RoomDoors[i]\buttons[0])
						PositionEntity e\room\RoomDoors[i]\buttons[1],EntityX(e\room\RoomDoors[i]\buttons[1]),0.0+0.7,EntityZ(e\room\RoomDoors[i]\buttons[1])
						For ne = Each NewElevator
							If ne\door = e\room\RoomDoors[i]
								If ne\currfloor = 3 And ne\state = 0.0
									e\room\RoomDoors[i]\open = True
								Else
									e\room\RoomDoors[i]\open = False
								EndIf
							EndIf
						Next
					Next
				EndIf
			EndIf
		Else
			HideEntity e\room\Objects[0]
			HideEntity e\room\Objects[1]
		EndIf
		
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D