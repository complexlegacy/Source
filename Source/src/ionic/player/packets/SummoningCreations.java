package ionic.player.packets;

import ionic.player.Client;
import ionic.player.content.skills.summoning.PouchAndScrollCreation;

public class SummoningCreations implements PacketType {
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int type = c.getInStream().readSignedWordBigEndianA();
        int amount = c.getInStream().readUnsignedWordA();
        int slot = c.getInStream().readUnsignedWordBigEndian();
        
        if (type == 1) {
        	PouchAndScrollCreation.createPouch(c, slot, amount);
        } else if (type == 2) {
        	PouchAndScrollCreation.createScroll(c, slot, amount);
        }
	}	
}
