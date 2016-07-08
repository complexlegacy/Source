package ionic.player;

import ionic.item.ItemAssistant;
import utility.Misc;

public class MysteryBox {
	
	private static int[] commonLoots = {995};
	private static int[] commonLootAmounts = {50000};
	private static int[] unCommonLoots = {995};
	private static int[] unCommonLootAmounts = {500000};
	private static int[] rareLoots = {995};
	private static int[] rareLootAmounts = {5000000};
	
	
	public static void openBox(Player c) {
		if (ItemAssistant.playerHasItem(c, 6199)) {
			Rarity lootRarity = Rarity.COMMON;
			ItemAssistant.deleteItem(c, 6199, 1);
			int chance = Misc.random(150);
			lootRarity = (chance > 0 && chance < 4) ? Rarity.RARE : (chance >= 4 && chance < 42) ? Rarity.UNCOMMON : Rarity.COMMON;
			giveLoot(c, lootRarity);
		}
	}
	
	public static void giveLoot(Player c, Rarity rarity) {
		int loot = Misc.random(rarity.itemLoots.length - 1);
		int item = rarity.itemLoots[loot];
		int amount = rarity.amountLoots[loot];
		ItemAssistant.addItem(c, item, amount);
		c.sendMessage("You opened a mystery box and received a loot from the "+rarity.name+" loot table");
	}
	
	public enum Rarity {
		COMMON(commonLoots, commonLootAmounts, "Common"),
		UNCOMMON(unCommonLoots, unCommonLootAmounts, "Uncommon"),
		RARE(rareLoots, rareLootAmounts, "Rare");
		private Rarity(int[] lootItems, int[] lootAmounts, String tablename) {
			this.itemLoots = lootItems;
			this.amountLoots = lootAmounts;
			this.name = tablename;
		}
		public int[] itemLoots;
		public int[] amountLoots;
		public String name;
	}
	
	
}
