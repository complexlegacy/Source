package ionic.player.banking;

import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Client;

import java.math.BigInteger;

import core.Constants;

/**
 * @author Keith
 */

public class BankHandler {

	/**
	 * Opens the bank for a player
	 */
	public static void openBank(Client c) {
		if (c.getBank().searching) {
			c.getBank().searching = false;
			searchOff(c);
			refreshBank(c);
		}
		c.sendMessage(c.getBank().note ? ":noteon:" : ":noteoff:");
		selectTab(c, c.getBank().tabSelected);
		updateTabNames(c);
		sendUpdateRequest(c);
		c.getPA().showInterface(45000);
		c.getPA().sendFrame126(""+c.getBank().getMaxBankItems()+"", 45012);
		updateSpaces(c);
	}

	public static void sendUpdateRequest(Client c) {
		updateTop(c);
		c.sendMessage(":updatebank:");
	}

	public static void updateSpaces(Client c) {
		int total = getTotalItemCount(c);
		c.getPA().sendFrame126(""+total+"", 45011);
	}
	public static void updateTop(Client c) {
		for(int i = 0; i < 8; i++) {
			c.getPA().sendFrame34a(45030, c.getBank().bankItems[i+1][0] > 0 ? c.getBank().bankItems[i+1][0] : -1, i, c.getBank().bankAmounts[i+1][0]);
		}
	}

	public static void sendSearch(Client c, String search) {
		Bank b = c.getBank();
		if (!b.searching) {
			return;
		}
		b.lastSearch = search;
		for (int i = 0; i < BankConstants.TOTAL_TABS; i++) {
			for (int j = 0; j < BankConstants.MAX_ITEMS_PER_TAB; j++) {
				b.searchItems[i][j] = -1;
				b.searchAmounts[i][j] = 0;
				b.searchCharges[i][j] = -1;
			}
		}
		search = search.toLowerCase();
		for (int i = 0; i < BankConstants.TOTAL_TABS; i++) {
			int results = 0;
			for (int j = 0; j < BankConstants.MAX_ITEMS_PER_TAB; j++) {
				if (b.bankItems[i][j] > 0) {
					if (ItemData.data[b.bankItems[i][j]] != null) {
						if (ItemData.data[b.bankItems[i][j]].getName().toLowerCase().contains(search)) {
							b.searchItems[i][results] = b.bankItems[i][j];
							b.searchAmounts[i][results] = b.bankAmounts[i][j];
							b.searchCharges[i][results] = b.bankCharges[i][j];
							results++;
						}
					}
				}
			}
		}
		refreshBank(c);
	}


	/**
	 * Refreshes the items in all of the player's tabs
	 */
	public static void refreshBank(Client c) {
		for (int i = 0; i < BankConstants.TOTAL_TABS; i++) {
			refreshTabItems(c, i);
		}
		sendUpdateRequest(c);
	}


	public static boolean forceItemAdd(Client c, int item, int amount, int charges) {
		Bank b = c.getBank();
		int slot = getFreeSlot(c, b.tabSelected);
		int tab = b.tabSelected;
		boolean in = false;
		if (!ItemData.data[item].degraded) {
			for (int i = 0; i < BankConstants.TOTAL_TABS; i++) {
				for (int j = 0; j < BankConstants.MAX_ITEMS_PER_TAB; j++) {
					if (c.getBank().bankItems[i][j] == item) {
						in = true;
						slot = j;
						tab = i;
					}
				}
			}
		}
		if (!in) {
			if (slot != -1) {
				b.bankItems[tab][slot] = item;
				b.bankAmounts[tab][slot] = amount;
				b.bankCharges[tab][slot] = charges;
				return true;
			}
		} else {
			b.bankAmounts[tab][slot] += amount;
			return true;
		}
		return false;
	}


	public static void updateTabNames(Client c) {
		String k = "";
		for (int i = 0; i < 8; i++) {
			k += ""+c.tabNames[i]+"`";
		}
		c.getPA().sendFrame126(k, 45000);
	}


	/**
	 * Refreshes one slot of one tab in a players bank
	 */
	public static void refreshBankSlot(Client c, int tab, int slot) {
		if (c.getBank().bankItems[tab][slot] > 0) {
			c.getPA().sendFrame34a(45100 + tab, c.getBank().bankItems[tab][slot], slot, c.getBank().bankAmounts[tab][slot]);
		} else {
			c.getPA().sendFrame34a(45100 + tab, -1, slot, 1);
		}
	}

	/**
	 * Counts the tabs which have items in them
	 */
	public static int countTabs(Client c) {
		int p = 0;
		for (int i = 0; i < BankConstants.TOTAL_TABS; i++) {
			if (c.getBank().bankItems[i][0] > 0) {
				p++;
			}
		}
		return p;
	}


	public static void rearrangeBank(Client c) {
		for (int i = 0; i < 9; i++) {
			rearrangeTab(c,i);
		}
	}

	/**
	 * Handles rearranging items, if there are empty slots
	 */
	public static void rearrangeTab(Client c, int tab) {
		Bank b = c.getBank();
		int highest = 0;
		int lowest = 0;
		for (int i = 0; i < BankConstants.MAX_ITEMS_PER_TAB; i++) {
			if (b.bankItems[tab][i] > 0) {
				highest = i;
			}
		}
		for (int i = 0; i < BankConstants.MAX_ITEMS_PER_TAB; i++) {
			if (b.bankItems[tab][i] <= 0) {
				lowest = i;
				break;
			}
		}
		for (int i = lowest; i < highest; i++) {
			b.bankItems[tab][i] = b.bankItems[tab][i + 1];
			b.bankAmounts[tab][i] = b.bankAmounts[tab][i + 1];
			b.bankCharges[tab][i] = b.bankCharges[tab][i + 1];
			b.bankItems[tab][i + 1] = -1;
			b.bankAmounts[tab][i + 1] = 1;
			b.bankCharges[tab][i + 1] = -1;
		}
		if (!b.searching) {
			refreshTabItems(c, tab);
		}
	}

	public static void rearrangeTabItems2(Client c, int tab) {
		Bank b = c.getBank();
		int highest = 0;
		int lowest = 0;
		for (int i = 0; i < BankConstants.MAX_ITEMS_PER_TAB; i++) {
			if (b.bankItems[tab][i] > 0) {
				highest = i;
			}
		}
		for (int i = 0; i < BankConstants.MAX_ITEMS_PER_TAB; i++) {
			if (b.bankItems[tab][i] <= 0) {
				lowest = i;
				break;
			}
		}
		for (int i = lowest; i < highest; i++) {
			b.bankItems[tab][i] = b.bankItems[tab][i + 1];
			b.bankAmounts[tab][i] = b.bankAmounts[tab][i + 1];
			b.bankCharges[tab][i] = b.bankCharges[tab][i + 1];
			b.bankItems[tab][i + 1] = -1;
			b.bankAmounts[tab][i + 1] = 1;
			b.bankCharges[tab][i + 1] = -1;
		}
	}



	/**
	 * Refreshes the items on one specific tab
	 */
	public static void refreshTabItems(Client c, int tab) {
		if (c.getBank().searching) {
			ItemAssistant.itemsOnInterface(c, 45100 + tab, c.getBank().searchItems[tab], c.getBank().searchAmounts[tab]);
		} else {
			ItemAssistant.itemsOnInterface(c, 45100 + tab, c.getBank().bankItems[tab], c.getBank().bankAmounts[tab]);
		}
	}


	/**
	 * Gets the count of items in a specific tab
	 */
	public static int getTabItemCount(Client c, int tab) {
		int count = 0;
		for (int i = 0; i < c.getBank().bankItems[tab].length; i++) {
			if (c.getBank().bankItems[tab][i] > 0) {
				count++;
			}
		}
		return count;
	}
	/**
	 * Gets the count of items in all tabs together
	 */
	public static int getTotalItemCount(Client c) {
		int count = 0;
		for (int i = 0; i < BankConstants.TOTAL_TABS; i++) {
			count += getTabItemCount(c, i);
		}
		return count;
	}

	/**
	 * Rearranges the tabs
	 */
	public static void rearrangeTabs(Client c) {
		for (int i = 0; i < 9; i++) {
			int amt = getTabItemCount(c, i);
			if (amt <= 0) {
				handleCollapsing(c, i);
			}
		}
		refreshBank(c);
		sendUpdateRequest(c);
	}

	public static void moveDown(Client c, int k, int j) {
		Bank b = c.getBank();
		c.tabNames[k-1] = c.tabNames[j-1];
		for (int i = 0; i < BankConstants.MAX_ITEMS_PER_TAB; i++) {
			b.bankItems[k][i] = b.bankItems[j][i];
			b.bankAmounts[k][i] = b.bankAmounts[j][i];
			b.bankCharges[k][i] = b.bankCharges[j][i];
			b.bankItems[j][i] = 0;
			b.bankAmounts[j][i] = 0;
			b.bankCharges[j][i] = -1;
		}
	}
	public static void handleCollapsing(Client c, int tab) {
		if (tab == 1) {
			moveDown(c, 1, 2); moveDown(c, 2, 3); moveDown(c, 3, 4); moveDown(c, 4, 5); 
			moveDown(c, 5, 6); moveDown(c, 6, 7); moveDown(c, 7, 8);
		} else if (tab == 2) {
			moveDown(c, 2, 3); moveDown(c, 3, 4); moveDown(c, 4, 5);
			moveDown(c, 5, 6); moveDown(c, 6, 7); moveDown(c, 7, 8);
		} else if (tab == 3) {
			moveDown(c, 3, 4); moveDown(c, 4, 5); moveDown(c, 5, 6);
			moveDown(c, 6, 7); moveDown(c, 7, 8);
		} else if (tab == 4) {
			moveDown(c, 4, 5); moveDown(c, 5, 6);
			moveDown(c, 6, 7); moveDown(c, 7, 8);
		} else if (tab == 5) {
			moveDown(c, 5, 6); moveDown(c, 6, 7); moveDown(c, 7, 8);
		} else if (tab == 6) {
			moveDown(c, 6, 7); moveDown(c, 7, 8);
		} else if (tab == 7) {
			moveDown(c, 7, 8);
		}
		if (c.getBank().tabSelected == tab && c.getBank().tabSelected != 0) {
			selectTab(c, tab - 1);
		}
		for (int i = 0; i < 8; i++) {
			if (c.tabNames[i].startsWith("Tab ") && c.tabNames[i].length() == 5) {
				c.tabNames[i] = "Tab "+(i + 1);
			}
		}
		sendUpdateRequest(c);
		updateTabNames(c);
	}

	public static String insertCommas(String str) {
		if(str.length() < 4){
			return str;
		}
		return insertCommas(str.substring(0, str.length() - 3)) +  "," + str.substring(str.length() - 3, str.length());
	}


	/**
	 * Handles clicking on 'value' when right clicking a tab
	 */
	public static void valueTab(Client c, int tab) {
		BigInteger value = null;
		if (tab != 0) {
			value = getTabValue(c, tab);
			c.sendMessage("The value of tab "+tab+" is: <col=255>"+insertCommas(""+value+"")+"</col> Coins");
		} else {
			value = new BigInteger("0");
			for (int i = 0; i < c.getBank().bankItems.length; i++) {
				value = value.add(getTabValue(c, i));
			}
			c.sendMessage("The total value of your bank is: <col=255>"+insertCommas(""+value+"")+"</col> Coins");
		}
	}
	public static BigInteger getBankValue(Client c) {
		BigInteger value = new BigInteger("0");
		for (int i = 0; i < c.getBank().bankItems.length; i++) {
			value = value.add(getTabValue(c, i));
		}
		return value;
	}
	public static BigInteger getTabValue(Client c, int tab) {
		BigInteger value = new BigInteger("0");
		for (int i = 0; i < c.getBank().bankItems[tab].length; i++) {
			BigInteger f = new BigInteger(""+ItemAssistant.getItemShopValue(c.getBank().bankItems[tab][i])+"");
			BigInteger b = new BigInteger(""+c.getBank().bankAmounts[tab][i]+"");
			value = value.add(new BigInteger(""+f.multiply(b)));
		}
		return value;
	}


	/**
	 * Handles collapsing a tab
	 */
	public static void collapseTab(Client c, int tab) {
		Bank b = c.getBank();
		if (getTabItemCount(c, 0) + getTabItemCount(c, tab) > BankConstants.MAX_ITEMS_PER_TAB) {
			c.sendMessage("Your bank doesn't have enough room to collapse this tab");
			return;
		}
		b.collapsing = true;
		for (int i = 0; i < BankConstants.MAX_ITEMS_PER_TAB; i++) {
			int slot = getFreeSlot(c, 0);
			if (slot != -1) {
				b.bankItems[0][slot] = b.bankItems[tab][i];
				b.bankAmounts[0][slot] = b.bankAmounts[tab][i];
				b.bankCharges[0][slot] = b.bankCharges[tab][i];
				b.bankItems[tab][i] = -1;
				b.bankAmounts[tab][i] = 1;
				b.bankCharges[tab][i] = -1;
			}
		}
		b.collapsing = false;
		rearrangeTabs(c);
	}

	/**
	 * gets a free slot in a tab
	 */
	public static int getFreeSlot(Client c, int tab) {
		for (int i = 0; i < BankConstants.MAX_ITEMS_PER_TAB; i++) {
			if (c.getBank().bankItems[tab][i] <=  0) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Handles selecting a tab
	 */
	public static void selectTab(Client c, int tab) {
		c.getBank().tabSelected = tab;
		c.sendMessage(":setbanktab: ="+c.getBank().tabSelected);
	}


	/**
	 * Handles clicking on buttons on the bank interface
	 */
	public static void clickBankButtons(Client c, int button) {
		switch(button) {
		case 175221: selectTab(c, 0); return;
		case 175222: selectTab(c, 1); return;
		case 175223: selectTab(c, 2); return;
		case 175224: selectTab(c, 3); return;
		case 175225: selectTab(c, 4); return;
		case 175226: selectTab(c, 5); return;
		case 175227: selectTab(c, 6); return;
		case 175228: selectTab(c, 7); return;
		case 175229: selectTab(c, 8); return;
		case 177009: valueTab(c, 0); return;
		case 177010: valueTab(c, 1); return;
		case 177011: valueTab(c, 2); return;
		case 177012: valueTab(c, 3); return;
		case 177013: valueTab(c, 4); return;
		case 177014: valueTab(c, 5); return;
		case 177015: valueTab(c, 6); return;
		case 177016: valueTab(c, 7); return;
		case 177017: valueTab(c, 8); return;
		case 176166: searchOff(c); collapseTab(c, 1); return;
		case 176167: searchOff(c); collapseTab(c, 2); return;
		case 176168: searchOff(c); collapseTab(c, 3); return;
		case 176169: searchOff(c); collapseTab(c, 4); return;
		case 176170: searchOff(c); collapseTab(c, 5); return;
		case 176171: searchOff(c); collapseTab(c, 6); return;
		case 176172: searchOff(c); collapseTab(c, 7); return;
		case 176173: searchOff(c); collapseTab(c, 8); return;
		case 175218: c.getBank().note = true; return;
		case 176190: c.getBank().note = false; return;
		case 175205: searchOff(c); bankInv(c); return;
		case 175208: searchOff(c); bankEquipment(c); return;
		case 176184: searchOff(c); bankMoneyPouch(c); refreshBank(c); return;
		case 177123: c.getBank().insert = true; return;
		case 177126: c.getBank().insert = false; return;
		case 177129: c.getBank().searching = true; return;
		case 177132: c.getBank().searching = false; rearrangeBank(c); rearrangeTabs(c); refreshBank(c); return;
		}
	}

	public static void searchOff(Client c) {
		c.sendMessage(":searchOff:");
	}

	public static void bankMoneyPouch(Client c) {
		BigInteger max = new BigInteger(""+Integer.MAX_VALUE);
		int tab = -1;
		int slot = -1;
		int in = 0;
		boolean broke = false;
		for (int j = 0; j < 9; j++) {
			for (int i = 0; i < BankConstants.MAX_ITEMS_PER_TAB; i++) {
				if (c.getBank().bankItems[j][i] == 995) {
					slot = i;
					tab = j;
					in = c.getBank().bankAmounts[j][i];
					broke = true;
					break;
				}
			}
			if (broke) { break; }
		}
		if (tab == -1) {
			tab = c.getBank().tabSelected;
			slot = getFreeSlot(c, tab);
		}
		if (slot == -1) {
			c.sendMessage("This tab is full");
			return;
		}
		if (getTotalItemCount(c) >= c.getBank().getMaxBankItems() && in == 0) {
			c.sendMessage("Your bank is full");
			return;
		}
		BigInteger amt = new BigInteger("0");
		if (c.pouchCoins.compareTo(max) < 0) {
			amt = c.pouchCoins;
		} else {
			amt = max;
		}
		if (in > 0) {
			BigInteger inside = new BigInteger(""+in);
			if (amt.add(inside).compareTo(max) > 0) {
				amt = max.subtract(inside);
			}
			int add = Integer.parseInt(""+amt);
			c.getBank().bankAmounts[tab][slot] += add;
			c.pouchCoins = c.pouchCoins.subtract(amt);
			c.getPouch().updatePouch();
			c.sendMessage("You deposit a total of "+insertCommas(""+add)+" coins from your money pouch into your bank");
			return;
		}
		int add = Integer.parseInt(""+amt);
		c.getBank().bankItems[tab][slot] = 995;
		c.getBank().bankAmounts[tab][slot] = add;
		c.pouchCoins = c.pouchCoins.subtract(amt);
		c.getPouch().updatePouch();
		c.sendMessage("You deposit a total of "+insertCommas(""+add)+" coins from your money pouch into your bank");
	}

	public static void bankEquipment(Client c) {
		boolean deposit = false;
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] > 0) {
				deposit = true;
			}
		}
		if (!deposit) {
			c.sendMessage("You don't have any worn items to deposit.");
			return;
		}
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] > 0 && c.playerEquipmentN[i] > 0) {
				if (!BankHandler.forceItemAdd(c, c.playerEquipment[i], c.playerEquipmentN[i], c.playerEquipmentC[i])) {
					c.sendMessage("This tab is currently full.");
					break;
				}
			}
			ItemAssistant.replaceEquipmentSlot(c, i, -1);
		}
		BankHandler.refreshBank(c);
		ItemAssistant.updateCombatInterface(c, c.playerEquipment[Constants.WEAPON_SLOT], ItemAssistant.getItemName(c.playerEquipment[Constants.WEAPON_SLOT]));
		c.getCombat().getPlayerAnimIndex(ItemAssistant.getItemName(c.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
		c.sendMessage("You deposit all your worn equipment.");
	}


	public static void bankInv(Client c) {
		for (int i = 0; i < c.playerItems.length; i++) {
			if (c.playerItems[i] > 0) {
				BankDepositing.handle(c, i, c.playerItems[i] - 1, c.playerItemsN[i], true);
			}
		}
		ItemAssistant.updateInventory(c);
		refreshTabItems(c, c.getBank().tabSelected);
		sendUpdateRequest(c);
		updateSpaces(c);
	}


}
