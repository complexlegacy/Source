package ionic.player.content.miscellaneous;

import ionic.player.Player;
import ionic.player.interfaces.AreaInterface;
import ionic.player.interfaces.ItemsKeptOnDeath;

/**
 * White and red skull.
 * @author MGT Madness, created on 29-01-2014.
 */
public class Skull 
{
	
	/**
	 * Activate white skull.
	 * @param player
	 * 			The associated player.
	 */
	public static void whiteSkull(Player player)
	{
		if (player.getRedSkull() && (player.inWilderness() || player.getCombat().inCombat()))
		{
			player.sendMessage("You cannot do this right now.");
			return;
		}
		player.setWhiteSkull(true);
		player.setRedSkull(false);
        player.skullTimer = 1200;
        AreaInterface.startSkullTimerEvent(player);
        player.headIconPk = 0;
        player.getPA().requestUpdates();
	}
	
	/**
	 * Turn off the skull.
	 * @param player
	 * 			The associated player.
	 */
	public static void clearSkull(Player player)
	{
		player.setWhiteSkull(false);
        player.setRedSkull(false);
        player.headIconPk = -1;
        player.attackedPlayers.clear();
        player.getPA().requestUpdates();
        ItemsKeptOnDeath.updateInterface(player);
	}
	
	/**
	 * Update the skull, above the player's head.
	 * @param player
	 * 			The associated player.
	 */
	public static void updateSkullAppearance(Player player)
	{
		if (player.getWhiteSkull())
		{
			player.headIconPk = 0;
			player.getPA().requestUpdates();
		}
		else if (player.getRedSkull())
		{
			player.headIconPk = 1;
			player.getPA().requestUpdates();
		}
	}

}
