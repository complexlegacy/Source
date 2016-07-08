package ionic.player.content.miscellaneous;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.PlayerHandler;
import utility.Misc;
import core.Server;

/**
 * Artefacts.
 * @author MGT Madness, created on 18-01-2014.
 */
public class Artefacts
{

        /**
         * Drop the artefact as a loot for the player.
         * @param victim
         * 			The associated player
         */
        public static void dropArtefacts(Client victim)
        {
                Client attacker = (Client) PlayerHandler.players[victim.lastAttackedBy];
                	Server.itemHandler.createGroundItem(attacker, artefactDrop(), victim.getX(), victim.getY(), 1, victim.lastAttackedBy, false);
                	boolean extraLoot = false;
                	
                	if (Misc.random(4) == 1)
                	{
                		Server.itemHandler.createGroundItem(attacker, artefactDrop(), victim.getX(), victim.getY(), 1, victim.lastAttackedBy, false);
                		extraLoot = true;
                	}
                	if (Misc.random(9) == 1 && !extraLoot)
                	{
                		Server.itemHandler.createGroundItem(attacker, artefactDrop(), victim.getX(), victim.getY(), 1, victim.lastAttackedBy, false);
                		Server.itemHandler.createGroundItem(attacker, artefactDrop(), victim.getX(), victim.getY(), 1, victim.lastAttackedBy, false);
                		extraLoot = true;
                	}
                	if (Misc.random(19) == 1 && !extraLoot)
                	{
                		Server.itemHandler.createGroundItem(attacker, artefactDrop(), victim.getX(), victim.getY(), 1, victim.lastAttackedBy, false);
                		Server.itemHandler.createGroundItem(attacker, artefactDrop(), victim.getX(), victim.getY(), 1, victim.lastAttackedBy, false);
                		Server.itemHandler.createGroundItem(attacker, artefactDrop(), victim.getX(), victim.getY(), 1, victim.lastAttackedBy, false);
                		extraLoot = true;
                	}
                	
        }
        
        /**
         * Artefacts item identities.
         */
        public static int artefactDrop[] = {
                14876, 14876, 14876, // 169 artifacts in total
                14877, 14877, 14877, 14877,
                14878, 14878, 14878, 14878, 14878,
                14879, 14879, 14879, 14879, 14879, 14879,
                14880, 14880, 14880, 14880, 14880, 14880, 14880,
                14881, 14881, 14881, 14881, 14881, 14881, 14881, 14881,
                14882, 14882, 14882, 14882, 14882, 14882, 14882, 14882, 14882,
                14883, 14883, 14883, 14883, 14883, 14883, 14883, 14883, 14883, 14883,
                14884, 14884, 14884, 14884, 14884, 14884, 14884, 14884, 14884, 14884, 14884, 
                14885, 14885, 14885, 14885, 14885, 14885, 14885, 14885, 14885, 14885, 14885, 14885,
                14886, 14886, 14886, 14886, 14886, 14886, 14886, 14886, 14886, 14886, 14886, 14886, 14886, 
                14887, 14887, 14887, 14887, 14887, 14887, 14887, 14887, 14887, 14887, 14887, 14887, 14887, 14887,
                14888, 14888, 14888, 14888, 14888, 14888, 14888, 14888, 14888, 14888, 14888, 14888, 14888, 14888, 14888, 
                14889, 14889, 14889, 14889, 14889, 14889, 14889, 14889, 14889, 14889, 14889, 14889, 14889, 14889, 14889, 14889, 
                14890, 14890, 14890, 14890, 14890, 14890, 14890, 14890, 14890, 14890, 14890, 14890, 14890, 14890, 14890, 14890, 14890, 
                14891, 14891, 14891, 14891, 14891, 14891, 14891, 14891, 14891, 14891, 14891, 14891, 14891, 14891, 14891, 14891, 14891, 14891, 
                14892, 14892, 14892, 14892, 14892, 14892, 14892, 14892, 14892, 14892, 14892, 14892, 14892, 14892, 14892, 14892, 14892, 14892, 14892
        };

        /**
         * Randomly choose one of the artefactDrop[].
         * @return
         * 			One of the artefactDrop[].
         */
        public static int artefactDrop()
        {
                return artefactDrop[(int)(Math.random() * artefactDrop.length)];
        }

        /**
         * Break the artefact and receive coins in return.
         * @param player
         * 			The associated player.
         */
        public static boolean breakArtefact(Client player, int itemId)
        {
        	    int amount = 0;
                if (itemId == 14892)
                {
                	    amount = 100;
                        ItemAssistant.deleteItem(player, 14892, 1);
                        ItemAssistant.addItem(player, 995, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }
                if (itemId == 14891)
                {
            	    	amount = 110;
                        ItemAssistant.deleteItem(player, 14891, 1);
                        ItemAssistant.addItem(player, 995, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }
                if (itemId == 14890)
                {
        	    	amount = 120;
                        ItemAssistant.deleteItem(player, 14890, 1);
                        ItemAssistant.addItem(player, 995, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }
                if (itemId == 14889)
                {
        	    	amount = 130;
                        ItemAssistant.deleteItem(player, 14889, 1);
                        ItemAssistant.addItem(player, 995, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }
                if (itemId == 14888)
                {
        	    	amount = 140;
                        ItemAssistant.deleteItem(player, 14888, 1);
                        ItemAssistant.addItem(player, 995, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }
                if (itemId == 14887)
                {
        	    	amount = 150;
                        ItemAssistant.deleteItem(player, 4887, 1);
                        ItemAssistant.addItem(player, 995, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }
                if (itemId == 14886)
                {
        	    	amount = 160;
                        ItemAssistant.deleteItem(player, 14886, 1);
                        ItemAssistant.addItem(player, 995, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }
                if (itemId == 14885)
                {
        	    	amount = 170;
                        ItemAssistant.deleteItem(player, 14885, 1);
                        ItemAssistant.addItem(player, 995, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }
                if (itemId == 14884)
                {
        	    	amount = 180;
                        ItemAssistant.deleteItem(player, 14884, 1);
                        ItemAssistant.addItem(player, 995, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }
                if (itemId == 14883)
                {
        	    	amount = 190;
                        ItemAssistant.deleteItem(player, 14883, 1);
                        ItemAssistant.addItem(player, 95, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }
                if (itemId == 14882)
                {
        	    	amount = 200;
                        ItemAssistant.deleteItem(player, 14882, 1);
                        ItemAssistant.addItem(player, 995, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }
                if (itemId == 14881)
                {
        	    	amount = 210;
                        ItemAssistant.deleteItem(player, 14881, 1);
                        ItemAssistant.addItem(player, 995, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }
                if (itemId == 14880)
                {
        	    	amount = 220;
                        ItemAssistant.deleteItem(player, 14880, 1);
                        ItemAssistant.addItem(player, 995, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }
                if (itemId == 14879)
                {
        	    	amount = 230;
                        ItemAssistant.deleteItem(player, 14879, 1);
                        ItemAssistant.addItem(player, 995, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }
                if (itemId == 14878)
                {
        	    	amount = 240;
                        ItemAssistant.deleteItem(player, 14878, 1);
                        ItemAssistant.addItem(player, 995, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }
                if (itemId == 14877)
                {
        	    	amount = 250;
                        ItemAssistant.deleteItem(player, 14877, 1);
                        ItemAssistant.addItem(player, 995, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }
                if (itemId == 14876)
                {
        	    	amount = 1000;
                        ItemAssistant.deleteItem(player, 14876, 1);
                        ItemAssistant.addItem(player, 995, amount * 1000);
                        player.sendMessage("You break the ancient artefact and find " + amount + "k coins.");
                        return true;
                }

                return false;
        }

}