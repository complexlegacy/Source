package ionic.player.content.miscellaneous;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import utility.Misc;

/**
 * Caskets.
 * @author MGT Madness, created on 07-07-2014.
 */
public class Casket 
{
	
	/**
	 * Tier 1 casket reward.
	 * @param player
	 * 			The associated player.
	 */
	public static void smallCasketItems(Player player)
    {
    		ItemAssistant.deleteItem(player, 2714, 1);
            int[] arrayOfItems = {
                    2587, 2589, //Full black (t)
                    2595, 2597, //Full black (g)
                    2633, 2635, 2637, //Beret
                    7392, 7396, 7388, //Full wizard (t)
                    7386, 7390, 7394, //Full wizard (g)
                    7364, 7368, //Full studded (t)
                    7366, 7362, //Full studded (g)
                    10404, 10406, //Red elegant full
                    10408, 10410, //Blue elegant full
                    10412, 10414, //Green elegant full
                    10738, //Amulet of magic (t)
                    10458, 10460, 10462, 10464, 10466, 10468, 10470, 10472, 10474, 10440, 10442,
                    10444, 10446, 10448, 10450, 10452, 10454, 10456 //Vestment robes full (saradomin/zamorak/guthix robe)
            };
            ItemAssistant.addItem(player, arrayOfItems[Misc.random(arrayOfItems.length - 1)], 1);
            player.gfx0(199);
    }
	
	/**
	 * Tier 2 casket reward.
	 * @param player
	 * 			The associated player.
	 */
	public static void mediumCasketItems(Player player)
    {
			ItemAssistant.deleteItem(player, 2714, 1);
            int[] arrayOfItems = {
                    2599, 2601, 2603, 2605, //Full adamant (t)
                    2607, 2609, 2611, 2613, //Full adamant (g)
                    2579, //Wizard boots
                    2645, 2649, //Headbands
                    10400, 10402, 10416, 10418, 10420, 10422, //Black/white/purple elegant full
                    10364 //Amulet of strength (t)
            };
            ItemAssistant.addItem(player, arrayOfItems[Misc.random(arrayOfItems.length - 1)], 1);
            player.gfx0(199);
    }

	/**
	 * Tier 3 casket reward.
	 * @param player
	 * 			The associated player.
	 */
    public static void bigCasketNormalItems(Player player)
    {
    		ItemAssistant.deleteItem(player, 2714, 1);
    		player.gfx0(199);
            int[] arrayOfItems = {
                    // 55 items
                    2615, 2617, 2619, 2621, 2623,
                    2625,
                    2627,
                    2629, // Full rune (g) and (t)
                    2653, 2655, 2657, 2659, 2661, 2663, 2665, 2667,
                    2669,
                    2671,
                    2673,
                    2675, // Saradomin/Zamorak/Guthix full rune armour
                    8950, // Pirate hat
                    2639,
                    2641,
                    2643, // Cavalier set
                    10362, // Amulet of glory (t)
                    10368, 10370, 10372, 10374, 10376, 10378, 10380, 10382, 10384,
                    10386, 10388,
                    10390, // Blessed dhide set
                    2581, // Robin hood hat
                    2577, // Ranger boots
                    3481, 3483, 3485, 3486, 3488 // Full gilded armour
            };
            ItemAssistant.addItem(player, arrayOfItems[Misc.random(arrayOfItems.length - 1)], 1);
    }

    /**
     * Third-age casket reward.
     * @param player
     * 			The associated player.
     */
    public static void thirdAgeItems(Player player)
    {
            ItemAssistant.deleteItem(player, 2717, 1);
            player.gfx0(199);
            int[] arrayOfItems = {
                    10330, 10332, 10334, 10336, 10338, 10340, 10342,
                    10344, 10346, 10348, 10350, 10352 // 3rd age set
            };
            ItemAssistant.addItem(player, arrayOfItems[Misc.random(arrayOfItems.length - 1)], 1);
    }

}
