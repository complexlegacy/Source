package tools;

import ionic.item.Item;

import java.io.File;
import java.util.Scanner;

/**
 * Scan the economy.
 * Server manage will scan the first tab of the bank and inventory + worn equipment, and to search for noted items in inventory,
 * i'll have to type in 14485 instead of 14484 for claws.
 * This program cannot scan the worn items. When scanning, only use inventory only or bank only.
 * @author MGT Madness, created on 10-04-2014.
 */
public class EconomyChecker
{

		/**
		 * Item to scan for.
		 */
        static int itemToCheck;

        /**
         * Item amount to scan for.
         */
        static int itemAmount;
        
        /**
         * True, if the item is noteable. False for bank checking and true if the item can be noted in the inventory.
         */
        static boolean noteable;
        
        /**
         * Program launcher.
         */
        public static void main(String args[])
        {
        	itemToCheck = 14484;
        	itemAmount = 5;
        	noteable = true;
        	System.out.println("Item: " + getItemName(itemToCheck) + ".");
        	System.out.println("Amount: " + itemAmount + ".");
        	System.out.println("--------------------------------");
        	scanInventory();
        	//scanBank();
        }
        
        /**
         * Scan the bank.
         */
        static void scanBank()
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
                                        if (loaded.getName().endsWith(".txt"))
                                        {
                                                Scanner s = new Scanner(loaded);
                                                int amountOfDuped = 0;
                                                while (s.hasNextLine())
                                                {
                                                        read = s.nextLine();
                                                        if (read.startsWith("character-bank"))
                                                        {
                                                                String[] temp = read.split("\t");
                                                                int itemScannedIdentity = Integer.parseInt(temp[1]);
                                                                int itemScannedAmount = Integer.parseInt(temp[2]);
                                                                if (itemScannedIdentity == itemToCheck + 1)
                                                                {
                                                                        amountOfDuped += itemScannedAmount;
                                                                        if (amountOfDuped >= itemAmount)
                                                                        {
                                                                        	    String username = loaded.getName().replace(".txt", "");
                                                                                System.out.println(username + " has " + amountOfDuped);
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
         * Scan the inventory.
         */
        static void scanInventory()
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
                                        if (loaded.getName().endsWith(".txt"))
                                        {
                                                Scanner s = new Scanner(loaded);
                                                int amountOfDuped = 0;
                                                while (s.hasNextLine())
                                                {
                                                        read = s.nextLine();
                                                        if (read.startsWith("character-item"))
                                                        {
                                                                String[] temp = read.split("\t");
                                                                int itemScannedIdentity = Integer.parseInt(temp[1]);
                                                                int itemScannedAmount = Integer.parseInt(temp[2]);
                                                                if (noteable)
                                                                {
                                                                if (itemScannedIdentity == itemToCheck + 1 || itemScannedIdentity == itemToCheck + 2 )
                                                                {
                                                                        amountOfDuped += itemScannedAmount;
                                                                        if (amountOfDuped >= itemAmount)
                                                                        {
                                                                        	    String username = loaded.getName().replace(".txt", "");
                                                                                System.out.println(username + " has " + amountOfDuped);
                                                                        }
                                                                }
                                                                }
                                                                else
                                                                {
                                                                	if (itemScannedIdentity == itemToCheck + 1)
                                                                    {
                                                                            amountOfDuped += itemScannedAmount;
                                                                            if (amountOfDuped >= itemAmount)
                                                                            {
                                                                            	    String username = loaded.getName().replace(".txt", "");
                                                                                    System.out.println(username + " has " + amountOfDuped);
                                                                            }
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
         * Grab the item's name by using the identity.
         * @param ItemID
         * 			The item's identity.
         */
        public static String getItemName(int ItemID)
        {
                return Item.getItemName(ItemID);
        }
        
}