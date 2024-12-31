; ~ Gun constants
;[Block]
; ~ Gun IDs ;TODO - Need to be replaced with something else in the future!
Const GUN_UNARMED = 0
Const GUN_KNIFE = 1
Const GUN_CROWBAR = 2
Const GUN_TT33 = 3
Const GUN_BERETTA = 4
Const GUN_USP = 5
Const GUN_FIVESEVEN = 6
Const GUN_GLOCK = 7
Const GUN_P90 = 8
Const GUN_MP5 = 9
Const GUN_MP7 = 10
Const GUN_RPK16 = 11
Const GUN_M4A1 = 12
Const GUN_M870 = 13
Const GUN_SPAS12 = 14
Const GUN_EMRP = 15
Const GUN_M67 = 16
Const GUN_RGD5 = 17
Const GUN_SCP127 = 18
; ~ Attachments IDs
Const ATT_SUPPRESSOR = 0
Const ATT_MATCH = 1
Const ATT_EXT_MAG = 2
Const ATT_SPECIAL_SCOPE = 3
Const ATT_ACOG_SCOPE = 4
Const ATT_RED_DOT = 5
Const ATT_EOTECH = 6
Const ATT_MUI = 7
; ~ Gun Types
Const GUNTYPE_UNKNOWN = -1
Const GUNTYPE_MELEE = 0
Const GUNTYPE_HANDGUN = 1
Const GUNTYPE_SMG = 2
Const GUNTYPE_RIFLE = 3
Const GUNTYPE_SHOTGUN = 4
; ~ Animation Types
Const GUNANIM_SMG = 0
Const GUNANIM_PISTOL = 1
Const GUNANIM_MP5K = 2
Const GUNANIM_SHOTGUN = 3
Const GUNANIM_MELEE = 4
Const GUNANIM_RIFLE = 5
; ~ Weapon Decal Types
Const GUNDECAL_DEFAULT = 0
Const GUNDECAL_SLASH = 1
Const GUNDECAL_SMASH = 2
; ~ Quick Slots
Const QUICKSLOT_PRIMARY = 0
Const QUICKSLOT_SECONDARY = 1
Const QUICKSLOT_HOLSTER = 2
Const QUICKSLOT_SCABBARD = 3
; ~ Other
Const MaxGunSlots = 4
Const MaxShootSounds = 4
Const MaxAttachments = 8
Const Grenade_M67% = 0
Const Grenade_RGD5% = 1
Const ScopeChargeTime# = 70.0*300
;[End Block]

Global AimCrossIMG[6]
Global BulletIcon%
Global MuzzleFlash
Global BulletHole1,BulletHole2,DustParticle

Global CanPlayerUseGuns% = True
Global IsPlayerShooting% = False
Global NTF_InfiniteAmmo% = False
Global NTF_NoReload% = False
Global OnSafety%

Global IronSightPivot%,IronSightPivot2%
Global IronSightTimer#
Global ReadyToShowDot% = False

Global GunPickPivot
Global GunPivot_Y#
Global GunPivot_YSide% = 0
Global GunPivot_X#
Global GunPivot_XSide% = 0
Global GunParticle

Global UsingScope%
Global ScopeTexture
Global ScopeCam
Global ScopeZoom#
Global ScopeNVG%

Global GunSFX,GunSFX2,GunCHN,GunCHN2
Global NVGOnSFX%, NVGOffSFX%

Type GunInstance
	Field GunAnimFLAG%
	Field GunChangeFLAG%
	Field Weapon_CurrSlot%
	Field HoldingGun%
	Field Weapon_InSlot$[MaxGunSlots]
	Field KevlarSFX%
	Field GunPivot
	Field UI_Select_SFX%
	Field UI_Deny_SFX%
	Field GunLight%
	Field GunLightTimer#
	Field AttachSFX%
	Field DetachSFX%
	Field IronSight%,RedDotIronSight%,AcogIronSight%,P90ScopeIronSight%,EoTechIronSight%
	Field IronSightAnim%
End Type

Type Guns
	;TODO: Clean this up!
	Field GunType%
	Field ID
	Field IMG
	Field INV_IMG
	Field CurrAmmo
	Field MaxCurrAmmo
	Field MaxCurrAmmo_Ext_Mag
	Field CurrReloadAmmo
	Field MaxReloadAmmo
	Field MaxReloadAmmo_Ext_Mag
	Field Stored_MaxCurrAmmo
	Field Stored_MaxReloadAmmo
	Field DamageOnEntity
	Field Accuracy#
	Field StoredAccuracy#
	Field Knockback#
	Field StoredKnockback#
	Field Rate_Of_Fire#
	Field Stored_Rate_Of_Fire#
	
	Field ScopeCharge#
	Field FireMode#
	Field CanSelectMenuAttachments%
	
	Field Frame_Idle#
	Field Frame_NoAmmo_Idle#
	Field Frame_Safety#
	Field Frame_NoAmmo_Safety#
	Field Frame_Attachment#
	Field Frame_NoAmmo_Attachment#
	Field Reload_Empty_Time#
	Field Reload_Empty_Time_Ext_Mag#
	Field Stored_Reload_Empty_Time#
	Field Reload_Time#
	Field Reload_Time_Ext_Mag#
	Field Stored_Reload_Time#
	Field Reload_Start_Time#
	Field Reload_Start_Empty_Time#
	Field Stored_Reload_Start_Time#
	Field Ready_Time#
	Field Ready_Time_Ext_Mag#
	Field Deploy_Time#
	Field Stored_Deploy_Time#
	
	Field Amount_Of_Bullets%
	Field ShootDelay#
	Field Range#
	Field ShootState#
	Field ReloadState#
	Field DeployState#
	Field Frame#
	Field name$
	Field DisplayName$
	Field MouseDownTimer#
	Field obj%
	Field CanUseIronSight
	
	Field ViewModelPath$
	Field IMGPath$
	
	Field IronSightCoords.Vector3D
	Field RedDotIronSightCoords.Vector3D
	Field EoTechIronSightCoords.Vector3D
	Field AcogIronSightCoords.Vector3D
	Field P90ScopeIronSightCoords.Vector3D
	
	Field Anim_Ready.Vector3D
	Field Anim_Ready_Ext_Mag.Vector3D
	Field Anim_Jam.Vector3D
	
	Field Anim_Deploy.Vector3D
	Field Anim_NoAmmo_Deploy.Vector3D
	
	Field Anim_Shoot.Vector3D
	Field Anim_Shoot_Alt.Vector3D
	Field Anim_NoAmmo_Shoot.Vector3D
	
	Field Anim_Reload_Empty.Vector3D
	Field Anim_Reload_Empty_Ext_Mag.Vector3D
	Field Anim_Reload.Vector3D
	Field Anim_Reload_Ext_Mag.Vector3D
	Field Anim_Reload_Start.Vector3D
	Field Anim_Reload_Start_Empty.Vector3D
	Field Anim_Reload_Stop.Vector3D
	
	Field Anim_Sprint_Transition.Vector3D
	Field Anim_Sprint_Cycle.Vector3D
	Field Anim_NoAmmo_Sprint_Transition.Vector3D
	Field Anim_NoAmmo_Sprint_Cycle.Vector3D
	
	Field MaxShootSounds%
	Field MaxSuppressedShootSounds%
	Field MaxReloadSounds%
	Field MaxWallhitSounds%
	
	Field Slot%
	Field ShootSounds%[MaxShootSounds]
	Field SuppressedShootSounds%[MaxShootSounds]
	Field MuzzleFlash%
	Field Reticle%
	Field PlayerModel%
	Field PlayerModelAnim%
	Field ShouldCreateItem%
	Field AttachedItemTemplate.ItemTemplates
	Field DecalType%
	
	Field Found% = False
	Field SharingHands% = False
	Field IsSeparate%
	Field HandsObj%
	
	Field JamTimer#
	Field JamTimer2#
	Field JamAmount#
	Field JamCooldown#
	Field JamState#
	Field MaxJams#
	
	Field CanHaveAttachments%[MaxAttachments]
	Field HasAttachments%[MaxAttachments]
	Field HasToggledAttachments%[MaxAttachments]
	Field HasPickedAttachments%[MaxAttachments]
	
	Field BarrelAttachments#
	Field MountAttachments#
	Field GripAttachments#
	Field MagazineAttachments#
	Field MiscAttachments#
	
	Field SightDelayPitch#
	Field SightDelayYaw#
End Type

Type Bullet
	Field Numb
	Field DamageOnEntity
	Field FlySpeed
	Field Accuracy
End Type

Type BulletHole
	Field obj%
	Field obj2%
	Field obj3%
	Field obj4%
	Field obj5%
	Field obj6%
	Field KillTimer#,KillTimer2#
End Type

Type Grenades
	Field Pivot, obj
	Field Speed#, Rollcurr#, Angle#
	Field Channel
	Field Constpitch#
	Field Constyaw#
	Field GrenadeParticle.Emitters
	Field Timer#
	Field Timer2#
	Field Prevfloor, Prevy#
	Field XSpeed#
	Field State
	Field CollisionTimer#
	Field Ticks%
	Field Gun.Guns
	Field PlayerID
	Field ID
End Type

Function CreateGun.Guns(DisplayName$,Name$,Id,Model$,CanAim=True,GunType$="melee",Slot$="melee",Ammo#=1,ReloadAmmo#=1,Damage#=1,Accuracy#=1,Knockback#=1,FireRate#=1,BulletAmmount#=1,Range#=0,FireDelay#=0,DeployTime#=0,ReadyTime#=0,ReloadEmptyTime#=0,ReloadTime#=0,ReloadStartTime#=0,ReloadStartEmptyTime#=0,IsSeparate=False,JamState#=0,JamAmount#=0,JamCooldown#=0,CanSelectMenuAttachments%=False,SharedHandsModel$="",ReadyTimeExt#=0,ReloadTimeExt#=0,ReloadEmptyTimeExt#=0,ExtAmmo#=1,SightBacklashPitch#=1.3,SightBacklashYaw#=2.5)
	Local g.Guns = New Guns
	Local i%
	
;! ~ [Weapon Variables]
	
	g\name$ = Name$
	g\ID = Id
	g\IMG = LoadImage("GFX\weapons\Icons\INV"+Name$+".jpg")
	MaskImage(g\IMG, 255, 0, 255)
	g\INV_IMG = LoadImage("GFX\weapons\Slots\SLOT_"+Name$+".png")
	MaskImage(g\INV_IMG, 255, 0, 255)
	
	g\CurrAmmo = Ammo
	g\CurrReloadAmmo = 0
	g\MaxReloadAmmo = ReloadAmmo
	g\MaxCurrAmmo = g\CurrAmmo
	g\MaxCurrAmmo_Ext_Mag = ExtAmmo
	;g\MaxReloadAmmo_Ext_Mag = ExtReloadAmmo
	g\Stored_MaxCurrAmmo = g\MaxCurrAmmo
	g\Stored_MaxReloadAmmo = g\MaxReloadAmmo
	g\DamageOnEntity = Damage
	g\Accuracy = Accuracy
	g\StoredAccuracy = g\Accuracy
	g\IsSeparate = IsSeparate
	g\obj = LoadAnimMesh_Strict("GFX\weapons\models\"+Model$,g_I\GunPivot)
	ScaleEntity g\obj,0.005,0.005,0.005
	MeshCullBox(g\obj,-MeshWidth(g\obj)*3,-MeshHeight(g\obj)*3,-MeshDepth(g\obj)*3,MeshWidth(g\obj)*6,MeshHeight(g\obj)*6,MeshDepth(g\obj)*6)
	HideEntity g\obj
	
	g\CanUseIronSight = CanAim
	g\CanSelectMenuAttachments = CanSelectMenuAttachments
	If SharedHandsModel <> "" Then
		g\SharingHands = True
	EndIf
	g\SightDelayPitch = SightBacklashPitch
	g\SightDelayYaw = SightBacklashYaw
	
	g\JamState = JamState
	g\MaxJams = JamAmount
	g\JamCooldown = JamCooldown
	
	If IsSeparate Then
		If (Not g\SharingHands) Then
			g\HandsObj = LoadAnimMesh_Strict("GFX\weapons\models\hands\"+g\name+"_hands.b3d",g_I\GunPivot)
		Else
			g\HandsObj = LoadAnimMesh_Strict("GFX\weapons\models\hands\"+SharedHandsModel,g_I\GunPivot)
		EndIf
		ScaleEntity g\HandsObj,0.005,0.005,0.005
		MeshCullBox(g\HandsObj,-MeshWidth(g\HandsObj)*3,-MeshHeight(g\HandsObj)*3,-MeshDepth(g\HandsObj)*3,MeshWidth(g\HandsObj)*6,MeshHeight(g\HandsObj)*6,MeshDepth(g\HandsObj)*6)
		HideEntity g\HandsObj
	EndIf
	
	g\ViewModelPath = Model$
	g\IMGPath = g\IMG;Img$
	
	Local StrTemp$ = GunType
	Select StrTemp
		Case "melee"
			g\GunType = GUNTYPE_MELEE
		Case "handgun"
			g\GunType = GUNTYPE_HANDGUN
		Case "smg"
			g\GunType = GUNTYPE_SMG
		Case "rifle"
			g\GunType = GUNTYPE_RIFLE
		Case "shotgun"
			g\GunType = GUNTYPE_SHOTGUN
		Default
			g\GunType = GUNTYPE_UNKNOWN
	End Select
	
	g\Knockback = Knockback
	g\Rate_Of_Fire = FireRate
	g\Stored_Rate_Of_Fire = FireRate
	g\Reload_Empty_Time = ReloadEmptyTime
	g\Reload_Empty_Time_Ext_Mag = ReloadEmptyTimeExt
	g\Stored_Reload_Empty_Time = g\Reload_Empty_Time
	g\Reload_Time = ReloadTime
	g\Reload_Time_Ext_Mag = ReloadTimeExt
	g\Stored_Reload_Time = g\Reload_Time
	g\Reload_Start_Time = ReloadStartTime
	g\Reload_Start_Empty_Time = ReloadStartEmptyTime
	g\Stored_Reload_Start_Time = g\Reload_Start_Time
	g\Deploy_Time = DeployTime
	g\Ready_Time = ReadyTime
	g\Ready_Time_Ext_Mag = ReadyTimeExt
	g\Stored_Deploy_Time = g\Deploy_Time
	g\Amount_Of_Bullets = BulletAmmount
	
	g\MaxShootSounds = GetINIInt(gv\WeaponFile,Name,"sounds_shoot",1)
	g\MaxSuppressedShootSounds = GetINIInt(gv\WeaponFile,Name,"sounds_shoot_suppressed",1)
	g\MaxReloadSounds = GetINIInt(gv\WeaponFile,Name,"sounds_reload",1)
	g\MaxWallhitSounds = GetINIInt(gv\WeaponFile,Name,"sounds_wallhit",1)
	
	Local AnimString$
	
;! ~ [Animation Sequences]
	
	; ~ Ready
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_ready","")
	g\Anim_Ready = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	; ~ Ready Ext Mag
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_ready_ext_mag","")
	g\Anim_Ready_Ext_Mag = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	; ~ Deploy
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_deploy","")
	g\Anim_Deploy = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	; ~ Shoot
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_shoot","")
	g\Anim_Shoot = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	; ~ Shoot Alt
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_shoot_alt","")
	g\Anim_Shoot_Alt = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	; ~ Shoot Empty
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_noammo_shoot","")
	If AnimString<>"" Then
		g\Anim_NoAmmo_Shoot = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	EndIf
	; ~ Reload Empty
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_reload_empty","")
	g\Anim_Reload_Empty = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	; ~ Reload Empty Ext Mag
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_reload_empty_ext_mag","")
	g\Anim_Reload_Empty_Ext_Mag = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	; ~ Reload
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_reload","")
	g\Anim_Reload = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	; ~ Reload Ext Mag
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_reload_ext_mag","")
	g\Anim_Reload_Ext_Mag = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	; ~ Sprint Transition
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_sprint_transition","")
	g\Anim_Sprint_Transition = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	; ~ Sprint Cycle
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_sprint_cycle","")
	g\Anim_Sprint_Cycle = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	; ~ Deploy Empty
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_noammo_deploy","")
	If AnimString<>"" Then
		g\Anim_NoAmmo_Deploy = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	EndIf
	; ~ Sprint Transition Empty
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_noammo_sprint_transition","")
	If AnimString<>"" Then
		g\Anim_NoAmmo_Sprint_Transition = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	EndIf
	; ~ Sprint Cycle Empty
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_noammo_sprint_cycle","")
	If AnimString<>"" Then
		g\Anim_NoAmmo_Sprint_Cycle = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	EndIf
	; ~ Reload Start
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_reload_start","")
	If AnimString<>"" Then
		g\Anim_Reload_Start = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	EndIf
	; ~ Reload Start Empty
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_reload_start_empty","")
	If AnimString<>"" Then
		g\Anim_Reload_Start_Empty = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	EndIf
	; ~ Reload Stop
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_reload_stop","")
	If AnimString<>"" Then
		g\Anim_Reload_Stop = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	EndIf
	
;! ~ [Animation Frames]
	
	; ~ Jam
	AnimString = GetINIString(gv\WeaponFile,Name,"anim_jam","")
	g\Anim_Jam = CreateVector3D(Piece(AnimString,1,"|"),Piece(AnimString,2,"|"),Piece(AnimString,3,"|"))
	; ~ Idle
	g\Frame_Idle = GetINIFloat(gv\WeaponFile,Name,"frame_idle")
	; ~ Idle Empty
	g\Frame_NoAmmo_Idle = GetINIFloat(gv\WeaponFile,Name,"frame_noammo_idle")
	; ~ Safety
	g\Frame_Safety = GetINIFloat(gv\WeaponFile,Name,"frame_safety")
	; ~ Safety Empty
	g\Frame_NoAmmo_Safety = GetINIFloat(gv\WeaponFile,Name,"frame_safety_empty")
	; ~ Attachments
	g\Frame_Attachment = GetINIFloat(gv\WeaponFile,Name,"frame_attachments")
	; ~ Attachments Empty
	g\Frame_NoAmmo_Attachment = GetINIFloat(gv\WeaponFile,Name,"frame_attachments_empty")
	
;! ~ [Weapon Offsets]
	
	; ~ Weapon Offset
	Local VectorString$
	VectorString = GetINIString(gv\WeaponFile,Name,"offset","")
	MoveEntity g\obj,Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|")
	If IsSeparate Then
		MoveEntity g\HandsObj,Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|")
	EndIf
	; ~ Weapon Aim Offsets
	VectorString = GetINIString(gv\WeaponFile,Name,"aimoffset","")
	If VectorString<>"" Then
		g\IronSightCoords = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"eotech_aimoffset","")
	If VectorString<>"" Then
		g\EoTechIronSightCoords = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"reddot_aimoffset","")
	If VectorString<>"" Then
		g\RedDotIronSightCoords = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"acog_aimoffset","")
	If VectorString<>"" Then
		g\AcogIronSightCoords = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	VectorString = GetINIString(gv\WeaponFile,Name,"p90_scope_aimoffset","")
	If VectorString<>"" Then
		g\P90ScopeIronSightCoords = CreateVector3D(Piece(VectorString,1,"|"),Piece(VectorString,2,"|"),Piece(VectorString,3,"|"))
	EndIf
	
;! ~ [Weapon Properties]
	
	g\DisplayName = DisplayName
	StrTemp$ = Slot
	Select StrTemp
		Case "primary"
			g\Slot = QUICKSLOT_PRIMARY
		Case "secondary"
			g\Slot = QUICKSLOT_SECONDARY
		Case "melee"
			g\Slot = QUICKSLOT_HOLSTER
	End Select
	
	g\ShootDelay = FireDelay
	g\Range = Range
	
;! ~ [Weapon Sounds]
	
	If g\MaxShootSounds > 1 Then
		For i = 0 To g\MaxShootSounds-1
			g\ShootSounds[i] = LoadSound_Strict("SFX\Guns\"+g\name+"\shoot"+(i+1)+".ogg")
		Next
	Else
		g\ShootSounds[0] = LoadSound_Strict("SFX\Guns\"+g\name+"\shoot.ogg")
	EndIf
	If g\MaxSuppressedShootSounds > 1 Then
		For i = 0 To g\MaxSuppressedShootSounds-1
			g\SuppressedShootSounds[i] = LoadSound_Strict("SFX\Guns\"+g\name+"\suppressed_shoot"+(i+1)+".ogg")
		Next
	Else
		g\SuppressedShootSounds[0] = LoadSound_Strict("SFX\Guns\"+g\name+"\suppressed_shoot.ogg")
	EndIf
	
;! ~ [Weapon Initialising]
	
	g\MuzzleFlash = CreateSprite()	
	EntityFX g\MuzzleFlash,1
	SpriteViewMode g\MuzzleFlash,2
	EntityParent g\MuzzleFlash,g\obj
	HideEntity g\MuzzleFlash
	
	g\Reticle = CreateSprite()	
	EntityFX g\Reticle,1
	SpriteViewMode g\Reticle,2
	EntityParent g\Reticle,g\obj
	HideEntity g\Reticle
	
	g\PlayerModel = LoadAnimMesh_Strict("GFX\Weapons\Models\"+g\name+"_Worldmodel.b3d")
	HideEntity g\PlayerModel
	
	Local SimpleString$ = GetINIString(gv\WeaponFile,Name,"player_model_anim","")
	Select Lower(SimpleString)
		Case "smg"
			g\PlayerModelAnim = GUNANIM_SMG
		Case "rifle"
			g\PlayerModelAnim = GUNANIM_RIFLE
		Case "pistol"
			g\PlayerModelAnim = GUNANIM_PISTOL
		Case "mp5k"
			g\PlayerModelAnim = GUNANIM_MP5K
		Case "shotgun"
			g\PlayerModelAnim = GUNANIM_SHOTGUN
		Case "melee"
			g\PlayerModelAnim = GUNANIM_MELEE
		Default
			RuntimeError "ERROR: Weapon type " + SimpleString + " doesn't exist!"
	End Select
	
	SimpleString = GetINIString(gv\WeaponFile,Name,"decal_type","bullet")
	Select Lower(SimpleString)
		Case "bullet"
			g\DecalType = GUNDECAL_DEFAULT
		Case "slash"
			g\DecalType = GUNDECAL_SLASH
		Case "smash"
			g\DecalType = GUNDECAL_SMASH
		Default
			RuntimeError "ERROR: Weapon decal type " + SimpleString + " doesn't exist!"
	End Select
	
;! ~ [End]
	
	Return g
End Function

Function AddAttachment(g.Guns, Attachment)
	Local isMultiplayer% = (gopt\GameMode = GAMEMODE_MULTIPLAYER)
	
	;For g = Each Guns
		;If g_I\HoldingGun = g\ID Then
			Select Attachment
				Case ATT_SUPPRESSOR ; ~ Suppressor
					If FindChild(g\obj,"att_suppressor") <> 0 Then ScaleEntity FindChild(g\obj,"att_suppressor"),1,1,1
					If g\CanHaveAttachments[ATT_MATCH] Then
						If g\name = "usp" Then
							If FindChild(g\obj,"att_match") <> 0 Then ScaleEntity FindChild(g\obj,"att_match"),0,0,0
						Else
							If FindChild(g\obj,"att_compensator") <> 0 Then ScaleEntity FindChild(g\obj,"att_compensator"),0,0,0
						EndIf
					EndIf
				Case ATT_MATCH ; ~ Match\Compensator
					If FindChild(g\obj,"att_suppressor") <> 0 Then ScaleEntity FindChild(g\obj,"att_suppressor"),0,0,0
					If g\name = "usp" Then
						If FindChild(g\obj,"att_match") <> 0 Then ScaleEntity FindChild(g\obj,"att_match"),1,1,1
					Else
						If FindChild(g\obj,"att_compensator") <> 0 Then ScaleEntity FindChild(g\obj,"att_compensator"),1,1,1
					EndIf
				Case ATT_EXT_MAG ; ~ Extended Magazine
					If FindChild(g\obj,"att_mag_ext") <> 0 Then ScaleEntity FindChild(g\obj,"att_mag_ext"),1,1,1
					If FindChild(g\obj,"att_mag") <> 0 Then ScaleEntity FindChild(g\obj,"att_mag"),0,0,0
					If isMultiplayer Then
						Players[mp_I\PlayerID]\ReloadAmmo[Players[mp_I\PlayerID]\SelectedSlot] = Players[mp_I\PlayerID]\ReloadAmmo[Players[mp_I\PlayerID]\SelectedSlot] + Players[mp_I\PlayerID]\Ammo[Players[mp_I\PlayerID]\SelectedSlot]
						Players[mp_I\PlayerID]\Ammo[Players[mp_I\PlayerID]\SelectedSlot] = 0
					Else
						g\CurrReloadAmmo = g\CurrReloadAmmo + g\CurrAmmo
						g\CurrAmmo = 0
					EndIf
				Case ATT_ACOG_SCOPE ; ~ ACOG Scope
					If g\name <> "mp5" And g\name <> "mp7" And g\name <> "m870" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),0,0,0
					ElseIf g\name = "mp5" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),1,1,1
					EndIf
					If FindChild(g\obj,"att_acog_scope") <> 0 Then ScaleEntity FindChild(g\obj,"att_acog_scope"),1,1,1
					If g\name = "p90" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),1,1,1
						If FindChild(g\obj,"att_p90_std_scope") <> 0 Then ScaleEntity FindChild(g\obj,"att_p90_std_scope"),0,0,0
					EndIf
					ApplyScopeMaterial(True)
					EntityTexture g\Reticle, AimCrossIMG[4]
				Case ATT_SPECIAL_SCOPE ; ~ P90\EMRP Scope
					If g\name = "p90" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),0,0,0
						If FindChild(g\obj,"att_acog_scope") <> 0 Then ScaleEntity FindChild(g\obj,"att_acog_scope"),0,0,0
						If FindChild(g\obj,"att_p90_std_scope") <> 0 Then ScaleEntity FindChild(g\obj,"att_p90_std_scope"),1,1,1
						EntityTexture g\Reticle, AimCrossIMG[1]
					ElseIf g\name = "emrp" Then
						If FindChild(g\obj,"att_reddot") <> 0 Then ScaleEntity FindChild(g\obj,"att_reddot"),0,0,0
						If FindChild(g\obj,"att_eotech") <> 0 Then ScaleEntity FindChild(g\obj,"att_eotech"),0,0,0
						If FindChild(g\obj,"att_emrp_std_scope") <> 0 Then ScaleEntity FindChild(g\obj,"att_emrp_std_scope"),1,1,1
						EntityTexture g\Reticle, AimCrossIMG[5]
					EndIf
				Case ATT_EOTECH ; ~ EoTech Sight
					If g\name <> "mp5" And g\name <> "mp7" And g\name <> "m870" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),0,0,0
					ElseIf g\name = "mp5" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),1,1,1
					EndIf
					If FindChild(g\obj,"att_eotech") <> 0 Then ScaleEntity FindChild(g\obj,"att_eotech"),1,1,1
					If g\name = "p90" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),1,1,1
						If FindChild(g\obj,"att_p90_std_scope") <> 0 Then ScaleEntity FindChild(g\obj,"att_p90_std_scope"),0,0,0
					EndIf
					EntityTexture g\Reticle, AimCrossIMG[3]
				Case ATT_RED_DOT ; ~ Red Dot Sight
					If g\name <> "mp5" And g\name <> "mp7" And g\name <> "m870" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),0,0,0
					ElseIf g\name = "mp5" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),1,1,1
					EndIf
					If FindChild(g\obj,"att_reddot") <> 0 Then ScaleEntity FindChild(g\obj,"att_reddot"),1,1,1
					If g\name = "p90" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),1,1,1
						If FindChild(g\obj,"att_p90_std_scope") <> 0 ScaleEntity FindChild(g\obj,"att_p90_std_scope"),0,0,0
					EndIf
					EntityTexture g\Reticle, AimCrossIMG[2]
				Case ATT_MUI ; ~ Monitoring Unit
					
			End Select
			g\HasAttachments[Attachment] = True
		;EndIf
	;Next
	
End Function

Function RemoveAttachment(g.Guns,Attachment)
	Local isMultiplayer% = (gopt\GameMode = GAMEMODE_MULTIPLAYER)
	
	;For g = Each Guns
		;If g_I\HoldingGun = g\ID Then
			Select Attachment
				Case ATT_SUPPRESSOR ; ~ Suppressor
					If FindChild(g\obj,"att_suppressor") <> 0 Then ScaleEntity FindChild(g\obj,"att_suppressor"),0,0,0
				Case ATT_MATCH ; ~ Match\Compensator
					If g\name = "usp" Then
						If FindChild(g\obj,"att_match") <> 0 Then ScaleEntity FindChild(g\obj,"att_match"),0,0,0
					Else
						If FindChild(g\obj,"att_compensator") <> 0 Then ScaleEntity FindChild(g\obj,"att_compensator"),0,0,0
					EndIf
				Case ATT_EXT_MAG ; ~ Extended Magazine
					If FindChild(g\obj,"att_mag_ext") <> 0 Then ScaleEntity FindChild(g\obj,"att_mag_ext"),0,0,0
					If FindChild(g\obj,"att_mag") <> 0 Then ScaleEntity FindChild(g\obj,"att_mag"),1,1,1
					If isMultiplayer Then
						Players[mp_I\PlayerID]\ReloadAmmo[Players[mp_I\PlayerID]\SelectedSlot] = Players[mp_I\PlayerID]\ReloadAmmo[Players[mp_I\PlayerID]\SelectedSlot] + Players[mp_I\PlayerID]\Ammo[Players[mp_I\PlayerID]\SelectedSlot]
						Players[mp_I\PlayerID]\Ammo[Players[mp_I\PlayerID]\SelectedSlot] = 0
					Else
						g\CurrReloadAmmo = g\CurrReloadAmmo + g\CurrAmmo
						g\CurrAmmo = 0
					EndIf
				Case ATT_ACOG_SCOPE ; ~ ACOG Scope
					If g\name <> "mp5" And g\name <> "mp7" And g\name <> "m870" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),1,1,1
					ElseIf g\name = "mp5" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),0,0,0
					EndIf
					If FindChild(g\obj,"att_acog_scope") <> 0 Then ScaleEntity FindChild(g\obj,"att_acog_scope"),0,0,0
					;If g_I\HoldingGun = g\ID Then ReplaceTextureByMaterial("GFX\Weapons\Models\wpn_p90_dot_alt.png",g\obj,"wpn_p90_dot_alt",15) ; ~ Scope Material
				Case ATT_SPECIAL_SCOPE ; ~ P90\EMRP Scope
					If g\name = "p90" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),1,1,1
						If FindChild(g\obj,"att_p90_std_scope") <> 0 Then ScaleEntity FindChild(g\obj,"att_p90_std_scope"),0,0,0
					ElseIf g\name = "emrp" Then
						If FindChild(g\obj,"att_emrp_std_scope") <> 0 Then ScaleEntity FindChild(g\obj,"att_emrp_std_scope"),0,0,0
					EndIf
				Case ATT_EOTECH ; ~ EoTech Sight
					If g\name <> "mp5" And g\name <> "mp7" And g\name <> "m870" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),1,1,1
					ElseIf g\name = "mp5" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),0,0,0
					EndIf
					If FindChild(g\obj,"att_eotech") <> 0 Then ScaleEntity FindChild(g\obj,"att_eotech"),0,0,0
				Case ATT_RED_DOT ; ~ Red Dot Sight
					If g\name <> "mp5" And g\name <> "mp7" And g\name <> "m870" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),1,1,1
					ElseIf g\name = "mp5" Then
						If FindChild(g\obj,"att_rail") <> 0 Then ScaleEntity FindChild(g\obj,"att_rail"),0,0,0
					EndIf
					If FindChild(g\obj,"att_reddot") <> 0 Then ScaleEntity FindChild(g\obj,"att_reddot"),0,0,0
				Case ATT_MUI ; ~ Monitoring Unit
					
			End Select
			g\HasAttachments[Attachment] = False
		;EndIf
	;Next
	
End Function

Function InitGuns()
	Local g.Guns, gr.Grenades
	Local it.ItemTemplates
	Local f%,l$, i%
	Local gunID%
	
	g_I.GunInstance = New GunInstance
	
	g_I\GunAnimFLAG = False
	g_I\GunChangeFLAG = False
	
	UsingScope% = False
	
	ScopeNVG% = False
	
	CanPlayerUseGuns% = True
	
	NTF_InfiniteAmmo% = False
	NTF_NoReload% = False
	
	For i = 0 To MaxGunSlots-1
		g_I\Weapon_InSlot[i] = ""
	Next
	
	AimCrossIMG[0] = LoadImage_Strict("GFX\Aim_Cross.png") : MidHandle AimCrossIMG[0]
	AimCrossIMG[1] = LoadTexture_Strict("GFX\Weapons\models\wpn_p90_dot.png", 1 + 2, DeleteAllTextures)
	AimCrossIMG[2] = LoadTexture_Strict("GFX\Weapons\models\wpn_red_dot.png", 1 + 2, DeleteAllTextures)
	AimCrossIMG[3] = LoadTexture_Strict("GFX\Weapons\models\wpn_eot_dot.png", 1 + 2, DeleteAllTextures)
	AimCrossIMG[4] = LoadTexture_Strict("GFX\Weapons\models\wpn_acog_dot.png", 1 + 2, DeleteAllTextures)
	AimCrossIMG[5] = LoadTexture_Strict("GFX\Weapons\models\wpn_emrp_dot.png", 1 + 2, DeleteAllTextures)
	BulletIcon% = LoadImage_Strict("GFX\bullet_icon.png")
	
	g_I\GunPivot = CreatePivot()
	
	GunPickPivot = CreatePivot()
	EntityParent GunPickPivot,g_I\GunPivot
	
	ScopeZoom# = 25.0
	
	ScopeTexture = CreateTextureUsingCacheSystem(128,128,1)
	ScopeCam = CreateCamera(g_I\GunPivot)
	MoveEntity ScopeCam,0,0,0.15
	CameraZoom ScopeCam,ScopeZoom#
	CameraViewport ScopeCam,0,0,128,128
	CameraRange ScopeCam,0.005,16
	CameraFogMode ScopeCam,1
	CameraFogRange (ScopeCam, CameraFogNear, CameraFogFar)
	CameraFogColor (ScopeCam,255,255,255)
	HideEntity ScopeCam
	
;! ~ [Creating Weapons]
	
	;! ~ [MELEE]
	
	; ~ Knife
	g.Guns = CreateGun(GetLocalString("Item Names","knife"),"knife",GUN_KNIFE,"Knife_Viewmodel.b3d",False,"melee","melee",0,0,20,1,0,35,1,0.85,15,70,280)
	; ~ Crowbar
	g.Guns = CreateGun(GetLocalString("Item Names","crowbar"),"crowbar",GUN_CROWBAR,"Crowbar_Viewmodel.b3d",False,"melee","melee",0,0,35,1,0,80,1,1,10,85,320)
	
	;! ~ [HANDGUNS]
	
	; ~ M9 Beretta
	g.Guns = CreateGun("M9 Beretta","beretta",GUN_BERETTA,"Beretta_Viewmodel.b3d",True,"handgun","secondary",15,105,17,1,1.5,7,1,0,0,70,140,265,245,0,0,True,230,120,0.03,True,"Pistol_hands.b3d",0,0,0,0,10,5)
	If g\name = "beretta" Then
		g\CanHaveAttachments[ATT_SUPPRESSOR] = True
	EndIf
	; ~ H&K USP
	g.Guns = CreateGun("H&K USP","usp",GUN_USP,"USP_Viewmodel.b3d",True,"handgun","secondary",12,84,20,1,4,8,1,0,0,70,140,265,245,0,0,True,230,73,0.03,True,"Pistol_hands.b3d",0,0,0,0,10,5)
	If g\name = "usp" Then
		g\CanHaveAttachments[ATT_SUPPRESSOR] = True
		g\CanHaveAttachments[ATT_MATCH] = True
	EndIf
	; ~ Tokarev TT-33
	If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
		g.Guns = CreateGun("Tokarev TT-33","tt33",GUN_TT33,"TT33_Viewmodel.b3d",True,"handgun","secondary",8,80,27,1,1.5,7,1,0,0,70,140,265,245,0,0,True,230,70,0.03,False,"Pistol_hands.b3d",0,0,0,0,10,5)
	Else
		g.Guns = CreateGun("Wolf's Tokarev TT-33","tt33",GUN_TT33,"TT33W_Viewmodel.b3d",True,"handgun","secondary",9,90,32,1,1.2,6.7,1,0,0,70,140,265,245,0,0,True,230,70,0.03,False,"Pistol_hands.b3d",0,0,0,0,10,5)
	EndIf
	; ~ FN Five-Seven
	g.Guns = CreateGun("FN Five-Seven","fiveseven",GUN_FIVESEVEN,"FiveSeven_Viewmodel.b3d",True,"handgun","secondary",20,140,11,1,1.5,6,1,0,0,70,140,265,245,0,0,True,230,150,0.03,True,"Pistol_hands.b3d",0,0,0,0,10,5)
	If g\name = "fiveseven" Then
		g\CanHaveAttachments[ATT_SUPPRESSOR] = True
	EndIf
	; ~ Glock 20-C
	g.Guns = CreateGun("Glock 20-C","glock",GUN_GLOCK,"Glock_Viewmodel.b3d",True,"smg","secondary",15,105,22,1,3.5,3,1,0,0,70,140,265,245,0,0,True,230,80,0.03,True,"Pistol_hands.b3d",0,0,0,0,10,5)
	If g\name = "glock" Then
		g\CanHaveAttachments[ATT_SUPPRESSOR] = True
	EndIf
	
	;! ~ [SMGs&PDWs]
	
	; ~ FN P90
	g.Guns = CreateGun("FN P90","p90",GUN_P90,"P90_Viewmodel.b3d",True,"smg","primary",50,350,10,2,0.4,4,1,0,0,70,120,280,240,0,0,True,560,220,0.04,True,"",0,0,0,70,0.5,1.2)
	If g\name = "p90" Then
		g\HasPickedAttachments[ATT_SPECIAL_SCOPE] = True
		
		g\CanHaveAttachments[ATT_SUPPRESSOR] = True
		g\CanHaveAttachments[ATT_ACOG_SCOPE] = True
		g\CanHaveAttachments[ATT_SPECIAL_SCOPE] = True
		g\CanHaveAttachments[ATT_RED_DOT] = True
		g\CanHaveAttachments[ATT_EOTECH] = True
	EndIf
	; ~ H&K MP5
	g.Guns = CreateGun("H&K MP5","mp5",GUN_MP5,"MP5_Viewmodel.b3d",True,"smg","primary",30,210,15,2,1,5.4,1,0,0,65,250,350,230,0,0,True,180,260,0.04,True,"",250,230,350,45,1.15,2.2)
	If g\name = "mp5" Then
		g\CanHaveAttachments[ATT_SUPPRESSOR] = True
		g\CanHaveAttachments[ATT_EXT_MAG] = True
		g\CanHaveAttachments[ATT_RED_DOT] = True
		g\CanHaveAttachments[ATT_EOTECH] = True
		g\CanHaveAttachments[ATT_ACOG_SCOPE] = True
	EndIf
	; ~ H&K MP7
	g.Guns = CreateGun("H&K MP7","mp7",GUN_MP7,"MP7_Viewmodel.b3d",True,"smg","primary",40,280,12,1,0.8,3.7,1,0,0,65,250,330,250,0,0,True,180,170,0.04,True,"",0,0,0,45,1.15,2.2)
	If g\name = "mp7" Then
		g\CanHaveAttachments[ATT_SUPPRESSOR] = True
		g\CanHaveAttachments[ATT_RED_DOT] = True
		g\CanHaveAttachments[ATT_EOTECH] = True
		g\CanHaveAttachments[ATT_ACOG_SCOPE] = True
	EndIf
	
	;! ~ [ASSAULT RIFLES]
	
	; ~ RPK-16
	g.Guns = CreateGun("RPK-16","rpk16",GUN_RPK16,"RPK16_Viewmodel.b3d",True,"rifle","primary",30,210,23,1,1.7,4.95,1,0,0,50,480,450,330,0,0,True,350,450,0.07,True,"",480,430,540,60)
	If g\name = "rpk16" Then
		g\CanHaveAttachments[ATT_SUPPRESSOR] = True
		g\CanHaveAttachments[ATT_EXT_MAG] = True
		g\CanHaveAttachments[ATT_RED_DOT] = True
		g\CanHaveAttachments[ATT_EOTECH] = True
		g\CanHaveAttachments[ATT_ACOG_SCOPE] = True
	EndIf
	; ~ Colt M4A1
	g.Guns = CreateGun("Colt M4A1","m4a1",GUN_M4A1,"m4a1_Viewmodel.b3d",True,"rifle","primary",20,140,24,1,1.4,4.75,1,0,0,50,450,420,365,0,0,True,350,240,0.06,True,"",450,430,500,45)
	If g\name = "m4a1" Then
		g\CanHaveAttachments[ATT_SUPPRESSOR] = True
		g\CanHaveAttachments[ATT_EXT_MAG] = True
		g\CanHaveAttachments[ATT_RED_DOT] = True
		g\CanHaveAttachments[ATT_EOTECH] = True
		g\CanHaveAttachments[ATT_ACOG_SCOPE] = True
	EndIf
	
	;! ~ [SHOTGUNS]
	
	; ~ Remington M870
	g.Guns = CreateGun("Remington M870","m870",GUN_M870,"M870_Viewmodel.b3d",True,"shotgun","primary",8,88,24,5,7,60,6,0,0,60,260,90,0,80,430,True,180,20,0.005,True,"Shotgun_hands.b3d",0,0,0,0,1.15,2.2)
	If g\name = "m870" Then
		g\CanHaveAttachments[ATT_RED_DOT] = True
		g\CanHaveAttachments[ATT_EOTECH] = True
	EndIf
	; ~ Franchi SPAS-12
	g.Guns = CreateGun("Franchi SPAS-12","spas12",GUN_SPAS12,"SPAS12_Viewmodel.b3d",True,"shotgun","primary",6,78,24,5,7,60,6,0,0,60,260,90,0,80,430,True,180,30,0.006,False,"Shotgun_hands.b3d")
	
	;! ~ [OTHER]
	
	; ~ SCP-127
	g.Guns = CreateGun("SCP-127","scp127",GUN_SCP127,"SCP127_Viewmodel.b3d",True,"smg","primary",60,0,6,2,0.3,5.4,1,0,0,65,65,0,0,0,0,True,0,0,0,False,"MP5_hands.b3d")
	
	; ~ M67 Grenade
	g.Guns = CreateGun(GetLocalString("Item Names", "grenade_m67"),"m67",GUN_M67,"m67_Viewmodel.b3d",False,"melee","melee",0,0,0,1,0,140,1,0.85,110,90,220,0,0,0,0,True,0,0,0,False,"Grenade_hands.b3d")
	; ~ RGD-5 Grenade
	g.Guns = CreateGun(GetLocalString("Item Names", "grenade_rgd5"),"rgd5",GUN_RGD5,"rgd5_Viewmodel.b3d",False,"melee","melee",0,0,0,1,0,140,1,0.85,110,90,220,0,0,0,0,True,0,0,0,False,"Grenade_hands.b3d")
	
	; ~ Electro-Magnetic Rifle - Prototype
	g.Guns = CreateGun(GetLocalString("Item Names","emrp"),"emrp",GUN_EMRP,"EMRP_Viewmodel.b3d",True,"handgun","primary",10,70,1000,0.5,20,220,1,0,0,50,300,360,360,0,0,True,340,3,0.01,True,"",0,0,0,0,1.1,2.1)
	If g\name = "emrp" Then
		g\HasPickedAttachments[ATT_SPECIAL_SCOPE] = True
		
		g\CanHaveAttachments[ATT_SPECIAL_SCOPE] = True
		g\CanHaveAttachments[ATT_RED_DOT] = True
		g\CanHaveAttachments[ATT_EOTECH] = True
	EndIf
	
;! ~ [Creating Items]
	
	it = CreateItemTemplate(GetLocalString("Item Names","knife"),"knife","GFX\weapons\models\knife_worldmodel.b3d","GFX\weapons\icons\INVknife.jpg","",0.006) : it\sound = 66 : it\IsGun% = True : it\IsForScabbard = True
	it = CreateItemTemplate(GetLocalString("Item Names","crowbar"),"crowbar","GFX\weapons\models\crowbar_worldmodel.b3d","GFX\weapons\icons\INVcrowbar.jpg","",0.025) : it\sound = 66 : it\IsGun% = True
	it = CreateItemTemplate("M9 Beretta","beretta","GFX\weapons\models\beretta_worldmodel.b3d","GFX\weapons\icons\INVberetta.jpg","",0.02) : it\sound = 66 : it\IsGun% = True : it\IsForHolster = True
	it = CreateItemTemplate("H&K USP","usp","GFX\weapons\models\usp_worldmodel.b3d","GFX\weapons\icons\INVusp.jpg","",0.02) : it\sound = 66 : it\IsGun% = True : it\IsForHolster = True
	If gopt\GameMode = GAMEMODE_MULTIPLAYER Then
		it = CreateItemTemplate("Tokarev TT-33","tt33","GFX\weapons\models\tt33_worldmodel.b3d","GFX\weapons\icons\INVtt33.jpg","",0.02) : it\sound = 66 : it\IsGun% = True : it\IsForHolster = True
	Else
		it = CreateItemTemplate("Wolf's Tokarev TT-33","tt33","GFX\weapons\models\tt33w_worldmodel.b3d","GFX\weapons\icons\INVtt33w.jpg","",0.02) : it\sound = 66 : it\IsGun% = True : it\IsForHolster = True
	EndIf
	it = CreateItemTemplate("FN Five-Seven","fiveseven","GFX\weapons\models\FiveSeven_worldmodel.b3d","GFX\weapons\icons\INVfiveseven.jpg","",0.02) : it\sound = 66 : it\IsGun% = True : it\IsForHolster = True
	it = CreateItemTemplate("Glock 20-C","glock","GFX\weapons\models\glock_worldmodel.b3d","GFX\weapons\icons\INVglock.jpg","",0.02) : it\sound = 66 : it\IsGun% = True : it\IsForHolster = True
	it = CreateItemTemplate("FN P90","p90","GFX\weapons\models\P90_worldmodel.b3d","GFX\weapons\icons\INVp90.jpg","",0.02) : it\sound = 66 : it\IsGun% = True
	it = CreateItemTemplate("H&K MP5","mp5","GFX\weapons\models\mp5_worldmodel.b3d","GFX\weapons\icons\INVmp5.jpg","",0.0026) : it\sound = 66 : it\IsGun% = True
	it = CreateItemTemplate("H&K MP7","mp7","GFX\weapons\models\mp7_worldmodel.b3d","GFX\weapons\icons\INVmp7.jpg","",0.0026) : it\sound = 66 : it\IsGun% = True
	it = CreateItemTemplate("RPK-16","rpk16","GFX\weapons\models\RPK16_worldmodel.b3d","GFX\weapons\icons\INVrpk16.jpg","",0.0026) : it\sound = 66 : it\IsGun% = True
	it = CreateItemTemplate("Colt M4A1","m4a1","GFX\weapons\models\M4A1_worldmodel.b3d","GFX\weapons\icons\INVm4a1.jpg","",0.0026) : it\sound = 66 : it\IsGun% = True
	it = CreateItemTemplate("Remington M870","m870","GFX\weapons\models\M870_worldmodel.b3d","GFX\weapons\icons\INVm870.jpg","",0.006) : it\sound = 66 : it\IsGun% = True
	it = CreateItemTemplate("Franchi SPAS-12","spas12","GFX\weapons\models\spas12_worldmodel.b3d","GFX\weapons\icons\INVspas12.jpg","",0.006) : it\sound = 66 : it\IsGun% = True
	it = CreateItemTemplate(GetLocalString("Item Names","emrp"),"emrp","GFX\weapons\models\emrp_worldmodel.b3d","GFX\weapons\icons\INVemrp.jpg","",0.0026) : it\sound = 66 : it\IsGun% = True
	it = CreateItemTemplate(GetLocalString("Item Names", "grenade_m67"),"m67","GFX\weapons\models\m67_worldmodel.b3d","GFX\weapons\icons\INVm67.jpg","",0.012) : it\sound = 66 : it\IsGun% = True : it\IsForHolster = True
	it = CreateItemTemplate(GetLocalString("Item Names", "grenade_rgd5"),"rgd5","GFX\weapons\models\rgd5_worldmodel.b3d","GFX\weapons\icons\INVrgd5.jpg","",0.012) : it\sound = 66 : it\IsGun% = True : it\IsForHolster = True
	
;! ~ [Ammunition]
	
	it = CreateItemTemplate(GetLocalString("Item Names","ammo"), "ammocrate", "GFX\Weapons\Models\Ammo\ammo_crate.b3d", "GFX\Items\icons\Icon_ammo_crate.jpg", "", 0.01)
	it = CreateItemTemplate(GetLocalString("Item Names","ammo_big"), "bigammocrate", "GFX\Weapons\Models\Ammo\big_ammo_crate.b3d", "GFX\Items\icons\Icon_big_ammo_crate.jpg", "", 0.01)
	
;! ~ [End]
	
	g_I\UI_Select_SFX = LoadSound_Strict("SFX\HUD\GunSelect_Accept.ogg")
	g_I\UI_Deny_SFX = LoadSound_Strict("SFX\HUD\GunSelect_Deny.ogg")
	
	IsPlayerShooting% = False
	
	IronSightPivot% = CreatePivot(g_I\GunPivot)
	IronSightPivot2% = CreatePivot(g_I\GunPivot)
	g_I\IronSight% = False
	g_I\IronSightAnim% = False
	
	NVGOnSFX% = LoadSound_Strict("SFX\Interact\NVGOn.ogg")
	NVGOffSFX% = LoadSound_Strict("SFX\Interact\NVGOff.ogg")
	
	g_I\GunLight = CreateLight(2,g_I\GunPivot)
	LightRange g_I\GunLight,0.4
	HideEntity g_I\GunLight
	g_I\GunLightTimer = 0.0
	
	g_I\AttachSFX = LoadSound_Strict("SFX\Guns\attach_addon.ogg")
	g_I\DetachSFX = LoadSound_Strict("SFX\Guns\detach_addon.ogg")
	
	Local nat%
	
	For g = Each Guns
		For nat = 0 To MaxAttachments - 1
			If g\CanHaveAttachments[nat] Then RemoveAttachment(g,nat)
			If g\HasAttachments[nat] Then AddAttachment(g,nat)
		Next
	Next
	
End Function

Function DeleteGuns()
	Local g.Guns,i
	
	For g.Guns = Each Guns
		Delete g
	Next
	
	For i = 0 To 2
		FreeImage AimCrossIMG[i] : AimCrossIMG[i] = 0
	Next
	FreeImage BulletIcon% : BulletIcon% = 0
	
	If g_I\UI_Select_SFX <> 0 Then FreeSound_Strict(g_I\UI_Select_SFX) : g_I\UI_Select_SFX=0
	If g_I\UI_Deny_SFX <> 0 Then FreeSound_Strict(g_I\UI_Deny_SFX) : g_I\UI_Deny_SFX=0
	
	If NVGOnSFX <> 0 Then FreeSound_Strict(NVGOnSFX) : NVGOnSFX=0
	If NVGOffSFX <> 0 Then FreeSound_Strict(NVGOffSFX) : NVGOffSFX=0
	
End Function

Function UpdateGuns()
	Local isMultiplayer% = (gopt\GameMode = GAMEMODE_MULTIPLAYER)
	Local g.Guns,g2.Guns,p.Particles,n.NPCs,pl.Player,i%
	Local shooting% = False
	Local currGun.Guns
	Local gr.Grenades
	
	;! ~ [WEAPON SWAY]
	
	Local campitch#, camyaw#, playerAlive%
	If isMultiplayer Then
		campitch# = EntityPitch(mpl\CameraPivot)+180
		camyaw# = EntityYaw(mpl\CameraPivot)
		Players[mp_I\PlayerID]\PressMouse1=False
		playerAlive = Players[mp_I\PlayerID]\CurrHP > 0
	Else
		campitch# = EntityPitch(Camera)+180
		camyaw# = EntityYaw(Camera)
		playerAlive = psp\Health > 0
	EndIf
	
	;For g = Each Guns
	;	Local pitchdelay# = g\SightDelayPitch
	;	Local yawdelay# = g\SightDelayYaw
	;Next
	
	Local gpivotpitch#, gpivotyaw#, pitch#, yaw#
	
	gpivotpitch# = EntityPitch(g_I\GunPivot)+180
	gpivotyaw# = EntityYaw(g_I\GunPivot)
	
	If SelectedDifficulty\OtherFactors > NORMAL And (Not isMultiplayer) Lor isMultiplayer And mp_I\Gamemode\Difficulty = KETER Then
		If g_I\IronSight Then
			pitch# = Clamp(gpivotpitch, campitch-1, campitch+1)
			yaw = ClampAngle(gpivotyaw, camyaw, 3)
		Else
			pitch# = Clamp(gpivotpitch, campitch-2, campitch+2)
			yaw = ClampAngle(gpivotyaw, camyaw, 6)
		EndIf
	Else
		If g_I\IronSight Then
			If KeyDown(KEY_SPRINT) Then
				pitch = campitch
				yaw = camyaw
			Else
				pitch# = Clamp(CurveAngle(campitch, gpivotpitch, 2.1), campitch-5, campitch+5)
				yaw# = CurveAngle(camyaw, gpivotyaw, 2.1)
				yaw = ClampAngle(yaw, camyaw, 5)
			EndIf
		Else
			pitch# = Clamp(CurveAngle(campitch, gpivotpitch, 10.0), campitch-5, campitch+5)
			yaw# = CurveAngle(camyaw, gpivotyaw, 10.0)
			yaw = ClampAngle(yaw, camyaw, 5)
		EndIf
	EndIf
	
	RotateEntity g_I\GunPivot, pitch-180, yaw, 0
	
	;! ~ [END]
	
	g_I\GunAnimFLAG = True
	UsingScope% = False
	IsPlayerShooting% = False
	
	Local scopeMat$,scopematnumber
	scopeMat$ = "wpn_p90_dot_alt"
	scopematnumber = 15
	
	For g = Each Guns
		
		;! ~ Gunlight
		
		If g_I\HoldingGun = g\ID Then
			If g\ID = GUN_EMRP Then
				LightColor g_I\GunLight,55,55,235
			Else
				If g\ID <> GUN_SCP127 Then
					LightColor g_I\GunLight,235,55,0
				EndIf
			EndIf
		EndIf
		
		;! ~ Scope Logic
		
		If g\HasAttachments[ATT_ACOG_SCOPE] Then
			If g_I\HoldingGun = g\ID Then
					; ~ Scope Charge
				If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
					If ScopeNVG Then 
						g\ScopeCharge# = g\ScopeCharge# + FPSfactor
					EndIf
					If g\ScopeCharge# >= ScopeChargeTime# And ScopeNVG Then 
						ScopeNVG = False
						PlaySound_Strict NVGOffSFX
					EndIf
				EndIf
				UpdateAttachmentMaterial(g\obj,scopeMat$,scopematnumber,True) ; ~ TODO
			EndIf
		EndIf
		
	Next
	
	ShowEntity g_I\GunPivot
	
	If g_I\GunLightTimer > 0.0 And g_I\GunLightTimer < 2.5 Then
		g_I\GunLightTimer = g_I\GunLightTimer + FPSfactor
		ShowEntity g_I\GunLight
	Else
		g_I\GunLightTimer = 0.0
		HideEntity g_I\GunLight
	EndIf
	
	Local reloadCondition%
	Local prevFrame#
	Local j
	
	Local pHoldingGun%, pDeployState#, pReloadState#, pShootState#, pPressMouse1%, pPressMouse2%, pPressReload%, pAmmo%, pReloadAmmo%, pIsPlayerSprinting%, pIronSight%
	If isMultiplayer Then
		pHoldingGun = Players[mp_I\PlayerID]\WeaponInSlot[Players[mp_I\PlayerID]\SelectedSlot]
		pDeployState = Players[mp_I\PlayerID]\DeployState
		pReloadState = Players[mp_I\PlayerID]\ReloadState
		pShootState = Players[mp_I\PlayerID]\ShootState
		pPressMouse1 = Players[mp_I\PlayerID]\PressMouse1
		pPressReload = Players[mp_I\PlayerID]\PressReload
		If Players[mp_I\PlayerID]\SelectedSlot < (MaxSlots - SlotsWithNoAmmo) Then
			pAmmo = Players[mp_I\PlayerID]\Ammo[Players[mp_I\PlayerID]\SelectedSlot]
			pReloadAmmo = Players[mp_I\PlayerID]\ReloadAmmo[Players[mp_I\PlayerID]\SelectedSlot]
		EndIf
		pIsPlayerSprinting = Players[mp_I\PlayerID]\IsPlayerSprinting
		pIronSight = Players[mp_I\PlayerID]\IronSight
	Else
		For g = Each Guns
			If g\ID = g_I\HoldingGun Then
				currGun = g
				Exit
			EndIf
		Next
		pHoldingGun = g_I\HoldingGun
		pDeployState = psp\DeployState
		pReloadState = psp\ReloadState
		pShootState = psp\ShootState
		pPressMouse1 = False
		pPressMouse2 = False
		pPressReload = False
		If currGun <> Null Then
			pAmmo = currGun\CurrAmmo
			pReloadAmmo = currGun\CurrReloadAmmo
		EndIf
		pIsPlayerSprinting = IsPlayerSprinting
		pIronSight = g_I\IronSight
	EndIf
	Local shootCondition%
	If playerAlive Then
		For g = Each Guns
			HideEntity g\MuzzleFlash
			HideEntity g\Reticle
			
			If g_I\GunChangeFLAG = False Then
				For g2.Guns = Each Guns
					If g2\ID%<>pHoldingGun Then
						SetAnimTime g2\obj,0
						HideEntity g2\obj
					Else
						ShowEntity g2\obj
					EndIf
				Next
				DeselectIronSight()
				pDeployState = 0
				pReloadState = 0
				pShootState = 0
				pPressMouse1 = False
				pPressMouse2 = False
				pPressReload = False
				MouseHit1 = False
				MouseDown1 = False
				MouseHit(1)
				If g\GunType = GUNTYPE_MELEE Then
					MouseHit2 = False
					MouseDown2 = False
					MouseHit(2)
				EndIf
				g_I\HoldingGun = pHoldingGun
				g_I\GunChangeFLAG = True
				pIronSight = False
				If isMultiplayer Then
					mp_I\LocalAmmo = pAmmo
				EndIf
			EndIf
			
			If g\Found Then
				g\Deploy_Time = g\Stored_Deploy_Time
			Else
				If (Not g\HasAttachments[ATT_EXT_MAG]) Then
					g\Deploy_Time = g\Ready_Time
				Else
					g\Deploy_Time = g\Ready_Time_Ext_Mag
				EndIf
			EndIf
			
			If g\HasAttachments[ATT_EXT_MAG] Then
				g\MaxCurrAmmo = g\MaxCurrAmmo_Ext_Mag
				;g\MaxReloadAmmo = g\MaxReloadAmmo_Ext_Mag
				
				g\Reload_Empty_Time = g\Reload_Empty_Time_Ext_Mag
				g\Reload_Time = g\Reload_Time_Ext_Mag
			Else
				g\MaxCurrAmmo = g\Stored_MaxCurrAmmo
				;g\MaxReloadAmmo = g\Stored_MaxReloadAmmo
				
				g\Reload_Empty_Time = g\Stored_Reload_Empty_Time
				g\Reload_Time = g\Stored_Reload_Time
			EndIf
			
			If g\GunType = GUNTYPE_SHOTGUN And g_I\HoldingGun = GUN_SPAS12 Then
				If g\FireMode = 0 Then
					g\Rate_Of_Fire = 30
				Else
					g\Rate_Of_Fire = g\Stored_Rate_Of_Fire
				EndIf
				
;				If pAmmo = 0 Then
;					g\Reload_Start_Time = g\Reload_Start_Empty_Time
;				Else
;					g\Reload_Start_Time = g\Stored_Reload_Start_Time
;				EndIf
			EndIf
			
			prevFrame# = AnimTime(g\obj)
			
			If g\HasAttachments[ATT_SUPPRESSOR] Then
				g\Accuracy = g\Accuracy/2
			Else
				g\Accuracy = g\StoredAccuracy
			EndIf
			
			If g\ID = pHoldingGun Then
				
				Local muzzlebone$
				
				If g\HasAttachments[ATT_SUPPRESSOR] Then
					muzzlebone$ = "weapon_muzzle_suppressor"
				Else
					muzzlebone$ = "weapon_muzzle"
				EndIf
				
				Local RetStrTemp$
				
				If g\HasAttachments[ATT_RED_DOT] Then
					RetStrTemp = "att_reddot_reticle"
				ElseIf g\HasAttachments[ATT_EOTECH] Then
					RetStrTemp = "att_eotech_reticle"
				ElseIf g\HasAttachments[ATT_ACOG_SCOPE] Then
					RetStrTemp = "att_acog_scope_reticle"
				ElseIf g\HasAttachments[ATT_SPECIAL_SCOPE] Then
					If g\name = "p90" Then
						RetStrTemp = "att_p90_std_scope_reticle"
					ElseIf g\name = "emrp"
						RetStrTemp = "att_emrp_std_scope_reticle"
					EndIf
				Else
					RetStrTemp = ""
				EndIf
				
				If RetStrTemp <> "" Then
					If ReadyToShowDot Then
						ShowEntity g\Reticle
					Else
						HideEntity g\Reticle
					EndIf
				Else
					HideEntity g\Reticle
				EndIf
				
				If g\GunType <> GUNTYPE_MELEE Then
					PositionEntity(g\MuzzleFlash, EntityX(FindChild(g\obj,muzzlebone$),True), EntityY(FindChild(g\obj,muzzlebone$),True), EntityZ(FindChild(g\obj,muzzlebone$),True),True)
					PositionEntity(g\Reticle, EntityX(FindChild(g\obj, RetStrTemp), True), EntityY(FindChild(g\obj, RetStrTemp), True), EntityZ(FindChild(g\obj, RetStrTemp), True), True)
					If g\HasAttachments[ATT_ACOG_SCOPE] Then
						ScaleSprite g\Reticle,0.0025,0.0025
					Else
						If g\HasAttachments[ATT_SPECIAL_SCOPE] And g\name = "p90" Then
							ScaleSprite g\Reticle,Rnd(0.0009,0.000925),Rnd(0.0009,0.000925)
						Else
							ScaleSprite g\Reticle,Rnd(0.0005,0.000525),Rnd(0.0005,0.000525)
						EndIf
					EndIf
				EndIf
				
				shootCondition = ((Not MenuOpen) And (Not AttachmentMenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor ((Not InLobby()) And (Not mp_I\ChatOpen) And (Not mp_I\Gamemode\DisableMovement) And (Not IsInVote()))) And (Not IsPlayerListOpen()) And (Not IsModerationOpen()))
				
				;! ~ [SPECIAL EFFECTS]
				
				; ~ Bullet Tracer for EMRP
				
				;If g\ID = GUN_EMRP Then
					;If shooting Then
						;p.Particles = CreateParticle(EntityX(FindChild(g\obj,"bullet_tracer"),True),EntityY(FindChild(g\obj,"bullet_tracer"),True),EntityZ(FindChild(g\obj,"bullet_tracer"),True),6,0.01,0.0,200)
					;EndIf
				;EndIf
				
				; ~ [END]
				
				Select g\GunType
					Case GUNTYPE_HANDGUN, GUNTYPE_SMG, GUNTYPE_RIFLE
						;[Block]
						
						; ~ [FIREMODES]
						
						If KeyHit(KEY_FIREMODE) And (Not MenuOpen) And (Not AttachmentMenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen) And (Not IsInVote())) And (Not IsModerationOpen()) And (g\JamAmount < g\MaxJams Lor g\MaxJams = 0) Then
							If pReloadState = 0.0 Then
								g\FireMode = g\FireMode + 1
								If g\GunType <> GUNTYPE_HANDGUN Then
									If g\FireMode = 0 Then
										PlayGunSound("change_fire_auto",1,1,False)
									ElseIf g\FireMode = 1 Then
										PlayGunSound("change_fire_semi",1,1,False)
									Else
										PlayGunSound(g\name+"\deploy",1,1,False)
									EndIf
								Else
									PlayGunSound(g\name+"\deploy",1,1,False)
								EndIf
							EndIf
						EndIf
						
						Local pMaxFireMode
						
						If g\GunType = GUNTYPE_HANDGUN Then
							pMaxFireMode = 1
						Else
							pMaxFireMode = 2
						EndIf
						
						If g\FireMode > pMaxFireMode Then
							If g\GunType <> GUNTYPE_HANDGUN Then
								PlayGunSound("change_fire_auto",1,1,False)
							EndIf
							PlayGunSound(g\name+"\deploy",1,1,False)
							g\FireMode = 0
						EndIf
						
						Select g\FireMode
							Case pMaxFireMode - 2
								OnSafety = False
								If pAmmo=0 Then
									If MouseHit1 And shootCondition Then
										pPressMouse1=True
										pPressReload=False
									EndIf
								Else
									If MouseDown1 And shootCondition Then
										pPressMouse1=True
										pPressReload=False
									EndIf
								EndIf
							Case pMaxFireMode - 1
								OnSafety = False
								If MouseHit1 And shootCondition Then
									pPressMouse1=True
									pPressReload=False
								EndIf
							Case pMaxFireMode
								OnSafety = True
						End Select
						
						; ~ [END]
						
						If g\IsSeparate Then
							If g_I\HoldingGun = g\ID Then
								If EntityHidden(g\HandsObj) Then ShowEntity g\HandsObj
							EndIf
						EndIf
						
						If (pAmmo = 0 And pShootState > 0.0) Lor pReloadState > 0.0 Lor pIsPlayerSprinting Lor (g\JamAmount >= g\MaxJams And g\MaxJams <> 0) Then
							pPressMouse1=False
						EndIf
						
						If pShootState = 0.0 And pPressMouse1 And pAmmo > 0 And pDeployState >= g\Deploy_Time And (g\JamAmount < g\MaxJams And g\MaxJams <> 0) Then
							If g\CanSelectMenuAttachments And AttachmentMenuOpen Then
								If pAmmo > 0 Then
									SetAnimTime(g\obj,g\Frame_Attachment)
								Else
									SetAnimTime(g\obj,g\Frame_NoAmmo_Attachment)
								EndIf
							Else
								If (Not OnSafety) Then
									If pAmmo = 0 Then
										SetAnimTime(g\obj,g\Frame_NoAmmo_Idle)
									Else
										SetAnimTime(g\obj,g\Frame_Idle)
									EndIf
								Else
									If pAmmo > 0 Then
										SetAnimTime(g\obj,g\Frame_Safety)
									Else
										SetAnimTime(g\obj,g\Frame_NoAmmo_Safety)
									EndIf
								EndIf
							EndIf
						EndIf
						
						If pDeployState < g\Deploy_Time Lor pAmmo > g\MaxCurrAmmo Lor pReloadState = 0.0 Then
							pPressReload=False
						EndIf
						If pReloadAmmo = 0 Then
							pPressReload=False
						EndIf
						
						If KeyHit(KEY_RELOAD) And (Not MenuOpen) And (Not AttachmentMenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen) And (Not IsInVote())) And (Not IsModerationOpen()) Then
							If pReloadState = 0.0 And pAmmo =< g\MaxCurrAmmo And pReloadAmmo > 0 Then
								pPressReload=True
								DeselectIronSight()
							EndIf
						EndIf
						
						shooting = False
						
						If g\Found Then
							If pDeployState < g\Deploy_Time Then
								DeselectIronSight()
								pIronSight = False
								pPressReload = False
								pPressMouse1 = False
								If pAmmo = 0 Then
									ChangeGunFrames(g,g\Anim_NoAmmo_Deploy,False)
									If prevFrame# < (g\Anim_NoAmmo_Deploy\x+1) And AnimTime(g\obj) >= (g\Anim_NoAmmo_Deploy\x+1) Then
										PlayGunSound(g\name+"\deploy",1,1,False)
									EndIf
								Else
									ChangeGunFrames(g,g\Anim_Deploy,False)
									If prevFrame# < (g\Anim_Deploy\x+1) And AnimTime(g\obj) >= (g\Anim_Deploy\x+1) Then
										PlayGunSound(g\name+"\deploy",1,1,False)
									EndIf
								EndIf
							Else
								reloadCondition = True
							EndIf
						Else
							If (Not g\HasAttachments[ATT_EXT_MAG]) Then
								If pDeployState < g\Deploy_Time Then
									DeselectIronSight()
									pIronSight = False
									pPressReload = False
									pPressMouse1 = False
									ChangeGunFrames(g,g\Anim_Ready,False)
									If prevFrame# < (g\Anim_Ready\x+1) And AnimTime(g\obj) >= (g\Anim_Ready\x+1) Then
										If g\ID = GUN_EMRP Then
											If pAmmo > 0 Then
												PlayGunSound(g\name+"\ready",1,1,False)
											Else
												PlayGunSound(g\name+"\ready_empty",1,1,False)
											EndIf
										Else
											PlayGunSound(g\name+"\ready",1,1,False)
										EndIf
									EndIf
								Else
									reloadCondition = True
									g\Found = True
								EndIf
							Else
								If pDeployState < g\Deploy_Time Then
									DeselectIronSight()
									pIronSight = False
									pPressReload = False
									pPressMouse1 = False
									ChangeGunFrames(g,g\Anim_Ready_Ext_Mag,False)
									If prevFrame# < (g\Anim_Ready_Ext_Mag\x+1) And AnimTime(g\obj) >= (g\Anim_Ready_Ext_Mag\x+1) Then
										PlayGunSound(g\name+"\ready_ext_mag",1,1,False)
									EndIf
								Else
									reloadCondition = True
									g\Found = True
								EndIf
							EndIf
						EndIf
						
						If reloadCondition = True Then
							
							; ~ Jamming mechanic
							
							If g\JamAmount => g\MaxJams And g\MaxJams <> 0 Then
								DeselectIronSight()
								pIronSight = False
								pPressReload = False
								pPressMouse1 = False
								
								p.Particles = CreateParticle(EntityX(FindChild(g\obj,muzzlebone$),True),EntityY(FindChild(g\obj,muzzlebone$),True),EntityZ(FindChild(g\obj,muzzlebone$),True),6,0.003,-0.005,150)
								
								If g\JamTimer < g\JamState Then
									g\JamTimer = g\JamTimer + FPSfactor
								Else
									g\JamAmount = Max(g\JamAmount - 1,0)
								EndIf
								If g\JamTimer < g\JamState Then
									If AnimTime(g\obj)>g\Anim_Jam\y Lor AnimTime(g\obj)<g\Anim_Jam\x Then
										SetAnimTime(g\obj,g\Anim_Jam\x)
									EndIf
									ChangeGunFrames(g,g\Anim_Jam,False)
									If prevFrame# < (g\Anim_Jam\x+1) And AnimTime(g\obj) >= (g\Anim_Jam\x+1) Then
										PlayGunSound(g\name+"\jam",1,1,False)
									ElseIf prevFrame# < (g\Anim_Jam\y-0.5) And AnimTime(g\obj) >= (g\Anim_Jam\y-0.5) Then
										pPressReload = False
									EndIf
								EndIf
							Else
								If g\JamAmount > 0 And g\JamTimer > 0 Then
									g\JamAmount = Max(g\JamAmount - (g\MaxJams/7),0)
								EndIf
								g\JamTimer = 0
							EndIf
							
							; ~ End
							
							If g\JamAmount < g\MaxJams Lor g\MaxJams = 0 Then
								If pReloadState = 0.0 Then
									If pShootState = 0.0 Then
										If AnimTime(g\obj) > g\Anim_Shoot\x And AnimTime(g\obj) < g\Anim_Shoot\y-0.5 Then
											ChangeGunFrames(g,g\Anim_Shoot,False)
										ElseIf AnimTime(g\obj) > g\Anim_NoAmmo_Shoot\x And AnimTime(g\obj) < g\Anim_NoAmmo_Shoot\y-0.5 Then
											ChangeGunFrames(g,g\Anim_NoAmmo_Shoot,False)
										Else
											If (Not AttachmentMenuOpen Lor OnSafety) Then
												If pIsPlayerSprinting Then
													pPressMouse1 = False
													pPressReload = False
													If pAmmo = 0 Then
														If AnimTime(g\obj)<=(g\Anim_NoAmmo_Sprint_Transition\y-0.5) Lor AnimTime(g\obj)>(g\Anim_NoAmmo_Sprint_Cycle\y) Then
															ChangeGunFrames(g,g\Anim_NoAmmo_Sprint_Transition,False)
														Else
															ChangeGunFrames(g,g\Anim_NoAmmo_Sprint_Cycle,True)
														EndIf
													Else
														If AnimTime(g\obj)<=(g\Anim_Sprint_Transition\y-0.5) Lor AnimTime(g\obj)>(g\Anim_Sprint_Cycle\y) Then
															ChangeGunFrames(g,g\Anim_Sprint_Transition,False)
														Else
															ChangeGunFrames(g,g\Anim_Sprint_Cycle,True)
														EndIf
													EndIf
												Else
													If pAmmo = 0 Then
														If AnimTime(g\obj)>(g\Anim_NoAmmo_Sprint_Transition\x+0.5) And AnimTime(g\obj)<=g\Anim_NoAmmo_Sprint_Cycle\y Then
															ChangeGunFrames(g,g\Anim_NoAmmo_Sprint_Transition,False,True)
														Else
															SetAnimTime(g\obj,g\Frame_NoAmmo_Idle)
														EndIf
													Else
														If AnimTime(g\obj)>(g\Anim_Sprint_Transition\x+0.5) And AnimTime(g\obj)<=g\Anim_Sprint_Cycle\y Then
															ChangeGunFrames(g,g\Anim_Sprint_Transition,False,True)
														Else
															SetAnimTime(g\obj,g\Frame_Idle)
														EndIf
													EndIf
												EndIf
											ElseIf AttachmentMenuOpen Then
												If pAmmo > 0 Then
													SetAnimTime(g\obj,g\Frame_Attachment)
												Else
													SetAnimTime(g\obj,g\Frame_NoAmmo_Attachment)
												EndIf
											Else
;												If pIsPlayerSprinting Then
;													pPressMouse1 = False
;													pPressReload = False
;													ChangeGunFrames(g,g\Anim_NoAmmo_Sprint_Cycle,True)
;												EndIf
												
												If pAmmo > 0 Then
													SetAnimTime(g\obj,g\Frame_Safety)
												Else
													SetAnimTime(g\obj,g\Frame_NoAmmo_Safety)
												EndIf
											EndIf
										EndIf
										
										If pPressMouse1 And pAmmo = 0 Then
											PlayGunSound(g\name+"\shoot_empty",1,1,False)
										EndIf
									Else
										If pAmmo = 0 Then
											ChangeGunFrames(g,g\Anim_NoAmmo_Shoot,False)
											If Ceil(AnimTime(g\obj)) = g\Anim_NoAmmo_Shoot\x Then
												If g\GunType = GUNTYPE_HANDGUN Then
													If g\ID = GUN_EMRP Then
														PlayGunSound(g\name+"\shoot_last",1,1,False)
													Else
														PlayGunSound(g\name+"\slide_back",1,1,False)
													EndIf
												EndIf
												If CameraShake <= (g\Knockback/2)-FPSfactor-0.05 Then
													If (Not g\HasAttachments[ATT_SUPPRESSOR]) Then
														If (Not g\HasAttachments[ATT_MATCH]) Then
															If g\ID <> GUN_EMRP Then
																PlayGunSound(g\name,g\MaxShootSounds,0,True)
															EndIf
														Else
															PlayGunSound(g\name+"\match_shoot",1,1,False)
														EndIf
													Else
														PlayGunSound(g\name,g\MaxSuppressedShootSounds,0,True)
													EndIf
													
													If g\HasAttachments[ATT_MATCH] Then
														CameraShake = g\Knockback/2.5
														user_camera_pitch = user_camera_pitch - g\Knockback/1.5
													Else
														CameraShake = g\Knockback/2.0
														user_camera_pitch = user_camera_pitch - g\Knockback
													EndIf
													
													; ~ Dymanic gun spread
													
													If (Not g_I\IronSight) Then
														TurnEntity g_I\GunPivot,CurveValue(Rand(-(g\Knockback+g\Knockback)/2,(g\Knockback+g\Knockback)*2),Rand((g\Knockback+g\Knockback)*2,-(g\Knockback+g\Knockback)/2),5.0),CurveValue(Rand(-(g\Knockback+g\Knockback)/2,(g\Knockback+g\Knockback)*2),Rand((g\Knockback+g\Knockback)*2,-(g\Knockback+g\Knockback)/2),5.0),CurveValue(Rand(-(g\Knockback+g\Knockback)/2,(g\Knockback+g\Knockback)*2),Rand((g\Knockback+g\Knockback)*2,-(g\Knockback+g\Knockback)/2),5.0)
													EndIf
													
													; ~ End
													
													If (Not g\HasAttachments[ATT_SUPPRESSOR]) Then
														If g\ID <> GUN_SCP127 Then
															g_I\GunLightTimer = FPSfactor
														EndIf
														If g\GunType = GUNTYPE_RIFLE Then
															EntityTexture g\MuzzleFlash, ParticleTextures[3],(Rand(0,3))
														Else
															If g\ID = GUN_EMRP Then
																EntityTexture g\MuzzleFlash, ParticleTextures[14],(Rand(0,3))
															ElseIf g\ID = GUN_SCP127 Then
																EntityTexture g\MuzzleFlash, ParticleTextures[5]
															Else
																EntityTexture g\MuzzleFlash, ParticleTextures[15],(Rand(0,3))
															EndIf
														EndIf
													ElseIf g\HasAttachments[ATT_SUPPRESSOR] Then
														EntityTexture g\MuzzleFlash, ParticleTextures[4],(Rand(0,3))
													EndIf
													ShowEntity g\MuzzleFlash
													TurnEntity g\MuzzleFlash,0,0,Rnd(360)
													ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
												EndIf
												
												shooting = True
												
											EndIf
										Else
											ChangeGunFrames(g,g\Anim_Shoot,False)
											If Ceil(AnimTime(g\obj)) = g\Anim_Shoot\x Then
												If (Not g\HasAttachments[ATT_SUPPRESSOR]) Then
													If (Not g\HasAttachments[ATT_MATCH]) Then
														PlayGunSound(g\name,g\MaxShootSounds,0,True)
													Else
														PlayGunSound(g\name+"\match_shoot",1,1,False)
													EndIf
												Else
													PlayGunSound(g\name,g\MaxSuppressedShootSounds,0,True)
												EndIf
												
												If g\HasAttachments[ATT_MATCH] Then
													CameraShake = g\Knockback/2.5
													user_camera_pitch = user_camera_pitch - g\Knockback/1.5
												Else
													CameraShake = g\Knockback/2.0
													user_camera_pitch = user_camera_pitch - g\Knockback
												EndIf
												
												; ~ Dynamic gun spread
												
												If (Not g_I\IronSight) Then
													TurnEntity g_I\GunPivot,CurveValue(Rand(-(g\Knockback+g\Knockback)/2,(g\Knockback+g\Knockback)*2),Rand((g\Knockback+g\Knockback)*2,-(g\Knockback+g\Knockback)/2),5.0),CurveValue(Rand(-(g\Knockback+g\Knockback)/2,(g\Knockback+g\Knockback)*2),Rand((g\Knockback+g\Knockback)*2,-(g\Knockback+g\Knockback)/2),5.0),CurveValue(Rand(-(g\Knockback+g\Knockback)/2,(g\Knockback+g\Knockback)*2),Rand((g\Knockback+g\Knockback)*2,-(g\Knockback+g\Knockback)/2),5.0)
												EndIf
												
												; ~ End
												
												shooting = True
												
												If (Not g\HasAttachments[ATT_SUPPRESSOR]) Then
													If g\ID <> GUN_SCP127 Then
														g_I\GunLightTimer = FPSfactor
													EndIf
													If g\GunType = GUNTYPE_RIFLE Then
														EntityTexture g\MuzzleFlash, ParticleTextures[3],(Rand(0,3))
													Else
														If g\ID = GUN_EMRP Then
															EntityTexture g\MuzzleFlash, ParticleTextures[14],(Rand(0,3))
														ElseIf g\ID = GUN_SCP127 Then
															EntityTexture g\MuzzleFlash, ParticleTextures[5]
														Else
															EntityTexture g\MuzzleFlash, ParticleTextures[15],(Rand(0,3))
														EndIf
													EndIf
												ElseIf g\HasAttachments[ATT_SUPPRESSOR] Then
													EntityTexture g\MuzzleFlash, ParticleTextures[4],(Rand(0,3))
												EndIf
												ShowEntity g\MuzzleFlash
												TurnEntity g\MuzzleFlash,0,0,Rnd(360)
												ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
											EndIf
											If pShootState >= g\Rate_Of_Fire-FPSfactor And pPressMouse1 Then
												SetAnimTime g\obj,g\Anim_Shoot\x
											EndIf
										EndIf
									EndIf
								Else
									If g\ID <> GUN_SCP127 Then
										If pAmmo = 0 Then
											If (Not g\HasAttachments[ATT_EXT_MAG]) Then
												If AnimTime(g\obj)>g\Anim_Reload_Empty\y Lor AnimTime(g\obj)<g\Anim_Reload_Empty\x Then
													SetAnimTime(g\obj,g\Anim_Reload_Empty\x)
												EndIf
												ChangeGunFrames(g,g\Anim_Reload_Empty,False)
												If prevFrame# < (g\Anim_Reload_Empty\x+1) And AnimTime(g\obj) >= (g\Anim_Reload_Empty\x+1) Then
													PlayGunSound(g\name+"\reload_empty",g\MaxReloadSounds,1,False)
												ElseIf prevFrame# < (g\Anim_Reload_Empty\y-0.5) And AnimTime(g\obj) >= (g\Anim_Reload_Empty\y-0.5) Then
													pPressReload = False
												EndIf
											Else
												If AnimTime(g\obj)>g\Anim_Reload_Empty_Ext_Mag\y Lor AnimTime(g\obj)<g\Anim_Reload_Empty_Ext_Mag\x Then
													SetAnimTime(g\obj,g\Anim_Reload_Empty_Ext_Mag\x)
												EndIf
												ChangeGunFrames(g,g\Anim_Reload_Empty_Ext_Mag,False)
												If prevFrame# < (g\Anim_Reload_Empty_Ext_Mag\x+1) And AnimTime(g\obj) >= (g\Anim_Reload_Empty_Ext_Mag\x+1) Then
													PlayGunSound(g\name+"\reload_empty_ext_mag",g\MaxReloadSounds,1,False)
												ElseIf prevFrame# < (g\Anim_Reload_Empty_Ext_Mag\y-0.5) And AnimTime(g\obj) >= (g\Anim_Reload_Empty_Ext_Mag\y-0.5) Then
													pPressReload = False
												EndIf
											EndIf
										Else
											If (Not g\HasAttachments[ATT_EXT_MAG]) Then
												If AnimTime(g\obj)>g\Anim_Reload\y Lor AnimTime(g\obj)<g\Anim_Reload\x Then
													SetAnimTime(g\obj,g\Anim_Reload\x)
												EndIf
												ChangeGunFrames(g,g\Anim_Reload,False)
												If prevFrame# < (g\Anim_Reload\x+1) And AnimTime(g\obj) >= (g\Anim_Reload\x+1) Then
													PlayGunSound(g\name+"\reload",g\MaxReloadSounds,1,False)
												ElseIf prevFrame# < (g\Anim_Reload\y-0.5) And AnimTime(g\obj) >= (g\Anim_Reload\y-0.5) Then
													pPressReload = False
												EndIf
											Else
												If AnimTime(g\obj)>g\Anim_Reload_Ext_Mag\y Lor AnimTime(g\obj)<g\Anim_Reload_Ext_Mag\x Then
													SetAnimTime(g\obj,g\Anim_Reload_Ext_Mag\x)
												EndIf
												ChangeGunFrames(g,g\Anim_Reload_Ext_Mag,False)
												If prevFrame# < (g\Anim_Reload_Ext_Mag\x+1) And AnimTime(g\obj) >= (g\Anim_Reload_Ext_Mag\x+1) Then
													PlayGunSound(g\name+"\reload_ext_mag",g\MaxReloadSounds,1,False)
												ElseIf prevFrame# < (g\Anim_Reload_Ext_Mag\y-0.5) And AnimTime(g\obj) >= (g\Anim_Reload_Ext_Mag\y-0.5) Then
													pPressReload = False
												EndIf
											EndIf
										EndIf
									EndIf
									pIronSight = False
								EndIf
							EndIf
						EndIf
						
						If g\IsSeparate Then
							SetAnimTime(g\HandsObj,AnimTime(g\obj))
						EndIf
						
						If (Not pIsPlayerSprinting) And (Not shooting) Then
							g_I\GunAnimFLAG = False
						Else
							g_I\GunAnimFLAG = True
						EndIf
						;[End Block]
					Case GUNTYPE_SHOTGUN
						;[Block]
						If MouseHit1 And shootCondition Then
							pPressMouse1=True
							pPressReload=False
						EndIf
						
						; ~ [FIREMODES]
						
						If KeyHit(KEY_FIREMODE) And (Not MenuOpen) And (Not AttachmentMenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen) And (Not IsInVote())) And (Not IsModerationOpen()) And g\JamAmount < g\MaxJams Then
							If pReloadState = 0.0 Then
								g\FireMode = g\FireMode + 1
								If g\ID = GUN_SPAS12 Then
									If g\FireMode = 0 Then
										PlayGunSound("change_fire_auto",1,1,False)
									ElseIf g\FireMode = 1 Then
										PlayGunSound("change_fire_semi",1,1,False)
									Else
										PlayGunSound(g\name+"\deploy",1,1,False)
									EndIf
								Else
									PlayGunSound(g\name+"\deploy",1,1,False)
								EndIf
							EndIf
						EndIf
						
						If g\ID = GUN_SPAS12 Then
							pMaxFireMode = 2
						Else
							pMaxFireMode = 1
						EndIf
						
						If g\FireMode > pMaxFireMode Then
							If g\ID = GUN_SPAS12 Then
								PlayGunSound("change_fire_auto",1,1,False)
							EndIf
							PlayGunSound(g\name+"\deploy",1,1,False)
							g\FireMode = 0
						EndIf
						
						Select g\FireMode
							Case pMaxFireMode - 2
								OnSafety = False
								If pAmmo=0 Then
									If MouseHit1 And shootCondition Then
										pPressMouse1=True
										pPressReload=False
									EndIf
								Else
									If MouseDown1 And shootCondition Then
										pPressMouse1=True
										pPressReload=False
									EndIf
								EndIf
							Case pMaxFireMode - 1
								OnSafety = False
								If MouseHit1 And shootCondition Then
									pPressMouse1=True
									pPressReload=False
								EndIf
							Case pMaxFireMode
								OnSafety = True
						End Select
						
						; ~ [END]
						
						If g\IsSeparate Then
							If g_I\HoldingGun = g\ID Then
								If EntityHidden(g\HandsObj) Then ShowEntity g\HandsObj
							EndIf
						EndIf
						
						If (pAmmo = 0 And pShootState > 0.0) Lor OnSafety Lor pReloadState > 0.0 Lor pIsPlayerSprinting Lor g\JamAmount >= g\MaxJams Then
							pPressMouse1=False
						EndIf
						
						If pShootState = 0.0 And pPressMouse1 And pAmmo > 0 And pDeployState >= g\Deploy_Time And g\JamAmount < g\MaxJams Then
							If (Not AttachmentMenuOpen) Then
								If (Not OnSafety) Then
									SetAnimTime(g\obj,g\Frame_Idle)
								Else
									SetAnimTime(g\obj,g\Frame_Safety)
								EndIf
							Else
								SetAnimTime(g\obj,g\Frame_Attachment)
							EndIf
						EndIf
						
						If pDeployState < g\Deploy_Time Lor pAmmo > g\MaxCurrAmmo Lor pReloadState = 0.0 Then
							pPressReload=False
						EndIf
						If pReloadAmmo = 0 Then
							pPressReload=False
						EndIf
						
						If KeyHit(KEY_RELOAD) And (Not MenuOpen) And (Not AttachmentMenuOpen) And (Not ConsoleOpen) And ((Not isMultiplayer) Lor (Not mp_I\ChatOpen) And (Not IsInVote())) And (Not IsModerationOpen()) Then
							If pReloadState = 0.0 And pAmmo =< g\MaxCurrAmmo And pReloadAmmo > 0 Then
								pPressReload=True
								DeselectIronSight()
							EndIf
						EndIf
						
						shooting = False
						
						If g\Found Then
							If pDeployState < g\Deploy_Time Then
								DeselectIronSight()
								pIronSight = False
								pPressReload = False
								pPressMouse1 = False
								ChangeGunFrames(g,g\Anim_Deploy,False)
								If prevFrame# < (g\Anim_Deploy\x+1) And AnimTime(g\obj) >= (g\Anim_Deploy\x+1) Then
									PlayGunSound(g\name+"\deploy",1,1,False)
								EndIf
							Else
								reloadCondition = True
							EndIf
						Else
							If pDeployState < g\Deploy_Time Then
								DeselectIronSight()
								pIronSight = False
								pPressReload = False
								pPressMouse1 = False
								ChangeGunFrames(g,g\Anim_Ready,False)
								If prevFrame# < (g\Anim_Ready\x+1) And AnimTime(g\obj) >= (g\Anim_Ready\x+1) Then
									PlayGunSound(g\name+"\ready",1,1,False)
								EndIf
							Else
								reloadCondition = True
								g\Found = True
							EndIf
						EndIf
						
						If reloadCondition = True Then
							
							If g\JamAmount => g\MaxJams Then
								DeselectIronSight()
								pIronSight = False
								pPressReload = False
								pPressMouse1 = False
								
								p.Particles = CreateParticle(EntityX(FindChild(g\obj,muzzlebone$),True),EntityY(FindChild(g\obj,muzzlebone$),True),EntityZ(FindChild(g\obj,muzzlebone$),True),6,0.003,-0.005,150)
								
								If g\JamTimer < g\JamState Then
									g\JamTimer = g\JamTimer + FPSfactor
								Else
									g\JamAmount = Max(g\JamAmount - 1,0)
								EndIf
								If g\JamTimer < g\JamState Then
									If AnimTime(g\obj)>g\Anim_Jam\y Lor AnimTime(g\obj)<g\Anim_Jam\x Then
										SetAnimTime(g\obj,g\Anim_Jam\x)
									EndIf
									ChangeGunFrames(g,g\Anim_Jam,False)
									If prevFrame# < (g\Anim_Jam\x+1) And AnimTime(g\obj) >= (g\Anim_Jam\x+1) Then
										PlayGunSound(g\name+"\jam",1,1,False)
									ElseIf prevFrame# < (g\Anim_Jam\y-0.5) And AnimTime(g\obj) >= (g\Anim_Jam\y-0.5) Then
										pPressReload = False
									EndIf
								EndIf
							Else
								If g\JamAmount > 0 And g\JamTimer > 0 Then
									g\JamAmount = Max(g\JamAmount - (g\MaxJams/7),0)
								EndIf
								g\JamTimer = 0
							EndIf
							
							If g\JamAmount < g\MaxJams Then
								If pReloadState = 0.0 Then
									If g\FireMode = 0 Then
										If pShootState = 0.0 Then
											If AnimTime(g\obj) > g\Anim_Shoot\x And AnimTime(g\obj) < g\Anim_Shoot\y-0.5 Then
												ChangeGunFrames(g,g\Anim_Shoot,False)
											ElseIf AnimTime(g\obj) >= g\Anim_Reload_Stop\x And AnimTime(g\obj) < (g\Anim_Reload_Stop\y-0.5) Then
												ChangeGunFrames(g,g\Anim_Reload_Stop,False)
												If AnimTime(g\obj) >= (g\Anim_Reload_Stop\y-0.5) Then
													SetAnimTime(g\obj,g\Frame_Idle)
													pPressReload = False
												EndIf
											Else
												If (Not AttachmentMenuOpen Lor OnSafety) Then
													If pIsPlayerSprinting Then
														pPressMouse1 = False
														pPressReload = False
														If AnimTime(g\obj)<=(g\Anim_Sprint_Transition\y-0.5) Lor AnimTime(g\obj)>(g\Anim_Sprint_Cycle\y) Then
															ChangeGunFrames(g,g\Anim_Sprint_Transition,False)
														Else
															ChangeGunFrames(g,g\Anim_Sprint_Cycle,True)
														EndIf
													Else
														If AnimTime(g\obj)>(g\Anim_Sprint_Transition\x+0.5) And AnimTime(g\obj)<=g\Anim_Sprint_Cycle\y Then
															ChangeGunFrames(g,g\Anim_Sprint_Transition,False,True)
														Else
															SetAnimTime(g\obj,g\Frame_Idle)
														EndIf
													EndIf
												ElseIf AttachmentMenuOpen Then
													SetAnimTime(g\obj,g\Frame_Attachment)
												Else
													SetAnimTime(g\obj,g\Frame_Safety)
												EndIf
											EndIf
											
											If pPressMouse1 And pAmmo = 0 Then
												PlayGunSound(g\name+"\shoot_empty",1,1,False)
											EndIf
										Else
											ChangeGunFrames(g,g\Anim_Shoot,False)
											If Ceil(AnimTime(g\obj)) = g\Anim_Shoot\x Then
												PlayGunSound(g\name,g\MaxShootSounds,0,True)
												CameraShake = g\Knockback/2.0
												user_camera_pitch = user_camera_pitch - g\Knockback
												
												If (Not g_I\IronSight) Then
													TurnEntity g_I\GunPivot,CurveValue(Rand(-(g\Knockback+g\Knockback)/4,(g\Knockback+g\Knockback)*2),Rand((g\Knockback+g\Knockback)*2,-(g\Knockback+g\Knockback)/4),5.0),CurveValue(Rand(-(g\Knockback+g\Knockback)/4,(g\Knockback+g\Knockback)*2),Rand((g\Knockback+g\Knockback)*2,-(g\Knockback+g\Knockback)/4),5.0),CurveValue(Rand(-(g\Knockback+g\Knockback)/4,(g\Knockback+g\Knockback)*2),Rand((g\Knockback+g\Knockback)*2,-(g\Knockback+g\Knockback)/2),5.0)
												EndIf
												
												g_I\GunLightTimer = FPSfactor
												shooting = True
												EntityTexture g\MuzzleFlash, ParticleTextures[1]
												ShowEntity g\MuzzleFlash
												TurnEntity g\MuzzleFlash,0,0,Rnd(360)
												ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
											EndIf
											If pShootState >= g\Rate_Of_Fire-FPSfactor And pPressMouse1 Then
												SetAnimTime g\obj,g\Anim_Shoot\x
											EndIf
										EndIf
									Else
										If pShootState = 0.0 Then
											If AnimTime(g\obj) > g\Anim_Shoot_Alt\x And AnimTime(g\obj) < g\Anim_Shoot_Alt\y-0.5 Then
												ChangeGunFrames(g,g\Anim_Shoot_Alt,False)
											ElseIf AnimTime(g\obj) >= g\Anim_Reload_Stop\x And AnimTime(g\obj) < (g\Anim_Reload_Stop\y-0.5) Then
												ChangeGunFrames(g,g\Anim_Reload_Stop,False)
												If AnimTime(g\obj) >= (g\Anim_Reload_Stop\y-0.5) Then
													SetAnimTime(g\obj,g\Frame_Idle)
													pPressReload = False
												EndIf
											Else
												If (Not AttachmentMenuOpen Lor OnSafety) Then
													If pIsPlayerSprinting Then
														pPressMouse1 = False
														pPressReload = False
														If AnimTime(g\obj)<=(g\Anim_Sprint_Transition\y-0.5) Lor AnimTime(g\obj)>(g\Anim_Sprint_Cycle\y) Then
															ChangeGunFrames(g,g\Anim_Sprint_Transition,False)
														Else
															ChangeGunFrames(g,g\Anim_Sprint_Cycle,True)
														EndIf
													Else
														If AnimTime(g\obj)>(g\Anim_Sprint_Transition\x+0.5) And AnimTime(g\obj)<=g\Anim_Sprint_Cycle\y Then
															ChangeGunFrames(g,g\Anim_Sprint_Transition,False,True)
														Else
															SetAnimTime(g\obj,g\Frame_Idle)
														EndIf
													EndIf
												ElseIf AttachmentMenuOpen Then
													SetAnimTime(g\obj,g\Frame_Attachment)
												Else
													SetAnimTime(g\obj,g\Frame_Safety)
												EndIf
											EndIf
											
											If pPressMouse1 And pAmmo = 0 Then
												PlayGunSound(g\name+"\shoot_empty",1,1,False)
											EndIf
										Else
											ChangeGunFrames(g,g\Anim_Shoot_Alt,False)
											If Ceil(AnimTime(g\obj)) = g\Anim_Shoot_Alt\x Then
												PlayGunSound(g\name+"\shoot_alt",1,1,False)
												CameraShake = g\Knockback/2.0
												user_camera_pitch = user_camera_pitch - g\Knockback
												
												If (Not g_I\IronSight) Then
													TurnEntity g_I\GunPivot,CurveValue(Rand(-(g\Knockback+g\Knockback)/4,(g\Knockback+g\Knockback)*2),Rand((g\Knockback+g\Knockback)*2,-(g\Knockback+g\Knockback)/4),5.0),CurveValue(Rand(-(g\Knockback+g\Knockback)/4,(g\Knockback+g\Knockback)*2),Rand((g\Knockback+g\Knockback)*2,-(g\Knockback+g\Knockback)/4),5.0),CurveValue(Rand(-(g\Knockback+g\Knockback)/4,(g\Knockback+g\Knockback)*2),Rand((g\Knockback+g\Knockback)*2,-(g\Knockback+g\Knockback)/2),5.0)
												EndIf
												
												g_I\GunLightTimer = FPSfactor
												shooting = True
												EntityTexture g\MuzzleFlash, ParticleTextures[1]
												ShowEntity g\MuzzleFlash
												TurnEntity g\MuzzleFlash,0,0,Rnd(360)
												ScaleSprite g\MuzzleFlash,Rnd(0.025,0.03),Rnd(0.025,0.03)
											EndIf
											If pShootState >= g\Rate_Of_Fire-FPSfactor And pPressMouse1 Then
												SetAnimTime g\obj,g\Anim_Shoot_Alt\x
											EndIf
										EndIf										
									EndIf
								Else
;									If pAmmo = 0 Then
;										If AnimTime(g\obj)>g\Anim_Reload_Stop\y Lor AnimTime(g\obj)<g\Anim_Reload_Start_Empty\x Then
;											SetAnimTime(g\obj,g\Anim_Reload_Start_Empty\x)
;										EndIf
;										If AnimTime(g\obj) >= g\Anim_Reload_Start_Empty\x And AnimTime(g\obj) < (g\Anim_Reload_Start_Empty\y-0.5) Then
;											ChangeGunFrames(g,g\Anim_Reload_Start_Empty,False)
;											If AnimTime(g\obj) >= (g\Anim_Reload_Start_Empty\y-0.5) Then
;												SetAnimTime(g\obj,g\Anim_Reload_Empty\x)
;												PlayGunSound(g\name+"\reload_start",g\MaxReloadSounds,1,False)
;											EndIf
;										ElseIf AnimTime(g\obj) >= g\Anim_Reload_Empty\x And AnimTime(g\obj) < (g\Anim_Reload_Empty\y-0.5) Then
;											ChangeGunFrames(g,g\Anim_Reload_Empty,False)
;											If AnimTime(g\obj) >= (g\Anim_Reload_Empty\y-0.5) Then
;												If pAmmo < g\MaxCurrAmmo And pReloadAmmo > 0 Then
;													SetAnimTime(g\obj,g\Anim_Reload_Empty\x)
;													PlayGunSound(g\name+"\reload",g\MaxReloadSounds,1,False)
;												Else
;													SetAnimTime(g\obj,g\Anim_Reload_Stop\x)
;													PlayGunSound(g\name+"\reload_stop",1,1,False)
;												EndIf
;											EndIf
;										ElseIf AnimTime(g\obj) >= g\Anim_Reload_Stop\x And AnimTime(g\obj) < (g\Anim_Reload_Stop\y-0.5) Then
;											ChangeGunFrames(g,g\Anim_Reload_Stop,False)
;											If AnimTime(g\obj) >= (g\Anim_Reload_Stop\y-0.5) Then
;												SetAnimTime(g\obj,g\Frame_Idle)
;												pPressReload = False
;											EndIf
;										EndIf
;									Else
									If AnimTime(g\obj)>g\Anim_Reload_Stop\y Lor AnimTime(g\obj)<g\Anim_Reload_Start\x Then
										SetAnimTime(g\obj,g\Anim_Reload_Start\x)
									EndIf
									If AnimTime(g\obj) >= g\Anim_Reload_Start\x And AnimTime(g\obj) < (g\Anim_Reload_Start\y-0.5) Then
										ChangeGunFrames(g,g\Anim_Reload_Start,False)
										If AnimTime(g\obj) >= (g\Anim_Reload_Start\y-0.5) Then
											SetAnimTime(g\obj,g\Anim_Reload_Empty\x)
											PlayGunSound(g\name+"\reload",g\MaxReloadSounds,1,False)
										EndIf
									ElseIf AnimTime(g\obj) >= g\Anim_Reload_Empty\x And AnimTime(g\obj) < (g\Anim_Reload_Empty\y-0.5) Then
										ChangeGunFrames(g,g\Anim_Reload_Empty,False)
										If AnimTime(g\obj) >= (g\Anim_Reload_Empty\y-0.5) Then
											If pAmmo < g\MaxCurrAmmo And pReloadAmmo > 0 Then
												SetAnimTime(g\obj,g\Anim_Reload_Empty\x)
												PlayGunSound(g\name+"\reload",g\MaxReloadSounds,1,False)
											Else
												SetAnimTime(g\obj,g\Anim_Reload_Stop\x)
												PlayGunSound(g\name+"\reload_stop",1,1,False)
											EndIf
										EndIf
									ElseIf AnimTime(g\obj) >= g\Anim_Reload_Stop\x And AnimTime(g\obj) < (g\Anim_Reload_Stop\y-0.5) Then
										ChangeGunFrames(g,g\Anim_Reload_Stop,False)
										If AnimTime(g\obj) >= (g\Anim_Reload_Stop\y-0.5) Then
											SetAnimTime(g\obj,g\Frame_Idle)
											pPressReload = False
										EndIf
									EndIf
;									EndIf
									pIronSight = False
								EndIf
							EndIf
						EndIf
						
						If g\IsSeparate Then
							SetAnimTime(g\HandsObj,AnimTime(g\obj))
						EndIf
						
						If (Not pIsPlayerSprinting) And (Not shooting) Then
							g_I\GunAnimFLAG = False
						Else
							g_I\GunAnimFLAG = True
						EndIf
						;[End Block]
					Case GUNTYPE_MELEE
						;[Block]
						If MouseDown1 And shootCondition Then
							pPressMouse1=True
						EndIf
						
						If MouseDown2 And shootCondition Then
							pPressMouse2=True
						EndIf
						
						If g\IsSeparate Then
							If g_I\HoldingGun = g\ID Then
								If EntityHidden(g\HandsObj) Then ShowEntity g\HandsObj
							Else
								HideEntity g\HandsObj
							EndIf
						EndIf
						
						If pShootState > 0.0 Then
							pPressMouse1=False
							pPressMouse2=False
						EndIf
						
						If pShootState = 0.0 And (pPressMouse1 Lor pPressMouse2) And pDeployState >= g\Deploy_Time Then
							SetAnimTime(g\obj,g\Frame_Idle)
						EndIf
						
						shooting = False
						
						If g\Found Then
							If pDeployState < g\Deploy_Time Then
								ChangeGunFrames(g,g\Anim_Deploy,False)
								If prevFrame# < (g\Anim_Deploy\x+1) And AnimTime(g\obj) >= (g\Anim_Deploy\x+1) Then
									PlayGunSound(g\name+"\deploy",1,1,False)
								EndIf
							Else
								reloadCondition = True
							EndIf
						Else
							If pDeployState < g\Deploy_Time Then
								ChangeGunFrames(g,g\Anim_Ready,False)
								If prevFrame# < (g\Anim_Ready\x+1) And AnimTime(g\obj) >= (g\Anim_Ready\x+1) Then
									PlayGunSound(g\name+"\ready",1,1,False)
								EndIf
							Else
								reloadCondition = True
								g\Found = True
							EndIf
						EndIf
						
						If reloadCondition = True Then
							
							If pShootState = 0.0 Then
								If AnimTime(g\obj) > g\Anim_Shoot\x And AnimTime(g\obj) < g\Anim_Shoot\y-0.5 Then
									ChangeGunFrames(g,g\Anim_Shoot,False)
								Else
									If (Not AttachmentMenuOpen) Then
										SetAnimTime(g\obj,g\Frame_Idle)
									Else
										SetAnimTime(g\obj,g\Frame_Attachment)
									EndIf
								EndIf
								If AnimTime(g\obj) > g\Anim_Shoot_Alt\x And AnimTime(g\obj) < g\Anim_Shoot_Alt\y-0.5 Then
									ChangeGunFrames(g,g\Anim_Shoot_Alt,False)
								Else
									If (Not AttachmentMenuOpen) Then
										SetAnimTime(g\obj,g\Frame_Idle)
									Else
										SetAnimTime(g\obj,g\Frame_Attachment)
									EndIf
								EndIf
								pReloadState = 0.0
							Else
								If pPressMouse2 Then
									ChangeGunFrames(g,g\Anim_Shoot_Alt,False)
									If Ceil(AnimTime(g\obj)) = g\Anim_Shoot_Alt\x Then
										PlayGunSound(g\name+"\miss",1,1,True)
									EndIf
								Else
									ChangeGunFrames(g,g\Anim_Shoot,False)
									If Ceil(AnimTime(g\obj)) = g\Anim_Shoot\x Then
										PlayGunSound(g\name+"\miss",1,1,True)
									EndIf
								EndIf
								
								If pShootState >= g\ShootDelay And pReloadState = 0.0 Then
									pReloadState = 1.0
								EndIf
								If pShootState >= g\Rate_Of_Fire-FPSfactor And pPressMouse1 Then
									SetAnimTime g\obj,g\Anim_Shoot\x
									pReloadState = 0.0
								EndIf
								If pShootState >= g\Rate_Of_Fire-FPSfactor And pPressMouse2 Then
									SetAnimTime g\obj,g\Anim_Shoot_Alt\x
									pReloadState = 0.0
								EndIf
							EndIf
							
						EndIf
						
						If g\IsSeparate Then						
							SetAnimTime(g\HandsObj,AnimTime(g\obj))
						EndIf
						
						If (Not shooting) Then
							g_I\GunAnimFLAG = False
						Else
							g_I\GunAnimFLAG = True
						EndIf
						;[End Block]
				End Select
				
				;! ~ Jamming Logic
				
				If g\MaxJams > 0 Then
					If shooting Then
						g\JamAmount = g\JamAmount + 1
						g\JamTimer2 = 0
					Else
						If g\JamAmount > 0 And g\JamTimer = 0 Then
							g\JamTimer2 = g\JamTimer2 + (FPSfactor*0.01)
						ElseIf g\JamAmount = 0 Then
							g\JamTimer2 = 0
						EndIf
						If g\JamAmount > 0 And g\JamTimer2 >= 2 Then
							g\JamAmount = Max(g\JamAmount - 1*(FPSfactor*g\JamCooldown),0)
						EndIf
					EndIf
				EndIf
				
				;! ~ End
				
			EndIf
			
			If (Not isMultiplayer) And g\ID = pHoldingGun Then
				If pDeployState < g\Deploy_Time Then
					pDeployState = pDeployState + FPSfactor
				Else
					If g\GunType = GUNTYPE_SHOTGUN Then
						If (pPressMouse1 And pShootState = 0.0) Then
							If pAmmo > 0 Then
								For j=1 To g\Amount_Of_Bullets
									ShootGun(g)
								Next
								pAmmo = pAmmo - 1
								pShootState = FPSfactor
								pReloadState = 0.0
								pPressReload = False
							EndIf
						EndIf
						If pShootState > 0.0 And pShootState < g\Rate_Of_Fire Then
							pShootState = pShootState + FPSfactor
							pReloadState = 0.0
							pPressReload = False
						Else
							pShootState = 0.0
						EndIf
						If pAmmo = g\MaxCurrAmmo Lor pReloadState > 0.0 Then
							pPressReload = False
						EndIf
						If pPressReload Then
							pReloadState = FPSfactor
						EndIf
						
						If pReloadState > 0.0 And pReloadState < g\Reload_Start_Time Then
							pShootState = 0.0
							pReloadState = pReloadState + FPSfactor
							If pReloadState >= g\Reload_Start_Time Then
								pAmmo = pAmmo + 1
								pReloadAmmo = pReloadAmmo - 1
							EndIf
						ElseIf pReloadState >= g\Reload_Start_Time And pReloadState < (g\Reload_Start_Time+g\Reload_Empty_Time) Then
							pReloadState = pReloadState + FPSfactor
							If pReloadState >= (g\Reload_Start_Time+g\Reload_Empty_Time) Then
								If pAmmo < g\MaxCurrAmmo And pReloadAmmo > 0 Then
									pReloadState = g\Reload_Start_Time
									pAmmo = pAmmo + 1
									pReloadAmmo = pReloadAmmo - 1
								EndIf
							EndIf
						Else
							pReloadState = 0.0
						EndIf
					ElseIf g\GunType = GUNTYPE_MELEE Then
						pPressReload = False
						
						If (pPressMouse1 And pShootState = 0.0) Then
							pShootState = FPSfactor
						EndIf
						If (pPressMouse2 And pShootState = 0.0) Then
							pShootState = FPSfactor
						EndIf
						If pShootState > 0.0 And pShootState < g\Rate_Of_Fire Then
							pShootState = pShootState + FPSfactor
							If pShootState >= g\ShootDelay And pShootState <= g\ShootDelay+FPSfactor Then
								For i=1 To g\Amount_Of_Bullets
									If g\name <> "m67" And g\name <> "rgd5" Then
										ShootGun(g)
									Else
										If g_I\HoldingGun = GUN_M67 Then
											CreateGrenade(EntityX(FindChild(g\obj,"Base HumanRPalm"),True),EntityY(FindChild(g\obj,"Base HumanRPalm"),True),EntityZ(FindChild(g\obj,"Base HumanRPalm"),True), EntityPitch(Camera), EntityYaw(Camera))
										Else
											CreateGrenade(EntityX(FindChild(g\obj,"Base HumanRPalm"),True),EntityY(FindChild(g\obj,"Base HumanRPalm"),True),EntityZ(FindChild(g\obj,"Base HumanRPalm"),True), EntityPitch(Camera), EntityYaw(Camera), Grenade_RGD5)
										EndIf
										If (Not NTF_InfiniteAmmo) Then DropGrenade()
									EndIf
								Next
							EndIf
						Else
							pShootState = 0.0
						EndIf
					Else
						If pReloadState = 0.0 Then
							If pPressMouse1 Lor pShootState = -1.0 Then
								If pShootState = 0.0 Lor pShootState = -1.0 Then
									If pAmmo > 0 Then
										For j=1 To g\Amount_Of_Bullets
											ShootGun(g)
										Next
										If (Not NTF_NoReload) Then pAmmo = pAmmo - 1
										pShootState = FPSfactor
										pPressReload = False
									EndIf
								EndIf
							EndIf
							If pShootState > 0.0 And pShootState < g\Rate_Of_Fire Then
								pShootState = pShootState + FPSfactor
								pPressReload = False
							Else
								pShootState = 0.0
							EndIf
							If pAmmo > g\MaxCurrAmmo Then
								pPressReload = False
							EndIf
							If pPressReload Then
								pReloadState = FPSfactor
							EndIf
						ElseIf pReloadState > 0.0 And pReloadState < g\Reload_Empty_Time And pAmmo = 0 Then
							pShootState = 0.0
							pReloadState = pReloadState + FPSfactor
						ElseIf pReloadState > 0.0 And pReloadState < g\Reload_Time And pAmmo > 0 Then
							pShootState = 0.0
							pReloadState = pReloadState + FPSfactor
						Else
							pReloadState = 0.0
							If g\ID <> GUN_SCP127 Then
								Local pAmmoDelta% = g\MaxCurrAmmo - pAmmo
								If pReloadAmmo <> 0 Then
									If pAmmo = 0 Then
										pAmmo = pAmmo + Min(pAmmoDelta, pReloadAmmo)
									Else
										pAmmo = pAmmo + Min(pAmmoDelta, pReloadAmmo)+1
										pReloadAmmo = pReloadAmmo - 1
									EndIf
								Else
									pAmmo = pAmmo + Min(pAmmoDelta, pReloadAmmo)
								EndIf
								If (Not NTF_InfiniteAmmo) Then pReloadAmmo = Max(pReloadAmmo - pAmmoDelta, 0)
								If NTF_NoReload Then pAmmo = g\MaxCurrAmmo
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
		Next
	EndIf
	If isMultiplayer Then
		Players[mp_I\PlayerID]\WeaponInSlot[Players[mp_I\PlayerID]\SelectedSlot] = pHoldingGun
		Players[mp_I\PlayerID]\DeployState = pDeployState
		Players[mp_I\PlayerID]\ReloadState = pReloadState
		Players[mp_I\PlayerID]\ShootState = pShootState
		Players[mp_I\PlayerID]\PressMouse1 = pPressMouse1
		Players[mp_I\PlayerID]\PressReload = pPressReload
		If Players[mp_I\PlayerID]\SelectedSlot < (MaxSlots - SlotsWithNoAmmo) Then
			Players[mp_I\PlayerID]\Ammo[Players[mp_I\PlayerID]\SelectedSlot] = pAmmo
			Players[mp_I\PlayerID]\ReloadAmmo[Players[mp_I\PlayerID]\SelectedSlot] = pReloadAmmo
		EndIf
		Players[mp_I\PlayerID]\IsPlayerSprinting = pIsPlayerSprinting
		Players[mp_I\PlayerID]\IronSight = pIronSight
	Else
		g_I\HoldingGun = pHoldingGun
		psp\DeployState = pDeployState
		psp\ReloadState = pReloadState
		psp\ShootState = pShootState
		If currGun <> Null Then
			currGun\CurrAmmo = pAmmo
			currGun\CurrReloadAmmo = pReloadAmmo
		EndIf
		IsPlayerSprinting = pIsPlayerSprinting
	EndIf
	g_I\IronSight = pIronSight
End Function

Function ToggleGuns()
	Local g.Guns
	Local i%, j%
	Local case1%, case2%
	Local GunInInventory%
	Local KeyPressed%[MaxGunSlots]
	Local KeyPressedHolster% = KeyHit(KEY_HOLSTERGUN)
	
	For i = 0 To MaxGunSlots-1
		If co\Enabled Then
			KeyPressed[i] = GetDPadButtonPress()
		Else
			KeyPressed[i] = KeyHit(i + 2)
		EndIf
	Next
	
	If KillTimer >= 0 And CanPlayerUseGuns And FPSfactor > 0.0 And ChatSFXOpened = False And (Not g_I\IronSight) Then
		For i = 0 To MaxGunSlots-1
			If co\Enabled Then
				case1 = -1
				case2 = KeyPressed[i]
				For g = Each Guns
					If g_I\HoldingGun <> g\ID Then
						If g\IsSeparate Then
							HideEntity g\HandsObj
						EndIf
					EndIf
				Next
			Else
				case1 = KeyPressed[i]
				case2 = False
				For g = Each Guns
					If g_I\HoldingGun <> g\ID Then
						If g\IsSeparate Then
							HideEntity g\HandsObj
						EndIf
					EndIf
				Next
			EndIf
			
			If (case1 = -1 And case2 = Int(WrapAngle(180 + (90 * i)))) Lor (case1 And (Not case2)) Then
				If g_I\Weapon_InSlot[i] <> "" Then
					g_I\GunChangeFLAG = False
					For g = Each Guns
						If g\name = g_I\Weapon_InSlot[i] Then
							g_I\HoldingGun = g\ID
							Exit
						EndIf
					Next
					g_I\Weapon_CurrSlot = (i + 1)
					mpl\SlotsDisplayTimer = 70*3
				EndIf
			EndIf
		Next
		
		If (Not co\Enabled) Then
			case1 = KeyPressedHolster
		EndIf
		If (case1 = -1 And case2 = 90) Lor (case1 And (Not case2)) Lor FallTimer < 0 Then
			For g.Guns = Each Guns
				If g\ID = g_I\HoldingGun Then
					If g\IsSeparate Then
						HideEntity g\HandsObj
					EndIf
					PlayGunSound(g\name$+"\holster",1,1,False)
					If g\JamAmount >= g\MaxJams Then
						g\JamTimer = 0
					EndIf
				EndIf
			Next
			g_I\GunChangeFLAG = False
			g_I\HoldingGun = 0
			g_I\Weapon_CurrSlot = 0
			mpl\SlotsDisplayTimer = 0
		EndIf
		
	EndIf
	
End Function

Function HolsterGun()
	Local g.Guns
	
	For g.Guns = Each Guns
		If g\ID = g_I\HoldingGun Then
			If g\IsSeparate Then
				HideEntity g\HandsObj
			EndIf
			HideEntity g\obj
			PlayGunSound(g\name$+"\holster",1,1,False)
			If g\JamAmount >= g\MaxJams Then
				g\JamTimer = 0
			EndIf
		EndIf
	Next
	g_I\GunChangeFLAG = False
	g_I\HoldingGun = 0
	g_I\Weapon_CurrSlot = 0
	mpl\SlotsDisplayTimer = 0
	
End Function

Function AnimateGuns()
	
	If (Not g_I\GunAnimFLAG) And (CurrSpeed=0.0 Lor mi_I\EndingTimer>0.0) And (Not IsPlayerSprinting%) And (Not g_I\IronSight) And (Not AttachmentMenuOpen)
		If GunPivot_YSide%=0
			If GunPivot_Y# > -0.005
				GunPivot_Y# = GunPivot_Y# - (0.00005*FPSfactor)
			Else
				GunPivot_Y# = -0.005
				GunPivot_YSide% = 1
			EndIf
		Else
			If GunPivot_Y# < 0.0
				GunPivot_Y# = GunPivot_Y# + (0.00005*FPSfactor)
			Else
				GunPivot_Y# = 0.0
				GunPivot_YSide% = 0
			EndIf
		EndIf
		
		If GunPivot_X# < 0.0
			GunPivot_X# = Min(GunPivot_X#+(0.0001*FPSfactor),0.0)
		ElseIf GunPivot_X# > 0.0
			GunPivot_X# = Max(GunPivot_X#-(0.0001*FPSfactor),0.0)
		EndIf
	ElseIf (Not g_I\GunAnimFLAG) And CurrSpeed<>0.0 And (Not IsPlayerSprinting%) And (Not g_I\IronSight) And (Not mi_I\EndingTimer>0.0 And (Not AttachmentMenuOpen))
		If GunPivot_XSide%=0
			If GunPivot_X# > -0.0025
				GunPivot_X# = GunPivot_X# - (0.000075/(1.0 + Crouch)*FPSfactor)
				If GunPivot_X# > -0.00125
					GunPivot_Y# = Min(GunPivot_Y#+(0.000125/(1.0 + Crouch)*FPSfactor),0.001)
				Else
					GunPivot_Y# = Max(GunPivot_Y#-(0.000125/(1.0 + Crouch)*FPSfactor),0.0)
				EndIf
			Else
				GunPivot_X# = -0.0025
				GunPivot_Y# = 0.0
				GunPivot_XSide% = 1
			EndIf
		Else
			If GunPivot_X# < 0.0
				GunPivot_X# = GunPivot_X# + (0.000075/(1.0 + Crouch)*FPSfactor)
				If GunPivot_X# < -0.00125
					GunPivot_Y# = Min(GunPivot_Y#+(0.000125/(1.0 + Crouch)*FPSfactor),0.001)
				Else
					GunPivot_Y# = Max(GunPivot_Y#-(0.000125/(1.0 + Crouch)*FPSfactor),0.0)
				EndIf
			Else
				GunPivot_X# = 0.0
				GunPivot_Y# = 0.0
				GunPivot_XSide% = 0
			EndIf
		EndIf
	Else
		If GunPivot_Y# < 0.0
			GunPivot_Y# = Max(GunPivot_Y#+(0.0001*FPSfactor),0.0)
		Else
			GunPivot_Y# = 0.0
		EndIf
		
		If GunPivot_X# < 0.0
			GunPivot_X# = Min(GunPivot_X#+(0.0001*FPSfactor),0.0)
		ElseIf GunPivot_X# > 0.0
			GunPivot_X# = Max(GunPivot_X#-(0.0001*FPSfactor),0.0)
		EndIf
	EndIf
	
	PositionEntity g_I\GunPivot,EntityX(Camera), EntityY(Camera)+GunPivot_Y#, EntityZ(Camera)
	MoveEntity g_I\GunPivot,GunPivot_X#,0,0
	
End Function

Function ChangeGunFrames(g.Guns,anim.Vector3D,loop%=True,reverse%=False)
	Local newTime#,temp%
	
	Local speed# = anim\z
	If reverse Then speed=-anim\z
	
	If speed > 0.0 Then
		newTime = Max(Min(AnimTime(g\obj) + speed * FPSfactor,anim\y),anim\x)
		
		If loop And newTime => anim\y Then
			newTime = anim\x
		EndIf
	Else
		Local anim_start% = anim\x
		Local anim_end% = anim\y
		If anim\x < anim\y Then
			anim_start = anim\y
			anim_end = anim\x
		EndIf
		
		If loop Then
			newTime = AnimTime(g\obj) + speed * FPSfactor
			
			If newTime < anim_end Then 
				newTime = anim_start
			ElseIf newTime > anim_start Then
				newTime = anim_end
			EndIf
		Else
			newTime = Max(Min(AnimTime(g\obj) + speed * FPSfactor,anim_start),anim_end)
		EndIf
	EndIf
	SetAnimTime g\obj,newTime
	
End Function

Function ShootPlayer(x#,y#,z#,hitProb#=1.0,particles%=True,damage%=10)
	Local pvt,i
	
	Local p.Particles = CreateParticle(x,y,z, 1, Rnd(0.08,0.1), 0.0, 5)
	TurnEntity p\obj, 0,0,Rnd(360)
	p\Achange = -0.15
	
	LightVolume = TempLightVolume*1.2
	
	If (Not GodMode) Then 
		
		If Rnd(1.0)=<hitProb Then
			TurnEntity Camera, Rnd(-3,3), Rnd(-3,3), 0
			
			DamageSPPlayer(damage)
			mpl\DamageTimer = 70
			
			PlaySound_Strict BullethitSFX
		ElseIf particles
			pvt = CreatePivot()
			PositionEntity pvt, EntityX(Collider),(EntityY(Collider)+EntityY(Camera))/2,EntityZ(Collider)
			PointEntity pvt, p\obj
			TurnEntity pvt, 0, 180, 0
			
			EntityPick(pvt, 2.5)
			
			If PickedEntity() <> 0 Then 
				PlaySound2(Gunshot3SFX, Camera, pvt, 0.4, Rnd(0.8,1.0))
				
				If particles Then 
					;dust/smoke particles
					p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.03, 0, 80)
					p\speed = 0.001
					p\SizeChange = 0.003
					p\A = 0.8
					p\Achange = -0.01
					RotateEntity p\pvt, EntityPitch(pvt)-180, EntityYaw(pvt),0
					
					For i = 0 To Rand(2,3)
						p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.006, 0.003, 80)
						p\speed = 0.02
						p\A = 0.8
						p\Achange = -0.01
						RotateEntity p\pvt, EntityPitch(pvt)+Rnd(170,190), EntityYaw(pvt)+Rnd(-10,10),0	
					Next
					
					;bullet hole decal
					Local de.Decals = CreateDecal(GetRandomDecalID(DECAL_TYPE_BULLETHOLE), PickedX(),PickedY(),PickedZ(), 0,0,0)
					AlignToVector de\obj,-PickedNX(),-PickedNY(),-PickedNZ(),3
					MoveEntity de\obj, 0,0,-0.001
					EntityFX de\obj, 1+8
					de\fx = 1+8
					de\lifetime = 70*20
					EntityBlend de\obj, 2
					de\blendmode = 2
					de\Size = Rnd(0.028,0.034)
					ScaleSprite de\obj, de\Size, de\Size
				EndIf				
			EndIf
			
			pvt = FreeEntity_Strict(pvt)
		EndIf
		
	EndIf
	
End Function

Function ShootTarget(x#,y#,z#,n.NPCs,hitProb#=1.0,particles%=True,damage%=10)
	Local pvt%,i%
	
	;muzzle flash
	Local p.Particles = CreateParticle(x,y,z, 1, Rnd(0.08,0.1), 0.0, 5)
	TurnEntity p\obj, 0,0,Rnd(360)
	p\Achange = -0.15
	
	If Rnd(1.0)=<hitProb Then
		p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 5, 0.06, 0.2, 80)
		p\speed = 0.001
		p\SizeChange = 0.003
		p\A = 0.8
		p\Achange = -0.02
		
		If n\Target <> Null
			n\Target\HP% = n\Target\HP% - damage%
			;n\Target\GotShot% = True
		EndIf
		
	ElseIf particles Then
		pvt = CreatePivot()
		PositionEntity pvt, EntityX(Collider),(EntityY(Collider)+EntityY(Camera))/2,EntityZ(Collider)
		PointEntity pvt, p\obj
		TurnEntity pvt, 0, 180, 0
		
		EntityPick(pvt, 2.5)
		
		If PickedEntity() <> 0 Then 
			PlaySound2(Gunshot3SFX, Camera, pvt, 0.4, Rnd(0.8,1.0))
			
			If particles Then 
				;dust/smoke particles
				p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.03, 0, 80)
				p\speed = 0.001
				p\SizeChange = 0.003
				p\A = 0.8
				p\Achange = -0.01
				RotateEntity p\pvt, EntityPitch(pvt)-180, EntityYaw(pvt),0
				
				For i = 0 To Rand(2,3)
					p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.006, 0.003, 80)
					p\speed = 0.02
					p\A = 0.8
					p\Achange = -0.01
					RotateEntity p\pvt, EntityPitch(pvt)+Rnd(170,190), EntityYaw(pvt)+Rnd(-10,10),0	
				Next
				
				;bullet hole decal
				Local de.Decals = CreateDecal(GetRandomDecalID(DECAL_TYPE_BULLETHOLE), PickedX(),PickedY(),PickedZ(), 0,0,0)
				AlignToVector de\obj,-PickedNX(),-PickedNY(),-PickedNZ(),3
				MoveEntity de\obj, 0,0,-0.001
				EntityFX de\obj, 1+8
				de\fx = 1+8
				de\lifetime = 70*20
				EntityBlend de\obj, 2
				de\blendmode = 2
				de\Size = Rnd(0.028,0.034)
				ScaleSprite de\obj, de\Size, de\Size
			EndIf				
		EndIf
		
		pvt = FreeEntity_Strict(pvt)
	EndIf
	
End Function

Function ShootGun(g.Guns)
	Local temp,n.NPCs,p.Particles,j,de.Decals,ent_pick%,i%
	Local hitNPC.NPCs ;unused right now, but could be very useful later, there should be no performance difference and better code
	Local DismemberedBone%
	
	IsPlayerShooting% = True
	
	If g\GunType <> GUNTYPE_MELEE And (Not g\HasAttachments[ATT_SUPPRESSOR]) And g\ID <> GUN_SCP127 Then
		LightVolume = TempLightVolume*1.2
		ShowEntity g_I\GunLight
		g_I\GunLightTimer = FPSfactor
	EndIf
	
	;If (Not g_I\IronSight) Lor g\GunType = GUNTYPE_SHOTGUN Then
		RotateEntity GunPickPivot,Rnd(-g\Accuracy,g\Accuracy)/(1.0+(3.0*g_I\IronSight)),Rnd(-g\Accuracy,g\Accuracy)/(1.0+(3.0*g_I\IronSight)),0
	;Else
	;	RotateEntity GunPickPivot,0,0,0
	;EndIf
	
	HideEntity Head
	If g\Range<=0.0 Then
		EntityPick GunPickPivot,10000.0
	Else
		EntityPick GunPickPivot,g\Range
	EndIf
	
	ent_pick% = PickedEntity()
	If ent_pick%<>0 Then
		For n.NPCs = Each NPCs
			For j = 0 To 24
				If ent_pick% = n\HitBox1[j] Then ; ~ Head
					
					If g_I\HoldingGun = GUN_EMRP Then
						If n\NPCtype = NPC_SCP_106 Then
							n\State[0] = 70 * 60 * Rand(10,13)
							TranslateEntity(n\Collider,0,-10.0,0,True)
						ElseIf n\NPCtype = NPC_SCP_049 Then
							n\State[0] = SCP049_STUNNED
						EndIf
					ElseIf g\GunType = GUNTYPE_SHOTGUN Then
						If n\NPCtype = NPC_SCP_049 Then
							n\State[0] = SCP049_STUNNED
						EndIf
					EndIf
					
					If g_I\HoldingGun = g\ID And g\GunType = GUNTYPE_SHOTGUN Lor g_I\HoldingGun =  GUN_EMRP Then
						If n\Boss <> n Then
							n\HP = 0
						Else
							n\HP = n\HP - g\DamageOnEntity*4
						EndIf
					Else
						n\HP = n\HP - g\DamageOnEntity*4
					EndIf
					hitNPC = n
					n\GotHit = True
					If n\HP <= 0 Then
						If g_I\HoldingGun = g\ID And g\GunType = GUNTYPE_SHOTGUN Lor g_I\HoldingGun =  GUN_EMRP Then
							If n\Boss <> n Then n\HeadShot = True; : DismemberedBone = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCtype, "head_bonename"))
						EndIf
					EndIf
					Exit
				EndIf
				If ent_pick% = n\HitBox2[j] Then ; ~ Body
					n\HP = n\HP - g\DamageOnEntity
					hitNPC = n
					n\GotHit = True
					
					If g_I\HoldingGun = GUN_EMRP Then
						If n\NPCtype = NPC_SCP_106 Then
							n\State[0] = 70 * 60 * Rand(10,13)
							TranslateEntity(n\Collider,0,-10.0,0,True)
						ElseIf n\NPCtype = NPC_SCP_049 Then
							n\State[0] = SCP049_STUNNED
						EndIf
					ElseIf g\GunType = GUNTYPE_SHOTGUN Then
						If n\NPCtype = NPC_SCP_049 Then
							n\State[0] = SCP049_STUNNED
						EndIf
					EndIf
					
					Exit
				EndIf
				If ent_pick% = n\HitBox3[j] Then ; ~ Arms\Legs
					n\HP = n\HP - (g\DamageOnEntity/2)
					;If (g_I\HoldingGun = g\ID And g\GunType = GUNTYPE_SHOTGUN Lor g_I\HoldingGun =  GUN_EMRP) And n\Boss <> n Then DismemberedBone = FindChild(n\obj, GetINIString("Data\NPCBones.ini", n\NPCtype, "weapon_hand_bonename"))
					hitNPC = n
					n\GotHit = True
					Exit
				EndIf
				
;				If DismemberedBone <> 0 Then
;					ScaleEntity(DismemberedBone,0,0,0)
;					
;					;PlaySound2(LoadTempSound("SFX\SCP\1048A\Explode.ogg"),Camera,n\Collider)
;					p.Particles = CreateParticle(EntityX(n\Collider),EntityY(n\Collider)+0.8,EntityZ(n\Collider),5,0.25,0.0)
;					EntityColor p\obj,100,100,100
;					RotateEntity p\pvt,0,0,Rnd(360)
;					p\Achange = -Rnd(0.02,0.03)
;					For i = 0 To 1
;						p.Particles = CreateParticle(EntityX(n\Collider)+Rnd(-0.2,0.2),EntityY(n\Collider)+0.85,EntityZ(n\Collider)+Rnd(-0.2,0.2),5,0.15,0.0)
;						EntityColor p\obj,100,100,100
;						RotateEntity p\pvt,0,0,Rnd(360)
;						p\Achange = -Rnd(0.02,0.03)
;					Next
;				EndIf
				
			Next
			If hitNPC <> Null Then Exit
		Next
		
		Local wa.Water,hitwater.Water
		
		For wa.Water = Each Water
			If ent_pick% = wa\obj Then
				hitwater = wa
				PlaySound2(LoadTempSound("SFX\General\Water_Splash.ogg"),Camera,wa\obj,50)
				
				p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 11, 0.2, 0.2, 80)
				p\speed = 0.001
				p\SizeChange = 0.003
				p\A = 0.8
				p\Achange = -0.02
				RotateEntity p\obj,0,0,0
			EndIf
			If hitwater <> Null Then Exit
		Next
	EndIf
	
	If hitNPC <> Null Then
		p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 5, 0.06, 0.2, 80)
		p\speed = 0.001
		p\SizeChange = 0.003
		p\A = 0.8
		p\Achange = -0.02
		
		If g\GunType = GUNTYPE_MELEE Then
			PlayGunSound(g\name+"\hitbody",g\MaxShootSounds,0,True,True)
		EndIf
		
		;;debuglog "shot"
	ElseIf ent_pick <> 0 And hitwater = Null Then
		p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.03, 0, 80)
		p\speed = 0.001
		p\SizeChange = 0.003
		p\A = 0.8
		p\Achange = -0.01
		RotateEntity p\pvt, EntityPitch(g_I\GunPivot)-180, EntityYaw(g_I\GunPivot),0
		
		If g\GunType <> GUNTYPE_MELEE Then
			PlaySound2(Gunshot3SFX, Camera, p\pvt, 0.4, Rnd(0.8,1.0))
		Else
			PlayGunSound(g\name+"\hitwall",g\MaxWallhitSounds,0,True,True)
		EndIf
		
		For i = 0 To Rand(2,3)
			p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.006, 0.003, 80)
			p\speed = 0.02
			p\A = 0.8
			p\Achange = -0.01
			RotateEntity p\pvt, EntityPitch(g_I\GunPivot)+Rnd(170,190), EntityYaw(g_I\GunPivot)+Rnd(-10,10),0
		Next
		
		Local DecalID% = 0
		Select g\DecalType
			Case GUNDECAL_DEFAULT
				DecalID = DECAL_TYPE_BULLETHOLE
			Case GUNDECAL_SLASH
				DecalID = DECAL_TYPE_SLASHHOLE
			Case GUNDECAL_SMASH
				DecalID = DECAL_TYPE_SMASHHOLE
		End Select
		
		de.Decals = CreateDecal(GetRandomDecalID(DecalID), PickedX(),PickedY(),PickedZ(), 0,0,0)
		AlignToVector de\obj,-PickedNX(),-PickedNY(),-PickedNZ(),3
		MoveEntity de\obj, 0,0,-0.001
		EntityFX de\obj, 1+8
		de\fx = 1+8
		de\lifetime = 70*20
		EntityBlend de\obj, 2
		de\blendmode = 2
		de\Size = Rnd(0.028,0.034)
		ScaleSprite de\obj, de\Size, de\Size
		EntityParent de\obj,ent_pick
	EndIf
	
End Function

Function PlayGunSound(name$,max_amount%=1,sfx%=0,pitchshift%=False,custom%=False)
	Local g.Guns, gun.Guns
	
	For g = Each Guns
		If name = g\name Then
			gun = g
			Exit
		EndIf
	Next
	
	If sfx%=0 Then
		If (Not custom) Then
			If (Not g\HasAttachments[ATT_SUPPRESSOR]) Then
				If max_amount% = 1 Then
					GunSFX = gun\ShootSounds[0]
				Else
					GunSFX = gun\ShootSounds[Rand(0,max_amount%-1)]
				EndIf
			Else
				If max_amount% = 1 Then
					GunSFX = gun\SuppressedShootSounds[0]
				Else
					GunSFX = gun\SuppressedShootSounds[Rand(0,max_amount%-1)]
				EndIf
			EndIf
		Else
			If max_amount% = 1 Then
				GunSFX = LoadSound_Strict("SFX\Guns\"+name$+".ogg")
			Else
				GunSFX = LoadSound_Strict("SFX\Guns\"+name$+Rand(1,max_amount%)+".ogg")
			EndIf
		EndIf
		GunCHN = PlaySound_Strict(GunSFX)
		If GunPitchShift% = 1 Then
			If pitchshift% Then
				ChannelPitch GunCHN,Rand(38000,43000)
			EndIf
		EndIf
	Else
		If GunSFX2 <> 0 Then FreeSound_Strict GunSFX2:GunSFX2=0
		If ChannelPlaying(GunCHN2) Then StopChannel(GunCHN2) : GunCHN2 = 0
		If max_amount% = 1 Then
			GunSFX2 = LoadSound_Strict("SFX\Guns\"+name$+".ogg")
		Else
			GunSFX2 = LoadSound_Strict("SFX\Guns\"+name$+Rand(1,max_amount%)+".ogg")
		EndIf
		GunCHN2 = PlaySound_Strict(GunSFX2)
	EndIf
	
End Function

Function UpdateIronSight()
	Local pvt%,g.Guns,hasIronSight%,prevIronSight%
	Local currGun.Guns
	
	If IsPlayerSprinting% Lor AttachmentMenuOpen Lor OnSafety Then
		DeselectIronSight()
	EndIf
	
	For g.Guns = Each Guns
		If g\ID = g_I\HoldingGun Then
			If g\GunType<>GUNTYPE_MELEE Then
				hasIronSight% = True
				If g_I\IronSight% Lor g_I\IronSightAnim% Then
					EntityParent g\obj,IronSightPivot2%
					If g\IsSeparate Then
						EntityParent g\HandsObj,IronSightPivot2%
					EndIf
				EndIf
				currGun = g
				Exit
			Else
				hasIronSight = False
				Exit
			EndIf
		EndIf
	Next
	
	If (Not hasIronSight) Then
		Return
	EndIf
	
	If g_I\IronSight% Then
		
		IronSightTimer = IronSightTimer + FPSfactor
		
		If g_I\IronSightAnim = 2 Then
			If currGun<>Null Then
				If g\HasAttachments[ATT_RED_DOT] Then
					PositionEntity IronSightPivot%,currGun\RedDotIronSightCoords\x,currGun\RedDotIronSightCoords\y,currGun\RedDotIronSightCoords\z
				ElseIf g\HasAttachments[ATT_ACOG_SCOPE] Then
					PositionEntity IronSightPivot%,currGun\AcogIronSightCoords\x,currGun\AcogIronSightCoords\y,currGun\AcogIronSightCoords\z
				ElseIf g\HasAttachments[ATT_EOTECH] Then
					PositionEntity IronSightPivot%,currGun\EoTechIronSightCoords\x,currGun\EoTechIronSightCoords\y,currGun\EoTechIronSightCoords\z
				ElseIf g\HasAttachments[ATT_SPECIAL_SCOPE] Then
					PositionEntity IronSightPivot%,currGun\P90ScopeIronSightCoords\x,currGun\P90ScopeIronSightCoords\y,currGun\P90ScopeIronSightCoords\z
				Else
					PositionEntity IronSightPivot%,currGun\IronSightCoords\x,currGun\IronSightCoords\y,currGun\IronSightCoords\z
				EndIf
			Else
				PositionEntity IronSightPivot%,0,0,0
			EndIf
			g_I\IronSightAnim = 1
		EndIf
	Else
		
		IronSightTimer = 0
		
		If g_I\IronSightAnim = 2 Then
			PositionEntity IronSightPivot%,0,0,0
			g_I\IronSightAnim = 1
		EndIf
	EndIf
	
	Local aimCondition%
	
	If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
		If psp\DeployState >= g\Deploy_Time Then
			aimCondition = True
		EndIf
	Else
		If Players[mp_I\PlayerID]\DeployState >= g\Deploy_Time Then
			aimCondition = True
		EndIf
	EndIf
	
	If aimCondition Then
		If IronSightTimer >= 8 Then
			ReadyToShowDot = True
			;If g\HasAttachments[ATT_ACOG_SCOPE] Then ApplyScopeMaterial(True)
		Else
			ReadyToShowDot = False
			;If g\HasAttachments[ATT_ACOG_SCOPE] Then ApplyScopeMaterial(False)
		EndIf
	EndIf
	
	PositionEntity IronSightPivot2%,CurveValue(EntityX(IronSightPivot),EntityX(IronSightPivot2),5.0),CurveValue(EntityY(IronSightPivot),EntityY(IronSightPivot2),5.0),CurveValue(EntityZ(IronSightPivot),EntityZ(IronSightPivot2),5.0)
	If EntityX(IronSightPivot2%) <= 0.001 And EntityX(IronSightPivot2%) >= -0.001 Then
		g_I\IronSightAnim = 0
	EndIf
	If currGun<>Null Then
		If g\HasAttachments[ATT_RED_DOT] Then
			If EntityX(IronSightPivot2%) <= currGun\RedDotIronSightCoords\x+0.001 And EntityX(IronSightPivot2%) >= currGun\RedDotIronSightCoords\x-0.001 Then
				g_I\IronSightAnim = 0
			EndIf
		ElseIf g\HasAttachments[ATT_ACOG_SCOPE] Then
			If EntityX(IronSightPivot2%) <= currGun\AcogIronSightCoords\x+0.001 And EntityX(IronSightPivot2%) >= currGun\AcogIronSightCoords\x-0.001 Then
				g_I\IronSightAnim = 0
			EndIf
		ElseIf g\HasAttachments[ATT_EOTECH] Then
			If EntityX(IronSightPivot2%) <= currGun\EoTechIronSightCoords\x+0.001 And EntityX(IronSightPivot2%) >= currGun\EoTechIronSightCoords\x-0.001 Then
				g_I\IronSightAnim = 0
			EndIf
		ElseIf g\HasAttachments[ATT_SPECIAL_SCOPE] Then
			If EntityX(IronSightPivot2%) <= currGun\P90ScopeIronSightCoords\x+0.001 And EntityX(IronSightPivot2%) >= currGun\P90ScopeIronSightCoords\x-0.001 Then
				g_I\IronSightAnim = 0
			EndIf
		Else
			If EntityX(IronSightPivot2%) <= currGun\IronSightCoords\x+0.001 And EntityX(IronSightPivot2%) >= currGun\IronSightCoords\x-0.001 Then
				g_I\IronSightAnim = 0
			EndIf
		EndIf
	Else
		g_I\IronSightAnim = 0
	EndIf
	
	If opt\HoldToAim Then
		If (Not g_I\IronSightAnim) And hasIronSight Then
			prevIronSight = g_I\IronSight
			g_I\IronSight% = MouseDown2
			If g_I\IronSight <> prevIronSight Then
				g_I\IronSightAnim = 2
			EndIf
		EndIf
	Else
		If MouseHit2 Then
			If SelectedItem = Null Then
				If (Not g_I\IronSightAnim) And hasIronSight Then
					g_I\IronSight% = (Not g_I\IronSight%)
					g_I\IronSightAnim = 2
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

Function DeselectIronSight()
	Local g.Guns
	
	g_I\IronSight% = 0
	g_I\IronSightAnim% = 0
	PositionEntity IronSightPivot%,0,0,0
	PositionEntity IronSightPivot2%,0,0,0
	For g = Each Guns
		EntityParent g\obj,g_I\GunPivot
		If g\IsSeparate Then
			EntityParent g\HandsObj,g_I\GunPivot
		EndIf
	Next
	
End Function

Function IsPlayerOutside()
	Local e.Events
	
	If gopt\GameMode = GAMEMODE_MULTIPLAYER Then Return False
	
	If PlayerRoom\RoomTemplate\Name = "pocketdimension" Then Return True
	If PlayerRoom\RoomTemplate\Name = "gate_a_intro" Then Return True
	If PlayerRoom\RoomTemplate\Name = "gate_a_topside" Then Return True
	If PlayerRoom\RoomTemplate\Name = "gate_a_road" Then Return True
	If PlayerRoom\RoomTemplate\Name = "gate_b_topside" Then Return True
	If PlayerRoom\RoomTemplate\Name = "gate_c_topside" Then Return True
	If PlayerRoom\RoomTemplate\Name = "gate_d_topside" Then Return True
	
	For e.Events = Each Events
		If e\EventName = "testroom_860" Then
			If e\EventState[0] = 1.0
				Return True
			EndIf
			Exit
		EndIf
	Next
	Return False
	
End Function

Function GetWeaponMaxCurrAmmo(gunID%)
	Local g.Guns
	
	For g = Each Guns
		If g\ID = gunID Then
			Return g\MaxCurrAmmo
			Exit
		EndIf
	Next
	
End Function

Function GetWeaponMaxReloadAmmo(gunID%)
	Local g.Guns
	
	For g = Each Guns
		If g\ID = gunID Then
			Return g\MaxReloadAmmo
			Exit
		EndIf
	Next
	
End Function

Function RenderAttachments()
	Local g.Guns,i
	Local x# = opt\GraphicWidth/2, y# = opt\GraphicHeight/2
	Local width# = 64, height# = 64
	Local attX#, attY#
	
	For g = Each Guns
		If g_I\HoldingGun = g\ID And g\CanSelectMenuAttachments Then
			SetFont(fo\Font[Font_Default_Large])
			Text x-160,y-450,GetLocalString("Attachments", "atts")
			SetFont(fo\Font[Font_Default])
			Text x-220,y-400,GetLocalStringR("Attachments", "atts_exit",KeyName[KEY_ATTACHMENTS])
			
		;! ~ [Barrel]
			
			If g\name = "beretta" Lor g\name = "usp" Lor g\name = "fiveseven" Lor g\name = "glock" Then
				attX = x-50
				attY = y-25
			ElseIf g\name = "p90" Then
				attX = x-70
				attY = y-35
			ElseIf g\name = "m4a1" Lor g\name = "rpk16" Then
				attX = x-280
				attY = y-45
			Else
				attX = x-180
				attY = y-35
			EndIf
			
			If g\name <> "m870" And g\name <> "emrp" Then
			
				DrawFrame(attX,attY,width*2.2,height*4,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
				
				DrawFrame(attX+6,attY+5,width*2,height/2.2,0,0,1024,1024,FRAME_THICK,256,False,255,0,50,70,0,20,40)
				Text attX+20,attY+14,GetLocalString("Attachments", "barrel_atts")
				
				; ~ (Nothing)
				
				If g\BarrelAttachments = 0 Then
					DrawFrame(attX+6,attY+35,width*2,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,38,127,0,19,63,0)
				Else
					DrawFrame(attX+6,attY+35,width*2,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
				EndIf
				Text attX+16,attY+42,GetLocalString("Attachments", "att_none")
				
				; ~ (Suppressor)
				
				If g\CanHaveAttachments[ATT_SUPPRESSOR] Then
					If g\HasPickedAttachments[ATT_SUPPRESSOR] And (Not g\HasToggledAttachments[ATT_SUPPRESSOR]) Then
						DrawFrame(attX+6,attY+70,width*2,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
					ElseIf g\HasPickedAttachments[ATT_SUPPRESSOR] And g\HasToggledAttachments[ATT_SUPPRESSOR] Then
						DrawFrame(attX+6,attY+70,width*2,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,38,127,0,19,63,0)
					ElseIf g\BarrelAttachments = 1 And (Not g\HasPickedAttachments[ATT_SUPPRESSOR]) Then
						DrawFrame(attX+6,attY+70,width*2,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,50,0,0,19,63,0)
					Else
						DrawFrame(attX+6,attY+70,width*2,height/2,0,0,1024,1024,FRAME_THICK,256,True)
					EndIf
					Text attX+16,attY+77,GetLocalString("Attachments", "att_suppressor")
				EndIf
				
				; ~ (Match\Compensator)
				
				If g\CanHaveAttachments[ATT_MATCH] Then
					If g\HasPickedAttachments[ATT_MATCH] And (Not g\HasToggledAttachments[ATT_MATCH]) Then
						DrawFrame(attX+6,attY+105,width*2,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
					ElseIf g\HasPickedAttachments[ATT_MATCH] And g\HasToggledAttachments[ATT_MATCH] Then
						DrawFrame(attX+6,attY+105,width*2,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,38,127,0,19,63,0)
					ElseIf g\BarrelAttachments = 2 And (Not g\HasPickedAttachments[ATT_MATCH]) Then
						DrawFrame(attX+6,attY+105,width*2,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,50,0,0,19,63,0)
					Else
						DrawFrame(attX+6,attY+105,width*2,height/2,0,0,1024,1024,FRAME_THICK,256,True)
					EndIf
				Else
					If g\BarrelAttachments = 2 Then
						DrawFrame(attX+6,attY+105,width*2,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,70,70,70,19,63,0)
					Else
						DrawFrame(attX+6,attY+105,width*2,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,70,70,70,40,40,40)
					EndIf
				EndIf
				If g\ID = GUN_USP Then
					Text attX+16,attY+112,GetLocalString("Attachments", "att_match")
				Else
					Text attX+16,attY+112,GetLocalString("Attachments", "att_compensator")
				EndIf
				
			EndIf
				
		;! ~ [Mount]
			
			If g\name <> "beretta" And g\name <> "usp" And g\name <> "fiveseven" And g\name <> "glock" Then
			
				If g\name = "p90" Lor g\name = "emrp" Then
					
					attX = x+150
					attY = y-200
					
					DrawFrame(attX,attY,width*3,height*4,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
					
					DrawFrame(attX+6,attY+5,width*2.8,height/2.2,0,0,1024,1024,FRAME_THICK,256,False,255,0,50,70,0,20,40)
					Text attX+20,attY+14,GetLocalString("Attachments", "mount_atts")
					
					; ~ (Nothing)
					
					If g\MountAttachments = 0 Then
						DrawFrame(attX+6,attY+35,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,38,127,0,19,63,0)
					Else
						DrawFrame(attX+6,attY+35,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
					EndIf
					Text attX+16,attY+42,GetLocalString("Attachments", "att_none")
					
					If g\name = "p90" Then
					
						; ~ (P90 Default Scope)
						
						If g\CanHaveAttachments[ATT_SPECIAL_SCOPE] Then
							If (Not g\HasToggledAttachments[ATT_SPECIAL_SCOPE]) Then
								DrawFrame(attX+6,attY+70,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
							Else
								DrawFrame(attX+6,attY+70,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,38,127,0,19,63,0)
							EndIf
							Text attX+16,attY+77,GetLocalString("Attachments", "att_p90_scope")
						EndIf
						
					Else
						
						; ~ (EMR-P Default Scope)
						
						If g\CanHaveAttachments[ATT_SPECIAL_SCOPE] Then
							If (Not g\HasToggledAttachments[ATT_SPECIAL_SCOPE]) Then
								DrawFrame(attX+6,attY+70,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
							Else
								DrawFrame(attX+6,attY+70,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,38,127,0,19,63,0)
							EndIf
							Text attX+16,attY+77,GetLocalString("Attachments", "att_emrp_scope")
						EndIf
						
					EndIf
					
					; ~ (Red Dot Sight)
					
					If g\CanHaveAttachments[ATT_RED_DOT] Then
						If g\HasPickedAttachments[ATT_RED_DOT] And (Not g\HasToggledAttachments[ATT_RED_DOT]) Then
							DrawFrame(attX+6,attY+105,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
						ElseIf g\HasPickedAttachments[ATT_RED_DOT] And g\HasToggledAttachments[ATT_RED_DOT] Then
							DrawFrame(attX+6,attY+105,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,38,127,0,19,63,0)
						ElseIf g\MountAttachments = 2 And (Not g\HasPickedAttachments[ATT_RED_DOT]) Then
							DrawFrame(attX+6,attY+105,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,50,0,0,19,63,0)
						Else
							DrawFrame(attX+6,attY+105,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,True)
						EndIf
						Text attX+16,attY+112,GetLocalString("Attachments", "att_red_dot")
					EndIf
					
					; ~ (EoTech Holocraphic Sight)
					
					If g\CanHaveAttachments[ATT_EOTECH] Then
						If g\HasPickedAttachments[ATT_EOTECH] And (Not g\HasToggledAttachments[ATT_EOTECH]) Then
							DrawFrame(attX+6,attY+140,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
						ElseIf g\HasPickedAttachments[ATT_EOTECH] And g\HasToggledAttachments[ATT_EOTECH] Then
							DrawFrame(attX+6,attY+140,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,38,127,0,19,63,0)
						ElseIf g\MountAttachments = 3 And (Not g\HasPickedAttachments[ATT_EOTECH]) Then
							DrawFrame(attX+6,attY+140,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,50,0,0,19,63,0)
						Else
							DrawFrame(attX+6,attY+140,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,True)
						EndIf
						Text attX+16,attY+147,GetLocalString("Attachments", "att_eotech")
					EndIf
					
					; ~ (Acog Scope)
					
					If g\CanHaveAttachments[ATT_ACOG_SCOPE] Then
						If g\HasPickedAttachments[ATT_ACOG_SCOPE] And (Not g\HasToggledAttachments[ATT_ACOG_SCOPE]) Then
							DrawFrame(attX+6,attY+175,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
						ElseIf g\HasPickedAttachments[ATT_ACOG_SCOPE] And g\HasToggledAttachments[ATT_ACOG_SCOPE] Then
							DrawFrame(attX+6,attY+175,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,38,127,0,19,63,0)
						ElseIf g\MountAttachments = 4 And (Not g\HasPickedAttachments[ATT_ACOG_SCOPE]) Then
							DrawFrame(attX+6,attY+175,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,50,0,0,19,63,0)
						Else
							DrawFrame(attX+6,attY+175,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,True)
						EndIf
					Else
						If g\MountAttachments = 4 Then
							DrawFrame(attX+6,attY+175,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,70,70,70,19,63,0)
						Else
							DrawFrame(attX+6,attY+175,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,70,70,70,40,40,40)
						EndIf
					EndIf
					Text attX+16,attY+182,GetLocalString("Attachments", "att_acog")
					
				Else
					
					attX = x+150
					attY = y-200
					
					DrawFrame(attX,attY,width*3,height*4,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
					
					DrawFrame(attX+6,attY+5,width*2.8,height/2.2,0,0,1024,1024,FRAME_THICK,256,False,255,0,50,70,0,20,40)
					Text attX+20,attY+14,GetLocalString("Attachments", "mount_atts")
					
					; ~ (Nothing)
					
					If g\MountAttachments = 0 Then
						DrawFrame(attX+6,attY+35,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,38,127,0,19,63,0)
					Else
						DrawFrame(attX+6,attY+35,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
					EndIf
					Text attX+16,attY+42,GetLocalString("Attachments", "att_none")
					
					; ~ (Red Dot Sight)
					
					If g\CanHaveAttachments[ATT_RED_DOT] Then
						If g\HasPickedAttachments[ATT_RED_DOT] And (Not g\HasToggledAttachments[ATT_RED_DOT]) Then
							DrawFrame(attX+6,attY+70,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
						ElseIf g\HasPickedAttachments[ATT_RED_DOT] And g\HasToggledAttachments[ATT_RED_DOT] Then
							DrawFrame(attX+6,attY+70,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,38,127,0,19,63,0)
						ElseIf g\MountAttachments = 1 And (Not g\HasPickedAttachments[ATT_RED_DOT]) Then
							DrawFrame(attX+6,attY+70,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,50,0,0,19,63,0)
						Else
							DrawFrame(attX+6,attY+70,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,True)
						EndIf
						Text attX+16,attY+77,GetLocalString("Attachments", "att_red_dot")
					EndIf
					
					; ~ (EoTech Holocraphic Sight)
					
					If g\CanHaveAttachments[ATT_EOTECH] Then
						If g\HasPickedAttachments[ATT_EOTECH] And (Not g\HasToggledAttachments[ATT_EOTECH]) Then
							DrawFrame(attX+6,attY+105,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
						ElseIf g\HasPickedAttachments[ATT_EOTECH] And g\HasToggledAttachments[ATT_EOTECH] Then
							DrawFrame(attX+6,attY+105,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,38,127,0,19,63,0)
						ElseIf g\MountAttachments = 2 And (Not g\HasPickedAttachments[ATT_EOTECH]) Then
							DrawFrame(attX+6,attY+105,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,50,0,0,19,63,0)
						Else
							DrawFrame(attX+6,attY+105,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,True)
						EndIf
						Text attX+16,attY+112,GetLocalString("Attachments", "att_eotech")
					EndIf
					
					; ~ (Acog Scope)
					
					If g\CanHaveAttachments[ATT_ACOG_SCOPE] Then
						If g\HasPickedAttachments[ATT_ACOG_SCOPE] And (Not g\HasToggledAttachments[ATT_ACOG_SCOPE]) Then
							DrawFrame(attX+6,attY+140,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
						ElseIf g\HasPickedAttachments[ATT_ACOG_SCOPE] And g\HasToggledAttachments[ATT_ACOG_SCOPE] Then
							DrawFrame(attX+6,attY+140,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,38,127,0,19,63,0)
						ElseIf g\MountAttachments = 3 And (Not g\HasPickedAttachments[ATT_ACOG_SCOPE]) Then
							DrawFrame(attX+6,attY+140,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,50,0,0,19,63,0)
						Else
							DrawFrame(attX+6,attY+140,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,True)
						EndIf
					Else
						If g\MountAttachments = 3 Then
							DrawFrame(attX+6,attY+140,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,70,70,70,19,63,0)
						Else
							DrawFrame(attX+6,attY+140,width*2.8,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,70,70,70,40,40,40)
						EndIf
					EndIf
					Text attX+16,attY+147,GetLocalString("Attachments", "att_acog")
					
				EndIf
				
			EndIf
				
		;! ~ [Magazine]
			
			If g\CanHaveAttachments[ATT_EXT_MAG] Then
				
				attX = x+100
				attY = y+120
				
				DrawFrame(attX,attY,width*3.2,height*4,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
				
				DrawFrame(attX+6,attY+5,width*3,height/2.2,0,0,1024,1024,FRAME_THICK,256,False,255,0,50,70,0,20,40)
				Text attX+20,attY+14,GetLocalString("Attachments", "mag_atts")
				
				; ~ (Nothing)
				
				If g\MagazineAttachments = 0 Then
					DrawFrame(attX+6,attY+35,width*3,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,38,127,0,19,63,0)
				Else
					DrawFrame(attX+6,attY+35,width*3,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
				EndIf
				Text attX+16,attY+42,GetLocalString("Attachments", "att_none")
				
				; ~ (Extended Magazine)
				
				If g\HasPickedAttachments[ATT_EXT_MAG] And (Not g\HasToggledAttachments[ATT_EXT_MAG]) Then
					DrawFrame(attX+6,attY+70,width*3,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,0,74,127,0,43,74)
				ElseIf g\HasPickedAttachments[ATT_EXT_MAG] And g\HasToggledAttachments[ATT_EXT_MAG] Then
					DrawFrame(attX+6,attY+70,width*3,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,38,127,0,19,63,0)
				ElseIf g\MagazineAttachments = 1 And (Not g\HasPickedAttachments[ATT_EXT_MAG]) Then
					DrawFrame(attX+6,attY+70,width*3,height/2,0,0,1024,1024,FRAME_THICK,256,False,255,50,0,0,19,63,0)
				Else
					DrawFrame(attX+6,attY+70,width*3,height/2,0,0,1024,1024,FRAME_THICK,256,True)
				EndIf
				Text attX+16,attY+77,GetLocalString("Attachments", "att_ext_mag")
			EndIf
			
		EndIf
	Next
	
End Function

Function UpdateAttachments()
	Local g.Guns,i,n,keyHits[5]
	Local MaxBarrelAmount#, MaxMountAmount#, MaxMagazineAmount#
	
	For g = Each Guns
		If g_I\HoldingGun = g\ID Then
			If g\CanSelectMenuAttachments Then
				
				If g\CanHaveAttachments[ATT_ACOG_SCOPE] And g\HasAttachments[ATT_ACOG_SCOPE] Then
					If g_I\HoldingGun = g\ID Then
						ApplyScopeMaterial(True)
					EndIf
				EndIf
				If g\name = "emrp" Then
					If g\CanHaveAttachments[ATT_SPECIAL_SCOPE] And g\HasAttachments[ATT_SPECIAL_SCOPE] Then
						If g_I\HoldingGun = g\ID Then
							ApplyScopeMaterial(True)
						EndIf
					EndIf
				EndIf
				
				If AttachmentMenuOpen Then
					For i = 0 To 4
						keyHits[i] = KeyHit(i+2)
					Next
					;keyHits[5] = KeyHit(KEY_RELOAD)
					
			;! ~ Attachment Selection
					
				; ~ [Barrel]
					
					If g\CanHaveAttachments[ATT_MATCH] Then
						MaxBarrelAmount# = 2
					Else
						MaxBarrelAmount# = 1
					EndIf
					
					If keyHits[0] Then
						g\BarrelAttachments = g\BarrelAttachments + 1
					EndIf
					
					If g\BarrelAttachments > MaxBarrelAmount# Then
						g\BarrelAttachments = 0
					EndIf
					
					Select g\BarrelAttachments
						Case 0 ; ~ No attachments selected (None)
							If g\HasPickedAttachments[ATT_SUPPRESSOR] And g\CanHaveAttachments[ATT_SUPPRESSOR] Then
								g\HasToggledAttachments[ATT_SUPPRESSOR] = False
							EndIf
							If g\HasPickedAttachments[ATT_MATCH] And g\CanHaveAttachments[ATT_MATCH] Then
								g\HasToggledAttachments[ATT_MATCH] = False
							EndIf
						Case 1 ; ~ First attachment in the row (Suppressor)
							If g\HasPickedAttachments[ATT_SUPPRESSOR] And g\CanHaveAttachments[ATT_SUPPRESSOR] Then
								g\HasToggledAttachments[ATT_SUPPRESSOR] = True
							EndIf
							If g\HasPickedAttachments[ATT_MATCH] And g\CanHaveAttachments[ATT_MATCH] Then
								g\HasToggledAttachments[ATT_MATCH] = False
							EndIf
						Case 2 ; ~ Second attachment in the row (Match\Compensator)
							If g\HasPickedAttachments[ATT_SUPPRESSOR] And g\CanHaveAttachments[ATT_SUPPRESSOR] Then
								g\HasToggledAttachments[ATT_SUPPRESSOR] = False
							EndIf
							If g\HasPickedAttachments[ATT_MATCH] And g\CanHaveAttachments[ATT_MATCH] Then
								g\HasToggledAttachments[ATT_MATCH] = True
							EndIf
					End Select
					
				; ~ [Mount]
					
					If g\CanHaveAttachments[ATT_SPECIAL_SCOPE] Then
						MaxMountAmount# = 4
					Else
						MaxMountAmount# = 3
					EndIf
					
					If keyHits[1] Then
						g\MountAttachments = g\MountAttachments + 1
					EndIf
					
					If g\MountAttachments > MaxMountAmount# Then
						g\MountAttachments = 0
					EndIf
					
					If g\name = "p90" Lor g\name = "emrp" Then
						Select g\MountAttachments
							Case 0 ; ~ No attachments selected (None)
								If g\HasPickedAttachments[ATT_SPECIAL_SCOPE] Then
									g\HasToggledAttachments[ATT_SPECIAL_SCOPE] = False
								EndIf
								If g\HasPickedAttachments[ATT_RED_DOT] And g\CanHaveAttachments[ATT_RED_DOT] Then
									g\HasToggledAttachments[ATT_RED_DOT] = False
								EndIf
								If g\HasPickedAttachments[ATT_EOTECH] And g\CanHaveAttachments[ATT_EOTECH] Then
									g\HasToggledAttachments[ATT_EOTECH] = False
								EndIf
								If g\HasPickedAttachments[ATT_ACOG_SCOPE] And g\CanHaveAttachments[ATT_ACOG_SCOPE] Then
									g\HasToggledAttachments[ATT_ACOG_SCOPE] = False
								EndIf
							Case 1 ; ~ First attachment in the row (FN P90 Standart Scope\EMR-P Standart Scope)
								If g\HasPickedAttachments[ATT_SPECIAL_SCOPE] Then
									g\HasToggledAttachments[ATT_SPECIAL_SCOPE] = True
								EndIf
								If g\HasPickedAttachments[ATT_RED_DOT] And g\CanHaveAttachments[ATT_RED_DOT] Then
									g\HasToggledAttachments[ATT_RED_DOT] = False
								EndIf
								If g\HasPickedAttachments[ATT_EOTECH] And g\CanHaveAttachments[ATT_EOTECH] Then
									g\HasToggledAttachments[ATT_EOTECH] = False
								EndIf
								If g\HasPickedAttachments[ATT_ACOG_SCOPE] And g\CanHaveAttachments[ATT_ACOG_SCOPE] Then
									g\HasToggledAttachments[ATT_ACOG_SCOPE] = False
								EndIf
							Case 2 ; ~ Second attachment in the row (Red Dot Sight)
								If g\HasPickedAttachments[ATT_SPECIAL_SCOPE] Then
									g\HasToggledAttachments[ATT_SPECIAL_SCOPE] = False
								EndIf
								If g\HasPickedAttachments[ATT_RED_DOT] And g\CanHaveAttachments[ATT_RED_DOT] Then
									g\HasToggledAttachments[ATT_RED_DOT] = True
								EndIf
								If g\HasPickedAttachments[ATT_EOTECH] And g\CanHaveAttachments[ATT_EOTECH] Then
									g\HasToggledAttachments[ATT_EOTECH] = False
								EndIf
								If g\HasPickedAttachments[ATT_ACOG_SCOPE] And g\CanHaveAttachments[ATT_ACOG_SCOPE] Then
									g\HasToggledAttachments[ATT_ACOG_SCOPE] = False
								EndIf
							Case 3 ; ~ Third attachment in the row (EoTech Sight)
								If g\HasPickedAttachments[ATT_SPECIAL_SCOPE] Then
									g\HasToggledAttachments[ATT_SPECIAL_SCOPE] = False
								EndIf
								If g\HasPickedAttachments[ATT_RED_DOT] And g\CanHaveAttachments[ATT_RED_DOT] Then
									g\HasToggledAttachments[ATT_RED_DOT] = False
								EndIf
								If g\HasPickedAttachments[ATT_EOTECH] And g\CanHaveAttachments[ATT_EOTECH] Then
									g\HasToggledAttachments[ATT_EOTECH] = True
								EndIf
								If g\HasPickedAttachments[ATT_ACOG_SCOPE] And g\CanHaveAttachments[ATT_ACOG_SCOPE] Then
									g\HasToggledAttachments[ATT_ACOG_SCOPE] = False
								EndIf
							Case 4 ; ~ Fourth attachment in the row (Acog Scope)
								If g\HasPickedAttachments[ATT_SPECIAL_SCOPE] Then
									g\HasToggledAttachments[ATT_SPECIAL_SCOPE] = False
								EndIf
								If g\HasPickedAttachments[ATT_RED_DOT] And g\CanHaveAttachments[ATT_RED_DOT] Then
									g\HasToggledAttachments[ATT_RED_DOT] = False
								EndIf
								If g\HasPickedAttachments[ATT_EOTECH] And g\CanHaveAttachments[ATT_EOTECH] Then
									g\HasToggledAttachments[ATT_EOTECH] = False
								EndIf
								If g\HasPickedAttachments[ATT_ACOG_SCOPE] And g\CanHaveAttachments[ATT_ACOG_SCOPE] Then
									g\HasToggledAttachments[ATT_ACOG_SCOPE] = True
								EndIf
						End Select
					Else
						Select g\MountAttachments
							Case 0 ; ~ No attachments selected (None)
								If g\HasPickedAttachments[ATT_RED_DOT] And g\CanHaveAttachments[ATT_RED_DOT] Then
									g\HasToggledAttachments[ATT_RED_DOT] = False
								EndIf
								If g\HasPickedAttachments[ATT_EOTECH] And g\CanHaveAttachments[ATT_EOTECH] Then
									g\HasToggledAttachments[ATT_EOTECH] = False
								EndIf
								If g\HasPickedAttachments[ATT_ACOG_SCOPE] And g\CanHaveAttachments[ATT_ACOG_SCOPE] Then
									g\HasToggledAttachments[ATT_ACOG_SCOPE] = False
								EndIf
							Case 1 ; ~ First attachment in the row (Red Dot Sight)
								If g\HasPickedAttachments[ATT_RED_DOT] And g\CanHaveAttachments[ATT_RED_DOT] Then
									g\HasToggledAttachments[ATT_RED_DOT] = True
								EndIf
								If g\HasPickedAttachments[ATT_EOTECH] And g\CanHaveAttachments[ATT_EOTECH] Then
									g\HasToggledAttachments[ATT_EOTECH] = False
								EndIf
								If g\HasPickedAttachments[ATT_ACOG_SCOPE] And g\CanHaveAttachments[ATT_ACOG_SCOPE] Then
									g\HasToggledAttachments[ATT_ACOG_SCOPE] = False
								EndIf
							Case 2 ; ~ Second attachment in the row (EoTech Sight)
								If g\HasPickedAttachments[ATT_RED_DOT] And g\CanHaveAttachments[ATT_RED_DOT] Then
									g\HasToggledAttachments[ATT_RED_DOT] = False
								EndIf
								If g\HasPickedAttachments[ATT_EOTECH] And g\CanHaveAttachments[ATT_EOTECH] Then
									g\HasToggledAttachments[ATT_EOTECH] = True
								EndIf
								If g\HasPickedAttachments[ATT_ACOG_SCOPE] And g\CanHaveAttachments[ATT_ACOG_SCOPE] Then
									g\HasToggledAttachments[ATT_ACOG_SCOPE] = False
								EndIf
							Case 3 ; ~ Third attachment in the row (Acog Scope)
								If g\HasPickedAttachments[ATT_RED_DOT] And g\CanHaveAttachments[ATT_RED_DOT] Then
									g\HasToggledAttachments[ATT_RED_DOT] = False
								EndIf
								If g\HasPickedAttachments[ATT_EOTECH] And g\CanHaveAttachments[ATT_EOTECH] Then
									g\HasToggledAttachments[ATT_EOTECH] = False
								EndIf
								If g\HasPickedAttachments[ATT_ACOG_SCOPE] And g\CanHaveAttachments[ATT_ACOG_SCOPE] Then
									g\HasToggledAttachments[ATT_ACOG_SCOPE] = True
								EndIf
						End Select
					EndIf
					
				; ~ [Magazine]
					
					MaxMagazineAmount# = 1
					
					If keyHits[2] Then
						g\MagazineAttachments = g\MagazineAttachments + 1
					EndIf
					
					If g\MagazineAttachments > MaxMagazineAmount# Then
						g\MagazineAttachments = 0
					EndIf
					
					Select g\MagazineAttachments
						Case 0 ; ~ No attachments selected (None)
							If g\HasPickedAttachments[ATT_EXT_MAG] And g\CanHaveAttachments[ATT_EXT_MAG] Then
								g\HasToggledAttachments[ATT_EXT_MAG] = False
							EndIf
						Case 1 ; ~ First attachment in the row (Extended Magazine)
							If g\HasPickedAttachments[ATT_EXT_MAG] And g\CanHaveAttachments[ATT_EXT_MAG] Then
								g\HasToggledAttachments[ATT_EXT_MAG] = True
							EndIf
					End Select
					
			;! ~ End
					
				EndIf
				
			;! ~ Attachment Logic
				
				For n = 0 To MaxAttachments - 1
					If g\HasPickedAttachments[n] Then
						If g\CanHaveAttachments[n] Then
							If g\HasToggledAttachments[n] Then
								If (Not g\HasAttachments[n]) Then
									AddAttachment(g,n)
									If AttachmentMenuOpen Then PlaySound_Strict g_I\AttachSFX
								EndIf
							Else
								If g\HasAttachments[n] Then
									RemoveAttachment(g,n)
									If AttachmentMenuOpen Then PlaySound_Strict g_I\DetachSFX
								EndIf
							EndIf
						EndIf
					EndIf
				Next
				
			;! ~ End
				
			EndIf
		EndIf
	Next
	
End Function

Function ApplyScopeMaterial(IsRenderScope%)
	CatchErrors("ApplyAttachmentMaterial")
	
	Local temp#
	Local i%, j%, sf%, b%, t1%, name$
	Local ScopeTex%
	Local g.Guns
	
	For g = Each Guns
		For i = 1 To CountSurfaces(g\obj)
			sf = GetSurface(g\obj,i)
			b = GetSurfaceBrush(sf)
			If b<>0 Then
				For j = 0 To 7
					t1 = GetBrushTexture(b,j)
					If t1<>0 Then
						name$ = StripPath(TextureName(t1))
						If Left(Lower(name),15) = "wpn_p90_dot_alt" Then
							If IsRenderScope Then
								ScopeTex = ScopeTexture
							Else
								ScopeTex = DarkTexture
							EndIf
							BrushTexture b, ScopeTex, 0, j
							PaintSurface sf,b
							DeleteSingleTextureEntryFromCache t1
							Exit
						EndIf
						If name<>"" Then DeleteSingleTextureEntryFromCache t1
					EndIf
				Next
				FreeBrush b
			EndIf
		Next
	Next
	
	CatchErrors("Uncaught (ApplyAttachmentMaterial)")
End Function

Function UpdateAttachmentMaterial(Obj,MatAndTexName$,LettersNumber,IsScope%=False)
	CatchErrors("UpdateAttachmentMaterial")
	
	Local temp#
	Local i%, j%, sf%, b%, t1%, name$
	
	For i = 1 To CountSurfaces(Obj)
		sf = GetSurface(Obj,i)
		b = GetSurfaceBrush(sf)
		If b<>0 Then
			For j = 0 To 7
				t1 = GetBrushTexture(b,j)
				If t1<>0 Then
					name$ = StripPath(TextureName(t1))
					If Left(Lower(name),LettersNumber) = MatAndTexName$ Then
						If IsScope Then
							If ScopeNVG; ~ TODO
								BrushColor b,0,255,0
							Else
								BrushColor b,255,255,255
							EndIf
						EndIf
						DeleteSingleTextureEntryFromCache t1
						Exit
					EndIf
					If name<>"" Then DeleteSingleTextureEntryFromCache t1
				EndIf
			Next
			FreeBrush b
		EndIf
	Next
	
	CatchErrors("Uncaught (UpdateAttachmentMaterial)")
End Function

Function UpdateScope()
	Local g.Guns
	Local ScopeHasCharge%
	
	If FPSfactor > 0.0
		If MouseHit3
			For g = Each Guns
				If (g\HasAttachments[ATT_ACOG_SCOPE] Lor g\HasAttachments[ATT_SPECIAL_SCOPE] And g\name = "emrp") And g\ID = g_I\HoldingGun Then
					If g\ScopeCharge# < ScopeChargeTime# Then
						ScopeHasCharge = True
						Exit
					EndIf
				EndIf
			Next
			If ScopeHasCharge% Then
				ScopeNVG = Not ScopeNVG
				If ScopeNVG
					PlaySound_Strict NVGOnSFX
				Else
					PlaySound_Strict NVGOffSFX
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

Function RenderScope()
	Local n.NPCs
	Local g.Guns
	
	If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
		If (Not ScopeNVG) Then
			CameraFogRange(ScopeCam, CameraFogNear*LightVolume,CameraFogFar*LightVolume)
			CameraRange(ScopeCam, 0.005, Min(CameraFogFar*LightVolume*1.5,32))
		Else
			CameraFogRange(ScopeCam, CameraFogNear*LightVolume,30*LightVolume)
			CameraRange(ScopeCam, 0.005, Min(30*LightVolume*1.5,32))
		EndIf
	Else
		If (Not ScopeNVG) Then
			CameraFogRange(ScopeCam,CameraFogNear,CameraFogFar*3)
		Else
			CameraFogRange(ScopeCam,CameraFogFar*2,CameraFogFar*3)
		EndIf
	EndIf
		
	CameraFogColor(ScopeCam, 0,0,0)
	CameraClsColor ScopeCam,0,0,0
	CameraFogMode ScopeCam,1
	
	ShowEntity ScopeCam
	HideEntity Camera
	
	If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
		Cls
	EndIf
	
	SetBuffer BackBuffer()
	
	If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
		If ScopeNVG
			For n = Each NPCs
				If n\NPCtype = NPC_SCP_966
					ShowEntity n\obj
				EndIf
			Next
		EndIf
		For g = Each Guns
			If g\ID = g_I\HoldingGun Then
				If g\CanHaveAttachments[ATT_ACOG_SCOPE] And g\HasAttachments[ATT_ACOG_SCOPE] Then
					;EntityTexture g\ScopeLenseObj,Charge_Interface,Int((g\ScopeCharge/ScopeChargeTime#)*4),0 ;~ TODO
					If ScopeNVG And Int((g\ScopeCharge/ScopeChargeTime#)*4) >= 3 Then
						If (Not ChannelPlaying(ScopeLowPowerChnSFX)) Then
							ScopeLowPowerChnSFX = PlaySound_Strict(LowBatterySFX[1])
						EndIf
					EndIf
				EndIf
				Exit
			EndIf
		Next
	EndIf
	
	RenderWorld
	
	CopyRect 0,0,128,128,0,0,BackBuffer(),TextureBuffer(ScopeTexture)
	
	ShowEntity Camera
	HideEntity ScopeCam
	
	If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
		If (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 3) = "nvg") Then
			If ScopeNVG
				For n = Each NPCs
					If n\NPCtype = NPC_SCP_966
						HideEntity n\obj
					EndIf
				Next
			EndIf
		EndIf
	EndIf
	
End Function

Function DropGrenade()
	Local n%
	
	For n = SLOT_PRIMARY To MaxItemAmount - 1
		If Inventory[n] <> Null Then
			If Inventory[n]\itemtemplate\tempname = "m67" And g_I\HoldingGun = GUN_M67 Then
				RemoveItem(Inventory[n])
				Exit
			ElseIf Inventory[n]\itemtemplate\tempname = "rgd5" And g_I\HoldingGun = GUN_RGD5 Then
				RemoveItem(Inventory[n])
				Exit
			EndIf
		EndIf
	Next
	
End Function

Function CreateGrenade(x#, y#, z#, pitch#, yaw#, grenadetype%=Grenade_M67)
	Local gr.Grenades = New Grenades
	Local g.Guns
	
	IsPlayerShooting% = True
	
	If grenadetype = Grenade_M67 Then
		gr\obj = LoadMesh_Strict("GFX\Weapons\Models\M67_Thrown.b3d")
	Else
		gr\obj = LoadMesh_Strict("GFX\Weapons\Models\RGD5_Thrown.b3d")
	EndIf
	ScaleEntity gr\obj, 0.012,0.012, 0.012
	PositionEntity gr\obj, x,y,z
	RotateEntity gr\obj, pitch, yaw, 45
	EntityType gr\obj, HIT_GRENADE
	EntityRadius gr\obj, 0.03
	
	If grenadetype = Grenade_M67 Then
		gr\Speed = 0.18
		gr\XSpeed = 0.01
	ElseIf grenadetype = Grenade_RGD5 Then
		gr\Speed = 0.12
		gr\XSpeed = 0.008
	EndIf
	
	gr\Angle = yaw
	
	gr\Gun = g
	
End Function

Function UpdateGrenades()
	Local PrevX#, PrevY#, PrevZ#
	Local pivot%,pvt%,de.Decals,e.Emitters
	Local gr.Grenades
	Local n.NPCs, i%
	Local g.Guns
	
	Local grenadename$
	
	For g = Each Guns
		If g\name = "m67" Then
			grenadename$ = "M67"
		ElseIf g\name = "rgd5" Then
			grenadename$ = "RGD5"
		EndIf
	Next
	
	For gr.Grenades = Each Grenades
		PrevX = EntityX(gr\obj)
		PrevY = EntityY(gr\obj)
		PrevZ = EntityZ(gr\obj)
		If gr\Speed > 0.01 Then
			If CountCollisions(gr\obj) <> 0 Then
				If CollisionNZ(gr\obj, 1) = 0 And gr\Prevfloor = False Then
					
					If CollisionNZ(gr\obj, 1) = 0 Then
						If Abs(CollisionNY(gr\obj, 1)) = 1 Then ; ~ Jump
							RotateEntity gr\obj, FlipAngle(EntityPitch(gr\obj)), EntityYaw(gr\obj), EntityRoll(gr\obj)
						Else
							RotateEntity gr\obj, FlipAngle(EntityPitch(gr\obj)), FlipAngle(EntityYaw(gr\obj)), EntityRoll(gr\obj)
						EndIf
					EndIf
					gr\Speed = gr\Speed*0.65 ; ~ Reducing speed if collided
				Else
					gr\Prevfloor = True
				EndIf
			Else
				If CountCollisions(gr\obj) <> 0 Then
					If Abs(CollisionNY(gr\obj, 1)) = 1 Then ; ~ Friction
						gr\Speed = gr\Speed-0.001*FPSfactor
					EndIf
				EndIf
				MoveEntity gr\obj, 0, 0, gr\Speed*FPSfactor
				TranslateEntity gr\obj, 0, -0.01*FPSfactor, 0
				If EntityPitch(gr\obj) < 90 Then 
					RotateEntity gr\obj, WrapAngle(EntityPitch(gr\obj)+0.8*FPSfactor), EntityYaw(gr\obj), WrapAngle(EntityRoll(gr\obj)+(gr\Speed*24)*FPSfactor)
				Else
					RotateEntity gr\obj, EntityPitch(gr\obj), EntityYaw(gr\obj), WrapAngle(EntityRoll(gr\obj)+(gr\Speed*24)*FPSfactor)
				EndIf
			EndIf
		Else
			If CountCollisions(gr\obj) <> 0 Then 
				AlignToVector(gr\obj, CollisionNX(gr\obj, 1), CollisionNY(gr\obj, 1), CollisionNZ(gr\obj, 1), 2)
				RotateEntity(gr\obj, EntityPitch(gr\obj), EntityYaw(gr\obj), 90)
			EndIf
		EndIf
		If Distance3(PrevX, PrevY, PrevZ, EntityX(gr\obj), EntityY(gr\obj), EntityZ(gr\obj)) <= 0.026 Then
			gr\Speed = gr\Speed-0.001*FPSfactor
		EndIf
		
		gr\Timer = gr\Timer + FPSfactor
		If gr\Timer > 259 Then
			
			Local sounddestiny$
			
			If EntityDistanceSquared(Collider, gr\obj) < PowTwo(5.0) And (EntityVisible(gr\obj, Collider)) Then
				sounddestiny$ = "close"
			ElseIf (EntityDistanceSquared(Collider, gr\obj) > PowTwo(5.0) And EntityDistanceSquared(Collider, gr\obj) < PowTwo(10.0)) Lor (Not EntityVisible(gr\obj, Collider)) Then
				sounddestiny$ = "distant"
			ElseIf EntityDistanceSquared(Collider, gr\obj) > PowTwo(10.0) Then
				sounddestiny$ = "far"
			EndIf
			
			Local currsound$ = PlaySound_Strict(LoadTempSound("SFX\Guns\"+grenadename+"\explosion_"+sounddestiny$+".ogg"))
			Local snd_real = LinePick(EntityX(gr\obj),EntityY(gr\obj),EntityZ(gr\obj), 0, 10, 0)
			Local snd_real_player = LinePick(EntityX(Collider),EntityY(Collider),EntityZ(Collider), 0, 10, 0)
			
			If (Not snd_real) And snd_real_player Then currsound = PlaySound_Strict(LoadTempSound("SFX\Guns\"+grenadename+"\explosion_"+sounddestiny$+".ogg"))
			If (Not snd_real) And (Not snd_real_player) Then currsound = PlaySound_Strict(LoadTempSound("SFX\Guns\"+grenadename+"\explosion_"+sounddestiny$+".ogg"))
			
			If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
				If EntityVisible(Camera, gr\obj) Then
					If EntityDistanceSquared(Collider, gr\obj) < PowTwo(4.0) Then
						If (Not GodMode) Then
							For g = Each Guns
								If g\name = "m67" Then
									DamageSPPlayer(10-EntityDistance(Collider, gr\obj))
								Else
									DamageSPPlayer(15-EntityDistance(Collider, gr\obj))
								EndIf
							Next
							m_msg\DeathTxt = GetLocalStringR("Singleplayer","grenade_death",Designation)
						EndIf
					EndIf
					If EntityDistanceSquared(Collider, gr\obj) < PowTwo(0.5) Then
						If (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") Then
							If (Not GodMode) Then
								Kill()
								m_msg\DeathTxt = GetLocalStringR("Singleplayer","grenade_death",Designation)
							EndIf
						Else
							If hds\Health <= 90 And hds\Health > 89 Lor hds\Health <= 86 And hds\Health > 85 Then
								If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[1])
							EndIf
							If hds\Health <= 50 And hds\Health > 49 Lor hds\Health <= 46 And hds\Health > 45 Then
								If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[2])
							EndIf
							If hds\Health <= 35 And hds\Health > 34 Lor hds\Health <= 30 And hds\Health > 29 Then
								If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[3])
							EndIf
							If hds\Health <= 20 And hds\Health > 19 Lor hds\Health <= 15 And hds\Health > 14 Then
								If ChannelPlaying(hds\SoundCHN) = False Then hds\SoundCHN = PlaySound_Strict(hds\Sound[4])
							EndIf
						EndIf
					ElseIf EntityDistanceSquared(Collider, gr\obj) < PowTwo(1.5) And g\name = "rgd5" Then
						If Rand(5) = 1 Then
							DamageSPPlayer(5-EntityDistance(Collider, gr\obj)) ; ~ Shrapnel
						Else
							PlaySound_Strict(LoadTempSound("SFX\General\BulletMiss.ogg"))
						EndIf
					EndIf
				EndIf
				For n.NPCs = Each NPCs
					If n\NPCtype = NPC_CI Then
						If EntityDistanceSquared(gr\obj,n\Collider) =< PowTwo(1.6) Then
							n\State[0] = CI_TAKE_COVER
						EndIf
					EndIf
					If EntityDistanceSquared(n\Collider,gr\obj) < PowTwo(4.0) Then
						If n\HP > 0 Then
							For g = Each Guns
								If g\name = "m67" Then
									n\HP = n\HP - (60-(EntityDistance(n\Collider, gr\obj))/2)
								Else
									n\HP = n\HP - (80-(EntityDistance(n\Collider, gr\obj))/2)
								EndIf
							Next
						EndIf
					EndIf
				Next
			Else
				For i = 0 To mp_I\MaxPlayers - 1
					If Players[i] <> Null Then
						If EntityVisible(Players[i]\Collider, gr\obj) Then
							If EntityDistance(Players[i]\Collider, gr\obj) < 6 Then
								DamagePlayer(i,50-EntityDistance(Players[i]\Collider, gr\obj),50,5)
							EndIf
							If EntityDistance(Players[i]\Collider, gr\obj) < 0.5 Then
								DamagePlayer(i,100-EntityDistance(Players[i]\Collider, gr\obj),100,0)
							EndIf
						EndIf
					EndIf
				Next
				For n.NPCs = Each NPCs
					If EntityDistanceSquared(n\Collider,gr\obj) < PowTwo(4.0) Then
						If n\HP > 0 Then
							For g = Each Guns
								If g\name = "m67" Then
									n\HP = n\HP - (60-(EntityDistance(n\Collider, gr\obj))/2)
								Else
									n\HP = n\HP - (80-(EntityDistance(n\Collider, gr\obj))/2)
								EndIf
							Next
						EndIf
					EndIf
				Next
			EndIf
			pvt = CreatePivot() 
			PositionEntity pvt, EntityX(gr\obj),EntityY(gr\obj)-0.05,EntityZ(gr\obj)
			TurnEntity pvt, 90, 0, 0
			If EntityPick(pvt,10) <> 0 Then
				de.Decals = CreateDecal(1, PickedX(), PickedY()+0.005, PickedZ(), 90, Rand(360), 0)
				de\Size = Rnd(0.5,0.7) : EntityAlpha(de\obj, 1.0) : ScaleSprite de\obj, de\Size, de\Size
			EndIf
			FreeEntity pvt
			
			If EntityVisible(Collider, gr\obj) Then
				CreateOverHereParticle(EntityX(gr\obj),EntityY(gr\obj),EntityZ(gr\obj),18)
			EndIf
			
			e.Emitters = CreateEmitter(EntityX(gr\obj, True), EntityY(gr\obj, True), EntityZ(gr\obj, True), 5, 25)
			TurnEntity(e\Obj, 90, Rand(180), 0, True)
			e\RandAngle = 5
			e\Speed = 0
			e\SizeChange = 0
			e\Size = 0.5
			e\MinImage = 17
			e\MaxImage = 17
			e\LifeTime = 25
			e\Room = PlayerRoom
			
			BigCameraShake = Max(0, 15-EntityDistance(gr\obj, Collider))
			
			gr\Channel = 0
			
			pivot = CreatePivot()
			PositionEntity pivot, EntityX(gr\obj),EntityY(gr\obj),EntityZ(gr\obj)
			
			PlaySound2(HitSnd[Rand(0,2)],Camera,pivot,(EntityDistance(Camera, pivot)*1000))
			;PlaySound2(HitSnd[Rand(0,2)],Camera,pivot,Max(40000, 44100-(EntityDistance(Camera, pivot)*500)))
			FreeEntity gr\obj
			Delete gr
		EndIf
	Next
	
End Function

;! ~ OLD CODE

;Function RenderAttachments()
;	
;	For g = Each Guns
;		If g_I\HoldingGun = g\ID Then
;			If g\CanSelectMenuAttachments Then
;				SetFont(fo\Font[Font_Default_Large])
;				Text x-160,y-450,GetLocalString("Attachments", "atts")
;				SetFont(fo\Font[Font_Default])
;				Text x-220,y-400,GetLocalStringR("Attachments", "atts_exit",KeyName[KEY_ATTACHMENTS])
;				
;				SetFont(fo\Font[Font_Default])
;				Text x-300,y-300,GetLocalString("Attachments", "att_on")
;				SetFont(fo\Font[Font_Default])
;				Text x-300,y-250,GetLocalString("Attachments", "att_off")
;				
;				Select g_I\HoldingGun
;					Case GUN_FIVESEVEN,GUN_BERETTA,GUN_GLOCK
;						; ~ Suppressor
;						If g\CanHaveAttachments[ATT_SUPPRESSOR] Then
;							DrawFrame(x-520,y-15,width,height)
;							;DrawImage(AttachmentIMG[0],x-520,y-15)
;							If (Not g\HasPickedAttachments[ATT_SUPPRESSOR]) Then
;							;	DrawImage(AttachmentIMG[15],x-520,y-15)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_SUPPRESSOR] Then
;							Color 20,255,20
;							Rect(x-522,y-17,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-520+(width2*i), y-15, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-520+(width2*i))+(width2*0.5), y-15+(height2*0.5), "1", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;					Case GUN_USP
;						; ~ Suppressor
;						If g\CanHaveAttachments[ATT_SUPPRESSOR] Then
;							DrawFrame(x-520,y-15,width,height)
;							;DrawImage(AttachmentIMG[0],x-520,y-15)
;							If (Not g\HasPickedAttachments[ATT_SUPPRESSOR]) Then
;							;	DrawImage(AttachmentIMG[15],x-520,y-15)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_SUPPRESSOR] Then
;							Color 20,255,20
;							Rect(x-522,y-17,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-520+(width2*i), y-15, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-520+(width2*i))+(width2*0.5), y-15+(height2*0.5), "1", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;						
;						; ~ match
;						If g\CanHaveAttachments[ATT_MATCH] Then
;							DrawFrame(x-500,y-180,width,height)
;							;DrawImage(AttachmentIMG[1],x-500,y-180)
;							If (Not g\HasPickedAttachments[ATT_MATCH]) Then
;							;	DrawImage(AttachmentIMG[15],x-500,y-180)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_MATCH] Then
;							Color 20,255,20
;							Rect(x-502,y-182,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-500+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-500+(width2*i))+(width2*0.5), y-180+(height2*0.5), "2", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;					Case GUN_P90
;						; ~ Suppressor
;						If g\CanHaveAttachments[ATT_SUPPRESSOR] Then
;							DrawFrame(x-520,y-15,width,height)
;							;DrawImage(AttachmentIMG[0],x-520,y-15)
;							If (Not g\HasPickedAttachments[ATT_SUPPRESSOR]) Then
;							;	DrawImage(AttachmentIMG[15],x-520,y-15)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_SUPPRESSOR] Then
;							Color 20,255,20
;							Rect(x-522,y-17,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-520+(width2*i), y-15, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-520+(width2*i))+(width2*0.5), y-15+(height2*0.5), "1", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;						
;						; ~ P90 Scope
;						If g\CanHaveAttachments[ATT_SPECIAL_SCOPE] Then
;							DrawFrame(x-500,y-180,width,height)
;							;DrawImage(AttachmentIMG[1],x-500,y-180)
;							If (Not g\HasPickedAttachments[ATT_SPECIAL_SCOPE]) Then
;							;	DrawImage(AttachmentIMG[15],x-500,y-180)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_SPECIAL_SCOPE] Then
;							Color 20,255,20
;							Rect(x-502,y-182,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-500+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-500+(width2*i))+(width2*0.5), y-180+(height2*0.5), "2", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;						
;						; ~ Acog Scope
;						If g\CanHaveAttachments[ATT_ACOG_SCOPE] Then
;							DrawFrame(x-425,y-180,width,height)
;							;DrawImage(AttachmentIMG[1],x-500,y-180)
;							If (Not g\HasPickedAttachments[ATT_ACOG_SCOPE]) Then
;							;	DrawImage(AttachmentIMG[15],x-500,y-180)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_ACOG_SCOPE] Then
;							Color 20,255,20
;							Rect(x-427,y-182,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-425+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-425+(width2*i))+(width2*0.5), y-180+(height2*0.5), "3", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;						
;						; ~ Red Dot Sight
;						If g\CanHaveAttachments[ATT_RED_DOT] Then
;							DrawFrame(x-350,y-180,width,height)
;							;DrawImage(AttachmentIMG[2],x-425,y-180)
;							If (Not g\HasPickedAttachments[ATT_RED_DOT]) Then
;							;	DrawImage(AttachmentIMG[15],x-425,y-180)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_RED_DOT] Then
;							Color 20,255,20
;							Rect(x-352,y-182,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-350+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-350+(width2*i))+(width2*0.5), y-180+(height2*0.5), "4", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;						
;						; ~ EoTech Sight
;						If g\CanHaveAttachments[ATT_EOTECH] Then
;							DrawFrame(x-310,y-180,width,height)
;							;DrawImage(AttachmentIMG[2],x-425,y-180)
;							If (Not g\HasPickedAttachments[ATT_EOTECH]) Then
;							;	DrawImage(AttachmentIMG[15],x-425,y-180)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_EOTECH] Then
;							Color 20,255,20
;							Rect(x-312,y-182,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-310+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-310+(width2*i))+(width2*0.5), y-180+(height2*0.5), "5", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;					Case GUN_M4A1,GUN_RPK16
;						; ~ Suppressor
;						If g\CanHaveAttachments[ATT_SUPPRESSOR] Then
;							DrawFrame(x-520,y-15,width,height)
;						;DrawImage(AttachmentIMG[0],x-520,y-15)
;							If (Not g\HasPickedAttachments[ATT_SUPPRESSOR]) Then
;						;	DrawImage(AttachmentIMG[15],x-520,y-15)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_SUPPRESSOR] Then
;							Color 20,255,20
;							Rect(x-522,y-17,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-520+(width2*i), y-15, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-520+(width2*i))+(width2*0.5), y-15+(height2*0.5), "1", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;						
;						; ~ Red Dot Sight
;						If g\CanHaveAttachments[ATT_RED_DOT] Then
;							DrawFrame(x-500,y-180,width,height)
;							;DrawImage(AttachmentIMG[1],x-500,y-180)
;							If (Not g\HasPickedAttachments[ATT_RED_DOT]) Then
;							;	DrawImage(AttachmentIMG[15],x-500,y-180)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_RED_DOT] Then
;							Color 20,255,20
;							Rect(x-502,y-182,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-500+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-500+(width2*i))+(width2*0.5), y-180+(height2*0.5), "2", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;						
;						; ~ EoTech
;						If g\CanHaveAttachments[ATT_EOTECH] Then
;							DrawFrame(x-425,y-180,width,height)
;							;DrawImage(AttachmentIMG[1],x-500,y-180)
;							If (Not g\HasPickedAttachments[ATT_EOTECH]) Then
;							;	DrawImage(AttachmentIMG[15],x-500,y-180)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_EOTECH] Then
;							Color 20,255,20
;							Rect(x-427,y-182,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-425+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-425+(width2*i))+(width2*0.5), y-180+(height2*0.5), "3", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;						
;						; ~ Extended Magazine
;						If g\CanHaveAttachments[ATT_EXT_MAG] Then
;							DrawFrame(x-460,y-15,width,height)
;						;DrawImage(AttachmentIMG[0],x-460,y-15)
;							If (Not g\HasPickedAttachments[ATT_EXT_MAG]) Then
;						;	DrawImage(AttachmentIMG[15],x-460,y-15)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_EXT_MAG] Then
;							Color 20,255,20
;							Rect(x-462,y-17,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-460+(width2*i), y-15, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-460+(width2*i))+(width2*0.5), y-15+(height2*0.5), "4", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;						
;					Case GUN_EMRP
;						; ~ MUI
;						If g\CanHaveAttachments[ATT_MUI] Then
;							DrawFrame(x-520,y-15,width,height)
;							;DrawImage(AttachmentIMG[0],x-520,y-15)
;							If (Not g\HasPickedAttachments[ATT_MUI]) Then
;							;	DrawImage(AttachmentIMG[15],x-520,y-15)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_MUI] Then
;							Color 20,255,20
;							Rect(x-522,y-17,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-520+(width2*i), y-15, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-520+(width2*i))+(width2*0.5), y-15+(height2*0.5), "1", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;					Default
;						; ~ Suppressor
;						If g\CanHaveAttachments[ATT_SUPPRESSOR] Then
;							DrawFrame(x-520,y-15,width,height)
;						;DrawImage(AttachmentIMG[0],x-520,y-15)
;							If (Not g\HasPickedAttachments[ATT_SUPPRESSOR]) Then
;						;	DrawImage(AttachmentIMG[15],x-520,y-15)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_SUPPRESSOR] Then
;							Color 20,255,20
;							Rect(x-522,y-17,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-520+(width2*i), y-15, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-520+(width2*i))+(width2*0.5), y-15+(height2*0.5), "1", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;						
;						; ~ Acog Scope
;						If g\CanHaveAttachments[ATT_ACOG_SCOPE] Then
;							DrawFrame(x-500,y-180,width,height)
;							;DrawImage(AttachmentIMG[1],x-500,y-180)
;							If (Not g\HasPickedAttachments[ATT_ACOG_SCOPE]) Then
;							;	DrawImage(AttachmentIMG[15],x-500,y-180)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_ACOG_SCOPE] Then
;							Color 20,255,20
;							Rect(x-502,y-182,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-500+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-500+(width2*i))+(width2*0.5), y-180+(height2*0.5), "2", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;						
;						; ~ Red Dot Sight
;						If g\CanHaveAttachments[ATT_RED_DOT] Then
;							DrawFrame(x-425,y-180,width,height)
;							;DrawImage(AttachmentIMG[1],x-500,y-180)
;							If (Not g\HasPickedAttachments[ATT_RED_DOT]) Then
;							;	DrawImage(AttachmentIMG[15],x-500,y-180)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_RED_DOT] Then
;							Color 20,255,20
;							Rect(x-427,y-182,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-425+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-425+(width2*i))+(width2*0.5), y-180+(height2*0.5), "3", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;						
;						; ~ EoTech Sight
;						If g\CanHaveAttachments[ATT_EOTECH] Then
;							DrawFrame(x-385,y-180,width,height)
;							;DrawImage(AttachmentIMG[1],x-500,y-180)
;							If (Not g\HasPickedAttachments[ATT_EOTECH]) Then
;							;	DrawImage(AttachmentIMG[15],x-500,y-180)
;							EndIf
;						EndIf
;						If g\HasAttachments[ATT_EOTECH] Then
;							Color 20,255,20
;							Rect(x-387,y-182,width+4,height+4)
;						EndIf
;						
;						SetFont fo\Font[Font_Default]
;						
;						Color 255,255,255
;						DrawFrame(x-385+(width2*i), y-180, width2, height2, (x Mod 64), (x Mod 64))
;						Text (x-385+(width2*i))+(width2*0.5), y-180+(height2*0.5), "4", 1, 1
;						spacing2# = 10
;						
;						SetFont fo\Font[Font_Default]
;						Color 0,0,0
;				End Select
;			EndIf
;		EndIf
;	Next
;
;End Function

;Function UpdateAttachments()
;	Local g.Guns
;	
;	For g = Each Guns
;		If g_I\HoldingGun = g\ID Then
;			If g\CanSelectMenuAttachments Then
;				If g\CanHaveAttachments[ATT_ACOG_SCOPE] And g\HasAttachments[ATT_ACOG_SCOPE] Then
;					If g_I\HoldingGun = g\ID Then
;						ApplyAttachmentMaterial(g\obj,"wpn_p90_dot_alt",15,True)
;					EndIf
;				EndIf
;				
;				If attopen Then
;					Select g_I\HoldingGun
;						Case GUN_P90
;							If keyHits[0] And g\HasPickedAttachments[ATT_SUPPRESSOR] Then
;								g\HasToggledAttachments[ATT_SUPPRESSOR] = Not g\HasToggledAttachments[ATT_SUPPRESSOR]
;							ElseIf keyHits[1] And (Not g\HasAttachments[ATT_RED_DOT] Lor g\HasAttachments[ATT_ACOG_SCOPE] Lor g\HasAttachments[ATT_EOTECH]) And g\HasPickedAttachments[ATT_SPECIAL_SCOPE] Then
;								g\HasToggledAttachments[ATT_SPECIAL_SCOPE] = Not g\HasToggledAttachments[ATT_SPECIAL_SCOPE]
;							ElseIf keyHits[2] And (Not g\HasAttachments[ATT_RED_DOT] Lor g\HasAttachments[ATT_SPECIAL_SCOPE] Lor g\HasAttachments[ATT_EOTECH]) And g\HasPickedAttachments[ATT_ACOG_SCOPE] Then
;								g\HasToggledAttachments[ATT_ACOG_SCOPE] = Not g\HasToggledAttachments[ATT_ACOG_SCOPE]
;							ElseIf keyHits[3] And (Not g\HasAttachments[ATT_ACOG_SCOPE] Lor g\HasAttachments[ATT_SPECIAL_SCOPE] Lor g\HasAttachments[ATT_EOTECH]) And g\HasPickedAttachments[ATT_RED_DOT] Then
;								g\HasToggledAttachments[ATT_RED_DOT] = Not g\HasToggledAttachments[ATT_RED_DOT]
;							ElseIf keyHits[4] And (Not g\HasAttachments[ATT_ACOG_SCOPE] Lor g\HasAttachments[ATT_SPECIAL_SCOPE] Lor g\HasAttachments[ATT_RED_DOT]) And g\HasPickedAttachments[ATT_EOTECH] Then
;								g\HasToggledAttachments[ATT_EOTECH] = Not g\HasToggledAttachments[ATT_EOTECH]
;							EndIf
;						Case GUN_USP
;							If keyHits[0] And (Not g\HasAttachments[ATT_MATCH]) And g\HasPickedAttachments[ATT_SUPPRESSOR] Then
;								g\HasToggledAttachments[ATT_SUPPRESSOR] = Not g\HasToggledAttachments[ATT_SUPPRESSOR]
;							ElseIf keyHits[1] And (Not g\HasAttachments[ATT_SUPPRESSOR]) And g\HasPickedAttachments[ATT_MATCH] Then
;								g\HasToggledAttachments[ATT_MATCH] = Not g\HasToggledAttachments[ATT_MATCH]
;							EndIf
;						Case GUN_M4A1, GUN_RPK16, GUN_MP5
;							If keyHits[0] And g\HasPickedAttachments[ATT_SUPPRESSOR] Then
;								g\HasToggledAttachments[ATT_SUPPRESSOR] = Not g\HasToggledAttachments[ATT_SUPPRESSOR]
;							ElseIf keyHits[1] And (Not g\HasAttachments[ATT_ACOG_SCOPE] Lor g\HasAttachments[ATT_EOTECH]) And g\HasPickedAttachments[ATT_RED_DOT] Then
;								g\HasToggledAttachments[ATT_RED_DOT] = Not g\HasToggledAttachments[ATT_RED_DOT]
;							ElseIf keyHits[2] And (Not g\HasAttachments[ATT_ACOG_SCOPE] Lor g\HasAttachments[ATT_RED_DOT]) And g\HasPickedAttachments[ATT_EOTECH] Then
;								g\HasToggledAttachments[ATT_EOTECH] = Not g\HasToggledAttachments[ATT_EOTECH]
;							ElseIf keyHits[3] And g\HasPickedAttachments[ATT_EOTECH] Then
;								g\HasToggledAttachments[ATT_EXT_MAG] = Not g\HasToggledAttachments[ATT_EXT_MAG]
;							ElseIf keyHits[4] And (Not g\HasAttachments[ATT_EOTECH] Lor g\HasAttachments[ATT_RED_DOT]) And g\HasPickedAttachments[ATT_ACOG_SCOPE] Then
;								g\HasToggledAttachments[ATT_ACOG_SCOPE] = Not g\HasToggledAttachments[ATT_ACOG_SCOPE]
;							EndIf
;						Default
;							If keyHits[0] And g\HasPickedAttachments[ATT_SUPPRESSOR] Then
;								g\HasToggledAttachments[ATT_SUPPRESSOR] = Not g\HasToggledAttachments[ATT_SUPPRESSOR]
;							ElseIf keyHits[1] And (Not g\HasAttachments[ATT_RED_DOT] Lor g\HasAttachments[ATT_EOTECH]) And g\HasPickedAttachments[ATT_ACOG_SCOPE] Then
;								g\HasToggledAttachments[ATT_ACOG_SCOPE] = Not g\HasToggledAttachments[ATT_ACOG_SCOPE]
;							ElseIf keyHits[2] And (Not g\HasAttachments[ATT_ACOG_SCOPE] Lor g\HasAttachments[ATT_EOTECH]) And g\HasPickedAttachments[ATT_RED_DOT] Then
;								g\HasToggledAttachments[ATT_RED_DOT] = Not g\HasToggledAttachments[ATT_RED_DOT]
;							ElseIf keyHits[3] And (Not g\HasAttachments[ATT_ACOG_SCOPE] Lor g\HasAttachments[ATT_RED_DOT]) And g\HasPickedAttachments[ATT_EOTECH] Then
;								g\HasToggledAttachments[ATT_EOTECH] = Not g\HasToggledAttachments[ATT_EOTECH]
;							EndIf
;					End Select
;				EndIf
;				For n = 0 To MaxAttachments - 1
;					If g\HasPickedAttachments[n] And g\CanHaveAttachments[n] Then
;						If g\HasToggledAttachments[n] Then
;							If (Not g\HasAttachments[n]) Then
;								AddAttachment(g,n)
;								If attopen Then
;									PlaySound_Strict g_I\AttachSFX
;								EndIf
;							EndIf
;						Else
;							If g\HasAttachments[n] Then
;								RemoveAttachment(g,n)
;								If attopen Then
;									PlaySound_Strict g_I\DetachSFX
;								EndIf
;							EndIf
;						EndIf
;					EndIf
;				Next
;			EndIf
;		EndIf
;	Next
;End Function
;~IDEal Editor Parameters:
;~C#Blitz3D TSS