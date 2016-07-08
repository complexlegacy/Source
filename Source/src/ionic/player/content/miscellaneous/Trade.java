package ionic.player.content.miscellaneous;

import ionic.item.GameItem;
import ionic.item.Item;
import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.npc.pet.Pet;
import ionic.player.Client;
import ionic.player.PlayerHandler;
import ionic.player.tracking.TradeLog;

import java.util.concurrent.CopyOnWriteArrayList;

import utility.Misc;
import core.Constants;
import core.Server;


public class Trade
{
        private Client player;
        public Trade(Client Client)
        {
                this.player = Client;
        }

        public CopyOnWriteArrayList < GameItem > offeredItems = new CopyOnWriteArrayList < GameItem > ();

        public void requestTrade(int id)
        {
                try
                {
                        Client o = (Client) PlayerHandler.players[id];
                        if (id == player.playerId)
                                return;
                        player.tradeWith = id;
                        if (!player.inTrade && o.tradeRequested && o.tradeWith == player.playerId)
                        {
                                player.getTradeAndDuel().openTrade();
                                o.getTradeAndDuel().openTrade();
                        }
                        else if (!player.inTrade)
                        {

                                player.tradeRequested = true;
                                player.sendMessage("Sending trade request...");
                                o.sendMessage(player.playerName + ":tradereq:");
                        }
                }
                catch (Exception e)
                {
                        Misc.println("Error requesting trade.");
                }
        }

        public void openTrade()
        {
                Client o = (Client) PlayerHandler.players[player.tradeWith];

                if (o == null)
                {
                        return;
                }
                player.inTrade = true;
                player.canOffer = true;
                player.tradeStatus = 1;
                player.tradeRequested = false;
                ItemAssistant.resetItems(player, 3322);
                resetTItems(3415);
                resetOTItems(3416);
                String out = o.playerName;

                if (o.isModeratorRank())
                {
                        out = "@cr1@" + out;
                }
                else if (o.isAdministratorRank())
                {
                        out = "@cr2@" + out;
                }
                player.getPA().sendFrame126("Trading with: " + o.playerName + " who has @gre@" + ItemAssistant.freeSlots(o) + "", 3417);
                player.getPA().sendFrame126("", 3431);
                player.getPA().sendFrame126("Are you sure you want to make this trade?", 3535);
                player.getPA().sendFrame248(3323, 3321);
        }



        public void resetTItems(int WriteFrame)
        {
                synchronized(player)
                {
                        player.getOutStream().createFrameVarSizeWord(53);
                        player.getOutStream().writeWord(WriteFrame);
                        int len = offeredItems.toArray().length;
                        int current = 0;
                        player.getOutStream().writeWord(len);
                        for (GameItem item: offeredItems)
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
        public boolean fromTrade(int itemID, int fromSlot, int amount)
        {
                Client o = (Client) PlayerHandler.players[player.tradeWith];
                if (o == null)
                {
                        return false;
                }
                try
                {
                        if (!player.inTrade || !player.canOffer)
                        {
                                declineTrade();
                                return false;
                        }
                        if (amount < 0)
                        {
                                return false;
                        }
                        player.tradeConfirmed = false;
                        o.tradeConfirmed = false;
                        if (!ItemData.data[itemID].stackable)
                        {
                                if (amount > 28)
                                {
                                        amount = 28;
                                }
                                for (int a = 0; a < amount; a++)
                                {
                                        for (GameItem item: offeredItems)
                                        {
                                                if (item.id == itemID)
                                                {
                                                        if (!item.stackable)
                                                        {
                                                                offeredItems.remove(item);
                                                                ItemAssistant.addItem(player, itemID, 1);
                                                        }
                                                        else if (item.amount > amount)
                                                        {
                                                                item.amount -= amount;
                                                                ItemAssistant.addItem(player, itemID, amount);
                                                        }
                                                        else
                                                        {
                                                                amount = item.amount;
                                                                offeredItems.remove(item);
                                                                ItemAssistant.addItem(player, itemID, amount);
                                                        }
                                                        break;
                                                }
                                        }
                                }
                        }
                        for (GameItem item: offeredItems)
                        {
                                if (item.id == itemID)
                                {
                                        if (!item.stackable)
                                        {}
                                        else if (item.amount > amount)
                                        {
                                                item.amount -= amount;
                                                ItemAssistant.addItem(player, itemID, amount);
                                        }
                                        else
                                        {
                                                amount = item.amount;
                                                offeredItems.remove(item);
                                                ItemAssistant.addItem(player, itemID, amount);

                                        }
                                        break;
                                }
                        }
                        player.tradeConfirmed = false;
                        o.tradeConfirmed = false;
                        ItemAssistant.resetItems(player, 3322);
                        resetTItems(3415);
                        o.getTradeAndDuel().resetOTItems(3416);
                        //displayWAndI(c);
                        player.getPA().sendFrame126("", 3431);
                        o.getPA().sendFrame126("", 3431);
                }
                catch (Exception e)
                {

                }
                return true;
        }

        public boolean tradeItem(int itemID, int fromSlot, int amount)
        {
                Client o = (Client) PlayerHandler.players[player.tradeWith];
                if (o == null)
                {
                        return false;
                }

                        if (ItemData.data[itemID].isUntradable())
                        {
                                player.sendMessage("You can't trade this item.");
                                return false;
                    }

                for (int i: Constants.ITEMS_TO_INVENTORY_ON_DEATH)
                {
                        if (i == itemID)
                        {
                                player.sendMessage("You can't trade this item.");
                                return false;
                        }
                }

                for (int i: Constants.ITEMS_TO_DISSAPEAR)
                {
                        if (i == itemID)
                        {
                                player.sendMessage("You can't trade this item.");
                                return false;
                        }
                }

                if (Pet.petItem(itemID))
                {
                        player.sendMessage("You can't trade this item.");
                        return false;
                }

                player.tradeConfirmed = false;
                o.tradeConfirmed = false;
                if (!ItemData.data[itemID].stackable)
                {
                        for (int a = 0; a < amount && a < 28; a++)
                        {
                                if (ItemAssistant.playerHasItem(player, itemID, 1))
                                {
                                        offeredItems.add(new GameItem(itemID, 1));
                                        ItemAssistant.deleteItem(player, itemID, ItemAssistant.getItemSlot(player, itemID), 1);
                                        o.getPA().sendFrame126("Trading with: " + player.playerName + " who has @gre@" + ItemAssistant.freeSlots(player) + "", 3417);
                                }
                        }
                        o.getPA().sendFrame126("Trading with: " + player.playerName + " who has @gre@" + ItemAssistant.freeSlots(player) + "", 3417);
                        ItemAssistant.resetItems(player, 3322);
                        resetTItems(3415);
                        o.getTradeAndDuel().resetOTItems(3416);
                        player.getPA().sendFrame126("", 3431);
                        o.getPA().sendFrame126("", 3431);
                }
                if (ItemAssistant.getInventoryItemAmount(player, itemID) < amount)
                {
                        amount = ItemAssistant.getInventoryItemAmount(player, itemID);
                        if (amount == 0)
                                return false;
                }
                if (!player.inTrade || !player.canOffer)
                {
                        declineTrade();
                        return false;
                }
                if (!ItemAssistant.playerHasItem(player, itemID, amount))
                        return false;

                if (ItemData.data[itemID].stackable)
                {
                        boolean inTrade = false;
                        for (GameItem item: offeredItems)
                        {
                                if (item.id == itemID)
                                {
                                        inTrade = true;
                                        item.amount += amount;
                                        ItemAssistant.deleteItem2(player, itemID, amount);
                                        o.getPA().sendFrame126("Trading with: " + player.playerName + " who has @gre@" + ItemAssistant.freeSlots(player) + "", 3417);
                                        break;
                                }
                        }

                        if (!inTrade)
                        {
                                offeredItems.add(new GameItem(itemID, amount));
                                ItemAssistant.deleteItem2(player, itemID, amount);
                                o.getPA().sendFrame126("Trading with: " + player.playerName + " who has @gre@" + ItemAssistant.freeSlots(player) + "", 3417);
                        }
                }
                o.getPA().sendFrame126("Trading with: " + player.playerName + " who has @gre@" + ItemAssistant.freeSlots(player) + "", 3417);
                ItemAssistant.resetItems(player, 3322);
                resetTItems(3415);
                o.getTradeAndDuel().resetOTItems(3416);
                player.getPA().sendFrame126("", 3431);
                o.getPA().sendFrame126("", 3431);
                return true;
        }

        public void tradeResetRequired()
        {
                Client o = (Client) PlayerHandler.players[player.tradeWith];
                if (o != null)
                {
                        if (o.tradeResetNeeded)
                        {
                                player.getTradeAndDuel().resetTrade();
                                o.getTradeAndDuel().resetTrade();
                        }
                }
        }
        public void resetTrade()
        {
                offeredItems.clear();
                player.inTrade = false;
                player.tradeWith = 0;
                player.canOffer = true;
                player.tradeConfirmed = false;
                player.tradeConfirmed2 = false;
                player.acceptedTrade = false;
                player.getPA().removeAllWindows();
                player.tradeResetNeeded = false;
                player.getPA().sendFrame126("Are you sure you want to make this trade?", 3535);
        }
        public void declineTrade()
        {
                player.tradeStatus = 0;
                declineTrade(true);
        }


        public void declineTrade(boolean tellOther)
        {
                player.getPA().removeAllWindows();
                Client o = (Client) PlayerHandler.players[player.tradeWith];
                if (o == null)
                {
                        return;
                }

                if (tellOther)
                {
                        o.getTradeAndDuel().declineTrade(false);
                        o.getTradeAndDuel().player.getPA().removeAllWindows();
                }

                for (GameItem item: offeredItems)
                {
                        if (item.amount < 1)
                        {
                                continue;
                        }
                        if (item.stackable)
                        {
                                ItemAssistant.addItem(player, item.id, item.amount);
                        }
                        else
                        {
                                for (int i = 0; i < item.amount; i++)
                                {
                                        ItemAssistant.addItem(player, item.id, 1);
                                }
                        }
                }
                player.canOffer = true;
                player.tradeConfirmed = false;
                player.tradeConfirmed2 = false;
                offeredItems.clear();
                player.inTrade = false;
                player.tradeWith = 0;
        }


        public void resetOTItems(int WriteFrame)
        {
                synchronized(player)
                {
                        Client o = (Client) PlayerHandler.players[player.tradeWith];
                        if (o == null)
                        {
                                return;
                        }
                        player.getOutStream().createFrameVarSizeWord(53);
                        player.getOutStream().writeWord(WriteFrame);
                        int len = o.getTradeAndDuel().offeredItems.toArray().length;
                        int current = 0;
                        player.getOutStream().writeWord(len);
                        for (GameItem item: o.getTradeAndDuel().offeredItems)
                        {
                                if (item.amount > 254)
                                {
                                        player.getOutStream().writeByte(255); // item's stack count. if over 254, write byte 255
                                        player.getOutStream().writeDWord_v2(item.amount);
                                }
                                else
                                {
                                        player.getOutStream().writeByte(item.amount);
                                }
                                player.getOutStream().writeWordBigEndianA(item.id + 1); // item id
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


        public void confirmScreen()
        {
                Client o = (Client) PlayerHandler.players[player.tradeWith];
                if (o == null)
                {
                        return;
                }
                player.canOffer = false;
                ItemAssistant.resetItems(player, 3214);
                String SendTrade = "Absolutely nothing!";
                String SendAmount = "";
                int Count = 0;
                for (GameItem item: offeredItems)
                {
                        if (item.id > 0)
                        {
                                if (item.amount >= 1000 && item.amount < 1000000)
                                {
                                        SendAmount = "@cya@" + (item.amount / 1000) + "K @whi@(" + Misc.format(item.amount) + ")";
                                }
                                else if (item.amount >= 1000000)
                                {
                                        SendAmount = "@gre@" + (item.amount / 1000000) + " million @whi@(" + Misc.format(item.amount) + ")";
                                }
                                else
                                {
                                        SendAmount = "" + Misc.format(item.amount);
                                }

                                if (Count == 0)
                                {
                                        SendTrade = ItemAssistant.getItemName(item.id);
                                }
                                else
                                {
                                        SendTrade = SendTrade + "\\n" + ItemAssistant.getItemName(item.id);
                                }

                                if (item.stackable)
                                {
                                        SendTrade = SendTrade + " x " + SendAmount;
                                }
                                Count++;
                        }
                }

                player.getPA().sendFrame126(SendTrade, 3557);
                SendTrade = "Absolutely nothing!";
                SendAmount = "";
                Count = 0;

                for (GameItem item: o.getTradeAndDuel().offeredItems)
                {
                        if (item.id > 0)
                        {
                                if (item.amount >= 1000 && item.amount < 1000000)
                                {
                                        SendAmount = "@cya@" + (item.amount / 1000) + "K @whi@(" + Misc.format(item.amount) + ")";
                                }
                                else if (item.amount >= 1000000)
                                {
                                        SendAmount = "@gre@" + (item.amount / 1000000) + " million @whi@(" + Misc.format(item.amount) + ")";
                                }
                                else
                                {
                                        SendAmount = "" + Misc.format(item.amount);
                                }
                                if (Count == 0)
                                {
                                        SendTrade = ItemAssistant.getItemName(item.id);
                                }
                                else
                                {
                                        SendTrade = SendTrade + "\\n" + ItemAssistant.getItemName(item.id);
                                }
                                if (item.stackable)
                                {
                                        SendTrade = SendTrade + " x " + SendAmount;
                                }
                                Count++;
                        }
                }
                player.getPA().sendFrame126(SendTrade, 3558);
                player.getPA().sendFrame248(3443, 197);
        }


        public void giveItems()
        {
                if (System.currentTimeMillis() - player.diceDelay < 6000)
                {
                        return;
                }
                player.diceDelay = System.currentTimeMillis();
                Client o = (Client) PlayerHandler.players[player.tradeWith];
                if (o == null)
                {
                        return;
                }
                try
                {
                        for (GameItem item: o.getTradeAndDuel().offeredItems)
                        {
                                if (item.id > 0)
                                {
                                        ItemAssistant.addItem(player, item.id, item.amount);
                                        TradeLog.tradeReceived(player, ItemAssistant.getItemName(item.id), item.amount);
                                        TradeLog.tradeGive(player, ItemAssistant.getItemName(item.id), item.amount);
                                }
                        }

                        player.getPA().removeAllWindows();
                        player.tradeResetNeeded = true;
                        player.getTradeAndDuel().tradeResetRequired();
                        player.sendMessage("You have finished a trade.");
                }
                catch (Exception e)
                {}
        }

}