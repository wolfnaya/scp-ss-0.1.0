
;Map constants
Const MaxMapLights = 128
Const MaxChunks = 8
Const MaxTriggers = 16

;Multiplayer gamemode constants
Const MaxGamemodeImages = 3
Const GamemodeEnd = BYTE_MAX
Const GamemodeEndTime# = 70*10
Const GamemodeEndTimeVote# = 70*15
Const GamemodeEndTimeAfterVote# = 70*10
Const GamemodeEndTimeTotal# = GamemodeEndTime + GamemodeEndTimeVote + GamemodeEndTimeAfterVote
Const Gamemode_Waves = 0
Const Gamemode_Deathmatch = 1
;Const Gamemode_Gungame = 2
Const Gamemode_EAF = 2

;Waves constants
Const Waves_StartServer = 0
Const Waves_StartGame = 1
Const Waves_Short = 6
Const Waves_Medium = 9
Const Waves_Long = 12

;Deathmatch constants
Const Deathmatch_GameStart = 0
Const Deathmatch_Game = 1
Const Deathmatch_MTFLost = 2
Const Deathmatch_CILost = 3
Const Deathmatch_TeamSwitch = 4

;Team constants
Const MaxPlayerTeams = 2
Const Team_Unknown = 0
Const Team_MTF = 1
Const Team_CI = 2

;Multiplayer difficulty constants
Const MP_DIFFICULTY_SAFE = 0
Const MP_DIFFICULTY_EUCLID = 1
Const MP_DIFFICULTY_KETER = 2

Type MultiplayerMap
	Field obj%
	Field LightAmount%
	Field Lights%[MaxMapLights]
	Field LightIntensity#[MaxMapLights]
	Field LightSprites%[MaxMapLights]
	Field LightSpriteHidden%[MaxMapLights]
	Field LightSpritesPivot%[MaxMapLights]
	Field LightSprites2%[MaxMapLights]
	Field LightHidden%[MaxMapLights]
	Field LightFlicker%[MaxMapLights]
	Field LightR#[MaxMapLights],LightG#[MaxMapLights],LightB#[MaxMapLights]
	Field Chunks%[MaxChunks]
	Field CurrChunk%
	Field Triggers%[MaxTriggers]
	Field TriggerPoint1%[MaxTriggers]
	Field TriggerPoint2%[MaxTriggers]
End Type

Type MapForList
	Field MeshPath$
	Field Name$
	Field Gamemodes$
	Field ChunkStart%, ChunkEnd%
	Field NTFSpawn%, CISpawn%
	Field TriggerAmount%
	Field TriggerMeshPath$
	Field TriggerYaw%[MaxTriggers]
	Field TriggerCoords.Vector3D[MaxTriggers]
	Field TriggerAreas.Vector2D[MaxTriggers]
	Field BossNPC$
	Field Image%
End Type

Type MultiplayerGameMode
	Field name$
	Field ID%
	Field EnemyCount%
	Field PrevEnemyCount%
	Field Phase%
	Field PhaseTimer#
	Field img%[MaxGamemodeImages]
	Field MaxPhase%
	Field Difficulty%
	Field DisableMovement%
	Field RoundWins%[MaxPlayerTeams]
	Field CanSurviveAllWaves%
	Field TeamSwitched%
	Field Timer939#
	Field Image%
	Field MaxPlayersAllowed%
End Type

Type MultiplayerMapTemplate
	Field obj%
End Type

Function LoadMPMaps()
	Local dir%, file$, dirPath$
	Local mfl.MapForList
	Local i%
	
	dirPath = "GFX\map\maps"
	dir = ReadDir(dirPath)
	Repeat 
		file$=NextFile$(dir)
		If file$="" Then Exit
		If FileType(dirPath+"\"+file$) = 2 Then
			If file <> "." And file <> ".." Then
				If (FileType(dirPath+"\"+file+"\config.ini") > 0) Then
					mfl = New MapForList
					mfl\MeshPath = dirPath+"\"+file+"\"+GetINIString(dirPath+"\"+file+"\config.ini","global","mesh")
					mfl\Name = GetINIString(dirPath+"\"+file+"\config.ini","global","name")
					mfl\Gamemodes = GetINIString(dirPath+"\"+file+"\config.ini","global","gamemodes")
					mfl\ChunkStart = GetINIInt(dirPath+"\"+file+"\config.ini","chunkdata","start")
					mfl\ChunkEnd = GetINIInt(dirPath+"\"+file+"\config.ini","chunkdata","end")
					mfl\NTFSpawn = GetINIInt(dirPath+"\"+file+"\config.ini","chunkdata","ntf_spawn")
					mfl\CISpawn = GetINIInt(dirPath+"\"+file+"\config.ini","chunkdata","ci_spawn")
					mfl\TriggerAmount = GetINIInt(dirPath+"\"+file+"\config.ini","chunkdata","trigger_amount")
					mfl\TriggerMeshPath = dirPath+"\"+file+"\"+GetINIString(dirPath+"\"+file+"\config.ini","chunkdata","trigger_mesh")
					For i = 0 To mfl\TriggerAmount-1
						mfl\TriggerYaw[i] = GetINIInt(dirPath+"\"+file+"\config.ini","chunkdata","trigger"+(i+1)+"_yaw")
						Local x# = GetINIFloat(dirPath+"\"+file+"\config.ini","chunkdata","trigger"+(i+1)+"_x")
						Local y# = GetINIFloat(dirPath+"\"+file+"\config.ini","chunkdata","trigger"+(i+1)+"_y")
						Local z# = GetINIFloat(dirPath+"\"+file+"\config.ini","chunkdata","trigger"+(i+1)+"_z")
						Local area1 = GetINIInt(dirPath+"\"+file+"\config.ini","chunkdata","trigger"+(i+1)+"_area1")
						Local area2 = GetINIInt(dirPath+"\"+file+"\config.ini","chunkdata","trigger"+(i+1)+"_area2")
						mfl\TriggerCoords[i] = CreateVector3D(x,y,z,True)
						mfl\TriggerAreas[i] = CreateVector2D(area1,area2,True)
					Next
					mfl\BossNPC = GetINIString(dirPath+"\"+file+"\config.ini","global","boss")
					If (FileType(dirPath+"\"+file+"\preview.png") > 0) Then
						mfl\Image = LoadImage_Strict(dirPath+"\"+file+"\preview.png")
					Else
						mfl\Image = LoadImage_Strict("GFX\menu\map_preview_unknown.png")
					EndIf
					mfl\Image = ResizeImage2(mfl\Image, 384 * MenuScale, 192 * MenuScale)
					;debuglog mfl\Name+"|"+mfl\MeshPath
				EndIf
			EndIf
		EndIf
	Forever
	
	Local SelMap$ = GetINIString(gv\OptionFile,"server","map")
	If SelMap<>"" Then
		;Get the selected map, depending on what the player has selected
		For mfl = Each MapForList
			If mfl\Name = SelMap Then
				mp_O\MapInList = mfl
				Exit
			EndIf
		Next
	EndIf
	
	If mp_O\MapInList = Null Then
		;No name/entry found or no matching one found, just use the first map available in the list
		mp_O\MapInList = First MapForList
	EndIf
	
End Function

Function CreateMPGameModes()
	Local mgm.MultiplayerGameMode
	
	;TODO: Some of that stuff can probably be moved in a function that loads the resources only when needed, aka when loading the multiplayer game with the specific gamemode
	
	;Gamemodes for multiplayer are hardcoded in the source code as there is no other way than using Blitz3D to code them - ENDSHN
	
	; ~ [Waves]
	
	mgm = New MultiplayerGameMode
	mgm\ID = Gamemode_Waves
	mgm\name = "Waves"
	mgm\img[0] = LoadImage_Strict("GFX\skull_logo.png")
	MidHandle mgm\img[0]
	ResizeImage(mgm\img[0], ImageWidth(mgm\img[0]) * MenuScale, ImageHeight(mgm\img[0]) * MenuScale)
	mgm\MaxPhase = GetINIInt(gv\OptionFile,"server","waves_max",Waves_Short)
	mgm\Difficulty = GetINIInt(gv\OptionFile,"server","waves_difficulty",MP_DIFFICULTY_SAFE)
	mgm\Image = LoadImage_Strict("GFX\menu\Multiplayer\gamemode_preview_waves.png")
	mgm\Image = ResizeImage2(mgm\Image, 384 * MenuScale, 192 * MenuScale)
	mgm\MaxPlayersAllowed = 6
	
	; ~ [Deathmatch]
	
	mgm = New MultiplayerGameMode
	mgm\ID = Gamemode_Deathmatch
	mgm\name = "Deathmatch"
	If mp_O\OtherTeams Then
		mgm\img[Team_MTF-1] = LoadImage_Strict("GFX\Menu\Multiplayer\RRH_Logo.jpg")
	Else
		mgm\img[Team_MTF-1] = LoadImage_Strict("GFX\Menu\Multiplayer\NTF_Logo.jpg")
	EndIf
	ResizeImage(mgm\img[Team_MTF-1], 230 * MenuScale, 230 * MenuScale)
	MaskImage mgm\img[Team_MTF-1],255,0,255
	If mp_O\OtherTeams Then
		mgm\img[Team_CI-1] = LoadImage_Strict("GFX\menu\Multiplayer\SH_logo.jpg")
	Else
		mgm\img[Team_CI-1] = LoadImage_Strict("GFX\menu\Multiplayer\CI_logo.jpg")
	EndIf
	ResizeImage(mgm\img[Team_CI-1], 230 * MenuScale, 230 * MenuScale)
	MaskImage mgm\img[Team_CI-1],255,0,255
	mgm\Image = LoadImage_Strict("GFX\menu\Multiplayer\gamemode_preview_deathmatch.png")
	mgm\Image = ResizeImage2(mgm\Image, 384 * MenuScale, 192 * MenuScale)
	mgm\MaxPlayersAllowed = MaxPlayers
	
	; ~ [Gungame]
	
	;mgm = New MultiplayerGameMode
	;mgm\ID = Gamemode_Gungame
	;mgm\name = "Gungame"
	
	; ~ [Earn And Flee]
	
	mgm = New MultiplayerGameMode
	mgm\ID = Gamemode_EAF
	mgm\name = "Earn And Flee"
	mgm\img[0] = LoadImage_Strict("GFX\skull_logo.png")
	MidHandle mgm\img[0]
	ResizeImage(mgm\img[0], ImageWidth(mgm\img[0]) * MenuScale, ImageHeight(mgm\img[0]) * MenuScale)
	mgm\MaxPhase = GetINIInt(gv\OptionFile,"server","waves_max",Waves_Short)
	mgm\Difficulty = GetINIInt(gv\OptionFile,"server","waves_difficulty",MP_DIFFICULTY_SAFE)
	mgm\Image = LoadImage_Strict("GFX\menu\Multiplayer\gamemode_preview_waves.png")
	mgm\Image = ResizeImage2(mgm\Image, 384 * MenuScale, 192 * MenuScale)
	mgm\MaxPlayersAllowed = 6
	
End Function

Function LoadMPGameModes()
	Local mgm.MultiplayerGameMode
	
	CreateMPGameModes()
	Local SelGamemode$ = GetINIString(gv\OptionFile,"server","gamemode","")
	If SelGamemode$<>"" Then
		For mgm = Each MultiplayerGameMode
			If mgm\name = SelGamemode Then
				mp_O\Gamemode = mgm
				Exit
			EndIf
		Next
	EndIf
	
	If mp_O\Gamemode = Null Then
		mp_O\Gamemode = First MultiplayerGameMode
	EndIf
	
End Function

Function AddLightMPMap(mpmap.MultiplayerMap,x#,y#,z#,ltype%,range#,r%,g%,b%)
	Local i
	
	mpmap\Lights[mpmap\LightAmount] = CreateLight(ltype)
	LightRange(mpmap\Lights[mpmap\LightAmount],range)
	LightColor(mpmap\Lights[mpmap\LightAmount],r,g,b)
	PositionEntity(mpmap\Lights[mpmap\LightAmount],x,y,z,True)
	
	mpmap\LightIntensity[mpmap\LightAmount] = (r+g+b)/255.0/3.0
	
	mpmap\LightSprites[mpmap\LightAmount] = CreateSprite()
	PositionEntity(mpmap\LightSprites[mpmap\LightAmount], x, y, z)
	ScaleSprite(mpmap\LightSprites[mpmap\LightAmount], 0.13 , 0.13)
	EntityTexture(mpmap\LightSprites[mpmap\LightAmount], LightSpriteTex[0])
	EntityBlend (mpmap\LightSprites[mpmap\LightAmount], 3)
	
	mpmap\LightSpritesPivot[mpmap\LightAmount] = CreatePivot()
	EntityRadius mpmap\LightSpritesPivot[mpmap\LightAmount],0.05
	PositionEntity(mpmap\LightSpritesPivot[mpmap\LightAmount], x, y, z)
	
	mpmap\LightSprites2[mpmap\LightAmount] = CreateSprite()
	PositionEntity(mpmap\LightSprites2[mpmap\LightAmount], x, y, z)
	ScaleSprite(mpmap\LightSprites2[mpmap\LightAmount], 0.6, 0.6)
	EntityTexture(mpmap\LightSprites2[mpmap\LightAmount], LightSpriteTex[2])
	EntityBlend(mpmap\LightSprites2[mpmap\LightAmount], 3)
	EntityOrder(mpmap\LightSprites2[mpmap\LightAmount], -1)
	EntityColor(mpmap\LightSprites2[mpmap\LightAmount], r%, g%, b%)
	EntityFX(mpmap\LightSprites2[mpmap\LightAmount],1)
	RotateEntity(mpmap\LightSprites2[mpmap\LightAmount],0,0,Rand(360))
	SpriteViewMode(mpmap\LightSprites2[mpmap\LightAmount],1)
	mpmap\LightSpriteHidden[mpmap\LightAmount] = True
	HideEntity mpmap\LightSprites2[mpmap\LightAmount]
	mpmap\LightFlicker[mpmap\LightAmount] = Rand(1,10)
	
	mpmap\LightR[mpmap\LightAmount] = r
	mpmap\LightG[mpmap\LightAmount] = g
	mpmap\LightB[mpmap\LightAmount] = b
	
	HideEntity mpmap\Lights[mpmap\LightAmount]
	
	mpmap\LightAmount = mpmap\LightAmount + 1
	
End Function

Function UpdateLightsMPMap(cam%)
	Local i, random#, alpha#
	
	For i=0 To mp_I\Map\LightAmount-1
		If mp_I\Map\Lights[i]<>0
			If opt\EnableRoomLights%
				ShowEntity mp_I\Map\LightSprites[i]
				
				If EntityDistanceSquared(cam%,mp_I\Map\Lights[i])<PowTwo(8.5)
					If mp_I\Map\LightHidden[i]
						ShowEntity mp_I\Map\Lights[i]
						mp_I\Map\LightHidden[i] = False
					EndIf
				Else
					If (Not mp_I\Map\LightHidden[i])
						HideEntity mp_I\Map\Lights[i]
						mp_I\Map\LightHidden[i] = True
					EndIf
				EndIf
				
				If (EntityDistanceSquared(cam%,mp_I\Map\LightSprites2[i])<PowTwo(8.5)) ;Lor ml\RoomTemplate\UseLightCones)
					If EntityVisible(cam%,mp_I\Map\LightSpritesPivot[i]) ;Lor ml\RoomTemplate\UseLightCones
						If mp_I\Map\LightSpriteHidden[i]
							ShowEntity mp_I\Map\LightSprites2[i]
							mp_I\Map\LightSpriteHidden[i] = False
						EndIf
						If mp_I\Map\LightFlicker[i]<5
							random# = Rnd(0.38,0.42)
						ElseIf mp_I\Map\LightFlicker[i]>4 And mp_I\Map\LightFlicker[i]<10
							random# = Rnd(0.35,0.45)
						Else
							random# = Rnd(0.3,0.5)
						EndIf
						ScaleSprite mp_I\Map\LightSprites2[i],random#,random#
						alpha# = Float(Inverse(Max(Min((EntityDistance(cam%,mp_I\Map\LightSpritesPivot[i])+0.5)/7.5,1.0),0.0)))
						
						If alpha# > 0.0
							EntityAlpha mp_I\Map\LightSprites2[i],Max(3*(Brightness/255)*(mp_I\Map\LightIntensity[i]/2),1)*alpha#
						Else
							;Instead of rendering the sprite invisible, just hiding it if the player is far away from it
							If (Not mp_I\Map\LightSpriteHidden[i])
								HideEntity mp_I\Map\LightSprites2[i]
								mp_I\Map\LightSpriteHidden[i]=True
							EndIf
						EndIf
					Else
						If (Not mp_I\Map\LightSpriteHidden[i])
							HideEntity mp_I\Map\LightSprites2[i]
							mp_I\Map\LightSpriteHidden[i] = True
						EndIf
					EndIf
				Else
					If (Not mp_I\Map\LightSpriteHidden[i])
						HideEntity mp_I\Map\LightSprites2[i]
						mp_I\Map\LightSpriteHidden[i] = True
						;If ml\LightCone<>0 Then HideEntity ml\LightCone
						;If ml\LightConeSpark<>0 HideEntity ml\LightConeSpark
					EndIf
				EndIf
			Else
				If (Not mp_I\Map\LightHidden[i])
					HideEntity mp_I\Map\Lights[i]
					mp_I\Map\LightHidden[i] = True
				EndIf
				If (Not mp_I\Map\LightSpriteHidden[i])
					HideEntity mp_I\Map\LightSprites2[i]
					mp_I\Map\LightSpriteHidden[i]=True
				EndIf
				;If ml\LightCone<>0 Then HideEntity ml\LightCone
				;If ml\LightConeSpark<>0 HideEntity ml\LightConeSpark
			EndIf
		EndIf
	Next
End Function

Function CreateMPWaypoint.WayPoints(x#,y#,z#)
	Local w.WayPoints = New WayPoints
	
	If 1 Then
		w\obj = CreatePivot()
		PositionEntity w\obj, x,y,z	
	Else
		w\obj = CreateSprite()
		PositionEntity(w\obj, x, y, z)
		ScaleSprite(w\obj, 0.15 , 0.15)
		EntityTexture(w\obj, LightSpriteTex[0])
		EntityBlend (w\obj, 3)	
	EndIf
	
	Return w
End Function

Function InitMPWayPoints(loadingstart=45,drawloadingscreen=True)
	Local w.WayPoints, w2.WayPoints
	
	Local temper = MilliSecs()
	
	Local dist#
	
	Local amount# = 0
	For w.WayPoints = Each WayPoints
		EntityPickMode w\obj, 1, True
		EntityRadius w\obj, 0.2
		amount=amount+1
	Next
	
	Local number = 0
	Local iter = 0
	For w.WayPoints = Each WayPoints
		
		number = number + 1
		iter = iter + 1
		If iter = 5 Then ;20
			If drawloadingscreen Then DrawLoading(loadingstart+Floor((35.0/amount)*number),False,"","Waypoints")
			iter = 0
		EndIf
		
		w2.WayPoints = After(w)
		
		Local canCreateWayPoint% = False
		
		While (w2<>Null)
				dist# = EntityDistance(w\obj, w2\obj) ;TODO waypoint dist
				canCreateWayPoint = True
				
				If dist < 7.0 Then
					If canCreateWayPoint
						If EntityVisible(w\obj, w2\obj) Then 
							For i = 0 To 4
								If w\connected[i] = Null Then
									w\connected[i] = w2.WayPoints 
									w\dist[i] = dist
									Exit
								EndIf
							Next
							
							For n = 0 To 4
								If w2\connected[n] = Null Then 
									w2\connected[n] = w.WayPoints 
									w2\dist[n] = dist
									Exit
								EndIf					
							Next
						EndIf
					EndIf	
				EndIf
			w2 = After(w2)
		Wend
		
	Next
	
	For w.WayPoints = Each WayPoints
		EntityPickMode w\obj, 0, 0
		EntityRadius w\obj, 0
		
		For i = 0 To 4
			If w\connected[i]<>Null Then 
				Local tline = CreateLine(EntityX(w\obj,True),EntityY(w\obj,True),EntityZ(w\obj,True),EntityX(w\connected[i]\obj,True),EntityY(w\connected[i]\obj,True),EntityZ(w\connected[i]\obj,True))
				EntityColor(tline, 255,0,0)
				EntityParent tline, w\obj
			EndIf
		Next
	Next
	
	;debuglog "InitWaypoints() - "+(MilliSecs()-temper)
End Function

Function UpdateGamemodeMP()
	Local it.Items, its.ItemSpawner, ps.PlayerSpawner, mfl.MapForList
	Local playerMultiplier# = 1.0
	Local maxPeopleOnTeamMTF%, maxPeopleOnTeamCI%, peopleOnTeamMTFDead%, peopleOnTeamCIDead%
	Local roundEnd%
	Local i%
	
	;Add a certain multiplier that makes more NPCs to spawn per wave when the player count increases
	If mp_I\PlayerCount >= 2 Then
		playerMultiplier = 1.5
		For i = 2 To mp_I\PlayerCount-1
			playerMultiplier = playerMultiplier + 0.25
		Next
	EndIf
	
	If mp_I\Gamemode\Phase <> GamemodeEnd Then
		If mp_I\ReadyTimer <= 0.0 Then
			Select mp_I\Gamemode\ID
				Case Gamemode_Waves
					;[Block]
					Select mp_I\Gamemode\Phase
						Case Waves_StartServer
							mp_I\Gamemode\PhaseTimer = 70*30
							mp_I\Gamemode\Phase = Waves_StartGame
						Case Waves_StartGame
							If mp_I\Gamemode\PhaseTimer <= 0 Then
								mp_I\Gamemode\Phase = Waves_StartGame+1
								mp_I\Gamemode\PhaseTimer = 70*5
								mp_I\Gamemode\EnemyCount = Int(20*playerMultiplier*(1+(mp_I\Gamemode\Difficulty*0.25)))
								mp_I\Gamemode\PrevEnemyCount = mp_I\Gamemode\EnemyCount
								PlaySound_Strict LoadTempSound("SFX\General\WaveStart.ogg")
							Else
								mp_I\Gamemode\PhaseTimer = mp_I\Gamemode\PhaseTimer - FPSfactor
								CountdownBeep(mp_I\Gamemode\PhaseTimer, 3)
							EndIf
						Default
							If (mp_I\Gamemode\Phase Mod 2) = 0
								If mp_I\Gamemode\EnemyCount = 0 Then
									mp_I\Gamemode\PhaseTimer = 70*60-(15*mp_I\Gamemode\Difficulty)
									mp_I\Gamemode\Phase = mp_I\Gamemode\Phase + 1
									For it = Each Items
										If it\Dropped = 0 Then
											RemoveItem(it)
										EndIf
									Next
									For its = Each ItemSpawner
										its\time = 0.0
										its\picked = True
									Next
									For ps = Each PlayerSpawner
										ps\hasPlayerSpawned = False
									Next
									RespawnPlayers()
								EndIf
								If mp_I\Gamemode\PhaseTimer > 0 Then
									mp_I\Gamemode\PhaseTimer = mp_I\Gamemode\PhaseTimer - FPSfactor
								EndIf
							Else
								If mp_I\Gamemode\PhaseTimer <= 0 Then
									mp_I\Gamemode\Phase = mp_I\Gamemode\Phase + 1
									mp_I\Gamemode\PhaseTimer = 70*5
									mp_I\Gamemode\EnemyCount = Int(mp_I\Gamemode\PrevEnemyCount+(Float(mp_I\Gamemode\Phase/2))*playerMultiplier*(1+(mp_I\Gamemode\Difficulty*0.25)))
									mp_I\Gamemode\PrevEnemyCount = mp_I\Gamemode\EnemyCount
									PlaySound_Strict LoadTempSound("SFX\General\WaveStart.ogg")
								Else
									mp_I\Gamemode\PhaseTimer = mp_I\Gamemode\PhaseTimer - FPSfactor
									CountdownBeep(mp_I\Gamemode\PhaseTimer, 3)
								EndIf
							EndIf
							maxPeopleOnTeamMTF = 0
							peopleOnTeamMTFDead = 0
							For i = 0 To (mp_I\MaxPlayers-1)
								If Players[i]<>Null Then
									maxPeopleOnTeamMTF = maxPeopleOnTeamMTF + 1
									If Players[i]\CurrHP <= 0 Then
										peopleOnTeamMTFDead = peopleOnTeamMTFDead + 1
									EndIf
								EndIf
							Next
							If maxPeopleOnTeamMTF > 0 And peopleOnTeamMTFDead = maxPeopleOnTeamMTF Then
								mp_I\BossNPC = Null
								mp_I\Gamemode\EnemyCount = 0
								EndGameForVoting()
							EndIf	
					End Select
					;[End Block]
				Case Gamemode_EAF
					;[Block]
					Select mp_I\Gamemode\Phase
						Case Waves_StartServer
							mp_I\Gamemode\PhaseTimer = 70*30
							mp_I\Gamemode\Phase = Waves_StartGame
						Case Waves_StartGame
							If mp_I\Gamemode\PhaseTimer <= 0 Then
								mp_I\Gamemode\Phase = Waves_StartGame+1
								mp_I\Gamemode\PhaseTimer = 70*5
								mp_I\Gamemode\EnemyCount = Int(20*playerMultiplier*(1+(mp_I\Gamemode\Difficulty*0.25)))
								mp_I\Gamemode\PrevEnemyCount = mp_I\Gamemode\EnemyCount
								PlaySound_Strict LoadTempSound("SFX\General\WaveStart.ogg")
							Else
								mp_I\Gamemode\PhaseTimer = mp_I\Gamemode\PhaseTimer - FPSfactor
								CountdownBeep(mp_I\Gamemode\PhaseTimer, 3)
							EndIf
						Default
							If (mp_I\Gamemode\Phase Mod 2) = 0
								If mp_I\Gamemode\EnemyCount = 0 Then
									mp_I\Gamemode\PhaseTimer = 70*60-(15*mp_I\Gamemode\Difficulty)
									mp_I\Gamemode\Phase = mp_I\Gamemode\Phase + 1
									For it = Each Items
										If it\Dropped = 0 Then
											RemoveItem(it)
										EndIf
									Next
									For its = Each ItemSpawner
										its\time = 0.0
										its\picked = True
									Next
									For ps = Each PlayerSpawner
										ps\hasPlayerSpawned = False
									Next
									RespawnPlayers()
								EndIf
								If mp_I\Gamemode\PhaseTimer > 0 Then
									mp_I\Gamemode\PhaseTimer = mp_I\Gamemode\PhaseTimer - FPSfactor
								EndIf
							Else
								If mp_I\Gamemode\PhaseTimer <= 0 Then
									mp_I\Gamemode\Phase = mp_I\Gamemode\Phase + 1
									mp_I\Gamemode\PhaseTimer = 70*5
									mp_I\Gamemode\EnemyCount = Int(mp_I\Gamemode\PrevEnemyCount+(Float(mp_I\Gamemode\Phase/2))*playerMultiplier*(1+(mp_I\Gamemode\Difficulty*0.25)))
									mp_I\Gamemode\PrevEnemyCount = mp_I\Gamemode\EnemyCount
									PlaySound_Strict LoadTempSound("SFX\General\WaveStart.ogg")
								Else
									mp_I\Gamemode\PhaseTimer = mp_I\Gamemode\PhaseTimer - FPSfactor
									CountdownBeep(mp_I\Gamemode\PhaseTimer, 3)
								EndIf
							EndIf
							maxPeopleOnTeamMTF = 0
							peopleOnTeamMTFDead = 0
							For i = 0 To (mp_I\MaxPlayers-1)
								If Players[i]<>Null Then
									maxPeopleOnTeamMTF = maxPeopleOnTeamMTF + 1
									If Players[i]\CurrHP <= 0 Then
										peopleOnTeamMTFDead = peopleOnTeamMTFDead + 1
									EndIf
								EndIf
							Next
							If maxPeopleOnTeamMTF > 0 And peopleOnTeamMTFDead = maxPeopleOnTeamMTF Then
								mp_I\BossNPC = Null
								mp_I\Gamemode\EnemyCount = 0
								EndGameForVoting()
							EndIf	
					End Select
					;[End Block]
				Case Gamemode_Deathmatch
					;[Block]
					Select mp_I\Gamemode\Phase
						Case Deathmatch_GameStart
							For its = Each ItemSpawner
								its\time = 0.0
								its\picked = True
							Next
							For ps = Each PlayerSpawner
								ps\hasPlayerSpawned = False
							Next
							
							For i = 0 To (mp_I\MaxPlayers-1)
								If Players[i]<>Null Then
									If Players[i]\Team > Team_Unknown Then ;CHECK FOR IMPLEMENTATION
										RespawnPlayer(i)
									EndIf
								EndIf
							Next
							
							mp_I\Gamemode\DisableMovement = True
							mp_I\Gamemode\PhaseTimer = 70*5
							mp_I\Gamemode\Phase = Deathmatch_Game
						Case Deathmatch_MTFLost, Deathmatch_CILost
							If mp_I\Gamemode\PhaseTimer > 0 Then
								mp_I\Gamemode\PhaseTimer = mp_I\Gamemode\PhaseTimer - FPSfactor
							Else
								If mp_I\Gamemode\TeamSwitched = False And (mp_I\Gamemode\RoundWins[Team_MTF-1]+mp_I\Gamemode\RoundWins[Team_CI-1]) = 15 Then
									mp_I\Gamemode\Phase = Deathmatch_TeamSwitch
									mp_I\Gamemode\PhaseTimer = 70*10
									mp_I\Gamemode\DisableMovement = True
								Else
									mp_I\Gamemode\Phase = Deathmatch_GameStart
								EndIf
								For it = Each Items
									RemoveItem(it)
								Next
							EndIf
						Case Deathmatch_TeamSwitch
							If mp_I\Gamemode\PhaseTimer > 0 Then
								mp_I\Gamemode\PhaseTimer = mp_I\Gamemode\PhaseTimer - FPSfactor
							Else	
								For i = 0 To (mp_I\MaxPlayers-1)
									If Players[i]<>Null Then
										If Players[i]\Team = Team_MTF Then
											Players[i]\Team = Team_CI
											Players[i]\ForceTeam = Team_CI
										ElseIf Players[i]\Team = Team_CI Then
											Players[i]\Team = Team_MTF
											Players[i]\ForceTeam = Team_MTF
										EndIf
										SetupTeam(i)
										Players[i]\WeaponInSlot[QUICKSLOT_PRIMARY] = GUN_UNARMED
										Players[i]\WeaponInSlot[QUICKSLOT_SECONDARY] = GUN_BERETTA
										Players[i]\WeaponInSlot[QUICKSLOT_HOLSTER] = GUN_KNIFE
										Players[i]\WantsSlot = QUICKSLOT_SECONDARY
										Players[i]\SelectedSlot = QUICKSLOT_SECONDARY
									EndIf
								Next
								Local swapValue% = mp_I\Gamemode\RoundWins[Team_CI-1]
								mp_I\Gamemode\RoundWins[Team_CI-1] = mp_I\Gamemode\RoundWins[Team_MTF-1]
								mp_I\Gamemode\RoundWins[Team_MTF-1] = swapValue
								mp_I\Gamemode\TeamSwitched = True
								mp_I\Gamemode\Phase = Deathmatch_GameStart
							EndIf	
						Case Deathmatch_Game
							If mp_I\Gamemode\PhaseTimer > 0 Then
								mp_I\Gamemode\PhaseTimer = mp_I\Gamemode\PhaseTimer - FPSfactor
								CountdownBeep(mp_I\Gamemode\PhaseTimer, 3)	
							Else
								mp_I\Gamemode\PhaseTimer = 0.0
								mp_I\Gamemode\DisableMovement = False
								maxPeopleOnTeamMTF = 0
								maxPeopleOnTeamCI = 0
								peopleOnTeamMTFDead = 0
								peopleOnTeamCIDead = 0
								For i = 0 To (mp_I\MaxPlayers-1)
									If Players[i]<>Null Then
										If Players[i]\Team = Team_MTF Then
											maxPeopleOnTeamMTF = maxPeopleOnTeamMTF + 1
											If Players[i]\CurrHP <= 0 Then
												peopleOnTeamMTFDead = peopleOnTeamMTFDead + 1
											EndIf
										ElseIf Players[i]\Team = Team_CI Then
											maxPeopleOnTeamCI = maxPeopleOnTeamCI + 1
											If Players[i]\CurrHP <= 0 Then
												peopleOnTeamCIDead = peopleOnTeamCIDead + 1
											EndIf
										EndIf
									EndIf
								Next
								
								roundEnd% = False
								
								;All MTF units are dead
								If maxPeopleOnTeamMTF > 0 And peopleOnTeamMTFDead = maxPeopleOnTeamMTF Then
									roundEnd = True
									mp_I\Gamemode\Phase = Deathmatch_MTFLost
									mp_I\Gamemode\RoundWins[Team_CI-1] = mp_I\Gamemode\RoundWins[Team_CI-1] + 1
								EndIf
								
								;All CI members are dead
								If maxPeopleOnTeamCI > 0 And peopleOnTeamCIDead = maxPeopleOnTeamCI Then
									roundEnd = True
									mp_I\Gamemode\Phase = Deathmatch_CILost
									mp_I\Gamemode\RoundWins[Team_MTF-1] = mp_I\Gamemode\RoundWins[Team_MTF-1] + 1
								EndIf
								
								If roundEnd Then
									If (mp_I\Gamemode\RoundWins[Team_MTF-1] >= 16 Lor mp_I\Gamemode\RoundWins[Team_CI-1] >= 16) Then
										EndGameForVoting()
									Else	
										mp_I\Gamemode\PhaseTimer = 70*5
									EndIf	
								EndIf
							EndIf
					End Select
					;[End Block]
			End Select
		EndIf
	Else
		If mp_I\Gamemode\PhaseTimer <= 0 Then
			mp_I\ResetGame = True
		Else
			If IsVoteCompleted() And mp_I\VotedMap = 0 Then
				Local Votes%[MAX_VOTED_MAPS]
				For i = 0 To (mp_I\MaxPlayers - 1)
					If Players[i] <> Null And Players[i]\MapVote > 0 Then
						Votes[Players[i]\MapVote - 1] = Votes[Players[i]\MapVote - 1] + 1
					EndIf
				Next
				
				Local AmountOfMaps = 0
				For mfl = Each MapForList
					If Instr(mfl\Gamemodes, mp_I\Gamemode\name) <> 0 Then
						AmountOfMaps = AmountOfMaps + 1
						If AmountOfMaps = MAX_VOTED_MAPS Then
							Exit
						EndIf
					EndIf
				Next
				
				Local MaxVotes% = -1
				Local MaxVoteIndex% = -1
				For i = 0 To (MAX_VOTED_MAPS - 1)
					If i < AmountOfMaps Then
						If Votes[i] > MaxVotes Then
							MaxVotes = Votes[i]
							MaxVoteIndex = i
						ElseIf Votes[i] = MaxVotes And Rand(0, 1) = 0 Then
							MaxVoteIndex = i
						EndIf
					Else
						Exit
					EndIf
				Next
				
				mp_I\VotedMap = MaxVoteIndex + 1
			EndIf
			mp_I\Gamemode\PhaseTimer = mp_I\Gamemode\PhaseTimer - FPSfactor
		EndIf
	EndIf
	
End Function

Function UpdateChunksMP()
	Local i,j
	If mp_I\Gamemode\ID = Gamemode_Waves Lor mp_I\Gamemode\ID = Gamemode_EAF Then
		If mp_I\Map\CurrChunk = 0 Then
			EntityAlpha GetChild(mp_I\Map\obj,RMESH_BSP),1.0
		Else
			EntityAlpha GetChild(mp_I\Map\obj,RMESH_BSP),0.0
		EndIf
		For i = 0 To MaxChunks
			If mp_I\Map\Chunks[i]=0 Then
				Exit
			Else
				If mp_I\Map\CurrChunk = (i+1) Then
					EntityAlpha GetChild(mp_I\Map\Chunks[i],RMESH_BSP),1.0
				Else
					EntityAlpha GetChild(mp_I\Map\Chunks[i],RMESH_BSP),0.0
				EndIf
			EndIf
		Next
		
		For i = 0 To MaxTriggers
			If mp_I\Map\Triggers[i]=0 Then
				Exit
			Else
				If Int(mp_I\MapInList\TriggerAreas[i]\x)-1 = mp_I\Map\CurrChunk Then
					If EntityDistanceSquared(mp_I\Map\Triggers[i],mpl\CameraPivot)<PowTwo(GetCameraFogRangeFar(Camera)) Then
						If EntityInView(mp_I\Map\Triggers[i],Camera) Then
							If Int(mp_I\MapInList\TriggerAreas[i]\y)-1=0 Then
								EntityAlpha GetChild(mp_I\Map\obj,RMESH_BSP),1.0
							Else
								EntityAlpha GetChild(mp_I\Map\Chunks[Int(mp_I\MapInList\TriggerAreas[i]\y)-2],RMESH_BSP),1.0
							EndIf
							
							If CheckSpecificTrigger(mpl\CameraPivot,mp_I\Map\Triggers[i]) Then
								If EntityDistanceSquared(mp_I\Map\TriggerPoint1[i],mpl\CameraPivot)>EntityDistanceSquared(mp_I\Map\TriggerPoint2[i],mpl\CameraPivot) Then
									mp_I\Map\CurrChunk = Int(mp_I\MapInList\TriggerAreas[i]\y)-1
								EndIf
							EndIf
						EndIf
					EndIf
				ElseIf Int(mp_I\MapInList\TriggerAreas[i]\y)-1 = mp_I\Map\CurrChunk Then
					If EntityDistanceSquared(mp_I\Map\Triggers[i],mpl\CameraPivot)<PowTwo(GetCameraFogRangeFar(Camera)) Then
						If EntityInView(mp_I\Map\Triggers[i],Camera) Then
							If Int(mp_I\MapInList\TriggerAreas[i]\x)-1=0 Then
								EntityAlpha GetChild(mp_I\Map\obj,RMESH_BSP),1.0
							Else
								EntityAlpha GetChild(mp_I\Map\Chunks[Int(mp_I\MapInList\TriggerAreas[i]\x)-2],RMESH_BSP),1.0
							EndIf
							
							If CheckSpecificTrigger(mpl\CameraPivot,mp_I\Map\Triggers[i]) Then
								If EntityDistanceSquared(mp_I\Map\TriggerPoint2[i],mpl\CameraPivot)>EntityDistanceSquared(mp_I\Map\TriggerPoint1[i],mpl\CameraPivot) Then
									mp_I\Map\CurrChunk = Int(mp_I\MapInList\TriggerAreas[i]\x)-1
								EndIf
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
		Next
	EndIf
End Function

Function CheckSpecificTrigger%(ent%,trigger%)
	Local isintersect% = False
	
	Local checkSphere% = CreateSphere(2)
	ScaleEntity checkSphere,0.3,0.3,0.3
	PositionEntity checkSphere,EntityX(ent),EntityY(ent),EntityZ(ent)
	
	isintersect% = MeshesIntersect(checkSphere,trigger)
	checkSphere = FreeEntity_Strict(checkSphere)
	
	Return isintersect
End Function

Const MaxFuseAmount% = 3

Type FuseBox
	Field model$
	Field obj%
	Field fuses%
	Field OverHereSprite%
End Type

Function CreateFuseBox.FuseBox(model$, Position.Vector3D, Rotation.Vector3D, Scale.Vector3D)
	Local fb.FuseBox = New FuseBox
	Local fb2.FuseBox
	
	fb\model = model
	For fb2 = Each FuseBox
		If fb2 <> fb And fb2\model = fb\model Then
			fb\obj = CopyEntity(fb2\obj)
			Exit
		EndIf
	Next
	If fb\obj = 0 Then
		fb\obj = LoadAnimMesh_Strict("GFX\map\" + model)
	EndIf
	PositionEntity fb\obj,Position\x,Position\y,Position\z
	RotateEntity fb\obj,Rotation\x,Rotation\y,Rotation\z
	ScaleEntity fb\obj,Scale\x,Scale\y,Scale\z
	EntityPickMode fb\obj,2
	EntityType fb\obj,HIT_MAP
	SetAnimTime fb\obj,1
	
	If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
		fb\OverHereSprite = CreateSprite()
		Local tex% = LoadTexture_Strict("GFX\HUD\communication_wheel\look_fuse.png",1+2)
		ScaleSprite fb\OverHereSprite,0.125,0.125
		EntityOrder fb\OverHereSprite,-2
		EntityTexture fb\OverHereSprite,tex
		EntityFX fb\OverHereSprite,1+8
		PositionEntity fb\OverHereSprite,Position\x,Position\y,Position\z
		HideEntity fb\OverHereSprite
	EndIf	
	
	Return fb
End Function

Function UpdateFuseBoxes()
	CatchErrors("UpdateFuseBoxes")
	Local fb.FuseBox
	Local dist#
	
	If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
		For fb = Each FuseBox
			If fb\fuses < MaxFuseAmount And Players[mp_I\PlayerID]\Item <> Null And Players[mp_I\PlayerID]\Item\itemtemplate\tempname = "fuse" And (Not Players[mp_I\PlayerID]\Item\isDeleted) Then
				dist = EntityDistanceSquared(Camera, fb\obj)
				If dist < PowTwo(0.9) Then
					If EntityInView(fb\obj, Camera) Then
						EntityPick(Camera, 0.8)
						
						If PickedEntity() = fb\obj Then
							DrawHandIcon = True
							If KeyHitUse Then
								If fb\fuses = 2 Then
									PlaySound_Strict(LoadTempSound("SFX\Interact\FuseInsert_Final.ogg"))
								Else
									PlaySound_Strict(LoadTempSound("SFX\Interact\FuseInsert.ogg"))
								EndIf
								If mp_I\PlayState = GAME_SERVER Then
									Delete Players[mp_I\PlayerID]\Item
								Else
									Players[mp_I\PlayerID]\Item\isDeleted = True
								EndIf
								fb\fuses = fb\fuses + 1
								Steam_Achieve(STAT_FUSE)
							EndIf
						EndIf
					EndIf
				EndIf
				ShowEntity fb\OverHereSprite
			Else
				HideEntity fb\OverHereSprite
			EndIf
			
			SetAnimTime fb\obj, fb\fuses+1
		Next
	Else
		For fb = Each FuseBox
			If fb\fuses < MaxFuseAmount Then
				dist = EntityDistanceSquared(Camera, fb\obj)
				If dist < PowTwo(0.9) Then
					If EntityInView(fb\obj, Camera) Then
						EntityPick(Camera, 0.8)
						
						If PickedEntity() = fb\obj Then
							DrawHandIcon = True
							If KeyHitUse Then
								If SelectedItem <> Null Then
									If fb\fuses = 2 Then
										PlaySound_Strict(LoadTempSound("SFX\Interact\FuseInsert_Final.ogg"))
									Else
										PlaySound_Strict(LoadTempSound("SFX\Interact\FuseInsert.ogg"))
									EndIf
									If SelectedItem\itemtemplate\tempname = "fuse" Then
										fb\fuses = fb\fuses + 1
										RemoveItem(SelectedItem)
										CreateMsg(GetLocalString("Singleplayer", "fuse_placed"))
									Else
										CreateMsg(GetLocalString("Singleplayer", "fuse_cantplace"))
									EndIf
								Else
									CreateMsg(GetLocalString("Singleplayer", "fuse_find"))
								EndIf
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
			SetAnimTime fb\obj, fb\fuses+1
		Next
	EndIf
	
	CatchErrors("Uncaught (UpdateFuseBoxes)")
End Function

Const GEN_CHARGE_TIME% = 2*60*70

Type Generator
	Field model$
	Field obj%
	Field soundCHN%
	Field progress#
	Field sound%
	Field range#
	Field id%
End Type

Function CreateGenerator.Generator(model$, Position.Vector3D, Rotation.Vector3D, Scale.Vector3D, sound%, range#, id%)
	Local ge.Generator = New Generator
	Local ge2.Generator
	
	ge\model = model
	For ge2 = Each Generator
		If ge2 <> ge And ge2\model = ge\model Then
			ge\obj = CopyEntity(ge2\obj)
			Exit
		EndIf
	Next
	If ge\obj = 0 Then
		ge\obj = LoadMesh_Strict("GFX\map\" + model)
	EndIf
	PositionEntity ge\obj,Position\x,Position\y,Position\z
	RotateEntity ge\obj,Rotation\x,Rotation\y,Rotation\z
	ScaleEntity ge\obj,Scale\x,Scale\y,Scale\z
	EntityPickMode ge\obj,2
	EntityType ge\obj,HIT_MAP
	ge\sound = sound
	ge\range = range
	ge\id = id
	
	Return ge
End Function

Function UpdateGenerators()
	CatchErrors("UpdateGenerators")
	Local ge.Generator, fb.FuseBox, it.Items
	Local FusesAmount% = 0
	Local FusesActivatedAmount% = 0
	Local i%
	
	For ge = Each Generator
		If ge\progress > 0.0 Then
			ge\progress = Min(ge\progress + FPSfactor, GEN_CHARGE_TIME)
			
			If ge\sound<>0 Then
				If EntityDistanceSquared(ge\obj,Camera) < PowTwo(ge\range) Then
					ge\soundCHN = LoopSound2(RoomAmbience[ge\sound], ge\soundCHN, Camera, ge\obj, ge\range, Clamp((ge\progress - 70) / 140.0, 0.0, 3.0))
				EndIf
			EndIf
		Else
			For fb = Each FuseBox
				FusesAmount% = FusesAmount + MaxFuseAmount
				FusesActivatedAmount = FusesActivatedAmount + fb\fuses
			Next
			
			If FusesActivatedAmount = FusesAmount Then
				ge\progress = FPSfactor
				
				If mp_I\PlayState = GAME_SERVER Then
					For i = 0 To (mp_I\MaxPlayers-1)
						If Players[i]<>Null Then
							If Players[i]\Item <> Null And Players[i]\Item\itemtemplate\tempname = "fuse" Then
								Delete Players[i]\Item
							EndIf
						EndIf
					Next
					For it = Each Items
						If it\itemtemplate\tempname = "fuse" Then
							RemoveItem(it)
						EndIf
					Next
				EndIf
			EndIf
		EndIf
	Next
	
	CatchErrors("Uncaught (UpdateGenerators)")
End Function

Function IsGeneratorCharged(ge.Generator)
	
	If ge\progress >= GEN_CHARGE_TIME Then
		Return True
	EndIf
	Return False
	
End Function

Function GetGenerator.Generator(id%)
	Local ge.Generator
	
	For ge = Each Generator
		If ge\id = id Then
			Return ge
		EndIf
	Next
	
End Function

Function ResetGenerator(id%)
	Local ge.Generator
	
	For ge = Each Generator
		If ge\id = id Then
			ge\progress = FPSfactor
			Return
		EndIf
	Next
	
End Function

Type SoundEmittor
	Field obj%
	Field soundCHN%
	Field sound%
	Field range#
End Type

Function CreateSoundEmittor.SoundEmittor(Position.Vector3D, sound%, range#)
	Local se.SoundEmittor = New SoundEmittor
	
	se\obj = CreatePivot()
	
	PositionEntity se\obj,Position\x,Position\y,Position\z
	
	se\sound = sound
	se\range = range
	
	Return se
End Function

Function UpdateSoundEmittors()
	CatchErrors("UpdateSoundEmittor")
	Local se.SoundEmittor
	
	For se = Each SoundEmittor
		If se\sound<>0 Then
			If EntityDistanceSquared(se\obj,Camera) < PowTwo(se\range) Then
				se\soundCHN = LoopSound2(RoomAmbience[se\sound], se\soundCHN, Camera, se\obj, se\range, 2.0)
			EndIf
		EndIf
	Next
	
	CatchErrors("Uncaught (UpdateSoundEmittor)")
End Function

Type ButtonGen
	Field model$
	Field obj%
	Field id%
	Field closest_player%
	Field activated%
	Field OverHereSprite%
End Type

Function CreateButtonGen.ButtonGen(model$, Position.Vector3D, Rotation.Vector3D, Scale.Vector3D, id%)
	Local bg.ButtonGen = New ButtonGen
	Local bg2.ButtonGen
	
	bg\model = model
	For bg2 = Each ButtonGen
		If bg2 <> bg And bg2\model = bg\model Then
			bg\obj = CopyEntity(bg2\obj)
			Exit
		EndIf
	Next
	If bg\obj = 0 Then
		bg\obj = LoadMesh_Strict("GFX\map\" + model)
	EndIf
	PositionEntity bg\obj,Position\x,Position\y,Position\z
	RotateEntity bg\obj,Rotation\x,Rotation\y,Rotation\z
	ScaleEntity bg\obj,Scale\x,Scale\y,Scale\z
	EntityPickMode bg\obj,2
	EntityType bg\obj,HIT_MAP
	bg\id = id
	
	If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
		bg\OverHereSprite = CreateSprite()
		Local tex% = LoadTexture_Strict("GFX\HUD\communication_wheel\look_use.png",1+2)
		ScaleSprite bg\OverHereSprite,0.125,0.125
		EntityOrder bg\OverHereSprite,-2
		EntityTexture bg\OverHereSprite,tex
		EntityFX bg\OverHereSprite,1+8
		PositionEntity bg\OverHereSprite,Position\x,Position\y+0.25,Position\z
		HideEntity bg\OverHereSprite
	EndIf
	
	Return bg
End Function

Function UpdateButtonGen()
	CatchErrors("UpdateButtonGen")
	Local bg.ButtonGen, ge.Generator
	Local dist#
	
	For bg = Each ButtonGen
		bg\closest_player = GetClosestPlayerIDFromEntity(bg\obj)
		ge = GetGenerator(bg\id)
		;If ge <> Null Then
			If IsGeneratorCharged(ge) Then
				If mp_I\BossNPC <> Null Then
					ShowEntity bg\OverHereSprite
				EndIf	
				If bg\closest_player = mp_I\PlayerID Then
					bg\activated = False
					dist = EntityDistanceSquared(Camera, bg\obj)
					If dist < PowTwo(0.8) Then
						DrawHandIcon = True
						If KeyHitUse Then
							;Sound playing (for client)
							PlaySound_Strict ButtonSFX[0]
							bg\activated = True
						EndIf
					EndIf
				EndIf
				
				If bg\activated Then
					ActivateDamageBossRadius(bg\id)
					ResetGenerator(bg\id)
				EndIf
			Else
				HideEntity bg\OverHereSprite
			EndIf
		;EndIf
	Next
	
	CatchErrors("Uncaught (UpdateButtonGen)")
End Function

Type LeverGen
	Field model$
	Field model_handle$
	Field obj%
	Field obj_handle%
	Field angle%
	Field id%
	Field closest_player%
	Field OverHereSprite%
End Type

Function CreateLeverGen.LeverGen(model$, model_handle$, Position.Vector3D, Rotation.Vector3D, Scale.Vector3D, angle%, id%)
	Local lg.LeverGen = New LeverGen
	Local lg2.LeverGen
	
	lg\model = model
	lg\model_handle = model_handle
	For lg2 = Each LeverGen
		If lg2 <> lg Then
			If lg2\model = lg\model Then
				lg\obj = CopyEntity(lg2\obj)
			EndIf
			If lg2\model_handle = lg\model_handle Then
				lg\obj_handle = CopyEntity(lg2\obj_handle)
			EndIf
			
			If lg\obj <> 0 And lg\obj_handle <> 0 Then
				Exit
			EndIf
		EndIf
	Next
	If lg\obj = 0 Then
		lg\obj = LoadMesh_Strict("GFX\map\" + model)
	EndIf
	If lg\obj_handle = 0 Then
		lg\obj_handle = LoadMesh_Strict("GFX\map\" + model_handle)
	EndIf
	PositionEntity lg\obj,Position\x,Position\y,Position\z
	PositionEntity lg\obj_handle,Position\x,Position\y,Position\z
	RotateEntity lg\obj,Rotation\x,Rotation\y,Rotation\z
	RotateEntity lg\obj_handle,Rotation\x,Rotation\y,Rotation\z
	ScaleEntity lg\obj,Scale\x,Scale\y,Scale\z
	ScaleEntity lg\obj_handle,Scale\x,Scale\y,Scale\z
	EntityPickMode lg\obj,2
	EntityPickMode lg\obj_handle,2
	EntityType lg\obj,HIT_MAP
	lg\angle = angle
	lg\id = id
	
	If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
		lg\OverHereSprite = CreateSprite()
		Local tex% = LoadTexture_Strict("GFX\HUD\communication_wheel\look_use.png",1+2)
		ScaleSprite lg\OverHereSprite,0.125,0.125
		EntityOrder lg\OverHereSprite,-2
		EntityTexture lg\OverHereSprite,tex
		EntityFX lg\OverHereSprite,1+8
		PositionEntity lg\OverHereSprite,Position\x,Position\y+0.25,Position\z
		HideEntity lg\OverHereSprite
	EndIf
	
	Return lg
End Function

Function UpdateLeverGen()
	CatchErrors("UpdateLeverGen")
	Local lg.LeverGen, ge.Generator
	Local activate%
	
	For lg = Each LeverGen
		lg\closest_player = GetClosestPlayerIDFromEntity(lg\obj_handle)
		ge = GetGenerator(lg\id)
		If IsGeneratorCharged(ge) Then
			If mp_I\BossNPC <> Null Then
			ShowEntity lg\OverHereSprite
			EndIf	
			activate = False
			If EntityRoll(lg\obj_handle, True) >= -85 Then
				If lg\closest_player = mp_I\PlayerID Then
					UpdateLever(lg\obj_handle, False, LEVER_AXIS_ROLL, -90, 0)
				EndIf
			Else
				activate = True
			EndIf
			
			If activate Then
				ActivateDamageBossRadius(lg\id)
				ResetGenerator(lg\id)
			EndIf
		Else
			RotateEntity lg\obj_handle,EntityPitch(lg\obj_handle),EntityYaw(lg\obj_handle),CurveValue(0, EntityRoll(lg\obj_handle), 10)
			HideEntity lg\OverHereSprite
		EndIf
	Next
	
	CatchErrors("Uncaught (UpdateLeverGen)")
End Function

Function GetClosestPlayerIDFromEntity(entity%)
	Local i,j
	
	Local dist# = 10000.0
	Local dist2# = 0.0
	Local smallestdist% = 0
	For i = 0 To (mp_I\MaxPlayers-1)
		If Players[i]<>Null Then
			If Players[i]\CurrHP > 0 And (Not IsSpectator(i)) Then ;CHECK FOR IMPLEMENTATION
				dist2# = EntityDistanceSquared(entity,Players[i]\Collider)
				If dist2 < dist Then
					dist = dist2
					smallestdist = i
				EndIf
			EndIf
		EndIf
	Next
	
	Return smallestdist
	
End Function

Const MAX_PARTICLE_EMITTERS = 3
Const ParticleType_Water% = 0
Const ParticleType_Electric% = 1

Type ParticleGen
	Field obj%
	Field ptype%
	Field pitch#
	Field yaw#
	Field id%
	Field activated%
	Field emitter.Emitters[MAX_PARTICLE_EMITTERS]
	Field sound%
	Field soundCHN%
End Type

Function CreateParticleGen.ParticleGen(Position.Vector3D, ptype%, pitch#, yaw#, id%)
	Local pg.ParticleGen = New ParticleGen
	
	pg\obj = CreatePivot()
	
	PositionEntity pg\obj,Position\x,Position\y,Position\z
	
	pg\ptype = ptype 	
	pg\pitch = pitch 	
	pg\yaw = yaw 	
	pg\id = id
	pg\sound = LoadSound_Strict("SFX\Room\ElectricalLoop.ogg")
	
	Return pg
End Function

Function UpdateParticleGen()
	CatchErrors("UpdateParticleGen")
	Local pg.ParticleGen, p.Particles, em.Emitters, n.NPCs, dbr.DamageBossRadius
	Local i%, pgInView%
	
	For pg = Each ParticleGen
		If pg\activated And pg\emitter[0]=Null Then
			Select pg\ptype
				Case ParticleType_Water
					;Y + 350 * RoomScale
					em.Emitters = CreateEmitter(EntityX(pg\obj), EntityY(pg\obj), EntityZ(pg\obj), 4) ;Z - 350 * RoomScale
					TurnEntity(em\Obj, 90, 0, 0, True)
					;EntityParent(em\Obj, r\obj)
					pg\emitter[0] = em
					em.Emitters = CreateEmitter(EntityX(pg\obj), EntityY(pg\obj), EntityZ(pg\obj), 4) ;X + 350 * RoomScale		Z - 350 * RoomScale
					TurnEntity(em\Obj, 90, 0, 0, True)
					;EntityParent(em\Obj, r\obj)
					pg\emitter[1] = em
					em.Emitters = CreateEmitter(EntityX(pg\obj), EntityY(pg\obj), EntityZ(pg\obj), 4) ;Z - 700 * RoomScale
					TurnEntity(em\Obj, 90, 0, 0, True)
					;EntityParent(em\Obj, r\obj)
					pg\emitter[2] = em
				Case ParticleType_Electric
					pgInView = EntityInView(pg\obj,Camera)
					For dbr = Each DamageBossRadius
						If pg\id = dbr\id Then
							For n = Each NPCs
								If n\HP > 0 And EntityDistanceSquared(n\Collider, dbr\obj) < PowTwo(dbr\range) Then
									p.Particles = CreateParticle(EntityX(n\obj,True),EntityY(n\obj,True)+0.4, EntityZ(n\obj,True), 4, 1.25, 0, 10)
									p\speed = 0.0
									p\A = 1.0
									LightFlash = pgInView*0.5
									CameraShake = pgInView*0.5
									pg\soundCHN = LoopSound2(pg\sound,pg\soundCHN,Camera,pg\obj)
								Else
									StopChannel(pg\soundCHN)
								EndIf
							Next
							
							For i = 0 To (mp_I\MaxPlayers-1)
								If Players[i]<>Null Then
									If Players[i]\CurrHP > 0 And EntityDistanceSquared(Players[i]\Collider, dbr\obj) < PowTwo(dbr\range) Then
										p.Particles = CreateParticle(EntityX(Players[i]\Collider,True),EntityY(Players[i]\Collider,True)+0.4, EntityZ(Players[i]\Collider,True), 4, 1.25, 0, 10)
										p\speed = 0.0
										p\A = 1.0
										LightFlash = pgInView*0.5
										CameraShake = pgInView*0.5
										pg\soundCHN = LoopSound2(pg\sound,pg\soundCHN,Camera,pg\obj)
									Else
										StopChannel(pg\soundCHN)
									EndIf
								EndIf
							Next
						EndIf
					Next
					If ParticleAmount > 0 Then
						If Rand(10)=1 Then
							Local pvt% = CreatePivot()
							PositionEntity(pvt, EntityX(pg\obj,True), EntityY(pg\obj,True)+Rnd(0.0,0.05), EntityZ(pg\obj,True))
							RotateEntity(pvt, pg\pitch, pg\yaw, 0)
							
							For i = 0 To (1+(2*(ParticleAmount-1)))
								p.Particles = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 7, 0.002, 0, 25)
								p\speed = Rnd(0.01,0.05)
								RotateEntity(p\pvt, EntityPitch(pvt)+Rnd(-10.0,10.0), EntityYaw(pvt)+Rnd(-10.0,10.0), 0)
								p\size = 0.0075
								ScaleSprite p\obj,p\size,p\size
								p\Achange = -0.05
							Next
							
							pvt = FreeEntity_Strict(pvt)
						EndIf
					EndIf
			End Select
		ElseIf (Not pg\activated) And pg\emitter[0]<>Null Then
			For i = 0 To MAX_PARTICLE_EMITTERS-1
				If pg\emitter[i]<>Null Then
					RemoveEmitter(pg\emitter[i])
				EndIf
			Next
		EndIf
	Next
	
	CatchErrors("Uncaught (UpdateParticleGen)")
End Function

Type DamageBossRadius
	Field obj%
	Field range#
	Field id%
	Field timer#
End Type

Function CreateDamageBossRadius.DamageBossRadius(Position.Vector3D, range#, id%)
	Local dbr.DamageBossRadius = New DamageBossRadius
	
	dbr\obj = CreatePivot()
	
	PositionEntity dbr\obj,Position\x,Position\y,Position\z
	
	dbr\range = range
	dbr\id = id
	
	Return dbr
End Function

Function UpdateDamageBossRadius()
	CatchErrors("DamageBossRadius")
	Local dbr.DamageBossRadius, pg.ParticleGen, n.NPCs, cmsg.ChatMSG
	Local i%
	Local damage% = 4 ;We have to test the exact amount of HP extraction
	
	For dbr = Each DamageBossRadius
		If dbr\timer > 0.0 Then
			dbr\timer = dbr\timer + FPSfactor
			For pg = Each ParticleGen
				If pg\id = dbr\id Then
					If pg\ptype = ParticleType_Electric Then
						For n = Each NPCs
							If EntityDistanceSquared(n\Collider, dbr\obj) < PowTwo(dbr\range) Then
								n\HP = n\HP - damage
							EndIf
						Next
						If mp_I\PlayState = GAME_SERVER Then
							For i = 0 To (mp_I\MaxPlayers-1)
								If Players[i]<>Null Then
									If Players[i]\CurrHP > 0 And EntityDistanceSquared(Players[i]\Collider, dbr\obj) < PowTwo(dbr\range) Then
									Players[i]\CurrHP = Max(Players[i]\CurrHP - (damage/2.0), 0)
										If Players[i]\CurrHP <= 0 Then
											cmsg = AddChatMSG("death_shock", 0, SERVER_MSG_IS, CHATMSG_TYPE_ONEPARAM_TRANSLATE)
											cmsg\Msg[1] = Players[i]\Name
											Players[i]\Deaths = Players[i]\Deaths + 1
										EndIf
									EndIf
								EndIf
							Next
						EndIf
					EndIf	
						If mp_I\BossNPC <> Null Then
							If EntityDistanceSquared(mp_I\BossNPC\Collider, dbr\obj) < PowTwo(dbr\range) Then
								mp_I\BossNPC\HP = mp_I\BossNPC\HP - damage
								If mp_I\Gamemode\ID <> Gamemode_EAF Then
									If mp_I\BossNPC\State[0] <> 3 Then
										mp_I\BossNPC\State[0] = 3 ;Assume that all bosses would have State 3 as a Stunned state
									EndIf
								EndIf
							EndIf
						EndIf
					EndIf
			Next
			If dbr\timer > 70*5 Then
				dbr\timer = 0.0
				For pg = Each ParticleGen
					If pg\id = dbr\id Then
						pg\activated = False
					EndIf
				Next
			EndIf
		EndIf
	Next
	
	CatchErrors("Uncaught (DamageBossRadius)")
End Function

Function ActivateDamageBossRadius(id%)
	Local dbr.DamageBossRadius, pg.ParticleGen
	
	For dbr = Each DamageBossRadius
		If dbr\id = id Then
			dbr\timer = FPSfactor
		EndIf
	Next
	For pg = Each ParticleGen
		If pg\id = id Then
			pg\activated = True
		EndIf
	Next
	
End Function

Const TIMER_939_MAX# = 70*5*60

Function LoadMultiplayerAchievements()
	
	If Steam_IsAchieved(ACHV_939_5MIN) Then
		mp_I\Gamemode\Timer939 = TIMER_939_MAX
	EndIf
	If mp_I\Gamemode\Phase = 0 Then
		mp_I\Gamemode\CanSurviveAllWaves = True
	EndIf
	
End Function

Function EndGameForVoting()
	Local mfl.MapForList
	Local i%
	Local Amount = 0
	Local Maps$ = ""
	
	mp_I\Gamemode\Phase = GamemodeEnd
	mp_I\Gamemode\PhaseTimer = GamemodeEndTimeTotal
	mp_I\VotedMap = 0
	
	Amount = -1
	For mfl = Each MapForList
		If Instr(mfl\Gamemodes, mp_I\Gamemode\name) <> 0 Then
			Amount = Amount + 1
			If Amount = (MAX_VOTED_MAPS - 1) Then
				Exit
			EndIf
		EndIf
	Next
	
	mp_I\MapsToVote[0] = mp_I\MapInList\Name
	Maps = "|" + mp_I\MapsToVote[0] + "|"
	i = 1
	While Amount > 0
		For mfl = Each MapForList
			If Instr(mfl\Gamemodes, mp_I\Gamemode\name) <> 0 And (Instr(Maps, "|" + mfl\Name  + "|") = 0) And Rand(1,3) = 1 Then
				mp_I\MapsToVote[i] = mfl\Name
				Maps = Maps + "|" + mp_I\MapsToVote[i] + "|"
				i = i + 1
				Amount = Amount - 1
			EndIf
		Next
	Wend
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D