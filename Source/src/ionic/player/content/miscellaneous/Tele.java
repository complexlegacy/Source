package ionic.player.content.miscellaneous;

public enum Tele {
	
	HOME(3087, 3500, 0),
	FARMING_CATHERBY(2790, 3465, 0),
	EDGEVILLE(3087, 3500, 0),
	VARROCK(3210, 3424, 0),
	FALADOR(2964, 3378, 0),
	LUMBRIDGE(3222, 3218, 0),
	CAMELOT(2757, 3477, 0),
	WEST_ARDOUNGE(2662, 3305, 0),
	EAST_ARGOUNGE(2529, 3307, 0),
	AL_KHARID(3293, 3174, 0),
	ANCIENT_PYRAMID(3233, 2902, 0),
	YANILLE(2606, 3093, 0),
	BURTHORPE(2926, 3559, 0),
	BARBARIAN_VILLAGE(3082, 3420, 0),
	ENTRANA(2834, 3335, 0),
	CATHERBY(2813, 3447, 0),
	SEERS_VILLAGE(2708, 3492, 0),
	DUEL_ARENA(3311, 3234, 0),
	CANIFIS(3506, 3496, 0),
	PORT_SARIM(3023, 3208, 0),
	DRAYNOR_VILLAGE(3093, 3244, 0),
	RIMMINGTON(2957, 3214, 0),
	KARAMJA(2948, 3147, 0),
	NEX(2925, 5203, 0),
	GODWARS(2882, 5310, 2),
	CORP(2885, 4373, 0),
	PURO_PURO(2592, 4317, 0),
	ZANARIS(2420, 4446, 0),
	GIANT_MOLE(1760, 5163, 0),
	JASTIZO(2405, 3782, 0),
	NETIZNOT(2310, 3782, 0),
	ANCIENT_CAVERN(1774, 5365, 0),
	PEST_CONTROL(2662, 2650, 0),
	BARROWS(3565, 3312, 0),
	POH(1884, 5106, 0),
	MISCELLANIA(2513, 3860, 0),
	ETCETERIA(2605, 3875, 0),
	SORCERESS_GARDEN(2912, 5474, 0),
	ROGUES_DEN(3054, 4981, 1),
	SUMMONING_CAVE(2206, 5346, 0),
	KELDAGRIM(2837, 10209, 0),
	BLAST_FURNACE(1940, 4958, 0),
	KALPHITE_QUEEN(3487, 9493, 0),
	TORMENTED_DEMONS(2533, 5805, 0),
	WARRIORS_GUILD(2868, 3546, 0),
	FIGHT_CAVES(2439, 5169, 0),
	FIGHT_PITS(2399, 5177, 0),
	CASTLE_WARS(2443, 3090, 0),
	SOUL_WARS(1886, 3178, 0),
	EXPERIMENTS(3555, 9947, 0),
	BARBARIAN_COURSE(2552, 3558, 0),
	WILDERNESS_COURSE(2998, 3916, 0),
	GNOME_COURSE(2469, 3435, 0),
	LIVING_ROCK_CAVERN(3651, 5122, 0),
	CHAOS_TUNNELS(3145, 5555, 0),
	CATHERBY_FISH(2852, 3433, 0),
	GLACORS(4204, 5712, 0),
	ABYSS(3039, 4834, 0),
	REVENANT_CAVE(3122, 10129, 0),
	NOMAD(3361, 5857, 0),
	ROCK_CRABS(2673, 3709, 0),
	
	;
	
	public int x, y, h;
	private Tele(int xLocation, int yLocation, int height) {
		this.x = xLocation;
		this.y = yLocation;
		this.h = height;
	}
	
	public static Tele forID(String s) {
		for (Tele i : Tele.values()) {
			if (i.name().toLowerCase().startsWith(s)) {
				return i;
			}
		}
		return null;
	}

}
