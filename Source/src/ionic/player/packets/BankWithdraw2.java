package ionic.player.packets;

import ionic.player.Client;
import ionic.player.banking.BankWithdrawing;

/**
 * @author Keith
 * Packet that actually handles withdrawing the items
 * ID : 196
 */

public class BankWithdraw2 implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int tab = c.getInStream().readUnsignedWord();
		int slot = c.getInStream().readUnsignedWord();
		int item = c.getInStream().readUnsignedWord();
		BankWithdrawing.handle(c, tab, slot, item, c.getBank().withdrawAmountReceived);
	}

}
