package ionic.player.content.minigames;

import utility.Misc;
import ionic.item.ItemAssistant;
import ionic.npc.NPC;
import ionic.npc.NPCHandler;
import ionic.player.Player;

import java.util.HashMap;

import core.Constants;


public class PestControl {
	/**
	 * /** how long before were put into the game from lobby
	 */
	private static final int WAIT_TIMER = 60;
	/**
	 * How many players we need to start a game
	 */
	private final static int PLAYERS_REQUIRED = 3;
	/**
	 * Hashmap for the players in lobby
	 */
	public static HashMap<Player, Integer> waitingBoat = new HashMap<Player, Integer>();
	private static HashMap<Player, Integer> gamePlayers = new HashMap<Player, Integer>();

	private static int gameTimer = -1;
	private static int waitTimer = 60;
	public static boolean gameStarted = false;
	public static int KNIGHTS_HEALTH = -1;

	/**
	 * Array used for storing the portals health
	 */
	public static int[] portalHealth = { 200, 200, 200, 200 };
	public static int[] portals = { 6142, 6143, 6144, 6145 };
	/**
	 * array used for storing the npcs used in the minigame
	 *
	 * @order npcId, xSpawn, ySpawn, health
	 */
	public int shifter() {
		return 3732 + Misc.random(9);
	}
	public int brawler() {
		return 3772 + Misc.random(4);
	}
	public int defiler() {
		return 3762 + Misc.random(9);
	}
	public int ravager() {
		return 3742 + Misc.random(4);
	}
	public int torcher() {
		return 3752 + Misc.random(7);
	}

	public int randomNpc() {
		int chance = Misc.random(14);
		if (chance >= 0 && chance <= 3) { return shifter(); }
		if (chance >= 4 && chance <= 7) { return defiler(); }
		if (chance >= 8 && chance <= 10) { return torcher(); }
		if (chance == 11 && chance == 12) { return brawler(); }
		return ravager();
	}

	public int npcsSpawned = 0;
	public static NPC knight = null;

	private int[][] pcNPCData = { { 6142, 2628, 2591 }, // portal
			{ 6143, 2680, 2588 }, // portal
			{ 6144, 2669, 2570 }, // portal
			{ 6145, 2645, 2569 }, // portal
	};
	
	public static void updatePortalHealth(NPC n) {
		for (int i = 0; i < portals.length; i++) {
			if (portals[i] == n.npcType) {
				portalHealth[i] = n.HP;
			}
		}
	}


	public void process() {
		try {
			setBoatInterface();
			if (waitTimer > 0)
				waitTimer -= 3;
			else if (waitTimer <= 0)
				startGame();
			if (KNIGHTS_HEALTH == 0)
				endGame(false, true);
			if (gameStarted && playersInGame() < 1) {
				endGame(false, false);
			}
			if (Misc.random(1) == 0 && gameStarted) {
				tryPortalSummon();
			}
			if (gameTimer > 0 && gameStarted) {
				gameTimer -= 3;
				setGameInterface();
				if (allPortalsDead() || allPortalsDead3())
					endGame(true, false);
			} else if (gameTimer <= 0 && gameStarted)
				endGame(false, false);
		} catch (RuntimeException e) {
			System.out.println("Failed to set process");
			e.printStackTrace();
		}
	}


	public void tryPortalSummon() {
		int[] portals = getPortals();
		npcsSpawned++;
		if (npcsSpawned >= 40) {
			return;
		}
		int p = portals[Misc.random(portals.length - 1)];
		if (p != -1) {
			NPC g = NPCHandler.npcs[p];
			if (g != null) {
				if (Misc.random(4) != 0) {
					int x = g.absX + 2 + Misc.random(2);
					int y = g.absY + 1 + Misc.random(2);
					int npcType = randomNpc();
					NPCHandler.pestControlNpc(npcType, x, y, 0, 2, 50, 14, 100, 25, true);
				} else {
					if (knight != null) {
						g = knight;
						int x = g.absX + 2 + Misc.random(2);
						int y = g.absY + 1 + Misc.random(2);
						int npcType = randomNpc();
						NPCHandler.pestControlNpc(npcType, x, y, 0, 2, 50, 14, 100, 25, true);
					}
				}
			}
		}
	}



	/**
	 * Method we use for removing a player from the pc game
	 *
	 * @param player
	 *            The Player.
	 */
	public static void removePlayerGame(Player player) {
		if (gamePlayers.containsKey(player)) {
			player.getPA().movePlayer(2657, 2639, 0);
			gamePlayers.remove(player);
		}
	}

	/**
	 * Setting the interfaces for the waiting lobby
	 */
	public void setBoatInterface() {
		try {
			for (Player c : waitingBoat.keySet()) {
				if (c != null) {
					try {
						if (gameStarted) {
							c.getPA().sendFrame126("Next Departure: "+(waitTimer + gameTimer)/60+" minutes", 21120);
						} else {
							c.getPA().sendFrame126("Next Departure: " + waitTimer + "", 21120);
						}
						c.getPA().sendFrame126("Players Ready: "+playersInBoat()+"", 21121);
						c.getPA().sendFrame126("(Need "+PLAYERS_REQUIRED+" to 25 players)", 21122);
						c.getPA().sendFrame126("Points: " + c.pcPoints + "",21123);
						switch (waitTimer) {
						case 60:
							c.sendMessage("Next game will start in: 60 seconds.");
							break;
						case 30:
							c.sendMessage("Next game will start in: 30 seconds.");
							break;
						case 0:
							if (playersInBoat() < PLAYERS_REQUIRED) {
								c.sendMessage("Not enough players are ready to play.");
								return;
							}
						}
					} catch (RuntimeException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (RuntimeException e) {
			System.out.println("Failed to set interfaces");
			e.printStackTrace();
		}
	}

	/**
	 * Setting the interface for in game players
	 */
	private void setGameInterface() {
		for (Player player : gamePlayers.keySet()) {
			if (player != null) {
				for (int i = 0; i < portalHealth.length; i++) {
					player.getPA().sendFrame126("" + portalHealth[i] + "", 21111 + i);
				}
				player.getPA().sendFrame126("" + KNIGHTS_HEALTH, 21115);
				player.getPA().sendFrame126("" + player.pcDamage, 21116);
				if (gameTimer > 60) {
					player.getPA().sendFrame126("Time remaining: " + (gameTimer / 60) + " minutes", 21117);
				} else {
					player.getPA().sendFrame126("Time remaining: " + gameTimer + " seconds", 21117);
				}
			}
		}
	}

	public static void setGameInterface2() {
		for (Player player : gamePlayers.keySet()) {
			if (player != null) {
				for (int i = 0; i < portalHealth.length; i++) {
					player.getPA().sendFrame126(" ", 21111 + i);
				}
				player.getPA().sendFrame126(" ", 21115);
				player.getPA().sendFrame126(" ", 21116);
				player.getPA().sendFrame126(" ", 21117);
			}
			player.getPA().sendFrame126("@or2@Pest Control Points: " + player.pcPoints + "", 16028);
		}
	}

	/***
	 * Moving players to arena if there's enough players
	 */
	public void startGame() {
		if (playersInBoat() < PLAYERS_REQUIRED) {
			waitTimer = WAIT_TIMER;
			return;
		}
		for (int i = 0; i < portalHealth.length; i++)
			portalHealth[i] = 200;
		gameTimer = 400;
		KNIGHTS_HEALTH = 250;
		waitTimer = -1;
		spawnNPC();
		gameStarted = true;
		for (Player player : waitingBoat.keySet()) {
			int team = waitingBoat.get(player);
			if (player == null) {
				continue;
			}
			if (!player.inPcBoat() && waitingBoat.containsKey(player)) {
				waitingBoat.remove(player);
			}
			player.getPA().movePlayer(2656 + Misc.random(3), 2614 - Misc.random(4), 0);
			player.getPA().delayWalkableInterfaces();
			player.sendMessage("@red@The Pest Control Game has begun!");
			gamePlayers.put(player, team);
		}

		waitingBoat.clear();
	}

	/**
	 * Checks how many players are in the waiting lobby
	 *
	 * @return players waiting
	 */
	private int playersInBoat() {
		int players = 0;
		for (Player player : waitingBoat.keySet()) {
			if (player != null) {
				players++;
			}
			if (player == null) {
				players--;
			}
		}
		return players;
	}

	/**
	 * Checks how many players are in the game
	 *
	 * @return players in the game
	 */
	private int playersInGame() {
		int players = 0;
		for (Player player : gamePlayers.keySet()) {
			if (player != null) {
				players++;
			}
			if (player == null) {
				players--;
			}
		}
		return players;
	}

	/**
	 * Ends the game
	 *
	 * @param won
	 *            Did you win?
	 */
	private void endGame(boolean won, boolean knightDied) {
		for (Player player : gamePlayers.keySet()) {
			if (player == null) {
				continue;
			}
			player.getPA().movePlayer(2657, 2639, 0);
			player.getPA().resetWalkableInterfaces();
			if (knightDied) {
				player.sendMessage("You failed to protect the void knight from the pests and have not been awarded any points.");
			} else {
			if (won && player.pcDamage > 10) {
				int POINT_REWARD;
				if (player.x2Points == true) {
					POINT_REWARD = 16;
				} else {
					POINT_REWARD = 8;
				}
					player.sendMessage("You have won the pest control game and have been awarded " + POINT_REWARD + " Pest Control points.");
					player.pcPoints += POINT_REWARD;
					ItemAssistant.addItem(player, 995, Misc.random(player.combatLevel * 1000));
				} else if (won) {
					player.sendMessage("The void knights notice your lack of zeal. You gain no points for this round.");
				} else {
					player.sendMessage("You failed to kill all the portals in 3 minutes and have not been awarded any points.");
				}
			}
		}
		setGameInterface2();
		cleanUpPlayer();
		cleanUp();
	}

	/**
	 * Resets the game variables and map
	 */
	private void cleanUp() {
		gameTimer = -1;
		KNIGHTS_HEALTH = -1;
		waitTimer = WAIT_TIMER;
		gameStarted = false;
		gamePlayers.clear();
		npcsSpawned = 0;
		for (int j = 0; j < NPCHandler.npcs.length; j++) {
			if (NPCHandler.npcs[j] != null) {
				if (NPCHandler.npcs[j].isPestControlNPC) {
					NPCHandler.npcs[j] = null;
				}
			}
		}
	}

	/**
	 * Cleans the player of any damage, loss they may of received
	 */
	private void cleanUpPlayer() {
		for (Player player : gamePlayers.keySet()) {
			for (int i = 0; i < 24; i++) {
				if (i != 3) {
					player.skillLevel[i] = player.getPA().getLevelForXP(player.playerXP[i]);
					player.getPA().refreshSkill(i);
				} else {
					player.skillLevel[i] = player.calculateMaxLifePoints();
					player.getPA().refreshSkill(i);
				}
			}
			player.specAmount = 10;
			player.getPA().refreshSkill(5);
			player.pcDamage = 0;
			ItemAssistant.addSpecialBar(player, player.playerEquipment[Constants.WEAPON_SLOT]);
		}
	}

	/**
	 * Checks if the portals are dead
	 *
	 * @return players dead
	 */
	private static boolean allPortalsDead() {
		int count = 0;
		for (int aPortalHealth : portalHealth) {
			if (aPortalHealth <= 0)
				count++;
		}
		return count >= 4;
	}

	public boolean allPortalsDead3() {
		int count = 0;
		for (int j = 0; j < NPCHandler.npcs.length; j++) {
			if (NPCHandler.npcs[j] != null) {
				if (NPCHandler.npcs[j].npcType > 6141
						&& NPCHandler.npcs[j].npcType < 6146)
					if (NPCHandler.npcs[j].needRespawn)
						count++;
			}
		}
		return count >= 4;
	}

	public int[] getPortals() {
		int[] portals = {-1, -1, -1, -1};
		int pc = 0;
		for (int j = 0; j < NPCHandler.npcs.length; j++) {
			if (NPCHandler.npcs[j] != null) {
				if (NPCHandler.npcs[j].npcType >= 6142 && NPCHandler.npcs[j].npcType <= 6145) {
					if (!NPCHandler.npcs[j].needRespawn) {
						portals[pc] = j;
						pc++;
					}
				}
			}
		}
		return portals;
	}

	/**
	 * Moves a player out of the waiting boat
	 *
	 * @param c
	 *            Client c
	 */
	 public static void leaveWaitingBoat(Player c) {
		if (waitingBoat.containsKey(c)) {
			waitingBoat.remove(c);
			c.getPA().removeAllWindows();
			c.getPA().sendFrame126(" ", 21120);
			c.getPA().sendFrame126(" ", 21121);
			c.getPA().sendFrame126(" ", 21122);
			c.getPA().sendFrame126(" ", 21123);
			c.getPA().movePlayer(2657, 2639, 0);
		}
	 }

	 /**
	  * Moves a player into the hash and into the lobby
	  *
	  * @param player
	  *            The player
	  */
	 public static void addToWaitRoom(Player player) {
		 if (player != null) {
			 waitingBoat.put(player, 1);
			 player.sendMessage("You have joined the Pest Control boat.");
			 player.getPA().movePlayer(2661, 2639, 0);
			 player.getPA().delayWalkableInterfaces();
		 }
	 }

	 /**
	  * Checks if a player is in the game
	  *
	  * @param player
	  *            The player
	  * @return return
	  */
	 public static boolean isInGame(Player player) {
		 return gamePlayers.containsKey(player);
	 }

	 /**
	  * Checks if a player is in the pc boat (lobby)
	  *
	  * @param player
	  *            The player
	  * @return return
	  */
	 public static boolean isInPcBoat(Player player) {
		 return waitingBoat.containsKey(player);
	 }

	 public static boolean npcIsPCMonster(int npcType) {
		 return npcType >= 3727 && npcType <= 3776;
	 }

	 private void spawnNPC() {
		 for (int[] aPcNPCData : pcNPCData) {
			 NPCHandler.pestControlNpc(aPcNPCData[0], aPcNPCData[1], aPcNPCData[2], 0, 0, 200, 0, 0, playersInGame() * 100, false);
		 }
		 NPCHandler.pestControlNpc(3782, 2656, 2592, 0, 0, 250, 0, 0, 0, false);
	 }
	 
	 
	 
	public static void ladders(Player c, int obX, int obY) {
		if (obX == 2669 && obY == 2601) {
			if (c.onTurret) {
				c.onTurret = false;
				c.getPA().movePlayer(2668, 2601, 0);
			} else {
				c.onTurret = true;
				c.getPA().movePlayer(2670, 2601, 0);
			}
		} else if (obX == 2666 && obY == 2586) {
			if (c.onTurret) {
				c.onTurret = false;
				c.getPA().movePlayer(2666, 2587, 0);
			} else {
				c.onTurret = true;
				c.getPA().movePlayer(2666, 2585, 0);
			}
		} else if (obX == 2647 && obY == 2586) {
			if (c.onTurret) {
				c.onTurret = false;
				c.getPA().movePlayer(2647, 2587, 0);
			} else {
				c.onTurret = true;
				c.getPA().movePlayer(2647, 2585, 0);
			}
		} else if (obX == 2644 && obY == 2601) {
			if (c.onTurret) {
				c.onTurret = false;
				c.getPA().movePlayer(2645, 2601, 0);
			} else {
				c.onTurret = true;
				c.getPA().movePlayer(2643, 2601, 0);
			}
		}
	}


}
