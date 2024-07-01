
Global SelectedLoadingScreen.LoadingScreens, LoadingScreenAmount%, LoadingScreenText%

InitLoadingScreens(I_Loc\LangPath + "Data\loadingscreens.ini")

Const LOADINGSCREEN_MAX_IMG% = 3
Const LOADINGSCREEN_MAX_TXT% = 5

Type LoadingScreens
	Field imgpath$[LOADINGSCREEN_MAX_IMG]
	Field img%[LOADINGSCREEN_MAX_IMG], imgamount%
	Field ID%
	Field title$
	Field txt$[LOADINGSCREEN_MAX_TXT], txtamount%
	Field rotImg%
	Field loadImg%
	Field loadimgtex%
	Field cam%
	Field percent%
	Field noAuto%
End Type

Global TextR# = 0.0, TextG# = 0.0, TextB# = 0.0
Global ChangeColor%

Function RenderLoadingText%(x%, y%, Txt$, AlignX% = False, AlignY% = False)
	If TextR = 0.0
		ChangeColor = True
	ElseIf TextR = 255.0
		ChangeColor = False
	EndIf
	
	If (Not ChangeColor)
		TextR = Max(0.0, TextR - 3.0)
		TextG = Max(0.0, TextG - 3.0)
		TextB = Max(0.0, TextB - 3.0)
	Else
		TextR = Min(TextR + 3.0, 255.0)
		TextG = Min(TextG + 3.0, 255.0)
		TextB = Min(TextB + 3.0, 255.0)
	EndIf
	
	SetFont fo\Font[Font_Default]
	Color(TextR, TextG, TextB)
	Text(x, y, Txt, AlignX, AlignY)
End Function

Function InitLoadingScreens(filename$)
	If I_Loc\Localized And FileType(I_Loc\LangPath + filename$)=1 Then
		filename = I_Loc\LangPath + filename
	EndIf
	Local TemporaryString$, i%
	Local ls.LoadingScreens
	Local file$ = filename
	
	Local f = OpenFile(file)
	
	While Not Eof(f)
		TemporaryString = Trim(ReadLine(f))
		If Left(TemporaryString,1) = "[" Then
			TemporaryString = Mid(TemporaryString, 2, Len(TemporaryString) - 2)
			
			ls.LoadingScreens = New LoadingScreens
			LoadingScreenAmount=LoadingScreenAmount+1
			ls\ID = LoadingScreenAmount
			
			ls\title = TemporaryString
			For i = 0 To (LOADINGSCREEN_MAX_IMG-1)
				ls\imgpath[i] = GetINIString(file, TemporaryString, "image"+(i+1))
				If ls\imgpath[i]<> "" Then ls\imgamount=ls\imgamount+1
			Next
			
			For i = 0 To (LOADINGSCREEN_MAX_TXT-1)
				ls\txt[i] = GetINIString(file, TemporaryString, "text"+(i+1))
				If ls\txt[i]<> "" Then ls\txtamount=ls\txtamount+1
			Next
			
			ls\noAuto = GetINIInt(file, TemporaryString, "disable auto selection")
		EndIf
	Wend
	
	CloseFile f
End Function

Function DrawLoading(percent%, shortloading=False, customloadingscreen$="", Assets$="Initializing")
	CatchErrors("Function DrawLoading (" + percent + ", " + shortloading + ", " + customloadingscreen + ")")
	Local ls.LoadingScreens
	Local x%, y%, width%, height%
	Local strtemp$, i%
	
	Local temp%, firstloop%, isSelected%
	
	If gopt\CurrZone = GATE_A_INTRO Then
		gopt\CurrZoneString = GetLocalString("Zones","gate_a_intro")
	ElseIf gopt\CurrZone = LCZ Then
		gopt\CurrZoneString = GetLocalString("Zones","lcz")
	ElseIf gopt\CurrZone = HCZ Then
		gopt\CurrZoneString = GetLocalString("Zones","hcz")
	ElseIf gopt\CurrZone = EZ Then
		gopt\CurrZoneString = GetLocalString("Zones","ez")
	ElseIf gopt\CurrZone = RCZ Then
		gopt\CurrZoneString = GetLocalString("Zones","rcz")
	ElseIf gopt\CurrZone = BCZ Then
		gopt\CurrZoneString = GetLocalString("Zones","bcz")
	ElseIf gopt\CurrZone = REACTOR_AREA Then
		gopt\CurrZoneString = GetLocalString("Zones","facility_reactor")
	ElseIf gopt\CurrZone = CLASSD_CELLS Then
		gopt\CurrZoneString = GetLocalString("Zones","class_d_cells")
	ElseIf gopt\CurrZone = GATE_A_ROAD Then
		gopt\CurrZoneString = GetLocalString("Zones","gate_a_road")
	ElseIf gopt\CurrZone = GATE_A_TOPSIDE Then
		gopt\CurrZoneString = GetLocalString("Zones","gate_a_topside")
	ElseIf gopt\CurrZone = GATE_B_TOPSIDE Then
		gopt\CurrZoneString = GetLocalString("Zones","gate_b_topside")
	ElseIf gopt\CurrZone = GATE_C_TOPSIDE Then
		gopt\CurrZoneString = GetLocalString("Zones","gate_c_topside")
	ElseIf gopt\CurrZone = GATE_D_TOPSIDE Then
		gopt\CurrZoneString = GetLocalString("Zones","gate_d_topside")
	ElseIf gopt\CurrZone = AREA_076 Then
		gopt\CurrZoneString = GetLocalString("Zones","area_076")
	ElseIf gopt\CurrZone = AREA_106_ESCAPE Then
		gopt\CurrZoneString = GetLocalString("Zones","area_106_escape")
	EndIf
	
	If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
		If IsStartingFromMenu Then
			customloadingscreen = GetLocalString("Loading", "loading_into_game")
		Else
			customloadingscreen = gopt\CurrZoneString
		EndIf
	EndIf
	
	If percent = 0 Then
		LoadingScreenText = 0
		If (Not shortloading) Then
			If customloadingscreen = "" Then
				isSelected = False
				While (Not isSelected)
					temp = Rand(1, LoadingScreenAmount)
					For ls = Each LoadingScreens
						If ls\ID = temp Then
							If (Not ls\noAuto) Then
								For i = 0 To (ls\imgamount-1)
									If ls\img[i] = 0 Then ls\img[i] = LoadImage_Strict("Loadingscreens\"+ls\imgpath[i])
									ls\img[i] = ResizeImage2(ls\img[i], ImageWidth(ls\img[i]) * MenuScale, ImageHeight(ls\img[i]) * MenuScale)
								Next
								SelectedLoadingScreen = ls
								isSelected = True
							EndIf
							Exit
						EndIf
					Next
				Wend
			Else
				For ls = Each LoadingScreens
					If ls\title = customloadingscreen Then
						For i = 0 To (ls\imgamount-1)
							If ls\img[i] = 0 Then ls\img[i] = LoadImage_Strict("Loadingscreens\"+ls\imgpath[i])
							ls\img[i] = ResizeImage2(ls\img[i], ImageWidth(ls\img[i]) * MenuScale, ImageHeight(ls\img[i]) * MenuScale)
						Next
						SelectedLoadingScreen = ls
						Exit
					Else
						SelectedLoadingScreen = First LoadingScreens
					EndIf
				Next
			EndIf
		Else
			SelectedLoadingScreen = First LoadingScreens
		EndIf
		
		If SelectedLoadingScreen = Null Then
			Return
		EndIf
		SelectedLoadingScreen\cam = CreateCamera()
		CameraRange SelectedLoadingScreen\cam,0.99,1.01
		SelectedLoadingScreen\rotImg = LoadSprite("GFX\menu\Loading_Icon.png",1+2,SelectedLoadingScreen\cam)
		If shortloading Then
			SelectedLoadingScreen\loadimgtex = LoadAnimTexture("GFX\menu\Menu_Loading_Icon.png",1+2,256,256,0,8)
			SelectedLoadingScreen\loadImg = CreateSprite(SelectedLoadingScreen\cam)
		EndIf
		ScaleSprite(SelectedLoadingScreen\rotImg, 0.05, 0.05)
		If shortloading Then
			ScaleSprite(SelectedLoadingScreen\loadImg, 0.1, 0.1)
		EndIf
		EntityOrder(SelectedLoadingScreen\rotImg,-100)
		If (Not shortloading) Then
			PositionEntity SelectedLoadingScreen\rotImg,0.0,-0.43-((0.5/(Float(opt\GraphicWidth)/Float(opt\GraphicHeight)/(16.0/9.0)))-0.5),1.0
		Else
			PositionEntity SelectedLoadingScreen\rotImg,0.0,0.0,1.0
			PositionEntity SelectedLoadingScreen\loadImg,0.0,0.0,1.0
		EndIf
		SpriteViewMode(SelectedLoadingScreen\rotImg,2)
	EndIf
	
	If SelectedLoadingScreen = Null Then
		Return
	EndIf
	
	If percent < SelectedLoadingScreen\percent Then
		percent = SelectedLoadingScreen\percent
	Else
		SelectedLoadingScreen\percent = percent
	EndIf
	
	firstloop = True
	Repeat
		If (Not shortloading) Then
			TurnEntity(SelectedLoadingScreen\rotImg, 0, 0, -5)
		Else
			TurnEntity(SelectedLoadingScreen\rotImg, 0, 0, -2)
		EndIf
		
		ClsColor 0,0,0
		Cls
		
		Rect 0,0,opt\GraphicWidth,opt\GraphicHeight
		
		CameraProjMode(SelectedLoadingScreen\cam,1)
		If Camera<>0 Then
			HideEntity Camera
		EndIf
		If m3d<>Null And m_I<>Null Then
			If m_I\Cam<>0 Then
				HideEntity m_I\Cam
			EndIf
			If m3d\Scene<>0 Then
				HideEntity m3d\Scene
			EndIf
		EndIf
		CameraViewport(SelectedLoadingScreen\cam,0,0,opt\GraphicWidth,opt\GraphicHeight)
		RenderWorld()
		CameraProjMode SelectedLoadingScreen\cam,0
		
		If (Not shortloading) Then
			
			If percent > (100.0 / SelectedLoadingScreen\txtamount)*(LoadingScreenText+1) Then
				LoadingScreenText=LoadingScreenText+1
			EndIf
			
			For i = 0 To (SelectedLoadingScreen\imgamount-1)
				width = ImageWidth(SelectedLoadingScreen\img[i])
				x = (opt\GraphicWidth / 2) - (width * 0.5) - (width * 0.625 * (SelectedLoadingScreen\imgamount-1-i)) + (width * 0.625 * i)
				DrawImage SelectedLoadingScreen\img[i], x, (opt\GraphicHeight / 2) - (ImageHeight(SelectedLoadingScreen\img[i]) / 2)
			Next
			
			width% = 300
			height% = 20
			x% = opt\GraphicWidth / 2 - width / 2
			y% = opt\GraphicHeight / 2 + 30 - 100
			
			Local width2% = Int((width - 2) * (100.0 / 330.0))*(opt\GraphicWidth/1280.0)
			Local height2% = (height/2-4)*(opt\GraphicHeight/720.0)
			
			If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then	
				SetFont fo\Font[Font_Menu]
				Text opt\GraphicWidth/2, opt\GraphicHeight-(height2)-(20*(opt\GraphicHeight/720.0))-900*MenuScale,GetLocalString("Menu","loading"),True
				
				SetFont fo\Font[Font_Default]
				Text opt\GraphicWidth/2, opt\GraphicHeight-(height2)-(20*(opt\GraphicHeight/720.0))-20*MenuScale,GetLocalString("Menu","menu_seed")+" "+RandomSeed,True
				If IsStartingFromMenu Then
					Text opt\GraphicWidth/2, opt\GraphicHeight-(height2)-(20*(opt\GraphicHeight/720.0))+20*MenuScale,GetLocalString("Menu","menu_init_zone"),True
				Else
					Text opt\GraphicWidth/2, opt\GraphicHeight-(height2)-(20*(opt\GraphicHeight/720.0))+20*MenuScale,GetLocalString("Menu","menu_zone")+" "+gopt\CurrZoneString,True
				EndIf
			Else
				Text opt\GraphicWidth/2, opt\GraphicHeight-(height2)-(20*(opt\GraphicHeight/720.0))-20*MenuScale,GetLocalString("Menu","mp_map")+" "+mp_I\MapInList\Name,True
			EndIf
			Text 0.0, opt\GraphicHeight-(height2)-(20*(opt\GraphicHeight/720.0))-20*MenuScale,GetLocalString("Menu","menu_loading")+" "+percent+"%"+"    "+GetLocalString("Menu","menu_assets")+" "+GetLocalString("Loading",Assets$)
			
			If gopt\GameMode = GAMEMODE_DEFAULT Then
				GamemodeName = GetLocalString("Menu","menu_gamemode_ryan")
				Color 255,255,255
			ElseIf gopt\GameMode = GAMEMODE_CLASS_D Then
				GamemodeName = GetLocalStringR("Menu","menu_gamemode_d", ClassDNumber)
				Color 255,106,0
			ElseIf gopt\GameMode = GAMEMODE_NTF Then
				GamemodeName = GetLocalString("Menu","menu_gamemode_ntf")
				Color 0,40,255
			ElseIf gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				GamemodeName = GetLocalString("Menu","menu_gamemode_mp")
				Color 255,255,255
			Else
				GamemodeName = "Error. Gamemode unknown."
				Color 255,0,0
			EndIf
			
			Text 0.0, opt\GraphicHeight-(height2)-(20*(opt\GraphicHeight/720.0))+20*MenuScale,GetLocalString("Menu","menu_gamemode")+" "+GamemodeName
			Color 255,255,255
			DrawFrame(0.0, opt\GraphicHeight-(height2)-(20*(opt\GraphicHeight/720.0))-4*MenuScale,(Int((width - 2) * (100 / 20)))*(opt\GraphicWidth/1280.0), height2+8*MenuScale)
			Rect(0.0, opt\GraphicHeight-(height2)-(20*(opt\GraphicHeight/720.0)), (Int((width - 2) * (percent / 20)))*(opt\GraphicWidth/1280.0), height2)
			
			If gopt\GameMode <> GAMEMODE_MULTIPLAYER And percent > 45 Then
				
;				Text opt\GraphicWidth/2, opt\GraphicHeight-(height2)-(20*(opt\GraphicHeight/720.0))-700*MenuScale,GetLocalString("Loading","generating_map"),True
;				
;				Local x2,z2
;				Local PlayerX% = Floor(EntityX(Collider) / 8.0 + 0.5)
;				Local PlayerZ% = Floor(EntityZ(Collider) / 8.0 + 0.5)
;				
;				x% = opt\GraphicWidth-(width2)-(20*(opt\GraphicHeight/720.0))-450*MenuScale
;				y% = opt\GraphicHeight-(height2)-(20*(opt\GraphicHeight/720.0))-600*MenuScale
;				
;				For z2 = 0 To MapGridSize - 1
;					For x2 = 0 To MapGridSize - 1
;						
;						If CurrGrid\Grid[x2 + (z2 * MapGridSize)] Then
;							Local drawx% = x + (PlayerX - x2) * 24 , drawy% = y - (PlayerZ - z2) * 24
;							
;							Color (255,255,255)
;							
;							If MapTemp[(x2 + 1) * MapWidth + z2] = False Then Rect(drawx - (12 * MenuScale), drawy - (12 * MenuScale), MenuScale, 24 * MenuScale)
;							If MapTemp[(x2 - 1) * MapWidth + z2] = False Then Rect(drawx + (12 * MenuScale), drawy - (12 * MenuScale), MenuScale, 24 * MenuScale)
;							
;							If MapTemp[x2 * MapWidth + z2 - 1] = False Then Rect(drawx - (12 * MenuScale), drawy - (12 * MenuScale), 24 * MenuScale, MenuScale)
;							If MapTemp[x2 * MapWidth + z2 + 1] = False Then Rect(drawx - (12 * MenuScale), drawy + (12 * MenuScale), 24 * MenuScale, MenuScale)
;						EndIf
;						
;					Next
;				Next
				
			EndIf
			
			SetFont fo\Font[Font_Menu]
			Color 255,255,255
			Text(opt\GraphicWidth / 2, opt\GraphicHeight / 6, SelectedLoadingScreen\title, True, True)
			
			SetFont fo\Font[Font_Default]
			If SelectedLoadingScreen\imgamount > 1 Then
				For i = 0 To Min(SelectedLoadingScreen\imgamount, SelectedLoadingScreen\txtamount)
					For i = 0 To (SelectedLoadingScreen\imgamount-1)
						width = ImageWidth(SelectedLoadingScreen\img[i])
						x = (opt\GraphicWidth / 2) - (width * 0.5) - (width * 0.625 * (SelectedLoadingScreen\imgamount-1-i)) + (width * 0.625 * i)
						y = (opt\GraphicHeight / 2) + (ImageHeight(SelectedLoadingScreen\img[i]) / 2) + 20
						RowText(SelectedLoadingScreen\txt[i], x, y, width, 300, True)
					Next
				Next
			Else
				RowText(SelectedLoadingScreen\txt[LoadingScreenText], opt\GraphicWidth / 2-(opt\GraphicWidth/6), opt\GraphicHeight / 1.25,opt\GraphicWidth/3,300,True)
			EndIf
			
		Else
			
			If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
				If (MilliSecs() Mod 1000) > 300 Then
					Color 255,255,255
					Text opt\GraphicWidth/2, opt\GraphicHeight-(height2)-(20*(opt\GraphicHeight/720.0))-700*MenuScale,GetLocalString("Menu","please_wait"),True
				EndIf
				Text opt\GraphicWidth/2, opt\GraphicHeight-(height2)-(20*(opt\GraphicHeight/720.0))-900*MenuScale,GetLocalString("Menu","loading"),True
				Text opt\GraphicWidth/2, opt\GraphicHeight-(height2)-(20*(opt\GraphicHeight/720.0))-150*MenuScale,GetLocalString("Menu","menu_assets")+" "+GetLocalString("Loading",Assets$),True
			EndIf
				
			Local Imgframe
			
			If percent =< 10 Then
				Imgframe = 0
			ElseIf percent > 10 And percent < 20 Then
				Imgframe = 1
			ElseIf percent > 20 And percent < 30 Then
				Imgframe = 2
			ElseIf percent > 30 And percent < 40 Then
				Imgframe = 3
			ElseIf percent > 40 And percent < 50 Then
				Imgframe = 4
			ElseIf percent > 50 And percent < 60 Then
				Imgframe = 5
			ElseIf percent > 60 And percent < 70 Then
				Imgframe = 6
			ElseIf percent > 80 Then
				Imgframe = 7
			EndIf
			EntityTexture SelectedLoadingScreen\loadImg,SelectedLoadingScreen\loadimgtex,Imgframe
		EndIf
		FlushKeys()
		FlushMouse()
		FlushJoy()
		
		GammaUpdate()
		
		Flip True
		
		Local close% = False
		firstloop = False
		If percent <> 100 Then
			Exit
		Else
			FlushKeys()
			FlushMouse()
			FlushJoy()
			ResetTimingAccumulator()
			SetFont fo\Font[Font_Default]
			close = True
			If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
				Players[mp_I\PlayerID]\FinishedLoading = True
			EndIf
		EndIf
	Until close
	
	DeleteMenuGadgets()
	
	If close Then
		SelectedLoadingScreen\rotImg = FreeEntity_Strict(SelectedLoadingScreen\rotImg)
		SelectedLoadingScreen\cam = FreeEntity_Strict(SelectedLoadingScreen\cam)
		
		If Camera<>0 Then
			ShowEntity Camera
		EndIf
		If m3d<>Null And m_I<>Null Then
			If m_I\Cam<>0 Then
				ShowEntity m_I\Cam
			EndIf
			If m3d\Scene<>0 Then
				ShowEntity m3d\Scene
			EndIf
		EndIf
		SelectedLoadingScreen\percent=0
	EndIf
	
	CatchErrors("Uncaught: Function DrawLoading")
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D