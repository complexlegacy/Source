package ionic.npc.pet;

import ionic.npc.NPCData;
import ionic.player.Player;

import java.util.HashMap;

public enum BossPet {
	CORPOREAL_YOUNGLING(3000, 8133, 139166, 35750, 100, 0,
			"To unlock this pet, you must kill the\\nCorporeal Beast a total of 100 times.",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\nthe "+getName(8133)+"."
			),
	BABY_FROST_DRAGON(3001, 51, 139167, 35751, 250, 1,
			"To unlock this pet, you must kill a\\ntotal of 250 frost dragons.",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\n"+getName(51)+"s."
			),
	PRINCE_BLACK_DRAGON(3002, 50, 139168, 35752, 200, 2,
			"To unlock this pet, you must kill the\\nKing Black Dragon a total of 200 times.",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\nthe "+getName(50)+"."
			),
	TORMENTED_BABY(3003, 8349, 139169, 35753, 200, 3,
			"To unlock this pet, you must defeat\\na total of 200 Tormented Demons.",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\n"+getName(8349)+"s."
			),
	BORK_JR(3004, 7133, 139170, 35754, 75, 4,
			"To unlock this pet, you must kill\\nBork a total of 75 times.",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\n"+getName(7133)+"."
			),
	GRAARDORS_SON(3005, 6260, 139171, 35755, 125, 5,
			"To unlock this pet, you must kill\\nGeneral Graardor a total of 125 times.",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\n"+getName(6260)+"."
			),
	ZILYANAS_MINION(3006, 6247, 139172, 35756, 125, 6,
			"To unlock this pet, you must kill\\nCommander Zilyana a total of 125 times.",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\n"+getName(6247)+"."
			),
	KRIL_JR(3007, 6203, 139173, 35757, 125, 7,
			"To unlock this pet, you must kill\\nK'ril Tsutaroth a total of 125 times.",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\n"+getName(6203)+"."
			),
	PUNY_KREEARRA(3008, 6222, 139174, 35758, 125, 8,
			"To unlock this pet, you must kill\\nKree'arra a total of 125 times.",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\n"+getName(6222)+"."
			),
	SUPREME_JR(3009, 2881, 139175, 35759, 100, 9,
			"To unlock this pet, you must kill the\\nDagannoth Supreme a total of 100 times.",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\nthe "+getName(2881)+"."
			),
	PRIME_JR(3010, 2882, 139176, 35760, 100, 10,
			"To unlock this pet, you must kill the\\nDagannoth Prime a total of 100 times.",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\nthe "+getName(2882)+"."
			),
	REX_JR(3011, 2883, 139177, 35761, 100, 11,
			"To unlock this pet, you must kill the\\nDagannoth Rex a total of 100 times.",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\nthe "+getName(2883)+"."
			),
	CHAOS_ELEMENTAL_JR(3012, 3200, 139178, 35762, 80, 12,
			"To unlock this pet, you must kill the\\nChaos Elemental a total of 80 times.",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\nthe "+getName(3200)+"."
			),
	KALPHITE_PRINCESS(3013, 3835, 139179, 35763, 125, 13,
			"To unlock this pet, you must kill the\\nKalphite Queen a total of 125 times",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\nthe "+getName(3835)+"."
			),
	KALPHITE_PRINCESS_BEE(3014, 3836, 139180, 35764, 125, 14,
			"To unlock this pet, you must kill the\\nKalphite Queen a total of 125 times",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\nthe "+getName(3836)+"."
			),
	BARRELCHEST_JR(3015, 5666, 139181, 35765, 150, 15,
			"To unlock this pet, you must kill\\nBarrelChest a total of 150 times.",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\n"+getName(5666)+"."
			),
	BABY_JAD(3016, 2745, 139182, 35766, 30, 16,
			"To unlock this pet, you must kill\\nJad a total of 30 times.",
			"?"
			),
	AHRIMS_PET(3017, 2025, 139183, 35767, 300, 17,
			"To unlock this pet, you must loot\\nthe Barrows Chest a total of 300 times.",
			"While this pet is following you, you will\\nhave a higher chance of loot from barrows."
			),
	DHAROKS_PET(3018, 2026, 139184, 35768, 300, 18,
			"To unlock this pet, you must loot\\nthe Barrows Chest a total of 300 times.",
			"While this pet is following you, you will\\nhave a higher chance of loot from barrows."
			),
	GUTHANS_PET(3019, 2027, 139185, 35769, 300, 19,
			"To unlock this pet, you must loot\\nthe Barrows Chest a total of 300 times.",
			"While this pet is following you, you will\\nhave a higher chance of loot from barrows."
			),
	KARILS_PET(3020, 2028, 139186, 35770, 300, 20,
			"To unlock this pet, you must loot\\nthe Barrows Chest a total of 300 times.",
			"While this pet is following you, you will\\nhave a higher chance of loot from barrows."
			),
	TORAGS_PET(3021, 2029, 139187, 35771, 300, 21,
			"To unlock this pet, you must loot\\nthe Barrows Chest a total of 300 times.",
			"While this pet is following you, you will\\nhave a higher chance of loot from barrows."
			),
	VERACS_PET(3022, 2030, 139188, 35772, 300, 22,
			"To unlock this pet, you must loot\\nthe Barrows Chest a total of 300 times.",
			"While this pet is following you, you will\\nhave a higher chance of loot from barrows."
			),
	MINI_NEX(3023, 13447, 139189, 35773, 275, 23,
			"To unlock this pet, you must kill\\nNex a total of 275 times.",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\n"+getName(13447)+"."
			),
	MOLE_RUNT(3024, 3340, 139190, 35774, 110, 24,
			"To unlock this pet, you must kill the\\nGiant Mole a total of 110 times.",
			"While this pet is following you, you will\\nhave a higher chance of getting drops from\\nthe "+getName(3340)+"."
			),
	;
	private int npcId, parentId, buttonId, textId, logSlot;
	public int killReq;
	private String unlock, perk;
	private BossPet(int npcId, int bossId, int buttonId, int textId, int killReq, int logSlot, String unlock, String perk) {
		this.npcId = npcId;
		this.parentId = bossId;
		this.buttonId = buttonId;
		this.textId = textId;
		this.killReq = killReq;
		this.logSlot = logSlot;
		this.unlock = unlock;
		this.perk = perk;
	}
	
	private static String getName(int boss) {
		return NPCData.data[boss].name;
	}
	
	private static HashMap<Integer, BossPet> boss = new HashMap<Integer, BossPet>();
	private static HashMap<Integer, BossPet> pets = new HashMap<Integer, BossPet>();
	private static HashMap<Integer, BossPet> buttons = new HashMap<Integer, BossPet>();
	
	static {
		for (BossPet d : BossPet.values()) {
			boss.put(d.parentId, d);
			pets.put(d.npcId, d);
			buttons.put(d.buttonId, d);
		}
	}
	
	public static BossPet getByPet(int p) {
		return pets.get(p);
	}
	
	public static BossPet getByBoss(int b) {
		return boss.get(b);
	}
	
	private static BossPet getByButton(int button) {
		return buttons.get(button);
	}
	
	public int getSlot() {
		return logSlot;
	}
	public int getId() {
		return npcId;
	}
	
	
	public static void openInterface(Player c) {
		for (BossPet b : BossPet.values()) {
			if (c.petKillLogs[b.getSlot()] >= b.killReq) {
				c.getPA().sendFrame126("unlocked", b.textId);
			} else {
				c.getPA().sendFrame126("locked", b.textId);
			}
		}
		c.getPA().showInterface(35730);
	}
	
	public static boolean clickButton(Player c, int button) {
		BossPet d = getByButton(button);
		if (d == null) {
			return false;
		}
		c.bosspetSelected = d;
		if (c.petKillLogs[d.getSlot()] >= d.killReq) {
			c.getPA().sendFrame126("", 35748);
		} else {
			c.getPA().sendFrame126("You have not unlocked this pet", 35748);
		}
		c.getPA().sendFrame126(d.unlock, 35742);
		c.getPA().sendFrame126(d.perk, 35744);
		c.getPA().sendFrame126(""+c.petKillLogs[d.getSlot()]+"/"+d.killReq, 35746);
		return true;
	}

}
