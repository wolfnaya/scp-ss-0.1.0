
; ~ [SCPs]

;[Block]

Global IsVaneCoinDropped% = False

Function Update008()
	Local temp#, i%, r.Rooms,e.Events
	Local de.Decals,p.Particles
	
	Local teleportForInfect% = True
	
	If PlayerRoom\RoomTemplate\Name = "testroom_860"
		For e.Events = Each Events
			If e\EventName = "testroom_860"
				If e\EventState[0] = 1.0
					teleportForInfect = False
				EndIf
				Exit
			EndIf
		Next
	ElseIf PlayerRoom\RoomTemplate\Name = "pocketdimension"
		teleportForInfect = False
	EndIf
	
	If I_008\Timer>0 Then
		ShowEntity InfectOverlay
		
		If I_008\Timer < 93.0 Then
			temp=I_008\Timer
			I_008\Timer = Min(I_008\Timer+FPSfactor*0.002,100)
			
			BlurTimer = Max(I_008\Timer*3*(2.0-CrouchState),BlurTimer)
			
			HeartBeatRate = Max(HeartBeatRate, 100)
			HeartBeatVolume = Max(HeartBeatVolume, I_008\Timer/120.0)
			
			EntityAlpha InfectOverlay, Min(((I_008\Timer*0.2)^2)/1000.0,0.5) * (Sin(MilliSecs()/8.0)+2.0)
			
			For i = 0 To 6
				If I_008\Timer>i*15+10 And temp =< i*15+10 Then
					PlaySound_Strict LoadTempSound("SFX\SCP\008\Voices"+i+".ogg")
				EndIf
			Next
			
			If I_008\Timer > 20 And temp =< 20.0 Then
				CreateMsg(GetLocalString("Singleplayer","scp008_1"))
			ElseIf I_008\Timer > 40 And temp =< 40.0
				CreateMsg(GetLocalString("Singleplayer","scp008_2"))
			ElseIf I_008\Timer > 60 And temp =< 60.0
				CreateMsg(GetLocalString("Singleplayer","scp008_3"))
			ElseIf I_008\Timer > 80 And temp =< 80.0
				CreateMsg(GetLocalString("Singleplayer","scp008_4"))
			ElseIf I_008\Timer =>91.5
				BlinkTimer = Max(Min(-10*(I_008\Timer-91.5),BlinkTimer),-10)
				IsZombie = True
				UnableToMove = True
				If I_008\Timer >= 92.7 And temp < 92.7 Then
					If teleportForInfect
						For r.Rooms = Each Rooms
							If r\RoomTemplate\Name="cont_008" Then
								PositionEntity Collider, EntityX(r\Objects[7],True),EntityY(r\Objects[7],True),EntityZ(r\Objects[7],True),True
								ResetEntity Collider
								r\NPC[0] = CreateNPC(NPC_Human, EntityX(r\Objects[6],True),EntityY(r\Objects[6],True)+0.2,EntityZ(r\Objects[6],True))
								r\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\008\KillScientist1.ogg")
								r\NPC[0]\SoundChn = PlaySound_Strict(r\NPC[0]\Sound)
								e\room\NPC[0]\texture = "GFX\npcs\scientist2.jpg"
								Local tex = LoadTexture_Strict(e\room\NPC[0]\texture, 1, 2)
								TextureBlend(tex,5)
								EntityTexture(e\room\NPC[0]\obj, tex)
								DeleteSingleTextureEntryFromCache tex
								r\NPC[0]\State[0]=STATE_SCRIPT
								PlayerRoom = r
								UnableToMove = False
								Exit
							EndIf
						Next
					EndIf
				EndIf
			EndIf
		Else
			
			temp=I_008\Timer
			I_008\Timer = Min(I_008\Timer+FPSfactor*0.004,100)
			
			If teleportForInfect
				If I_008\Timer < 94.7 Then
					EntityAlpha InfectOverlay, 0.5 * (Sin(MilliSecs()/8.0)+2.0)
					BlurTimer = 900
					
					If I_008\Timer > 94.5 Then BlinkTimer = Max(Min(-50*(I_008\Timer-94.5),BlinkTimer),-10)
					PointEntity Collider, PlayerRoom\NPC[0]\Collider
					PointEntity PlayerRoom\NPC[0]\Collider, Collider
					PointEntity Camera, PlayerRoom\NPC[0]\Collider,EntityRoll(Camera)
					ForceMove = 0.75
					UnableToMove = False
					
					Animate2(PlayerRoom\NPC[0]\obj, AnimTime(PlayerRoom\NPC[0]\obj), 357, 381, 0.3)
				ElseIf I_008\Timer < 98.5
					
					EntityAlpha InfectOverlay, 0.5 * (Sin(MilliSecs()/5.0)+2.0)
					BlurTimer = 950
					;I_1079\Foam = 0
					;I_1079\Trigger = 0
					ForceMove = 0.0
					UnableToMove = True
					PointEntity Camera, PlayerRoom\NPC[0]\Collider
					
					If temp < 94.7 Then 
						PlayerRoom\NPC[0]\Sound = LoadSound_Strict("SFX\SCP\008\KillScientist2.ogg")
						PlayerRoom\NPC[0]\SoundChn = PlaySound_Strict(PlayerRoom\NPC[0]\Sound)
						
						m_msg\DeathTxt = GetLocalStringR("Singleplayer","spc008_death_1",Designation)
						m_msg\DeathTxt = m_msg\DeathTxt + GetLocalString("Singleplayer","spc008_death_2")
						
						Kill()
						de.Decals = CreateDecal(DECAL_BLOODSPLAT2, EntityX(PlayerRoom\NPC[0]\Collider), 544*RoomScale + 0.01, EntityZ(PlayerRoom\NPC[0]\Collider),90,Rnd(360),0)
						de\Size = 0.8
						ScaleSprite(de\obj, de\Size,de\Size)
					ElseIf I_008\Timer > 96
						BlinkTimer = Max(Min(-10*(I_008\Timer-96),BlinkTimer),-10)
					Else
						KillTimer = Max(-350, KillTimer)
					EndIf
					
					If PlayerRoom\NPC[0]\State[1]=0 Then
						Animate2(PlayerRoom\NPC[0]\obj, AnimTime(PlayerRoom\NPC[0]\obj), 13, 19, 0.3,False)
						If AnimTime(PlayerRoom\NPC[0]\obj) => 19 Then PlayerRoom\NPC[0]\State[1]=1
					Else
						Animate2(PlayerRoom\NPC[0]\obj, AnimTime(PlayerRoom\NPC[0]\obj), 19, 13, -0.3)
						If AnimTime(PlayerRoom\NPC[0]\obj) =< 13 Then PlayerRoom\NPC[0]\State[1]=0
					EndIf
					
					If ParticleAmount>0
						If Rand(50)=1 Then
							p.Particles = CreateParticle(EntityX(PlayerRoom\NPC[0]\Collider),EntityY(PlayerRoom\NPC[0]\Collider),EntityZ(PlayerRoom\NPC[0]\Collider), 5, Rnd(0.05,0.1), 0.15, 200)
							p\speed = 0.01
							p\SizeChange = 0.01
							p\A = 0.5
							p\Achange = -0.01
							RotateEntity p\pvt, Rnd(360),Rnd(360),0
						EndIf
					EndIf
					
					PositionEntity Head, EntityX(PlayerRoom\NPC[0]\Collider,True), EntityY(PlayerRoom\NPC[0]\Collider,True)+0.65,EntityZ(PlayerRoom\NPC[0]\Collider,True),True
					RotateEntity Head, (1.0+Sin(MilliSecs()/5.0))*15, PlayerRoom\angle-180, 0, True
					MoveEntity Head, 0,0,-0.4
					TurnEntity Head, 80+(Sin(MilliSecs()/5.0))*30,(Sin(MilliSecs()/5.0))*40,0
				EndIf
			Else
				Kill()
				BlinkTimer = Max(Min(-10*(I_008\Timer-96),BlinkTimer),-10)
				m_msg\DeathTxt = ""
			EndIf
		EndIf
		
		
	Else
		HideEntity InfectOverlay
	EndIf
End Function

Function Update016()
	Local prev016timer#,Infection016
	prev016timer = I_016\Timer
	
	If I_016\Timer > 0 Then
		
		If I_016\Timer =< 94.05 Then
			If (Not I_427\Using And I_427\Timer < 70*360) Then
				I_016\Timer = ((Min(I_016\Timer+FPSfactor*0.004,100)))
			EndIf						
			BlurTimer = Max(I_016\Timer*3*(2.0-CrouchState),BlurTimer)
		ElseIf I_016\Timer > 94.05 And I_016\Timer < 95.26 Then
			I_016\Timer = Min(I_016\Timer+FPSfactor*0.004,100)
			Playable=False
			BlurTimer=4.0
			CameraShake = (I_016\Timer-94)*0.5
		EndIf
		
		If I_016\Timer > 95 And prev016timer =< 95 Then
			PlaySound_Strict LoadTempSound("SFX\SCP\914\PlayerDeath.ogg")
			DamageSPPlayer(Rand(2,3),True)
		EndIf
		
		If I_016\Timer > 93 And prev016timer =< 93 Then
			PlaySound_Strict LoadTempSound("SFX\SCP\016\016Death.ogg")
			DamageSPPlayer(Rand(2,3),True)
		EndIf
		
		If I_016\Timer > 96 And prev016timer =< 96 Then
			PlaySound_Strict LoadTempSound("SFX\Horror\Horror14.ogg")
			DamageSPPlayer(Rand(2,3),True)
		EndIf
		
		If I_016\Timer >= 95.26 Then
			Infection016 = True
		EndIf
		
		If I_016\Timer > 40 And prev016timer =< 40 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_1"))
			PlaySound_Strict LoadTempSound("SFX\Character\Player\Cough3.ogg")
		EndIf
		If I_016\Timer > 55 And prev016timer =< 55 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_2"))
		EndIf
		If I_016\Timer > 70 And prev016timer =< 70 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_3"))
		EndIf
		If I_016\Timer > 85 And prev016timer =< 85 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_4"))
			DamageSPPlayer(Rand(2,3),True)
			BlurTimer = 15
		EndIf
		If I_016\Timer > 76 And prev016timer =< 76 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_5"))
			DamageSPPlayer(Rand(2,3),True)
			BlurTimer = 15
		EndIf
		If I_016\Timer > 79 And prev016timer =< 79 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_6"))
			DamageSPPlayer(Rand(2,3),True)
		EndIf
		If I_016\Timer > 83 And prev016timer =< 83 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_7"))
			PlaySound_Strict LoadTempSound("SFX\Character\Player\Cough1.ogg")
			DamageSPPlayer(Rand(2,3),True)
		EndIf
		If I_016\Timer > 86 And prev016timer =< 86 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_8"))
			PlaySound_Strict LoadTempSound("SFX\Character\Player\Cough3.ogg")
			DamageSPPlayer(Rand(2,3),True)
			Stamina = 999
		EndIf
		If I_016\Timer > 89 And prev016timer =< 89 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_9"))
			DamageSPPlayer(Rand(2,3),True)
		EndIf
		If I_016\Timer > 93 And prev016timer =< 93 Then
			CreateMsg(GetLocalString("Singleplayer","scp016_10"))
			DamageSPPlayer(Rand(2,3),True)
		EndIf
		
		If (Infection016 = True) And (KillTimer>=0) Then	
			Playable=True
			m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp016_death_1",Designation)
			m_msg\DeathTxt = m_msg\DeathTxt + GetLocalString("Singleplayer","scp016_death_2")
			Kill()
		EndIf
	EndIf
	
End Function
;
;Function Update059()
;	Local prev059timer#, Infection059
;	prev059timer = I_059\Timer
;	
;	If I_059\Timer > 0 Then
;		
;		If I_059\Timer =< 94.05 Then
;			If (Not I_427\Using And I_427\Timer < 70*360) Then
;				I_059\Timer = ((Min(I_059\Timer+FPSfactor*0.004,100)))
;			EndIf						
;			BlurTimer = Max(I_059\Timer*3*(2.0-CrouchState),BlurTimer)
;		ElseIf I_059\Timer > 94.05 And I_059\Timer < 95.26 Then
;			I_059\Timer = Min(I_059\Timer+FPSfactor*0.004,100)
;			Playable=False
;			BlurTimer=4.0
;			LightFlash = 5.0
;		EndIf
;		
;		If I_059\Timer > 94 And prev059timer =< 94 Then
;			PlaySound_Strict LoadTempSound("SFX\SCP\059\059WorldEnter.ogg")
;			DamageSPPlayer(Rand(5,8),True)
;		EndIf
;		
;		If I_059\Timer >= 100 Then
;			Infection059 = True
;		EndIf
;		
;		If I_059\Timer > 40 And prev059timer =< 40 Then
;			CreateMsg(GetLocalString("Singleplayer","scp059_1"))
;		EndIf
;		
;		If I_059\Timer > 55 And prev059timer =< 40 Then
;			CreateMsg(GetLocalString("Singleplayer","scp059_2"))
;		EndIf
;		
;		If I_059\Timer > 73 And prev059timer =< 93 Then
;			CreateMsg(GetLocalString("Singleplayer","scp059_3"))
;		EndIf
;		
;		If (Infection059 = True) And (KillTimer>=0) Then	
;			Playable=True
;			m_msg\DeathTxt = GetLocalString("Singleplayer","scp059_death_1")
;			m_msg\DeathTxt = m_msg\DeathTxt + GetLocalStringR("Singleplayer","scp059_death_2",Designation)
;			Kill()
;		EndIf
;	Else
;	EndIf
;End Function

Function Update109()
	Local prev109vomittimer# = I_109\VomitTimer
	
	If I_109\Vomit Then
		I_109\VomitTimer = I_109\VomitTimer + FPSfactor
	EndIf
	
	If I_109\VomitTimer > 70*15 And prev109vomittimer# =< 70*15 Then
		VomitTimer = -15
		I_109\Vomit = False
		I_109\VomitTimer = 1
	EndIf
	
End Function

Function Update198()
	Local prev198timer# = I_198\DeathTimer
	Local prev198vomittimer# = I_198\VomitTimer
	
	If I_198\Timer > 0 Then
		I_198\DeathTimer = I_198\DeathTimer + FPSfactor*0.8
		
		If I_198\DeathTimer > 70*30 And prev198timer# =< 70*30 Then
			I_198\Injuries = 20
			If I_1033RU\HP = 0
				DamageSPPlayer(I_198\Injuries,True,0)
			Else
				Damage1033RU(30 + (5 * SelectedDifficulty\AggressiveNPCs))
			EndIf
		ElseIf I_198\DeathTimer > 70*60 And prev198timer# =< 70*60 Then
			I_198\Injuries = 30
			If I_1033RU\HP = 0
				DamageSPPlayer(I_198\Injuries,True,0)
			Else
				Damage1033RU(40 + (10 * SelectedDifficulty\aggressiveNPCs))
			EndIf
		ElseIf I_198\DeathTimer > 70*90 And prev198timer# =< 70*90 Then
			I_198\Injuries = 40
			If I_1033RU\HP = 0
				DamageSPPlayer(I_198\Injuries,True,0)
			Else
				Damage1033RU(50 + (15 * SelectedDifficulty\aggressiveNPCs))
			EndIf
		EndIf
		
		If I_198\Vomit Then
			I_198\VomitTimer = I_198\VomitTimer + FPSfactor
		EndIf
		
		If I_198\VomitTimer > 70*8 And prev198vomittimer# =< 70*8 Then
			VomitTimer = -15
			I_198\Vomit = False
			I_198\VomitTimer = 1
		EndIf
		
		If I_198\DeathTimer > 70*120 And prev198timer# =< 70*120 Then
			m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp198_death",Designation)
			Kill()
		EndIf
		
	EndIf
	
End Function

Function Update127()
	Local g.Guns,it.Items
	
	For g.Guns = Each Guns
		If g\ID = 18 Then
			If g\CurrAmmo < g\MaxCurrAmmo Then
				I_127\RestoreTimer = I_127\RestoreTimer + FPSfactor
				If I_127\RestoreTimer >= 70*5 Then
					I_127\RestoreTimer = 0
					g\CurrAmmo = g\CurrAmmo + 1
				EndIf
			EndIf
			Exit
		EndIf
	Next
	For it.Items = Each Items
		If it\name = "SCP-127" Then
			If it\state < 60 Then
				I_127\RestoreTimer = I_127\RestoreTimer + FPSfactor
				If I_127\RestoreTimer >= 70*5 Then
					I_127\RestoreTimer = 0
					it\state = it\state + 1
				EndIf
			EndIf
			Exit
		EndIf
	Next
	
End Function

Function Update207()
	
    If I_207\Timer > 0.0 Then
        If (Not I_427\Using = 1 And I_427\Timer < 70 * 360) Then
            I_207\Timer = Min(I_207\Timer + FPSfactor * 0.002, 51)
        EndIf
		If I_207\Factor > 1 And I_207\Factor < 5 Then
			If IsPlayerSprinting Then
				I_207\DeathTimer = I_207\DeathTimer + FPSfactor*(I_207\Factor*I_207\Factor - 2)
			Else
				I_207\DeathTimer = Max(I_207\DeathTimer - FPSfactor*(5 - I_207\Factor),0)
			EndIf
		ElseIf I_207\Factor >= 5 Then
			If IsPlayerSprinting Then
				I_207\DeathTimer = I_207\DeathTimer + FPSfactor*(I_207\Factor*I_207\Factor - 2)
			Else
				I_207\DeathTimer = I_207\DeathTimer + FPSfactor*(I_207\Factor*I_207\Factor - 4)
			EndIf
		EndIf
		
		If I_207\Factor > 1 Then
			If I_207\DeathTimer > 70*360 Then
				HeartBeatRate = Max(HeartBeatRate, I_207\DeathTimer/100)
				HeartBeatVolume = 1.0
			EndIf
			If I_207\DeathTimer > 70*420 And KillTimer >= 0 Then
				If I_207\Factor = 2 Then
					m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp207_death_1",Designation)
				ElseIf I_207\Factor = 3 Then
					m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp207_death_2",Designation)
				ElseIf I_207\Factor = 4 Then
					m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp207_death_3",Designation)
				ElseIf I_207\Factor = 5 Then
					m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp207_death_4",Designation)
				ElseIf I_207\Factor = 6 Then
					PlaySound_Strict(LoadTempSound("SFX\Player\Rude_Scream.ogg"))
					m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp207_death_4",Designation)
				EndIf
				Kill()
			EndIf
			If I_207\Factor >= 7 And KillTimer >= 0 Then
				m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp207_death_4",Designation)
				Kill()
			EndIf
		EndIf
    EndIf
End Function

Function Update268()
	
	If I_268\Using > 0 Then
		
		If I_268\Sound[0] = 0 Then
		    I_268\Sound[0] = LoadSound_Strict("SFX\SCP\268\InvisibilityOn.ogg")
	    EndIf
        If I_268\Sound[1] = 0 Then
		    I_268\Sound[1] = LoadSound_Strict("SFX\SCP\268\InvisibilityOff.ogg")
	    EndIf
		
		If I_268\Using = 2 Then 
            I_268\Timer = Max(I_268\Timer - ((FPSfactor / 1.5)), 0)
        Else
            I_268\Timer = Max(I_268\Timer - (FPSfactor), 0)
        EndIf
    Else
        I_268\Timer = Min(I_268\Timer + FPSfactor, 600.0)
    EndIf
	
End Function

Function Use294()
	Local x#,y#, xtemp%,ytemp%, strtemp$, temp%
	
	ShowPointer()
	
	x = opt\GraphicWidth/2 - (ImageWidth(Panel294)/2)
	y = opt\GraphicHeight/2 - (ImageHeight(Panel294)/2)
	DrawImage Panel294, x, y
	
	temp = True
	If PlayerRoom\SoundCHN<>0 Then temp = False
	
	Text x+907, y+185, Input294, True,True
	
	If temp Then
		If MouseHit1 Then
			MouseHit1 = False
			xtemp = Floor((ScaledMouseX()-x-228) / 35.5)
			ytemp = Floor((ScaledMouseY()-y-342) / 36.5)
			
			If ytemp => 0 And ytemp < 5 Then
				If xtemp => 0 And xtemp < 10 Then PlaySound_Strict SplashTextSFX[Rand(0,2)]
			EndIf
			
			strtemp = ""
			
			temp = False
			
			Select ytemp
				Case 0
					strtemp = (xtemp + 1) Mod 10
				Case 1
					Select xtemp
						Case 0
							strtemp = "Q"
						Case 1
							strtemp = "W"
						Case 2
							strtemp = "E"
						Case 3
							strtemp = "R"
						Case 4
							strtemp = "T"
						Case 5
							strtemp = "Y"
						Case 6
							strtemp = "U"
						Case 7
							strtemp = "I"
						Case 8
							strtemp = "O"
						Case 9
							strtemp = "P"
					End Select
				Case 2
					Select xtemp
						Case 0
							strtemp = "A"
						Case 1
							strtemp = "S"
						Case 2
							strtemp = "D"
						Case 3
							strtemp = "F"
						Case 4
							strtemp = "G"
						Case 5
							strtemp = "H"
						Case 6
							strtemp = "J"
						Case 7
							strtemp = "K"
						Case 8
							strtemp = "L"
						Case 9 ;~ Dispense
							temp = True
					End Select
				Case 3
					Select xtemp
						Case 0
							strtemp = "Z"
						Case 1
							strtemp = "X"
						Case 2
							strtemp = "C"
						Case 3
							strtemp = "V"
						Case 4
							strtemp = "B"
						Case 5
							strtemp = "N"
						Case 6
							strtemp = "M"
						Case 7
							strtemp = "-"
						Case 8
							strtemp = " "
						Case 9
							Input294 = Left(Input294, Max(Len(Input294)-1,0))
					End Select
				Case 4
					strtemp = " "
			End Select
			
			Input294 = Input294 + strtemp
			
			Input294 = Left(Input294, Min(Len(Input294),15))
			
			If temp And Input294<>"" Then ; ~ Dispense
				Input294 = Trim(Lower(Input294))
				If Left(Input294, Min(7,Len(Input294))) = "cup of " Then
					Input294 = Right(Input294, Len(Input294)-7)
				ElseIf Left(Input294, Min(9,Len(Input294))) = "a cup of " 
					Input294 = Right(Input294, Len(Input294)-9)
				EndIf
				
				If Input294<>""
					Local loc% = GetINISectionLocation(Data294, Input294)
				EndIf
				
				If loc > 0 Then
					strtemp$ = GetINIString2(Data294, loc, "dispensesound")
					If strtemp="" Then
						PlayerRoom\SoundCHN = PlaySound_Strict (LoadTempSound("SFX\SCP\294\dispense1.ogg"))
					Else
						PlayerRoom\SoundCHN = PlaySound_Strict (LoadTempSound(strtemp))
					EndIf
					
					If GetINIInt2(Data294, loc, "explosion")=True Then 
						ExplosionTimer = 135
						m_msg\DeathTxt = GetINIString2(Data294, loc, "deathmessage")
					EndIf
					
					strtemp$ = GetINIString2(Data294, loc, "color")
					
					Local sep1 = Instr(strtemp, ",", 1)
					Local sep2 = Instr(strtemp, ",", sep1+1)
					Local r% = Trim(Left(strtemp, sep1-1))
					Local g% = Trim(Mid(strtemp, sep1+1, sep2-sep1-1))
					Local b% = Trim(Right(strtemp, Len(strtemp)-sep2))
					
					Local alpha# = Float(GetINIString2(Data294, loc, "alpha",1.0))
					Local glow = GetINIInt2(Data294, loc, "glow")
					If glow Then alpha = -alpha
					
					Local it.Items
					it.Items = CreateItem(GetLocalString("Item Names","cup"), "cup", EntityX(PlayerRoom\Objects[1],True),EntityY(PlayerRoom\Objects[1],True),EntityZ(PlayerRoom\Objects[1],True), r,g,b,alpha)
					it\name = GetLocalString("Item Names","cup_of")+" "+Input294
					EntityType (it\collider, HIT_ITEM)
					
				Else
					; ~ Out of range
					Input294 = GetLocalString("Items","scp294_out")
					PlayerRoom\SoundCHN = PlaySound_Strict (LoadTempSound("SFX\SCP\294\outofrange.ogg"))
				EndIf
				
			EndIf
			
		EndIf
		
		If MouseHit2 Lor (Not Using294) Lor InvOpen Then 
			HidePointer()
			Using294 = False
			Input294 = ""
		EndIf
		
	Else ;
		If Input294 <> GetLocalString("Items","scp294_out") Then Input294 = GetLocalString("Items","scp294_1")
		
		If Not ChannelPlaying(PlayerRoom\SoundCHN) Then
			If Input294 <> GetLocalString("Items","scp294_out") Then
				HidePointer()
				Using294 = False
				MouseXSpeed() : MouseYSpeed() : MouseZSpeed() : mouse_x_speed_1#=0.0 : mouse_y_speed_1#=0.0
				Local e.Events
				For e.Events = Each Events
					If e\room = PlayerRoom
						e\EventState[1] = 0
						Exit
					EndIf
				Next
			EndIf
			Input294=""
			PlayerRoom\SoundCHN=0
		EndIf
	EndIf
	
End Function

; ~ Update any ailments inflicted by SCP-294 drinks.

Function Update294()
	Local de.Decals,pvt%
	CatchErrors("Uncaught (Update294)")
	
	If CameraShakeTimer > 0 Then
		CameraShakeTimer = CameraShakeTimer - (FPSfactor/70)
		CameraShake = 2
	EndIf
	
	If VomitTimer > 0 Then
		;debuglog VomitTimer
		VomitTimer = VomitTimer - (FPSfactor/70)
		
		If (MilliSecs() Mod 1600) < Rand(200, 400) Then
			If BlurTimer = 0 Then BlurTimer = Rnd(10, 20)*70
			CameraShake = Rnd(0, 2)
		EndIf
		If Rand(50) = 50 And (MilliSecs() Mod 4000) < 200 Then PlaySound_Strict(CoughSFX[Rand(0,2)])
		If VomitTimer < 10 And Rnd(0, 500 * VomitTimer) < 2 Then
			If (Not ChannelPlaying(VomitCHN)) And (Not Regurgitate) Then
				VomitCHN = PlaySound_Strict(LoadTempSound("SFX\SCP\294\Retch" + Rand(1, 2) + ".ogg"))
				Regurgitate = MilliSecs() + 50
			EndIf
		EndIf
		
		If Regurgitate > MilliSecs() And Regurgitate <> 0 Then
			Mouse_Y_Speed_1 = Mouse_Y_Speed_1 + 1.0
		Else
			Regurgitate = 0
		EndIf
		
	ElseIf VomitTimer < 0 Then
		VomitTimer = VomitTimer - (FPSfactor/70)
		
		If VomitTimer > -5 Then
			If (MilliSecs() Mod 400) < 50 Then CameraShake = 4 
			Mouse_X_Speed_1 = 0.0
			If (Not I_198\Vomit) Then
				Playable = False
			Else
				Playable = True
			EndIf
		Else
			Playable = True
		EndIf
		
		If (Not Vomit) Then
			BlurTimer = 40 * 70
			VomitSFX = LoadSound_Strict("SFX\SCP\294\Vomit.ogg")
			VomitCHN = PlaySound_Strict(VomitSFX)
			EyeIrritation = 9 * 70
			pvt = CreatePivot()
			PositionEntity(pvt, EntityX(Camera), EntityY(Collider) - 0.05, EntityZ(Camera))
			TurnEntity(pvt, 90, 0, 0)
			EntityPick(pvt, 0.3)
			de.Decals = CreateDecal(DECAL_173BLOOD2, PickedX(), PickedY() + 0.005, PickedZ(), 90, 180, 0)
			de\Size = 0.001 : de\SizeChange = 0.001 : de\MaxSize = 0.6 : EntityAlpha(de\obj, 1.0) : EntityColor(de\obj, 0.0, Rnd(200, 255), 0.0) : ScaleSprite de\obj, de\Size, de\Size
			FreeEntity pvt
			Vomit = True
		EndIf
		
		UpdateDecals()
		
		Mouse_Y_Speed_1 = Mouse_Y_Speed_1 + Max((1.0 + VomitTimer / 10), 0.0)
		
		If VomitTimer < -15 Then
			FreeSound_Strict(VomitSFX)
			VomitTimer = 0
			PlaySound_Strict(BreathSFX[0])
			Vomit = False
			If I_198\Vomit Then
				I_198\Vomit = False
			EndIf
			If I_109\Vomit Then
				I_109\Vomit = False
			EndIf
		EndIf
	EndIf
	
	CatchErrors("Update294")
End Function

;Function Update357() 
;	Local prevI357Timer# = I_357\Timer
;	Local i%
;	
;	For i=0 To MaxItemAmount-1
;		If Inventory[i]<>Null Then
;			If Inventory[i]\itemtemplate\name="SCP-357" Then
;				If (Not I_427\Using=1 And I_427\Timer < 70*360) Then
;					I_357\Timer = Min(I_357\Timer+FPSfactor*0.004,75)
;				EndIf
;			EndIf
;		EndIf
;	Next
;	
;	If I_357\Timer > 0 Then
;		
;		If I_357\Timer > 20.0 And prevI357Timer =< 20.0 Then
;			BlurTimer = 1900
;		EndIf
;		If I_357\Timer > 30.0 And prevI357Timer =< 30.0 Then
;			BlurTimer = 3000
;		EndIf
;		If I_357\Timer > 35.0 And prevI357Timer =< 35.0 Then
;			BlurTimer = 4000
;		EndIf
;		If I_357\Timer > 40.0 And prevI357Timer =< 40.0 Then
;			BlurTimer = 5000
;			CreateMsg(GetLocalString("Items","scp357_4"))
;		EndIf
;		If I_357\Timer > 56.0 And prevI357Timer =< 56.0 Then
;			BlurTimer = 5000
;			CreateMsg(GetLocalString("Items","scp357_5"))
;		EndIf
;		If I_357\Timer > 65.0 Then
;			HeartBeatRate=Max(HeartBeatRate, 70 + I_357\Timer)
;			HeartBeatVolume = 1.0
;			CameraShake = Sin(I_357\Timer / 5.0) * (I_357\Timer / 15.0)
;		EndIf 
;		If I_357\Timer > 70.0 And prevI357Timer =< 70.0 Then
;			BlurTimer = 5600
;			If I_1033RU\HP = 0
;				DamageSPPlayer(Rand(5,10),True)
;				Playable = False
;			Else
;				Damage1033RU(50 + (Rand(5) * SelectedDifficulty\AggressiveNPCs))
;			EndIf
;		EndIf
;		If I_357\Timer >= 75.0 Then
;			If Rand(2) = 1 Then
;				m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp357_death_1",Designation)
;			Else
;				m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp357_death_2",Designation)
;			EndIf
;			m_msg\DeathTxt = m_msg\DeathTxt + GetLocalString("Singleplayer","scp357_death_3")
;			Kill()
;		EndIf
;	EndIf
;	
;End Function
;
;Function Update402() 
;    Local prevI402Timer# = I_402\Timer
;	
;    If I_402\Using > 0 Then
;        If I_402\Timer >= 0 Then
;            I_402\Timer = Min(I_402\Timer+FPSfactor*0.004, 61)
;			
;            If I_402\Timer > 10.0 And prevI402Timer =< 10.0 Then
;                PlaySound_Strict(CoughSFX[Rand(0, 2)])
;                CameraShake = 5
;                If I_1033RU\HP = 0
;					CreateMsg(GetLocalString("items","scp402_2"))
;                    DamageSPPlayer(Rand(2,3),True)
;                Else
;					CreateMsg(GetLocalString("items","scp402_3"))
;                    Damage1033RU(10 + (Rand(5) * SelectedDifficulty\AggressiveNPCs))
;                EndIf
;            EndIf
;			
;            If I_402\Timer > 15.0 And prevI402Timer =< 15.0 Then
;				CreateMsg(GetLocalString("items","scp402_4"))
;            EndIf
;			
;            If I_1033RU\HP > 0
;                If I_402\Timer > 12.0 Then
;					CreateMsg(GetLocalString("items","scp402_5"))
;                    Damage1033RU(10 + (Rand(5) * SelectedDifficulty\AggressiveNPCs))
;					PlaySound_Strict(CoughSFX[Rand(0, 2)])
;                    I_402\Using = 0
;                EndIf
;            EndIf
;			
;            If I_402\Timer > 20.0 And prevI402Timer =< 20.0 Then
;				CreateMsg(GetLocalString("items","scp402_6"))
;                CameraShake = 5
;				DamageSPPlayer(Rand(2,3),True)
;            EndIf
;			
;            If I_402\Timer > 40.0 And prevI402Timer =< 40.0 Then
;				CreateMsg(GetLocalString("items","scp402_7"))
;                PlaySound_Strict(CoughSFX[Rand(0, 2)])
;                CameraShake = 5
;				DamageSPPlayer(Rand(2,3),True)
;   	        EndIf
;			
;            If I_402\Timer > 42.0 Then
;                HeartBeatRate=Max(HeartBeatRate, 70+I_402\Timer)
;			    HeartBeatVolume = 1.0
;            EndIf
;			
;	        If I_402\Timer > 45.0 And prevI402Timer =< 45.0 Then
;	            PlaySound_Strict(CoughSFX[Rand(0, 2)])
;	        EndIf
;			
;	        If I_402\Timer > 50.0 And prevI402Timer =< 50.0 Then
;	            CreateMsg(Chr(34)+GetLocalString("Singleplayer","cant_breathe")+Chr(34))
;				PlaySound_Strict(CoughSFX[Rand(0, 2)])
;	        EndIf
;			
;            If I_402\Timer > 55.0 And prevI402Timer =< 55.0 Then
;	            PlaySound_Strict(CoughSFX[Rand(0, 2)])
;	        EndIf
;			
;	        If I_402\Timer >= 60.0 And prevI402Timer =< 60.0 Then
;				PlaySound_Strict(CoughSFX[Rand(0, 2)])
;                m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp402_death",Designation)
;		        Kill()
;	        EndIf   
;	    EndIf
;	Else
;	    I_402\Timer = 0
;	EndIf
;	
;End Function

Function Update409%()
	Local PrevI409Timer# = I_409\Timer
	
	If I_409\Timer > 0.0 Then
		If EntityHidden(SCP409Overlay) Then ShowEntity(SCP409Overlay)
		
		If (Not I_427\Using) And I_427\Timer < 70.0 * 360.0 Then
			If I_409\Revert Then
				I_409\Timer = Max(0.0, I_409\Timer - (FPSfactor * 0.01))
			Else
				I_409\Timer = Min(I_409\Timer + (FPSfactor * 0.004), 100.0)
			EndIf
		EndIf	
		EntityAlpha(SCP409Overlay, Min(((I_409\Timer * 0.2) ^ 2.0) / 1000.0, 0.5))
		BlurTimer = Max(I_409\Timer * 3.0 * (2.0 - CrouchState), BlurTimer)
		
		If I_409\Timer > 40.0 And PrevI409Timer <= 40.0 Then
			If I_409\Revert Then
				CreateMsg(GetLocalString("Items","scp409_2"))
			Else
				CreateMsg(GetLocalString("Items","scp409_3"))
			EndIf
		ElseIf I_409\Timer > 55.0 And PrevI409Timer <= 55.0
			If I_409\Revert Then
				CreateMsg(GetLocalString("Items","scp409_4"))
			Else
				CreateMsg(GetLocalString("Items","scp409_5"))
			EndIf
		ElseIf I_409\Timer > 70.0 And PrevI409Timer <= 70.0
			If I_409\Revert Then
				CreateMsg(GetLocalString("Items","scp409_6"))
			Else
				CreateMsg(GetLocalString("Items","scp409_7"))
			EndIf
		ElseIf I_409\Timer > 85.0 And PrevI409Timer <= 85.0
			If I_409\Revert Then
				CreateMsg(GetLocalString("Items","scp409_8"))
			Else
				CreateMsg(GetLocalString("Items","scp409_8"))
			EndIf
		ElseIf I_409\Timer > 93.0 And PrevI409Timer <= 93.0
			If (Not I_409\Revert) Then
				PlaySound_Strict(DamageSFX[9])
				DamageSPPlayer(10,True)
			EndIf
		ElseIf I_409\Timer > 94.0
			I_409\Timer = Min(I_409\Timer + (FPSfactor * 0.004), 100.0)
			Playable = False
			BlurTimer = 4.0
			CameraShake = 3.0
		EndIf
		If I_409\Timer >= 55.0 Then
			StaminaEffect = 1.2
			StaminaEffectTimer = 1.0
			Stamina = Min(Stamina, 60.0)
		EndIf
		If I_409\Timer >= 96.9222 Then
			m_msg\DeathTxt = GetLocalStringR("Singleplayer","scp409_death_1",Designation)
			m_msg\DeathTxt = m_msg\DeathTxt + GetLocalString("Singleplayer","scp409_death_2")
			Kill()
		EndIf
	Else
		If I_409\Revert Then I_409\Revert = False
		If (Not EntityHidden(SCP409Overlay)) Then HideEntity(SCP409Overlay)	
	EndIf
	
End Function

Function Use427()
	Local i%,pvt%,de.Decals,tempchn%
	Local prevI427Timer# = I_427\Timer
	
	If I_427\Timer < 70*360
		If I_427\Using=True Then
			I_427\Timer = I_427\Timer + FPSfactor
			HealSPPlayer(0.05 * FPSfactor)
			If I_008\Timer > 0.0 Then
				I_008\Timer = Max(I_008\Timer - 0.001 * FPSfactor,0.0)
			EndIf
;			For i = 0 To 5
;				If I_1025\State[i]>0.0 Then
;					I_1025\State[i] = Max(I_1025\State[i] - 0.001 * FPSfactor,0.0)
;				EndIf
;			Next
;			If I_1079\Foam > 0.0 And psp\Health =< 70 Then
;				I_1079\Foam = Max(I_1079\Foam - 0.001 * FPSfactor, 0.0)
;			EndIf
			If I_207\Timer > 0.0 Then
				I_207\Timer = Max(I_207\Timer - 0.001 * FPSfactor, 0.0)
			EndIf
			;If I_357\Timer > 0.0 Then
			;	I_357\Timer = Max(I_357\Timer - 0.002 * FPSfactor,0.0)
			;EndIf
			If I_427\Sound[0]=0 Then
				I_427\Sound[0] = LoadSound_Strict("SFX\SCP\427\Effect.ogg")
			EndIf
			If (Not ChannelPlaying(I_427\SoundCHN[0])) Then
				I_427\SoundCHN[0] = PlaySound_Strict(I_427\Sound[0])
			EndIf
			
			If I_427\Timer => 70*180 Then
				If I_427\Sound[1]=0 Then
					I_427\Sound[1] = LoadSound_Strict("SFX\SCP\427\Transform.ogg")
				EndIf
				If (Not ChannelPlaying(I_427\SoundCHN[1])) Then
					I_427\SoundCHN[1] = PlaySound_Strict(I_427\Sound[1])
				EndIf
			EndIf
			If prevI427Timer < 70*60 And I_427\Timer => 70*60 Then
				CreateMsg(GetLocalString("Singleplayer","scp427_1"))
			ElseIf prevI427Timer < 70*180 And I_427\Timer => 70*180 Then
				CreateMsg(GetLocalString("Singleplayer","scp427_2"))
			EndIf
		Else
			For i = 0 To 1
				If I_427\SoundCHN[i]<>0 Then
					If ChannelPlaying(I_427\SoundCHN[i]) Then
						StopChannel(I_427\SoundCHN[i])
					EndIf
				EndIf
			Next
		EndIf
	Else
		If prevI427Timer-FPSfactor < 70*360 And I_427\Timer => 70*360 Then
			CreateMsg(GetLocalString("Singleplayer","scp427_3"))
		ElseIf prevI427Timer-FPSfactor < 70*390 And I_427\Timer => 70*390 Then
			CreateMsg(GetLocalString("Singleplayer","scp427_4"))
		EndIf
		I_427\Timer = I_427\Timer + FPSfactor
		If I_427\Sound[0]=0 Then
			I_427\Sound[0] = LoadSound_Strict("SFX\SCP\427\Effect.ogg")
		EndIf
		If I_427\Sound[1]=0 Then
			I_427\Sound[1] = LoadSound_Strict("SFX\SCP\427\Transform.ogg")
		EndIf
		For i = 0 To 1
			If (Not ChannelPlaying(I_427\SoundCHN[i])) Then
				I_427\SoundCHN[i] = PlaySound_Strict(I_427\Sound[i])
			EndIf
		Next
		If Rnd(200)<2.0 Then
			pvt = CreatePivot()
			PositionEntity pvt, EntityX(Collider)+Rnd(-0.05,0.05),EntityY(Collider)-0.05,EntityZ(Collider)+Rnd(-0.05,0.05)
			TurnEntity pvt, 90, 0, 0
			EntityPick(pvt,0.3)
			de.Decals = CreateDecal(DECAL_FOAM, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
			de\Size = Rnd(0.03,0.08)*2.0 : EntityAlpha(de\obj, 1.0) : ScaleSprite de\obj, de\Size, de\Size
			tempchn% = PlaySound_Strict (DripSFX[Rand(0,3)])
			ChannelVolume tempchn, Rnd(0.0,0.8)*(opt\SFXVolume*opt\MasterVol)
			ChannelPitch tempchn, Rand(20000,30000)
			FreeEntity pvt
			BlurTimer = 800
		EndIf
		If I_427\Timer >= 70*420 Then
			Kill()
			m_msg\DeathTxt = Chr(34)+GetLocalString("Singleplayer","scp427_death")+Chr(34)
		ElseIf I_427\Timer >= 70*390 Then
			Crouch = True
		EndIf
	EndIf
	
End Function


Const ROUGH% = -2, COARSE% = -1, ONETOONE% = 0, FINE% = 1, VERY_FINE% = 2

Function Use914(item.Items, setting%, x#, y#, z#)
	Local d.Decals,n.NPCs,it.Items,i
	
	RefinedItems = RefinedItems + 1
	
	Local it2.Items
	Select item\itemtemplate\name
		Case GetLocalString("Item Names","gas_mask"), GetLocalString("Item Names","gas_mask_heavy")
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					RemoveItem(item)
				Case ONETOONE
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
				Case FINE, VERY_FINE
					it2 = CreateItem(GetLocalString("Item Names","gas_mask"), "gasmask2", x, y, z)
					RemoveItem(item)
			End Select
		Case GetLocalString("Item Names","vest")
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					RemoveItem(item)
				Case ONETOONE
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
				Case FINE
					it2 = CreateItem(GetLocalString("Item Names","vest_heavy"), "vest2", x, y, z)
					RemoveItem(item)
				Case VERY_FINE
					it2 = CreateItem(GetLocalString("Item Names","vest_bulky"), "vest3", x, y, z)
					RemoveItem(item)
			End Select
		Case GetLocalString("Item Names","nvg")
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
					RemoveItem(item)
				Case ONETOONE
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)
				Case FINE
					it2 = CreateItem(GetLocalString("Item Names","nvg"), "nvg3", x, y, z)
					RemoveItem(item)
				Case VERY_FINE
					it2 = CreateItem(GetLocalString("Item Names","nvg"), "nvg2", x, y, z)
					it2\state = 1000
					RemoveItem(item)
			End Select
		Case GetLocalString("Item Names","severed_hand"), GetLocalString("Item Names","severed_hand_black"), GetLocalString("Item Names","severed_hand_yellow")
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_BLOODSPLAT2, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE, FINE, VERY_FINE
					If (item\itemtemplate\name = GetLocalString("Item Names","severed_hand"))
						it2 = CreateItem(GetLocalString("Item Names","severed_hand_yellow"), "hand3", x, y, z)
					ElseIf (item\itemtemplate\name = GetLocalString("Item Names","severed_hand_black"))
						it2 = CreateItem(GetLocalString("Item Names","severed_hand"), "hand", x, y, z)	
					Else
						it2 = CreateItem(GetLocalString("Item Names","severed_hand_black"), "hand2", x, y, z)
					EndIf
			End Select
			RemoveItem(item)
		Case GetLocalString("Item Names","first_aid"), GetLocalString("Item Names","first_aid_blue")
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.12 : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					If Rand(2)=1 Then
						it2 = CreateItem(GetLocalString("Item Names","first_aid_blue"), "firstaid2", x, y, z)
					Else
						it2 = CreateItem(GetLocalString("Item Names","first_aid"), "firstaid", x, y, z)
					EndIf
				Case FINE
					it2 = CreateItem(GetLocalString("Item Names","first_aid_small"), "finefirstaid", x, y, z)
				Case VERY_FINE
					it2 = CreateItem(GetLocalString("Item Names","strange_bottle"), "veryfinefirstaid", x, y, z)
			End Select
			RemoveItem(item)
		Case GetLocalString("Item Names","key_0"), GetLocalString("Item Names","key_1"), GetLocalString("Item Names","key_2"), GetLocalString("Item Names","key_3"), GetLocalString("Item Names","key_4"), GetLocalString("Item Names","key_5"), GetLocalString("Item Names","key_omni"), GetLocalString("Item Names","key_cave"), GetLocalString("Item Names","key_cave_2"), GetLocalString("Item Names","key_class_d")
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.07 : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					it2 = CreateItem(GetLocalString("Item Names","playing_card"), "misc", x, y, z)
				Case FINE
					Select item\itemtemplate\name
						Case GetLocalString("Item Names","key_cave")
							Select SelectedDifficulty\OtherFactors
								Case EASY
									it2 = CreateItem(GetLocalString("Item Names","key_cave_2"), "key_cave2", x, y, z)
								Case NORMAL
									If Rand(2)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_cave_2"), "key_cave2", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case HARD
									If Rand(3)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_cave_2"), "key_cave2", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case HARDER
									If Rand(4)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_cave_2"), "key_cave2", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case IMPOSSIBLE
									If Rand(5)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_cave_2"), "key_cave2", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
							End Select
						Case GetLocalString("Item Names","key_cave_2")
							Select SelectedDifficulty\OtherFactors
								Case EASY
									it2 = CreateItem(GetLocalString("Item Names","key_cave"), "key_cave", x, y, z)
								Case NORMAL
									If Rand(2)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_cave"), "key_cave", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case HARD
									If Rand(3)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_cave"), "key_cave", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case HARDER
									If Rand(4)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_cave"), "key_cave", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case IMPOSSIBLE
									If Rand(5)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_cave"), "key_cave", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
							End Select
						Case GetLocalString("Item Names","key_0")
							Select SelectedDifficulty\OtherFactors
								Case EASY
									it2 = CreateItem(GetLocalString("Item Names","key_1"), "key1", x, y, z)
								Case NORMAL
									If Rand(2)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_1"), "key1", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case HARD
									If Rand(3)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_1"), "key1", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case HARDER
									If Rand(4)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_1"), "key1", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case IMPOSSIBLE
									If Rand(5)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_1"), "key1", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
							End Select
						Case GetLocalString("Item Names","key_1")
							Select SelectedDifficulty\OtherFactors
								Case EASY
									it2 = CreateItem(GetLocalString("Item Names","key_2"), "key2", x, y, z)
								Case NORMAL
									If Rand(2)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_2"), "key2", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case HARD
									If Rand(3)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_2"), "key2", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case HARDER
									If Rand(4)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_2"), "key2", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case IMPOSSIBLE
									If Rand(5)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_2"), "key2", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
							End Select
						Case GetLocalString("Item Names","key_2")
							Select SelectedDifficulty\OtherFactors
								Case EASY
									it2 = CreateItem(GetLocalString("Item Names","key_3"), "key3", x, y, z)
								Case NORMAL
									If Rand(2)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_3"), "key3", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case HARD
									If Rand(3)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_3"), "key3", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case HARDER
									If Rand(6)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_3"), "key3", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case IMPOSSIBLE
									If Rand(9)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_3"), "key3", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
							End Select
						Case GetLocalString("Item Names","key_3")
							Select SelectedDifficulty\OtherFactors
								Case EASY
									If Rand(10)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_4"), "key4", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","playing_card"), "misc", x, y, z)	
									EndIf
								Case NORMAL
									If Rand(15)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_4"), "key4", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","playing_card"), "misc", x, y, z)	
									EndIf
								Case HARD
									If Rand(20)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_4"), "key4", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","playing_card"), "misc", x, y, z)	
									EndIf
								Case HARDER
									If Rand(25)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_4"), "key4", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","playing_card"), "misc", x, y, z)	
									EndIf
								Case IMPOSSIBLE
									If Rand(30)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_4"), "key4", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","playing_card"), "misc", x, y, z)	
									EndIf
							End Select
						Case GetLocalString("Item Names","key_4")
							Select SelectedDifficulty\OtherFactors
								Case EASY
									If Rand(2)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_5"), "key5", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case NORMAL
									If Rand(4)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_5"), "key5", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case HARD
									If Rand(6)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_5"), "key5", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case HARDER
									If Rand(8)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_5"), "key5", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case IMPOSSIBLE
									If Rand(10)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_5"), "key5", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
							End Select
						Case GetLocalString("Item Names","key_5")
							Select SelectedDifficulty\OtherFactors
								Case EASY
									If Rand(6)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_omni"), "key6", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case NORMAL
									If Rand(12)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_omni"), "key6", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case HARD
									If Rand(15)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_omni"), "key6", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case HARDER
									If Rand(21)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_omni"), "key6", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
								Case IMPOSSIBLE
									If Rand(30)=1 Then
										it2 = CreateItem(GetLocalString("Item Names","key_omni"), "key6", x, y, z)
									Else
										it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
									EndIf
							End Select
						Case GetLocalString("Item Names","key_omni")
							it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
					End Select
				Case VERY_FINE
					Select SelectedDifficulty\OtherFactors
						Case EASY
							it2 = CreateItem(GetLocalString("Item Names","key_omni"), "key6", x, y, z)
						Case NORMAL
							If Rand(4)=1 Then
								it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
							Else
								it2 = CreateItem(GetLocalString("Item Names","key_omni"), "key6", x, y, z)
							EndIf
						Case HARD
							If Rand(3)=1 Then
								it2 = CreateItem(GetLocalString("Item Names","mastercard"), "misc", x, y, z)
							Else
								it2 = CreateItem(GetLocalString("Item Names","key_omni"), "key6", x, y, z)
							EndIf
					End Select
			End Select
			RemoveItem(item)
		Case GetLocalString("Item Names","playing_card"), GetLocalString("Item Names","coin"), GetLocalString("Item Names","coin_rusty")
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.07 : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					it2 = CreateItem(GetLocalString("Item Names","key_0"), "key0", x, y, z)	
			    Case FINE
					it2 = CreateItem(GetLocalString("Item Names","key_1"), "key1", x, y, z)
				Case VERY_FINE
					it2 = CreateItem(GetLocalString("Item Names","key_2"), "key2", x, y, z)
			End Select
			RemoveItem(item)
		Case GetLocalString("Item Names","mastercard")
			Select setting
				Case ROUGH
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.07 : ScaleSprite(d\obj, d\Size, d\Size)
				Case COARSE
					it2 = CreateItem(GetLocalString("Item Names","coin_rusty"), "25ct", x, y, z)
					Local it3.Items,it4.Items,it5.Items
					it3 = CreateItem(GetLocalString("Item Names","coin_rusty"), "25ct", x, y, z)
					it4 = CreateItem(GetLocalString("Item Names","coin_rusty"), "25ct", x, y, z)
					it5 = CreateItem(GetLocalString("Item Names","coin_rusty"), "25ct", x, y, z)
					EntityType (it3\collider, HIT_ITEM)
					EntityType (it4\collider, HIT_ITEM)
					EntityType (it5\collider, HIT_ITEM)
				Case ONETOONE
					it2 = CreateItem(GetLocalString("Item Names","key_0"), "key0", x, y, z)	
			    Case FINE
					it2 = CreateItem(GetLocalString("Item Names","key_1"), "key1", x, y, z)
				Case VERY_FINE
					it2 = CreateItem(GetLocalString("Item Names","key_2"), "key2", x, y, z)
			End Select
			RemoveItem(item)
		Case GetLocalString("Item Names","nav"), GetLocalString("Item Names","nav_300"), GetLocalString("Item Names","nav_310"), GetLocalString("Item Names","nav_ultimate")
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.07 : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					it2 = CreateItem(GetLocalString("Item Names","nav"), "nav", x, y, z)
					it2\state = 100
				Case FINE
					it2 = CreateItem(GetLocalString("Item Names","nav_310"), "nav", x, y, z)
					it2\state = 100
				Case VERY_FINE
					it2 = CreateItem(GetLocalString("Item Names","nav_ultimate"), "nav", x, y, z)
					it2\state = 101
			End Select
			
			RemoveItem(item)
		Case GetLocalString("Item Names","radio")
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
					d\Size = 0.07 : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					it2 = CreateItem(GetLocalString("Item Names","radio"), "18vradio", x, y, z)
					it2\state = 100
				Case FINE
					it2 = CreateItem(GetLocalString("Item Names","radio"), "fineradio", x, y, z)
					it2\state = 101
				Case VERY_FINE
					it2 = CreateItem(GetLocalString("Item Names","radio"), "veryfineradio", x, y, z)
					it2\state = 101
			End Select
			
			RemoveItem(item)
		Case "SCP-513"
			Select setting
				Case ROUGH, COARSE
					PlaySound_Strict LoadTempSound("SFX\SCP\513\914Refine.ogg")
					For n.NPCs = Each NPCs
						If n\NPCtype = NPC_SCP_513 Then RemoveNPC(n)
					Next
					d.Decals = CreateDecal(DECAL_DECAY, x, 8*RoomScale+0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE, FINE, VERY_FINE
					it2 = CreateItem("SCP-513", "scp513", x, y, z)
					
			End Select
			
			RemoveItem(item)
		Case GetLocalString("Item Names","bat_9v"), GetLocalString("Item Names","bat_18v"), GetLocalString("Item Names","bat_strange")
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					it2 = CreateItem(GetLocalString("Item Names","bat_18v"), "18vbat", x, y, z)
				Case FINE, VERY_FINE
					it2 = CreateItem(GetLocalString("Item Names","bat_strange"), "killbat", x, y, z)
			End Select
			RemoveItem(item)
		Case GetLocalString("Item Names","hazmat")
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					it2 = CreateItem(GetLocalString("Item Names","hazmat"), "hazmat", x,y,z)
				Case FINE
					it2 = CreateItem(GetLocalString("Item Names","hazmat"), "hazmat2", x,y,z)
				Case VERY_FINE
					it2 = CreateItem(GetLocalString("Item Names","hazmat_heavy"), "hazmat3", x,y,z)
			End Select
			RemoveItem(item)
		Case GetLocalString("Item Names","syringe")
			Select item\itemtemplate\tempname
				Case "syringe"
					Select setting
						Case ROUGH, COARSE
							d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
							d\Size = 0.07 : ScaleSprite(d\obj, d\Size, d\Size)
						Case ONETOONE
							it2 = CreateItem(GetLocalString("Item Names","first_aid_small"), "finefirstaid", x, y, z)	
						Case FINE, VERY_FINE
							it2 = CreateItem(GetLocalString("Item Names","syringe"), "finesyringe", x, y, z)
					End Select
				Case "finesyringe"
					Select setting
						Case ROUGH
							d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.005, z, 90, Rand(360), 0)
							d\Size = 0.07 : ScaleSprite(d\obj, d\Size, d\Size)
						Case COARSE
							it2 = CreateItem(GetLocalString("Item Names","first_aid"), "firstaid", x, y, z)
						Case ONETOONE
							it2 = CreateItem(GetLocalString("Item Names","first_aid_blue"), "firstaid2", x, y, z)	
						Case FINE, VERY_FINE
							it2 = CreateItem(GetLocalString("Item Names","syringe"), "veryfinesyringe", x, y, z)
					End Select
				Case "veryfinesyringe"
					n.NPCs = CreateNPC(NPC_Zombie,x,y,z,Surgeon_Zombie)
					n\State[0] = 2
			End Select
			
			RemoveItem(item)
			
		Case "SCP-500-01", GetLocalString("Item Names","pill"), GetLocalString("Item Names","pill_upgraded")
			Select setting
				Case ROUGH, COARSE
					d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.010, z, 90, Rand(360), 0)
					d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
				Case ONETOONE
					it2 = CreateItem(GetLocalString("Item Names","pill"), "pill", x, y, z)
					RemoveItem(item)
				Case FINE
					Local no427Spawn% = False
					For it3.Items = Each Items
						If it3\itemtemplate\tempname = "scp427" Then
							no427Spawn = True
							Exit
						EndIf
					Next
					If (Not no427Spawn) Then
						it2 = CreateItem("SCP-427", "scp427", x, y, z)
					Else
						it2 = CreateItem(GetLocalString("Item Names","pill_upgraded"), "scp500death", x, y, z)
					EndIf
					RemoveItem(item)
				Case VERY_FINE
					it2 = CreateItem(GetLocalString("Item Names","pill_upgraded"), "scp500death", x, y, z)
					RemoveItem(item)
			End Select
		Default
			Select item\itemtemplate\tempname
				Case GetLocalString("Item Names","cup")
					Select setting
						Case ROUGH, COARSE
							d.Decals = CreateDecal(DECAL_DECAY, x, 8 * RoomScale + 0.010, z, 90, Rand(360), 0)
							d\Size = 0.2 : EntityAlpha(d\obj, 0.8) : ScaleSprite(d\obj, d\Size, d\Size)
						Case ONETOONE
							it2 = CreateItem(GetLocalString("Item Names","cup"), "cup", x,y,z, 255-item\r,255-item\g,255-item\b,item\a)
							it2\name = item\name
							it2\state = item\state
						Case FINE
							it2 = CreateItem(GetLocalString("Item Names","cup"), "cup", x,y,z, Min(item\r*Rnd(0.9,1.1),255),Min(item\g*Rnd(0.9,1.1),255),Min(item\b*Rnd(0.9,1.1),255),item\a)
							it2\name = item\name
							it2\state = item\state+1.0
						Case VERY_FINE
							it2 = CreateItem(GetLocalString("Item Names","cup"), "cup", x,y,z, Min(item\r*Rnd(0.5,1.5),255),Min(item\g*Rnd(0.5,1.5),255),Min(item\b*Rnd(0.5,1.5),255),item\a)
							it2\name = item\name
							it2\state = item\state*2
							If Rand(5)=1 Then
								ExplosionTimer = 135
							EndIf
					End Select	
					
					RemoveItem(item)
				Default
					PositionEntity(item\collider, x, y, z)
					ResetEntity(item\collider)	
			End Select
			
	End Select
	
	If it2 <> Null Then EntityType (it2\collider, HIT_ITEM)
End Function

Function Damage1033RU(dmg%, rndm%=True)
	Local i
	
	For i = 0 To 3
	    If I_1033RU\Sound[i] = 0 Then
	        I_1033RU\Sound[i] = LoadSound_Strict("SFX\SCP\1033RU\Damage" + i + ".ogg")
	    EndIf
	Next
	
	If I_1033RU\Sound2[0] = 0 Then
	    I_1033RU\Sound2[0] = LoadSound_Strict("SFX\SCP\1033RU\Death.ogg")
	EndIf
	
	;If dmg% > 0 And dmg% < 15 Then
	;    Bloodloss = Bloodloss + Rnd(5)
	;ElseIf dmg% >= 15 And dmg% < 30
	;    Bloodloss = Bloodloss + Rnd(7.5)
	;ElseIf dmg% >= 30 And dmg% < 50
	;    Bloodloss = Bloodloss + Rnd(11.25)
	;Else
	;    Bloodloss = Bloodloss + Rnd(16.875)
	;EndIf
	
	LightFlash = 0.2
	
	If rndm% = True Then dmg% = Rand(Int(dmg%))
	
	I_1033RU\HP = I_1033RU\HP - Int(dmg%)
	
    If I_1033RU\HP =< 0 Then I_1033RU\HP = 0
	
	If I_1033RU\Using = 2 Then
	    If I_1033RU\HP > 200 Then I_1033RU\HP = 200
	Else
	    If I_1033RU\HP > 100 Then I_1033RU\HP = 100
    EndIf
	
	I_1033RU\DHP = I_1033RU\DHP + Int(dmg%)
	
	If I_1033RU\DHP =< 0 Then I_1033RU\DHP = 0
	
	If I_1033RU\Using = 2 Then
	    If I_1033RU\DHP > 200 Then I_1033RU\DHP = 200
	Else
	    If I_1033RU\DHP > 100 Then I_1033RU\DHP = 100
	EndIf
	
	If I_1033RU\HP > 0 Then
	    PlaySound_Strict I_1033RU\Sound[Rand(0, 3)]
	Else
	    PlaySound_Strict I_1033RU\Sound2[0]
	EndIf
	
End Function

Function IsItemGoodFor1162(itt.ItemTemplates)
	
	Select itt\tempname
		Case "knife", "usp", "key3"
			Return True
		Case "misc", "420", "cigarette"
			Return True
		Case "vest", "vest2","helmet"
			Return True
		Case "radio","18vradio"
			Return True
		Case "clipboard","eyedrops","wallet"
			Return True
		Default
			Return False
	End Select
End Function

;[End Block]
;~IDEal Editor Parameters:
;~C#Blitz3D TSS