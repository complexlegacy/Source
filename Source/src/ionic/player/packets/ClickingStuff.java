package ionic.player.packets;

import ionic.player.Client;
import ionic.player.PlayerHandler;


/**
 * Clicking stuff (interfaces)
 **/
public class ClickingStuff implements PacketType
{

        @
        Override
        public void processPacket(Client c, int packetType, int packetSize)
        {
                c.isUsingDeathInterface = false;
                if (c.inTrade)
                {
                        if (!c.acceptedTrade)
                        {
                                Client o = (Client) PlayerHandler.players[c.tradeWith];
                                o.tradeAccepted = false;
                                c.tradeAccepted = false;
                                o.tradeStatus = 0;
                                c.tradeStatus = 0;
                                c.tradeConfirmed = false;
                                c.tradeConfirmed2 = false;
                                c.getTradeAndDuel().declineTrade();
                        }
                }

                Client o = (Client) PlayerHandler.players[c.duelingWith];
                if (c.duelStatus == 5)
                {
                        return;
                }
                if (o != null)
                {
                        if (c.duelStatus >= 1 && c.duelStatus <= 4)
                        {
                                c.getDuelArena().declineDuel();
                                o.getDuelArena().declineDuel();
                        }
                }

                c.getDuelArena().claimStakedItems();

        }

}