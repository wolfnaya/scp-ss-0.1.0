
Function FillRoom_Testmap(r.Rooms)
	;Local cs.Cutscene, crp.CutsceneReferencePoint, Start.CutsceneReferencePoint, EndP.CutsceneReferencePoint
	
	;Needs to be initialized backwards, LOL XD
;	crp = CreateCutsceneReferencePoint(r, CreateVector3D(r\x - 100.0*RoomScale, r\y + 300.0*RoomScale, r\z + 100.0*RoomScale), 200.0, 0.0, 0.002, False, Null, Null, 0.001, 100.0)
;	crp = CreateCutsceneReferencePoint(r, CreateVector3D(r\x + 500.0*RoomScale, r\y + 300.0*RoomScale, r\z - 500.0*RoomScale), 200.0, 0.0, 0.002, False, crp, Null, 0.001, 100.0)
;	crp = CreateCutsceneReferencePoint(r, CreateVector3D(r\x + 400.0*RoomScale, r\y + 200.0*RoomScale, r\z), 200.0, 70 * 3, 0.002, False, crp, CreateVector3D(0, 180, 0), 0.001, 0.01)
;	Start = CreateCutsceneReferencePoint(r, CreateVector3D(r\x, r\y + 200.0*RoomScale, r\z), 200.0, 0.0, 0.002, False, crp, CreateVector3D(0, 180, 0), 0.001, 0.01)
	Local em.Emitters = CreateEmitter(r\x, r\y+350.0 * RoomScale, r\z-350.0 * RoomScale, 4)
	TurnEntity(em\Obj, 90, 0, 0, True)
	EntityParent(em\Obj, r\obj)
	em.Emitters = CreateEmitter(r\x+350.0 * RoomScale, r\y+350.0 * RoomScale, r\z-350.0 * RoomScale, 4)
	TurnEntity(em\Obj, 90, 0, 0, True)
	EntityParent(em\Obj, r\obj)
	em.Emitters = CreateEmitter(r\x, r\y+350.0 * RoomScale, r\z-700.0 * RoomScale, 4)
	TurnEntity(em\Obj, 90, 0, 0, True)
	EntityParent(em\Obj, r\obj)
;	CreateCutscene("Testmap_TestCutscene", Start)
	
;	r\Objects[0] = LoadMesh_Strict("GFX\puddle.b3d");LoadMesh_Strict("GFX\map\forest\detail\rock.b3d")
;	ScaleEntity r\Objects[0],1,0.01,1
;	PositionEntity r\Objects[0],r\x,r\y+0.005,r\z,True
;	EntityParent r\Objects[0],r\obj
	;EntityAlpha r\Objects[0],0.33
	
	;[Block]
;	Local Texture=CreateTextureUsingCacheSystem(512,512,1+128+256)
;	TextureBlend Texture,3
;	Local Cam=CreateCamera()
;	CameraFogMode Cam,1
;	CameraFogRange Cam,5,10
;	CameraRange Cam,0.01,20
;	;CameraClsMode Cam,False,True
;	CameraProjMode Cam,0
;	CameraViewport Cam,0,0,512,512
;	PositionEntity Cam,EntityX(r\Objects[0]),EntityY(r\Objects[0])+0.005,EntityZ(r\Objects[0])
;	
;	Local tex_sz=TextureWidth(Texture);*3
;	;ScaleTexture Texture,3.0,3.0
;	CameraProjMode Camera,0
;	CameraProjMode Cam,1
;	
;	CameraFogRange Cam,0.1,3
;	CameraFogColor Cam,0,0,0
;	
;	MoveEntity Cam,0,-1000,0
;	MoveEntity r\obj,0,-1000,0
;	
;	SetCubeMode Texture,1
;	
;	;do left view	
;	SetCubeFace Texture,0
;	RotateEntity Cam,0,90,0
;	RenderWorld
;	CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(Texture)
;	;do forward view
;	SetCubeFace Texture,1
;	RotateEntity Cam,0,0,0
;	RenderWorld
;	CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(Texture)
;	;do right view	
;	SetCubeFace Texture,2
;	RotateEntity Cam,0,-90,0
;	RenderWorld
;	CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(Texture)
;	;do backward view
;	SetCubeFace Texture,3
;	RotateEntity Cam,0,180,0
;	RenderWorld
;	CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(Texture)
;	;do up view
;	SetCubeFace Texture,4
;	RotateEntity Cam,-90,0,0
;	RenderWorld
;	CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(Texture)
;	
;	CameraProjMode Camera,1
;	CameraProjMode Cam,0
;	
;	EntityTexture r\Objects[0],Texture
;	DeleteSingleTextureEntryFromCache(Texture)
;	MoveEntity r\obj,0,1000,0
	;[End Block]
	
;	CreateCubeMap.CubeMap("puddle",1,True,r\x,r\y,r\z,5.0,512)
;	RenderCubeMap(r\Objects[0],"puddle")
	r\Levers[0]=CreateLever(r,r\x,r\y+50*RoomScale,r\z,90)
End Function

Function UpdateEvent_Testmap(e.Events)
	Local em.Emitters
	If e\room = PlayerRoom Then
		e\eventstate[0] = UpdateLever(e\room\Levers[0]\obj)
;		EntityShininess e\room\Objects[0],1.0
;		UpdateCubeMap(e\room\Objects[0],"puddle")
		For em = Each Emitters
			em\Disable = (Not e\eventstate[0])
		Next	
	EndIf
End Function



;~IDEal Editor Parameters:
;~C#Blitz3D