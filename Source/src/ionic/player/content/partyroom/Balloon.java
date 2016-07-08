package ionic.player.content.partyroom;

import utility.Misc;


public class Balloon {
	
	private int item;
	private int slot;
	private int amount;
	private int x;
	private int y;
	private boolean empty;
	private boolean dropped = false;
	public int obj;
	
	
	public Balloon(int item, int amount, int slot, boolean empty) {
		this.item = item;
		this.amount = amount;
		this.empty = empty;
		this.setSlot(slot);
	}
	
	
	public void drop(int x, int y) {
		this.x = x;
		this.y = y;
		setDropped(true);
		int balloon = PartyRoom.balloonObjects[Misc.random(PartyRoom.balloonObjects.length - 1)];
		this.obj = balloon;
		PartyRoom.makeBalloon(balloon, x, y);
	}
	


	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean isEmpty() {
		return empty;
	}
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
	public boolean isDropped() {
		return dropped;
	}
	public void setDropped(boolean dropped) {
		this.dropped = dropped;
	}
	public int getSlot() {
		return slot;
	}
	public void setSlot(int slot) {
		this.slot = slot;
	}

}
