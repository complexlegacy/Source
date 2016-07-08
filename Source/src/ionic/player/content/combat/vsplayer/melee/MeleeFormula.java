package ionic.player.content.combat.vsplayer.melee;

import ionic.player.Player;
import core.Constants;

/**
 * Melee Formulas.
 * @author MGT Madness, created on 20-11-2013.
 */
public class MeleeFormula
{

        /**
         * Calculate the total melee attack advantage the player has.
         * @param player The player
         */
        public static int attack(Player player)
        {
                double attackLevel = player.skillLevel[Constants.ATTACK];
                int itemBonus = player.playerBonus[meleeAttackBonus(player)];
                attackLevel += (itemBonus * 5.5);
                double bonusMultiplier = 1.0;
                /* Start of Prayer accuracy multiplier */
                if (player.prayerActive[Constants.CLARITY_OF_THOUGHT])
                {
                	bonusMultiplier += 0.05;
                }
                else if (player.prayerActive[Constants.IMPROVED_REFLEXES])
                {
                        bonusMultiplier += 0.10;
                }
                else if (player.prayerActive[Constants.INCREDIBLE_REFLEXES])
                {
                        bonusMultiplier += 0.15;
                }
                else if (player.prayerActive[Constants.CHIVALRY])
                {
                        bonusMultiplier += 0.15;
                }
                else if (player.prayerActive[Constants.PIETY])
                {
                        bonusMultiplier += 0.2;
                }
                else if (player.curseActive[player.curses().TURMOIL])
                {
                		bonusMultiplier += 0.25;
                }
                /* End of Prayer accuracy multiplier */

                /* Start of armour accuracy multiplier */
                if (player.fullVoidMelee())
                {
                	bonusMultiplier += 0.10;
                }
                /* End of armour accuracy multiplier */
                bonusMultiplier += player.specAccuracy - 1.0;
                attackLevel *= bonusMultiplier;
                int roundedAttack = (int) Math.round(attackLevel);
                return (int)(roundedAttack);
        }

        /**
         * @param player Player
         * @return the highest type of melee attack bonus.
         */
        private static int meleeAttackBonus(Player player)
        {
                if (player.playerBonus[0] > player.playerBonus[1] && player.playerBonus[0] > player.playerBonus[2])
                {
                        return 0;
                }
                if (player.playerBonus[1] > player.playerBonus[0] && player.playerBonus[1] > player.playerBonus[2])
                {
                        return 1;
                }
                return player.playerBonus[2] <= player.playerBonus[1] || player.playerBonus[2] <= player.playerBonus[0] ? 0 : 2;
        }

        /**
         * Calculate the total defence advantage the player has.
         */
        public static int defence(Player player)
        {
                int defenceLevel = player.skillLevel[Constants.DEFENCE];
                int bonus = player.playerBonus[meleeDefenceBonus(player)];
                defenceLevel += (bonus * 2.70);

                /* Start of prayer defence multiplier */
                if (player.prayerActive[Constants.THICK_SKIN])
                {
                        defenceLevel *= 1.05;
                }
                else if (player.prayerActive[Constants.ROCK_SKIN])
                {
                        defenceLevel *= 1.1;
                }
                else if (player.prayerActive[Constants.STEEL_SKIN])
                {
                        defenceLevel *= 1.15;
                }
                else if (player.prayerActive[Constants.CHIVALRY])
                {
                        defenceLevel *= 1.2;
                }
                else if (player.prayerActive[Constants.PIETY])
                {
                        defenceLevel *= 1.25;
                }
                else if (player.curseActive[player.curses().TURMOIL])
                {
                        defenceLevel = (int)(defenceLevel * player.curses().getTurmoilMultiplier("Defence"));
                }
                else if (player.curseActive[13]) // Leech Defence
                {
                        defenceLevel += player.getLevelForXP(player.playerXP[Constants.DEFENCE]) * 0.10;
                }
                /* End of prayer defence multiplier */
                return (int)(defenceLevel);
        }

        /**
         * @param player Player
         * @return the highest type of melee defence bonus.
         */
        public static int meleeDefenceBonus(Player player)
        {
                if (player.playerBonus[5] > player.playerBonus[6] && player.playerBonus[5] > player.playerBonus[7])
                {
                        return 5;
                }
                if (player.playerBonus[6] > player.playerBonus[5] && player.playerBonus[6] > player.playerBonus[7])
                {
                        return 6;
                }
                return player.playerBonus[7] <= player.playerBonus[5] || player.playerBonus[7] <= player.playerBonus[6] ? 5 : 7;
        }

        /**
         * Maximum damage of the player with melee.
         * @param player
         * 			The associated player.
         * @return
         * 			The maximum damage of melee.
         */
        public static int maximumDamage(Player player)
        {
                double maxHit = 0;
                int strength = player.skillLevel[2];
                int strBonus = player.playerBonus[10];
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
                maxHit += 1.05D + (double)(strBonus * strength) * 0.00160D;
                maxHit += (double) strength * 0.11D;

                if (player.playerEquipment[Constants.WEAPON_SLOT] == 4718 && player.playerEquipment[Constants.HEAD_SLOT] == 4716 && player.playerEquipment[Constants.TORSO_SLOT] == 4720 && player.playerEquipment[Constants.LEG_SLOT] == 4722)
                {
                        maxHit += (player.maximumHitPoints() - player.skillLevel[Constants.HITPOINTS]) / 2;		
                }
                if (player.specDamage > 1)
                {
                        maxHit = (int)(maxHit * player.specDamage);
                }
                if (player.fullVoidMelee())
                {
                        maxHit *= 1.1;
                }
                if (player.playerEquipment[Constants.AMULET_SLOT] == 11128 && player.playerEquipment[Constants.WEAPON_SLOT] == 6528)
                {
                        maxHit *= 1.25;
                }
                if (player.thirdResultOfDragonClaws)
                {
                	maxHit *= 0.75;
                }
                return (int) maxHit;
        }
}