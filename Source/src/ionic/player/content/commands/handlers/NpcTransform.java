package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.content.commands.Command;

public class NpcTransform implements Command {
	
	@Override
	public void processCommand(Client c, String command, int rights) {
		if (rights == 2) {
			try {
				String[] s = command.split(" ");
				int transform = Integer.parseInt(s[1]);
				c.npcId2 = transform;
	            c.getPA().requestUpdates();
			} catch (Exception e) {
				c.sendMessage("invalid input");
			}
		}
	}
	
}