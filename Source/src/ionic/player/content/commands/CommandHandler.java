package ionic.player.content.commands;

import ionic.player.Client;
import ionic.player.PlayerHandler;

/**
 * @author Keith
 */
public class CommandHandler {
	
	/**
	 * Processes the command
	 */
	public static void processCommand(Client c, String command) {
		Command d = CommandNames.getCommandByName(command);
			d.processCommand(c, command, c.playerRights);
	}

	
	/**
	 * Finds a player by his name and returns client
	 */
	
	public static Client getPlayerByName(String name) {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].playerName.equalsIgnoreCase(name)) {
					return (Client) PlayerHandler.players[i];
				}
			}
		}
		return null;
	}
	
	
	
}
