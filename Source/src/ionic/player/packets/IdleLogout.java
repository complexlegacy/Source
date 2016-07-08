package ionic.player.packets;


import ionic.player.Client;


public class IdleLogout implements PacketType {
	
	
	/**
	 * This is called when the player is idle for a while.
	 */
	@Override
	public void processPacket(Client player, int packetType, int packetSize) 
	{
	}
}