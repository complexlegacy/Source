package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.content.commands.Command;
import ionic.player.content.miscellaneous.Tele;

public class TeleportHome implements Command {
	
	@Override
	public void processCommand(Client c, String command, int rights) {
		c.getPA().tele("spell", Tele.HOME);
	}
	
}