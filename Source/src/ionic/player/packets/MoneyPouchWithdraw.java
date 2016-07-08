package ionic.player.packets;

import ionic.player.Client;
import utility.Misc;


public class MoneyPouchWithdraw implements PacketType {
		@Override
        public void processPacket(Client c, int packetType, int packetSize) {
			String textSent = Misc.longToPlayerName2(c.getInStream().readQWord());
			textSent = textSent.replaceAll("_", " ");
			try {
				c.getPouch().withdraw(Integer.parseInt(textSent));
			} catch(Exception e) {
				c.getPouch().withdraw(Integer.MAX_VALUE);
			}
		}
}