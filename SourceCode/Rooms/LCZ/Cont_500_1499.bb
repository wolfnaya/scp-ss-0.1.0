
Function FillRoom_Cont_500_1499(r.Rooms)
	Local d.Doors,it.Items,sc.SecurityCams
	
	r\RoomDoors[0] = CreateDoor(r\zone, r\x + 288.0*RoomScale, r\y, r\z + 576.0*RoomScale, 90, r, False, False, KEY_CARD_3)
	r\RoomDoors[0]\open = False : r\RoomDoors[0]\locked = True
	d = CreateDoor(r\zone, r\x + 777.0*RoomScale, r\y, r\z + 671.0*RoomScale, 90, r, False, False, KEY_CARD_4)
	d = CreateDoor(r\zone, r\x + 556.0*RoomScale, r\y, r\z + 296.0*RoomScale, 0, r, False, False, KEY_CARD_3)
	r\Objects[0] = CreatePivot()
	PositionEntity r\Objects[0],r\x + 576.0*RoomScale,r\y+160.0*RoomScale,r\z+632.0*RoomScale
	EntityParent r\Objects[0],r\obj
	
	it = CreateItem("D-7651's Note", "paper", r\x + 600.0 * RoomScale, r\y + 176.0 * RoomScale, r\z - 228.0 * RoomScale)
	RotateEntity it\collider, 0, r\angle, 0
	EntityParent(it\collider, r\obj)
	
	;it = CreateItem("Document SCP-1499", "paper", r\x + 840.0 * RoomScale, r\y + 260.0 * RoomScale, r\z + 224.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	;it = CreateItem("Document SCP-500", "paper", r\x + 1152.0 * RoomScale, r\y + 224.0 * RoomScale, r\z + 336.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	it = CreateItem(GetLocalString("Item Names","badge_emily"), "badge", r\x + 364.0 * RoomScale, r\y + 5.0 * RoomScale, r\z + 716.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	sc.SecurityCams = CreateSecurityCam(r\x + 850.0 * RoomScale, r\y + 350.0 * RoomScale, r\z + 876.0 * RoomScale, r)
	sc\angle = 220 : sc\turn = 30
	TurnEntity(sc\CameraObj, 30, 0, 0)
	EntityParent(sc\obj, r\obj)
	
	sc.SecurityCams = CreateSecurityCam(r\x + 600.0 * RoomScale, r\y + 514.0 * RoomScale, r\z + 150.0 * RoomScale, r)
	sc\angle = 180 : sc\turn = 30
	TurnEntity(sc\CameraObj, 30, 0, 0)
	EntityParent(sc\obj, r\obj)
	
End Function

Function UpdateEvent_Cont_500_1499(e.Events)
	Local de.Decals
	Local n.NPCs
	
	If e\room\dist < 15
		If Curr106\Contained Then e\EventState[0] = 2.0
		If Curr106\State[0] < 0 Then e\EventState[0] = 2.0
		
		If e\EventState[0] < 2.0
			If e\EventState[0] = 0.0
				LoadEventSound(e,"SFX\Character\Scientist\EmilyScream.ogg")
				e\SoundCHN[0] = PlaySound2(e\Sound[0], Camera, e\room\Objects[0], 100, 1.0)
				de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[0],True), e\room\y+2.0*RoomScale, EntityZ(e\room\Objects[0],True), 90, Rand(360), 0)
				de\Size = 0.5 : EntityAlpha(de\obj, 0.8)
				EntityFX de\obj,1
				e\EventState[0] = 1.0
			ElseIf e\EventState[0] = 1.0
				If (Not ChannelPlaying(e\SoundCHN[0]))
					e\EventState[0] = 2.0
					e\room\RoomDoors[0]\locked = False
				Else
					UpdateSoundOrigin(e\SoundCHN[0],Camera,e\room\Objects[0],100,1.0)
				EndIf
			EndIf
		Else
			DebugLog "Removed 'cont_500_1499' event"
			e\room\RoomDoors[0]\locked = False
			de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[0],True), e\room\y+2.0*RoomScale, EntityZ(e\room\Objects[0],True), 90, Rand(360), 0)
			de\Size = 0.5 : EntityAlpha(de\obj, 0.8)
			EntityFX de\obj,1
			RemoveEvent(e)
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D