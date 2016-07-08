package ionic.player.content.gambling.impl;

import ionic.player.content.gambling.Gamble;
import ionic.player.content.gambling.GamblingGame;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import utility.Misc;

public class DiceDuel implements GamblingGame {

	@Override
	public void run(Gamble g) {
		g.p1.getPA().sendFrame126("Your Rolls:", 37244);
		g.p1.getPA().sendFrame126("Opponent's Rolls:", 37245);
		g.p2.getPA().sendFrame126("Your Rolls:", 37244);
		g.p2.getPA().sendFrame126("Opponent's Rolls:", 37245);
		for (int i = 0; i < 3; i++) {
			g.p1.getPA().sendFrame126("", 37246 + i);
			g.p1.getPA().sendFrame126("", 37249 + i);
			g.p2.getPA().sendFrame126("", 37246 + i);
			g.p2.getPA().sendFrame126("", 37249 + i);
		}
		g.p1.getPA().showInterface(37240);
		g.p2.getPA().showInterface(37240);
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer e) {
				g.p1Total = 0;
				g.p2Total = 0;
				g.p1Rolls[g.gameStage] = Misc.random(Gamble.ROLL_MAX);
				g.p2Rolls[g.gameStage] = Misc.random(Gamble.ROLL_MAX);
				g.p1.getPA().sendFrame126(""+g.p1Rolls[g.gameStage]+"", 37246 + g.gameStage);
				g.p1.getPA().sendFrame126(""+g.p2Rolls[g.gameStage]+"", 37249 + g.gameStage);
				g.p2.getPA().sendFrame126(""+g.p2Rolls[g.gameStage]+"", 37246 + g.gameStage);
				g.p2.getPA().sendFrame126(""+g.p1Rolls[g.gameStage]+"", 37249 + g.gameStage);
				for (int i = 0; i < 3; i++) {
					g.p1Total += g.p1Rolls[i];
					g.p2Total += g.p2Rolls[i];
				}
				g.p1.getPA().sendFrame126("Your Rolls: ("+g.p1Total+")", 37244);
				g.p1.getPA().sendFrame126("Opponent's Rolls: ("+g.p2Total+")", 37245);
				g.p2.getPA().sendFrame126("Your Rolls: ("+g.p2Total+")", 37244);
				g.p2.getPA().sendFrame126("Opponent's Rolls: ("+g.p1Total+")", 37245);
				g.gameStage++;
				if (g.gameStage == 3) {
					e.stop();
				}
			}
			@Override
			public void stop() {
				CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer e) {
						if (g.p1Total == g.p2Total) {
							g.gameTie();
						} else if (g.p1Total > g.p2Total) {
							g.giveWinnings(g.p1, g.p2, false);
						} else if (g.p2Total > g.p1Total) {
							g.giveWinnings(g.p2, g.p1, false);
						}
						e.stop();
					}
					public void stop() {
						g.reset();
						g.p1.getPA().closeAllWindows();
						g.p2.getPA().closeAllWindows();
					}
				}, 4);
			}
		}, 4);
	}

}
