package ionic.player.content.skills.summoning;

import core.Constants;
import ionic.item.ItemAssistant;
import ionic.player.Player;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;

public class PouchAndScrollCreation {
	
	/**
	 * Handles when you click on a button to create that pouch
	 * @param c - the player who clicked
	 * @param slot - the slot that was clicked
	 * @param amount - the amount selected
	 */
	public static void createPouch(Player c, int slot, int amount) {
		SummoningData.pouchData p = SummoningData.pouchFromSlot(slot);
		if (p == null) return;
		if (!p.canCreate(c)) {
			p.sendRequirements(c);
		} else {
			int make = 1;
			for (int i = 1; i <= amount; i++) {
				if (ItemAssistant.playerHasItem(c, p.charm, i)
						&& ItemAssistant.playerHasItem(c, p.secondIngredient, i)
							&& ItemAssistant.playerHasItem(c, SummoningData.SHARDS, p.shards * i)
								&& ItemAssistant.playerHasItem(c, SummoningData.POUCH, i)) {
					make = i;
				}
			}
			c.getPA().closeAllWindows();
			c.startAnimation(725);
            c.gfx0(1207);
            ItemAssistant.deleteItemForBank(c, SummoningData.SHARDS, p.shards * make);
			ItemAssistant.deleteItemForBank(c, p.charm, make);
			ItemAssistant.deleteItemForBank(c, p.secondIngredient, make);
			ItemAssistant.deleteItemForBank(c, SummoningData.POUCH, make);
			final int created = make;
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					container.stop();
				}
				@Override
				public void stop() {
					ItemAssistant.addItem(c, p.pouch, created);
					c.getPA().addSkillXP((p.infuseXp * created) * 10, Constants.SUMMONING);
				}
			}, 3);
		}
	}

	
	/**
	 * Handles when you click on a button to create that scroll
	 * @param c - the player who clicked
	 * @param slot - the slot that was clicked
	 * @param amount - the amount selected
	 */
	public static void createScroll(Player c, int slot, int amount) {
		SummoningData.scrollData s = SummoningData.getScrollBySlot(slot);
		if (s == null) return;
		if (!ItemAssistant.playerHasItem(c, s.pouch.pouch)) {
			s.sendRequirements(c);
		} else {
			int make = 1;
			for (int i = 1; i <= amount; i++) {
				if (ItemAssistant.playerHasItem(c, s.pouch.pouch, i)) {
					make = i;
				}
			}
			ItemAssistant.deleteItemForBank(c, s.pouch.pouch, make);
			final int created = make;
			c.getPA().closeAllWindows();
			c.startAnimation(725);
            c.gfx0(1207);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					container.stop();
				}
				@Override
				public void stop() {
					ItemAssistant.addItem(c, s.scroll, created * 10);
					c.getPA().addSkillXP((s.pouch.summonXp * created) * 10, Constants.SUMMONING);
				}
			}, 3);
		}
	}
	
}
