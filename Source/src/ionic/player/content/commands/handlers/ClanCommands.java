package ionic.player.content.commands.handlers;

import ionic.clans.Clan;
import ionic.clans.ClanHandler;
import ionic.clans.ClanSaver;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.content.commands.Command;

import java.io.File;

public class ClanCommands implements Command {
	
	@Override
	public void processCommand(Client c, String command, int rights) {
		String[] cmd = command.split("`");
		switch(cmd[1]) {
			case "kickplayer":
				if (c.clan != null) {
					if (ClanHandler.canKick(c, c.clan)) {
						String n = cmd[2];
						Player toKick = null;
						for (int i = 0; i < c.clan.members.length; i++) {
							if (c.clan.members[i] != null) {
								if (c.clan.members[i].playerName.toLowerCase().equals(n.toLowerCase())) {
									toKick = c.clan.members[i];
								}
							}
						}
						if (toKick == null) {
							return;
						}
						if (toKick == c) {
							c.sendMessage("You can't kick yourself..");
							return;
						}
						if (ClanHandler.getRank(toKick, c.clan) >= ClanHandler.getRank(c, c.clan)) {
							c.sendMessage("You can only kick players who are lower rank than you");
							return;
						}
						int ks = ClanHandler.getKickSlot(c.clan);
						c.clan.kicked[ks] = toKick.playerName;
						c.clan.kickTicks[ks] = 21;
						ClanHandler.kickMessage(c, toKick);
						ClanHandler.leaveClan(toKick, "You have been kicked from the channel.", false);
					} else {
						c.sendMessage("You're not allowed to kick players.");
					}
				}
				break;
			case "setclanprefix":
				if (c.clanOwned == null) {
					int slot = ClanHandler.getSlot();
					if (slot == -1) { return; }
					Clan clan = new Clan(slot, c.playerName, cmd[2], new String[100], new int[100], 0, 0, 6);
					ClanHandler.clans[slot] = clan;
					c.clanOwned = clan;
					if (c.clan == null){
						ClanHandler.joinClan(c, c.playerName);
					} else {
						c.sendMessage("You have created your clan chat!");
					}
					ClanHandler.friendsToRanks(c);
					new ClanSaver(c.clanOwned);
				} else {
					c.clanOwned.prefix = cmd[2];
					new ClanSaver(c.clanOwned);
				}
				ClanHandler.updateSetupInterface(c);
				if (c.clanOwned != null) {
					ClanHandler.sendClanTabUpdate(c.clanOwned);
				}
			break;
			
			case "setplayerrank":
				if (c.clanOwned == null) {
					c.sendMessage("You must have a clan before doing this.");
					return;
				}
				for (int i = 0; i < 100; i++) {
					if (c.clanOwned.ranks[i] != null) {
						if (c.clanOwned.ranks[i].toLowerCase().equals(cmd[2].toLowerCase())) {
							c.clanOwned.ranksN[i] = Integer.parseInt(cmd[3]);
							break;
						}
					}
				}
				c.sendClanRanks();
				ClanHandler.sendClanTabUpdate(c.clanOwned);
			break;
			
			case "disableclan":
				if (c.clanOwned != null) {
					for (int i = 0; i < 100; i++) { c.clanOwned.ranksN[i] = 0; }
					c.sendClanRanks();
					try {
						File file = new File("./data/clans/"+c.clanOwned.clanId+".txt");
						file.delete();
					} catch (Exception e) { }
					for (int i = 0; i < 100; i++) {
						if (c.clanOwned.members[i] != null) {
							ClanHandler.leaveClan(c.clanOwned.members[i], 
									"This clan chat has been disabled by the owner.", true);
						}
					}
					ClanHandler.clans[c.clanOwned.clanId] = null;
					c.clanOwned = null;
					ClanHandler.updateSetupInterface(c);
					c.sendMessage("You no longer own a clan chat.");
				} else {
					c.sendMessage("You must own a clan before doing this.");
				}
			break;
			
			case "setwhocanenter":
				if (c.clanOwned != null) {
					c.clanOwned.whoCanEnter = Integer.parseInt(cmd[2]);
					ClanHandler.updateSetupInterface(c);
				} else {
					c.sendMessage("You must have a clan before doing this.");
				}
			break;
			
			case "setwhocantalk":
				if (c.clanOwned != null) {
					c.clanOwned.whoCanTalk = Integer.parseInt(cmd[2]);
					ClanHandler.updateSetupInterface(c);
				} else {
					c.sendMessage("You must have a clan before doing this.");
				}
			break;
			
			case "setwhocankick":
				if (c.clanOwned != null) {
					c.clanOwned.whoCanKick = Integer.parseInt(cmd[2]);
					ClanHandler.updateSetupInterface(c);
					for (int i = 0; i < 100; i++) {
						if (c.clanOwned.members[i] != null) {
							ClanHandler.updateKick(c.clanOwned.members[i]);
						}
					}
				} else {
					c.sendMessage("You must have a clan before doing this.");
				}
			break;
		}
	}
	
}