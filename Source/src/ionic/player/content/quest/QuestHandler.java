package ionic.player.content.quest;

import ionic.player.Player;
import ionic.player.content.quest.impl.*;

/**
 * @author Keith
 */
public class QuestHandler {
	
	public static void loadQuestDialogues() {
		for (QuestList q : QuestList.values()) {
			q.getQuest().loadDialogue();
		}
	}
	
	public static void questText(Player c, String line, int frame, boolean done) {
		if (done)
			line = "@str@"+line;
		c.getPA().sendFrame126(line, frame);
	}
	
	public static boolean requiredLevel(Player c, int skill, int required) {
		return c.getLevelForXP(c.playerXP[skill]) >= required;
	}

	public enum QuestList {
		THE_STOLEN_CANNON(0, "The Stolen Cannon", new StolenCannon(), 65086),
		;

		private final String name;
		private final Quest quest;
		private final int questId, buttonId;

		private QuestList(final int questSlot, final String questName, final Quest questClass, final int actionButtonId) {
			this.name = questName;
			this.quest = questClass;
			this.questId = questSlot;
			this.buttonId = actionButtonId;
		}

		public String getName() {
			return name;
		}
		public Quest getQuest() {
			return quest;
		}
		public int getId() {
			return questId;
		}
		public int getButton() {
			return buttonId;
		}
	}

}
