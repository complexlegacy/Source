package ionic.player.packets;

import ionic.grandExchange.Other;
import ionic.player.Client;

/**
 * Bank X Items
 **/

public class RemoveX2 implements PacketType {
	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
		int amount = player.getInStream().readDWord();
		if (amount == 0 && !player.changeTitleInput) {
			return;
		}
		if (amount < 0) {
			return;
		} else if (player.xInterfaceId == 3322) {
			if (player.duelStatus <= 0) {
				player.getTradeAndDuel().tradeItem(player.xRemoveId, player.xRemoveSlot, amount);
			} else {
				player.getDuelArena().stakeItem(player.xRemoveId, player.xRemoveSlot, amount);
			}

		} else if (player.xInterfaceId == 3415) {
			if (player.duelStatus <= 0) {
				player.getTradeAndDuel().fromTrade(player.xRemoveId, player.xRemoveSlot, amount);
			}

		} else if (player.xInterfaceId == 6669) {
			player.getDuelArena().fromDuel(player.xRemoveId, player.xRemoveSlot, amount);

		} else if (player.changeTitleInput) {
			player.getPA().setTitle(amount);
			player.changeTitleInput = false;
			player.sendMessage("You have successfully changed your title.");

		}

		switch (player.inputAction) {
		case "editSellAmount":
			Other.customSellAmount(player, amount);
			break;
		case "editSellPrice":
			Other.customSellPrice(player, amount);
			break;
		case "editBuyAmount":
			Other.customBuyAmount(player, amount);
			break;
		case "editBuyPrice": 
			Other.customBuyPrice(player, amount);
			break;
		}
		player.inputAction = "";
	}
}