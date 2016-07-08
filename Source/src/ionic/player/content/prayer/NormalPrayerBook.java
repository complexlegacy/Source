package ionic.player.content.prayer;

import ionic.player.Player;

/**
 * Normal prayer book.
 * @author MGT Madness, created on 12-01-2013.
 */
public class NormalPrayerBook 
{
    
    /**
     * Turn off all prayer glows.
     * @param player
     * 			The associated player.
     */
    public static void resetAllPrayerGlows(Player player)
    {
    	for (int p = 0; p < player.PRAYER.length; p++)
        {
                player.prayerActive[p] = false;
                player.getPA().sendFrame36(player.PRAYER_GLOW[p], 0);
        }
    }

}
