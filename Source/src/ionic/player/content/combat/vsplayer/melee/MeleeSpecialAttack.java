package ionic.player.content.combat.vsplayer.melee;

import ionic.player.Player;
import utility.Misc;

/**
 * Melee special attacks.
 * @author MGT Madness, created on 09-02-2014.
 */
public class MeleeSpecialAttack
{

        /**
         * Saradomin sword special attack effect..
         * @param attacker
         * 			The player attacking.
         * @param victim
         * 			The player under attack.
         */
        public static int saradominSwordSpecialAttack(Player attacker, int damage, int damageType)
        {
                if (damageType != 2)
                {
                        return damage;
                }
                if (!attacker.saradominSwordSpecialAttack)
                {
                        return damage;
                }
                damage = Misc.random(damage);
                if (damage > 18)
                {
                        damage = 18;
                }
                return damage;
        }

        /**
         * Calculate the Dragon claws special attack.
         * @param attacker
         * 			The player attacking.
         * @param victim
         * 			The player under attack.
         */
        public static void calculateDragonClawsSpecialAttack(Player attacker, Player victim)
        {
                if (!attacker.getDragonClawsSpecialAttack())
                {
                        return;
                }
                MeleeDamage.calculateDamage(attacker, victim, 1, true);
                int damage1 = attacker.meleeFirstDamage;
                int damage2 = 0;
                int damage3 = 0;
                int damage4 = 0;

                /* Start of First result. */
                if (damage1 > 0)
                {
                        damage2 = damage1 / 2;
                        damage3 = damage2 / 2;
                        damage4 = damage3;
                }
                /* End of First result. */
                else
                {
                        /* Start of Second result. */
                        MeleeDamage.calculateDamage(attacker, victim, 2, true);
                        damage2 = attacker.meleeSecondDamage;
                        if (damage2 > 0)
                        {
                                damage3 = damage2 / 2;
                                damage4 = damage3;
                        }
                        /* End of Second result. */

                        else
                        {

                                /* Start of Third result. */
                                attacker.thirdResultOfDragonClaws = true;
                                MeleeDamage.calculateDamage(attacker, victim, 3, true);
                                attacker.thirdResultOfDragonClaws = false;
                                damage3 = attacker.meleeThirdDamage;
                                if (damage3 > 0)
                                {
                                        MeleeDamage.calculateDamage(attacker, victim, 4, true);
                                        damage4 = attacker.meleeFourthDamage;
                                }
                                /* End of Third result. */

                                else
                                {

                                        /* Start of Fourth result. */
                                        MeleeDamage.calculateDamage(attacker, victim, 4, true);
                                        damage4 = attacker.meleeFourthDamage;
                                        /* End of Fourth result. */

                                }
                        }
                }
                attacker.getCombat().addCombatXP(0, (damage1 + damage2 + damage3 + damage4));
                attacker.meleeSecondDamage = damage2;
                attacker.meleeThirdDamage = damage3;
                attacker.meleeFourthDamage = damage4;
        		attacker.curses().vsPlayerSoulSplit(victim, damage1, false);
        		attacker.curses().vsPlayerSoulSplit(victim, damage2, false);
        		attacker.curses().vsPlayerSoulSplit(victim, damage3, false);
        		attacker.curses().vsPlayerSoulSplit(victim, damage4, false);
        }
}