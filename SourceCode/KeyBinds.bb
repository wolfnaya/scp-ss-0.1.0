
Type KeyBinds
	Field NVToggleKey%
	Field CommandWheelKey%
	Field SocialWheelKey%
	Field ChatKey%
End Type

Function LoadKeyBinds()
	
	kb\NVToggleKey = GetINIInt(gv\OptionFile, "binds", "Nightvision key", 33)
	kb\CommandWheelKey = GetINIInt(gv\OptionFile, "binds", "Command wheel key", 46)
	kb\SocialWheelKey = GetINIInt(gv\OptionFile, "binds", "Social wheel key", 47)
	kb\ChatKey = GetINIInt(gv\OptionFile, "binds", "Chat key", 28)
	
End Function

Function SaveKeyBinds()
	
	PutINIValue(gv\OptionFile, "binds", "Nightvision key", kb\NVToggleKey)
	PutINIValue(gv\OptionFile, "binds", "Command wheel key", kb\CommandWheelKey)
	PutINIValue(gv\OptionFile, "binds", "Social wheel key", kb\SocialWheelKey)
	PutINIValue(gv\OptionFile, "binds", "Chat key", kb\ChatKey)
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D