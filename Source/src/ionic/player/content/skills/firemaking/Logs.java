package ionic.player.content.skills.firemaking;

public enum Logs {
	
	NORMAL_LOG(1511, 1, 40, 3038, 60),
	BLUE_LOG(7406, 1, 45, 11406, 60),
	RED_LOG(7404, 1, 45, 11404, 60),
	GREEN_LOG(7405, 1, 45, 11405, 60),
	WHITE_LOG(10328, 1, 45, 20000, 60),
	PURPLE_LOG(10329, 1, 45, 20001, 60),
	OAK_LOG(1521, 15, 60, 3038, 70),
	WILLOW_LOG(1519, 30, 90, 3038, 80),
	MAPLE_LOG(1517, 45, 135, 3038, 100),
	YEW_LOG(1515, 60, 202.5, 3038, 115),
	MAGIC_LOG(1513, 75, 303.8, 3038, 125);
	
	public int itemId;
	public int levelReq;
	public int fireObj;
	public double exp;
	public int timer;
	
	private Logs(int item, int level, double xp, int fire, int t) {
		this.itemId = item;
		this.levelReq = level;
		this.exp = xp;
		this.fireObj = fire;
		this.timer = t;
	}
	
	public static Logs forID(int id) {
		for (Logs l : values()) {
			if (l.itemId == id) {
				return l;
			}
		}
		return null;
	}

}
