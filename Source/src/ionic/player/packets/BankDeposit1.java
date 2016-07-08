package ionic.player.packets;

import ionic.player.Client;

/**
 * @author Keith
 * Packet that sets the amount to deposit
 * Sent right before the BankDeposit2
 * ID : 197
 */

public class BankDeposit1 implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int amount = c.getInStream().readDWord();
		c.getBank().depositAmountReceived = amount;
	}

}
