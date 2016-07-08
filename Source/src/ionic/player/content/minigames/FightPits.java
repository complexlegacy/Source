package ionic.player.content.minigames;

import core.Constants;
import utility.Misc;
import ionic.item.ItemAssistant;
import ionic.player.Player;
import ionic.player.PlayerHandler;

/**
 * @author Keith
 */

public class FightPits {
	
	public static int timeLeft = 60;
	public static boolean gameOn = false;
	public static String lastWinner = "Nobody";
	public static final int MINIMUM_PLAYERS = 2;
	public static int totalPlayers = 0;
	
	public static void process() {
		if (gameOn) {
			checkEnd();
		}
		if (timeLeft > 0 && !gameOn) {
			timeLeft -= 3;
			updateLobby();
		} else if (timeLeft <= 0 && !gameOn){
			timeLeft = 0;
			checkStart();
			updateLobby();
		}
	}
	
	public static void checkStart() {
		timeLeft = 60;
		int count = countWaitingPlayers();
		if (count < MINIMUM_PLAYERS)
			return;
		start();
	}
	
	public static void checkEnd() {
		int count = countPlayingPlayers();
		if (count > 1) {
			return;
		}
		end();
	}
	
	public static void end() {
		Player winner = null;
		gameOn = false;
		timeLeft = 60;
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			Player c = PlayerHandler.players[i];
			if (c != null) {
				if (inArena(c)) {
					winner = c;
				}
			}
		}
		if (winner != null) {
			lastWinner = winner.playerName;
			winner.sendMessage("Congratulations you have won the games.");
			winner.getPA().movePlayer(2399, 5174, 0);
			winner.pitWins ++;
			winner.specAmount = 10.0;
			winner.skillLevel[3] = winner.calculateMaxLifePoints();
			winner.getPA().refreshSkill(3);
			ItemAssistant.addSpecialBar(winner, winner.playerEquipment[Constants.WEAPON_SLOT]);
			reward(winner);
			totalPlayers = 0;
			winner.getPA().updatePlayerTab();
		} else {
			lastWinner = "Nobody";
			totalPlayers = 0;
		}
		updateLobby();
	}
	
	public static void reward(Player winner) {
		int amount = Misc.random(1000 * totalPlayers);
		ItemAssistant.addItem(winner, 6529, amount);
	}
	
	public static void updateLobby() {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			Player c = PlayerHandler.players[i];
			if (c != null) {
				if (inWaitingRoom(c)) {
					updateWaitInterface(c);
				}
			}
		}
	}
	
	public static void start() {
		gameOn = true;
		totalPlayers = countWaitingPlayers();
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			Player c = PlayerHandler.players[i];
			if (c != null) {
				if (inWaitingRoom(c)) {
					c.getPA().movePlayer(2392 + Misc.random(12), 5139 + Misc.random(25), 0);
				}
			}
		}
	}
	
	public static int countWaitingPlayers() {
		int count = 0;
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			Player c = PlayerHandler.players[i];
			if (c != null) {
				if (inWaitingRoom(c)) {
					count ++;
				}
			}
		}
		return count;
	}
	public static int countPlayingPlayers() {
		int count = 0;
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			Player c = PlayerHandler.players[i];
			if (c != null) {
				if (inArena(c)) {
					count ++;
				}
			}
		}
		return count;
	}
	
	public static void updateWaitInterface(Player c) {
		if (!gameOn)
			c.getPA().sendFrame126("Time till next game : "+timeLeft, 27515);
		else
			c.getPA().sendFrame126("Game onGoing...", 27515);
		c.getPA().sendFrame126("Last champion : "+lastWinner, 27516);
		c.getPA().sendFrame126("My total wins : "+c.pitWins, 27517);
	}
	
	public static boolean inWaitingRoom(Player c) {
		return (c.absX >= 2394 && c.absY >= 5169 && c.absX <= 2404 && c.absY <= 5175);
	}
	public static boolean inArena(Player c) {
		return (c.absX >= 2371 && c.absY >= 5125 && c.absX <= 2428 && c.absY <= 5168);
	}

}
