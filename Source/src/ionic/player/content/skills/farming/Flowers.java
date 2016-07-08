/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ionic.player.content.skills.farming;

/**
 *
 * @author ArrowzFtw
 */
import core.Constants;
import core.Server;
import ionic.item.Item;
import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.dialogue.Dialogue;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import utility.Misc;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by IntelliJ IDEA. User: vayken Date: 24/02/12 Time: 20:34 To change
 * this template use File | Settings | File Templates.
 */
public class Flowers { // todo scarecrow 6059

	private Client player;

	// set of global constants for Farming

	private static final double WATERING_CHANCE = 0.5;
	private static final double COMPOST_CHANCE = 0.9;
	private static final double SUPERCOMPOST_CHANCE = 0.7;
	private static final double CLEARING_EXPERIENCE = 4;
	public static final int SCARECROW = 6059;

	public Flowers(Player player) {
		this.player = (Client) player;
	}

	// Farming data
	public int[] flowerStages = new int[4];
	public int[] flowerSeeds = new int[4];
	public int[] flowerState = new int[4];
	public long[] flowerTimer = new long[4];
	public double[] diseaseChance = {1, 1, 1, 1};
	public boolean[] hasFullyGrown = {false, false, false, false};

	/* set of the constants for the patch */

	// states - 2 bits plant - 6 bits
	public static final int GROWING = 0x00;
	public static final int WATERED = 0x01;
	public static final int DISEASED = 0x02;
	public static final int DEAD = 0x03;

	public static final int FLOWER_PATCH_CONFIGS = 508;

	/* This is the enum holding the seeds info */

	public enum FlowerData {

		MARIGOLD(5096, 6010, 2, 20, 0.35, 8.5, 47, 0x08, 0x0c),
		ROSEMARY(5097, 6014, 11, 20, 0.32, 12, 66.5, 0x0d, 0x11),
		NASTURTIUM(5098, 6012, 24, 20, 0.30, 19.5, 111, 0x12, 0x16),
		WOAD(5099, 1793, 25, 20, 0.27, 20.5, 115.5, 0x17, 0x1b),
		LIMPWURT(5100, 225, 26, 25, 21.5, 8.5, 120, 0x1c, 0x20), ;

		private int seedId;
		private int harvestId;
		private int levelRequired;
		private int growthTime;
		private double diseaseChance;
		private double plantingXp;
		private double harvestXp;
		private int startingState;
		private int endingState;

		private static Map<Integer, FlowerData> seeds = new HashMap<Integer, FlowerData>();

		static {
			for (FlowerData data : FlowerData.values()) {
				seeds.put(data.seedId, data);
			}
		}

		FlowerData(int seedId, int harvestId, int levelRequired, int growthTime, double diseaseChance, double plantingXp, double harvestXp, int startingState, int endingState) {
			this.seedId = seedId;
			this.harvestId = harvestId;
			this.levelRequired = levelRequired;
			this.growthTime = growthTime;
			this.diseaseChance = diseaseChance;
			this.plantingXp = plantingXp;
			this.harvestXp = harvestXp;
			this.startingState = startingState;
			this.endingState = endingState;
		}

		public static FlowerData forId(int seedId) {
			return seeds.get(seedId);
		}

		public int getSeedId() {
			return seedId;
		}

		public int getHarvestId() {
			return harvestId;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public int getGrowthTime() {
			return growthTime;
		}

		public double getDiseaseChance() {
			return diseaseChance;
		}

		public double getPlantingXp() {
			return plantingXp * 20;
		}

		public double getHarvestXp() {
			return harvestXp * 20;
		}

		public int getStartingState() {
			return startingState;
		}

		public int getEndingState() {
			return endingState;
		}
	}

	/* This is the enum data about the different patches */

	public enum FlowerFieldsData {
		ARDOUGNE(0, new Point[]{new Point(2666, 3374), new Point(2667, 3375)}),
		PHASMATYS(1, new Point[]{new Point(3601, 3525), new Point(3602, 3526)}),
		FALADOR(2, new Point[]{new Point(3054, 3307), new Point(3055, 3308)}),
		CATHERBY(3, new Point[]{new Point(2809, 3463), new Point(2810, 3464)});

		private int flowerIndex;
		private Point[] flowerPosition;

		FlowerFieldsData(int flowerIndex, Point[] flowerPosition) {
			this.flowerIndex = flowerIndex;
			this.flowerPosition = flowerPosition;
		}

		public static FlowerFieldsData forIdPosition(Point point) {
			for (FlowerFieldsData flowerFieldsData : FlowerFieldsData.values()) {
				if (FarmingConstants.inRangeArea(flowerFieldsData.getFlowerPosition()[0], flowerFieldsData.getFlowerPosition()[1], point)) {
					return flowerFieldsData;
				}
			}
			return null;
		}

		public int getFlowerIndex() {
			return flowerIndex;
		}

		public Point[] getFlowerPosition() {
			return flowerPosition;
		}
	}

	/* This is the enum that hold the different data for inspecting the plant */

	public enum InspectData {

		MARIGOLD(5096, new String[][]{{"The seeds have only just been planted."}, {"The marigold plants have developed leaves."}, {"The marigold plants have begun to grow their", "flowers. The new flowers are orange and small at", "first."}, {"The marigold plants are larger, and more", "developed in their petals."}, {"The marigold plants are ready to harvest. Their", "flowers are fully matured."}}), ROSEMARY(5097, new String[][]{{"The seeds have only just been planted."}, {"The rosemary plant is taller than before."}, {"The rosemary plant is bushier and taller than", "before."}, {"The rosemary plant is developing a flower bud at", "its top."}, {"The plant is ready to harvest. The rosemary", "plant's flower has opened."}}), NASTURTIUM(5098, new String[][]{{"The nasturtium seed has only just been planted."}, {"The nasturtium plants have started to develop", "leaves."}, {"The nasturtium plants have grown more leaves,", "and nine flower buds."},
				{"The nasturtium plants open their flower buds."}, {"The plants are ready to harvest. The nasturtium", "plants grow larger than before and the flowers", "fully open."}}), WOAD(5099, new String[][]{{"The woad seed has only just been planted."}, {"The woad plant produces more stalks, that split", "in tow near the top."}, {"The woad plant grows more segments from its", "intitial stalks."}, {"The woad plant develops flower buds on the end", "of each of its stalks."}, {"The woad plant is ready to harvest. The plant has", "all of its stalks pointing directly up, with", "all flowers open."}}), LIMPWURT(5100, new String[][]{{"The seed has only just been planted."}, {"The limpwurt plant produces more roots."}, {"The limpwurt plant produces an unopened pink", "flower bud and continues to grow larger."}, {"The limpwurt plant grows larger, with more loops", "in its roots. The flower bud is still unopened."},
				{"The limpwurt plant is ready to harvest. The", "flower finally opens wide, with a spike in the", "middle."}});
		private int seedId;
		private String[][] messages;

		private static Map<Integer, InspectData> seeds = new HashMap<Integer, InspectData>();

		static {
			for (InspectData data : InspectData.values()) {
				seeds.put(data.seedId, data);
			}
		}

		InspectData(int seedId, String[][] messages) {
			this.seedId = seedId;
			this.messages = messages;
		}

		public static InspectData forId(int seedId) {
			return seeds.get(seedId);
		}

		public int getSeedId() {
			return seedId;
		}

		public String[][] getMessages() {
			return messages;
		}
	}

	/* update all the patch states */

	public void updateFlowerStates() {
		// ardougne - phasmatys - falador - catherby
		int[] configValues = new int[flowerStages.length];

		int configValue;
		for (int i = 0; i < flowerStages.length; i++) {
			configValues[i] = getConfigValue(flowerStages[i], flowerSeeds[i], flowerState[i], i);
		}

		configValue = (configValues[0] << 16) + (configValues[1] << 8 << 16) + configValues[2] + (configValues[3] << 8);
		player.getPA().sendConfig(FLOWER_PATCH_CONFIGS, configValue);

	}

	/* getting the different config values */

	public int getConfigValue(int flowerStage, int seedId, int plantState, int index) {
		if (flowerSeeds[index] >= 0x21 && flowerSeeds[index] <= 0x24 && flowerStages[index] > 3) {
			return (GROWING << 6) + flowerSeeds[index];
		}
		FlowerData flowerData = FlowerData.forId(seedId);
		switch (flowerStage) {
			case 0 :// weed
				return (GROWING << 6) + 0x00;
			case 1 :// weed cleared
				return (GROWING << 6) + 0x01;
			case 2 :
				return (GROWING << 6) + 0x02;
			case 3 :
				return (GROWING << 6) + 0x03;
		}
		if (flowerData == null) {
			return -1;
		}
		if (flowerData.getEndingState() == flowerData.getStartingState() + flowerStage - 2) {
			hasFullyGrown[index] = true;
		}
		return (getPlantState(plantState) << 6) + flowerData.getStartingState() + flowerStage - 4;
	}

	/* getting the plant states */

	public int getPlantState(int plantState) {
		switch (plantState) {
			case 0 :
				return GROWING;
			case 1 :
				return WATERED;
			case 2 :
				return DISEASED;
			case 3 :
				return DEAD;
		}
		return -1;
	}

	/* calculating the disease chance and making the plant grow */

	public void doCalculations() {
		for (int i = 0; i < flowerSeeds.length; i++) {
			if (flowerStages[i] > 0 && flowerStages[i] <= 3 && Server.getMinutesCounter() - flowerTimer[i] >= 5) {
				flowerStages[i]--;
				flowerTimer[i] = Server.getMinutesCounter();
				updateFlowerStates();
			}
			if (Server.getMinutesCounter() - flowerTimer[i] >= 5 && flowerSeeds[i] > 0x21 && flowerSeeds[i] <= 0x24) {
				flowerSeeds[i]--;
				updateFlowerStates();
				return;
			}
			FlowerData flowerData = FlowerData.forId(flowerSeeds[i]);
			if (flowerData == null) {
				continue;
			}

			long difference = Server.getMinutesCounter() - flowerTimer[i];
			long growth = flowerData.getGrowthTime();
			int nbStates = flowerData.getEndingState() - flowerData.getStartingState();
			int state = (int) (difference * nbStates / growth);
			if (flowerState[i] == 3 || flowerSeeds[i] == 0x21 || flowerTimer[i] == 0 || state > nbStates) {
				continue;
			}

			if (4 + state != flowerStages[i]) {
				flowerStages[i] = 4 + state;
				doStateCalculation(i);
				updateFlowerStates();
			}
		}
	}

	/* calculations about the diseasing chance */

	public void doStateCalculation(int index) {
		if (flowerState[index] == 3) {
			return;
		}
		// if the patch is diseased, it dies, if its watched by a farmer, it
		// goes back to normal
		if (flowerState[index] == 2) {
			flowerState[index] = 3;
		}

		if (flowerState[index] == 1 || flowerState[index] == 5 && flowerStages[index] != 3) {
			diseaseChance[index] *= 2;
			flowerState[index] = 0;
		}
		if (flowerState[index] == 0 && flowerStages[index] >= 5 && !hasFullyGrown[index]) {
			FlowerData flowerData = FlowerData.forId(flowerSeeds[index]);
			if (flowerData == null) {
				return;
			}
			double chance = diseaseChance[index] * flowerData.getDiseaseChance();
			int maxChance = (int) (chance * 100);

			if (Misc.random(100) <= maxChance) {
				flowerState[index] = 2;
			}
		}
	}

	/* watering the patch */

	public boolean waterPatch(int objectX, int objectY, int itemId) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Point(objectX, objectY));
		if (flowerFieldsData == null) {
			return false;
		}
		FlowerData flowerData = FlowerData.forId(flowerSeeds[flowerFieldsData.getFlowerIndex()]);
		if (flowerData == null) {
			return false;
		}
		if (flowerState[flowerFieldsData.getFlowerIndex()] == 1 || flowerStages[flowerFieldsData.getFlowerIndex()] <= 1 || flowerStages[flowerFieldsData.getFlowerIndex()] == flowerData.getEndingState() - flowerData.getStartingState() + 4) {
			player.sendMessage("This patch doesn't need watering.");
			return true;
		}
		ItemAssistant.deleteItem(player, itemId, 1);
		ItemAssistant.addItem(player, itemId == 5333 ? itemId - 2 : itemId - 1, 1);

		if (!ItemAssistant.playerHasItem(player, FarmingConstants.RAKE)) {
			Dialogue.sendStatement2(player, new String[] {"You need a seed dibber to plant seed here."});
			return true;
		}
		player.sendMessage("You water the patch.");
		player.startAnimation(FarmingConstants.WATERING_CAN_ANIM);

		player.stopPlayerSkill = true;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				diseaseChance[flowerFieldsData.getFlowerIndex()] *= WATERING_CHANCE;
				flowerState[flowerFieldsData.getFlowerIndex()] = 1;
				container.stop();
			}

			@Override
			public void stop() {
				updateFlowerStates();
				player.stopPlayerSkill = false;
				player.getPA().resetAnimation();
			}
		}, 5);
		return true;
	}

	/* clearing the patch with a rake of a spade */

	public boolean clearPatch(int objectX, int objectY, int itemId) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Point(objectX, objectY));
		int finalAnimation;
		int finalDelay;
		if (flowerFieldsData == null || (itemId != FarmingConstants.RAKE && itemId != FarmingConstants.SPADE)) {
			return false;
		}
		if (flowerStages[flowerFieldsData.getFlowerIndex()] == 3) {
			return true;
		}
		if (flowerStages[flowerFieldsData.getFlowerIndex()] <= 3) {
			if (!ItemAssistant.playerHasItem(player, FarmingConstants.RAKE)) {
				Dialogue.sendStatement2(player, new String[] {"You need a rake to clear this path."});
				return true;
			} else {
				finalAnimation = FarmingConstants.RAKING_ANIM;
				finalDelay = 5;
			}
		} else {
			if (!ItemAssistant.playerHasItem(player, FarmingConstants.SPADE)) {
				Dialogue.sendStatement2(player, new String[] {"You need a spade to clear this path."});
				return true;
			} else {
				finalAnimation = FarmingConstants.SPADE_ANIM;
				finalDelay = 3;
			}
		}
		final int animation = finalAnimation;
		player.stopPlayerSkill = true;
		player.startAnimation(animation);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.startAnimation(animation);
				if (flowerStages[flowerFieldsData.getFlowerIndex()] <= 2) {
					flowerStages[flowerFieldsData.getFlowerIndex()]++;
					ItemAssistant.addItem(player, 6055, 1);
				} else {
					flowerStages[flowerFieldsData.getFlowerIndex()] = 3;
					container.stop();
				}
				player.getPA().addSkillXP((int) CLEARING_EXPERIENCE, Constants.FARMING);
				flowerTimer[flowerFieldsData.getFlowerIndex()] = Server.getMinutesCounter();
				updateFlowerStates();
				if (flowerStages[flowerFieldsData.getFlowerIndex()] == 3) {
					container.stop();
					return;
				}
			}

			@Override
			public void stop() {
				resetFlowers(flowerFieldsData.getFlowerIndex());
				player.sendMessage("You clear the patch.");
				player.stopPlayerSkill = false;
				player.getPA().resetAnimation();
			}
		}, finalDelay);
		return true;

	}

	/* planting the seeds */

	public boolean plantSeed(int objectX, int objectY, final int seedId) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Point(objectX, objectY));
		final FlowerData flowerData = FlowerData.forId(seedId);
		if (flowerFieldsData == null || flowerData == null) {
			return false;
		}
		if (flowerStages[flowerFieldsData.getFlowerIndex()] != 3) {
			Dialogue.sendStatement2(player, new String[] {"You can't plant a seed here."});
			return false;
		}
		if (flowerData.getLevelRequired() > player.skillLevel[Constants.FARMING]) {
			Dialogue.sendStatement2(player, new String[] {"You need a farming level of " + flowerData.getLevelRequired() + " to plant this seed."});
			return true;
		}
		if (!ItemAssistant.playerHasItem(player, FarmingConstants.SEED_DIBBER)) {
			Dialogue.sendStatement2(player, new String[] {"You need a seed dibber to plant seed here."});
			return true;
		}

		player.startAnimation(FarmingConstants.SEED_DIBBING);
		flowerStages[flowerFieldsData.getFlowerIndex()] = 4;
		ItemAssistant.deleteItem(player, seedId, 1);

		player.stopPlayerSkill = true;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				flowerState[flowerFieldsData.getFlowerIndex()] = 0;
				flowerSeeds[flowerFieldsData.getFlowerIndex()] = seedId;
				flowerTimer[flowerFieldsData.getFlowerIndex()] = Server.getMinutesCounter();
				player.getPA().addSkillXP((int) flowerData.getPlantingXp(), Constants.FARMING);
				container.stop();
			}

			@Override
			public void stop() {
				updateFlowerStates();
				player.stopPlayerSkill = false;
			}
		}, 3);
		return true;
	}

	@SuppressWarnings("unused")
	private void displayAll() {
		for (int i = 0; i < flowerStages.length; i++) {
			System.out.println("index : " + i);
			System.out.println("state : " + flowerState[i]);
			System.out.println("seeds : " + flowerSeeds[i]);
			System.out.println("level : " + flowerStages[i]);
			System.out.println("timer : " + flowerTimer[i]);
			System.out.println("disease chance : " + diseaseChance[i]);
			System.out.println("-----------------------------------------------------------------");
		}
	}

	/* harvesting the plant resulted */

	public boolean harvest(int objectX, int objectY) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Point(objectX, objectY));
		if (flowerFieldsData == null) {
			return false;
		}
		final FlowerData flowerData = FlowerData.forId(flowerSeeds[flowerFieldsData.getFlowerIndex()]);
		if (flowerData == null) {
			return false;
		}
		if (!ItemAssistant.playerHasItem(player, FarmingConstants.SPADE)) {
			Dialogue.sendStatement2(player, new String[] {"You need a spade to harvest here."});
			return true;
		}

		player.startAnimation(FarmingConstants.SPADE_ANIM);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				resetFlowers(flowerFieldsData.getFlowerIndex());
				flowerStages[flowerFieldsData.getFlowerIndex()] = 3;
				flowerTimer[flowerFieldsData.getFlowerIndex()] = Server.getMinutesCounter();
				player.startAnimation(FarmingConstants.SPADE_ANIM);
				player.sendMessage("You harvest the crop, and get some vegetables.");
				ItemAssistant.addItem(player, flowerData.getHarvestId(), flowerData.getHarvestId() == 5099 || flowerData.getHarvestId() == 5100 ? 3 : 1);
				player.getPA().addSkillXP((int) flowerData.getHarvestXp(), Constants.FARMING);
				container.stop();
			}

			@Override
			public void stop() {
				updateFlowerStates();
				player.getPA().resetAnimation();
			}
		}, 2);
		return true;
	}

	/* putting compost onto the plant */

	public boolean putCompost(int objectX, int objectY, final int itemId) {
		if (itemId != 6032 && itemId != 6034) {
			return false;
		}
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Point(objectX, objectY));
		if (flowerFieldsData == null) {
			return false;
		}
		if (flowerStages[flowerFieldsData.getFlowerIndex()] != 3 || flowerState[flowerFieldsData.getFlowerIndex()] == 5) {
			player.sendMessage("This patch doesn't need compost.");
			return true;
		}
		ItemAssistant.deleteItem(player, itemId, 1);
		ItemAssistant.addItem(player, 1925, 1);

		player.sendMessage("You pour some " + (itemId == 6034 ? "super" : "") + "compost on the patch.");
		player.startAnimation(FarmingConstants.PUTTING_COMPOST);
		player.getPA().addSkillXP((int) (itemId == 6034 ? Compost.SUPER_COMPOST_EXP_USE : Compost.COMPOST_EXP_USE), Constants.FARMING);

		player.stopPlayerSkill = true;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				diseaseChance[flowerFieldsData.getFlowerIndex()] *= itemId == 6032 ? COMPOST_CHANCE : SUPERCOMPOST_CHANCE;
				flowerState[flowerFieldsData.getFlowerIndex()] = 5;
				container.stop();
			}

			@Override
			public void stop() {
				player.stopPlayerSkill = false;
				player.getPA().resetAnimation();
			}
		}, 7);
		return true;
	}

	/* inspecting a plant */

	public boolean inspect(int objectX, int objectY) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Point(objectX, objectY));
		if (flowerFieldsData == null) {
			return false;
		}
		final InspectData inspectData = InspectData.forId(flowerSeeds[flowerFieldsData.getFlowerIndex()]);
		final FlowerData flowerData = FlowerData.forId(flowerSeeds[flowerFieldsData.getFlowerIndex()]);
		if (flowerState[flowerFieldsData.getFlowerIndex()] == 2) {
			Dialogue.sendStatement2(player, new String[] {"This plant is diseased. Use a plant cure on it to cure it, ", "or clear the patch with a spade."});
			return true;
		} else if (flowerState[flowerFieldsData.getFlowerIndex()] == 3) {
			Dialogue.sendStatement2(player, new String[] {"This plant is dead. You did not cure it while it was diseased.", "Clear the patch with a spade."});
			return true;
		}
		if (flowerStages[flowerFieldsData.getFlowerIndex()] == 0) {
			Dialogue.sendStatement2(player, new String[] {"This is an flower patch. The soil has not been treated.", "The patch needs weeding."});
		} else if (flowerStages[flowerFieldsData.getFlowerIndex()] == 3) {
			Dialogue.sendStatement2(player, new String[] {"This is an flower patch. The soil has not been treated.", "The patch is empty and weeded."});
		} else if (inspectData != null && flowerData != null) {
			player.sendMessage("You bend down and start to inspect the patch...");

			player.startAnimation(1331);
			player.stopPlayerSkill = true;
			CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					if (flowerStages[flowerFieldsData.getFlowerIndex()] - 4 < inspectData.getMessages().length - 2) {
						Dialogue.sendStatement2(player, inspectData.getMessages()[flowerStages[flowerFieldsData.getFlowerIndex()] - 4]);
					} else if (flowerStages[flowerFieldsData.getFlowerIndex()] < flowerData.getEndingState() - flowerData.getStartingState() + 4) {
						Dialogue.sendStatement2(player, inspectData.getMessages()[inspectData.getMessages().length - 2]);
					} else {
						Dialogue.sendStatement2(player, inspectData.getMessages()[inspectData.getMessages().length - 1]);
					}
					container.stop();
				}

				@Override
				public void stop() {
					player.startAnimation(1332);
					player.stopPlayerSkill = false;
				}
			}, 5);
		}
		return true;
	}

	/* opening the corresponding guide about the patch */

	public boolean guide(int objectX, int objectY) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Point(objectX, objectY));
		if (flowerFieldsData == null) {
			return false;
		}
//		player.getSkillGuide().farmingComplex(6);
//		player.getSkillGuide().selected = 20;
		return true;
	}

	/* Curing the plant */

	public boolean curePlant(int objectX, int objectY, int itemId) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Point(objectX, objectY));
		if (flowerFieldsData == null || itemId != 6036) {
			return false;
		}
		final FlowerData flowerData = FlowerData.forId(flowerSeeds[flowerFieldsData.getFlowerIndex()]);
		if (flowerData == null) {
			return false;
		}
		if (flowerState[flowerFieldsData.getFlowerIndex()] != 2) {
			player.sendMessage("This plant doesn't need to be cured.");
			return true;
		}
		ItemAssistant.deleteItem(player, itemId, 1);
		ItemAssistant.addItem(player, 229, 1);
		player.startAnimation(FarmingConstants.CURING_ANIM);
		flowerState[flowerFieldsData.getFlowerIndex()] = 0;
		player.stopPlayerSkill = true;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.sendMessage("You cure the plant with a plant cure.");
				container.stop();
			}

			@Override
			public void stop() {
				updateFlowerStates();
				player.stopPlayerSkill = false;
				player.getPA().resetAnimation();
			}
		}, 7);

		return true;

	}

	/* Planting scarecrow to push off the birds */

	public boolean plantScareCrow(int objectX, int objectY, int itemId) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Point(objectX, objectY));
		if (flowerFieldsData == null || itemId != SCARECROW) {
			return false;
		}
		if (flowerStages[flowerFieldsData.getFlowerIndex()] != 3) {
			player.sendMessage("You need to clear the patch before planting a scarecrow");
			return false;
		}
		ItemAssistant.deleteItem(player, SCARECROW, 1);
		player.startAnimation(832);
		player.stopPlayerSkill = true;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.sendMessage("You put a scarecrow on the flower patch, and some weeds start to grow around it.");
				flowerSeeds[flowerFieldsData.getFlowerIndex()] = 0x24;
				flowerStages[flowerFieldsData.getFlowerIndex()] = 4;
				flowerTimer[flowerFieldsData.getFlowerIndex()] = Server.getMinutesCounter();
				container.stop();
			}

			@Override
			public void stop() {
				updateFlowerStates();
				player.stopPlayerSkill = false;
				player.getPA().resetAnimation();
			}
		}, 2);
		return true;
	}

	@SuppressWarnings("unused")
	private void resetFlowers() {
		for (int i = 0; i < flowerStages.length; i++) {
			flowerSeeds[i] = 0;
			flowerState[i] = 0;
			diseaseChance[i] = 0;
		}
	}

	/* reseting the patches */

	private void resetFlowers(int index) {
		flowerSeeds[index] = 0;
		flowerState[index] = 0;
		diseaseChance[index] = 1;
	}

	/* checking if the patch is raked */

	public boolean checkIfRaked(int objectX, int objectY) {
		final FlowerFieldsData flowerFieldsData = FlowerFieldsData.forIdPosition(new Point(objectX, objectY));
		if (flowerFieldsData == null)
			return false;
		if (flowerStages[flowerFieldsData.getFlowerIndex()] == 3)
			return true;
		return false;
	}

}
