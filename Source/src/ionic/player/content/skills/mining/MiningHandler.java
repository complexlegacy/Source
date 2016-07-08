package ionic.player.content.skills.mining;

import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.achievements.AchievementHandler;
import ionic.player.content.skills.Skill;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import utility.Misc;
import core.Constants;

public class MiningHandler {

	public static Rock[] rocks = new Rock[1000];


	public static boolean clickRock(Player c, int obj, int obX, int obY) {
		RockData r = RockData.forID(obj);
		if (r != null) {
			if (ItemAssistant.freeSlots(c) == 0) {
				c.sendMessage("You don't have enough free inventory slots.");
				return true;
			}
			if (c.skillLevel[Constants.MINING] >= r.levelRequired) {
				MiningAxeData m = findAxe(c);
				if (m == null) {
					c.sendMessage("You don't have a pickaxe that you are able to use.");
					return true;
				}
				Rock rock = findRock(obX, obY);
				if (rock == null) {
					int slot = getFreeRock();
					if (slot == -1) {
						return true;
					}
					rock = new Rock(obj, obX, obY, r.respawnTimer, 1 + Misc.random(r.life), r.replacementId);
					rocks[slot] = rock;
				}
				c.turnPlayerTo(obX, obY);
				mineRock(c, rock, r, m);
			} else {
				c.sendMessage("You need a mining level of "+r.levelRequired+" to mine this rock.");
			}
			return true;
		}
		return false;
	}

	public static void mineRock(Player c, Rock r, RockData d, MiningAxeData m) {
		if (r.life == 0) {
			return;
		}
		c.sendMessage("You swing your pick at the rock.");
		c.startAnimation(m.anim);
		c.rock = r;
		c.miningTicks = 0;
		c.isMining = true;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer e) {
				if (!c.isMining || c.rock == null || c.rock.life == 0) {
					e.stop();
					return;
				}
				if (Skill.isSuccess(c, Constants.MINING, d.levelRequired, m.level)) {
					if (d.name() == "COPPER_ORE") {
						AchievementHandler.add(c, 19, "easy", 1);
					}
					if (d.name() == "TIN_ORE") {
						AchievementHandler.add(c, 20, "easy", 1);
					}
					if (d.name() == "IRON_ORE") {
						AchievementHandler.add(c, 21, "easy", 1);
					}
					ItemAssistant.addItem(c, d.reward, 1);
					c.sendMessage("You get some "+ItemData.data[d.reward].getName()+"");
					c.getPA().addSkillXP(d.experience * Constants.MINING_EXPERIENCE, Constants.MINING);
					if (ItemAssistant.freeSlots(c) == 0) {
						e.stop();
						return;
					}
					c.rock.life --;
					if(c.rock.life == 0) {
						e.stop();
						return;
					}
				}
				if (c.miningTicks == 5) {
					c.miningTicks = 0;
					c.startAnimation(m.anim);
				}
				c.miningTicks++;
			}
			@Override
			public void stop() {
				c.startAnimation(65535);
				c.isMining = false;
				c.miningTicks = 0;
				if (c.rock != null) {
					if (c.rock.life == 0) {
						globalSpawn(d.replacementId, r.x, r.y);
					}
				}
				c.rock = null;
			}
		}, 1);
	}
	
	public static int getFreeRock() {
		for (int i = 0; i < rocks.length; i++) {
			if (rocks[i] == null) {
				return i;
			}
		}
		return -1;
	}

	public static Rock findRock(int x, int y) {
		for (int i = 0; i < rocks.length; i++) {
			if (rocks[i] != null) {
				if (rocks[i].x == x && rocks[i].y == y) {
					return rocks[i];
				}
			}
		}
		return null;
	}


	private static int[] pickAxes = { 1265, 1267, 1269, 1273, 1271, 1275, 15259 };
	public static MiningAxeData findAxe(Player c) {
		for (int i = pickAxes.length - 1; i >= 0; i--) {
			if (c.playerEquipment[Constants.WEAPON_SLOT] == pickAxes[i]) {
				MiningAxeData b = MiningAxeData.forID(pickAxes[i]);
				if (c.skillLevel[Constants.MINING] >= b.level) {
					return b;
				}
			}
			if (ItemAssistant.playerHasItem(c, pickAxes[i])) {
				MiningAxeData b = MiningAxeData.forID(pickAxes[i]);
				if (c.skillLevel[Constants.MINING] >= b.level) {
					return b;
				}
			}
		}
		return null;
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
