
Function FillRoom_Gate_A_Topside(r.Rooms)
	Local d2.Doors,r2.Rooms
	
	CreateDarkSprite(r, 0)
	
	r\Objects[1]=CreatePivot(); ~ Ending Trigger
	PositionEntity(r\Objects[1], r\x-343.0*RoomScale, r\y-1233.0*RoomScale, r\z-5493.0*RoomScale, True)
	EntityParent r\Objects[1], r\obj
	
End Function

Function UpdateEvent_Gate_A_Topside(e.Events)
	Local r.Rooms,n.NPCs,de.Decals,p.Particles
	Local xtemp#,ztemp#,temp%,angle#,dist#,pvt%
	Local i
	
	If PlayerRoom = e\room Then
		
		If e\EventState[0] = 0 Then
			StartChapter("chapter_9", 9)
			;AddShopPoints(200)
		EndIf
		
		If TaskExists(TASK_GET_TOPSIDE) Then
			EndTask(TASK_GET_TOPSIDE)
		EndIf
		
		For r.Rooms = Each Rooms
			HideEntity r\obj
		Next					
		ShowEntity e\room\obj
		
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
			
			CreateConsoleMsg("WARNING! Teleporting away from this area may cause bugs or crashing.")
			
			TranslateEntity(e\room\obj, 0,12000.0*RoomScale,0)
			TranslateEntity(Collider, 0,12000.0*RoomScale,0)
			
			Sky = sky_CreateSky("GFX\map\sky\sky_day")
			RotateEntity Sky,0,e\room\angle,0
			
			DrawLoading(60, False, "", "PLAYER")
			
			ResetEntity Collider
			
			PlayAnnouncement("SFX\Intercom\MTF\NTF\AnnouncExposure.ogg")
			
			e\EventState[0] = 1.0
			
			RotateEntity Collider,0,EntityYaw(Collider)+(e\room\angle+180),0
			
			DrawLoading(100, False, "", "LOADING_DONE")
		Else
			
			If Sky = 0 Then
				Sky = sky_CreateSky("GFX\map\sky\sky_day")
				RotateEntity Sky,0,e\room\angle,0
			EndIf
			
			ShouldPlay = MUS_SURFACE
			
			e\EventState[0] = e\EventState[0] + FPSfactor
			HideEntity Fog
			CameraFogRange Camera, 5,50
			
			angle = Max(Sin(EntityYaw(Collider)+90),0.0)
			CameraFogColor(Camera,200+(angle*40),200+(angle*20),200)
			CameraClsColor(Camera,200+(angle*40),200+(angle*20),200)
			CameraRange(Camera, 0.01, 65)
			
			AmbientLight (140, 140, 140)
			
			UpdateSky(Sky)
			
			If e\EventState[1] > 0 Then
				CameraShake=CurveValue(2.0,CameraShake,10.0)
				LightFlash = 0.7
				e\EventState[2] = e\EventState[2] + (FPSfactor*0.01)
				EntityAlpha e\room\Objects[0],Min(e\EventState[2],1.0)
			EndIf
			
			If e\EventState[2] > 1.05 Then
				
				SaveGame(SavePath + CurrSave\Name + "\", True)
				
				gopt\CurrZone = GATE_A_ROAD
				
				ResetControllerSelections()
				DropSpeed = 0
				NullGame(True,False) 
				LoadEntities()
				LoadAllSounds()
				Local zonecache% = gopt\CurrZone
				InitNewGame()
				gopt\CurrZone = zonecache
				MainMenuOpen = False
				FlushKeys()
				FlushMouse()
				FlushJoy()
				ResetInput()
				
				SaveGame(SavePath + CurrSave\Name + "\", True)
				Return
			EndIf
			
			If Abs(EntityY(Collider)-EntityY(e\room\Objects[1],True))<1.0 Then
				If DistanceSquared(EntityX(Collider),EntityX(e\room\Objects[1],True),EntityZ(Collider),EntityZ(e\room\Objects[1],True)) < PowTwo(1.0) Then
					If e\EventState[2] = 0 Then
						If (Not ChannelPlaying(e\SoundCHN[0])) Then
							PlaySound_Strict LoadTempSound("SFX\Ending\GateA\TunnelCollapse.ogg")
							GodMode = 0
							NoClip = 0
							e\EventState[1] = 1.0
							psp\NoMove=True
							psp\NoRotation=True
							PointEntity(Collider,e\room\Objects[1])
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