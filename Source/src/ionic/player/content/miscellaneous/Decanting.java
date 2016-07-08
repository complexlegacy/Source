package ionic.player.content.miscellaneous;

import ionic.player.Client;
import ionic.player.Player;
import ionic.player.banking.BankConstants;
import ionic.player.banking.BankHandler;
import ionic.player.content.consumable.PotionData;

public class Decanting {

	public static void decantBank(Player c) {
		PotionData[][] bankPots = new PotionData[BankConstants.TOTAL_TABS][BankConstants.MAX_ITEMS_PER_TAB];
		int[][] doses = new int[BankConstants.TOTAL_TABS][BankConstants.MAX_ITEMS_PER_TAB];
		for (int i = 0; i < c.getBank().bankItems.length; i++) {
			for (int k = 0; k < c.getBank().bankItems[i].length; k++) {
				if (c.getBank().bankItems[i][k] > 0) {
					PotionData d = PotionData.findPotion(c.getBank().bankItems[i][k]);
					if (d != null) {
						bankPots[i][k] = d;
						doses[i][k] = getDoses(d, c.getBank().bankItems[i][k]) * c.getBank().bankAmounts[i][k];
						c.getBank().bankItems[i][k] = -1;
						c.getBank().bankAmounts[i][k] = 0;
					}
				}
			}
			for (int bb = 0; bb < 125; bb++) {
				BankHandler.rearrangeTabItems2((Client)c, i);
			}
		}
		for (int b = 0; b < bankPots.length; b++) {
			for (int j = 0; j < bankPots[b].length; j++) {
				if (bankPots[b][j] != null) {
					for (int i = 0; i < bankPots.length; i++) {
						for (int n = 0; n < bankPots[i].length; n++) {
							if (bankPots[i][n] == bankPots[b][j]) {
								if (can(b, i, j, n)) {
									bankPots[i][n] = null;
									doses[b][j] += doses[i][n];
									doses[i][n] = 0;
								}
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < bankPots.length; i++) {
			for (int j = 0; j < bankPots[i].length; j++) {
				if (bankPots[i][j] != null) {
					c.sendMessage("Total doses to add: "+doses[i][j]+"");
				}
			}
		}
		BankHandler.refreshBank((Client)c);
	}
	
	
	private static boolean can(int k, int b, int o, int h) {
		if (o == h && k == b) {
			return false;
		}
		return true;
	}

	private static int getDoses(PotionData d, int item) {
		if (item == d.six) return 6;
		if (item == d.five) return 5;
		if (item == d.four) return 4;
		if (item == d.three) return 3;
		if (item == d.two) return 2;
		if (item == d.one) return 1;
		return 0;
	}


}
