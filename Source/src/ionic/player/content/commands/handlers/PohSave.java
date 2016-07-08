package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.content.commands.Command;
import ionic.player.content.skills.construction.House;

/**
 * Created by Jon on 2/18/2016.
 */
public class PohSave implements Command {
    public House house;
    @Override
    public void processCommand(Client c, String command, int rights) {
        if (c.getHouse() != null)
            c.getHouse().save();
        c.sendMessage("You successfully save your house.");
    }
}
