package ionic.player.content.skills.summoning;

import core.Constants;
import ionic.item.ItemAssistant;
import ionic.npc.NPC;
import ionic.npc.NPCHandler;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.content.skills.summoning.SummoningData.pouchData;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;

public class Summoning {

	public static final int SUMMON_GFX = 1315;

	public static void summon(Player c, pouchData pouch) {
		if (c.familiar != null) {
			c.sendMessage("You already have a familiar.");
			return;
		}
		if (c.getLevelForXP(c.playerXP[Constants.SUMMONING]) < pouch.levelReq) {
			c.sendMessage("You need a summoning level of "+pouch.levelReq+" to summon this familiar");
			return;
		}
		if (c.petSummoned == true) {
			c.sendMessage("You can't have a pet and a familiar at the same time.");
			return;
		}
		int drain = (pouch.levelReq/10) + 1;
		if (c.skillLevel[Constants.SUMMONING] <= drain) {
			c.sendMessage("You don't have enough summoning points to summon this familiar.");
			return;
		}
		c.skillLevel[Constants.SUMMONING] -= drain;
		ItemAssistant.deleteItem(c, pouch.pouch, 1);
		c.familiarTime = pouch.time * 60;
		updateTimer(c);
		if (c.inWilderness()) {
			summonFamiliar(c, pouch.npcId + 1, c.absX -1, c.absY-2, c.heightLevel, pouch, pouch.hitPoints);
		} else {
			summonFamiliar(c, pouch.npcId, c.absX -1, c.absY-2, c.heightLevel, pouch, pouch.hitPoints);
		}
		c.getPA().addSkillXP(pouch.summonXp * 10, Constants.SUMMONING);
		c.familiar.gfx0(SUMMON_GFX);
		c.getPA().sendFrame75(pouch.npcId, 18021);
		c.getPA().sendFrame126(pouch.getName(), 18028);
		c.famType = pouch;
		c.getPA().sendFrame126(SummoningData.scrollByPouch(pouch).getName(), 39510);
		updateLevel(c);
		c.summonDrain = 0.0;
	}
	
	public static void handleDrain(Player c) {
		if (c.familiar == null) 
			return;
		c.summonDrain += c.famType.drainRate;
		if (c.summonDrain >= 4) {
			c.summonDrain -= 4;
			c.skillLevel[Constants.SUMMONING] -= 1;
			updateLevel(c);
			c.getPA().refreshSkill(24);
			if (c.skillLevel[Constants.SUMMONING] == 0) {
				c.sendMessage("You have run out of summoning points");
				autoDismiss(c);
			}
		}
	}
	
	
	public static void updateLevel(Player c) {
		c.getPA().sendFrame126(""+c.skillLevel[Constants.SUMMONING]+"/"+c.getPA().getLevelForXP(c.playerXP[Constants.SUMMONING]), 18045);
	}
	
	
	/**
	 * When logging in, summoning the familiar
	 */
	public static void loginSummon(Player c, pouchData familiar) {
		updateLevel(c);
		updateTimer(c);
		if (familiar == null) {
			c.getPA().sendFrame75(4000, 18021);
			c.getPA().sendFrame126("None", 18028);
			c.getPA().sendFrame126("Special move", 39510);
			return;
		}
		summonFamiliar(c, familiar.npcId, c.absX -1, c.absY-1, c.heightLevel, familiar, familiar.hitPoints);
		c.familiar.gfx0(SUMMON_GFX);
		c.getPA().sendFrame75(familiar.npcId, 18021);
		c.getPA().sendFrame126(familiar.getName(), 18028);
		c.getPA().sendFrame126(SummoningData.scrollByPouch(familiar).getName(), 39510);
	}
	
	
	
	/**
	 * When a player calls his familiar.
	 */
	public static void call(Player c, boolean automatic) {
		if (c.familiar == null) {
			c.sendMessage("You don't have a familiar.");
			return;
		}
		if (System.currentTimeMillis() - c.lastCall < 5000 && !automatic) {
			c.sendMessage("You can only call your familiar once every 5 seconds");
			return;
		}
		familiarTele(c, true);
		if (!automatic) {
		c.sendMessage("You call your familiar to your side.");
		}
		c.lastCall = System.currentTimeMillis();
	}
	
	
	/**
	 * Familiar teleporting
	 */
	public static int teleTicks = 0;
	public static void familiarTele(Player c, boolean call) {
		if (c.familiar == null) {
			return;
		}
		if (c.summonEvent) {
			return;
		}
		final int hp = c.familiar.HP;
		c.summonEvent = true;
		c.familiar.gfx0(SUMMON_GFX);
		NPC n = NPCHandler.npcs[c.familiar.npcId];
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent(){
			@Override
                public void execute(CycleEventContainer container) {
				teleTicks++;
					if (teleTicks == 2 && call == true) {
						n.disappearTime = 6;
						n.npcTeleport(50,50,50);
					}
					if (teleTicks == 3 && call == true) {
						container.stop();
					}
					if (teleTicks == 3 && call == false) {
						n.disappearTime = 6;
						n.npcTeleport(50,50,50);
					}
					if (teleTicks == 4 && call == false) {
						container.stop();
					}
                }
                @Override
                public void stop() {
                	teleTicks = 0;
                	c.summonEvent = false;
                	if (call == true && c.familiar != null) {
                		if (c.inWilderness()) {
                			summonFamiliar(c, c.famType.npcId + 1, c.absX -1, c.absY-1, c.heightLevel, c.famType, hp);
                		} else {
                			summonFamiliar(c, c.famType.npcId, c.absX -1, c.absY-1, c.heightLevel, c.famType, hp);
                		}
                		c.familiar.gfx0(SUMMON_GFX);
                	}
            }
        }, 1);
	}
	
	
	
	/**
	 * When a player dismisses his familiar.
	 */
	public static void dismiss(Player c) {
		if (c.familiar == null) {
			c.sendMessage("You don't have a familiar.");
			return;
		}
		if (c.familiar.HP > 0) {
			c.sendMessage("You dismiss your familiar");
		}
		familiarTele(c, false);
		c.familiarTime = 0;
		updateTimer(c);
		c.getPA().sendFrame75(4000, 18021);
		c.getPA().sendFrame126("None", 18028);
		c.getPA().sendFrame126("Special move", 39510);
		c.famType = null;
		c.familiar = null;
	}
	
	/**
	 * When the players timer runs out or he runs out of summoning points
	 */
	public static void autoDismiss(Player c) {
		if (c.familiar == null) {
			return;
		}
		familiarTele(c, false);
		c.getPA().sendFrame75(4000, 18021);
		c.getPA().sendFrame126("None", 18028);
		c.famType = null;
		c.getPA().sendFrame126("Special move", 39510);
		c.summonDrain = 0.0;
		c.familiarTime = 0;
		c.familiar = null;
	}
	
	
	/**
	 * When a player clicks to renew his familiar
	 */
	public static void renew(Player c) {
		if (c.familiar == null) {
			c.sendMessage("You don't have a familiar.");
			return;
		}
		if (!ItemAssistant.playerHasItem(c, c.famType.pouch)) {
			c.sendMessage("You need a pouch from the same type of familiar to renew it.");
			return;
		}
		if (c.familiarTime > 120) {
			c.sendMessage("There must be less than 2 minutes left on your familiar to renew it.");
			return;
		}
		int drain = (c.famType.levelReq/10) + 1;
		if (c.skillLevel[Constants.SUMMONING] <= drain) {
			c.sendMessage("You don't have enough summoning points to summon this familiar.");
			return;
		}
		call(c, false);
		c.skillLevel[Constants.SUMMONING] -= drain;
		c.familiarTime = c.famType.time * 60;
		c.familiar.HP = c.famType.hitPoints;
		updateTimer(c);
		ItemAssistant.deleteItem1(c, c.famType.pouch, 1);
		updateLevel(c);
	}
	
	
	/**
	 * Timer for familiars
	 */
	public static void checkTimer(Player c) {
		if (c.familiarTime <= 0) {
			c.sendMessage("Your familiar has run out of time and has been dismissed.");
			autoDismiss(c);
			c.familiarTime = 0;
		}
		updateTimer(c);
	}
	
	public static void updateTimer(Player c) {
		int minutes = c.familiarTime / 60;
		int seconds = c.familiarTime - (minutes * 60);
		String s = ""+seconds;
		if (s.length() == 1) {
			s += "0";
		}
		
		c.getPA().sendFrame126(""+minutes+":"+s+"", 18043);
	}
	
	
	
	/**
	 * Handles messaging the player when his familiar will run out of time
	 */
	public static void handleTimeMessages(Player c) {
		if (c.familiarTime == 60) {
			c.sendMessage("Your familiar has 1 minute left");
		}
	}
	
	
	/**
	 * The timer on the familiar runs for all players.
	 */
	public void timer() {
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
			@Override
                public void execute(CycleEventContainer container) {
					for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
							Client c = (Client) PlayerHandler.players[j];
							if (c.familiar != null) {
								c.familiarTime -= 15;
								handleTimeMessages(c);
								checkTimer(c);
							}
						}
					}
				}
				@Override
                public void stop() {
				}
			}, 25);
		}
	
	
	/**
	 * Clicking button to auto withdraw items for you
	 */
	public static void autoWithdraw(Player c) {
		if (c.familiar == null) {
			return;
		}
		if (!isBob(c)) {
			return;
		}
		if (ItemAssistant.freeSlots(c) == 0) {
			c.sendMessage("You need some free slots.");
		}
		if (System.currentTimeMillis() - c.lastWithdraw < 500) {
			return;
		}
		if (getItemsInBob(c) <= 0) {
			c.sendMessage("There are no item's in your familiar's inventory.");
			return;
		}
		c.lastWithdraw = System.currentTimeMillis();
		for (int i = 0; i < getMaxAmount(c); i++) {
			if (ItemAssistant.freeSlots(c) > 0) {
				ItemAssistant.addItem(c, c.bobItems[i], c.bobItemsN[i]);
				c.bobItems[i] = 0;
				c.bobItemsN[i] = 0;
				updateBob(c, i);
			} else {
				break;
			}
		}
		c.sendMessage("You take items out of your familiar");
	}
	
	
	/**
	 * Opening the interface for beast of burden
	 */
	public static void openBob(Player c) {
		if (c.familiar == null) {
			c.sendMessage("You don't have a familiar.");
			return;
		}
		if (!isBob(c)) {
			c.sendMessage("Your familiar is not a beast of burden, so it does not hold items");
			return;
		}
		updateBob(c);
		c.getPA().showInterface(39500);
	}
	
	/**
	 * updates the bob inventory
	 */
	public static void updateBob(Player c) {
		for (int i = 0; i < c.bobItems.length; i++) {
			updateBob(c, i);
		}
	}
	
	/**
	 * Updates a certain slot of the bob's inventory
	 */
	public static void updateBob(Player c, int slot) {
		if (c.bobItems[slot] > 0) {
			c.getPA().sendFrame34a(39502, c.bobItems[slot], slot, c.bobItemsN[slot]);
		} else {
			c.getPA().sendFrame34a(39502, -1, slot, 1);
		}
	}
	
	
	
	/**
	 * If the familiar is a Beast of burden
	 */
	public static boolean isBob(Player c) {
		if (c.famType == null) { return false; }
		if (c.famType.npcId == 6873) { return true; }
		if (c.famType.npcId == 6806) { return true; }
		if (c.famType.npcId == 6815) { return true; }
		if (c.famType.npcId == 6867) { return true; }
		if (c.famType.npcId == 6794) { return true; }
		if (c.famType.npcId == 6994) { return true; }
		return false;
	}
	public static boolean isBob(int npcid) {
		if (npcid == 6873) { return true; }
		if (npcid == 6806) { return true; }
		if (npcid == 6815) { return true; }
		if (npcid == 6867) { return true; }
		if (npcid == 6794) { return true; }
		if (npcid == 6994) { return true; }
		return false;
	}
	
	
	/**
	 * Storing items in a beast of burden
	 */
	public static void addToBob(Player c, int item, int amount) {
		if (c.familiar == null) {
			return;
		}
		if (getItemsInBob(c) >= getMaxAmount(c)) {
			c.sendMessage("Your familiar can't hold any more items!");
			return;
		}
		if (ItemAssistant.getItemAmount(c, item) == 0) {
			return;
		}
		if (!ItemAssistant.playerHasItem(c, item, amount)) {
			amount = ItemAssistant.getItemAmount(c, item);
		}
		if (!ItemAssistant.isStackable(item)) {
			for (int j = 0; j < amount; j++) {
			for (int i = 0; i < c.bobItems.length; i++)	{
				if (c.bobItems[i] <= 0) {
					c.bobItems[i] = item;
					c.bobItemsN[i] = 1;
					updateBob(c, i);
					ItemAssistant.deleteItem1(c, item, 1);
				break;
					}
				}
			}
		}
		
		if (ItemAssistant.isStackable(item)) {
			int exists = -1;
			for (int i = 0; i < c.bobItems.length; i++) {
				if (c.bobItems[i] == item) {
					exists = i;
					break;
				}
			}
			if (exists >= 0) {
				c.bobItemsN[exists] += amount;
				updateBob(c, exists);
				ItemAssistant.deleteItem1(c, item, amount);
			}
			if (exists == -1) {
				for (int i = 0; i < c.bobItems.length; i++)	{
					if (c.bobItems[i] <= 0) {
						c.bobItems[i] = item;
						c.bobItemsN[i] = amount;
						updateBob(c, i);
						ItemAssistant.deleteItem1(c, item, amount);
					break;
					}
				}
			}
		}
	}
	
	
	/**
	 * Withdrawing items from a beast of burden.
	 */
	public static void takeFromBob(Player c, int item, int amount) {
		if (System.currentTimeMillis() - c.lastWithdraw < 500) {
			return;
		}
		c.lastWithdraw = System.currentTimeMillis();
		if (ItemAssistant.freeSlots(c) > 0) {
			int slot = getBobSlot(c, item);
			if (amount > c.bobItemsN[slot]) {
				amount = c.bobItemsN[slot];
			}
			
			c.bobItemsN[slot] = c.bobItemsN[slot] - amount;
			if (c.bobItemsN[slot] <= 0) {
				c.bobItems[slot] = 0;
			}
			updateBob(c, slot);
			ItemAssistant.addItem(c, item, amount);
		} else {
			c.sendMessage("You need atleast 1 free inventory slot, to be able to withdraw items.");
		}
	}
	
	/**
	 * get's the slot of an item currently in the bob inventory
	 */
	public static int getBobSlot(Player c, int item) {
		for (int i = 0; i < c.bobItems.length; i++) {
			if (c.bobItems[i] == item) {
				return i;
			}
		}
		return 0;
	}
	
	
	/**
	 * Gets the amount of items that can be stored in your Bob
	 */
	public static int getMaxAmount(Player c) {
		if (c.famType == null) { return 0; }
		if (c.famType.npcId == 6873) { return 30; }//pack yak
		if (c.famType.npcId == 6806) { return 3; }//thorny snail
		if (c.famType.npcId == 6815) { return 18; }//war tortoise
		if (c.famType.npcId == 6867) { return 9; }//bull ant
		if (c.famType.npcId == 6794) { return 12; }//terrorbird
		if (c.famType.npcId == 6994) { return 6; }//spirit kalphite
		return 0;
	}
	/**
	 * Finds the amount of items that are inside your Bob.
	 */
	public static int getItemsInBob(Player c) {
		int amount = 0;
		for (int i = 0; i < c.bobItems.length; i++) {
			if (c.bobItems[i] > 0) {
				amount += 1;
			}
		}
		return amount;
	}
	
	
	public static void summonFamiliar(Player player, int npcType, int x, int y, int heightLevel, pouchData p, int hp)
    {
            int slot = -1;
            for (int i = 1; i < NPCHandler.maxNPCs; i++)
            {
                    if (NPCHandler.npcs[i] == null)
                    {
                            slot = i;
                            break;
                    }
            }
            if (slot == -1)
            {
                    return;
            }
            player.familiar = new NPC(slot, npcType);
            player.familiar.absX = x;
            player.familiar.absY = y;
            player.familiar.makeX = x;
            player.familiar.makeY = y;
            player.familiar.heightLevel = heightLevel;
            player.familiar.walkingType = 0;
            player.familiar.HP = hp;
            player.familiar.MaxHP = p.hitPoints;
            player.familiar.maxHit = (p.attack / 10) + 1;
            player.familiar.attack = p.attack;
            player.familiar.defence = p.defence;
            player.familiar.spawnedBy = player.getId();
            player.familiar.underAttack = true;
            player.familiar.facePlayer(player.playerId);
            player.familiar.summoned = true;
            player.familiar.summonedBy = player.playerId;
            player.familiar.owner = player;
            player.familiar.isFamiliar = true;
            NPCHandler.npcs[slot] = player.familiar;
    }
	

}
