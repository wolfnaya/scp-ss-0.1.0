
; ~ MTF Unit constants

;[Block]
Const GUARD_FOLLOWPLAYER = 0
Const GUARD_CONTACT = 1
Const GUARD_FLEE = 2
Const GUARD_TOTARGET = 3
Const GUARD_TARGET_PLAYER = 4
;[End Block]

Function CreateNPCtypeGuard(n.NPCs)
	Local temp#
	
	n\NPCName = "Guard"
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.2
	EntityType n\Collider, HIT_PLAYER
	n\obj = CopyEntity(NPCModel[Model_Guard])
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "Guard", "speed") / 100.0)
	temp# = (GetINIFloat("DATA\NPCs.ini", "Guard", "scale") / 2.5)
	
	ScaleEntity n\obj, temp, temp, temp
	
	MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	
	SwitchNPCGun%(n, GUN_P90)
	n\Gun\Ammo = n\Gun\MaxAmmo
	
	CopyHitBoxes(n)
	
	n\HP = 140
	
End Function

Function UpdateNPCtypeGuard(n.NPCs)
	Local prevFrame#, dist#, pvt%
	Local p.Particles, wp.WayPoints
	
	prevFrame# = n\Frame
	
	n\BoneToManipulate = ""
	n\ManipulateBone = False
	n\ManipulationType = 0
	n\NPCNameInSection = "Guard"
	
	Select n\State[0]
		Case 1 ;aims and shoots at the player
			;[Block]
			If n\Frame < 39 Lor (n\Frame > 76 And n\Frame < 245) Lor (n\Frame > 248 And n\Frame < 302) Lor n\Frame > 344 Then
				AnimateNPC(n,345,357,0.2,False)
				If n\Frame >= 356 Then
					SetNPCFrame(n,302)
				EndIf
			EndIf
			
			If KillTimer >= 0 Then
				dist = EntityDistanceSquared(n\Collider,Collider)
				Local ShootAccuracy# = 0.4+0.5*SelectedDifficulty\AggressiveNPCs ;TODO this can probably be optimized with an if else
				Local DetectDistance# = PowTwo(11.0)
				
				If dist < DetectDistance Then
					pvt% = CreatePivot()
					PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider), EntityZ(n\Collider))
					PointEntity(pvt, Collider)
					RotateEntity(pvt, Min(EntityPitch(pvt), 20), EntityYaw(pvt), 0)
					
					RotateEntity(n\Collider, CurveAngle(EntityPitch(pvt), EntityPitch(n\Collider), 10), CurveAngle(EntityYaw(pvt), EntityYaw(n\Collider), 10), 0, True)
					
					PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider)+0.8, EntityZ(n\Collider))
					PointEntity(pvt, Collider)
					RotateEntity(pvt, Min(EntityPitch(pvt), 40), EntityYaw(n\Collider), 0)
					
					If n\Reload = 0 Then
						;debuglog "entitypick"
						EntityPick(pvt, Sqr(dist))
						If PickedEntity() = Collider Lor n\State[2]=1 Then
							Local instaKillPlayer% = False
							
							If gopt\GameMode = GAMEMODE_CLASS_D Then
								If PlayerRoom\RoomTemplate\Name = "cont_173" Then 
									m_msg\DeathTxt = "Class D. Cause of death: Gunshot wound to the Head. The surveillance tapes confirm that the subject was terminated by Agent Ulgrin shortly after the site lockdown was initiated."
									instaKillPlayer = True
								ElseIf PlayerRoom\RoomTemplate\Name = "gate_b_topside" Then
									m_msg\DeathTxt = Chr(34)+"Agent G. to control. Eliminated a Class D escapee in Gate B's courtyard."+Chr(34)
								Else
									m_msg\DeathTxt = ""
								EndIf
							EndIf
							
							PlaySound2(GunshotSFX, Camera, n\Collider, 35)
							
							RotateEntity(pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
							PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
							MoveEntity (pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)
							
							PointEntity pvt, Collider
							ShootPlayer(EntityX(pvt), EntityY(pvt), EntityZ(pvt), ShootAccuracy, False, instaKillPlayer)
							n\Reload = 4.67 ;900 RPM
						Else
							n\CurrSpeed = n\Speed
						EndIf
					EndIf
					
					If n\Reload > 0 And n\Reload <= 4.67 Then ;900 RPM
						AnimateNPC(n,245,248,0.35,True)
					Else
						If n\Frame < 302 Then
							AnimateNPC(n,302,344,0.35,True)
						EndIf
					EndIf
					
					pvt = FreeEntity_Strict(pvt)
				Else
					AnimateNPC(n,302,344,0.35,True)
				EndIf
				
				n\ManipulateBone = True
				
				If n\State[1] = 10 Then ;Hacky way of applying spine pitch to specific guards.
					n\BoneToManipulate = "Chest"
					n\ManipulationType = 3
				Else
					n\BoneToManipulate = "Chest"
					n\ManipulationType = 0
				EndIf
			Else
				n\State[0] = 0
			EndIf
			;[End Block]
		Case 2 ;shoots
			;[Block]
			AnimateNPC(n,245,248,0.35,True)
			If n\Reload = 0 Then
				If n\ShootSFXCHN<>0 Then
					If ChannelPlaying(n\ShootSFXCHN) Then StopChannel(n\ShootSFXCHN) : n\ShootSFXCHN = 0
					FreeSound_Strict(n\ShootSFX) : n\ShootSFX = 0
				EndIf
				n\ShootSFX = LoadSound_Strict("SFX\Guns\P90\shoot"+Rand(1,4)+".ogg")
				n\ShootSFXCHN = PlaySound2(n\ShootSFX,Camera,n\Collider,20)
				p.Particles = CreateParticle(EntityX(n\obj, True), EntityY(n\obj, True), EntityZ(n\obj, True), 1, 0.2, 0.0, 5)
				PositionEntity(p\pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
				RotateEntity(p\pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
				MoveEntity (p\pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)
				n\Reload = 4.67 ;900 RPM
			EndIf
			;[End Block]
		Case 3 ;follows a path
			;[Block]
			If n\PathStatus = 2 Then
				n\State[0] = 0
				n\CurrSpeed = 0
			ElseIf n\PathStatus = 1 Then
				If n\Path[n\PathLocation]=Null Then 
					If n\PathLocation > 19 Then 
						n\PathLocation = 0 : n\PathStatus = 0
					Else
						n\PathLocation = n\PathLocation + 1
					EndIf
				Else
					PointEntity n\obj, n\Path[n\PathLocation]\obj
					
					RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
					
					AnimateNPC(n,1,38,n\CurrSpeed*40)
					n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
					
					MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
					
					If EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj) < PowTwo(0.2) Then
						n\PathLocation = n\PathLocation + 1
					EndIf 
				EndIf
			Else
				n\CurrSpeed = 0
				n\State[0] = 4
			EndIf
			;[End Block]
		Case 4
			;[Block]
			AnimateNPC(n,77,201,0.2)
			
			If Rand(400) = 1 Then n\Angle = Rnd(-180, 180)
			
			RotateEntity(n\Collider, 0, CurveAngle(n\Angle + Sin(MilliSecs() / 50) * 2, EntityYaw(n\Collider), 150.0), 0, True)
			
			If EntityDistanceSquared(n\Collider, Collider) < PowTwo(15.0) Then
				If WrapAngle(EntityYaw(n\Collider)-DeltaYaw(n\Collider, Collider))<90 Then
					If EntityVisible(pvt,Collider) Then n\State[0] = 1
				EndIf
			EndIf
			;[End Block]
		Case 5 ;following a target
			;[Block]
			RotateEntity n\Collider, 0, CurveAngle(VectorYaw(n\EnemyX-EntityX(n\Collider), 0, n\EnemyZ-EntityZ(n\Collider))+n\Angle, EntityYaw(n\Collider), 20.0), 0
			
			dist# = DistanceSquared(EntityX(n\Collider),n\EnemyX,EntityZ(n\Collider),n\EnemyZ)
			
			AnimateNPC(n,1,38,n\CurrSpeed*40)
			
			If dist > PowTwo(2.0) Lor dist < PowTwo(1.0)  Then
				n\CurrSpeed = CurveValue(n\Speed*Sgn(Sqr(dist)-1.5)*0.75, n\CurrSpeed, 10.0)
			Else
				n\CurrSpeed = CurveValue(0, n\CurrSpeed, 10.0)
			EndIf
			
			MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
			;[End Block]
		Case 7
			;[Block]
			AnimateNPC(n,77,201,0.2)
			;[End Block]
		Case 8
			
		Case 9
			;[Block]
			AnimateNPC(n,77,201,0.2)
			n\BoneToManipulate = "Head"
			n\ManipulateBone = True
			n\ManipulationType = 0
			n\Angle = EntityYaw(n\Collider)
			;[End Block]
		Case 10
			;[Block]
			AnimateNPC(n, 1, 38, n\CurrSpeed*40)
			
			n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
			
			MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
			;[End Block]
		Case 11
			;[Block]
			If n\Frame < 39 Lor (n\Frame > 76 And n\Frame < 245) Lor (n\Frame > 248 And n\Frame < 302) Lor n\Frame > 344 Then
				AnimateNPC(n,345,357,0.2,False)
				If n\Frame >= 356 Then
					SetNPCFrame(n,302)
				EndIf
			EndIf
			
			If KillTimer => 0 Then
				dist = EntityDistanceSquared(n\Collider,Collider)
				
				Local SearchPlayer% = False
				If dist < PowTwo(11.0) Then
					If EntityVisible(n\Collider,Collider) Then
						SearchPlayer = True
					EndIf
				EndIf
				
				If SearchPlayer Then
					pvt% = CreatePivot()
					PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider), EntityZ(n\Collider))
					PointEntity(pvt, Collider)
					RotateEntity(pvt, Min(EntityPitch(pvt), 20), EntityYaw(pvt), 0)
					
					RotateEntity(n\Collider, CurveAngle(EntityPitch(pvt), EntityPitch(n\Collider), 10), CurveAngle(EntityYaw(pvt), EntityYaw(n\Collider), 10), 0, True)
					
					PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider)+0.8, EntityZ(n\Collider))
					PointEntity(pvt, Collider)
					RotateEntity(pvt, Min(EntityPitch(pvt), 40), EntityYaw(n\Collider), 0)
					
					If n\Reload = 0 Then
						;debuglog "entitypick"
						EntityPick(pvt, Sqr(dist))
						If PickedEntity() = Collider Lor n\State[2]=1 Then
							instaKillPlayer% = False
							
							m_msg\DeathTxt = ""
							
							PlaySound2(GunshotSFX, Camera, n\Collider, 35)
							
							RotateEntity(pvt, EntityPitch(n\Collider), EntityYaw(n\Collider), 0, True)
							PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
							MoveEntity (pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)
							
							PointEntity pvt, Collider
							ShootPlayer(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 1.0, False, instaKillPlayer)
							n\Reload = 4.67 ;900 RPM
						Else
							n\CurrSpeed = n\Speed
						EndIf
					EndIf
					
					If n\Reload > 0 And n\Reload <= 4.67 Then ;900 RPM
						AnimateNPC(n,245,248,0.35,True)
					Else
						If n\Frame < 302 Then
							AnimateNPC(n,302,344,0.35,True)
						EndIf
					EndIf
					
					pvt = FreeEntity_Strict(pvt)
				Else
					If n\PathStatus = 1 Then
						If n\Path[n\PathLocation]=Null Then 
							If n\PathLocation > 19 Then 
								n\PathLocation = 0 : n\PathStatus = 0
							Else
								n\PathLocation = n\PathLocation + 1
							EndIf
						Else
							AnimateNPC(n,39,76,n\CurrSpeed*40)
							n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
							
							PointEntity n\obj, n\Path[n\PathLocation]\obj
							
							RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
							
							If EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj) < PowTwo(0.2) Then
								n\PathLocation = n\PathLocation + 1
							EndIf
						EndIf
					Else
						If n\PathTimer = 0 Then
							n\PathStatus = FindPath(n,EntityX(Collider),EntityY(Collider)+0.5,EntityZ(Collider))
						EndIf
						
						Local wayPointCloseToPlayer.WayPoints
						wayPointCloseToPlayer = Null
						
						For wp.WayPoints = Each WayPoints
							If EntityDistanceSquared(wp\obj,Collider) < PowTwo(2.0) Then
								wayPointCloseToPlayer = wp
								Exit
							EndIf
						Next
						
						If wayPointCloseToPlayer<>Null Then
							n\PathTimer = 1
							If EntityVisible(wayPointCloseToPlayer\obj,n\Collider) And Abs(DeltaYaw(n\Collider,wayPointCloseToPlayer\obj))>0 Then
								PointEntity n\obj, wayPointCloseToPlayer\obj
								RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
							EndIf
						Else
							n\PathTimer = 0
						EndIf
						
						If n\PathTimer = 1 Then
							AnimateNPC(n,39,76,n\CurrSpeed*40)
							n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
							MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
						EndIf
					EndIf
					
					If prevFrame < 43 And n\Frame => 43 Then
						PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.5,0.7))						
					ElseIf prevFrame < 61 And n\Frame => 61 Then
						PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.5,0.7))
					EndIf
				EndIf
				
			Else
				n\State[0] = 0
			EndIf
			;[End Block]
		Case 12
			;[Block]
			If n\Frame < 39 Lor (n\Frame > 76 And n\Frame < 245) Lor (n\Frame > 248 And n\Frame < 302) Lor n\Frame > 344 Then
				AnimateNPC(n,345,357,0.2,False)
				If n\Frame >= 356 Then
					SetNPCFrame(n,302)
				EndIf
			EndIf
			If n\Frame < 345 Then
				AnimateNPC(n,302,344,0.35,True)
			EndIf
			
			pvt% = CreatePivot()
			PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider), EntityZ(n\Collider))
			If n\State[1] = 1.0 Then
				PointEntity(pvt, Collider)
			Else
				RotateEntity pvt,0,n\Angle,0
			EndIf
			RotateEntity(pvt, Min(EntityPitch(pvt), 20), EntityYaw(pvt), 0)
			
			RotateEntity(n\Collider, CurveAngle(EntityPitch(pvt), EntityPitch(n\Collider), 10), CurveAngle(EntityYaw(pvt), EntityYaw(n\Collider), 10), 0, True)
			
			PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider)+0.8, EntityZ(n\Collider))
			If n\State[1] = 1.0 Then
				PointEntity(pvt, Collider)
				n\ManipulateBone = True
				n\BoneToManipulate = "Chest"
				n\ManipulationType = 0
			Else
				RotateEntity pvt,0,n\Angle,0
			EndIf
			RotateEntity(pvt, Min(EntityPitch(pvt), 40), EntityYaw(n\Collider), 0)
			
			pvt = FreeEntity_Strict(pvt)
			
			UpdateSoundOrigin(n\SoundChn,Camera,n\Collider,20)
			;[End Block]
		Case 13
			;[Block]
			AnimateNPC(n,202,244,0.35,True)
			;[End Block]
		Case 14
			;[Block]
			If n\PathStatus = 2 Then
				n\State[0] = 13
				n\CurrSpeed = 0
			ElseIf n\PathStatus = 1 Then
				If n\Path[n\PathLocation]=Null Then 
					If n\PathLocation > 19 Then 
						n\PathLocation = 0 : n\PathStatus = 0
					Else
						n\PathLocation = n\PathLocation + 1
					EndIf
				Else
					PointEntity n\obj, n\Path[n\PathLocation]\obj
					
					RotateEntity n\Collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\Collider), 20.0), 0
					
					AnimateNPC(n,39,76,n\CurrSpeed*40)
					n\CurrSpeed = CurveValue(n\Speed*0.7, n\CurrSpeed, 20.0)
					
					MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
					
					If EntityDistanceSquared(n\Collider,n\Path[n\PathLocation]\obj) < PowTwo(0.2) Then
						n\PathLocation = n\PathLocation + 1
					EndIf 
				EndIf
			Else
				n\CurrSpeed = 0
				n\State[0] = 13
			EndIf
			
			If prevFrame < 43 And n\Frame => 43 Then
				PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.5,0.7))						
			ElseIf prevFrame < 61 And n\Frame => 61 Then
				PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.5,0.7))
			EndIf
			;[End Block]
		Case 15
			;aims and shoots at the player immediately
			;[Block]
			If n\Frame < 39 Lor (n\Frame > 76 And n\Frame < 245) Lor (n\Frame > 248 And n\Frame < 302) Lor n\Frame > 344 Then
				AnimateNPC(n,345,357,0.2,False)
				If n\Frame >= 356 Then
					
					pvt% = CreatePivot()
					PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider), EntityZ(n\Collider))
					PointEntity(pvt, Collider)
					RotateEntity(pvt, Min(EntityPitch(pvt), 20), EntityYaw(pvt), 0)
					
					RotateEntity(n\Collider, CurveAngle(EntityPitch(pvt), EntityPitch(n\Collider), 10), CurveAngle(EntityYaw(pvt), EntityYaw(n\Collider), 10), 0, True)
					
					PositionEntity(pvt, EntityX(n\Collider), EntityY(n\Collider)+0.8, EntityZ(n\Collider))
					PointEntity(pvt, Collider)
					RotateEntity(pvt, Min(EntityPitch(pvt), 40), EntityYaw(n\Collider), 0)
					
					If n\Reload = 0 Then
						n\ShootSFX = LoadSound_Strict("SFX\Guns\"+n\Gun\Name+"\shoot"+Rand(1,4)+".ogg")
						n\ShootSFXCHN = PlaySound2(n\ShootSFX,Camera,n\Collider,20)
						ShootPlayer(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 1, True, 100)
						n\Reload = 1000
					EndIf
					
					;SetNPCFrame(n,302)
				EndIf
			EndIf
			;[End Block]
		Default
			;[Block]
			If Rand(400) = 1 Then
				n\PrevState = Rnd(-30, 30)
			EndIf
			n\PathStatus = 0
			AnimateNPC(n,77,201,0.2)
			
			RotateEntity(n\Collider, 0, CurveAngle(n\Angle + n\PrevState + Sin(MilliSecs() / 50) * 2, EntityYaw(n\Collider), 50), 0, True)
			;[End Block]
	End Select
	
	If n\CurrSpeed > 0.01 Then
		If prevFrame < 5 And n\Frame >= 5 Then
			PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.5,0.7))						
		ElseIf prevFrame < 23 And n\Frame >= 23 Then
			PlaySound2(StepSFX(2,0,Rand(0,2)),Camera, n\Collider, 8.0, Rnd(0.5,0.7))						
		EndIf
	EndIf
	
	If n\Frame > 286.5 And n\Frame < 288.5 Then
		n\IsDead = True
	EndIf
	
	n\Reload = Max(0, n\Reload - FPSfactor)
	PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.2, EntityZ(n\Collider))
	
	RotateEntity n\obj, 0, EntityYaw(n\Collider)+180, 0
	
End Function

Function CreateNPCTypeVincent(n.NPCs)
	Local temp#
	
	n\NPCName = "Vincent"
	n\NVName = "Human"
	n\Collider = CreatePivot()
	EntityRadius n\Collider, 0.2
	EntityType n\Collider, HIT_PLAYER
	n\obj = CopyEntity(NPCModel[Model_Vincent])
	
	n\Speed = (GetINIFloat("DATA\NPCs.ini", "Guard", "speed") / 100.0)
	temp# = (GetINIFloat("DATA\NPCs.ini", "Guard", "scale_small") / 2.5)
	
	ScaleEntity n\obj, temp, temp, temp
	
	MeshCullBox (n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)
	
	CopyHitBoxes(n)
	
	;n\HP = 40
	
End Function

Function UpdateNPCTypeVincent(n.NPCs)
	Local g.Guns,it.Items
	Local prevFrame# = AnimTime(n\obj)
	Local isDead%
	
	Select n\State[0]
		Case 0
			;If n\HP > 0 Then
				AnimateNPC(n,1,300,0.3,True)
;			Else
;				isDead = True
;			EndIf
		Case 1
			;If n\HP > 0 Then
				AnimateNPC(n,342,641,0.3,False)
;			Else
;				isDead = True
;			EndIf
		Case 2
			SetNPCFrame(n, 342)
		Case STATE_SCRIPT
			
	End Select
	
;	If n\HP < 1 Then
;		isDead = True
;		EntityType n\Collider, HIT_DEAD
;		If n\State[0] = 0 Then
;			If n\Frame = 341 Then
;				SetNPCFrame(n, 341)
;			Else
;				AnimateNPC(n, 301, 341, 0.5, False)
;			EndIf
;		ElseIf n\State[0] = 1 Then
;			If n\Frame = 788 Then
;				SetNPCFrame(n, 788)
;			Else
;				AnimateNPC(n, 678, 788, 0.4, False)
;			EndIf
;		EndIf
;		If n\SoundChn <> 0
;			StopChannel n\SoundChn
;			n\SoundChn = 0
;			FreeSound_Strict n\Sound
;			n\Sound = 0
;		EndIf
;		If n\Gun <> Null And n\Frame >= 1390-0.5 Then
;			Local bone% = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCName, "weapon_hand_bonename"))
;			For g = Each Guns
;				If g\ID = n\Gun\ID Then
;					it = CreateItem(g\DisplayName, g\name, EntityX(bone%, True), EntityY(bone%, True) + 0.025, EntityZ(bone%, True))
;					EntityType it\collider, HIT_ITEM
;					it\state = n\Gun\Ammo
;					it\state2 = Rand(0,2)
;					it\Dropped = 1
;					Exit
;				EndIf
;			Next
;			RemoveNPCGun(n)
;		EndIf
;	EndIf
	
	PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.2, EntityZ(n\Collider))
	
	RotateEntity n\obj, 0, EntityYaw(n\Collider)+130, 0
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D