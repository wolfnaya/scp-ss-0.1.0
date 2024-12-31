Function FillRoom_Room4_Tunnel(r.Rooms)
	
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x + 1.0, 0.5, r\z + 1.0)
	EntityParent(r\Objects[0], r\obj)
	
End Function	

Function UpdateEvent_Room4_Tunnel(e.Events)
	
	If e\room\dist < 10.0 And e\room\dist > 0 Then
		e\room\NPC[0]=CreateNPC(NPC_Human, EntityX(e\room\Objects[0],True), 0.5, EntityZ(e\room\Objects[0],True))
		e\room\NPC[0]\texture = "GFX\npcs\Humans\Personnel\body1.jpg"
		Local tex = LoadTexture_Strict(e\room\NPC[0]\texture, 1, 2)
		TextureBlend(tex,5)
		EntityTexture(e\room\NPC[0]\obj, tex)
		DeleteSingleTextureEntryFromCache tex
		RotateEntity e\room\NPC[0]\Collider, 0, e\room\angle+65.0, 0
		SetNPCFrame e\room\NPC[0], 558
		e\room\NPC[0]\State[0]=STATE_SCRIPT
		e\room\NPC[0]\IsDead=True
		
		RemoveEvent(e)
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS