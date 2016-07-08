package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.banking.BankHandler;
import ionic.player.content.commands.Command;

public class ChangeBankTabName implements Command {

	@Override
	public void processCommand(Client c, String command, int rights) {
		
		String[] s = command.split("`");
		int slot = Integer.parseInt(s[1]);
		String name = s[2];
		
		c.tabNames[slot] = name;
		BankHandler.updateTabNames(c);
		
	}
	
}