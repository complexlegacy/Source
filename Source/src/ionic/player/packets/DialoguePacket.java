package ionic.player.packets;

import ionic.player.Client;
import ionic.player.dialogue.Dialogues;


/**
 * Dialogue
 **/
public class DialoguePacket implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		
		Dialogues.next(c);
		
	}

}
