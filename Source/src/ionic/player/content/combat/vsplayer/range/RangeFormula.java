package ionic.player.content.combat.vsplayer.range;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import utility.Misc;
import core.Configuration;
import core.Constants;

/**
 * Player Range formulas.
 * @author MGT Madness, created on 17-11-2013.
 */

public class RangeFormula
{

    /**
     * Generate the range damage for normal attacks that only deal 1 hitsplat at a time.
     * <p>
     * This method is called during the weapon animation and the damage is used when the hitsplat appears.
     * Remember to update calculateSecondDamage same as this but change the rangeSecondDamage.
     * For calculateSecondDamage, remove the Bolts Effects part.
     * @param player
     * 			The player that is attacking.
     * @param theTarget
     * 			The player being attacked.
     */
    public static void calculateFirstDamage(Player player, Player target)
    {
        if (target == null)
        {
            return;
        }
        player.maximumDamageRange = maxDamage(player);

        int damage;
        boolean damageWillNotBe0 = false;
        
        /* Player activate Diamond bolts (e) special attack */
        if (player.lastArrowUsed == 9243 && Misc.random(99) < 5) // Diamond bolt (e)
        {
        	damageWillNotBe0 = true;
        	player.showDiamondBoltGFX = true;
        }
        int storeDamageBeforeAbsorb = 0;
        if (isDamage0(player, target) && !damageWillNotBe0)
        {
            damage = 0;
        }
        else
        {
            damage = Misc.random(player.maximumDamageRange);
            storeDamageBeforeAbsorb = damage;
            if (Misc.random(99) < 96 && !damageWillNotBe0) // Absorb damage 96% of time.
            {
            	damage = getAbsorbtion(player, damage, target);
            }
            
        }	

        /* Target prayer protect is active. */
        if (target.prayerActive[Constants.PROTECT_FROM_RANGE])
        {
            damage = (int) damage * 60 / 100;
        }
        
        /* Player Dark bow special attack is active. */
        if (player.usingDarkBowSpecialAttack && damage < 8)
        {
        	damage = 8;
        }
        
        /* Player activate Dragon bolts (e) special attack */
        if (player.lastArrowUsed == 9244 && target.playerEquipment[Constants.SHIELD_SLOT] != 1540 && target.playerEquipment[Constants.SHIELD_SLOT] != 11283 && !(target.getPA().antiFire() > 0) && Misc.random(99) < 5)
        {
        	player.showDragonBoltGFX = true;
        	damage *= 1.45;
    	}
        
        /* Player activate the Ruby bolts (e) special attack */
        if (player.lastArrowUsed == 9242 && damage > 0 && Misc.random(99) < 5)
        {
        	player.showRubyBoltGFX = true;
        }
        
        player.rangeSingleDamage = damage;
        player.getCombat().addCombatXP(1, damage);
        target.underAttackBy = player.playerId;
        target.logoutDelay = System.currentTimeMillis();
        
        if (Configuration.COMBAT_DEBUG)
        {
        	player.sendMessage("------------------------------------------");
            player.sendMessage("Range max hit with " + ItemAssistant.getItemName(player.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase() + " is: " + player.maximumDamageRange);
            player.sendMessage("Range Difference: " + (attack(player) - defence(target)));
            player.sendMessage("Range attack of myself: " + attack(player));
            player.sendMessage("Range defence of target: " + defence(target));
        	player.sendMessage("Range damage before absorb: " + storeDamageBeforeAbsorb);
        	player.sendMessage("Range damage after " + player.absorbtionAmountRange + "% absorb: " + damage);
        }
    }
    
    /**
     * Generate the range damage for double attacks that only deal 2 hitsplat at a time.
     * <p>
     * This method is called during the weapon animation and the damage is used when the hitsplat appears.
     * @param attacker
     * 			The player that is attacking.
     * @param theTarget
     * 			The player being attacked.
     */
    public static void calculateSecondDamage(Player attacker, Player victim)
    {
         if (victim == null)
         {
             return;
         }
         attacker.maximumDamageRange = maxDamage(attacker);

         int damage;
         boolean damageWillNotBe0 = false;
         
         int storeDamageBeforeAbsorb = 0;
         if (isDamage0(attacker, victim) && !damageWillNotBe0)
         {
             damage = 0;
         }
         else
         {
             damage = Misc.random(attacker.maximumDamageRange);
             storeDamageBeforeAbsorb = damage;
             if (Misc.random(99) < 96 && !damageWillNotBe0) // Absorb damage 96% of time.
             {
             	damage = getAbsorbtion(attacker, damage, victim);
             }
             
         }	

         /* Target prayer protect is active. */
         if (victim.prayerActive[Constants.PROTECT_FROM_RANGE])
         {
             damage = (int) damage * 60 / 100;
         }
         
         /* Player Dark bow special attack is active. */
         if (attacker.usingDarkBowSpecialAttack && damage < 8)
         {
         	damage = 8;
         }
         
         attacker.rangeSecondDamage = damage;
         attacker.curses().vsPlayerSoulSplit(victim, damage, false);
         attacker.getCombat().addCombatXP(1, damage);
         victim.underAttackBy = attacker.playerId;
         victim.logoutDelay = System.currentTimeMillis();
         
         if (Configuration.COMBAT_DEBUG)
         {
         	attacker.sendMessage("------------------------------------------");
             attacker.sendMessage("Range max hit with " + ItemAssistant.getItemName(attacker.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase() + " is: " + attacker.maximumDamageRange);
             attacker.sendMessage("Range Difference: " + (attack(attacker) - defence(victim)));
             attacker.sendMessage("Range attack of myself: " + attack(attacker));
             attacker.sendMessage("Range defence of target: " + defence(victim));
         	attacker.sendMessage("Range damage before absorb: " + storeDamageBeforeAbsorb);
         	attacker.sendMessage("Range damage after " + attacker.absorbtionAmountRange + "% absorb: " + damage);
         }
     }

    /**
     * Calculate the range attack advantage.
     * @param player The player
     * @return The range attack advantage.
     */
    public static int attack(Player player)
    {
        int attackLevel = player.skillLevel[Constants.RANGED];
        attackLevel += (player.playerBonus[4] * 5); // * 5 to make range attack bonus in the equipment interface matter more.

        if (player.prayerActive[3])
        {
            attackLevel *= 1.05;
        }
        else if (player.prayerActive[11])
        {
            attackLevel *= 1.10;
        }
        else if (player.prayerActive[19])
        {
            attackLevel *= 1.15;
        }
        else if (player.curseActive[11])
        {
            attackLevel *= 1.10;
        }
        if (player.fullVoidRange())
        {
            attackLevel += player.getLevelForXP(player.playerXP[Constants.RANGED]) * 0.1;
        }
        attackLevel *= player.specAccuracy;
        return (int)(attackLevel);
    }

    /**
     * Calculate the range defence advantage.
     *
     * @param player
     * 			The player.
     * @return
     * 		The defence advantage.
     */
    public static int defence(Player player)
    {
        int defenceLevel = player.skillLevel[Constants.DEFENCE];
        defenceLevel += (player.playerBonus[9] * 4.3); // * 5 to make range defence bonus in the equipment interface matter.

        if (player.prayerActive[0])
        {
            defenceLevel *= 1.05;
        }
        else if (player.prayerActive[5])
        {
            defenceLevel *= 1.1;
        }
        else if (player.prayerActive[13])
        {
            defenceLevel *= 1.15;
        }
        else if (player.prayerActive[24])
        {
            defenceLevel *= 1.2;
        }
        else if (player.prayerActive[25])
        {
            defenceLevel *= 1.25;
        }
        else if (player.curseActive[13]) // Leech Defence
        {
            defenceLevel *= 1.1;
        }

        return (int)(defenceLevel);
    }

    /**
     * Calculate weather the 0 should appear.
     *
     * @param player Player
     * @param i Other player
     */
    private static boolean isDamage0(Player player, Player target)
    {
        int Difference;
        Difference = Misc.random(attack(player)) - Misc.random(defence(target));
        if (Difference <= 0)
        {
            return true;
        }
        return false;
    }

    /**
     * Calculate the maximum range damage depending on weapon etc..
     * @param player
     * 			The player
     * @return
     * 			The max damage of range.
     */
    public static int maxDamage(Player player)
    {
        int rangeLevel = player.skillLevel[4];
        double modifier = 1.0;
        double specialAttackDamageMultiplier = player.specDamage;
        int itemUsed = player.lastArrowUsed;
        if (player.prayerActive[3])
        {
            modifier += 0.05;
        }
        else if (player.prayerActive[11])
        {
            modifier += 0.10;
        }
        else if (player.prayerActive[19])
        {
            modifier += 0.15;
        }
        if (player.fullVoidRange())
        {
            modifier += 0.20;
        }
        
        double c = modifier * rangeLevel;
        int rangeStr = rangeDamageBonus(itemUsed);
        
        // Morrigan's javelin.
        if (player.playerEquipment[Constants.WEAPON_SLOT] == 13879)
        {
        	rangeStr = 100;
        }
        
        // Morrigan's throwing axe.
        else if (player.playerEquipment[Constants.WEAPON_SLOT] == 13883)
        {
        	rangeStr = 110;
        }
        double maxDamage = (c + 8) * (rangeStr + 70) / 640;
        if (specialAttackDamageMultiplier != 1)
        {
            maxDamage *= specialAttackDamageMultiplier;
        }
        if (maxDamage < 1)
        {
            maxDamage = 1;
        }
        return (int) maxDamage;
    }

    public static int rangeDamageBonus(int i)
    {
        switch (i)
        {
        case 4214:
        	return 70;
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
            
        case 15243: // Hand cannon shot.
        	 return 140;
        	 
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
        case 4740:
            return 49;
        case 11212:
            return 60;
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
    
    /**
     * Calculate the amount of absorbtion, it is 30% max.
     * For each +30 difference between my attack and the target's defence, the less 1% absorb i get.
     *
     * @param player Player
     * @param damage Player's damage
     * @return the final damage after absorbtion.
     */
    private static int getAbsorbtion(Player player, int damage, Player target)
    {
    	
    	int AMOUNT_OF_DIFFERENCE = 30; // The difference amount between player attack and the target difference for less 1% absorb.
    	int MAXIMUM_ABSORB_AMOUNT = 30; // The maximum percentage amount to absorb from the damage.
    	
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
        player.absorbtionAmountRange = result;
        return (int)(damage * result);
    }
    
    /**
     * Range Effects that are called when the animation starts.
     * @param player
     * 			The player attacking.
     * @param theTarget
     * 			The player being attacked.
     * @param Type
     * 			1 for rangeSingleDamage and 2 for rangeSecondDamage.
     */
    public static void Effects(Player player, Player victim, int type)
    {
    	if (type == 1)
    	{
    			player.curses().vsPlayerSoulSplit(victim, player.rangeSingleDamage, false);
    	}
    	else
    	{
				player.curses().vsPlayerSoulSplit(victim, player.rangeSecondDamage, false);
    	}
    }

}