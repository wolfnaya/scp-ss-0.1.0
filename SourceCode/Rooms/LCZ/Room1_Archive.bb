
Function FillRoom_Room1_Archive(r.Rooms)
	Local it.Items,sc.SecurityCams
	Local tempstr$,tempstr2$,temp3%,x#,y#,z#
	Local xtemp,ytemp,ztemp
	
	For xtemp = 0 To 1
		For ytemp = 0 To 2
			For ztemp = 0 To 2
				
				tempstr$ = GetLocalString("Item Names","bat_9v") : tempstr2$ = "bat"
				chance% = Rand(-10,100)
				Select True
					Case (chance<0)
						Exit
					Case (chance<40) ;40% chance for a document
;						tempstr="Document SCP-"
;						Select Rand(1,6)
;							Case 1
;								tempstr=tempstr+"1123"
;							Case 2
;								tempstr=tempstr+"1048"
;							Case 3
;								tempstr=tempstr+"939"
;							Case 4
;								tempstr=tempstr+"682"
;							Case 5
;								tempstr=tempstr+"079"
;							Case 6
;								tempstr=tempstr+"096"
;							Case 6
;								tempstr=tempstr+"966"
;						End Select
;						tempstr2="paper"
						Exit
					Case (chance>=40) And (chance<45) ;5% chance for a key card
						temp3%=Rand(1,2)
						tempstr=GetLocalString("Item Names","key_"+Str(temp3)+"")
						tempstr2="key"+Str(temp3)
					Case (chance>=45) And (chance<50) ;5% chance for a medkit
						tempstr=GetLocalString("Item Names","first_aid")
						tempstr2="firstaid"
					Case (chance>=50) And (chance<60) ;10% chance for a battery
						tempstr=GetLocalString("Item Names","bat_9v")
						tempstr2="bat"
					Case (chance>=60) And (chance<70) ;10% chance for an SNAV
						tempstr=GetLocalString("Item Names","nav_300")
						tempstr2="nav"
					Case (chance>=70) And (chance<85) ;15% chance for a radio
						tempstr=GetLocalString("Item Names","radio")
						tempstr2="radio"
					;Case (chance>=85) And (chance<95) ;10% chance for a clipboard
						;tempstr=GetLocalString("Item Names","clipboard")
						;tempstr2="clipboard"
					Case (chance>=85) And (chance=<100) ;5% chance for misc
						temp3%=Rand(1,3)
						Select temp3
							Case 1 ;playing card
								tempstr=GetLocalString("Item Names","playing_card")
							Case 2 ;Mastercard
								tempstr=GetLocalString("Item Names","mastercard")
							Case 3 ;origami
								tempstr=GetLocalString("Item Names","origami")
						End Select
						tempstr2="misc"
				End Select
				
				x# = (-672.0 + 864.0 * xtemp)* RoomScale
				y# = (96.0  + 96.0 * ytemp) * RoomScale
				z# = (480.0 - 352.0*ztemp + Rnd(-96.0,96.0)) * RoomScale
				
				it = CreateItem(tempstr,tempstr2,r\x+x,y,r\z+z)
				EntityParent it\collider,r\obj							
			Next
		Next
	Next
	
	r\RoomDoors[0] = CreateDoor(r\zone,r\x,r\y,r\z - 528.0 * RoomScale,0,r,False,False,KEY_CARD_1)
	
	sc.SecurityCams = CreateSecurityCam(r\x-256.0*RoomScale, r\y+384.0*RoomScale, r\z+640.0*RoomScale, r)
	sc\angle = 180
	TurnEntity(sc\CameraObj, 20, 0, 0)
	sc\ID = 1
	
End Function

Function UpdateEvent_Room1_Archive(e.Events)
	
	If e\EventState[0] = 0
		e\EventState[0] = Rand(1,3)
	Else
		e\room\RoomDoors[0]\KeyCard = e\EventState[0]
	EndIf
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D