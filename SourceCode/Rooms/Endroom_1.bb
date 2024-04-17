
Function FillRoom_Endroom_1(r.Rooms)
	Local i%
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x, 0, r\z + 1136 * RoomScale, 0, r, False, True, 6)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
	For i = 0 To 1
		r\RoomDoors[0]\buttons[i] = FreeEntity_Strict(r\RoomDoors[0]\buttons[i])
	Next
	
End Function

Function UpdateEvent_Endroom_1(e.Events)
	Local de.Decals
	
	If Curr106\Contained Then
		If e\EventState[0] = 0 Then
			If e\room\dist < 8 And e\room\dist > 0 Then
				If Curr106\State[0] < 0 Then 
					RemoveEvent(e)
				Else
					e\room\RoomDoors[0]\open = True
					
					e\room\NPC[0]=CreateNPC(NPCtypeD, EntityX(e\room\RoomDoors[0]\obj,True), 0.5, EntityZ(e\room\RoomDoors[0]\obj,True))
					
					ChangeNPCTextureID(e\room\NPC[0],4)
					
					PointEntity e\room\NPC[0]\Collider, e\room\obj
					RotateEntity e\room\NPC[0]\Collider, 0, EntityYaw(e\room\NPC[0]\Collider),0, True
					MoveEntity e\room\NPC[0]\Collider, 0,0,0.5 
					
					e\room\RoomDoors[0]\open = False
					PlaySound2(LoadTempSound("SFX\Door\EndroomDoor.ogg"), Camera, e\room\obj, 15)
					
					e\EventState[0] = 1							
				EndIf
			EndIf
		ElseIf e\EventState[0] = 1
			If PlayerRoom = e\room Then
				e\room\NPC[0]\State[0] = 1
				e\EventState[0] = 2
				
				e\Sound[0] = LoadSound_Strict("SFX\Character\Janitor\106Abduct.ogg")
				PlaySound_Strict(e\Sound[0])		
				
				If e\SoundCHN[0]<>0 Then StopChannel e\SoundCHN[0]
			ElseIf e\room\dist < 8
				If e\Sound[0] = 0 Then e\Sound[0] = LoadSound_Strict("SFX\Character\Janitor\Idle.ogg")
				e\SoundCHN[0] = LoopSound2(e\Sound[0], e\SoundCHN[0], Camera, e\room\NPC[0]\obj, 15.0)
			EndIf
		ElseIf e\EventState[0] = 2
			If EntityDistanceSquared(e\room\NPC[0]\Collider, e\room\obj)<PowTwo(1.5) Then
				de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\obj), 0.01, EntityZ(e\room\obj), 90, Rand(360), 0)
				de\Size = 0.05 : de\SizeChange = 0.008 : de\Timer=10000 : UpdateDecals
				e\EventState[0] = 3
			EndIf					
		Else
			PositionEntity(Curr106\obj, EntityX(e\room\obj, True), 0.0, EntityZ(e\room\obj, True))
			PointEntity(Curr106\obj, e\room\NPC[0]\Collider)
			RotateEntity(Curr106\obj, 0, EntityYaw(Curr106\obj), 0, True)
			
			Curr106\Idle = True
			
			If DistanceSquared(EntityX(e\room\NPC[0]\Collider),EntityX(e\room\obj),EntityZ(e\room\NPC[0]\Collider), EntityZ(e\room\obj))<PowTwo(0.4) Then
				If e\room\NPC[0]\State[0]=1 Then
					SetNPCFrame(e\room\NPC[0],41)
				EndIf
				e\EventState[0] = e\EventState[0]+FPSfactor/2
				e\room\NPC[0]\State[0] = 6
				e\room\NPC[0]\CurrSpeed = CurveValue(0.0, e\room\NPC[0]\CurrSpeed, 25.0)
				PositionEntity(e\room\NPC[0]\Collider, CurveValue(EntityX(e\room\obj, True), EntityX(e\room\NPC[0]\Collider), 25.0), 0.3-e\EventState[0]/70, CurveValue(EntityZ(e\room\obj, True), EntityZ(e\room\NPC[0]\Collider), 25.0))
				ResetEntity(e\room\NPC[0]\Collider)
				
				AnimateNPC(e\room\NPC[0], 41, 58, 0.1, False)
				
				AnimateNPC(Curr106, 206,112, -1.0, False)
			Else
				AnimateNPC(Curr106, 112,206, 1.5, False)
			EndIf
			
			If e\EventState[0] > 35 Then
				
				PositionEntity(Curr106\obj, EntityX(Curr106\Collider), -100.0, EntityZ(Curr106\Collider), True)
				PositionEntity(Curr106\Collider, EntityX(Curr106\Collider), -100.0, EntityZ(Curr106\Collider), True)
				
				Curr106\Idle = False
				If EntityDistanceSquared(Collider, e\room\obj)<PowTwo(2.5) Then Curr106\State[0]=-0.1
				
				RemoveNPC(e\room\NPC[0])
				
				RemoveEvent(e)
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D