package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.achievements.AchievementLoader;
import ionic.player.content.commands.Command;

public class ReloadAchievements implements Command {

	@Override
	public void processCommand(Client c, String command, int rights) {
		if (rights == 2) {
			new AchievementLoader();
			c.sendMessage("Reloaded achievements.");
		}
	}

}
