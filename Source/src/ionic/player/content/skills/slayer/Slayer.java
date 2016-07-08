package ionic.player.content.skills.slayer;

import ionic.npc.NPC;
import ionic.npc.NPCData;
import ionic.player.Player;
import ionic.player.achievements.AchievementHandler;
import ionic.player.dialogue.Dialogue;
import ionic.player.dialogue.DialogueList;
import ionic.player.dialogue.DialogueType;
import ionic.player.dialogue.Dialogues;
import ionic.player.dialogue.Emotion;
import utility.Misc;
import core.Constants;

public class Slayer {
	
	public enum Difficulty {
		EASY(3, 1, new int[] {1,2,3,4,5,6}),
		MEDIUM(60, 1, new int[] {20,21,22,23,24,25,26,27,28,29,30,31}),
		HARD(90, 1, new int[] {40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57}),
		BOSS(115, 80, new int[] {70,71,72,73,74,75,76,77,78}),
		;
		public int cmbReq, slayReq;
		public int[] tasks;
		private Difficulty(int cmbLvlReq, int slayLvlReq, int[] tasks) {
			this.cmbReq = cmbLvlReq;
			this.slayReq = slayLvlReq;
			this.tasks = tasks;
		}
		
		public static Difficulty forID(int task) {
			for (Difficulty j : Difficulty.values()) {
				for (int k : j.tasks) {
					if (k == task) {
						return j;
					}
				}
			}
			return null;
		}
	}
	
	
	
	public enum Tasks {
		/** Easy tasks **/
		CRAWLING_HAND(1, new int[] {1648, 1649, 1650, 1651, 1652, 1653, 1654, 1655, 1656, 1657}, 30, 80, 5, null),
		CAVE_BUG(2, new int[] {1832}, 40, 80, 7, null),
		CAVE_CRAWLER(3, new int[] {1600, 1601, 1602, 1603}, 30, 80, 10, null),
		BANSHEE(4, new int[] {1612}, 30, 70, 15, "some Earmuffs, or a slayer helmet"),
		CAVE_SLIME(5, new int[] {1831}, 30, 70, 17, null),
		HILL_GIANT(6, new int[] {117}, 15, 40, 1, null),
		
		
		/** Medium tasks **/
		COCKATRICE(20, new int[] {1620, 1621}, 40, 80, 25, "a Mirror shield"),
		PYREFIEND(21, new int[] {1633, 1634, 1635, 1636}, 40, 80, 30, null),
		BASILISK(22, new int[] {1616, 1617}, 20, 50, 40, "a Mirror shield"),
		BLOODVELD(23, new int[] {1618, 1619}, 20, 50, 50, null),
		TUROTH(24, new int[] {1626, 1627, 1628, 1629, 1630, 1631, 1632}, 20, 50, 55, "a Leaf-bladed weapon"),
		HILL_GIANT_2(25, new int[] {117}, 25, 70, 1, null),
		MOSS_GIANT(26, new int[] {1587, 1588}, 25, 70, 1, null),
		FIRE_GIANT(27, new int[] {1582, 1583, 1584, 1585, 1586}, 25, 70, 1, null),
		LESSER_DEMON(28, new int[] {82}, 25, 70, 1, null),
		GREATER_DEMON(29, new int[] {83}, 25, 50, 1, null),
		GREEN_DRAGON(30, new int[] {941}, 20, 60, 1, null),
		BLUE_DRAGON(31, new int[] {55, 52}, 20, 60, 1, null),
		
		
		/** Hard tasks **/
		BASILISK_2(40, new int[] {1616, 1617}, 40, 100, 40, "a Mirror shield"),
		BLOODVELD_2(41, new int[] {1618, 1619}, 40, 80, 50, null),
		JELLY(42, new int[] {1637, 1638, 1639, 1640, 1641, 1642}, 45, 75, 1, null),
		TUROTH_2(43, new int[] {1626, 1627, 1628, 1629, 1630, 1631, 1632}, 40, 80, 55, "a Leaf-bladed weapon"),
		ABERRANT_SPECTRE(44, new int[] {1604, 1605, 1606, 1607}, 40, 80, 60, "a Nosepeg or a slayer helmet"),
		DUST_DEVIL(45, new int[] {1624}, 30, 70, 65, "a Facemask or a slayer helmet"),
		SMOKE_DEVIL(46, new int[] {1625}, 30, 60, 93, "a Facemask or a slayer helmet"),
		SKELETAL_WYVERN(47, new int[] {3070}, 30, 60, 72, "a Dragonfire shield"),
		NECHRYAEL(48, new int[] {1613}, 30, 60, 80, null),
		ABBY_DEMON(49, new int[] {1615}, 30, 60, 85, null),
		DARK_BEAST(50, new int[] {2783}, 30, 60, 90, null),
		GREATER_DEMON_2(51, new int[] {83}, 40, 80, 1, null),
		BLACK_DEMON(52, new int[] {84}, 40, 80, 1, null),
		RED_DRAGON(53, new int[] {53, 1589}, 20, 60, 1, null),
		BACK_DRAGON(54, new int[] {54}, 20, 60, 1, null),
		BRONZE_DRAGON(55, new int[] {1590}, 20, 60, 1, null),
		IRON_DRAGON(56, new int[] {1591}, 20, 60, 1, null),
		STEEL_DRAGON(57, new int[] {1592}, 20, 60, 1, null),
		
		/** Boss tasks **/
		BORK(70, new int[] {7133}, 5, 15, 1, null),
		GIANT_MOLE(71, new int[] {3340}, 5, 15, 1, null),
		KBD(72, new int[] {50}, 5, 15, 1, null),
		CORPOREAL_BEAST(73, new int[] {8133}, 5, 15, 1, null),
		JAD(74, new int[] {2745}, 1, 3, 1, null),
		GENERAL_GRAARDOR(75, new int[] {6260}, 5, 15, 1, null),
		COMMANDER_ZYLIANA(76, new int[] {6247}, 5, 15, 1, null),
		KREE_ARA(77, new int[] {6222}, 5, 15, 1, null),
		KRIL_TSUTAROTH(78, new int[] {6203}, 5, 15, 1, null),
		;
		
		public int taskId;
		public int min;
		public int max;
		public int req;
		public int[] npcId;
		public String extra;
		private Tasks(int taskId, int[] npcId, int min, int max, int req, String extra) {
			this.taskId = taskId;
			this.npcId = npcId;
			this.min = min;
			this.max = max;
			this.req = req;
			this.extra = extra;
		}
		
		public static Tasks forID(int id) {
			for (Tasks t: Tasks.values()) {
				if (t.taskId == id) {
					return t;
				}
			}
			return null;
		}
	}
	
	public static void kill(Player c, NPC npc) {
		c.taskAmount --;
		c.getPA().addSkillXP(npc.MaxHP * Constants.SLAYER_EXPERIENCE, Constants.SLAYER);
		if (c.taskAmount == 0) {
			if (Difficulty.forID(c.taskId) == Difficulty.EASY) {
				AchievementHandler.add(c, 18, "easy", 1);
			}
			c.slayerStreak ++;
			c.taskId = 0;
			c.sendMessage("You have completed your slayer task. Speak to Vannaka to get another one.");
			c.slayerPoints += c.slayerStreak;
			c.sendMessage("You receive "+c.slayerStreak+" slayer points. You now have "+c.slayerPoints+"");
			double xpBonus = (Math.sqrt(c.slayerStreak)) * 10000;
			int bonus = (int)xpBonus;
			c.sendMessage("You receive "+bonus+" slayer experience for completing the task.");
			c.getPA().addSkillXP(bonus, Constants.SLAYER);
			c.getPA().updatePlayerTab();
		}
	}
	
	public static void attemptAssign(Player c, Difficulty d) {
		if (c.skillLevel[Constants.SLAYER] < d.slayReq || c.combatLevel < d.cmbReq) {
			Dialogue.sendStatement2(c, new String[] {
					"You must have a slayer level of "+d.slayReq+",",
					"and a combat level of "+d.cmbReq+", to do this task."});
			return;
		}
		appendTask(c, d);
	}
	
	public static void appendTask(Player c, Difficulty d) {
		Tasks t = Tasks.forID(d.tasks[Misc.random(d.tasks.length - 1)]);
		checkTask(c, d, t);
	}
	
	public static void checkTask(Player c, Difficulty d, Tasks t) {
		if (c.skillLevel[Constants.SLAYER] < t.req) {
			appendTask(c, d);
			return;
		} else {
			c.taskId = t.taskId;
			c.taskAmount = Misc.random(t.min, t.max);
			tellTask(c, true);
		}
	}
	
	public static void tellTask(Player c, boolean justGiven) {
		Tasks t = Tasks.forID(c.taskId);
		if (t != null) {
			String name = NPCData.data[t.npcId[0]].name;
			if (justGiven) {
				if (t.extra == null) {
					Dialogue.sendNpcChat(c, "Vannaka", 1597, new String[] {
							"Your new task is to kill "+c.taskAmount+" "+name+"",}, Emotion.TALKING);
				} else {
					Dialogue.sendNpcChat(c, "Vannaka", 1597, new String[] {
							"Your new task is to kill "+c.taskAmount+" "+name+"",
							"You should have "+t.extra+" when killing those."}, Emotion.TALKING);
				}
			} else {
				if (t.extra == null) {
					Dialogue.sendNpcChat(c, "Vannaka", 1597, new String[] {
						"Your task is to kill "+c.taskAmount+" "+name+""}, Emotion.TALKING);
				} else {
					Dialogue.sendNpcChat(c, "Vannaka", 1597, new String[] {
							"Your task is to kill "+c.taskAmount+" "+name+"",
							"You should have "+t.extra+" or a slayer helmet when killing those."}, Emotion.TALKING);
				}
			}
		} else {
			Dialogue.sendNpcChat(c, "Vannaka", 1597, new String[] {
					"You don't currently have a task assigned."}, Emotion.NO);
		}
	}
	
	
	
	public static void resetTask(Player c) {
		if (c.taskId == 0) {
			c.getPA().closeAllWindows();
			c.sendMessage("You don't have a slayer task.");
			return;
		}
		Dialogues.send(c, 
		new DialogueList(new Dialogue[] {
			new Dialogue(DialogueType.NPC_CHAT, Emotion.UNSURE, "Vannaka", 1597, 
					new String[] {"Are you sure you want to reset your task?",
					"If you reset your task you will lose your task streak.",
					"your streak effects the amount of slayer points per task."}),
			new Dialogue(DialogueType.OPTIONS, new String[] {
					"I'm sure I want to reset my task", "No, I don't want to reset my task"
					},
				new Dialogue.Options() {@Override
					public void click(Player c, int option) {
						switch(option) {
						case 1:
							c.taskId = 0;
							c.taskAmount = 0;
							c.slayerStreak = 0;
							c.getPA().closeAllWindows();
							c.sendMessage("You no longer have a slayer task.");
							break;
						case 2:
							c.getPA().closeAllWindows();
							break;
						}
					}
			}),}));
	}
	

	
	
	
	
	
}
