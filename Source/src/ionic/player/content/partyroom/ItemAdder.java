package ionic.player.content.partyroom;

import ionic.item.Item;
import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Player;

public class ItemAdder {
	
	private Player c;
	public ItemAdder(Player c) {
		this.c = c;
	}
	
	
	public int[] items = new int[8];
	public int[] amounts = new int[8];
	
	public void add(int item, int slot, int amt) {
		if (ItemData.data[item].isUntradable()) {
			c.sendMessage("This item is untradable and cannot be added to the drop party chest.");
			return;
		}
		if (c.playerItems[slot] - 1 == item) {
			if (amt > c.playerItemsN[slot] && ItemData.data[item].stackable) {
				amt = c.playerItemsN[slot];
			} else if (amt > ItemAssistant.getItemAmount(c, item) && !ItemData.data[item].stackable) {
				amt = ItemAssistant.getItemAmount(c, item);
			}
			int putSlot = -1;
			boolean found = false;
			if (ItemData.data[item].stackable) {
				putSlot = getSlot(item);
				found = true;
			}
			if (putSlot == -1) {
				putSlot = getSlot();
			}
			if (putSlot == -1) {
				c.sendMessage("You can only add 8 items at a time.");
				return;
			}
			if (found) {
				int remaining = 0;
				if (amt + amounts[putSlot] < 0) {
					amt = Integer.MAX_VALUE - amounts[putSlot];
					remaining = Integer.MAX_VALUE - amt;
				}
				ItemAssistant.deleteItemForBank(c, item, amt);
				items[putSlot] = item;
				if (amounts[putSlot] > 0) {
					amounts[putSlot] += amt;
				} else {
					amounts[putSlot] = amt;
				}
				refresh();
				if (remaining > 0) {
					add(item, slot, remaining);
				}
				return;
			}
			if (!ItemData.data[item].stackable) {
				if (amt > 8) {
					amt = 8;
				}
				for (int i = 0; i < amt; i++) {
					if (ItemAssistant.playerHasItem(c, item)) {
						putSlot = getSlot();
						if (putSlot != -1) {
							ItemAssistant.deleteItem(c, item, 1);
							items[putSlot] = item;
							amounts[putSlot] = 1;
						}
					}
				}
			}
		}
		refresh();
	}
	
	
	
	public int getSlot(int item) {
		for (int i = 0; i < 8; i++) {
			if (items[i] == item && amounts[i] < Integer.MAX_VALUE) {
				return i;
			}
		}
		return -1;
	}
	public int getSlot() {
		for (int i = 0; i < 8; i++) {
			if (items[i] <= 0) {
				return i;
			}
		}
		return -1;
	}
	
	public void refresh() {
		for (int i = 0; i < 8; i++) {
			if (items[i] > 0) {
				c.getPA().sendFrame34a(27407, items[i], i, amounts[i]);
			} else {
				c.getPA().sendFrame34a(27407, -1, i, 1);
			}
		}
	}
	
	public void take(int item, int slot) {
		if (ItemAssistant.freeSlots(c) == 0) { 
			c.sendMessage("No free inventory spaces.");
			return; 
		}
		if (items[slot] == item) {
			int amount = amounts[slot];
			items[slot] = -1;
			amounts[slot] = -1;
			ItemAssistant.addItem(c, item, amount);
		}
		refresh();
	}
	
	
	

}
