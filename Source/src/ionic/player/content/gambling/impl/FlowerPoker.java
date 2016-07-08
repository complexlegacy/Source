package ionic.player.content.gambling.impl;

import ionic.player.content.gambling.Gamble;
import ionic.player.content.gambling.GamblingGame;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import utility.Misc;

public class FlowerPoker implements GamblingGame {

	@Override
	public void run(Gamble g) {
		g.p1.getPA().sendFrame126("Your Plants:", 37236);
		g.p1.getPA().sendFrame126("Opponent's Plants:", 37237);
		g.p2.getPA().sendFrame126("Your Plants:", 37236);
		g.p2.getPA().sendFrame126("Opponent's Plants:", 37237);
		for (int i = 0; i < 5; i++) {
			g.p1.getPA().sendFrame34a(37238, -1, i, 1);
			g.p1.getPA().sendFrame34a(37239, -1, i, 1);
			g.p2.getPA().sendFrame34a(37238, -1, i, 1);
			g.p2.getPA().sendFrame34a(37239, -1, i, 1);
		}
		g.p1.getPA().showInterface(37230);
		g.p2.getPA().showInterface(37230);
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer e) {
				g.p1Flowers[g.gameStage] = Gamble.FLOWERS[Misc.random(Gamble.FLOWERS.length - 1)];
				g.p2Flowers[g.gameStage] = Gamble.FLOWERS[Misc.random(Gamble.FLOWERS.length - 1)];
				g.p1Hand = g.getHand(g.p1Flowers);
				g.p2Hand = g.getHand(g.p2Flowers);
				g.p1.getPA().sendFrame126("Your Plants: ("+g.p1Hand.name+")", 37236);
				g.p1.getPA().sendFrame126("Opponent's Plants: ("+g.p2Hand.name+")", 37237);
				g.p2.getPA().sendFrame126("Your Plants: ("+g.p2Hand.name+")", 37236);
				g.p2.getPA().sendFrame126("Opponent's Plants: ("+g.p1Hand.name+")", 37237);
				g.p1.getPA().sendFrame34a(37238, g.p1Flowers[g.gameStage].id, g.gameStage, 1);
				g.p1.getPA().sendFrame34a(37239, g.p2Flowers[g.gameStage].id, g.gameStage, 1);
				g.p2.getPA().sendFrame34a(37238, g.p2Flowers[g.gameStage].id, g.gameStage, 1);
				g.p2.getPA().sendFrame34a(37239, g.p1Flowers[g.gameStage].id, g.gameStage, 1);
				g.gameStage++;
				if (g.gameStage == 5) {
					e.stop();
				}
			}
			@Override
			public void stop() {
				CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer e) {
						if (g.p1Hand.points == g.p2Hand.points) {
							g.gameTie();
						} else if (g.p1Hand.points > g.p2Hand.points) {
							g.giveWinnings(g.p1, g.p2, false);
						} else if (g.p2Hand.points > g.p1Hand.points) {
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
