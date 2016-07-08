package ionic.player.content.combat;

import ionic.player.Player;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;

/**
 * Combat posioning.
 * @author MGT Madness, created on 09-04-2014.
 */
public class Poison 
{
	
	/**
	 * Inform the client to turn on the poison orb.
	 * @param player
	 * 			The associated player.
	 */
	public static void informClientOfPoisonOn(Player player)
	{
		player.sendMessage(":poisonon:");
	}
	
	/**
	 * Inform the client to turn off the poison orb.
	 * @param player
	 * 			The associated player.
	 */
	public static void informClientOfPoisonOff(Player player)
	{
		player.sendMessage(":poisonoff:");
	}
	
	/**
	 * Poison the player.
	 * @param player
	 * 			The associated player.
	 */
	public static void appendPoison(final Player player)
    {
            if (System.currentTimeMillis() - player.lastPoisonSip < player.poisonImmune)
            {
                    return;
            }
            player.sendMessage("You have been poisoned!");
            player.poisonRunOut = 14;
            player.lastPoison = System.currentTimeMillis();
            Poison.informClientOfPoisonOn(player);

            CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
            {@
                    Override
                    public void execute(CycleEventContainer container)
                    {
                            if (player.isDead || System.currentTimeMillis() - player.lastPoisonSip < player.poisonImmune)
                            {
                                    container.stop();
                            }
                            if (System.currentTimeMillis() - player.lastPoison > 45000 && player.poisonRunOut > 0)
                            {
                                    player.lastPoison = System.currentTimeMillis();
                                    player.getCombat().appendHit(player, 6, 2, -1, false);
                                    player.poisonRunOut--;
                            }
                            if (player.poisonRunOut == 0)
                            {
                                    player.sendMessage("The poison has worn off.");
                                    container.stop();
                            }

                    }

                    @
                    Override
                    public void stop()
                    {
                            player.poisonRunOut = 0;
                            Poison.informClientOfPoisonOff(player);
                    }
            }, 1);
    }

}
