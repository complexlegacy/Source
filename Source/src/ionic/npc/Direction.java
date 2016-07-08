package ionic.npc;

public enum Direction {
	
	SOUTH(0),
	SOUTH_WEST(1),
	WEST(2),
	NORTH_WEST(3),
	NORTH(4),
	NORTH_EAST(5),
	EAST(6),
	SOUTH_EAST(7);
	
	public int rotationId;
	private Direction(int type) {
		this.rotationId = type;
	}
	
	public static Direction getByName(String s) {
		for (Direction d : Direction.values()) {
			if (d.name().equals(s)) {
				return d;
			}
		}
		return SOUTH;
	}

}
