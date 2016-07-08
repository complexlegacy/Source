package ionic.player.object;

import ionic.player.Client;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;

/**
 * Handle the object events.
 * @author MGT Madness
 *
 */

public class ObjectEvent 
{
	
	/**
	 * Start an event to find the first click object action.
	 */
	public static void clickObjectType1Event(final Client player)
	{
		
		if (player.doingClickObjectType1Event)
		{
			return;
		}
		player.doingClickObjectType1Event = true;
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
        {@
            Override
            public void execute(CycleEventContainer container)
            {

        	if (player.goodDistance(player.objectX + player.objectXOffset, player.objectY + player.objectYOffset, player.getX(), player.getY(), player.objectDistance))
            {
                    player.getActions().firstClickObject(player.objectId, player.objectX, player.objectY);
            }
        	
        	if (player.clickObjectType != 1)
        	{
                    container.stop();
        	}
        	
            }@
            Override
            public void stop()
            {
            	player.doingClickObjectType1Event = false;
            }
        }, 1);
		
	}
	
	/**
	 * Start an event to find the second click object action.
	 */
	public static void clickObjectType2Event(final Client player)
	{
		
		if (player.doingClickObjectType2Event)
		{
			return;
		}
		player.doingClickObjectType2Event = true;
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
        {@
            Override
            public void execute(CycleEventContainer container)
            {
        	
        	if (player.goodDistance(player.objectX + player.objectXOffset, player.objectY + player.objectYOffset, player.getX(), player.getY(), player.objectDistance))
            {
                    player.getActions().secondClickObject(player.objectId, player.objectX, player.objectY);
            }
        	
        	if (player.clickObjectType != 2)
        	{
                    container.stop();
        	}
        	
            }@
            Override
            public void stop()
            {
            	player.doingClickObjectType2Event = false;
            }
        }, 1);
		
	}
	
	/**
	 * Start an event to find the third click object action.
	 */
	public static void clickObjectType3Event(final Client player)
	{
		
		if (player.doingClickObjectType3Event)
		{
			return;
		}
		player.doingClickObjectType3Event = true;
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
        {@
            Override
            public void execute(CycleEventContainer container)
            {
        	
        	if (player.goodDistance(player.objectX + player.objectXOffset, player.objectY + player.objectYOffset, player.getX(), player.getY(), player.objectDistance))
            {
                    player.getActions().thirdClickObject(player.objectId, player.objectX, player.objectY);
            }
        	
        	if (player.clickObjectType != 3)
        	{
                    container.stop();
        	}
        	
            }@
            Override
            public void stop()
            {
            	player.doingClickObjectType3Event = false;
            }
        }, 1);
		
	}
	
	
	/**
	 * Start an event to find the third click object action.
	 */
	public static void clickObjectType4Event(final Client player)
	{
		
		if (player.doingClickObjectType4Event)
		{
			return;
		}
		player.doingClickObjectType4Event = true;
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
        {@
            Override
            public void execute(CycleEventContainer container)
            {
        	
        	if (player.goodDistance(player.objectX + player.objectXOffset, player.objectY + player.objectYOffset, player.getX(), player.getY(), player.objectDistance))
            {
                    player.getActions().thirdClickObject(player.objectId, player.objectX, player.objectY);
            }
        	
        	if (player.clickObjectType != 3)
        	{
                    container.stop();
        	}
        	
            }@
            Override
            public void stop()
            {
            	player.doingClickObjectType4Event = false;
            }
        }, 1);
		
	}
	
	/**
	 * Start an event to find the third click object action.
	 */
	public static void clickObjectType5Event(final Client player)
	{
		
		if (player.doingClickObjectType5Event)
		{
			return;
		}
		player.doingClickObjectType5Event = true;
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
        {@
            Override
            public void execute(CycleEventContainer container)
            {
        	
        	if (player.goodDistance(player.objectX + player.objectXOffset, player.objectY + player.objectYOffset, player.getX(), player.getY(), player.objectDistance))
            {
                    player.getActions().thirdClickObject(player.objectId, player.objectX, player.objectY);
            }
        	
        	if (player.clickObjectType != 3)
        	{
                    container.stop();
        	}
        	
            }@
            Override
            public void stop()
            {
            	player.doingClickObjectType5Event = false;
            }
        }, 1);
		
	}

}
