package ionic.player.packets;

import ionic.item.ItemAssistant;
import ionic.player.Client;


/**
 * Wear Item
 **/
 
public class WearItem implements PacketType
{

    @
    Override
    public void processPacket(Client player, int packetType, int packetSize) {
    	if (player.doingAction(false) || player.getDoingAgility() || player.isTeleporting() || player.isDead || player.barrowsDoor){
    		return;
    	}
    	if (player.gamble != null) {
			return;
		}
    	boolean nexEffectChanged = false;
        player.wearId = player.getInStream().readUnsignedWord();
        player.wearSlot = player.getInStream().readUnsignedWordA();
        player.interfaceId = player.getInStream().readUnsignedWordA();
        
        if (player.isSmithing) { player.stopSmithing = true; }
        if (player.isHerblore) { player.stopHerblore = true; }
		
		if (!ItemAssistant.playerHasItem(player, player.wearId, 1, player.wearSlot)) {
			return;
		}
		if (player.resting) {
			player.getPA().stopResting();
		}

        if ((player.playerIndex > 0 || player.npcIndex > 0) && player.wearId != 4153) {
            player.getCombat().resetPlayerAttack();
        }
        if (player.wearId == 4153) {
        	player.usedGmaul = true;
        }
        if (player.wearSlot == 0 || player.wearSlot == 4 || player.wearSlot == 7) {
			if (player.hasNexItems())
				nexEffectChanged = true;
		}
        ItemAssistant.wearItem(player, player.wearId, player.wearSlot);
        if (nexEffectChanged && player.skillLevel[3] > player.calculateMaxLifePoints()) {
        	player.skillLevel[3] = player.calculateMaxLifePoints();
        	player.getPA().refreshSkill(3);
		}
    }

}