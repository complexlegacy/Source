package ionic.player.packets;

import ionic.player.Client;
import core.Configuration;

/**
 * Trading
 */
public class Trade implements PacketType {

	@Override
	public void processPacket(Client player, int packetType, int packetSize) 
	{
		int tradeId = player.getInStream().readSignedWordBigEndian();
		player.getPA().resetFollow();
		if (player.disconnected) 
		{
			player.tradeStatus = 0;
		}
        if (player.inTrade || player.arenas() || tradeId == player.playerId || player.barrowsDoor) 
        {
        	return;
		}
		if((player.isIronman()) && !Configuration.DEBUG)
		{
			player.sendMessage("You are unable to trade.");
			return;
		}
		if (tradeId != player.playerId)
		{
			player.getTradeAndDuel().requestTrade(tradeId);
		}
	}
		
}
