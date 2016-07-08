package ionic.player.content.skills.woodcutting;
/**
 * @author Keith
 */
/**
 * An instance for each tree that's being cut, or that has been cut.
 */
public class Tree {

	public int o;
	public int x;
	public int y;
	public int t;
	public int l;
	public int s;
	
	public Tree(int obj, int treeX, int treeY, int life, int stump) {
		this.o = obj;
		this.x = treeX;
		this.y = treeY;
		this.l = life;
		this.s = stump;
	}
	
	
	
	
}
