
; ~ Math constants

Const Vector_X = 0
Const Vector_Y = 1
Const Vector_Z = 2
Const Rotator_Pitch = 0
Const Rotator_Yaw = 1
Const Rotator_Roll = 2

; ~ Data type constants

Const BYTE_MIN = 0
Const BYTE_MAX = 255
Const SHORT_MIN = 0
Const SHORT_MAX = 65535
Const INT_MIN = -2147483648
Const INT_MAX = 2147483647

;! ~ [Global functions]

Function GenerateSeedNumber(seed$)
	Local i%
 	Local temp% = 0
 	Local shift% = 0
	
 	For i = 1 To Len(seed)
 		temp = temp Xor (Asc(Mid(seed,i,1)) Shl shift)
 		shift=(shift+1) Mod 24
	Next
	
 	Return temp
End Function

Function CurveValue#(number#, old#, smooth#)
	
	If number < old Then
		Return Max(old + (number - old) * (1.0 / smooth * FPSfactor), number)
	Else
		Return Min(old + (number - old) * (1.0 / smooth * FPSfactor), number)
	EndIf
	
End Function

Function Inverse#(number#)
	
	Return Float(1.0-number#)
	
End Function

Function Rnd_Array#(numb1#,numb2#,Array1#,Array2#)
	Local whatarray% = Rand(1,2)
	
	If whatarray% = 1
		Return Rnd(Array1#,numb1#)
	Else
		Return Rnd(numb2#,Array2#)
	EndIf
	
End Function

;! ~ [Angle calculation]

Function WrapAngle#(angle#,wraparound%=0)
	If angle = Infinity Then Return 0.0
	While angle < 0 - wraparound
		angle = angle + 360
	Wend
	While angle >= 360 - wraparound
		angle = angle - 360
	Wend
	
	Return angle
End Function

Function CurveAngle#(val#, old#, smooth#)
	Local diff# = WrapAngle(val) - WrapAngle(old)
	
	If diff > 180 Then diff = diff - 360
	If diff < - 180 Then diff = diff + 360
	
	Return WrapAngle(old + diff * (1.0 / smooth * FPSfactor))
End Function

Function GetAngle#(x1#, y1#, x2#, y2#)
	Return ATan2( y2 - y1, x2 - x1 )
End Function

Function point_direction#(x1#,z1#,x2#,z2#)
	Local dx#, dz#
	dx = x1 - x2
	dz = z1 - z2
	Return ATan2(dz,dx)
End Function

Function angleDist#(a0#,a1#)
	Local b# = a0-a1
	Local bb#
	If b<-180.0 Then
		bb = b+360.0
	Else If b>180.0 Then
		bb = b-360.0
	Else
		bb = b
	EndIf
	Return bb
End Function

Function ClampAngle#(currAngle#,targetAngle#,deviation#)
    currAngle = WrapAngle(currAngle,180)
    targetAngle = WrapAngle(targetAngle,180)
    If Abs(currAngle-targetAngle)>90 Then
        If (currAngle>0)<>(targetAngle>0) Then
            If currAngle>0 Then
                targetAngle=targetAngle+360.0
            Else
                currAngle=currAngle+360.0
            EndIf
        EndIf
    EndIf
    currAngle = Clamp(currAngle,(targetAngle-deviation),(targetAngle+deviation))
    Return WrapAngle(currAngle)
End Function

;! ~ [Distance calculation]

Function CircleToLineSegIsect%(cx#, cy#, r#, l1x#, l1y#, l2x#, l2y#)

	r = PowTwo(r)
	
	If DistanceSquared(cx, l1x, cy, l1y) <= r Lor DistanceSquared(cx, l2x, cy, l2y) <= r Then
		Return True
	EndIf
	
	Local SegVecX# = l2x - l1x
	Local SegVecY# = l2y - l1y
	
	Local PntVec1X# = cx - l1x
	Local PntVec1Y# = cy - l1y
	
	Local PntVec2X# = cx - l2x
	Local PntVec2Y# = cy - l2y
	
	Local dp1# = SegVecX * PntVec1X + SegVecY * PntVec1Y
	Local dp2# = -SegVecX * PntVec2X - SegVecY * PntVec2Y
	
	If dp1 = 0 Lor dp2 = 0 Then
	ElseIf (dp1 > 0 And dp2 > 0) Lor (dp1 < 0 And dp2 < 0) Then
		
	Else
		Return False
	EndIf
	
	Local a# = (l2y - l1y) / (l2x - l1x)
	Local b# = -1
	Local c# = -(l2y - l1y) / (l2x - l1x) * l1x + l1y
	
	If PowTwo(Abs(a * cx + b * cy + c) / Sqr(a * a + b * b)) > r Then Return False
	Return True
End Function

;! ~ [Grenade calculation]

Function Distance3#(x1#, y1#, z1#, x2#, y2#, z2#)
	Return ((x2-x1)^2 + (y2-y1)^2 + (z2-z1)^2)^0.5
End Function

Function SQRvalue#(val#)
	Return Sqr(val*val)
End Function

Function FlipAngle#(angle#)
	If angle < 0 Then
		Return SQRvalue(angle)
	Else
		Return -angle
	EndIf
End Function

;! ~ [Vector functions]

Type Vector2D
	Field x#
	Field y#
	Field persistent%
End Type

Function CreateVector2D.Vector2D(x#, y#, persistent%=False)
	Local v2d.Vector2D = New Vector2D
	
	v2d\x = x
	v2d\y = y
	v2d\persistent = persistent
	
	Return v2d
End Function

Function FreeVector2D(v2d.Vector2D)
	
	Delete v2d
	
End Function

Function DeleteVectors2D()
	Local v2d.Vector2D
	
	For v2d = Each Vector2D
		If (Not v2d\persistent) Then
			Delete v2d
		EndIf
	Next
	
End Function

Type Vector3D
	Field x#
	Field y#
	Field z#
	Field persistent%
End Type

Function CreateVector3D.Vector3D(x#, y#, z#, persistent%=False)
	Local v3d.Vector3D = New Vector3D
	
	v3d\x = x
	v3d\y = y
	v3d\z = z
	v3d\persistent = persistent
	
	Return v3d
End Function

Function FreeVector3D(v3d.Vector3D)
	
	Delete v3d
	
End Function

Function DeleteVectors3D()
	Local v3d.Vector3D
	
	For v3d = Each Vector3D
		If (Not v3d\persistent) Then
			Delete v3d
		EndIf
	Next
	
End Function

;! ~ [Colission functions]

Function MakeCollBox(mesh%)
	Local sx# = EntityScaleX(mesh, 1)
	Local sy# = Max(EntityScaleY(mesh, 1), 0.001)
	Local sz# = EntityScaleZ(mesh, 1)
	GetMeshExtents(mesh)
	EntityBox mesh, Mesh_MinX * sx, Mesh_MinY * sy, Mesh_MinZ * sz, Mesh_MagX * sx, Mesh_MagY * sy, Mesh_MagZ * sz
End Function

Function GetMeshExtents(Mesh%)
	Local s%, surf%, surfs%, v%, verts%, x#, y#, z#
	Local minx# = Infinity
	Local miny# = Infinity
	Local minz# = Infinity
	Local maxx# = -Infinity
	Local maxy# = -Infinity
	Local maxz# = -Infinity
	
	surfs = CountSurfaces(Mesh)
	
	For s = 1 To surfs
		surf = GetSurface(Mesh, s)
		verts = CountVertices(surf)
		
		For v = 0 To verts - 1
			x = VertexX(surf, v)
			y = VertexY(surf, v)
			z = VertexZ(surf, v)
			
			If (x < minx) Then minx = x
			If (x > maxx) Then maxx = x
			If (y < miny) Then miny = y
			If (y > maxy) Then maxy = y
			If (z < minz) Then minz = z
			If (z > maxz) Then maxz = z
		Next
	Next
	
	Mesh_MinX = minx
	Mesh_MinY = miny
	Mesh_MinZ = minz
	Mesh_MaxX = maxx
	Mesh_MaxY = maxy
	Mesh_MaxZ = maxz
	Mesh_MagX = maxx-minx
	Mesh_MagY = maxy-miny
	Mesh_MagZ = maxz-minz
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D_TSS