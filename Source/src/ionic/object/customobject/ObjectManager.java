package ionic.object.customobject;

import ionic.player.Client;
import ionic.player.PlayerHandler;
import ionic.player.content.combat.dwarfcannon.CannonHandler;
import ionic.player.content.miscellaneous.Pickables;
import ionic.player.content.miscellaneous.Teleport;
import ionic.player.content.partyroom.PartyRoom;
import ionic.player.content.skills.firemaking.Firemaking;
import ionic.player.content.skills.mining.MiningHandler;
import ionic.player.content.skills.woodcutting.Woodcutting;

import java.util.ArrayList;

import utility.Misc;

/**
 * @author Sanity
 */

public class ObjectManager
{

    public ArrayList < Object > objects = new ArrayList < Object > ();
    private ArrayList < Object > toRemove = new ArrayList < Object > ();
    public void process()
    {
        for (Object o: objects)
        {
            if (o.tick > 0)
                o.tick--;
            else
            {
                updateObject(o);
                toRemove.add(o);
            }
        }
        for (Object o: toRemove)
        {
            if (isObelisk(o.newId))
            {
                int index = getObeliskIndex(o.newId);
                if (activated[index])
                {
                    activated[index] = false;
                    teleportObelisk(index);
                }
            }
            objects.remove(o);
        }
        toRemove.clear();
        
        for (int i = 0; i < Woodcutting.trees.length; i++) {
	        if (Woodcutting.trees[i] != null) {
	        	if (Woodcutting.trees[i].t > 0) { Woodcutting.trees[i].t --; } 
	        	else if (Woodcutting.trees[i].t == 0 && Woodcutting.trees[i].l == 0) {
	        		Woodcutting.globalSpawn(Woodcutting.trees[i].o, Woodcutting.trees[i].x, Woodcutting.trees[i].y);
	        		Woodcutting.trees[i] = null;
	        	}
	        }
	        if (Firemaking.fires[i] != null) {
	        	if (Firemaking.fires[i].t > 0) { Firemaking.fires[i].t --; } 
	        	else if (Firemaking.fires[i].t == 0) {
	        		Firemaking.globalSpawn(-1, Firemaking.fires[i].x, Firemaking.fires[i].y);
	        		Firemaking.fires[i] = null;
	        	}
	        }
	        if (Pickables.picks[i] != null) {
	        	if (Pickables.picks[i].t > 0) { Pickables.picks[i].t --; } 
	        	else if (Pickables.picks[i].t == 0) {
	        		Pickables.globalSpawn(Pickables.picks[i].o, Pickables.picks[i].x, Pickables.picks[i].y);
	        		Pickables.picks[i] = null;
	        	}
	        }
	        if (MiningHandler.rocks[i] != null) {
	        	if (MiningHandler.rocks[i].ticks > 0) { MiningHandler.rocks[i].ticks --; } 
	        	else if (MiningHandler.rocks[i].ticks == 0 && MiningHandler.rocks[i].life == 0) {
	        		MiningHandler.globalSpawn(MiningHandler.rocks[i].objectId, MiningHandler.rocks[i].x, MiningHandler.rocks[i].y);
	        		MiningHandler.rocks[i] = null;
	        	}
	        }
        }
        
    }

    public void removeObject(int x, int y)
    {
        for (int j = 0; j < PlayerHandler.players.length; j++)
        {
            if (PlayerHandler.players[j] != null)
            {
                Client c = (Client) PlayerHandler.players[j];
                c.getPA().object(-1, x, y, 0, 10);
            }
        }
    }

    public void updateObject(Object o)
    {
        for (int j = 0; j < PlayerHandler.players.length; j++)
        {
            if (PlayerHandler.players[j] != null)
            {
                Client c = (Client) PlayerHandler.players[j];
                c.getPA().object(o.newId, o.objectX, o.objectY, o.face, o.type);
            }
        }
    }

    public void placeObject(Object o)
    {
        for (int j = 0; j < PlayerHandler.players.length; j++)
        {
            if (PlayerHandler.players[j] != null)
            {
                Client c = (Client) PlayerHandler.players[j];
                if (c.distanceToPoint(o.objectX, o.objectY) <= 60)
                    c.getPA().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
            }
        }
    }

    public Object getObject(int x, int y, int height)
    {
        for (Object o: objects)
        {
            if (o.objectX == x && o.objectY == y && o.height == height)
                return o;
        }
        return null;
    }

    public void loadObjects(Client c)
    {
        if (c == null)
            return;
        for (Object o: objects)
        {
            if (loadForPlayer(o, c))
                c.getPA().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
        }
        loadCustomSpawns(c);
    }

    public void loadCustomSpawns(Client c) {
        for (int i = 0; i < 5; i++) {
            c.getPA().checkObjectSpawn(1814, 3090, 3475, 0, i);
        }
        c.getPA().checkObjectSpawn(-1, 2657, 2585, 0, 0);
        c.getPA().checkObjectSpawn(-1, 2656, 2585, 0, 0);
        c.getPA().checkObjectSpawn(-1, 2643, 2592, 0, 0);
        c.getPA().checkObjectSpawn(-1, 2643, 2593, 0, 0);
        c.getPA().checkObjectSpawn(-1, 2670, 2593, 0, 0);
        c.getPA().checkObjectSpawn(-1, 2670, 2592, 0, 0);
        c.getPA().checkObjectSpawn(9369, 2400, 5176, 0, 10);
        c.getPA().checkObjectSpawn(9369, 2398, 5176, 0, 10);
        
        for (int i = 0; i < Woodcutting.trees.length; i++) {
        	if (Woodcutting.trees[i] != null) {
        		if (Woodcutting.trees[i].l == 0) {
        			c.getPA().checkObjectSpawn(Woodcutting.trees[i].s, Woodcutting.trees[i].x, Woodcutting.trees[i].y, 0, 10);
        		}
        	}
        	if (Firemaking.fires[i] != null) {
        		if (Firemaking.fires[i].t > 0) {
        			c.getPA().checkObjectSpawn(Firemaking.fires[i].o, Firemaking.fires[i].x, Firemaking.fires[i].y, 0, 10);
        		}
        	}
        	if (MiningHandler.rocks[i] != null) {
        		if (MiningHandler.rocks[i].life == 0) {
        			c.getPA().checkObjectSpawn(MiningHandler.rocks[i].rep, MiningHandler.rocks[i].x, MiningHandler.rocks[i].y, 0, 10);
        		}
        	}
        }
        c.getPA().checkObjectSpawn(2783, 2974, 3373, 0, 10);
        PartyRoom.loadBalloons(c);
        CannonHandler.loadCannonsInRegion(c);
    }

    public boolean isObelisk(int id)
    {
        for (int j = 0; j < obeliskIds.length; j++)
        {
            if (obeliskIds[j] == id)
                return true;
        }
        return false;
    }
    public int[] obeliskIds = {
        14829, 14830, 14827, 14828, 14826, 14831
    };
    public int[][] obeliskCoords = {
        {
            3154, 3618
        },
        {
            3225, 3665
        },
        {
            3033, 3730
        },
        {
            3104, 3792
        },
        {
            2978, 3864
        },
        {
            3305, 3914
        }
    };
    public boolean[] activated = {
        false, false, false, false, false, false
    };

    public void startObelisk(int obeliskId)
    {
        int index = getObeliskIndex(obeliskId);
        if (index >= 0)
        {
            if (!activated[index])
            {
                activated[index] = true;
                addObject(new Object(14825, obeliskCoords[index][0], obeliskCoords[index][1], 0, -1, 10, obeliskId, 16));
                addObject(new Object(14825, obeliskCoords[index][0] + 4, obeliskCoords[index][1], 0, -1, 10, obeliskId, 16));
                addObject(new Object(14825, obeliskCoords[index][0], obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId, 16));
                addObject(new Object(14825, obeliskCoords[index][0] + 4, obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId, 16));
            }
        }
    }

    public int getObeliskIndex(int id)
    {
        for (int j = 0; j < obeliskIds.length; j++)
        {
            if (obeliskIds[j] == id)
            {
                return j;
            }
        }
        return -1;
    }

    public void teleportObelisk(int port)
    {
        int random = Misc.random(5);
        while (random == port)
        {
            random = Misc.random(5);
        }
        for (int j = 0; j < PlayerHandler.players.length; j++)
        {
            if (PlayerHandler.players[j] != null)
            {
                Client c = (Client) PlayerHandler.players[j];
                int xOffset = c.absX - obeliskCoords[port][0];
                int yOffset = c.absY - obeliskCoords[port][1];
                if (c.goodDistance(c.getX(), c.getY(), obeliskCoords[port][0] + 2, obeliskCoords[port][1] + 2, 1))
                {
                    Teleport.startTeleport(c, obeliskCoords[random][0] + xOffset, obeliskCoords[random][1] + yOffset, 0, "lever");
                }
            }
        }
    }

    public boolean loadForPlayer(Object o, Client c)
    {
        if (o == null || c == null)
            return false;
        return c.distanceToPoint(o.objectX, o.objectY) <= 60 && c.heightLevel == o.height;
    }

    public void addObject(Object o)
    {
        if (getObject(o.objectX, o.objectY, o.height) == null)
        {
            objects.add(o);
            placeObject(o);
        }
    }




}