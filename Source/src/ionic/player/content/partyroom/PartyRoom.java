package ionic.player.content.partyroom;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;

import java.math.BigInteger;
import java.util.Timer;
import java.util.TimerTask;

import utility.Misc;
import core.Configuration;
import core.Server;

public class PartyRoom {
	
	public static int[] itemsWaiting = new int[100];
	public static int[] amountsWaiting = new int[100];
	public static int[] itemsDropping = new int[100];
	public static int[] amountsDropping = new int[100];
	public static boolean timer = false;
	public static int timeSeconds = 0;
	public static int timeMinutes = 0;
	public static boolean dropping = false;
	public static int[] balloonObjects = {115, 116, 117, 118, 119, 120, 121, 122};
	public static Balloon[] balloons;
	
	public static void loadBalloons(Player c) {
		if (balloons != null) {
			for (int i = 0; i < balloons.length; i++) {
				if (balloons[i] != null) {
					c.getPA().checkObjectSpawn(balloons[i].obj, balloons[i].getX(), balloons[i].getY(), Misc.random(4), 10);
				}
			}
		}
	}
	
	public static BigInteger getValue() {
		BigInteger value = new BigInteger("0");
		for (int i = 0; i < itemsWaiting.length; i++) {
			if (itemsWaiting[i] > 0) {
				BigInteger f = new BigInteger(""+ItemAssistant.getItemShopValue(itemsWaiting[i])+"");
				BigInteger a = new BigInteger(""+amountsWaiting[i]+"");
				f = f.multiply(a);
				value = value.add(f);
			}
		}
		return value;
	}
	
	
	public static void confirm(Player c) {
		for (int i = 0; i < 8; i++) {
			int slot = getSlot();
			if (slot == -1) {
				c.getIA().refresh();
				refreshWaiting();
				c.sendMessage("The drop party chest can only hold 100 items, not all items were added");
				return;
			} else {
				ItemAdder a = c.getIA();
				itemsWaiting[slot] = a.items[i];
				amountsWaiting[slot] = a.amounts[i];
				c.getIA().items[i] = -1;
				c.getIA().amounts[i] = -1;
			}
		}
		c.getIA().refresh();
		refreshWaiting();
	}
	
	public static int getSlot() {
		for (int i = 0; i < 100; i++) {
			if (itemsWaiting[i] <= 0) {
				return i;
			}
		}
		return -1;
	}
	
	public static void refreshWaiting() {
		for (int i = 0; i < Configuration.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Player c = PlayerHandler.players[i];
				refreshWaiting(c);
			}
		}
	}
	public static void refreshWaiting(Player c) {
		String s = "";
		for (int i = 0; i < 100; i++) {
			int k = itemsWaiting[i] + 1;
			int z = amountsWaiting[i];
			if (k <= 1) {
				k = -1;
				z = 1;
			}
			s += "# "+i+" "+k+" "+z+" ";
		}
		c.getPA().sendFrame126(s, 27411);
	}
	
	public static void refreshDropping() {
		for (int i = 0; i < Configuration.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Player c = PlayerHandler.players[i];
				refreshDropping(c);
			}
		}
	}
	public static void refreshDropping(Player c) {
		String s = "";
		int count = 0;
		for (int i = 0; i < 100; i++) {
			s += "# "+i+" -1 1 ";
		}
		c.getPA().sendFrame126(s, 27421);
		s = "";
		for (int i = 0; i < 100; i++) {
			int k = itemsDropping[i] + 1;
			int z = amountsDropping[i];
			if (k >= 1 && z != 0) {
				s += "# "+count+" "+k+" "+z+" ";
				count++;
			}
		}
		c.getPA().sendFrame126(s, 27421);
	}
	
	public static int countWaiting() {
		int count = 0;
		for (int i = 0; i < 100; i++) {
			if (itemsWaiting[i] > 0) {
				count ++;
			}
		}
		return count;
	}
	public static int countDropping() {
		int count = 0;
		for (int i = 0; i < 100; i++) {
			if (itemsDropping[i] > 0) {
				count ++;
			}
		}
		return count;
	}
	
	public static String getConvertedValue() {
		int t = -1;
		int m = -1;
		int b = -1;
		BigInteger g = getValue();
		BigInteger f = g;
		String s = "";
		f = f.divide(new BigInteger("1000"));
		s = ""+f+"";
		try {
			t = Integer.parseInt(s);
		} catch (Exception e) {
			t = -1;
		}
		f = g;
		f = f.divide(new BigInteger("1000000"));
		s = ""+f+"";
		try {
			m = Integer.parseInt(s);
		} catch (Exception e) {
			m = -1;
		}
		f = g;
		f = f.divide(new BigInteger("1000000000"));
		s = ""+f+"";
		try {
			b = Integer.parseInt(s);
		} catch (Exception e) {
			b = -1;
		}
		String e = "";
		if (t != -1 && t < 10000) {
			e = ""+t+"K";
		} else if (m != -1 && t >= 10000 && m < 10000) {
			e = ""+m+"M";
		} else {
			e = ""+b+"B";
		}
		return e;
	}
	
	public static void pullLever(Player c) {
		if (timer == true) {
			c.sendMessage("You must wait for the current drop to finish before starting a new one.");
			return;
		}
		int count = countWaiting();
		if (count > 0) {
			Server.partyroom.countDown();
			globalMsg("<col=255>The party lever has been pulled - Estimated Value: </col><col=ff0000>"+getConvertedValue());
		} else {
			c.sendMessage("There are no items currently waiting to be dropped.");
		}
	}

	
	public static void startDrop() {
		int count = countDropping();
		balloons = new Balloon[count * 5];
		for (int i = 0; i < balloons.length; i++) {
			balloons[i] = null;
		}
		for (int i = 0; i < balloons.length; i+= 5) {
			int amount = amountsDropping[i/5];
			if (amount > 1) {
				for (int j = 1; j < 5; j++) {
					if (amount > 0) {
						int amount2 = 1 + Misc.random(amount - 1);
						balloons[i+j] = new Balloon(itemsDropping[i/5], amount2, i/5, false);
						amount -= amount2;
					}
				}
			}
			balloons[i] = new Balloon(itemsDropping[i/5], amount, i/5, false);
		}
		for (int i = 0; i < balloons.length; i++) {
			if (balloons[i] == null) {
				balloons[i] = new Balloon(0, 0, i, true);
			}
		}
		
		dropping = true;
		
		CycleEventHandler.getSingleton().addEvent(Server.partyroom, new CycleEvent() {
    		@Override
    		public void execute(CycleEventContainer e) {
    			Balloon b = null;
    			for (int i = 0; i < 10; i++) {
	    			if (b == null) { b = randomBalloon(); } else { break; }
    			}
    			if (b != null) {
    				int[] spot = getLocation();
    				if (spot[0] != 0 && spot[1] != 0) {
    					if (!b.isDropped()) {
    						b.drop(spot[0], spot[1]);
    					}
    				}
    			}
    			if (countDropping() == 0 || dropping == false) {
    				e.stop();
    			}
    		}
			@Override
			public void stop() {
				dropping = false;
				for (int i = 0; i < balloons.length; i++) {
					if (balloons[i] != null && balloons[i].isDropped()) {
						makeBalloon(-1, balloons[i].getX(), balloons[i].getY());
					}
					balloons[i] = null;
				}
				balloons = null;
				for (int i = 0; i < 100; i++) {
					itemsDropping[i] = 0;
					amountsDropping[i] = 0;
				}
				refreshDropping();
				globalMsg("The drop party is now over.");
			}
		}, 1);
	}
	
	public static Balloon randomBalloon() {
		Balloon b = null;
		b = balloons[Misc.random(balloons.length - 1)];
		for (int i = 0; i < 20; i++) {
			if (b != null) {
				if (b.isDropped()) {
					b = balloons[Misc.random(balloons.length - 1)];
				} else {
					return b;
				}
			}
		}
		return b;
	}
	
	public static int[] getLocation() {
		int x = getX();
		int y = getY();
		
		if ((x == 3051 && y == 3375)
			|| (x == 3051 && y == 3381)
			|| (x == 3040 && y == 3381)
			|| (x == 3040 && y == 3375)
			|| (y == 3378 && x >= 3042 && x <= 3049)) {
			return new int[] {0,0};
		}
		
		for (int i = 0; i < balloons.length; i++) {
			if (balloons[i] != null) {
				if (balloons[i].getX() == x && balloons[i].getY() == y) {
					return new int[] {0,0};
				}
			}
		}
		return new int[] {x,y};
	}
	
	public static int getX() {
		return 3039 + Misc.random(14);
	}
	public static int getY() {
		return 3383 - Misc.random(10);
	}
	
	
	public static void globalMsg(String s) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player a =  PlayerHandler.players[j];
				a.sendMessage(s);
			}
		}
	}
	
	
	private static Timer GAME_TIMER = null;
	public void countDown() {
		GAME_TIMER = new Timer();
		timer = true;
		if (!dropping) {
			timeMinutes = 2;
			timeSeconds = 30;
		} else {
			timeMinutes = 5;
			timeSeconds = 30;
		}
		for (int i = 0; i < Configuration.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				PlayerHandler.players[i].getPA().sendFrame126(""+timeMinutes+":"+timeSeconds+"", 27403);
			}
		}
		GAME_TIMER.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				tickTimer();
			}
		}, 0, 1000);
	}
	
	public static void tickTimer() {
		if (timeSeconds > 0) {
			timeSeconds -= 1;
		} else {
			timeMinutes --;
			timeSeconds = 59;
		}
		String secs = ""+timeSeconds;
		if (secs.length() == 1) {secs = "0"+timeSeconds;}
		for (int i = 0; i < Configuration.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				PlayerHandler.players[i].getPA().sendFrame126(""+timeMinutes+":"+secs+"", 27403);
			}
		}
		if (timeSeconds <= 20 && timeMinutes == 0) {
			dropping = false;
		}
		if (timeSeconds == 30 && timeMinutes == 0) {
			globalMsg("<col=255>A drop party with a value of </col><col=ff0000>"+getConvertedValue()+"</col><col=255> will commence in 30 seconds.");
		}
		if (timeSeconds == 0 && timeMinutes == 0) {
			stopTimer();
			return;
		}
	}
	
	public static void stopTimer() {
		GAME_TIMER.cancel();
		timer = false;
		for (int i = 0; i < 100; i++) {
			itemsDropping[i] = itemsWaiting[i];
			amountsDropping[i] = amountsWaiting[i];
			itemsWaiting[i] = -1;
			amountsWaiting[i] = -1;
		}
		refreshWaiting();
		refreshDropping();
		startDrop();
	}
	
	
	
	
	public static void makeBalloon(int balloon, int x, int y) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player a =  PlayerHandler.players[j];
				a.getPA().checkObjectSpawn(balloon, x, y, Misc.random(3), 10);
			}
		}
	}
	
	public static void pop(Player c, int x, int y) {
		Balloon popped = null;
		int index = -1;
		if (balloons == null) { return; }
		for (int i = 0; i < balloons.length; i++) {
			if (balloons[i] != null) {
				if (balloons[i].getX() == x && balloons[i].getY() == y) {
					popped = balloons[i];
					index = i;
				}
			}
		}
		c.startAnimation(794);
		if (popped != null) {
			makeBalloon(-1, popped.getX(), popped.getY());
			if (!popped.isEmpty() && popped.getItem() > 0) {
				Server.itemHandler.createGroundItem((Client)c, popped.getItem(), popped.getX(), popped.getY(), popped.getAmount(), c.playerId, false);
				amountsDropping[popped.getSlot()] -= popped.getAmount();
				if (amountsDropping[popped.getSlot()] <= 0) {
					itemsDropping[popped.getSlot()] = 0;
				}
				refreshDropping();
			}
			balloons[index] = null;
		} else {
			makeBalloon(-1, x, y);
		}
	}
	

}
