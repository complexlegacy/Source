package ionic.player.content.skills.thieving;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import utility.Misc;
import core.Constants;

public class StallThieving {
	
	public enum stalls {
	VEGETABLE_STALL(4706, 6, 634, 2, 10, 0, 5, new int[] { 1965, 1 }), 
	BAKER_STALL(34834, 7, 634, 5, 16, 3, 5, new int[] { 2309, 1 }, new int[] { 1891, 1 }, new int[] { 1895, 1 }), 
	TEA_STALL(635, 8, 634, 5, 16, 0, 5, new int[] { 712, 1 }), 
	SILK_STALL(2560, 10,634, 20, 24, 2, 6, new int[] { 950, 1 }), 
	WINE_STALL(14011, 11, 634, 22, 27, 0, 7, new int[] { 1935, 1 }), 
	SEED_STALL(7053, 12, 634, 27, 10, 0, 7, new int[] { 5318, 1 }), 
	FUR_STALL(2563, 13, 634, 35, 36, 0, 8, new int[] { 6814, 1 }, new int[] { 958, 1 }), 
	FISH_STALL(4705, 13, 634, 42, 42, 0, 8, new int[] { 359, 1 }), 
	SILVER_STALL(2565, 14, 634, 50, 54, 2, 9, new int[] { 442, 1 }, new int[] { 2355, 1 }), 
	SPICE_STALL(2564,15, 634, 65, 81, 0, 10, new int[] { 2007, 1 }, new int[] { 946, 1 }, new int[] { 1550, 1 }), 
	GEM_STALL(2562, 17, 634, 75, 160, 3, 11, new int[] { 1625, 1 }, new int[] { 1617, 1 },
			new int[] { 1619, 1 }, new int[] { 1621, 1 },
			new int[] { 1623, 1 }, new int[] { 1627, 1 }, new int[] { 1629, 1 });

	public int objectId, levelReq, face, decayChance;
	public int[][] stalls;
	public int xp;
	public final int replacement, respawnTimer;

	private stalls(final int objectId, final int respawnTimer, final int replacement, final int levelReq, final int xp, final int face, int decayChance, final int[]... stalls) {
		this.objectId = objectId;
		this.levelReq = levelReq;
		this.xp = xp;
		this.face = face;
		this.stalls = stalls;
		this.replacement = replacement;
		this.respawnTimer = respawnTimer;
		this.decayChance = decayChance;
	}
	}
	
	private static stalls forID(int obj) {
		for (stalls s : stalls.values()) {
			if (s.objectId == obj) {
				return s;
			}
		}
		return null;
	}
	
	public void thieveStall(Player c, int obj, int x, int y) {
		stalls s = forID(obj);
		if (s == null) {
			return;
		}
		if (c.skillLevel[Constants.THIEVING] < s.levelReq) {
			c.sendMessage("You need a thieving level of at least "+s.levelReq+" to steal from this stall.");
			return;
		}
		if (System.currentTimeMillis() - c.lastThieve < 1000) {
			return;
		}
		c.lastThieve = System.currentTimeMillis();
		c.startAnimation(832);
		int stall = Misc.random(s.stalls.length - 1);
		int item = s.stalls[stall][0];
		int amount = s.stalls[stall][1];
		int coins = Misc.random(4);
		if (coins == 0) {
			amount = s.levelReq * (1000 + Misc.random(1000));
			ItemAssistant.addItem(c, 995, amount);
		} else {
			ItemAssistant.addItem(c, item, amount);
		}
		c.getPA().addSkillXP(s.xp * Constants.THIEVING_EXPERIENCE, Constants.THIEVING);
		if (Misc.random(s.decayChance) == 0) {
			globalReplace(s, x, y, false);
			CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer e) {
					e.stop();
				}
				@Override
				public void stop() {
					globalReplace(s, x, y, true);
				}
			}, s.respawnTimer);
		}
	}
	
	private static void globalReplace(stalls s, int x, int y, boolean regen) {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			Player d = PlayerHandler.players[i];
			if (d != null) {
				if (!regen)
					d.getPA().checkObjectSpawn(s.replacement, x, y, s.face, 10);
				else if (regen)
					d.getPA().checkObjectSpawn(s.objectId, x, y, s.face, 10);
			}
		}
	}

}
