package ionic.player.content.skills.fletching;

import ionic.item.ItemAssistant;
import ionic.player.Player;

/**
 * @author Keith
 */
public class FletchingUseItem {

	
	public static final int KNIFE = 946;
	public static final int BOW_STRING = 1777;
	public static final int CROSSBOW_STRING = 9438;
	public static final int ARROW_SHAFT = 52;
	public static final int UNFINISHED_ARROWS = 53;
	public static final int FEATHERS = 314;
	
	/**
	 * Logs id, interface item 1, interface item 2, interface item 3, 
	 * amount of items on the interface, interface identifier
	 **/
	public static int[][] KNIFE_ON_LOGS = {
		{1511, 50, 48, 52, 3, 1},//normal
		{1521, 54, 56, 9442, 3, 2},//oak
		{1519, 60, 58, 9444, 3, 3},//willow
		{1517, 64, 62, 9448, 3, 4},//maple
		{1515, 68, 66, 9452, 3, 5},//yew
		{1513, 72, 70, -1, 2, 6}//magic
	};
	/**
	 * Unstrung bow id, strung bow id for interface display.
	 * interface identifier
	 */
	public static int[][] BOWSTRING_ON_BOW = {
		{50, 841, 7}, {48, 839, 8},//normal
		{54, 843, 9}, {56, 845, 10},//oak
		{60, 849, 11}, {58, 847, 12},//willow
		{64, 853, 13}, {62, 851, 14},//maple
		{68, 857, 15}, {66, 855, 16},//yew
		{72, 861, 17}, {70, 859, 18},//magic
	};
	/**
	 * Arrow tips id, arrows id, interface identifier
	 */
	public static int[][] TIPS_ON_ARROWS = {
		{39, 882, 19},//bronze
		{40, 884, 20},//iron
		{41, 886, 21},//steel
		{42, 888, 22},//mithril
		{43, 890, 23},//adamant
		{44, 892, 24},//rune
		{11237, 11212, 25},//dragon
	};
	/**
	 * Unstrung id, stock id, limbs id, interface identifier
	 */
	public static int[][] STOCK_ON_LIMBS = {
		{9454, 9442, 9420, 27},//bronze
		{9457, 9442, 9423, 28},//iron
		{9459, 9444, 9425, 29},//steel
		{9461, 9448, 9427, 30},//mithril
		{9463, 9452, 9429, 31},//adamant
		{9465, 9452, 9431, 32},//rune
	};
	/**
	 * Unstrung crossbow on cbow string
	 * Strung id, unstrung id, interface identifier
	 */
	public static int[][] UNSTRUNG_ON_STRING = {
		{9174, 9454, 33},//bronze
		{9177, 9457, 34},//iron
		{9179, 9459, 35},//steel
		{9181, 9461, 36},//mithril
		{9183, 9463, 37},//adamant
		{9185, 9465, 38},//rune
	};
	/**
	 * Completed bolts id, unfinished bolts id, interface identifier
	 */
	public static int[][] BOLTS = {
		{877, 9375, 39},//bronze
		{9140, 9377, 40},//iron
		{9141, 9378, 41},//steel
		{9142, 9379, 42},//mithril
		{9143, 9380, 43},//adamant
		{9144, 9381, 44},//rune
	};
	/**
	 * Completed darts id, dart tips, interface identifier
	 */
	public static int[][] DARTS = {
		{806, 819, 46},//bronze
		{807, 820, 47},//iron
		{808, 821, 48},//steel
		{809, 822, 49},//mithril
		{810, 823, 50},//adamant
		{811, 824, 51},//rune
		{11230, 11232, 52},//dragon
	};
	
	public FletchingUseItem(Player c, int i, int i2) {
		if (i == KNIFE || i2 == KNIFE) {
			for (int j = 0; j < KNIFE_ON_LOGS.length; j++) {
				if (KNIFE_ON_LOGS[j][0] == i2 || KNIFE_ON_LOGS[j][0] == i) {
					sendInterface(c, KNIFE_ON_LOGS[j][1], KNIFE_ON_LOGS[j][2], KNIFE_ON_LOGS[j][3], KNIFE_ON_LOGS[j][4], KNIFE_ON_LOGS[j][5]);
					return;
				}
			}
		}
		if (i == BOW_STRING || i2 == BOW_STRING) {
			for (int j = 0; j < BOWSTRING_ON_BOW.length; j++) {
				if (BOWSTRING_ON_BOW[j][0] == i2 || BOWSTRING_ON_BOW[j][0] == i) {
					sendInterface(c, BOWSTRING_ON_BOW[j][1], -1, -1, 1, BOWSTRING_ON_BOW[j][2]);
					return;
				}
			}
		}
		if (i == ARROW_SHAFT || i2 == ARROW_SHAFT) {
			if (i == FEATHERS || i2 == FEATHERS) {
				sendInterface(c, UNFINISHED_ARROWS, -1, -1, 1, 26);
			}
		}
		if (i == UNFINISHED_ARROWS || i2 == UNFINISHED_ARROWS) {
			for (int j = 0; j < TIPS_ON_ARROWS.length; j++) {
				if (TIPS_ON_ARROWS[j][0] == i2 || TIPS_ON_ARROWS[j][0] == i) {
					sendInterface(c, TIPS_ON_ARROWS[j][1], -1, -1, 1, TIPS_ON_ARROWS[j][2]);
					return;
				}
			}
		}
		for (int j = 0; j < STOCK_ON_LIMBS.length; j++) {
			if (STOCK_ON_LIMBS[j][1] == i2 && STOCK_ON_LIMBS[j][2] == i
					|| STOCK_ON_LIMBS[j][2] == i2 && STOCK_ON_LIMBS[j][1] == i) {
				sendInterface(c, STOCK_ON_LIMBS[j][0], -1, -1, 1, STOCK_ON_LIMBS[j][3]);
				return;
			}
		}
		if (i == CROSSBOW_STRING || i2 == CROSSBOW_STRING) {
			for (int j = 0; j < UNSTRUNG_ON_STRING.length; j++) {
				if (UNSTRUNG_ON_STRING[j][1] == i2 || UNSTRUNG_ON_STRING[j][1] == i) {
					sendInterface(c, UNSTRUNG_ON_STRING[j][0], -1, -1, 1, UNSTRUNG_ON_STRING[j][2]);
					return;
				}
			}
		}
		if (i == FEATHERS || i2 == FEATHERS) {
			for (int j = 0; j < BOLTS.length; j++) {
				if (BOLTS[j][1] == i2 || BOLTS[j][1] == i) {
					sendInterface(c, BOLTS[j][0], -1, -1, 1, BOLTS[j][2]);
					return;
				}
			}
			for (int j = 0; j < DARTS.length; j++) {
				if (DARTS[j][1] == i2 || DARTS[j][1] == i) {
					sendInterface(c, DARTS[j][0], -1, -1, 1, DARTS[j][2]);
					return;
				}
			}
		}
	}
	
	
	public void sendInterface(Player c, int i1, int i2, int i3, int amt, int interf) {
		c.inFletchInterface = true;
		c.isFletching = false;
		if (amt == 1) {
			c.getPA().sendFrame164(4429);
         	c.getPA().sendFrame246(1746, 190, i1);
     		c.getPA().sendFrame126(""+ItemAssistant.getItemName(i1)+"", 2799);
     		c.fletchInterface = interf;
		} else if (amt == 2) {
			c.getPA().sendFrame164(8866);
	        c.getPA().sendFrame246(8869, 190, i1);
	        c.getPA().sendFrame246(8870, 190, i2);
	        c.getPA().sendFrame126(""+ItemAssistant.getItemName(i1) +"", 8874);
	        c.getPA().sendFrame126(""+ItemAssistant.getItemName(i2) +"", 8878);
	        c.fletchInterface = interf;
		} else if (amt == 3) {
			c.getPA().sendFrame164(8880);
         	c.getPA().sendFrame246(8883, 190, i1);
         	c.getPA().sendFrame246(8884, 190, i2);
         	c.getPA().sendFrame246(8885, 190, i3);
         	c.getPA().sendFrame126(""+ItemAssistant.getItemName(i1)+"", 8889);
         	c.getPA().sendFrame126(""+ItemAssistant.getItemName(i2)+"", 8893);
     		c.getPA().sendFrame126(""+ItemAssistant.getItemName(i3)+"", 8897);
     		c.fletchInterface = interf;
		}
	}
	
	
	
	
}
