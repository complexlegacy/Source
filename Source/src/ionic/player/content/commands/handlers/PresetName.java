package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.content.commands.Command;
import ionic.player.content.miscellaneous.Preset;

public class PresetName implements Command {

	@Override
	public void processCommand(Client c, String command, int rights) {
		String[] s = command.split("`");
		Preset.setName(c, Integer.parseInt(s[1]), s[2]);
	}
	
}
