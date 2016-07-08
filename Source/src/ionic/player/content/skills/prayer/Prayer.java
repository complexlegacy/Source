package ionic.player.content.skills.prayer;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import ionic.player.achievements.AchievementHandler;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import core.Constants;

public class Prayer {
	
	public static boolean buryBones(Player c, int item) {
		Bones b = Bones.getID(item);
		if (b == null || !c.canBury) {
			return false;
		}
		c.canBury = false;
		ItemAssistant.deleteItem(c, item, 1);
		c.getPA().addSkillXP(b.getExpGained() * Constants.PRAYER_EXPERIENCE, Constants.PRAYER);
		c.startAnimation(827);
		c.sendMessage("You dig a hole in the ground.");
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer e) {
				e.stop();
			}
			@Override
			public void stop() {
				c.canBury = true;
				c.sendMessage("You bury the bones.");
			}
		}, 2);
		return true;
	}
	
	public static void handleAchievements(Player c, Bones d) {
		if (d == Bones.BONES) {
			AchievementHandler.add(c, 13, "easy", 1);
		}
		
	}
	
	
	
	
	
}
