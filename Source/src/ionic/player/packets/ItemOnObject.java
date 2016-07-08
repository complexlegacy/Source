package ionic.player.packets;

import ionic.item.UseItem;
import ionic.player.Client;
import ionic.player.content.combat.dwarfcannon.CannonHandler;
import ionic.player.content.skills.cooking.Cooking;



public class ItemOnObject implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.getInStream().readUnsignedWord();
		int objectId = c.getInStream().readSignedWordBigEndian();
		int objectY = c.getInStream().readSignedWordBigEndianA();
		c.getInStream().readUnsignedWord();
		int objectX = c.getInStream().readSignedWordBigEndianA();
		int itemId = c.getInStream().readUnsignedWord();
		if (c.isSmithing) { c.stopSmithing = true; }
		if (c.isHerblore) { c.stopHerblore = true; }
		UseItem.ItemonObject(c, objectId, objectX, objectY, itemId);
		c.turnPlayerTo(objectX, objectY);
		switch (objectId) {
			case 6:
				if (itemId == 2)
					CannonHandler.loadCannon(c, objectX, objectY);
				break;
			case 12269:
			case 2732:
			case 114:
			case 9374:
			case 2728:
			case 3038:
			case 11404:
			case 11405:
			case 11406:
				Cooking.cookThisFood(c, itemId, objectId); 
				break;
		}
	}

}
