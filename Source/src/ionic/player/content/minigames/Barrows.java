package ionic.player.content.minigames;

import ionic.player.Client;
import ionic.player.Player;
import ionic.player.dialogue.Dialogues;
import core.Server;
import utility.Misc;

public class Barrows {
	
	public static final int ALIVE = 0, SPAWNED = 1, DEAD = 2, TUNNEL = 3;
	public static final int[][] BROTHERS = {
		{2025, 6821, 3557, 9699}, //Ahrims
		{2026, 6822, 3549, 9682}, //Dharoks
		{2027, 6773, 3538, 9703}, //Guthans
		{2028, 6771, 3553, 9715}, //Karils
		{2029, 6823, 3575, 9707}, //Torags
		{2030, 6772, 3568, 9686}, //Veracs
		};
	
	public static boolean stairs(Player c, int object) {
		switch (object) {
			case 6702://ahrims
				c.getPA().movePlayer(3565, 3288, 0);
				checkBrother(c);
			return true;
			case 6705://dhaoks
				c.getPA().movePlayer(3575, 3297, 0);
				checkBrother(c);
			return true;
			case 6706://veracs
				c.getPA().movePlayer(3556, 3297, 0);
				checkBrother(c);
			return true;
			case 6707://torags
				c.getPA().movePlayer(3553, 3282, 0);
				checkBrother(c);
			return true;
			case 6703://karils
				c.getPA().movePlayer(3566, 3274, 0);
				checkBrother(c);
			return true;
			case 6704://guthans
				c.getPA().movePlayer(3578, 3282, 0);
				checkBrother(c);
			return true;
		}
		return false;
	}
	
	public static void checkBrother(Player c) {
		if (c.brotherSpawned != null) {
			c.brotherSpawned.disappearTime = 2;
			int id = c.brotherSpawned.npcType;
			for (int i = 0; i < BROTHERS.length; i++) {
				if (BROTHERS[i][0] == id && c.barrowsData[i] == SPAWNED) {
					c.barrowsData[i] = ALIVE;
				}
			}
		}
	}
	
	public static boolean coffin(Player c, int obj) {
		for (int i = 0; i < BROTHERS.length; i++) {
			if (obj == BROTHERS[i][1]) {
				clickCoffin(c, i);
				return true;
			}
		}
		return false;
	}
	
	public static void clickCoffin(Player c, int id) {
		if (c.barrowsData[id] == ALIVE) {
			Server.npcHandler.spawnBarrows((Client)c, BROTHERS[id][0], BROTHERS[id][2], BROTHERS[id][3], 3, 32, 300, 200, false);
			c.barrowsData[id] = SPAWNED;
		} else if (c.barrowsData[id] == TUNNEL) {
			Dialogues.send(c, Dialogues.BARROWS_ENTER_TOMB);
			return;
		}
		c.sendMessage("You don't find anything.");
	}
	
	public static void enterTunnel(Player c) {
		c.getPA().movePlayer(3552, 9690, 0);
	}
	
	public static void hasDug(Player c) {
		if (noBarrows(c)) {
			int tunnel = Misc.random(5);
			c.barrowsData[tunnel] = TUNNEL;
			for (int i = 0; i < c.barrowsData.length; i++) {
				if (c.barrowsData[i] != TUNNEL) {
					c.barrowsData[i] = ALIVE;
				}
			}
		}
	}
	
	public static boolean noBarrows(Player c) {
		for (int i = 0; i < c.barrowsData.length; i++) {
			if (c.barrowsData[i] > -1) {
				return false;
			}
		}
		return true;
	}
	
	public static void resetBarrows(Player c) {
		for (int i = 0; i < c.barrowsData.length; i++) {
			c.barrowsData[i] = -1;
		}
		c.brotherSpawned = null;
	}

	public static void barrowsInterfaceUpdate(Player c) {
		hasDug(c);
		c.getPA().sendFrame126("Ahrim: "+getStatus(c.barrowsData[0]), 23502);
		c.getPA().sendFrame126("Dharok: "+getStatus(c.barrowsData[1]), 23503);
		c.getPA().sendFrame126("Guthan: "+getStatus(c.barrowsData[2]), 23504);
		c.getPA().sendFrame126("Karil: "+getStatus(c.barrowsData[3]), 23505);
		c.getPA().sendFrame126("Torag: "+getStatus(c.barrowsData[4]), 23506);
		c.getPA().sendFrame126("Verac: "+getStatus(c.barrowsData[5]), 23507);
	}
	
	public static String getStatus(int status) {
		String s = "@gre@Alive";
		if (status == 3) {
			s = "@cya@Tunnel";
		} else if (status == 2) {
			s = "@red@Dead";
		}
		return s;
	}
	
	

}
