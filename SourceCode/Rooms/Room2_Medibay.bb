
Function FillRoom_Room2_Medibay(r.Rooms)
	Local it.Items
	
	r\Objects[0] = LoadMesh_Strict("GFX\map\rooms\room2_medibay\medibay_props.b3d",r\obj)
	EntityType r\Objects[0],HIT_MAP
	EntityPickMode r\Objects[0],2
	
	r\Objects[1] = CreatePivot(r\obj)
	PositionEntity(r\Objects[1], r\x - 762.0 * RoomScale, r\y + 0.0 * RoomScale, r\z - 346.0 * RoomScale, True)
	r\Objects[2] = CreatePivot(r\obj)
	PositionEntity(r\Objects[2], (EntityX(r\Objects[1],True)+(126.0 * RoomScale)), EntityY(r\Objects[1],True), EntityZ(r\Objects[1],True), True)
	it = CreateItem("First Aid Kit", "firstaid", r\x - 506.0 * RoomScale, r\y + 192.0 * RoomScale, r\z - 322.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem("Syringe", "syringe", r\x - 333.0 * RoomScale, r\y + 100.0 * RoomScale, r\z + 97.3 * RoomScale)
	EntityParent(it\collider, r\obj)
	it = CreateItem("Syringe", "syringe", r\x - 340.0 * RoomScale, r\y + 100.0 * RoomScale, r\z + 52.3 * RoomScale)
	EntityParent(it\collider, r\obj)
	r\RoomDoors[0] = CreateDoor(r\zone, r\x - 264.0 * RoomScale, r\y - 0.0 * RoomScale, r\z + 640.0 * RoomScale, 90, r, False, False, 3)
	
	r\Objects[3] = CreatePivot(r\obj)
	PositionEntity r\Objects[3],r\x-820.0*RoomScale,r\y,r\z-318.399*RoomScale,True
	
End Function

Function UpdateEvent_Room2_Medibay(e.Events)
	
	;e\eventstate[0]: Determines if the player has entered the room or not
	;	- 0 : Not entered
	;	- 1 : Has entered
	;e\EventState[1]: A timer for the zombie wake up
	
	;Hiding/Showing the props in this room
	If PlayerRoom <> e\room
		HideEntity e\room\Objects[0]
	Else
		ShowEntity e\room\Objects[0]
	EndIf
		;Setup
;		If e\eventstate[0] = 0
;			e\room\NPC[0] = CreateNPC(NPCtype008,EntityX(e\room\Objects[3],True),0.5,EntityZ(e\room\Objects[3],True))
;			RotateEntity e\room\NPC[0]\Collider,0,e\room\angle-90,0
;			e\eventstate[0] = 1
;		EndIf
;		
;		If EntityDistanceSquared(e\room\NPC[0]\Collider,Collider)<PowTwo(1.2)
;			If e\EventState[1] = 0
;				LightBlink = 12.0
;				PlaySound_Strict LightSFX
;				e\EventState[1] = FPSfactor
;			EndIf
;		EndIf
;	EndIf
;	
;	If e\EventState[1] > 0 And e\EventState[1] < 70*4
;		e\EventState[1] = e\EventState[1] + FPSfactor
;	ElseIf e\EventState[1] >= 70*4
;		If e\room\NPC[0]\State = 0
;			e\room\NPC[0]\State = 2
;		EndIf
;	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D