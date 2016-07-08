package ionic.player.content.miscellaneous;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import utility.Misc;

public class EssencePouch {
	
	private static final int PURE_ESSENCE = 7936;
	private static final int RUNE_ESSENCE = 1436;
	
	public static void open(Player c) {
		refreshTexts(c);
		refreshItems(c);
		c.getPA().showInterface(31200);
	}
	public static void refreshItems(Player c) {
		for (int i = 0; i < c.pouchSize; i++) {
			if (c.essence[i] <= 0) {//empty
				c.getPA().sendFrame34a(31206, -1, i, 1);
			} else if (c.essence[i] == 4) {//rune essence
				c.getPA().sendFrame34a(31206, RUNE_ESSENCE, i, 1);
			} else if (c.essence[i] == 9) {//pure essence
				c.getPA().sendFrame34a(31206, PURE_ESSENCE, i, 1);
			}
		}
	}
	public static void refreshTexts(Player c) {
		c.getPA().sendFrame126("Space: "+c.pouchSize, 31203);
		c.getPA().sendFrame126("Essence: "+getPouchEssence(c), 31204);
	}
	
	public static void withdraw(Player c) {
		if (ItemAssistant.freeSlots(c) <= 0) {
			c.sendMessage("You need more free inventory slots");
			return;
		}
		for (int i = 0; i < c.pouchSize; i++) {
			if (ItemAssistant.freeSlots(c) == 0) {
				refreshTexts(c);
				refreshItems(c);
				c.sendMessage("You need more free inventory slots");
				return;
			}
			int slot = getEssenceSlot(c);
			if (slot == -1) {
				refreshTexts(c);
				refreshItems(c);
				c.sendMessage("Your pouch is empty.");
				return;
			}
			int toAdd = 0;
			if (c.essence[slot] == 4) {
				toAdd = RUNE_ESSENCE;
			} else if (c.essence[slot] == 9) {
				toAdd = PURE_ESSENCE;
			}
			ItemAssistant.addItem(c, toAdd, 1);
			c.essence[slot] = -1;
		}
		refreshTexts(c);
		refreshItems(c);
	}
	
	public static void deposit(Player c) {
		for (int i = 0; i < c.pouchSize; i++) {
			int s = getFreeSlot(c);
			if (s == -1) {
				refreshTexts(c);
				refreshItems(c);
				c.sendMessage("Your essence pouch is full!");
				return;
			}
			int toDelete = 0;
			if (ItemAssistant.playerHasItem(c, RUNE_ESSENCE)) {
				toDelete = RUNE_ESSENCE;
			}
			if (ItemAssistant.playerHasItem(c, PURE_ESSENCE)) {
				toDelete = PURE_ESSENCE;
			}
			if (toDelete == 0) {
				refreshTexts(c);
				refreshItems(c);
				return;
			}
			ItemAssistant.deleteItem(c, toDelete, 1);
			c.essence[s] = toDelete == PURE_ESSENCE ? 9 : 4;
		}
		refreshTexts(c);
		refreshItems(c);
	}
	
	public static int getFreeSlot(Player c) {
		for (int i = 0; i < c.pouchSize; i++) {
			if (c.essence[i] <= 0) {
				return i;
			}
		}
		return -1;
	}
	
	public static int getEssenceSlot(Player c) {
		for (int i = 0; i < c.pouchSize; i++) {
			if (c.essence[i] > 0) {
				return i;
			}
		}
		return -1;
	}
	
	public static int getPouchEssence(Player c) {
		int r = 0;
		for (int i = 0; i < c.pouchSize; i++) {
			if (c.essence[i] > 0) {
				r++;
			}
		}
		return r;
	}
	
	
	public static void extend(Player c) {
		if (!ItemAssistant.playerHasItem(c, 6953)) {
			return;
		}
		refreshTexts(c);
		int a = 2 + Misc.random(5);
		if (c.pouchSize == 54) {
			c.sendMessage("Your essence pouch is already the largest it can be!");
			return;
		}
		if (c.pouchSize + a > 54) {
			ItemAssistant.deleteItem(c, 6953, 1);
			c.pouchSize = 54;
			c.sendMessage("Your pouch is now the largest it can be, which can hold 54 essence!");
			return;
		} else {
			ItemAssistant.deleteItem(c, 6953, 1);
			c.pouchSize = c.pouchSize + a;
			c.sendMessage("You have extended your essence pouch, it can now hold "+c.pouchSize+" essence");
		}
	}
	
	

}
