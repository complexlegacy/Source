package ionic.player.content.miscellaneous;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import core.Constants;
import utility.Misc;


public class AncientEffigy {

	public static final int LAMP = 18782;
	public static final int STARVED = 18778;
	public static final int NOURISHED = 18779;
	public static final int STATED = 18780;
	public static final int GORGED = 18781;
	//public static DialogueHandler dh;

	private static int calculateXp(Player p, int skillId) {
		int playerLevel = p.skillLevel[skillId];
		int xp = (int) Math.floor((Math.pow(playerLevel, 3) - 2
				* Math.pow(playerLevel, 2) + 100 * playerLevel) / 20);
		return xp;
	}

	public static int[] getRandomSkills() {
		int first = 7 + Misc.random(14);
		int second = first;
		while (second == first) {
			second = 7 + Misc.random(14);
		}
		return new int[] { first, second };
	}

	public static void handleItemClick(Player p, int itemId) {
		switch (itemId) {
		case STARVED:
			if (p.effigyFirst == 0) {
				int[] skills = getRandomSkills();
				p.effigyFirst = skills[0];
				p.effigySecond = skills[1];
			}
			p.triengEffigy = STARVED;
			//dh.sendOption( Constants.SKILL_NAMES[p.effigyFirst],Constants.SKILL_NAMES[p.effigySecond]);
			break;
		case NOURISHED:
			if (p.effigyFirst == 0) {
				int[] skills = getRandomSkills();
				p.effigyFirst = skills[0];
				p.effigySecond = skills[1];
			}
			p.triengEffigy = NOURISHED;
			//dh.sendOption(Constants.SKILL_NAMES[p.effigyFirst],Constants.SKILL_NAMES[p.effigySecond]);

			break;
		case STATED:
			if (p.effigyFirst == 0) {
				int[] skills = getRandomSkills();
				p.effigyFirst = skills[0];
				p.effigySecond = skills[1];
			}
			p.triengEffigy = STATED;
			//dh.sendOption( Constants.SKILL_NAMES[p.effigyFirst],Constants.SKILL_NAMES[p.effigySecond]);

			break;
		case GORGED:
			if (p.effigyFirst == 0) {
				int[] skills = getRandomSkills();
				p.effigyFirst = skills[0];
				p.effigySecond = skills[1];
			}
			p.triengEffigy = GORGED;
			//dh.sendOption( Constants.SKILL_NAMES[p.effigyFirst],Constants.SKILL_NAMES[p.effigySecond]);

			break;
		case LAMP:
			p.getPA().showInterface(2808);
			p.xpLamp = LAMP;
			break;
		}
	}

	public static int getLevelNeeded(int effigy) {
		if (effigy == STARVED)
			return 91;
		if (effigy == NOURISHED)
			return 93;
		if (effigy == STATED)
			return 95;
		if (effigy == GORGED)
			return 97;
		return -1;
	}

	public static int getXPReward(int effigy) {

		if (effigy == STARVED)
			return 15000;
		if (effigy == NOURISHED)
			return 20000;
		if (effigy == STATED)
			return 25000;
		if (effigy == GORGED)
			return 30000;
		return -1;
	}

	public static void attemptNourish(Player p, int type) {
		if (!ItemAssistant.playerHasItem(p, p.triengEffigy))
			return;
		int skill = type == 0 ? p.effigyFirst : p.effigySecond;
		if (p.skillLevel[skill] >= getLevelNeeded(p.triengEffigy)) {
			int xp = getXPReward(p.triengEffigy);
			if (xp != -1) {
				p.getPA().addSkillXP(xp, skill);
				ItemAssistant.deleteItem(p, p.triengEffigy, 1);
				ItemAssistant.addItem(p, p.triengEffigy + 1, 1);
				p.triengEffigy = 0;
				p.gfx0(2692);
				p.startAnimation(14177);
				p.effigyFirst = 0;
				p.effigySecond = 0;
			}
		} else {
			p.sendMessage("You need to be atleast level "
					+ getLevelNeeded(p.triengEffigy) + " in "
					+ Constants.SKILL_NAMES[skill] + " to" + "nourish this effigy.");
			p.triengEffigy = 0;
		}

	}

	public static void openLamp(Player p, int skillId) {
		int xp = calculateXp(p, skillId);
		p.getPA().addSkillXP(xp, skillId);
		p.effigyFirst = 0;
		p.effigySecond = 0;
		ItemAssistant.deleteItem(p, LAMP, 1);
	}
}
