
Function UpdateEvent_ClassD_Spawn(e.Events)
	Local n.NPCs
	
	If e\room\dist < HideDistance*1.5 And e\room\dist > 2 Then
		If e\EventState[0] = 0.0
			If Rand(0,1)=0
				n.NPCs = CreateNPC(NPC_Class_D,e\room\x,e\room\y+0.5,e\room\z)
			Else
				n.NPCs = CreateNPC(NPC_Class_D,e\room\x,e\room\y+0.5,e\room\z)
				n.NPCs = CreateNPC(NPC_Class_D,e\room\x+Rnd(1.0,1.5),e\room\y+0.5,e\room\z)
			EndIf
			e\EventState[0] = 1.0
		Else
			RemoveEvent(e)
		EndIf
	EndIf
	
End Function

Function UpdateEvent_ClassD_Spawn_Group(e.Events)
	Local i
	
	If e\room\dist < HideDistance*1.5
		If e\EventState[0] = 0
			If e\room\dist > 2
				e\EventState[1] = Rand(2,3+(1*SelectedDifficulty\OtherFactors))
				For i = 0 To e\EventState[1]
					e\room\NPC[i] = CreateNPC(NPC_Class_D,e\room\x+Rnd(-1.0,1.0),e\room\y+0.5,e\room\z+Rnd(-1.0,1.0))
				Next
				e\EventState[0] = 1
			EndIf
		ElseIf e\EventState[0] = 1
			Local suspense% = False
			For i = 0 To e\EventState[1]
				If e\room\NPC[i]\State[0] = 1
					suspense = True
					Exit
				EndIf
			Next
			If suspense
				CurrOverhaul = 2
			EndIf
			Local isDead% = True
			For i = 0 To e\EventState[1]
				If (Not e\room\NPC[i]\IsDead)
					isDead = False
					Exit
				EndIf
			Next
			If isDead Then e\EventState[0] = 2
		Else
			RemoveEvent(e)
		EndIf
	EndIf
	
End Function

Function UpdateEvent_Zombie_Spawn(e.Events)
	Local n.NPCs
	
	If e\room\dist < HideDistance*1.5 And e\room\dist > 2 Then
		If e\EventState[0] = 0.0
			If Rand(4)=0
				n.NPCs = CreateNPC(NPC_Zombie,e\room\x,e\room\y+0.5,e\room\z,Topless_Zombie) ; ~ 008
				n\State[0] = 2
			ElseIf Rand(4)=1
				n.NPCs = CreateNPC(NPC_Zombie_Armed,e\room\x,e\room\y+0.5,e\room\z,Guard_Zombie) ; ~ Guard
				n\State[0] = Z_STATE_WANDER
				If gopt\CurrZone = BCZ Then
					n.NPCs = CreateNPC(NPC_Zombie_Armed,e\room\x+Rnd(1.0,1.5),e\room\y+0.5,e\room\z,NTF_Zombie) ; ~ NTF
					n\State[0] = Z_STATE_WANDER
				EndIf
			ElseIf Rand(4)=2
				n.NPCs = CreateNPC(NPC_Zombie,e\room\x,e\room\y+0.5,e\room\z,ClassD_Zombie) ; ~ Class-D
				n\State[0] = 2
				n.NPCs = CreateNPC(NPC_Zombie,e\room\x+Rnd(1.0,1.5),e\room\y+0.5,e\room\z,Clerk_Zombie) ; ~ Clerk
				n\State[0] = 2
			ElseIf Rand(4)=3
				If gopt\CurrZone = HCZ Lor gopt\CurrZone = BCZ Then
					n.NPCs = CreateNPC(NPC_Zombie_Armed,e\room\x,e\room\y+0.5,e\room\z,CI_Zombie) ; ~ CI
					n\State[0] = 2
				EndIf
				n.NPCs = CreateNPC(NPC_Zombie,e\room\x+Rnd(1.0,1.5),e\room\y+0.5,e\room\z,Hazmat_Zombie) ; ~ 008
				n\State[0] = 2
			Else
				n.NPCs = CreateNPC(NPC_Zombie,e\room\x,e\room\y+0.5,e\room\z,Worker_Zombie); ~ Worker
				n\State[0] = 2
				n.NPCs = CreateNPC(NPC_Zombie,e\room\x+Rnd(1.0,1.5),e\room\y+0.5,e\room\z,Surgeon_Zombie) ; ~ Surgeon
				n\State[0] = 2
			EndIf
			e\EventState[0] = 1.0
		Else
			RemoveEvent(e)
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D