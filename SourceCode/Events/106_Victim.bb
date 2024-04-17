
Function UpdateEvent_106_Victim(e.Events)
	Local de.Decals
	
;	If (Not Curr106\Contained) Then
;		If PlayerRoom = e\room Then
;			If e\EventState[0] = 0 Then
;				de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\obj), 799.0*RoomScale, EntityZ(e\room\obj), -90, Rand(360), 0)
;				de\Size = 0.05 : de\SizeChange = 0.0015 : EntityAlpha(de\obj, 0.8) : UpdateDecals()			
;				PlaySound2(DecaySFX[3], Camera, de\obj, 15.0)
;				e\EventState[0]=1
;			EndIf
;		EndIf
;		
;		If e\EventState[0] > 0 Then 
;			If e\room\NPC[0]=Null Then
;				e\EventState[0]=e\EventState[0]+FPSfactor
;			EndIf
;			If e\EventState[0]>200 Then
;				If e\room\NPC[0]=Null Then
;					e\room\NPC[0]=CreateNPC(NPCtypeD, EntityX(e\room\obj), 900.0*RoomScale, EntityZ(e\room\obj))
;					RotateEntity e\room\NPC[0]\Collider, 0, Rnd(360), 0, True
;					ChangeNPCTextureID(e\room\NPC[0],5)
;					e\room\NPC[0]\State[0]=6
;					
;					PlaySound_Strict HorrorSFX[0]
;					PlaySound2(DecaySFX[2], Camera, e\room\NPC[0]\Collider, 15.0)
;				EndIf
;				
;				e\room\NPC[0]\FallingPickDistance = 20.0
;				EntityType e\room\NPC[0]\Collider,HIT_PLAYER
;				If EntityY(e\room\NPC[0]\Collider)>0.35 Then
;					AnimateNPC(e\room\NPC[0], 1, 10, 0.12, False)
;					Local dist# = EntityDistanceSquared(Collider,e\room\NPC[0]\Collider)
;					If dist<PowTwo(0.8) Then ;get the player out of the way
;						dist = Sqr(dist)
;						Local fdir# = point_direction(EntityX(Collider,True),EntityZ(Collider,True),EntityX(e\room\NPC[0]\Collider,True),EntityZ(e\room\NPC[0]\Collider,True))
;						TranslateEntity Collider,Cos(-fdir+90)*(dist-0.8)*(dist-0.8),0,Sin(-fdir+90)*(dist-0.8)*(dist-0.8)
;					EndIf
;					
;					If EntityY(e\room\NPC[0]\Collider)>0.6 Then EntityType e\room\NPC[0]\Collider,0
;				Else
;					e\EventState[0]=e\EventState[0]+FPSfactor
;					AnimateNPC(e\room\NPC[0], 11, 19, 0.25, False)
;					If e\Sound[0]=0 Then 
;						LoadEventSound(e,"SFX\General\BodyFall.ogg")
;						PlaySound_Strict e\Sound[0]
;						
;						de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\obj), 0.001, EntityZ(e\room\obj), 90, Rand(360), 0)
;						de\Size = 0.4 : EntityAlpha(de\obj, 0.8) : UpdateDecals()			
;					EndIf
;					
;					If e\EventState[0]>400 Then
;						If e\Sound[0]<>0 Then FreeSound_Strict e\Sound[0] : e\Sound[0]=0
;						RemoveEvent(e)
;					EndIf								
;				EndIf
;				
;			EndIf
;		EndIf
;		
;	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D