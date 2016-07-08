package ionic.player.packets;

import ionic.player.Client;
import utility.Misc;
import core.Configuration;

public class ItemOnGroundItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.getInStream().readSignedWordBigEndian();
		int itemUsed = c.getInStream().readSignedWordA();
		int groundItem = c.getInStream().readUnsignedWord();
		c.getInStream().readSignedWordA();
		c.getInStream().readSignedWordBigEndianA();
		c.getInStream().readUnsignedWord();
		
		if (c.isHerblore) { c.stopHerblore = true; }
		if (c.isCrafting) { c.isCrafting = false; }
		switch(itemUsed) {
		
		default:
			if (Configuration.DEBUG)
	        {
				Misc.println("ItemUsed "+itemUsed+" on Ground Item "+groundItem);
	        }
			break;
		}
	}

}
