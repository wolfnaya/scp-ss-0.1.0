
Function FillRoom_Cont_008(r.Rooms)
	Local d.Doors,d2.Doors,it.Items,sc.SecurityCams
	Local tex
	
	; ~ Decontamination gateway doors
	r\RoomDoors[0] = CreateDoor(r\zone, r\x - 96.0 * RoomScale, r\y - 5104.0 * RoomScale, r\z - 384.0 * RoomScale, 180.0, r, True, DOOR_WINDOWED, KEY_CARD_4)
	r\RoomDoors[0]\AutoClose = False
	FreeEntity(r\RoomDoors[0]\buttons[0]) : r\RoomDoors[0]\buttons[0] = 0
	PositionEntity(r\RoomDoors[0]\buttons[1], EntityX(r\RoomDoors[0]\buttons[1], True) - 0.08, EntityY(r\RoomDoors[0]\buttons[1], True), EntityZ(r\RoomDoors[0]\buttons[1], True), True)
	
	r\RoomDoors[1] = CreateDoor(r\zone, r\x - 96.0 * RoomScale, r\y - 5104.0 * RoomScale, r\z + 256.0 * RoomScale, 0.0, r, False, DOOR_WINDOWED)
	r\RoomDoors[1]\AutoClose = False
	PositionEntity(r\RoomDoors[1]\buttons[0], r\x + 70.0 * RoomScale, EntityY(r\RoomDoors[1]\buttons[0], True), r\z - 24.0 * RoomScale, True)
	RotateEntity(r\RoomDoors[1]\buttons[0], 0.0, -90.0, 0.0, True)	
	
	r\RoomDoors[0]\LinkedDoor = r\RoomDoors[1]
	r\RoomDoors[1]\LinkedDoor = r\RoomDoors[0]
	
	r\RoomDoors[2] = CreateDoor(r\zone, r\x - 816.0 * RoomScale, r\y - 5104.0 * RoomScale, r\z - 384.0 * RoomScale, 0.0, r, False, DOOR_DEFAULT, KEY_CARD_4)
	PositionEntity(r\RoomDoors[2]\buttons[1], EntityX(r\RoomDoors[2]\buttons[1], True) + 0.08, EntityY(r\RoomDoors[2]\buttons[1], True), EntityZ(r\RoomDoors[2]\buttons[1], True), True)
	r\RoomDoors[2]\locked = 1
	
	; ~ Elevators' doors
	r\RoomDoors[3] = CreateDoor(r\zone, r\x + 448.0 * RoomScale, r\y, r\z, -90.0, r, True, DOOR_ELEVATOR)
	
	r\RoomDoors[4] = CreateDoor(r\zone, r\x + 448.0 * RoomScale, r\y - 5104.0 * RoomScale, r\z, -90.0, r, False, DOOR_ELEVATOR)
	
	d = CreateDoor(r\zone, r\x + 96.0 * RoomScale, r\y - 5104.0 * RoomScale, r\z - 576.0 * RoomScale, 90.0, r)
	PositionEntity(d\buttons[1], EntityX(d\buttons[1], True), EntityY(d\buttons[1], True), EntityZ(d\buttons[1], True) + 0.162, True)
	
	d = CreateDoor(r\zone, r\x - 456.0 * RoomScale, r\y - 5104.0 * RoomScale, r\z - 736.0 * RoomScale, 0.0, r, False, DOOR_DEFAULT, KEY_CARD_4)
	d\locked = 1 : d\DisableWaypoint = True : d\MTFClose = False
	FreeEntity(d\buttons[0]) : d\buttons[0] = 0
	FreeEntity(d\obj2) : d\obj2 = 0
	
	; ~ The container
	r\Objects[0] = CreatePivot()
	PositionEntity(r\Objects[0], r\x - 62.0 * RoomScale, r\y - 4985.0 * RoomScale, r\z + 889.0 * RoomScale)
	
	; ~ The lid of the container
	r\Objects[1] = LoadRMesh("GFX\map\rooms\cont_008\008_2_opt.rmesh", Null)
	ScaleEntity(r\Objects[1], RoomScale, RoomScale, RoomScale)
	PositionEntity(r\Objects[1], r\x - 62.0 * RoomScale, r\y - 4954.0 * RoomScale, r\z + 945.0 * RoomScale)
	RotateEntity(r\Objects[1], 85.0, 0.0, 0.0, True)
	
	tex = LoadTexture_Strict("GFX\map\textures\glass.png", 1 + 2)
	r\Objects[2] = CreateSprite()
	EntityTexture(r\Objects[2], tex)
	DeleteSingleTextureEntryFromCache(tex)
	SpriteViewMode(r\Objects[2], 2)
	ScaleSprite(r\Objects[2], 194.0 * RoomScale * 0.5, 194.0 * RoomScale * 0.5)
	PositionEntity(r\Objects[2], r\x - 640.0 * RoomScale, r\y - 4881.0 * RoomScale, r\z + 800.0 * RoomScale)
	TurnEntity(r\Objects[2], 0.0, 90.0, 0.0)			
	
	; ~ SCP-173's spawnpoint
	r\Objects[3] = CreatePivot()
	PositionEntity(r\Objects[3], r\x - 820.0 * RoomScale, r\y - 4985.0 * RoomScale, r\z + 657.0 * RoomScale)
	
	; ~ SCP-173's attack point
	r\Objects[4] = CreatePivot()
	PositionEntity(r\Objects[4], r\x - 384.0 * RoomScale, r\y - 4985.0 * RoomScale, r\z + 752.0 * RoomScale)
	
	; ~ Red light
	
	r\Objects[5] = CreateSprite()
	PositionEntity(r\Objects[5], r\x - 158 * RoomScale, 368 * RoomScale, r\z + 298.0 * RoomScale)
	ScaleSprite(r\Objects[5], 0.02, 0.02)
	EntityTexture(r\Objects[5], LightSpriteTex[1])
	EntityBlend (r\Objects[5], 3)
	EntityParent(r\Objects[5], r\obj)
	HideEntity r\Objects[5]
	
	; ~ Spawnpoint for the scientist used in the "SCP-008-1's scene"
	r\Objects[6] = CreatePivot()
	PositionEntity(r\Objects[6], r\x + 160.0 * RoomScale, r\y + 670.0 * RoomScale, r\z - 384.0 * RoomScale)
	
	; ~ Spawnpoint for the player
	r\Objects[7] = CreatePivot()
	PositionEntity(r\Objects[7], r\x, r\y + 672.0 * RoomScale, r\z + 350.0 * RoomScale)
	
	; ~ Elevators' pivots
	r\Objects[8] = CreatePivot()
	PositionEntity(r\Objects[8], r\x + 752.0 * RoomScale, r\y + 240.0 * RoomScale, r\z)
	
	r\Objects[9] = CreatePivot()
	PositionEntity(r\Objects[9], r\x + 752.0 * RoomScale, r\y - 4864.0 * RoomScale, r\z)
	
	Local i
	
	For i = 0 To 9
		EntityParent(r\Objects[i], r\obj)
	Next
	
	it.Items = CreateItem(GetLocalString("Item Names","hazmat"), "hazmat", r\x - 537.0 * RoomScale, r\y - 4895.0 * RoomScale, r\z - 66.0 * RoomScale)
	RotateEntity(it\collider, 0.0, 90.0, 0.0)
	EntityParent(it\collider, r\obj)
	
	;it.Items = CreateItem("Document SCP-008", "paper", r\x - 944.0 * RoomScale, r\y - 5008.0 * RoomScale, r\z + 672.0 * RoomScale)
	;RotateEntity(it\collider, 0.0, 0.0, 0.0)
	;EntityParent(it\collider, r\obj)
	
	sc.SecurityCams = CreateSecurityCam(r\x + 384.0 * RoomScale, r\y - 4654.0 * RoomScale, r\z + 1168.0 * RoomScale, r)
	sc\angle = 135.0 : sc\turn = 45.0
	TurnEntity(sc\CameraObj, 20.0, 0.0, 0.0)
	
End Function

Function UpdateEvent_Cont_008(e.Events)
	Local p.Particles
	Local distsquared#
	
	If ecst\Contained008 And ecst\Contained409 Then
		ecst\WasInBCZ = True
	EndIf
	
	If gopt\CurrZone = BCZ Then
		If ecst\WasInBCZ Then
			If (Not TaskExists(TASK_COME_BACK_TO_O5_AGAIN)) Then
				BeginTask(TASK_COME_BACK_TO_O5_AGAIN)
			EndIf
			If TaskExists(TASK_CLOSE_008) Then
				EndTask(TASK_CLOSE_008)
			EndIf
		EndIf
	EndIf
	
	If ecst\WasInHCZ Then
		e\room\RoomDoors[0]\locked = False
		e\room\RoomDoors[1]\locked = False
	Else
		e\room\RoomDoors[0]\locked = True
		e\room\RoomDoors[1]\locked = True
	EndIf
	
	If PlayerRoom = e\room Then
		
		If (Not ecst\Contained008) Then
		
			If TaskExists(TASK_FIND_008) Then
				EndTask(TASK_FIND_008)
				If (Not TaskExists(TASK_CLOSE_008)) Then
					BeginTask(TASK_CLOSE_008)
				EndIf
			Else
				If ecst\WasInHCZ Then
					If (Not TaskExists(TASK_CLOSE_008)) Then
						BeginTask(TASK_CLOSE_008)
					EndIf
				EndIf
			EndIf
			
		EndIf
		
		If EntityY(Collider) < (-4496.0) * RoomScale Then
			GiveAchievement(Achv008)
			;container open
			If e\EventState[0] = 0 Then
				If Curr173\Idle <> SCP173_BOXED Lor Curr173\Idle <> SCP173_CONTAINED Then
					If Curr173\Idle<2 And EntityDistanceSquared(Curr173\Collider,Collider)>PowTwo(HideDistance) ;Just making sure that 173 is far away enough to spawn him to this room
						PositionEntity Curr173\Collider, EntityX(e\room\Objects[3],True),0.5,EntityZ(e\room\Objects[3],True),True
						ResetEntity Curr173\Collider
					EndIf
				EndIf
				e\EventState[0] = 1
			ElseIf e\EventState[0] = 1
				e\SoundCHN[0] = LoopSound2(AlarmSFX[0], e\SoundCHN[0], Camera, e\room\Objects[0], 5.0)
				
				If (MilliSecs() Mod 1000)<500 Then
					ShowEntity e\room\Objects[5] 
				Else
					HideEntity e\room\Objects[5]
				EndIf
				
				distsquared = EntityDistanceSquared(Collider, e\room\Objects[0])
				If distsquared<PowTwo(2.0) Then 
					e\room\RoomDoors[0]\locked = True
					e\room\RoomDoors[1]\locked = True
					
					If e\EventState[1]=0 Then
						ShowEntity e\room\Objects[2]
						If Curr173\Idle <> SCP173_BOXED Lor Curr173\Idle <> SCP173_CONTAINED Then
							If BlinkTimer<-10 And Curr173\Idle = 0 Then
								PositionEntity Curr173\Collider, EntityX(e\room\Objects[4],True),0.5,EntityZ(e\room\Objects[4],True),True
								ResetEntity Curr173\Collider
								
								HideEntity e\room\Objects[2]
								
								If (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Lor (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat") Then
									DamageSPPlayer(5)
									If I_008\Timer=0 Then I_008\Timer=1
									CreateMsg(GetLocalString("Singleplayer", "cont_008_1"))
								EndIf
								
								PlaySound2(LoadTempSound("SFX\General\GlassBreak.ogg"), Camera, e\room\Objects[0]) 
								
								e\EventState[1]=1
							EndIf
						EndIf
					EndIf
					
					If distsquared<PowTwo(1.0) Then
						If EntityInView(e\room\Objects[0], Camera) Then
							DrawHandIcon = True
							
							If KeyDownUse Then
								DrawArrowIcon[2] = True
								RotateEntity(e\room\Objects[1], Max(Min(EntityPitch(e\room\Objects[1])+Max(Min(-Mouse_Y_Speed_1,10.0),-10), 89), 35), EntityYaw(e\room\Objects[1]), 0)
							EndIf
						EndIf
					EndIf
					
					If (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Lor (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat") Then
						If I_008\Timer=0
							I_008\Timer=1
							DebugLog "Infected player"
						EndIf
					EndIf
				EndIf
				
				If EntityPitch(e\room\Objects[1],True)<40 Then 
					e\EventState[0] = 2
					ecst\Contained008 = True
					If TaskExists(TASK_CLOSE_008) Then
						EndTask(TASK_CLOSE_008)
					EndIf
					PlaySound_Strict LeverSFX
				Else
					p.Particles = CreateParticle(EntityX(e\room\Objects[0],True),EntityY(e\room\Objects[0],True),EntityZ(e\room\Objects[0],True), 6, 0.02, -0.12)
					RotateEntity (p\pvt,-90,0,0,True)
					TurnEntity(p\pvt, Rnd(-26,26), Rnd(-26,26), Rnd(360))
					
					p\SizeChange = 0.012
					p\Achange = -0.015
				EndIf		
			Else
				HideEntity e\room\Objects[5]
				e\room\RoomDoors[0]\locked = False
				e\room\RoomDoors[1]\locked = False
				e\room\RoomDoors[2]\locked = False
				
				RotateEntity (e\room\Objects[1],CurveAngle(1,EntityPitch(e\room\Objects[1],True),15.0),EntityYaw(e\room\Objects[1],True),0,True)
				
				If EntityPitch(e\room\Objects[1],True)=<1.0 Then
					RemoveEvent(e)
				EndIf
			EndIf
		EndIf
		e\EventState[2] = UpdateElevators(e\EventState[2], e\room\RoomDoors[3], e\room\RoomDoors[4], e\room\Objects[8], e\room\Objects[9], e)
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D