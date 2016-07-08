package ionic.player.content.combat.dwarfcannon;

import core.Constants;
import utility.Misc;
import ionic.item.ItemAssistant;
import ionic.npc.NPC;
import ionic.npc.NPCHandler;
import ionic.object.clip.Region;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import ionic.player.movement.Movement;

/**
 * @author Keith
 */
public class CannonHandler {


	/**
	 * All the cannons on the server
	 */
	public static Cannon[] cannons = new Cannon[200];
	/**
	 * The maximum amount of balls that the cannon can hold
	 */
	private static final int MAX_BALLS = 50;
	/** 
	 * The maximum the cannon can hit 
	 */
	private static final int MAXHIT = 30;
	/**
	 * Maximum npc distance
	 */
	private static final int MAX_DISTANCE = 10;



	/**
	 * When clicking a cannon base it calls this method to set up the cannon
	 * @param c : The player who is setting up the cannon
	 */
	public static void setUpCannon(Player c) {
		if (cantSetUp(c)) {
			return;
		}
		c.disableStuff = true;
		int slot = getCannonSlot();
		final Cannon cannon = c.cannon = cannons[slot] = new Cannon(slot, c.absX, c.absY, c.heightLevel);
		cannon.setState(CannonState.SETTING_UP_BASE);
		Movement.stopMovement(c);
		c.turnPlayerTo(cannon.getX() + 1, cannon.getY());
		processSetUp(c, cannon);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!c.disableStuff) {
					container.stop();
					return;
				}
				if (ItemAssistant.playerHasItem(c, cannon.getState().getItem())) {
					processSetUp(c, cannon);
				} else {
					container.stop();
				}
			}
			@Override
			public void stop() {
				c.disableStuff = false;
			}
		}, 4);
	}




	/**
	 * Handles every tick of setting up the cannon
	 * @param c : The player who is setting up the cannon
	 * @param cannon : The cannon object of the player
	 */
	private static void processSetUp(Player c, Cannon cannon) {
		ItemAssistant.deleteItem(c, cannon.getState().getItem(), 1);
		c.startAnimation(827);
		placeObject(cannon.getState().getObject(), cannon.getX(), cannon.getY());
		cannon.setState(cannon.getState().getNextState());
	}





	/**
	 * Checks if the player is not able to set up a cannon
	 * @param c : The player who it is checking
	 */
	private static boolean cantSetUp(Player c) {
		if (c.cannon != null) {
			c.sendMessage("You already have a cannon.");
			return true;
		}
		if (notAllowed(c.getX(), c.getY())) {
			c.sendMessage("You're not allowed to have a cannon here.");
			return true;
		}
		if (!ItemAssistant.playerHasItem(c, 6) || !ItemAssistant.playerHasItem(c, 8)
				|| !ItemAssistant.playerHasItem(c, 10) || !ItemAssistant.playerHasItem(c, 12)) {
			c.sendMessage("You don't have all 4 parts to the cannon.");
			return true;
		}
		if (!Region.canMove(c.absX, c.absY, c.absX + 2, c.absY + 2, c.heightLevel, 1, 1)) {
			c.sendMessage("You are too close to other objects.");
			return true;
		}
		if (nearCannon(c.absX, c.absY, c.heightLevel)) {
			c.sendMessage("You are too close to another players cannon.");
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * Checks if the player is inside coordinates that the cannon can't be in
	 * @param x : The 'X' coordinate
	 * @param y : The 'Y' coordinate
	 */
	private static boolean notAllowed(int x, int y) {
		
		return false;
	}




	/**
	 * Checks coordinates to see if the coordinates are too close to an existing cannon
	 * @param x : The 'X' coordinate
	 * @param y : The 'Y' coordinate
	 * @param z : The height
	 * @return
	 */
	private static boolean nearCannon(int x, int y, int z) {
		for (Cannon c : cannons) {
			if (c != null) {
				if (x >= c.getX() - 2 && x <= c.getX() + 2 && y >= c.getY() - 2 && y <= c.getY() + 2 && c.getZ() == z) {
					return true;
				}
			}
		}
		return false;
	}




	/**
	 * Gets an available slot for a cannon object
	 */
	private static int getCannonSlot() {
		for (int i = 0; i < cannons.length; i++) {
			if (cannons[i] == null) {
				return i;
			}
		}
		return -1;
	}





	/**
	 * Displays an object for all players
	 * @param id : The object to create
	 * @param x : The 'X' coordinate to create it at
	 * @param y : The 'Y' coordinate to create it at
	 */
	private static void placeObject(int id, int x, int y) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				c.getPA().checkObjectSpawn(id, x, y, 0, 10);
			}
		}
	}





	/**
	 * When a player clicks the first action on a cannon object
	 * @param c : The player who clicked
	 * @param obX : The object's 'X' coordinate
	 * @param obY : The object's 'Y' coordinate
	 */
	public static void firstClickCannon(Player c, int obX, int obY) {
		Cannon cannon = getCannonByCoords(obX, obY, c.heightLevel);
		if (cannon == null)
			return;
		if (c.cannon != cannon) {
			c.sendMessage("This cannon doesn't belong to you.");
			return;
		}
		if (c.cannon != null && !c.cannon.getState().fullPickup()) {
			addCannonItems(c, cannon.getState().getPickup());
		} else if (c.cannon != null && c.cannon.getState().fullPickup()) {
			if (c.cannon.getState() == CannonState.EMPTY_CANNON) {
				c.sendMessage("Your cannon has no ammo.");
				return;
			} else if (c.cannon.getState() == CannonState.LOADED_CANNON) {
				c.cannon.setState(CannonState.FIRING_CANNON);
				rotate(c, c.cannon);
			} else if (c.cannon.getState() == CannonState.FIRING_CANNON) {
				if (c.cannon.getBalls() <= 0) {
					cannon.setState(CannonState.EMPTY_CANNON);
				} else {
					cannon.setState(CannonState.LOADED_CANNON);
				}
			}
		}
	}




	/**
	 * When a player clicks the second action on a cannon object
	 * @param c : The player who clicked
	 * @param obX : The object's 'X' coordinate
	 * @param obY : The object's 'Y' coordinate
	 */
	public static void secondClickCannon(Player c, int obX, int obY) {
		Cannon cannon = getCannonByCoords(obX, obY, c.heightLevel);
		if (cannon == null)
			return;
		if (c.cannon != cannon) {
			c.sendMessage("This cannon doesn't belong to you.");
			return;
		}
		if (c.cannon != null && cannon.getState().fullPickup()) {
			addCannonItems(c, cannon.getState().getPickup());
		}
	}




	/**
	 * Finds a cannon from the object coordinates
	 * @param x : The 'X' coordinate to search
	 * @param y : The 'Y' coordinate to search
	 * @param z : The height to search
	 */
	private static Cannon getCannonByCoords(int x, int y, int z) {
		for (Cannon c : cannons) {
			if (c.getX() == x && c.getY() == y && c.getZ() == z) {
				return c;
			}
		}
		return null;
	}




	/**
	 * When a player picks up his cannon, this gives him the items
	 * @param c : The player picking up the items
	 * @param items : An Integer array for the items
	 */
	private static void addCannonItems(Player c, int[] items) {
		if (c.cannon.getBalls() > 0) {
			ItemAssistant.addItem(c, 2, c.cannon.getBalls());
		}
		placeObject(-1, c.cannon.getX(), c.cannon.getY());
		c.cannon = cannons[c.cannon.getIndex()] = null;
		c.disableStuff = false;
		for (int i : items) {
			ItemAssistant.addItem(c, i, 1);
		}
	}




	/**
	 * When a player uses cannon balls on a cannon
	 * @param c : The player who is trying to load the cannon
	 * @param obX : The cannon object 'X' coordinate
	 * @param obY : The cannon object 'Y' coordinate
	 */
	public static void loadCannon(Player c, int obX, int obY) {
		Cannon cannon = getCannonByCoords(obX, obY, c.heightLevel);
		if (cannon == null)
			return;
		if (c.cannon != cannon) {
			c.sendMessage("This cannon doesn't belong to you.");
			return;
		}
		if (c.cannon != null) {
			if (c.cannon.getBalls() >= MAX_BALLS) {
				c.sendMessage("Your cannon is already carrying the maximum amount of ammo.");
				return;
			}
			if (ItemAssistant.playerHasItem(c, 2)) {
				int amount = ItemAssistant.getItemAmount(c, 2);
				if (amount > MAX_BALLS)
					amount = MAX_BALLS;
				ItemAssistant.deleteItem1(c, 2, amount);
				if (c.cannon.getBalls() > 0)
					c.cannon.setBalls(c.cannon.getBalls() + amount);
				else
					c.cannon.setBalls(amount);
				c.sendMessage("Your cannon now has "+c.cannon.getBalls()+" cannonballs.");
				if (c.cannon.getState() == CannonState.EMPTY_CANNON) {
					c.cannon.setState(CannonState.LOADED_CANNON);
				}
			}
		}
	}




	/**
	 * Rotates a cannon
	 * @param c : The cannon to rotate
	 */
	public static void rotate(Player c, Cannon cannon) {
		CycleEventHandler.getSingleton().addEvent(cannon, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (cannon == null || !cannon.getState().equals(CannonState.FIRING_CANNON)) {
					container.stop();
					return;
				}
				cannon.setRotation(CannonRotation.getRotation(cannon.getRotation().getIndex() + 1));
				if (cannon.getRotation() == null)
					cannon.setRotation(CannonRotation.NORTH);
				objectAnim(cannon.getX(), cannon.getY(), cannon.getRotation().getRotationAnim(), 10, -1);
				shootNpcs(c, cannon);
			}
			@Override
			public void stop() {
			}
		}, 3);
	}




	/**
	 * Does an object animation for every player within 25 tiles.
	 * @param x : The 'X' coordinate for the anim
	 * @param y : The 'Y' coordinate for the anim
	 * @param animationID : The object animation ID
	 */
	private static void objectAnim(int x, int y, int animationID, int tileObjectType, int orientation) {
		for (Player p : PlayerHandler.players) {
			if(p != null) {
				Client players = (Client)p;
				if(players.distanceToPoint(x, y) <= 25) {
					players.getPA().createPlayersObjectAnim(x, y, animationID, tileObjectType, orientation);	
				}
			}
		}
	}
	


	/**
	 * Searches for an npc, then shoots a cannon ball at it
	 * @param c : The owner of the cannon
	 * @param cannon : The cannon
	 */
	private static void shootNpcs(Player c, Cannon cannon) {
		int damage = Misc.random(MAXHIT);
		NPC target = targetNpc(cannon);
		if (target != null) {
			if (damage > target.HP) {
				damage = target.HP;
			}
			if (c.inMulti() && target.inMulti()) {
				cannonProjectile(c, cannon, target);
				c.getCombat().appendHit(target, damage, 0, 4, 2);
				target.killerId = c.playerId;
				target.facePlayer(c.playerId);
				target.hitUpdateRequired2 = true;
				target.updateRequired = true;
				cannon.setBalls(cannon.getBalls() - 1);
				c.getPA().addSkillXP(damage * 20, Constants.RANGED);
			}
			if (!c.inMulti()) {
				if (target.underAttackBy > 0 && target.underAttackBy != c.playerId) {
					return;
				}
				if (c.underAttackBy2 > 0 && c.underAttackBy2 != target.npcId) {
					return;
				}
				cannonProjectile(c, cannon, target);
				c.getCombat().appendHit(target, damage, 0, 4, 2);
				target.killerId = c.playerId;
				target.facePlayer(c.playerId);
				target.hitUpdateRequired2 = true;
				target.updateRequired = true;
				cannon.setBalls(cannon.getBalls() - 1);
				c.getPA().addSkillXP(damage * 20, Constants.RANGED);
			}
			if (cannon.getBalls() <= 0) {
				cannon.setState(CannonState.EMPTY_CANNON);
				c.sendMessage("Your cannon has ran out of ammo.");
			}
		}
	}



	/**
	 * Finds an npc in the range of the cannon
	 * @param cannon : The cannon
	 */
	private static NPC targetNpc(Cannon cannon) {
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] == null) {
				continue;
			}
			NPC npc = NPCHandler.npcs[i];
			int myX = cannon.getX();
			int myY = cannon.getY();
			int theirX = npc.absX;
			int theirY = npc.absY;
			if (!npc.isDead && npc.HP > 0 && inDistance(myX, myY, theirX, theirY)) {
				switch (cannon.getRotation().getIndex()) {
				case 1:
					if (theirY > myY && theirX >= myX - 1 && theirX <= myX + 1)
						return npc;
					break;
				case 2:
					if (theirX >= myX + 1 && theirY >= myY + 1)
						return npc;
					break;
				case 3:
					if (theirX > myX && theirY >= myY - 1 && theirY <= myY + 1)
						return npc;
					break;
				case 4:
					if (theirY <= myY - 1 && theirX >= myX + 1)
						return npc;
					break;
				case 5:
					if (theirY < myY && theirX >= myX - 1 && theirX <= myX + 1)
						return npc;
					break;
				case 6:
					if (theirX <= myX - 1 && theirY <= myY - 1)
						return npc;
					break;
				case 7:
					if (theirX < myX && theirY >= myY - 1 && theirY <= myY + 1)
						return npc;
					break;
				case 8:
					if (theirX <= myX - 1 && theirY >= myY + 1)
						return npc;
					break;
				}
			}
		}
		return null;
	}


	
	/**
	 * Checks if an npc is within firing range
	 * @param myX : cannon 'X' coordinate
	 * @param myY : cannon 'Y' coordinate
	 * @param npcX : npc 'X' coordinate
	 * @param npcY : npc 'Y' coordinate
	 */
	private static boolean inDistance(int myX, int myY, int npcX, int npcY) {
		return npcX >= myX - MAX_DISTANCE && npcX <= myX + MAX_DISTANCE && npcY >= myY - MAX_DISTANCE && npcY <= myY + MAX_DISTANCE;
	}


	
	/**
	 * Creates a cannonball projectile
	 * @param c : The player who owns the cannon
	 * @param cannon : The cannon
	 * @param n : The NPC to fire at
	 */
	private static void cannonProjectile(Player c, Cannon cannon, NPC n) {
		int oX = cannon.getX() + cannon.getRotation().getProjectileXOffset();
		int oY = cannon.getY() + cannon.getRotation().getProjectileYOffset();
		int offX = ((oX - n.absX) * -1);
		int offY = ((oY - n.absY) * -1);
		c.getPA().createPlayersProjectile(oX, oY, offY, offX, 50, 60, 53, 20, 20, -c.oldNpcIndex + 1, 30);
	}
	
	/**
	 * When a player enters a region or logs in, it spawns the cannons that are in that region
	 * @param c : The player to spawn the objects for
	 */
	public static void loadCannonsInRegion(Player c) {
		for (Cannon cannon : cannons) {
			if (cannon != null && c.distanceToPoint(cannon.getX(), cannon.getY()) <= 60) {
				c.getPA().checkObjectSpawn(cannon.getState().getObject(), cannon.getX(), cannon.getY(), 0, 10);
			}
		}
	}
	
	

}
