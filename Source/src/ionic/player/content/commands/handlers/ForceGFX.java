package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.content.commands.Command;

public class ForceGFX implements Command {
	
	@Override
	public void processCommand(Client c, String command, int rights) {
		if (rights == 2) {
			try {
				String[] s = command.split(" ");
				int id = Integer.parseInt(s[1]);
				c.gfx0(id);
			} catch (Exception e) {
				c.sendMessage("invalid input");
			}
		}
	}
	
}