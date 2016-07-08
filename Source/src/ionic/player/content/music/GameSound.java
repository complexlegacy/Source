package ionic.player.content.music;

import ionic.item.ItemAssistant;
import ionic.player.Client;

/**
 *
 *
 * @author relex lawl
 */
public final class GameSound {
	
	public static void getCombatSound(Client client) {
		int sound = -1;
		
		if (client.usingMagic) {
		} else if (client.getUsingRange()) {
		} else {
			if (client.usingSpecial) {
				sound = getSpecialSound(client);
			} else {
				sound = getAttackSound(client);
			}
		}
		
		if (sound != -1) {
			client.getPA().sendSound(sound, 0, 0, 10);
		}
	}
	
	/**
	 * Regular combat
	 * 
	 * itemId : soundId
	 */
	private static final int[][] WEAPON_SOUND = { { 4718, 1320 },
			{ 4734, 1081 }, { 4747, 1330 }, { 4710, 2555 }, { 4755, 1323 },
			{ 4726, 2562 }, { 6528, 2520 }, { 4675, 2555 }, { 11235, 3731 },
			{ 4151, 1081 }, };
	/**
	 * When player is using special attack
	 * 
	 * itemId : soundId
	 */
	private static final int[][] SPECIAL_SOUND = { { 1534, 2541 },
			{ 5698, 2537 }, { 4151, 1082 }, { 1305, 2529 }, { 861, 2545 }, };

	private static int getSpecialSound(Client player) {
		for (int i = 0; i < SPECIAL_SOUND.length; i++) {
			if (ItemAssistant.hasItemEquippedSlot(player, 3, SPECIAL_SOUND[i][0])) {
				return SPECIAL_SOUND[i][1];
			}
		}
		return -1;
	}

	private static int getAttackSound(Client player) {
		for (int i = 0; i < WEAPON_SOUND.length; i++) {
			if (ItemAssistant.hasItemEquippedSlot(player, 3, WEAPON_SOUND[i][0]))
			{
				return WEAPON_SOUND[i][1];
			}
		}
		int attackStyle = player.fightMode;
		try {
			String weaponName = ItemAssistant.getItemName(player.playerEquipment[3]).toLowerCase();
			if (weaponName.contains("staff")) {
				return 2555;
			} else if (weaponName.contains("shortbow")
					|| weaponName.contains("longbow")) {
				return 2693;
			} else if (weaponName.contains("c'bow")
					|| weaponName.contains("crossbow")) {
				return 2700;
			} else if (weaponName.contains("axe")
					|| weaponName.contains("battleaxe")) {
				return 2508;
			} else if (weaponName.contains("2h")) {
				return 2503;
			} else if (weaponName.contains("knife")) {
				return 2696;
			} else if (weaponName.contains("dagger")) {
				return attackStyle == 4 ? 2548 : 2547;
			} else if (weaponName.contains("dart")) {
				return 2547;
			}
		} catch (Exception e) {
			return attackStyle == 1 ? 2565 : 2564;
		}
		return 2500;
	}

	/**
	 * Other
	 */
	
	public static final int FROZEN = 154;
	public static final int SPLASH = 227;

	/**
	 * Non combat spells
	 */
	public static final int HIGH_ALCHEMY = 97;
	public static final int LOW_ALCHEMY = 98;
	public static final int SUPER_HEAT_ITEM = 114;
	public static final int ENCHANTE_JEWELLERY = 116;
	public static final int TELE_GRAB = 192;
	public static final int HOME_TELEPORT_START = 193;
	public static final int HOME_TELEPORT_NEXT = 194;
	public static final int HOME_TELEPORT_END = 195;
	public static final int ANCIENT_END_TELE = 197;
	public static final int TELE_TAB = 965;
	public static final int TELE_OTHER = 199;
	
	/**
	 * God spells
	 */
	
	public static final int GUTHIX_FAIL = 1652;
	public static final int GUTHIX_HIT = 1653;
	public static final int ZAMMY_FAIL = 1654;
	public static final int ZAMMY_HIT = 1655;
	public static final int SARA_FAIL = 1656;
	public static final int SARA_HIT = 1659;

	/**
	 * Modern defensive spells
	 */
	
	public static final int BIND = 100;
	public static final int BIND_FAIL = 101;
	
	public static final int VULNERABILITY_CAST = 148;
	public static final int VULNERABILITY_FAIL = 149;
	public static final int VULNERABILITY_END = 150;
	
	public static final int ENTANGLE_CAST = 151;
	public static final int ENTANGLE_FAIL = 152;
	public static final int ENTANGLE_END = 153;
	
	public static final int WEAKEN_CAST = 119;
	public static final int WEAKEN_END = 121;
	public static final int CURSE_CAST = 127;
	public static final int CURSE_END = 126;

	public static final int TELEBLOCK_CAST = 202;
	public static final int TELEBLOCK_HIT = 203;
	
	/**
	 * Modern offensive spells
	 */
	
	public static final int CRUMBLE_UNDEAD_CAST = 122;
	public static final int CRUMBLE_UNDEAD_END = 123;
	
	public static final int AIR_BLAST_CAST = 216;
	public static final int AIR_BLAST_HIT = 217;
	public static final int AIR_BOLT_CAST = 218;
	public static final int AIR_BOLT_HIT = 219;
	public static final int AIR_STRIKE_CAST = 220;
	public static final int AIR_STRIKE_HIT = 221;
	public static final int AIR_WAVE_CAST = 222;
	public static final int AIR_WAVE_HIT = 223;
	
	public static final int EARTH_BLAST_CAST = 128;
	public static final int EARTH_BLAST_HIT = 129;
	public static final int EARTH_BOLT_CAST = 130;
	public static final int EARTH_BOLT_HIT = 131;
	public static final int EARTH_STRIKE_CAST = 132;
	public static final int EARTH_STRIKE_HIT = 133;
	public static final int EARTH_WAVE_CAST = 134;
	public static final int EARTH_WAVE_HIT = 135;
	
	public static final int WATER_BLAST_CAST = 207;
	public static final int WATER_BLAST_HIT = 208;
	public static final int WATER_BOLT_CAST = 209;
	public static final int WATER_BOLT_HIT = 210;
	public static final int WATER_STRIKE_CAST = 211;
	public static final int WATER_STRIKE_HIT = 212;
	public static final int WATER_WAVE_CAST = 213;
	public static final int WATER_WAVE_HIT = 214;
	
	public static final int FIRE_BLAST_CAST = 155;
	public static final int FIRE_BLAST_HIT = 156;
	public static final int FIRE_BOLT_CAST = 157;
	public static final int FIRE_BOLT_HIT = 158;
	public static final int FIRE_STRIKE_CAST = 160;
	public static final int FIRE_STRIKE_HIT = 161;
	public static final int FIRE_WAVE_CAST = 162;
	public static final int FIRE_WAVE_HIT = 163;
	
	/**
	 * Ancient spells
	 */
	
	public static final int BLOOD_RUSH = 103;
	public static final int BLOOD_BURST = 104;
	public static final int BLOOD_BLITZ = 105;
	public static final int BLOOD_BARRAGE = 106;
	public static final int BLOOD_FAIL = 109;
	public static final int BLOOD_START = 108;

	public static final int ICE_SPELL_CAST = 171;
	public static final int ICE_BARRAGE_HIT = 168;
	public static final int ICE_BLITZ_HIT = 169;
	public static final int ICE_BURST_HIT = 170;
	public static final int ICE_RUSH_HIT = 173;
	
	/**
	 * Lunar spells
	 */
	public static final int VENGEANCE = 2908;
	public static final int VENGEANCE_OTHER = 2907;
	public static final int HUMIDIFY = 3614;
	
	/**
	 * 382 enter wilderness
	 * 
	 * World sounds
	 */

	/**
	 * Item related world
	 */

	public static final int DROP_COINS = 10;
	public static final int DROP = 2739;
	public static final int PICKUP = 2582;
	public static final int UNEQUIP = 2238;
	public static final int NO_INVENTORY_SPACE = 2268;
	public static final int EATING = 2393;
	public static final int DRINKING = 2401;

	/**
	 * Object related world
	 */

	public static final int BARROW_DOOR = 41;
	public static final int OPEN_CHEST = 1775;
	public static final int OPEN_METAL_GATE = 70;
	public static final int CLOSE_METAL_GATE = 68;
	public static final int OPEN_DOOR = 81;
	public static final int CLOSE_DOOR = 43;
	public static final int FORCE_DOOR = 3419;
	public static final int TELEPORT_MINI_ESS = 125;
	public static final int TELEPORT_OBELISK = 204;
	public static final int PULL_LEVER = 2400;
	public static final int ENTER_HOUSE = 984;
	public static final int HOPPER = 3189;
	public static final int POP_BALLOON = 3252;
	// 3692 To 3705 - Piano Keys
	public static final int CANNON_TURNING = 2877;

	/**
	 * Misc related world
	 */
	public static final int SOFT_WIND = 2277;
	public static final int STRONGER_WIND = 2278;
	public static final int LOUDER_WIND = 2279;
	public static final int VERY_STRONG_WIND = 2280;

	public static final int BANK_PIN_CORRECT = 1040;
	public static final int PRESSING_PIN = 1041;

	public static final int EARTHQUAKE = 1464;
	// 984

	/**
	 * Skills related
	 */
	public static final int CUTTING_GEM = 2586;

	public static final int PICK_FLAX = 2581;
	public static final int SPINNING_WHEEL = 2590;

	public static final int LIGH_FIRE_ATTEMPT = 2599;
	public static final int SUCCED_FIRE = 2594;

	public static final int CATCH_FISH = 2600;

	public static final int FLETCHING_SHAFT = 760;
	public static final int FLETCHING_BOW = 761;
	public static final int STRINGIN_BOW = 2606;

	public static final int MINE_ONCE = 2656;
	public static final int MINE_THIRD = 2657;
	public static final int MINE_FIFTH = 2658;
	public static final int MINE_FINISH = 2659;

	public static final int HAMMERING = 3771;

	public static final int COOKING = 2577;

	public static final int WOODCUT = 3037;

	public static final int BURY_BONES = 2738;
	public static final int BONES_ON_ALTAR = 958;

	public static final int THIEVING_STUNNED = 1842;
	public static final int PICKPOCKET_CHEST = 37;
	public static final int GENIE_LAMP = 2655;
	public static final int PRAYER_OFF = 2663;
	public static final int NO_PRAYER_LEFT = 2672;
	public static final int WRONG_LEVEL = 2673;
	public static final int RECHARGE = 2674;
	public static final int IMPROVED_REFLEXES = 2662;
	public static final int INCREDIBLE_REFLEXES = 2667;
	public static final int CLARITY_OF_THOUGHT = 2664;
	public static final int HAWK_EYE = 2665;
	public static final int EAGLE_EYE = 2666;
	public static final int MYSTIC_LORE = 2668;
	public static final int MISTIC_MIGHT = 2669;
	public static final int MISTIC_WILL = 2670;
	public static final int MAGIC_PROTECT = 2675;
	public static final int MELEE_PROTECT = 2676;
	public static final int RANGE_PROTECT = 2677;
	public static final int RAPID_HEAL = 2678;
	public static final int RAPID_RESTORE = 2679;
	public static final int REDEMPTION = 2678;
	public static final int REDEMPTION_USED = 2679;
	public static final int ROCK_SKIN = 2684;
	public static final int STEEL_SKIN = 2687;
	public static final int BURST_OF_STRENGTH = 2689;
	public static final int SUPERHUMAN_STRENGTH = 2690;
	public static final int ULTIMATE_STRENGTH = 2691;
	public static final int SMITE = 2685;
	public static final int PROT_ITEM = 2671;
	public static final int THICK_SKIN = 10000;
	public static final int SHARP_EYE = 10000;
	public static final int RETRIBUTION = 10000;
}
