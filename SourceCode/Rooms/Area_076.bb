
Function FillRoom_Area_076(r.Rooms)
	
	r\Objects[0] = CreateButton(r\x + 416.0*RoomScale, r\y + 160.0*RoomScale, r\z + 992.0*RoomScale,0,0,0)
	EntityParent (r\Objects[0],r\obj)
	
End Function

Function UpdateEvent_Area_076(e.Events)
	Local dr.Doors,wayp.WayPoints,it.Items
	Local oldseed%,seedDec$
	Local dir%,ix%,iy%,count%,tempInt%,tempInt2%,maxX%,canRetry%,firstX%,lastX%,firstY%,lastY%,tempStr$,ia%,ib%,ic%,id%,dist#
	Local Meshes[6]
	Local i
	
	If EntityY(Collider,True)>=8.0 And EntityY(Collider,True)<=12.0 Then
		If (EntityX(Collider,True)>=e\room\x-6.0) And (EntityX(Collider,True)<=(e\room\x+2.0*gridsz+6.0)) Then
			If (EntityZ(Collider,True)>=e\room\z-6.0) And (EntityZ(Collider,True)<=(e\room\z+2.0*gridsz+6.0)) Then
				PlayerRoom=e\room
			EndIf
		EndIf
	EndIf
	
	If PlayerRoom = e\room Then
		ShouldPlay = 31
	EndIf
	
	;Local tempStr$
	
	;Local ia%,ib%,ic%,id%
	;Local dr.Doors
	
	;Local tempInt%,tempInt2%
	;Local ix%,iy%
	
	If e\room\grid = Null Then
		
		e\room\grid = New Grids
		
		oldseed% = RndSeed()
		;Local seedDec$ = ""
		For i = 1 To Len(RandomSeed)
			seedDec = seedDec+Asc(Mid(RandomSeed,i,1))
		Next
		SeedRnd Abs(Int(seedDec))
		
		;Local dir%
		
		dir=Rand(0,1) Shl 1
		;0 = right
		;1 = up
		;2 = left
		;3 = down
		
		ix=gridsz/2+Rand(-2,2)
		iy=gridsz/2+Rand(-2,2)
		
		e\room\grid\grid[ix+(iy*gridsz)]=1
		
		If dir=2 Then e\room\grid\grid[(ix+1)+(iy*gridsz)]=1 Else e\room\grid\grid[(ix-1)+(iy*gridsz)]=1
		
		;;Local count% = 2
		
		While count<100
			tempInt=Rand(1,5) Shl Rand(1,2)
			For i=1 To tempInt
				
				tempInt2=True
				
				Select dir
					Case 0
						If ix<gridsz-2-(i Mod 2) Then ix=ix+1 Else tempInt2=False
					Case 1
						If iy<gridsz-2-(i Mod 2) Then iy=iy+1 Else tempInt2=False
					Case 2
						If ix>1+(i Mod 2) Then ix=ix-1 Else tempInt2=False
					Case 3
						If iy>1+(i Mod 2) Then iy=iy-1 Else tempInt2=False
				End Select
				
				If tempInt2 Then
					If e\room\grid\grid[ix+(iy*gridsz)]=0 Then
						e\room\grid\grid[ix+(iy*gridsz)]=1
						count=count+1
					EndIf
				Else
					Exit
				EndIf
			Next
			dir=dir+((Rand(0,1) Shl 1)-1)
			While dir<0
				dir=dir+4
			Wend
			While dir>3
				dir=dir-4
			Wend
		Wend
		
		;generate the tunnels
		For iy=0 To gridsz-1
			For ix=0 To gridsz-1
				If e\room\grid\grid[ix+(iy*gridsz)]>0 Then
					e\room\grid\grid[ix+(iy*gridsz)]=(e\room\grid\grid[(ix)+((iy+1)*gridsz)]>0)+(e\room\grid\grid[(ix)+((iy-1)*gridsz)]>0)+(e\room\grid\grid[(ix+1)+((iy)*gridsz)]>0)+(e\room\grid\grid[(ix-1)+((iy)*gridsz)]>0)
				EndIf
			Next
		Next
		
		;Local maxX%=gridsz-1
		;Local canRetry%=0
		
		For ix=0 To maxX
			For iy=0 To gridsz-1
				If e\room\grid\grid[ix+1+(iy*gridsz)]>0 Then
					maxX=ix
					If (e\room\grid\grid[ix+1+((iy+1)*gridsz)]<3) And (e\room\grid\grid[ix+1+((iy-1)*gridsz)]<3) Then
						canRetry=1
						If Rand(0,1)=1 Then
							e\room\grid\grid[ix+1+((iy)*gridsz)]=e\room\grid\grid[ix+1+((iy)*gridsz)]+1
							e\room\grid\grid[ix+((iy)*gridsz)]=7 ;generator room
							canRetry=0
							Exit
						EndIf
					EndIf
				EndIf
			Next
			If canRetry Then ix=ix-1
		Next
		
		;Local firstX%,lastX%
		;Local firstY%,lastY%
		
		firstX=-1
		lastY=-1
		firstX=-1
		lastY=-1
		
		For iy=0 To gridsz-1
			For ix=0 To gridsz-1
				If e\room\grid\grid[ix+(iy*gridsz)]=2 Then
					If e\room\grid\grid[(ix+1)+((iy)*gridsz)]>0 And e\room\grid\grid[(ix-1)+((iy)*gridsz)]>0 Then ;horizontal
						If firstX=-1 Lor firstY=-1 Then
							If e\room\grid\grid[ix-1+(iy*gridsz)]<3 And e\room\grid\grid[ix+1+(iy*gridsz)]<3 And e\room\grid\grid[ix+((iy-1)*gridsz)]<3 And e\room\grid\grid[ix+((iy+1)*gridsz)]<3 Then
								If e\room\grid\grid[ix-1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix+1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix+1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix-1+((iy+1)*gridsz)]<1 Then
									firstX=ix : firstY=iy
								EndIf
							EndIf
						EndIf
						If e\room\grid\grid[ix-1+(iy*gridsz)]<3 And e\room\grid\grid[ix+1+(iy*gridsz)]<3 And e\room\grid\grid[ix+((iy-1)*gridsz)]<3 And e\room\grid\grid[ix+((iy+1)*gridsz)]<3 Then
							If e\room\grid\grid[ix-1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix+1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix+1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix-1+((iy+1)*gridsz)]<1 Then
								lastX=ix : lastY=iy
							EndIf
						EndIf
					ElseIf e\room\grid\grid[(ix)+((iy+1)*gridsz)]>0 And e\room\grid\grid[(ix)+((iy-1)*gridsz)]>0 Then ;vertical
						If firstX=-1 Lor firstY=-1 Then
							If e\room\grid\grid[ix-1+(iy*gridsz)]<3 And e\room\grid\grid[ix+1+(iy*gridsz)]<3 And e\room\grid\grid[ix+((iy-1)*gridsz)]<3 And e\room\grid\grid[ix+((iy+1)*gridsz)]<3 Then
								If e\room\grid\grid[ix-1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix+1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix+1+((iy-1)*gridsz)]<1 And e\room\grid\grid[x-1+((iy+1)*gridsz)]<1 Then
									firstX=ix : firstY=iy
								EndIf
							EndIf
						EndIf
						If e\room\grid\grid[ix-1+(iy*gridsz)]<3 And e\room\grid\grid[ix+1+(iy*gridsz)]<3 And e\room\grid\grid[ix+((iy-1)*gridsz)]<3 And e\room\grid\grid[ix+((iy+1)*gridsz)]<3 Then
							If e\room\grid\grid[ix-1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix+1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix+1+((iy-1)*gridsz)]<1 And e\room\grid\grid[ix-1+((iy+1)*gridsz)]<1 Then
								lastX=ix : lastY=iy
							EndIf
						EndIf
					EndIf
				EndIf
			Next
		Next
		
		If lastX=firstX And lastY=firstY Then
			RuntimeError("SCP-076's area could not be generated properly!")
		EndIf
		
		;place the tunnels
		
		For i=0 To 4
			Select True
				Case i=2
					tempStr="2c"
				Case i>2
					tempStr=Str(i)
				Default
					tempStr=Str(i+1)
			End Select
			Meshes[i]=LoadRMesh("GFX\map\rooms\area_076\area076_room"+tempStr+"_opt.rmesh",Null)
			DebugLog i
			HideEntity Meshes[i]
		Next
		
		Meshes[5]=LoadRMesh("GFX\map\rooms\area_076\area076_leaveelevator_opt.rmesh",Null)
		HideEntity Meshes[5]
		
		;FreeTextureCache
		
		tempInt=0
		
		For iy=0 To gridsz-1
			For ix=0 To gridsz-1
				If e\room\grid\grid[ix+(iy*gridsz)]>0 Then
					
					Select e\room\grid\grid[ix+(iy*gridsz)]
						Case 1,7
							
							tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]-1])
							
							If e\room\grid\grid[(ix+1)+((iy)*gridsz)]>0 Then
								RotateEntity tempInt,0,90,0
								e\room\grid\angles[ix+(iy*gridsz)]=1
							ElseIf e\room\grid\grid[(ix-1)+((iy)*gridsz)]>0 Then
								RotateEntity tempInt,0,270,0
								e\room\grid\angles[ix+(iy*gridsz)]=3
							ElseIf e\room\grid\grid[(ix)+((iy+1)*gridsz)]>0 Then
								RotateEntity tempInt,0,180,0
								e\room\grid\angles[ix+(iy*gridsz)]=2
							Else
								RotateEntity tempInt,0,0,0
								e\room\grid\angles[ix+(iy*gridsz)]=0
							EndIf
						Case 2
							
							If (ix=firstX And iy=firstY) Lor (ix=lastX And iy=lastY) Then
								e\room\grid\grid[ix+(iy*gridsz)]=6
							EndIf
							
							If e\room\grid\grid[(ix+1)+((iy)*gridsz)]>0 And e\room\grid\grid[(ix-1)+((iy)*gridsz)]>0 Then ;horizontal
								tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]-1])
								
								
								tempInt2=Rand(0,1)
								RotateEntity tempInt,0.0,tempInt2*180.0+90,0.0
								
								e\room\grid\angles[ix+(iy*gridsz)]=(tempInt2*2)+1
							ElseIf e\room\grid\grid[(ix)+((iy+1)*gridsz)]>0 And e\room\grid\grid[(ix)+((iy-1)*gridsz)]>0 Then ;vertical
								tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]-1])
								
								
								tempInt2=Rand(0,1)
								RotateEntity tempInt,0.0,tempInt2*180.0,0.0
								e\room\grid\angles[ix+(iy*gridsz)]=(tempInt2*2)
							Else
								tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]])
								
								
								ia=e\room\grid\grid[(ix)+((iy+1)*gridsz)]
								ib=e\room\grid\grid[(ix)+((iy-1)*gridsz)]
								ic=e\room\grid\grid[(ix+1)+((iy)*gridsz)]
								id=e\room\grid\grid[(ix-1)+((iy)*gridsz)]
								
								If ia>0 And ic>0 Then
									RotateEntity tempInt,0,0,0
									e\room\grid\angles[ix+(iy*gridsz)]=0
								ElseIf ia>0 And id>0 Then
									RotateEntity tempInt,0,90,0
									e\room\grid\angles[ix+(iy*gridsz)]=1
								ElseIf ib>0 And ic>0 Then
									RotateEntity tempInt,0,270,0
									e\room\grid\angles[ix+(iy*gridsz)]=3
								Else
									RotateEntity tempInt,0,180,0
									e\room\grid\angles[ix+(iy*gridsz)]=2
								EndIf
							EndIf
							
							If (ix=firstX And iy=firstY) Then
								e\room\grid\grid[ix+(iy*gridsz)]=5
							EndIf
							
						Case 3
							tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]])
							
							ia=e\room\grid\grid[(ix)+((iy+1)*gridsz)]
							ib=e\room\grid\grid[(ix)+((iy-1)*gridsz)]
							ic=e\room\grid\grid[(ix+1)+((iy)*gridsz)]
							id=e\room\grid\grid[(ix-1)+((iy)*gridsz)]
							If ia>0 And ic>0 And id>0 Then
								RotateEntity tempInt,0,90,0
								e\room\grid\angles[ix+(iy*gridsz)]=1
							ElseIf ib>0 And ic>0 And id>0 Then
								RotateEntity tempInt,0,270,0
								e\room\grid\angles[ix+(iy*gridsz)]=3
							ElseIf ic>0 And ia>0 And ib>0 Then
								RotateEntity tempInt,0,0,0
								e\room\grid\angles[ix+(iy*gridsz)]=0
							Else
								RotateEntity tempInt,0,180,0
								e\room\grid\angles[ix+(iy*gridsz)]=2
							EndIf
						Case 4
							tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]])
							
							tempInt2=Rand(0,3)
							RotateEntity tempInt,0,tempInt2*90.0,0
							
							e\room\grid\angles[ix+(iy*gridsz)]=tempInt2
					End Select
					
					ScaleEntity tempInt,RoomScale,RoomScale,RoomScale,True
					PositionEntity tempInt,e\room\x+ix*8.0,8.0,e\room\z+iy*8.0,True
					
					If e\room\grid\grid[ix+(iy*gridsz)]=6 Lor e\room\grid\grid[ix+(iy*gridsz)]=5 Then
						dr=CreateDoor(e\room\zone,e\room\x+(ix*2.0)+(Cos(EntityYaw(tempInt,True))*240.0*RoomScale),8.0,e\room\z+(iy*2.0)+(Sin(EntityYaw(tempInt,True))*240.0*RoomScale),EntityYaw(tempInt,True)+90.0,Null,False,False,False,"")
						PositionEntity dr\buttons[0],EntityX(dr\buttons[0],True)+(Cos(EntityYaw(tempInt,True))*0.05),EntityY(dr\buttons[0],True)+0.0,EntityZ(dr\buttons[0],True)+(Sin(EntityYaw(tempInt,True))*0.05),True
						
						
						tempInt2=CreatePivot()
						RotateEntity tempInt2,0,EntityYaw(tempInt,True)+180.0,0,True
						PositionEntity tempInt2,e\room\x+(ix*2.0)+(Cos(EntityYaw(tempInt,True))*552.0*RoomScale),8.0+(240.0*RoomScale),e\room\z+(iy*2.0)+(Sin(EntityYaw(tempInt,True))*552.0*RoomScale)
						If e\room\grid\grid[ix+(iy*gridsz)]=6 Then
							e\room\RoomDoors[1]=dr
						Else
							e\room\RoomDoors[3]=dr
						EndIf
					EndIf
					
					e\room\grid\Entities[ix+(iy*gridsz)]=tempInt
					
					wayp.WayPoints = CreateWaypoint(e\room\x+(ix*2.0),8.2,e\room\z+(iy*2.0),Null,e\room)
					
					e\room\grid\waypoints[ix+(iy*gridsz)]=wayp
					
					If iy<gridsz-1 Then
						If e\room\grid\waypoints[ix+((iy+1)*gridsz)]<>Null Then
							dist=EntityDistance(e\room\grid\waypoints[ix+(iy*gridsz)]\obj,e\room\grid\waypoints[ix+((iy+1)*gridsz)]\obj) ;TODO waypoints squared? check whole file
							For i=0 To 3
								If e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+((iy+1)*gridsz)] Then
									Exit
								ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
									e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+((iy+1)*gridsz)]
									e\room\grid\waypoints[ix+(iy*gridsz)]\dist[i]=dist
									Exit
								EndIf
							Next
							For i=0 To 3
								If e\room\grid\waypoints[ix+((iy+1)*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)] Then
									Exit
								ElseIf e\room\grid\waypoints[ix+((iy+1)*gridsz)]\connected[i]=Null Then
									e\room\grid\waypoints[ix+((iy+1)*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)]
									e\room\grid\waypoints[ix+((iy+1)*gridsz)]\dist[i]=dist
									Exit
								EndIf
							Next
						EndIf
					EndIf
					If iy>0 Then
						If e\room\grid\waypoints[ix+((iy-1)*gridsz)]<>Null Then
							dist=EntityDistance(e\room\grid\waypoints[ix+(iy*gridsz)]\obj,e\room\grid\waypoints[ix+((iy-1)*gridsz)]\obj)
							For i=0 To 3
								If e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+((iy-1)*gridsz)] Then
									Exit
								ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
									e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+((iy-1)*gridsz)]
									e\room\grid\waypoints[ix+(iy*gridsz)]\dist[i]=dist
									Exit
								EndIf
							Next
							For i=0 To 3
								If e\room\grid\waypoints[ix+((iy-1)*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)] Then
									Exit
								ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
									e\room\grid\waypoints[ix+((iy-1)*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)]
									e\room\grid\waypoints[ix+((iy-1)*gridsz)]\dist[i]=dist
									Exit
								EndIf
							Next
						EndIf
					EndIf
					If ix>0 Then
						If e\room\grid\waypoints[ix-1+(iy*gridsz)]<>Null Then
							dist=EntityDistance(e\room\grid\waypoints[ix+(iy*gridsz)]\obj,e\room\grid\waypoints[ix-1+(iy*gridsz)]\obj)
							For i=0 To 3
								If e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix-1+(iy*gridsz)] Then
									Exit
								ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
									e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix-1+(iy*gridsz)]
									e\room\grid\waypoints[ix+(iy*gridsz)]\dist[i]=dist
									Exit
								EndIf
							Next
							For i=0 To 3
								If e\room\grid\waypoints[ix-1+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)] Then
									Exit
								ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
									e\room\grid\waypoints[ix-1+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)]
									e\room\grid\waypoints[ix-1+(iy*gridsz)]\dist[i]=dist
									Exit
								EndIf
							Next
						EndIf
					EndIf
					If ix<gridsz-1 Then
						If e\room\grid\waypoints[ix+1+(iy*gridsz)]<>Null Then
							dist=EntityDistance(e\room\grid\waypoints[ix+(iy*gridsz)]\obj,e\room\grid\waypoints[ix+1+(iy*gridsz)]\obj)
							For i=0 To 3
								If e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+1+(iy*gridsz)] Then
									Exit
								ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
									e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+1+(iy*gridsz)]
									e\room\grid\waypoints[ix+(iy*gridsz)]\dist[i]=dist
									Exit
								EndIf
							Next
							For i=0 To 3
								If e\room\grid\waypoints[ix+1+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)] Then
									Exit
								ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
									e\room\grid\waypoints[ix+1+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)]
									e\room\grid\waypoints[ix+1+(iy*gridsz)]\dist[i]=dist
									Exit
								EndIf
							Next
						EndIf
					EndIf
					
				EndIf
			Next
		Next
		
		For i=0 To 6
			e\room\grid\Meshes[i]=Meshes[i]
			;Meshes[i] = FreeEntity_Strict(Meshes[i])
		Next
		
	Else If e\room\grid\Meshes[0]=0 Then
		
		;place the tunnels
		
		For i=0 To 4
			Select True
				Case i=2
					tempStr="2c"
				Case i>2
					tempStr=Str(i)
				Default
					tempStr=Str(i+1)
			End Select
			Meshes[i]=LoadRMesh("GFX\map\rooms\area_076\area076_room"+tempStr+"_opt.rmesh",Null)
			DebugLog i
			HideEntity Meshes[i]
		Next
		
		Meshes[5]=LoadRMesh("GFX\map\rooms\area_076\area076_leaveelevator_opt.rmesh",Null)
		HideEntity Meshes[5]
		
		;FreeTextureCache
		
		tempInt=0
		
		For iy=0 To gridsz-1
			For ix=0 To gridsz-1
				If e\room\grid\grid[ix+(iy*gridsz)]>0 Then
					Select e\room\grid\grid[ix+(iy*gridsz)]
						Case 1,7
							tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]-1])
						Case 2
							If e\room\grid\grid[(ix+1)+((iy)*gridsz)]>0 And e\room\grid\grid[(ix-1)+((iy)*gridsz)]>0 Then ;horizontal
								tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]-1])
								AddLight%(Null, e\room\x+ix*2.0, 8.0+(368.0*RoomScale), e\room\z+iy*2.0, 2, 500.0 * RoomScale, 255, 255, 255)
							ElseIf e\room\grid\grid[(ix)+((iy+1)*gridsz)]>0 And e\room\grid\grid[(ix)+((iy-1)*gridsz)]>0 Then ;vertical
								tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]-1])
								AddLight%(Null, e\room\x+ix*2.0, 8.0+(368.0*RoomScale), e\room\z+iy*2.0, 2, 500.0 * RoomScale, 255, 255, 255)
							Else
								tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]])
								AddLight%(Null, e\room\x+ix*2.0, 8.0+(412.0*RoomScale), e\room\z+iy*2.0, 2, 500.0 * RoomScale, 255, 255, 255)
							EndIf
						Case 3,4
							tempInt%=CopyEntity(Meshes[e\room\grid\grid[ix+(iy*gridsz)]])
						Case 5,6
							tempInt%=CopyEntity(Meshes[5])
					End Select
					
					ScaleEntity tempInt,RoomScale,RoomScale,RoomScale,True
					
					RotateEntity tempInt,0,e\room\grid\angles[ix+(iy*gridsz)]*90.0,0
					PositionEntity tempInt,e\room\x+ix*8.0,8.0,e\room\z+iy*8.0,True
					
					Select e\room\grid\grid[ix+(iy*gridsz)]
						Case 1,5,6
							AddLight%(Null, e\room\x+ix*2.0, 8.0+(368.0*RoomScale), e\room\z+iy*2.0, 2, 500.0 * RoomScale, 255, 255, 255)
						Case 3,4
							AddLight%(Null, e\room\x+ix*2.0, 8.0+(412.0*RoomScale), e\room\z+iy*2.0, 2, 500.0 * RoomScale, 255, 255, 255)
						Case 7
							AddLight%(Null, e\room\x+ix*2.0-(Sin(EntityYaw(tempInt,True))*504.0*RoomScale)+(Cos(EntityYaw(tempInt,True))*16.0*RoomScale), 8.0+(396.0*RoomScale), e\room\z+iy*2.0+(Cos(EntityYaw(tempInt,True))*504.0*RoomScale)+(Sin(EntityYaw(tempInt,True))*16.0*RoomScale), 2, 500.0 * RoomScale, 255, 200, 200)
					End Select
					
					If e\room\grid\grid[ix+(iy*gridsz)]=6 Lor e\room\grid\grid[ix+(iy*gridsz)]=5 Then
						dr=CreateDoor(e\room\zone,e\room\x+(ix*2.0)+(Cos(EntityYaw(tempInt,True))*240.0*RoomScale),8.0,e\room\z+(iy*2.0)+(Sin(EntityYaw(tempInt,True))*240.0*RoomScale),EntityYaw(tempInt,True)+90.0,Null,False,False,False,"")
						
						AddLight%(Null, e\room\x+ix*2.0+(Cos(EntityYaw(tempInt,True))*555.0*RoomScale), 8.0+(469.0*RoomScale), e\room\z+iy*2.0+(Sin(EntityYaw(tempInt,True))*555.0*RoomScale), 2, 600.0 * RoomScale, 255, 255, 255)
						
						PositionEntity dr\buttons[0],EntityX(dr\buttons[0],True)+(Cos(EntityYaw(tempInt,True))*0.05),EntityY(dr\buttons[0],True)+0.0,EntityZ(dr\buttons[0],True)+(Sin(EntityYaw(tempInt,True))*0.05),True
						tempInt2=CreatePivot()
						RotateEntity tempInt2,0,EntityYaw(tempInt,True)+180.0,0,True
						PositionEntity tempInt2,e\room\x+(ix*2.0)+(Cos(EntityYaw(tempInt,True))*552.0*RoomScale),8.0+(240.0*RoomScale),e\room\z+(iy*2.0)+(Sin(EntityYaw(tempInt,True))*552.0*RoomScale)
						If e\room\grid\grid[ix+(iy*gridsz)]=6 Then
							dr\open = (Not e\room\RoomDoors[0]\open)
							e\room\RoomDoors[1]=dr
						Else
							dr\open = (Not e\room\RoomDoors[2]\open)
							e\room\RoomDoors[3]=dr
						EndIf
					EndIf
					
					e\room\grid\Entities[ix+(iy*gridsz)]=tempInt
					
					wayp.WayPoints = CreateWaypoint(e\room\x+(ix*2.0),8.2,e\room\z+(iy*2.0),Null,e\room)
					
					e\room\grid\waypoints[ix+(iy*gridsz)]=wayp
					
					If iy<gridsz-1 Then
						If e\room\grid\waypoints[ix+((iy+1)*gridsz)]<>Null Then
							dist=EntityDistance(e\room\grid\waypoints[ix+(iy*gridsz)]\obj,e\room\grid\waypoints[ix+((iy+1)*gridsz)]\obj)
							For i=0 To 3
								If e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+((iy+1)*gridsz)] Then
									Exit
								ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
									e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+((iy+1)*gridsz)]
									e\room\grid\waypoints[ix+(iy*gridsz)]\dist[i]=dist
									Exit
								EndIf
							Next
							For i=0 To 3
								If e\room\grid\waypoints[ix+((iy+1)*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)] Then
									Exit
								ElseIf e\room\grid\waypoints[ix+((iy+1)*gridsz)]\connected[i]=Null Then
									e\room\grid\waypoints[ix+((iy+1)*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)]
									e\room\grid\waypoints[ix+((iy+1)*gridsz)]\dist[i]=dist
									Exit
								EndIf
							Next
						EndIf
					EndIf
					If iy>0 Then
						If e\room\grid\waypoints[ix+((iy-1)*gridsz)]<>Null Then
							dist=EntityDistance(e\room\grid\waypoints[ix+(iy*gridsz)]\obj,e\room\grid\waypoints[ix+((iy-1)*gridsz)]\obj)
							For i=0 To 3
								If e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+((iy-1)*gridsz)] Then
									Exit
								ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
									e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+((iy-1)*gridsz)]
									e\room\grid\waypoints[ix+(iy*gridsz)]\dist[i]=dist
									Exit
								EndIf
							Next
							For i=0 To 3
								If e\room\grid\waypoints[ix+((iy-1)*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)] Then
									Exit
								ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
									e\room\grid\waypoints[ix+((iy-1)*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)]
									e\room\grid\waypoints[ix+((iy-1)*gridsz)]\dist[i]=dist
									Exit
								EndIf
							Next
						EndIf
					EndIf
					If ix>0 Then
						If e\room\grid\waypoints[ix-1+(iy*gridsz)]<>Null Then
							dist=EntityDistance(e\room\grid\waypoints[ix+(iy*gridsz)]\obj,e\room\grid\waypoints[ix-1+(iy*gridsz)]\obj)
							For i=0 To 3
								If e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix-1+(iy*gridsz)] Then
									Exit
								ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
									e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix-1+(iy*gridsz)]
									e\room\grid\waypoints[ix+(iy*gridsz)]\dist[i]=dist
									Exit
								EndIf
							Next
							For i=0 To 3
								If e\room\grid\waypoints[ix-1+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)] Then
									Exit
								ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
									e\room\grid\waypoints[ix-1+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)]
									e\room\grid\waypoints[ix-1+(iy*gridsz)]\dist[i]=dist
									Exit
								EndIf
							Next
						EndIf
					EndIf
					If ix<gridsz-1 Then
						If e\room\grid\waypoints[ix+1+(iy*gridsz)]<>Null Then
							dist=EntityDistance(e\room\grid\waypoints[ix+(iy*gridsz)]\obj,e\room\grid\waypoints[ix+1+(iy*gridsz)]\obj)
							For i=0 To 3
								If e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+1+(iy*gridsz)] Then
									Exit
								ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
									e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+1+(iy*gridsz)]
									e\room\grid\waypoints[ix+(iy*gridsz)]\dist[i]=dist
									Exit
								EndIf
							Next
							For i=0 To 3
								If e\room\grid\waypoints[ix+1+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)] Then
									Exit
								ElseIf e\room\grid\waypoints[ix+(iy*gridsz)]\connected[i]=Null Then
									e\room\grid\waypoints[ix+1+(iy*gridsz)]\connected[i]=e\room\grid\waypoints[ix+(iy*gridsz)]
									e\room\grid\waypoints[ix+1+(iy*gridsz)]\dist[i]=dist
									Exit
								EndIf
							Next
						EndIf
					EndIf
				EndIf
			Next
		Next
		
		For i=0 To 6
			e\room\grid\Meshes[i]=Meshes[i]
		Next
		
		SeedRnd oldseed
		
		For it.Items = Each Items
			If (EntityY(it\collider,True)>=8.0) And (EntityY(it\collider,True)<=12.0) Then
				DebugLog it\name+" is within Y limits"
				If (EntityX(it\collider,True)>=e\room\x-6.0) And (EntityX(it\collider,True)<=(e\room\x+(2.0*gridsz)+6.0)) Then
					DebugLog "and within X limits"
				EndIf
				If (EntityZ(it\collider,True)>=e\room\z-6.0) And (EntityZ(it\collider,True)<=(e\room\z+(2.0*gridsz)+6.0)) Then
					DebugLog "and within Z limits"
				EndIf
			EndIf
			
			If (EntityY(it\collider,True)>=8.0) And (EntityY(it\collider,True)<=12.0) And (EntityX(it\collider,True)>=e\room\x-6.0) And (EntityX(it\collider,True)<=(e\room\x+(2.0*gridsz)+6.0)) And (EntityZ(it\collider,True)>=e\room\z-6.0) And (EntityZ(it\collider,True)<=(e\room\z+(2.0*gridsz)+6.0)) Then
				DebugLog it\name
				TranslateEntity it\collider,0.0,0.3,0.0,True
				ResetEntity it\collider
			EndIf
		Next
		
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~F#1
;~C#Blitz3D