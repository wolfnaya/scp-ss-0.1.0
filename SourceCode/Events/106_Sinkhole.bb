
Function CreateEvent_106_Sinkhole(e.Events)
	Local de.Decals
	
	de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\obj)+Rnd(-0.5,0.5), 0.01, EntityZ(e\room\obj)+Rnd(-0.5,0.5), 90, Rand(360), 0)
	de\Size = 2.5 : ScaleSprite(de\obj, de\Size, de\Size);
	
End Function

Function UpdateEvent_106_Sinkhole(e.Events)
	Local x#,y#,z# ;TODO should I have removed the dist here?
	
	If PlayerRoom = e\room
		If e\Sound[0]=0 Then
			e\Sound[0]=LoadSound_Strict("SFX\Room\Sinkhole.ogg")
		Else
			e\SoundCHN[0] = LoopSound2(e\Sound[0], e\SoundCHN[0], Camera, e\room\obj, 4.5, 1.5)
		EndIf
		Local dist = DistanceSquared(EntityX(Collider),EntityX(e\room\obj),EntityZ(Collider),EntityZ(e\room\obj))
		If dist < PowTwo(2.0) Then
			dist = Sqr(dist)
			CurrStepSFX=1
			CurrSpeed = CurveValue(0.0, CurrSpeed, Max(dist*50,1.0))	
			CrouchState = (2.0-dist)/2.0
			
			If dist<0.5 Then
				If e\EventState[0] = 0 Then
					PlaySound_Strict(LoadTempSound("SFX\Room\SinkholeFall.ogg"))
				EndIf
				
				CurrSpeed = CurveValue(0.0, CurrSpeed, Max(dist*50,1.0))
				
				x = CurveValue(EntityX(e\room\obj),EntityX(Collider),10.0)
				y = CurveValue(EntityY(e\room\obj)-e\EventState[1],EntityY(Collider),25.0)
				z = CurveValue(EntityZ(e\room\obj),EntityZ(Collider),10.0)
				PositionEntity Collider, x, y, z, True
				
				DropSpeed = 0
				
				ResetEntity Collider
				
				e\EventState[0]=Min(e\EventState[0]+FPSfactor/200.0,2.0)
				
				LightBlink = Min(e\EventState[0]*5,10.0)
				BlurTimer = e\EventState[0]*500
				
				If e\EventState[0] = 2.0 Then MoveToPocketDimension()
			EndIf
		EndIf
	Else 
		e\EventState[0]=0
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D