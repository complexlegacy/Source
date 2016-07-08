package ionic.player.interfaces;

import ionic.player.Client;
import ionic.player.Player;

/**
 * Interface updating methods go here.
 * @author MGT Madness, created on 12-01-2014.
 */
public class InterfaceAssistant
{
	
	/**
	 * Sign post at Entrana.
	 * @param player
	 * 			The associated player.
	 */
	public static void signPost(Player player)
	{
		player.getPA().showInterface(13585);
        player.getPA().sendFrame126("@dre@Directions", 13589);
        player.getPA().sendFrame126("West: farming.", 13591);
        player.getPA().sendFrame126("North: mining and smithing.", 13592);
        player.getPA().sendFrame126("North east: runecrafting.", 13593);
        player.getPA().sendFrame126("South east: woodcutting, fishing, cooking", 13594);
        player.getPA().sendFrame126("and runecrafting.", 13595);
        player.getPA().sendFrame126("", 13596);
        player.getPA().sendFrame126("", 13597);
        player.getPA().sendFrame126("", 13598);
        player.getPA().sendFrame126("", 13599);
        player.getPA().sendFrame126("", 13600);
        player.getPA().sendFrame126("", 13601);
        player.getPA().sendFrame126("", 13602);
        player.getPA().sendFrame126("", 13603);
        player.getPA().sendFrame126("", 13604);
        player.getPA().sendFrame126("", 13605);
        player.getPA().sendFrame126("", 13606);
        player.getPA().sendFrame126("", 13607);
        player.getPA().sendFrame126("", 13608);
        player.getPA().sendFrame126("", 13609);
	}
	
	/**
	 * Inform the client that the player is currently changing titles.
	 * @param player
	 * 			The associated player.
	 */
	public static void updateChangeTitleTextOn(Player player)
	{
		player.sendMessage(":changetitleon:");
	}
	
	/**
	 * Inform the client that the player is currently not changing titles.
	 * @param player
	 * 			The associated player.
	 */
	public static void updateChangeTitleTextOff(Player player)
	{
		player.sendMessage(":changetitleoff:");
	}
	
	/**
	 * Update the unlock Xp/ lock Xp on the Xp counter orb.
	 * @param player
	 * 			The associated player.
	 */
	public static void updateLockXp(Player player)
	{
	}
	
	/**
	 * Inform Client that lock Xp is on.
	 * @param player
	 * 			The associated player.
	 */
	public static void lockXpOn(Player player)
	{
	}
	
	/**
	 * Inform Client that lock Xp is off.
	 * @param player
	 * 			The associated player.
	 */
	public static void lockXpOff(Player player)
	{
	}
	
    
    /**
     * Inform the client to turn off quick prayer orb glow.
     * @param player
     * 			The associated player.
     */
    public static void quickPrayersOff(Client player)
    {
    	player.sendMessage(":quickprayeroff:");
    }
    
    /**
     * Inform the client to turn on quick prayer orb glow.
     * @param player
     * 			The associated player.
     */
    public static void quickPrayersOn(Client player)
    {
    	player.sendMessage(":quickprayeron:");
    }
    
    /**
     * Inform the client to clear the Achievement interface.
     * @param player
     * 			The associated player.
     */
    public static void clearAchievementInterface(Player player)
    {
    	player.sendMessage(":clearachievementinterface:");
    }
    
    /**
     * Inform the client about the resting state.
     * @param player
     * 			The associated player.
     * @param state
     * 			The state of the resting.
     */
    public static void informClientResting(Player player, String state)
    {
    	if (state.equals("on"))
    	{
    		player.sendMessage(":restingon:");
    	}
    	else if (state.equals("off"))
    	{
    		player.sendMessage(":restingoff:");
    	}
    }
    
    /**
     * Update the combat level text on the combat tab interface.
     * @param player
     * 			The associated player.
     */
    public static void updateCombatLevel(Player player)
    {
    	player.getPA().sendFrame126("Combat Level: " + player.combatLevel, 19000);
    }
    
    /**
     * Update the total level text on the skill tab interface.
     * @param player
     * 			The associated player.
     */
    public static void updateTotalLevel(Player player)
    {
    	player.getPA().sendFrame126("Total Lvl: " + player.totalLevel, 3984);
    }
	
	/**
     * Show the tabs.
     * @param player
     * 			The associated player.
     */
    public static void showTabs(Player player)
    {
            player.sendMessage(":updatetabs:"); // Send information to the client to shown tabs.
            player.setSidebarInterface(6, player.getPA().spellBook[player.playerMagicBook]); // Spellbook Tab
            player.setSidebarInterface(5, player.Prayerbook == 0 ? 5608 : 23377); //Normal prayer book
    }
    
    /**
     * Update other interfaces.
     * @param player
     * 			The associated player.
     */
    public static void miscellaneousInterfaces(Player player)
    {
                player.getPA().sendFrame126("The Bank Of " + player.playerName + "", 5383);
                player.getPA().sendFrame126(" When you are ready to leave", 2450);
                player.getPA().sendFrame126("Runite, use the", 2451);
                player.getPA().sendFrame126("button below to logout safely.", 2452);
                player.getPA().sendFrame126("Welcome to Runite - Character Design", 3649);
                player.getPA().sendFrame126("Welcome to Runite", 15259);

                /* Start of Ancient magicks */
                player.getPA().sendFrame126("West dragons", 13037);
                player.getPA().sendFrame126("East dragons", 13047);
                player.getPA().sendFrame126("Hill giants", 13055);
                player.getPA().sendFrame126("Mage bank", 13063);
                player.getPA().sendFrame126("City teleports", 13071);
                player.getPA().sendFrame126("Minigame teleports", 13081);
                player.getPA().sendFrame126("Skilling teleports", 13089);
                player.getPA().sendFrame126("None", 13097);
                /* End of Ancient magicks */

                /* Start of Modern spellbook */
                player.getPA().sendFrame126("West dragons", 1350);
                player.getPA().sendFrame126("East dragons", 1325);
                player.getPA().sendFrame126("Hill giants", 1300);
                player.getPA().sendFrame126("Mage bank", 1382);
                player.getPA().sendFrame126("City teleports", 1415);
                player.getPA().sendFrame126("Minigame teleports", 1454);
                player.getPA().sendFrame126("Skilling teleports", 7457);
                /* End of Modern spellbook */
    }
    
    /**
     * Update the split private chat interface.
     * @param player
     * 			The associated interface.
     */
    public static void splitPrivateChat(Player player)
    {
    	player.getPA().sendFrame36(502, player.splitChat ? 1 : 0);
		player.getPA().sendFrame36(287, player.splitChat ? 1 : 0);
    }

}
