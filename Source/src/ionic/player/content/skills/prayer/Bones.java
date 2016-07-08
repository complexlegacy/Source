package ionic.player.content.skills.prayer;


public enum Bones {
	BONES(526, 5, 28),
	BAT_BONES(530, 5, 28),
	PLAYER_BONES(2530, 5, 28),
	WOLF_BONES(2859, 5, 28),
	BIG_BONES(532, 15, 52),
	BABY_DRAGON_BONES(534, 30, 105),
	DRAGON_BONES(536, 72, 252),
	JOGRE_BONES(3125, 23, 79),
	ZOGRE_BONES(4812, 82, 79),
	OURG_BONES(4834, 140, 490),
	DAGANNOTH_BONES(6729, 125, 437),
	FROST_DRAGON_BONES(18830, 175, 630),
	WYVERN_BONES(6812, 50, 175),
	;
	
	
	private int itemID, expGained, altarXp;
	private Bones(final int itemID, final int expGained, int altarXp) {
		this.itemID = itemID;
		this.expGained = expGained;
		this.altarXp = altarXp;
	}
	public int getItemID() {
		return itemID;
	}
	public int getExpGained() {
		return expGained;
	}
	public int getAltarXp() {
		return altarXp;
	}
	public static Bones getID(final int ID) {
		for (Bones p : values()) {
			if (p.getItemID() == ID) {
				return p;
			}
		}
		return null;
	}
}
