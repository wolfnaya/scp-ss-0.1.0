Function FillRoom_Cont_207_268_1079_1033RU(r.Rooms)
	Local it.Items,de.Decals
	
	; ~ Pivot for a dead Class D
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x + 433.0 * RoomScale, r\y, r\z - 840.0 * RoomScale)
	EntityParent(r\Objects[0], r\obj)
	
	r\Objects[1] = CreatePivot()
	PositionEntity(r\Objects[1], r\x + 139.0 * RoomScale, r\y+182*RoomScale, r\z +998.0 * RoomScale)
	EntityParent(r\Objects[1], r\obj)
	
	; ~ SCP-268
	r\RoomDoors[0] = CreateDoor(r\zone,r\x+290.0*RoomScale,0,r\z+680.0*RoomScale,270,r,False,False,KEY_CARD_4)
	r\RoomDoors[0]\AutoClose = False
	; ~ SCP-207
	r\RoomDoors[1] = CreateDoor(r\zone,r\x-255.0*RoomScale,0,r\z-670.0*RoomScale,90,r,False,False,KEY_CARD_4)
	r\RoomDoors[1]\AutoClose = False
	; ~ SCP-1033-RU
	r\RoomDoors[2] = CreateDoor(r\zone,r\x-221.0*RoomScale,0,r\z+680.0*RoomScale,270,r,False,False,KEY_CARD_4)
	r\RoomDoors[2]\AutoClose = False
	; ~ SCP-1079
	r\RoomDoors[3] = CreateDoor(r\zone,r\x+255.0*RoomScale,0,r\z-672.0*RoomScale,90,r,False,False,KEY_CARD_3)
	r\RoomDoors[3]\AutoClose = False
	
	it = CreateItem("SCP-207", "scp207", r\x - 706.0 * RoomScale, r\y + 10.0 * RoomScale, r\z - 649.0 * RoomScale)
	RotateEntity it\collider, 0, 90, 0
	EntityParent(it\collider, r\obj)
	
	;it = CreateItem(GetLocalString("Item Names","doc_207"), "paper", r\x -700.0 * RoomScale, r\y + 110.0 * RoomScale, r\z - 647.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	;it = CreateItem("SCP-268", "scp268", r\x + 791.0 * RoomScale, r\y + 20.0 * RoomScale, r\z + 660.0 * RoomScale)
	;RotateEntity it\Collider, 0, 90, 0
	;EntityParent(it\Collider, r\obj)
	
	;it = CreateItem(GetLocalString("Item Names","doc_268"), "paper", r\x + 696.0 * RoomScale, r\y + 140.0 * RoomScale, r\z + 923.0 * RoomScale)
	;RotateEntity it\collider, 0, -25, 0
	;EntityParent(it\collider, r\obj)
	
	;it = CreateItem(GetLocalString("Item Names","doc_268_ad"), "paper", r\x + 468.0 * RoomScale, r\y + 10.0 * RoomScale, r\z + 670.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	it = CreateItem("SCP-1033-RU", "scp1033ru", r\x - 561.0 * RoomScale, r\y + 130.0 * RoomScale, r\z + 657.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	;it = CreateItem(GetLocalString("Item Names","doc_1033-ru"), "paper", r\x - 342.0 * RoomScale, r\y + 200.0 * RoomScale, r\z + 843.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	;it = CreateItem("SCP-1079", "scp1079", r\x + 668.0 * RoomScale, r\y + 50.0 * RoomScale, r\z - 790.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	;it = CreateItem("SCP-1079", "scp1079", r\x + 668.0 * RoomScale, r\y + 50.0 * RoomScale, r\z - 547.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	;it = CreateItem("SCP-1079-01", "scp1079sweet", r\x + 371.0 * RoomScale, r\y + 10.0 * RoomScale, r\z - 810.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	;it = CreateItem(GetLocalString("Item Names","doc_1079"), "paper", r\x +385.0 * RoomScale, r\y + 205.0 * RoomScale, r\z - 447.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	de.Decals = CreateDecal(DECAL_1079,r\x + 371.0 * RoomScale, r\y + 10.0 * RoomScale, r\z - 810.0 * RoomScale,90,0,Rand(360))
	de\Size = Rnd(0.1,0.2) : EntityAlpha(de\obj, 0.7) : ScaleSprite de\obj, de\Size, de\Size
	EntityParent de\obj, r\obj
	
End Function

Function UpdateEvent_Cont_207_268_1079_1033RU(e.Events)
	Local i,p.Particles
	
	If PlayerRoom = e\room Then
		If e\room\NPC[0] = Null Then
			
			e\Sound[0] = LoadSound_Strict("SFX\General\Spark_Short.ogg")
			
			e\room\NPC[0] = CreateNPC(NPC_Human, EntityX(e\room\Objects[0], True), 0.5, EntityZ(e\room\Objects[0], True))
			RotateEntity e\room\NPC[0]\Collider, 0, e\room\angle + 360, 0
			e\room\NPC[0]\IsDead = True
			e\room\NPC[0]\texture = "GFX\npcs\1079victim.png"
			Local tex = LoadTexture_Strict(e\room\NPC[0]\texture, 1, 2)
			TextureBlend(tex,5)
			EntityTexture(e\room\NPC[0]\obj, tex)
			DeleteSingleTextureEntryFromCache tex
			SetNPCFrame(e\room\NPC[0], 40)
			e\room\NPC[0]\State[0] = STATE_SCRIPT
		EndIf
		
		If e\EventState[0] = 0 Then
			If e\room\RoomDoors[3]\open = True Then 
				If e\room\RoomDoors[3]\openstate = 180 Then 
					e\EventState[0] = 1
				EndIf
			Else
				If (EntityDistance(Collider, e\room\RoomDoors[3]\obj) < 1.0) Then
					BlurTimer = 1000.0
					e\room\RoomDoors[3]\open = True
					PlaySound_Strict HorrorSFX[10]
					RemoveEvent(e)
				EndIf
			EndIf
		EndIf
		
		If Rand(50) = 1 Then
			PlaySound2(e\Sound[0], Camera, e\room\Objects[1], 3.0, 0.4)
			If ParticleAmount > 0 Then
				For i = 0 To (2 + (1 * (ParticleAmount - 1)))
					p.Particles = CreateParticle(EntityX(e\room\Objects[1],True), EntityY(e\room\Objects[1],True)+Rnd(0.0,0.05), EntityZ(e\room\Objects[1],True),7, 0.002, 0.0, 25.0)
					p\speed = Rnd(0.005, 0.03) : p\size = Rnd(0.005, 0.0075) : p\Achange = -0.05
					RotateEntity(p\pvt, 0.0, e\room\angle, Rnd(-20.0, 0.0))
					ScaleSprite(p\obj, p\size, p\size)
				Next
			EndIf	
		EndIf
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D TSS