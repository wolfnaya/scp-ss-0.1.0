
Const MaxInventorySpace% = 37

Const SLOT_HEAD = 9
Const SLOT_TORSO = 10
Const SLOT_BACKPACK = 11

Const SLOT_PRIMARY = 12
Const SLOT_SECONDARY = 13
Const SLOT_HOLSTER = 14
Const SLOT_SCABBARD = 15

Function RenderInventory()
	Local Temp%
	Local x%, y%, z%, i%, yawvalue#, pitchvalue#
	Local x2#,y2#,z2#
	Local n%, xtemp, ytemp, strtemp$, projY#, scale#
	
	Local e.Events, it.Items
	
	Local PrevInvOpen% = InvOpen, MouseSlot% = 66
	Local g.Guns
	Local ShouldDrawHUD%=True
	
	Local Spacing%
	Local PrevOtherOpen.Items
	Local OtherSize%,OtherAmount%
	Local IsEmpty%
	Local IsMouseOn%
	Local ClosedInv%
	
	If OtherOpen<>Null Then
		;[Block]
		PrevOtherOpen = OtherOpen
		OtherSize=OtherOpen\invSlots
		
		For i%=0 To OtherSize-1
			If OtherOpen\SecondInv[i] <> Null Then
				OtherAmount = OtherAmount+1
			EndIf
		Next
		
		Local tempX% = 0
		
		Local width = 70
		Local height = 70
		Spacing% = 35
		
		x = opt\GraphicWidth / 2 - (width * 9 / 2 + Spacing * (9 / 2 - 1)) / 2
		y = opt\GraphicHeight / 2 - (height * OtherSize / 5 + height * (OtherSize / 5 - 1)) / 2
		
		For n% = 0 To OtherSize - 1
			IsMouseOn% = False
			If MouseOn(x, y, width, height) Then 
				IsMouseOn = True
				MouseSlot = n
				Color 255, 187, 0
				Rect(x - 1, y - 1, width + 2, height + 2)
			EndIf
			
			DrawFrame(x, y, width, height, (x Mod 64), (x Mod 64))
			
			If OtherOpen = Null Then Exit
			
			If OtherOpen\SecondInv[n] <> Null Then
				If (SelectedItem <> OtherOpen\SecondInv[n] Lor IsMouseOn) Then DrawImage(OtherOpen\SecondInv[n]\invimg, x + width / 2 - 32, y + height / 2 - 32)
			EndIf
			If OtherOpen\SecondInv[n] <> Null And SelectedItem <> OtherOpen\SecondInv[n] Then
				If IsMouseOn Then
					Color 255, 255, 255
					SetFont(fo\Font[Font_Default])
					Text(x + width / 2, y + height + Spacing - 15, OtherOpen\SecondInv[n]\itemtemplate\name, True)
				EndIf
				
			EndIf					
			
			x=x+width + Spacing
			tempX=tempX + 1
			If tempX = 5 Then 
				tempX=0
				y = y + height*2 
				x = opt\GraphicWidth / 2 - (width * MaxItemAmount /2 + Spacing * (MaxItemAmount / 2 - 1)) / 2
			EndIf
		Next
		
		If n = 5 Then
			y = y + height + Spacing
			x = opt\GraphicWidth / 2 - (width * 9 / 2 + Spacing * (9 / 2 - 1)) / 2
		EndIf
		
		If SelectedItem <> Null Then
			If MouseDown1 Then
				If MouseSlot = 66 Then
					DrawImage(SelectedItem\invimg, ScaledMouseX() - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, ScaledMouseY() - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
				ElseIf SelectedItem <> PrevOtherOpen\SecondInv[MouseSlot]
					DrawImage(SelectedItem\invimg, ScaledMouseX() - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, ScaledMouseY() - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
				EndIf
			EndIf
		EndIf
		;[End Block]
		
	ElseIf InvOpen Then ; ~ Inventory has been open
		
		d_I\SelectedDoor = Null
		
		width% = 70
		height% = 70
		Spacing% = 1
		
		x = opt\GraphicWidth / 2 - (width * 9 / 3 + Spacing * (9 / 3 - 1)) / 2
		If Inventory[SLOT_BACKPACK] <> Null And Inventory[SLOT_BACKPACK]\itemtemplate\tempname = "backpack" Then
			y = opt\GraphicHeight / 2 - (height * 30 / 2.5 + Spacing * (30 / 2.5 - 1)) / 2
		Else
			y = opt\GraphicHeight / 2 - (height * 9 / 3 + Spacing * (9 / 3 - 1)) / 2
		EndIf
		
		Local fx2 = opt\GraphicWidth / 2 - (width * 3 + Spacing * 2) / 2
		Local fy2 = opt\GraphicHeight / 2 - (height * 3 + Spacing * 2) / 2
		
		Local width2# = 70
		Local height2# = 70
		Local spacing2# = 350
		
		;! ~ [Inventory Text]
		
		x2 = opt\GraphicWidth / 2 - (width2 * 9 / 2 + spacing2 * (9 / 2 - 1)) / 2
		y2 = opt\GraphicHeight / 2 - height2 + height2*4
		
		x2=x2+width2 + spacing2
		
		SetFont fo\Font[Font_Default_Medium]
		Text(x+width*7.5, y-height/1.5,GetLocalString("Inventory","inventory"),True,True)
		If Inventory[SLOT_BACKPACK] <> Null And Inventory[SLOT_BACKPACK]\itemtemplate\tempname = "backpack" Then
			Text x+width*7.5,y2-height*5.5,GetLocalString("Inventory","slot_backpack"),1,1
		EndIf
		SetFont fo\Font[Font_Default]
		
		;! ~ [Inventory Image]
		
		DrawImage(inv_character_img, opt\GraphicWidth / 2 - width/3.5, opt\GraphicHeight / 2)
		
		;! ~ [Weapon Slots Text]
		
		Text x2+((width2+spacing2))-width*5.5+7.5,y2-height*6,GetLocalString("Inventory","slot_primary"),1,1
		Text x2+((width2+spacing2))+width/2+7.5,y2-height*6,GetLocalString("Inventory","slot_secondary"),1,1
		Text x2+((width2+spacing2))-width*5.5+10,y2-height/2-2,GetLocalString("Inventory","slot_holster"),1,1
		Text x2+((width2+spacing2))+width/2+10,y2-height/2-2,GetLocalString("Inventory","slot_scabbard"),1,1
		
		;! ~ [Item Slots]
		
		For  n% = 0 To MaxItemAmount - 1
			IsMouseOn% = False
			
			;! ~ [Inventory Frames]
			
			If n < SLOT_HEAD Then ; ~ Inventory
				If MouseOn(x+width*6, y, width, height) Then 
					IsMouseOn = True
					MouseSlot = n
					Color 255, 187, 0
					Rect(x+width*6 - 1, y - 1, width + 2, height + 2)
				EndIf
			ElseIf n > SLOT_SCABBARD Then ; ~ Backpack Inventory Slots
				If MouseOn(x+width*6, y - height/0.7, width, height) Then 
					IsMouseOn = True
					MouseSlot = n
					Color 255, 187, 0
					Rect(x+width*6 - 1, y - height/0.7 - 1, width + 2, height + 2)
				EndIf
			ElseIf n = SLOT_HEAD Then ; ~ Head
				If MouseOn(fx2 + width*0.4, fy2 - height*1.45, width, height) Then
					IsMouseOn = True
					MouseSlot = n
					Color 100, 100, 100
					Rect(fx2 + width*0.4 - 1, fy2 - height*1.45 - 0.5, width + 2, height + 2)
				EndIf
			ElseIf n = SLOT_TORSO Then ; ~ Torso
				If MouseOn(fx2 + width*0.4, fy2 + height/1.45, width, height) Then
					IsMouseOn = True
					MouseSlot = n
					Color 100, 100, 100
					Rect(fx2 + width*0.4 - 1, fy2 + height/1.45 - 1, width + 2, height + 2)
				EndIf
			ElseIf n = SLOT_BACKPACK Then ; ~ Backpack
				If MouseOn(fx2 + width*2, fy2 - height*0.1, width, height) Then
					IsMouseOn = True
					MouseSlot = n
					Color 100, 100, 100
					Rect(fx2 + width*2 - 1, fy2 - height*0.1 - 1, width + 2, height + 2)
				EndIf
			ElseIf n = SLOT_PRIMARY Then ; ~ Primary Weapon Slot
				If MouseOn(fx2-width*2.7, fy2-height*1.2, width2*2, height2*5) Then
					IsMouseOn = True
					MouseSlot = n
					Color 100, 100, 100
					Rect(fx2-width*2.7 - 1, fy2-height*1.2 - 1, width2*2 + 2, height2*5 + 2)
				EndIf
			ElseIf n = SLOT_SECONDARY Then ; ~ Secondary Weapon Slot
				If MouseOn(fx2+width*3.5, fy2-height*1.2, width2*2, height2*5) Then
					IsMouseOn = True
					MouseSlot = n
					Color 100, 100, 100
					Rect(fx2+width*3.5 - 1, fy2-height*1.2 - 1, width2*2 + 2, height2*5 + 2)
				EndIf
			ElseIf n = SLOT_HOLSTER Then ; ~ Holster Weapon Slot
				If MouseOn(fx2-width*2.1, fy2+height*4.4, width, height) Then
					IsMouseOn = True
					MouseSlot = n
					Color 100, 100, 100
					Rect(fx2-width*2.1 - 1, fy2+height*4.4 - 1, width + 2, height + 2)
				EndIf
			ElseIf n = SLOT_SCABBARD Then ; ~ Scabbard Weapon Slot
				If MouseOn(fx2+width*3.9, fy2+height*4.4, width, height) Then
					IsMouseOn = True
					MouseSlot = n
					Color 100, 100, 100
					Rect(fx2+width*3.9 - 1, fy2+height*4.4 - 1, width + 2, height + 2)
				EndIf
			EndIf
			
			Color 255, 255, 255
			If n < SLOT_HEAD Then ; ~ Inventory
				DrawFrame(x+width*6, y, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
			ElseIf n > SLOT_SCABBARD Then ; ~ Backpack Inventory Slots
				If SelectedItem <> Null And SelectedItem\itemtemplate\IsBackpack Then
					DrawFrame(x+width*6, y - height/0.7, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,15,0,0,200,0,0)
				Else
					DrawFrame(x+width*6, y - height/0.7, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
				EndIf
			ElseIf n = SLOT_HEAD Then ; ~ Head
				If SelectedItem <> Null And (Not SelectedItem\itemtemplate\IsHead) Lor (SelectedItem <> Null And SelectedItem\itemtemplate\IsHead And Inventory[SLOT_TORSO] <> Null And Inventory[SLOT_TORSO]\itemtemplate\IsFullBody) Then
					DrawFrame(fx2 + width*0.4, fy2 - height*1.45, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,15,0,0,200,0,0)
				ElseIf SelectedItem <> Null And SelectedItem\itemtemplate\IsHead Then
					DrawFrame(fx2 + width*0.4, fy2 - height*1.45, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,0,15,0,0,200,0)
				Else
					DrawFrame(fx2 + width*0.4, fy2 - height*1.45, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
				EndIf
			ElseIf n = SLOT_TORSO Then ; ~ Torso
				If SelectedItem <> Null And (Not SelectedItem\itemtemplate\IsTorso) Lor (SelectedItem <> Null And SelectedItem\itemtemplate\IsTorso And SelectedItem\itemtemplate\IsFullBody And Inventory[SLOT_HEAD] <> Null) Then
					DrawFrame(fx2 + width*0.4, fy2 + height/1.45, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,15,0,0,200,0,0)
				ElseIf SelectedItem <> Null And SelectedItem\itemtemplate\IsTorso Then
					DrawFrame(fx2 + width*0.4, fy2 + height/1.45, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,0,15,0,0,200,0)
				Else
					DrawFrame(fx2 + width*0.4, fy2 + height/1.45, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
				EndIf
			ElseIf n = SLOT_BACKPACK Then ; ~ Backpack
				If SelectedItem <> Null And (Not SelectedItem\itemtemplate\IsBackpack) Then
					DrawFrame(fx2 + width*2, fy2 - height*0.1, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,15,0,0,200,0,0)
				ElseIf SelectedItem <> Null And SelectedItem\itemtemplate\IsBackpack Then
					DrawFrame(fx2 + width*2, fy2 - height*0.1, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,0,15,0,0,200,0)
				Else
					DrawFrame(fx2 + width*2, fy2 - height*0.1, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
				EndIf
			ElseIf n = SLOT_PRIMARY Then ; ~ Primary Weapon Slot
				If SelectedItem <> Null And (Not SelectedItem\itemtemplate\IsGun) Then
					DrawFrame(fx2-width*2.7, fy2-height*1.2, width2*2, height2*5, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,15,0,0,200,0,0)
				ElseIf SelectedItem <> Null And SelectedItem\itemtemplate\IsGun Then
					DrawFrame(fx2-width*2.7, fy2-height*1.2, width2*2, height2*5, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,0,15,0,0,200,0)
				Else
					DrawFrame(fx2-width*2.7, fy2-height*1.2, width2*2, height2*5, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
				EndIf
			ElseIf n = SLOT_SECONDARY Then ; ~ Secondary Weapon Slot
				If SelectedItem <> Null And (Not SelectedItem\itemtemplate\IsGun) Then
					DrawFrame(fx2+width*3.5, fy2-height*1.2, width2*2, height2*5, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,15,0,0,200,0,0)
				ElseIf SelectedItem <> Null And SelectedItem\itemtemplate\IsGun Then
					DrawFrame(fx2+width*3.5, fy2-height*1.2, width2*2, height2*5, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,0,15,0,0,200,0)
				Else
					DrawFrame(fx2+width*3.5, fy2-height*1.2, width2*2, height2*5, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
				EndIf
			ElseIf n = SLOT_HOLSTER Then ; ~ Holster Weapon Slot
				If SelectedItem <> Null And (Not SelectedItem\itemtemplate\IsForHolster) Then
					DrawFrame(fx2-width*2.1, fy2+height*4.4, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,15,0,0,200,0,0)
				ElseIf SelectedItem <> Null And SelectedItem\itemtemplate\IsForHolster Then
					DrawFrame(fx2-width*2.1, fy2+height*4.4, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,0,15,0,0,200,0)
				Else
					DrawFrame(fx2-width*2.1, fy2+height*4.4, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
				EndIf
			ElseIf n = SLOT_SCABBARD Then ; ~ Scabbard Weapon Slot
				If SelectedItem <> Null And (Not SelectedItem\itemtemplate\IsForScabbard) Then
					DrawFrame(fx2+width*3.9, fy2+height*4.4, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,15,0,0,200,0,0)
				ElseIf SelectedItem <> Null And SelectedItem\itemtemplate\IsForScabbard Then
					DrawFrame(fx2+width*3.9, fy2+height*4.4, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,0,15,0,0,200,0)
				Else
					DrawFrame(fx2+width*3.9, fy2+height*4.4, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
				EndIf
			EndIf
			
			If Inventory[n] <> Null Then
				
				Local hasGunInSlot% = False
				
				If (SelectedItem <> Inventory[n] Lor IsMouseOn) Then
					If n < SLOT_HEAD Then ; ~ Inventory
						DrawImage(Inventory[n]\invimg, x + width / 2 + width*6 - 32, y + height / 2 - 32)
					ElseIf n > SLOT_SCABBARD Then ; ~ Backpack Inventory Slots
						DrawImage(Inventory[n]\invimg, x + width / 2 + width*6 - 32, y - height/0.55 / 2 - 32)
					ElseIf n = SLOT_HEAD Then ; ~ Head
						DrawImage(Inventory[n]\invimg, fx2 + width*0.4 + 3, fy2 - height*1.45 + 1)
					ElseIf n = SLOT_TORSO Then ; ~ Torso
						DrawImage(Inventory[n]\invimg, fx2 + width*0.4 + 3, fy2 + height/1.45 + 1)
					ElseIf n = SLOT_BACKPACK Then ; ~ Backpack
						DrawImage(Inventory[n]\invimg, fx2 + width*2 + 3, fy2 - height*0.1 + 1)
					ElseIf n = SLOT_PRIMARY Then ; ~ Primary Weapon Slot
						If g_I\Weapon_InSlot[QUICKSLOT_PRIMARY] <> "" Then
							For g = Each Guns
								If g\name = g_I\Weapon_InSlot[QUICKSLOT_PRIMARY] Then
									Color 255,255,255
									DrawImage g\INV_IMG, fx2-width*2.7 + 2, fy2-height*1.2 + 2
									hasGunInSlot = True
									Exit
								EndIf
							Next
						EndIf
					ElseIf n = SLOT_SECONDARY Then ; ~ Secondary Weapon Slot
						If g_I\Weapon_InSlot[QUICKSLOT_SECONDARY] <> "" Then
							For g = Each Guns
								If g\name = g_I\Weapon_InSlot[QUICKSLOT_SECONDARY] Then
									Color 255,255,255
									DrawImage g\INV_IMG, fx2+width*3.5 + 2, fy2-height*1.2 + 2
									hasGunInSlot = True
									Exit
								EndIf
							Next
						EndIf
					ElseIf n = SLOT_HOLSTER Then ; ~ Holster Weapon Slot
						If g_I\Weapon_InSlot[QUICKSLOT_HOLSTER] <> "" Then
							For g = Each Guns
								If g\name = g_I\Weapon_InSlot[QUICKSLOT_HOLSTER] Then
									Color 255,255,255
									DrawImage g\IMG, fx2-width*2.1 + 2, fy2+height*4.4 + 2
									hasGunInSlot = True
									Exit
								EndIf
							Next
						EndIf
					ElseIf n = SLOT_SCABBARD Then ; ~ Scabbard Weapon Slot
						If g_I\Weapon_InSlot[QUICKSLOT_SCABBARD] <> "" Then
							For g = Each Guns
								If g\name = g_I\Weapon_InSlot[QUICKSLOT_SCABBARD] Then
									Color 255,255,255
									DrawImage g\IMG, fx2+width*3.9 + 2, fy2+height*4.4 + 2
									hasGunInSlot = True
									Exit
								EndIf
							Next
						EndIf
					EndIf
				EndIf
			EndIf
			
			Text fx2 + width*0.4 + 35, fy2 - height*1.45 - 15,GetLocalString("Inventory","slot_head"),1,1
			
			Text fx2 + width*0.4 + 35, fy2 + height/1.45 - 15,GetLocalString("Inventory","slot_torso"),1,1
			
			Text fx2 + width*2 + 35, fy2 - height*0.1 - 15,GetLocalString("Inventory","slot_backpack"),1,1
			
			;! ~ [Item Display Name]
			
			Local fx = opt\GraphicWidth / 2 - width / 2
			If Inventory[SLOT_BACKPACK] <> Null And Inventory[SLOT_BACKPACK]\itemtemplate\tempname = "backpack" Then
				Local fy = opt\GraphicHeight / 2 + height * 3
			Else
				fy = opt\GraphicHeight / 2 - height / 2
			EndIf
			
			If Inventory[n] <> Null And SelectedItem <> Inventory[n] Then
				If IsMouseOn Then
					If SelectedItem = Null
						
						spacing2# = 0
						x2# = fx
						y2# = fy+height
						
						; ~ [Display Text]
						
						SetFont fo\Font[Font_Default]
						
						DrawFrame(fx+width*5, fy + height*2.5, width*3 + 1, height/2, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
						
						Color 0,0,0
						Text(fx + width / 2 +width*6 + 1, fy + height + Spacing + spacing2 - 15+height*1.85 + 1, Inventory[n]\name, True)
						Color 255, 255, 255	
						Text(fx + width / 2 +width*6, fy + height + Spacing + spacing2 - 15+height*1.85, Inventory[n]\name, True)
						
					EndIf
				EndIf
			Else
				
			EndIf
			
			x=x+width + Spacing
			
			;! ~ [Inventory Frames Division]
			
			If n = 2 Lor n = 5 Then
				y = y + height + Spacing
				x = opt\GraphicWidth / 2 - (width * 9 / 3 + Spacing * (9 / 3 - 1)) / 2
			ElseIf n = 15 Lor n = 18 Lor n = 21 Lor n = 24 Lor n = 27 Lor n = 30 Lor n = 33 Lor n = 36 Then
				y = y + height + Spacing
				x = opt\GraphicWidth / 2 - (width * 9 / 3 + Spacing * (9 / 3 - 1)) / 2
			ElseIf n = SLOT_HEAD Then
				x = opt\GraphicWidth / 2 - (width * 9 / 3 + Spacing * (MaxItemAmount / 3 - 1)) / 2
				y = opt\GraphicHeight / 2 - (height * 9 / 3 + Spacing * (9 / 3 - 1)) / 2
			EndIf
			
			;! ~ [End]
			
		Next
		
		;! ~ [End]
		
		If SelectedItem <> Null Then
			If MouseDown1 Then
				If MouseSlot = 66 Then
					DrawImage(SelectedItem\invimg, ScaledMouseX() - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, ScaledMouseY() - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
				ElseIf SelectedItem <> Inventory[MouseSlot]
					DrawImage(SelectedItem\invimg, ScaledMouseX() - ImageWidth(SelectedItem\itemtemplate\invimg) / 2, ScaledMouseY() - ImageHeight(SelectedItem\itemtemplate\invimg) / 2)
				EndIf
			Else
				If MouseSlot = 66 Then
					
				Else
					
				EndIf
			EndIf
		EndIf
		
	EndIf
	
End Function

Function UpdateInventory()
	Local Temp%
	Local x%, y%, z%, i%, yawvalue#, pitchvalue#, pvt%
	Local x2#,y2#,z2#
	Local n%, xtemp, ytemp, strtemp$, projY#, scale#
	Local e.Events, it.Items, np.NPCs, ne.NewElevator
	
	Local PrevInvOpen% = InvOpen, MouseSlot% = 66
	Local g.Guns
	Local shouldDrawHUD%=True
	
	Local Spacing%
	Local PrevOtherOpen.Items, PrevItem.Items
	Local PrevBackPackOpen.Items
	Local OtherSize%,OtherAmount%
	Local IsEmpty%
	Local IsMouseOn%
	Local ClosedInv%
	
	If OtherOpen<>Null Then
		;[Block]
		If (PlayerRoom\RoomTemplate\Name = "gate_a_topside") Then
			HideEntity Fog
			CameraFogRange Camera, 5,30
			CameraFogColor (Camera,200,200,200)
			CameraClsColor (Camera,200,200,200)					
			CameraRange(Camera, 0.01, 30)
		ElseIf (PlayerRoom\RoomTemplate\Name = "gate_b_topside") Then
			HideEntity Fog
			CameraFogRange Camera, 5,45
			CameraFogColor (Camera,200,200,200)
			CameraClsColor (Camera,200,200,200)					
			CameraRange(Camera, 0.01, 60)
		ElseIf (PlayerRoom\RoomTemplate\Name = "gate_a_intro") Then
			CameraFogRange Camera, 5,30
			CameraFogColor (Camera,200,200,200)
			CameraClsColor (Camera,200,200,200)					
			CameraRange(Camera, 0.005, 100)
		EndIf
		
		PrevOtherOpen = OtherOpen
		OtherSize=OtherOpen\invSlots
		
		For i%=0 To OtherSize-1
			If OtherOpen\SecondInv[i] <> Null Then
				OtherAmount = OtherAmount+1
			EndIf
		Next
		
		InvOpen = False
		d_I\SelectedDoor = Null
		Local tempX% = 0
		
		Local width = 70
		Local height = 70
		Spacing% = 35
		
		x = opt\GraphicWidth / 2 - (width * 9 / 2 + Spacing * (9 / 2 - 1)) / 2
		y = opt\GraphicHeight / 2 - (height * OtherSize / 5 + height * (OtherSize / 5 - 1)) / 2
		
		ItemAmount = 0
		For n% = 0 To OtherSize - 1
			IsMouseOn% = False
			If MouseOn(x, y, width, height) Then 
				IsMouseOn = True
				MouseSlot = n
			EndIf
			
			If OtherOpen = Null Then Exit
			
			;debuglog "otheropen: "+(OtherOpen<>Null)
			If OtherOpen\SecondInv[n] <> Null And SelectedItem <> OtherOpen\SecondInv[n] Then
				If IsMouseOn Then
					If SelectedItem = Null Then
						If MouseHit1 Then
							SelectedItem = OtherOpen\SecondInv[n]
							MouseHit1 = False
							
							If DoubleClick Then
								If OtherOpen\SecondInv[n]\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[OtherOpen\SecondInv[n]\itemtemplate\sound])
								OtherOpen = Null
								ClosedInv=True
								InvOpen = False
								DoubleClick = False
							EndIf
						EndIf
					Else
						
					EndIf
				EndIf
				
				ItemAmount=ItemAmount+1
			Else
				If IsMouseOn And MouseHit1 Then
					For z% = 0 To OtherSize - 1
						If OtherOpen\SecondInv[z] = SelectedItem Then OtherOpen\SecondInv[z] = Null
					Next
					OtherOpen\SecondInv[n] = SelectedItem
				EndIf
				
			EndIf
			
			x=x+width + Spacing
			tempX=tempX + 1
			If tempX = 5 Then 
				tempX=0
				y = y + height*2 
				x = opt\GraphicWidth / 2 - (width * MaxItemAmount /2 + Spacing * (MaxItemAmount / 2 - 1)) / 2
			EndIf
		Next
		
		If SelectedItem <> Null Then
			If MouseDown1 Then
				
			Else
				If MouseSlot = 66 Then
					If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
					
					ShowEntity(SelectedItem\collider)
					PositionEntity(SelectedItem\collider, EntityX(Camera), EntityY(Camera), EntityZ(Camera))
					RotateEntity(SelectedItem\collider, EntityPitch(Camera), EntityYaw(Camera), 0)
					MoveEntity(SelectedItem\collider, 0, -0.1, 0.1)
					RotateEntity(SelectedItem\collider, 0, Rand(360), 0)
					ResetEntity (SelectedItem\collider)
					
					SelectedItem\DropSpeed = 0.0
					
					SelectedItem\Picked = False
					For z% = 0 To OtherSize - 1
						If OtherOpen\SecondInv[z] = SelectedItem Then OtherOpen\SecondInv[z] = Null
					Next
					
					IsEmpty=True
					
					For z% = 0 To OtherSize - 1
						If OtherOpen\SecondInv[z]<>Null Then IsEmpty=False : Exit
					Next
					
					If IsEmpty Then
						Select OtherOpen\itemtemplate\tempname
							Case "clipboard"
								OtherOpen\invimg = OtherOpen\itemtemplate\invimg2
								SetAnimTime OtherOpen\model,17.0
						End Select
					EndIf
					
					SelectedItem = Null
					OtherOpen = Null
					ClosedInv=True
					
					MoveMouse Viewport_Center_X, Viewport_Center_Y
				Else
					
					If PrevOtherOpen\SecondInv[MouseSlot] = Null
						For z = 0 To OtherSize - 1
							If PrevOtherOpen\SecondInv[z] = SelectedItem
								PrevOtherOpen\SecondInv[z] = Null
								Exit
							EndIf
						Next
						PrevOtherOpen\SecondInv[MouseSlot] = SelectedItem
						SelectedItem = Null
					ElseIf PrevOtherOpen\SecondInv[MouseSlot] <> SelectedItem
						PrevItem = PrevOtherOpen\SecondInv[MouseSlot]
						
						Select SelectedItem\itemtemplate\tempname
							Default
								;[Block]
								For z = 0 To OtherSize - 1
									If PrevOtherOpen\SecondInv[z] = SelectedItem
										PrevOtherOpen\SecondInv[z] = PrevItem
										Exit
									EndIf
								Next
								PrevOtherOpen\SecondInv[MouseSlot] = SelectedItem
								SelectedItem = Null
								;[End Block]
						End Select
					EndIf
				EndIf
				SelectedItem = Null
			EndIf
		EndIf
		
		If (ClosedInv) And (Not InvOpen) Then
			ResumeSounds()
			ResetInput()
			OtherOpen=Null
		EndIf
		;[End Block]
		
	ElseIf InvOpen Then ; ~ Inventory has been open
		
		; ~ Beginning Task for keycard
		
		;If TaskExists(TASK_OPEN_INV) Then
		;	EndTask(TASK_OPEN_INV)
		;EndIf
		
		Local keyhits[MaxGunSlots]
		For i = 0 To MaxGunSlots-1
			keyhits[i] = KeyHit(i + 2)
		Next
		
		If (PlayerRoom\RoomTemplate\Name = "gate_a_topside") Then
			HideEntity Fog
			CameraFogRange Camera, 5,30
			CameraFogColor (Camera,200,200,200)
			CameraClsColor (Camera,200,200,200)					
			CameraRange(Camera, 0.01, 30)
		ElseIf (PlayerRoom\RoomTemplate\Name = "gate_b_topside") Then
			HideEntity Fog
			CameraFogRange Camera, 5,45
			CameraFogColor (Camera,200,200,200)
			CameraClsColor (Camera,200,200,200)					
			CameraRange(Camera, 0.01, 60)
		ElseIf (PlayerRoom\RoomTemplate\Name = "gate_a_intro") Then
			CameraFogRange Camera, 5,30
			CameraFogColor (Camera,200,200,200)
			CameraClsColor (Camera,200,200,200)					
			CameraRange(Camera, 0.005, 100)
		EndIf
		
		d_I\SelectedDoor = Null
		
		width% = 70
		height% = 70
		Spacing% = 1
		
		x = opt\GraphicWidth / 2 - (width * 9 / 3 + Spacing * (9 / 3 - 1)) / 2
		If Inventory[SLOT_BACKPACK] <> Null And Inventory[SLOT_BACKPACK]\itemtemplate\tempname = "backpack" Then
			y = opt\GraphicHeight / 2 - (height * 30 / 2.5 + Spacing * (30 / 2.5 - 1)) / 2
		Else
			y = opt\GraphicHeight / 2 - (height * 9 / 3 + Spacing * (9 / 3 - 1)) / 2
		EndIf
		
		Local fx2 = opt\GraphicWidth / 2 - (width * 3 + Spacing * 2) / 2
		Local fy2 = opt\GraphicHeight / 2 - (height * 3 + Spacing * 2) / 2
		
		Local isOverQuickSelection% = 0
		Local isOverQuickHolsterSelection% = 0
		
		Local width2# = 70
		Local height2# = 70
		Local spacing2# = 350
		
		x2 = opt\GraphicWidth / 2 - (width2 * 9 / 2 + spacing2 * (9 / 2 - 1)) / 2
		y2 = opt\GraphicHeight / 2 - height2 + height2*4
		
		x2=x2+width2 + spacing2
		
		;! ~ [Item Slots]
		
		ItemAmount = 0
		For  n% = 0 To MaxItemAmount - 1
			IsMouseOn% = False
			
			; ~ [Inventory Frames]
			
			If n < SLOT_HEAD Then
				If MouseOn(x+width*6, y, width, height) Then
					IsMouseOn = True
					MouseSlot = n
				EndIf
			ElseIf n > SLOT_SCABBARD Then
				If MouseOn(x+width*6, y  - height/0.7 , width, height) Then
					IsMouseOn = True
					MouseSlot = n
				EndIf
			ElseIf n = SLOT_HEAD Then ; ~ Head
				If MouseOn(fx2 + width*0.4, fy2 - height*1.45, width, height) Then
					IsMouseOn = True
					MouseSlot = n
				EndIf
			ElseIf n = SLOT_TORSO Then ; ~ Torso
				If MouseOn(fx2 + width*0.4, fy2 + height/1.45, width, height) Then
					IsMouseOn = True
					MouseSlot = n
				EndIf
			ElseIf n = SLOT_BACKPACK Then ; ~ Backpack
				If MouseOn(fx2 + width*2, fy2 - height*0.1, width, height) Then
					IsMouseOn = True
					MouseSlot = n
				EndIf
			ElseIf n = SLOT_PRIMARY Then ; ~ Primary Weapon Slot
				If MouseOn(fx2-width*2.7, fy2-height*1.2, width2*2, height2*5) Then
					IsMouseOn = True
					MouseSlot = n
				EndIf
			ElseIf n = SLOT_SECONDARY Then ; ~ Secondary Weapon Slot
				If MouseOn(fx2+width*3.5, fy2-height*1.2, width2*2, height2*5) Then
					IsMouseOn = True
					MouseSlot = n
				EndIf
			ElseIf n = SLOT_HOLSTER Then ; ~ Holster Weapon Slot
				If MouseOn(fx2-width*2.1, fy2+height*4.4, width, height) Then
					IsMouseOn = True
					MouseSlot = n
				EndIf
			ElseIf n = SLOT_SCABBARD Then ; ~ Scabbard Weapon Slot
				If MouseOn(fx2+width*3.9, fy2+height*4.4, width, height) Then
					IsMouseOn = True
					MouseSlot = n
				EndIf
			EndIf
			
			; ~ [End]
			
			If Inventory[n] <> Null And SelectedItem <> Inventory[n] Then
				If IsMouseOn Then
					If SelectedItem = Null Then
						If MouseHit1 Then
							SelectedItem = Inventory[n]
							MouseHit1 = False
							
							If DoubleClick Then
								If Inventory[n]\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[Inventory[n]\itemtemplate\sound])
								InvOpen = False
								DoubleClick = False
							EndIf
						EndIf
						
						x2# = x
						y2# = y+height
						width2# = width/3
						height2# = height/3
						
					EndIf
				EndIf
				
				ItemAmount=ItemAmount+1
			Else
				If IsMouseOn And MouseHit1 Then
					For z% = 0 To MaxItemAmount - 1
						If Inventory[z] = SelectedItem Then Inventory[z] = Null : Exit
					Next
					Inventory[n] = SelectedItem
					SelectedItem = Null
				EndIf
				
			EndIf
			
			x = x + width + Spacing
			
			; ~ [Inventory Frames Division]
			
			If n = 2 Lor n = 5 Then
				y = y + height + Spacing
				x = opt\GraphicWidth / 2 - (width * 9 / 3 + Spacing * (9 / 3 - 1)) / 2
			ElseIf n = 15 Lor n = 18 Lor n = 21 Lor n = 24 Lor n = 27 Lor n = 30 Lor n = 33 Lor n = 36 Then
				y = y + height + Spacing
				x = opt\GraphicWidth / 2 - (width * 9 / 3 + Spacing * (9 / 3 - 1)) / 2
			ElseIf n = SLOT_HEAD Then
				x = opt\GraphicWidth / 2 - (width * 9 / 3 + Spacing * (MaxItemAmount / 3 - 1)) / 2
				y = opt\GraphicHeight / 2 - (height * 9 / 3 + Spacing * (9 / 3 - 1)) / 2
			EndIf
			
			; ~ [End]
			
			width2# = 70
			height2# = 70
			spacing2# = 35;140
			
			x2# = opt\GraphicWidth / 2
			y2# = opt\GraphicHeight / 2 + height2*2.6
			
			x2 = opt\GraphicWidth / 2 - (width2 * 9 /2 + spacing2 * (9 / 2 - 1)) / 2
			y2 = opt\GraphicHeight / 2 - height2 + height2*4
			
			x2=x2+width2 + spacing2
			
			x2=x2+width2 + spacing2
			
			x2=x2+width2 + spacing2
			
		Next
		
		If SelectedItem <> Null Then
			
			If MouseDown1 Then
				; ~ TODO Interface when item is held on top of slot
			Else
				
				If MouseSlot = 66 Then
					
					DropItem(SelectedItem)
					InvOpen = False
					MoveMouse Viewport_Center_X, Viewport_Center_Y
					SelectedItem = Null
					
				Else
					
					;! ~ [Inventory Slot Logic]
					
					If Inventory[MouseSlot] = Null Then ; ~ If Hovered Inventory Slot Is Empty
						
						; ~ (Head Slot Logic)
						If MouseSlot = SLOT_HEAD And SelectedItem\itemtemplate\IsHead And (Not Inventory[SLOT_TORSO] <> Null And Inventory[SLOT_TORSO]\itemtemplate\IsFullBody) Then
							PlaySound_Strict(g_I\UI_Select_SFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory[z] = SelectedItem Then Inventory[z] = Null : Exit
							Next
							Inventory[MouseSlot] = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = SLOT_HEAD And (Not SelectedItem\itemtemplate\IsHead) Then
							PlaySound_Strict(g_I\UI_Deny_SFX)
						ElseIf MouseSlot = SLOT_HEAD And SelectedItem\itemtemplate\IsHead And Inventory[SLOT_TORSO] <> Null And Inventory[SLOT_TORSO]\itemtemplate\IsFullBody Then
							PlaySound_Strict(g_I\UI_Deny_SFX)
							CreateHintMsg(GetLocalStringR("Inventory", "remove_torso",Inventory[SLOT_TORSO]\itemtemplate\name))
						; ~ (Torso Slot Logic)
						ElseIf MouseSlot = SLOT_TORSO And SelectedItem\itemtemplate\IsTorso And (Not SelectedItem\itemtemplate\IsFullBody And Inventory[SLOT_HEAD] <> Null) Then
							PlaySound_Strict(g_I\UI_Select_SFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory[z] = SelectedItem Then Inventory[z] = Null : Exit
							Next
							Inventory[MouseSlot] = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = SLOT_TORSO And (Not SelectedItem\itemtemplate\IsTorso) Then
							PlaySound_Strict(g_I\UI_Deny_SFX)
						ElseIf MouseSlot = SLOT_TORSO And SelectedItem\itemtemplate\IsTorso And SelectedItem\itemtemplate\IsFullBody And Inventory[SLOT_HEAD] <> Null Then
							PlaySound_Strict(g_I\UI_Deny_SFX)
							CreateHintMsg(GetLocalStringR("Inventory", "remove_head",Inventory[SLOT_HEAD]\itemtemplate\name))
						; ~ (Backpack Slot Logic)	
						ElseIf MouseSlot = SLOT_BACKPACK And SelectedItem\itemtemplate\IsBackpack Then
							PlaySound_Strict(g_I\UI_Select_SFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory[z] = SelectedItem Then Inventory[z] = Null : Exit
							Next
							Inventory[MouseSlot] = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = SLOT_BACKPACK And (Not SelectedItem\itemtemplate\IsBackpack) Then
							PlaySound_Strict(g_I\UI_Deny_SFX)
						; ~ (Primary Weapon Slot Logic)	
						ElseIf MouseSlot = SLOT_PRIMARY And SelectedItem\itemtemplate\IsGun Then
							PlaySound_Strict(g_I\UI_Select_SFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory[z] = SelectedItem Then Inventory[z] = Null : Exit
							Next
							g_I\Weapon_InSlot[QUICKSLOT_PRIMARY] = SelectedItem\itemtemplate\tempname
							Inventory[MouseSlot] = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = SLOT_PRIMARY And (Not SelectedItem\itemtemplate\IsGun) Then
							PlaySound_Strict(g_I\UI_Deny_SFX)
						; ~ (Secondary Weapon Slot Logic)	
						ElseIf MouseSlot = SLOT_SECONDARY And SelectedItem\itemtemplate\IsGun Then
							PlaySound_Strict(g_I\UI_Select_SFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory[z] = SelectedItem Then Inventory[z] = Null : Exit
							Next
							g_I\Weapon_InSlot[QUICKSLOT_SECONDARY] = SelectedItem\itemtemplate\tempname
							Inventory[MouseSlot] = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = SLOT_SECONDARY And (Not SelectedItem\itemtemplate\IsGun) Then
							PlaySound_Strict(g_I\UI_Deny_SFX)
						; ~ (Holster Weapon Slot Logic)	
						ElseIf MouseSlot = SLOT_HOLSTER And (SelectedItem\itemtemplate\IsGun And SelectedItem\itemtemplate\IsForHolster) Then
							PlaySound_Strict(g_I\UI_Select_SFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory[z] = SelectedItem Then Inventory[z] = Null : Exit
							Next
							g_I\Weapon_InSlot[QUICKSLOT_HOLSTER] = SelectedItem\itemtemplate\tempname
							Inventory[MouseSlot] = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = SLOT_HOLSTER And (Not SelectedItem\itemtemplate\IsForHolster) Then
							PlaySound_Strict(g_I\UI_Deny_SFX)
						; ~ (Scabbard Weapon Slot Logic)	
						ElseIf MouseSlot = SLOT_SCABBARD And (SelectedItem\itemtemplate\IsGun And SelectedItem\itemtemplate\IsForScabbard) Then
							PlaySound_Strict(g_I\UI_Select_SFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory[z] = SelectedItem Then Inventory[z] = Null : Exit
							Next
							g_I\Weapon_InSlot[QUICKSLOT_SCABBARD] = SelectedItem\itemtemplate\tempname
							Inventory[MouseSlot] = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = SLOT_SCABBARD And (Not SelectedItem\itemtemplate\IsForScabbard) Then
							PlaySound_Strict(g_I\UI_Deny_SFX)
						; ~ (Backpack Inventory Slots Logic)	
						ElseIf MouseSlot > SLOT_SCABBARD And (Not SelectedItem\itemtemplate\IsBackpack) Then
							For z% = 0 To MaxItemAmount - 1
								If Inventory[z] = SelectedItem Then Inventory[z] = Null : Exit
							Next
							Inventory[MouseSlot] = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot > SLOT_SCABBARD And SelectedItem\itemtemplate\IsBackpack Then
							PlaySound_Strict(g_I\UI_Deny_SFX)
						; ~ (Other Slots Logic)	
						Else
							For z% = 0 To MaxItemAmount - 1
								If Inventory[z] = SelectedItem Then Inventory[z] = Null : Exit
							Next
							Inventory[MouseSlot] = SelectedItem
							SelectedItem = Null
						EndIf
						
					;! ~ [End]
						
					ElseIf Inventory[MouseSlot] <> SelectedItem ; ~ If Hovered Inventory Slot Has Other Item
						
						PrevItem = Inventory[MouseSlot]
						
						Select SelectedItem\itemtemplate\tempname
							Case "scopebat"
								;[Block]
								For g.Guns = Each Guns
									If g\name$ = Inventory[MouseSlot]\itemtemplate\tempname$
										If g\HasAttachments[ATT_ACOG_SCOPE] And g\ScopeCharge# >= ScopeChargeTime# Then
											RemoveItem(SelectedItem)
											g\ScopeCharge# = 0.0
											CreateMsg(GetLocalStringR("Items", "scope_battery_1",Inventory[MouseSlot]\itemtemplate\name)+".")
										ElseIf g\ScopeCharge# <= ScopeChargeTime# Then
											CreateMsg(GetLocalString("Items", "scope_battery_2"))
										Else
											CreateMsg(GetLocalString("Items", "scope_battery_3"))
										EndIf
									EndIf
								Next
								;[End Block]
							Case "ammocrate","bigammocrate"
								;[Block]
								If Inventory[MouseSlot]\itemtemplate\IsGun = True Then
									If Inventory[MouseSlot]\itemtemplate\tempname<>"crowbar" And Inventory[MouseSlot]\itemtemplate\tempname<>"knife" And Inventory[MouseSlot]\itemtemplate\tempname<>"grenade" And Inventory[MouseSlot]\itemtemplate\tempname<>"scp127" Then
										For g = Each Guns
											If g_I\HoldingGun = g\ID Then
												If g\CurrReloadAmmo < g\MaxReloadAmmo Then
													If SelectedItem\itemtemplate\tempname = "ammocrate" Then
														g\CurrReloadAmmo = Min(g\CurrReloadAmmo+g\MaxReloadAmmo*2,g\MaxReloadAmmo)
													Else
														g\CurrReloadAmmo = Min(g\CurrReloadAmmo+g\MaxReloadAmmo*4,g\MaxReloadAmmo)
													EndIf
													CreateMsg(GetLocalString("Weapons","ammo_picked"))
													RemoveItem(SelectedItem)
													SelectedItem = Null
													Inventory[MouseSlot]\state = 100.0
												Else
													CreateMsg(GetLocalString("Weapons","enough_ammo"))
												EndIf
											EndIf
										Next
									EndIf
								EndIf
								;[End Block]
							Case "emrp_mag"
								;[Block]
								If Inventory[MouseSlot]\itemtemplate\IsGun = True Then
									If Inventory[MouseSlot]\itemtemplate\tempname = "emrp" Then
										For g = Each Guns
											If g_I\HoldingGun = g\ID Then
												If g\CurrReloadAmmo < g\MaxReloadAmmo Then
													g\CurrReloadAmmo = Min(g\CurrReloadAmmo+10,g\MaxReloadAmmo)
													CreateMsg(GetLocalString("Items","emrp_mag_picked"))
													RemoveItem(SelectedItem)
													SelectedItem = Null
													Inventory[MouseSlot]\state = 100.0
												Else
													CreateMsg(GetLocalString("Items","cant_pick_emrp_mag"))
												EndIf
											EndIf
										Next
									EndIf
								EndIf
								;[End Block]
							Case "paper", "oldpaper", "origami", "candy", "key0", "key1", "key2", "key3", "key4", "key5", "key6", "key_class_d", "key_cave", "key_cave2", "playcard", "mastercard", "badge", "oldbadge", "ticket", "25ct", "coin", "key", "scp860", "scp714", "ring", "scp500pill", "scp500pilldeath", "pill", "fuse", "scope_bat", "scp005"
								;[Block]
								If Inventory[MouseSlot]\itemtemplate\tempname = "clipboard"
									; ~ Add an item to clipboard
									Local added.Items = Null
									Local b$ = SelectedItem\itemtemplate\tempname
									Local c%, ri%
									
									If b <> "25ct" And b <> "coin" And b <> "key" And b <> "scp860" And b <> "scp714" And b <> "coarse714" And b <> "fine714" And b <> "ring" And b <> "scp500pill" And b <> "scp500pilldeath" And b <> "pill" And b <> "fuse" And b <> "scp005"
										For c = 0 To Inventory[MouseSlot]\invSlots - 1
											If Inventory[MouseSlot]\SecondInv[c] = Null
												If SelectedItem <> Null
													Inventory[MouseSlot]\SecondInv[c] = SelectedItem
													Inventory[MouseSlot]\state = 1.0
													SetAnimTime(Inventory[MouseSlot]\model, 0.0)
													Inventory[MouseSlot]\invimg = Inventory[MouseSlot]\itemtemplate\invimg
													
													For ri = 0 To MaxItemAmount - 1
														If Inventory[ri] = SelectedItem
															Inventory[ri] = Null
															PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
															Exit
														EndIf
													Next
													added = SelectedItem
													SelectedItem = Null
													Exit
												EndIf
											EndIf
										Next
										If SelectedItem <> Null Then
											CreateMsg(GetLocalString("Items", "clipboard_notstrong"))
										Else
											If added\itemtemplate\tempname = "paper" Lor added\itemtemplate\tempname = "oldpaper" Then
												CreateMsg(GetLocalString("Items", "clipboard_added_1"))
											ElseIf added\itemtemplate\tempname = "badge"
												CreateMsg(GetLocalStringR("Items", "clipboard_added_2",added\itemtemplate\name))
											Else
												CreateMsg(GetLocalStringR("Items", "clipboard_added_3",added\itemtemplate\name))
											EndIf
										EndIf
									Else
										UpdateItemDetermination(MouseSlot,PrevItem)
									EndIf
								ElseIf Inventory[MouseSlot]\itemtemplate\tempname = "wallet"
									; ~ Add an item to wallet
									added.Items = Null
									b = SelectedItem\itemtemplate\tempname
									If b <> "paper" And b <> "oldpaper" And b <> "origami"
										If (SelectedItem\itemtemplate\tempname = "scp714" And I_714\Using) Then
											CreateMsg((GetLocalString("Items", "take_off")))
											SelectedItem = Null
											Return
										EndIf
										
										For c = 0 To Inventory[MouseSlot]\invSlots - 1
											If Inventory[MouseSlot]\SecondInv[c] = Null
												Inventory[MouseSlot]\SecondInv[c] = SelectedItem
												Inventory[MouseSlot]\state = 1.0
												; ~ TODO!!!
												;If b <> "25ct" And b <> "coin" And b <> "key" And b <> "scp860" And b <> "scp714" And b <> "coarse714" And b <> "scp500pill" And b <> "scp500pilldeath" And b <> "pill" Then SetAnimTime(Inventory[MouseSlot]\model, 3.0)
												Inventory[MouseSlot]\invimg = Inventory[MouseSlot]\itemtemplate\invimg
												
												For ri = 0 To MaxItemAmount - 1
													If Inventory[ri] = SelectedItem
														Inventory[ri] = Null
														PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])
														Exit
													EndIf
												Next
												added = SelectedItem
												SelectedItem = Null
												Exit
											EndIf
										Next
										If SelectedItem <> Null
											CreateMsg(GetLocalString("Items", "wallet_full"))
										Else
											CreateMsg(GetLocalStringR("Items", "wallet_added", added\itemtemplate\name))
										EndIf
									Else
										UpdateItemDetermination(MouseSlot,PrevItem)
									EndIf
								Else
									UpdateItemDetermination(MouseSlot,PrevItem)
								EndIf
								SelectedItem = Null
								;[End Block]
							Case "battery", "bat"
								;[Block]
								Select Inventory[MouseSlot]\itemtemplate\name
									Case "S-NAV Navigator", "S-NAV 300 Navigator", "S-NAV 310 Navigator"
										If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])	
										RemoveItem (SelectedItem)
										SelectedItem = Null
										Inventory[MouseSlot]\state = 100.0
										CreateMsg(GetLocalString("Items", "battery_nav"))
									Case "S-NAV Navigator Ultimate"
										CreateMsg(GetLocalString("Items", "battery_nav_noplace"))
									Case "Radio Transceiver"
										Select Inventory[MouseSlot]\itemtemplate\tempname 
											Case "fineradio", "veryfineradio"
												CreateMsg(GetLocalString("Items", "battery_radio_noplace"))
											Case "18vradio"
												CreateMsg(GetLocalString("Items", "battery_radio_18v"))
											Case "radio"
												If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])	
												RemoveItem (SelectedItem)
												SelectedItem = Null
												Inventory[MouseSlot]\state = 100.0
												CreateMsg(GetLocalString("Items", "battery_radio"))
										End Select
									Case "Night Vision Goggles"
										Local nvname$ = Inventory[MouseSlot]\itemtemplate\tempname
										If nvname$="nvg" Lor nvname$="nvg2" Then
											If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])	
											RemoveItem (SelectedItem)
											SelectedItem = Null
											Inventory[MouseSlot]\state = 1000.0
											CreateMsg(GetLocalString("Items", "battery_nvg"))
										Else
											CreateMsg(GetLocalString("Items", "battery_nvg_noplace"))
										EndIf
									Default
										UpdateItemDetermination(MouseSlot,PrevItem)
								End Select
								SelectedItem = Null
								;[End Block]
							Case "18vbat"
								;[Block]
								Select Inventory[MouseSlot]\itemtemplate\name
									Case "S-NAV Navigator", "S-NAV 300 Navigator", "S-NAV 310 Navigator"
										CreateMsg(GetLocalString("Items", "battery_nav_18v"))
									Case "S-NAV Navigator Ultimate"
										CreateMsg(GetLocalString("Items", "battery_nav_noplace"))
									Case "Radio Transceiver"
										Select Inventory[MouseSlot]\itemtemplate\tempname 
											Case "fineradio", "veryfineradio"
												CreateMsg(GetLocalString("Items", "battery_radio_noplace"))
											Case "18vradio"
												If SelectedItem\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[SelectedItem\itemtemplate\sound])	
												RemoveItem (SelectedItem)
												SelectedItem = Null
												Inventory[MouseSlot]\state = 100.0
												CreateMsg(GetLocalString("Items", "battery_radio"))
										End Select 
									Default
										UpdateItemDetermination(MouseSlot,PrevItem)
								End Select
								SelectedItem = Null
								;[End Block]
							Default
								;[Block]
								UpdateItemDetermination(MouseSlot,PrevItem)
								SelectedItem = Null
								;[End Block]
						End Select
					EndIf
					
				EndIf
				SelectedItem = Null
			EndIf
		Else
			If MouseHit1 Then
				;
			EndIf
		EndIf
		
		If InvOpen = False Then 
			ResumeSounds()
			ResetInput()
		EndIf
		
	EndIf
	
End Function

Function UpdateItemInSlotProperties()
	Local p%, g.Guns
	
	;! ~ [Definition Of Item Usage In Slots]
	
	; ~ [Head Slot]
	
	If Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 3) = "nvg" And Inventory[SLOT_HEAD]\state > 0 Then
		If StoredCameraFogFar = 0 Then
			StoredCameraFogFar = CameraFogFar
		EndIf
		CameraFogFar = 30
	Else
		CameraFogFar = StoredCameraFogFar
	EndIf
	
	If Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "scp268" Then
		GiveAchievement(Achv268)
		PlaySound_Strict I_268\Sound[0]
	Else
		If ChannelPlaying(I_268\SoundCHN[0]) = False Then PlaySound_Strict I_268\Sound[1]
	EndIf
	
	If Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet" Then
		psp\Helmet = Inventory[SLOT_HEAD]\state
	Else
		psp\Helmet = 0
	EndIf
	
	; ~ [Torso Slot]
	
	If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds" Then
		hds\Health = Inventory[SLOT_TORSO]\state
	Else
		hds\Health = 0
	EndIf
	
	If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 4) = "vest" Then
		psp\Kevlar = Inventory[SLOT_TORSO]\state
	Else
		psp\Kevlar = 0
	EndIf
	
	If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat" Then
		;psp\Oxygen = Inventory[SLOT_TORSO]\State
	Else
		;psp\Oxygen = 0
	EndIf
	
	; ~ [Backpack Slot]
	
	If Inventory[SLOT_BACKPACK] <> Null And Inventory[SLOT_BACKPACK]\itemtemplate\tempname = "backpack" Then
		MaxItemAmount = 37
	Else
		MaxItemAmount = 16
	EndIf
	
	; ~ [Weapon Slots]
	
	For p = SLOT_PRIMARY To SLOT_SCABBARD
		If Inventory[p] <> Null Then
			g_I\Weapon_InSlot[p-SLOT_PRIMARY] = Inventory[p]\itemtemplate\tempname
		Else
			For g = Each Guns
				If g\name = g_I\Weapon_InSlot[p-SLOT_PRIMARY] And g\ID = g_I\HoldingGun Then
					g_I\GunChangeFLAG = False
					g_I\HoldingGun = 0
					g_I\Weapon_CurrSlot = 0
					mpl\SlotsDisplayTimer = 0
					If g\IsSeparate Then
						HideEntity g\HandsObj
					EndIf
					PlayGunSound(g\name$+"\holster",1,1,False)
					If g\JamAmount >= g\MaxJams Then
						g\JamTimer = 0
					EndIf
					Exit
				EndIf
			Next
			g_I\Weapon_InSlot[p-SLOT_PRIMARY] = ""
		EndIf
	Next
	
End Function

Function UpdateItemDetermination(MouseSlotID%,PreviousItem.Items)
	Local z%, b%
	
	If MouseSlotID <> SLOT_HEAD And MouseSlotID <> SLOT_TORSO And MouseSlotID <> SLOT_BACKPACK And MouseSlotID <> SLOT_PRIMARY And MouseSlotID <> SLOT_SECONDARY And MouseSlotID <> SLOT_HOLSTER And MouseSlotID <> SLOT_SCABBARD Then
		If Inventory[SLOT_HEAD] <> SelectedItem And Inventory[SLOT_TORSO] <> SelectedItem And Inventory[SLOT_BACKPACK] <> SelectedItem And Inventory[SLOT_PRIMARY] <> SelectedItem And Inventory[SLOT_SECONDARY] <> SelectedItem And Inventory[SLOT_HOLSTER] <> SelectedItem And Inventory[SLOT_SCABBARD] <> SelectedItem Then
			For z = 0 To MaxItemAmount - 1
				If Inventory[z] = SelectedItem
					Inventory[z] = PreviousItem
					Exit
				EndIf
			Next
			Inventory[MouseSlotID] = SelectedItem
		Else
			PlaySound_Strict(g_I\UI_Deny_SFX)
		EndIf
	Else
		PlaySound_Strict(g_I\UI_Deny_SFX)
	EndIf
	
End Function

Function ClearInventory(withbackpack=False)
	Local invs%,ItemAmount
	
	If withbackpack Then
		ItemAmount = MaxInventorySpace
	Else
		ItemAmount = MaxItemAmount
	EndIf
	
	For invs = 0 To ItemAmount-1
		If Inventory[invs] <> Null Then
			RemoveItem(Inventory[invs])
		EndIf
	Next
		
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D