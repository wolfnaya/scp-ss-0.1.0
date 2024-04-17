
Function FillRoom_Cont_1162(r.Rooms)
	Local d.Doors,it.Items,sc.SecurityCams
	
	d = CreateDoor(r\zone, r\x + 248.0*RoomScale, 0.0, r\z - 736.0*RoomScale, 90, r, False, False, KEY_CARD_2)
	r\Objects[0] = CreatePivot()
	PositionEntity r\Objects[0],r\x+1012.0*RoomScale,r\y+128.0*RoomScale,r\z-640.0*RoomScale
	EntityParent r\Objects[0],r\obj
	EntityPickMode r\Objects[0],1
	;it = CreateItem("Document SCP-1162", "paper", r\x + 863.227 * RoomScale, r\y + 152.0 * RoomScale, r\z - 953.231 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	sc.SecurityCams = CreateSecurityCam(r\x-192.0*RoomScale, r\y+704.0*RoomScale, r\z+192.0*RoomScale, r)
	sc\angle = 225
	TurnEntity(sc\CameraObj, 20, 0, 0)
	sc\ID = 8
	
End Function

Function UpdateEvent_Cont_1162(e.Events)
	Local it.Items,itt.ItemTemplates,de.Decals
	Local pvt%
	Local i
	
	;e\EventState[0] = A variable to determine the "nostalgia" items
	;- 0.0 = No nostalgia item
	;- 1.0 = Red Key
	;- 2.0 = Disciplinary Hearing DH-S-4137-17092
	;- 3.0 = Coin
	;- 4.0 = Movie Ticket
	;e\EventState[1] = Defining which slot from the Inventory should be picked
	;e\EventState[2] = A check for if a item should be removed
	;- 0.0 = no item "trade" will happen
	;- 1.0 = item "trade" will happen
	;- 2.0 = the player doesn't has any items in the Inventory, giving him heavily Injuries and giving him a random item
	;- 3.0 = player got a memorial item (to explain a bit D-9341's background)
	;- 3.1 = player got a memorial item + injuries (because he didn't had any item in his inventory before)
	If PlayerRoom = e\room
		
		GrabbedEntity = 0
		
		e\EventState[0] = 0
		
		Local Pick1162% = True
		Local pp% = CreatePivot(e\room\obj)
		PositionEntity pp,976,128,-640,False
		
		For it.Items = Each Items
			If (Not it\Picked)
				If EntityDistanceSquared(it\collider,e\room\Objects[0])<PowTwo(0.75)
					Pick1162% = False
				EndIf
			EndIf
		Next
		
		If EntityDistanceSquared(e\room\Objects[0],Collider)<PowTwo(0.75) And Pick1162%
			DrawHandIcon = True
			If KeyHitUse Then GrabbedEntity = e\room\Objects[0]
		EndIf
		
		If GrabbedEntity <> 0 
			e\EventState[1] = Rand(0,MaxItemAmount-1)
			If Inventory[e\EventState[1]]<>Null
				;randomly picked item slot has an item in it, using this slot
				e\EventState[2] = 1.0
				DebugLog "pick1"
			Else
				;randomly picked item slot is empty, getting the first available slot
				For i = 0 To MaxItemAmount-1
					Local isSlotEmpty% = (Inventory[(i+e\EventState[1]) Mod MaxItemAmount] = Null)
					
					If (Not isSlotEmpty) Then
						;successful
						e\EventState[1] = (i+e\EventState[1]) Mod MaxItemAmount
					EndIf
					
					If Rand(8)=1 Then
						If isSlotEmpty Then
							e\EventState[2] = 3.1
						Else
							e\EventState[2] = 3.0
						EndIf
						
						e\EventState[0] = Rand(1,4)
						
						;Checking if the selected nostalgia item already exists or not
						Local itemName$ = ""
						Select (e\EventState[0])
							Case 1
								itemName = "H&K USP"
							Case 2
								itemName = "Ballistic Helmet"
							Case 3
								itemName = "Coin"
							Case 4
								itemName = "Movie Ticket"
						End Select
						
						Local itemExists% = False
						For it.Items = Each Items
							If (it\name = itemName) Then
								itemExists = True
								e\EventState[2] = 1.0
								e\EventState[0] = 0.0
								Exit
							EndIf
						Next
						
						If ((Not itemExists) And (Not isSlotEmpty)) Exit
						Else
							If isSlotEmpty Then
								e\EventState[2] = 2.0
							Else
								e\EventState[2] = 1.0
								Exit
							EndIf
						EndIf
					Next
				EndIf
			EndIf
			
			
			;trade successful
			If e\EventState[2] = 1.0
				Local shouldCreateItem% = False
				
				For itt.ItemTemplates = Each ItemTemplates
					If (IsItemGoodFor1162(itt)) Then
						Select Inventory[e\EventState[1]]\itemtemplate\tempname
							Case "key"
								If itt\tempname = "key1" Lor itt\tempname = "key2" And Rand(2)=1
									shouldCreateItem = True
									DebugLog "lostkey"
								EndIf
							Case "gasmask","gasmask3","gasmask2","hazmat","hazmat2","hazmat3"
								If itt\tempname = "gasmask" Lor itt\tempname = "gasmask3" Lor itt\tempname = "gasmask2" Lor itt\tempname = "hazmat" Lor itt\tempname = "hazmat2" Lor itt\tempname = "hazmat3" And Rand(2)=1
									shouldCreateItem = True
									DebugLog "gasmask hazmat"
								EndIf
							Case "key1","key2","key3"
								If itt\tempname = "key1" Lor itt\tempname = "key2" Lor itt\tempname = "key3" Lor itt\tempname = "misc" And Rand(6)=1
									shouldCreateItem = True
									DebugLog "key"
								EndIf
							Case "vest","vest2"
								If itt\tempname = "vest" Lor itt\tempname = "vest2" And Rand(1)=1
									shouldCreateItem = True
									DebugLog "vest"
								EndIf
							Default
								If itt\tempname = "misc" And Rand(6)=1
									shouldCreateItem = True
									DebugLog "default"
								EndIf
						End Select
					EndIf
					
					If (shouldCreateItem) Then
						I_714\Using = 0
						RemoveItem(Inventory[e\EventState[1]])
						it=CreateItem(itt\name,itt\tempname,EntityX(pp,True),EntityY(pp,True),EntityZ(pp,True))
						EntityType(it\collider, HIT_ITEM)
						PlaySound_Strict LoadTempSound("SFX\SCP\1162\Exchange"+Rand(0,4)+".ogg")
						e\EventState[2] = 0.0
						GiveAchievement(Achv1162)
						KeyHitUse = False
						Exit
					EndIf
				Next
				;trade not sucessful (player got in return to injuries a new item)
			ElseIf e\EventState[2] = 2.0
				;Injuries = Injuries + 5.0
				DamageSPPlayer(50, True)
				pvt = CreatePivot()
				PositionEntity pvt, EntityX(Collider),EntityY(Collider)-0.05,EntityZ(Collider)
				TurnEntity pvt, 90, 0, 0
				EntityPick(pvt,0.3)
				de.Decals = CreateDecal(DECAL_BLOODSPLAT2, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
				de\Size = 0.75 : ScaleSprite de\obj, de\Size, de\Size
				FreeEntity pvt
				For itt.ItemTemplates = Each ItemTemplates
					If IsItemGoodFor1162(itt) And Rand(6)=1
						it = CreateItem(itt\name, itt\tempname, EntityX(pp,True),EntityY(pp,True),EntityZ(pp,True))
						EntityType(it\collider, HIT_ITEM)
						GiveAchievement(Achv1162)
						KeyHitUse = False
						e\EventState[2] = 0.0
						If (Not IsSPPlayerAlive()) Then
							m_msg\DeathTxt = GetLocalStringR("Singleplayer", "cont_1162_death", Designation)
							PlaySound_Strict LoadTempSound("SFX\SCP\1162\BodyHorrorExchange"+Rand(1,4)+".ogg")
							LightFlash = 5.0
							Kill()
						Else
							PlaySound_Strict LoadTempSound("SFX\SCP\1162\BodyHorrorExchange"+Rand(1,4)+".ogg")
							LightFlash = 5.0
							CreateMsg(GetLocalString("Singleplayer", "cont_1162_1"))
						EndIf
						Exit
					EndIf
				Next
				;trade with nostalgia item
			ElseIf e\EventState[2] >= 3.0
				If e\EventState[2] < 3.1
					PlaySound_Strict LoadTempSound("SFX\SCP\1162\Exchange"+Rand(0,4)+".ogg")
					I_714\Using = 0
					RemoveItem(Inventory[e\EventState[1]])
				Else
					DamageSPPlayer(50, True)
					pvt = CreatePivot()
					PositionEntity pvt, EntityX(Collider),EntityY(Collider)-0.05,EntityZ(Collider)
					TurnEntity pvt, 90, 0, 0
					EntityPick(pvt,0.3)
					de.Decals = CreateDecal(DECAL_BLOODSPLAT2, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
					de\Size = 0.75 : ScaleSprite de\obj, de\Size, de\Size
					FreeEntity pvt
					If (Not IsSPPlayerAlive()) Then
						m_msg\DeathTxt = GetLocalStringR("Singleplayer", "cont_1162_death", Designation)
						PlaySound_Strict LoadTempSound("SFX\SCP\1162\BodyHorrorExchange"+Rand(1,4)+".ogg")
						LightFlash = 5.0
						Kill()
					Else
						PlaySound_Strict LoadTempSound("SFX\SCP\1162\BodyHorrorExchange"+Rand(1,4)+".ogg")
						LightFlash = 5.0
						CreateMsg(GetLocalString("Singleplayer", "cont_1162_2"))
					EndIf
					e\EventState[1] = 0.0
				EndIf
				Select e\EventState[0]
					Case 1
						it = CreateItem("H&K USP","usp",EntityX(pp,True),EntityY(pp,True),EntityZ(pp,True))
						it\state = 9 : it\state2 = 12
					Case 2
						it = CreateItem(GetLocalString("Item Names","helmet"),"helmet",EntityX(pp,True),EntityY(pp,True),EntityZ(pp,True))
					Case 3
						it = CreateItem(GetLocalString("Item Names","coin"),"coin",EntityX(pp,True),EntityY(pp,True),EntityZ(pp,True))
					Case 4
						it = CreateItem(GetLocalString("Item Names","key_3"),"key3",EntityX(pp,True),EntityY(pp,True),EntityZ(pp,True))
				End Select
				EntityType(it\collider, HIT_ITEM)
				GiveAchievement(Achv1162)
				KeyHitUse = False
				e\EventState[2] = 0.0
			EndIf
			FreeEntity pp
		EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D