
Function UpdateEvent_Room3_Servers(e.Events)
	Local temp%
	
	If Curr173\Idle <> SCP173_BOXED Lor Curr173\Idle <> SCP173_CONTAINED Then
		If PlayerRoom = e\room Then
			If e\EventState[2]=0 And Curr173\Idle = 0 Then
				If BlinkTimer < -10 Then 
					temp = Rand(0,2)
					PositionEntity Curr173\Collider, EntityX(e\room\Objects[temp],True),EntityY(e\room\Objects[temp],True),EntityZ(e\room\Objects[temp],True)
					ResetEntity Curr173\Collider
					e\EventState[2]=1
				EndIf
			EndIf
			
			If e\room\Objects[3]<>0 Then 
				If BlinkTimer<-8 And BlinkTimer >-12 Then
					PointEntity e\room\Objects[3], Camera
					RotateEntity(e\room\Objects[3], 0, EntityYaw(e\room\Objects[3],True),0, True)
				EndIf
				If e\EventState[1] = 0 Then 
					e\EventState[0] = CurveValue(0, e\EventState[0], 15.0)
					If Rand(800)=1 Then e\EventState[1] = 1
				Else
					e\EventState[0] = e\EventState[0]+(FPSfactor*0.5)
					If e\EventState[0] > 360 Then e\EventState[0] = 0	
					
					If Rand(1200)=1 Then e\EventState[1] = 0
				EndIf
				
				PositionEntity e\room\Objects[3], EntityX(e\room\Objects[3],True), (-608.0*RoomScale)+0.05+Sin(e\EventState[0]+270)*0.05, EntityZ(e\room\Objects[3],True), True
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D