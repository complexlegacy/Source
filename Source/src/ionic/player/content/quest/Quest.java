package ionic.player.content.quest;

import ionic.player.Player;

/**
 * @author Keith
 */
public interface Quest {
	
	public abstract void loadDialogue();
	
	public abstract void clickQuestListButton(Player c);
	
	public abstract boolean talkToNpc(Player c, int npc);
	
	public abstract void setProgress(Player c, int amount);
	
	public abstract int getProgress(Player c);
	
	public abstract boolean meetsRequirements(Player c);

}
