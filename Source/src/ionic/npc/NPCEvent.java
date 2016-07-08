package ionic.npc;

import ionic.npc.clicknpc.FirstClickNPC;
import ionic.npc.clicknpc.SecondClickNPC;
import ionic.npc.clicknpc.ThirdClickNPC;
import ionic.player.Client;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;

/**
 * Handle the Npc cycle events
 * @author MGT Madness
 *
 */

public class NPCEvent
{
	
	/**
	 * Start the cycle event of left clicking an NPC, which executes the NPC action once the player is close enough to the NPC.
	 */
	public static void clickNpcType1Event(final Client player)
	{
		
		if (player.usingClickNpcType1Event)
		{
			return;
		}
		
		player.usingClickNpcType1Event = true;

        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
        {@
            Override
            public void execute(CycleEventContainer container)
            {

        	if (NPCHandler.npcs[player.npcClickIndex] != null && player.goodDistance(player.getX(), player.getY(), NPCHandler.npcs[player.npcClickIndex].getX(), 
        		NPCHandler.npcs[player.npcClickIndex].getY(), 1))
                {
                        player.turnPlayerTo(NPCHandler.npcs[player.npcClickIndex].getX(), NPCHandler.npcs[player.npcClickIndex].getY());
                        NPCHandler.npcs[player.npcClickIndex].facePlayer(player.playerId);
                        FirstClickNPC.firstClickNpc(player, player.npcType);
                }
        	
        	if (player.clickNpcType != 1)
        	{
        		container.stop();
        	}
            
        	
            }@
            Override
            public void stop()
            {
            	player.usingClickNpcType1Event = false;
            }
        }, 1);
        
	}
	
	/**
	 * Start the cycle event of second click on an NPC, which executes the NPC action once the player is close enough to the NPC.
	 */
	public static void clickNpcType2Event(final Client player)
	{
		
		if (player.usingClickNpcType2Event)
		{
			return;
		}
		
		player.usingClickNpcType2Event = true;

        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
        {@
            Override
            public void execute(CycleEventContainer container)
            {

        	if (NPCHandler.npcs[player.npcClickIndex] != null && player.goodDistance(player.getX(), player.getY(), NPCHandler.npcs[player.npcClickIndex].getX(), 
        		NPCHandler.npcs[player.npcClickIndex].getY(), 1))
                {
                        player.turnPlayerTo(NPCHandler.npcs[player.npcClickIndex].getX(), NPCHandler.npcs[player.npcClickIndex].getY());
                        NPCHandler.npcs[player.npcClickIndex].facePlayer(player.playerId);
                        SecondClickNPC.secondClickNpc(player, player.npcType);
                }
        	
        	if (player.clickNpcType != 2)
        	{
        		container.stop();
        	}
            
        	
            }@
            Override
            public void stop()
            {
            	player.usingClickNpcType2Event = false;
            }
        }, 1);
        
	}
	
	/**
	 * Start the cycle event of third click on an NPC, which executes the NPC action once the player is close enough to the NPC.
	 */
	public static void clickNpcType3Event(final Client player)
	{
		
		if (player.usingClickNpcType3Event)
		{
			return;
		}
		
		player.usingClickNpcType3Event = true;

        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
        {@
            Override
            public void execute(CycleEventContainer container)
            {

        	if (NPCHandler.npcs[player.npcClickIndex] != null && player.goodDistance(player.getX(), player.getY(), NPCHandler.npcs[player.npcClickIndex].getX(), 
        		NPCHandler.npcs[player.npcClickIndex].getY(), 1))
                {
                        player.turnPlayerTo(NPCHandler.npcs[player.npcClickIndex].getX(), NPCHandler.npcs[player.npcClickIndex].getY());
                        NPCHandler.npcs[player.npcClickIndex].facePlayer(player.playerId);
                        ThirdClickNPC.thirdClickNpc(player, player.npcType);
                }
        	
        	if (player.clickNpcType != 3)
        	{
        		container.stop();
        	}
            
        	
            }@
            Override
            public void stop()
            {
            	player.usingClickNpcType3Event = false;
            }
        }, 1);
        
	}

}
