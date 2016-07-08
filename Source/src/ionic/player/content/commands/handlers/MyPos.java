package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.content.commands.Command;

public class MyPos implements Command {
	
	@Override
	public void processCommand(Client c, String command, int rights) {
		c.sendMessage("Your coordinates: X:"+c.absX+" Y:"+c.absY+" Height:"+c.heightLevel+"");
	}
	
}