
; ~ SCP-096 Constant

;[Block]
Const SCP096_SIT = 0
Const SCP096_GETUP = 1
Const SCP096_LOOP = 2
Const SCP096_STARTRAGE = 3
Const SCP096_CHASE = 4
Const SCP096_WALKING = 5
Const SCP096_STUNNED = 6
;[End Block]

Function CreateNPCtype096(n.NPCs)
	Local temp#, headBone
	
	n\NVName = "SCP-096"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.3
	EntityType n\Collider, HIT_PLAYER
	n\obj = CopyEntity(NPCModel[Model_096])
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-096", "speed") / 100.0)
	
	temp = (GetINIFloat("DATA\NPCs.ini", "SCP-096", "scale") / 3.0)
	ScaleEntity n\obj, temp, temp, temp	
	
	MeshCullBox (n\obj, -MeshWidth(n\obj)*2, -MeshHeight(n\obj)*2, -MeshDepth(n\obj)*2, MeshWidth(n\obj)*2, MeshHeight(n\obj)*4, MeshDepth(n\obj)*4)
	
	n\obj2 = CreateSprite(FindChild(n\obj, "Reyelid"))
	ScaleSprite(n\obj2, 0.07, 0.08)
	EntityOrder(n\obj2, -5)
	EntityTexture(n\obj2, DarkTexture)
	HideEntity(n\obj2)
	
	n\Sound = LoadSound_Strict("SFX\Music\SCPs\096.ogg")
	
	n\CollRadius = 0.3
	n\HP = 100
End Function

Function UpdateNPCtype096(n.NPCs)
	Local Dist# = EntityDistanceSquared(Collider, n\Collider)
	Local Angle# = WrapAngle(DeltaYaw(n\Collider, Collider))
	Local Pvt, de.Decals, i, Dist2#
	
	If Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 8) = "scramble" And Dist < PowTwo(CameraFogFar * LightVolume) And (Angle < 135.0 Lor Angle > 225.0) And EntityVisible(Camera, n\obj2) Then
		If EntityHidden(n\obj2) Then ShowEntity(n\obj2)
		ScaleSprite(n\obj2, Rnd(0.06, 0.08), Rnd(0.07, 0.09))
		PositionEntity(n\obj2, Rnd(0.1) - 0.05, Rnd(0.1) - 0.05, Rnd(0.1) - 0.05)
	Else
		If (Not EntityHidden(n\obj2)) Then HideEntity(n\obj2)
	EndIf
	
	Select n\State[0]
		Case 0.0
			;[Block]
			If Dist < 64.0 Then
				GiveAchievement(Achv096)
				If (Not Curr096\Idle) Then
					n\SoundChn = LoopSound2(n\Sound, n\SoundChn, Camera, n\Collider, 6.0, 0.6)
				EndIf
				
				If n\State[2] = -1.0
					AnimateNPC(n, 936.0, 1263.0, 0.1, False)
					If n\Frame >= 1262.9
						SetNPCFrame(n, 312.0)
						n\State[2] = 0.0
						n\State[0] = 5.0
					EndIf
				Else
					AnimateNPC(n, 936.0, 1263.0, 0.1)
					If n\State[2] < 70.0 * 6.0
						n\State[2] = n\State[2] + FPSfactor
					Else
						If Rand(5) = 1
							n\State[2] = -1.0
						Else
							n\State[2] = 70.0 * Rand(0, 3)
						EndIf
					EndIf
				EndIf
				
				If (I_268\Using <> 3) Then
					If Dist < PowTwo(CameraFogFar * LightVolume) Then
						If (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 8) = "scramble") And (Angle < 135.0 Lor Angle > 225.0) And (EntityVisible(Camera, n\obj2) And EntityInView(n\obj2, Camera)) Then
							If (BlinkTimer < -16.0 Lor BlinkTimer > -6.0) And LightBlink <= 0.0
								PlaySound_Strict(LoadTempSound("SFX\SCP\096\Triggered.ogg"))
								
								CurrCameraZoom = 10.0
								
								SetNPCFrame(n, 194.0)
								
								StopStream_Strict(n\SoundChn) : n\SoundChn=0
								n\Sound = 0
								
								n\State[2] = 0.0
								n\State[0] = 1.0
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
			;[End Block]
		Case 4.0
			;[Block]
			CurrCameraZoom = CurveValue(Max(CurrCameraZoom, (Sin(Float(MilliSecs()) / 20.0) + 1.0) * 10.0), CurrCameraZoom, 8.0)
			
			If n\Target = Null Then 
				If (Not Curr096\Idle) Then
					If n\SoundChn = 0
						n\SoundChn = StreamSound_Strict("SFX\SCP\096\Scream.ogg",0)
						n\SoundChn_IsStream = True
					Else
						UpdateStreamSoundOrigin(n\SoundChn,Camera,n\Collider,7.5,1.0)
					EndIf
					
					If opt\MusicVol > 0 Then
						If n\SoundChn2 = 0
							n\SoundChn2 = StreamSound_Strict("SFX\Music\Chase_Music\096_Chase.ogg",0)
							n\SoundChn2_IsStream = 2
						Else
							SetStreamVolume_Strict(n\SoundChn2,Min(Max(8.0-Sqr(Dist),0.6),1.0)*(opt\VoiceVol*opt\MasterVol))
						EndIf
					EndIf
				EndIf
			EndIf
			
			If I_268\Using = 3 And n\Target = Null Then n\State[0] = 5.0
			
			If (Not IsSPPlayerAlive) Then
				If MilliSecs() > n\State[2] Then
					n\LastSeen = 0
					If n\Target = Null Then
						If EntityVisible(Collider, n\Collider) Then n\LastSeen = 1
					Else
						If EntityVisible(n\Target\Collider, n\Collider) Then n\LastSeen = 1
					EndIf
					n\State[2] = MilliSecs() + 3000.0
				EndIf
				
				If n\LastSeen = 1 Then
					n\PathTimer = Max(70.0 * 3.0, n\PathTimer)
					n\PathStatus = 0
					
					If n\Target <> Null Then Dist = EntityDistanceSquared(n\Target\Collider, n\Collider)
					
					If Dist < 7.84 Lor n\Frame < 150.0 Then 
						If n\Frame > 193.0 Then n\Frame = 2.0 ; ~ Go to the start of the jump animation
						
						AnimateNPC(n, 2.0, 193.0, 0.7)
						
						If Dist > 1.0 Then 
							n\CurrSpeed = CurveValue(n\Speed * 2.0, n\CurrSpeed, 15.0)
						Else
							n\CurrSpeed = 0.0
							
							If n\Target = Null Then
								If (Not GodMode) Then 
									Pvt = CreatePivot()
									CameraShake = 30.0
									BlurTimer = 2000.0
									m_msg\DeathTxt = GetLocalStringR("Singleplayer", "scp096_death", Designation)
									PlaySound_Strict DamageSFX[4]
									Kill()
									KillAnim = 1
									For i = 0 To 6
										PositionEntity(Pvt, EntityX(Collider) + Rnd(-0.1, 0.1), EntityY(Collider) - 0.05, EntityZ(Collider) + Rnd(-0.1, 0.1))
										TurnEntity(Pvt, 90.0, 0.0, 0.0)
										EntityPick(Pvt, 0.3)
										
										de.Decals = CreateDecal(Rand(DECAL_BLOODDROP1, DECAL_BLOODDROP2), PickedX(), PickedY() + 0.005, PickedZ(), 90.0, Rnd(360.0), 0.0)
										ScaleEntity de\obj,Rnd(0.2, 0.6),Rnd(0.2, 0.6),Rnd(0.2, 0.6)
									Next
									FreeEntity(Pvt)
								EndIf
							EndIf				
						EndIf
						
						If n\Target = Null Then
							PointEntity(n\Collider, Collider)
						Else
							PointEntity(n\Collider, n\Target\Collider)
						EndIf
					Else
						If n\Target = Null Then 
							PointEntity(n\obj, Collider)
						Else
							PointEntity(n\obj, n\Target\Collider)
						EndIf
						
						RotateEntity(n\Collider, 0.0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 5.0), 0.0)
						
						If n\Frame > 847.0 Then n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
						
						If n\Frame < 906 Then
							AnimateNPC(n, 737.0, 906.0, n\Speed * 8.0, False)
						Else
							AnimateNPC(n, 907.0, 935.0, n\CurrSpeed * 8.0)
						EndIf
					EndIf
					
					RotateEntity(n\Collider, 0.0, EntityYaw(n\Collider), 0.0, True)
					MoveEntity(n\Collider, 0.0, 0.0, n\CurrSpeed * FPSfactor)
				Else
					If n\PathStatus = 1 Then
						If n\Path[n\PathLocation] = Null Then 
							If n\PathLocation > 19 Then 
								n\PathLocation = 0 : n\PathStatus = 0
							Else
								n\PathLocation = n\PathLocation + 1
							EndIf
						Else
							PointEntity(n\obj, n\Path[n\PathLocation]\obj)
							
							RotateEntity(n\Collider, 0.0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 5.0), 0.0)
							
							If n\Frame > 847.0 Then n\CurrSpeed = CurveValue(n\Speed * 1.5, n\CurrSpeed, 15.0)
							MoveEntity(n\Collider, 0.0, 0.0, n\CurrSpeed * FPSfactor)
							
							If n\Frame < 906.0 Then
								AnimateNPC(n, 737.0, 906.0, n\Speed * 8.0, False)
							Else
								AnimateNPC(n, 907.0, 935.0, n\CurrSpeed * 8.0)
							EndIf
							
							Dist2 = EntityDistanceSquared(n\Collider, n\Path[n\PathLocation]\obj)
							If Dist2 < 0.64 Then
								If n\Path[n\PathLocation]\door <> Null Then
									If (Not n\Path[n\PathLocation]\door\open) Then
										n\Path[n\PathLocation]\door\open = True
										n\Path[n\PathLocation]\door\fastopen = 1
										PlaySound2(OpenDoorFastSFX, Camera, n\Path[n\PathLocation]\door\obj)
									EndIf
								EndIf							
								If Dist2 < 0.49 Then n\PathLocation = n\PathLocation + 1
							EndIf 
						EndIf
					Else
						AnimateNPC(n, 1471.0, 1556.0, 0.1)
						
						n\PathTimer = Max(0.0, n\PathTimer - FPSfactor)
						If n\PathTimer <= 0 Then
							If n\Target <> Null Then
								n\PathStatus = FindPath(n, EntityX(n\Target\Collider), EntityY(n\Target\Collider) + 0.2, EntityZ(n\Target\Collider))	
							Else
								n\PathStatus = FindPath(n, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))	
							EndIf
							n\PathTimer = 70.0 * 5.0
						EndIf
					EndIf
				EndIf
				
				If Rand(50) = 1 Then
					If Dist > 1024.0 Lor EntityY(n\Collider) < -50.0 Then TeleportCloser(n)
				EndIf
			Else
				AnimateNPC(n, Min(27.0, AnimTime(n\obj)), 193.0, 0.5)
			EndIf
			;[End Block]
		Case 1.0, 2.0, 3.0
			;[Block]
			If (Not Curr096\Idle) Then
				If n\SoundChn = 0
					n\SoundChn = StreamSound_Strict("SFX\Music\SCPs\096_Angered.ogg",0)
					n\SoundChn_IsStream = True
				Else
					UpdateStreamSoundOrigin(n\SoundChn,Camera,n\Collider,10.0,opt\VoiceVol*opt\MasterVol)
				EndIf
			EndIf
			
			If n\State[0] = 1.0 Then ; ~ Get up
				If n\Frame < 312.0 Then
					AnimateNPC(n, 193.0, 311.0, 0.3, False)
					If n\Frame > 310.9 Then
						SetNPCFrame(n, 737.0)
						n\State[0] = 2.0
					EndIf
				ElseIf n\Frame >= 312.0 And n\Frame <= 422.0
					AnimateNPC(n, 312.0, 422.0, 0.3, False)
					If n\Frame > 421.9 Then SetNPCFrame(n, 677.0)
				Else
					AnimateNPC(n, 677.0, 736.0, 0.3, False)
					If n\Frame > 735.9 Then
						SetNPCFrame(n, 737.0)
						n\State[0] = 2.0
					EndIf
				EndIf
			ElseIf n\State[0] = 2.0
				AnimateNPC(n, 677.0, 737.0, 0.3, False)
				If n\Frame >= 737.0 Then n\State[0] = 3.0 : n\State[1] = 0.0
			ElseIf n\State[0] = 3.0
				n\State[1] = n\State[1] + FPSfactor
				If n\State[1] > 70.0 * 26.0 Then
					AnimateNPC(n, 823.0, 847.0, n\Speed * 8.0, False)
					If n\Frame > 846.9 Then
						StopStream_Strict(n\SoundChn)
						n\State[0] = 4.0
					EndIf
				Else
					AnimateNPC(n, 1471.0, 1556.0, 0.4)
				EndIf
			EndIf
			;[End Block]
		Case 5.0
			;[Block]
			If Dist < 256.0 Then 
				If Dist < 16.0 Then GiveAchievement(Achv096)
				If (Not Curr096\Idle) Then
					n\SoundChn = LoopSound2(n\Sound, n\SoundChn, Camera, n\Collider, 6.0, 0.6)
;					If n\SoundChn = 0
;						n\SoundChn = StreamSound_Strict("SFX\Music\SCPs\096.ogg",0)
;						n\SoundChn_IsStream = True
;					Else
;						UpdateStreamSoundOrigin(n\SoundChn,Camera,n\Collider,14.0,opt\VoiceVol*opt\MasterVol)
;					EndIf
				EndIf
				
				If n\Frame >= 422.0 Then
					n\State[1] = n\State[1] + FPSfactor
					If n\State[1] > 1000.0 Then
						If n\State[1] > 1600.0 Then n\State[1] = Rnd(0.0, 500.0)
						
						If n\Frame < 1382.0 Then
							n\CurrSpeed = CurveValue(n\Speed * 0.1, n\CurrSpeed, 5.0)
							AnimateNPC(n, 1369.0, 1382.0, n\CurrSpeed * 45.0, False)
						Else
							n\CurrSpeed = CurveValue(n\Speed * 0.1, n\CurrSpeed, 5.0)
							AnimateNPC(n, 1383.0, 1456.0, n\CurrSpeed * 45.0)
						EndIf
						
						If MilliSecs() > n\State[2] Then
							n\LastSeen = 0
							If EntityVisible(Collider, n\Collider) Then 
								n\LastSeen = 1
							Else
								If (Not EntityHidden(n\Collider)) Then HideEntity(n\Collider)
								EntityPick(n\Collider, 1.5)
								If PickedEntity() <> 0 Then
									n\Angle = EntityYaw(n\Collider) + Rnd(80.0, 110.0)
								EndIf
								If EntityHidden(n\Collider) Then ShowEntity(n\Collider)
							EndIf
							n\State[2] = MilliSecs() + 3000.0
						EndIf
						
						If n\LastSeen Then 
							PointEntity(n\obj, Collider)
							RotateEntity(n\Collider, 0.0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 130.0), 0.0)
							If Dist < 2.25 Then n\State[1] = 0.0
						Else
							RotateEntity(n\Collider, 0.0, CurveAngle(n\Angle, EntityYaw(n\Collider), 50.0), 0.0)
						EndIf
					Else
						If n\Frame > 472.0 Then ; ~ Walk to idle
							n\CurrSpeed = CurveValue(n\Speed * 0.05, n\CurrSpeed, 8.0)
							AnimateNPC(n, 1383.0, 1469.0, n\CurrSpeed * 45.0, False)
							If n\Frame >= 1468.9 Then SetNPCFrame(n, 423.0)
						Else
							n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 4.0)	
							AnimateNPC(n, 423.0, 471.0, 0.2)
						EndIf
					EndIf
					MoveEntity(n\Collider, 0.0, 0.0, n\CurrSpeed * FPSfactor)
				Else
					AnimateNPC(n, 312.0, 422.0, 0.3, False)
				EndIf
				
				If (I_268\Using <> 3) Then
					If Dist < PowTwo(CameraFogFar * LightVolume) Then
						If (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 8) = "scramble") And (Angle < 135.0 Lor Angle > 225.0) And (EntityVisible(Camera, n\obj2) And EntityInView(n\obj2, Camera)) Then
							If (BlinkTimer < -16.0 Lor BlinkTimer > -6.0) And LightBlink <= 0.0
								PlaySound_Strict(LoadTempSound("SFX\SCP\096\Triggered.ogg"))
								
								CurrCameraZoom = 10.0
								
								If n\Frame >= 422.0 Then SetNPCFrame(n, 677.0)
								
								StopStream_Strict(n\SoundChn) : n\SoundChn=0
								n\Sound = 0
								
								n\State[0] = 2.0
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
			;[End Block]
		Case SCP096_STUNNED
			;[Block]
			AnimateNPC(n, 1333, 1368, 3, False)
			;[End Block]	
	End Select
	
	PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.03, EntityZ(n\Collider))
	
	RotateEntity(n\obj, EntityPitch(n\Collider), EntityYaw(n\Collider), 0.0)
	;[End Block]
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D