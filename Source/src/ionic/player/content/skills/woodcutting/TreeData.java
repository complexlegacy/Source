package ionic.player.content.skills.woodcutting;

public enum TreeData {
	
	NORMAL(new int[]{1276, 1277, 1278, 1279, 1280, 1282, 1283, 1284, 1285, 1286, 1289,
			1290, 1291, 1315, 1316, 1318, 1319, 1330, 1331, 1332, 1333, 1365, 1383, 1384,
			2409, 3033, 3034, 3035, 3036, 3881, 3882, 3883, 5902, 5903, 5904}, 1, 25, 1511, 
			1342, 14, 100, 0),
	OAK(new int[]{1281, 2037}, 15, 37.5, 1521, 1356, 22, 25, 7),
	WILLOW(new int[]{1308, 5551, 5552, 5553}, 30, 67.5, 1519, 7399, 30, 5, 30),
	MAPLE(new int[]{1307, 4677}, 45, 100, 1517, 1343, 36, 15, 40),
	YEW(new int[]{1309}, 60, 175, 1515, 7402, 60, 5, 40),
	MAGIC(new int[]{1306}, 75, 250, 1513, 7401, 80, 3, 35),
	ACHEY(new int[]{2023}, 1, 25, 2862, 3371, 75, 100, 20),
	MAHOGANY(new int[]{9034}, 50, 125, 6332, 9035, 80, 10, 20),
	ARCTIC(new int[]{21273}, 55, 175, 10810, 21274, 80, 6, 10),
	TEAK(new int[]{9036}, 35, 85, 6333, 9037, 14, 20, 5),
	HOLLOW(new int[]{2289, 4060}, 45, 83, 3239, 2310, 59, 15, 1),
	DRAWMEN(new int[]{1292}, 36, 0, 771, 1513, 59, 100, 1);
	
	public int[] objectId;
	public int reqLvl, logId, stumpId, respawnTime, decayChance, maxLife;
	public double xp;
	
	private TreeData(int[] objectId, int reqLvl, double xp, int logId, int stumpId, int respawnTime, int decayChance, int life) {
		this.objectId = objectId;
		this.reqLvl = reqLvl;
		this.xp = xp;
		this.logId = logId;
		this.stumpId = stumpId;
		this.respawnTime = respawnTime;
		this.decayChance = decayChance;
		this.maxLife = life;
	}
	
	public static TreeData forId(int o) {
		for (TreeData t : TreeData.values()) {
			for (int i : t.objectId) {
				if (i == o) {
					return t;
				}
			}
		}
		return null;
	}
	

}
