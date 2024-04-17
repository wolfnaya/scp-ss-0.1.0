
Function FillRoom_Room2_Medibay(r.Rooms)
	Local it.Items,d.Doors,tex,ItemName$,ItemTempName$,i
	
	; ~ Doors
	d = CreateDoor(r\zone, r\x - 256.0 * RoomScale, r\y, r\z + 640.0 * RoomScale, 90.0, r, False, DOOR_ONE_SIDED, KEY_CARD_3)
	
	d = CreateDoor(r\zone, r\x - 512.0 * RoomScale, r\y, r\z + 378.0 * RoomScale, 0.0, r, False, DOOR_OFFICE)
	
	d = CreateDoor(r\zone, r\x - 1104.0 * RoomScale, r\y, r\z + 640.0 * RoomScale, 270.0, r, False, DOOR_ONE_SIDED, KEY_CARD_3)
	d\locked = 1 : d\DisableWaypoint = True : d\MTFClose = False
	FreeEntity(d\buttons[0]) : d\buttons[0] = 0
	FreeEntity(d\obj2) : d\obj2 = 0
	
	; ~ Zombie spawnpoint
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x - 820.0 * RoomScale, r\y + 500.0 * RoomScale, r\z - 464.0 * RoomScale)
	
	; ~ Orange duck
	r\Objects[1] = LoadMesh_Strict("GFX\npcs\duck_low_res.b3d")
	tex = LoadTexture_Strict("GFX\Npcs\duck4.png")
	TextureBlend(tex, 5)
	EntityTexture(r\Objects[1], tex)
	DeleteSingleTextureEntryFromCache(tex)
	ScaleEntity(r\Objects[1], 0.07, 0.07, 0.07)
	PositionEntity(r\Objects[1], r\x - 910.0 * RoomScale, r\y + 144.0 * RoomScale, r\z - 778.0 * RoomScale)				
	TurnEntity(r\Objects[1], 6.0, 180.0, 0.0)
	
	For i = 0 To 1
		EntityParent(r\Objects[i], r\obj)
	Next
	
	it.Items = CreateItem(GetLocalString("Item Names","first_aid"), "firstaid", r\x - 333.0 * RoomScale, r\y + 192.0 * RoomScale, r\z - 123.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	If ecst\ChanceToSpawnWolfNote = 4 Then
		it = CreateItem("Wolfnaya's Room Note", "paper", r\x-656.0*RoomScale, r\y+180.0*RoomScale, r\z-835.0*RoomScale)
		EntityParent(it\collider, r\obj)
	EndIf
	
	InitFluLight(0,FLU_STATE_OFF,r)
	InitFluLight(1,FLU_STATE_ON,r)
	InitFluLight(2,FLU_STATE_FLICKER,r)
	
End Function

Function UpdateEvent_Room2_Medibay(e.Events)
	Local n.NPCs
	
	If PlayerRoom = e\room Then
		
		For n = Each NPCs
			If e\room\NPC[0]\HP > 0 Then
				If e\EventState[0] = 0.0 Then
					e\room\NPC[0] = CreateNPC(NPC_Zombie, EntityX(e\room\Objects[0], True), 0.5, EntityZ(e\room\Objects[0], True),Surgeon_Zombie)
					e\room\NPC[0]\State[0] = Z_STATE_LYING
					RotateEntity(e\room\NPC[0]\Collider, 0.0, e\room\angle - 90.0, 0.0)
					e\EventState[0] = 1.0
				EndIf
				
				If e\EventState[1] = 0.0 Then
					If EntityDistanceSquared(e\room\NPC[0]\Collider, Collider) < 1.44 Then
						LightBlink = 10.0
						PlaySound_Strict(LightSFX)
						e\room\NPC[0]\State[0] = Z_STATE_STANDUP
						e\EventState[1] = 1.0
					EndIf
				EndIf
			EndIf
		Next
		
		If e\EventState[2] < 4 Then
			If psp\Health < 100 Then
				If InteractWithObject(e\room\Objects[1], 0.49) Then
					CreateMsg(GetLocalString("Items","duck_medibay"))
					HealSPPlayer(15)
					e\EventState[2] = e\EventState[2] + 1
					PlaySound_Strict(LoadTempSound("SFX\SCP\Joke\Quack.ogg"))
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D