
Function FillRoom_Room3_Pit(r.Rooms)
	Local em.Emitters
	
	em.Emitters = CreateEmitter(r\x + 512.0 * RoomScale, -76 * RoomScale, r\z - 688 * RoomScale, 0)
	TurnEntity(em\Obj, -90, 0, 0)
	EntityParent(em\Obj, r\obj)
	em\RandAngle = 55
	em\Speed = 0.0005
	em\Achange = -0.015
	em\SizeChange = 0.007
	
	em.Emitters = CreateEmitter(r\x - 512.0 * RoomScale, -76 * RoomScale, r\z - 688 * RoomScale, 0)
	TurnEntity(em\Obj, -90, 0, 0)
	EntityParent(em\Obj, r\obj)
	em\RandAngle = 55
	em\Speed = 0.0005
	em\Achange = -0.015
	em\SizeChange = 0.007
	
	r\Objects[0]= CreatePivot(r\obj)
	PositionEntity(r\Objects[0], r\x + 704.0 * RoomScale, 112.0*RoomScale, r\z-416.0*RoomScale, True)
	
End Function

Function UpdateEvent_Room3_Pit(e.Events)
	Local tex%
	
	If PlayerRoom = e\room Then
		If e\room\Objects[2] = 0 Then
			e\room\Objects[2] =	LoadMesh_Strict("GFX\npcs\SCPs\Anomalous Ducks\Anomalous_Duck.b3d")
			ScaleEntity(e\room\Objects[2], 0.07, 0.07, 0.07)
			tex = LoadTexture_Strict("GFX\npcs\SCPs\Anomalous Ducks\duck1.png",1,0)
			EntityTexture e\room\Objects[2], tex
			DeleteSingleTextureEntryFromCache tex
			PositionEntity (e\room\Objects[2], EntityX(e\room\Objects[0],True), EntityY(e\room\Objects[0],True), EntityZ(e\room\Objects[0],True))
			PointEntity e\room\Objects[2], e\room\obj
			RotateEntity(e\room\Objects[2], 0, EntityYaw(e\room\Objects[2],True),0, True)
			
			LoadEventSound(e,"SFX\SCP\Joke\Saxophone.ogg")
		Else
			If EntityInView(e\room\Objects[2],Camera)=False Then
				e\EventState[0] = e\EventState[0] + FPSfactor
				If Rand(200)=1 And e\EventState[0] > 300 Then
					e\EventState[0] = 0
					e\SoundCHN[0] = PlaySound2(e\Sound[0], Camera, e\room\Objects[2],6.0)
				EndIf
			Else
				If e\SoundCHN[0] <> 0 Then
					If ChannelPlaying(e\SoundCHN[0]) Then StopChannel e\SoundCHN[0]
				EndIf
			EndIf						
		EndIf
	EndIf
	
End Function

Function UpdateEvent_Room3_Pit_1048(e.Events)
;	Local tex%,sf%,b%,t%,texname$
;	Local i
;	
;	If PlayerRoom = e\room Then
;		If e\room\Objects[2] = 0 Then
;			e\room\Objects[2] =	LoadAnimMesh_Strict("GFX\npcs\scps\1048-B\1048-b_2.b3d")
;			ScaleEntity e\room\Objects[2], 0.05,0.05,0.05
;			SetAnimTime(e\room\Objects[2], 414)
;			
;			Local imgPath$ = "GFX\items\1048\1048_"+Rand(1,20)+".jpg"
;			
;			Local itt.ItemTemplates
;			For itt.ItemTemplates = Each ItemTemplates
;				If itt\name = "Drawing" Then
;					If itt\img<>0 Then FreeImage itt\img	
;					itt\img = LoadImage_Strict(imgPath)
;					MaskImage(itt\img, 255,0,255)
;					itt\imgpath = imgPath
;					
;					Exit
;				EndIf
;			Next
;			
;			tex% = LoadTexture_Strict(imgPath)
;			Local brush% = LoadBrush_Strict(imgPath, 1)
;			
;			For i = 1 To CountSurfaces(e\room\Objects[2])
;				sf% = GetSurface(e\room\Objects[2],i)
;				b% = GetSurfaceBrush( sf )
;				t% = GetBrushTexture(b, 0)
;				texname$ = StripPath(TextureName(t))
;				;debuglog "texname: "+texname
;				If Lower(texname) = "1048_1.jpg" Then
;					PaintSurface sf, brush
;				EndIf
;				FreeBrush b
;			Next
;			
;			DeleteSingleTextureEntryFromCache tex ;TODO tex is unused here
;			FreeBrush brush
;			
;			PositionEntity (e\room\Objects[2], EntityX(e\room\Objects[0],True), EntityY(e\room\Objects[0],True), EntityZ(e\room\Objects[0],True))
;		Else
;			PointEntity e\room\Objects[2], Collider
;			RotateEntity(e\room\Objects[2], -90, EntityYaw(e\room\Objects[2],True),0, True)
;			
;			If e\EventState[0]=0 Then
;				If (EntityDistanceSquared(Collider, e\room\Objects[2])<PowTwo(3.0)) Then
;					If EntityInView(e\room\Objects[2],Camera) Then 
;						e\EventState[0] = 1
;						GiveAchievement(Achv1048)
;					EndIf
;				EndIf
;			Else If e\EventState[0]=1
;				Animate2(e\room\Objects[2], AnimTime(e\room\Objects[2]), 1, 205, 0.5, False)
;				If AnimTime(e\room\Objects[2])=205 Then e\EventState[0]=2
;			Else If e\EventState[0] = 2
;				Animate2(e\room\Objects[2], AnimTime(e\room\Objects[2]), 205, 353, 1.0)	
;				If (EntityDistanceSquared(Collider, e\room\Objects[2])<PowTwo(1.5)) Then
;					DrawHandIcon = True
;					
;					If KeyHitUse Then
;						If ItemAmount >= MaxItemAmount Then
;							CreateMsg(GetLocalString("Items", "cannot_carry"))
;						Else
;							SelectedItem = CreateItem("Drawing", "paper", 0.0, 0.0, 0.0)
;							EntityType SelectedItem\collider,HIT_ITEM
;							
;							PickItem(SelectedItem)
;							
;							FreeEntity(e\room\Objects[2])
;							e\room\Objects[2] = 0
;							
;							e\EventState[0] = 3
;							RemoveEvent(e)
;						EndIf
;					EndIf
;				EndIf
;			EndIf
;		EndIf
;	EndIf
;	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D