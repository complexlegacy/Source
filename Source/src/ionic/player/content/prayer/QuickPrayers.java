package ionic.player.content.prayer;

import ionic.player.Client;

/**
 * 
 * @author relex lawl / relex
 * 
 *         I do not give permission for this to be released on any website
 * 
 *         Old class I decided to upgrade
 */

public class QuickPrayers {

	public static final int MAX_PRAYERS = 26;
	private static final int[] defPrayId = { 67050, 67055, 67063, 67074, 67075 };
	private static final int[] strPrayId = { 67051, 67056, 67064, 67074, 67075 };
	private static final int[] atkPrayId = { 67052, 67057, 67065, 67074, 67075 };
	private static final int[] rangePrayId = { 67053, 67061, 67069 };
	private static final int[] magePrayId = { 67054, 67062, 67070 };
	private static final int[] headIconsId = { 67066, 67067, 67068, 67071,
			67072, 67073 };
	private static final int[] defPray = { 0, 5, 13, 24, 25 };
	private static final int[] strPray = { 1, 6, 14, 24, 25 };
	private static final int[] atkPray = { 2, 7, 15, 24, 25 };
	private static final int[] rangePray = { 3, 11, 19 };
	private static final int[] magePray = { 4, 12, 20 };
	private static final int[] headIcons = { 16, 17, 18, 21, 22, 23 };
	private static final int[][] data = { { 67050, 650, 0 }, { 67051, 651, 1 },
			{ 67052, 652, 2 }, { 67053, 653, 3 }, { 67054, 654, 4 },
			{ 67055, 655, 5 }, { 67056, 656, 6 }, { 67057, 657, 7 },
			{ 67058, 658, 8 }, { 67059, 659, 9 }, { 67060, 660, 10 },
			{ 67061, 661, 11 }, { 67062, 662, 12 }, { 67063, 663, 13 },
			{ 67064, 664, 14 }, { 67065, 665, 15 }, { 67066, 666, 16 },
			{ 67067, 667, 17 }, { 67068, 668, 18 }, { 67069, 669, 19 },
			{ 67070, 670, 20 }, { 67071, 671, 21 }, { 67072, 672, 22 },
			{ 67073, 673, 23 }, { 67074, 674, 24 }, { 67075, 675, 25 } };// actionID,
																			// frameID,
																			// quickCurse
																			// =
																			// true

	public static void canBeSelected(Client c, int actionId) {
		boolean[] prayer = new boolean[MAX_PRAYERS];
		for (int j = 0; j < prayer.length; j++)
			prayer[j] = true;
		switch (actionId) {
		case 67050:
		case 67055:
		case 67063:
			for (int j = 0; j < defPrayId.length; j++) {
				if (defPrayId[j] != actionId) {
					prayer[defPray[j]] = false;
				}
			}
			break;

		case 67051:
		case 67056:
		case 67064:
			for (int j = 0; j < strPray.length; j++) {
				if (strPrayId[j] != actionId) {
					prayer[strPray[j]] = false;
				}
			}
			for (int j = 0; j < rangePray.length; j++) {
				if (rangePrayId[j] != actionId) {
					prayer[rangePray[j]] = false;
				}
			}
			for (int j = 0; j < magePray.length; j++) {
				if (magePrayId[j] != actionId) {
					prayer[magePray[j]] = false;
				}
			}
			break;

		case 67052:
		case 67057:
		case 67065:
			for (int j = 0; j < atkPray.length; j++) {
				if (atkPrayId[j] != actionId) {
					prayer[atkPray[j]] = false;
				}
			}
			for (int j = 0; j < rangePray.length; j++) {
				if (rangePrayId[j] != actionId) {
					prayer[rangePray[j]] = false;
				}
			}
			for (int j = 0; j < magePray.length; j++) {
				if (magePrayId[j] != actionId) {
					prayer[magePray[j]] = false;
				}
			}
			break;

		case 67053:
		case 67061:
		case 67069:
			for (int j = 0; j < atkPray.length; j++) {
				if (atkPrayId[j] != actionId) {
					prayer[atkPray[j]] = false;
				}
			}
			for (int j = 0; j < strPray.length; j++) {
				if (strPrayId[j] != actionId) {
					prayer[strPray[j]] = false;
				}
			}
			for (int j = 0; j < rangePray.length; j++) {
				if (rangePrayId[j] != actionId) {
					prayer[rangePray[j]] = false;
				}
			}
			for (int j = 0; j < magePray.length; j++) {
				if (magePrayId[j] != actionId) {
					prayer[magePray[j]] = false;
				}
			}
			break;

		case 67054:
		case 67062:
		case 67070:
			for (int j = 0; j < atkPray.length; j++) {
				if (atkPrayId[j] != actionId) {
					prayer[atkPray[j]] = false;
				}
			}
			for (int j = 0; j < strPray.length; j++) {
				if (strPrayId[j] != actionId) {
					prayer[strPray[j]] = false;
				}
			}
			for (int j = 0; j < rangePray.length; j++) {
				if (rangePrayId[j] != actionId) {
					prayer[rangePray[j]] = false;
				}
			}
			for (int j = 0; j < magePray.length; j++) {
				if (magePrayId[j] != actionId) {
					prayer[magePray[j]] = false;
				}
			}
			break;

		case 67066:
		case 67067:
		case 67068:
		case 67071:
		case 67072:
		case 67073:
			for (int j = 0; j < headIcons.length; j++) {
				if (headIconsId[j] != actionId) {
					prayer[headIcons[j]] = false;
				}
			}
			break;

		case 67074:
			for (int j = 0; j < atkPray.length; j++) {
				if (atkPrayId[j] != actionId) {
					prayer[atkPray[j]] = false;
				}
			}
			for (int j = 0; j < strPray.length; j++) {
				if (strPrayId[j] != actionId) {
					prayer[strPray[j]] = false;
				}
			}
			for (int j = 0; j < rangePray.length; j++) {
				if (rangePrayId[j] != actionId) {
					prayer[rangePray[j]] = false;
				}
			}
			for (int j = 0; j < magePray.length; j++) {
				if (magePrayId[j] != actionId) {
					prayer[magePray[j]] = false;
				}
			}
			for (int j = 0; j < defPray.length; j++) {
				if (defPrayId[j] != actionId) {
					prayer[defPray[j]] = false;
				}
			}
			break;

		case 67075:
			for (int j = 0; j < atkPray.length; j++) {
				if (atkPrayId[j] != actionId) {
					prayer[atkPray[j]] = false;
				}
			}
			for (int j = 0; j < strPray.length; j++) {
				if (strPrayId[j] != actionId) {
					prayer[strPray[j]] = false;
				}
			}
			for (int j = 0; j < rangePray.length; j++) {
				if (rangePrayId[j] != actionId) {
					prayer[rangePray[j]] = false;
				}
			}
			for (int j = 0; j < magePray.length; j++) {
				if (magePrayId[j] != actionId) {
					prayer[magePray[j]] = false;
				}
			}
			for (int j = 0; j < defPray.length; j++) {
				if (defPrayId[j] != actionId) {
					prayer[defPray[j]] = false;
				}
			}
			break;
		}
		for (int i = 0; i < MAX_PRAYERS; i++) {
			if (!prayer[i]) {
				c.quickPrayers[i] = false;
				for (int j = 0; j < data.length; j++) {
					if (i == data[j][2])
						c.getPA().sendFrame36(data[j][1], 0);
				}
			}
		}
	}

	public static void clickPray(Client c, int actionId) {
		canBeSelected(c, actionId);
		for (int j = 0; j < data.length; j++) {
			if (data[j][0] == actionId) {
				if (c.quickPrayers[data[j][2]]) {
					c.quickPrayers[data[j][2]] = false;
					c.getPA().sendFrame36(data[j][1], 0);
				} else {
					c.quickPrayers[data[j][2]] = true;
					c.getPA().sendFrame36(data[j][1], 1);
				}
			}
		}
	}

	public static void loadCheckMarks(Client player) {
		for (int j = 0; j < data.length; j++)
			player.getPA().sendFrame36(data[j][1],
					player.quickPrayers[data[j][2]] ? 1 : 0);
	}
}
