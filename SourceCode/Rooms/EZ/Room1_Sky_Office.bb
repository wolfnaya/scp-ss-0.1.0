
Function FillRoom_Room1_Sky_Office(r.Rooms)
	Local it.Items,i
	
	; ~ SCP-294
	
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x-1734.0*RoomScale,r\y+1293.0*RoomScale,r\z-2802*RoomScale, True)
	
	; ~ SCP-294's cups spawnpoint
	
	r\Objects[1] = CreatePivot()
	PositionEntity(r\Objects[1], r\x-1676.0*RoomScale,r\y+1346.0*RoomScale,r\z-2848*RoomScale, True)
	
	r\Objects[2] = CreatePivot()
	PositionEntity(r\Objects[2], r\x-81.0*RoomScale,r\y+1766.0*RoomScale,r\z-1024*RoomScale, True)
	
	For i = 0 To 2
		EntityParent r\Objects[i],r\obj
	Next
	
	it = CreateItem(GetLocalString("Item Names","cup"), "cup", r\x-1124.0*RoomScale,r\y+1399*RoomScale,r\z-1426.0*RoomScale, 240,175,70)
	EntityParent(it\collider, r\obj) : it\name = "Cup of Orange Juice"
	
	it = CreateItem(GetLocalString("Item Names","cup"), "cup", r\x-887*RoomScale,r\y+1366*RoomScale,r\z-2709.0*RoomScale, 87,62,45)
	EntityParent(it\collider, r\obj) : it\name = "Cup of Coffee"
	
	it = CreateItem(GetLocalString("Item Names","cup_empty"), "emptycup", r\x-108*RoomScale, r\y+161*RoomScale, r\z-343.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","coin_rusty"), "25ct", r\x-1600.0*RoomScale,r\y+1406.0*RoomScale,r\z-1468.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem(GetLocalString("Item Names","coin_rusty"), "25ct", r\x-1600.0*RoomScale,r\y+1406.0*RoomScale,r\z-1468.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x-403*RoomScale,r\y+1597*RoomScale,r\z-2048*RoomScale,90,r,False,DOOR_OFFICE)
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x,r\y+1597*RoomScale,r\z-3061*RoomScale,0,r,False,DOOR_OFFICE_2,KEY_CARD_4)
	r\RoomDoors[1]\locked = True
	
	r\RoomDoors[2] = CreateDoor(r\zone,r\x,r\y+1597*RoomScale,r\z-1024*RoomScale,0,r,False,DOOR_EZ,KEY_CARD_2)
	
	r\RoomDoors[3] = CreateDoor(r\zone,r\x,r\y+799*RoomScale,r\z-1011*RoomScale,0,r,False,DOOR_EZ,KEY_CARD_3)
	r\RoomDoors[3]\locked = True
	
End Function

Function UpdateEvent_Room1_Sky_Office(e.Events)
	Local it.Items,p.Particles
	Local temp%,i
	
	If PlayerRoom = e\room Then
		
		If Sky = 0 Then
			Sky = sky_CreateSky("GFX\map\sky\sky_day")
			RotateEntity Sky,0,e\room\angle,0
		EndIf
		
		UpdateSky(Sky)
		
		If e\EventState[0] = 0 Then
			e\Sound[0] = LoadSound_Strict("SFX\General\Spark_Short.ogg")
			e\EventState[0] = 1
		EndIf
		
		If e\EventState[0] = 1 Then
			If e\room\RoomDoors[2]\open Then
				If Rand(2) = 1 Then
					e\SoundCHN[0] = PlaySound2(LoadTempSound("SFX\Door\DoorSparks.ogg"),Camera,e\room\RoomDoors[2]\frameobj,10,2.5)
				Else
					e\SoundCHN[0] = PlaySound2(LoadTempSound("SFX\General\Spark_medium.ogg"),Camera,e\room\RoomDoors[2]\frameobj,10,2.5)
				EndIf
				StopChannel(e\room\RoomDoors[2]\SoundCHN)
				e\EventState[0] = 2
			EndIf
		ElseIf e\EventState[0] = 2 Then
			UpdateSoundOrigin(e\SoundCHN[0],Camera,e\room\RoomDoors[2]\frameobj,10,2.5)
			e\room\RoomDoors[2]\openstate = 0.1
			e\room\RoomDoors[2]\open = False
			
			p.Particles = CreateParticle(EntityX(e\room\Objects[2],True), EntityY(e\room\Objects[2],True)+Rnd(0.0,0.05), EntityZ(e\room\Objects[2],True),6,0.25,-0.2)
			EntityColor p\obj,100,100,100
			RotateEntity(p\obj, Rnd(-45,0), EntityYaw(p\obj)+Rnd(-10.0,10.0), 0)
			p\Achange = -Rnd(0.02,0.03)
			For i = 0 To 1
				p.Particles = CreateParticle(EntityX(e\room\Objects[2],True), EntityY(e\room\Objects[2],True)+Rnd(0.0,0.05), EntityZ(e\room\Objects[2],True)+Rnd(-0.2,0.2),6,0.15,-0.1)
				EntityColor p\obj,100,100,100
				RotateEntity(p\obj, Rnd(-45,0), EntityYaw(p\obj)+Rnd(-10.0,10.0), 0)
				p\Achange = -Rnd(0.02,0.03)
			Next
			
			If ParticleAmount > 0 Then
				If Rand(10)=1 Then
					PlaySound2(e\Sound[0], Camera, e\room\Objects[2], 3.0, 0.4)
					Local pvt% = CreatePivot()
					PositionEntity(pvt, EntityX(e\room\Objects[2],True), EntityY(e\room\Objects[2],True)+Rnd(0.0,0.05), EntityZ(e\room\Objects[2],True))
					RotateEntity(pvt, 0, EntityYaw(e\room\Objects[2],True), 0)
					MoveEntity pvt,0,0,0.2
					
					For i = 0 To (1+(2*(ParticleAmount-1)))
						p.Particles = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 7, 0.002, 0, 25)
						p\speed = Rnd(0.01,0.05)
						RotateEntity(p\pvt, Rnd(-45,0), EntityYaw(pvt)+Rnd(-10.0,10.0), 0)
						p\size = 0.0075
						ScaleSprite p\obj,p\size,p\size
						p\Achange = -0.05
					Next
					
					pvt = FreeEntity_Strict(pvt)
				EndIf
			EndIf
			
		EndIf
		If e\EventState[0] = 2 And (Not ChannelPlaying(e\SoundCHN[0])) Then
			OpenCloseDoor(e\room\RoomDoors[2])
			e\room\RoomDoors[2]\locked = True
			e\EventState[0] = 3
		EndIf
		
		If (Not Using294) Then
			If EntityDistanceSquared(e\room\Objects[0], Collider)<PowTwo(1.5) Then
				GiveAchievement(Achv294)
				If EntityInView(e\room\Objects[0], Camera) Then
					DrawHandIcon = True
					If KeyHitUse Then
						temp = True
						For it.Items = Each Items
							If it\Picked=False Then
								If EntityX(it\collider)-EntityX(e\room\Objects[1],True)=0 Then
									If EntityZ(it\collider)-EntityZ(e\room\Objects[1],True)=0 Then
										temp = False
										Exit
									EndIf
								EndIf
							EndIf
						Next
						Local inserted% = False
						If e\EventState[1] < 2 Then
							If SelectedItem<>Null Then
								If SelectedItem\itemtemplate\tempname="25ct" Lor SelectedItem\itemtemplate\tempname="coin" Then
									RemoveItem(SelectedItem)
									SelectedItem=Null
									e\EventState[1] = e\EventState[1] + 1
									PlaySound_Strict LoadTempSound("SFX\SCP\294\coin_drop.ogg")
									inserted = True
									If IsVaneCoinDropped Then
										IsVaneCoinDropped = False
									EndIf
								ElseIf SelectedItem\itemtemplate\tempname="vanecoin" Then
									RemoveItem(SelectedItem)
									SelectedItem=Null
									e\EventState[1] = 2
									PlaySound_Strict LoadTempSound("SFX\SCP\294\vane_coin_drop.ogg")
									inserted = True
									If (Not IsVaneCoinDropped) Then
										IsVaneCoinDropped = True
									EndIf
								EndIf
							EndIf
						EndIf
						If e\EventState[1] = 2 Then
							Using294=temp
							If Using294 Then MouseHit1=False
						ElseIf e\EventState[1] = 1 And (Not inserted) Then
							Using294=False
							CreateMsg(GetLocalString("Items","scp294_2"))
						ElseIf (Not inserted) Then
							Using294=False
							CreateMsg(GetLocalString("Items","scp294_3"))
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf
	Else
		If Sky <> 0 Then Sky = FreeEntity_Strict(Sky)
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D