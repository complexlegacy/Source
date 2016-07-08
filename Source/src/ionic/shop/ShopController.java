package ionic.shop;

import java.math.BigInteger;

import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Client;
import ionic.player.PlayerHandler;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import core.Constants;

public class ShopController {
	
	public static final int MAX_SHOPS = 100;
	public static Shop[] shops = new Shop[MAX_SHOPS];
	public static ShopOwner[] owners = new ShopOwner[20];
	private static int[] shopTexts = {43402, 43405, 43408, 43411, 43414, 43417, 43420, 43423, 43426, 43429};
	private static int[] buttons = {169136, 169139, 169142, 169145, 169148, 169151, 169154, 169157, 169160, 169163};
	private static final int MAX_BUY = 1000; //Maximum amount of 1 item you can buy at a time
	private static final int RESTOCK_RATE = 17; //Amount of cycles to go through to add +1 to shop items
	
	
	
	/**
	 * Sets all the shops that the shop owners own
	 */
	public static void setShopOwners() {
		owners[0] = new ShopOwner(new int[] {1,2,3}, "Combat supplies for low levels", 241);
		owners[1] = new ShopOwner(new int[] {4}, "Vannaka's Slayer Shops", 1597);
		owners[2] = new ShopOwner(new int[] {5}, "Martin Thwait's thieving blackmarket", 2270);
		owners[3] = new ShopOwner(new int[] {6, 7}, "Death's Player Killing supplies", 2862);
		owners[4] = new ShopOwner(new int[] {8}, "Pest Control Rewards", 3786);
	}
	
	
	
	/**
	 * Handles player buying items from shop
	 * only if currency is Coins
	 */
	public static void buyFromShop(Client c, int slot, int amount) {
		Currency currency = Currency.findCurrency(shops[c.shopOpen].currency);
		if (currency == null) {
			System.out.println("Currency not found: "+shops[c.shopOpen].currency);
			return;
		}
		if (currency.getAmount(c) <= 0) {
			c.sendMessage("You don't have any "+currency.getType());
			return;
		}
		if (amount > shops[c.shopOpen].amounts[slot]) {
			amount = shops[c.shopOpen].amounts[slot];
		}
		if (ItemAssistant.freeSlots(c) == 0) {
			c.sendMessage("You need more free inventory space.");
			return;
		}
		if (ItemAssistant.freeSlots(c) < amount && !c.noteShop) {
			amount = ItemAssistant.freeSlots(c);
		}
		BigInteger b = new BigInteger(""+shops[c.shopOpen].prices[slot]);
		BigInteger g = new BigInteger(""+amount);
		BigInteger p = b.multiply(g);
		BigInteger max = new BigInteger(""+Integer.MAX_VALUE);
		if (p.compareTo(max) > 0) {
			c.sendMessage("Total price is too high.");
			return;
		}
		if (!currency.has(c, shops[c.shopOpen].prices[slot] * amount)) {
			c.sendMessage("You can't buy that many");
			amount = (currency.getAmount(c) / shops[c.shopOpen].prices[slot]);
		}
		if (!currency.has(c, shops[c.shopOpen].prices[slot] * amount) || amount <= 0) {
			return;
		}
		if(amount > MAX_BUY) {
			amount = MAX_BUY;
			c.sendMessage("You may only buy "+MAX_BUY+" of an item at a time.");
		}
		currency.spend(c, (shops[c.shopOpen].prices[slot] * amount));
		shops[c.shopOpen].amounts[slot] -= amount;
			if (c.noteShop) {
				if (ItemData.data[shops[c.shopOpen].items[slot] + 1].stackable) {
					ItemAssistant.addItem(c,shops[c.shopOpen].items[slot] + 1, amount);
				} else {
					ItemAssistant.addItem(c,shops[c.shopOpen].items[slot], amount);
				}
			} else {
				ItemAssistant.addItem(c,shops[c.shopOpen].items[slot], amount);
			}
			if (shops[c.shopOpen].amounts[slot] == 0 && shops[c.shopOpen].buyAll) {
				shops[c.shopOpen].amounts[slot] = 1;
				shops[c.shopOpen].items[slot] = -1;
				shops[c.shopOpen].prices[slot] = 0;
			}
		refreshShops();
		ItemAssistant.updateInventory(c);
		c.getPA().updatePlayerTab();
	}
	
	
	/**
	 * Handles player selling items to shop
	 */
	public static void sellToShop(Client c, int item, int amount) {
		Currency currency = Currency.findCurrency(shops[c.shopOpen].currency);
		if (currency == null) {
			System.out.println("Currency not found: "+shops[c.shopOpen].currency);
			return;
		}
		if (ItemAssistant.playerHasItem(c, item)) {
			if (ItemAssistant.getItemAmount(c, item) < amount) {
				amount = ItemAssistant.getItemAmount(c, item);
			}
			boolean untradable = untradable(item);
			boolean noted = ItemData.data[item].getUnnoted() > -1;
			if (untradable) {
				c.sendMessage("You cannot sell "+ItemAssistant.getItemName(item)+" to stores.");
				return;
			}
			boolean shopBuys = false;
			int putSlot = getFreeShopSlot(c.shopOpen);
			if (shops[c.shopOpen].buyAll == true) {
				shopBuys = true;
				for (int i = 0; i < shops[c.shopOpen].items.length; i++) {
					if (shops[c.shopOpen].items[i] == (noted ? (ItemData.data[item].getUnnoted()) : item)) {
						putSlot = i;
						break;
					}
				}
			}
			if (shops[c.shopOpen].buyBack == true) {
				for (int i = 0; i < shops[c.shopOpen].items.length; i++) {
					if (shops[c.shopOpen].items[i] == (noted ? (ItemData.data[item].getUnnoted()) : item)) {
						shopBuys = true;
						putSlot = i;
						break;
					}
				}
			}
			if (!shopBuys) {
				c.sendMessage("You can not sell "+ItemAssistant.getItemName(item)+" at this store.");
				return;
			}
			ItemAssistant.deleteItemForBank(c, item, amount);
			int o = shops[c.shopOpen].items[putSlot];
			int f = shops[c.shopOpen].items[putSlot] = (noted ? (ItemData.data[item].getUnnoted()) : item);
			boolean g = false;
			if (o == f)
				g = true;
			if (shops[c.shopOpen].amounts[putSlot] > 1) {
				shops[c.shopOpen].amounts[putSlot] += amount;
			} else {
				shops[c.shopOpen].amounts[putSlot] = amount;
			}
			if (!g)
				shops[c.shopOpen].prices[putSlot] = getItemShopValue((noted ? (ItemData.data[item].getUnnoted()) : item));
			if (shops[c.shopOpen].buyAll || shops[c.shopOpen].buyBack) {
				currency.give(c, getItemShopValue(c, (noted ? (ItemData.data[item].getUnnoted()) : item)) * amount);
			}
			refreshShops();
			c.getPA().updatePlayerTab();
		}
	}
	
	/**
	 * Checks if an item is untradable
	 */
	public static boolean untradable(int item) {
		if (item == 995) {
			return true;
		}
        for (int j = 0; j < Constants.ITEMS_TO_INVENTORY_ON_DEATH.length; j++) {
                if (item == Constants.ITEMS_TO_INVENTORY_ON_DEATH[j]) {
                    return true;
                }
        }
                if (ItemData.data[item].isUntradable()) {
                    return true;
                }
        for (int j = 0; j < Constants.ITEMS_TO_DISSAPEAR.length; j++) {
                if (item == Constants.ITEMS_TO_DISSAPEAR[j]) {
                    return true;
                }
        }
		return false;
	}
	
	/**
	 * gets a empty slot id in a shop
	 */
	public static int getFreeShopSlot(int shop) {
		for(int i = 0; i < 50; i++){
			if (shops[shop].items[i] <= 0) {
				return i;
			}
		}
		return -1;
	}
	
	
	/**
	 * if a player is in a shop and the item's amount changes, the shop updates
	 */
	public static void refreshShops() {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			Client c = (Client)PlayerHandler.players[i];
			if (c != null) {
				if (c.shopOpen > -1 && c.shopOwner > -1) {
					updateShop(c, c.shopOpen);
				}
			}
		}
	}
	
	
	/**
	 * When a player trys to value a shop item
	 * it sends them the price of the item.
	 */
	public static void valueShopItem(Client c, int slot) {
		Shop s = shops[c.shopOpen];
		int i = s.items[slot];
		String n = ItemAssistant.getItemName(i);
		String cu = s.currency.toLowerCase();
		c.sendMessage("the item : "+n+" costs "+s.prices[slot]+" "+cu+" at this store.");
	}
	
	/**
	 * when a player values the item that they want to sell to the shop
	 */
	public static void valueShopSellItem(Client c, int item) {
		String result = "";
		if (shops[c.shopOpen].buyAll == false || shops[c.shopOpen].buyBack == false) {
			result = "This shopkeeper doesn't buy items.";
		}
		if (shops[c.shopOpen].buyBack == true) {
			if (getShopItemSlot(c.shopOpen,item) < 0) {
				c.sendMessage("The shopkeeper does not want this item");
				return;
			}
			double amountz = shops[c.shopOpen].prices[getShopItemSlot(c.shopOpen,item)] * 0.85;
			int amount = (int) amountz;
			result = "The shopkeeper will buy "+ItemAssistant.getItemName(item)+" for "+amount+" "+shops[c.shopOpen].currency;
			c.sendMessage(result);
			return;
		}
		if (shops[c.shopOpen].buyAll == true) {
			double amountz = getItemShopValue(item) * 0.75;
			int amount = (int) amountz;
			result = "The shopkeeper will buy "+ItemAssistant.getItemName(item)+" for "+amount+" "+shops[c.shopOpen].currency;
			c.sendMessage(result);
			return;
		}
	}
	
	
	/**
	 * gets the item value as an integer
	 */
	public static int getItemShopValue(Client c, int item) {
		int price = 0;
		if (shops[c.shopOpen].buyAll == false && shops[c.shopOpen].buyBack == false) {
			price = -1;
			return price;
		}
		if (shops[c.shopOpen].buyBack == true) {
			if (getShopItemSlot(c.shopOpen,item) < 0) {
				price = -1;
				return price;
			}
			double amountz = shops[c.shopOpen].prices[getShopItemSlot(c.shopOpen,item)] * 0.85;
			price = (int) amountz;
			return price;
		}
		if (shops[c.shopOpen].buyAll == true) {
			double amountz = getItemShopValue(item) * 0.75;
			price = (int) amountz;
			return price;
		}
		return price;
	}
	
	
	/**
	 * gets the item's value from the item.cfg file
	 */
	public static int getItemShopValue(int itemId) {
		return ItemAssistant.getItemShopValue(itemId);
	}
	
	
	/**
	 * gets the item slot of an item id in a shop
	 */
	public static int getShopItemSlot(int shop, int item) {
		if (ItemData.data[item].getUnnoted() > -1) {
			item = ItemData.data[item].getUnnoted();
		}
		for (int i = 0; i < 50; i++) {
			if (shops[shop].items[i] == item) {
				return i;
			}
		}
		return -1;
	}
	
	
	/**
	 * Opens the shop interface according to the shop owner,
	 * loads all of the owner's shops
	 */
	public static void openShopOwner(Client c, int shopOwner) {
		for (int i = 0; i < 20; i++) {
			if (owners[i] != null) {
				if (owners[i].ownerId == shopOwner) {
					for (int j = 0; j < 10; j++) {
						c.getPA().sendFrame126(" ", shopTexts[j]);
					}
					for (int j = 0; j < owners[i].shops.length; j++) {
						c.getPA().sendFrame126(""+shops[owners[i].shops[j]].name+"", shopTexts[j]);
					}
						c.getPA().sendFrame126(""+owners[i].ownerName+"", 43002);
						c.shopOwner = i;
						updateShop(c, owners[i].shops[0]);
					break;
				}
			}
		}
	}
	
	
	/**
	 * updates the shop for the player, 'refreshes' it.
	 */
	public static void updateShop(Client c, int shop) {
		c.shopOpen = shop;
		String currency = shops[shop].currency;
		c.getPA().sendFrame126("Shop currency: "+currency, 43051);
		loadItems(c, shop);
		c.getPA().showInterface(43000);
	}
	
	/**
	 * loads the shop's items on the shop interface
	 */
	public static void loadItems(Client c, int shop) {
		String s = ""; 
		if (shops[shop] == null) { return; }
		for (int i = 0; i < 50; i++) {
			int j = shops[shop].items[i];
			int a = shops[shop].amounts[i];
			if (j > 0) {
				if (c.noteShop) {
					if (ItemData.data[j].getNoted() > -1) {
						j = ItemData.data[j].getNoted();
					}
				}
			} else {
				j = -1;
			}
			s += "# "+i+" "+(j + 1)+" "+a+" ";
			c.getPA().sendFrame126(""+convertPrice(shops[shop].prices[i])+"", 43300+i);
		}
		c.getPA().sendFrame126(s, 43025);
	}
	
	
	/**
	 * Converts the item's price into a number with 'k' 'm' 'b' at the end of it
	 * to shorten the price on the interface in the item box.
	 */
	public static String convertPrice(int p) {
		if (p == 0) {
			return "<col=0xffffff>FREE";
		}
		String b = "";
		String col = "";
		if (p < 10000) { b = "";};
		if (p >= 10000000) { p /= 1000000; b = "m"; col = "<col=65280>"; }
		if (p >= 100000) { p /= 1000; b = "k"; col = "<col=0xffffff>"; }
		if (p >= 10000) { p /= 1000; b = "k"; col = ""; }
		return ""+col+""+p+""+b+"";
	}
	
	
	/**
	 * Handles selecting a shop from the list of shops owned by that person.
	 */
	public static void selectShop(Client c, int button) {
		if (button == 167251) {
			c.getPA().closeAllWindows();
			c.shopOpen = -1;
			c.shopOwner = 1;
			return;
		}
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i] == button) {
				updateShop(c, owners[c.shopOwner].shops[i]);
				break;
			}
		}
	}
	
	/**
	 * calls doRestock every certain amount of cycles
	 */
	public void restockShops() {
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
			public void execute(CycleEventContainer container) {
				doRestock();
				refreshShops();
			}
			public void stop() { }
		}, RESTOCK_RATE);
	}
	
	/**
	 * adds +1 to shops, being called by a CycleEvent.
	 */
	public void doRestock() {
		for (int i = 0; i < MAX_SHOPS; i++) {
			if (shops[i] != null) {
				for (int j = 0; j < shops[i].amounts.length; j++) {
					if (shops[i].maxAmounts[j] > shops[i].amounts[j]) {
						shops[i].amounts[j] += 1;
					}
				}
			}
		}
	}
	

}
