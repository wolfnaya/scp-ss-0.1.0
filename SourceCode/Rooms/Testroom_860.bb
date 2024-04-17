
Function FillRoom_Testroom_860(r.Rooms)
	Local d.Doors,it.Items
	
	;the wooden door
	r\Objects[2] = LoadMesh_Strict("GFX\map\forest\door_frame.b3d")
	PositionEntity r\Objects[2],r\x + 184.0 * RoomScale,0,r\z,True
	ScaleEntity r\Objects[2],45.0*RoomScale,45.0*RoomScale,80.0*RoomScale,True
	EntityParent r\Objects[2],r\obj
	
	r\Objects[3] =  LoadMesh_Strict("GFX\map\forest\door.b3d")
	PositionEntity r\Objects[3],r\x + 112.0 * RoomScale,0,r\z+0.05,True
	EntityType r\Objects[3], HIT_MAP
	
	ScaleEntity r\Objects[3],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True
	EntityParent r\Objects[3],r\obj
	
	r\Objects[4] = CopyEntity(r\Objects[3])
	PositionEntity r\Objects[4],r\x + 256.0 * RoomScale,0,r\z-0.05,True
	RotateEntity r\Objects[4], 0,180,0
	ScaleEntity r\Objects[4],46.0*RoomScale,45.0*RoomScale,46.0*RoomScale,True
	EntityParent r\Objects[4],r\obj
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x+928.0*RoomScale,0,r\z+640.0*RoomScale,0,r,True,False,False,"ABCD")
	r\RoomDoors[0]\AutoClose = False
	d = CreateDoor(r\zone, r\x + 928.0 * RoomScale,0,r\z - 640.0 * RoomScale,0,r,True,False,False,"ABCD")
	d\AutoClose = False
	
	;doors to the room itself
	d = CreateDoor(r\zone, r\x+416.0*RoomScale,0,r\z - 640.0 * RoomScale,0,r,False,False,1)
	d = CreateDoor(r\zone, r\x+416.0*RoomScale,0,r\z + 640.0 * RoomScale,0,r,False,False,1)
	
	;the forest
	Local fr.Forest = New Forest
	r\fr=fr
	GenForestGrid(fr)
	PlaceForest(fr,r\x,r\y+30.0,r\z,r)
	
	it = CreateItem("Document SCP-860-1", "paper", r\x + 672.0 * RoomScale, r\y + 176.0 * RoomScale, r\z + 335.0 * RoomScale)
	RotateEntity it\collider, 0, r\angle+10, 0
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Document SCP-860", "paper", r\x + 1152.0 * RoomScale, r\y + 176.0 * RoomScale, r\z - 384.0 * RoomScale)
	RotateEntity it\collider, 0, r\angle+170, 0
	EntityParent(it\collider, r\obj)
	
End Function

Function UpdateEvent_Testroom_860(e.Events)
	Local x%,pvt%,ang#
	Local i
	
	;e\eventstate[0] = is the player in the forest
	;e\EventState[1] = which side of the door did the player enter from
	;e\EventState[2] = monster spawn timer
	
	Local fr.Forest=e\room\fr
	
	If PlayerRoom = e\room And fr<>Null Then
		
		If e\EventState[0]=1.0 Then ;the player is in the forest
			CurrStepSFX = 2
			
			Curr106\Idle = True
			
			UpdateForest(fr,Collider)
			
			If e\EventStr = "" And QuickLoadPercent = -1
				QuickLoadPercent = 0
				QuickLoad_CurrEvent = e
				e\EventStr = "load0"
			EndIf
			
			If e\room\NPC[0]<>Null Then
				If (e\room\NPC[0]\State[1] = 1 And e\room\NPC[0]\State[0]>1) Lor e\room\NPC[0]\State[0]>2 ;the monster is chasing the player
					ShouldPlay = 12
				Else
					ShouldPlay = 9
				EndIf
			EndIf
			
			;the player fell
			If (Not NoClip)
				If EntityY(Collider)<=28.5 Then 
					Kill() 
					BlinkTimer=-2
				ElseIf EntityY(Collider)>EntityY(fr\Forest_Pivot,True)+0.5
					MoveEntity(Collider, 0, ((EntityY(fr\Forest_Pivot,True)+0.5) - EntityY(Collider))*FPSfactor, 0)
				EndIf
			EndIf
			
			If e\room\NPC[0]<>Null
				If e\room\NPC[0]\State[0] = 0 Lor EntityDistanceSquared(Collider, e\room\NPC[0]\Collider)>PowTwo(20.0) Then
					e\EventState[2] = e\EventState[2] + (1+CurrSpeed)* FPSfactor
					If (e\EventState[2] Mod 500) < 10.0 And ((e\EventState[2]-FPSfactor) Mod 500) > 490.0 Then
						If e\EventState[2] > 3000-(500*SelectedDifficulty\aggressiveNPCs) And Rnd(10000+(500*SelectedDifficulty\aggressiveNPCs)) < e\EventState[2]
							e\room\NPC[0]\State[0]=2
							PositionEntity e\room\NPC[0]\Collider, 0,-110,0
							e\EventState[2]=e\EventState[2]-Rnd(1000,2000-(500*SelectedDifficulty\aggressiveNPCs))
							DebugLog "attack"
						Else
							e\room\NPC[0]\State[0]=1
							PositionEntity e\room\NPC[0]\Collider, 0,-110,0
							DebugLog "spawn"
						EndIf
					EndIf
				EndIf
			EndIf
			
			For i = 0 To 1
				If EntityDistanceSquared(fr\Door[i], Collider)<PowTwo(0.5) Then
					If EntityInView(fr\Door[i], Camera) Then
						DrawHandIcon = True
						If keyhituse Then
							If i=e\EventState[1] Then
								IsCutscene = True
								BlinkTimer = -10
								IsCutscene = False
								
								PlaySound_Strict(LoadTempSound("SFX\Door\WoodenDoorOpen.ogg"))
								
								RotateEntity e\room\Objects[3], 0, 0, 0
								RotateEntity e\room\Objects[4], 0, 180, 0
								PositionEntity Collider, EntityX(e\room\Objects[2],True),0.5,EntityZ(e\room\Objects[2],True)
								
								RotateEntity Collider, 0, EntityYaw(e\room\obj,True)+e\EventState[1]*180, 0
								MoveEntity Collider, 0,0,1.5
								
								ResetEntity Collider
								
								d_I\UpdateDoorsTimer = 0
								UpdateDoors()
								
								SecondaryLightOn = PrevSecondaryLightOn
								
								e\EventState[0] = 0.0
								
							Else
								PlaySound_Strict(LoadTempSound("SFX\Door\WoodenDoorBudge.ogg"))
								CreateMsg(GetLocalString("Singleplayer", "testroom_860_1"))
							EndIf
						EndIf
					EndIf
				EndIf
			Next
			
			If e\room\NPC[0]<>Null
				x = Max(1.0-(e\room\NPC[0]\State[2]/300.0),0.1)
			Else
				x = 2.0
			EndIf
			
			CameraClsColor Camera,98*x,133*x,162*x
			CameraRange Camera,RoomScale,8.5
			CameraFogRange Camera,0.5,8.0
			CameraFogColor Camera,98*x,133*x,162*x
			
		Else
			
			If (Not Curr106\Contained) Then Curr106\Idle = False
			
			If EntityYaw(e\room\Objects[3])=0.0 Then
				HideEntity fr.Forest\Forest_Pivot
				If (DistanceSquared(EntityX(e\room\Objects[3],True),EntityX(Collider,True),EntityZ(e\room\Objects[3],True),EntityZ(Collider,True))<1.0) Then
					DrawHandIcon = True
					
					If SelectedItem = Null Then
						If keyhituse Then
							PlaySound_Strict(LoadTempSound("SFX\Door\WoodenDoorBudge.ogg"))
							CreateMsg(GetLocalString("Singleplayer", "testroom_860_1"))
						EndIf
					ElseIf SelectedItem\itemtemplate\tempname="scp860" 
						If keyhituse Then
							PlaySound_Strict(LoadTempSound("SFX\Door\WoodenDoorOpen.ogg"))
							ShowEntity fr.Forest\Forest_Pivot
							SelectedItem = Null
							
							IsCutscene = True
							BlinkTimer = -10
							IsCutscene = False
							
							e\EventState[0]=1.0
							
							;reset monster spawn timer
							e\EventState[2] = 0.0
							
							If e\room\NPC[0]<>Null Then
								;reset monster to the (hidden) idle state
								e\room\NPC[0]\State[0] = 0
							EndIf
							
							PrevSecondaryLightOn = SecondaryLightOn
							SecondaryLightOn = True
							
							pvt = CreatePivot()
							PositionEntity pvt, EntityX(Camera),EntityY(Camera),EntityZ(Camera)
							PointEntity pvt, e\room\obj
							ang# = WrapAngle(EntityYaw(pvt)-EntityYaw(e\room\obj,True))
							If ang > 90 And ang < 270 Then
								PositionEntity Collider,EntityX(fr\Door[0],True),EntityY(fr\Door[0],True)+EntityY(Collider,True)+0.5,EntityZ(fr\Door[0],True),True
								RotateEntity Collider, 0.0, EntityYaw(fr\Door[0],True)-180, 0.0, True
								MoveEntity Collider, -0.5,0.0,0.5
								e\EventState[1] = 1
							Else
								PositionEntity Collider,EntityX(fr\Door[1],True),EntityY(fr\Door[1],True)+EntityY(Collider,True)+0.5,EntityZ(fr\Door[1],True),True
								RotateEntity Collider, 0.0, EntityYaw(fr\Door[1],True)-180, 0.0, True
								MoveEntity Collider, -0.5,0.0,0.5
								e\EventState[1] = 0
							EndIf
							pvt = FreeEntity_Strict(pvt)
							
							ResetEntity Collider
						EndIf
					EndIf
				EndIf
			EndIf
			
		EndIf
		
	Else
		If (fr=Null) Then
			RemoveEvent(e)
		Else
			If (fr\Forest_Pivot<>0) Then HideEntity fr\Forest_Pivot
		EndIf
	EndIf
	
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D