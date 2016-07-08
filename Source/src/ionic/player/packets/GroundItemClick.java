package ionic.player.packets;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.content.skills.firemaking.Firemaking;
import ionic.player.dialogue.Dialogue;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import ionic.player.movement.Movement;

public class GroundItemClick implements PacketType {
	
	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
		
		if (player.playerIsFiremaking || player.doingAction(false) || player.getDoingAgility()|| player.isTeleporting() || player.didTeleport) {
			return;
		}
		if (player.isSmithing) { player.stopSmithing = true; }
		if (player.isHerblore) { player.stopHerblore = true; }
		if (player.isCrafting) { player.isCrafting = false; }
		
		player.pItemX = player.getInStream().readSignedWordBigEndian();
		player.pItemY = player.getInStream().readSignedWordBigEndianA();
		int itemId = player.getInStream().readUnsignedWordA();
		player.pItemId = itemId;
		if (itemId > 1510 && itemId < 1525) {
			
			if (!ItemAssistant.playerHasItem(player, 590)) {
				Dialogue.sendStatement2(player, new String[] {"You need a tinderbox to light these logs on fire."});
				return;
			}
			
			
			if (Math.abs(player.getX() - player.pItemX) > 25 || Math.abs(player.getY() - player.pItemY) > 25) {
            	Movement.resetWalkingQueue(player);
                    return;
            }
            player.getCombat().resetPlayerAttack();
            if (player.getX() == player.pItemX && player.getY() == player.pItemY) {
            	Firemaking.fireMake(player, itemId, 590, true, player.pItemX, player.pItemY);
            } else {
                    player.walkingToItem = true;

                    CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                    		@Override
                            public void execute(CycleEventContainer container) {
                                    if (player.walkingToItem) {
                                            if (player.getX() == player.pItemX && player.getY() == player.pItemY || player.goodDistance(player.getX(), player.getY(), player.pItemX, player.pItemY, 1)){
                                                    player.walkingToItem = false;
                                                    Firemaking.fireMake(player, itemId, 590, true, player.pItemX, player.pItemY);
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
	}