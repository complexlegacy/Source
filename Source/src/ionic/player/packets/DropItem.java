package ionic.player.packets;
import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.npc.pet.DropPet;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.dialogue.Dialogue;
import ionic.player.dialogue.DialogueList;
import ionic.player.dialogue.DialogueType;
import ionic.player.dialogue.Dialogues;
import core.Constants;
import core.Server;

/**
 * Drop Item
 **/

public class DropItem implements PacketType
{

        @
        Override
        public void processPacket(Client player, int packetType, int packetSize)
        {
        		if (player.doingAction(false) || player.getDoingAgility() || player.isTeleporting() || player.isDead || player.inTrade || 
        			System.currentTimeMillis() - player.alchDelay < 1800 || player.barrowsDoor)
        		{
        			return;
        		}
        	
                int itemId = player.getInStream().readUnsignedWordA();
                player.getInStream().readUnsignedByte();
                player.getInStream().readUnsignedByte();
                int slot = player.getInStream().readUnsignedWordA();
                boolean droppable = true;
                
                if (!ItemAssistant.playerHasItem(player, itemId)) {
                	return;
                }
                
                if (ItemData.data[itemId].degraded) {
                	Dialogues.send(player, new DialogueList(
                			new Dialogue[] {
                			new Dialogue(DialogueType.STATEMENT, new String[] {
                					"This item is degraded, but still has some charges left", 
                					"if you confirm to drop this item, it will break completely.", 
                					"Do you wish to drop it?"}),
                			new Dialogue(DialogueType.OPTIONS, new String[] {"Yes", "No"}, new Dialogue.Options() {
								@Override
								public void click(Player c, int option) {
									switch(option) {
										case 1:
											c.getPA().closeAllWindows();
											Server.itemHandler.createGroundItem(player, itemId, player.getX(), player.getY(), player.playerItemsN[slot], player.getId(), player.inWilderness() ? true : false);
			                        		ItemAssistant.deleteItem(player, itemId, slot, player.playerItemsN[slot]);
											break;
										case 2:
											c.getPA().closeAllWindows();
											break;
									}
								}
							})}));
                	return;
                }
                
                
                for (int j = 0; j < Constants.ITEMS_TO_INVENTORY_ON_DEATH.length; j++)
                {
                        if (itemId == Constants.ITEMS_TO_INVENTORY_ON_DEATH[j])
                        {
                        	player.sendMessage("You cannot drop this item.");
                            return;
                        }
                }
                
                        if (ItemData.data[itemId].isUntradable())
                        {
                        	player.sendMessage("You cannot drop this item.");
                            return;
                        }
                
                for (int j = 0; j < Constants.ITEMS_TO_DISSAPEAR.length; j++)
                {
                        if (itemId == Constants.ITEMS_TO_DISSAPEAR[j])
                        {
                        	player.sendMessage("You cannot drop this item.");
                            return;
                        }
                }
                
                DropPet.dropPetRequirements(player, itemId, slot);
                if (player.getPetSummoned())
                {
                	return;
                }
                
                if (itemId == 4045)
                {
                        if (player.skillLevel[3] <= 15)
                        {
                                player.sendMessage("You are unable to kill yourself.");
                                return;
                        }
                        player.startAnimation(827);
                        ItemAssistant.deleteItem(player, itemId, slot, player.playerItemsN[slot]);
                        player.getCombat().appendHit(player, 15, 0, -1, false);
                        player.forcedText = "Ow! That really hurt!";
                        player.forcedChatUpdateRequired = true;
                        player.getPA().refreshSkill(3);

                }
                if (player.playerItemsN[slot] != 0 && itemId != -1 && player.playerItems[slot] == itemId + 1)
                {
                        if (droppable)
                        {
                        		Server.itemHandler.createGroundItem(player, itemId, player.getX(), player.getY(), player.playerItemsN[slot], player.getId(), player.inWilderness() ? true : false);
                        		ItemAssistant.deleteItem(player, itemId, slot, player.playerItemsN[slot]);
                        }
                }
        }
}