Const VERSION$ = "1.0"
AppTitle "RMESHer v"+VERSION

Graphics3D(1280,720,0,2)
EnableDragDrop(SystemProperty("AppHwnd"),True) ;turning on Drag-Drops!

SetBuffer BackBuffer()
InitErrorMsgs(2)
SetErrorMsg 0, "An error occurred in 3D viewer! Please restart the application."
CatchErrors("Launch")

ChangeDir("..")

SetFont LoadFont("GFX\font\Courier New.ttf")

Const FIF_BMP = 0
Const FIF_PNG = 13

Global s$,s2$

Type OptionData
	Field Booleans%
End Type
InitOptions()

Type World
	Field Camera%
	Field Room%
End Type
InitWorld()
Const RoomScale# = 8.0 / 2048.0
Const MapTexturesFolder$ = "GFX\map\textures\"
Repeat
	CheckOptions()
	Cls
	
	RenderWorld
	Test()
	Flip 1
Until KeyHit(1)
EnableDragDrop(SystemProperty("AppHwnd"),False)
End

Function InitOptions()
	Local o.OptionData = New OptionData
	
	o\Booleans = %11110
	
End Function

Function InitWorld()
	Local w.World = New World
	
	w\Camera = CreateCamera()
	CameraViewport w\Camera,0,0,GraphicsWidth(),GraphicsHeight()
	
End Function

Function CheckOptions()
	CatchErrors("CheckOptions")
	Local o.OptionData = First OptionData
	Local w.World = First World
	Local f%
	Local mesh%
	Local dragged$
	
	dragged=GetDragDrop()
	If dragged<>"" Then
		If FileExtension(dragged)="b3d" Or FileExtension(dragged)="rmesh" Then
			s = dragged
		EndIf	
	EndIf	
	
	If KeyHit(63) Then
		s = RequestFile(SystemProperty("AppHwnd"),"Supported formats"+Chr(0)+"*.b3d;*.rmesh"+Chr(0)+"Blitz3D File format (*.b3d)"+Chr(0)+"*.b3d"+Chr(0)+"Room mesh format (*.rmesh)"+Chr(0)+"*.rmesh"+Chr(0),0,4+8+4096+131072,"")
		;DebugLog Left(s,Len(s)-4)+".rmesh"
	EndIf
	If KeyHit(64) Then
		If s<>"" Then
			If FileExtension(s)="b3d" Then
				s2 = RequestFile(SystemProperty("AppHwnd"),"Room mesh format (*.rmesh)"+Chr(0)+"*.rmesh"+Chr(0),1,2+4+8+4096+131072,Left(s,Len(s)-4)+".rmesh")
			EndIf	
		EndIf	
		If s2<>"" Then
			If FileExtension(s2)<>"rmesh" Then
				s2 = s2+".rmesh"
			EndIf
		EndIf
	EndIf
	If FileExtension(s)="b3d" And FileExtension(s2)="rmesh" Then
		mesh = LoadAnimMesh(s)
		SaveRoomMesh(mesh,s2)
		FreeEntity mesh
		Delete Each ConvertedTexture
		s="" : s2=""
		
;	ElseIf FileExtension(s)="rmesh" Then
;		If w\Room<>0 Then
;			FreeEntity w\Room : w\Room = 0
;		EndIf
;		w\Room = LoadRMesh(s)
;		DeleteTextureEntriesFromCache()
;		Delete Each Props
	EndIf
	
	CatchErrors("Uncaught CheckOptions")
End Function

Function Test()
	Local o.OptionData = First OptionData
	
;	Color 255,255,255
;	Text 0,0,"Wireframe: " + ((o\Booleans Shr 0) Mod 2)
;	Text 0,20,"Lightmaps: " + ((o\Booleans Shr 1) Mod 2)
;	Text 0,40,"Entities: " + ((o\Booleans Shr 2) Mod 2)
;	Text 0,60,"Props: " + ((o\Booleans Shr 3) Mod 2)
;	Text 0,80,"Waypoints: " + ((o\Booleans Shr 4) Mod 2)
	
	Text 0,2,"Press F5 to open"
	Text 0,22,"Press F6 to save"
	If s<>"" Then
		Text 0,52,"Current loaded file: "+s
	EndIf	
	
End Function

Function ShowProgress(prog$)
	Cls
	Color 255,255,255
	Text 0,GraphicsHeight()-30,"Progress: "+prog+"%"
	Flip 1
	If prog = 100 Then
		Delay 1000
	EndIf
End Function	

;Room related functions
Function StripPath$(file$) 
	Local name$,i%,mi$
	
	If Len(file$)>0 Then
		For i=Len(file) To 1 Step -1
			mi=Mid(file,i,1)
			If mi="\" Or mi="/" Then
				Return name
			EndIf
			name=mi+name
		Next
	EndIf
	
	Return name
End Function 

Function StripFilename$(file$)
	Local mi$,lastSlash%,i%
	
	If Len(file)>0 Then
		For i=1 To Len(file)
			mi=Mid(file$,i,1)
			If mi="\" Or mi="/" Then
				lastSlash=i
			EndIf
		Next
	EndIf
	
	Return Left(file,lastSlash)
End Function

;[Block]
;Function EntityScaleX#(entity,globl=False) 
;	
;	If globl Then
;		TFormVector 1,0,0,entity,0
;	Else
;		TFormVector 1,0,0,entity,GetParent(entity) 
;	EndIf
;	
;	Return Sqr(TFormedX()*TFormedX()+TFormedY()*TFormedY()+TFormedZ()*TFormedZ()) 
;End Function 
;
;Function EntityScaleY#(entity,globl=False)
;	
;	If globl Then
;		TFormVector 0,1,0,entity,0
;	Else
;		TFormVector 0,1,0,entity,GetParent(entity) 
;	EndIf
;	
;	Return Sqr(TFormedX()*TFormedX()+TFormedY()*TFormedY()+TFormedZ()*TFormedZ()) 
;End Function 
;
;Function EntityScaleZ#(entity,globl=False)
;	
;	If globl Then
;		TFormVector 0,0,1,entity,0
;	Else
;		TFormVector 0,0,1,entity,GetParent(entity)  
;	EndIf
;	
;	Return Sqr(TFormedX()*TFormedX()+TFormedY()*TFormedY()+TFormedZ()*TFormedZ()) 
;End Function
;[End Block]

Function Piece$(s$,entry,char$=" ")
	Local n%,p%,a$
	
	While Instr(s,char+char)
		s=Replace(s,char+char,char)
	Wend
	For n=1 To entry-1
		p=Instr(s,char)
		s=Right(s,Len(s)-p)
	Next
	p=Instr(s,char)
	If p<1 Then
		a=s
	Else
		a=Left(s,p-1)
	EndIf
	
	Return a
End Function

Function KeyValue$(entity,key$,defaultvalue$="")
	;CatchErrors("KeyValue "+entity+", "+key+", "+defaultvalue)
	Local properties$,p%,testkey$,value$,t$
	
	properties=Replace(EntityName(entity),Chr(13),"")
	key=Lower(key)
	Repeat
		p=Instr(properties,Chr(10))
		If p Then
			t=(Left(properties,p-1))
		Else
			t=properties
		EndIf
		testkey=Piece(t,1,"=")
		testkey=Trim(testkey)
		testkey=Replace(testkey,Chr(34),"")
		testkey=Lower(testkey)
		If testkey=key Then
			value=Piece(t,2,"=")
			value=Trim(value)
			value=Replace(value,Chr(34),"")
			Return value
		EndIf
		If (Not p) Then
			Return defaultvalue
		EndIf
		properties=Right(properties,Len(properties)-p)
	Forever
	
End Function

Function isAlpha%(tex%)
	DebugLog TextureName(tex)
	CatchErrors("isAlpha "+TextureName(tex))
	Local temp1s$,temp1i%,x%,y%
	
	temp1s = StripPath(TextureName(tex))
	If Instr(temp1s,".png")<>0 Or Instr(temp1s,".tga")<>0 Or Instr(temp1s,".tpic")<>0 Then
		LockBuffer(TextureBuffer(tex))
		For x%=0 To TextureWidth(tex)-1
			For y%=0 To TextureHeight(tex)-1
				temp1i=ReadPixelFast(x,y,TextureBuffer(tex))
				temp1i=temp1i Shr 24
				If temp1i<255 Then
					UnlockBuffer(TextureBuffer(tex))
					Return 3
				EndIf
			Next
		Next
		UnlockBuffer(TextureBuffer(tex))
		Return 1
	ElseIf Instr(temp1s,"_lm")<>0 Then
		Return 2
	EndIf
	
	Return 1
End Function

Type ConvertedTexture
	Field name$
End Type

Function SaveRoomMesh(BaseMesh%,filename$)
	CatchErrors("SaveRoomMesh "+BaseMesh+", "+filename)
	Local node%,classname$
	Local surf%,brush%,tex%,texname$
	Local ct.ConvertedTexture
	
	Local temp1i%
	
	Local tempmesh% = BaseMesh
	
	Local f% = WriteFile(filename)
	
	Local drawnmesh% = CreateMesh()
	Local hiddenmesh% = CreateMesh()
	Local nocollmesh% = CreateMesh()
	Local TriggerboxAmount% = 0
	Local Triggerbox[128]
	Local TriggerboxName$[128]
	
	Local NoCollAmount% = 0
	ShowProgress(1)
	Local c%
	For c%=1 To CountChildren(tempmesh)
		node=GetChild(tempmesh,c)
		classname$=Lower(KeyValue(node,"classname"))
		Select classname
			Case "mesh"
				ScaleMesh node,EntityScaleX(node),EntityScaleY(node),EntityScaleZ(node)
				RotateMesh node,EntityPitch(node),EntityYaw(node),EntityRoll(node)
				PositionMesh node,EntityX(node),EntityY(node),EntityZ(node)
				AddMesh node,drawnmesh
			Case "brush"
				RotateMesh node,EntityPitch(node),EntityYaw(node),EntityRoll(node)
				PositionMesh node,EntityX(node),EntityY(node),EntityZ(node)
				AddMesh node,drawnmesh
			Case "field_hit"
				RotateMesh node,EntityPitch(node),EntityYaw(node),EntityRoll(node)
				PositionMesh node,EntityX(node),EntityY(node),EntityZ(node)
				AddMesh node,hiddenmesh
			Case "trigger"
				Triggerbox[TriggerboxAmount] = CreateMesh()
				RotateMesh node,EntityPitch(node),EntityYaw(node),EntityRoll(node)
				PositionMesh node,EntityX(node),EntityY(node),EntityZ(node)
				AddMesh node,Triggerbox[TriggerboxAmount]
				TriggerboxName[TriggerboxAmount] = String(KeyValue(node,"event","event"),1)
				TriggerboxAmount=TriggerboxAmount+1
			Case "mesh_nocoll"
				ScaleMesh node,EntityScaleX(node),EntityScaleY(node),EntityScaleZ(node)
				RotateMesh node,EntityPitch(node),EntityYaw(node),EntityRoll(node)
				PositionMesh node,EntityX(node),EntityY(node),EntityZ(node)
				AddMesh node,nocollmesh
				NoCollAmount = NoCollAmount + 1
			Case "brush_nocoll"
				RotateMesh node,EntityPitch(node),EntityYaw(node),EntityRoll(node)
				PositionMesh node,EntityX(node),EntityY(node),EntityZ(node)
				AddMesh node,nocollmesh
				NoCollAmount = NoCollAmount + 1
		End Select
	Next
	
	ShowProgress(15)
	
	Local Header$ = "RoomMesh"
	
	If TriggerboxAmount% > 0 Then
		Header = Header + ".HasTriggerBox"
	EndIf
	If NoCollAmount% > 0 Then
		Header = Header + ".HasNoColl"
	EndIf
	
	WriteString f, Header
	
	ShowProgress(20)
	
	Local i%,j%, done%, loadtex%
	WriteInt f,CountSurfaces(drawnmesh)
	For i%=1 To CountSurfaces(drawnmesh)
		surf=GetSurface(drawnmesh,i)
		brush=GetSurfaceBrush(surf)
		
		tex=0
		tex=GetBrushTexture(brush,0)
		If tex<>0 Then
			CatchErrors("SaveRoomMesh (convertion: "+tex+", "+texname+") "+filename)
			DebugLog "SaveRoomMesh (convertion: "+tex+", "+texname+") "+filename
			
			texname=TextureName(tex)
			WriteByte(f,isAlpha(tex))
			If Instr(texname,".bmp")<>0 Then
				done%=0
				For ct = Each ConvertedTexture
					If ct\name=texname Then done=1 : Exit
				Next
				If Not done Then
					ct = New ConvertedTexture
					ct\name=texname
					loadtex%=FI_Load(FIF_BMP,texname,0)
				EndIf
				;DeleteFile texname
				texname=Replace(texname,".bmp",".png")
				If Not done Then
					FI_Save(FIF_PNG,loadtex,texname,0)
					FI_Unload(loadtex)
				EndIf
			EndIf
			WriteString f,StripPath(texname)
		Else
			WriteByte(f,0)
		EndIf
		
		FreeTexture tex
		tex=0
		tex=GetBrushTexture(brush,1)
		If tex<>0 Then
			texname=TextureName(tex)
			WriteByte(f,isAlpha(tex))
			WriteString f,StripPath(texname)
		Else
			WriteByte(f,0)
		EndIf
		
		FreeTexture tex
		FreeBrush brush
		
		WriteInt f,CountVertices(surf)
		For j%=0 To CountVertices(surf)-1
			
			;world coords
			WriteFloat f,VertexX(surf,j)
			WriteFloat f,VertexY(surf,j)
			WriteFloat f,VertexZ(surf,j)
			
			;texture coords
			WriteFloat f,VertexU(surf,j,0)
			WriteFloat f,VertexV(surf,j,0)
			
			WriteFloat f,VertexU(surf,j,1)
			WriteFloat f,VertexV(surf,j,1)
			
			;colors
			WriteByte f,VertexRed(surf,j)
			WriteByte f,VertexGreen(surf,j)
			WriteByte f,VertexBlue(surf,j)
		Next
		
		WriteInt f,CountTriangles(surf)
		For j%=0 To CountTriangles(surf)-1
			WriteInt f,TriangleVertex(surf,j,0)
			WriteInt f,TriangleVertex(surf,j,1)
			WriteInt f,TriangleVertex(surf,j,2)
		Next
	Next
	
	ShowProgress(50)
	
	WriteInt f,CountSurfaces(hiddenmesh)
	For i%=1 To CountSurfaces(hiddenmesh)
		surf=GetSurface(hiddenmesh,i)
		WriteInt f,CountVertices(surf)
		For j%=0 To CountVertices(surf)-1
			;world coords
			WriteFloat f,VertexX(surf,j)
			WriteFloat f,VertexY(surf,j)
			WriteFloat f,VertexZ(surf,j)
		Next
		
		WriteInt f,CountTriangles(surf)
		For j%=0 To CountTriangles(surf)-1
			WriteInt f,TriangleVertex(surf,j,0)
			WriteInt f,TriangleVertex(surf,j,1)
			WriteInt f,TriangleVertex(surf,j,2)
		Next
	Next
	
	ShowProgress(65)
	
	If NoCollAmount > 0 Then
		DebugLog "NoCollAmount: "+NoCollAmount
		WriteInt f,CountSurfaces(nocollmesh)
		For i%=1 To CountSurfaces(nocollmesh)
			surf=GetSurface(nocollmesh,i)
			brush=GetSurfaceBrush(surf)
			
			FreeTexture tex
			tex=0
			tex=GetBrushTexture(brush,0)
			If tex<>0 Then
				texname=TextureName(tex)
				WriteByte(f,isAlpha(tex))
				CatchErrors("SaveRoomMesh (convertion: "+tex+", "+texname+") "+filename)
				DebugLog "SaveRoomMesh (NoColl convertion: "+tex+", "+texname+") "+filename
				If Instr(texname,".bmp")<>0 Then
					done%=0
					DebugLog "Lightmap found in nocoll mesh"
					For ct = Each ConvertedTexture
						If ct\name=texname Then done=1 : Exit
					Next
					If Not done Then
						ct = New ConvertedTexture
						ct\name=texname
						loadtex%=FI_Load(FIF_BMP,texname,0)
					EndIf
					;DeleteFile texname
					texname=Replace(texname,".bmp",".png")
					If Not done Then
						FI_Save(FIF_PNG,loadtex,texname,0)
						FI_Unload(loadtex)
					EndIf
				EndIf
				WriteString f,StripPath(texname)
			Else
				WriteByte(f,0)
			EndIf
			
			FreeTexture tex
			tex=0
			tex=GetBrushTexture(brush,1)
			If tex<>0 Then
				texname=TextureName(tex)
				WriteByte(f,isAlpha(tex))
				WriteString f,StripPath(texname)
			Else
				WriteByte(f,0)
			EndIf
			
			FreeTexture tex
			FreeBrush brush
			
			WriteInt f,CountVertices(surf)
			For j%=0 To CountVertices(surf)-1
				
				;world coords
				WriteFloat f,VertexX(surf,j)
				WriteFloat f,VertexY(surf,j)
				WriteFloat f,VertexZ(surf,j)
				
				;texture coords
				WriteFloat f,VertexU(surf,j,0)
				WriteFloat f,VertexV(surf,j,0)
				
				WriteFloat f,VertexU(surf,j,1)
				WriteFloat f,VertexV(surf,j,1)
				
				;colors
				WriteByte f,VertexRed(surf,j)
				WriteByte f,VertexGreen(surf,j)
				WriteByte f,VertexBlue(surf,j)
			Next
			
			WriteInt f,CountTriangles(surf)
			For j%=0 To CountTriangles(surf)-1
				WriteInt f,TriangleVertex(surf,j,0)
				WriteInt f,TriangleVertex(surf,j,1)
				WriteInt f,TriangleVertex(surf,j,2)
			Next
		Next
	EndIf
	
	ShowProgress(75)
	
	CatchErrors("SaveRoomMesh (Triggerbox) "+filename)
	Local z%
	If TriggerboxAmount > 0
		WriteInt f,TriggerboxAmount
		For z=0 To TriggerboxAmount-1
			WriteInt f,CountSurfaces(Triggerbox[z]) : DebugLog CountSurfaces(Triggerbox[z])
			For i%=1 To CountSurfaces(Triggerbox[z])
				surf=GetSurface(Triggerbox[z],i)
				WriteInt f,CountVertices(surf)
				For j%=0 To CountVertices(surf)-1
					;world coords
					WriteFloat f,VertexX(surf,j)
					WriteFloat f,VertexY(surf,j)
					WriteFloat f,VertexZ(surf,j)
				Next
				
				WriteInt f,CountTriangles(surf)
				For j%=0 To CountTriangles(surf)-1
					WriteInt f,TriangleVertex(surf,j,0)
					WriteInt f,TriangleVertex(surf,j,1)
					WriteInt f,TriangleVertex(surf,j,2)
				Next
			Next
			WriteString f,TriggerboxName[z]
		Next
	EndIf
	
	temp1i=0
	
	ShowProgress(80)
	
	For c%=1 To CountChildren(tempmesh)
		node=GetChild(tempmesh,c)	
		classname$=Lower(KeyValue(node,"classname"))
		
		Select classname
			Case "screen","waypoint","light","spotlight","soundemitter","playerstart","model"
				temp1i=temp1i+1
			Case "mp_playerspawn","mp_enemyspawn","mp_itemspawn","flu_light","model_nocoll","fusebox","generator","button_gen","lever_gen","mp_damageboss_radius","particle_gen"
				temp1i=temp1i+1
		End Select
		
	Next
	
	ShowProgress(85)
	
	WriteInt f,temp1i
	
	For c%=1 To CountChildren(tempmesh)
		
		node=GetChild(tempmesh,c)	
		classname$=Lower(KeyValue(node,"classname"))
		CatchErrors("SaveRoomMesh ("+classname+") "+filename)
		Select classname
			Case "screen"
				WriteString f,classname
				
				WriteFloat f,EntityX(node)
				WriteFloat f,EntityY(node)
				WriteFloat f,EntityZ(node)
				
				WriteString f,KeyValue(node,"imgpath","")
			Case "waypoint"
				WriteString f,classname
				
				WriteFloat f,EntityX(node)
				WriteFloat f,EntityY(node)
				WriteFloat f,EntityZ(node)
			Case "light"
				WriteString f,classname
				
				WriteFloat f,EntityX(node)
				WriteFloat f,EntityY(node)
				WriteFloat f,EntityZ(node)
				
				WriteFloat f,Float(KeyValue(node,"range","1"))
				WriteString f,KeyValue(node,"color","255 255 255")
				WriteFloat f,Float(KeyValue(node,"intensity","1.0"))
			Case "spotlight"
				WriteString f,classname
				
				WriteFloat f,EntityX(node)
				WriteFloat f,EntityY(node)
				WriteFloat f,EntityZ(node)
				
				WriteFloat f,Float(KeyValue(node,"range","1"))
				WriteString f,KeyValue(node,"color","255 255 255")
				WriteFloat f,Float(KeyValue(node,"intensity","1.0"))
				WriteString f,KeyValue(node,"angles","0 0 0")
				
				WriteInt f,Int(KeyValue(node,"innerconeangle",""))
				WriteInt f,Int(KeyValue(node,"outerconeangle",""))
			Case "soundemitter"
				WriteString f,classname
				
				WriteFloat f,EntityX(node)
				WriteFloat f,EntityY(node)
				WriteFloat f,EntityZ(node)
				
				WriteInt f,Int(KeyValue(node,"sound","0"))
				WriteFloat f,Float(KeyValue(node,"range","1"))
			Case "playerstart"
				WriteString f,classname
				
				WriteFloat f,EntityX(node)
				WriteFloat f,EntityY(node)
				WriteFloat f,EntityZ(node)
				
				WriteString f,KeyValue(node,"angles","0 0 0")
			Case "model", "model_nocoll", "fusebox", "generator", "button_gen", "lever_gen"
				WriteString f,classname
				
				WriteString f,KeyValue(node,"file","")
				
				If classname = "generator" Lor classname = "button_gen" Lor classname = "lever_gen" Then
					If classname = "lever_gen" Then
						WriteString f,KeyValue(node,"file_handle","")
						WriteInt f,Int(KeyValue(node,"angle","0"))
					ElseIf classname = "generator" Then
						WriteInt f,Int(KeyValue(node,"sound","0"))
						WriteFloat f,Float(KeyValue(node,"range","1"))
					EndIf
					WriteInt f,Int(KeyValue(node,"id","0"))
				EndIf	
				
				WriteFloat f,EntityX(node)
				WriteFloat f,EntityY(node)
				WriteFloat f,EntityZ(node)
				
				WriteFloat f,EntityPitch(node)
				WriteFloat f,EntityYaw(node)
				WriteFloat f,EntityRoll(node)
				
				WriteFloat f,EntityScaleX(node)
				WriteFloat f,EntityScaleY(node)
				WriteFloat f,EntityScaleZ(node)
			Case "particle_gen"
				WriteString f,classname
				
				WriteFloat f,EntityX(node)
				WriteFloat f,EntityY(node)
				WriteFloat f,EntityZ(node)
				
				WriteByte f,KeyValue(node,"type","0")
				WriteString f,KeyValue(node,"angles","0 0 0")
				WriteInt f,Int(KeyValue(node,"id","0"))
			Case "mp_damageboss_radius"
				WriteString f,classname
				
				WriteFloat f,EntityX(node)
				WriteFloat f,EntityY(node)
				WriteFloat f,EntityZ(node)
				
				WriteFloat f,KeyValue(node,"radius","0.0")
				WriteInt f,Int(KeyValue(node,"id","0"))
			Case "mp_playerspawn"
				WriteString f,classname
				
				WriteFloat f,EntityX(node)
				WriteFloat f,EntityY(node)
				WriteFloat f,EntityZ(node)
				
				WriteByte f,KeyValue(node,"team","0")
				WriteFloat f,KeyValue(node,"direction","0.0")
			Case "mp_enemyspawn"
				WriteString f,classname
				
				WriteFloat f,EntityX(node)
				WriteFloat f,EntityY(node)
				WriteFloat f,EntityZ(node)
				
				WriteString f,KeyValue(node,"npctype","")
			Case "mp_itemspawn"
				WriteString f,classname
				
				WriteFloat f,EntityX(node)
				WriteFloat f,EntityY(node)
				WriteFloat f,EntityZ(node)
				
				WriteByte f,KeyValue(node,"itemtype","0")
				
				WriteInt f,KeyValue(node,"respawntime","10")
				WriteString f,KeyValue(node,"randomtime","0 0")
			Case "flu_light"
				WriteString f,classname
				
				WriteFloat f,EntityX(node)
				WriteFloat f,EntityY(node)
				WriteFloat f,EntityZ(node)
				
				WriteFloat f,EntityPitch(node)
				WriteFloat f,EntityYaw(node)
				WriteFloat f,EntityRoll(node)
				
				WriteInt f,KeyValue(node,"id","0")
		End Select
		
	Next
	
	ShowProgress(100)
	
	WriteString f,"EOF"
	
	CloseFile f
	
	FreeEntity drawnmesh
	FreeEntity hiddenmesh
	FreeEntity nocollmesh
	
	CatchErrors("Uncaught SaveRoomMesh "+BaseMesh+", "+filename)
End Function

Function LoadRMesh(file$,doublesided=True)
	CatchErrors("LoadRMesh "+file+", "+doublesided)
	ClsColor 0,0,0
	
	;read the file
	Local f%=ReadFile(file)
	Local i%,j%,k%,x#,y#,z#,yaw#
	Local vertex%
	Local temp1i%,temp2i%,temp3i%
	Local temp1#,temp2#,temp3#
	Local temp1s$, temp2s$
	
	Local collisionMeshes% = CreatePivot()
	
	Local hasTriggerBox% = False
	
	Local hasNoColl% = False
	
	For i=0 To 3 ;reattempt up to 3 times
		If f=0 Then
			f=ReadFile(file)
		Else
			Exit
		EndIf
	Next
	If f=0 Then RuntimeError "Error reading file "+Chr(34)+file+Chr(34)
	Local isRMesh$ = ReadString(f)
	If Instr(isRMesh,"RoomMesh") = 0 Then
		RuntimeError Chr(34)+file+Chr(34)+" is Not RMESH ("+isRMesh+")"
	EndIf
	If Instr(isRMesh,".HasTriggerBox") > 0 Then
		hasTriggerBox% = True
	EndIf
	If Instr(isRMesh,".HasNoColl") > 0 Then
		hasNoColl% = True
	EndIf
	
	file=StripFilename(file)
	
	Local count%,count2%
	
	;drawn meshes
	Local Opaque%,Alpha%
	
	Opaque=CreateMesh()
	Alpha=CreateMesh()
	
	count = ReadInt(f)
	Local childMesh%
	Local surf%,tex%[2],brush%
	
	Local isTexAlpha%
	
	Local u#,v#
	
	;[Block]
	For i=1 To count ;drawn mesh
		childMesh=CreateMesh()
		
		surf=CreateSurface(childMesh)
		
		brush=CreateBrush()
		
		tex[0]=0 : tex[1]=0
		
		Local reflectiontex = 0
		
		isAlpha=0
		
		For j=0 To 1
			temp1i=ReadByte(f)
			If temp1i<>0 Then
				temp1s=ReadString(f)
				If FileType(file+temp1s)=1 Then ;Check if texture is existing in original path
					If temp1i<3 Then
						tex[j]=LoadTextureCheckingIfInCache(file+temp1s,1)
					Else
						tex[j]=LoadTextureCheckingIfInCache(file+temp1s,3)
					EndIf
				ElseIf FileType(MapTexturesFolder+temp1s)=1 Then ;If not, check the MapTexturesFolder
					If temp1i<3 Then
						tex[j]=LoadTextureCheckingIfInCache(MapTexturesFolder+temp1s,1)
					Else
						tex[j]=LoadTextureCheckingIfInCache(MapTexturesFolder+temp1s,3)
					EndIf
				EndIf
				If tex[j]<>0 Then
					If temp1i=1 Then TextureBlend tex[j],5
					If Instr(Lower(temp1s),"_lm")<>0 Then
						TextureBlend tex[j],3
					EndIf
					
					isTexAlpha=2
					If temp1i=3 Then isTexAlpha=1
					TextureCoords tex[j],1-j
				EndIf
			EndIf
		Next
		
		If isTexAlpha=1 Then
			If tex[1]<>0 Then
				TextureBlend tex[1],2
				BrushTexture brush,tex[1],0,0
			Else
;				BrushTexture brush,MissingTexture,0,0
			EndIf
		Else
			If tex[0]<>0 And tex[1]<>0 Then
				For j=0 To 1
					BrushTexture brush,tex[j],0,j+1
				Next
;				BrushTexture brush,AmbientLightRoomTex,0
			Else
				For j=0 To 1
					If tex[j]<>0 Then
						BrushTexture brush,tex[j],0,j
					Else
;						BrushTexture brush,MissingTexture,0,j
					EndIf
				Next
			EndIf
		EndIf
		
		surf=CreateSurface(childMesh)
		
		If isTexAlpha>0 Then
			PaintSurface surf,brush
		EndIf
		
		FreeBrush brush : brush = 0
		
		count2=ReadInt(f) ;vertices
		
		For j%=1 To count2
			;world coords
			x=ReadFloat(f) : y=ReadFloat(f) : z=ReadFloat(f)
			
			vertex=AddVertex(surf,x,y,z)
			
			;texture coords
			For k%=0 To 1
				u=ReadFloat(f) : v=ReadFloat(f)
				VertexTexCoords surf,vertex,u,v,0.0,k
			Next
			
			;colors
			temp1i=ReadByte(f)
			temp2i=ReadByte(f)
			temp3i=ReadByte(f)
			VertexColor surf,vertex,temp1i,temp2i,temp3i,1.0
		Next
		
		count2=ReadInt(f) ;polys
		For j%=1 To count2
			temp1i = ReadInt(f) : temp2i = ReadInt(f) : temp3i = ReadInt(f)
			AddTriangle(surf,temp1i,temp2i,temp3i)
		Next
		
		If isAlpha=1 Then
			AddMesh childMesh,Alpha
			EntityAlpha childMesh,0.0
		Else
			AddMesh childMesh,Opaque
			EntityParent childMesh,collisionMeshes
			EntityAlpha childMesh,0.0
;			EntityType childMesh,HIT_MAP
;			EntityPickMode childMesh,2
;			
;			If doublesided Then	;make collision double-sided
;				Local flipChild% = CopyMesh(childMesh)
;				FlipMesh(flipChild)
;				AddMesh flipChild,childMesh
;				FreeEntity flipChild
;			EndIf
		EndIf
		HideEntity childMesh
	Next
	;[End Block]
	
	Local hiddenMesh%
	hiddenMesh=CreateMesh()
	
	count=ReadInt(f) ;invisible collision mesh
	For i%=1 To count
		surf=CreateSurface(hiddenMesh)
		count2=ReadInt(f) ;vertices
		For j%=1 To count2
			;world coords
			x=ReadFloat(f) : y=ReadFloat(f) : z=ReadFloat(f)
			vertex=AddVertex(surf,x,y,z)
		Next
		
		count2=ReadInt(f) ;polys
		For j%=1 To count2
			temp1i = ReadInt(f) : temp2i = ReadInt(f) : temp3i = ReadInt(f)
			AddTriangle(surf,temp1i,temp2i,temp3i)
			AddTriangle(surf,temp1i,temp3i,temp2i)
		Next
	Next
	
	;no coll mesh
	;[Block]
	If hasNoColl Then
		count = ReadInt(f)
		
		For i=1 To count ;drawn mesh
			childMesh=CreateMesh()
			
			surf=CreateSurface(childMesh)
			
			brush=CreateBrush()
			
			tex[0]=0 : tex[1]=0
			
			reflectiontex = 0
			
			isTexAlpha=0
			
			For j=0 To 1
				temp1i=ReadByte(f)
				If temp1i<>0
					temp1s=ReadString(f)
					If FileType(file+temp1s)=1 ;Check if texture is existing in original path
						If temp1i<3 Then
							tex[j]=LoadTextureCheckingIfInCache(file+temp1s,1)
						Else
							tex[j]=LoadTextureCheckingIfInCache(file+temp1s,3)
						EndIf
					ElseIf FileType(MapTexturesFolder+temp1s)=1 ;If not, check the MapTexturesFolder
						If temp1i<3 Then
							tex[j]=LoadTextureCheckingIfInCache(MapTexturesFolder+temp1s,1)
						Else
							tex[j]=LoadTextureCheckingIfInCache(MapTexturesFolder+temp1s,3)
						EndIf
					EndIf
					If tex[j]<>0 Then
						If temp1i=1 Then TextureBlend tex[j],5
						If Instr(Lower(temp1s),"_lm")<>0
							TextureBlend tex[j],3
						EndIf
						
						isTexAlpha=2
						If temp1i=3 Then
							isTexAlpha=1
						EndIf
						TextureCoords tex[j],1-j
					EndIf
				EndIf
			Next
			
			If isTexAlpha=1 Then
				If tex[1]<>0 Then
					TextureBlend tex[1],2
					BrushTexture brush,tex[1],0,0
				Else
;					BrushTexture brush,MissingTexture,0,0
				EndIf
			Else
				If tex[0]<>0 And tex[1]<>0 Then
					For j=0 To 1
						BrushTexture brush,tex[j],0,j+1
					Next
;					BrushTexture brush,AmbientLightRoomTex,0
				Else
					For j=0 To 1
						If tex[j]<>0 Then
							BrushTexture brush,tex[j],0,j
						Else
;							BrushTexture brush,MissingTexture,0,j
						EndIf
					Next
				EndIf
			EndIf
			
			surf=CreateSurface(childMesh)
			
			If isTexAlpha>0 Then
				PaintSurface surf,brush
			EndIf
			
			FreeBrush brush : brush = 0
			
			count2=ReadInt(f) ;vertices
			
			For j%=1 To count2
				;world coords
				x=ReadFloat(f) : y=ReadFloat(f) : z=ReadFloat(f)
				
				vertex=AddVertex(surf,x,y,z)
				
				;texture coords
				For k%=0 To 1
					u=ReadFloat(f) : v=ReadFloat(f)
					VertexTexCoords surf,vertex,u,v,0.0,k
				Next
				
				;colors
				temp1i=ReadByte(f)
				temp2i=ReadByte(f)
				temp3i=ReadByte(f)
				VertexColor surf,vertex,temp1i,temp2i,temp3i,1.0
			Next
			
			count2=ReadInt(f) ;polys
			For j%=1 To count2
				temp1i = ReadInt(f) : temp2i = ReadInt(f) : temp3i = ReadInt(f)
				AddTriangle(surf,temp1i,temp2i,temp3i)
			Next
			
			If isTexAlpha=1 Then
				AddMesh childMesh,Alpha
				EntityAlpha childMesh,0.0
			Else
				AddMesh childMesh,Opaque
				EntityParent childMesh,collisionMeshes
				EntityAlpha childMesh,0.0
;				EntityPickMode childMesh,2
			EndIf
			HideEntity childMesh
		Next
	EndIf
	;[End Block]
	
	;trigger boxes
	;[Block]
	Local tb%
	If hasTriggerBox Then
		If False Then
;			rt\TempTriggerboxAmount = ReadInt(f)
;			For tb = 0 To rt\TempTriggerboxAmount-1
;				rt\TempTriggerbox[tb] = CreateMesh(rt\obj)
;				count = ReadInt(f)
;				For i%=1 To count
;					surf=CreateSurface(rt\TempTriggerbox[tb])
;					count2=ReadInt(f)
;					For j%=1 To count2
;						x=ReadFloat(f) : y=ReadFloat(f) : z=ReadFloat(f)
;						vertex=AddVertex(surf,x,y,z)
;					Next
;					count2=ReadInt(f)
;					For j%=1 To count2
;						temp1i = ReadInt(f) : temp2i = ReadInt(f) : temp3i = ReadInt(f)
;						AddTriangle(surf,temp1i,temp2i,temp3i)
;						AddTriangle(surf,temp1i,temp3i,temp2i)
;					Next
;				Next
;				rt\TempTriggerboxName[tb] = ReadString(f)
;			Next
		Else
			For tb = 0 To ReadInt(f)-1
				For i%=1 To ReadInt(f)
					For j%=1 To ReadInt(f)
						ReadFloat(f)
						ReadFloat(f)
						ReadFloat(f)
					Next
					For j%=1 To ReadInt(f)
						ReadInt(f)
						ReadInt(f)
						ReadInt(f)
					Next
				Next
			Next
		EndIf
	EndIf
	;[End Block]
	
	Local range#,lcolor$,intensity#,r%,g%,b%,angles$,pitch#,innercone%,outercone%
	count=ReadInt(f) ;point entities
	For i%=1 To count
		temp1s=ReadString(f)
		Select temp1s
			Case "screen"
				;[Block]
				temp1=ReadFloat(f)*RoomScale
				temp2=ReadFloat(f)*RoomScale
				temp3=ReadFloat(f)*RoomScale
				temp2s$ =ReadString(f)
				
;				If NTF_GameModeFlag<>3 And (Not MainMenuOpen) Then
;					If temp1<>0 Or temp2<>0 Or temp3<>0 Then 
;						Local ts.TempScreens = New TempScreens	
;						ts\x = temp1
;						ts\y = temp2
;						ts\z = temp3
;						ts\imgpath = temp2s
;						ts\roomtemplate = rt
;					EndIf
;				EndIf
				;[End Block]
			Case "waypoint"
				;[Block]
				temp1=ReadFloat(f)*RoomScale
				temp2=ReadFloat(f)*RoomScale
				temp3=ReadFloat(f)*RoomScale
				
;				If (Not MainMenuOpen) And NTF_GameModeFlag<>3 Then
;					Local w.TempWayPoints = New TempWayPoints
;					w\roomtemplate = rt
;					w\x = temp1
;					w\y = temp2
;					w\z = temp3
;				ElseIf (Not MainMenuOpen) Then
;					Local wa.WayPoints = CreateMPWaypoint(temp1,temp2,temp3)
;				EndIf
				;[End Block]
			Case "light"
				;[Block]
				temp1=ReadFloat(f)*RoomScale
				temp2=ReadFloat(f)*RoomScale
				temp3=ReadFloat(f)*RoomScale
				
				If temp1<>0 Or temp2<>0 Or temp3<>0 Then 
					range# = ReadFloat(f)/2000.0
					lcolor$=ReadString(f)
					intensity# = Min(ReadFloat(f)*0.8,1.0)
					r%=Int(Piece(lcolor,1," "))*intensity
					g%=Int(Piece(lcolor,2," "))*intensity
					b%=Int(Piece(lcolor,3," "))*intensity
					
;					If (Not MainMenuOpen) And NTF_GameModeFlag<>3 Then
;						AddTempLight(rt, temp1,temp2,temp3, 2, range, r,g,b)
;					ElseIf (Not MainMenuOpen) And NTF_GameModeFlag=3 Then
;						AddLightMPMap(mp_I\Map,temp1,temp2,temp3,2,range,r,g,b)
;					ElseIf MainMenuOpen Then
;						AddLightMenu3D(temp1,temp2,temp3,2,range,r,g,b)
;					EndIf
				Else
					ReadFloat(f) : ReadString(f) : ReadFloat(f)
				EndIf
				;[End Block]
			Case "spotlight"
				;[Block]
				temp1=ReadFloat(f)*RoomScale
				temp2=ReadFloat(f)*RoomScale
				temp3=ReadFloat(f)*RoomScale
				
				If temp1<>0 Or temp2<>0 Or temp3<>0 Then 
					range# = ReadFloat(f)/2000.0
					lcolor$=ReadString(f)
					intensity# = Min(ReadFloat(f)*0.8,1.0)
					r%=Int(Piece(lcolor,1," "))*intensity
					g%=Int(Piece(lcolor,2," "))*intensity
					b%=Int(Piece(lcolor,3," "))*intensity
					angles$=ReadString(f)
					pitch#=Piece(angles,1," ")
					yaw#=Piece(angles,2," ")
					innercone = ReadInt(f)
					outercone = ReadInt(f)
					
;					If (Not MainMenuOpen) And NTF_GameModeFlag<>3 Then
;						Local lt.LightTemplates = AddTempLight(rt, temp1,temp2,temp3, 2, range, r,g,b)
;						lt\pitch = pitch
;						lt\yaw = yaw
;						lt\innerconeangle = innercone
;						lt\outerconeangle = outercone
;					ElseIf (Not MainMenuOpen) And NTF_GameModeFlag=3 Then
;						AddLightMPMap(mp_I\Map,temp1,temp2,temp3,3,range,r,g,b)
;						RotateEntity mp_I\Map\Lights[mp_I\Map\LightAmount-1],pitch,yaw,0
;						LightConeAngles(mp_I\Map\Lights[mp_I\Map\LightAmount-1],innercone,outercone)
;					ElseIf MainMenuOpen Then
;						Local ml.Menu3DLights = AddLightMenu3D(temp1,temp2,temp3,3,range,r,g,b)
;						RotateEntity ml\Lights,pitch,yaw,0
;						LightConeAngles ml\Lights,innercone,outercone
;					EndIf
				Else
					ReadFloat(f) : ReadString(f) : ReadFloat(f) : ReadString(f) : ReadInt(f) : ReadInt(f)
				EndIf
				;[End Block]
			Case "soundemitter"
				;[Block]
				temp1i=0
				
;				If (Not MainMenuOpen) And NTF_GameModeFlag<>3 Then
;					If rt<>Null Then
;						For j = 0 To MaxRoomEmitters-1
;							If rt\TempSoundEmitter[j]=0 Then
;								rt\TempSoundEmitterX[j]=ReadFloat(f)*RoomScale
;								rt\TempSoundEmitterY[j]=ReadFloat(f)*RoomScale
;								rt\TempSoundEmitterZ[j]=ReadFloat(f)*RoomScale
;								rt\TempSoundEmitter[j]=ReadInt(f)
;								
;								rt\TempSoundEmitterRange[j]=ReadFloat(f)
;								temp1i=1
;								Exit
;							EndIf
;						Next
;					EndIf
;				ElseIf (Not MainMenuOpen) And NTF_GameModeFlag=3 Then
;					;WIP: Add support for sound emitters for multiplayer maps!
;				EndIf
				
				If temp1i=0 Then
					ReadFloat(f)
					ReadFloat(f)
					ReadFloat(f)
					ReadInt(f)
					ReadFloat(f)
				EndIf
				;[End Block]
			Case "model", "model_nocoll"
				;[Block]
				file = ReadString(f)
				If file<>""
					Local model = CreatePropObj("..\GFX\Map\Props\"+file)
					
					temp1=ReadFloat(f) : temp2=ReadFloat(f) : temp3=ReadFloat(f)
					PositionEntity model,temp1,temp2,temp3
					
					temp1=ReadFloat(f) : temp2=ReadFloat(f) : temp3=ReadFloat(f)
					RotateEntity model,temp1,temp2,temp3
					
					temp1=ReadFloat(f) : temp2=ReadFloat(f) : temp3=ReadFloat(f)
					ScaleEntity model,temp1,temp2,temp3
					
					EntityParent model,Opaque
;					If temp1s <> "model_nocoll" Then
;						EntityType model,HIT_MAP
;					Else
;						EntityType model,0
;					EndIf
;					EntityPickMode model,2
				Else
					temp1=ReadFloat(f) : temp2=ReadFloat(f) : temp3=ReadFloat(f)
				EndIf
				;[End Block]
			Case "mp_playerspawn"
				;[Block]
				temp1=ReadFloat(f)*RoomScale
				temp2=ReadFloat(f)*RoomScale
				temp3=ReadFloat(f)*RoomScale
				Local team% = ReadByte(f)
				yaw = ReadFloat(f)
;				If mp_I\PlayState = GAME_SERVER Then
;					Local pls.PlayerSpawner = CreatePlayerSpawner(temp1,temp2,temp3,yaw,team)
;				EndIf
				;[End Block]
			Case "mp_enemyspawn"
				;[Block]
				temp1=ReadFloat(f)*RoomScale
				temp2=ReadFloat(f)*RoomScale
				temp3=ReadFloat(f)*RoomScale
				Local enemyString$=ReadString(f)
				
;				If mp_I\PlayState = GAME_SERVER Then
;					Local ens.EnemySpawner = CreateEnemySpawner(temp1,temp2,temp3,enemyString)
;				EndIf
				;[End Block]
			Case "mp_itemspawn"
				;[Block]
				temp1=ReadFloat(f)*RoomScale
				temp2=ReadFloat(f)*RoomScale
				temp3=ReadFloat(f)*RoomScale
				Local ittype = ReadByte(f)
				Local respawntime = ReadInt(f)
				Local rndtime$ = ReadString(f)
				Local rndtime1 = Piece(rndtime,1," ")
				Local rndtime2 = Piece(rndtime,2," ")
				
;				If mp_I\PlayState = GAME_SERVER Then
;					Local its.ItemSpawner = CreateItemSpawner(temp1,temp2,temp3,ittype,respawntime,rndtime1,rndtime2)
;				EndIf
				;[End Block]
			Case "flu_light"
				;[Block]
;				Local tfll.TempFluLight = New TempFluLight
				temp1=ReadFloat(f)*RoomScale
				temp2=ReadFloat(f)*RoomScale
				temp3=ReadFloat(f)*RoomScale
;				tfll\position = CreateVector3D(temp1,temp2,temp3)
				temp1=ReadFloat(f)
				temp2=ReadFloat(f)
				temp3=ReadFloat(f)
;				tfll\rotation = CreateVector3D(temp1,temp2,temp3)
				;tfll\id = ReadInt(f)
				ReadInt(f)
				;[End Block]
		End Select
	Next
	
	Local obj%
	
	temp1i=CopyMesh(Alpha)
	FlipMesh temp1i
	AddMesh temp1i,Alpha
	FreeEntity temp1i
	
	If brush <> 0 Then FreeBrush brush
	
	AddMesh Alpha,Opaque
	FreeEntity Alpha
	
	EntityFX Opaque,3
	
	EntityAlpha hiddenMesh,0.0
	EntityAlpha Opaque,1.0
	
;	EntityType hiddenMesh,HIT_MAP
	
	obj=CreatePivot()
	CreatePivot(obj) ;skip "meshes" object
	EntityParent Opaque,obj
	EntityParent hiddenMesh,obj
	CreatePivot(obj) ;skip "pointentites" object
	CreatePivot(obj) ;skip "solidentites" object
	EntityParent collisionMeshes,obj
	
	CloseFile f
	CatchErrors("Uncaught LoadRMesh "+file+", "+doublesided)
	Return obj%
	
End Function

Type TextureInCache
	Field tex%
	Field texname$
End Type

Function LoadTextureCheckingIfInCache(texname$,texflags%=1)
	CatchErrors("DeleteTextureEntriesFromCache "+texname+", "+texflags)
	Local tic.TextureInCache,texture%,currpath$
	
	For tic = Each TextureInCache
		If StripPath(texname) = tic\texname Then
			Return tic\tex
		EndIf
	Next
	
	currpath$ = texname$
	tic = New TextureInCache
	tic\texname = StripPath(texname$)
	
	If FileType(texname)=1 Then
		currpath = texname
	ElseIf FileType(MapTexturesFolder+tic\texname)=1 Then
		currpath = MapTexturesFolder+tic\texname
		tic\texname = StripPath(currpath)
	Else
		RuntimeError "ERROR: Texture " + texname + " not found!"
	EndIf
	
	tic\tex = LoadTexture(currpath,texflags%)
	
	CatchErrors("Uncaught DeleteTextureEntriesFromCache "+texname+", "+texflags)
	
	Return tic\tex
	
End Function

Function DeleteTextureEntriesFromCache()
	CatchErrors("DeleteTextureEntriesFromCache")
	Local tic.TextureInCache
	
	For tic = Each TextureInCache
		If tic\tex<>0 Then FreeTexture tic\tex : tic\tex=0
		Delete tic
	Next
	CatchErrors("Uncaught DeleteTextureEntriesFromCache")
End Function

Type Props
	Field file$
	Field obj
End Type

Function CreatePropObj(file$)
	CatchErrors("CreatePropObj "+file)
	Local p.Props
	
	For p.Props = Each Props
		If p\file = file Then
			Return CopyEntity(p\obj)
		EndIf
	Next
	
	p.Props = New Props
	p\file = file
	p\obj = LoadMesh(file)
	
	CatchErrors("Uncaught CreatePropObj")
	
	Return p\obj
End Function

Function CatchErrors(location$)
	SetErrorMsg(1,"Error located in: "+location)
End Function

;~IDEal Editor Parameters:
;~F#14#19#2B#32#3A#6C#7E#89#99#A8#CB#DF#FE#119#2F6#43A#46B#47D#48D#4A6
;~F#4CC#4EB#505#510#51B#52A#55B#560#57F#58A#58F#5A2
;~C#Blitz3D