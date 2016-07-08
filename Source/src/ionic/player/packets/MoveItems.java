package ionic.player.packets;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.banking.MoveItemSlot;

/**
 * Move Items
 **/
public class MoveItems implements PacketType {

	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
		int interfaceId = player.getInStream().readUnsignedWordBigEndianA();
		byte insert = player.getInStream().readSignedByteC();
		int itemFrom = player.getInStream().readUnsignedWordBigEndianA();
		int itemTo = player.getInStream().readUnsignedWordBigEndian();
		
		if (interfaceId >= 45100 && interfaceId <= 45109) {
			int tab = interfaceId - 45100;
			MoveItemSlot.handle(player, tab, tab, itemFrom, itemTo, 
					player.getBank().bankItems[tab][itemFrom], player.getBank().bankItems[tab][itemTo]);
		}

		if(player.inTrade) {
			player.getTradeAndDuel().declineTrade();
                             		return;
                        	}
		if(player.tradeStatus == 1) {
			player.getTradeAndDuel().declineTrade();
                             		return;
                        	}
		if(player.duelStatus == 1) {
			player.getDuelArena().declineDuel();
			return;
		}
		ItemAssistant.moveItems(player, itemFrom, itemTo, interfaceId, insert);
	}
}
