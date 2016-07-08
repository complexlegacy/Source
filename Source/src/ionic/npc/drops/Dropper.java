package ionic.npc.drops;

import ionic.npc.NPC;
import ionic.npc.NPCHandler;
import ionic.npc.pet.BossPet;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.profiles.Profile;
import utility.Misc;
import core.Constants;
import core.Server;

public class Dropper {
	
	public static final int[] LOW_HERBS = { 199, 201, 203, 205, 207, 209 };
	public static final int[] HIGH_HERBS = { 207, 211, 213, 215, 217, 219, 2485 };
	public static final int[] BOTH_HERBS = { 199, 201, 203, 205, 207, 209, 207, 211, 213, 215, 217, 219, 2485 };
	
	/**
	 * When an npc dies, this gets called and handles dropping the items.
	 * @param n - The npc that died
	 * @param c - The player who killed that npc
	 */
	public Dropper(NPC n, Player c) {
		if (c == null) {
			return;
		}
		boolean[] type = new boolean[4];
		boolean ROW = c.playerEquipment[Constants.RING_SLOT] == 2572;
		if (n.drops != null) {
			if (n.drops.bones > 0) {
				Server.itemHandler.createGroundItem((Client)c, n.drops.bones, n.absX, n.absY, 1, c.playerId, false);
			}
			for (int i = 0; i < n.drops.drops.length; i++) {
				if (n.drops.drops[i] > 0 && n.drops.amounts[i] > 0 && n.drops.raritys[i] != null) {
					int roll = Misc.random(1000);
					int chance = n.drops.raritys[i].chance;
					if (rare(n.drops, i)) {
						if (ROW) {
							chance += Misc.random(getRowBonus(n.drops, i));
						}
						if (c.petSummoned) {
							if (BossPet.getByBoss(n.npcType) == BossPet.getByPet(c.petID)) {
								if (Misc.random(3) == 1) {
									chance += 1;
								}
							}
						}
					}
					if (roll < chance) {
						if (n.drops.raritys[i].type > 0) {
							if (!type[n.drops.raritys[i].type]) {
								int amount = Misc.random(n.drops.minAmounts[i], n.drops.amounts[i]);
								Server.itemHandler.createGroundItem((Client)c, n.drops.drops[i], n.absX, n.absY, amount, c.playerId, false);
								type[n.drops.raritys[i].type] = true;
								if (c.profile != null && rare(n.drops, i)) {
									Profile.newDropLog(c.profile, n.drops.drops[i], n.npcType);
								}
							}
						} else {
							int amount = Misc.random(n.drops.minAmounts[i], n.drops.amounts[i]);
							Server.itemHandler.createGroundItem((Client)c, n.drops.drops[i], n.absX, n.absY, amount, c.playerId, false);
						}
					}
				}
			}
			if (Misc.random(4) == 0) {
				if (n.drops.dropsHighHerbs && n.drops.dropsLowHerbs) {
					int herb = BOTH_HERBS[Misc.random(BOTH_HERBS.length -1)];
					Server.itemHandler.createGroundItem((Client)c, herb, n.absX, n.absY, 1, c.playerId, false);
					return;
				} else if (n.drops.dropsLowHerbs) {
					int herb = LOW_HERBS[Misc.random(LOW_HERBS.length -1)];
					Server.itemHandler.createGroundItem((Client)c, herb, n.absX, n.absY, 1, c.playerId, false);
					return;
				} else if (n.drops.dropsHighHerbs) {
					int herb = HIGH_HERBS[Misc.random(HIGH_HERBS.length -1)];
					Server.itemHandler.createGroundItem((Client)c, herb, n.absX, n.absY, 1, c.playerId, false);
					return;
				}
			}
		}
	}
	
	
	public static int getRowBonus(Drop d, int i) {
		if (d.raritys[i] == DropType.VERY_RARE) { return 3; }
		if (d.raritys[i] == DropType.SUPER_RARE) { return 2; }
		if (d.raritys[i] == DropType.EXTREMELY_RARE) { return 1; }
		if (d.raritys[i] == DropType.ALMOST_NEVER) { return 1; }
		return 0;
	}
	
	public static boolean rare(Drop d, int i) {
		if (d.raritys[i] == DropType.VERY_RARE
		 || d.raritys[i] == DropType.SUPER_RARE
		 || d.raritys[i] == DropType.EXTREMELY_RARE
		 || d.raritys[i] == DropType.ALMOST_NEVER) {
			return true;
		}
		return false;
	}
	
	
	
	
	

}
