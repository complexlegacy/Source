package ionic.player.content.skills.summoning;

import core.Constants;
import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Player;
import ionic.player.dialogue.Dialogue;

public class SummoningData {
	
	public static final int SHARDS = 12183;
	public static final int POUCH = 12155;

	public enum pouchData {
		SPIRIT_WOLF(12047, 12231, 7, 1, 12425, 0, 6, 6829, 12158, 2859, 5, 10, 10, 15),
		DREADFOWL(12043, 12225, 8, 4, 12445, 0, 4, 6825, 12158, 2138, 9, 15, 10, 16),
		SPIRIT_SPIDER(12059, 12236, 8, 10, 12428, 0, 15, 6841, 12158, 6291, 13, 10, 15, 18),
		THORNY_SNAIL(12019, 12255, 9, 13, 12459, 0, 16, 6806, 12158, 3363, 13, 20, 10, 28),
		GRANITE_CRAB(12009, 12226, 7, 16, 12533, 0, 18, 6796, 12158, 440, 22, 15, 25, 39),
		SPIRIT_MOSQUITO(12778, 12286, 1, 17, 12838, 1, 12, 7331, 12158, 6319, 47, 25, 20, 43),
		DESERT_WYRM(12049, 12238, 45, 18, 12460, 0, 19, 6831, 12159, 1783, 31, 30, 20, 47),
		SPIRIT_SCORPIAN(12055, 12258, 57, 19, 12432, 1, 17, 6837, 12160, 3095, 83, 30, 30, 67),
		SPIRIT_TZ_KIH(12808, 12290, 64, 22, 12839, 1, 18, 7361, 12160, 12168, 97, 35, 20, 63),
		ALBINO_RAT(12067, 12245, 75, 23, 12430, 2, 23, 6847, 12163, 2134, 202, 30, 30, 68),
		SPIRIT_KALPHITE(12063, 12241, 51, 25, 12446, 2, 22, 6994, 12163, 3138, 220, 30, 35, 77),
		COMPOST_MOUND(12091, 12237, 47, 28, 12440, 1, 24, 6871, 12159, 6032, 50, 45, 45, 95),
		GIANT_CHINCHOMPA(12800, 12276, 84, 29, 12834, 3, 31, 7353, 12163, 9976, 225, 20, 40, 97),
		VAMPIRE_BAT(12053, 12227, 81, 31, 12447, 2, 33, 6835, 12160, 3325, 136, 25, 45, 105),
		HONEY_BADGER(12065, 12246, 84, 32, 12433, 2, 25, 6845, 12160, 12156, 141, 30, 45, 110),
		BEAVER(12021, 12232, 72, 33, 12429, 1, 27, 6808, 12159, 1519, 58, 35, 45, 100),
		VOID_RAVAGER(12818, 12282, 74, 34, 12443, 1, 27, 7370, 12159, 12164, 60, 65, 50, 99),
		VOID_SHIFTER(12814, 12285, 74, 34, 12443, 1, 94, 7367, 12163, 12165, 60, 65, 50, 99),
		VOID_SPINNER(12780, 12283, 74, 34, 12443, 1, 27, 7333, 12163, 12166, 60, 65, 50, 99),
		VOID_TORCHER(12798, 12284, 74, 34, 12443, 1, 94, 7351, 12163, 12167, 60, 65, 50, 99),
		BRONZE_MINOTAUR(12073, 12264, 102, 36, 12461, 0, 30, 6853, 12163, 2349, 317, 75, 50, 133),
		BULL_ANT(12087, 12243, 11, 40, 12431, 0, 30, 6867, 12158, 6010, 53, 58, 58, 154),
		MACAW(12071, 12228, 78, 41, 12422, 1, 31, 6851, 12159, 13572, 72, 58, 58, 1),
		EVIL_TURNIP(12051, 12229, 104, 42, 12448, 2, 30, 6833, 12160, 12153, 185, 62, 60, 162),
		SPIRIT_COCKATRICE(12095, 12266, 88, 43, 12458, 1, 36, 6875, 12159, 12109, 75, 62, 60, 162),
		SPIRIT_GUTHATRICE(12097, 12266, 88, 43, 12458, 1, 36, 6877, 12159, 12111, 75, 62, 60, 162),
		SPIRIT_SARATRICE(12099, 12266, 88, 43, 12458, 1, 36, 6879, 12159, 12113, 75, 62, 60, 162),
		SPIRIT_ZAMATRICE(12101, 12266, 88, 43, 12458, 1, 36, 6881, 12159, 12115, 75, 62, 60, 162),
		SPIRIT_PENGATRICE(12103, 12266, 88, 43, 12458, 1, 36, 6883, 12159, 12117, 75, 62, 60, 162),
		SPIRIT_CORAXATRICE(12105, 12266, 88, 43, 12458, 1, 36, 6885, 12159, 12119, 75, 62, 60, 162),
		SPIRIT_VULATRICE(12107, 12266, 88, 43, 12458, 1, 36, 6887, 12159, 12121, 75, 62, 60, 162),
		IRON_MINOTAUR(12075, 12264, 125, 46, 12462, 5, 37, 6855, 12163, 2351, 405, 70, 65, 193),
		PYRELORD(12816, 12288, 111, 46, 12829, 2, 32, 7377, 12160, 590, 202, 70, 65, 193),
		MAGPIE(12041, 12235, 88, 47, 12426, 6, 34, 6824, 12159, 1635, 83, 1, 1, 1),
		BLOATED_LEECH(12061, 12259, 117, 49, 12444, 2, 34, 6843, 12160, 2132, 215, 75, 70, 211),
		SPIRIT_TERRORBIRD(12007, 12238, 12, 52, 12441, 1, 36, 6794, 12158, 9978, 68, 75, 200, 233),
		ABYSSAL_PARASITE(12035, 12258, 106, 54, 12454, 1, 30, 6818, 12159, 12161, 95, 86, 80, 248),
		SPIRIT_JELLY(12027, 12257, 151, 55, 12453, 6, 43, 6992, 12163, 1937, 484, 88, 85, 255),
		STEEL_MINOTAUR(12077, 12264, 141, 56, 12463, 6, 46, 6857, 12163, 2353, 493, 90, 90, 260),
		IBIS(12531, 12233, 109, 56, 12424, 1, 38, 6991, 12159, 311, 99, 1, 1, 1),
		SPIRIT_GRAAHK(12810, 12275, 154, 57, 12835, 6, 49, 7363, 12163, 10099, 502, 100, 90, 268),
		SPIRIT_KYATT(12812, 12274, 153, 57, 12836, 6, 49, 7365, 12163, 10103, 502, 100, 90, 268),
		SPIRIT_LARUPIA(12784, 12289, 155, 57, 12840, 6, 49, 7337, 12163, 10095, 502, 100, 90, 268),
		KARAMTHULHU_OVERLORD(12023, 12267, 144, 58, 12455, 6, 44, 6809, 12163, 6667, 510, 110, 110, 276),
		SMOKE_DEVIL(12085, 12239, 141, 61, 12468, 3, 48, 6865, 12160, 9736, 268, 120, 110, 300),
		ABYSSAL_LURKER(12037, 12234, 119, 62, 12427, 0, 41, 6820, 12159, 12161, 110, 80, 110, 308),
		SPIRIT_COBRA(12015, 12254, 116, 63, 12436, 3, 56, 6802, 12160, 6287, 277, 105, 110, 314),
		STRANGER_PLANT(12045, 12265, 128, 64, 12467, 3, 49, 6827, 12160, 8431, 282, 105, 100, 322),
		MITHRIL_MINOTAUR(12079, 12264, 152, 66, 12464, 7, 55, 6859, 12163, 2359, 107, 112, 112, 340),
		BARKER_TOAD(12123, 12252, 11, 66, 12452, 1, 8, 6889, 12158, 2150, 87, 100, 100, 340),
		WAR_TORTOISE(12031, 12253, 1, 67, 12439, 1, 43, 6815, 12158, 7939, 59, 88, 87, 348),
		BUNYIP(12029, 12262, 110, 68, 12438, 1, 44, 6813, 12159, 383, 119, 75, 72, 400),
		FRUIT_BAT(12033, 12230, 130, 69, 12423, 1, 45, 6817, 12159, 1963, 121, 1, 1, 1),
		RAVENOUS_LOCUST(12820, 12244, 79, 70, 12830, 2, 24, 7372, 12160, 1933, 132, 133, 134, 375),
		ARTIC_BEAR(12057, 12249, 14, 71, 12451, 1, 28, 6839, 12158, 10117, 93, 136, 136, 381),
		PHEONIX(14623, 14625, 165, 72, 14622, 8, 30, 8548, 12160, 14616, 301, 139, 139, 395),
		OBSIDIAN_GOLEM(12792, 12279, 195, 73, 12826, 7, 55, 7345, 12163, 12168, 642, 141, 141, 406),
		GRANITE_LOBSTER(12069, 12242, 166, 74, 12449, 4, 47, 6849, 12159, 6979, 326, 141, 142, 418),
		PRAYING_MANTIS(12011, 12247, 168, 75, 12450, 4, 69, 6798, 12160, 2462, 330, 145, 145, 428),
		FORGE_REGENT(12782, 12291, 141, 76, 12449, 2, 45, 10020, 12159, 10020, 134, 146, 146, 430),
		ADAMANT_MINOTAUR(12081, 12264, 144, 76, 12465, 8, 66, 6861, 12163, 2361, 669, 146, 148, 441),
		TALON_BEAST(12794, 12261, 174, 77, 12831, 11, 49, 7347, 12160, 12795, 1015, 151, 160, 454),
		GIANT_ENT(12013, 12269, 124, 78, 12457, 2, 49, 6800, 12159, 5933, 137, 165, 140, 467),
		FIRE_TITAN(12802, 12271, 198, 79, 12824, 8, 62, 7355, 12163, 1442, 695, 152, 152, 476),
		MOSS_TITAN(12804, 12272, 202, 79, 12824, 8, 79, 7357, 12163, 1440, 695, 152, 152, 476),
		ICE_TITAN(12806, 12273, 198, 79, 12824, 8, 64, 7359, 12163, 1444, 695, 152, 152, 476),
		HYDRA(12025, 12263, 128, 80, 12442, 2, 19, 9488, 12159, 571, 141, 155, 155, 485),
		SPIRIT_DAGANNOTH(12017, 12260, 1, 83, 12456, 4, 57, 6804, 12160, 6255, 365, 162, 161, 528),
		LAVA_TITAN(12788, 12277, 219, 83, 12837, 8, 61, 7341, 12163, 12168, 730, 162, 161, 528),
		SWAMP_TITAN(12776, 12270, 150, 85, 12832, 4, 56, 7329, 12160, 10149, 374, 166, 161, 556),
		RUNE_MINOTAUR(12083, 12264, 1, 86, 12466, 9, 51, 6863, 12163, 2363, 757, 170, 165, 570),
		UNICORN_STALLION(12039, 12250, 140, 88, 12434, 2, 54, 6822, 12159, 237, 154, 88, 88, 100),
		GEYSER_TITAN(12786, 12280, 222, 89, 12833, 9, 69, 7339, 12163, 1444, 783, 185, 180, 620),
		WOLPERTINGER(12089, 12268, 203, 92, 12437, 5, 62, 6869, 12160, 2859, 404, 190, 190, 651),
		ABYSSAL_TITAN(12796, 12281, 112, 93, 12827, 2, 32, 7349, 12159, 12161, 163, 200, 200, 667),
		IRON_TITAN(12822, 12287, 198, 95, 12828, 5, 60, 7375, 12160, 1115, 418, 205, 205, 694),
		PACK_YACK(12093, 12251, 211, 96, 12435, 5, 58, 6873, 12160, 10818, 422, 200, 350, 710),
		STEEL_TITAN(12790, 12278, 178, 99, 12825, 5, 64, 7343, 12160, 1119, 435, 210, 400, 754),
		;
		public final int pouch, cantCreate, shards, levelReq, scroll, summonXp, 
		time, npcId, charm, secondIngredient, infuseXp, attack, defence, hitPoints;
		public double drainRate = 0.0;
		private pouchData(int pouch, int cantCreate, int shards, int levelReq, int scroll, 
				int summonXp, int time, int npcId, int charm, int secondIngredient, int infuseXp,
				int attack, int defence, int hitpoints) {
			this.cantCreate = cantCreate;
			this.pouch = pouch;
			this.shards = shards;
			this.levelReq = levelReq;
			this.scroll = scroll;
			this.summonXp = summonXp;
			this.time = time;
			this.npcId = npcId;
			this.charm = charm;
			this.secondIngredient = secondIngredient;
			this.infuseXp = infuseXp;
			this.attack = attack;
			this.defence = defence;
			this.hitPoints = hitpoints;
		}

		public boolean hasLevelReq(Player c) {
			if (c.getPA().getLevelForXP(c.playerXP[Constants.SUMMONING]) >= this.levelReq) {
				return true;
			}
			return false;
		}
		
		public boolean canCreate(Player c) {
			return hasLevelReq(c) 
					&& ItemAssistant.playerHasItem(c, secondIngredient)
					&& ItemAssistant.playerHasItem(c, SHARDS, shards)
					&& ItemAssistant.playerHasItem(c, charm)
					&& ItemAssistant.playerHasItem(c, POUCH);
		}

		
		public void sendRequirements(Player c) {
			Dialogue.sendStatement3(c, new String[] {
					"Requirements: -A pouch,", 
					"-Level "+levelReq+" Summoning,",
					"-"+shards+"x Spirit Shards,",
					"-1x "+ItemData.data[charm].getName()+",",
					"-1x "+ItemData.data[secondIngredient].getName()+""});
		}
		
		public String getName() {
			return textFix(name().toLowerCase().replaceAll("_", " "));
		}
	}
	
	public static void setDrainRates() {
		double drain = 0.1;
		for (pouchData k : pouchData.values()) {
			drain += 0.0013;
			k.drainRate = drain;
		}
	}
	
	public static pouchData getByName(String s) {
		for (pouchData k : pouchData.values()) {
			if (k.name().equals(s)) {
				return k;
			}
		}
		return null;
	}
	
	public static pouchData getByNpc(int npc) {
		for (pouchData k : pouchData.values()) {
			if (k.npcId == npc || k.npcId == npc + 1) {
				return k;
			}
		}
		return null;
	}

	public static pouchData forID(int pouch) {
		for (pouchData k : pouchData.values()) {
			if (k.pouch == pouch) {
				return k;
			}
		}
		return null;
	}
	
	public static pouchData pouchFromSlot(int slot) {
		int s = 0;
		for (pouchData k : pouchData.values()) {
			if (s == slot) {
				return k;
			}
			s++;
		}
		return null;
	}


	public enum scrollData {
		HOWL_SCROLL(12425, 12377, pouchData.SPIRIT_WOLF),
		DREADFOWL_STRIKE_SCROLL(12445, 12293, pouchData.DREADFOWL),
		EGG_SPAWN_SCROLL(12428, 12353, pouchData.SPIRIT_SPIDER),
		SLIME_SPRAY_SCROLL(12459, 12325, pouchData.THORNY_SNAIL),
		STONY_SHELL_SCROLL(12533, 12355, pouchData.GRANITE_CRAB),
		PESTER_SCROLL(12838, 12409, pouchData.SPIRIT_MOSQUITO),
		ELECTRIC_LASH_SCROLL(12460, 12381, pouchData.DESERT_WYRM),
		VENOM_SHOT_SCROLL(12432, 12357, pouchData.SPIRIT_SCORPIAN),
		FIREBALL_ASSAULT_SCROLL(12839, 12415, pouchData.SPIRIT_TZ_KIH),
		CHEESE_FEAST_SCROLL(12430, 12351, pouchData.ALBINO_RAT),
		SANDSTORM_SCROLL(12446, 12327, pouchData.SPIRIT_KALPHITE),
		GENERATE_COMPOST_SCROLL(12440, 12313, pouchData.COMPOST_MOUND),
		EXPLODE_SCROLL(12834, 12395, pouchData.GIANT_CHINCHOMPA),
		VAMPYRE_TOUCH_SCROLL(12447, 12375, pouchData.VAMPIRE_BAT),
		INSANE_FEROCITY_SCROLL(12433, 12321, pouchData.HONEY_BADGER),
		MULTICHOP_SCROLL(12429, 12303, pouchData.BEAVER),
		CALL_TO_ARMS_SCROLL(12443, 12407, pouchData.VOID_RAVAGER),
		CALL_TO_ARMS_SCROLL2(12443, 12407, pouchData.VOID_SHIFTER),
		CALL_TO_ARMS_SCROLL3(12443, 12407, pouchData.VOID_SPINNER),
		CALL_TO_ARMS_SCROLL4(12443, 12407, pouchData.VOID_TORCHER),
		BRONZE_BULL_RUSH_SCROLL(12461, 12341, pouchData.BRONZE_MINOTAUR),
		UNBURDEN_SCROLL(12431, 12301, pouchData.BULL_ANT),
		HERBCALL_SCROLL(12422, 12335, pouchData.MACAW),
		EVIL_FLAMES_SCROLL(12448, 12371, pouchData.EVIL_TURNIP),
		PETRIFYING_GAZE_SCROLL(12458, 12311, pouchData.SPIRIT_COCKATRICE),
		PETRIFYING_GAZE_SCROLL2(12458, 12311, pouchData.SPIRIT_GUTHATRICE),
		PETRIFYING_GAZE_SCROLL3(12458, 12311, pouchData.SPIRIT_SARATRICE),
		PETRIFYING_GAZE_SCROLL4(12458, 12311, pouchData.SPIRIT_ZAMATRICE),
		PETRIFYING_GAZE_SCROLL5(12458, 12311, pouchData.SPIRIT_PENGATRICE),
		PETRIFYING_GAZE_SCROLL6(12458, 12311, pouchData.SPIRIT_CORAXATRICE),
		PETRIFYING_GAZE_SCROLL7(12458, 12311, pouchData.SPIRIT_VULATRICE),
		IRON_BULL_RUSH_SCROLL(12462, 12341, pouchData.IRON_MINOTAUR),
		IMMENSE_HEAT_SCROLL(12829, 12411, pouchData.PYRELORD),
		THIEVING_FINGERS_SCROLL(12426, 12337, pouchData.MAGPIE),
		BLOOD_DRAIN_SCROLL(12444, 12347, pouchData.BLOATED_LEECH),
		TIRELESS_RUN_SCROLL(12441, 12345, pouchData.SPIRIT_TERRORBIRD),
		ABYSSAL_DRAIN_SCROLL(12454, 12299, pouchData.ABYSSAL_PARASITE),
		DISSOLVE_SCROLL(12453, 12325, pouchData.SPIRIT_JELLY),
		STEEL_BULL_RUSH_SCROLL(12463, 12341, pouchData.STEEL_MINOTAUR),
		FISH_RAIN_SCROLL(12424, 12363, pouchData.IBIS),
		GOAD_SCROLL(12835, 12393, pouchData.SPIRIT_GRAAHK),
		AMBUSH_SCROLL(12836, 12391, pouchData.SPIRIT_KYATT),
		RENDING_SCROLL(12840, 12419, pouchData.SPIRIT_LARUPIA),
		DOOMSPHERE_SCROLL(12455, 12329, pouchData.KARAMTHULHU_OVERLORD),
		DUST_CLOUD_SCROLL(12468, 12317, pouchData.SMOKE_DEVIL),
		ABYSSAL_STEALTH_SCROLL(12427, 12297, pouchData.ABYSSAL_LURKER),
		OPH_INCUBATION_SCROLL(12436, 12361, pouchData.SPIRIT_COBRA),
		POISONOUS_BLAST_SCROLL(12467, 12365, pouchData.STRANGER_PLANT),
		MITH_BULL_RUSH_SCROLL(12464, 12341, pouchData.MITHRIL_MINOTAUR),
		TOAD_BARK_SCROLL(12452, 12367, pouchData.BARKER_TOAD),
		TESTUDO_SCROLL(12439, 12369, pouchData.WAR_TORTOISE),
		SWALLOW_WHOLE_SCROLL(12438, 12309, pouchData.BUNYIP),
		FRUITFALL_SCROLL(12423, 12319, pouchData.FRUIT_BAT),
		FAMINE_SCROLL(12830, 12333, pouchData.RAVENOUS_LOCUST),
		ARCTIC_BLAST_SCROLL(12451, 12349, pouchData.ARTIC_BEAR),
		RISE_FROM_THE_ASHES_SCROLL(14622, 14620, pouchData.PHEONIX),
		VOLCANIC_STR_SCROLL(12826, 12401, pouchData.OBSIDIAN_GOLEM),
		CRUSHING_CLAW_SCROLL(12449, 12331, pouchData.GRANITE_LOBSTER),
		MANTIS_STRIKE_SCROLL(12450, 12339, pouchData.PRAYING_MANTIS),
		CRUSHING_CLAW_SCROLL2(12449, 12331, pouchData.FORGE_REGENT),
		ADDY_BULL_RUSH_SCROLL(12465, 12341, pouchData.ADAMANT_MINOTAUR),
		DEADLY_CLAW_SCROLL(12831, 12343, pouchData.TALON_BEAST),
		ACORN_MISSILE_SCROLL(12457, 12385, pouchData.GIANT_ENT),
		TITANS_CON_SCROLL(12824, 12389, pouchData.FIRE_TITAN),
		TITANS_CON_SCROLL2(12824, 12389, pouchData.MOSS_TITAN),
		TITANS_CON_SCROLL3(12824, 12389, pouchData.ICE_TITAN),
		REGROWTH_SCROLL(12442, 12323, pouchData.HYDRA),
		SPIKE_SHOT_SCROLL(12456, 12315, pouchData.SPIRIT_DAGANNOTH),
		EBON_THUNDER_SCROLL(12837, 12397, pouchData.LAVA_TITAN),
		SWAMP_PLAGUE_SCROLL(12832, 12387, pouchData.SWAMP_TITAN),
		RUNE_BULL_RUSH_SCROLL(12466, 12341, pouchData.RUNE_MINOTAUR),
		HEALING_AURA_SCROLL(12434, 12373, pouchData.UNICORN_STALLION),
		BOIL_SCROLL(12833, 12405, pouchData.GEYSER_TITAN),
		MAGIC_FOCUS_SCROLL(12437, 12379, pouchData.WOLPERTINGER),
		ESSENCE_SHIPMENT_SCROLL(12827, 12403, pouchData.ABYSSAL_TITAN),
		IRON_WITHIN_SCROLL(12828, 12413, pouchData.IRON_TITAN),
		WINTER_STORAGE_SCROLL(12435, 12383, pouchData.PACK_YACK),
		STEEL_OF_LEGENDS_SCROLL(12825, 12399, pouchData.STEEL_TITAN),
		;
		public int scroll, cantCreate;
		public pouchData pouch;
		private scrollData(int scroll, int cantCreate, pouchData pouch) {
			this.scroll = scroll;
			this.cantCreate = cantCreate;
			this.pouch = pouch;
		}
		
		public void sendRequirements(Player c) {
			Dialogue.sendStatement3(c, new String[] {"Requirements:", "-1x "+ItemData.data[pouch.pouch].getName()+""});
		}
		
		public String getName() {
			String s = this.name();
			s = s.toLowerCase();
			s = s.replaceAll("_", " ");
			s = s.replaceAll("2", "");
			s = s.replaceAll("3", "");
			s = s.replaceAll("4", "");
			s = s.replaceAll("5", "");
			s = s.replaceAll("6", "");
			s = s.replaceAll("7", "");
			s = s.replaceAll("scroll", "");
			return textFix(s);
		}
		
	}
	
	public static String textFix(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
	
	public static scrollData scrollByPouch(pouchData p) {
		for (scrollData s : scrollData.values()) {
			if (s.pouch == p) {
				return s;
			}
		}
		return null;
	}
	
	public static scrollData getScrollBySlot(int slot) {
		int f = 0;
		for (scrollData j: scrollData.values()) {
			if (f == slot) {
				return j;
			}
			f++;
		}
		return null;
	}


	public static void openPouchCreation(Player c) {
		String s = "";
		int i = 0;
		for (pouchData p : pouchData.values()) {
			if (p.hasLevelReq(c)) {
				s += "# "+i+" "+(p.pouch + 1)+" "+p.shards+" ";
			} else {
				s += "# "+i+" "+(p.cantCreate + 1)+" "+p.shards+" ";
			}
			i++;
		}
		c.getPA().sendFrame126(s, 39799);
		c.getPA().showInterface(39700);
	}


	public static void openScrollCreation(Player c) {
		String s = "";
		int i = 0;
		for (scrollData p : scrollData.values()) {
			if (ItemAssistant.playerHasItem(c, p.pouch.pouch)) {
				s += "# "+i+" "+(p.scroll + 1)+" "+(p.pouch.pouch + 1)+" ";
			} else {
				s += "# "+i+" "+(p.cantCreate + 1)+" "+(p.pouch.pouch + 1)+" ";
			}
			i++;
		}
		c.getPA().sendFrame126(s, 38799);
		c.getPA().showInterface(38700);
	}	
	
	
	
	public static int familiarAttackEmotes(int npc) {
		npc -= 1;
		switch(npc) {
		case 6825:
			return 7810;
		case 6806:
			return 8143;
		case 6841:
			return 8165;
		case 6796:
			return 8107;
		case 7331:
			return 8032;
		case 6831:
			return 7795;
		case 7361:
			return 8257;
		case 6847:
			return 7905;
		case 6837:
			return 6261;
		case 6994:
			return 6223;
		case 6871:
			return 7769;
		case 7353:
			return 7755;
		case 6835:
			return 8275;
		case 6845:
			return 7928;
		case 7370:
			return 8086;
		case 7367:
			return 8131;
		case 7351:
			return 8235;
		case 7333:
			return 8172;
		case 6853:
		case 6855:
		case 6857:
		case 6859:
		case 6861:
		case 6863:
			return 8024;
		case 6867:
			return 7896;
		case 6851:
			return 8010;
		case 6833:
			return 8248;
		case 7377:
			return 8080;
		case 6824:
			return 8010;
		case 6843:
			return 7657;
		case 6818:
			return 7672;
		case 6992:
			return 8569;
		case 6991:
			return 8200;
		case 6875:
			// the cocks
		case 6877:
		case 6879:
		case 6881:
		case 6883:
		case 6885:
		case 6887:
			return 7762;
		case 7363:
		case 7365:
		case 7337:
			return 7913;
		case 6809:
			return 7970;
		case 6865:
			return 7816;
		case 6820:
			return 7680;
		case 6802:
			return 8152;
		case 6827:
			return 8211;
		case 6889:
			return 7700;
		case 6815:
			return 8286;
		case 7372:
			return 7994;
		case 6839:
			return 8524;
		case 8575:
			return 11093;
		case 7345:
			return 8050;
		case 6849:
			return 8112;
		case 6798:
			return 8064;
		case 7335:
			return 7866;
		case 7347:
			return 5989;
		case 6800:
			return 7853;
		case 6811:
			return 7935;
		case 6804:
			return 7786;
		case 7341:
			return 7980;
		case 7329:
			return 8222;

		case 6873:
			return 5782;
		case 3101:
			// Melee
			return 10058;
		case 3102:
			// Range
		case 3103:
			// Mage
			return 10057;
		case 6822:
			return 6376;
		case 7339:
			return 7879;
		case 6869:
			return 8303;
		case 7349:
			return 7694;
		case 7343:
		case 7375:
			return 7844; 
		}
		return 0;
	}
	


}
