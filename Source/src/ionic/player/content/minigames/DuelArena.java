package ionic.player.content.minigames;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import core.Constants;
import core.Server;
import ionic.item.GameItem;
import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.npc.pet.Pet;
import ionic.player.Client;
import ionic.player.PlayerHandler;
import utility.Misc;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Jon on 2/14/2016.
 */
public class DuelArena {
    public CopyOnWriteArrayList< GameItem > otherStakedItems = new CopyOnWriteArrayList < GameItem > ();
    public CopyOnWriteArrayList < GameItem > stakedItems = new CopyOnWriteArrayList < GameItem > ();
    private Client player;
    public DuelArena(Client Client)
    {
        this.player = Client;
    }

    public void requestDuel(int id)
    {
        try
        {
            if (id == player.playerId)
                return;
            resetDuel();
            resetDuelItems();
            player.duelingWith = id;
            Client o = (Client) PlayerHandler.players[id];
            if (o == null)
            {
                return;
            }
            player.duelRequested = true;
            if (player.duelStatus == 0 && o.duelStatus == 0 && player.duelRequested && o.duelRequested && player.duelingWith == o.getId() && o.duelingWith == player.getId())
            {
                if (player.goodDistance(player.getX(), player.getY(), o.getX(), o.getY(), 1))
                {
                    player.getDuelArena().openDuel();
                    o.getDuelArena().openDuel();
                }
                else
                {
                    player.sendMessage("You need to get closer to your opponent to start the duel.");
                }

            }
            else
            {
                player.sendMessage("Sending duel request...");
                o.sendMessage(player.playerName + ":duelreq:");
            }
        }
        catch (Exception e)
        {
            Misc.println("Error requesting duel.");
        }
    }

    public void openDuel()
    {
        Client o = (Client) PlayerHandler.players[player.duelingWith];
        if (o == null)
        {
            return;
        }
        player.duelStatus = 1;
        refreshduelRules();
        refreshDuelScreen();
        player.canOffer = true;
        for (int i = 0; i < player.playerEquipment.length; i++)
        {
            sendDuelEquipment(player.playerEquipment[i], player.playerEquipmentN[i], i);
        }
        player.getPA().sendFrame126("Dueling with: " + o.playerName + " (level-" + o.combatLevel + ")", 6671);
        player.getPA().sendFrame126("", 6684);
        player.getPA().sendFrame248(6575, 3321);
        ItemAssistant.resetItems(player, 3322);
    }

    public void sendDuelEquipment(int itemId, int amount, int slot)
    {
        synchronized(player)
        {
            if (itemId != 0)
            {
                player.getOutStream().createFrameVarSizeWord(34);
                player.getOutStream().writeWord(13824);
                player.getOutStream().writeByte(slot);
                player.getOutStream().writeWord(itemId + 1);

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
            }
        }
    }

    public void refreshduelRules()
    {
        for (int i = 0; i < player.duelRule.length; i++)
        {
            player.duelRule[i] = false;
        }
        player.getPA().sendFrame87(286, 0);
        player.duelOption = 0;
    }

    public void refreshDuelScreen()
    {
        synchronized(player)
        {
            Client o = (Client) PlayerHandler.players[player.duelingWith];
            if (o == null)
            {
                return;
            }
            player.getOutStream().createFrameVarSizeWord(53);
            player.getOutStream().writeWord(6669);
            player.getOutStream().writeWord(stakedItems.toArray().length);
            int current = 0;
            for (GameItem item: stakedItems)
            {
                if (item.amount > 254)
                {
                    player.getOutStream().writeByte(255);
                    player.getOutStream().writeDWord_v2(item.amount);
                }
                else
                {
                    player.getOutStream().writeByte(item.amount);
                }
                if (item.id > Constants.MAX_ITEM_ID || item.id < 0)
                {
                    item.id = Constants.MAX_ITEM_ID;
                }
                player.getOutStream().writeWordBigEndianA(item.id + 1);

                current++;
            }

            if (current < 27)
            {
                for (int i = current; i < 28; i++)
                {
                    player.getOutStream().writeByte(1);
                    player.getOutStream().writeWordBigEndianA(-1);
                }
            }
            player.getOutStream().endFrameVarSizeWord();
            player.flushOutStream();

            player.getOutStream().createFrameVarSizeWord(53);
            player.getOutStream().writeWord(6670);
            player.getOutStream().writeWord(o.getDuelArena().stakedItems.toArray().length);
            current = 0;
            for (GameItem item: o.getDuelArena().stakedItems)
            {
                if (item.amount > 254)
                {
                    player.getOutStream().writeByte(255);
                    player.getOutStream().writeDWord_v2(item.amount);
                }
                else
                {
                    player.getOutStream().writeByte(item.amount);
                }
                if (item.id > Constants.MAX_ITEM_ID || item.id < 0)
                {
                    item.id = Constants.MAX_ITEM_ID;
                }
                player.getOutStream().writeWordBigEndianA(item.id + 1);
                current++;
            }

            if (current < 27)
            {
                for (int i = current; i < 28; i++)
                {
                    player.getOutStream().writeByte(1);
                    player.getOutStream().writeWordBigEndianA(-1);
                }
            }
            player.getOutStream().endFrameVarSizeWord();
            player.flushOutStream();
        }
    }


    public boolean stakeItem(int itemID, int fromSlot, int amount)
    {

        if (ItemData.data[itemID].isUntradable())
        {
            player.sendMessage("You can't stake this item.");
            return false;
        }
        for (int i: Constants.ITEMS_TO_INVENTORY_ON_DEATH)
        {
            if (i == itemID)
            {
                player.sendMessage("You can't stake this item.");
                return false;
            }
        }
        for (int i: Constants.ITEMS_TO_DISSAPEAR)
        {
            if (i == itemID)
            {
                player.sendMessage("You can't stake this item.");
                return false;
            }
        }
        if (Pet.petItem(itemID))
        {
            player.sendMessage("You can't stake this item.");
            return false;
        }
        if (!ItemAssistant.playerHasItem(player, itemID, amount))
            return false;
        if (itemID == 4740 || itemID == 9244 || itemID == 11212 || itemID == 892 || itemID == 9194 || itemID == 9243 || itemID == 9242 || itemID == 9241 || itemID == 9240 || itemID == 9239 || itemID == 882 || itemID == 884 || itemID == 886 || itemID == 888 || itemID == 890)
        {
            player.sendMessage("You can't stake bolts or arrows.");
            return false;
        }
        if (!ItemAssistant.playerHasItem(player, itemID, amount))
            return false;
        if (player.playerItems[fromSlot] - 1 != itemID && player.playerItems[fromSlot] != itemID)
        { // duel dupe fix by Aleksandr
            if (player.playerItems[fromSlot] == 0)
                return false;
            return false;
        }
        if (amount <= 0)
            return false;
        Client o = (Client) PlayerHandler.players[player.duelingWith];
        if (o == null)
        {
            declineDuel();
            return false;
        }
        if (o.duelStatus <= 0 || player.duelStatus <= 0)
        {
            declineDuel();
            o.getDuelArena().declineDuel();
            return false;
        }
        if (!player.canOffer)
        {
            return false;
        }
        changeDuelStuff();
        if (!ItemData.data[itemID].stackable)
        {
            for (int a = 0; a < amount; a++)
            {
                if (ItemAssistant.playerHasItem(player, itemID, 1))
                {
                    stakedItems.add(new GameItem(itemID, 1));
                    ItemAssistant.deleteItem(player, itemID, ItemAssistant.getItemSlot(player, itemID), 1);
                }
            }
            ItemAssistant.resetItems(player, 3214);
            ItemAssistant.resetItems(player, 3322);
            ItemAssistant.resetItems(o, 3214);
            ItemAssistant.resetItems(o, 3322);
            refreshDuelScreen();
            o.getDuelArena().refreshDuelScreen();
            player.getPA().sendFrame126("", 6684);
            o.getPA().sendFrame126("", 6684);
        }

        if (!ItemAssistant.playerHasItem(player, itemID, amount))
        {
            return false;
        }
        if (ItemData.data[itemID].stackable)
        {
            boolean found = false;
            for (GameItem item: stakedItems)
            {
                if (item.id == itemID)
                {
                    found = true;
                    item.amount += amount;
                    ItemAssistant.deleteItem(player, itemID, fromSlot, amount);
                    break;
                }
            }
            if (!found)
            {
                ItemAssistant.deleteItem(player, itemID, fromSlot, amount);
                stakedItems.add(new GameItem(itemID, amount));
            }
        }

        ItemAssistant.resetItems(player, 3214);
        ItemAssistant.resetItems(player, 3322);
        ItemAssistant.resetItems(o, 3214);
        ItemAssistant.resetItems(o, 3322);
        refreshDuelScreen();
        o.getDuelArena().refreshDuelScreen();
        player.getPA().sendFrame126("", 6684);
        o.getPA().sendFrame126("", 6684);
        return true;
    }


    public boolean fromDuel(int itemID, int fromSlot, int amount)
    {
        Client o = (Client) PlayerHandler.players[player.duelingWith];
        if (o == null)
        {
            declineDuel();
            return false;
        }
        if (o.duelStatus <= 0 || player.duelStatus <= 0)
        {
            declineDuel();
            o.getDuelArena().declineDuel();
            return false;
        }
        if (ItemData.data[itemID].stackable)
        {
            if (ItemAssistant.freeSlots(player) - 1 < (player.duelSpaceReq))
            {
                player.sendMessage("You have too many rules set to remove that item.");
                return false;
            }
        }

        if (!player.canOffer)
        {
            return false;
        }

        changeDuelStuff();
        boolean goodSpace = true;
        if (!ItemData.data[itemID].stackable)
        {
            for (int a = 0; a < amount; a++)
            {
                for (GameItem item: stakedItems)
                {
                    if (item.id == itemID)
                    {
                        if (!item.stackable)
                        {
                            if (ItemAssistant.freeSlots(player) - 1 < (player.duelSpaceReq))
                            {
                                goodSpace = false;
                                break;
                            }
                            stakedItems.remove(item);
                            ItemAssistant.addItem(player, itemID, 1);
                        }
                        else
                        {
                            if (ItemAssistant.freeSlots(player) - 1 < (player.duelSpaceReq))
                            {
                                goodSpace = false;
                                break;
                            }
                            if (item.amount > amount)
                            {
                                item.amount -= amount;
                                ItemAssistant.addItem(player, itemID, amount);
                            }
                            else
                            {
                                if (ItemAssistant.freeSlots(player) - 1 < (player.duelSpaceReq))
                                {
                                    goodSpace = false;
                                    break;
                                }
                                amount = item.amount;
                                stakedItems.remove(item);
                                ItemAssistant.addItem(player, itemID, amount);
                            }
                        }
                        break;
                    }
                    o.duelStatus = 1;
                    player.duelStatus = 1;
                    ItemAssistant.resetItems(player, 3214);
                    ItemAssistant.resetItems(player, 3322);
                    ItemAssistant.resetItems(o, 3214);
                    ItemAssistant.resetItems(o, 3322);
                    player.getDuelArena().refreshDuelScreen();
                    o.getDuelArena().refreshDuelScreen();
                    o.getPA().sendFrame126("", 6684);
                }
            }
        }

        for (GameItem item: stakedItems)
        {
            if (item.id == itemID)
            {
                if (!item.stackable)
                {}
                else
                {
                    if (item.amount > amount)
                    {
                        item.amount -= amount;
                        ItemAssistant.addItem(player, itemID, amount);
                    }
                    else
                    {
                        amount = item.amount;
                        stakedItems.remove(item);
                        ItemAssistant.addItem(player, itemID, amount);
                    }
                }
                break;
            }
        }
        o.duelStatus = 1;
        player.duelStatus = 1;
        ItemAssistant.resetItems(player, 3214);
        ItemAssistant.resetItems(player, 3322);
        ItemAssistant.resetItems(o, 3214);
        ItemAssistant.resetItems(o, 3322);
        player.getDuelArena().refreshDuelScreen();
        o.getDuelArena().refreshDuelScreen();
        o.getPA().sendFrame126("", 6684);
        if (!goodSpace)
        {
            player.sendMessage("You have too many rules set to remove that item.");
            return true;
        }
        return true;
    }

    public void confirmDuel()
    {
        Client o = (Client) PlayerHandler.players[player.duelingWith];
        if (o == null)
        {
            declineDuel();
            return;
        }
        String itemId = "";
        for (GameItem item: stakedItems)
        {
            if (ItemData.data[item.id].stackable)
            {
                itemId += ItemAssistant.getItemName(item.id) + " x " + Misc.format(item.amount) + "\\n";
            }
            else
            {
                itemId += ItemAssistant.getItemName(item.id) + "\\n";
            }
        }
        player.getPA().sendFrame126(itemId, 6516);
        itemId = "";
        for (GameItem item: o.getDuelArena().stakedItems)
        {
            if (ItemData.data[item.id].stackable)
            {
                itemId += ItemAssistant.getItemName(item.id) + " x " + Misc.format(item.amount) + "\\n";
            }
            else
            {
                itemId += ItemAssistant.getItemName(item.id) + "\\n";
            }
        }
        player.getPA().sendFrame126(itemId, 6517);
        player.getPA().sendFrame126("", 8242);
        for (int i = 8238; i <= 8253; i++)
        {
            player.getPA().sendFrame126("", i);
        }
        player.getPA().sendFrame126("Hitpoints will be restored.", 8250);
        player.getPA().sendFrame126("Boosted stats will be restored.", 8238);
        if (player.duelRule[8])
        {
            player.getPA().sendFrame126("There will be obstacles in the arena.", 8239);
        }
        player.getPA().sendFrame126("", 8240);
        player.getPA().sendFrame126("", 8241);

        String[] rulesOption = {
                "Players cannot forfeit!", "Players cannot move.", "Players cannot use range.", "Players cannot use melee.", "Players cannot use magic.", "Players cannot drink pots.", "Players cannot eat food.", "Players cannot use prayer."
        };

        int lineNumber = 8242;
        for (int i = 0; i < 8; i++)
        {
            if (player.duelRule[i])
            {
                player.getPA().sendFrame126("" + rulesOption[i], lineNumber);
                lineNumber++;
            }
        }
        player.getPA().sendFrame126("", 6571);
        player.getPA().sendFrame248(6412, 197);
        //c.getPA().showInterface(6412);
    }


    @
            SuppressWarnings("unused")
    public void startDuel()
    {
        player.freezeTimer = 2;
        player.getPA().resetFollow();
        Client o = (Client) PlayerHandler.players[player.duelingWith];
        if (o.disconnected)
        {
            duelVictory();
        }
        if (o == null)
        {
            duelVictory();
        }
        player.headIconHints = 2;
        player.vengOn = false;

        if (player.duelRule[7])
        {
            for (int p = 0; p < player.PRAYER.length; p++)
            { // reset prayer glows
                player.prayerActive[p] = false;
                player.getPA().sendFrame36(player.PRAYER_GLOW[p], 0);
            }
            player.headIcon = -1;
            player.getPA().requestUpdates();
        }
        if (player.duelRule[9]) {

        }
        if (player.duelRule[11])
        {
            ItemAssistant.removeItem(player, player.playerEquipment[0], 0);
        }
        if (player.duelRule[12])
        {
            ItemAssistant.removeItem(player, player.playerEquipment[1], 1);
        }
        if (player.duelRule[13])
        {
            ItemAssistant.removeItem(player, player.playerEquipment[2], 2);
        }
        if (player.duelRule[14])
        {
            ItemAssistant.removeItem(player, player.playerEquipment[3], 3);
        }
        if (player.duelRule[15])
        {
            ItemAssistant.removeItem(player, player.playerEquipment[4], 4);
        }
        if (player.duelRule[16])
        {
            ItemAssistant.removeItem(player, player.playerEquipment[5], 5);
        }
        if (player.duelRule[17])
        {
            ItemAssistant.removeItem(player, player.playerEquipment[7], 7);
        }
        if (player.duelRule[18])
        {
            ItemAssistant.removeItem(player, player.playerEquipment[9], 9);
        }
        if (player.duelRule[19])
        {
            ItemAssistant.removeItem(player, player.playerEquipment[10], 10);
        }
        if (player.duelRule[20])
        {
            ItemAssistant.removeItem(player, player.playerEquipment[12], 12);
        }
        if (player.duelRule[21])
        {
            ItemAssistant.removeItem(player, player.playerEquipment[13], 13);
        }
        player.duelStatus = 5;
        player.getPA().removeAllWindows();
        player.specAmount = 10;
        ItemAssistant.addSpecialBar(player, player.playerEquipment[Constants.WEAPON_SLOT]);

        if (player.duelRule[8])
        {
            if (player.duelRule[1])
            {
                player.getPA().movePlayer(player.duelTeleX, player.duelTeleY, 0);
            }
            else
            {
                player.getPA().movePlayer(3366 + Misc.random(12), 3246 + Misc.random(6), 0);
            }
        }
        else
        {
            if (player.duelRule[1])
            {
                player.getPA().movePlayer(player.duelTeleX, player.duelTeleY, 0);
            }
            else
            {
                player.getPA().movePlayer(3335 + Misc.random(12), 3246 + Misc.random(6), 0);
            }
        }

        player.getPA().createPlayerHints(10, o.playerId);
        player.getPA().showOption(3, 0, "Attack", 1);
        for (int i = 0; i < 20; i++)
        {
            player.skillLevel[i] = player.getPA().getLevelForXP(player.playerXP[i]);
            player.getPA().refreshSkill(i);
        }
        for (GameItem item: o.getDuelArena().stakedItems)
        {
            otherStakedItems.add(new GameItem(item.id, item.amount));
        }
        player.getPA().requestUpdates();
    }


    public void duelVictory()
    {
        Client o = (Client) PlayerHandler.players[player.duelingWith];
        if (o != null)
        {
            player.getPA().sendFrame126("" + o.combatLevel, 6839);
            player.getPA().sendFrame126(o.playerName, 6840);
            o.duelStatus = 0;
            player.freezeTimer = 3;
        }
        else
        {
            player.getPA().sendFrame126("", 6839);
            player.getPA().sendFrame126("", 6840);
        }
        player.duelStatus = 6;
        player.getCombat().resetPrayers();
        for (int i = 0; i < 20; i++)
        {
            player.skillLevel[i] = player.getPA().getLevelForXP(player.playerXP[i]);
            player.getPA().refreshSkill(i);
        }
        player.getPA().refreshSkill(3);
        //c.getPA().refreshSkill(i);
        player.specAmount = 10;
        ItemAssistant.addSpecialBar(player, player.playerEquipment[Constants.WEAPON_SLOT]);
        duelRewardInterface();
        player.getPA().showInterface(6733);
        player.getPA().movePlayer(3362, 3263, 0);
        player.getPA().requestUpdates();
        player.getPA().showOption(3, 0, "Challenge", 3);
        player.getPA().createPlayerHints(10, -1);
        player.canOffer = true;
        player.duelSpaceReq = 0;
        player.duelingWith = 0;
        player.freezeTimer = 1;
        player.getPA().resetFollow();
        player.getCombat().resetPlayerAttack();
        player.duelRequested = false;
    }


    public void duelRewardInterface()
    {
        synchronized(player)
        {
            player.getOutStream().createFrameVarSizeWord(53);
            player.getOutStream().writeWord(6822);
            player.getOutStream().writeWord(otherStakedItems.toArray().length);
            for (GameItem item: otherStakedItems)
            {
                if (item.amount > 254)
                {
                    player.getOutStream().writeByte(255);
                    player.getOutStream().writeDWord_v2(item.amount);
                }
                else
                {
                    player.getOutStream().writeByte(item.amount);
                }
                if (item.id > Constants.MAX_ITEM_ID || item.id < 0)
                {
                    item.id = Constants.MAX_ITEM_ID;
                }
                player.getOutStream().writeWordBigEndianA(item.id + 1);
            }
            player.getOutStream().endFrameVarSizeWord();
            player.flushOutStream();
        }
    }

    public void claimStakedItems()
    {
        if (player.duelStatus != 6)
        {
            return;
        }

        for (GameItem item: otherStakedItems)
        {
            if (item.id > 0 && item.amount > 0)
            {
                if (ItemData.data[item.id].stackable)
                {
                    if (!ItemAssistant.addItem(player, item.id, item.amount))
                    {
                        Server.itemHandler.createGroundItem(player, item.id, player.getX(), player.getY(), item.amount, player.getId(), false);
                    }
                }
                else
                {
                    int amount = item.amount;
                    for (int a = 1; a <= amount; a++)
                    {
                        if (!ItemAssistant.addItem(player, item.id, 1))
                        {
                            Server.itemHandler.createGroundItem(player, item.id, player.getX(), player.getY(), 1, player.getId(), false);
                        }
                    }
                }
            }
        }
        for (GameItem item: stakedItems)
        {
            if (item.id > 0 && item.amount > 0)
            {
                if (ItemData.data[item.id].stackable)
                {
                    if (!ItemAssistant.addItem(player, item.id, item.amount))
                    {
                        Server.itemHandler.createGroundItem(player, item.id, player.getX(), player.getY(), item.amount, player.getId(), false);
                    }
                }
                else
                {
                    int amount = item.amount;
                    for (int a = 1; a <= amount; a++)
                    {
                        if (!ItemAssistant.addItem(player, item.id, 1))
                        {
                            Server.itemHandler.createGroundItem(player, item.id, player.getX(), player.getY(), 1, player.getId(), false);
                        }
                    }
                }
            }
        }
        resetDuel();
        resetDuelItems();
        player.duelStatus = 0;
    }

    public void declineDuel()
    {
        player.getPA().removeAllWindows();
        player.canOffer = true;
        player.duelStatus = 0;
        player.duelingWith = 0;
        player.duelSpaceReq = 0;
        player.duelRequested = false;
        for (GameItem item: stakedItems)
        {
            if (item.amount < 1) continue;
            if (ItemData.data[item.id].stackable)
            {
                ItemAssistant.addItem(player, item.id, item.amount);
            }
            else
            {
                ItemAssistant.addItem(player, item.id, 1);
            }
        }
        stakedItems.clear();
        for (int i = 0; i < player.duelRule.length; i++)
        {
            player.duelRule[i] = false;
        }
    }

    public void resetDuel()
    {
        player.getPA().showOption(3, 0, "Challenge", 3);
        player.headIconHints = 0;
        for (int i = 0; i < player.duelRule.length; i++)
        {
            player.duelRule[i] = false;
        }
        player.getPA().createPlayerHints(10, -1);
        player.duelStatus = 0;
        player.canOffer = true;
        player.duelSpaceReq = 0;
        player.duelingWith = 0;
        player.freezeTimer = 1;
        player.getPA().resetFollow();
        player.getPA().requestUpdates();
        player.getCombat().resetPlayerAttack();
        player.duelRequested = false;
    }

    public void resetDuelItems()
    {
        stakedItems.clear();
        otherStakedItems.clear();
    }

    public void changeDuelStuff()
    {
        Client o = (Client) PlayerHandler.players[player.duelingWith];
        if (o == null)
        {
            return;
        }
        o.duelStatus = 1;
        player.duelStatus = 1;
        o.getPA().sendFrame126("", 6684);
        player.getPA().sendFrame126("", 6684);
    }

    /**
     * Remove player from the arena
     */
    public void removeFromArena()
    {
        if (player.arenas() && player.duelStatus != 5)
        {
            player.getPA().movePlayer(Constants.DUEL_ARENA_X + (Misc.random(Constants.RANDOM_DISTANCE)), Constants.DUEL_ARENA_Y + (Misc.random(Constants.RANDOM_DISTANCE)), 0);
        }
    }


    public void selectRule(int i)
    { // rules
        Client o = (Client) PlayerHandler.players[player.duelingWith];
        if (o == null)
        {
            return;
        }
        if (!player.canOffer)
            return;
        changeDuelStuff();
        o.duelSlot = player.duelSlot;
        if (i >= 11 && player.duelSlot > -1)
        {
            if (player.playerEquipment[player.duelSlot] > 0)
            {
                if (!player.duelRule[i])
                {
                    player.duelSpaceReq++;
                }
                else
                {
                    player.duelSpaceReq--;
                }
            }
            if (o.playerEquipment[o.duelSlot] > 0)
            {
                if (!o.duelRule[i])
                {
                    o.duelSpaceReq++;
                }
                else
                {
                    o.duelSpaceReq--;
                }
            }
        }

        if (i >= 11)
        {
            if (ItemAssistant.freeSlots(player) < (player.duelSpaceReq) || ItemAssistant.freeSlots(o) < (o.duelSpaceReq))
            {
                player.sendMessage("You or your opponent don't have the required space to set this rule.");
                if (player.playerEquipment[player.duelSlot] > 0)
                {
                    player.duelSpaceReq--;
                }
                if (o.playerEquipment[o.duelSlot] > 0)
                {
                    o.duelSpaceReq--;
                }
                return;
            }
        }

        if (!player.duelRule[i])
        {
            player.duelRule[i] = true;
            player.duelOption += player.DUEL_RULE_ID[i];
        }
        else
        {
            player.duelRule[i] = false;
            player.duelOption -= player.DUEL_RULE_ID[i];
        }

        player.getPA().sendFrame87(286, player.duelOption);
        o.duelOption = player.duelOption;
        o.duelRule[i] = player.duelRule[i];
        o.getPA().sendFrame87(286, o.duelOption);

        if (player.duelRule[8])
        {
            if (player.duelRule[1])
            {
                player.duelTeleX = 3366 + Misc.random(12);
                o.duelTeleX = player.duelTeleX - 1;
                player.duelTeleY = 3246 + Misc.random(6);
                o.duelTeleY = player.duelTeleY;
            }
        }
        else
        {
            if (player.duelRule[1])
            {
                player.duelTeleX = 3335 + Misc.random(12);
                o.duelTeleX = player.duelTeleX - 1;
                player.duelTeleY = 3246 + Misc.random(6);
                o.duelTeleY = player.duelTeleY;
            }
        }

    }
}
