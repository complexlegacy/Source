package ionic.npc.clicknpc;

import ionic.item.ItemAssistant;
import ionic.item.ItemDegrade;
import ionic.npc.NPC;
import ionic.npc.NPCHandler;
import ionic.player.Client;
import ionic.player.content.skills.fishing.Fishing;
import ionic.player.content.skills.summoning.FamiliarInteraction;
import ionic.player.content.skills.summoning.Summoning;
import ionic.player.content.skills.summoning.SummoningData;
import ionic.player.content.skills.thieving.PickPocketing;
import ionic.shop.ShopController;
import utility.Misc;
import core.Configuration;

/**
 * Second click on NPC interactions.
 * @author MGT Madness, created on 18-01-2013.
 */
public class SecondClickNPC
{

        /**
         * Second click on NPC.
         * @param player
         * 			The associated player.
         * @param npcType
         * 			The NPC identity.
         */
        public static void secondClickNpc(Client player, int npcType) {
        	NPC npc = NPCHandler.npcs[player.npcClickIndex];
        	
                player.clickNpcType = 0;
                player.npcClickIndex = 0;

                
                if (player.familiar != null) {
                	if (player.familiar == npc) {
                		Summoning.openBob(player);
                	} else {
                		player.sendMessage("This is not your familiar.");
                	}
                }
            if (player.fishTimer == 0) {
                if (Fishing.fishingNPC(npcType)) {
                    player.fishTimer = 3;
                    Fishing.fishingNPC(player, 2, npcType);
                    return;
                }
            }
                if (Configuration.DEBUG) {
                        Misc.println("Second Click Npc: " + player.npcType);
                }
                PickPocketing.attemptPickpocket(player, npc);

                switch (npcType) { 
                case 519:
                	ItemDegrade.openRepair(player);
                	break;
                case 599:
                        player.getPA().showInterface(3559);
                        player.canChangeAppearance = true;
                        break;

                case 494: // Banker.
                case 902: // Gundai.
                case 3021: // Tool Leprechaun.
                case 13455:
                        player.getPA().openUpBank(0);
                        break;
                        
                case 1597: //vannaka
                case 2270: //thief
                case 2862: //Death
                case 3786: //pc shop
                	ShopController.openShopOwner(player, npcType);
                	break;
                	
                	
                	
                case 5502://Grundt for quest
                	if (!ItemAssistant.playerHasItem(player, 2408) && ItemAssistant.freeSlots(player) > 0) {
	                	player.turnPlayerTo(npc.absX, npc.absY);
	                	player.startAnimation(881);
	                	ItemAssistant.addItem(player, 2408, 1);
                	}
                	break;
                }
        }

}