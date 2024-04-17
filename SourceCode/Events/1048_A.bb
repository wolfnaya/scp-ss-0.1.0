
Function UpdateEvent_1048_A(e.Events)
	
	If PlayerRoom<>e\room And BlinkTimer<-10 Then
		If (DistanceSquared(EntityX(Collider),EntityX(e\room\obj),EntityZ(Collider),EntityZ(e\room\obj))<PowTwo(16.0)) Then
			CreateNPC(NPC_scp_1048, EntityX(e\room\obj), 0.2, EntityZ(e\room\obj))
			RemoveEvent(e)
		EndIf
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D