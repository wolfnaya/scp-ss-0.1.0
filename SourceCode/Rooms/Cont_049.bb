
Function FillRoom_Cont_049(r.Rooms)
	Local d.Doors,it.Items
	Local i,n
	
	r\Objects[0] = CreatePivot(r\obj)
	PositionEntity(r\Objects[0], r\x + 640.0 * RoomScale, 240.0 * RoomScale, r\z + 656.0 * RoomScale, True)
	
	r\Objects[1] = CreatePivot(r\obj)
	PositionEntity(r\Objects[1], r\x + 3211.0 * RoomScale, -3280.0 * RoomScale, r\z + 1824.0 * RoomScale, True)
	
	r\Objects[2] = CreatePivot(r\obj)
	PositionEntity(r\Objects[2], r\x - 672.0 * RoomScale, 240.0 * RoomScale, r\z - 93.0 * RoomScale, True)
	
	r\Objects[3] = CreatePivot(r\obj)
	PositionEntity(r\Objects[3], r\x - 2766.0 * RoomScale, -3280.0 * RoomScale, r\z - 1277.0 * RoomScale, True)
	
	;zombie 1
	r\Objects[4] = CreatePivot(r\obj)
	PositionEntity(r\Objects[4], r\x + 528.0 * RoomScale, -3440.0 * RoomScale, r\z + 96.0 * RoomScale, True)
	;zombie 2
	r\Objects[5] = CreatePivot(r\obj)
	PositionEntity(r\Objects[5], r\x  + 64.0 * RoomScale, -3440.0 * RoomScale, r\z - 1000.0 * RoomScale, True)
	
	r\Levers[0] = CreateLever(r, r\x + 852.0 * RoomScale, r\y - 3374.0 * RoomScale, r\z - 854.0 * RoomScale, 270, True)
	r\Levers[1] = CreateLever(r, r\x - 834.0 * RoomScale, r\y - 3400.0 * RoomScale, r\z + 1093.0 * RoomScale, 180)
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x + 330.0 * RoomScale, 0.0, r\z + 656.0 * RoomScale, 90, r, True, 3)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = True
	PositionEntity(r\RoomDoors[0]\buttons[1], r\x + 288.0 * RoomScale, 0.7, r\z + 512.0 * RoomScale, True)
	PositionEntity(r\RoomDoors[0]\buttons[0], r\x + 368.0 * RoomScale, 0.7, r\z + 840.0 * RoomScale, True)
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x + 2898.0 * RoomScale, -3520.0 * RoomScale, r\z + 1824.0 * RoomScale, 90, r, False, 3)
	r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False	
	PositionEntity(r\RoomDoors[1]\buttons[1], r\x + 2881.0 * RoomScale, EntityY(r\RoomDoors[1]\buttons[1],True), r\z + 1663.0 * RoomScale, True)
	PositionEntity(r\RoomDoors[1]\buttons[0], r\x + 2936.0 * RoomScale, EntityY(r\RoomDoors[1]\buttons[0],True), r\z + 2009.0 * RoomScale, True)				
	
	r\RoomDoors[2] = CreateDoor(r\zone, r\x - 672.0 * RoomScale, 0.0, r\z - 408.0 * RoomScale, 0, r, True, 3)
	r\RoomDoors[2]\AutoClose = False : r\RoomDoors[2]\open = True
	PositionEntity(r\RoomDoors[2]\buttons[0], r\x - 487.0 * RoomScale, 0.7, r\z - 447.0 * RoomScale, True)
	PositionEntity(r\RoomDoors[2]\buttons[1], r\x - 857.0 * RoomScale, 0.7, r\z - 369.0 * RoomScale, True)				
	
	r\RoomDoors[3] = CreateDoor(r\zone, r\x - 2766.0 * RoomScale, -3520.0 * RoomScale, r\z - 1592.0 * RoomScale, 0, r, False, 3)
	r\RoomDoors[3]\AutoClose = False : r\RoomDoors[3]\open = False		
	PositionEntity(r\RoomDoors[3]\buttons[0], r\x - 2581.0 * RoomScale, EntityY(r\RoomDoors[3]\buttons[0],True), r\z - 1631.0 * RoomScale, True)
	PositionEntity(r\RoomDoors[3]\buttons[1], r\x - 2951.0 * RoomScale, EntityY(r\RoomDoors[3]\buttons[1],True), r\z - 1553.0 * RoomScale, True)	
	
	;storage room doors
	r\RoomDoors[4] = CreateDoor(r\zone, r\x + 272.0 * RoomScale, -3552.0 * RoomScale, r\z + 104.0 * RoomScale, 90, r, False)
	r\RoomDoors[4]\AutoClose = False : r\RoomDoors[4]\open = True : r\RoomDoors[4]\locked = True
	r\RoomDoors[5] = CreateDoor(r\zone, r\x + 264.0 * RoomScale, -3520.0 * RoomScale, r\z - 1824.0 * RoomScale, 90, r, False)
	r\RoomDoors[5]\AutoClose = False : r\RoomDoors[5]\open = True : r\RoomDoors[5]\locked = True
	r\RoomDoors[6] = CreateDoor(r\zone, r\x - 264.0 * RoomScale, -3520.0 * RoomScale, r\z + 1824.0 * RoomScale, 90, r, False)
	r\RoomDoors[6]\AutoClose = False : r\RoomDoors[6]\open = True : r\RoomDoors[6]\locked = True
	
	d.Doors = CreateDoor(0, r\x,0,r\z, 0, r, False, 2, -2)
	
	it = CreateItem("Document SCP-049", "paper", r\x - 608.0 * RoomScale, r\y - 3332.0 * RoomScale, r\z + 876.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Level 4 Key Card", "key4", r\x - 512.0 * RoomScale, r\y - 3412.0 * RoomScale, r\z + 864.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("First Aid Kit", "firstaid", r\x +385.0 * RoomScale, r\y - 3412.0 * RoomScale, r\z + 271.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	d = CreateDoor(r\zone,r\x-272.0*RoomScale,r\y-3552.0*RoomScale,r\z+98.0*RoomScale,90,r,True,True)
	d\AutoClose = False : d\open = True : d\MTFClose = False : d\locked = True
	For i = 0 To 1
		d\buttons[i] = FreeEntity_Strict(d\buttons[i])
	Next
	
	d = CreateDoor(r\zone,r\x-2990.0*RoomScale,r\y-3520.0*RoomScale,r\z-1824.0*RoomScale,90,r,False,2)
	d\locked = True : d\DisableWaypoint = True
	d = CreateDoor(r\zone,r\x-896.0*RoomScale,r\y,r\z-640*RoomScale,90,r,False,2)
	d\locked = True : d\DisableWaypoint = True
	
	r\Objects[10] = CreatePivot(r\obj)
	PositionEntity r\Objects[10],r\x-832.0*RoomScale,r\y-3484.0*RoomScale,r\z+1572.0*RoomScale,True
	
	;Spawnpoint for the map layout document
	r\Objects[11] = CreatePivot(r\obj)
	PositionEntity r\Objects[11],r\x+2642.0*RoomScale,r\y-3516.0*RoomScale,r\z+1822.0*RoomScale,True
	r\Objects[12] = CreatePivot(r\obj)
	PositionEntity r\Objects[12],r\x-2666.0*RoomScale,r\y-3516.0*RoomScale,r\z-1792.0*RoomScale,True
	
End Function

Function UpdateEvent_Cont_049(e.Events)
	Local n.NPCs,it.Items
	Local temp%,x%,pvt%
	Local i
	
	If PlayerRoom = e\room Then
		If EntityY(Collider) > -2848*RoomScale Then
			e\EventState[1] = UpdateElevators(e\EventState[1], e\room\RoomDoors[0], e\room\RoomDoors[1],e\room\Objects[0],e\room\Objects[1], e)
			e\EventState[2] = UpdateElevators(e\EventState[2], e\room\RoomDoors[2], e\room\RoomDoors[3],e\room\Objects[2],e\room\Objects[3], e)
		Else
			
			ShouldPlay = 39
			
			If e\eventstate[0] = 0 Then
				If e\EventStr = "" And QuickLoadPercent = -1
					QuickLoadPercent = 0
					QuickLoad_CurrEvent = e
					e\EventStr = "load0"
				EndIf
				PlaySound_Strict LoadTempSound("SFX\Room\Blackout.ogg")
				If EntityDistanceSquared(e\room\Objects[11],Collider)<EntityDistanceSquared(e\room\Objects[12],Collider) Then
					it = CreateItem("Research Sector-02 Scheme", "paper", EntityX(e\room\Objects[11],True),EntityY(e\room\Objects[11],True),EntityZ(e\room\Objects[11],True))
					EntityType it\collider,HIT_ITEM
				Else
					it = CreateItem("Research Sector-02 Scheme", "paper", EntityX(e\room\Objects[12],True),EntityY(e\room\Objects[12],True),EntityZ(e\room\Objects[12],True))
					EntityType it\collider,HIT_ITEM
				EndIf
			ElseIf e\eventstate[0] > 0
				
				Local prevGenLever
				If EntityPitch(e\room\Levers[1]\obj,True) > 0 Then
					prevGenLever = True
				Else
					prevGenLever = False
				EndIf
				temp = Not UpdateLever(e\room\Levers[0]\obj) ;power feed
				x = UpdateLever(e\room\Levers[1]\obj) ;generator
				
				e\room\RoomDoors[1]\locked = True
				e\room\RoomDoors[3]\locked = True
				e\room\RoomDoors[1]\IsElevatorDoor = 0
				e\room\RoomDoors[3]\IsElevatorDoor = 0
				
				If (prevGenLever <> x) Then
					If x=False Then
						PlaySound_Strict LightSFX
					Else
						PlaySound_Strict TeslaPowerUpSFX
					EndIf
				EndIf
				
				If e\EventState[0] >= 70 Then
					If x Then
						ShouldPlay = 8
						e\EventState[0] = Max(e\EventState[0],70*180)
						SecondaryLightOn = CurveValue(1.0, SecondaryLightOn, 10.0)
						If e\Sound[1]=0 Then LoadEventSound(e,"SFX\Ambient\Room ambience\fuelpump.ogg",1)
						e\SoundCHN[1]=LoopSound2(e\Sound[1], e\SoundCHN[1], Camera, e\room\Objects[10], 6.0)
						For i = 4 To 6
							e\room\RoomDoors[i]\locked = False
						Next
					Else
						SecondaryLightOn = CurveValue(0.0, SecondaryLightOn, 10.0)
						If ChannelPlaying(e\SoundCHN[1]) Then
							StopChannel(e\SoundCHN[1])
						EndIf
						For i = 4 To 6
							e\room\RoomDoors[i]\locked = True
						Next
					EndIf
				Else
					e\EventState[0] = Min(e\EventState[0]+FPSfactor,70)
				EndIf
				
				If temp And x Then
					e\room\RoomDoors[1]\locked = False
					e\room\RoomDoors[3]\locked = False
					e\EventState[1] = UpdateElevators(e\EventState[1], e\room\RoomDoors[0], e\room\RoomDoors[1],e\room\Objects[0],e\room\Objects[1], e)
					e\EventState[2] = UpdateElevators(e\EventState[2], e\room\RoomDoors[2], e\room\RoomDoors[3],e\room\Objects[2],e\room\Objects[3], e)
					
					If e\room\NPC[0]\Idle > 0
						i = 0
						If EntityDistanceSquared(Collider,e\room\RoomDoors[1]\frameobj)<PowTwo(3.0)
							i = 1
						ElseIf EntityDistanceSquared(Collider,e\room\RoomDoors[3]\frameobj)<PowTwo(3.0)
							i = 3
						EndIf
						If i > 0
							PositionEntity e\room\NPC[0]\Collider,EntityX(e\room\Objects[i],True),EntityY(e\room\Objects[i],True),EntityZ(e\room\Objects[i],True)
							ResetEntity e\room\NPC[0]\Collider
							PlaySound2(ElevatorBeepSFX, Camera, e\room\Objects[i], 4.0)
							e\room\RoomDoors[i]\locked = False
							UseDoor(e\room\RoomDoors[i],False,True)
							e\room\RoomDoors[i-1]\open = False
							e\room\RoomDoors[i]\open = True
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
						e\room\RoomDoors[1]\open = False
						e\room\RoomDoors[3]\open = False
						e\room\RoomDoors[0]\open = True
						e\room\RoomDoors[2]\open = True
						
						e\EventState[0]= 70*190
					EndIf
				ElseIf e\EventState[0] < 70*240
					For n.NPCs = Each NPCs ;awake the zombies
						If n\NPCtype = NPCtype049_2 And n\State[0] = 0 Then
							n\State[0] = 1
							SetNPCFrame(n, 155)
						EndIf
					Next
					e\EventState[0]=70*241
				EndIf
			EndIf
		EndIf
	Else
		e\EventState[1] = UpdateElevators(e\EventState[1], e\room\RoomDoors[0], e\room\RoomDoors[1],e\room\Objects[0],e\room\Objects[1], e)
		e\EventState[2] = UpdateElevators(e\EventState[2], e\room\RoomDoors[2], e\room\RoomDoors[3],e\room\Objects[2],e\room\Objects[3], e)
	EndIf 
	
	If e\EventState[0] < 0 Then
		If e\EventState[0] > -70*4 Then
			Infect = 0
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
					IsCutscene = True
					BlinkTimer = -10
					IsCutscene = False
					FallTimer = 0
					PositionEntity Collider, EntityX(e\room\obj,True), EntityY(e\room\Objects[5],True)+0.2, EntityZ(e\room\obj,True)
					ResetEntity Collider										
					
					PositionEntity e\room\NPC[0]\Collider, EntityX(e\room\Objects[0],True),EntityY(e\room\Objects[0],True),EntityZ(e\room\Objects[0],True),True
					ResetEntity e\room\NPC[0]\Collider
					
					For n.NPCs = Each NPCs
						If n\NPCtype = NPCtype049_2 Then
							PositionEntity n\Collider, EntityX(e\room\Objects[4],True),EntityY(e\room\Objects[4],True),EntityZ(e\room\Objects[4],True),True
							ResetEntity n\Collider
							n\State[0] = 4
							DebugLog "moving zombie"
						EndIf
					Next
					
					n.NPCs = CreateNPC(NPCtypeNTF, EntityX(e\room\Objects[5],True), EntityY(e\room\Objects[5],True)+0.2, EntityZ(e\room\Objects[5],True))
					n\State[0] = MTF_TARGET_PLAYER
					n\Reload = 6*70
					PointEntity n\Collider,Collider
					e\room\NPC[1] = n
					
					n.NPCs = CreateNPC(NPCtypeNTF, EntityX(e\room\Objects[5],True), EntityY(e\room\Objects[5],True)+0.2, EntityZ(e\room\Objects[5],True))
					n\State[0] = MTF_TARGET_PLAYER
					n\Reload = (6*70)+Rnd(15,30)
					RotateEntity n\Collider,0,EntityYaw(e\room\NPC[1]\Collider),0
					MoveEntity n\Collider,0.5,0,0
					PointEntity n\Collider,Collider
					
					n.NPCs = CreateNPC(NPCtypeNTF, EntityX(e\room\Objects[5],True), EntityY(e\room\Objects[5],True)+0.2, EntityZ(e\room\Objects[5],True))
					n\State[0] = MTF_TARGET_PLAYER
					n\Reload = 6*70+Rnd(15,30)
					RotateEntity n\Collider,0,EntityYaw(e\room\NPC[1]\Collider),0
					n\State[1] = EntityYaw(n\Collider)
					MoveEntity n\Collider,-0.65,0,0
					
					MoveEntity e\room\NPC[1]\Collider,0,0,0.1
					PointEntity Collider, e\room\NPC[1]\Collider
					
					PlaySound_Strict LoadTempSound("SFX\Character\MTF\049\Player0492_1.ogg")
					
					LoadEventSound(e,"SFX\SCP\049\0492Breath.ogg")
					
					IsZombie = True
				EndIf
			EndIf
		Else
			BlurTimer = 800
			ForceMove = 0.25
			;Injuries = Max(2.0,Injuries)
			;Bloodloss = 0
			Infect = 0
			
			pvt% = CreatePivot()
			PositionEntity pvt%,EntityX(e\room\NPC[1]\Collider),EntityY(e\room\NPC[1]\Collider)+0.5,EntityZ(e\room\NPC[1]\Collider)
			
			PointEntity Collider, e\room\NPC[1]\Collider
			PointEntity Camera, pvt%,EntityRoll(Camera)
			
			pvt = FreeEntity_Strict(pvt)
			
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