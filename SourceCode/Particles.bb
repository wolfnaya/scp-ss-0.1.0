
Global ParticleTextures%[19] ; TODO this can be shrunk

Type Particles
	Field obj%, pvt%
	Field image%
	
	Field R#, G#, B#, A#, size#
	Field speed#, yspeed#, gravity#
	Field Rchange#, Gchange#, Bchange#, Achange#
	Field SizeChange#
	
	Field lifetime#
	Field NoParticleRotation%
	;Multiplayer variables
	Field sync% = 0
End Type 
	
Function CreateParticle.Particles(x#, y#, z#, image%, size#, gravity# = 1.0, lifetime% = 200)
	CatchErrors("CreateParticle.Particles(" + x + ", " + y + ", " + z + ", " + image + ", " + size + ", " + gravity + ", " + lifetime + ")")
	Local p.Particles = New Particles
	p\lifetime = lifetime
	
	p\obj = CreateSprite()
	PositionEntity(p\obj, x, y, z, True)
	If image <> 10 And image <> 11 Then
		EntityTexture(p\obj, ParticleTextures[image])
	Else
		EntityTexture(p\obj, WaterParticleTexture[image-10])
	EndIf
	RotateEntity(p\obj, 0, 0, Rnd(360)*(Not p\NoParticleRotation))
	EntityFX(p\obj, 1 + 8)
	
	SpriteViewMode (p\obj, 3)
	
	Select image
		Case 0,5,6,8,9,10,12
			EntityBlend(p\obj, 1)
		Case 1,2,3,4,7,13
			EntityBlend(p\obj, 3)
	End Select
	
	p\pvt = CreatePivot()
	PositionEntity(p\pvt, x, y, z, True)
	
	p\image = image
	p\gravity = gravity * 0.004
	p\R = 255 : p\G = 255 : p\B = 255 : p\A = 1.0
	p\size = size
	ScaleSprite(p\obj, p\size, p\size)
	Return p
	CatchErrors("Uncaught: CreateParticle.Particles(" + x + ", " + y + ", " + z + ", " + image + ", " + size + ", " + gravity + ", " + lifetime + ")")
End Function
	
Function UpdateParticles()
	CatchErrors("UpdateParticles()")
	Local p.Particles
	For p.Particles = Each Particles
		MoveEntity(p\pvt, 0, 0, p\speed * FPSfactor)
		If p\gravity <> 0 Then p\yspeed = p\yspeed - p\gravity * FPSfactor
		TranslateEntity(p\pvt, 0, p\yspeed * FPSfactor, 0, True)
		
		PositionEntity(p\obj, EntityX(p\pvt,True), EntityY(p\pvt,True), EntityZ(p\pvt,True), True)
		
		;TurnEntity(p\obj, 0, 0, FPSfactor)
		
		If p\Achange <> 0 Then
			p\A=Min(Max(p\A+p\Achange * FPSfactor,0.0),1.0)
			EntityAlpha(p\obj, p\A)		
		EndIf
		
		If p\SizeChange <> 0 Then 
			p\size= p\size+p\SizeChange * FPSfactor
			ScaleSprite p\obj, p\size, p\size
		EndIf
		
		p\lifetime=p\lifetime-FPSfactor
		If p\lifetime <= 0 Lor p\size < 0.00001 Lor p\A =< 0 Then
			RemoveParticle(p)
		End If
	Next
	CatchErrors("Uncaught: UpdateParticles()")
End Function

Function RemoveParticle(p.Particles)
	CatchErrors("RemoveParticle")
	
	p\obj = FreeEntity_Strict(p\obj)
	p\pvt = FreeEntity_Strict(p\pvt)	
	Delete p
	
	CatchErrors("Uncaught: RemoveParticle")
End Function

Global HissSFX% = LoadSound_Strict("SFX\General\Hiss.ogg")
Global SmokeDelay# = 0.0
Global UpdateBiggerParticles% = False

Type Emitters
	Field Obj%
	Field Size#
	Field MinImage%, MaxImage%
	Field Gravity#
	Field LifeTime%
	Field Disable%
	Field Room.Rooms
	Field SoundCHN%
	Field Speed#, RandAngle#
	Field SizeChange#, Achange#
	Field emittertype%
	Field NoParticleRotation%
	Field map_generated% = False
	Field Emitterlifetime#
End Type 

Function UpdateEmitters()
	CatchErrors("UpdateEmitters()")
	Local InSmoke% = False
	Local e.Emitters,i
	Local updateEmitter%
	For e.Emitters = Each Emitters
		If e\Disable = True Then 
			updateEmitter = False
		ElseIf gopt\GameMode = GAMEMODE_MULTIPLAYER Then
			updateEmitter = (EntityDistanceSquared(e\Obj, Players[mp_I\PlayerID]\Collider) < PowTwo(16))
		Else
			updateEmitter = (PlayerRoom = e\Room Lor e\Room\dist < 8)
		EndIf
		If FPSfactor > 0 And updateEmitter Then
			;If ParticleAmount = 2 Lor SmokeDelay#=0.0
			If e\emittertype=2
				If UpdateBiggerParticles
					If Rand(3)=1
						Local p.Particles = CreateParticle(EntityX(e\Obj, True), EntityY(e\Obj, True), EntityZ(e\Obj, True), 0, e\Size, e\Gravity, e\LifeTime)
					Else
						p.Particles = CreateParticle(EntityX(e\Obj, True), EntityY(e\Obj, True), EntityZ(e\Obj, True), 6, e\Size, e\Gravity, e\LifeTime)
					EndIf
					p\speed = e\Speed
					RotateEntity(p\pvt, EntityPitch(e\Obj, True), EntityYaw(e\Obj, True), EntityRoll(e\Obj, True), True)
					
					EntityColor p\obj,10,103,11
					
					TurnEntity(p\pvt, Rnd(-e\RandAngle, e\RandAngle), Rnd(-e\RandAngle, e\RandAngle), 0)
					
					TurnEntity p\obj, 0,0,Rnd(360)
					
					p\SizeChange = e\SizeChange
					
					p\Achange = e\Achange
				EndIf
			ElseIf e\emittertype=3
				If UpdateBiggerParticles
					p.Particles = CreateParticle(EntityX(e\Obj,True),EntityY(e\Obj,True)+Rnd(-0.025,0.025),EntityZ(e\Obj,True),Rand(e\MinImage,e\MaxImage),e\Size,e\Gravity,e\LifeTime)
					p\speed = e\Speed
					RotateEntity(p\pvt, EntityPitch(e\Obj, True), EntityYaw(e\Obj, True), EntityRoll(e\Obj, True), True)
					
					EntityColor p\obj,20,225,30
					SpriteViewMode p\obj,2
					EntityFX p\obj,16
					RotateEntity p\obj,0,EntityYaw(p\pvt),EntityRoll(p\obj)
					
					TurnEntity(p\pvt, Rnd(-e\RandAngle, e\RandAngle), Rnd(-e\RandAngle, e\RandAngle), 0)
					
					TurnEntity p\obj, 0,0,Rnd(360)
					
					p\SizeChange = e\SizeChange
					
					p\Achange = e\Achange
					
					If ParticleAmount=2
						If Rand(2)=1
							Local pvt% = CreatePivot()
							PositionEntity(pvt, EntityX(p\pvt,True)+Rnd(-0.025,0.025), EntityY(p\pvt,True)+Rnd(-5.0,0.1), EntityZ(p\pvt,True)+Rnd(-0.025,0.025))
							RotateEntity(pvt, 0, Rnd(360), 0)
							
							p.Particles = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), 2, 0.002, 0, 300)
							p\speed = 0.0025
							RotateEntity(p\pvt, Rnd(-20, 20), Rnd(360), 0)
							
							p\SizeChange = -0.00001
							p\size = 0.01
							ScaleSprite p\obj,p\size,p\size
							
							p\Achange = -0.01
							
							EntityColor p\obj,20,225,30
							
							pvt = FreeEntity_Strict(pvt)
						EndIf
					EndIf
				EndIf
			Else
				p.Particles = CreateParticle(EntityX(e\Obj, True), EntityY(e\Obj, True), EntityZ(e\Obj, True), Rand(e\MinImage, e\MaxImage), e\Size, e\Gravity, e\LifeTime)
				p\speed = e\Speed
				RotateEntity(p\pvt, EntityPitch(e\Obj, True), EntityYaw(e\Obj, True), EntityRoll(e\Obj, True), True)
				
				TurnEntity(p\pvt, Rnd(-e\RandAngle, e\RandAngle), Rnd(-e\RandAngle, e\RandAngle), 0)
				p\NoParticleRotation=e\NoParticleRotation
				TurnEntity p\obj, 0,0,Rnd(360)*(Not e\NoParticleRotation)
				
				p\SizeChange = e\SizeChange
				
				p\Achange = e\Achange
			EndIf
			
			; ~ Sound
			
			If e\emittertype<>1 And e\emittertype<>2 And e\emittertype<>3 And e\emittertype<>4 And e\emittertype<>5
				e\SoundCHN = LoopSound2(HissSFX, e\SoundCHN, Camera, e\Obj)
			ElseIf e\emittertype = 4 Then
				e\SoundCHN = LoopSound2(WaterHissSFX,e\SoundCHN,Camera,e\Obj)
			EndIf
			
			; ~ Grenade Emitter Lifetime
			
			If e\emittertype = 5 Then
				If e\Emitterlifetime <> 0 Then
					e\Emitterlifetime = e\Emitterlifetime-FPSfactor
					If e\Emitterlifetime < 1 Then
						FreeEntity e\Obj
						Delete e
						Return
					EndIf
				EndIf
			EndIf
			
			If InSmoke = False Then
				If e\emittertype<> 4 Then
					If (Not mpl\HasNTFGasmask) And (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 3) = "hds") And (Not Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask") And (Not Inventory[SLOT_TORSO] <> Null And Left(Inventory[SLOT_TORSO]\itemtemplate\tempname, 6) = "hazmat") Then
						If DistanceSquared(EntityX(Camera, True), EntityX(e\Obj, True), EntityZ(Camera, True), EntityZ(e\Obj, True)) < PowTwo(0.8) Then
							If Abs(EntityY(Camera, True)-EntityY(e\Obj,True))<5.0 Then InSmoke = True
						EndIf
					EndIf	
				EndIf
			EndIf
		EndIf
	Next
	
	If InSmoke Then
		If EyeIrritation > (70 * 6) Then BlurVolume = Max(BlurVolume, (EyeIrritation - (70 * 6)) / (70.0 * 24.0))
		If EyeIrritation > (70 * 24) Then 
			m_msg\DeathTxt = GetLocalStringR("Singleplayer", "smoke_death", Designation)
			psp\Health = Max(psp\Health - 0.25,0.0)
		EndIf
		
		If KillTimer => 0 Then 
			If Rand(150) = 1 Then
				If CoughCHN = 0 Then
					CoughCHN = PlaySound_Strict(CoughSFX[Rand(0, 2)])
				Else
					If Not ChannelPlaying(CoughCHN) Then CoughCHN = PlaySound_Strict(CoughSFX[Rand(0, 2)])
				End If
			EndIf
		EndIf
		
		EyeIrritation = Min(EyeIrritation + FPSfactor * 4.0, 25 * 70)
	Else
		If BlinkTimer < 0 Then
			EyeIrritation = Max(0.0, EyeIrritation - FPSfactor * 4.0)
		Else
			EyeIrritation = Max(0.0, EyeIrritation - FPSfactor * 2.0)
		EndIf
	EndIf
	CatchErrors("Uncaught: UpdateEmitters()")
End Function 

Function CreateEmitter.Emitters(x#, y#, z#, emittertype%, lifetime# = 200)
	CatchErrors("CreateEmitter.Emitters(" + x + ", " + y + ", " + z + ", " + emittertype + ")")
	Local e.Emitters = New Emitters
	
	e\Obj = CreatePivot()
	NameEntity e\Obj,"Emitter1"
	PositionEntity(e\Obj, x, y, z, True)
	
	e\emittertype = emittertype
	Select emittertype
		Case 0 ; ~ Smoke
			e\Size = 0.03
			e\Gravity = -0.2
			e\LifeTime = 200
			e\SizeChange = 0.005
			e\Speed = 0.004
			e\RandAngle = 20
			e\Achange = -0.008
		Case 1,2,3 ; ~ White Smoke
			e\Size = 0.03
			e\Gravity = -0.2
			e\LifeTime = 200
			e\SizeChange = 0.008
			e\Speed = 0.004
			e\RandAngle = 40
			e\Achange = -0.01
			
			e\MinImage = 6 : e\MaxImage = 6	
		Case 4 ; ~ Water
			e\RandAngle = 5
			e\Speed = 0.01
			e\SizeChange = 0.05
			e\Achange = -0.01
			e\Gravity = 1.0
			e\MinImage = 12 : e\MaxImage = 12
			e\NoParticleRotation = 1
			e\LifeTime = 20
		Case 5 ; ~ Explosion
			e\RandAngle = 5
			e\Speed = 0
			e\SizeChange = 0
			e\Size = 1.5
			e\MinImage = 17
			e\MaxImage = 17
			e\LifeTime = 30
		Case 6 ; ~ Fire
			e\RandAngle = 5
			e\Speed = 0
			e\SizeChange = 0
			e\Size = 1.5
			e\MinImage = 13
			e\MaxImage = 13
			e\LifeTime = 30
	End Select
	
	e\Emitterlifetime = lifetime
	e\LifeTime = lifetime
	
	For r.Rooms = Each Rooms
		If Abs(EntityX(e\Obj) - EntityX(r\obj)) < 4.0 And Abs(EntityZ(e\Obj) - EntityZ(r\obj)) < 4.0 Then
			e\Room = r
		EndIf
	Next
	
	Return e
	CatchErrors("Uncaught: CreateEmitter.Emitters(" + x + ", " + y + ", " + z + ", " + emittertype + ")")
End Function

Function RemoveEmitter(em.Emitters)
	CatchErrors("RemoveEmitter")
	
	em\Obj = FreeEntity_Strict(em\Obj)
	Delete em
	
	CatchErrors("Uncaught: RemoveEmitter")
End Function

Const PARTICLEMP_SHOT = 0
Const PARTICLEMP_WALL = 1
Const PARTICLEMP_035 = 2

Type ParticleMP
	Field ptype%
	Field pos.Vector3D
	Field rot.Vector2D
	Field npos.Vector3D
	Field playerID%
End Type

Function CreateParticleMP.ParticleMP(ptype%,x#,y#,z#,pitch#,yaw#,nx#,ny#,nz#,playerID%)
	CatchErrors("CreateParticleMP.ParticleMP")
	Local pmp.ParticleMP
	Local p.Particles, de.Decals, g.Guns
	Local i%
	
	pmp = New ParticleMP
	pmp\ptype = ptype
	pmp\pos = CreateVector3D(x,y,z)
	pmp\rot = CreateVector2D(pitch,yaw)
	pmp\npos = CreateVector3D(nx,ny,nz)
	pmp\playerID = playerID
	
	Select ptype
		Case PARTICLEMP_SHOT
			p.Particles = CreateParticle(x,y,z, 5, 0.06, 0.2, 80)
			p\speed = 0.001
			p\SizeChange = 0.003
			p\A = 0.8
			p\Achange = -0.02
			
			For g = Each Guns
				If g\ID = Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot] And g\GunType = GUNTYPE_MELEE Then
					If mp_I\PlayerID = playerID Then
						PlayGunSound(g\name+"\hitbody",g\MaxShootSounds,0,True,True)
					Else
						If g\MaxShootSounds = 1 Then
							Players[playerID]\GunSFXChannel[GUN_CHANNEL_SHOT] = PlaySound2(LoadTempSound("SFX\Guns\"+g\name$+"\hitbody.ogg"),Camera,Players[playerID]\Collider,5)
						Else
							Players[playerID]\GunSFXChannel[GUN_CHANNEL_SHOT] = PlaySound2(LoadTempSound("SFX\Guns\"+g\name$+"\hitbody"+Rand(1,g\MaxShootSounds)+".ogg"),Camera,Players[playerID]\Collider,5)
						EndIf
					EndIf
					Exit
				EndIf
			Next
		Case PARTICLEMP_WALL
			p.Particles = CreateParticle(x,y,z, 0, 0.03, 0, 80)
			p\speed = 0.001
			p\SizeChange = 0.003
			p\A = 0.8
			p\Achange = -0.01
			RotateEntity p\pvt, pitch-180, yaw,0
			
			For i = 0 To Rand(2,3)
				p.Particles = CreateParticle(x,y,z, 0, 0.006, 0.003, 80)
				p\speed = 0.02
				p\A = 0.8
				p\Achange = -0.01
				RotateEntity p\pvt, pitch+Rnd(170,190), yaw+Rnd(-10,10),0	
			Next
			
			Local DecalID% = 0
			For g = Each Guns
				If g\ID = Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot] Then
					Select g\DecalType
						Case GUNDECAL_DEFAULT
							DecalID = DECAL_TYPE_BULLETHOLE
						Case GUNDECAL_SLASH
							DecalID = DECAL_TYPE_SLASHHOLE
						Case GUNDECAL_SMASH
							DecalID = DECAL_TYPE_SMASHHOLE
					End Select
					Exit
				EndIf
			Next
			
			de.Decals = CreateDecal(GetRandomDecalID(DecalID), x,y,z, 0,0,0)
			AlignToVector de\obj,nx,ny,nz,3
			MoveEntity de\obj, 0,0,-0.001
			EntityFX de\obj, 1+8
			de\lifetime = 70*20
			EntityBlend de\obj, 2
			de\Size = Rnd(0.028,0.034)
			de\blendmode = 2
			ScaleSprite de\obj, de\Size, de\Size
			
			For g.Guns = Each Guns
				If g\ID = Players[playerID]\WeaponInSlot[Players[playerID]\SelectedSlot] And g\GunType = GUNTYPE_MELEE Then
					If mp_I\PlayerID = playerID Then
						PlayGunSound(g\name+"\hitwall",g\MaxWallhitSounds,0,True,True)
					Else
						If g\MaxWallhitSounds = 1 Then
							Players[playerID]\GunSFXChannel[GUN_CHANNEL_SHOT] = PlaySound2(LoadTempSound("SFX\Guns\"+g\name$+"\hitwall.ogg"),Camera,Players[playerID]\Collider,5)
						Else
							Players[playerID]\GunSFXChannel[GUN_CHANNEL_SHOT] = PlaySound2(LoadTempSound("SFX\Guns\"+g\name$+"\hitwall"+Rand(1,g\MaxWallhitSounds)+".ogg"),Camera,Players[playerID]\Collider,5)
						EndIf
					EndIf
					Exit
				EndIf
			Next
		Case PARTICLEMP_035
			de.Decals = CreateDecal(DECAL_035, x, y, z, 90, 0, Rnd(360))
			de\Size = 0.2 : ScaleSprite(de\obj, de\Size, de\Size) : de\SizeChange = 0.005 : de\MaxSize = 0.6 : UpdateDecals()
	End Select
	
	Return pmp
	CatchErrors("Uncaught: CreateParticleMP.ParticleMP")
End Function

Function UpdateDust()
	CatchErrors("UpdateDust")
	Local p.Particles
	Local i%, Pvt%
	
	If ParticleAmount > 0 Then
		; ~ Create a single dust particle
		If Rand(35 + (35 * (ParticleAmount = 1))) = 1 Then
			Pvt = CreatePivot()
			
			PositionEntity(Pvt, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True))
			RotateEntity(Pvt, 0.0, Rnd(360.0), 0.0)
			If Rand(2) = 1 Then
				MoveEntity(Pvt, 0.0, Rnd(-0.5, 0.5), Rnd(0.5, 1.0))
			Else
				MoveEntity(Pvt, 0.0, Rnd(-0.5, 0.5), Rnd(0.5, 1.0))
			EndIf
			
			p.Particles = CreateParticle(EntityX(Pvt), EntityY(Pvt), EntityZ(Pvt),2, 0.002, 0.0, 300.0)
			p\speed = 0.001 : p\SizeChange = -0.00001
			RotateEntity(p\pvt, Rnd(-20.0, 20.0), Rnd(360.0), 0.0)
			FreeEntity(Pvt)
		EndIf
		
		; ~ Create extra dust particles while the camera is shaking
		If BigCameraShake > 0.0 Then
			For i = 0 To 5 + (5 * (ParticleAmount - 1))
				Pvt = CreatePivot()
				PositionEntity(Pvt, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True))
				RotateEntity(Pvt, 0.0, Rnd(360.0), 0.0)
				If Rand(2) = 1 Then
					MoveEntity(Pvt, 0.0, Rnd(-0.5, 0.5), Rnd(0.5, 1.0))
				Else
					MoveEntity(Pvt, 0.0, Rnd(-0.5, 0.5), Rnd(0.5, 1.0))
				EndIf
				
				p.Particles = CreateParticle(EntityX(Pvt), EntityY(Pvt), EntityZ(Pvt),2, 0.002, 0.0, 300.0)
				p\speed = 0.001 : p\SizeChange = -0.00001
				RotateEntity(p\pvt, Rnd(-20.0, 20.0), Rnd(360.0), 0.0)
				FreeEntity(Pvt)
			Next
		EndIf
	EndIf
	CatchErrors("Uncaught: UpdateDust")
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D