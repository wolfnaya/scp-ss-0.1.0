
Function FillRoom_Room2_Elevator_1(r.Rooms)
	
	r\Objects[0] = CreatePivot(r\obj)
	PositionEntity(r\Objects[0], r\x+888.0*RoomScale, 240.0*RoomScale, r\z, True)
	
	r\Objects[1] = CreatePivot(r\obj)
	PositionEntity(r\Objects[1], r\x+1024.0*RoomScale-0.01, 120.0*RoomScale, r\z, True)
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x + 448.0 * RoomScale, 0.0, r\z, 90, r, False, DOOR_ELEVATOR)
	PositionEntity(r\RoomDoors[0]\buttons[1], r\x + 416.0 * RoomScale, EntityY(r\RoomDoors[0]\buttons[1],True), r\z - 208.0 * RoomScale,True)
	PositionEntity(r\RoomDoors[0]\buttons[0], r\x + 480.0 * RoomScale, EntityY(r\RoomDoors[0]\buttons[0],True), r\z + 184.0 * RoomScale,True)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = True : r\RoomDoors[0]\locked = True
	
End Function

Function UpdateEvent_Room2_Elevator_1(e.Events)
	Local de.Decals
	
	If e\EventState[0] = 0 Then
		If e\room\dist < 8.0 And e\room\dist > 0 Then
			e\room\NPC[0]=CreateNPC(NPCtypeGuard, EntityX(e\room\obj,True), 0.5, EntityZ(e\room\obj,True))
			PointEntity e\room\NPC[0]\Collider, Collider
			RotateEntity e\room\NPC[0]\Collider, 0, EntityYaw(e\room\NPC[0]\Collider),0, True	
			
			e\EventState[0] = 1
		EndIf
	Else
		If e\EventState[0] = 1 Then
			If e\room\dist<5.0 Lor Rand(700)=1 Then 
				e\EventState[0] = 2
				
				e\room\NPC[0]\State[0] = 5
				e\room\NPC[0]\EnemyX = EntityX(e\room\Objects[1],True)
				e\room\NPC[0]\EnemyY = EntityY(e\room\Objects[1],True)
				e\room\NPC[0]\EnemyZ = EntityZ(e\room\Objects[1],True)
			EndIf
		ElseIf e\EventState[0] = 2
			If EntityDistanceSquared(e\room\NPC[0]\Collider,e\room\Objects[1])<PowTwo(2.0) Then
				e\room\RoomDoors[0]\open = False
				PlaySound2(CloseDoorSFX[3 * 3 + 0], Camera, e\room\RoomDoors[0]\obj, 8.0)			
				
				PlaySound_Strict (LoadTempSound("SFX\Room\Room2ElevatorDeath.ogg"))
				
				e\EventState[0] = 2.05
			EndIf
		ElseIf e\EventState[0] < 13*70
			e\EventState[0] = e\EventState[0]+FPSfactor
			If e\EventState[0] > 6.7*70 And e\EventState[0] < 7.4*70 Then
				CameraShake = 7.4-(e\EventState[0]/70.0)
			ElseIf e\EventState[0] > 8.6*70 And e\EventState[0] < 10.6*70 
				CameraShake = 10.6-(e\EventState[0]/70.0)
			ElseIf e\EventState[0] > 12.6*70
				CameraShake = 0
				If e\EventState[0]-FPSfactor < 12.6*70 And e\room\NPC[0]<>Null Then
					RemoveNPC(e\room\NPC[0])
					e\room\NPC[0]=Null
					
					de.Decals = CreateDecal(DECAL_BLOODSPLAT2, EntityX(e\room\Objects[0],True), 0.0005, EntityZ(e\room\Objects[0],True),90,Rnd(360),0)
					
					de.Decals = CreateDecal(DECAL_BLOODPOOL, EntityX(e\room\Objects[0],True), 0.002, EntityZ(e\room\Objects[0],True),90,Rnd(360),0)
					de\Size = 0.5
					
					de.Decals = CreateDecal(DECAL_BLOODSPLAT2, EntityX(e\room\Objects[1],True), EntityY(e\room\Objects[1],True), EntityZ(e\room\Objects[1],True),0,e\room\angle+270,0)
					de\Size = 0.9
				EndIf
				e\room\RoomDoors[0]\locked = False
			EndIf
		Else
			If e\room\RoomDoors[0]\open Then e\room\RoomDoors[0]\locked = True : RemoveEvent(e)
		EndIf
	EndIf
	
End Function

Function CreateEvent_Room2_Elevator_1_Alt_2(e.Events)
	Local de.Decals
	
	de.Decals = CreateDecal(DECAL_BLOODSPLAT2, EntityX(e\room\Objects[0],True), 0.0005, EntityZ(e\room\Objects[0],True),90,Rnd(360),0)
	
	e\room\NPC[0]=CreateNPC(NPCtypeD, EntityX(e\room\Objects[0],True), 0.5, EntityZ(e\room\Objects[0],True))
	ChangeNPCTextureID(e\room\NPC[0],0)
	RotateEntity e\room\NPC[0]\Collider, 0, EntityYaw(e\room\obj)-80,0, True
	SetNPCFrame e\room\NPC[0], 19
	e\room\NPC[0]\State[0]=8
	
	RemoveEvent(e)
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D