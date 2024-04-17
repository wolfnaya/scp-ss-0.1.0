
;[Block]
;Lower body states
Const STATE_BODY_LOWER_DIE = 0
Const STATE_BODY_LOWER_IDLE = 1
Const STATE_BODY_LOWER_CROUCH_IDLE = 2
Const STATE_BODY_LOWER_CROUCH_FORWARD = 3
Const STATE_BODY_LOWER_CROUCH_LEFT = 4
Const STATE_BODY_LOWER_CROUCH_RIGHT = 5
Const STATE_BODY_LOWER_RUN = 6
Const STATE_BODY_LOWER_WALK_FORWARD = 7
Const STATE_BODY_LOWER_WALK_LEFT = 8
Const STATE_BODY_LOWER_WALK_RIGHT = 9

;Upper body pistol states
Const STATE_BODY_UPPER_PISTOL_DIE = 0
Const STATE_BODY_UPPER_PISTOL_IDLE = 1
Const STATE_BODY_UPPER_PISTOL_RELOAD = 2
Const STATE_BODY_UPPER_PISTOL_SHOT = 3
Const STATE_BODY_UPPER_PISTOL_RUN = 4
;Upper body rifle states
Const STATE_BODY_UPPER_RIFLE_DIE = 5
Const STATE_BODY_UPPER_RIFLE_IDLE = 6
Const STATE_BODY_UPPER_RIFLE_RELOAD = 7
Const STATE_BODY_UPPER_RIFLE_SHOT = 8
Const STATE_BODY_UPPER_RIFLE_RUN = 9
;Upper body shotgun states
Const STATE_BODY_UPPER_SHOTGUN_DIE = 10
Const STATE_BODY_UPPER_SHOTGUN_IDLE = 11
Const STATE_BODY_UPPER_SHOTGUN_RELOAD = 12
Const STATE_BODY_UPPER_SHOTGUN_SHOT = 13
Const STATE_BODY_UPPER_SHOTGUN_RUN = 14
Const STATE_BODY_UPPER_SHOTGUN_IDLE_TO_PUMP = 15
Const STATE_BODY_UPPER_SHOTGUN_RELOAD_TP_PUMP = 16
;Upper body smg states
Const STATE_BODY_UPPER_SMG_DIE = 17
Const STATE_BODY_UPPER_SMG_IDLE = 18
Const STATE_BODY_UPPER_SMG_RELOAD = 19
Const STATE_BODY_UPPER_SMG_SHOT = 20
Const STATE_BODY_UPPER_SMG_RUN = 21
;Upper body melee states
Const STATE_BODY_UPPER_MELEE_DIE = 22
Const STATE_BODY_UPPER_MELEE_IDLE = 23
Const STATE_BODY_UPPER_MELEE_STAB = 24
;Upper body mp5k states
Const STATE_BODY_UPPER_MP5K_DIE = 25
Const STATE_BODY_UPPER_MP5K_IDLE = 26
Const STATE_BODY_UPPER_MP5K_RELOAD = 27
Const STATE_BODY_UPPER_MP5K_SHOT = 28
Const STATE_BODY_UPPER_MP5K_RUN = 29
;[End Block]

Function AnimatePlayerModelUp(up%,state_upper%,animspeed_multiply#=1.0)
	
	Local animspeed# = 0.5 * animspeed_multiply#
	Local animloop% = True
	
	Select state_upper%
		;Pistol
		Case STATE_BODY_UPPER_PISTOL_DIE
			animspeed = animspeed * 2.5
			animloop = False
		Case STATE_BODY_UPPER_PISTOL_SHOT, STATE_BODY_UPPER_PISTOL_RELOAD
			animspeed = animspeed * 2.0
			animloop = False
		Case STATE_BODY_UPPER_PISTOL_RUN
			animspeed = animspeed * 3.0
		;Rifle
		Case STATE_BODY_UPPER_RIFLE_DIE
			animspeed = animspeed * 2.5
			animloop = False
		Case STATE_BODY_UPPER_RIFLE_SHOT, STATE_BODY_UPPER_RIFLE_RELOAD
			animspeed = animspeed * 2.0
			animloop = False
		Case STATE_BODY_UPPER_RIFLE_RUN
			animspeed = animspeed * 3.0
		;Shotgun
		Case STATE_BODY_UPPER_SHOTGUN_DIE
			animspeed = animspeed * 2.5
			animloop = False
		Case STATE_BODY_UPPER_SHOTGUN_SHOT, STATE_BODY_UPPER_SHOTGUN_IDLE_TO_PUMP, STATE_BODY_UPPER_SHOTGUN_RELOAD_TP_PUMP
			animspeed = animspeed * 2.0
			animloop = False
		Case STATE_BODY_UPPER_SHOTGUN_RELOAD
			animspeed = animspeed * 1.0
		Case STATE_BODY_UPPER_SHOTGUN_RUN
			animspeed = animspeed * 3.0
		;SMG
		Case STATE_BODY_UPPER_SMG_DIE	
			animspeed = animspeed * 2.5
			animloop = False
		Case STATE_BODY_UPPER_SMG_SHOT, STATE_BODY_UPPER_SMG_RELOAD
			animspeed = animspeed * 2.0
			animloop = False
		Case STATE_BODY_UPPER_SMG_RUN
			animspeed = animspeed * 3.0
		;Melee
		Case STATE_BODY_UPPER_MELEE_DIE
			animspeed = animspeed * 2.5
			animloop = False
		Case STATE_BODY_UPPER_MELEE_STAB
			animspeed = animspeed * 1.5
			animloop = False
		;MP5K
		Case STATE_BODY_UPPER_MP5K_DIE
			animspeed = animspeed * 2.5
			animloop = False
		Case STATE_BODY_UPPER_MP5K_SHOT, STATE_BODY_UPPER_MP5K_RELOAD
			animspeed = animspeed * 2.0
			animloop = False
		Case STATE_BODY_UPPER_MP5K_RUN
			animspeed = animspeed * 3.0
	End Select
	Animate(up, 1 + (2 * (Not animloop)), animspeed#, state_upper+1, 10.0)
	
End Function

Function AnimatePlayerModelLow(low%,state_lower%,animspeed_multiply#=1.0)
	
	Local animspeed# = 0.5 * animspeed_multiply#
	Local animloop% = True
	
	Select state_lower%
		Case STATE_BODY_LOWER_DIE
			animspeed = animspeed * 2.5
			animloop = False
		Case STATE_BODY_LOWER_RUN
			animspeed = animspeed * 2.75
		Case STATE_BODY_LOWER_WALK_FORWARD, STATE_BODY_LOWER_CROUCH_LEFT, STATE_BODY_LOWER_CROUCH_RIGHT
			animspeed = animspeed * 2.0
	End Select
	Animate(low, 1 + (2 * (Not animloop)), animspeed#, state_lower+1, 10.0)
	
End Function

Function PlayPlayerStepSounds(playerid%, low%, state_lower%)
	Local animframe# = AnimTime(low)
	Local checkFrame1#, checkFrame2#
	Local isRunning% = False
	Local temp%
	
	Select state_lower%
		Case STATE_BODY_LOWER_WALK_FORWARD, STATE_BODY_LOWER_CROUCH_FORWARD
			checkFrame1# = 30.0
			checkFrame2# = 60.0
		Case STATE_BODY_LOWER_WALK_LEFT, STATE_BODY_LOWER_WALK_RIGHT
			checkFrame1# = 15.0
			checkFrame2# = 30.0
		Case STATE_BODY_LOWER_CROUCH_LEFT, STATE_BODY_LOWER_CROUCH_RIGHT
			;checkFrame1# = 28.0
			checkFrame2# = 56.0
		Case STATE_BODY_LOWER_RUN
			checkFrame1# = 25.0
			checkFrame2# = 50.0
			isRunning% = True
	End Select
	
	If animframe < checkFrame1 And animframe+FPSfactor > checkFrame1 Then
		temp = GetStepSound(Players[playerid]\Collider)
		If (Not isRunning) Then
			PlaySound2(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)],Camera,Players[playerid]\Collider,5,(1.0-(Players[playerid]\Crouch*0.6)*(opt\SFXVolume#*opt\MasterVol)))
		Else
			PlaySound2(mpl\StepSoundRun[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)],Camera,Players[playerid]\Collider,5,(1.0-(Players[playerid]\Crouch*0.6)*(opt\SFXVolume#*opt\MasterVol)))
		EndIf
	ElseIf animframe < checkFrame2 And animframe+FPSfactor > checkFrame2 Then
		temp = GetStepSound(Players[playerid]\Collider)
		If (Not isRunning) Then
			PlaySound2(mpl\StepSoundWalk[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)],Camera,Players[playerid]\Collider,5,(1.0-(Players[playerid]\Crouch*0.6)*(opt\SFXVolume#*opt\MasterVol)))
		Else
			PlaySound2(mpl\StepSoundRun[Rand(0,MaxStepSounds-1)+(temp*MaxStepSounds)],Camera,Players[playerid]\Collider,5,(1.0-(Players[playerid]\Crouch*0.6)*(opt\SFXVolume#*opt\MasterVol)))
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D