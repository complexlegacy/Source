package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.content.commands.Command;

public class PouchExamine implements Command {
	
	@Override
	public void processCommand(Client c, String command, int rights) {
		
		c.getPouch().tellAmount();
		
	}
	
}