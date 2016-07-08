package ionic.player.content.skills.woodcutting;

import ionic.player.Player;
import utility.Misc;

/**
 * @author Keith
 */
/**
 * Handles random events - Bird nests, Axe Head Disconnecting, Axe Breaking.
 */
public class WoodcuttingRandom {
	
	public WoodcuttingRandom(Player c, AxeData a) {
		int random = Misc.random(330);
		if (random == 1) {
			int event = Misc.random(15);
			if (event >= 0 && event <= 8) {
				new BirdNest(c);
			}
			if (event >= 9 && event <= 12) {
				new AxeBreaking(c, a);
			}
			if (event >= 13) {
				new AxeHeadDisconnects(c, a);
			}
		}
	}

}
