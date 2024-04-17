
Function FillRoom_Cont_372(r.Rooms)
	Local it.Items,d.Doors
	
	it = CreateItem("Document SCP-372", "paper", r\x + 800.0 * RoomScale, r\y + 176.0 * RoomScale, r\z + 1108.0 * RoomScale)
	RotateEntity it\collider, 0, r\angle, 0
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Radio Transceiver", "radio", r\x + 800.0 * RoomScale, r\y + 112.0 * RoomScale, r\z + 944.0 * RoomScale)
	it\state = 80.0
	EntityParent(it\collider, r\obj)
	
	r\Objects[3] = LoadMesh_Strict("GFX\map\rooms\cont_372\372_hb.b3d",r\obj)
	EntityPickMode r\Objects[3],2
	EntityType r\Objects[3],HIT_MAP
	EntityAlpha r\Objects[3],0.0
	
	d = CreateDoor(r\zone,r\x,r\y,r\z-368.0*RoomScale,0,r,True,True,2)
	d\AutoClose = False
	PositionEntity (d\buttons[0], r\x - 496.0 * RoomScale, 0.7, r\z - 272.0 * RoomScale, True)
	TurnEntity(d\buttons[0], 0, 90, 0)
	
End Function

Function UpdateEvent_Cont_372(e.Events)
	
	If PlayerRoom = e\room Then
		If e\EventState[0] = 0 Then
			If EntityDistanceSquared(Collider, e\room\obj) < PowTwo(2.5) Then
				PlaySound_Strict(RustleSFX[Rand(0,2)])
				CreateNPC(NPCtype372, 0, 0, 0)
				e\EventState[0] = 1
				RemoveEvent(e)
			EndIf					
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D