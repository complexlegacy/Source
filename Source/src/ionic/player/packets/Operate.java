package ionic.player.packets;

import ionic.item.ItemData;
import ionic.item.ItemDegrade;
import ionic.player.Client;
import ionic.player.content.miscellaneous.Teleport;


/**
 * @author Keith
 * Handles clicking operate on equipped items
 */

public class Operate implements PacketType {
	
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		
		@SuppressWarnings("unused") 
		int interfaceId = c.getInStream().readUnsignedWordA();
		int itemSlot = c.getInStream().readUnsignedWordA();
		int itemId = c.getInStream().readUnsignedWordA();
		if (c.isHerblore) { c.stopHerblore = true; }
		if (c.isSmithing) { c.stopSmithing = true; }
		if (c.isChopping) {
        	c.startAnimation(65535);
        	c.isChopping = false;
        }
		
		if (ItemData.data[itemId].degradable && itemId != 11283) {
			ItemDegrade.checkCharges(c, itemId, itemSlot, true);
			return;
		}
		
			if (itemId ==  20769 || itemId ==  20770) {
				c.getPA().showInterface(18700);
				return;
	        }
			if (itemId ==  22546) {
				c.getPA().showInterface(18800);
				return;
	        }
			if (itemId ==  22547) {
				c.getPA().showInterface(18801);
				return;
	        }
			
			if (itemId == 11283) {
				if (c.playerIndex > 0) {
					c.getCombat().handleDfs();
				} else if (c.npcIndex > 0) {
					c.getCombat().handleDfsNPC();
				}
				return;
			}

            if (itemId == 1712) {
            if (c.getCombat().inCombatAlert()) {
            		return;
            	}
                    Teleport.startTeleport(c, 3092, 3493, 0, "glory");
            	}

            if (c.getCombat().inPVPAreaOrCombat()) {
                    return;
            }
            switch (itemId) {
            case 19780:
            	c.startAnimation(14788);
                c.gfx0(1730);
            	break;
            case 11696:
                    c.startAnimation(11991);
                    c.gfx0(2114);
                    break;
            case 11698:
                    c.startAnimation(12019);
                    c.gfx0(2109);
                    break;
            case 11694:
                    c.startAnimation(11989);
                    c.gfx0(2113);
                    break;
            case 13902:
	                c.startAnimation(10505);
	                c.gfx0(1840);
	                break;
            case 11700:
                    c.startAnimation(7070);
                    c.gfx0(1221);
                    break;
            case 1305:
                    c.gfx100(248);
                    c.startAnimation(1058);
                    break;
            case 1249:
                    c.startAnimation(406);
                    c.gfx100(253);
                    break;
            case 3204:
                    c.gfx100(282);
                    c.startAnimation(1203);
                    break;
            case 4587:
                    c.gfx100(347);
                    c.startAnimation(1872);
                    break;

            case 1434:
                    c.startAnimation(1060);
                    c.gfx100(251);
                    break;
            case 10887:
                    c.gfx100(1027);
                    c.startAnimation(5870);
                    break;
            case 1215:
            case 1231:
            case 5680:
            case 5698:
                    c.gfx100(252);
                    c.startAnimation(1062);
                    break;
            case 11730:
                    c.startAnimation(7072);
                    c.gfx100(1224);
                    break;
            case 4151:
            case 15445:
            case 15444:
            case 15443:
            case 15442:
            case 15441:
                    c.startAnimation(11956);
                    break;
            case 14484:
                    c.startAnimation(10961);
                    c.gfx0(1950);
                    break;
            case 13905:
                    c.startAnimation(10499);
                    c.gfx0(1835);
                    break;
            case 13899:
                    c.startAnimation(10502);
                    break;
            case 4153:
                    c.startAnimation(1667);
                    c.gfx100(340);
                    break;
            case 1377:
                    c.gfx0(246);
                    c.forcedChat("Raarrrrrgggggghhhhhhh!");
                    c.startAnimation(1056);
                    break;
            }
		}
	

}
