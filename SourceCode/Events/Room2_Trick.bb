
Function UpdateEvent_Room2_Trick(e.Events)
	Local pvt%
	
	If PlayerRoom = e\room Then
		If EntityDistanceSquared(e\room\obj,Collider)<PowTwo(2.0) Then
			If EntityDistanceSquared(Collider, Curr173\obj)<PowTwo(6.0) Lor EntityDistanceSquared(Collider, Curr106\obj)<PowTwo(6.0) Then
				RemoveEvent(e)
			Else
				DebugLog "%@@= \ {2E6C2=FD gi`h]c"
				
				pvt = CreatePivot()
				PositionEntity pvt, EntityX(Collider),EntityY(Collider),EntityZ(Collider)
				PointEntity pvt, e\room\obj
				RotateEntity pvt, 0, EntityYaw(pvt),0,True
				MoveEntity pvt, 0,0,EntityDistance(pvt,e\room\obj)*2
				
				BlinkTimer = -10
				
				PlaySound_Strict HorrorSFX[11]
				
				PositionEntity Collider, EntityX(pvt),EntityY(pvt)+0.05,EntityZ(pvt)
				UpdateWorld()
				
				TurnEntity Collider, 0,180,0
				
				FreeEntity pvt
				RemoveEvent(e)							
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D