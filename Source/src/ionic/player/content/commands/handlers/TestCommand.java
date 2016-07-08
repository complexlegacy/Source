package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.content.commands.Command;
import ionic.player.content.quest.cutscene.CutsceneHandler;
import ionic.player.content.quest.cutscene.Tutorial;

public class TestCommand implements Command {

	@Override
	public void processCommand(Client c, String command, int rights) {
		if (command.equals("test")) {
			CutsceneHandler.send(c, new Tutorial());
		} else {
			c.resetCamera();
			c.disableStuff = false;
		}
		
	}
	
	

}
