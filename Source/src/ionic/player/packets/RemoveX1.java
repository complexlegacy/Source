package ionic.player.packets;

import ionic.player.Client;
import ionic.player.interfaces.InterfaceAssistant;
/**
 * Bank X Items
 **/
public class RemoveX1 implements PacketType {

	public static final int PART1 = 135;
	public static final int	PART2 = 208;
	public int XremoveSlot, XinterfaceID, XremoveID, Xamount;
	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
		if (packetType == 135) {
			player.xRemoveSlot = player.getInStream().readSignedWordBigEndian();
			player.xInterfaceId = player.getInStream().readUnsignedWordA();
			player.xRemoveId = player.getInStream().readSignedWordBigEndian();
			if (player.xInterfaceId > 0)
			{
				InterfaceAssistant.updateChangeTitleTextOff(player);
			}
		}

		if(packetType == PART1) {
			synchronized(player) {
				player.getOutStream().createFrame(27);
			}			
		}
	
	}
}
