package ionic.player.content.combat.dwarfcannon;

import java.util.HashMap;

public enum CannonRotation {
	
	NORTH(1, 516, 1, 2),
	NORTH_EAST(2, 517, 2, 2),
	EAST(3, 518, 2, 1),
	SOUTH_EAST(4, 519, 2, 0),
	SOUTH(5, 520, 1, 0),
	SOUTH_WEST(6, 521, 0, 0),
	WEST(7, 514, 0, 1),
	NORTH_WEST(8, 515, 0, 2);
	
	private final int index, rotationAnim, projectileXOffset, projectileYOffset;
	
	private CannonRotation(int slot, int anim, int pxo, int pyo) {
		this.index = slot;
		this.rotationAnim = anim;
		this.projectileXOffset = pxo;
		this.projectileYOffset = pyo;
	}

	
	private static HashMap<Integer, CannonRotation> rotations = new HashMap<Integer, CannonRotation>();
	static {
		for (CannonRotation c : CannonRotation.values()) {
			rotations.put(c.index, c);
		}
	}
	
	
	public static CannonRotation getRotation(int i) {
		return rotations.get(i);
	}
	public int getIndex() {
		return index;
	}
	public int getRotationAnim() {
		return rotationAnim;
	}
	public int getProjectileXOffset() {
		return projectileXOffset;
	}
	public int getProjectileYOffset() {
		return projectileYOffset;
	}
	
}
