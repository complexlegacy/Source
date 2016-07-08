package ionic.player.packets;

import ionic.player.Client;
import ionic.player.PlayerHandler;
import ionic.player.interfaces.AreaInterface;

/**
 * Walking packet
 **/
public class Walking implements PacketType {

    @
    Override
    public void processPacket(Client player, int packetType, int packetSize) {

        if (player.isTeleporting() || player.didTeleport || player.barrowsDoor) {
            return;
        }
        if (player.gamble != null) {
			return;
		}
        if (player.disableStuff)
        	return;
		AreaInterface.startInterfaceEvent(player);
        player.getPA().removeAllWindows();
        
        player.shopOpen = -1; player.shopOwner = -1;
        
        if (player.isAgility) { return; }
        if (player.isCrafting) { player.isCrafting = false; }
        
        if (player.isFletching) { player.isFletching = false; }
        if (player.isChopping) { player.startAnimation(65535); player.isChopping = false; }
        if (player.isSmithing) { player.stopSmithing = true; }
        if (player.isHerblore) { player.stopHerblore = true; }

        /* 248 is Clicking on minimap and 164 is clicking on the tiles. When clicking on Npc/object, packet 98 is sent. */
        if (packetType == 248 || packetType == 164)
        {
            player.clickObjectType = 0;
            player.clickNpcType = 0;
            player.faceUpdate(0);
            player.npcIndex = 0;
            player.playerIndex = 0;
        }
        player.nextChat = 0;
        player.getPA().resetVariables();
        player.walkingToItem = false;
        
        if (player.followId > 0 || player.followId2 > 0)
        {
            player.getPA().resetFollow();
        }
        
        if (player.resting) {
			player.getPA().stopResting();
		}
        
            player.isUsingDeathInterface = false;
            
        if (player.isLevitate)
        {
            player.isLevitate = false;
            player.playerStandIndex = 0x328;
            player.playerTurnIndex = 0x337;
            player.playerWalkIndex = 0x333;
            player.playerTurn180Index = 0x334;
            player.playerTurn90CWIndex = 0x335;
            player.playerTurn90CCWIndex = 0x336;
            player.playerRunIndex = 0x338;
            player.updateRequired = true;
            player.setAppearanceUpdateRequired(true);
        }

        if (player.duelRule[1] && player.duelStatus == 5)
        {
            if (PlayerHandler.players[player.duelingWith] != null)
            {
                if (!player.goodDistance(player.getX(), player.getY(), PlayerHandler.players[player.duelingWith].getX(), PlayerHandler.players[player.duelingWith].getY(), 1) || player.attackTimer == 0)
                {
                    player.sendMessage("Walking has been disabled in this duel!");
                }
            }
            player.playerIndex = 0;
            return;
        }

        if (player.freezeTimer > 0)
        {
            if (PlayerHandler.players[player.playerIndex] != null)
            {
                if (player.goodDistance(player.getX(), player.getY(), PlayerHandler.players[player.playerIndex].getX(), PlayerHandler.players[player.playerIndex].getY(), 1) && packetType != 98)
                {
                    player.playerIndex = 0;
                    return;
                }
            }
            if (packetType != 98)
            {
                player.sendMessage("A magical force stops you from moving.");
                player.playerIndex = 0;
            }
            return;
        }

        if (System.currentTimeMillis() - player.lastSpear < 4000)
        {
            player.sendMessage("You have been stunned.");
            player.playerIndex = 0;
            return;
        }

        if (packetType == 98)
        {
            player.mageAllowed = true;
        }

        if ((player.duelStatus >= 1 && player.duelStatus <= 4) || player.duelStatus == 6)
        {
                player.getDuelArena().claimStakedItems();
                return;
        }


        if (player.isDead)
        {
            return;
        }
        if (player.inTrade)
        {
            player.getTradeAndDuel().declineTrade();
        }
        if (packetType == 248)
        {
            packetSize -= 14;
        }
        player.newWalkCmdSteps = (packetSize - 5) / 2;
        if (++player.newWalkCmdSteps > player.walkingQueueSize)
        {
            player.newWalkCmdSteps = 0;
            return;
        }

        player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = 0;

        int firstStepX = player.getInStream().readSignedWordBigEndianA() - player.getMapRegionX() * 8;
        for (int i = 1; i < player.newWalkCmdSteps; i++)
        {
            player.getNewWalkCmdX()[i] = player.getInStream().readSignedByte();
            player.getNewWalkCmdY()[i] = player.getInStream().readSignedByte();
        }

        int firstStepY = player.getInStream().readSignedWordBigEndian() - player.getMapRegionY() * 8;
        player.setNewWalkCmdIsRunning(player.getInStream().readSignedByteC() == 1);
        for (int i1 = 0; i1 < player.newWalkCmdSteps; i1++)
        {
            player.getNewWalkCmdX()[i1] += firstStepX;
            player.getNewWalkCmdY()[i1] += firstStepY;
        }
    }

}