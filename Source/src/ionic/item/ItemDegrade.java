package ionic.item;

import core.Constants;
import ionic.player.Player;
import ionic.player.banking.BankHandler;

public class ItemDegrade {
	
	public static void doDegrade(Player c, int degradeType) {
		boolean changed = false;
		for (int i = 0; i < c.playerEquipment.length; i++) {
			if (c.playerEquipment[i] > 0) {
				ItemData d = ItemData.data[c.playerEquipment[i]];
				if (d.degradable && d.degradeType == degradeType) {
					if (c.playerEquipment[i] != d.degradedId || c.playerEquipmentC[i] == d.maxCharges) {
						c.playerEquipment[i] = d.degradedId;
						c.sendMessage("Your "+d.getName()+" has degraded slightly.");
						changed = true;
					}
					c.playerEquipmentC[i] --;
					if (c.playerEquipmentC[i] <= 0 && d.degradable && d.degraded) {
						changed = true;
						c.sendMessage("Your "+d.getName()+" has degraded completely.");
						c.playerEquipment[i] = -1;
						c.playerEquipmentN[i] = 0;
						ItemAssistant.updateEquipmentInterface(c);
						ItemAssistant.updateCombatInterface(c, c.playerEquipment[Constants.WEAPON_SLOT], ItemAssistant.getItemName(c.playerEquipment[Constants.WEAPON_SLOT]));
						c.getCombat().getPlayerAnimIndex(ItemAssistant.getItemName(c.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
						c.updateRequired = true;
    					c.setAppearanceUpdateRequired(true);
						if (d.broken > 0)
							ItemAssistant.addItem(c, d.broken, 1);
					}
				}
			}
		}
		if (changed) {
			ItemAssistant.updateEquipmentInterface(c);
		}
	}
	
	public static void checkCharges(Player c, int item, int slot, boolean equipment) {
		int charges = equipment ? c.playerEquipmentC[slot] : c.playerItemsC[slot];
		int max = ItemData.data[item].maxCharges;
		c.sendMessage("Your "+ItemData.data[item].getName()+" has "+(int)((double)charges/max * 100)+"% of its charges left. ("+charges+"/"+max+")");
	}
	
	public static void openRepair(Player c) {
		c.repairSlot = 0;
		c.getPA().sendFrame126("@or2@Current charges: @gre@0", 37304);
		c.getPA().sendFrame126("@or2@Maximum charges: @gre@0", 37305);
		c.getPA().sendFrame126("@or2@Full repair cost: @gre@0", 37306);
		c.getPA().sendFrame34a(37303, -1, 0, 1);
		c.getPA().showInterface(37300);
	}
	
	public static void addToRepair(Player c, int item, int slot) {
		if (c.playerItems[slot] - 1 != item) {
			return;
		}
		if (ItemData.data[item].repairCost > 0) {
			int charges = c.playerItemsC[slot];
			if (charges < 0) {
				c.playerItemsC[slot] = 0;
				charges = 0;
			}
			int max = ItemData.data[item].maxCharges;
			int left = max - charges;
			if (charges >= max) {
				c.sendMessage("This item is already fully repaired.");
				return;
			}
			c.getPA().sendFrame34a(37303, item, 0, 1);
			c.getPA().sendFrame126("@or2@Current charges: @gre@"+charges, 37304);
			c.getPA().sendFrame126("@or2@Maximum charges: @gre@"+max, 37305);
			String cost = BankHandler.insertCommas(""+(ItemData.data[item].repairCost * left));
			c.getPA().sendFrame126("@or2@Full repair cost: @gre@"+cost+" coins", 37306);
			c.repairSlot = slot;
		} else {
			c.sendMessage("This item can't be repaired.");
		}
	}
	
	public static void repair(Player c) {
		int slot = c.repairSlot;
		int item = c.playerItems[slot] - 1;
		int costPer = ItemData.data[item].repairCost;
		if (item > 0) {
			if ((ItemData.data[item].degradable || ItemData.data[item].broken == item) && costPer > 0) {
				int charges = c.playerItemsC[slot];
				int max = ItemData.data[item].maxCharges;
				int left = max - charges;
				int cost = costPer * left;
				if (ItemAssistant.playerHasItem(c, 995, cost)) {
					ItemAssistant.deleteItemForBank(c, 995, cost);
					addRepair(c, slot, max);
					c.sendMessage("You have fully repaired your "+ItemData.data[item].getName());
				} else {
					int coins = ItemAssistant.getItemAmount(c, 995);
					if (coins >= costPer) {
						int repairAmt = coins / costPer;
						ItemAssistant.deleteItemForBank(c, 995, coins);
						addRepair(c, slot, repairAmt);
						c.sendMessage("You pay Bob to add "+repairAmt+" charges to your item.");
					} else {
						c.sendMessage("You can't afford to add more charges to this item.");
					}
				}
			}
		}
	}
	
	public static void addRepair(Player c, int slot, int repair) {
		c.playerItemsC[slot] += repair;
		int item = c.playerItems[slot] - 1;
		if (c.playerItemsC[slot] >= ItemData.data[item].maxCharges) {
			c.playerItemsC[slot] = ItemData.data[item].maxCharges;
			c.playerItems[slot] = ItemData.data[item].fullyCharged + 1;
			ItemAssistant.updateInventory(c);
		}
		item = c.playerItems[slot] - 1;
		if (ItemData.data[item].broken == item) {
			c.playerItems[slot] = ItemData.data[item].degradedId + 1;
			ItemAssistant.updateInventory(c);
		}
		openRepair(c);
	}
	
	

}
