package ionic.item;

import ionic.npc.NPCHandler;
import ionic.npc.pet.Pet;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.interfaces.AreaInterface;
import utility.Misc;
import core.Constants;
import core.Server;

public class ItemAssistant
{
	
	
	public static int getItemCountWithNotes(Player player, int item) {
    	int amount = 0;
    	if (!ItemData.data[item].isNoted()) {
    	if (ItemData.data[item].getNoted() != -1) {
    		amount += getItemAmount(player, ItemData.data[item].getNoted());
    	}
    	amount += getItemAmount(player, item);
    	}
    	return amount;
    }
    
    public static boolean playerHasItemWithNotes(Player c, int item, int amount) {
    	if (getItemCountWithNotes(c, item) >= amount) {
    		return true;
    	}
    	return false;
    }
    
    public static void deleteItemWithNotes(Player c, int item, int amount) {
    	if (ItemData.data[item].getNoted() != -1) {
    		int k4 = getItemAmount(c, ItemData.data[item].getNoted());
    		deleteItem1(c, ItemData.data[item].getNoted(), amount);
    		amount -= k4;
    	}
    	if (amount > 0) {
    		deleteItem(c, item, amount);
    	}
    }
	
	
	
	
	/**
	 * Deleting items
	 */
	public static void deleteItemForBank(Player c, int item, int amount) {
		if (ItemData.data[item].stackable) {
			deleteItem1(c, item, amount);
		} else {
			deleteItem(c, item, amount);
		}
	}
	public static void deleteItemForBank2(Player c, int item, int amount) {
		if (ItemData.data[item].stackable) {
			deleteItem33(c, item, getItemSlot(c, item), amount);
		} else {
			deleteItem34(c, item, amount);
		}
	}
	
	
	public static void deleteItem34(Player player, int id, int amount) {
		if (id <= 0) return;
		for (int j = 0; j < player.playerItems.length; j++) {
			if (amount <= 0) break;
			if (player.playerItems[j] == id + 1) {
				player.playerItems[j] = 0;
				player.playerItemsN[j] = 0;
				player.playerItemsC[j] = -1;
				amount--;
			}
		}
	}
	
	public static void deleteItem33(Player player, int id, int slot, int amount) {
		if (id <= 0 || slot < 0) {
			return;
		}
		if (player.playerItems[slot] == (id + 1)) {
			if (player.playerItemsN[slot] > amount) {
				player.playerItemsN[slot] -= amount;
			} else {
				player.playerItemsN[slot] = 0;
				player.playerItems[slot] = 0;
				player.playerItemsC[slot] = -1;
			}
		}
	}
	
	
	
	public static int getItemShopValue(int itemId) {
		if (itemId < 0 || ItemData.data[itemId] == null)
			return 0;
            return ItemData.data[itemId].shopValue;
    }
	
        /**
         * Items used for slicing through webs at Magebank.
         * @return
         * 			True, if the player has a sharp weapon weilded.
         */
        public static boolean weildingSharpWeapon(Player player)
        {
                String s = getItemName(player.playerEquipment[Constants.WEAPON_SLOT]);
                if (s.contains("2h") || s.contains("sword") || s.contains("dagger") || s.contains("rapier") || s.contains("scimitar") || s.contains("halberd") || s.contains("spear") || s.contains("axe") || s.contains("claws") || s.contains("whip"))
                {
                        return true;
                }
                return false;
        }

        /**
         * Update the inventory appearance.
         */
        public static void updateInventory(Player player)
        {
                resetItems(player, 3214);
        }

        /**
         * @param player
         * 			The associated player.
         * @param itemID
         * 			The item identity to search for.
         * @param equipmentSlot
         * 			The equipment slot to search in.
         * @return
         * 			True, if the player has the itemID equipped in the required equipmentSlot
         */
        public static boolean hasItemEquippedSlot(Player player, int itemID, int equipmentSlot)
        {
                return player.playerEquipment[equipmentSlot] == itemID;
        }

        /**
         * @param player
         * 			The associated player.
         * @param itemID
         * 			The itemID to check for in the player's equipped items.
         * @return
         * 			True, if the player has the itemID equipped.
         */
        public static boolean hasItemEquipped(Player player, int itemID)
        {
                for (int i = 0; i < player.playerEquipment.length; i++)
                {
                        if (player.playerEquipment[i] == itemID)
                        {
                                return true;
                        }
                }
                return false;
        }

        /**
         * Replace the player's chosen equipment slot with an item.
         * @param player
         * 			The associated player.
         * @param equipmentSlot
         * 			The equipment slot ID to put the itemID in.
         * @param itemID
         * 			The item ID to put in the equipmentSlot
         */
        public static void replaceEquipmentSlot(Player player, int equipmentSlot, int itemID)
        {
                if (player.playerEquipment[equipmentSlot] > 0)
                {
                        player.playerEquipment[equipmentSlot] = itemID;
                        if (itemID <= 0)
                        {
                                player.playerEquipmentN[equipmentSlot] = 0;
                                player.updateRequired = true;
                                player.getPA().requestUpdates();
                                player.setAppearanceUpdateRequired(true);
                        }
                        updateSlot(player, equipmentSlot);
                        player.setInventoryUpdate(true);
                }
        }
     

        /**
         * @param Player
         * 			The associated player.
         * @param itemID
         * 			The item identity to check.
         * @return
         * 			The amount of itemID the player has equipped.
         */
        public static int getWornItemAmount(Player player, int itemID)
        {
                for (int i = 0; i < 12; i++)
                {
                        if (player.playerEquipment[i] == itemID)
                        {
                                return player.playerEquipmentN[i];
                        }
                }
                return 0;
        }

        /**
         * Update the writeFrame.
         * <p> Also used for updating the inventory visuals.
         * @param player
         * 			The associated player.
         * @param writeFrame
         * 			The ID to update, 3214 is inventory.
         */
        public static void resetItems(Player player, int writeFrame)
        {
                if (player.getOutStream() != null && player != null)
                {
                        player.getOutStream().createFrameVarSizeWord(53);
                        player.getOutStream().writeWord(writeFrame);
                        player.getOutStream().writeWord(player.playerItems.length);
                        for (int i = 0; i < player.playerItems.length; i++)
                        {
                                if (player.playerItemsN[i] > 254)
                                {
                                        player.getOutStream().writeByte(255);
                                        player.getOutStream().writeDWord_v2(player.playerItemsN[i]);
                                }
                                else player.getOutStream().writeByte(player.playerItemsN[i]);
                                player.getOutStream().writeWordBigEndianA(player.playerItems[i]);
                        }
                        player.getOutStream().endFrameVarSizeWord();
                        player.flushOutStream();
                }
        }
        
        
        
        public static void itemsOnInterface(Player c, int frame, int[] items, int[] amounts) {
        	if (c.getOutStream() != null && c != null) {
        		c.getOutStream().createFrameVarSizeWord(53);
        		c.getOutStream().writeWord(frame);
        		c.getOutStream().writeWord(items.length);
        		for (int i = 0; i < items.length; i++) {
        			if (amounts[i] > 254) {
        				c.getOutStream().writeByte(255);
        				c.getOutStream().writeDWord_v2(amounts[i]);
        			} else {
        				c.getOutStream().writeByte(amounts[i]);
        			}
        				c.getOutStream().writeWordBigEndianA(items[i] + 1);
        		}
        		c.getOutStream().endFrameVarSizeWord();
        		c.flushOutStream();
        	}
        }
        
        

        /**
         * Note: this does not additionally scan noted items.
         * @param player
         * 			The associated player.
         * @param itemID
         * 			The item identity to check.
         * @return
         * 			The amount of the itemID in the player's inventory.
         */
        public static int getInventoryItemAmount(Player player, int itemID)
        {
                int count = 0;
                for (int j = 0; j < player.playerItems.length; j++)
                {
                        if (player.playerItems[j] == itemID + 1) count += player.playerItemsN[j];
                }
                return count;
        }

        /**
         * Update equipment interface.
         */
        public static void writeBonus(Player player)
        {
                int offset = 0;
                String send = "";
                for (int i = 0; i < player.playerBonus.length; i++)
                {
                        send = (player.playerBonus[i] >= 0) ? Constants.EQUIPMENT_BONUS[i] + ": +" + player.playerBonus[i] : Constants.EQUIPMENT_BONUS[i] + ": -" + java.lang.Math.abs(player.playerBonus[i]);
                        if (i == 10) offset = 1;
                        player.getPA().sendFrame126(send, (1675 + i + offset));
                }
        }

        /**
         * Delete all worn items and items in inventory.
         **/
        public static void deleteAllItems(Player player)
        {
                for (int i1 = 0; i1 < player.playerEquipment.length; i1++)
                {
                        deleteEquipment(player, player.playerEquipment[i1], i1);
                }
                for (int i = 0; i < player.playerItems.length; i++)
                {
                        deleteItem(player, player.playerItems[i] - 1, getItemSlot(player, player.playerItems[i] - 1), player.playerItemsN[i]);
                }
        }

        /**
         * @param itemID
         * 			The item identity to dissapear on death.
         * @return
         * 			True if the item will dissapear on death.
         */
        public static boolean itemDissapear(int itemID)
        {
                for (int j = 0; j < Constants.ITEMS_TO_DISSAPEAR.length; j++)
                {
                        if (itemID == Constants.ITEMS_TO_DISSAPEAR[j])
                        {
                                return true;
                        }
                }
                for (int j = 0; j < Constants.ITEMS_TO_INVENTORY_ON_DEATH.length; j++)
                {
                        if (itemID == Constants.ITEMS_TO_INVENTORY_ON_DEATH[j])
                        {
                                return true;
                        }
                }

                if (Pet.petItem(itemID))
                {
                	return true;
                }
                return false;
        }

        /**
         * Add item/s to the player's inventory.
         * @param player
         * 			The associated player.
         * @param itemID
         * 			The item identity to add to the inventory.
         * @param amount
         * 			The amount of the itemID.
         * @return
         * 			True if the player has enough free slots to receive the items.
         */
        public static boolean addItem(Player player, int itemID, int amount)
        {
        	int charges = -1;
                if (amount < 1)
                {
                        amount = 1;
                }
                if (itemID <= 0)
                {
                        return false;
                }
                if (charges <= 0 && ItemData.data[itemID].degradable) {
            		charges = ItemData.data[itemID].maxCharges;
            		if (itemID == 11283)
            			charges = 0;
            	}
                if (player.chargeAdd) {
                	if (ItemData.data[itemID].degradable) {
                		charges = player.chargesAdd;
                	}
                	player.chargeAdd = false;
                }
                if (freeSlots(player) == 0 && !ItemData.data[itemID].stackable)
                {
                        player.sendMessage("Not enough inventory space.");
                        return false;
                }
                for (int i = 0; i < player.playerItems.length; i++)
                {
                        if (player.playerItems[i] == itemID + 1 && ItemData.data[itemID].stackable && player.playerItems[i] > 0)
                        {
                                player.playerItems[i] = (itemID + 1);
                                player.playerItemsC[i] = charges;
                                if (player.playerItemsN[i] + amount > -1)
                                {
                                        player.playerItemsN[i] += amount;
                                }
                                else
                                {
                                        player.sendMessage("You have reached the maximum amount of a single item. Cannot add anymore.");
                                        return false;
                                }
                                if (player.getOutStream() != null && player != null)
                                {
                                        player.getOutStream().createFrameVarSizeWord(34);
                                        player.getOutStream().writeWord(3214);
                                        player.getOutStream().writeByte(i);
                                        player.getOutStream().writeWord(player.playerItems[i]);
                                        if (player.playerItemsN[i] > 254)
                                        {
                                                player.getOutStream().writeByte(255);
                                                player.getOutStream().writeDWord(player.playerItemsN[i]);
                                        }
                                        else player.getOutStream().writeByte(player.playerItemsN[i]);
                                        player.getOutStream().endFrameVarSizeWord();
                                        player.flushOutStream();
                                }
                                i = 30;
                                updateInventory(player);
                                return true;
                        }
                }
                for (int i = 0; i < player.playerItems.length; i++)
                {
                        if (player.playerItems[i] <= 0)
                        {
                                player.playerItems[i] = itemID + 1;
                                player.playerItemsC[i] = charges;
                                if (amount < Constants.MAX_ITEM_AMOUNT && amount > -1)
                                {
                                        player.playerItemsN[i] = 1;
                                        if (amount > 1)
                                        {
                                                addItem(player, itemID, amount - 1);
                                                updateInventory(player);
                                                return true;
                                        }
                                }
                                else player.playerItemsN[i] = Constants.MAX_ITEM_AMOUNT;
                                updateInventory(player);
                                i = 30;
                                return true;
                        }
                }
                updateInventory(player);
                return false;
        }


        /**
         * Calculate the equipment bonuses.
         */
        public static void calculateEquipmentBonuses(Player player) {
        	for (int i = 0; i < player.playerBonus.length; i++) {
        		player.playerBonus[i] = 0;
        	}

        	for (int i = 0; i < player.playerEquipment.length; i++) {
        		if (player.playerEquipment[i] > -1) {
        			if (ItemData.data[player.playerEquipment[i]].getId() == player.playerEquipment[i]) {
        				for (int k = 0; k < player.playerBonus.length; k++) {
        					int bonus = 0;
        					if (ItemData.data[player.playerEquipment[i]] != null) {
        						if (ItemData.data[player.playerEquipment[i]].bonuses != null) {
        							bonus = ItemData.data[player.playerEquipment[i]].bonuses[k];
        						}
        					}
        					player.playerBonus[k] += bonus;
        				}
        			}
        		}
        	}
        }

        /**
         * Update the combat interface with the correct interface according to the weapon weilded.
         **/
        public static void updateCombatInterface(Player player, int Weapon, String WeaponName)
        {
                if (WeaponName.contains("Unarmed"))
                {
                        player.setSidebarInterface(0, 5855); // punch, kick, block
                        player.getPA().sendFrame126(WeaponName, 5857);
                }
                else if (WeaponName.contains("whip"))
                {
                        player.setSidebarInterface(0, 12290); // flick, lash, deflect
                        player.getPA().sendFrame246(12291, 200, Weapon);
                        player.getPA().sendFrame126(WeaponName, 12293);
                }
                else if (WeaponName.contains("bow") || WeaponName.contains("cannon") || WeaponName.contains("javelin") || WeaponName.contains("throwing"))
                {
                        player.setSidebarInterface(0, 1764); // accurate, rapid, longrange
                        player.getPA().sendFrame246(1765, 200, Weapon);
                        player.getPA().sendFrame126(WeaponName, 1767);
                }
                else if (WeaponName.contains("dagger") || WeaponName.contains("sword") && !WeaponName.contains("godsword") && !WeaponName.contains("Vesta's"))
                {
                        player.setSidebarInterface(0, 2276); // stab, lunge, slash, block
                        player.getPA().sendFrame246(2277, 200, Weapon);
                        player.getPA().sendFrame126(WeaponName, 2279);
                }
                else if (WeaponName.contains("Staff") || WeaponName.contains("staff") || WeaponName.contains("wand") || WeaponName.contains("Staff of light"))
                {
                        player.setSidebarInterface(0, 328); // spike, impale, smash, block
                        player.getPA().sendFrame246(329, 200, Weapon);
                        player.getPA().sendFrame126(WeaponName, 331);
                }
                else if (WeaponName.contains("dart") || WeaponName.contains("knife") || WeaponName.contains("javelin") || WeaponName.contains("toktz-xil-ul"))
                {
                        player.setSidebarInterface(0, 4446); // accurate, rapid, longrange
                        player.getPA().sendFrame246(4447, 200, Weapon);
                        player.getPA().sendFrame126(WeaponName, 4449);
                }
                else if (WeaponName.contains("pickaxe"))
                {
                        player.setSidebarInterface(0, 5570); // spike, impale, smash, block
                        player.getPA().sendFrame246(5571, 200, Weapon);
                        player.getPA().sendFrame126(WeaponName, 5573);
                }
                else if (WeaponName.contains("axe"))
                {
                        player.setSidebarInterface(0, 1698); // chop, hack, smash, block
                        player.getPA().sendFrame246(1699, 200, Weapon);
                        player.getPA().sendFrame126(WeaponName, 1701);
                }
                else if (WeaponName.contains("claws"))
                {
                        player.setSidebarInterface(0, 7762);
                        player.getPA().sendFrame246(7763, 200, Weapon);
                        player.getPA().sendFrame126(WeaponName, 7765);
                }
                else if (WeaponName.contains("halberd"))
                {
                        player.setSidebarInterface(0, 8460); // jab, swipe, fend
                        player.getPA().sendFrame246(8461, 200, Weapon);
                        player.getPA().sendFrame126(WeaponName, 8463);
                }
                else if (WeaponName.contains("Scythe"))
                {
                        player.setSidebarInterface(0, 8460); // jab, swipe, fend
                        player.getPA().sendFrame246(8461, 200, Weapon);
                        player.getPA().sendFrame126(WeaponName, 8463);
                }
                else if (WeaponName.contains("spear"))
                {
                        player.setSidebarInterface(0, 4679); // lunge, swipe, pound, block
                        player.getPA().sendFrame246(4680, 200, Weapon);
                        player.getPA().sendFrame126(WeaponName, 4682);
                }
                else if (WeaponName.contains("mace"))
                {
                        player.setSidebarInterface(0, 3796);
                        player.getPA().sendFrame246(3797, 200, Weapon);
                        player.getPA().sendFrame126(WeaponName, 3799);
                }
                else if (WeaponName.contains("maul") || WeaponName.contains("hammer"))
                {
                        player.setSidebarInterface(0, 425); // war hamer equip.
                        player.getPA().sendFrame246(426, 200, Weapon);
                        player.getPA().sendFrame126(WeaponName, 428);
                }
                else
                {
                        player.setSidebarInterface(0, 2423); // chop, slash, lunge, block
                        player.getPA().sendFrame246(2424, 200, Weapon);
                        player.getPA().sendFrame126(WeaponName, 2426);
                }
        }

        /**
         * Set the item requirement variables.
         * @param player
         * 			The associated player.
         * @param itemName
         * 			The name of the item that will have it's item requirement variables set.
         * @param itemID
         * 			The identity of the item that will have it's item requirement variables set.
         **/
        public static void setItemRequirements(Player player, String itemName, int itemID)
        {
                player.attackLevelReq = player.defenceLevelReq = player.strengthLevelReq = player.rangeLevelReq = player.prayerLevelReq = player.magicLevelReq = 0;

                if (itemName.contains("dragon mask"))
                {
                        player.defenceLevelReq = 1;

                }

                else if (itemName.contains("mystic") || itemName.contains("enchanted"))
                {
                        if (itemName.contains("staff"))
                        {
                                player.magicLevelReq = 20;
                                player.attackLevelReq = 40;
                        }
                        else
                        {
                                player.magicLevelReq = 20;
                                player.defenceLevelReq = 20;
                        }
                }

                else if (itemName.contains("infinity"))
                {
                        player.magicLevelReq = 50;
                        player.defenceLevelReq = 25;
                }

                else if (itemName.contains("rune c'bow"))
                {
                        player.rangeLevelReq = 61;
                }

                else if (itemName.contains("steel knife"))
                {
                        player.rangeLevelReq = 5;
                }

                else if (itemName.contains("black knife"))
                {
                        player.rangeLevelReq = 10;
                }

                else if (itemName.contains("mithril knife"))
                {
                        player.rangeLevelReq = 20;
                }

                else if (itemName.contains("adamant knife"))
                {
                        player.rangeLevelReq = 30;
                }

                else if (itemName.contains("rune knife"))
                {
                        player.rangeLevelReq = 40;
                }

                else if (itemName.contains("splitbark"))
                {
                        player.magicLevelReq = 40;
                        player.defenceLevelReq = 40;
                }

                else if (itemName.contains("green"))
                {
                        if (itemName.contains("hide"))
                        {
                                player.rangeLevelReq = 40;
                                if (itemName.contains("body"))
                                {
                                        player.defenceLevelReq = 40;
                                }

                        }
                }

                else if (itemName.contains("blue"))
                {
                        if (itemName.contains("hide"))
                        {
                                player.rangeLevelReq = 50;
                                if (itemName.contains("body"))
                                {
                                        player.defenceLevelReq = 40;
                                }

                        }
                }

                else if (itemName.contains("red"))
                {
                        if (itemName.contains("hide"))
                        {
                                player.rangeLevelReq = 60;
                                if (itemName.contains("body"))
                                {
                                        player.defenceLevelReq = 40;
                                }

                        }
                }

                else if (itemName.contains("black") && !itemName.contains("defender"))
                {
                        if (itemName.contains("hide"))
                        {
                                player.rangeLevelReq = 70;
                                if (itemName.contains("body")) player.defenceLevelReq = 40;

                        }
                }

                else if (itemName.contains("bronze"))
                {
                        if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe"))
                        {
                                player.attackLevelReq = player.defenceLevelReq = 1;
                        }

                }

                else if (itemName.contains("iron"))
                {
                        if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe"))
                        {
                                player.attackLevelReq = player.defenceLevelReq = 1;
                        }

                }

                else if (itemName.contains("steel"))
                {
                        if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe"))
                        {
                                player.attackLevelReq = player.defenceLevelReq = 5;
                        }

                }

                else if (itemName.contains("mithril"))
                {
                        if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe"))
                        {
                                player.attackLevelReq = player.defenceLevelReq = 20;
                        }

                }

                else if (itemName.contains("adamant"))
                {
                        if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe"))
                        {
                                player.attackLevelReq = player.defenceLevelReq = 30;
                        }

                }

                else if (itemName.contains("rune"))
                {
                        if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin") && !itemName.contains("thrownaxe") && !itemName.contains("'bow"))
                        {
                                player.attackLevelReq = player.defenceLevelReq = 40;
                        }

                }

                else if (itemName.contains("dragon"))
                {
                        if (!itemName.contains("nti-") && !itemName.contains("fire"))
                        {
                                player.attackLevelReq = player.defenceLevelReq = 60;

                        }
                        else if (itemName.contains("dragonfire shield"))
                        {
                                player.defenceLevelReq = 75;
                        }
                }

                else if (itemName.contains("ahrim"))
                {
                        if (itemName.contains("staff"))
                        {
                                player.attackLevelReq = 70;
                        }
                        else
                        {
                                player.defenceLevelReq = 70;
                        }
                        player.magicLevelReq = 70;
                }

                else if (itemName.contains("dagon"))
                {
                        player.magicLevelReq = 40;
                        player.defenceLevelReq = 20;
                }

                else if (itemName.contains("arcane stream necklace"))
                {
                        player.magicLevelReq = 70;
                }

                else if (itemName.contains("initiate"))
                {
                        player.defenceLevelReq = 20;
                }

                else if (itemName.contains("chaotic"))
                {
                        if (itemName.contains("crossbow"))
                        {
                                player.rangeLevelReq = 80;
                        }
                        else if (itemName.contains("shield"))
                        {
                                player.defenceLevelReq = 80;
                        }
                        else
                        {
                                player.attackLevelReq = 80;
                        }
                }

                else if (itemName.contains("vesta") && itemName.contains("corrupt"))
                {
                        if (itemName.contains("longsword") || itemName.contains("spear"))
                        {
                                player.attackLevelReq = 20;
                        }
                        else
                        {
                                player.defenceLevelReq = 20;
                        }
                }

                else if (itemName.contains("vesta") && !itemName.contains("corrupt"))
                {
                        if (itemName.contains("longsword") || itemName.contains("spear"))
                        {
                                player.attackLevelReq = 78;
                        }
                        else
                        {
                                player.defenceLevelReq = 78;
                        }
                }

                else if (itemName.contains("statius") && itemName.contains("corrupt"))
                {
                        if (itemName.contains("warhammer"))
                        {
                                player.attackLevelReq = 20;
                        }
                        else
                        {
                                player.defenceLevelReq = 20;
                        }
                }

                else if (itemName.contains("statius") && !itemName.contains("corrupt"))
                {
                        if (itemName.contains("warhammer"))
                        {
                                player.attackLevelReq = 78;
                        }
                        else
                        {
                                player.defenceLevelReq = 78;
                        }
                }

                else if (itemName.contains("zuriel") && itemName.contains("corrupt"))
                {
                        if (itemName.contains("staff"))
                        {
                                player.attackLevelReq = 20;
                        }
                        else
                        {
                                player.defenceLevelReq = 20;
                        }
                        player.magicLevelReq = 20;
                }

                else if (itemName.contains("zuriel") && !itemName.contains("corrupt"))
                {
                        if (itemName.contains("staff"))
                        {
                                player.attackLevelReq = 78;
                        }
                        else
                        {
                                player.defenceLevelReq = 78;
                        }
                        player.magicLevelReq = 78;
                }

                else if (itemName.contains("morrigan") && itemName.contains("corrupt"))
                {
                        if (itemName.contains("javelin"))
                        {
                                player.rangeLevelReq = 20;
                        }
                        else
                        {
                                player.rangeLevelReq = 20;
                                player.defenceLevelReq = 20;
                        }
                }

                else if (itemName.contains("morrigan") && !itemName.contains("corrupt"))
                {
                        if (itemName.contains("javelin"))
                        {
                                player.rangeLevelReq = 78;
                        }
                        else
                        {
                                player.rangeLevelReq = 78;
                                player.defenceLevelReq = 78;
                        }
                }

                else if (itemName.contains("karil"))
                {
                        if (itemName.contains("crossbow"))
                        {
                                player.rangeLevelReq = 70;
                        }
                        else
                        {
                                player.rangeLevelReq = 70;
                                player.defenceLevelReq = 70;
                        }
                }

                else if (itemName.contains("elite"))
                {
                        player.defenceLevelReq = 40;
                }

                else if (itemName.contains("torva"))
                {
                        player.defenceLevelReq = 80;
                }

                else if (itemName.contains("pernix"))
                {
                        player.defenceLevelReq = 80;
                        player.rangeLevelReq = 80;
                }

                else if (itemName.contains("virtus"))
                {
                        player.defenceLevelReq = 80;
                        player.magicLevelReq = 80;
                }

                else if (itemName.contains("godsword"))
                {
                        player.attackLevelReq = 75;
                }

                else if (itemName.contains("3rd age") && !itemName.contains("amulet"))
                {
                        player.defenceLevelReq = 60;
                }

                else if (itemName.contains("Initiate"))
                {
                        player.defenceLevelReq = 20;
                }

                else if (itemName.contains("verac") || itemName.contains("guthan") || itemName.contains("dharok") || itemName.contains("torag"))
                {
                        if (itemName.contains("hammers"))
                        {
                                player.attackLevelReq = 70;
                                player.strengthLevelReq = 70;
                        }
                        else if (itemName.contains("axe"))
                        {
                                player.attackLevelReq = 70;
                                player.strengthLevelReq = 70;
                        }
                        else if (itemName.contains("warspear"))
                        {
                                player.attackLevelReq = 70;
                                player.strengthLevelReq = 70;
                        }
                        else if (itemName.contains("flail"))
                        {
                                player.attackLevelReq = 70;
                                player.strengthLevelReq = 70;
                        }
                        else
                        {
                                player.defenceLevelReq = 70;
                        }
                }

                else if (itemName.contains("void"))
                {
                        player.attackLevelReq = 42;
                        player.rangeLevelReq = 42;
                        player.strengthLevelReq = 42;
                        player.magicLevelReq = 42;
                        player.defenceLevelReq = 42;
                }

                else if (itemName.contains("ancient staff"))
                {
                        player.attackLevelReq = 50;
                        player.magicLevelReq = 50;
                }

                else if (itemName.contains("staff of light"))
                {
                        player.attackLevelReq = 75;
                        player.magicLevelReq = 75;
                }

                else if (itemName.contains("saradomin sword") || itemName.contains("zamorakian spear"))
                {
                        player.attackLevelReq = 70;
                }

                else if (itemName.contains("divine spirit shield") || itemName.contains("elysian spirit shield"))
                {
                        player.defenceLevelReq = 75;
                        player.prayerLevelReq = 75;
                }

                else if (itemName.contains("spirit shield"))
                {
                        player.defenceLevelReq = 40;
                        player.prayerLevelReq = 55;
                }

                else if (itemName.contains("blessed spirit shield"))
                {
                        player.defenceLevelReq = 70;
                        player.prayerLevelReq = 60;
                }

                else if (itemName.contains("spectral spirit shield") || itemName.contains("arcane spirit shield"))
                {
                        player.defenceLevelReq = 75;
                        player.magicLevelReq = 65;
                        player.prayerLevelReq = 70;
                }

                else if (itemName.contains("fighter"))
                {
                        player.defenceLevelReq = 40;
                }

                else if (itemName.contains("dark bow") || itemName.contains("toktz-xil-ul"))
                {
                        player.rangeLevelReq = 60;
                }

                else if (itemName.contains("toktz-ket-xil"))
                {
                        player.defenceLevelReq = 60;
                }

                else if (itemName.contains("master wand") || itemName.contains("mages' book"))
                {
                        player.magicLevelReq = 60;
                }

                else if (itemName.contains("magic shortbow"))
                {
                        player.rangeLevelReq = 50;
                }

                else if (itemName.contains("helm of neitiznot"))
                {
                        player.defenceLevelReq = 55;
                }

                else if (itemName.contains("bandos chestplate") || itemName.contains("bandos tassets") || itemName.contains("bandos boots"))
                {
                        player.defenceLevelReq = 65;
                }

                else if (itemName.contains("berserker helm") || itemName.contains("archer helm") || itemName.contains("farseer helm") || itemName.contains("warrior helm"))
                {
                        player.defenceLevelReq = 45;
                }

                else if (itemName.contains("zamorak cape") || itemName.contains("saradomin cape") || itemName.contains("guthix cape"))
                {
                        player.magicLevelReq = 60;
                }

                else if (itemName.contains("steel defender"))
                {
                        player.defenceLevelReq = 5;
                }

                else if (itemName.contains("black defender"))
                {
                        player.defenceLevelReq = 10;
                }

                else if (itemName.contains("mithril defender"))
                {
                        player.defenceLevelReq = 20;
                }

                else if (itemName.contains("adamant defender"))
                {
                        player.defenceLevelReq = 30;
                }

                else if (itemName.contains("rune defender"))
                {
                        player.defenceLevelReq = 40;
                }

                else if (itemName.contains("barrelchest anchor"))
                {
                        player.strengthLevelReq = 60;
                        player.attackLevelReq = 60;
                }

                else if (itemName.contains("abyssal whip"))
                {
                        player.attackLevelReq = 70;
                }

                else if (itemName.contains("granite maul"))
                {
                        player.attackLevelReq = 50;
                        player.strengthLevelReq = 50;
                }
                switch (itemID)
                {

                case 7462:
                        // Barrows gloves.
                case 7461:
                        // Dragon gloves.
                        player.defenceLevelReq = 40;
                        break;
                case 7460:
                        // Rune gloves.
                        player.defenceLevelReq = 40;
                        break;
                }
        }

        /**
         * @param itemName
         * 			The name of the item to check if it's 2-handed.
         * @param itemID
         * 			The ID of the item to check if it's 2-handed.
         * @return
         * 			True, if the itemID is a 2-handed weapon.
         */
        public static boolean is2handed(String itemName, int itemID)
        {
                String[] names =
                {
                        "ahrim", "karil", "verac", "guthan", "dharok", "torag", "longbow", "shortbow", "dark bow", "godsword", "saradomin sword", "2h", "spear", "maul", "tzhaar-ket-om", "claws", "barrelchest anchor", "boxing gloves", "hand cannon"
                };
                for (int i = 0; i < names.length; i++)
                {
                        if (itemName.contains(names[i]))
                        {
                                return true;
                        }
                }
                return false;
        }

        /**
         * Weapons special bar, adds the spec bars to weapons that require them and
         * removes the spec bars from weapons which don't require them
         **/
        public static void addSpecialBar(Player player, int weapon) {
        	if (weapon > 0) {
        	if (ItemData.data[weapon].isLent()) {
        		weapon = ItemData.data[weapon].getUnlentItemId();
        	}
        	}
                switch (weapon) {
                case 14484:
                case 3101:
                        // Dragon claws
                        player.getPA().sendFrame171(0, 7800);
                        specialAmount(player, weapon, player.specAmount, 7812);
                        break;
                case 15441:
                        // whip
                case 15442:
                        // whip
                case 15443:
                        // whip
                case 15444:
                        // whip
                case 4151:
                        // whip
                        player.getPA().sendFrame171(0, 12323);
                        specialAmount(player, weapon, player.specAmount, 12335);
                        break;
                case 859:
                        // Magic bows
                case 861:
                case 11235:
                        // Dark bow
                case 15241:
                        // Hand cannon
                case 13883:
                        // morrigan throwing axe
                case 13879:
                        // Morrigan Javeline
                        player.getPA().sendFrame171(0, 7549);
                        specialAmount(player, weapon, player.specAmount, 7561);
                        break;
                case 4587:
                        // dscimmy
                case 10887:
                case 11694:
                case 11698:
                case 11700:
                case 11696:
                
                case 13923: //v longs
                case 13925: 
                case 13901: 
                case 13899:
                        player.getPA().sendFrame171(0, 7599);
                        specialAmount(player, weapon, player.specAmount, 7611);
                        break;

                case 11730:
                        // Saradomin sword.
                        player.getPA().sendFrame171(0, 7574);
                        specialAmount(player, weapon, player.specAmount, 7586);
                        break;
                case 3204:
                        // d hally
                        player.getPA().sendFrame171(0, 8493);
                        specialAmount(player, weapon, player.specAmount, 8505);
                        break;
                case 1377:
                        // d battleaxe
                        player.getPA().sendFrame171(0, 7499);
                        specialAmount(player, weapon, player.specAmount, 7511);
                        break;
                case 4153:
                        // gmaul
                case 13902:
                case 13904:
                case 13926:
                case 13928:
                        player.getPA().sendFrame171(0, 7474);
                        specialAmount(player, weapon, player.specAmount, 7486);
                        break;
                case 1249:
                        // dspear
                case 13905:
                case 13929: 
                case 13931: 
                case 13907:
                        player.getPA().sendFrame171(0, 7674);
                        specialAmount(player, weapon, player.specAmount, 7686);
                        break;
                case 1215:
                        // dragon dagger
                case 1231:
                case 5680:
                case 5698:
                case 1305:
                case 19780:
                        // dragon long
                        player.getPA().sendFrame171(0, 7574);
                        specialAmount(player, weapon, player.specAmount, 7586);
                        break;
                case 11061: //Ancient Mace
                case 1434:
                        // dragon mace
                        player.getPA().sendFrame171(0, 7624);
                        specialAmount(player, weapon, player.specAmount, 7636);
                        break;
                default:
                        player.getPA().sendFrame171(1, 7624); // mace interface
                        player.getPA().sendFrame171(1, 7474); // hammer, gmaul
                        player.getPA().sendFrame171(1, 7499); // axe
                        player.getPA().sendFrame171(1, 7549); // bow interface
                        player.getPA().sendFrame171(1, 7574); // sword interface
                        player.getPA().sendFrame171(1, 7599); // scimmy sword interface, for
                        // most
                        // swords
                        player.getPA().sendFrame171(1, 8493);
                        player.getPA().sendFrame171(1, 12323); // whip interface
                        break;
                }
        }

        /**
         * Specials bar filling amount
         **/
        public static void specialAmount(Player player, int weapon, double specAmount, int barId)
        {
                player.specBarId = barId;
                player.getPA().sendFrame70(specAmount >= 10 ? 500 : 0, 0, (--barId));
                player.getPA().sendFrame70(specAmount >= 9 ? 500 : 0, 0, (--barId));
                player.getPA().sendFrame70(specAmount >= 8 ? 500 : 0, 0, (--barId));
                player.getPA().sendFrame70(specAmount >= 7 ? 500 : 0, 0, (--barId));
                player.getPA().sendFrame70(specAmount >= 6 ? 500 : 0, 0, (--barId));
                player.getPA().sendFrame70(specAmount >= 5 ? 500 : 0, 0, (--barId));
                player.getPA().sendFrame70(specAmount >= 4 ? 500 : 0, 0, (--barId));
                player.getPA().sendFrame70(specAmount >= 3 ? 500 : 0, 0, (--barId));
                player.getPA().sendFrame70(specAmount >= 2 ? 500 : 0, 0, (--barId));
                player.getPA().sendFrame70(specAmount >= 1 ? 500 : 0, 0, (--barId));
                updateSpecialBar(player);
                updateCombatInterface(player, weapon, getItemName(weapon));
        }

        /**
         * Special attack text and what to highlight or blackout
         **/
        public static void updateSpecialBar(Player player)
        {
                String percent = Double.toString(player.specAmount);
                if (percent.contains("."))
                {
                        percent = percent.replace(".", "");
                }
                if (percent.startsWith("0") && !percent.equals("00"))
                {
                        percent = percent.replace("0", "");
                }
                if (percent.startsWith("0") && percent.equals("00"))
                {
                        percent = percent.replace("00", "0");
                }
                if (player.usingSpecial)
                {
                        player.getPA().sendFrame126("@yel@Special Attack (" + percent + "%)", player.specBarId);
                }
                else
                {
                        player.getPA().sendFrame126("@bla@Special Attack (" + percent + "%)", player.specBarId);
                }

        }

        /**
         * Wear Item
         **/
        public static int targetSlot(String item, int itemId) {
                int targetSlot = -1;
                for (int i = 0; i < HATS.length; i++) {
                        if (item.contains(HATS[i])) targetSlot = Constants.HEAD_SLOT;
                }
                for (int i = 0; i < CAPES.length; i++) {
                        if (item.contains(CAPES[i])) targetSlot = Constants.CAPE_SLOT;
                }
                for (int i = 0; i < AMULETS.length; i++) {
                        if (item.contains(AMULETS[i])) targetSlot = 2;
                }
                for (int i = 0; i < WEAPONS.length; i++) {
                        if (item.contains(WEAPONS[i])) targetSlot = 3;
                }
                for (int i = 0; i < BODY.length; i++) {
                        if (item.contains(BODY[i])) targetSlot = 4;
                }
                for (int i = 0; i < SHIELDS.length; i++) {
                        if (item.contains(SHIELDS[i])) targetSlot = 5;
                }
                for (int i = 0; i < LEGS.length; i++) {
                        if (item.contains(LEGS[i])) targetSlot = 7;
                }
                for (int i = 0; i < GLOVES.length; i++) {
                        if (item.contains(GLOVES[i])) targetSlot = 9;
                }
                for (int i = 0; i < BOOTS.length; i++) {
                        if (item.contains(BOOTS[i])) targetSlot = 10;
                }
                for (int i = 0; i < RINGS.length; i++) {
                        if (item.contains(RINGS[i])) targetSlot = 12;
                }
                for (int i = 0; i < ARROWS.length; i++) {
                        if (item.contains(ARROWS[i])) targetSlot = 13;
                }
                
                switch (itemId) {
                case 19747:
                case 7011:
                case 19314:
                        targetSlot = Constants.HEAD_SLOT;
                        break;
                case 19709:
                case 19710:
                case 20769:
                        targetSlot = Constants.CAPE_SLOT;
                        break;
                case 4566:
                case 4565:
                case 7053:
                case 7671:
                case 7673:
                case 11259:
                case 10501:
                        targetSlot = Constants.WEAPON_SLOT;
                        break;
                case 1035:
                case 19706:
                case 19317:
                        targetSlot = Constants.TORSO_SLOT;
                        break;
                case 1033:
                case 19320:
                        targetSlot = Constants.LEG_SLOT;
                        break;
                }
                
                return targetSlot;
        }

        public static String[] HATS =
        {
                "boater", "cowl", "head", "peg", "coif", "helm", "Coif", "mask", "hat", "headband", "hood", "disguise", "cavalier", "full", "tiara", "helmet", "Hat", "ears", "crown", "partyhat", "helm(t)", "helm(g)", "beret", "facemask", "sallet", "hat(g)", "hat(t)", "bandana", "Helm", "Mitre", "mitre", "Bomber cap", "afro", "Afro", "Lord marshal cap", "cap", "Pernix cowl"
        };
        public static String[] CAPES =
        {
                "cape", "accumulator", "attractor", "cloak", "alerter", "kal", "master cape"
        };
        public static String[] AMULETS =
        {
                "amulet", "Amulet", "scarf", "Necklace", "necklace", "Pendant", "pendant", "Symbol", "symbol", "stole", "Stole"
        };
        public static String[] WEAPONS =
        {
                "hand", "mace", "dart", "knife", "javelin", "scythe", "claws", "bow", "crossbow", "c' bow", "adze", "axe", "sword", "rapier", "scimitar", "spear", "dagger", "staff", "wand", "blade", "whip", "silverlight", "darklight", "maul", "halberd", "anchor", "tzhaar-ket-om", "hammer", "hand cannon", "flail", "crozier"
        };
        public static String[] BODY =
        {
                "body", "top", "Priest gown", "apron", "shirt", "platebody", "robetop", "body(g)", "body(t)", "Wizard robe (g)", "Wizard robe (t)", "body", "brassard", "blouse", "tunic", "leathertop", "Saradomin plate", "chainbody", "hauberk", "Shirt", "torso", "chestplate", "jacket"
        };
        public static String[] SHIELDS =
        {
                "kiteshield", "book", "Kiteshield", "toktz-ket-xil", "Toktz-ket-xil", "shield", "Shield", "Kite", "kite", "Defender", "defender", "Tome"
        };
        public static String[] LEGS =
        {
                "tassets", "chaps", "bottoms", "gown", "trousers", "platelegs", "robebottoms", "plateskirt", "legs", "leggings", "shorts", "Skirt", "skirt", "cuisse", "Trousers", "Pantaloons", "tasset", "robe bottom"
        };
        public static String[] GLOVES =
        {
                "Gloves", "gloves", "glove", "Glove", "Vamb", "vamb", "gauntlets", "Gauntlets", "bracers", "Bracers", "Vambraces", "vambraces", "Bracelet", "bracelet"
        };
        public static String[] BOOTS =
        {
                "boots", "shoes", "flipper", "Flipper"
        };
        public static String[] RINGS =
        {
                "ring"
        };
        public static String[] ARROWS =
        {
                "bolts", "arrow", "bolt rack", "hand cannon shot"
        };

        public static boolean wearItem(Player player, int wearID, int slot)
        {
                synchronized(player)
                {
                        int targetSlot = 0;
                        boolean canWearItem = true;
                        if (player.playerItems[slot] == (wearID + 1))
                        {
                        	if (wearID == 10501 && !player.inWilderness()) {
                        		AreaInterface.startInterfaceEvent(player);
                        	}
                                setItemRequirements(player, getItemName(wearID).toLowerCase(), wearID);
                                targetSlot = targetSlot(getItemName(wearID).toLowerCase(), wearID);
                                if (targetSlot < 0) {
                                	return false;
                                }
                                if (player.duelRule[11] && targetSlot == Constants.HEAD_SLOT) {
                                        player.sendMessage("Wearing hats has been disabled in this duel!");
                                        return false;
                                }
                                if (player.duelRule[9]) {
                                        if (wearID == 4151 || wearID == 5698) {
                                                canWearItem = true;
                                        } else {
                                                player.sendMessage("You cannot wear this weapon with whip & dds rule enabled.");
                                                canWearItem = false;
                                        }
                                }
                                if (player.duelRule[12] && targetSlot == Constants.CAPE_SLOT)
                                {
                                        player.sendMessage("Wearing capes has been disabled in this duel!");
                                        return false;
                                }
                                if (player.duelRule[13] && targetSlot == Constants.AMULET_SLOT)
                                {
                                        player.sendMessage("Wearing amulets has been disabled in this duel!");
                                        return false;
                                }
                                if (player.duelRule[14] && targetSlot == Constants.WEAPON_SLOT)
                                {
                                        player.sendMessage("Wielding weapons has been disabled in this duel!");
                                        return false;
                                }
                                if (player.duelRule[15] && targetSlot == Constants.TORSO_SLOT)
                                {
                                        player.sendMessage("Wearing bodies has been disabled in this duel!");
                                        return false;
                                }
                                if ((player.duelRule[16] && targetSlot == Constants.SHIELD_SLOT) || (player.duelRule[16] && is2handed(getItemName(wearID).toLowerCase(), wearID)))
                                {
                                        player.sendMessage("Wearing shield has been disabled in this duel!");
                                        return false;
                                }
                                if (player.duelRule[17] && targetSlot == Constants.LEG_SLOT)
                                {
                                        player.sendMessage("Wearing legs has been disabled in this duel!");
                                        return false;
                                }
                                if (player.duelRule[18] && targetSlot == Constants.HAND_SLOT)
                                {
                                        player.sendMessage("Wearing gloves has been disabled in this duel!");
                                        return false;
                                }
                                if (player.duelRule[19] && targetSlot == Constants.FEET_SLOT)
                                {
                                        player.sendMessage("Wearing boots has been disabled in this duel!");
                                        return false;
                                }
                                if (player.duelRule[20] && targetSlot == Constants.RING_SLOT)
                                {
                                        player.sendMessage("Wearing rings has been disabled in this duel!");
                                        return false;
                                }
                                if (player.duelRule[21] && targetSlot == Constants.ARROW_SLOT)
                                {
                                        player.sendMessage("Wearing arrows has been disabled in this duel!");
                                        return false;
                                }
                                if (targetSlot == Constants.FEET_SLOT || targetSlot == Constants.LEG_SLOT || targetSlot == Constants.SHIELD_SLOT || targetSlot == Constants.TORSO_SLOT || targetSlot == Constants.HEAD_SLOT || targetSlot == Constants.HAND_SLOT)
                                {
                                        if (player.defenceLevelReq > 0)
                                        {
                                                if (player.getPA().getLevelForXP(player.playerXP[Constants.DEFENCE]) < player.defenceLevelReq)
                                                {
                                                        player.sendMessage("You need a defence level of " + player.defenceLevelReq + " to wear this item.");
                                                        canWearItem = false;
                                                }
                                        }
                                        if (player.rangeLevelReq > 0)
                                        {
                                                if (player.getPA().getLevelForXP(player.playerXP[Constants.RANGED]) < player.rangeLevelReq)
                                                {
                                                        player.sendMessage("You need a range level of " + player.rangeLevelReq + " to wear this item.");
                                                        canWearItem = false;
                                                }
                                        }
                                        if (player.magicLevelReq > 0)
                                        {
                                                if (player.getPA().getLevelForXP(player.playerXP[Constants.MAGIC]) < player.magicLevelReq)
                                                {
                                                        player.sendMessage("You need a magic level of " + player.magicLevelReq + " to wear this item.");
                                                        canWearItem = false;
                                                }
                                        }
                                }
                                else if (targetSlot == Constants.AMULET_SLOT)
                                {
                                        if (player.getPA().getLevelForXP(player.playerXP[Constants.MAGIC]) < player.magicLevelReq)
                                        {
                                                player.sendMessage("You need a magic level of " + player.magicLevelReq + " to wear this amulet.");
                                                canWearItem = false;
                                        }
                                }
                                else if (targetSlot == Constants.CAPE_SLOT)
                                {
                                        if (player.getPA().getLevelForXP(player.playerXP[Constants.MAGIC]) < player.magicLevelReq)
                                        {
                                                player.sendMessage("You need a magic level of " + player.magicLevelReq + " to wear this amulet.");
                                                canWearItem = false;
                                        }
                                }
                                else if (targetSlot == Constants.WEAPON_SLOT)
                                {
                                        if (player.attackLevelReq > 0)
                                        {
                                                if (player.getPA().getLevelForXP(player.playerXP[Constants.ATTACK]) < player.attackLevelReq)
                                                {
                                                        player.sendMessage("You need an attack level of " + player.attackLevelReq + " to wield this weapon.");
                                                        canWearItem = false;
                                                }
                                        }
                                        if (player.rangeLevelReq > 0)
                                        {
                                                if (player.getPA().getLevelForXP(player.playerXP[Constants.RANGED]) < player.rangeLevelReq)
                                                {
                                                        player.sendMessage("You need a range level of " + player.rangeLevelReq + " to wield this weapon.");
                                                        canWearItem = false;
                                                }
                                        }
                                        if (player.prayerLevelReq > 0)
                                        {
                                                if (player.getPA().getLevelForXP(player.playerXP[Constants.PRAYER]) < player.prayerLevelReq)
                                                {
                                                        player.sendMessage("You need a prayer level of " + player.prayerLevelReq + " to wield this weapon.");
                                                        canWearItem = false;
                                                }
                                        }
                                        if (player.magicLevelReq > 0)
                                        {
                                                if (player.getPA().getLevelForXP(player.playerXP[Constants.MAGIC]) < player.magicLevelReq)
                                                {
                                                        player.sendMessage("You need a magic level of " + player.magicLevelReq + " to wield this weapon.");
                                                        canWearItem = false;
                                                }
                                        }
                                        if (player.strengthLevelReq > 0)
                                        {
                                                if (player.getPA().getLevelForXP(player.playerXP[Constants.STRENGTH]) < player.strengthLevelReq)
                                                {
                                                        player.sendMessage("You need a strength level of " + player.strengthLevelReq + " to wield this weapon.");
                                                        canWearItem = false;
                                                }
                                        }
                                }
                                if (!canWearItem)
                                {
                                        return false;
                                }
                                int wearAmount = player.playerItemsN[slot];
                                if (wearAmount < 1)
                                {
                                        return false;
                                }
                                if (slot >= 0 && wearID >= 0)
                                {
                                        player.setInventoryUpdate(true);
                                        int toEquip = player.playerItems[slot];
                                        int toEquipN = player.playerItemsN[slot];
                                        int equipCharges = player.playerItemsC[slot];
                                        int toRemove = player.playerEquipment[targetSlot];
                                        int toRemoveN = player.playerEquipmentN[targetSlot];
                                        int removeCharges = player.playerEquipmentC[targetSlot];
                                        if (toEquip == toRemove + 1 && ItemData.data[toRemove].stackable)
                                        {
                                                deleteItem(player, toRemove, getItemSlot(player, toRemove), toEquipN);
                                                player.playerEquipmentN[targetSlot] += toEquipN;
                                        }
                                        else if (targetSlot != Constants.SHIELD_SLOT && targetSlot != Constants.WEAPON_SLOT)
                                        {
                                                player.playerItems[slot] = toRemove + 1;
                                                player.playerItemsN[slot] = toRemoveN;
                                                player.playerItemsC[slot] = removeCharges;
                                                player.playerEquipment[targetSlot] = toEquip - 1;
                                                player.playerEquipmentN[targetSlot] = toEquipN;
                                                player.playerEquipmentC[targetSlot] = equipCharges;
                                        }
                                        else if (targetSlot == Constants.SHIELD_SLOT)
                                        {
                                                boolean wearing2h = is2handed(getItemName(player.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase(), player.playerEquipment[Constants.WEAPON_SLOT]);
                                                if (wearing2h)
                                                {
                                                        toRemove = player.playerEquipment[Constants.WEAPON_SLOT];
                                                        toRemoveN = player.playerEquipmentN[Constants.WEAPON_SLOT];
                                                        removeCharges = player.playerEquipmentC[Constants.WEAPON_SLOT];
                                                        player.playerEquipment[Constants.WEAPON_SLOT] = -1;
                                                        player.playerEquipmentN[Constants.WEAPON_SLOT] = 0;
                                                        player.playerEquipmentC[Constants.WEAPON_SLOT] = -1;
                                                        updateSlot(player, Constants.WEAPON_SLOT);
                                                }
                                                player.playerItems[slot] = toRemove + 1;
                                                player.playerItemsN[slot] = toRemoveN;
                                                player.playerItemsC[slot] = removeCharges;
                                                player.playerEquipment[targetSlot] = toEquip - 1;
                                                player.playerEquipmentN[targetSlot] = toEquipN;
                                                player.playerEquipmentC[targetSlot] = equipCharges;
                                        }
                                        else if (targetSlot == Constants.WEAPON_SLOT)
                                        {
                                                boolean is2h = is2handed(getItemName(wearID).toLowerCase(), wearID);
                                                boolean wearingShield = player.playerEquipment[Constants.SHIELD_SLOT] > 0;
                                                boolean wearingWeapon = player.playerEquipment[Constants.WEAPON_SLOT] > 0;
                                                if (is2h)
                                                {
                                                        if (wearingShield && wearingWeapon)
                                                        {
                                                                if (freeSlots(player) > 0)
                                                                {
                                                                        player.playerItems[slot] = toRemove + 1;
                                                                        player.playerItemsN[slot] = toRemoveN;
                                                                        player.playerItemsC[slot] = removeCharges;
                                                                        player.playerEquipment[targetSlot] = toEquip - 1;
                                                                        player.playerEquipmentN[targetSlot] = toEquipN;
                                                                        player.playerEquipmentC[targetSlot] = equipCharges;
                                                                        removeItem(player, player.playerEquipment[Constants.SHIELD_SLOT], Constants.SHIELD_SLOT);
                                                                }
                                                                else
                                                                {
                                                                        player.sendMessage("You do not have enough inventory space to do this.");
                                                                        return false;
                                                                }
                                                        }
                                                        else if (wearingShield && !wearingWeapon)
                                                        {
                                                                player.playerItems[slot] = player.playerEquipment[Constants.SHIELD_SLOT] + 1;
                                                                player.playerItemsN[slot] = player.playerEquipmentN[Constants.SHIELD_SLOT];
                                                                player.playerItemsC[slot] = player.playerEquipmentC[Constants.SHIELD_SLOT];
                                                                player.playerEquipment[targetSlot] = toEquip - 1;
                                                                player.playerEquipmentN[targetSlot] = toEquipN;
                                                                player.playerEquipmentC[targetSlot] = equipCharges;
                                                                player.playerEquipment[Constants.SHIELD_SLOT] = -1;
                                                                player.playerEquipmentN[Constants.SHIELD_SLOT] = 0;
                                                                player.playerEquipmentC[Constants.SHIELD_SLOT] = -1;
                                                                updateSlot(player, Constants.SHIELD_SLOT);
                                                        }
                                                        else
                                                        {
                                                                player.playerItems[slot] = toRemove + 1;
                                                                player.playerItemsN[slot] = toRemoveN;
                                                                player.playerItemsC[slot] = removeCharges;
                                                                player.playerEquipment[targetSlot] = toEquip - 1;
                                                                player.playerEquipmentN[targetSlot] = toEquipN;
                                                                player.playerEquipmentC[targetSlot] = equipCharges;
                                                        }
                                                }
                                                else
                                                {
                                                        player.playerItems[slot] = toRemove + 1;
                                                        player.playerItemsN[slot] = toRemoveN;
                                                        player.playerItemsC[slot] = removeCharges;
                                                        player.playerEquipment[targetSlot] = toEquip - 1;
                                                        player.playerEquipmentN[targetSlot] = toEquipN;
                                                        player.playerEquipmentC[targetSlot] = equipCharges;
                                                }
                                        }
                                }
                                if (targetSlot == Constants.WEAPON_SLOT)
                                {
                                        player.usingSpecial = false;
                                        addSpecialBar(player, wearID);
                                }
                                if (player.getOutStream() != null && player != null)
                                {
                                        player.getOutStream().createFrameVarSizeWord(34);
                                        player.getOutStream().writeWord(1688);
                                        player.getOutStream().writeByte(targetSlot);
                                        player.getOutStream().writeWord(wearID + 1);
                                        if (player.playerEquipmentN[targetSlot] > 254)
                                        {
                                                player.getOutStream().writeByte(255);
                                                player.getOutStream().writeDWord(
                                                player.playerEquipmentN[targetSlot]);
                                        }
                                        else
                                        {
                                                player.getOutStream().writeByte(
                                                player.playerEquipmentN[targetSlot]);
                                        }
                                        player.getOutStream().endFrameVarSizeWord();
                                        player.flushOutStream();
                                }
                                updateCombatInterface(player, player.playerEquipment[Constants.WEAPON_SLOT], getItemName(player.playerEquipment[Constants.WEAPON_SLOT]));
                                player.getCombat().getPlayerAnimIndex(getItemName(player.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
                                player.switches++;
                                return true;
                        }
                        else
                        {
                                return false;
                        }
                }
        }

        public static void updateSlot(Player player, int slot)
        {
                synchronized(player)
                {
                        if (player.getOutStream() != null && player != null)
                        {
                                player.getOutStream().createFrameVarSizeWord(34);
                                player.getOutStream().writeWord(1688);
                                player.getOutStream().writeByte(slot);
                                player.getOutStream().writeWord(
                                player.playerEquipment[slot] + 1);
                                if (player.playerEquipmentN[slot] > 254)
                                {
                                        player.getOutStream().writeByte(255);
                                        player.getOutStream().writeDWord(
                                        player.playerEquipmentN[slot]);
                                }
                                else
                                {
                                        player.getOutStream().writeByte(
                                        player.playerEquipmentN[slot]);
                                }
                                player.getOutStream().endFrameVarSizeWord();
                                player.flushOutStream();
                        }
                }
        }

        /**
         * Remove Item from equipment tab
         **/
        public static void removeItem(Player player, int wearID, int slot) {
        	if (player.doingAction(false) || player.getDoingAgility() || player.isTeleporting()) {
        		return;
        	}
        	boolean nexEffectChanged = false;
        	if (player.usedGmaul) {
        		player.usedGmaul = false;
        	} else {
        		player.getCombat().resetPlayerAttack();
        	} synchronized(player) {
        		if (player.getOutStream() != null && player != null) {
        			if (player.playerEquipment[slot] > -1) {
        				if (ItemData.data[wearID].degradable) {
        					player.chargeAdd = true;
        					player.chargesAdd = player.playerEquipmentC[slot];
        				}
        				if (addItem(player, player.playerEquipment[slot], player.playerEquipmentN[slot])) {
        					if (slot == 0 || slot == 4 || slot == 7) {
        						if (player.hasNexItems()) {
        							nexEffectChanged = true;
        						}
        					}
        					if (player.playerEquipment[slot] == 10501 && !player.inWilderness()) {
        						AreaInterface.startInterfaceEvent(player);
        					}
        					player.switches++;
        					player.playerEquipment[slot] = -1;
        					player.playerEquipmentN[slot] = 0;
        					player.playerEquipmentC[slot] = -1;
        					updateCombatInterface(player, player.playerEquipment[Constants.WEAPON_SLOT], getItemName(player.playerEquipment[Constants.WEAPON_SLOT]));
        					player.setInventoryUpdate(true);
        					player.getCombat().getPlayerAnimIndex(getItemName(player.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
        					player.getOutStream().createFrame(34);
        					player.getOutStream().writeWord(6);
        					player.getOutStream().writeWord(1688);
        					player.getOutStream().writeByte(slot);
        					player.getOutStream().writeWord(0);
        					player.getOutStream().writeByte(0);
        					player.flushOutStream();
        					player.updateRequired = true;
        					player.setAppearanceUpdateRequired(true);
        					if (nexEffectChanged && player.skillLevel[3] > player.calculateMaxLifePoints()) {
        						player.skillLevel[3] = player.calculateMaxLifePoints();
        						player.getPA().refreshSkill(3);
        					}
        				}
        			}
        		}
        	}
        }

        /**
         * BANK
         */

        public static void itemOnInterface(Player player, int id, int amount)
        {
                // synchronized(c) {
                player.getOutStream().createFrameVarSizeWord(53);
                player.getOutStream().writeWord(2274);
                player.getOutStream().writeWord(1);
                if (amount > 254)
                {
                        player.getOutStream().writeByte(255);
                        player.getOutStream().writeDWord_v2(amount);
                }
                else
                {
                        player.getOutStream().writeByte(amount);
                }
                player.getOutStream().writeWordBigEndianA(id);
                player.getOutStream().endFrameVarSizeWord();
                player.flushOutStream();
                // }
        }

        public static void itemOnInterfaces(Player player, int interfaceChild, int zoom, int itemId)
        {
                if (player.getOutStream() != null && player != null)
                {
                        player.outStream.createFrame(246);
                        player.outStream.writeWordBigEndian(interfaceChild);
                        player.outStream.writeWord(zoom);
                        player.outStream.writeWord(itemId);
                        player.flushOutStream();
                }
        }

        public static void sendFrame34(Player player, int item, int amount, int slot, int frame)
        {
                player.getOutStream().createFrameVarSizeWord(34);
                player.getOutStream().writeWord(frame);
                player.getOutStream().writeByte(slot);
                player.getOutStream().writeWord(item + 1);
                player.getOutStream().writeByte(255);
                player.getOutStream().writeDWord(amount);
                player.getOutStream().endFrameVarSizeWord();
        }

        

       

        public static void resetTempItems(Player player)
        {
                // synchronized(c) {
                int itemCount = 0;
                for (int i = 0; i < player.playerItems.length; i++)
                {
                        if (player.playerItems[i] > -1)
                        {
                                itemCount = i;
                        }
                }
                player.getOutStream().createFrameVarSizeWord(53);
                player.getOutStream().writeWord(5064);
                player.getOutStream().writeWord(itemCount + 1);
                for (int i = 0; i < itemCount + 1; i++)
                {
                        if (player.playerItemsN[i] > 254)
                        {
                                player.getOutStream().writeByte(255);
                                player.getOutStream().writeDWord_v2(player.playerItemsN[i]);
                        }
                        else
                        {
                                if (player.playerItemsN[i] != -1) player.getOutStream().writeByte(player.playerItemsN[i]);
                        }
                        if (player.playerItems[i] > Constants.MAX_ITEM_ID || player.playerItems[i] < 0)
                        {
                                player.playerItems[i] = Constants.MAX_ITEM_ID;
                        }
                        player.getOutStream().writeWordBigEndianA(player.playerItems[i]);
                }
                player.getOutStream().endFrameVarSizeWord();
                player.flushOutStream();
        }

        public static int itemAmount(Player player, int itemID)
        {
                int tempAmount = 0;
                for (int i = 0; i < player.playerItems.length; i++)
                {
                        if (player.playerItems[i] == itemID)
                        {
                                tempAmount += player.playerItemsN[i];
                        }
                }
                return tempAmount;
        }

        public static boolean isStackable(int itemID)
        {
                return ItemData.data[itemID].stackable;
        }

        /**
         * Update the equipment interface to show the items in the slots.
         */
        public static void updateEquipmentInterface(Player player)
        {
                setEquipment(player, player.playerEquipment[Constants.HEAD_SLOT], 1, Constants.HEAD_SLOT);
                setEquipment(player, player.playerEquipment[Constants.CAPE_SLOT], 1, Constants.CAPE_SLOT);
                setEquipment(player, player.playerEquipment[Constants.AMULET_SLOT], 1, Constants.AMULET_SLOT);
                setEquipment(player, player.playerEquipment[Constants.ARROW_SLOT], player.playerEquipmentN[Constants.ARROW_SLOT], Constants.ARROW_SLOT);
                setEquipment(player, player.playerEquipment[Constants.TORSO_SLOT], 1, Constants.TORSO_SLOT);
                setEquipment(player, player.playerEquipment[Constants.SHIELD_SLOT], 1, Constants.SHIELD_SLOT);
                setEquipment(player, player.playerEquipment[Constants.LEG_SLOT], 1, Constants.LEG_SLOT);
                setEquipment(player, player.playerEquipment[Constants.HAND_SLOT], 1, Constants.HAND_SLOT);
                setEquipment(player, player.playerEquipment[Constants.FEET_SLOT], 1, Constants.FEET_SLOT);
                setEquipment(player, player.playerEquipment[Constants.RING_SLOT], 1, Constants.RING_SLOT);
                setEquipment(player, player.playerEquipment[Constants.WEAPON_SLOT], player.playerEquipmentN[Constants.WEAPON_SLOT], Constants.WEAPON_SLOT);
        }

        /**
         * Update Equip tab
         **/
        public static void setEquipment(Player player, int wearID, int amount, int targetSlot)
        {
                synchronized(player)
                {
                        player.getOutStream().createFrameVarSizeWord(34);
                        player.getOutStream().writeWord(1688);
                        player.getOutStream().writeByte(targetSlot);
                        player.getOutStream().writeWord(wearID + 1);
                        if (amount > 254)
                        {
                                player.getOutStream().writeByte(255);
                                player.getOutStream().writeDWord(amount);
                        }
                        else
                        {
                                player.getOutStream().writeByte(amount);
                        }
                        player.getOutStream().endFrameVarSizeWord();
                        player.flushOutStream();
                        player.playerEquipment[targetSlot] = wearID;
                        player.playerEquipmentN[targetSlot] = amount;
                        player.updateRequired = true;
                        player.setAppearanceUpdateRequired(true);
                }
        }


        /**
         * Move Items
         **/
        public static void moveItems(Player player, int from, int to, int moveWindow, byte insert)
        {
                if (moveWindow == 3214)
                {
                        int tempI;
                        int tempN;
                        int tempC;
                        tempI = player.playerItems[from];
                        tempN = player.playerItemsN[from];
                        tempC = player.playerItemsC[from];
                        player.playerItems[from] = player.playerItems[to];
                        player.playerItemsN[from] = player.playerItemsN[to];
                        player.playerItemsC[from] = player.playerItemsC[to];
                        player.playerItems[to] = tempI;
                        player.playerItemsN[to] = tempN;
                        player.playerItemsC[to] = tempC;
                }
                if (moveWindow == 18579 || moveWindow == 5064)
                {
                        int tempI;
                        int tempN;
                        int tempC;
                        tempI = player.playerItems[from];
                        tempN = player.playerItemsN[from];
                        tempC = player.playerItemsC[from];
                        player.playerItems[from] = player.playerItems[to];
                        player.playerItemsN[from] = player.playerItemsN[to];
                        player.playerItemsC[from] = player.playerItemsC[to];
                        player.playerItems[to] = tempI;
                        player.playerItemsN[to] = tempN;
                        player.playerItemsC[to] = tempC;
                        updateInventory(player);
                }
                resetTempItems(player);
                if (moveWindow == 3214)
                {
                        updateInventory(player);
                }
        }

        /**
         * delete Item
         **/
        public static void deleteEquipment(Player player, int i, int j)
        {
                synchronized(player)
                {
                        if (PlayerHandler.players[player.playerId] == null)
                        {
                                return;
                        }
                        if (i < 0)
                        {
                                return;
                        }
                        player.playerEquipment[j] = -1;
                        player.playerEquipmentN[j] = player.playerEquipmentN[j] - 1;
                        player.getOutStream().createFrame(34);
                        player.getOutStream().writeWord(6);
                        player.getOutStream().writeWord(1688);
                        player.getOutStream().writeByte(j);
                        player.getOutStream().writeWord(0);
                        player.getOutStream().writeByte(0);
                        if (j == Constants.WEAPON_SLOT)
                        {
                                updateCombatInterface(player, -1, "Unarmed");
                        }
                        player.setInventoryUpdate(true);
                        player.updateRequired = true;
                        player.setAppearanceUpdateRequired(true);
                }
        }

        public static void deleteItem(Player player, int id, int amount)
        {
                if (id <= 0) return;
                for (int j = 0; j < player.playerItems.length; j++)
                {
                        if (amount <= 0) break;
                        if (player.playerItems[j] == id + 1)
                        {
                                player.playerItems[j] = 0;
                                player.playerItemsN[j] = 0;
                                player.playerItemsC[j] = -1;
                                amount--;
                        }
                }
                updateInventory(player);
        }

        public static void deleteItem1(Player player, int id, int amount)
        {
                deleteItem(player, id, getItemSlot(player, id), amount);
        }

        public static void deleteItem(Player player, int id, int slot, int amount)
        {
                if (id <= 0 || slot < 0)
                {
                        return;
                }
                if (player.playerItems[slot] == (id + 1))
                {
                        if (player.playerItemsN[slot] > amount)
                        {
                                player.playerItemsN[slot] -= amount;
                        }
                        else
                        {
                                player.playerItemsN[slot] = 0;
                                player.playerItems[slot] = 0;
                                player.playerItemsC[slot] = -1;
                        }
                        updateInventory(player);
                }
        }

        /**
         * Delete Arrows
         **/
        public static void deleteArrow(Player player)
        {
                synchronized(player)
                {
                        if (player.playerEquipment[Constants.CAPE_SLOT] == 10499 && Misc.random(5) != 1 && player.playerEquipment[Constants.ARROW_SLOT] != 4740 && player.playerEquipment[Constants.ARROW_SLOT] != 15243)
                        {
                                return;
                        }
                        if (player.playerEquipmentN[Constants.ARROW_SLOT] == 1)
                        {
                                deleteEquipment(player, player.playerEquipment[Constants.ARROW_SLOT], Constants.ARROW_SLOT);
                        }
                        if (player.playerEquipmentN[Constants.ARROW_SLOT] != 0)
                        {
                                player.getOutStream().createFrameVarSizeWord(34);
                                player.getOutStream().writeWord(1688);
                                player.getOutStream().writeByte(Constants.ARROW_SLOT);
                                player.getOutStream().writeWord(player.playerEquipment[Constants.ARROW_SLOT] + 1);
                                if (player.playerEquipmentN[Constants.ARROW_SLOT] - 1 > 254)
                                {
                                        player.getOutStream().writeByte(255);
                                        player.getOutStream().writeDWord(player.playerEquipmentN[Constants.ARROW_SLOT] - 1);
                                }
                                else
                                {
                                        player.getOutStream().writeByte(player.playerEquipmentN[Constants.ARROW_SLOT] - 1);
                                }
                                player.getOutStream().endFrameVarSizeWord();
                                player.flushOutStream();
                                player.playerEquipmentN[Constants.ARROW_SLOT] -= 1;
                        }
                        player.updateRequired = true;
                        player.setAppearanceUpdateRequired(true);
                }
        }

        public static void deleteEquipment(Player player)
        {
                synchronized(player)
                {
                        if (player.playerEquipmentN[Constants.WEAPON_SLOT] == 1)
                        {
                                deleteEquipment(player, player.playerEquipment[Constants.WEAPON_SLOT], Constants.WEAPON_SLOT);
                        }
                        if (player.playerEquipmentN[Constants.WEAPON_SLOT] != 0)
                        {
                                player.getOutStream().createFrameVarSizeWord(34);
                                player.getOutStream().writeWord(1688);
                                player.getOutStream().writeByte(Constants.WEAPON_SLOT);
                                player.getOutStream().writeWord(player.playerEquipment[Constants.WEAPON_SLOT] + 1);
                                if (player.playerEquipmentN[Constants.WEAPON_SLOT] - 1 > 254)
                                {
                                        player.getOutStream().writeByte(255);
                                        player.getOutStream().writeDWord(player.playerEquipmentN[Constants.WEAPON_SLOT] - 1);
                                }
                                else
                                {
                                        player.getOutStream().writeByte(player.playerEquipmentN[Constants.WEAPON_SLOT] - 1);
                                }
                                player.getOutStream().endFrameVarSizeWord();
                                player.flushOutStream();
                                player.playerEquipmentN[Constants.WEAPON_SLOT] -= 1;
                        }
                        player.updateRequired = true;
                        player.setAppearanceUpdateRequired(true);
                }
        }

        /**
         * Dropping Arrows
         **/
        public static void dropArrowNpc(Player player)
        {
                if (player.playerEquipment[Constants.CAPE_SLOT] == 10499) return;
                int enemyX = NPCHandler.npcs[player.oldNpcIndex].getX();
                int enemyY = NPCHandler.npcs[player.oldNpcIndex].getY();
                if (Misc.random(10) >= 4)
                {
                        if (Server.itemHandler.itemAmount(player.rangeItemUsed, enemyX, enemyY) == 0)
                        {
                                Server.itemHandler.createGroundItem((Client) player, player.rangeItemUsed, enemyX, enemyY, 1, player.getId(), false);
                        }
                        else if (Server.itemHandler.itemAmount(player.rangeItemUsed, enemyX, enemyY) != 0)
                        {
                                int amount = Server.itemHandler.itemAmount(
                                player.rangeItemUsed, enemyX, enemyY);
                                Server.itemHandler.removeGroundItem((Client) player, player.rangeItemUsed, enemyX, enemyY, false);
                                Server.itemHandler.createGroundItem((Client) player, player.rangeItemUsed, enemyX, enemyY, amount + 1, player.getId(), false);
                        }
                }
        }

        public static void dropArrowPlayer(Player player)
        {
                int enemyX = PlayerHandler.players[player.oldPlayerIndex].getX();
                int enemyY = PlayerHandler.players[player.oldPlayerIndex].getY();
                if (player.playerEquipment[Constants.CAPE_SLOT] == 10499 && player.playerEquipment[Constants.WEAPON_SLOT] != 13883 && player.playerEquipment[Constants.WEAPON_SLOT] != 13879)
                {
                        return;
                }
                if (Misc.random(10) >= 4)
                {
                        if (Server.itemHandler.itemAmount(player.rangeItemUsed, enemyX, enemyY) == 0)
                        {
                                Server.itemHandler.createGroundItem((Client) player, player.rangeItemUsed, enemyX, enemyY, 1, player.getId(), false);
                        }
                        else if (Server.itemHandler.itemAmount(player.rangeItemUsed, enemyX, enemyY) != 0)
                        {
                                int amount = Server.itemHandler.itemAmount(player.rangeItemUsed, enemyX, enemyY);
                                Server.itemHandler.removeGroundItem((Client) player, player.rangeItemUsed, enemyX, enemyY, false);
                                Server.itemHandler.createGroundItem((Client) player, player.rangeItemUsed, enemyX, enemyY, amount + 1, player.getId(), false);
                        }
                }
        }

        public static void removeAllItems(Player player)
        {
                for (int i = 0; i < player.playerItems.length; i++)
                {
                        player.playerItems[i] = 0;
                }
                for (int i = 0; i < player.playerItemsN.length; i++)
                {
                        player.playerItemsN[i] = 0;
                }
                updateInventory(player);
        }

        public static int freeSlots(Player player)
        {
                int freeS = 0;
                for (int i = 0; i < player.playerItems.length; i++)
                {
                        if (player.playerItems[i] <= 0)
                        {
                                freeS++;
                        }
                }
                return freeS;
        }

        public static String getItemName(int ItemID) {
        	if (ItemID >= 0 && ItemData.data[ItemID] != null) {
        		return ItemData.data[ItemID].getName();
        	} else {
        		return "Unarmed";
        	}
        }

        public static int getItemId(String itemName) {
        	for (int i = 0; i < ItemData.data.length; i++) {
        		if (ItemData.data[i] != null) {
        			if (ItemData.data[i].getName().equalsIgnoreCase(itemName)) {
        				return ItemData.data[i].getId();
        			}
        		}
        	}
        	return -1;
        }

        public static int getItemSlot(Player player, int ItemID)
        {
                for (int i = 0; i < player.playerItems.length; i++)
                {
                        if ((player.playerItems[i] - 1) == ItemID)
                        {
                                return i;
                        }
                }
                return -1;
        }

        public static int getItemAmount(Player player, int ItemID)
        {
                int itemCount = 0;
                for (int i = 0; i < player.playerItems.length; i++)
                {
                        if ((player.playerItems[i] - 1) == ItemID)
                        {
                                itemCount += player.playerItemsN[i];
                        }
                }
                return itemCount;
        }

        public static boolean playerHasItem(Player player, int itemID, int amt, int slot)
        {
                itemID++;
                int found = 0;
                if (player.playerItems[slot] == (itemID))
                {
                        for (int i = 0; i < player.playerItems.length; i++)
                        {
                                if (player.playerItems[i] == itemID)
                                {
                                        if (player.playerItemsN[i] >= amt)
                                        {
                                                return true;
                                        }
                                        else
                                        {
                                                found++;
                                        }
                                }
                        }
                        if (found >= amt)
                        {
                                return true;
                        }
                        return false;
                }
                return false;
        }

        public static boolean playerHasItem(Player player, int itemID)
        {
                itemID++;
                for (int i = 0; i < player.playerItems.length; i++)
                {
                        if (player.playerItems[i] == itemID) return true;
                }
                return false;
        }

        public static boolean playerHasItem(Player player, int itemID, int amt)
        {
                itemID++;
                int found = 0;
                for (int i = 0; i < player.playerItems.length; i++)
                {
                        if (player.playerItems[i] == itemID)
                        {
                                if (player.playerItemsN[i] >= amt)
                                {
                                        return true;
                                }
                                else
                                {
                                        found++;
                                }
                        }
                }
                if (found >= amt)
                {
                        return true;
                }
                return false;
        }

        public static int getUnnotedItem(int ItemID) {
        	int NewID = ItemID - 1;
        	String NotedName = "";
        	for (int i = 0; i < ItemData.data.length; i++) {
        		if (ItemData.data[i] != null) {
        			if (ItemData.data[i].getId() == ItemID) {
        				NotedName = ItemData.data[i].getName();
        			}
        		}
        	}
        	for (int i = 0; i < ItemData.data.length; i++)  {
        		if (ItemData.data[i] != null) {
        			if (ItemData.data[i].getName() == NotedName) {
        				if (ItemData.data[i].getDescription().startsWith("Swap this note at any bank for a") == false) {
        					NewID = ItemData.data[i].getId();
        					break;
        				}
        			}
        		}
        	}
        	return NewID;
        }

        /**
         * Drop Item
         **/
        public static void createGroundItem(Player player, int itemID, int itemX, int itemY, int itemAmount)
        {
                synchronized(player)
                {
                        player.getOutStream().createFrame(85);
                        player.getOutStream().writeByteC((itemY - 8 * player.mapRegionY));
                        player.getOutStream().writeByteC((itemX - 8 * player.mapRegionX));
                        player.getOutStream().createFrame(44);
                        player.getOutStream().writeWordBigEndianA(itemID);
                        player.getOutStream().writeWord(itemAmount);
                        player.getOutStream().writeByte(0);
                        player.flushOutStream();
                }
        }

        /**
         * Pickup Item
         **/
        public static void removeGroundItem(Player player, int itemID, int itemX, int itemY, int Amount)
        {
                synchronized(player)
                {
                        player.getOutStream().createFrame(85);
                        player.getOutStream().writeByteC((itemY - 8 * player.mapRegionY));
                        player.getOutStream().writeByteC((itemX - 8 * player.mapRegionX));
                        player.getOutStream().createFrame(156);
                        player.getOutStream().writeByteS(0);
                        player.getOutStream().writeWord(itemID);
                        player.flushOutStream();
                }
        }

        public static int[][] brokenBarrows =
        {
                {
                        4708, 4860
                }, {
                        4710, 4866
                }, {
                        4712, 4872
                }, {
                        4714, 4878
                }, {
                        4716, 4884
                }, {
                        4720, 4896
                }, {
                        4718, 4890
                }, {
                        4720, 4896
                }, {
                        4722, 4902
                }, {
                        4732, 4932
                }, {
                        4734, 4938
                }, {
                        4736, 4944
                }, {
                        4738, 4950
                }, {
                        4724, 4908
                }, {
                        4726, 4914
                }, {
                        4728, 4920
                }, {
                        4730, 4926
                }, {
                        4745, 4956
                }, {
                        4747, 4962
                }, {
                        4749, 4968
                }, {
                        4751, 4974
                }, {
                        4753, 4980
                }, {
                        4755, 4986
                }, {
                        4757, 4992
                }, {
                        4759, 4998
                }
        };

        public static void deleteItem2(Player player, int id, int amount)
        {
                int am = amount;
                for (int i = 0; i < player.playerItems.length; i++)
                {
                        if (am == 0)
                        {
                                break;
                        }
                        if (player.playerItems[i] == (id + 1))
                        {
                                if (player.playerItemsN[i] > amount)
                                {
                                        player.playerItemsN[i] -= amount;
                                        break;
                                }
                                else
                                {
                                        player.playerItems[i] = 0;
                                        player.playerItemsN[i] = 0;
                                        am--;
                                }
                        }
                }
                updateInventory(player);
        }

        public static void deleteItem3(Player player, int id, int amount)
        {
                if (id <= 0) return;
                for (int j = 0; j < player.playerItems.length; j++)
                {
                        if (amount <= 0) break;
                        if (player.playerItems[j] == id + 1)
                        {
                                player.playerItems[j] = 0;
                                player.playerItemsN[j] = 0;
                                amount--;
                        }
                }
        }

        /**
         * Item kept on death
         **/
        public static void keepItem(Player player, int keepItem, boolean deleteItem)
        {
                int value = 0;
                int item = 0;
                int slotId = 0;
                boolean itemInInventory = false;
                for (int i = 0; i < player.playerItems.length; i++)
                {
                        if (player.playerItems[i] - 1 > 0)
                        {
                                int inventoryItemValue = getItemShopValue(player.playerItems[i] - 1);

                                if (inventoryItemValue > value && (!player.invSlot[i] || isStackable(player.playerItems[i] - 1)))
                                {
                                        value = inventoryItemValue;
                                        item = player.playerItems[i] - 1;
                                        slotId = i;
                                        itemInInventory = true;
                                }
                        }
                }
                for (int i1 = 0; i1 < player.playerEquipment.length; i1++)
                {
                        if (player.playerEquipment[i1] > 0)
                        {
                                int equipmentItemValue = getItemShopValue(player.playerEquipment[i1]);
                                if (equipmentItemValue > value && (!player.equipSlot[i1] || isStackable(player.playerEquipment[i1] - 1)))
                                {
                                        value = equipmentItemValue;
                                        item = player.playerEquipment[i1];
                                        slotId = i1;
                                        itemInInventory = false;
                                }
                        }
                }
                if (itemInInventory)
                {
                        player.invSlot[slotId] = true;
                        if (deleteItem) deleteItem(player, player.playerItems[slotId] - 1, getItemSlot(player, player.playerItems[slotId] - 1), 1);
                }
                else
                {
                        player.equipSlot[slotId] = true;
                        if (deleteItem) deleteEquipment(player, item, slotId);
                }
                player.itemKeptId[keepItem] = item;
        }

        /**
         * Reset items kept on death
         **/
        public static void resetKeepItems(Player player)
        {
                for (int i = 0; i < player.itemKeptId.length; i++)
                {
                        player.itemKeptId[i] = -1;
                }
                for (int i1 = 0; i1 < player.invSlot.length; i1++)
                {
                        player.invSlot[i1] = false;
                }
                for (int i2 = 0; i2 < player.equipSlot.length; i2++)
                {
                        player.equipSlot[i2] = false;
                }
        }
}