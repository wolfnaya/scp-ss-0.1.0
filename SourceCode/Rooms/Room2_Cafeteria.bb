
Function FillRoom_Room2_Cafeteria(r.Rooms)
	Local it.Items
	
	;scp-294
	r\Objects[0] = CreatePivot(r\obj)
	PositionEntity(r\Objects[0], r\x+1847.0*RoomScale, -240.0*RoomScale, r\z-321*RoomScale, True)
	;"spawnpoint" for the cups
	r\Objects[1] = CreatePivot(r\obj)
	PositionEntity(r\Objects[1], r\x+1780.0*RoomScale, -248.0*RoomScale, r\z-276*RoomScale, True)
	
	it = CreateItem("cup", "cup", r\x-508.0*RoomScale, -187*RoomScale, r\z+284.0*RoomScale, 240,175,70)
	EntityParent(it\collider, r\obj) : it\name = "Cup of Orange Juice"
	
	it = CreateItem("cup", "cup", r\x+1412 * RoomScale, -187*RoomScale, r\z-716.0 * RoomScale, 87,62,45)
	EntityParent(it\collider, r\obj) : it\name = "Cup of Coffee"
	
	it = CreateItem("Empty Cup", "emptycup", r\x-540*RoomScale, -187*RoomScale, r\z+124.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Quarter", "25ct", r\x-447.0*RoomScale, r\y-334.0*RoomScale, r\z+36.0*RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem("Quarter", "25ct", r\x+1409.0*RoomScale, r\y-334.0*RoomScale, r\z-732.0*RoomScale)
	EntityParent(it\collider, r\obj)
	
End Function

Function UpdateEvent_Room2_Cafeteria(e.Events)
	Local it.Items
	Local temp%
	
	If PlayerRoom = e\room Then
		If Not Using294 Then
			If EntityDistanceSquared(e\room\Objects[0], Collider)<PowTwo(1.5) Then
				GiveAchievement(Achv294)
				If EntityInView(e\room\Objects[0], Camera) Then
					DrawHandIcon = True
					If keyhituse Then
						temp = True
						For it.Items = Each Items
							If it\Picked=False Then
								If EntityX(it\collider)-EntityX(e\room\Objects[1],True)=0 Then
									If EntityZ(it\collider)-EntityZ(e\room\Objects[1],True)=0 Then
										temp = False
										Exit
									EndIf
								EndIf
							EndIf
						Next
						Local inserted% = False
						If e\EventState[1] < 2 Then
							If SelectedItem<>Null Then
								If SelectedItem\itemtemplate\tempname="25ct" Lor SelectedItem\itemtemplate\tempname="coin" Then
									RemoveItem(SelectedItem)
									SelectedItem=Null
									e\EventState[1] = e\EventState[1] + 1
									PlaySound_Strict LoadTempSound("SFX\SCP\294\coin_drop.ogg")
									inserted = True
								EndIf
							EndIf
						EndIf
						If e\EventState[1] = 2 Then
							Using294=temp
							If Using294 Then MouseHit1=False
						ElseIf e\EventState[1] = 1 And (Not inserted) Then
							Using294=False
							CreateMsg("You need to insert another Quarter in order to use this machine.")
						ElseIf (Not inserted) Then
							Using294=False
							CreateMsg("You need to insert two Quarters in order to use this machine.")
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf		
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D