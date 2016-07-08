package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.content.commands.Command;
import ionic.player.dialogue.Dialogues;

public class EmptyInventory implements Command {
	
	@Override
	public void processCommand(Client c, String command, int rights) {
		
		Dialogues.send(c, Dialogues.EMPTY_INVENTORY);
		
	}
	
}