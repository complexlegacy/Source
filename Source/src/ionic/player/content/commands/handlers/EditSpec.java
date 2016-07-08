package ionic.player.content.commands.handlers;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.content.commands.Command;

public class EditSpec implements Command {
	
	@Override
	public void processCommand(Client c, String command, int rights) {
		if (rights == 2) {
			try {
				String[] s = command.split(" ");
				int amount = Integer.parseInt(s[1]);
				c.specAmount = (amount/10);
	            c.sendMessage("Your special amount is now "+amount+"%");
	            ItemAssistant.updateSpecialBar(c);
			} catch (Exception e) {
				c.sendMessage("invalid input");
			}
		}
	}
	
}