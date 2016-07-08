package core;

import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.content.minigames.CastleWars;
import ionic.player.content.minigames.FightPits;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;

public class Processors {

	public void start() {
		statRestoring();
		runMinigames();
		Server.summoning.timer();
	}
	
	public static void runMinigames() {
		CycleEventHandler.getSingleton().addEvent(Server.pestControl, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				Server.pestControl.process();
				FightPits.process();
				CastleWars.process();
			}
			@Override
			public void stop() {
			}
		}, 5);
	}

	public void statRestoring() {
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				for (int i = 0; i < PlayerHandler.players.length; i++) {
					if (PlayerHandler.players[i] != null) {
						Player c = PlayerHandler.players[i];
						boolean justOff = false;
						boolean ovlMsg = false;
						if (c.overloaded && System.currentTimeMillis() - c.overloadTime > 300000) {
							c.overloaded = false;
							justOff = true;
						}
						for (int level = 0; level < c.skillLevel.length; level++) {
							if (c.skillLevel[level] < (level == 3 ? c.maximumHitPoints() : c.getLevelForXP(c.playerXP[level]))) {
								if (level != 5 && level != 3 && level != 24) {
									c.skillLevel[level] += 1;
									c.getPA().setSkillLevel(level, c.skillLevel[level], c.playerXP[level]);
									c.getPA().refreshSkill(level);
								}
							} else if (c.skillLevel[level] > (level == 3 ? c.maximumHitPoints() : c.getLevelForXP(c.playerXP[level]))) {
								if (level != 3) {
									if (!c.overloaded && !justOff) {
										c.skillLevel[level] -= 1;
										c.getPA().setSkillLevel(level, c.skillLevel[level], c.playerXP[level]);
										c.getPA().refreshSkill(level);
									} else if (!c.overloaded && justOff) {
										if (!ovlMsg) {
											ovlMsg = true;
											c.sendMessage("The effects from your overload potion have worn off.");
										}
										c.skillLevel[level] = c.getLevelForXP(c.playerXP[level]);
										c.getPA().setSkillLevel(level, c.skillLevel[level], c.playerXP[level]);
										c.getPA().refreshSkill(level);
									}
								}
							}
							if (c.loggingInFinished && level == 3) {
								if (c.skillLevel[3] < c.maximumHitPoints()) {
									c.addToHitPoints(1);
								} else if (c.skillLevel[3] > c.maximumHitPoints()) {
									c.subtractFromHitPoints(1);
								}
							}
						}
					}
				}
			}
			@Override
			public void stop() {} 
		}, 100);
	}

}
