package ionic.npc.drops;


public enum DropType {
	
	/**100% chance - Every kill**/
	ALWAYS(1000, 0),
	
	
	/**50% chance - Every 2 kills**/
	HALF_ALWAYS(500, 0),
	
	
	/**23% chance - Every 4.35 kills**/
	EXTREMELY_COMMON(230, 1),
	
	
	/**15% chance - Every 6.666 kills**/
	VERY_COMMON(150, 1),
	
	
	/**10% chance - Every 10 kills**/
	COMMON(100, 1),
	
	
	/**8.5% chance - Every 11.7 kills**/
	LESS_COMMON(85, 1),
	
	
	/**6% chance - Every 16.666 kills**/
	UNCOMMON(60, 1),
	
	
	/**4.5% chance - Every 22.222 kills**/
	MORE_UNCOMMON(45, 2),
	
	
	/**3% chance - Every 33.333 kills**/
	VERY_UNCOMMON(30, 2),
	
	
	/**1.8% chance - Every 55.555 kills**/
	EXTREMELY_UNCOMMON(18, 2),
	
	
	/**1% chance - Every 100 kills**/
	RARE(10, 3),
	
	
	/**0.7% chance - Every 142.85 kills**/
	VERY_RARE(7, 3),
	
	
	/**0.5% chance - Every 200 kills**/
	SUPER_RARE(5, 3),
	
	
	/**0.4% chance - Every 250 kills**/
	EXTREMELY_RARE(4, 3),
	
	
	/**0.2% chance - Every 500 kills**/
	ALMOST_NEVER(2, 3),
	
	
	
	
	;
	
	public int chance, type;
	
	private DropType(int chance, int type) {
		this.chance = chance;
		this.type = type;
	}
	
	public static DropType forID(String s) {
		for (DropType gg : DropType.values()) {
			if (gg.name().equals(s)) {
				return gg;
			}
		}
		return null;
	}
	

}
