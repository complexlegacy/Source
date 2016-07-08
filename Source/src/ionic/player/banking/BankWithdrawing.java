package ionic.player.banking;

import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Client;

/**
 * @author Keith
 * Handles withdrawing items from the bank
 */

public class BankWithdrawing {
	
	public static void handle(Client c, int tab, int slot, int item, int amount) {
		Bank bank = c.getBank();
		if (bank.collapsing == true) { return; }
		if (bank.bankItems[tab][slot] != item && !bank.searching) { return; }
		int pSlot = slot;
		if (bank.searching) {
			slot = -1;
			for (int i = 0; i < BankConstants.MAX_ITEMS_PER_TAB; i++) {
				if (bank.bankItems[tab][i] == item && bank.bankCharges[tab][i] == bank.searchCharges[tab][pSlot]) {
					slot = i;
				}
			}
		}
		int charges = bank.bankCharges[tab][slot];
		if (slot < 0) {
			return;
		}
		if (ItemAssistant.freeSlots(c) == 0) { c.sendMessage("You don't have any free inventory space slots"); return; }
		if (amount > bank.bankAmounts[tab][slot]) { amount = bank.bankAmounts[tab][slot]; }
		int itemToAdd = item;
		if (bank.note) {
			if (ItemData.data[item].getNoted() > -1) {
				itemToAdd = ItemData.data[item].getNoted();
			}
		}
		if (bank.note && ItemData.data[itemToAdd].getNoted() > -1) {
			for (int i = 0; i < c.playerItems.length; i++){
				if (c.playerItems[i] - 1 == itemToAdd) {
					if (c.playerItemsN[i] == Integer.MAX_VALUE) {
						c.sendMessage("You have the maximum amount of that item in your inventory already");
						return;
					}
					if (c.playerItemsN[i] + amount > Integer.MAX_VALUE || c.playerItemsN[i] + amount < 0) {
						amount = Integer.MAX_VALUE - c.playerItemsN[i];
						c.sendMessage("Could not withdraw all the items you requested.");
					}
				}
			}
			bank.bankAmounts[tab][slot] -= amount;
			if (bank.bankAmounts[tab][slot] <= 0) {
				bank.bankItems[tab][slot] = -1;
				bank.bankCharges[tab][slot] = -1;
			}
			ItemAssistant.addItem(c, itemToAdd, amount);
		} else {
			if (bank.note && ItemData.data[item].getNoted() == -1) {
				c.sendMessage("This item can't be withdrawn as note.");
			}
			if (ItemData.data[itemToAdd].stackable) {
				for (int i = 0; i < c.playerItems.length; i++){
					if (c.playerItems[i] - 1 == itemToAdd) {
						if (c.playerItemsN[i] == Integer.MAX_VALUE) {
							c.sendMessage("You have the maximum amount of that item in your inventory already");
							return;
						}
						if (c.playerItemsN[i] + amount > Integer.MAX_VALUE || c.playerItemsN[i] + amount < 0) {
							amount = (Integer.MAX_VALUE - c.playerItemsN[i]);
							c.sendMessage("Could not withdraw all the items you requested.");
						}
					}
				}
				bank.bankAmounts[tab][slot] -= amount;
				if (bank.bankAmounts[tab][slot] <= 0) {
					bank.bankItems[tab][slot] = -1;
					bank.bankCharges[tab][slot] = -1;
				}
				ItemAssistant.addItem(c, itemToAdd, amount);
			} else {
				int freeSlots = ItemAssistant.freeSlots(c);
				if (amount > freeSlots) {
					amount = freeSlots;
					c.sendMessage("Not enough inventory space to withdraw that many.");
				}
				bank.bankAmounts[tab][slot] -= amount;
				if (bank.bankAmounts[tab][slot] <= 0) {
					bank.bankItems[tab][slot] = -1;
					bank.bankCharges[tab][slot] = -1;
				}
				c.chargeAdd = true;
				c.chargesAdd = charges;
				ItemAssistant.addItem(c, itemToAdd, amount);
			}
		}
		BankHandler.rearrangeTab(c, tab);
		if (!bank.searching) {
			BankHandler.rearrangeTabs(c);
			BankHandler.sendUpdateRequest(c);
		} else {
			BankHandler.sendSearch(c, c.getBank().lastSearch);
		}
		BankHandler.updateSpaces(c);
	}

	
	
}
