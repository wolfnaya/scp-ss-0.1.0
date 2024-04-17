
Function FillRoom_Pocketdimension(r.Rooms)
	Local de.Decals
	Local terrain%,entity%,angle#
	Local i,n
	
	Local hallway = LoadRMesh("GFX\map\rooms\pocketdimension\pocketdimension2_opt.rmesh",Null,False) ;the tunnels in the first room
	r\Objects[8]=LoadRMesh("GFX\map\rooms\pocketdimension\pocketdimension3_opt.rmesh",Null,False)	;the room with the throne, moving pillars etc 
	r\Objects[9]=LoadRMesh("GFX\map\rooms\pocketdimension\pocketdimension4_opt.rmesh",Null,False) ;the flying pillar
	r\Objects[10]=CopyEntity(r\Objects[9])
	
	r\Objects[11]=LoadRMesh("GFX\map\rooms\pocketdimension\pocketdimension5_opt.rmesh",Null,False) ;the pillar room
	
	terrain = LoadMesh_Strict("GFX\map\rooms\pocketdimension\pocketdimensionterrain.b3d")
	ScaleEntity terrain,RoomScale,RoomScale,RoomScale,True
	PositionEntity terrain, 0, 2944, 0, True
	
	CreateItem("Burnt Note", "paper", EntityX(r\obj),0.5,EntityZ(r\obj)+3.5)
	
;	For n = 0 To -1
;		
;		Select n
;			Case 0
;				entity = hallway 					
;			Case 1
;				entity = r\Objects[8]						
;			Case 2
;				entity = r\Objects[9]						
;			Case 3
;				entity = r\Objects[10]							
;			Case 4
;				entity = r\Objects[11]							
;		End Select
;		
;	Next
	
	For i = 8 To 11
		ScaleEntity (r\Objects[i],RoomScale,RoomScale,RoomScale)
		EntityType GetChild(r\Objects[i],2), HIT_MAP
		EntityPickMode GetChild(r\Objects[i],2), 2
		PositionEntity(r\Objects[i],r\x,r\y,r\z+32.0,True)
		EntityParent(r\Objects[i], r\obj)
	Next
	
	ScaleEntity (terrain,RoomScale,RoomScale,RoomScale)
	EntityType terrain, HIT_MAP
	EntityPickMode terrain, 3
	PositionEntity(terrain,r\x,r\y+2944.0*RoomScale,r\z+32.0,True)			
	
	r\RoomDoors[0] = CreateDoor(0, r\x,2048*RoomScale,r\z+32.0-1024*RoomScale,0,r,False)
	r\RoomDoors[1] = CreateDoor(0, r\x,2048*RoomScale,r\z+32.0+1024*RoomScale,180,r,False)
	
	de.Decals = CreateDecal(DECAL_PD6, r\x-(1536*RoomScale), 0.02,r\z+608*RoomScale+32.0, 90,0,0)
	EntityParent(de\obj, r\obj)
	de\Size = Rnd(0.8, 0.8)
	de\blendmode = 2
	de\fx = 1+8
	ScaleSprite(de\obj, de\Size, de\Size)
	EntityFX(de\obj, 1+8)
	EntityBlend de\obj, 2
	
	ScaleEntity (r\Objects[10],RoomScale*1.5,RoomScale*2.0,RoomScale*1.5,True)			
	PositionEntity(r\Objects[11],r\x,r\y,r\z+64.0,True)			
	
	For i = 1 To 8
		r\Objects[i-1] = CopyEntity(hallway)
		ScaleEntity (r\Objects[i-1],RoomScale,RoomScale,RoomScale)
		angle# = (i-1) * (360.0/8.0)
		
		EntityType GetChild(r\Objects[i-1],2), HIT_MAP
		EntityPickMode GetChild(r\Objects[i-1],2), 2		
		
		RotateEntity(r\Objects[i-1],0,angle-90,0)
		PositionEntity(r\Objects[i-1],r\x+Cos(angle)*(512.0*RoomScale),0.0,r\z+Sin(angle)*(512.0*RoomScale))
		EntityParent (r\Objects[i-1], r\obj)
		
		If i < 6 Then 
			de.Decals = CreateDecal(DECAL_PD1 - 1 + i, r\x+Cos(angle)*(512.0*RoomScale)*3.0, 0.02,r\z+Sin(angle)*(512.0*RoomScale)*3.0, 90,angle-90,0)
			de\Size = Rnd(0.5, 0.5)
			de\blendmode = 2
			de\fx = 1+8
			ScaleSprite(de\obj, de\Size, de\Size)
			EntityFX(de\obj, 1+8)
			EntityBlend de\obj, 2
		EndIf				
	Next
	
	For i = 12 To 16
		r\Objects[i] = CreatePivot(r\Objects[11])
		Select i
			Case 12
				PositionEntity(r\Objects[i],r\x,r\y+200*RoomScale,r\z+64.0,True)	
			Case 13
				PositionEntity(r\Objects[i],r\x+390*RoomScale,r\y+200*RoomScale,r\z+64.0+272*RoomScale,True)	
			Case 14
				PositionEntity(r\Objects[i],r\x+838*RoomScale,r\y+200*RoomScale,r\z+64.0-551*RoomScale,True)	
			Case 15
				PositionEntity(r\Objects[i],r\x-139*RoomScale,r\y+200*RoomScale,r\z+64.0+1201*RoomScale,True)	
			Case 16
				PositionEntity(r\Objects[i],r\x-1238*RoomScale,r\y-1664*RoomScale,r\z+64.0+381*RoomScale,True)
		End Select 
		
	Next
	
	Local OldManEyes% = LoadTexture_Strict("GFX\npcs\oldmaneyes.jpg",1,2)
	r\Objects[17] = CreateSprite()
	ScaleSprite(r\Objects[17], 0.03, 0.03)
	EntityTexture(r\Objects[17], OldManEyes)
	EntityBlend (r\Objects[17], 3)
	EntityFX(r\Objects[17], 1 + 8)
	SpriteViewMode(r\Objects[17], 2)
	
	r\Objects[18] = LoadTexture_Strict("GFX\npcs\pdplane.png",1+2,2)
	r\Objects[19] = LoadTexture_Strict("GFX\npcs\pdplaneeye.png",1+2,2)
	
	r\Objects[20] = CreateSprite()
	ScaleSprite(r\Objects[20], 8.0, 8.0)
	EntityTexture(r\Objects[20], r\Objects[18])
	EntityOrder r\Objects[20], 100
	EntityBlend (r\Objects[20], 2)
	EntityFX(r\Objects[20], 1 + 8)
	SpriteViewMode(r\Objects[20], 2)
	
	hallway = FreeEntity_Strict(hallway)
	
End Function

Function UpdateEvent_Pocketdimension(e.Events)
	Local r.Rooms,de.Decals,e2.Events,p.Particles
	Local angle#,x#,y#,z#,dist#,pvt%,temp#
	Local i
	
	;eventstate: a timer for scaling the tunnels in the starting room
	;eventstate2:
		;0 if the player is in the starting room
		;1 if in the room with the throne, moving pillars, plane etc
		;12-15 if player is in the room with the tall pillars 
			;(goes down from 15 to 12 And 106 teleports from pillar to another, pillars being room\objects[12 to 15])
	;eventstate3:
		;1 when appearing in the tunnel that looks like the tunnels in hcz
		;2 after opening the door in the tunnel
		;otherwise 0
	
	If PlayerRoom = e\room Then
		ShowEntity e\room\obj
		
		PlayerFallingPickDistance = 0.0
		
		;Injuries = Injuries+FPSfactor*0.00005
		PrevSecondaryLightOn = SecondaryLightOn : SecondaryLightOn = True
		
		If (EntityY(Collider)<2000*RoomScale Lor EntityY(Collider)>2608*RoomScale) Then CurrStepSFX = 1
		
		If e\Sound[0] = 0 Then LoadEventSound(e,"SFX\Room\PocketDimension\Rumble.ogg")
		If e\Sound[1] = 0 Then e\Sound[1] = LoadEventSound(e,"SFX\Room\PocketDimension\PrisonVoices.ogg",1)
		
		If e\EventState[0] = 0 Then
			CameraFogColor Camera, 0,0,0
			CameraClsColor Camera, 0,0,0
			PlaySound_Strict(Use914SFX)
			PlaySound_Strict(OldManSFX[5])
			e\EventState[0] = 0.1
		EndIf
		
		If EntityY(Collider)<2000*RoomScale Lor e\EventState[2]=0 Lor EntityY(Collider)>2608*RoomScale Then 
			ShouldPlay = 3
		Else 
			ShouldPlay = 0
		EndIf
		
		If e\EventState[0] < 600 Then
			BlurTimer = 1000
			IsCutscene = True
			BlinkTimer = -10-(e\EventState[0]/60.0)
			IsCutscene = False
		EndIf
		
		ScaleEntity(e\room\obj,RoomScale, RoomScale*(1.0 + Sin(e\EventState[0]/14.0)*0.2), RoomScale)
		For i = 0 To 7
			ScaleEntity(e\room\Objects[i],RoomScale*(1.0 + Abs(Sin(e\EventState[0]/21.0+i*45.0)*0.1)),RoomScale*(1.0 + Sin(e\EventState[0]/14.0+i*20.0)*0.1), RoomScale,True)
		Next
		ScaleEntity(e\room\Objects[9],RoomScale*(1.5 + Abs(Sin(e\EventState[0]/21.0+i*45.0)*0.1)),RoomScale*(1.0 + Sin(e\EventState[0]/14.0+i*20.0)*0.1), RoomScale,True)
		
		e\EventState[0] = e\EventState[0] + FPSfactor
		
		If e\EventState[1] = 0 Then 
			e\room\RoomDoors[0]\open = False
			e\room\RoomDoors[1]\open = False
			
			If e\EventState[0] > 65*70 Then
				If Rand(800)=1 And Curr106\State[0] =>0 Then	
					PlaySound_Strict HorrorSFX[8]
					Curr106\State[0] = -0.1
					e\EventState[0] = 601
				EndIf
			ElseIf Curr106\State[0] > 0 ;106 circles around the starting room
				angle = (e\EventState[0]/10 Mod 360)
				PositionEntity(Curr106\Collider, EntityX(e\room\obj), 0.2+0.35+Sin(e\EventState[0]/14.0+i*20.0)*0.4, EntityX(e\room\obj))
				RotateEntity(Curr106\Collider, 0,angle,0)
				MoveEntity(Curr106\Collider,0,0,6.0-Sin(e\EventState[0]/10.0))
				AnimateNPC(Curr106, 55, 104, 0.5)
				RotateEntity(Curr106\Collider, 0,angle+90,0)
				Curr106\Idle = True
			EndIf
		EndIf 
		
		If EntityDistanceSquared(Collider, Curr106\Collider) < PowTwo(0.3) Then ;106 attacks if close enough to player
			Curr106\Idle = False
			Curr106\State[0] = -11
		EndIf
		
		If e\EventState[1] = 1 Then ;in the second room
			
			ShowEntity(e\room\Objects[9])
			PositionEntity(e\room\Objects[9], EntityX(e\room\Objects[8],True)+3384*RoomScale, 0.0, EntityZ(e\room\Objects[8],True),True)
			TranslateEntity e\room\Objects[9], Cos(e\EventState[0]*0.8)*5, 0, Sin(e\EventState[0]*1.6)*4, True
			RotateEntity e\room\Objects[9],0,e\EventState[0] * 2,0
			
			ShowEntity(e\room\Objects[10])
			PositionEntity(e\room\Objects[10], EntityX(e\room\Objects[8],True), 0.0, EntityZ(e\room\Objects[8],True)+3384*RoomScale,True)
			TranslateEntity e\room\Objects[10], Sin(e\EventState[0]*1.6)*4, 0, Cos(e\EventState[0]*0.8)*5, True
			RotateEntity e\room\Objects[10],0,e\EventState[0] * 2,0
			
			If e\EventState[2] = 1 Lor e\EventState[2] = 2 Then ;the "trick room"
				If e\EventState[2] = 1 And (e\room\RoomDoors[0]\openstate>150 Lor e\room\RoomDoors[1]\openstate>150) Then
					PlaySound_Strict LoadTempSound("SFX\Horror\Horror16.ogg")
					BlurTimer = 800
					e\EventState[2]=2
				EndIf
				
				If EntityY(Collider)<5.0 Then e\EventState[2] = 0
			Else
				;the trenches
				If EntityY(Collider)>6.0 Then
					ShouldPlay = 15
					
					CameraFogColor Camera, 38, 55, 47
					CameraClsColor Camera, 38, 55, 47
					
					If EntityX(e\room\Objects[20],True)<EntityX(e\room\Objects[8],True)-4000*RoomScale Then
						e\SoundCHN[1] = PlaySound_Strict(e\Sound[1])
						
						PositionEntity e\room\Objects[20], EntityX(Collider,True)+4000*RoomScale, 12.0, EntityZ(Collider,True)
					EndIf
					
					MoveEntity(Collider, 0, Min((12.0 - EntityY(Collider)),0.0)*FPSfactor, 0)
					
					x = -FPSfactor*RoomScale*4.0
					y = (17.0-Abs(EntityX(Collider)-EntityX(e\room\Objects[20]))*0.5)-EntityY(e\room\Objects[20])
					z = EntityZ(Collider,True)-EntityZ(e\room\Objects[20])
					TranslateEntity e\room\Objects[20], x, y, z,True
					RotateEntity e\room\Objects[20], -90-(EntityX(Collider)-EntityX(e\room\Objects[20]))*1.5, -90.0, 0.0, True
					
					
					;check if the plane can see the player
					Local safe=False
					For i = 0 To 2
						Select i
							Case 0
								x = -1452*RoomScale
								z = -37*RoomScale
							Case 1
								x = -121*RoomScale
								z = 188*RoomScale
							Case 2
								x = 1223*RoomScale
								z = -196*RoomScale							
						End Select
						
						x = x + EntityX(e\room\Objects[8],True)
						z = z + EntityZ(e\room\Objects[8],True)
						
						If DistanceSquared(EntityX(Collider), x, EntityZ(Collider), z) < PowTwo(200*RoomScale) Then safe = True : Exit
					Next
					
					dist = EntityDistance(Collider, e\room\Objects[20])
					
					If e\SoundCHN[1]<>0 And ChannelPlaying(e\SoundCHN[1])
						e\SoundCHN[1] = LoopSound2(e\Sound[1], e\SoundCHN[1], Camera, Camera, 10.0, 0.3+(Not safe)*0.6)
					EndIf	
					
					If safe Then
						EntityTexture e\room\Objects[20], e\room\Objects[18]
					ElseIf dist < 8.0
						e\SoundCHN[0] = LoopSound2(e\Sound[0], e\SoundCHN[0], Camera, e\room\Objects[20], 8.0)
						EntityTexture e\room\Objects[20], e\room\Objects[19]
						;Injuries=Injuries+(8.0-dist)*FPSfactor*0.0005
						
						If dist<7.0 Then 
							pvt% = CreatePivot()
							PositionEntity pvt, EntityX(Camera), EntityY(Camera), EntityZ(Camera)
							PointEntity(pvt, e\room\Objects[20])
							TurnEntity(pvt, 90, 0, 0)
							user_camera_pitch = CurveAngle(EntityPitch(pvt), user_camera_pitch + 90.0, 10.0)
							user_camera_pitch=user_camera_pitch-90
							RotateEntity(Collider, EntityPitch(Collider), CurveAngle(EntityYaw(pvt), EntityYaw(Collider), 10), 0)
							pvt = FreeEntity_Strict(pvt)
						EndIf
					EndIf
					
					CameraShake = Max(4.0+((Not safe) * 4.0) - dist, 0.0)
					
					;check if player is at the sinkhole (the exit from the trench room)
					If EntityY(Collider)<8.5 Then
						LoadEventSound(e,"SFX\Room\PocketDimension\Rumble.ogg")
						LoadEventSound(e,"SFX\Room\PocketDimension\PrisonVoices.ogg",1)
						
						;move to the "exit room"
						BlurTimer = 1500
						e\EventState[1]=1
						
						IsCutscene = True
						BlinkTimer = -10
						IsCutscene = False
						
						PositionEntity(Collider, EntityX(e\room\Objects[8],True)-400*RoomScale, -304*RoomScale, EntityZ(e\room\Objects[8],True))
						ResetEntity Collider
						
						CameraFogColor Camera, 0,0,0
						CameraClsColor Camera, 0,0,0
					EndIf
					
				Else
					e\EventState[2] = 0
					
					For i = 9 To 10
						dist = DistanceSquared(EntityX(Collider),EntityX(e\room\Objects[i],True),EntityZ(Collider),EntityZ(e\room\Objects[i],True))
						If dist<PowTwo(6.0) Then 
							If dist<PowTwo(100.0*RoomScale) Then
								pvt=CreatePivot()
								PositionEntity pvt, EntityX(e\room\Objects[i],True),EntityY(Collider),EntityZ(e\room\Objects[i],True)
								
								PointEntity pvt, Collider
								RotateEntity pvt, 0, Int(EntityYaw(pvt)/90)*90,0,True
								MoveEntity pvt, 0,0,100*RoomScale
								PositionEntity Collider, EntityX(pvt),EntityY(Collider),EntityZ(pvt)
								
								pvt = FreeEntity_Strict(pvt)
								
								If KillTimer = 0 Then
									m_msg\DeathTxt = GetLocalString("Singleplayer", "pd_death_1")
									
									PlaySound_Strict LoadTempSound("SFX\Room\PocketDimension\Impact.ogg")
									KillTimer=-1.0
								EndIf
							EndIf
							e\SoundCHN[0] = LoopSound2(e\Sound[0], e\SoundCHN[0], Camera, e\room\Objects[i], 6.0)	
						EndIf
					Next
					
					pvt=CreatePivot()
					PositionEntity pvt, EntityX(e\room\Objects[8],True)-1536*RoomScale,500*RoomScale,EntityZ(e\room\Objects[8],True)+608*RoomScale
					If EntityDistanceSquared(pvt, Collider)<PowTwo(5.0) Then 
						e\SoundCHN[1] = LoopSound2(e\Sound[1], e\SoundCHN[1], Camera, pvt, 3.0)
					EndIf
					pvt = FreeEntity_Strict(pvt)
					
					;106's eyes
					ShowEntity e\room\Objects[17]
					PositionEntity e\room\Objects[17], EntityX(e\room\Objects[8],True),1376*RoomScale,EntityZ(e\room\Objects[8],True)-2848*RoomScale
					PointEntity e\room\Objects[17], Collider
					TurnEntity e\room\Objects[17], 0, 180, 0
					
					temp = EntityDistance(Collider, e\room\Objects[17])
					If temp < 2000*RoomScale Then
						;Injuries = Injuries + (FPSfactor/4000)
						
;						If Injuries > 1.0 Then
;							If Injuries - (FPSfactor/4000)=< 1.0 Then
;								PlaySound_Strict LoadTempSound("SFX\Room\PocketDimension\Kneel.ogg")
;							EndIf
;						EndIf
						
						Sanity = Max(Sanity - FPSfactor / temp / 8,-1000)
						
						e\SoundCHN[0] = LoopSound2(OldManSFX[4], e\SoundCHN[0], Camera, e\room\Objects[17], 5.0, 0.6)
						
						CurrCameraZoom = Max(CurrCameraZoom, (Sin(Float(MilliSecs()) / 20.0)+1.0)*15.0*Max((6.0-temp)/6.0,0.0))
						
						pvt% = CreatePivot()
						PositionEntity pvt, EntityX(Camera), EntityY(Camera), EntityZ(Camera)
						PointEntity(pvt, e\room\Objects[17])
						TurnEntity(pvt, 90, 0, 0)
						user_camera_pitch = CurveAngle(EntityPitch(pvt), user_camera_pitch + 90.0, Min(Max(15000.0 / (-Sanity), 15.0), 500.0))
						user_camera_pitch=user_camera_pitch-90
						RotateEntity(Collider, EntityPitch(Collider), CurveAngle(EntityYaw(pvt), EntityYaw(Collider), Min(Max(15000.0 / (-Sanity), 15.0), 500.0)), 0)
						pvt = FreeEntity_Strict(pvt)
						
						;teleport the player to the trenches
						If Crouch Then
							;TODO fix trenches!
;							BlinkTimer = -10
;							PositionEntity Collider, EntityX(e\room\Objects[8],True)-1344*RoomScale,2944*RoomScale,EntityZ(e\room\Objects[8],True)-1184*RoomScale
;							ResetEntity Collider
							Crouch = False
;							
;							LoadEventSound(e,"SFX\Room\PocketDimension\Explosion.ogg")
;							LoadEventSound(e,"SFX\Room\PocketDimension\TrenchPlane.ogg",1)
;							PositionEntity e\room\Objects[20], EntityX(e\room\Objects[8],True)-1000,0,0,True
							TeleportPlayerFromPD(e)
						EndIf
					ElseIf EntityY(Collider)<-180*RoomScale ;the "exit room"
						temp = Distance(EntityX(Collider),EntityX(e\room\Objects[8],True)+1024*RoomScale,EntityZ(Collider),EntityZ(e\room\Objects[8],True))
						If temp<640*RoomScale
							BlurTimer = (640*RoomScale-temp)*3000
							
							e\SoundCHN[1] = LoopSound2(DecaySFX[Rand(1, 3)], e\SoundCHN[1], Camera, Collider, 2.0, (640*RoomScale-temp)*Abs(CurrSpeed)*100)
							CurrSpeed = CurveValue(0.0, CurrSpeed, temp*10)
							
							If temp < 130*RoomScale Then
								TeleportPlayerFromPD(e)
							EndIf
						EndIf
					EndIf
				EndIf	
			EndIf
			
			If EntityY(Collider) < -1600*RoomScale Then
				If EntityDistanceSquared(Collider, e\room\Objects[8]) > PowTwo(4750*RoomScale) Then
					CameraFogColor Camera, 0,0,0
					CameraClsColor Camera, 0,0,0
					
					DropSpeed = 0
					BlurTimer = 500
					BlurTimer = 1500
					PositionEntity(Collider, EntityX(e\room\obj,True), 0.4, EntityX(e\room\obj,True))
					TeleportPlayerFromPD(e)
				Else ;the player is not at the exit, must've fallen down
					
					If KillTimer => 0 Then 
						PlaySound_Strict HorrorSFX[8]
						m_msg\DeathTxt = GetLocalString("Singleplayer", "pd_death_2")
						
					EndIf
					KillTimer = Min(-1, KillTimer)	
					BlurTimer = 3000
				EndIf
			EndIf
		ElseIf e\EventState[1] = 0
			HideEntity(e\room\Objects[9])
			HideEntity(e\room\Objects[10])
			dist# = EntityDistance(Collider, e\room\obj)	
			
			If dist > 1700*RoomScale Then
				
				IsCutscene = True
				BlinkTimer = -10
				IsCutscene = False
				
				Select Rand(25)
					Case 1,2,3,4
						PlaySound_Strict(OldManSFX[3])
						
						pvt = CreatePivot()
						PositionEntity(pvt, EntityX(Collider), EntityY(Collider), EntityZ(Collider))
						
						PointEntity(pvt, e\room\obj)
						MoveEntity pvt, 0,0,dist*1.9
						PositionEntity(Collider, EntityX(pvt), EntityY(Collider), EntityZ(pvt))
						ResetEntity Collider
						
						MoveEntity pvt, 0,0,0.8
						PositionEntity(e\room\Objects[10], EntityX(pvt), 0.0, EntityZ(pvt))
						RotateEntity e\room\Objects[10], 0, EntityYaw(pvt), 0, True	
						
						pvt = FreeEntity_Strict(pvt)
					Case 5,6,7,8,9,10 
						e\EventState[1]=1
						IsCutscene = True
						BlinkTimer = -10
						IsCutscene = False
						PlaySound_Strict(OldManSFX[3])
						
						PositionEntity(Collider, EntityX(e\room\Objects[8],True), 0.5, EntityZ(e\room\Objects[8],True))
						ResetEntity Collider
					Case 11,12 ;middle of the large starting room
						BlurTimer = 500
						PositionEntity Collider,EntityX(e\room\obj), 0.5, EntityZ(e\room\obj)
					Case 13,14,15 ;"exit room"
						BlurTimer = 1500
						e\EventState[1]=1
						
						IsCutscene = True
						BlinkTimer = -10
						IsCutscene = False
						
						PositionEntity(Collider, EntityX(e\room\Objects[8],True)-400*RoomScale, -304*RoomScale, EntityZ(e\room\Objects[8],True))
						ResetEntity Collider
					Case 16,17,18,19
						TeleportPlayerFromPD(e)
					Case 20,21,22 ;the tower room
						IsCutscene = True
						BlinkTimer = -10
						IsCutscene = False
						PositionEntity(Collider, EntityX(e\room\Objects[12],True), 0.6, EntityZ(e\room\Objects[12],True))
						ResetEntity Collider
						e\EventState[1] = 15
					Case 23,24,25
						BlurTimer = 1500
						e\EventState[1]=1
						e\EventState[2]=1
						
						IsCutscene = True
						BlinkTimer = -10
						IsCutscene = False
						
						PlaySound_Strict(OldManSFX[3])
						
						PositionEntity(Collider, EntityX(e\room\Objects[8],True), 2288*RoomScale, EntityZ(e\room\Objects[8],True))
						ResetEntity Collider
				End Select
			EndIf					
		Else ;pillar room
			CameraFogColor Camera, 38*0.5, 55*0.5, 47*0.5
			CameraClsColor Camera, 38*0.5, 55*0.5, 47*0.5
			
			If ParticleAmount > 0
				If Rand(800)=1 Then 
					angle = EntityYaw(Camera,True)+Rnd(150,210)
					p.Particles = CreateParticle(EntityX(Collider)+Cos(angle)*7.5, 0.0, EntityZ(Collider)+Sin(angle)*7.5, 16, 4.0, 0.0, 2500); hg.png
					EntityBlend(p\obj, 2)
					p\speed = 0.01
					p\SizeChange = 0
					PointEntity(p\pvt, Camera)
					TurnEntity(p\pvt, 0, 145, 0, True)
					TurnEntity(p\pvt, Rand(10,20), 0, 0, True)
				EndIf
			EndIf
			
			If e\EventState[1] > 12 Then 
				Curr106\Idle = True
				PositionEntity(Curr106\Collider, EntityX(e\room\Objects[e\EventState[1]],True),0.27, EntityZ(e\room\Objects[e\EventState[1]],True))
				
				PointEntity(Curr106\Collider, Camera)
				TurnEntity(Curr106\Collider, 0, Sin(MilliSecs() / 20) * 6.0, 0, True)
				MoveEntity(Curr106\Collider, 0, 0, Sin(MilliSecs() / 15) * 0.06)
				
				If Rand(750)=1 And e\EventState[1] > 12 Then
					IsCutscene = True
					BlinkTimer = -10
					IsCutscene = False
					e\EventState[1] = e\EventState[1]-1
					PlaySound_Strict HorrorSFX[8]
				EndIf
				
				If e\EventState[1] = 12 Then
					CameraShake = 1.0
					PositionEntity(Curr106\Collider, EntityX(e\room\Objects[e\EventState[1]],True),-1.0, EntityZ(e\room\Objects[e\EventState[1]],True))
					Curr106\State[0] = -11
					ResetEntity Curr106\Collider
				EndIf
				
			Else 
				Curr106\State[0] = -11
				Curr106\Idle = False
			EndIf
			
			If EntityY(Collider) < -1600*RoomScale Then
				;player is at the exit
				If DistanceSquared(EntityX(e\room\Objects[16],True),EntityX(Collider),EntityZ(e\room\Objects[16],True),EntityZ(Collider))<PowTwo(144*RoomScale) Then
					
					CameraFogColor Camera, 0,0,0
					CameraClsColor Camera, 0,0,0
					
					DropSpeed = 0
					BlurTimer = 500
					PositionEntity(Collider, EntityX(e\room\obj), 0.5, EntityZ(e\room\obj))
					ResetEntity Collider
					e\EventState[1] = 0
					d_I\UpdateDoorsTimer = 0
					UpdateDoors()
					UpdateRooms()
				Else ;somewhere else -> must've fallen down
					If KillTimer => 0 Then 
						PlaySound_Strict HorrorSFX[8]
						m_msg\DeathTxt = GetLocalString("Singleplayer", "pd_death_2")
					EndIf
					KillTimer = Min(-1, KillTimer)	
					BlurTimer = 3000
				EndIf
			EndIf 
			
		EndIf
		
	Else
		HideEntity e\room\obj
		
		CameraClsColor Camera, 0,0,0
		e\EventState[0] = 0
		e\EventState[1] = 0
		e\EventState[2] = 0
	EndIf
	
End Function

Function TeleportPlayerFromPD(e.Events)
	Local r.Rooms, de.Decals, e2.Events
	Local random%, temp%
	
	If KillTimer < 0 Then Return
	
	For e2.Events = Each Events
		If e2\EventName = "room1_sewers" And e2\EventState[0] = 0 Then
			temp = True
			Exit
		EndIf
	Next
	
	random = Rand(1,9)
	For r.Rooms = Each Rooms
		If NTF_CurrZone = HCZ Then
			If r\RoomTemplate\Name = "room1_sewers" And temp Then
				TeleportEntity(Collider,EntityX(r\obj,True),0.4,EntityZ(r\obj,True),0.3,True)
				Exit
			ElseIf r\RoomTemplate\Name = "room2_tunnel_1" And random <= 3 And (Not temp) Then
				TeleportEntity(Collider,EntityX(r\obj,True),0.4,EntityZ(r\obj,True),0.3,True)
				;de.Decals = CreateDecal(DECAL_DECAY, EntityX(r\obj,True),EntityY(r\obj,True),EntityZ(r\obj,True), 270, Rand(360), 0)
				;TeleportEntity(de\obj,EntityX(r\obj,True),EntityY(r\obj,True)+1.0,EntityZ(r\obj,True),0.3,True,4,1)
				Exit
			ElseIf r\RoomTemplate\Name = "cont_106" And (random > 3 And random <= 6) And (Not temp) Then
				TeleportEntity(Collider,EntityX(r\Objects[10],True),0.4,EntityZ(r\Objects[10],True),0.3,True)
				Exit
			ElseIf r\RoomTemplate\Name = "room2_elevator_2" And random > 6 And (Not temp) Then	
				TeleportEntity(Collider,EntityX(r\Objects[0],True),0.4,EntityZ(r\Objects[0],True),0.3,True)
				;de.Decals = CreateDecal(DECAL_DECAY, EntityX(r\Objects[0],True),EntityY(r\Objects[0],True),EntityZ(r\Objects[0],True), 270, Rand(360), 0)
				;TeleportEntity(de\obj,EntityX(r\Objects[0],True),EntityY(r\Objects[0],True)+1.0,EntityZ(r\Objects[0],True),0.3,True,4,1)
				Exit
			EndIf
		ElseIf NTF_CurrZone = LCZ Then
			If r\RoomTemplate\Name = "room2_3" And random <= 3 Then
				TeleportEntity(Collider,EntityX(r\obj,True),0.4,EntityZ(r\obj,True),0.3,True)
				Exit
			ElseIf r\RoomTemplate\Name = "endroom_1" And (random > 3 And random <= 6) Then
				TeleportEntity(Collider,EntityX(r\obj,True),0.4,EntityZ(r\obj,True),0.3,True)
				Exit
			ElseIf r\RoomTemplate\Name = "room2c_1" And random > 6 Then
				TeleportEntity(Collider,EntityX(r\obj,True),0.4,EntityZ(r\obj,True),0.3,True)
				Exit
			EndIf
		ElseIf NTF_CurrZone = EZ Then
			If r\RoomTemplate\Name = "room4_ez" And random <= 3 Then
				TeleportEntity(Collider,EntityX(r\obj,True),0.4,EntityZ(r\obj,True),0.3,True)
				Exit
			ElseIf r\RoomTemplate\Name = "endroom_1" And (random > 3 And random <= 6) Then
				TeleportEntity(Collider,EntityX(r\obj,True),0.4,EntityZ(r\obj,True),0.3,True)
				Exit
			ElseIf r\RoomTemplate\Name = "room2c_ez" And random > 6 Then
				TeleportEntity(Collider,EntityX(r\obj,True),0.4,EntityZ(r\obj,True),0.3,True)
				Exit
			EndIf
		EndIf	
	Next
	e\EventState[0] = 0
	e\EventState[1] = 0
	d_I\UpdateDoorsTimer = 0
	
	SecondaryLightOn = PrevSecondaryLightOn
	PrevSecondaryLightOn = 0.0
	
	If (Not temp) Then
		IsCutscene = True
		BlinkTimer = -10
		IsCutscene = False
		LightBlink = 5
		BlurTimer = 1500
	EndIf
	
	PlayerRoom = r
	PlaySound_Strict(LoadTempSound("SFX\Room\PocketDimension\Exit.ogg"))
	UpdateRooms()
	UpdateDoors()
	Curr106\State[0] = 10000
	Curr106\Idle = False
End Function	
;~IDEal Editor Parameters:
;~C#Blitz3D