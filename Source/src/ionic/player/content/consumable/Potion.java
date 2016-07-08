package ionic.player.content.consumable;

import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Player;
/**
 * @author Keith
 */
public class Potion {

	public static void handleDrinking(Player c, int item, PotionData data, int slot) {
		c.potDelay = System.currentTimeMillis();
		c.foodDelay = c.potDelay;
		c.getCombat().resetPlayerAttack();
		if (c.resting) {
			c.getPA().stopResting();
		}
		int toDelete = 0, toAdd = 0;
		c.sendMessage("You drink some of your " + ionic.item.Item.getItemName(item) + ".");
		String message = "";
		if (item == data.six) {
			toDelete = data.six;	toAdd = data.five;
			message = "You have 5 doses of potion left.";
		} else if (item == data.five) {
			toDelete = data.five;	toAdd = data.four;
			message = "You have 4 doses of potion left.";
		} else if (item == data.four) {
			toDelete = data.four;	toAdd = data.three;
			message = "You have 3 doses of potion left.";
		} else if (item == data.three) {
			toDelete = data.three;	toAdd = data.two;
			message = "You have 2 doses of potion left.";
		} else if (item == data.two) {
			toDelete = data.two;	toAdd = data.one;
			message = "You have 1 dose of potion left.";
		} else if (item == data.one) {
			toDelete = data.one;	toAdd = 229;
			if (ItemData.data[item].getName().contains("flask"))
				toAdd = 22321;
			message = "You have finished your potion.";
		}
		c.playerItems[slot] = toAdd + 1;
		ItemAssistant.resetItems(c, 3214);
		//ItemAssistant.deleteItem(c, toDelete, slot, 1);
		//ItemAssistant.addItem(c, toAdd, 1);
		c.sendMessage(message);
		c.startAnimation(829);
		data.function(c);
	}

	public static boolean drink(Player c, int item, int slot) {
		PotionData data = PotionData.findPotion(item);
		if (data == null) {
			return false;
		}
		if (canDrink(c, item, slot)) {
			handleDrinking(c, item, data, slot);
		}
		return true;
	}

	public static boolean canDrink(Player c, int item, int slot) {
		if (c.playerItems[slot] != item + 1) {
			return false;
		}
		if (!ItemAssistant.playerHasItem(c, item)) {
			return false;
		}
		if (c.duelRule[5]) {
			c.sendMessage("You may not drink potions in this duel.");
			return false;
		}
		if (c.skillLevel[3] == 0) {
			return false;
		}
		if (c.isTeleporting() || c.doingAction(false)) {
			return false;
		}
		if (System.currentTimeMillis() - c.potDelay < 1000) {
			return false;
		}
		return true;
	}

	 
}
