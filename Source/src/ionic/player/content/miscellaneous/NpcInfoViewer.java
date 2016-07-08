package ionic.player.content.miscellaneous;

import ionic.item.ItemData;
import ionic.npc.NPCData;
import ionic.npc.NPCHandler;
import ionic.npc.drops.Drop;
import ionic.npc.drops.DropType;
import ionic.player.Player;

public class NpcInfoViewer {
	
	private static MonsterType NORMAL = MonsterType.NORMAL;
	private static MonsterType BOSS = MonsterType.BOSS;
	private static MonsterType SLAYER = MonsterType.SLAYER;
	
	public enum MonsterType {
		NORMAL,
		BOSS,
		SLAYER
	}

	public enum MonsterData {
		KING_BLACK_DRAGON(50, "", true, BOSS),
		NEX(13447, "", true, BOSS),
		GENERAL_GRAARDOR(6260, "", true, BOSS),
		CHAOS_ELEMENTAL(3200, "", true, BOSS),
		DAGANNOTH_SUPREME(2881, "", true, BOSS),
		DAGANNOTH_REX(2883, "", true, BOSS),
		DAGANNOTH_PRIME(2882, "", true, BOSS),
		CORPOREAL_BEAST(8133, "", true, BOSS),
		TORMENTED_DEMON(8349, "", true, BOSS),
		GIANT_MOLE(3340, "", true, BOSS),
		AVATAR_OF_CREATION(8597, "", true, BOSS),
		AVATAR_OF_DESTRUCTION(8596, "", true, BOSS),
		NOMAD(8607, "", true, BOSS),
		BARRELCHEST(5666, "", true, BOSS),
		MUTATED_JADINKO_GUARD(13821, "", true, NORMAL),
		MUTATED_JADINKO_MALE(13822, "", true, NORMAL),
		MUTATED_JADINKO_BABY(13820, "", true, NORMAL),
		ABYSSAL_DEMON(1615, "", true, SLAYER),
		DARK_BEAST(2783, "", true, SLAYER),
		WYVERN(3068, "", true, SLAYER),
		ICE_STRYKEWYRM(9463, "", true, NORMAL),
		DESERT_STRYKEWYRM(9465, "", true, NORMAL),
		JUNGLE_STRYKEWYRM(9476, "", true, NORMAL),
		MOSS_GIANT(4688, "", true, NORMAL),
		ICE_GIANT(4685, "", true, NORMAL),
		BABY_RED_DRAGON(1589, "", true, NORMAL),
		BRONZE_DRAGON(1590, "", true, SLAYER),
		IRON_DRAGON(1591, "", true, SLAYER),
		STEEL_DRAGON(1592, "", true, SLAYER),
		MITHIRIL_DRAGON(5363, "", true, SLAYER),
		BABY_BLUE_DRAGON(52, "", true, NORMAL),
		CHAOS_DWARF(119, "", true, NORMAL),
		BLUE_DRAGON(4681, "", true, SLAYER),
		ABYSSAL_WALKER(2265, "", true, NORMAL),
		CAVE_HORROR(4353, "", true, SLAYER),
		DUST_DEVIL(1264, "", true, SLAYER),
		GREEN_DRAGON(941, "", true, SLAYER),
		CYCLOPS(116, "", true, SLAYER),
		REVENANT_IMP(13465, "", true, NORMAL),
		REVENANT_GOBLIN(13466, "", true, NORMAL),
		REVENANT_ICEFIEND(13470, "", true, NORMAL),
		REVENANT_PYREFIEND(13471, "", true, NORMAL),
		REVENANT_HOBGOBLIN(13472, "", true, NORMAL),
		REVENANT_VAMPYRE(13473, "", true, NORMAL),
		REVENANT_WEREWOLF(13474, "", true, NORMAL),
		REVENANT_CYCLOPS(13475, "", true, NORMAL),
		REVENANT_HELLHOUND(13476, "", true, NORMAL),
		REVENANT_DEMON(13477, "", true, NORMAL),
		REVENANT_ORK(13478, "", true, NORMAL),
		REVENANT_DARK_BEAST(13480, "", true, NORMAL),
		REVENANT_DRAGON(13481, "", true, NORMAL),
		GLACIES(13454, "", true, NORMAL),
		FUMUS(13451, "", true, NORMAL),
		UMBRA(13452, "", true, NORMAL),
		CRUOR(13453, "", true, NORMAL),
		ICEFIEND(3406, "", true, SLAYER),
		RED_DRAGON(53, "", true, SLAYER),
		PHOENIX(8549, "", true, BOSS),
		FROST_DRAGON(51, "", true, BOSS),
		BORK(7133, "", true, BOSS),
		BLACK_DEMON(84, "", true, SLAYER),
		GIANT_ROCK_CRAB(2452, "", true, SLAYER),
		DAGANNOTH_GUARDIAN(9092, "", true, SLAYER),
		LESSER_DEMON(82, "", true, SLAYER),
		KREE_ARRA(6222, "", true, BOSS),
		COMMANDER_ZILYANA(6247, "", true, BOSS),
		K_RIL_TSUTSAROTH(6203, "", true, BOSS),
		WILDYWYRM(3334, "", true, BOSS),
		GLACOR(14301, "", true, BOSS),
		STARLIGHT(6248, "", true, NORMAL),
		GROWLER(6250, "", true, NORMAL),
		BREE(6252, "", true, NORMAL),
		BALFRUG_KREEYATH(6208, "", true, NORMAL),
		TSTANON_KARLAK(6204, "", true, NORMAL),
		ZAKL_N_GRITCH(6206, "", true, NORMAL),
		WINGMAN_SKREE(6223, "", true, NORMAL),
		FLOCKLEADER_GEERIN(6225, "", true, NORMAL),
		FLIGHT_KILISA(6227, "", true, NORMAL),
		SERGEANT_STRONGSTACK(6261, "", true, NORMAL),
		SERGEANT_STEELWILL(6263, "", true, NORMAL),
		SERGEANT_GRIMSPIKE(6265, "", true, NORMAL),
		OGRESS(7081, "", true, SLAYER),
		OGRESS_WARRIOR(7079, "", true, SLAYER),
		ORK(6271, "", true, SLAYER),
		ROCK_CRAB(222, "", false, NORMAL)
		;
		public int npc = 0;
		public String loc = "";
		public boolean shows;
		public MonsterType type;
		private MonsterData(int npcId, String location, boolean onInterface, MonsterType type) {
			this.npc = npcId;
			this.loc = location;
			this.shows = onInterface;
			this.type = type;
		}
		public static MonsterData forID(int id) {
			for (MonsterData t: MonsterData.values()) {
				if (t.npc == id) {
					return t;
				}
			}
			return null;
		}
		public static MonsterData getFromSlot(int slot) {
			int current = 0;
			for (MonsterData g : MonsterData.values()) {
				if (current == slot) {
					return g;
				}
				current++;
			}
			return null;
		}
		public static int getSlot(MonsterData m) {
			int current = 0;
			for (MonsterData g : MonsterData.values()) {
				if (g == m) {
					return current;
				}
				current++;
			}
			return 0;
		}
		public Drop getDrop() {
			return Drop.npcDrops[npc];
		}
		public int getHealth() {
			return NPCData.data[npc].hitPoints;
		}
		public int getMaxHit() {
			return NPCData.data[npc].maxHit;
		}
	}


	/**
	 * Sends the drops to the player of the selected npc
	 */
	public static void sendDrops(Player c, Drop d) {
		if (d != null) {
			//String s = ""+d.bones+"=1_";
			String s = "";
			for (int i = 0; i < d.raritys.length; i++) {
				if (always(d.raritys[i])) {
					s += ""+d.drops[i]+"="+d.amounts[i]+"_";
				}
			}
			s += "`";
			for (int i = 0; i < d.raritys.length; i++) {
				if (common(d.raritys[i])) {
					s += ""+d.drops[i]+"="+d.amounts[i]+"_";
				}
			}
			s += "`";
			for (int i = 0; i < d.raritys.length; i++) {
				if (uncommon(d.raritys[i])) {
					s += ""+d.drops[i]+"="+d.amounts[i]+"_";
				}
			}
			s += "`";
			for (int i = 0; i < d.raritys.length; i++) {
				if (rare(d.raritys[i])) {
					s += ""+d.drops[i]+"="+d.amounts[i]+"_";
				}
			}
			s += "`";
			for (int i = 0; i < d.raritys.length; i++) {
				if (veryRare(d.raritys[i]) && !brawlingGloves(d.drops[i])) {
					s += ""+d.drops[i]+"="+d.amounts[i]+"_";
				}
			}
			c.getPA().sendFrame126(s, 35220);
		} else {
			c.getPA().sendFrame126("", 35220);
		}
	}
	
	public static boolean brawlingGloves(int i) {
		return ItemData.data[i].getName().toLowerCase().startsWith("brawling");
	}


	public static boolean clickButton(Player c, int button) {
		int button2 = -1;
		if (button >= 136204 && button <= 136255) {
			button2 = button - 136204;
		} else if (button >= 137000 && button <= 137031) {
			button2 = button - 137000 + 52;
		}
		if (button2 != -1) {
			MonsterData m = MonsterData.getFromSlot(button2);
			c.getPA().sendFrame126("Health: "+m.getHealth(), 35214);
			c.getPA().sendFrame126("Max Hit: "+m.getMaxHit(), 35215);
			c.getPA().sendFrame126("Location:\\n"+m.loc, 35216);
			c.getPA().sendFrame126("Total times killed: "+c.killLogs[MonsterData.getSlot(m)], 35217);
			sendDrops(c, m.getDrop());
			return true;
		}
		return false;
	}


	public static void sendMonsters(Player c) {
		String s = "";
		for (MonsterData i : MonsterData.values()) {
			if (i.shows) {
				s += ""+i.npc+"`";
			}
		}
		c.getPA().sendFrame126(s, 35011);
	}



	public static boolean always(DropType d) {
		return d == DropType.ALWAYS;
	}
	public static boolean common(DropType d) {
		return ((d == DropType.HALF_ALWAYS) || (d == DropType.COMMON)
				|| (d == DropType.EXTREMELY_COMMON) || (d == DropType.VERY_COMMON)
				|| (d == DropType.LESS_COMMON));
	}
	public static boolean uncommon(DropType d) {
		return ((d == DropType.UNCOMMON) || (d == DropType.MORE_UNCOMMON)
				|| (d == DropType.VERY_UNCOMMON) || (d == DropType.EXTREMELY_UNCOMMON));
	}
	public static boolean rare(DropType d) {
		return ((d == DropType.RARE) || (d == DropType.VERY_RARE));
	}
	public static boolean veryRare(DropType d) {
		return ((d == DropType.SUPER_RARE) || (d == DropType.EXTREMELY_RARE)
				|| (d == DropType.ALMOST_NEVER));
	}

}
