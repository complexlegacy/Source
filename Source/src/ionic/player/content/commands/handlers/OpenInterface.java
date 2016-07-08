package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.content.commands.Command;
import ionic.player.profiles.Profile;
import ionic.player.profiles.ProfileSend;

public class OpenInterface implements Command {

	@Override
	public void processCommand(Client c, String command, int rights) {
		String[] s = command.split(" ");
		if (command.startsWith("profilesearch")) {
			Profile p = Profile.search(s[1]);
			if (p != null) {
				c.sendMessage("You are now viewing the profile of: "+p.name);
				ProfileSend.send(p, c);
			} else {
				if (command.startsWith("profilesearch2")) {
					c.sendMessage("This player doesn't have a profile.");
				} else {
					c.sendMessage("No profile found for the name: "+s[1]);
				}
			}
		} else {
			if (rights == 2) {
				try {
					int id = Integer.parseInt(s[1]);
					c.getPA().showInterface(id);
				} catch (Exception e) {
					c.sendMessage("invalid input");
				}
			}
		}
	}

}