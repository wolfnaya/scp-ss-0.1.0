Type Materials
	Field name$
	Field Diff%
	Field Bump%
	Field isDiffuseAlpha%
	Field useMask%
	Field sphereReflection%
	Field specular#
	Field StepSound%
	Field Reflection%
	Field UScale#
	Field VScale#
	Field DetailTex%
End Type

Function LoadMaterials(file$)
	CatchErrors("Uncaught (LoadMaterials)")
	
	Local TemporaryString$
	Local mat.Materials = Null
	Local StrTemp$ = ""
	
	Local f = OpenFile(file)
	
	While Not Eof(f)
		TemporaryString = Trim(ReadLine(f))
		If Left(TemporaryString,1) = "[" Then
			TemporaryString = Mid(TemporaryString, 2, Len(TemporaryString) - 2)
			
			mat.Materials = New Materials
			
			mat\name = Lower(TemporaryString)
			
			If BumpEnabled Then
				StrTemp = GetINIString(file, TemporaryString, "bump")
				If StrTemp <> "" Then 
					mat\Bump =  LoadTexture_Strict(StrTemp,256)
					ApplyBumpMap(mat\Bump)
				EndIf
				StrTemp = GetINIString(file, TemporaryString, "reflection")
				If StrTemp <> "" Then
					mat\Reflection = LoadTexture_Strict(StrTemp,1+64+256)
					TextureBlend mat\Reflection,1
					ScaleTexture mat\Reflection,0.01,0.01
				EndIf
				StrTemp = GetINIString(file, TemporaryString, "detailtex")
				If StrTemp <> "" Then
					mat\DetailTex= LoadTexture_Strict(StrTemp,1+256)
					ScaleTexture mat\DetailTex,0.1,0.1
				EndIf
			EndIf
			
			mat\StepSound = (GetINIInt(file, TemporaryString, "stepsound")+1)
			mat\isDiffuseAlpha = GetINIInt(file, TemporaryString, "transparent")
			mat\useMask = GetINIInt(file, TemporaryString, "masked")
			mat\specular = GetINIFloat(file, TemporaryString, "specular")
			mat\sphereReflection = GetINIInt(file, TemporaryString, "sphere_reflection")
			mat\UScale = GetINIFloat(file, TemporaryString, "u_scale",1.0)
			mat\VScale = GetINIFloat(file, TemporaryString, "v_scale",1.0)
		EndIf
	Wend
	
	CloseFile f
	
	CatchErrors("LoadMaterials")
End Function








;~IDEal Editor Parameters:
;~C#Blitz3D