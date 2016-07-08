package ionic.player.packets;

import ionic.item.ItemAssistant;
import ionic.player.Client;


/**
 * Magic on items
 **/
public class MagicOnItems implements PacketType {

	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
		int slot = player.getInStream().readSignedWord();
		int itemId = player.getInStream().readSignedWordA();
		@SuppressWarnings("unused")
		int junk = player.getInStream().readSignedWord(); /// WHEN I REMOVE THIS LINE, WHY DOES ALCHING NOT WORK ANYMORE, THIS LOCAL VARIABLE IS NOT EVEN USED LOCALLY?!?!?!
		@SuppressWarnings("unused")
		int spellId = player.getInStream().readSignedWordA();
        if (!ItemAssistant.playerHasItem(player, itemId, 1, slot)) {
            return;
        }
		
		player.usingMagic = true;
		// Magic on item code here.
		player.usingMagic = false;

	}

}
