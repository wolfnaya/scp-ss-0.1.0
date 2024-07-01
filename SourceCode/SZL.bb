; ~ Nah, it would take me too long to port that stuff here, I don't have time for that - Wolfnaya.
;Function ChangeZone%(Zone%)
;	
;	ShouldPlay = MUS_NULL
;	
;	SaveGame(SavePath + CurrSave\Name + "\", True)
;	gopt\CurrZone = Zone
;	
;	ResetControllerSelections()
;	DropSpeed = 0
;	
;	NullGame(True, False)
;	LoadEntities()
;	LoadAllSounds()
;	
;	If FileType(SavePath + CurrSave\Name + "\" + Zone + ".sav") = 1 Then
;		LoadGame(SavePath + CurrSave\Name + "\", Zone)
;		InitLoadGame()
;	Else
;		InitNewGame()
;		LoadDataForZones(SavePath + CurrSave\Name + "\")
;	EndIf
;	gopt\CurrZone = Zone
;	
;	MainMenuOpen = False
;	FlushKeys()
;	FlushMouse()
;	FlushJoy()
;	ResetInput()
;	
;	SaveGame(SavePath + CurrSave\Name + "\", True)
;	Return
;End Function
;~IDEal Editor Parameters:
;~C#Blitz3D