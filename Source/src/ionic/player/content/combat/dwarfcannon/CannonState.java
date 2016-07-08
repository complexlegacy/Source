package ionic.player.content.combat.dwarfcannon;

import java.util.HashMap;

public enum CannonState {
	
	SETTING_UP_BASE(6, 7, null),
	SETTING_UP_STAND(8, 8, new int[] {6}),
	SETTING_UP_BARRELS(10, 9, new int[] {6, 8}),
	SETTING_UP_FURNACE(12, 6, new int[] {6, 8, 10}),
	EMPTY_CANNON(),
	LOADED_CANNON(),
	FIRING_CANNON(),
	;
	
	private int itemReq, objectCreation;
	private int[] pickupItems;
	private CannonState(int itemRequired, int object, int[] pickup) {
		this.itemReq = itemRequired;
		this.objectCreation = object;
		this.pickupItems = pickup;
	}
	private CannonState() {
		this.pickupItems = new int[] {6, 8, 10, 12};
		this.objectCreation = 6;
	}
	
	public static HashMap<Integer, CannonState> setUpStages = new HashMap<Integer, CannonState>();
	
	
	public int getItem() {
		return this.itemReq;
	}
	public int getObject() {
		return this.objectCreation;
	}
	public int[] getPickup() {
		return this.pickupItems;
	}
	
	
	public CannonState getNextState() {
		if (this == SETTING_UP_BASE) return SETTING_UP_STAND;
		if (this == SETTING_UP_STAND) return SETTING_UP_BARRELS;
		if (this == SETTING_UP_BARRELS) return SETTING_UP_FURNACE;
		return EMPTY_CANNON;
	}
	
	public boolean fullPickup() {
		if (this == EMPTY_CANNON || this == LOADED_CANNON || this == FIRING_CANNON)
			return true;
		return false;
	}
	
}
