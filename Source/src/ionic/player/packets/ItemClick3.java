package ionic.player.packets;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.content.miscellaneous.EssencePouch;
import ionic.player.content.miscellaneous.Teleport;
import ionic.player.content.skills.firemaking.Firemaking;
import ionic.player.content.skills.summoning.Summoning;
import ionic.player.content.skills.summoning.SummoningData;
import ionic.player.dialogue.Dialogue;
import utility.Misc;
import core.Configuration;


public class ItemClick3 implements PacketType {

	public void processPacket(Client player, int packetType, int packetSize) {
	
		int itemId11 = player.getInStream().readSignedWordBigEndianA();
		int itemId1 = player.getInStream().readUnsignedWordA();
		int itemId = player.getInStream().readSignedWordA();
		
		if (!ItemAssistant.playerHasItem(player, itemId)) {
        	return;
        }
		
		if (player.barrowsDoor) { return;}
		if (player.isSmithing) { player.stopSmithing = true; }
		
		SummoningData.pouchData p = SummoningData.forID(itemId);
        if (p != null) {
        	Summoning.summon(player, p);
        	return;
        }
		
		switch (itemId) {
		
		case 6952:
      	  EssencePouch.deposit(player);
      	  break;
		
		case 995:
			player.getPouch().deposit(ItemAssistant.getItemAmount(player, 995));
		break;
		
		case 1511:
		case 1521:
		case 1519:
		case 1515:
		case 1517:
		case 1513:
			if (ItemAssistant.playerHasItem(player, 590)) {
				Firemaking.fireMake(player, itemId, 590, false, 0, 0);
			} else {
				Dialogue.sendStatement2(player, new String[] {"You need a tinderbox to light these logs."});
			}
			break;
		
		// Amulet of glory, Edgeville teleport
		case 1712:
                Teleport.startTeleport(player, 3086, 3494, 0, "glory");
		break;
		
			
		default:
			if (Configuration.DEBUG)
	        {
				Misc.println("[3rd Item Click] [Item ID:"+itemId+"] Other: "+itemId11+" : "+itemId1);
	        }
			break;
		}

	}

}
