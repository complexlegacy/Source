package ionic.player.packets;

import ionic.player.Client;
import ionic.player.content.miscellaneous.Preset;

public class MultipleActionButtons implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.getInStream().readSignedWordBigEndianA();
        int button = c.getInStream().readUnsignedWordA();
        int slot = c.getInStream().readUnsignedWordBigEndian();
		
        if (button >= 31005 && button <= 31014) {
        	if (slot == 0) {
        		Preset.view(c, button - 31005);
        	}
        	if (slot == 2) {
        		c.presets[button - 31005] = null;
        		Preset.view(c, button - 31005);
        	}
        }
	}

}
