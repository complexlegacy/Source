package ionic.player.packets;

import ionic.player.Client;
import network.connection.Connection;

/**
 * Chat
 **/
public class Chat implements PacketType
{

    @
    Override
    public void processPacket(Client player, int packetType, int packetSize)
    {

        player.setChatTextEffects(player.getInStream().readUnsignedByteS());
        player.setChatTextColor(player.getInStream().readUnsignedByteS());
        player.setChatTextSize((byte)(player.packetSize - 2));
        player.inStream.readBytes_reverseA(player.getChatText(), player.getChatTextSize(), 0);
        //c.getPA().writeChatLog(Misc.textUnpack(c.getChatText(), packetSize-2));

        if (Connection.isMuted(player))
        {
        	player.sendMessage("You are muted for breaking a rule.");
            return;
        }
        player.setChatTextUpdateRequired(true);

    }

}