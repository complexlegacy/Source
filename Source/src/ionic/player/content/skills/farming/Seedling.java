package ionic.player.content.skills.farming;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.Player;

import java.util.HashMap;
import java.util.Map;


public class Seedling {

	private Client player;

	public Seedling(Player player) {
		this.player = (Client) player;
	}

	public enum SeedlingData {
		OAK(5312, 5358, 5364, 5370), WILLOW(5313, 5359, 5365, 5371), MAPLE(5314, 5360, 5366, 5372), YEW(5315, 5361, 5367, 5373), MAGIC(5316, 5362, 5368, 5374), SPIRIT(5317, 5363, 5369, 5375), APPLE(5283, 5480, 5488, 5496), BANANA(5284, 5481, 5489, 5497), ORANGE(5285, 5482, 5490, 5498), CURRY(5286, 5483, 5491, 5499), PINEAPPLE(5287, 5484, 5492, 5500), PAPAYA(5288, 5485, 5493, 5501), PALM(5289, 5486, 5494, 5502), CALQUAT(5290, 5487, 5495, 5503);

		private int seedId;
		private int unwateredSeedlingId;
		private int wateredSeedlingId;
		private int saplingId;

		private static Map<Integer, SeedlingData> seeds = new HashMap<Integer, SeedlingData>();
		private static Map<Integer, SeedlingData> unwatered = new HashMap<Integer, SeedlingData>();
		private static Map<Integer, SeedlingData> watered = new HashMap<Integer, SeedlingData>();

		static {
			for (SeedlingData data : SeedlingData.values()) {
				seeds.put(data.seedId, data);
				unwatered.put(data.unwateredSeedlingId, data);
				watered.put(data.wateredSeedlingId, data);
			}
		}
		SeedlingData(int seedId, int unwateredSeedlingId, int wateredSeedlingId, int saplingId) {
			this.seedId = seedId;
			this.unwateredSeedlingId = unwateredSeedlingId;
			this.wateredSeedlingId = wateredSeedlingId;
			this.saplingId = saplingId;
		}

		public static SeedlingData getSeed(int seedId) {
			return seeds.get(seedId);
		}

		public static SeedlingData getUnwatered(int seedId) {
			return unwatered.get(seedId);
		}

		public static SeedlingData getWatered(int seedId) {
			return watered.get(seedId);
		}

		public int getSeedId() {
			return seedId;
		}

		public int getUnwateredSeedlingId() {
			return unwateredSeedlingId;
		}

		public int getWateredSeedlingId() {
			return wateredSeedlingId;
		}

		public int getSaplingId() {
			return saplingId;
		}
	}

	public boolean waterSeedling(int itemUsed, int usedWith, int itemUsedSlot, int usedWithSlot) {
		SeedlingData seedlingData = SeedlingData.getUnwatered(itemUsed);
		if (seedlingData == null)
			seedlingData = SeedlingData.getUnwatered(usedWith);
		if (seedlingData == null || (!ItemAssistant.getItemName(itemUsed).toLowerCase().contains("watering") && !ItemAssistant.getItemName(usedWith).toLowerCase().contains("watering")))
			return false;

//		if (itemUsed >= 5333 && itemUsed <= 5340)
//			ItemAssistant.set(itemUsedSlot, new Item(itemUsed == 5333 ? itemUsed - 2 : itemUsed - 1));
//		if (usedWith >= 5333 && usedWith <= 5340)
//			ItemAssistant.set(usedWithSlot, new Item(usedWith == 5333 ? usedWith - 2 : usedWith - 1));

		player.sendMessage("You water the " + ItemAssistant.getItemName(seedlingData.getSeedId()).toLowerCase() + ".");
		ItemAssistant.deleteItem(player, seedlingData.getUnwateredSeedlingId(), 1);
		ItemAssistant.addItem(player, seedlingData.getWateredSeedlingId(), 1);
		return true;

	}

	public boolean placeSeedInPot(int itemUsed, int usedWith, int itemUsedSlot, int usedWithSlot) {
		SeedlingData seedlingData = SeedlingData.getSeed(itemUsed);
		if (seedlingData == null)
			seedlingData = SeedlingData.getUnwatered(usedWith);
		if (seedlingData == null || (itemUsed != 5354 && usedWith != 5354))
			return false;
		ItemAssistant.deleteItem(player, seedlingData.getSeedId(), 1);
		player.playerItems[itemUsed == 5354 ? itemUsedSlot : usedWithSlot] = seedlingData.getUnwateredSeedlingId();
		player.playerItemsN[itemUsed == 5354 ? itemUsedSlot : usedWithSlot] = 1;
		player.sendMessage("You sow some maple tree seeds in the plantpots.");
		player.sendMessage("They need watering before they will grow.");
		return true;
	}

	public boolean fillPotWithSoil(int itemId, int objectX, int objectY) {
		if (itemId != 5350)
			return false;
		
                /*TODO DISABLED
                 * 
                 */
                /*if (!(player.getAllotment().checkIfRaked(objectX, objectY) || player.getBushes().checkIfRaked(objectX, objectY) || player.getFlowers().checkIfRaked(objectX, objectY) || player.getFruitTrees().checkIfRaked(objectX, objectY) || player.getHerbs().checkIfRaked(objectX, objectY) || player.getHops().checkIfRaked(objectX, objectY) || player.getTrees().checkIfRaked(objectX, objectY) || player.getSpecialPlantOne().checkIfRaked(objectX, objectY) || player.getSpecialPlantTwo().checkIfRaked(objectX, objectY))) {
			player.getActionSender().sendMessage("You can only fill your pot on raked patches.");
			return true;
		}*/
                
		if (!ItemAssistant.playerHasItem(player, FarmingConstants.TROWEL)) {
			player.sendMessage("You need a gardening trowel to fill this pot with soil.");
			return true;
		}
		ItemAssistant.deleteItem(player, itemId, 1);
		player.startAnimation(FarmingConstants.FILLING_POT_ANIM);
		player.sendMessage("You fill the empty plant pot with soil.");
		ItemAssistant.addItem(player, 5354, 1);
		return true;
	}
}

