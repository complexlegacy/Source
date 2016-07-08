package ionic.player.packets;

import ionic.player.Client;
import ionic.player.banking.MoveItemSlot;


public class BankDragSwap implements PacketType {
	
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int fromTab = c.getInStream().readUnsignedWord();
		int toTab = c.getInStream().readUnsignedWord();
		int fromSlot = c.getInStream().readUnsignedWord();
		int toSlot = c.getInStream().readUnsignedWord();
		int itemId = c.getInStream().readUnsignedWord();
		int itemIdSwapped = c.getInStream().readUnsignedWord();
		
		MoveItemSlot.handle(c,fromTab, toTab, fromSlot, toSlot, itemId, itemIdSwapped);
		
	}	
}
