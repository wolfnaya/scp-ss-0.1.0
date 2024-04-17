; ~ NPC constants

;[Block]
Const NPCDistanceCheckTime# = 70*2
Const MaxZombieTypes = 6
Const MaxGuardZombieTypes = 3
Const MaxEnemyCount = 20
Const MinEnemyLeft = 5
;[End Block]

Function ShouldSyncNPC(n.NPCs)
	
	If n\NPCtype = NPC_SCP_035_Tentacle And n\Target <> Null Then
		Return False
	EndIf
	
	Return True
	
End Function

Type EnemySpawner
	Field obj%
	Field time#
	Field enemies$
End Type

Function CreateEnemySpawner.EnemySpawner(x#,y#,z#,enemyString$)
	Local ens.EnemySpawner = New EnemySpawner
	
	ens\obj = CreatePivot()
	PositionEntity ens\obj,x#,y#,z#
	ens\enemies = enemyString
	
	Return ens
End Function

Function UpdateEnemySpawners()
	Local ens.EnemySpawner,n.NPCs
	Local NPCtoSpawnType% = -1
	Local spawn_chance#
	
	For ens = Each EnemySpawner
		If ens\time <= 0.0 Then
			NPCtoSpawnType = -1
			Local EnemyCount% = 0
			Local EnemyCount2% = 0
			Local EnemyCount939% = 0
			Local EnemyCount1048% = 0
			For n.NPCs = Each NPCs
				EnemyCount = EnemyCount + 1
				If (Not n\IsDead) Then
					EnemyCount2 = EnemyCount2 + 1
				EndIf
				If n\NPCtype = NPC_SCP_939 Then
					EnemyCount939 = EnemyCount939 + 1
				ElseIf n\NPCtype = NPC_SCP_1048 Then
					EnemyCount1048 = EnemyCount1048 + 1
				EndIf
			Next
			
			If EnemyCount <= MaxEnemyCount And EnemyCount2 < mp_I\Gamemode\EnemyCount Then ;Only spawn an enemy if the maximum amount of enemies doesn't get overexceeded
				Repeat
					spawn_chance = Rand(1,100)
					If spawn_chance < 80 Then
						If Instr(ens\enemies,"zombie")>0 Then
							If (mp_I\BossNPC = Null And (mp_I\Gamemode\Phase / 2) = mp_I\Gamemode\MaxPhase) Then
								If mp_I\Gamemode\ID = Gamemode_EAF Then
									NPCtoSpawnType = NPC_NTF_HDS_Boss
								Else
									Select mp_I\MapInList\BossNPC
										Case "SCP-035"
											NPCtoSpawnType = NPC_SCP_035
										Case "SCP-076"
											NPCtoSpawnType = NPC_SCP_076
										Case "SCP-457"
											NPCtoSpawnType = NPC_SCP_457
									End Select
								EndIf
							Else
								NPCtoSpawnType = NPC_Zombie
							EndIf
							Exit
						EndIf
					ElseIf spawn_chance >= 80 And spawn_chance < 90 Then
						If Instr(ens\enemies,"939")>0 Then ;On wave 5 (out of 6), on wave 7 (out of 9), on wave 9 (out of 12)
							If EnemyCount939 < mp_I\PlayerCount+1 And ((Float(mp_I\Gamemode\Phase / 2) / mp_I\Gamemode\MaxPhase > 0.7 And (mp_I\Gamemode\Phase/2) <> mp_I\Gamemode\MaxPhase) Lor mp_I\BossNPC <> Null) Then
								NPCtoSpawnType = NPC_SCP_939
							Else
								NPCtoSpawnType = -1
							EndIf
							Exit
						EndIf
					Else
						If Instr(ens\enemies,"1048a")>0 Then ;On wave 3 (out of 6), on wave 4 (out of 9), on wave 5 (out of 12)
							If EnemyCount1048 < mp_I\PlayerCount*2 And ((Float(mp_I\Gamemode\Phase / 2) / mp_I\Gamemode\MaxPhase > 0.4 And (mp_I\Gamemode\Phase/2) <> mp_I\Gamemode\MaxPhase) Lor mp_I\BossNPC <> Null) Then
								NPCtoSpawnType = NPC_SCP_1048
							Else
								NPCtoSpawnType = -1
							EndIf
							Exit
						EndIf
					EndIf
				Forever
				
				Select NPCtoSpawnType
					Case -1
						;Skip
					Case NPC_Zombie
						If mp_O\HardcoreMP Then
							n = CreateNPC(NPC_Zombie_Armed,EntityX(ens\obj),EntityY(ens\obj)+0.5,EntityZ(ens\obj))
							n\State[5] = 1
						Else
							If Rand(10)<10 Then
								n = CreateNPC(NPC_Zombie,EntityX(ens\obj),EntityY(ens\obj)+0.5,EntityZ(ens\obj))
							Else
								n = CreateNPC(NPC_Zombie_Armed,EntityX(ens\obj),EntityY(ens\obj)+0.5,EntityZ(ens\obj))
							EndIf
						EndIf
						TeleportEntity(n\Collider,EntityX(ens\obj),EntityY(ens\obj)+0.5,EntityZ(ens\obj))
					Case NPC_SCP_1048
						If mp_I\Gamemode\ID = Gamemode_EAF Then
							If Rand(5)<5 Then
								n = CreateNPC(NPC_Scientist,EntityX(ens\obj),EntityY(ens\obj)+0.5,EntityZ(ens\obj))
							Else
								n = CreateNPC(NPC_Guard,EntityX(ens\obj),EntityY(ens\obj)+0.5,EntityZ(ens\obj))
							EndIf
						Else
							n = CreateNPC(NPC_SCP_1048,EntityX(ens\obj),EntityY(ens\obj)+0.5,EntityZ(ens\obj))
						EndIf
						TeleportEntity(n\Collider,EntityX(ens\obj),EntityY(ens\obj)+0.5,EntityZ(ens\obj))
					Case NPC_SCP_939
						If mp_I\Gamemode\ID = Gamemode_EAF Then
							If Rand(5)<5 Then
								n = CreateNPC(NPC_NTF,EntityX(ens\obj),EntityY(ens\obj)+0.5,EntityZ(ens\obj))
							Else
								n = CreateNPC(NPC_NTF_HDS,EntityX(ens\obj),EntityY(ens\obj)+0.5,EntityZ(ens\obj))
							EndIf
						Else
							n = CreateNPC(NPC_SCP_939,EntityX(ens\obj),EntityY(ens\obj)+0.5,EntityZ(ens\obj))
						EndIf
						TeleportEntity(n\Collider,EntityX(ens\obj),EntityY(ens\obj)+0.5,EntityZ(ens\obj))
					Default
						n = CreateNPC(NPCtoSpawnType,EntityX(ens\obj),EntityY(ens\obj)+0.5,EntityZ(ens\obj))
						TeleportEntity(n\Collider,EntityX(ens\obj),EntityY(ens\obj)+0.5,EntityZ(ens\obj))
				End Select
				ens\time = 70*5 ;70*15
			EndIf
		Else
			ens\time = ens\time - FPSfactor
		EndIf
	Next
	
End Function

Function UpdateNPCsServer()
	CatchErrors("UpdateNPCsServer")
	Local n.NPCs,i%,dist#,w.WayPoints,n2.NPCs,j%
	Local prevDeathState%,IsUpdating%
	
	For n = Each NPCs
		;A variable to determine if the NPC is in the facility or not
		n\InFacility = CheckForNPCInFacility(n)
		
		prevDeathState = n\IsDead
		
		If Players[n\ClosestPlayer] = Null Lor Players[n\ClosestPlayer]\CurrHP <= 0 Lor n\DistanceTimer <= 0.0 Then
			n\ClosestPlayer = GetClosestPlayerID(n)
		EndIf
		
		Select n\NPCtype
			Case NPC_SCP_035
				UpdateNPCtype035MP(n)
			Case NPC_SCP_076
				UpdateNPCtype076MP(n)
			Case NPC_SCP_1048
				UpdateNPCtype1048aMP(n)
			Case NPC_SCP_457
				UpdateNPCtype457MP(n)
			Case NPC_SCP_939
				UpdateNPCtype939MP(n)
			Case NPC_SCP_106
				UpdateNPCtype106MP(n)
			Case NPC_SCP_035_Tentacle
				UpdateNPCtypeTentacleMP(n)
			Case NPC_Zombie, NPC_Zombie_Armed
				UpdateNPCtypeZombieMP(n)
			Case NPC_Guard, NPC_NTF, NPC_NTF_HDS
				UpdateNPCtypeNTFMP(n)
			Case NPC_NTF_HDS_Boss
				UpdateNPCtypeHDSBoss(n)
			Case NPC_Scientist
				UpdateNPCtypeScientist(n)
		End Select
		
		IsUpdating% = False
		For i = 0 To (mp_I\MaxPlayers-1)
			If Players[i]<>Null Then
				If Players[i]\CurrHP > 0 Then
					IsUpdating = True
					Exit
				EndIf
			EndIf
		Next
		
		If n<>Null Then
			If (Not IsUpdating) Then
				n\State[0] = -1 ;This is considered as the "freezing" state
			Else
				If (Not n\IsDead) And (n\NPCtype<>NPC_SCP_1048) And (n\NPCtype<>NPC_SCP_035_Tentacle) Then
					For n2.NPCs = Each NPCs
						If n2<>n And n2\IsDead=False And (n2\NPCtype<>NPC_SCP_1048 And n2\NPCtype<>NPC_SCP_939 And n2\NPCtype<>NPC_SCP_035_Tentacle) Then
							If n2\ID>n\ID Then
								If Abs(DeltaYaw(n\Collider,n2\Collider))<80.0 Then
									If EntityDistanceSquared(n\Collider,n2\Collider)<(n\CollRadius+n2\CollRadius)*0.5 Then							
										TranslateEntity n2\Collider, Cos(EntityYaw(n\Collider,True)+90)* 0.01 * FPSfactor, 0, Sin(EntityYaw(n\Collider,True)+90)* 0.01 * FPSfactor, True
									EndIf
								EndIf
							Else
								If EntityDistanceSquared(n\Collider,n2\Collider)<(n\CollRadius+n2\CollRadius)*0.5 Then
									TranslateEntity n\Collider, Cos(EntityYaw(n\Collider,True)-45)* 0.01 * FPSfactor, 0, Sin(EntityYaw(n\Collider,True)-45)* 0.01 * FPSfactor, True
								EndIf
							EndIf
						EndIf
					Next
				EndIf
				
				If n\NPCtype <> NPC_SCP_035_Tentacle Then
					TranslateEntity n\Collider, 0, n\DropSpeed, 0
					
					Local CollidedFloor% = False
					For i% = 1 To CountCollisions(n\Collider)
						If CollisionY(n\Collider, i) < EntityY(n\Collider) - 0.01 Then CollidedFloor = True : Exit
					Next
					
					If CollidedFloor = True Then
						n\DropSpeed# = 0
					Else
						n\DropSpeed# = Max(n\DropSpeed - 0.005*FPSfactor*n\GravityMult,-n\MaxGravity)
					EndIf
				EndIf
				
				If mp_I\BossNPC <> Null Then
					If n = mp_I\BossNPC Then
						If n\IsDead Then
							For n2 = Each NPCs
								n2\HP = 0
							Next
							mp_I\BossNPC = Null
							mp_I\Gamemode\EnemyCount = 0
							EndGameForVoting()
						EndIf
					EndIf
				EndIf
				
				n\GotHit = 0
				n\KilledBy = 0
				
				If mp_I\PlayState = GAME_SERVER Then
					If prevDeathState<>n\IsDead And mp_I\BossNPC = Null Then
						mp_I\Gamemode\EnemyCount = mp_I\Gamemode\EnemyCount - 1
					EndIf
					
					If n\DeleteTimer <= 0.0 Then
						n\DeleteTimer = 70*5.0
						If EntityY(n\Collider) < EntityY(mp_I\Map\obj) - 10.0 Then
							RemoveNPC(n)
						EndIf
					Else
						n\DeleteTimer = n\DeleteTimer - FPSfactor
					EndIf
				EndIf
			EndIf	
		EndIf
	Next
	
	CatchErrors("Uncaught (UpdateNPCsServer)")
End Function

Function GetClosestPlayerID(n.NPCs)
	
	Local smallestdist% = GetClosestPlayerIDFromEntity(n\Collider)
	Return smallestdist
	
End Function

Function TeleportCloserMP(n.NPCs)
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
					If (EntityDistanceSquared(Players[n\ClosestPlayer]\Collider, w\obj)>PowTwo(16)) Then
						;teleports to the nearby waypoint that takes it closest to the player
						Local newDist# = EntityDistanceSquared(Players[n\ClosestPlayer]\Collider, w\obj)
						If (newDist < closestDist Lor closestWaypoint = Null) Then
							closestDist = newDist
							closestWaypoint = w
						EndIf
					EndIf
				EndIf
			EndIf
		EndIf
	Next
	
	If (closestWaypoint<>Null) Then
		PositionEntity n\Collider, EntityX(closestWaypoint\obj,True), EntityY(closestWaypoint\obj,True)+0.15, EntityZ(closestWaypoint\obj,True), True
		ResetEntity n\Collider
		n\PathStatus = 0
		n\PathTimer = 0.0
		n\PathLocation = 0
		n\DistanceTimer = 0.0
		n\State[0] = 0
		n\DropSpeed = 0.0
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D