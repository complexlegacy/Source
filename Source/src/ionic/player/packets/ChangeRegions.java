package ionic.player.packets;

import ionic.player.Client;
import ionic.player.content.miscellaneous.Skull;
import core.Server;

/**
 * Change Regions
 */
public class ChangeRegions implements PacketType {

	@Override
	public void processPacket(Client player, int packetType, int packetSize)
	{
		Server.itemHandler.reloadItems(player);
		Server.objectManager.loadObjects(player);
		player.saveFile = true;
		player.getPA().loadWalkableInterfaces();
    	Skull.updateSkullAppearance(player);
	    //RegionMusic.playSound(player);
	}
		
}
