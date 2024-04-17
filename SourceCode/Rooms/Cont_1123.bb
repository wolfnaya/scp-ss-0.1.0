
Function FillRoom_Cont_1123(r.Rooms)
	Local it.Items,d.Doors
	
	it = CreateItem("Document SCP-1123", "paper", r\x + 511.0 * RoomScale, r\y + 125.0 * RoomScale, r\z - 936.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("SCP-1123", "1123", r\x + 832.0 * RoomScale, r\y + 166.0 * RoomScale, r\z + 784.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Leaflet", "paper", r\x - 816.0 * RoomScale, r\y + 704.0 * RoomScale, r\z+ 888.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Gas Mask", "gasmask", r\x + 457.0 * RoomScale, r\y + 150.0 * RoomScale, r\z + 960.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	d.Doors = CreateDoor(r\zone, r\x + 832.0 * RoomScale, 0.0, r\z + 367.0 * RoomScale, 0, r, False, DOOR_WINDOWED, 3)
	PositionEntity(d\buttons[0], r\x + 956.0 * RoomScale, EntityY(d\buttons[0],True), r\z + 352.0 * RoomScale, True)
	PositionEntity(d\buttons[1], r\x + 713.0 * RoomScale, EntityY(d\buttons[1],True), r\z + 384.0 * RoomScale, True)
	d.Doors = CreateDoor(r\zone, r\x + 280.0 * RoomScale, 0.0, r\z - 607.0 * RoomScale, 90, r, False, False)
	PositionEntity(d\buttons[0], EntityX(d\buttons[0],True), EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
	PositionEntity(d\buttons[1], EntityX(d\buttons[1],True), EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
	
	d.Doors = CreateDoor(r\zone, r\x + 280.0 * RoomScale, 512.0 * RoomScale, r\z - 607.0 * RoomScale, 90, r, False, False)
	PositionEntity(d\buttons[0], EntityX(d\buttons[0],True), EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
	d\buttons[1] = FreeEntity_Strict(d\buttons[1])
	r\RoomDoors[0] = d	
	
	r\Objects[3] = CreatePivot(r\obj)
	PositionEntity(r\Objects[3], r\x + 832.0 * RoomScale, r\y + 166.0 * RoomScale, r\z + 784.0 * RoomScale, True)
	r\Objects[4] = CreatePivot(r\obj)
	PositionEntity(r\Objects[4], r\x -648.0 * RoomScale, r\y + 592.0 * RoomScale, r\z + 692.0 * RoomScale, True)
	r\Objects[5] = CreatePivot(r\obj)
	PositionEntity(r\Objects[5], r\x + 828.0 * RoomScale, r\y + 592.0 * RoomScale, r\z + 592.0 * RoomScale, True)
	
	r\Objects[6] = CreatePivot(r\obj)
	PositionEntity(r\Objects[6], r\x - 76.0 * RoomScale, r\y + 620.0 * RoomScale, r\z + 744.0 * RoomScale, True)
	r\Objects[7] = CreatePivot(r\obj)
	PositionEntity(r\Objects[7], r\x - 640.0 * RoomScale, r\y + 620.0 * RoomScale, r\z - 864.0 * RoomScale, True)	
	
	r\Objects[8] = LoadMesh_Strict("GFX\map\forest\door_frame.b3d")
	PositionEntity r\Objects[8], r\x - 272.0 * RoomScale, 512.0 * RoomScale, r\z + 288.0 * RoomScale,True
	RotateEntity r\Objects[8],0,90,0,True
	ScaleEntity r\Objects[8],45.0*RoomScale,45.0*RoomScale,80.0*RoomScale,True
	EntityParent r\Objects[8],r\obj
	
	r\Objects[9] =  LoadMesh_Strict("GFX\map\forest\door.b3d")
	PositionEntity r\Objects[9],r\x - 272.0 * RoomScale, 512.0 * RoomScale, r\z + (288.0-70) * RoomScale,True
	RotateEntity r\Objects[9],0,10,0,True
	EntityType r\Objects[9], HIT_MAP
	ScaleEntity r\Objects[9],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True
	EntityParent r\Objects[9],r\obj
	
	r\Objects[10] = CopyEntity(r\Objects[8])
	PositionEntity r\Objects[10], r\x - 272.0 * RoomScale, 512.0 * RoomScale, r\z + 736.0 * RoomScale,True
	RotateEntity r\Objects[10],0,90,0,True
	ScaleEntity r\Objects[10],45.0*RoomScale,45.0*RoomScale,80.0*RoomScale,True
	EntityParent r\Objects[10],r\obj
	
	r\Objects[11] =  CopyEntity(r\Objects[9])
	PositionEntity r\Objects[11],r\x - 272.0 * RoomScale, 512.0 * RoomScale, r\z + (736.0-70) * RoomScale,True
	RotateEntity r\Objects[11],0,90,0,True
	EntityType r\Objects[11], HIT_MAP
	ScaleEntity r\Objects[11],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True
	EntityParent r\Objects[11],r\obj
	
	r\Objects[12] = CopyEntity(r\Objects[8])
	PositionEntity r\Objects[12], r\x - 592.0 * RoomScale, 512.0 * RoomScale, r\z - 704.0 * RoomScale,True
	RotateEntity r\Objects[12],0,0,0,True
	ScaleEntity r\Objects[12],45.0*RoomScale,45.0*RoomScale,80.0*RoomScale,True
	EntityParent r\Objects[12],r\obj
	
	r\Objects[13] =  CopyEntity(r\Objects[9])
	PositionEntity r\Objects[13],r\x - (592.0+70.0) * RoomScale, 512.0 * RoomScale, r\z - 704.0 * RoomScale,True
	RotateEntity r\Objects[13],0,0,0,True
	EntityType r\Objects[13], HIT_MAP
	ScaleEntity r\Objects[13],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True
	EntityParent r\Objects[13],r\obj	
	
	r\Objects[14] = LoadMesh_Strict("GFX\map\rooms\cont_1123\1123_hb.b3d",r\obj)
	EntityPickMode r\Objects[14],2
	EntityType r\Objects[14],HIT_MAP
	EntityAlpha r\Objects[14],0.0
	
End Function

Function UpdateEvent_Cont_1123(e.Events)
	Local de.Decals
	Local nazi%,scale#,x#,y#,z#
	Local i
	
	If PlayerRoom = e\room Then
		;the event is started when the player picks up SCP-1123 (in Items.bb/UpdateItems())
		If e\EventState[0]>0 And e\EventState[0]<7 Then
			CanSave = False
		EndIf
		If e\EventState[0] = 1 Then
			
			;Saving Injuries and Bloodloss, so that the player won't be healed automatically
			;PrevInjuries = Injuries
			;PrevBloodloss = Bloodloss
			PrevSecondaryLightOn = SecondaryLightOn
			SecondaryLightOn = True
			
			e\room\NPC[0] = CreateNPC(NPCtypeD, EntityX(e\room\Objects[6],True),EntityY(e\room\Objects[6],True),EntityZ(e\room\Objects[6],True))
			
			nazi = LoadAnimMesh_Strict("GFX\npcs\naziofficer.b3d")
			scale# = 0.5 / MeshWidth(nazi)
			
			e\room\NPC[0]\obj = FreeEntity_Strict(e\room\NPC[0]\obj)
			e\room\NPC[0]\obj = CopyEntity(nazi)
			ScaleEntity e\room\NPC[0]\obj, scale, scale, scale
			
			nazi = FreeEntity_Strict(nazi)
			PositionEntity Collider, EntityX(e\room\Objects[4],True),EntityY(e\room\Objects[4],True),EntityZ(e\room\Objects[4],True),True
			ResetEntity Collider
			CameraShake = 1.0
			BlurTimer = 1200
			;Injuries = 1.0
			e\EventState[0] = 2
			
		ElseIf e\EventState[0] = 2
			e\EventState[1] = e\EventState[1] + FPSfactor
			
			PointEntity e\room\NPC[0]\Collider, Collider
			BlurTimer = Max(BlurTimer, 100)
			
			If e\EventState[1]>200 And e\EventState[1]-FPSfactor=<200 Then 							
				e\Sound[0] = LoadSound_Strict("SFX\Music\1123.ogg");
				e\SoundCHN[0] = PlaySound_Strict(e\Sound[0])
			EndIf
			
			If e\EventState[1] > 1000 Then
				If e\Sound[1]=0 Then
					e\Sound[1] = LoadSound_Strict("SFX\Door\1123DoorOpen.ogg")
					e\SoundCHN[1] = PlaySound_Strict(e\Sound[1])
				EndIf
				RotateEntity e\room\Objects[11], 0, CurveAngle(10, EntityYaw(e\room\Objects[11],0), 40), 0,False
				If e\EventState[1]=>1040 And e\EventState[1]-FPSfactor<1040 Then 
					PlaySound2(LoadTempSound("SFX\SCP\1123\Officer1.ogg"), Camera, e\room\NPC[0]\obj)
				ElseIf e\EventState[1]=>1400 And e\EventState[1]-FPSfactor<1400 Then 
					PlaySound2(LoadTempSound("SFX\SCP\1123\Officer2.ogg"), Camera, e\room\NPC[0]\obj)
				EndIf
				e\room\NPC[0]\State[0] = 3
				AnimateNPC(e\room\NPC[0],3,26,0.2,True)
				If EntityDistanceSquared(Collider, e\room\Objects[4])>PowTwo(392*RoomScale) Then
					IsCutscene = True
					BlinkTimer = -10
					IsCutscene = False
					BlurTimer = 500
					PositionEntity Collider, EntityX(e\room\Objects[5],True),EntityY(e\room\Objects[5],True),EntityZ(e\room\Objects[5],True),True
					RotateEntity Collider, 0, EntityYaw(e\room\obj,True)+180, 0
					ResetEntity(Collider)
					e\EventState[0] = 3
				EndIf
				
			EndIf
		ElseIf e\EventState[0]=3
			If e\room\RoomDoors[0]\openstate>160 Then
				If e\Sound[0]=0 Then e\Sound[0] = LoadSound_Strict("SFX\Music\1123.ogg")
				e\SoundCHN[0] = PlaySound_Strict(e\Sound[0])
				
				PositionEntity e\room\NPC[0]\Collider, EntityX(e\room\Objects[7],True),EntityY(e\room\Objects[7],True),EntityZ(e\room\Objects[7],True)
				ResetEntity e\room\NPC[0]\Collider
				
				e\EventState[0]=4
			EndIf
		ElseIf e\EventState[0]=4
			
			TFormPoint EntityX(Collider),EntityY(Collider),EntityZ(Collider),0,e\room\obj
			
			If TFormedX()<256 And TFormedZ()>-480 Then
				e\room\RoomDoors[0]\open = False
			EndIf
			
			If EntityYaw(e\room\Objects[13],False)=0 Then
				DebugLog "aaaaaaaa"
				If EntityDistanceSquared(Collider, e\room\Objects[12])<PowTwo(1.0) Then
					DrawHandIcon = True
					If MouseHit1 Then
						RotateEntity e\room\Objects[13], 0, 1, 0, False
						RotateEntity e\room\Objects[11], 0, 90, 0, False
						PlaySound_Strict(LoadTempSound("SFX\SCP\1123\Horror.ogg"))
					EndIf
				EndIf							
			Else
				DebugLog "bbbbbbb"
				RotateEntity e\room\Objects[13], 0, CurveAngle(90, EntityYaw(e\room\Objects[13], False), 40), 0
				If EntityYaw(e\room\Objects[13], False)>30 Then
					e\room\NPC[0]\State[0] = 3
					PointEntity e\room\NPC[0]\Collider, Collider
					AnimateNPC(e\room\NPC[0], 27, 54, 0.5, False)
					If e\room\NPC[0]\Frame => 54 Then
						e\EventState[0] = 5
						e\EventState[1] = 0
						PositionEntity Collider, EntityX(e\room\obj,True),0.3,EntityZ(e\room\obj,True),True
						ResetEntity Collider
						IsCutscene = True
						BlinkTimer = -10
						IsCutscene = False
						BlurTimer = 500	
						;Injuries = 1.5
						;Bloodloss = 70							
					EndIf								
				EndIf
			EndIf
			
		ElseIf e\EventState[0] = 5
			e\EventState[1] = e\EventState[1] + FPSfactor
			If e\EventState[1] > 500 Then 
				RotateEntity e\room\Objects[9],0,90,0,False
				RotateEntity e\room\Objects[13],0,0,0,False
				
				x = (EntityX(e\room\Objects[8], True)+EntityX(e\room\Objects[12], True))/2
				y = EntityY(e\room\Objects[5], True)
				z = (EntityZ(e\room\Objects[8], True)+EntityZ(e\room\Objects[12], True))/2
				PositionEntity Collider, x,y,z, True
				ResetEntity(Collider)
				
				x = (EntityX(Collider, True)+EntityX(e\room\Objects[12], True))/2
				z = (EntityZ(Collider, True)+EntityZ(e\room\Objects[12], True))/2
				
				PositionEntity e\room\NPC[0]\Collider, x,y+0.2,z
				ResetEntity e\room\NPC[0]\Collider
				
				;Injuries = 1.5
				;Bloodloss = 70
				
				IsCutscene = True
				BlinkTimer = -10
				IsCutscene = False
				
				de.Decals = CreateDecal(DECAL_BLOODSPLAT2, EntityX(Collider), 512*RoomScale + 0.0005, EntityZ(Collider),90,Rnd(360),0)
				de\Size = 0.5 : ScaleSprite de\obj, de\Size, de\Size
				
				e\room\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\1123\Officer3.ogg")
				
				e\EventState[0] = 6
			EndIf
		ElseIf e\EventState[0] = 6
			PointEntity e\room\NPC[0]\Collider, Collider
			AnimateNPC(e\room\NPC[0], 75, 128, 0.04, True)	
			If e\room\NPC[0]\Sound<>0 Then 
				If e\room\NPC[0]\SoundChn<>0 Then
					If (Not ChannelPlaying(e\room\NPC[0]\SoundChn)) Then 
						PlaySound_Strict(LoadTempSound("SFX\SCP\1123\Gunshot.ogg"))
						e\EventState[0] = 7
						FreeSound_Strict e\room\NPC[0]\Sound : e\room\NPC[0]\Sound=0	
					EndIf
				EndIf
				
				If e\room\NPC[0]\Sound<>0 Then e\room\NPC[0]\SoundChn = LoopSound2(e\room\NPC[0]\Sound, e\room\NPC[0]\SoundChn, Camera, e\room\NPC[0]\Collider, 7.0)
			EndIf
		ElseIf e\eventstate[0]=7
			PositionEntity Collider, EntityX(e\room\obj,True),0.3,EntityZ(e\room\obj,True),True
			ResetEntity Collider
			LightFlash = 6
			BlurTimer = 500	
			;Injuries = PrevInjuries
			;Bloodloss = PrevBloodloss
			SecondaryLightOn = PrevSecondaryLightOn
			RotateEntity e\room\Objects[9],0,0,0,False
			
			;PrevInjuries = 0
			;PrevBloodloss = 0
			PrevSecondaryLightOn = 0.0
			Crouch = False
			CanSave = True
			For i = 0 To MaxItemAmount-1
				If Inventory[i] <> Null Then
					If Inventory[i]\itemtemplate\name = "Leaflet"
						RemoveItem(Inventory[i])
						Exit
					EndIf
				EndIf
			Next
			
			GiveAchievement(Achv1123)
			
			RemoveNPC(e\room\NPC[0])
			RemoveEvent(e)						
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D