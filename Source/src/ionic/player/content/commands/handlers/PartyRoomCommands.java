package ionic.player.content.commands.handlers;

import ionic.item.ItemDegrade;
import ionic.player.Client;
import ionic.player.content.commands.Command;
import ionic.player.content.gambling.GamblingHandler;
import ionic.player.content.miscellaneous.PriceChecker;

public class PartyRoomCommands implements Command {
	
	@Override
	public void processCommand(Client c, String command, int rights) {
		
		String[] s = command.split(" ");
		int slot = Integer.parseInt(s[1]);
		int item = Integer.parseInt(s[2]);
		
		if (command.startsWith("addtoproomchest")) {
			int amount = Integer.parseInt(s[3]);
			c.getIA().add(item, slot, amount);
		} else if (command.startsWith("takefromproomchest")) {
			c.getIA().take(item, slot);
		} else if (command.startsWith("addtopricechecker")) {
			int amount = Integer.parseInt(s[3]);
			PriceChecker.add(c, item, slot, amount);
		} else if (command.startsWith("addtogamble")) {
			int amount = Integer.parseInt(s[3]);
			GamblingHandler.add(c, item, slot, amount);
		} else if (command.startsWith("repairitem")) {
			ItemDegrade.addToRepair(c, item, slot);
		}
		
	}
	

}
