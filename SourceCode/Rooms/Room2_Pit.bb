
Function FillRoom_Room2_Pit(r.Rooms)
	Local em.Emitters
	Local xtemp%,ztemp%
	Local i
	
	i = 0
	For  xtemp% = -1 To 1 Step 2
		For ztemp% = -1 To 1
			em.Emitters = CreateEmitter(r\x + 202.0 * RoomScale * xtemp, 8.0 * RoomScale, r\z + 256.0 * RoomScale * ztemp, 0)
			em\RandAngle = 30
			em\Speed = 0.0045
			em\SizeChange = 0.007
			em\Achange = -0.016
			r\Objects[i] = em\Obj
			If i < 3 Then 
				TurnEntity(em\Obj, 0, -90, 0, True) 
			Else 
				TurnEntity(em\Obj, 0, 90, 0, True)
			EndIf
			TurnEntity(em\Obj, -45, 0, 0, True)
			EntityParent(em\Obj, r\obj)
			i=i+1
		Next
	Next
	
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x + 0.25, 0.5, r\z + 0.75)
	EntityParent(r\Objects[0], r\obj)
	
	r\Objects[1] = CreatePivot()
	PositionEntity(r\Objects[1], r\x + 640.0 * RoomScale, 8.0 * RoomScale, r\z - 896.0 * RoomScale)
	EntityParent(r\Objects[1], r\obj)
	
	r\Objects[2] = CreatePivot()
	PositionEntity(r\Objects[2], r\x - 864.0 * RoomScale, -400.0 * RoomScale, r\z - 632.0 * RoomScale)
	EntityParent(r\Objects[2],r\obj)
	
End Function

Function UpdateEvent_Room2_Pit(e.Events)
	
	If Curr173 <> Null And Curr173\Idle = SCP173_ACTIVE Then 
		If e\room\dist < 8.0  And e\room\dist > 0 Then			
			If (Not EntityVisible(Curr173\Collider, Camera)) And (Not EntityVisible(e\room\Objects[1], Camera)) Then 
				PositionEntity(Curr173\Collider, EntityX(e\room\Objects[1], True), 0.5, EntityZ(e\room\Objects[1], True))
				ResetEntity(Curr173\Collider)
				RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	
End Function

Function UpdateEvent_Room2_Pit_106(e.Events)
	
	If (Not Curr106\Contained) And Curr106\State[0]>0 Then 
		If e\EventState[0] = 0 Then
			If PlayerRoom = e\room Then e\EventState[0] = 1
		Else
			e\EventState[0] = e\EventState[0] + 1
			PositionEntity(Curr106\Collider, EntityX(e\room\Objects[2], True), EntityY(e\room\Objects[2], True), EntityZ(e\room\Objects[2], True))
			ResetEntity(Curr106\Collider)
			
			PointEntity(Curr106\Collider, Camera)
			TurnEntity(Curr106\Collider, 0, Sin(MilliSecs() / 20) * 6.0, 0, True)
			MoveEntity(Curr106\Collider, 0, 0, Sin(MilliSecs() / 15) * 0.06)
			PositionEntity(Curr106\obj, EntityX(Curr106\Collider), EntityY(Curr106\Collider) - 0.15, EntityZ(Curr106\Collider))
			
			RotateEntity Curr106\obj, 0, EntityYaw(Curr106\Collider), 0
			Curr106\Idle = True
			AnimateNPC(Curr106, 334, 494, 0.3)
			If e\EventState[0] > 800 Then
				If BlinkTimer < - 5 Then Curr106\Idle = False : RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	
End Function

Function UpdateEvent_Room2_Pit_Body(e.Events)
	
	If e\room\dist < 10.0 And e\room\dist > 0 Then
		e\room\NPC[0]=CreateNPC(NPCtypeD, EntityX(e\room\Objects[0],True), 0.5, EntityZ(e\room\Objects[0],True))
		RotateEntity e\room\NPC[0]\Collider, 0, e\room\angle-45.0, 0
		SetNPCFrame e\room\NPC[0], 559
		e\room\NPC[0]\State[0]=3
		e\room\NPC[0]\IsDead=True
		
		RemoveEvent(e)
	EndIf
	
End Function	
;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D