
Global BurntNote%
Global MaxItemAmount%
Global ItemAmount%
Global Inventory.Items[MaxInventorySpace]
Global InvSelect%, SelectedItem.Items

Global ClosestItem.Items

Global LastItemID%

Type ItemTemplates
	Field name$
	Field tempname$
	Field sound%
	Field found%
	Field obj%, objpath$, parentobjpath$
	Field invimg%,invimg2%,invimgpath$
	Field imgpath$, img%
	Field isAnim%
	Field scale#
	Field tex%, texpath$
	Field IsGun% = False
	Field IsForHolster% = False
	Field IsForScabbard% = False
	Field IsHead% = False
	Field IsTorso% = False
	Field IsBackpack% = False
	Field IsFullBody% = False
	Field IsUsable% = False
	Field IsEquippable% = False
	Field fastPickable% = False
	Field isPlot% = False
End Type 

Function CreateItemTemplate.ItemTemplates(name$, tempname$, objpath$, invimgpath$, imgpath$, scale#, texturepath$ = "",invimgpath2$="",Anim%=0, texflags%=9)
	Local it.ItemTemplates = New ItemTemplates, n
	Local it2.ItemTemplates
	
	For it2.ItemTemplates = Each ItemTemplates
		If it2\objpath = objpath And it2\obj <> 0 Then it\obj = CopyEntity(it2\obj) : it\parentobjpath=it2\objpath : Exit
	Next
	
	If it\obj = 0 Then
		If Anim<>0 Then
			it\obj = LoadAnimMesh_Strict(objpath)
			it\isAnim=True
		Else
			it\obj = LoadMesh_Strict(objpath)
			it\isAnim=False
		EndIf
		it\objpath = objpath
	EndIf
	it\objpath = objpath
	
	Local texture%
	
	If texturepath <> "" Then
		For it2.ItemTemplates = Each ItemTemplates
			If it2\texpath = texturepath And it2\tex<>0 Then
				texture = it2\tex
				Exit
			EndIf
		Next
		If texture=0 Then texture=LoadTexture_Strict(texturepath,texflags%,0) : it\texpath = texturepath
		EntityTexture it\obj, texture
		it\tex = texture
	EndIf  
	
	it\scale = scale
	ScaleEntity it\obj, scale, scale, scale, True
	
	For it2.ItemTemplates = Each ItemTemplates
		If it2\invimgpath = invimgpath And it2\invimg <> 0 Then
			it\invimg = it2\invimg
			If it2\invimg2<>0 Then
				it\invimg2=it2\invimg2
			EndIf
			Exit
		EndIf
	Next
	If it\invimg=0 Then
		it\invimg = LoadImage_Strict(invimgpath)
		it\invimgpath = invimgpath
		MaskImage(it\invimg, 255, 0, 255)
	EndIf
	
	If (invimgpath2 <> "") Then
		If it\invimg2=0 Then
			it\invimg2 = LoadImage_Strict(invimgpath2)
			MaskImage(it\invimg2,255,0,255)
		EndIf
	Else
		it\invimg2 = 0
	EndIf
	
	it\imgpath = imgpath
	it\tempname = tempname
	it\name = name
	it\sound = 1
	
	HideEntity it\obj
	
	Return it
End Function

Function InitItemTemplates()
	Local it.ItemTemplates,it2.ItemTemplates
	
	;! ~ [ATTACHMENTS]
	
	it = CreateItemTemplate(GetLocalString("Item Names", "suppressor"), "suppressor", "GFX\Weapons\Models\Suppressor_Worldmodel.b3d","GFX\items\Icons\Icon_misc.jpg","",0.015) : it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate(GetLocalString("Item Names", "match"), "match", "GFX\Weapons\Models\Match_Worldmodel.b3d","GFX\items\Icons\Icon_misc.jpg","",0.03) : it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate(GetLocalString("Item Names", "acog"), "acog", "GFX\Weapons\Models\Acog_Worldmodel.b3d","GFX\items\Icons\Icon_misc.jpg","",0.02) : it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate(GetLocalString("Item Names", "eotech"), "eotech", "GFX\Weapons\Models\EoTech_Worldmodel.b3d","GFX\items\Icons\Icon_misc.jpg","",0.04) : it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate(GetLocalString("Item Names", "red_dot"), "reddot", "GFX\Weapons\Models\RedDot_Worldmodel.b3d","GFX\items\Icons\Icon_misc.jpg","",0.04) : it\sound = 2 : it\fastPickable = True
;	it = CreateItemTemplate(GetLocalString("Item Names", "laser"), "lasersight", "GFX\Weapons\Models\Attachments\lasersight.b3d","GFX\items\Icons\Icon_misc.jpg","",0.04) : it\sound = 2 : it\fastPickable = True
	it = CreateItemTemplate(GetLocalString("Item Names", "mag"), "extmag", "GFX\Weapons\Models\extmag_box.b3d","GFX\items\Icons\Icon_misc.jpg","",0.02) : it\sound = 2 : it\fastPickable = True
;	it = CreateItemTemplate(GetLocalString("Item Names", "m_u"), "mui", "GFX\Weapons\Models\Attachments\emr-p_mui.b3d","GFX\items\Icons\Icon_misc.jpg","",0.01) : it\sound = 2 : it\fastPickable = True
	
	;! ~ [COMBINABLE ITEMS]
	
	; ~ (Batteries)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "bat_9v"), "bat", "GFX\items\Combinable\Battery.x", "GFX\items\Icons\Icon_battery_9v.jpg", "", 0.008)
	it = CreateItemTemplate(GetLocalString("Item Names", "bat_18v"), "18vbat", "GFX\items\Combinable\Battery.x", "GFX\items\Icons\Icon_battery_18v.jpg", "", 0.01, "GFX\items\Combinable\Battery 18V.jpg")
	it = CreateItemTemplate(GetLocalString("Item Names", "bat_strange"), "killbat", "GFX\items\Combinable\Battery.x", "GFX\items\Icons\Icon_battery_strange.jpg", "", 0.01,"GFX\items\Combinable\Strange Battery.jpg")
	
	; ~ (Wallet)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "wallet"),"wallet", "GFX\items\Combinable\wallet.b3d", "GFX\items\Icons\Icon_wallet.jpg", "", 0.0005,"","GFX\items\Icons\Icon_wallet_empty.jpg",1) : it\sound = 2
	
	; ~ (Other)
	
	it = CreateItemTemplate(GetLocalString("Item Names","scope_bat"), "scopebat", "GFX\Items\Combinable\scope_bat.b3d", "GFX\Items\Icons\Icon_scope_bat.jpg","",0.008) : it\sound = 2
	it = CreateItemTemplate(GetLocalString("Item Names","hds_power_cell"), "hds_fuse", "GFX\items\Combinable\HDS_Battery.b3d", "GFX\items\Icons\Icon_hds_bat.png", "", 0.012) : it\sound = 2 : it\fastPickable = True
	
	;! ~ [CONSUMABLE ITEMS]
	
	; ~ (Cigarettes)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "cigarette"), "cigarette", "GFX\items\SCPs\scp_420.x", "GFX\items\Icons\Icon_scp_420.jpg", "", 0.0004) : it\sound = 2
	it = CreateItemTemplate(GetLocalString("Item Names", "joint"), "420s", "GFX\items\SCPs\scp_420.x", "GFX\items\Icons\Icon_scp_420.jpg", "", 0.0004) : it\sound = 2
	it = CreateItemTemplate(GetLocalString("Item Names", "joint_smelly"), "420s", "GFX\items\SCPs\scp_420.x", "GFX\items\Icons\Icon_scp_420.jpg", "", 0.0004) : it\sound = 2
	
	; ~ (Cups)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "cup"), "cup", "GFX\items\Consumable\cup.x", "GFX\items\Icons\Icon_cup.jpg", "", 0.04) : it\sound = 2
	it = CreateItemTemplate(GetLocalString("Item Names", "cup_empty"), "emptycup", "GFX\items\Consumable\cup.x", "GFX\items\Icons\Icon_cup.jpg", "", 0.04) : it\sound = 2
	
	; ~ (First Aid Kits)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "first_aid"), "firstaid", "GFX\items\Consumable\firstaid.x", "GFX\items\Icons\Icon_first_aid.png", "", 0.05)
	it = CreateItemTemplate(GetLocalString("Item Names", "first_aid_small"), "finefirstaid", "GFX\items\Consumable\first_aid_small.x", "GFX\items\Icons\Icon_first_aid_small.jpg", "", 0.03)
	it = CreateItemTemplate(GetLocalString("Item Names", "first_aid_blue"), "firstaid2", "GFX\items\Consumable\firstaid.x", "GFX\items\Icons\Icon_first_aid (2).png", "", 0.03, "GFX\items\Consumable\firstaidkit2.jpg")
	it = CreateItemTemplate(GetLocalString("Item Names", "strange_bottle"), "veryfinefirstaid", "GFX\items\Consumable\eye_drops.b3d", "GFX\items\Icons\Icon_strange_bottle.jpg", "", 0.002, "GFX\items\Consumable\strange_bottle.jpg")
	
	; ~ (Pills)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "pill"), "pill", "GFX\items\SCPs\scp_500_pill.b3d", "GFX\items\Icons\Icon_scp_500_death_pill.jpg", "", 0.0001) : it\sound = 2 : EntityColor it\obj,255,255,255
	it = CreateItemTemplate(GetLocalString("Item Names", "pill_upgraded"), "scp500death", "GFX\items\SCPs\scp_500_pill.b3d", "GFX\items\Icons\Icon_scp_500_pill.jpg", "", 0.0001) : it\sound = 2 : EntityColor it\obj,255,0,0
	
	; ~ (Syringes)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "syringe"), "syringe", "GFX\items\Consumable\syringe.b3d", "GFX\items\Icons\Icon_syringe.png", "", 0.005) : it\sound = 2
	it = CreateItemTemplate(GetLocalString("Item Names", "syringe"), "finesyringe", "GFX\items\Consumable\syringe.b3d", "GFX\items\Icons\Icon_syringe.png", "", 0.005) : it\sound = 2
	it = CreateItemTemplate(GetLocalString("Item Names", "syringe"), "veryfinesyringe", "GFX\items\Consumable\syringe.b3d", "GFX\items\Icons\Icon_syringe.png", "", 0.005) : it\sound = 2
	
	; ~ (Food)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "beer"), "beer", "GFX\map\props\beer.x", "GFX\items\Icons\Icon_beer.jpg", "", 0.04) : it\sound = 4
	
	it = CreateItemTemplate(GetLocalString("Item Names", "candy"), "red_candy", "GFX\items\Consumable\candy.b3d", "GFX\items\Icons\Icon_candy_red.jpg", "", 0.005,"GFX\Items\Consumable\candy_red.jpg") : it\sound = 0
	it = CreateItemTemplate(GetLocalString("Item Names", "candy"), "blue_candy", "GFX\items\Consumable\candy.b3d", "GFX\items\Icons\Icon_candy_blue.jpg", "", 0.005,"GFX\Items\Consumable\candy_blue.jpg") : it\sound = 0
	it = CreateItemTemplate(GetLocalString("Item Names", "candy"), "yellow_candy", "GFX\items\Consumable\candy.b3d", "GFX\items\Icons\Icon_candy_yellow.jpg", "", 0.005,"GFX\Items\Consumable\candy_yellow.jpg") : it\sound = 0
	
	;! ~ [PAPER ITEMS]
	
	; ~ (Badges)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "badge_emily"), "badge", "GFX\items\Paper\badge.x", "GFX\items\Icons\Icon_badge_emily.jpg", "GFX\items\Paper\badge_emily.jpg", 0.0001, "GFX\items\Paper\badge_emily.jpg")
	it = CreateItemTemplate(GetLocalString("Item Names", "badge_benjamin"), "badge", "GFX\items\Paper\badge.x", "GFX\items\Icons\Icon_badge_d9341.jpg", "GFX\items\Paper\badge_d9341.png", 0.0001, "GFX\items\Paper\badge_d9341.png") : it\isPlot = True
	
	; ~ (Documents)
	
	it = CreateItemTemplate("Laboratory Document", "paper", "GFX\items\Paper\paper.x", "GFX\items\Icons\Icon_paper.jpg", "GFX\items\Paper\doc_lab.jpg", 0.003) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Laboratory Log #1", "paper", "GFX\items\Paper\paper.x", "GFX\items\Icons\Icon_paper.jpg", "GFX\items\Paper\lab_log_1.jpg", 0.003) : it\sound = 0 : it\isPlot = True
	
	; ~ (Notes)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "doc_sci_closet"), "paper", "GFX\items\Paper\note.x", "GFX\items\Icons\Icon_note.jpg", "GFX\items\Paper\note_sci_closet.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate(GetLocalString("Item Names", "note_d_ecape"), "paper", "GFX\items\Paper\note.x", "GFX\items\Icons\Icon_note (2).jpg", "GFX\items\Paper\note_d_escape.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Class-D Zone Note", "paper", "GFX\items\Paper\note.x", "GFX\items\Icons\Icon_note (2).jpg", "GFX\items\Paper\classd_note.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("O5 Council Room Note", "paper", "GFX\items\Paper\note.x", "GFX\items\Icons\Icon_note (2).jpg", "GFX\items\Paper\noteo5.jpg", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Surveillance Room Password Note", "paper", "GFX\items\Paper\note.x", "GFX\items\Icons\Icon_note (2).jpg", "GFX\items\Paper\note_surveil.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("D-7651's Note", "paper", "GFX\items\Paper\note.x", "GFX\items\Icons\Icon_note (2).jpg", "GFX\items\Paper\note1499.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Note from Maynard", "paper", "GFX\items\Paper\note.x", "GFX\items\Icons\Icon_note (2).jpg", "GFX\items\Paper\note_maynard.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Unknown Note", "paper", "GFX\items\Paper\note.x", "GFX\items\Icons\Icon_note.jpg", "GFX\items\Paper\note_friend.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Dr. Harmann's Note", "paper", "GFX\items\Paper\note.x", "GFX\items\Icons\Icon_note.jpg", "GFX\items\Paper\note016.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Dr. Singlinton's Note", "paper", "GFX\items\Paper\note.x", "GFX\items\Icons\Icon_note.jpg", "GFX\items\Paper\note016_2.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Final Note", "paper", "GFX\items\Paper\note.x", "GFX\items\Icons\Icon_note.jpg", "GFX\items\Paper\note016_3.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Strange Message", "paper", "GFX\items\Paper\note.x", "GFX\items\Icons\Icon_note.jpg", "GFX\items\Paper\note268.png", 0.0025) : it\sound = 0 : it\isPlot = True
	it = CreateItemTemplate("Wolfnaya's Room Note", "paper", "GFX\items\Paper\note.x", "GFX\items\Icons\Icon_note.jpg", "GFX\items\Paper\noteWolfnaya.png", 0.0025) : it\sound = 0 : it\isPlot = True
	
	; ~ (Other)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "origami"), "misc", "GFX\items\Paper\origami.b3d", "GFX\items\Icons\Icon_origami.jpg", "", 0.003) : it\sound = 0
	
	;! ~ [SCP ITEMS]
	
	it = CreateItemTemplate("SCP-005","scp005", "GFX\items\SCPs\scp_005.b3d", "GFX\items\Icons\Icon_scp_005.png", "", 0.04) : it\sound = 3
	it = CreateItemTemplate("SCP-035","scp035", "GFX\items\SCPs\scp_035.b3d", "GFX\items\Icons\Icon_scp_035.jpg", "", 0.02) : it\sound = 2
	it = CreateItemTemplate("SCP-109","scp109","GFX\items\SCPs\scp_109.b3d","GFX\items\Icons\Icon_scp_109.jpg","",0.0009,"","",1) : it\sound = 4
	it = CreateItemTemplate("SCP-127","scp127","GFX\weapons\Models\scp127_Worldmodel.b3d","GFX\Weapons\Icons\INVscp127.jpg","", 0.0026) : it\sound = 66 : it\IsGun% = True
	it = CreateItemTemplate("SCP-198","scp198","GFX\items\SCPs\scp_198.b3d","GFX\items\Icons\Icon_scp_198.jpg","",0.04)
	it = CreateItemTemplate("SCP-207","scp207","GFX\items\SCPs\scp_207.b3d","GFX\items\Icons\Icon_scp_207.png", "", 0.14) : it\sound = 4
	it = CreateItemTemplate("SCP-207 Empty Bottle","scp207empty","GFX\items\SCPs\scp_207_empty.b3d","GFX\items\Icons\Icon_scp_207_empty.png", "", 0.14)
	it = CreateItemTemplate("SCP-268","scp268","GFX\items\SCPs\scp_268.b3d","GFX\items\Icons\Icon_scp_268.png", "", 0.09, "GFX\items\SCPs\scp_268.png") : it\sound = 2 : it\IsHead = True
	it = CreateItemTemplate("SCP-268","super268","GFX\items\SCPs\scp_268.b3d","GFX\items\Icons\Icon_scp_268 (2).png", "", 0.09, "GFX\items\SCPs\scp_268_fine.png") : it\sound = 2 : it\IsHead = True
	it = CreateItemTemplate("Some SCP-420-J", "scp420j", "GFX\items\SCPs\scp_420.x", "GFX\items\Icons\Icon_scp_420.jpg", "", 0.0005) : it\sound = 2
	it = CreateItemTemplate("SCP-427","scp427","GFX\items\SCPs\scp_427.b3d","GFX\items\Icons\Icon_scp_427.jpg", "", 0.001)
	it = CreateItemTemplate("SCP-500", "scp500", "GFX\items\SCPs\scp_500_bottle.b3d","GFX\items\Icons\Icon_scp_500_bottle.jpg","",0.05) : it\sound = 2
	it = CreateItemTemplate("SCP-500-01", "scp500pill", "GFX\items\SCPs\scp_500_pill.b3d", "GFX\items\Icons\Icon_scp_500_pill.jpg", "", 0.0001) : it\sound = 2 : EntityColor it\obj,255,0,0
	it = CreateItemTemplate("SCP-714", "scp714", "GFX\items\SCPs\scp_714.b3d", "GFX\items\Icons\Icon_scp_714.jpg", "", 0.3) : it\sound = 3
	it = CreateItemTemplate("SCP-860", "scp860", "GFX\items\SCPs\scp_860.b3d", "GFX\items\Icons\Icon_scp_860.png", "", 0.001) : it\sound = 3
	it = CreateItemTemplate("SCP-1033-RU", "scp1033ru", "GFX\items\SCPs\scp_1033_ru.b3d", "GFX\items\Icons\Icon_scp_1033_ru.png", "", 0.5,"GFX\items\SCPs\scp_1033_ru.png") : it\sound = 3
    it = CreateItemTemplate("SCP-1033-RU", "super1033ru", "GFX\items\SCPs\scp_1033_ru.b3d", "GFX\items\Icons\Icon_scp_1033_ru (2).png", "", 0.5,"GFX\items\SCPs\scp_1033_ru_fine.png") : it\sound = 3
	
	;! ~ [USABLE ITEMS]
	
	; ~ (Key Cards)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "key_0"), "key0",  "GFX\items\Usable\key_card.x", "GFX\items\Icons\Icon_key_card_0.jpg", "", 0.0004,"GFX\items\Usable\key_card_0.png")
	it = CreateItemTemplate(GetLocalString("Item Names", "key_1"), "key1",  "GFX\items\Usable\key_card.x", "GFX\items\Icons\Icon_key_card_1.jpg", "", 0.0004,"GFX\items\Usable\key_card_1.png")
	it = CreateItemTemplate(GetLocalString("Item Names", "key_2"), "key2",  "GFX\items\Usable\key_card.x", "GFX\items\Icons\Icon_key_card_2.jpg", "", 0.0004,"GFX\items\Usable\key_card_2.png")
	it = CreateItemTemplate(GetLocalString("Item Names", "key_3"), "key3",  "GFX\items\Usable\key_card.x", "GFX\items\Icons\Icon_key_card_3.jpg", "", 0.0004,"GFX\items\Usable\key_card_3.png")
	it = CreateItemTemplate(GetLocalString("Item Names", "key_4"), "key4",  "GFX\items\Usable\key_card.x", "GFX\items\Icons\Icon_key_card_4.jpg", "", 0.0004,"GFX\items\Usable\key_card_4.png")
	it = CreateItemTemplate(GetLocalString("Item Names", "key_5"), "key5", "GFX\items\Usable\key_card.x", "GFX\items\Icons\Icon_key_card_5.jpg", "", 0.0004,"GFX\items\Usable\key_card_5.png")
	it = CreateItemTemplate(GetLocalString("Item Names", "key_omni"), "key6", "GFX\items\Usable\key_card.x", "GFX\items\Icons\Icon_key_card_omni.jpg", "", 0.0004,"GFX\items\Usable\key_card_omni.png")
	it = CreateItemTemplate(GetLocalString("Item Names", "key_cave"), "key_cave",  "GFX\items\Usable\key_card.x", "GFX\items\Icons\Icon_key_card_cave.jpg", "", 0.0004,"GFX\items\Usable\key_card_cave.png")
	it = CreateItemTemplate(GetLocalString("Item Names", "key_cave_2"), "key_cave2",  "GFX\items\Usable\key_card.x", "GFX\items\Icons\Icon_key_card_storage.jpg", "", 0.0004,"GFX\items\Usable\key_card_cave_2.png")
	it = CreateItemTemplate(GetLocalString("Item Names", "key_class_d"), "key_class_d",  "GFX\items\Usable\key_card.x", "GFX\items\Icons\Icon_key_card_class_d.jpg", "", 0.0004,"GFX\items\Usable\key_card_class_d.png")
	
	; ~ (Other Cards)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "playing_card"), "misc", "GFX\items\Usable\key_card.x", "GFX\items\Icons\Icon_playing_card.jpg", "", 0.0004,"GFX\items\Usable\playing_card.jpg")
	it = CreateItemTemplate(GetLocalString("Item Names", "mastercard"), "misc", "GFX\items\Usable\key_card.x", "GFX\items\Icons\Icon_master_card.jpg", "", 0.0004,"GFX\items\Usable\mastercard.jpg")
	
	; ~ (Severed Hands)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "severed_hand"), "hand", "GFX\items\Usable\severed_hand.b3d", "GFX\items\Icons\Icon_hand.jpg", "", 0.04) : it\sound = 2
	it = CreateItemTemplate(GetLocalString("Item Names", "severed_hand_black"), "hand2", "GFX\items\Usable\severed_hand.b3d", "GFX\items\Icons\Icon_hand (2).jpg", "", 0.04, "GFX\items\Usable\severed_hand (2).png") : it\sound = 2
	it = CreateItemTemplate(GetLocalString("Item Names", "severed_hand_yellow"), "hand3", "GFX\items\Usable\severed_hand.b3d", "GFX\items\Icons\Icon_hand (3).png", "", 0.04, "GFX\items\Usable\severed_hand (3).png") : it\sound = 2
	
	; ~ (Electronics)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "nav"), "nav", "GFX\items\Usable\navigator.x", "GFX\items\Icons\Icon_nav.jpg", "GFX\items\Usable\navigator.png", 0.0008)
	it = CreateItemTemplate(GetLocalString("Item Names", "nav_300"), "nav", "GFX\items\Usable\navigator.x", "GFX\items\Icons\Icon_nav.jpg", "GFX\items\Usable\navigator.png", 0.0008)
	it = CreateItemTemplate(GetLocalString("Item Names", "nav_310"), "nav", "GFX\items\Usable\navigator.x", "GFX\items\Icons\Icon_nav.jpg", "GFX\items\Usable\navigator.png", 0.0008)
	it = CreateItemTemplate(GetLocalString("Item Names", "nav_ultimate"), "nav", "GFX\items\Usable\navigator.x", "GFX\items\Icons\Icon_nav.jpg", "GFX\items\Usable\navigator.png", 0.0008)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "radio"), "radio", "GFX\items\Usable\radio.b3d", "GFX\items\Icons\Icon_radio.jpg", "GFX\items\Usable\Radio_HUD.png", 1.0)
	it = CreateItemTemplate(GetLocalString("Item Names", "radio"), "fineradio", "GFX\items\Usable\radio.b3d", "GFX\items\Icons\Icon_radio.jpg", "GFX\items\Usable\Radio_HUD.png", 1.0)
	it = CreateItemTemplate(GetLocalString("Item Names", "radio"), "veryfineradio", "GFX\items\Usable\radio.b3d", "GFX\items\Icons\Icon_radio.jpg", "GFX\items\Usable\Radio_HUD.png", 1.0)
	it = CreateItemTemplate(GetLocalString("Item Names", "radio"), "18vradio", "GFX\items\Usable\radio.b3d", "GFX\items\Icons\Icon_radio.jpg", "GFX\items\Usable\Radio_HUD.png", 1.02)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "fuse"), "fuse", "GFX\items\Usable\fuse.b3d", "GFX\items\Icons\Icon_fuse.jpg", "", 0.025)
	
	;! ~ [WEARIBLE ITEMS]
	
	it = CreateItemTemplate(GetLocalString("Item Names", "backpack"), "backpack", "GFX\items\Wearible\backpack.b3d", "GFX\items\Icons\Icon_Backpack.png", "", 0.02,"") : it\sound = 2 : it\IsBackpack = True
	
	it = CreateItemTemplate(GetLocalString("Item Names", "gas_mask"), "gasmask", "GFX\items\Wearible\gas_mask.b3d", "GFX\items\Icons\Icon_gas_mask.jpg", "", 0.02) : it\sound = 2 : it\IsHead = True
	it = CreateItemTemplate(GetLocalString("Item Names", "gas_mask"), "gasmask2", "GFX\items\Wearible\gas_mask.b3d", "GFX\items\Icons\Icon_gas_mask.jpg", "", 0.021) : it\sound = 2 : it\IsHead = True
	it = CreateItemTemplate(GetLocalString("Item Names", "gas_mask_heavy"), "gasmask3", "GFX\items\Wearible\gas_mask.b3d", "GFX\items\Icons\Icon_gas_mask.jpg", "", 0.021) : it\sound = 2 : it\IsHead = True
	
	it = CreateItemTemplate(GetLocalString("Item Names", "nvg"), "nvg", "GFX\items\Wearible\NVG.b3d", "GFX\items\Icons\Icon_nvg.jpg", "", 0.02, "GFX\items\Wearible\NVG_Green.png") : it\sound = 2 : it\IsHead = True
	it = CreateItemTemplate(GetLocalString("Item Names", "nvg"), "nvg2", "GFX\items\Wearible\NVG.b3d", "GFX\items\Icons\Icon_nvg (2).jpg", "", 0.02, "GFX\items\Wearible\NVG_Blue.png") : it\sound = 2 : it\IsHead = True
	it = CreateItemTemplate(GetLocalString("Item Names", "nvg"), "nvg3", "GFX\items\Wearible\NVG.b3d", "GFX\items\Icons\Icon_nvg (3).jpg", "", 0.02, "GFX\items\Wearible\NVG_Red.png") : it\sound = 2 : it\IsHead = True
	
	it = CreateItemTemplate(GetLocalString("Item Names", "vest"), "vest", "GFX\items\Wearible\vest.b3d", "GFX\items\Icons\Icon_vest.jpg", "", 0.02,"GFX\items\Wearible\Vest.png") : it\sound = 2 : it\IsTorso = True
	it = CreateItemTemplate(GetLocalString("Item Names", "vest_heavy"), "vestfine", "GFX\items\Wearible\vest.b3d", "GFX\items\Icons\Icon_vest (2).jpg", "", 0.022,"GFX\items\Wearible\Fine_Vest.png") : it\sound = 2 : it\IsTorso = True
	it = CreateItemTemplate(GetLocalString("Item Names", "vest_bulky"), "vestveryfine", "GFX\items\Wearible\vest.b3d", "GFX\items\Icons\Icon_vest.jpg", "", 0.025,"GFX\items\Wearible\Heavy_Vest.png") : it\sound = 2
	
	it = CreateItemTemplate(GetLocalString("Item Names", "hazmat"), "hazmat", "GFX\items\Wearible\hazmat.b3d", "GFX\items\Icons\Icon_hazmat.jpg", "", 0.013, "", "", 1) : it\sound = 2 : it\IsTorso = True : it\IsFullBody = True
	it = CreateItemTemplate(GetLocalString("Item Names", "hazmat"), "hazmat2", "GFX\items\Wearible\hazmat.b3d", "GFX\items\Icons\Icon_hazmat.jpg", "", 0.013, "", "", 1) : it\sound = 2 : it\IsTorso = True : it\IsFullBody = True
	it = CreateItemTemplate(GetLocalString("Item Names", "hazmat_heavy"), "hazmat3", "GFX\items\Wearible\hazmat.b3d", "GFX\items\Icons\Icon_hazmat.jpg", "", 0.013, "", "", 1) : it\sound = 2 : it\IsTorso = True : it\IsFullBody = True
	
	it = CreateItemTemplate(GetLocalString("Item Names", "ntf_helmet"), "ntf_helmet", "GFX\Items\Wearible\NTF_Helmet.b3d", "GFX\items\Icons\Icon_ntf_helmet.jpg", "", 0.0375/2.5,"gfx\items\Wearible\ntf_helmet_green.png") : it\sound = 2 : it\IsHead = True
	it = CreateItemTemplate(GetLocalString("Item Names", "fine_ntf_helmet"), "ntf_helmet2", "GFX\Items\Wearible\NTF_Helmet.b3d", "GFX\items\Icons\Icon_ntf_helmet (2).jpg", "", 0.0375/2.5,"gfx\items\Wearible\ntf_helmet_blue.png") : it\sound = 2 : it\IsHead = True
	
	it = CreateItemTemplate(GetLocalString("Item Names", "helmet"), "helmet", "GFX\Items\Wearible\Helmet.b3d", "GFX\items\Icons\Icon_helmet.png", "", 0.0375/2.5) : it\sound = 2 : it\IsHead = True
	it = CreateItemTemplate(GetLocalString("Item Names", "fine_helmet"), "helmet2", "GFX\Items\Wearible\Fine_Helmet.b3d", "GFX\items\Icons\Icon_helmet (2).png", "", 0.0375/2.5) : it\sound = 2 : it\IsHead = True
	
	it = CreateItemTemplate(GetLocalString("Item Names", "scramble"), "scramble", "GFX\items\Wearible\SCRAMBLE_gear.b3d", "GFX\items\Icons\Icon_SCRAMBLE.png", "", 0.02,"GFX\items\Wearible\SCRAMBLE_gear.png") : it\sound = 2 : it\IsHead = True
	it = CreateItemTemplate(GetLocalString("Item Names", "scramble"), "scramble2", "GFX\items\Wearible\SCRAMBLE_gear.b3d", "GFX\items\Icons\Icon_SCRAMBLE.png", "", 0.02,"GFX\items\Wearible\SCRAMBLE_gear.png") : it\sound = 2 : it\IsHead = True
	
	it = CreateItemTemplate(GetLocalString("Item Names", "hds"), "hds", "GFX\Items\Wearible\HDS_Suit.b3d", "GFX\items\Icons\Icon_hds.png", "", 0.0375/2.5) : it\sound = 5 : it\IsTorso = True : it\IsFullBody = True
	
	;! ~ [MISC ITEMS]
	
	; ~ (Coins)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "coin"), "coin", "GFX\items\Misc\coin.b3d", "GFX\items\Icons\Icon_coin_rusty.jpg", "", 0.0005, "GFX\items\Misc\coin_rusty.png") : it\sound = 3
	it = CreateItemTemplate(GetLocalString("Item Names", "coin_rusty"),"25ct", "GFX\items\Misc\coin.b3d", "GFX\items\Icons\Icon_coin.jpg", "", 0.0005, "GFX\items\Misc\coin.png") : it\sound = 3
	it = CreateItemTemplate(GetLocalString("Item Names", "v_coin"), "vanecoin", "GFX\items\Misc\coin.b3d", "GFX\items\Icons\Icon_coin_vane.jpg", "", 0.0005, "GFX\items\Misc\coin_vane.png","",0,1+2+8) : it\sound = 3
	
	; ~ (Unliftable)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "broken_helmet"), "brokenhelmet", "GFX\items\Misc\helmet_broken.b3d", "GFX\items\Icons\Icon_misc.jpg", "", 0.0375/2.5)
	
	; ~ (Temporary)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "toolbox"), "toolbox", "GFX\Map\Props\toolbox.x", "GFX\Items\Icons\Icon_toolbox.png","",0.005) : it\sound = 2
	
	; ~ (Other)
	
	it = CreateItemTemplate(GetLocalString("Item Names", "red_star"), "star", "GFX\items\Misc\coin.b3d", "GFX\items\Icons\Icon_soviet_star.png", "", 0.0005, "GFX\items\Misc\soviet_star.png","",0,1+2+8) : it\sound = 3
	
	;! ~ [END]
	
	For it = Each ItemTemplates
		If (it\tex<>0) Then
			If (it\texpath<>"") Then
				For it2=Each ItemTemplates
					If (it2<>it) And (it2\tex=it\tex) Then
						it2\tex = 0
					EndIf
				Next
			EndIf
			DeleteSingleTextureEntryFromCache it\tex : it\tex = 0
		EndIf
	Next
	
End Function 

Type Items
	Field name$
	Field collider%,model%
	Field itemtemplate.ItemTemplates
	Field DropSpeed#
	Field r%,g%,b%,a#
	Field SoundChn%
	Field dist#, disttimer#
	Field state#, state2#
	Field Picked%,Dropped%
	Field invimg%
	Field WontColl% = False
	Field xspeed#,zspeed#
	Field SecondInv.Items[20]
	Field ID%
	Field invSlots%
	; ~ Multiplayer
	Field noDelete%
	Field OverHereSprite%
End Type 

Function CreateItem.Items(name$, tempname$, x#, y#, z#, r%=0,g%=0,b%=0,a#=1.0,invSlots%=0)
	CatchErrors("Uncaught (CreateItem)")
	
	Local i.Items = New Items
	Local it.ItemTemplates
	
	name = Lower(name)
	tempname = Lower (tempname)
	
	For it.ItemTemplates = Each ItemTemplates
		If Lower(it\name) = name Then
			If Lower(it\tempname) = tempname Then
				i\itemtemplate = it
				i\collider = CreatePivot()
				EntityRadius i\collider, 0.01
				EntityPickMode i\collider, 1, False
				i\model = CopyEntity(it\obj,i\collider)
				i\name = it\name
				ShowEntity i\collider
				ShowEntity i\model
			EndIf
		EndIf
	Next 
	
	i\WontColl = False
	
	If i\itemtemplate = Null Then RuntimeError("Item template not found ("+name+", "+tempname+")")
	
	ResetEntity i\collider		
	PositionEntity(i\collider, x, y, z, True)
	RotateEntity (i\collider, 0, Rand(360), 0)
	If gopt\GameMode <> GAMEMODE_MULTIPLAYER Then
		i\dist = EntityDistance(Collider, i\collider)
	Else
		i\dist = EntityDistance(Players[mp_I\PlayerID]\Collider, i\collider)
	EndIf
	i\DropSpeed = 0.0
	
	If tempname = "cup" Then
		i\r=r
		i\g=g
		i\b=b
		i\a=a
		
		Local liquid = CopyEntity(LiquidObj)
		ScaleEntity liquid, i\itemtemplate\scale,i\itemtemplate\scale,i\itemtemplate\scale,True
		PositionEntity liquid, EntityX(i\collider,True),EntityY(i\collider,True),EntityZ(i\collider,True)
		EntityParent liquid, i\model
		EntityColor liquid, r,g,b
		
		If a < 0 Then 
			EntityFX liquid, 1
		EndIf
		EntityAlpha liquid, Abs(a)
		EntityShininess liquid, 1.0
	EndIf
	
	i\invimg = i\itemtemplate\invimg
	If (tempname="clipboard") And (invSlots=0) Then
		invSlots = 10
		SetAnimTime i\model,17.0
		i\invimg = i\itemtemplate\invimg2
	EndIf
	If (tempname="wallet") And (invSlots=0) Then
		invSlots = 10
	EndIf
	If tempname="hazmat" Lor tempname="hazmat2" Lor tempname="hazmat3" Then
		RotateEntity i\model,0,180,0
		SetAnimTime i\model,1.0
	EndIf
	
	i\invSlots=invSlots
	
	i\ID=LastItemID+1
	LastItemID=i\ID
	
	CatchErrors("CreateItem")
	Return i
End Function

Function RemoveItem(i.Items)
	CatchErrors("Uncaught (RemoveItem)")
	Local n
	i\model = FreeEntity_Strict(i\model)
	i\collider = FreeEntity_Strict(i\collider)
	
	For n% = 0 To MaxItemAmount - 1
		If Inventory[n] = i
			;debuglog "Removed "+i\itemtemplate\name+" from slot "+n
			Inventory[n] = Null
			ItemAmount = ItemAmount-1
			Exit
		EndIf
	Next
	If SelectedItem = i Then
		Select SelectedItem\itemtemplate\tempname
			Case "scp714"
				I_714\Using = 0
			Case "scp268","super268"
				I_268\Using = False
			Case "scp1033ru", "super1033ru"
				I_1033RU\Using = False
;			Case "scp059"
;				I_059\Timer = 0.9
;				I_059\Using = False
;			Case "scp357"
;				I_357\Timer = 0.0
;				I_357\Using = False
;			Case "scp402"
;				I_402\Using = False
		End Select
		
		SelectedItem = Null
	EndIf
	If i\itemtemplate\img <> 0
		FreeImage i\itemtemplate\img
		i\itemtemplate\img = 0
	EndIf
	i\OverHereSprite = FreeEntity_Strict(i\OverHereSprite)
	Delete i
	
	CatchErrors("RemoveItem")
End Function

Function UpdateItems()
	CatchErrors("Uncaught (UpdateItems)")
	Local n, i.Items, i2.Items
	Local xtemp#, ytemp#, ztemp#
	Local temp%, np.NPCs
	Local pick%
	
	Local HideDist = HideDistance*0.5
	Local deletedItem% = False
	
	ClosestItem = Null
	For i.Items = Each Items
		i\Dropped = 0
		
		If (Not i\Picked) Then
			If i\disttimer < MilliSecs() Then
				i\dist = EntityDistance(Camera, i\collider)
				i\disttimer = MilliSecs() + 700
				If i\dist < HideDist Then ShowEntity i\collider
			EndIf
			
			If i\dist < HideDist Then
				ShowEntity i\collider
				
				If i\dist < 1.2 Then
					If ClosestItem = Null Then
						If EntityInView(i\model, Camera) Then
							If EntityVisible(i\collider,Camera) Then
								ClosestItem = i
							EndIf
						EndIf
					ElseIf ClosestItem = i Lor i\dist < EntityDistance(Camera, ClosestItem\collider) Then 
						If EntityInView(i\model, Camera) Then
							If EntityVisible(i\collider,Camera) Then
								ClosestItem = i
							EndIf
						EndIf
					EndIf
				EndIf
				
				If EntityCollided(i\collider, HIT_MAP) Then
					i\DropSpeed = 0
					i\xspeed = 0.0
					i\zspeed = 0.0
				Else
					If ShouldEntitiesFall
						pick = LinePick(EntityX(i\collider),EntityY(i\collider),EntityZ(i\collider),0,-10,0)
						If pick
							i\DropSpeed = i\DropSpeed - 0.0004 * FPSfactor
							TranslateEntity i\collider, i\xspeed*FPSfactor, i\DropSpeed * FPSfactor, i\zspeed*FPSfactor
							If i\WontColl Then ResetEntity(i\collider)
						Else
							i\DropSpeed = 0
							i\xspeed = 0.0
							i\zspeed = 0.0
						EndIf
					Else
						i\DropSpeed = 0
						i\xspeed = 0.0
						i\zspeed = 0.0
					EndIf
				EndIf
				
				If i\dist<HideDist*0.2 Then
					For i2.Items = Each Items
						If i<>i2 And (Not i2\Picked) And i2\dist<HideDist*0.2 Then
							
							xtemp# = (EntityX(i2\collider,True)-EntityX(i\collider,True))
							ytemp# = (EntityY(i2\collider,True)-EntityY(i\collider,True))
							ztemp# = (EntityZ(i2\collider,True)-EntityZ(i\collider,True))
							
							Local ed# = (xtemp*xtemp+ztemp*ztemp)
							If ed<0.07 And Abs(ytemp)<0.25 Then
								;items are too close together, push away
								If PlayerRoom\RoomTemplate\Name	<> "cont_970" Then
									xtemp = xtemp*(0.07-ed)
									ztemp = ztemp*(0.07-ed)
									
									While Abs(xtemp)+Abs(ztemp)<0.001
										xtemp = xtemp+Rnd(-0.002,0.002)
										ztemp = ztemp+Rnd(-0.002,0.002)
									Wend
									
									TranslateEntity i2\collider,xtemp,0,ztemp
									TranslateEntity i\collider,-xtemp,0,-ztemp
								EndIf
							EndIf
						EndIf
					Next
				EndIf
				
				If EntityY(i\collider) < - 35.0 Then RemoveItem(i) : deletedItem = True ; : DebugLog "remove: " + i\itemtemplate\name
			Else
				HideEntity i\collider
			EndIf
		Else
			i\DropSpeed = 0
			i\xspeed = 0.0
			i\zspeed = 0.0
		EndIf
		
		If Not deletedItem Then
			CatchErrors(Chr(34)+i\itemtemplate\name+Chr(34)+" item")
		EndIf
		deletedItem = False
	Next
	
	If ClosestItem <> Null Then
		If KeyHitUse Then PickItem(ClosestItem)
	EndIf
	
End Function

Function PickItem(item.Items)
	Local n% = 0, z%
	Local g.Guns, e.Events
	CatchErrors("Uncaught (PickItem)")
	
	If (Not item\itemtemplate\IsGun) And (Not item\itemtemplate\fastPickable)
		If ItemAmount < MaxItemAmount Then
			For n% = 0 To MaxItemAmount - 1
				If n < SLOT_HEAD Lor n > SLOT_SCABBARD
					If Inventory[n] = Null Then
						Select item\itemtemplate\name
							Case "Class-D Zone Note"
								If gopt\GameMode = GAMEMODE_DEFAULT Then
									If cpt\Current < 2 Then
										If (Not TaskExists(TASK_READ_NOTE)) Then
											BeginTask(TASK_READ_NOTE)
										EndIf
									EndIf
								EndIf
							Case "Surveillance Room Password Note"
								If TaskExists(TASK_FIND_NOTE) Then
									EndTask(TASK_FIND_NOTE)
								EndIf
								If ecst\UnlockedAirlock Then
									If TaskExists(TASK_FIND_ROOM2_SL) Then
										CancelTask(TASK_FIND_ROOM2_SL)
									EndIf
								Else
									If (Not TaskExists(TASK_FIND_ROOM2_SL)) Then
										BeginTask(TASK_FIND_ROOM2_SL)
									EndIf
								EndIf
						End Select
						Select item\itemtemplate\tempname
							Case "key_class_d"
								If gopt\GameMode = GAMEMODE_DEFAULT And gopt\CurrZone <> CLASSD_CELLS Then
									If TaskExists(TASK_FIND_CLASS_D_KEY) Then
										EndTask(TASK_FIND_CLASS_D_KEY)
									EndIf
									If (Not TaskExists(TASK_FIND_CLASS_D_ZONE)) Then
										BeginTask(TASK_FIND_CLASS_D_ZONE)
									EndIf
								EndIf
							Case "scp109","scp178"
								SetAnimTime item\model, 19.0
							Case "hazmat", "hazmat2", "hazmat3"
								SetAnimTime item\model,20.0
							Case "killbat"
								LightFlash = 1.0
								PlaySound_Strict(IntroSFX[2])
								If I_1033RU\HP = 0
									m_msg\DeathTxt = GetLocalStringR("Singleplayer","killbad_death_1",Designation)
									m_msg\DeathTxt = m_msg\DeathTxt + GetLocalString("Singleplayer","killbad_death_2")
									Kill()
								Else
									Damage1033RU(100 * I_1033RU\Using)
								EndIf
							Case "scp148"
								GiveAchievement(Achv148)	
							Case "scp513"
								GiveAchievement(Achv513)
							Case "scp860"
								GiveAchievement(Achv860)
							Case "scp207"
	;							If I_402\Timer > 0 Then
	;								PlaySound_Strict(HorrorSFX[Rand(0, 3)])
	;								CreateMsg(Chr(34) + GetLocalString("Singleplayer","i_cant") + Chr(34))
	;								Exit
	;							Else
									GiveAchievement(Achv207)
	;							EndIf
							Case "scp207empty"
								CreateMsg(GetLocalString("Items", "scp207_empty"))
								Exit
	;						Case "scp357"
	;							;[Block]
	;							If (Not I_059\Using) Then
	;								I_357\Using = True
	;								GiveAchievement(Achv357)
	;								CreateMsg(GetLocalString("Items", "scp357_2"))
	;								I_357\Timer = 1.0
	;							Else
	;								CreateMsg(GetLocalString("Items", "scp357_3"))
	;								Exit
	;							EndIf
	;							;[End Block]
	;						Case "scp059"
	;							If (Not I_357\Using) Then
	;								I_059\Using = True
	;								GiveAchievement(Achv059)
	;								CreateMsg(GetLocalString("Items", "scp059_1"))
	;								I_059\Timer = 10.0
	;							Else
	;								CreateMsg(GetLocalString("Items", "scp059_2"))
	;								Exit
	;							EndIf
							Case "scp198"
								;[Block]
								GiveAchievement(Achv198)
								I_198\Timer = 0
								CreateMsg(GetLocalString("Items", "scp198_schock"))
								PlaySound_Strict LoadTempSound("SFX\SCP\198\Shock.ogg")
								LightFlash = 2.5
								BlurTimer = 1000
								Sanity = Max(-850, Sanity)
								I_198\Timer = 1
								If I_1033RU\HP = 0
									DamageSPPlayer(Rand(10,15),True)
								Else
									Damage1033RU(30 + (5 * SelectedDifficulty\AggressiveNPCs))
								EndIf
								;[End Block]
							Case "scp035"
								;[Block]
								
								Local snd.Sound,i
								
								If (Not I_714\Using Lor mpl\HasNTFGasmask Lor (Inventory[SLOT_HEAD] <> Null And Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 3) = "nvg" Lor Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 7) = "gasmask" Lor Left(Inventory[SLOT_HEAD]\itemtemplate\tempname, 6) = "helmet")) Then
									
									RemoveItem(item)
									
									For snd = Each Sound
										For i = 0 To MaxChannelsAmount - 1
											If snd\channels[i]<>0 Then
												StopChannel snd\channels[i]
											EndIf
										Next
									Next
									
									HolsterGun()
									
									I_035\Possessed = True
									
									InvOpen = False
									GiveAchievement(Achv035)
									PlaySound_Strict LoadTempSound("SFX\SCP\035\Shock.ogg")
									LightFlash = 2
									BlurTimer = 100
									If KillTimer >= 0 And FallTimer >= 0 Then
										FallTimer = Min(-1, FallTimer)
										PositionEntity(Head, EntityX(Camera, True), EntityY(Camera, True), EntityZ(Camera, True), True)
										ResetEntity (Head)
										RotateEntity(Head, 0, EntityYaw(Camera) + Rand(-45, 45), 0)
										mpl\DamageTimer = 70.0*1.0
										If Inventory[SLOT_TORSO] <> Null Then
											RemoveItem(Inventory[SLOT_TORSO])
										EndIf
										
										psp\Health = 1
									EndIf
									If gopt\CurrZone = LCZ Lor gopt\CurrZone = HCZ Lor gopt\CurrZone = EZ Then
										Local r.Rooms
										For r = Each Rooms
											If r\RoomTemplate\Name = "area_035_ntf_encounter" Then
												PlayerRoom = r
												TeleportEntity(Collider, r\x,r\y,r\z)
												Exit
											EndIf
										Next
										DropSpeed = 0
										FallTimer = 0
										psp\NoMove = False
										psp\NoRotation = False
										IsZombie = False
										FlushKeys()
										FlushMouse()
										FlushJoy()
										Return
									Else
										Kill()
									EndIf
								Else
									Exit
								EndIf
								;[End Block]
							Case "scp1102ru"
								;[Block]
								item\state = 0
								;[End Block]
							Case "key6"
								GiveAchievement(AchvOmni)
							Case "vest3"
								CreateMsg(GetLocalString("Items","vest_heavy"))
								Exit
							Case "firstaid", "finefirstaid", "veryfinefirstaid", "firstaid2"
								item\state = 0
	;							If I_402\Timer > 0 Then
	;								PlaySound_Strict(HorrorSFX[Rand(0, 3)])
	;								CreateMsg(Chr(34) + GetLocalString("Singleplayer","i_cant") + Chr(34))
	;								Exit
	;							EndIf
							Case "navigator", "nav"
								If item\itemtemplate\name = "S-NAV Navigator Ultimate" Then GiveAchievement(AchvSNAV)
							Case "paper"
								If item\itemtemplate\name = "I_008\Timered SCP-409 Document" Then
									CreateMsg(GetLocalString("Items","scp409_9"))
									I_409\Timer = 1
								EndIf
							Case "brokenhelmet"
								CreateMsg(GetLocalString("Items","brokenhelmet"))
								Exit
						End Select
						
						CreateMsg(item\itemtemplate\name+" "+GetLocalString("Items","item_picked_up"))
						
						If item\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[item\itemtemplate\sound])
						item\Picked = True
						item\Dropped = -1
						
						item\itemtemplate\found=True
						ItemAmount = ItemAmount + 1
						
						Inventory[n] = item
						HideEntity(item\collider)
						Exit
					EndIf
				EndIf
			Next
		Else
			CreateMsg(GetLocalString("Items","cannot_carry"))
		EndIf
	ElseIf (item\itemtemplate\IsGun) And (Not item\itemtemplate\fastPickable)
		Local found = False
		
		For n% = 0 To MaxItemAmount - 1
			If Inventory[n] <> Null And Inventory[n]\itemtemplate\tempname = item\itemtemplate\tempname Then
				found = True
				Exit
			EndIf
		Next
		
		Local att%
		
		For g = Each Guns
			If g\name = item\itemtemplate\tempname Then
				If (g\GunType = GUNTYPE_MELEE And g\name <> "m67" And g\name <> "rgd5") And found Then
					CreateMsg(GetLocalString("Weapons","already_have") + item\itemtemplate\name)
					Exit
				EndIf
				If (Not found) Lor g\name = "m67" Lor g\name = "rgd5" Then
					If ItemAmount < MaxItemAmount Then
						For n = 0 To MaxItemAmount - 1
							If n < SLOT_HEAD Lor n > SLOT_SCABBARD
								If Inventory[n] = Null Then
									PlaySound_Strict(LoadTempSound("SFX\Guns\" + item\itemtemplate\tempname + "\pickup.ogg"))
									item\Picked = True
									item\Dropped = -1
									item\itemtemplate\found = True
									Inventory[n] = item
									HideEntity item\collider
									g\CurrAmmo = item\state
									g\CurrReloadAmmo = item\state2
									For att = 0 To MaxAttachments - 1
										If g\HasAttachments[att] Then g\HasPickedAttachments[att] = True
									Next
									Exit
								EndIf
							EndIf
						Next
					Else
						CreateMsg(GetLocalString("Items","cannot_carry"))
					EndIf
				Else
					If g\CurrReloadAmmo < g\MaxReloadAmmo Then
						If g\GunType = GUNTYPE_SHOTGUN Then
							Local prev = g\CurrReloadAmmo
							g\CurrReloadAmmo = Min(g\CurrReloadAmmo+item\state+item\state2,g\MaxReloadAmmo)
							If item\state <> 0 Then
								CreateMsg((g\CurrReloadAmmo - prev) + " " + item\itemtemplate\name + GetLocalString("Weapons","shells_picked_up"))
							Else
								CreateMsg(GetLocalString("Weapons","no_ammo_1")+item\itemtemplate\name + GetLocalString("Weapons","no_ammo_2"))
							EndIf
							m_msg\Timer = 70 * 5
						Else
							g\CurrReloadAmmo = Min(g\CurrReloadAmmo+item\state+item\state2,g\MaxReloadAmmo)
							If item\state <> 0 Then
								CreateMsg(GetLocalString("Weapons","ammo_picked_up_1")+ " " + item\itemtemplate\name + " " + GetLocalString("Weapons","ammo_picked_up_2"))
							Else
								CreateMsg(GetLocalString("Weapons","no_ammo_1")+ " " +item\itemtemplate\name + " " + GetLocalString("Weapons","no_ammo_2"))
							EndIf
							m_msg\Timer = 70 * 5
						EndIf
						PlaySound_Strict LoadTempSound("SFX\Guns\" + item\itemtemplate\tempname + "\pickup.ogg")
						RemoveItem(item)
					Else
						CreateMsg(GetLocalString("Weapons","cant_have_ammo") + item\itemtemplate\name)
					EndIf
				EndIf
				Exit
			EndIf
		Next
		If TaskExists(TASK_NTF_FIND_WEAPON) Then
			EndTask(TASK_NTF_FIND_WEAPON)
		EndIf
	Else
		Select item\itemtemplate\tempname
			Case "suppressor","reddot","eotech","acog","match","mui","extmag"
				
				;CreateMsg(GetLocalString("Weapons","picked_suppressor"))
				PlaySound_Strict(PickSFX[item\itemtemplate\sound])
				
				Local attint%
				
				If item\itemtemplate\tempname = "suppressor" Then
					attint = ATT_SUPPRESSOR
				ElseIf item\itemtemplate\tempname = "reddot" Then
					attint = ATT_RED_DOT
				ElseIf item\itemtemplate\tempname = "eotech" Then
					attint = ATT_EOTECH
				ElseIf item\itemtemplate\tempname = "acog" Then
					attint = ATT_ACOG_SCOPE
				ElseIf item\itemtemplate\tempname = "match" Then
					attint = ATT_MATCH
				ElseIf item\itemtemplate\tempname = "mui" Then
					attint = ATT_MUI
				ElseIf item\itemtemplate\tempname = "extmag" Then
					attint = ATT_EXT_MAG
				EndIf
				
				RemoveItem(item)
				
				For g = Each Guns
					g\HasPickedAttachments[attint] = True
				Next
			Case "hds_fuse"
				If Inventory[SLOT_TORSO] <> Null And Inventory[SLOT_TORSO]\itemtemplate\tempname = "hds" Then
					If hds\Health < 100 Then
						PlaySound_Strict(PickSFX[item\itemtemplate\sound])
						hds\Health = Min(hds\Health+Rand(10,25),100)
						RemoveItem(item)
					EndIf
				EndIf
		End Select
	EndIf
	CatchErrors("PickItem")
End Function

Function DropItem(item.Items)
	CatchErrors("Uncaught (DropItem)")
	Local g.Guns, z%
	
	If item\itemtemplate\sound <> 66 Then PlaySound_Strict(PickSFX[item\itemtemplate\sound])
	
	If item\itemtemplate\IsGun Then
		For g.Guns = Each Guns
			If g\name$ = item\itemtemplate\tempname
				item\state = g\CurrAmmo
				item\state2 = g\CurrReloadAmmo
				HolsterGun()
				If g\ID% <> g_I\HoldingGun Then
					If g\IsSeparate Then
						HideEntity g\HandsObj
					EndIf
					PlayGunSound(g\name$+"\holster",1,1,False)
					If g\JamAmount >= g\MaxJams Then
						g\JamTimer = 0
					EndIf
					Exit
				EndIf
			EndIf
		Next
	EndIf
	
	item\Dropped = 1
	
	ShowEntity(item\collider)
	PositionEntity(item\collider, EntityX(Camera), EntityY(Camera), EntityZ(Camera))
	RotateEntity(item\collider, EntityPitch(Camera), EntityYaw(Camera)+Rnd(-20,20), 0)
	MoveEntity(item\collider, 0, -0.1, 0.1)
	RotateEntity(item\collider, 0, EntityYaw(Camera)+Rnd(-110,110), 0)
	
	ResetEntity (item\collider)
	item\Picked = False
	For z% = 0 To MaxItemAmount - 1
		If Inventory[z] = item Then Inventory[z] = Null
	Next
	Select item\itemtemplate\tempname
		Case "scp714"
			I_714\Using = 0
		Case "scp268","super268"
			I_268\Using = False
		Case "scp1033ru", "super1033ru"
			I_1033RU\Using = False
;		Case "scp059"
;			I_059\Timer = 5.0
;			I_059\Using = False
;		Case "scp357"
;			I_357\Timer = 0.0
;			I_357\Using = False
;		Case "scp402"
;			I_402\Using = False
	End Select
	
	CreateMsg(item\itemtemplate\name+" "+GetLocalString("Items","item_dropped"))
	
	CatchErrors("DropItem")
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D TSS