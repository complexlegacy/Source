package ionic.player.content.commands.handlers;

import ionic.item.ItemData;
import ionic.player.Client;
import ionic.player.content.commands.Command;

public class GetItemByName implements Command {

	@Override
	public void processCommand(Client c, String command, int rights) {
		if (rights == 2) {
			String a[] = command.split(" ");
			String name = "";
			int results = 0;
			for(int i = 1; i < a.length; i++)
				name = name + a[i]+ " ";
			name = name.substring(0, name.length()-1);
			c.sendMessage("Searching: " + name);
			for (int j = 0; j < ItemData.data.length; j++) {
				if (ItemData.data[j] != null)
					if (ItemData.data[j].getName().toLowerCase().contains(name.toLowerCase())) {
						c.sendMessage("@red@ "+ItemData.data[j].getName() +" - "+ItemData.data[j].getId()+"");
						results++;
					}
			}
			c.sendMessage(results + " results found...");
		}
	}

}
