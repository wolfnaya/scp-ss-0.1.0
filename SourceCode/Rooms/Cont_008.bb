
Function FillRoom_Cont_008(r.Rooms)
	Local d.Doors,d2.Doors,it.Items,sc.SecurityCams
	Local Glasstex
	
	;the container
	r\Objects[0] = CreatePivot(r\obj)
	PositionEntity(r\Objects[0], r\x + 292.0 * RoomScale, 130.0*RoomScale, r\z + 516.0 * RoomScale, True)
	
	;the lid of the container
	r\Objects[1] = LoadRMesh("GFX\map\rooms\cont_008\008_2_opt.rmesh",Null)
	ScaleEntity r\Objects[1], RoomScale, RoomScale, RoomScale
	PositionEntity(r\Objects[1], r\x + 292 * RoomScale, 151 * RoomScale, r\z + 576.0 * RoomScale, 0)
	EntityParent(r\Objects[1], r\obj)
	
	RotateEntity(r\Objects[1],89,0,0,True)
	
	Glasstex = LoadTexture_Strict("GFX\map\textures\glass.png",1+2,0)
	r\Objects[2] = CreateSprite()
	EntityTexture(r\Objects[2],Glasstex)
	SpriteViewMode(r\Objects[2],2)
	ScaleSprite(r\Objects[2],256.0*RoomScale*0.5, 194.0*RoomScale*0.5)
	PositionEntity(r\Objects[2], r\x - 176.0 * RoomScale, 224.0*RoomScale, r\z + 448.0 * RoomScale)
	TurnEntity(r\Objects[2],0,90,0)			
	EntityParent(r\Objects[2], r\obj)
	
	;scp-173 spawnpoint
	r\Objects[3] = CreatePivot(r\obj)
	PositionEntity(r\Objects[3], r\x - 445.0 * RoomScale, 120.0*RoomScale, r\z + 544.0 * RoomScale, True)
	
	;scp-173 attack point
	r\Objects[4] = CreatePivot(r\obj)
	PositionEntity(r\Objects[4], r\x + 67.0 * RoomScale, 120.0*RoomScale, r\z + 464.0 * RoomScale, True)
	
	r\Objects[5] = CreateSprite()
	PositionEntity(r\Objects[5], r\x - 158 * RoomScale, 368 * RoomScale, r\z + 298.0 * RoomScale)
	ScaleSprite(r\Objects[5], 0.02, 0.02)
	EntityTexture(r\Objects[5], LightSpriteTex[1])
	EntityBlend (r\Objects[5], 3)
	EntityParent(r\Objects[5], r\obj)
	HideEntity r\Objects[5]
	
	d = CreateDoor(r\zone, r\x + 296.0 * RoomScale, 0, r\z - 672.0 * RoomScale, 180, r, True, 0, 4)
	d\AutoClose = False
	PositionEntity (d\buttons[1], r\x + 164.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
	d\buttons[0] = FreeEntity_Strict(d\buttons[0])
	d\obj2 = FreeEntity_Strict(d\obj2)
	r\RoomDoors[0] = d
	
	d2 = CreateDoor(r\zone, r\x + 296.0 * RoomScale, 0, r\z - 144.0 * RoomScale, 0, r, False)
	d2\AutoClose = False
	PositionEntity (d2\buttons[0], r\x + 432.0 * RoomScale, EntityY(d2\buttons[0],True), r\z - 480.0 * RoomScale, True)
	RotateEntity(d2\buttons[0], 0, -90, 0, True)			
	PositionEntity (d2\buttons[1], r\x + 164.0 * RoomScale, EntityY(d2\buttons[0],True), r\z - 128.0 * RoomScale, True)
	d2\obj2 = FreeEntity_Strict(d2\obj2)
	r\RoomDoors[1] = d2
	
	d\LinkedDoor = d2
	d2\LinkedDoor = d
	
	d = CreateDoor(r\zone, r\x - 384.0 * RoomScale, 0, r\z - 672.0 * RoomScale, 0, r, False, 0, 4)
	d\AutoClose = False : d\locked = True : r\RoomDoors[2]=d
	
	it = CreateItem("Hazmat Suit", "hazmatsuit", r\x - 76.0 * RoomScale, 0.5, r\z - 396.0 * RoomScale)
	EntityParent(it\collider, r\obj) : RotateEntity(it\collider, 0, 90, 0)
	
	it = CreateItem("Document SCP-008", "paper", r\x - 245.0 * RoomScale, r\y + 192.0 * RoomScale, r\z + 368.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	;spawnpoint for the scientist used in the "008 zombie scene"
	r\Objects[6] = CreatePivot(r\obj)
	PositionEntity(r\Objects[6], r\x + 160 * RoomScale, 672 * RoomScale, r\z - 384.0 * RoomScale, True)
	;spawnpoint for the player
	r\Objects[7] = CreatePivot(r\obj)
	PositionEntity(r\Objects[7], r\x, 672 * RoomScale, r\z + 352.0 * RoomScale, True)
	
	sc.SecurityCams = CreateSecurityCam(r\x+578.956*RoomScale, r\y+444.956*RoomScale, r\z+772.0*RoomScale, r)
	sc\angle = 135
	TurnEntity(sc\CameraObj, 20, 0, 0)
	sc\ID = 7
	
End Function

Function UpdateEvent_Cont_008(e.Events)
	Local p.Particles
	Local distsquared#
	
	If PlayerRoom = e\room Then	
		GiveAchievement(Achv008)=True
		;container open
		If e\EventState[0] = 0 Then
			If Curr173 <> Null And Curr173\Idle = SCP173_ACTIVE And EntityDistanceSquared(Curr173\Collider,Collider) > PowTwo(HideDistance) ;Just making sure that 173 is far away enough to spawn him to this room
				PositionEntity Curr173\Collider, EntityX(e\room\Objects[3],True),0.5,EntityZ(e\room\Objects[3],True),True
				ResetEntity Curr173\Collider
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
					If BlinkTimer<-10 And Curr173 <> Null And Curr173\Idle = SCP173_ACTIVE Then
						PositionEntity Curr173\Collider, EntityX(e\room\Objects[4],True),0.5,EntityZ(e\room\Objects[4],True),True
						ResetEntity Curr173\Collider
						
						HideEntity e\room\Objects[2]
						
						If (Not wbl\Hazmat) Then 
							;Injuries=Injuries+0.1
							DamageSPPlayer(5)
							If Infect=0 Then Infect=1
							CreateMsg(GetLocalString("Singleplayer", "cont_008_1"))
						EndIf
						
						PlaySound2(LoadTempSound("SFX\General\GlassBreak.ogg"), Camera, e\room\Objects[0]) 
						
						e\EventState[1]=1
					EndIf
				EndIf
				
				If distsquared<PowTwo(1.0) Then
					If EntityInView(e\room\Objects[0], Camera) Then
						DrawHandIcon = True
						
						If keydownuse Then
							DrawArrowIcon[2] = True
							RotateEntity(e\room\Objects[1], Max(Min(EntityPitch(e\room\Objects[1])+Max(Min(-mouse_y_speed_1,10.0),-10), 89), 35), EntityYaw(e\room\Objects[1]), 0)
						EndIf
					EndIf
				EndIf
				
				If (Not wbl\Hazmat) And Bloodloss>0.0
					If Infect=0
						Infect=1
						DebugLog "Infected player"
					EndIf
				EndIf
			EndIf
			
			If EntityPitch(e\room\Objects[1],True)<40 Then 
				e\eventstate[0] = 2
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
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D