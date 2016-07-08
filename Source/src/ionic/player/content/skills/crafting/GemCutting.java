package ionic.player.content.skills.crafting;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import core.Constants;

public class GemCutting extends CraftingData {
	
	public static void cutGem(final Client c, final int itemUsed, final int usedWith) {
		if (c.isCrafting == true) {
			return;
		}
		final int itemId = (itemUsed == 1755 ? usedWith : itemUsed);
		for (final cutGemData g : cutGemData.values()) {
			if (itemId == g.getUncut()) {
				if (c.skillLevel[12] < g.getLevel()) {
					c.sendMessage("You need a crafting level of "+ g.getLevel() +" to cut this gem.");
					return;
				}
				if (!ItemAssistant.playerHasItem(c, itemId)) {
					return;
				}
				c.startAnimation(g.getAnimation());
				c.isCrafting = true;
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (c.isCrafting == true) {
							if (ItemAssistant.playerHasItem(c, itemId)) {
								ItemAssistant.deleteItem(c, itemId, 1);
								ItemAssistant.addItem(c, g.getCut(), 1);	
								c.getPA().addSkillXP((int) g.getXP() * Constants.CRAFTING_EXPERIENCE, 12);
								c.sendMessage("You cut the "+ ItemAssistant.getItemName(itemId).toLowerCase() +".");
								c.startAnimation(g.getAnimation());
							} else {
								container.stop();
							}
						} else {
							container.stop();
						}
					}
					@Override
					public void stop() {
						c.isCrafting = false;
					}
				}, 4);
			}
		}
	}
}