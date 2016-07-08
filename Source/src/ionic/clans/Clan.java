package ionic.clans;

import ionic.player.Player;

/**
 * @author Keith
 */
public class Clan {
	
	public int clanId;//the clan id (also the file name)
	public String owner = "";//name of the clan owner
	public String prefix = "";//clan name/prefix
	public String[] ranks = new String[100];//names of ranked players
	public int[] ranksN = new int[100];//player's rank #s
	public int whoCanEnter = 0;//what ranks can enter
	public int whoCanTalk = 0;//what ranks can talk
	public int whoCanKick = 0;//what ranks can kick
	public Player[] members = new Player[100];//players in the clan chat
	public String[] kicked = new String[100];//names of kicked players
	public int[] kickTicks = new int[100];//amount in minutes left for kicked players to join clan
	//millz
	public Clan(int id, String owner, String prefix, String[] ranks, int[] ranksN, int enter, int talk, int kick) {
		this.clanId = id;
		this.owner = owner;
		this.prefix = prefix;
		this.ranks = ranks;
		this.ranksN = ranksN;
		this.whoCanEnter = enter;
		this.whoCanKick = kick;
		this.whoCanTalk = talk;
	}
	
}