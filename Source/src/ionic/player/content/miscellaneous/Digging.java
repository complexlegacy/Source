package ionic.player.content.miscellaneous;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import ionic.player.content.minigames.Barrows;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;

/**
 * @author Keith
 */
public class Digging {
	private static final String BARROWS_MSG = "You have broken into a crypt.";
	
	
	/**
	 * The type of function that the area you're digging in has.
	 */
	private static enum digType {
		TELEPORT(),
		ADDITEM(),
		BARROWS(),
		;
	}
	
	/**
	 * the data for the areas that you can dig in
	 */
	private static enum digAreas {
		AHRIMS(new int[] {3561, 3285, 3569, 3292, 0}, digType.BARROWS, new int[] {3557, 9703, 3}, BARROWS_MSG),
		DHAROKS(new int[] {3572, 3294, 3578, 3300, 0}, digType.BARROWS, new int[] {3546, 9684, 3}, BARROWS_MSG),
		GUTHANS(new int[] {3574, 3278, 3580, 3285, 0}, digType.BARROWS, new int[] {3534, 9704, 3}, BARROWS_MSG),
		VERACS(new int[] {3554, 3294, 3560, 3301, 0}, digType.BARROWS, new int[] {3568, 9683, 3}, BARROWS_MSG),
		TORAGS(new int[] {3550, 3279, 3556, 3285, 0}, digType.BARROWS, new int[] {3578, 9706, 3}, BARROWS_MSG),
		KARILS(new int[] {3562, 3272, 3569, 3278, 0}, digType.TELEPORT, new int[] {3556, 9718, 3}, BARROWS_MSG),
		;
		
		public int[] coords, data;
		public digType type;
		public String message;
		
		/**
		 * @param coordinates - The area you must be in for the dig to work = {swX, swY, neX, neY, Height}
		 * @param diggingType - The function for after the player digs here
		 * @param actionData - The data, if it's a teleport function, {X, Y, Height}
		 * @param actionData - The data, if it's an add item function, {Item ID, Amount}
		 * @param msg - The message the player gets when digging in the area
		 */
		private digAreas(int[] coordinates, digType diggingType, int[] actionData, String msg) {
			this.coords = coordinates;
			this.type = diggingType;
			this.data = actionData;
			this.message = msg;
		}
		
		/**
		 * Finds a function for the coordinates of the player when digging
		 * @param x : The x coordinate the player is in.
		 * @param y : The y coordinate the player is in.
		 */
		public static digAreas forID(int x, int y, int h) {
			digAreas k = null;
			for (digAreas b : digAreas.values()) {
				if (x >= b.coords[0] && y >= b.coords[1] && x <= b.coords[2] && y <= b.coords[3] && h == b.coords[4]) {
					k = b;
					break;
				}
			}
			return k;
		}
		
	}
	
	
	/**
	 * When a player clicks on the spade, it performs this cycle.
	 */
	public Digging(Player c) {
		if (System.currentTimeMillis() - c.lastDig < 1000) {
			return;
		}
		c.lastDig = System.currentTimeMillis();
		c.startAnimation(831);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
	    	@Override
	    	public void execute(CycleEventContainer e) {
	    		e.stop();
			}
	    	public void stop() {
	    		c.startAnimation(65535);
	    		dig(c);
	    	}
		}, 1);	
	}
	
	
	
	/**
	 * After the cycle is completed for digging, it searches for a digArea.
	 */
	private static void dig(Player c) {
		digAreas d = digAreas.forID(c.absX, c.absY, c.heightLevel);
		if (d == null) {
			c.sendMessage("Nothing interesting happens.");
			return;
		}
		switch(d.type) {
			case TELEPORT:
				int height = 0;
				if (d.data.length > 2) { height = d.data[2]; }
				c.getPA().movePlayer(d.data[0], d.data[1], height);
			break;
			case ADDITEM:
				int amount = 0;
				if (d.data.length > 1) { amount = d.data[1]; }
				ItemAssistant.addItem(c, d.data[0], amount);
			break;
			case BARROWS:
				height = 0;
				if (d.data.length > 2) { height = d.data[2]; }
				c.getPA().movePlayer(d.data[0], d.data[1], height);
				Barrows.hasDug(c);
			break;
		}
		if (d.message != null) { c.sendMessage(d.message); }
	}

}
