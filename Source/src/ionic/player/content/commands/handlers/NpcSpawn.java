package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.content.commands.Command;
import core.Server;

public class NpcSpawn implements Command {
	
	@Override
	public void processCommand(Client c, String command, int rights) {
		try {
			if (rights == 2) {
				String[] s = command.split(" ");
				int npcToSpawn = Integer.parseInt(s[1]);
				Server.npcHandler.spawnNpc(c, npcToSpawn, c.absX, c.absY + 1, c.heightLevel, 0, 50, 0, 0, 0, false, false);
			}
		} catch (Exception e) {
			c.sendMessage("invalid input");
		}
 	}
	
}