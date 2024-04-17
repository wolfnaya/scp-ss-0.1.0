
Function FillRoom_Gate_A_Topside(r.Rooms)
	Local d2.Doors,r2.Rooms
	
	r\RoomDoors[2] = CreateDoor(r\zone, r\x - 4064.0 * RoomScale, (-1280.0+12000.0)*RoomScale, r\z + 3952.0 * RoomScale, 0, r, False)
	r\RoomDoors[2]\AutoClose = False : r\RoomDoors[2]\open = False
	
	d2 = CreateDoor(r\zone, r\x, 12000.0*RoomScale, r\z - 1024.0 * RoomScale, 0, r, False)
	d2\AutoClose = False : d2\open = False : d2\locked = True
	
	d2 = CreateDoor(r\zone, r\x-1440*RoomScale, (12000.0-480.0)*RoomScale, r\z + 2328.0 * RoomScale, 0, r, False, False, 2)
	If SelectedEnding = "A2" Then 
		d2\AutoClose = False : d2\open = True : d2\locked = True	
	Else
		d2\AutoClose = False : d2\open = False : d2\locked = False	
	EndIf	
	PositionEntity d2\buttons[0], r\x-1320.0*RoomScale, EntityY(d2\buttons[0],True), r\z + 2288.0*RoomScale, True
	PositionEntity d2\buttons[1], r\x-1584*RoomScale, EntityY(d2\buttons[0],True), r\z + 2488.0*RoomScale, True	
	RotateEntity d2\buttons[1], 0, 90, 0, True
	
	d2 = CreateDoor(r\zone, r\x-1440*RoomScale, (12000.0-480.0)*RoomScale, r\z + 4352.0 * RoomScale, 0, r, False, False, 2)
	If SelectedEnding = "A2" Then 
		d2\AutoClose = False : d2\open = True : d2\locked = True	
	Else
		d2\AutoClose = False : d2\open = False : d2\locked = False
	EndIf
	PositionEntity d2\buttons[0], r\x-1320.0*RoomScale, EntityY(d2\buttons[0],True), r\z + 4384.0*RoomScale, True
	RotateEntity d2\buttons[0], 0, 180, 0, True	
	PositionEntity d2\buttons[1], r\x-1584.0*RoomScale, EntityY(d2\buttons[0],True), r\z + 4232.0*RoomScale, True	
	RotateEntity d2\buttons[1], 0, 90, 0, True	
	
	For r2.Rooms = Each Rooms
		If r2\RoomTemplate\Name = "gate_b_entrance" Then
			r\Objects[1]=r2\Objects[1]
			r\Objects[2]=r2\Objects[2]	
		ElseIf r2\RoomTemplate\Name = "gate_a_entrance" Then
			r\RoomDoors[1] = CreateDoor(0, r\x+1544.0*RoomScale,12000.0*RoomScale, r\z-64.0*RoomScale, 90, r, False)
			r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False
			PositionEntity(r\RoomDoors[1]\buttons[0],r\x+1584*RoomScale, EntityY(r\RoomDoors[1]\buttons[0],True), r\z+80*RoomScale, True)
			PositionEntity(r\RoomDoors[1]\buttons[1],r\x+1456*RoomScale, EntityY(r\RoomDoors[1]\buttons[1],True), r\z-208*RoomScale, True)	
			r2\Objects[1] = CreatePivot()
			PositionEntity(r2\Objects[1], r\x+1848.0*RoomScale, 240.0*RoomScale, r\z-64.0*RoomScale, True)
			EntityParent r2\Objects[1], r\obj						
		EndIf
	Next
	
	;106:n spawnpoint
	r\Objects[3]=CreatePivot()
	PositionEntity(r\Objects[3], r\x+1216.0*RoomScale, 0, r\z+2112.0*RoomScale, True)
	EntityParent r\Objects[3], r\obj
	
	r\Objects[4]=CreatePivot()
	PositionEntity(r\Objects[4], r\x, 96.0*RoomScale, r\z+6400.0*RoomScale, True)
	EntityParent r\Objects[4], r\obj		
	
	r\Objects[5]=CreatePivot()
	PositionEntity(r\Objects[5], r\x+1784.0*RoomScale, 2124.0*RoomScale, r\z+4512.0*RoomScale, True)
	EntityParent r\Objects[5], r\obj	
	
	r\Objects[6]=CreatePivot()
	PositionEntity(r\Objects[6], r\x-5048.0*RoomScale, 1912.0*RoomScale, r\z+4656.0*RoomScale, True)
	EntityParent r\Objects[6], r\obj	
	
	r\Objects[7]=CreatePivot()
	PositionEntity(r\Objects[7], r\x+1824.0*RoomScale, 224.0*RoomScale, r\z+7056.0*RoomScale, True)
	EntityParent r\Objects[7], r\obj	
	
	r\Objects[8]=CreatePivot()
	PositionEntity(r\Objects[8], r\x-1824.0*RoomScale, 224.0*RoomScale, r\z+7056.0*RoomScale, True)
	EntityParent r\Objects[8], r\obj	
	
	r\Objects[9]=CreatePivot()
	PositionEntity(r\Objects[9], r\x+2624.0*RoomScale, 992.0*RoomScale, r\z+6157.0*RoomScale, True)
	EntityParent r\Objects[9], r\obj
	
	r\Objects[11]=CreatePivot()
	PositionEntity(r\Objects[11], r\x-4064.0*RoomScale, -1248.0*RoomScale, r\z-1696.0*RoomScale, True)
	EntityParent r\Objects[11], r\obj
	
	r\Objects[13]=LoadMesh_Strict("GFX\map\rooms\gate_a_topside\gateawall1.b3d",r\obj)
	PositionEntity(r\Objects[13], r\x-4308.0*RoomScale, -1045.0*RoomScale, r\z+544.0*RoomScale, True)
	EntityColor r\Objects[13], 25,25,25
	EntityType r\Objects[13],HIT_MAP
	
	r\Objects[14]=LoadMesh_Strict("GFX\map\rooms\gate_a_topside\gateawall2.b3d",r\obj)
	PositionEntity(r\Objects[14], r\x-3820.0*RoomScale, -1045.0*RoomScale, r\z+544.0*RoomScale, True)	
	EntityColor r\Objects[14], 25,25,25
	EntityType r\Objects[14],HIT_MAP
	
	r\Objects[15]=CreatePivot(r\obj)
	PositionEntity(r\Objects[15], r\x-3568.0*RoomScale, -1089.0*RoomScale, r\z+4944.0*RoomScale, True)
	
	r\Objects[16] = LoadMesh_Strict("GFX\map\rooms\gate_a_topside\gatea_hitbox1.b3d",r\obj)
	EntityPickMode r\Objects[16],2
	EntityType r\Objects[16],HIT_MAP
	EntityAlpha r\Objects[16],0.0
	
End Function

Function UpdateEvent_Gate_A_Topside(e.Events)
	Local r.Rooms,n.NPCs,de.Decals,p.Particles
	Local xtemp#,ztemp#,temp%,angle#,dist#,pvt%
	Local i
	
	If PlayerRoom = e\room Then 
		For r.Rooms = Each Rooms
			HideEntity r\obj
		Next					
		ShowEntity e\room\obj
		
		If e\EventState[0] = 0 Then
			
			DrawLoading(0,False,"","Sky")
			e\room\Objects[0] = LoadRMesh("GFX\map\rooms\gate_a_topside\gateatunnel_opt.rmesh",Null)
			PositionEntity e\room\Objects[0], EntityX(e\room\obj,True),EntityY(e\room\obj,True),EntityZ(e\room\obj,True)
			ScaleEntity (e\room\Objects[0],RoomScale,RoomScale,RoomScale)
			EntityType e\room\Objects[0], HIT_MAP
			EntityPickMode e\room\Objects[0], 3
			EntityParent(e\room\Objects[0],e\room\obj)
			
			DrawLoading(30,False,"","Lights")
			
			For i = 0 To e\room\MaxLights
				If e\room\LightSprites[i]<>0 Then 
					EntityFX e\room\LightSprites[i], 1+8
				EndIf
			Next
			
			For n.NPCs = Each NPCs
				If n <> Curr106 And n <> Curr173
					RemoveNPC(n)
				EndIf
			Next
			Curr173\Idle = SCP173_DISABLED
			
			CameraFogMode(Camera, 0)
			SecondaryLightOn = True
			
			HideDistance = 35.0
			
			CreateConsoleMsg("WARNING! Teleporting away from this area may cause bugs or crashing.")
			
			TranslateEntity(e\room\obj, 0,12000.0*RoomScale,0)
			TranslateEntity(Collider, 0,12000.0*RoomScale,0)
			
			Sky = sky_CreateSky("GFX\map\sky\sky")
			RotateEntity Sky,0,e\room\angle,0
			
			DrawLoading(60,False,"","NPCs")
			
			For n.NPCs = Each NPCs
				If n\NPCtype = NPCtypeNTF Then Delete n
			Next
			
			For i = 0 To 1
				e\room\NPC[i] = CreateNPC(NPCtypeGuard, EntityX(e\room\Objects[i+5],True),EntityY(e\room\Objects[i+5],True),EntityZ(e\room\Objects[i+5],True))
				e\room\NPC[i]\State[0] = 0
				PointEntity e\room\NPC[i]\Collider, e\room\Objects[3]
			Next
			
			For i = 7 To 8
				e\room\NPC[i] = CreateNPC(NPCtypeNTF, EntityX(e\room\Objects[i],True)+0.8,EntityY(e\room\Objects[i],True),EntityZ(e\room\Objects[i],True)+0.8)
				e\room\NPC[i]\State[0] = 5
				e\room\NPC[i]\PrevState = 1
				PointEntity e\room\NPC[i]\Collider, e\room\Objects[3]
			Next	
			
			For i = 5 To 6
				e\room\NPC[i] = CreateNPC(NPCtypeNTF, EntityX(e\room\Objects[i+2],True),EntityY(e\room\Objects[i+2],True),EntityZ(e\room\Objects[i+2],True))
				e\room\NPC[i]\State[0] = 5
				e\room\NPC[i]\PrevState = 1
				PointEntity e\room\NPC[i]\Collider, e\room\Objects[3]
			Next	
			
			e\room\NPC[10] = CreateNPC(NPCtypeD9341,EntityX(e\room\obj,True),EntityY(e\room\obj,True)+0.35,EntityZ(e\room\obj,True))
			
			If Curr106\Contained Then
				e\room\RoomDoors[2]\locked = True
				
				PositionEntity e\room\NPC[5]\Collider, EntityX(e\room\Objects[15],True)+(i-6)*0.2,EntityY(e\room\Objects[15],True),EntityZ(e\room\Objects[15],True)+(i-6)*0.2, True
				ResetEntity e\room\NPC[5]\Collider
				
			EndIf
			
			xtemp#=EntityX(e\room\Objects[9],True)
			ztemp#=EntityZ(e\room\Objects[9],True)
			e\room\Objects[9] = FreeEntity_Strict(e\room\Objects[9])
			
			e\room\Objects[9] = LoadMesh_Strict("GFX\map\rooms\gate_a_topside\lightgunbase.b3d")
			ScaleEntity e\room\Objects[9], RoomScale,RoomScale,RoomScale
			EntityFX(e\room\Objects[9],0)
			PositionEntity(e\room\Objects[9], xtemp, (992.0+12000.0)*RoomScale, ztemp)
			e\room\Objects[10] = LoadMesh_Strict("GFX\map\rooms\gate_a_topside\lightgun.b3d")
			EntityFX(e\room\Objects[10],0)
			ScaleEntity e\room\Objects[10], RoomScale,RoomScale,RoomScale
			PositionEntity(e\room\Objects[10], xtemp, (992.0+12000.0+288.0)*RoomScale, ztemp-176.0*RoomScale,True)
			EntityParent e\room\Objects[10],e\room\Objects[9]
			RotateEntity e\room\Objects[9], 0, 48, 0
			RotateEntity e\room\Objects[10], 40, 0, 0
			
			For temp = 0 To 20
				For i = 0 To 1
					TranslateEntity e\room\NPC[i]\Collider, 0, -0.04, 0
				Next							
				For i = 5 To 8
					TranslateEntity e\room\NPC[i]\Collider, 0, -0.04, 0
				Next
			Next
			
			ResetEntity Collider
			e\EventState[0] = 1.0
			
			RotateEntity Collider,0,EntityYaw(Collider)+(e\room\angle+180),0
			
			If (Not Curr106\Contained) Then PlaySound_Strict LoadTempSound("SFX\Ending\GateA\106Escape.ogg") 
			
			DrawLoading(100,False,"","Done!")
		Else
			
			ShouldPlay = 17
			
			e\EventState[0] = e\EventState[0]+FPSfactor
			HideEntity Fog
			CameraFogRange Camera, 5,30
			
			angle = Max(Sin(EntityYaw(Collider)+90),0.0)
			CameraFogColor (Camera,200+(angle*40),200+(angle*20),200)
			CameraClsColor (Camera,200+(angle*40),200+(angle*20),200)		
			CameraRange(Camera, 0.01, 30)
			
			AmbientLight (140, 140, 140)
			
			UpdateSky(Sky)
			
			If e\EventState[2] = 0 Then
				e\room\NPC[10]\State[0] = 2
				e\room\NPC[10]\PathStatus = FindPath(e\room\NPC[10],EntityX(e\room\Objects[11],True),EntityY(e\room\Objects[11],True),EntityZ(e\room\Objects[11],True))
				e\room\NPC[10]\PathTimer = 70*Rand(15,20)
				
				e\EventState[2] = 1
			ElseIf e\EventState[2] = 1 And e\EventState[0]>1000 Then
				e\room\NPC[10]\State[0] = 1
				e\room\NPC[10]\PathStatus = FindPath(e\room\NPC[10],EntityX(e\room\Objects[11],True),EntityY(e\room\Objects[11],True),EntityZ(e\room\Objects[11],True))
				e\room\NPC[10]\PathTimer = 70*Rand(15,20)
				
				e\EventState[2] = 2
			EndIf
			
			If e\EventState[0]=>10 Then
				If Curr106\Contained=False Then
					If e\EventState[0]-FPSfactor < 10
						Curr106\State[0] = -0.1
						SetNPCFrame(Curr106, 110.0)
						PositionEntity (Curr106\Collider, EntityX(e\room\Objects[3],True),EntityY(Collider)-100.0,EntityZ(e\room\Objects[3],True),True)
						PositionEntity (Curr106\obj, EntityX(e\room\Objects[3],True),EntityY(Collider)-100.0,EntityZ(e\room\Objects[3],True),True)
						de.Decals = CreateDecal(DECAL_DECAY, EntityX(e\room\Objects[3],True),EntityY(e\room\Objects[3],True)+0.01,EntityZ(e\room\Objects[3],True), 90, Rand(360), 0)
						de\Size = 0.05 : de\SizeChange = 0.001 : EntityAlpha(de\obj, 0.8) : UpdateDecals() 
						PlaySound_Strict (HorrorSFX[5])
						PlaySound_Strict DecaySFX[0]
					ElseIf Curr106\State[0] < 0
						HideEntity Curr106\obj2
						Curr106\PathTimer = 70*100
						
						If Curr106\State[2] = 0 Then
							If Curr106\PathStatus <> 1 Then
								PositionEntity Curr106\Collider,EntityX(e\room\Objects[3],True),EntityY(Curr106\Collider),EntityZ(e\room\Objects[3],True),True
								If Curr106\State[0] =< -10 Then
									dist# = EntityY(Curr106\Collider)
									PositionEntity Curr106\Collider,EntityX(Curr106\Collider),EntityY(e\room\Objects[3],True),EntityZ(Curr106\Collider),True
									Curr106\PathStatus = FindPath(Curr106,EntityX(e\room\NPC[5]\Collider,True),EntityY(e\room\NPC[5]\Collider,True),EntityZ(e\room\NPC[5]\Collider,True))
									Curr106\PathTimer = 70*200
									PositionEntity Curr106\Collider,EntityX(Curr106\Collider),dist,EntityZ(Curr106\Collider),True
									ResetEntity Curr106\Collider
									Curr106\PathLocation = 1
								EndIf
							Else
								Curr106\PathTimer = 70*200
								
								For i = 5 To 8
									e\room\NPC[i]\State[0] = 5
									e\room\NPC[i]\EnemyX = EntityX(Curr106\obj,True)
									e\room\NPC[i]\EnemyY = EntityY(Curr106\obj,True)+0.4
									e\room\NPC[i]\EnemyZ = EntityZ(Curr106\obj,True)											
								Next
								
								pvt=CreatePivot()
								PositionEntity pvt, EntityX(e\room\Objects[10],True),EntityY(e\room\Objects[10],True),EntityZ(e\room\Objects[10],True)
								PointEntity pvt, Curr106\Collider
								RotateEntity(e\room\Objects[9],0,CurveAngle(EntityYaw(pvt),EntityYaw(e\room\Objects[9],True),150.0),0,True)
								RotateEntity(e\room\Objects[10],CurveAngle(EntityPitch(pvt),EntityPitch(e\room\Objects[10],True),200.0),EntityYaw(e\room\Objects[9],True),0, True)
								
								pvt = FreeEntity_Strict(pvt)
								
								If FPSfactor > 0 Then ;decals under 106
									If ((e\EventState[0]-FPSfactor) Mod 100.0)=<50.0 And (e\EventState[0] Mod 100.0)>50.0 Then
										de.Decals = CreateDecal(DECAL_DECAY, EntityX(Curr106\Collider,True),EntityY(e\room\Objects[3],True)+0.01,EntityZ(Curr106\Collider,True), 90, Rand(360), 0)
										de\Size = 0.2 : de\SizeChange = 0.004 : de\Timer = 90000 : EntityAlpha(de\obj, 0.8) : UpdateDecals() 											
									EndIf
								EndIf
							EndIf
						EndIf
						
						dist# = Distance(EntityX(Curr106\Collider),EntityX(e\room\Objects[4],True),EntityZ(Curr106\Collider),EntityZ(e\room\Objects[4],True))
						
						Curr106\CurrSpeed = CurveValue(0, Curr106\CurrSpeed, Max(5*dist,2.0))
						If dist < 15.0 Then
							If e\SoundCHN[1] = 0 Then
								LoadEventSound(e,"SFX\Ending\GateA\Franklin.ogg")
							EndIf
							
							If dist<0.4 Then
								Curr106\PathStatus = 0
								Curr106\PathTimer = 70*200
								If Curr106\State[2]=0 Then 
									SetNPCFrame(Curr106, 259.0)
									If e\Sound[0] <> 0 Then FreeSound_Strict e\Sound[0] : e\Sound[0] = 0
									LoadEventSound(e,"SFX\Ending\GateA\106Retreat.ogg")
									e\SoundCHN[0] = PlaySound2(e\Sound[0], Camera, Curr106\Collider, 35.0)
								EndIf
								
								If FPSfactor > 0 Then
									If ((e\EventState[0]-FPSfactor) Mod 160.0)=<50.0 And (e\EventState[0] Mod 160.0)>50.0 Then
										de.Decals = CreateDecal(DECAL_DECAY, EntityX(Curr106\Collider,True),EntityY(e\room\Objects[3],True)+0.01,EntityZ(Curr106\Collider,True), 90, Rand(360), 0)
										de\Size = 0.05 : de\SizeChange = 0.004 : de\Timer = 90000 : EntityAlpha(de\obj, 0.8) : UpdateDecals() 											
									EndIf
								EndIf
								
								AnimateNPC(Curr106, 259, 110, -0.1, False)
								
								Curr106\State[2] = Curr106\State[2]+FPSfactor
								PositionEntity(Curr106\Collider, EntityX(Curr106\Collider,True),CurveValue(EntityY(e\room\Objects[3],True)-(Curr106\State[2]/4500.0),EntityY(Curr106\Collider,True),100.0),EntityZ(Curr106\Collider,True))
								If Curr106\State[2]>700.0 Then
									Curr106\State[0] = 100000
									e\EventState[1] = 0
									For i = 5 To 8
										e\room\NPC[i]\State[0] = 1
									Next
									HideEntity Curr106\obj
								EndIf
							Else
								If dist < 8.5 Then 
									If e\EventState[1]=0
										LoadEventSound(e,"SFX\Ending\GateA\HIDTurret.ogg")
										e\EventState[1] = 1
									ElseIf e\EventState[1]>0
										e\EventState[1]=e\EventState[1]+FPSfactor
										If e\EventState[1]=> 7.5*70 Then
											If e\EventState[1]-FPSfactor < 7.5*70 Then
												p.Particles = CreateParticle(EntityX(Curr106\obj,True),EntityY(Curr106\obj,True)+0.4, EntityZ(Curr106\obj,True), 1, 7.0, 0, (6.7*70)); ~ Flash
												p\speed = 0.0
												p\A = 1.0
												EntityParent p\pvt, Curr106\Collider, True
												
												p.Particles = CreateParticle(EntityX(e\room\Objects[10],True),EntityY(e\room\Objects[10],True),EntityZ(e\room\Objects[10],True), 4, 2.0, 0, (6.7*70))
												RotateEntity p\pvt, EntityPitch(e\room\Objects[10],True),EntityYaw(e\room\Objects[10],True),0,True
												MoveEntity p\pvt, 0, 92.0*RoomScale, 512.0*RoomScale
												p\speed = 0.0
												p\A = 1.0
												EntityParent p\pvt, e\room\Objects[10], True
											ElseIf e\EventState[1] < 14.3*70
												CameraShake = 0.5
												LightFlash = 0.3+EntityInView(e\room\Objects[10],Camera)*0.5
											EndIf
										EndIf
									EndIf
									
									If ParticleAmount > 0
										For i = 0 To Rand(2,2+(6*(ParticleAmount-1)))-Int(dist)
											p.Particles = CreateParticle(EntityX(Curr106\obj,True),EntityY(Curr106\obj,True)+Rnd(0.4,0.9), EntityZ(Curr106\obj), 0, 0.006, -0.002, 40)
											p\speed = 0.005
											p\A = 0.8
											p\Achange = -0.01
											RotateEntity p\pvt, -Rnd(70,110), Rnd(360),0	
										Next										
									EndIf
								EndIf
								
								
							EndIf
						EndIf
					EndIf
					If Abs(EntityY(Collider)-EntityY(e\room\Objects[11],True))<1.0 Then
						If DistanceSquared(EntityX(Collider),EntityX(e\room\Objects[11],True),EntityZ(Collider),EntityZ(e\room\Objects[11],True)) < PowTwo(19.0) Then
							If ChannelPlaying(e\SoundCHN[0])=False And SelectedEnding="" Then
								PlaySound_Strict LoadTempSound("SFX\Ending\GateA\TunnelCollapse.ogg")
								SelectedEnding = "A1"
								GodMode = 0
								NoClip = 0
								KillTimer = -0.1
								m_msg\DeathTxt = ""
								Kill()
								RemoveEvent(e)
							EndIf
							
							If SelectedEnding <> "" Then
								CameraShake=CurveValue(2.0,CameraShake,10.0)
								LightFlash = CurveValue(2.0,LightFlash,8.0)
							EndIf
						EndIf
					EndIf
					
				Else ;Curr106\Contained = true
					
					If e\EventState[1] = 0 Then
						e\EventState[1] = 1
						
						For i = 5 To 8
							e\room\NPC[i]\State[0] = 3
							
							e\room\NPC[i]\PathStatus = FindPath(e\room\NPC[i], EntityX(e\room\obj)-1.0+2.0*(i Mod 2),EntityY(Collider)+0.2,EntityZ(e\room\obj)-2.0*(i Mod 2))
							e\room\NPC[i]\PathTimer = 70*Rand(15,20)
							e\room\NPC[i]\LastSeen = 70*300
						Next
						e\room\NPC[10]\State[0] = 2
						
						e\room\NPC[10]\PathStatus = FindPath(e\room\NPC[10],EntityX(e\room\Objects[4],True),EntityY(e\room\Objects[4],True),EntityZ(e\room\Objects[4],True))
						e\room\NPC[10]\PathTimer = 70*Rand(15,20)
					Else
						
						For i = 5 To 8
							If e\room\NPC[i]\State[0] = 5
								e\room\NPC[i]\EnemyX = EntityX(e\room\NPC[10]\Collider)
								e\room\NPC[i]\EnemyY = EntityY(e\room\NPC[10]\Collider)
								e\room\NPC[i]\EnemyZ = EntityZ(e\room\NPC[10]\Collider)
							Else
								If EntityDistanceSquared(e\room\NPC[i]\Collider,e\room\NPC[10]\Collider)<PowTwo(6.0)
									e\room\NPC[i]\State[0] = 5
									e\room\NPC[i]\CurrSpeed = 0
								EndIf
							EndIf
						Next
						
						If e\EventState[1]=<1 Then
							For i = 5 To 8
								If e\room\NPC[i]\State[0] = 5 Then
									For temp = 5 To 8
										e\room\NPC[temp]\State[0] = 5
										e\room\NPC[temp]\EnemyX = EntityX(e\room\NPC[10]\Collider)
										e\room\NPC[temp]\EnemyY = EntityY(e\room\NPC[10]\Collider)
										e\room\NPC[temp]\EnemyZ = EntityZ(e\room\NPC[10]\Collider)
										e\room\NPC[temp]\PathTimer = 70*Rand(7,10)
										e\room\NPC[temp]\Reload = 2000
										e\room\NPC[10]\State[0] = 0
									Next
									
									If e\EventState[1]=1 Then
										e\SoundCHN[0] = PlaySound_Strict (LoadTempSound("SFX\Ending\GateA\STOPRIGHTTHERE.ogg"))
										e\EventState[1]=2			
									EndIf
								Else
									e\room\NPC[i]\LastSeen = 70*300
									e\room\NPC[i]\Reload = 2000
									e\room\NPC[i]\State[2] = 70*145											
								EndIf
							Next										
						Else
							
							ShouldPlay = 0
							CurrSpeed = 0
							If ChannelPlaying(e\SoundCHN[0])=False Then
								PlaySound_Strict IntroSFX[0]
								SelectedEnding = "A2"
								GodMode = 0
								NoClip = 0
								KillTimer = -0.1
								m_msg\DeathTxt = ""
								Kill()
								IsCutscene = True
								BlinkTimer = -10
								IsCutscene = False
								RemoveEvent(e)
							EndIf
						EndIf									
						
					EndIf
					
				EndIf
			EndIf
			
		EndIf
	Else
		HideEntity e\room\obj
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D