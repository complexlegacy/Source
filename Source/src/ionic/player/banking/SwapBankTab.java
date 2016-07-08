package ionic.player.banking;

import ionic.player.Client;


/**
 * @author Keith
 * Handles changing the bank tab of a bank item
 */
public class SwapBankTab {
	
	public static void handle(Client c, int fromTab, int toTab, int fromSlot, int itemId) {
		Bank b = c.getBank();
		if (toTab <= BankHandler.countTabs(c)) {
			boolean newTab = b.bankItems[toTab][0] <= 0;
			if (newTab && toTab != 0) {
				c.tabNames[toTab - 1] = "Tab "+toTab+"";
				BankHandler.updateTabNames(c);
			}
			if (b.bankItems[fromTab][fromSlot] != itemId) { return; }
			int toSlot = getFreeSlot(c, toTab);
			if (toSlot == -1) { c.sendMessage("That tab is already full"); return; }
			b.bankItems[toTab][toSlot] = b.bankItems[fromTab][fromSlot];
			b.bankAmounts[toTab][toSlot] = b.bankAmounts[fromTab][fromSlot];
			b.bankCharges[toTab][toSlot] = b.bankCharges[fromTab][fromSlot];
			b.bankItems[fromTab][fromSlot] = -1;
			b.bankAmounts[fromTab][fromSlot] = 1;
			b.bankCharges[fromTab][fromSlot] = -1;
			BankHandler.refreshBankSlot(c, fromTab, fromSlot);
			BankHandler.refreshBankSlot(c, toTab, toSlot);
			BankHandler.rearrangeTab(c, fromTab);
			BankHandler.rearrangeTabs(c);
			BankHandler.sendUpdateRequest(c);
		}
	}
	
	public static int getFreeSlot(Client c, int tab) {
		for (int i = 0; i < BankConstants.MAX_ITEMS_PER_TAB; i++) {
			if (c.getBank().bankItems[tab][i] <= 0) {
				return i;
			}
		}
		return -1;
	}

}
