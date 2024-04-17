
Function FillRoom_Room2_Offices_5(r.Rooms)
	Local d.Doors
	
	InitFluLight(0,FLU_STATE_OFF,r)
	InitFluLight(1,FLU_STATE_ON,r)
	InitFluLight(2,FLU_STATE_FLICKER,r)
	
	d = CreateDoor(r\zone,r\x+1792.0*RoomScale,r\y,r\z-1024.0*RoomScale,0,r,False,False)
	d\locked = True : d\DisableWaypoint = True
	
	d = CreateDoor(r\zone,r\x+1792.0*RoomScale,r\y,r\z+1024.0*RoomScale,0,r,False,False)
	d\locked = True : d\DisableWaypoint = True
	
End Function

Function UpdateEvent_Room2_Offices_5(e.Events)
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D