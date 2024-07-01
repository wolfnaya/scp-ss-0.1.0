
; ~ [NPCs constants]

;[Block]
Const NPC_SCP_173 = 1,NPC_SCP_106 = 2, NPC_SCP_096 = 3, NPC_SCP_049 = 4, NPC_SCP_372 = 5, NPC_SCP_513 = 6, NPC_SCP_035 = 7, NPC_SCP_035_Tentacle = 8, NPC_SCP_939 = 9
Const NPC_SCP_860 = 10, NPC_SCP_966 = 11, NPC_SCP_1048 = 12, NPC_SCP_076 = 13, NPC_SCP_457 = 14, NPC_SCP_682 = 15, NPC_Zombie = 16, NPC_Human = 17, NPC_Class_D = 18, NPC_Scientist = 19
Const NPC_Guard = 20, NPC_Vincent = 21, NPC_NTF = 22, NPC_NTF_HDS = 23, NPC_NTF_HDS_Boss = 24, NPC_CI = 25, NPC_SM4Nn = 26, NPC_Zombie_Armed = 27, NPC_Elias = 28

Const MaxHitBoxes% = 25
Const MaxWayPoints% = 21
Const MaxNPCStates% = 8
Const STATE_SCRIPT = 99
;[End Block]

Include "SourceCode\NPCs\NPCtype035.bb"
Include "SourceCode\NPCs\NPCtype049.bb"
Include "SourceCode\NPCs\NPCtype076.bb"
Include "SourceCode\NPCs\NPCtype096.bb"
Include "SourceCode\NPCs\NPCtype106.bb"
Include "SourceCode\NPCs\NPCtype173.bb"
Include "SourceCode\NPCs\NPCtype457.bb"
Include "SourceCode\NPCs\NPCtype682.bb"
Include "SourceCode\NPCs\NPCtype939.bb"
Include "SourceCode\NPCs\NPCtype966.bb"
Include "SourceCode\NPCs\NPCtype1048a.bb"
Include "SourceCode\NPCs\NPCtypeGuard.bb"
Include "SourceCode\NPCs\NPCtypeMTF.bb"
Include "SourceCode\NPCs\NPCtypeZombie.bb"
Include "SourceCode\NPCs\NPCtypeZombieMP.bb"
Include "SourceCode\NPCs\NPCTypeD2.bb"
Include "SourceCode\NPCs\NPCtypeCI.bb"
Include "SourceCode\NPCs\NPCtypeScientist.bb"

Global Curr049.NPCs, Curr096.NPCs, Curr106.NPCs, Curr173.NPCs, Curr5131.NPCs, CurrD9341.NPCs

Type NPCs
	Field obj%, obj2%, obj3%, obj4%, Collider%
	Field NPCtype%, ID%
	Field DropSpeed#, Gravity%
	Field State[MaxNPCStates], PrevState%
	Field MakingNoise%
	Field NPCName$
	Field Frame#
	Field Angle#
	Field Sound%, SoundChn%, SoundTimer#
	Field Sound2%, SoundChn2%
	Field Speed#, CurrSpeed#
	Field texture$
	Field Idle#
	Field Reload#
	Field LastSeen%, LastDist#
	Field FearTarget%
	Field PrevX#, PrevY#, PrevZ#
	Field Target.NPCs, TargetID%
	Field EnemyX#, EnemyY#, EnemyZ#
	Field Path.WayPoints[MaxWayPoints], PathStatus%, PathTimer#, PathLocation%
	Field NVX#,NVY#,NVZ#,NVName$
	Field GravityMult# = 1.0
	Field MaxGravity# = 0.2
	Field IsDead%
	Field BlinkTimer# = 1.0
	Field IgnorePlayer%
	Field ManipulateBone%
	Field ManipulationType%
	Field BoneToManipulate$
	Field BonePitch#
	Field BoneYaw#
	Field BoneRoll#
	Field NPCNameInSection$
	Field InFacility% = True
	Field CanUseElevator% = False
	Field CurrElevator.ElevatorObj
	Field HP%
	Field PathX#,PathZ#
	Field Model$
	Field ModelScaleX#,ModelScaleY#,ModelScaleZ#
	Field HideFromNVG
	Field TextureID%=-1
	Field CollRadius#
	Field IdleTimer#
	Field SoundChn_IsStream%,SoundChn2_IsStream%
	Field FallingPickDistance#
	Field Clearance%
	Field Gun.NPCGun
	Field NPCRoom.Rooms
	Field Contained%
	Field BossName$
	Field Boss.NPCs
	Field MaxBossHealth%
	Field BloodTimer#
	Field HeadShot%
	Field Pause%
	
	Field HitBox1[MaxHitBoxes]
	Field HitBox2[MaxHitBoxes]
	Field HitBox3[MaxHitBoxes]
	Field BoneName$[MaxHitBoxes]
	Field HitBoxPosX#[MaxHitBoxes]
	Field HitBoxPosY#[MaxHitBoxes]
	Field HitBoxPosZ#[MaxHitBoxes]
	Field ShootSFX%,ShootSFXCHN%
	Field CurrAnimSeq%
	
	; ~ Multiplayer Variables
	
	Field noDelete%
	Field DistanceTimer#
	Field ClosestPlayer%
	Field DeleteTimer#
	Field IsStuck%
	Field StuckTimer#
	Field GotHit%
	Field KilledBy%
End Type

Type NPCGun
	Field ID%
	Field AnimType%
	Field Name$
	Field obj%
	Field Ammo%
	Field MaxAmmo%
	Field ReloadAmmo%
	Field MaxGunshotSounds%
	Field MaxReloadSounds%
	Field GunType%
	Field Damage%
	Field ShootFrequency#
	Field BulletsPerShot%
End Type

Type NPCAnim
	Field NPCtype%
	Field Animation.Vector3D
	Field AnimName$
End Type

Function GetNPCWeaponAnim$(GunType%)
	
	Select GunType
		Case GUNANIM_PISTOL
			Return "pistol"
		Case GUNANIM_SMG
			Return "smg"
		Case GUNANIM_MP5K
			Return "mp5k"
		Case GUNANIM_RIFLE
			Return "rifle"
		Case GUNANIM_SHOTGUN
			Return "shotgun"
	End Select
	Return "pistol"
	
End Function

Function CreateNPC.NPCs(NPCtype%, x#, y#, z#, model%=-1)
	Local n.NPCs = New NPCs, n2.NPCs, r.Rooms
	Local temp#, i%, diff1, bump1, spec1
	Local sf, b, t1,tex
	
	n\CurrAnimSeq = 0
	
	n\NPCtype% = NPCtype
	n\GravityMult = 1.0
	n\MaxGravity = 0.2
	n\CollRadius = 0.2
	n\FallingPickDistance = 10
	
	Select NPCtype%
		Case NPC_SCP_173
			CreateNPCtype173(n)
		Case NPC_SCP_106
			CreateNPCtype106(n)
		Case NPC_Guard
			If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
				CreateNPCtypeGuard(n)
			Else
				CreateNPCtypeGuardMP(n)
			EndIf
		Case NPC_Vincent
			CreateNPCTypeVincent(n)
		Case NPC_Scientist
			CreateNPCtypeScientist(n)
		Case NPC_NTF
			If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
				CreateNPCtypeNTF(n)
			Else
				CreateNPCtypeNTFMP(n)
			EndIf
		Case NPC_NTF_HDS
			CreateNPCtypeNTFHDSMP(n)
		Case NPC_NTF_HDS_Boss
			CreateNPCtypeHDSBoss(n)
		Case NPC_CI
			CreateNPCtypeCI(n)
		Case NPC_SM4Nn
			;[Block]
			n\NVName = "Unknown"
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.32
			EntityType n\Collider, HIT_PLAYER
			
			n\obj = CopyEntity(NPCModel[Model_SMAN])
			
			temp# = 0.5 / MeshWidth(n\obj)
			ScaleEntity n\obj, temp, temp, temp
			
			n\Speed = 2.0 / 100
			
			MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
			
			n\CollRadius = 0.32
			CopyHitBoxes(n)
			;[End Block]
		Case NPC_Human
			;[Block]
			n\NVName = "Human"
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.32
			EntityType n\Collider, HIT_PLAYER
			
			n\obj = CopyEntity(NPCModel[Model_Class_D])
			
			temp# = 0.5 / MeshWidth(n\obj)
			ScaleEntity n\obj, temp, temp, temp
			
			n\Speed = 2.0 / 100
			
			MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
			
			n\CollRadius = 0.32
			CopyHitBoxes(n)
			;[End Block]
		Case NPC_SCP_372
			;[Block]
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.2
			n\obj = CopyEntity(NPCModel[Model_372])
			
			temp# = 0.35 / MeshWidth(n\obj)
			ScaleEntity n\obj, temp, temp, temp
			;[End Block]
		Case NPC_SCP_513
			;[Block]
			n\NVName = "SCP-513-1"
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.2
			n\obj = CopyEntity(NPCModel[Model_513])
			
			n\obj2 = CopyEntity (n\obj)
			EntityAlpha n\obj2, 0.6
			
			temp# = 1.8 / MeshWidth(n\obj)
			ScaleEntity n\obj, temp, temp, temp
			ScaleEntity n\obj2, temp, temp, temp
			;[End Block]
		Case NPC_SCP_096
			CreateNPCtype096(n)
		Case NPC_SCP_049
			CreateNPCtype049(n)
		Case NPC_Zombie, NPC_Elias
			If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
				CreateNPCtypeZombie(n, model)
			Else
				CreateNPCtypeZombieMP(n, model)
			EndIf
		Case NPC_Zombie_Armed
			If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
				CreateNPCtypeArmedZombie(n, model)
			Else
				CreateNPCtypeArmedZombieMP(n, model)
			EndIf
		Case NPC_SCP_035_Tentacle
			If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
				;[Block]
				n\NVName = "Unidentified"
				
				n\Collider = CreatePivot()
				
				For n2.NPCs = Each NPCs
					If n\NPCtype = n2\NPCtype And n<>n2 Then
						n\obj = CopyEntity (n2\obj)
						Exit
					EndIf
				Next
				
				If n\obj = 0 Then 
					n\obj = CopyEntity(NPCModel[Model_Tentacle])
					ScaleEntity n\obj, 0.065,0.065,0.065
				EndIf
				
				SetAnimTime n\obj, 283
				
				n\HP = 50
				
				CopyHitBoxes(n)
				;[End Block]
			Else
				CreateNPCtypeTentacleMP(n)
			EndIf
		Case NPC_SCP_860
			;[Block]
			n\NVName = "Unidentified"
			
			n\Collider = CreatePivot()
			EntityRadius n\Collider, 0.25
			EntityType n\Collider, HIT_PLAYER
			n\obj = CopyEntity(NPCModel[Model_860])
			
			EntityFX(n\obj, 1)
			
			tex = LoadTexture_Strict("GFX\npcs\860_eyes.png",1+2)
			
			n\obj2 = CreateSprite()
			ScaleSprite(n\obj2, 0.1, 0.1)
			EntityTexture(n\obj2, tex)
			DeleteSingleTextureEntryFromCache tex
			
			EntityFX(n\obj2, 1 + 8)
			EntityBlend(n\obj2, BLEND_ADD)
			SpriteViewMode(n\obj2, 2)
			
			n\Speed = (GetINIFloat("DATA\NPCs.ini", "forestmonster", "speed") / 100.0)
			
			temp# = (GetINIFloat("DATA\NPCs.ini", "forestmonster", "scale") / 20.0)
			ScaleEntity n\obj, temp, temp, temp	
			
			MeshCullBox (n\obj, -MeshWidth(n\obj)*2, -MeshHeight(n\obj)*2, -MeshDepth(n\obj)*2, MeshWidth(n\obj)*2, MeshHeight(n\obj)*4, MeshDepth(n\obj)*4)
			
			n\CollRadius = 0.25
			;[End Block]
		Case NPC_SCP_939
			If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
				CreateNPCtype939(n)
			Else
				CreateNPCtype939MP(n)
			EndIf
		Case NPC_SCP_966
			CreateNPCtype966(n)
		Case NPC_SCP_1048
			;[Block]
			If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
				CreateNPCtype1048a(n)
			Else
				CreateNPCtype1048aMP(n)
			EndIf
			;[End Block]
		Case NPC_Class_D
			;[Block]
			CreateNPCTypeD2(n, model)
			;[End Block]
		Case NPC_SCP_035
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				CreateNPCtype035MP(n)
			Else
				CreateNPCtype035(n)
			EndIf
		Case NPC_SCP_076
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				CreateNPCType076MP(n)
			Else
				CreateNPCtype076(n)
			EndIf
		Case NPC_SCP_682
			CreateNPCtype682(n)
		Case NPC_SCP_457
			If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
				CreateNPCtype457(n)
			Else
				CreateNPCtype457MP(n)
			EndIf
	End Select
	
	If n <> Null Then
		PositionEntity(n\Collider, x, y, z, True)
		PositionEntity(n\obj, x, y, z, True)
		ResetEntity(n\Collider)
	EndIf
	
	If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
		n\ID = 0
		n\ID = FindFreeNPCID()
	Else
		n\ID = mp_I\CurrNPCID
		mp_I\CurrNPCID = mp_I\CurrNPCID + 1
	EndIf
	
	;debuglog ("Created NPC "+n\NVName+" (ID: "+n\ID+")")
	
	NPCSpeedChange(n)
	
	HideNPCHitBoxes(n)
	
	Return n
End Function

Function RemoveNPC(n.NPCs)
	
	If n=Null Then Return
	
	n\BloodTimer = 0
	
	If n\Gun <> Null Then
		n\Gun\obj = FreeEntity_Strict(n\Gun\obj)
		Delete n\Gun
	EndIf
	
	n\obj2 = FreeEntity_Strict(n\obj2)
	n\obj3 = FreeEntity_Strict(n\obj3)
	n\obj4 = FreeEntity_Strict(n\obj4)
	
	If (Not n\SoundChn_IsStream)
		If (n\SoundChn <> 0 And ChannelPlaying(n\SoundChn)) Then
			StopChannel(n\SoundChn)
		EndIf
	Else
		If (n\SoundChn <> 0)
			StopStream_Strict(n\SoundChn)
		EndIf
	EndIf
	
	If (Not n\SoundChn2_IsStream)
		If (n\SoundChn2 <> 0 And ChannelPlaying(n\SoundChn2)) Then
			StopChannel(n\SoundChn2)
		EndIf
	Else
		If (n\SoundChn2 <> 0)
			StopStream_Strict(n\SoundChn2)
		EndIf
	EndIf
	
	If n\Sound<>0 Then FreeSound_Strict n\Sound
	If n\Sound2<>0 Then FreeSound_Strict n\Sound2
	
	n\obj = FreeEntity_Strict(n\obj)
	n\Collider = FreeEntity_Strict(n\Collider)
	
	Delete n
End Function

Function UpdateNPCs()
	CatchErrors("UpdateNPCs")
	Local n.NPCs, n2.NPCs, d.Doors, de.Decals, r.Rooms, eo.ElevatorObj, eo2.ElevatorObj, w.WayPoints
	Local i%, dist#, dist2#, angle#, x#, y#, z#, prevFrame#, PlayerSeeAble%, RN$, temp%, pvt%, TempSound2%
	
	Local target
	
	Local pick%
	
	Local sfxstep%
	
	For n.NPCs = Each NPCs
		CatchErrors("UpdateNPCs: "+n\NVName+", "+n\NPCtype+" ("+n\State[0]+", "+n\State[1]+", "+n\State[2]+")")
		;A variable to determine if the NPC is in the facility or not
		n\InFacility = CheckForNPCInFacility(n)
		
		HideNPCHitBoxes(n)
		
		Select n\NPCtype
			Case NPC_SCP_173
				UpdateNPCtype173(n)
			Case NPC_SCP_106
				UpdateNPCtype106(n)
			Case NPC_SCP_096
				UpdateNPCtype096(n)
			Case NPC_SCP_035
				If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
					UpdateNPCtype035(n)
				EndIf
			Case NPC_SCP_076
				If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
					UpdateNPCtype076(n)
				EndIf
			Case NPC_SCP_049
				UpdateNPCtype049(n)
			Case NPC_Zombie, NPC_Zombie_Armed, NPC_Elias
				If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
					UpdateNPCtypeZombie(n)
				EndIf
			Case NPC_Guard
				If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
					UpdateNPCtypeGuard(n)
				EndIf
			Case NPC_Vincent
				UpdateNPCTypeVincent(n)
			Case NPC_NTF
				If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
					UpdateNPCtypeNTF(n)
				EndIf
			Case NPC_CI
				UpdateNPCtypeCI(n)
			Case NPC_Human, NPC_SM4Nn
				;[Block]
				RotateEntity(n\Collider, 0, EntityYaw(n\Collider), EntityRoll(n\Collider), True)
				
				prevFrame = AnimTime(n\obj)
				
				Select n\State[0]
					Case 0 ;idle
						n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
						Animate2(n\obj, AnimTime(n\obj), 210, 235, 0.1)
					Case 1 ;walking
						If n\State[1] = 1.0
							n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
						Else
							n\CurrSpeed = CurveValue(0.015, n\CurrSpeed, 5.0)
						EndIf
						Animate2(n\obj, AnimTime(n\obj), 236, 260, n\CurrSpeed * 18)
					Case 2 ;running
						n\CurrSpeed = CurveValue(0.03, n\CurrSpeed, 5.0)
						Animate2(n\obj, AnimTime(n\obj), 301, 319, n\CurrSpeed * 18)
				End Select
				
				If n\State[1] <> 2
					If n\State[0] = 1
						If n\CurrSpeed > 0.01 Then
							If prevFrame < 244 And AnimTime(n\obj)=>244 Then
								sfxstep = GetStepSound(n\Collider,n\CollRadius)
								PlaySound2(StepSFX(sfxstep,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
							ElseIf prevFrame < 256 And AnimTime(n\obj)=>256
								sfxstep = GetStepSound(n\Collider,n\CollRadius)
								PlaySound2(StepSFX(sfxstep,0,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
							EndIf
						EndIf
					ElseIf n\State[0] = 2
						If n\CurrSpeed > 0.01 Then
							If prevFrame < 309 And AnimTime(n\obj)=>309
								sfxstep = GetStepSound(n\Collider,n\CollRadius)
								PlaySound2(StepSFX(sfxstep,1,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
							ElseIf prevFrame =< 319 And AnimTime(n\obj)=<301
								sfxstep = GetStepSound(n\Collider,n\CollRadius)
								PlaySound2(StepSFX(sfxstep,1,Rand(0,7)),Camera, n\Collider, 8.0, Rnd(0.3,0.5))
							EndIf
						EndIf
					EndIf
				EndIf
				
				If n\Frame = 19 Lor n\Frame = 60
					n\IsDead = True
				EndIf
				If AnimTime(n\obj)=19 Lor AnimTime(n\obj)=60
					n\IsDead = True
				EndIf
				
				MoveEntity(n\Collider, 0, 0, n\CurrSpeed * FPSfactor)
				
				PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.32, EntityZ(n\Collider))
				
				RotateEntity n\obj, EntityPitch(n\Collider), EntityYaw(n\Collider)-180.0, 0
				;[End Block]
			Case NPC_SCP_513
				;[Block}
				;If KeyHit(48) Then n\Idle = True : n\State[1] = 0
				
				If PlayerRoom\RoomTemplate\Name <> "pocketdimension" Then 
					If n\Idle Then
						HideEntity(n\obj)
						HideEntity(n\obj2)
						If Rand(200)=1 Then
							For w.WayPoints = Each WayPoints
								If w\room<>PlayerRoom Then
									x = Abs(EntityX(Collider)-EntityX(w\obj,True))
									If x>3 And x < 9 Then
										z = Abs(EntityZ(Collider)-EntityZ(w\obj,True))
										If z>3 And z < 9 Then
											PositionEntity(n\Collider, EntityX(w\obj,True), EntityY(w\obj,True), EntityZ(w\obj,True))
											PositionEntity(n\obj, EntityX(w\obj,True), EntityY(w\obj,True), EntityZ(w\obj,True))
											ResetEntity n\Collider
											ShowEntity(n\obj)
											ShowEntity(n\obj2)
											
											n\LastSeen = 0
											
											n\Path[0]=w
											
											n\Idle = False
											n\State[1] = Rand(15,20)*70
											n\State[0] = Max(Rand(-1,2),0)
											n\PrevState = Rand(0,1)
											Exit
										EndIf
									EndIf
								EndIf
							Next
						End If
					Else
						dist = EntityDistanceSquared(Collider, n\Collider)
						
						;use the prev-values to do a "twitching" effect
						n\PrevX = CurveValue(0.0, n\PrevX, 10.0)
						n\PrevZ = CurveValue(0.0, n\PrevZ, 10.0)
						
						If Rand(100)=1 Then
							If Rand(5)=1 Then
								n\PrevX = (EntityX(Collider)-EntityX(n\Collider))*0.9
								n\PrevZ = (EntityZ(Collider)-EntityZ(n\Collider))*0.9
							Else
								n\PrevX = Rnd(0.1,0.5)
								n\PrevZ = Rnd(0.1,0.5)						
							EndIf
						EndIf
						
						temp = Rnd(-1.0,1.0)
						PositionEntity n\obj2, EntityX(n\Collider)+n\PrevX*temp, EntityY(n\Collider) - 0.2 + Sin((MilliSecs()/8-45) Mod 360)*0.05, EntityZ(n\Collider)+n\PrevZ*temp
						RotateEntity n\obj2, 0, EntityYaw(n\obj), 0
						If (Floor(AnimTime(n\obj2))<>Floor(n\Frame)) Then SetAnimTime n\obj2, n\Frame
						
						If n\State[0] = 0 Then
							If n\PrevState=0
								AnimateNPC(n,2,74,0.2)
							Else
								AnimateNPC(n,75,124,0.2)
							EndIf
							;AnimateNPC(n, 229, 299, 0.2)
							
							If n\LastSeen Then 	
								PointEntity n\obj2, Collider
								RotateEntity n\obj, 0, CurveAngle(EntityYaw(n\obj2),EntityYaw(n\obj),40), 0
								If dist < PowTwo(4) Then n\State[0] = Rand(1,2)
							Else
								If dist < PowTwo(6) And Rand(5)=1 Then
									If EntityInView(n\Collider,Camera) Then
										If EntityVisible(Collider, n\Collider) Then
											n\LastSeen = 1
											PlaySound_Strict LoadTempSound("SFX\SCP\513\Bell"+Rand(2,3)+".ogg")
										EndIf
									EndIf
								EndIf								
							EndIf
							
						Else
							If n\Path[0]=Null Then
								
								;move towards a waypoint that is:
								;1. max 8 units away from 513-1
								;2. further away from the player than 513-1's current position 
								For w.WayPoints = Each WayPoints
									x = Abs(EntityX(n\Collider,True)-EntityX(w\obj,True))
									If x < 8.0 And x > 1.0 Then
										z = Abs(EntityZ(n\Collider,True)-EntityZ(w\obj,True))
										If z < 8.0 And z > 1.0 Then
											If EntityDistanceSquared(Collider, w\obj) > dist Then
												n\Path[0]=w
												Exit
											EndIf
										EndIf
									EndIf
								Next
								
								;no suitable path found -> 513-1 simply disappears
								If n\Path[0] = Null Then
									n\Idle = True
									n\State[1] = 0
								EndIf
							Else
								
								If EntityDistanceSquared(n\Collider, n\Path[0]\obj) > PowTwo(1.0) Then
									PointEntity n\obj, n\Path[0]\obj
									RotateEntity n\Collider, CurveAngle(EntityPitch(n\obj),EntityPitch(n\Collider),15.0), CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),15.0), 0, True
									n\CurrSpeed = CurveValue(0.05*Max((7.0-Sqr(dist))/7.0,0.0),n\CurrSpeed,15.0)
									MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor
									If Rand(200)=1 Then MoveEntity n\Collider, 0, 0, 0.5
									RotateEntity n\Collider, 0, EntityYaw(n\Collider), 0, True
								Else
									For i = 0 To 4
										If n\Path[0]\connected[i] <> Null Then
											If EntityDistanceSquared(Collider, n\Path[0]\connected[i]\obj) > dist Then
												
												If n\LastSeen = 0 Then 
													If EntityInView(n\Collider,Camera) Then
														If EntityVisible(Collider, n\Collider) Then
															n\LastSeen = 1
															PlaySound_Strict LoadTempSound("SFX\SCP\513\Bell"+Rand(2,3)+".ogg")
														EndIf
													EndIf
												EndIf
												
												n\Path[0]=n\Path[0]\connected[i]
												Exit
											EndIf
										EndIf
									Next
									
									If n\Path[0]=Null Then n\State[1] = 0
								EndIf
							EndIf
						EndIf
						
						PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.2 + Sin((MilliSecs()/8) Mod 360)*0.1, EntityZ(n\Collider))
						
						Select n\State[0] 
							Case 1
								If n\PrevState=0
									AnimateNPC(n,125,194,n\CurrSpeed*20)
								Else
									AnimateNPC(n,195,264,n\CurrSpeed*20)
								EndIf
								;AnimateNPC(n, 458, 527, n\CurrSpeed*20)
								RotateEntity n\obj, 0, EntityYaw(n\Collider), 0 
							Case 2
								If n\PrevState=0
									AnimateNPC(n,2,74,0.2)
								Else
									AnimateNPC(n,75,124,0.2)
								EndIf
								;AnimateNPC(n, 229, 299, 0.2)
								RotateEntity n\obj, 0, EntityYaw(n\Collider), 0						
						End Select
						
						If n\State[1] > 0 Then
							If dist < PowTwo(4.0) Then n\State[1] = n\State[1]-FPSfactor*4
							n\State[1] = n\State[1]-FPSfactor
						Else
							n\Path[0]=Null
							n\Idle = True
							n\State[1]=0
						EndIf
						
					End If
					
				EndIf
				
				n\DropSpeed = 0
				ResetEntity(n\Collider)						
				;[End Block]
			Case NPC_SCP_372
				;[Block]
				RN$ = PlayerRoom\RoomTemplate\Name
				If RN$ <> "pocketdimension" Then 
					If n\Idle Then
						HideEntity(n\obj)
						If Rand(50) = 1 And (BlinkTimer < -5 And BlinkTimer > -15) Then
							ShowEntity(n\obj)
							angle# = EntityYaw(Collider)+Rnd(-90,90)
							
							dist = Rnd(1.5, 2.0)
							PositionEntity(n\Collider, EntityX(Collider) + Sin(angle) * dist, EntityY(Collider)+0.2, EntityZ(Collider) + Cos(angle) * dist)
							n\Idle = False
							n\State[0] = Rand(20, 60)
							
							If Rand(300)=1 Then PlaySound2(RustleSFX[Rand(0,2)],Camera, n\obj, 8, Rnd(0.0,0.2))
						End If
					Else
						PositionEntity(n\obj, EntityX(n\Collider) + Rnd(-0.005, 0.005), EntityY(n\Collider)+0.3+0.1*Sin(MilliSecs()/2), EntityZ(n\Collider) + Rnd(-0.005, 0.005))
						RotateEntity n\obj, 0, EntityYaw(n\Collider), ((MilliSecs()/5) Mod 360)
						
						AnimateNPC(n, 32, 113, 0.4)
						;Animate2(n\obj, AnimTime(n\obj), 32, 113, 0.4)
						
						If EntityInView(n\obj, Camera) Then
							GiveAchievement(Achv372)
							
							If Rand(30)=1 Then 
								If (Not ChannelPlaying(n\SoundChn)) Then
									If EntityVisible(Camera, n\obj) Then 
										n\SoundChn = PlaySound2(RustleSFX[Rand(0,2)],Camera, n\obj, 8, 0.3)
									EndIf
								EndIf
							EndIf
							
							temp = CreatePivot()
							PositionEntity temp, EntityX(Collider), EntityY(Collider), EntityZ(Collider)
							PointEntity temp, n\Collider
							
							angle =  WrapAngle(EntityYaw(Collider)-EntityYaw(temp))
							If angle < 180 Then
								RotateEntity n\Collider, 0, EntityYaw(Collider)-80, 0		
							Else
								RotateEntity n\Collider, 0, EntityYaw(Collider)+80, 0
							EndIf
							temp = FreeEntity_Strict(temp)
							
							MoveEntity n\Collider, 0, 0, 0.03*FPSfactor
							
							n\State[0] = n\State[0]-FPSfactor
						EndIf
						n\State[0]=n\State[0]-(FPSfactor/80.0)
						If n\State[0] <= 0 Then n\Idle = True	
					End If
					
				EndIf
				
				n\DropSpeed = 0
				ResetEntity(n\Collider)						
				;[End Block]
			Case NPC_SCP_035_Tentacle
				;[Block]
				dist = EntityDistanceSquared(n\Collider,Collider)
				
				If n\HP <= 0 Then
					n\IsDead = True
					SetNPCFrame(n, 285)
				Else
					If dist < PowTwo(HideDistance)
					
						Select n\State[0] 
							Case 0 ;spawn
							
								If n\Frame>283 Then
									HeartBeatVolume = Max(CurveValue(1.0, HeartBeatVolume, 50),HeartBeatVolume)
									HeartBeatRate = Max(CurveValue(130, HeartBeatRate, 100),HeartBeatRate)
								
									PointEntity n\obj, Collider
									RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),25.0), 0
								
									AnimateNPC(n, 283, 389, 0.3, False)
									;Animate2(n\obj, AnimTime(n\obj), 283, 389, 0.3, False)
								
									If n\Frame>388 Then n\State[0] = 1
								Else
									If dist < PowTwo(2.5) Then 
										SetNPCFrame(n, 284)
										n\Sound2 = LoadSound_Strict("SFX\Room\035Chamber\TentacleSpawn.ogg")
										PlaySound_Strict(n\Sound2)
									EndIf
								EndIf
								;spawn 283,389
								;attack 2, 32
								;idle 33, 174
							Case 1 ;idle
								If dist < PowTwo(1.8) Then 
									If Abs(DeltaYaw(n\Collider, Collider))<20 Then 
										n\State[0] = 2
										If n\Sound<>0 Then FreeSound_Strict n\Sound : n\Sound = 0 
										If n\Sound2<>0 Then FreeSound_Strict n\Sound2 : n\Sound2 = 0 
									
									EndIf
								EndIf
							
								PointEntity n\obj, Collider
								RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),25.0), 0
							
								AnimateNPC(n, 33, 174, 0.3, True)
								;Animate2(n\obj, AnimTime(n\obj), 33, 174, 0.3, True)
							Case 2
							
								;finish the idle animation before playing the attack animation
								If n\Frame>33 And n\Frame<174 Then
									AnimateNPC(n, 33, 174, 2.0, False)
									;Animate2(n\obj, AnimTime(n\obj), 33, 174, 2.0, False)
								Else
									PointEntity n\obj, Collider
									RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),10.0), 0							
								
									If n\Frame>33 Then 
										;SetAnimTime(n\obj,2)
										n\Frame = 2
										n\Sound = LoadSound_Strict("SFX\Room\035Chamber\TentacleAttack"+Rand(1,2)+".ogg")
										PlaySound_Strict(n\Sound)
									EndIf
									AnimateNPC(n, 2, 32, 0.3, False)
									;Animate2(n\obj, AnimTime(n\obj), 2, 32, 0.3, False)
								
									If n\Frame>=5 And n\Frame<6 Then
										If dist < PowTwo(1.8) Then
											If Abs(DeltaYaw(n\Collider, Collider))<20 Then 
												If Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 7) = "hazmat" Then
													;Injuries = Injuries+Rnd(0.5)
													If (Not GodMode) Then
														DamageSPPlayer(Rnd(10.0))
													EndIf
													PlaySound_Strict(LoadTempSound("SFX\General\BodyFall.ogg"))
												Else
													BlurTimer = 100
													;Injuries = Injuries+Rnd(1.0,1.5)
													If (Not GodMode) Then
														DamageSPPlayer(Rnd(25.0,50.0))
													EndIf
													PlaySound_Strict DamageSFX[Rand(3,4)]
												
													If (Not IsSPPlayerAlive()) Then
														m_msg\DeathTxt = Chr(34)+"We will need more than the regular cleaning team to care of this. "
														m_msg\DeathTxt = m_msg\DeathTxt + "Two large and highly active tentacle-like appendages seem "
														m_msg\DeathTxt = m_msg\DeathTxt + "to have formed inside the chamber. Their level of aggression is "
														m_msg\DeathTxt = m_msg\DeathTxt + "unlike anything we've seen before - it looks like they have "
														m_msg\DeathTxt = m_msg\DeathTxt + "beaten some unfortunate Class D to death at some point during the breach."+Chr(34)
														Kill()
													EndIf
												EndIf
											
											EndIf
										EndIf
									
										n\Frame = 6
										;SetAnimTime(n\obj, 6)
									ElseIf n\Frame=32
										n\State[0] = 1
										n\Frame = 173
										;SetAnimTime(n\obj, 173)
									EndIf
								EndIf
							
						End Select
					
					EndIf
				
					PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider), EntityZ(n\Collider))
					RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider)-180, EntityRoll(n\Collider), True
				
					n\DropSpeed = 0
				
					ResetEntity n\Collider
				EndIf
				;[End Block]
			Case NPC_SCP_860
				;[Block]
				If PlayerRoom\RoomTemplate\Name = "room860" Then
					Local fr.Forest=PlayerRoom\fr;Object.Forest(e\room\Objects[1])
					
					If n\State[0] <> 0 Then
						dist = EntityDistance(Collider,n\Collider)
					EndIf
					
					If ForestNPC<>0
						If ForestNPCData[2]=1
							ShowEntity ForestNPC
							If n\State[0]<>1
								If (BlinkTimer<-8 And BlinkTimer >-12) Lor (Not EntityInView(ForestNPC,Camera))
									ForestNPCData[2]=0
									HideEntity ForestNPC
								EndIf
							EndIf
						Else
							HideEntity ForestNPC
						EndIf
					EndIf
					
					Select n\State[0]
						Case 0 ;idle (hidden)
							
							HideEntity n\Collider
							HideEntity n\obj
							HideEntity n\obj2
							
							n\State[1] = 0
							PositionEntity(n\Collider, 0, -100, 0)
						Case 1 ;appears briefly behind the trees
							n\DropSpeed = 0
							
							If EntityY(n\Collider)<= -100 Then
								;transform the position of the player to the local coordinates of the forest
								TFormPoint(EntityX(Collider),EntityY(Collider),EntityZ(Collider),0,fr\Forest_Pivot)
								
								;calculate the indices of the forest cell the player is in
								x = Floor((TFormedX()+6.0)/12.0)
								z = Floor((TFormedZ()+6.0)/12.0)
								
								;step through nearby cells
								For x2 = Max(x-1,1) To Min(x+1,gridsize) Step 2
									For z2 = Max(z-1,1) To Min(z+1,gridsize) Step 2
										;choose an empty cell (not on the path)
										If fr\grid[(z2*gridsize)+x2]=0 Then
											;spawn the monster between the empty cell and the cell the player is in
											TFormPoint(((x2+x)/2)*12.0,0,((z2+z)/2)*12.0,fr\Forest_Pivot,0)
											
											;in view -> nope, keep searching for a more suitable cell
											If EntityInView(n\Collider, Camera) Then
												PositionEntity n\Collider, 0, -110, 0
												;debuglog("spawned monster in view -> hide")
											Else ; not in view -> all good
												;debuglog("spawned monster successfully")
												
												PositionEntity n\Collider, TFormedX(), EntityY(fr\Forest_Pivot,True)+2.3, TFormedZ()
												
												x2 = gridsize
												Exit												
											EndIf
										EndIf
									Next
								Next
								
								If EntityY(n\Collider)> -100 Then
									PlaySound2(Step2SFX[Rand(3,5)], Camera, n\Collider, 15.0, 0.5)
									
									If ForestNPCData[2]<>1 Then ForestNPCData[2]=0
									
									Select Rand(3)
										Case 1
											PointEntity n\Collider, Collider
											n\Frame = 2
											;SetAnimTime(n\obj, 2)
										Case 2
											PointEntity n\Collider, Collider
											n\Frame = 201
											;SetAnimTime(n\obj, 201)
										Case 3
											PointEntity n\Collider, Collider
											TurnEntity n\Collider, 0, 90, 0
											n\Frame = 299
											;SetAnimTime(n\obj, 299)
									End Select
									
									n\State[1] = 0
								EndIf
							Else
								
								ShowEntity n\obj
								ShowEntity n\Collider
								
								PositionEntity n\Collider, EntityX(n\Collider), EntityY(fr\Forest_Pivot,True)+2.3, EntityZ(n\Collider)
								
								If ForestNPC<>0
									If ForestNPCData[2]=0
										Local docchance% = 0
										Local docamount% = 0
										For i = 0 To MaxItemAmount-1
											If Inventory[i]<>Null
												Local docname$ = Inventory[i]\itemtemplate\name
												If docname = "Log #1" Lor docname = "Log #2" Lor docname = "Log #3"
													;860,850,830,800
													docamount% = docamount% + 1
													docchance = docchance + 10*docamount%
												EndIf
											EndIf
										Next
										
										If Rand(1,860-docchance)=1
											ShowEntity ForestNPC
											ForestNPCData[2]=1
											If Rand(2)=1
												ForestNPCData[0]=0
											Else
												ForestNPCData[0]=2
											EndIf
											ForestNPCData[1]=0
											PositionEntity ForestNPC,EntityX(n\Collider),EntityY(n\Collider)+0.5,EntityZ(n\Collider)
											RotateEntity ForestNPC,0,EntityYaw(n\Collider),0
											MoveEntity ForestNPC,0.75,0,0
											RotateEntity ForestNPC,0,0,0
											EntityTexture ForestNPC,ForestNPCTex,ForestNPCData[0]
										Else
											ForestNPCData[2]=2
										EndIf
									ElseIf ForestNPCData[2]=1
										If ForestNPCData[1]=0.0
											If Rand(200)=1
												ForestNPCData[1]=FPSfactor
												EntityTexture ForestNPC,ForestNPCTex,ForestNPCData[0]+1
											EndIf
										ElseIf ForestNPCData[1]>0.0 And ForestNPCData[1]<5.0
											ForestNPCData[1]=Min(ForestNPCData[1]+FPSfactor,5.0)
										Else
											ForestNPCData[1]=0
											EntityTexture ForestNPC,ForestNPCTex,ForestNPCData[0]
										EndIf
									EndIf
								EndIf
								
								If n\State[1] = 0 Then ;don't start moving until the player is looking
									If EntityInView(n\Collider, Camera) Then 
										n\State[1] = 1
										If Rand(8)=1 Then
											PlaySound2(LoadTempSound("SFX\SCP\860\Cancer"+Rand(0,2)+".ogg"), Camera, n\Collider, 20.0)
										EndIf										
									EndIf
								Else
									If n\Frame<=199 Then
										AnimateNPC(n, 2, 199, 0.5,False)
										If n\Frame=199 Then n\Frame = 298 : PlaySound2(Step2SFX[Rand(3,5)], Camera, n\Collider, 15.0)
									ElseIf n\Frame <= 297
										PointEntity n\Collider, Collider
										
										AnimateNPC(n, 200, 297, 0.5, False)
										If n\Frame=297 Then n\Frame=298 : PlaySound2(Step2SFX[Rand(3,5)], Camera, n\Collider, 15.0)
									Else
										angle = CurveAngle(point_direction(EntityX(n\Collider),EntityZ(n\Collider),EntityX(Collider),EntityZ(Collider)),EntityYaw(n\Collider)+90,20.0)
										
										RotateEntity n\Collider, 0, angle-90, 0, True
										
										AnimateNPC(n, 298, 316, n\CurrSpeed*10)
										
										;Animate2(n\obj, AnimTime(n\obj), 298, 316, n\CurrSpeed*10)
										
										n\CurrSpeed = CurveValue(n\Speed, n\CurrSpeed, 10.0)
										MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor
										
										If dist>15.0 Then
											PositionEntity n\Collider, 0,-110,0
											n\State[0] = 0
											n\State[1] = 0
										EndIf
									EndIf									
								EndIf
								
							EndIf
							
							ResetEntity n\Collider
						Case 2 ;appears on the path and starts to walk towards the player
							ShowEntity n\obj
							ShowEntity n\Collider
							
							prevFrame = n\Frame
							
							If EntityY(n\Collider)<= -100 Then
								;transform the position of the player to the local coordinates of the forest
								TFormPoint(EntityX(Collider),EntityY(Collider),EntityZ(Collider),0,fr\Forest_Pivot)
								
								;calculate the indices of the forest cell the player is in
								x = Floor((TFormedX()+6.0)/12.0)
								z = Floor((TFormedZ()+6.0)/12.0)
								
								For x2 = Max(x-1,1) To Min(x+1,gridsize)
									For z2 = Max(z-1,1) To Min(z+1,gridsize)
										;find a nearby cell that's on the path and NOT the cell the player is in
										If fr\grid[(z2*gridsize)+x2]>0 And (x2<>x Lor z2<>z) And (x2=x Lor z2=z) Then
											
											;transform the position of the cell back to world coordinates
											TFormPoint(x2*12.0, 0,z2*12.0, fr\Forest_Pivot,0)
											
											PositionEntity n\Collider, TFormedX(), EntityY(fr\Forest_Pivot,True)+1.0,TFormedZ()
											
											;debuglog(TFormedX()+", "+TFormedZ())
											
											If EntityInView(n\Collider, Camera) Then
												BlinkTimer=-10
											Else
												x2 = gridsize
												Exit
											EndIf
										EndIf
									Next
								Next
							Else
								angle = CurveAngle(Find860Angle(n, fr),EntityYaw(n\Collider)+90,80.0)
								
								RotateEntity n\Collider, 0, angle-90, 0, True
								
								n\CurrSpeed = CurveValue(n\Speed*0.3, n\CurrSpeed, 50.0)
								MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor
								
								AnimateNPC(n, 494, 569, n\CurrSpeed*25)
								
								If n\State[1] = 0 Then
									If dist<8.0 Then
										If EntityInView(n\Collider,Camera) Then
											PlaySound_Strict LoadTempSound("SFX\SCP\860\Chase"+Rand(1,2)+".ogg")
											
											PlaySound2(LoadTempSound("SFX\SCP\860\Cancer"+Rand(0,2)+".ogg"), Camera, n\Collider)	
											n\State[1] = 1
										EndIf										
									EndIf
								EndIf
								
								If CurrSpeed > 0.03 Then ;the player is running
									n\State[2] = n\State[2] + FPSfactor
									If Rnd(5000)<n\State[2] Then
										temp = True
										If n\SoundChn <> 0 Then
											If ChannelPlaying (n\SoundChn) Then temp = False
										EndIf
										If temp Then
											n\SoundChn = PlaySound2(LoadTempSound("SFX\SCP\860\Cancer"+Rand(0,2)+".ogg"), Camera, n\Collider)
										EndIf
									EndIf
								Else
									n\State[2] = Max(n\State[2] - FPSfactor,0)
								EndIf
								
								If dist<4.5 Lor n\State[2] > Rnd(200,250) Then
									n\SoundChn = PlaySound2(LoadTempSound("SFX\SCP\860\Cancer"+Rand(3,5)+".ogg"), Camera, n\Collider)
									n\State[0] = 3
								EndIf
								
								If dist > 20.0 Then
									n\State[0] = 0
									n\State[1] = 0
									PositionEntity n\Collider, 0,-110,0
								EndIf
							EndIf
							
							;535, 568
							If (prevFrame < 533 And n\Frame=>533) Lor (prevFrame > 568 And n\Frame<2) Then
								PlaySound2(Step2SFX[Rand(3,5)], Camera, n\Collider, 15.0, 0.6)
							EndIf
							
						Case 3 ;runs towards the player and attacks
							ShowEntity n\obj
							ShowEntity n\Collider
							
							prevFrame = n\Frame
							
							angle = CurveAngle(Find860Angle(n, fr),EntityYaw(n\Collider)+90,40.0)
							
							RotateEntity n\Collider, 0, angle-90, 0, True
							
							If n\Sound = 0 Then n\Sound = LoadSound_Strict("SFX\General\Slash1.ogg")
							If n\Sound2 = 0 Then n\Sound2 = LoadSound_Strict("SFX\General\Slash2.ogg")
							
							;if close enough to attack OR already attacking, play the attack anim
							If (dist<1.1 Lor (n\Frame>451 And n\Frame<493) Lor KillTimer < 0) Then
								m_msg\DeathTxt = ""
								
								n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 5.0)
								
								AnimateNPC(n, 451,493, 0.5, False)
								
								;Animate2(n\obj, AnimTime(n\obj), 451,493, 0.5, False)
								If (prevFrame < 461 And n\Frame=>461) Then 
									If KillTimer => 0 Then Kill() : KillAnim = 0
									PlaySound_Strict(n\Sound)
								EndIf
								If (prevFrame < 476 And n\Frame=>476) Then PlaySound_Strict(n\Sound2)
								If (prevFrame < 486 And n\Frame=>486) Then PlaySound_Strict(n\Sound2)
							Else
								n\CurrSpeed = CurveValue(n\Speed*0.8, n\CurrSpeed, 10.0)
								
								AnimateNPC(n, 298, 316, n\CurrSpeed*10)
								;Animate2(n\obj, AnimTime(n\obj), 298, 316, n\CurrSpeed*10)
								
								If (prevFrame < 307 And n\Frame=>307) Then
									PlaySound2(Step2SFX[Rand(3,5)], Camera, n\Collider, 10.0)
								EndIf
							EndIf
							
							MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor
					End Select
					
					If n\State[0] <> 0 Then
						RotateEntity n\Collider, 0, EntityYaw(n\Collider), 0, True	
						
						PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider)-0.1, EntityZ(n\Collider))
						RotateEntity n\obj, EntityPitch(n\Collider)-90, EntityYaw(n\Collider), EntityRoll(n\Collider), True
						
						If dist > 8.0 Then
							ShowEntity n\obj2
							EntityAlpha n\obj2, Min(dist-8.0,1.0)
							
							PositionEntity(n\obj2, EntityX(n\obj), EntityY(n\obj) , EntityZ(n\obj))
							RotateEntity(n\obj2, 0, EntityYaw(n\Collider) - 180, 0)
							MoveEntity(n\obj2, 0, 30.0*0.025, -33.0*0.025)
							
							;render distance is set to 8.5 inside the forest,
							;so we need to cheat a bit to make the eyes visible if they're further than that
							pvt = CreatePivot()
							PositionEntity pvt, EntityX(Camera),EntityY(Camera),EntityZ(Camera)
							PointEntity pvt, n\obj2
							MoveEntity pvt, 0,0,8.0
							PositionEntity n\obj2, EntityX(pvt),EntityY(pvt),EntityZ(pvt)
							pvt = FreeEntity_Strict(pvt)
						Else
							HideEntity n\obj2
						EndIf
					EndIf
				EndIf
				;[End Block] 
			Case NPC_SCP_939
				UpdateNPCtype939(n)
			Case NPC_SCP_966
				UpdateNPCtype966(n)
			Case NPC_SCP_1048
				UpdateNPCtype1048a(n)
			Case NPC_Class_D
				UpdateNPCTypeD2(n)
			Case NPC_SCP_682
				UpdateNPCtype682(n)
			Case NPC_SCP_457
				UpdateNPCtype457(n)
		End Select
		
		If n = Null Then Return
		
		Local hb.HitBox
		
		If hb.HitBox <> Null Then
			Local headbonename$ = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCtype, "head_bonename"))
			Local p.Particles
			
			If n\HeadShot And n <> Null Then
				;debuglog "bleeding"
				p.Particles = CreateParticle(EntityX(FindChild(n\obj,headbonename$),True),EntityY(FindChild(n\obj,headbonename$),True),EntityZ(FindChild(n\obj,headbonename$),True),5,0.01,-0.005,150)
			EndIf
		EndIf
			
			
		If n\Boss <> Null Then
			If n = n\Boss Then
				If n\IsDead Then
					n\Boss = Null
				EndIf
			EndIf
		EndIf
		
		If n\IsDead
			EntityType n\Collider,HIT_DEAD
		EndIf
		
		If DistanceSquared(EntityX(Collider),EntityX(n\Collider),EntityZ(Collider),EntityZ(n\Collider))<PowTwo(HideDistance*0.7) Then
			If n\InFacility = InFacility
				TranslateEntity n\Collider, 0, n\DropSpeed, 0
				
				Local CollidedFloor% = False
				For i% = 1 To CountCollisions(n\Collider)
					If CollisionY(n\Collider, i) < EntityY(n\Collider) - 0.01 Then CollidedFloor = True : Exit
				Next
				
				If CollidedFloor = True Then
					n\DropSpeed# = 0
				Else
					If ShouldEntitiesFall
						Local UpdateGravity% = False
						Local MaxX#,MinX#,MaxZ#,MinZ#
						If n\InFacility=1
							For e.Events = Each Events
								If e\EventName = "cont_860" Then
									If e\eventstate[0] = 1.0
										UpdateGravity = True
										Exit
									EndIf
								EndIf
							Next
							If (Not UpdateGravity)
								For r.Rooms = Each Rooms
									If r\MaxX<>0 Lor r\MinX<>0 Lor r\MaxZ<>0 Lor r\MinZ<>0
										MaxX# = r\MaxX
										MinX# = r\MinX
										MaxZ# = r\MaxZ
										MinZ# = r\MinZ
									Else
										MaxX# = 4.0
										MinX# = 0.0
										MaxZ# = 4.0
										MinZ# = 0.0
									EndIf
									If Abs(EntityX(n\Collider)-EntityX(r\obj))<=Abs(MaxX-MinX)
										If Abs(EntityZ(n\Collider)-EntityZ(r\obj))<=Abs(MaxZ-MinZ)
											If r=PlayerRoom
												UpdateGravity = True
												Exit
											EndIf
											If IsRoomAdjacent(PlayerRoom,r)
												UpdateGravity = True
												Exit
											EndIf
											For i=0 To 3
												If (IsRoomAdjacent(PlayerRoom\Adjacent[i],r))
													UpdateGravity = True
													Exit
												EndIf
											Next
										EndIf
									EndIf
								Next
							EndIf
						Else
							UpdateGravity = True
						EndIf
						If UpdateGravity
							n\DropSpeed# = Max(n\DropSpeed - 0.005*FPSfactor*n\GravityMult,-n\MaxGravity)
						Else
							If n\FallingPickDistance>0
								n\DropSpeed = 0.0
							Else
								n\DropSpeed# = Max(n\DropSpeed - 0.005*FPSfactor*n\GravityMult,-n\MaxGravity)
							EndIf
						EndIf
					Else
						n\DropSpeed# = 0.0
					EndIf
				EndIf
			Else
				n\DropSpeed = 0
			EndIf
		Else
			n\DropSpeed = 0
		EndIf
		
		ShowNPCHitBoxes(n)
		
		CatchErrors("Uncaught (UpdateNPCs)")
		
	Next
	
End Function

Function TeleportCloser(n.NPCs)
	Local closestDist# = 0
	Local closestWaypoint.WayPoints
	Local w.WayPoints
	
	Local xtemp#, ztemp#
	
	For w.WayPoints = Each WayPoints
		If w\door = Null Then
			xtemp = Abs(EntityX(w\obj,True)-EntityX(n\Collider,True))
			If xtemp < 10.0 And xtemp > 1.0 Then 
				ztemp = Abs(EntityZ(w\obj,True)-EntityZ(n\Collider,True))
				If ztemp < 10.0 And ztemp > 1.0 Then
					If (EntityDistanceSquared(Collider, w\obj)>PowTwo(16-(8*SelectedDifficulty\AggressiveNPCs))) Then
						;teleports to the nearby waypoint that takes it closest to the player
						Local newDist# = EntityDistanceSquared(Collider, w\obj)
						If (newDist < closestDist Lor closestWaypoint = Null) Then
							closestDist = newDist	
							closestWaypoint = w
						EndIf						
					EndIf
				EndIf
			EndIf
		EndIf
	Next
	
	Local shouldTeleport% = False
	If (closestWaypoint<>Null) Then
		If n\InFacility <> 1 Lor SelectedDifficulty\AggressiveNPCs Then
			shouldTeleport = True
		ElseIf EntityY(closestWaypoint\obj,True)<=7.0 And EntityY(closestWaypoint\obj,True)>=-10.0 Then
			shouldTeleport = True
		EndIf
		
		If shouldTeleport Then
			PositionEntity n\Collider, EntityX(closestWaypoint\obj,True), EntityY(closestWaypoint\obj,True)+0.15, EntityZ(closestWaypoint\obj,True), True
			ResetEntity n\Collider
			n\PathStatus = 0
			n\PathTimer# = 0.0
			n\PathLocation = 0
		EndIf
	EndIf
	
End Function

Function OtherNPCSeesMeNPC%(me.NPCs,other.NPCs)
	If other\BlinkTimer<=0.0 Then Return False
	
	If EntityDistanceSquared(other\Collider,me\Collider)<PowTwo(6.0) Then
		If Abs(DeltaYaw(other\Collider,me\Collider))<60.0 Then
			Return True
		EndIf
		If EntityDistanceSquared(other\Collider,me\Collider)<PowTwo(1.5) Then
			Return True
		EndIf
	EndIf
	Return False
End Function

Function MeNPCSeesPlayer%(me.NPCs,disablesoundoncrouch%=False,disablesounddetect%=False)
	;Return values:
		;False (=0): Player is not detected anyhow
		;True (=1): Player is detected by vision
		;2: Player is detected by emitting a sound
		;3: Player is detected by a camera (only for MTF Units!)
		;4: Player is detected through glass
	
	If NoTarget Then Return False
	
	PlayerDetected = False
	If (Not PlayerDetected) Lor me\NPCtype <> NPC_NTF
		If me\BlinkTimer<=0.0 Then Return False
		If EntityDistanceSquared(Collider,me\Collider)>PowTwo(8.0-CrouchState+PlayerSoundVolume) Then Return False
		
		;spots the player if he's either in view or making a loud sound
		If PlayerSoundVolume>1.0
			If (Abs(DeltaYaw(me\Collider,Collider))>60.0) And EntityVisible(me\Collider,Collider)
				Return 1
			ElseIf (Not EntityVisible(me\Collider,Collider))
				If (disablesoundoncrouch% And Crouch%) Lor disablesounddetect%
					Return False
				Else
					Return 2
				EndIf
			EndIf
		Else
			If (Abs(DeltaYaw(me\Collider,Collider))>60.0) Then Return False
		EndIf
		Return EntityVisible(me\Collider,Collider)
	Else
		If EntityDistanceSquared(Collider,me\Collider)>PowTwo(8.0-CrouchState+PlayerSoundVolume) Then Return 3
		If EntityVisible(me\Collider, Camera) Then Return True
		
		;spots the player if he's either in view or making a loud sound
		If PlayerSoundVolume>1.0 Then Return 2
		Return 3
	EndIf
	
End Function

Function PlayMTFSound(sound%, n.NPCs, allowOverlap=False)
	Local n2.NPCs
	Local snd%
	
	For n2 = Each NPCs
		If n <> Null Then
			If n2 <> Null And allowOverlap = False Then
				If n2\NPCtype = n\NPCtype Then
					If n\ID > n2\ID Then
						snd = PlaySound_Strict(sound)
						Exit
					EndIf
				EndIf
			Else
				snd = PlaySound_Strict(sound)
				Exit
			EndIf
		EndIf
	Next
	
	ChannelVolume(snd, opt\VoiceVol*opt\MasterVol)
End Function

Function MoveToPocketDimension()
	Local r.Rooms
	
	For r.Rooms = Each Rooms
		If r\RoomTemplate\Name = "pocketdimension" Then
			FallTimer = 0
			UpdateDoors()
			UpdateRooms()
			ShowEntity Collider
			PlaySound_Strict(Use914SFX)
			PlaySound_Strict(OldManSFX[5])
			PositionEntity(Collider, EntityX(r\obj),0.8,EntityZ(r\obj))
			DropSpeed = 0
			ResetEntity Collider
			
			IsCutscene = True
			BlinkTimer = -10
			IsCutscene = False
			
			;Injuries = Injuries+0.5
			DamageSPPlayer(10.0, True)
			
			PlayerRoom = r
			
			Return
		EndIf
	Next		
End Function

Function FindFreeNPCID%()
	Local id% = 1
	While (True)
		Local taken% = False
		For n2.NPCs = Each NPCs
			If n2\ID = id Then
				taken = True
				Exit
			EndIf
		Next
		If (Not taken) Then
			Return id
		EndIf
		id = id + 1
	Wend
End Function

Function ForceSetNPCID(n.NPCs, newID%)
	n\ID = newID
	
	For n2.NPCs = Each NPCs
		If n2 <> n And n2\ID = newID Then
			n2\id = FindFreeNPCID()
		EndIf
	Next
End Function

Function Find860Angle(n.NPCs, fr.Forest)
	TFormPoint(EntityX(Collider),EntityY(Collider),EntityZ(Collider),0,fr\Forest_Pivot)
	Local playerx = Floor((TFormedX()+6.0)/12.0)
	Local playerz = Floor((TFormedZ()+6.0)/12.0)
	
	TFormPoint(EntityX(n\Collider),EntityY(n\Collider),EntityZ(n\Collider),0,fr\Forest_Pivot)
	Local x# = (TFormedX()+6.0)/12.0
	Local z# = (TFormedZ()+6.0)/12.0
	
	Local xt = Floor(x), zt = Floor(z)
	
	Local x2,z2
	If xt<>playerx Lor zt<>playerz Then ;the monster is not on the same tile as the player
		For x2 = Max(xt-1,0) To Min(xt+1,gridsize-1)
			For z2 = Max(zt-1,0) To Min(zt+1,gridsize-1)
				If fr\grid[(z2*gridsize)+x2]>0 And (x2<>xt Lor z2<>zt) And (x2=xt Lor z2=zt) Then
					
					;tile (x2,z2) is closer to the player than the monsters current tile
					If (Abs(playerx-x2)+Abs(playerz-z2))<(Abs(playerx-xt)+Abs(playerz-zt)) Then
						;calculate the position of the tile in world coordinates
						TFormPoint(x2*12.0,0,z2*12.0,fr\Forest_Pivot,0)
						
						Return point_direction(EntityX(n\Collider),EntityZ(n\Collider),TFormedX(),TFormedZ())+180
					EndIf
					
				EndIf
			Next
		Next
	Else
		Return point_direction(EntityX(n\Collider),EntityZ(n\Collider),EntityX(Collider),EntityZ(Collider))+180
	EndIf		
End Function

Function Console_SpawnNPC(c_input$, c_state$ = "", model=-1)
	Local n.NPCs
	Local consoleMSG$
	
	If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
		Collider = Players[mp_I\PlayerID]\Collider
	EndIf
	
	Select c_input$ 
			
		Case "049", "scp049", "scp-049"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				CreateConsoleMsg("SCP-049 cannot be spawned in Multiplayer. Sorry!", 255, 0, 0)
			Else
				n.NPCs = CreateNPC(NPC_SCP_049, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
				n\State[0] = 1
				consoleMSG = "SCP-049 spawned."
			EndIf	
			
		Case "049-2", "0492", "scp-049-2", "scp049-2", "049zombie"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER And mp_I\Gamemode <> Null And mp_I\Gamemode\ID = Gamemode_Deathmatch Then
				CreateConsoleMsg("SCP-049-2 cannot be spawned in Deathmatch. Sorry!", 255, 0, 0)
			Else	
				n.NPCs = CreateNPC(NPC_Zombie_Armed, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider),model)
				n\State[0] = 1
				consoleMSG = "SCP-049-2 spawned."
			EndIf
			
		Case "049-3", "0493", "scp-049-3", "scp049-3", "049zombie3"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER And mp_I\Gamemode <> Null And mp_I\Gamemode\ID = Gamemode_Deathmatch Then
				CreateConsoleMsg("SCP-049-3 cannot be spawned in Deathmatch. Sorry!", 255, 0, 0)
			Else	
				n.NPCs = CreateNPC(NPC_Zombie, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider),model)
				n\State[0] = 1
				consoleMSG = "SCP-049-3 spawned."
			EndIf
			
		Case "096", "scp096", "scp-096"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				CreateConsoleMsg("SCP-096 cannot be spawned in Multiplayer. Sorry!", 255, 0, 0)
			Else
				n.NPCs = CreateNPC(NPC_SCP_096, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
				n\State[0] = 5
				If (Curr096 = Null) Then Curr096 = n
				consoleMSG = "SCP-096 spawned."
			EndIf	
			
		Case "106", "scp106", "scp-106", "larry"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				CreateConsoleMsg("SCP-106 cannot be spawned in Multiplayer. Sorry!", 255, 0, 0)
			Else
				n.NPCs = CreateNPC(NPC_SCP_106, EntityX(Collider), EntityY(Collider) - 0.5, EntityZ(Collider))
				n\State[0] = -1
				consoleMSG = "SCP-106 spawned."
			EndIf	
			
		Case "173", "scp173", "scp-173", "statue"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				CreateConsoleMsg("SCP-173 cannot be spawned in Multiplayer. Sorry!", 255, 0, 0)
			Else
				n.NPCs = CreateNPC(NPC_SCP_173, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
				Curr173 = n
				If (Curr173\Idle = SCP173_DISABLED) Then Curr173\Idle = SCP173_ACTIVE
				consoleMSG = "SCP-173 spawned."
			EndIf
			
		Case "372", "scp372", "scp-372"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				CreateConsoleMsg("SCP-372 cannot be spawned in Multiplayer. Sorry!", 255, 0, 0)
			Else
				n.NPCs = CreateNPC(NPC_SCP_372, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
				consoleMSG = "SCP-372 spawned."
			EndIf	
			
		Case "513-1", "5131", "scp513-1", "scp-513-1"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				CreateConsoleMsg("SCP-513-1 cannot be spawned in Multiplayer. Sorry!", 255, 0, 0)
			Else
				n.NPCs = CreateNPC(NPC_SCP_513, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
				consoleMSG = "SCP-513-1 spawned."
			EndIf	
			
		Case "860-2", "8602", "scp860-2", "scp-860-2"
			CreateConsoleMsg("SCP-860-2 cannot be spawned with the console. Sorry!", 255, 0, 0)
			
		Case "939", "scp939", "scp-939"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER And mp_I\Gamemode <> Null And mp_I\Gamemode\ID = Gamemode_Deathmatch Then
				CreateConsoleMsg("SCP-939 instances cannot be spawned in Deathmatch. Sorry!", 255, 0, 0)
			Else
				n.NPCs = CreateNPC(NPC_SCP_939, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
				consoleMSG = "SCP-939 instance spawned."
			EndIf
			
		Case "966", "scp966", "scp-966"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				CreateConsoleMsg("SCP-966 cannot be spawned in Multiplayer. Sorry!", 255, 0, 0)
			Else
				n.NPCs = CreateNPC(NPC_SCP_966, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
				consoleMSG = "SCP-966 instance spawned."
			EndIf	
			
		Case "1048-a", "scp1048-a", "scp-1048-a", "scp1048a", "scp-1048a", "1048a"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER And mp_I\Gamemode <> Null And mp_I\Gamemode\ID = Gamemode_Deathmatch Then
				CreateConsoleMsg("SCP-1048-A cannot be spawned in Deathmatch. Sorry!", 255, 0, 0)
			Else
				n.NPCs = CreateNPC(NPC_SCP_1048, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
				consoleMSG = "SCP-1048-a instance spawned."
			EndIf
			
		Case "human"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				CreateConsoleMsg("Human cannot be spawned in Multiplayer. Sorry!", 255, 0, 0)
			Else
				n.NPCs = CreateNPC(NPC_Human, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
				consoleMSG = "Human spawned."
			EndIf
			
		Case "ci", "chaos", "chaosinsurgency"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				CreateConsoleMsg("Chaos Insurgent cannot be spawned in Multiplayer. Sorry!", 255, 0, 0)
			Else
				n.NPCs = CreateNPC(NPC_CI, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
				consoleMSG = "Chaos Insurgent spawned."
			EndIf
			
		Case "guard"
			n.NPCs = CreateNPC(NPC_Guard, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			consoleMSG = "Guard spawned."	
			
		Case "ntf", "ninetailedfox"
			n.NPCs = CreateNPC(NPC_NTF, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			consoleMSG = "MTF unit E-11 spawned."
			
		Case "ntfhds"
			n.NPCs = CreateNPC(NPC_NTF_HDS, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			consoleMSG = "MTF special unit E-11 spawned."
			
		Case "hdsboss"
			n.NPCs = CreateNPC(NPC_NTF_HDS_Boss, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
			consoleMSG = "MTF HDS trooper spawned."
			
		Case "tentacle"
			n.NPCs = CreateNPC(NPC_SCP_035_Tentacle, EntityX(Collider), EntityY(Collider), EntityZ(Collider))
			consoleMSG = "SCP-035 tentacle spawned."
			
		Case "d","aggressive_d","classd","class-d"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				CreateConsoleMsg("Class-D cannot be spawned in Multiplayer. Sorry!", 255, 0, 0)
			Else
				n.NPCs = CreateNPC(NPC_Class_D, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
				consoleMSG = "Class-D spawned."
			EndIf
			
		Case "d9341","benjamin","subject d-9341"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				CreateConsoleMsg("Subject D-9341 cannot be spawned in Multiplayer. Sorry!", 255, 0, 0)
			Else
				n.NPCs = CreateNPC(NPC_Class_D, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider),0)
				consoleMSG = "Subject D-9341 spawned."
			EndIf
			
		Case "076","scp-076","scp-076-2","scp076","scp076-2"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				CreateConsoleMsg("SCP-076-2 cannot be spawned in Multiplayer. Sorry!", 255, 0, 0)
			Else
				n.NPCs = CreateNPC(NPC_SCP_076, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
				consoleMSG = "SCP-076-2 spawned."
			EndIf	
			
		Case "035", "scp-035", "scp035"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER And mp_I\Gamemode <> Null And mp_I\Gamemode\ID <> Gamemode_Deathmatch Then
				n.NPCs = CreateNPC(NPC_SCP_035, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
				consoleMSG = "SCP-035 spawned."
			Else
				CreateConsoleMsg("SCP-035 cannot be spawned in Singleplayer. Sorry!", 255, 0, 0)
			EndIf	
		Case "682", "scp-682", "scp682"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				CreateConsoleMsg("SCP-682 cannot be spawned in Multiplayer. Sorry!", 255, 0, 0)
			Else
				n.NPCs = CreateNPC(NPC_SCP_682, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
				consoleMSG = "SCP-682 spawned."
			EndIf	
		Case "457", "scp-457", "scp457"
			If gopt\GameMode = GAMEMODE_MULTIPLAYER And mp_I\Gamemode <> Null And mp_I\Gamemode\ID = Gamemode_Deathmatch Then
				CreateConsoleMsg("SCP-457 cannot be spawned in Deathmatch. Sorry!", 255, 0, 0)
			Else
				n.NPCs = CreateNPC(NPC_SCP_457, EntityX(Collider), EntityY(Collider) + 0.2, EntityZ(Collider))
				consoleMSG = "SCP-457 spawned."
			EndIf	
		Default 
			CreateConsoleMsg("NPC type not found.", 255, 0, 0) : Return
	End Select
	
	If n <> Null Then
		If c_state <> "" Then n\State[0] = Float(c_state) : consoleMSG = consoleMSG + " (State = " + n\State[0] + ")"
	EndIf
	
	If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
		Collider = 0
		If n <> Null Then
			ResetEntity n\Collider
		EndIf
	EndIf
	
	If consoleMSG <> "" Then
		CreateConsoleMsg(consoleMSG)
	EndIf	
	
End Function

Function ManipulateNPCBones()
	Local n.NPCs,bone%,pvt%,bonename$
	Local maxvalue#,minvalue#,offset#,smooth#
	Local i%
	Local tovalue#
	
	For n = Each NPCs
		If n\ManipulateBone
			bonename$ = GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"bonename",0)
			If bonename$<>""
				pvt% = CreatePivot()
				bone% = FindChild(n\obj,bonename$)
				If bone% = 0 Then RuntimeError "ERROR: NPC bone "+Chr(34)+bonename$+Chr(34)+" does not exist."
				PositionEntity pvt%,EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True)
				Select n\ManipulationType
					Case 0 ;<--- looking at player
						For i = 1 To GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"controller_max",1)
							If GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"controlleraxis"+i,0) = "pitch"
								maxvalue# = GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"controlleraxis"+i+"_max",2)
								minvalue# = GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"controlleraxis"+i+"_min",2)
								offset# = GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"controlleraxis"+i+"_offset",2)
								If GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"controlleraxis"+i+"_inverse",3)
									tovalue = -DeltaPitch(bone,Camera)+offset
								Else
									tovalue = DeltaPitch(bone,Camera)+offset
								EndIf
								;n\BonePitch = CurveAngle(tovalue,n\BonePitch,20.0)
								smooth# = GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"controlleraxis"+i+"_smoothing",2)
								If smooth>0.0
									n\BonePitch = CurveAngle(tovalue,n\BonePitch,smooth)
								Else
									n\BonePitch = tovalue
								EndIf
								n\BonePitch = ChangeAngleValueForCorrectBoneAssigning(n\BonePitch)
								n\BonePitch = Max(Min(n\BonePitch,maxvalue),minvalue)
							ElseIf GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"controlleraxis1",0) = "yaw"
								maxvalue# = GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"controlleraxis"+i+"_max",2)
								minvalue# = GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"controlleraxis"+i+"_min",2)
								offset# = GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"controlleraxis"+i+"_offset",2)
								If GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"controlleraxis"+i+"_inverse",3)
									tovalue = -DeltaYaw(bone,Camera)+offset
								Else
									tovalue = DeltaYaw(bone,Camera)+offset
								EndIf
								;n\BoneYaw = CurveAngle(tovalue,n\BoneYaw,20.0)
								smooth# = GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"controlleraxis"+i+"_smoothing",2)
								If smooth>0.0
									n\BoneYaw = CurveAngle(tovalue,n\BoneYaw,smooth)
								Else
									n\BoneYaw = tovalue
								EndIf
								n\BoneYaw = ChangeAngleValueForCorrectBoneAssigning(n\BoneYaw)
								n\BoneYaw = Max(Min(n\BoneYaw,maxvalue),minvalue)
							;ElseIf --> (Roll Value)
							;	
							EndIf
						Next
						
						RotateEntity bone%,EntityPitch(bone)+n\BonePitch,EntityYaw(bone)+n\BoneYaw,EntityRoll(bone)+n\BoneRoll
				End Select
				pvt = FreeEntity_Strict(pvt)
			EndIf
		EndIf
	Next
	
End Function

Function GetNPCManipulationValue$(NPC$,bone$,section$,valuetype%=0)
	;valuetype determines what type of variable should the Output be returned
	;0 - String
	;1 - Int
	;2 - Float
	;3 - Boolean
	
	Local value$ = GetINIString("Data\NPCBones.ini",NPC$,bone$+"_"+section$)
	Select valuetype%
		Case 0
			Return value$
		Case 1
			Return Int(value$)
		Case 2
			Return Float(value$)
		Case 3
			If value$ = "true" Lor value$ = "1"
				Return True
			Else
				Return False
			EndIf
	End Select
	
End Function

Function ChangeAngleValueForCorrectBoneAssigning(value#)
	Local numb#
	
	If value# <= 180.0
		numb# = value#
	Else
		numb# = -360+value#
	EndIf
	
	Return numb#
End Function

Function NPCSpeedChange(n.NPCs)
	
	Select n\NPCtype
		Case NPC_SCP_173,NPC_SCP_106,NPC_SCP_096,NPC_SCP_049,NPC_SCP_939,NPC_SCP_076
			Select SelectedDifficulty\OtherFactors
				Case EASY
					n\Speed = n\Speed * 0.8
				Case HARD
					n\Speed = n\Speed * 1.2
			End Select
	End Select
	
End Function

Function PlayerInReachableRoom(canSpawnIn049Chamber=False)
	Local RN$ = ""
	Local e.Events, temp
	
	If PlayerRoom<>Null Then
		RN = PlayerRoom\RoomTemplate\Name
	EndIf
	
	;Player is in these rooms, returning false
	If RN = "pocketdimension" Lor RN = "gate_a_topside" Lor RN = "gate_b_topside" Lor RN = "gate_a_intro" Then
		Return False
	EndIf
	;Player is in 860's test room and inside the forest, returning false
	temp = False
	For e = Each Events
		If e\EventName$ = "testroom_860" And e\EventState[0] = 1.0 Then
			temp = True
			Exit
		EndIf
	Next
	If RN = "testroom_860" And temp Then
		Return False
	EndIf
	If (Not canSpawnIn049Chamber) Then
		If SelectedDifficulty\AggressiveNPCs = False Then
			If RN = "cont_049" And EntityY(Collider)<=-2848*RoomScale Then
				Return False
			EndIf
		EndIf
	EndIf
	;Return true, this means player is in reachable room
	Return True
	
End Function

Function CheckForNPCInFacility(n.NPCs)
	;False (=0): NPC is not in facility (mostly meant for "dimension1499")
	;True (=1): NPC is in facility
	;2: NPC is in tunnels (maintenance tunnels/049 tunnels/939 storage room, etc...)
	
	If EntityY(n\Collider)>100.0
		Return False
	EndIf
	If EntityY(n\Collider)< -10.0
		Return 2
	EndIf
	If EntityY(n\Collider)> 7.0 And EntityY(n\Collider)<=100.0
		Return 2
	EndIf
	
	Return True
End Function

Function FindNextElevator(n.NPCs)
	Local eo.ElevatorObj, eo2.ElevatorObj
	
	For eo = Each ElevatorObj
		If eo\InFacility = n\InFacility
			If Abs(EntityY(eo\obj,True)-EntityY(n\Collider))<10.0
				For eo2 = Each ElevatorObj
					If eo2 <> eo
						If eo2\InFacility = n\InFacility
							If Abs(EntityY(eo2\obj,True)-EntityY(n\Collider))<10.0
								If EntityDistanceSquared(eo2\obj,n\Collider)<EntityDistanceSquared(eo\obj,n\Collider)
									n\PathStatus = FindPath(n, EntityX(eo2\obj,True),EntityY(eo2\obj,True),EntityZ(eo2\obj,True))
									n\CurrElevator = eo2
									;debuglog "eo2 found for "+n\NPCtype
									Exit
								EndIf
							EndIf
						EndIf
					EndIf
				Next
				If n\CurrElevator = Null
					n\PathStatus = FindPath(n, EntityX(eo\obj,True),EntityY(eo\obj,True),EntityZ(eo\obj,True))
					n\CurrElevator = eo
					;debuglog "eo found for "+n\NPCtype
				EndIf
				If n\PathStatus <> 1
					n\CurrElevator = Null
					;debuglog "Unable to find elevator path: Resetting CurrElevator"
				EndIf
				Exit
			EndIf
		EndIf
	Next
	
End Function

Function GoToElevator(n.NPCs)
	Local distsquared#,inside%
	
	If n\PathStatus <> 1
		PointEntity n\obj,n\CurrElevator\obj
		RotateEntity n\Collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),20.0),0
		
		inside% = False
		If Abs(EntityX(n\Collider)-EntityX(n\CurrElevator\obj,True))<280.0*RoomScale
			If Abs(EntityZ(n\Collider)-EntityZ(n\CurrElevator\obj,True))<280.0*RoomScale Then
				If Abs(EntityY(n\Collider)-EntityY(n\CurrElevator\obj,True))<280.0*RoomScale Then
					inside% = True
				EndIf
			EndIf
		EndIf
		
		distsquared = EntityDistanceSquared(n\Collider,n\CurrElevator\door\frameobj)
		If n\CurrElevator\door\open
			If (distsquared > PowTwo(0.4) And distsquared < PowTwo(0.7)) And inside%
				UseDoor(n\CurrElevator\door,False)
				;debuglog n\NPCtype+" used elevator"
			EndIf
		Else
			If distsquared < PowTwo(0.7)
				n\CurrSpeed = 0.0
				If n\CurrElevator\door\NPCCalledElevator=False
					n\CurrElevator\door\NPCCalledElevator = True
					;debuglog n\NPCtype+" called elevator"
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

Function FinishWalking(n.NPCs,startframe#,endframe#,speed#)
	Local centerframe#
	
	If n<>Null
		centerframe# = (endframe#-startframe#)/2
		If n\Frame >= centerframe#
			AnimateNPC(n,startframe#,endframe#,speed#,False)
		Else
			AnimateNPC(n,endframe#,startframe#,-speed#,False)
		EndIf
	EndIf
	
End Function

Function ChangeNPCTexture(n.NPCs,texture$)
	
	n\texture = texture
	Local tex = LoadTexture_Strict(n\texture, 1, 2)
	TextureBlend(tex,5)
	EntityTexture(n\obj, tex)
	DeleteSingleTextureEntryFromCache tex
	
End Function

Function IsNPCStuck(n.NPCs,time#)
	Local x#,y#,z#
	Local timer#
	
	timer=Max(timer-FPSfactor,0.0)
	;debuglog("Stuck Timer: "+timer)
	If timer<=0.0 Then
		If EntityX(n\Collider)=x Then
			If EntityZ(n\Collider)=z Then
				Return True
			EndIf
		EndIf
		timer=time
	EndIf
	If timer=time/2 Then
		x = EntityX(n\Collider)
		y = EntityY(n\Collider)
		z = EntityZ(n\Collider)
	EndIf
	
	Return False
End Function

Function NPC_GoTo(n.NPCs, IdleAnim.Vector3D, MoveAnim.Vector3D, path_target%, n_speed#=0.0)
	Local WaypointDist# = 0.0
	Local temp% = False
	
	Local StuckTimer# = 5.0
	
	Local w.WayPoints
	
	While n\Path[n\PathLocation]=Null
		If n\PathLocation > 19 Then
			n\PathLocation = 0 : n\PathStatus = 0 : Exit
		Else
			n\PathLocation = n\PathLocation + 1
		EndIf
	Wend
	If n\PathStatus = 1 Then
		n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,n\Path[n\PathLocation]\obj)
		RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
		
		AnimateNPC(n, MoveAnim\x, MoveAnim\y, n\CurrSpeed * 100 * MoveAnim\z)
		
		n\CurrSpeed = CurveValue(n\Speed * n_speed, n\CurrSpeed, 20.0)
		
		MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
		
		WaypointDist = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
		If WaypointDist < 0.77 Then
			temp = True
			If n\Path[n\PathLocation]\door<>Null Then
				If (Not n\Path[n\PathLocation]\door\IsElevatorDoor) Then
					If n\Path[n\PathLocation]\door\locked Lor n\Path[n\PathLocation]\door\Code<>"" Then
						temp = False
					Else
						If n\Path[n\PathLocation]\door\open = False Then UseDoorNPC(n\Path[n\PathLocation]\door, n, True)
						;Check if the beeping will be necessary
						If n\NPCtype = NPC_NTF And n\Path[n\PathLocation]\door\open = False Then PlaySound2(LoadTempSound("SFX\Character\MTF\Beep.ogg"), Camera, n\Collider)
					EndIf
				EndIf
			EndIf
			If WaypointDist < 0.3 And temp Then
				n\PathLocation = n\PathLocation + 1
			ElseIf WaypointDist<0.5 And (Not temp) Then
				n\PathStatus = 0
				n\PathTimer# = 0.0
			EndIf
		EndIf
		
		If IsNPCStuck(n, 70*StuckTimer) Then
			CreateConsoleMsg("NPCtype "+n\NPCName+": "+n\ID+" . Restarting pathfinding...", 255)
			n\PathStatus = 0
		EndIf
	Else
		n\PathTimer = Max(0, n\PathTimer-FPSfactor)
		AnimateNPC(n, IdleAnim\x, IdleAnim\y, IdleAnim\z)
		n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 20.0)
		If n\PathTimer = 0.0 Then
			n\PathStatus = FindPath(n,EntityX(path_target),EntityY(path_target)+0.2,EntityZ(path_target))
			n\PathTimer = 70*2
		EndIf
	EndIf
End Function

Function NPC_GoToRoom(n.NPCs, IdleAnim.Vector3D, MoveAnim.Vector3D, n_speed#=0.0)
	Local WaypointDist# = 0.0
	Local temp% = False
	
	Local w.WayPoints, r.Rooms
	
	While n\Path[n\PathLocation]=Null
		If n\PathLocation > 19 Then
			n\PathLocation = 0 : n\PathStatus = 0 : Exit
		Else
			n\PathLocation = n\PathLocation + 1
		EndIf
	Wend
	If n\PathStatus = 1 Then
		n\Angle = EntityYaw(n\Collider) + DeltaYaw(n\obj,n\Path[n\PathLocation]\obj)
		RotateEntity n\Collider, 0, CurveAngle(n\Angle, EntityYaw(n\Collider), 20.0), 0
		
		AnimateNPC(n, MoveAnim\x, MoveAnim\y, n\CurrSpeed * 100 * MoveAnim\z)
		
		If n\Speed<>0 Then
			n\CurrSpeed = CurveValue(n\Speed * n_speed, n\CurrSpeed, 20.0)
		Else
			n\CurrSpeed = CurveValue(n\Speed * n_speed, n\CurrSpeed, 20.0)
		EndIf
		
		MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
		
		WaypointDist = EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj)
		If WaypointDist < 0.77 Then
			temp = True
			If n\Path[n\PathLocation]\door<>Null Then
				If (Not n\Path[n\PathLocation]\door\IsElevatorDoor) Then
					If n\Path[n\PathLocation]\door\locked Lor n\Path[n\PathLocation]\door\Code<>"" Then
						temp = False
					Else
						If n\Path[n\PathLocation]\door\open = False Then UseDoor(n\Path[n\PathLocation]\door, False)
					EndIf
				EndIf
			EndIf
			If WaypointDist<0.3 And temp Then
				n\PathLocation = n\PathLocation + 1
			ElseIf WaypointDist<0.5 And (Not temp) Then
				n\PathStatus = 0
				n\PathTimer# = 0.0
			EndIf
		EndIf
	Else
		AnimateNPC(n, IdleAnim\x, IdleAnim\y, IdleAnim\z)
		n\CurrSpeed = CurveValue(0.0, n\CurrSpeed, 20.0)
		MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
		If n\PathTimer = 0.0 Then
			Local roomfound% = False
			While roomfound = False
				If n\NPCRoom=Null Then
					GetNPCRoom(n)
				EndIf
				For r.Rooms = Each Rooms
					If IsRoomAdjacent(r,n\NPCRoom) Then
						If Rand(5)=1 Then
							n\EnemyX = EntityX(r\obj)
							n\EnemyY = EntityY(r\obj)
							n\EnemyZ = EntityZ(r\obj)
							n\NPCRoom = r
							roomfound = True
							Exit
						EndIf
					EndIf
				Next
			Wend
			If n\Idle > 0.1 Then
				n\CurrSpeed = 0.0
			EndIf
			n\PathStatus = FindPath(n,n\EnemyX,n\EnemyY,n\EnemyZ)
			n\PathTimer = 70*5
			If n\Path[2]=Null Then
				For w.WayPoints = Each WayPoints
					If EntityDistanceSquared(w\obj,Collider)<=2.8284 Then
						n\PathLocation = 2
						n\Path[2] = w
						Exit
					EndIf
				Next
			EndIf
		EndIf
	EndIf
End Function

Function NPC_GoToCover%(n.NPCs, MoveAnim.Vector3D, target%, n_speed#=0.0)
	Local waypoint.WayPoints
	Local i%, visible%
	
	If n\Path[0] = Null Then
		n\Path[0] = GetClosestWaypoint(n\Collider, True, target)
		If n\Path[0] = Null Lor ((n\Path[0]\door <> Null) And (Not n\Path[0]\door\open)) Then
			n\Path[0] = Null
			n\CurrSpeed = 0
			Return True
		EndIf
	EndIf
	
	If EntityDistanceSquared(n\Collider, n\Path[0]\obj) > PowTwo(0.65) Then
		n\CurrSpeed = CurveValue(n\Speed * n_speed, n\CurrSpeed, 20.0)
		PointEntity n\obj, n\Path[0]\obj
		RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 10.0), 0
		MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
		AnimateNPC(n, MoveAnim\x, MoveAnim\y, n\CurrSpeed * 100 * MoveAnim\z)
	Else
		n\CurrSpeed = 0
		n\Path[0] = Null
		Return True
	EndIf
	
	Return False
End Function

Function GetClosestWaypoint.WayPoints(entity%, mustBeVisible% = False, target% = 0)
	Local dist# = 10000.0
	Local dist2#, smallestway.WayPoints
	Local w.WayPoints
	
	For w = Each WayPoints
		dist2# = EntityDistanceSquared(entity, w\obj)
		If dist2 < dist And ((Not mustBeVisible) Lor EntityVisible(entity, w\obj)) Then
			If target = 0 Lor (Not EntityVisible(target, w\obj)) Then
				dist = dist2
				smallestway = w
			EndIf
		EndIf
	Next
	
	Return smallestway
End Function

Function NPCSeesEntity(n.NPCs, target, surface=False)
	If surface Then
		If EntityVisible(n\Collider, target) Then
			Return True
		EndIf
	Else
		If EntityDistanceSquared(n\Collider, target)<PowTwo(7) Then
			If EntityVisible(n\Collider, target) Then
				Return True
			EndIf
		EndIf
	EndIf
	
	Return False
End Function

Function PlayNPCSound(n.NPCs, sound$, loop%=False, range#=10, volume#=1.0)
	range# = Max(range, 1.0)
    If loop Then
        n\SoundChn = LoopSound2(sound, n\SoundChn, Camera, n\Collider, range, volume)
    Else
        n\SoundChn = PlaySound2(sound, Camera, n\Collider, range, volume)
    EndIf
	
	Local dist# = EntityDistance(Camera, n\Collider) / range#
	If 1 - dist# > 0 And 1 - dist# < 1 Then
		ChannelVolume(n\SoundChn, volume# * (1 - dist#)*opt\VoiceVol#*opt\MasterVol)
    EndIf
	
    If (Not ChannelPlaying(n\SoundChn)) Then
        FreeSound_Strict(sound)
        n\SoundChn = 0
    EndIf
End Function

Function SwitchNPCGun%(n.NPCs, WeaponID%)
	CatchErrors("SwitchNPCGun(" + n\NPCtype + ", " + WeaponID + ")")
	Local g.Guns
	Local bone%, gunname$
	Local Scale#, VectorString$, WeaponType%
	
	RemoveNPCGun(n)
	
	Local prevYaw# = EntityYaw(n\obj)
	Local prevX# = EntityX(n\obj)
	Local prevY# = EntityY(n\obj)
	Local prevZ# = EntityZ(n\obj)
	
	RotateEntity n\obj,0,0,0
	PositionEntity n\obj,0,0,0
	
	bone = FindChild(n\obj,GetINIString("Data\NPCBones.ini", n\NPCName, "weapon_hand_bonename"))
	
	gunname$ = ""
	For g = Each Guns
		If g\ID = WeaponID Then
			n\Gun = New NPCGun
			n\Gun\obj = CopyEntity(g\PlayerModel, bone)
			n\Gun\ID = g\ID
			n\Gun\AnimType = g\PlayerModelAnim
			n\Gun\ReloadAmmo = g\CurrReloadAmmo
			n\Gun\MaxAmmo = g\MaxCurrAmmo
			n\Gun\Ammo = g\CurrAmmo
			n\Gun\Name = g\name
			n\Gun\GunType = g\GunType
			n\Gun\MaxGunshotSounds = g\MaxShootSounds
			n\Gun\MaxReloadSounds = g\MaxReloadSounds
			n\Gun\Damage = g\DamageOnEntity
			n\Gun\ShootFrequency = g\Rate_Of_Fire
			n\Gun\BulletsPerShot = g\Amount_Of_Bullets
			gunname = g\name
			Exit
		EndIf
	Next
	
	If gunname <> "" Then
		Scale# = GetINIFloat(gv\WeaponFile, gunname, "world scale", 0.02) / EntityScaleX(n\obj)
		ScaleEntity n\Gun\obj,Scale,Scale,Scale
		
		VectorString = GetINIString(gv\WeaponFile, gunname, "npc_model_offset", "")
		If VectorString <> "" Then
			PositionEntity n\Gun\obj, Piece(VectorString,1,"|"), Piece(VectorString,2,"|"), Piece(VectorString,3,"|")
		EndIf
		VectorString = GetINIString(gv\WeaponFile, gunname, "npc_model_rotation", "")
		If VectorString <> "" Then
			RotateEntity n\Gun\obj, Piece(VectorString,1,"|"), Piece(VectorString,2,"|"), Piece(VectorString,3,"|")
		EndIf
	EndIf
	
	RotateEntity n\obj,0,prevYaw,0
	PositionEntity n\obj,prevX,prevY,prevZ
	
	CatchErrors("Uncaught (SwitchNPCGun(" + n\NPCtype + ", " + WeaponID + "))")
End Function

Function RemoveNPCGun(n.NPCs)
	
	If n\Gun <> Null Then
		EntityParent n\Gun\obj, 0
		n\Gun\obj = FreeEntity_Strict(n\Gun\obj)
		Delete n\Gun
	EndIf
	
End Function

Function IsTarget(n.NPCs, target.NPCs)
	
	If n <> Null And target <> Null Then
		Select n\NPCtype
			Case NPC_NTF
				Select target\NPCtype
					Case NPC_Class_D
						Return True
					Case NPC_Zombie,NPC_Zombie_Armed
						If target\State[0] = Z_STATE_WANDER Then
							Return True
						EndIf
					Case NPC_SCP_939
						Return True
					Case NPC_SCP_106
						Return True
					Case NPC_SCP_173
						Return True
					Case NPC_SCP_049
						Return True
					Case NPC_SCP_096
						Return True
					Case NPC_SCP_1048
						Return True
				End Select
		End Select
	EndIf
	
	Return False
End Function

Function GetNPCRoom(n.NPCs)
	Local r.Rooms
	Local x#, z#
	
	For r.Rooms = Each Rooms
		x = Abs(r\x-EntityX(n\Collider,True))
		z = Abs(r\z-EntityZ(n\Collider,True))
		
		If x<16 And z < 16 Then
			If x < 4.0 Then
				If z < 4.0 Then
					If Abs(EntityY(n\Collider) - EntityY(r\obj)) < 1.5 Then
						n\NPCRoom = r
						Exit
					EndIf
				EndIf
			EndIf
		EndIf
	Next
End Function

Function PreloadAllNPCAnimations()
	
	; ~ Class-D
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "idle")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "walk")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "death_front")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "death_back")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "death_left")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "death_right")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "pistol_idle")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "pistol_walk")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "pistol_reload")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "rifle_idle")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "rifle_walk")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "rifle_reload")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "shotgun_idle")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "shotgun_walk")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "shotgun_reload")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "smg_idle")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "smg_walk")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "smg_reload")
	PreloadNPCAnimation("Class-D-Armed", "Class-D", "surrender")
	; ~ Chaos Insurgency
	PreloadNPCAnimation("CI", "Chaos Insurgency", "idle")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "walk")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "death_1")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "death_2")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "death_3")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "pistol_idle")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "pistol_walk")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "pistol_reload")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "rifle_idle")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "rifle_walk")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "rifle_reload")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "shotgun_idle")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "shotgun_walk")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "shotgun_reload")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "smg_idle")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "smg_walk")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "smg_reload")
	PreloadNPCAnimation("CI", "Chaos Insurgency", "throw_grenade")
	
End Function

Function PreloadNPCAnimation(NPCtype, NPCName$, AnimName$)
	Local file$ = "Data\NPCAnims.ini"
	Local na.NPCAnim
	
	Local AnimString$ = GetINIString(file, NPCName, AnimName)
	If AnimString<>"" Then
		na = New NPCAnim
		na\NPCtype = NPCtype
		na\AnimName = AnimName
		na\Animation = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	EndIf
	
End Function

Function FindNPCAnimation.Vector3D(NPCtype, AnimName$)
	Local na.NPCAnim
	
	For na = Each NPCAnim
		If na\NPCtype = NPCtype And na\AnimName = AnimName Then
			Return na\Animation
		EndIf
	Next
	Return Null
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D