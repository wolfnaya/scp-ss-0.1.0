
Function FillRoom_Testroom_2B(r.Rooms)
	Local d.Doors,it.Items,de.Decals
	
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x - 640.0 * RoomScale, 0.5, r\z - 912.0 * RoomScale)
	EntityParent(r\Objects[0], r\obj)
	
	r\Objects[1] = CreatePivot()
	PositionEntity(r\Objects[1], r\x - 669.0 * RoomScale, 0.5, r\z - 16.0 * RoomScale)
	EntityParent(r\Objects[1], r\obj)
	
	Local Glasstex = LoadTexture_Strict("GFX\map\textures\glass.png",1+2)
	r\Objects[2] = CreateSprite()
	EntityTexture(r\Objects[2],Glasstex)
	SpriteViewMode(r\Objects[2],2)
	ScaleSprite(r\Objects[2],182.0*RoomScale*0.5, 192.0*RoomScale*0.5)
	PositionEntity(r\Objects[2], r\x - 632.0 * RoomScale, 224.0*RoomScale, r\z - 208.0 * RoomScale)
	TurnEntity(r\Objects[2],0,180,0)			
	EntityParent(r\Objects[2], r\obj)
	HideEntity (r\Objects[2])
	
	r\Objects[3] = CreatePivot()
	PositionEntity(r\Objects[3], r\x + 200.0*RoomScale, 0.5, r\z + 762.0*RoomScale)
	EntityParent(r\Objects[3], r\obj)
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x - 240.0 * RoomScale, 0.0, r\z + 640.0 * RoomScale, 90, r, False, False, KEY_CARD_1)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
	
	d = CreateDoor(r\zone, r\x - 512.0 * RoomScale, 0.0, r\z + 384.0 * RoomScale, 0, r, False, False)
	d\AutoClose = False : d\open = False
	
	r\RoomDoors[1] = CreateDoor(r\zone,r\x+288.0*RoomScale, 0.0, r\z+416.0*RoomScale,270,r,False,DOOR_OFFICE)
	
	it = CreateItem(GetLocalString("Item Names","key_2"), "key2", r\x - 834.0 * RoomScale, r\y + 137.0 * RoomScale, r\z + 61.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","nav_300"), "nav", r\x - 312.0 * RoomScale, r\y + 264.0 * RoomScale, r\z + 176.0 * RoomScale)
	it\state = 20 : EntityParent(it\collider, r\obj)
	
	it = CreateItem("Class-D Zone Note", "paper", r\x, r\y + 31.0 * RoomScale, r\z + 640.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	de.Decals = CreateDecal(DECAL_BLOODSPLAT2, r\x, r\y + 1.0 * RoomScale, r\z + 640.0 * RoomScale,90,0,0)
	de\Size = Rnd(0.5,0.7) : EntityAlpha(de\obj, 1.0) : ScaleSprite de\obj, de\Size, de\Size
	EntityParent de\obj, r\obj
	
End Function

Function UpdateEvent_Testroom_2B(e.Events)
	Local pvt%,distSquared#,n.NPCs,it.Items
	
	If e\EventState[2] = 0 Then
		n.NPCs = CreateNPC(NPC_Human,EntityX(e\room\Objects[3],True),0.5,EntityZ(e\room\Objects[3],True))
		RotateEntity n\Collider,0,e\room\angle+90,0
		n\State[0] = 3
		SetNPCFrame(n,555)
		n\IsDead = True
		ChangeNPCTexture(n,"GFX\npcs\body2.jpg")
		
		Local bone% = FindChild(n\obj,"Bip01_L_Hand")
		it = CreateItem(GetLocalString("Item Names","key_class_d"), "key_class_d",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
		RotateEntity it\collider,EntityPitch(it\collider,True),e\room\angle+45,EntityRoll(it\collider,True),True
		EntityType it\collider, HIT_ITEM
		
		e\EventState[2] = 1
	EndIf
	
	If Curr173\Idle <> SCP173_BOXED Lor Curr173\Idle <> SCP173_CONTAINED Then
		If PlayerRoom = e\room	
			If Curr173\Idle = 0 Then 
				If e\EventState[0] = 0 Then
					If e\room\RoomDoors[0]\open = True
						PositionEntity(Curr173\Collider, EntityX(e\room\Objects[0], True), 0.5, EntityZ(e\room\Objects[0], True))
						ResetEntity(Curr173\Collider)
						e\EventState[0] = 1
					EndIf
				Else
					If e\room\Objects[2]=0
						Local Glasstex = LoadTexture_Strict("GFX\map\textures\glass.png",1+2,0)
						e\room\Objects[2] = CreateSprite()
						EntityTexture(e\room\Objects[2],Glasstex)
						SpriteViewMode(e\room\Objects[2],2)
						ScaleSprite(e\room\Objects[2],182.0*RoomScale*0.5, 192.0*RoomScale*0.5)
						pvt% = CreatePivot(e\room\obj)
						PositionEntity pvt%,-595.0,224.0,-208.0,False
						PositionEntity(e\room\Objects[2], EntityX(pvt,True), EntityY(pvt,True), EntityZ(pvt,True))
						FreeEntity pvt
						RotateEntity e\room\Objects[2],0,e\room\angle,0
						TurnEntity(e\room\Objects[2],0,180,0)
						EntityParent(e\room\Objects[2], e\room\obj)
						DeleteSingleTextureEntryFromCache Glasstex
					EndIf
					
					ShowEntity (e\room\Objects[2])
					;start a timer for 173 breaking through the window
					e\EventState[0] = e\EventState[0] + 1
					distSquared = EntityDistanceSquared(Collider, e\room\Objects[1])
					If distSquared < PowTwo(1.0) Then
						;if close, increase the timer so that 173 is ready to attack
						e\EventState[0] = Max(e\EventState[0], 70*12)
					ElseIf distSquared > PowTwo(1.4)
						;if the player moves a bit further and blinks, 173 attacks
						If e\EventState[0] > 70*12 And BlinkTimer =< -10 Then
							If (EntityDistanceSquared(Curr173\Collider, e\room\Objects[0]) > PowTwo(5.0)) Then
								;if 173 is far away from the room (perhaps because the player 
								;left and 173 moved to some other room?) -> disable the event
								RemoveEvent(e)
							Else
								PlaySound2(LoadTempSound("SFX\General\GlassBreak.ogg"), Camera, Curr173\obj) 
								FreeEntity(e\room\Objects[2])
								e\room\Objects[2]=0
								PositionEntity(Curr173\Collider, EntityX(e\room\Objects[1], True), 0.5, EntityZ(e\room\Objects[1], True))
								ResetEntity(Curr173\Collider)
								RemoveEvent(e)
							EndIf
						EndIf	
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D