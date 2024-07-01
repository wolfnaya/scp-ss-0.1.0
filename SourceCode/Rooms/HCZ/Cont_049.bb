
Function FillRoom_Cont_049(r.Rooms)
	Local d.Doors,it.Items
	Local i,n,ne.NewElevator,em.Emitters
	
	; ~ [Chamber Gas]
	
	em.Emitters = CreateEmitter(r\x +4466.0 * RoomScale, +3100.0*RoomScale, r\z +2005* RoomScale, 0)
	EntityParent(em\Obj, r\obj) : em\Room = r
	em\Speed = 0.03
	em\SizeChange = 0.01 : em\Achange = -0.006
	em\Gravity = 0.07
	em\Speed = 0.01
	
	em.Emitters = CreateEmitter(r\x +3950.0 * RoomScale, +3100.0*RoomScale, r\z +2005* RoomScale, 0)
	EntityParent(em\Obj, r\obj) : em\Room = r
	em\Speed = 0.03
	em\SizeChange = 0.01 : em\Achange = -0.006
	em\Gravity = 0.07
	em\Speed = 0.01
	
	em.Emitters = CreateEmitter(r\x +3442.0 * RoomScale, +3100.0*RoomScale, r\z +2005* RoomScale, 0)
	EntityParent(em\Obj, r\obj) : em\Room = r
	em\Speed = 0.03
	em\SizeChange = 0.01 : em\Achange = -0.006
	em\Gravity = 0.07
	em\Speed = 0.01
	
	em.Emitters = CreateEmitter(r\x +2934.0 * RoomScale, +3100.0*RoomScale, r\z +2005* RoomScale, 0)
	EntityParent(em\Obj, r\obj) : em\Room = r
	em\Speed = 0.03
	em\SizeChange = 0.01 : em\Achange = -0.006
	em\Gravity = 0.07
	em\Speed = 0.01
	
	em.Emitters = CreateEmitter(r\x +2418.0 * RoomScale, +3100.0*RoomScale, r\z +2005* RoomScale, 0)
	EntityParent(em\Obj, r\obj) : em\Room = r
	em\Speed = 0.03
	em\SizeChange = 0.01 : em\Achange = -0.006
	em\Gravity = 0.07
	em\Speed = 0.01
	
	; ~ [Zombie Cutscene]
	
	r\Objects[0] = CreatePivot(r\obj)
	PositionEntity(r\Objects[0], r\x + 640.0 * RoomScale, 240.0 * RoomScale, r\z + 656.0 * RoomScale, True)
	
	r\Objects[1] = CreatePivot(r\obj)
	PositionEntity(r\Objects[1], r\x + 3211.0 * RoomScale, -3280.0 * RoomScale, r\z + 1824.0 * RoomScale, True)
	
	r\Objects[2] = CreatePivot(r\obj)
	PositionEntity(r\Objects[2], r\x - 672.0 * RoomScale, 240.0 * RoomScale, r\z - 93.0 * RoomScale, True)
	
	r\Objects[3] = CreatePivot(r\obj)
	PositionEntity(r\Objects[3], r\x - 2766.0 * RoomScale, -3280.0 * RoomScale, r\z - 1277.0 * RoomScale, True)
	
	; ~ [Zombies]
	
	r\Objects[4] = CreatePivot()
	PositionEntity(r\Objects[4], r\x + 4208.0 * RoomScale, r\y + 2455.0 * RoomScale, r\z + 3011.0 * RoomScale, True)
	EntityParent r\Objects[4], r\obj
	
	r\Objects[5] = CreatePivot()
	PositionEntity(r\Objects[5], r\x  + 2614.0 * RoomScale, r\y + 2743.0 * RoomScale, r\z + 1959.0 * RoomScale, True)
	EntityParent r\Objects[5], r\obj
	
	; ~ [Elevator]
	
	Local ElevatorOBJ% = LoadRMesh("GFX\map\Elevators\elevator.rmesh",Null)
	HideEntity ElevatorOBJ
	r\Objects[13] = CopyEntity(ElevatorOBJ,r\obj)
	
	RotateEntity r\Objects[13],0.0,90.0,0.0
	PositionEntity(r\Objects[13],-632,0,-293.5)
	EntityType r\Objects[13],HIT_MAP
	EntityPickMode r\Objects[13],2
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x - 632.0 * RoomScale, 0.0, r\z - 614.0 * RoomScale, 180, r, False, DOOR_ELEVATOR_3FLOOR,False,"",2)
	r\RoomDoors[0]\DisableWaypoint = True
	
	ne = CreateNewElevator(r\Objects[13],1,r\RoomDoors[0],2,r,0.0,2395.0,3500.0)
	ne\floorlocked[2] = True
	
	; ~ [Doors]
	
	d.Doors = CreateDoor(r\zone, r\x,0,r\z, 0, r, False, DOOR_HCZ, SEVERED_HAND_2)
	
	d.Doors = CreateDoor(r\zone, r\x+256*RoomScale,0,r\z-512*RoomScale, 90, r, False, DOOR_OFFICE)
	
	d.Doors = CreateDoor(r\zone, r\x+256*RoomScale,0,r\z+512*RoomScale, -90, r, False, DOOR_OFFICE)
	
	d.Doors = CreateDoor(r\zone, r\x+256*RoomScale,r\y+799*RoomScale,r\z+512*RoomScale, -90, r, False, DOOR_WINDOWED)
	d\locked = True
	
	d.Doors = CreateDoor(r\zone, r\x+256*RoomScale,r\y+1597*RoomScale,r\z+512*RoomScale, -90, r, False, DOOR_WINDOWED)
	d\locked = True
	
	d.Doors = CreateDoor(r\zone, r\x+256*RoomScale,r\y+3193*RoomScale,r\z+512*RoomScale, -90, r, False, DOOR_WINDOWED)
	
	d.Doors = CreateDoor(r\zone, r\x-1568*RoomScale,r\y+3193*RoomScale,r\z+1854*RoomScale, -90, r, False, DOOR_HCZ, KEY_CARD_2)
	
	d.Doors = CreateDoor(r\zone, r\x-2080*RoomScale,r\y+3193*RoomScale,r\z+1854*RoomScale, -90, r, False, DOOR_087B, KEY_CARD_OMNI)
	d\dir = 1
	
	d.Doors = CreateDoor(r\zone, r\x+4562*RoomScale,r\y+2395*RoomScale,r\z+3015*RoomScale, 90, r, False, DOOR_DEFAULT)
	
	d.Doors = CreateDoor(r\zone, r\x+4562*RoomScale,r\y+2521*RoomScale,r\z+995*RoomScale, 90, r, False, DOOR_DEFAULT)
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x + 256.0 * RoomScale, r\y + 2395 * RoomScale, r\z + 512.0 * RoomScale, -90, r, True, DOOR_WINDOWED)
	r\RoomDoors[2] = CreateDoor(r\zone, r\x + 10526.0 * RoomScale, r\y + 2395 * RoomScale, r\z - 4428.0 * RoomScale, 45, r, False, DOOR_WINDOWED)
	
	r\RoomDoors[3] = CreateDoor(r\zone, r\x+4722*RoomScale,r\y+2395*RoomScale,r\z+2004*RoomScale, -90, r, True, DOOR_CONTAINMENT)
	r\RoomDoors[3]\buttons[0] = FreeEntity_Strict(r\RoomDoors[3]\buttons[0])
	r\RoomDoors[3]\buttons[1] = FreeEntity_Strict(r\RoomDoors[3]\buttons[1])
	;ScaleEntity r\RoomDoors[3]\obj, 56.34*RoomScale, 59.52*RoomScale, 59.74*RoomScale
	;ScaleEntity r\RoomDoors[3]\obj2, 56.34*RoomScale, 59.52*RoomScale, 59.74*RoomScale
	
	; ~ [Levers]
	
	r\Levers[0] = CreateLever(r, r\x + 11782.0 * RoomScale, r\y + 2577.0 * RoomScale, r\z + 1999.0 * RoomScale, 270, True)
	r\Levers[1] = CreateLever(r, r\x + 11547.0 * RoomScale, r\y + 2563.0 * RoomScale, r\z - 4850.0 * RoomScale, 270)
	
	r\Levers[2] = CreateLever(r, r\x + 2504.0 * RoomScale, r\y + 2745.0 * RoomScale, r\z + 1259.0 * RoomScale, 0, True) ; ~ Door
	r\Levers[3] = CreateLever(r, r\x + 2574.0 * RoomScale, r\y + 2745.0 * RoomScale, r\z + 1259.0 * RoomScale, 0) ; ~ Gas
	
	; ~ [Items]
	
	;it = CreateItem("Document SCP-049", "paper", r\x + 4438.0 * RoomScale, r\y + 2760.0 * RoomScale, r\z + 585.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","key_4"), "key4", r\x + 3089.0 * RoomScale, r\y + 2655.0 * RoomScale, r\z + 1091.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","first_aid"), "firstaid", r\x +11370.0 * RoomScale, r\y + 2616.0 * RoomScale, r\z + 3172.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	; ~ [Pump]
	
	r\Objects[10] = CreatePivot(r\obj)
	PositionEntity r\Objects[10],r\x+11588.0*RoomScale,r\y+2395.0*RoomScale,r\z-5130.0*RoomScale,True
	
	; ~ [Spawnpoint for the NVG]
	
	r\Objects[11] = CreatePivot(r\obj)
	PositionEntity r\Objects[11],r\x-678.0*RoomScale,r\y+2438.0*RoomScale,r\z-871.0*RoomScale,True
	
	r\Objects[12] = CreatePivot(r\obj)
	PositionEntity r\Objects[12],r\x-6.0*RoomScale,r\y+2438.0*RoomScale,r\z+471.0*RoomScale,True
	
	; ~ [SCP-049 Spawn]
	
	r\Objects[14] = CreatePivot(r\obj)
	PositionEntity r\Objects[14],r\x+1981.0*RoomScale,r\y+2740.0*RoomScale,r\z-1291.0*RoomScale,True
	
	r\Objects[15] = CreatePivot(r\obj)
	PositionEntity r\Objects[15],r\x+428.0*RoomScale,r\y+2740.0*RoomScale,r\z+3765.0*RoomScale,True
	
	; ~ [Containment Point]
	
	r\Objects[6] = CreatePivot(r\obj)
	PositionEntity r\Objects[6],r\x+3372.0*RoomScale,r\y+2559.0*RoomScale,r\z+1926.0*RoomScale,True
	
End Function

Function UpdateEvent_Cont_049(e.Events)
	Local n.NPCs,it.Items
	Local temp%,x%,pvt%
	Local i, ne.NewElevator, em.Emitters
	
	If PlayerRoom = e\room Then
		
		If gopt\GameMode = GAMEMODE_NTF Then
			If TaskExists(TASK_NTF_SEARCH_049) Then
				EndTask(TASK_NTF_SEARCH_049)
				BeginTask(TASK_NTF_CONTAIN_049)
			EndIf
		EndIf
		
		For em = Each Emitters
			If EntityPitch(e\room\Levers[3]\obj,True) >= 80 Then ; ~ Gas Valves Open
				If em\emittertype = 0 Then
					em\Disable = False
					e\EventState[6] = 1
				EndIf
			ElseIf EntityPitch(e\room\Levers[3]\obj,True) <= -80 Then  ; ~ Gas Valves Closed
				If em\emittertype = 0 Then
					em\Disable = True
					e\EventState[6] = 0
				EndIf
			EndIf
		Next
		
		If EntityY(Collider) < 2395*RoomScale Then
			
			If (Not PlayerInNewElevator)
				PositionEntity e\room\RoomDoors[0]\frameobj,EntityX(e\room\RoomDoors[0]\frameobj),0,EntityZ(e\room\RoomDoors[0]\frameobj)
				PositionEntity e\room\RoomDoors[0]\obj,EntityX(e\room\RoomDoors[0]\obj),0,EntityZ(e\room\RoomDoors[0]\obj)
				PositionEntity e\room\RoomDoors[0]\obj2,EntityX(e\room\RoomDoors[0]\obj2),0,EntityZ(e\room\RoomDoors[0]\obj2)
				PositionEntity e\room\RoomDoors[0]\buttons[0],EntityX(e\room\RoomDoors[0]\buttons[0]),0.6,EntityZ(e\room\RoomDoors[0]\buttons[0])
				PositionEntity e\room\RoomDoors[0]\buttons[1],EntityX(e\room\RoomDoors[0]\buttons[1]),0.7,EntityZ(e\room\RoomDoors[0]\buttons[1])
				For ne = Each NewElevator
					If ne\door = e\room\RoomDoors[0]
						If ne\currfloor = 1 And ne\state = 0.0
							e\room\RoomDoors[0]\open = True
						Else
							e\room\RoomDoors[0]\open = False
						EndIf
					EndIf
				Next
			EndIf
			
			CameraFogRange Camera,1,6
			CameraFogColor Camera,5,20,3
			CameraClsColor Camera,5,20,3
			CameraRange Camera,0.01,7
			
		Else
			
			If (Not PlayerInNewElevator)
				PositionEntity e\room\RoomDoors[0]\frameobj,EntityX(e\room\RoomDoors[0]\frameobj),2395.0*RoomScale,EntityZ(e\room\RoomDoors[0]\frameobj)
				PositionEntity e\room\RoomDoors[0]\obj,EntityX(e\room\RoomDoors[0]\obj),2395.0*RoomScale,EntityZ(e\room\RoomDoors[0]\obj)
				PositionEntity e\room\RoomDoors[0]\obj2,EntityX(e\room\RoomDoors[0]\obj2),2395.0*RoomScale,EntityZ(e\room\RoomDoors[0]\obj2)
				PositionEntity e\room\RoomDoors[0]\buttons[0],EntityX(e\room\RoomDoors[0]\buttons[0]),2395.0*RoomScale+0.6,EntityZ(e\room\RoomDoors[0]\buttons[0])
				PositionEntity e\room\RoomDoors[0]\buttons[1],EntityX(e\room\RoomDoors[0]\buttons[1]),2395.0*RoomScale+0.7,EntityZ(e\room\RoomDoors[0]\buttons[1])
				For ne = Each NewElevator
					If ne\door = e\room\RoomDoors[0]
						If ne\currfloor = 2 And ne\state = 0.0
							e\room\RoomDoors[0]\open = True
						Else
							e\room\RoomDoors[0]\open = False
						EndIf
					EndIf
				Next
			EndIf
			
			CameraFogRange Camera, 5,30
			CameraFogColor (Camera,200,200,200)
			CameraClsColor (Camera,200,200,200)
			CameraRange(Camera, 0.005, 100)
			HideEntity Fog
			
			ShouldPlay = MUS_SAVE_ME_FROM
			
			If e\EventState[0] = 0 And (Not PlayerInNewElevator) Then
				
				n.NPCs = CreateNPC(NPC_Zombie_Armed, EntityX(e\room\Objects[4],True),EntityY(e\room\Objects[4],True),EntityZ(e\room\Objects[4],True),Guard_Zombie)
				PointEntity n\Collider, e\room\obj
				TurnEntity n\Collider, 0, 190, 0
				
				n.NPCs = CreateNPC(NPC_Zombie_Armed, EntityX(e\room\Objects[5],True),EntityY(e\room\Objects[5],True),EntityZ(e\room\Objects[5],True),Guard_Zombie)
				PointEntity n\Collider, e\room\obj
				TurnEntity n\Collider, 0, 20, 0
				
				For n.NPCs = Each NPCs
					If n\NPCtype = NPC_SCP_049
						e\room\NPC[0]=n
						e\room\NPC[0]\State[0] = 2
						e\room\NPC[0]\Idle = 1
						e\room\NPC[0]\HideFromNVG = True
						PositionEntity e\room\NPC[0]\Collider,EntityX(e\room\Objects[4],True),EntityY(e\room\Objects[4],True)+3,EntityZ(e\room\Objects[4],True)
						ResetEntity e\room\NPC[0]\Collider
						Exit
					EndIf
				Next
				If e\room\NPC[0]=Null
					n.NPCs = CreateNPC(NPC_SCP_049, EntityX(e\room\Objects[4],True), EntityY(e\room\Objects[4],True)+3, EntityZ(e\room\Objects[4],True))
					PointEntity n\Collider, e\room\obj
					n\State[0] = 2
					n\Idle = 1
					n\HideFromNVG = True
					e\room\NPC[0]=n
				EndIf
				
				e\EventState[0]=1
				
				PlaySound_Strict LoadTempSound("SFX\Room\Blackout.ogg")
				If EntityDistanceSquared(e\room\Objects[11],Collider)<EntityDistanceSquared(e\room\Objects[12],Collider) Then
					it = CreateItem(GetLocalString("Item Names","nvg"), "nvg", EntityX(e\room\Objects[11],True),EntityY(e\room\Objects[11],True),EntityZ(e\room\Objects[11],True))
					it\state = 100
					EntityType it\collider,HIT_ITEM
				Else
					it = CreateItem(GetLocalString("Item Names","nvg"), "nvg", EntityX(e\room\Objects[12],True),EntityY(e\room\Objects[12],True),EntityZ(e\room\Objects[12],True))
					it\state = 100
					EntityType it\collider,HIT_ITEM
				EndIf
			ElseIf e\EventState[0] > 0
				
				Local prevGenLever
				If EntityPitch(e\room\Levers[1]\obj,True) > 0 Then
					prevGenLever = True
				Else
					prevGenLever = False
				EndIf
				temp = Not UpdateLever(e\room\Levers[0]\obj) ;power feed
				x = UpdateLever(e\room\Levers[1]\obj) ;generator
				
				e\room\RoomDoors[0]\locked = True
				e\room\RoomDoors[1]\locked = True
				
				If EntityDistanceSquared(Collider,e\room\Objects[12]) < PowTwo(0.5) Then
					e\room\RoomDoors[1]\open = False
				EndIf
				
				If (prevGenLever <> x) Then
					If x=False Then
						PlaySound_Strict LightSFX
					Else
						PlaySound_Strict TeslaPowerUpSFX
					EndIf
				EndIf
				
				If e\EventState[0] >= 70 Then
					If x Then
						ShouldPlay = MUS_CONT_049
						e\EventState[0] = Max(e\EventState[0],70*180)
						SecondaryLightOn = CurveValue(1.0, SecondaryLightOn, 10.0)
						If e\Sound[1]=0 Then LoadEventSound(e,"SFX\Ambient\Room ambience\fuelpump.ogg",1)
						e\SoundCHN[1]=LoopSound2(e\Sound[1], e\SoundCHN[1], Camera, e\room\Objects[10], 6.0)
						
						If e\room\RoomDoors[3]\openstate = 0 Lor e\room\RoomDoors[3]\openstate = 180 And e\EventState[5] < 70*11 Then
							UpdateLever(e\room\Levers[2]\obj)
						EndIf
						
						If EntityPitch(e\room\Levers[2]\obj,True) <= -80 Then ; ~ Cont Door Open
							If e\room\RoomDoors[3]\open = True
								UseDoor(e\room\RoomDoors[3])
							EndIf
						ElseIf EntityPitch(e\room\Levers[2]\obj,True) >= 80 Then  ; ~ Cont Door Closed
							If e\room\RoomDoors[3]\open = False
								UseDoor(e\room\RoomDoors[3])
							EndIf
						EndIf
						
						UpdateLever(e\room\Levers[3]\obj)
						
						For n = Each NPCs
							If e\room\NPC[0] <> Null Then
								If e\EventState[6] = 1 And (Not e\room\RoomDoors[3]\open) Then
									If EntityDistanceSquared(e\room\NPC[0]\Collider, e\room\Objects[6])<PowTwo(15.0) Then
										e\EventState[7] = 1
									EndIf
								EndIf
								
								If e\EventState[7] = 1 Then
									e\EventState[5] = e\EventState[5] + FPSfactor
								EndIf
								
								If e\EventState[5] = 0 Then
									e\room\NPC[0]\Contained = False
									e\room\RoomDoors[3]\locked = False
								ElseIf e\EventState[5] >= 70*10 And e\EventState[5] < 70*10.1 Then
									If gopt\GameMode = GAMEMODE_NTF Then
										PlayAnnouncement("SFX\Intercom\MTF\NTF\Announc049Contain.ogg")
										SaveGame(SavePath + CurrSave\Name + "\", True)
										If TaskExists(TASK_NTF_CONTAIN_049) Then
											EndTask(TASK_NTF_CONTAIN_049)
											BeginTask(TASK_NTF_GO_TO_ZONE)
										EndIf
									Else
										PlayAnnouncement("SFX\Intercom\MTF\NTF\Announc049Contain_2.ogg")
									EndIf
								ElseIf e\EventState[5] > 70*11 Then
									e\room\NPC[0]\State[0] = SCP049_STUNNED
									e\room\NPC[0]\Contained = True
									ecst\Contained049 = True
									e\room\RoomDoors[3]\locked = True
								EndIf
								
							EndIf
						Next
						
					Else
						SecondaryLightOn = CurveValue(0.0, SecondaryLightOn, 10.0)
						If ChannelPlaying(e\SoundCHN[1]) Then
							StopChannel(e\SoundCHN[1])
						EndIf
					EndIf
				Else
					e\EventState[0] = Min(e\EventState[0]+FPSfactor,70)
				EndIf
				
				If temp And x Then
					e\room\RoomDoors[0]\locked = False
					e\room\RoomDoors[1]\locked = False : e\room\RoomDoors[1]\open = True
					
					If e\room\NPC[0]\Idle > 0
						If EntityDistanceSquared(Collider,e\room\RoomDoors[0]\frameobj)<PowTwo(3.0) Then
							i = 14
						ElseIf EntityDistanceSquared(Collider,e\room\RoomDoors[1]\frameobj)<PowTwo(3.0) Then
							i = 15
						EndIf
						If i > 0
							PositionEntity e\room\NPC[0]\Collider,EntityX(e\room\Objects[i],True),EntityY(e\room\Objects[i],True),EntityZ(e\room\Objects[i],True)
							ResetEntity e\room\NPC[0]\Collider
							PlaySound2(LoadTempSound("SFX\Room\Room2SL049Spawn.ogg"), Camera, e\room\RoomDoors[i-14]\frameobj, 4.0)
							e\room\NPC[0]\PathStatus = FindPath(e\room\NPC[0],EntityX(Collider),EntityY(Collider),EntityZ(Collider))
							If e\room\NPC[0]\Sound2 <> 0 Then FreeSound_Strict(e\room\NPC[0]\Sound2)
							e\room\NPC[0]\Sound2 = LoadSound_Strict("SFX\SCP\049\DetectedInChamber.ogg")
							e\room\NPC[0]\SoundChn2 = LoopSound2(e\room\NPC[0]\Sound2,e\room\NPC[0]\SoundChn2,Camera,e\room\NPC[0]\obj)
							e\room\NPC[0]\Idle = 0
							e\room\NPC[0]\HideFromNVG = False
							e\room\NPC[0]\PrevState = 2
							e\room\NPC[0]\State[0] = 2
						EndIf
					EndIf
				EndIf
				
				If e\EventState[0] < 70*190 Then
					If e\EventState[0] >= 70*180 Then
						e\EventState[0]= 70*190
					EndIf
				ElseIf e\EventState[0] < 70*240
					For n.NPCs = Each NPCs ;awake the zombies
						If n\NPCtype = NPC_Zombie_Armed And n\State[0] = 0 Then
							n\State[0] = 1
							SetNPCFrame(n, 155)
						EndIf
					Next
					e\EventState[0]=70*241
				EndIf
			EndIf
		EndIf
	EndIf 
	
	If e\EventState[0] < 0 Then
		If e\EventState[0] > -70*4 Then
			I_008\Timer = 0
			If FallTimer => 0 Then 
				FallTimer = Min(-1, FallTimer)
				PositionEntity(Head, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True), True)
				ResetEntity (Head)
				RotateEntity(Head, 0, EntityYaw(Camera) + Rand(-45, 45), 0)
			ElseIf FallTimer < -230
				FallTimer = -231
				BlinkTimer = 0
				e\EventState[0] = e\EventState[0]-FPSfactor
				
				If e\EventState[0] =< -70*4 Then 
					d_I\UpdateDoorsTimer = 0
					UpdateDoors()
					UpdateRooms()
					ShowEntity Collider
					DropSpeed = 0
					BlinkTimer = -10
					FallTimer = 0
					PositionEntity Collider, EntityX(e\room\obj,True), EntityY(e\room\Objects[5],True)+0.2, EntityZ(e\room\obj,True)
					ResetEntity Collider
					
					PositionEntity e\room\NPC[0]\Collider, EntityX(e\room\Objects[0],True),EntityY(e\room\Objects[0],True),EntityZ(e\room\Objects[0],True),True
					ResetEntity e\room\NPC[0]\Collider
					
					For n.NPCs = Each NPCs
						If n\NPCtype = NPC_Zombie_Armed Then
							PositionEntity n\Collider, EntityX(e\room\Objects[4],True),EntityY(e\room\Objects[4],True),EntityZ(e\room\Objects[4],True),True
							ResetEntity n\Collider
							n\State[0] = 4
							;debuglog "moving zombie"
						EndIf
					Next
					
					n.NPCs = CreateNPC(NPC_NTF, EntityX(e\room\Objects[5],True), EntityY(e\room\Objects[5],True)+0.2, EntityZ(e\room\Objects[5],True))
					n\State[0] = MTF_CONTACT
					n\Reload = 6*70
					PointEntity n\Collider,Collider
					e\room\NPC[1] = n
					
					n.NPCs = CreateNPC(NPC_NTF, EntityX(e\room\Objects[5],True), EntityY(e\room\Objects[5],True)+0.2, EntityZ(e\room\Objects[5],True))
					n\State[0] = MTF_CONTACT
					n\Reload = (6*70)+Rnd(15,30)
					RotateEntity n\Collider,0,EntityYaw(e\room\NPC[1]\Collider),0
					MoveEntity n\Collider,0.5,0,0
					PointEntity n\Collider,Collider
					
					n.NPCs = CreateNPC(NPC_NTF, EntityX(e\room\Objects[5],True), EntityY(e\room\Objects[5],True)+0.2, EntityZ(e\room\Objects[5],True))
					n\State[0] = MTF_CONTACT
					n\Reload = 6*70+Rnd(15,30)
					RotateEntity n\Collider,0,EntityYaw(e\room\NPC[1]\Collider),0
					n\State[1] = EntityYaw(n\Collider)
					MoveEntity n\Collider,-0.65,0,0
					
					MoveEntity e\room\NPC[1]\Collider,0,0,0.1
					PointEntity Collider, e\room\NPC[1]\Collider
					
					PlaySound_Strict LoadTempSound("SFX\Character\MTF\049\Player0492_1.ogg")
					
					LoadEventSound(e,"SFX\SCP\049\0492Breath.ogg")
					
					HolsterGun(); ~ Test
					
					IsZombie = True
				EndIf
			EndIf
		Else
			BlurTimer = 800
			ForceMove = 0.25
			I_008\Timer = 0
			
			pvt% = CreatePivot()
			PositionEntity pvt%,EntityX(e\room\NPC[1]\Collider),EntityY(e\room\NPC[1]\Collider)+0.5,EntityZ(e\room\NPC[1]\Collider)
			
			PointEntity Collider, e\room\NPC[1]\Collider
			PointEntity Camera, pvt%,EntityRoll(Camera)
			
			FreeEntity pvt%
			
			If KillTimer < 0 Then
				PlaySound_Strict LoadTempSound("SFX\Character\MTF\049\Player0492_2.ogg")
				RemoveEvent(e)
			Else
				If e\SoundCHN[0] = 0 Then
					e\SoundCHN[0] = PlaySound_Strict (e\Sound[0])
				Else
					If (Not ChannelPlaying(e\SoundCHN[0])) Then e\SoundCHN[0] = PlaySound_Strict(e\Sound[0])
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D