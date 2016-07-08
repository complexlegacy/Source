package ionic.player.content.combat.vsplayer.melee;

import ionic.player.Player;
import utility.Misc;
import core.Constants;

/**
 * Melee damage related.
 * @author MGT Madness, created on 07-02-2014.
 */
public class MeleeDamage
{

        /**
         * True, if the damage will 0.
         *
         * @param c Player
         * @param i Other player
         */
        public static boolean isDamage0(Player player, Player victim)
        {
                int Difference;
                Difference = Misc.random(MeleeFormula.attack(player)) - Misc.random(MeleeFormula.defence(victim));
                if (Difference <= 0)
                {
                        return true;
                }
                return false;
        }

        /**
         * Calculate the amount of absorbtion, it is 30% max.
         * For each +30 difference between my attack and the target's defence, the less 1% absorb i get.
         *
         * @param player Player
         * @param damage Player's damage
         * @return the final damage after absorbtion.
         */
        public static int getAbsorbtion(Player player, int damage, Player victim)
        {
                int AMOUNT_OF_DIFFERENCE = 30; // The difference amount between player attack and the target difference for less 1% absorb.
                int MAXIMUM_ABSORB_AMOUNT = 30; // The maximum percentage amount to absorb from the damage.

                int Difference = MeleeFormula.attack(player) - MeleeFormula.defence(victim);
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
                player.absorbtionAmountMelee = result;

                /* If the damage is more than 2, then absorb it */
                if (damage > 2)
                {
                        damage *= result;
                }
                return (int)(damage);
        }

        /**
         * Calculate the damage that the player will deal.
         *
         * @param attacker
         * 			The player attacking.
         * @param victim
         * 			The player being attacked.
         * @param damageType
         * 			1 is for a single hitsplat, 2 is for the second hitsplat and so on..
         * @param calculatingDragonClawsSpecialAttack
         * 			True, if this method is being used for calculating dragon claws special attack damages.
         */
        public static void calculateDamage(Player attacker, Player victim, int damageType, boolean calculatingDragonClawsSpecialAttack)
        {
                int damage = 0;
                int damageBeforeReduction = 0;
                Melee.criticalDamage(attacker);

                if (isDamage0(attacker, victim))
                {
                        damage = 0;
                }
                else
                {
                        damage = Misc.random(MeleeFormula.maximumDamage(attacker));
                        damage = MeleeSpecialAttack.saradominSwordSpecialAttack(attacker, damage, damageType);
                        damageBeforeReduction = damage;

                        if (Misc.random(99) < 96)
                        {
                                damage = getAbsorbtion(attacker, damage, victim);
                        }
                }
                damage = victimPrayerActive(attacker, victim, damage);
                Melee.combatDebug(attacker, victim, damage, damageBeforeReduction);
                if (!calculatingDragonClawsSpecialAttack)
                {
                		attacker.curses().vsPlayerSoulSplit(victim, damage, false);
                }
                
                if (damageType == 1)
                {
                        attacker.meleeFirstDamage = damage;
                }
                else if (damageType == 2)
                {
                        attacker.meleeSecondDamage = damage;
                }
                else if (damageType == 3)
                {
                        attacker.meleeThirdDamage = damage;
                }
                else if (damageType == 4)
                {
                        attacker.meleeFourthDamage = damage;
                }
        }
        
        /**
         * Reduce the damage if the victim has melee protection prayer active.
         * @param attacker
         * 			The player attacking.
         * @param victim
         * 			The player under attack.
         * @param damage
         * 			The attacker's damage.
         */
        public static int victimPrayerActive(Player attacker, Player victim, int damage)
        {
        	if (victim.prayerActive[Constants.PROTECT_FROM_MELEE] && !attacker.getPA().fullVeracs())
            {
                    damage = (int) damage * 60 / 100;
            }
            else if (victim.curseActive[attacker.curses().DEFLECT_MELEE])
            {
                    damage = (int) damage * 60 / 100;
                    victim.curses().deflect(attacker, damage, 0);
            }
        	return damage;
        }
}