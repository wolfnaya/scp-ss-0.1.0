
Function FillRoom_Cont_079(r.Rooms)
	Local d.Doors,de.Decals
	
	d = CreateDoor(r\zone, r\x, -448.0*RoomScale, r\z + 1136.0 * RoomScale, 0, r, False,True, KEY_CARD_4)
	d\dir = 1 : d\AutoClose = False : d\open = False
	PositionEntity(d\buttons[1], r\x + 224.0 * RoomScale, -250*RoomScale, r\z + 918.0 * RoomScale, True)
	PositionEntity(d\buttons[0], r\x - 240.0 * RoomScale, -250*RoomScale, r\z + 1366.0 * RoomScale, True)
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x + 1456.0*RoomScale, -448.0*RoomScale, r\z + 976.0 * RoomScale, 0, r, False, True, KEY_CARD_3)
	r\RoomDoors[0]\dir = 1 : r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
	PositionEntity(r\RoomDoors[0]\buttons[1], r\x + 1760.0 * RoomScale, -250*RoomScale, r\z + 1236.0 * RoomScale, True)
	TurnEntity(r\RoomDoors[0]\buttons[0],0,-90-90,0,True)
	PositionEntity(r\RoomDoors[0]\buttons[0], r\x + 1760.0 * RoomScale, -240*RoomScale, r\z + 740.0 * RoomScale, True)
	TurnEntity(r\RoomDoors[0]\buttons[1],0, 90-90,0,True)
	
	CreateDoor(0, r\x + 1144.0*RoomScale, -448.0*RoomScale, r\z + 704.0 * RoomScale, 90, r, False, False, SEVERED_HAND)
	
	r\Objects[0] = LoadAnimMesh_Strict("GFX\map\rooms\cont_079\079.b3d")
	ScaleEntity(r\Objects[0], 1.3, 1.3, 1.3, True)
	PositionEntity (r\Objects[0], r\x + 1856.0*RoomScale, -560.0*RoomScale, r\z-672.0*RoomScale, True)
	EntityParent(r\Objects[0], r\obj)
	TurnEntity r\Objects[0],0,180,0,True
	
	r\Objects[1] = CreateSprite(r\Objects[0])
	SpriteViewMode(r\Objects[1],2)
	PositionEntity(r\Objects[1], 0.082, 0.119, 0.010)
	ScaleSprite(r\Objects[1],0.18*0.5,0.145*0.5)
	TurnEntity(r\Objects[1],0,13.0,0)
	MoveEntity r\Objects[1], 0,0,-0.022
	;EntityTexture (r\Objects[1],OldAiPics[0])
	
	HideEntity r\Objects[1]
	
	r\Objects[2] = CreatePivot(r\obj)
	PositionEntity (r\Objects[2], r\x + 1184.0*RoomScale, -448.0*RoomScale, r\z+1792.0*RoomScale, True)
	
	de.Decals = CreateDecal(DECAL_BLOODSPLAT2,  r\x + 1184.0*RoomScale, -448.0*RoomScale+0.01, r\z+1792.0*RoomScale,90,Rnd(360),0)
	de\Size = 0.5
	ScaleSprite(de\obj, de\Size,de\Size)
	EntityParent de\obj, r\obj
	
End Function

Function UpdateEvent_Cont_079(e.Events)
	Local e2.Events, it.Items, pvt%
	
	;[Block]
	If PlayerRoom = e\room Then
		
		If e\EventState[0] = 0 Then
			
			If TaskExists(TASK_SEARCH_FOR_CONT_079) Then
				EndTask(TASK_SEARCH_FOR_CONT_079)
				BeginTask(TASK_SEARCH_FOR_GENERATOR)
			EndIf
			
			e\room\NPC[0]=CreateNPC(NPC_Guard, EntityX(e\room\Objects[2],True)+0.25, EntityY(e\room\Objects[2],True)+0.5,EntityZ(e\room\Objects[2],True)-0.25)
			PointEntity e\room\NPC[0]\Collider, e\room\obj
			RotateEntity e\room\NPC[0]\Collider, 0, EntityYaw(e\room\NPC[0]\Collider),0, True
			SetNPCFrame(e\room\NPC[0], 288)
			e\room\NPC[0]\State[0] = 8
			;RemoveNPCGun(e\room\NPC[0])
			
			it = CreateItem("H&K USP", "usp",EntityX(e\room\Objects[2],True)+0.15,EntityY(e\room\Objects[2],True)+0.5,EntityZ(e\room\Objects[2],True)+0.35)
			RotateEntity it\collider,EntityPitch(it\collider),45,EntityRoll(it\collider),True
			it\state = Rand(6,12)
			it\state2 = 15
			EntityType it\collider, HIT_ITEM
			
			e\EventState[0] = 1
		EndIf
		
		ShouldPlay = MUS_CONT_079
		
		If (Not ecst\KilledGuard) Then 
			If e\room\RoomDoors[0]\open Then 
				If e\room\RoomDoors[0]\openstate > 50 Or EntityDistance(Collider, e\room\RoomDoors[0]\frameobj)<0.5 Then
					e\room\RoomDoors[0]\openstate = Min(e\room\RoomDoors[0]\openstate,50)
					e\room\RoomDoors[0]\open = False
					PlaySound_Strict(LoadTempSound("SFX\Door\DoorError.ogg"))
				EndIf							
			EndIf
		Else
			If e\room\RoomDoors[0]\open Then
				e\room\RoomDoors[0]\locked = True
				e\EventState[0] = e\EventState[0] + FPSfactor
				e\EventState[1] = 1
			EndIf
			If e\EventState[0] >= 70*0.00101 And e\EventState[0] < 70*0.00101
				e\SoundCHN[0] = StreamSound_Strict("SFX\SCP\079\Speech.ogg",opt\SFXVolume,0)
				e\SoundCHN_isStream[0] = True
			EndIf
			If e\EventState[1] = 1 Then
				GiveAchievement(Achv079)
				
				pvt = CreatePivot()
				PositionEntity(pvt,EntityX(Collider),EntityY(Collider),EntityZ(Collider))
				
				PointEntity pvt, e\room\Objects[0]
				RotateEntity Collider, 0, CurveAngle(EntityYaw(pvt),EntityYaw(Collider),100.0), 0
				
				psp\NoMove = True
				psp\NoRotation = True
				
				If IsStreamPlaying_Strict(e\SoundCHN[0])
					If Rand(3) = 1 Then
						;EntityTexture(e\room\Objects[1], OldAiPics[0])
						ShowEntity (e\room\Objects[1])
					ElseIf Rand(10) = 1 
						HideEntity (e\room\Objects[1])							
					EndIf							
				Else
					If e\SoundCHN[0]<>0
						StopStream_Strict(e\SoundCHN[0]) : e\SoundCHN[0]=0
					EndIf
					;EntityTexture(e\room\Objects[1], OldAiPics[1])
					ShowEntity (e\room\Objects[1])
				EndIf
				
			EndIf
			If e\EventState[0] >= 70*15 And e\EventState[0] < 70*15.01 Then
				e\EventState[1] = 2
				FreeEntity_Strict pvt
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D