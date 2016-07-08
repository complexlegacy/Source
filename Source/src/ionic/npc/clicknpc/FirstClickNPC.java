package ionic.npc.clicknpc;

import ionic.npc.NPC;
import ionic.npc.NPCHandler;
import ionic.npc.pet.Pet;
import ionic.player.Client;
import ionic.player.content.quest.QuestHandler.QuestList;
import ionic.player.content.skills.crafting.Tanning;
import ionic.player.content.skills.fishing.Fishing;
import ionic.player.content.skills.summoning.FamiliarInteraction;
import ionic.player.dialogue.Dialogues;
import ionic.shop.ShopController;
import utility.Misc;
import core.Configuration;


public class FirstClickNPC {
        public static void firstClickNpc(Client player, int npcType) {
        	NPC npc = NPCHandler.npcs[player.npcClickIndex];
                player.clickNpcType = 0;
                Pet.pickUpPetRequirements(player, npcType);
                player.npcClickIndex = 0;

                if (player.isTeleporting()) {
                        return;
                }
                if (Configuration.DEBUG) {
                        Misc.println("First click Npc: " + player.npcType);
                }
                
                if (player.familiar != null) {
                	if (player.familiar == npc) {
                		FamiliarInteraction.interactWithFamiliar(player, player.famType, player.famType.getName());
                	} else {
                		player.sendMessage("This is not your familiar.");
                	}
                }
                 if (Fishing.fishingNPC(npcType)) {
                    player.fishTimer = 3;
                    Fishing.fishingNPC(player, 1, npcType);
                return;
                 }

                switch (npcType){
                case 804:
        			Tanning.sendTanningInterface(player);
        			break;
                case 241:
        			ShopController.openShopOwner(player, npcType);
        		break;
                case 494://bankers
                case 902:
                case 13455:
                        player.getPA().openUpBank(0);
                        break;
                        
                case 599://makeover mage
                	Dialogues.send(player, Dialogues.MAKEOVER_MAGE);
                    break;
                    
                case 1597://vannaka
                	Dialogues.send(player, Dialogues.SLAYER);
                	break;
               
                case 659://party pete
                	Dialogues.send(player, Dialogues.PARTY_PETE);
                	break;
                	
                case 6524:
                	Dialogues.send(player, Dialogues.DECANT);
                	break;
                	
                case 2270://thief
                	Dialogues.send(player, Dialogues.THIEF);
                	break;

                default:
                        break;
                }
                
                
                
                for (QuestList q : QuestList.values()) {
                	if (q.getQuest().talkToNpc(player, npcType)) {
                		return;
                	}
                }
                
                
        }

}