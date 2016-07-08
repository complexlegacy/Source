package core;

/**
 * Important server configurations.
 * @author MGT Madness, created on 20-12-2013.
 */
public class Configuration 
{

	/**
	 * True, when developing.
	 */
	public static boolean DEBUG = false;
	
	/**
	 * True, for stability testing.
	 */
	public static boolean stabilityTest = false;
	
	/**
	 * True, for the packets sent messages to show in-game.
	 */
	public static boolean SERVER_PACKETS = false;
	
	/**
	 * True, to show debug messages in-game related to combat.
	 */
	public static boolean COMBAT_DEBUG = false;
	
	/**
	 * Maximum amount of players allowed to be online at once.
	 */
	public static int MAX_PLAYERS = 200;
	
}
