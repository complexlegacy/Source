package ionic.player.packets;

import ionic.grandExchange.Other;
import ionic.player.Client;
import ionic.player.content.gambling.GamblingHandler;
import ionic.player.content.miscellaneous.PriceChecker;
import ionic.player.content.skills.crafting.JewelryMaking;
import ionic.player.content.skills.smithing.Smithing;
import core.Constants;

public class Remove5 implements PacketType {

	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
	int interfaceId = player.getInStream().readSignedWordBigEndianA();
	int removeId = player.getInStream().readSignedWordBigEndianA();
	int removeSlot = player.getInStream().readSignedWordBigEndian();
	
		switch(interfaceId){
		
		case -14517:
			Other.handleCollect(player, "gecollectitemunnoted");
			break;
		
		case -28424:
			PriceChecker.remove(player, removeId, removeSlot, 5);
			break;
			
		case -28315:
			GamblingHandler.remove(player, removeId, removeSlot, 5);
			break;
		
		case 4233:
			JewelryMaking.jewelryMaking(player, "RING", removeId, 5);
			break;
		case 4239:
			JewelryMaking.jewelryMaking(player, "NECKLACE", removeId, 5);
			break;
		case 4245:
			JewelryMaking.jewelryMaking(player, "AMULET", removeId, 5);
			break;

			
			case 3322:
			if(player.duelStatus <= 0) { 
                player.getTradeAndDuel().tradeItem(removeId, removeSlot, 5);
           	} else {
				player.getDuelArena().stakeItem(removeId, removeSlot, 5);
			}	
			break;
			
			case 3415:
			if(player.duelStatus <= 0) { 
				player.getTradeAndDuel().fromTrade(removeId, removeSlot, 5);
			}
			break;
			
			case 6669:
			player.getDuelArena().fromDuel(removeId, removeSlot, 5);
			break;

			case 1119:
			case 1120:
			case 1121:
			case 1122:
			case 1123:
				Smithing.readInput(player.skillLevel[Constants.SMITHING], Integer.toString(removeId), player, 5);
			break;
			
		}
	}

}
