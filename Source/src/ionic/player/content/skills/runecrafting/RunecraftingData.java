package ionic.player.content.skills.runecrafting;


public enum RunecraftingData {

	AIR(2478, 556, 5, 1, new int[] {11,22,33,44,55,66,77,88,99}),
	MIND(2479, 558, 5.5, 1, new int[] {14,28,42,56,70,84,98}),
	WATER(2480, 555, 6, 5, new int[] {19,38,57,76,95}),
	EARTH(2481, 557, 6.5, 9, new int[] {26,52, 78}),
	FIRE(2482, 554, 7, 14, new int[] {35,70}),
	BODY(2483, 559, 7.5, 20, new int[] {46,92}),
	COSMIC(2484, 564, 8, 27, new int[] {59}),
	CHAOS(2487, 562, 8.5, 35, new int[] {74}),
	ASTRAL(17010, 9075, 8.7, 40, new int[] {82}),
	NATURE(2486, 561, 9, 44, new int[] {91}),
	LAW(2485, 563, 9.5, 54, null),
	DEATH(2488, 565, 10, 65, null),
	BLOOD(2490, 560, 10.5, 77, null),
	SOUL(2489, 566, 11.3, 90, null),
	;

	public int altarId, runeId, levelReq;
	public double xp;
	public int[] multiplier;

	private RunecraftingData(int altarId, int runeId, double xp, int level, int[] multiplier) {
		this.altarId = altarId;
		this.runeId = runeId;
		this.xp = xp;
		this.multiplier = multiplier;
		this.levelReq = level;
	}
	
	public static RunecraftingData forID(int i) {
		for (RunecraftingData f: RunecraftingData.values()) {
			if (f.altarId == i) {
				return f;
			}
		}
		return null;
	}
	
	
}