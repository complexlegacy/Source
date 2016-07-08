package ionic.grandExchange;

import java.io.*;

import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Player;

/**
 * @author Keith / Hitten
 */

public class Other {

	/**
	 * Handles decreasing the price for the offer to buy
	 */
	public static void decreaseBuyPrice(Player c, int amount) {
		if (c.getPD().buyItem <= 0) {
			c.sendMessage("Select an item to buy first!");
			return;
		}
		if (c.getPD().buyPrice - amount < 1) {
			c.getPD().buyPrice = 1;
			updatePrice(c);
			return;
		}
		c.getPD().buyPrice -= amount;
		updatePrice(c);
	}

	/**
	 * Handles increasing the price for the offer to buy
	 */
	public static void increaseBuyPrice(Player c, int amount) {
		c.getPD().something = c.getPD().buyPrice;
		if (c.getPD().something + amount > Integer.MAX_VALUE) {
			c.getPD().buyPrice = Integer.MAX_VALUE;
			updatePrice(c);
			return;
		}

		if (c.getPD().buyItem <= 0) {
			c.sendMessage("Select an item to buy first!");
			return;
		}
		c.getPD().buyPrice += amount;
		updatePrice(c);
	}

	/**
	 * Handles decreasing the amount of an item to buy
	 */
	public static void decreaseBuyAmount(Player c, int amount) {

		if (c.getPD().buyItem <= 0) {
			c.sendMessage("Select an item to buy first!");
			return;
		}
		if (c.getPD().buyAmount - amount < 1) {
			c.getPD().buyAmount = 1;
			updateAmount(c);
			return;
		}
		c.getPD().buyAmount -= amount;
		updateAmount(c);
	}

	/**
	 * Handles increasing the amount of an item to buy
	 */
	public static void increaseBuyAmount(Player c, int amount) {
		c.getPD().something = c.getPD().buyAmount;
		if (c.getPD().something + amount > Integer.MAX_VALUE) {
			c.getPD().buyAmount = Integer.MAX_VALUE;
			updateAmount(c);
			return;
		}

		if (c.getPD().buyItem <= 0) {
			c.sendMessage("Select an item to buy first!");
			return;
		}
		c.getPD().buyAmount += amount;
		updateAmount(c);
	}





	/**
	 * Handles updating the info on interface
	 */
	public static void updatePrice(Player c) {
		String s = insertCommas(""+c.getPD().buyPrice+"");
		c.getPA().sendFrame126(""+s+" gp", 24672);
		updateTotal(c);
	}
	public static void updateAmount(Player c) {
		String s = insertCommas(""+c.getPD().buyAmount+"");
		c.getPA().sendFrame126(s, 24671);
		updateTotal(c);
	}
	public static void updateTotal(Player c) {
		String s = "";
		c.getPD().buyTotal = c.getPD().buyAmount;
		c.getPD().buyTotal *= c.getPD().buyPrice;
		if (c.getPD().buyTotal > Integer.MAX_VALUE) {
			s = "Too High!";
		} else {
			s = insertCommas(""+c.getPD().buyTotal+" gp");
		}
		c.getPA().sendFrame126(""+s+"", 24673);
	}




	private static String insertCommas(String str) {
		if(str.length() < 4){
			return str;
		}
		return insertCommas(str.substring(0, str.length() - 3)) +  "," + str.substring(str.length() - 3, str.length());
	}












	/**
	 * Handles decreasing the sell price
	 */
	public static void decreaseSellPrice(Player c, int amount) {
		if (c.getPD().sellItem <= 0) {
			c.sendMessage("Select an item to sell first!");
			return;
		}
		if (c.getPD().sellPrice - amount < 1) {
			c.getPD().sellPrice = 1;
			updateSellPrice(c);
			return;
		}
		c.getPD().sellPrice -= amount;
		updateSellPrice(c);
	}

	/**
	 * Handles increasing the sell price
	 */
	public static void increaseSellPrice(Player c, int amount) {
		c.getPD().something = c.getPD().sellPrice;
		if (c.getPD().something + amount > Integer.MAX_VALUE) {
			c.getPD().sellPrice = Integer.MAX_VALUE;
			updateSellPrice(c);
			return;
		}

		if (c.getPD().sellItem <= 0) {
			c.sendMessage("Select an item to sell first!");
			return;
		}
		c.getPD().sellPrice += amount;
		updateSellPrice(c);
	}

	/**
	 * Handles decreasing the sell amount
	 */
	public static void decreaseSellAmount(Player c, int amount) {
		if (c.getPD().sellItem <= 0) {
			c.sendMessage("Select an item to sell first!");
			return;
		}
		if (c.getPD().sellAmount - amount < 1) {
			c.getPD().sellAmount = 1;
			updateSellAmount(c);
			return;
		}
		c.getPD().sellAmount -= amount;
		updateSellAmount(c);
	}

	/**
	 * Handles decreasing the sell amount
	 */
	public static void increaseSellAmount(Player c, int amount) {
		c.getPD().something = c.getPD().sellAmount;
		if (c.getPD().something + amount > Integer.MAX_VALUE) {
			c.getPD().sellAmount = Integer.MAX_VALUE;
			updateSellAmount(c);
			return;
		}

		if (c.getPD().sellItem <= 0) {
			c.sendMessage("Select an item to sell first!");
			return;
		}
		c.getPD().sellAmount += amount;
		updateSellAmount(c);
	}


	public static void updateSellPrice(Player c) {
		String s = insertCommas(""+c.getPD().sellPrice+"");
		c.getPA().sendFrame126(""+s+" gp", 24772);
		updateTotalSell(c);
	}
	public static void updateSellAmount(Player c) {
		String s = insertCommas(""+c.getPD().sellAmount+"");
		c.getPA().sendFrame126(s, 24771);
		updateTotalSell(c);
	}

	public static void updateTotalSell(Player c) {
		String s = "";
		c.getPD().sellTotal = c.getPD().sellAmount;
		c.getPD().sellTotal *= c.getPD().sellPrice;
		if (c.getPD().sellTotal > Integer.MAX_VALUE) {
			s = "Too High!";
		} else {
			s = insertCommas(""+c.getPD().sellTotal+" gp");
		}
		c.getPA().sendFrame126(""+s+"", 24773);
	}


	public static void applySell(Player c, int item) {
		if (ItemData.data[item].isNoted()) {
			item = ItemData.data[item].getUnnoted();
		}
		c.getPA().sendFrame126(ItemData.data[item].getName(), 24769);
		c.getPA().sendFrame126(ItemData.data[item].getDescription(), 24770);
		c.getPA().sendFrame34a(24780, item, 0, 1);
		int valueAmount = ItemData.data[item].shopValue;
		String value = insertCommas(""+valueAmount+"");
		c.getPA().sendFrame126(value, 24782);
		c.getPA().sendFrame126(value, 24772);
		c.getPA().sendFrame126(value, 24773);
		if (c.getPD().sellAmount == 0) {
			c.getPD().sellAmount = 1;
			c.getPA().sendFrame126("1", 24771);
		}
		c.getPD().sellItem = item;
		c.getPD().sellPrice = valueAmount;
		c.getPD().sellDefaultValue = valueAmount;
		Other.updateSellPrice(c);

	}






	public static void confirmSell(Player c) {
		int slot = getOfferSlot();
		int item = c.getPD().sellItem;
		int amount = c.getPD().sellAmount;
		int price = c.getPD().sellPrice;
		String type = "Sell";
		String name = c.playerName;

		if (ItemData.data[item].isNoted()) {
			item = ItemData.data[item].getUnnoted();
		}

		if (!canConfirmSell(c, item, amount)) {
			return;
		}

		if (slot == -1) {
			c.sendMessage("Could not complete offer at this time. Grand Exchange Full!");
			return;
		}
		if (ItemData.data[item].stackable) {
			ItemAssistant.deleteItem1(c, item, amount);
		} else {
			ItemAssistant.deleteItemWithNotes(c, item, amount);
		}

		GrandExchangeHandler.offers[slot] = new Offer(slot, item, amount, price, type, name, 0, false);
		c.getPD().geSlots[c.getPD().slotSelected] = slot;
		openGE(c);
		GrandExchangeHandler.saveAll();
		GrandExchangeHandler.process(slot);
	}

	public static void confirmBuy(Player c) {
		if (!canConfirmBuy(c)) {
			return;
		}
		int slot = getOfferSlot();
		int item = c.getPD().buyItem;
		int amount = c.getPD().buyAmount;
		int price = c.getPD().buyPrice;  
		String type = "Buy";
		String name = c.playerName;

		if (slot == -1) {
			c.sendMessage("Could not complete offer at this time. Grand Exchange Full!");
			return;
		}
		ItemAssistant.deleteItem1(c, 995, price * amount);
		GrandExchangeHandler.offers[slot] = new Offer(slot, item, amount, price, type, name, 0, false);
		c.getPD().geSlots[c.getPD().slotSelected] = slot;
		openGE(c);
		GrandExchangeHandler.saveAll();
		GrandExchangeHandler.process(slot);
	}


	public static int getOfferSlot() {
		for (int i = 1; i < GrandExchangeHandler.MAX_OFFERS; i++) {
			if (GrandExchangeHandler.offers[i] == null) {
				return i;
			}
		}
		return -1;
	}


	public static void openGE(Player c) {
		for (int i = 0; i < 6; i++) {
			refreshSlot(c, i);
		}
		c.getPA().showInterface(50000);
	}

	public static void refreshSlot(Player c, int slot) {
		Offer slotOffer = GrandExchangeHandler.offers[c.getPD().geSlots[slot]];
		if (c.getPD().geSlots[slot] > 0 && slotOffer != null) {
			changeGeSlot(c, slot, 1);
			int k = 50200 + (slot * 25);
			c.getPA().sendFrame34a(k+5, slotOffer.getItem(), 0, slotOffer.getTotal());

			String name = ItemAssistant.getItemName(slotOffer.getItem());
			String name2 = " ";

			if (name.length() > 15) {
				String[] split = name.split(" ");
				name = name.replaceAll(split[split.length - 1], "");
				name2 = split[split.length - 1];
			}

			c.getPA().sendFrame126(name, k+9);
			c.getPA().sendFrame126(name2, k+10);
			c.getPA().sendFrame126(""+insertCommas(""+slotOffer.getPrice()+"")+" gp", k+11);
			c.getPA().sendFrame126(""+slotOffer.getType()+"", 50002+slot);

			double percent = (slotOffer.getAmount() * 100) / slotOffer.getTotal();
			int color = 2;
			if (slotOffer.type.equalsIgnoreCase("buy")) {
				color = 1;
			}
			if (!slotOffer.getAborted()) { progressGE(c, slot, 1, (int)percent, color); }
			else if (slotOffer.getAborted()) { progressGE(c, slot, 1, 100, 0); }
		} else {
			changeGeSlot(c, slot, 2);
			c.getPA().sendFrame126("Empty", 50002+slot);
			progressGE(c, slot, 1, 100, 3);
		}
	}

	public static boolean canConfirmBuy(Player c) {
		if (c.getPD().buyItem < 1) {
			c.sendMessage("Please select an item to buy first");
			return false;
		}
		if (c.getPD().buyTotal > Integer.MAX_VALUE) {
			c.sendMessage("Total price is too high!");
			return false;
		}
		if (!ItemAssistant.playerHasItem(c, 995, (int)c.getPD().buyTotal)) {
			c.sendMessage("You need more coins!");
			return false;
		}
		return true;
	}

	public static boolean canConfirmSell(Player c, int item, int amount) {
		if (ItemData.data[item].isNoted()) {
			item = ItemData.data[item].getUnnoted();
		}
		if (c.getPD().sellItem < 1) {
			c.sendMessage("Please select an item to sell first");
			return false;
		}
		if (!ItemAssistant.playerHasItemWithNotes(c, item, amount)) {
			c.sendMessage("You don't have "+amount+"x : "+ItemAssistant.getItemName(item));
			return false;
		}
		if (c.getPD().sellTotal > Integer.MAX_VALUE) {
			c.sendMessage("Total price is too high!");
			return false;
		}
		return true;
	}




	public static void customSellAmount(Player c, int amount) {
		c.getPD().sellAmount = amount;
		updateSellAmount(c);
	}
	public static void customSellPrice(Player c, int amount) {
		c.getPD().sellPrice = amount;
		updateSellPrice(c);
	}
	public static void customBuyAmount(Player c, int amount) {
		c.getPD().buyAmount = amount;
		updateAmount(c);
	}
	public static void customBuyPrice(Player c, int amount) {
		c.getPD().buyPrice = amount;
		updatePrice(c);
	}

	public static void abortOffer(Player c, int slot, int button) {
		Offer o = GrandExchangeHandler.offers[c.getPD().geSlots[slot]];
		if (o.getAborted()) { return; }
		o.aborted = true;
		refreshSlot(c, slot);
		progressGE(c, 0, 2, 100, 0);

		if (o.getType().equalsIgnoreCase("sell")) {
			int amount = (o.getTotal() - o.getAmount());
			o.collectslot1 = amount;
		}
		if (o.getType().equalsIgnoreCase("buy")) {
			int amount = (o.getTotal() - o.getAmount()) * o.getPrice();
			if (o.getAmount() - o.getCollected() > 0) {
				amount += (o.getAmount() - o.getCollected()) * o.getPrice();
			}
			o.collectslot2 = amount;
		}

		if (button == 201060 || button == 199072) {
			viewOffer(c, slot);
		}
		GrandExchangeHandler.save(o.getSlot());
	}


	public static void viewOffer(Player c, int slot) {
		Offer o = GrandExchangeHandler.offers[c.getPD().geSlots[slot]];
		if (o == null)
			return;
		viewOfferTexts(c,o);
		c.getPD().slotSelected = slot;
		c.getPA().sendFrame34a(51021, o.getItem(), 0, o.getTotal());

		
		if (o.getCSlot(2) > 0) {
			c.getPA().sendFrame34a(51020, 995, 0, o.getCSlot(2));
		}
		if (o.getCSlot(1) > 0) {
			c.getPA().sendFrame34a(51019, o.getItem(), 0, o.getCSlot(1));
		}
		
		if (o.getAborted()) {
			if (o.getCSlot(2) > 0) {
				c.getPA().sendFrame34a(51020, 995, 0, o.getCSlot(2));
			}
			if (o.getCSlot(1) > 0) {
				c.getPA().sendFrame34a(51019, o.getItem(), 0, o.getCSlot(1));
			}
			progressGE(c, 0, 2, 100, 0);
		} else {
			int percent = (o.getAmount() * 100) / o.getTotal();
			if (o.getType().equalsIgnoreCase("Buy")) {
				progressGE(c, 0, 2, percent, 1);
			} else if (o.getType().equalsIgnoreCase("Sell")) {
				progressGE(c, 0, 2, percent, 2);
			}
		}

		c.getPA().showInterface(51000);
	}

	public static void viewOfferTexts(Player c, Offer o) {
		if (o == null)
			return;
		if (o.getType().equalsIgnoreCase("Buy")) {
			c.getPA().sendFrame126("Buy Offer", 51005);
			c.getPA().sendFrame126("You have bought a total of "+o.getAmount(), 51010);
			c.getPA().sendFrame126("for a total price of "+insertCommas(""+o.getAmount() * o.getPrice()+"")+" gp", 51011);
			c.getPA().sendFrame126(""+insertCommas(""+o.getTotal()+"")+"",51006);
			c.getPA().sendFrame126(""+ItemData.data[o.getItem()].shopValue+"",51001);
			c.getPA().sendFrame126(""+insertCommas(""+o.getPrice()+"")+" gp", 51007);
			c.getPA().sendFrame126(""+insertCommas(""+o.getPrice() * o.getTotal()+"")+" gp", 51008);
		} else if (o.getType().equalsIgnoreCase("Sell")) {
			c.getPA().sendFrame126("Sell Offer", 51005);
			c.getPA().sendFrame126("You have sold a total of "+o.getAmount(), 51010);
			c.getPA().sendFrame126("for a total price of "+insertCommas(""+o.getAmount() * o.getPrice()+"")+" gp", 51011);
			c.getPA().sendFrame126(""+insertCommas(""+o.getTotal()+"")+"",51006);
			c.getPA().sendFrame126(""+ItemData.data[o.getItem()].shopValue+"",51001);
			c.getPA().sendFrame126(""+insertCommas(""+o.getPrice()+"")+" gp", 51007);
			c.getPA().sendFrame126(""+insertCommas(""+o.getPrice() * o.getTotal()+"")+" gp", 51008);
		}
		c.getPA().sendFrame34a(51019, -1, 0, 1);
		c.getPA().sendFrame34a(51020, -1, 0, 1);
		c.getPA().sendFrame34a(51021, -1, 0, 1);
		progressGE(c, 0, 2, 100, 3);
	}


	public static void handleCollect(Player c, String cmd) {
		int slot = c.getPD().slotSelected;
		Offer o = GrandExchangeHandler.offers[c.getPD().geSlots[slot]];
		if (o == null){return;}
		int item = o.getItem();
		if (ItemAssistant.freeSlots(c) < 1) {return;}
		if (cmd.equals("gecollectitemnoted")) {
			if (ItemData.data[item].getNoted() != -1) {
				ItemAssistant.addItem(c, item+1, o.collectslot1);
				c.getPA().sendFrame34a(51019, -1, 0, 1);
				o.collectslot1 = 0;
				o.collected = o.getTotal();
			} else {
				if (ItemData.data[item].stackable) {
					ItemAssistant.addItem(c, item, o.collectslot1);
					c.getPA().sendFrame34a(51019, -1, 0, 1);
					o.collectslot1 = 0;
					o.collected = o.getTotal();
				} else {
					int free = ItemAssistant.freeSlots(c);
					if (o.collectslot1 > free) {
						ItemAssistant.addItem(c, item, (o.collectslot1 - free));
						o.collectslot1 -= free;
						c.getPA().sendFrame34a(51019, item, 0, o.collectslot1);
						o.collected = free;
					} else {
						ItemAssistant.addItem(c, item, o.collectslot1);
						c.getPA().sendFrame34a(51019, -1, 0, 1);
						o.collectslot1 = 0;
						o.collected = o.getTotal();
					}
				}
			}
		}
		if (cmd.equals("gecollectitemunnoted")) {
			int free = ItemAssistant.freeSlots(c);
			if (ItemData.data[item].stackable) {
				if (free < 1) { c.sendMessage("No free space"); return; }
				ItemAssistant.addItem(c, item, o.collectslot1);
				c.getPA().sendFrame34a(51019, -1, 0, 1);
				o.collectslot1 = 0;
			} else {
				if (o.getCSlot(1) > free) {
					ItemAssistant.addItem(c, item, (o.collectslot1 - free));
					o.collectslot1 -= free;
					c.getPA().sendFrame34a(51019, item, 0, o.collectslot1);
					o.collected += free;
				} else {
					ItemAssistant.addItem(c, item, o.collectslot1);
					o.collectslot1 = 0;
					c.getPA().sendFrame34a(51019, -1, 0, 1);
					o.collected = o.getTotal();
				}
			}
		}
		if (cmd.equals("gecollectcash")) {
			ItemAssistant.addItem(c, 995, o.collectslot2);
			o.collectslot2 = 0;
			c.getPA().sendFrame34a(51020, -1, 0, 1);
		}

		if (o.getCSlot(1) <= 0 && o.getCSlot(2) <= 0) {
			completeSlot(c, slot, o);
		}

		GrandExchangeHandler.save(slot);
	}


	public static void completeSlot(Player c, int playerSlot, Offer o) {
		refreshSlot(c, playerSlot);
		c.getPD().geSlots[playerSlot] = 0;
		File file = new File("./data/grandexchange/"+o.getSlot()+".txt");
		file.delete();
		GrandExchangeHandler.offers[o.getSlot()] = null;
		openGE(c);
	}



	public static void changeGeSlot(Player c, int slot, int type) {
		if (type == 1) {
			sendGePacket(c, 1, 0, 50000, 1+slot, 50200+(slot*25));
		} else if (type == 2) {
			sendGePacket(c, 1, 0, 50000, 1+slot, 50050+(slot*25));
		}
	}
	public static void progressGE(Player c, int slot, int type, int amount, int type2) {
		if (type == 1) {
			sendGePacket(c,2, type2, 50204+(slot*25), 0, amount);
		} else if (type == 2) {
			sendGePacket(c,3, type2, 51004, 0, amount);
		}
	}
	public static void sendGePacket(Player c, int type, int type2, int frame, int child, int progress) {
		c.getPA().sendFrame126(""+type+"/"+type2+"/"+frame+"/"+child+"/"+progress+"",50010);
	}


}
