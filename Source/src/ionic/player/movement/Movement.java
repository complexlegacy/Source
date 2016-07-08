package ionic.player.movement;

import ionic.player.Player;
import utility.Misc;

public class Movement
{
	
	/**
	 * The only difference i've seen with this method and resetWalkingQueue, is that stopMovement definitely stops the player for a game tick, even if the player is spamming walking.
	 * But for resetWalkingQueue, if the player spam walks and resetWalkingQueue is called, the player won't stop at all.
	 * @param player
	 */
	public static void stopMovement(Player player)
    {
            if (player.teleportToX <= 0 && player.teleportToY <= 0)
            {
            	player.teleportToX = player.absX;
            	player.teleportToY = player.absY;
            }
            player.newWalkCmdSteps = 0;
            player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = player.travelBackX[0] = player.travelBackY[0] = 0;
            Movement.getNextPlayerMovement(player);
    }
	
	public static void resetWalkingQueue(Player player)
    {
		player.wQueueReadPtr = player.wQueueWritePtr = 0;
            for (int i = 0; i < player.walkingQueueSize; i++)
            {
            	player.walkingQueueX[i] = player.currentX;
            	player.walkingQueueY[i] = player.currentY;
            }
    }
	
	public synchronized static void getNextPlayerMovement(Player player)
    {
			player.mapRegionDidChange = false;
			player.didTeleport = false;
			player.dir1 = player.dir2 = -1;
            if (player.teleportToX != -1 && player.teleportToY != -1)
            {
            	player.mapRegionDidChange = true;
                    if (player.mapRegionX != -1 && player.mapRegionY != -1)
                    {
                            int relX = player.teleportToX - player.mapRegionX * 8, relY = player.teleportToY - player.mapRegionY * 8;
                            if (relX >= 2 * 8 && relX < 11 * 8 && relY >= 2 * 8 && relY < 11 * 8)
                            	player.mapRegionDidChange = false;
                    }
                    if (player.mapRegionDidChange)
                    {
                    	player.mapRegionX = (player.teleportToX >> 3) - 6;
                    	player.mapRegionY = (player.teleportToY >> 3) - 6;
                    }
                    player.currentX = player.teleportToX - 8 * player.mapRegionX;
                    player.currentY = player.teleportToY - 8 * player.mapRegionY;
                    player.absX = player.teleportToX;
                    player.absY = player.teleportToY;
                    resetWalkingQueue(player);
                    player.teleportToX = player.teleportToY = -1;
                    player.didTeleport = true;
            }
            else
            {
            	player.dir1 = player.getNextWalkingDirection();
                    if (player.dir1 == -1)
                            return;

                    if (player.isRunning)
                    {
                    	player.dir2 = player.getNextWalkingDirection();
                    }

                    int deltaX = 0, deltaY = 0;

                    if (player.currentX < 2 * 8)
                    {
                            deltaX = 4 * 8;
                            player.mapRegionX -= 4;
                            player.mapRegionDidChange = true;
                    }
                    else if (player.currentX >= 11 * 8)
                    {
                            deltaX = -4 * 8;
                            player.mapRegionX += 4;
                            player.mapRegionDidChange = true;
                    }
                    if (player.currentY < 2 * 8)
                    {
                            deltaY = 4 * 8;
                            player.mapRegionY -= 4;
                            player.mapRegionDidChange = true;
                    }
                    else if (player.currentY >= 11 * 8)
                    {
                            deltaY = -4 * 8;
                            player.mapRegionY += 4;
                            player.mapRegionDidChange = true;
                    }
                    if (player.mapRegionDidChange)
                    {
                    	player.currentX += deltaX;
                    	player.currentY += deltaY;
                            for (int i = 0; i < player.walkingQueueSize; i++)
                            {
                            	player.walkingQueueX[i] += deltaX;
                            	player.walkingQueueY[i] += deltaY;
                            }
                    }
            }
    }
	
    public static void addToWalkingQueue(Player player, int x, int y)
    {
            int next = (player.wQueueWritePtr + 1) % player.walkingQueueSize;
            if (next == player.wQueueWritePtr)
                    return;
            player.walkingQueueX[player.wQueueWritePtr] = x;
            player.walkingQueueY[player.wQueueWritePtr] = y;
            player.wQueueWritePtr = next;
    }
    
    public synchronized static void postProcessing(Player player)
    {
            if (player.doingAction(true) || player.isTeleporting())
            {
                    resetWalkingQueue(player);
                    return;
            }

            player.isRunning = player.isRunning2;

            if (player.newWalkCmdSteps > 0)
            {
                    int firstX = player.getNewWalkCmdX()[0], firstY = player.getNewWalkCmdY()[0];
                    int lastDir = 0;
                    boolean found = false;
                    player.numTravelBackSteps = 0;
                    int ptr = player.wQueueReadPtr;
                    int dir = Misc.direction(player.currentX, player.currentY, firstX, firstY);
                    if (dir != -1 && (dir & 1) != 0)
                    {
                            do {
                                    lastDir = dir;
                                    if (--ptr < 0)
                                            ptr = player.walkingQueueSize - 1;
                                    player.travelBackX[player.numTravelBackSteps] = player.walkingQueueX[ptr];
                                    player.travelBackY[player.numTravelBackSteps++] = player.walkingQueueY[ptr];
                                    dir = Misc.direction(player.walkingQueueX[ptr],
                                    		player.walkingQueueY[ptr], firstX, firstY);
                                    if (lastDir != dir)
                                    {
                                            found = true;
                                            break;
                                    }
                            } while (ptr != player.wQueueWritePtr);
                    }
                    else
                            found = true;
                    if (!found)
                    {}
                    else
                    {
                    	player.wQueueWritePtr = player.wQueueReadPtr;
                            addToWalkingQueue(player, player.currentX, player.currentY);
                            if (dir != -1 && (dir & 1) != 0)
                            {
                                    for (int i = 0; i < player.numTravelBackSteps - 1; i++)
                                    {
                                            addToWalkingQueue(player, player.travelBackX[i], player.travelBackY[i]);
                                    }
                                    int wayPointX2 = player.travelBackX[player.numTravelBackSteps - 1], wayPointY2 = player.travelBackY[player.numTravelBackSteps - 1];
                                    int wayPointX1, wayPointY1;
                                    if (player.numTravelBackSteps == 1)
                                    {
                                            wayPointX1 = player.currentX;
                                            wayPointY1 = player.currentY;
                                    }
                                    else
                                    {
                                            wayPointX1 = player.travelBackX[player.numTravelBackSteps - 2];
                                            wayPointY1 = player.travelBackY[player.numTravelBackSteps - 2];
                                    }
                                    dir = Misc.direction(wayPointX1, wayPointY1, wayPointX2,
                                            wayPointY2);
                                    if (dir == -1 || (dir & 1) != 0)
                                    {}
                                    else
                                    {
                                            dir >>= 1;
                                            found = false;
                                            int x = wayPointX1, y = wayPointY1;
                                            while (x != wayPointX2 || y != wayPointY2)
                                            {
                                                    x += Misc.directionDeltaX[dir];
                                                    y += Misc.directionDeltaY[dir];
                                                    if ((Misc.direction(x, y, firstX, firstY) & 1) == 0)
                                                    {
                                                            found = true;
                                                            break;
                                                    }
                                            }
                                            if (!found)
                                            {}
                                            else
                                                    addToWalkingQueue(player, wayPointX1, wayPointY1);
                                    }
                            }
                            else
                            {
                                    for (int i = 0; i < player.numTravelBackSteps; i++)
                                    {
                                            addToWalkingQueue(player, player.travelBackX[i], player.travelBackY[i]);
                                    }
                            }
                            for (int i = 0; i < player.newWalkCmdSteps; i++)
                            {
                                    addToWalkingQueue(player, player.getNewWalkCmdX()[i], player.getNewWalkCmdY()[i]);
                            }
                    }
                    player.isRunning = player.isNewWalkCmdIsRunning() || player.isRunning2;
            }
            player.didTeleport = false;
    }

}
