
Include "SourceCode\Events\096_Spawn.bb"
Include "SourceCode\Events\106_Sinkhole.bb"
Include "SourceCode\Events\106_Victim.bb"
Include "SourceCode\Events\682_Roar.bb"
Include "SourceCode\Events\1048_A.bb"
Include "SourceCode\Events\ClassD_Spawns.bb"
Include "SourceCode\Events\Room_GW.bb"
Include "SourceCode\Events\Room2_Trick.bb"
Include "SourceCode\Events\Room2_Tunnel_106.bb"
Include "SourceCode\Events\Room3_Servers.bb"
Include "SourceCode\Events\Room4_Tunnel.bb"
Include "SourceCode\Events\Room4_1.bb"
Include "SourceCode\Events\Distant_Explosion.bb"
Include "SourceCode\Events\Random_Loot.bb"

Const MaxEventStates% = 20
Const MaxEventChannels% = 4
Const MaxEventSounds% = 2

Type Events
	Field EventName$
	Field room.Rooms
	Field EventState#[MaxEventStates]
	Field SoundCHN[MaxEventChannels]
	Field SoundCHN_isStream[MaxEventChannels]
	Field Sound[MaxEventSounds]
	Field EventStr$
	Field img%
End Type 

Function CreateEvent.Events(eventname$, roomname$, id%, prob# = 0.0)
	;roomname = the name of the room(s) you want the event to be assigned to
	
	;the id-variable determines which of the rooms the event is assigned to,
	;0 will assign it to the first generated room, 1 to the second, etc
	
	;the prob-variable can be used to randomly assign events into some rooms
	;0.5 means that there's a 50% chance that event is assigned to the rooms
	;1.0 means that the event is assigned to every room
	;the id-variable is ignored if prob <> 0.0
	
	Local i% = 0, temp%, e.Events, e2.Events, r.Rooms
	
	If prob = 0.0 Then
		For r.Rooms = Each Rooms
			If (roomname = "" Lor roomname = r\RoomTemplate\Name) Then
				temp = False
				For e2.Events = Each Events
					If e2\room = r Then temp = True : Exit
				Next
				
				i=i+1
				If i >= id And temp = False Then
					e.Events = New Events
					e\EventName = eventname					
					e\room = r
					LoadEventAssets(e)
					Return e
				EndIf
			EndIf
		Next
	Else
		For r.Rooms = Each Rooms
			If (roomname = "" Lor roomname = r\RoomTemplate\Name) Then
				temp = False
				For e2.Events = Each Events
					If e2\room = r Then temp = True : Exit
				Next
				
				If Rnd(0.0, 1.0) < prob And temp = False Then
					e.Events = New Events
					e\EventName = eventname
					e\room = r
					LoadEventAssets(e)
				EndIf
			EndIf
		Next		
	EndIf
	
	Return Null
End Function

Function LoadEventAssets(e.Events)
	
	Select e\EventName
		Case "106_sinkhole"
			CreateEvent_106_Sinkhole(e)
		Case "room2_gw_b"
			CreateEvent_Room2_GW_B(e)
		;Case "room2_elevator_alt_2"
		;	CreateEvent_Room2_Elevator_1_Alt_2(e)
		;Case "room2_elevator_2"
		;	CreateEvent_Room2_Elevator_2(e)
	End Select
	
End Function

Function InitEvents()
	Local e.Events
	
	SeedRnd GenerateSeedNumber(RandomSeed)
	
	CreateEvent("pocketdimension", "pocketdimension", 0)
	
	CreateEvent("gate_a_intro","gate_a_intro",0)
	CreateEvent("gate_a_road","gate_a_road",0)
	CreateEvent("gate_a_topside","gate_a_topside",0)
	CreateEvent("gate_b_topside","gate_b_topside",0)
	CreateEvent("gate_c_topside","gate_c_topside",0)
	CreateEvent("gate_d_topside","gate_d_topside",0)
	
	CreateEvent("gate_a_entrance","gate_a_entrance",0)
	CreateEvent("gate_b_entrance","gate_b_entrance",0)
	CreateEvent("gate_c_entrance","gate_c_entrance",0)
	CreateEvent("gate_d_entrance","gate_d_entrance",0)
	
	If gopt\GameMode <> GAMEMODE_UNKNOWN Then
		CreateEvent("cores","core_ez",0,1.0)
		CreateEvent("cores","core_hcz",0,1.0)
		CreateEvent("cores","core_lcz",0,1.0)
		
		CreateEvent("checkpoints","checkpoint_bcz",0,1.0)
		CreateEvent("checkpoints","checkpoint_rcz",0,1.0)
		If gopt\GameMode = GAMEMODE_NTF Then
			CreateEvent("checkpoint_hcz_106","checkpoint_hcz",0,1.0)
		Else
			CreateEvent("checkpoints","checkpoint_hcz",0,1.0)
		EndIf
	EndIf
	
	CreateEvent("class_d_cells_entrance","class_d_cells_entrance",0,1.0)
	CreateEvent("class_d_cells","class_d_cells",0,1.0)
	
	CreateEvent("room2_offices_3","room2_offices_3",0)
	CreateEvent("room2_offices_5","room2_offices_5",0)
	
	CreateEvent("personnel_offices","personnel_offices",0)
	CreateEvent("room1_sky_office","room1_sky_office",0)
	CreateEvent("room1_elevators","room1_elevators",0)
	CreateEvent("room1_o5","room1_o5",0)
	
	CreateEvent("room1_intro","room1_intro",0)
	CreateEvent("room1_start","room1_start",0)
	
	CreateEvent("room3_ct","room3_ct",0)
	
	e = CreateEvent("room1_sewers", "room1_sewers", 0)
	If e <> Null And gopt\GameMode <> GAMEMODE_NTF Then
		e\EventState[0] = 8
		e\EventState[1] = 1.0
	EndIf
	
	CreateEvent("facility_reactor_entrance","facility_reactor_entrance",0)
	CreateEvent("facility_reactor","facility_reactor",0)
	CreateEvent("pocketdimension","pocketdimension",0)
	
	CreateEvent("area_2935_entrance", "area_2935_entrance", 0, 1.0)
	CreateEvent("area_106_escape", "area_106_escape", 0, 1.0)
	CreateEvent("area_076", "area_076", 0, 1.0)
	CreateEvent("area_076_entrance", "area_076_entrance", 0, 1.0)
	CreateEvent("area_035_ntf_encounter", "area_035_ntf_encounter", 0, 1.0)
	
	CreateEvent("cont_106","cont_106",0)
	CreateEvent("cont_008","cont_008",0)
	CreateEvent("cont_016","cont_016",0)
	CreateEvent("cont_409","cont_409",0)
	CreateEvent("cont_173","cont_173",0)
	CreateEvent("cont_049","cont_049",0)
	CreateEvent("cont_079","cont_079",0)
	CreateEvent("cont_457","cont_457",0)
	CreateEvent("cont_966","cont_966",0)
	CreateEvent("cont_1162","cont_1162",0)
	CreateEvent("cont_372_914","cont_372_914",0)
	CreateEvent("testroom_860","testroom_860", 0)
	CreateEvent("cont_500_1499","cont_500_1499",0)
	CreateEvent("cont_427_714_860_1025","cont_427_714_860_1025",0)
	CreateEvent("cont_207_268_1079_1033ru","cont_207_268_1079_1033ru",0)
	
	CreateEvent("room2_lws","room2_lws",0,1.0)
	CreateEvent("room2c_hws","room2c_hws",0,1.0)
	CreateEvent("room2_ws","room2_ws",0,1.0)
	CreateEvent("room2c_ews","room2c_ews",0,1.0)
	CreateEvent("room1_sws","room1_sws",0,1.0)
	
	CreateEvent("testroom_2b","testroom_2b", 0)
	CreateEvent("room2_medibay","room2_medibay",0)
	CreateEvent("room2_maintenance","room2_maintenance",0)
	CreateEvent("room2_nuke","room2_nuke",0)
	CreateEvent("room2_pipes_1","room2_pipes_1",0)
	CreateEvent("room2_pit","room2_pit",0)
	CreateEvent("room2_tunnel_2","room2_tunnel_2",0)
	CreateEvent("room3_pit","room3_pit",0)
	CreateEvent("room3_tunnel_1","room3_tunnel_1",0)
	CreateEvent("room3_tunnel_2","room3_tunnel_2",0)
	CreateEvent("room_gw","room2_gw_a",0,1.0)
	CreateEvent("surveil_room","surveil_room",0)
	CreateEvent("room2_gw_b","room2_gw_b",Rand(0,1))
	CreateEvent("checkpoint_acz","checkpoint_acz",0)
	CreateEvent("room2_closets", "room2_closets", 0)
	CreateEvent("room2_2", "room2_2", 0, 1.0)
	CreateEvent("room2_ventilation", "room2_ventilation", 0, 1.0)
	CreateEvent("room4_ventilation", "room4_ventilation", 0, 1.0)
	CreateEvent("toilet_guard", "room2_toilets", 1)
	CreateEvent("buttghost", "room2_toilets", 0, 0.8)
	CreateEvent("room2_tesla", "room2_tesla_lcz", 0, 0.9)
	CreateEvent("room2_tesla", "room2_tesla_hcz", 0, 0.9)
	CreateEvent("room1_archive", "room1_archive", 0, 1.0)
	
	If Rand(3)<3 Then CreateEvent("lockroom_1", "lockroom_1", 0)
	CreateEvent("lockroom_1", "lockroom_1", 0, 0.3 + (0.5*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("room2_doors", "room2_doors", 0, 0.5 + (0.4*SelectedDifficulty\AggressiveNPCs))
	
	CreateEvent("1048_a", "room2_1", 0, 0.3 + (0.3*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("1048_a", "room4_1", 0, 0.3 + (0.3*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("1048_a", "room2_tunnel_1", 0, 0.3 + (0.3*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("1048_a", "room4_tunnel", 0, 0.3 + (0.3*SelectedDifficulty\AggressiveNPCs))
	
	CreateEvent("096_spawn","room4_pit",0,0.6+(0.2*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("096_spawn","room3_pit",0,0.6+(0.2*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("096_spawn","room2_pipes_1",0,0.4+(0.2*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("096_spawn","room2_pit",0,0.5+(0.2*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("096_spawn","room3_tunnel",0,0.6+(0.2*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("096_spawn","room4_tunnel",0,0.7+(0.2*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("096_spawn","room2_tunnel_1",0,0.6+(0.2*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("096_spawn","room2_tunnel_2",0,0.4+(0.2*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("096_spawn","room3_hcz",0,0.7+(0.2*SelectedDifficulty\AggressiveNPCs))
	
	CreateEvent("classd_spawn","room2_1", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","room2_2", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","room2_3", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","room2_4", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","room2_5", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","room2_tunnel_1", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","room2_tunnel_2", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","room2_tunnel_3", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","room2c_tunnel", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","room3_3", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","room4_1", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","room4_2", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","room3_tunnel", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","room4_ez", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","endroom_1", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","endroom_2", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","endroom_3", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","room2_pipes_2", 0, 0.2 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","room2_pit_lcz", 0, 0.35 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","surveil_room", 0, 0.9 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","testroom_2b", 0, 0.5 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("classd_spawn","cont_914_372", 0, 0.5 + (0.1*SelectedDifficulty\AggressiveNPCs))
	
	CreateEvent("zombie_spawn", "room3_reception", 0, 0.5 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("zombie_spawn", "room2_offices_bcz", 0, 0.9 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("zombie_spawn", "room1_2_bcz", 0, 0.35 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("zombie_spawn", "room2_bcz", 0, 0.8 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("zombie_spawn", "room3_bcz", 0, 0.4 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("zombie_spawn", "room3_1", 0, 0.4 + (0.1*SelectedDifficulty\AggressiveNPCs))
	CreateEvent("zombie_spawn", "room3_2", 0, 0.4 + (0.1*SelectedDifficulty\AggressiveNPCs))
	
	CreateEvent("random_loot","room2_rcz",0);,0.3)
	;CreateEvent("random_loot","room3_rcz",0,0.3)
	;CreateEvent("random_loot","room3_2_rcz",0,0.3)
	;CreateEvent("random_loot","room4_rcz",0,0.3)
	
	If Rand(5)<5 Then
		Select Rand(3)
			Case 1
				If Rand(2) = 1 Then
					CreateEvent("682_roar", "room2_tunnel_1", Rand(0,2))
				Else
					CreateEvent("distant_explosion", "room2_tunnel_1", Rand(0,2))
				EndIf
			Case 2
				If Rand(2) = 1 Then
					CreateEvent("682_roar", "room3_pit", Rand(0,2))
				Else
					CreateEvent("distant_explosion", "room3_pit", Rand(0,2))
				EndIf
			Case 3
				If Rand(2) = 1 Then
					CreateEvent("682_roar", "room2_ez_1", 0)
				Else
					CreateEvent("distant_explosion", "room2_ez_1", 0)
				EndIf
		End Select 
	EndIf
	If Rand(4) = 1 Then
		CreateEvent("682_roar", "room2_5", 0, 0.5)
	ElseIf Rand(4) = 2 Then
		CreateEvent("distant_explosion", "room2_5", 0, 1)
	ElseIf Rand(4) = 3 Then
		CreateEvent("classd_spawn", "room2_5", 0, 0.3 + (0.1*SelectedDifficulty\AggressiveNPCs))
	Else
		CreateEvent("zombie_spawn", "room2_5", 0, 0.4 + (0.1*SelectedDifficulty\AggressiveNPCs))
	EndIf
	
	If Rand(3) = 1 Then
		CreateEvent("distant_explosion", "room2_rocky", 0, 0.5)
	EndIf
	
End Function

Function RemoveEvent(e.Events)
	If e\Sound[0]<>0 Then FreeSound_Strict e\Sound[0]
	If e\Sound[1]<>0 Then FreeSound_Strict e\Sound[1]
	If e\img<>0 Then FreeImage e\img
	Delete e
End Function

Function UpdateEvents()
	CatchErrors("UpdateEvents")
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%
	
	Local p.Particles, n.NPCs, r.Rooms, e.Events, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams
	Local rt.RoomTemplates, ne.NewElevator
	
	Local CurrTrigger$ = ""
	
	Local x#, y#, z#
	
	Local angle#
	
	CurrStepSFX = 0
	
	UpdateRooms()
	
	UpdateBiggerParticles = False
	
	NTF_DisableLight = False
	
	;CoffinDistance = 1000.0
	
	For e.Events = Each Events
		If e<>Null Then
			CatchErrors(Chr(34)+e\EventName+Chr(34)+" event")
		Else
			CatchErrors("Deleted event")
		EndIf
		Select e\EventName
			Case "cores"
				UpdateEvent_Cores(e)
			Case "checkpoints"
				UpdateEvent_Checkpoints(e)
			Case "checkpoint_hcz_106"
				UpdateEvent_Checkpoint_HCZ_106(e)
			Case "class_d_cells_entrance"
				UpdateEvent_ClassD_Cells_Checkpoint(e)
			Case "class_d_cells"
				UpdateEvent_ClassD_Cells_Zone(e)
			Case "gate_a_intro"
				UpdateEvent_Gate_A_Intro(e)
			Case "gate_a_road"
				UpdateEvent_Gate_A_Road(e)
			Case "gate_a_topside"
				UpdateEvent_Gate_A_Topside(e)
			Case "gate_b_topside"
				UpdateEvent_Gate_B_Topside(e)
			Case "gate_c_topside"
				UpdateEvent_Gate_C_Topside(e)
			Case "gate_d_topside"
				UpdateEvent_Gate_D_Topside(e)
			Case "gate_a_entrance"
				UpdateEvent_Gate_A_Entrance(e)
			Case "gate_b_entrance"
				UpdateEvent_Gate_B_Entrance(e)
			Case "gate_c_entrance"
				UpdateEvent_Gate_C_Entrance(e)
			Case "gate_d_entrance"
				UpdateEvent_Gate_D_Entrance(e)
			Case "room1_sky_office"
				UpdateEvent_Room1_Sky_Office(e)
			Case "room1_elevators"
				UpdateEvent_Room1_Elevators(e)
			Case "area_076_entrance"
				UpdateEvent_Area_076_Entrance(e)
			Case "area_076"
				UpdateEvent_Area_076(e)
			Case "cont_106"
				UpdateEvent_Cont_106(e)
			Case "room1_sws"
				UpdateEvent_Room1_Sws(e)
			Case "room1_o5"
				UpdateEvent_Room1_O5(e)
			Case "room2_offices_3"
				UpdateEvent_Room2_Offices_3(e)
			Case "room2_offices_5"
				UpdateEvent_Room2_Offices_5(e)
			Case "personnel_offices"
				UpdateEvent_Personnel_Offices(e)
			Case "room1_intro"
				UpdateEvent_Room1_Intro(e)
			Case "room1_start"
				UpdateEvent_Room1_Start(e)
			Case "room1_sewers"
				UpdateEvent_Room1_Sewers(e)
			Case "room2_maintenance"
				UpdateEvent_Room2_Maintenance(e)
			Case "room2_medibay"
				UpdateEvent_Room2_Medibay(e)
			Case "cont_008"
				UpdateEvent_Cont_008(e)
			Case "cont_016"
				UpdateEvent_Cont_016(e)
			Case "cont_409"
				UpdateEvent_Cont_409(e)
			Case "room2_nuke"
				UpdateEvent_Room2_Nuke(e)
			Case "room2_pipes_1"
				UpdateEvent_Room2_Pipes_1(e)
			Case "room2_pit"
				UpdateEvent_Room2_Pit(e)
			Case "room2_tunnel_2"
				UpdateEvent_Room2_Tunnel_2(e)
			Case "room3_pit"
				UpdateEvent_Room3_Pit(e)
			Case "room3_tunnel_1"
				UpdateEvent_Room3_Tunnel_1(e)
			Case "room3_tunnel_2"
				UpdateEvent_Room3_Tunnel_2(e)
			Case "area_2935_entrance"
				UpdateEvent_Area_2935_Entrance(e)
			Case "area_106_escape"
				UpdateEvent_Area_106_Escape(e)
			Case "area_035_ntf_encounter"
				UpdateEvent_Area_035_NTF_Encounter(e)
			Case "cont_173"
				UpdateEvent_Cont_173(e)
			Case "cont_049"
				UpdateEvent_Cont_049(e)
			Case "cont_079"
				UpdateEvent_Cont_079(e)
			Case "cont_457"
				UpdateEvent_Cont_457(e)
			Case "cont_966"
				UpdateEvent_Cont_966(e)
			Case "room3_ct"
				UpdateEvent_Room3_CT(e)
			Case "cont_500_1499"
				UpdateEvent_Cont_500_1499(e)
			Case "cont_372_914"
				UpdateEvent_Cont_372_914(e)
			Case "cont_427_714_860_1025"
				UpdateEvent_Cont_427_714_860_1025(e)
			Case "cont_207_268_1079_1033ru"
				UpdateEvent_Cont_207_268_1079_1033RU(e)
			Case "cont_1162"
				UpdateEvent_Cont_1162(e)
			Case "testroom_860"
				UpdateEvent_Testroom_860(e)
			Case "testroom_2b"
				UpdateEvent_Testroom_2B(e)
			Case "lockroom_1"
				UpdateEvent_Lockroom_1(e)
			Case "checkpoint_acz"
				UpdateEvent_Checkpoint_ACZ(e)
			Case "facility_reactor_entrance"
				UpdateEvent_Facility_Reactor_Entrance(e)
			Case "facility_reactor"
				UpdateEvent_Facility_Reactor(e)
			Case "pocketdimension"
				UpdateEvent_Pocketdimension(e)
			Case "room1_archive"
				UpdateEvent_Room1_Archive(e)
			Case "room2_2"
				UpdateEvent_Room2_2(e)
			Case "room2_ventilation"
				UpdateEvent_Room2_Ventilation(e)
			Case "room4_ventilation"
				UpdateEvent_Room4_Ventilation(e)
			Case "room2_closets"
				UpdateEvent_Room2_Closets(e)
			Case "room2_doors"
				UpdateEvent_Room2_Doors(e)
			Case "room_gw"
				UpdateEvent_Room_GW(e)
			Case "room2_gw_b"
				UpdateEvent_Room2_GW_B(e)
			Case "surveil_room"
				UpdateEvent_Surveil_Room(e)
			Case "room2_lws"
				UpdateEvent_Room2_Lws(e)
			Case "room2c_hws"
				UpdateEvent_Room2C_Hws(e)
			Case "room2_ws"
				UpdateEvent_Room2_Ws(e)
			Case "room2c_ews"
				UpdateEvent_Room2C_Ews(e)
			Case "096_spawn"
				UpdateEvent_096_Spawn(e)
			Case "106_victim"
				UpdateEvent_106_Victim(e)
			Case "106_sinkhole"
				UpdateEvent_106_Sinkhole(e)
			Case "682_roar"
				UpdateEvent_682_Roar(e)
			Case "1048_a"
				UpdateEvent_1048_A(e)
			Case "classd_spawn", "classd_spawn_group"
				UpdateEvent_ClassD_Spawn(e)
			Case "zombie_spawn"
				UpdateEvent_Zombie_Spawn(e)
			Case "room3_servers"
				UpdateEvent_Room3_Servers(e)
			Case "room4_1"
				UpdateEvent_Room4_1(e)
			Case "room4_tunnel"
				UpdateEvent_Room4_Tunnel(e)
			Case "buttghost"
				UpdateEvent_Buttghost(e)
			Case "distant_explosion"
				UpdateEvent_Distant_Explosion(e)
			Case "random_loot"
				UpdateEvent_Random_Loot(e, e\room\RoomTemplate\Name)
	;		Case "classd_spawn_group"
	;			UpdateEvent_ClassD_Spawn_Group(e)
			Case "room2_tesla"
				UpdateEvent_Room2_Tesla(e)
			Case "testroom_2b"
				UpdateEvent_Testroom_2B(e)
			Case "toilet_guard"
				UpdateEvent_Toilet_Guard(e)
		End Select
	Next
	
	If ExplosionTimer > 0 Then
		ExplosionTimer = ExplosionTimer+FPSfactor
		
		If ExplosionTimer < 140.0 Then
			If ExplosionTimer-FPSfactor < 5.0 Then
				If PlayerRoom\RoomTemplate\Name <> "facility_reactor" Then
					ExplosionSFX = LoadSound_Strict("SFX\Ending\GateB\Nuke1.ogg")
				Else
					ExplosionSFX = LoadSound_Strict("SFX\General\Reactor_Explosion_1.ogg")
				EndIf
				PlaySound_Strict ExplosionSFX
				BigCameraShake = 10.0
				ExplosionTimer = 5.0
			EndIf
			
			BigCameraShake = CurveValue(ExplosionTimer/60.0,BigCameraShake, 50.0)
		Else
			BigCameraShake = Min((ExplosionTimer/20.0),20.0)
			If ExplosionTimer-FPSfactor < 140.0 Then
				BlinkTimer = 1.0
				If PlayerRoom\RoomTemplate\Name <> "facility_reactor" Then
					ExplosionSFX = LoadSound_Strict("SFX\Ending\GateB\Nuke2.ogg")
				Else
					ExplosionSFX = LoadSound_Strict("SFX\General\Reactor_Explosion_2.ogg")
				EndIf
				PlaySound_Strict ExplosionSFX				
				For i = 0 To (10+(10*(ParticleAmount+1)))
					p.Particles = CreateParticle(EntityX(Collider)+Rnd(-0.5,0.5),EntityY(Collider)-Rnd(0.2,1.5),EntityZ(Collider)+Rnd(-0.5,0.5),0, Rnd(0.2,0.6), 0.0, 350)	
					RotateEntity p\pvt,-90,0,0,True
					p\speed = Rnd(0.05,0.07)
				Next
			EndIf
			LightFlash = Min((ExplosionTimer-140.0)/10.0,5.0)
			
			If ExplosionTimer > 160 Then KillTimer = Min(KillTimer,-0.1)
			If ExplosionTimer > 500 Then ExplosionTimer = 0
			
			PositionEntity Collider, EntityX(Collider), 200, EntityZ(Collider)
		EndIf
	EndIf
	
	CatchErrors("UpdateEvents (Uncaught)")
End Function

Function QuickLoadEvents()
	CatchErrors("Uncaught (QuickLoadEvents)")
	
	If QuickLoad_CurrEvent = Null Then
		QuickLoadPercent = -1
		Return
	EndIf
	
	Local e.Events = QuickLoad_CurrEvent
	
	Local r.Rooms,sc.SecurityCams,sc2.SecurityCams,scale#,pvt%,n.NPCs,tex%,i%,x#,z#
	
	;might be a good idea to use QuickLoadPercent to determine the "steps" of the loading process 
	;instead of magic values in e\eventstate[0] and e\eventStr
	
	Select e\EventName
		Case "room2_closets"
			;[Block]
			If e\EventState[0] = 0
				If e\EventStr = "load0"
					QuickLoadPercent = 10
					If e\room\NPC[0]=Null Then
						e\room\NPC[0] = CreateNPC(NPC_Human, EntityX(e\room\Objects[0],True),EntityY(e\room\Objects[0],True),EntityZ(e\room\Objects[0],True))
					EndIf
					
					ChangeNPCTexture(e\room\NPC[0],"GFX\npcs\janitor.jpg")
					e\EventStr = "load1"
				ElseIf e\EventStr = "load1"
					QuickLoadPercent = 20
					e\room\NPC[0]\Sound=LoadSound_Strict("SFX\Room\Storeroom\Escape1.ogg")
					e\EventStr = "load2"
				ElseIf e\EventStr = "load2"
					QuickLoadPercent = 35
					e\room\NPC[0]\SoundChn = PlaySound2(e\room\NPC[0]\Sound, Camera, e\room\NPC[0]\Collider, 12)
					e\EventStr = "load3"
				ElseIf e\EventStr = "load3"
					QuickLoadPercent = 55
					If e\room\NPC[1]=Null Then
						e\room\NPC[1] = CreateNPC(NPC_Human, EntityX(e\room\Objects[1],True),EntityY(e\room\Objects[1],True),EntityZ(e\room\Objects[1],True))
					EndIf
					
					ChangeNPCTexture(e\room\NPC[1],"GFX\npcs\scientist.jpg")
					e\EventStr = "load4"
				ElseIf e\EventStr = "load4"
					QuickLoadPercent = 80
					e\room\NPC[1]\Sound=LoadSound_Strict("SFX\Room\Storeroom\Escape2.ogg")
					e\EventStr = "load5"
				ElseIf e\EventStr = "load5"
					QuickLoadPercent = 100
					PointEntity e\room\NPC[0]\Collider, e\room\NPC[1]\Collider
					PointEntity e\room\NPC[1]\Collider, e\room\NPC[0]\Collider
					
					e\EventState[0]=1
				EndIf
			EndIf
			;[End Block]
		Case "testroom_860"
			;[Block]
			If e\EventStr = "load0"
				QuickLoadPercent = 15
				ForestNPC = CreateSprite()
				;0.75 = 0.75*(410.0/410.0) - 0.75*(width/height)
				ScaleSprite ForestNPC,0.75*(140.0/410.0),0.75
				SpriteViewMode ForestNPC,4
				EntityFX ForestNPC,1+8
				ForestNPCTex = LoadAnimTexture("GFX\npcs\AgentIJ.AIJ",1+2,140,410,0,4)
				ForestNPCData[0] = 0
				EntityTexture ForestNPC,ForestNPCTex,ForestNPCData[0]
				ForestNPCData[1]=0
				ForestNPCData[2]=0
				HideEntity ForestNPC
				e\EventStr = "load1"
			ElseIf e\EventStr = "load1"
				QuickLoadPercent = 40
				e\EventStr = "load2"
			ElseIf e\EventStr = "load2"
				QuickLoadPercent = 100
				If e\room\NPC[0]=Null Then e\room\NPC[0]=CreateNPC(NPC_SCP_860, 0,0,0)
				e\EventStr = "loaddone"
			EndIf
			;[End Block]
		Case "cont_966"
			;[Block]
			If e\EventState[0] = 1
				e\EventState[1] = e\EventState[1]+FPSfactor
				If e\EventState[1]>30 Then
					If e\EventStr = ""
						CreateNPC(NPC_SCP_966, EntityX(e\room\Objects[0],True), EntityY(e\room\Objects[0],True), EntityZ(e\room\Objects[0],True))
						QuickLoadPercent = 50
						e\EventStr = "load0"
					ElseIf e\EventStr = "load0"
						CreateNPC(NPC_SCP_966, EntityX(e\room\Objects[2],True), EntityY(e\room\Objects[2],True), EntityZ(e\room\Objects[2],True))
						QuickLoadPercent = 100
						e\EventState[0]=2
					EndIf
				Else
					QuickLoadPercent = Int(e\EventState[1])
				EndIf
			EndIf
			;[End Block]
	End Select
	
	CatchErrors("QuickLoadEvents "+e\EventName)
	
End Function

Function UpdateEventValues()
	; ~ Event after chapter - 6
	If cpt\Current = 6 Then
		If ecst\WasIn076 Then
			If ecst\After076Timer > -1 And ecst\After076Timer < 70*61 Then
				ecst\After076Timer = ecst\After076Timer + FPSfactor
			Else
				ecst\After076Timer = -1
			EndIf
		EndIf
		
		If ecst\After076Timer > 70*60.0 And ecst\After076Timer < 70*60.01 Then
			If TaskExists(TASK_COME_BACK_TO_JANITOR) Then
				CancelTask(TASK_COME_BACK_TO_JANITOR)
			EndIf
			If (Not TaskExists(TASK_FIND_CAVES)) Then
				BeginTask(TASK_FIND_CAVES)
				ecst\NewCavesEvent = True
				PlayPlayerSPVoiceLine("SFX\Player\Voice\Ryan\longgeorgeradio")
			EndIf
		EndIf
	EndIf
	; ~ Intercom event
	If cpt\Current = 6 And ecst\WasIn076 Then
		ecst\IntercomEnabled = True
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS