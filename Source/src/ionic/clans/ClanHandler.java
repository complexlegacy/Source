package ionic.clans;

import ionic.player.Player;
import ionic.player.Ranking;
import ionic.player.achievements.AchievementHandler;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;

/**
 * @author Keith
 */
public class ClanHandler {
	
	public static Clan[] clans = new Clan[250];
	
	public void processPunishments() {
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer container) {
    			for (int i = 0; i < clans.length; i++) {
    				if (clans[i] != null) {
    					for (int j = 0; j < clans[i].kickTicks.length; j++) {
    						if (clans[i].kickTicks[j] > 0) {
    							clans[i].kickTicks[j] --;
    							if (clans[i].kickTicks[j] == 0) {
    								if (clans[i].kicked[j] != null) {
    									clans[i].kicked[i] = null;
    								}
    							}
    						}
    					}
    				}
    			}
    		}
    		public void stop() {}
		}, 100);
	}
	
	public static boolean isKicked(Clan clan, Player c) {
		for (int i = 0; i < clan.kicked.length; i++) {
			if (clan.kicked[i] != null) {
				if (clan.kicked[i].toLowerCase().equals(c.playerName.toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static int getKickSlot(Clan c) {
		for (int i = 0; i < c.kicked.length; i++) {
			if (c.kicked[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	public static void clanMessage(Clan clan, Player c, String s) {
		if (clan != null) {
			if (!canTalk(c, clan)) {
				c.sendMessage("You are not a high enough rank to talk on this channel.");
				return;
			}				
			String crown = "";
			int r = Ranking.forID(c.playerRights).rights - 1;
			if (r != -1) { crown = "<img="+r+">"; }
			String message = "@bla@[@blu@"+clan.prefix+"@bla@] "+crown+""+c.playerName+": @dre@"+s;				
			for (int i = 0; i < clan.members.length; i++) {
				if (clan.members[i] != null) {
					clan.members[i].sendMessage(message);
				}
			}
		}
	}
	
	public static void kickMessage(Player kicker, Player victim) {
		if (kicker != null && victim != null && kicker.clan != null) {
			for (int i = 0; i < kicker.clan.members.length; i++) {
				if (kicker.clan.members[i] != null) {
					kicker.clan.members[i].sendMessage(
							"[<img=1>@blu@"+kicker.clan.prefix+"@bla@<img=1>] @dre@"+kicker.playerName+" - is kicking "+victim.playerName+" from this clan chat.");
				}
			}
		}
	}
	
	public static void updateKick(Player c) {
		if (c.clan != null) {
			boolean s = canKick(c, c.clan);
			c.getPA().sendFrame126(""+s+"", 18146);
		}
	}
	
	public static void joinClan(Player c, String name) {
		Clan clan = null;
		for (int i = 0; i < clans.length; i++) {
			if (clans[i] != null) {
				if (clans[i].owner.toLowerCase().equals(name.toLowerCase())) {
					int slot = getSlot(clans[i]);
					if (isKicked(clans[i], c)) {
						c.sendMessage("You have recently been kicked from this channel and cannot re-join yet.");
						return;
					}
					if (!canEnter(c, clans[i])) {
						c.sendMessage("You do not have a high enough rank to join this clan chat channel.");
						return;
					}
					if (slot == -1) {
						c.sendMessage("The clan that you were trying to enter is currently full");
						return;
					}
					clan = clans[i];
					clan.members[slot] = c;
					break;
				}
			}
		}
		if (clan != null) {
			c.clan = clan;
			sendClanTabUpdate(c.clan);
			c.sendMessage("Now talking in clan chat: "+clan.prefix+"");
		} else {
			c.sendMessage("That player does not exist, or does not own a clan chat.");
		}
		AchievementHandler.add(c, 24, "easy", 1);
	}
	
	public static int getSlot(Clan c) {
		for (int i = 0; i < c.members.length; i++) {
			if (c.members[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	public static void leaveClan(Player c, String message, boolean disabled) {
		if (c.clan != null) {
			for (int i = 0; i < 100; i++) {
				if (c.clan.members[i] == c) {
					c.clan.members[i] = null;
				}
			}
			Clan f = c.clan;
			c.clan = null;
			if (!disabled) {
				sendClanTabUpdate(f);
			}
			sendClanTabUpdate(c);
			if (message != null) {
				c.sendMessage(message);
			}
		}
	}
	
	public static void sendClanTabUpdate(Clan c) {
		for (int i = 0; i < c.members.length; i++) {
			if (c.members[i] != null) {
				sendClanTabUpdate(c.members[i]);
			}
		}
	}
	
	public static void sendClanTabUpdate(Player c) {
		if (c.clan == null) {
			c.getPA().sendFrame126("Join Chat", 18135);
			c.getPA().sendFrame126("Talking in: Not in chat", 18139);
			c.getPA().sendFrame126("Owner: None", 18140);
		} else {
			c.getPA().sendFrame126("Leave Chat", 18135);
			c.getPA().sendFrame126("Talking in: "+c.clan.prefix, 18139);
			c.getPA().sendFrame126("Owner: "+c.clan.owner, 18140);
		}
		String s = "";
		for (int i = 0; i < 100; i++) {
			if (c.clan != null) {
				if (c.clan.members[i] != null) {
					int rank = getRank(c.clan.members[i]);
					String k = "";
					if (c.clan.members[i].playerRights == 2) { k += "<clan=`8`>"; }
					if (rank != -1) { k += "<clan=`"+rank+"`>"; }
					k += ""+c.clan.members[i].playerName+"###";
					s += k;
				}
			}
		}
		c.getPA().sendFrame126(s, 18128);
		updateKick(c);
	}
	
	public static int getRank(Player c) {
		if (c.clan.owner.toLowerCase().equals(c.playerName.toLowerCase())) {
			return 7;
		}
		for (int i = 0; i < 100; i++) {
			if (c.clan.ranks[i] != null) {
				if (c.playerName.toLowerCase().equals(c.clan.ranks[i].toLowerCase())) {
					return c.clan.ranksN[i];
				}
			}
		}
		return -1;
	}
	
	public static int getRank(Player c, Clan d) {
		if (d.owner.toLowerCase().equals(c.playerName.toLowerCase())) {
			return 7;
		}
		for (int i = 0; i < 100; i++) {
			if (d.ranks[i] != null) {
				if (c.playerName.toLowerCase().equals(d.ranks[i].toLowerCase())) {
					return d.ranksN[i];
				}
			}
		}
		return -1;
	}
	
	public static void friendsToRanks(Player c) {
		if (c.clanOwned == null) { return; }
		for (int i = 0; i < 100; i++) {
			if (c.friends[i] != 0) {
				String name = c.long2Name(c.friends[i]);
				c.clanOwned.ranks[i] = name;
			} else {
				c.clanOwned.ranks[i] = null;
				c.clanOwned.ranksN[i] = 0;
			}
		}
		c.sendClanRanks();
		new ClanSaver(c.clanOwned);
	}
	
	public static int getSlot() {
		for (int i = 0; i < clans.length; i++) {
			if (clans[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	public static Clan findClan(String name) {
		Clan c = null;
		for (int i = 0; i < clans.length; i++) {
			if (clans[i] != null) {
				if (clans[i].owner.toLowerCase().equals(name.toLowerCase())) {
					return clans[i];
				}
			}
		}
		return c;
	}
	
	public static void loadClans() {
		for (int i = 0; i < clans.length; i++) {
			new ClanLoader(i);
		}
	}
	
	public static void updateSetupInterface(Player c) {
		if (c.clanOwned == null) {
			c.getPA().sendFrame126("Chat Disabled", 24121);
		} else {
			c.getPA().sendFrame126(c.clanOwned.prefix, 24121);
		}
		if (c.clanOwned != null) {
			c.getPA().sendFrame126(convert9(c.clanOwned.whoCanEnter), 24123);
			c.getPA().sendFrame126(convert9(c.clanOwned.whoCanTalk), 24125);
			c.getPA().sendFrame126(convert7(c.clanOwned.whoCanKick), 24127);
		} else {
			c.getPA().sendFrame126(convert9(0), 24123);
			c.getPA().sendFrame126(convert9(0), 24125);
			c.getPA().sendFrame126(convert7(6), 24127);
		}
	}
	
	public static boolean canEnter(Player c, Clan clan) {
		if (clan.whoCanEnter == 0) { return true; }
		if (c.playerRights == 2 || c.playerRights == 1) { return true; }
		if (c.playerName.toLowerCase().equals(clan.owner.toLowerCase())) { return true; }
		if (clan.whoCanEnter >= 1) {
			int rank = getRank(c, clan);
			if (rank >= (clan.whoCanEnter - 1)) {
				return true;
			}
		}
		return false;
	}
	public static boolean canTalk(Player c, Clan clan) {
		if (clan.whoCanTalk == 0) { return true; }
		if (c.playerRights == 2 || c.playerRights == 1) { return true; }
		if (c.playerName.toLowerCase().equals(clan.owner.toLowerCase())) { return true; }
		if (clan.whoCanTalk >= 1) {
			int rank = getRank(c, clan);
			if (rank >= (clan.whoCanTalk - 1)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean canKick(Player c, Clan clan) {
		if (c.playerRights == 2) { return true; }
		if (c.playerName.toLowerCase().equals(clan.owner.toLowerCase())) { return true; }
		if (clan.whoCanKick >= 0) {
			int rank = getRank(c, clan);
			if (rank >= (clan.whoCanKick + 1)) {
				return true;
			}
		}
		return false;
	}

	public static String convert9(int x) {
		String[] b = {"Anyone", "Any friends", "Recruit+", "Corporal+", "Sergeant+", "Lieutenant+",
				"Captain+", "General+", "Only me"};
		
		return b[x];
	}
	
	public static String convert7(int x) {
		String[] b = {"Recruit+", "Corporal+", "Sergeant+", "Lieutenant+",
				"Captain+", "General+", "Only me"};
		
		return b[x];
	}
	
}
