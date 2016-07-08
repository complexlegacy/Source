package ionic.player.content.commands.handlers;

import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Client;
import ionic.player.content.commands.Command;
import core.Constants;
import core.Server;

public class DropSelectedItems implements Command {
	
	@Override
	public void processCommand(Client c, String command, int rights) {
		try {
			String[] d = command.split(" ");
			boolean[] drop = new boolean[28];
			int[] amounts = new int[28];
			for (int i = 1; i < d.length; i++) {
				String[] b = d[i].split("#");
				drop[i - 1] = b[0].equals("1") ? true : false;
				amounts[i - 1] = Integer.parseInt(b[1]);
			}
			
			for (int i = 0; i < drop.length; i++) {
				if (!drop[i]) { continue; }
				if (c.playerItems[i] <= 0) { continue; }
			if (c.doingAction(false) || c.getDoingAgility() || c.isTeleporting() || c.isDead || c.inTrade || 
        			System.currentTimeMillis() - c.alchDelay < 1800) {
        			return;
        		}
        	
                int itemId = c.playerItems[i] - 1;
                int slot = i;
                boolean droppable = true;
                
                if (amounts[slot] > c.playerItemsN[slot]) {
                	amounts[slot] = c.playerItemsN[slot];
                }
                if (amounts[slot] <= 0) {
                	amounts[slot] = c.playerItemsN[slot];
                }
                
                for (int j = 0; j < Constants.ITEMS_TO_INVENTORY_ON_DEATH.length; j++) {
                        if (itemId == Constants.ITEMS_TO_INVENTORY_ON_DEATH[j]) {
                        	c.sendMessage("You cannot drop this item.");
                        	droppable = false;
                        }
                }
                
                        if (ItemData.data[itemId].isUntradable()) {
                        	c.sendMessage("You cannot drop this item.");
                        	droppable = false;
                        }
                
                for (int j = 0; j < Constants.ITEMS_TO_DISSAPEAR.length; j++) {
                        if (itemId == Constants.ITEMS_TO_DISSAPEAR[j]) {
                        	c.sendMessage("You cannot drop this item.");
                        	droppable = false;
                        }
                }
                if (c.playerItemsN[slot] != 0 && itemId != -1 && c.playerItems[slot] == itemId + 1) {
                        if (droppable) {
                        		Server.itemHandler.createGroundItem(c, itemId, c.getX(), c.getY(), amounts[slot], c.getId(), c.inWilderness() ? true : false);
                        		ItemAssistant.deleteItemForBank(c, itemId, amounts[slot]);
                        }
                }
			}
			
		} catch (Exception e) {
		}
	}
	
}