package ionic.player.content.prayer;

import ionic.npc.NPC;
import ionic.npc.NPCHandler;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.interfaces.InterfaceAssistant;
import ionic.player.interfaces.ItemsKeptOnDeath;
import core.Constants;

public class AncientCursesPrayerBook
{
	private Client player;

	public AncientCursesPrayerBook(Client client)
	{
		this.player = client;
	}

	/* All of the configuration such as activation, curse names, etc. */

	public final int[] PRAYER_LEVEL_REQUIRED = {
			50, 50, 52, 54, 56, 59, 62, 65, 68, 71, 74, 76, 78, 80, 82, 84, 86, 89, 92, 95
	};
	public final String[] CURSE_NAME = {
			"Protect Item", "Sap Warrior", "Sap Ranger", "Sap Mage", "Sap Spirit", "Berserker", "Deflect Summoning", "Deflect Magic", "Deflect Missiles", "Deflect Melee", "Leech Attack", "Leech Ranged", "Leech Magic", "Leech Defence", "Leech Strength", "Leech Energy", "Leech Special Attack", "Wrath", "Soul Split", "Turmoil"
	};
	public final static int[] GLOW = {
		83, 84, 85, 101, 102, 86, 87, 88, 89, 90, 91, 103, 104, 92, 93, 94, 95, 96, 97, 105
	};
	public final int[] HEADICONS = {
			-1, -1, -1, -1, -1, -1, 12, 10, 11, 9, -1, -1, -1, -1, -1, -1, -1, 16, 17, -1
	};
	public double[] curseData = {
			0.6, // Protect Item
			1, // Sap Warrior
			1, // Sap Range
			1, // Sap Mage
			1, // Sap Spirit
			1.5, // Berserker
			1.5, // Deflect Summoning
			1.5, // Deflect Mage
			1.5, // Deflect Range
			1.5, // Deflect Melee
			1.5, // Leech Attack
			1.5, // Leech Range
			1.5, // Leech Mage
			1.5, // Leech Defence
			1.5, // Leech Strength
			1.5, // Leech Energy
			1.5, // Leech Special
			1.5, // Wrath
			3, // Soul Split
			5, // Turmoil
	};

	public final int 
	PROTECT_ITEM = 0, 
	SAP_WARRIOR = 1, 
	SAP_RANGER = 2, 
	SAP_MAGE = 3, 
	SAP_SPIRIT = 4, 
	BERSERKER = 5, 
	DEFLECT_SUMMONING = 6,
	DEFLECT_MAGIC = 7, 
	DEFLECT_MISSILES = 8, 
	DEFLECT_MELEE = 9, 
	LEECH_ATTACK = 10, 
	LEECH_RANGED = 11, 
	LEECH_MAGIC = 12, 
	LEECH_DEFENCE = 13, 
	LEECH_STRENGTH = 14, 
	LEECH_ENERGY = 15, 
	LEECH_SPEC = 16, 
	WRATH = 17, 
	SOUL_SPLIT = 18, 
	TURMOIL = 19;

	public void activateCurse(int i)
	{

		if (player.Prayerbook == 0)
		{
			return;
		}
		if (player.duelRule[7])
		{
			for (int p = 0; p < 20; p++)
			{
				player.curseActive[p] = false;
				player.getPA().sendFrame36(GLOW[p], 0);
			}
			player.sendMessage("Prayer has been disabled in this duel!");
			InterfaceAssistant.quickPrayersOff(player);
			return;
		}
		if (player.skillLevel[1] < 30)
		{
			player.getPA().sendFrame36(GLOW[i], 0);
			player.sendMessage("You need 30 Defence to use this prayer.");
			InterfaceAssistant.quickPrayersOff(player);
			return;
		}
		int[] leeches = {
				LEECH_ATTACK, LEECH_RANGED, LEECH_MAGIC, LEECH_DEFENCE, LEECH_STRENGTH, LEECH_ENERGY, LEECH_SPEC
		};
		int[] saps = {
				SAP_WARRIOR, SAP_RANGER, SAP_MAGE, SAP_SPIRIT
		};
		int[] isHeadIcon = {
				DEFLECT_MAGIC, DEFLECT_MISSILES, DEFLECT_MELEE, WRATH, SOUL_SPLIT
		};
		if (player.skillLevel[5] > 0)
		{
			if (player.getPA().getLevelForXP(player.playerXP[5]) >= PRAYER_LEVEL_REQUIRED[i])
			{
				if (!player.curseActive[i]) curseEmote(i);
				boolean headIcon = false;
				switch (i)
				{
				case PROTECT_ITEM:
					if (!player.prayerActive[Constants.PROTECT_ITEM] && !player.getRedSkull())
					{
						player.prayerActive[Constants.PROTECT_ITEM] = true;
					}
					else
					{
						player.prayerActive[Constants.PROTECT_ITEM] = false;
						if (player.getRedSkull())
						{
							player.sendMessage("You cannot use this while on red skull.");
						}
					}
					ItemsKeptOnDeath.updateInterface(player);
					break;

				case SAP_WARRIOR:
				case SAP_RANGER:
				case SAP_MAGE:
				case SAP_SPIRIT:
					if (!player.curseActive[i])
					{
						for (int j = 0; j < leeches.length; j++)
						{
							if (leeches[j] != i) deactivate(leeches[j]);
						}
						deactivate(TURMOIL);
					}
					break;

				case LEECH_ATTACK:
				case LEECH_RANGED:
				case LEECH_MAGIC:
				case LEECH_DEFENCE:
				case LEECH_STRENGTH:
				case LEECH_ENERGY:
				case LEECH_SPEC:
					if (!player.curseActive[i])
					{
						for (int j = 0; j < saps.length; j++)
						{
							if (saps[j] != i)
							{
								deactivate(saps[j]);
							}
						}
						deactivate(TURMOIL);
					}
					break;

				case DEFLECT_SUMMONING:
				case DEFLECT_MAGIC:
				case DEFLECT_MISSILES:
				case DEFLECT_MELEE:
					if (System.currentTimeMillis() - player.stopPrayerDelay < 5000)
					{
						player.sendMessage("You have been injured and can't use this prayer!");
						deactivate(i);
						return;
					}
				case WRATH:
				case SOUL_SPLIT:
					headIcon = true;
					if (!player.curseActive[i])
					{
						for (int j = 0; j < isHeadIcon.length; j++)
						{
							if (isHeadIcon[j] != i && i != DEFLECT_SUMMONING) {
								deactivate(isHeadIcon[j]);
							}
						}
					}
					break;

				case TURMOIL:
					if (!player.curseActive[i])
					{
						for (int j = 0; j < leeches.length; j++)
						{
							if (leeches[j] != i) deactivate(leeches[j]);
						}
						for (int j = 0; j < saps.length; j++)
						{
							if (saps[j] != i) deactivate(saps[j]);
						}
					}
					break;
				}
				if (!headIcon)
				{
					if (!player.curseActive[i])
					{
						player.curseActive[i] = true;
						player.getPA().sendFrame36(GLOW[i], 1);
					}
					else
					{
						player.curseActive[i] = false;
						player.getPA().sendFrame36(GLOW[i], 0);
					}
				}
				else
				{
					if (!player.curseActive[i])
					{
						player.curseActive[i] = true;
						player.getPA().sendFrame36(GLOW[i], 1);
						player.headIcon = HEADICONS[i];
						if (player.curseActive[DEFLECT_SUMMONING] && i == DEFLECT_MELEE
								|| player.curseActive[DEFLECT_MELEE] && i == DEFLECT_SUMMONING) {
							player.headIcon = 13;
						}
						if (player.curseActive[DEFLECT_SUMMONING] && i == DEFLECT_MAGIC
								|| player.curseActive[DEFLECT_MAGIC] && i == DEFLECT_SUMMONING) {
							player.headIcon = 15;                          	  
						}
						if (player.curseActive[DEFLECT_SUMMONING] && i == DEFLECT_MISSILES
								|| player.curseActive[DEFLECT_MISSILES] && i == DEFLECT_SUMMONING) {
							player.headIcon = 14;
						}
						if (player.curseActive[DEFLECT_SUMMONING] && i == SOUL_SPLIT) {
							deactivate(DEFLECT_SUMMONING);
						}

						player.getPA().requestUpdates();
					}
					else
					{
						player.headIcon = -1;
						deactivate(i);
						player.getPA().requestUpdates();
					}
				}
			}
			else
			{
				deactivate(i);
				player.getPA().sendFrame126("You need a Prayer level of " + PRAYER_LEVEL_REQUIRED[i] + " to use " + CURSE_NAME[i] + ".", 357);
				player.getPA().sendFrame126("Click here to continue", 358);
				player.getPA().sendFrame164(356);
			}
		}
		else
		{
			player.getPA().sendFrame36(GLOW[i], 0);
			player.sendMessage("You have run out of prayer points!");
		}
		if (player.getRedSkull())
		{
			player.curseActive[0] = false;
			player.getPA().sendFrame36(AncientCursesPrayerBook.GLOW[0], 0);
			player.getPA().requestUpdates();
		}

	}

	public void curseButtons(int buttonId)
	{
		int[] buttonIds = {
				95183, 95815, 95817, 95819, 95191, 95193, 95195, 95197, 95199, 95201, 95203, 95205,
				95207, 95209, 95211, 95213, 95215, 95217, 95219, 95221
		};
		for (int i = 0; i < buttonIds.length; i++)
		{
			if (buttonIds[i] == buttonId) activateCurse(i);
		}
		checkPrayerActive(player);
	}

	/**
	 * Check if any prayer is active.
	 */
	public static void checkPrayerActive(Player player)
	{
		boolean anyPrayerActive = false;
		for (int p = 0; p < 20; p++)
		{
			if (player.curseActive[p])
			{
				anyPrayerActive = true;
			}
		}
		for (int i = 0; i < player.prayerActive.length; i++)
		{
			if (player.prayerActive[i])
			{
				anyPrayerActive = true;
			}
		}
		if (!anyPrayerActive)
		{
			InterfaceAssistant.quickPrayersOff((Client) player);
			if (player.quickPray || player.quickCurse)
			{
				QuickCurses.turnOffQuicks((Client) player);
				if (!player.prayerActive[Constants.PROTECT_ITEM])
				{
					ItemsKeptOnDeath.updateInterface(player);
				}
			}
		}
	}

	public void curseEmote(int curseId)
	{
		switch (curseId)
		{
		case PROTECT_ITEM:
			player.startAnimation(12567);
			player.gfx0(2213);
			break;
		case BERSERKER:
			player.startAnimation(12589);
			player.gfx0(2266);
			break;
		case TURMOIL:
			player.startAnimation(12565);
			player.gfx0(2226);
			break;
			case DEFLECT_MAGIC:
                	player.startAnimation(12573);
                	player.gfx0(2228);
                	break;
                case DEFLECT_MISSILES:
                	player.startAnimation(12573);
                	player.gfx0(2229);
                	break;
                case DEFLECT_MELEE:
                	player.startAnimation(12573);
                	player.gfx0(2230);
                	break;
                case DEFLECT_SUMMONING:
                	player.startAnimation(12573);
                	player.gfx0(2227);
                	break;
		case SOUL_SPLIT:
			player.startAnimation(12584);
			player.gfx0(2255);
			break;
		}
	}

	public void deactivate(int i)
	{
		player.curseActive[i] = false;
		player.getPA().sendFrame36(GLOW[i], 0);

		if (player.curseActive[DEFLECT_SUMMONING]) {
			if (i == DEFLECT_MAGIC || i == DEFLECT_MISSILES || i == DEFLECT_MELEE) {
				player.headIcon = 12;
			}
		}

		if (player.curseActive[DEFLECT_MAGIC]) {
			if (i == DEFLECT_SUMMONING) {
				player.headIcon = 10;
			}
		}
		if (player.curseActive[DEFLECT_MISSILES]) {
			if (i == DEFLECT_SUMMONING) {
				player.headIcon = 11;
			}
		}
		if (player.curseActive[DEFLECT_MELEE]) {
			if (i == DEFLECT_SUMMONING) {
				player.headIcon = 9;
			}
		}


	}

	/* Actual curse content */
	public double getTurmoilMultiplier(String stat)
	{
		NPC n = null;
		Client c2 = null;
		double otherLevel = 0;
		double turmoilMultiplier = stat.equalsIgnoreCase("Strength") ? 1.23 : 1.15;
		if (player.oldPlayerIndex > 0) c2 = (Client) PlayerHandler.players[player.oldPlayerIndex];
		else if (player.oldNpcIndex > 0) n = NPCHandler.npcs[player.oldNpcIndex];
		if (stat.equalsIgnoreCase("Defence"))
		{
			if (c2 != null) otherLevel = c2.getLevelForXP(c2.playerXP[1]) * 0.15;
			else if (n != null) otherLevel = n.getCombatLevel() * 0.15;
			else otherLevel = 0;
		}
		else if (stat.equalsIgnoreCase("Strength"))
		{
			if (c2 != null) otherLevel = c2.getLevelForXP(c2.playerXP[2]) * 0.10;
			else if (n != null) otherLevel = n.getCombatLevel() * 0.10;
			else otherLevel = 0;
		}
		else if (stat.equalsIgnoreCase("Attack"))
		{
			if (c2 != null) otherLevel = c2.getLevelForXP(c2.playerXP[0]) * 0.15;
			else if (n != null) otherLevel = n.getCombatLevel() * 0.15;
			else otherLevel = 0;
		}
		if (otherLevel > 14) otherLevel = 14;
		turmoilMultiplier += otherLevel * .01;
		return turmoilMultiplier;
	}

	/**
	 * Soulsplit effect.
	 * @param theTarget
	 * 			The player being attacked.
	 * @param damage
	 * 			The damage dealt to theTarget
	 * @param stage2
	 * 			True, to show the gfx travelling from theTarget to the player.
	 */
	public void vsNpcSoulSplit(int theTarget, int damage, boolean stage2)
	{
		NPC target = NPCHandler.npcs[theTarget];
		if (target == null)
		{
			return;
		}
		if (player.isDead)
		{
			return;
		}
		if (!player.curseActive[SOUL_SPLIT])
		{
			return;
		}
		target.gfx0(2264);
		player.getPA().createPlayersProjectile(player.getX(), player.getY(), (player.getX() - target.getX()) * -1, (player.getY() - target.getY()) * -1, 50, 70, 2263, 25, 25, theTarget + 1, 0);
		player.getPA().createPlayersProjectile(target.getX(), target.getY(), (target.getX() - player.getX()) * -1, (target.getY() - player.getY()) * -1, 50, 70, 2263, 25, 25, -player.getId() - 1, 0);
		player.addToHitPoints(damage / 5);
	}

	public void vsPlayerSoulSplit(Player victim, int damage, boolean stage2)
	{
		if (victim == null)
		{
			return;
		}
		if (player.isDead)
		{
			return;
		}
		if (!player.curseActive[SOUL_SPLIT])
		{
			return;
		}
		if (stage2)
		{
			victim.getPA().createPlayersProjectile(victim.getX(), victim.getY(), (victim.getY() - player.getY()) * -1, (victim.getX() - player.getX()) * -1, 50, 70, 2263, 25, 25, -player.getId() - 1, 40);
			return;
		}
		player.getPA().createPlayersProjectile(player.getX(), player.getY(), (player.getY() - victim.getY()) * -1, (player.getX() - victim.getX()) * -1, 50, 70, 2263, 25, 25, -victim.getId() - 1, 40);
		victim.gfx0(2264);
		if (victim.skillLevel[5] >= 0)
		{
			victim.skillLevel[5] -= (int) damage / 5;
			if (victim.skillLevel[5] < 0)
			{
				victim.skillLevel[5] = 0;
			}
			victim.getPA().refreshSkill(5);
		}
		player.addToHitPoints((int) damage / 5);
	}

	public void deflect(Player c2, int damage, int damageType) {
			int deflect = (damage < 10) ? 1 : (int)(damage / 10);
			player.getCombat().appendHit(c2, deflect, 0, 3, false);
	}

	public void deflectNPC(NPC n, int damage, int damageType) {
		if (damage > 0) {
			player.getCombat().appendHit(n, damage, 0, 3, 2);
		}
	}

}