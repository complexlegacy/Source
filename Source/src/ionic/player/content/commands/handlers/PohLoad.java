package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.content.commands.Command;
import ionic.player.content.skills.construction.House;

/**
 * Created by Jon on 2/18/2016.
 */
public class PohLoad implements Command {
    @Override
    public void processCommand(Client c, String command, int rights) {
        c.setHouse(House.load(c));
        c.sendMessage("You have loaded your house.");


    }
}
