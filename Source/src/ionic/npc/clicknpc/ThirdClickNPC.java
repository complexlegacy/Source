package ionic.npc.clicknpc;

import ionic.player.Client;
import utility.Misc;
import core.Configuration;

/**
 * Third option click on NPC interactions.
 * @author MGT Madness, created on 18-01-2013.
 */
public class ThirdClickNPC
{
	
	/**
	 * Third option click on NPC.
	 * @param player
	 * 			The associated player.
	 * @param npcType
	 * 			The NPC identity.
	 */
	public static void thirdClickNpc(Client player, int npcType)
    {

        player.clickNpcType = 0;
        player.npcClickIndex = 0;

        if (Configuration.DEBUG)
        {
            Misc.println("Third Click Npc: " + player.npcType);
        }

        switch (npcType)
        {

            default: 
            break;

        }

    }	

}
