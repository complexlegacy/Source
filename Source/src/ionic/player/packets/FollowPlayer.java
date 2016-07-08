package ionic.player.packets;

import ionic.player.Client;
import ionic.player.PlayerHandler;

/**
 * Follow Player
 **/
public class FollowPlayer implements PacketType {
	
	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
		int followPlayer = player.getInStream().readUnsignedWordBigEndian();
		if(PlayerHandler.players[followPlayer] == null  || player.barrowsDoor) 
		{
			return;
		}
		if (player.isCrafting) { player.isCrafting = false; }
		if (player.isChopping) {
        	player.startAnimation(65535);
        	player.isChopping = false;
        }
		player.setUsingRange(false);
		player.playerIndex = 0;
		player.npcIndex = 0;
		player.mageFollow = false;
		player.usingBow = false;
		player.usingRangeWeapon = false;
		player.followDistance = 1;
		player.followId = followPlayer;
	}	
}
