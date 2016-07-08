package ionic.player.packets;

import ionic.player.Client;
import ionic.player.banking.SwapBankTab;


public class BankDragToTab implements PacketType {
	
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int fromTab = c.getInStream().readUnsignedWord();
		int toTab = c.getInStream().readUnsignedWord();
		int fromSlot = c.getInStream().readUnsignedWord();
		int itemId = c.getInStream().readUnsignedWord();
		
		SwapBankTab.handle(c,fromTab, toTab, fromSlot, itemId);
	}	
}
