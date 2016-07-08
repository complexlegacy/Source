package ionic.player.packets;

import ionic.clans.ClanHandler;
import ionic.clans.ClanSaver;
import ionic.player.Client;
import ionic.player.PlayerHandler;
import utility.Misc;
import network.connection.Connection;
import core.Configuration;

/**
 * Private messaging, friends etc
 **/
public class PrivateMessaging implements PacketType {

	public final int ADD_FRIEND = 188, SEND_PM = 126, REMOVE_FRIEND = 215,
			CHANGE_PM_STATUS = 95, REMOVE_IGNORE = 74, ADD_IGNORE = 133;

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {	
		c.getPA().sendFrame126("", 22100);
		switch (packetType) {

		case ADD_FRIEND:
			long friendToAdd = c.getInStream().readQWord();
			boolean canAdd = true;

			for (int i1 = 0; i1 < c.friends.length; i1++) {
				if (c.friends[i1] != 0 && c.friends[i1] == friendToAdd) {
					canAdd = false;
					c.sendMessage(friendToAdd
							+ " is already on your friends list.");
				}
			}
			if (canAdd == true) {
				for (int i1 = 0; i1 < c.friends.length; i1++) {
					if (c.friends[i1] == 0) {
						c.friends[i1] = friendToAdd;
						for (int i2 = 1; i2 < Configuration.MAX_PLAYERS; i2++) {
							if (PlayerHandler.players[i2] != null
									&& PlayerHandler.players[i2].isActive
									&& Misc.playerNameToInt64(PlayerHandler.players[i2].playerName) == friendToAdd) {
								Client o = (Client) PlayerHandler.players[i2];
								if (o != null) {
									if (PlayerHandler.players[i2].privateChat == 0
											|| (PlayerHandler.players[i2].privateChat == 1 && o
													.getPA()
													.isInPM(Misc
															.playerNameToInt64(c.playerName)))) {
										c.getPA().loadPM(friendToAdd, 1);
										break;
									}
								}
							}
						}
						break;
					}
				}
			}
			ClanHandler.friendsToRanks(c);
			if (c.clanOwned != null) {
				new ClanSaver(c.clanOwned);
				ClanHandler.sendClanTabUpdate(c.clanOwned);
			}
			break;

		case SEND_PM:
			long sendMessageToFriendId = c.getInStream().readQWord();
			byte pmchatText[] = new byte[100];
			int pmchatTextSize = (byte) (packetSize - 8);
			c.getInStream().readBytes(pmchatText, pmchatTextSize, 0);

			if (Connection.isMuted(c))
				break;

			for (int i1 = 0; i1 < c.friends.length; i1++) {
				if (c.friends[i1] == sendMessageToFriendId) {
					boolean pmSent = false;

					for (int i2 = 1; i2 < Configuration.MAX_PLAYERS; i2++) {
						if (PlayerHandler.players[i2] != null && PlayerHandler.players[i2].isActive && Misc.playerNameToInt64(PlayerHandler.players[i2].playerName) == sendMessageToFriendId) {
							Client o = (Client) PlayerHandler.players[i2];
							if (o != null) {
								if (PlayerHandler.players[i2].privateChat == 0 || (PlayerHandler.players[i2].privateChat == 1 && o.getPA().isInPM(Misc.playerNameToInt64(c.playerName)))) {
									o.getPA().sendPM(Misc.playerNameToInt64(c.playerName), c.playerRights, pmchatText, pmchatTextSize);
									pmSent = true;

								}
							}
							break;
						}
					}
					if (!pmSent) {
						c.sendMessage("That player is currently offline.");
						break;
					}
				}
			}
			break;

		case REMOVE_FRIEND:
			long friendToRemove = c.getInStream().readQWord();

			for (int i1 = 0; i1 < c.friends.length; i1++) {
				if (c.friends[i1] == friendToRemove) {
					for (int i2 = 1; i2 < Configuration.MAX_PLAYERS; i2++) {
						Client o = (Client) PlayerHandler.players[i2];
						if (o != null) {
							if (c.friends[i1] == Misc
									.playerNameToInt64(PlayerHandler.players[i2].playerName)) {
								o.getPA().updatePM(c.playerId, 0, false);
								break;
							}
						}
					}
					c.friends[i1] = 0;
					c.getPA().sendFrame126("", i1 + 14101);
					c.getPA().sendFrame126("", i1 + 17551);
					break;
				}
			}
			ClanHandler.friendsToRanks(c);
			if (c.clanOwned != null) {
				new ClanSaver(c.clanOwned);
				ClanHandler.sendClanTabUpdate(c.clanOwned);
			}
			break;

		case REMOVE_IGNORE:
			long ignore = c.getInStream().readQWord();
			
			for (int i = 0; i < c.ignores.length; i++) {
				if (c.ignores[i] == ignore) {
					c.ignores[i] = 0;
					break;
				}
			}
			break;

		case CHANGE_PM_STATUS:
			c.getInStream().readUnsignedByte();
			c.privateChat = c.getInStream().readUnsignedByte();
			c.getInStream().readUnsignedByte();
			for (int i1 = 1; i1 < Configuration.MAX_PLAYERS; i1++) {
				if (PlayerHandler.players[i1] != null
						&& PlayerHandler.players[i1].isActive == true) {
					Client o = (Client) PlayerHandler.players[i1];
					if (o != null) {
						o.getPA().updatePM(c.playerId, 1, c.privateChat == 2);
					}
				}
			}
			break;

		case ADD_IGNORE:
			long ignoreAdd = c.getInStream().readQWord();
			for (int i = 0; i < c.ignores.length; i++) {
				if (c.ignores[i] == 0) {
					c.ignores[i] = ignoreAdd;
					break;
				}
			}
			break;

		}

	}
}
