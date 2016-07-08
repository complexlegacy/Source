package ionic.player.content.skills.cooking;

public enum Cook {
	
	SHRIMP(317, 315, 323, 1, 34, 13, 30),
	
	TUNA(359, 361, 367, 30, 65, 44, 100),
	
	LOBSTER(377, 379, 381, 40, 74, 62, 120),
	
	SWORDFISH(371, 373, 375, 45, 86, 73, 140),
	
	MONKFISH(7944, 7946, 7948, 62, 92, 84, 160),
	
	SHARK(383, 385, 387, 1, 98, 89, 210),
	
	MANTA_RAY(389, 391, 393, 91, 120, 98, 220),
	
	ROCKTAIL(15270, 15272, 15274, 93, 120, 99, 250);
	
	public int raw, cook, burn, lvlReq, stop, stopG, xp;
	private Cook(int uncooked, int cooked, int burnt, int lvl, int stopBurn, int stopGaunts, int exp) {
		this.raw = uncooked;
		this.cook = cooked;
		this.burn = burnt;
		this.lvlReq = lvl;
		this.stop = stopBurn;
		this.stopG = stopGaunts;
		this.xp = exp;
	}
	
	public static Cook forID(int k) {
		for (Cook i: Cook.values()) {
			if (i.raw == k) {
				return i;
			}
		}
		return null;
	}

}
