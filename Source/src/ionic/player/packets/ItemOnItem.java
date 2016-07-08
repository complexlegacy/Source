package ionic.player.packets;

/**
 * @author Ryan / Lmctruck30
 */

import ionic.item.ItemAssistant;
import ionic.item.UseItem;
import ionic.player.Client;
import ionic.player.content.miscellaneous.ItemCombinations;
import ionic.player.content.miscellaneous.WhipColouring;
import ionic.player.content.skills.firemaking.Firemaking;
import ionic.player.content.skills.fletching.FletchingUseItem;
import ionic.player.content.skills.woodcutting.HatchetAttaching;

public class ItemOnItem implements PacketType {

	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
		
		int usedWithSlot = player.getInStream().readUnsignedWord();
		int itemUsedSlot = player.getInStream().readUnsignedWordA();
		int useWith = player.playerItems[usedWithSlot] - 1;
		int itemUsed = player.playerItems[itemUsedSlot] - 1;
		
		if (player.isSmithing) { player.stopSmithing = true; }
		if (player.isHerblore) { player.stopHerblore = true; }
		if (player.isCrafting) { player.isCrafting = false; }
		if(!ItemAssistant.playerHasItem(player, useWith, 1, usedWithSlot) || !ItemAssistant.playerHasItem(player, itemUsed, 1, itemUsedSlot) || player.barrowsDoor) {
            return;
        }
		
		Firemaking.fireMake(player,itemUsed, useWith, false, 0, 0);
		WhipColouring.combine(player, itemUsed, useWith);
		UseItem.ItemonItem(player, itemUsed, useWith);
		new HatchetAttaching(player, itemUsed, useWith);
		new FletchingUseItem(player, itemUsed, useWith);
		ItemCombinations.useItems(player, itemUsed, itemUsedSlot, useWith, usedWithSlot);

		
	}

}
