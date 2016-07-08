package ionic.player.banking;

import ionic.player.Player;

/**
 * @author Keith
 * This class is for the variables for each player that has a bank
 */
public class Bank {
	
	/**
	 * The array for the item IDs in the slot inside a tab
	 */
	public int[][] bankItems = new int[BankConstants.TOTAL_TABS][BankConstants.MAX_ITEMS_PER_TAB];
	
	/**
	 * The array for the amount of items in the slot inside a tab
	 */
	public int[][] bankAmounts = new int[BankConstants.TOTAL_TABS][BankConstants.MAX_ITEMS_PER_TAB];
	
	/**
	 * The array for the charges of the items
	 */
	public int[][] bankCharges = new int[BankConstants.TOTAL_TABS][BankConstants.MAX_ITEMS_PER_TAB];
	
	/**
	 * The ID of the bank tab that the player currently has selected
	 */
	public int tabSelected = 0;
	
	/**
	 * If a player trys to withdraw while collapsing
	 */
	public boolean collapsing = false;
	
	
	/**
	 * Creates the instance for the player
	 */
	public Player c;
	public Bank(Player p) {
		this.c = p;
	}
	
	/**
	 * The amount of an item to deposit that's sent from the client
	 * before the server reads the packet for depositing
	 */
	public int depositAmountReceived;
	
	
	
	/**
	 * The amount of an item to withdraw that's sent from the client
	 * before the server reads the packet for withdrawing
	 */
	public int withdrawAmountReceived;
	
	
	/**
	 * Gets the amount of bank items the player is allowed to have
	 */
	public int getMaxBankItems() {
		int max = 350;
			switch(c.playerRights) {
				case 1: max = 750; break;
				case 2: max = 1350; break;
				case 3: max = 425; break;
				case 4: max = 620; break;
				case 5: max = 850; break;
				case 6: max = 1100; break;
				case 7: max = 700; break;
				case 8: max = 400; break;
				case 9: max = 475; break;
				case 10: max = 600; break;
			}
		return max;
	}
	
	/**
	 * If the player has the note option on
	 */
	public boolean note = false;
	
	/**
	 * If the player's drag mode is on insert
	 */
	public boolean insert = false;
	
	/**
	 * If the player is searching or not
	 */
	public boolean searching = false;
	
	/**
	 * Saves the searching items
	 */
	public int[][] searchItems = new int[BankConstants.TOTAL_TABS][BankConstants.MAX_ITEMS_PER_TAB];
	public int[][] searchAmounts = new int[BankConstants.TOTAL_TABS][BankConstants.MAX_ITEMS_PER_TAB];
	public int[][] searchCharges = new int[BankConstants.TOTAL_TABS][BankConstants.MAX_ITEMS_PER_TAB];
	
	/**
	 * Remembers the last search
	 */
	public String lastSearch = "";
	

}
