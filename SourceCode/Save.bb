
Function SaveGame(File$, Auto% = False, NewZone%= -1)
	
	If IsZombie Lor psp\Health = 0 Then Return ; ~ Don't save if the player can't move at all
	
	If DropSpeed > 0.02 * FPSfactor Lor DropSpeed < (-0.02) * FPSfactor Then Return
	
	CatchErrors("SaveGame(" + File + ", " + NewZone + ")")
	
	GameSaved = True
	
	If FileType(File) <> 2 Then
		CreateDir(File)
	EndIf
	
	Local SaveName$ = Replace(Replace(File, SavePath, ""), "\", "")
	PutINIValue(gv\OptionFile, "options", "last save", SaveName)
	
	Local f% = WriteFile(File + "main.sav")
	
	WriteString f, CompatibleNumber
	WriteString f, CurrentTime()
	WriteString f, CurrentDate()
	WriteByte f, gopt\GameMode + 1
	WriteInt f, ClassDNumber
	WriteString f, gopt\CurrZoneString
	
	WriteInt f, PlayTime
	WriteFloat f, EntityX(Collider)
	WriteFloat f, EntityY(Collider)
	WriteFloat f, EntityZ(Collider)
	
	WriteFloat f, EntityX(Head)
	WriteFloat f, EntityY(Head)
	WriteFloat f, EntityZ(Head)
	
	WriteFloat f, EntityPitch(Collider)
	WriteFloat f, EntityYaw(Collider)
	
	; ~ Player
	;[Block]
	Local x#, y#, z#, i%, j%, temp%, temp2%, strtemp$, npv%
	Local g.Guns, itt.ItemTemplates, it2.Items, it.Items, t.NewTask, n.NPCs
	
	WriteByte f, ecst\SuccessRocketLaunch
	WriteByte f, ecst\EzDoorOpened
	WriteByte f, ecst\WasInHCZ
	WriteByte f, ecst\NewCavesEvent
	WriteByte f, ecst\CIArrived
	WriteByte f, ecst\WasInRoom2_SL
	WriteByte f, ecst\WasInLCZCore
	WriteByte f, ecst\UnlockedGateDoors
	WriteByte f, ecst\NTFArrived
	WriteByte f, ecst\WasInO5
	WriteByte f, ecst\WasIn076
	WriteFloat f, ecst\After076Timer
	WriteByte f, ecst\WasInCaves
	WriteByte f, ecst\WasInO5Again
	WriteByte f, ecst\WasInPO
	WriteByte f, ecst\WasInReactor
	WriteByte f, ecst\WasInBCZ
	WriteByte f, ecst\Contained008
	WriteByte f, ecst\Contained049
	WriteByte f, ecst\Contained409
	
	WriteByte f, ecst\UnlockedAirlock
	
	WriteByte f, ecst\UnlockedWolfnaya
	WriteInt f, ecst\ChanceToSpawnWolfNote
	
	WriteInt f, ecst\FusesAmount
	
	WriteByte f, ecst\UnlockedEMRP
	WriteByte f, ecst\UnlockedHDS
	
	WriteByte f, ecst\WasInLWS
	WriteByte f, ecst\WasInWS
	WriteByte f, ecst\WasInEWS
	WriteByte f, ecst\WasInHWS
	WriteByte f, ecst\WasInSWS
	WriteByte f, ecst\WasInAllSupplies
	
	WriteByte f, ecst\IntercomEnabled
	WriteByte f, ecst\IntercomIsReady
	WriteFloat f, ecst\IntercomTimer
	
	WriteByte f, ecst\OmegaWarheadActivated
	WriteByte f, ecst\OmegaWarheadDetonate
	WriteFloat f, ecst\OmegaWarheadTimer	
	
;	For i = 0 To 3
;		WriteString f, Int(AccessCode[i])
;	Next
	WriteFloat f, BlinkTimer
	WriteFloat f, BlinkEffect
	WriteFloat f, BlinkEffectTimer
	
	WriteInt f, DeathTimer
	WriteInt f, BlurTimer
	WriteFloat f, HealTimer
	
	WriteByte f, OnSafety
	
	For g = Each Guns
		WriteInt f, g\CurrAmmo
		WriteInt f, g\CurrReloadAmmo
		WriteByte f, g\Found
		WriteFloat f, g\JamTimer
		WriteFloat f, g\JamTimer2
		WriteFloat f, g\JamAmount
	Next
	WriteByte f, AttachmentMenuOpen
	WriteInt f, g_I\HoldingGun
	WriteFloat f, psp\Health
	WriteFloat f, psp\Kevlar
	WriteFloat f, psp\Helmet
	WriteByte f, psp\NoMove
	WriteByte f, psp\NoRotation
	WriteByte f, psp\IsShowingHUD
	WriteByte f, g_I\Weapon_CurrSlot
	For i = 0 To MaxGunSlots-1
		WriteString f, g_I\Weapon_InSlot[i]
	Next
	For g = Each Guns
		WriteInt f, g\BarrelAttachments
		WriteInt f, g\MountAttachments
		WriteInt f, g\GripAttachments
		WriteInt f, g\MagazineAttachments
		WriteInt f, g\MiscAttachments
		For i = 0 To MaxAttachments - 1
			WriteByte f, g\HasAttachments[i]
			WriteByte f, g\HasToggledAttachments[i]
			WriteByte f, g\HasPickedAttachments[i]
		Next
	Next
	
	WriteByte f, psp\Checkpoint106Passed
	
	For t = Each NewTask
		If t\Status <> TASK_STATUS_END Then
			WriteByte f, 1
			WriteInt f, t\ID
		EndIf
	Next
	WriteByte f, 0
	
	WriteByte f, mtfd\Enabled
	WriteByte f, mtfd\IsPlaying
	WriteByte f, mtfd\CurrentProgress
	WriteByte f, mtfd\PrevDialogue
	WriteByte f, mtfd\CurrentDialogue
	WriteInt f, mtfd\CurrentSequence
	WriteFloat f, mtfd\Timer
	
	WriteByte f, Crouch
	
	WriteFloat f, Stamina
	WriteFloat f, StaminaEffect
	WriteFloat f, StaminaEffectTimer
	
	WriteFloat f, EyeStuck
	WriteFloat f, EyeIrritation
	
	WriteString f, m_msg\DeathTxt
	
	WriteInt f, I_005\ChanceToSpawn
	WriteByte f, I_035\Possessed
	WriteByte f, I_207\Limit
	WriteByte f, I_500\Limit
	WriteFloat f, I_008\Timer
	WriteFloat f, I_207\Timer
	WriteFloat f, I_207\DeathTimer
	WriteFloat f, I_207\Factor
	WriteFloat f, I_198\Timer
	WriteFloat f, I_198\DeathTimer
	WriteFloat f, I_198\Vomit
	WriteFloat f, I_198\VomitTimer
	WriteFloat f, I_198\Injuries
	WriteInt f, I_109\Used
    WriteFloat f, I_109\Timer
	WriteByte f, I_268\Using
    WriteFloat f, I_268\Timer
	WriteByte f, I_1033RU\Using
	WriteInt f, I_1033RU\HP
	WriteInt f, I_1033RU\DHP
	WriteFloat f, I_330\Taken
	WriteFloat f, I_330\Timer
	
	WriteFloat f, VomitTimer
	WriteByte f, Vomit
	WriteFloat f, CameraShakeTimer
	
	For i = 0 To ESOTERIC
		If (SelectedDifficulty = difficulties[i]) Then
			WriteByte f, i
			
			If (i = ESOTERIC) Then
				SelectedDifficulty\AggressiveNPCs = ReadByte(f)
				SelectedDifficulty\PermaDeath = ReadByte(f)
				SelectedDifficulty\SaveType	= ReadByte(f)
				SelectedDifficulty\OtherFactors = ReadByte(f)
			EndIf
		EndIf
	Next
	
	WriteFloat f, CameraFogFar
    WriteFloat f, StoredCameraFogFar
	
	WriteFloat f, Sanity
	
	WriteByte f, IsCutscene
	
	WriteByte f, mpl\HasNTFGasmask
	WriteByte f, mpl\ShowPlayerModel
	WriteByte f, hds\isBroken
	WriteByte f, hds\CantWear
	WriteFloat f, hds\Timer
	WriteFloat f, hds\Health
	WriteByte f, I_427\Using
	WriteFloat f, I_427\Timer
	WriteByte f, I_714\Using
	WriteByte f, mpl\NightVisionEnabled
	
	WriteByte f, SuperMan
	WriteFloat f, SuperManTimer
	WriteByte f, LightsOn
	
	WriteFloat f, SecondaryLightOn
	WriteFloat f, PrevSecondaryLightOn
	WriteByte f, RemoteDoorOn
	WriteByte f, SoundTransmission
	
	WriteInt f, RefinedItems
	
	WriteFloat f, MTFtimer
	WriteFloat f, CITimer
	
	For it = Each Items
		If IsItemInInventory(it) Then
			WriteByte f, 1
			WriteString f, it\itemtemplate\name
			WriteString f, it\itemtemplate\tempname
			WriteString f, it\name
			WriteFloat f, EntityX(it\collider, True)
			WriteFloat f, EntityY(it\collider, True)
			WriteFloat f, EntityZ(it\collider, True)
			WriteByte f, it\r
			WriteByte f, it\g
			WriteByte f, it\b
			WriteFloat f, it\a
			WriteFloat f, EntityPitch(it\collider)
			WriteFloat f, EntityYaw(it\collider)
			WriteFloat f, it\state
			If SelectedItem = it Then WriteByte f, 1 Else WriteByte f, 0
			If it\itemtemplate\isAnim<>0 Then
				WriteFloat f, AnimTime(it\model)
			EndIf
			WriteByte f,it\invSlots
			WriteInt f,it\ID
			If it\itemtemplate\invimg=it\invimg Then WriteByte f,0 Else WriteByte f,1
		EndIf
	Next
	WriteByte f, 0
	
	For n = Each NPCs
		If (n\NPCtype = NPC_NTF And n\HP > 0) Lor (n\NPCtype = NPC_SCP_173 And n\Idle = SCP173_BOXED) Then
			WriteByte f, 1
			
			;debuglog("Saving NPC " +n\NVName+ " (ID "+n\ID+")")
			
			WriteByte f, n\NPCtype
			
			WriteFloat f, EntityX(n\Collider,True)
			WriteFloat f, EntityY(n\Collider,True)
			WriteFloat f, EntityZ(n\Collider,True)
			
			WriteFloat f, EntityPitch(n\Collider)
			WriteFloat f, EntityYaw(n\Collider)
			WriteFloat f, EntityRoll(n\Collider)
			
			For npv = 0 To MaxNPCStates - 1
				WriteFloat f, n\State[npv]
			Next
			WriteInt f, n\PrevState
			
			WriteByte f, n\Idle
			WriteFloat f, n\IdleTimer
			WriteFloat f, n\LastDist
			WriteInt f, n\LastSeen
			
			WriteInt f, n\CurrSpeed
			
			WriteFloat f, n\Angle
			
			WriteFloat f, n\Reload
			
			WriteInt f, n\ID
			If n\Target <> Null Then
				WriteInt f, n\Target\ID		
			Else
				WriteInt f, 0
			EndIf
			
			WriteFloat f, n\EnemyX
			WriteFloat f, n\EnemyY
			WriteFloat f, n\EnemyZ
			
			WriteString f, n\texture
			
			WriteFloat f, AnimTime(n\obj)
			
			WriteInt f, n\IsDead
			WriteFloat f, n\PathX
			WriteFloat f, n\PathZ
			WriteInt f, n\HP
			WriteString f, n\Model
			WriteFloat f, n\ModelScaleX#
			WriteFloat f, n\ModelScaleY#
			WriteFloat f, n\ModelScaleZ#
			WriteInt f, n\TextureID
			
			If n\Gun <> Null Then
				WriteInt f, n\Gun\ID
				WriteInt f, n\Gun\Ammo
			Else
				WriteInt f, GUN_UNARMED
			EndIf
		EndIf
	Next
	WriteByte f, 0
	
	For i = 0 To MaxInventorySpace-1
		If Inventory[i] <> Null Then
			WriteInt f, Inventory[i]\ID
			If Inventory[i]\invSlots > 0 Then
				For j = 0 To Inventory[i]\invSlots-1
					If Inventory[i]\SecondInv[j] <> Null Then
						WriteInt f, Inventory[i]\SecondInv[j]\ID
					Else
						WriteInt f, -1
					EndIf
				Next
			EndIf
		Else
			WriteInt f, -1
		EndIf
	Next
	
	For itt.ItemTemplates = Each ItemTemplates
		WriteByte f, itt\found
	Next
	
	WriteByte f, UsedConsole
	;[End Block]
	
	If NewZone > -1 Then
		WriteInt f, NewZone
	Else
		WriteInt f, gopt\CurrZone
	EndIf
	
	CloseFile f
	
	SaveZoneData(File)
	
	If (Not Auto) Then
		If Not MenuOpen Then
			If SelectedDifficulty\SaveType = SAVEONSCREENS Then
				PlaySound_Strict(LoadTempSound("SFX\General\Save2.ogg"))
			Else
				PlaySound_Strict(LoadTempSound("SFX\General\Save1.ogg"))
			EndIf
			
			CreateHintMsg(GetLocalString("Menu", "progress_saved"))
		EndIf
	Else
		CreateAutoSaveMsg()
	EndIf
	
	CatchErrors("Uncaught (SaveGame(" + File + ", " + NewZone + "))")
End Function

Function SaveZoneData(file$)
	Local n.NPCs, r.Rooms, em.Emitters, do.Doors, it.Items, fb.FuseBox, ne.NewElevator
	Local f%, temp%, i%, j%, s%
	
	If FileType(file) <> 2 Then
		CreateDir(file)
	EndIf
	
	f% = WriteFile(file + "\" + gopt\CurrZone + ".sav")
	
	WriteString f, RandomSeed
	
	temp = 0
	For  n.NPCs = Each NPCs
		If (n\NPCtype <> NPC_NTF Lor n\HP <= 0) And (n\NPCtype <> NPC_SCP_173 Lor n\Idle <> SCP173_BOXED) Then
			temp = temp + 1
		EndIf
	Next
	
	WriteInt f, temp
	For n.NPCs = Each NPCs
		If (n\NPCtype <> NPC_NTF Lor n\HP <= 0) And (n\NPCtype <> NPC_SCP_173 Lor n\Idle <> SCP173_BOXED) Then
			;debuglog("Saving NPC " +n\NVName+ " (ID "+n\ID+")")
			
			WriteByte f, n\NPCtype
			WriteFloat f, EntityX(n\Collider,True)
			WriteFloat f, EntityY(n\Collider,True)
			WriteFloat f, EntityZ(n\Collider,True)
			
			WriteFloat f, EntityPitch(n\Collider)
			WriteFloat f, EntityYaw(n\Collider)
			WriteFloat f, EntityRoll(n\Collider)
			
			For i = 0 To MaxNPCStates - 1
				WriteFloat f, n\State[i]
			Next
			WriteInt f, n\PrevState
			
			WriteByte f, n\Idle
			WriteFloat f, n\IdleTimer
			WriteFloat f, n\LastDist
			WriteInt f, n\LastSeen
			
			WriteInt f, n\CurrSpeed
			
			WriteFloat f, n\Angle
			
			WriteFloat f, n\Reload
			
			WriteInt f, n\ID
			If n\Target <> Null Then
				WriteInt f, n\Target\ID		
			Else
				WriteInt f, 0
			EndIf
			
			WriteFloat f, n\EnemyX
			WriteFloat f, n\EnemyY
			WriteFloat f, n\EnemyZ
			
			WriteString f, n\texture
			
			WriteFloat f, AnimTime(n\obj)
			
			WriteInt f, n\IsDead
			WriteFloat f, n\PathX
			WriteFloat f, n\PathZ
			WriteInt f, n\HP
			WriteString f, n\Model
			WriteFloat f, n\ModelScaleX#
			WriteFloat f, n\ModelScaleY#
			WriteFloat f, n\ModelScaleZ#
			WriteInt f, n\TextureID
			
			If n\Gun <> Null Then
				WriteInt f, n\Gun\ID
				WriteInt f, n\Gun\Ammo
			Else
				WriteInt f, GUN_UNARMED
			EndIf
		EndIf
	Next
	
	For i = 0 To 6
		If MTFrooms[0]<>Null Then 
			WriteString f, MTFrooms[0]\RoomTemplate\Name 
		Else 
			WriteString f,	"a"
		EndIf
		WriteInt f, MTFroomState[i]
	Next
	
	WriteInt f, room2gw_brokendoor
	WriteFloat f,room2gw_x
	WriteFloat f,room2gw_z
	
	WriteFloat f, PlayerRoom\x
	WriteFloat f, PlayerRoom\z
	For r = Each Rooms
		WriteByte f, r\found
		
		For i = 0 To 11
			If r\NPC[i]=Null Then
				WriteInt f, 0
			Else
				WriteInt f, r\NPC[i]\ID
			EndIf
		Next
		
		For i=0 To 10
			If r\Levers[i]=Null Then
				WriteByte(f,2)
			Else
				If EntityPitch(r\Levers[i]\obj,True) > 0 Then
					WriteByte(f,1)
				Else
					WriteByte(f,0)
				EndIf	
			EndIf
		Next
		
		temp = 0
		For em = Each Emitters
			If (Not em\map_generated) And em\Room = r Then
				temp = temp + 1
			EndIf
		Next
		WriteInt f, temp
		For em = Each Emitters
			If (Not em\map_generated) And em\Room = r Then
				WriteFloat f, EntityX(em\Obj, True)
				WriteFloat f, EntityY(em\Obj, True)
				WriteFloat f, EntityZ(em\Obj, True)
				WriteInt f, em\emittertype
				WriteFloat f, EntityPitch(em\Obj)
				WriteFloat f, EntityYaw(em\Obj)
				WriteFloat f, EntityRoll(em\Obj)
				WriteFloat f, em\Size
				WriteFloat f, em\SizeChange
				WriteFloat f, em\Speed
				WriteFloat f, em\RandAngle
				WriteByte f, em\Disable
			EndIf
		Next
	Next
	
	temp = 0
	For do.Doors = Each Doors
		temp = temp + 1	
	Next	
	WriteInt f, temp	
	For do.Doors = Each Doors
		WriteFloat f, EntityX(do\frameobj,True)
		WriteFloat f, EntityY(do\frameobj,True)
		WriteFloat f, EntityZ(do\frameobj,True)
		WriteByte f, do\open
		WriteFloat f, do\openstate
		WriteByte f, do\locked
		WriteByte f, do\AutoClose
		
		WriteFloat f, EntityX(do\obj, True)
		WriteFloat f, EntityZ(do\obj, True)
		
		If do\obj2 <> 0 Then
			WriteFloat f, EntityX(do\obj2, True)
			WriteFloat f, EntityZ(do\obj2, True)
		Else
			WriteFloat f, 0.0
			WriteFloat f, 0.0
		End If
		
		WriteFloat f, do\timer
		WriteFloat f, do\timerstate
		
		WriteByte f, do\IsElevatorDoor
		WriteByte f, do\MTFClose
	Next
	
	Local d.Decals
	temp = 0
	For d.Decals = Each Decals
		temp = temp+1
	Next	
	WriteInt f, temp
	For d.Decals = Each Decals
		WriteInt f, d\ID
		
		WriteFloat f, EntityX(d\obj,True)
		WriteFloat f, EntityY(d\obj,True)
		WriteFloat f, EntityZ(d\obj,True)
		
		WriteFloat f, EntityPitch(d\obj,True)
		WriteFloat f, EntityYaw(d\obj,True)
		WriteFloat f, EntityRoll(d\obj,True)
		
		WriteByte f, d\blendmode
		WriteInt f, d\fx
		
		WriteFloat f, d\Size
		WriteFloat f, d\Alpha
		WriteFloat f, d\AlphaChange
		WriteFloat f, d\Timer
		WriteFloat f, d\lifetime
	Next
	
	Local e.Events
	temp = 0
	For e.Events = Each Events
		temp=temp+1
	Next	
	WriteInt f, temp
	For e.Events = Each Events
		WriteString f, e\EventName
		For s = 0 To MaxEventStates - 1
			WriteFloat f, e\EventState[s]
		Next
		WriteFloat f, EntityX(e\room\obj)
		WriteFloat f, EntityZ(e\room\obj)
		WriteString f, e\EventStr
	Next
	
	For it = Each Items
		If (Not IsItemInInventory(it)) Then
			WriteByte f, 1
			WriteString f, it\itemtemplate\name
			WriteString f, it\itemtemplate\tempname
			WriteString f, it\name
			WriteFloat f, EntityX(it\collider, True)
			WriteFloat f, EntityY(it\collider, True)
			WriteFloat f, EntityZ(it\collider, True)
			WriteByte f, it\r
			WriteByte f, it\g
			WriteByte f, it\b
			WriteFloat f, it\a
			WriteFloat f, EntityPitch(it\collider)
			WriteFloat f, EntityYaw(it\collider)
			WriteFloat f, it\state
			If it\itemtemplate\isAnim<>0 Then
				WriteFloat f, AnimTime(it\model)
			EndIf
			WriteByte f,it\invSlots
			WriteInt f,it\ID
			If it\itemtemplate\invimg=it\invimg Then WriteByte f,0 Else WriteByte f,1
		EndIf
	Next
	WriteByte f, 0
	
	For it = Each Items
		If (Not IsItemInInventory(it)) And it\invSlots > 0 Then
			For i = 0 To it\invSlots-1
				If it\SecondInv[i] <> Null Then
					WriteInt f, it\SecondInv[i]\ID
				Else
					WriteInt f, -1
				EndIf
			Next
		EndIf
	Next
	
	For fb = Each FuseBox
		WriteByte f, fb\fuses
	Next
	
	For ne = Each NewElevator
		WriteByte f, ne\tofloor
		WriteByte f, ne\currfloor
		WriteFloat f, EntityY(ne\obj)
		WriteByte f, ne\currsound
		WriteFloat f, ne\state
		WriteByte f, ne\door\open
	Next
	WriteByte f, PlayerInNewElevator
	WriteByte f, PlayerNewElevator
	
	CloseFile f
	
End Function

Function LoadPlayerData(file$, f%)
	Local x#, y#, z#, i%, j%, temp%, temp2%, strtemp$, npv%
	Local g.Guns, itt.ItemTemplates, it2.Items, it.Items, t.NewTask, n.NPCs
	
	ecst\SuccessRocketLaunch = ReadByte(f)
	ecst\EzDoorOpened = ReadByte(f)
	ecst\WasInHCZ = ReadByte(f)
	ecst\NewCavesEvent = ReadByte(f)
	ecst\CIArrived = ReadByte(f)
	ecst\WasInRoom2_SL = ReadByte(f)
	ecst\WasInLCZCore = ReadByte(f)
	ecst\UnlockedGateDoors = ReadByte(f)
	ecst\NTFArrived = ReadByte(f)
	ecst\WasInO5 = ReadByte(f)
	ecst\WasIn076 = ReadByte(f)
	ecst\After076Timer = ReadFloat(f)
	ecst\WasInCaves = ReadByte(f)
	ecst\WasInO5Again = ReadByte(f)
	ecst\WasInPO = ReadByte(f)
	ecst\WasInReactor = ReadByte(f)
	ecst\WasInBCZ = ReadByte(f)
	ecst\Contained008 = ReadByte(f)
	ecst\Contained049 = ReadByte(f)
	ecst\Contained409 = ReadByte(f)
	
	ecst\UnlockedAirlock = ReadByte(f)
	
	ecst\UnlockedWolfnaya = ReadByte(f)
	ecst\ChanceToSpawnWolfNote = ReadInt(f)
	
	ecst\FusesAmount = ReadInt(f)
	
	ecst\UnlockedEMRP = ReadByte(f)
	ecst\UnlockedHDS = ReadByte(f)
	
	ecst\WasInLWS = ReadByte(f)
	ecst\WasInWS = ReadByte(f)
	ecst\WasInEWS = ReadByte(f)
	ecst\WasInHWS = ReadByte(f)
	ecst\WasInSWS = ReadByte(f)
	ecst\WasInAllSupplies = ReadByte(f)
	
	ecst\IntercomEnabled = ReadByte(f)
	ecst\IntercomIsReady = ReadByte(f)
	ecst\IntercomTimer = ReadFloat(f)
	
	ecst\OmegaWarheadActivated = ReadByte(f)
	ecst\OmegaWarheadDetonate = ReadByte(f)
	ecst\OmegaWarheadTimer = ReadFloat(f)
	
;	For i = 0 To 3
;		AccessCode[i] = Int(ReadString(f))
;	Next
	BlinkTimer = ReadFloat(f)
	BlinkEffect = ReadFloat(f)
	BlinkEffectTimer = ReadFloat(f)
	
	DeathTimer = ReadInt(f)	
	BlurTimer = ReadInt(f)	
	HealTimer = ReadFloat(f)
	
	OnSafety = ReadByte(f)
	
	For g = Each Guns
		g\CurrAmmo = ReadInt(f)
		g\CurrReloadAmmo = ReadInt(f)
		g\Found = ReadByte(f)
		g\JamTimer = ReadFloat(f)
		g\JamTimer2 = ReadFloat(f)
		g\JamAmount = ReadFloat(f)
	Next
	AttachmentMenuOpen = ReadByte(f)
	g_I\HoldingGun = ReadInt(f)
	psp\Health = ReadFloat(f)
	psp\Kevlar = ReadFloat(f)
	psp\Helmet = ReadFloat(f)
	psp\NoMove = ReadByte(f)
	psp\NoRotation = ReadByte(f)
	psp\IsShowingHUD = ReadByte(f)
	g_I\Weapon_CurrSlot = ReadByte(f)
	For i = 0 To MaxGunSlots-1
		g_I\Weapon_InSlot[i] = ReadString(f)
	Next
	DeselectIronSight()
	g_I\GunChangeFLAG = False
	
	For g = Each Guns
		g\BarrelAttachments = ReadInt(f)
		g\MountAttachments = ReadInt(f)
		g\GripAttachments = ReadInt(f)
		g\MagazineAttachments = ReadInt(f)
		g\MiscAttachments = ReadInt(f)
		For i = 0 To MaxAttachments - 1
			g\HasAttachments[i] = ReadByte(f)
			g\HasToggledAttachments[i] = ReadByte(f)
			g\HasPickedAttachments[i] = ReadByte(f)
			If g\HasAttachments[i] Then AddAttachment(g,i)
		Next
	Next
	
	psp\Checkpoint106Passed = ReadByte(f)
	
	Delete Each NewTask
	temp = ReadByte(f)
	While temp
		t.NewTask = BeginTask(ReadInt(f))
		t\Timer = 0
		t\Status = TASK_STATUS_ALREADY
		temp = ReadByte(f)
	Wend
	
	mtfd\Enabled = ReadByte(f)
	mtfd\IsPlaying = ReadByte(f)
	mtfd\CurrentProgress = ReadByte(f)
	mtfd\PrevDialogue = ReadByte(f)
	mtfd\CurrentDialogue = ReadByte(f)
	mtfd\CurrentSequence = ReadInt(f)
	mtfd\Timer = ReadFloat(f)
	
	Crouch = ReadByte(f)
	
	Stamina = ReadFloat(f)
	StaminaEffect = ReadFloat(f)	
	StaminaEffectTimer = ReadFloat(f)	
	
	EyeStuck = ReadFloat(f)
	EyeIrritation = ReadFloat(f)
	
	m_msg\DeathTxt = ReadString(f)
	
	I_005\ChanceToSpawn = ReadInt(f)
	I_035\Possessed = ReadByte(f)
	I_207\Limit = ReadByte(f)
	I_500\Limit = ReadByte(f)
	I_008\Timer = ReadFloat(f)
	I_207\Timer = ReadFloat(f)
	I_207\DeathTimer = ReadFloat(f)
	I_207\Factor = ReadFloat(f)
	I_198\Timer = ReadFloat(f)
	I_198\DeathTimer = ReadFloat(f)
	I_198\Vomit = ReadFloat(f)
	I_198\VomitTimer = ReadFloat(f)
	I_198\Injuries = ReadFloat(f)
	I_109\Used = ReadInt(f)
    I_109\Timer = ReadFloat(f)
	I_268\Using = ReadByte(f)
    I_268\Timer = ReadFloat(f)
	I_1033RU\Using = ReadByte(f)
	I_1033RU\HP = ReadInt(f)
	I_1033RU\DHP = ReadInt(f)
	I_330\Taken = ReadFloat(f)
	I_330\Timer = ReadFloat(f)
	
	VomitTimer = ReadFloat(f)
	Vomit = ReadByte(f)
	CameraShakeTimer = ReadFloat(f)
	
	Local difficultyIndex = ReadByte(f)
	SelectedDifficulty = difficulties[difficultyIndex]
	If (difficultyIndex = ESOTERIC) Then
		SelectedDifficulty\AggressiveNPCs = ReadByte(f)
		SelectedDifficulty\PermaDeath = ReadByte(f)
		SelectedDifficulty\SaveType	= ReadByte(f)
		SelectedDifficulty\OtherFactors = ReadByte(f)
	EndIf
	
	CameraFogFar = ReadFloat(f)
    StoredCameraFogFar = ReadFloat(f)
	If CameraFogFar = 0 Then
		CameraFogFar = 6
	EndIf
	
	Sanity = ReadFloat(f)
	
	IsCutscene = ReadByte(f)
	
	mpl\HasNTFGasmask = ReadByte(f)
	mpl\ShowPlayerModel = ReadByte(f)
	hds\isBroken = ReadByte(f)
	hds\CantWear = ReadByte(f)
	hds\Timer = ReadFloat(f)
	hds\Health = ReadFloat(f)
	I_427\Using = ReadByte(f)
	I_427\Timer = ReadFloat(f)
	I_714\Using = ReadByte(f)
	mpl\NightVisionEnabled = ReadByte(f)
	
	SuperMan = ReadByte(f)
	SuperManTimer = ReadFloat(f)
	LightsOn = ReadByte(f)
	
	SecondaryLightOn = ReadFloat(f)
	PrevSecondaryLightOn = ReadFloat(f)
	RemoteDoorOn = ReadByte(f)
	SoundTransmission = ReadByte(f)
	
	RefinedItems = ReadInt(f)
	
	MTFtimer = ReadFloat(f)
	CITimer = ReadFloat(f)
	
	temp = ReadByte(f)
	While temp
		Local ittName$ = ReadString(f)
		Local tempName$ = ReadString(f)
		Local Name$ = ReadString(f)
		
		x = ReadFloat(f)
		y = ReadFloat(f)
		z = ReadFloat(f)
		
		Local red% = ReadByte(f)
		Local green% = ReadByte(f)
		Local blue% = ReadByte(f)		
		Local a# = ReadFloat(f)
		
		it.Items = CreateItem(ittName, tempName, x, y, z, red,green,blue,a)
		it\name = Name
		
		EntityType it\collider, HIT_ITEM
		
		x = ReadFloat(f)
		y = ReadFloat(f)
		RotateEntity(it\collider, x, y, 0)
		
		it\state = ReadFloat(f)
		it\Picked = True : HideEntity(it\collider)
		
		Local nt% = ReadByte(f)
		If nt = True Then SelectedItem = it
		
		For itt.ItemTemplates = Each ItemTemplates
			If (itt\tempname = tempName) And (itt\name = ittName) Then
				If itt\isAnim<>0 Then SetAnimTime it\model,ReadFloat(f) : Exit
			EndIf
		Next
		it\invSlots = ReadByte(f)
		it\ID = ReadInt(f)
		
		If it\ID>LastItemID Then LastItemID=it\ID
		
		If ReadByte(f)=0 Then
			it\invimg=it\itemtemplate\invimg
		Else
			it\invimg=it\itemtemplate\invimg2
		EndIf	
		
		temp = ReadByte(f)
	Wend
	
	temp = ReadByte(f)
	While temp
		Local NPCtype% = ReadByte(f)
		x = ReadFloat(f)
		y = ReadFloat(f)
		z = ReadFloat(f)
		
		n.NPCs = CreateNPC(NPCtype, x, y, z)
		
		If n\NPCtype = NPC_SCP_173 Then
			If Curr173 <> Null Then
				RemoveNPC(Curr173)
			EndIf
			Curr173 = n
		EndIf
		
		x = ReadFloat(f)
		y = ReadFloat(f)
		z = ReadFloat(f)
		RotateEntity(n\Collider, x, y, z)
		
		For npv = 0 To MaxNPCStates - 1
			n\State[npv] = ReadFloat(f)
		Next
		n\PrevState = ReadInt(f)
		
		n\Idle = ReadByte(f)
		n\IdleTimer = ReadFloat(f)
		n\LastDist = ReadFloat(f)
		n\LastSeen = ReadInt(f)
		
		n\CurrSpeed = ReadInt(f)
		n\Angle = ReadFloat(f)
		n\Reload = ReadFloat(f)
		
		ForceSetNPCID(n, ReadInt(f))
		n\TargetID = ReadInt(f)
		
		;debuglog("Loading NPC " +n\NVName+ " (ID "+n\ID+")")
		
		n\EnemyX = ReadFloat(f)
		n\EnemyY = ReadFloat(f)
		n\EnemyZ = ReadFloat(f)
		
		n\texture = ReadString(f)
		If n\texture <> "" Then
			Local tex = LoadTexture_Strict (n\texture)
			TextureBlend(tex,5)
			EntityTexture n\obj, tex
		EndIf
		
		Local frame# = ReadFloat(f)
		Select NPCtype
			Case NPC_SCP_106, NPC_Human, NPC_SCP_096, NPC_NTF, NPC_Guard, NPC_SCP_049, NPC_Zombie, NPC_Zombie_Armed, NPC_Class_D, NPC_SCP_1048, NPC_SCP_076, NPC_SM4Nn
				SetAnimTime(n\obj, frame)
		End Select
		
		n\Frame = frame
		
		n\IsDead = ReadInt(f)
		n\PathX = ReadFloat(f)
		n\PathZ = ReadFloat(f)
		n\HP = ReadInt(f)
		n\Model = ReadString(f)
		n\ModelScaleX# = ReadFloat(f)
		n\ModelScaleY# = ReadFloat(f)
		n\ModelScaleZ# = ReadFloat(f)
		If n\Model <> ""
			n\obj = FreeEntity_Strict(n\obj)
			n\obj = LoadAnimMesh_Strict(n\Model)
			ScaleEntity n\obj,n\ModelScaleX,n\ModelScaleY,n\ModelScaleZ
			SetAnimTime n\obj,frame
		EndIf
		n\TextureID = ReadInt(f)
		If n\TextureID > 0
			ChangeNPCTexture(n.NPCs,n\texture)
			SetAnimTime(n\obj,frame)
		EndIf
		
		Local GunID% = ReadInt(f)
		If GunID <> GUN_UNARMED Then
			If n\Gun = Null Lor n\Gun\ID <> GunID Then
				SwitchNPCGun(n, GunID)
			EndIf
			Local GunAmmo% = ReadInt(f)
			If n\Gun <> Null Then
				n\Gun\Ammo = GunAmmo
			EndIf
		ElseIf n\Gun <> Null Then
			RemoveNPCGun(n)
		EndIf
		
		temp = ReadByte(f)
	Wend
	
	For i = 0 To MaxInventorySpace-1
		temp = ReadInt(f)
		If temp > -1 Then
			For it = Each Items
				If it\ID = temp Then
					Inventory[i] = it
					ItemAmount = ItemAmount + 1
					If it\invSlots > 0 Then
						For j = 0 To it\invSlots-1
							temp2 = ReadInt(f)
							If temp2 > -1 Then
								For it2 = Each Items
									If it2\ID = temp2 Then
										it\SecondInv[j] = it2
										Exit
									EndIf
								Next
							EndIf
						Next
					EndIf
					Exit
				EndIf
			Next
		EndIf
	Next
	
	For itt.ItemTemplates = Each ItemTemplates
		itt\found = ReadByte(f)
	Next
	
	UsedConsole = ReadByte(f)
	
	gopt\CurrZone = ReadInt(f)
	
End Function

Function LoadDataForZones(file$)
	Local f% = ReadFile(file + "main.sav")
	
	ReadString(f)
	ReadString(f)
	ReadString(f)
	gopt\GameMode = ReadByte(f) - 1
	ClassDNumber = ReadInt(f)
	gopt\CurrZoneString = ReadString(f)
	
	PlayTime = ReadInt(f)
	
	ReadFloat(f)
	ReadFloat(f)
	ReadFloat(f)
	
	ReadFloat(f)
	ReadFloat(f)
	ReadFloat(f)
	
	ReadFloat(f)
	ReadFloat(f)
	
	LoadPlayerData(file, f)
	
	CloseFile f
End Function

Function LoadGame(File$, ZoneToLoad% = -1)
	CatchErrors("LoadGame(" + File + ")")
	;debuglog "---------------------------------------------------------------------------"
	Local version$ = ""
	
	DropSpeed=0.0
	
	DebugHUD = False
	
	GameSaved = True
	
	Local x#, y#, z#, i%, j%, temp%, temp2%, strtemp$, id%, tex%, dist#, dist2#, s%, npv%
	Local player_x#,player_y#,player_z#, r.Rooms,r2.Rooms, n.NPCs, do.Doors, g.Guns, itt.ItemTemplates, it2.Items, n2.NPCs, it.Items, em.Emitters, fb.FuseBox, ne.NewElevator
	Local f% = ReadFile(File + "main.sav")
	
	version = ReadString(f)
	strtemp = ReadString(f)
	strtemp = ReadString(f)
	gopt\GameMode = ReadByte(f) - 1
	ClassDNumber = ReadInt(f)
	gopt\CurrZoneString = ReadString(f)
	
	PlayTime = ReadInt(f)
	
	x = ReadFloat(f)
	y = ReadFloat(f)
	z = ReadFloat(f)	
	PositionEntity(Collider, x, y+0.05, z)
	ResetEntity(Collider)
	
	x = ReadFloat(f)
	y = ReadFloat(f)
	z = ReadFloat(f)	
	PositionEntity(Head, x, y+0.05, z)
	ResetEntity(Head)
	
	x = ReadFloat(f)
	y = ReadFloat(f)
	RotateEntity(Collider, x, y, 0, 0)
	
	LoadPlayerData(File, f)
	
	If ZoneToLoad > -1 Then
		gopt\CurrZone = ZoneToLoad
	EndIf
	
	CloseFile f
	
	f = ReadFile(File + gopt\CurrZone + ".sav")
	
	RandomSeed = ReadString(f)
	
	temp = ReadInt(f)
	For i = 1 To temp
		Local NPCtype% = ReadByte(f)
		x = ReadFloat(f)
		y = ReadFloat(f)
		z = ReadFloat(f)
		
		n.NPCs = CreateNPC(NPCtype, x, y, z)
		Select NPCtype
			Case NPC_SCP_173
				Curr173 = n
			Case NPC_SCP_106
				Curr106 = n
			Case NPC_SCP_096
				Curr096 = n
			Case NPC_SCP_513
				Curr5131 = n
		End Select
		
		x = ReadFloat(f)
		y = ReadFloat(f)
		z = ReadFloat(f)
		RotateEntity(n\Collider, x, y, z)
		
		For npv = 0 To MaxNPCStates - 1
			n\State[npv] = ReadFloat(f)
		Next
		n\PrevState = ReadInt(f)
		
		n\Idle = ReadByte(f)
		n\IdleTimer = ReadFloat(f)
		n\LastDist = ReadFloat(f)
		n\LastSeen = ReadInt(f)
		
		n\CurrSpeed = ReadInt(f)
		n\Angle = ReadFloat(f)
		n\Reload = ReadFloat(f)
		
		ForceSetNPCID(n, ReadInt(f))
		n\TargetID = ReadInt(f)
		
		;debuglog("Loading NPC " +n\NVName+ " (ID "+n\ID+")")
		
		n\EnemyX = ReadFloat(f)
		n\EnemyY = ReadFloat(f)
		n\EnemyZ = ReadFloat(f)
		
		n\texture = ReadString(f)
		If n\texture <> "" Then
			tex = LoadTexture_Strict (n\texture)
			TextureBlend(tex,5)
			EntityTexture n\obj, tex
		EndIf
		
		Local frame# = ReadFloat(f)
		Select NPCtype
			Case NPC_SCP_106, NPC_Human, NPC_SCP_096, NPC_NTF, NPC_Guard, NPC_SCP_049, NPC_Zombie, NPC_Zombie_Armed, NPC_Class_D, NPC_SCP_1048, NPC_SCP_076, NPC_SM4Nn
				SetAnimTime(n\obj, frame)
		End Select
		
		n\Frame = frame
		
		n\IsDead = ReadInt(f)
		n\PathX = ReadFloat(f)
		n\PathZ = ReadFloat(f)
		n\HP = ReadInt(f)
		n\Model = ReadString(f)
		n\ModelScaleX# = ReadFloat(f)
		n\ModelScaleY# = ReadFloat(f)
		n\ModelScaleZ# = ReadFloat(f)
		If n\Model <> ""
			n\obj = FreeEntity_Strict(n\obj)
			n\obj = LoadAnimMesh_Strict(n\Model)
			ScaleEntity n\obj,n\ModelScaleX,n\ModelScaleY,n\ModelScaleZ
			SetAnimTime n\obj,frame
		EndIf
		n\TextureID = ReadInt(f)
		If n\TextureID > 0
			ChangeNPCTexture(n.NPCs,n\texture)
			SetAnimTime(n\obj,frame)
		EndIf
		
		Local GunID% = ReadInt(f)
		If GunID <> GUN_UNARMED Then
			If n\Gun = Null Lor n\Gun\ID <> GunID Then
				SwitchNPCGun(n, GunID)
			EndIf
			Local GunAmmo% = ReadInt(f)
			If n\Gun <> Null Then
				n\Gun\Ammo = GunAmmo
			EndIf
		ElseIf n\Gun <> Null Then
			RemoveNPCGun(n)
		EndIf
	Next
	
	For n.NPCs = Each NPCs
		If n\TargetID <> 0 Then
			For n2.NPCs = Each NPCs
				If n2<>n Then
					If n2\ID = n\TargetID Then n\Target = n2
				EndIf
			Next
		EndIf
	Next
	
	For i = 0 To 6
		strtemp =  ReadString(f)
		If strtemp <> "a" Then
			For r.Rooms = Each Rooms
				If r\RoomTemplate\Name = strtemp Then
					MTFrooms[i]=r
				EndIf
			Next
		EndIf
		MTFroomState[i]=ReadInt(f)
	Next
	
	room2gw_brokendoor = ReadInt(f)
	room2gw_x = ReadFloat(f)
	room2gw_z = ReadFloat(f)
	
	CreateMap()
	
	Local rx = ReadFloat(f)
	Local rz = ReadFloat(f)
	For r = Each Rooms
		r\found = ReadByte(f)
		
		If r\x = rx And r\z = rz Then
			PlayerRoom = r
		EndIf	
		
		For x = 0 To 11
			id = ReadInt(f)
			If id > 0 Then
				For n.NPCs = Each NPCs
					If n\ID = id Then r\NPC[x]=n : Exit
				Next
			EndIf
		Next
		
		For x=0 To 10
			id = ReadByte(f)
			If id=1 Then
				RotateEntity(r\Levers[x]\obj, 78, EntityYaw(r\Levers[x]\obj), 0)
			ElseIf id=0 Then
				RotateEntity(r\Levers[x]\obj, -78, EntityYaw(r\Levers[x]\obj), 0)
			EndIf
		Next
		
		temp = ReadInt(f)
		For j = 1 To temp
			x = ReadFloat(f)
			y = ReadFloat(f)
			z = ReadFloat(f)
			id = ReadInt(f)
			em.Emitters = CreateEmitter(x, y, z, id)
			x = ReadFloat(f)
			y = ReadFloat(f)
			z = ReadFloat(f)
			RotateEntity em\Obj, x, y, z
			EntityParent(em\Obj, r\obj)
			em\Size = ReadFloat(f)
			em\SizeChange = ReadFloat(f)
			em\Speed = ReadFloat(f)
			em\RandAngle = ReadFloat(f)
			em\Disable = ReadByte(f)
		Next
	Next
	
	temp = ReadInt(f)
	
	For i = 1 To temp
		x = ReadFloat(f)
		y = ReadFloat(f)
		z = ReadFloat(f)
		
		Local open% = ReadByte(f)
		Local openstate# = ReadFloat(f)
		Local locked% = ReadByte(f)
		Local autoclose% = ReadByte(f)
		
		Local objX# = ReadFloat(f)
		Local objZ# = ReadFloat(f)
		
		Local obj2X# = ReadFloat(f)
		Local obj2Z# = ReadFloat(f)
		
		Local timer% = ReadFloat(f)
		Local timerstate# = ReadFloat(f)
		
		Local IsElevDoor = ReadByte(f)
		Local MTFClose = ReadByte(f)
		
		For do.Doors = Each Doors
			If EntityX(do\frameobj,True) = x And EntityY(do\frameobj,True) = y And EntityZ(do\frameobj,True) = z Then
				do\open = open
				do\openstate = openstate
				do\locked = locked
				do\AutoClose = autoclose
				do\timer = timer
				do\timerstate = timerstate
				do\IsElevatorDoor = IsElevDoor
				do\MTFClose = MTFClose
				
				PositionEntity(do\obj, objX, y, objZ, True)
				If do\obj2 <> 0 Then PositionEntity(do\obj2, obj2X, y, obj2Z, True)
				Exit
			EndIf
		Next
	Next
	
	InitWayPoints()
	
	Local d.Decals
	For d.Decals = Each Decals
		d\obj = FreeEntity_Strict(d\obj)
		Delete d
	Next
	
	temp = ReadInt(f)
	For i = 1 To temp
		id% = ReadInt(f)
		x = ReadFloat(f)
		y = ReadFloat(f)
		z = ReadFloat(f)
		Local pitch# = ReadFloat(f)
		Local yaw# = ReadFloat(f)
		Local roll# = ReadFloat(f)
		d.Decals = CreateDecal(id, x, y, z, pitch, yaw, roll)
		d\blendmode = ReadByte (f)
		d\fx = ReadInt(f)
		
		d\Size = ReadFloat(f)
		d\Alpha = ReadFloat(f)
		d\AlphaChange = ReadFloat(f)
		d\Timer = ReadFloat(f)
		d\lifetime = ReadFloat(f)
		
		ScaleSprite(d\obj, d\Size, d\Size)
		EntityBlend d\obj, d\blendmode
		EntityFX d\obj, d\fx
		
		;debuglog "Created Decal @"+x+","+y+","+z
	Next
	UpdateDecals()
	
	temp = ReadInt(f)
	For i = 1 To temp
		Local e.Events = New Events
		e\EventName = ReadString(f)
		For s = 0 To MaxEventStates - 1
			e\EventState[s] = ReadFloat(f)
		Next
		x = ReadFloat(f)
		z = ReadFloat(f)
		For r.Rooms = Each Rooms
			If EntityX(r\obj) = x And EntityZ(r\obj) = z Then
				e\room = r
				Exit
			EndIf
		Next
		e\EventStr = ReadString(f)
	Next
	
	For it.Items = Each Items
		If (Not IsItemInInventory(it)) Then
			RemoveItem(it)
		EndIf
	Next
	
	temp = ReadByte(f)
	While temp
		Local ittName$ = ReadString(f)
		Local tempName$ = ReadString(f)
		Local Name$ = ReadString(f)
		
		x = ReadFloat(f)
		y = ReadFloat(f)
		z = ReadFloat(f)
		
		Local red% = ReadByte(f)
		Local green% = ReadByte(f)
		Local blue% = ReadByte(f)
		Local a# = ReadFloat(f)
		
		it.Items = CreateItem(ittName, tempName, x, y, z, red,green,blue,a)
		it\name = Name
		
		EntityType it\collider, HIT_ITEM
		
		x = ReadFloat(f)
		y = ReadFloat(f)
		RotateEntity(it\collider, x, y, 0)
		
		it\state = ReadFloat(f)
		
		For itt.ItemTemplates = Each ItemTemplates
			If (itt\tempname = tempName) And (itt\name = ittName) Then
				If itt\isAnim<>0 Then SetAnimTime it\model,ReadFloat(f) : Exit
			EndIf
		Next
		it\invSlots = ReadByte(f)
		it\ID = ReadInt(f)
		
		If it\ID>LastItemID Then LastItemID=it\ID
		
		If ReadByte(f)=0 Then
			it\invimg=it\itemtemplate\invimg
		Else
			it\invimg=it\itemtemplate\invimg2
		EndIf	
		
		temp = ReadByte(f)
	Wend
	
	For it = Each Items
		If (Not IsItemInInventory(it)) And it\invSlots > 0 Then
			For i = 0 To it\invSlots-1
				temp2 = ReadInt(f)
				If temp2 > -1 Then
					For it2 = Each Items
						If it2\ID = temp2 Then
							it\SecondInv[i] = it2
							Exit
						EndIf
					Next
				EndIf
			Next
		EndIf
	Next
	
	For fb = Each FuseBox
		fb\fuses = ReadByte(f)
	Next
	
	For ne = Each NewElevator
		ne\tofloor = ReadByte(f)
		ne\currfloor = ReadByte(f)
		y = ReadFloat(f)
		PositionEntity ne\obj, EntityX(ne\obj), y, EntityZ(ne\obj)
		ne\currsound = ReadByte(f)
		ne\state = ReadFloat(f)
		ne\door\open = ReadByte(f)
	Next
	PlayerInNewElevator = ReadByte(f)
	PlayerNewElevator = ReadByte(f)
	
	For do.Doors = Each Doors
		If do\room <> Null Then
			dist# = 20.0
			Local closestroom.Rooms
			For r.Rooms = Each Rooms
				dist2# = EntityDistance(r\obj, do\obj)
				If dist2 < dist Then
					dist = dist2
					closestroom = r.Rooms
				EndIf
			Next
			do\room = closestroom
		EndIf
	Next
	
	CloseFile f
	
	For r.Rooms = Each Rooms
		r\Adjacent[0]=Null
		r\Adjacent[1]=Null
		r\Adjacent[2]=Null
		r\Adjacent[3]=Null
		For r2.Rooms = Each Rooms
			If r<>r2 Then
				If r2\z=r\z Then
					If (r2\x)=(r\x+8.0) Then
						r\Adjacent[0]=r2
					ElseIf (r2\x)=(r\x-8.0)
						r\Adjacent[2]=r2
					EndIf
				ElseIf r2\x=r\x Then
					If (r2\z)=(r\z-8.0) Then
						r\Adjacent[1]=r2
					ElseIf (r2\z)=(r\z+8.0)
						r\Adjacent[3]=r2
					EndIf
				EndIf
			EndIf
			If (r\Adjacent[0]<>Null) And (r\Adjacent[1]<>Null) And (r\Adjacent[2]<>Null) And (r\Adjacent[3]<>Null) Then Exit
		Next
		
		For do.Doors = Each Doors
			If (do\KeyCard = -1) And (do\Code="") Then
				If EntityZ(do\frameobj,True)=r\z Then
					If EntityX(do\frameobj,True)=r\x+4.0 Then
						r\AdjDoor[0] = do
					ElseIf EntityX(do\frameobj,True)=r\x-4.0 Then
						r\AdjDoor[2] = do
					EndIf
				ElseIf EntityX(do\frameobj,True)=r\x Then
					If EntityZ(do\frameobj,True)=r\z+4.0 Then
						r\AdjDoor[3] = do
					ElseIf EntityZ(do\frameobj,True)=r\z-4.0 Then
						r\AdjDoor[1] = do
					EndIf
				EndIf
			EndIf
		Next
	Next
	
	For r.Rooms = Each Rooms
		r\Adjacent[0]=Null
		r\Adjacent[1]=Null
		r\Adjacent[2]=Null
		r\Adjacent[3]=Null
		For r2.Rooms = Each Rooms
			If r<>r2 Then
				If r2\z=r\z Then
					If (r2\x)=(r\x+8.0) Then
						r\Adjacent[0]=r2
						If r\AdjDoor[0] = Null Then r\AdjDoor[0] = r2\AdjDoor[2]
					ElseIf (r2\x)=(r\x-8.0)
						r\Adjacent[2]=r2
						If r\AdjDoor[2] = Null Then r\AdjDoor[2] = r2\AdjDoor[0]
					EndIf
				ElseIf r2\x=r\x Then
					If (r2\z)=(r\z-8.0) Then
						r\Adjacent[1]=r2
						If r\AdjDoor[1] = Null Then r\AdjDoor[1] = r2\AdjDoor[3]
					ElseIf (r2\z)=(r\z+8.0)
						r\Adjacent[3]=r2
						If r\AdjDoor[3] = Null Then r\AdjDoor[3] = r2\AdjDoor[1]
					EndIf
				EndIf
			EndIf
			If (r\Adjacent[0]<>Null) And (r\Adjacent[1]<>Null) And (r\Adjacent[2]<>Null) And (r\Adjacent[3]<>Null) Then Exit
		Next
		
		For do.Doors = Each Doors
			If (do\KeyCard = -1) And (do\Code="") Then
				If EntityZ(do\frameobj,True)=r\z Then
					If EntityX(do\frameobj,True)=r\x+4.0 Then
						r\AdjDoor[0] = do
					ElseIf EntityX(do\frameobj,True)=r\x-4.0 Then
						r\AdjDoor[2] = do
					EndIf
				ElseIf EntityX(do\frameobj,True)=r\x Then
					If EntityZ(do\frameobj,True)=r\z+4.0 Then
						r\AdjDoor[3] = do
					ElseIf EntityZ(do\frameobj,True)=r\z-4.0 Then
						r\AdjDoor[1] = do
					EndIf
				EndIf
			EndIf
		Next
	Next
	
	If Collider <> 0 Then
		If PlayerRoom <> Null Then
			ShowEntity PlayerRoom\obj
		EndIf
		ShowEntity Collider
		TeleportEntity(Collider, EntityX(Collider), EntityY(Collider) + 0.5, EntityZ(Collider), 0.3, True)
		If PlayerRoom<>Null Then
			HideEntity PlayerRoom\obj
		EndIf
	EndIf
	
	d_I\UpdateDoorsTimer = 0
	
	CatchErrors("Uncaught (LoadGame(" + File + "))")
End Function

Function LoadGameQuick(file$, showmsg%=True)
	CatchErrors("LoadGameQuick(" + file + ")")
	;debuglog "---------------------------------------------------------------------------"
	Local version$ = ""
	
	DebugHUD = False
	GameSaved = True
	NoTarget = False
	InfiniteStamina = False
	IsZombie% = False
	DeafPlayer% = False
	DeafTimer# = 0.0
	UnableToMove% = False
	m_msg\Txt = ""
	SelectedEnding = ""
	
	PositionEntity Collider,0,1000.0,0,True
	ResetEntity Collider
	
	Local x#, y#, z#, i%, j%, temp%, temp2%, strtemp$, id%, tex%, dist#, dist2#, s%, npv%
	Local player_x#,player_y#,player_z#, r.Rooms,r2.Rooms, n.NPCs, do.Doors, g.Guns, itt.ItemTemplates, it2.Items, n2.NPCs, it.Items, em.Emitters, fb.FuseBox, ne.NewElevator,sc.SecurityCams
	Local f% = ReadFile(file + "main.sav")
	
	version = ReadString(f)
	strtemp = ReadString(f)
	strtemp = ReadString(f)
	gopt\GameMode = ReadByte(f) - 1
	ClassDNumber = ReadInt(f)
	gopt\CurrZoneString = ReadString(f)
	
	DropSpeed = -0.1
	HeadDropSpeed = 0.0
	Shake = 0
	CurrSpeed = 0
	
	HeartBeatVolume = 0
	
	CameraShake = 0
	BigCameraShake = 0
	Shake = 0
	LightFlash = 0
	BlurTimer = 0
	
	KillTimer = 0
	FallTimer = 0
	MenuOpen = False
	
	GodMode = 0
	NoClip = 0
	
	PlayTime = ReadInt(f)
	
	HideEntity Collider
	
	x = ReadFloat(f)
	y = ReadFloat(f)
	z = ReadFloat(f)
	PositionEntity(Collider, x, y+0.05, z)
	
	ShowEntity Collider
	
	x = ReadFloat(f)
	y = ReadFloat(f)
	z = ReadFloat(f)	
	PositionEntity(Head, x, y+0.05, z)
	ResetEntity(Head)
	
	x = ReadFloat(f)
	y = ReadFloat(f)
	RotateEntity(Collider, x, y, 0, 0)
	
	For n = Each NPCs
		RemoveNPC(n)
	Next
	For it = Each Items
		RemoveItem(it)
	Next
	
	LoadPlayerData(file, f)
	
	CloseFile f
	
	f = ReadFile(file + gopt\CurrZone + ".sav")
	
	If showmsg Then CreateHintMsg(GetLocalString("Menu", "game_loaded"))
	
	RandomSeed = ReadString(f)
	
	temp = ReadInt(f)
	For i = 1 To temp
		Local NPCtype% = ReadByte(f)
		x = ReadFloat(f)
		y = ReadFloat(f)
		z = ReadFloat(f)
		
		n.NPCs = CreateNPC(NPCtype, x, y, z)
		Select NPCtype
			Case NPC_SCP_173
				Curr173 = n
			Case NPC_SCP_106
				Curr106 = n
			Case NPC_SCP_096
				Curr096 = n
			Case NPC_SCP_513
				Curr5131 = n
		End Select
		
		x = ReadFloat(f)
		y = ReadFloat(f)
		z = ReadFloat(f)
		RotateEntity(n\Collider, x, y, z)
		
		For npv = 0 To MaxNPCStates - 1
			n\State[npv] = ReadFloat(f)
		Next
		n\PrevState = ReadInt(f)
		
		n\Idle = ReadByte(f)
		n\IdleTimer = ReadFloat(f)
		n\LastDist = ReadFloat(f)
		n\LastSeen = ReadInt(f)
		
		n\CurrSpeed = ReadInt(f)
		n\Angle = ReadFloat(f)
		n\Reload = ReadFloat(f)
		
		ForceSetNPCID(n, ReadInt(f))
		n\TargetID = ReadInt(f)
		
		n\EnemyX = ReadFloat(f)
		n\EnemyY = ReadFloat(f)
		n\EnemyZ = ReadFloat(f)
		
		n\texture = ReadString(f)
		If n\texture <> "" Then
			tex = LoadTexture_Strict (n\texture)
			TextureBlend(tex,5)
			EntityTexture n\obj, tex
		EndIf
		
		Local frame# = ReadFloat(f)
		Select NPCtype
			Case NPC_SCP_106, NPC_Human, NPC_SCP_096, NPC_NTF, NPC_Guard, NPC_SCP_049, NPC_Zombie, NPC_Zombie_Armed, NPC_Class_D, NPC_SCP_1048, NPC_SCP_076, NPC_SM4Nn
				SetAnimTime(n\obj, frame)
		End Select		
		
		n\Frame = frame
		
		n\IsDead = ReadInt(f)
		n\PathX = ReadFloat(f)
		n\PathZ = ReadFloat(f)
		n\HP = ReadInt(f)
		n\Model = ReadString(f)
		n\ModelScaleX# = ReadFloat(f)
		n\ModelScaleY# = ReadFloat(f)
		n\ModelScaleZ# = ReadFloat(f)
		If n\Model <> ""
			n\obj = FreeEntity_Strict(n\obj)
			n\obj = LoadAnimMesh_Strict(n\Model)
			ScaleEntity n\obj,n\ModelScaleX,n\ModelScaleY,n\ModelScaleZ
			SetAnimTime n\obj,frame
		EndIf
		n\TextureID = ReadInt(f)
		If n\TextureID > 0
			ChangeNPCTexture(n.NPCs,n\texture)
			SetAnimTime(n\obj,frame)
		EndIf
		
		Local GunID% = ReadInt(f)
		If GunID <> GUN_UNARMED Then
			If n\Gun = Null Lor n\Gun\ID <> GunID Then
				SwitchNPCGun(n, GunID)
			EndIf
			Local GunAmmo% = ReadInt(f)
			If n\Gun <> Null Then
				n\Gun\Ammo = GunAmmo
			EndIf
		ElseIf n\Gun <> Null Then
			RemoveNPCGun(n)
		EndIf
	Next
	
	For n.NPCs = Each NPCs
		If n\TargetID <> 0 Then
			For n2.NPCs = Each NPCs
				If n2<>n Then
					If n2\ID = n\TargetID Then n\Target = n2
				EndIf
			Next
		EndIf
	Next
	
	For i = 0 To 6
		strtemp =  ReadString(f)
		If strtemp <> "a" Then
			For r.Rooms = Each Rooms
				If r\RoomTemplate\Name = strtemp Then
					MTFrooms[i]=r
				EndIf
			Next
		EndIf
		MTFroomState[i]=ReadInt(f)
	Next
	
	room2gw_brokendoor = ReadInt(f)
	room2gw_x = ReadFloat(f)
	room2gw_z = ReadFloat(f)
	
	For em = Each Emitters
		If (Not em\map_generated) Then
			RemoveEmitter(em)
		EndIf
	Next
	
	Local rx = ReadFloat(f)
	Local rz = ReadFloat(f)
	For r = Each Rooms
		r\found = ReadByte(f)
		
		If r\x = rx And r\z = rz Then
			PlayerRoom = r
		EndIf	
		
		For x = 0 To 11
			id = ReadInt(f)
			If id > 0 Then
				For n.NPCs = Each NPCs
					If n\ID = id Then r\NPC[x]=n : Exit
				Next
			EndIf
		Next
		
		For x=0 To 10
			id = ReadByte(f)
			If id=1 Then
				RotateEntity(r\Levers[x]\obj, 78, EntityYaw(r\Levers[x]\obj), 0)
			ElseIf id=0 Then
				RotateEntity(r\Levers[x]\obj, -78, EntityYaw(r\Levers[x]\obj), 0)
			EndIf
		Next
		
		temp = ReadInt(f)
		For j = 1 To temp
			x = ReadFloat(f)
			y = ReadFloat(f)
			z = ReadFloat(f)
			id = ReadInt(f)
			em.Emitters = CreateEmitter(x, y, z, id)
			x = ReadFloat(f)
			y = ReadFloat(f)
			z = ReadFloat(f)
			RotateEntity em\Obj, x, y, z
			EntityParent(em\Obj, r\obj)
			em\Size = ReadFloat(f)
			em\SizeChange = ReadFloat(f)
			em\Speed = ReadFloat(f)
			em\RandAngle = ReadFloat(f)
			em\Disable = ReadByte(f)
		Next
	Next
	
	temp = ReadInt(f)
	
	For i = 1 To temp
		x = ReadFloat(f)
		y = ReadFloat(f)
		z = ReadFloat(f)
		
		Local open% = ReadByte(f)
		Local openstate# = ReadFloat(f)
		Local locked% = ReadByte(f)
		Local autoclose% = ReadByte(f)
		
		Local objX# = ReadFloat(f)
		Local objZ# = ReadFloat(f)
		
		Local obj2X# = ReadFloat(f)
		Local obj2Z# = ReadFloat(f)
		
		Local timer% = ReadFloat(f)
		Local timerstate# = ReadFloat(f)
		
		Local IsElevDoor = ReadByte(f)
		Local MTFClose = ReadByte(f)
		
		For do.Doors = Each Doors
			If EntityX(do\frameobj,True) = x Then 
				If EntityZ(do\frameobj,True) = z Then	
					If EntityY(do\frameobj,True) = y 
						do\open = open
						do\openstate = openstate
						do\locked = locked
						do\AutoClose = autoclose
						do\timer = timer
						do\timerstate = timerstate
						do\IsElevatorDoor = IsElevDoor
						do\MTFClose = MTFClose
						
						PositionEntity(do\obj, objX, EntityY(do\obj), objZ, True)
						If do\obj2 <> 0 Then PositionEntity(do\obj2, obj2X, EntityY(do\obj2), obj2Z, True)
						
						Exit
					EndIf
				EndIf
			End If
		Next		
	Next
	
	Local d.Decals
	For d.Decals = Each Decals
		d\obj = FreeEntity_Strict(d\obj)
		Delete d
	Next
	
	temp = ReadInt(f)
	For i = 1 To temp
		id% = ReadInt(f)
		x = ReadFloat(f)
		y = ReadFloat(f)
		z = ReadFloat(f)
		Local pitch# = ReadFloat(f)
		Local yaw# = ReadFloat(f)
		Local roll# = ReadFloat(f)
		d.Decals = CreateDecal(id, x, y, z, pitch, yaw, roll)
		d\blendmode = ReadByte (f)
		d\fx = ReadInt(f)
		
		d\Size = ReadFloat(f)
		d\Alpha = ReadFloat(f)
		d\AlphaChange = ReadFloat(f)
		d\Timer = ReadFloat(f)
		d\lifetime = ReadFloat(f)
		
		ScaleSprite(d\obj, d\Size, d\Size)
		EntityBlend d\obj, d\blendmode
		EntityFX d\obj, d\fx
		
		;debuglog "Created Decal @"+x+","+y+","+z
	Next
	UpdateDecals()
	
	Local e.Events
	For e.Events = Each Events
		If e\Sound[0] <> 0 Then FreeSound_Strict e\Sound[0]
		Delete e
	Next
	
	temp = ReadInt(f)
	For i = 1 To temp
		e.Events = New Events
		e\EventName = ReadString(f)
		For s = 0 To MaxEventStates - 1
			e\EventState[s] = ReadFloat(f)
		Next
		x = ReadFloat(f)
		z = ReadFloat(f)
		For r.Rooms = Each Rooms
			If EntityX(r\obj) = x And EntityZ(r\obj) = z Then
				e\room = r
				Exit
			EndIf
		Next	
		e\EventStr = ReadString(f)
	Next
	
	temp = ReadByte(f)
	While temp
		Local ittName$ = ReadString(f)
		Local tempName$ = ReadString(f)
		Local Name$ = ReadString(f)
		
		x = ReadFloat(f)
		y = ReadFloat(f)
		z = ReadFloat(f)
		
		Local red% = ReadByte(f)
		Local green% = ReadByte(f)
		Local blue% = ReadByte(f)
		Local a# = ReadFloat(f)
		
		it.Items = CreateItem(ittName, tempName, x, y, z, red,green,blue,a)
		it\name = Name
		
		EntityType it\collider, HIT_ITEM
		
		x = ReadFloat(f)
		y = ReadFloat(f)
		RotateEntity(it\collider, x, y, 0)
		
		it\state = ReadFloat(f)
		
		For itt.ItemTemplates = Each ItemTemplates
			If (itt\tempname = tempName) And (itt\name = ittName) Then
				If itt\isAnim<>0 Then SetAnimTime it\model,ReadFloat(f) : Exit
			EndIf
		Next
		it\invSlots = ReadByte(f)
		it\ID = ReadInt(f)
		
		If it\ID>LastItemID Then LastItemID=it\ID
		
		If ReadByte(f)=0 Then
			it\invimg=it\itemtemplate\invimg
		Else
			it\invimg=it\itemtemplate\invimg2
		EndIf	
		
		temp = ReadByte(f)
	Wend
	
	For it = Each Items
		If (Not IsItemInInventory(it)) And it\invSlots > 0 Then
			For i = 0 To it\invSlots-1
				temp2 = ReadInt(f)
				If temp2 > -1 Then
					For it2 = Each Items
						If it2\ID = temp2 Then
							it\SecondInv[i] = it2
							Exit
						EndIf
					Next
				EndIf
			Next
		EndIf
	Next
	
	For fb = Each FuseBox
		fb\fuses = ReadByte(f)
	Next
	
	For ne = Each NewElevator
		ne\tofloor = ReadByte(f)
		ne\currfloor = ReadByte(f)
		y = ReadFloat(f)
		PositionEntity ne\obj, EntityX(ne\obj), y, EntityZ(ne\obj)
		ne\currsound = ReadByte(f)
		ne\state = ReadFloat(f)
		ne\door\open = ReadByte(f)
	Next
	PlayerInNewElevator = ReadByte(f)
	PlayerNewElevator = ReadByte(f)
	
	For do.Doors = Each Doors
		If do\room <> Null Then
			dist# = 20.0
			Local closestroom.Rooms
			For r.Rooms = Each Rooms
				dist2# = EntityDistance(r\obj, do\obj)
				If dist2 < dist Then
					dist = dist2
					closestroom = r.Rooms
				EndIf
			Next
			do\room = closestroom
		EndIf
	Next
	
	For sc.SecurityCams = Each SecurityCams
		sc\PlayerState = 0
	Next
	RestoreSanity = True
	
	CloseFile f
	
	If Collider <> 0 Then
		If PlayerRoom<>Null Then
			ShowEntity PlayerRoom\obj
		EndIf
		ShowEntity Collider
		TeleportEntity(Collider,EntityX(Collider),EntityY(Collider)+0.5,EntityZ(Collider),0.3,True)
		If PlayerRoom<>Null Then
			HideEntity PlayerRoom\obj
		EndIf
	EndIf
	
	d_I\UpdateDoorsTimer = 0
	
	Local xtemp#,ztemp#
	If Sky <> 0 Then
		Sky = FreeEntity_Strict(Sky)
	EndIf
	CameraFogMode(Camera, 1)
	HideDistance# = 15.0
	
	CatchErrors("Uncaught (LoadGameQuick(" + file + "))")
End Function

Function IsItemInInventory(it.Items)
	Local i%, j%
	
	For i = 0 To MaxInventorySpace-1
		If Inventory[i] <> Null Then
			If it = Inventory[i] Then
				Return True
			ElseIf Inventory[i]\invSlots > 0 Then
				For j = 0 To Inventory[i]\invSlots-1
					If it = Inventory[i]\SecondInv[j] Then
						Return True
					EndIf
				Next
			EndIf
		EndIf
	Next
	Return False
	
End Function

Type Save
	Field Name$
	Field Date$
	Field Time$
	Field Version$
	Field Gamemode%
	Field DNumber#
	Field ZoneString$
End Type

Const SavePath$ = "Saves\"

Global CurrSave.Save
Global DelSave.Save

Global SaveGameAmount%

Function LoadSaveGames()
	CatchErrors("Uncaught (LoadSaveGames)")
	Local I_SAV.Save
	
	For I_SAV.Save = Each Save
		Delete I_SAV
	Next
	SaveGameAmount = 0
	
	If m_I <> Null And m_I\CurrentSave = "" Then
		m_I\CurrentSave = GetINIString(gv\OptionFile, "options", "last save")
	EndIf
	
	If FileType(SavePath)=1 Then RuntimeError "Can't create dir "+Chr(34)+SavePath+Chr(34)
	If FileType(SavePath)=0 Then CreateDir(SavePath)
	Local myDir=ReadDir(SavePath) 
	Local found% = False
	Repeat
		Local file$=NextFile$(myDir)
		If file$="" Then Exit 
		If FileType(SavePath+"\"+file$) = 2 Then 
			If file <> "." And file <> ".." Then 
				If (FileType(SavePath + file + "\main.sav")=1) Then
					SaveGameAmount=SaveGameAmount+1
					Local NEW_SAV.Save = New Save
					NEW_SAV\Name = file
					Local f% = ReadFile(SavePath + file + "\main.sav")
					NEW_SAV\Version = ReadString(f)
					NEW_SAV\Time = ReadString(f)
					NEW_SAV\Date = ReadString(f)
					NEW_SAV\Gamemode = ReadByte(f) - 1
					NEW_SAV\DNumber = ReadInt(f)
					NEW_SAV\ZoneString = ReadString(f)
					CloseFile f
					If m_I <> Null And m_I\CurrentSave = file Then
						found% = True
					EndIf
				EndIf
			EndIf
		EndIf 
	Forever 
	CloseDir myDir
	
	If m_I <> Null And (Not found) Then
		m_I\CurrentSave = ""
	EndIf
	
	CatchErrors("LoadSaveGames")
End Function

Function DeleteGame(I_SAV.Save)
	I_SAV\Name = SavePath + I_SAV\Name + "\"
	Local delDir% = ReadDir(I_SAV\Name)
	If delDir <> 0 Then
		NextFile(delDir) : NextFile(delDir)
		Local file$=NextFile(delDir)
		While file<>""
			DeleteFile(I_SAV\Name + file)
			file=NextFile$(delDir)
		Wend
		CloseDir(delDir)
		DeleteDir(I_SAV\Name)
	EndIf
	Delete I_SAV
End Function

Function SaveChaptersValueFile%()
	Local File%
	
	File = WriteFile(GetEnv("AppData") + "\scp-security-stories\Chapters_Data.scpss")
	WriteFloat(File, cpt\Unlocked)
	WriteFloat(File, cpt\NTFUnlocked)
	WriteFloat(File, cpt\DUnlocked)
	
	CloseFile(File)
End Function

Function LoadChaptersValueFile%()
	
	If FileType(GetEnv("AppData") + "\scp-security-stories\Chapters_Data.scpss") <> 1 Then Return
	
	Local File%
	
	File = OpenFile(GetEnv("AppData") + "\scp-security-stories\Chapters_Data.scpss")
	cpt\Unlocked = ReadFloat(File)
	cpt\NTFUnlocked = ReadFloat(File)
	cpt\DUnlocked = ReadFloat(File)
	
	CloseFile(File)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS