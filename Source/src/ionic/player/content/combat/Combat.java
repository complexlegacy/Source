package ionic.player.content.combat;

import ionic.item.ItemAssistant;
import ionic.item.ItemDegrade;
import ionic.npc.NPC;
import ionic.npc.NPCHandler;
import ionic.npc.NPCSize;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.content.combat.vsplayer.Attack;
import ionic.player.content.combat.vsplayer.magic.MagicApplyDamage;
import ionic.player.content.combat.vsplayer.melee.MeleeApplyDamage;
import ionic.player.content.combat.vsplayer.range.RangeApplyDamage;
import ionic.player.content.minigames.PestControl;
import ionic.player.content.prayer.AncientCursesPrayerBook;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import ionic.player.interfaces.InterfaceAssistant;
import ionic.player.movement.Movement;
import utility.Misc;
import core.Constants;
import core.Server;

public class Combat
{
	public int strBonus;
	private Client player;

	public Combat(Client Client)
	{

		this.player = Client;

	}

	/**
	 * Reset variables like using special attack etc..
	 */
	public void resetEffects()
	{
		player.specAccuracy = 1.0;
		player.specDamage = 1.0;
		player.delayedDamage = player.delayedDamage2 = 0;
		player.setDragonClawsSpecialAttack(false);
		player.setSpecialAttack(false);
		player.setMultipleDamageSpecialAttack(false);
		player.saradominSwordSpecialAttack = false;
		player.morrigansJavelinSpecialAttack = false;
		player.handCannonSpecialAttack = false;
	}

	/**
	 * Reset the in-combat data. Which means the player is no-longer in combat.
	 */
	public void resetInCombat()
	{
		player.lastTimeEngagedNPC = 0;
		player.lastTimeEngagedPlayer = 0;
	}

	/**
	 * Restore the special attack by 5% every 30 seconds.
	 */
	public void restoreSpecialAttackEvent()
	{
		if (player.specialAttackEvent)
		{
			return;
		}
		player.specialAttackEvent = true;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
		{@
			Override
			public void execute(CycleEventContainer container)
		{
			if (player.specAmount < 10)
			{
				player.specAmount += 0.5;
				ItemAssistant.addSpecialBar(player, player.playerEquipment[Constants.WEAPON_SLOT]);
			}
		}

		@
		Override
		public void stop()
		{}
		}, 50);
	}

	/**
	 * Lower the boosted combat levels that are done by extremes or overload.
	 */
	public void lowerBoosterCombatLevels()
	{
		if (player.skillLevel[0] > 118 || player.skillLevel[1] > 118 || player.skillLevel[2] > 118 || player.skillLevel[4] > 112 || player.skillLevel[6] > 105)
		{
			player.skillLevel[0] = 118;
			player.skillLevel[1] = 118;
			player.skillLevel[2] = 118;
			player.skillLevel[4] = 112;
			player.skillLevel[6] = 105;
			player.sendMessage("Your boosted combat levels have been drained.");
		}
	}

	/**
	 * Use to check if the player is in a player vs player area or in combat and also send alert messages to the player.
	 * @return
	 * 			True, if the player is in PVP area or in combat.
	 */
	public boolean inPVPAreaOrCombat()
	{
		if (player.inPVPArea())
		{
			player.sendMessage("You cannot do this in a player vs player area.");
			return true;
		}
		if (player.getCombat().inCombat())
		{
			player.sendMessage("You need to wait a few more seconds from being out of combat to use this.");
			return true;
		}
		return false;
	}

	/**
	 * Use to check if the player is in combat and also send alert message to the player.
	 * @return
	 * 		True, if player is in combat.
	 */
	public boolean inCombatAlert()
	{
		if (player.getCombat().inCombat())
		{
			player.sendMessage("You need to wait a few more seconds from being out of combat to use this.");
			return true;
		}
		return false;
	}

	/**
	 * @return True, if the player has been in combat with a player in the last 10 seconds.
	 */
	public boolean wasEngagingPlayer()
	{
		if (System.currentTimeMillis() - player.lastTimeEngagedPlayer < 10000)
		{
			return true;
		}
		return false;
	}

	/**
	 * @return True, if the player has been in combat with an NPC in the last 10 seconds.
	 */
	public boolean wasEngagingNPC()
	{
		if (System.currentTimeMillis() - player.lastTimeEngagedNPC < 10000)
		{
			return true;
		}
		return false;
	}

	/**
	 * @return True, if the player has been in combat in the last 10 seconds.
	 */
	public boolean inCombat()
	{
		if (wasEngagingPlayer() || wasEngagingNPC())
		{
			return true;
		}
		return false;
	}

	/**
	 * Attack Npcs
	 */

	public int[][] slayerReqs = { //Slayer level required to attack these npcs
			{
				1643, 45 // Infernal mage
			},
			{
				1618, 50 // Bloodveld
			},
			{
				1624, 65 // Dustdevil
			},
			{
				1610, 75 // Gargoyle
			},
			{
				1613, 80 // Nechryael
			},
			{
				1615, 85 // Abyssal demon
			},
			{
				2783, 90 // Dark beast
			}
	};

	public void attackNpc(NPC npc)
	{
		if (npc == null)
		{
			return;
		}

		strBonus = player.playerBonus[10];
		if (npc.isDead || npc.MaxHP <= 0)
		{
			player.usingMagic = false;
			player.faceUpdate(0);
			player.npcIndex = 0;
			return;
		}
		if (player.isDead)
		{
			player.npcIndex = 0;
			return;
		}
		if (npc.underAttackBy > 0 && npc.underAttackBy != player.playerId && !npc.inMulti())
		{
			resetPlayerAttack();
			player.npcIndex = 0;
			player.sendMessage("This monster is already in combat.");
			return;
		}
		if ((player.underAttackBy > 0 || player.underAttackBy2 > 0) && player.underAttackBy2 != npc.npcId && !player.inMulti())
		{
			resetPlayerAttack();
			player.sendMessage("I am already under attack.");
			return;
		}
		player.followId = 0;
		player.followId2 = npc.npcId;
		if (player.attackTimer <= 0)
		{
			boolean usingBow = false;
			boolean usingArrows = false;
			boolean usingOtherRangeWeapons = false;
			boolean usingCross = player.playerEquipment[Constants.WEAPON_SLOT] == 9185 || player.playerEquipment[Constants.WEAPON_SLOT] == 18357;
			player.bonusAttack = 0;
			player.rangeItemUsed = 0;
			player.projectileStage = 0;
			if (player.autocasting)
			{
				player.spellId = player.autocastId;
				player.usingMagic = true;
			}
			if (player.spellId > 0)
			{
				player.usingMagic = true;
			}
			player.attackTimer = getAttackDelay(ItemAssistant.getItemName(player.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
			player.specAccuracy = 1.0;
			player.specDamage = 1.0;
			if (!player.usingMagic)
			{
				for (int bowId: player.BOWS)
				{
					if (player.playerEquipment[Constants.WEAPON_SLOT] == bowId)
					{
						usingBow = true;
						for (int arrowId: player.ARROWS)
						{
							if (player.playerEquipment[Constants.ARROW_SLOT] == arrowId)
							{
								usingArrows = true;
							}
						}
					}
				}
				for (int otherRangeId: player.OTHER_RANGE_WEAPONS)
				{
					if (player.playerEquipment[Constants.WEAPON_SLOT] == otherRangeId)
					{
						usingOtherRangeWeapons = true;
					}
				}
			}
			if (armaNpc(npc.npcId) && !usingCross && !usingBow && !player.usingMagic && !usingCrystalBow() && !usingOtherRangeWeapons)
			{
				player.sendMessage("You can only use range against this.");
				resetPlayerAttack();
				return;
			}
			if ((!player.goodDistance(player.getX(), player.getY(), npc.getX(), npc.getY(), 3) && (usingHally() && !usingOtherRangeWeapons && !usingBow && !player.usingMagic)) ||
					(!player.goodDistance(player.getX(), player.getY(), npc.getX(), npc.getY(), 4) && (usingOtherRangeWeapons && !usingBow && !player.usingMagic)) ||
					((!player.goodDistance(player.getX(), player.getY(), npc.getX(), npc.getY(), 8) && (usingBow || player.usingMagic))))
			{
				player.attackTimer = 2;
				return;
			}
			if (!player.goodDistance(player.getX(), player.getY(), npc.getX(), npc.getY(), NPCSize.getSize()[npc.npcType]) && (!usingOtherRangeWeapons && !usingHally() && !usingBow && !player.usingMagic))
			{
				player.attackTimer = 2;
				return;
			}
			if (!usingCross && !usingArrows && usingBow)
			{
				player.sendMessage("You have run out of arrows!");
				Movement.stopMovement(player);
				player.npcIndex = 0;
				return;
			}
			if (correctBowAndArrows() < player.playerEquipment[Constants.ARROW_SLOT] && usingBow && !usingCrystalBow() && player.playerEquipment[Constants.WEAPON_SLOT] != 9185 && player.playerEquipment[Constants.WEAPON_SLOT] != 18357)
			{
				player.sendMessage("You can't use " + ItemAssistant.getItemName(player.playerEquipment[Constants.ARROW_SLOT]).toLowerCase() + "s with a " + ItemAssistant.getItemName(player.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase() + ".");
				Movement.stopMovement(player);
				player.npcIndex = 0;
				return;
			}
			if ((player.playerEquipment[Constants.WEAPON_SLOT] == 9185 || player.playerEquipment[Constants.WEAPON_SLOT] == 18357) && !properBolts())
			{
				player.sendMessage("You must use bolts with a crossbow.");
				Movement.stopMovement(player);
				resetPlayerAttack();
				return;
			}
			if (usingBow || player.usingMagic || usingOtherRangeWeapons || (player.goodDistance(player.getX(), player.getY(), npc.getX(), npc.getY(), 2) && usingHally()))
			{
				Movement.stopMovement(player);
			}
			if (!checkMagicReqs(player.spellId))
			{
				Movement.stopMovement(player);
				player.npcIndex = 0;
				return;
			}
			player.faceUpdate(npc.npcId);
			npc.underAttackBy = player.playerId;
			npc.lastDamageTaken = System.currentTimeMillis();
			if (player.usingSpecial && !player.usingMagic)
			{
				if (checkSpecAmount(player.playerEquipment[Constants.WEAPON_SLOT]))
				{
					player.lastWeaponUsed = player.playerEquipment[Constants.WEAPON_SLOT];
					player.lastArrowUsed = player.playerEquipment[Constants.ARROW_SLOT];
					SpecialAttack.activateSpecial(player, player.playerEquipment[Constants.WEAPON_SLOT], npc.npcId);
					return;
				}
				else
				{
					player.sendMessage("You don't have the required special energy to use this attack.");
					player.usingSpecial = false;
					ItemAssistant.updateSpecialBar(player);
					player.npcIndex = 0;
					return;
				}
			}
			if (!player.usingMagic)
			{
				player.startAnimation(weaponAttackAnimation(ItemAssistant.getItemName(player.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase()));
				ItemDegrade.doDegrade(player, 1);
			}
			else
			{
				player.startAnimation(player.MAGIC_SPELLS[player.spellId][2]);
				ItemDegrade.doDegrade(player, 1);
			}
			int max = finalMagicDamage(player);
			player.magicDamage1 = Misc.random2(max);
			player.maxMageDamage = max;
			player.lastWeaponUsed = player.playerEquipment[Constants.WEAPON_SLOT];
			player.lastArrowUsed = player.playerEquipment[Constants.ARROW_SLOT];
			if (!usingBow && !player.usingMagic && !usingOtherRangeWeapons)
			{ // melee hit delay
				player.hitDelay = getHitDelay(ItemAssistant.getItemName(player.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
				player.projectileStage = 0;
				player.oldNpcIndex = npc.npcId;
			}
			if (usingBow && !usingOtherRangeWeapons && !player.usingMagic || usingCross)
			{ // range hit delay					
				if (usingCross) player.usingBow = true;
				if (player.fightMode == player.RAPID) player.attackTimer--;
				player.lastArrowUsed = player.playerEquipment[Constants.ARROW_SLOT];
				player.lastWeaponUsed = player.playerEquipment[Constants.WEAPON_SLOT];
				player.gfx100(getRangeStartGFX());
				player.hitDelay = getHitDelay(ItemAssistant.getItemName(player.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
				player.projectileStage = 1;
				player.oldNpcIndex = npc.npcId;
				if (player.playerEquipment[Constants.WEAPON_SLOT] >= 4212 && player.playerEquipment[Constants.WEAPON_SLOT] <= 4223)
				{
					player.rangeItemUsed = player.playerEquipment[Constants.WEAPON_SLOT];
					player.lastArrowUsed = 0;
				}
				else
				{
					player.rangeItemUsed = player.playerEquipment[Constants.ARROW_SLOT];
					ItemAssistant.deleteArrow(player);
				}
				fireProjectileNpc();
			}
			if (usingOtherRangeWeapons && !player.usingMagic && !usingBow)
			{ // knives, darts, etc hit delay		
				player.rangeItemUsed = player.playerEquipment[Constants.WEAPON_SLOT];
				ItemAssistant.deleteEquipment(player);
				player.gfx100(getRangeStartGFX());
				player.lastArrowUsed = 0;
				player.hitDelay = getHitDelay(ItemAssistant.getItemName(player.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
				player.projectileStage = 1;
				player.oldNpcIndex = npc.npcId;
				if (player.fightMode == player.RAPID) player.attackTimer--;
				fireProjectileNpc();
			}
			if (player.usingMagic)
			{ // magic hit delay
				int pX = player.getX();
				int pY = player.getY();
				int nX = npc.getX();
				int nY = npc.getY();
				int offX = (pY - nY) * -1;
				int offY = (pX - nX) * -1;
				player.castingMagic = true;
				player.projectileStage = 2;
				if (player.MAGIC_SPELLS[player.spellId][3] > 0)
				{
					if (getStartGfxHeight() == 100)
					{
						player.gfx100(player.MAGIC_SPELLS[player.spellId][3]);
					}
					else
					{
						player.gfx0(player.MAGIC_SPELLS[player.spellId][3]);
					}
				}
				if (player.MAGIC_SPELLS[player.spellId][4] > 0)
				{
					player.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 78, player.MAGIC_SPELLS[player.spellId][4], getStartHeight(), getEndHeight(), npc.npcId + 1, 50);
				}
				player.hitDelay = getHitDelay(ItemAssistant.getItemName(player.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
				player.oldNpcIndex = npc.npcId;
				player.oldSpellId = player.spellId;
				player.spellId = 0;
				if (!player.autocasting)
				{
					player.npcIndex = 0;
				}
			}
		}
	}
	public void delayedHit(int i)
	{ // npc hit delay
		if (NPCHandler.npcs[i] != null)
		{
			if (NPCHandler.npcs[i].isDead)
			{
				player.npcIndex = 0;
				player.dBowHits = 0;
				player.usingDarkBowSpecialAttack = false;
				return;
			}
			NPCHandler.npcs[i].facePlayer(player.playerId);
			if (NPCHandler.npcs[i].underAttackBy > 0)
			{
				NPCHandler.npcs[i].killerId = player.playerId;
			}
			player.lastNpcAttacked = i;

			if (player.projectileStage == 0)
			{ // melee hit damage

				if (!player.adminHax)
				{
					applyNpcMeleeDamage(i, 1, Misc.random(player.getCombat().calculateMeleeMaxHit()));
				}
				else
				{
					applyNpcMeleeDamage(i, 1, player.getCombat().calculateMeleeMaxHit());
				}

			if (player.doubleHit)
			{

				if (!player.adminHax)
				{
					applyNpcMeleeDamage(i, 2, Misc.random(player.getCombat().calculateMeleeMaxHit()));
				}
				else
				{
					applyNpcMeleeDamage(i, 2, player.getCombat().calculateMeleeMaxHit());
				}

			}
			}
			if (!player.castingMagic && player.projectileStage > 0)
			{ // range hit damage
				int damage = Misc.random(rangeMaxHit());
				int damage2 = -1;
				if (player.lastWeaponUsed == 11235 || player.bowSpecShot == 1)
				{
					if (Misc.random(5) >= 1)
					{
						damage2 = 0;
					}
					else
					{
						damage2 = Misc.random(rangeMaxHit());
					}
				}
				boolean ignoreDef = false;
				if (Misc.random(9) == 1 && player.lastArrowUsed == 9243)
				{
					ignoreDef = true;
					NPCHandler.npcs[i].gfx0(758);
				}
				if (Misc.random(NPCHandler.npcs[i].defence) > Misc.random(10 + calculateRangeAttack()) && !ignoreDef)
				{
					damage = 0;
				}
				else if (NPCHandler.npcs[i].npcType == 2881 || NPCHandler.npcs[i].npcType == 2883 && !ignoreDef)
				{
					damage = 0;
				}
				if (Misc.random(11) == 1 && player.lastArrowUsed == 9242 && damage > 0 && player.playerEquipment[Constants.WEAPON_SLOT] == 9185)
				{
					NPCHandler.npcs[i].gfx0(754);
					damage = NPCHandler.npcs[i].HP / 5;
					player.dealDamage(player.skillLevel[3] / 10);
					player.gfx0(754);
				}
				if (Misc.random(7) == 1 && player.lastArrowUsed == 9242 && damage > 0 && player.playerEquipment[Constants.WEAPON_SLOT] == 18357)
				{
					NPCHandler.npcs[i].gfx0(754);
					damage = NPCHandler.npcs[i].HP / 5;
					player.dealDamage(player.skillLevel[3] / 10);
					player.gfx0(754);
				}
				if (player.bowSpecShot == 1)
				{
					if (Misc.random(NPCHandler.npcs[i].defence) > Misc.random(10 + calculateRangeAttack())) damage2 = 0;
				}
				if (player.usingDarkBowSpecialAttack)
				{
					NPCHandler.npcs[i].gfx100(1100);
					player.dBowHits++;
					if (damage < 8) damage = 8;
					if (player.dBowHits == 2)
					{
						player.usingDarkBowSpecialAttack = false;
						player.dBowHits = 0;
					}
				}
				if (damage > 0 && Misc.random(9) == 1 && player.lastArrowUsed == 9244 && player.playerEquipment[Constants.WEAPON_SLOT] == 9185) // Rune c'bow
				{
					damage *= 1.55;
					NPCHandler.npcs[i].gfx0(756);
				}
				else if (damage > 0 && Misc.random(6) == 1 && player.lastArrowUsed == 9244 && player.playerEquipment[Constants.WEAPON_SLOT] == 18357) // Chaotic c'bow
				{
					damage *= 1.55;
					NPCHandler.npcs[i].gfx0(756);
				}
				if (NPCHandler.npcs[i].HP - damage < 0)
				{
					damage = NPCHandler.npcs[i].HP;
				}
				if (NPCHandler.npcs[i].HP - damage <= 0 && damage2 > 0)
				{
					damage2 = 0;
				}
				if (damage > 0)
				{
					if (NPCHandler.npcs[i].npcType >= 3777 && NPCHandler.npcs[i].npcType <= 3780)
					{
						player.pcDamage += damage;
					}
				}
				boolean dropArrows = true;
				for (int noArrowId: player.NO_ARROW_DROP)
				{
					if (player.lastWeaponUsed == noArrowId)
					{
						dropArrows = false;
						break;
					}
				}
				if (dropArrows)
				{
					ItemAssistant.dropArrowNpc(player);
				}
				NPCHandler.npcs[i].underAttack = true;
				appendHit(NPCHandler.npcs[i], damage, 0, 1, 1);
				addCombatXP(1, damage);
				if (damage2 > -1)
				{
					appendHit(NPCHandler.npcs[i], damage2, 0, 1, 2);
					player.totalDamageDealt += damage2;
					addCombatXP(1, damage2);
				}
				if (player.killingNpcIndex != player.oldNpcIndex)
				{
					player.totalDamageDealt = 0;
				}
				player.killingNpcIndex = player.oldNpcIndex;
				player.totalDamageDealt += damage;
				player.curses().vsNpcSoulSplit(i, damage, false);
			}
			else if (player.projectileStage > 0)
			{ // magic hit damage
				int damage = player.magicDamage1;
				if (godSpells())
				{
					damage += Misc.random(10);
				}
				boolean magicFailed = false;
				int bonusAttack = getBonusAttack(i);
				if (Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc.random(mageAtk()) + bonusAttack)
				{
					damage = 0;
					magicFailed = true;
				}
				else if (NPCHandler.npcs[i].npcType == 2881 || NPCHandler.npcs[i].npcType == 2882)
				{
					damage = 0;
					magicFailed = true;
				}
				if (NPCHandler.npcs[i].inMulti() && multis())
				{
					player.barrageCount = 0;
					for (int j = 0; j < NPCHandler.npcs.length; j++)
					{
						if (NPCHandler.npcs[j] != null)
						{
							if (player.barrageCount >= 9) break;
							NPCHandler.npcs[i].getY();
						}
					}
				}
				if (NPCHandler.npcs[i].HP - damage < 0)
				{
					damage = NPCHandler.npcs[i].HP;
				}
				player.getPA().refreshSkill(3);
				player.getPA().refreshSkill(6);
				if (damage > 0)
				{
					if (NPCHandler.npcs[i].npcType >= 3777 && NPCHandler.npcs[i].npcType <= 3780)
					{
						player.pcDamage += damage;
					}
				}
				if (getEndGfxHeight() == 100 && !magicFailed)
				{ // end GFX
					NPCHandler.npcs[i].gfx100(player.MAGIC_SPELLS[player.oldSpellId][5]);
				}
				else if (!magicFailed)
				{
					NPCHandler.npcs[i].gfx0(player.MAGIC_SPELLS[player.oldSpellId][5]);
				}
				if (magicFailed)
				{
					NPCHandler.npcs[i].gfx100(85);
				}
				if (!magicFailed)
				{
					int freezeDelay = getFreezeTime(); //freeze 
					if (freezeDelay > 0 && NPCHandler.npcs[i].freezeTimer == 0)
					{
						NPCHandler.npcs[i].freezeTimer = freezeDelay;
					}
					switch (player.MAGIC_SPELLS[player.oldSpellId][0])
					{
					case 12901:
					case 12919:
						// blood spells
					case 12911:
					case 12929:
						int heal = Misc.random(damage / 2);
						player.addToHitPoints(heal);
						break;
					}
				}
				NPCHandler.npcs[i].underAttack = true;
				if (player.MAGIC_SPELLS[player.oldSpellId][6] != 0)
				{
					appendHit(NPCHandler.npcs[i], damage, 0, 2, 1);
					addCombatXP(2, damage);
					player.totalDamageDealt += damage;
				}
				else
				{
					addCombatXP(1, 0);
				}
				player.curses().vsNpcSoulSplit(i, damage, false);
				player.killingNpcIndex = player.oldNpcIndex;
				NPCHandler.npcs[i].updateRequired = true;
				player.usingMagic = false;
				player.castingMagic = false;
				player.oldSpellId = 0;
			}
		}
		if (player.bowSpecShot <= 0)
		{
			player.oldNpcIndex = 0;
			player.projectileStage = 0;
			player.doubleHit = false;
			player.lastWeaponUsed = 0;
			player.bowSpecShot = 0;
		}
		if (player.bowSpecShot >= 2)
		{
			player.bowSpecShot = 0;
		}
		if (player.bowSpecShot == 1)
		{
			fireProjectileNpc();
			player.hitDelay = 2;
			player.bowSpecShot = 0;
		}
		//GameSound.getCombatSound(player);
	}
	public void applyNpcMeleeDamage(int i, int damageMask, int damage)
	{
		boolean fullVeracsEffect = player.getPA().fullVeracs() && Misc.random(3) == 1;
		if (NPCHandler.npcs[i].HP - damage < 0)
		{
			damage = NPCHandler.npcs[i].HP;
		}
		if (!player.adminHax)
		{
			if (!fullVeracsEffect)
			{
				if (Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc.random(calculateMeleeAttack()))
				{
					damage = 0;
				}
				else if (NPCHandler.npcs[i].npcType == 2882 || NPCHandler.npcs[i].npcType == 2883)
				{
					damage = 0;
				}
			}
		}
		boolean guthansEffect = false;
		if (player.getPA().fullGuthans())
		{
			if (Misc.random(3) == 1)
			{
				guthansEffect = true;
			}
		}
		if (damage > 0)
		{
			if (NPCHandler.npcs[i].npcType >= 3777 && NPCHandler.npcs[i].npcType <= 3780)
			{
				player.pcDamage += damage;
			}
		}
		if (damage > 0 && guthansEffect)
		{
			player.addToHitPoints(damage);
			NPCHandler.npcs[i].gfx0(398);
		}
		if (damage > 0 && PestControl.isInGame(player)) {
			if (NPCHandler.npcs[i].npcType >= 6142 && NPCHandler.npcs[i].npcType <= 6145) {
				player.pcDamage += damage;
			}
		}
		int hitIcon = 0;
		NPCHandler.npcs[i].underAttack = true;
		player.killingNpcIndex = player.npcIndex;
		player.lastNpcAttacked = i;
		switch (player.specEffect)
		{
		case 10:
			player.hit1 = damage;
			if (player.hit1 > 0) player.hit2 = player.hit1 / 2;
			else player.hit2 = Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc.random(calculateMeleeAttack()) ? Misc.random(calculateMeleeMaxHit()) : 0;
			if (player.hit2 > 0) player.hit3 = player.hit2 / 2;
			else player.hit3 = Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc.random(calculateMeleeAttack()) ? Misc.random(calculateMeleeMaxHit()) : 0;
			if (player.hit3 > 0) player.hit4 = player.hit3 + 1;
			else player.hit4 = Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc.random(calculateMeleeAttack()) ? Misc.random(calculateMeleeMaxHit()) : 1;
			appendHit(NPCHandler.npcs[i], player.hit2, 0, 0, damageMask == 1 ? 2 : 1);
			player.clawTargNPC = i;


			CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
			{@
				Override
				public void execute(CycleEventContainer container)
			{
				container.stop();
			}@
			Override
			public void stop()
			{
				player.getCombat().appendHit(NPCHandler.npcs[player.clawTargNPC], player.hit3, 0, 0, 1);
				player.getCombat().appendHit(NPCHandler.npcs[player.clawTargNPC], player.hit4, 0, 0, 2);
			}
			}, 1);

			break;
		case 666:
			hitIcon = 2;
			break;
		}
		appendHit(NPCHandler.npcs[i], damage, 0, hitIcon, damageMask);
		addCombatXP(0, damage);
		player.specEffect = 0;
		player.totalDamageDealt += damage;
		player.curses().vsNpcSoulSplit(i, damage, false);
	}
	public void fireProjectileNpc()
	{
		if (player.oldNpcIndex > 0)
		{
			if (NPCHandler.npcs[player.oldNpcIndex] != null)
			{
				player.projectileStage = 2;
				int pX = player.getX();
				int pY = player.getY();
				int nX = NPCHandler.npcs[player.oldNpcIndex].getX();
				int nY = NPCHandler.npcs[player.oldNpcIndex].getY();
				int offX = (pY - nY) * -1;
				int offY = (pX - nX) * -1;
				player.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, getProjectileSpeed(), getRangeProjectileGFX(), 43, 31, player.oldNpcIndex + 1, getStartDelay());
				if (usingDbow()) player.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, getProjectileSpeed() - 20, getRangeProjectileGFX(), 43, 31, player.oldNpcIndex + 1, getStartDelay(), 29);
			}
		}
	}

	public boolean usingCrystalBow()
	{
		return player.playerEquipment[Constants.WEAPON_SLOT] >= 4212 && player.playerEquipment[Constants.WEAPON_SLOT] <= 4223;
	}
	public void appendHit(Player player, int damage, int mask, int icon, boolean playerHitting)
	{
		int soak = 0;
		boolean maxHit = false;
		if (playerHitting)
		{
			switch (icon)
			{
			case 0:
				maxHit = damage >= calculateMeleeMaxHit() * 0.98;
				break;
			case 1:
				maxHit = damage >= rangeMaxHit() * 0.98;
				break;
			case 2:
				maxHit = damage >= player.maxMageDamage * 0.98;
				break;
			}
		}
		damage -= soak;
		player.handleHitMask(damage, mask, icon, soak, maxHit);
		player.dealDamage(damage);
	}


	/**
	 *
	 * @param attacker
	 * 			The player dealing the hitsplat.
	 * @param theVictim
	 * 			The player receiving the hitsplat.
	 * @param damage
	 * 			The damage dealt.
	 * @param mask
	 * @param icon
	 * 			The icon type. 0 is Blocked, 1 is Ranged, 2 is Magic
	 */
	 public void createHitsplatOnPlayer(Player attacker, Player victim, int damage, int mask, int icon)
	{
		boolean maxHit = false;
		switch (icon)
		{
		case 0:
			maxHit = damage >= attacker.maximumDamageMelee * 0.98;
			break;
		case 1:
			maxHit = damage >= attacker.maximumDamageRange * 0.98;
			break;
		case 2:
			maxHit = damage >= attacker.maximumDamageMagic * 0.98;
			break;
		}
		victim.handleHitMask(damage, mask, icon, 0, maxHit);
		victim.dealDamage(damage);
		victim.getPA().refreshSkill(3);
	}

	public void appendHit(NPC n, int damage, int mask, int icon, int damageMask) {
		if (n.npcType == 14301) {
			if (!n.glacytesSpawned && n.HP < (n.MaxHP / 2)) {
				n.glacytesSpawned = true;
				Server.npcHandler.spawnGlacytes(n);
			}
			if (n.glacytesSpawned) {
				if (checkGlacytes(n)) {
					damage = 0;
					icon = 0;
				}
			}
		}
		if (damage > n.HP) {
			damage = n.HP;
		}
		n.HP -= damage;
		if (n.HP > 0) {
			if (System.currentTimeMillis() - n.lastBlock > 2000) {
				int blockAnim = Server.npcHandler.getNpcBlockAnimation(n.npcType);
				if (blockAnim > -1) {
					Server.npcHandler.startAnimation(blockAnim, n.npcId);
				}
				n.lastBlock = System.currentTimeMillis();
			}
		}
		
		boolean maxHit = false;
		switch (icon)
		{
		case 0:
			maxHit = damage >= calculateMeleeMaxHit() * 0.98;
			break;
		case 1:
			maxHit = damage >= rangeMaxHit() * 0.98;
			break;
		case 2:
			maxHit = damage >= player.maxMageDamage * 0.98;
			break;
		}
		if (maxHit) mask = 1;
		switch (damageMask)
		{
		case 1:
			n.hitDiff = damage;
			n.hitUpdateRequired = true;
			n.updateRequired = true;
			n.hitIcon = icon;
			n.hitMask = mask;
			break;
		case 2:
			n.hitDiff2 = damage;
			n.hitUpdateRequired2 = true;
			n.updateRequired = true;
			player.doubleHit = false;
			n.hitIcon2 = icon;
			n.hitMask2 = mask;
			break;
		}
		if (n.npcType >= 6142 && n.npcType <= 6145) {
			PestControl.updatePortalHealth(n);
		}
	}
	public void appendHit(Client c2, int damage, int mask, int icon, boolean playerHitting, int soak)
	{
		boolean maxHit = false;
		if (playerHitting)
		{
			switch (icon)
			{
			case 0:
				maxHit = damage >= calculateMeleeMaxHit() * 0.98;
				break;
			case 1:
				maxHit = damage >= rangeMaxHit() * 0.98;
				break;
			case 2:
				maxHit = damage >= player.maxMageDamage * 0.98;
				break;
			}
		}
		if (damage > c2.skillLevel[3])
		{
			damage = c2.skillLevel[3];
		}
		c2.handleHitMask(damage, mask, icon, soak, maxHit);
		c2.dealDamage(damage);
	}
	
	
	public static void pestControlKnightHit(NPC n, int damage, int mask, int icon, int damageMask) {
		if (damage > n.HP) {
			damage = n.HP;
		}
		n.HP -= damage;
		n.hitDiff = damage;
		n.hitUpdateRequired = true;
		n.updateRequired = true;
		n.hitIcon = icon;
		n.hitMask = mask;
	}
	
	public boolean checkGlacytes(NPC n) {
			boolean sent = false;
			for (int i = 0; i < 3; i++) {
				if (!n.glacytes[i].isDead && NPCHandler.npcs[n.glacytes[i].npcId] != null) {
					Player c = PlayerHandler.players[n.killerId];
					if (c != null) {
						if (!sent) {
							c.sendMessage("This Glacor cannot be damaged until the Glacytes have been killed");
							sent = true;
						}
					}
				}
			}
		return sent;
	}
	
	
	
	public void appendVengeance(Player victim, int damage)
	{
		if (damage <= 0) return;
		victim.forcedText = "Taste vengeance!";
		victim.forcedChatUpdateRequired = true;
		victim.updateRequired = true;
		victim.vengOn = false;
		damage = (int)(damage * 0.75);
		if (damage > player.skillLevel[3])
		{
			damage = player.skillLevel[3];
		}
		appendHit(player, damage, 0, 8, false);
		player.getPA().refreshSkill(3);
		player.updateRequired = true;
	}
	public void playerDelayedHit(int theTarget)
	{
		Client target = (Client) PlayerHandler.players[theTarget];
		if (target == null)
		{
			return;
		}
		if (target.isTeleporting())
		{
			return;
		}
		if (target.isDead)
		{
			player.faceUpdate(0);
			player.playerIndex = 0;
			return;
		}

		target.getPA().removeAllWindows();
		player.getPA().removeAllWindows();

		if (target.playerIndex <= 0 && target.npcIndex <= 0)
		{
			if (target.autoRet == 1)
			{
				target.playerIndex = player.playerId;
			}
		}
		if (target.attackTimer <= 3 || target.attackTimer == 0 && target.playerIndex == 0 && !player.castingMagic && !player.getDoingAgility())
		{ // block animation
			target.startAnimation(target.getCombat().getBlockAnimation());
		}
		if (target.inTrade)
		{
			target.getTradeAndDuel().declineTrade();
		}

		/* Player apply melee hitsplat */
		if (player.projectileStage == 0)
		{
			MeleeApplyDamage.applyDamage(player, target, true);
			if (player.getMultipleDamageSpecialAttack())
			{
				MeleeApplyDamage.applyDamage(player, target, false);
			}
		}

		/* Player apply range hitsplat */
		else if (!player.castingMagic && player.projectileStage > 0)
		{
			RangeApplyDamage.applyRangeDamage(player, theTarget, 1);
			if (player.magicBowSpecialAttack || player.usingDarkBowSpecialAttack || player.usingDarkBowNormalAttack)
			{
				RangeApplyDamage.applyRangeDamage(player, theTarget, 2);
			}
		}

		/* Player apply magic hitsplat */
		else if (player.projectileStage > 0)
		{
			MagicApplyDamage.applyDamage(player, theTarget);
		}
		player.getPA().requestUpdates();
		if (player.bowSpecShot <= 0)
		{
			player.oldPlayerIndex = 0;
			player.projectileStage = 0;
			player.lastWeaponUsed = 0;
			player.bowSpecShot = 0;
		}
		if (player.bowSpecShot != 0)
		{
			player.bowSpecShot = 0;
		}
		//GameSound.getCombatSound(player);
	}
	public boolean multis()
	{
		switch (player.MAGIC_SPELLS[player.oldSpellId][0])
		{
		case 12891:
		case 12881:
		case 13011:
		case 13023:
		case 12919:
			// blood spells
		case 12929:
		case 12963:
		case 12975:
			return true;
		}
		return false;
	}
	public void appendMultiBarrage(int playerId, boolean splashed)
	{
		if (PlayerHandler.players[playerId] != null)
		{
			Client c2 = (Client) PlayerHandler.players[playerId];
			if (c2.isDead) return;
			if (checkMultiBarrageReqs(playerId))
			{
				player.barrageCount++;
				if (Misc.random(mageAtk()) > Misc.random(mageDef()) && !player.magicSplash)
				{
					int spellGFX = player.MAGIC_SPELLS[player.spellId][5];
					if (spellGFX == 369 && c2.orb)
					{ //ORB
						spellGFX = 1677;
					}
					c2.orb = true;
					if (getEndGfxHeight() == 100)
					{ // end GFX
						c2.gfx100(player.MAGIC_SPELLS[player.oldSpellId][5]);
					}
					else
					{
						if (spellGFX == 1677) c2.gfx0(spellGFX);
						else c2.gfx0(player.MAGIC_SPELLS[player.oldSpellId][5]);
					}
					int damage = Misc.random(finalMagicDamage(player));
					if (c2.prayerActive[12])
					{
						damage *= (int)(.60);
					}
					if (c2.skillLevel[3] - damage < 0)
					{
						damage = c2.skillLevel[3];
					}
					appendHit(c2, damage, 0, 2, true);
					addCombatXP(2, damage);
					PlayerHandler.players[playerId].damageTaken[player.playerId] += damage;
					player.totalPlayerDamageDealt += damage;
					multiSpellEffect(playerId, damage);
				}
				else
				{
					c2.gfx100(85);
				}
			}
		}
	}
	/*
    public void appendMultiBarrageNPC(int npcId, boolean splashed)
    {
        if (NPCHandler.npcs[npcId] != null)
        {
            NPC n = NPCHandler.npcs[npcId];
            if (n.isDead || n.HP <= 0) return;
            if (checkMultiBarrageReqsNPC(npcId))
            {
                c.barrageCount++;
                if (Misc.random(NPCHandler.npcs[npcId].defence) < (10 + Misc.random(mageAtk())) && !c.magicFailed)
                {
                    if (getEndGfxHeight() == 100) n.gfx100(c.MAGIC_SPELLS[c.oldSpellId][5]);
                    else n.gfx0(c.MAGIC_SPELLS[c.oldSpellId][5]);
                    int damage = Misc.random(finalMagicDamage(c));
                    if (n.HP - damage < 0)
                    {
                        damage = n.HP;
                    }
                    appendHit(n, damage, 0, 2, 1);
                    addCombatXP(2, damage);
                    n.underAttackBy = c.playerId;
                    n.underAttack = true;
                    c.totalDamageDealt += damage;
                    multiSpellEffectNPC(npcId, damage);
                }
                else n.gfx100(85);
            }
        }
    }
	 */
	public void multiSpellEffect(int playerId, int damage)
	{
		switch (player.MAGIC_SPELLS[player.oldSpellId][0])
		{
		case 13011:
		case 13023:
			if (System.currentTimeMillis() - PlayerHandler.players[playerId].reduceStat > 35000)
			{
				PlayerHandler.players[playerId].reduceStat = System.currentTimeMillis();
				PlayerHandler.players[playerId].skillLevel[0] -= ((PlayerHandler.players[playerId].getLevelForXP(PlayerHandler.players[playerId].playerXP[0]) * 10) / 100);
			}
			break;
		case 12919:
			// blood spells
		case 12929:
			int heal = (int)(damage / 4);
			player.addToHitPoints(heal);
			break;
		case 12891:
		case 12881:
			if (PlayerHandler.players[playerId].freezeTimer < -4)
			{
				PlayerHandler.players[playerId].freezeTimer = getFreezeTime();
				Movement.stopMovement(PlayerHandler.players[playerId]);
			}
			break;
		}
	}
	public void multiSpellEffectNPC(int npcId, int damage)
	{
		switch (player.MAGIC_SPELLS[player.oldSpellId][0])
		{
		case 12919:
			// blood spells
		case 12929:
			int heal = (int)(damage / 4);
			player.addToHitPoints(heal);
			break;
		case 12891:
		case 12881:
			if (NPCHandler.npcs[npcId].freezeTimer == 0)
			{
				NPCHandler.npcs[npcId].freezeTimer = getFreezeTime();
			}
			break;
		}
	}

	public void applySmite(Player victim, int damage)
	{
		if (player.skillLevel[5] < 0)
		{
			player.skillLevel[5] = 0;
		}
		if (!player.prayerActive[23] && !player.curseActive[18])
		{
			return;
		}
		if (damage <= 0)
		{
			return;
		}
		if (victim != null)
		{
			if (player.curseActive[18] && !player.prayerActive[23])
			{
				int heal = (int)(damage / 50);
				player.addToHitPoints(heal);
			}
			if (player.curseActive[18])
			{
				victim.skillLevel[5] -= (int)(damage / 40);
			}
			else if (player.prayerActive[23])
			{
				victim.skillLevel[5] -= (int)(damage / 4);
			}
			if (victim.skillLevel[5] <= 0)
			{
				victim.skillLevel[5] = 0;
				victim.getCombat().resetPrayers();
			}
			victim.getPA().refreshSkill(5);
		}
	}

	public void fireProjectilePlayer()
	{
		if (player.oldPlayerIndex > 0)
		{
			if (PlayerHandler.players[player.oldPlayerIndex] != null)
			{
				player.projectileStage = 2;
				final int pX = player.getX();
				final int pY = player.getY();
				int oX = PlayerHandler.players[player.oldPlayerIndex].getX();
				int oY = PlayerHandler.players[player.oldPlayerIndex].getY();
				final int offX = (pY - oY) * -1;
				final int offY = (pX - oX) * -1;
				if (!player.msbSpec)
				{
					if (player.handCannonSpecialAttack)
					{
						CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
						{@
							Override
							public void execute(CycleEventContainer container)
						{
							player.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, getProjectileSpeed(), getRangeProjectileGFX(), 43, 31, -player.oldPlayerIndex - 1, getStartDelay(), 16);
							container.stop();
						}@
						Override
						public void stop()
						{

						}
						}, 2);
					}
					player.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, getProjectileSpeed(), getRangeProjectileGFX(), 43, 31, -player.oldPlayerIndex - 1, getStartDelay(), 16);
				}
				else if (player.msbSpec)
				{
					player.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, getProjectileSpeed(), getRangeProjectileGFX(), 43, 31, -player.oldPlayerIndex - 1, getStartDelay(), 10);
					player.msbSpec = false;
				}
				if (usingDbow()) 
				{
					player.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, getProjectileSpeed(), getRangeProjectileGFX(), 60, 31, -player.oldPlayerIndex - 1, getStartDelay(), 30);
				}
			}
		}
	}
	public boolean usingDbow()
	{
		return player.playerEquipment[Constants.WEAPON_SLOT] == 11235;
	}
	/* Prayer */
	public void activatePrayer(int i)
	{
		if (player.Prayerbook == 1 || player.isDead)
		{
			return;
		}
		if (player.duelRule[7])
		{
			for (int p = 0; p < player.PRAYER.length; p++)
			{
				player.prayerActive[p] = false;
				player.getPA().sendFrame36(player.PRAYER_GLOW[p], 0);
			}
			player.sendMessage("Prayer has been disabled in this duel!");
			return;
		}
		if (i == 24 && player.skillLevel[1] < 65)
		{
			player.getPA().sendFrame36(player.PRAYER_GLOW[i], 0);
			player.sendMessage("You may not use this prayer yet.");
			return;
		}
		if (i == 25 && player.skillLevel[1] < 70)
		{
			player.getPA().sendFrame36(player.PRAYER_GLOW[i], 0);
			player.sendMessage("You may not use this prayer yet.");
			return;
		}
		int[] defPray = {
				0, 5, 13, 24, 25
		};
		int[] strPray = {
				1, 6, 14, 24, 25
		};
		int[] atkPray = {
				2, 7, 15, 24, 25
		};
		int[] rangePray = {
				3, 11, 19
		};
		int[] magePray = {
				4, 12, 20
		};
		if (player.skillLevel[5] > 0)
		{
			if (player.getPA().getLevelForXP(player.playerXP[5]) >= player.PRAYER_LEVEL_REQUIRED[i])
			{
				boolean headIcon = false;
				switch (i)
				{
				case 0:
				case 5:
				case 13:
					if (player.prayerActive[i] == false)
					{
						for (int j = 0; j < defPray.length; j++)
						{
							if (defPray[j] != i)
							{
								player.prayerActive[defPray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[defPray[j]], 0);
							}
						}
					}
					break;
				case 1:
				case 6:
				case 14:
					if (player.prayerActive[i] == false)
					{
						for (int j = 0; j < strPray.length; j++)
						{
							if (strPray[j] != i)
							{
								player.prayerActive[strPray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++)
						{
							if (rangePray[j] != i)
							{
								player.prayerActive[rangePray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++)
						{
							if (magePray[j] != i)
							{
								player.prayerActive[magePray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
				case 2:
				case 7:
				case 15:
					if (player.prayerActive[i] == false)
					{
						for (int j = 0; j < atkPray.length; j++)
						{
							if (atkPray[j] != i)
							{
								player.prayerActive[atkPray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++)
						{
							if (rangePray[j] != i)
							{
								player.prayerActive[rangePray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++)
						{
							if (magePray[j] != i)
							{
								player.prayerActive[magePray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
				case 3:
					//range prays
				case 11:
				case 19:
					if (player.prayerActive[i] == false)
					{
						for (int j = 0; j < atkPray.length; j++)
						{
							if (atkPray[j] != i)
							{
								player.prayerActive[atkPray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++)
						{
							if (strPray[j] != i)
							{
								player.prayerActive[strPray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++)
						{
							if (rangePray[j] != i)
							{
								player.prayerActive[rangePray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++)
						{
							if (magePray[j] != i)
							{
								player.prayerActive[magePray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
				case 4:
				case 12:
				case 20:
					if (player.prayerActive[i] == false)
					{
						for (int j = 0; j < atkPray.length; j++)
						{
							if (atkPray[j] != i)
							{
								player.prayerActive[atkPray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++)
						{
							if (strPray[j] != i)
							{
								player.prayerActive[strPray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++)
						{
							if (rangePray[j] != i)
							{
								player.prayerActive[rangePray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++)
						{
							if (magePray[j] != i)
							{
								player.prayerActive[magePray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
				case 16:
				case 17:
				case 18:
					if (System.currentTimeMillis() - player.stopPrayerDelay < 5000)
					{
						player.sendMessage("You have been injured and can't use this prayer!");
						player.getPA().sendFrame36(player.PRAYER_GLOW[16], 0);
						player.getPA().sendFrame36(player.PRAYER_GLOW[17], 0);
						player.getPA().sendFrame36(player.PRAYER_GLOW[18], 0);
						return;
					}
				case 21:
				case 22:
				case 23:
					headIcon = true;
					for (int p = 16; p < 24; p++)
					{
						if (i != p && p != 19 && p != 20)
						{
							player.prayerActive[p] = false;
							player.getPA().sendFrame36(player.PRAYER_GLOW[p], 0);
						}
					}
					break;
				case 24:
				case 25:
					if (player.prayerActive[i] == false)
					{
						for (int j = 0; j < atkPray.length; j++)
						{
							if (atkPray[j] != i)
							{
								player.prayerActive[atkPray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++)
						{
							if (strPray[j] != i)
							{
								player.prayerActive[strPray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++)
						{
							if (rangePray[j] != i)
							{
								player.prayerActive[rangePray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++)
						{
							if (magePray[j] != i)
							{
								player.prayerActive[magePray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[magePray[j]], 0);
							}
						}
						for (int j = 0; j < defPray.length; j++)
						{
							if (defPray[j] != i)
							{
								player.prayerActive[defPray[j]] = false;
								player.getPA().sendFrame36(player.PRAYER_GLOW[defPray[j]], 0);
							}
						}
					}
					break;
				}
				if (!headIcon)
				{
					if (player.prayerActive[i] == false)
					{
						player.prayerActive[i] = true;
						player.getPA().sendFrame36(player.PRAYER_GLOW[i], 1);
					}
					else
					{
						player.prayerActive[i] = false;
						player.getPA().sendFrame36(player.PRAYER_GLOW[i], 0);
					}
				}
				else
				{
					if (player.prayerActive[i] == false)
					{
						player.prayerActive[i] = true;
						player.getPA().sendFrame36(player.PRAYER_GLOW[i], 1);
						player.headIcon = player.PRAYER_HEAD_ICONS[i];
						player.getPA().requestUpdates();
					}
					else
					{
						player.prayerActive[i] = false;
						player.getPA().sendFrame36(player.PRAYER_GLOW[i], 0);
						player.headIcon = -1;
						player.getPA().requestUpdates();
					}
				}
			}
			else
			{
				player.getPA().sendFrame36(player.PRAYER_GLOW[i], 0);
				player.getPA().sendFrame126("You need a <col=255>Prayer level of " + player.PRAYER_LEVEL_REQUIRED[i] + " to use " + player.PRAYER_NAME[i] + ".", 357);
				player.getPA().sendFrame126("Click here to continue", 358);
				player.getPA().sendFrame164(356);
			}
		}
		else
		{
			player.getPA().sendFrame36(player.PRAYER_GLOW[i], 0);
			player.sendMessage("You have run out of prayer points!");
		}
		AncientCursesPrayerBook.checkPrayerActive((Client) player);
	}



	public boolean checkSpecAmount(int weapon)
	{
		if (player.playerEquipment[Constants.RING_SLOT] == 19669)
		{
			player.specAmount += player.specAmount * 0.1;
		}
		switch (weapon)
		{
		case 19780: // Korasi's sword
		if (player.specAmount >= 6) {
			player.specAmount -= 6;
			ItemAssistant.addSpecialBar(player, weapon);
			return true;
		}
		return false;

		// Hand cannon.
		case 15241:
			if (player.specAmount >= 5)
			{
				player.specAmount -= 5;
				ItemAssistant.addSpecialBar(player, weapon);
				return true;
			}
			return false;
		case 1249: // Dragon spear
		case 3101: // RuneClaws
		case 1215:
		case 1231:
		case 5680:
		case 5698:
		case 13899:
		case 13901:
		case 13923:
		case 13925:
		case 1305:
		case 1434:
			if (player.specAmount >= 2.5)
			{
				player.specAmount -= 2.5;
				ItemAssistant.addSpecialBar(player, weapon);
				return true;
			}
			return false;
		case 4151:
		case 15445:
		case 15444:
		case 15443:
		case 15442:
		case 15441:
		case 11694:
		case 11698:
		case 4153:
		case 14484:
		case 13883:
		case 10887:
			// morrigan throwing axe
		case 13879:
			// Morrigan Javeline
			if (player.specAmount >= 5)
			{
				player.specAmount -= 5;
				ItemAssistant.addSpecialBar(player, weapon);
				return true;
			}
			return false;
		case 3204:
		case 13902:
		case 13904:
		case 13926:
		case 13928:
			if (player.specAmount >= 3)
			{
				player.specAmount -= 3;
				ItemAssistant.addSpecialBar(player, weapon);
				return true;
			}
			return false;
		case 1377:
		case 11696:
		case 11730:
		case 13905:
		case 13907:
		case 13929:
		case 13931:
		case 15486:
		case 11061:
			if (player.specAmount >= 10)
			{
				player.specAmount -= 10;
				ItemAssistant.addSpecialBar(player, weapon);
				return true;
			}
			return false;
		case 4587:
		case 859:
		case 861:
		case 11235:
		case 11700:
			if (player.specAmount >= 5.5)
			{
				player.specAmount -= 5.5;
				ItemAssistant.addSpecialBar(player, weapon);
				return true;
			}
			return false;
		default:
			return true; // incase u want to test a weapon
		}
	}

	/**
	 * Stop the player from attacking.
	 */
	public void resetPlayerAttack()
	{
		player.usingMagic = false;
		player.npcIndex = 0;
		player.faceUpdate(0);
		player.playerIndex = 0;
		player.getPA().resetFollow();
	}

	public int getCombatDifference(int combat1, int combat2)
	{
		if (combat1 > combat2)
		{
			return (combat1 - combat2);
		}
		if (combat2 > combat1)
		{
			return (combat2 - combat1);
		}
		return 0;
	}

	double[] prayerData = {
			1, // Thick Skin.
			1, // Burst of Strength.
			1, // Clarity of Thought.
			1, // Sharp Eye.
			1, // Mystic Will.
			2, // Rock Skin.
			2, // SuperHuman Strength.
			2, // Improved Reflexes.
			0.4, // Rapid restore.
			0.6, // Rapid Heal.
			0.6, // Protect Items.
			1.5, // Hawk eye.
			2, // Mystic Lore.
			4, // Steel Skin.
			4, // Ultimate Strength.
			4, // Incredible Reflexes.
			4, // Protect from Magic.
			4, // Protect from Missiles.
			4, // Protect from Melee.
			4, // Eagle Eye.
			4, // Mystic Might.
			1, // Retribution.
			2, // Redemption.
			6, // Smite.
			8, // Chivalry.
			8, // Piety.
	};

	public void handlePrayerDrain()
	{
		if (player.isDead)
		{
			return;
		}
		player.usingPrayer = false;
		double toRemove = 0.0;
		for (int j = 0; j < prayerData.length; j++)
		{
			if (player.prayerActive[j])
			{
				toRemove += prayerData[j] / 15;
				player.usingPrayer = true;
			}
		}
		for (int j = 0; j < player.curses().curseData.length; j++)
		{
			if (player.curseActive[j])
			{
				toRemove += player.curses().curseData[j] / 15;
				player.usingPrayer = true;
			}
		}
		if (toRemove > 0)
		{
			toRemove /= (1 + (0.035 * player.playerBonus[11]));
		}
		player.prayerPoint -= toRemove;
		if (player.prayerPoint <= 0)
		{
			player.prayerPoint = 1.0 + player.prayerPoint;
			reducePrayerLevel();
		}

		if (player.skillLevel[5] < 0)
		{
			player.skillLevel[5] = 0;
		}
	}

	public void reducePrayerLevel()
	{
		if (player.skillLevel[5] - 1 > 0)
		{
			player.skillLevel[5] -= 1;
		}
		else
		{
			player.sendMessage("You have run out of prayer points!");
			player.skillLevel[5] = 0;
			resetPrayers();
		}
		player.getPA().refreshSkill(5);
	}

	public void resetPrayers()
	{
		for (int i = 0; i < player.prayerActive.length; i++)
		{
			player.prayerActive[i] = false;
			player.getPA().sendFrame36(player.PRAYER_GLOW[i], 0);
		}
		for (int i = 0; i < player.curseActive.length; i++)
		{
			player.curses().deactivate(i);
		}
		player.headIcon = -1;
		player.getPA().requestUpdates();
		InterfaceAssistant.quickPrayersOff(player);
	}

	/**
	 *
	 * @param attackType The combat type, 0 is Melee, 1 is Range, 2 is Magic.
	 * @param damage The damage dealt to the target.
	 */
	 public void addCombatXP(int attackType, int damage)
	{

		player.getPA().addSkillXP((int)(damage * 1.3), 3); // Hitpoints experience.

		switch (attackType)
		{

		case 0: // Melee

			switch (player.fightMode)
			{

			case 0: // Accurate
				player.getPA().addSkillXP((int)(4 * damage), 0);
				break;
			case 1: // Aggressive
				player.getPA().addSkillXP((int)(4 * damage), 2);
				break;
			case 2: // Block
				player.getPA().addSkillXP((int)(4 * damage), 1);
				break;
			case 3: // Controlled
				for (int i = 0; i < 3; i++)
				{
					player.getPA().addSkillXP((int)(1.3 / 4 * damage), i);
				}
				break;

			}

			break;

		case 1: // Ranged

			switch (player.fightMode)
			{

			case 0: // Accurate
			case 1: // Rapid
				player.getPA().addSkillXP(
						(int)(4 * damage), 4);
				break;
			case 3: // Block
				player.getPA().addSkillXP((int)(6 * damage), 1);
				player.getPA().addSkillXP((int)(6 * damage), 4);
				break;

			}

			break;

		case 2: // Magic
			if (player.magicSplash)
			{
				damage = 0;
			}
			int magicXP = damage * 2 + player.MAGIC_SPELLS[player.spellId][7];
			player.getPA().addSkillXP(magicXP, 6);
			break;

		}
	}


	 public boolean checkMultiBarrageReqs(int i)
	 {
		 if (PlayerHandler.players[i] == null)
		 {
			 return false;
		 }
		 if (i == player.playerId) return false;
		 if (!PlayerHandler.players[i].inWilderness())
		 {
			 return false;
		 }
		 if (!player.inSafePkArea())
		 {
			 int combatDif1 = player.getCombat().getCombatDifference(player.combatLevel, PlayerHandler.players[i].combatLevel);
			 if (combatDif1 > player.wildLevel || combatDif1 > PlayerHandler.players[i].wildLevel)
			 {
				 player.sendMessage("Your combat level difference is too great to attack that player here.");
				 return false;
			 }
		 }
		 if (!PlayerHandler.players[i].inMulti())
		 { // single combat zones
			 if (PlayerHandler.players[i].underAttackBy != player.playerId && PlayerHandler.players[i].underAttackBy != 0)
			 {
				 return false;
			 }
			 if (PlayerHandler.players[i].playerId != player.underAttackBy && player.underAttackBy != 0)
			 {
				 player.sendMessage("You are already in combat.");
				 return false;
			 }
		 }
		 return true;
	 }
	 public boolean checkMultiBarrageReqsNPC(int i)
	 {
		 if (NPCHandler.npcs[i] == null) return false;
		 return true;
	 }
	 /**
	  *Weapon stand, walk, run, etc emotes
	  **/
	 public void getPlayerAnimIndex(String weaponName)
	 {
		 /* Normal animations for without weapons. */
		 player.playerStandIndex = 0x328;
		 player.playerTurnIndex = 0x337;
		 player.playerWalkIndex = 0x333;
		 player.playerTurn180Index = 0x334;
		 player.playerTurn90CWIndex = 0x335;
		 player.playerTurn90CCWIndex = 0x336;
		 player.playerRunIndex = 0x338;

		 if (weaponName.contains("ahrim"))
		 {
			 weaponRoamAnimations(809, 1146, 1210);
			 return;
		 }
		 if (weaponName.contains("boxing"))
		 {
			 weaponRoamAnimations(3677, 3680, 3680);
			 return;
		 }
		 if (weaponName.contains("staff") || weaponName.contains("halberd") || weaponName.contains("guthan") || weaponName.contains("rapier") || weaponName.contains("wand"))
		 {
			 weaponRoamAnimations(12010, 1146, 1210);
			 player.playerTurnIndex = 1205;
			 player.playerTurn180Index = 1206;
			 player.playerTurn90CWIndex = 1207;
			 player.playerTurn90CCWIndex = 1208;
			 return;
		 }
		 if (weaponName.contains("dharok"))
		 {
			 player.playerStandIndex = 0x811;
			 player.playerWalkIndex = 0x67F;
			 player.playerRunIndex = 12001;
			 return;
		 }
		 if (weaponName.contains("verac"))
		 {
			 weaponRoamAnimations(1832, 1830, 1831);
			 return;
		 }
		 if (weaponName.contains("karil"))
		 {
			 player.playerStandIndex = 2074;
			 player.playerWalkIndex = 2076;
			 player.playerRunIndex = 2077;
			 return;
		 }
		 if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.contains("saradomin sw"))
		 {
			 weaponRoamAnimations(7047, 7046, 7039);
			 player.playerTurnIndex = 7040;
			 player.playerTurn180Index = 7045;
			 player.playerTurn90CWIndex = 7043;
			 player.playerTurn90CCWIndex = 7044;
			 return;
		 }
		 if (weaponName.contains("sword") || weaponName.contains("scimitar"))
		 {
			 weaponRoamAnimations(0x328, 0x333, 1210);
			 player.playerTurnIndex = 0x337;
			 player.playerTurn180Index = 0x334;
			 player.playerTurn90CWIndex = 0x335;
			 player.playerTurn90CCWIndex = 0x336;
			 return;
		 }
		 if (weaponName.contains("bow"))
		 {
			 weaponRoamAnimations(808, 819, 824);
			 return;
		 }
		 switch (player.playerEquipment[Constants.WEAPON_SLOT])
		 {
		 // Abyssal Whips.
		 case 4151:
		 case 15445:
		 case 15444:
		 case 15443:
		 case 15442:
		 case 15441:
		 case 21371:
		 case 21372:
		 case 21373:
		 case 21374:
		 case 21375:
			 weaponRoamAnimations(11973, 11975, 11976);
			 break;

		 case 10887:
			 // Barrelchest Anchor
			 player.playerStandIndex = 5869;
			 player.playerWalkIndex = 5867;
			 player.playerRunIndex = 5868;
			 break;

		 case 15241:
			 // Hand cannon
			 player.playerStandIndex = 12155;
			 player.playerWalkIndex = 12154;
			 player.playerRunIndex = 12154;
			 break;
		 case 18355:
			 // Chaotic staff
			 player.playerStandIndex = 808;
			 break;
		 case 6528:
			 // Tzhaar-ket-om
			 player.playerStandIndex = 0x811;
			 player.playerWalkIndex = 1663;
			 player.playerRunIndex = 1664;
			 break;
		 case 18353:
		 case 4153:
			 // Chaotic maul and Granite maul
			 player.playerStandIndex = 1662;
			 player.playerWalkIndex = 1663;
			 player.playerRunIndex = 1664;
			 break;
		 case 1305:
			 // Dragon longsword
			 player.playerStandIndex = 809;
			 break;
		 case 19784:
			 player.playerStandIndex = 809;
			 break;
		 case 11716:
			 player.playerRunIndex = 12016;
			 player.playerWalkIndex = 12012;
			 player.playerStandIndex = 12010;
			 break;
		 }
	 }

	 /**
	  * The animations of the player when weilding a weapon.
	  * @param stand
	  * 			Animation when standing.
	  * @param walk
	  * 			Animation when walking.
	  * @param run
	  * 			Animation when running.
	  */
	 public void weaponRoamAnimations(int stand, int walk, int run)
	 {
		 player.playerStandIndex = stand;
		 player.playerWalkIndex = walk;
		 player.playerRunIndex = run;
	 }

	 /**
	  * Weapon attack emote
	  **/
	 public int weaponAttackAnimation(String weaponName)
	 {
		 player.lastAttackAnimationTimer = System.currentTimeMillis();
		 if (player.playerEquipment[Constants.WEAPON_SLOT] <= 0)
		 {
			 if (player.combatType(player.ACCURATE) || player.combatType(player.BLOCK))
			 {
				 return 422;
			 }
			 if (player.combatType(player.AGGRESSIVE)) 
			 {
				 return 423;
			 }
		 }
		 switch (player.playerEquipment[Constants.WEAPON_SLOT])
		 {
		 case 13879: // Morrigan's javelin.
		 return 10501;

		 case 13883: // Morrigan's throwing axe.
		 return 10504;
		 }

		 if (weaponName.contains("knife") || weaponName.contains("dart") || weaponName.contains("javelin") || weaponName.contains("thrownaxe"))
		 {
			 return 806;
		 }
		 if (weaponName.contains("boxing gloves"))
		 {
			 return 3678;
		 }
		 if (weaponName.contains("halberd"))
		 {
			 return 440;
		 }
		 if (weaponName.startsWith("dragon dagger"))
		 {
			 return 402;
		 }
		 if (weaponName.endsWith("dagger"))
		 {
			 return 412;
		 }
		 if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.contains("aradomin sword"))
		 {
			 if (player.combatType(player.AGGRESSIVE) || player.combatType(player.ACCURATE))
			 {
				 return 7041;
			 }
			 if (player.combatType(player.CONTROLLED))
			 {
				 return 7048;
			 }
			 if (player.combatType(player.DEFENSIVE))
			 {
				 return 7049;
			 }
		 }
		 if (weaponName.contains("sword"))
		 {
			 return 390;
		 }
		 if (weaponName.contains("karil"))
		 {
			 return 2075;
		 }
		 if (weaponName.contains("'bow") || weaponName.contains("crossbow"))
		 {
			 return 4230;
		 }
		 if (weaponName.contains("bow"))
		 {
			 return 426;
		 }
		 if (weaponName.contains("battleaxe"))
		 {
			 return 395;
		 }
		 if (weaponName.contains("scimitar") || weaponName.contains("longsword") || weaponName.contains("korasi"))
		 {
			 switch (player.fightMode)
			 {
			 case 0:
				 return 390;
			 case 1:
				 return 390;
			 case 2:
				 return 390;
			 case 3:
				 return 390;
			 }
		 }
		 if (weaponName.contains("pickaxe"))
		 {
			 return 13035;
		 }
		 if (weaponName.contains("rapier"))
		 {
			 switch (player.fightMode)
			 {
			 case 0:
				 return 12028;
			 case 1:
				 return 12028;
			 case 2:
				 return 12028;
			 case 3:
				 return 12028;
			 }
		 }
		 switch (player.playerEquipment[Constants.WEAPON_SLOT])
		 {
		 case 6522:
			 // Toktz-xil-ul (tzhaar range weapon, looks like a hoolahoop)
			 return 2614;
		 case 1434:
			 // Dragon mace
			 return 13035;
		 case 4153:
			 // Granite maul
			 return 1665;
		 case 4726:
			 // Guthan's spear 
			 return 2080;
		 case 4747:
			 // Torags hammers
			 return 0x814;
		 case 13905:
		 case 13907:
		 case 13929:
		 case 13931:
		 case 11716:
			 // Vesta's spear and Zamorokian spear
				 return 12006;
		 case 4718:
			 // Dharok's greataxe
			 if (player.combatType(player.AGGRESSIVE))
				 return 2066;
			 if (player.combatType(player.ACCURATE))
				 return 2066;
			 return 2067;
		 case 4710:
			 // Ahrim's staff
			 return 406;
			 // Boxing gloves
		 case 7671:
		 case 7673:
			 return 3678;
		 case 14484:
			 // Dragon claws
			 return 393;
		 case 4755:
			 // Verac's flail
			 return 2062;
		 case 4734:
			 // Karil's crossbow
			 return 2075;
		 case 13902:
		 case 13904:
		 case 13926:
		 case 13928:
			 // Stat warhammer
			 return 13035;
		 case 10887: // Barrelchest anchor
			 return 5865;
		 case 4151:
		 case 15445:
		 case 15444:
		 case 15443:
		 case 15442:
		 case 15441:
			 return 1658;
		 case 15241:
			 return 12152;
		 case 1215:
		 case 5698:
			 return 402;
		 case 6528:
			 // Obby maul
		 case 18353:
			 return 2661;
		 default:
			 return 451;
		 }
	 }
	 /**
	  * Block animations
	  */
	 public int getBlockAnimation()
	 {
		 if (System.currentTimeMillis() - player.lastAttackAnimationTimer < 1000)
		 {
			 return -1;
		 }
		 if (player.getDoingAgility())
		 {
			 return -1;
		 }
		 String shield = ItemAssistant.getItemName(player.playerEquipment[Constants.SHIELD_SLOT]).toLowerCase();
		 String weapon = ItemAssistant.getItemName(player.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase();
		 if (shield.contains("defender"))
		 {
			 return 4177;
		 }
		 if (shield.contains("book") && (weapon.contains("wand")))
		 {
			 return 404;
		 }
		 if (shield.contains("shield"))
		 {
			 return 1156;
		 }
		 if (weapon.contains("rapier") || weapon.contains("scimitar") || weapon.contains("korasi"))
		 {
			 return 12030;
		 }
		 if (weapon.contains("staff"))
		 {
			 return 420;
		 }
		 if (weapon.contains("boxing gloves"))
		 {
			 return 3679;
		 }
		 switch (player.playerEquipment[Constants.WEAPON_SLOT])
		 {
		 case 1291:
		 case 1293:
		 case 1295:
		 case 1297:
		 case 1299:
		 case 1301:
		 case 1303:
		 case 1305:
		 case 6607:
		 case 13474:
		 case 13899:
		 case 13901:
		 case 13923:
		 case 13925:
		 case 13982:
		 case 13984:
		 case 16024:
		 case 16025:
		 case 16026:
		 case 16027:
		 case 16028:
		 case 16029:
		 case 16030:
		 case 16031:
		 case 16032:
		 case 16033:
		 case 16034:
		 case 16383:
		 case 16385:
		 case 16387:
		 case 16389:
		 case 16391:
		 case 16393:
		 case 16395:
		 case 16397:
		 case 16399:
		 case 16401:
		 case 16403:
		 case 16961:
		 case 16963:
		 case 18351:
		 case 18352:
		 case 18367:
		 case 18368:
		 case 1321:
		 case 1323:
		 case 1325:
		 case 1327:
		 case 1329:
		 case 1331:
		 case 1333:
		 case 4587:
		 case 6611:
		 case 13979:
		 case 13981:
		 case 14097:
		 case 14287:
		 case 14289:
		 case 14291:
		 case 14293:
		 case 14295:
		 case 746:
		 case 747:
		 case 1203:
		 case 1205:
		 case 1207:
		 case 1209:
		 case 1211:
		 case 1213:
		 case 1215:
		 case 1217:
		 case 1219:
		 case 1221:
		 case 1223:
		 case 1225:
		 case 1227:
		 case 1229:
		 case 1231:
		 case 1233:
		 case 1235:
		 case 1813:
		 case 5668:
		 case 5670:
		 case 5672:
		 case 5674:
		 case 5676:
		 case 5678:
		 case 5680:
		 case 5682:
		 case 5684:
		 case 5686:
		 case 5688:
		 case 5690:
		 case 5692:
		 case 5694:
		 case 5696:
		 case 5698:
		 case 5700:
		 case 5702:
		 case 6591:
		 case 6593:
		 case 6595:
		 case 6597:
		 case 8872:
		 case 8873:
		 case 8875:
		 case 8877:
		 case 8879:
		 case 13976:
		 case 13978:
		 case 14297:
		 case 14299:
		 case 14301:
		 case 14303:
		 case 14305:
		 case 15826:
		 case 15848:
		 case 15849:
		 case 15850:
		 case 15851:
		 case 15853:
		 case 15854:
		 case 15855:
		 case 15856:
		 case 15857:
		 case 15858:
		 case 15859:
		 case 15860:
		 case 15861:
		 case 15862:
		 case 15863:
		 case 15864:
		 case 15865:
		 case 15866:
		 case 15867:
		 case 15868:
		 case 15869:
		 case 15870:
		 case 15871:
		 case 15872:
		 case 15873:
		 case 15874:
		 case 15875:
		 case 15876:
		 case 15877:
		 case 15879:
		 case 15880:
		 case 15881:
		 case 15882:
		 case 15883:
		 case 15884:
		 case 15885:
		 case 15886:
		 case 15887:
		 case 15888:
		 case 15889:
		 case 15890:
		 case 15891:
		 case 16757:
		 case 16759:
		 case 16761:
		 case 16763:
		 case 16765:
		 case 16767:
		 case 16769:
		 case 16771:
		 case 16773:
		 case 16775:
		 case 16777:
		 case 16779:
		 case 16781:
		 case 16783:
		 case 16785:
		 case 16787:
		 case 16789:
		 case 16791:
		 case 16793:
		 case 16795:
		 case 16797:
		 case 16799:
		 case 16801:
		 case 16803:
		 case 16805:
		 case 16807:
		 case 16809:
		 case 16811:
		 case 16813:
		 case 16815:
		 case 16817:
		 case 16819:
		 case 16821:
		 case 16823:
		 case 16825:
		 case 16827:
		 case 16829:
		 case 16831:
		 case 16833:
		 case 16835:
		 case 16837:
		 case 16839:
		 case 16841:
		 case 16843:
		 case 17275:
		 case 17277:
		 case 667:
		 case 1277:
		 case 1279:
		 case 1281:
		 case 1283:
		 case 1285:
		 case 1287:
		 case 1289:
		 case 19780:
		 case 16035:
		 case 16036:
		 case 16037:
		 case 16038:
		 case 16039:
		 case 16040:
		 case 16041:
		 case 16042:
		 case 16043:
		 case 16044:
		 case 16045:
		 case 16935:
		 case 16937:
		 case 16939:
		 case 16941:
		 case 16943:
		 case 16945:
		 case 16947:
		 case 16949:
		 case 16951:
		 case 16953:
		 case 16955:
		 case 16957:
		 case 16959:
		 case 18349:
		 case 18350:
		 case 18365:
		 case 18366:
		 case 11061:
			 return 12030;

		 case 1171:
		 case 1173:
		 case 1175:
		 case 1177:
		 case 1179:
		 case 1181:
		 case 1183:
		 case 1185:
		 case 1187:
		 case 1189:
		 case 1191:
		 case 1193:
		 case 1195:
		 case 1197:
		 case 1199:
		 case 1201:
		 case 1540:
		 case 2589:
		 case 2597:
		 case 2603:
		 case 2611:
		 case 2621:
		 case 2629:
		 case 2659:
		 case 2675:
		 case 2890:
		 case 3122:
		 case 3488:
		 case 3758:
		 case 4156:
		 case 4224:
		 case 4226:
		 case 4227:
		 case 4228:
		 case 4229:
		 case 4230:
		 case 4231:
		 case 4232:
		 case 4233:
		 case 4234:
		 case 4235:
		 case 4507:
		 case 4512:
		 case 6215:
		 case 6217:
		 case 6219:
		 case 6221:
		 case 6223:
		 case 6225:
		 case 6227:
		 case 6229:
		 case 6231:
		 case 6233:
		 case 6235:
		 case 6237:
		 case 6239:
		 case 6241:
		 case 6243:
		 case 6245:
		 case 6247:
		 case 6249:
		 case 6251:
		 case 6253:
		 case 6255:
		 case 6257:
		 case 6259:
		 case 6261:
		 case 6263:
		 case 6265:
		 case 6267:
		 case 6269:
		 case 6271:
		 case 6273:
		 case 6275:
		 case 6277:
		 case 6279:
		 case 6631:
		 case 6633:
		 case 6894:
		 case 7332:
		 case 7334:
		 case 7336:
		 case 7338:
		 case 7340:
		 case 7342:
		 case 7344:
		 case 7346:
		 case 7348:
		 case 7350:
		 case 7352:
		 case 7354:
		 case 7356:
		 case 7358:
		 case 7360:
		 case 7676:
		 case 9731:
		 case 10352:
		 case 10665:
		 case 10667:
		 case 10669:
		 case 10671:
		 case 10673:
		 case 10675:
		 case 10677:
		 case 10679:
		 case 10827:
		 case 11284:
		 case 12908:
		 case 12910:
		 case 12912:
		 case 12914:
		 case 12916:
		 case 12918:
		 case 12920:
		 case 12922:
		 case 12924:
		 case 12926:
		 case 12928:
		 case 12930:
		 case 12932:
		 case 12934:
		 case 13506:
		 case 13734:
		 case 13736:
		 case 13738:
		 case 13740:
		 case 13742:
		 case 13744:
		 case 13964:
		 case 13966:
		 case 14578:
		 case 14579:
		 case 15808:
		 case 15809:
		 case 15810:
		 case 15811:
		 case 15812:
		 case 15813:
		 case 15814:
		 case 15815:
		 case 15816:
		 case 15817:
		 case 15818:
		 case 16079:
		 case 16933:
		 case 16934:
		 case 16971:
		 case 16972:
		 case 17341:
		 case 17342:
		 case 17343:
		 case 17344:
		 case 17345:
		 case 17346:
		 case 17347:
		 case 17348:
		 case 17349:
		 case 17351:
		 case 17353:
		 case 17355:
		 case 17357:
		 case 17359:
		 case 17361:
		 case 17405:
		 case 18359:
		 case 18360:
		 case 18361:
		 case 18362:
		 case 18363:
		 case 18364:
		 case 18582:
		 case 18584:
		 case 18691:
		 case 18709:
		 case 18747:
		 case 19340:
		 case 19345:
		 case 19352:
		 case 19410:
		 case 19426:
		 case 19427:
		 case 19440:
		 case 19441:
		 case 19442:
		 case 19749:
			 return 1156;

		 case 4151:
		 case 13444:
		 case 14661:
		 case 15441:
		 case 15442:
		 case 15443:
		 case 15444:
		 case 21369:
		 case 21371:
		 case 21372:
		 case 21373:
		 case 21374:
		 case 21375:
		 case 23691:
			 return 11974;

		 case 8844:
		 case 8845:
		 case 8846:
		 case 8847:
		 case 8848:
		 case 8849:
		 case 8850:
		 case 15455:
		 case 15456:
		 case 15457:
		 case 15458:
		 case 15459:
		 case 15825:
		 case 17273:
		 case 20072:
			 return 4177;

		 case 3095:
		 case 3096:
		 case 3097:
		 case 3098:
		 case 3099:
		 case 3100:
		 case 3101:
		 case 6587:
		 case 14484:
			 return 397;

		 case 1379:
		 case 1381:
		 case 1383:
		 case 1385:
		 case 1387:
		 case 1389:
		 case 1391:
		 case 1393:
		 case 1395:
		 case 1397:
		 case 1399:
		 case 1401:
		 case 1403:
		 case 1405:
		 case 1407:
		 case 1409:
		 case 2415:
		 case 2416:
		 case 2417:
		 case 3053:
		 case 3054:
		 case 3055:
		 case 3056:
		 case 4170:
		 case 4675:
		 case 4710:
		 case 4862:
		 case 4863:
		 case 4864:
		 case 4865:
		 case 4866:
		 case 4867:
		 case 6562:
		 case 6603:
		 case 6727:
		 case 9084:
		 case 9091:
		 case 9092:
		 case 9093:
		 case 11736:
		 case 11738:
		 case 11739:
		 case 11953:
		 case 13406:
		 case 13629:
		 case 13630:
		 case 13631:
		 case 13632:
		 case 13633:
		 case 13634:
		 case 13635:
		 case 13636:
		 case 13637:
		 case 13638:
		 case 13639:
		 case 13640:
		 case 13641:
		 case 13642:
		 case 6908:
		 case 6910:
		 case 6912:
		 case 6914:
			 return 415;
			 // Boxing gloves
		 case 7671:
		 case 7673:
			 return 3679;
		 case 4153:
		 case 6528:
			 return 1666;
		 case 1307:
		 case 1309:
		 case 1311:
		 case 1313:
		 case 1315:
		 case 1317:
		 case 1319:
		 case 6609:
		 case 7158:
		 case 7407:
		 case 16127:
		 case 16128:
		 case 16129:
		 case 16130:
		 case 16131:
		 case 16132:
		 case 16133:
		 case 16134:
		 case 16135:
		 case 16136:
		 case 16137:
		 case 16889:
		 case 16891:
		 case 16893:
		 case 16895:
		 case 16897:
		 case 16899:
		 case 16901:
		 case 16903:
		 case 16905:
		 case 16907:
		 case 16909:
		 case 16973:
		 case 18369:
		 case 20874:
		 case 11694:
		 case 11696:
		 case 11698:
		 case 11700:
		 case 11730:
			 return 13051;

		 case 18355:
		 case 15486:
		 case 3190:
		 case 3192:
		 case 3194:
		 case 3196:
		 case 3198:
		 case 3200:
		 case 3202:
		 case 3204:
		 case 6599:
			 return 12806;

		 case 18353:
		 case 18354:
			 return 13054;

		 case 15241:
			 return 12156;

		 case 4718:
			 return 12004;

		 case 10887:
			 return 5866;

		 case 4755:
			 return 2063;
		 case 11716:
			 return 12008;
		 case 15445:
			 return 11974;
		 default:
			 return 424;
		 }
	 }
	 /**
	  * Weapon and magic attack speed!
	  **/
	 public int getAttackDelay(String s)
	 {
		 if (player.usingMagic)
		 {
			 switch (player.MAGIC_SPELLS[player.spellId][0])
			 {
			 case 12871:
				 // ice blitz
			 case 13023:
				 // shadow barrage
			 case 12891:
				 // ice barrage
				 return 5;
			 default:
				 return 5;
			 }
		 }
		 if (player.playerEquipment[Constants.WEAPON_SLOT] == -1) return 4; //unarmed
		 switch (player.playerEquipment[Constants.WEAPON_SLOT])
		 {
		 case 15241:
			 return 10;
		 case 11235:
			 return 9;
		 case 11730:
			 return 4;
		 case 6528:
		 case 18353:
		 case 10887: // Barrelchest anchor
		 return 7;
		 }
		 if (s.endsWith("greataxe")) return 7;
		 else if (s.equals("torags hammers")) return 5;
		 else if (s.equals("guthans warspear")) return 5;
		 else if (s.equals("veracs flail")) return 5;
		 else if (s.equals("ahrims staff")) return 6;
		 else if (s.contains("staff"))
		 {
			 if (s.contains("zamarok") || s.contains("guthix") || s.contains("saradomian") || s.contains("slayer") || s.contains("ancient")) return 4;
			 else return 5;
		 }
		 else if (s.contains("bow"))
		 {
			 if (s.contains("composite") || s.equals("seercull")) return 5;
			 else if (s.contains("aril")) return 4;
			 else if (s.contains("Ogre")) return 8;
			 else if (s.contains("short") || s.contains("hunt") || s.contains("sword")) return 4;
			 else if (s.contains("long") || s.contains("crystal")) return 6;
			 else if (s.contains("'bow")) return 7;
			 return 5;
		 }
		 else if (s.contains("dagger")) return 4;
		 else if (s.contains("godsword") || s.contains("2h")) return 6;
		 else if (s.contains("longsword")) return 5;
		 else if (s.contains("sword")) return 4;
		 else if (s.contains("scimitar")) return 4;
		 else if (s.contains("rapier")) return 4;
		 else if (s.contains("mace")) return 5;
		 else if (s.contains("battleaxe")) return 6;
		 else if (s.contains("pickaxe")) return 5;
		 else if (s.contains("thrownaxe")) return 5;
		 else if (s.contains("axe")) return 5;
		 else if (s.contains("warhammer")) return 6;
		 else if (s.contains("2h")) return 7;
		 else if (s.contains("spear")) return 5;
		 else if (s.contains("claw")) return 4;
		 else if (s.contains("halberd")) return 7;
		 //sara sword, 2400ms
		 else if (s.equals("granite maul")) return 7;
		 else if (s.equals("toktz-xil-ak")) //sword
			 return 4;
		 else if (s.equals("tzhaar-ket-em")) //mace
			 return 5;
		 else if (s.equals("tzhaar-ket-om")) //maul
			 return 7;
		 else if (s.equals("toktz-xil-ek")) //knife
			 return 4;
		 else if (s.equals("toktz-xil-ul")) //rings
			 return 4;
		 else if (s.equals("toktz-mej-tal")) //staff
			 return 6;
		 else if (s.contains("whip")) return 4;
		 else if (s.contains("dart")) return 3;
		 else if (s.contains("knife")) return 3;
		 else if (s.contains("javelin")) return 6;
		 return 5;
	 }
	 /**
	  * How long it takes to hit your enemy
	  **/
	 public int getHitDelay(String weaponName)
	 {
		 if (player.usingMagic)
		 {
			 switch (player.MAGIC_SPELLS[player.spellId][0])
			 {
			 case 12871:
				 return 6;
			 default:
				 return 4;
			 }
		 }
		 else
		 {
			 if (weaponName.contains("knife") || weaponName.contains("dart") || weaponName.contains("javelin") || weaponName.contains("thrownaxe"))
			 {
				 return 3;
			 }
			 if (weaponName.contains("cross") || weaponName.contains("c'bow"))
			 {
				 return 4;
			 }
			 if (weaponName.contains("bow") && !player.usingDarkBowSpecialAttack)
			 {
				 return 4;
			 }
			 else if (player.usingDarkBowSpecialAttack)
			 {
				 return 4;
			 }
			 switch (player.playerEquipment[Constants.WEAPON_SLOT])
			 {
			 case 6522:
				 // Toktz-xil-ul
				 return 3;
			 default:
				 return 2;
			 }
		 }
	 }
	 public int getRequiredDistance()
	 {
		 if (player.followId > 0 && player.freezeTimer <= 0 && !player.isMoving) return 2;
		 else if (player.followId > 0 && player.freezeTimer <= 0 && player.isMoving)
		 {
			 return 3;
		 }
		 else
		 {
			 return 1;
		 }
	 }
	 public boolean usingHally()
	 {
		 switch (player.playerEquipment[Constants.WEAPON_SLOT])
		 {
		 case 3190:
		 case 3192:
		 case 3194:
		 case 3196:
		 case 3198:
		 case 3200:
		 case 3202:
		 case 3204:
			 return true;
		 default:
			 return false;
		 }
	 }
	 /**
	  * Melee
	  **/
	  public boolean meleeHitSuccess(int a, int d)
	 {
		 a = Misc.random(a);
		 d = Misc.random(d);
		 return a > d;
	 }
	 public int calculateMeleeAttack1()
	 {
		 int attackLevel = player.skillLevel[0];
		 if (player.prayerActive[2])
		 {
			 attackLevel += player.getLevelForXP(player.playerXP[Constants.ATTACK]) * 0.05;
		 }
		 else if (player.prayerActive[7])
		 {
			 attackLevel += player.getLevelForXP(player.playerXP[Constants.ATTACK]) * 0.1;
		 }
		 else if (player.prayerActive[15])
		 {
			 attackLevel += player.getLevelForXP(player.playerXP[Constants.ATTACK]) * 0.15;
		 }
		 else if (player.prayerActive[24])
		 {
			 attackLevel += player.getLevelForXP(player.playerXP[Constants.ATTACK]) * 0.15;
		 }
		 else if (player.prayerActive[25])
		 {
			 attackLevel += player.getLevelForXP(player.playerXP[Constants.ATTACK]) * 0.2;
		 }
		 else if (player.curseActive[player.curses().TURMOIL])
		 {
			 attackLevel = (int)(attackLevel * player.curses().getTurmoilMultiplier("Attack"));
		 }
		 if (player.fullVoidMelee())
		 {
			 attackLevel += player.getLevelForXP(player.playerXP[Constants.ATTACK]) * 0.1;
		 }
		 attackLevel *= player.specAccuracy;
		 int i = player.playerBonus[bestMeleeAtk()];
		 i += player.bonusAttack;
		 if (player.playerEquipment[Constants.AMULET_SLOT] == 11128 && player.playerEquipment[Constants.WEAPON_SLOT] == 6528) // Berserker necklace and Obby maul
		 {
			 i *= 1.05;
		 }
		 if (player.playerEquipment[Constants.WEAPON_SLOT] == 18353) // Chaotic maul
		 {
			 i *= 1.10;
		 }
		 if (player.playerEquipment[Constants.WEAPON_SLOT] == 10887) // Barrelchest anchor
		 {
			 i *= 1.05;
		 }
		 return (int)((attackLevel + (attackLevel * 0.15) + (i + i * 0.05)) * 1.5);
	 }
	 public int calculateMeleeAttack()
	 {
		 int attackLevel = player.skillLevel[0];
		 if (player.prayerActive[2])
		 {
			 attackLevel += player.getLevelForXP(player.playerXP[Constants.ATTACK]) * 0.05;
		 }
		 else if (player.prayerActive[7])
		 {
			 attackLevel += player.getLevelForXP(player.playerXP[Constants.ATTACK]) * 0.1;
		 }
		 else if (player.prayerActive[15])
		 {
			 attackLevel += player.getLevelForXP(player.playerXP[Constants.ATTACK]) * 0.15;
		 }
		 else if (player.prayerActive[24])
		 {
			 attackLevel += player.getLevelForXP(player.playerXP[Constants.ATTACK]) * 0.15;
		 }
		 else if (player.prayerActive[25])
		 {
			 attackLevel += player.getLevelForXP(player.playerXP[Constants.ATTACK]) * 0.2;
		 }
		 else if (player.curseActive[player.curses().TURMOIL])
		 {
			 attackLevel = (int)(attackLevel * player.curses().getTurmoilMultiplier("Attack"));
		 }
		 if (player.fullVoidMelee())
		 {
			 attackLevel += player.getLevelForXP(player.playerXP[Constants.ATTACK]) * 0.1;
		 }
		 attackLevel *= player.specAccuracy;
		 int i = player.playerBonus[bestMeleeAtk()];
		 i += player.bonusAttack;
		 if (player.playerEquipment[Constants.AMULET_SLOT] == 11128 && player.playerEquipment[Constants.WEAPON_SLOT] == 6528) // Berserker necklace and Obby maul
		 {
			 i *= 1.50;
		 }
		 if (player.playerEquipment[Constants.WEAPON_SLOT] == 18353) // Chaotic maul
		 {
			 i *= 1.95;
		 }
		 if (player.playerEquipment[Constants.WEAPON_SLOT] == 10887) // Barrelchest anchor
		 {
			 i *= 1.70;
		 }
		 return (int)(attackLevel + (attackLevel * 0.15) + (i + i * 0.05));
	 }
	 public int bestMeleeAtk()
	 {
		 if (player.playerBonus[0] > player.playerBonus[1] && player.playerBonus[0] > player.playerBonus[2]) return 0;
		 if (player.playerBonus[1] > player.playerBonus[0] && player.playerBonus[1] > player.playerBonus[2]) return 1;
		 return player.playerBonus[2] <= player.playerBonus[1] || player.playerBonus[2] <= player.playerBonus[0] ? 0 : 2;
	 }
	 public int calculateMeleeMaxHit()
	 {
		 double maxHit = 0;
		 int strength = player.skillLevel[2];
		 int lvlForXP = player.getLevelForXP(player.playerXP[2]);
		 if (player.prayerActive[1])
		 {
			 strength += (int)(lvlForXP * .05);
		 }
		 else if (player.prayerActive[6])
		 {
			 strength += (int)(lvlForXP * .10);
		 }
		 else if (player.prayerActive[14])
		 {
			 strength += (int)(lvlForXP * .15);
		 }
		 else if (player.prayerActive[24])
		 {
			 strength += (int)(lvlForXP * .18);
		 }
		 else if (player.prayerActive[25])
		 {
			 strength += (int)(lvlForXP * .23);
		 }
		 else if (player.curseActive[player.curses().TURMOIL])
		 {
			 strength = (int)(strength * player.curses().getTurmoilMultiplier("Strength"));
		 }
		 maxHit += 1.05D + (double)(strBonus * strength) * 0.00160D; //Change the 0.00160D to change strength bonus damage
		 maxHit += (double) strength * 0.11D;

		 if (player.playerEquipment[Constants.WEAPON_SLOT] == 4718 && player.playerEquipment[Constants.HEAD_SLOT] == 4716 && player.playerEquipment[Constants.TORSO_SLOT] == 4720 && player.playerEquipment[Constants.LEG_SLOT] == 4722)
		 {
			 maxHit += (player.maximumHitPoints() - player.skillLevel[3]) / 2; //multiplied by 10 due to constitution			
		 }
		 if (player.specDamage > 1)
		 {
			 maxHit = (int)(maxHit * player.specDamage);
		 }
		 if (maxHit < 0)
		 {
			 maxHit = 1;
		 }
		 if (player.fullVoidMelee())
		 {
			 maxHit = (int)(maxHit * 1.10);
		 }
		 if (player.playerEquipment[Constants.AMULET_SLOT] == 11128 && player.playerEquipment[Constants.WEAPON_SLOT] == 6528)
		 {
			 maxHit *= 1.25;
		 }
		 return (int)(Math.floor(maxHit));

	 }
	 public int calculateMeleeDefence()
	 {
		 int defenceLevel = player.skillLevel[1];
		 int i = player.playerBonus[bestMeleeDef()];
		 if (player.prayerActive[0])
		 {
			 defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.05;
		 }
		 else if (player.prayerActive[5])
		 {
			 defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.1;
		 }
		 else if (player.prayerActive[13])
		 {
			 defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.15;
		 }
		 else if (player.prayerActive[24])
		 {
			 defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.2;
		 }
		 else if (player.prayerActive[25])
		 {
			 defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.25;
		 }
		 else if (player.curseActive[player.curses().TURMOIL])
		 {
			 defenceLevel = (int)(defenceLevel * player.curses().getTurmoilMultiplier("Defence"));
		 }
		 else if (player.curseActive[13]) // Leech Defence
		 {
			 defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.10;
		 }
		 return (int)(defenceLevel + (defenceLevel * 0.15) + (i + i * 0.05));
	 }
	 public int bestMeleeDef()
	 {
		 if (player.playerBonus[5] > player.playerBonus[6] && player.playerBonus[5] > player.playerBonus[7]) return 5;
		 if (player.playerBonus[6] > player.playerBonus[5] && player.playerBonus[6] > player.playerBonus[7]) return 6;
		 return player.playerBonus[7] <= player.playerBonus[5] || player.playerBonus[7] <= player.playerBonus[6] ? 5 : 7;
	 }
	 /**
	  * Range
	  **/
	 public int calculateRangeAttack()
	 {
		 int attackLevel = player.skillLevel[4];
		 attackLevel *= player.specAccuracy;
		 if (player.fullVoidRange())
		 {
			 attackLevel += player.getLevelForXP(player.playerXP[Constants.RANGED]) * 0.1;
		 }
		 if (player.prayerActive[3])
		 {
			 attackLevel *= 1.07;
		 }
		 else if (player.prayerActive[11])
		 {
			 attackLevel *= 1.12; // Hawk eye
		 }
		 else if (player.prayerActive[19])
		 {
			 attackLevel *= 1.17; // Eagle eye
		 }
		 else if (player.curseActive[11])
		 {
			 attackLevel *= 1.10; // Curses range
		 }
		 return (int)(attackLevel + (player.playerBonus[4] * 2.10));
	 }
	 public int calculateRangeDefence()
	 {
		 int defenceLevel = player.skillLevel[1];
		 if (player.prayerActive[0])
		 {
			 defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.05;
		 }
		 else if (player.prayerActive[5])
		 {
			 defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.1;
		 }
		 else if (player.prayerActive[13])
		 {
			 defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.15;
		 }
		 else if (player.prayerActive[24])
		 {
			 defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.2;
		 }
		 else if (player.prayerActive[25])
		 {
			 defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.25;
		 }
		 return (int)(defenceLevel + player.playerBonus[9] + (player.playerBonus[9] / 2));
	 }
	 public boolean usingBolts()
	 {
		 return player.playerEquipment[Constants.ARROW_SLOT] >= 9130 && player.playerEquipment[Constants.ARROW_SLOT] <= 9145 || player.playerEquipment[Constants.ARROW_SLOT] >= 9230 && player.playerEquipment[Constants.ARROW_SLOT] <= 9245;
	 }
	 public int rangeMaxHit()
	 {
		 int rangeLevel = player.skillLevel[4];
		 double modifier = 1.0;
		 double specDamage = player.specDamage;
		 int itemUsed = player.usingBow ? player.lastArrowUsed : player.lastWeaponUsed;
		 if (player.prayerActive[3])
		 {
			 modifier += 0.05;
		 }
		 else if (player.prayerActive[11])
		 {
			 modifier += 0.12; //Hawk eye
		 }
		 else if (player.prayerActive[19])
		 {
			 modifier += 0.17; //Eagle eye
		 }
		 // else if (c.prayerActive[26]) modifier += 0.22; //Rigour
		 if (player.fullVoidRange())
		 {
			 modifier += 0.25;
		 }
		 double c = modifier * rangeLevel;
		 int rangeStr = getRangeStr(itemUsed);
		 double max = (c + 8) * (rangeStr + 64) / 640;
		 if (specDamage != 1)
		 {
			 max *= specDamage;
		 }
		 if (max < 1)
		 {
			 max = 1;
		 }
		 player.sendMessage("max hit: "+max);
		 return (int)(max);
	 }
	 public int getRangeStr(int i)
	 {
		 if (i == 4214) return 70;
		 switch (i)
		 {
		 case 15243:
			 return 150;
			 //bronze to rune bolts
		 case 877:
			 return 10;
		 case 9140:
			 return 46;
		 case 9141:
			 return 64;
		 case 9142:
		 case 9241:
		 case 9240:
			 return 82;
		 case 9143:
		 case 9243:
		 case 9242:
			 return 100;
		 case 9144:
		 case 9244:
		 case 9245:
			 return 115;
			 //bronze to dragon arrows
		 case 882:
			 return 7;
		 case 884:
			 return 10;
		 case 886:
			 return 16;
		 case 888:
			 return 22;
		 case 890:
			 return 31;
		 case 892:
			 return 49;
		 case 11212:
			 return 50;
		 case 4740:
			 return 55;
			 //knifes
		 case 864:
			 return 3;
		 case 863:
			 return 4;
		 case 865:
			 return 7;
		 case 866:
			 return 10;
		 case 867:
			 return 14;
		 case 868:
			 return 24;
		 }
		 return 0;
	 }
	 public boolean properBolts()
	 {
		 return player.playerEquipment[Constants.ARROW_SLOT] >= 9140 && player.playerEquipment[Constants.ARROW_SLOT] <= 9144 || player.playerEquipment[Constants.ARROW_SLOT] >= 9240 && player.playerEquipment[Constants.ARROW_SLOT] <= 9244;
	 }
	 public int correctBowAndArrows()
	 {
		 if (usingBolts()) return -1;
		 if (player.playerEquipment[Constants.WEAPON_SLOT] == 839 || player.playerEquipment[Constants.WEAPON_SLOT] == 841)
		 {
			 return 884;
		 }
		 switch (player.playerEquipment[Constants.WEAPON_SLOT])
		 {
		 case 839:
		 case 841:
			 return 882;
		 case 843:
		 case 845:
			 return 884;
		 case 847:
		 case 849:
			 return 886;
		 case 851:
		 case 853:
			 return 890;
		 case 855:
		 case 857:
			 return 890;
		 case 859:
		 case 861:
			 return 892;
		 case 4734: // Karil's crossbow.
		 return 4740;
		 case 11235:
			 return 11212;
		 case 15241:
			 return 15243;
		 }
		 return -1;
	 }
	 public int getRangeStartGFX()
	 {
		 switch (player.rangeItemUsed)
		 {
		 case 863:
			 return 220;
		 case 864:
			 return 219;
		 case 865:
			 return 221;
		 case 866:
			 // knives
			 return 223;
		 case 867:
			 return 224;
		 case 868:
			 return 225;
		 case 869:
			 return 222;
		 case 806:
			 return 232;
		 case 807:
			 return 233;
		 case 808:
			 return 234;
		 case 809:
			 // darts
			 return 235;
		 case 810:
			 return 236;
		 case 811:
			 return 237;
		 case 825:
			 return 206;
		 case 826:
			 return 207;
		 case 827:
			 // javelin
			 return 208;
		 case 828:
			 return 209;
		 case 829:
			 return 210;
		 case 830:
			 return 211;
		 case 800:
			 return 42;
		 case 801:
			 return 43;
		 case 802:
			 return 44; // axes
		 case 803:
			 return 45;
		 case 804:
			 return 46;
		 case 805:
			 return 48;
		 case 882:
			 return 19;
		 case 884:
			 return 18;
		 case 886:
			 return 20;
		 case 888:
			 return 21;
		 case 890:
			 return 22;
		 case 892:
			 return 24;
		 case 11212:
			 return 26;
		 case 4212:
		 case 4214:
		 case 4215:
		 case 4216:
		 case 4217:
		 case 4218:
		 case 4219:
		 case 4220:
		 case 4221:
		 case 4222:
		 case 4223:
			 return 250;
		 }
		 return -1;
	 }
	 public int getRangeProjectileGFX()
	 {
		 if (player.usingDarkBowSpecialAttack)
		 {
			 return 1099;
		 }
		 if (player.bowSpecShot > 0)
		 {
			 switch (player.rangeItemUsed)
			 {
			 default: return 249;
			 }
		 }
		 if (player.playerEquipment[Constants.WEAPON_SLOT] == 9185 || player.playerEquipment[Constants.WEAPON_SLOT] == 18357)
		 {
			 return 27;
		 }
		 if (player.playerEquipment[Constants.WEAPON_SLOT] == 13879)
		 {
			 return 1837;
		 }
		 if (player.playerEquipment[Constants.WEAPON_SLOT] == 13883)
		 {
			 return 1839;
		 }
		 switch (player.rangeItemUsed)
		 {
		 case 15241:
		 case 15243:
			 return 2143;
		 case 863:
			 return 213;
		 case 864:
			 return 212;
		 case 865:
			 return 214;
		 case 866:
			 // knives
			 return 216;
		 case 867:
			 return 217;
		 case 868:
			 return 218;
		 case 869:
			 return 215;
		 case 806:
			 return 226;
		 case 807:
			 return 227;
		 case 808:
			 return 228;
		 case 809:
			 // darts
			 return 229;
		 case 810:
			 return 230;
		 case 811:
			 return 231;
		 case 825:
			 return 200;
		 case 826:
			 return 201;
		 case 827:
			 // javelin
			 return 202;
		 case 828:
			 return 203;
		 case 829:
			 return 204;
		 case 830:
			 return 205;
		 case 6522:
			 // Toktz-xil-ul
			 return 442;
		 case 800:
			 return 36;
		 case 801:
			 return 35;
		 case 802:
			 return 37; // axes
		 case 803:
			 return 38;
		 case 804:
			 return 39;
		 case 805:
			 return 40;
		 case 882:
			 return 10;
		 case 884:
			 return 9;
		 case 886:
			 return 11;
		 case 888:
			 return 12;
		 case 890:
			 return 13;
		 case 892:
			 return 15;
		 case 11212:
			 return 17;
		 case 4740:
			 // bolt rack
			 return 27;
		 case 4212:
		 case 4214:
		 case 4215:
		 case 4216:
		 case 4217:
		 case 4218:
		 case 4219:
		 case 4220:
		 case 4221:
		 case 4222:
		 case 4223:
			 return 249;
		 }
		 return -1;
	 }
	 public int getProjectileSpeed()
	 {
		 if (player.usingDarkBowSpecialAttack) return 100;
		 if (player.playerEquipment[Constants.WEAPON_SLOT] == 15241) return 20;
		 return 70;
	 }
	 public int getProjectileShowDelay()
	 {
		 switch (player.playerEquipment[Constants.WEAPON_SLOT])
		 {
		 case 863:
		 case 864:
		 case 865:
		 case 866:
			 // knives
		 case 867:
		 case 868:
		 case 869:
		 case 806:
		 case 807:
		 case 808:
		 case 809:
			 // darts
		 case 810:
		 case 811:
		 case 825:
		 case 826:
		 case 827:
			 // javelin
		 case 828:
		 case 829:
		 case 830:
		 case 800:
		 case 801:
		 case 802:
		 case 803:
			 // axes
		 case 804:
		 case 805:
		 case 4734:
		 case 9185: // Rune c'bow
		 case 18357: // Chaotic c'bow
		 case 4935:
		 case 4936:
		 case 4937:
			 return 15;
		 default:
			 return 0;
		 }
	 }

	 public int mageAtk()
	 {
		 int attackLevel = player.skillLevel[6];
		 if (player.fullVoidMage())
		 {
			 attackLevel += player.getLevelForXP(player.playerXP[6]) * 0.2;
		 }
		 if (player.prayerActive[4])
		 {
			 attackLevel *= 1.05;
		 }
		 else if (player.prayerActive[12])
		 {
			 attackLevel *= 1.10;
		 }
		 else if (player.prayerActive[20])
		 {
			 attackLevel *= 1.15;
		 }
		 else if (player.curseActive[12]) // Curses magic
		 {
			 attackLevel *= 1.10;
		 }
		 return (int)(attackLevel + (player.playerBonus[3] * 2));
	 }

	 public int mageDef()
	 {
		 int defenceLevel = player.skillLevel[1] / 2 + player.skillLevel[6] / 2;
		 if (player.prayerActive[0])
		 {
			 defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.05;
		 }
		 else if (player.prayerActive[3])
		 {
			 defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.1;
		 }
		 else if (player.prayerActive[9])
		 {
			 defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.15;
		 }
		 else if (player.prayerActive[18])
		 {
			 defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.2;
		 }
		 else if (player.prayerActive[19])
		 {
			 defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.25;
		 }
		 return (int)(defenceLevel + player.playerBonus[8] + (player.playerBonus[8] / 3));
	 }

	 public boolean wearingStaff(int runeId)
	 {
		 int wep = player.playerEquipment[Constants.WEAPON_SLOT];
		 switch (runeId)
		 {
		 case 554:
			 if (wep == 1387) return true;
			 break;
		 case 555:
			 if (wep == 1383) return true;
			 break;
		 case 556:
			 if (wep == 1381) return true;
			 break;
		 case 557:
			 if (wep == 1385) return true;
			 break;
		 }
		 return false;
	 }

	 public boolean checkMagicReqs(int spell)
	 {
		 if (player.usingMagic)
		 { // check for runes
			 if ((!ItemAssistant.playerHasItem(player, player.MAGIC_SPELLS[spell][8], player.MAGIC_SPELLS[spell][9]) && !wearingStaff(player.MAGIC_SPELLS[spell][8])) || (!ItemAssistant.playerHasItem(player, player.MAGIC_SPELLS[spell][10], player.MAGIC_SPELLS[spell][11]) && !wearingStaff(player.MAGIC_SPELLS[spell][10])) || (!ItemAssistant.playerHasItem(player, player.MAGIC_SPELLS[spell][12], player.MAGIC_SPELLS[spell][13]) && !wearingStaff(player.MAGIC_SPELLS[spell][12])) || (!ItemAssistant.playerHasItem(player, player.MAGIC_SPELLS[spell][14], player.MAGIC_SPELLS[spell][15]) && !wearingStaff(player.MAGIC_SPELLS[spell][14])))
			 {
				 player.sendMessage("You don't have the required runes to cast this spell.");
				 return false;
			 }
		 }
		 if (player.usingMagic && player.playerIndex > 0)
		 {
			 if (PlayerHandler.players[player.playerIndex] != null)
			 {
				 for (int r = 0; r < player.REDUCE_SPELLS.length; r++)
				 { // reducing spells, confuse etc
					 if (PlayerHandler.players[player.playerIndex].REDUCE_SPELLS[r] == player.MAGIC_SPELLS[spell][0])
					 {
						 player.reduceSpellId = r;
						 if ((System.currentTimeMillis() - PlayerHandler.players[player.playerIndex].reduceSpellDelay[player.reduceSpellId]) > PlayerHandler.players[player.playerIndex].REDUCE_SPELL_TIME[player.reduceSpellId])
						 {
							 PlayerHandler.players[player.playerIndex].canUseReducingSpell[player.reduceSpellId] = true;
						 }
						 else
						 {
							 PlayerHandler.players[player.playerIndex].canUseReducingSpell[player.reduceSpellId] = false;
						 }
						 break;
					 }
				 }
				 if (!PlayerHandler.players[player.playerIndex].canUseReducingSpell[player.reduceSpellId])
				 {
					 player.sendMessage("That player is currently immune to this spell.");
					 player.usingMagic = false;
					 Movement.stopMovement(player);
					 resetPlayerAttack();
					 return false;
				 }
			 }
		 }
		 int staffRequired = getStaffNeeded();
		 if (player.usingMagic && staffRequired > 0)
		 { // staff required
			 if (player.playerEquipment[Constants.WEAPON_SLOT] != staffRequired)
			 {
				 player.sendMessage("You need a " + ItemAssistant.getItemName(staffRequired).toLowerCase() + " to cast this spell.");
				 return false;
			 }
		 }
		 if (player.skillLevel[6] < player.MAGIC_SPELLS[spell][1])
		 {
			 player.sendMessage("You need to have a magic level of " + player.MAGIC_SPELLS[spell][1] + " to cast this spell.");
			 return false;
		 }
		 if (player.usingMagic)
		 {
			 if (player.MAGIC_SPELLS[spell][8] > 0)
			 { // deleting runes
				 if (!wearingStaff(player.MAGIC_SPELLS[spell][8])) ItemAssistant.deleteItem(player, player.MAGIC_SPELLS[spell][8], ItemAssistant.getItemSlot(player, player.MAGIC_SPELLS[spell][8]), player.MAGIC_SPELLS[spell][9]);
			 }
			 if (player.MAGIC_SPELLS[spell][10] > 0)
			 {
				 if (!wearingStaff(player.MAGIC_SPELLS[spell][10])) ItemAssistant.deleteItem(player, player.MAGIC_SPELLS[spell][10], ItemAssistant.getItemSlot(player, player.MAGIC_SPELLS[spell][10]), player.MAGIC_SPELLS[spell][11]);
			 }
			 if (player.MAGIC_SPELLS[spell][12] > 0)
			 {
				 if (!wearingStaff(player.MAGIC_SPELLS[spell][12])) ItemAssistant.deleteItem(player, player.MAGIC_SPELLS[spell][12], ItemAssistant.getItemSlot(player, player.MAGIC_SPELLS[spell][12]), player.MAGIC_SPELLS[spell][13]);
			 }
			 if (player.MAGIC_SPELLS[spell][14] > 0)
			 {
				 if (!wearingStaff(player.MAGIC_SPELLS[spell][14])) ItemAssistant.deleteItem(player, player.MAGIC_SPELLS[spell][14], ItemAssistant.getItemSlot(player, player.MAGIC_SPELLS[spell][14]), player.MAGIC_SPELLS[spell][15]);
			 }
		 }
		 return true;
	 }

	 public int getFreezeTime()
	 {
		 switch (player.MAGIC_SPELLS[player.oldSpellId][0])
		 {
		 case 1572:
		 case 12861:
			 // ice rush
			 return 10;
		 case 1582:
		 case 12881:
			 // ice burst
			 return 17;
		 case 1592:
		 case 12871:
			 // ice blitz
			 return 25;
		 case 12891:
			 // ice barrage
			 return 33;
		 default:
			 return 0;
		 }
	 }

	 public int getStartHeight()
	 {
		 switch (player.MAGIC_SPELLS[player.spellId][0])
		 {
		 case 1562:
			 // stun
			 return 25;
		 case 12939:
			 // smoke rush
			 return 35;
		 case 12987:
			 // shadow rush
			 return 38;
		 case 12861:
			 // ice rush
			 return 15;
		 case 12951:
			 // smoke blitz
			 return 38;
		 case 12999:
			 // shadow blitz
			 return 25;
		 case 12911:
			 // blood blitz
			 return 25;
		 default:
			 return 43;
		 }
	 }
	 public int getEndHeight()
	 {
		 switch (player.MAGIC_SPELLS[player.spellId][0])
		 {
		 case 1562:
			 // stun
			 return 10;
		 case 12939:
			 // smoke rush
			 return 20;
		 case 12987:
			 // shadow rush
			 return 28;
		 case 12861:
			 // ice rush
			 return 10;
		 case 12951:
			 // smoke blitz
			 return 28;
		 case 12999:
			 // shadow blitz
			 return 15;
		 case 12911:
			 return 10; // blood blitz
		 default:
			 return 31;
		 }
	 }
	 public int getStartDelay()
	 {
		 switch (player.playerEquipment[Constants.WEAPON_SLOT])
		 {
		 case 15241:
			 return 0;
		 }
		 switch (player.MAGIC_SPELLS[player.spellId][0])
		 {
		 case 1539:
			 return 60;
		 default:
			 return 53;
		 }
	 }
	 public int getStaffNeeded()
	 {
		 switch (player.MAGIC_SPELLS[player.spellId][0])
		 {
		 case 1539:
			 return 1409;
		 case 12037:
			 return 4170;
		 case 1190:
			 return 2415;
		 case 1191:
			 return 2416;
		 case 1192:
			 return 2417;
		 default:
			 return 0;
		 }
	 }
	 public boolean godSpells()
	 {
		 switch (player.MAGIC_SPELLS[player.spellId][0])
		 {
		 case 1190:
			 return true;
		 case 1191:
			 return true;
		 case 1192:
			 return true;
		 default:
			 return false;
		 }
	 }
	 public int getEndGfxHeight()
	 {
		 switch (player.MAGIC_SPELLS[player.oldSpellId][0])
		 {
		 case 12987:
		 case 12901:
		 case 12861:
		 case 12445:
		 case 1192:
		 case 13011:
		 case 12919:
		 case 12881:
		 case 12999:
		 case 12911:
		 case 12871:
		 case 13023:
		 case 12929:
		 case 12891:
			 return 0;
		 default:
			 return 100;
		 }
	 }
	 public int getStartGfxHeight()
	 {
		 switch (player.MAGIC_SPELLS[player.spellId][0])
		 {
		 case 12871:
		 case 12891:
			 return 0;
		 default:
			 return 100;
		 }
	 }

	 public void handleDfs() {
		 final Client o = (Client) PlayerHandler.players[player.playerIndex];
		 if (o == null) {
			 return;
		 }
		 if (System.currentTimeMillis() - player.dfsDelay < 60000) {
			 player.sendMessage("Your shield is cooling down.");
			 return;
		 }
		 if (player.playerEquipmentC[Constants.SHIELD_SLOT] == 0) {
			 player.sendMessage("Your shield has no charges left.");
			 return;
		 }
		 if (player.playerIndex > 0 && o != null) {
			 int damage = Misc.random(13) + Misc.random(12);
			 player.projectileStage = 2;
			 player.startAnimation(6696);
			 player.gfx0(1165);
			 if (damage > o.skillLevel[3]) {
				 damage = o.skillLevel[3];
			 }
			 appendHit(o, damage, 0, 7, true, 0);
			 o.gfx100(1167);
			 player.playerEquipmentC[Constants.SHIELD_SLOT] -= 1;
			 player.dfsDelay = System.currentTimeMillis();
		 }
	 }


	 public void handleDfsNPC() {
		 try {
			 if (player.npcIndex > 0) {
				 if (player.playerEquipmentC[Constants.SHIELD_SLOT] == 0) {
					 player.sendMessage("Your shield has no charges left.");
					 return;
				 }
				 if (NPCHandler.npcs[player.npcIndex] != null) {
					 player.projectileStage = 2;
					 if (System.currentTimeMillis() - player.dfsDelay > 60000) {
						 if (player.npcIndex > 0 && NPCHandler.npcs[player.npcIndex] != null) {
							 if (NPCHandler.npcs[player.npcIndex].isDead == true) {
								 player.sendMessage("This NPC is already dead!");
								 return;
							 }
							 int damage = Misc.random(25);
							 player.startAnimation(6696);
							 player.gfx0(1165);
							 player.playerEquipmentC[Constants.SHIELD_SLOT] -= 1;
							 if (damage > NPCHandler.npcs[player.npcIndex].HP) {
								 damage = NPCHandler.npcs[player.npcIndex].HP;
							 }
							 appendHit(NPCHandler.npcs[player.npcIndex], damage, 0, 7, 1);
							 NPCHandler.npcs[player.npcIndex].gfx100(1167);
							 player.dfsDelay = System.currentTimeMillis();
						 } else {
							 player.sendMessage("I should be in combat before using this.");
						 }
					 } else {
						 player.sendMessage("My shield hasn't finished recharging yet.");
					 }
				 }
			 }
		 }
		 catch (Exception e)
		 {}
	 }

	 public void applyRecoil(int damage, int i)
	 {
		 if (damage > 0 && PlayerHandler.players[i].playerEquipment[Constants.RING_SLOT] == 2550)
		 {
			 int recDamage = damage / 10 + 1;
			 appendHit(player, recDamage, 0, 3, false);
		 }
	 }
	 public int getBonusAttack(int i)
	 {
		 switch (NPCHandler.npcs[i].npcType)
		 {
		 case 2883:
			 return Misc.random(50) + 30;
		 case 2026:
		 case 2027:
		 case 2029:
		 case 2030:
			 return Misc.random(50) + 30;
		 }
		 return 0;
	 }
	 public void handleGmaulPlayer()
	 {
		 if (player.playerIndex > 0)
		 {
			 Client o = (Client) PlayerHandler.players[player.playerIndex];
			 if (player.goodDistance(player.getX(), player.getY(), o.getX(), o.getY(), getRequiredDistance()))
			 {
				 if (Attack.requirementsToAttack(player))
				 {
					 if (checkSpecAmount(4153))
					 {
						 int damage = 0;
						 if (meleeHitSuccess(calculateMeleeAttack(), o.getCombat().calculateMeleeDefence())) damage = Misc.random(calculateMeleeMaxHit());
						 if (o.prayerActive[18]) damage *= .6;
						 if (o.curseActive[o.curses().DEFLECT_MELEE]) damage *= .4;
						 player.startAnimation(1667);
						 player.gfx100(340);
						 if (damage > 48)
						 {
							 damage = Misc.random(48);
						 }
						 appendHit(o, damage, 0, 0, (calculateMeleeMaxHit() - 20) <= damage, 0);

					 }
				 }
			 }
		 }
	 }
	 public boolean armaNpc(int i)
	 {
		 switch (NPCHandler.npcs[i].npcType)
		 {
		 case 6222:
		 case 6223:
		 case 6229:
		 case 6225:
		 case 6230:
		 case 6227:
		 case 6232:
		 case 6239:
		 case 6233:
		 case 6231:
			 return true;
		 }
		 return false;
	 }

	 public static int finalMagicDamage(Client c)
	 {
		 int damage = c.MAGIC_SPELLS[c.spellId][6];
		 double damageMultiplier = 1;
		 switch (c.playerEquipment[Constants.WEAPON_SLOT])
		 {
		 case 4675:
			 // Ancient Staff
		 case 4710:
			 // Ahrim's Staff
		 case 4862:
			 // Ahrim's Staff
		 case 4864:
			 // Ahrim's Staff
		 case 4865:
			 // Ahrim's Staff
		 case 6914:
			 // Master Wand
		 case 8841:
			 // Void Knight Mace
		 case 13867:
			 // Zuriel's Staff
		 case 13869:
			 // Zuriel's Staff (Deg)
			 damageMultiplier += .12;
			 break;
		 case 15486:
			 // Staff of Light
			 damageMultiplier += .25;
			 break;
		 case 18355:
			 // Chaotic Staff
			 damageMultiplier += .37;
			 break;
		 }

		 switch (c.playerEquipment[Constants.AMULET_SLOT])
		 {
		 case 18335:
			 // Arcane Stream
			 damageMultiplier += .17;
			 break;
		 }
		 damage *= damageMultiplier;
		 return (int) damage;
	 }

	 /**
	  *
	  * @return true, if player is using a Chaotic weapon.
	  */
	  public boolean usingChaotic()
	 {
		  int ID = player.playerEquipment[Constants.WEAPON_SLOT];

		  if (ID >= 18349 && ID <= 18363)
		  {
			  return true;
		  }

		  return false;


	 }
}