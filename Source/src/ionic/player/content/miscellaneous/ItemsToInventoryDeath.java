package ionic.player.content.miscellaneous;

import ionic.item.ItemAssistant;
import ionic.player.Player;

import java.util.Map;

import core.Constants;

/**
 * Items sent to inventory after death.
 * @author MGT Madness, created on 08-04-2014.
 */
public class ItemsToInventoryDeath 
{
	
	/**
     * Add the items kept on death to the player's inventory.
     */
    public static void itemsToInventory(Player player)
    {
		for (Map.Entry<Integer, Integer> entry : player.itemsToInventory.entrySet()) 
		{
	     	ItemAssistant.addItem(player, entry.getKey(), entry.getValue());
		}
    }
    
    /**
     * Store the data of the items that will be sent to inventory.
     */
    public static void getItemsToInventory(Player player)
    {
        player.itemsToInventory.clear();
		for (int item = 0; item < Constants.ITEMS_TO_INVENTORY_ON_DEATH.length; item++)
        {
                int itemId = Constants.ITEMS_TO_INVENTORY_ON_DEATH[item];
                int itemAmount = ItemAssistant.getItemAmount(player, itemId) + ItemAssistant.getWornItemAmount(player, itemId);
                if (ItemAssistant.playerHasItem(player, itemId) || ItemAssistant.hasItemEquipped(player, itemId))
                {
                	player.itemsToInventory.put(itemId, itemAmount);
                }
        }
    }

}
