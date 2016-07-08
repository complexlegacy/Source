package ionic.player.content.commands;

import ionic.player.Client;

public interface Command {
	public void processCommand(Client c, String command, int rights);
}