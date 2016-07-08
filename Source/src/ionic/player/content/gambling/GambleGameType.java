package ionic.player.content.gambling;

import ionic.player.content.gambling.impl.*;

public enum GambleGameType {
	
	DICE_DUEL(15098, "Dice\\n\\n\\n         Duel", ":pvpGambleFlowerOff:", new DiceDuel()),
	FLOWER_POKER(2472, "Flower\\n\\n\\n         Poker", ":pvpGambleFlowerOn:", new FlowerPoker()),
	;
	
	public int i;
	public String s, u;
	public GamblingGame game;
	private GambleGameType(int item, String text, String update, GamblingGame g) {
		this.i = item;
		this.s = text;
		this.u = update;
		this.game = g;
	}

}
