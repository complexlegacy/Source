package ionic.player.content.skills.agility;

import ionic.player.Player;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import core.Constants;

public class WildernessCourse {
	
	
	public static void clickObjects(Player c, int objectId, int obx, int oby) {
		if (System.currentTimeMillis() - c.lastAgility < 3000) { return; }
		switch(objectId) {
			case 2309:
				if (c.absX == 2998 && c.absY == 3916 && !c.isAgility && c.closeAgility) {
					c.turnPlayerTo(obx, oby);
					enterCourse(c);
					c.lastAgility = System.currentTimeMillis();
				}
			break;
			case 2307:
				if (c.absX == 2998 && c.absY == 3931 && !c.isAgility && c.closeAgility) {
					c.turnPlayerTo(obx, oby);
					leaveCourse(c);
					c.lastAgility = System.currentTimeMillis();
				}
			break;
			case 2288:
				if (c.absX == 3004 && c.absY == 3937 && !c.isAgility && c.closeAgility) {
					c.turnPlayerTo(obx, oby);
					crawlThroughPipe(c, 13);
					c.lastAgility = System.currentTimeMillis();
				}
			break;
			case 2283:
				if (c.absX == 3005 && c.absY == 3953 && !c.isAgility && c.closeAgility) {
					c.turnPlayerTo(c.absX, c.absY + 1);
					c.getPA().objectAnim(obx, oby, 497, 10, 2);
					ropeSwing(c);
					c.lastAgility = System.currentTimeMillis();
				}
			break;
			case 2311:
				if (c.absX == 3002 && c.absY == 3960 && !c.isAgility && c.closeAgility) {
					c.turnPlayerTo(2995, 3960);
					steppingStones(c);
					c.lastAgility = System.currentTimeMillis();
				}
			break;
			case 2297:
				if (c.absX == 3002 && c.absY == 3945 && !c.isAgility && c.closeAgility) {
					c.turnPlayerTo(obx, oby);
					crossLog(c);
					c.lastAgility = System.currentTimeMillis();
				}
			break;
			case 2328:
				
			break;
		}
	}
	
	public static void crossLog(Player c) {
		boolean wasRunning = c.isRunning2;
		c.isRunning2 = false;
		c.playerWalkIndex = 762;
		c.updateRequired = true;
        c.setAppearanceUpdateRequired(true);
		c.getPA().walkTo2(-8, 0);
		c.isAgility = true;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			if (c.absX == 2994 && c.absY == 3945) {
    				e.stop();
    			}
    		}
    		public void stop() {
    			c.startAnimation(65535);
    			c.playerWalkIndex = 0x333;
                c.playerRunIndex = 0x338;
                c.updateRequired = true;
                c.setAppearanceUpdateRequired(true);
    			c.isRunning2 = wasRunning;
    			c.isAgility = false;
    			c.getPA().addSkillXP(36 * Constants.AGILITY_EXPERIENCE, Constants.AGILITY);
    			c.courseStage = 1;
    		}
		}, 1);
	}
	
	public static void steppingStones(Player c) {
		c.turnPlayerTo(2995, 3960);
		c.isAgility = true;
		c.agilityLoop = 2;
		c.startAnimation(741);
		
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			if (c.absX == 2996 && c.absY == 3960) {
    				e.stop();
    				return;
    			}
    			if (c.agilityLoop == 2) {
    				c.getPA().movePlayer(c.absX - 1, c.absY, 0);
    				c.agilityLoop = 0;
    				return;
    			}
    			if (c.agilityLoop == 1) {
    				c.startAnimation(741);
    				c.agilityLoop = 2;
    				return;
    			}
    			c.agilityLoop = 1;
    		}
    		public void stop() {
    			c.isAgility = false;
    			c.agilityLoop = 0;
    			c.getPA().addSkillXP(43 * Constants.AGILITY_EXPERIENCE, Constants.AGILITY);
    		}
		}, 1);
	}
	
	public static void ropeSwing(Player c) {
		c.isAgility = true;
		c.agilityLoop = 0;
		c.startAnimation(751);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			if (c.agilityLoop == 0) {
    				c.getPA().movePlayer(c.absX, c.absY + 3, 0);
    			}
    			if (c.agilityLoop == 1) {
    				c.getPA().movePlayer(c.absX, c.absY + 2, 0);
    				e.stop();
    				return;
    			}
    			c.agilityLoop ++;
    		}
    		public void stop() {
    			c.agilityLoop = 0;
    			c.turnPlayerTo(c.absX, c.absY + 1);
    			c.getPA().addSkillXP(30 * Constants.AGILITY_EXPERIENCE, Constants.AGILITY);
    			c.isAgility = false;
    		}
		}, 1);
	}
	
	public static void crawlThroughPipe(Player c, int f) {
		boolean wasRunning = c.isRunning2;
		c.isRunning2 = false;
		c.playerWalkIndex = 844;
		c.updateRequired = true;
        c.setAppearanceUpdateRequired(true);
		c.getPA().walkTo2(0, f);
		c.isAgility = true;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			if (c.absX == 3004 && c.absY == 3950) {
    				e.stop();
    			}
    		}
    		public void stop() {
    			c.startAnimation(65535);
    			c.playerWalkIndex = 0x333;
                c.playerRunIndex = 0x338;
                c.updateRequired = true;
                c.setAppearanceUpdateRequired(true);
    			c.isRunning2 = wasRunning;
    			c.isAgility = false;
    			c.turnPlayerTo(c.absX, c.absY + 1);
    			c.getPA().addSkillXP(40 * Constants.AGILITY_EXPERIENCE, Constants.AGILITY);
    		}
		}, 1);
	}
	
	public static void leaveCourse(Player c) {
		c.agilityLoop = 0;
		c.isAgility = true;
		c.getPA().walkTo2(0, -15);
		boolean wasRunning = c.isRunning2;
		c.getPA().checkObjectSpawn(2307, 2998, 3931, 2, 0);
		c.getPA().checkObjectSpawn(2308, 2997, 3931, 4, 0);
		c.isRunning2 = false;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			if (c.agilityLoop == 3) {
    				c.getPA().checkObjectSpawn(2307, 2998, 3931, 3, 0);
        			c.getPA().checkObjectSpawn(2308, 2997, 3931, 3, 0);
    			}
    			if (c.absX == 2998 && c.absY == 3916) {
    				e.stop();
    			}
    			if (c.absX == 2998 && c.absY >= 3919 && c.absY <= 3922) {
        			c.getPA().checkObjectSpawn(2309, 2998, 3917, 4, 0);
    			}
    			c.agilityLoop ++;
    		}
    		public void stop() {
    			c.getPA().checkObjectSpawn(2309, 2998, 3917, 3, 0);
    			c.isRunning2 = wasRunning;
    			c.agilityLoop = 0;
    			c.isAgility = false;
    		}
		}, 1);
	}
	
	public static void enterCourse(Player c) {
		c.getPA().checkObjectSpawn(2309, 2998, 3917, 4, 0);
		c.agilityLoop = 0;
		c.getPA().walkTo2(0, 15);
		c.isAgility = true;
		boolean wasRunning = c.isRunning2;
		c.isRunning2 = false;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			if (c.agilityLoop == 3) {
    				c.getPA().checkObjectSpawn(2309, 2998, 3917, 3, 0);
    			}
    			if (c.absX == 2998 && c.absY == 3931) {
    				e.stop();
    			}
    			if (c.absX == 2998 && c.absY == 3927 && c.absY <= 3930) {
    				c.getPA().checkObjectSpawn(2307, 2998, 3931, 2, 0);
        			c.getPA().checkObjectSpawn(2308, 2997, 3931, 4, 0);
    			}
    			c.agilityLoop ++;
    		}
    		public void stop() {
    			c.isRunning2 = wasRunning;
    			c.agilityLoop = 0;
    			c.getPA().checkObjectSpawn(2307, 2998, 3931, 3, 0);
    			c.getPA().checkObjectSpawn(2308, 2997, 3931, 3, 0);
    			c.isAgility = false;
    		}
		}, 1);
	}
	
}
