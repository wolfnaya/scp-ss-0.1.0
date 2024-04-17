
Function FillRoom_Lockroom_2(r.Rooms)
	Local de.Decals,sc.SecurityCams
	Local i
	
	For i = 0 To 5
		de.Decals = CreateDecal(GetRandomDecalID(DECAL_TYPE_BLOODSPLAT), r\x+Rnd(-392,520)*RoomScale, 3.0*RoomScale+Rnd(0,0.001), r\z+Rnd(-392,520)*RoomScale,90,Rnd(360),0)
		de\Size = Rnd(0.3,0.6)
		ScaleSprite(de\obj, de\Size,de\Size)
		CreateDecal(GetRandomDecalID(DECAL_TYPE_BLOODDROP), r\x+Rnd(-392,520)*RoomScale, 3.0*RoomScale+Rnd(0,0.001), r\z+Rnd(-392,520)*RoomScale,90,Rnd(360),0)
		de\Size = Rnd(0.1,0.6)
		ScaleSprite(de\obj, de\Size,de\Size)
		CreateDecal(GetRandomDecalID(DECAL_TYPE_BLOODDROP), r\x+Rnd(-0.5,0.5), 3.0*RoomScale+Rnd(0,0.001), r\z+Rnd(-0.5,0.5),90,Rnd(360),0)
		de\Size = Rnd(0.1,0.6)
		ScaleSprite(de\obj, de\Size,de\Size)
	Next
	
	sc.SecurityCams = CreateSecurityCam(r\x + 512.0 * RoomScale, r\y + 384 * RoomScale, r\z + 384.0 * RoomScale, r, True)
	sc\angle = 45 + 90
	sc\turn = 45
	TurnEntity(sc\CameraObj, 40, 0, 0)
	EntityParent(sc\obj, r\obj)
	
	PositionEntity(sc\ScrObj, r\x + 668 * RoomScale, 1.1, r\z - 96.0 * RoomScale)
	TurnEntity(sc\ScrObj, 0, 90, 0)
	EntityParent(sc\ScrObj, r\obj)
	
	sc.SecurityCams = CreateSecurityCam(r\x - 384.0 * RoomScale, r\y + 384 * RoomScale, r\z - 512.0 * RoomScale, r, True)
	sc\angle = 45 + 90 + 180
	sc\turn = 45
	
	TurnEntity(sc\CameraObj, 40, 0, 0)
	EntityParent(sc\obj, r\obj)				
	
	PositionEntity(sc\ScrObj, r\x + 96.0 * RoomScale, 1.1, r\z - 668.0 * RoomScale)
	EntityParent(sc\ScrObj, r\obj)
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D