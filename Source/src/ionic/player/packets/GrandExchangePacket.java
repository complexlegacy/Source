package ionic.player.packets;

import ionic.grandExchange.Interfaces;
import ionic.grandExchange.Other;
import ionic.player.Client;

public class GrandExchangePacket implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int item = c.getInStream().readDWord();
		if (item >= 1000000) {
			Other.applySell(c, item - 1000000);
		} else {
			Interfaces.clickSearch(c, item);
		}
	}

}
