package ionic.player.tracking;

import ionic.player.Player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Store the player's username in a text file if the player is suspected of duping.
 * If this causes too much lag, i think i should scan all files manually, daily. So i should use the feature in my economy checker which scans all accounts that ends with .txt
 * @author MGT Madness, created on 10-04-2014.
 */
public class AccountFlagging
{
        /**
         * Scan the account for excessive items.
         * @param player
         * 			The associated player.
         */
        public static void scanAccount(Player player)
        {
                if (player.isAdministratorRank())
                {
                        return;
                }
                scanBankAndInventory(player, 995, 100000000, false);
                
                /* Start of rare items. */
                scanBankAndInventory(player, 14484, 5, true);
                scanBankAndInventory(player, 11694, 5, true);
                /* End of rare items. */
                
                /* Start of Bandos godsword, chestplate, tassets and boots. */
                scanBankAndInventory(player, 11696, 5, true);
                scanBankAndInventory(player, 11724, 5, true);
                scanBankAndInventory(player, 11726, 5, true);
                /* End of Bandos godsword, chestplate, tassets and boots. */
                
                /* Start of Armadyl helmet, chestplate and plateskirt. */
                scanBankAndInventory(player, 11718, 5, true);
                scanBankAndInventory(player, 11720, 5, true);
                scanBankAndInventory(player, 11722, 5, true);
                /* End of Armadyl helmet, chestplate and plateskirt. */
                
                /* Start of PVP items. */
                scanBankAndInventory(player, 13858, 5, true);
                scanBankAndInventory(player, 13861, 5, true);
                scanBankAndInventory(player, 13870, 5, true);
                scanBankAndInventory(player, 13873, 5, true);
                scanBankAndInventory(player, 13896, 5, true);
                scanBankAndInventory(player, 13884, 5, true);
                scanBankAndInventory(player, 13890, 5, true);
                scanBankAndInventory(player, 13902, 5, true);
                scanBankAndInventory(player, 13887, 5, true);
                scanBankAndInventory(player, 13893, 5, true);
                scanBankAndInventory(player, 13899, 5, true);
                /* End of PVP items. */
                
                /* Start of Barrows items. */
                scanBankAndInventory(player, 4712, 100, true);
                scanBankAndInventory(player, 4714, 100, true);
                scanBankAndInventory(player, 4716, 100, true);
                scanBankAndInventory(player, 4718, 100, true);
                scanBankAndInventory(player, 4720, 100, true);
                scanBankAndInventory(player, 4722, 100, true);
                scanBankAndInventory(player, 4728, 100, true);
                scanBankAndInventory(player, 4730, 100, true);
                scanBankAndInventory(player, 4734, 100, true);
                scanBankAndInventory(player, 4736, 100, true);
                scanBankAndInventory(player, 4738, 100, true);
                scanBankAndInventory(player, 4749, 100, true);
                scanBankAndInventory(player, 4751, 100, true);
                scanBankAndInventory(player, 4753, 100, true);
                scanBankAndInventory(player, 4757, 100, true);
                scanBankAndInventory(player, 4759, 100, true);
                /* End of Barrows items. */
                
                scanBankAndInventory(player, 11732, 100, true); // Dragon boots.
                scanBankAndInventory(player, 15486, 100, true); // Staff of light.
                scanBankAndInventory(player, 6889, 100, true); // Mages' book.
                scanBankAndInventory(player, 4151, 100, true); // Abyssal whip.
                scanBankAndInventory(player, 6920, 100, true); // Infinity boots.
        }

        /**
         * Scan the bank and inventory.
         * @param player
         * 			The associated player.
         * @param itemIdentity
         * 			The item identity to scan for.
         * @param itemAmount
         * 			The amount to scan for.
         * @param noteable
         * 			True, if the item is note-able
         */
        static void scanBankAndInventory(Player player, int itemIdentity, int itemAmount, boolean noteable)
        {
                scanBank(player, itemIdentity, itemAmount);
                scanInventory(player, itemIdentity, itemAmount, noteable);
                if (player.quantityOfItem >= itemAmount)
                {
                        flagAccount(player, player.playerName);
                }
                player.quantityOfItem = 0;
        }

        /**
         * Scan the bank.
         * @param player
         * 			The associated player.
         * @param itemIdentity
         * 			The item identity to scan for.
         * @param itemAmount
         * 			The amount to scan for.
         */
        static void scanBank(Player player, int itemIdentity, int itemAmount)
        {
                try
                {
                        File dir = new File("./Data/characters");
                        if (dir.exists())
                        {
                                String read;
                                File files[] = dir.listFiles();
                                for (int j = 0; j < files.length; j++)
                                {
                                        File loaded = files[j];
                                        String username1 = loaded.getName().replace(".txt", "");
                                        if (username1.equalsIgnoreCase(player.playerName))
                                        {
                                                Scanner s = new Scanner(loaded);
                                                while (s.hasNextLine())
                                                {
                                                        read = s.nextLine();
                                                        if (read.startsWith("character-bank"))
                                                        {
                                                                String[] temp = read.split("\t");
                                                                int itemScannedIdentity = Integer.parseInt(temp[1]);
                                                                int itemScannedAmount = Integer.parseInt(temp[2]);
                                                                if (itemScannedIdentity == itemIdentity + 1)
                                                                {
                                                                	player.quantityOfItem += itemScannedAmount;
                                                                }
                                                        }
                                                }
                                                s.close();
                                        }
                                }
                        }
                        else
                        {
                                System.out.println("Did not work.");
                        }
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
        }

        /**
         * Scan the inventory.
         * @param player
         * 			The associated player.
         * @param itemIdentity
         * 			The item identity to scan for.
         * @param itemAmount
         * 			The amount to scan for.
         * @param noteable
         * 			True, if the item is note-able.
         */
        static void scanInventory(Player player, int itemIdentity, int itemAmount, boolean noteable)
        {
                try
                {
                        File dir = new File("./Data/characters");
                        if (dir.exists())
                        {
                                String read;
                                File files[] = dir.listFiles();
                                for (int j = 0; j < files.length; j++)
                                {
                                        File loaded = files[j];
                                        String username1 = loaded.getName().replace(".txt", "");
                                        if (username1.equalsIgnoreCase(player.playerName))
                                        {
                                                Scanner s = new Scanner(loaded);
                                                while (s.hasNextLine())
                                                {
                                                        read = s.nextLine();
                                                        if (read.startsWith("inventory-slot"))
                                                        {
                                                                String[] temp = read.split("\t");
                                                                int itemScannedIdentity = Integer.parseInt(temp[1]);
                                                                int itemScannedAmount = Integer.parseInt(temp[2]);
                                                                if (noteable)
                                                                {
                                                                        if (itemScannedIdentity == itemIdentity + 1 || itemScannedIdentity == itemIdentity + 2)
                                                                        {
                                                                        	player.quantityOfItem += itemScannedAmount;
                                                                        }
                                                                }
                                                                else
                                                                {
                                                                        if (itemScannedIdentity == itemIdentity + 1)
                                                                        {
                                                                        	player.quantityOfItem += itemScannedAmount;
                                                                        }
                                                                }

                                                        }
                                                }
                                                s.close();
                                        }
                                }
                        }
                        else
                        {
                                System.out.println("Did not work.");
                        }
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
        }

        /**
         * Store the account's name in a text file.
         * @param text
         * 			The text to store.
         */
        static void flagAccount(Player player, String text)
        {
                if (duplicateName(player))
                {
                        return;
                }
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                String filePath = "./data/logs/Flagged Accounts.txt";
                BufferedWriter bw = null;
                try
                {
                        bw = new BufferedWriter(new FileWriter(filePath, true));
                        bw.write("[" + dateFormat.format(cal.getTime()) + "] [" + text + "].");
                        bw.newLine();
                        bw.flush();
                }
                catch (IOException ioe)
                {
                        ioe.printStackTrace();
                }
                finally
                {
                        if (bw != null)
                        {
                                try
                                {
                                        bw.close();
                                }
                                catch (IOException ioe2)
                                {}
                        }
                }
        }

        /**
         * True, if the player's name is already in the text file.
         * @param player
         * 			The associated player.
         * @return
         * 			True, if the player's name is already in the text file.
         */
        static boolean duplicateName(Player player)
        {
                try
                {@
                        SuppressWarnings("resource")
                        BufferedReader file = new BufferedReader(new FileReader("./data/logs/Flagged Accounts.txt"));
                        String line;
                        while ((line = file.readLine()) != null)
                        {
                                if (line.contains("[" + player.playerName + "]"))
                                {
                                        return true;
                                }
                        }
                }
                catch (Exception e)
                {
                        System.out.println("Problem scanning for duplicate name.");
                }
                return false;
        }

}