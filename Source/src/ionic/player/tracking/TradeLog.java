package ionic.player.tracking;

import ionic.player.Client;
import ionic.player.PlayerHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
 * TradeLog class
 * @author Aintaro.
 */

public class TradeLog
{

        /**
         * Will write what kind of item a player has received.
         * MONTH = 0 = January
         * DAY OF MONTH = 30 || 31
         */
        public static void tradeReceived(Client player, String itemName, int itemAmount)
        {
                Client o = (Client) PlayerHandler.players[player.tradeWith];
                Calendar C = Calendar.getInstance();
                try
                {
                        BufferedWriter bItem = new BufferedWriter(new FileWriter("./data/logs/TradeLog/received/" + player.playerName + ".txt", true));
                        try
                        {
                                bItem.newLine();
                                bItem.write("Year : " + C.get(Calendar.YEAR) + "\tMonth : " + C.get(Calendar.MONTH) + "\tDay : " + C.get(Calendar.DAY_OF_MONTH));
                                bItem.newLine();
                                bItem.write("Received " + itemAmount + " " + itemName + " From " + o.playerName);
                                bItem.newLine();
                                bItem.write("--------------------------------------------------");
                        }
                        finally
                        {
                                bItem.close();
                        }
                }
                catch (IOException e)
                {
                        e.printStackTrace();
                }
        }

        /**
         * Will write what kind of item a player has traded with another player.
         * MONTH = 0 = January
         * DAY OF MONTH = 30 || 31
         */
        public static void tradeGive(Client player, String itemName, int itemAmount)
        {
                Client o = (Client) PlayerHandler.players[player.tradeWith];
                Calendar C = Calendar.getInstance();
                try
                {
                        BufferedWriter bItem = new BufferedWriter(new FileWriter("./data/logs/TradeLog/given/" + player.playerName + ".txt", true));
                        try
                        {
                                bItem.newLine();
                                bItem.write("Year : " + C.get(Calendar.YEAR) + "\tMonth : " + C.get(Calendar.MONTH) + "\tDay : " + C.get(Calendar.DAY_OF_MONTH));
                                bItem.newLine();
                                bItem.write("Given " + itemAmount + " " + itemName + " To " + o.playerName);
                                bItem.newLine();
                                bItem.write("--------------------------------------------------");
                        }
                        finally
                        {
                                bItem.close();
                        }
                }
                catch (IOException e)
                {
                        e.printStackTrace();
                }
        }
}