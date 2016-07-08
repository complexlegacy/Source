package ionic.player.content.skills.herblore;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import ionic.player.dialogue.Dialogue;
import core.Constants;

public class Herblore {
	
	public static boolean makeUnfinished(Player c, int used, int with) {
		
		return false;
	}
	
	public static boolean cleanHerb(Player c, int used) {
			if (ItemAssistant.playerHasItem(c, used)) {
				HerbCleaning j = null;
				j = HerbCleaning.forID(used);
				if (j != null) {
					cleanHerb(c, j);
					return true;
				}
			}
		return false;
	}
	
	public static void cleanHerb(Player c, HerbCleaning d) {
		if (c.skillLevel[Constants.HERBLORE] >= d.lvlReq) {
			ItemAssistant.deleteItem(c, d.dirty, 1);
			ItemAssistant.addItem(c, d.clean, 1);
			c.getPA().addSkillXP(d.xp, Constants.HERBLORE);
		} else {
			Dialogue.sendStatement2(c, new String[]{"You need a herblore level of "+d.lvlReq+" to clean this herb"});
		}
	}

}
