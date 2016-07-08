package ionic.player.content.gambling;

import ionic.item.ItemAssistant;
import ionic.player.Player;

public class Gamble {

	public GambleGameType type;
	public Player p1;
	public Player p2;
	public boolean p1Accepted = false;
	public boolean p2Accepted = false;
	private int[] pot = new int[24];
	private int[] potA = new int[24];
	public int gameStage = 0;
	public int[] p1Rolls = new int[3];
	public int[] p2Rolls = new int[3];
	public int p1Total = 0;
	public int p2Total = 0;
	public Flowers[] p1Flowers = new Flowers[5];
	public Flowers[] p2Flowers = new Flowers[5];
	public PokerHand p1Hand;
	public PokerHand p2Hand;
	public static final int ROLL_MAX = 100;
	public static final Flowers[] FLOWERS = {Flowers.RED, Flowers.BLUE, Flowers.YELLOW, Flowers.PURPLE, 
		Flowers.ORANGE, Flowers.RAINBOW, Flowers.PASTEL};

	public Gamble(Player player1, Player player2) {
		this.p1 = player1;
		this.p2 = player2;
		this.type = GambleGameType.DICE_DUEL;
	}

	/**
	 * Opens the gambling interface for both players.
	 */
	public void open() {
		empty(p1);
		empty(p2);
		updateType();
		updateStatus();
		p1.getPA().showInterface(37200);
		p2.getPA().showInterface(37200);
	}

	/**
	 * Closes the gambling interface for both players.
	 */
	public void decline(Player decliner) {
		if (decliner == p1 && gameStage > 0) {
			p2.sendMessage("You have won by default.");
			giveWinnings(p2, p1, true);
			reset();
			p2.getPA().closeAllWindows();
			return;
		} else if (decliner == p2 && gameStage > 0) {
			p1.sendMessage("You have won by default.");
			giveWinnings(p1, p2, true);
			reset();
			p1.getPA().closeAllWindows();
			return;
		}
		for (int i = 0; i < 12; i++) {
			if (p1.gambleItems[i] > 0) {
				ItemAssistant.addItem(p1, p1.gambleItems[i], p1.gambleAmounts[i]);
			}
			if (p2.gambleItems[i] > 0) {
				ItemAssistant.addItem(p2, p2.gambleItems[i], p2.gambleAmounts[i]);
			}
		}
		reset();
		p1.getPA().closeAllWindows();
		p2.getPA().closeAllWindows();
	}



	/**
	 * Updates the gambling type on the interface for both players.
	 */
	public void updateType() {
		p1.sendMessage(type.u);
		p2.sendMessage(type.u);
		p1.getPA().sendFrame126(type.s, 37215);
		p2.getPA().sendFrame126(type.s, 37215);
		p1.getPA().sendFrame34a(37214, type.i, 0, 1);
		p2.getPA().sendFrame34a(37214, type.i, 0, 1);
	}

	/**
	 * Updates the status of the duel
	 */
	public void updateStatus() {
		if (!p1Accepted && !p2Accepted) {
			p1.getPA().sendFrame126("", 37211);
			p2.getPA().sendFrame126("", 37211);
		} else if (p1Accepted && !p2Accepted) {
			p1.getPA().sendFrame126("Waiting for other player...", 37211);
			p2.getPA().sendFrame126("Other player has accepted", 37211);
		} else if (p2Accepted && !p1Accepted) {
			p1.getPA().sendFrame126("Other player has accepted", 37211);
			p2.getPA().sendFrame126("Waiting for other player...", 37211);
		} else if (p2Accepted && p1Accepted) {
			startGame();
		}
	}

	/**
	 * Starts the game once both players have accepted
	 */
	public void startGame() {
		loadPots();
		type.game.run(this);
	}

	/**
	 * Handles if the game was a tie
	 */
	public void gameTie() {
		p1.sendMessage("The game ended in a draw!");
		p2.sendMessage("The game ended in a draw!");
		for (int i = 0; i < 12; i++) {
			if (p1.gambleItems[i] > 0) {
				ItemAssistant.addItem(p1, p1.gambleItems[i], p1.gambleAmounts[i]);
			}
			if (p2.gambleItems[i] > 0) {
				ItemAssistant.addItem(p2, p2.gambleItems[i], p2.gambleAmounts[i]);
			}
		}
	}

	/**
	 * Handles the winner
	 */
	public void giveWinnings(Player winner, Player loser, boolean cheatAttempt) {
		for (int i = 0; i < 24; i++) {
			ItemAssistant.addItem(winner, pot[i], potA[i]);
			pot[i] = 0;
			potA[i] = 0;
		}
		if (!cheatAttempt) {
			winner.sendMessage("You have won the game!");
			loser.sendMessage("You have lost the game!");
		}
	}



	/**
	 * Combines both players bet into one pot.
	 */
	public void loadPots() {
		for (int i = 0; i < 12; i++) {
			if (p1.gambleItems[i] > 0) {
				int slot = getFreePotSlot();
				pot[slot] = p1.gambleItems[i];
				potA[slot] = p1.gambleAmounts[i];
			}
			if (p2.gambleItems[i] > 0) {
				int slot = getFreePotSlot();
				pot[slot] = p2.gambleItems[i];
				potA[slot] = p2.gambleAmounts[i];
			}
		}
		sendPot();
	}
	public int getFreePotSlot() {
		for (int i = 0; i < pot.length; i++) {
			if (pot[i] <= 0) {
				return i;
			}
		}
		return 0;
	}

	public void sendPot() {
		if (p1.getOutStream() != null && p1 != null && p2.getOutStream() != null && p2 != null) {
			p1.getOutStream().createFrameVarSizeWord(53);
			p1.getOutStream().writeWord(37233);
			p1.getOutStream().writeWord(12);
			for (int i = 0; i < pot.length; i++) {
				int displayItem = pot[i] + 1;
				if (displayItem <= 1) {
					displayItem = 0;
				}
				if (potA[i] > 254) {
					p1.getOutStream().writeByte(255);
					p1.getOutStream().writeDWord_v2(potA[i]);
				} else { 
					p1.getOutStream().writeByte(potA[i]);
				}
				p1.getOutStream().writeWordBigEndianA(displayItem);
			}
			p1.getOutStream().endFrameVarSizeWord();
			p1.flushOutStream();
			p2.getOutStream().createFrameVarSizeWord(53);
			p2.getOutStream().writeWord(37233);
			p2.getOutStream().writeWord(12);
			for (int i = 0; i < pot.length; i++) {
				int displayItem = pot[i] + 1;
				if (displayItem <= 1) {
					displayItem = 0;
				}
				if (potA[i] > 254) {
					p2.getOutStream().writeByte(255);
					p2.getOutStream().writeDWord_v2(potA[i]);
				} else { 
					p2.getOutStream().writeByte(potA[i]);
				}
				p2.getOutStream().writeWordBigEndianA(displayItem);
			}
			p2.getOutStream().endFrameVarSizeWord();
			p2.flushOutStream();
		}
	}


	/**
	 * When something is changed, it makes it so both players need
	 * to accept again, to prevent quick scams.
	 */
	public void somethingChanged() {
		p1Accepted = false;
		p2Accepted = false;
		updateStatus();
	}


	/**
	 * Updates the bets for both players
	 */
	public void updateBets() {
		if (p1.getOutStream() != null && p1 != null) {
			p1.getOutStream().createFrameVarSizeWord(53);
			p1.getOutStream().writeWord(37221);
			p1.getOutStream().writeWord(12);
			for (int i = 0; i < p1.gambleItems.length; i++) {
				int displayItem = p1.gambleItems[i] + 1;
				if (displayItem <= 1) {
					displayItem = 0;
				}
				if (p1.gambleAmounts[i] > 254) {
					p1.getOutStream().writeByte(255);
					p1.getOutStream().writeDWord_v2(p1.gambleAmounts[i]);
				} else { 
					p1.getOutStream().writeByte(p1.gambleAmounts[i]);
				}
				p1.getOutStream().writeWordBigEndianA(displayItem);
			}
			p1.getOutStream().endFrameVarSizeWord();
			p1.flushOutStream();
		}
		if (p1.getOutStream() != null && p1 != null) {
			p1.getOutStream().createFrameVarSizeWord(53);
			p1.getOutStream().writeWord(37226);
			p1.getOutStream().writeWord(12);
			for (int i = 0; i < p2.gambleItems.length; i++) {
				int displayItem = p2.gambleItems[i] + 1;
				if (displayItem <= 1) {
					displayItem = 0;
				}
				if (p2.gambleAmounts[i] > 254) {
					p1.getOutStream().writeByte(255);
					p1.getOutStream().writeDWord_v2(p2.gambleAmounts[i]);
				} else { 
					p1.getOutStream().writeByte(p2.gambleAmounts[i]);
				}
				p1.getOutStream().writeWordBigEndianA(displayItem);
			}
			p1.getOutStream().endFrameVarSizeWord();
			p1.flushOutStream();
		}
		if (p2.getOutStream() != null && p2 != null) {
			p2.getOutStream().createFrameVarSizeWord(53);
			p2.getOutStream().writeWord(37221);
			p2.getOutStream().writeWord(12);
			for (int i = 0; i < p2.gambleItems.length; i++) {
				int displayItem = p2.gambleItems[i] + 1;
				if (displayItem <= 1) {
					displayItem = 0;
				}
				if (p2.gambleAmounts[i] > 254) {
					p2.getOutStream().writeByte(255);
					p2.getOutStream().writeDWord_v2(p2.gambleAmounts[i]);
				} else { 
					p2.getOutStream().writeByte(p2.gambleAmounts[i]);
				}
				p2.getOutStream().writeWordBigEndianA(displayItem);
			}
			p2.getOutStream().endFrameVarSizeWord();
			p2.flushOutStream();
		}
		if (p2.getOutStream() != null && p2 != null) {
			p2.getOutStream().createFrameVarSizeWord(53);
			p2.getOutStream().writeWord(37226);
			p2.getOutStream().writeWord(12);
			for (int i = 0; i < p1.gambleItems.length; i++) {
				int displayItem = p1.gambleItems[i] + 1;
				if (displayItem <= 1) {
					displayItem = 0;
				}
				if (p1.gambleAmounts[i] > 254) {
					p2.getOutStream().writeByte(255);
					p2.getOutStream().writeDWord_v2(p1.gambleAmounts[i]);
				} else { 
					p2.getOutStream().writeByte(p1.gambleAmounts[i]);
				}
				p2.getOutStream().writeWordBigEndianA(displayItem);
			}
			p2.getOutStream().endFrameVarSizeWord();
			p2.flushOutStream();
		}
	}


	/**
	 * Resets the game.
	 */
	public void reset() {
		if (p1 != null) {
			for (int i = 0; i < 12; i++) {
				p1.gambleItems[i] = 0;
				p1.gambleAmounts[i] = 0;
			}
			p1.gamble = null;
		}
		if (p2 != null) {
			for (int i = 0; i < 12; i++) {
				p2.gambleItems[i] = 0;
				p2.gambleAmounts[i] = 0;
			}
			p2.gamble = null;
		}
	}



	/**
	 * Emptys the items displayed on the gambling interface
	 * for the bets, before the players see the gamble interface
	 */
	public void empty(Player c) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(37221);
			c.getOutStream().writeWord(0);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(53);
			c.getOutStream().writeWord(37226);
			c.getOutStream().writeWord(0);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}


	public enum Flowers {
		RED(0, 2462),
		BLUE(1, 2464),
		YELLOW(2, 2466),
		PURPLE(3, 2468),
		ORANGE(4, 2470),
		RAINBOW(5, 2472),
		PASTEL(6, 2460),
		;
		public int id, n;
		private Flowers(int num, int itemId) {
			this.n = num;
			this.id = itemId;
		}
		public static Flowers forID(int flower) {
			for (Flowers pp : Flowers.values()) {
				if (pp.id == flower) {
					return pp;
				}
			}
			return null;
		}
	}

	public enum PokerHand {
		BUST(0, "Bust"),
		ONE_PAIR(1, "One Pair"),
		TWO_PAIR(2, "Two Pair"),
		THREE_OAK(3, "Three Oak"),
		FULL_HOUSE(4, "Full House"),
		FOUR_OAK(5, "Four Oak"),
		FIVE_OAK(6, "Five Oak"),
		;
		public int points;
		public String name;
		private PokerHand(int p, String n) {
			this.points = p;
			this.name = n;
		}
		public static PokerHand forID(int pts) {
			for (PokerHand pp : PokerHand.values()) {
				if (pp.points == pts) {
					return pp;
				}
			}
			return null;
		}
	}

	public PokerHand getHand(Flowers[] flowers) {
		int[] count = {0, 0, 0, 0, 0, 0, 0, 0, 0};
		for (int i = 0; i < flowers.length; i++) {
			if (flowers[i] != null) {
				count[flowers[i].n] ++;
			}
		}
		boolean onePair = false;
		boolean twoPair = false;
		boolean threeOak = false;
		boolean fullHouse = false;
		boolean fourOak = false;
		boolean fiveOak = false;
		boolean foundPair = false;
		for (int i = 0; i < count.length; i++) {
			if (count[i] == 2) {
				if (foundPair) {
					twoPair = true;
				} else {
					onePair = true;
					foundPair = true;
				}
			}
			if (count[i] == 3) {
				threeOak = true;
			}
			if (count[i] == 4) {
				fourOak = true;
			}
			if (count[i] == 5) {
				fiveOak = true;
			}
		}
		if (threeOak || twoPair) {
			boolean fhc1 = false;
			boolean fhc2 = false;
			for (int j = 0; j < count.length; j++) {
				if (count[j] == 3) {
					fhc1 = true;
				}
				if (count[j] == 2) {
					fhc2 = true;
				}
			}
			if (fhc1 && fhc2)
				fullHouse = true;
		}
		if (fiveOak)	return PokerHand.FIVE_OAK;
		if (fourOak)	return PokerHand.FOUR_OAK;
		if (fullHouse)	return PokerHand.FULL_HOUSE;
		if (threeOak)	return PokerHand.THREE_OAK;
		if (twoPair)	return PokerHand.TWO_PAIR;
		if (onePair)	return PokerHand.ONE_PAIR;
		return PokerHand.BUST;
	}


}
