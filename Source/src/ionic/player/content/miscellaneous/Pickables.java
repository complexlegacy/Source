package ionic.player.content.miscellaneous;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.achievements.AchievementHandler;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import utility.Misc;

/**
 * @author Keith
 */
public class Pickables {
	
	public static Picked[] picks = new Picked[1000];

	public enum pickableData {
		FLAX(2646, 1779, 26),
		FLAX_2(5583, 1779, 26),
		FLAX_3(5584, 1779, 26),
		FLAX_4(5585, 1779, 26),
		FLAX_5(15508, 1779, 26),
		CABBAGE(1161, 1965, 40),
		POTATOES(312, 1942, 40),
		WHEAT(313, 1947, 20),
		ONION(3366, 1957, 40),
		;
		public int o, a, t;
		private pickableData(int objectId, int toAdd, int hideTime) {
			this.o = objectId;
			this.a = toAdd;
			this.t = hideTime;
		}
		public static pickableData forID(int obj) {
			for (pickableData i : pickableData.values()) {
				if (i.o == obj) {
					return i;
				}
			}
			return null;
		}
	}
	
	public static void pick(Player c, int obj, int x, int y) {
		final int slot = freeSlot();
		if (slot == -1) { return; }
		pickableData j = pickableData.forID(obj);
		if (j == null) { return; }
		if (System.currentTimeMillis() - c.lastPickable < 1500) { return; }
		if (ItemAssistant.freeSlots(c) == 0) {
			//c.getDH().sendStatement("You need atleast 1 free inventory space to do this.");
			return;
		}
		c.lastPickable = System.currentTimeMillis();
		c.startAnimation(827);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    	@Override
    		public void execute(CycleEventContainer e) {
    			globalSpawn(-1, x, y);
    			picks[slot] = new Picked(obj, x, y, (j.t + Misc.random(-10, 10)));
    			e.stop();
			}
    		public void stop() {
    			ItemAssistant.addItem(c, j.a, 1);
    			HandleAchievements(c, j);
    		}
		}, 1);		
	}
	
	
	public static void HandleAchievements(Player c, pickableData d) {
		if (d.a == 1779) {//flax
			AchievementHandler.add(c, 14, "easy", 1);
		}
	}
	
	
	public static int freeSlot() {
		for (int i = 0; i < picks.length; i++) {
			if (picks[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	public static void globalSpawn(int id, int x, int y) {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] != null) {
				Player p = PlayerHandler.players[i];
				if (p != null) {
					p.getPA().checkObjectSpawn(id, x, y, 0, 10);
				}
			}
		}
	}
	
	
	
}
