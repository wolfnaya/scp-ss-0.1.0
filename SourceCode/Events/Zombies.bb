
Function UpdateEvent_Zombie_Spawn(e.Events)
	Local n.NPCs
	
	If e\room\dist < HideDistance*1.5 And e\room\dist > 2 Then
		If e\EventState[0] = 0.0
			If Rand(4)=0
				n.NPCs = CreateNPC(NPCtype008_2,e\room\x,e\room\y+0.5,e\room\z)
				n\State[0] = 2
			ElseIf Rand(4)=1
				n.NPCs = CreateNPC(NPCtype049_2,e\room\x,e\room\y+0.5,e\room\z)
				n\State[0] = Z_STATE_WANDER
				If NTF_CurrZone = BCZ Then
					n.NPCs = CreateNPC(NPCType049_6,e\room\x+Rnd(1.0,1.5),e\room\y+0.5,e\room\z)
					n\State[0] = Z_STATE_WANDER
				EndIf
			ElseIf Rand(4)=2
				n.NPCs = CreateNPC(NPCtype049_3,e\room\x,e\room\y+0.5,e\room\z)
				n\State[0] = 2
				n.NPCs = CreateNPC(NPCtype049_4,e\room\x+Rnd(1.0,1.5),e\room\y+0.5,e\room\z)
				n\State[0] = 2
			ElseIf Rand(4)=3
				If NTF_CurrZone = HCZ Lor NTF_CurrZone = BCZ Then
					n.NPCs = CreateNPC(NPCtype008_4,e\room\x,e\room\y+0.5,e\room\z); TODO 008_5!!
					n\State[0] = 2
				EndIf
				n.NPCs = CreateNPC(NPCtype008,e\room\x+Rnd(1.0,1.5),e\room\y+0.5,e\room\z)
				n\State[0] = 2
			Else
				n.NPCs = CreateNPC(NPCtype049_5,e\room\x,e\room\y+0.5,e\room\z)
				n\State[0] = 2
				n.NPCs = CreateNPC(NPCtype008_3,e\room\x+Rnd(1.0,1.5),e\room\y+0.5,e\room\z)
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