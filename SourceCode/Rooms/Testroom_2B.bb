
Function FillRoom_Testroom_2B(r.Rooms)
	Local d.Doors,it.Items
	
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
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x - 240.0 * RoomScale, 0.0, r\z + 640.0 * RoomScale, 90, r, False, False, 1)
	r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
	
	d = CreateDoor(r\zone, r\x - 512.0 * RoomScale, 0.0, r\z + 384.0 * RoomScale, 0, r, False, False)
	d\AutoClose = False : d\open = False
	
	it = CreateItem("Level 2 Key Card", "key2", r\x - 914.0 * RoomScale, r\y + 137.0 * RoomScale, r\z + 61.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("S-NAV 300 Navigator", "nav", r\x - 312.0 * RoomScale, r\y + 264.0 * RoomScale, r\z + 176.0 * RoomScale)
	it\state = 20 : EntityParent(it\collider, r\obj)
	
End Function

Function UpdateEvent_Testroom_2B(e.Events)
	Local pvt%,distSquared#
	
	If PlayerRoom = e\room	
		If Curr173 <> Null And Curr173\Idle = SCP173_ACTIVE Then 
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
					pvt = FreeEntity_Strict(pvt)
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
							e\room\Objects[2] = FreeEntity_Strict(e\room\Objects[2])
							PositionEntity(Curr173\Collider, EntityX(e\room\Objects[1], True), 0.5, EntityZ(e\room\Objects[1], True))
							ResetEntity(Curr173\Collider)
							RemoveEvent(e)
						EndIf
					EndIf	
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D