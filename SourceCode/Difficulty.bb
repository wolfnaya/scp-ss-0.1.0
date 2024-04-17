Type Difficulty
	Field Name$
	Field Description$
	Field PermaDeath%
	Field AggressiveNPCs
	Field SaveType%
	Field OtherFactors%
	
	Field r%
	Field g%
	Field b%
	
	Field Customizable%
	Field Locked%
End Type

Global difficulties.Difficulty[6]

Global SelectedDifficulty.Difficulty

Const SAFE=0, EUCLID=1, KETER=2, THAUMIEL=3, APPOLYON=4, ESOTERIC=5

Const SAVEANYWHERE = 0, SAVEONQUIT=1, SAVEONSCREENS=2

Const EASY = 0, NORMAL = 1, HARD = 2, HARDER = 3, IMPOSSIBLE = 4

difficulties[SAFE] = New Difficulty
difficulties[SAFE]\Name = GetLocalString("Difficulty", "safe")
difficulties[SAFE]\Description = GetLocalString("Difficulty", "safe_desc")
difficulties[SAFE]\PermaDeath = False
difficulties[SAFE]\AggressiveNPCs = False
difficulties[SAFE]\SaveType = SAVEANYWHERE
difficulties[SAFE]\OtherFactors = EASY
difficulties[SAFE]\r = 120
difficulties[SAFE]\g = 150
difficulties[SAFE]\b = 50

difficulties[EUCLID] = New Difficulty
difficulties[EUCLID]\Name = GetLocalString("Difficulty", "euclid")
difficulties[EUCLID]\Description = GetLocalString("Difficulty", "euclid_desc")
difficulties[EUCLID]\PermaDeath = False
difficulties[EUCLID]\AggressiveNPCs = False
difficulties[EUCLID]\SaveType = SAVEONSCREENS
difficulties[EUCLID]\OtherFactors = NORMAL
difficulties[EUCLID]\r = 200
difficulties[EUCLID]\g = 200
difficulties[EUCLID]\b = 0

difficulties[KETER] = New Difficulty
difficulties[KETER]\Name = GetLocalString("Difficulty", "keter")
difficulties[KETER]\Description = GetLocalString("Difficulty", "keter_desc")
difficulties[KETER]\PermaDeath = True
difficulties[KETER]\AggressiveNPCs = True
difficulties[KETER]\SaveType = SAVEONQUIT
difficulties[KETER]\OtherFactors = HARD
difficulties[KETER]\r = 200
difficulties[KETER]\g = 0
difficulties[KETER]\b = 0

difficulties[THAUMIEL] = New Difficulty
difficulties[THAUMIEL]\Name = GetLocalString("Difficulty", "thaumiel")
difficulties[THAUMIEL]\Description = GetLocalString("Difficulty", "thaumiel_desc")
difficulties[THAUMIEL]\PermaDeath = True
difficulties[THAUMIEL]\AggressiveNPCs = True
difficulties[THAUMIEL]\SaveType = SAVEONQUIT
difficulties[THAUMIEL]\OtherFactors = HARDER
difficulties[THAUMIEL]\r = 150
difficulties[THAUMIEL]\g = 150
difficulties[THAUMIEL]\b = 150

difficulties[APPOLYON] = New Difficulty
difficulties[APPOLYON]\Name = GetLocalString("Difficulty", "appolyon")
difficulties[APPOLYON]\Description = GetLocalString("Difficulty", "appolyon_desc")
difficulties[APPOLYON]\PermaDeath = True
difficulties[APPOLYON]\AggressiveNPCs = True
difficulties[APPOLYON]\SaveType = SAVEONQUIT
difficulties[APPOLYON]\OtherFactors = IMPOSSIBLE
difficulties[APPOLYON]\r = 50
difficulties[APPOLYON]\g = 150
difficulties[APPOLYON]\b = 255

difficulties[ESOTERIC] = New Difficulty
difficulties[ESOTERIC]\Name = GetLocalString("Difficulty", "esoteric")
difficulties[ESOTERIC]\PermaDeath = False
difficulties[ESOTERIC]\AggressiveNPCs = True
difficulties[ESOTERIC]\SaveType = SAVEANYWHERE
difficulties[ESOTERIC]\Customizable = True
difficulties[ESOTERIC]\OtherFactors = EASY
difficulties[ESOTERIC]\r = 120
difficulties[ESOTERIC]\g = 50
difficulties[ESOTERIC]\b = 150

SelectedDifficulty = difficulties[SAFE]
;~IDEal Editor Parameters:
;~C#Blitz3D