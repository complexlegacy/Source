package ionic.player.content.skills.firemaking;

import ionic.item.ItemAssistant;
import ionic.object.clip.Region;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.achievements.AchievementHandler;
import ionic.player.dialogue.Dialogue;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import utility.Misc;
import core.Constants;
import core.Server;

/**
 * @author Keith
 */

public class Firemaking {
	
	public static Fire[] fires = new Fire[1000];
	
	public static int getTime(Logs d) {
		int time = d.timer;
		int r = Misc.random(1);
		if (r == 0) {
			time -= Misc.random(16);
		} else {
			time += Misc.random(8);
		}
		return time;
	}
	
	public static int getSlot() {
		int slot = -1;
		for (int i = 0; i < fires.length; i++) {
			if (fires[i] == null) {
				slot = i;
				break;
			}
		}
		return slot;
	}
	
	public static boolean onFire(int x, int y) {
		for (int i = 0; i < fires.length; i++) {
			if (fires[i] != null) {
				if (fires[i].x == x && fires[i].y == y) {
					return true;
				}
			}
		}
		return false;
	}
	

	public static void fireMake(final Client player, final int useWith, final int withUse, boolean ground, int x, int y) {
		player.isChopping = false;
        if (useWith != 590 && withUse != 590) { return; }
        if (!ItemAssistant.playerHasItem(player, 590)) { return; }
        int slot = getSlot();
        
        int theX = 0;
        int theY = 0;
        
        if (ground) {
        	theX = x;
        	theY = y;
        } else {
        	theX = player.getX();
        	theY = player.getY();
        }
        
        if (slot == -1) { return; }
        Logs l = Logs.forID(useWith);
        if (l == null) { l = Logs.forID(withUse); }
        if (l == null) { return; }
        	if (player.skillLevel[Constants.FIREMAKING] < l.levelReq){
        		Dialogue.sendStatement2(player, new String[] {"You need a Firemaking level of at least " + l.levelReq + " to light these logs"});
        		return;
        	}
        
        	
        	final Logs d = l;
        	
        	if (System.currentTimeMillis() - player.lastFire > 1200){
        		if (player.playerIsFiremaking) {
                	return;
                }
        		if (onFire(theX, theY)) {
        			Dialogue.sendStatement2(player, new String[] {"There is already a fire here"});
            		return;
            	}
        		fires[slot] = new Fire(l.fireObj, theX, theY, 10);
            	Fire f = fires[slot];
                final int[] time = new int[3];
                	if (System.currentTimeMillis() - player.lastFire > 3000){
                		player.startAnimation(733);
                		time[0] = 4;
                		time[1] = 3;
                	} else {
                		time[0] = 1;
                		time[1] = 2;
                	}
                player.playerIsFiremaking = true;
                if (!ground) {
                	Server.itemHandler.createGroundItem(player, l.itemId, f.x, f.y, 1, player.getId(), false);
                }
                	CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                		@Override
                		public void execute(CycleEventContainer container) {
                			globalSpawn(d.fireObj, f.x, f.y);
                			f.t = getTime(d);
                			Server.itemHandler.removeGroundItem(player, d.itemId, f.x, f.y, false);
                			player.playerIsFiremaking = false;
                			container.stop();
                		}
                			@Override
                			public void stop() {
                			}
                		}, time[0]);
                		player.sendMessage("You light the logs.");
                		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                			@Override
                			public void execute(CycleEventContainer container) {
                				player.startAnimation(65535);
                				container.stop();
                			}
                			@Override
                			public void stop() {
                			}
                		}, time[1]);
						if (Region.getClipping(player.getX() - 1, player.getY(), player.heightLevel, -1, 0)) {
							player.getPA().goTo(-1, 0);
						} else if (Region.getClipping(player.getX() + 1, player.getY(), player.heightLevel, 1, 0)) {
							player.getPA().goTo(1, 0);
						} else if (Region.getClipping(player.getX(), player.getY() - 1, player.heightLevel, 0, -1)) {
							player.getPA().goTo(0, -1);
						} else if (Region.getClipping(player.getX(), player.getY() + 1, player.heightLevel, 0, 1)) {
							player.getPA().goTo(0, 1);
						}
						
						player.getPA().addSkillXP(((int)(l.exp * Constants.FIREMAKING_EXPERIENCE)), 11);
						player.turnPlayerTo(player.absX + 1, player.absY);
						handleAchievements(player, l);
						if (!ground) {
							ItemAssistant.deleteItem(player, l.itemId, ItemAssistant.getItemSlot(player, l.itemId), 1);
						}
						player.lastFire = System.currentTimeMillis();
                	}
        		}
	
	public static void handleAchievements(Player c, Logs d) {
		AchievementHandler.add(c, 3, "easy", 1);
		
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
			if (id == -1) {
				Player nub = getRandomPlayerForAshes();
				if (nub != null) {
					Server.itemHandler.createGroundItem((Client)nub, 592, x, y, 1, nub.getId(), true);
				}
			}
		}
		
		public static Player getRandomPlayerForAshes() {
			Player k = null;
				for (int i = 0; i < PlayerHandler.players.length; i++) {
					if (PlayerHandler.players[i] != null) {
						k = PlayerHandler.players[i];
						break;
					}
				}
			return k;
		}
}