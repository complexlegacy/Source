package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.PlayerSave;
import ionic.player.content.commands.Command;

public class Recoloring implements Command {
	
	@Override
	public void processCommand(Client c, String command, int rights) {
		String[] colors = command.split("_");
		if (command.startsWith("sendcaperecolor")) {
			for (int i = 0; i < 7; i++) {
				c.compColor[i] = Integer.parseInt(colors[i + 1]);
			}
        	PlayerSave.saveGame(c);
		} else if (command.startsWith("recolorween")) {
			c.weenColor = Integer.parseInt(colors[1]);
		} else if (command.startsWith("recolorphat")) {
			c.phatColor = Integer.parseInt(colors[1]);
		} else if (command.startsWith("capergbcolors")) {
			for (int i = 1; i < colors.length; i++) {
				c.compColorsRGB[i - 1] = Integer.parseInt(colors[i]);
			}
			return;
		} else if (command.startsWith("setCompPreset")) {
			int preset = Integer.parseInt(colors[1]);
			for (int i = 0; i < 7; i++) {
				c.compPreset[preset][i] = Integer.parseInt(colors[i + 2]);
			}
			return;
		}
		c.updateRequired = true;
        c.setAppearanceUpdateRequired(true);
        c.handler.updatePlayer(c, c.outStream);
        c.sendMessage(":compu:");
	}
	

}
