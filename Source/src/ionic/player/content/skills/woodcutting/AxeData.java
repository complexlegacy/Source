package ionic.player.content.skills.woodcutting;
/**
 * 
 * @author Keith
 *
 */
public enum AxeData {
	BRONZE_AXE(1351, 1, 10, 879, 508, 494),
	IRON_AXE(1349, 1, 9, 877, 510, 496),
	STEEL_AXE(1353, 6, 8, 875, 512, 498),
	BLACK_AXE(1361, 6, 7, 873, 514, 500),
	MITHRIL_AXE(1355, 21, 6, 871, 516, 502),
	ADAMANT_AXE(1357, 31, 5, 869, 518, 504),
	RUNE_AXE(1359, 41, 4, 867, 520, 506),
	DRAGON_AXE(6739, 61, 3, 2846, 6743, 6741);
	public int itemId, levelRequired, bonus, animation, head, broken;

	AxeData(int id, int level, int bonus, int animation, int axeHead, int broken) {
		this.itemId = id;
		this.levelRequired = level;
		this.bonus = bonus;
		this.animation = animation;
		this.head = axeHead;
		this.broken = broken;
	}
	
	public static AxeData forId(int o) {
		for (AxeData t : AxeData.values()) {
			if (t.itemId == o) {
				return t;			
			}
		}
		return null;
	}
	
}
