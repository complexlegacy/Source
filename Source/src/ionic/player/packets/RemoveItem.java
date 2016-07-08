package ionic.player.packets;

import ionic.grandExchange.Other;
import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.content.gambling.GamblingHandler;
import ionic.player.content.miscellaneous.PriceChecker;
import ionic.player.content.skills.crafting.JewelryMaking;
import ionic.player.content.skills.smithing.Smithing;
import core.Constants;


public class RemoveItem implements PacketType {

	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
		int interfaceId = player.getInStream().readUnsignedWordA();
		int removeSlot = player.getInStream().readUnsignedWordA();
		int removeId = player.getInStream().readUnsignedWordA();
		
		switch(interfaceId) {
		
		case 51019:
			Other.handleCollect(player, "gecollectitemnoted");
			break;
			
		case 51020:
			Other.handleCollect(player, "gecollectcash");
			break;
		
		case 37221:
			GamblingHandler.remove(player, removeId, removeSlot, 1);
			break;
		
		case 37112:
			PriceChecker.remove(player, removeId, removeSlot, 1);
			break;
		
		case 4233:
			JewelryMaking.jewelryMaking(player, "RING", removeId, 1);
			break;
		case 4239:
			JewelryMaking.jewelryMaking(player, "NECKLACE", removeId, 1);
			break;
		case 4245:
			JewelryMaking.jewelryMaking(player, "AMULET", removeId, 1);
			break;
			
			case 1688:
			ItemAssistant.removeItem(player, removeId, removeSlot);
			break;
			
			case 3322:
				if(player.duelStatus <= 0 && player.inTrade) 
				{ 
	                player.getTradeAndDuel().tradeItem(removeId, removeSlot, 1);
	           	} 
				if (player.duelStatus == 1)
				{
					player.getDuelArena().stakeItem(removeId, removeSlot, 1);
				}
				break;
			
			case 3415:
			if(player.duelStatus <= 0) 
			{ 
				player.getTradeAndDuel().fromTrade(removeId, removeSlot, 1);
           	} 
			break;
			
			case 6669:
			player.getDuelArena().fromDuel(removeId, removeSlot, 1);
			break;
			

			case 1119:
			case 1120:
			case 1121:
			case 1122:
			case 1123:
				Smithing.readInput(player.skillLevel[Constants.SMITHING], Integer.toString(removeId), player, 1);
			break;
		}
	}
			
}
