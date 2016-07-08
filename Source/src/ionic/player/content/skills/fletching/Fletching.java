package ionic.player.content.skills.fletching;

import ionic.item.Item;
import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Player;
import ionic.player.achievements.AchievementHandler;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import core.Constants;

/**
 * @author Keith
 */
public class Fletching {
	
	public static int timer = 3;
	
	public Fletching(Player c, FletchingData d, int amount) {
		if (d.anim > 0) { c.startAnimation(d.anim); }
		c.isFletching = true;
		c.fletchAmount = amount;
		if ((d.identifier >= 19 && d.identifier <= 26) || (d.identifier >= 39 && d.identifier <= 52)) { 
			timer = 1; 
		} else {
			timer = 3;
		}
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			if (!c.isFletching) { e.stop(); return; }
    			if (!ItemAssistant.playerHasItem(c, d.used) || !ItemAssistant.playerHasItem(c, d.used2)) { e.stop(); return; }
    			int makeAmt = d.makeAmt;
    			if (makeAmt > ItemAssistant.getItemAmount(c, d.used)) { makeAmt = ItemAssistant.getItemAmount(c, d.used); }
    			if (c.fletchAmount == 0) { e.stop(); return; }
    			c.fletchAmount --;
    			if (d.anim > 0) { c.startAnimation(d.anim); }
    			if (d.identifier == 1 && d.slot == 3) {
    				ItemAssistant.deleteItemForBank(c, d.used, 1);
    				makeAmt = 15;
    			} else {
    				ItemAssistant.deleteItemForBank(c, d.used, makeAmt);
    			}
    			if (d.used2 != 946) { ItemAssistant.deleteItemForBank(c, d.used2, !ItemData.data[d.used2].stackable ? 1 : makeAmt); }
    			ItemAssistant.addItem(c, d.make, makeAmt);
    			handleAchievements(c, d);
    			c.getPA().addSkillXP((d.xp * Constants.FLETCHING_EXPERIENCE), Constants.FLETCHING);
    		}
    		public void stop() {
    			c.isFletching = false;
    			c.startAnimation(65535);
    		}
		}, timer);
	}

	
	
	public static void handleAchievements(Player c, FletchingData d) {
		if (d == FletchingData.ARROW_SHAFTS) { AchievementHandler.add(c, 5, "easy", 15); }
		if (d == FletchingData.NORMAL_SHORTBOW) { AchievementHandler.add(c, 6, "easy", 1); }
	}
	
	
}
