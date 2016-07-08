package ionic.player.content.combat.dwarfcannon;


public class Cannon {
	
	private CannonState state = null;
	private final int index;
	private final int xPosition;
	private final int yPosition;
	private final int zPosition;
	private int balls;
	private CannonRotation rotation = null;
	
	public Cannon(int slot, int x, int y, int z) {
		this.index = slot;
		this.xPosition = x;
		this.yPosition = y;
		this.zPosition = z;
		this.rotation = CannonRotation.NORTH;
	}
	
	
	
	public CannonState getState() {
		return state;
	}
	public int getX() {
		return xPosition;
	}
	public int getY() {
		return yPosition;
	}
	public int getZ() {
		return zPosition;
	}
	public int getIndex() {
		return index;
	}
	public int getBalls() {
		return balls;
	}
	public CannonRotation getRotation() {
		return rotation;
	}
	
	
	
	public void setState(CannonState state) {
		this.state = state;
	}
	public void setBalls(int balls) {
		this.balls = balls;
	}
	public void setRotation(CannonRotation rotation) {
		this.rotation = rotation;
	}
	
}
