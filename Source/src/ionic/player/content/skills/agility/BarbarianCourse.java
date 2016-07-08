package ionic.player.content.skills.agility;

import ionic.player.Player;
import ionic.player.event.*;
import ionic.player.movement.Movement;
import core.Constants;

public class BarbarianCourse {
	
	public static void clickObjects(Player c, int objectId, int obx, int oby) {
		if (System.currentTimeMillis() - c.lastAgility < 3000) { return; }
		switch(objectId) {
		case 2282:
		if (c.absX == 2551 && c.absY == 3554 && !c.isAgility && c.closeAgility) {
			c.turnPlayerTo(c.absX, c.absY - 1);
			c.getPA().objectAnim(obx, oby, 497, 10, 4);
			ropeSwing(c);
			c.lastAgility = System.currentTimeMillis();
		}
		break;
		case 2294:
			if (c.absX == 2551 && c.absY == 3546 && !c.isAgility && c.closeAgility) {
				Movement.resetWalkingQueue(c);
				c.turnPlayerTo(obx, oby);
				crossLog(c);
				c.lastAgility = System.currentTimeMillis();
			}
		break;
		case 2284:
			if (c.absX == 2539 && c.absY == 3545 && !c.isAgility && c.closeAgility) {
				Movement.resetWalkingQueue(c);
				c.turnPlayerTo(obx, oby);
				climbNet(c);
				c.lastAgility = System.currentTimeMillis();
			}
			break;
		case 2302:
			if (c.absX == 2536 && c.absY == 3547 && !c.isAgility && c.closeAgility) {
				Movement.resetWalkingQueue(c);
				c.turnPlayerTo(obx, oby);
				crossLedge(c);
				c.lastAgility = System.currentTimeMillis();
			}
			break;
		case 1948:
			if (c.absX == 2535 && c.absY == 3553 || c.absX == 2538 && c.absY == 3553 || c.absX == 2541 && c.absY == 3553 && !c.isAgility && c.closeAgility) {
				Movement.resetWalkingQueue(c);
				c.turnPlayerTo(obx, oby);
				climbWalls(c);
				c.lastAgility = System.currentTimeMillis();
			}
			break;
			
		}
	}
	public static void ropeSwing(Player c) {
		c.isAgility = true;
		c.agilityLoop = 0;
		c.startAnimation(751);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			if (c.agilityLoop == 0) {
    				c.getPA().movePlayer(c.absX, c.absY - 3, 0);
    			}
    			if (c.agilityLoop == 1) {
    				c.getPA().movePlayer(c.absX, c.absY - 2, 0);
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
    				c.getPA().movePlayer(c.absX - 1, c.absY, 1);
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
	
	public static void climbWalls(Player c) {
		boolean wasRunning = c.isRunning2;
		c.isRunning2 = false;
		c.playerWalkIndex = 839;
		c.updateRequired = true;
        c.setAppearanceUpdateRequired(true);
		c.getPA().walkTo2(2, 0);
		c.isAgility = true;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			if (c.absX == 2537 && c.absY == 3553 || c.absX == 2540 && c.absY == 3553 || c.absX == 2543 && c.absY == 3553) {
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
	
	public static void crossLedge(Player c) {
		boolean wasRunning = c.isRunning2;
		c.isRunning2 = false;
		c.playerWalkIndex = 756;
		c.updateRequired = true;
        c.setAppearanceUpdateRequired(true);
		c.getPA().walkTo2(-4, 0);
		c.isAgility = true;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			if (c.absX == 2532 && c.absY == 3547) {
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
	
	public static void crossLog(Player c) {
		boolean wasRunning = c.isRunning2;
		c.isRunning2 = false;
		c.playerWalkIndex = 762;
		c.updateRequired = true;
        c.setAppearanceUpdateRequired(true);
		c.getPA().walkTo2(-10, 0);
		c.isAgility = true;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			if (c.absX == 2541 && c.absY == 3546) {
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
