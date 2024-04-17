
Function FillRoom_Gate_A_Road(r.Rooms)
	Local wa.Water,it.Items
	
	wa.Water = CreateWater("GFX\map\rooms\gate_a_road\gate_a_road_river.b3d","road_river",0,0,0,r\obj,(0.0*RoomScale))
	EntityAlpha wa\obj,0.8
	EntityColor wa\obj,100,100,100
	
	r\Objects[0] = LoadTexture_Strict("GFX\map\textures\SLH_water4.png",1,1)
	EntityTexture wa\obj,r\Objects[0]
	ScaleTexture r\Objects[0],0.1,0.1
	TextureBlend r\Objects[0],2
		CreateDarkSprite(r, 1)
	
	r\Objects[2] = CreatePivot()
	PositionEntity r\Objects[2],r\x+400*RoomScale,0.5,r\z-1643*RoomScale,True
	EntityParent r\Objects[2],r\obj
	
	r\Objects[3] = CreatePivot()
	PositionEntity r\Objects[3],r\x-670*RoomScale,0.5,r\z+1417*RoomScale,True
	EntityParent r\Objects[3],r\obj
	
	r\Objects[4] = CreatePivot()
	PositionEntity r\Objects[4],r\x-361*RoomScale,0.5,r\z+1417*RoomScale,True
	EntityParent r\Objects[4],r\obj
	
	r\Objects[5] = CreatePivot()
	PositionEntity r\Objects[5],r\x-2*RoomScale,0.5,r\z+1417*RoomScale,True
	EntityParent r\Objects[5],r\obj
	
	r\Objects[6] = CreatePivot()
	PositionEntity r\Objects[6],r\x+440*RoomScale,0.5,r\z+1411*RoomScale,True
	EntityParent r\Objects[6],r\obj
	
;	it = CreateItem("Colt M4A1","m4a1", r\x-30.0*RoomScale,r\y, r\z-2230.0*RoomScale)
;	it\state = 30 : it\state2 = 120
;	EntityParent(it\collider, r\obj)
;	it = CreateItem("H&K MP5", "mp5", r\x-30.0*RoomScale, r\y, r\z-2230.0*RoomScale)
;	it\state = 30 : it\state2 = 90
;	EntityParent(it\collider, r\obj)
;	it = CreateItem("FN P90","p90", r\x-30.0*RoomScale, r\y, r\z-2230.0*RoomScale)
;	it\state = 50 : it\state2 = 150
;	EntityParent(it\collider, r\obj)
;	it = CreateItem("Franchi SPAS-12","spas12", r\x+255.0*RoomScale, r\y, r\z-26.0*RoomScale)
;	it\state = 6 : it\state2 = 56
;	EntityParent(it\collider, r\obj)
	
End Function

Function UpdateEvent_Gate_A_Road(e.Events)
	Local r.Rooms,n.NPCs,de.Decals,p.Particles
	Local xtemp#,ztemp#,temp%,angle#,dist#,pvt%
	Local i,it.Items,g.Guns,b
	
	If PlayerRoom = e\room Then
		
		CanSave = False
		
		If e\EventState[2] = 0 Then
			If opt\MusicVol > 0 Then
				e\Sound[0] = LoadSound_Strict("SFX\Music\Chase_Music\Target_Number_1.ogg")
				If ChannelPlaying(e\SoundCHN[0]) = False Then e\SoundCHN[0] = PlaySound_Strict(e\Sound[0])
			EndIf
		EndIf
		ShouldPlay = MUS_NULL
		
		If e\EventState[0] = 0 Then
			
			psp\Health = 100
				;psp\Kevlar = 100
				
			;P90
;			it = CreateItem("FN P90", "p90", 1, 1, 1)
;			it\Picked = True
;			it\Dropped = -1
;			it\itemtemplate\found=True
;			Inventory[0] = it
;			HideEntity(it\collider)
;			EntityType (it\collider, HIT_ITEM)
;			EntityParent(it\collider, 0)
;			ItemAmount = ItemAmount + 1
;				;USP
;			it = CreateItem("RPK-16", "rpk16", 1, 1, 1)
;			it\Picked = True
;			it\Dropped = -1
;			it\itemtemplate\found=True
;			Inventory[1] = it
;			HideEntity(it\collider)
;			EntityType (it\collider, HIT_ITEM)
;			EntityParent(it\collider, 0)
;			ItemAmount = ItemAmount + 1
;				;Knife
;			it = CreateItem("Colt M4A1", "m4a1", 1, 1, 1)
;			it\Picked = True
;			it\Dropped = -1
;			it\itemtemplate\found=True
;			Inventory[2] = it
;			HideEntity(it\collider)
;			EntityType (it\collider, HIT_ITEM)
;			EntityParent(it\collider, 0)
;			ItemAmount = ItemAmount + 1
;			
;			g_I\Weapon_InSlot[GunSlot1] = "p90"
;			g_I\Weapon_InSlot[GunSlot2] = "rpk16"
;			g_I\Weapon_InSlot[GunSlot3] = "m4a1"
;			g_I\Weapon_CurrSlot% = GunSlot1+1
;			g_I\HoldingGun = 0
;			For g = Each Guns
;				Select g\ID
;					Case GUN_P90
;						g\CurrReloadAmmo = 150
;					Case GUN_RPK16
;						g\CurrReloadAmmo = 120
;					Case GUN_M4A1
;						g\CurrReloadAmmo = 120
;				End Select
;			Next
				
			DrawLoading(0, False , "")
			
			DrawLoading(30, False, "", "SKY")
			
			For i = 0 To e\room\MaxLights
				If e\room\LightSprites[i]<>0 Then 
					EntityFX e\room\LightSprites[i], 1+8
				EndIf
			Next
			For n.NPCs = Each NPCs
				If n <> Curr106 And n <> Curr173
					RemoveNPC(n)
				EndIf
			Next
			Curr173\Idle = True
			CameraFogMode(Camera, 0)
			SecondaryLightOn = True
			HideDistance = 35.0
			TranslateEntity(e\room\obj, 0,12000.0*RoomScale,0)
			TranslateEntity(Collider, 0,12000.0*RoomScale,0)
			
			Sky = sky_CreateSky("GFX\map\sky\sky_day")
			RotateEntity Sky,0,e\room\angle,0
			
			DrawLoading(60, False, "", "PLAYER")
			
			ResetEntity Collider
			
			RotateEntity Camera,0,180,0
			RotateEntity Collider,0,180,0
			
			e\EventState[0] = 1
			
			DrawLoading(100, False, "", "LOADING_DONE")
			CreateSplashText(GetLocalString("Singleplayer","ci_status"),20,opt\GraphicHeight-200,250,1,Font_Default_Large,False,False)
		Else
			
			If Sky = 0 Then
				Sky = sky_CreateSky("GFX\map\sky\sky_day")
				RotateEntity Sky,0,e\room\angle,0
			EndIf
			
			e\EventState[0] = e\EventState[0] + FPSfactor
			HideEntity Fog
			CameraFogRange Camera, 5,80
			
			angle = Max(Sin(EntityYaw(Collider)+90),0.0)
			
			If e\EventState[2] = 0 Then
				Local FogColorR1% = Left(FogColor_Outside,3)
				Local FogColorG1% = Mid(FogColor_Outside,4,3)
				Local FogColorB1% = Right(FogColor_Outside,3)
				CameraFogColor Camera,FogColorR1,FogColorG1,FogColorB1
				CameraClsColor Camera,FogColorR1,FogColorG1,FogColorB1
			Else
				Local FogColorR% = Left(FogColor_HCZ,3)
				Local FogColorG% = Mid(FogColor_HCZ,4,3)
				Local FogColorB% = Right(FogColor_HCZ,3)
				CameraFogColor Camera,FogColorR,FogColorG,FogColorB
				CameraClsColor Camera,FogColorR,FogColorG,FogColorB
			EndIf
			
			;CameraFogColor (Camera,200+(angle*40),200+(angle*20),200)
			;CameraClsColor (Camera,200+(angle*40),200+(angle*20),200)
			CameraRange(Camera, 0.01, 80)
			
			AmbientLight (140, 140, 140)
			
			UpdateSky(Sky)
		EndIf 
		
; ~ Actual Ending Event
		
		If e\EventState[0] < 70*9 Then
			EntityAlpha e\room\Objects[1],1
		ElseIf e\EventState[0] > 70*9 And e\EventState[0] < 70*57 Then
			e\EventState[1] = e\EventState[1] - (0.01*FPSfactor)
			EntityAlpha e\room\Objects[1],Min(1,e\EventState[1]+3)
		EndIf
		
		If e\EventState[0] > 70*46 And e\EventState[0] < 70*47 Then
			e\EventState[1] = 0
			EntityAlpha e\room\Objects[1],1
		EndIf
		If e\EventState[0] > 70*49 And e\EventState[0] < 70*57 Then
			e\EventState[1] = 0
			EntityAlpha e\room\Objects[1],1
		EndIf
		
		If e\EventState[0] < 70*46 Then
			CanPlayerUseGuns = False
			psp\IsShowingHUD = False
			psp\NoRotation = True
			psp\NoMove = True
			mpl\ShowPlayerModel = False
			CameraShake = 0.25
		ElseIf e\EventState[0] > 70*57 And e\EventState[0] < 70*70 Then
			e\EventState[1] = e\EventState[1] - (0.01*FPSfactor)
			EntityAlpha e\room\Objects[1],Min(1,e\EventState[1]+3)
			CanPlayerUseGuns = False
			UnableToMove = False
			psp\NoMove = True
			PositionEntity Collider,EntityX(e\room\Objects[2],True)-0.5,EntityY(e\room\Objects[2],True),EntityZ(e\room\Objects[2],True)+2.2
			ResetEntity Collider
		ElseIf e\EventState[0] > 70*70 Then
			CanPlayerUseGuns = True
			psp\NoRotation = False
			psp\NoMove = False
			psp\IsShowingHUD = True
			mpl\ShowPlayerModel = True
;			If e\room\NPC[0] = Null Then
;				e\room\NPC[0] = CreateNPC(NPCTypeCI,EntityX(e\room\Objects[3],True),EntityY(e\room\Objects[3],True),EntityZ(e\room\Objects[3],True))
;			EndIf
;			If e\room\NPC[1] = Null Then
;				e\room\NPC[1] = CreateNPC(NPCTypeCI,EntityX(e\room\Objects[4],True),EntityY(e\room\Objects[4],True),EntityZ(e\room\Objects[4],True))
;			EndIf
;			If e\room\NPC[2] = Null Then
;				e\room\NPC[2] = CreateNPC(NPCTypeCI,EntityX(e\room\Objects[5],True),EntityY(e\room\Objects[5],True),EntityZ(e\room\Objects[5],True))
;			EndIf
;			If e\room\NPC[3] = Null Then
;				e\room\NPC[3] = CreateNPC(NPCTypeCI,EntityX(e\room\Objects[6],True),EntityY(e\room\Objects[6],True),EntityZ(e\room\Objects[6],True))
;			EndIf
		EndIf
		
;! ~ Old Event
		
		;[Block]
;		If e\EventState[0] >= 70*70 Then
;			Local alldead% = True
;		
;			For b = 0 To 3
;				If e\room\NPC[b] <> Null And (Not e\room\NPC[b]\IsDead) Then
;					alldead = False
;					Exit
;				EndIf
;			Next
;			
;			If alldead Then
;				e\EventState[2] = 1
;			EndIf
;		EndIf
;		
;		If e\EventState[2] > 0 Then
;			SelectedEnding = "a1"
;			psp\IsShowingHUD = False
;			
;			CameraFogRange Camera, 0.5, 2.5
;			
;			If ChannelPlaying(e\SoundCHN[0]) Then
;				StopChannel(e\SoundCHN[0])
;				If opt\MusicVol > 0 Then
;					e\SoundCHN[1] = PlaySound_Strict(LoadTempSound("SFX\Music\Chase_Music\Target_Number_1_End.ogg"))
;					e\SoundCHN_isStream[1] = True
;				EndIf
;				e\EventState[0] = 4
;			EndIf
;			
;			RemoveEvent(e)
;			Return
;		EndIf
		;[End Block]
		
		
		
; ~ End
		
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D