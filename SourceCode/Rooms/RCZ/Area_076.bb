
Function FillRoom_Area_076_Entrance(r.Rooms)
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x + 281.0 * RoomScale, 0.0, r\z + 6.0 * RoomScale, 90, r, False, False, False, "ABCD")
	r\RoomDoors[0]\locked = True
	
	r\Objects[0] = CreatePivot()
	PositionEntity r\Objects[0],r\x+217*RoomScale,r\y,r\z+166*RoomScale,True
	EntityParent r\Objects[0], r\obj
	
	CreateDarkSprite(r, 1)
	
End Function

Function UpdateEvent_Area_076_Entrance(e.Events)
	Local i%, tex%
	Local ne.NewElevator,r.Rooms,e2.Events
	Local playerElev%,prevZone%
	
	If cpt\Current > 4 And cpt\Current < 8 Then
		If e\room\NPC[0] = Null Then
			e\room\NPC[0] = CreateNPC(NPC_Vincent,EntityX(e\room\Objects[0],True),EntityY(e\room\Objects[0],True)+0.2,EntityZ(e\room\Objects[0],True))
			RotateEntity(e\room\NPC[0]\Collider,0,e\room\angle-45,0)
		EndIf
	EndIf
	
	If PlayerRoom = e\room Then
		
		If TaskExists(TASK_FIND_AREA_076) Then
			EndTask(TASK_FIND_AREA_076)
		EndIf
		
		; ~ Entering to SCP-076 Areas
		
		If ecst\WasInO5 Then
			
			If (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then
				If (Not ecst\WasIn076) Then
					
					If e\room\NPC[0] <> Null Then
						
						HolsterGun()
						
						PointEntity(Camera,e\room\NPC[0]\Collider)
					EndIf
					
					If e\room\NPC[0] <> Null Then
						e\room\NPC[0]\State[0] = 1
					EndIf
					
					If e\EventState[0] > 0 And e\EventState[0] < 70*0.02 Then
						PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\goto076")
					EndIf
					
					If ecst\WasInO5 Then
						e\EventState[0] = e\EventState[0] + FPSfactor
					EndIf
					
					If e\EventState[0] > 70*12 Then
						e\EventState[1] = e\EventState[1] + (0.01*FPSfactor)
						EntityAlpha e\room\Objects[1],Min(e\EventState[1],1.0)
					EndIf
					
					If e\EventState[1] > 1.05 Then
						SaveGame(SavePath + CurrSave\Name + "\", True)
						
						gopt\CurrZone = AREA_076
						
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
								e2\EventState[4] = 0
								Exit
							EndIf
						Next
						SaveGame(SavePath + CurrSave\Name + "\", True)
						Return
					EndIf
					
				EndIf
			EndIf
		EndIf
		
		; ~ Event After SCP-076 Areas
		
		If ecst\WasIn076 Then
			
			If e\EventState[2] = 0 Then
				EntityAlpha e\room\Objects[1],1
				e\EventState[2] = 1
			EndIf
			
			e\EventState[3] = e\EventState[3] - FPSfactor
			EntityAlpha e\room\Objects[1],Min(1,e\EventState[3]+3)
			
			psp\NoMove = False
			psp\NoRotation = False
			CanPlayerUseGuns% = True
			
			If (Not TaskExists(TASK_COME_BACK_TO_JANITOR)) Then
				BeginTask(TASK_COME_BACK_TO_JANITOR)
			EndIf
			If TaskExists(TASK_COME_BACK_TO_JANITOR) Then
				cpt\Current = 6
			EndIf
			
		EndIf
		
	EndIf
	
End Function

Function FillRoom_Area_076(r.Rooms)
	Local it.Items,de.Decals,ne.NewElevator
	
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x + 2640.0 * RoomScale, -2496.0 * RoomScale, r\z + 400.0 * RoomScale)
	EntityParent(r\Objects[0], r\obj)
	
	r\Objects[1] = CreatePivot()
	PositionEntity(r\Objects[1], r\x - 4336.0 * RoomScale, -2496.0 * RoomScale, r\z - 2512.0 * RoomScale)
	EntityParent(r\Objects[1], r\obj)
	
	r\Objects[2] = CreatePivot()
	RotateEntity r\Objects[2],0.0,180.0,0.0,True
	PositionEntity(r\Objects[2], r\x + 552.0 * RoomScale, 240.0 * RoomScale, r\z + 656.0 * RoomScale)
	EntityParent(r\Objects[2], r\obj)
	
	r\Objects[4] = CreatePivot()
	PositionEntity(r\Objects[4], r\x - 552.0 * RoomScale, 240.0 * RoomScale, r\z - 656.0 * RoomScale)
	EntityParent(r\Objects[4], r\obj)
	
	r\Objects[6] = CreatePivot()
	PositionEntity(r\Objects[6], r\x - 3.0 * RoomScale, 0.0 * RoomScale, r\z - 932.0 * RoomScale, True)
	EntityParent(r\Objects[6], r\obj)
	
	CreateDarkSprite(r, 7)
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x + 244.0 * RoomScale, 0.0, r\z + 656.0 * RoomScale, 90, r, True, DOOR_ELEVATOR_3FLOOR,False,"",1)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = True
	
	Local ElevatorObj = LoadRMesh("GFX\Map\elevators\elevator.rmesh",Null)
	HideEntity ElevatorObj
	r\Objects[8] = CopyEntity(ElevatorObj,r\obj)
	
	PositionEntity(r\Objects[8],560, 0, 656)
	RotateEntity r\Objects[8],0,0,0
	EntityType r\Objects[8],HIT_MAP
	EntityPickMode r\Objects[8],2
	
	ne = CreateNewElevator(r\Objects[8],2,r\RoomDoors[0],1,r,-2020,0,2000)
	
	r\Objects[9] = CreatePivot()
	PositionEntity(r\Objects[9], r\x +2667 * RoomScale, r\y-2111 * RoomScale, r\z + 6939 * RoomScale, True)
	EntityParent(r\Objects[9], r\obj)
	
	r\Objects[10] = CreatePivot()
	PositionEntity r\Objects[10],r\x+1784 * RoomScale,r\y -2174 * RoomScale,r\z+ 5224 * RoomScale
	EntityParent(r\Objects[10], r\obj)
	
	r\RoomDoors[4] = CreateDoor(r\zone, r\x, 0.0, r\z + 1024.0 * RoomScale, 0, r, False)
	r\RoomDoors[4]\AutoClose = False
	
	r\RoomDoors[5] = CreateDoor(r\zone, r\x, 0.0, r\z + 2337.0 * RoomScale, 0, r, False, DOOR_HCZ, KEY_CARD_3)
	r\RoomDoors[5]\AutoClose = False : r\RoomDoors[5]\dir = 1
	PositionEntity(r\RoomDoors[5]\buttons[0], r\x - 165.0 * RoomScale, r\y+170*RoomScale, r\z + 2304.0 * RoomScale, True)
	
	it = CreateItem("FN P90", "p90", r\x -165.0 * RoomScale, r\y +100.0 * RoomScale, r\z +3358.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	it\state2 = 150 : it\state = 50
	
	it = CreateItem("Colt M4A1", "m4a1", r\x -494.0 * RoomScale, r\y +140.0 * RoomScale, r\z +3418.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	it\state2 = 60 : it\state = 20
	
	; TODO ATTS!!!
	;it = CreateItem(GetLocalString("Item Names","laser"), "lasersight", r\x -867.0 * RoomScale, r\y +130.0 * RoomScale, r\z +3086.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	it = CreateItem("H&K MP7", "mp7", r\x -140.0 * RoomScale, r\y +100.0 * RoomScale, r\z +3098.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	it\state2 = 60 : it\state = 30
	
	it = CreateItem("FN Five-Seven", "fiveseven", r\x -678.0 * RoomScale, r\y +155.0 * RoomScale, r\z +2661.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	it\state2 = 45 : it\state = 15
	
	it = CreateItem("H&K USP", "usp", r\x -69.0 * RoomScale, r\y +210.0 * RoomScale, r\z +2655.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	it\state2 = 36  : it\state = 12
	
	it = CreateItem("H&K MP5", "mp5", r\x +541.0 * RoomScale, r\y +210.0 * RoomScale, r\z +2653.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	it\state2 = 90  : it\state = 30
	
	it = CreateItem(GetLocalString("Item Names","acog"), "acog", r\x -548.0 * RoomScale, r\y +155.0 * RoomScale, r\z +2609.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Laboratory Document", "paper", r\x +1883.0 * RoomScale, r\y -2150.0 * RoomScale, r\z +4690.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","key_cave_2"), "key_cave2", r\x +2309.0 * RoomScale, r\y -2011.0 * RoomScale, r\z +5960.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	Local dx#,dz#,i%
	For i = 0 To 14
		Select i
			Case 0
				dx# = 2031
				dz# = 4472
			Case 1
				dx# = 2217
				dz# = 4560
			Case 2
				dx# = 2641
				dz# = 4563
			Case 3
				dx# = 2956
				dz# = 4546
			Case 4
				dx# = 3351
				dz# = 4458
			Case 5
				dx# = 3755
				dz# = 4415
			Case 6
				dx# = 3992
				dz# = 4590
			Case 7
				dx# = 4207
				dz# = 4845
			Case 8
				dx# = 4237
				dz# = 5287
			Case 9
				dx# = 4124
				dz# = 5588
			Case 10
				dx# = 3775
				dz# = 5651
			Case 11
				dx# = 3398
				dz# = 5616
			Case 12
				dx# = 3117
				dz# = 5689
			Case 13
				dx# = 2622
				dz# = 5753
			Case 14
				dx# = 2420
				dz# = 5942
		End Select
		de.Decals = CreateDecal(GetRandomDecalID(DECAL_TYPE_BLOODDROP),r\x+dx#*RoomScale,r\y-2174*RoomScale+0.005,r\z+dz#*RoomScale,90,Rand(360),0)
		If i > 10 Then
			de\Size = Rnd(0.3,0.4)
		Else
			de\Size = Rnd(0.2,0.3)
		EndIf
		EntityAlpha(de\obj, 1.0) : ScaleSprite(de\obj,de\Size,de\Size)
		EntityParent de\obj, r\obj
	Next
	
End Function

Function UpdateEvent_Area_076(e.Events)
	Local dr.Doors,item%,e2.Events,ne.NewElevator,g.Guns
	
	If PlayerRoom = e\room
		
		If EntityY(Collider) > -1200*RoomScale Then
			
			If (Not PlayerInNewElevator)
				PositionEntity e\room\RoomDoors[0]\frameobj,EntityX(e\room\RoomDoors[0]\frameobj),0,EntityZ(e\room\RoomDoors[0]\frameobj)
				PositionEntity e\room\RoomDoors[0]\obj,EntityX(e\room\RoomDoors[0]\obj),0,EntityZ(e\room\RoomDoors[0]\obj)
				PositionEntity e\room\RoomDoors[0]\obj2,EntityX(e\room\RoomDoors[0]\obj2),0,EntityZ(e\room\RoomDoors[0]\obj2)
				PositionEntity e\room\RoomDoors[0]\buttons[0],EntityX(e\room\RoomDoors[0]\buttons[0]),0.6,EntityZ(e\room\RoomDoors[0]\buttons[0])
				PositionEntity e\room\RoomDoors[0]\buttons[1],EntityX(e\room\RoomDoors[0]\buttons[1]),0.7,EntityZ(e\room\RoomDoors[0]\buttons[1])
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
			
		Else
			
			If (Not PlayerInNewElevator)
				PositionEntity e\room\RoomDoors[0]\frameobj,EntityX(e\room\RoomDoors[0]\frameobj),-2020.0*RoomScale,EntityZ(e\room\RoomDoors[0]\frameobj)
				PositionEntity e\room\RoomDoors[0]\obj,EntityX(e\room\RoomDoors[0]\obj),-2020.0*RoomScale,EntityZ(e\room\RoomDoors[0]\obj)
				PositionEntity e\room\RoomDoors[0]\obj2,EntityX(e\room\RoomDoors[0]\obj2),-2020.0*RoomScale,EntityZ(e\room\RoomDoors[0]\obj2)
				PositionEntity e\room\RoomDoors[0]\buttons[0],EntityX(e\room\RoomDoors[0]\buttons[0]),-2020.0*RoomScale+0.6,EntityZ(e\room\RoomDoors[0]\buttons[0])
				PositionEntity e\room\RoomDoors[0]\buttons[1],EntityX(e\room\RoomDoors[0]\buttons[1]),-2020.0*RoomScale+0.7,EntityZ(e\room\RoomDoors[0]\buttons[1])
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
			
		EndIf
			
		CanPlayerUseGuns = True
		psp\NoMove = False
		psp\NoRotation = False
		IsZombie = False
		UnableToMove = False
		
		For g = Each Guns
			
			If EntityDistanceSquared(Collider,e\room\Objects[10]) < PowTwo(0.8) Then
				If g_I\HoldingGun > GUN_UNARMED And g_I\HoldingGun <> 1 And g_I\HoldingGun <> 2 And g_I\HoldingGun <> 17 Then
					If g_I\HoldingGun = g\ID Then
						If g\CurrReloadAmmo < g\MaxReloadAmmo Then
							DrawHandIcon = True
							If KeyHitUse Then
								PlaySound_Strict(LoadTempSound("SFX\General\ammo_crate.ogg"))
								g\CurrReloadAmmo = g\MaxReloadAmmo
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
			
		Next
		
		Local i
		
;		If e\room\NPC[0] <> Null Then
;			For ne = Each NewElevator
;				For i = 0 To 2
;					If e\room\NPC[0]\HP >= 1 Then
;						ne\floorlocked[i] = True
;					Else
;						ne\floorlocked[i] = False
;					EndIf
;				Next
;				ne\floorlocked[2] = True
;			Next
;		EndIf
		
		If e\EventState[3] = 0 Then
			
			If e\room\NPC[1] = Null Then
				e\room\NPC[1]=CreateNPC(NPC_Vincent,EntityX(e\room\Objects[6],True),EntityY(e\room\Objects[6],True)+0.5,EntityZ(e\room\Objects[6],True))
				e\room\NPC[1]\State[0] = 2
				RotateEntity(e\room\NPC[1]\Collider,0,e\room\angle-115,0)
				
				StartChapter("chapter_6", 6)
			EndIf
			
			If (Not TaskExists(TASK_FIND_KEY_IN_076)) Then
				BeginTask(TASK_FIND_KEY_IN_076)
			EndIf
			
			For item = 0 To MaxItemAmount-1
				If Inventory[item] <> Null Then
					If Inventory[item]\itemtemplate\tempname = "key_cave2" Then
						e\EventState[3] = 1
						SaveGame(SavePath + CurrSave\Name + "\", True)
						Exit
					EndIf
				EndIf
			Next
			
		EndIf
		
		If e\EventState[3] = 1 Then
			e\room\NPC[0]=CreateNPC(NPC_SCP_076,EntityX(e\room\Objects[9],True),EntityY(e\room\Objects[9],True)+0.5,EntityZ(e\room\Objects[9],True))
			e\room\NPC[0]\State[0] = SCP076_EVENT_WANDER
			If TaskExists(TASK_FIND_KEY_IN_076) Then
				FailTask(TASK_FIND_KEY_IN_076)
			EndIf
			e\EventState[3] = 2
		EndIf
		
		If e\EventState[3] <> 3 Then
			ShouldPlay = MUS_CONT_076
		Else
			ShouldPlay = MUS_CONT_076_2
		EndIf
		
		If e\room\NPC[0] <> Null Then
			If e\room\NPC[0]\HP < 1 Then
				If (Not TaskExists(TASK_COME_BACK_TO_GUARD_3)) Then
					;psp\Karma = psp\Karma + 5
					BeginTask(TASK_COME_BACK_TO_GUARD_3)
				EndIf
				e\EventState[3] = 3
			EndIf 
		EndIf
		
		If e\EventState[3] = 4 Then
			
			e\EventState[4] = e\EventState[4] + (0.01*FPSfactor)
			EntityAlpha e\room\Objects[7],Min(e\EventState[4],1.0)
			
			If e\EventState[4] > 2.05 Then
				
				ecst\WasIn076 = True
				ecst\UnlockedEMRP = True
				
				SaveGame(SavePath + CurrSave\Name + "\", True)
				
				gopt\CurrZone = RCZ
				
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
						e2\EventState[1] = 0
						TeleportEntity(Collider, EntityX(e2\room\obj), EntityY(e2\room\obj) + 1.0, EntityZ(e2\room\obj), 0.3, True)
						RotateEntity(Collider, 0, e2\room\angle, 0)
						Exit
					EndIf
				Next
				SaveGame(SavePath + CurrSave\Name + "\", True)
				Return
			EndIf
			
		EndIf
		
		If e\EventState[3] = 3 Then
			If e\room\NPC[0] = Null Then 
				If EntityDistanceSquared(Collider,e\room\NPC[1]\Collider)<PowTwo(1.5) Then
					DrawHandIcon = True
					If KeyHitUse Then
						
						PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\timetogo")
						
						e\EventState[3] = 4
						
						If TaskExists(TASK_COME_BACK_TO_GUARD_3) Then
							EndTask(TASK_COME_BACK_TO_GUARD_3)
						EndIf
						
					EndIf
				EndIf
			EndIf
		EndIf
		
		Curr173\Idle = 3
		HideEntity Curr173\obj
		HideEntity Curr173\obj2
		HideEntity Curr173\Collider
		
		Curr106\Idle = True
		
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS