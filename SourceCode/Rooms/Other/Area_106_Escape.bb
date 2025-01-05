
Function FillRoom_Area_106_Escape(r.Rooms)
	Local ne.NewElevator
	
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x - 176.0 * RoomScale,r\x + 0.0 * RoomScale, r\z -17297.0 * RoomScale)
	EntityParent(r\Objects[0], r\obj)
	
	Local ElevatorOBJ% = LoadRMesh("GFX\map\Elevators\elevator.rmesh",Null)
	HideEntity ElevatorOBJ
	r\Objects[6] = CopyEntity(ElevatorOBJ,r\obj)
	
	PositionEntity(r\Objects[6],-158,1110,11072)
	EntityType r\Objects[6],HIT_MAP
	RotateEntity r\Objects[6],0,90,0
	EntityPickMode r\Objects[6],2
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x -158.0 * RoomScale, r\y+1110 * RoomScale, r\z+10709 * RoomScale, 180, r, True, 5, -1, "", 1)
	r\RoomDoors[0]\DisableWaypoint = True
	
	ne = CreateNewElevator(r\Objects[6],1,r\RoomDoors[0],1,r,1110.0,4095.0,8190.0)
	ne\floorlocked[2] = True
	
End Function

Function UpdateEvent_Area_106_Escape(e.Events)
	Local ne.NewElevator
	
	If PlayerRoom = e\room
		
		If Curr173\Idle <> SCP173_DISABLED Then
			Curr173\Idle = SCP173_DISABLED
			HideEntity Curr173\obj
			HideEntity Curr173\obj2
		EndIf
		
		If e\EventState[0] = 0 Then
			SaveGame(SavePath + CurrSave\Name + "\", True)
		EndIf
		
		e\EventState[0] = e\EventState[0] + FPSfactor
		
		If e\EventState[0] > 0 And e\EventState[0] < 12.01*70 Then
			If e\room\NPC[0] = Null Then
				e\room\NPC[0] = CreateNPC(NPC_SCP_106, EntityX(e\room\Objects[0],True), EntityY(e\room\Objects[0],True), EntityZ(e\room\Objects[0],True))
			EndIf
			Local pvt = CreatePivot()
			PositionEntity(pvt,EntityX(Collider),EntityY(Collider),EntityZ(Collider))
			
			PointEntity pvt, e\room\NPC[0]\Collider
			RotateEntity Collider, 0, CurveAngle(EntityYaw(pvt),EntityYaw(Collider),100.0), 0
			
			psp\NoMove = True
			psp\NoRotation = True
		ElseIf e\EventState[0] >= 12.01*70 Then
			psp\NoMove = False
			psp\NoRotation = False
			
			FreeEntity_Strict pvt
		EndIf
		
		If e\EventState[0] >= 12.01*70 And e\EventState[0] < 12.01*70 Then
			If (Not TaskExists(TASK_NTF_ESCAPE_106)) Then
				BeginTask(TASK_NTF_ESCAPE_106)
			EndIf
			;CreateSplashText("RUN!",opt\GraphicWidth/2.0,opt\GraphicHeight/2.0,100,1,Font_Default_Large,True,False,255,20,20)
			e\EventState[1] = 1
		EndIf
		
		If e\EventState[2] = 0 Then
			InfiniteStamina% = True
			ShouldPlay = MUS_CHASE_106_3
			
;			If e\EventState[0] >= 12.01*70 Then
;				If EntityDistanceSquared(e\room\NPC[0]\Collider, Collider)>PowTwo(8.0) Then
;					Curr106\State = 0
;				EndIf 
;			EndIf
		EndIf
		
		If PlayerInNewElevator Then
			e\EventState[2] = 1
		EndIf
		If e\EventState[2] = 1 Then
			If e\room\NPC[0] <> Null Then
				RemoveNPC(e\room\NPC[0])
			EndIf
		EndIf
		
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D TSS