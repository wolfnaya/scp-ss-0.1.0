
Function Fillroom_Room2_Toilets(r.Rooms)
	
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x + 1040.0 * RoomScale, 192.0 * RoomScale, r\z)
	EntityParent(r\Objects[0], r\obj)
	
	r\Objects[1] = CreatePivot()
	PositionEntity(r\Objects[1], r\x + 1530.0*RoomScale, 0.5, r\z+512.0*RoomScale)
	EntityParent(r\Objects[1], r\obj)
	
	r\Objects[2] = CreatePivot()
	PositionEntity(r\Objects[2], r\x + 1535.0*RoomScale, r\y+150.0*RoomScale, r\z+512.0*RoomScale)
	EntityParent(r\Objects[2], r\obj)
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x+240*RoomScale,r\y,r\z,90,r,False,DOOR_OFFICE)
	
End Function

Function UpdateEvent_Buttghost(e.Events)
	
	If PlayerRoom = e\room Then
		If EntityDistanceSquared(Collider, e\room\Objects[0]) < PowTwo(1.8) Then
			If e\EventState[0] = 0
				GiveAchievement(Achv789)
				e\SoundCHN[0] = PlaySound2(ButtGhostSFX, Camera,e\room\Objects[0])
				e\EventState[0] = 1
			Else
				If (Not ChannelPlaying(e\SoundCHN[0]))
					RemoveEvent(e)
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

Function UpdateEvent_Toilet_Guard(e.Events)
	Local de.Decals, it.Items
	
	If e\EventState[0] = 0 Then
		If e\room\dist < 8.0  And e\room\dist > 0 Then
			e\room\RoomDoors[0]\open = True
			e\EventState[0] = 1
		EndIf
	ElseIf e\EventState[0] = 1
		e\room\NPC[0]=CreateNPC(NPC_Guard, EntityX(e\room\Objects[1],True), EntityY(e\room\Objects[1],True)+0.5, EntityZ(e\room\Objects[1],True))
		PointEntity e\room\NPC[0]\Collider, e\room\obj
		RotateEntity e\room\NPC[0]\Collider, 0, EntityYaw(e\room\NPC[0]\Collider)-20,0, True
		
		SetNPCFrame (e\room\NPC[0], 287)
		e\room\NPC[0]\State[0] = 8
		
		e\EventState[0] = 2	
	Else
		If e\Sound[0] = 0 Then e\Sound[0] = LoadSound_Strict("SFX\Character\Guard\SuicideGuard1.ogg")
		If e\room\dist < 15.0 And e\room\dist >= 4.0 Then 
			e\SoundCHN[0] = LoopSound2(e\Sound[0], e\SoundCHN[0], Camera, e\room\NPC[0]\Collider, 15.0)
			
		ElseIf e\room\dist<4.0 And PlayerSoundVolume > 1.0
			If e\EventState[1]=0
				de.Decals = CreateDecal(DECAL_BLOODSPLAT2,  EntityX(e\room\Objects[2],True), EntityY(e\room\Objects[2],True), EntityZ(e\room\Objects[2],True),0,e\room\angle+270,0)
				de\Size = 0.3 : ScaleSprite (de\obj, de\Size, de\Size)
				
				Local bone% = FindChild(e\room\NPC[0]\obj,"Hand.L")
				it = CreateItem("FN P90", "p90",EntityX(bone%,True)+0.15,EntityY(bone%,True),EntityZ(bone%,True))
				RotateEntity it\collider,EntityPitch(it\collider,True),e\room\angle+45,EntityRoll(it\collider,True),True
				RotateEntity it\model,EntityPitch(it\model,True),EntityYaw(it\model,True),EntityRoll(it\model,True)+180,True
				MoveEntity it\model, 0,-0.025,0
				it\state = 49
				EntityType it\collider, HIT_ITEM
				RemoveNPCGun(e\room\NPC[0])
				
				e\EventState[1] = 1
			EndIf
			If e\SoundCHN[1] = 0
				StopChannel(e\SoundCHN[0])
				FreeSound_Strict(e\Sound[0])
				e\room\NPC[0]\Sound = LoadSound_Strict("SFX\Character\Guard\SuicideGuard2.ogg")
				e\SoundCHN[1] = PlaySound2(e\room\NPC[0]\Sound, Camera, e\room\NPC[0]\Collider, 15.0)
			EndIf
			UpdateSoundOrigin(e\SoundCHN[1],Camera,e\room\NPC[0]\Collider,15.0)
			If (Not ChannelPlaying(e\SoundCHN[1])) Then RemoveEvent(e)
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D