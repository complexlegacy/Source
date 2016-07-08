package ionic.player.packets;

import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.item.ItemDegrade;
import ionic.player.Client;
import ionic.player.content.miscellaneous.*;
import utility.Misc;
import core.Configuration;
import core.Constants;


public class ItemClick2 implements PacketType {

	@Override
	public void processPacket(Client player, int packetType, int packetSize) 
	{
		player.getInStream().readSignedWordBigEndianA();
        int slot = player.getInStream().readUnsignedWordA();
        int itemId = player.getInStream().readUnsignedWordBigEndian();
        if (itemId != player.playerItems[slot] - 1) {
            return;
        }
        if (!ItemAssistant.playerHasItem(player, itemId)) {
        	return;
        }
        
		
		if (player.isSmithing) { player.stopSmithing = true; }
		
		ItemCombinations.split(player, itemId);
		
		if (ItemData.data[itemId].degradable) {
			ItemDegrade.checkCharges(player, itemId, slot, false);
		}

		switch (itemId) {
		
		case 6952:
      	  EssencePouch.withdraw(player);
      	  break;
		
		// Completionist cape
		case 20769:
			player.getPA().showInterface(18700);
			break;

		default:
			if (Configuration.DEBUG)
	        {
				Misc.println(player.playerName+ " - Item3rdOption: "+itemId);
	        }
			break;
		}

	}

}
