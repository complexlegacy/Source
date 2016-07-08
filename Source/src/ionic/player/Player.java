package ionic.player;

import core.Server;
import ionic.clans.Clan;
import ionic.clans.ClanHandler;
import ionic.clans.ClanSaver;
import ionic.grandExchange.PersonalData;
import ionic.item.Item;
import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.item.ItemDegrade;
import ionic.npc.NPC;
import ionic.npc.NPCHandler;
import ionic.npc.pet.BossPet;
import ionic.player.banking.Bank;
import ionic.player.content.combat.Combat;
import ionic.player.content.combat.Death;
import ionic.player.content.combat.MultiWay;
import ionic.player.content.combat.Poison;
import ionic.player.content.combat.dwarfcannon.Cannon;
import ionic.player.content.combat.vsplayer.Attack;
import ionic.player.content.gambling.Gamble;
import ionic.player.content.minigames.*;
import ionic.player.content.miscellaneous.MonsterKillLog;
import ionic.player.content.miscellaneous.Preset;
import ionic.player.content.miscellaneous.PriceChecker;
import ionic.player.content.miscellaneous.Trade;
import ionic.player.content.music.PlayList;
import ionic.player.content.partyroom.ItemAdder;
import ionic.player.content.prayer.AncientCursesPrayerBook;
import ionic.player.content.prayer.QuickCurses;
import ionic.player.content.prayer.QuickPrayers;
import ionic.player.content.skills.construction.House;
import ionic.player.content.skills.construction.Room;
import ionic.player.content.skills.farming.*;
import ionic.player.content.skills.herblore.FinishingPotions;
import ionic.player.content.skills.herblore.UnfinishedPotions;
import ionic.player.content.skills.mining.Rock;
import ionic.player.content.skills.summoning.Summoning;
import ionic.player.content.skills.summoning.SummoningData;
import ionic.player.content.skills.woodcutting.Tree;
import ionic.player.dialogue.DialogueList;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import ionic.player.movement.Movement;
import ionic.player.object.ActionHandler;
import ionic.player.packets.PacketHandler;
import ionic.player.profiles.Profile;
import ionic.player.tracking.AccountFlagging;

import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.mina.common.IoSession;

import utility.ISAACRandomGen;
import utility.Misc;
import network.connection.HostList;
import network.packet.Packet;
import network.packet.StaticPacketBuilder;
import network.packet.Stream;
import core.Configuration;
import core.Constants;

/**
 * Everything declared in this class will belong to the individual player.
 */
public abstract class Player {

	public long hunterDelay;
	public boolean playerIsFishing;
	public int fishTimer = 0;
	public int[] fishingProp = new int[24];
	public int[] questProgress = new int[10];
	public Cannon cannon;
	public int[] killLogs = new int[200];
	public int[] petKillLogs = new int[25];
	public int duoPoints = 0;
	public String inputAction = "";
	public WeakReference<Player> duoPartner;
	public WeakReference<Player> potentialPartner;
	public int duoTask = -1, duoTaskAmount = -1;
	public int killStreak = 0;
	public int killStreakRecord = 0;
	public int cwGames = 0, cwKills = 0, cwDeaths = 0;
	public boolean isRepairing = false, isCollapsing = false, isTakingFromStall = false, isAttackingGate = false;
	public boolean x2Points = false;
	public int waveId, tzKekTimer = 0, tzKekSpawn = 0;
	public long lunarDelay = 0, lastCast = 0;
	public int dream;
	public boolean indream = false;
	private House house;
	public Room toReplace, replaceWith;
	public int effigyFirst, effigySecond, triengEffigy;
	public int xpLamp;
	public long lastImpCatch = 0;
	public Tree tree = null;
	public boolean isChopping = false;
	public boolean inFletchInterface = false;
	public boolean herbloreInterface = false;
	public int herbloreMakeAmt = 0;
	public UnfinishedPotions herbResult = null;
	public FinishingPotions herbResult2 = null;
	public boolean isUnfinished = false;
	public boolean isHerblore = false;
	public boolean stopHerblore = false;
	public int fletchInterface = 0;
	public boolean isFletching = false;
	public boolean isCrafting = false;
	public boolean isMining = false;
	public int leatherType = -1;
	public int fletchAmount = 0;
	public int courseStage = 0;
	public long lastPickable = 0;
	public long lastDig = 0;
	public boolean isAgility = false;
	public int agilityLoop = 0;
	public long lastAgility = 0;
	public boolean canBury = true;
	public boolean closeAgility = false;
	public BossPet bosspetSelected = BossPet.CORPOREAL_YOUNGLING;
	public String[] tabNames = {"Tab 1", "Tab 2", "Tab 3", "Tab 4", 
		 "Tab 5", "Tab 6", "Tab 7", "Tab 8"};
	public Bank bankInstance = new Bank(this);
		public Bank getBank() {
			return bankInstance;
		}
		public int[] easyProgress = new int[100];
		public int[] mediumProgress = new int[100];
		public int[] hardProgress = new int[100];
		public int[] eliteProgress = new int[100];
		public boolean gotStarter = false;
		public int[] essence = new int[54];
		public int pouchSize = 10;
		public int shopOpen = -1;
		public int shopOwner = -1;
		public boolean noteShop = false;
		public int shopPriceAmount = 0;
		public BigInteger pouchCoins = new BigInteger("0");
		public boolean[] announced99 = new boolean[25];
		public boolean[] announced100m = new boolean[25];
		public boolean[] announced200m = new boolean[25];
		public int[] barrowsData = {-1, -1, -1, -1, -1, -1};
		public int barrowsCrypt = 0, tunnelX = 0, tunnelY = 0, monstersKilled = 0, lastRocks = 0;
		public boolean lootedChest = false;
		public NPC brotherSpawned = null;
		public boolean overloaded = false;
		public long overloadTime = 0;
		public Rock rock = null;
		public int miningTicks = 0;
		public int chestLoots = 0;
		public boolean wildyIcon = false;
		public int[] checkerItems = new int[30], checkerAmounts = new int[30];
		public int[] gambleItems = new int[12], gambleAmounts = new int[12];
		public Gamble gamble;
		public Preset[] presets = new Preset[10];
		public int presetSelected = 0;
		
		public Profile profile;
		
		public int pitWins;
		
		public int pkPoints = 0;
		
		public DialogueList dialogues;
		public int dialogueProgress = 0;
		
		public Clan clanOwned = null;
		public Clan clan = null;
		
		public int smeltAmount = 0;
		public long lastSmelt = 0;
		public boolean stopSmithing = false;
		public boolean isSmithing = false;
		
		public boolean onTurret = false;
		
		public int prayerRenewal = 0;
		
		public int donatorPoints = 0;
		
		public boolean inCastleWars = false;
		public CastleWarsTeam cwTeam = null;
		public int cwRespawnPointTime = 120;
		
		public boolean chargeAdd = false;
		public int chargesAdd = 0;
		public int repairSlot = 0;
		
		public String totalWealth = "0";
		
		public long evaluatorEnd = 0;
		public boolean evaluatorTrial = false;
		
		public int weenColor = 34503, phatColor = 34503;
		
		public NPC familiar;
		public int[] bobItems = new int[30], bobItemsN = new int[30];
		public int familiarTime = 0;
		public long lastCall = 0, lastWithdraw = 0, lastClick = 0, lastAttack = 0, lastSpec = 0;
		public SummoningData.pouchData famType = null;
		public boolean summonEvent = false;
		public double summonDrain = 0.0;
		public boolean disableStuff = false;
		public boolean statementCloses = true;
		
		
		
		public void stillCamera(int x, int y, int height, int speed, int angle) {
			outStream.createFrame(177);
			outStream.writeByte(x);
			outStream.writeByte(y);
			outStream.writeWord(height);
			outStream.writeByte(speed);
			outStream.writeByte(angle);
		}

		public void spinCamera(int direction, int i2, int i3, int i4, int i5) {
			outStream.createFrame(166);
			outStream.writeByte(direction);
			outStream.writeByte(i2);
			outStream.writeWord(i3);
			outStream.writeByte(i4);
			outStream.writeByte(i5);
		}

		public void resetCamera() {
			outStream.createFrame(107);
			updateRequired = true;
			appearanceUpdateRequired = true;
		}
		
		
		public int calculateMaxLifePoints() {
			int lifePoints = getLevelForXP(playerXP[3]);
			int helm = playerEquipment[0];
			int body = playerEquipment[0x4];
			int legs = playerEquipment[0x7];
			String s = "";
			if (helm > 0) {
				s = ItemData.data[helm].getName().toLowerCase();
				if (isNexItem(s)) 
					lifePoints += 7;
			}
			if (body > 0) {
				s = ItemData.data[body].getName().toLowerCase();
				if (isNexItem(s)) 
					lifePoints += 20;
			}
			if (legs > 0) {
				s = ItemData.data[legs].getName().toLowerCase();
				if (isNexItem(s)) 
					lifePoints += 13;
			}
			return lifePoints;
		}
		
		public boolean isNexItem(String name) {
			return (name.contains("torva") || name.contains("virtus") || name.contains("pernix"));
		}
		
		public boolean hasNexItems() {
			return calculateMaxLifePoints() > getPA().getLevelForXP(playerXP[3]);
		}
		
		
		public int[] gwdKC = new int[5];
		
		
		public void sendClanRanks() {
			if (clanOwned == null) { return; }
			String d = "";
			for (int i = 0; i < 100; i++) {
				if (clanOwned.ranks[i] != null) {
					d += "#"+clanOwned.ranks[i]+"/"+clanOwned.ranksN[i]+"/";
				}
			}
			d += "#";
			getPA().sendFrame126(d, 24149);
		}
		
		public String long2Name(long s) {
			return fixName(nameForLong(s));
		}
		
		public static String nameForLong(long l)
		{
				if(l <= 0L || l >= 0x5b5b57f8a98a5dd1L)
					return "invalid_name";
				if(l % 37L == 0L)
					return "invalid_name";
				int i = 0;
				char ac[] = new char[12];
				while(l != 0L) 
				{
					long l1 = l;
					l /= 37L;
					ac[11 - i++] = validChars[(int)(l1 - l * 37L)];
				}
				return new String(ac, 12 - i, i);
			}
		public static String fixName(String s)
		{
			if(s.length() > 0)
			{
				char ac[] = s.toCharArray();
				for(int j = 0; j < ac.length; j++)
					if(ac[j] == '_')
					{
						ac[j] = ' ';
						if(j + 1 < ac.length && ac[j + 1] >= 'a' && ac[j + 1] <= 'z')
							ac[j + 1] = (char)((ac[j + 1] + 65) - 97);
					}

				if(ac[0] >= 'a' && ac[0] <= 'z')
					ac[0] = (char)((ac[0] + 65) - 97);
				return new String(ac);
			} else
			{
				return s;
			}
		}
		private static final char[] validChars = {
			'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 
			't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', 
			'3', '4', '5', '6', '7', '8', '9'
		};
		
		
		
		public int slayerPoints = 0;
		public int slayerStreak = 0;
		public int taskId = 0;
		public int taskAmount = 0;
		
		
		

		public int overloads = 0;
		public boolean overloadEffect = false;
		public void drankOverload() {
			CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
        		@Override
        		public void execute(CycleEventContainer e) {
        			if (overloads > 0) {
        				overloadEffect = true;
	        			startAnimation(3170);
	        			int damage = (skillLevel[3] <= 10) ? (skillLevel[3] - 1) : 10;
	        			handleHitMask(damage, 0, 6, 0, true);
	        			dealDamage(damage);
	        			overloads -= 1;
	        			getPA().refreshSkill(3);
	        			if (overloads == 0 || damage < 10) {
	        				e.stop();
	        			}
        			} else {
        				e.stop();
        			}
        		}
        		public void stop() {
        			overloadEffect = false;
        			overloads = 0;
        		}
			}, 2);
		}
		
		
		
		
		
	/**
	 * Used to stop block emote being used an instant after the attack emote is called. Which ends up cancelling the attack emote.
	 */
	public long lastAttackAnimationTimer;
	
	/**
	 * Potion sip timer.
	 */
	public byte timer;
	
	 /**
     * The amount of ticks the startInterfaceEvent will last for.
     */
    public int extraTime;
    
    /**
     * True, if the startSkullTimerEvent is active.
     */
    public boolean isUsingSkullTimerEvent;
	
	/**
	 * True, if the player is logging out manually.
	 */
	public boolean manualLogOut;
	
	
	
	/**
     * Set the state of inventoryUpdate.
     * @param state
     * 			State of inventoryUpdate.
     */
    public void setInventoryUpdate(boolean state)
    {
            inventoryUpdate = state;
    }
    
    private MoneyPouch mpouch = new MoneyPouch((Client) this);
    public MoneyPouch getPouch() {
    	return mpouch;
    }

    /**
     * @return
     * 			The state of inventoryUpdate.
     */
    public boolean getInventoryUpdate()
    {
            return inventoryUpdate;
    }

    /**
     * True, if the inventory needs visual updating.
     */
    private boolean inventoryUpdate;

        /**
         * Store the time of when the casket was last opened.
         */
        public long casketTime;

        /**
         * Amount of Chaotic charges left.
         */
        public int chaoticCharges = 600;

        /**
         * Is the wcTimerEvent being used?
         */
        public boolean isUsingWcTimerEvent;

        /**
         * True, if the crafting event is being used.
         */
        public boolean craftingEvent;

        /**
         * True, if the herblore event is being used.
         */
        public boolean usingHerbloreEvent;

        /**
         * Is the miningTimerEvent being used?
         */
        public boolean isUsingMiningTimerEvent;

        /**
         * Is the fishTimerEvent being used?
         */
        public boolean isUsingFishTimerEvent;

        /**
         * Get the amount of nomadsKilled
         * @return
         * 			The amount of nomadsKilled.
         */
        public int getWildernessHunterCount()
        {
                return nomadsKilled;
        }

        /**
         * The total amount of the item scanned in the and inventory.
         */
        public int quantityOfItem;

        /**
         * True, if the player is typing in the timed ban amount.
         */
        public boolean typingTimedBanAmount;

        /**
         * The player to time ban.
         */
        public String playerToTimeBan;

        /**
         * Time untill the player is un-banned.
         */
        public long timeUnBanned;

        /**
         * True, if the player is typing in the timed mute amount.
         */
        public boolean typingTimedMuteAmount;

        /**
         * The player to time mute.
         */
        public String playerToTimeMute;

        /**
         * The time the player will be un-muted.
         */
        public long timeUnMuted;

        /**
         * True, if the player is using Hand cannon special attack.
         */
        public boolean handCannonSpecialAttack;

        /**
         * The damage of the Morrigan's javelin special attack to deal, either 5 or less, depending on the victim's hitpoints.
         */
        public int morrigansJavelinDamageToDeal;

        /**
         * Amount of Morrigan's javelin special attack damages to apply.
         */
        public int amountOfDamages;

        /**
         * True, if Morrigan's javelin special attack is being used.
         */
        public boolean morrigansJavelinSpecialAttack;

        /**
         * The time of when the last poison damage appeared..
         */
        public long lastPoison;

        /**
         * The time of when the last anti-poison potion was taken.
         */
        public long lastPoisonSip;

        /**
         * The amount of time of immunity to poison.
         */
        public long poisonImmune;

        /**
         * True, if the player has the change title input interface opened.
         */
        public boolean changeTitleInput;

        /**
         * The last ip connected to the account.
         */
        public String lastIP = "";

        /**
         * The time of when the player logged out.
         */
        public long logOutTime = System.currentTimeMillis();

        /**
         * The amount of Nomad NPCs killed.
         */
        public int nomadsKilled;

        /**
         * True, if the player's character was just created.
         */
        public boolean newPlayer;

        /**
         * True if Saradomin special attack is activated.
         */
        public boolean saradominSwordSpecialAttack;

        /**
         * True if player is wearing full Guthan's.
         */
        public boolean wearingFullGuthan;

        /**
         * True if player is wearing full Verac's.
         */
        public boolean wearingFullVerac;

        /**
         * True, if the player has received the third result of a Dragon claws special attack.
         */
        public boolean thirdResultOfDragonClaws;

        /**
         * True, if the player is using a special attack that causes multiple damage. e.g: Dragon dagger, Dragon claws and Dragon halberd.
         */
        public boolean multipleDamageSpecialAttack;

        /**
         * Get the state of multipleDamageSpecialAttack.
         * @return
         * 			The state of multipleDamageSpecialAttack.
         */
        public boolean getMultipleDamageSpecialAttack()
        {
                return multipleDamageSpecialAttack;
        }

        /**
         * Change the state of multipleDamageSpecialAttack.
         * @param state
         * 			The state of multipleDamageSpecialAttack.
         */
        public void setMultipleDamageSpecialAttack(boolean state)
        {
                multipleDamageSpecialAttack = state;
        }

        /**
         * True, if the player is using a weapon special attack.
         */
        public boolean specialAttack;

        /**
         * Get the state of specialAttack.
         * @return
         * 			The state of specialAttack.
         */
        public boolean getSpecialAttack()
        {
                return specialAttack;
        }

        /**
         * Change the state of specialAttack.
         * @param state
         * 			The state of specialAttack.
         */
        public void setSpecialAttack(boolean state)
                {
                        specialAttack = state;
                }
                /**
                 * True, if the player is using Dragon claws special attack.
                 */
        public boolean dragonClawsSpecialAttack;

        /**
         * Change the state of dragonClawsSpecialAttack.
         * @param state
         * 			The state of dragonClawsSpecialAttack.
         */
        public void setDragonClawsSpecialAttack(boolean state)
        {
                dragonClawsSpecialAttack = state;
        }

        /**
         * Get the state of dragonClawsSpecialAttack.
         * @return
         * 			The state of dragonClawsSpecialAttack.
         */
        public boolean getDragonClawsSpecialAttack()
        {
                return dragonClawsSpecialAttack;
        }
        
        /**
         * True, if the player is using the dragon claws special attack event.
         */
        public boolean usingDragonClawsSpecialAttackEvent;
        
        /**
         * Get the state of usingDragonClawsSpecialAttackEvent.
         * @return
         * 			The state of usingDragonClawsSpecialAttackEvent.
         */
        public boolean getUsingDragonClawsSpecialAttackEvent()
        {
        	return usingDragonClawsSpecialAttackEvent;
        }
        
        /**
         * Set the state of usingDragonClawsSpecialAttackEvent.
         * @param state
         * 			The state of usingDragonClawsSpecialAttackEvent.
         */
        public void setUsingDragonClawsSpecialAttackEvent(boolean state)
        {
        	usingDragonClawsSpecialAttackEvent = state;
        }

        /**
         * Store the item identity and the amount that will be added to the player's inventory after death.
         */
        public Map < Integer, Integer > itemsToInventory = new HashMap < Integer, Integer > ();

        /**
         * True, if the player has a red skull.
         */
        public boolean redSkull;

        /**
         * Change the state of redSkull.
         * @param state
         * 			The state of redSkull.
         */
        public void setRedSkull(boolean state)
        {
                redSkull = state;
        }

        /**
         * Get the state of redSkull.
         * @return
         * 			The state of redSkull.
         */
        public boolean getRedSkull()
        {
                return redSkull;
        }

        /**
         * True, if the player has a white skull.
         */
        public boolean whiteSkull;

        /**
         * Change the state of whiteSkull.
         * @param state
         * 			The state of whiteSkull.
         */
        public void setWhiteSkull(boolean state)
        {
                whiteSkull = state;
        }

        /**
         * Get the state of whiteSkull.
         * @return
         * 			The state of whiteSkull.
         */
        public boolean getWhiteSkull()
        {
                return whiteSkull;
        }

        /**
         * Amount of milliseconds, the player has been online for, in this session.
         */
        public long millisecondsOnline;

        /**
         * The amount of seconds, the player has been online for.
         */
        public int secondsOnline;

        /**
         * The date of when the account was created.
         */
        public String accountDateCreated;

        /**
         * True, if the special attack event is being used.
         */
        public boolean specialAttackEvent;

        /**
         * Amount of item points.
         */
        public int itemPoints;

        /**
         * Get the amount of itemPoints.
         * @return
         * 			The amount of itemPoints.
         */
        public int getItemPoints()
        {
                return itemPoints;
        }

        /**
         * Amount of wolpertinger special attacks remaining.
         */
        public int wolpertingerSpecialAttack;

        /**
         * Get the amount of wolpertingerSpecialAttack.
         * @return
         * 			The amount of wolpertingerSpecialAttack.
         */
        public int getWolpertingerSpecialAttack()
                {
                        return wolpertingerSpecialAttack;
                }
        
                /**
                 * Store the time of the last Wolpertinger special attack used.
                 */
        public long lastWolpertingerSpecialAttack;
        
        
        private final ItemAdder itemadder = new ItemAdder(this);
        public ItemAdder getIA() {
        	return itemadder;
        }
        
        

        /**
         * This method is called when the player has logged out.
         **/
        public void destruct() {
        	if (gamble != null) {
        		gamble.decline(this);
        	}
			if (inPcGame() || inPcBoat()) {
				PestControl.removePlayerGame(this);
			}
        		PriceChecker.close(this);
        		Barrows.checkBrother(this);
			    if (getHouse() != null) { getHouse().save(); }
        		if (clan != null) { ClanHandler.leaveClan(this, "", false); }
        		if (clanOwned != null) { new ClanSaver(clanOwned); }
              	getDuelArena().claimStakedItems();
                Poison.informClientOfPoisonOff(this);
                AccountFlagging.scanAccount(this);
                MonsterKillLog.saveLog(this);
                if (profile != null) {
                	profile.update(this);
                }
                this.lastIP = this.connectedFrom;
                CycleEventHandler.getSingleton().stopEvents(this);
                PlayerSave.saveGame((Client) this);
                HostList.getHostList().remove(session);
                disconnected = true;
                session.close();
                session = null;
                inStream = null;
                outStream = null;
                isActive = false;
                buffer = null;
                playerListSize = 0;
                for (int i = 0; i < maxPlayerListSize; i++) {
                        playerList[i] = null;
                }
                absX = absY = -1;
                mapRegionX = mapRegionY = -1;
                currentX = currentY = 0;
                Movement.resetWalkingQueue(this);
        }

        /**
         * Change the interface of a certain tab.
         *
         * @param menuId
         *            The tab identity.
         * @param form
         *            The interface to spawn in the tab.
         */
        public void setSidebarInterface(int menuId, int form)
        {
                synchronized(this)
                {
                        if (getOutStream() != null)
                        {
                                outStream.createFrame(71);
                                outStream.writeWord(form);
                                outStream.writeByteA(menuId);
                        }
                }
        }

        /**
         * True, if the player's account has just been created. Used to add starter package.
         */
        public boolean addStarter;

        /**
         * @param amount
         *            The amount to add to the Hitpoints of the player.
         */
        public void addToHitPoints(int amount)
        {
                if (this.isDead)
                {
                        return;
                }
                if (skillLevel[Constants.HITPOINTS] + amount > maximumHitPoints())
                {
                        int extraAmount = (skillLevel[Constants.HITPOINTS] + amount) - maximumHitPoints();
                        amount -= extraAmount;
                        skillLevel[Constants.HITPOINTS] += amount;
                }
                else
                {
                        skillLevel[Constants.HITPOINTS] += amount;
                }
                Client c = (Client) PlayerHandler.players[this.playerId];
                c.getPA().refreshSkill(3);
        }

        /**
         * @param amount
         *            The amount to subtract from the Hitpoints of the player.
         */
        public void subtractFromHitPoints(int amount)
        {
                if (this.isDead)
                {
                        return;
                }
                if (skillLevel[Constants.HITPOINTS] - amount < 0)
                {
                        amount = skillLevel[Constants.HITPOINTS];
                }
                else
                {
                        skillLevel[Constants.HITPOINTS] -= amount;
                }
                this.getPA().refreshSkill(Constants.HITPOINTS);
        }

        /**
         * @param amount
         *            Change the player's Hitpoints to this.
         */
        public void setHitPoints(int amount)
        {
                if (this.isDead)
                {
                        return;
                }
                skillLevel[Constants.HITPOINTS] = amount;
                Client c = (Client) PlayerHandler.players[this.playerId];
                c.getPA().refreshSkill(3);
        }

        /**
         * Amount of times the player has died in a safe area.
         */
        public int safeDeath;

        /**
         * Amount of times the player has killed another player in a safe area.
         */
        public int safeKill;

        /**
         * Amount of potion doses drank.
         */
        public int potionDrank;

        /**
         * Amount of attacks started by the player.
         */
        public int attacksGiven;

        /**
         * @param amount
         *            The amount to add to the attacksGiven player integer.
         */
        public void addAttacksGiven(int amount)
        {
                attacksGiven += amount;
        }

        /**
         * Amount of attacks received by the player.
         */
        public int attacksReceived;

        /**
         * @param amount
         *            The amount to add to the attacksReceived player integer.
         */
        public void addAttacksReceived(int amount)
        {
                attacksReceived += amount;
        }

        /**
         * KDR of the player, used for highscores.
         */
        public int kdr;

        /**
         * True, if snow particles is showing.
         */
        public boolean snow;

        /**
         * True, if the resting event is active.
         */
        public boolean restingEvent;
        /**
         * True, if the player is resting.
         */
        public boolean resting;

        /**
         * The delay in milliseconds to gain energy.
         */
        public int agilityRestoreDelay = 3000;

        /**
         * Run energy remaining.
         */
        public double runEnergy = 100;

        /**
         * Store the time of the last time the player had their run energy restored.
         */
        public long lastRunRecovery;

        /**
         * True, if the player is running.
         */
        public boolean isRunning()
        {
                return isNewWalkCmdIsRunning() || (isRunning2 && isMoving);
        }

        /**
         * Store the time of when the Title list button was used.
         */
        public long titleListTimer;
        /**
         * True, if the player is idle.
         */
        public boolean isIdle;

        /**
         * Store the time of when a clan chat message was last sent by the player.
         */
        public long clanChatMessageTime;

        /**
         * True, if the player is dead.
         */
        public boolean isDead;

        /**
         * Change the state of isDead.
         */
        public void isDead(boolean state)
        {
                isDead = state;
        }

        /**
         * True, if the player has summoned a pet.
         */
        public boolean petSummoned;

        /**
         * The state of petSummoned.
         * @return
         * 			The state of petSummoned.
         */
        public boolean getPetSummoned()
        {
                return petSummoned;
        }

        /**
         * Change the state of petSummoned.
         * @param state
         * 			The state of petSummoned.
         */
        public void setPetSummoned(boolean state)
        {
                petSummoned = state;
        }

        /**
         * The NPC type of the pet that the player currently has summoned.
         */
        public int petID;
        
        /**
         * True if the ladder event is being used.
         */
        public boolean ladderEvent;

        /**
         * True, if the idle Event is in use.
         */
        public boolean idleEventUsed;

        /**
         * This will keep increasing by +1 when the player is not sending any
         * packets to the server. If this is 3 or more, then the player is not
         * sending any connections to the server.
         * <p>
         * This is used to disconnect the player, if in-combat, after 40 seconds.
         */
        public int timeOutCounter = 0;

        /**
         * Is the clickNpcTypeEvent1 being used?
         */
        public boolean usingClickNpcType1Event;

        /**
         * Is the clickNpcTypeEvent2 being used?
         */
        public boolean usingClickNpcType2Event;

        /**
         * Is the clickNpcTypeEvent3 being used?
         */
        public boolean usingClickNpcType3Event;

        /**
         * Is the clickObject1Event active?
         */
        public boolean doingClickObjectType1Event;

        /**
         * Is the clickObject2Event active?
         */
        public boolean doingClickObjectType2Event;

        /**
         * Is the clickObject3Event active?
         */
        public boolean doingClickObjectType3Event;
        /**
         * Is the clickObject4Event active?
         */
        public boolean doingClickObjectType4Event;
        /**
         * Is the clickObject5Event active?
         */
        public boolean doingClickObjectType5Event;

        /**
         * if true, the player cannot perform any action and is performing agility.
         */
        public boolean doingAgility;

        /**
         * The state of doingAgility.
         * @return
         * 		The state of doingAgility.
         */
        public boolean getDoingAgility()
        {
                return doingAgility;
        }

        /**
         * Change the state of doingAgility.
         * @param state
         * 			The state of doingAgility.
         */
        public void setDoingAgility(boolean state)
        {
                doingAgility = state;
        }

        /**
         * True, if the interface Cycle Event is being used.
         */
        public boolean isUsingInterfaceEvent;

        /**
         * Store the absorb amount.
         */
        public double absorbtionAmountRange;

        /**
         * The range damage for a single hit.
         */
        public int rangeSingleDamage;

        /**
         * The range damage for a double hit.
         */
        public int rangeSecondDamage;

        /**
         * True, to show the Diamond bolts (e) GFX during the hitsplat.
         */
        public boolean showDiamondBoltGFX;

        /**
         * True, to show the Dragon bolts (e) GFX during the hitsplat.
         */
        public boolean showDragonBoltGFX;

        /**
         * True, to show the Ruby bolts (e) GFX during the hitsplat.
         */
        public boolean showRubyBoltGFX;

        /**
         * Store the maximum damage of the player.
         */
        public int maximumDamageRange;

        /**
         * Absorb amount of Melee.
         */
        public double absorbtionAmountMelee;

        /**
         * * Store the normal single hit damage of a melee weapon..
         */
        public int meleeFirstDamage;

        /**
         * * Store the second hit of a Dragon dagger or Halbred special attack.
         */
        public int meleeSecondDamage;

        /**
         * Store the third hit of a Dragon claw special attack.
         */
        public int meleeThirdDamage;

        /**
         * Store the fourth hit of a Dragon claw special attack.
         */
        public int meleeFourthDamage;

        /**
         * Maximum damage of Melee.
         */
        public int maximumDamageMelee;

        /**
         * Magic damage.
         */
        public int magicDamage;

        /**
         * Maximum damage of Magic.
         */
        public int maximumDamageMagic;

        /**
         * Absorb amount of Magic.
         */
        public double absorbtionAmountMagic;

        /**
         * Amount of 600ms cycles untill the teleport finishes.
         */
        public byte teleportCycle;

        /**
         * This will create a delay so the player cannot teleport twice before
         * reaching destination.
         */
        public long teleportDelay;

        /**
         * True if the player has started a teleport. Do not use this to check if
         * the player is teleporting.
         */
        public boolean isTeleporting;

        /**
         * True when the teleportDelay long is used.
         */
        public boolean teleportDelayHasStarted;

        /**
         * True, if the player is teleporting. Use this to check if a player is
         * teleporting.
         */
        public boolean isTeleporting()
        {
                if (System.currentTimeMillis() - teleportDelay > 1000 && teleportDelayHasStarted)
                {
                        isTeleporting = false;
                        teleportDelayHasStarted = false;
                }
                if (System.currentTimeMillis() - teleportDelay <= 1000 || isTeleporting)
                {
                        return true;
                }
                return false;

        }

        /**
         * True, if Magic bow special attack is being used.
         */
        public boolean magicBowSpecialAttack;

        /**
         * True, if using Dark bow to start a normal attack.
         */
        public boolean usingDarkBowNormalAttack;

        /**
         * True if magic will splash.
         */
        public boolean magicSplash;

        /**
         * The level in a skill.
         */
        public int[] skillLevel = new int[25];

        /**
         * Experience in a skill.
         */
        public int[] playerXP = new int[25];

        /**
         * The maximum Hitpoints of the player.
         */
        //public int maximumHitPoints = getLevelForXP(playerXP[Constants.HITPOINTS]);
        
        public int maximumHitPoints() {
        	return calculateMaxLifePoints();
        }
        

        /**
         * The other player that is being attacked by this player.
         */
        public int playerIndex;

        /**
         * True, if the player is teleporting.
         */
        public boolean teleporting;

        /**
         * True, if the player is using range.
         */
        public boolean usingRange;

        /**
         * The state of usingRange.
         * @return
         * 			The state of usingRange.
         */
        public boolean getUsingRange()
        {
                return usingRange;
        }

        /**
         * Change the state of usingRange.
         * @param state
         * 			The state of usingRange.
         */
        public void setUsingRange(boolean state)
        {
                usingRange = state;
        }

        /**
         * True if the player is a Normal player.
         */
        public boolean isNormalRank()
        {
                return playerRights == 0;
        }

        /**
         * True if the player is a Moderator.
         */
        public boolean isModeratorRank()
        {
                return playerRights == 1;
        }

        /**
         * True if the player is an Administrator.
         */
        public boolean isAdministratorRank()
        {
                return playerRights == 2;
        }

        /**
         * True if the player is a Member.
         */
        public boolean isMember()
        {
                return playerRights == 3;
        }

		public boolean isIronman()
		{
			    return playerRights == 4;
		}

        /**
         * True, if the player wants to see the Bounty hunter interface rather than
         * the normal wilderness interface.
         */
        public boolean wildInterface = true;

        /**
         * Has the player finished logging in?
         */
        public boolean loggingInFinished;

        
        public boolean barrowsDoor = false;
        public int barrowsDoorTicks = 0;
        
        /**
         * True if the player is doing an action.
         *
         * @param looped
         *            True, if this method is being called every main game tick.
         */
        public boolean doingAction(boolean looped) {
                if (!looped) {
                        if (isIdle) {
                                isIdle = false;
                                startAnimation(65535);
                        }
                }
                if (doingActionTimer > 0) {
                        return true;
                }

                return false;
        }

        /**
         * if 1 or more, the player cannot do anything..
         */
        public int doingActionTimer;

        /**
         * Is the doingActionEvent being used?
         */
        public boolean isUsingDoingActionEvent;

        /**
         * Decrease the doingAction variable untill it reaches 0.
         *
         * @param time
         *            The amount of cycles the player will be doing an action.
         */
        public void doingActionEvent(int time)
        {
                /* Check if this event is being used, if it is, then stop */
                if (isUsingDoingActionEvent)
                {
                        return;
                }
                isUsingDoingActionEvent = true;

                doingActionTimer = time;

                /* The event is continious untill doingAction reaches 0. */
                CycleEventHandler.getSingleton().addEvent(this, new CycleEvent()
                {@
                        Override
                        public void execute(CycleEventContainer container)
                        {
                                if (doingActionTimer > 0)
                                {
                                        doingActionTimer--;
                                }
                                else
                                {
                                        container.stop();
                                }
                        }

                        @
                        Override
                        public void stop()
                        {
                                isUsingDoingActionEvent = false;
                        }
                }, 1);

        }


        /**
         * These are to be used for creating delays, using
         * System.currentTimeMillis(), If there is a few objects close to the
         * player, use a different delay variable for each one. The different
         * variables are objectDelay1/2/3/4/5
         */
        public long objectDelay1, objectDelay2, objectDelay3, objectDelay4, objectDelay5;

        /**
         * Store the time of when the player last attacked another player.
         */
        public long lastTimeEngagedPlayer;

        /**
         * Store the time of when the NPC last attacked the player.
         */
        public long lastTimeEngagedNPC;

        /**
         * The players i have recently attacked. Used for skulling.
         */
        public ArrayList < Integer > attackedPlayers = new ArrayList < Integer > ();

        /**
         * Amount of earning potential.
         */
        public int EP = 0;

        /**
         * Minutes spent gaining earning potential.
         */
        public int EP_MINUTES;

        /**
         * Target percentage.
         */
        public int targetPercentage;

        /**
         * The target.
         */
        public int targetIndex;

        /**
         * Amount left untill i lose the target.
         */
        public int logoutTimer;

        /**
         * True if to start gaining earning potential.
         */
        public boolean EP_ACTIVE;

        /**
         * The target name.
         */
        public String targetName;

        /**
         * Amount of poison time remaining.
         */
        public byte poisonRunOut;

        /**
         * Amount of achievement points.
         */
        public short achievementPoint;

        /**
         * Amount of times the player died in a dangerous area.
         */
        public short deathCount;

        /**
         * Amount of players killed in a dangeorus area.
         */
        public short killCount;

        /**
         * The player title.
         */
        public int title;

        /**
         * True, if the player can use player titles.
         */
        public boolean canSetTitle;

        /**
         * Total level.
         */
        public int totalLevel;

        /**
         * Total experience in all skills.
         */
        public BigInteger xpTotal;

        /**
         * Quick prayers of Ancient curses.
         */
        public boolean[] quickCurses = new boolean[QuickCurses.MAX_CURSES];
        
        /**
         * Quick prayers of normal prayers.
         */
        public boolean[] quickPrayers = new boolean[QuickPrayers.MAX_PRAYERS];
        
        /**
         * True if quick prayers of normal prayers are active.
         */
        public boolean quickPray;
        
        /**
         * True if quick prayers of Ancient curses are active.
         */
        public boolean quickCurse;

        public int[][] compPreset = {
        		{-2805961, -5626324, -8643808, -12249823, -66051, -449772, -5342402}, 
        		{-2805961, -5626324, -8643808, -12249823, -66051, -449772, -5342402}, 
        		{-2805961, -5626324, -8643808, -12249823, -66051, -449772, -5342402}};
        public int[] compColorsRGB = {-2805961, -5626324, -8643808, -12249823, -66051, -449772, -5342402};
        public int[] compColor = {65214, 65200, 65186, 62995, 64639, 961, 5683};
        
        
        public void sendCompCapePresets() {
        	for (int i = 0; i < 3; i++) {
        		String b = "";
        		for (int f = 0; f < 7; f++) {
        			b += compPreset[i][f] + " ";
        		}
        		getPA().sendFrame126(b, 18939 + i);
        	}
        }
        

        /**
         * 0: ore identity.<p>
         * 1: level required to mine the ore.<p>
         * 2: experience to gain from the ore.
         */
        public int[] oreInformation = new int[3];
        
        /**
         * Amount of game ticks untill the ore is mined.
         */
        public int miningTimer = 0;
        
        /**
         * The amount of game ticks, the ropeSwing cycle event will last for.
         */
        public int ropeSwingActionEvent;
        
        /**
         * The amount of game ticks, the crumbling wall cycle event will last for.
         */
        public int firstCrumblingWallActionEvent;
        
        
        /**
         * The amount of game ticks, the log balance cycle event will last for.
         */
        public int logBalanceActionEvent;
        
        /**
         * The amount of game ticks, the wood cutting event will last for.
         */
        public int woodCuttingEventTimer;
        
        /**
         * The amount of game ticks, the fishing event will last for.
         */
        public int fishTimerAmount = 0;
        
        /**
         * The identity of the latest attacker (other player) that attacked this player.
         */
        public int lastAttackedBy;
        
        public boolean seedPlanted = false;
        public boolean seedWatered = false;
   		public boolean patchRaked = false;
   		public boolean patchCleaned = false;
        public boolean logBalance;
        public boolean obstacleNetUp;
        public boolean treeBranchUp;
        public boolean balanceRope;
        public boolean treeBranchDown;
        public boolean obstacleNetOver;
        public boolean ropeSwing;
        public boolean logBalance1;
        public boolean obstacleNet;
        public boolean balancingLedge;
        public boolean Ladder;
        public int waitTime = 0;
        public int doAmount;
        public boolean playerFletch, playerIsFiremaking;
        public int[][] playerSkillProp = new int[20][15];
        public boolean stopPlayerSkill;
        public long lastFire;
        public long lastLockPick;
        public boolean smeltInterface;
        public int[] farm = new int[2];
        public long agility1;
        public long agility2;
        public long agility3;
        public long agility4;
        public long agility5;
        public long agility6;
        public long agility7;
        public int totalPlayerDamageDealt;
        public int magicDamage1;
        public int maxMageDamage;
        public int privateChat;
        public int specEffect;
        public int specBarId;
        public int attackLevelReq;
        public int defenceLevelReq;
        public int strengthLevelReq;
        public int rangeLevelReq;
        public int magicLevelReq;
        public int prayerLevelReq;
        public int followId;
        public int switches;
        public int skullTimer;
        public int votingPoints;
        public int nextChat;
        public int dialogueAction;
        public int autocastId;
        public int followDistance;
        public int followId2;
        public int barrageCount;
        public int voteTotalPoints;
        public int votePoints;
        public int delayedDamage;
        public int delayedDamage2;
        public int barrowsRunCompleted;
        public int bossKill;
        public int foodEverAte;
        public int pointsEverEarned;
        public int desertTreasure;
        public int autoRet;
        public int pcDamage;
        public int xInterfaceId;
        public int xRemoveId;
        public int xRemoveSlot;
        public int tzhaarToKill;
        public int tzhaarKilled;
        public int CashPile;
        public int frozenBy;
        public int magicAltar;
        public int bonusAttack;
        public int lastNpcAttacked;
        public int destroyItem;
        public int npcId2;
        public int lastArrowUsed = -1;
        public int lastChatId = 1;
        public int clawTargNPC;
        public int hit1;
        public int hit2;
        public int hit3;
        public int hit4 = -1;
        public int clanId = -1;
        public int talkingNpc = -1;
        public int safeTimer;
        public int droppedItem = -1;
        public boolean splitChat;
        public boolean antiFirePot;
        public boolean usedGmaul;
        public boolean inJail;
        public boolean initialized;
        public boolean disconnected;
        public boolean RebuildNPCList;
        public boolean isActive;
        public boolean hasMultiSign;
        public boolean saveCharacter;
        public boolean mouseButton;
        public boolean chatEffects = true;
        public boolean adminAttack;
        public boolean acceptAid;
        public boolean autocasting;
        public boolean adminHax;
        public boolean mageFollow;
        public boolean usingDarkBowSpecialAttack;
        public boolean vengOn;
        public boolean msbSpec;
        public boolean isLevitate;
        public boolean isUsingDeathInterface;
        public boolean offMessages;
        public boolean attackSkill;
        public boolean strengthSkill;
        public boolean defenceSkill;
        public boolean mageSkill;
        public boolean rangeSkill;
        public boolean prayerSkill;
        public boolean healthSkill;
        public final int ACCURATE = 0;
        public final int RAPID = 1;
        public final int AGGRESSIVE = 1;
        public final int LONGRANGE = 2;
        public final int BLOCK = 2;
        public final int DEFENSIVE = 2;
        public final int CONTROLLED = 3;
        public String lastKilled;
        public String currentTime, date;
        public boolean orb;
        public int[] itemKeptId = new int[4];
        public int freezeDelay, freezeTimer = -6, oldPlayerIndex,
                lastWeaponUsed, projectileStage, Prayerbook, playerMagicBook,
                teleGfx, teleEndGfx, teleEndAnimation, teleHeight, teleX, teleY,
                rangeItemUsed, killingNpcIndex, totalDamageDealt, oldNpcIndex,
                attackTimer, npcIndex, npcClickIndex, npcType,
                castingSpellId, oldSpellId, spellId, hitDelay, hitDelay2, dBowHits;
        public boolean oldMagicFailed;
        public int bowSpecShot, clickNpcType, clickObjectType, objectId, objectX,
        objectY, objectXOffset, objectYOffset, objectDistance;
        public int pItemX, pItemY, pItemId;
        public boolean isMoving, walkingToItem;
        public boolean isShopping, updateShop;
        public int myShopId;
        public int tradeStatus, tradeWith;
        public boolean forcedChatUpdateRequired, inDuel, tradeAccepted, goodTrade,
        inTrade, tradeRequested, tradeResetNeeded, tradeConfirmed,
        tradeConfirmed2, canOffer, acceptTrade, acceptedTrade;
        public int attackAnim, animationRequest = -1, animationWaitCycles;
        public int[] playerBonus = new int[12];
        public boolean isRunning2 = true;
        public boolean takeAsNote;
        public int combatLevel;
        public boolean saveFile;
        public int playerAppearance[] = new int[13];
        public int actionID;
        public int wearItemTimer, wearId, wearSlot, interfaceId;
        public int XremoveSlot, XinterfaceID, XremoveID, Xamount;
        public boolean[] invSlot = new boolean[28], equipSlot = new boolean[14];
        public long friends[] = new long[200];
        public long ignores[] = new long[200];
        public double specAmount = 10;
        public double specAccuracy = 1;
        public double specDamage = 1;
        public double prayerPoint = 1.0;
        public int teleGrabItem;
        public int teleGrabX;
        public int teleGrabY;
        public int duelCount;
        
        /**
         * The attacker(other player) identity that is attacking this player.
         */
        public int underAttackBy;
        public int underAttackBy2;
        public int wildLevel;
        public int teleTimer;
        public int teleBlockLength;
        public int poisonDelay;
        public int vengTimer;
        public long lastPlayerMove;
        public long lastSpear;
        public long dfsDelay;
        public long lastVeng;
        public long lastYell;
        public long teleGrabDelay;
        public long lastAction;
        public long lastThieve;
        public long alchDelay;
        public long duelDelay;
        public long teleBlockDelay;
        public long godSpellDelay;
        public long singleCombatDelay2;
        public long reduceStat;
        public long restoreStatsDelay;
        public long logoutDelay;
        public long buryDelay;
        public long foodDelay;
        public long potDelay;
        public long diceDelay;
        public long ditchDelay;
        public long teletabDelay;
        public long restoreDelay;
        public long restoreStatsDelay2 = 6000;
        public boolean canChangeAppearance;
        public boolean mageAllowed;
        public byte poisonMask = 0, duelForceChatCount = 4;
        public int reduceSpellId;
        public int headIcon = -1;
        public boolean[] curseActive = new boolean[20];
        public int duelTimer, duelTeleX, duelTeleY, duelSlot, duelSpaceReq,
        duelOption, duelingWith, duelStatus;
        public int headIconPk = -1, headIconHints;
        public boolean duelRequested;
        public boolean doubleHit, usingSpecial, usingRangeWeapon,
        usingBow, usingMagic, castingMagic;
        public int fightMode;
        public boolean[] duelRule = new boolean[22];
        /* Smithing related. */
        public int item;
        public int xp;
        public int remove;
        public int removeamount;
        public int maketimes;
        /* Smithing related. */
        public final int[] BOWS = {
                9185, 18357, 839, 845, 847, 851, 855, 859, 841,
                843, 849, 853, 857, 861, 4212, 4214, 4215, 11235, 4216, 4217, 4218,
                4219, 4220, 4221, 4222, 4223, 6724, 4734, 4934, 4935, 4936, 4937, 15241
        };
        public final int[] ARROWS = {
                882, 884, 886, 888, 890, 892, 4740, 11212,
                9140, 9141, 4142, 9143, 9144, 9240, 9241, 9242, 9243, 9244, 9245, 15243
        };
        public final int[] NO_ARROW_DROP = {
                4212, 4214, 4215, 4216, 4217, 4218,
                4219, 4220, 4221, 4222, 4223, 4734, 4934, 4935, 4936, 4937, 15241
        };
        public final int[] OTHER_RANGE_WEAPONS = {
                863, 864, 865, 866, 867, 868,
                869, 806, 807, 808, 809, 810, 811, 825, 826, 827, 828, 829, 830,
                800, 801, 802, 803, 804, 805, 6522, 13879, 13883
        };
        public final int[][] MAGIC_SPELLS = {
                // example {magicId, level req, animation, startGFX, projectile Id,
                // endGFX, maxhit, exp gained, rune 1, rune 1 amount, rune 2, rune 2
                // amount, rune 3, rune 3 amount, rune 4, rune 4 amount}
                // Modern Spells
                {
                        1152, 1, 14221, 0, 2699, 2700, 2, 5, 556, 1, 558, 1, 0, 0, 0, 0
                }, // wind
                // strike
                {
                        1154, 5, 14220, 2701, 2703, 2708, 4, 7, 555, 1, 556, 1, 558, 1, 0, 0
                }, // water
                // strike
                {
                        1156, 9, 14222, 2713, 2718, 2723, 6, 9, 557, 2, 556, 1, 558, 1, 0, 0
                }, // earth
                // strike
                {
                        1158, 13, 14223, 2728, 2729, 2737, 8, 11, 554, 3, 556, 2, 558, 1, 0, 0
                }, // fire
                // strike
                {
                        1160, 17, 14221, 0, 2699, 2700, 9, 13, 556, 2, 562, 1, 0, 0, 0, 0
                }, // wind
                // bolt
                {
                        1163, 23, 14220, 2702, 2704, 2709, 10, 16, 556, 2, 555, 2, 562, 1, 0,
                        0
                }, // water bolt
                {
                        1166, 29, 14222, 2714, 2719, 2724, 11, 20, 556, 2, 557, 3, 562, 1, 0,
                        0
                }, // earth bolt
                {
                        1169, 35, 14223, 2728, 2731, 2738, 12, 22, 556, 3, 554, 4, 562, 1, 0,
                        0
                }, // fire bolt
                {
                        1172, 41, 14221, 0, 2699, 2700, 13, 25, 556, 3, 560, 1, 0, 0, 0, 0
                }, // wind
                // blast
                {
                        1175, 47, 14220, 2702, 2705, 2710, 14, 28, 556, 3, 555, 3, 560, 1, 0,
                        0
                }, // water blast
                {
                        1177, 53, 14222, 2715, 2720, 2725, 15, 31, 556, 3, 557, 4, 560, 1, 0,
                        0
                }, // earth blast
                {
                        1181, 59, 14223, 2728, 2733, 2739, 16, 35, 556, 4, 554, 5, 560, 1, 0,
                        0
                }, // fire blast
                {
                        1183, 62, 14221, 0, 2699, 2700, 17, 36, 556, 5, 565, 1, 0, 0, 0, 0
                }, // wind
                // wave
                {
                        1185, 65, 14220, 2702, 2706, 2711, 18, 37, 556, 5, 555, 7, 565, 1, 0,
                        0
                }, // water wave
                {
                        1188, 70, 14222, 2715, 2721, 2726, 19, 40, 556, 5, 557, 7, 565, 1, 0,
                        0
                }, // earth wave
                {
                        1189, 75, 14223, 2728, 2735, 2740, 20, 42, 556, 5, 554, 7, 565, 1, 0,
                        0
                }, // fire wave
                {
                        1153, 3, 716, 102, 103, 104, 0, 13, 555, 3, 557, 2, 559, 1, 0, 0
                }, // confuse
                {
                        1157, 11, 716, 105, 106, 107, 0, 20, 555, 3, 557, 2, 559, 1, 0, 0
                }, // weaken
                {
                        1161, 19, 716, 108, 109, 110, 0, 29, 555, 2, 557, 3, 559, 1, 0, 0
                }, // curse
                {
                        1542, 66, 729, 167, 168, 169, 0, 76, 557, 5, 555, 5, 566, 1, 0, 0
                }, // vulnerability
                {
                        1543, 73, 729, 170, 171, 172, 0, 83, 557, 8, 555, 8, 566, 1, 0, 0
                }, // enfeeble
                {
                        1562, 80, 729, 173, 174, 107, 0, 90, 557, 12, 555, 12, 556, 1, 0,
                        0
                }, // stun
                {
                        1572, 20, 711, 177, 178, 181, 0, 30, 557, 3, 555, 3, 561, 2, 0, 0
                }, // bind
                {
                        1582, 50, 711, 177, 178, 180, 2, 60, 557, 4, 555, 4, 561, 3, 0, 0
                }, // snare
                {
                        1592, 79, 711, 177, 178, 179, 4, 90, 557, 5, 555, 5, 561, 4, 0, 0
                }, // entangle
                {
                        1171, 39, 724, 145, 146, 147, 15, 25, 556, 2, 557, 2, 562, 1, 0,
                        0
                }, // crumble undead
                {
                        1539, 50, 708, 87, 88, 89, 25, 42, 554, 5, 560, 1, 0, 0, 0, 0
                }, // iban
                // blast
                {
                        12037, 50, 1576, 327, 328, 329, 19, 30, 560, 1, 558, 4, 0, 0, 0,
                        0
                }, // magic dart
                {
                        1190, 60, 811, 0, 0, 76, 20, 60, 554, 2, 565, 2, 556, 4, 0, 0
                }, // sara
                // strike
                {
                        1191, 60, 811, 0, 0, 77, 20, 60, 554, 1, 565, 2, 556, 4, 0, 0
                }, // cause
                // of
                // guthix
                {
                        1192, 60, 811, 0, 0, 78, 20, 60, 554, 4, 565, 2, 556, 1, 0, 0
                }, // flames
                // of
                // zammy
                {
                        12445, 85, 1819, 1841, 1842, 1843, 0, 65, 563, 1, 562, 1, 560, 1,
                        0, 0
                }, // teleblock
                // Ancient Spells
                {
                        12939, 50, 1978, 0, 384, 385, 13, 30, 560, 2, 562, 2, 554, 1,
                        556, 1
                }, // smoke rush
                {
                        12987, 52, 1978, 0, 378, 379, 14, 31, 560, 2, 562, 2, 566, 1,
                        556, 1
                }, // shadow rush
                {
                        12901, 56, 1978, 0, 0, 373, 15, 33, 560, 2, 562, 2, 565, 1, 0, 0
                }, // blood
                // rush
                {
                        12861, 58, 1978, 0, 360, 361, 16, 34, 560, 2, 562, 2, 555, 2, 0,
                        0
                }, // ice rush
                {
                        12963, 62, 1979, 0, 0, 389, 19, 36, 560, 2, 562, 4, 556, 2, 554,
                        2
                }, // smoke burst
                {
                        13011, 64, 1979, 0, 0, 382, 20, 37, 560, 2, 562, 4, 556, 2, 566,
                        2
                }, // shadow burst
                {
                        12919, 68, 1979, 0, 0, 376, 21, 39, 560, 2, 562, 4, 565, 2, 0, 0
                }, // blood
                // burst
                {
                        12881, 70, 1979, 0, 0, 363, 22, 40, 560, 2, 562, 4, 555, 4, 0, 0
                }, // ice
                // burst
                {
                        12951, 74, 1978, 0, 386, 387, 23, 42, 560, 2, 554, 2, 565, 2,
                        556, 2
                }, // smoke blitz
                {
                        12999, 76, 1978, 0, 380, 381, 24, 43, 560, 2, 565, 2, 556, 2,
                        566, 2
                }, // shadow blitz
                {
                        12911, 80, 1978, 0, 374, 375, 25, 45, 560, 2, 565, 4, 0, 0, 0, 0
                }, // blood
                // blitz
                {
                        12871, 82, 1978, 366, 0, 367, 26, 46, 560, 2, 565, 2, 555, 3, 0,
                        0
                }, // ice blitz
                {
                        12975, 86, 1979, 0, 0, 391, 27, 48, 560, 4, 565, 2, 556, 4, 554,
                        4
                }, // smoke barrage
                {
                        13023, 88, 1979, 0, 0, 383, 28, 49, 560, 4, 565, 2, 556, 4, 566,
                        3
                }, // shadow barrage
                {
                        12929, 92, 1979, 0, 0, 377, 29, 51, 560, 4, 565, 4, 566, 1, 0, 0
                }, // blood
                // barrage
                {
                        12891, 94, 1979, 0, 0, 369, 30, 52, 560, 4, 565, 2, 555, 6, 0, 0
                }, // ice
                // barrage
                {
                        -1, 80, 811, 301, 0, 0, 0, 0, 554, 3, 565, 3, 556, 3, 0, 0
                }, // charge
                {
                        -1, 21, 712, 112, 0, 0, 0, 10, 554, 3, 561, 1, 0, 0, 0, 0
                }, // low
                // alch
                {
                        -1, 55, 713, 113, 0, 0, 0, 20, 554, 5, 561, 1, 0, 0, 0, 0
                }, // high
                // alch
                {
                        -1, 33, 728, 142, 143, 144, 0, 35, 556, 1, 563, 1, 0, 0, 0, 0
                },
				{
						21744, 61, 10513, 1845, 0, 1847, 160, 300, 557, 1, 566, 1, 562, 2, 0, 0
				}, //miasmic rush
				{
						22168, 73, 10524, 1850, 0, 1851, 220, 300, 557, 2, 566, 2, 562, 4, 0, 0
				},//miasmic blitz
				{
						21745, 85, 10516, 1848, 0, 1849, 260, 300, 557, 3, 566, 3, 565, 2, 0, 0
				}, //miasmic burst
				{
						21746, 97, 10518, 1853, 0, 1854, 370, 300, 557, 4, 566, 4, 565, 4, 0, 0
				} //miasmic barrage
				// example {magicId, level req, animation, startGFX, projectile Id,
				// endGFX, maxhit, exp gained, rune 1, rune 1 amount, rune 2, rune 2
				// amount, rune 3, rune 3 amount, rune 4, rune 4 amount}
        };

        public boolean isAutoButton(int button)
        {
                for (int j = 0; j < autocastIds.length; j += 2)
                {
                        if (autocastIds[j] == button)
                                return true;
                }
                return false;
        }

        public int[] autocastIds = {
                51133, 32, 51185, 33, 51091, 34, 24018, 35,
                51159, 36, 51211, 37, 51111, 38, 51069, 39, 51146, 40, 51198, 41,
                51102, 42, 51058, 43, 51172, 44, 51224, 45, 51122, 46, 51080, 47,
                7038, 0, 7039, 1, 7040, 2, 7041, 3, 7042, 4, 7043, 5, 7044, 6,
                7045, 7, 7046, 8, 7047, 9, 7048, 10, 7049, 11, 7050, 12, 7051, 13,
                7052, 14, 7053, 15, 47019, 27, 47020, 25, 47021, 12, 47022, 13,
                47023, 14, 47024, 15
        };

        public void assignAutocast(int button)
        {
                for (int j = 0; j < autocastIds.length; j++)
                {
                        if (autocastIds[j] == button)
                        {
                                Client c = (Client) PlayerHandler.players[this.playerId];
                                autocasting = true;
                                autocastId = autocastIds[j + 1];
                                c.getPA().sendFrame36(108, 1);
                                c.setSidebarInterface(0, 328);
                                c = null;
                                break;
                        }
                }
        }
        
        public int calculateCombatLevel()
        {
                int j = getLevelForXP(playerXP[Constants.ATTACK]);
                int k = getLevelForXP(playerXP[Constants.DEFENCE]);
                int l = getLevelForXP(playerXP[Constants.STRENGTH]);
                int i1 = getLevelForXP(playerXP[Constants.HITPOINTS]);
                int j1 = getLevelForXP(playerXP[Constants.PRAYER]);
                int k1 = getLevelForXP(playerXP[Constants.RANGED]);
                int l1 = getLevelForXP(playerXP[Constants.MAGIC]);
                int combatLevel = (int)(((k + i1) + Math.floor(j1 / 2)) * 0.25D) + 1;
                double d = (j + l) * 0.32500000000000001D;
                double d1 = Math.floor(k1 * 1.5D) * 0.32500000000000001D;
                double d2 = Math.floor(l1 * 1.5D) * 0.32500000000000001D;
                if (d >= d1 && d >= d2)
                {
                        combatLevel += d;
                }
                else if (d1 >= d && d1 >= d2)
                {
                        combatLevel += d1;
                }
                else if (d2 >= d && d2 >= d1)
                {
                        combatLevel += d2;
                }
                return combatLevel;
        }

        public void handleHitMask(int damage)
        {
                if (!hitUpdateRequired)
                {
                        hitUpdateRequired = true;
                        hitDiff = damage;
                }
                else if (!hitUpdateRequired2)
                {
                        hitUpdateRequired2 = true;
                        hitDiff2 = damage;
                }
                updateRequired = true;
        }

        public boolean fullVoidRange()
        {
                return playerEquipment[Constants.HEAD_SLOT] == 11664 && playerEquipment[Constants.LEG_SLOT] == 8840 && playerEquipment[Constants.TORSO_SLOT] == 8839 && playerEquipment[Constants.HAND_SLOT] == 8842;
        }

        public boolean fullVoidMage()
        {
                return playerEquipment[Constants.HEAD_SLOT] == 11663 && playerEquipment[Constants.LEG_SLOT] == 8840 && playerEquipment[Constants.TORSO_SLOT] == 8839 && playerEquipment[Constants.HAND_SLOT] == 8842;
        }

        public boolean fullVoidMelee()
        {
                return playerEquipment[Constants.HEAD_SLOT] == 11665 && playerEquipment[Constants.LEG_SLOT] == 8840 && playerEquipment[Constants.TORSO_SLOT] == 8839 && playerEquipment[Constants.HAND_SLOT] == 8842;
        }

        public final int[] REDUCE_SPELL_TIME = {
                250000, 250000, 250000, 500000,
                500000, 500000
        }; // how long does the other player stay immune to
        // the spell
        public long[] reduceSpellDelay = new long[6];
        public final int[] REDUCE_SPELLS = {
                1153, 1157, 1161, 1542, 1543, 1562
        };
        public boolean[] canUseReducingSpell = {
                true, true, true, true, true, true
        };

        /* Prayer */
        public long stopPrayerDelay;
        public boolean usingPrayer;
        public final int[] PRAYER_LEVEL_REQUIRED = {
                1, 4, 7, 8, 9, 10, 13, 16, 19,
                22, 25, 26, 27, 28, 31, 34, 37, 40, 43, 44, 45, 46, 49, 52, 60, 70
        };
        public final int[] PRAYER = {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
                14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25
        };
        public final String[] PRAYER_NAME = {
                "Thick Skin", "Burst of Strength",
                "Clarity of Thought", "Sharp Eye", "Mystic Will", "Rock Skin",
                "Superhuman Strength", "Improved Reflexes", "Rapid Restore",
                "Rapid Heal", "Protect Item", "Hawk Eye", "Mystic Lore",
                "Steel Skin", "Ultimate Strength", "Incredible Reflexes",
                "Protect from Magic", "Protect from Missiles",
                "Protect from Melee", "Eagle Eye", "Mystic Might", "Retribution",
                "Redemption", "Smite", "Chivalry", "Piety"
        };
        public final int[] PRAYER_GLOW = {
                83, 84, 85, 601, 602, 86, 87, 88, 89,
                90, 91, 603, 604, 92, 93, 94, 95, 96, 97, 605, 606, 98, 99, 100,
                607, 608
        };
        public final int[] PRAYER_HEAD_ICONS = {
                -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, 1, 0, -1, -1, 3, 5, 4, -1, -1
        };
        public boolean[] prayerActive = {
                false, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false,
                false, false
        };

        public final int[] DUEL_RULE_ID = {
                1, 2, 16, 32, 64, 128, 256, 512, 1024,
                4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 2097152,
                8388608, 16777216, 67108864, 134217728
        };
        
        public boolean combatType(int type)
        {
                return fightMode == type;
        }

        public boolean inSafePkArea() {
                if (absX >= 3174 && absX <= 3190 && absY >= 3403 && absY <= 3432 ||
                		FightPits.inArena(this) ||
                		(inCastleWars && CastleWars.inCastleWars(this))) {
                        return true;
                }
                return false;
        }

        /**
         * True if the player is in the Wilderness.
         */
        public boolean inWilderness()
        {
                if (inSafePkArea() || false)
                {
                        return true;
                }
                if (absX > 2941 && absX < 3392 && absY > 3519 && absY < 3966 || absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366)
                {
                        return true;
                }
                return false;
        }

		public boolean inConstruction() {
			if ((absX >= 16 && absX <= 55 && absY >= 16 && absY <= 55)) {
				return true;
		}
			return false;
		}

        /**
         * Used to check if the player is in a PVP area.
         *
         * @return The current state.
         */
        public boolean inPVPArea()
        {
                if (inSafePkArea() || false || inWilderness())
                {
                        return true;
                }
                return false;
        }

        public boolean arenas()
        {
                if (absX > 3331 && absX < 3391 && absY > 3242 && absY < 3260)
                {
                        return true;
                }
                return false;
        }

        public boolean inDuelArena()
        {
                if ((absX > 3322 && absX < 3394 && absY > 3195 && absY < 3291) || (absX > 3311 && absX < 3323 && absY > 3223 && absY < 3248))
                {
                        return true;
                }
                return false;
        }

        public boolean inMulti() {
        	return MultiWay.inMulti(this.getX(), this.getY());
        }

        public boolean coordsCheck(int X1, int X2, int Y1, int Y2)
        {
                return absX >= X1 && absX <= X2 && absY >= Y1 && absY <= Y2;
        }

        public Stream outStream = null;

        public synchronized Stream getOutStream()
        {
                return outStream;
        }

        /**
         * Send a message to the chatbox.
         * @param message
         * 			The message to send.
         */
        public void sendMessage(String message)
        {
                synchronized(this)
                {
                        if (getOutStream() != null)
                        {
                                outStream.createFrameVarSize(253);
                                outStream.writeString(message);
                                outStream.endFrameVarSize();
                        }
                }
        }

        /**
         * Announce a message to all players.
         *
         * @param message
         *            The message to announce to all players.
         */
        public void announce(String message)
        {
                for (int j = 0; j < PlayerHandler.players.length; j++)
                {
                        if (PlayerHandler.players[j] != null)
                        {
                                Client c3 = (Client) PlayerHandler.players[j];
                                c3.sendMessage(message);
                        }
                }
        }

        public String connectedFrom = "";
        
        /**
         * The player's unique identity number.
         */
        public int playerId = -1;
        
        public String playerName = null;
        public String playerName2 = null;
        public String playerPass = null;
        public int playerRights;
		public boolean ironMan;
        public boolean expLock;
        public PlayerHandler handler = null;
        public int playerItems[] = new int[28];
        public int playerItemsN[] = new int[28];
        public int playerItemsC[] = new int[28];
        public int playerStandIndex = 0x328;
        public int playerTurnIndex = 0x337;
        public int playerWalkIndex = 0x333;
        public int playerTurn180Index = 0x334;
        public int playerTurn90CWIndex = 0x335;
        public int playerTurn90CCWIndex = 0x336;
        public int playerRunIndex = 0x338;
        public int[] playerEquipment = new int[14];
        public int[] playerEquipmentN = new int[14];
        public int[] playerEquipmentC = new int[14];

        public Player(int _playerId)
        {
                playerId = _playerId;
                playerRights = 0;
                for (int i = 0; i < playerItems.length; i++)
                {
                        playerItems[i] = 0;
                }
                for (int i = 0; i < playerItemsN.length; i++)
                {
                        playerItemsN[i] = 0;
                }
                for (int i = 0; i < skillLevel.length; i++)
                {
                        if (i == 3)
                        {
                                skillLevel[i] = 10;
                        }
                        else
                        {
                                skillLevel[i] = 1;
                        }
                }
                for (int i = 0; i < playerXP.length; i++)
                {
                        if (i == 3)
                        {
                                playerXP[i] = 1300;
                        }
                        else
                        {
                                playerXP[i] = 0;
                        }
                }
                playerAppearance[0] = 0; // gender
                playerAppearance[1] = 0; // head
                playerAppearance[2] = 18; // Torso
                playerAppearance[3] = 26; // arms
                playerAppearance[4] = 33; // hands
                playerAppearance[5] = 36; // legs
                playerAppearance[6] = 42; // feet
                playerAppearance[7] = 10; // beard
                playerAppearance[8] = 0; // hair colour
                playerAppearance[9] = 0; // torso colour
                playerAppearance[10] = 0; // legs colour
                playerAppearance[11] = 0; // feet colour
                playerAppearance[12] = 0; // skin colour
                actionID = 0;
                playerEquipment[Constants.HEAD_SLOT] = -1;
                playerEquipment[Constants.CAPE_SLOT] = -1;
                playerEquipment[Constants.AMULET_SLOT] = -1;
                playerEquipment[Constants.TORSO_SLOT] = -1;
                playerEquipment[Constants.SHIELD_SLOT] = -1;
                playerEquipment[Constants.LEG_SLOT] = -1;
                playerEquipment[Constants.HAND_SLOT] = -1;
                playerEquipment[Constants.FEET_SLOT] = -1;
                playerEquipment[Constants.RING_SLOT] = -1;
                playerEquipment[Constants.ARROW_SLOT] = -1;
                playerEquipment[Constants.WEAPON_SLOT] = -1;
                heightLevel = 0;
                teleportToX = 3087; // Starter co-ord for new players
                teleportToY = 3493;
                absX = absY = -1;
                mapRegionX = mapRegionY = -1;
                currentX = currentY = 0;
                Movement.resetWalkingQueue(this);
        }

        public static final int maxPlayerListSize = Configuration.MAX_PLAYERS;
        public Player playerList[] = new Player[maxPlayerListSize];
        public int playerListSize = 0;
        public byte playerInListBitmap[] = new byte[(Configuration.MAX_PLAYERS + 7) >> 3];
        public static final int maxNPCListSize = NPCHandler.maxNPCs;
		public int pcPoints = 0;
        public NPC npcList[] = new NPC[maxNPCListSize];
        public int npcListSize = 0;
        public byte npcInListBitmap[] = new byte[(NPCHandler.maxNPCs + 7) >> 3];

        public boolean withinDistance(Player otherPlr)
        {
                if (heightLevel != otherPlr.heightLevel)
                        return false;
                int deltaX = otherPlr.absX - absX, deltaY = otherPlr.absY - absY;
                return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
        }

        public boolean withinDistance(NPC npc)
        {
                if (npc == null || npc.needRespawn || heightLevel != npc.heightLevel)
                        return false;
                int deltaX = npc.absX - absX, deltaY = npc.absY - absY;
                return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
        }

        public int distanceToPoint(int pointX, int pointY)
        {
                return (int) Math.sqrt(Math.pow(absX - pointX, 2) + Math.pow(absY - pointY, 2));
        }

        public int mapRegionX, mapRegionY;
        public int absX, absY;
        public int currentX, currentY;
        public int heightLevel;
        public int playerSE = 0x328;
        public int playerSEW = 0x333;
        public int playerSER = 0x334;
        public boolean updateRequired = true;
        public final int walkingQueueSize = 50;
        public int walkingQueueX[] = new int[walkingQueueSize],
                walkingQueueY[] = new int[walkingQueueSize];
        public int wQueueReadPtr = 0;
        public int wQueueWritePtr = 0;
        public boolean isRunning = true;
        public int teleportToX = -1, teleportToY = -1;

        public boolean goodDistance(int objectX, int objectY, int playerX,
                int playerY, int distance)
        {
                for (int i = 0; i <= distance; i++)
                {
                        for (int j = 0; j <= distance; j++)
                        {
                                if ((objectX + i) == playerX && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY))
                                {
                                        return true;
                                }
                                else if ((objectX - i) == playerX && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY))
                                {
                                        return true;
                                }
                                else if (objectX == playerX && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY))
                                {
                                        return true;
                                }
                        }
                }
                return false;
        }

        public int getNextWalkingDirection() {
                if (wQueueReadPtr == wQueueWritePtr)
                        return -1;
                int dir;
                do {
                        dir = Misc.direction(currentX, currentY,
                                walkingQueueX[wQueueReadPtr], walkingQueueY[wQueueReadPtr]);
                        if (dir == -1) {
                                wQueueReadPtr = (wQueueReadPtr + 1) % walkingQueueSize;
                        }
                        else if ((dir & 1) != 0) {
                                // println_debug("Invalid waypoint in walking queue!");
                        	Movement.resetWalkingQueue(this);
                                return -1;
                        }
                } while ((dir == -1) && (wQueueReadPtr != wQueueWritePtr));
                if (dir == -1)
                        return -1;
                dir >>= 1;
                currentX += Misc.directionDeltaX[dir];
                currentY += Misc.directionDeltaY[dir];
                absX += Misc.directionDeltaX[dir];
                absY += Misc.directionDeltaY[dir];
				getPA().agilityDrain();
                return dir;
        }

        public boolean didTeleport;
        public boolean mapRegionDidChange;
        public int dir1 = -1, dir2 = -1;
        public boolean createItems;
        public int poimiX = 0, poimiY = 0;

        public void updateThisPlayerMovement(Stream str)
        {
                synchronized(this)
                {
                        if (mapRegionDidChange)
                        {
                                str.createFrame(73);
                                str.writeWordA(mapRegionX + 6);
                                str.writeWord(mapRegionY + 6);
                        }
                        if (didTeleport)
                        {
                                str.createFrameVarSizeWord(81);
                                str.initBitAccess();
                                str.writeBits(1, 1);
                                str.writeBits(2, 3);
                                str.writeBits(2, heightLevel);
                                str.writeBits(1, 1);
                                str.writeBits(1, (updateRequired) ? 1 : 0);
                                str.writeBits(7, currentY);
                                str.writeBits(7, currentX);
                                return;
                        }
                        if (dir1 == -1)
                        {
                                // don't have to update the character position, because we're
                                // just standing
                                str.createFrameVarSizeWord(81);
                                str.initBitAccess();
                                isMoving = false;
                                if (updateRequired)
                                {
                                        // tell client there's an update block appended at the end
                                        str.writeBits(1, 1);
                                        str.writeBits(2, 0);
                                }
                                else
                                {
                                        str.writeBits(1, 0);
                                }
                                if (DirectionCount < 50)
                                {
                                        DirectionCount++;
                                }
                        }
                        else
                        {
                                DirectionCount = 0;
                                str.createFrameVarSizeWord(81);
                                str.initBitAccess();
                                str.writeBits(1, 1);
                                if (dir2 == -1)
                                {
                                        isMoving = true;
                                        str.writeBits(2, 1);
                                        str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
                                        if (updateRequired)
                                                str.writeBits(1, 1);
                                        else
                                                str.writeBits(1, 0);
                                }
                                else
                                {
                                        isMoving = true;
                                        str.writeBits(2, 2);
                                        str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
                                        str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
                                        if (updateRequired)
                                                str.writeBits(1, 1);
                                        else
                                                str.writeBits(1, 0);
                                }
                        }
                }
        }

        public void updatePlayerMovement(Stream str)
        {
                synchronized(this)
                {
                        if (dir1 == -1)
                        {
                                if (updateRequired || isChatTextUpdateRequired())
                                {
                                        str.writeBits(1, 1);
                                        str.writeBits(2, 0);
                                }
                                else
                                        str.writeBits(1, 0);
                        }
                        else if (dir2 == -1)
                        {
                                str.writeBits(1, 1);
                                str.writeBits(2, 1);
                                str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
                                str.writeBits(1, (updateRequired || isChatTextUpdateRequired()) ? 1 : 0);
                        }
                        else
                        {
                                str.writeBits(1, 1);
                                str.writeBits(2, 2);
                                str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
                                str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
                                str.writeBits(1, (updateRequired || isChatTextUpdateRequired()) ? 1 : 0);
                        }
                }
        }

        public void addNewNPC(NPC npc, Stream str, Stream updateBlock)
        {
                synchronized(this)
                {
                        int id = npc.npcId;
                        npcInListBitmap[id >> 3] |= 1 << (id & 7);
                        npcList[npcListSize++] = npc;
                        str.writeBits(14, id);
                        int z = npc.absY - absY;
                        if (z < 0)
                                z += 32;
                        str.writeBits(5, z);
                        z = npc.absX - absX;
                        if (z < 0)
                                z += 32;
                        str.writeBits(5, z);
                        str.writeBits(1, 0);
                        str.writeBits(18, npc.npcType);
                        str.writeBits(1, npc.isFamiliar == false ? 0 : 1);
                        str.writeBits(1, npc.owner == this ? 1 : 0);
                        str.writeBits(5, npc.defaultRotation.rotationId);
                        boolean savedUpdateRequired = npc.updateRequired;
                        npc.updateRequired = true;
                        npc.appendNPCUpdateBlock(updateBlock, (Client) this);
                        npc.updateRequired = savedUpdateRequired;
                        str.writeBits(1, 1);
                }
        }

        public void addNewPlayer(Player plr, Stream str, Stream updateBlock)
        {
                synchronized(this)
                {
                        if (playerListSize >= 255)
                        {
                                return;
                        }
                        int id = plr.playerId;
                        playerInListBitmap[id >> 3] |= 1 << (id & 7);
                        playerList[playerListSize++] = plr;
                        str.writeBits(11, id);
                        str.writeBits(1, 1);
                        boolean savedFlag = plr.isAppearanceUpdateRequired();
                        boolean savedUpdateRequired = plr.updateRequired;
                        plr.setAppearanceUpdateRequired(true);
                        plr.updateRequired = true;
                        plr.appendPlayerUpdateBlock(updateBlock);
                        plr.setAppearanceUpdateRequired(savedFlag);
                        plr.updateRequired = savedUpdateRequired;
                        str.writeBits(1, 1);
                        int z = plr.absY - absY;
                        if (z < 0)
                                z += 32;
                        str.writeBits(5, z);
                        z = plr.absX - absX;
                        if (z < 0)
                                z += 32;
                        str.writeBits(5, z);
                }
        }

        /**
         * The timer used for Completionist cape emote.
         */
        private int dungTime = 16;

        /**
         * Perform the Completionist cape emote.
         */
        public void completionistCapeEmote()
        {

                CycleEventHandler.getSingleton().addEvent(this, new CycleEvent()
                {@
                        Override
                        public void execute(CycleEventContainer container)
                        {
                                if (dungTime == 16)
                                {
                                        gfx0(2442);
                                        startAnimation(13190);
                                }
                                else if (dungTime == 15)
                                {
                                        npcId2 = 11228;
                                        isNpc = true;
                                        updateRequired = true;
                                        appearanceUpdateRequired = true;
                                        startAnimation(13192);
                                }
                                else if (dungTime == 10)
                                {
                                        npcId2 = 11227;
                                        isNpc = true;
                                        updateRequired = true;
                                        appearanceUpdateRequired = true;
                                        startAnimation(13193);
                                }
                                else if (dungTime == 6)
                                {
                                        gfx0(2442);
                                }
                                else if (dungTime == 5)
                                {
                                        npcId2 = 11229;
                                        updateRequired = true;
                                        appearanceUpdateRequired = true;
                                        startAnimation(13194);
                                }
                                if (dungTime == 0)
                                {
                                        npcId2 = -1;
                                        updateRequired = true;
                                        appearanceUpdateRequired = true;
                                        container.stop();
                                }
                                if (dungTime > 0)
                                {
                                        dungTime--;
                                }
                        }

                        @
                        Override
                        public void stop()
                        {
                                dungTime = 16;
                        }
                }, 1);

        }

        public int DirectionCount = 0;
        public boolean appearanceUpdateRequired = true;
        public boolean appearanceUpdateRequired2 = true;
        public boolean isNpc;
        protected int hitDiff2;
        private int hitDiff = 0;
        protected boolean hitUpdateRequired2;
        private boolean hitUpdateRequired;
        protected static Stream playerProps;
        static
        {
                playerProps = new Stream(new byte[100]);
        }

        protected void appendPlayerAppearance(Stream str)
        {
                synchronized(this)
                {
                        playerProps.currentOffset = 0;
                        playerProps.writeByte(playerAppearance[0]);
                        playerProps.writeByte(headIcon);
                        playerProps.writeByte(headIconPk);
                        for (int i = 0; i < compColor.length; i++) {
                        	playerProps.writeWord(compColor[i]);
                        }
                        playerProps.writeWord(weenColor);
                        playerProps.writeWord(phatColor);
                        if (npcId2 <= 0)
                        {
                                if (playerEquipment[Constants.HEAD_SLOT] > 1)
                                {
                                        playerProps.writeWord(0x200 + playerEquipment[Constants.HEAD_SLOT]);
                                }
                                else
                                {
                                        playerProps.writeByte(0);
                                }
                                if (playerEquipment[Constants.CAPE_SLOT] > 1)
                                {
                                        playerProps.writeWord(0x200 + playerEquipment[Constants.CAPE_SLOT]);
                                }
                                else
                                {
                                        playerProps.writeByte(0);
                                }
                                if (playerEquipment[Constants.AMULET_SLOT] > 1)
                                {
                                        playerProps.writeWord(0x200 + playerEquipment[Constants.AMULET_SLOT]);
                                }
                                else
                                {
                                        playerProps.writeByte(0);
                                }
                                if (playerEquipment[Constants.WEAPON_SLOT] > 1)
                                {
                                        playerProps.writeWord(0x200 + playerEquipment[Constants.WEAPON_SLOT]);
                                }
                                else
                                {
                                        playerProps.writeByte(0);
                                }
                                if (playerEquipment[Constants.TORSO_SLOT] > 1)
                                {
                                        playerProps.writeWord(0x200 + playerEquipment[Constants.TORSO_SLOT]);
                                }
                                else
                                {
                                        playerProps.writeWord(0x100 + playerAppearance[2]);
                                }
                                if (playerEquipment[Constants.SHIELD_SLOT] > 1)
                                {
                                        playerProps.writeWord(0x200 + playerEquipment[Constants.SHIELD_SLOT]);
                                }
                                else
                                {
                                        playerProps.writeByte(0);
                                }
                                if (!Item.isFullBody(playerEquipment[Constants.TORSO_SLOT]))
                                {
                                        playerProps.writeWord(0x100 + playerAppearance[3]);
                                }
                                else
                                {
                                        playerProps.writeByte(0);
                                }
                                if (playerEquipment[Constants.LEG_SLOT] > 1)
                                {
                                        playerProps.writeWord(0x200 + playerEquipment[Constants.LEG_SLOT]);
                                }
                                else
                                {
                                        playerProps.writeWord(0x100 + playerAppearance[5]);
                                }
                                if (!Item.isNormalHelm(playerEquipment[Constants.HEAD_SLOT]) && !Item.isFullMask(playerEquipment[Constants.HEAD_SLOT]))
                                {
                                        playerProps.writeWord(0x100 + playerAppearance[1]);
                                }
                                else
                                {
                                        playerProps.writeByte(0);
                                }
                                if (playerEquipment[Constants.HAND_SLOT] > 1)
                                {
                                        playerProps.writeWord(0x200 + playerEquipment[Constants.HAND_SLOT]);
                                }
                                else
                                {
                                        playerProps.writeWord(0x100 + playerAppearance[4]);
                                }
                                if (playerEquipment[Constants.FEET_SLOT] > 1)
                                {
                                        playerProps.writeWord(0x200 + playerEquipment[Constants.FEET_SLOT]);
                                }
                                else
                                {
                                        playerProps.writeWord(0x100 + playerAppearance[6]);
                                }
                                if (playerAppearance[0] != 1 && !Item.isFullMask(playerEquipment[Constants.HEAD_SLOT]))
                                {
                                        playerProps.writeWord(0x100 + playerAppearance[7]);
                                }
                                else
                                {
                                        playerProps.writeByte(0);
                                }
                        }
                        else
                        {
                                playerProps.writeWord(-1);
                                playerProps.writeWord(npcId2);
                        }
                        playerProps.writeByte(playerAppearance[8]);
                        playerProps.writeByte(playerAppearance[9]);
                        playerProps.writeByte(playerAppearance[10]);
                        playerProps.writeByte(playerAppearance[11]);
                        playerProps.writeByte(playerAppearance[12]);
                        playerProps.writeWord(playerStandIndex); // standAnimIndex
                        playerProps.writeWord(playerTurnIndex); // standTurnAnimIndex
                        playerProps.writeWord(playerWalkIndex); // walkAnimIndex
                        playerProps.writeWord(playerTurn180Index); // turn180AnimIndex
                        playerProps.writeWord(playerTurn90CWIndex); // turn90CWAnimIndex
                        playerProps.writeWord(playerTurn90CCWIndex); // turn90CCWAnimIndex
                        playerProps.writeWord(playerRunIndex); // runAnimIndex
                        playerProps.writeWord(skillLevel[3]);
                        playerProps.writeWord(maximumHitPoints());
                        playerProps.writeQWord(Misc.playerNameToInt64(playerName));
                        int mag = (int)((getLevelForXP(playerXP[6])) * 1.5);
                        int ran = (int)((getLevelForXP(playerXP[4])) * 1.5);
                        int attstr = (int)((double)(getLevelForXP(playerXP[0])) + (double)(getLevelForXP(playerXP[2])));
                        combatLevel = 0;
                        if (ran > attstr)
                        {
                                combatLevel = (int)(((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25) + ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[4])) * 0.4875) + ((getLevelForXP(playerXP[22])) * 0.125));
                        }
                        else if (mag > attstr)
                        {
                                combatLevel = (int)(((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25) + ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[6])) * 0.4875) + ((getLevelForXP(playerXP[22])) * 0.125));
                        }
                        else
                        {
                                combatLevel = (int)(((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25) + ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[0])) * 0.325) + ((getLevelForXP(playerXP[2])) * 0.325) + ((getLevelForXP(playerXP[22])) * 0.125));
                        }
                        playerProps.writeByte(combatLevel); // combat level
                        playerProps.writeWord(title);
                        str.writeByteC(playerProps.currentOffset);
                        str.writeBytes(playerProps.buffer, playerProps.currentOffset, 0);
                }
        }

        public int getLevelForXP(int exp)
        {
                int points = 0;
                int output = 0;
                for (int lvl = 1; lvl <= 99; lvl++)
                {
                        points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
                        output = (int) Math.floor(points / 4);
                        if (output >= exp)
                                return lvl;
                }
                return 99;
        }

        private boolean chatTextUpdateRequired;
        private byte chatText[] = new byte[4096];
        private byte chatTextSize = 0;
        private int chatTextColor = 0;
        private int chatTextEffects = 0;

        protected void appendPlayerChatText(Stream str)
        {
                synchronized(this)
                {
                        str.writeWordBigEndian(((getChatTextColor() & 0xFF) << 8) + (getChatTextEffects() & 0xFF));
                        str.writeByte(playerRights);
                        str.writeByteC(getChatTextSize());
                        str.writeBytes_reverse(getChatText(), getChatTextSize(), 0);
                }
        }

        public void forcedChat(String text)
        {
                forcedText = text;
                forcedChatUpdateRequired = true;
                updateRequired = true;
                setAppearanceUpdateRequired(true);
        }

        public String forcedText = "null";

        public void appendForcedChat(Stream str)
        {
                synchronized(this)
                {
                        str.writeString(forcedText);
                }
        }

        /**
         * Graphics
         **/
        public int mask100var1 = 0;
        public int mask100var2 = 0;
        protected boolean mask100update;

        public void appendMask100Update(Stream str)
        {
                synchronized(this)
                {
                        str.writeWordBigEndian(mask100var1);
                        str.writeDWord(mask100var2);
                }
        }

        public void gfx100(int gfx)
        {
                mask100var1 = gfx;
                mask100var2 = 6553600;
                mask100update = true;
                updateRequired = true;
        }

        public void gfx0(int gfx)
        {
                mask100var1 = gfx;
                mask100var2 = 65536;
                mask100update = true;
                updateRequired = true;
        }

        public void gfx(int gfx, int height)
        {
                mask100var1 = gfx;
                mask100var2 = 65536 * height;
                mask100update = true;
                updateRequired = true;
        }

        /**
         * Perform an animation.
         *
         * @param animId
         *            The animation identity number.
         */
        public void startAnimation(int animId)
        {
                animationRequest = animId;
                animationWaitCycles = 0;
                updateRequired = true;
        }

        public void appendAnimationRequest(Stream str)
        {
                synchronized(this)
                {
                        str.writeWordBigEndian((animationRequest == -1) ? 65535 : animationRequest);
                        str.writeByteC(animationWaitCycles);
                }
        }

        /**
         * Face Update
         **/
        protected boolean faceUpdateRequired;
        public int face = -1;
        public int FocusPointX = -1, FocusPointY = -1;

        public void faceUpdate(int index)
        {
                face = index;
                faceUpdateRequired = true;
                updateRequired = true;
        }

        public void appendFaceUpdate(Stream str)
        {
                synchronized(this)
                {
                        str.writeWordBigEndian(face);
                }
        }

        public void turnPlayerTo(int pointX, int pointY)
        {
                FocusPointX = 2 * pointX + 1;
                FocusPointY = 2 * pointY + 1;
                updateRequired = true;
        }

        private void appendSetFocusDestination(Stream str)
        {
                synchronized(this)
                {
                        str.writeWordBigEndianA(FocusPointX);
                        str.writeWordBigEndian(FocusPointY);
                }
        }

        /**
         * Hit Update
         **/
        public int soakDamage, soakDamage2 = 0;

        protected void appendHitUpdate(Stream str)
        {
                //maximumHitPoints = getLevelForXP(playerXP[3]);
                synchronized(this)
                {
                        str.writeWordA(getHitDiff());
                        str.writeByte(hitMask);
                        str.writeByte(hitIcon);
                        str.writeWordA(soakDamage);
                        str.writeWordA(skillLevel[3]);
                        str.writeWordA(maximumHitPoints());
                }
        }

        protected void appendHitUpdate2(Stream str)
        {
                //maximumHitPoints = getLevelForXP(playerXP[3]);
                synchronized(this)
                {
                        str.writeWordA(hitDiff2);
                        str.writeByte(hitMask2);
                        str.writeByte(hitIcon2);
                        str.writeWordA(soakDamage2);
                        str.writeWordA(skillLevel[3]);
                        str.writeWordA(maximumHitPoints());
                }
        }

        public void appendPlayerUpdateBlock(Stream str)
        {
                //maximumHitPoints = getLevelForXP(playerXP[3]);
                synchronized(this)
                {
                        if (!updateRequired && !isChatTextUpdateRequired())
                                return; // nothing required
                        int updateMask = 0;
                        if (mask100update)
                        {
                                updateMask |= 0x100;
                        }
                        if (animationRequest != -1)
                        {
                                updateMask |= 8;
                        }
                        if (forcedChatUpdateRequired)
                        {
                                updateMask |= 4;
                        }
                        if (isChatTextUpdateRequired())
                        {
                                updateMask |= 0x80;
                        }
                        if (isAppearanceUpdateRequired())
                        {
                                updateMask |= 0x10;
                        }
                        if (faceUpdateRequired)
                        {
                                updateMask |= 1;
                        }
                        if (FocusPointX != -1)
                        {
                                updateMask |= 2;
                        }
                        if (isHitUpdateRequired())
                        {
                                updateMask |= 0x20;
                        }
                        if (hitUpdateRequired2)
                        {
                                updateMask |= 0x200;
                        }
                        if (updateMask >= 0x100)
                        {
                                updateMask |= 0x40;
                                str.writeByte(updateMask & 0xFF);
                                str.writeByte(updateMask >> 8);
                        }
                        else
                        {
                                str.writeByte(updateMask);
                        }
                        // now writing the various update blocks itself - note that their
                        // order crucial
                        if (mask100update)
                        {
                                appendMask100Update(str);
                        }
                        if (animationRequest != -1)
                        {
                                appendAnimationRequest(str);
                        }
                        if (forcedChatUpdateRequired)
                        {
                                appendForcedChat(str);
                        }
                        if (isChatTextUpdateRequired())
                        {
                                appendPlayerChatText(str);
                        }
                        if (faceUpdateRequired)
                        {
                                appendFaceUpdate(str);
                        }
                        if (isAppearanceUpdateRequired())
                        {
                                appendPlayerAppearance(str);
                        }
                        if (FocusPointX != -1)
                        {
                                appendSetFocusDestination(str);
                        }
                        if (isHitUpdateRequired())
                        {
                                appendHitUpdate(str);
                        }
                        if (hitUpdateRequired2)
                        {
                                appendHitUpdate2(str);
                        }
                }
        }

        public void clearUpdateFlags()
        {
                updateRequired = false;
                setChatTextUpdateRequired(false);
                setAppearanceUpdateRequired(false);
                setHitUpdateRequired(false);
                hitUpdateRequired2 = false;
                forcedChatUpdateRequired = false;
                mask100update = false;
                animationRequest = -1;
                FocusPointX = -1;
                FocusPointY = -1;
                poisonMask = -1;
                faceUpdateRequired = false;
                face = 65535;
        }

        public int newWalkCmdX[] = new int[walkingQueueSize];
        public int newWalkCmdY[] = new int[walkingQueueSize];
        public int newWalkCmdSteps = 0;
        public boolean newWalkCmdIsRunning;
        public int travelBackX[] = new int[walkingQueueSize];
        public int travelBackY[] = new int[walkingQueueSize];
        public int numTravelBackSteps = 0;

        public void preProcessing()
        {
                newWalkCmdSteps = 0;
        }

        /**
         * Called every main tick.
         */
        public void process() {
			    PlayerOther.agilityGain((Client) this);
                getPA().refreshSkill(3);
                if (followId > 0) {
                        getPA().followPlayer();
                } else if (followId2 > 0) {
                        getPA().followNpc();
                }
                getCombat().handlePrayerDrain();
                Summoning.handleDrain(this);

                if (!getCombat().wasEngagingPlayer()) {
                        underAttackBy = 0;
                }
                if (System.currentTimeMillis() - singleCombatDelay2 > 10000) {
                        underAttackBy2 = 0;
                }
                if (freezeTimer > -6) {
                        freezeTimer--;
                        if (frozenBy > 0) {
                                if (PlayerHandler.players[frozenBy] == null) {
                                        freezeTimer = -1;
                                        frozenBy = -1;
                                }
                        }
                }
                if (hitDelay > 0) {
                        hitDelay--;
                }
                if (hitDelay == 1) {
                        if (oldNpcIndex > 0) {
                                getCombat().delayedHit(oldNpcIndex);
                        }
                        if (oldPlayerIndex > 0) {
                                getCombat().playerDelayedHit(oldPlayerIndex);
                        }
                }
                if (attackTimer > 0)  {
                        attackTimer--;
                }
                if (attackTimer == 1) {
                        if (npcIndex > 0 && clickNpcType == 0) {
                        		NPC NPC = NPCHandler.npcs[npcIndex];
                                getCombat().attackNpc(NPC);
                        }
                        if (playerIndex > 0) {
                        	 Client victim = (Client) PlayerHandler.players[playerIndex];
                                Attack.attackPlayer((Client) this, victim);
                        }
                } else if (attackTimer <= 0 && (npcIndex > 0 || playerIndex > 0)) {
                        if (npcIndex > 0) {
                                attackTimer = 0;
                                NPC NPC = NPCHandler.npcs[npcIndex];
                                getCombat().attackNpc(NPC);
                        } else if (playerIndex > 0) {
                                attackTimer = 0;
                                Client victim = (Client) PlayerHandler.players[playerIndex];
                                Attack.attackPlayer((Client) this, victim);
                        }
                }

                if (getInventoryUpdate()) {
                		ItemAssistant.resetItems(this, 3214); // Update inventory.
                        getPA().requestUpdates();
                        ItemAssistant.calculateEquipmentBonuses(this);
                        ItemAssistant.writeBonus(this);
                        setInventoryUpdate(false);
                }

                if (timeOutCounter > Constants.TIMEOUT) {
                        disconnected = true;
                }
                timeOutCounter++;
        }

        public int getMapRegionX()
        {
                return mapRegionX;
        }

        public int getMapRegionY()
        {
                return mapRegionY;
        }

        public int getLocalX()
        {
                return getX() - 8 * getMapRegionX();
        }

        public int getLocalY()
        {
                return getY() - 8 * getMapRegionY();
        }

        /**
         * The player's x-coordinate.
         * @return
         * 			Player's x-cooridnate.
         */
        public int getX()
        {
                return absX;
        }

        /**
         * The player's y-coordinate.
         * @return
         * 			Player's y-cooridnate.
         */
        public int getY()
        {
                return absY;
        }

        public int getId()
        {
                return playerId;
        }

        public int getHitDiff()
        {
                return hitDiff;
        }

        public void setHitUpdateRequired(boolean hitUpdateRequired)
        {
                this.hitUpdateRequired = hitUpdateRequired;
        }

        public void setHitUpdateRequired2(boolean hitUpdateRequired2)
        {
                this.hitUpdateRequired2 = hitUpdateRequired2;
        }

        public boolean isHitUpdateRequired()
        {
                return hitUpdateRequired;
        }

        public boolean getHitUpdateRequired()
        {
                return hitUpdateRequired;
        }

        public boolean getHitUpdateRequired2()
        {
                return hitUpdateRequired2;
        }

        public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired)
        {
                this.appearanceUpdateRequired = appearanceUpdateRequired;
        }

        public boolean isAppearanceUpdateRequired()
        {
                return appearanceUpdateRequired;
        }

        public void setAppearanceUpdateRequired2(boolean appearanceUpdateRequired2)
        {
                this.appearanceUpdateRequired2 = appearanceUpdateRequired2;
        }

        public boolean isAppearanceUpdateRequired2()
        {
                return appearanceUpdateRequired2;
        }

        public void setChatTextEffects(int chatTextEffects)
        {
                this.chatTextEffects = chatTextEffects;
        }

        public int getChatTextEffects()
        {
                return chatTextEffects;
        }

        public void setChatTextSize(byte chatTextSize)
        {
                this.chatTextSize = chatTextSize;
        }

        public byte getChatTextSize()
        {
                return chatTextSize;
        }

        public void setChatTextUpdateRequired(boolean chatTextUpdateRequired)
        {
                this.chatTextUpdateRequired = chatTextUpdateRequired;
        }

        public boolean isChatTextUpdateRequired()
        {
                return chatTextUpdateRequired;
        }

        public byte[] getChatText()
        {
                return chatText;
        }

        public void setChatTextColor(int chatTextColor)
        {
                this.chatTextColor = chatTextColor;
        }

        public int getChatTextColor()
        {
                return chatTextColor;
        }

        public void setNewWalkCmdX(int newWalkCmdX[])
        {
                this.newWalkCmdX = newWalkCmdX;
        }

        public int[] getNewWalkCmdX()
        {
                return newWalkCmdX;
        }

        public void setNewWalkCmdY(int newWalkCmdY[])
        {
                this.newWalkCmdY = newWalkCmdY;
        }

        public int[] getNewWalkCmdY()
        {
                return newWalkCmdY;
        }

        public void setNewWalkCmdIsRunning(boolean newWalkCmdIsRunning)
        {
                this.newWalkCmdIsRunning = newWalkCmdIsRunning;
        }

        public boolean isNewWalkCmdIsRunning()
        {
                return newWalkCmdIsRunning;
        }

        public void setInStreamDecryption(ISAACRandomGen inStreamDecryption)
        {}

        public void setOutStreamDecryption(ISAACRandomGen outStreamDecryption)
        {}

        public void dealDamage(int damage)
        {
                if (skillLevel[Constants.HITPOINTS] - damage < 0)
                {
                        damage = skillLevel[Constants.HITPOINTS];
                }
                this.subtractFromHitPoints(damage);
                if (damage >= 0) {
                	ItemDegrade.doDegrade(this, 0);
                }
                int difference = skillLevel[Constants.HITPOINTS] - damage;
                if (difference <= maximumHitPoints() / 10 && difference > 0)
                {
                        appendRedemption();
                }
                this.getPA().refreshSkill(Constants.HITPOINTS);
                if (this.skillLevel[Constants.HITPOINTS] == 0 && !this.isDead)
                {
                        Death.killPlayer((Client) this);
                }
        }

        public void appendRedemption()
        {
                Client c = (Client) PlayerHandler.players[this.playerId];
                if (prayerActive[22])
                {
                        this.addToHitPoints((int)(maximumHitPoints() * .25));
                        skillLevel[5] = 0;
                        c.getPA().refreshSkill(3);
                        c.getPA().refreshSkill(5);
                        gfx0(436);
                        c.getCombat().resetPrayers();
                }
        }

        public int[] damageTaken = new int[Configuration.MAX_PLAYERS];
        public int hitMask;
        public int hitIcon;
        public int hitMask2;
        public int hitIcon2;

        public void handleHitMask(int damage, int mask, int icon, int soak, boolean maxHit)
        {
                if (!hitUpdateRequired)
                {
                        hitDiff = damage;
                        hitMask = maxHit ? 1 : mask;
                        hitIcon = icon;
                        soakDamage = soak;
                        hitUpdateRequired = true;
                }
                else if (!hitUpdateRequired2)
                {
                        hitDiff2 = damage;
                        hitMask2 = maxHit ? 1 : mask;
                        hitIcon2 = icon;
                        soakDamage2 = soak;
                        hitUpdateRequired2 = true;
                }
                updateRequired = true;
        }

        /* Items kept on death */

        public int[] keepItems = new int[4];
        public int[] keepItemsN = new int[4];
        public int WillKeepAmt1, WillKeepAmt2, WillKeepAmt3, WillKeepAmt4,
        WillKeepItem1, WillKeepItem2, WillKeepItem3, WillKeepItem4,
        WillKeepItem1Slot, WillKeepItem2Slot, WillKeepItem3Slot,
        WillKeepItem4Slot, EquipStatus;

        public byte buffer[] = null;
        public int packetSize = 0, packetType = -1;

        public IoSession session;
        public IoSession getSession()
        {
                return session;
        }
        public Queue < Packet > queuedPackets = new LinkedList < Packet > ();
        public void queueMessage(Packet arg1)
        {
                synchronized(queuedPackets)
                {
                        queuedPackets.add(arg1);
                }
        }

        public PlayList playList = new PlayList((Client) this);
        public PlayList getPlayList()
        {
                return playList;
        }

        public Stream inStream = null;
        public synchronized Stream getInStream()
        {
                return inStream;
        }

        private AncientCursesPrayerBook curses = new AncientCursesPrayerBook((Client) this);
        public AncientCursesPrayerBook curses()
        {
                return curses;
        }


        private Trade tradeAndDuel = new Trade((Client) this);
        public Trade getTradeAndDuel()
        {
                return tradeAndDuel;
        }

		private DuelArena duelArena = new DuelArena((Client) this);
		public DuelArena getDuelArena()
		{
				return duelArena;
		}

        private PlayerOther playerAssistant = new PlayerOther((Client) this);
        public PlayerOther getPA()
        {
                return playerAssistant;
        }

        private Combat combatAssistant = new Combat((Client) this);
        public Combat getCombat()
        {
                return combatAssistant;
        }

        private ActionHandler actionHandler = new ActionHandler((Client) this);
        public ActionHandler getActions()
        {
                return actionHandler;
        }

        private PersonalData gePersonalData = new PersonalData(this);
    	public PersonalData getPD() {
    		return gePersonalData;
    	}
        
        public void flushOutStream()
        {
                Client c = (Client) PlayerHandler.players[this.playerId];
                if (disconnected || outStream.currentOffset == 0)
                {
                        return;
                }
                synchronized(this)
                {
                        StaticPacketBuilder out = new StaticPacketBuilder().setBare(true);
                        byte[] temp = new byte[outStream.currentOffset];
                        System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
                        out.addBytes(temp);
                        c.session.write(out.toPacket());
                        outStream.currentOffset = 0;
                }
        }

        public void update()
        {
                synchronized(this)
                {
                        handler.updatePlayer(this, outStream);
                        handler.updateNPC(this, outStream);
                        flushOutStream();
                }
        }

        public synchronized boolean processQueuedPackets()
        {
                Client c = (Client) PlayerHandler.players[this.playerId];
                Packet p = null;
                synchronized(c.queuedPackets)
                {
                        p = c.queuedPackets.poll();
                }
                if (p == null)
                {
                        return false;
                }
                c.inStream.currentOffset = 0;
                c.packetType = p.getId();
                c.packetSize = p.getLength();
                c.inStream.buffer = p.getData();
                if (c.packetType > 0)
                {
                        PacketHandler.processPacket(c, c.packetType, c.packetSize);
                }
                timeOutCounter = 0;
                return true;
        }
	    public void checkOptionsTab() {
	        checkBrightness();
	        checkFog();
	    }
        public int brightnessLevel = 3;
        public void checkBrightness() {
        	if(brightnessLevel == 0) {
        		getPA().sendFrame36(166, 1);
        	} else if(brightnessLevel == 1) {
        		getPA().sendFrame36(166, 2);
        	} else if(brightnessLevel == 2) {
        		getPA().sendFrame36(166, 3);
        	} else if(brightnessLevel == 3) {
        		getPA().sendFrame36(166, 4);
        	}
        }
        public int fogIntensity;
        public void checkFog() {
	        if(fogIntensity == 0) {
	        	getPA().sendFrame36(199, 1);
	       } else if(fogIntensity == 1) {
	    	    getPA().sendFrame36(199, 2);
	        } else if(fogIntensity == 2) {
	        	getPA().sendFrame36(199, 3);
	        } else if(fogIntensity == 3) {
	        	getPA().sendFrame36(199, 4);
	        }
	    }

	public static int[] getNewObjectCoords(int rotation, int[] startCoords) {
		int[][] coords = new int[4][];

		coords[0] = new int[] { startCoords[0], startCoords[1] };

		for (int i = 1; i < 4; i++) {
			coords[i] = new int[] { coords[i - 1][1], 7 - coords[i - 1][0] };
		}

		return coords[rotation];
	}

	public static int[] getOriginalObjectCoords(int rotation, int[] startCoords) {
		int[][] coords = new int[4][];

		if (startCoords[0] == 3 && startCoords[1] == 3)
			return new int[] {3, 3};

		coords[0] = startCoords;

		for (int i = 1; i < 4; i++) {
			coords[i] = new int[] { coords[i - 1][1], 7 - coords[i - 1][0] };
		}

		switch (rotation) {
			case 0:
				return coords[0];
			case 1:
				return coords[3];
			case 2:
				return coords[2];
			case 3:
				return coords[1];
		}

		return coords[0];
	}

	public static int getNewXCoord(int rotation, int j, int k, int l, int i1) {
		rotation &= 3;
		if (rotation == 0)
			return k;
		if (rotation == 1)
			return l;
		if (rotation == 2)
			return 7 - k - (i1 - 1);
		else
			return 7 - l - (j - 1);
	}

	public static int getNewYCoord(int j, int k, int rotation, int i1, int j1) {
		rotation &= 3;
		if (rotation == 0)
			return j;
		if (rotation == 1)
			return 7 - j1 - (i1 - 1);
		if (rotation == 2)
			return 7 - j - (k - 1);
		else
			return j1;
	}
	public House getHouse() {
		return house;
	}
	public void setHouse(House house) {
		this.house = house;
	}

	public boolean inPartyRoom() {
		if (absX >= 3033 && absY >= 3369 && absX <= 3056 && absY <= 3386) {
			return true;
		}
		return false;
	}
	public boolean inFightCaves() {
		return absX >= 2360 && absX <= 2445 && absY >= 5045 && absY <= 5125;
	}

	public boolean inPcBoat() {
		return absX >= 2660 && absX <= 2663 && absY >= 2638 && absY <= 2643;
	}

	public boolean inPcGame() { return absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619; }

	public void correctCoordinates() {
		if (inFightCaves()) {
			getPA().movePlayer(absX, absY, playerId * 4);
			sendMessage("Your wave will start in 10 seconds.");
			CycleEventHandler.getSingleton().addEvent(playerId, new CycleEvent() {
				public void execute(CycleEventContainer c) {
					Server.fightCaves.spawnNextWave((Client) PlayerHandler.players[playerId]);
					c.stop();
				}
				@Override
				public void stop() {
				}
			}, 17);
		}
	}
	public Player getDuoPartner() {
		return duoPartner == null ? null : duoPartner.get();
	}


	public void setPotentialPartner(Player client) {
		potentialPartner = client == null ? null : new WeakReference<Player>(
				client);
	}

	public void setDuoPartner(Player client) {
		duoPartner = client == null ? null : new WeakReference<Player>(client);
	}

	/**
	 * Special plant one constructor & getter
	 */
	private SpecialPlantOne specialPlantOne = new SpecialPlantOne(this);

	public SpecialPlantOne getSpecialPlantOne() {
		return specialPlantOne;
	}

	/**
	 * Special plant one constructor & getter
	 */
	private SpecialPlantTwo specialPlantTwo = new SpecialPlantTwo(this);

	public SpecialPlantTwo getSpecialPlantTwo() {
		return specialPlantTwo;
	}

	/**
	 * Compost constructor & getter
	 */
	private Compost compost = new Compost(this);

	public Compost getCompost() {
		return compost;
	}

	/**
	 * Allotoments constructor & getter
	 */
	private Allotments allotment = new Allotments(this);

	public Allotments getAllotment() {
		return allotment;
	}

	/**
	 * Followers constructor & getter
	 */
	private Flowers flower = new Flowers(this);

	public Flowers getFlowers() {
		return flower;
	}

	/**
	 * Herbs constructor & getter
	 */
	private Herbs herb = new Herbs(this);

	public Herbs getHerbs() {
		return herb;
	}

	/**
	 * Hops constructor & getter
	 */
	private Hops hops = new Hops(this);

	public Hops getHops() {
		return hops;
	}

	/**
	 * Bushes constructor & getter
	 */
	private Bushes bushes = new Bushes(this);

	public Bushes getBushes() {
		return bushes;
	}

	/**
	 * Seedling constructor & getter
	 */
	private Seedling seedling = new Seedling(this);

	public Seedling getSeedling() {
		return seedling;
	}

	/**
	 * Wood trees constructor & getter
	 */
	private WoodTrees trees = new WoodTrees(this);

	public WoodTrees getTrees() {
		return trees;
	}

	/**
	 * Fruit tree constructor & getter
	 */
	private FruitTree fruitTrees = new FruitTree((this));

	public FruitTree getFruitTrees() {
		return fruitTrees;
	}

	public static int Flower[] = { 2980, 2981, 2982, 2983, 2984, 2985, 2986, 2987, 2988 };

	public static int randomFlower() {
		return Flower[(int) (Math.random() * Flower.length)];
	}

	private int tempInteger;

	public void setTempInteger(int i) {
		tempInteger = i;
	}

	public int getTempInteger() {
		return tempInteger;
	}

}