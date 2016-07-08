package ionic.player.content.skills;

import ionic.player.Client;
import ionic.player.Player;
import utility.Misc;

import java.util.Random;

public class Skill {

	public Skill(int fishing) {
	}

	public static final boolean isSuccess(Player p, int skillId, int levelRequired) {
		double level = p.getLevelForXP(p.playerXP[skillId]);
		double req = levelRequired;
		double successChance = Math.ceil((level * 50 - req * 15) / req / 3 * 4);
		int roll = Misc.random(99);
		if (successChance >= roll) {
			return true;
		}
		return false;
	}

	public static boolean hasRequiredLevel(final Client c, int id, int lvlReq,
										   String skill, String event) {
		if (c.skillLevel[id] < lvlReq) {
			c.sendMessage("You dont't have a high enough " + skill+ " level to " + event + "");
			c.sendMessage("You at least need the " + skill + " level of "+ lvlReq + ".");
			c.sendMessage("You haven't got high enough " + skill + " level to "+ event + "!");
			return false;
		}
		return true;
	}

	public static boolean skillCheck(int level, int levelRequired, int itemBonus) {
		double chance = 0.0;
		double baseChance = levelRequired < 11 ? 15 : levelRequired < 51 ? 10 : 5;
		chance = baseChance + ((level - levelRequired) / 2d) + (itemBonus / 10d);
		return chance >= (new Random().nextDouble() * 100.0);
	}

	public static final boolean isSuccess(Player p, int skillId, int levelRequired, int toolLevelRequired) {
		double level = (p.getLevelForXP(p.playerXP[skillId]) + (double) toolLevelRequired) / 2;
		double req = levelRequired;
		double successChance = Math.ceil((level * 50 - req * 15) / req / 3 * 4);
		int roll = Misc.random(99);
		if (successChance >= roll) {
			return true;
		}
		return false;
	}

}
