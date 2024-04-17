
Function FillRoom_Gate_C_Topside(r.Rooms)
	Local ne.NewElevator,it.Items
	
	it = CreateItem("RPK-16","rpk16",r\x+4975*RoomScale,r\y+1682*RoomScale,r\z+1830*RoomScale)
	it\state = 30 : it\state2 = 210
	EntityParent it\collider, r\obj
	
;	it = CreateItem(GetLocalString("Item Names","vest_heavy"),"vest2",r\x+4975*RoomScale,r\y+1682*RoomScale,r\z+1830*RoomScale)
;	EntityParent it\collider, r\obj
	
	r\Objects[0] = LoadMesh_Strict("GFX\map\rooms\gate_c_topside\gate_c_mountains.b3d",r\obj)
	EntityPickMode r\Objects[0],2
	EntityType r\Objects[0],HIT_MAP
	EntityFX r\Objects[0], 2
	LightMesh r\Objects[0],-120,-120,-120
	
	r\Objects[1]=CreatePivot(); ~ Rocket Launch Button
	PositionEntity(r\Objects[1], r\x+5052.0*RoomScale, r\y+1607.0*RoomScale, r\z+1169.0*RoomScale, True)
	EntityParent r\Objects[1], r\obj
	
	r\Objects[2]=LoadAnimMesh_Strict("GFX\Map\Props\Satellite_Rocket.b3d"); ~ Rocket Itself
	ScaleEntity r\Objects[2], RoomScale*50,RoomScale*50,RoomScale*50
	PositionEntity(r\Objects[2], r\x+1002.0*RoomScale, r\y-450.0*RoomScale, r\z+1493.0*RoomScale, True)
	MeshCullBox (r\Objects[2], -MeshWidth(r\Objects[2]), -MeshHeight(r\Objects[2]), -MeshDepth(r\Objects[2]), MeshWidth(r\Objects[2])*6, MeshHeight(r\Objects[2])*6, MeshDepth(r\Objects[2])*6)
	EntityParent r\Objects[2], r\obj
	
	r\Objects[3] = CreateLight(3 ,r\Objects[2]); ~ Rocket Light
	;MoveEntity(r\Objects[3],0,0,0.01)
	PositionEntity(r\Objects[3], r\x+1002.0*RoomScale, r\y-450.0*RoomScale, r\z+1493.0*RoomScale, True)
	LightRange(r\Objects[3], 10.5)
	LightColor(r\Objects[3], 235.0 * 5.0, 55.0 * 5.0, 0.0)
	RotateEntity(r\Objects[3], 0.0, 270.0, 0.0)
	LightConeAngles(r\Objects[3], 10.0, 95.0)
	
	r\Objects[4] = CreateLight(3 ,r\Objects[2]); ~ Rocket Light 2
	;MoveEntity(r\Objects[4],0,0,0.01)
	PositionEntity(r\Objects[4], r\x+1002.0*RoomScale, r\y-450.0*RoomScale, r\z+1493.0*RoomScale, True)
	LightRange(r\Objects[4], 10.5)
	LightColor(r\Objects[4], 235.0 * 5.0, 55.0 * 5.0, 0.0)
	RotateEntity(r\Objects[4], 0.0, 0.0, 0.0)
	LightConeAngles(r\Objects[4], 10.0, 95.0)
	
	r\Objects[5] = CreateLight(3 ,r\Objects[2]); ~ Rocket Light 3
	;MoveEntity(r\Objects[5],0,0,0.01)
	PositionEntity(r\Objects[5], r\x+1002.0*RoomScale, r\y-450.0*RoomScale, r\z+1493.0*RoomScale, True)
	LightRange(r\Objects[5], 10.5)
	LightColor(r\Objects[5], 235.0 * 5.0, 55.0 * 5.0, 0.0)
	RotateEntity(r\Objects[5], 0.0, 90.0, 0.0)
	LightConeAngles(r\Objects[5], 10.0, 95.0)
	
	r\Objects[6] = CreateLight(3 ,r\Objects[2]); ~ Rocket Light 4
	;MoveEntity(r\Objects[6],0,0,0.01)
	PositionEntity(r\Objects[6], r\x+1002.0*RoomScale, r\y-450.0*RoomScale, r\z+1493.0*RoomScale, True)
	LightRange(r\Objects[6], 10.5)
	LightColor(r\Objects[6], 235.0 * 5.0, 55.0 * 5.0, 0.0)
	RotateEntity(r\Objects[6], 0.0, 180.0, 0.0)
	LightConeAngles(r\Objects[6], 10.0, 95.0)
	
	r\Objects[8]=LoadAnimMesh_Strict("GFX\Map\Props\Rocket_Silo_Door.b3d"); ~ Rocket Silo Door
	ScaleEntity r\Objects[8], RoomScale*1,RoomScale*1,RoomScale*1
	PositionEntity(r\Objects[8], r\x+969.0*RoomScale, r\y-2.0*RoomScale, r\z+1487.0*RoomScale, True)
	EntityParent r\Objects[8], r\obj
	EntityType r\Objects[8],HIT_MAP
	EntityPickMode r\Objects[8],2
	
	Local ElevOBJ% = LoadRMesh("GFX\map\Elevators\elevator.rmesh",Null)
	HideEntity ElevOBJ%
	r\Objects[10] = CopyEntity(ElevOBJ%,r\obj)
	
;	PositionEntity(r\Objects[9],576,0,640)
;	EntityType r\Objects[9],HIT_MAP
;	EntityPickMode r\Objects[9],2
	
	RotateEntity r\Objects[10],0,270,0
	PositionEntity(r\Objects[10],-3238,382,-6533)
	EntityType r\Objects[10],HIT_MAP
	EntityPickMode r\Objects[10],2
	
	r\Objects[11]=CreatePivot(); ~ CI Spawn
	PositionEntity(r\Objects[11], r\x-689.0*RoomScale, r\y+400.0*RoomScale, r\z-2765.0*RoomScale, True)
	EntityParent r\Objects[11], r\obj
	
	;CreateDarkSprite(r,12)
	
	r\Objects[13]=CreatePivot(); ~ CI Spawn 2
	PositionEntity(r\Objects[13], r\x-84.0*RoomScale, r\y+676.0*RoomScale, r\z+4903.0*RoomScale, True)
	EntityParent r\Objects[13], r\obj
	
	r\Objects[14]=LoadAnimMesh_Strict("GFX\Map\Props\gate_c_rocket_elevator.b3d"); ~ Rocket Elevator
	ScaleEntity r\Objects[14], RoomScale*50,RoomScale*50,RoomScale*50
	RotateEntity r\Objects[14],0,90,0
	PositionEntity(r\Objects[14], r\x+1002.0*RoomScale, r\y-450.0*RoomScale, r\z+1493.0*RoomScale, True)
	MeshCullBox (r\Objects[14], -MeshWidth(r\Objects[14]), -MeshHeight(r\Objects[14]), -MeshDepth(r\Objects[14]), MeshWidth(r\Objects[14])*2, MeshHeight(r\Objects[14])*2, MeshDepth(r\Objects[14])*2)
	EntityParent r\Objects[14], r\obj
	
	r\Objects[15] = LoadMesh_Strict("GFX\map\rooms\gate_c_topside\gate_c_mesh.b3d",r\obj)
	EntityPickMode r\Objects[15],2
	EntityType r\Objects[15],HIT_MAP
	EntityFX r\Objects[15], 2
	LightMesh r\Objects[15],-120,-120,-120
	HideEntity r\Objects[15]
	
	r\Objects[16] = LoadMesh_Strict("GFX\map\rooms\gate_c_topside\gate_c_screen.b3d",r\obj)
	EntityPickMode r\Objects[16],2
	EntityType r\Objects[16],HIT_MAP
	EntityFX r\Objects[16], 2
	
	r\Objects[17]=CreatePivot()
	PositionEntity(r\Objects[17], EntityX(FindChild(r\Objects[2],"root2"),True), EntityY(FindChild(r\Objects[2],"root2"),True), EntityZ(FindChild(r\Objects[2],"root2"),True), True)
	EntityParent r\Objects[17], FindChild(r\Objects[2],"root2")
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x -3237.0 * RoomScale, r\y+382*RoomScale, r\z-6225*RoomScale, 0, r, True, 5, False, "", 2)
	r\RoomDoors[0]\DisableWaypoint = True
	
	ne = CreateNewElevator(r\Objects[10],3,r\RoomDoors[0],2,r,-2800.0,0.0,418.0)
	ne\door\locked = True
	
End Function

Function UpdateEvent_Gate_C_Topside(e.Events)
	Local r.Rooms,n.NPCs,de.Decals,p.Particles
	Local xtemp#,ztemp#,temp%,angle#,dist#,pvt%
	Local i
	
	If PlayerRoom = e\room Then
		
		; ~ Screen
		
		e\EventState[7] = e\EventState[7] + FPSfactor
		
		If e\EventState[7] > 70*5 Then
			If ecst\SuccessRocketLaunch Then
				EntityTexture e\room\Objects[16], Satellite_Screen[0], Floor(((e\EventState[7]-70*5)/70) Mod 4.0)
			Else
				EntityTexture e\room\Objects[16], Satellite_Screen[1], Floor(((e\EventState[7]-70*5)/70) Mod 4.0)
			EndIf
		EndIf
		
		; ~ Event
		
		If e\EventState[0] = 0 Then
			StartChapter("chapter_9", 9)
		EndIf
		
		If TaskExists(TASK_GET_TOPSIDE) Then
			EndTask(TASK_GET_TOPSIDE)
		EndIf
		
		For r.Rooms = Each Rooms
			HideEntity r\obj
		Next					
		ShowEntity e\room\obj
		ShowEntity e\room\Objects[2]
		ShowEntity e\room\Objects[8]
		ShowEntity e\room\Objects[14]
		
		CanSave = False
		
		If e\EventState[0] = 0 Then
			
			DrawLoading(0, False, "")
			
			DrawLoading(30, False, "", "SKY")
			
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
			Curr173\Idle = True
			
			CameraFogMode(Camera, 0)
			SecondaryLightOn = True
			
			HideDistance = 35.0
			
			e\EventState[1] = 0.0
			
			CreateConsoleMsg("WARNING! Teleporting away from this area may cause bugs or crashing.")
			
			Sky = sky_CreateSky("GFX\map\sky\sky_night")
			RotateEntity Sky,0,e\room\angle,0
			
			DrawLoading(60, False, "", "PLAYER")
			
			ResetEntity Collider
			e\EventState[0] = 1.0
			
			RotateEntity Collider,0,EntityYaw(Collider)+(e\room\angle+0),0
			TeleportEntity Collider,e\room\x-3238*RoomScale,e\room\y+418*RoomScale,e\room\z-6533*RoomScale
			
			DrawLoading(100, False, "", "LOADING_DONE")
			
			Local PrevAnim# = AnimTime(e\room\Objects[2])
			
		Else
			
			If Sky = 0 Then
				Sky = sky_CreateSky("GFX\map\sky\sky_night")
				RotateEntity Sky,0,e\room\angle,0
			EndIf
			
			For i = 0 To e\room\MaxLights
				If e\room\Lights[i]<>0 Then 
					HideEntity e\room\Lights[i]
				EndIf
			Next
			
			If (Not e\EventState[2] >= 70.0) And e\EventState[2] < 70*70 Then
				If ecst\SuccessRocketLaunch Then
					ShouldPlay = MUS_SURFACE
				Else
					ShouldPlay = MUS_THE_UNKNOWN
				EndIf
			Else
				If ecst\SuccessRocketLaunch Then
					If e\EventState[2] < 70*70 Then
						ShouldPlay = MUS_NULL
						If ChannelPlaying(e\SoundCHN[1])=False Then
							If opt\MusicVol > 0 Then
								e\SoundCHN[1] = PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Rocket_Launch_Success.ogg"))
							EndIf
						EndIf
					Else
						ShouldPlay = MUS_NULL
					EndIf
				Else
					ShouldPlay = MUS_NULL
				EndIf
			EndIf
			
			e\EventState[0] = e\EventState[0] + FPSfactor
			HideEntity Fog
			CameraFogRange Camera, 5,200
			
			angle = Max(Sin(EntityYaw(Collider)+90),0.0)
			CameraFogColor(Camera,2,2,20)
			CameraClsColor(Camera,2,2,20)
			CameraRange(Camera, 0.01, 200)
			
			AmbientLight (2, 2, 20)
			
			UpdateSky(Sky)
	; ~ Flight
			If ecst\SuccessRocketLaunch Then
				If e\EventState[2] = 0 Then
					SetAnimTime(e\room\Objects[2],1)
					SetAnimTime(e\room\Objects[14],1)
				ElseIf e\EventState[2] >= 70*50 Then
					SetAnimTime(e\room\Objects[2],750)
					SetAnimTime(e\room\Objects[14],750)
				EndIf
				If e\EventState[2] >= 70.0 Then
					e\EventState[2] = e\EventState[2] + FPSfactor
					e\EventState[6] = e\EventState[6] + FPSfactor
					Animate2(e\room\Objects[2], AnimTime(e\room\Objects[2]), 1, 750, 0.24,False)
					Animate2(e\room\Objects[8], AnimTime(e\room\Objects[8]), 1, 720, 0.24,False)
					Animate2(e\room\Objects[14], AnimTime(e\room\Objects[14]), 1, 750, 0.24,False)
					HideEntity e\room\Objects[3]
					HideEntity e\room\Objects[4]
					HideEntity e\room\Objects[5]
					HideEntity e\room\Objects[6]
					HideEntity e\room\Objects[15]
				EndIf
				;If e\EventState[6] > 0 And e\EventState[6] < 70*40 Then
				If e\EventState[6] > 70*14 And e\EventState[6] < 70*40 Then
					HolsterGun()
					TeleportEntity(Camera,5294*RoomScale,2673*RoomScale,-587*RoomScale,0.3,True)
					TeleportEntity(Sky,5294*RoomScale,2673*RoomScale,-587*RoomScale,0.3,True)
					;PositionEntity Camera,5294*RoomScale,2673*RoomScale,-587*RoomScale,True
					PointEntity(Camera,e\room\Objects[17])
				ElseIf e\EventState[6] > 0 And e\EventState[6] < 70*14 Then
					HolsterGun()
					TeleportEntity(Camera,5294*RoomScale,2673*RoomScale,-587*RoomScale,0.3,True)
					TeleportEntity(Sky,5294*RoomScale,2673*RoomScale,-587*RoomScale,0.3,True)
					;PositionEntity Camera,5294*RoomScale,2673*RoomScale,-587*RoomScale,True
					PointEntity(Camera,e\room\Objects[2])
				EndIf
				If e\EventState[2] >= 70*35 And e\EventState[2] < 70*50 Then
					BigCameraShake = 3
					ShowEntity e\room\Objects[3]
					ShowEntity e\room\Objects[4]
					ShowEntity e\room\Objects[5]
					ShowEntity e\room\Objects[6]
					ShowEntity e\room\Objects[15]
					
					MoveEntity(e\room\Objects[3],0,0.55,0)
					MoveEntity(e\room\Objects[4],0,0.55,0)
					MoveEntity(e\room\Objects[5],0,0.55,0)
					MoveEntity(e\room\Objects[6],0,0.55,0)
					
					Local rocketbone% = FindChild(e\room\Objects[2],"flash")
					
					p.Particles = CreateParticle(EntityX(rocketbone,True), EntityY(rocketbone,True), EntityZ(rocketbone,True),1, 1, 0.5, 10)
					EntityParent p\obj,rocketbone
					p.Particles = CreateParticle(EntityX(rocketbone,True), EntityY(rocketbone,True), EntityZ(rocketbone,True),13, 1.5, 1.0, 20)
					EntityParent p\obj,rocketbone
					p.Particles = CreateParticle(EntityX(rocketbone,True), EntityY(rocketbone,True) - 0.45, EntityZ(rocketbone,True),0, 1, 1.0, 20)
					EntityParent p\obj,rocketbone
				Else
					HideEntity e\room\Objects[3]
					HideEntity e\room\Objects[4]
					HideEntity e\room\Objects[5]
					HideEntity e\room\Objects[6]
					HideEntity e\room\Objects[15]
				EndIf
				If e\EventState[2] > 70*70 Then
					e\EventState[3] = 1
					StopStream_Strict(e\SoundCHN[0])
					StopStream_Strict(e\SoundCHN[1])
				EndIf
			EndIf
	; ~ Explosion
			If (Not ecst\SuccessRocketLaunch) Then
				HideEntity e\room\Objects[3]
				HideEntity e\room\Objects[4]
				HideEntity e\room\Objects[5]
				HideEntity e\room\Objects[6]
				HideEntity e\room\Objects[15]
				If e\EventState[2] = 0 Then
					SetAnimTime(e\room\Objects[2],752)
					SetAnimTime(e\room\Objects[14],752)
				ElseIf e\EventState[2] >= 70*28 Then
					SetAnimTime(e\room\Objects[2],1230)
					SetAnimTime(e\room\Objects[14],1230)
				EndIf
				If e\EventState[2] >= 70.0 Then
					e\EventState[2] = e\EventState[2] + FPSfactor
					e\EventState[6] = e\EventState[6] + FPSfactor
					Animate2(e\room\Objects[2], AnimTime(e\room\Objects[2]), 752, 1230, 0.24,False)
					Animate2(e\room\Objects[8], AnimTime(e\room\Objects[8]), 1, 720, 0.25,False)
				EndIf
				;If e\EventState[6] > 0 And e\EventState[6] < 70*40 Then
				If e\EventState[6] > 70*14 And e\EventState[6] < 70*40 Then
					HolsterGun()
					TeleportEntity(Camera,5294*RoomScale,2673*RoomScale,-587*RoomScale,0.3,True)
					TeleportEntity(Sky,5294*RoomScale,2673*RoomScale,-587*RoomScale,0.3,True)
					;PositionEntity Camera,5294*RoomScale,2673*RoomScale,-587*RoomScale,True
					PointEntity(Camera,e\room\Objects[17])
				ElseIf e\EventState[6] > 0 And e\EventState[6] < 70*14 Then
					HolsterGun()
					TeleportEntity(Camera,5294*RoomScale,2673*RoomScale,-587*RoomScale,0.3,True)
					TeleportEntity(Sky,5294*RoomScale,2673*RoomScale,-587*RoomScale,0.3,True)
					;PositionEntity Camera,5294*RoomScale,2673*RoomScale,-587*RoomScale,True
					PointEntity(Camera,e\room\Objects[2])
				EndIf
				If e\EventState[2] >= 70*23.5 And e\EventState[2] < 70*23.525 Then
					BigCameraShake = 35
					e\EventState[5] = 1
					If TaskExists(TASK_LAUNCH_ROCKET) Then
						FailTask(TASK_LAUNCH_ROCKET)
					EndIf
					If (Not ChannelPlaying(e\SoundCHN[0])) Then
						e\SoundCHN[0] = PlaySound_Strict(LoadTempSound("SFX\Ending\GateC\Rocket_Explosion.ogg"))
					EndIf
					If (Not ChannelPlaying(e\SoundCHN[1])) Then
						If opt\MusicVol > 0 Then
							e\SoundCHN[1] = PlaySound_Strict(LoadTempSound("SFX\Music\Misc\Rocket_Launch_Fail.ogg"))
						EndIf
					EndIf
					CreateSplashText(GetLocalString("Singleplayer","mission_failed"),opt\GraphicWidth/2,opt\GraphicHeight/2,500,3,Font_Menu,True,False,255,0,0)
				EndIf
				If e\EventState[2] >= 70*23.5 And e\EventState[2] < 70*28.5 Then
					
					Local rocketbone2% = FindChild(e\room\Objects[2],"root1")
					Local rocketbone3% = FindChild(e\room\Objects[2],"root2")
					
					If EntityVisible(Camera, e\room\Objects[2]) Then
						CreateOverHereParticle(EntityX(rocketbone2,True),EntityY(rocketbone2,True) +1.5,EntityZ(rocketbone2,True),18)
					EndIf
					p.Particles = CreateParticle(EntityX(rocketbone2, True), EntityY(rocketbone2, True) +1.5, EntityZ(rocketbone2, True), 17, 2.5 ,0, 25)
					EntityParent p\obj,rocketbone2
					p.Particles = CreateParticle(EntityX(rocketbone2, True), EntityY(rocketbone2, True) +1.5, EntityZ(rocketbone2, True), 0, -1.2 ,0.5, 205)
					EntityParent p\obj,rocketbone2
					p.Particles = CreateParticle(EntityX(rocketbone3, True), EntityY(rocketbone3, True), EntityZ(rocketbone3, True), 13, 1 ,0, 25)
					EntityParent p\obj,rocketbone3
					p.Particles = CreateParticle(EntityX(rocketbone3, True), EntityY(rocketbone3, True), EntityZ(rocketbone3, True), 0, 1.3 ,0, 25)
					EntityParent p\obj,rocketbone3
				EndIf
			EndIf
			
			If e\EventState[3] = 1 Then
				ShouldPlay = MUS_CYBER_CHASE
				If e\room\NPC[0] = Null Then
					e\room\NPC[0]=CreateNPC(NPC_CI, EntityX(e\room\Objects[11],True), EntityY(e\room\Objects[11],True), EntityZ(e\room\Objects[11],True))
					RotateEntity e\room\NPC[0]\Collider, 0, EntityYaw(e\room\obj)+90,0, True
				EndIf
				If e\room\NPC[1] = Null Then
					e\room\NPC[1]=CreateNPC(NPC_CI, EntityX(e\room\Objects[11],True), EntityY(e\room\Objects[11],True), EntityZ(e\room\Objects[11],True)+0.5)
					RotateEntity e\room\NPC[1]\Collider, 0, EntityYaw(e\room\obj)+90,0, True
				EndIf
				If e\room\NPC[2] = Null Then
					e\room\NPC[2]=CreateNPC(NPC_CI, EntityX(e\room\Objects[11],True), EntityY(e\room\Objects[11],True), EntityZ(e\room\Objects[11],True)+0.7)
					RotateEntity e\room\NPC[2]\Collider, 0, EntityYaw(e\room\obj)+90,0, True
				EndIf
				If e\room\NPC[3] = Null Then
					e\room\NPC[3]=CreateNPC(NPC_CI, EntityX(e\room\Objects[13],True), EntityY(e\room\Objects[13],True), EntityZ(e\room\Objects[13],True))
					RotateEntity e\room\NPC[3]\Collider, 0, EntityYaw(e\room\obj)+90,0, True
				EndIf
				If e\room\NPC[4] = Null Then
					e\room\NPC[4]=CreateNPC(NPC_CI, EntityX(e\room\Objects[13],True), EntityY(e\room\Objects[13],True), EntityZ(e\room\Objects[13],True)+0.5)
					RotateEntity e\room\NPC[4]\Collider, 0, EntityYaw(e\room\obj)+90,0, True
				EndIf
				If e\room\NPC[5] = Null Then
					e\room\NPC[5]=CreateNPC(NPC_CI, EntityX(e\room\Objects[13],True), EntityY(e\room\Objects[13],True), EntityZ(e\room\Objects[13],True)+0.7)
					RotateEntity e\room\NPC[5]\Collider, 0, EntityYaw(e\room\obj)+90,0, True
				EndIf
				
				Local b
				
				If e\EventState[3] > 0 Then
					Local alldead% = True
					For b = 0 To 5
						If e\room\NPC[b] <> Null And (Not e\room\NPC[b]\IsDead) Then
							alldead = False
							Exit
						EndIf
					Next
					If alldead Then
						e\EventState[11] = 1
					EndIf
				EndIf
				If e\EventState[11] = 1 Then
					e\EventState[4] = e\EventState[4] + FPSfactor
				EndIf
				
				If e\EventState[11] = 1 And IsSPPlayerAlive() Then
					ShouldPlay = MUS_CYBER_CHASE_CONTINUE
					psp\IsShowingHUD = False
					If e\EventState[4] >= 70*1 And e\EventState[4] < 70*1.013 Then
						SaveGame(SavePath + CurrSave\Name + "\", True)
						CreateSplashText(GetLocalString("Singleplayer","mission_accomplished"),opt\GraphicWidth/2,opt\GraphicHeight/2,500,3,Font_Menu,True,False,0,40,80)
					EndIf
					If e\EventState[4] >= 70*25 Then
						SelectedEnding = "c1"
						RemoveEvent(e)
						Return
					EndIf
				EndIf
			EndIf
			
			If e\EventState[5] > 0 Then
				e\EventState[4] = e\EventState[4] + FPSfactor
				psp\IsShowingHUD = False
				If e\EventState[4] >= 70*32 Then
					SelectedEnding = "c2"
					RemoveEvent(e)
					Return
				EndIf
			EndIf
			
			If Abs(EntityY(Collider)-EntityY(e\room\Objects[1],True))<1.0 Then
				If DistanceSquared(EntityX(Collider),EntityX(e\room\Objects[1],True),EntityZ(Collider),EntityZ(e\room\Objects[1],True)) < PowTwo(0.8) Then
					If e\EventState[1] <> 1.0 Then
						DrawHandIcon = True
						If KeyHitUse Then
							e\EventState[1] = 1.0
							e\EventState[2] = 70*1
							If (Not ChannelPlaying(e\SoundCHN[3])) Then
								e\SoundCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Ending\GateC\Rocket_Moving.ogg"))
							EndIf
							If ecst\SuccessRocketLaunch = True Then
								SaveGame(SavePath + CurrSave\Name + "\", True)
								If TaskExists(TASK_LAUNCH_ROCKET) Then
									EndTask(TASK_LAUNCH_ROCKET)
								EndIf
								If (Not ChannelPlaying(e\SoundCHN[0])) Then
									e\SoundCHN [0]= PlaySound_Strict(LoadTempSound("SFX\Ending\GateC\Rocket_Launch.ogg"))
								EndIf
							Else
								If (Not ChannelPlaying(e\SoundCHN[3])) Then
									e\SoundCHN[3] = PlaySound_Strict(LoadTempSound("SFX\Ending\GateC\Rocket_Failure.ogg"))
								EndIf
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