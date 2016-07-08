package ionic.player.content.miscellaneous;

import ionic.player.Client;
import ionic.player.PlayerSave;
import ionic.player.banking.BankHandler;
import ionic.player.content.quest.cutscene.CutsceneHandler;
import ionic.player.content.quest.cutscene.Tutorial;

public class Starter {
	
	public int[][] starterItems = {
			{995, 125000}, {946}, {1351}, {1265}, {1436, 200}, {590}, {1755}, {227, 50}, {307}, {313, 300},
			{554, 250}, {555, 250}, {556, 250}, {557, 250}, {558, 250}, {559, 250}, {560, 50}, {562, 50}, {561, 30}, {565, 50},
			{577}, {1011}, {579}, {1379}, {1731}, {1115}, {1067}, {1153}, {1191}, {1323},
			{1325}, {1331}, {841}, {882, 200}, {884, 200}, {1059}, {1061}, {1063}, {1129}, {1095},
			};
	
	public Starter(Client c) {
		if (c.gotStarter) { return; }
		c.gotStarter = true;
		PlayerSave.saveGame(c);
		c.getPA().showInterface(3559);
        c.canChangeAppearance = true;
        giveItems(c);
        c.sendMessage("Some useful items have been added to your bank, to help you get started.");
	}
	
	public void giveItems(Client c) {
		for (int i = 0; i < starterItems.length; i++) {
			int item = starterItems[i][0];
			int amount = 1;
			if (starterItems[i].length > 1) {
				amount = starterItems[i][1];
			}
			add(c, item, amount);
		}
		BankHandler.refreshTabItems(c, 0);
	}
	
	public void add(Client c, int item, int amount) {
		BankHandler.forceItemAdd(c, item, amount, -1);
	}

}
