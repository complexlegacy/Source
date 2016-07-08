package ionic.player.dialogue;

import ionic.player.Player;

public class Dialogue {
	
	public String npcName;
	public int talkingNpc;
	public String[] texts;
	public Emotion emotion;
	public DialogueType type;
	public Options options = null;
	public Actions actions = null;
	
	/**
	 * For npc chat
	 * @param t - the type of dialogue
	 * @param e - the emotion of the chathead in the chatbox
	 * @param npc - the name of the talking npc
	 * @param npcId - the id of the talking npc
	 * @param lines - the text to show on the chat interface
	 */
	public Dialogue(DialogueType t, Emotion e, String npc, int npcId, String[] lines) {
		this.type = t;
		this.emotion = e;
		this.npcName = npc;
		this.talkingNpc = npcId;
		this.texts = lines;
	}
	public Dialogue(DialogueType t, Emotion e, String npc, int npcId, String[] lines, Actions a) {
		this.type = t;
		this.emotion = e;
		this.npcName = npc;
		this.talkingNpc = npcId;
		this.texts = lines;
		this.actions = a;
	}
	
	
	
	/**
	 * For player chat
	 * @param t - the type of dialogue
	 * @param e - the emotion of the chathead in the chatbox
	 * @param lines - the text to show on the chat interface
	 */
	public Dialogue(DialogueType t, Emotion e, String[] lines) {
		this.type = t;
		this.emotion = e;
		this.texts = lines;
	}
	public Dialogue(DialogueType t, Emotion e, String[] lines, Actions a) {
		this.type = t;
		this.emotion = e;
		this.texts = lines;
		this.actions = a;
	}
	
	
	
	
	
	/**
	 * For statement dialogues
	 * @param t - the type of dialogue
	 * @param lines - the text to show on the chat interface
	 */
	public Dialogue(DialogueType t, String[] lines) {
		this.type = t;
		this.texts = lines;
	}
	public Dialogue(DialogueType t, String[] lines, Actions a) {
		this.type = t;
		this.texts = lines;
		this.actions = a;
	}
	
	
	
	
	/**
	 * For option dialogues
	 * @param t - the type of dialogue
	 * @param lines - the text to show on the chat interface
	 */
	public Dialogue(DialogueType t, String[] lines, Options o) {
		this.type = t;
		this.texts = lines;
		this.options = o;
	}
	
	
	
	
	
	
	public interface Options {
		public abstract void click(Player c, int option);
	}
	public interface Actions {
		public abstract void perform(Player c);
	}
	
	
	
	
	
	
	/**
	 * Sends this instanced dialogue to the player.
	 * @param c - The player who is getting the dialogue
	 */
	public void sendDialogue(Player c) {
		switch (type) {
			case NPC_CHAT:
				sendNpcChat(c, npcName, talkingNpc, texts, emotion);
			break;
			case PLAYER_CHAT:
				sendPlayerChat(c, texts, emotion);
			break;
			case OPTIONS:
				sendOptions(c, texts);
			break;
			case STATEMENT:
				sendStatement(c, texts);
			break;
		}
	}
	
	
	
	
	public static void sendNpcChat(Player c, String npcName, int npcTalking, String[] lines, Emotion e) {
		for (int i = 0; i < lines.length; i++) {
			lines[i] = lines[i].replaceAll("$name", c.playerName);
		}
		switch(lines.length) {
			case 1:
				c.getPA().sendFrame200(4883, e.get());
		        c.getPA().sendFrame126(npcName, 4884);
		        c.getPA().sendFrame126(lines[0], 4885);
		        c.getPA().sendFrame75(npcTalking, 4883);
		        c.getPA().sendFrame164(4882);
			break;
			case 2:
				c.getPA().sendFrame200(4888, e.get());
		        c.getPA().sendFrame126(npcName, 4889);
		        c.getPA().sendFrame126(lines[0], 4890);
		        c.getPA().sendFrame126(lines[1], 4891);
		        c.getPA().sendFrame75(npcTalking, 4888);
		        c.getPA().sendFrame164(4887);
			break;
			case 3:
				c.getPA().sendFrame200(4894, e.get());
		        c.getPA().sendFrame126(npcName, 4895);
		        c.getPA().sendFrame126(lines[0], 4896);
		        c.getPA().sendFrame126(lines[1], 4897);
		        c.getPA().sendFrame126(lines[2], 4898);
		        c.getPA().sendFrame75(npcTalking, 4894);
		        c.getPA().sendFrame164(4893);
			break;
			case 4:
				c.getPA().sendFrame200(4901, e.get());
		        c.getPA().sendFrame126(npcName, 4902);
		        c.getPA().sendFrame126(lines[0], 4903);
		        c.getPA().sendFrame126(lines[1], 4904);
		        c.getPA().sendFrame126(lines[2], 4905);
		        c.getPA().sendFrame126(lines[3], 4906);
		        c.getPA().sendFrame75(npcTalking, 4901);
		        c.getPA().sendFrame164(4900);
			break;
		}
	}
	
	public static void sendPlayerChat(Player c, String[] lines, Emotion e) {
		switch(lines.length) {
			case 1:
				c.getPA().sendFrame200(969, e.get());
		        c.getPA().sendFrame126(c.playerName, 970);
		        c.getPA().sendFrame126(lines[0], 971);
		        c.getPA().sendFrame185(969);
		        c.getPA().sendFrame164(968);
			break;
			case 2:
				c.getPA().sendFrame200(974, e.get());
		        c.getPA().sendFrame126(c.playerName, 975);
		        c.getPA().sendFrame126(lines[0], 976);
		        c.getPA().sendFrame126(lines[1], 977);
		        c.getPA().sendFrame185(974);
		        c.getPA().sendFrame164(973);
			break;
			case 3:
				c.getPA().sendFrame200(980, e.get());
		        c.getPA().sendFrame126(c.playerName, 981);
		        c.getPA().sendFrame126(lines[0], 982);
		        c.getPA().sendFrame126(lines[1], 983);
		        c.getPA().sendFrame126(lines[2], 984);
		        c.getPA().sendFrame185(980);
		        c.getPA().sendFrame164(979);
			break;
			case 4:
				c.getPA().sendFrame200(987, e.get());
		        c.getPA().sendFrame126(c.playerName, 988);
		        c.getPA().sendFrame126(lines[0], 989);
		        c.getPA().sendFrame126(lines[1], 990);
		        c.getPA().sendFrame126(lines[2], 991);
		        c.getPA().sendFrame126(lines[3], 992);
		        c.getPA().sendFrame185(987);
		        c.getPA().sendFrame164(986);
			break;
		}
	}
	
	public static void sendOptions(Player c, String[] lines) {
		switch (lines.length) {
			case 2:
				c.getPA().sendFrame126("Select an Option", 2460);
		        c.getPA().sendFrame126(lines[0], 2461);
		        c.getPA().sendFrame126(lines[1], 2462);
		        c.getPA().sendFrame164(2459);
			break;
			case 3:
				c.getPA().sendFrame126("Select an Option", 2470);
		        c.getPA().sendFrame126(lines[0], 2471);
		        c.getPA().sendFrame126(lines[1], 2472);
		        c.getPA().sendFrame126(lines[2], 2473);
		        c.getPA().sendFrame164(2469);
			break;
			case 4:
				c.getPA().sendFrame126("Select an Option", 2481);
		        c.getPA().sendFrame126(lines[0], 2482);
		        c.getPA().sendFrame126(lines[1], 2483);
		        c.getPA().sendFrame126(lines[2], 2484);
		        c.getPA().sendFrame126(lines[3], 2485);
		        c.getPA().sendFrame164(2480);
			break;
			case 5:
				c.getPA().sendFrame126("Select an Option", 2493);
		        c.getPA().sendFrame126(lines[0], 2494);
		        c.getPA().sendFrame126(lines[1], 2495);
		        c.getPA().sendFrame126(lines[2], 2496);
		        c.getPA().sendFrame126(lines[3], 2497);
		        c.getPA().sendFrame126(lines[4], 2498);
		        c.getPA().sendFrame164(2492);
			break;
		}
	}
	public static void sendStatement2(Player c, String[] lines) {
		c.dialogues = null;
		c.statementCloses = true;
		sendStatement(c, lines);
	}
	/**
	 * sends a statement to a player, but doesn't close open interface
	 */
	public static void sendStatement3(Player c, String[] lines) {
		c.dialogues = null;
		c.statementCloses = false;
		sendStatement(c, lines);
		c.statementCloses = true;
	}
	private static void sendStatement(Player c, String[] lines) {
		switch (lines.length) {
		case 1:
			c.getPA().sendFrame126(lines[0], 357);
			c.getPA().sendFrame164(356);
			break;
		case 2:
			c.getPA().sendFrame126(lines[0], 360);
			c.getPA().sendFrame126(lines[1], 361);
			c.getPA().sendFrame164(359);
			break;
		case 3:
			c.getPA().sendFrame126(lines[0], 364);
			c.getPA().sendFrame126(lines[1], 365);
			c.getPA().sendFrame126(lines[2], 366);
			c.getPA().sendFrame164(363);
			break;
		case 4:
			c.getPA().sendFrame126(lines[0], 369);
			c.getPA().sendFrame126(lines[1], 370);
			c.getPA().sendFrame126(lines[2], 371);
			c.getPA().sendFrame126(lines[3], 372);
			c.getPA().sendFrame164(368);
			break;
		case 5:
			c.getPA().sendFrame126(lines[0], 375);
			c.getPA().sendFrame126(lines[1], 376);
			c.getPA().sendFrame126(lines[2], 377);
			c.getPA().sendFrame126(lines[3], 378);
			c.getPA().sendFrame126(lines[4], 379);
			c.getPA().sendFrame164(374);
			break;
		}
	}

}
