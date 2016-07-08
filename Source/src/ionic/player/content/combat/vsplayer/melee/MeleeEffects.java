package ionic.player.content.combat.vsplayer.melee;

import ionic.player.Player;

/**
 * Melee effects such as Saradomin sword special attack effect etc..
 * @author MGT Madness, created on 08-02-2014.
 */
public class MeleeEffects 
{
	
	/**
	 * Effects that are applied when the weapon animation starts.
	 * @param attacker
	 * 			The player attacking the victim.
	 * @param theVictim
	 * 			The player being attacked.
	 * @param damage
	 * 			The damage of the attacker.
	 * @param special
	 * 			True, if a special attack is being used.
	 */
	public static void earlyEffects(Player attacker, Player victim, int damage, boolean special)
	{
		if (special)
		{
		}
		else
		{
			attacker.wearingFullGuthan = false;
            if (attacker.getPA().fullGuthans())
            {
            	attacker.wearingFullGuthan = true;
            }
		}
	}
}
