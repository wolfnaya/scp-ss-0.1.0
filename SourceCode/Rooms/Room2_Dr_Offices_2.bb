
Function FillRoom_Room2_Dr_Offices_2(r.Rooms)
	Local d.Doors,de.Decals,it.Items
	
	d = CreateDoor(r\zone, r\x + 240.0 * RoomScale, 0.0, r\z + 48.0 * RoomScale, 270, r, False, False, 3)
	PositionEntity(d\buttons[0], r\x + 224.0 * RoomScale, EntityY(d\buttons[0],True), r\z + 176.0 * RoomScale,True)
	PositionEntity(d\buttons[1], r\x + 256.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True),True)			
	d\AutoClose = False : d\open = False
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x - 432.0 * RoomScale, 0.0, r\z, 90, r, False, False, 0, "1234")
	PositionEntity(r\RoomDoors[0]\buttons[0], r\x - 416.0 * RoomScale, EntityY(r\RoomDoors[0]\buttons[0],True), r\z + 176.0 * RoomScale,True)
	r\RoomDoors[0]\buttons[1] = FreeEntity_Strict(r\RoomDoors[0]\buttons[1])
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False : r\RoomDoors[0]\locked = True	
	
	de.Decals = CreateDecal(DECAL_DECAY, r\x - 808.0 * RoomScale, 0.005, r\z - 72.0 * RoomScale, 90, Rand(360), 0)
	EntityParent(de\obj, r\obj)
	de.Decals = CreateDecal(DECAL_BLOODSPLAT1, r\x - 808.0 * RoomScale, 0.01, r\z - 72.0 * RoomScale, 90, Rand(360), 0)
	de\Size = 0.3 : ScaleSprite(de\obj, de\Size, de\Size) : EntityParent(de\obj, r\obj)
	
	de.Decals = CreateDecal(DECAL_DECAY, r\x - 432.0 * RoomScale, 0.01, r\z, 90, Rand(360), 0)
	EntityParent(de\obj, r\obj)
	
	r\Objects[0] = CreatePivot(r\obj)
	PositionEntity(r\Objects[0], r\x - 808.0 * RoomScale, 1.0, r\z - 72.0 * RoomScale, True)
	
	it = CreateItem("Dr. L's Burnt Note", "paper", r\x - 688.0 * RoomScale, 1.0, r\z - 16.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Dr L's Burnt Note", "paper", r\x - 808.0 * RoomScale, 1.0, r\z - 72.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("The Modular Site Project", "paper", r\x + 622.0*RoomScale, r\y + 125.0*RoomScale, r\z - 73.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
End Function

Function UpdateEvent_Room2_Dr_Offices_2(e.Events)
	
	If PlayerRoom = e\room Then
		If e\EventState[0] = 0 Then
			If e\room\RoomDoors[0]\open = True Then 
				If e\room\RoomDoors[0]\openstate = 180 Then 
					e\EventState[0] = 1
					PlaySound_Strict HorrorSFX[5]
				EndIf
			Else
				If (EntityDistanceSquared(Collider, e\room\RoomDoors[0]\obj)<PowTwo(1.5)) And (RemoteDoorOn) Then
					e\room\RoomDoors[0]\open = True
				EndIf
			EndIf
		Else
			If EntityDistanceSquared(e\room\Objects[0], Collider) < PowTwo(2.0) Then
				HeartBeatVolume = CurveValue(0.5, HeartBeatVolume, 5)
				HeartBeatRate = CurveValue(120, HeartBeatRate, 150) 
				e\SoundCHN[0] = LoopSound2(OldManSFX[4], e\SoundCHN[0], Camera, e\room\obj, 5.0, 0.3)
				Curr106\State[0]=Curr106\State[0]-FPSfactor*3
			EndIf
			
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D