package ionic.npc.drops;

import ionic.npc.NPCHandler;

public class Drop {
	
	public static Drop[] npcDrops = new Drop[NPCHandler.maxNPCDrops];
	
	/**
	 * The NPC types for the npcs that use this drop instance.
	 */
	public int npc;
	
	/**
	 * When the NPC dies, the bones it drops
	 */
	public int bones;
	
	/**
	 * If the NPC drops low level herbs
	 */
	public boolean dropsLowHerbs;
	
	/**
	 * If the NPC drops high level herbs
	 */
	public boolean dropsHighHerbs;
	
	/**
	 * The item drops, the raritys, the amounts
	 */
	public int[] drops;
	public int[] amounts;
	public int[] minAmounts;
	public DropType[] raritys;
	
	public Drop(int npc, int bones, boolean dropsLH, boolean dropsHH, int[] drops, int[] amounts, int[] minAmounts, DropType[] raritys) {
		this.npc = npc;
		this.bones = bones;
		this.dropsLowHerbs = dropsLH;
		this.dropsHighHerbs = dropsHH;
		this.drops = drops;
		this.amounts = amounts;
		this.raritys = raritys;
		this.minAmounts = minAmounts;
	}

}
