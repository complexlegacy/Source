package ionic.player.content.commands.handlers;

import ionic.player.Client;
import ionic.player.PlayerHandler;
import ionic.player.content.commands.Command;
import ionic.player.content.commands.CommandHandler;
import ionic.player.content.miscellaneous.Tele;

public class Teleporting implements Command {
	
	@Override
	public void processCommand(Client c, String command, int rights) {
		if (rights >= 1 && rights <= 2) {
			String[] s = command.split(" ");
			
			if (command.startsWith("tele") && !command.startsWith("teleto") && 
					!command.startsWith("telen") && !command.startsWith("teleall")
					 && !command.startsWith("teletome")) {
				try {
					int x = Integer.parseInt(s[1]);
					int y = Integer.parseInt(s[2]);
					int h = 0;
					if (s.length > 3) {
						h = Integer.parseInt(s[3]);
					}
					c.getPA().movePlayer(x, y, h);
				} catch (Exception e) {
					c.sendMessage("invalid input");
				}
				return;
			}
			
			if (command.startsWith("telen")) {
				try {
					Tele t = Tele.forID(s[1]);
					if (t != null) {
						c.getPA().movePlayer(t.x, t.y, t.h);
					} else {
						c.sendMessage("That location was not found.");
					}
				} catch(Exception e) {
					c.sendMessage("invalid input");
				}
			}
			
			if (command.startsWith("teletome") || command.startsWith("xteletome") || command.startsWith("xteleme")) {
				try {
					String name = s[1];
					Client p = CommandHandler.getPlayerByName(name);
					if (p != null) {
						p.sendMessage("You have been force teleported to "+c.playerName+"");
						c.sendMessage("You teleport "+p.playerName+" to you");
						p.getPA().movePlayer(c.absX, c.absY, c.heightLevel);
					} else {
						c.sendMessage("No player online with that name.");
					}
				} catch (Exception e) {
					c.sendMessage("invalid input");
				}
				return;
			}
			
			if (command.startsWith("teleto") || command.startsWith("xteleto") || command.startsWith("xtelemeto")) {
				try {
					String name = s[1];
					Client p = CommandHandler.getPlayerByName(name);
					if (p != null) {
						c.sendMessage("You force yourself to teleport to "+p.playerName+"");
						c.getPA().movePlayer(p.absX, p.absY, p.heightLevel);
					} else {
						c.sendMessage("No player online with that name.");
					}
				} catch (Exception e) {
					c.sendMessage("invalid input");
				}
				return;
			}
			
			
			if ((command.startsWith("xteleall") || command.startsWith("teleall") || command.startsWith("alltome")) && rights == 3) {
				for (int i = 0; i < PlayerHandler.players.length; i++) {
					if (PlayerHandler.players[i] != null) {
						if (PlayerHandler.players[i] != c) {
							PlayerHandler.players[i].getPA().movePlayer(c.absX, c.absY, c.heightLevel);
							PlayerHandler.players[i].sendMessage(""+c.playerName+" has force teleported everyone to him");
						}
					}
				}
				c.sendMessage("You have mass teleported all players to you");
			}
			
			
		}
	}
	
}