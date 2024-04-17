
Function FillRoom_Cont_457(r.Rooms)
	Local ne.NewElevator,it.Items
	
	Local ElevatorOBJ% = LoadRMesh("GFX\map\Elevators\elevator_cabin_3.rmesh",Null)
	HideEntity ElevatorOBJ
	r\Objects[0] = CopyEntity(ElevatorOBJ,r\obj)
	r\Objects[1] = CopyEntity(ElevatorOBJ,r\obj)
	
	PositionEntity(r\Objects[0],990,1,-577)
	RotateEntity r\Objects[0],0,270,0
	EntityType r\Objects[0],HIT_MAP
	EntityPickMode r\Objects[0],2
	
	PositionEntity(r\Objects[1],1870,1,608)
	EntityType r\Objects[1],HIT_MAP
	EntityPickMode r\Objects[1],2
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x + 992.0 * RoomScale, r\y, r\z-258*RoomScale, 0, r, True, DOOR_ELEVATOR_3FLOOR, False, "", 1)
	r\RoomDoors[1] = CreateDoor(r\zone,r\x + 1556.0 * RoomScale, r\y, r\z+608*RoomScale, 90, r, True, DOOR_ELEVATOR_3FLOOR, False, "", 2)
	r\RoomDoors[0]\DisableWaypoint = True
	r\RoomDoors[1]\DisableWaypoint = True
	
	r\RoomDoors[2] = CreateDoor(r\zone,r\x + 1304.0 * RoomScale, r\y, r\z+36*RoomScale, 119, r, False, DOOR_WINDOWED, SEVERED_HAND_3)
	
	r\RoomDoors[3] = CreateDoor(r\zone,r\x -8384.0 * RoomScale, r\y+4608*RoomScale, r\z-6208*RoomScale, 0, r, True, DOOR_CONTAINMENT)
	r\RoomDoors[3]\buttons[0] = FreeEntity_Strict(r\RoomDoors[3]\buttons[0]) : r\RoomDoors[3]\buttons[1] = FreeEntity_Strict(r\RoomDoors[3]\buttons[1])
	ScaleEntity r\RoomDoors[3]\obj,40, 68, 40
	ScaleEntity r\RoomDoors[3]\obj2,40, 68, 40
	
	r\RoomDoors[4] = CreateDoor(r\zone,r\x -6816.0 * RoomScale, r\y+4607*RoomScale, r\z-6241*RoomScale, 0, r, False, DOOR_DEFAULT, KEY_CARD_4)
	
	ne = CreateNewElevator(r\Objects[0],1,r\RoomDoors[0],1,r,1,2095,4607)
	ne\floorlocked[1] = True
	ne = CreateNewElevator(r\Objects[1],1,r\RoomDoors[1],2,r,1,2095,4607)
	ne\floorlocked[1] = True
	
	r\Objects[2] = CreatePivot()
	PositionEntity r\Objects[2],r\x-674*RoomScale,r\y+4667*RoomScale,r\z-2362*RoomScale
	EntityParent r\Objects[2],r\obj
	
	r\Objects[3] = CreatePivot()
	PositionEntity r\Objects[3],r\x-7536*RoomScale,r\y+4676*RoomScale,r\z-6941*RoomScale
	EntityParent r\Objects[3],r\obj
	
	r\Objects[4] = CreatePivot()
	PositionEntity r\Objects[4],r\x-6851*RoomScale,r\y+4700*RoomScale,r\z-5826*RoomScale
	EntityParent r\Objects[4],r\obj
	
	r\Objects[5] = CreatePivot()
	PositionEntity r\Objects[5],r\x-5366*RoomScale,r\y+5285*RoomScale,r\z-1394*RoomScale
	EntityParent r\Objects[5],r\obj
	
	r\Objects[6] = CreatePivot()
	PositionEntity r\Objects[6],r\x-5366*RoomScale,r\y+5285*RoomScale,r\z-1260*RoomScale
	EntityParent r\Objects[6],r\obj
	
	r\Objects[7] = CreatePivot()
	PositionEntity r\Objects[7],r\x-5366*RoomScale,r\y+5285*RoomScale,r\z-1500*RoomScale
	EntityParent r\Objects[7],r\obj
	
	Local em.Emitters = CreateEmitter(r\x-7536*RoomScale,r\y+5052*RoomScale,r\z-6941*RoomScale,4)
	TurnEntity(em\Obj, 0, -90, 0, True)
	EntityParent(em\Obj, r\obj) : em\Room = r
	em\RandAngle = 10 : em\Speed = 0.03
	em\SizeChange = 0.01 : em\Achange = -0.006
	em\Gravity = 0.4
	
	r\Levers[0] = CreateLever(r,r\x-6024*RoomScale,r\y+4832*RoomScale,r\z-6610*RoomScale)
	
	;it = CreateItem("Document SCP-457 Page 1/2", "paper", r\x + 860.0 * RoomScale, r\y + 4778.0 * RoomScale, r\z + 1183.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
	;it = CreateItem("Document SCP-457 Page 2/2", "paper", r\x - 7059.0 * RoomScale, r\y + 4778.0 * RoomScale, r\z -7146.0 * RoomScale)
	;EntityParent(it\collider, r\obj)
	
End Function

Function UpdateEvent_Cont_457(e.Events)
	Local ne.NewElevator,r.Rooms,i
	
	If PlayerRoom = e\room
		
		If (Not Curr173\Contained) Then
			If gopt\GameMode <> GAMEMODE_NTF Then
				If PlayerInNewElevator Then
					Curr173\Idle = SCP173_DISABLED
					HideEntity Curr173\obj
					HideEntity Curr173\obj2
					HideEntity Curr173\Collider
				Else
					Curr173\Idle = SCP173_ACTIVE
					If EntityHidden(Curr173\obj) Then ShowEntity Curr173\obj
					If EntityHidden(Curr173\obj2) Then ShowEntity Curr173\obj2
					If EntityHidden(Curr173\Collider) Then ShowEntity Curr173\Collider
				EndIf
			EndIf
		EndIf
		
		If EntityY(Collider)>4500.0*RoomScale ;Level 3
			
			Local p.Particles
			
			If ParticleAmount > 0 Then
				If Rand(20)=1 Then
					p.Particles = CreateParticle(EntityX(e\room\Objects[5],True),EntityY(e\room\Objects[5],True),EntityZ(e\room\Objects[5],True),2,0.015,0.05,150)
				;ElseIf Rand(20)=2 Then
				;	p.Particles = CreateParticle(EntityX(e\room\Objects[7],True),EntityY(e\room\Objects[7],True),EntityZ(e\room\Objects[7],True),2,0.015,0.05,150)
				;Else
				;	p.Particles = CreateParticle(EntityX(e\room\Objects[7],True),EntityY(e\room\Objects[7],True),EntityZ(e\room\Objects[7],True),2,0.015,0.05,150)
				EndIf
			EndIf
			
			If (Not PlayerInNewElevator)
				ShouldPlay = MUS_CONT_457
				
				If e\room\NPC[0] = Null Then
					e\room\NPC[0] = CreateNPC(NPC_SCP_457,EntityX(e\room\Objects[2],True),EntityY(e\room\Objects[2],True),EntityZ(e\room\Objects[2],True))
				EndIf
				If e\room\NPC[1] = Null Then
					e\room\NPC[1] = CreateNPC(NPC_human,EntityX(e\room\Objects[4],True),EntityY(e\room\Objects[4],True),EntityZ(e\room\Objects[4],True))
					RotateEntity e\room\NPC[1]\Collider,EntityX(e\room\NPC[1]\Collider,True),EntityY(e\room\NPC[1]\Collider,True)+180,EntityZ(e\room\NPC[1]\Collider,True)
					e\room\NPC[1]\State[0] = STATE_SCRIPT
					SetNPCFrame(e\room\NPC[1],558)
					e\room\NPC[1]\IsDead = True
					
					ChangeNPCTexture(e\room\NPC[1],"GFX\Npcs\457victim.jpg")
					
					Local bone% = FindChild(e\room\NPC[1]\obj,"Bip01_L_Hand")
					Local it.Items = CreateItem(GetLocalString("Item Names","key_4"), "key4",EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True))
					RotateEntity it\collider,EntityPitch(it\collider,True),e\room\angle+45,EntityRoll(it\collider,True),True
					;RotateEntity it\model,EntityPitch(it\model,True),EntityYaw(it\model,True),EntityRoll(it\model,True)+180,True
					;MoveEntity it\model, 0,-0.025,0
					EntityType it\collider, HIT_ITEM
				EndIf
				
				For i = 0 To 1
					If (Not e\room\NPC[0]\Contained) Then
						e\room\RoomDoors[i]\locked = True ; ~ Locking the elevators untill SCP-457 is contained.
					Else
						e\room\RoomDoors[i]\locked = False ; ~ Unlocking the elevators when SCP-457 is contained.
					EndIf
				Next
				
				If e\room\NPC[0] <> Null Then
					
					If e\EventState[1] = 0 Then
						If EntityDistanceSquared(e\room\NPC[0]\Collider,e\room\Objects[3]) < PowTwo(0.6) Then
							e\room\NPC[0]\State[0] = SCP457_STATE_STUNNED
							e\EventState[1] = 1
						EndIf
					Else
						If EntityDistanceSquared(e\room\NPC[0]\Collider,e\room\Objects[3]) > PowTwo(0.7) Then
							e\EventState[1] = 0
						EndIf
					EndIf
					If EntityDistanceSquared(e\room\NPC[0]\Collider,e\room\Objects[3]) < PowTwo(5.0) Then
						e\EventState[2] = 1
					Else
						e\EventState[2] = 0
					EndIf
					If e\EventState[2] = 1 Then
						UpdateLever(e\room\Levers[0]\obj,False)
					EndIf
					If EntityPitch(e\room\Levers[0]\obj,True) >= 60 Then ; ~ Cont Door Open
						If e\room\RoomDoors[3]\open = True
							UseDoor(e\room\RoomDoors[3])
							e\EventState[3] = 1
						EndIf
					ElseIf EntityPitch(e\room\Levers[0]\obj,True) <= -60 Then  ; ~ Cont Door Closed
						If e\room\RoomDoors[3]\open = False
							UseDoor(e\room\RoomDoors[3])
							e\EventState[3] = 0
						EndIf
					EndIf
					If e\EventState[3] = 1 Then
						e\EventState[5] = 1
						e\EventState[4] = e\EventState[4] + FPSfactor
					Else
						e\EventState[4] = 0
					EndIf
					If e\EventState[4] > 0 And e\EventState[4] < 70*10 Then
						e\room\NPC[0]\State[0] = SCP457_STATE_STUNNED
						e\room\NPC[0]\HP = e\room\NPC[0]\HP - 50
					EndIf
					If e\EventState[4] > 0 And e\EventState[4] < 70*0.02 Then
						PlaySound_Strict(LoadTempSound("SFX\Door\DoorDecont.ogg"))
					EndIf
					If e\room\NPC[0]\IsDead Then
						e\room\NPC[0]\Contained = True
					EndIf
				EndIf
				If e\room\NPC[1] <> Null Then
					If ParticleAmount > 0 Then
						If Rand(20-(10*(ParticleAmount-1)))=1 Then
							p.Particles = CreateParticle(EntityX(e\room\NPC[1]\Collider),EntityY(e\room\NPC[1]\obj)+0.25,EntityZ(e\room\NPC[1]\Collider)-0.2,0,0.05,0,60)
							p\speed = 0.002
							RotateEntity(p\pvt, 0, EntityYaw(e\room\NPC[1]\Collider), 0)
							MoveEntity p\pvt,Rnd(-0.1,0.1),0,0.1+Rnd(0,0.5)
							RotateEntity(p\pvt, -90, EntityYaw(e\room\NPC[1]\Collider), 0)
							p\Achange = -0.02
						EndIf
					EndIf
				EndIf
				
			EndIf
			
			If (Not PlayerInNewElevator)
				For i = 0 To 1
					PositionEntity e\room\RoomDoors[i]\frameobj,EntityX(e\room\RoomDoors[i]\frameobj),4607*RoomScale,EntityZ(e\room\RoomDoors[i]\frameobj)
					PositionEntity e\room\RoomDoors[i]\obj,EntityX(e\room\RoomDoors[i]\obj),4607*RoomScale,EntityZ(e\room\RoomDoors[i]\obj)
					PositionEntity e\room\RoomDoors[i]\obj2,EntityX(e\room\RoomDoors[i]\obj2),4607*RoomScale,EntityZ(e\room\RoomDoors[i]\obj2)
					PositionEntity e\room\RoomDoors[i]\buttons[0],EntityX(e\room\RoomDoors[i]\buttons[0]),4607*RoomScale+0.6,EntityZ(e\room\RoomDoors[i]\buttons[0])
					PositionEntity e\room\RoomDoors[i]\buttons[1],EntityX(e\room\RoomDoors[i]\buttons[1]),4607*RoomScale+0.7,EntityZ(e\room\RoomDoors[i]\buttons[1])
					For ne = Each NewElevator
						If ne\door = e\room\RoomDoors[i]
							If ne\currfloor = 3 And ne\state = 0.0
								e\room\RoomDoors[i]\open = True
							Else
								e\room\RoomDoors[i]\open = False
							EndIf
						EndIf
					Next
				Next
			EndIf
			
		Else ;Level 1
			
			If (Not PlayerInNewElevator)
				For i = 0 To 1
					PositionEntity e\room\RoomDoors[i]\frameobj,EntityX(e\room\RoomDoors[i]\frameobj),0.0,EntityZ(e\room\RoomDoors[i]\frameobj)
					PositionEntity e\room\RoomDoors[i]\obj,EntityX(e\room\RoomDoors[i]\obj),0.0,EntityZ(e\room\RoomDoors[i]\obj)
					PositionEntity e\room\RoomDoors[i]\obj2,EntityX(e\room\RoomDoors[i]\obj2),0.0,EntityZ(e\room\RoomDoors[i]\obj2)
					PositionEntity e\room\RoomDoors[i]\buttons[0],EntityX(e\room\RoomDoors[i]\buttons[0]),0.0+0.6,EntityZ(e\room\RoomDoors[i]\buttons[0])
					PositionEntity e\room\RoomDoors[i]\buttons[1],EntityX(e\room\RoomDoors[i]\buttons[1]),0.0+0.7,EntityZ(e\room\RoomDoors[i]\buttons[1])
					For ne = Each NewElevator
						If ne\door = e\room\RoomDoors[i]
							If ne\currfloor = 1 And ne\state = 0.0
								e\room\RoomDoors[i]\open = True
							Else
								e\room\RoomDoors[i]\open = False
							EndIf
						EndIf
					Next
				Next
			EndIf
			
		EndIf
		
	EndIf
	
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D