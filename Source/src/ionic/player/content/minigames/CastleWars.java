package ionic.player.content.minigames;

import core.Constants;
import utility.Misc;
import ionic.item.ItemAssistant;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.dialogue.Dialogue;

public class CastleWars {
	
	public static CastleWarsTeam zamorak = new CastleWarsTeam(4515, 4516);
	public static CastleWarsTeam saradomin = new CastleWarsTeam(4513, 4514);
	public static int MINIMUM_PLAYERS = 1;
	
	public static int minutes = 2;
	public static int seconds = 0;
	public static boolean gameStarted = false;
	
	public static void process() {
		tick();
		globalProcess();
	}
	public static void tick() {
		if (minutes <= 0 && seconds <= 0) {
			starting();
		} else {
			if (seconds <= 0 && minutes >= 0) {
				seconds = 60;
				minutes --;
			}
			seconds -= 3;
			if (seconds < 0)
				seconds = 0;
		}
	}
	public static void globalProcess() {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			Player c = PlayerHandler.players[i];
			if (c != null) {
				if (inWaitingRoom(c)) {
					String s = seconds < 10 ? "0"+seconds : ""+seconds;
					c.getPA().sendFrame126("Time until next game - "+minutes+":"+s+"", 27519);
				} else if (inRespawnPoint(c)) {
					c.getPA().sendFrame126("You have "+c.cwRespawnPointTime+" seconds to leave the respawn area", 12837);
					c.cwRespawnPointTime -= 3;
					if (c.cwRespawnPointTime <= 0) {
						forceKick(c);
					}
				}
			}
		}
	}
	
	public static void forceKick(Player c) {
		c.getPA().movePlayer(2443, 3090, 0);
		Dialogue.sendStatement2(c, new String[] {"You stayed in the respawn area for too long", "You were kicked from the castle wars game."});
		cleanUp(c);
	}

	public static void cleanUp(Player c) {
		c.cwRespawnPointTime = 120;
		c.cwTeam = null;
		c.inCastleWars = false;
	}
	
	public static void starting() {
		if (getWaitingSara() >= MINIMUM_PLAYERS && getWaitingZammy() >= MINIMUM_PLAYERS) {
			gameStarted = true;
			minutes = 15;
			seconds = 0;
			for (int i = 0; i < PlayerHandler.players.length; i++) {
				Player c = PlayerHandler.players[i];
				if (c != null) {
					if (inSaraWaitingRoom(c.absX, c.absY)) {
						c.inCastleWars = true;
						c.cwTeam = saradomin;
						c.cwRespawnPointTime = 120;
						c.getPA().movePlayer(2425 + Misc.random(3), 3075 + Misc.random(3), 1);
					} else if (inZammyWaitingRoom(c.absX, c.absY)) {
						c.inCastleWars = true;
						c.cwTeam = zamorak;
						c.cwRespawnPointTime = 120;
						c.getPA().movePlayer(2371 + Misc.random(3), 3129 + Misc.random(3), 1);
					}
				}
			}
		} else {
			for (int i = 0; i < PlayerHandler.players.length; i++) {
				Player c = PlayerHandler.players[i];
				if (c != null) {
					if (inWaitingRoom(c)) {
						c.sendMessage("There needs to be at least "+MINIMUM_PLAYERS+" players on each team, to start.");
					}
				}
			}
		}
		minutes = 2;
		seconds = 0;
	}
	
	public static void enterZamorak(Player c) {
		if (getWaitingZammy() <= getWaitingSara()) {
			if (canEnter(c)) {
				ItemAssistant.setEquipment(c, zamorak.cape, 1, Constants.CAPE_SLOT);
				ItemAssistant.setEquipment(c, zamorak.hood, 1, Constants.HEAD_SLOT);
				c.getPA().movePlayer(2421, 9524, 0);
			}
		} else {
			Dialogue.sendStatement2(c, new String[] {
					"The teams are unbalanced. Try joining the Saradomin team.", 
			"Or wait for somebody else to join that team."});
		}
	}

	public static void enterSaradomin(Player c) {
		if (getWaitingZammy() >= getWaitingSara()) {
			if (canEnter(c)) {
				ItemAssistant.setEquipment(c, saradomin.cape, 1, Constants.CAPE_SLOT);
				ItemAssistant.setEquipment(c, saradomin.hood, 1, Constants.HEAD_SLOT);
				c.getPA().movePlayer(2377, 9485, 0);
			}
		} else {
			Dialogue.sendStatement2(c, new String[] {
					"The teams are unbalanced. Try joining the Zamorak team.", 
			"Or wait for somebody else to join that team."});
		}
	}

	public static void enterGuthix(Player c) {
		if (getWaitingZammy() == getWaitingSara()) {
			int team = Misc.random(1);
			if (team == 0)
				enterSaradomin(c);
			if (team == 1)
				enterZamorak(c);
		} else if (getWaitingZammy() > getWaitingSara()) {
			enterSaradomin(c);
		} else if (getWaitingSara() > getWaitingZammy()) {
			enterZamorak(c);
		}
	}
	
	public static boolean canEnter(Player c) {
		int[] food = {10476, 315, 333, 329, 361, 379, 373, 7946, 385, 397, 7060, 391, 15272};
		boolean hasFood = false;
		boolean hasCape = false;
		boolean hasHelm = false;
		for (int i = 0; i < c.playerItems.length; i++) {
			for (int j = 0; j < food.length; j++) {
				if (c.playerItems[i] > 0) {
					if (c.playerItems[i] - 1 == food[j]) {
						hasFood = true;
					}
				}
			}
			if (c.playerItems[i] > 0) {
				int targetSlot = ItemAssistant.targetSlot(ItemAssistant.getItemName(c.playerItems[i] - 1).toLowerCase(), c.playerItems[i] - 1);
				if (targetSlot == Constants.HEAD_SLOT) {
					hasHelm = true;
				}
				if (targetSlot == Constants.CAPE_SLOT) {
					hasCape = true;
				}
			}
		}
		if (c.playerEquipment[Constants.HEAD_SLOT] > 0) {
			hasHelm = true;
		}
		if (c.playerEquipment[Constants.CAPE_SLOT] > 0) {
			hasCape = true;
		}
		String[] illegals = {"", "", "", ""};
		illegals[0] = "Not allowed:";
		int cur = 1;
		if (hasFood) {
			illegals[cur] = "-Food of any kind"; cur++;
		}
		if (hasCape) {
			illegals[cur] = "-Capes"; cur++;
		}
		if (hasHelm) {
			illegals[cur] = "-Helmets or hats"; cur++;
		}
		if (hasFood || hasCape || hasHelm) {
			Dialogue.sendStatement2(c, illegals);
			return false;
		}
		return true;
	}
	
	public static int getWaitingSara() {
		int players = 0;
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			Player c = PlayerHandler.players[i];
			if (c != null) {
				if (inSaraWaitingRoom(c.absX, c.absY)) {
					players++;
				}
			}
		}
		return players;
	}

	public static int getWaitingZammy() {
		int players = 0;
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			Player c = PlayerHandler.players[i];
			if (c != null) {
				if (inZammyWaitingRoom(c.absX, c.absY)) {
					players++;
				}
			}
		}
		return players;
	}
	
	public static void table(Player c, int giveItem, int obX, int obY) {
		if (c.inCastleWars) {
			c.turnPlayerTo(obX, obY);
			if (ItemAssistant.freeSlots(c) > 0) {
				c.startAnimation(832);
				ItemAssistant.addItem(c, giveItem, 1);
			}
		}
	}
	
	public static void clickObject(Player c, int object, int obX, int obY) {
		switch(object) {
		case 4388:
			enterZamorak(c);
			break;
		case 4408:
			enterGuthix(c);
			break;
		case 4387:
			enterSaradomin(c);
			break;
		case 4469:
		case 4470:
			if (inRespawnPoint(c)) {
				c.getPA().movePlayer(obX, obY, 1);
			}
			break;
		case 4458:
			if (inRespawnPoint(c))
				table(c, 4049, obX,obY);
			break;
			
			
		case 4415:
			if (obX == 2380 && obY == 3127) {
				c.getPA().movePlayer(2380, 3130, 0);
			} if (obX == 2419 && obY == 3080) {
				c.getPA().movePlayer(2419, 3077, 0);
			}
			break;
		case 4420:
			if (obX == 2382 && obY == 3131) {
				if (c.absX >= 2383) {
					c.getPA().movePlayer(2382, 3130, 0);
				} else {
					c.getPA().movePlayer(2383, 3133, 0);
				}
			}
			break;
			
		case 4417:
			if (obX == 2419 && obY == 3078) {
				c.getPA().movePlayer(2420, 3080, 1);
			}
			break;
		case 4418:
			if (obX == 2380 && obY == 3127) {
				c.getPA().movePlayer(2379, 3127, 1);
			}
			break;
			
		}
	}
	
	
	
	
	
	public static boolean inWaitingRoom(Player c) {
		return (inSaraWaitingRoom(c.absX, c.absY) || inZammyWaitingRoom(c.absX, c.absY));
	}
	public static boolean inWaitingRoom(int x, int y) {
		return (inSaraWaitingRoom(x, y) || inZammyWaitingRoom(x, y));
	}
	public static boolean inSaraWaitingRoom(int x, int y) {
		return (x >= 2369 && y >= 9478 && x <= 2394 && y <= 9498);
	}
	public static boolean inZammyWaitingRoom(int x, int y) {
		return (x >= 2409 && y >= 9510 && x <= 2431 && y <= 9535);
	}
	public static boolean inRespawnPoint(Player c) {
		if (c.absX >= 2368 && c.absY >= 3127 && c.absX <= 2376 && c.absY <= 3135 && c.heightLevel == 1)
			return true;
		if (c.absX >= 2423 && c.absY >= 3072 && c.absX <= 2431 && c.absY <= 3080 && c.heightLevel == 1)
			return true;
		return false;
	}
	public static boolean inCastleWars(Player c) {
		return (c.absX >= 2370 && c.absY >= 3072 && c.absX <= 2433 && c.absY <= 3140);
	}
	public static boolean inCastleWars(int x, int y) {
		return (x >= 2370 && y >= 3072 && x <= 2433 && y <= 3140);
	}
}