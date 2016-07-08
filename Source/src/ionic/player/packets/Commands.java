package ionic.player.packets;

import ionic.clans.ClanHandler;
import ionic.player.Client;
import ionic.player.content.commands.CommandHandler;

public class Commands implements PacketType {
	@Override
        public void processPacket(Client player, int packetType, int packetSize) {
                String playerCommand = player.getInStream().readString();
                
                if (playerCommand.startsWith("/")) {
                	ClanHandler.clanMessage(player.clan, player, playerCommand.substring(3));
                	return;
                }
                
                CommandHandler.processCommand(player, playerCommand);
        }

}