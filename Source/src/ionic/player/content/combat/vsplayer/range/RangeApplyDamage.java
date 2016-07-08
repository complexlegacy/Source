package ionic.player.content.combat.vsplayer.range;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;

/**
 * Handle the methods called once the range hitsplat appears for player vs player.
 * @author MGT Madness, created on 17-11-2013.
 */
public class RangeApplyDamage
{

    /**
     *
     * @param attacker
     * 			The player
     * @param theTarget
     * 			The player being attacked.
     * @param damageMask
     * 			1 is for single damages and 2 is for double damages.
     */
    public static void applyRangeDamage(Client attacker, int theTarget, int damageMask)
    {
        Client victim = (Client) PlayerHandler.players[theTarget];
        int damage;
        if (damageMask == 1)
        {
            damage = attacker.rangeSingleDamage;
        }
        else
        {
            damage = attacker.rangeSecondDamage;
        }
        
        /* If the damage is more than the target's hitpoints, then adjust. */
        if (victim.skillLevel[3] - damage < 0)
        {
                damage = victim.skillLevel[3];
        }
        
        	morrigansJavelinSpecialAttackDamage(attacker, victim, damage);
        	handCannonSpecialAttack(attacker, victim);
        if (attacker.showDiamondBoltGFX) // Diamond bolts (e) special attack.
        {
            victim.gfx0(758);
            attacker.showDiamondBoltGFX = false;
        }
        else if (attacker.showDragonBoltGFX) // Dragon bolts (e) special attack.
    	{
        	victim.gfx0(756);
        	attacker.showDragonBoltGFX = false;
     	}
        else if (attacker.showRubyBoltGFX) // Ruby bolts (e) special attack.
        {
        	victim.gfx0(754);
        	attacker.showRubyBoltGFX = false;
        	attacker.subtractFromHitPoints(attacker.skillLevel[3] / 10);
        	victim.subtractFromHitPoints(victim.skillLevel[3] / 8);
        }
        
        attacker.getCombat().applySmite(victim, damage);
        
        if (victim.vengOn)
        {
            attacker.getCombat().appendVengeance(victim, damage);
        }
        boolean dropArrows = true;
        for (int noArrowId: attacker.NO_ARROW_DROP)
        {
            if (attacker.lastWeaponUsed == noArrowId)
            {
                dropArrows = false;
                break;
            }
        }
        if (dropArrows)
        {
        	ItemAssistant.dropArrowPlayer(attacker);
        }
        PlayerHandler.players[theTarget].underAttackBy = attacker.playerId;
        PlayerHandler.players[theTarget].logoutDelay = System.currentTimeMillis();
        PlayerHandler.players[theTarget].lastAttackedBy = attacker.playerId;
        PlayerHandler.players[theTarget].damageTaken[attacker.playerId] += damage;
        attacker.getCombat().createHitsplatOnPlayer(attacker, victim, damage, 0, 1);
        if (damageMask == 2)
        {
        	attacker.magicBowSpecialAttack = attacker.usingDarkBowSpecialAttack = attacker.usingDarkBowNormalAttack = false;
        }
        PlayerHandler.players[theTarget].updateRequired = true;
        attacker.curses().vsPlayerSoulSplit(victim, damage, true);
    }
    
    private static void handCannonSpecialAttack(final Player attacker, final Player victim)
    {
    	if (!attacker.handCannonSpecialAttack)
    	{
    		return;
    	}
    	CycleEventHandler.getSingleton().addEvent(attacker, new CycleEvent()
        {@
                Override
                public void execute(CycleEventContainer container)
                {
        				if (victim.skillLevel[3] - attacker.rangeSecondDamage < 0)
        				{
        						attacker.rangeSecondDamage = victim.skillLevel[3];
        				}
        				attacker.getCombat().createHitsplatOnPlayer((Client) attacker, (Client) victim, attacker.rangeSecondDamage, 0, -1);
        				container.stop();
                }@
                Override
                public void stop()
                {
                }
        }, 2);
    }
    
    /**
     * Morrigan's javelin special attack effect.
     * @param attacker
     * 			The player attacking.
     * @param victim
     * 			The player under attack.
     * @param damage
     * 			The damage dealt by Morrigan's javelin special attack.
     */
    public static void morrigansJavelinSpecialAttackDamage(final Player attacker, final Player victim, int damage)
	{
		if (!attacker.morrigansJavelinSpecialAttack)
		{
			return;
		}
		attacker.amountOfDamages = damage / 5;
		if (attacker.amountOfDamages < 1)
		{
			return;
		}
		attacker.morrigansJavelinDamageToDeal = 5;
		
		CycleEventHandler.getSingleton().addEvent(attacker, new CycleEvent()
        {@
                Override
                public void execute(CycleEventContainer container)
                {
        	       if (!victim.isDead)
        	       {
        				if (attacker.amountOfDamages > 0)
        				{
        						attacker.amountOfDamages --;
        						if (victim.skillLevel[3] - attacker.morrigansJavelinDamageToDeal < 0)
                                {
                           		 	attacker.morrigansJavelinDamageToDeal = victim.skillLevel[3];
                                }
        						attacker.getCombat().createHitsplatOnPlayer((Client) attacker, (Client) victim, attacker.morrigansJavelinDamageToDeal, 0, -1);
        				}
        				else
        				{
        						container.stop();
        				}
        	       }
        	       else
        	       {
        	    	   container.stop();
        	       }
                }@
                Override
                public void stop()
                {
                }
        }, 2);
	}
}