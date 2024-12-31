
Function FillRoom_Cont_106(r.Rooms)
	Local it.Items,d.Doors,sc.SecurityCams,w.WayPoints
	Local i,n
	
	it = CreateItem(GetLocalString("Item Names","key_5"), "key5", r\x - 752.0 * RoomScale, r\y - 592 * RoomScale, r\z + 3026.0 * RoomScale)
	EntityParent(it\collider, r\obj)
	
	;it = CreateItem("Dr. Allok's Note", "paper", r\x - 416.0 * RoomScale, r\y - 576 * RoomScale, r\z + 2492.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	;it = CreateItem("Recall Protocol RP-106-N", "paper", r\x + 268.0 * RoomScale, r\y - 576 * RoomScale, r\z + 2593.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	d = CreateDoor(r\zone, r\x - 968.0 * RoomScale, -764.0 * RoomScale, r\z + 1392.0 * RoomScale, 0, r, False, False, KEY_CARD_4)
	d\AutoClose = False : d\open = False	
	
	d = CreateDoor(r\zone, r\x, 0, r\z - 464.0 * RoomScale, 0, r, False, False, KEY_CARD_3)
	d\AutoClose = False : d\open = False			
	
	d = CreateDoor(r\zone, r\x - 624.0 * RoomScale, -1280.0 * RoomScale, r\z, 90, r, False, False, KEY_CARD_4)
	d\AutoClose = False : d\open = False	
	
	r\Objects[6] = LoadRMesh("GFX\map\rooms\cont_106\room1062_opt.rmesh",Null)
	EntityPickMode(r\Objects[6], 2)
	
	ScaleEntity (r\Objects[6],RoomScale,RoomScale,RoomScale)
	EntityPickMode r\Objects[6], 2
	PositionEntity(r\Objects[6],r\x+784.0*RoomScale,-980.0*RoomScale,r\z+720.0*RoomScale,True)
	
	EntityParent(r\Objects[6], r\obj)
	
	r\Levers[0] = CreateLever(r, r\x - 555.0 * RoomScale, r\y - 576.0 * RoomScale, r\z + 3040.0 * RoomScale,0,True)
	r\Levers[1] = CreateLever(r, r\x - (555.0 - 81.0) * RoomScale, r\y - 576.0 * RoomScale, r\z + 3040.0 * RoomScale)
	
	r\Objects[4] = CreateButton(r\x - 146.0*RoomScale, r\y - 576.0 * RoomScale, r\z + 3045.0 * RoomScale, 0,0,0)
	EntityParent (r\Objects[4],r\obj)
	
	sc.SecurityCams = CreateSecurityCam(r\x + 768.0 * RoomScale, r\y + 1392.0 * RoomScale, r\z + 1696.0 * RoomScale, r, True)
	sc\angle = 45 + 90 + 180
	sc\turn = 20
	TurnEntity(sc\CameraObj, 45, 0, 0)
	EntityParent(sc\obj, r\obj)
	
	r\Objects[7] = sc\CameraObj
	r\Objects[8] = sc\obj
	
	PositionEntity(sc\ScrObj, r\x - 272.0 * RoomScale, -544.0 * RoomScale, r\z + 3020.0 * RoomScale)
	TurnEntity(sc\ScrObj, 0, -10, 0)
	EntityParent sc\ScrObj, r\obj
	sc\CoffinEffect=0
	
	r\Objects[5] = CreatePivot()
	TurnEntity r\Objects[5], 0,180,0
	PositionEntity (r\Objects[5], r\x + 1088.0 * RoomScale, 1104.0 * RoomScale, r\z + 1888.0 * RoomScale) 
	EntityParent r\Objects[5], r\obj
	
	r\Objects[9] = CreatePivot(r\obj)
	PositionEntity (r\Objects[9], r\x - 272 * RoomScale, r\y - 672.0 * RoomScale, r\z + 2736.0 * RoomScale, True)
	
	r\Objects[10] = CreatePivot(r\obj)
	PositionEntity (r\Objects[10], r\x, r\y, r\z - 720.0 * RoomScale, True)
	
	w.WayPoints = CreateWaypoint(r\x + 480.0*RoomScale, r\y + 50*RoomScale, r\z + 2240.0*RoomScale,Null,r)
	w.WayPoints = CreateWaypoint(r\x, r\y + 50*RoomScale, r\z + 1600.0*RoomScale,Null,r)
	w.WayPoints = CreateWaypoint(r\x - 2, r\y + 50*RoomScale, r\z + 2166*RoomScale,Null,r)
	
End Function

Function UpdateEvent_Cont_106(e.Events)
	Local d.Decals,e2.Events
	Local temp%
	Local n.NPCs
	
	;EventState[1] = are the magnets on
	
	If SoundTransmission Then 
		If e\EventState[0] = 1 Then
			e\EventState[2] = Min(e\EventState[2]+FPSfactor,4000)
		EndIf
		If ChannelPlaying(e\SoundCHN[0]) = False Then e\SoundCHN[0] = PlaySound_Strict(RadioStatic)   
	EndIf
	
	If e\room\NPC[0]=Null Then ;add the lure subject
		TFormPoint(1088, 1096, 1728, e\room\obj, 0)
		e\room\NPC[0] = CreateNPC(NPC_Human, TFormedX(), TFormedY(), TFormedZ())
		TurnEntity e\room\NPC[0]\Collider,0,e\room\angle+90,0,True
		e\room\NPC[0]\HideFromNVG = True
	EndIf
	
	If PlayerRoom = e\room And e\room\NPC[0]<>Null Then
		
		If TaskExists(TASK_NTF_SEARCH_106) Then
			EndTask(TASK_NTF_SEARCH_106)
			BeginTask(TASK_NTF_CONTAIN_106)
		EndIf
		
		ShouldPlay = MUS_CONT_106
		
		e\room\NPC[0]\State[0]=STATE_SCRIPT
		If e\room\NPC[0]\Idle = 0 Then
			AnimateNPC(e\room\NPC[0], 17.0, 19.0, 0.01, False)
			If e\room\NPC[0]\Frame = 19.0 Then e\room\NPC[0]\Idle = 1
		Else
			AnimateNPC(e\room\NPC[0], 19.0, 17.0, -0.01, False)	
			If e\room\NPC[0]\Frame = 17.0 Then e\room\NPC[0]\Idle = 0
		EndIf
		
		PositionEntity(e\room\NPC[0]\Collider, EntityX(e\room\Objects[5],True),EntityY(e\room\Objects[5],True)+0.1,EntityZ(e\room\Objects[5],True),True)
		RotateEntity(e\room\NPC[0]\Collider,EntityPitch(e\room\Objects[5],True),EntityYaw(e\room\Objects[5],True),0,True)
		ResetEntity(e\room\NPC[0]\Collider)
		
		temp = e\EventState[1]
		
		Local leverstate = UpdateLever(e\room\Levers[0]\obj,((EntityY(e\room\Objects[6],True)<-990*RoomScale) And (EntityY(e\room\Objects[6],True)>-1275.0*RoomScale)))
		If GrabbedEntity = e\room\Levers[0]\obj And DrawHandIcon = True Then e\EventState[1] = leverstate
		
		If e\EventState[1] <> temp Then 
			If e\EventState[1] = False Then
				PlaySound_Strict(MagnetDownSFX)
			Else
				PlaySound_Strict(MagnetUpSFX)	
			EndIf
		EndIf
		
		If ((e\EventState[2]>3200) Lor (e\EventState[2]<2500)) Lor (e\EventState[0]<>1) Then
			SoundTransmission% = UpdateLever(e\room\Levers[1]\obj)
		EndIf
		If (Not SoundTransmission) Then
			If (e\SoundCHN[1]<>0) Then
				If ChannelPlaying(e\SoundCHN[1]) Then StopChannel e\SoundCHN[1]
			EndIf
			If (e\SoundCHN[0]<>0) Then
				If ChannelPlaying(e\SoundCHN[0]) Then StopChannel e\SoundCHN[0]
			EndIf
		EndIf
		
		If e\EventState[0] = 0 Then 
			If SoundTransmission And Rand(100)=1 Then
				If e\SoundCHN[1] = 0 Lor ChannelPlaying(e\SoundCHN[1]) = False Then
					LoadEventSound(e,"SFX\Character\LureSubject\Idle"+Rand(1,6)+".ogg",1)
					e\SoundCHN[1] = PlaySound_Strict(e\Sound[1])
				EndIf
			EndIf
			
			If SoundTransmission Then
				UpdateButton(e\room\Objects[4])
				If d_I\ClosestButton = e\room\Objects[4] And KeyHitUse Then
					e\EventState[0] = 1 ;start the femur breaker
					If SoundTransmission = True Then ;only play sounds if transmission is on
						If e\SoundCHN[1] <> 0 Then
							If ChannelPlaying(e\SoundCHN[1]) Then StopChannel e\SoundCHN[1]
						EndIf
						FemurBreakerSFX = LoadSound_Strict("SFX\Room\106Chamber\FemurBreaker.ogg")
						e\SoundCHN[1] = PlaySound_Strict(FemurBreakerSFX)
					EndIf
				EndIf
			EndIf
		ElseIf e\EventState[0] = 1 ;bone broken
			If SoundTransmission And e\EventState[2] < 2000 Then 
				If e\SoundCHN[1] = 0 Lor ChannelPlaying(e\SoundCHN[1]) = False Then
					LoadEventSound(e,"SFX\Character\LureSubject\Sniffling.ogg",1)
					e\SoundCHN[1] = PlaySound_Strict(e\Sound[1])
				EndIf
			EndIf
			
			If e\EventState[2] => 2500 Then
				
				If e\EventState[1] = 1 And e\EventState[2]-FPSfactor < 2500 Then
					PositionEntity(Curr106\Collider, EntityX(e\room\Objects[6], True), EntityY(e\room\Objects[6], True), EntityZ(e\room\Objects[6], True))
					Curr106\Contained = False
					ShowEntity Curr106\obj
					Curr106\Idle = False
					Curr106\State[0] = -11
					e\EventState[0] = 2
					Return
				EndIf
				
				ShouldPlay = MUS_CHASE_106
				
				PositionEntity(Curr106\Collider, EntityX(e\room\Objects[5], True), (700.0 + 108.0*(Min(e\EventState[2]-2500.0,800)/320.0))*RoomScale , EntityZ(e\room\Objects[5], True))
				HideEntity Curr106\obj2
				
				RotateEntity(Curr106\Collider,0, EntityYaw(e\room\Objects[5],True)+180.0, 0, True)
				Curr106\State[0] = -11
				AnimateNPC(Curr106, 206, 250, 0.1)
				Curr106\Idle = True	
				
				If e\EventState[2]-FPSfactor < 2500 Then 
					d.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[5], True), 936.0*RoomScale, EntityZ(e\room\Objects[5], True), 90, 0, Rnd(360)) 
					d\Timer = 90000
					d\Alpha = 0.01 : d\AlphaChange = 0.005
					d\Size = 0.1 : d\SizeChange = 0.003	
					
					If e\SoundCHN[1] <> 0 Then
						If ChannelPlaying(e\SoundCHN[1]) Then StopChannel e\SoundCHN[1]
					EndIf 
					LoadEventSound(e,"SFX\Character\LureSubject\106Bait.ogg",1)
					e\SoundCHN[1]=PlaySound_Strict(e\Sound[1])
				ElseIf e\EventState[2]-FPSfactor < 2900 And e\EventState[2] => 2900 Then
					If FemurBreakerSFX <> 0 Then FreeSound_Strict FemurBreakerSFX : FemurBreakerSFX = 0
					
					d.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[7], True), EntityY(e\room\Objects[7], True) , EntityZ(e\room\Objects[7], True), 0, 0, 0) 
					RotateEntity(d\obj, EntityPitch(e\room\Objects[7], True)+Rand(10,20), EntityYaw(e\room\Objects[7], True)+30, EntityRoll(d\obj))
					MoveEntity d\obj, 0,0.05,0.2
					RotateEntity(d\obj, EntityPitch(e\room\Objects[7], True), EntityYaw(e\room\Objects[7], True), EntityRoll(d\obj))
					
					EntityParent d\obj, e\room\Objects[7]
					
					d\Timer = 90000
					d\Alpha = 0.01 : d\AlphaChange = 0.005
					d\Size = 0.05 : d\SizeChange = 0.002
				ElseIf e\EventState[2] > 3200 Then
					
					If e\EventState[1] = True Then ;magnets on -> 106 caught
						Curr106\Contained = True
						If e\EventState[3] = 0 Then
							If gopt\GameMode = GAMEMODE_NTF Then
								PlayAnnouncement("SFX\Intercom\MTF\NTF\Announc106Contain.ogg")
							ElseIf gopt\GameMode = GAMEMODE_CLASS_D Then
								PlayAnnouncement("SFX\Intercom\MTF\NTF\Announc106Contain_2.ogg")
							Else
								PlayAnnouncement("SFX\Intercom\MTF\NTF\Announc106Contain_3.ogg")
							EndIf
							e\EventState[3] = 1
						EndIf
						If TaskExists(TASK_NTF_CONTAIN_106) Then
							EndTask(TASK_NTF_CONTAIN_106)
							If (Not ecst\Contained049 Lor (Not Curr173\Contained) Lor (Not ecst\Contained008) Lor (Not ecst\Contained409)) Then
								BeginTask(TASK_NTF_GO_TO_ZONE)
							EndIf
						EndIf
						If opt\SteamEnabled Then Steam_Achieve(ACHV_106_CONTAINED)
					Else ;magnets off -> 106 comes out and attacks
						PositionEntity(Curr106\Collider, EntityX(e\room\Objects[6], True), EntityY(e\room\Objects[6], True), EntityZ(e\room\Objects[6], True))
						
						Curr106\Contained = False
						ShowEntity Curr106\obj
						Curr106\Idle = False
						Curr106\State[0] = -11
						
						e\EventState[0] = 2
						Return
					EndIf
				EndIf
				
			EndIf 
			
		EndIf
		
		If e\EventState[1] Then
			PositionEntity (e\room\Objects[6],EntityX(e\room\Objects[6],True),CurveValue(-980.0*RoomScale + Sin(Float(MilliSecs())*0.04)*0.07,EntityY(e\room\Objects[6],True),200.0),EntityZ(e\room\Objects[6],True),True)
			RotateEntity(e\room\Objects[6], Sin(Float(MilliSecs())*0.03), EntityYaw(e\room\Objects[6],True), -Sin(Float(MilliSecs())*0.025), True)
		Else
			PositionEntity (e\room\Objects[6],EntityX(e\room\Objects[6],True),CurveValue(-1280.0*RoomScale,EntityY(e\room\Objects[6],True),200.0),EntityZ(e\room\Objects[6],True),True)
			RotateEntity(e\room\Objects[6], 0, EntityYaw(e\room\Objects[6],True), 0, True)
		EndIf
	Else
		If PlayerRoom\RoomTemplate\Name = "pocketdimension" Then
			If (e\SoundCHN[1]<>0) Then
				If ChannelPlaying(e\SoundCHN[1]) Then StopChannel e\SoundCHN[1]
			EndIf
			If (e\SoundCHN[0]<>0) Then
				If ChannelPlaying(e\SoundCHN[0]) Then StopChannel e\SoundCHN[0]
			EndIf
		ElseIf PlayerRoom\RoomTemplate\Name = "room860" Then
			For e2.Events = Each Events
				If e2\EventName = "room860" Then
					If e2\EventState[0] = 1.0 Then
						If (e\SoundCHN[1]<>0) Then
							If ChannelPlaying(e\SoundCHN[1]) Then StopChannel e\SoundCHN[1]
						EndIf
						If (e\SoundCHN[0]<>0) Then
							If ChannelPlaying(e\SoundCHN[0]) Then StopChannel e\SoundCHN[0]
						EndIf
					EndIf
					Exit
				EndIf
			Next
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS