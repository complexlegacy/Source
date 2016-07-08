package ionic.player.content.quest;

import ionic.player.Player;
import ionic.player.content.quest.QuestHandler.QuestList;

/**
 * @author Keith
 */
public class QuestTab {
	
	
	public static void updateQuestTab(Player c) {
		for (QuestList q : QuestList.values()) {
			c.getPA().sendFrame126(""+progressColor(c, q)+""+q.getName()+"", 16726 + q.getId());
		}
	}
	
	private static final String progressColor(Player c, QuestList q) {
		if (c.questProgress[q.getId()] >= 100)
			return "@gre@";
		if (c.questProgress[q.getId()] >= 1)
			return "@yel@";
		return "@red@";
	}
	

}
