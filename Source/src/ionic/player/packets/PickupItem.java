package ionic.player.packets;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import ionic.player.movement.Movement;
import core.Server;


/**
 * Pickup Item
 **/
public class PickupItem implements PacketType {

        @
        Override
        public void processPacket(final Client player, int packetType, int packetSize) {
        		if (player.playerIsFiremaking || player.doingAction(false) || player.getDoingAgility()|| player.isTeleporting() || player.didTeleport) {
        				return;
        		}
        		if (player.isHerblore) { player.stopHerblore = true; }
                player.pItemY = player.getInStream().readSignedWordBigEndian();
                player.pItemId = player.getInStream().readUnsignedWord();
                player.pItemX = player.getInStream().readSignedWordBigEndian();
                if (player.isSmithing) { player.stopSmithing = true; }
                if (Math.abs(player.getX() - player.pItemX) > 25 || Math.abs(player.getY() - player.pItemY) > 25) {
                	Movement.resetWalkingQueue(player);
                        return;
                }
                player.getCombat().resetPlayerAttack();
                if (player.getX() == player.pItemX && player.getY() == player.pItemY) {
                	if (ItemAssistant.freeSlots(player) > 0)  {
                        Server.itemHandler.removeGroundItem(player, player.pItemId, player.pItemX, player.pItemY, true);
                    } else {
                		player.sendMessage("You do not have enough inventory space.");
                	}
                } else {
                        player.walkingToItem = true;

                        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                        		@Override
                                public void execute(CycleEventContainer container) {
                                        if (player.walkingToItem) {
                                                if (player.getX() == player.pItemX && player.getY() == player.pItemY || player.goodDistance(player.getX(), player.getY(), player.pItemX, player.pItemY, 1)){
                                                        player.walkingToItem = false;
                                                        if (ItemAssistant.freeSlots(player) > 0)  {
                                                        	Server.itemHandler.removeGroundItem(player, player.pItemId, player.pItemX, player.pItemY, true);
                                                        }else{
                                                    		player.sendMessage("You do not have enough inventory space.");
                                                    	}
                                                }
                                        } else {
                                        	container.stop();
                                        }
                                }
                        		@Override
                                public void stop() {

                                }
                        }, 1);

                }

        }

}