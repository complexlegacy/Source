package ionic.player.content.skills.mining;

import ionic.player.Player;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;

public enum Prospecting {
	
	COPPER(new int[] { 31080, 31081, 31082, 2090, 2091, 9708 }), 
	TIN(new int[] { 31077, 31078, 2094, 2095, 9714 }), 
	IRON(new int[] { 31071, 31072, 31073, 2093, 2092, 9717 }), 
	COAL(new int[] { 31068, 31069, 31070, 2096, 2097, 14850, 452, 450 }), 
	GOLD(new int[] { 31065, 31066, 9720, 2098, 2099 }),
	MITHRIL(new int[] { 31086, 31088, 2103, 2102, 14853 }), 
	ADAMANTITE(new int[] { 31083, 31085, 14862, 2104, 2105 }), 
	RUNITE(new int[] { 14859, 4860, 2106, 2107 });

	private int[] objectId;

	private Prospecting(int[] objectId) {
		this.objectId = objectId;
	}
	
	private String oreName() {
		return toString().toLowerCase();
	}
	
	private static Prospecting forID(int obj) {
		for (Prospecting p : Prospecting.values()) {
			for (int i = 0; i < p.objectId.length; i++) {
				if (p.objectId[i] == obj) {
					return p;
				}
			}
		}
		return null;
	}
	
	public static boolean prospect(Player c, int obj) {
		Prospecting p = forID(obj);
		if (p != null) {
			c.sendMessage("You prospect the rock...");
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					c.sendMessage("The rock contains "+p.oreName());
					container.stop();
				}
				@Override
				public void stop() {
				}
			}, 2);
			return true;
		}
		return false;
	}
	
	

}
