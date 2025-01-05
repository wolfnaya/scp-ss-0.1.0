Const MAXACHIEVEMENTS% = 51

Type Achievements
	Field Achievement%[MAXACHIEVEMENTS]
	Field AchievementStrings$[MAXACHIEVEMENTS]
	Field AchievementDescs$[MAXACHIEVEMENTS]
	Field AchvIMG%[MAXACHIEVEMENTS]
	Field AchvLocked%
End Type

Global UsedConsole%
Global AchievementsMenu%

; ~ [SCP-CONTAINMENT BREACH]
Const Achv008%=0, Achv035%=1, Achv049%=2, Achv055=3,  Achv079%=4, Achv096%=5, Achv106%=6, Achv148%=7
Const Achv294%=8, Achv372%=9, Achv420%=10, Achv427=11, Achv500%=12, Achv714%=13, Achv789%=14, Achv860%=15
Const Achv914%=16, Achv966%=17
Const AchvMaynard%=18, AchvHarp%=19, AchvSNAV%=20, AchvOmni%=21, AchvConsole%=22, AchvTesla%=23, AchvPD%=24
Const Achv1162% = 25
Const AchvKeter% = 26
; ~ [SCP - SECURITY STORIES]
Const AchvLWS% = 27,AchvHWS% = 28,AchvWS% = 29,AchvEWS% = 30,AchvSWS% = 31,AchvWeapons% = 32
Const Achv268% = 33,Achv1033RU% = 34,Achv198% = 35,Achv207% = 36,Achv109% = 37,Achv016% = 38
Const AchvVane% = 39,Achv076% = 40,AchvGungame% = 41,AchvEMRP% = 42,AchvHDS% = 43,Achv409% = 44,Achv127% = 45,AchvWolfnaya% = 46,AchvThaumiel% = 47,AchvAppolyon% = 48
Const Achv173Cont% = 49, Achv457% = 50

For i = 0 To MAXACHIEVEMENTS-1
	Local Loc2% = GetINISectionLocation(AchvIni, "s"+Str(i))
	achv\AchievementStrings[i] = GetINIString2(AchvIni, Loc2, "AchvName")
	achv\AchievementDescs[i] = GetINIString2(AchvIni, Loc2, "AchvDesc")
	
	Local Image$ = GetINIString2(AchvIni, Loc2, "AchvImage") 
	
	achv\AchvIMG[i] = LoadImage_Strict("GFX\achievements\"+Image+".png")
	achv\AchvIMG[i] = ResizeImage2(achv\AchvIMG[i],ImageWidth(achv\AchvIMG[i])*opt\GraphicHeight/768.0,ImageHeight(achv\AchvIMG[i])*opt\GraphicHeight/768.0)
Next
achv\AchvLocked = LoadImage_Strict("GFX\achievements\achvlocked.png")
achv\AchvLocked = ResizeImage2(achv\AchvLocked,ImageWidth(achv\AchvLocked)*opt\GraphicHeight/768.0,ImageHeight(achv\AchvLocked)*opt\GraphicHeight/768.0)

Function GiveAchievement%(AchvName%, ShowMessage% = True)
	If achv\Achievement[AchvName] <> True Then
		achv\Achievement[AchvName] = True
		Local Loc2% = GetINISectionLocation(AchvIni, "s" + AchvName)
		Local AchievementName$ = GetINIString2(AchvIni, Loc2, "AchvName")
		
		CreateAchievementMsg(AchvName, AchievementName)
	EndIf
End Function

Function AchievementTooltip%(AchvNo%)
	Local Scale# = opt\GraphicHeight / 768.0
	
	SetFont fo\Font%[Font_Digital_Medium]
	
	Local Width% = StringWidth(achv\AchievementStrings[AchvNo])
	
	SetFont(fo\Font[Font_Default])
	If StringWidth(achv\AchievementDescs[AchvNo]) > Width Then
		Width = StringWidth(achv\AchievementDescs[AchvNo])
	EndIf
	Width = Width + (20 * MenuScale)
	
	Local Height% = 38 * Scale
	
	Color(25, 25, 25)
	Rect(ScaledMouseX() + (20 * MenuScale), ScaledMouseY() + (20 * MenuScale), Width, Height, True)
	Color(150, 150, 150)
	Rect(ScaledMouseX() + (20 * MenuScale), ScaledMouseY() + (20 * MenuScale), Width, Height, False)
	SetFont fo\Font%[Font_Digital_Medium]
	Text(ScaledMouseX() + (20 * MenuScale) + (Width / 2), ScaledMouseY() + (35 * MenuScale), achv\AchievementStrings[AchvNo], True, True)
	SetFont(fo\Font[Font_Default])
	Text(ScaledMouseX() + (20 * MenuScale) + (Width / 2), ScaledMouseY() + (55 * MenuScale), achv\AchievementDescs[AchvNo], True, True)
End Function

Function DrawAchvIMG%(x%, y%, AchvNo%)
	Local Row%
	Local Scale# = opt\GraphicHeight / 768.0
	Local SeparationConst2# = 76.0 * Scale
	
	Row = (AchvNo Mod 4)
	Color(0, 0, 0)
	Rect((x + ((Row) * SeparationConst2)), y, 64 * Scale, 64 * Scale, True)
	If achv\Achievement[AchvNo] = True Then
		DrawImage(achv\AchvIMG[AchvNo], (x + (Row * SeparationConst2)), y)
	Else
		DrawImage(achv\AchvLocked, (x + (Row * SeparationConst2)), y)
	EndIf
	Color(50, 50, 50)
	
	Rect((x + (Row * SeparationConst2)), y, 64 * Scale, 64 * Scale, False)
End Function

Global CurrAchvMSGID% = 0

Type AchievementMsg
	Field AchvID%
	Field Txt$
	Field MsgX#
	Field MsgTime#
	Field MsgID%
End Type

Function CreateAchievementMsg.AchievementMsg(ID%, Txt$)
	Local amsg.AchievementMsg
	
	amsg.AchievementMsg = New AchievementMsg
	amsg\AchvID = ID
	amsg\Txt = Txt
	amsg\MsgX = 0.0
	amsg\MsgTime = FPSfactor2
	amsg\MsgID = CurrAchvMSGID
	CurrAchvMSGID = CurrAchvMSGID + 1
	
	Return(amsg)
End Function

Function UpdateAchievementMsg%()
	Local amsg.AchievementMsg, amsg2.AchievementMsg
	Local Scale# = opt\GraphicHeight / 768.0
	Local Width% = 264.0 * Scale
	Local Height% = 84.0 * Scale
	Local x%, y%
	
	For amsg.AchievementMsg = Each AchievementMsg
		If amsg\MsgTime <> 0.0 Then
			If amsg\MsgTime > 0.0 And amsg\MsgTime < 70.0 * 7.0 Then
				amsg\MsgTime = amsg\MsgTime + FPSfactor2
				If amsg\MsgX > -Width Then amsg\MsgX = Max(amsg\MsgX - (4.0 * FPSfactor2), -Width)
			ElseIf amsg\MsgTime >= 70.0 * 7.0
				amsg\MsgTime = -1.0
			ElseIf amsg\MsgTime = -1.0
				If amsg\MsgX < 0.0 Then
					amsg\MsgX = Min(amsg\MsgX + (4.0 * FPSfactor2), 0.0)
				Else
					amsg\MsgTime = 0.0
				EndIf
			EndIf
		Else
			Delete(amsg)
		EndIf
	Next
End Function

Function RenderAchievementMsg%()
	Local amsg.AchievementMsg, amsg2.AchievementMsg
	Local Scale# = opt\GraphicHeight / 768.0
	Local Width% = 264.0 * Scale
	Local Height% = 84.0 * Scale
	Local x%, y%
	
	For amsg.AchievementMsg = Each AchievementMsg
		If amsg\MsgTime <> 0.0 Then
			x = opt\GraphicWidth + amsg\MsgX
			y = 0
			For amsg2.AchievementMsg = Each AchievementMsg
				If amsg2 <> amsg Then
					If amsg2\MsgID > amsg\MsgID Then y = y + Height 
				EndIf
			Next
			DrawFrame(x, y, Width, Height)
			Color(0, 0, 0)
			Rect(x + (10.0 * Scale), y + (10.0 * Scale), 64.0 * Scale, 64.0 * Scale)
			DrawImage(achv\AchvIMG[amsg\AchvID], x + (10 * Scale), y + 10 * Scale)
			Color(50, 50, 50)
			Rect(x + (10.0 * Scale), y + (10.0 * Scale), 64.0 * Scale, 64.0 * Scale, False)
			Color(255, 255, 255)
			SetFont(fo\Font[Font_Default])
			RowText("Achievement Unlocked - " + amsg\Txt, x + (84.0 * Scale), y + (10.0 * Scale), Width - (94.0 * Scale), y - (20.0 * Scale))
		EndIf
	Next
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D TSS