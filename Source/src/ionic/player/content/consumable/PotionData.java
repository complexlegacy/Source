package ionic.player.content.consumable;

import ionic.player.Player;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;

/**
 * @author Keith
 */
public enum PotionData {

	OVERLOAD(15332, 15333, 15334, 15335) {
		@Override
		public void function(Player c) {
			c.overloads += 5;
			c.overloaded = true;
			c.overloadTime = System.currentTimeMillis();
			if (!c.overloadEffect) { c.drankOverload(); }
			int[] toIncrease = {0, 1, 2, 4, 6};
			for (int i = 0; i < toIncrease.length; i++) {
				boostStat(c, toIncrease[i], 3);
			}
		}
	},
	
	SARADOMIN_BREW(6685, 6687, 6689, 6691) {
		@Override
		public void function(Player c) {
			saraBrew(c);
		}
	},
	
	ENERGY_POTION(3008, 3010, 3012, 3014) {
		@Override
		public void function(Player c) {
			c.runEnergy += 10.0;
			if (c.runEnergy > 100) c.runEnergy = 100.0;
		}
	},
	
	SUPER_ATTACK(2436, 145, 147, 149) {
		@Override
		public void function(Player c) {
			boostStat(c, 0, 2);
		}
	},
	
	SUPER_STRENGTH(2440, 157, 159, 161) {
		@Override
		public void function(Player c) {
			boostStat(c, 2, 2);
		}
	},
	
	SUPER_DEFENCE(2442, 163, 165, 167) {
		@Override
		public void function(Player c) {
			boostStat(c, 1, 2);
		}
	},
	
	RANGE_POTION(2444, 169, 171, 172) {
		@Override
		public void function(Player c) {
			boostStat(c, 4, 1);
		}
	},
	
	DEFENCE_POTION(2432, 133, 135, 137) {
		@Override
		public void function(Player c) {
			boostStat(c, 1, 1);
		}
	},
	
	STRENGTH_POTION(113, 115, 117, 119) {
		@Override
		public void function(Player c) {
			boostStat(c, 2, 1);
		}
	},
	
	ATTACK_POTION(2428, 121, 123, 125) {
		@Override
		public void function(Player c) {
			boostStat(c, 0, 1);
		}
	},
	
	SUPER_RESTORE(3024, 3026, 3028, 3030) {
		@Override
		public void function(Player c) {
			c.skillLevel[5] += (c.getLevelForXP(c.playerXP[5]) * .30);
			c.skillLevel[5] += 1;
			if (c.skillLevel[5] > c.getLevelForXP(c.playerXP[5]))
				c.skillLevel[5] = c.getLevelForXP(c.playerXP[5]);
			c.getPA().refreshSkill(5);
			restoreStats(c);
		}
	},
	
	PRAYER_POTION(2434, 139, 141, 143) {
		@Override
		public void function(Player c) {
			c.skillLevel[5] += (c.getLevelForXP(c.playerXP[5]) * .33);
			if (c.skillLevel[5] > c.getLevelForXP(c.playerXP[5]))
				c.skillLevel[5] = c.getLevelForXP(c.playerXP[5]);
			c.getPA().refreshSkill(5);
		}
	},
	
	ANTIPOISON(2446, 175, 177, 179) {
		@Override
		public void function(Player c) {
			curePoison(c, 30000);
		}
	},
	
	SUPER_ANTIPOISON(2448, 181, 183, 185) {
		@Override
		public void function(Player c) {
			curePoison(c, 300000);
		}
	},
	
	SUPER_ENERGY(3016, 3018, 3020, 3022) {
		@Override
		public void function(Player c) {
			c.runEnergy += 25.0;
			if (c.runEnergy > 100) c.runEnergy = 100.0;
		}
	},
	
	MAGIC_POTION(3040, 3042, 3044, 3046) {
		@Override
		public void function(Player c) {
			boostStat(c, 6, 1);
		}
	},
	
	EXTREME_STRENGTH(15312, 15313, 15314, 15315) {
		@Override
		public void function(Player c) {
			boostStat(c, 2, 3);
		}
	},
	
	EXTREME_ATTACK(15308, 15309, 15310, 15311) {
		@Override
		public void function(Player c) {
			boostStat(c, 0, 3);
		}
	},
	
	EXTREME_DEFENCE(15316, 15317, 15318, 15319) {
		@Override
		public void function(Player c) {
			boostStat(c, 1, 3);
		}
	},
	
	EXTREME_RANGE(15324, 15325, 15326, 15327) {
		@Override
		public void function(Player c) {
			boostStat(c, 4, 3);
		}
	},
	

	
	PRAYER_FLASK(22323, 22325, 22327, 22329, 22331, 22333) {
		@Override
		public void function(Player c) {
			c.skillLevel[5] += (c.getLevelForXP(c.playerXP[5]) * .33);
			if (c.skillLevel[5] > c.getLevelForXP(c.playerXP[5]))
				c.skillLevel[5] = c.getLevelForXP(c.playerXP[5]);
			c.getPA().refreshSkill(5);
		}
	},
	
	SUPER_ATTACK_FLASK(22335, 22337, 22339, 22341, 22343, 22345) {
		@Override
		public void function(Player c) {
			boostStat(c, 0, 2);
		}
	},
	
	SUPER_STRENGTH_FLASK(22347, 22349, 22351, 22353, 22355, 22357) {
		@Override
		public void function(Player c) {
			boostStat(c, 2, 2);
		}
	},
	
	SUPER_DEFENCE_FLASK(22359, 22361, 22363, 22365, 22367, 22369) {
		@Override
		public void function(Player c) {
			boostStat(c, 1, 2);
		}
	},
	
	RANGING_FLASK(22371, 22373, 22375, 22379, 22381, 22383) {
		@Override
		public void function(Player c) {
			boostStat(c, 4, 2);
		}
	},
	
	SUPER_ANTIPOISON_FLASK(22385, 22387, 22389, 22391, 22393, 22395) {
		@Override
		public void function(Player c) {
			curePoison(c, 300000);
		}
	},
	
	SARADOMIN_BREW_FLASK(22397, 22399, 22401, 22403, 22405, 22407) {
		@Override
		public void function(Player c) {
			saraBrew(c);
		}
	},
	
	SUPER_RESTORE_FLASK(22409, 22411, 22413, 22415, 22417, 22419) {
		@Override
		public void function(Player c) {
			c.skillLevel[5] += (c.getLevelForXP(c.playerXP[5]) * .30);
			c.skillLevel[5] += 1;
			if (c.skillLevel[5] > c.getLevelForXP(c.playerXP[5]))
				c.skillLevel[5] = c.getLevelForXP(c.playerXP[5]);
			c.getPA().refreshSkill(5);
			restoreStats(c);
		}
	},
	
	MAGIC_FLASK(22421, 22423, 22425, 22427, 22429, 22431) {
		@Override
		public void function(Player c) {
			boostStat(c, 6, 2);
		}
	},
	
	EXTREME_ATTACK_FLASK(22445, 22447, 22449, 22451, 22453, 22455) {
		@Override
		public void function(Player c) {
			boostStat(c, 0, 3);
		}
	},
	
	EXTREME_STRENGTH_FLASK(22459, 22461, 22463, 22465, 22467, 22469) {
		@Override
		public void function(Player c) {
			boostStat(c, 2, 3);
		}
	},
	
	EXTREME_DEFENCE_FLASK(22471, 22473, 22475, 22477, 22479, 22481) {
		@Override
		public void function(Player c) {
			boostStat(c, 1, 3);
		}
	},
	
	EXTREME_RANGE_FLASK(22495, 22497, 22499, 22501, 22503, 22505) {
		@Override
		public void function(Player c) {
			boostStat(c, 4, 3);
		}
	},
	
	EXTREME_MAGIC_FLASK(22483, 22485, 22487, 22489, 22491, 22493) {
		@Override
		public void function(Player c) {
			boostStat(c, 6, 3);
		}
	},
	
	SUPER_PRAYER_FLASK(22507, 22509, 22511, 22513, 22515, 22517) {
		@Override
		public void function(Player c) {
			c.skillLevel[5] += (c.getLevelForXP(c.playerXP[5]) * .44);
			if (c.skillLevel[5] > c.getLevelForXP(c.playerXP[5]))
				c.skillLevel[5] = c.getLevelForXP(c.playerXP[5]);
			c.getPA().refreshSkill(5);
		}
	},
	
	OVERLOAD_FLASK(22519, 22521, 22523, 22525, 22527, 22529) {
		@Override
		public void function(Player c) {
			c.overloads += 5;
			c.overloaded = true;
			c.overloadTime = System.currentTimeMillis();
			if (!c.overloadEffect) { c.drankOverload(); }
			int[] toIncrease = {0, 1, 2, 4, 6};
			for (int i = 0; i < toIncrease.length; i++) {
				boostStat(c, toIncrease[i], 3);
			}
		}
	},
	
	PRAYER_RENEWAL(21630, 21632, 21634, 21636) {
		@Override
		public void function(Player c) {
			startPrayerRenewal(c, false);
		}
	},
	PRAYER_RENEWAL_FLASK(22531, 22533, 22535, 22537, 22539, 22541) {
		@Override
		public void function(Player c) {
			startPrayerRenewal(c, false);
		}
	},
	
	
	RECOVER_SPECIAL(15300, 15302, 15304, 15306) {
		@Override
		public void function(Player c) {
		}
	},
	RECOVER_SPECIAL_FLASK(22433, 22435, 22437, 22439, 22441, 22443) {
		@Override
		public void function(Player c) {
		}
	},
	
	;

	public int six, five, four, three, two, one, stat, power;
	public boolean flask = false;
	public abstract void function(Player c);

	/* Use this for 4 dose potions */
	private PotionData(int fourDoses, int threeDoses, int twoDoses, int oneDose) {
		this.four = fourDoses;
		this.three = threeDoses;
		this.two = twoDoses;
		this.one = oneDose;
		this.flask = false;
	}

	/* Use this for 6 dose potions (Flasks) */
	private PotionData(int sixDoses, int fiveDoses, int fourDoses, int threeDoses, 
			int twoDoses, int oneDose) {
		this.six = sixDoses;
		this.five = fiveDoses;
		this.four = fourDoses;
		this.three = threeDoses;
		this.two = twoDoses;
		this.one = oneDose;
		this.flask = true;
	}

	public static PotionData findPotion(int itemId) {
		for (PotionData data : PotionData.values()) {
			if (data.one == itemId || data.two == itemId || data.three == itemId || data.four == itemId
					|| data.five == itemId || data.six == itemId) {
				return data;
			}
		}
		return null;
	}


	public static void startPrayerRenewal(Player c, boolean j) {
		int amount = c.getLevelForXP(c.playerXP[5]) > 74 ? 62 : 50;
		if (c.prayerRenewal > 0) {
			amount = c.prayerRenewal;
		}
		prayerRenewal(c, amount, j);
	}

	public static void prayerRenewal(Player c, int amt, boolean justLogged) {
		boolean on = false;
		if (c.prayerRenewal > 0)
			on = true;
		if (justLogged)
			on = false;
		c.prayerRenewal = amt;
		int level = c.getLevelForXP(c.playerXP[5]);
		boolean fast = level >= 75;
		if (!on) {
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					c.prayerRenewal --;
					if (c.prayerRenewal == 0) {
						container.stop();
					}
					c.gfx0(1305);
					c.skillLevel[5] += 1;
					if (c.skillLevel[5] > level)
						c.skillLevel[5] = level;
					c.getPA().refreshSkill(5);
				}
				@Override
				public void stop() {
					c.sendMessage("The effects from your prayer renewal potion have worn off.");
				}
			}, fast ? 8 : 10);
		}
	}


	public static int getBrewStat(Player c, int skill, double amount) {
		return (int)(c.getLevelForXP(c.playerXP[skill]) * amount);
	}

	public static void curePoison(Player c, long delay) {
		c.poisonImmune = delay;
		c.lastPoisonSip = System.currentTimeMillis();
	}


	public static void boostStat(Player c, int skill, int potionType) {
		c.skillLevel[skill] += getBoostedStat(c, skill, potionType);
		c.getPA().refreshSkill(skill);
	}

	/** level 1 = regular pot, level 2 = super pot, level 3 = extreme pot **/
	public static int getBoostedStat(Player c, int skill, int potionType) {
		int increaseBy = 0;
		if (potionType == 1)
			increaseBy = (int)((skill == 3 ? c.maximumHitPoints() : c.getLevelForXP(c.playerXP[skill])) * .20) + 1;
		if (potionType == 2)
			increaseBy = (int)((skill == 3 ? c.maximumHitPoints() : c.getLevelForXP(c.playerXP[skill])) * .20);
		if (potionType == 3)
			increaseBy = (int)((skill == 3 ? c.maximumHitPoints() : c.getLevelForXP(c.playerXP[skill])) * .27);
		if (c.skillLevel[skill] + increaseBy > (skill == 3 ? c.maximumHitPoints() : c.getLevelForXP(c.playerXP[skill])) + increaseBy + 1) {
			return (skill == 3 ? c.maximumHitPoints() : c.getLevelForXP(c.playerXP[skill])) + increaseBy - c.skillLevel[skill];
		}
		return increaseBy;
	}

	public static void restoreStats(Player c) {
		for (int j = 0; j <= 24; j++) {
			if (j == 0 || j == 1 || j == 2 || j == 4 || j == 24) {
				if (c.skillLevel[j] < c.getLevelForXP(c.playerXP[j])) {
					c.skillLevel[j] += (c.getLevelForXP(c.playerXP[j]) * .33);
					if (c.skillLevel[j] > c.getLevelForXP(c.playerXP[j]))  {
						c.skillLevel[j] = c.getLevelForXP(c.playerXP[j]);
					}
					c.getPA().refreshSkill(j);
					c.getPA().setSkillLevel(j, c.skillLevel[j], c.playerXP[j]);
				}
			}
		}
	}


	public static void saraBrew(Player c) {
		int[] toDecrease = { 0, 2, 4, 6 };
		for (int tD: toDecrease) {
			if (!c.overloadEffect) {
				c.skillLevel[tD] -= getBrewStat(c, tD, .10);
				if (c.skillLevel[tD] < 0)
					c.skillLevel[tD] = 1;
				c.getPA().refreshSkill(tD);
				c.getPA().setSkillLevel(tD, c.skillLevel[tD], c.playerXP[tD]);
			}
		}
		c.skillLevel[1] += getBrewStat(c, 1, .20);
		if (c.skillLevel[1] > (c.getLevelForXP(c.playerXP[1]) * 1.2 + 1)) {
			c.skillLevel[1] = (int)(c.getLevelForXP(c.playerXP[1]) * 1.2);
		}
		c.getPA().refreshSkill(1);
		c.skillLevel[3] += getBrewStat(c, 3, .15);
		if (c.skillLevel[3] > (c.maximumHitPoints() * 1.17 + 1)) {
			c.skillLevel[3] = (int)(c.maximumHitPoints() * 1.17);
		}
		c.getPA().refreshSkill(3);
	}









}
