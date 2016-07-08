package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.content.commands.Command;

/**
 * Created by Jon on 1/7/2016.
 */
public class GFX implements Command{
    @Override
    public void processCommand(Client c, String command, int rights) {
        String[] args = command.split(" ");
        c.gfx0(Integer.parseInt(args[1]));
    }
}
