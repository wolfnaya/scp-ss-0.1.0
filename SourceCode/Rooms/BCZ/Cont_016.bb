
Function FillRoom_Cont_016(r.Rooms)
	Local d.Doors,it.Items,de.Decals
	Local tex%
	Local i
	
	it = CreateItem("Final Note", "paper", r\x - 1241.0 * RoomScale, r\y + 160.0 * RoomScale, r\z - 556.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Dr. Singlinton's Note", "paper", r\x - 691.0 * RoomScale, r\y + 165.0 * RoomScale, r\z + 99.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Dr. Harmann's Note", "paper", r\x - 674.0 * RoomScale, r\y + 276.0 * RoomScale, r\z + 37.0 * RoomScale)
	RotateEntity it\collider,90,0,0
	EntityParent(it\collider, r\obj)
	
	;it = CreateItem("SCP-016", "scp016", r\x - 1280.0 * RoomScale, r\y + 160.0 * RoomScale, r\z + 256.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x,r\y,r\z,0,r,True,DOOR_WINDOWED,KEY_CARD_3)
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x-288*RoomScale,r\y,r\z+320*RoomScale,90,r,True,DOOR_WINDOWED,KEY_CARD_3)
	
	r\RoomDoors[2] = CreateDoor(r\zone,r\x-383*RoomScale,r\y,r\z-512*RoomScale,90,r,True,DOOR_WINDOWED,KEY_CARD_3)
	
	r\RoomDoors[3] = CreateDoor(r\zone,r\x-964*RoomScale,r\y,r\z-512*RoomScale,90,r,True,DOOR_WINDOWED,KEY_CARD_3)
	
	r\RoomDoors[4] = CreateDoor(r\zone,r\x-1280*RoomScale,r\y,r\z-212*RoomScale,0,r,True,DOOR_WINDOWED,KEY_CARD_3)
	
	r\RoomDoors[5] = CreateDoor(r\zone,r\x-1280*RoomScale,r\y,r\z+52*RoomScale,0,r,False,DOOR_WINDOWED,KEY_CARD_3)
	
	r\Objects[0] = CreatePivot()
	PositionEntity r\Objects[0],r\x-681*RoomScale,r\y+83*RoomScale,r\z-175*RoomScale,True
	EntityParent r\Objects[0], r\obj
	
End Function

Function UpdateEvent_Cont_016(e.Events)
	
	If e\room\NPC[0] = Null Then
		e\room\NPC[0] = CreateNPC(NPC_Human,EntityX(e\room\Objects[0],True),1.0,EntityZ(e\room\Objects[0],True))
		ChangeNPCTexture(e\room\NPC[0],"GFX\npcs\scientist.jpg")
		SetNPCFrame e\room\NPC[0], 559
		e\room\NPC[0]\State[0]=3
		e\room\NPC[0]\IsDead=True
	EndIf
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D