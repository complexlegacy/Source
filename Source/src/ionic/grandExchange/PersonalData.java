package ionic.grandExchange;

import ionic.player.Player;

public class PersonalData {
	
	public PersonalData(Player c) {
		this.c = c;
	}
	
	@SuppressWarnings("unused")
	private Player c;
	
	public int slotSelected = 0;
	
	public long something;
	
	public long buyTotal;
	public long sellTotal;
	
	public int buyPrice = 1;
	public int buyAmount = 1;
	public int buyItem = 0;
	public int buyDefaultValue = 0;
	
	public int sellPrice = 1;
	public int sellAmount = 1;
	public int sellItem = 0;
	public int sellDefaultValue = 0;
	
	
	public int[] geSlots = new int[6];
}
