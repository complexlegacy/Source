package ionic.player.content.skills.mining;

public enum MiningAxeData {
	BRONZE(1265, 1, 625),
	IRON(1267, 1, 626),
	STEEL(1269, 6, 627),
	MITHRIL(1273, 21, 629),
	ADAMANT(1271, 31, 628),
	RUNE(1275, 41, 624),
	DRAGON(15259, 61, 12188);
	
	public int item, level, anim;
	private MiningAxeData(int item, int level, int anim) {
		this.item = item;
		this.level = level;
		this.anim = anim;
	}
	
	public static MiningAxeData forID(int pick) {
		for (MiningAxeData m : MiningAxeData.values()) {
			if (m.item == pick) {
				return m;
			}
		}
		return null;
	}
	
}
