
Function FillRoom_Room2_Maintenance(r.Rooms)
	Local d.Doors, lt.LightTemplates, tw.TempWayPoints
	Local newlt, tex
	Local i, it.Items
	Local ne.NewElevator
	
	Local ElevatorOBJ% = LoadRMesh("GFX\map\Elevators\elevator.rmesh",Null)
	HideEntity ElevatorOBJ
	r\Objects[1] = CopyEntity(ElevatorOBJ,r\obj)
	r\Objects[2] = CopyEntity(ElevatorOBJ,r\obj)
	
	PositionEntity(r\Objects[1],576,0,640)
	EntityType r\Objects[1],HIT_MAP
	EntityPickMode r\Objects[1],2
	
	RotateEntity r\Objects[2],0.0,180.0,0.0
	PositionEntity(r\Objects[2],-576,0,-640)
	EntityType r\Objects[2],HIT_MAP
	EntityPickMode r\Objects[2],2
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x - 256.0 * RoomScale, r\y, r\z - 640.0 * RoomScale, 270, r, True, 5, False, "", 2)
	r\RoomDoors[1] = CreateDoor(r\zone,r\x + 256.0 * RoomScale, r\y, r\z + 640.0 * RoomScale, 90, r, True, 5, False, "", 1)
	r\RoomDoors[0]\DisableWaypoint = True
	r\RoomDoors[1]\DisableWaypoint = True
	
	ne = CreateNewElevator(r\Objects[1],3,r\RoomDoors[1],1,r,-5632.0,-2784.0,0.0)
	ne\floorlocked[1] = True
	ne = CreateNewElevator(r\Objects[2],3,r\RoomDoors[0],2,r,-5632.0,-2784.0,0.0)
	ne\floorlocked[1] = True
	
	d.Doors = CreateDoor(0, r\x,r\y,r\z,0, r, False, True, False, AccessCode[2])
	PositionEntity(d\buttons[0], r\x + 224.0 * RoomScale, r\y + 0.7, r\z - 360.0 * RoomScale, True)
	RotateEntity (d\buttons[0], 0,-90,0,True)
	PositionEntity(d\buttons[1], r\x - 224.0 * RoomScale, r\y + 0.7, r\z + 360.0 * RoomScale, True)		
	RotateEntity (d\buttons[1], 0,90,0,True)
	
	d.Doors = CreateDoor(r\zone, r\x+279*RoomScale,r\y,r\z-480*RoomScale,90, r, False, DOOR_OFFICE)
	
	it = CreateItem(GetLocalString("Item Names","severed_hand_yellow"), "hand3", r\x + 1920.0 * RoomScale, r\y-5596 * RoomScale, r\z+2301.0* RoomScale)
	EntityParent(it\collider, r\obj)
	
	r\Objects[8] = LoadMesh_Strict("GFX\map\rooms\room2_maintenance\room2mt2_shaftwall.b3d",r\obj)
	Local rt.RoomTemplates = New RoomTemplates
	rt\objPath = "GFX\map\rooms\room2_maintenance\room2mt2_down_opt.rmesh"
	rt\obj = LoadRMesh(rt\objPath,rt)
	r\Objects[0] = rt\obj
	ScaleEntity(r\Objects[0], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[0], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[0], 2), 2)
	
	PositionEntity(r\Objects[0],r\x,r\y,r\z)
	EntityParent r\Objects[0],r\obj
	
	For lt.LightTemplates = Each LightTemplates
		If lt\roomtemplate = rt Then
			newlt = AddLight(r, r\x+lt\x, r\y+lt\y, r\z+lt\z, lt\ltype, lt\range, lt\r, lt\g, lt\b, r\Objects[0])
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
			r\SoundEmitterObj[i]=CreatePivot(r\Objects[0])
			PositionEntity r\SoundEmitterObj[i], r\x+rt\TempSoundEmitterX[i],r\y+rt\TempSoundEmitterY[i],r\z+rt\TempSoundEmitterZ[i],True
			EntityParent(r\SoundEmitterObj[i],r\Objects[0])
			
			r\SoundEmitter[i] = r\RoomTemplate\TempSoundEmitter[i]
			r\SoundEmitterRange[i] = r\RoomTemplate\TempSoundEmitterRange[i]
		EndIf
	Next
	Delete rt
	rt.RoomTemplates = New RoomTemplates
	rt\objPath = "GFX\map\rooms\room2_maintenance\room2mt2_down2_opt.rmesh"
	rt\obj = LoadRMesh(rt\objPath,rt)
	r\Objects[5] = rt\obj
	ScaleEntity(r\Objects[5], RoomScale, RoomScale, RoomScale)
	EntityType(GetChild(r\Objects[5], 2), HIT_MAP)
	EntityPickMode(GetChild(r\Objects[5], 2), 2)
	
	PositionEntity(r\Objects[5],r\x,r\y,r\z)
	EntityParent r\Objects[5],r\obj
	
	For lt.LightTemplates = Each LightTemplates
		If lt\roomtemplate = rt Then
			newlt = AddLight(r, r\x+lt\x, r\y+lt\y, r\z+lt\z, lt\ltype, lt\range, lt\r, lt\g, lt\b, r\Objects[5], 1+8)
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
	Local emittersexist% = 0
	For i = 0 To MaxRoomEmitters-1
		If r\SoundEmitterObj[i]<>0
			emittersexist = emittersexist + 1
		Else
			Exit
		EndIf
	Next
	;debuglog emittersexist
	For i = emittersexist To MaxRoomEmitters-1
		If rt\TempSoundEmitter[i-emittersexist]<>0 Then
			r\SoundEmitterObj[i]=CreatePivot(r\Objects[5])
			PositionEntity r\SoundEmitterObj[i], r\x+rt\TempSoundEmitterX[i-emittersexist],r\y+rt\TempSoundEmitterY[i-emittersexist],r\z+rt\TempSoundEmitterZ[i-emittersexist],True
			EntityParent(r\SoundEmitterObj[i],r\Objects[5])
			
			r\SoundEmitter[i] = rt\TempSoundEmitter[i-emittersexist]
			r\SoundEmitterRange[i] = rt\TempSoundEmitterRange[i-emittersexist]
		EndIf
	Next
	Delete rt
	HideEntity(r\Objects[0])
	HideEntity(r\Objects[5])
	
	;Stuff for 538 area
	
	
	;Stuff for sewers
;	r\Objects[9] = CreateSprite(Camera)
;	EntityOrder r\Objects[9],-1
;	ScaleSprite r\Objects[9],2.0,2.0
;	PositionEntity r\Objects[9],0,0,1
;	EntityColor r\Objects[9],10,40,6
;	EntityAlpha r\Objects[9],0.6
;	EntityFX r\Objects[9],1
;	HideEntity r\Objects[9]
	
	tex = LoadTexture_Strict("GFX\map\rooms\room2_maintenance\sewer_vines1.png",1+2)
	;Moss Movement Pivot
	r\Objects[6] = CreatePivot()
	PositionEntity r\Objects[6],r\x+1952.96*RoomScale,r\y-5142.0*RoomScale,r\z-2530.96*RoomScale,True
	RotateEntity r\Objects[6],0,32,0,True
	EntityParent r\Objects[6],r\obj
	;1st Moss
	Local sprite = CreateSprite(r\Objects[6])
	HandleSprite sprite,0,1
	ScaleSprite sprite,0.3,0.4
	SpriteViewMode sprite,2
	EntityFX sprite,16
	EntityTexture sprite,tex
	PositionEntity sprite,0,0,2*RoomScale
	sprite = CreateSprite(r\Objects[6])
	HandleSprite sprite,0,1
	ScaleSprite sprite,0.3,0.4
	SpriteViewMode sprite,2
	EntityFX sprite,16
	EntityTexture sprite,tex
	PositionEntity sprite,0,0,-2*RoomScale
	;2nd Moss
	sprite = CreateSprite()
	HandleSprite sprite,0,1
	ScaleSprite sprite,0.4,0.3
	SpriteViewMode sprite,2
	EntityFX sprite,16
	EntityTexture sprite,tex
	PositionEntity sprite,r\x+1922.96*RoomScale,r\y-5142.0*RoomScale,r\z-2606.96*RoomScale,True
	EntityParent sprite,r\obj
	sprite = CreateSprite()
	HandleSprite sprite,0,1
	ScaleSprite sprite,0.4,0.3
	SpriteViewMode sprite,2
	EntityFX sprite,16
	EntityTexture sprite,tex
	PositionEntity sprite,r\x+1922.96*RoomScale,r\y-5142.0*RoomScale,r\z-2602.96*RoomScale,True
	EntityParent sprite,r\obj
	DeleteSingleTextureEntryFromCache tex
	
	Local wa.Water = CreateWater("GFX\map\rooms\room2_maintenance\water_ent.b3d","sewers_water1",0,0,0,r\obj,(-5664.0*RoomScale))
	EntityAlpha wa\obj,0.8
	EntityColor wa\obj,100,100,100
	;EntityType wa\obj,HIT_MAP
	EntityPickMode wa\obj,2
	r\Objects[3] = LoadTexture_Strict("GFX\map\textures\SLH_water2.jpg",1,1)
	EntityTexture wa\obj,r\Objects[3]
	ScaleTexture r\Objects[3],0.1,0.1
	TextureBlend r\Objects[3],2
	
	r\Objects[4] = LoadMesh_Strict("GFX\map\rooms\room2_maintenance\sewers_props.b3d",r\obj)
	EntityPickMode r\Objects[4],2
	EntityType r\Objects[4],HIT_MAP
	
	r\Objects[9] = LoadMesh_Strict("GFX\map\rooms\room2_maintenance\room2mt2_down2_props.b3d",r\obj)
	
End Function

Function UpdateEvent_Room2_Maintenance(e.Events)
	Local r.Rooms,ne.NewElevator
	Local i
	Local lt.LightTemplates, newlt
	
	If PlayerRoom = e\room
		
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
		
		If TaskExists(TASK_SEARCH_FOR_ROOM2_MT) Then
			EndTask(TASK_SEARCH_FOR_ROOM2_MT)
			BeginTask(TASK_SEARCH_FOR_GENERATOR)
		EndIf
		
		If EntityY(Collider)<-3500.0*RoomScale ; ~ Sewer Complex
			
			PlayerFallingPickDistance = 0.0
			
			ShowEntity e\room\Objects[5]
			HideEntity e\room\Objects[0]
			For r.Rooms = Each Rooms
				HideEntity r\obj
			Next
			ShowEntity PlayerRoom\obj
			EntityAlpha(GetChild(PlayerRoom\obj,2),0.0)
			EntityAlpha(GetChild(e\room\Objects[5],2),1.0)
			If PlayerInNewElevator
				For ne = Each NewElevator
					If PlayerNewElevator = ne\ID
						If ne\door\openstate = 0
							EntityAlpha(GetChild(e\room\Objects[5],2),0.0)
							Exit
						EndIf
					EndIf
				Next
			EndIf
			If (Not PlayerInNewElevator)
				ShouldPlay =MUS_SEWERS
			EndIf
			
			If EntityY(Collider)<=-9728.0*RoomScale And KillTimer=>0 Then
				m_msg\DeathTxt=""
				PlaySound_Strict LoadTempSound("SFX\Room\PocketDimension\Impact.ogg")
				KillTimer=-1.0
			EndIf
			
			e\EventState[1]=e\EventState[1]+FPSfactor
			RotateEntity(e\room\Objects[6], Sin(Float(e\EventState[1])*1.4), EntityYaw(e\room\Objects[6],True), EntityRoll(e\room\Objects[6],True), True)
			
			If (Not PlayerInNewElevator)
				For i = 0 To 1
					PositionEntity e\room\RoomDoors[i]\frameobj,EntityX(e\room\RoomDoors[i]\frameobj),-5632.0*RoomScale,EntityZ(e\room\RoomDoors[i]\frameobj)
					PositionEntity e\room\RoomDoors[i]\obj,EntityX(e\room\RoomDoors[i]\obj),-5632.0*RoomScale,EntityZ(e\room\RoomDoors[i]\obj)
					PositionEntity e\room\RoomDoors[i]\obj2,EntityX(e\room\RoomDoors[i]\obj2),-5632.0*RoomScale,EntityZ(e\room\RoomDoors[i]\obj2)
					PositionEntity e\room\RoomDoors[i]\buttons[0],EntityX(e\room\RoomDoors[i]\buttons[0]),-5632.0*RoomScale+0.6,EntityZ(e\room\RoomDoors[i]\buttons[0])
					PositionEntity e\room\RoomDoors[i]\buttons[1],EntityX(e\room\RoomDoors[i]\buttons[1]),-5632.0*RoomScale+0.7,EntityZ(e\room\RoomDoors[i]\buttons[1])
					For ne = Each NewElevator
						If ne\door = e\room\RoomDoors[i]
							If ne\currfloor = 1 And ne\state = 0.0
								e\room\RoomDoors[i]\open = True
							Else
								e\room\RoomDoors[i]\open = False
							EndIf
						EndIf
					Next
				Next
			EndIf
			CameraFogRange Camera,1,6
			CameraFogColor Camera,5,20,3
			CameraClsColor Camera,5,20,3
			CameraRange Camera,0.01,7
			HideEntity e\room\Objects[4]
			
			PositionTexture e\room\Objects[3],0,e\EventState[1]*0.005
			WaterRender_IgnoreObject = e\room\Objects[4]
			EntityAlpha MapCubeMap\CamOverlay,0.7
			CameraFogRange MapCubeMap\Cam,1,6
			CameraFogColor MapCubeMap\Cam,5,20,3
			ShouldUpdateWater = "sewers_water1"
			ShowEntity e\room\Objects[4]
			
		Else ; ~ Exit
			
			PlayerFallingPickDistance = 10.0
			
			HideEntity e\room\Objects[5]
			HideEntity e\room\Objects[0]
			HideEntity e\room\Objects[4]
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
			e\EventState[1] = 0.0
			e\EventState[2] = 0.0
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
		HideEntity e\room\Objects[5]
		HideEntity e\room\Objects[4]
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D