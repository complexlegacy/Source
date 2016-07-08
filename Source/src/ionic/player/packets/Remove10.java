package ionic.player.packets;

import ionic.player.Client;
import ionic.player.content.gambling.GamblingHandler;
import ionic.player.content.miscellaneous.PriceChecker;
import ionic.player.content.skills.crafting.JewelryMaking;
import ionic.player.content.skills.smithing.Smithing;
import core.Constants;


public class Remove10 implements PacketType {

	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
	int interfaceId = player.getInStream().readUnsignedWordBigEndian();
	int removeId = player.getInStream().readUnsignedWordA();
	int removeSlot = player.getInStream().readUnsignedWordA();
	
					
		switch(interfaceId){
		
		case 37112:
			PriceChecker.remove(player, removeId, removeSlot, 10);
			break;
		
		case 37221:
			GamblingHandler.remove(player, removeId, removeSlot, 10);
			break;
		
		case 4233:
			JewelryMaking.jewelryMaking(player, "RING", removeId, 10);
			break;
		case 4239:
			JewelryMaking.jewelryMaking(player, "NECKLACE", removeId, 10);
			break;
		case 4245:
			JewelryMaking.jewelryMaking(player, "AMULET", removeId, 10);
			break;

			
			case 3322:
			if(player.duelStatus <= 0) { 
                player.getTradeAndDuel().tradeItem(removeId, removeSlot, 10);
           	} else {
				player.getDuelArena().stakeItem(removeId, removeSlot, 10);
			}	
			break;
			
			case 3415:
			if(player.duelStatus <= 0) { 
				player.getTradeAndDuel().fromTrade(removeId, removeSlot, 10);
           	} 
			break;
			
			case 6669:
			player.getDuelArena().fromDuel(removeId, removeSlot, 10);
			break;
			

			case 1119:
			case 1120:
			case 1121:
			case 1122:
			case 1123:
				Smithing.readInput(player.skillLevel[Constants.SMITHING], Integer.toString(removeId), player, 10);
			break;
		}	
	}

}
