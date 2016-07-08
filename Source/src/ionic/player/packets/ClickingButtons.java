package ionic.player.packets;

import java.io.File;

import ionic.clans.ClanHandler;
import ionic.grandExchange.GEButtons;
import ionic.item.GameItem;
import ionic.item.ItemAssistant;
import ionic.item.ItemDegrade;
import ionic.npc.pet.BossPet;
import ionic.npc.pet.Pet;
import ionic.player.Client;
import ionic.player.PlayerHandler;
import ionic.player.achievements.AchievementHandler;
import ionic.player.achievements.ViewAchievement;
import ionic.player.banking.BankHandler;
import ionic.player.content.gambling.GamblingHandler;
import ionic.player.content.miscellaneous.MonsterKillLog;
import ionic.player.content.miscellaneous.NpcInfoViewer;
import ionic.player.content.miscellaneous.Preset;
import ionic.player.content.miscellaneous.PriceChecker;
import ionic.player.content.miscellaneous.SkillGuides;
import ionic.player.content.miscellaneous.Tele;
import ionic.player.content.miscellaneous.WealthEvaluator;
import ionic.player.content.partyroom.PartyRoom;
import ionic.player.content.prayer.QuickCurses;
import ionic.player.content.prayer.QuickPrayers;
import ionic.player.content.quest.QuestHandler.QuestList;
import ionic.player.content.skills.cooking.Cooking;
import ionic.player.content.skills.crafting.LeatherMaking;
import ionic.player.content.skills.crafting.Tanning;
import ionic.player.content.skills.crafting.CraftingData.tanningData;
import ionic.player.content.skills.fletching.FletchingButtons;
import ionic.player.content.skills.herblore.FinishingPotions;
import ionic.player.content.skills.herblore.UnfinishedPotions;
import ionic.player.content.skills.smithing.Smithing;
import ionic.player.content.skills.summoning.SummoningButtons;
import ionic.player.content.skills.summoning.SummoningData;
import ionic.player.dialogue.Dialogues;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import ionic.player.interfaces.ItemsKeptOnDeath;
import ionic.player.movement.Movement;
import ionic.player.profiles.Profile;
import ionic.player.profiles.ProfileSend;
import ionic.shop.ShopController;
import utility.Misc;
import core.Configuration;
import core.Constants;
import core.Server;

public class ClickingButtons implements PacketType
{

	@
	Override
	public void processPacket(final Client player, int packetType, int packetSize) {
		int buttonID = Misc.hexToInt(player.getInStream().buffer, 0, packetSize);

		if (player.playerName.equalsIgnoreCase("keith") || player.playerName.equalsIgnoreCase("keith2")) { player.sendMessage("Clicked actionbutton : "+buttonID+""); }
		
		if (GamblingHandler.clickButton(player, buttonID)) {
			return;
		}
		if (player.gamble != null) {
			return;
		}
		
		if (Smithing.smithingButtons(player, buttonID)) { return; }
		
		player.getPA().switchCombatType(buttonID);
		int[] curseButtonId = {  95183, 95815, 95817, 95819, 95191, 95193, 95195, 95197, 95199, 95201, 95203, 95205,
				95207, 95209, 95211, 95213, 95215, 95217, 95219, 95221
		};

		if (player.doingAction(false) || player.getDoingAgility() || player.isDead || player.isTeleporting()) {
			boolean state = false;
			for (int i = 0; i < curseButtonId.length; i++) {
				if (curseButtonId[i] == buttonID) {
					state = true;
				}
			}
			if (!state) {
				return;
			}
		}

		if (player.inFletchInterface == true) {
			for (int i = 0; i < FletchingButtons.buttons.length; i++) {
				for (int k = 0; k < FletchingButtons.buttons[i].length; k++) {
					if (buttonID == FletchingButtons.buttons[i][k]) {
						new FletchingButtons(player, i, k);
						return;
					}
				}
			}
		}
		
		for (tanningData t : tanningData.values()) {
			if (buttonID == t.getButtonId(buttonID)) {
				Tanning.tanHide(player, buttonID);
			}
		}
		LeatherMaking.craftLeather(player, buttonID);
		GEButtons.click(player, buttonID);
		player.curses().curseButtons(buttonID);
		player.getPlayList().handleButton(buttonID);
		
		if (SummoningButtons.click(player, buttonID)) {
			return;
		}
		if (BossPet.clickButton(player, buttonID)) {
			return;
		}
		
		for (QuestList q : QuestList.values()) {
			if (q.getButton() == buttonID) {
				q.getQuest().clickQuestListButton(player);
				return;
			}
		}

		int[] spellIds = {
				4128, 4130, 4132, 4134, 4136, 4139, 4142, 4145, 4148, 4151, 4153, 4157, 4159, 4161, 4164, 4165, 4129, 
				4133, 4137, 6006, 6007, 6026, 6036, 6046, 6056, 4147, 6003, 47005, 4166, 4167, 4168, 48157, 50193, 50187, 
				50101, 50061, 50163, 50211, 50119, 50081, 50151, 50199, 50111, 50071, 50175, 50223, 50129, 50091
		};

		for (int i = 0; i < spellIds.length; i++)  {
			if (buttonID == spellIds[i])  {
				if (player.playerMagicBook == 2) {
					player.autocastId = -1;
					player.getPA().resetAutocast();
					return;
				}
				player.autocasting = (player.autocastId != i) ? true : false;
				if (!player.autocasting) {
					player.getPA().resetAutocast();
				} else {
					player.getPA().sendFrame36(43, -1);
					player.autocastId = i;
					player.getCombat().resetPlayerAttack();
				}
			}
		}
		
		if (ViewAchievement.clickButton(player, buttonID)) {
			return;
		}
		
		if (NpcInfoViewer.clickButton(player, buttonID)) {
			return;
		}

		if (buttonID >= 67050 && buttonID <= 67075 || buttonID >= 67100 && buttonID <= 67119) {
			if (player.Prayerbook == 0) {
				QuickPrayers.clickPray(player, buttonID);
			} else {
				QuickCurses.clickCurse(player, buttonID);
			}
		}
		
		BankHandler.clickBankButtons(player, buttonID);

		switch (buttonID) {
		
		case 121056:
			Preset.upload(player);
			break;
		case 121057:
			Preset.gearUp(player);
			break;
		case 177135:
			Preset.open(player);
			break;
			
		case 139163:
			if (player.getPetSummoned()) {
				player.sendMessage("You already have a pet following you.");
				return;
	        }
			if (player.familiar != null) {
				player.sendMessage("You can't have a familiar and a pet at the same time");
				return;
			}
			if (player.petKillLogs[player.bosspetSelected.getSlot()] >= player.bosspetSelected.killReq)
				Pet.summonPet(player, player.bosspetSelected.getId(), player.absX, player.absY - 1, player.heightLevel);
			else
				player.sendMessage("You have not yet unlocked this pet.");
			break;
		
		
		case 139137://create
			if (player.profile == null) {
				Profile.newProfile(player);
				player.sendMessage("You have created your profile.");
				ProfileSend.send(player.profile, player);
			} else {
				player.sendMessage("You already have a profile.");
			}
			break;
		case 139138://update
			if (player.profile != null) {
				player.profile.update(player);
				player.sendMessage("Your profile has been updated & saved.");
			} else {
				player.sendMessage("You don't have a profile.");
			}
			break;
		case 139139://delete
			String name = "";
			if (player.profile != null) {
				name = player.profile.name;
				for (int i = 0; i < Server.profiles.length; i++) {
					if (Server.profiles[i] == player.profile) {
						Server.profiles[i] = null;
					}
				}
				File file = new File("./data/characters/profiles/"+name+".txt");
				if (file != null && file.exists()) {
					file.delete();
				}
				player.profile = null;
				player.sendMessage("You have successfully deleted your profile.");
			} else {
				player.sendMessage("You don't have a profile.");
			}
			break;
		case 139140://view my
			if (player.profile != null) {
				ProfileSend.send(player.profile, player);
			} else {
				player.sendMessage("You don't have a profile.");
			}
			break;
		
		
		
		case 65119:
			player.getPA().showInterface(35000);
			break;
		case 65120:
			MonsterKillLog.openLog(player);
			break;
		case 65122:
			BossPet.openInterface(player);
			break;
		
		case 14067:
			AchievementHandler.add(player, 23, "easy", 1);
		break;
		case 155026:
		case 151046:
			SummoningData.openScrollCreation(player);
			break;
		case 151045:
		case 155025:
			SummoningData.openPouchCreation(player);
			break;
		
		case 145195:
			WealthEvaluator.open(player);
			break;
		case 145212:
			WealthEvaluator.buyMore(player);
			break;
		case 145205:
			WealthEvaluator.announce(player);
			break;
		
		case 145187:
			ItemDegrade.repair(player);
			break;
		
		case 6004:
		case 51013:
			Dialogues.send(player, Dialogues.SKILLING_TELEPORTS);
			break;
		case 6005:
		case 51023:
			Dialogues.send(player, Dialogues.MINIGAME_TELEPORTS);
			break;
		case 4140:
		case 50235:
			Dialogues.send(player, Dialogues.PVP_TELEPORTS);
			break;
		case 4143:
		case 50245:
			Dialogues.send(player, Dialogues.CITY_TELEPORTS);
			break;
		case 4146:
		case 50253:
			Dialogues.send(player, Dialogues.MONSTER_TELEPORTS);
			break;
		case 4150:
		case 51005:
			Dialogues.send(player, Dialogues.BOSS_TELEPORTS);
			break;
		case 75010:
		case 84237:
			player.getPA().tele("spell", Tele.HOME);
			break;
			
		case 5019:
			PriceChecker.open(player);
			break;

		case 10239:
			if (player.herbloreInterface) {
				if (player.isUnfinished) {
					UnfinishedPotions.selectAmount(player, 1);
				} else {
					FinishingPotions.mix(player, 1);
				}
			}
			break;
		case 10238:
			if (player.herbloreInterface) {
				if (player.isUnfinished) {
					UnfinishedPotions.selectAmount(player, 5);
				} else {
					FinishingPotions.mix(player, 5);
				}
			}
			break;
		case 6212:
			if (player.herbloreInterface) {
				if (player.isUnfinished) {
					UnfinishedPotions.selectAmount(player, 10);
				} else {
					FinishingPotions.mix(player, 10);
				}
			}
			break;
		case 6211:
			if (player.herbloreInterface) {
				if (player.isUnfinished) {
					UnfinishedPotions.selectAmount(player, 28);
				} else {
					FinishingPotions.mix(player, 28);
				}
			}
			break;
		
		case 144243:
			for (int i = 0; i < player.playerItems.length; i++) {
				PriceChecker.add(player, player.playerItems[i] - 1, i, player.playerItemsN[i]);
			}
			break;
		
		case 73048:
		case 73033:
			player.getPA().closeAllWindows();
			break;
		
		case 107016:
			PartyRoom.confirm(player);
			break;

		case 70209:
			if (player.clan == null) {
				player.getOutStream().createFrame(187);
			} else {
				ClanHandler.leaveClan(player, "You have left the clan chat", false);
			}
			break;


		case 70212:
			ClanHandler.updateSetupInterface(player);
			player.getPA().showInterface(24100);
			break;
			

			/**Shop**/
		case 169136:
		case 169139:
		case 169142:
		case 169145:
		case 169148:
		case 169151:
		case 169154:
		case 169157:
		case 169160:
		case 169163:
		case 167251:
			ShopController.selectShop(player, buttonID);
			break;
		case 169196:
			player.noteShop = !player.noteShop;
			ShopController.updateShop(player, player.shopOpen);
			break;
			/**End Shop**/


		case 53152:
			Cooking.getAmount(player, 1);
			break;
		case 53151:
			Cooking.getAmount(player, 5);
			break;
		case 53150:
			Cooking.getAmount(player, 10);
			break;
		case 53149:
			Cooking.getAmount(player, 28);
			break;

			case 33206:// Attack button
			case 34142:
				SkillGuides.atkInterface(player);
				break;
			case 33209:// str button
			case 34119:
				SkillGuides.strInterface(player);
				break;
			case 33212: //Defence
			case 34120:
				SkillGuides.defInterface(player);
				break;
			case 34133:
			case 33215: //Range
				SkillGuides.rangeInterface(player);
				break;
			case 34123:
			case 33207: //Hitpoints
				//SkillGuides.hpInterface(c);
				break;
			case 34139:
			case 33218: //Prayer 
				SkillGuides.prayInterface(player);
				break;
			case 34136:
			case 33221: //Magic

				SkillGuides.mageInterface(player);
				break;
			case 34155:
			case 33224: //Runecrafting
				SkillGuides.rcInterface(player);
				break;
			case 34158:
			case 33210: //Agility
				SkillGuides.agilityInterface(player);
				break;
			case 34161:
			case 33213: //Herblore
				SkillGuides.herbloreInterface(player);
				break;
			case 59199:
			case 33216: //Theiving
				SkillGuides.thievingInterface(player);
				break;
			case 59202:
			case 33219: //craft
				SkillGuides.craftingInterface(player);
				break;
			case 33222: //Fletching
				SkillGuides.fletchingInterface(player);
				break;
			case 59205:
			case 47130: //Slayer
				SkillGuides.slayerInterface(player);
				break;
			case 33208: //Mining
				SkillGuides.miningInterface(player);
				break;
			case 33211: //Smithing
				SkillGuides.smithingInterface(player);
				break;
			case 33214: //Fishing
				SkillGuides.fishingInterface(player);
				break;
			case 33217: //Cooking
				SkillGuides.cookingInterface(player);
				break;
			case 33220: //Firemaking
				SkillGuides.firemakingInterface(player);
				break;
			case 33223: //Woodcutting
				SkillGuides.woodcuttingInterface(player);
				break;
			case 54104: //Farming
				SkillGuides.farmingInterface(player);
				break;
		

		case 19136:
			//Toggle quick prayers
			if (player.quickPray || player.quickCurse) {
				QuickCurses.turnOffQuicks(player);
				if (!player.prayerActive[Constants.PROTECT_ITEM]) {
					ItemsKeptOnDeath.updateInterface(player);
				}
				return;
			}
			QuickCurses.turnOnQuicks(player);
			break;

		case 19137:
			//Select quick prayers
			QuickCurses.selectQuickInterface(player);
			break;

		case 67079:
			//quick curse confirm
			QuickCurses.clickConfirm(player);
			break;

		case 5001:
			//select your quick prayers/curses
			QuickCurses.selectQuickInterface(player);
			player.getPA().sendFrame106(5);
			break;


			// Close button in Dawntained Guide interface
		case 102189:
			player.getPA().closeAllWindows();
			break;



		case 9157:
			player.dialogues.dialogues[player.dialogueProgress].options.click(player, 1);
			break;
		case 9158:
			player.dialogues.dialogues[player.dialogueProgress].options.click(player, 2);
			break;

		case 9167:
			player.dialogues.dialogues[player.dialogueProgress].options.click(player, 1);
			break;
		case 9168:
			player.dialogues.dialogues[player.dialogueProgress].options.click(player, 2);
			break;
		case 9169:
			player.dialogues.dialogues[player.dialogueProgress].options.click(player, 3);
			break;

		case 9178:
			player.dialogues.dialogues[player.dialogueProgress].options.click(player, 1);
			break;
		case 9179:
			player.dialogues.dialogues[player.dialogueProgress].options.click(player, 2);
			break;
		case 9180:
			player.dialogues.dialogues[player.dialogueProgress].options.click(player, 3);
			break;
		case 9181:
			player.dialogues.dialogues[player.dialogueProgress].options.click(player, 4);
			break;

		case 9190:
			player.dialogues.dialogues[player.dialogueProgress].options.click(player, 1);
			break;
		case 9191:
			player.dialogues.dialogues[player.dialogueProgress].options.click(player, 2);
			break;
		case 9192:
			player.dialogues.dialogues[player.dialogueProgress].options.click(player, 3);
			break;
		case 9193:
			player.dialogues.dialogues[player.dialogueProgress].options.click(player, 4);
			break;
		case 9194:
			player.dialogues.dialogues[player.dialogueProgress].options.click(player, 5);
			break;




		case 153:
			if (!player.resting) {
				player.getPA().startResting();
			} else {
				player.getPA().stopResting();
			}
			break;


			/* Destroy item */

		case 55096:
			player.getPA().removeAllWindows(); // Choosing No will remove all
			player.droppedItem = -1;
			break;
		case 55095:
			player.getPA().destroyItem(player.droppedItem); // Choosing Yes will
			player.droppedItem = -1;
			break;

			// Show equipment stats
		case 59097:
			if (player.getCombat().inCombatAlert())
			{
				return;
			}
			player.getPA().showInterface(15106);
			player.getPA().changeToSidebar(3);
			break;

			// Show items kept on death
		case 59100:
			if (player.getCombat().inCombatAlert())
			{
				return;
			}
			ItemsKeptOnDeath.showDeathInterface(player);
			break;


		case 58253:
			player.setInventoryUpdate(true);
			break;
		case 59004:
			player.getPA().removeAllWindows();
			break;
		case 150:
		case 89061:
			// Auto retaliate
			player.autoRet = (player.autoRet == 0) ? 1 : 0;
			break;

			/** Specials **/
		case 29188:
			player.specBarId = 7636;
			player.usingSpecial = !player.usingSpecial;
			ItemAssistant.updateSpecialBar(player);
			break;
		case 29163:
			player.specBarId = 7611;
			player.usingSpecial = !player.usingSpecial;
			ItemAssistant.updateSpecialBar(player);
			break;
		case 33033:
			player.specBarId = 8505;
			player.usingSpecial = !player.usingSpecial;
			ItemAssistant.updateSpecialBar(player);
			break;
		case 29049:
			player.specBarId = 7486;
			player.usingSpecial = !player.usingSpecial;
			ItemAssistant.updateSpecialBar(player);
			break;
		case 29038:
			player.specBarId = 7486;
			if (player.playerEquipment[Constants.WEAPON_SLOT] == 4153)
			{
				player.getCombat().handleGmaulPlayer();
			}
			else
			{
				player.usingSpecial = !player.usingSpecial;
			}
			ItemAssistant.updateSpecialBar(player);
			break;
		case 8041:
			player.getPA().removeAllWindows();
			break;
		case 29063:
			if (player.getCombat().checkSpecAmount(
					player.playerEquipment[Constants.WEAPON_SLOT]))
			{
				player.gfx0(246);
				player.forcedChat("Raarrrrrgggggghhhhhhh!");
				player.startAnimation(1056);
				player.skillLevel[2] = player.getLevelForXP(player.playerXP[2]) + (player.getLevelForXP(player.playerXP[2]) * 15 / 100);
				player.getPA().refreshSkill(2);
				ItemAssistant.updateSpecialBar(player);
			}
			else
			{
				player.sendMessage("You don't have the required special energy to use this attack.");
			}
			break;
		case 48023:
			player.specBarId = 12335;
			player.usingSpecial = !player.usingSpecial;
			ItemAssistant.updateSpecialBar(player);
			break;
		case 29138:
			player.specBarId = 7586;
			player.usingSpecial = !player.usingSpecial;
			ItemAssistant.updateSpecialBar(player);
			break;
		case 29113:
			player.specBarId = 7561;
			player.usingSpecial = !player.usingSpecial;
			ItemAssistant.updateSpecialBar(player);
			break;
		case 29238:
			player.specBarId = 7686;
			player.usingSpecial = !player.usingSpecial;
			ItemAssistant.updateSpecialBar(player);
			break;
		case 29199:
			player.specBarId = 7624;
			player.usingSpecial = !player.usingSpecial;
			ItemAssistant.updateSpecialBar(player);
			break;
		case 30108:
			// Claws
			player.specBarId = 7812;
			player.usingSpecial = !player.usingSpecial;
			ItemAssistant.updateSpecialBar(player);
			break; /** Dueling **/
		case 26065:
			// no forfeit
		case 26040:
			player.duelSlot = -1;
			player.getDuelArena().selectRule(0);
			break;
		case 26066:
			// no movement
		case 26048:
			player.duelSlot = -1;
			player.getDuelArena().selectRule(1);
			break;
		case 26069:
			// no range
		case 26042:
			player.duelSlot = -1;
			player.getDuelArena().selectRule(2);
			break;
		case 26070:
			// no melee
		case 26043:
			player.duelSlot = -1;
			player.getDuelArena().selectRule(3);
			break;
		case 26071:
			// no mage
		case 26041:
			player.duelSlot = -1;
			player.getDuelArena().selectRule(4);
			break;
		case 26072:
			// no drinks
		case 26045:
			player.duelSlot = -1;
			player.getDuelArena().selectRule(5);
			break;
		case 26073:
			// no food
		case 26046:
			player.duelSlot = -1;
			player.getDuelArena().selectRule(6);
			break;
		case 26074:
			// no prayer
		case 26047:
			player.duelSlot = -1;
			player.getDuelArena().selectRule(7);
			break;
		case 26076:
			// obsticals
		case 26075:
			player.duelSlot = -1;
			player.getDuelArena().selectRule(8);
			break;
		case 2158:
			// fun weapons
		case 2157:
			player.duelSlot = -1;
			player.getDuelArena().selectRule(9);
			break;
		case 30136:
			// sp attack
		case 30137:
			player.duelSlot = -1;
			player.getDuelArena().selectRule(10);
			break;
		case 53245:
			// no helm
			player.duelSlot = 0;
			player.getDuelArena().selectRule(11);
			break;
		case 53246:
			// no cape
			player.duelSlot = 1;
			player.getDuelArena().selectRule(12);
			break;
		case 53247:
			// no ammy
			player.duelSlot = 2;
			player.getDuelArena().selectRule(13);
			break;
		case 53249:
			// no weapon.
			player.duelSlot = 3;
			player.getDuelArena().selectRule(14);
			break;
		case 53250:
			// no body
			player.duelSlot = 4;
			player.getDuelArena().selectRule(15);
			break;
		case 53251:
			// no shield
			player.duelSlot = 5;
			player.getDuelArena().selectRule(16);
			break;
		case 53252:
			// no legs
			player.duelSlot = 7;
			player.getDuelArena().selectRule(17);
			break;
		case 53255:
			// no gloves
			player.duelSlot = 9;
			player.getDuelArena().selectRule(18);
			break;
		case 53254:
			// no boots
			player.duelSlot = 10;
			player.getDuelArena().selectRule(19);
			break;
		case 53253:
			// no rings
			player.duelSlot = 12;
			player.getDuelArena().selectRule(20);
			break;
		case 53248:
			// no arrows
			player.duelSlot = 13;
			player.getDuelArena().selectRule(21);
			break;

		case 26018:
			if (player.duelStatus == 5)
			{
				return;
			}
			if (player.inDuelArena())
			{
				Client o = (Client) PlayerHandler.players[player.duelingWith];
				if (o == null)
				{
					player.getDuelArena().declineDuel();
					return;
				}

				if (player.duelRule[2] && player.duelRule[3] && player.duelRule[4])
				{
					player.sendMessage("You won't be able to attack the player with the rules you have set.");
					break;
				}
				player.duelStatus = 2;
				if (player.duelStatus == 2)
				{
					player.getPA().sendFrame126("Waiting for other player...", 6684);
							o.getPA().sendFrame126("Other player has accepted.", 6684);
				}
				if (o.duelStatus == 2)
				{
					o.getPA().sendFrame126("Waiting for other player...", 6684);
					player.getPA().sendFrame126("Other player has accepted.", 6684);
				}

				if (player.duelStatus == 2 && o.duelStatus == 2)
				{
					player.canOffer = false;
					o.canOffer = false;
					player.duelStatus = 3;
					o.duelStatus = 3;
					player.getDuelArena().confirmDuel();
					o.getDuelArena().confirmDuel();
				}
			}
			else
			{
				Client o = (Client) PlayerHandler.players[player.duelingWith];
				player.getDuelArena().declineDuel();
				o.getDuelArena().declineDuel();
				player.sendMessage("You can't stake out of Duel Arena.");
			}
			break;

			/*
			 * Accepting Duel Interface Fixed by: Ardi Remember to click thanks
			 * button & karma (reputation) for Ardi, if you're using this.
			 */
		case 25120:
			if (player.duelStatus == 5)
			{
				// c.sendMessage("This glitch has been fixed by Ardi, sorry sir.");
				return;
			}
			if (player.inDuelArena())
			{
				if (player.duelStatus == 5)
				{
					break;
				}
				final Client o1 = (Client) PlayerHandler.players[player.duelingWith];
				if (o1 == null)
				{
					player.getDuelArena().declineDuel();
					return;
				}

				player.duelStatus = 4;
				if (o1.duelStatus == 4 && player.duelStatus == 4)
				{
					player.getDuelArena().startDuel();
					o1.getDuelArena().startDuel();
					o1.duelCount = 4;
					player.duelCount = 4;

					CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
					{@
						Override
						public void execute(
								CycleEventContainer container)
					{
						player.duelForceChatCount--;
						player.forcedChat("" + player.duelForceChatCount + "");
						if (player.duelForceChatCount == 0)
						{
							container.stop();
						}
					}

					@
					Override
					public void stop()
					{
						player.forcedChat("FIGHT!");
						player.duelForceChatCount = 4;
						player.damageTaken = new int[Configuration.MAX_PLAYERS];
						player.duelCount = 0;
					}
					}, 2);

					CycleEventHandler.getSingleton().addEvent(o1, new CycleEvent()
					{@
						Override
						public void execute(
								CycleEventContainer container)
					{
						o1.duelForceChatCount--;
						o1.forcedChat("" + o1.duelForceChatCount + "");
						if (o1.duelForceChatCount == 0)
						{
							container.stop();
						}
					}

					@
					Override
					public void stop()
					{
						o1.forcedChat("FIGHT!");
						o1.duelForceChatCount = 4;
						o1.damageTaken = new int[Configuration.MAX_PLAYERS];
						o1.duelCount = 0;
					}
					}, 2);

				}
				else
				{
					player.getPA().sendFrame126("Waiting for other player...", 6571);
					o1.getPA().sendFrame126("Other player has accepted", 6571);
				}
			}
			else
			{
				Client o = (Client) PlayerHandler.players[player.duelingWith];
				player.getDuelArena().declineDuel();
				o.getDuelArena().declineDuel();
				player.sendMessage("You can't stake out of Duel Arena.");
			}
			break;


		case 152:
			if (player.getDoingAgility())
			{
				return;
			}
			if (player.isRunning2)
			{
				player.isRunning2 = false;
				player.getPA().sendFrame36(173, 0);
			}
			else
			{
				player.isRunning2 = true;
				player.getPA().sendFrame36(173, 1);
			}
			break;

		case 140178:
			AchievementHandler.add(player, 22, "easy", 1);
			player.getPA().showInterface(25000);
			break;


		case 9154:
		case 144137:
			if (player.getCombat().inCombatAlert()) {
				return;
			}
			player.outStream.createFrame(109);
			player.manualLogOut = true;
			break;
		case 21233:
			// thick skin
			player.getCombat().activatePrayer(0);
			break;
		case 21234:
			// burst of str
			player.getCombat().activatePrayer(1);
			break;
		case 21235:
			// charity of thought
			player.getCombat().activatePrayer(2);
			break;
		case 70080:
			// range
			player.getCombat().activatePrayer(3);
			break;
		case 70082:
			// mage
			player.getCombat().activatePrayer(4);
			break;
		case 21236:
			// rockskin
			player.getCombat().activatePrayer(5);
			break;
		case 21237:
			// super human
			player.getCombat().activatePrayer(6);
			break;
		case 21238:
			// improved reflexes
			player.getCombat().activatePrayer(7);
			break;
		case 21239:
			// hawk eye
			player.getCombat().activatePrayer(8);
			break;
		case 21240:
			player.getCombat().activatePrayer(9);
			break;
		case 21241:
			// protect Item
			if (!player.getRedSkull())
			{
				player.getCombat().activatePrayer(10);
			}
			else
			{
				player.sendMessage("You cannot use this while on red skull.");
				player.prayerActive[Constants.PROTECT_ITEM] = false;
				player.getPA().sendFrame36(player.PRAYER_GLOW[10], 0);
			}
			ItemsKeptOnDeath.updateInterface(player);
			break;
		case 70084:
			// 26 range
			player.getCombat().activatePrayer(11);
			break;
		case 70086:
			// 27 mage
			player.getCombat().activatePrayer(12);
			break;
		case 21242:
			// steel skin
			player.getCombat().activatePrayer(13);
			break;
		case 21243:
			// ultimate str
			player.getCombat().activatePrayer(14);
			break;
		case 21244:
			// incredible reflex
			player.getCombat().activatePrayer(15);
			break;
		case 21245:
			// protect from magic
			player.getCombat().activatePrayer(16);
			break;
		case 21246:
			// protect from range
			player.getCombat().activatePrayer(17);
			break;
		case 21247:
			// protect from melee
			player.getCombat().activatePrayer(18);
			break;
		case 70088:
			// 44 range
			player.getCombat().activatePrayer(19);
			break;
		case 70090:
			// 45 mystic
			player.getCombat().activatePrayer(20);
			break;
		case 2171:
			// retrui
			player.getCombat().activatePrayer(21);
			break;
		case 2172:
			// redem
			player.getCombat().activatePrayer(22);
			break;
		case 2173:
			// smite
			player.getCombat().activatePrayer(23);
			break;
		case 70092:
			// chiv
			player.getCombat().activatePrayer(24);
			break;
		case 70094:
			// piety
			player.getCombat().activatePrayer(25);
			break;

		case 13092:
			Client other = (Client) PlayerHandler.players[player.tradeWith];
			if (other == null)
			{
				player.getTradeAndDuel().declineTrade();
				player.sendMessage("Trade declined as the other player has disconnected.");
				break;
			}
			player.getPA().sendFrame126("Waiting for other player...", 3431);
			other.getPA().sendFrame126("Other player has accepted", 3431);
			player.goodTrade = true;
			other.goodTrade = true;
			for (GameItem item: player.getTradeAndDuel().offeredItems)
			{
				if (item.id > 0)
				{
					if (ItemAssistant.freeSlots(other) < player.getTradeAndDuel().offeredItems.size())
					{
						player.sendMessage(other.playerName + " only has " + ItemAssistant.freeSlots(other) + " free slots, please remove " + (player.getTradeAndDuel().offeredItems.size() - ItemAssistant.freeSlots(other)) + " items.");
						other.sendMessage(player.playerName + " has to remove " + (player.getTradeAndDuel().offeredItems.size() - ItemAssistant.freeSlots(other)) + " items or you could offer them " + (player.getTradeAndDuel().offeredItems.size() - ItemAssistant.freeSlots(other)) + " items.");
						player.goodTrade = false;
						other.goodTrade = false;
						player.getPA().sendFrame126("Not enough inventory space...", 3431);
						other.getPA().sendFrame126("Not enough inventory space...", 3431);
						break;
					}
					else
					{
						player.getPA().sendFrame126("Waiting for other player...", 3431);
						other.getPA().sendFrame126("Other player has accepted", 3431);
						player.goodTrade = true;
						other.goodTrade = true;
					}
				}
			}
			if (player.inTrade && !player.tradeConfirmed && other.goodTrade && player.goodTrade)
			{
				player.tradeConfirmed = true;
				if (other.tradeConfirmed)
				{
					player.getTradeAndDuel().confirmScreen();
					other.getTradeAndDuel().confirmScreen();
					break;
				}
			}
			break;
		case 13218:
			player.tradeAccepted = true;
			Client ot1 = (Client) PlayerHandler.players[player.tradeWith];
			if (ot1 == null)
			{
				player.getTradeAndDuel().declineTrade();
				player.sendMessage("Trade declined as the other player has disconnected.");
				break;
			}
			if (player.inTrade && player.tradeConfirmed && ot1.tradeConfirmed && !player.tradeConfirmed2)
			{
				player.tradeConfirmed2 = true;
				if (ot1.tradeConfirmed2)
				{
					player.acceptedTrade = true;
					ot1.acceptedTrade = true;
					player.getTradeAndDuel().giveItems();
					ot1.getTradeAndDuel().giveItems();
					break;
				}
				ot1.getPA().sendFrame126("Other player has accepted.", 3535);
				player.getPA().sendFrame126("Waiting for other player...", 3535);
			}
			break;
		case 74176:
			if (!player.mouseButton)
			{
				player.mouseButton = true;
				player.getPA().sendFrame36(500, 1);
				player.getPA().sendFrame36(170, 1);
			}
			else if (player.mouseButton)
			{
				player.mouseButton = false;
				player.getPA().sendFrame36(500, 0);
				player.getPA().sendFrame36(170, 0);
			}
			break;
		case 3189:
			if (!player.splitChat)
			{
				player.getPA().sendFrame36(502, 1);
				player.getPA().sendFrame36(287, 1);
				player.splitChat = true;
			}
			else if (player.splitChat)
			{
				player.getPA().sendFrame36(502, 0);
				player.getPA().sendFrame36(287, 0);
				player.splitChat = false;
			}
			break;
		case 74180:
			if (!player.chatEffects)
			{
				player.chatEffects = true;
				player.getPA().sendFrame36(501, 1);
				player.getPA().sendFrame36(171, 0);
			}
			else
			{
				player.chatEffects = false;
				player.getPA().sendFrame36(501, 0);
				player.getPA().sendFrame36(171, 1);
			}
			break;
		case 74188:
			if (!player.acceptAid)
			{
				player.acceptAid = true;
				player.getPA().sendFrame36(503, 1);
				player.getPA().sendFrame36(427, 1);
			}
			else
			{
				player.acceptAid = false;
				player.getPA().sendFrame36(503, 0);
				player.getPA().sendFrame36(427, 0);
			}
			break;
		case 140180:
			player.fogIntensity = 0;
			player.checkFog();
			break;

		case 140182:
			player.fogIntensity = 1;
			player.checkFog();
			break;

		case 140184:
			player.fogIntensity = 2;
			player.checkFog();
			break;

		case 140186:
			player.fogIntensity = 3;
			player.checkFog();
			break;
		case 3138:
			player.brightnessLevel = 0;
			player.checkBrightness();
			break;

		case 3140:
			player.brightnessLevel = 1;
			player.checkBrightness();
			break;

		case 3142:
			player.brightnessLevel = 2;
			player.checkBrightness();
			break;

		case 3144:
			player.brightnessLevel = 3;
			player.checkBrightness();
			break;
		case 74206:
			// area1
			player.getPA().sendFrame36(509, 1);
			player.getPA().sendFrame36(510, 0);
			player.getPA().sendFrame36(511, 0);
			player.getPA().sendFrame36(512, 0);
			break;
		case 74207:
			// area2
			player.getPA().sendFrame36(509, 0);
			player.getPA().sendFrame36(510, 1);
			player.getPA().sendFrame36(511, 0);
			player.getPA().sendFrame36(512, 0);
			break;
		case 74208:
			// area3
			player.getPA().sendFrame36(509, 0);
			player.getPA().sendFrame36(510, 0);
			player.getPA().sendFrame36(511, 1);
			player.getPA().sendFrame36(512, 0);
			break;
		case 74209:
			// area4
			player.getPA().sendFrame36(509, 0);
			player.getPA().sendFrame36(510, 0);
			player.getPA().sendFrame36(511, 0);
			player.getPA().sendFrame36(512, 1);
			break;

			/* Emote */

		case 168:
			// Yes
			if (System.currentTimeMillis() - player.diceDelay >= 1200)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(855);
			}
			break;
		case 169:
			if (System.currentTimeMillis() - player.diceDelay >= 1200)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(856);
			}
			break;
		case 162:
			if (System.currentTimeMillis() - player.diceDelay >= 1100)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(857);
			}
			break;
		case 164:
			if (System.currentTimeMillis() - player.diceDelay >= 1200)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(858);
			}
			break;
		case 165:
			if (System.currentTimeMillis() - player.diceDelay >= 1500)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(859);
			}
			break;
		case 161:
			if (System.currentTimeMillis() - player.diceDelay >= 1500)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(860);
			}
			break;
		case 170:
			if (System.currentTimeMillis() - player.diceDelay >= 1400)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(861);
			}
			break;
		case 171:
			if (System.currentTimeMillis() - player.diceDelay >= 1500)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(862);
			}
			break;
		case 163:
			if (System.currentTimeMillis() - player.diceDelay >= 1500)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(863);
			}
			break;
		case 167:
			if (System.currentTimeMillis() - player.diceDelay >= 1200)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(864);
			}
			break;
		case 172:
			if (System.currentTimeMillis() - player.diceDelay >= 2100)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(865);
			}
			break;
		case 166:
			if (System.currentTimeMillis() - player.diceDelay >= 3600)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(866);
			}
			break;
		case 52050:
			if (System.currentTimeMillis() - player.diceDelay >= 1200)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(2105);
			}
			break;
		case 52051:
			if (System.currentTimeMillis() - player.diceDelay >= 3100)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(2106);
			}
			break;
		case 52052:
			if (System.currentTimeMillis() - player.diceDelay >= 400)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(2107);
			}
			break;
		case 52053:
			if (System.currentTimeMillis() - player.diceDelay >= 3000)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(2108);
			}
			break;
		case 52054:
			if (System.currentTimeMillis() - player.diceDelay >= 1200)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(2109);
			}
			break;
		case 52055:
			if (System.currentTimeMillis() - player.diceDelay >= 1500)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(2110);
			}
			break;
		case 52056:
			if (System.currentTimeMillis() - player.diceDelay >= 1850)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(2111);
			}
			break;
		case 52057:
			if (System.currentTimeMillis() - player.diceDelay >= 700)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(2112);
			}
			break;
		case 52058:
			if (System.currentTimeMillis() - player.diceDelay >= 620)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(2113);
			}
			break;
		case 43092:
			if (System.currentTimeMillis() - player.diceDelay >= 5200)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(0x558);
				player.gfx0(574);
			}
			break;
		case 2155:
			if (System.currentTimeMillis() - player.diceDelay >= 1500)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(0x46B);
			}
			break;
		case 25103:
			if (System.currentTimeMillis() - player.diceDelay >= 1500)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(0x46A);
			}
			break;
		case 25106:
			if (System.currentTimeMillis() - player.diceDelay >= 1500)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(0x469);
			}
			break;
		case 2154:
			if (System.currentTimeMillis() - player.diceDelay >= 1500)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(0x468);
			}
			break;
		case 52071:
			if (System.currentTimeMillis() - player.diceDelay >= 1000)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(0x84F);
			}
			break;
		case 52072:
			if (System.currentTimeMillis() - player.diceDelay >= 1500)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(0x850);
			}
			break;
		case 59062:
			if (System.currentTimeMillis() - player.diceDelay >= 3100)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(2836);
			}
			break;
		case 72032:
			if (System.currentTimeMillis() - player.diceDelay >= 6000)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(3544);
			}
			break;
		case 72033:
			if (System.currentTimeMillis() - player.diceDelay >= 3010)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(3543);
			}
			break;
		case 72254:
			if (System.currentTimeMillis() - player.diceDelay >= 4100)
			{
				player.diceDelay = System.currentTimeMillis();
				player.startAnimation(6111);
			}
			break;
		case 118098:
			// Vengeance
			if (player.vengOn == false)
			{
				player.getPA().vengMe();
				player.doingActionEvent(6);
				Movement.stopMovement(player);
			}
			else
			{
				player.sendMessage("You have already casted vengeance.");
			}
			break;

		case 24017:
			player.getPA().resetAutocast();
			ItemAssistant.updateCombatInterface(player, player.playerEquipment[Constants.WEAPON_SLOT], ItemAssistant.getItemName(player.playerEquipment[Constants.WEAPON_SLOT]));
			break;

		case 74108:
			handleSkillCape(player);
			break;

		}
		if (player.isAutoButton(buttonID)) player.assignAutocast(buttonID);
	}

	/**
	 * Skill cape emotes.
	 *
	 * @param player
	 *            The player.
	 */
	public void handleSkillCape(Client player)
	{
		if (player.getCombat().inCombatAlert())
		{
			return;
		}

		// Attack cape
		if (player.playerEquipment[Constants.CAPE_SLOT] == 9747 || player.playerEquipment[Constants.CAPE_SLOT] == 9748)
		{
			player.startAnimation(4959);
			player.gfx0(823);
			player.doingActionEvent(5);
		}

		// Strength cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9750 || player.playerEquipment[Constants.CAPE_SLOT] == 9751)
		{
			player.startAnimation(4981);
			player.gfx0(828);
			player.doingActionEvent(16);
		}

		// Defence cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9753 || player.playerEquipment[Constants.CAPE_SLOT] == 9754)
		{
			player.startAnimation(4961);
			player.gfx0(824);
			player.doingActionEvent(9);
		}

		// Ranging cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9756 || player.playerEquipment[Constants.CAPE_SLOT] == 9757)
		{
			player.startAnimation(4973);
			player.gfx0(832);
			player.doingActionEvent(8);
		}

		// Prayer cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9759 || player.playerEquipment[Constants.CAPE_SLOT] == 9760)
		{
			player.startAnimation(4979);
			player.gfx0(829);
			player.doingActionEvent(10);
		}

		// Magic cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9762 || player.playerEquipment[Constants.CAPE_SLOT] == 9763)
		{
			player.startAnimation(4939);
			player.gfx0(813);
			player.doingActionEvent(5);
		}

		// Runecrafting cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9765 || player.playerEquipment[Constants.CAPE_SLOT] == 9766)
		{
			player.startAnimation(4947);
			player.gfx0(817);
			player.doingActionEvent(10);
		}

		// Hitpoints cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9768 || player.playerEquipment[Constants.CAPE_SLOT] == 9769)
		{
			player.startAnimation(4971);
			player.gfx0(833);
			player.doingActionEvent(6);
		}

		// Agility
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9771 || player.playerEquipment[Constants.CAPE_SLOT] == 9772)
		{
			player.startAnimation(4977);
			player.gfx0(830);
			player.doingActionEvent(7);
		}

		// Herblore cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9774 || player.playerEquipment[Constants.CAPE_SLOT] == 9775)
		{
			player.startAnimation(4969);
			player.gfx0(835);
			player.doingActionEvent(14);
		}

		// Thieving cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9777 || player.playerEquipment[Constants.CAPE_SLOT] == 9778)
		{
			player.startAnimation(4965);
			player.gfx0(826);
			player.doingActionEvent(5);
		}

		// Crafting cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9780 || player.playerEquipment[Constants.CAPE_SLOT] == 9781)
		{
			player.startAnimation(4949);
			player.gfx0(818);
			player.doingActionEvent(13);
		}

		// Fletching cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9783 || player.playerEquipment[Constants.CAPE_SLOT] == 9784)
		{
			player.startAnimation(4937);
			player.doingActionEvent(13);
			// c.gfx0(812);
		}

		// Slayer cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9786 || player.playerEquipment[Constants.CAPE_SLOT] == 9787)
		{
			player.startAnimation(4967);
			player.doingActionEvent(4);
			player.gfx0(827);
		}

		// Mining cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9792 || player.playerEquipment[Constants.CAPE_SLOT] == 9793)
		{
			player.startAnimation(4941);
			player.gfx0(814);
			player.doingActionEvent(7);
		}

		// Smithing cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9795 || player.playerEquipment[Constants.CAPE_SLOT] == 9796)
		{
			player.startAnimation(4943);
			player.doingActionEvent(19);
			player.gfx0(815);
		}

		// Fishing cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9798 || player.playerEquipment[Constants.CAPE_SLOT] == 9799)
		{
			player.startAnimation(4951);
			player.doingActionEvent(12);
			player.gfx0(819);
		}

		// Cooking cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9801 || player.playerEquipment[Constants.CAPE_SLOT] == 9802)
		{
			player.startAnimation(4955);
			player.gfx0(821);
			player.doingActionEvent(24);
		}

		// Firemaking cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9804 || player.playerEquipment[Constants.CAPE_SLOT] == 9805)
		{
			player.startAnimation(4975);
			player.gfx0(831);
			player.doingActionEvent(7);
		}

		// Woodcutting cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9807 || player.playerEquipment[Constants.CAPE_SLOT] == 9808)
		{
			player.startAnimation(4957);
			player.gfx0(822);
			player.doingActionEvent(20);
		}

		// Farming cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9810 || player.playerEquipment[Constants.CAPE_SLOT] == 9811)
		{
			player.startAnimation(4963);
			player.gfx0(825);
			player.doingActionEvent(12);
		}

		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9813)
		{
			player.startAnimation(4945);
			player.gfx0(816);
			player.doingActionEvent(14);
		}

		// Completionist cape, Dungeoneering master cape,
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 19710 || player.playerEquipment[Constants.CAPE_SLOT] >= 20758 && player.playerEquipment[Constants.CAPE_SLOT] <= 20769)
		{
			player.completionistCapeEmote();
			player.doingActionEvent(16);
		}

		// Hunter cape
		else if (player.playerEquipment[Constants.CAPE_SLOT] == 9948 || player.playerEquipment[Constants.CAPE_SLOT] == 9949)
		{
			player.startAnimation(5158);
			player.gfx0(907);
			player.doingActionEvent(11);
		}
		else
		{
			player.sendMessage("You need a skillcape to perform this emote.");
		}
	}

}