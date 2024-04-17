
Function FillRoom_Cont_035(r.Rooms)
	Local d.Doors,d2.Doors,em.Emitters,it.Items
	Local i,n
	
	d = CreateDoor(r\zone, r\x - 296.0 * RoomScale, 0, r\z - 672.0 * RoomScale, 180, r, True, 0, 5)
	d\AutoClose = False : d\locked = True : r\RoomDoors[0]=d
	PositionEntity (d\buttons[1], r\x - 164.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
	d\buttons[0] = FreeEntity_Strict(d\buttons[0])
	d\obj2 = FreeEntity_Strict(d\obj2)
	
	d2 = CreateDoor(r\zone, r\x - 296.0 * RoomScale, 0, r\z - 144.0 * RoomScale, 0, r, False)
	d2\AutoClose = False : d2\locked = True : r\RoomDoors[1]=d2
	PositionEntity (d2\buttons[0], r\x - 432.0 * RoomScale, EntityY(d2\buttons[0],True), r\z - 480.0 * RoomScale, True)
	RotateEntity(d2\buttons[0], 0, 90, 0, True)
	d2\buttons[1] = FreeEntity_Strict(d2\buttons[1])
	d2\obj2 = FreeEntity_Strict(d2\obj2)
	
	;door to the control room
	r\RoomDoors[2] = CreateDoor(r\zone, r\x + 384.0 * RoomScale, 0, r\z - 672.0 * RoomScale, 180, r, False, 0, 5)
	r\RoomDoors[2]\AutoClose = False
	
	;door to the storage room
	r\RoomDoors[3] = CreateDoor(0, r\x + 768.0 * RoomScale, 0, r\z +512.0 * RoomScale, 90, r, False, 0, 0, "5731")
	r\RoomDoors[3]\AutoClose = False			
	
	d\LinkedDoor = d2 : d2\LinkedDoor = d
	
	r\Levers[0] = CreateLever(r,r\x + 210.0 * RoomScale, r\y + 224.0 * RoomScale, r\z - 208 * RoomScale, 90)
	r\Levers[1] = CreateLever(r,r\x + 210.0 * RoomScale, r\y + 224.0 * RoomScale, r\z - (208-76) * RoomScale, 90)
	
	;the control room
	r\Objects[3] = CreatePivot(r\obj)
	PositionEntity(r\Objects[3], r\x + 456 * RoomScale, 0.5, r\z + 400.0 * RoomScale, True)
	
	r\Objects[4] = CreatePivot(r\obj)
	PositionEntity(r\Objects[4], r\x - 576 * RoomScale, 0.5, r\z + 640.0 * RoomScale, True)
	
	For i = 0 To 1
		em.Emitters = CreateEmitter(r\x - 272.0 * RoomScale, 10, r\z + (624.0-i*512) * RoomScale, 0)
		TurnEntity(em\Obj, 90, 0, 0, True)
		EntityParent(em\Obj, r\obj)
		em\RandAngle = 15
		em\Speed = 0.05
		em\SizeChange = 0.007
		em\Achange = -0.006
		em\Gravity = -0.24
		
		r\Objects[5+i]=em\Obj
	Next
	
	;the corners of the cont chamber (needed to calculate whether the player is inside the chamber)
	r\Objects[7] = CreatePivot(r\obj)
	PositionEntity(r\Objects[7], r\x - 720 * RoomScale, 0.5, r\z + 880.0 * RoomScale, True)
	r\Objects[8] = CreatePivot(r\obj)
	PositionEntity(r\Objects[8], r\x + 176 * RoomScale, 0.5, r\z - 144.0 * RoomScale, True)			
	
	it = CreateItem("SCP-035 Addendum", "paper", r\x + 248.0 * RoomScale, r\y + 220.0 * RoomScale, r\z + 576.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Radio Transceiver", "radio", r\x - 544.0 * RoomScale, 0.5, r\z + 704.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("SCP-500-01", "scp500", r\x + 1168*RoomScale, 224*RoomScale, r\z+576*RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Metal Panel", "scp148", r\x - 360 * RoomScale, 0.5, r\z + 644 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Document SCP-035", "paper", r\x + 1168.0 * RoomScale, 104.0 * RoomScale, r\z + 608.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
End Function

Function UpdateEvent_Cont_035(e.Events)
	Local n.NPCs,do.Doors
	Local temp#,x#,distsquared#,angle#
	Local i
	
	If PlayerRoom = e\room Then
		If UpdateLever(e\room\Levers[1]\obj,False) Then 
			PositionEntity(e\room\Objects[5], EntityX(e\room\Objects[5],True), 424.0*RoomScale, EntityZ(e\room\Objects[5],True),True)
			PositionEntity(e\room\Objects[6], EntityX(e\room\Objects[6],True), 424.0*RoomScale, EntityZ(e\room\Objects[6],True),True)
		Else
			PositionEntity(e\room\Objects[5], EntityX(e\room\Objects[5],True), 10, EntityZ(e\room\Objects[5],True),True)
			PositionEntity(e\room\Objects[6], EntityX(e\room\Objects[6],True), 10, EntityZ(e\room\Objects[6],True),True)
		EndIf
		
		temp = False
		
			;player is inside the containment chamber
		If EntityX(Collider)>Min(EntityX(e\room\Objects[7],True),EntityX(e\room\Objects[8],True)) Then
			If EntityX(Collider)<Max(EntityX(e\room\Objects[7],True),EntityX(e\room\Objects[8],True)) Then
				If EntityZ(Collider)>Min(EntityZ(e\room\Objects[7],True),EntityZ(e\room\Objects[8],True)) Then
					If EntityZ(Collider)<Max(EntityZ(e\room\Objects[7],True),EntityZ(e\room\Objects[8],True)) Then
						
						ShouldPlay = 0
						
						If e\room\NPC[0]=Null Then
							If e\room\NPC[0]=Null Then e\room\NPC[0] = CreateNPC(NPCtypeTentacle, 0,0,0)
						EndIf
						
						PositionEntity e\room\NPC[0]\Collider, EntityX(e\room\Objects[4],True), 0, EntityZ(e\room\Objects[4],True)
						
						If e\room\NPC[0]\State[0] > 0 Then 
							If e\room\NPC[1]=Null Then
								If e\room\NPC[1]=Null Then e\room\NPC[1] = CreateNPC(NPCtypeTentacle, 0,0,0)
							EndIf
						EndIf
						
						Stamina = CurveValue(Min(60,Stamina), Stamina, 20.0)
						
						temp = True
						
						If e\Sound[0] = 0 Then LoadEventSound(e,"SFX\Room\035Chamber\Whispers1.ogg")
						If e\Sound[1] = 0 Then LoadEventSound(e,"SFX\Room\035Chamber\Whispers2.ogg",1)
						
						e\EventState[1] = Min(e\EventState[1]+(FPSfactor/6000),1.0)
						e\EventState[2] = CurveValue(e\EventState[1], e\EventState[2], 50)
						
						If (Not I_714\Using) And wbl\Hazmat<3 And wbl\GasMask<3 Then
							Sanity=Sanity-FPSfactor*1.1
							BlurTimer = Sin(MilliSecs()/10)*Abs(Sanity)
						EndIf
						
							;If (Not wbl\Hazmat) Then
							;	Injuries = Injuries + (FPSfactor/5000)
							;Else
							;	Injuries = Injuries + (FPSfactor/10000)
							;EndIf
						If (Not wbl\Hazmat) Then
							DamageSPPlayer(0.05, True)
						Else
							DamageSPPlayer(0.01, True)
						EndIf
						
						If KillTimer < 0 And Bloodloss =>100 Then
							m_msg\DeathTxt = GetLocalStringR("Singleplayer", "cont_035_death", Designation)
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf
		
		If e\room\NPC[1]<>Null Then 
			PositionEntity e\room\NPC[1]\Collider, EntityX(e\room\obj,True), 0, EntityZ(e\room\obj,True)
			angle = WrapAngle(EntityYaw(e\room\NPC[1]\Collider)-e\room\angle)
			
			If angle>90 Then 
				If angle < 225 Then 
					RotateEntity e\room\NPC[1]\Collider, 0, e\room\angle-89-180, 0
				Else
					RotateEntity e\room\NPC[1]\Collider, 0, e\room\angle-1, 0	
				EndIf
			EndIf
		EndIf
		
		If temp = False Then 
			e\EventState[1] = Max(e\EventState[1]-(FPSfactor/2000),0)
			e\EventState[2] = Max(e\EventState[2]-(FPSfactor/100),0)
		EndIf
		
		If e\EventState[2] > 0 And (Not I_714\Using) And wbl\Hazmat<3 And wbl\GasMask<3 Then 
			e\SoundCHN[0] = LoopSound2(e\Sound[0], e\SoundCHN[0], Camera, e\room\obj, 10, e\EventState[2])
			e\SoundCHN[1] = LoopSound2(e\Sound[1], e\SoundCHN[1], Camera, e\room\obj, 10, (e\EventState[2]-0.5)*2)
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D