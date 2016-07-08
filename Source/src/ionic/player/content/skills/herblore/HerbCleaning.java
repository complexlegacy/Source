package ionic.player.content.skills.herblore;

public enum HerbCleaning {

	GUAM(199, 249, 1, 3),
	MARRENTILL(201, 251, 5, 4),
	TARROMIN(203, 253, 11, 5),
	HARRALANDER(205, 255, 20, 6),
	RANARR(207, 257, 25, 8),
	TOADFLAX(3049, 2998, 30, 8),
	SPIRITWEED(12174, 12172, 35, 8),
	IRIT(209, 259, 40, 9),
	WERGALI(14836, 14854, 30, 8),
	AVANTOE(211, 261, 48, 10),
	KWUARM(213, 263, 54, 11),
	SNAPDRAGON(3051, 3000, 59, 12),
	CADANTINE(215, 265, 65, 13),
	LANTADYME(2485, 2481, 67, 13),
	DWARFWEED(217, 267, 70, 14),
	TORSTOL(219, 269, 75, 15);
	
	public int dirty, clean, xp, lvlReq;
	private HerbCleaning(int grimy, int cleaned, int levelReq, int exp) {
		this.dirty = grimy;
		this.clean = cleaned;
		this.xp = exp;
		this.lvlReq = levelReq;
	}
	
	public static HerbCleaning forID(int d) {
		for (HerbCleaning f : HerbCleaning.values()) {
			if (f.dirty == d) {
				return f;
			}
		}
		return null;
	}
	
}
