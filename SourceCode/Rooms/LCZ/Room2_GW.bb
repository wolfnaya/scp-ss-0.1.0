
Function FillRoom_Room2_GW(r.Rooms)
	Local de.Decals,r2.Rooms,i
	
	If r\RoomTemplate\Name = "room2_gw_b" Then
		r\Objects[2] = CreatePivot(r\obj)
		PositionEntity (r\Objects[2], r\x - 156.825*RoomScale, -37.3458*RoomScale, r\z+121.364*RoomScale, True)
		
		de.Decals = CreateDecal(DECAL_BLOODSPLAT2,  r\x - 156.825*RoomScale, 0.0005, r\z+121.364*RoomScale,90,Rnd(360),0)
		de\Size = 0.5
		ScaleSprite(de\obj, de\Size,de\Size)
		EntityParent de\obj, r\obj
		
		r\Objects[0] = CreatePivot()
		PositionEntity r\Objects[0],r\x+280.0*RoomScale,r\y+345.0*RoomScale,r\z-340.0*RoomScale,True
		EntityParent r\Objects[0],r\obj
	EndIf
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x + 336.0 * RoomScale, 0.0, r\z - 382.0 * RoomScale, 0, r, False, DOOR_WINDOWED)
	For i = 0 To 1 
		r\RoomDoors[0]\buttons[i] = FreeEntity_Strict(r\RoomDoors[0]\buttons[i])
	Next
	r\RoomDoors[0]\dir = 0 : r\RoomDoors[0]\AutoClose = False	: r\RoomDoors[0]\open = True  : r\RoomDoors[0]\locked = True	
	r\RoomDoors[0]\MTFClose = False
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x + 336.0 * RoomScale, 0.0, r\z + 462.0 * RoomScale, 180, r, False, DOOR_WINDOWED)
	For i = 0 To 1 
		r\RoomDoors[1]\buttons[i] = FreeEntity_Strict(r\RoomDoors[0]\buttons[i])
	Next
	r\RoomDoors[1]\dir = 0 : r\RoomDoors[1]\AutoClose = False	: r\RoomDoors[1]\open = True  : r\RoomDoors[1]\locked = True
	r\RoomDoors[1]\MTFClose = False
	
	For r2.Rooms = Each Rooms
		If r2<>r Then
			If r2\RoomTemplate\Name = "room2_gw_a" Lor r2\RoomTemplate\Name = "room2_gw_b" Then
				r\Objects[3] = CopyEntity(r2\Objects[3],r\obj) ;don't load the mesh again
				Exit
			EndIf
		EndIf
	Next
	If r\Objects[3]=0 Then r\Objects[3] = LoadRMesh("GFX\map\room2gw_pipes_opt.rmesh",Null)
	EntityParent r\Objects[3],r\obj
	EntityPickMode r\Objects[3],2
	
	If r\RoomTemplate\Name = "room2_gw_a"
		r\Objects[0] = CreatePivot()
		PositionEntity r\Objects[0],r\x+344.0*RoomScale,128.0*RoomScale,r\z
		EntityParent r\Objects[0],r\obj
	EndIf
	
End Function

Function UpdateEvent_Room2_GW_B(e.Events)
	Local p.Particles
	
	If e\room\dist < 8 Then
		p.Particles = CreateParticle(EntityX(e\room\Objects[0],True), EntityY(e\room\Objects[0],True), EntityZ(e\room\Objects[0],True), 6, 0.2, 0, 10)
		p\speed = 0.01
		RotateEntity(p\pvt, -60, e\room\angle-90, 0)
		
		p\Achange = -0.02
		
		e\SoundCHN[0] = LoopSound2(AlarmSFX[3],e\SoundCHN[0],Camera,e\room\Objects[3],5)
	EndIf
	
End Function

Function CreateEvent_Room2_GW_B(e.Events)
	
	e\room\NPC[0]=CreateNPC(NPC_Guard, EntityX(e\room\Objects[2],True), EntityY(e\room\Objects[2],True)+0.5, EntityZ(e\room\Objects[2],True))
	PointEntity e\room\NPC[0]\Collider, e\room\obj
	RotateEntity e\room\NPC[0]\Collider, 0, EntityYaw(e\room\NPC[0]\Collider),0, True
	SetNPCFrame(e\room\NPC[0], 288)
	e\room\NPC[0]\State[0] = 8
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS