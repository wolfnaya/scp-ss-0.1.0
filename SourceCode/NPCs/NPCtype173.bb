
; ~ SCP-173 constants

;[Block]
Const SCP173_ACTIVE = 0
Const SCP173_STATIONARY = 1
Const SCP173_BOXED = 2
Const SCP173_CONTAINED = 3
Const SCP173_DISABLED = 4
;[End Block]

Function CreateNPCtype173(n.NPCs)
	Local temp#
	Local cheat.Cheats = First Cheats
	
	n\NVName = "SCP-173"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.23, 0.32
	EntityType n\Collider, HIT_PLAYER
	n\Gravity = True
	
	n\obj = CopyEntity(NPCModel[Model_173])
	n\obj2 = CopyEntity(NPCModel[Model_173_Head])
	
	temp# = (GetINIFloat("DATA\NPCs.ini", "SCP-173", "scale") / MeshDepth(n\obj))
	ScaleEntity n\obj, temp,temp,temp
	ScaleEntity n\obj2, temp,temp,temp
	
	; ~ On Halloween set jack-o-latern texture.
	If (Left(CurrentDate(), 7) = "31 Oct ") Then
		HalloweenTex = True
		Local texFestive = LoadTexture_Strict("GFX\npcs\173\173h.pt", 1)
		EntityTexture n\obj, texFestive
		EntityTexture n\obj2, texFestive
		DeleteSingleTextureEntryFromCache texFestive
	; ~ ENDSHN's birthday
	ElseIf (Left(CurrentDate(), 7) = "11 Nov ") And RandomSeed = "ENDSHN" Then
		Local scaleX# = EntityScaleX(n\obj)
		Local scaleY# = EntityScaleY(n\obj)
		Local scaleZ# = EntityScaleZ(n\obj)
		cheat\OwO = True
		n\obj = FreeEntity_Strict(n\obj)
		n\obj = LoadAnimMesh_Strict("GFX\npcs\173\173body_owo.b3d")
		Animate n\obj, 1, 0.5
		n\obj2 = FreeEntity_Strict(n\obj2)
		n\obj2 = LoadAnimMesh_Strict("GFX\npcs\173\173head_owo.b3d")
		Animate n\obj2, 1, 0.5
		ScaleEntity n\obj, scaleX,scaleY,scaleZ
		ScaleEntity n\obj2, scaleX,scaleY,scaleZ
	EndIf
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-173", "speed") / 200.0)
	
	n\obj3 = LoadMesh_Strict("GFX\npcs\173\173box.b3d")
	ScaleEntity n\obj3, RoomScale, RoomScale, RoomScale
	HideEntity n\obj3
	
	n\CollRadius = 0.32
	n\HP = 100
End Function

Function UpdateNPCtype173(n.NPCs)
	Local Cheat.Cheats = First Cheats
	Local w.WayPoints,d.Doors,n2.NPCs,e.Events
	Local dist#,x#,z#,pvt%,dist2#
	Local i
	Local snd.Sound
	
		If Curr173\Idle <> SCP173_DISABLED Then
			dist# = EntityDistance(n\Collider, Collider)
			
			n\State[2] = 1
			
			PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.32, EntityZ(n\Collider))
			RotateEntity (n\obj, 0, EntityYaw(n\Collider)-180, 0)
			
			PositionEntity(n\obj2, EntityX(n\Collider), EntityY(n\Collider) - 0.32, EntityZ(n\Collider))
			RotateEntity (n\obj2, 0, (EntityYaw(n\Collider)-180)+n\Angle, 0)
			
			If n\Idle < SCP173_BOXED Then
				If n\Idle = SCP173_ACTIVE Then
					Local temp% = False
					Local move% = True
					If dist < 15 Then
						If dist < 10.0 Then 
							If EntityVisible(n\Collider, Collider) Then
								temp = True
								n\EnemyX = EntityX(Collider, True)
								n\EnemyY = EntityY(Collider, True)
								n\EnemyZ = EntityZ(Collider, True)
							EndIf
						EndIf
						
						Local SoundVol# = Max(Min((Distance(EntityX(n\Collider), n\PrevX, EntityZ(n\Collider), n\PrevZ) * 2.5), 1.0), 0.0)
						n\SoundChn = LoopSound2(StoneDragSFX, n\SoundChn, Camera, n\Collider, 10.0, n\State[0])
						
						n\PrevX = EntityX(n\Collider)
						n\PrevZ = EntityZ(n\Collider)				
						
						If (BlinkTimer < - 16 Lor BlinkTimer > - 6) Then
							If EntityInView(n\obj, Camera) Lor EntityInView(n\obj2, Camera) Then move = False
						EndIf
					EndIf
					
					If (I_268\Using > 0 And I_268\Timer > 0.0) Then move = True
					
					;player is looking at it -> doesn't move
					If move=False Then
						BlurVolume = Max(Max(Min((4.0 - dist) / 6.0, 0.9), 0.1), BlurVolume)
						CurrCameraZoom = Max(CurrCameraZoom, (Sin(Float(MilliSecs())/20.0)+1.0)*15.0*Max((3.5-dist)/3.5,0.0))								
						
						If dist < 3.5 And MilliSecs() - n\LastSeen > 60000 And temp Then
							i = Rand(3,4)
							If Cheat\Mini173 Then
								snd = Object.Sound(HorrorSFX[i])
								snd\internalHandle = LoadSound(snd\name)
								SoundPitch(snd\internalHandle,48000)
							EndIf
							PlaySound_Strict(HorrorSFX[i])
							
							n\LastSeen = MilliSecs()
						EndIf
						
						If dist < 1.5 And Rand(700) = 1 Then
							i = Rand(0,2)
							If Cheat\Mini173 Then
								snd = Object.Sound(Scp173SFX[i])
								snd\internalHandle = LoadSound(snd\name)
								SoundPitch(snd\internalHandle,48000)
							EndIf
							PlaySound2(Scp173SFX[i], Camera, n\obj)
						EndIf
						
						If dist < 1.5 And n\LastDist > 2.0 And temp Then
							CurrCameraZoom = 40.0
							HeartBeatRate = Max(HeartBeatRate, 140)
							HeartBeatVolume = 0.75
							
							Select Rand(5)
								Case 1
									i = 1
								Case 2
									i = 2
								Case 3
									i = 9
								Case 4
									i = 10
								Case 5
									i = 14
							End Select
							If Cheat\Mini173 Then
								snd = Object.Sound(HorrorSFX[i])
								snd\internalHandle = LoadSound(snd\name)
								SoundPitch(snd\internalHandle,48000)
							EndIf
							PlaySound_Strict(HorrorSFX[i])
						EndIf									
						
						n\LastDist = dist
						
						n\State[0] = Max(0, n\State[0] - FPSfactor / 20)
					Else 
						;more than 6 room lengths away from the player -> teleport to a room closer to the player
						If dist > 50 Then
							If Rand(70)=1 Then
								If PlayerRoom\RoomTemplate\Name <> "gate_b_topside" And PlayerRoom\RoomTemplate\Name <> "gate_a_topside" And PlayerRoom\RoomTemplate\Name <> "pocketdimension" Then
									For w.WayPoints = Each WayPoints
										If w\door=Null And Rand(5)=1 Then
											x = Abs(EntityX(Collider)-EntityX(w\obj,True))
											If x < 25.0 And x > 15.0 Then
												z = Abs(EntityZ(Collider)-EntityZ(w\obj,True))
												If z < 25 And z > 15.0 Then
													DebugLog "MOVING 173 TO "+w\room\RoomTemplate\Name
													TeleportEntity n\Collider, EntityX(w\obj,True), EntityY(w\obj,True)+0.25,EntityZ(w\obj,True),n\CollRadius
													Exit
												EndIf
											EndIf
										EndIf
									Next
								EndIf
							EndIf
						ElseIf dist > HideDistance*0.8 ;3-6 rooms away from the player -> move randomly from waypoint to another
							If Rand(70)=1 Then TeleportCloser(n)
						Else ;less than 3 rooms away -> actively move towards the player
							n\State[0] = CurveValue(SoundVol, n\State[0], 3)
							
							;try to open doors
							If Rand(20) = 1 Then
								For d.Doors = Each Doors
									If (Not d\locked) And d\open = False And d\Code = "" And d\KeyCard=0 Then
										For i% = 0 To 1
											If d\buttons[i] <> 0 Then
												If Abs(EntityX(n\Collider) - EntityX(d\buttons[i])) < 0.5 Then
													If Abs(EntityZ(n\Collider) - EntityZ(d\buttons[i])) < 0.5 Then
														If (d\openstate >= 180 Lor d\openstate <= 0) Then
															pvt = CreatePivot()
															PositionEntity pvt, EntityX(n\Collider), EntityY(n\Collider) + 0.5, EntityZ(n\Collider)
															PointEntity pvt, d\buttons[i]
															MoveEntity pvt, 0, 0, n\Speed * 0.6
															
															If EntityPick(pvt, 0.5) = d\buttons[i] Then 
																PlaySound_Strict (LoadTempSound("SFX\Door\DoorOpen173.ogg"))
																UseDoor(d,False)
															EndIf
															
															pvt = FreeEntity_Strict(pvt)
														EndIf
													EndIf
												EndIf
											EndIf
										Next
									EndIf
								Next
							EndIf
							
							If (I_268\Using > 0 And I_268\Timer > 0.0) Then
								temp = False
								n\EnemyX = 0
								n\EnemyY = 0
								n\EnemyZ = 0
							EndIf
							
							Local stored_dist# = PowTwo(dist)
							n\Target = Null
							For n2 = Each NPCs
								If n2\NPCtype = NPC_NTF And n2\HP > 0 Then
									dist2 = EntityDistanceSquared(n\Collider, n2\Collider)
									If dist2 < stored_dist Then
										stored_dist = dist2
										n\Target = n2
										If stored_dist < PowTwo(0.65) Then
											n2\HP = 0
											PlaySound2(NeckSnapSFX[Rand(0,2)], Camera, n\Collider)
										EndIf
									EndIf
								ElseIf n2\NPCtype = NPC_CI And n2\HP > 0 Then
									dist2 = EntityDistanceSquared(n\Collider, n2\Collider)
									If dist2 < stored_dist Then
										stored_dist = dist2
										n\Target = n2
										If stored_dist < PowTwo(0.65) Then
											n2\HP = 0
											PlaySound2(NeckSnapSFX[Rand(0,2)], Camera, n\Collider)
										EndIf
									EndIf
								ElseIf n2\NPCtype = NPC_Class_D And n2\HP > 0 Then
									dist2 = EntityDistanceSquared(n\Collider, n2\Collider)
									If dist2 < stored_dist Then
										stored_dist = dist2
										n\Target = n2
										If stored_dist < PowTwo(0.65) And EntityDistanceSquared(Collider,n\Collider) < PowTwo(8.0) Then
											n2\HP = 0
											PlaySound2(NeckSnapSFX[Rand(0,2)], Camera, n\Collider)
										EndIf
									EndIf
								EndIf
							Next
							If n\Target <> Null Then
								PointEntity(n\Collider, n\Target\Collider)
								RotateEntity n\Collider, 0, EntityYaw(n\Collider), 0
								TranslateEntity n\Collider, Cos(EntityYaw(n\Collider) + 90.0) * n\Speed * FPSfactor, 0.0, Sin(EntityYaw(n\Collider) + 90.0) * n\Speed * FPSfactor
							EndIf
							
;							If n\State[3] <> 0 Then
;								Curr173\Idle = SCP173_STATIONARY
;							EndIf
;							If n\State[3] = 70*5 Then
;								n\State[3] = n\State[3] - FPSfactor
;							EndIf
							
							;player is not looking and is visible from 173's position -> attack
							If temp And n\Target = Null Then
								n\Angle = DeltaYaw(n\Collider, Camera)
								If dist < 0.65 Then
									If KillTimer >= 0 And (Not GodMode) Then
										Select PlayerRoom\RoomTemplate\Name
											Case "lockroom_1", "room2_closets"
												m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp173_death_1",Designation)
											Default 
												m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp173_death_2",Designation)
										End Select
										
										If (Not GodMode) Then n\Idle = SCP173_STATIONARY
										If (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" And hds\Health > 50) Then
											PlaySound_Strict(NeckSnapSFX[Rand(0,2)])
											If Rand(2) = 1 Then 
												TurnEntity(Camera, 0, Rand(80,100), 0)
											Else
												TurnEntity(Camera, 0, Rand(-100,-80), 0)
											EndIf
											Kill()
										Else
											hds\Health = Max(hds\Health - 90, 0.0)
											;n\State[3] = 70*5
											PlaySound_Strict(LoadTempSound("SFX\SCP\173\NeckSnapHDS.ogg"))
										EndIf
									EndIf
								Else
									PointEntity(n\Collider, Collider)
									RotateEntity n\Collider, 0, EntityYaw(n\Collider), 0
									TranslateEntity n\Collider, Cos(EntityYaw(n\Collider) + 90.0) * n\Speed * FPSfactor, 0.0, Sin(EntityYaw(n\Collider) + 90.0) * n\Speed * FPSfactor
								EndIf
							Else ;player is not visible -> move to the location where he was last seen
								If n\EnemyX <> 0 Then
									n\Angle = DeltaYaw(n\Collider,Camera)
									If DistanceSquared(EntityX(n\Collider), n\EnemyX, EntityZ(n\Collider), n\EnemyZ) > PowTwo(0.5) Then
										AlignToVector(n\Collider, n\EnemyX-EntityX(n\Collider), 0, n\EnemyZ-EntityZ(n\Collider), 3)
										MoveEntity(n\Collider, 0, 0, n\Speed * FPSfactor)
										If Rand(500) = 1 Then
											n\EnemyX = 0
											n\EnemyY = 0
											n\EnemyZ = 0
										EndIf
									Else
										n\EnemyX = 0 : n\EnemyY = 0 : n\EnemyZ = 0
									EndIf
								Else
									If Rand(400)=1 Then RotateEntity (n\Collider, 0, Rnd(360), 0)
									TranslateEntity n\Collider, Cos(EntityYaw(n\Collider) + 90.0) * n\Speed * FPSfactor, 0.0, Sin(EntityYaw(n\Collider) + 90.0) * n\Speed * FPSfactor
									n\Angle = Rnd(-120, 120)
								EndIf
							EndIf
						EndIf ; less than 2 rooms away from the player
					EndIf
				EndIf ;idle = false
				
				PositionEntity(n\Collider, EntityX(n\Collider), Min(EntityY(n\Collider), 0.35), EntityZ(n\Collider))
				
				HideEntity n\obj3
				
				If Curr173\Contained Then
					n\Idle = SCP173_CONTAINED
					PositionEntity n\Collider, 0, -500, 0
					ResetEntity n\Collider
				EndIf
			ElseIf n\Idle = SCP173_BOXED Lor n\Idle = SCP173_CONTAINED Then
				If n\Idle = SCP173_BOXED Then
					PointEntity n\obj, Collider
					RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0, True
					MoveEntity n\Collider, 0, 0, 0.016 * FPSfactor * Max(Min((EntityDistanceSquared(n\Collider, Collider) * PowTwo(2) - 1.0), 1.0), -1.0)
					
					n\GravityMult = 1.0
					
					If EntityDistanceSquared(n\Collider, Collider) > PowTwo(HideDistance) Then
						TeleportEntity(n\Collider, EntityX(Collider), EntityY(Collider) + 0.1, EntityZ(Collider), n\CollRadius)
						RotateEntity n\Collider, 0, EntityYaw(Collider), 0
						MoveEntity n\Collider, 0, 0, -0.5
					EndIf
					
					If PlayerRoom\RoomTemplate\Name = "pocketdimension" Then
						n\Idle = SCP173_ACTIVE
						FailTask(TASK_NTF_173_TO_CHAMBER)
						BeginTask(TASK_NTF_CONTAIN_173)
					EndIf
				EndIf
				
				PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) + 0.05 + Sin(MilliSecs()*0.08)*0.02, EntityZ(n\Collider))
				RotateEntity (n\obj, 0, EntityYaw(n\Collider)-180, 0)
				
				PositionEntity(n\obj2, EntityX(n\Collider), EntityY(n\Collider) + 0.05 + Sin(MilliSecs()*0.08)*0.02, EntityZ(n\Collider))
				RotateEntity (n\obj2, 0, (EntityYaw(n\Collider)-180)+n\Angle, 0)
				
				ShowEntity n\obj3
				
				PositionEntity(n\obj3, EntityX(n\Collider), EntityY(n\Collider) - 0.05 + Sin(MilliSecs()*0.08)*0.02, EntityZ(n\Collider))
				RotateEntity (n\obj3, 0, EntityYaw(n\Collider)-180, 0)
			EndIf
		EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D