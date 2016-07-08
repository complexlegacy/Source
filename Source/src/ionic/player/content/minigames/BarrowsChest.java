package ionic.player.content.minigames;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.Player;
import utility.Misc;
import core.Server;

public class BarrowsChest {
	
	
	public BarrowsChest(Player c) {
		int id = hasTunnelBrother(c);
		if (id > -1) {
			c.barrowsData[hasTunnelBrother(c)] = Barrows.SPAWNED;
			Server.npcHandler.spawnBarrows((Client)c, Barrows.BROTHERS[id][0], 3552, 
					9694, 0, 32, 300, 220, true);
			return;
		} else {
			loot(c);
		}
	}
	
	public static void looted(Player c) {
		c.chestLoots ++;
		c.getPA().updatePlayerTab();
		c.getPA().movePlayer(c.tunnelX, c.tunnelY, 3);
	}
	
	public void loot(Player c) {
		if (killed(c) == 0) { 
			c.sendMessage("The chest is empty.");
			return;
		}
		checkArmour(c, killed(c));
		int runes = Misc.random(5);
		int mra = 0;
		int bra = 0;
		int dra = 0;
		switch(runes) {
		case 0:
			mra = (int)(100 + Misc.random(900));
			break;
		case 1:
			bra = (int)(10 + Misc.random(60));
			break;
		case 2:
			dra = (int)(20 + Misc.random(120));
			break;
		case 3:
			mra = (int)(100 + Misc.random(600));
			dra = (int)(20 + Misc.random(100));
			break;
		case 4:
			mra = (int)(100 + Misc.random(600));
			bra = (int)(20 + Misc.random(70));
			break;
		case 5:
			mra = (int)(100 + Misc.random(600));
			bra = (int)(20 + Misc.random(70));
			dra = (int)(30 + Misc.random(100));
			break;
		}
		if (mra > 0)
			ItemAssistant.addItem(c, 558, mra);
		if (bra > 0)
			ItemAssistant.addItem(c, 565, bra);
		if (dra > 0)
			ItemAssistant.addItem(c, 560, dra);
		if (killedKaril(c)) {
			int b = Misc.random(2);
			if (b == 0) {
				ItemAssistant.addItem(c, 4740, 20 + Misc.random(50));
			}
		}
		Barrows.resetBarrows(c);
		looted(c);
	}
	
	public void checkArmour(Player c, int killed) {
		int h = 100;
		int l = 1;
		switch(killed) {
		case 0: return;
		case 1: l = 6; break;
		case 2: l = 8; break;
		case 3: l = 12; break;
		case 4: l = 16; break;
		case 5: l = 20; break;
		case 6: l = 25; break;
		}
		boolean give = (Misc.random(h)) <= l;
		if (give) {
			giveArmour(c);
		}
	}
	
	
	public static final int[][] ARMOURS = {
		{4708, 4710, 4712, 4714}, //Ahrims
		{4716, 4718, 4720, 4722}, //Dharoks
		{4724, 4726, 4728, 4730}, //Guthans
		{4732, 4734, 4736, 4738}, //Karils
		{4745, 4747, 4749, 4751}, //Torags
		{4753, 4755, 4757, 4759}, //Veracs
		};
	
	public void giveArmour(Player c) {
		int p = 0;
		for (int i = 0; i < c.barrowsData.length; i++) {
			if (c.barrowsData[i] == Barrows.DEAD) {
				p += 4;
			}
		}
		int[] armourParts = new int[p];
		int d = 0;
		for (int i = 0; i < c.barrowsData.length; i++) {
			if (c.barrowsData[i] == Barrows.DEAD) {
				for (int j = 0; j < 4; j++) {
					armourParts[d] = ARMOURS[i][j];
					d++;
				}
			}
		}
		int getRandomPart = armourParts[Misc.random(armourParts.length - 1)];
		ItemAssistant.addItem(c, getRandomPart, 1);
		boolean two = (Misc.random(10) == 3);
		if (two) {
			getRandomPart = armourParts[Misc.random(armourParts.length - 1)];
			ItemAssistant.addItem(c, getRandomPart, 1);
		}
	}
	
	public boolean killedKaril(Player c) {
		if (c.barrowsData[3] == Barrows.DEAD) {
			return true;
		}
		return false;
	}
	
	public int killed(Player c) {
		int k = 0;
		for (int i = 0; i < c.barrowsData.length; i++) {
			if (c.barrowsData[i] == Barrows.DEAD) {
				k++;
			}
		}
		return k;
	}
	
	public int hasTunnelBrother(Player c) {
		for (int i = 0; i < c.barrowsData.length; i++) {
			if (c.barrowsData[i] == Barrows.TUNNEL) {
				return i;
			}
		}
		return -1;
	}
	
	

}
