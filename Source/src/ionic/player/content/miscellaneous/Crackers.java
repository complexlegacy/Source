package ionic.player.content.miscellaneous;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import utility.Misc;

public class Crackers {

	public static void handleCrackers(Player c, Player usedOn, int itemId) {
		if (!ItemAssistant.playerHasItem(c, itemId))
			return;

		if (ItemAssistant.freeSlots(c) < 1) {
			c.sendMessage("You don't have enough inventory space!");
			return;
		}
		if (ItemAssistant.freeSlots(usedOn) < 1) {
			c.sendMessage("The other player doesn't have enough inventory space!");
			return;
		}

		c.turnPlayerTo(usedOn.getX(), usedOn.getY());
		usedOn.turnPlayerTo(c.getX(), c.getY());
		c.sendMessage("You pull a Christmas cracker...");
		usedOn.sendMessage("You pull a Christmas cracker...");
		c.gfx0(176);
		c.startAnimation(15153);
		usedOn.startAnimation(15153);
		
		ItemAssistant.deleteItem(c, itemId, 1);
		if (Misc.random(1) == 0) {
			ItemAssistant.addItem(c, getRandomPhat(), 1);
			c.sendMessage("You got the prize!");
			c.forcedChat("Hey! I got the party hat!");
			usedOn.sendMessage("You didn't get the prize.");
		} else {
			ItemAssistant.addItem(usedOn, getRandomPhat(), 1);
			usedOn.sendMessage("You got the prize!");
			usedOn.forcedChat("Hey! I got the party hat!");
			c.sendMessage("You didn't get the prize.");
		}
	}

	private static int getRandomPhat() {
		int[] phats = { 1038, 1040, 1042, 1044, 1048 };
		return phats[(int) Math.floor(Math.random() * phats.length)];
	}
}