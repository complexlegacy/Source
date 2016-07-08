package core;

import ionic.clans.ClanHandler;
import ionic.grandExchange.GrandExchangeHandler;
import ionic.item.ItemData;
import ionic.item.ItemHandler;
import ionic.npc.NPCData;
import ionic.npc.NPCHandler;
import ionic.npc.NPCSize;
import ionic.npc.drops.DropLoader;
import ionic.object.clip.ObjectDef;
import ionic.object.clip.Region;
import ionic.object.customobject.ObjectHandler;
import ionic.object.customobject.ObjectManager;
import ionic.player.PlayerHandler;
import ionic.player.achievements.AchievementLoader;
import ionic.player.content.minigames.FightCaves;
import ionic.player.content.minigames.PestControl;
import ionic.player.content.partyroom.PartyRoom;
import ionic.player.content.quest.QuestHandler;
import ionic.player.content.skills.summoning.Summoning;
import ionic.player.content.skills.summoning.SummoningData;
import ionic.player.content.skills.thieving.StallThieving;
import ionic.player.event.CycleEventHandler;
import ionic.player.object.Doors;
import ionic.player.object.DoubleDoors;
import ionic.player.profiles.Profile;
import ionic.player.profiles.ProfileLoader;
import ionic.player.tracking.MainTickLog;
import ionic.shop.ShopController;
import ionic.shop.ShopLoader;

import java.io.*;
import java.net.InetSocketAddress;

import network.connection.Connection;
import network.connection.ConnectionHandler;
import network.connection.ConnectionThrottleFilter;

import org.apache.mina.common.IoAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;

import utility.Logger;
import utility.SimpleTimer;
import core.maintick.Task;
import core.maintick.TaskScheduler;


public class Server {
	
	public static Profile[] profiles = new Profile[1000];

	private static IoAcceptor acceptor;
	private static ConnectionHandler connectionHandler;
	private static ConnectionThrottleFilter throttleFilter;
	public static ItemHandler itemHandler = new ItemHandler();
	public static PlayerHandler playerHandler = new PlayerHandler();
	public static NPCHandler npcHandler = new NPCHandler();
	public static ObjectHandler objectHandler = new ObjectHandler();
	public static ObjectManager objectManager = new ObjectManager();
	public static PestControl pestControl = new PestControl();
	public static final TaskScheduler scheduler = new TaskScheduler();
	public static ShopController shopcontroller = new ShopController();
	public static ClanHandler clanHandler = new ClanHandler();
	public static Processors processors = new Processors();
	public static PartyRoom partyroom = new PartyRoom();
	public static StallThieving stallThieving = new StallThieving();
	public static FightCaves fightCaves = new FightCaves();
	public static Summoning summoning = new Summoning();

	/**
	 * True, if a server update countdown timer is active.
	 */
	 public static boolean UpdateServer = false;
	/**
	 * Game time variable
	 */
	public static int gameTime = 0;

	 /**
	  * Launch the server.
	  */
	 public static void main(java.lang.String args[]) throws NullPointerException, IOException {
		 System.out.println("Server loading...");
		 new SimpleTimer();
		 System.setOut(new Logger(System.out));
		 System.setErr(new Logger(System.err));
		 loadSystems();
		 initiateConnections();
		 processors.start();
		 mainTick();
		 System.out.println("Server finished loading. players can now connect");
	 }

	 /**
	  * Load all systems.
	  */
	 private static long k = 0;
	 private static void loadSystems() throws IOException {
		 k = System.currentTimeMillis();
		 Doors.getSingleton().load();
		 progresslog("Loaded doors in");
		 DoubleDoors.getSingleton().load();
		 progresslog("Loaded double doors in");
		 Connection.initialize();
		 progresslog("Loaded connections in");
		 ObjectDef.loadConfig();
		 progresslog("Loaded objectdef in");
		 Region.load();
		 progresslog("Loaded region in");
		 NPCSize.init();
		 progresslog("Loaded npc sizes in");
		 ClanHandler.loadClans();
		 clanHandler.processPunishments();
		 progresslog("Loaded clan chats in");
		 NPCData.load();
		 progresslog("Loaded NPC Data in");
		 new DropLoader();
		 npcHandler.loadSpawns();
		 progresslog("Loaded npc drops & spawns in");
		 new AchievementLoader();
		 progresslog("Loaded achievement data from files in");
		 ItemData.loadData();
		 itemHandler.load();
		 progresslog("Loaded ItemData in");
		 ShopLoader.loadAll();
		 ShopController.setShopOwners();
		 shopcontroller.restockShops();
		 progresslog("Loaded shops in");
		 SummoningData.setDrainRates();
		 ProfileLoader.load();
		 progresslog("Loaded player profiles in");
		 GrandExchangeHandler.loadGrandExchangeOffers();
		 progresslog("Loaded Grand Exchange offers in");
		 QuestHandler.loadQuestDialogues();
		 progresslog("Loaded Quest dialogues in");
	 }
	 
	 private static void progresslog(String s) {
		 s += " "+(System.currentTimeMillis() - k)+" Milliseconds";
		 System.out.println(s);
		 k = System.currentTimeMillis();
	 }

	 /**
	  * Initiate all the connections.
	  */
	 private static void initiateConnections()
	 {
		 acceptor = new SocketAcceptor();
		 connectionHandler = new ConnectionHandler();
		 SocketAcceptorConfig sac = new SocketAcceptorConfig();
		 sac.getSessionConfig().setTcpNoDelay(false);
		 sac.setReuseAddress(true);
		 sac.setBacklog(100);
		 throttleFilter = new ConnectionThrottleFilter(Configuration.stabilityTest ? 0 : Constants.CONNECTION_DELAY);
		 sac.getFilterChain().addFirst("throttleFilter", throttleFilter);
		 try 
		 {
			 acceptor.bind(new InetSocketAddress(43594), connectionHandler, sac);
		 } 
		 catch (IOException e) 
		 {
			 e.printStackTrace();
		 }
	 }

	 /**
	  * The main game tick.
	  */
	 public static void mainTick()
	 {
		 scheduler.schedule(new Task()
		 {@
			 Override
			 protected void execute()
		 {
			 MainTickLog.loopDebugPart1();
			 CycleEventHandler.getSingleton().process();
			 itemHandler.process();
			 playerHandler.process();
			 npcHandler.process();
			 objectManager.process();
			 MainTickLog.loopDebugPart2();
		 }
		 });
	 }

	public static void setMinutesCounter(int minutesCounter) {
		Server.gameTime = minutesCounter;
		try {
			File file = new File("./data/minutes.dat");
			DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
			out.writeInt(getMinutesCounter());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadMinutesCounter() {
		try {
			File file = new File("./data/minutes.dat");
			if (file.exists()) {
				DataInputStream in = new DataInputStream(new FileInputStream(file));
				gameTime = in.readInt();
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int getMinutesCounter() {
		return gameTime;
	}

}