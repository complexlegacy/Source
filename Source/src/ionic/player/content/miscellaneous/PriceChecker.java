package ionic.player.content.miscellaneous;

import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Player;
import ionic.player.banking.BankHandler;

import java.math.BigInteger;

public class PriceChecker {

	public static void add(Player c, int item, int slot, int amount) {
		if (item <= 0) { 
			return;
		}
		if (ItemData.data[item].isUntradable()) {
			c.sendMessage("This item is untradable and cannot be valued in the price checker.");
			return;
		}
		if (ItemAssistant.playerHasItem(c, item)) {
			if (c.playerItems[slot] == item + 1) {
				int toSlot = -1;
				for (int i = 0; i < 30; i++) {
					if (c.checkerItems[i] <= 0) {
						toSlot = i;
						break;
					}
				}
				if (toSlot == -1) {
					c.sendMessage("Price checker is currently full!");
					return;
				}
				boolean found = false;
				for (int i = 0; i < 30; i++) {
					if (c.checkerItems[i] == item) {
						toSlot = i;
						found = true;
					}
				}
				if (amount > ItemAssistant.getItemAmount(c, item)) {
					amount = ItemAssistant.getItemAmount(c, item);
				}
				if (found == true) {
					if (c.checkerAmounts[toSlot] + amount < 0) {
						amount = Integer.MAX_VALUE - c.checkerAmounts[toSlot];
					}
				}
				if (!ItemAssistant.playerHasItem(c, item, amount)) {
					return;
				}
				ItemAssistant.deleteItemForBank(c, item, amount);
				if (found) {
					c.checkerAmounts[toSlot] += amount;
				} else {
					c.checkerItems[toSlot] = item;
					c.checkerAmounts[toSlot] = amount;
				}
				updateSlot(c, toSlot);
			}
		}
		refreshTotalValue(c);
	}

	public static void remove(Player c, int item, int slot, int amount) {
		if (item > 0) {
			if (c.checkerItems[slot] == item) {
				if (amount > c.checkerAmounts[slot]) {
					amount = c.checkerAmounts[slot];
				}
				c.checkerAmounts[slot] -= amount;
				if (c.checkerAmounts[slot] <= 0) {
					c.checkerItems[slot] = -1;
					c.checkerAmounts[slot] = 0;
				}
				ItemAssistant.addItem(c, item, amount);
				updateSlot(c, slot);
			}
			refreshTotalValue(c);
		}
	}

	public static void open(Player c) {
		refreshTotalValue(c);
		for (int i = 0; i < 30; i++) {
			updateSlot(c, i);
		}
		c.getPA().showInterface(37100);
	}

	public static void updateSlot(Player c, int slot) {
		int item = c.checkerItems[slot];
		int amount = c.checkerAmounts[slot];
		String p = "";
		if (item > 0) {
			BigInteger itemValue = new BigInteger(""+ItemData.data[item].shopValue+"");
			BigInteger itemAmount = new BigInteger(""+amount+"");
			BigInteger totalPrice = itemValue.multiply(itemAmount);
			BigInteger m1 = new BigInteger("100000000");
			BigInteger m = new BigInteger("1000000");
			if (amount > 1) {
				if (itemAmount.compareTo(m1) >= 0) {
					p += "@gre@"+itemAmount.divide(m)+"M@whi@";
				} else {
					p += ""+commas(""+itemAmount);
				}
				p += " x ";
				if (itemValue.compareTo(m1) >= 0) {
					p += "@gre@"+itemValue.divide(m)+"M@whi@";
				} else {
					p += ""+commas(""+itemValue);
				}
				p += "\\n= ";
				if (totalPrice.compareTo(m1) >= 0) {
					p += "@gre@"+totalPrice.divide(m)+"M@whi@";
				} else {
					p += ""+commas(""+totalPrice);
				}
			} else {
				if (totalPrice.compareTo(m1) >= 0) {
					p = "@gre@"+totalPrice.divide(m)+"M";
				} else {
					p = ""+commas(""+totalPrice);
				}
			}
		}
		if (item <= 0) {
			amount = 1;
			item = -1;
			p = "";
		}
		c.getPA().sendFrame34a(37112, item, slot, amount);
		c.getPA().sendFrame126(p, 37115+slot);
	}

	public static void refreshTotalValue(Player c) {
		BigInteger d = new BigInteger("0");
		for (int i = 0; i < 30; i++) {
			if (c.checkerItems[i] > 0) {
				BigInteger b = new BigInteger(""+ItemData.data[c.checkerItems[i]].shopValue);
				BigInteger s = new BigInteger(""+c.checkerAmounts[i]+"");
				d = d.add(b.multiply(s));
			}
		}
		c.getPA().sendFrame126(""+commas(""+d), 37111);
	}

	public static String commas(String s) {
		return BankHandler.insertCommas(s);
	}

	public static void close(Player c) {
		for (int i = 0; i < 30; i++) {
			remove(c, c.checkerItems[i], i, c.checkerAmounts[i]);
		}
	}


}
