package ionic.player.content.skills.cooking;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.achievements.AchievementHandler;
import ionic.player.dialogue.Dialogue;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import utility.Misc;
import core.Constants;

public class Cooking {

	public static void cookThisFood(Client p, int i, int object) {
		Cook c = Cook.forID(i);
		if (c == null) { Dialogue.sendStatement2(p, new String[]{"You cannot cook this."}); return;}
		cookFish(p, c.raw, c.xp, c.lvlReq, c.burn, c.cook, object);
	}

	private static int fishStopsBurning(Client p, int i) {
		Cook c = Cook.forID(i);
		if (c != null) {
			if(p.playerEquipment[Constants.HAND_SLOT] == 775) {
				return c.stopG;
			} else {
				return c.stop;
			}
		}
		return 99;
	}
	
	public static void handleAchievements(Player c, int f) {
		if (f == 315) { AchievementHandler.add(c, 4, "easy", 1); }
	}

	private static void cookFish(Client player, int itemID, int xpRecieved, int levelRequired, int burntFish, int cookedFish, int object) {
		if (player.skillLevel[7] < levelRequired) {
			Dialogue.sendStatement2(player, new String[] {"You need a cooking level of "+levelRequired+" to cook this/"});
			return;
		}
		int chance = player.skillLevel[7];
		if(player.playerEquipment[Constants.HAND_SLOT] == 775) {
			chance = player.skillLevel[7] + 8; 
		}
		if(chance <= 0) {
			chance = Misc.random(5);
		}
    	player.playerSkillProp[7][0] = itemID;
		player.playerSkillProp[7][1] = xpRecieved * Constants.COOKING_EXPERIENCE;
		player.playerSkillProp[7][2] = levelRequired;
		player.playerSkillProp[7][3] = burntFish;
		player.playerSkillProp[7][4] = cookedFish;
		player.playerSkillProp[7][5] = object;
		player.playerSkillProp[7][6] = chance;
		player.stopPlayerSkill = false;
		int item = ItemAssistant.getItemAmount(player, player.playerSkillProp[7][0]);
		if(item == 1) {
			player.doAmount = 1;
			cookTheFish(player);
			return;
		}
		viewCookInterface(player, itemID);
	}

	public static void getAmount(Client player, int amount) {
		int item = ItemAssistant.getItemAmount(player, player.playerSkillProp[7][0]);
		if(amount > item) {
			amount = item;
		}
		player.doAmount = amount;
		cookTheFish(player);
	}

	public static void resetCooking(Client c) {
		c.stopPlayerSkill = false;
		for(int i = 0; i < 6; i++) {
			c.playerSkillProp[7][i] = -1;
		}
	}

	private static void viewCookInterface(Client player, int item) {
        	player.getPA().sendFrame164(1743);
         	player.getPA().sendFrame246(13716, 190, item);
     		player.getPA().sendFrame126(""+ItemAssistant.getItemName(item)+"", 13717);
	}

	private static void cookTheFish(final Client player) {
		player.stopPlayerSkill = true;
		player.getPA().removeAllWindows();
		if(player.playerSkillProp[7][5] > 0) {
			player.startAnimation(player.playerSkillProp[7][5] == 3038 ? 897 : 896);
		}
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				ItemAssistant.deleteItem(player, player.playerSkillProp[7][0], ItemAssistant.getItemSlot(player, player.playerSkillProp[7][0]), 1);
				if(player.skillLevel[7] >= fishStopsBurning(player, player.playerSkillProp[7][0]) || Misc.random(player.playerSkillProp[7][6]) > Misc.random(player.playerSkillProp[7][2])) {
					player.sendMessage("You successfully cook the "+ ItemAssistant.getItemName(player.playerSkillProp[7][0]).toLowerCase() +".");
					player.getPA().addSkillXP(player.playerSkillProp[7][1], 7);
					ItemAssistant.addItem(player, player.playerSkillProp[7][4], 1);
					handleAchievements(player, player.playerSkillProp[7][4]);
				} else {
					player.sendMessage("You burn the "+ ItemAssistant.getItemName(player.playerSkillProp[7][0]).toLowerCase() +".");
					ItemAssistant.addItem(player, player.playerSkillProp[7][3], 1);
				}
				if(!ItemAssistant.playerHasItem(player, player.playerSkillProp[7][0], 1) || player.doAmount <= 0) {
					container.stop();
				}
				if(!player.stopPlayerSkill) {
					container.stop();
				}
			}
			@Override
			public void stop() {
				resetCooking(player);
			}
		}, 2);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {//
				if(player.playerSkillProp[7][5] > 0) {
					player.startAnimation(player.playerSkillProp[7][5] == 3038 ? 897 : 896);
				}
				if(!player.stopPlayerSkill) {
					container.stop();
				}
			}
			@Override
			public void stop() {

			}
		}, 4);
	}
	public static void pieBake(Client player, int ingredient1, int ingredient2, int ingredient3, int xp, int product) {
		player.playerSkillProp[8][0] = ingredient1;
		player.playerSkillProp[8][1] = ingredient2;
		player.playerSkillProp[8][2] = ingredient3;
		player.playerSkillProp[8][3] = xp * Constants.COOKING_EXPERIENCE;
		player.playerSkillProp[8][4] = product;
		player.stopPlayerSkill = false;
		int item = ItemAssistant.getItemAmount(player, player.playerSkillProp[8][0]);
		if(item == 1) {
			player.doAmount = 1;
			bakePie(player);
			return;
		}
	}
	public static void bakeThePie(Client p, int i) {
		Pies c = Pies.forID(i);
		if (c == null) { Dialogue.sendStatement2(p, new String[]{"You cannot cook this."}); return;}
		pieBake(p, c.ing1, c.ing2, c.ing3, c.exp, c.product);
	}
	public static void bakePie(Client player) {
		player.stopPlayerSkill = true;
		player.getPA().removeAllWindows();
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				ItemAssistant.deleteItem(player, player.playerSkillProp[8][0], ItemAssistant.getItemSlot(player, player.playerSkillProp[8][0]), 1);
				ItemAssistant.deleteItem(player, player.playerSkillProp[8][1], ItemAssistant.getItemSlot(player, player.playerSkillProp[8][1]), 1);
				ItemAssistant.deleteItem(player, player.playerSkillProp[8][2], ItemAssistant.getItemSlot(player, player.playerSkillProp[8][2]), 1);
					player.sendMessage("You successfully cook the "+ ItemAssistant.getItemName(player.playerSkillProp[8][4]).toLowerCase() +".");
					player.getPA().addSkillXP(player.playerSkillProp[8][3], 7);
					ItemAssistant.addItem(player, player.playerSkillProp[8][4], 1);
					handleAchievements(player, player.playerSkillProp[8][4]);
				if(!ItemAssistant.playerHasItem(player, player.playerSkillProp[8][0], 1) || player.doAmount <= 0) {
					container.stop();
				}
				if(!player.stopPlayerSkill) {
					container.stop();
				}
			}
			@Override
			public void stop() {
				resetCooking(player);
			}
		}, 2);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {//
				if(!player.stopPlayerSkill) {
					container.stop();
				}
			}
			@Override
			public void stop() {

			}
		}, 4);
	}
}