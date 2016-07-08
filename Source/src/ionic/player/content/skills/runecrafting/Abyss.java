package ionic.player.content.skills.runecrafting;

public enum Abyss {
	
	AIR(13599, 7139, 2841, 4829),
    MIND(13600, 7140, 2793, 4828),
    WATER(13601, 7137, 2725, 4832),
    EARTH(13602, 7130, 2655, 4830),
    FIRE(13603, 7129, 2574, 4848),
    BODY(13604, 7131, 2523, 4826),
    COSMIC(13605, 7132, 2162, 4833),
    CHAOS(13606, 7134, 2281, 4837),
    NATURE(13607, 7133, 2400, 4835),
    LAW(13608, 7135, 2464, 4818),
    DEATH(13609, 7136, 2208, 4830),
    BLOOD(13610, 7141, 2468, 4889),
	;

	public int x, y, objectId, teleTab;
	private Abyss(int tab, int obj, int xpos, int ypos) {
		this.x = xpos;
		this.y = ypos;
		this.objectId = obj;
		this.teleTab = tab;
	}
	
	public static Abyss forID(int i) {
		for (Abyss f: Abyss.values()) {
			if (f.objectId == i) {
				return f;
			}
		}
		return null;
	}

}
