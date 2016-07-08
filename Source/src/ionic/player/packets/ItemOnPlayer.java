package ionic.player.packets;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.content.minigames.CastleWars;
import ionic.player.content.miscellaneous.Crackers;
import ionic.player.content.miscellaneous.SnowBalls;

public class ItemOnPlayer implements PacketType {
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.getInStream().readUnsignedWordBigEndianA();
		int playerIndex = c.inStream.readUnsignedWord();
		int item = c.inStream.readUnsignedWord();
		c.inStream.readSignedWordBigEndian();
		
		if (!ItemAssistant.playerHasItem(c, item)) {
			return;
		}
		
		Player usedOn = PlayerHandler.players[playerIndex];
		
		if (c.isSmithing) { c.stopSmithing = true; }
		if (c.isCrafting) { c.isCrafting = false; }
		if (usedOn == null) { 
			return; 
		}
		
		switch (item) {
		case 962:
			Crackers.handleCrackers(c, usedOn, item);
		break;
		
		case 4049:
			if (CastleWars.inCastleWars(c) && CastleWars.inCastleWars(usedOn) && usedOn.inCastleWars && c.inCastleWars) {
				if (usedOn.cwTeam == c.cwTeam) {
					ItemAssistant.deleteItem(c, 4049, 1);
					usedOn.skillLevel[3] += usedOn.calculateMaxLifePoints() / 10;
					if (usedOn.skillLevel[3] > usedOn.calculateMaxLifePoints()) {
						usedOn.skillLevel[3] = usedOn.calculateMaxLifePoints();
					}
					usedOn.getPA().refreshSkill(3);
					usedOn.sendMessage(""+c.playerName+" has healed you with some bandages");
					c.sendMessage("You use your bandages to heal "+usedOn.playerName);
					usedOn.startAnimation(829);
				} else {
					c.sendMessage("You can only heal players on your team.");
				}
			}
			break;
		
		default:
			c.sendMessage("Nothing interesting happens.");
		break;
		}

	}

}