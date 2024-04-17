
Const MaxInventorySpace% = 16

Const SLOT_HEAD = 9
Const SLOT_TORSO = 10
Const SLOT_BACKPACK = 11

Const SLOT_PRIMARY = 12
Const SLOT_SECONDARY = 13
Const SLOT_HOLSTER = 14
Const SLOT_SCABBARD = 15

;Global WeaponSlots.Items[MaxGunSlots]

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
		
		;! ~ [Weapon Slots]
		
		Text x2+((width2+spacing2))-width*5.5+7.5,y2-height*6,GetLocalString("Inventory","slot_primary"),1,1
		Text x2+((width2+spacing2))+width/2+7.5,y2-height*6,GetLocalString("Inventory","slot_secondary"),1,1
		Text x2+((width2+spacing2))-width*5.5+10,y2-height/2-2,GetLocalString("Inventory","slot_holster"),1,1
		Text x2+((width2+spacing2))+width/2+10,y2-height/2-2,GetLocalString("Inventory","slot_scabbard"),1,1
		
		; ~ [Slots 1 and 2]
		
;		For i = 0 To MaxGunSlots-3
;			Local hasGunInSlot% = False
;			If g_I\Weapon_InSlot[i] <> "" Then
;				For g = Each Guns
;					If g\name = g_I\Weapon_InSlot[i] Then
;						If g_I\Weapon_CurrSlot = (i + 1) Then
;							Color 0,200,0
;							Rect x2+((width2+spacing2)*i)-width/2.5 - 2.5, y2-height*5.7 - 3, width2*2 + 6, height2*5 + 6
;						EndIf
;						Color 255,255,255
;						DrawFrame(x2+((width2+spacing2)*i)-width/2.5, y2-height*5.7, width2*2, height2*5, (x2 Mod 64), (x2 Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
;						DrawImage g\INV_IMG, (x2+((width2+spacing2)*i)) - width2/2.8 + 0.8 , y2 + height2 / 2 -height*6.14 - 1
;						hasGunInSlot = True
;						Exit
;					EndIf
;				Next
;			EndIf
;			If (Not hasGunInSlot) Then
;				DrawFrame(x2+((width2+spacing2)*i)-width/2.5, y2-height*5.7, width2*2, height2*5, (x2 Mod 64), (x2 Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
;			EndIf
;		Next
;		
		; ~ [Slots 3 and 4]
;		
;		For i = 2 To MaxGunSlots-1
;			hasGunInSlot% = False
;			If g_I\Weapon_InSlot[i] <> "" Then
;				For g = Each Guns
;					If g\name = g_I\Weapon_InSlot[i] Then
;						If g_I\Weapon_CurrSlot = (i + 1) Then
;							Color 0,200,0
;							Rect x2+((width2+spacing2)*i-2)-width*11.85 - 3, y2-height/2.8 - 3, width2 + 6, height2 + 6
;						EndIf
;						Color 255,255,255
;						DrawFrame(x2+((width2+spacing2)*i-2)-width*11.85, y2-height/2.8, width2, height2, (x2 Mod 64), (x2 Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
;						DrawImage g\IMG, (x2+((width2+spacing2)*i-2)) - width2*11.9 + 6.8, y2 - height2 / 2.8 + 3
;						hasGunInSlot = True
;						Exit
;					EndIf
;				Next
;			EndIf
;			If (Not hasGunInSlot) Then
;				DrawFrame(x2+((width2+spacing2)*i-2)-width*11.85, y2-height/2.8, width2, height2, (x2 Mod 64), (x2 Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
;			EndIf
;		Next
		
		;! ~ [Item Slots]
		
		For  n% = 0 To MaxItemAmount - 1
			IsMouseOn% = False
			
			;! ~ [Inventory Frames]
			
			If n < SLOT_HEAD Then
				If MouseOn(x+width*6, y, width, height) Then 
					IsMouseOn = True
					MouseSlot = n
					Color 255, 187, 0
					Rect(x+width*6 - 1, y - 1, width + 2, height + 2)
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
			If n < SLOT_HEAD Then
				DrawFrame(x+width*6, y, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
			ElseIf n = SLOT_HEAD Then ; ~ Head
				DrawFrame(fx2 + width*0.4, fy2 - height*1.45, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
			ElseIf n = SLOT_TORSO Then ; ~ Torso
				DrawFrame(fx2 + width*0.4, fy2 + height/1.45, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
			ElseIf n = SLOT_BACKPACK Then ; ~ Backpack
				DrawFrame(fx2 + width*2, fy2 - height*0.1, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
			ElseIf n = SLOT_PRIMARY Then ; ~ Primary Weapon Slot
				DrawFrame(fx2-width*2.7, fy2-height*1.2, width2*2, height2*5, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
			ElseIf n = SLOT_SECONDARY Then ; ~ Secondary Weapon Slot
				DrawFrame(fx2+width*3.5, fy2-height*1.2, width2*2, height2*5, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
			ElseIf n = SLOT_HOLSTER Then ; ~ Holster Weapon Slot
				DrawFrame(fx2-width*2.1, fy2+height*4.4, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
			ElseIf n = SLOT_SCABBARD Then ; ~ Scabbard Weapon Slot
				DrawFrame(fx2+width*3.9, fy2+height*4.4, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
			EndIf
			
			If Inventory[n] <> Null Then
				
				Local hasGunInSlot% = False
				
				If (SelectedItem <> Inventory[n] Lor IsMouseOn) Then
					If n < SLOT_HEAD Then
						DrawImage(Inventory[n]\invimg, x + width / 2 + width*6 - 32, y + height / 2 - 32)
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
									If g_I\Weapon_CurrSlot = QUICKSLOT_PRIMARY-1 Then
										Color 0,200,0
										Rect(fx2-width*2.7 - 1, fy2-height*1.2 - 1, width2*2 + 3, height2*5 + 3)
									EndIf
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
									If g_I\Weapon_CurrSlot = QUICKSLOT_SECONDARY-1 Then
										Color 0,200,0
										Rect(fx2+width*3.5 - 1, fy2-height*1.2 - 1, width2*2 + 3, height2*5 + 3)
									EndIf
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
									If g_I\Weapon_CurrSlot = QUICKSLOT_HOLSTER-1 Then
										Color 0,200,0
										Rect(fx2-width*2.1 - 1, fy2+height*4.4 - 1, width + 3, height + 3)
									EndIf
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
									If g_I\Weapon_CurrSlot = QUICKSLOT_SCABBARD-1 Then
										Color 0,200,0
										Rect(fx2+width*3.9 - 1, fy2+height*4.4 - 1, width + 3, height + 3)
									EndIf
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
						;width2# = width/3
						;height2# = height/3
						
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
		
		;! ~ [Player Wearing Backpack]
		
		If Inventory[SLOT_BACKPACK] <> Null And Inventory[SLOT_BACKPACK]\itemtemplate\tempname = "backpack" Then
			;[Block]
			
			x = opt\GraphicWidth / 2 - (width * 9 / 3 + Spacing * (9 / 3 - 1)) / 2
			
			OtherSize=Inventory[SLOT_BACKPACK]\invSlots
			
			;! ~ [Storage Slots]
			
			For n = 0 To OtherSize - 1
				
				If Inventory[SLOT_BACKPACK]\SecondInv[n] <> Null Then
					OtherAmount = OtherAmount + 1
				EndIf
				
				IsMouseOn% = False
				
			;! ~ [Storage Frames]
				
				If MouseOn(x+width*6, y - height/2, width, height) Then 
					IsMouseOn = True
					MouseSlot = n
					Color 255, 187, 0
					Rect(x+width*6 - 1, y - height/2 - 1, width + 2, height + 2)
				EndIf
					
				Color 255, 255, 255
				DrawFrame(x+width*6, y - height/2, width, height, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
					
				If Inventory[SLOT_BACKPACK]\SecondInv[n] <> Null Then
					If (SelectedItem <> Inventory[SLOT_BACKPACK]\SecondInv[n] Lor IsMouseOn) Then
						DrawImage(Inventory[SLOT_BACKPACK]\SecondInv[n]\invimg, x + width / 2 + width*6 - 32, y - height/2 + height / 2 - 32)
					EndIf
				EndIf
				
			;! ~ [Storage Item Display Name]
				
				If Inventory[SLOT_BACKPACK]\SecondInv[n] <> Null And SelectedItem <> Inventory[SLOT_BACKPACK]\SecondInv[n] Then
					If IsMouseOn Then
						If SelectedItem = Null
							
							spacing2# = 0
							x2# = fx
							y2# = fy+height
							width2# = width/3
							height2# = height/3
							
							; ~ [Display Text]
							
							SetFont fo\Font[Font_Default]
							
							DrawFrame(fx+width*5, fy + height*2.5, width*3 + 1, height/2, (x Mod 64), (x Mod 64),1024,1024,FRAME_THICK,256,False,255,5,5,5,20,20,20)
							
							Color 0,0,0
							Text(fx + width / 2 +width*6 + 1, fy + height + Spacing + spacing2 - 15+height*1.85 + 1, Inventory[SLOT_BACKPACK]\SecondInv[n]\name, True)
							Color 255, 255, 255	
							Text(fx + width / 2 +width*6, fy + height + Spacing + spacing2 - 15+height*1.85, Inventory[SLOT_BACKPACK]\SecondInv[n]\name, True)
							
						EndIf
					EndIf
				Else
					
				EndIf
				
				x=x+width + Spacing
				
			;! ~ [Storage Frames Division]
				
				If n = 2 Lor n = 5 Lor n = 8 Lor n = 11 Lor n = 14 Lor n = 17 Lor n = 20 Then
					y = y + height + Spacing
					x = opt\GraphicWidth / 2 - (width * 9 / 3 + Spacing * (9 / 3 - 1)) / 2
				EndIf
				
			;! ~ [End]
				
			Next
			
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
			
			;[End Block]
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
			
			DebugLog "otheropen: "+(OtherOpen<>Null)
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
		
		If TaskExists(TASK_OPEN_INV) Then
			EndTask(TASK_OPEN_INV)
		EndIf
		
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
		
		;! ~ [Weapon Slots]
		
		; ~ [Slots 1 and 2]
		
		For i = 0 To MaxGunSlots-3
			If MouseOn(x2+((width2+spacing2)*i)-width/2.5, y2-height*5.7, width2*2, height2*5) Then
				isOverQuickSelection = (i + 1)
				Exit
			EndIf
		Next
		
		; ~ [Slots 3 and 4]
		
		For i = 2 To MaxGunSlots-1
			If MouseOn(x2+((width2+spacing2)*i-2)-width*11.85, y2-height/2.8, width2, height2) Then
				isOverQuickHolsterSelection = (i + 1)
				Exit
			EndIf
		Next
		
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
			ElseIf n = SLOT_HEAD Then
				x = opt\GraphicWidth / 2 - (width * 9 / 3 + Spacing * (9 / 3 - 1)) / 2
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
		
		;! ~ [Player Wearing Backpack]
		
		If Inventory[SLOT_BACKPACK] <> Null And Inventory[SLOT_BACKPACK]\itemtemplate\tempname = "backpack" Then
			;[Block]
			
			x = opt\GraphicWidth / 2 - (width * 9 / 3 + Spacing * (9 / 3 - 1)) / 2
			
			OtherSize=Inventory[SLOT_BACKPACK]\invSlots
			
			;! ~ [Storage Slots]
			
			For n = 0 To OtherSize - 1
				
				If Inventory[SLOT_BACKPACK]\SecondInv[n] <> Null Then
					OtherAmount = OtherAmount + 1
				EndIf
				
				IsMouseOn% = False
				
				;! ~ [Storage Frames]
				
				If MouseOn(x+width*6, y - height/2, width, height) Then
					IsMouseOn = True
					MouseSlot = n
				EndIf
				
				If Inventory[SLOT_BACKPACK]\SecondInv[n] <> Null And SelectedItem <> Inventory[SLOT_BACKPACK]\SecondInv[n] Then
					If IsMouseOn Then
						If SelectedItem = Null Then
							If MouseHit1 Then
								SelectedItem = Inventory[SLOT_BACKPACK]\SecondInv[n]
								MouseHit1 = False
								
								If DoubleClick Then
									If Inventory[SLOT_BACKPACK]\SecondInv[n]\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[Inventory[SLOT_BACKPACK]\SecondInv[n]\itemtemplate\sound])
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
					
					ItemAmount = ItemAmount + 1
				Else
					
					If IsMouseOn And MouseHit1 Then
						If Inventory[SLOT_BACKPACK]\SecondInv[n] = SelectedItem Then Inventory[SLOT_BACKPACK]\SecondInv[n] = Null : Exit
						Inventory[SLOT_BACKPACK]\SecondInv[n] = SelectedItem
						SelectedItem = Null
					EndIf
					
				EndIf
				
				x = x + width + Spacing
				
				;! ~ [Storage Frames Division]
				
				If n = 2 Lor n = 5 Lor n = 8 Lor n = 11 Lor n = 14 Lor n = 17 Lor n = 20 Then
					y = y + height + Spacing
					x = opt\GraphicWidth / 2 - (width * 9 / 3 + Spacing * (9 / 3 - 1)) / 2
				EndIf
				
				;! ~ [End]
				
			Next
			
			;[End Block]
		EndIf
		
		If SelectedItem <> Null Then
			
			If MouseDown1 Then
				; ~ TODO Interface when item is held on top of slot
			Else
				
				If MouseSlot = 66 Then
					
					;If (Not isOverQuickSelection) And (Not isOverQuickHolsterSelection) Then
						DropItem(SelectedItem)
						
						InvOpen = False
						
						MoveMouse Viewport_Center_X, Viewport_Center_Y
;					ElseIf isOverQuickSelection Then
;						If SelectedItem\itemtemplate\IsGun Then
;							If isOverQuickSelection = g_I\Weapon_CurrSlot Then
;								PlaySound_Strict g_I\UI_Deny_SFX
;							Else
;								g_I\Weapon_InSlot[isOverQuickSelection - 1] = SelectedItem\itemtemplate\tempname
;								
;								WeaponSlots[isOverQuickSelection - 1] = SelectedItem
;								For n = 0 To MaxItemAmount - 1
;									If Inventory[n] = SelectedItem Then
;										Inventory[n] = Null
;										Exit
;									EndIf
;								Next
;								
;								PlaySound_Strict g_I\UI_Select_SFX
;							EndIf
;							
;						Else
;							For n = 0 To MaxItemAmount - 1
;								If Inventory[n] <> SelectedItem Then
;									DropItem(SelectedItem)
;									Exit
;								EndIf
;							Next
;							CreateHintMsg(GetLocalString("Items", "cannot_non_weapon"))
;						EndIf
;					ElseIf isOverQuickHolsterSelection Then
;						If SelectedItem\itemtemplate\IsGun And SelectedItem\itemtemplate\IsForHolster Then
;							If isOverQuickHolsterSelection = g_I\Weapon_CurrSlot Then
;								PlaySound_Strict g_I\UI_Deny_SFX
;							Else
;								g_I\Weapon_InSlot[isOverQuickHolsterSelection - 1] = SelectedItem\itemtemplate\tempname
;								
;								WeaponSlots[isOverQuickHolsterSelection - 1] = SelectedItem
;								For n = 0 To MaxItemAmount - 1
;									If Inventory[n] = SelectedItem Then
;										Inventory[n] = Null
;										Exit
;									EndIf
;								Next
;								
;								PlaySound_Strict g_I\UI_Select_SFX
;							EndIf
;							
;						ElseIf SelectedItem\itemtemplate\IsGun And (Not SelectedItem\itemtemplate\IsForHolster) Then
;							For n = 0 To MaxItemAmount - 1
;								If Inventory[n] <> SelectedItem Then
;									DropItem(SelectedItem)
;									Exit
;								EndIf
;							Next
;							CreateHintMsg(GetLocalString("Items", "weapon_big"))
;						Else
;							For n = 0 To MaxItemAmount - 1
;								If Inventory[n] <> SelectedItem Then
;									DropItem(SelectedItem)
;									Exit
;								EndIf
;							Next
;							CreateHintMsg(GetLocalString("Items", "cannot_non_weapon"))
;						EndIf
;					EndIf
					
					SelectedItem = Null
					
				Else
					
					;! ~ [Inventory Slot Logic]
					
					If Inventory[MouseSlot] = Null Then ; ~ If Hovered Inventory Slot Is Empty
						
						; ~ (Head Slot Logic)
						If MouseSlot = SLOT_HEAD And SelectedItem\itemtemplate\IsHead Then
							PlaySound_Strict(g_I\UI_Select_SFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory[z] = SelectedItem Then Inventory[z] = Null : Exit
							Next
							Inventory[MouseSlot] = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = SLOT_HEAD And (Not SelectedItem\itemtemplate\IsHead) Then
							PlaySound_Strict(g_I\UI_Deny_SFX)
						; ~ (Torso Slot Logic)
						ElseIf MouseSlot = SLOT_TORSO And SelectedItem\itemtemplate\IsTorso Then
							PlaySound_Strict(g_I\UI_Select_SFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory[z] = SelectedItem Then Inventory[z] = Null : Exit
							Next
							Inventory[MouseSlot] = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = SLOT_TORSO And (Not SelectedItem\itemtemplate\IsTorso) Then
							PlaySound_Strict(g_I\UI_Deny_SFX)
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
						ElseIf MouseSlot = SLOT_SCABBARD And (SelectedItem\itemtemplate\IsGun And SelectedItem\itemtemplate\IsForHolster) Then
							PlaySound_Strict(g_I\UI_Select_SFX)
							For z% = 0 To MaxItemAmount - 1
								If Inventory[z] = SelectedItem Then Inventory[z] = Null : Exit
							Next
							g_I\Weapon_InSlot[QUICKSLOT_SCABBARD] = SelectedItem\itemtemplate\tempname
							Inventory[MouseSlot] = SelectedItem
							SelectedItem = Null
						ElseIf MouseSlot = SLOT_SCABBARD And (Not SelectedItem\itemtemplate\IsForHolster) Then
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
							Case "paper", "oldpaper", "origami", "key0", "key1", "key2", "key3", "key4", "key5", "key6", "keyomni", "playcard", "mastercard", "badge", "oldbadge", "ticket", "25ct", "coin", "key", "scp860", "scp714", "ring", "scp500pill", "scp500pilldeath", "pill"
								;[Block]
								If Inventory[MouseSlot]\itemtemplate\tempname = "clipboard"
									; ~ Add an item to clipboard
									Local added.Items = Null
									Local b$ = SelectedItem\itemtemplate\tempname
									Local c%, ri%
									
									If b <> "25ct" And b <> "coin" And b <> "key" And b <> "scp860" And b <> "scp714" And b <> "coarse714" And b <> "fine714" And b <> "ring" And b <> "scp500pill" And b <> "scp500pilldeath" And b <> "pill"
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
								ElseIf Inventory[MouseSlot]\itemtemplate\tempname = "backpack" Then
									; ~ Add an item to backpack
									
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
											CreateMsg(GetLocalString("Items", "backpack_full"))
										Else
											CreateMsg(GetLocalStringR("Items", "backpack_added", added\itemtemplate\name))
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
			
			; ~ [Torso Slot]
			
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
			
			;! ~ [Other Items]
			
			If MouseHit1 Then
;				If isOverQuickSelection Then
;					If WeaponSlots[isOverQuickSelection - 1] <> Null Then
;						
;						For g = Each Guns
;							If g\name = g_I\Weapon_InSlot[isOverQuickSelection - 1] And g\ID = g_I\HoldingGun Then
;								g_I\GunChangeFLAG = False
;								g_I\HoldingGun = 0
;								g_I\Weapon_CurrSlot = 0
;								mpl\SlotsDisplayTimer = 0
;								PlayGunSound(g\name$+"\holster",1,1,False)
;								Exit
;							EndIf
;						Next
;						SelectedItem = WeaponSlots[isOverQuickSelection - 1]
;						g_I\Weapon_InSlot[isOverQuickSelection - 1] = ""
;						WeaponSlots[isOverQuickSelection - 1] = Null
;						
;						PlaySound_Strict(g_I\UI_Deny_SFX)
;					EndIf
;				ElseIf isOverQuickHolsterSelection Then
;					If WeaponSlots[isOverQuickHolsterSelection - 1] <> Null Then
;						
;						For g = Each Guns
;							If g\name = g_I\Weapon_InSlot[isOverQuickHolsterSelection - 1] And g\ID = g_I\HoldingGun Then
;								g_I\GunChangeFLAG = False
;								g_I\HoldingGun = 0
;								g_I\Weapon_CurrSlot = 0
;								mpl\SlotsDisplayTimer = 0
;								PlayGunSound(g\name$+"\holster",1,1,False)
;								Exit
;							EndIf
;						Next
;						SelectedItem = WeaponSlots[isOverQuickHolsterSelection - 1]
;						g_I\Weapon_InSlot[isOverQuickHolsterSelection - 1] = ""
;						WeaponSlots[isOverQuickHolsterSelection - 1] = Null
;						
;						PlaySound_Strict(g_I\UI_Deny_SFX)
;					EndIf
;				EndIf
			EndIf
		EndIf
		
		If InvOpen = False Then 
			ResumeSounds()
			ResetInput()
		EndIf
		
	EndIf
	
End Function

Function UpdateItemDetermination(MouseSlotID%,PreviousItem.Items)
	Local z%
	
	; ~ (Head Slot Logic)
	If MouseSlotID = SLOT_HEAD And SelectedItem\itemtemplate\IsHead Then
		PlaySound_Strict(g_I\UI_Select_SFX)
		For z = 0 To MaxItemAmount - 1
			If Inventory[z] = SelectedItem
				Inventory[z] = PreviousItem
				Exit
			EndIf
		Next
		Inventory[MouseSlotID] = SelectedItem
	ElseIf MouseSlotID = SLOT_HEAD And (Not SelectedItem\itemtemplate\IsHead) Then
		PlaySound_Strict(g_I\UI_Deny_SFX)
	; ~ (Torso Slot Logic)	
	ElseIf MouseSlotID = SLOT_TORSO And SelectedItem\itemtemplate\IsTorso Then
		PlaySound_Strict(g_I\UI_Select_SFX)
		For z = 0 To MaxItemAmount - 1
			If Inventory[z] = SelectedItem
				Inventory[z] = PreviousItem
				Exit
			EndIf
		Next
		Inventory[MouseSlotID] = SelectedItem
	ElseIf MouseSlotID = SLOT_TORSO And (Not SelectedItem\itemtemplate\IsTorso) Then
		PlaySound_Strict(g_I\UI_Deny_SFX)
	; ~ (Backpack Slot Logic)
	ElseIf MouseSlotID = SLOT_BACKPACK And SelectedItem\itemtemplate\IsBackpack Then
		PlaySound_Strict(g_I\UI_Select_SFX)
		For z = 0 To MaxItemAmount - 1
			If Inventory[z] = SelectedItem
				Inventory[z] = PreviousItem
				Exit
			EndIf
		Next
		Inventory[MouseSlotID] = SelectedItem
	ElseIf MouseSlotID = SLOT_BACKPACK And (Not SelectedItem\itemtemplate\IsBackpack) Then
		PlaySound_Strict(g_I\UI_Deny_SFX)
	; ~ (Other Slots Logic)
	Else
		For z = 0 To MaxItemAmount - 1
			If Inventory[z] = SelectedItem
				Inventory[z] = PreviousItem
				Exit
			EndIf
		Next
		Inventory[MouseSlotID] = SelectedItem
	EndIf
	
End Function

; ~ OLD CODE FOR ITEM REMOVAL

;If MouseHit1 Then
;	
;				;Local IndexSlot% = -1
;	
;				;For n = 0 To MaxItemAmount-1
;				;	If Inventory[n] = Null Then
;				;		IndexSlot = n
;				;		Exit
;				;	EndIf
;				;Next
;				;If IndexSlot >= 0 Then
;	
;	If isOverHeadSlot Then
;		If wbl\GasMask > 0 Then
;			wbl\GasMask = 0
;			
;			SelectedItem = WearableSlots[SLOT_HEAD]
;							;Inventory[IndexSlot] = WearableSlots[SLOT_HEAD]
;			WearableSlots[SLOT_HEAD] = Null
;			
;			Msg = GetLocalString("Items", "gasmask_off") : MsgTimer = 70 * 5
;			PlaySound_Strict(g_I\UI_Deny_SFX)
;		ElseIf wbl\NVG > 0 Then
;			wbl\NVG = 0
;			CameraFogFar = StoredCameraFogFar
;			Msg = GetLocalString("Items", "nvg_off") : MsgTimer = 70 * 5
;			PlaySound_Strict(g_I\UI_Deny_SFX)
;		EndIf
;	ElseIf isOverTorsoSlot Then
;		If wbl\Vest > 0 Then
;			
;			wbl\Vest = 0
;			
;			Msg = GetLocalString("Items", "vest_off") : MsgTimer = 70 * 5
;			PlaySound_Strict(g_I\UI_Deny_SFX)
;		EndIf
;	ElseIf isOverBackpackSlot Then
;		If wbl\Backpack > 0 Then
;			
;			wbl\Backpack = 0
;			
;			SelectedItem = WearableSlots[SLOT_BACKPACK]
;			WearableSlots[SLOT_BACKPACK] = Null
;			
;			MaxItemAmount = 9
;			
;			Msg = GetLocalString("Items", "backpack_off") : MsgTimer = 70 * 5
;			PlaySound_Strict(g_I\UI_Deny_SFX)
;		EndIf
;	EndIf
;	
;				;Else
;				;	Msg = GetLocalString("Items", "inventory_full") : MsgTimer = 70 * 5
;				;	PlaySound_Strict(g_I\UI_Deny_SFX)
;				;EndIf
;	
;EndIf
;~IDEal Editor Parameters:
;~C#Blitz3D