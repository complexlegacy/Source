package ionic.player.content.skills.fishing;


import core.Constants;
import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.content.skills.Skill;
import ionic.player.dialogue.Dialogue;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import utility.Misc;

public class Fishing {


	public enum Pool {
		LOBSTER_TUNA_SWORD(Fish.LOBSTER, Fish.TUNA, Fish.SWORDFISH, Equipment.LOBSTER_POT, Equipment.HARPOON),
		NET_AND_BAIT(Fish.LOBSTER, Fish.TUNA, Fish.SWORDFISH, Equipment.LOBSTER_POT, Equipment.HARPOON),
		TROUT_PIKE(Fish.TROUT, Fish.PIKE, null, Equipment.FLY_FISHING_ROD, Equipment.FISHING_ROD),
		SHARK_BASS_COD(Fish.SHARK, Fish.BASS, Fish.COD, Equipment.HARPOON, Equipment.BIG_FISHING_NET),
		SHARK_MONKFISH(Fish.SHARK, Fish.MONKFISH, null, Equipment.HARPOON, Equipment.SMALL_FISHING_NET)
		;

		private Fish fish1, fish2, fish3;
		private Equipment equipment1, equipment2;

		private Pool(Fish fish1_1, Fish fish2_1, Fish fish2_2, Equipment equipment1, Equipment equipment2) {
			this.fish1 = fish1_1;
			this.fish2 = fish2_1;
			this.fish3 = fish2_2;
			this.equipment1 = equipment1;
			this.equipment2 = equipment2;
		}

		public Equipment getEquipment1() {
			return equipment1;
		}

		public Equipment getEquipment2() {
			return equipment2;
		}
	}

	public enum Equipment {
		HARPOON(311, 618),
		LOBSTER_POT(301, 619),
		BIG_FISHING_NET(305, 620),
		SMALL_FISHING_NET(303, 621),
		FLY_FISHING_ROD(309, 622),
		FISHING_ROD(307, 622);

		private int id, animation;

		private Equipment(int id, int animation) {
			this.id = id;
			this.animation = animation;
		}

		public int getId() {
			return id;
		}

		public int getAnimation() {
			return animation;
		}
	}

	public enum Fish {
		SHRIMP(1, 10, 317, -1),
		ANCHOVIES(15, 40, 319, -1),
		SARDINE(5, 20, 327, 313),
		HERRING(10, 30, 345, 313),
		MONKFISH(62, 120, 7944, -1),
		TROUT(20, 50, 335, 314),
		BASS(46, 100, 363, -1),
		COD(23, 45, 341, -1),
		PIKE(25, 60, 349, 313),
		TUNA(35, 80, 359, -1),
		SWORDFISH(50, 100, 371, -1),
		LOBSTER(40, 90, 377, -1),
		MACKEREL(16, 20, 353, -1),
		SHARK(76, 110, 383, -1),
		MANTA_RAY(81, 46, 389, -1)
		;

		private int level, fish, bait;
		private double exp;

		private Fish(int level, double exp, int fish, int bait) {
			this.level = level;
			this.exp = exp;
			this.fish = fish;
			this.bait = bait;
		}

		public int getFish() {
			return fish;
		}

		public int getLevel() {
			return level;
		}

		public double getExp() {
			return exp;
		}

		public int getBait() {
			return bait;
		}
	}

	public static boolean canFish(Pool pool, int clickType, int playerLevel, int playerEq, int playerBait, int playerBaitN) {
		Fish fish = clickType == 1 ? pool.fish1 : pool.fish2;
		Equipment equipment = clickType == 1 ? pool.getEquipment1() : pool.getEquipment2();

		if (fish.getLevel() > playerLevel) {
			System.out.println("You don't meet the fishing requirement to use this spot.");
			System.out.println("You need a fishing level of " + fish.getLevel() + " to fish here.");
			return false;
		}

		if (equipment.getId() != playerEq) {
			System.out.println("You need a " + ItemAssistant.getItemName(equipment.getId()) + " to fish here.");
			return false;
		}

		if ((fish.getBait() > 0 && fish.getBait() != playerBait) || playerBaitN == 0) {
			System.out.println("You don't have any " + ItemAssistant.getItemName(fish.getBait()) + " left!");
			System.out.println("You need " + ItemAssistant.getItemName(fish.getBait()) + " to fish here.");
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws InterruptedException {
		int playerLevel = 86;
		int playerEq = Equipment.FLY_FISHING_ROD.getId();
		int playerBait = 314;
		int playerBaitN = 10;
		int clickType = 1;
		Pool pool = Pool.TROUT_PIKE;

		if (!canFish(pool, clickType, playerLevel, playerEq, playerBait, playerBaitN)) {
			System.exit(0);
			return;
		}

		System.out.println("You begin to fish.");

		int inv = 0;

		while (inv++ != 28) {
			if (!canFish(pool, clickType, playerLevel, playerEq, playerBait, playerBaitN)) {
				System.exit(0);
				return;
			}

			boolean thirdFish = pool.fish3 != null && playerLevel > pool.fish3.getLevel() && Math.random() >= 0.60;
			Fish caught = clickType == 1 ? pool.fish1 : thirdFish  ? pool.fish3 : pool.fish2;

			if (caught.getBait() > 0) {
				playerBaitN--;
			}

			System.out.println("You catch a " + ItemAssistant.getItemName(caught.getFish()) + ".");
//			Thread.sleep(600);
		}
		System.out.println("inv full");
		System.exit(0);
	}

	private final static int[][] data = {
			{ 1, 1, 303, -1, 317, 17, 621, 321, 15, 30, Misc.random(0) + 4 }, // SHRIMP + ANCHOVIES
			{ 2, 5, 307, 313, 327, 33, 622, 345, 10, 30, Misc.random(1) + 4 }, // SARDINE + HERRING
			{ 3, 62, 303, -1, 7944, 133, 621, -1, -1, -1, Misc.random(2) + 4 }, // MONKFISH
			{ 4, 20, 309, 314, 335, 63, 622, 331, 30, 70, Misc.random(2) + 4 }, // TROUT
			{ 5, 23, 305, -1, 341, 58, 620, 363, 46, 100, Misc.random(2) + 4 }, // BASS + COD
			{ 6, 25, 307, 313, 349, 81, 622, -1, -1, -1, Misc.random(2) + 4 }, // PIKE
			{ 7, 35, 311, -1, 359, 96, 618, 371, 50, 100, Misc.random(3) + 4 }, // TUNA + SWORDFISH
			{ 8, 40, 301, -1, 377, 103, 619, -1, -1, -1, Misc.random(3) + 4 }, // LOBSTER
			{ 9, 16, 303, -1, 353, 38, 620, -1, -1, -1, Misc.random(2) + 4 }, // MACKEREL
			{ 10, 76, 311, -1, 383, 168, 618, -1, -1, -1, Misc.random(4) + 4 }, // SHARK
			{ 11, 79, 303, -1, 395, 197, 619, -1, -1, -1, Misc.random(5) + 4 }, // SEATURTLE
			{ 12, 81, 303, -1, 389, 178, 621, 15270, 90, 200, Misc.random(5) + 4 }, // MANTA RAY
	};

	private static void attemptdata(final Client c, int npcId) {
		if (c.fishingProp[4] > 0) {
			c.playerIsFishing = false;
			c.fishingProp[4] = -1;
			return;
		}
		if (c.stopPlayerSkill) {
			return;
		}
		if (ItemAssistant.freeSlots(c) == 0) {
			return;
		}
		//c.events++;
		for (int[] aData : data) {
			if (npcId == aData[0]) {
				if (c.skillLevel[Constants.FISHING] < aData[1]) {
					c.sendMessage("You don't meet the fishing requirement to use this spot.");
					Dialogue.sendStatement2(c, new String[] {"You need a fishing level of \" + aData[1] + \" to fish here."});
					return;
				}
				if (aData[3] > 0) {
					if (!ItemAssistant.playerHasItem(c, aData[3])) {
						c.sendMessage("You don't have any " + ItemAssistant.getItemName(aData[3]) + "!");
						return;
					}
				}
				c.fishingProp[0] = aData[6]; // ANIM
				c.fishingProp[1] = aData[4]; // FISH
				c.fishingProp[2] = aData[5]; // XP
				c.fishingProp[3] = aData[3]; // BAIT
				c.fishingProp[4] = aData[2]; // EQUIP
				c.fishingProp[5] = aData[7]; // sFish
				c.fishingProp[6] = aData[8]; // sLvl
				c.fishingProp[7] = aData[4]; // FISH
				c.fishingProp[8] = aData[9]; // sXP
				c.fishingProp[9] = Misc.random(1) == 0 ? 7 : 5;
				c.fishingProp[10] = aData[0]; // INDEX
				if (!hasFishingEquipment(c, c.fishingProp[4])) {
					return;
				}

				c.sendMessage("You begin fishing");
				c.startAnimation(c.fishingProp[0]);
				c.stopPlayerSkill = true;

				if (c.playerIsFishing) {
					return;
				}

				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (!c.stopPlayerSkill) {
							container.stop();
							return;
						}
						if (c.fishingProp[3] > 0) {
							ItemAssistant.deleteItem(c, c.fishingProp[3], ItemAssistant.getItemSlot(c, c.fishingProp[3]), 1);
							if (!ItemAssistant.playerHasItem(c, c.fishingProp[3])) {
								c.sendMessage("You don't have any " + ItemAssistant.getItemName(c.fishingProp[3]) + " left!");
								c.sendMessage("You need " + ItemAssistant.getItemName(c.fishingProp[3]) + " to fish here.");
								container.stop();
								return;
							}
						}
						/*if (c.events > 1) {
							container.stop();
							return;
						}*/
						if (!hasFishingEquipment(c, c.fishingProp[4])) {
							container.stop();
							return;
						}
						if (ItemAssistant.freeSlots(c) == 0) {
							container.stop();
							return;
						}
						if (Math.random() > 1 - 1 / 50.0) {
							c.sendMessage("A fish spits at you, and you stop fishing.");
							container.stop();
							return;
						}
						if (c.fishingProp[5] > 0) {
							if (c.skillLevel[Constants.FISHING] >= c.fishingProp[6]) {
								c.fishingProp[1] = c.fishingProp[Misc.random(1) == 0 ? 7 : 5];
							}
						}

						if (c.fishingProp[1] > 0) {
							ItemAssistant.addItem(c, c.fishingProp[1], 1);
							c.sendMessage("You catch a " + ItemAssistant.getItemName(c.fishingProp[1]) + ".");
							c.startAnimation(c.fishingProp[0]);
						}
						if (c.fishingProp[2] > 0) {
							c.getPA().addSkillXP(c.fishingProp[2], Constants.FISHING);
						}
					}

					@Override
					public void stop() {
						resetFishing(c);
						//c.events = 0;
					}
				}, 7);
			}
		}
	}

	private static boolean hasFishingEquipment(Client c, int equipment) {
		if (!ItemAssistant.playerHasItem(c, equipment)) {
			if (equipment == 311) {
				if (!ItemAssistant.playerHasItem(c, 311) && !ItemAssistant.playerHasItem(c, 10129) && c.playerEquipment[3] != 10129) {
					c.sendMessage("You need a " + ItemAssistant.getItemName(equipment) + " to fish here.");
					return false;
				}
			} else {
				c.sendMessage("You need a " + ItemAssistant.getItemName(equipment) + " to fish here.");
				return false;
			}
		}
		return true;
	}

	public static void resetFishing(Client c) {
		c.fishTimer = 0;
		c.startAnimation(65535);
		c.stopPlayerSkill = false;
		c.fishingProp[4] = -1;
	}

	public static void fishingNPC(Client c, int i, int l) {
		switch (i) {
			case 1:
				switch (l) {
					case 319:
					case 329:
					case 323:
					case 325:
					case 327:
					case 330:
					case 332:
					case 316: // NET + BAIT (shrimp/anchovies
						Fishing.attemptdata(c, 1);
						break;
					case 334:
					case 313: // NET + HARPOON (monkfish)
						Fishing.attemptdata(c, 3);
						break;
					case 322: // NET + HARPOON (bass/cod)
						Fishing.attemptdata(c, 5);
						break;

					case 309: // LURE
					case 310:
					case 311:
					case 314:
					case 315:
					case 317:
					case 318:
					case 328:
					case 331:
						Fishing.attemptdata(c, 4);
						break;

					case 312:
					case 321:
					case 324: // CAGE + HARPOON (lobster)
						Fishing.attemptdata(c, 8);
						break;
					case 326: // Manta Rays
						Fishing.attemptdata(c, 12);
						break;
				}
				break;
			case 2:
				switch (l) {
					case 327:
					case 330:
					case 332:
					case 316: // BAIT + NET
						Fishing.attemptdata(c, 2);
						break;
					case 319:
					case 323:
					case 325: // BAIT + NET
						Fishing.attemptdata(c, 9);
						break;
					case 310:
					case 311:
					case 314:
					case 315:
					case 317:
					case 318:
					case 328:
					case 329:
					case 331:
					case 309: // BAIT + LURE
						Fishing.attemptdata(c, 6);
						break;
					case 312:
					case 321:
					case 324:// SWORDIES+TUNA-CAGE+HARPOON
						Fishing.attemptdata(c, 7);
						break;
					case 313:
					case 322:
					case 334: // NET+HARPOON
						Fishing.attemptdata(c, 10);
						break;
					case 326: // SEA TURTLE
						Fishing.attemptdata(c, 11);
						break;
				}
				break;
		}
	}

	public static boolean fishingNPC(int npc) {
		for (int i = 308; i < 335; i++) {
			if (npc == i) {
				return true;
			}
		}
		return false;
	}
}