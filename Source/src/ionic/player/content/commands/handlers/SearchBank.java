package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.banking.BankHandler;
import ionic.player.content.commands.Command;

public class SearchBank implements Command {
	
	@Override
	public void processCommand(Client c, String command, int rights) {
		String[] s = command.split("`");
		String search = s[1];
		BankHandler.sendSearch(c, search);
	}
	
}