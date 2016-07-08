package ionic.grandExchange;

import ionic.item.ItemData;
import ionic.player.Player;

/**
 * This class handles most of the interfaces
 * @author Keith / Hitten
 */

public class Interfaces {
	
	/**
	 * Resets the 'sell' interface info
	 */
	public static void resetSellInterface(Player c) {
		c.getPA().sendFrame126("Choose an item to exchange", 24769);
		c.getPA().sendFrame126("Select an item from your inventory to sell.", 24770);
		c.getPA().sendFrame126("1", 24771);
		c.getPA().sendFrame126("1 gp", 24772);
		c.getPA().sendFrame126("1 gp", 24773);
		c.getPA().sendFrame126("N/A", 24782);
		c.getPA().sendFrame34a(24780, -1, 0, 1);
		c.getPD().sellAmount = 1;
		c.getPD().sellItem = 0;
		c.getPD().sellPrice = 1;
		c.getPD().sellDefaultValue = 0;
	}
	/**
	 * Resets the 'buy' interface info
	 */
	public static void resetBuyInterface(Player c) {
		c.getPA().sendFrame126("Choose an item to exchange", 24669);
		c.getPA().sendFrame126("Grand Exchange: Search for an item to buy", 24670);
		c.getPA().sendFrame126("1", 24671);
		c.getPA().sendFrame126("1 gp", 24672);
		c.getPA().sendFrame126("1 gp", 24673);
		c.getPA().sendFrame126("N/A", 24682);
		c.getPA().sendFrame34a(24680, -1, 0, 1);
		c.getPD().buyAmount = 1;
		c.getPD().buyItem = 0;
		c.getPD().buyPrice = 1;
		c.getPD().buyDefaultValue = 0;
	}
	
	
	
	
	/**
	 * When clicking an item on the item search
	 * @param c				The player who did the action
	 * @param item			The item id searched
	 * @param name			The item name
	 * @param examine		The item's examine text
	 */
	public static void clickSearch(Player c, int item) {
		if (ItemData.data[item].isNoted()) {
			item = ItemData.data[item].getUnnoted();
		}
		c.getPA().sendFrame126(ItemData.data[item].getName(), 24669);
		c.getPA().sendFrame126(ItemData.data[item].getDescription(), 24670);
		c.getPA().sendFrame34a(24680, item, 0, 1);
		int valueAmount = ItemData.data[item].shopValue;
		String value = insertCommas(""+valueAmount+"");
		c.getPA().sendFrame126(value, 24682);
		c.getPA().sendFrame126(value, 24672);
		c.getPA().sendFrame126(value, 24673);
		if (c.getPD().buyAmount == 0) {
		c.getPD().buyAmount = 1;
		c.getPA().sendFrame126("1", 24671);
		}
		c.getPD().buyItem = item;
		c.getPD().buyPrice = valueAmount;
		c.getPD().buyDefaultValue = valueAmount;
		Other.updatePrice(c);
	}
	
	
	
	
	private static String insertCommas(String str) {
	    if(str.length() < 4){
	        return str;
	    }
	    return insertCommas(str.substring(0, str.length() - 3)) +  "," + str.substring(str.length() - 3, str.length());
	}
	
}
