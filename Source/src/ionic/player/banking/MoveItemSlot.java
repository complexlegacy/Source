package ionic.player.banking;

import ionic.player.Client;

/**
 * @author Keith
 * Handles moving an item's slot to swap with another item
 */

public class MoveItemSlot {
	
	
	public static void handle(Client c, int fromTab, int toTab, int fromSlot, int toSlot, int itemId, int itemIdSwapped) {
		Bank b = c.getBank();
		if (b.insert == false) {
			int k = b.bankItems[fromTab][fromSlot];
			int f = b.bankAmounts[fromTab][fromSlot];
			int y = b.bankCharges[fromTab][fromSlot];
			int e = b.bankItems[toTab][toSlot];
			int g = b.bankAmounts[toTab][toSlot];
			int u = b.bankCharges[toTab][toSlot];
			b.bankItems[fromTab][fromSlot] = e;
			b.bankAmounts[fromTab][fromSlot] = g;
			b.bankCharges[fromTab][fromSlot] = u;
			b.bankItems[toTab][toSlot] = k;
			b.bankAmounts[toTab][toSlot] = f;
			b.bankCharges[toTab][toSlot] = y;
			if (toTab != fromTab) {
				BankHandler.refreshTabItems(c, toTab);
			}
		} else {
			if (BankHandler.getTabItemCount(c, toTab) >= BankConstants.MAX_ITEMS_PER_TAB - 1) {
				c.sendMessage("Can't insert item, tab is too full");
				return;
			}
			int k = b.bankItems[fromTab][fromSlot];
			int f = b.bankAmounts[fromTab][fromSlot];
			int j = b.bankCharges[fromTab][fromSlot];
			b.bankItems[fromTab][fromSlot] = -1;
			b.bankAmounts[fromTab][fromSlot] = 0;
			b.bankCharges[fromTab][fromSlot] = -1;
			int[] items = new int[BankConstants.MAX_ITEMS_PER_TAB];
			int[] amounts = new int[BankConstants.MAX_ITEMS_PER_TAB];
			int[] charges = new int[BankConstants.MAX_ITEMS_PER_TAB];
			for (int i = toSlot; i < BankConstants.MAX_ITEMS_PER_TAB - 1; i++) {
				items[i] = b.bankItems[toTab][i];
				amounts[i] = b.bankAmounts[toTab][i];
				charges[i] = b.bankCharges[toTab][i];
			}
			for (int i = toSlot; i < BankConstants.MAX_ITEMS_PER_TAB - 1; i++) {
				b.bankItems[toTab][i + 1] = items[i];
				b.bankAmounts[toTab][i + 1] = amounts[i];
				b.bankCharges[toTab][i + 1] = charges[i];
			}
			b.bankItems[toTab][toSlot] = k;
			b.bankAmounts[toTab][toSlot] = f;
			b.bankCharges[toTab][toSlot] = j;
			BankHandler.rearrangeTab(c, fromTab);
			BankHandler.rearrangeTab(c, toTab);
		}
		BankHandler.refreshBank(c);
		BankHandler.sendUpdateRequest(c);
	}
	
	
}
