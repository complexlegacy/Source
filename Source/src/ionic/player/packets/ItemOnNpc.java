package ionic.player.packets;

import ionic.item.UseItem;
import ionic.npc.NPCHandler;
import ionic.player.Client;


public class ItemOnNpc implements PacketType {

	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
		int itemId = player.getInStream().readSignedWordA();
		int i = player.getInStream().readSignedWordA();
		int slot = player.getInStream().readSignedWordBigEndian();
		int npcId = NPCHandler.npcs[i].npcType;
		
		if (player.isSmithing) { player.stopSmithing = true; }
		
		UseItem.ItemonNpc(player, itemId, npcId, slot);
		/* Example of how to only be able to use dragon claws on the player's own pet to receive the following message.
		if (itemId == 14484)
		{
			if (NPCHandler.npcs[i].summonedBy == player.playerId)
			{
				player.sendMessage("Your pet.");
			}
		}
		*/
	}
}
