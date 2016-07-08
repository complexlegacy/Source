package ionic.player.content.skills.mining;

public enum RockData {
	COPPER_ORE(new int[] { 2090, 2091, 11960, 11961, 11962, 31081, 31080, 31082, 11937 }, 1, 436, 452, 15, 18, 0),
	TIN_ORE(new int[] { 2094, 2095, 11959, 11957, 31077, 31078, 31079, 11935 }, 1, 438, 452, 15, 18, 0),
	IRON_ORE(new int[] { 2092, 2093, 11954, 11955, 11956, 37309 }, 15, 440, 452, 25, 35, 7),
	COAL_ORE(new int[] { 2096, 2097, 31070, 31068 }, 30, 453, 452, 35, 50, 9),
	GOLD_ORE(new int[] { 2098, 2099, 45067, 45068 }, 40, 444, 452, 40, 65, 9),
	MITHRIL_ORE(new int[] { 2102, 2103 }, 55, 447, 452, 55, 80, 10),
	ADAMANT_ORE(new int[] { 2105, 29233, 29235, }, 70, 449, 452, 65, 95, 11),
	RUNITE_ORE(new int[] { 45070, 45069, 11943 }, 85, 451, 452, 210, 125, 13),
	;

	public int[] rockIds;
	public int levelRequired, replacementId, reward, respawnTimer, experience, life;

	private RockData(int[] rocks, int level, int reward, int replacement, int respawn, int experience, int life) {
		this.rockIds = rocks;
		this.levelRequired = level;
		this.reward = reward;
		this.replacementId = replacement;
		this.respawnTimer = respawn;
		this.experience = experience;
		this.life = life;
	}
	
	public static RockData forID(int obj) {
		for (RockData r : RockData.values()) {
			for (int i = 0; i < r.rockIds.length; i++) {
				if (r.rockIds[i] == obj) {
					return r;
				}
			}
		}
		return null;
	}

}
