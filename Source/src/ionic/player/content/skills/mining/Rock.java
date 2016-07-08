package ionic.player.content.skills.mining;

public class Rock {
	
	public int objectId;
	public int ticks;
	public int x;
	public int y;
	public int life;
	public int rep;
	
	public Rock(int o, int x, int y, int t, int l, int r) {
		this.objectId = o;
		this.ticks = t;
		this.x = x;
		this.y = y;
		this.life = l;
		this.rep = r;
	}

}
