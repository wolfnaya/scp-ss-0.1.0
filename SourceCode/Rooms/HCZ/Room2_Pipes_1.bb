
Function FillRoom_Room2_Pipes_1(r.Rooms)
	
	r\Objects[0]= CreatePivot(r\obj)
	PositionEntity(r\Objects[0], r\x + 368.0 * RoomScale, 0.0, r\z, True)
	
	r\Objects[1] = CreatePivot(r\obj)
	PositionEntity(r\Objects[1], r\x - 368.0 * RoomScale, 0.0, r\z, True)
	
	r\Objects[2] = CreatePivot(r\obj)
	PositionEntity(r\Objects[2], r\x + 224.0 * RoomScale - 0.005, 192.0 * RoomScale, r\z, True)
	
	r\Objects[3] = CreatePivot(r\obj)
	PositionEntity(r\Objects[3], r\x - 224.0 * RoomScale + 0.005, 192.0 * RoomScale, r\z, True)
	
End Function

Function UpdateEvent_Room2_Pipes_1(e.Events)
	Local d.Decals
	Local pvt%
	Local n.NPCs
	
	If (Not Curr106\Contained) Then 
		If e\EventState[0] = 0 Then
			If PlayerRoom = e\room Then e\EventState[0] = 1
		Else
			e\EventState[0]=(e\EventState[0]+FPSfactor*0.7)
			;0-50 = walk to the middle
			;50-200 = look around
			;200-250 = leave
			If e\EventState[0] < 50 Then
				Curr106\Idle = True
				PositionEntity(Curr106\Collider, EntityX(e\room\Objects[0], True), EntityY(Collider) - 0.15, EntityZ(e\room\Objects[0], True))
				PointEntity(Curr106\Collider, e\room\Objects[1])
				MoveEntity(Curr106\Collider, 0, 0, EntityDistance(e\room\Objects[0], e\room\Objects[1])*0.5 * (e\EventState[0] / 50.0))
				AnimateNPC(Curr106, 284, 333, 0.02*35)
			ElseIf e\EventState[0] < 200
				Curr106\Idle = True
				AnimateNPC(Curr106, 334, 494, 0.2)
				
				PositionEntity(Curr106\Collider, (EntityX(e\room\Objects[0], True)+EntityX(e\room\Objects[1], True))/2, EntityY(Collider) - 0.15, (EntityZ(e\room\Objects[0], True)+EntityZ(e\room\Objects[1], True))/2)
				RotateEntity(Curr106\Collider,0, CurveValue(e\EventState[0],EntityYaw(Curr106\Collider),30.0),0,True)
				If EntityDistanceSquared(Curr106\Collider, Collider)<PowTwo(4.0) Then
					pvt = CreatePivot()
					PositionEntity(pvt, EntityX(Curr106\Collider),EntityY(Curr106\Collider),EntityZ(Curr106\Collider))
					PointEntity pvt, Collider
					If WrapAngle(EntityYaw(pvt)-EntityYaw(Curr106\Collider))<80 Then
						Curr106\State[0] = -11
						Curr106\Idle = False
						PlaySound_Strict(HorrorSFX[10])
						e\EventState[0] = 260
					EndIf
					FreeEntity pvt
				EndIf
			ElseIf e\EventState[0] < 250
				Curr106\Idle = True
				PositionEntity(Curr106\Collider, EntityX(e\room\Objects[0], True), EntityY(Collider) - 0.15, EntityZ(e\room\Objects[0], True))
				PointEntity(Curr106\Collider, e\room\Objects[1])
				MoveEntity(Curr106\Collider, 0, 0, EntityDistance(e\room\Objects[0], e\room\Objects[1]) * ((e\EventState[0]-150.0) / 100.0))
				AnimateNPC(Curr106, 284, 333, 0.02*35)
			EndIf
			ResetEntity(Curr106\Collider)
			
			PositionEntity(Curr106\obj, EntityX(Curr106\Collider), EntityY(Curr106\Collider) - 0.15, EntityZ(Curr106\Collider))
			RotateEntity Curr106\obj, 0, EntityYaw(Curr106\Collider), 0
			
			If (e\EventState[0] / 250.0) > 0.3 And ((e\EventState[0] - FPSfactor*0.7) / 250.0) <= 0.3 Then
				e\SoundCHN[0] = PlaySound_Strict(HorrorSFX[6])
				BlurTimer = 800
				d.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[2], True), EntityY(e\room\Objects[2], True), EntityZ(e\room\Objects[2], True), 0, e\room\angle - 90, Rnd(360))
				d\Timer = 90000
				d\Alpha = 0.01 : d\AlphaChange = 0.005
				d\Size = 0.1 : d\SizeChange = 0.003
			EndIf
			
			If (e\EventState[0] / 250.0) > 0.65 And ((e\EventState[0] - FPSfactor*0.7) / 250.0) <= 0.65 Then
				d.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[3], True), EntityY(e\room\Objects[3], True), EntityZ(e\room\Objects[3], True), 0, e\room\angle + 90, Rnd(360))
				d\Timer = 90000
				d\Alpha = 0.01 : d\AlphaChange = 0.005
				d\Size = 0.1 : d\SizeChange = 0.003
			EndIf						
			
			If e\EventState[0] > 250 Then Curr106\Idle = False : RemoveEvent(e)
			
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D