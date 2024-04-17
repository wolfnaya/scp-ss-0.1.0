
Function FillRoom_Checkpoint_ACZ(r.Rooms)
	Local d.Doors,d2.Doors,it.Items
	Local i%,ne.NewElevator,fb.FuseBox
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x,r\y,r\z - 400.0*RoomScale,180,r,True,DOOR_WINDOWED,False,"1234")
	r\RoomDoors[2] = CreateDoor(r\zone,r\x,r\y,r\z + 400.0*RoomScale,0,r,False,DOOR_WINDOWED,False,"1234")
	For i = 0 To 1
		FreeEntity r\RoomDoors[1]\buttons[i] : r\RoomDoors[1]\buttons[i] = 0
		FreeEntity r\RoomDoors[2]\buttons[i] : r\RoomDoors[2]\buttons[i] = 0
	Next
	
	r\RoomDoors[1]\open = True
	r\RoomDoors[2]\open = False
	
	r\Objects[11] = CreateButton(r\x - 125.0 * RoomScale, r\y + 134 * RoomScale, r\z, 430, 90, 0)
	EntityParent(r\Objects[11],r\obj)
	
	r\RoomDoors[11] = CreateDoor(r\zone,r\x+753*RoomScale,r\y,r\z -673.0*RoomScale,90,r,False,DOOR_OFFICE)
	r\RoomDoors[11]\locked = True
	
	r\RoomDoors[12] = CreateDoor(r\zone,r\x - 768.0*RoomScale,r\y,r\z + 672.0*RoomScale,90,r,False,DOOR_OFFICE_2)
	r\RoomDoors[12]\locked = True
	
	r\Levers[7] = CreateLever(r, r\x -200.0 * RoomScale, r\y +211.0 * RoomScale, r\z +318 * RoomScale,270,False)
	
	r\Objects[10] = CreatePivot()
	PositionEntity r\Objects[10],r\x,128*RoomScale,r\z
	EntityParent r\Objects[10],r\obj
	
	r\RoomDoors[5] = CreateDoor(r\zone,r\x+1265*RoomScale,r\y,r\z+1392.0*RoomScale,90,r,False,DOOR_OFFICE)
	r\RoomDoors[5]\locked = True
	r\RoomDoors[6] = CreateDoor(r\zone,r\x+1265*RoomScale,r\y,r\z+912.0*RoomScale,270,r,False,DOOR_OFFICE)
	r\RoomDoors[6]\locked = True
	
	r\RoomDoors[10] = CreateDoor(r\zone,r\x-480*RoomScale,r\y,r\z -400.0*RoomScale,0,r,False,DOOR_OFFICE)
	
	;it = CreateItem("S&W Model 500", "sw500",r\x-30*RoomScale,1.0,r\z+700*RoomScale)
	;it\state = 5 : it\state2 = 45
	;EntityParent it\collider, r\obj
	
	it = CreateItem(GetLocalString("Item Names","scramble"), "scramble",r\x-26*RoomScale,1.0,r\z+806*RoomScale)
	it\state = 1000
	EntityParent it\collider, r\obj
	
	it = CreateItem("Strange Message", "paper",r\x-26*RoomScale,1.0,r\z+806*RoomScale)
	EntityParent it\collider, r\obj
	
End Function

Function UpdateEvent_Checkpoint_ACZ(e.Events)
	Local ne.NewElevator,r.Rooms,e2.Events
	Local playerElev%,prevZone%, pvt%, pvt2%,n.NPCs,de.Decals,i,it.Items
	
	Local p.Particles
	
	If PlayerRoom = e\room
		
		If e\EventState[12] = 0.0
			
			UpdateButton(e\room\Objects[11])
			
			If EntityDistanceSquared(e\room\Objects[10],Collider)<PowTwo(1.4) And e\EventState[10] = 0.0
				If d_I\ClosestButton = e\room\Objects[11] Then
					If keyhituse Then
						PlaySound_Strict(ButtonSFX[0])
						e\EventState[12] = 1.0
						StopChannel e\SoundCHN[1]
						e\SoundCHN[1] = 0
						e\room\RoomDoors[1]\locked = False
						e\room\RoomDoors[2]\locked = False
						
						If e\room\RoomDoors[1]\open Then
							e\EventState[9] = 0
						EndIf
						If e\room\RoomDoors[2]\open Then
							e\EventState[9] = 1
						EndIf
						
						If e\EventState[9] = 0.0 Then
							UseDoor(e\room\RoomDoors[1])
						Else
							UseDoor(e\room\RoomDoors[2])
						EndIf
						
						PlaySound_Strict(AlarmSFX[6])
					EndIf
				EndIf
			EndIf
		Else
			If e\EventState[11] < 70*7
				e\EventState[11] = e\EventState[11] + FPSfactor
				e\room\RoomDoors[1]\open = False
				e\room\RoomDoors[2]\open = False
				If e\EventState[11] < 70*1
					
					
				ElseIf e\EventState[11] > 70*3 And e\EventState[9] < 70*5.5
					pvt% = CreatePivot(e\room\obj)								
					For i = 0 To 3
						
						If i = 0
							PositionEntity pvt%,-96.0,318.0,176.0,False
						ElseIf i = 1
							PositionEntity pvt%,96.0,318.0,176.0,False
						ElseIf i = 2
							PositionEntity pvt%,-96.0,318.0,-176.0,False
						Else
							PositionEntity pvt%,96.0,318.0,-176.0,False
						EndIf
						
						p.Particles = CreateParticle(EntityX(pvt,True), EntityY(pvt,True), EntityZ(pvt,True),  0, 0.4, 0, 50)
						p\speed = 0.025
						RotateEntity(p\pvt, 90, 0, 0)
						
						p\Achange = -0.02
					Next
					
					FreeEntity pvt
					If e\SoundCHN[1] = 0 Then e\SoundCHN[1] = PlaySound2(AirlockSFX[1],Camera,e\room\Objects[10],5)
				EndIf
			Else
				
				e\EventState[12] = 0.0
				e\EventState[11] = 0.0
				e\EventState[10] = 1.0
				If e\room\RoomDoors[1]\open = False
					e\room\RoomDoors[1]\locked = True
					e\room\RoomDoors[2]\locked = True
					
					If e\EventState[9] = 0.0 Then
						UseDoor(e\room\RoomDoors[2])
						If e\Sound[0] = 0 Then LoadEventSound(e,"SFX\Alarm\Airlock\Decont_Error.ogg")
						If (Not ChannelPlaying(e\SoundCHN[2])) Then e\SoundCHN[2] = PlaySound2(e\Sound[0],Camera,e\room\RoomDoors[2]\obj)
						e\Sound[0] = 0
					Else
						UseDoor(e\room\RoomDoors[1])
					EndIf
					
					e\room\RoomDoors[1]\open = True
					e\room\RoomDoors[2]\open = True
					
				EndIf
			EndIf
		EndIf
		
		If ChannelPlaying(e\SoundCHN[1])
			UpdateSoundOrigin(e\SoundCHN[1],Camera,e\room\Objects[10],5)
		EndIf
		
	EndIf
	
	If e\EventState[10] > 0 Then
		If e\room\dist < 6.0 Then
			If e\room\RoomDoors[1]\locked = True Then
				If (Not e\SoundCHN[0]) Then
					e\SoundCHN[0] = PlaySound_Strict(AlarmSFX[9])
				Else
					If (Not ChannelPlaying(e\SoundCHN[0])) Then e\SoundCHN[0] = PlaySound_Strict(AlarmSFX[9])
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D