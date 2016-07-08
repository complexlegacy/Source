package ionic.player.content.skills.woodcutting;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.achievements.AchievementHandler;
import ionic.player.dialogue.Dialogue;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import utility.Misc;
import core.Constants;
/**
 * @author Keith
 */
/**
 * Handles mostly everything for the woodcutting skill
 */
public class Woodcutting {
	
	public static Tree[] trees = new Tree[1000];
	
	public static void chopTree(Player c, int obj, int x, int y) {
		TreeData z = TreeData.forId(obj);
		if (z != null) {
			AxeData a = findAxe(c);
			if (a == null) { 
			Dialogue.sendStatement2(c, new String[] {"You don't have an axe with the required woodcutting level to use."}); 
			return; 
			}
			if (c.skillLevel[Constants.WOODCUTTING] < z.reqLvl) { 
				Dialogue.sendStatement2(c, new String[] {"You need a woodcutting level of "+z.reqLvl+" to chop this tree"});
				return;
			}
			if (ItemAssistant.freeSlots(c) <= 0) { 
				Dialogue.sendStatement2(c, new String[] {"You need atleast one free inventory spaces to attempt cutting this tree"});
				return; 
			}
			c.turnPlayerTo(x, y);
			
			c.tree = findTree(x, y);
			if (c.tree == null) { newTree(obj, x, y, z); c.tree = findTree(x, y); }
			c.startAnimation(a.animation);
			c.isChopping = true;
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer e) {
					if (c.isChopping == false) {
						e.stop();
						return;
					}
					if (c.tree.l <= 0) {
						e.stop();
						return;
					}
					c.startAnimation(a.animation);
					new WoodcuttingRandom(c, a);
				}
				public void stop() {
					c.startAnimation(65535);
					c.isChopping = false;
				}
			}, 3);
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer e) {
					if (c.isChopping == false) {
						e.stop();
						return;
					}
					if (ItemAssistant.freeSlots(c) <= 0) {
						Dialogue.sendStatement2(c, new String[] {"Your inventory is full."});
						e.stop(); 
						return;
					}
						c.tree.l -= 1;
						addLogs(c, z);
						
					if (c.tree.l <= 0) {
						c.tree.t = z.respawnTime;
						globalSpawn(z.stumpId, c.tree.x, c.tree.y);
						e.stop();
						return;
					}
				}
				public void stop() {
					c.startAnimation(65535);
					c.isChopping = false;
				}
			}, (getAxeStrength(c, a) + (10 - (int)Math.floor(c.skillLevel[8] / 10))));
		}
	}
	
	
	
	
	public static int getAxeStrength(Player c, AxeData a) {
		if (a != null) {
			return a.bonus;
		}
		return 1;
	}
	
	
	
	public static void addLogs(Player c, TreeData d) {
		ItemAssistant.addItem(c, d.logId, 1);
		c.sendMessage("You manage to get some logs from the tree");
		c.getPA().addSkillXP((int)(d.xp * Constants.WOODCUTTING_EXPERIENCE), Constants.WOODCUTTING);
		handleAchievements(c, d);
	}
	
	
	
	public static void handleAchievements(Player c, TreeData d) {
		if (d == TreeData.NORMAL) { AchievementHandler.add(c, 1, "easy", 1); }
		if (d == TreeData.OAK) { AchievementHandler.add(c, 2, "easy", 1); }
		
	}
	
	
	private static int[] axes = { 1351, 1349, 1353, 1361, 1355, 1357, 1359, 6739 };
	public static AxeData findAxe(Player c) {
		for (int i = axes.length - 1; i >= 0; i--) {
			if (c.playerEquipment[Constants.WEAPON_SLOT] == axes[i]) {
				AxeData b = AxeData.forId(axes[i]);
				if (c.skillLevel[Constants.WOODCUTTING] >= b.levelRequired) {
					return b;
				}
			}
			if (ItemAssistant.playerHasItem(c, axes[i])) {
				AxeData b = AxeData.forId(axes[i]);
				if (c.skillLevel[Constants.WOODCUTTING] >= b.levelRequired) {
					return b;
				}
			}
		}
		return null;
	}
	
	
	public static void newTree(int obj, int x, int y, TreeData f) {
		int slot = -1;
		for (int i = 0; i < trees.length; i++) {
			if (trees[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot != -1) {
			trees[slot] = new Tree(obj, x, y, 1 + Misc.random(f.maxLife), f.stumpId);
		}
	}
	
	
	public static Tree findTree(int x, int y) {
		for (int i = 0; i < trees.length; i++) {
			if (trees[i] != null) {
				if (trees[i].x == x && trees[i].y == y) {
					return trees[i];
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
