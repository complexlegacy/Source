package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.content.commands.Command;

public class ForceAnimation implements Command {
	
	@Override
	public void processCommand(Client c, String command, int rights) {
		if (rights == 2) {
			try {
				String[] s = command.split(" ");
				int id = Integer.parseInt(s[1]);
				c.startAnimation(id);
			} catch (Exception e) {
				c.sendMessage("invalid input");
			}
		}
	}
	
}