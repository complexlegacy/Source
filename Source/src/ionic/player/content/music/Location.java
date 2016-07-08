package ionic.player.content.music;

/**
 * Simple file that contains map coordinates.
 *
 * @author relex lawl
 */
public final class Location {

	public Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	private final int x;
	
	private final int y;
	
	private final int z;
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
}
