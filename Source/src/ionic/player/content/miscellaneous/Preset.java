package ionic.player.content.miscellaneous;

import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.banking.Bank;
import ionic.player.banking.BankHandler;

public class Preset {

	public String name;
	public int[] equipment;
	public int[] equipmentN;
	public int[] inventory;
	public int[] inventoryN;
	public Preset(String name, int[] equipment, int[] equipmentN, int[] inventory, int[] inventoryN) {
		this.name = name;
		this.equipment = equipment;
		this.inventory = inventory;
		this.equipmentN = equipmentN;
		this.inventoryN = inventoryN;
	}

	public static void view(Player c, int preset) {
		if (c.presets[preset] == null) {
			c.presets[preset] = new Preset("Preset "+(preset + 1)+"", new int[14], new int[14], new int[28], new int[28]);
		}
		Preset p = c.presets[preset];
		c.presetSelected = preset;
		c.getPA().sendFrame126(p.name, 31005 + preset);
		c.getPA().sendFrame126(p.name, 31003);
		ItemAssistant.itemsOnInterface(c, 31030, p.inventory, p.inventoryN);
		int[] slots = {0, 1, 2, 13, 3, 4, 5, 7, 9, 10, 12};
		int item = 0;
		for (int i = 0; i < slots.length; i++) {
			item = p.equipment[slots[i]];
			if (item <= 0) 
				item = -1;
			c.getPA().sendFrame34a(31017 + i, item, 0, p.equipmentN[slots[i]]);
		}
	}

	public static void upload(Player c) {
		if (c.presets[c.presetSelected] != null) {
			for (int i = 0; i < 14; i++) {
				c.presets[c.presetSelected].equipment[i] = c.playerEquipment[i];
				c.presets[c.presetSelected].equipmentN[i] = c.playerEquipmentN[i];
			}
			for (int i = 0; i < 28; i++) {
				c.presets[c.presetSelected].inventory[i] = c.playerItems[i] - 1;
				c.presets[c.presetSelected].inventoryN[i] = c.playerItemsN[i];
			}
		}
		view(c, c.presetSelected);
	}

	public static void gearUp(Player c) {
		if (System.currentTimeMillis() - c.lastWithdraw < 5000) {
			c.sendMessage("Please wait before doing this again.");
			return;
		}
		c.lastWithdraw = System.currentTimeMillis();
		Preset p = c.presets[c.presetSelected];
		Bank b = c.getBank();
		b.searching = false;
		if (p != null) {
			BankHandler.bankEquipment((Client)c);
			BankHandler.bankInv((Client)c);
			for (int i = 0; i < p.equipment.length; i++) {
				if (p.equipment[i] > 0) {
					for (int j = 0; j < b.bankItems.length; j++) {
						for (int t = 0; t < b.bankItems[j].length; t++) {
							if (b.bankItems[j][t] == p.equipment[i]) {
								equip(c, p, i, j, t, b);
							}
						}
					}
				}
			}
			for (int i = 0; i < p.inventory.length; i++) {
				if (p.inventory[i] > 0) {
					for (int j = 0; j < b.bankItems.length; j++) {
						for (int t = 0; t < b.bankItems[j].length; t++) {
							if (b.bankItems[j][t] == getItem(p.inventory[i], p.inventoryN[i])) {
								inventory(c, p, i, j, t, b);
							}
						}
					}
				}
			}
			ItemAssistant.updateEquipmentInterface(c);
			ItemAssistant.updateInventory(c);
			for (int jj = 0; jj < 42; jj++) {
				for (int i = 0; i < 9; i++) {
					BankHandler.rearrangeTabItems2((Client)c, i); 
				}
			}
			BankHandler.rearrangeTabs((Client)c);
			BankHandler.refreshBank((Client) c);
		} else {
			c.sendMessage("This preset doesn't exist.");
		}
	}
	
	public static int getItem(int id, int amount) {
		if (ItemData.data[id].isNoted()) {
			return ItemData.data[id].getUnnoted();
		}
		return id;
	}

	public static void equip(Player c, Preset p, int equipSlot, int fromTab, int fromSlot, Bank bank) {
		int charges = bank.bankCharges[fromTab][fromSlot];
		int item = bank.bankItems[fromTab][fromSlot];
		int amount = deleteAmount(c, bank, p.equipmentN[equipSlot], fromTab, fromSlot);
		if (amount == 0)
			return;
		c.playerEquipment[equipSlot] = item;
		c.playerEquipmentC[equipSlot] = charges;
		c.playerEquipmentN[equipSlot] = amount;
	}
	
	public static void inventory(Player c, Preset p, int invSlot, int fromTab, int fromSlot, Bank bank) {
		int charges = bank.bankCharges[fromTab][fromSlot];
		int item = bank.bankItems[fromTab][fromSlot];
		int amount = deleteAmount(c, bank, p.inventoryN[invSlot], fromTab, fromSlot);
		if (amount == 0)
			return;
		if (amount > 1 && !ItemData.data[item].stackable) {
			item = ItemData.data[item].getNoted();
		}
		c.playerItems[invSlot] = item + 1;
		c.playerItemsC[invSlot] = charges;
		c.playerItemsN[invSlot] = amount;
	}
	
	public static int deleteAmount(Player c, Bank b, int amount, int fromTab, int fromSlot) {
		if (b.bankAmounts[fromTab][fromSlot] > amount) {
			b.bankAmounts[fromTab][fromSlot] -= amount;
			return amount;
		}
		if (b.bankAmounts[fromTab][fromSlot] <= amount) {
			int amt = b.bankAmounts[fromTab][fromSlot];
			b.bankAmounts[fromTab][fromSlot] = 0;
			b.bankCharges[fromTab][fromSlot] = -1;
			b.bankItems[fromTab][fromSlot] = 0;
			return amt;
		}
		return 0;
	}

	public static void setName(Player c, int preset, String name) {
		if (c.presets[preset] == null) {
			c.presets[preset] = new Preset("Preset "+(preset + 1)+"", new int[14], new int[14], new int[28], new int[28]);
		}
		name = name.replaceAll(" = ", "");
		Preset p = c.presets[preset];
		p.name = name;
		c.getPA().sendFrame126(p.name, 31005 + preset);
		if (c.presetSelected == preset) {
			c.getPA().sendFrame126(p.name, 31003);
		}
	}

	public static void open(Player c) {
		c.getPA().sendFrame126("Select a preset from the list", 31003);
		for (int i = 0; i < 10; i++) {
			if (c.presets[i] != null) {
				c.getPA().sendFrame126(c.presets[i].name, 31005 + i);
			} else {
				c.getPA().sendFrame126("Preset "+(i + 1), 31005 + i);
			}
		}
		if (c.presets[c.presetSelected] != null) {
			view(c, c.presetSelected);
		}
		c.getPA().showInterface(31000);
	}

	public static void load(Player c, String line) {
		try{ 
			String[] b = line.split(" = ");
			String[] f = null;
			int[] equipment = new int[14];
			int[] equipmentN = new int[14];
			int[] inventory = new int[28];
			int[] inventoryN = new int[28];
			for (int i = 0; i < 14; i++) {
				f = b[3].split(",");
				equipment[i] = Integer.parseInt(f[i]);
			}
			for (int i = 0; i < 14; i++) {
				f = b[4].split(",");
				equipmentN[i] = Integer.parseInt(f[i]);
			}
			for (int i = 0; i < 28; i++) {
				f = b[5].split(",");
				inventory[i] = Integer.parseInt(f[i]);
			}
			for (int i = 0; i < 28; i++) {
				f = b[6].split(",");
				inventoryN[i] = Integer.parseInt(f[i]);
			}
			c.presets[Integer.parseInt(b[1])] = new Preset(b[2], equipment, equipmentN, inventory, inventoryN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
