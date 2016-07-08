package ionic.item;

import ionic.player.Client;
import ionic.player.content.miscellaneous.*;
import ionic.player.content.skills.crafting.GemCutting;
import ionic.player.content.skills.crafting.JewelryMaking;
import ionic.player.content.skills.farming.Farming;
import ionic.player.content.skills.herblore.FinishingPotions;
import ionic.player.content.skills.herblore.UnfinishedPotions;
import ionic.player.content.skills.smithing.Smithing;
import ionic.player.content.skills.smithing.SmithingInterface;



public class UseItem {

    public static void ItemonObject(Client player, int objectID, int objectX, int objectY, int itemId) {
        if (!ItemAssistant.playerHasItem(player, itemId, 1))
            return;
        if (Farming.prepareCrop(player, itemId, objectID, objectX, objectY)) { return; }
        player.turnPlayerTo(player.objectX, player.objectY);
        switch (objectID) {
        case 2783:
            SmithingInterface.showSmithInterface(player, itemId);
            break;
        case 11666:
        case 2781:
        	if (itemId == 2357) {
    			JewelryMaking.jewelryInterface(player);
    			return;
    		}
        	if (itemId == 2353 || itemId == 4) {
        		player.stopSmithing = false;
        		Smithing.smelt(player, Smithing.smelting.CANNONBALL, 28);
        	} else {
        		Smithing.sendSmelting(player);
        	}
        	break;
        }

    }

    public static void ItemonItem(Client player, int itemUsed, int useWith) {
    	if (itemUsed == 1759 || useWith == 1759) {
			JewelryMaking.stringAmulet(player, itemUsed, useWith);
		}
    	if (itemUsed == 1755 || useWith == 1755) {
			GemCutting.cutGem(player, itemUsed, useWith);
		}
    	if ((itemUsed == 7936 || useWith == 7936) || (itemUsed == 1436 || useWith == 1436)) {
    		if (itemUsed == 6952 || useWith == 6952) {
    			EssencePouch.deposit(player);
    		}
    	}
    	if (itemUsed == 227 || useWith == 227) {
    		UnfinishedPotions.useItem(player, itemUsed, useWith);
    		return;
    	}
    	FinishingPotions.checkMix(player, itemUsed, useWith);
        if ((itemUsed == 1511 && useWith == 7329) || (itemUsed == 7329 && useWith == 1511)) {
            ItemAssistant.deleteItem(player, 1511, 1);
            ItemAssistant.deleteItem(player, 7329, 1);
            ItemAssistant.addItem(player, 7404, 1);
        }
        if ((itemUsed == 1511 && useWith == 7330) || (itemUsed == 7330 && useWith == 1511)) {
            ItemAssistant.deleteItem(player, 1511, 1);
            ItemAssistant.deleteItem(player, 7330, 1);
            ItemAssistant.addItem(player, 7405, 1);
        }
        if ((itemUsed == 1511 && useWith == 7331) || (itemUsed == 7331 && useWith == 1511)) {
            ItemAssistant.deleteItem(player, 1511, 1);
            ItemAssistant.deleteItem(player, 7331, 1);
            ItemAssistant.addItem(player, 7406, 1);
        }
        if ((itemUsed == 1511 && useWith == 10326) || (itemUsed == 10326 && useWith == 1511)) {
            ItemAssistant.deleteItem(player, 1511, 1);
            ItemAssistant.deleteItem(player, 10326, 1);
            ItemAssistant.addItem(player, 10328, 1);
        }
        if ((itemUsed == 1511 && useWith == 10327) || (itemUsed == 10327 && useWith == 1511)) {
            ItemAssistant.deleteItem(player, 1511, 1);
            ItemAssistant.deleteItem(player, 10327, 1);
            ItemAssistant.addItem(player, 10329, 1);
        }
    	if ((itemUsed == 6953 && useWith == 6952) || (itemUsed == 6952 && useWith == 6953)) {
    		EssencePouch.extend(player);
    	}
    }
    public static void ItemonNpc(Client player, int itemId, int npcId, int slot) {
        switch (itemId) {
        }

    }


}