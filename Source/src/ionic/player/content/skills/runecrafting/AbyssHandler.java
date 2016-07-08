package ionic.player.content.skills.runecrafting;

import ionic.player.Player;

public class AbyssHandler {
	
	public static boolean clickObject(Player c, int obj) {
		Abyss a = Abyss.forID(obj);
		if (a != null) {
			c.getPA().movePlayer(a.x, a.y, 0);
			return true;
		}
		return false;
	}

}
