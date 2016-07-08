package ionic.item;

import ionic.player.Client;
import ionic.player.Player;
import ionic.player.PlayerHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles ground items
 **/

public class ItemHandler
{

    public List < GroundItem > items = new ArrayList < GroundItem > ();
    public static final int HIDE_TICKS = 100;

    public ItemHandler() {
    }

    public void load() {
        loadItemPrices("prices.txt");
    }

    /**
     * Adds item to list
     **/
    public void addItem(GroundItem item)
    {
        items.add(item);
    }

    /**
     * Removes item from list
     **/
    public void removeItem(GroundItem item)
    {
        items.remove(item);
    }

    /**
     * Item amount
     **/
    public int itemAmount(int itemId, int itemX, int itemY)
    {
        for (GroundItem i: items)
        {
            if (i.getItemId() == itemId && i.getItemX() == itemX && i.getItemY() == itemY)
            {
                return i.getItemAmount();
            }
        }
        return 0;
    }


    /**
     * Item exists
     **/
    public boolean itemExists(int itemId, int itemX, int itemY)
    {
        for (GroundItem i: items)
        {
            if (i.getItemId() == itemId && i.getItemX() == itemX && i.getItemY() == itemY)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Reloads any items if you enter a new region
     **/
    public void reloadItems(Client player)
    {
        for (GroundItem i: items)
        {
            if (player != null)
            {
                if (!ItemAssistant.itemDissapear(i.getItemId()) || i.getName().equalsIgnoreCase(player.playerName))
                {
                    if (player.distanceToPoint(i.getItemX(), i.getItemY()) <= 60)
                    {
                        if (i.hideTicks > 0 && i.getName().equalsIgnoreCase(player.playerName))
                        {
                            ItemAssistant.removeGroundItem(player, i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
                            ItemAssistant.createGroundItem(player, i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
                        }
                        if (i.hideTicks == 0)
                        {
                            ItemAssistant.removeGroundItem(player, i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
                            ItemAssistant.createGroundItem(player, i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
                        }
                    }
                }
            }
        }
    }

    public void process()
    {
        ArrayList < GroundItem > toRemove = new ArrayList < GroundItem > ();
        for (int j = 0; j < items.size(); j++)
        {
            if (items.get(j) != null)
            {
                GroundItem i = items.get(j);
                if (i.hideTicks > 0)
                {
                    i.hideTicks--;
                }
                if (i.hideTicks == 1)
                { // item can now be seen by others
                    i.hideTicks = 0;
                    createGlobalItem(i);
                    i.removeTicks = HIDE_TICKS;
                }
                if (i.removeTicks > 0)
                {
                    i.removeTicks--;
                }
                if (i.removeTicks == 1)
                {
                    i.removeTicks = 0;
                    toRemove.add(i);
                }

            }

        }

        for (int j = 0; j < toRemove.size(); j++)
        {
            GroundItem i = toRemove.get(j);
            removeGlobalItem(i, i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
        }
    }

    /**
     * Create a ground item.
     *
     * @param player
     * @param itemId
     * @param itemX
     * @param itemY
     * @param itemAmount
     * @param playerId
     * @param appearInstantly
     * 			True, to make the item appear instantly to everyone.
     */
    public void createGroundItem(Client player, int itemId, int itemX, int itemY, int itemAmount, int playerId, boolean appearInstantly)
    {
        if (itemId > 0)
        {
            if (ItemData.data[itemId] == null) {
                return;
            }
            if (ItemData.data[itemId].degraded) {
                itemId = ItemData.data[itemId].broken;
            }
            if (!ItemData.data[itemId].stackable && itemAmount > 0)
            {
                for (int j = 0; j < itemAmount; j++)
                {
                    ItemAssistant.createGroundItem(player, itemId, itemX, itemY, 1);
                    if (appearInstantly)
                    {
                        GroundItem item = new GroundItem(itemId, itemX, itemY, 1, player.playerId, 2, PlayerHandler.players[playerId].playerName);
                        addItem(item);
                    }
                    else
                    {
                        GroundItem item = new GroundItem(itemId, itemX, itemY, 1, player.playerId, HIDE_TICKS, PlayerHandler.players[playerId].playerName);
                        addItem(item);
                    }
                }
            }
            else
            {
                ItemAssistant.createGroundItem(player, itemId, itemX, itemY, itemAmount);
                if (appearInstantly)
                {
                    GroundItem item = new GroundItem(itemId, itemX, itemY, itemAmount, player.playerId, 2, PlayerHandler.players[playerId].playerName);
                    addItem(item);
                }
                else
                {
                    GroundItem item = new GroundItem(itemId, itemX, itemY, itemAmount, player.playerId, HIDE_TICKS, PlayerHandler.players[playerId].playerName);
                    addItem(item);
                }
            }
        }
    }


    /**
     * Shows items for everyone who is within 60 squares
     **/
    public void createGlobalItem(GroundItem i)
    {
        for (Player p: PlayerHandler.players)
        {
            if (p != null)
            {
                Client person = (Client) p;
                if (person != null)
                {
                    if (person.playerId != i.getItemController())
                    {
                        if (ItemAssistant.itemDissapear(i.getItemId()) && person.playerId != i.getItemController())
                        {
                            continue;
                        }
                        if (person.distanceToPoint(i.getItemX(), i.getItemY()) <= 60)
                        {
                            ItemAssistant.createGroundItem(person, i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
                        }
                    }
                }
            }
        }
    }



    /**
     * Removing the ground item
     **/

    public void removeGroundItem(Client player, int itemId, int itemX, int itemY, boolean add)
    {
        for (GroundItem i: items)
        {
            if (i.getItemId() == itemId && i.getItemX() == itemX && i.getItemY() == itemY)
            {
                if (i.hideTicks > 0 && i.getName().equalsIgnoreCase(player.playerName))
                {
                    if (add)
                    {
                        if (ItemAssistant.addItem(player, i.getItemId(), i.getItemAmount()))
                        {
                            removeControllersItem(i, player, i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
                            break;
                        }
                    }
                    else
                    {
                        removeControllersItem(i, player, i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
                        break;
                    }
                    removeControllersItem(i, player, i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
                    break;
                }
                else if (i.hideTicks <= 0)
                {
                    if (add)
                    {
                        if (ItemAssistant.addItem(player, i.getItemId(), i.getItemAmount()))
                        {
                            removeGlobalItem(i, i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
                            break;
                        }
                    }
                    else
                    {
                        removeGlobalItem(i, i.getItemId(), i.getItemX(), i.getItemY(), i.getItemAmount());
                        break;
                    }
                }
            }
        }
    }

    /**
     * Remove item for just the item controller (item not global yet)
     **/

    public void removeControllersItem(GroundItem i, Client player, int itemId, int itemX, int itemY, int itemAmount)
    {
        ItemAssistant.removeGroundItem(player, itemId, itemX, itemY, itemAmount);
        removeItem(i);
    }

    /**
     * Remove item for everyone within 60 squares
     **/

    public void removeGlobalItem(GroundItem i, int itemId, int itemX, int itemY, int itemAmount)
    {
        for (Player p: PlayerHandler.players)
        {
            if (p != null)
            {
                Client person = (Client) p;
                if (person != null)
                {
                    if (person.distanceToPoint(itemX, itemY) <= 60)
                    {
                        ItemAssistant.removeGroundItem(person, itemId, itemX, itemY, itemAmount);
                    }
                }
            }
        }
        removeItem(i);
    }

    /**
     * prices
     */
    public void loadItemPrices(String filename) {
        String[] line = null;
        ItemData temp = null;
        try {
            Scanner s = new Scanner(new File("./data/items/" + filename));
            while (s.hasNextLine()) {
                line = s.nextLine().split("  =  ");
                temp = ItemData.data[Integer.parseInt(line[0])];
                if (temp != null) {
                    temp.shopValue = Integer.parseInt(line[2]);
                    if (temp.getNoted() > 0) {
                        temp = ItemData.data[temp.getNoted()];
                        if (temp != null) {
                            temp.shopValue = Integer.parseInt(line[2]);
                        }
                    }
                }
            }
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}