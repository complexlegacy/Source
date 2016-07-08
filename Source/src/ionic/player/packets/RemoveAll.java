package ionic.player.packets;

import ionic.item.GameItem;
import ionic.item.ItemData;
import ionic.player.Client;
import ionic.player.content.gambling.GamblingHandler;
import ionic.player.content.miscellaneous.PriceChecker;

/**
 * Bank All Items
 **/
public class RemoveAll implements PacketType
{

    @
    Override
    public void processPacket(Client player, int packetType, int packetSize)
    {
        int removeSlot = player.getInStream().readUnsignedWordA();
        int interfaceId = player.getInStream().readUnsignedWord();
        int removeId = player.getInStream().readUnsignedWordA();

        switch (interfaceId)
        {
        
        case 37221:
			GamblingHandler.remove(player, removeId, removeSlot, Integer.MAX_VALUE);
			break;
        
        case 37112:
			PriceChecker.remove(player, removeId, removeSlot, Integer.MAX_VALUE);
			break;


        case 3322:
            if (player.duelStatus <= 0)
            {
                if (ItemData.data[removeId].stackable)
                {
                    player.getTradeAndDuel().tradeItem(removeId, removeSlot, player.playerItemsN[removeSlot]);
                }
                else
                {
                    player.getTradeAndDuel().tradeItem(removeId, removeSlot, 28);
                }
            }
            else
            {
                if (ItemData.data[removeId].stackable)
                {
                    player.getDuelArena().stakeItem(removeId, removeSlot, player.playerItemsN[removeSlot]);
                }
                else
                {
                    player.getDuelArena().stakeItem(removeId, removeSlot, 28);
                }
            }
            break;

        case 3415:
            if (player.duelStatus <= 0)
            {
                if (ItemData.data[removeId].stackable)
                {
                    for (GameItem item: player.getTradeAndDuel().offeredItems)
                    {
                        if (item.id == removeId)
                        {
                            player.getTradeAndDuel().fromTrade(removeId, removeSlot, player.getTradeAndDuel().offeredItems.get(removeSlot).amount);
                        }
                    }
                }
                else
                {
                    for (GameItem item: player.getTradeAndDuel().offeredItems)
                    {
                        if (item.id == removeId)
                        {
                            player.getTradeAndDuel().fromTrade(removeId, removeSlot, 28);
                        }
                    }
                }
            }
            break;

        case 6669:
            if (ItemData.data[removeId].stackable)
            {
                for (GameItem item: player.getDuelArena().stakedItems)
                {
                    if (item.id == removeId)
                    {
                        player.getDuelArena().fromDuel(removeId, removeSlot, player.getDuelArena().stakedItems.get(removeSlot).amount);
                    }
                }

            }
            else
            {
                player.getDuelArena().fromDuel(removeId, removeSlot, 28);
            }
            break;

        }
    }

}