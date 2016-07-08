package ionic.player.banking;

import ionic.item.Item;
import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Client;

/**
 * @author Keith
 * Handles depositing items into your bank
 */

public class BankDepositing {

	public static void handle(Client c, int invSlot, int item, int amount, boolean allin) {
		if (!ItemAssistant.playerHasItem(c, item)) { return; }
		if (!((c.playerItems[invSlot] - 1) == item)) { return; }
		Bank bank = c.getBank();
		int itemToDisplay = item;
		int charges = c.playerItemsC[invSlot];
		if (ItemData.data[itemToDisplay].getUnnoted() > 0) {
			itemToDisplay = ItemData.data[itemToDisplay].getUnnoted();
		}
		int tabSelected = bank.tabSelected;
		int[] bankSlot = getSlot(c, itemToDisplay);
		boolean itemIn = bankSlot[2] == 1;
		if (!itemIn) {
			if (BankHandler.getTabItemCount(c, tabSelected) >= BankConstants.MAX_ITEMS_PER_TAB) {
				c.sendMessage("This tab has 150 items inside it and cannot hold more items.");
				return;
			}
			if (BankHandler.getTotalItemCount(c) >= bank.getMaxBankItems()) {
				c.sendMessage("Your bank cannot hold any more items, you may upgrade your membership to gain more slots");
				return;
			}
		}
		int itemsInvAmountAvailable = ItemAssistant.getItemAmount(c, item);
		if (amount > itemsInvAmountAvailable) { amount = itemsInvAmountAvailable; }

		if (bankSlot[0] >= 0 && bankSlot[1] >= 0) {
			if (itemIn) {
				if ((bank.bankAmounts[bankSlot[0]][bankSlot[1]]) == Integer.MAX_VALUE) {
					c.sendMessage("You have too much of this item in your bank.");
					return;
				}
				if ((bank.bankAmounts[bankSlot[0]][bankSlot[1]] + amount) > Integer.MAX_VALUE 
						|| (bank.bankAmounts[bankSlot[0]][bankSlot[1]] + amount) < 0) {
					amount = Integer.MAX_VALUE - bank.bankAmounts[bankSlot[0]][bankSlot[1]];
					c.sendMessage("Couldn't deposit all items");
				}
				if (bank.tabSelected != 0  && !allin) {
					c.sendMessage(":setbanktab: ="+bankSlot[0]);
				}
				if (!allin)
					ItemAssistant.deleteItemForBank(c, item, amount);
				else
					ItemAssistant.deleteItemForBank2(c, item, amount);
				bank.bankAmounts[bankSlot[0]][bankSlot[1]] += amount;
			} else if (!itemIn) {
				if (!allin)
					ItemAssistant.deleteItemForBank(c, item, amount);
				else
					ItemAssistant.deleteItemForBank2(c, item, amount);
				bank.bankItems[bankSlot[0]][bankSlot[1]] = itemToDisplay;
				bank.bankAmounts[bankSlot[0]][bankSlot[1]] = amount;
				bank.bankCharges[bankSlot[0]][bankSlot[1]] = charges;
			}
		}
		if (!allin) {
			BankHandler.refreshBankSlot(c, bankSlot[0], bankSlot[1]);
			BankHandler.sendUpdateRequest(c);
			BankHandler.updateSpaces(c);
		}
	}

	public static int[] getSlot(Client c, int item) {
		int[] slot = { -1, -1, -1 };
		if (!ItemData.data[item].degraded) {
			for (int i = 0; i < BankConstants.TOTAL_TABS; i++) {
				for (int j = 0; j < BankConstants.MAX_ITEMS_PER_TAB; j++) {
					if (c.getBank().bankItems[i][j] == item) {
						return new int[] {i, j, 1};
					}
				}
			}
		}
		for (int i = 0; i < BankConstants.MAX_ITEMS_PER_TAB; i++) {
			if (c.getBank().bankItems[c.getBank().tabSelected][i] <= 0) {
				return new int[] {c.getBank().tabSelected, i, 0};
			}
		}
		return slot;
	}




}
