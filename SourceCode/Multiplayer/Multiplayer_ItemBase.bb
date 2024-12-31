
; ~ Item spawner constants

Const ITEMTYPE_GUN = 0
Const ITEMTYPE_HEALTH = 1
Const ITEMTYPE_KEVLAR = 2
Const ITEMTYPE_AMMO = 3

Function InitMPItemTemplates()
	Local it.ItemTemplates,it2.ItemTemplates
	
	it = CreateItemTemplate(GetLocalString("Item Names","vest"), "vest", "GFX\items\Wearible\vest.b3d", "GFX\items\Icons\Icon_misc.jpg", "", 0.02,"GFX\items\Wearible\Fine_Vest.png") : it\sound = 2 : it\IsEquippable = True
	it = CreateItemTemplate(GetLocalString("Item Names","vest_heavy"), "heavyvest", "GFX\items\Wearible\vest.b3d", "GFX\items\Icons\Icon_misc.jpg", "", 0.02,"GFX\Items\Wearible\Heavy_vest.png") : it\sound = 2 : it\IsEquippable = True
	it = CreateItemTemplate(GetLocalString("Item Names","first_aid"), "firstaid", "GFX\items\Consumable\firstaid.x", "GFX\items\Icons\Icon_misc.jpg", "", 0.05) : it\IsUsable = True
	it = CreateItemTemplate(GetLocalString("Item Names","first_aid_blue"), "firstaid2", "GFX\items\Consumable\firstaid.x", "GFX\items\Icons\Icon_misc.jpg", "", 0.03, "GFX\items\Consumable\firstaidkit2.jpg") : it\IsUsable = True
	it = CreateItemTemplate(GetLocalString("Item Names","syringe"), "syringe", "GFX\items\Consumable\syringe.b3d", "GFX\items\Icons\Icon_misc.jpg", "", 0.005) : it\sound = 2 : it\IsUsable = True
	it = CreateItemTemplate(GetLocalString("Item Names","fuse"), "fuse", "GFX\items\Usable\fuse.b3d", "GFX\items\Icons\Icon_fuse.jpg", "", 0.025)
	it = CreateItemTemplate("SCP-500-01", "scp500", "GFX\items\SCPs\scp_500_pill.b3d", "GFX\items\Icons\Icon_misc.jpg", "", 0.0001) : it\sound = 2 : it\IsUsable = True
	it = CreateItemTemplate(GetLocalString("Item Names", "suppressor"), "suppressor", "GFX\Weapons\Models\Suppressor_Worldmodel.b3d","GFX\items\Icons\Icon_misc.jpg","",0.015) : it\sound = 2
	it = CreateItemTemplate(GetLocalString("Item Names", "match"), "match", "GFX\Weapons\Models\Match_Worldmodel.b3d","GFX\items\Icons\Icon_misc.jpg","",0.03) : it\sound = 2
	it = CreateItemTemplate(GetLocalString("Item Names", "acog"), "acog", "GFX\Weapons\Models\Acog_Worldmodel.b3d","GFX\items\Icons\Icon_misc.jpg","",0.02) : it\sound = 2
	it = CreateItemTemplate(GetLocalString("Item Names", "eotech"), "eotech", "GFX\Weapons\Models\EoTech_Worldmodel.b3d","GFX\items\Icons\Icon_misc.jpg","",0.04) : it\sound = 2
	it = CreateItemTemplate(GetLocalString("Item Names", "red_dot"), "reddot", "GFX\Weapons\Models\RedDot_Worldmodel.b3d","GFX\items\Icons\Icon_misc.jpg","",0.04) : it\sound = 2
;	it = CreateItemTemplate(GetLocalString("Item Names", "laser"), "lasersight", "GFX\Weapons\Models\Attachments\lasersight.b3d","GFX\items\Icons\Icon_misc.jpg","",0.04) : it\sound = 2
	it = CreateItemTemplate(GetLocalString("Item Names", "mag"), "extmag", "GFX\Weapons\Models\extmag_box.b3d","GFX\items\Icons\Icon_misc.jpg","",0.02) : it\sound = 2
;	it = CreateItemTemplate(GetLocalString("Item Names", "m_u"), "mui", "GFX\Weapons\Models\Attachments\emr-p_mui.b3d","GFX\items\Icons\Icon_misc.jpg","",0.01) : it\sound = 2
	
	For it = Each ItemTemplates
		If (it\tex<>0) Then
			If (it\texpath<>"") Then
				For it2=Each ItemTemplates
					If (it2<>it) And (it2\tex=it\tex) Then
						it2\tex = 0
					EndIf
				Next
			EndIf
			DeleteSingleTextureEntryFromCache it\tex : it\tex = 0
		EndIf
	Next
	
End Function

Type ItemSpawner
	Field obj%
	Field itemtype%
	Field time#
	Field picked%
	Field item.Items
	Field respawntime%
	Field rndtime1%,rndtime2%
End Type

Function CreateItemSpawner.ItemSpawner(x#,y#,z#,itemtype%,rstime%,rndtime1%=0,rndtime2%=0)
	Local its.ItemSpawner = New ItemSpawner
	
	its\obj = CreatePivot()
	PositionEntity its\obj,x#,y#,z#
	its\itemtype = itemtype
	its\picked = True
	its\respawntime = rstime
	its\rndtime1 = rndtime1
	its\rndtime2 = rndtime2
	
	Return its
End Function

Function UpdateItemSpawners()
	Local its.ItemSpawner,Random%, RandomIt%
	Local g.Guns,gunAmount%
	
	Local stopItemSpawn% = False
	If mp_I\Gamemode\ID = Gamemode_Waves Lor mp_I\Gamemode\ID = Gamemode_EAF Then
		If mp_I\Gamemode\Phase > 0 And (mp_I\Gamemode\Phase Mod 2) = 0 Then
			stopItemSpawn = True
		EndIf
	EndIf
	
	If (Not stopItemSpawn) Then
		For its = Each ItemSpawner
			If its\time <= 0.0 Then
				If its\picked Then
					Select its\itemtype
						Case ITEMTYPE_GUN
							gunAmount = 0
							For g = Each Guns
								gunAmount = gunAmount + 1
							Next
							Local found% = False
							While (Not found)
								Random = Rand(1, gunAmount)
								For g = Each Guns
									If g\ID = Random Then
										If g\name <> "knife" And g\name <> "emrp" And g\name <> "scp127" And g\name <> "tt33" And g\name <> "m67" And g\name <> "rgd5" Then
											its\item = CreateItem(g\DisplayName, g\name, EntityX(its\obj), EntityY(its\obj), EntityZ(its\obj))
											its\item\state = GetWeaponMaxCurrAmmo(g\ID)
											its\item\state2 = Floor(GetWeaponMaxReloadAmmo(g\ID)/2.0)
											EntityType its\item\collider, HIT_ITEM
											found = True
										EndIf
										Exit
									EndIf
								Next
							Wend
						Case ITEMTYPE_HEALTH
							Random = Rand(0,3)
							Select Random
								Case 0
									its\item = CreateItem(GetLocalString("Item Names","first_aid"),"firstaid",EntityX(its\obj),EntityY(its\obj),EntityZ(its\obj))
								Case 1
									its\item = CreateItem(GetLocalString("Item Names","first_aid_blue"),"firstaid2",EntityX(its\obj),EntityY(its\obj),EntityZ(its\obj))
								Case 2
									its\item = CreateItem(GetLocalString("Item Names","syringe"),"syringe",EntityX(its\obj),EntityY(its\obj),EntityZ(its\obj))
								Case 3
									its\item = CreateItem("SCP-500-01","scp500",EntityX(its\obj),EntityY(its\obj),EntityZ(its\obj))
							End Select
							EntityType its\item\collider,HIT_ITEM
						Case ITEMTYPE_KEVLAR
							RandomIt = Rand(0,2)
							Select RandomIt
								Case 0 ; ~ Kevlar
									Random = Rand(0,1)
									Select Random
										Case 0
											its\item = CreateItem(GetLocalString("Item Names","vest"),"vest",EntityX(its\obj),EntityY(its\obj),EntityZ(its\obj))
										Case 1
											its\item = CreateItem(GetLocalString("Item Names","vest_heavy"),"heavyvest",EntityX(its\obj),EntityY(its\obj),EntityZ(its\obj))
									End Select
								Case 1,2 ; ~ Attachments
									Random = Rand(0,5)
									Select Random
										Case 0
											its\item = CreateItem(GetLocalString("Item Names", "suppressor"), "suppressor",EntityX(its\obj),EntityY(its\obj),EntityZ(its\obj))
										Case 1
											its\item = CreateItem(GetLocalString("Item Names", "match"), "match",EntityX(its\obj),EntityY(its\obj),EntityZ(its\obj))
										Case 2
											its\item = CreateItem(GetLocalString("Item Names", "acog"), "acog",EntityX(its\obj),EntityY(its\obj),EntityZ(its\obj))
										Case 3
											its\item = CreateItem(GetLocalString("Item Names", "eotech"), "eotech",EntityX(its\obj),EntityY(its\obj),EntityZ(its\obj))
										Case 4
											its\item = CreateItem(GetLocalString("Item Names", "red_dot"), "reddot",EntityX(its\obj),EntityY(its\obj),EntityZ(its\obj))
										Case 5
											its\item = CreateItem(GetLocalString("Item Names", "mag"), "extmag",EntityX(its\obj),EntityY(its\obj),EntityZ(its\obj))
									End Select
							End Select
							EntityType its\item\collider,HIT_ITEM
						Case ITEMTYPE_AMMO
							Random = Rand(0,1)
							Select Random
								Case 0
									its\item = CreateItem(GetLocalString("Item Names","ammo"),"ammocrate",EntityX(its\obj),EntityY(its\obj),EntityZ(its\obj))
								Case 1
									its\item = CreateItem(GetLocalString("Item Names","ammo_big"),"bigammocrate",EntityX(its\obj),EntityY(its\obj),EntityZ(its\obj))
							End Select
							EntityType its\item\collider,HIT_ITEM
					End Select
				EndIf
				its\picked = False
				If its\item = Null
					its\picked = True
					If mp_I\Gamemode\ID = Gamemode_Waves Then
						its\time = mp_I\Gamemode\PhaseTimer+70
					ElseIf mp_I\Gamemode\ID = Gamemode_EAF Then
						its\time = mp_I\Gamemode\PhaseTimer+60
					Else
						its\time = 70*(its\respawntime+Rand(its\rndtime1,its\rndtime2))
					EndIf
				EndIf
			Else
				its\time = its\time - FPSfactor
			EndIf
		Next
	EndIf
	
End Function

Function UpdateMPItems()
	Local i.Items
	
	Local HideDist = GetCameraRangeFar(Camera)
	Local deletedItem% = False
	
	ClosestItem = Null
	For i.Items = Each Items
		If i\collider<>0 Then
			If i\itemtemplate\tempname = "fuse" Then
				If i\OverHereSprite = 0 Then
					i\OverHereSprite = CreateSprite()
					Local tex% = LoadTexture_Strict("GFX\HUD\communication_wheel\look_fuse.png",1+2)
					ScaleSprite i\OverHereSprite,0.125,0.125
					EntityOrder i\OverHereSprite,-2
					EntityTexture i\OverHereSprite,tex
					EntityFX i\OverHereSprite,1+8
					DeleteSingleTextureEntryFromCache tex
				EndIf
				
				If Players[mp_I\PlayerID]\Item <> Null And Players[mp_I\PlayerID]\Item\itemtemplate\tempname = "fuse" Then
					HideEntity i\OverHereSprite
				Else
					ShowEntity i\OverHereSprite
					PositionEntity i\OverHereSprite, EntityX(i\collider), EntityY(i\collider), EntityZ(i\collider)
				EndIf
			EndIf
			
			If (Not i\Picked) Then
				If i\disttimer < MilliSecs() Then
					i\dist = EntityDistance(Players[mp_I\PlayerID]\Collider, i\collider)
					i\disttimer = MilliSecs() + 700
				EndIf
				
				If i\dist < HideDist Then
					ShowEntity i\model
					
					If i\dist < 1.2 Then
						If GetClosestPlayerIDFromEntity(i\collider) = mp_I\PlayerID Then
							If ClosestItem = Null Then
								If EntityInView(i\model, Camera) Then
									If EntityVisible(i\collider,Camera) Then
										ClosestItem = i
									EndIf
								EndIf
							ElseIf ClosestItem = i Lor i\dist < EntityDistance(Camera, ClosestItem\collider) Then
								If EntityInView(i\model, Camera) Then
									If EntityVisible(i\collider,Camera) Then
										ClosestItem = i
									EndIf
								EndIf
							EndIf
						EndIf
					EndIf
				Else
					HideEntity i\model
				EndIf
			EndIf
			deletedItem = False
		EndIf
	Next
	
	If MenuOpen Lor ConsoleOpen Then ClosestItem=Null
	If Players[mp_I\PlayerID]\CurrHP <= 0 Then ClosestItem = Null
	
	If ClosestItem <> Null Then
		If KeyHitUse Then
			ClosestItem\Dropped = 0
			PickMPItem(ClosestItem,mp_I\PlayerID)
		EndIf
	EndIf
	
End Function

Function UpdateMPItemsGravity()
	Local i.Items,i2.Items
	Local xtemp#,ytemp#,ztemp#,ed#
	
	For i.Items = Each Items
		If i\collider<>0 Then
			If EntityCollided(i\collider, HIT_MAP) Then
				i\DropSpeed = 0
				i\xspeed = 0.0
				i\zspeed = 0.0
			Else
				i\DropSpeed = i\DropSpeed - 0.0004 * FPSfactor
				TranslateEntity i\collider, i\xspeed*FPSfactor, i\DropSpeed * FPSfactor, i\zspeed*FPSfactor
				If i\WontColl Then ResetEntity(i\collider)
			EndIf
			
			For i2.Items = Each Items
				If i<>i2 And (Not i2\Picked) Then
					
					xtemp# = (EntityX(i2\collider,True)-EntityX(i\collider,True))
					ytemp# = (EntityY(i2\collider,True)-EntityY(i\collider,True))
					ztemp# = (EntityZ(i2\collider,True)-EntityZ(i\collider,True))
					
					ed# = (xtemp*xtemp+ztemp*ztemp)
					If ed<0.07 And Abs(ytemp)<0.25 Then
						
						;items are too close together, push away
						
						xtemp = xtemp*(0.07-ed)
						ztemp = ztemp*(0.07-ed)
						
						While Abs(xtemp)+Abs(ztemp)<0.001
							xtemp = xtemp+Rnd(-0.002,0.002)
							ztemp = ztemp+Rnd(-0.002,0.002)
						Wend
						
						TranslateEntity i2\collider,xtemp,0,ztemp
						TranslateEntity i\collider,-xtemp,0,-ztemp
					EndIf
				EndIf
			Next
			
			If EntityY(i\collider) < - 35.0 Then RemoveItem(i) ; : DebugLog "remove: " + i\itemtemplate\name
		EndIf
	Next
	
End Function

Function PickMPItem(item.Items,playerID%)
	CatchErrors("Uncaught (PickMPItem)")
	Local picked = False
	Local i%
	Local g.Guns,g2.Guns
	Local it2.Items
	
	Select item\itemtemplate\tempname
		Case "vest"
			If Players[playerID]\CurrKevlar < 100 Then
				PlayItemPickSoundMP(item,playerID)
				If mp_I\PlayState=GAME_SERVER Then
					Players[playerID]\CurrKevlar = 100
				EndIf
				picked = True
			EndIf
		Case "heavyvest"
			If Players[playerID]\CurrKevlar < 150 Then
				PlayItemPickSoundMP(item,playerID)
				If mp_I\PlayState=GAME_SERVER Then
					Players[playerID]\CurrKevlar = 150
				EndIf
				picked = True
			EndIf
		Case "firstaid","firstaid2"
			If Players[playerID]\CurrHP < 100 Then
				PlayItemPickSoundMP(item,playerID)
				If mp_I\PlayState=GAME_SERVER Then
					Select item\itemtemplate\tempname
						Case "firstaid"
							Players[playerID]\CurrHP = Min(Players[playerID]\CurrHP+75,100)
						Case "firstaid2"
							Players[playerID]\CurrHP = Min(Players[playerID]\CurrHP+50,100)
					End Select		
				EndIf
				picked = True
			EndIf
		Case "scp500"
			PlayItemPickSoundMP(item,playerID)
			If mp_I\PlayState=GAME_SERVER Then
				Players[playerID]\CurrHP = 100
				Players[playerID]\StaminaEffectTimer = 20*70
			EndIf
			picked = True
		Case "syringe"
			PlayItemPickSoundMP(item,playerID)
			If mp_I\PlayState=GAME_SERVER Then
				Players[playerID]\StaminaEffectTimer = 10*70
				Players[playerID]\CurrHP = Min(Players[playerID]\CurrHP+10,100)
			EndIf
			picked = True
		Case "ammocrate", "bigammocrate"
			For i = 0 To MaxSlots-SlotsWithNoAmmo-1
				For g = Each Guns
					If g\ID = Players[playerID]\WeaponInSlot[i] Then
						If Players[playerID]\ReloadAmmo[i]<g\MaxReloadAmmo Then
							PlayItemPickSoundMP(item,playerID)
							If mp_I\PlayState=GAME_SERVER Then
								Select item\itemtemplate\tempname
									Case "ammocrate"
										Players[playerID]\ReloadAmmo[i]=Min(Players[playerID]\ReloadAmmo[i]+(2*g\MaxCurrAmmo),g\MaxReloadAmmo)
									Case "bigammocrate"
										Players[playerID]\ReloadAmmo[i]=Min(Players[playerID]\ReloadAmmo[i]+(4*g\MaxCurrAmmo),g\MaxReloadAmmo)
								End Select
							EndIf
							picked = True
							Exit
						EndIf
					EndIf
				Next
			Next
		Case "fuse"
			If Players[playerID]\Item = Null Then
				PlayItemPickSoundMP(item,playerID)
				picked = True
				If mp_I\PlayState = GAME_SERVER Then
					Players[playerID]\Item = CreateInventoryItem(item)
				EndIf
			EndIf
		Case "suppressor"
			For g = Each Guns
				g\HasPickedAttachments[ATT_SUPPRESSOR] = True
			Next
			picked = True
;			If mp_I\PlayState = GAME_SERVER Then
;				Players[playerID]\Item = CreateInventoryItem(item)
;			EndIf
		Case "match"
			For g = Each Guns
				g\HasPickedAttachments[ATT_MATCH] = True
			Next
			picked = True
;			If mp_I\PlayState = GAME_SERVER Then
;				Players[playerID]\Item = CreateInventoryItem(item)
;			EndIf
		Case "acog"
			For g = Each Guns
				g\HasPickedAttachments[ATT_ACOG_SCOPE] = True
			Next
			picked = True
;			If mp_I\PlayState = GAME_SERVER Then
;				Players[playerID]\Item = CreateInventoryItem(item)
;			EndIf
		Case "eotech"
			For g = Each Guns
				g\HasPickedAttachments[ATT_EOTECH] = True
			Next
			picked = True
;			If mp_I\PlayState = GAME_SERVER Then
;				Players[playerID]\Item = CreateInventoryItem(item)
;			EndIf
		Case "reddot"
			For g = Each Guns
				g\HasPickedAttachments[ATT_RED_DOT] = True
			Next
			picked = True
;			If mp_I\PlayState = GAME_SERVER Then
;				Players[playerID]\Item = CreateInventoryItem(item)
;			EndIf
		Case "extmag"
			For g = Each Guns
				g\HasPickedAttachments[ATT_EXT_MAG] = True
			Next
			picked = True
;			If mp_I\PlayState = GAME_SERVER Then
;				Players[playerID]\Item = CreateInventoryItem(item)
;			EndIf
		Default
			If item\itemtemplate\IsGun Then
				Local found = False
				For i = 0 To MaxSlots-SlotsWithNoAmmo
					For g = Each Guns
						If g\name = item\itemtemplate\tempname Then
							If g\ID = Players[playerID]\WeaponInSlot[i] Then
								found = True
								Exit
							EndIf
						EndIf
					Next
					If found Then
						Exit
					EndIf
				Next
				For g = Each Guns
					If g\name = item\itemtemplate\tempname Then
						If (Not found) Then
							PlayItemPickSoundMP(item,playerID)
							If playerID=mp_I\PlayerID Then
								mpl\SlotsDisplayTimer = 70*3
							EndIf
							If mp_I\PlayState=GAME_SERVER Then
								For g2 = Each Guns
									If g2\ID = Players[playerID]\WeaponInSlot[g\Slot] Then
										it2 = CreateItem(g2\DisplayName,g2\name,EntityX(Players[playerID]\Collider),EntityY(Players[playerID]\Collider),EntityZ(Players[playerID]\Collider))
										EntityType it2\collider,HIT_ITEM
										it2\xspeed = Cos(Players[playerID]\Yaw+90)*0.005
										it2\zspeed = Sin(Players[playerID]\Yaw+90)*0.005
										If g\GunType <> GUNTYPE_MELEE Then
											it2\state = Players[playerID]\Ammo[g\Slot]
											it2\state2 = Players[playerID]\ReloadAmmo[g\Slot]
										EndIf
									EndIf
								Next
								Players[playerID]\WeaponInSlot[g\Slot] = g\ID
								If g\GunType <> GUNTYPE_MELEE Then
									Players[playerID]\Ammo[g\Slot] = item\state
									Players[playerID]\ReloadAmmo[g\Slot] = item\state2
								EndIf
								If playerID = mp_I\PlayerID Then
									g_I\GunChangeFLAG = False
									Players[playerID]\SelectedSlot = g\Slot
								Else
									Players[playerID]\DeployState = 0
									Players[playerID]\ReloadState = 0
									Players[playerID]\ShootState = 0
									Players[playerID]\PressMouse1 = False
									Players[playerID]\PressReload = False
								EndIf
							Else
								Players[playerID]\WantsSlot = g\Slot
							EndIf
							picked = True
						Else
							For i = 0 To MaxSlots-SlotsWithNoAmmo-1
								If g\ID = Players[playerID]\WeaponInSlot[i] Then
									If Players[playerID]\ReloadAmmo[i]<g\MaxReloadAmmo Then
										PlayItemPickSoundMP(item,playerID)
										If mp_I\PlayState=GAME_SERVER Then
											Players[playerID]\ReloadAmmo[i]=Min(Players[playerID]\ReloadAmmo[i]+item\state+item\state2,g\MaxReloadAmmo)
										EndIf
										picked = True
									EndIf
									Exit
								EndIf
							Next
						EndIf
						Exit
					EndIf
				Next
			EndIf
	End Select
	
	If picked Then
		If mp_I\PlayState=GAME_SERVER Then
			RemoveItem(item)
		Else
			item\Picked = True
		EndIf
	EndIf
	
	CatchErrors("PickMPItem")
End Function

Function PlayItemPickSoundMP(item.Items,playerID%)
	Local g.Guns
	
	Select item\itemtemplate\tempname
		Case "vest","heavyvest"
			If playerID=mp_I\PlayerID Then
				PlaySound_Strict LoadTempSound("SFX\Interact\PickUpKevlar.ogg")
			Else
				PlaySound2(LoadTempSound("SFX\Interact\PickUpKevlar.ogg"),Camera,item\collider,5)
			EndIf
		Default
			If item\itemtemplate\IsGun Then
				For g = Each Guns
					If g\name = item\itemtemplate\tempname Then
						If playerID=mp_I\PlayerID Then
							PlaySound_Strict LoadTempSound("SFX\Guns\"+g\name+"\pickup.ogg")
						Else
							PlaySound2(LoadTempSound("SFX\Guns\"+g\name+"\pickup.ogg"),Camera,item\collider,5)
						EndIf
						Exit
					EndIf
				Next
			Else
				If playerID=mp_I\PlayerID Then
					PlaySound_Strict PickSFX[item\itemtemplate\sound]
				Else
					PlaySound2(PickSFX[item\itemtemplate\sound],Camera,item\collider,5)
				EndIf
			EndIf
	End Select
	
End Function

Type InventoryItem
	Field itemtemplate.ItemTemplates
	Field isDeleted% ;A variable for syncing the client to server
End Type

Function CreateInventoryItem.InventoryItem(item.Items)
	Local it.InventoryItem = New InventoryItem
	
	it\itemtemplate = item\itemtemplate
	
	Return it
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS