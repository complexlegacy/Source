package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.content.commands.Command;

/**
 * Created by Jon on 12/23/2015.
 */
public class OpenBank implements Command {
    @Override
    public void processCommand(Client c, String command, int rights) {
        if (rights == 2) {
            c.getPA().openUpBank(0);
        }
    }
}
