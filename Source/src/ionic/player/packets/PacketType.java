package ionic.player.packets;

import ionic.player.Client;


	
public interface PacketType
{
	public void processPacket(Client c, int packetType, int packetSize);
}

