package core;

import ionic.player.content.miscellaneous.Tele;


/**
 * Constants regarding whole server.
 */
public class Constants
{
	
		/**
	 	* The name of the equipment bonuses.
	 	*/
		public final static String[] EQUIPMENT_BONUS =
        {
                "Stab", "Slash", "Crush", "Magic", "Ranged", "Stab", "Slash", "Crush", "Magic", "Range", "Strength", "Prayer"
        };

        /**
         * Player respawns in this x-axis coordinate when died in Wilderness.
         */
        public static final int deadRespawnX = Tele.HOME.x;

        /**
         * Player respawns in this y-axis coordinate when died in Wilderness.
         */
        public static final int deadRespawnY = Tele.HOME.y;

        /**
         * Offensive words.
         */
        public static final String offensiveLanguage[] = {
                "sex", "fuck", "faggot", "bitch", "cunt", "asshole", "nigger", "gay",
                "lesbian", "bastard", "blowjob", "blow job", "bukakke", "bukake", "cum", "homosexual"
        };

        /**
         * The delay in milliseconds between continious connections from the same IP.
         */
        public static final int CONNECTION_DELAY = 100;

        /**
         * The amount of simultaneous connections from the same IP.
         */
        public static final int IPS_ALLOWED = 2;
        
        

        /**
         * Equivelant to 40 seconds.
         * <p>
         * This is used to force disconnect the player if in combat.
         */
        public static final int TIMEOUT = 67;

        /**
         * Items added to inventory on death. These items also dissapear on death. These items are also un-tradeable and un-stake-able.
         */
        public static final int[] ITEMS_TO_INVENTORY_ON_DEATH = {
                11665, 11664, 11663, 8839, 8840, // Void items.
                10548, 10547, 10550, // Barbarian assault helms .
                6570, // Fire cape.
                15349, // Ardougne cape.
                7454, 7455, 7456, 7457, 7458, 7459, 7460, 7461, 7462, // All gloves. Such as barrows gloves etc..
                3840, // Holy book.
                3842, // Unholy book.
                3844, // Book of balance.
                8844, 8845, 8846, 8847, 8848, 8849, 8850, // All defenders.
                2412, 2413, 2414, // God capes.
                10499, // Ava's accumulator
                15098, // Dice
                20769, // Completionist set
        };

        /**
         * Item that dissapear on death. These items are also un-tradeable and un-stake-able.
         */
        public static final int[] ITEMS_TO_DISSAPEAR = {
                15018, 15019, 15220, // Imbued rings
                18335, // Arcane stream necklace
                10551, // Fighter torso
                19111, // Tokhaar-Kal
                18349, 18351, 18353, 18355, 18357, 18359, 18361, 18363, // Chaotics
                /* Skill capes */
                9747, 9748, 9749, 9750, 9751, 9752, 9753, 9754, 9755, 9756, 9757, 9758, 9759, 9760,
                9761, 9762, 9763, 9764, 9765, 9766, 9767, 9768, 9769, 9770, 9771, 9772, 9773, 9774,
                9775, 9776, 9777, 9778, 9779, 9780, 9781, 9782, 9783, 9784, 9785, 9786, 9787, 9788,
                9789, 9790, 9791, 9792, 9793, 9794, 9795, 9796, 9797, 9798, 9799, 9800, 9801, 9802,
                9803, 9804, 9805, 9806, 9807, 9808, 9809, 9810, 9811, 9812, 9813, 9814
                /* End of Skill capes */
        };


        /**
         * The maximum item identity.
         */
        public static final int MAX_ITEM_ID = 25800;
        
        /**
         * The maximum amount of a single item.
         */
        public static final int MAX_ITEM_AMOUNT = Integer.MAX_VALUE;
        
        /**
         * Maximum bank size.
         */
        public static final int BANK_SIZE = 352;
        
        /**
         * Remove player from Duel arena to this X coordinate.
         */
        public static final int DUEL_ARENA_X = 3362;
        
        /**
         * Remove player from Duel arena to this Y coordinate.
         */
        public static final int DUEL_ARENA_Y = 3263;
        
        /**
         * The random distance from the Duel arena X and Y coordinate spawn.
         */
        public static final int RANDOM_DISTANCE = 5;
        
        /**
         * Thieving experience multiplier.
         */
        public static final int THIEVING_EXPERIENCE = 100;
        
        /**
         * Slayer experience multiplier.
         */
        public static final int SLAYER_EXPERIENCE = 100;
        
        /**
         * Woodcutting experience multiplier.
         */
        public static final int WOODCUTTING_EXPERIENCE = 100;
        
        /**
         * Mining experience multiplier.
         */
        public static final int MINING_EXPERIENCE = 100;
        
        /**
         * Mining experience multiplier.
         */
        public static final int PRAYER_EXPERIENCE = 100;
        
        /**
         * Mining experience multiplier.
         */
        public static final int CRAFTING_EXPERIENCE = 100;
        
        /**
         * Smithing experience multiplier.
         */
        public static final int SMITHING_EXPERIENCE = 100;
        
        /**
         * Farming experience multiplier.
         */
        public static final int FARMING_EXPERIENCE = 100;
        
        /**
         * Firemaking experience multiplier.
         */
        public static final int FIREMAKING_EXPERIENCE = 100;
        
        /**
         * Herblore experience multiplier.
         */
        public static final int HERBLORE_EXPERIENCE = 100;
        
        /**
         * Fishing experience multiplier.
         */
        public static final int FISHING_EXPERIENCE = 100;
        
        /**
         * Agility experience multiplier.
         */
        public static final int AGILITY_EXPERIENCE = 200;
        
        /**
         * Runecrafting experience multiplier.
         */
        public static final int RUNECRAFTING_EXPERIENCE = 100;
        
        /**
         * Cooking experience multiplier.
         */
        public static final int COOKING_EXPERIENCE = 100;
        
        /**
         * Fletching experience multiplier.
         */
        public static final int FLETCHING_EXPERIENCE = 100;
        
        /**
         * Maximum distance an NPC can roam from it's spawn location.
         */
        public static final int NPC_RANDOM_WALK_DISTANCE = 8;
        
        /**
         * Maximum distance an NPC can follow the player from it's spawn location.
         */
        public static final int NPC_FOLLOW_DISTANCE = 8;	
        
        /**
         * Undead NPCs identities.
         */
        public static final int[] UNDEAD_NPCS = 
        {
                90, 91, 92, 93, 94, 103, 104, 73, 74, 75, 76, 77
        };
        
        /**
         * The slot identity of the head in the equipment tab.
         */
        public static final int HEAD_SLOT = 0;
        
        /**
         * The slot identity of the cape in the equipment tab.
         */
        public static final int CAPE_SLOT = 1;
        
        /**
         * The slot identity of the amulet in the equipment tab.
         */
        public static final int AMULET_SLOT = 2;
        
        /**
         * The slot identity of the weapon in the equipment tab.
         */
        public static final int WEAPON_SLOT = 3;
        
        /**
         * The slot identity of the torso in the equipment tab.
         */
        public static final int TORSO_SLOT = 4;
        
        /**
         * The slot identity of the shield in the equipment tab.
         */
        public static final int SHIELD_SLOT = 5;
        
        /**
         * The slot identity of the leg in the equipment tab.
         */
        public static final int LEG_SLOT = 7;
        
        /**
         * The slot identity of the hand in the equipment tab.
         */
        public static final int HAND_SLOT = 9;
        
        /**
         * The slot identity of the feet in the equipment tab.
         */
        public static final int FEET_SLOT = 10;
        
        /**
         * The slot identity of the ring in the equipment tab.
         */
        public static final int RING_SLOT = 12;
        
        /**
         * The slot identity of the arrow in the equipment tab.
         */
        public static final int ARROW_SLOT = 13;
        
        public static final String[] SKILL_NAMES = {
        	"Attack","Defence","Strength","Hitpoints","Ranged","Prayer","Magic","Cooking","Woodcutting",
        	"Fletching","Fishing","Firemaking","Crafting","Smithing","Mining","Herblore","Agility",
        	"Thieving","Slayer","Farming","Runecrafting","Hunter","Construction","Dungeoneering","Summoning",
        };
        
        public static final int ATTACK = 0;
        public static final int DEFENCE = 1;
        public static final int STRENGTH = 2;
        public static final int HITPOINTS = 3;
        public static final int RANGED = 4;
        public static final int PRAYER = 5;
        public static final int MAGIC = 6;
        public static final int COOKING = 7;
        public static final int WOODCUTTING = 8;
        public static final int HANDS = 9;
        public static final int FLETCHING = 9;
        public static final int FISHING = 10;
        public static final int FIREMAKING = 11;
        public static final int CRAFTING = 12;
        public static final int SMITHING = 13;
        public static final int MINING = 14;
        public static final int HERBLORE = 15;
        public static final int AGILITY = 16;
        public static final int THIEVING = 17;
        public static final int SLAYER = 18;
        public static final int FARMING = 19;
        public static final int RUNECRAFTING = 20;
        public static final int HUNTER = 21;
        public static final int CONSTRUCTION = 22;
        public static final int DUNGEONEERING = 23;
        public static final int SUMMONING = 24;
        
        public static final int THICK_SKIN = 0;
        public static final int BURST_OF_STRENGTH = 1;
        public static final int CLARITY_OF_THOUGHT = 2;
        public static final int SHARP_EYE = 3;
        public static final int MYSTIC_WILL = 4;
        public static final int ROCK_SKIN = 5;
        public static final int SUPERHUMAN_STRENGTH = 6;
        public static final int IMPROVED_REFLEXES = 7;
        public static final int RAPID_RESTORE = 8;
        public static final int RAPID_HEAL = 9;
        public static final int PROTECT_ITEM = 10;
        public static final int HAWK_EYE = 11;
        public static final int MYSTIC_LORE = 12;
        public static final int STEEL_SKIN = 13;
        public static final int ULTIMATE_STRENGTH = 14;
        public static final int INCREDIBLE_REFLEXES = 15;
        public static final int PROTECT_FROM_MAGIC = 16;
        public static final int PROTECT_FROM_RANGE = 17;
        public static final int PROTECT_FROM_MELEE = 18;
        public static final int EAGLE_EYE = 19;
        public static final int MYSTIC_MIGHT = 20;
        public static final int RETRIBUTION = 21;
        public static final int REDEMPTION = 22;
        public static final int SMITE = 23;
        public static final int CHIVALRY = 24;
        public static final int PIETY = 25;

        public static final int BUFFER_SIZE = 10000;

        public static int CONSTRUCTION_EXPERIENCE = 220;

        public static final int PACKET_SIZES[] = {
                0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
                0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
                0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
                0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
                2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
                0, 0, 0, 12, 4, 4, 0, 8, 8, 12, // 50
                8, 8, 0, 8, 0, 0, 8, 12, 6, 0, // 60
                6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
                0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
                0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
                0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
                0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
                1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
                0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
                0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
                0, 0, 1, 2, 0, 2, 6, 6, 4, 0, // 150
                0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
                0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
                0, 0, 12, 0, 0, 4, 6, 4, 4, 6, // 190
                2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
                4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
                0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
                1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
                0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
                0, 0, 6, 6, 0, 0, 0 // 250
        };
}