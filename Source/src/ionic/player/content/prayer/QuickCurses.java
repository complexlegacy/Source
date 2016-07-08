package ionic.player.content.prayer;

import ionic.player.Client;
import ionic.player.interfaces.InterfaceAssistant;
import core.Constants;

/**
 * 
 * @author relex lawl / relex
 * 
 * Old class I decided to upgrade
 */

public class QuickCurses {

	public static final int MAX_CURSES = 20;
	private static final int[][] data = { 
			{ 67100, 680, 0 }, { 67101, 681, 1 },
			{ 67102, 682, 2 }, { 67103, 683, 3 }, { 67104, 684, 4 },
			{ 67105, 685, 5 }, { 67106, 686, 6 }, { 67107, 687, 7 },
			{ 67108, 688, 8 }, { 67109, 689, 9 }, { 67110, 690, 10 },
			{ 67111, 691, 11 }, { 67112, 692, 12 }, { 67113, 693, 13 },
			{ 67114, 694, 14 }, { 67115, 695, 15 }, { 67116, 696, 16 },
			{ 67117, 697, 17 }, { 67118, 698, 18 }, { 67119, 699, 19 } };// actionID,
																			// frameID,
																			// quickCurse
																			// =
																			// true

	public static void canBeSelected(Client c, int actionId) {
		boolean[] curse = new boolean[MAX_CURSES];
		for (int j = 0; j < curse.length; j++)
			curse[j] = true;
		switch (actionId) {
		case 67101:
			curse[10] = false;
			curse[19] = false;
			break;

		case 67102:
			curse[11] = false;
			curse[19] = false;
			break;

		case 67103:
			curse[12] = false;
			curse[19] = false;
			break;

		case 67104:
			curse[16] = false;
			break;

		case 67107:
			curse[8] = false;
			curse[9] = false;
			curse[17] = false;
			curse[18] = false;
			break;

		case 67108:
			curse[7] = false;
			curse[9] = false;
			curse[17] = false;
			curse[18] = false;
			break;

		case 67109:
			curse[7] = false;
			curse[8] = false;
			curse[17] = false;
			curse[18] = false;
			break;

		case 67110:
			curse[1] = false;
			curse[19] = false;
			break;

		case 67111:
			curse[2] = false;
			curse[19] = false;
			break;

		case 67112:
			curse[3] = false;
			curse[19] = false;
			break;

		case 67113:
		case 67114:
		case 67115: // Leech run-energy
			curse[19] = false;
			break;

		case 67116: // Leech special
			curse[4] = false;
			curse[19] = false;
			break;

		case 67117:
			for (int i = 7; i < 10; i++)
				curse[i] = false;
			curse[18] = false;
			break;

		case 67118:
			for (int i = 6; i < 10; i++)
				curse[i] = false;
			curse[17] = false;
			break;

		case 67119:
			for (int i = 1; i < 5; i++) {
				for (int j = 10; j < 17; j++) {
					curse[i] = false;
					curse[j] = false;
				}
			}
			break;
		}
		for (int i = 0; i < MAX_CURSES; i++) {
			if (!curse[i]) {
				c.quickCurses[i] = false;
				for (int j = 0; j < data.length; j++) {
					if (i == data[j][2])
						c.getPA().sendFrame36(data[j][1], 0);
				}
			}
		}
	}

	public static void clickConfirm(Client c) {
		c.setSidebarInterface(5, c.Prayerbook == 1 ? 23377 : 5608);
	}

	public static void clickCurse(Client c, int actionId) {
		canBeSelected(c, actionId);
		for (int j = 0; j < data.length; j++) {
			if (data[j][0] == actionId) {
				if (c.quickCurses[data[j][2]]) {
					c.quickCurses[data[j][2]] = false;
					c.getPA().sendFrame36(data[j][1], 0);
				} else {
					c.quickCurses[data[j][2]] = true;
					c.getPA().sendFrame36(data[j][1], 1);
				}
			}
		}
	}

	public static void loadCheckMarks(Client player) {
		for (int j = 0; j < data.length; j++)
			player.getPA().sendFrame36(data[j][1],
					player.quickCurses[data[j][2]] ? 1 : 0);
	}

	public static void selectQuickInterface(Client c) {
		if (c.Prayerbook != 1)
			QuickPrayers.loadCheckMarks(c);
		else
			loadCheckMarks(c);
		c.setSidebarInterface(5, c.Prayerbook == 1 ? 22992 : 22923);
		c.getPA().sendFrame106(5);
	}

	public static void turnOffQuicks(Client c) {
		c.getCombat().resetPrayers();
		c.quickPray = false;
		c.quickCurse = false;
		c.headIcon = -1;
		c.getPA().requestUpdates();
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public static void turnOnQuicks(Client c) {
		c.getCombat().resetPrayers();
		if (c.Prayerbook != 1)
		{
			for (int i = 0; i < c.quickPrayers.length; i++)
			{
				if (c.quickPrayers[i] && !c.prayerActive[i]) 
				{
					c.getCombat().activatePrayer(i);
					c.quickPray = true;
					c.updateRequired = true;
					c.setAppearanceUpdateRequired(true);
					InterfaceAssistant.quickPrayersOn(c);
				}
				if (!c.quickPrayers[i])
				{
					c.prayerActive[i] = false;
					c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
					c.getPA().requestUpdates();
				}
			}
		} else 
		{
			for (int i = 0; i < c.quickCurses.length; i++)
			{
				if (c.quickCurses[i] && !c.prayerActive[i]) 
				{
					c.curses().activateCurse(i);
					c.quickCurse = true;
					c.updateRequired = true;
					c.setAppearanceUpdateRequired(true);
					InterfaceAssistant.quickPrayersOn(c);
				}
				if (!c.quickCurses[i])
				{
					if (!c.prayerActive[Constants.PROTECT_ITEM])
					{
						c.prayerActive[i] = false;
					}
					c.getPA().requestUpdates();
				}
			}
		}
	}
}