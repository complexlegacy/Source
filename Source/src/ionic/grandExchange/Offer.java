package ionic.grandExchange;

/**
 * this class handles holding data for an individual offer on the grand exchange
 * @author Keith
 */

public class Offer {
	
	public int slot;//GrandExchangeHandler offers slot
	public int item;//item id
	public int amount;//amount sold
	public int totalAmount;//total amount for sale at beginning
	public int priceEach;//price of each item
	public String type = "";//type "Buy" or "Sell"
	public String ownerName = "";//name of owner
	public int collected = 0;//amount of item collected
	public boolean aborted = false;//offer aborted
	public int collectslot1 = 0; //amount of items in collect slot #1
	public int collectslot2 = 0; //amount of items in collect slot #2
	

	public Offer(int slot, int item, int amount, int price, String type, String owner, int collected, boolean abortedz) {
		this.slot = slot;
		this.item = item;
		this.amount = 0;
		this.totalAmount = amount;
		this.priceEach = price;
		this.type = type;
		this.ownerName = owner;
		this.collected = collected;
		this.aborted = abortedz;
	}
	
	public Offer(int slot, int item, int amount, int tamt, int price, String type, String owner, int collected, boolean abortedz, int cslot1, int cslot2) {
		this.slot = slot;
		this.item = item;
		this.totalAmount = tamt;
		this.amount = amount;
		this.priceEach = price;
		this.type = type;
		this.ownerName = owner;
		this.collected = collected;
		this.aborted = abortedz;
		this.collectslot1 = cslot1;
		this.collectslot2 = cslot2;
	}
	
	
	public int getSlot() {
		return this.slot;
	}
	public int getItem() {
		return this.item;
	}
	public int getAmount() {
		return this.amount;
	}
	public int getTotal() {
		return this.totalAmount;
	}
	public int getPrice() {
		return this.priceEach;
	}
	public String getType() {
		return this.type;
	}
	public String getOwner() {
		return this.ownerName;
	}
	public int getCollected() {
		return this.collected;
	}
	public boolean getAborted() {
		return this.aborted;
	}
	public int getCSlot(int slot) {
		switch(slot) {
			case 1: return this.collectslot1;
			case 2: return this.collectslot2;
		}
		return 0;
	}
	
	
}
