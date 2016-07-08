package ionic.player.packets;

import ionic.player.Client;

/**
 * @author Keith
 * Packet that sets the amount to withdraw
 * Sent right before the BankWithdraw2
 * ID : 195
 */

public class BankWithdraw1 implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int amount = c.getInStream().readDWord();
		c.getBank().withdrawAmountReceived = amount;
	}

}
