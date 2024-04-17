
Function RenderInterfaceIcons()
	Local temp%, x%, y%, z%, i%, yawvalue#, pitchvalue#
	Local DoorStr$
	
	If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
	
		If psp\IsShowingHUD And HUDenabled Then
			If ClosestItem <> Null Lor DrawHandIcon Lor d_I\ClosestButton <> 0 And d_I\SelectedDoor = Null And InvOpen = False And MenuOpen = False And OtherOpen = Null And (Not PlayerInNewElevator) Then
				If InterfaceTimer# < 70*2 Then
					InterfaceTimer# = InterfaceTimer# + FPSfactor*3
				Else
					InterfaceTimer# = 70*2
				EndIf
			Else
				InterfaceTimer# = 0
			EndIf
			
			If d_I\ClosestButton <> 0 And d_I\SelectedDoor = Null And InvOpen = False And MenuOpen = False And OtherOpen = Null And (Not PlayerInNewElevator)
				
				temp% = CreatePivot()
				PositionEntity temp, EntityX(Camera), EntityY(Camera), EntityZ(Camera)
				PointEntity temp, d_I\ClosestButton
				yawvalue# = WrapAngle(EntityYaw(Camera) - EntityYaw(temp))
				If yawvalue > 90 And yawvalue <= 180 Then yawvalue = 90
				If yawvalue > 180 And yawvalue < 270 Then yawvalue = 270
				pitchvalue# = WrapAngle(EntityPitch(Camera) - EntityPitch(temp))
				If pitchvalue > 90 And pitchvalue <= 180 Then pitchvalue = 90
				If pitchvalue > 180 And pitchvalue < 270 Then pitchvalue = 270
				
				temp = FreeEntity_Strict(temp)
				
				Color 10,10,10
				Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,64,64)
				
				;If InterfaceTimer# > 70*1 Then
				Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,Min(InterfaceTimer#*2,230),64)
				;EndIf
				
				Color 50,50,50
				Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,64,64,False)
				
				;If InterfaceTimer# > 70*1 Then
				Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,Min(InterfaceTimer#*2,230),64,False)
				;EndIf
				
				Color 255,255,255
				SetFont fo\Font[Font_Default_Large]
				Text opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 16, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 14,"E"
				
			EndIf
			
			If ClosestItem <> Null Then
				yawvalue# = -DeltaYaw(Camera, ClosestItem\collider)
				If yawvalue > 90 And yawvalue <= 180 Then yawvalue = 90
				If yawvalue > 180 And yawvalue < 270 Then yawvalue = 270
				pitchvalue# = -DeltaPitch(Camera, ClosestItem\collider)
				If pitchvalue > 90 And pitchvalue <= 180 Then pitchvalue = 90
				If pitchvalue > 180 And pitchvalue < 270 Then pitchvalue = 270
				
				Color 10,10,10
				
				Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,64,64)
				
				Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,Min(InterfaceTimer#*3,300),64)
				
				Color 50,50,50
				
				Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,64,64,False)
				
				Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,Min(InterfaceTimer#*3,300),64,False)
				
				Color 255,255,255
				SetFont fo\Font[Font_Default_Large]
				Text opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 16, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 14,"E"
			EndIf
			
			If InterfaceTimer# >= 70*1.5 Then
				If ClosestItem <> Null Then
					SetFont fo\Font[Font_Default]
					Text opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 48, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 25,GetLocalString("Interface","pick_up")
					SetFont fo\Font[Font_Default_Medium]
					Text opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 48, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 5,ClosestItem\itemtemplate\name
				ElseIf d_I\ClosestButton <> 0 And d_I\SelectedDoor = Null And InvOpen = False And MenuOpen = False And OtherOpen = Null And (Not PlayerInNewElevator) Then
					
					;If d_I\SelectedDoor\open Then
					;	DoorStr = "door_close"
					;Else
					;	DoorStr = "door_open"
					;EndIf
					
					DoorStr = "door_operate"
					
					SetFont fo\Font[Font_Default_Medium]
					Text opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 48, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 10,GetLocalString("Interface",DoorStr)
				ElseIf d_I\ClosestButton <> 0 And d_I\SelectedDoor = Null And InvOpen = False And MenuOpen = False And OtherOpen = Null And  PlayerInNewElevator Then
					SetFont fo\Font[Font_Default_Medium]
					Text opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 48, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 10,GetLocalString("Interface","call_elevator")
				ElseIf DrawHandIcon Then
					SetFont fo\Font[Font_Default_Medium]
					Text opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 48, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 10,GetLocalString("Interface","interact")
				EndIf
			EndIf
			
			If DrawHandIcon Then
				
				Color 10,10,10
				Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,64,64)
				
				Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,Min(InterfaceTimer#*2,200),64)
				Color 50,50,50
				Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,64,64,False)
				
				Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,Min(InterfaceTimer#*2,200),64,False)
				Color 255,255,255
				SetFont fo\Font[Font_Default_Large]
				Text opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 16, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 14,"E"
				
				;If IsDialogue Then
				;	Text opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 48, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 10,GetLocalString("Interface","talk")
				;EndIf
				
			EndIf
			
			For i = 0 To 3
				If DrawArrowIcon[i] Then
					x = opt\GraphicWidth / 2 - 32
					y = opt\GraphicHeight / 2 - 32		
					Select i
						Case 0
							y = y - 64 - 5
						Case 1
							x = x + 64 + 5
						Case 2
							y = y + 64 + 5
						Case 3
							x = x - 5 - 64
					End Select
					
					Color 10,10,10
					Rect(x,y,64,64)
					Color 50,50,50
					Rect(x,y,64,64,False)
					Color 255,255,255
					SetFont fo\Font[Font_Default_Large]
					Text x/2, y/2,"E"
					
					Color 0, 0, 0
					Rect(x + 4, y + 4, 64 - 8, 64 - 8)
					DrawImage(I_MIG\ArrowIMG[i], x + 21, y + 21)
				EndIf
			Next
			
		Else
			Return
		EndIf
		
	Else
		
		If Players[mp_I\PlayerID]\CurrHP > 0 And ClosestItem <> Null Lor DrawHandIcon And MenuOpen = False Then
			If InterfaceTimer# < 70*2 Then
				InterfaceTimer# = InterfaceTimer# + FPSfactor*3
			Else
				InterfaceTimer# = 70*2
			EndIf
		Else
			InterfaceTimer# = 0
		EndIf
		
		If ClosestItem <> Null Then
			yawvalue# = -DeltaYaw(Camera, ClosestItem\collider)
			If yawvalue > 90 And yawvalue <= 180 Then yawvalue = 90
			If yawvalue > 180 And yawvalue < 270 Then yawvalue = 270
			pitchvalue# = -DeltaPitch(Camera, ClosestItem\collider)
			If pitchvalue > 90 And pitchvalue <= 180 Then pitchvalue = 90
			If pitchvalue > 180 And pitchvalue < 270 Then pitchvalue = 270
			
			Color 10,10,10
			
			Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,64,64)
			
			Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,Min(InterfaceTimer#*3,300),64)
			
			Color 50,50,50
			
			Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,64,64,False)
			
			Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,Min(InterfaceTimer#*3,300),64,False)
			
			Color 255,255,255
			SetFont fo\Font[Font_Default_Large]
			Text opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 16, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 14,"E"
		EndIf
		
		If DrawHandIcon Then
			
			Color 10,10,10
			Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,64,64)
			
			Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,Min(InterfaceTimer#*2,200),64)
			Color 50,50,50
			Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,64,64,False)
			
			Rect(opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 32, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 32,Min(InterfaceTimer#*2,200),64,False)
			Color 255,255,255
			SetFont fo\Font[Font_Default_Large]
			Text opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) - 16, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 14,"E"
			
		EndIf
		
		If InterfaceTimer# >= 70*1.5 Then
			If ClosestItem <> Null Then
				SetFont fo\Font[Font_Default]
				
				Local ItemStr$
				
				If ClosestItem\itemtemplate\IsUsable Then
					ItemStr$ = "use"
				ElseIf ClosestItem\itemtemplate\IsEquippable Then
					ItemStr$ = "equip"
				Else
					ItemStr$ = "pick_up"
				EndIf
				
				Text opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 48, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 25,GetLocalString("Interface",ItemStr$)
				SetFont fo\Font[Font_Default_Medium]
				Text opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 48, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 5,ClosestItem\itemtemplate\name
			ElseIf DrawHandIcon Then
				SetFont fo\Font[Font_Default_Medium]
				Text opt\GraphicWidth / 2 + Sin(yawvalue) * (opt\GraphicWidth / 3) + 48, opt\GraphicHeight / 2 - Sin(pitchvalue) * (opt\GraphicHeight / 3) - 10,GetLocalString("Interface","interact")
			EndIf
		EndIf
		
		For i = 0 To 3
			If DrawArrowIcon[i] Then
				x = opt\GraphicWidth / 2 - 32
				y = opt\GraphicHeight / 2 - 32		
				Select i
					Case 0
						y = y - 64 - 5
					Case 1
						x = x + 64 + 5
					Case 2
						y = y + 64 + 5
					Case 3
						x = x - 5 - 64
				End Select
				
				Color 10,10,10
				Rect(x,y,64,64)
				Color 50,50,50
				Rect(x,y,64,64,False)
				Color 255,255,255
				SetFont fo\Font[Font_Default_Large]
				Text x/2, y/2,"E"
				
				Color 0, 0, 0
				Rect(x + 4, y + 4, 64 - 8, 64 - 8)
				DrawImage(I_MIG\ArrowIMG[i], x + 21, y + 21)
			EndIf
		Next
		
	EndIf
	
End Function

Global InterfaceTimer#

Function UpdateInterfaceIcons()
	Local temp%
	Local np.NPCs, ne.NewElevator
	
	If d_I\ClosestButton <> 0 And d_I\SelectedDoor = Null And InvOpen = False And MenuOpen = False And OtherOpen = Null And (Not PlayerInNewElevator) Then
		
		If KeyHitUse Then
			If d_I\ClosestDoor <> Null Then 
				If d_I\ClosestDoor\Code <> "" Lor (d_I\ClosestDoor\dir=DOOR_ELEVATOR_3FLOOR And d_I\ClosestButton=d_I\ClosestDoor\buttons[0]) Lor (d_I\ClosestDoor\dir=DOOR_STORAGE_ELEVATOR And d_I\ClosestButton=d_I\ClosestDoor\buttons[0]) Then
					temp = True
					For ne = Each NewElevator
						If ne\door = d_I\ClosestDoor Then
							For np = Each NPCs
								If np\NPCtype = NPC_SCP_173 And np\Idle = SCP173_BOXED Then
									If Abs(EntityX(np\Collider) - EntityX(ne\obj, True)) >= 280.0 * RoomScale + (0.015 * FPSfactor) Lor Abs(EntityZ(np\Collider) - EntityZ(ne\obj, True)) >= 280.0 * RoomScale + (0.015 * FPSfactor) Then
										temp = 2
									EndIf
								EndIf
								If np\NPCtype = NPC_NTF And np\HP > 0 Then
									If Abs(EntityX(np\Collider) - EntityX(ne\obj, True)) >= 280.0 * RoomScale + (0.015 * FPSfactor) Lor Abs(EntityZ(np\Collider) - EntityZ(ne\obj, True)) >= 280.0 * RoomScale + (0.015 * FPSfactor) Then
										temp = False
										Exit
									EndIf
								EndIf
							Next
							Exit
						EndIf
					Next
					
					If temp = 1 Then
						d_I\SelectedDoor = d_I\ClosestDoor
						co\KeyPad_CurrButton = 0
						co\WaitTimer = 0
					ElseIf temp = 2 Then
						CreateMsg(GetLocalString("Doors", "elevator_wait_173"))
					Else
						CreateMsg(GetLocalString("Doors", "elevator_wait"))
					EndIf
				ElseIf (d_I\ClosestDoor\dir=DOOR_ELEVATOR_3FLOOR And d_I\ClosestButton=d_I\ClosestDoor\buttons[1]) Lor(d_I\ClosestDoor\dir=DOOR_STORAGE_ELEVATOR And d_I\ClosestButton=d_I\ClosestDoor\buttons[1]) Then
					PlaySound2(ButtonSFX[0], Camera, d_I\ClosestButton)
					For ne = Each NewElevator
						If ne\door = d_I\ClosestDoor
							If ne\state = 0.0
								If EntityY(ne\door\frameobj)>ne\floory[1]*RoomScale+1
									StartNewElevator(d_I\ClosestDoor,3)
									DebugLog "Option 3"
								ElseIf EntityY(ne\door\frameobj)<ne\floory[2]*RoomScale-1 And EntityY(ne\door\frameobj)>ne\floory[0]*RoomScale
									StartNewElevator(d_I\ClosestDoor,2)
									DebugLog "Option 2"
								Else
									StartNewElevator(d_I\ClosestDoor,1)
									DebugLog "Option 1"
								EndIf
							Else
								If (m_msg\Txt<>GetLocalString("Doors", "elevator_called"))
									If (m_msg\Txt=GetLocalString("Doors", "elevator_called2")) Lor (m_msg\Timer<70*3)	
										Select Rand(10)
											Case 1
												CreateMsg(GetLocalString("Doors", "elevator_rand_1"))
											Case 2
												CreateMsg(GetLocalString("Doors", "elevator_rand_2"))
											Case 3
												CreateMsg(GetLocalString("Doors", "elevator_rand_3"))
											Default
												CreateMsg(GetLocalString("Doors", "elevator_called2"))
										End Select
									EndIf
								Else
									CreateMsg(GetLocalString("Doors", "elevator_called2"))
								EndIf
							EndIf
						EndIf
					Next
				ElseIf Playable Then
					PlaySound2(ButtonSFX[0], Camera, d_I\ClosestButton)
					UseDoor(d_I\ClosestDoor,True)
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

Function DrawGUI()
	CatchErrors("Uncaught (DrawGUI)")
	
	Local temp%, x%, y%, z%, i%, yawvalue#, pitchvalue#
	Local x2#,y2#,z2#
	Local n%, xtemp, ytemp, strtemp$, projY#, scale#, g.Guns
	
	Local e.Events, it.Items
	
	If MenuOpen Lor ConsoleOpen Lor d_I\SelectedDoor <> Null Lor InvOpen Lor OtherOpen <> Null Lor EndingTimer < 0 Then
		ShowPointer()
	Else
		HidePointer()
	EndIf
	
	If PlayerRoom\RoomTemplate\Name = "pocketdimension" Then
		For e.Events = Each Events
			If e\room = PlayerRoom And e\EventState[0] > 600 Then
				If BlinkTimer < -3 And BlinkTimer > -11 Then
					If e\img = 0 Then
						If BlinkTimer > -5 And Rand(30)=1 Then
							If Rand(5)<5 Then PlaySound_Strict DripSFX[0]
							If e\img = 0 Then e\img = LoadImage_Strict("GFX\npcs\106face.jpg")
						EndIf
					Else
						DrawImage e\img, opt\GraphicWidth/2-Rand(390,310), opt\GraphicHeight/2-Rand(290,310)
					EndIf
				Else
					If e\img <> 0 Then FreeImage e\img : e\img = 0
				EndIf
				
				Exit
			EndIf
		Next
	EndIf
	
	RenderInterfaceIcons()
	
	If Using294 Then Use294()
	
	If (Not MenuOpen) And (Not InvOpen) And (OtherOpen=Null) And (ConsoleOpen=False) And (Using294=False) And (SelectedScreen=Null) And EndingTimer=>0 And KillTimer >= 0
		If PlayerRoom\RoomTemplate\Name$ <> "gate_a_intro"
			
		EndIf
	EndIf
	
	DrawSplashTexts()
	DrawSplashMsg()
	DrawScopeDots()
	
	If psp\IsShowingHUD And (Not MenuOpen) Then
		If HUDenabled Then
			
			Local width% = 204, height% = 20
			x = (opt\GraphicWidth / 2) - (width / 2) + 20
			y% = opt\GraphicHeight - 95
			; ~ Blinking Bar
			If (EntityVisible(Curr173\Collider,Camera)) And EntityDistanceSquared(Curr173\Collider, Collider) < PowTwo(15.0) Then
				Color 255, 255, 255	
				Rect (x, y, width, height, False)
				If BlinkTimer < 150 Then
					Color 110, 0, 0
				Else
					Color 110, 110, 110
				EndIf		
				Rect(x + 3, y + 3, Float(BlinkTimer * ((width - 6) / BLINKFREQ)), 14)
				
				Color 0, 0, 0
				Rect(x - 50, y, 30, 30)
				
				If EyeIrritation > 0 And (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 3) = "nvg") And (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 8) = "scramble")
					Color 200, 0, 0
					Rect(x - 50 - 3, y - 3, 30 + 6, 30 + 6)
				EndIf
				
				If BlinkEffect < 1.0 Then
					Color 0, 255, 0
				Else
					Color 255, 255, 255
				EndIf
				Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
				
				DrawImage BlinkIcon[0], x - 50, y
				
			EndIf
			SetFont fo\Font%[Font_Digital_Medium]
			x% = 80
			y = opt\GraphicHeight - 55
			; ~ Health
			Color 0,0,0
			Rect(x - 10, y - 50, 40, 40)
			
			If psp\Health > 20 Then
				Color 255,255,255
			Else
				Color 255,0,0
			EndIf
			Rect(x - 11, y - 51, 40 + 2, 40 + 2, False)
			DrawImage mpl\HealthIcon, x - 10, y - 50
			
			If psp\Health > 20
				Color 0,255,0
			Else
				Color 255,0,0
			EndIf
			TextWithAlign x + 80, y - 40, Int(psp\Health), 2
			; ~ Helmet
			If Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet" Lor mpl\HasNTFGasmask Then
				Color 0,0,0
				Rect(x+40, y, 30, 30)
				
				If psp\Helmet > 20 Then
					Color 255,255,255
				Else
					Color 255,0,0
				EndIf
				Rect(x+40 - 1, y - 1, 30 + 2, 30 + 2, False)
				DrawImage mpl\HelmetIcon, x+40, y
				
				If psp\Helmet > 20
					Color 0,255,0
				Else
					Color 255,0,0
				EndIf
				TextWithAlign x + 120, y + 5, Int(psp\Helmet), 2
			EndIf
			; ~ Armor
			If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 4) = "vest" Then
				Color 0,0,0
				Rect(x - 50, y, 30, 30)
				
				If psp\Kevlar > 20 Then
					Color 255,255,255
				Else
					Color 255,0,0
				EndIf
				Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
				DrawImage mpl\KevlarIcon, x - 50, y
				
				If psp\Kevlar > 20
					Color 0,255,0
				Else
					Color 255,0,0
				EndIf
				TextWithAlign x + 30, y + 5, Int(psp\Kevlar), 2
			EndIf
			; ~ HDS Suit
			If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" Then
				Color 0,0,0
				Rect(x - 10, y, 30, 30)
				
				If hds\Health > 20 Then
					Color 255,255,255
				Else
					Color 255,0,0
				EndIf
				Rect(x - 10 - 1, y - 1, 30 + 2, 30 + 2, False)
				DrawImage HDSIcon, x - 10, y
				
				If hds\Health > 20
					Color 0,255,0
				Else
					Color 255,0,0
				EndIf
				If hds\Health < 0 Then
					TextWithAlign x + 80, y + 5, GetLocalString("Hazardous Suit","charge_crit"), 2
				ElseIf hds\Health > 100 Then
					TextWithAlign x + 80, y + 5, GetLocalString("Hazardous Suit","charge_over"), 2
				Else
					TextWithAlign x + 80, y + 5, Int(hds\Health), 2
				EndIf
			EndIf
			; ~ SCP-1033-RU
			If I_1033RU\Using > 0 Then
				If I_268\Using > 0 Then
					y% = opt\GraphicHeight - 175
				Else
					y% = opt\GraphicHeight - 135
				EndIf
				
				x% = 80
				
				Color 255, 255, 255
				Rect (x, y, width, height, False)
				
				If I_1033RU\Using = 1 Then
					If I_1033RU\HP < 35 Then
						Color 110, 0, 0
					Else
						Color 110, 110, 110
					EndIf		
					Rect(x + 3, y + 3, Float(I_1033RU\HP * (width - 6) / 100), 14)	
				Else
					If I_1033RU\HP < 75 Then
						Color 110, 0, 0
					Else
						Color 110, 110, 110
					EndIf		
					Rect(x + 3, y + 3, Float(I_1033RU\HP * (width - 6) / 200), 14)	
				EndIf
				
				Color 0, 0, 0
				Rect(x - 50, y, 30, 30)
				
				If I_1033RU\Using = 2 Then
					Color 0, 200, 0
					Rect(x - 50 - 3, y - 3, 30 + 6, 30 + 6)
					Color 0, 0, 0
					Rect(x - 50, y, 30, 30)
				EndIf
				
				Color 255, 255, 255
				Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
				
				DrawImage SCP1033RUIcon, x - 50, y
			EndIf
			; ~ SCP-268
			If I_268\Using > 0 Then
				y% = opt\GraphicHeight - 135
				x% = 80
				
				Color 255, 255, 255
				Rect (x, y, width, height, False)
				
				If I_268\Timer < 200 Then
					Color 110, 0, 0
				Else
					Color 110, 110, 110
				EndIf	
				Rect(x + 3, y + 3, Float(I_268\Timer * (width - 6) / 700), 14)	
				
				Color 0, 0, 0
				Rect(x - 50, y, 30, 30)
				
				If I_268\Using = 2 Then
					Color 0, 200, 0
					Rect(x - 50 - 3, y - 3, 30 + 6, 30 + 6)
					Color 0, 0, 0
					Rect(x - 50, y, 30, 30)
				EndIf 
				
				Color 255, 255, 255
				Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
				
				DrawImage SCP268Icon, x - 50, y
			EndIf
			; ~ SCP-207
			If I_207\DeathTimer > 0 Then
				y = opt\GraphicHeight - 135
				x = (opt\GraphicWidth / 2) - (width / 2) + 20
				Color 255,255,255
				Rect (x, y, width, height, False)
				
				Color 55, 0, 55
				Rect(x + 3, y + 3, Min(Float(I_207\DeathTimer * (width - 6) / (70*420)),width - 6), 14)
				
				Color 0, 0, 0
				Rect(x - 50, y, 30, 30)
				
				Color 255, 255, 255
				Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
				DrawImage SCP207Icon, x - 50, y
			EndIf
			; ~ SCP-198
			If I_198\Timer > 0 Then
				y = opt\GraphicHeight - 175
				x = (opt\GraphicWidth / 2) - (width / 2) + 20
				Color 255,255,255
				Rect (x, y, width, height, False)
				
				Color 255, 255, 255
				Rect(x + 3, y + 3, Min(Float(I_198\DeathTimer * (width - 6) / (70*120)),width - 6), 14)
				
				Color 0, 0, 0
				Rect(x - 50, y, 30, 30)
				
				Color 255, 255, 255
				Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
				DrawImage SCP198Icon, x - 50, y
			EndIf
			; ~ Stamina Bar
			If Stamina < 100.0 And PlayerRoom\RoomTemplate\Name <> "pocketdimension" Then
				y = opt\GraphicHeight - 55
				x = (opt\GraphicWidth / 2) - (width / 2) + 20
				If Stamina < 30.0 Then
					Color 55, 0, 0
				Else
					Color 255, 255, 255
				EndIf
				Rect (x, y, width, height, False)
				If Stamina < 30 Then
					Color 55, 0, 0
				Else
					Color 55, 55, 55
				EndIf		
				Rect(x + 3, y + 3, Float(Stamina * (width - 6) / 100), 14)
				
				Color 0, 0, 0
				Rect(x - 50, y, 30, 30)
				
				If StaminaEffect < 1.0 Then
					Color 0, 255, 0
				Else
					If Stamina <= 0.0 Then
						Color 255, 0, 0
					Else
						Color 255, 255, 255
					EndIf
				EndIf
				Rect(x - 50 - 1, y - 1, 30 + 2, 30 + 2, False)
				If Crouch Then
					DrawImage CrouchIcon, x - 50, y
				Else
					DrawImage SprintIcon, x - 50, y
				EndIf
			EndIf
			
			; ~ Boss HP bar
			
			Local npc.NPCs
			
			For npc = Each NPCs
				If npc\Boss <> Null Then
					If npc\Boss\HP > 0 Then
						x = (opt\GraphicWidth / 2) - 202
						y = 50
						width = 404
						height = 20
						
						Color 255,255,255
						Rect(x, y, width, height, False)
						
						Color 255,0,0
						SetFont fo\Font[Font_Digital_Medium]
						
						If (Not npc\Pause) Then
							Text opt\GraphicWidth/2,15,npc\Boss\BossName,True,False
							Color 255,255,255
							
							If npc\Boss\HP =< npc\MaxBossHealth/6 Then
								Color 255, 0, 0
							ElseIf npc\Boss\HP =< npc\MaxBossHealth/4 Then
								Color 255, 106, 0
							ElseIf npc\Boss\HP =< npc\MaxBossHealth/2 Then
								Color 255, 216, 0
							Else
								Color 0, 127, 14
							EndIf
						Else
							If (MilliSecs() Mod 1000) > 300 Then
								Text opt\GraphicWidth/2,15,npc\Boss\BossName+" ("+GetLocalString("Singleplayer","pause")+") ",True,False
							EndIf
							Color 0, 148, 255
						EndIf
						
						Rect(x + 2, y + 3, Int(((width - 2) * (npc\Boss\HP / Float(npc\MaxBossHealth)))), 14)
					EndIf
				EndIf
			Next
			; ~ Weapons
			DrawGunsInHUD()
		EndIf
		If AttachmentMenuOpen Then
			RenderAttachments()
		EndIf
		
		If DebugHUD Then
			x% = 80
			
			Color 255, 255, 255
			SetFont fo\ConsoleFont
			
			;Text x + 250, 50, "Zone: " + (EntityZ(Collider)/8.0)
			Text x + 50, 20, "Delta time: "+ft\DeltaTime
			Text x - 50, 50, "Player Position: (" + f2s(EntityX(Collider), 3) + ", " + f2s(EntityY(Collider), 3) + ", " + f2s(EntityZ(Collider), 3) + ")"
			Text x - 50, 70, "Camera Position: (" + f2s(EntityX(Camera), 3)+ ", " + f2s(EntityY(Camera), 3) +", " + f2s(EntityZ(Camera), 3) + ")"
			Text x - 50, 100, "Player Rotation: (" + f2s(EntityPitch(Collider), 3) + ", " + f2s(EntityYaw(Collider), 3) + ", " + f2s(EntityRoll(Collider), 3) + ")"
			Text x - 50, 120, "Camera Rotation: (" + f2s(EntityPitch(Camera), 3)+ ", " + f2s(EntityYaw(Camera), 3) +", " + f2s(EntityRoll(Camera), 3) + ")"
			Text x - 50, 150, "Map seed: "+RandomSeed
			Text x - 50, 170, "Room: " + PlayerRoom\RoomTemplate\Name
			
			Local ev.Events
			
			For ev.Events = Each Events
				If ev\room = PlayerRoom Then
					Text x - 50, 190, "Room event: " + ev\EventName   
					Text x - 50, 205, "state: " + ev\EventState[0]
					Text x - 50, 220, "state2: " + ev\EventState[1]   
					Text x - 50, 235, "state3: " + ev\EventState[2]
					Text x - 50, 250, "str: " + ev\EventStr
					Exit
				EndIf
			Next
			Text x - 50, 270, "Room coordinates: (" + Floor(EntityX(PlayerRoom\obj) / 8.0 + 0.5) + ", " + Floor(EntityZ(PlayerRoom\obj) / 8.0 + 0.5) + ", angle: "+PlayerRoom\angle + ")"
			Text x - 50, 290, "Current Trigger: " + CheckTriggers()
			Text x - 50, 320, "Stamina: " + f2s(Stamina, 3)
			Text x - 50, 340, "Death timer: " + f2s(KillTimer, 3)               
			Text x - 50, 360, "Blink timer: " + f2s(BlinkTimer, 3)
			Text x - 50, 380, "Health: " + psp\Health
			Text x - 50, 400, "Kevlar: " + psp\Kevlar
			If Curr173 <> Null
				Text x - 50, 430, "SCP - 173 Position (collider): (" + f2s(EntityX(Curr173\Collider), 3) + ", " + f2s(EntityY(Curr173\Collider), 3) + ", " + f2s(EntityZ(Curr173\Collider), 3) + ")"
				Text x - 50, 450, "SCP - 173 Position (obj): (" + f2s(EntityX(Curr173\obj), 3) + ", " + f2s(EntityY(Curr173\obj), 3) + ", " + f2s(EntityZ(Curr173\obj), 3) + ")"
				;Text x - 50, 410, "SCP - 173 Idle: " + Curr173\Idle
				Text x - 50, 470, "SCP - 173 State: " + Curr173\State[0]
			EndIf
			If Curr106 <> Null
				Text x - 50, 490, "SCP - 106 Position: (" + f2s(EntityX(Curr106\obj), 3) + ", " + f2s(EntityY(Curr106\obj), 3) + ", " + f2s(EntityZ(Curr106\obj), 3) + ")"
				Text x - 50, 510, "SCP - 106 Idle: " + Curr106\Idle
				Text x - 50, 530, "SCP - 106 State: " + Curr106\State[0]
			EndIf
			Local offset% = 0
			For npc.NPCs = Each NPCs
				If npc\NPCtype = NPC_SCP_096 Then
					Text x - 50, 550, "SCP - 096 Position: (" + f2s(EntityX(npc\obj), 3) + ", " + f2s(EntityY(npc\obj), 3) + ", " + f2s(EntityZ(npc\obj), 3) + ")"
					Text x - 50, 570, "SCP - 096 Idle: " + npc\Idle
					Text x - 50, 590, "SCP - 096 State: " + npc\State[0]
					Text x - 50, 610, "SCP - 096 Speed: " + f2s(npc\CurrSpeed, 5)
				EndIf
				Text x + - 50, 630, "NPC Blood timer: "+npc\BloodTimer
				;If npc\NPCtype = NPCtypeNTF Then
				;	Text x - 50, 600 + 60 * offset, "MTF " + offset + " Position: (" + f2s(EntityX(npc\obj), 3) + ", " + f2s(EntityY(npc\obj), 3) + ", " + f2s(EntityZ(npc\obj), 3) + ")"
				;	Text x - 50, 640 + 60 * offset, "MTF " + offset + " State: " + npc\State
				;	Text x - 50, 620 + 60 * offset, "MTF " + offset + " LastSeen: " + npc\lastseen					
				;	offset = offset + 1
				;EndIf
			Next
			Text x + 350, 50, "Current Room Position: ("+PlayerRoom\x+", "+PlayerRoom\y+", "+PlayerRoom\z+")"
			
			Text x + 350, 90, SystemProperty("os")+" "+gv\OSBit+" bit, CPU: "+SystemProperty("cpuname")+" (Arch: "+SystemProperty("cpuarch")+", "+GetEnv("NUMBER_OF_PROCESSORS")+" Threads)"
			Text x + 350, 110, "Phys. Memory: "+(AvailPhys()/1024)+" MB/"+(TotalPhys()/1024)+" MB ("+(AvailPhys())+" KB/"+(TotalPhys())+" KB). CPU Usage: "+MemoryLoad()+"%"
			Text x + 350, 130, "Virtual Memory: "+(AvailVirtual()/1024)+" MB/"+(TotalVirtual()/1024)+" MB ("+(AvailVirtual())+" KB/"+(TotalVirtual())+" KB)"
			Text x + 350, 150, "Video Memory: "+(AvailVidMem()/1024)+" MB/"+(TotalVidMem()/1024)+" MB ("+(AvailVidMem())+" KB/"+(TotalVidMem())+" KB)"
			Text x + 350, 170, "Triangles rendered: "+CurrTrisAmount
			Text x + 350, 190, "Active textures: "+ActiveTextures()
			Text x + 350, 210, "MTF Camera Scan Timer: "+MTF_CameraCheckTimer
			
			For g = Each Guns
				If g_I\HoldingGun = g\ID Then
					Text x + 350, 280, "Gun max jam amount: "+g\MaxJams
					Text x + 350, 300, "Gun jam amount: "+g\JamAmount
					Text x + 350, 320, "Gun jam timer: "+g\JamTimer
					Text x + 350, 340, "Gun jam cooldown timer: "+g\JamTimer2
				EndIf
			Next
			
			SetFont fo\Font[Font_Default]
		EndIf
	EndIf
	
;	If (Not MenuOpen) And (Not InvOpen) And (OtherOpen=Null) And (SelectedDoor=Null) And (ConsoleOpen=False) And (Using294=False) And (SelectedScreen=Null) And EndingTimer=>0 And KillTimer >= 0
;		ToggleGuns()
;	EndIf
	
	If SelectedScreen <> Null Then
		DrawImage SelectedScreen\img, opt\GraphicWidth/2-ImageWidth(SelectedScreen\img)/2,opt\GraphicHeight/2-ImageHeight(SelectedScreen\img)/2
	EndIf
	
	Local PrevInvOpen% = InvOpen, MouseSlot% = 66
	
	Local shouldDrawHUD%=True
	If d_I\SelectedDoor <> Null Then
		If SelectedItem <> Null Then
			If SelectedItem\itemtemplate\tempname = "scp005" Then
				shouldDrawHUD = False
			EndIf
		EndIf
		If shouldDrawHUD Then
			If (d_I\SelectedDoor\dir<>DOOR_ELEVATOR_3FLOOR And d_I\SelectedDoor\dir<>DOOR_STORAGE_ELEVATOR) Then
				CameraProject(Camera, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True)+MeshHeight(d_I\ButtonOBJ[BUTTON_NORMAL])*0.015,EntityZ(d_I\ClosestButton,True))
				projY# = ProjectedY()
				CameraProject(Camera, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True)-MeshHeight(d_I\ButtonOBJ[BUTTON_NORMAL])*0.015,EntityZ(d_I\ClosestButton,True))
				scale# = (ProjectedY()-projY)/462.0
				
				x = opt\GraphicWidth/2-317*scale/2
				y = opt\GraphicHeight/2-462*scale/2
				
				SetFont fo\Font[Font_Digital_Small]
				If KeypadMSG <> "" Then 
					If (KeypadTimer Mod 70) < 35 Then Text opt\GraphicWidth/2, y+124*scale, KeypadMSG, True,True
				Else
					Text opt\GraphicWidth/2, y+70*scale, GetLocalString("Devices","access_code"),True,True	
					SetFont fo\Font[Font_Digital_Large]
					Text opt\GraphicWidth/2, y+124*scale, KeypadInput,True,True	
				EndIf
				
				x = x+44*scale
				y = y+249*scale
				
			Else
				CameraProject(Camera, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True)+MeshHeight(d_I\ButtonOBJ[BUTTON_NORMAL])*0.015,EntityZ(d_I\ClosestButton,True))
				projY# = ProjectedY()
				CameraProject(Camera, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True)-MeshHeight(d_I\ButtonOBJ[BUTTON_NORMAL])*0.015,EntityZ(d_I\ClosestButton,True))
				scale# = (ProjectedY()-projY)/462.0
				
				x = opt\GraphicWidth/2-ImageWidth(KeypadHUD)*scale/2
				y = opt\GraphicHeight/2-ImageHeight(KeypadHUD)*scale/2
				
				Color 255,0,0
				x=x+120*scale
				y=y+259*scale
				If (Not co\Enabled)
					If RectsOverlap(x,y,82*scale,82*scale,MousePosX,MousePosY,0,0)
						Rect x,y,82*scale,82*scale,False
					EndIf
				Else
					If co\KeyPad_CurrButton = 0
						Rect x,y,82*scale,82*scale,False
					EndIf
				EndIf
				
				y=y+131*scale
				If (Not co\Enabled)
					If RectsOverlap(x,y,82*scale,82*scale,MousePosX,MousePosY,0,0)
						Rect x,y,82*scale,82*scale,False
					EndIf
				Else
					If co\KeyPad_CurrButton = 1
						Rect x,y,82*scale,82*scale,False
					EndIf
				EndIf
				
				y=y+130*scale
				If (Not co\Enabled)
					If RectsOverlap(x,y,82*scale,82*scale,MousePosX,MousePosY,0,0)
						Rect x,y,82*scale,82*scale,False
					EndIf
				Else
					If co\KeyPad_CurrButton = 2
						Rect x,y,82*scale,82*scale,False
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
	RenderInventory()
	
	If (Not InvOpen) And OtherOpen = Null Then
		
		If SelectedItem <> Null Then
			Select SelectedItem\itemtemplate\tempname
				Case "scp860", "hand", "hand2", "hand3", "25ct", "coin", "fuse", "fuse_purple", "scopebat","vanecoin"
					;[Block]
					DrawImage(SelectedItem\itemtemplate\invimg, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
					;[End Block]
				Case "key0", "key1", "key2", "key3", "key4", "key5", "key6", "key_cave", "key_cave2", "key_class_d", "scp005","misc"
					;[Block]
					DrawImage(SelectedItem\itemtemplate\invimg, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
					;[End Block]
				Case "firstaid", "finefirstaid", "firstaid2"
					;[Block]
					If HUDenabled And psp\IsShowingHUD Then
						If psp\Health < 100 Then
							If (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat") Then
								DrawImage(SelectedItem\itemtemplate\invimg, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
								
								width% = 300
								height% = 20
								x% = opt\GraphicWidth / 2 - width / 2
								y% = opt\GraphicHeight / 2 + 80
								Rect(x, y, width+4, height, False)
								Color 110, 110, 110		
								Rect(x + 3, y + 3, Float(SelectedItem\state * (width - 6) / 100), 14)
							EndIf
						EndIf
					EndIf
					;[End Block]
				Case "scp1102ru"
					;[Block]
					If HUDenabled And psp\IsShowingHUD Then
						DrawImage(SelectedItem\itemtemplate\invimg, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
						
						width% = 300
						height% = 20
						x% = opt\GraphicWidth / 2 - width / 2
						y% = opt\GraphicHeight / 2 + 80
						Rect(x, y, width+4, height, False)
						Color 110, 110, 110
						Rect(x + 3, y + 3, Float(SelectedItem\state * (width - 6) / 100), 14)
					EndIf
					;[End Block]
				Case "paper", "ticket"
					;[Block]
					Select SelectedItem\itemtemplate\name
						Case "Class-D Zone Note"
							If gopt\GameMode = GAMEMODE_DEFAULT Then
								If cpt\Current < 2 Then
									If TaskExists(TASK_READ_NOTE) Then
										EndTask(TASK_READ_NOTE)
									EndIf
									If Inventory[n]\itemtemplate\tempname = "key_class_d" Then
									Else
										If (Not TaskExists(TASK_FIND_CLASS_D_KEY)) Then
											BeginTask(TASK_FIND_CLASS_D_KEY)
										EndIf
									EndIf
								EndIf
							EndIf
					End Select
					If SelectedItem\itemtemplate\img = 0 Then
						Select SelectedItem\itemtemplate\name
							Case "Burnt Note" 
								SelectedItem\itemtemplate\img = LoadImage_Strict("GFX\items\Paper\burnt_note.it")
								SetBuffer ImageBuffer(SelectedItem\itemtemplate\img)
								Color 0,0,0
								SetFont fo\Font[Font_Default_Medium]
								Text 277, 469, AccessCode[0], True, True
								Color 255,255,255
								SetBuffer BackBuffer()
							Case "O5 Council Room Note","Document SCP-372", "Surveillance Room Password Note"
								SelectedItem\itemtemplate\img = LoadImage_Strict(SelectedItem\itemtemplate\imgpath)	
								SelectedItem\itemtemplate\img = ResizeImage2(SelectedItem\itemtemplate\img, ImageWidth(SelectedItem\itemtemplate\img) * MenuScale, ImageHeight(SelectedItem\itemtemplate\img) * MenuScale)
								
								SetBuffer ImageBuffer(SelectedItem\itemtemplate\img)
								
								Select SelectedItem\itemtemplate\name
									Case "O5 Council Room Note"
										Color 0,40,247
										SetFont fo\Font[Font_Journal]
										Text 240*MenuScale, 125*MenuScale, AccessCode[1], True, True
									Case "Document SCP-372"
										Color 37,45,137
										SetFont fo\Font[Font_Journal]
										Text 383*MenuScale, 734*MenuScale, AccessCode[2], True, True
									Case "Surveillance Room Password Note"
										Color 0,10,10
										SetFont fo\Font[Font_Journal]
										Text 220*MenuScale, 140*MenuScale, AccessCode[3], True, True
								End Select
								
								Color 255,255,255
								SetBuffer BackBuffer()
							Case "Movie Ticket"
								SelectedItem\itemtemplate\img=LoadImage_Strict(SelectedItem\itemtemplate\imgpath)	
							Case "Wolfnaya's Room Note"
								SelectedItem\itemtemplate\img=LoadImage_Strict(SelectedItem\itemtemplate\imgpath)	
								SelectedItem\itemtemplate\img = ResizeImage2(SelectedItem\itemtemplate\img, ImageWidth(SelectedItem\itemtemplate\img) * MenuScale, ImageHeight(SelectedItem\itemtemplate\img) * MenuScale)
								ecst\UnlockedWolfnaya = True
							Default 
								SelectedItem\itemtemplate\img=LoadImage_Strict(SelectedItem\itemtemplate\imgpath)	
								SelectedItem\itemtemplate\img = ResizeImage2(SelectedItem\itemtemplate\img, ImageWidth(SelectedItem\itemtemplate\img) * MenuScale, ImageHeight(SelectedItem\itemtemplate\img) * MenuScale)
						End Select
						
						MaskImage(SelectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					DrawImage(SelectedItem\itemtemplate\img, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\img) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\img) / 2)
					;[End Block]
;				Case "scp1025"
;					;[Block]
;					If SelectedItem\itemtemplate\img=0 Then
;						SelectedItem\itemtemplate\img=LoadImage_Strict("GFX\items\1025\1025_"+Int(SelectedItem\state)+".jpg")	
;						SelectedItem\itemtemplate\img=ResizeImage2(SelectedItem\itemtemplate\img, ImageWidth(SelectedItem\itemtemplate\img) * MenuScale, ImageHeight(SelectedItem\itemtemplate\img) * MenuScale)
;						
;						MaskImage(SelectedItem\itemtemplate\img, 255, 0, 255)
;					EndIf
;					
;					DrawImage(SelectedItem\itemtemplate\img, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\img) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\img) / 2)
;					
;					;[End Block]
				Case "radio","18vradio","fineradio","veryfineradio"
					;[Block]
					If SelectedItem\itemtemplate\img=0 Then
						SelectedItem\itemtemplate\img=LoadImage_Strict(SelectedItem\itemtemplate\imgpath)	
						MaskImage(SelectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					If RadioState[5] = 0 And SelectedItem\state > 0 Then
						CreateMsg(GetLocalString("Items","radio_1"))
					EndIf
					
					strtemp$ = ""
					
					If SelectedItem\itemtemplate\img <> 0 Then
						x = opt\GraphicWidth - ImageWidth(SelectedItem\itemtemplate\img)
						y = opt\GraphicHeight - ImageHeight(SelectedItem\itemtemplate\img)
						
						DrawImage(SelectedItem\itemtemplate\img, x, y)
					EndIf
					
					If SelectedItem\state > 0 Then
						If PlayerRoom\RoomTemplate\Name <> "pocketdimension" Then; Lor CoffinDistance > 4.0 Then
							Select Int(SelectedItem\state2)
								Case 0
									strtemp = GetLocalString("Devices","radio_track_found")
									If (Not EnableUserTracks)
										strtemp = strtemp + GetLocalString("Devices","radio_track_disabled")
									ElseIf UserTrackMusicAmount<1
										strtemp = strtemp + GetLocalString("Devices","radio_no_track")
									EndIf
								Case 1
									DebugLog RadioState[1]
									strtemp = GetLocalString("Devices","radio_cb_1")
								Case 2
									strtemp = GetLocalString("Devices","radio_scp")
								Case 3
									strtemp = GetLocalString("Devices","radio_cb_2")
							End Select 
							
							x=x+66
							y=y+419
							
							Color (30,30,30)
							
							If SelectedItem\state <= 100 Then
								For i = 0 To 4
									Rect(x, y+8*i, 43 - i * 6, 4, Ceil(SelectedItem\state / 20.0) > 4 - i )
								Next
							EndIf	
							
							SetFont fo\Font[Font_Digital_Small]
							Text(x+60, y, GetLocalString("Devices","radio_channel"))						
							
							If SelectedItem\itemtemplate\tempname = "veryfineradio" Then
								strtemp = ""
								For i = 0 To Rand(5, 30)
									strtemp = strtemp + Chr(Rand(1,100))
								Next
								
								SetFont fo\Font[Font_Digital_Large]
								Text(x+97, y+16, Rand(0,9),True,True)
							Else
								SetFont fo\Font[Font_Digital_Large]
								Text(x+97, y+16, Int(SelectedItem\state2+1),True,True)
							EndIf
							
							SetFont fo\Font[Font_Digital_Small]
							
							If strtemp <> "" Then
								strtemp = Right(Left(strtemp, (Int(MilliSecs()/300) Mod Len(strtemp))),10)
								Text(x+32, y+33, strtemp)
							EndIf
							
							SetFont fo\Font[Font_Default]
						EndIf
					EndIf
					
					;[End Block]
				Case "navigator", "nav"
					;[Block]
					If SelectedItem\itemtemplate\img=0 Then
						SelectedItem\itemtemplate\img=LoadImage_Strict(SelectedItem\itemtemplate\imgpath)	
						MaskImage(SelectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					x = opt\GraphicWidth - ImageWidth(SelectedItem\itemtemplate\img)*0.5+20
					y = opt\GraphicHeight - ImageHeight(SelectedItem\itemtemplate\img)*0.4-85
					width = 287
					height = 256
					
					Local PlayerX,PlayerZ
					
					DrawImage(SelectedItem\itemtemplate\img, x - ImageWidth(SelectedItem\itemtemplate\img) / 2, y - ImageHeight(SelectedItem\itemtemplate\img) / 2 + 85)
					
					SetFont fo\Font[Font_Digital_Small]
					
					If PlayerRoom\RoomTemplate\Name = "pocketdimension" Then
						If (MilliSecs() Mod 1000) > 300 Then
							Color 30,30,30
							Text(x, y + height / 2 - 80, GetLocalString("Devices","nav_pd_1"), True)
							Text(x, y + height / 2 - 60, GetLocalString("Devices","nav_pd_2"), True)						
						EndIf
					Else
						
						If (SelectedItem\state > 0 Or SelectedItem\itemtemplate\name = "S-NAV Navigator Ultimate") Then; And PlayerRoom\RoomTemplate\Name <> "cont_895" Then
							
							PlayerX% = Floor(EntityX(Collider) / 8.0 + 0.5)
							PlayerZ% = Floor(EntityZ(Collider) / 8.0 + 0.5)
							
							SetBuffer ImageBuffer(NavBG)
							Local xx = x-ImageWidth(SelectedItem\itemtemplate\img)/2
							Local yy = y-ImageHeight(SelectedItem\itemtemplate\img)/2+85
							DrawImage(SelectedItem\itemtemplate\img, xx, yy)
							
							Local posX# = EntityX(Collider) - 4.0
							Local posZ# = EntityZ(Collider) - 4.0
							Local stepsX% = 0
							Local stepsZ% = 0
							Local tempPos# = posX
							While tempPos < 0.0
								stepsX = stepsX + 1
								tempPos = tempPos + 8.0
							Wend
							tempPos# = posZ
							While tempPos < 0.0
								stepsZ = stepsZ + 1
								tempPos = tempPos + 8.0
							Wend
							x = x - 12 + ((posX + (8.0 * stepsX)) Mod 8.0) * 3
							y = y + 12 - ((posZ + (8.0 * stepsZ)) Mod 8.0) * 3
							For z2 = Max(0, PlayerZ - 6) To Min(MapGridSize - 1, PlayerZ + 6)
								For x2 = Max(0, PlayerX - 6) To Min(MapGridSize - 1, PlayerX + 6)
									If SelectedItem\itemtemplate\name = "S-NAV 300 Navigator" And Rand(0,1) Then Exit
									;If CoffinDistance > 16.0 Lor Rnd(16.0) < CoffinDistance Then
									If CurrGrid\Grid[x2 + (z2 * MapGridSize)] Then
										Local drawx% = x + (PlayerX - x2) * 24 , drawy% = y - (PlayerZ - z2) * 24
										
										Color (30,30,30)
										If SelectedItem\itemtemplate\name = "S-NAV Navigator" Then Color(100, 0, 0)
										
										If MapTemp[(x2 + 1) * MapWidth + z2] = False Then Rect(drawx - (12 * MenuScale), drawy - (12 * MenuScale), MenuScale, 24 * MenuScale)
										If MapTemp[(x2 - 1) * MapWidth + z2] = False Then Rect(drawx + (12 * MenuScale), drawy - (12 * MenuScale), MenuScale, 24 * MenuScale)
										
										If MapTemp[x2 * MapWidth + z2 - 1] = False Then Rect(drawx - (12 * MenuScale), drawy - (12 * MenuScale), 24 * MenuScale, MenuScale)
										If MapTemp[x2 * MapWidth + z2 + 1] = False Then Rect(drawx - (12 * MenuScale), drawy + (12 * MenuScale), 24 * MenuScale, MenuScale)
									EndIf
									;EndIf
								Next
							Next
							SetBuffer BackBuffer()
							DrawImageRect NavBG,xx+80,yy+70,xx+80,yy+70,270,230
							Color 30,30,30
							If SelectedItem\itemtemplate\name = "S-NAV Navigator" Then Color(100, 0, 0)
							Rect xx+80,yy+70,270,230,False
							
							x = opt\GraphicWidth - ImageWidth(SelectedItem\itemtemplate\img)*0.5+20
							y = opt\GraphicHeight - ImageHeight(SelectedItem\itemtemplate\img)*0.4-85
							
							If (MilliSecs() Mod 1000) > 300 Then
								If SelectedItem\itemtemplate\name <> "S-NAV 310 Navigator" And SelectedItem\itemtemplate\name <> "S-NAV Navigator Ultimate" Then
									Color(100, 0, 0)
									Text(x - width/2 + 10, y - height/2 + 10, GetLocalString("Devices","nav_low_signal"))
								EndIf
								
								Color(0, 150, 50)
								Oval(x-7,y-5,width/20,height/20) ; ~ Player
							EndIf
							
							If SelectedItem\itemtemplate\name = "S-NAV Navigator" Then 
								Color(100, 0, 0)
							Else
								Color (30,30,30)
							EndIf
							
							Local SCPs_found% = 0
							If SelectedItem\itemtemplate\name = "S-NAV Navigator Ultimate" And (MilliSecs() Mod 600) < 400 Then
								Local dist# = EntityDistanceSquared(Camera, Curr173\obj)
								If dist < PowTwo(8.0 * 4) Then
									dist = Sqr(dist)
									Color 100, 0, 0
									Oval(x - dist * 3, y - 7 - dist * 3, dist * 3 * 2, dist * 3 * 2, False)
									Text(x - width / 2 + 10, y - height / 2 + 30, "SCP-173")
									SCPs_found% = SCPs_found% + 1
								EndIf
								dist# = EntityDistanceSquared(Camera, Curr106\obj)
								If dist < PowTwo(8.0 * 4) Then
									dist = Sqr(dist)
									Color 100, 0, 0
									Oval(x - dist * 1.5, y - 7 - dist * 1.5, dist * 3, dist * 3, False)
									Text(x - width / 2 + 10, y - height / 2 + 30 + (20*SCPs_found), "SCP-106")
									SCPs_found% = SCPs_found% + 1
								EndIf
								If Curr096<>Null Then 
									dist# = EntityDistanceSquared(Camera, Curr096\obj)
									If dist < PowTwo(8.0 * 4) Then
										dist = Sqr(dist)
										Color 100, 0, 0
										Oval(x - dist * 1.5, y - 7 - dist * 1.5, dist * 3, dist * 3, False)
										Text(x - width / 2 + 10, y - height / 2 + 30 + (20*SCPs_found), "SCP-096")
										SCPs_found% = SCPs_found% + 1
									EndIf
								EndIf
								Local np.NPCs
								For np.NPCs = Each NPCs
									If np\NPCtype = NPC_SCP_049
										dist# = EntityDistanceSquared(Camera, np\obj)
										If dist < PowTwo(8.0 * 4) Then
											dist = Sqr(dist)
											If (Not np\HideFromNVG)
												Color 100, 0, 0
												Oval(x - dist * 1.5, y - 7 - dist * 1.5, dist * 3, dist * 3, False)
												Text(x - width / 2 + 10, y - height / 2 + 30 + (20*SCPs_found), "SCP-049")
												SCPs_found% = SCPs_found% + 1
											EndIf
										EndIf
									EndIf
								Next
;								If PlayerRoom\RoomTemplate\Name = "cont_895" Then
;									If CoffinDistance < 8.0 Then
;										dist = Rnd(4.0, 8.0)
;										Color 100, 0, 0
;										Oval(x - dist * 1.5, y - 7 - dist * 1.5, dist * 3, dist * 3, False)
;										Text(x - width / 2 + 10, y - height / 2 + 30 + (20*SCPs_found), "SCP-895")
;									EndIf
;								EndIf
							End If
							
							Color (30,30,30)
							If SelectedItem\itemtemplate\name = "S-NAV Navigator" Then Color(100, 0, 0)
							If SelectedItem\state <= 100 Then
								xtemp = x - width/2 + 196
								ytemp = y - height/2 + 10
								Rect xtemp,ytemp,80,20,False
								; ~ Battery
								If SelectedItem\state <= 20.0 Then
									Color(100, 0, 0)
									If SelectedItem\itemtemplate\name = "S-NAV 310 Navigator" Then Text (x - width/2 + 10, y - height/2 + 10, GetLocalString("Devices","nav_bat_2"))
								Else
									Color(30, 30, 30)
									If SelectedItem\itemtemplate\name = "S-NAV 310 Navigator" Then 	Text (x - width/2 + 10, y - height/2 + 10, GetLocalString("Devices","nav_bat_1"))
								EndIf
								For i = 1 To Min(Ceil(SelectedItem\state / 10.0), 10.0)
									Rect(xtemp + ((i * 8) * MenuScale) - (6 * MenuScale), ytemp + (4 * MenuScale), 4 * MenuScale, 12 * MenuScale)
								Next
								SetFont(fo\Font[Font_Digital_Small])
							EndIf
						EndIf
					EndIf
					;[End Block]
				Case "badge"
					;[Block]
					If SelectedItem\itemtemplate\img=0 Then
						SelectedItem\itemtemplate\img=LoadImage_Strict(SelectedItem\itemtemplate\imgpath)	
						MaskImage(SelectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					DrawImage(SelectedItem\itemtemplate\img, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\img) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\img) / 2)
					;[End Block]
				Case "oldpaper"
					;[Block]
					If SelectedItem\itemtemplate\img = 0 Then
						SelectedItem\itemtemplate\img = LoadImage_Strict(SelectedItem\itemtemplate\imgpath)	
						SelectedItem\itemtemplate\img = ResizeImage2(SelectedItem\itemtemplate\img, ImageWidth(SelectedItem\itemtemplate\img) * MenuScale, ImageHeight(SelectedItem\itemtemplate\img) * MenuScale)
						
						MaskImage(SelectedItem\itemtemplate\img, 255, 0, 255)
					EndIf
					
					DrawImage(SelectedItem\itemtemplate\img, opt\GraphicWidth / 2 - ImageWidth(SelectedItem\itemtemplate\img) / 2, opt\GraphicHeight / 2 - ImageHeight(SelectedItem\itemtemplate\img) / 2)
					;[End Block]
			End Select
			
		EndIf		
	EndIf
	
	If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" Then
		If hds\ExplodeTimer > 0 And hds\Health < 0 Then
			If (MilliSecs() Mod 1000) > 300 Then
				SetFont fo\Font[Font_Digital_Large]
				Color 255,0,0
				Text(opt\GraphicWidth/2, opt\GraphicHeight/2, GetLocalString("Hazardous Suit","warning"),True,True)
				SetFont fo\Font[Font_Digital_Medium]
				Text(opt\GraphicWidth/2, opt\GraphicHeight/2+50, GetLocalString("Hazardous Suit","systems_critical"),True,True)
			EndIf
		ElseIf hds\ExplodeTimer > 0 And hds\Health > 100 Then
			If (MilliSecs() Mod 500) > 300 Then
				SetFont fo\Font[Font_Digital_Large]
				Color 255,200,0
				Text(opt\GraphicWidth/2, opt\GraphicHeight/2, GetLocalString("Hazardous Suit","warning"),True,True)
				SetFont fo\Font[Font_Digital_Medium]
				Text(opt\GraphicWidth/2, opt\GraphicHeight/2+50, GetLocalString("Hazardous Suit","system_overcharge"),True,True)
			EndIf
		EndIf
	EndIf
	
	DrawTasks()
	
	If gopt\GameMode = GAMEMODE_NTF Then
		RenderCommunicationAndSocialWheel()
	EndIf
	
	CatchErrors("DrawGUI")
End Function

Function UpdateGUI()
	CatchErrors("Uncaught (UpdateGUI)")
	
	Local temp%, x%, y%, z%, i%, yawvalue#, pitchvalue#, pvt%
	Local x2#,y2#,z2#
	Local n%, xtemp, ytemp, strtemp$, projY#, scale#, width%, height%
	
	Local e.Events, it.Items, np.NPCs, ne.NewElevator
	
	UpdateInterfaceIcons()
	
	If (Not MenuOpen) And (Not InvOpen) And (OtherOpen=Null) And (ConsoleOpen=False) And (Using294=False) And (SelectedScreen=Null) And EndingTimer=>0 And KillTimer >= 0
		If gopt\GameMode = GAMEMODE_NTF Then
			UpdateCommunicationAndSocialWheel()
		EndIf
	EndIf
	
	UpdateSplashTexts()
	UpdateSplashMsg()
	
	Local g.Guns
	
	If (Not MenuOpen) And (Not InvOpen) And (OtherOpen=Null) And (d_I\SelectedDoor=Null) And (Not AttachmentMenuOpen) And (Not ConsoleOpen) And (Not Using294) And (SelectedScreen=Null) And EndingTimer=>0 And KillTimer >= 0
		ToggleGuns()
	EndIf
		
	mpl\SlotsDisplayTimer = Max(mpl\SlotsDisplayTimer-FPSfactor,0.0)
	
	If SelectedScreen <> Null Then
		If MouseUp1 Lor MouseHit2 Then
			FreeImage SelectedScreen\img : SelectedScreen\img = 0
			SelectedScreen = Null
			MouseUp1 = False
		EndIf
	EndIf
	
	Local PrevInvOpen% = InvOpen, MouseSlot% = 66
	
	Local shouldDrawHUD%=True
	If d_I\SelectedDoor <> Null Then
		If SelectedItem <> Null Then
			If SelectedItem\itemtemplate\tempname = "scp005" Then
				UseDoor(d_I\SelectedDoor)
				shouldDrawHUD = False
			Else
				SelectedItem = Null
			EndIf
		EndIf
		
		If shouldDrawHUD Then
			HideEntity g_I\GunPivot
			If (d_I\SelectedDoor\dir<>DOOR_ELEVATOR_3FLOOR And d_I\SelectedDoor\dir<>DOOR_STORAGE_ELEVATOR) Then
				pvt = CreatePivot()
				PositionEntity pvt, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True),EntityZ(d_I\ClosestButton,True)
				RotateEntity pvt, 0, EntityYaw(d_I\ClosestButton,True)-180,0
				MoveEntity pvt, 0,0,0.22
				PositionEntity Camera, EntityX(pvt),EntityY(pvt),EntityZ(pvt)
				PointEntity Camera, d_I\ClosestButton
				pvt = FreeEntity_Strict(pvt)
				
				CameraProject(Camera, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True)+MeshHeight(d_I\ButtonOBJ[BUTTON_NORMAL])*0.015,EntityZ(d_I\ClosestButton,True))
				projY# = ProjectedY()
				CameraProject(Camera, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True)-MeshHeight(d_I\ButtonOBJ[BUTTON_NORMAL])*0.015,EntityZ(d_I\ClosestButton,True))
				scale# = (ProjectedY()-projY)/462.0
				
				x = opt\GraphicWidth/2-ImageWidth(KeypadHUD)*scale/2
				y = opt\GraphicHeight/2-ImageHeight(KeypadHUD)*scale/2
				
				If KeypadMSG <> "" Then 
					KeypadTimer = KeypadTimer-FPSfactor2
					
					If KeypadTimer =<0 Then
						KeypadMSG = ""
						d_I\SelectedDoor = Null
						MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
					EndIf
				EndIf
				
				x = x+44*scale
				y = y+249*scale
				
				For n = 0 To 3
					For i = 0 To 2
						xtemp = x+Int(58.5*scale*n)
						ytemp = y+(67*scale)*i
						
						temp = False
						If MouseOn(xtemp,ytemp, 54*scale,65*scale) And KeypadMSG = "" Then
							If MouseUp1 Then 
								PlaySound_Strict ButtonSFX[0]
								
								Select (n+1)+(i*4)
									Case 1,2,3
										KeypadInput=KeypadInput + ((n+1)+(i*4))
									Case 4
										KeypadInput=KeypadInput + "0"
									Case 5,6,7
										KeypadInput=KeypadInput + ((n+1)+(i*4)-1)
									Case 8
										If KeypadInput = d_I\SelectedDoor\Code Then
											PlaySound_Strict ScannerSFX1
											
											If d_I\SelectedDoor\Code = Str(AccessCode[0]) Then
												GiveAchievement(AchvMaynard)
											ElseIf d_I\SelectedDoor\Code = "7816"
												GiveAchievement(AchvHarp)
											EndIf									
											
											d_I\SelectedDoor\locked = 0
											UseDoor(d_I\SelectedDoor,True)
											d_I\SelectedDoor = Null
											MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : Mouse_X_Speed_1#=0.0 : Mouse_Y_Speed_1#=0.0
										Else
											PlaySound_Strict ScannerSFX2
											KeypadMSG = Upper(GetLocalString("Doors", "keypad_denied"))
											KeypadTimer = 210
											KeypadInput = ""	
										EndIf
									Case 9,10,11
										KeypadInput=KeypadInput + ((n+1)+(i*4)-2)
									Case 12
										KeypadInput = ""
								End Select 
								
								If Len(KeypadInput)> 4 Then KeypadInput = Left(KeypadInput,4)
							EndIf
							
						Else
							temp = False
						EndIf
						
					Next
				Next
			Else
				pvt = CreatePivot()
				PositionEntity pvt, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True),EntityZ(d_I\ClosestButton,True)
				RotateEntity pvt, 0, EntityYaw(d_I\ClosestButton,True)-180,0
				MoveEntity pvt, 0,0,0.3
				PositionEntity Camera, EntityX(pvt),EntityY(pvt),EntityZ(pvt)
				PointEntity Camera, d_I\ClosestButton
				pvt = FreeEntity_Strict(pvt)
				
				CameraZoom Camera,1.0
				
				CameraProject(Camera, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True)+MeshHeight(d_I\ButtonOBJ[BUTTON_NORMAL])*0.015,EntityZ(d_I\ClosestButton,True))
				projY# = ProjectedY()
				CameraProject(Camera, EntityX(d_I\ClosestButton,True),EntityY(d_I\ClosestButton,True)-MeshHeight(d_I\ButtonOBJ[BUTTON_NORMAL])*0.015,EntityZ(d_I\ClosestButton,True))
				scale# = (ProjectedY()-projY)/462.0
				
				MoveEntity Camera,0.001,0.1672,0
				
				x = opt\GraphicWidth/2-ImageWidth(KeypadHUD)*scale/2
				y = opt\GraphicHeight/2-ImageHeight(KeypadHUD)*scale/2
				
				If co\Enabled
					If co\WaitTimer = 0.0
						If GetDPadButtonPress()=0
							co\KeyPad_CurrButton = co\KeyPad_CurrButton - 1
							PlaySound_Strict co\SelectSFX
							co\WaitTimer = FPSfactor2
							If co\KeyPad_CurrButton < 0
								co\KeyPad_CurrButton = 2
							EndIf
						ElseIf GetDPadButtonPress()=180
							co\KeyPad_CurrButton = co\KeyPad_CurrButton + 1
							PlaySound_Strict co\SelectSFX
							co\WaitTimer = FPSfactor2
							If co\KeyPad_CurrButton > 2
								co\KeyPad_CurrButton = 0
							EndIf
						EndIf
						
						If GetLeftAnalogStickPitch(True) > 0.0
							co\KeyPad_CurrButton = co\KeyPad_CurrButton - 1
							PlaySound_Strict co\SelectSFX
							co\WaitTimer = FPSfactor2
							If co\KeyPad_CurrButton < 0
								co\KeyPad_CurrButton = 2
							EndIf
						ElseIf GetLeftAnalogStickPitch(True) < 0.0
							co\KeyPad_CurrButton = co\KeyPad_CurrButton + 1
							PlaySound_Strict co\SelectSFX
							co\WaitTimer = FPSfactor2
							If co\KeyPad_CurrButton > 2
								co\KeyPad_CurrButton = 0
							EndIf
						EndIf
					Else
						If co\WaitTimer > 0.0 And co\WaitTimer < 15.0
							co\WaitTimer = co\WaitTimer + FPSfactor2
						ElseIf co\WaitTimer >= 15.0
							co\WaitTimer = 0.0
						EndIf
					EndIf
				EndIf
				
				x=x+120*scale
				y=y+259*scale
				If (Not co\Enabled)
					If RectsOverlap(x,y,82*scale,82*scale,MousePosX,MousePosY,0,0)
						If MouseHit1
							PlaySound_Strict ButtonSFX[0]
							StartNewElevator(d_I\SelectedDoor,3)
							d_I\SelectedDoor = Null
							ResetInput()
						EndIf
					EndIf
				Else
					If co\KeyPad_CurrButton = 0
						If JoyHit(CKM_Press)
							PlaySound_Strict ButtonSFX[0]
							StartNewElevator(d_I\SelectedDoor,3)
							d_I\SelectedDoor = Null
							ResetInput()
						EndIf
					EndIf
				EndIf
				
				y=y+131*scale
				If (Not co\Enabled)
					If RectsOverlap(x,y,82*scale,82*scale,MousePosX,MousePosY,0,0)
						If MouseHit1
							PlaySound_Strict ButtonSFX[0]
							StartNewElevator(d_I\SelectedDoor,2)
							d_I\SelectedDoor = Null
							ResetInput()
						EndIf
					EndIf
				Else
					If co\KeyPad_CurrButton = 1
						If JoyHit(CKM_Press)
							PlaySound_Strict ButtonSFX[0]
							StartNewElevator(d_I\SelectedDoor,2)
							d_I\SelectedDoor = Null
							ResetInput()
						EndIf
					EndIf
				EndIf
				
				y=y+130*scale
				If (Not co\Enabled)
					If RectsOverlap(x,y,82*scale,82*scale,MousePosX,MousePosY,0,0)
						If MouseHit1
							PlaySound_Strict ButtonSFX[0]
							StartNewElevator(d_I\SelectedDoor,1)
							d_I\SelectedDoor = Null
							ResetInput()
						EndIf
					EndIf
				Else
					If co\KeyPad_CurrButton = 2
						If JoyHit(CKM_Press)
							PlaySound_Strict ButtonSFX[0]
							StartNewElevator(d_I\SelectedDoor,1)
							d_I\SelectedDoor = Null
							ResetInput()
						EndIf
					EndIf
				EndIf
			EndIf
			
			If (Not co\Enabled)
				If MouseHit2 Then
					d_I\SelectedDoor = Null
					ResetInput()
				EndIf
			Else
				If JoyHit(CKM_Back)
					PlaySound_Strict ButtonSFX[0]
					d_I\SelectedDoor = Null
					ResetInput()
				EndIf
			EndIf
		Else
			d_I\SelectedDoor = Null
		EndIf
	Else
		KeypadInput = ""
		KeypadTimer = 0
		KeypadMSG = ""
	EndIf
	
	If (InteractHit(1,CK_Pause) Lor ((Not InFocus()) And (Not MenuOpen)) Lor (opt\SteamEnabled And Steam_GetOverlayUpdated() = 1 And (Not (MenuOpen Lor InvOpen)))) And EndingTimer = 0 Then
		If MenuOpen And (Not InvOpen) Then
			ResumeSounds()
			If OptionsMenu <> 0 Then SaveOptionsINI()
			DeleteMenuGadgets()
			ResetInput()
		Else
			PauseSounds()
		EndIf
		MenuOpen = (Not MenuOpen)
		
		AchievementsMenu = 0
		OptionsMenu = 0
		QuitMSG = 0
		
		d_I\SelectedDoor = Null
		SelectedScreen = Null
		SelectedMonitor = Null
	EndIf
	
	UpdateInventory()
	
	If (Not InvOpen) And OtherOpen = Null Then
		
		If SelectedItem <> Null Then
			Select SelectedItem\itemtemplate\tempname
;! ~ Nostalgia - Items
				Case "vanecoin"
					;[Block]
					If SelectedItem\state = 0 Then
						PlaySound_Strict LoadTempSound("SFX\SCP\1162\NostalgiaCancer"+Rand(6,10)+".ogg")
						CreateMsg(Chr(34)+GetLocalString("Items","vane")+Chr(34))
						SelectedItem\state = 1
					EndIf
					;[End Block]
;! ~ Devices - Items
				Case "radio","18vradio","fineradio","veryfineradio"
					;[Block]
					If SelectedItem\state <= 100 Then SelectedItem\state = Max(0, SelectedItem\state - FPSfactor * 0.004)
					
					If RadioState[5] = 0 Then
						If SelectedItem\state > 0 Then
							RadioState[5] = 1
						EndIf
						RadioState[0] = -1
					EndIf
					
					x = opt\GraphicWidth - ImageWidth(SelectedItem\itemtemplate\img)
					y = opt\GraphicHeight - ImageHeight(SelectedItem\itemtemplate\img)
					
					If SelectedItem\state > 0 Then
						If PlayerRoom\RoomTemplate\Name = "pocketdimension" Then; Lor CoffinDistance < 4.0 Then
							ResumeChannel(RadioCHN[5])
							If ChannelPlaying(RadioCHN[5]) = False Then RadioCHN[5] = PlaySound_Strict(RadioStatic)	
						Else
							Select Int(SelectedItem\state2)
								Case 0
									ResumeChannel(RadioCHN[0])
									If (Not EnableUserTracks)
										If ChannelPlaying(RadioCHN[0]) = False Then RadioCHN[0] = PlaySound_Strict(RadioStatic)
										strtemp = strtemp + GetLocalString("Devices","radio_track_disabled")
									ElseIf UserTrackMusicAmount<1
										If ChannelPlaying(RadioCHN[0]) = False Then RadioCHN[0] = PlaySound_Strict(RadioStatic)
										strtemp = strtemp + GetLocalString("Devices","radio_no_track")
									Else
										If (Not ChannelPlaying(RadioCHN[0]))
											If (Not UserTrackFlag%)
												If UserTrackMode
													If RadioState[0]<(UserTrackMusicAmount-1)
														RadioState[0] = RadioState[0] + 1
													Else
														RadioState[0] = 0
													EndIf
													UserTrackFlag = True
												Else
													RadioState[0] = Rand(0,UserTrackMusicAmount-1)
												EndIf
											EndIf
											If CurrUserTrack%<>0 Then FreeSound_Strict(CurrUserTrack%) : CurrUserTrack% = 0
											CurrUserTrack% = LoadSound_Strict("SFX\Radio\UserTracks\"+UserTrackName[RadioState[0]])
											RadioCHN[0] = PlaySound_Strict(CurrUserTrack%)
											DebugLog "CurrTrack: "+RadioState[0]
											DebugLog UserTrackName[RadioState[0]]
										Else
											strtemp = strtemp + Upper(UserTrackName[RadioState[0]]) + "          "
											UserTrackFlag = False
										EndIf
										
										If KeyHit(2) Then
											PlaySound_Strict RadioSquelch
											If (Not UserTrackFlag%)
												If UserTrackMode
													If RadioState[0]<(UserTrackMusicAmount-1)
														RadioState[0] = RadioState[0] + 1
													Else
														RadioState[0] = 0
													EndIf
													UserTrackFlag = True
												Else
													RadioState[0] = Rand(0,UserTrackMusicAmount-1)
												EndIf
											EndIf
											If CurrUserTrack%<>0 Then FreeSound_Strict(CurrUserTrack%) : CurrUserTrack% = 0
											CurrUserTrack% = LoadSound_Strict("SFX\Radio\UserTracks\"+UserTrackName[RadioState[0]])
											RadioCHN[0] = PlaySound_Strict(CurrUserTrack%)
											DebugLog "CurrTrack: "+RadioState[0]
											DebugLog UserTrackName[RadioState[0]]
										EndIf
									EndIf
								Case 1
									ResumeChannel(RadioCHN[1])
									If ChannelPlaying(RadioCHN[1]) = False Then
										If RadioState[1] => 5 Then
											RadioCHN[1] = PlaySound_Strict(RadioSFX[0 * 10 + 1])	
											RadioState[1] = 0
										Else
											RadioState[1]=RadioState[1]+1	
											RadioCHN[1] = PlaySound_Strict(RadioSFX[0 * 10])	
										EndIf
									EndIf
								Case 2
									ResumeChannel(RadioCHN[2])
									If ChannelPlaying(RadioCHN[2]) = False Then
										RadioState[2]=RadioState[2]+1
										If RadioState[2] = 17 Then RadioState[2] = 1
										If Floor(RadioState[2]/2)=Ceil(RadioState[2]/2) Then
											RadioCHN[2] = PlaySound_Strict(RadioSFX[1 * 10 + Int(RadioState[2]/2)])
										Else
											RadioCHN[2] = PlaySound_Strict(RadioSFX[1 * 10])
										EndIf
									EndIf 
								Case 3
									ResumeChannel(RadioCHN[3])
									If ChannelPlaying(RadioCHN[3]) = False Then RadioCHN[3] = PlaySound_Strict(RadioStatic)
									If (Not ecst\CIArrived) Then
										If ecst\NTFArrived Then
											If MTFtimer > 0 Then 
												RadioState[3]=RadioState[3]+Max(Rand(-10,1),0)
												Select RadioState[3]
													Case 40
														If Not RadioState3[0] Then
															RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random1.ogg"))
															RadioState[3] = RadioState[3]+1	
															RadioState3[0] = True	
														EndIf											
													Case 400
														If Not RadioState3[1] Then
															RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random2.ogg"))
															RadioState[3] = RadioState[3]+1	
															RadioState3[1] = True	
														EndIf	
													Case 800
														If Not RadioState3[2] Then
															RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random3.ogg"))
															RadioState[3] = RadioState[3]+1	
															RadioState3[2] = True
														EndIf													
													Case 1200
														If Not RadioState3[3] Then
															RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random4.ogg"))	
															RadioState[3] = RadioState[3]+1	
															RadioState3[3] = True
														EndIf
													Case 1600
														If Not RadioState3[4] Then
															RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random5.ogg"))	
															RadioState[3] = RadioState[3]+1
															RadioState3[4] = True
														EndIf
													Case 2000
														If Not RadioState3[5] Then
															RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random6.ogg"))	
															RadioState[3] = RadioState[3]+1
															RadioState3[5] = True
														EndIf
													Case 2400
														If Not RadioState3[6] Then
															RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Random7.ogg"))	
															RadioState[3] = RadioState[3]+1
															RadioState3[6] = True
														EndIf
												End Select
											EndIf
										EndIf
									Else
										RadioState[3]=RadioState[3]+Max(Rand(-10,1),0)
										Select RadioState[3]
											Case 40
												If Not RadioState3[0] Then
													RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\CI\Dialogues\Random_1.ogg"))
													RadioState[3] = RadioState[3]+1	
													RadioState3[0] = True	
												EndIf											
											Case 400
												If Not RadioState3[1] Then
													RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\CI\Dialogues\Random_2.ogg"))
													RadioState[3] = RadioState[3]+1	
													RadioState3[1] = True	
												EndIf	
											Case 800
												If Not RadioState3[2] Then
													RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\CI\Dialogues\Random_3.ogg"))
													RadioState[3] = RadioState[3]+1	
													RadioState3[2] = True
												EndIf													
											Case 1200
												If Not RadioState3[3] Then
													RadioCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Character\MTF\Dialogues\Secret.ogg"))	
													RadioState[3] = RadioState[3]+1	
													RadioState3[3] = True
												EndIf
										End Select
									EndIf
								Case 4
									ResumeChannel(RadioCHN[6])
									If ChannelPlaying(RadioCHN[6]) = False Then RadioCHN[6] = PlaySound_Strict(RadioStatic)
									ResumeChannel(RadioCHN[4])
									If ChannelPlaying(RadioCHN[4]) = False Then 
										If RemoteDoorOn = False And RadioState[8] = False Then
											RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\Chatter3.ogg"))	
											RadioState[8] = True
										Else
											RadioState[4]=RadioState[4]+Max(Rand(-10,1),0)
											Select RadioState[4]
												Case 10
													If (Not Curr106\Contained)
														If Not RadioState4[0] Then
															RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\OhGod.ogg"))
															RadioState[4] = RadioState[4]+1
															RadioState4[0] = True
														EndIf
													EndIf													
												Case 100
													If Not RadioState4[1] Then
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\Chatter2.ogg"))
														RadioState[4] = RadioState[4]+1
														RadioState4[1] = True
													EndIf		
												Case 158
													If MTFtimer = 0 Then 
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\franklin1.ogg"))
														RadioState[4] = RadioState[4]+1
														RadioState[2] = True
													EndIf
												Case 200
													If Not RadioState4[3] Then
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\Chatter4.ogg"))
														RadioState[4] = RadioState[4]+1
														RadioState4[3] = True
													EndIf		
												Case 260
													If Not RadioState4[4] Then
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\SCP\035\RadioHelp1.ogg"))
														RadioState[4] = RadioState[4]+1
														RadioState4[4] = True
													EndIf		
												Case 300
													If Not RadioState4[5] Then
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\Chatter1.ogg"))	
														RadioState[4] = RadioState[4]+1	
														RadioState4[5] = True
													EndIf		
												Case 350
													If Not RadioState4[6] Then
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\franklin2.ogg"))
														RadioState[4] = RadioState[4]+1
														RadioState4[6] = True
													EndIf		
												Case 400
													If Not RadioState4[7] Then
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\SCP\035\RadioHelp2.ogg"))
														RadioState[4] = RadioState[4]+1
														RadioState4[7] = True
													EndIf		
												Case 450
													If Not RadioState4[8] Then
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\franklin3.ogg"))	
														RadioState[4] = RadioState[4]+1		
														RadioState4[8] = True
													EndIf		
												Case 600
													If Not RadioState4[2] Then
														RadioCHN[4] = PlaySound_Strict(LoadTempSound("SFX\radio\franklin4.ogg"))	
														RadioState[4] = RadioState[4]+1	
														RadioState4[2] = True
													EndIf		
											End Select
										EndIf
									EndIf
								Case 5
									ResumeChannel(RadioCHN[5])
									If ChannelPlaying(RadioCHN[5]) = False Then RadioCHN[5] = PlaySound_Strict(RadioStatic)
							End Select 
							
							x=x+66
							y=y+419
							
							Color (30,30,30)
							
							If SelectedItem\state <= 100 Then
								For i = 0 To 4
									Rect(x, y+8*i, 43 - i * 6, 4, Ceil(SelectedItem\state / 20.0) > 4 - i )
								Next
							EndIf	
							
							If SelectedItem\itemtemplate\tempname = "veryfineradio" Then
								ResumeChannel(RadioCHN[0])
								If ChannelPlaying(RadioCHN[0]) = False Then RadioCHN[0] = PlaySound_Strict(RadioStatic)
								RadioState[6]=RadioState[6] + FPSfactor
								temp = Mid(Str(AccessCode[0]),RadioState[8]+1,1)
								If RadioState[6]-FPSfactor =< RadioState[7]*50 And RadioState[6]>RadioState[7]*50 Then
									PlaySound_Strict(RadioBuzz)
									RadioState[7]=RadioState[7]+1
									If RadioState[7]=>temp Then
										RadioState[7]=0
										RadioState[6]=-100
										RadioState[8]=RadioState[8]+1
										If RadioState[8]=4 Then RadioState[8]=0 : RadioState[6]=-200
									EndIf
								EndIf
							Else
								
								Local mouseZspeed_var# = MouseZSpeed()
								
								If mouseZspeed_var# <> 0 Then
									PlaySound_Strict RadioSquelch
									If RadioCHN[Int(SelectedItem\state2)] <> 0 Then PauseChannel(RadioCHN[Int(SelectedItem\state2)])
									If mouseZspeed_var# > 0 Then
										SelectedItem\state2 = SelectedItem\state2 + 1
										If SelectedItem\state2 >= 5 Then
											SelectedItem\state2 = 0
										EndIf
									Else
										SelectedItem\state2 = SelectedItem\state2 - 1
										If SelectedItem\state2 < 0 Then
											SelectedItem\state2 = 4
										EndIf
									EndIf
									If RadioCHN[SelectedItem\state2]<>0 Then ResumeChannel(RadioCHN[SelectedItem\state2])
								EndIf
							EndIf
							SetFont fo\Font[Font_Default]
						EndIf
					EndIf
					;[End Block]
				Case "navigator", "nav"
					;[Block]
					If SelectedItem\state <= 100 Then SelectedItem\state = Max(0, SelectedItem\state - FPSfactor * 0.005)
					
					If PlayerRoom\RoomTemplate\Name <> "pocketdimension" Then
						If SelectedItem\state > 0.0 And SelectedItem\state <= 20.0 Then
							UpdateBatteryTimer()
							If BatMsgTimer >= 70.0 * 1.0 Then
								If (Not ChannelPlaying(LowBatteryCHN[0])) Then LowBatteryCHN[0] = PlaySound_Strict(LowBatterySFX[0])
							EndIf
						EndIf
					EndIf
					;[End Block]
;! ~ Consumable - Items
				Case "red_candy","blue_candy","yellow_candy"
					;[Block]
					If (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet" Lor Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask") Lor (Not mpl\HasNTFGasmask) Then
						StaminaEffect = 0.9
						StaminaEffectTimer = 200
						If SelectedItem\itemtemplate\name = "red_candy" Then
							CreateMsg(GetLocalString("Items","candy_1"))
						ElseIf SelectedItem\itemtemplate\name = "blue_candy" Then
							CreateMsg(GetLocalString("Items","candy_2"))
						Else
							CreateMsg(GetLocalString("Items","candy_3"))
						EndIf
						RemoveItem(SelectedItem)
					Else
						CreateMsg(GetLocalString("Items","cant_scp1079"))
					EndIf
					;[End Block]
				Case "veryfinefirstaid"
					;[Block]
					If (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat") Lor (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask") Then
						;If (Not I_402\Using) Then
							Select Rand(5)
								Case 1
									DamageSPPlayer(80, True)
									CreateMsg(GetLocalString("Items", "strangebottle_1"))
								Case 2
									HealSPPlayer(100)
									;I_1079\Foam = 0
									CreateMsg(GetLocalString("Items", "strangebottle_2"))
								Case 3
									HealSPPlayer(50)
									CreateMsg(GetLocalString("Items", "strangebottle_3"))
								Case 4
									BlurTimer = 10000
									HealSPPlayer(25)
									CreateMsg(GetLocalString("Items", "bluefirstaid_3"))
								Case 5
									BlinkTimer = -10
									Local roomname$ = PlayerRoom\RoomTemplate\Name
									Local r.Rooms
									For r.Rooms = Each Rooms
										If r\RoomTemplate\Name = "pocketdimension" Then
											PositionEntity(Collider, EntityX(r\obj),0.8,EntityZ(r\obj))		
											ResetEntity Collider									
											UpdateDoors()
											UpdateRooms()
											PlaySound_Strict(Use914SFX)
											DropSpeed = 0
											Curr106\State[0] = -2500
											Exit
										EndIf
									Next
									CreateMsg(GetLocalString("Items", "strangebottle_4"))
							End Select
;						Else
;							CreateMsg(Chr(34)+GetLocalString("Singleplayer","i_cant")+Chr(34))
;							Return
;							SelectedItem = Null
;						EndIf
						
						RemoveItem(SelectedItem)
					Else
						CreateMsg(GetLocalString("Singleplayer","cant_firstaid"))
					EndIf
					;[End Block]
				Case "firstaid", "finefirstaid", "firstaid2"
					;[Block]
					If psp\Health = 100 Then
						;I_1079\Foam = 0
						CreateMsg(GetLocalString("Items", "firstaid_noneed"))
						SelectedItem = Null
					Else
						If (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat") Then
							;If (Not I_402\Using) Then
								CurrSpeed = CurveValue(0, CurrSpeed, 5.0)
								Crouch = True
								
								SelectedItem\state = Min(SelectedItem\state+(FPSfactor/5.0),100)			
								
								If SelectedItem\state = 100 Then
									If SelectedItem\itemtemplate\tempname = "finefirstaid" Then
										HealSPPlayer(45)
										If psp\Health = 100 Then
											CreateMsg(GetLocalString("Items", "finefirstaid_1"))
										ElseIf psp\Health > 70 Then
											CreateMsg(GetLocalString("Items", "finefirstaid_2"))
										Else
											CreateMsg(GetLocalString("Items", "finefirstaid_3"))
										EndIf
										RemoveItem(SelectedItem)
									Else
										HealSPPlayer(30)
										CreateMsg(GetLocalString("Items", "firstaid_1"))
										
										If SelectedItem\itemtemplate\tempname = "firstaid2" Then 
											Select Rand(6)
												Case 1
													SuperMan = True
													CreateMsg(GetLocalString("Items", "bluefirstaid_1"))
												Case 2
													InvertMouse = (Not InvertMouse)
													CreateMsg(GetLocalString("Items", "bluefirstaid_2"))
												Case 3
													BlurTimer = 5000
													CreateMsg(GetLocalString("Items", "bluefirstaid_3"))
												Case 4
													BlinkEffect = 0.6
													BlinkEffectTimer = Rand(20,30)
												Case 5
													HealSPPlayer(100)
													CreateMsg(GetLocalString("Items", "bluefirstaid_4"))
												Case 6
													CreateMsg(GetLocalString("Items", "bluefirstaid_5"))
													DamageSPPlayer(70, True)
											End Select
										EndIf
										RemoveItem(SelectedItem)
									EndIf							
								EndIf
;							Else
;								CreateMsg(Chr(34)+GetLocalString("Singleplayer","i_cant")+Chr(34))
;								Return
;								SelectedItem = Null
;							EndIf
						Else
							CreateMsg(GetLocalString("Singleplayer","cant_firstaid_with_hazmat"))
						EndIf
					EndIf
					;[End Block]
				Case "cup"
					;[Block]
					If (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet") Lor (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask") Lor (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat")Lor (Not mpl\HasNTFGasmask) Lor(Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
						;If (Not I_402\Using) Then
							
							If IsVaneCoinDropped Then
								If opt\MusicVol > 0 Then
									PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Vane_NTF.ogg"))
									GiveAchievement(AchvVane)
								EndIf
							EndIf
							
							SelectedItem\name = Trim(Lower(SelectedItem\name))
							If Left(SelectedItem\name, Min(6,Len(SelectedItem\name))) = "cup of" Then
								SelectedItem\name = Right(SelectedItem\name, Len(SelectedItem\name)-7)
							ElseIf Left(SelectedItem\name, Min(8,Len(SelectedItem\name))) = "a cup of" 
								SelectedItem\name = Right(SelectedItem\name, Len(SelectedItem\name)-9)
							EndIf
							
							x2 = (SelectedItem\state+1.0)
							
							Local iniStr$ = Data294
							
							Local loc% = GetINISectionLocation(iniStr, SelectedItem\name)
							
							strtemp = GetINIString2(iniStr, loc, "message")
							
							If IsVaneCoinDropped Then
								CreateMsg("DAMN, THAT SOUNDS NICE!",70*10,True,Rand(100,255),Rand(100,255),Rand(100,255))
							Else
								If strtemp <> "" Then
									If strtemp <> "jorge" Then
										CreateMsg(strtemp)
									Else
										CreateMsg(strtemp,70*6,True,255,216,0)
									EndIf
								EndIf
							EndIf
							
							If GetINIInt2(iniStr, loc, "lethal") Lor GetINIInt2(iniStr, loc, "deathtimer") Then 
								m_msg\DeathTxt = GetINIString2(iniStr, loc, "deathmessage")
								If GetINIInt2(iniStr, loc, "lethal") Then Kill()
							EndIf
							BlurTimer = GetINIInt2(iniStr, loc, "blur")*70
							If VomitTimer = 0 Then VomitTimer = GetINIInt2(iniStr, loc, "vomit")
							CameraShakeTimer = GetINIString2(iniStr, loc, "camerashake")
							DamageSPPlayer(GetINIInt2(iniStr, loc, "damage") * 25.0, True)
							strtemp =  GetINIString2(iniStr, loc, "sound")
							If strtemp<>"" Then
								PlaySound_Strict LoadTempSound(strtemp)
							EndIf
							;If GetINIInt2(iniStr, loc, "stomachache") Then I_1025\State[3]=1
							
							DeathTimer=GetINIInt2(iniStr, loc, "deathtimer")*70
							
							If GetINIInt2(iniStr, loc, "cola") Then I_207\Factor = 1
							
							;I_1079\Foam = Max(I_1079\Foam + GetINIInt2(iniStr, loc, "bubble foam"),0)
							
							BlinkEffect = Float(GetINIString2(iniStr, loc, "blink effect", 1.0))*x2
							BlinkEffectTimer = Float(GetINIString2(iniStr, loc, "blink effect timer", 1.0))*x2
							
							StaminaEffect = Float(GetINIString2(iniStr, loc, "stamina effect", 1.0))*x2
							StaminaEffectTimer = Float(GetINIString2(iniStr, loc, "stamina effect timer", 1.0))*x2
							
							strtemp = GetINIString2(iniStr, loc, "refusemessage")
							If strtemp <> "" Then
								CreateMsg(strtemp)
							Else
								it.Items = CreateItem(GetLocalString("Item Names","cup_empty"), "emptycup", 0,0,0)
								it\Picked = True
								For i = 0 To MaxItemAmount-1
									If Inventory[i]=SelectedItem Then Inventory[i] = it : Exit
								Next					
								EntityType (it\collider, HIT_ITEM)
								
								RemoveItem(SelectedItem)						
							EndIf
							
							SelectedItem = Null
;						Else
;							CreateMsg(Chr(34)+GetLocalString("Singleplayer","i_cant")+Chr(34))
;							Return
;							SelectedItem = Null
;						EndIf
					Else
						CreateMsg(GetLocalString("Items","cant_cup"))
					EndIf
					;[End Block]
				Case "syringe","finesyringe","veryfinesyringe"
					;[Block]
					If (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat") Lor (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
						;If (Not I_402\Using) Then
							If SelectedItem\itemtemplate\name = "syringe" Then
								HealTimer = 30
								StaminaEffect = 0.5
								StaminaEffectTimer = 20
							ElseIf SelectedItem\itemtemplate\name = "finesyringe" Then
								HealTimer = Rnd(20, 40)
								StaminaEffect = Rnd(0.5, 0.8)
								StaminaEffectTimer = Rnd(20, 30)
							Else
								Select Rand(3)
									Case 1
										HealTimer = Rnd(40, 60)
										StaminaEffect = 0.1
										StaminaEffectTimer = 30
										CreateMsg(GetLocalString("Items", "syringe_huge"))
									Case 2
										SuperMan = True
										CreateMsg(GetLocalString("Items", "syringe_super"))
									Case 3
										VomitTimer = 30
										CreateMsg(GetLocalString("Items", "syringe_vomit"))
								End Select
							EndIf
							
							CreateMsg(GetLocalString("Items", "syringe_slight"))
							
							RemoveItem(SelectedItem)
;						Else
;							CreateMsg(Chr(34)+GetLocalString("Singleplayer","i_cant")+Chr(34))
;							Return
;							SelectedItem = Null
;						EndIf
					Else
						CreateMsg(GetLocalString("Items","cant_syringe"))
					EndIf
					;[End Block]
				Case "pill"
					;[Block]
					If (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet") Lor (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask") Lor (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat")Lor (Not mpl\HasNTFGasmask) Lor(Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
						CreateMsg(GetLocalString("Items", "pill"))
						
						RemoveItem(SelectedItem)
						SelectedItem = Null
					Else
						CreateMsg(GetLocalString("Items", "cant_pill"))
					EndIf	
					;[End Block]
				Case "beer"
					;[Block]
					If (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet") Lor (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask") Lor (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat")Lor (Not mpl\HasNTFGasmask) Lor(Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
						PlaySound_Strict(LoadTempSound("SFX\SCP\294\ew"+Rand(1,2)+".ogg"))
						RemoveItem(SelectedItem)
						CreateMsg(GetLocalString("Items","beer_use"))
					Else
						CreateMsg(GetLocalString("Items","cant_beer"))
					EndIf
					SelectedItem = Null
					;[End Block]
;! ~ SCP - Items
					
;				Case "scp016"
;					PlaySound_Strict LoadTempSound("SFX\SCP\016\DishBreak.ogg")
;					
;					If (Not wbl\Hazmat) Then
;						GiveAchievement(Achv016)
;						CreateMsg(GetLocalString("Items", "scp016_1"))
;						DamageSPPlayer(1,True)
;						I_016\Timer = 10
;						RemoveItem(SelectedItem)
;					Else
;						CreateMsg(GetLocalString("Items", "scp016_2"))
;					EndIf
;					;[End Block]
				Case "scp109"
					;[Block]
					If (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet") Lor (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask") Lor (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat")Lor (Not mpl\HasNTFGasmask) Lor(Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
						;If (Not I_402\Using) Then
							If I_109\Sound[0] = 0 Then
								I_109\Sound[0] = LoadSound_Strict("SFX\SCP\109\ahh.ogg")
							EndIf
							
							GiveAchievement(Achv109)
							
							I_109\Used = I_109\Used + 1
							I_109\Timer = 70*10
							
							Stamina = Stamina + Rnd(60)
							psp\Health = Max(0, psp\Health - Rnd(1, 2))
							
							If I_109\Used < 5 Then
								PlaySound_Strict I_109\Sound[0]
								
								If Rand(3) = 1 Then
									CreateMsg(GetLocalString("Items", "scp109_1"))
								Else
									If Rand(2) = 1 Then
										CreateMsg(GetLocalString("Items", "scp109_2"))
									Else
										CreateMsg(GetLocalString("Items", "scp109_3"))
									EndIf
								EndIf
							Else
								BlurTimer = 10000
								
								I_109\Vomit = True
								VomitTimer = -4
								
								CreateMsg(GetLocalString("Items", "scp109_vomit"))
							EndIf
							SelectedItem\state = 0
							SelectedItem = Null
;						Else
;							CreateMsg(Chr(34)+GetLocalString("Singleplayer","i_cant")+Chr(34))
;							Return
;							SelectedItem = Null
;						EndIf
					Else
						CreateMsg(GetLocalString("Items","cant_scp109"))
					EndIf
					SelectedItem = Null
					;[End Block]
				Case "scp198"
					;[Block]
					If (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet") Lor (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask") Lor (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat")Lor (Not mpl\HasNTFGasmask) Lor(Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
						PlaySound_Strict(LoadTempSound("SFX\SCP\294\ew"+Rand(1,2)+".ogg"))
						CreateMsg(GetLocalString("Items", "scp198_2"))
						If Rand(5) = 1 Then
							I_198\Vomit = True
							VomitTimer = -5
							If I_1033RU\HP = 0
								HealSPPlayer(I_198\Injuries)
							EndIf
							I_198\Injuries = 0
							I_198\DeathTimer = 1
						Else
							If I_1033RU\HP = 0
								HealSPPlayer(I_198\Injuries)
							EndIf
							I_198\Injuries = 0
							I_198\DeathTimer = 1
						EndIf
						SelectedItem = Null
					EndIf
					;[End Block]
				Case "scp207"
					;[Block]
					If (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet") Lor (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask") Lor (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat")Lor (Not mpl\HasNTFGasmask) Lor(Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
						CreateMsg(GetLocalString("Items","cant_scp207"))
						SelectedItem = Null
						Return
					Else
						;If (Not I_402\Using) Then
							PlaySound_Strict LoadTempSound("SFX\SCP\109\Ahh.ogg")
							
							I_207\Factor = I_207\Factor + 1
							
							I_207\Limit = I_207\Limit + 1
							
							Select I_207\Factor
								Case 1
									I_207\Timer = 0
									I_207\Timer = 1.0
								Case 4, 5
									InfiniteStamina = True
								Case 6
									SuperMan = True
							End Select
							
							If I_207\Factor > 1 Then
								I_207\DeathTimer = 1.0
							EndIf
							
							BlinkEffect = 0.8
							BlinkEffectTimer = 150
							StaminaEffect = 0.8
							StaminaEffectTimer = 10
							
							;I_357\Timer = 0
							;I_402\Timer = 0
							;I_1079\Take = 0
							;I_1079\Foam = 0
							;I_1079\Trigger = 0
							;I_016\Timer = 0
							;I_059\Timer = 0
							DeathTimer = 0
							I_008\Timer = 0
							Stamina = 100
							psp\Health = 110
;							For i = 0 To 5
;								I_1025\State[i]=0
;							Next
							
							For e.Events = Each Events
								If e\EventName = "cont_009" Then e\EventState[0] = 0.0 : e\EventState[2] = 0.0
							Next
							
							Select Rand(1, 4)
								Case 1
									CreateMsg(GetLocalString("Items", "scp207_1"))
								Case 2
									CreateMsg(GetLocalString("Items", "scp207_2"))
								Case 3
									CreateMsg(GetLocalString("Items", "scp207_3"))
								Case 4
									CreateMsg(GetLocalString("Items", "scp207_4"))
							End Select
							
							SelectedItem\state = 0
							
							If I_207\Limit >= 4 Then 
								RemoveItem(SelectedItem)
								If it = Null Then
									it = CreateItem(GetLocalString("Item Names","scp_207_empty"),"scp207empty",EntityX(Collider),EntityY(Camera,True),EntityX(Collider))
									EntityType it\collider, HIT_ITEM
								EndIf
								I_207\Limit = 0
							EndIf
;						Else
;							CreateMsg(Chr(34)+GetLocalString("Singleplayer","i_cant")+Chr(34))
;							Return
;							SelectedItem = Null
;						EndIf
					EndIf
					SelectedItem = Null
					;[End Block]
;				Case "scp402"
;					;[Block]
;					If I_402\Timer >= 40
;						I_402\Using = 1
;						CreateMsg(GetLocalString("Items","scp402_1"))
;						SelectedItem = Null
;						Return
;					EndIf
;					
;					If (Not wbl\Hazmat Lor wbl\GasMask Lor mpl\HasNTFGasmask) Then
;						If I_402\Using = 1 Then
;							CreateMsg(GetLocalString("Items","scp402_off"))
;							I_402\Using = 0
;						Else
;							GiveAchievement(Achv402)
;							CreateMsg(GetLocalString("Items","scp402_on"))
;							I_402\Using = 1
;						EndIf
;						SelectedItem = Null
;					Else
;						CreateMsg(GetLocalString("Singleplayer","cant_scp402"))
;						SelectedItem = Null
;					EndIf
;					;[End Block]
				Case "420"
					;[Block]
					If (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet") Lor (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask") Lor (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat")Lor (Not mpl\HasNTFGasmask) Lor(Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
						If I_714\Using=1 Then
							CreateMsg(Chr(34) + GetLocalString("Items", "scp420_no") + Chr(34))
						Else
							CreateMsg(Chr(34) + GetLocalString("Items", "scp420_1") + Chr(34))
							HealSPPlayer(80)
							BlurTimer = 500
							GiveAchievement(Achv420)
							If opt\MusicVol > 0 Then
								PlaySound_Strict LoadTempSound("SFX\Music\SCPs\420-J_"+Rand(1,2)+".ogg")
							EndIf
						EndIf
						RemoveItem(SelectedItem)
					Else
						CreateMsg(GetLocalString("Items", "cant_scp420"))
					EndIf
					;[End Block]
				Case "420s"
					;[Block]
					If (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet") Lor (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask") Lor (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat")Lor (Not mpl\HasNTFGasmask) Lor(Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
						If I_714\Using=1 Then
							CreateMsg(Chr(34) + GetLocalString("Items", "scp420_no") + Chr(34))
						Else
							m_msg\DeathTxt = GetLocalStringR("Singleplayer", "scp420_death_1",Designation)
							m_msg\DeathTxt = m_msg\DeathTxt+GetLocalString("Singleplayer", "scp420_death_2")
							m_msg\DeathTxt = m_msg\DeathTxt+GetLocalString("Singleplayer", "scp420_death_3")
							CreateMsg(Chr(34) + GetLocalString("Items", "scp420_2") + Chr(34))
							KillTimer = -1						
						EndIf
						RemoveItem(SelectedItem)
					Else
						CreateMsg(GetLocalString("Items", "cant_scp420"))
					EndIf
					;[End Block]
				Case "scp427"
					;[Block]
					If I_427\Using > 0 Then
						CreateMsg(GetLocalString("Items", "scp427_off"))
						I_427\Using = False
					Else
						GiveAchievement(Achv427)
						CreateMsg(GetLocalString("Items", "scp427_on"))
						I_427\Using = True
					EndIf
					SelectedItem = Null
					;[End Block]
				Case "scp500"
					;[Block]					
					If ItemAmount < MaxItemAmount Then
						For n% = 0 To ItemAmount+0
							If Inventory[n] = Null Then
								If ItemAmount > MaxItemAmount Then
									CreateMsg(GetLocalString("Items", "cannot_carry"))
								Else
									Inventory[n] = CreateItem("SCP-500-01", "scp500pill", 1, 1, 1)
									Inventory[n]\Picked = True
									Inventory[n]\Dropped = -1
									Inventory[n]\itemtemplate\found=True
									I_500\Limit = I_500\Limit + 1
									HideEntity Inventory[n]\collider
									EntityType (Inventory[n]\collider, HIT_ITEM)
									EntityParent(Inventory[n]\collider, 0)
									CreateMsg(GetLocalString("Items", "scp500_take"))
								EndIf										
							EndIf	
						Next
						If I_500\Limit >= 3 Then 
							RemoveItem(SelectedItem)
							I_500\Limit = 0
						EndIf
					Else
						CreateMsg(GetLocalString("Items", "cannot_carry"))				
					EndIf																																										
					;[End Block]
				Case "scp500pill"
					;[Block]
					If (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet") Lor (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask") Lor (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat")Lor (Not mpl\HasNTFGasmask) Lor(Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
						If psp\Health < 100 And I_008\Timer > 0 Then
							CreateMsg(GetLocalString("Items", "scp500_1"))
						ElseIf I_008\Timer > 0 Then
							CreateMsg(GetLocalString("Items", "scp500_2"))
						ElseIf I_409\Timer > 0.0 Then
							CreateMsg(GetLocalString("Items", "scp500_5"))
							I_409\Revert = True
						Else
							CreateMsg(GetLocalString("Items", "scp500_3"))
						EndIf
;						If I_357\Timer > 0 Then
;							CreateMsg(GetLocalString("Items", "scp500_4"))
;						EndIf
						
						GiveAchievement(Achv500)
						
						DeathTimer = 0
						HealSPPlayer(100)
						Sanity = 0
						
						I_008\Timer = 0
						;I_016\Timer = 0
						;I_059\Timer = 0
						I_109\Timer = 0
						I_109\Used = 0
						I_109\Vomit = False
						I_109\VomitTimer = 0
						I_198\Timer = 0
						I_198\DeathTimer = 0
						I_198\Injuries = 0
						I_198\Vomit = False
						I_198\VomitTimer = 0
						I_207\Timer = 0
						I_207\DeathTimer = 0
						I_207\Factor = 0
						;I_357\Timer = 0
						;I_402\Timer = 0
						I_409\Timer = 0
						;I_1079\Foam = 0
						;I_1079\Trigger = 0
						
						Stamina = 100
;						For i = 0 To 5
;							I_1025\State[i]=0
;						Next
						
						RemoveItem(SelectedItem)
						SelectedItem = Null
					Else
						CreateMsg(GetLocalString("Items","cant_pill"))
					EndIf
					;[End Block]
				Case "scp500death"
					;[Block]
					If (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet") Lor (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask") Lor (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat")Lor (Not mpl\HasNTFGasmask) Lor(Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
						CreateMsg(GetLocalString("Items", "pill"))
						
						If I_427\Timer < 70*360 Then
							I_427\Timer = 70*360
						EndIf
						
						RemoveItem(SelectedItem)
						SelectedItem = Null
					Else
						CreateMsg(GetLocalString("Items", "cant_pill"))
					EndIf
					;[End Block]
				Case "scp513"
					;[Block]
					PlaySound_Strict LoadTempSound("SFX\SCP\513\Bell1.ogg")
					
					If Curr5131 = Null
						Curr5131 = CreateNPC(NPC_SCP_513, 0,0,0)
					EndIf	
					SelectedItem = Null
					;[End Block]
				Case "scp714"
					;[Block]
					If I_714\Using=1 Then
						CreateMsg(GetLocalString("Items", "scp714_off"))
						I_714\Using = False
					Else
						GiveAchievement(Achv714)
						CreateMsg(GetLocalString("Items", "scp714_on"))
						I_714\Using = True
					EndIf
					SelectedItem = Null	
					;[End Block]
;				Case "scp1025"
;					;[Block]
;					GiveAchievement(Achv1025) 
;					If SelectedItem\itemtemplate\img=0 Then
;						SelectedItem\state = Rand(0,5)
;					EndIf
;					
;					If (Not I_714\Using) Then I_1025\State[SelectedItem\state]=Max(1,I_1025\State[SelectedItem\state])
;					;[End Block]
				Case "scp1033ru", "super1033ru"
					;[Block]
					If I_1033RU\Using > 0 Then
						CreateMsg(GetLocalString("Items", "scp1033ru_off"))
						I_1033RU\Using = False
					Else
						GiveAchievement(Achv1033RU)
						CreateMsg(GetLocalString("Items", "scp1033ru_on"))
						If SelectedItem\itemtemplate\tempname = "scp1033ru" Then
							I_1033RU\Using = 1
						Else
							I_1033RU\Using = 2
						EndIf
					EndIf
					SelectedItem = Null		
					;[End Block]
;				Case "scp1079"
;					;[Block]					
;					If ItemAmount < MaxItemAmount Then
;						For n% = 0 To ItemAmount
;							If Inventory[n] = Null Then
;								If ItemAmount > MaxItemAmount Then
;									CreateMsg(GetLocalString("Items", "cannot_carry"))
;								Else
;									Inventory[n] = CreateItem("SCP-1079-01", "scp1079sweet", 1, 1, 1)
;									Inventory[n]\Picked = True
;									Inventory[n]\Dropped = -1
;									Inventory[n]\itemtemplate\found=True
;									I_1079\Limit = I_1079\Limit + 1
;									HideEntity Inventory[n]\collider
;									EntityType (Inventory[n]\collider, HIT_ITEM)
;									EntityParent(Inventory[n]\collider, 0)
;									CreateMsg(GetLocalString("Items", "scp1079_took"))
;								EndIf										
;							EndIf	
;						Next
;						If I_1079\Limit >= 4 Then 
;							RemoveItem(SelectedItem)
;							I_1079\Limit = 0
;						EndIf
;					Else
;						CreateMsg(GetLocalString("Items", "cannot_carry"))
;					EndIf																																									
;					;[End Block]
;				Case "scp1079sweet"
;					;[Block]
;					If (Not wbl\Helmet Lor wbl\GasMask Lor mpl\HasNTFGasmask) Then
;						CreateMsg(GetLocalString("Items", "scp1079_1"))
;						
;						I_1079\Take = I_1079\Take + 1
;						If I_1033RU\HP = 0
;							DamageSPPlayer(Rand(20,30),True)
;						Else
;							Damage1033RU(Rand(5))
;						EndIf
;						
;						PlaySound_Strict(SizzSFX[Rand(0,1)])
;						
;						GiveAchievement(Achv1079)
;						
;						Local tempchn%
;						
;						RemoveItem(SelectedItem)
;						pvt = CreatePivot()
;						PositionEntity pvt, EntityX(Collider)+Rnd(-0.05,0.05),EntityY(Collider)-0.05,EntityZ(Collider)+Rnd(-0.05,0.05)
;						TurnEntity pvt, 90, 0, 0
;						EntityPick(pvt,0.3)
;						Local de.Decals = CreateDecal(DECAL_1079, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
;						de\Size = Rnd(0.1,0.2) : EntityAlpha(de\obj, 0.7) : ScaleSprite de\obj, de\Size, de\Size
;						de.Decals = CreateDecal(DECAL_1079, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
;						de\Size = Rnd(0.06,0.1) : EntityAlpha(de\obj, 0.7) : ScaleSprite de\obj, de\Size, de\Size
;						de.Decals = CreateDecal(DECAL_1079, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
;						de\Size = Rnd(0.2,0.25) : EntityAlpha(de\obj, 0.7) : ScaleSprite de\obj, de\Size, de\Size
;						de.Decals = CreateDecal(DECAL_1079, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
;						de\Size = Rnd(0.3,0.31) : EntityAlpha(de\obj, 0.7) : ScaleSprite de\obj, de\Size, de\Size
;						ChannelVolume tempchn, Rnd(0.0,0.8)*opt\SFXVolume
;						ChannelPitch tempchn, Rand(20000,30000)
;						
;						FreeEntity pvt
;					Else
;						CreateMsg(GetLocalString("Items", "cant_scp1079"))
;					EndIf
;					
;					If I_1079\Take >= 4 Then
;						I_1079\Trigger = 1													
;						Kill()
;						m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp1079_death_1",Designation)
;						m_msg\DeathTxt = m_msg\DeathTxt+GetLocalString("Singleplayer","scp1079_death_2")
;						m_msg\DeathTxt = m_msg\DeathTxt+GetLocalString("Singleplayer","scp1079_death_3")
;					EndIf
;					;[End Block]
;				Case "scp1102ru"
;					;[Block]
;					If (Not IsPlayerOutside()) Then
;						CurrSpeed = CurveValue(0.0, CurrSpeed, 5.0)
;						
;						SelectedItem\state = Min(SelectedItem\state + (FPSfactor/2), 100.0)
;						
;						If SelectedItem\state = 100.0 Then
;							If I_1102RU\IsInside = 0 Then
;								If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
;								CreateMsg(GetLocalString("Items", "scp1102ru_1"))
;								I_1102RU\IsInside = 1
;								PlaySound_Strict I_1102RU\Sound[0]
;								
;								GiveAchievement(Achv1102RU)
;								
;								For r = Each Rooms
;									If r\RoomTemplate\Name = "area_1102_ru" Then
;										PlayerRoom = r
;										TeleportEntity(Collider, r\x,r\y,r\z)
;										Exit
;									EndIf
;								Next
;								DropSpeed = 0
;								FallTimer = 0
;								psp\NoMove = False
;								psp\NoRotation = False
;								IsZombie = False
;								FlushKeys()
;								FlushMouse()
;								FlushJoy()
;								Return
;								
;							EndIf
;							
;							RemoveItem(SelectedItem)
;						EndIf
;					Else
;						CreateMsg(GetLocalString("Items", "scp1102ru_jammed"))
;					EndIf
;					;[End Block]
				Default
					;[Block]
					If SelectedItem\invSlots>0 Then
						DoubleClick = 0
						MouseHit1 = 0
						MouseDown1 = 0
						LastMouseHit1 = 0
						OtherOpen = SelectedItem
						SelectedItem = Null
					EndIf
					;[End Block]
			End Select
			
			If SelectedItem <> Null Then
				If SelectedItem\itemtemplate\img <> 0
					Local IN$ = SelectedItem\itemtemplate\tempname
					If IN$ = "paper" Lor IN$ = "badge" Lor IN$ = "oldpaper" Lor IN$ = "ticket" Then
						Local a_it.Items
						For a_it.Items = Each Items
							If a_it <> SelectedItem
								Local IN2$ = a_it\itemtemplate\tempname
								If IN2$ = "paper" Lor IN2$ = "badge" Lor IN2$ = "oldpaper" Lor IN2$ = "ticket" Then
									If a_it\itemtemplate\img<>0
										If a_it\itemtemplate\img <> SelectedItem\itemtemplate\img
											FreeImage(a_it\itemtemplate\img)
											a_it\itemtemplate\img = 0
										EndIf
									EndIf
								EndIf
							EndIf
						Next
					EndIf
				EndIf			
			EndIf
			
			If MouseHit2 Then
				EntityAlpha Dark, 0.0
				
				IN$ = SelectedItem\itemtemplate\tempname
;				If IN$ = "scp1025" Then
;					If SelectedItem\itemtemplate\img<>0 Then FreeImage(SelectedItem\itemtemplate\img)
;					SelectedItem\itemtemplate\img=0
;				EndIf
				If IN$="nvg" Lor IN$="nvg2" Lor IN$="nvg3" Lor IN$ = "scramble" Lor IN$ = "scramble2" Then
					SelectedItem\state2 = 0
				EndIf
				If IN$="hazmat" Lor IN$="hazmat2" Lor IN$="hazmat3"
					SelectedItem\state = 0
				EndIf
				If IN$="gasmask" Lor IN$="gasmask2" Lor IN$="gasmask3"
					SelectedItem\state = 0
				EndIf
				If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
				SelectedItem = Null
			EndIf
		EndIf		
	EndIf
	
	If SelectedItem = Null Then
		For i = 0 To 6
			If RadioCHN[i] <> 0 Then
				If ChannelPlaying(RadioCHN[i]) Then PauseChannel(RadioCHN[i])
			EndIf
		Next
	EndIf
	
	UpdateTasks()
	
	If PrevInvOpen And (Not InvOpen) Then MoveMouse Viewport_Center_X, Viewport_Center_Y
	
	CatchErrors("UpdateGUI")
End Function

Global ShowingAimCross%

Function DrawScopeDots()
	Local g.Guns, alive% = False
	
	If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
		If Players[mp_I\PlayerID]\CurrHP > 0 Then alive = True
	Else
		If IsSPPlayerAlive() Then alive = True
	EndIf
	
	For g = Each Guns
		
		If g_I\HoldingGun = g\ID Then
			If ReadyToShowDot And alive Then
				If g\HasAttachments[ATT_SPECIAL_SCOPE] Then
					If g\name = "p90" Then
						DrawImage AimCrossIMG[1],opt\GraphicWidth/2,opt\GraphicHeight/2
					Else
						DrawImage AimCrossIMG[5],opt\GraphicWidth/2,opt\GraphicHeight/2
					EndIf
				ElseIf g\HasAttachments[ATT_RED_DOT] Then
					DrawImage AimCrossIMG[2],opt\GraphicWidth/2,opt\GraphicHeight/2
				ElseIf g\HasAttachments[ATT_EOTECH] Then
					DrawImage AimCrossIMG[3],opt\GraphicWidth/2,opt\GraphicHeight/2
				ElseIf g\HasAttachments[ATT_ACOG_SCOPE] Then
					DrawImage AimCrossIMG[4],opt\GraphicWidth/2,opt\GraphicHeight/2
				EndIf
			EndIf
		EndIf
	Next
	
End Function

Function DrawGunsInHUD()
	Local isMultiplayer% = (gopt\GameMode = GAMEMODE_MULTIPLAYER)
	Local g.Guns
	Local x# = 50, x2# = 150
	Local y# = 50
	Local width#=64,height#=64
	
	Local width2%
	Local i%
	width# = 204
	height# = 20
	
	x# = opt\GraphicWidth - 60
	y# = opt\GraphicHeight - 55
	
	Local pAmmo%, pReloadAmmo%
	If isMultiplayer Then
		pAmmo = Players[mp_I\PlayerID]\Ammo[Players[mp_I\PlayerID]\SelectedSlot]
		pReloadAmmo = Players[mp_I\PlayerID]\ReloadAmmo[Players[mp_I\PlayerID]\SelectedSlot]
	Else
		For g = Each Guns
			If g\ID = g_I\HoldingGun Then
				pAmmo = g\CurrAmmo
				pReloadAmmo = g\CurrReloadAmmo
			EndIf
		Next
	EndIf
	
	For g = Each Guns
		
		If ShowingAimCross Then
			DrawImage AimCrossIMG[0],opt\GraphicWidth/2,opt\GraphicHeight/2
		EndIf
		
		If g\ID = g_I\HoldingGun Then
			If (g\GunType <> GUNTYPE_MELEE) Then
				
				If g\MaxJams > 0 Then
					
					Color 255,0,0
					Rect(x - 50 - 1 - 30, y - 80, Float(g\JamAmount * (width - 6) / (g\MaxJams*1.6)), 15, True)
					
					If g\JamAmount < g\MaxJams Then
						Color 255,255,255
						TextWithAlign x - 40, y - 110, GetLocalString("Weapons","heat")
					Else
						Color 255,0,0
						TextWithAlign x - 70, y - 110, GetLocalString("Weapons","overheat")
					EndIf
					Rect(x - 50 - 1 - 30, y - 81, 125, 15 + 2, False)
				EndIf
				
				Color 255,255,255
				
				If g\GunType <> GUNTYPE_HANDGUN And g\GunType <> GUNTYPE_SHOTGUN Then
					If g\FireMode = 0 Then
						DrawImage WeaponFireModeIcons[0], x - 50 - 30, y - 50
						TextWithAlign x - 40, y - 45, GetLocalString("Weapons","auto")
					ElseIf g\FireMode = 1 Then
						DrawImage WeaponFireModeIcons[1], x - 50 - 30, y - 50
						TextWithAlign x - 40, y - 45, GetLocalString("Weapons","semi")
					EndIf
				ElseIf g\GunType = GUNTYPE_SHOTGUN
					If g_I\HoldingGun = GUN_SPAS12 Then
						If g\FireMode = 0 Then
							DrawImage WeaponFireModeIcons[0], x - 50 - 30, y - 50
							TextWithAlign x - 40, y - 45, GetLocalString("Weapons","auto")
						ElseIf g\FireMode = 1 Then
							DrawImage WeaponFireModeIcons[1], x - 50 - 30, y - 50
							TextWithAlign x - 40, y - 45, GetLocalString("Weapons","pump")
						EndIf
					Else
						If g\FireMode = 0 Then
							DrawImage WeaponFireModeIcons[0], x - 50 - 30, y - 50
							TextWithAlign x - 40, y - 45, GetLocalString("Weapons","semi")
						EndIf
					EndIf
				Else
					If g\FireMode = 0 Then
						DrawImage WeaponFireModeIcons[0], x - 50 - 30, y - 50
						TextWithAlign x - 40, y - 45, GetLocalString("Weapons","semi")
					EndIf
				EndIf
				
				If pAmmo > 0 Then
					Color 255,255,255
				Else
					Color 255,0,0
				EndIf
				Rect(x - 50 - 1 - 30, y - 1, 30 + 2, 30 + 2, False)
				DrawImage BulletIcon, x - 50 - 30, y
				
				SetFont fo\Font%[Font_Digital_Medium]
				If pAmmo > g\MaxCurrAmmo/5
					Color 0,255,0
				Else
					Color 255,0,0
				EndIf
				TextWithAlign x, y + 5, pAmmo, 2
				Color 0,255,0
				Text x, y + 5, "/"
				width2% = StringWidth("/")
				If pReloadAmmo > 0
					Color 0,255,0
				Else
					Color 255,0,0
				EndIf
				Text x + width2, y + 5, pReloadAmmo
			EndIf
			Exit
		EndIf
	Next
	
	Color 255,255,255
	
	x = 55
	y = 55
	width = 64
	height = 64
	If mpl\SlotsDisplayTimer > 0.0 Then
		For i = 0 To MaxGunSlots-1
			DrawFrame((x-3)+(128*i),y-3,width+6,height+6)
			If g_I\Weapon_CurrSlot = (i + 1) Then
				Color 0,255,0
				Rect (x-3)+(128*i),y-3,width+6,height+6,True
			EndIf
			If g_I\Weapon_InSlot[i] <> "" Then
				For g = Each Guns
					If g\name = g_I\Weapon_InSlot[i] Then
						DrawImage g\IMG,x+(128*i),y
						Color 255,255,255
						If g_I\Weapon_CurrSlot = (i + 1) Then
							SetFont fo\Font[Font_Default]
							Text(x+(width/2)+(128*i),y+height+10,g\DisplayName,True,False)
						EndIf
						Exit
					EndIf
				Next
			EndIf
		Next
	EndIf
	
	Color 255,255,255
	
End Function

Function DrawMenu()
	CatchErrors("Uncaught (DrawMenu)")
	
	Local x%, y%, width%, height%
	If MenuOpen Then
		If KillTimer >= 0 Then
			CameraProjMode Camera, 0
			CameraProjMode m_I\Cam, 1
			PositionEntity m_I\Cam,0,-1000,0
			ShowEntity m_I\MenuLogo\logo
			ShowEntity m_I\MenuLogo\gradient
			RenderWorld
			CameraProjMode Camera, 1
			CameraProjMode m_I\Cam, 0
			HideEntity m_I\MenuLogo\logo
			HideEntity m_I\MenuLogo\gradient
			
			Color 255, 255, 255
			SetFont fo\Font[Font_Default]
			
			Local difficultystr$
			
			If SelectedDifficulty\Name = "Safe" Then
				Color 120,150,50
				difficultystr$ = GetLocalString("Difficulty", "safe")
			ElseIf SelectedDifficulty\Name = "Euclid" Then
				Color 200,200,0
				difficultystr$ = GetLocalString("Difficulty", "euclid")
			ElseIf SelectedDifficulty\Name = "Keter" Then
				Color 200,0,0
				difficultystr$ = GetLocalString("Difficulty", "keter")
			ElseIf SelectedDifficulty\Name = "Thaumiel" Then
				Color 150,150,150
				difficultystr$ = GetLocalString("Difficulty", "thaumiel")
			ElseIf SelectedDifficulty\Name = "Appolyon" Then
				Color 50,150,255
				difficultystr$ = GetLocalString("Difficulty", "appolyon")
			ElseIf SelectedDifficulty\Name = "Esoteric" Then
				Color 120,50,150
				difficultystr$ = GetLocalString("Difficulty", "esoteric")
			EndIf
			
			Text 20, opt\GraphicHeight-110, GetLocalString("Menu", "difficulty")+": "+difficultystr$
			
			Local gamemodestr$
			
			If gopt\GameMode = GAMEMODE_DEFAULT Then
				gamemodestr$ = GetLocalString("Menu","save_gamemode_r")
				Color 255,255,255
			ElseIf gopt\GameMode = GAMEMODE_CLASS_D Then
				gamemodestr$ = GetLocalString("Menu","save_gamemode_d")
				Color 255,106,0
			ElseIf gopt\GameMode = GAMEMODE_NTF Then
				gamemodestr$ = GetLocalString("Menu","save_gamemode_ntf")
				Color 0,40,255
			Else
				gamemodestr$ = "..."
				Color 255,0,0
			EndIf
			
			Text 20, opt\GraphicHeight-90, GetLocalString("Menu", "save_gamemode")+" "+gamemodestr$
			
			Color 255, 255, 255
			
			Text 20, opt\GraphicHeight-70, GetLocalString("Menu", "save_name")+": "+CurrSave\Name
			Text 20, opt\GraphicHeight-50, GetLocalString("Menu", "map_seed")+": "+RandomSeed
			Text 20, opt\GraphicHeight-30, "v"+VersionNumber
			
			RenderMainMenu()
		Else
			;[Block]
			width = ImageWidth(PauseMenuIMG[0])
			height = ImageHeight(PauseMenuIMG[0])
			x = opt\GraphicWidth / 2 - width / 2
			y = opt\GraphicHeight / 2 - height / 2
			
			DrawImage PauseMenuIMG[0], x, y
			
			Color(255, 255, 255)
			
			x = x+132*MenuScale
			y = y+122*MenuScale
			
			SetFont fo\Font[Font_Menu]
			Text(x, y-(122-45)*MenuScale, Upper(GetLocalString("Menu", "you_died")),False,True)
			SetFont fo\Font[Font_Default]
			
			DrawAllMenuButtons()
			
			y = y+104*MenuScale
			If (Not GameSaved) Or SelectedDifficulty\PermaDeath Then
				Color 50,50,50
				Text(x + 185*MenuScale, y + 30*MenuScale, GetLocalString("Menu", "loadgame"), True, True)
			EndIf
			y= y + 80*MenuScale
			
			SetFont fo\Font[Font_Default]
			Color(255, 255, 255)
			RowText(m_msg\DeathTxt$, x, y + 80*MenuScale, 390*MenuScale, 600*MenuScale)
			;[End Block]
		EndIf
		
		;[Block]
;		width = ImageWidth(PauseMenuIMG[0])
;		height = ImageHeight(PauseMenuIMG[0])
;		x = opt\GraphicWidth / 2 - width / 2
;		y = opt\GraphicHeight / 2 - height / 2
;		
;		DrawImage PauseMenuIMG[0], x, y
;		
;		Color(255, 255, 255)
;		
;		x = x+132*MenuScale
;		y = y+122*MenuScale	
;		
;		If AchievementsMenu > 0 Then
;			SetFont fo\Font[Font_Menu]
;			Text(x, y-(122-45)*MenuScale, GetLocalString("Menu", "achievements",False,True)
;			SetFont fo\Font[Font_Default]
;		ElseIf OptionsMenu > 0 Then
;			SetFont fo\Font[Font_Menu]
;			Text(x, y-(122-45)*MenuScale, "OPTIONS",False,True)
;			SetFont fo\Font[Font_Default]
;		ElseIf QuitMSG > 0 Then
;			SetFont fo\Font[Font_Menu]
;			Text(x, y-(122-45)*MenuScale, "QUIT?",False,True)
;			SetFont fo\Font[Font_Default]
;		ElseIf KillTimer >= 0 Then
;			SetFont fo\Font[Font_Menu]
;			Text(x, y-(122-45)*MenuScale, "PAUSED",False,True)
;			SetFont fo\Font[Font_Default]
;		Else
;			SetFont fo\Font[Font_Menu]
;			Text(x, y-(122-45)*MenuScale, "YOU DIED",False,True)
;			SetFont fo\Font[Font_Default]
;		End If		
;		
;		Local AchvXIMG% = (x + (22*MenuScale))
;		Local scale# = opt\GraphicHeight/768.0
;		Local SeparationConst% = 76*scale
;		Local imgsize% = 64
;		
;		If AchievementsMenu <= 0 And OptionsMenu <= 0 And QuitMSG <= 0
;			SetFont fo\Font[Font_Default]
;			Text x, y, "Difficulty: "+SelectedDifficulty\name
;			Text x, y+20*MenuScale, "Save: "+CurrSave\Name
;			Text x, y+40*MenuScale, "Map seed: "+RandomSeed
;		ElseIf AchievementsMenu <= 0 And OptionsMenu > 0 And QuitMSG <= 0 And KillTimer >= 0
;			Color 0,255,0
;			If OptionsMenu = 1
;				Rect(x-10*MenuScale,y-5*MenuScale,110*MenuScale,40*MenuScale,True)
;			ElseIf OptionsMenu = 2
;				Rect(x+100*MenuScale,y-5*MenuScale,110*MenuScale,40*MenuScale,True)
;			ElseIf OptionsMenu = 3
;				Rect(x+210*MenuScale,y-5*MenuScale,110*MenuScale,40*MenuScale,True)
;			ElseIf OptionsMenu = 4
;				Rect(x+320*MenuScale,y-5*MenuScale,110*MenuScale,40*MenuScale,True)
;			EndIf
;			
;			Local tx# = (opt\GraphicWidth/2)+(width/2)
;			Local ty# = y
;			Local tw# = 400*MenuScale
;			Local th# = 150*MenuScale
;			
;			Color 255,255,255
;			Select OptionsMenu
;				Case 1 ;Graphics
;					;[Block]
;					SetFont fo\Font[Font_Default]
;					
;					y=y+50*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "VSync:")
;					If MouseOn(x+270*MenuScale,y+MenuScale,20*MenuScale,20*MenuScale) And OnSliderID=0
;						DrawOptionsTooltip("vsync")
;					EndIf
;					
;					y=y+30*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Enable room lights:")
;					If MouseOn(x+270*MenuScale,y+MenuScale,20*MenuScale,20*MenuScale) And OnSliderID=0
;						DrawOptionsTooltip("roomlights")
;					EndIf
;					
;					y=y+30*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Screen gamma")
;					If MouseOn(x+270*MenuScale,y+6*MenuScale,100*MenuScale+14,20) And OnSliderID=0
;						DrawOptionsTooltip("gamma",ScreenGamma)
;					EndIf
;					
;					y=y+50*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Particle amount:")
;					If (MouseOn(x + 270 * MenuScale, y-6*MenuScale, 100*MenuScale+14, 20) And OnSliderID=0) Lor OnSliderID=2
;						DrawOptionsTooltip("particleamount",ParticleAmount)
;					EndIf
;					
;					y=y+50*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Texture LOD Bias:")
;					If (MouseOn(x+270*MenuScale,y-6*MenuScale,100*MenuScale+14,20) And OnSliderID=0) Lor OnSliderID=3
;						DrawOptionsTooltip("texquality")
;					EndIf
;					
;					y=y+50*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Cubemap render mode:")
;					If (MouseAndControllerSelectBox(x + 270 * MenuScale, y-6*MenuScale, 100*MenuScale+14, 20, 8, MainMenuTab) And OnSliderID=0) Lor OnSliderID=4
;						DrawOptionsTooltip("cubemap",RenderCubeMapMode)
;					EndIf
;					
;					y=y+50*MenuScale
;					Color 255,255,255
;					Text(x, y, "Field of view:")
;					Color 255,255,0
;					Text(x + 5 * MenuScale, y + 25 * MenuScale, FOV+" FOV")
;					If MouseOn(x+250*MenuScale,y-4*MenuScale,100*MenuScale+14,20)
;						DrawOptionsTooltip("fov")
;					EndIf
;					;[End Block]
;				Case 2 ;Audio
;					;[Block]
;					SetFont fo\Font[Font_Default]
;					
;					y = y + 50*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Music volume:")
;					If MouseOn(x+250*MenuScale,y-4*MenuScale,100*MenuScale+14,20)
;						DrawOptionsTooltip("musicvol",MusicVolume)
;					EndIf
;					
;					y = y + 30*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Sound volume:")
;					If MouseOn(x+250*MenuScale,y-4*MenuScale,100*MenuScale+14,20)
;						DrawOptionsTooltip("soundvol",PrevSFXVolume)
;					EndIf
;					
;					y = y + 30*MenuScale
;					
;					Color 100,100,100
;					Text x, y, "Sound auto-release:"
;					If MouseOn(x+270*MenuScale,y+MenuScale,20*MenuScale,20*MenuScale)
;						DrawOptionsTooltip("sfxautorelease")
;					EndIf
;					
;					y = y + 30*MenuScale
;					
;					Color 100,100,100
;					Text x, y, "Enable user tracks:"
;					If MouseOn(x+270*MenuScale,y+MenuScale,20*MenuScale,20*MenuScale)
;						DrawOptionsTooltip("usertrack")
;					EndIf
;					
;					If EnableUserTracks
;						y = y + 30 * MenuScale
;						Color 255,255,255
;						Text x, y, "User track mode:"
;						If UserTrackMode
;							Text x, y + 20 * MenuScale, "Repeat"
;						Else
;							Text x, y + 20 * MenuScale, "Random"
;						EndIf
;						If MouseOn(x+270*MenuScale,y+MenuScale,20*MenuScale,20*MenuScale)
;							DrawOptionsTooltip("usertrackmode")
;						EndIf
;					EndIf
;					;[End Block]
;				Case 3 ;Controls
;					;[Block]
;					SetFont fo\Font[Font_Default]
;					
;					y = y + 50*MenuScale
;					
;					Color(255, 255, 255)
;					Text(x, y, "Mouse sensitivity:")
;					If MouseOn(x+270*MenuScale,y-4*MenuScale,100*MenuScale,20)
;						DrawOptionsTooltip("mousesensitivity",MouseSens)
;					EndIf
;					
;					y = y + 30*MenuScale
;					
;					Color(255, 255, 255)
;					Text(x, y, "Invert mouse Y-axis:")
;					If MouseOn(x+270*MenuScale,y+MenuScale,20*MenuScale,20*MenuScale)
;						DrawOptionsTooltip("mouseinvert")
;					EndIf
;					
;					y = y + 30*MenuScale
;					Text(x, y, "Control configuration:")
;					y = y + 10*MenuScale
;					
;					Text(x, y + 20 * MenuScale, "Move Forward")
;					
;					Text(x, y + 40 * MenuScale, "Strafe Left")
;					
;					Text(x, y + 60 * MenuScale, "Move Backward")
;					
;					Text(x, y + 80 * MenuScale, "Strafe Right")
;					
;					
;					Text(x, y + 100 * MenuScale, "Manual Blink")
;					
;					Text(x, y + 120 * MenuScale, "Sprint")
;					
;					Text(x, y + 140 * MenuScale, "Open/Close Inventory")
;					
;					Text(x, y + 160 * MenuScale, "Crouch")
;					
;					Text(x, y + 180 * MenuScale, "Quick Save")
;					
;					Text(x, y + 200 * MenuScale, "Open/Close Console")
;					
;					If MouseOn(x,y,300*MenuScale,220*MenuScale)
;						DrawOptionsTooltip("controls")
;					EndIf
;					;[End Block]
;				Case 4 ;Advanced
;					;[Block]
;					SetFont fo\Font[Font_Default]
;					
;					y = y + 50*MenuScale
;					
;					Color 255,255,255				
;					Text(x, y, "Show HUD:")
;					If MouseOn(x+270*MenuScale,y+MenuScale,20*MenuScale,20*MenuScale)
;						DrawOptionsTooltip("hud")
;					EndIf
;					
;					y = y + 30*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Enable console:")
;					If MouseOn(x+270*MenuScale,y+MenuScale,20*MenuScale,20*MenuScale)
;						DrawOptionsTooltip("consoleenable")
;					EndIf
;					
;					y = y + 30*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Open console on error:")
;					If MouseOn(x+270*MenuScale,y+MenuScale,20*MenuScale,20*MenuScale)
;						DrawOptionsTooltip("consoleerror")
;					EndIf
;					
;					y = y + 50*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Achievement popups:")
;					If MouseOn(x+270*MenuScale,y+MenuScale,20*MenuScale,20*MenuScale)
;						DrawOptionsTooltip("achpopup")
;					EndIf
;					
;					y = y + 50*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Show FPS:")
;					If MouseOn(x+270*MenuScale,y+MenuScale,20*MenuScale,20*MenuScale)
;						DrawOptionsTooltip("showFPS")
;					EndIf
;					
;					y = y + 30*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Framelimit:")
;					
;					Color 255,255,255
;					If CurrFrameLimit>0.0
;						Color 255,255,0
;						Text(x + 5 * MenuScale, y + 25 * MenuScale, Framelimit%+" FPS")
;					EndIf
;					If MouseOn(x+270*MenuScale,y+MenuScale,20*MenuScale,20*MenuScale)
;						DrawOptionsTooltip("framelimit",Framelimit)
;					EndIf
;					If MouseOn(x+150*MenuScale,y+30*MenuScale,100*MenuScale,20)
;						DrawOptionsTooltip("framelimit",Framelimit)
;					EndIf
;					
;					y = y + 80*MenuScale
;					
;					Color 255,255,255
;					Text(x, y, "Antialiased text:")
;					If MouseOn(x+270*MenuScale,y+MenuScale,20*MenuScale,20*MenuScale)
;						DrawOptionsTooltip("antialiastext")
;					EndIf
;					;[End Block]
;			End Select
;		ElseIf AchievementsMenu <= 0 And OptionsMenu <= 0 And QuitMSG > 0 And KillTimer >= 0
;			Local QuitButton% = 60 
;			If SelectedDifficulty\saveType = SAVEONQUIT Lor SelectedDifficulty\saveType = SAVEANYWHERE Then
;				Local RN$ = PlayerRoom\RoomTemplate\Name$
;				Local AbleToSave% = True
;				If RN$ = "173" Lor RN$ = "exit1" Lor RN$ = "gatea" Then AbleToSave = False
;				If (Not CanSave) Then AbleToSave = False
;				If AbleToSave
;					QuitButton = 140
;				EndIf
;			EndIf
;		Else
;			If AchievementsMenu>0 Then
;				For i=0 To 11
;					If i+((AchievementsMenu-1)*12)<MAXACHIEVEMENTS Then
;						DrawAchvIMG(AchvXIMG,y+((i/4)*120*MenuScale),i+((AchievementsMenu-1)*12))
;					Else
;						Exit
;					EndIf
;				Next
;				
;				For i=0 To 11
;					If i+((AchievementsMenu-1)*12)<MAXACHIEVEMENTS Then
;						If MouseOn(AchvXIMG+((i Mod 4)*SeparationConst),y+((i/4)*120*MenuScale),64*scale,64*scale) Then
;							AchievementTooltip(i+((AchievementsMenu-1)*12))
;							Exit
;						EndIf
;					Else
;						Exit
;					EndIf
;				Next
;				
;			EndIf
;		EndIf
;		
;		y = y+10
;		
;		If AchievementsMenu<=0 And OptionsMenu<=0 And QuitMSG<=0 Then
;			If KillTimer >= 0 Then	
;				
;				y = y+ 72*MenuScale
;				
;				y = y + 75*MenuScale
;				If (Not SelectedDifficulty\permaDeath) Then
;					If GameSaved Then
;						
;					Else
;						DrawFrame(x,y,390*MenuScale, 60*MenuScale)
;						Color (100, 100, 100)
;						SetFont fo\Font[Font_Menu]
;						Text(x + (390*MenuScale) / 2, y + (60*MenuScale) / 2, "Load Game", True, True)
;					EndIf
;					y = y + 75*MenuScale
;				EndIf
;				
;				y = y + 75*MenuScale
;				
;				y = y + 75*MenuScale
;			Else
;				y = y+104*MenuScale
;				If GameSaved And (Not SelectedDifficulty\permaDeath) Then
;					
;				Else
;					Color 50,50,50
;					Text(x + 185*MenuScale, y + 30*MenuScale, "Load Game", True, True)
;				EndIf
;				y= y + 80*MenuScale
;			EndIf
;			
;			If KillTimer >= 0 And (Not MainMenuOpen)
;				
;			EndIf
;			
;			SetFont fo\Font[Font_Default]
;			If KillTimer < 0 Then Color(255, 255, 255) : RowText(m_msg\DeathTxt$, x, y + 80*MenuScale, 390*MenuScale, 600*MenuScale)
;		EndIf
;		
;		DrawAllMenuButtons()
;		DrawAllMenuTicks()
;		DrawAllMenuInputBoxes()
;		DrawAllMenuSlideBars()
;		DrawAllMenuSliders()
;		
;		If opt\DisplayMode=2 Then DrawImage CursorIMG, ScaledMouseX(),ScaledMouseY()
		;[End Block]
	EndIf
	
	SetFont fo\Font[Font_Default]
	
	CatchErrors("DrawMenu")
End Function

Function UpdateMenu()
	CatchErrors("Uncaught (UpdateMenu)")
	Local x%, y%, width%, height%
	
	If MenuOpen
		If KillTimer >= 0 Then
			ShowEntity m_I\Sprite
			UpdateMainMenu()
		Else
			;[Block]
			width = ImageWidth(PauseMenuIMG[0])
			height = ImageHeight(PauseMenuIMG[0])
			x = opt\GraphicWidth / 2 - width / 2
			y = opt\GraphicHeight / 2 - height / 2
			
			x = x+132*MenuScale
			y = y+122*MenuScale	
			
			y = y+104*MenuScale
			If GameSaved And (Not SelectedDifficulty\PermaDeath) Then
				If DrawButton(x, y, 390*MenuScale, 60*MenuScale, GetLocalString("Menu", "loadgame")) Then
					;DrawLoading(0)
					
					DeleteMenuGadgets()
					MenuOpen = False
					LoadGameQuick(SavePath + CurrSave\Name + "\")
					
					MoveMouse Viewport_Center_X,Viewport_Center_Y
					HidePointer()
					
					KillSounds()
					
					Playable=True
					
					UpdateRooms()
					UpdateDoors()
					
					Local r.Rooms, z
					
					For r.Rooms = Each Rooms
						x = Abs(EntityX(Collider) - EntityX(r\obj))
						z = Abs(EntityZ(Collider) - EntityZ(r\obj))
						
						If x < 12.0 And z < 12.0 Then
							MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)] = Max(MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)], 1)
							If x < 4.0 And z < 4.0 Then
								If Abs(EntityY(Collider) - EntityY(r\obj)) < 1.5 Then PlayerRoom = r
								MapFound[Floor(EntityX(r\obj) / 8.0) * MapWidth + Floor(EntityZ(r\obj) / 8.0)] = 1
							EndIf
						End If
					Next
					
					;DrawLoading(100,True)
					PlaySound_Strict LoadTempSound(("SFX\Horror\Horror8.ogg"))
					
					DropSpeed=0
					
					UpdateWorld 0.0
					
					PrevTime = MilliSecs()
					FPSfactor = 0
					
					ResetInput()
					
					ResumeSounds()
					Return
				EndIf
			Else
				DrawButton(x, y, 390*MenuScale, 60*MenuScale, "")
			EndIf
;			If DrawButton(x, y, 390*MenuScale, 60*MenuScale, GetLocalString("Menu", "revive")) Then
;				
;				;[Block]
;				HealSPPlayer(100)
;				If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 4) = "vest" Then
;					psp\Kevlar = 100
;				EndIf
;				If Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet" Then
;					psp\Helmet = 100
;				EndIf
;				
;				DropSpeed = -0.1
;				HeadDropSpeed = 0.0
;				Shake = 0
;				CurrSpeed = 0
;				
;				HeartBeatVolume = 0
;				
;				CameraShake = 0
;				Shake = 0
;				LightFlash = 0
;				BlurTimer = 0
;				
;				FallTimer = 0
;				MenuOpen = False
;				
;				GodMode = 0
;				NoClip = 0
;				
;				ShowEntity Collider
;				
;				TeleportEntity(Collider,EntityX(Collider),EntityY(Collider)+0.5,EntityZ(Collider),0.3,True)
;				
;				KillTimer = 0
;				KillAnim = 0
;				;[End Block]
;				
;				ResetInput()
;				
;				ResumeSounds()
;				Return
;			EndIf
			
			If DrawButton(x, y + 80*MenuScale, 390*MenuScale, 60*MenuScale, GetLocalString("Menu", "quit_to_menu")) Then
				MainMenuOpen = True
				NullGame()
				MenuOpen = False
				MainMenuTab = 0
				CurrSave = Null
				FlushKeys()
				Return
			EndIf
			;[End Block]
		EndIf
		
		;[Block]
	Else
		HideEntity m_I\Sprite
	EndIf
	
	CatchErrors("UpdateMenu")
End Function

Function MouseOn%(x%, y%, width%, height%)
	If ScaledMouseX() > x And ScaledMouseX() < x + width Then
		If ScaledMouseY() > y And ScaledMouseY() < y + height Then
			Return True
		End If
	End If
	Return False
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D