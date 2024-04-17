.lib "FileRequester.dll"
RequestFile$(hwnd%,filter$,save%,flags%,deffilename$):"_WinApi_ChooseFile@20"

EnableDragDrop(hwnd%,enable%):"_EnableDragDrop@8"
GetDragDrop$():"_GetDragDrop@0"