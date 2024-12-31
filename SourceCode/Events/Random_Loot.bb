
Function UpdateEvent_Random_Loot(e.Events, LootTree$)
;	Local LootData$, LootAmount% = GetINIInt("Data\RandomLoot.ini", LootTree, "Amount")
;	Local n%, s$, it.Items
;	Local x%, y%, z%
;	
;	If e\EventState[0] = 0 Then
;		For n = 0 To LootAmount - 1
;			LootData$ = GetINIString("Data\RandomLoot.ini", LootTree, "Item"+(n+1))
;			CreateItem(Piece(GetLocalString("Item Names", LootData),1,"|"), Piece(LootData,2,"|"), e\room\RandomLootX[n], e\room\RandomLootY[n], e\room\RandomLootZ[n], 1, 1, 1)
;		Next
;		e\EventState[0] = 1
;		RemoveEvent(e)
;	EndIf
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D TSS