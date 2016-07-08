package ionic.player.achievements;

import ionic.player.Player;

public class ViewAchievement {
	
	private enum type {
		EASY,
		MEDIUM,
		HARD,
		ELITE;
	}
	
	public static boolean clickButton(Player c, int button) {
		if (button >= 63198 && button <= 63255) {
			open (c, type.EASY, button - 63198);
			return true;
		}
		if (button >= 64000 && button <= 64041) {
			open (c, type.EASY, button - 64000 + 58);
			return true;
		}
		if (button >= 101146 && button <= 101245) {
			open (c, type.MEDIUM, button - 101146);
			return true;
		}
		if (button >= 102090 && button <= 102189) {
			open (c, type.HARD, button - 102090);
			return true;
		}
		if (button >= 103034 && button <= 103133) {
			open (c, type.ELITE, button - 103034);
			return true;
		}
		return false;
	}
	
	public static void open(Player c, type t, int s) {
		Achievement a = null;
		int progress = 0;
		switch(t) {
		case EASY: a = AchievementData.easy[s]; progress = c.easyProgress[s]; break;
		case MEDIUM: a = AchievementData.medium[s]; progress = c.mediumProgress[s]; break;
		case HARD: a = AchievementData.hard[s]; progress = c.hardProgress[s]; break;
		case ELITE: a = AchievementData.elite[s]; progress = c.eliteProgress[s]; break;
		}
		if (a != null) {
			showInterface(c, a, progress);
		}
	}
	
	public static void showInterface(Player c, Achievement a, int prog) {
		c.getPA().sendFrame126(a.n, 28403);
		c.getPA().sendFrame126(a.d, 28404);
		String s = "";
		s = a.p > 1 ? "s" : "";
		c.getPA().sendFrame126("Completion Reward: @cya@"+a.p+"@yel@ Achievement point"+s, 28405);
		c.getPA().sendFrame126(""+getCol(prog, a.a)+"("+prog+"/"+a.a+")", 28406);
		c.getPA().showInterface(28400);
	}
	
	public static String getCol(int p, int r) {
		String s = "@red@";
		if (p > 0)
			s = "@yel@";
		if (p >= r)
			s = "@gre@";
		return s;
	}

}
