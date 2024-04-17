
;[Block]
;General Constants
Const MAX_DECALS% = 24

;035 Decals
Const DECAL_035% = 22

;173 Decals
Const DECAL_173BLOOD1% = 4
Const DECAL_173BLOOD2% = 5
Const DECAL_173BLOOD3% = 6

;1079 Decals
Const DECAL_1079% = 23

;Blood Decals
Const DECAL_BLOODSPLAT1% = 2
Const DECAL_BLOODSPLAT2% = 3
Const DECAL_BLOODDROP1% = 15
Const DECAL_BLOODDROP2% = 16
Const DECAL_BLOODPOOL% = 17
Const DECAL_FOAM% = 21

;Crystal Decal
Const DECAL_CRYSTAL% = 19

;Map Decals
Const DECAL_DECAY% = 0
Const DECAL_CRACKS% = 1

;Paperstrip Decal
Const DECAL_PAPERSTRIPS% = 7

;Pocket Dimension Decals
Const DECAL_PD1% = 8
Const DECAL_PD2% = 9
Const DECAL_PD3% = 10
Const DECAL_PD4% = 11
Const DECAL_PD5% = 12
Const DECAL_PD6% = 18

;Weapon Decals
Const DECAL_BULLETHOLE1% = 13
Const DECAL_BULLETHOLE2% = 14
Const DECAL_SLASHHOLE% = 20
Const DECAL_SMASHHOLE% = 21

;Decal Types
Const DECAL_TYPE_BULLETHOLE% = 0
Const DECAL_TYPE_SLASHHOLE% = 1
Const DECAL_TYPE_SMASHHOLE% = 2
Const DECAL_TYPE_BLOODSPLAT% = 3
Const DECAL_TYPE_BLOODDROP% = 4
;[End Block]

Global DecalTextures%[MAX_DECALS]

Type Decals
	Field obj%
	Field SizeChange#, Size#, MaxSize#
	Field AlphaChange#, Alpha#
	Field blendmode%
	Field fx%
	Field ID%
	Field Timer#
	
	Field lifetime#
	
	Field x#, y#, z#
	Field pitch#, yaw#, roll#
	
	;Multiplayer variables
	Field sync% = 0
End Type

Function GetRandomDecalID%(decaltype%)
	
	Select decaltype
		Case DECAL_TYPE_BULLETHOLE
			Return Rand(DECAL_BULLETHOLE1, DECAL_BULLETHOLE2)
		Case DECAL_TYPE_SLASHHOLE
			Return DECAL_SLASHHOLE
		Case DECAL_TYPE_SMASHHOLE
			Return DECAL_SMASHHOLE
		Case DECAL_TYPE_BLOODSPLAT
			Return Rand(DECAL_BLOODSPLAT1, DECAL_BLOODSPLAT2)
		Case DECAL_TYPE_BLOODDROP
			Return Rand(DECAL_BLOODDROP1, DECAL_BLOODDROP2)
	End Select
	
End Function

Function CreateDecal.Decals(id%, x#, y#, z#, pitch#, yaw#, roll#)
	Local d.Decals = New Decals
	
	d\x = x
	d\y = y
	d\z = z
	d\pitch = pitch
	d\yaw = yaw
	d\roll = roll
	
	d\MaxSize = 1.0
	
	d\Alpha = 1.0
	d\Size = 1.0
	d\obj = CreateSprite()
	d\blendmode = 1
	
	EntityTexture(d\obj, DecalTextures[id])
	EntityFX(d\obj, 0)
	SpriteViewMode(d\obj, 2)
	PositionEntity(d\obj, x, y, z)
	RotateEntity(d\obj, pitch, yaw, roll)
	
	d\ID = id
	
	If DecalTextures[id] = 0 Lor d\obj = 0 Then Return Null
	
	Return d
End Function

Function LoadDecals(isMultiplayer% = False)
	Local i%
	
	If (Not isMultiplayer) Then
		For i = DECAL_DECAY To DECAL_173BLOOD3
			DecalTextures[i] = LoadTexture_Strict("GFX\decal"+(i+1)+".png", 1+2, 2)
		Next
		DecalTextures[DECAL_PAPERSTRIPS] = LoadTexture_Strict("GFX\items\Icons\Icon_paper_strips.jpg", 1+2, 2)
		For i = DECAL_PD1 To DECAL_PD5
			DecalTextures[i] = LoadTexture_Strict("GFX\decalpd"+(i-7)+".jpg", 1+2, 2)
		Next
		DrawLoading(22,False,"","Decals")
		For i = DECAL_BLOODDROP1 To DECAL_BLOODDROP2
			DecalTextures[i] = LoadTexture_Strict("GFX\blooddrop"+(i-14)+".png", 1+2, 2)
		Next
		DecalTextures[DECAL_BLOODPOOL] = LoadTexture_Strict("GFX\decal8.png", 1+2, 2)
		DecalTextures[DECAL_PD6] = LoadTexture_Strict("GFX\decalpd6.dc", 1+2, 2)
		DrawLoading(24,False,"","Decals")
		DecalTextures[DECAL_CRYSTAL] = LoadTexture_Strict("GFX\decal19.png", 1+2, 2)
		DecalTextures[DECAL_FOAM] = LoadTexture_Strict("GFX\decal427.png", 1+2, 2)
		DecalTextures[DECAL_1079] = LoadTexture_Strict("GFX\decal1079.png", 1+2, 2)
	Else
		DecalTextures[DECAL_035] = LoadTexture_Strict("GFX\decal035.png", 1+2, 2)
	EndIf
	For i = DECAL_BULLETHOLE1 To DECAL_BULLETHOLE2
		DecalTextures[i] = LoadTexture_Strict("GFX\bullethole"+(i-12)+".jpg", 1+2, 2)
	Next
	DecalTextures[DECAL_SLASHHOLE] = LoadTexture_Strict("GFX\slashhole.jpg", 1+2, 2)
	DecalTextures[DECAL_SMASHHOLE] = LoadTexture_Strict("GFX\smashhole.jpg", 1+2, 2)
	
End Function

Function UpdateDecals()
	Local d.Decals
	For d.Decals = Each Decals
		If d\SizeChange <> 0 Then
			d\Size=d\Size + d\SizeChange * FPSfactor
			ScaleSprite(d\obj, d\Size, d\Size)
			
			Select d\ID
				Case DECAL_DECAY
					If d\Timer <= 0 Then
						Local angle# = Rand(360)
						Local temp# = Rnd(d\Size)
						Local d2.Decals = CreateDecal(DECAL_CRACKS, EntityX(d\obj) + Cos(angle) * temp, EntityY(d\obj) - 0.0005, EntityZ(d\obj) + Sin(angle) * temp, EntityPitch(d\obj), Rnd(360), EntityRoll(d\obj))
						d2\Size = Rnd(0.1, 0.5) : ScaleSprite(d2\obj, d2\Size, d2\Size)
						PlaySound2(DecaySFX[Rand(1, 3)], Camera, d2\obj, 10.0, Rnd(0.1, 0.5))
						;d\Timer = d\Timer + Rand(50,150)
						d\Timer = Rand(50, 100)
					Else
						d\Timer= d\Timer-FPSfactor
					End If
				;Case 6
				;	EntityBlend d\obj, 2
			End Select
			
			If d\Size >= d\MaxSize Then d\SizeChange = 0 : d\Size = d\MaxSize
		End If
		
		If d\AlphaChange <> 0 Then
			d\Alpha = Min(d\Alpha + FPSfactor * d\AlphaChange, 1.0)
			EntityAlpha(d\obj, d\Alpha)
		End If
		
		If d\lifetime > 0 Then
			d\lifetime=Max(d\lifetime-FPSfactor,5)
		EndIf
		
		If d\Size <= 0 Lor d\Alpha <= 0 Lor d\lifetime=5.0  Then
			d\obj = FreeEntity_Strict(d\obj)
			Delete d
		End If
	Next
End Function

;~IDEal Editor Parameters:
;~F#3A
;~C#Blitz3D