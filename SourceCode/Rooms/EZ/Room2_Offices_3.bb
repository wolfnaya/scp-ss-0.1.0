Function FillRoom_Room2_Offices_3(r.Rooms)
	Local it.Items,r2.Rooms,i
	Local firstRoom% = True
	
	For r2 = Each Rooms
		If r2\RoomTemplate\Name = r\RoomTemplate\Name And r2 <> r Then
			firstRoom = False
			Exit
		EndIf
	Next
	
	r\Objects[3] = CreatePivot()
	PositionEntity(r\Objects[3],r\x,r\y+1.0,r\z-50*RoomScale,True)
	EntityParent(r\Objects[3], r\obj)
	
	InitFluLight(0,FLU_STATE_OFF,r)
	
	If firstRoom Then
		
		If gopt\GameMode = GAMEMODE_DEFAULT Then
			it = CreateItem("SCP-035","scp035",r\x,1.0,r\z)
			EntityParent it\collider, r\obj
		EndIf
			
		r\Objects[0] = LoadRMesh("GFX\map\rooms\room2_offices_3\room2_offices_3_obj_1.rmesh",Null)
		
		ScaleEntity (r\Objects[0],RoomScale,RoomScale,RoomScale)
		EntityType GetChild(r\Objects[0],2), HIT_MAP
		EntityPickMode GetChild(r\Objects[0],2), 2
		PositionEntity(r\Objects[0],r\x,r\y,r\z,True)
		EntityParent(r\Objects[0], r\obj)
		
		it = CreateItem(GetLocalString("Item Names","crowbar"), "crowbar", r\x + 403.0 * RoomScale, r\y + 460.0 * RoomScale, r\z + 862.0 * RoomScale); ~ TODO: Add Axe weapon
		EntityParent(it\collider, r\obj)
		
		InitFluLight(1,FLU_STATE_OFF,r)
		InitFluLight(2,FLU_STATE_ON,r)
	Else
		r\Objects[0] = LoadRMesh("GFX\map\rooms\room2_offices_3\room2_offices_3_obj_2.rmesh",Null)
		
		ScaleEntity (r\Objects[0],RoomScale,RoomScale,RoomScale)
		EntityType GetChild(r\Objects[0],2), HIT_MAP
		EntityPickMode GetChild(r\Objects[0],2), 2
		PositionEntity(r\Objects[0],r\x,r\y,r\z,True)
		EntityParent(r\Objects[0], r\obj)
		
;		If Rand(2)=1 Then 
;			it = CreateItem("Mobile Task Forces", "paper", r\x + 1243.0 * RoomScale, r\y +140.0 * RoomScale, r\z - 5.0 * RoomScale)
;			EntityParent(it\collider, r\obj)	
;		Else
;			it = CreateItem("Security Clearance Levels", "paper", r\x + 1243.0 * RoomScale, r\y +140.0 * RoomScale, r\z - 5.0 * RoomScale)
;			EntityParent(it\collider, r\obj)			
;		EndIf
;		
;		it = CreateItem("Object Classes", "paper", r\x + 938.0 * RoomScale, r\y +120.0 * RoomScale, r\z + 124.0 * RoomScale)
;		EntityParent(it\collider, r\obj)	
;		
;		it = CreateItem("Document", "paper", r\x + 754.0 * RoomScale, r\y +120.0 * RoomScale, r\z + 5.0 * RoomScale)
;		EntityParent(it\collider, r\obj)	
		
		it = CreateItem(GetLocalString("Item Names","radio"), "radio", r\x + 614.0 * RoomScale, r\y + 120.0 * RoomScale, r\z + 935.0 * RoomScale)
		EntityParent(it\collider, r\obj)				
		
		;For i = 0 To Rand(0,1)
		;	it = CreateItem("ReVision Eyedrops", "eyedrops", r\x + 1238.0*RoomScale, r\y + 220.0 * RoomScale, r\z + 220.0*RoomScale + i*0.05)
		;	EntityParent(it\collider, r\obj)				
		;Next
		
		it = CreateItem(GetLocalString("Item Names","bat_9v"), "bat", r\x + 841.0 * RoomScale, r\y + 135.0 * RoomScale, r\z + 955.0 * RoomScale)
		EntityParent(it\collider, r\obj)
		
		InitFluLight(1,FLU_STATE_ON,r)
		InitFluLight(2,FLU_STATE_FLICKER,r)
	EndIf
	
End Function

Function UpdateEvent_Room2_Offices_3(e.Events)
	Local r2.Rooms,it.Items
	Local firstRoom% = True
	
	If gopt\GameMode = GAMEMODE_DEFAULT
		For r2 = Each Rooms
			If r2\RoomTemplate\Name = e\room\RoomTemplate\Name And r2 <> e\room Then
				firstRoom = False
				Exit
			EndIf
		Next
		
		If firstRoom Then
			If e\room\dist < 8
				If e\room\NPC[0]=Null
					e\room\NPC[0]=CreateNPC(NPC_Human,e\room\x,0.5,e\room\z)
					RotateEntity e\room\NPC[0]\Collider,0,e\room\angle+180,0
					MoveEntity e\room\NPC[0]\Collider,0,0,-0.5
					e\room\NPC[0]\State[0] = STATE_SCRIPT
					ChangeNPCTexture(e\room\NPC[0],"GFX\npcs\035victim.jpg")
					SetNPCFrame(e\room\NPC[0],19)
				EndIf
				If e\room\NPC[1]=Null
					If EntityDistanceSquared(e\room\NPC[0]\Collider,Collider)<PowTwo(2.5)
						e\room\NPC[1]=CreateNPC(NPC_SCP_035_Tentacle,EntityX(e\room\NPC[0]\Collider),0.0,EntityZ(e\room\NPC[0]\Collider))
						RotateEntity e\room\NPC[1]\Collider,0,e\room\angle,0
						MoveEntity e\room\NPC[1]\Collider,0,0,0.6
					EndIf
				EndIf
			Else
				If e\room\dist>HideDistance
					If e\room\NPC[1]<>Null
						RemoveNPC(e\room\NPC[1])
						e\room\NPC[1]=Null
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS