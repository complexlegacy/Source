package ionic.player.content.miscellaneous;

import ionic.item.ItemAssistant;
import ionic.player.Player;

/**
 * Abyssal whip colouring.
 * @author MGT Madness, created on 08-04-2014.
 */
public class WhipColouring 
{
	static int WHITE_DYE = 1773;
	static int BLUE_DYE = 1767;
	static int GREEN_DYE = 1771;
	static int YELLOW_DYE = 1765;
	static int ABYSSAL_WHIP = 4151;
	static int WHITE_ABYSSAL_WHIP = 15443;
	static int BLUE_ABYSSAL_WHIP = 15442;
	static int GREEN_ABYSSAL_WHIP = 15444;
	static int YELLOW_ABYSSAL_WHIP = 15441;
	
	/**
	 * Abyssal whip dying.
	 * @param player
	 * 			The associated player.
	 * @param itemUsed
	 * 			The item being used.
	 * @param usedWith
	 * 			The item being used on.
	 */
	public static void combine(Player player, int itemUsed, int usedWith)
	{
		if (itemUsed == WHITE_DYE && usedWith == ABYSSAL_WHIP || itemUsed == ABYSSAL_WHIP && usedWith == WHITE_DYE)
		{
			ItemAssistant.deleteItem(player, WHITE_DYE, 1);
			ItemAssistant.deleteItem(player, ABYSSAL_WHIP, 1);
			ItemAssistant.addItem(player, WHITE_ABYSSAL_WHIP, 1);
		}
		else if (itemUsed == BLUE_DYE && usedWith == ABYSSAL_WHIP || itemUsed == ABYSSAL_WHIP && usedWith == BLUE_DYE)
		{
			ItemAssistant.deleteItem(player, BLUE_DYE, 1);
			ItemAssistant.deleteItem(player, ABYSSAL_WHIP, 1);
			ItemAssistant.addItem(player, BLUE_ABYSSAL_WHIP, 1);
		}
		else if (itemUsed == GREEN_DYE && usedWith == ABYSSAL_WHIP || itemUsed == ABYSSAL_WHIP && usedWith == GREEN_DYE)
		{
			ItemAssistant.deleteItem(player, GREEN_DYE, 1);
			ItemAssistant.deleteItem(player, ABYSSAL_WHIP, 1);
			ItemAssistant.addItem(player, GREEN_ABYSSAL_WHIP, 1);
		}
		else if (itemUsed == YELLOW_DYE && usedWith == ABYSSAL_WHIP || itemUsed == ABYSSAL_WHIP && usedWith == YELLOW_DYE)
		{
			ItemAssistant.deleteItem(player, YELLOW_DYE, 1);
			ItemAssistant.deleteItem(player, ABYSSAL_WHIP, 1);
			ItemAssistant.addItem(player, YELLOW_ABYSSAL_WHIP, 1);
		}
	}

}
