Const MAXACHIEVEMENTS% = 57

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
Const Achv294%=8, Achv372%=9, Achv420%=10, Achv427=11, Achv500%=12, Achv513%=13, Achv714%=14, Achv789%=15, Achv860%=16
Const Achv914%=17, Achv966%=18, Achv1025%=19, Achv1048=20
Const AchvMaynard%=21, AchvHarp%=22, AchvSNAV%=23, AchvOmni%=24, AchvConsole%=25, AchvTesla%=26, AchvPD%=27
Const Achv1162% = 28
Const AchvKeter% = 29
; ~ [SCP - SECURITY STORIES]
Const AchvLWS% = 30,AchvHWS% = 31,AchvWS% = 32,AchvEWS% = 33,AchvSWS% = 34,AchvWeapons% = 35
Const Achv268% = 36,Achv1033RU% = 37,Achv198% = 38,Achv207% = 39,Achv1079% = 40,Achv109% = 41,Achv059% = 42,Achv016% = 43,Achv357% = 44
Const Achv402% = 45,AchvVane% = 46,Achv076% = 47,AchvGungame% = 48,AchvEMRP% = 49,AchvHDS% = 50,Achv409% = 51,Achv127% = 52,AchvWolfnaya% = 53,AchvThaumiel% = 54,AchvAppolyon% = 55
Const Achv1102RU% = 56

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
;~C#Blitz3D