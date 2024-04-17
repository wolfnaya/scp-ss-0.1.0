
; ~ SCP-966 Constants

;[Block]
Const SCP966_IDLE = 0
;[End Block]

Function CreateNPCtype966(n.NPCs)
	Local i%, temp#
	Local n2.NPCs
	
	i = 1
	For n2.NPCs = Each NPCs
		If (n\NPCtype = n2\NPCtype) And (n<>n2) Then i=i+1
	Next
	n\NVName = "SCP-966-"+i
	
	n\Collider = CreatePivot()
	EntityRadius n\Collider,0.2
	
	For n2.NPCs = Each NPCs
		If (n\NPCtype = n2\NPCtype) And (n<>n2) Then
			n\obj = CopyEntity (n2\obj)
			Exit
		EndIf
	Next
	
	If n\obj = 0 Then 
		n\obj = CopyEntity(NPCModel[Model_966])
	EndIf
	
	EntityFX n\obj,1
	
	temp# = GetINIFloat("DATA\NPCs.ini", "SCP-966", "scale")/40.0
	ScaleEntity n\obj, temp, temp, temp
	
	SetAnimTime n\obj,15.0
	
	EntityType n\Collider,HIT_PLAYER
	
	n\Speed = 0.02
End Function

Function UpdateNPCtype966(n.NPCs)
	Local temp#, dist#, prevFrame#, angle#
	Local n2.NPCs
	
	dist = EntityDistance(n\Collider,Collider)
	
	If (dist<HideDistance) Then
		
		prevFrame = n\Frame
		
		If n\Sound > 0 Then
			temp = 0.5
			If n\State[0] > 0 Then temp = 1.0	
			
			n\SoundChn = LoopSound2(n\Sound, n\SoundChn, Camera, Camera, 10.0,temp)
		EndIf
		
		temp = Rnd(-1.0,1.0)
		PositionEntity n\obj,EntityX(n\Collider,True),EntityY(n\Collider,True)-0.2,EntityZ(n\Collider,True)
		RotateEntity n\obj,-90.0,EntityYaw(n\Collider),0.0
		
		If (Not mpl\NightVisionEnabled) And (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 3) = "nvg") Then
			HideEntity n\obj
			If (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then
				If dist<1 And n\Reload <= 0 And m_msg\Timer <= 0 Then
					Select Rand(6)
						Case 1
							CreateMsg(GetLocalString("Singleplayer","scp966_1"))
						Case 2
							CreateMsg(Chr(34)+GetLocalString("Singleplayer","scp966_2")+Chr(34))
						Case 3
							CreateMsg(GetLocalString("Singleplayer","scp966_3"))
						Case 4
							CreateMsg(Chr(34)+GetLocalString("Singleplayer","scp966_4")+Chr(34))
						Case 5
							CreateMsg(GetLocalString("Singleplayer","scp966_5"))
						Case 6
							CreateMsg(GetLocalString("Singleplayer","scp966_6"))
					End Select
					n\Reload = 20*70
				EndIf
			EndIf
			n\Reload = n\Reload - FPSfactor
			
		Else
			ShowEntity n\obj
		EndIf
		
		If n\State[2]>5*70 Then
			If n\State[2]<1000.0 Then
				For n2.NPCs = Each NPCs	
					If n2\NPCtype = n\NPCtype Then n2\State[2]=1000.0 
				Next
			EndIf
			
			n\State[0] = Max(n\State[0],8)
			n\State[2] = 1000.0					
			
		EndIf
		
		If Stamina<10 Then 
			n\State[2]=n\State[2]+FPSfactor
		Else If n\State[2] < 900.0
			n\State[2]=Max(n\State[2]-FPSfactor*0.2,0.0)
		EndIf
		
		If n\State[0] <> 10
			n\LastSeen = 0
		EndIf
		
		Select n\State[0]
			Case 0 ;idle, standing
				;[Block]
				If n\Frame>556.0
					AnimateNPC(n, 628, 652, 0.25, False)
					If n\Frame>651.0 Then SetNPCFrame(n, 2)
				Else
					AnimateNPC(n, 2, 214, 0.25, False)
					
					If n\Frame>213.0
						If Rand(3)=1 And dist<4 Then
							n\State[0] = Rand(1,4)
						Else
							n\State[0] = Rand(5,6)								
						EndIf
					EndIf
					If dist<2.0 Then 
						n\State[0]=Rand(1,4)
					EndIf 							
				EndIf
				
				n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 10.0)
				
				MoveEntity n\Collider,0,0,n\CurrSpeed
				;[End Block]
			Case 1,2 ;echo
				;[Block]
				AnimateNPC(n, 214, 257, 0.25, False)
				If n\Frame > 256.0 Then n\State[0] = 0
				
				If n\Frame>228.0 And prevFrame<=228.0
					PlayNPCSound(n, LoadTempSound("SFX\SCP\966\Echo"+Rand(1,3)+".ogg"))
				EndIf
				If (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then
					angle = VectorYaw(EntityX(Collider)-EntityX(n\Collider),0,EntityZ(Collider)-EntityZ(n\Collider))
					RotateEntity n\Collider,0.0,CurveAngle(angle,EntityYaw(n\Collider),20.0),0.0
					
					If n\State[2]<900 Then
						BlurTimer = ((Sin(MilliSecs()/50)+1.0)*200)/dist
						
						If (mpl\NightVisionEnabled>0) Then GiveAchievement(Achv966)
						
						If (Not I_714\Using) And dist<16 Then
							BlinkEffect = Max(BlinkEffect, 1.5)
							BlinkEffectTimer = 1000
							
							StaminaEffect = 2.0
							StaminaEffectTimer = 1000
							
							If m_msg\Timer<=0 Then
								Select Rand(4)
									Case 1
										CreateMsg(GetLocalString("Singleplayer","scp966_2_1"))
									Case 2
										CreateMsg(Chr(34)+GetLocalString("Singleplayer","scp966_2_2")+Chr(34))
									Case 3
										CreateMsg(Chr(34)+GetLocalString("Singleplayer","scp966_2_3")+Chr(34))
									Case 4
										CreateMsg(GetLocalString("Singleplayer","scp966_2_4"))
								End Select
							EndIf
						EndIf
					EndIf
				EndIf
				;[End Block]
			Case 3,4 ;stare at player
				;[Block]
				If n\State[0]=3
					AnimateNPC(n, 257, 332, 0.25, False)
					If n\Frame > 331.0 Then n\State[0] = 0
				Else
					AnimateNPC(n, 332, 457, 0.25, False)
					If n\Frame > 456.0 Then n\State[0] = 0
				EndIf
				
				If n\Frame>271.0 And prevFrame<=271.0 Lor n\Frame>414.0 And prevFrame<=414.0 Lor n\Frame>314.0 And prevFrame<=314.0 Lor n\Frame>301.0 And prevFrame<=301.0
					PlayNPCSound(n, LoadTempSound("SFX\SCP\966\Idle"+Rand(1,3)+".ogg"))
				EndIf
				If (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then
					angle = VectorYaw(EntityX(Collider)-EntityX(n\Collider),0,EntityZ(Collider)-EntityZ(n\Collider))
					RotateEntity n\Collider,0.0,CurveAngle(angle,EntityYaw(n\Collider),20.0),0.0
				EndIf
				;[End Block]
			Case 5,6,8 ;walking or chasing
				If n\Frame<580.0 ;start walking
					AnimateNPC(n, 556, 580, 0.25, False)
				Else
					AnimateNPC(n, 580, 628, n\CurrSpeed*25.0)
					
								;chasing the player
					If n\State[0] = 8 And dist<32 And (I_268\Using = 0 Lor I_268\Timer =< 0.0) Then
						If n\PathTimer <= 0 Then
							n\PathStatus = FindPath (n, EntityX(Collider,True), EntityY(Collider,True), EntityZ(Collider,True))
							n\PathTimer = 40*10
							n\CurrSpeed = 0
						EndIf
						n\PathTimer = Max(n\PathTimer-FPSFACTOR,0)
						
						If (Not EntityVisible(n\Collider,Collider)) Then
							If n\PathStatus = 2 Then
								n\CurrSpeed = 0
								SetNPCFrame(n,2)
							ElseIf n\PathStatus = 1
								If n\Path[n\PathLocation]=Null Then 
									If n\PathLocation > 19 Then 
										n\PathLocation = 0 : n\PathStatus = 0
									Else
										n\PathLocation = n\PathLocation + 1
									EndIf
								Else
									n\Angle = VectorYaw(EntityX(n\Path[n\PathLocation]\obj,True)-EntityX(n\Collider),0,EntityZ(n\Path[n\PathLocation]\obj,True)-EntityZ(n\Collider))
									If EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj) < PowTwo(0.8) Then 
										If n\Path[n\PathLocation]\door<>Null Then
											If (Not n\Path[n\PathLocation]\door\open) Then UseDoorNPC(n\Path[n\PathLocation]\door,n)
										EndIf
										If dist < 0.2 Then n\PathLocation = n\PathLocation + 1
									EndIf
									
								EndIf
							ElseIf n\PathStatus = 0
								n\CurrSpeed = CurveValue(0,n\CurrSpeed,10.0)
							EndIf
						Else
							n\Angle = VectorYaw(EntityX(Collider)-EntityX(n\Collider),0,EntityZ(Collider)-EntityZ(n\Collider))
							
							If dist<1.0 Then n\State[0]=10
							
						EndIf
						
						n\CurrSpeed = CurveValue(n\Speed,n\CurrSpeed,10.0)
					Else
						If MilliSecs() > n\State[1] And dist<16.0 Then
							HideEntity n\Collider
							EntityPick(n\Collider, 1.5)
							If PickedEntity() <> 0 Then
								n\Angle = EntityYaw(n\Collider)+Rnd(80,110)
							EndIf
							ShowEntity n\Collider
							
							n\State[1]=MilliSecs()+1000
							
							If Rand(5)=1 Then n\State[0]=0
						EndIf	
						
						n\CurrSpeed = CurveValue(n\Speed*0.5, n\CurrSpeed, 20.0)
						
					EndIf
					
					RotateEntity n\Collider, 0, CurveAngle(n\Angle,EntityYaw(n\Collider),30.0),0
					
					MoveEntity n\Collider,0,0,n\CurrSpeed
				EndIf
			Case 10 ;attack
				If n\LastSeen=0
					PlayNPCSound(n, LoadTempSound("SFX\SCP\966\Echo"+Rand(1,3)+".ogg"))
					n\LastSeen = 1
				EndIf
				
				If n\Frame>557.0
					AnimateNPC(n, 628, 652, 0.25, False)
					If n\Frame>651.0
						Select Rand(3)
							Case 1
								SetNPCFrame(n, 458)
							Case 2
								SetNPCFrame(n, 488)
							Case 3
								SetNPCFrame(n, 518)
						End Select
						
					EndIf
				Else
					If n\Frame <= 487
						AnimateNPC(n, 458, 487, 0.3, False)
						If n\Frame > 486.0 Then n\State[0] = 8
					ElseIf n\Frame <= 517
						AnimateNPC(n, 488, 517, 0.3, False)
						If n\Frame > 516.0 Then n\State[0] = 8
					ElseIf n\Frame <= 557
						AnimateNPC(n, 518, 557, 0.3, False)
						If n\Frame > 556.0 Then n\State[0] = 8
					EndIf
				EndIf
				
				If dist<1.0 Then
					If n\Frame>470.0 And prevFrame<=470.0 Lor n\Frame>500.0 And prevFrame<=500.0 Lor n\Frame>527.0 And prevFrame<=527.0
						PlayNPCSound(n, LoadTempSound("SFX\General\Slash"+Rand(1,2)+".ogg"))
						If I_1033RU\HP = 0
							DamageSPPlayer(Rnd(10.0,20.0))
						Else
							Damage1033RU(25 + 5 * SelectedDifficulty\AggressiveNPCs)
						EndIf	
					EndIf	
				EndIf
				
				n\Angle = VectorYaw(EntityX(Collider)-EntityX(n\Collider),0,EntityZ(Collider)-EntityZ(n\Collider))
				RotateEntity n\Collider, 0, CurveAngle(n\Angle,EntityYaw(n\Collider),30.0),0
				
		End Select
	Else
		HideEntity n\obj
		If (Rand(600)=1) Then
			TeleportCloser(n)
		EndIf
	EndIf
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D