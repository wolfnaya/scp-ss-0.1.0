
; ~ SCP-049 Constants

;[Block]
Const SCP049_NULL = 0
Const SCP049_LOOKING = 1
Const SCP049_ACTIVE = 2
Const SCP049_KILL = 3
Const SCP049_CATWALK = 4
Const SCP049_ROOM2SL = 5
Const SCP049_STUNNED = 6
;[End Block]

Function CreateNPCtype049(n.NPCs)
	Local temp#
	
	n\NVName = "SCP-049"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.2
	EntityType n\Collider, HIT_PLAYER
	
	n\obj = CopyEntity(NPCModel[Model_049])
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "SCP-049", "speed") / 100.0)
	
	temp = GetINIFloat("DATA\NPCs.ini", "SCP-049", "scale")
	ScaleEntity n\obj, temp, temp, temp	
	
	n\CanUseElevator = True
	
	n\HP = 100
	CopyHitBoxes(n)
	
End Function

Function UpdateNPCtype049(n.NPCs)
	Local prevFrame#, dist#, dist2#, i%, j%, PlayerSeeAble%, temp%
	Local e.Events, r.Rooms
	
	prevFrame# = n\Frame
	
	dist# = EntityDistanceSquared(Collider, n\Collider)
	
	n\BlinkTimer# = 1.0
	
	If n\Idle > 0.1
		If PlayerRoom\RoomTemplate\Name$ <> "cont_049"
			n\Idle = Max(n\Idle-(1+SelectedDifficulty\AggressiveNPCs)*FPSfactor,0.1)
		EndIf
		n\DropSpeed = 0
		If ChannelPlaying(n\SoundChn) Then StopChannel(n\SoundChn)
		If ChannelPlaying(n\SoundChn2) Then StopChannel(n\SoundChn2)
		PositionEntity n\Collider,0,-500,0
		PositionEntity n\obj,0,-500,0
	Else
		If n\Idle = 0.1 Then
			If PlayerInReachableRoom() Then
				For i = 0 To 3
					If PlayerRoom\Adjacent[i]<>Null Then
						For j = 0 To 3
							If PlayerRoom\Adjacent[i]\Adjacent[j]<>Null Then
								TeleportEntity(n\Collider,PlayerRoom\Adjacent[i]\Adjacent[j]\x,0.5,PlayerRoom\Adjacent[i]\Adjacent[j]\z,n\CollRadius,True)
								Exit
							EndIf
						Next
						Exit
					EndIf
				Next
				n\Idle = 0.0
				;debuglog "SCP-049 not idle"
			EndIf
		EndIf
		
		Select n\State[0]
			Case SCP049_NULL,STATE_SCRIPT ;nothing (used for events)
			Case SCP049_LOOKING ;looking around before getting active
				;[Block]
				If n\Frame=>538 Then
					AnimateNPC(n, 659, 538, -0.45, False)
					If n\Frame > 537.9 Then n\Frame = 37
				Else
					AnimateNPC(n, 37, 269, 0.7, False)
					If n\Frame>268.9 Then n\State[0] = 2
				EndIf
				;[End Block]
			Case SCP049_ACTIVE ;being active
				;[Block]
				If (dist < PowTwo(HideDistance*2)) And (Not n\Idle) And PlayerInReachableRoom(True) Then
					n\SoundChn = LoopSound2(HorrorSFX[12], n\SoundChn, Camera, n\Collider)
					PlayerSeeAble% = MeNPCSeesPlayer(n)
					If PlayerSeeAble%=True Lor n\State[1]>0 Then ;Player is visible for 049's sight - attacking
						GiveAchievement(Achv049)
						
									;Playing a sound after detecting the player
						If n\PrevState <= 1 And ChannelPlaying(n\SoundChn2)=False
							If n\Sound2 <> 0 Then FreeSound_Strict(n\Sound2)
							n\Sound2 = LoadSound_Strict("SFX\SCP\049\Spotted"+Rand(1,7)+".ogg")
							n\SoundChn2 = PlaySound2(n\Sound2,Camera,n\Collider)
							n\PrevState = 2
						EndIf
						n\PathStatus = 0
						n\PathTimer# = 0.0
						n\PathLocation = 0
						If PlayerSeeAble%=True Then n\State[1] = 70*2
						
						PointEntity n\obj,Collider
						RotateEntity n\Collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0),0
						
						If dist < PowTwo(0.5) Then
							If Inventory[SLOT_TORSO] <> Null And Inventory[SLOT_TORSO]\itemtemplate\tempname = "hazmat" Then
								BlurTimer = BlurTimer+FPSfactor*2.5
								If BlurTimer>250 And BlurTimer-FPSfactor*2.5 <= 250 And n\PrevState<>3 Then
									If n\SoundChn2 <> 0 Then StopChannel(n\SoundChn2)
									n\SoundChn2 = PlaySound_Strict(LoadTempSound("SFX\SCP\049\TakeOffHazmat.ogg"))
									n\PrevState=3
								ElseIf BlurTimer => 500
									For i = 0 To MaxItemAmount-1
										If Inventory[i]<>Null Then
											If Instr(Inventory[i]\itemtemplate\tempname,"hazmat") And Inventory[SLOT_TORSO] <> Null And Inventory[SLOT_TORSO]\itemtemplate\tempname = "hazmat3" Then
												If Inventory[i]\state2 < 3 Then
													Inventory[i]\state2 = Inventory[i]\state2 + 1
													BlurTimer = 260.0
													CameraShake = 2.0
												Else
													RemoveItem(Inventory[i])
												EndIf
												Exit
											EndIf
										EndIf
									Next
								EndIf
							ElseIf I_714\Using Then
								BlurTimer = BlurTimer+FPSfactor*2.5
								If BlurTimer>250 And BlurTimer-FPSfactor*2.5 <= 250 And n\PrevState<>3 Then
									If n\SoundChn2 <> 0 Then StopChannel(n\SoundChn2)
									n\SoundChn2 = PlaySound_Strict(LoadTempSound("SFX\SCP\049\714Equipped.ogg"))
									n\PrevState=3
								ElseIf BlurTimer => 500
									I_714\Using=False
								EndIf
							Else
								CurrCameraZoom = 20.0
								BlurTimer = 500.0
								
								If (Not GodMode) Then
									If PlayerRoom\RoomTemplate\Name$ = "cont_049"
										m_msg\DeathTxt = "Three (3) active instances of SCP-049-2 discovered in the tunnel outside SCP-049's containment chamber. Terminated by Nine-Tailed Fox."
										For e.Events = Each Events
											If e\EventName = "cont_049" Then e\EventState[0]=-1 : Exit
										Next
									Else
										m_msg\DeathTxt = "An active instance of SCP-049-2 was discovered in [REDACTED]. Terminated by Nine-Tailed Fox."
										Kill()
									EndIf
									PlaySound_Strict HorrorSFX[13]
									If n\Sound2 <> 0 Then FreeSound_Strict(n\Sound2)
									n\Sound2 = LoadSound_Strict("SFX\SCP\049\Kidnap"+Rand(1,2)+".ogg")
									n\SoundChn2 = PlaySound2(n\Sound2,Camera,n\Collider)
									n\State[0] = 3
									KillAnim = 0
								EndIf										
							EndIf
						Else
							n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor	
							
							If n\PrevState = 3 Then n\PrevState = 2
							
							If dist < PowTwo(3.0) Then
								AnimateNPC(n, Max(Min(AnimTime(n\obj),428.0),387), 463.0, n\CurrSpeed*38)
							Else
								If n\Frame>428.0 Then
									AnimateNPC(n, Min(AnimTime(n\obj),463.0), 498.0, n\CurrSpeed*38,False)
									If n\Frame>497.9 Then n\Frame = 358
								Else
									AnimateNPC(n, Max(Min(AnimTime(n\obj),358.0),346), 393.0, n\CurrSpeed*38)
								EndIf
							EndIf
						EndIf
					Else ;Finding a path to the player
						If n\PathStatus = 1 ;Path to player found
							While n\Path[n\PathLocation]=Null
								If n\PathLocation > 19
									n\PathLocation = 0 : n\PathStatus = 0 : Exit
								Else
									n\PathLocation = n\PathLocation + 1
								EndIf
							Wend
							If n\Path[n\PathLocation]<>Null Then
											;closes doors behind him
								If n\PathLocation>0 Then
									If n\Path[n\PathLocation-1] <> Null Then
										If n\Path[n\PathLocation-1]\door <> Null Then
											If (Not n\Path[n\PathLocation-1]\door\IsElevatorDoor) Then
												If EntityDistanceSquared(n\Path[n\PathLocation-1]\obj,n\Collider)>PowTwo(0.3) Then
													If (n\Path[n\PathLocation-1]\door\MTFClose) And (n\Path[n\PathLocation-1]\door\openstate=180) And (n\Path[n\PathLocation-1]\door\buttons[0]<>0 Lor n\Path[n\PathLocation-1]\door\buttons[1]<>0) Then
														UseDoorNPC(n\Path[n\PathLocation-1]\door, n)
													EndIf
												EndIf
											EndIf
										EndIf
									EndIf
								EndIf
								
								n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
								PointEntity n\obj,n\Path[n\PathLocation]\obj
								RotateEntity n\Collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0),0
								MoveEntity n\Collider,0,0,n\CurrSpeed*FPSfactor
								
											;opens doors in front of him
								dist2# = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
								If dist2 < PowTwo(0.6) Then
									temp = True
									If n\Path[n\PathLocation]\door <> Null Then
										If (Not n\Path[n\PathLocation]\door\IsElevatorDoor)
											If (n\Path[n\PathLocation]\door\locked Lor n\Path[n\PathLocation]\door\KeyCard<>0 Lor n\Path[n\PathLocation]\door\Code<>"") And (Not n\Path[n\PathLocation]\door\open) Then
												temp = False
											Else
												If n\Path[n\PathLocation]\door\open = False And (n\Path[n\PathLocation]\door\buttons[0]<>0 Lor n\Path[n\PathLocation]\door\buttons[1]<>0) Then
													UseDoorNPC(n\Path[n\PathLocation]\door, n)
												EndIf
											EndIf
										EndIf
									EndIf
									If dist2<PowTwo(0.2) And temp
										n\PathLocation = n\PathLocation + 1
									ElseIf dist2<PowTwo(0.5) And (Not temp)
													;Breaking up the path when the door in front of 049 cannot be operated by himself
										n\PathStatus = 0
										n\PathTimer# = 0.0
									EndIf
								EndIf
								
								AnimateNPC(n, Max(Min(AnimTime(n\obj),358.0),346), 393.0, n\CurrSpeed*38)
								
											;Playing a sound if he hears the player
								If n\PrevState = 0 And ChannelPlaying(n\SoundChn2)=False
									If n\Sound2 <> 0 Then FreeSound_Strict(n\Sound2)
									If Rand(30)=1
										n\Sound2 = LoadSound_Strict("SFX\SCP\049\Searching7.ogg")
									Else
										n\Sound2 = LoadSound_Strict("SFX\SCP\049\Searching"+Rand(1,6)+".ogg")
									EndIf
									n\SoundChn2 = PlaySound2(n\Sound2,Camera,n\Collider)
									n\PrevState = 1
								EndIf
								
											;Resetting the "PrevState" value randomly, to make 049 talking randomly 
								If Rand(600)=1 And ChannelPlaying(n\SoundChn2)=False Then n\PrevState = 0
								
								If n\PrevState > 1 Then n\PrevState = 1
							EndIf
						Else ;No Path to the player found - stands still and tries to find a path
							;[Block]
							n\PathTimer# = n\PathTimer# + FPSfactor
							If n\PathTimer# > 70*(5-(2*SelectedDifficulty\AggressiveNPCs)) Then
								n\PathStatus = FindPath(n, EntityX(Collider),EntityY(Collider),EntityZ(Collider))
								n\PathTimer# = 0.0
								n\State[2] = 0
								
											;Attempt to find a room (the Playerroom or one of it's adjacent rooms) for 049 to go to but select the one closest to him
								If n\PathStatus <> 1 Then
									Local closestdist# = EntityDistanceSquared(PlayerRoom\obj,n\Collider)
									Local closestRoom.Rooms = PlayerRoom
									Local currdist# = 0.0
									For i = 0 To 3
										If PlayerRoom\Adjacent[i]<>Null Then
											currdist = EntityDistanceSquared(PlayerRoom\Adjacent[i]\obj,n\Collider)
											If currdist < closestdist Then
												closestdist = currdist
												closestRoom = PlayerRoom\Adjacent[i]
											EndIf
										EndIf
									Next
									n\PathStatus = FindPath(n,EntityX(closestRoom\obj),0.5,EntityZ(closestRoom\obj))
									;debuglog "Find path for 049 in another room (pathstatus: "+n\PathStatus+")"
								EndIf
								
											;Making 3 attempts at finding a path
								While Int(n\State[2]) < 3
												;Breaking up the path if no "real" path has been found (only 1 waypoint and it is too close)
									If n\PathStatus = 1 Then
										If n\Path[1]<>Null Then
											If n\Path[2]=Null And EntityDistanceSquared(n\Path[1]\obj,n\Collider)<PowTwo(0.4) Then
												n\PathLocation = 0
												n\PathStatus = 0
												;debuglog "Breaking up path for 049 because no waypoint number 2 has been found and waypoint number 1 is too close."
											EndIf
										EndIf
										If n\Path[0]<>Null And n\Path[1]=Null Then
											n\PathLocation = 0
											n\PathStatus = 0
											;debuglog "Breaking up path for 049 because no waypoint number 1 has been found."
										EndIf
									EndIf
									
												;No path could still be found, just make 049 go to a room (further away than the very first attempt)
									If n\PathStatus <> 1 Then
										closestdist# = PowTwo(100.0) ;Prevent the PlayerRoom to be considered the closest, so 049 wouldn't try to find a path there
										closestRoom.Rooms = PlayerRoom
										currdist# = 0.0
										For i = 0 To 3
											If PlayerRoom\Adjacent[i]<>Null Then
												currdist = EntityDistanceSquared(PlayerRoom\Adjacent[i]\obj,n\Collider)
												If currdist < closestdist Then
													closestdist = currdist
													For j = 0 To 3
														If PlayerRoom\Adjacent[i]\Adjacent[j]<>Null Then
															If PlayerRoom\Adjacent[i]\Adjacent[j]<>PlayerRoom Then
																closestRoom = PlayerRoom\Adjacent[i]\Adjacent[j]
																Exit
															EndIf
														EndIf
													Next
												EndIf
											EndIf
										Next
										n\PathStatus = FindPath(n,EntityX(closestRoom\obj),0.5,EntityZ(closestRoom\obj))
										;debuglog "Find path for 049 in another further away room (pathstatus: "+n\PathStatus+")"
									EndIf
									
												;Making 049 skip waypoints for doors he can't interact with, but only if the actual path is behind him
									If n\PathStatus = 1 Then
										If n\Path[1]<>Null Then
											If n\Path[1]\door<>Null Then
												If (n\Path[1]\door\locked Lor n\Path[1]\door\KeyCard<>0 Lor n\Path[1]\door\Code<>"") And (Not n\Path[1]\door\open) Then
													Repeat
														If n\PathLocation > 19
															n\PathLocation = 0 : n\PathStatus = 0 : Exit
														Else
															n\PathLocation = n\PathLocation + 1
														EndIf
														If n\Path[n\PathLocation]<>Null Then
															If Abs(DeltaYaw(n\Collider,n\Path[n\PathLocation]\obj))>(45.0-Abs(DeltaYaw(n\Collider,n\Path[1]\obj))) Then
																;debuglog "Skip until waypoint number "+n\PathLocation
																n\State[2] = 3
																Exit
															EndIf
														EndIf
													Forever
												Else
													n\State[2] = 3
												EndIf
											Else
												n\State[2] = 3
											EndIf
										EndIf
									EndIf
									n\State[2] = n\State[2] + 1
								Wend
							EndIf
							AnimateNPC(n, 269, 345, 0.2)
							;[End Block]
						EndIf
					EndIf
					
					If n\CurrSpeed > 0.005 Then
						If (prevFrame < 361 And n\Frame=>361) Lor (prevFrame < 377 And n\Frame=>377) Then
							PlaySound2(StepSFX(3,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.8,1.0))						
						ElseIf (prevFrame < 431 And n\Frame=>431) Lor (prevFrame < 447 And n\Frame=>447) Then
							PlaySound2(StepSFX(3,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.8,1.0))
						EndIf
					EndIf
					
					If ChannelPlaying(n\SoundChn2)
						UpdateSoundOrigin(n\SoundChn2,Camera,n\obj)
					EndIf
				ElseIf (Not n\Idle)
					If ChannelPlaying(n\SoundChn) Then
						StopChannel(n\SoundChn)
					EndIf
					If PlayerInReachableRoom(True) And InFacility=1 Then ;Player is in a room where SCP-049 can teleport to
						If Rand(1,3-SelectedDifficulty\OtherFactors)=1 Then
							TeleportCloser(n)
							;debuglog "SCP-049 teleported closer due to distance"
						Else
							n\Idle = 60*70
							;debuglog "SCP-049 is now idle"
						EndIf
					EndIf
				EndIf
				;[End Block]
			Case SCP049_KILL ;The player was killed by SCP-049
				;[Block]
				AnimateNPC(n, 537, 660, 0.7, False)
				
							;Animate2(n\obj, AnimTime(n\obj), 537, 660, 0.7, False)
				PositionEntity n\Collider, CurveValue(EntityX(Collider),EntityX(n\Collider),20.0),EntityY(n\Collider),CurveValue(EntityZ(Collider),EntityZ(n\Collider),20.0)
				RotateEntity n\Collider, 0, CurveAngle(EntityYaw(Collider)-180.0,EntityYaw(n\Collider),40), 0
				;[End Block]
			Case SCP049_CATWALK ;Standing on catwalk in room4
				;[Block]
				If dist < PowTwo(8.0) Then
					AnimateNPC(n, 18, 19, 0.05)
					
								;Animate2(n\obj, AnimTime(n\obj), 18, 19, 0.05)
					PointEntity n\obj, Collider	
					RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 45.0), 0
					
					n\State[2] = 1
				ElseIf dist > PowTwo(HideDistance*0.8) And n\State[2] > 0 Then
					n\State[0] = 2
					n\State[2] = 0
					For r.Rooms = Each Rooms
						If EntityDistanceSquared(r\obj,n\Collider)<PowTwo(4.0) Then
							TeleportEntity(n\Collider,EntityX(r\obj),0.1,EntityZ(r\obj),n\CollRadius,True)
							Exit
						EndIf
					Next
				EndIf
				;[End Block]
			Case SCP049_ROOM2SL ;used for "room2sl"
				;[Block]
				n\SoundChn = PlayNPCSound(n, n\Sound, True)
				PlayerSeeAble% = MeNPCSeesPlayer(n,True)
				If PlayerSeeAble% = True
					n\State[0] = 2
					n\PathStatus = 0
					n\PathLocation = 0
					n\PathTimer = 0
					n\State[2] = 0
					n\State[1] = 70*2
					n\PrevState = 0
					PlaySound_Strict LoadTempSound("SFX\Room\Room2SL049Spawn.ogg")
				ElseIf PlayerSeeAble% = 2 And n\State[2] > 0.0
					n\PathStatus = FindPath(n,EntityX(Collider),EntityY(Collider),EntityZ(Collider))
				Else
					If n\State[2] = 6.0
						If EntityDistanceSquared(n\Collider,Collider)>PowTwo(HideDistance)
							n\State[0] = 2
							n\PathStatus = 0
							n\PathLocation = 0
							n\PathTimer = 0
							n\State[2] = 0
							n\PrevState = 0
						Else
							If n\PathStatus <> 1 Then n\PathStatus = FindPath(n,EntityX(Collider),EntityY(Collider),EntityZ(Collider))
						EndIf
					EndIf
					
					If n\PathStatus = 1
						If n\Path[n\PathLocation]=Null
							If n\PathLocation > 19 Then
								n\PathLocation = 0 : n\PathStatus = 0
							Else
								n\PathLocation = n\PathLocation + 1
							EndIf
						Else
							n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 20.0)
							PointEntity n\obj,n\Path[n\PathLocation]\obj
							RotateEntity n\Collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0),0
							MoveEntity n\Collider,0,0,n\CurrSpeed*FPSfactor
							
										;closes doors behind him
							If n\PathLocation>0 Then
								If n\Path[n\PathLocation-1] <> Null
									If n\Path[n\PathLocation-1]\door <> Null Then
										If n\Path[n\PathLocation-1]\door\KeyCard=0
											If EntityDistanceSquared(n\Path[n\PathLocation-1]\obj,n\Collider)>PowTwo(0.3)
												If n\Path[n\PathLocation-1]\door\open Then UseDoorNPC(n\Path[n\PathLocation-1]\door, n)
											EndIf
										EndIf
									EndIf
								EndIf
							EndIf
							
										;opens doors in front of him
							dist2# = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
							If dist2 < PowTwo(0.6) Then
								If n\Path[n\PathLocation]\door <> Null Then
									If n\Path[n\PathLocation]\door\open = False Then UseDoorNPC(n\Path[n\PathLocation]\door, n)
								EndIf
								If dist2 < PowTwo(0.2)
									n\PathLocation = n\PathLocation + 1
								EndIf
							EndIf
							
							AnimateNPC(n, Max(Min(AnimTime(n\obj),358.0),346), 393.0, n\CurrSpeed*38)
						EndIf
					Else
						Select n\PrevState
							Case 0
								AnimateNPC(n, 269, 345, 0.2)
							Case 1
								AnimateNPC(n, 661, 891, 0.4, False)
							Case 2
								AnimateNPC(n, 892, 1119, 0.4, False)
						End Select
					EndIf
				EndIf
				
				If n\CurrSpeed > 0.005 Then
					If (prevFrame < 361 And n\Frame=>361) Lor (prevFrame < 377 And n\Frame=>377) Then
						PlaySound2(StepSFX(3,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.8,1.0))						
					ElseIf (prevFrame < 431 And n\Frame=>431) Lor (prevFrame < 447 And n\Frame=>447)
						PlaySound2(StepSFX(3,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.8,1.0))
					EndIf
				EndIf
				
				If ChannelPlaying(n\SoundChn2)
					UpdateSoundOrigin(n\SoundChn2,Camera,n\obj)
				EndIf
				;[End Block]
			Case SCP049_STUNNED ;Stunned
				;[Block]
				;If n\HP < 60 Then
					AnimateNPC(n, 610, 537, -1.4, False)
					If n\Frame = 537 Then
						n\State[0] = SCP049_LOOKING
					EndIf
				;Else
				;	AnimateNPC(n, 537, 610, 1.4, False)
				;EndIf
				;[End Block]
		End Select
	EndIf
	
	PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider)-0.22, EntityZ(n\Collider))
	
	RotateEntity n\obj, 0, EntityYaw(n\Collider), 0
	
	n\LastSeen = Max(n\LastSeen-FPSfactor,0)
	n\State[1] = Max(n\State[1]-FPSfactor,0)
	n\HP = Min(n\HP+FPSfactor*0.5,100)
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D