package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.content.commands.Command;
import ionic.player.content.skills.construction.House;

/**
 * Created by Jon on 2/18/2016.
 */
public class PohEnter implements Command{

    @Override
    public void processCommand(Client c, String command, int rights) {
        if (c.getHouse() == null) {
            c.sendMessage("You do not have a house loaded.");
            return;
        }
        House house = c.getHouse();
        house.setBuildMode(false);
        house.enter(c);
    }
    }
