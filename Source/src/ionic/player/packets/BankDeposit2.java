package ionic.player.packets;

import ionic.player.Client;
import ionic.player.banking.BankDepositing;

/**
 * @author Keith
 * Packet that actually handles depositing the items
 * ID : 198
 */

public class BankDeposit2 implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int slot = c.getInStream().readUnsignedWord();
		int item = c.getInStream().readUnsignedWord();
		BankDepositing.handle(c, slot, item, c.getBank().depositAmountReceived, false);
	}

}
