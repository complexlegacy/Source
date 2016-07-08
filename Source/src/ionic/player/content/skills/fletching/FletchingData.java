package ionic.player.content.skills.fletching;

/**
 * @author Keith
 */

public enum FletchingData {
	
	/** Unstrung bows **/
	NORMAL_SHORTBOW_U(1, 1, 50, 1511, 946, 5, 1, 5, 1248),
	NORMAL_LONGBOW_U(1, 2, 48, 1511, 946, 10, 1, 10, 1248),
	ARROW_SHAFTS(1, 3, 52, 1511, 946, 1, 15, 15, 1248),
	OAK_SHORTBOW_U(2, 1, 54, 1521, 946, 20, 1, 20, 1248),
	OAK_LONGBOW_U(2, 2, 56, 1521, 946, 25, 1, 25, 1248),
	OAK_CBOW_STOCK(2, 3, 9442, 1521, 946, 25, 1, 25, 1248),
	WILLOW_SHORTBOW_U(3, 1, 60, 1519, 946, 35, 1, 35, 1248),
	WILLOW_LONGBOW_U(3, 2, 58, 1519, 946, 40, 1, 40, 1248),
	WILLOW_CBOW_STOCK(3, 3, 9444, 1519, 946, 40, 1, 25, 1248),
	MAPLE_SHORTBOW_U(4, 1, 64, 1517, 946, 50, 1, 50, 1248),
	MAPLE_LONGBOW_U(4, 2, 62, 1517, 946, 55, 1, 55, 1248),
	MAPLE_CBOW_STOCK(4, 3, 9448, 1517, 946, 55, 1, 55, 1248),
	YEW_SHORTBOW_U(5, 1, 68, 1515, 946, 65, 1, 65, 1248),
	YEW_LONGBOW_U(5, 2, 66, 1515, 946, 70, 1, 70, 1248),
	YEW_CBOW_STOCK(5, 3, 9452, 1515, 946, 70, 1, 75, 1248),
	MAGIC_SHORTBOW_U(6, 1, 72, 1513, 946, 80, 1, 80, 1248),
	MAGIC_LONGBOW_U(6, 2, 70, 1513, 946, 85, 1, 87, 1248),
	/** Strung bows **/
	NORMAL_SHORTBOW(7, 1, 841, 50, 1777, 5, 1, 5, 6678),
	NORMAL_LONGBOW(8, 1, 839, 48, 1777, 10, 1, 10, 6684),
	OAK_SHORTBOW(9, 1, 843, 54, 1777, 20, 1, 20, 6679),
	OAK_LONGBOW(10, 1, 845, 56, 1777, 25, 1, 25, 6685),
	WILLOW_SHORTBOW(11, 1, 849, 60, 1777, 35, 1, 35, 6680),
	WILLOW_LONGBOW(12, 1, 847, 58, 1777, 40, 1, 40, 6686),
	MAPLE_SHORTBOW(13, 1, 853, 64, 1777, 50, 1, 50, 6681),
	MAPLE_LONGBOW(14, 1, 851, 62, 1777, 55, 1, 55, 6687),
	YEW_SHORTBOW(15, 1, 857, 68, 1777, 65, 1, 65, 6682),
	YEW_LONGBOW(16, 1, 855, 66, 1777, 70, 1, 70, 6688),
	MAGIC_SHORTBOW(17, 1, 861, 72, 1777, 80, 1, 80, 6683),
	MAGIC_LONGBOW(18, 1, 859, 70, 1777, 85, 1, 87, 6689),
	/** Arrows **/
	HEADLESS_ARROWS(26, 1, 53, 314, 52, 1, 15, 15, -1),
	BRONZE_ARROWS(19, 1, 882, 39, 53, 1, 15, 40, -1),
	IRON_ARROWS(20, 1, 884, 40, 53, 15, 15, 58, -1),
	STEEL_ARROWS(21, 1, 886, 41, 53, 30, 15, 95, -1),
	MITHRIL_ARROWS(22, 1, 888, 42, 53, 45, 15, 132, -1),
	ADAMANT_ARROWS(23, 1, 890, 43, 53, 60, 15, 170, -1),
	RUNE_ARROWS(24, 1, 892, 44, 53, 75, 15, 207, -1),
	DRAGON_ARROWS(25, 1, 11212, 11237, 53, 90, 15, 300, -1),
	/** Unstrung Crossbows **/
	BRONZE_CBOW_U(27, 1, 9454, 9442, 9420, 15, 1, 40, 4436),
	IRON_CBOW_U(28, 1, 9457, 9442, 9423, 25, 1, 80, 4438),
	STEEL_CBOW_U(29, 1, 9459, 9444, 9425, 40, 1, 120, 4439),
	MITHRIL_CBOW_U(30, 1, 9461, 9448, 9427, 55, 1, 140, 4440),
	ADAMANT_CBOW_U(31, 1, 9463, 9452, 9429, 70, 1, 180, 4441),
	RUNE_CBOW_U(32, 1, 9465, 9452, 9431, 80, 1, 230, 4442),
	/** Finishing Crossbows **/
	BRONZE_CBOW(33, 1, 9174, 9454, 9438, 15, 1, 50, 6671),
	IRON_CBOW(34, 1, 9177, 9457, 9438, 25, 1, 85, 6673),
	STEEL_CBOW(35, 1, 9179, 9459, 9438, 40, 1, 125, 6674),
	MITHRIL_CBOW(36, 1, 9181, 9461, 9438, 55, 1, 150, 6675),
	ADAMANT_CBOW(37, 1, 9183, 9463, 9438, 70, 1, 200, 6676),
	RUNE_CBOW(38, 1, 9185, 9465, 9438, 80, 1, 245, 6677),
	/** Finishing bolts **/
	BRONZE_BOLTS(39, 1, 877, 9375, 314, 5, 15, 40, -1),
	IRON_BOLTS(40, 1, 9140, 9377, 314, 20, 15, 80, -1),
	STEEL_BOLTS(41, 1, 9141, 9378, 314, 35, 15, 130, -1),
	MITHRIL_BOLTS(42, 1, 9142, 9379, 314, 50, 15, 160, -1),
	ADAMANT_BOLTS(43, 1, 9143, 9380, 314, 60, 15, 200, -1),
	RUNE_BOLTS(44, 1, 9144, 9381, 314, 75, 15, 240, -1),
	/** Throwing Darts **/
	BRONZE_DARTS(46, 1, 806, 819, 314, 10, 15, 40, -1),
	IRON_DARTS(47, 1, 807, 820, 314, 25, 15, 80, -1),
	STEEL_DARTS(48, 1, 808, 821, 314, 40, 15, 130, -1),
	MITHRIL_DARTS(49, 1, 809, 822, 314, 50, 15, 160, -1),
	ADAMANT_DARTS(50, 1, 810, 823, 314, 65, 15, 200, -1),
	RUNE_DARTS(51, 1, 811, 824, 314, 75, 15, 240, -1),
	DRAGON_DARTS(52, 1, 11230, 11232, 314, 90, 15, 320, -1),
	;
	
	public int identifier, slot, make, used, used2, levelReq, makeAmt, xp, anim;
	private FletchingData(int interfaceId, int interfaceSlot, int toCreate, 
			int toDelete1, int toDelete2, int levelRequired, int makeAmount, int exp, int animation) {
		this.identifier = interfaceId;
		this.slot = interfaceSlot;
		this.make = toCreate;
		this.used = toDelete1;
		this.used2 = toDelete2;
		this.levelReq = levelRequired;
		this.makeAmt = makeAmount;
		this.xp = exp;
		this.anim = animation;
	}
	
	public static FletchingData forID(int i, int s) {
		for (FletchingData f: FletchingData.values()) {
			if (f.identifier == i && f.slot == s) {
				return f;
			}
		}
		return null;
	}

}
