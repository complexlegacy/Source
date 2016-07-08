package ionic.player.content.skills.runecrafting;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import ionic.player.dialogue.Dialogue;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import core.Constants;

public class Runecrafting {
	
	private static final int PURE_ESSENCE = 7936;
	private static final int RUNE_ESSENCE = 1436;
	
	public static void handleRunecrafting(Player c, RunecraftingData d) {
		if (c.skillLevel[Constants.RUNECRAFTING] < d.levelReq) {
			Dialogue.sendStatement2(c, new String[] {"You need a runecrafting level of "+d.levelReq+"\\nto make these runes."});
			return;
		}
		int amount = 0;
		amount += ItemAssistant.getItemAmount(c, PURE_ESSENCE);
		amount += ItemAssistant.getItemAmount(c, RUNE_ESSENCE);
		if (amount <= 0) {
			Dialogue.sendStatement2(c, new String[] {"You need some rune essence or pure essence to craft runes"});
			return;
		}
		int giveAmount = 0;
		if (d.multiplier == null) {
			giveAmount = amount;
		} else {
			for (int i = 0; i < d.multiplier.length; i++) {
				if (c.skillLevel[Constants.RUNECRAFTING] >= d.multiplier[i]) {
					giveAmount = (amount * (i + 1));
				}
			}
		}
		final int give = giveAmount;
		final int xp = (int) ((amount * d.xp) * Constants.RUNECRAFTING_EXPERIENCE);
		deleteEssence(c);
		c.startAnimation(791);
		c.gfx0(186);
		c.sendMessage(give+"");
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			e.stop();
    		}
    		public void stop() {
    			giveRunes(c, d, give);
    			c.getPA().addSkillXP(xp, Constants.RUNECRAFTING);
    		}
		}, 1);
	}
	
	public static void giveRunes(Player c, RunecraftingData d, int amt) {
		ItemAssistant.addItem(c, d.runeId, amt);
	}
	
	public static void deleteEssence(Player c) {
		ItemAssistant.deleteItem(c, RUNE_ESSENCE, 28);
		ItemAssistant.deleteItem(c, PURE_ESSENCE, 28);
	}
	
	
	public static boolean clickObject(Player c, int objectId) {
		RunecraftingData d = null;
		d = RunecraftingData.forID(objectId);
		if (d != null) {
			handleRunecrafting(c, d);
			return true;
		}
		return false;
	}
	
	
	

}
