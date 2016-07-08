package ionic.player.content.combat.vsplayer.melee;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import core.Configuration;
import core.Constants;

/**
 * General melee related.
 * @author MGT Madness, created on 08-02-2014.
 */
public class Melee
{

        /**
         * A normal single hitsplat damage melee attack.
         * @param attacker
         * 			The player attacking.
         * @param theVictim
         * 			The player being attacked.
         * @param specialAttack
         * 			True, if the attacker is using a special attack.
         */
        public static void normalMeleeAttack(Player attacker, Player victim)
        {
                attacker.startAnimation(MeleeData.getWeaponAnimation(attacker, ItemAssistant.getItemName(attacker.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase()));
                MeleeDamage.calculateDamage(attacker, victim, 1, false);
                MeleeEffects.earlyEffects(attacker, victim, attacker.meleeFirstDamage, false);
                attacker.getCombat().addCombatXP(0, attacker.meleeFirstDamage);
                attacker.addAttacksGiven(1);
                victim.addAttacksReceived(1);
        }

        /**
         * Melee special attack.
         * @param attacker
         * 			The player attacking.
         * @param victim
         * 			The player under attack.
         */
        public static void meleeSpecialAttack(Player attacker, Player victim)
        {
                attacker.addAttacksGiven(1);
                victim.addAttacksReceived(1);
                singleHitSplatSpecialAttack(attacker, victim);
                multipleHitSplatSpecialAttack(attacker, victim);
        }

        /**
         * Special attacks that cause 1 hitsplat.
         * @param attacker
         * 			The player who is using the special attack.
         * @param victim
         * 			The player being attacked.
         */
        public static void singleHitSplatSpecialAttack(Player attacker, Player victim)
        {
                if (attacker.getMultipleDamageSpecialAttack())
                {
                        return;
                }
                MeleeDamage.calculateDamage(attacker, victim, 1, false);
                attacker.getCombat().addCombatXP(0, attacker.meleeFirstDamage);
        }

        /**
         * Special attacks that cause multiple hitsplats.
         * @param attacker
         * 			The player who is using the special attack.
         * @param victim
         * 			The player being attacked.
         */
        public static void multipleHitSplatSpecialAttack(Player attacker, Player victim)
        {
                if (attacker.getDragonClawsSpecialAttack())
                {
                        MeleeSpecialAttack.calculateDragonClawsSpecialAttack(attacker, victim);
                        return;
                }
                if (!attacker.getMultipleDamageSpecialAttack())
                {
                        return;
                }
                MeleeDamage.calculateDamage(attacker, victim, 1, false);
                MeleeDamage.calculateDamage(attacker, victim, 2, false);
                attacker.getCombat().addCombatXP(0, attacker.meleeSecondDamage + attacker.meleeFirstDamage);
        }

        /**
         * Grab the maximum melee damage of the attacker, to use with the 634 hitsplats criticals.
         * @param attacker
         * 			The player attacking.
         */
        public static void criticalDamage(Player attacker)
        {
                attacker.maximumDamageMelee = MeleeFormula.maximumDamage(attacker);
        }

        /**
         * Combat debugging messages.
         * @param attacker
         * 			The player attacking.
         * @param victim
         * 			The player under attack.
         * @param damage
         * 			The attacker's damage.
         * @param damageBeforeReduction
         * 			The attacker's damage before reduction occured.
         */
        public static void combatDebug(Player attacker, Player victim, int damage, int damageBeforeReduction)
        {
                if (Configuration.COMBAT_DEBUG)
                {
                        attacker.sendMessage("------------------------------------------");
                        attacker.sendMessage("Melee max hit with " + ItemAssistant.getItemName(attacker.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase() + " is: " + MeleeFormula.maximumDamage(attacker));
                        attacker.sendMessage("Melee difference: " + (MeleeFormula.attack(attacker) - MeleeFormula.defence(victim)));
                        attacker.sendMessage("Melee damage before reduction: " + damageBeforeReduction);
                        attacker.sendMessage("Melee damage after " + attacker.absorbtionAmountMelee + "% reduction: " + damage);
                        attacker.sendMessage("Melee attack is: " + MeleeFormula.attack(attacker));
                        attacker.sendMessage("Melee defence of target: " + MeleeFormula.defence(victim));
                }
        }

}