package ionic.player.content.skills.agility;

import ionic.player.Player;
import ionic.player.event.*;
import core.Constants;

public class GnomeCourse {
	
	public static void clickObjects(Player c, int objectId, int obx, int oby) {
		if (System.currentTimeMillis() - c.lastAgility < 3000) { return; }
		switch(objectId) {
			case 2295:
				if (c.absX == 2474 && c.absY == 3436 && !c.isAgility && c.closeAgility) {
					c.turnPlayerTo(obx, oby);
					crossLog(c);
					c.lastAgility = System.currentTimeMillis();
				}
			break;
			case 2285:
				if (c.absX >= 2471 && c.absX <= 2476 && c.absY == 3426 && !c.isAgility && c.closeAgility) {
					c.turnPlayerTo(c.absX, oby);
					climbNet(c);
					c.lastAgility = System.currentTimeMillis();
				}
			break;
			case 2313:
				if (c.absX == 2473 && c.absY == 3423 && !c.isAgility && c.closeAgility) {
					c.turnPlayerTo(obx, oby);
					climbBranch(c, false);
					c.lastAgility = System.currentTimeMillis();
				}
			break;
			case 2312:
				if (c.absX == 2477 && c.absY == 3420 && !c.isAgility && c.closeAgility) {
					c.turnPlayerTo(obx, oby);
					crossRope(c, 6, 2483, 3420);
					c.lastAgility = System.currentTimeMillis();
				}
			break;
			case 2314:
			case 2315:
				if (!c.isAgility && c.closeAgility) {
					c.turnPlayerTo(obx, oby);
					climbBranch(c, true);
					c.lastAgility = System.currentTimeMillis();
				}
			break;
			case 2286:
				if (!c.isAgility && c.closeAgility && c.absY == 3425) {
					c.turnPlayerTo(c.absX, c.absY + 2);
					overNet(c, false);
					c.lastAgility = System.currentTimeMillis();
				}/* else if (!c.isAgility && c.closeAgility && c.absY == 3427) {
					Movement.resetWalkingQueue(c);
					c.turnPlayerTo(c.absX, c.absY - 2);
					overNet(c, true);
					c.lastAgility = System.currentTimeMillis();
				}*/
			break;
			case 154:
				if (c.absX == 2484 && c.absY == 3430 && !c.isAgility && c.closeAgility) {
					c.turnPlayerTo(obx, oby);
					crawlThroughPipe(c, 7, 2484, 3437, true);
					c.lastAgility = System.currentTimeMillis();
				}/* else if (c.absX == 2484 && c.absY == 3437 && !c.isAgility && c.closeAgility) {
					Movement.resetWalkingQueue(c);
					c.turnPlayerTo(obx, oby);
					crawlThroughPipe(c, -7, 2484, 3430, false);
					c.lastAgility = System.currentTimeMillis();
				}*/
			break;
			case 4058:
				if (c.absX == 2487 && c.absY == 3430 && !c.isAgility && c.closeAgility) {
					c.turnPlayerTo(obx, oby);
					crawlThroughPipe(c, 7, 2487, 3437, true);
					c.lastAgility = System.currentTimeMillis();
				}/* else if (c.absX == 2487 && c.absY == 3437 && !c.isAgility && c.closeAgility) {
					Movement.resetWalkingQueue(c);
					c.turnPlayerTo(obx, oby);
					crawlThroughPipe(c, -7, 2487, 3430, false);
					c.lastAgility = System.currentTimeMillis();
				}*/
			break;
		}
	}
	
	public static void crawlThroughPipe(Player c, int f, int j, int k, boolean forward) {
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
    			if (c.absX == j && c.absY == k) {
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
    			int xp = 10;
    			c.turnPlayerTo(c.absX, c.absY + 1);
    			if (c.courseStage == 6) {
    				c.courseStage = 0;
    				xp += 50;
    			}
    			c.getPA().addSkillXP(xp * Constants.AGILITY_EXPERIENCE, Constants.AGILITY);
    		}
		}, 1);
	}
	
	public static void overNet(Player c, boolean b) {
		c.isAgility = true;
		c.startAnimation(828);
		c.agilityLoop = 0;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			if (c.agilityLoop == 1) {
    				e.stop();
    				return;
    			}
    			if (c.agilityLoop == 0) {
    				c.turnPlayerTo(c.absX, b ? (c.absY + 2) : (c.absY - 2));
    				c.getPA().movePlayer(c.absX, b ? (c.absY - 2) : (c.absY + 2), 0);
    			}
    			c.agilityLoop ++;
    		}
    		public void stop() {
    			c.isAgility = false;
    			c.getPA().addSkillXP(10 * Constants.AGILITY_EXPERIENCE, Constants.AGILITY);
    			c.agilityLoop = 0;
    			if (c.courseStage == 5) {
    				c.courseStage = 6;
    			}
    		}
		}, 1);
	}
	
	public static void crossRope(Player c, int f, int j, int k) {
		boolean wasRunning = c.isRunning2;
		c.isRunning2 = false;
		c.playerWalkIndex = 762;
		c.updateRequired = true;
        c.setAppearanceUpdateRequired(true);
		c.getPA().walkTo2(f, 0);
		c.isAgility = true;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			if (c.absX == j && c.absY == k) {
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
    			c.getPA().addSkillXP(10 * Constants.AGILITY_EXPERIENCE, Constants.AGILITY);
    			c.turnPlayerTo(c.absX + 1, c.absY);
    			if (c.courseStage == 3) {
    				c.courseStage = 4;
    			}
    		}
		}, 1);
	}
	
	public static void climbBranch(Player c, boolean down) {
		c.isAgility = true;
		c.startAnimation(828);
		c.agilityLoop = 0;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			if (c.agilityLoop == 1) {
    				e.stop();
    				return;
    			}
    			if (c.agilityLoop == 0) {
    				c.getPA().movePlayer(c.absX, down ? c.absY : (c.absY - 3), down ? 0 : 2);
    			}
    			c.agilityLoop ++;
    		}
    		public void stop() {
    			c.isAgility = false;
    			c.getPA().addSkillXP(8 * Constants.AGILITY_EXPERIENCE, Constants.AGILITY);
    			c.agilityLoop = 0;
    			if (c.courseStage == 2 && !down) {
    				c.courseStage = 3;
    			} else if (c.courseStage == 4 && down) {
    				c.courseStage = 5;
    			}
    		}
		}, 1);
	}
	
	
	public static void climbNet(Player c) {
		c.isAgility = true;
		c.startAnimation(828);
		c.agilityLoop = 0;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			if (c.agilityLoop == 1) {
    				e.stop();
    				return;
    			}
    			if (c.agilityLoop == 0) {
    				c.getPA().movePlayer(c.absX, c.absY - 2, 1);
    			}
    			c.agilityLoop ++;
    		}
    		public void stop() {
    			c.isAgility = false;
    			c.getPA().addSkillXP(10 * Constants.AGILITY_EXPERIENCE, Constants.AGILITY);
    			c.agilityLoop = 0;
    			if (c.courseStage == 1) {
    				c.courseStage = 2;
    			}
    		}
		}, 1);
		
	}
	
	
	public static void crossLog(Player c) {
		boolean wasRunning = c.isRunning2;
		c.isRunning2 = false;
		c.playerWalkIndex = 762;
		c.updateRequired = true;
        c.setAppearanceUpdateRequired(true);
		c.getPA().walkTo2(0, -8);
		c.isAgility = true;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			if (c.absX == 2474 && c.absY == 3428) {
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
    			c.getPA().addSkillXP(10 * Constants.AGILITY_EXPERIENCE, Constants.AGILITY);
    			c.courseStage = 1;
    		}
		}, 1);
	}

}
