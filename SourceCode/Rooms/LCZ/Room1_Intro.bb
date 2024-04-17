
Const ROOM1_INTRO_Pipe_Obj% = 0
Const ROOM1_INTRO_Tram_Obj% = 1
Const ROOM1_INTRO_Elevator_Obj% = 2
Const ROOM1_INTRO_Monitors_Obj% = 3
Const ROOM1_INTRO_Dark_Sprite% = 4
Const ROOM1_INTRO_Go_Point% = 5
Const ROOM1_INTRO_Event_Hit_Point% = 6
Const ROOM1_INTRO_Pipe_Point% = 7
Const ROOM1_INTRO_Room_Object% = 8

Const ROOM1_INTRO_Elevator_Door% = 0
Const ROOM1_INTRO_Windowed_Door% = 1
Const ROOM1_INTRO_Entrance_Door% = 2

Function FillRoom_Room1_Intro(r.Rooms)
	Local ne.NewElevator, d.Doors
	
	r\Objects[ROOM1_INTRO_Room_Object] = r\obj
	
	r\Objects[ROOM1_INTRO_Pipe_Obj] =	LoadAnimMesh_Strict("GFX\Map\props\Intro_Pipe.b3d")
	ScaleEntity r\Objects[ROOM1_INTRO_Pipe_Obj], RoomScale*3,RoomScale*3,RoomScale*3
	PositionEntity(r\Objects[ROOM1_INTRO_Pipe_Obj],r\x+3331*RoomScale,r\y-1*RoomScale,r\z+1126*RoomScale)
	RotateEntity(r\Objects[ROOM1_INTRO_Pipe_Obj],EntityPitch(r\Objects[ROOM1_INTRO_Pipe_Obj]),EntityYaw(r\Objects[ROOM1_INTRO_Pipe_Obj]),EntityRoll(r\Objects[ROOM1_INTRO_Pipe_Obj])+450)
	EntityParent r\Objects[ROOM1_INTRO_Pipe_Obj], r\obj
	
	r\Objects[ROOM1_INTRO_Tram_Obj] = LoadAnimMesh_Strict("GFX\Map\props\Tram.b3d")
	ScaleEntity r\Objects[ROOM1_INTRO_Tram_Obj], RoomScale*4,RoomScale*4,RoomScale*4
	MeshCullBox (r\Objects[ROOM1_INTRO_Tram_Obj], -MeshWidth(r\Objects[ROOM1_INTRO_Tram_Obj]), -MeshHeight(r\Objects[ROOM1_INTRO_Tram_Obj]), -MeshDepth(r\Objects[ROOM1_INTRO_Tram_Obj]), MeshWidth(r\Objects[ROOM1_INTRO_Tram_Obj])*2, MeshHeight(r\Objects[ROOM1_INTRO_Tram_Obj])*2, MeshDepth(r\Objects[ROOM1_INTRO_Tram_Obj])*2)
	PositionEntity(r\Objects[ROOM1_INTRO_Tram_Obj],r\x+7823*RoomScale,r\y-905*RoomScale,r\z+2903*RoomScale)
	RotateEntity(r\Objects[ROOM1_INTRO_Tram_Obj],EntityPitch(r\Objects[ROOM1_INTRO_Tram_Obj]),EntityYaw(r\Objects[ROOM1_INTRO_Tram_Obj])-90,EntityRoll(r\Objects[ROOM1_INTRO_Tram_Obj]))
	EntityParent r\Objects[ROOM1_INTRO_Tram_Obj], r\obj
	
	r\Objects[ROOM1_INTRO_Monitors_Obj] = LoadMesh_Strict("GFX\Map\Rooms\Room1_intro\room1_intro_monitors.b3d")
	ScaleEntity r\Objects[ROOM1_INTRO_Monitors_Obj], RoomScale, RoomScale, RoomScale
	PositionEntity r\Objects[ROOM1_INTRO_Monitors_Obj], r\x, r\y, r\z, True
	EntityParent r\Objects[ROOM1_INTRO_Monitors_Obj], r\obj
	
	r\Objects[ROOM1_INTRO_Go_Point] = CreatePivot()
	PositionEntity r\Objects[ROOM1_INTRO_Go_Point], r\x+4053*RoomScale, r\y-820*RoomScale, r\z+1475*RoomScale, True
	EntityParent r\Objects[ROOM1_INTRO_Go_Point], r\obj
	
	r\Objects[ROOM1_INTRO_Event_Hit_Point] = CreatePivot()
	PositionEntity r\Objects[ROOM1_INTRO_Event_Hit_Point], r\x+3367*RoomScale, r\y-820*RoomScale, r\z+1145*RoomScale, True
	EntityParent r\Objects[ROOM1_INTRO_Event_Hit_Point], r\obj
	
	r\Objects[ROOM1_INTRO_Pipe_Point] = CreatePivot()
	PositionEntity r\Objects[ROOM1_INTRO_Pipe_Point], r\x+3054*RoomScale, r\y+95*RoomScale, r\z+1116*RoomScale, True
	EntityParent r\Objects[ROOM1_INTRO_Pipe_Point], r\obj
	
	Local ElevatorOBJ = LoadRMesh("GFX\map\Elevators\elevator_cabin_4.rmesh",Null)
	HideEntity ElevatorOBJ
	r\Objects[ROOM1_INTRO_Elevator_Obj] = CopyEntity(ElevatorOBJ,r\obj)
	
	PositionEntity(r\Objects[ROOM1_INTRO_Elevator_Obj],8177,1733,1227)
	EntityType r\Objects[ROOM1_INTRO_Elevator_Obj],HIT_MAP
	EntityPickMode r\Objects[ROOM1_INTRO_Elevator_Obj],2
	
	r\RoomDoors[ROOM1_INTRO_Elevator_Door] = CreateDoor(r\zone,r\x +7849.0 * RoomScale, r\y+1733*RoomScale, r\z+1227*RoomScale, 90, r, True, 5, False, "", 1)
	r\RoomDoors[ROOM1_INTRO_Elevator_Door]\DisableWaypoint = True
	
	ne = CreateNewElevator(r\Objects[ROOM1_INTRO_Elevator_Obj],3,r\RoomDoors[ROOM1_INTRO_Elevator_Door],1,r,-1000.0,-820.0,1779.0)
	ne\floorlocked[0] = True
	
	CreateDarkSprite(r, ROOM1_INTRO_Dark_Sprite)
	
	r\RoomDoors[ROOM1_INTRO_Windowed_Door] = CreateDoor(r\zone,r\x+5080.0 * RoomScale, r\y-824*RoomScale, r\z+1194*RoomScale, 90, r, False, DOOR_WINDOWED)
	
	r\RoomDoors[ROOM1_INTRO_Entrance_Door] = CreateDoor(r\zone,r\x+1961.0 * RoomScale, r\y-824*RoomScale, r\z+1192*RoomScale, 90, r, False, DOOR_DEFAULT)
	r\RoomDoors[ROOM1_INTRO_Entrance_Door]\locked = True
	
	d = CreateDoor(r\zone,r\x+5142.0 * RoomScale, r\y+1733*RoomScale, r\z+1227*RoomScale, 90, r, False, DOOR_LCZ)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+7191.0 * RoomScale, r\y+1733*RoomScale, r\z+1227*RoomScale, 90, r, False, DOOR_LCZ)
	
	d = CreateDoor(r\zone,r\x+7864.0 * RoomScale, r\y-824*RoomScale, r\z+666*RoomScale, 90, r, False, DOOR_DEFAULT)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+7158.0 * RoomScale, r\y-824*RoomScale, r\z+697*RoomScale, 90, r, False, DOOR_DEFAULT)
	d\locked = True
	
	d = CreateDoor(r\zone,r\x+7160.0 * RoomScale, r\y-824*RoomScale, r\z+1227*RoomScale, 90, r, False, DOOR_DEFAULT)
	
End Function

Function UpdateEvent_Room1_Intro(e.Events)
	Local ne.NewElevator, it.Items
	
	If PlayerRoom = e\room Then
		
		;! ~ [Screens]
		
		e\EventState[5] = e\EventState[5] + FPSfactor
		
		If e\EventState[5] > 70*5 Then
			If e\EventState[4] < 1 Then
				EntityTexture e\room\Objects[ROOM1_INTRO_Monitors_Obj], Tram_Screen[0], Floor(((e\EventState[5]-70*5)/(70*5)) Mod 2.0)
			Else
				EntityTexture e\room\Objects[ROOM1_INTRO_Monitors_Obj], Tram_Screen[1], Floor(((e\EventState[5]-70*5)/(70*5)) Mod 2.0)
			EndIf
		EndIf
		
		;! ~ [Music]
		
		If e\EventState[4] < 5 Then
			ShouldPlay = MUS_INTRODUCTION
		Else
			ShouldPlay = MUS_CHASE_106
		EndIf
		
		;! ~ [Fog]
		
		CameraFogRange Camera, 5,30
		CameraFogColor (Camera,200,200,200)
		CameraClsColor (Camera,200,200,200)
		CameraRange(Camera, 0.005, 100)
		HideEntity Fog
		
		;! ~ [Other]
		
		Curr106\Idle = True
		Curr173\Idle = True
		PositionEntity Curr173\Collider,0,-2000,0
		
		If e\EventState[5] > 70*49 And e\EventState[5] < 70*49.017 Then
			PlayAnnouncement("SFX\Intercom\on.ogg")
		EndIf
		If e\EventState[5] > 70*50 And e\EventState[5] < 70*50.017 Then
			PlayAnnouncement("SFX\Intercom\Facility\Scripted\scripted"+Rand(1,5)+".ogg")
		EndIf
		
		;! ~ [NPCs]
		
		If e\room\NPC[0] = Null Then
			e\room\NPC[0] = CreateNPC(NPC_Guard, e\room\x+7771*RoomScale, e\room\y-730*RoomScale, e\room\z+672*RoomScale)
			RotateEntity(e\room\NPC[0]\Collider,0,e\room\angle+90,0)
			e\room\NPC[0]\State[0] = 9
			ChangeNPCTexture(e\room\NPC[0], "GFX\npcs\guards\guard_bill.png")
		EndIf
		If e\room\NPC[1] = Null Then
			e\room\NPC[1] = CreateNPC(NPC_Human, e\room\x+3921*RoomScale, e\room\y-730*RoomScale, e\room\z+2279*RoomScale)
			ChangeNPCTexture(e\room\NPC[1], "GFX\npcs\worker.jpg")
		EndIf
		If e\room\NPC[2] = Null Then
			e\room\NPC[2] = CreateNPC(NPC_Human, e\room\x+4596*RoomScale, e\room\y-730*RoomScale, e\room\z+1900*RoomScale)
			e\room\NPC[2]\State[0] = STATE_SCRIPT
			ChangeNPCTexture(e\room\NPC[2], "GFX\npcs\scientist2.jpg")
		EndIf
		If e\room\NPC[3] = Null Then
			e\room\NPC[3] = CreateNPC(NPC_SM4Nn, e\room\x+2111*RoomScale, e\room\y+200*RoomScale, e\room\z+4799*RoomScale)
			RotateEntity(e\room\NPC[3]\Collider,0,e\room\angle-90,0)
			e\room\NPC[3]\State[0] = STATE_SCRIPT
		EndIf
		If e\room\NPC[4] = Null Then
			e\room\NPC[4] = CreateNPC(NPC_Human, e\room\x+8690*RoomScale, e\room\y+1816*RoomScale, e\room\z+1231*RoomScale)
			RotateEntity(e\room\NPC[4]\Collider,0,e\room\angle+90,0)
			e\room\NPC[4]\State[0] = STATE_SCRIPT
			ChangeNPCTexture(e\room\NPC[4], "GFX\npcs\scientist3.jpg")
		EndIf
		If e\room\NPC[5] = Null Then
			e\room\NPC[5] = CreateNPC(NPC_Human, e\room\x+8690*RoomScale, e\room\y-746*RoomScale, e\room\z+1231*RoomScale)
			RotateEntity(e\room\NPC[5]\Collider,0,e\room\angle+90,0)
			e\room\NPC[5]\State[0] = STATE_SCRIPT
			ChangeNPCTexture(e\room\NPC[5], "GFX\npcs\scientist4.jpg")
		EndIf
		
		;! ~ [Introduction]
		
		If e\EventState[0] = 0 Then
			
			e\Sound[0] = LoadSound_Strict("SFX\General\Spark_Short.ogg")
			
			e\Sound[1] = LoadSound_Strict("SFX\General\Spark_Medium.ogg")
			
			; ~ Beretta
			it = CreateItem("M9 Beretta", "beretta", 1, 1, 1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			Inventory[SLOT_HOLSTER] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
			; ~ Knife
			it = CreateItem(GetLocalString("Item Names","knife"), "knife", 1, 1, 1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			Inventory[SLOT_SCABBARD] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
			; ~ Level 3 Key Card
			it = CreateItem(GetLocalString("Item Names","key_3"), "key3", 1, 1, 1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			Inventory[0] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
			; ~ Radio
			it = CreateItem(GetLocalString("Item Names","radio"), "radio", 1, 1, 1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			it\state = 1000
			Inventory[1] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
			; ~ Vest
			it = CreateItem(GetLocalString("Item Names","vest"), "vest", 1, 1, 1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			it\state = 100
			Inventory[SLOT_TORSO] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
			; ~ Helmet
			it = CreateItem(GetLocalString("Item Names","helmet"), "helmet", 1, 1, 1)
			it\Picked = True
			it\Dropped = -1
			it\itemtemplate\found=True
			it\state = 100
			Inventory[SLOT_HEAD] = it
			HideEntity(it\collider)
			EntityType (it\collider, HIT_ITEM)
			EntityParent(it\collider, 0)
			ItemAmount = ItemAmount + 1
			
			If (Not ChannelPlaying(e\SoundCHN[0])) Then
				e\SoundCHN[0] = PlaySound_Strict(LoadTempSound("SFX\Room\Intro\Ryan\ArrivalSound.ogg"))
			EndIf
			e\EventState[0] = 1
		EndIf
		
		If e\EventState[0] > 0 Then
			e\EventState[0] = e\EventState[0] + FPSfactor
		EndIf
		
		If e\EventState[0] > 70*1 And e\EventState[0] < 70*7 Then
			e\EventState[1] = e\EventState[1] - (0.01*FPSfactor)
			EntityAlpha e\room\Objects[ROOM1_INTRO_Dark_Sprite],Min(1,e\EventState[1]+3)
		EndIf
		
		If e\EventState[0] > 70*5 Then
			If TaskExists(TASK_GO_TO_LCZ) Then
				EndTask(TASK_GO_TO_LCZ)
				BeginTask(TASK_GO_TO_TRAM)
			Else
				If (Not TaskExists(TASK_GO_TO_TRAM))
					BeginTask(TASK_GO_TO_TRAM)
				EndIf
			EndIf
			If HUDenabled Then
				psp\IsShowingHUD = True
			EndIf
			psp\NoMove = False
			psp\NoRotation = False
		ElseIf e\EventState[0] < 70*5
			EntityAlpha e\room\Objects[ROOM1_INTRO_Dark_Sprite],1
			psp\NoMove = True
			psp\NoRotation = True
			psp\IsShowingHUD = False
		EndIf
		
		;! ~ Guard dialogue
		
		If e\room\NPC[0] <> Null Then
			If e\EventState[3] <> 4 Then
				If InteractWithObject(FindChild(e\room\NPC[0]\obj,"Neck"),1.0) Then
					If e\EventState[3] = 0 Then
						PlayPlayerSPVoiceLine("SFX\Room\Intro\Ryan\GuardHello"+(Rand(1,3)))
						e\EventState[3] = 1
					ElseIf e\EventState[3] = 1 And (Not ChannelPlaying(psp\SoundCHN)) Then
						PlayPlayerSPVoiceLine("SFX\Room\Intro\Ryan\GuardRandom"+(Rand(1,2)))
						e\EventState[3] = 2
					ElseIf e\EventState[3] = 2 And (Not ChannelPlaying(psp\SoundCHN)) Then
						PlayPlayerSPVoiceLine("SFX\Room\Intro\Ryan\GuardAssigned"+(Rand(1,2)))
						e\EventState[3] = 3
					ElseIf e\EventState[3] = 3 And (Not ChannelPlaying(psp\SoundCHN)) Then
						PlayPlayerSPVoiceLine("SFX\Room\Intro\Ryan\GuardAssigned3")
						e\EventState[3] = 4
					EndIf
				EndIf
			EndIf
		EndIf
		
		;! ~ Worker dialogue
		
		If e\room\NPC[1] <> Null Then
			If e\EventState[4] = 1 And (Not ChannelPlaying(psp\SoundCHN)) Then
				If InteractWithObject(FindChild(e\room\NPC[1]\obj,"Bip01_Head"),1.0) Then
					PlayPlayerSPVoiceLine("SFX\Room\Intro\Ryan\WorkerWarn")
					e\EventState[4] = 2
				EndIf
			ElseIf e\EventState[4] = 2 And ChannelPlaying(psp\SoundCHN) Then
				
				Local pvt% = CreatePivot()
				PositionEntity pvt, EntityX(Camera), EntityY(e\room\NPC[1]\Collider,True)-0.05, EntityZ(Camera)
				PointEntity(pvt, e\room\NPC[1]\Collider)
				RotateEntity(Collider, EntityPitch(Collider), CurveAngle(EntityYaw(pvt), EntityYaw(Collider), 80-(e\EventState[10]/200.0)), 0)
				
				TurnEntity(pvt, 70, 0, 0)
				user_camera_pitch = CurveAngle(EntityPitch(pvt)+25, user_camera_pitch + 90.0, 80-(e\EventState[10]/200.0))
				user_camera_pitch=user_camera_pitch-90
				
				psp\NoMove = True
				
			ElseIf e\EventState[4] = 2 And (Not ChannelPlaying(psp\SoundCHN)) Then
				
				psp\NoMove = False
				
				e\EventState[4] = 3
			EndIf
		EndIf
		
		;! ~ Other NPCs
		
		
		; ~ (Scientist)
		
		If e\room\NPC[2] <> Null Then
			AnimateNPC(e\room\NPC[2],561,660,0.3,True)
		EndIf
		
		; ~ (Scientist)
		
		If e\room\NPC[4] <> Null Then
			AnimateNPC(e\room\NPC[4],561,660,0.3,True)
		EndIf
		
		; ~ (Scientist)
		
		If e\room\NPC[5] <> Null Then
			AnimateNPC(e\room\NPC[5],561,660,0.3,True)
		EndIf
		
		; ~ (5%$M3A1Nn4_)
		
		If e\room\RoomDoors[ROOM1_INTRO_Windowed_Door]\open Then
			If e\room\NPC[3] <> Null Then
				e\room\NPC[3]\State[0] = 1
			EndIf
		EndIf
		
		;! ~ [Cutscene]
		
		;! ~ Tram arrival
		
		Local steam1bone% = FindChild(e\room\Objects[ROOM1_INTRO_Tram_Obj], "Steam1")
		Local steam2bone% = FindChild(e\room\Objects[ROOM1_INTRO_Tram_Obj], "Steam2")
		
		If e\room\RoomDoors[ROOM1_INTRO_Windowed_Door]\open Then
			If e\EventState[4] = 0 Then
				
				e\EventState[7] = e\EventState[7] + FPSfactor
				
				If (Not ChannelPlaying(e\SoundCHN[0])) Then
					e\SoundCHN[0] = PlaySound2(LoadTempSound("SFX\Room\Intro\Ryan\TramBreak.ogg"),Camera,e\room\Objects[ROOM1_INTRO_Tram_Obj],25,2)
				EndIf
				MoveEntity e\room\Objects[ROOM1_INTRO_Tram_Obj],0,0,-(FPSfactor*3.4)
				
				If e\EventState[7] > 70*15 Then
					Local p.Particles = CreateParticle(EntityX(steam1bone,True),EntityY(steam1bone,True),EntityZ(steam1bone,True),6,0.4,-0.1,260)
					EntityParent p\obj, steam1bone
				EndIf
				
				If e\EventState[7] > 70*20 And e\EventState[7] < 70*20.1 Then
					PlayPlayerSPVoiceLine("SFX\Room\Intro\Ryan\WorkerMad")
					e\EventState[4] = 1
				EndIf
			EndIf
		EndIf
		
		If e\EventState[4] > 0 Then
			p.Particles = CreateParticle(EntityX(steam1bone,True),EntityY(steam1bone,True),EntityZ(steam1bone,True),6,0.4,-0.1,260)
			EntityParent p\obj, steam1bone
			
			Local i
			
			If Rand(20) = 1 Then
				If Rand(3) = 1 Then
					PlaySound2(e\Sound[1], Camera, steam2bone, 8.0, 0.6)
				Else
					PlaySound2(e\Sound[0], Camera, steam2bone, 8.0, 0.6)
				EndIf
				If ParticleAmount > 0 Then
					For i = 0 To (2 + (1 * (ParticleAmount - 1)))
						p.Particles = CreateParticle(EntityX(steam2bone,True),EntityY(steam2bone,True),EntityZ(steam2bone,True),7, 0.01, 0.0, 25.0)
						p\speed = Rnd(0.005, 0.03) : p\size = Rnd(0.005, 0.0075) : p\Achange = -0.05
						RotateEntity(p\pvt, Rnd(-20.0, 0.0), e\room\angle, 0.0)
						ScaleSprite(p\obj, p\size, p\size)
					Next
				EndIf	
			EndIf
		EndIf
		
		;! ~ Pipe event
		
		If e\EventState[4] = 3 Then
			
			pvt% = CreatePivot()
			PositionEntity pvt, EntityX(Camera), EntityY(e\room\Objects[ROOM1_INTRO_Go_Point],True)-0.05, EntityZ(Camera)
			PointEntity(pvt, e\room\Objects[ROOM1_INTRO_Go_Point])
			RotateEntity(Collider, EntityPitch(Collider), CurveAngle(EntityYaw(pvt), EntityYaw(Collider), 80-(e\EventState[7]/200.0)), 0)
			
			TurnEntity(pvt, 70, 0, 0)
			user_camera_pitch = CurveAngle(EntityPitch(pvt)+25, user_camera_pitch + 90.0, 80-(e\EventState[7]/200.0))
			user_camera_pitch=user_camera_pitch-90
			
			If EntityDistanceSquared(Collider,e\room\Objects[ROOM1_INTRO_Go_Point]) < PowTwo(0.5) Then
				IsZombie = False
				ForceMove = 0.0
				;psp\NoRotation = False
				FreeEntity pvt
				e\EventState[4] = 4
			Else
				;psp\NoRotation = True
				IsZombie = True
				ForceMove = 1.0
			EndIf
			
		ElseIf e\EventState[4] = 4 Then
			
			pvt% = CreatePivot()
			PositionEntity pvt, EntityX(Camera), EntityY(e\room\RoomDoors[ROOM1_INTRO_Entrance_Door]\frameobj,True)-0.05, EntityZ(Camera)
			PointEntity(pvt, e\room\RoomDoors[ROOM1_INTRO_Entrance_Door]\frameobj)
			RotateEntity(Collider, EntityPitch(Collider), CurveAngle(EntityYaw(pvt), EntityYaw(Collider), 80-(e\EventState[8]/200.0)), 0)
			
			TurnEntity(pvt, 70, 0, 0)
			user_camera_pitch = CurveAngle(EntityPitch(pvt)+25, user_camera_pitch + 90.0, 80-(e\EventState[8]/200.0))
			user_camera_pitch=user_camera_pitch-90
			
			If EntityDistanceSquared(Collider,e\room\Objects[ROOM1_INTRO_Event_Hit_Point]) < PowTwo(0.6) Then
				IsZombie = False
				ForceMove = 0.0
				;psp\NoRotation = False
				FreeEntity pvt
				e\EventState[4] = 5
			Else
				;psp\NoRotation = True
				IsZombie = True
				ForceMove = 1.0
			EndIf
			
		ElseIf e\EventState[4] = 5 Then
			
			TeleportEntity(Collider, EntityX(e\room\Objects[ROOM1_INTRO_Event_Hit_Point], True), EntityY(e\room\Objects[ROOM1_INTRO_Event_Hit_Point], True), EntityZ(e\room\Objects[ROOM1_INTRO_Event_Hit_Point], True),0.3,True)
			
			PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\whatthehellpipe")
			
			PlaySound_Strict(LoadTempSound("SFX\SCP\106\Laugh.ogg"))
			
			Local de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[ROOM1_INTRO_Pipe_Point], True), EntityY(e\room\Objects[ROOM1_INTRO_Pipe_Point], True) - 0.01, EntityZ(e\room\Objects[ROOM1_INTRO_Pipe_Point], True), -90, Rand(360), 0)
			de\Size = 0.1 : de\SizeChange = 0.01 : de\MaxSize = 1.75 : EntityAlpha(de\obj, 1.0)
			
			e\EventState[4] = 6
			
		ElseIf e\EventState[4] = 6 Then
			
			psp\NoMove = True
			psp\NoRotation = True
			
			user_camera_pitch = CurveAngle(DeltaPitch(Camera, e\room\Objects[ROOM1_INTRO_Pipe_Point]), user_camera_pitch, 15.0)
			
			Local PrevPipeAnim# = AnimTime(e\room\Objects[ROOM1_INTRO_Pipe_Obj])
			
			Animate2(e\room\Objects[ROOM1_INTRO_Pipe_Obj], AnimTime(e\room\Objects[ROOM1_INTRO_Pipe_Obj]), 1, 140, 0.31, False)
			
			If AnimTime(e\room\Objects[ROOM1_INTRO_Pipe_Obj]) >= 105 Then
				e\EventState[4] = 7
			EndIf
			
		ElseIf e\EventState[4] = 7 Then
			
			If KillTimer >= 0 And FallTimer >= 0 Then
				FallTimer = Min(-1, FallTimer)
				PositionEntity(Head, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True), True)
				ResetEntity (Head)
				RotateEntity(Head, 0, EntityYaw(Camera) + Rand(-45, 45), 0)
				mpl\DamageTimer = 70.0*1.0
				psp\Health = 70
				psp\IsShowingHUD = False
			EndIf
			
			BlurTimer = 10
			LightFlash = 0.7
			
			EntityAlpha e\room\Objects[ROOM1_INTRO_Dark_Sprite],Min(e\EventState[6]-3,1)
			PointEntity Camera,e\room\Objects[ROOM1_INTRO_Pipe_Point],EntityRoll(Camera)
			Animate2(e\room\Objects[ROOM1_INTRO_Pipe_Obj], AnimTime(e\room\Objects[ROOM1_INTRO_Pipe_Obj]), 1.0, 140.0, 0.31,False)
			
			e\EventState[4] = 8
			
		ElseIf e\EventState[4] = 8 Then
			
			e\EventState[6] = e\EventState[6] + (0.01*FPSfactor)
			
		EndIf
		
		If e\EventState[6] > 5.05 Then
			psp\IsShowingHUD = False
			Local r.Rooms
			For r = Each Rooms
				If r\RoomTemplate\Name = "room1_start" Then
					PlayerRoom = r
					TeleportEntity(Collider, r\x,r\y,r\z)
					Exit
				EndIf
			Next
			DropSpeed = 0
			FallTimer = 0
			psp\NoMove = False
			psp\NoRotation = False
			IsZombie = False
			FlushKeys()
			FlushMouse()
			FlushJoy()
			;FreeEntity e\room\Objects[ROOM1_INTRO_Room_Object]
			SaveGame(SavePath + CurrSave\Name + "\", True)
			Return
		EndIf
		
	EndIf
	
End Function

; ~ Old Event

;Function FillRoom_Room1_Intro(r.Rooms)
;	Local it.Items,ne.NewElevator,d.Doors
;	
;	r\Objects[1] = CreatePivot()
;	PositionEntity(r\Objects[1], r\x +16.0 * RoomScale, r\y +20.0 * RoomScale, r\z -2416.0 * RoomScale)
;	EntityParent(r\Objects[1], r\obj)
;	
;	r\Objects[4] = CreatePivot()
;	PositionEntity(r\Objects[4], r\x -1.0 * RoomScale, r\y +222.0 * RoomScale, r\z -3603.0 * RoomScale)
;	EntityParent(r\Objects[4], r\obj)
;	
;	r\Objects[2] = CreatePivot()
;	PositionEntity(r\Objects[2], r\x -2.0 * RoomScale, r\y +597.0 * RoomScale, r\z -3057.0 * RoomScale)
;	EntityParent(r\Objects[2], r\obj)
;	
;	r\Objects[3] = CreatePivot()
;	PositionEntity(r\Objects[3], r\x -2.0 * RoomScale, r\y +837.0 * RoomScale, r\z -2933.0 * RoomScale)
;	EntityParent(r\Objects[3], r\obj)
;	
;	r\Objects[0] = CreatePivot()
;	PositionEntity(r\Objects[0], r\x -2 * RoomScale, r\y +7.0 * RoomScale, r\z -1390.0 * RoomScale)
;	EntityParent(r\Objects[0], r\obj)
;	
;	r\Objects[5] = CreatePivot()
;	PositionEntity(r\Objects[5], r\x +3.0 * RoomScale, r\y +938.0 * RoomScale, r\z -2925.0 * RoomScale)
;	EntityParent(r\Objects[5], r\obj)
;	
;	r\Objects[8] = CreatePivot()
;	PositionEntity(r\Objects[8], r\x -1262.0 * RoomScale, r\y -5.0 * RoomScale, r\z -1924.0 * RoomScale)
;	EntityParent(r\Objects[8], r\obj)
;	
;	r\Objects[6] =	LoadAnimMesh_Strict("GFX\Map\props\Intro_Pipe.b3d")
;	ScaleEntity r\Objects[6], RoomScale*3,RoomScale*3,RoomScale*3
;	PositionEntity(r\Objects[6],r\x -2*RoomScale,r\y+837*RoomScale,r\z-2653*RoomScale)
;	RotateEntity(r\Objects[6],EntityPitch(r\Objects[6])+360,EntityYaw(r\Objects[6])+90,EntityRoll(r\Objects[6])+450)
;	
;	r\Objects[9] =	LoadAnimMesh_Strict("GFX\Map\props\Tram.b3d")
;	ScaleEntity r\Objects[9], RoomScale*3,RoomScale*3,RoomScale*3
;	PositionEntity(r\Objects[9],r\x -1718*RoomScale,r\y-81*RoomScale,r\z)
;	
;	r\Objects[7] = r\obj
;	
;	r\RoomDoors[0] = CreateDoor(r\zone,r\x-2.0*RoomScale,r\y-6.0*RoomScale,r\z-3697.0*RoomScale,0,r,False,False)
;	r\RoomDoors[0]\open = False
;	
;	r\RoomDoors[1] = CreateDoor(r\zone,r\x-2.0*RoomScale,r\y-6.0*RoomScale,r\z-1202.0*RoomScale,0,r,False,DOOR_WINDOWED)
;	r\RoomDoors[1]\open = False
;	
;	CreateDarkSprite(r, 10)
;	
;	Local ElevatorOBJ = LoadRMesh("GFX\map\Elevators\elevator_cabin_4.rmesh",Null)
;	HideEntity ElevatorOBJ
;	r\Objects[11] = CopyEntity(ElevatorOBJ,r\obj)
;	
;	PositionEntity(r\Objects[11],2881,2560,1562)
;	EntityType r\Objects[11],HIT_MAP
;	EntityPickMode r\Objects[11],2
;	
;	r\RoomDoors[2] = CreateDoor(r\zone,r\x +2561.0 * RoomScale, r\y+2560*RoomScale, r\z+1562*RoomScale, 90, r, True, 5, False, "", 2)
;	r\RoomDoors[2]\DisableWaypoint = True
;	
;	ne = CreateNewElevator(r\Objects[11],3,r\RoomDoors[2],2,r,-10.0,0.0,2560.0)
;	ne\floorlocked[0] = True
;	
;	r\Objects[12] = CopyEntity(ElevatorOBJ,r\obj)
;	
;	PositionEntity(r\Objects[12],735,2560,7698)
;	RotateEntity r\Objects[12],0,90,0
;	EntityType r\Objects[12],HIT_MAP
;	EntityPickMode r\Objects[12],2
;	
;	r\RoomDoors[3] = CreateDoor(r\zone,r\x +735.0 * RoomScale, r\y+2560*RoomScale, r\z+7378*RoomScale, 0, r, True, 5, False, "", 3)
;	r\RoomDoors[3]\DisableWaypoint = True
;	
;	ne = CreateNewElevator(r\Objects[12],3,r\RoomDoors[3],3,r,-10.0,0.0,2560.0)
;	Local i
;	For i = 0 To 2
;		ne\floorlocked[i] = True
;	Next
;	
;	d = CreateDoor(r\zone,r\x + 735.0 * RoomScale, r\y+2560*RoomScale, r\z+4628*RoomScale, 0, r, False, False)
;	
;	d = CreateDoor(r\zone,r\x + 735.0 * RoomScale, r\y+2560*RoomScale, r\z+6693*RoomScale, 0, r, False, False)
;	
;	d = CreateDoor(r\zone,r\x - 296.0 * RoomScale, r\y+2560*RoomScale, r\z+5661*RoomScale, -90, r, False, False)
;	
;	d = CreateDoor(r\zone,r\x + 735.0 * RoomScale, r\y+2560*RoomScale, r\z+2588*RoomScale, 0, r, False, False)
;	
;	d = CreateDoor(r\zone,r\x + 796.0 * RoomScale, r\y+2560*RoomScale, r\z+1564*RoomScale, 90, r, True, False)
;	
;	d = CreateDoor(r\zone,r\x + 1880.0 * RoomScale, r\y+2560*RoomScale, r\z+1564*RoomScale, 90, r, False, False, KEY_CARD_2)
;	
;	d = CreateDoor(r\zone,r\x +1847.0 * RoomScale, r\y, r\z+1562*RoomScale, 90, r, False, False)
;	
;	d = CreateDoor(r\zone,r\x - 4.0 * RoomScale, r\y, r\z+794*RoomScale, 0, r, False, False)
;	
;	it = CreateItem(GetLocalString("Item Names","key_2"), "key2", r\x+199.0*RoomScale, r\y+2663.0*RoomScale, r\z+1417.0*RoomScale)
;	EntityParent(it\collider, r\Objects[7])
;	
;	it = CreateItem(GetLocalString("Item Names","helmet"), "helmet", r\x+199.0*RoomScale, r\y+2760.0*RoomScale, r\z+1417.0*RoomScale)
;	EntityParent(it\collider, r\Objects[7])
;	
;	it = CreateItem("H&K USP", "usp", r\x+196.0*RoomScale, r\y+2663.0*RoomScale, r\z+1329.0*RoomScale)
;	EntityParent(it\collider, r\Objects[7])
;	
;End Function
;
;Function UpdateEvent_Room1_Intro(e.Events)
;	Local de.Decals,ne.NewElevator,b
;	
;	If PlayerRoom = e\room
;		Curr106\Idle = True
;		Curr173\Idle = True
;		PositionEntity Curr173\Collider,0,-2000,0
;		
;		If e\EventState[2] = 0 Then
;			CreateSplashText("SCP - SECURITY STORIES",opt\GraphicWidth/2,opt\GraphicHeight/2,100,20,Font_Menu_Medium,True,False)
;			psp\IsShowingHUD = False
;			psp\NoMove = True
;			psp\NoRotation = True
;			wbl\Vest = True
;			psp\Kevlar = 100
;			EntityAlpha e\room\Objects[10],1
;			e\EventState[2] = 1
;		EndIf
;		
;		If e\EventState[2] <> 0 Then
;			e\EventState[2] = e\EventState[2] + FPSfactor
;		EndIf
;		
;		If e\EventState[2] >= 70*6 Then
;			e\EventState[3] = e\EventState[3] - (0.01*FPSfactor)
;			EntityAlpha e\room\Objects[10],Min(1,e\EventState[3]+3)
;		EndIf
;		
;		If e\EventState[2] >= 70*10.0 And e\EventState[2] < 70*10.1 Then
;			If HUDenabled Then
;				psp\IsShowingHUD = True
;			EndIf
;			psp\NoMove = False
;			psp\NoRotation = False
;			If (Not TaskExists(TASK_GO_TO_TRAM)) Then
;				StartChapter("chapter_0")
;				cpt\Current = 0
;				BeginTask(TASK_GO_TO_TRAM)
;			EndIf
;			ElseIf e\EventState[2] < 70*10.0 Then
;				psp\IsShowingHUD = False
;			EndIf
;			
;			CanPlayerUseGuns% = False
;			
;			mpl\HasNTFGasmask = False
;			
;			If EntityY(Collider)<3000.0*RoomScale ; ~ 3rd floor
;				
;				If (Not PlayerInNewElevator)
;					PositionEntity e\room\RoomDoors[b]\frameobj,EntityX(e\room\RoomDoors[b]\frameobj),2560.0*RoomScale,EntityZ(e\room\RoomDoors[b]\frameobj)
;					PositionEntity e\room\RoomDoors[b]\obj,EntityX(e\room\RoomDoors[b]\obj),2560.0*RoomScale,EntityZ(e\room\RoomDoors[b]\obj)
;					PositionEntity e\room\RoomDoors[b]\obj2,EntityX(e\room\RoomDoors[b]\obj2),2560.0*RoomScale,EntityZ(e\room\RoomDoors[b]\obj2)
;					PositionEntity e\room\RoomDoors[b]\buttons[0],EntityX(e\room\RoomDoors[b]\buttons[0]),2560.0*RoomScale+0.6,EntityZ(e\room\RoomDoors[b]\buttons[0])
;					PositionEntity e\room\RoomDoors[b]\buttons[1],EntityX(e\room\RoomDoors[b]\buttons[1]),2560.0*RoomScale+0.7,EntityZ(e\room\RoomDoors[b]\buttons[1])
;					For ne = Each NewElevator
;						If ne\door = e\room\RoomDoors[b]
;							If ne\currfloor = 2 And ne\state = 0.0
;								e\room\RoomDoors[b]\open = True
;							Else
;								e\room\RoomDoors[b]\open = False
;							EndIf
;						EndIf
;					Next
;				EndIf
;				
;			Else ; ~ Exit
;				
;				If (Not PlayerInNewElevator)
;					PositionEntity e\room\RoomDoors[b]\frameobj,EntityX(e\room\RoomDoors[b]\frameobj),0.0,EntityZ(e\room\RoomDoors[b]\frameobj)
;					PositionEntity e\room\RoomDoors[b]\obj,EntityX(e\room\RoomDoors[b]\obj),0.0,EntityZ(e\room\RoomDoors[b]\obj)
;					PositionEntity e\room\RoomDoors[b]\obj2,EntityX(e\room\RoomDoors[b]\obj2),0.0,EntityZ(e\room\RoomDoors[b]\obj2)
;					PositionEntity e\room\RoomDoors[b]\buttons[0],EntityX(e\room\RoomDoors[b]\buttons[0]),0.0+0.6,EntityZ(e\room\RoomDoors[b]\buttons[0])
;					PositionEntity e\room\RoomDoors[b]\buttons[1],EntityX(e\room\RoomDoors[b]\buttons[1]),0.0+0.7,EntityZ(e\room\RoomDoors[b]\buttons[1])
;					For ne = Each NewElevator
;						If ne\door = e\room\RoomDoors[b]
;							If ne\currfloor = 3 And ne\state = 0.0
;								e\room\RoomDoors[b]\open = True
;							Else
;								e\room\RoomDoors[b]\open = False
;							EndIf
;						EndIf
;					Next
;				EndIf
;			EndIf
;			
;			If e\room\NPC[0] = Null Then
;				e\room\NPC[0]=CreateNPC(NPCtypeD, EntityX(e\room\Objects[8],True), 0.5, EntityZ(e\room\Objects[8],True))
;				ChangeNPCTexture(e\room\NPC[0],"GFX\npcs\scientist.jpg")
;				RotateEntity e\room\NPC[0]\Collider, 0, EntityYaw(e\room\obj)+90,0, True
;				e\room\NPC[0]\State[0] = 0
;			EndIf
;			
;			CameraFogRange Camera, 5,30
;			CameraFogColor (Camera,200,200,200)
;			CameraClsColor (Camera,200,200,200)
;			CameraRange(Camera, 0.005, 100)
;			HideEntity Fog
;			ShowEntity e\room\Objects[6]
;			ShowEntity e\room\Objects[9]
;			
;			Animate2(e\room\Objects[9], AnimTime(e\room\Objects[9]), 1.0, 251.0, 0.2,False)
;			
;			If e\EventState[1] <> 1.0 Then
;				If e\EventState[2] >= 70*8 Then
;					ShouldPlay = MUS_INTRODUCTION; ~ Intro Music
;				Else
;					ShouldPlay = MUS_NULL
;					If e\Sound[0] = 0 Then
;						e\Sound[0] = PlaySound_Strict(LoadTempSound("SFX\General\Introduction.ogg"))
;					EndIf
;				EndIf
;			ElseIf e\EventState[1] = 1.0
;				ShouldPlay = MUS_CHASE_106; ~ SCP-106 Music
;			EndIf
;			
;			If e\EventState[0] = 0 Then
;				If EntityDistanceSquared(e\room\Objects[0],Collider) < PowTwo(0.5) Then
;					PointEntity Collider, e\room\Objects[1]
;					IsZombie = True
;					BlurTimer = 0
;					ForceMove = 1.0
;					psp\NoMove = False
;					psp\NoRotation = True
;					e\EventState[0] = 0.5
;					
;					If TaskExists(TASK_GO_TO_TRAM) Then
;						EndTask(TASK_GO_TO_TRAM)
;					EndIf
;					
;				EndIf
;			ElseIf e\EventState[0] = 0.5 Then
;				user_camera_pitch = CurveAngle(DeltaPitch(Camera, e\room\Objects[4]), user_camera_pitch, 15.0)
;				e\EventState[1] = 1.0
;				If EntityDistanceSquared(e\room\Objects[1],Collider) < PowTwo(0.2) Then
;					
;					ForceMove = 0.0
;					BlurTimer = 8
;					IsZombie = True
;					psp\NoMove = True
;					psp\NoRotation = True
;					
;					user_camera_pitch = CurveAngle(DeltaPitch(Camera, e\room\Objects[2]), user_camera_pitch, 15.0)
;					
;					Local PrevAnim# = AnimTime(e\room\Objects[6])
;					
;					Animate2(e\room\Objects[6], AnimTime(e\room\Objects[6]), 1.0, 140.0, 0.31,False)
;					
;					If AnimTime(e\room\Objects[6]) >= 1.0 And PrevAnim < 1.0 Then
;						PlayNewDialogue(Dialogue_Intro,%01)
;					EndIf
;					
;					PlaySound_Strict(LoadTempSound("SFX\SCP\106\Laugh.ogg"))
;					
;					de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[5], True), EntityY(e\room\Objects[5], True) - 0.01, EntityZ(e\room\Objects[5], True), -90, Rand(360), 0)
;					de\Size = 0.1 : de\SizeChange = 0.01 : de\MaxSize = 1.75 : EntityAlpha(de\obj, 1.0)
;					
;					e\EventState[0] = 1
;				EndIf
;			ElseIf e\EventState[0] >= 1 And e\EventState[0] < 2 Then
;				
;				e\EventState[0] = e\EventState[0] + (FPSfactor / 70.0)/5
;				
;				user_camera_pitch = CurveAngle(DeltaPitch(Camera, e\room\Objects[3]), user_camera_pitch, 15.0)
;				Animate2(e\room\Objects[6], AnimTime(e\room\Objects[6]), 1.0, 140.0, 0.31,False)
;				
;				If e\EventState[0] >=2 Then
;					e\EventState[0] = 3
;				EndIf
;			Else
;				
;				psp\NoMove = True
;				psp\NoRotation = True
;				user_camera_pitch = CurveAngle(DeltaPitch(Camera, e\room\Objects[3]), user_camera_pitch, 15.0)
;				
;				If KillTimer >= 0 And FallTimer >= 0 Then
;					FallTimer = Min(-1, FallTimer)
;					PositionEntity(Head, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True), True)
;					ResetEntity (Head)
;					RotateEntity(Head, 0, EntityYaw(Camera) + Rand(-45, 45), 0)
;					mpl\DamageTimer = 70.0*1.0
;					If wbl\Helmet Then
;						wbl\Helmet = False
;						psp\Helmet = 0
;					EndIf
;					psp\Health = 70
;					wbl\Vest = False
;				EndIf
;				
;				e\EventState[0] = e\EventState[0] + (0.01*FPSfactor)
;				
;				BlurTimer = 10
;				LightFlash = 0.7
;				
;				EntityAlpha e\room\Objects[10],Min(e\EventState[0]-3,1)
;				PointEntity Camera,e\room\Objects[3],EntityRoll(Camera)
;				Animate2(e\room\Objects[6], AnimTime(e\room\Objects[6]), 1.0, 140.0, 0.31,False)
;				
;				If e\EventState[0] > 4.05 Then
;					psp\IsShowingHUD = False
;					Local r.Rooms
;					For r = Each Rooms
;						If r\RoomTemplate\Name = "room1_start" Then
;							PlayerRoom = r
;							TeleportEntity(Collider, r\x,r\y,r\z)
;							Exit
;						EndIf
;					Next
;					DropSpeed = 0
;					FallTimer = 0
;					psp\NoMove = False
;					psp\NoRotation = False
;					IsZombie = False
;					FlushKeys()
;					FlushMouse()
;					FlushJoy()
;					FreeEntity r\Objects[7]
;					SaveGame(SavePath + CurrSave\Name + "\")
;					Return
;				EndIf
;				
;			EndIf
;		Else
;			HideEntity(e\room\Objects[10])
;			
;			CameraRange(Camera, 0.01, CameraFogFar)
;			CameraFogRange (Camera, CameraFogNear, CameraFogFar)
;			CameraFogColor (Camera,200,200,200)
;			CameraRange(Camera, 0.005, 100)
;			ShowEntity Fog
;		EndIf
;		
;End Function
;~IDEal Editor Parameters:
;~C#Blitz3D