package ionic.player.content.gambling;

import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Player;
import ionic.player.dialogue.Dialogue;
import ionic.player.dialogue.DialogueList;
import ionic.player.dialogue.DialogueType;
import ionic.player.dialogue.Dialogues;

import java.math.BigInteger;

public class GamblingHandler {

	public static boolean clickButton(Player c, int button) {
		switch(button) {
		case 145096://Select dice duel
			if (c.gamble != null) {
				c.gamble.type = GambleGameType.DICE_DUEL;
				c.gamble.somethingChanged();
				c.gamble.updateType();
			}
			return true;
		case 145097://Select flower poker
			if (c.gamble != null) {
				c.gamble.type = GambleGameType.FLOWER_POKER;
				c.gamble.somethingChanged();
				c.gamble.updateType();
			}
			return true;
		case 145083://Click accept button
			clickAccept(c);
			return true;
		case 145086://Click decline button
			if (c.gamble != null && c.gamble.gameStage == 0) {
        		c.gamble.decline(c);
        	}
			return true;
		case 144240:
			c.getPA().closeAllWindows();
			return true;
		}
		return false;
	}

	public static void clickAccept(Player c) {
		if (c.gamble != null) {
			if (c.gamble.p1 == c) {
				c.gamble.p1Accepted = true;
			} else if (c.gamble.p2 == c) {
				c.gamble.p2Accepted = true;
			}
			c.gamble.updateStatus();
		}
	}


	public static void add(Player c, int item, int slot, int amount) {
		if (c.gamble == null || item <= 0 || (c.gamble.p1Accepted && c.gamble.p2Accepted)) {
			return;
		}
		if (ItemData.data[item].isUntradable()) {
			c.sendMessage("This item is untradable and cannot be gambled.");
			return;
		}
		if (c.playerItems[slot] == item + 1) {
			boolean stacks = false;
			if (ItemData.data[item].stackable || ItemData.data[item].isNoted()) {
				stacks = true;
			}
			int toSlot = getFreeSlot(c);
			boolean inside = false;
			if (stacks) {
				for (int i = 0; i < 12; i++) {
					if (c.gambleItems[i] == item) {
						toSlot = i;
						inside = true;
						break;
					}
				}
			}
			if (toSlot == -1) {
				c.sendMessage("You can't offer any more items currently.");
				return;
			}
			int amountPlayerHas = ItemAssistant.getItemAmount(c, item);
			if (amountPlayerHas < amount) {
				amount = amountPlayerHas;
			}
			int freeSlots = 0;
			for (int i = 0; i < c.gambleItems.length; i++) {
				if (c.gambleItems[i] <= 0) {
					freeSlots++;
				}
			}
			if (!stacks && amount > freeSlots) {
				amount = freeSlots;
			}
			if (stacks) {
				if (inside) {
					BigInteger f = new BigInteger(""+amount+"");
					BigInteger g = new BigInteger(""+c.gambleAmounts[toSlot]+"");
					BigInteger m = new BigInteger(""+Integer.MAX_VALUE+"");
					if (g.add(f).compareTo(m) > 0) {
						amount = Integer.MAX_VALUE - c.gambleAmounts[toSlot];
					}
					c.gambleAmounts[toSlot] += amount;
				} else {
					c.gambleItems[toSlot] = item;
					c.gambleAmounts[toSlot] = amount;
				}
			} else {
				for (int i = 0; i < amount; i++) {
					toSlot = getFreeSlot(c);
					if (toSlot != -1) {
						c.gambleItems[toSlot] = item;
						c.gambleAmounts[toSlot] = 1;
					}
				}
			}
			ItemAssistant.deleteItemForBank(c, item, amount);
			c.gamble.somethingChanged();
			c.gamble.updateBets();
		}
	}
	
	public static int getFreeSlot(Player c) {
		for (int i = 0; i < 12; i++) {
			if (c.gambleItems[i] <= 0) {
				return i;
			}
		}
		return -1;
	}

	public static void remove(Player c, int item, int slot, int amount) {
		if (c.gamble == null || item <= 0 || (c.gamble.p1Accepted && c.gamble.p2Accepted)) {
			return;
		}
		if (c.gambleItems[slot] == item && c.gambleAmounts[slot] > 0) {
			boolean stacks = false;
			if (ItemData.data[item].stackable || ItemData.data[item].isNoted()) {
				stacks = true;
			}
			if (stacks) {
				if (amount > c.gambleAmounts[slot]) {
					amount = c.gambleAmounts[slot];
					c.gambleItems[slot] = 0;
				}
				c.gambleAmounts[slot] -= amount;
				ItemAssistant.addItem(c, item, amount);
			} else {
				if (amount > 12) {
					amount = 12;
				}
				for (int i = 0; i < 12; i++) {
					if (c.gambleItems[i] == item && amount > 0) {
						amount --;
						ItemAssistant.addItem(c, item, 1);
						c.gambleItems[i] = 0;
						c.gambleAmounts[i] = 0;
					}
				}
			}
			c.gamble.somethingChanged();
			c.gamble.updateBets();
		} else if (c.gambleAmounts[slot] <= 0) {
			c.gambleItems[slot] = 0;
			c.gamble.somethingChanged();
			c.gamble.updateBets();
		}
	}

	public static void acceptReq(Player c, Player o) {
		if (c == o) {
			c.getPA().removeAllWindows();
			c.sendMessage("You can't gamble against yourself.");
			return;
		}
		boolean close = c.goodDistance(o.getX(), o.getY(), c.getX(), c.getY(), 5);
		if (!close) {
			c.getPA().removeAllWindows();
			c.sendMessage("You must be closer to that player accept their request");
			return;
		}
		if (c != null && o != null) {
			Gamble game = new Gamble(o, c);
			c.gamble = game;
			o.gamble = game;
			game.open();
		}
	}

	public static void sendReq(final Player c, final Player o) {
		if (o.gamble != null) {
			c.sendMessage("Other player is busy at the moment.");
			return;
		}
		boolean close = c.goodDistance(o.getX(), o.getY(), c.getX(), c.getY(), 5);
		if (!close) {
			c.sendMessage("You must be closer to that player to send a request");
			return;
		}
		c.faceUpdate(o.playerId);
		c.sendMessage("Sending gamble request...");
		Dialogues.send(o, new DialogueList(new Dialogue[] {
				new Dialogue(DialogueType.OPTIONS, new String[] {
						"-"+c.playerName+" wishes to gamble with you-", "Accept", "Decline"},
						new Dialogue.Options() {@Override
					public void click(Player p, int option) {
							switch(option) {
							case 2: acceptReq(o, c); break;
							case 3: c.getPA().removeAllWindows(); break;
							}}})}));
	}



}
