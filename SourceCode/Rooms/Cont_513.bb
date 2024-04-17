
Function FillRoom_Cont_513(r.Rooms)
	Local d.Doors,sc.SecurityCams,it.Items
	
	d = CreateDoor(r\zone, r\x - 704.0 * RoomScale, 0, r\z + 304.0 * RoomScale, 0, r, False, 0, 2)
	d\AutoClose = False
	PositionEntity (d\buttons[0], EntityX(d\buttons[0],True), EntityY(d\buttons[0],True), r\z + 288.0 * RoomScale, True)
	PositionEntity (d\buttons[1], EntityX(d\buttons[1],True), EntityY(d\buttons[1],True), r\z + 320.0 * RoomScale, True)
	
	sc.SecurityCams = CreateSecurityCam(r\x-312.0 * RoomScale, r\y + 414*RoomScale, r\z + 656*RoomScale, r)
	sc\FollowPlayer = True
	
	it = CreateItem("SCP-513", "scp513", r\x - 32.0 * RoomScale, r\y + 196.0 * RoomScale, r\z + 688.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Blood-stained Note", "paper", r\x + 736.0 * RoomScale,1.0, r\z + 48.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	it = CreateItem("Document SCP-513", "paper", r\x - 480.0 * RoomScale, 104.0*RoomScale, r\z - 176.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	r\Objects[0] = LoadMesh_Strict("GFX\map\props\513_jelly.b3d")
	ScaleEntity r\Objects[0],RoomScale,RoomScale,RoomScale
	PositionEntity r\Objects[0],r\x,r\y+160.72*RoomScale,r\z+666.76*RoomScale,True
	RotateEntity r\Objects[0],0,r\angle,0
	EntityAlpha r\Objects[0],0.65
	EntityParent r\Objects[0],r\obj
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D