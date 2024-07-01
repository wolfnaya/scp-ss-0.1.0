
Function UpdateEvent_096_Spawn(e.Events)
	Local e2.Events,r.Rooms
	Local pvt%
	
	Local xspawn#,zspawn#,place%
	If e\room\dist < HideDistance*1.5
		;Checking some statements in order to determine if SCP-096 can spawn in this room
		;[Block]
		If e\EventState[0] <> 2
			If Curr096<>Null
				If EntityDistanceSquared(Curr096\Collider,Collider)<PowTwo(40)
					e\EventState[0] = 2
					;debuglog "Failed to spawn SCP-096 in room "+e\room\RoomTemplate\Name$
					;debuglog "- SCP-096 too close to player"
				EndIf
				
				For e2.Events = Each Events
					If e2\EventName = "room2_servers_1"
						If e2\EventState[0] > 0 And e2\room\NPC[0]<>Null
							e\EventState[0] = 2
							;debuglog "Failed to spawn SCP-096 in room "+e\room\RoomTemplate\Name$
							;debuglog "- room2_servers_1 event still in progress"
							Exit
						EndIf
					EndIf
				Next
				
				For r.Rooms = Each Rooms
					If r\RoomTemplate\Name = "checkpoint_hcz"
						If r\dist < 10
							e\EventState[0] = 2
							;debuglog "Failed to spawn SCP-096 in room "+e\room\RoomTemplate\Name$
							;debuglog "- too close to checkpoint_hcz"
							Exit
						EndIf
					EndIf
				Next
				
				If Curr096\State[0] <> 5
					e\EventState[0] = 2
					;debuglog "Failed to spawn SCP-096 in room "+e\room\RoomTemplate\Name$
					;debuglog "- SCP-096 enraged"
				EndIf
				
				If EntityDistanceSquared(Curr096\Collider,e\room\obj)>EntityDistanceSquared(Curr096\Collider,Collider)
					e\EventState[0] = 2
					;debuglog "Failed to spawn SCP-096 in room "+e\room\RoomTemplate\Name$
					;debuglog "- Room is too far away"
				EndIf
			EndIf
			For e2.Events = Each Events
				If e2\EventName = "room2_servers_1"
					If e2\EventState[0] = 0 And (Abs(e2\room\dist-e\room\dist)<HideDistance)
						e\EventState[0] = 2
						;debuglog "Failed to spawn SCP-096 in room "+e\room\RoomTemplate\Name$
						;debuglog "- room2_servers_1 event not activated + room is too close to room2_servers_1"
						Exit
					EndIf
				EndIf
			Next
			If PlayerRoom = e\room Then e\EventState[0] = 2
		EndIf
		
		;[End Block]
		
		If e\EventState[0] = 0
			Select e\room\RoomTemplate\Name
				Case "room4_pit","room3_pit","room3_hcz","room4_tunnel","room3_tunnel"
					If e\room\RoomTemplate\Name$ = "room4_pit" Lor e\room\RoomTemplate\Name$ = "room4_tunnel"
						place% = Rand(0,3)
					Else
						place% = Rand(0,2)
					EndIf
					
					If place% = 0
						xspawn# = -608.0
						zspawn# = 0.0
					ElseIf place% = 1
						xspawn# = 0.0
						zspawn# = -608.0
					ElseIf place% = 2
						xspawn# = 608.0
						zspawn# = 0.0
					Else
						xspawn# = 0.0
						zspawn# = 608.0
					EndIf
				Default
					xspawn# = Rnd(-100,100)
					zspawn# = Rnd(-100,100)
			End Select
			pvt% = CreatePivot(e\room\obj)
			PositionEntity pvt%,xspawn#,0,zspawn#
			If Curr096 = Null
				Curr096 = CreateNPC(NPC_SCP_096,EntityX(pvt%,True),e\room\y+0.5,EntityZ(pvt%,True))
				;debuglog EntityY(Curr096\Collider)
			Else
				PositionEntity Curr096\Collider,EntityX(pvt%,True),e\room\y+0.5,EntityZ(pvt%,True)
				ResetEntity Curr096\Collider
			EndIf
			PointEntity Curr096\Collider,Collider
			RotateEntity Curr096\Collider,0,EntityYaw(Curr096\Collider)+180,0
			pvt = FreeEntity_Strict(pvt)
			Curr096\State[0] = 5
			
			;debuglog "SCP-096 successfully placed in "+Chr(34)+e\room\RoomTemplate\Name+Chr(34)
			e\EventState[0] = 1
		ElseIf e\EventState[0] = 1
			PointEntity Curr096\Collider,Collider
			RotateEntity Curr096\Collider,0,EntityYaw(Curr096\Collider)+180,0
			
			If EntityDistanceSquared(Curr096\Collider,Collider)<PowTwo(HideDistance*0.5)
				If EntityVisible(Curr096\Collider,Camera)
					PointEntity Curr096\Collider,Collider
					RotateEntity Curr096\Collider,0,EntityYaw(Curr096\Collider)+Rnd(170,190),0
					e\EventState[0] = 2
				EndIf
			EndIf
		ElseIf e\EventState[0] = 3
			e\EventState[0] = 2
		EndIf
	Else
		If e\eventstate[0] = 2
			If Rand(-1,1+(2*SelectedDifficulty\aggressiveNPCs))>0 Then
				e\eventstate[0] = 0
			Else
				e\eventstate[0] = 3
			EndIf
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D