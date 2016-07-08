package ionic.player.content.commands.handlers;

import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Client;
import ionic.player.content.commands.Command;
import ionic.player.content.skills.summoning.SummoningData;

public class Spawn implements Command {

	@Override
	public void processCommand(Client c, String command, int rights) {
		if (rights == 2) {
			if (!command.startsWith("itemn")) {
				try {
					String[] s = command.split(" ");
					int item = Integer.parseInt(s[1]);
					int amount = 1;
					try {
						if (s.length > 2) {
							amount = Integer.parseInt(s[2]);
						}
					} catch (NumberFormatException e) { amount = Integer.MAX_VALUE; }
					ItemAssistant.addItem(c, item, amount);
					c.sendMessage("Spawned item ID : "+item+" Name : "+ItemAssistant.getItemName(item)+" amount : "+amount+"");
				} catch (Exception E) {
					c.sendMessage("Invalid input");
				}
			} else {
				String[] s = command.split("-");
				String name = "";
				try {
					name = s[1];
				} catch (ArrayIndexOutOfBoundsException e) {
					c.sendMessage("use as ::itemn-name");
					return;
				}
				for (int i = 0; i < ItemData.data.length; i++) {
					if (ItemData.data[i] != null) {
						if (ItemData.data[i].getName().toLowerCase().contains(name.toLowerCase())) {
							if (!ItemData.data[i].isNoted()) {
								ItemAssistant.addItem(c, i, 1);
							}
						}
					}
				}
			}
		}
	}


}
