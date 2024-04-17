
Function FillRoom_Room2_Servers_1(r.Rooms)
	Local d.Doors
	Local i,n
	
	d.Doors = CreateDoor(0, r\x,0,r\z, 0, r, False, 2, False)
	d\locked = True
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x - 208.0 * RoomScale, 0.0, r\z - 736.0 * RoomScale, 90, r, True, False)
	PositionEntity(r\RoomDoors[0]\buttons[0], r\x - 198.0 * RoomScale, EntityY(r\RoomDoors[0]\buttons[0],True), EntityZ(r\RoomDoors[0]\buttons[0],True), True)
	PositionEntity(r\RoomDoors[0]\buttons[1], r\x - 218.0 * RoomScale, EntityY(r\RoomDoors[0]\buttons[1],True), EntityZ(r\RoomDoors[0]\buttons[1],True), True)
	r\RoomDoors[0]\AutoClose=False
	r\RoomDoors[1] = CreateDoor(r\zone, r\x - 208.0 * RoomScale, 0.0, r\z + 736.0 * RoomScale, 90, r, True, False)
	PositionEntity(r\RoomDoors[1]\buttons[0], r\x - 198.0 * RoomScale, EntityY(r\RoomDoors[1]\buttons[0],True), EntityZ(r\RoomDoors[1]\buttons[0],True), True)
	PositionEntity(r\RoomDoors[1]\buttons[1], r\x - 218.0 * RoomScale, EntityY(r\RoomDoors[1]\buttons[1],True), EntityZ(r\RoomDoors[1]\buttons[1],True), True)
	r\RoomDoors[1]\AutoClose=False
	
	r\RoomDoors[2] = CreateDoor(r\zone, r\x - 672.0 * RoomScale, 0.0, r\z - 1024.0 * RoomScale, 0, r, False, False, False, "GEAR")
	r\RoomDoors[2]\AutoClose=False : r\RoomDoors[2]\DisableWaypoint = True
	For i = 0 To 1
		r\RoomDoors[2]\buttons[i] = FreeEntity_Strict(r\RoomDoors[2]\buttons[i])
	Next
	
	r\Levers[0] = CreateLever(r, r\x - 1260.0 * RoomScale, r\y + 234.0 * RoomScale, r\z + 750 * RoomScale, 0, False)
	r\Levers[1] = CreateLever(r, r\x - 920.0 * RoomScale, r\y + 164.0 * RoomScale, r\z + 898 * RoomScale, 0, True)
	r\Levers[2] = CreateLever(r, r\x - 837.0 * RoomScale, r\y + 152.0 * RoomScale, r\z + 886 * RoomScale, 0, True)
	
	;096 spawnpoint
	r\Objects[6]=CreatePivot(r\obj)
	PositionEntity(r\Objects[6],r\x-320*RoomScale,0.5,r\z,True)
	;guard spawnpoint
	r\Objects[7]=CreatePivot(r\obj)
	PositionEntity(r\Objects[7], r\x - 1328.0 * RoomScale, 0.5, r\z + 528*RoomScale, True)
	;the point where the guard walks to
	r\Objects[8]=CreatePivot(r\obj)
	PositionEntity(r\Objects[8], r\x - 1376.0 * RoomScale, 0.5, r\z + 32*RoomScale, True)
	
	r\Objects[9]=CreatePivot(r\obj)
	PositionEntity(r\Objects[9], r\x - 848*RoomScale, 0.5, r\z+576*RoomScale, True)
	
End Function

Function UpdateEvent_Room2_Servers_1(e.Events)
	Local de.Decals
	Local x%,z%,temp%
	Local i
	
	If PlayerRoom = e\room Then
		x = UpdateLever(e\room\Levers[1]\obj) ;fuel pump
		z = UpdateLever(e\room\Levers[2]\obj) ;generator
		temp = UpdateLever(e\room\Levers[0]\obj) ;power switch
		
		If e\EventState[0] = 0
			For i = 0 To 6
				If e\room\angle = 0 Lor e\room\angle = 180 Then
					de.Decals = CreateDecal(GetRandomDecalID(DECAL_TYPE_BLOODSPLAT), e\room\x-Rnd(197,199)*Cos(e\room\angle)*RoomScale, 1.0, e\room\z+(140.0*(i-3))*RoomScale,0,e\room\angle+90,Rnd(360))
					de\Size = Rnd(0.8,0.85) : de\SizeChange = 0.001
					de.Decals = CreateDecal(GetRandomDecalID(DECAL_TYPE_BLOODSPLAT), e\room\x-Rnd(197,199)*Cos(e\room\angle)*RoomScale, 1.0, e\room\z+(140.0*(i-3))*RoomScale,0,e\room\angle-90,Rnd(360))
					de\Size = Rnd(0.8,0.85) : de\SizeChange = 0.001
				Else
					de.Decals = CreateDecal(GetRandomDecalID(DECAL_TYPE_BLOODSPLAT), e\room\x+(140.0*(i-3))*RoomScale, 1.0, e\room\z-Rnd(197,199)*Sin(e\room\angle)*RoomScale-Rnd(0.001,0.003),0,e\room\angle+90,Rnd(360))
					de\Size = Rnd(0.8,0.85) : de\SizeChange = 0.001
					de.Decals = CreateDecal(GetRandomDecalID(DECAL_TYPE_BLOODSPLAT), e\room\x+(140.0*(i-3))*RoomScale, 1.0, e\room\z-Rnd(197,199)*Sin(e\room\angle)*RoomScale-Rnd(0.001,0.003),0,e\room\angle-90,Rnd(360))
					de\Size = Rnd(0.8,0.85) : de\SizeChange = 0.001
				EndIf
			Next
			de\Size = Rnd(0.5,0.7)
			ScaleSprite(de\obj, de\Size,de\Size)
			
			e\EventState[0] = 1
		EndIf
		
		;fuel pump on
		If x Then
			e\EventState[1] = Min(1.0, e\EventState[1]+FPSfactor/350)
			
			;generator on
			If z Then
				If e\Sound[1]=0 Then LoadEventSound(e,"SFX\General\GeneratorOn.ogg",1)
				e\EventState[2] = Min(1.0, e\EventState[2]+FPSfactor/450)
			Else
				e\EventState[2] = Min(0.0, e\EventState[2]-FPSfactor/450)
			EndIf
		Else
			e\EventState[1] = Max(0, e\EventState[1]-FPSfactor/350)
			e\EventState[2] = Max(0, e\EventState[2]-FPSfactor/450)
		EndIf
		
		If e\EventState[1]>0 Then e\SoundCHN[0]=LoopSound2(RoomAmbience[8], e\SoundCHN[0], Camera, e\room\Levers[1]\objBase, 5.0, e\EventState[1]*0.8)
		If e\EventState[2]>0 Then e\SoundCHN[1]=LoopSound2(e\Sound[1], e\SoundCHN[1], Camera, e\room\Levers[2]\objBase, 6.0, e\EventState[2])
		
		If temp=0 And x And z Then
			e\room\RoomDoors[0]\locked = False
			e\room\RoomDoors[1]\locked = False
		Else
			If Rand(200)<5 Then LightBlink = Rnd(0.5,1.0)
			
			If e\room\RoomDoors[0]\open Then 
				e\room\RoomDoors[0]\locked = False
				UseDoor(e\room\RoomDoors[0],False) 
			EndIf
			If e\room\RoomDoors[1]\open Then 
				e\room\RoomDoors[1]\locked = False
				UseDoor(e\room\RoomDoors[1],False)
			EndIf
			e\room\RoomDoors[0]\locked=True
			e\room\RoomDoors[1]\locked=True							
		EndIf 
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D