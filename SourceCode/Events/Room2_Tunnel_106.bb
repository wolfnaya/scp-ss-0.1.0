
Function UpdateEvent_Room2_Tunnel_106(e.Events)
	Local d.Decals
	Local n.NPCs
	
	If e\EventState[0] = 0 Then
		If e\room\dist < 5.0 And e\room\dist > 0 Then
			If Curr106\State[0] >= 0 Then
				e\EventState[0] = 1
			Else
				If Curr106\State[0] <= -10 And EntityDistanceSquared(Curr106\Collider,Collider)>PowTwo(5) And (Not EntityInView(Curr106\obj,Camera)) Then
					e\EventState[0] = 1
					e\EventState[1] = 1
				EndIf
			EndIf
		ElseIf Curr106\Contained
			RemoveEvent(e)
		EndIf
	ElseIf e\EventState[0] = 1
		
		If e\room\dist < 3.0 Lor Rand(7000)=1 Then
			e\EventState[0] = 2
			d.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\obj), 445.0*RoomScale, EntityZ(e\room\obj), -90, Rand(360), 0)
			d\Size = Rnd(0.5, 0.7) : EntityAlpha(d\obj, 0.7) : d\ID = 1 : ScaleSprite(d\obj, d\Size, d\Size)
			EntityAlpha(d\obj, Rnd(0.7, 0.85))
			
			PlaySound_Strict HorrorSFX[10]
		ElseIf e\room\dist > 8.0
			If Rand(5) = 1 Then
				Curr106\Idle = False
				RemoveEvent(e)
			Else
				Curr106\Idle = False
				Curr106\State[0] = -10000
				RemoveEvent(e)
			End If
		EndIf
	Else
		If e\EventState[1] = 1 Then
			ShouldPlay = MUS_CHASE_106
		EndIf
		e\EventState[0] = e\EventState[0]+FPSfactor
		If e\EventState[0] <= 180 Then
			PositionEntity(Curr106\Collider, EntityX(e\room\obj, True), EntityY(Collider) + 1.0 - Min(Sin(e\EventState[0])*1.5,1.1), EntityZ(e\room\obj, True), True)
			PointEntity(Curr106\Collider, Camera)
			AnimateNPC(Curr106, 55, 104, 0.1)
			Curr106\Idle = True
			Curr106\State[0] = 1
			ResetEntity(Curr106\Collider)
			Curr106\DropSpeed = 0
			PositionEntity(Curr106\obj, EntityX(Curr106\Collider), EntityY(Curr106\Collider) - 0.15, EntityZ(Curr106\Collider))
			RotateEntity Curr106\obj, 0, EntityYaw(Curr106\Collider), 0
			ShowEntity Curr106\obj
		ElseIf e\EventState[0] > 180 And e\EventState[0] < 300 Then
			Curr106\Idle = False
			Curr106\State[0] = -10
			PositionEntity(Curr106\Collider, EntityX(e\room\obj, True), -3.0, EntityZ(e\room\obj, True), True)
			Curr106\PathTimer = 70*10
			Curr106\PathStatus = 0
			Curr106\PathLocation = 0
			d.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\obj, True), 0.01, EntityZ(e\room\obj, True), 90, Rand(360), 0)
			d\Size = 0.05 : d\SizeChange = 0.01 : EntityAlpha(d\obj, 0.8) : UpdateDecals
			e\EventState[0] = 300
		ElseIf e\EventState[0] < 800
			If EntityY(Curr106\Collider)>=EntityY(Collider)-0.05 Then
				RemoveEvent(e)
			Else
				TranslateEntity Curr106\Collider, 0, ((EntityY(Collider,True) - 0.11) - EntityY(Curr106\Collider)) / 50.0, 0
				If EntityY(Curr106\Collider)<-0.1 Then
					Curr106\CurrSpeed = 0.0
				EndIf
			EndIf
		Else
			RemoveEvent(e)
		EndIf
		
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D