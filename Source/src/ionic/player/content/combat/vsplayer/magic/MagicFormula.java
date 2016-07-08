package ionic.player.content.combat.vsplayer.magic;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import utility.Misc;
import core.Configuration;
import core.Constants;

/**
 * Player vs player Magic formulas
 * @author MGT Madness, created on 21-11-2013.
 */
public class MagicFormula
{

        /**
         * calculate the int magicDamage.
         * <p>
         * This is called when the animation starts.
         *
         * @param attacker The player who is attacking.
         * @param theTarget The player being attacked.
         */
        public static int calculateMagicDamage(Player attacker, Player victim)
        {
                attacker.maximumDamageMagic = maximumDamage(attacker);
                int damage = Misc.random(attacker.maximumDamageMagic); // The damage to the target.
                int damageBeforeAbsorb = damage;
                damage = getAbsorbtion(attacker, victim, damage);
                
                if (victim.curseActive[attacker.curses().DEFLECT_MAGIC])
                {
                        damage = (int) damage * 60 / 100;
                        victim.curses().deflect(attacker, 0, 0);
                }
                else if (victim.prayerActive[Constants.PROTECT_FROM_MAGIC])
                {
                        damage = (int) damage * 60 / 100;
                }
                if (damage == 0)
                {
                	damage = 1;
                }

                if (Configuration.COMBAT_DEBUG)
                {
                        attacker.sendMessage("------------------------------------------");
                        attacker.sendMessage("Magic max hit with " + ItemAssistant.getItemName(attacker.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase() + " is: " + attacker.maximumDamageMagic);
                        attacker.sendMessage("Magic difference: " + (attack(attacker) - defence(victim)));
                        attacker.sendMessage("Magic attack is: " + attack(attacker));
                        attacker.sendMessage("Magic defence of target: " + defence(victim));
                        if (!attacker.magicSplash)
                        {
                        		attacker.sendMessage("Magic damage before absorb: " + damageBeforeAbsorb);
                        		attacker.sendMessage("Magic damage after " + attacker.absorbtionAmountMagic + "% reduction: " + damage);
                        }
                        else
                        {
                        		attacker.sendMessage("Magic splashed.");
                        }
                }
                
                return attacker.magicDamage = damage;
        }

        /**
         * The maximum damage with magic.
         * @param player The player.
         * @return The maximum damage
         */
        public static int maximumDamage(Player player)
        {
                double damage = player.MAGIC_SPELLS[player.spellId][6];
                double bonusDamageMultiplier = 1;
                
                /* For each boosted magic level, the player receives 3% extra damage. */
                double magicLevelBonus = 0;
                magicLevelBonus = player.skillLevel[6] - player.getPA().getLevelForXP(player.playerXP[6]);
                magicLevelBonus *= 3.0 / 100.0;
                bonusDamageMultiplier += magicLevelBonus;
                
                switch (player.playerEquipment[Constants.WEAPON_SLOT])
                {
                case 4710: // Ahrim's Staff
                case 4675: // Ancient Staff
                case 13867: // Zuriel's Staff
                case 6914: // Master Wand
                	bonusDamageMultiplier += 0.10;
                	break;
                case 15486: // Staff of Light
                        bonusDamageMultiplier += 0.15;
                        break;
                case 18355: // Chaotic Staff
                        bonusDamageMultiplier += 0.20;
                        break;
                }

                if (player.playerEquipment[Constants.AMULET_SLOT] == 18335) // Arcane Stream necklace.
                {
                        bonusDamageMultiplier += 0.15;
                }
                damage *= bonusDamageMultiplier;
                int roundedDamage = (int) Math.round(damage);
                return (int) roundedDamage;
        }

        /**
         * True, if the random calculated difference between the player's magic attack and opponenet's magic defence is 0 or less.
         * <p>
         * Used to calculate weather a magic casted spell will be a splash.
         *
         * @param player The player that is attacking.
         * @param theTarget The player being attacked.
         * @return the splash result.
         */
        public static boolean isSplash(Player player, Player target)
        {
                int Difference = Misc.random(attack(player)) - Misc.random(defence(target));
                if (Difference <= 0)
                {
                        return true;
                }
                return false;
        }

        /**
         * Calculate the Magic attack advantage.
         * @param player The player.
         * @return Magic attack advantage.
         */
        public static int attack(Player player)
        {
                int attackTotal = player.skillLevel[Constants.MAGIC];
                if (attackTotal < 35)
                {
                	attackTotal = 35;
                }
                int itemBonus = player.playerBonus[3];
                attackTotal += itemBonus * 4.7;
                if (player.fullVoidMage())
                {
                        attackTotal += player.getLevelForXP(player.playerXP[6]) * 0.2;
                }
                if (player.prayerActive[4])
                {
                        attackTotal *= 1.05;
                }
                else if (player.prayerActive[12])
                {
                        attackTotal *= 1.10;
                }
                else if (player.prayerActive[20])
                {
                        attackTotal *= 1.15;
                }
                else if (player.curseActive[12]) // Curses magic
                {
                	attackTotal *= 1.10;
                }
                return (int)(attackTotal);
        }

        /**
         * Calculate the Magic defence advantage.
         * @param player The player.
         * @return Magic defence advantage.
         */
        public static int defence(Player player)
        {
                return (int)((player.playerBonus[8] * 5) + 35);
        }

        /**
         * Calculate the amount of absorbtion.
         *
         * @param player Player
         * @param damage Player's damage
         * @return the final damage after absorbtion.
         */
        private static int getAbsorbtion(Player player, Player target, int damage)
        {
                int AMOUNT_OF_DIFFERENCE = 30; // The difference amount between player attack and the target difference for less 1% absorb.
                int MAXIMUM_ABSORB_AMOUNT = 15; // The maximum percentage amount to absorb from the damage.

                int Difference = attack(player) - defence(target);
                if (Difference < 0)
                {
                        Difference = 0;
                }
                double result = 0;
                result = Difference / AMOUNT_OF_DIFFERENCE; // 30, so the maximum absorb amount is 30% and for each 30 difference, the less 1% absorb.
                if (result > MAXIMUM_ABSORB_AMOUNT)
                {
                        result = MAXIMUM_ABSORB_AMOUNT;
                }
                result = MAXIMUM_ABSORB_AMOUNT - result;
                result = 100 - result;
                result /= 100;
                player.absorbtionAmountMagic = result;
                return (int)(damage * result);
        }
        
        /**
         * Effects that are applied when the magic animation starts.
         * @param player
         * 			The player attacking.
         * @param theTarget
         * 			The player being attacked.
         */
        public static void Effects(Player player, Player victim)
        {
        	player.curses().vsPlayerSoulSplit(victim, player.magicDamage, false);	
        }

}