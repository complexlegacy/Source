package ionic.player.content.combat.vsplayer.melee;

import ionic.player.Client;
import ionic.player.Player;
import ionic.player.content.combat.Poison;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import ionic.player.movement.Movement;
import utility.Misc;
import core.Constants;

/**
 * Create the melee hitsplat and other effects on the player.
 * @author MGT Madness, created on 20-11-2013.
 */
public class MeleeApplyDamage
{

        /**
         * Apply the melee damage on target.
         * @param attacker
         * 			The player applying the damage.
         * @param victim
         * 			The player being damaged.
         * @param firstHitsplat
         * 			True, to apply the damage of the first hitsplat. False to apply the second hitsplat(Like DDS second hitsplat).
         */
        public static void applyDamage(final Player attacker, Player victim, boolean firstHitsplat)
        {
                int damage = 0;
                if (firstHitsplat)
                {
                        damage = attacker.meleeFirstDamage;
                }
                else
                {
                        damage = attacker.meleeSecondDamage;
                }

                /* If the damage is more than the target's hitpoints, then adjust. */
                if (victim.skillLevel[Constants.HITPOINTS] - damage < 0)
                {
                        damage = victim.skillLevel[Constants.HITPOINTS];
                }

                victim.underAttackBy = attacker.playerId;
                victim.lastAttackedBy = attacker.playerId;
             	attacker.totalPlayerDamageDealt = 0;
                attacker.getCombat().applySmite(victim, damage);
                int hitIcon = 0;
                if (attacker.specEffect == 666) {
                	hitIcon = 2;
                }
                attacker.getCombat().createHitsplatOnPlayer(attacker, victim, damage, 0, hitIcon);
                if (firstHitsplat)
                {
                        victim.damageTaken[attacker.playerId] += damage;
                        attacker.totalPlayerDamageDealt += damage;
                        Effects(attacker, victim, damage);
                }
                attacker.curses().vsPlayerSoulSplit(victim, damage, true);
                attacker.specEffect = 0;
                applyDragonClawsDamage(attacker, victim);
        }

        /**
         * Melee effects of the damage. This is called after the Melee hitsplat appears.
         */
        public static void Effects(final Player attacker, final Player victim, int damage)
        {
                if (attacker.playerEquipment[Constants.WEAPON_SLOT] == 5698 && victim.poisonRunOut == 0 && damage > 0 && Misc.random(2) == 1)
                {
                        Poison.appendPoison(victim);
                }
                if (damage > 0)
                {
                        if (attacker.wearingFullGuthan && Misc.random(9) == 1)
                        {
                                attacker.addToHitPoints(damage);
                                if (attacker.skillLevel[3] > attacker.maximumHitPoints())
                                {
                                        attacker.setHitPoints(attacker.maximumHitPoints());
                                }
                                victim.gfx0(398);
                        }
                        if (victim.vengOn)
                        {
                                attacker.getCombat().appendVengeance(victim, damage);
                        }
                }
                final int DRAGON_SCIMITAR = 1;
                final int ZAMORAK_GODSWORD = 2;
                final int BANDOS_GODSWORD = 3;
                final int SARADOMIN_GODSWORD = 4;
                switch (attacker.specEffect)
                {
                case DRAGON_SCIMITAR:
                        if (damage > 0)
                        {
                                if (victim.prayerActive[Constants.PROTECT_FROM_MAGIC] || victim.prayerActive[Constants.PROTECT_FROM_RANGE] || victim.prayerActive[Constants.PROTECT_FROM_MELEE])
                                {
                                        victim.headIcon = -1;
                                        victim.getPA().sendFrame36(attacker.PRAYER_GLOW[16], 0);
                                        victim.getPA().sendFrame36(attacker.PRAYER_GLOW[17], 0);
                                        victim.getPA().sendFrame36(attacker.PRAYER_GLOW[18], 0);
                                }
                                if (victim.curseActive[victim.curses().DEFLECT_MELEE] || victim.curseActive[victim.curses().DEFLECT_MAGIC] || victim.curseActive[victim.curses().DEFLECT_MISSILES])
                                {
                                        victim.headIcon = -1;
                                        victim.curses().deactivate(victim.curses().DEFLECT_MAGIC);
                                        victim.curses().deactivate(victim.curses().DEFLECT_MISSILES);
                                        victim.curses().deactivate(victim.curses().DEFLECT_MELEE);
                                }
                                victim.sendMessage("You have been injured!");
                                victim.stopPrayerDelay = System.currentTimeMillis();
                                victim.prayerActive[16] = false;
                                victim.prayerActive[17] = false;
                                victim.prayerActive[18] = false;
                                victim.getPA().requestUpdates();
                        }
                        break;

                case ZAMORAK_GODSWORD:
                        if (damage > 0)
                        {
                                if (victim.freezeTimer <= 0)
                                {
                                        victim.freezeTimer = 30;
                                }
                                victim.gfx0(369);
                                victim.sendMessage("You have been frozen.");
                                attacker.sendMessage("You have frozen your target.");
                                victim.frozenBy = attacker.playerId;
                                Movement.stopMovement(victim);
                        }
                        break;

                case BANDOS_GODSWORD:
                        if (damage > 0)
                        {
                                victim.skillLevel[1] -= damage;
                                victim.sendMessage("You feel weak.");
                                if (victim.skillLevel[1] < 1)
                                {
                                        victim.skillLevel[1] = 1;
                                }
                                victim.getPA().refreshSkill(1);
                        }
                        break;

                case SARADOMIN_GODSWORD:
                        if (damage > 20)
                        {
                                attacker.addToHitPoints(damage / 2);
                                attacker.skillLevel[5] += damage / 4;
                                attacker.getPA().refreshSkill(5);
                        }
                        break;
                }
        }

        /**
         * Apply the third and fourth hitsplat of the Dragon claws special attack.
         * @param attacker
         * 			The player attacking.
         * @param victim
         * 			The player under attack.
         */
        public static void applyDragonClawsDamage(final Player attacker, final Player victim)
        {
                if (!attacker.getDragonClawsSpecialAttack())
                {
                        return;
                }
                if (attacker.getUsingDragonClawsSpecialAttackEvent())
                {
                	return;
                }
                attacker.setUsingDragonClawsSpecialAttackEvent(true);
                CycleEventHandler.getSingleton().addEvent(attacker, new CycleEvent()
                {@
                        Override
                        public void execute(CycleEventContainer container)
                        {
                                container.stop();
                        }@
                        Override
                        public void stop()
                        {
                        	 if (victim.skillLevel[Constants.HITPOINTS] - attacker.meleeThirdDamage < 0)
                             {
                        		 attacker.meleeThirdDamage = victim.skillLevel[3];
                             }
                        	 	attacker.getCombat().createHitsplatOnPlayer((Client) attacker, (Client) victim, attacker.meleeThirdDamage, 0, 0);
                        	 	
                        	 if (victim.skillLevel[Constants.HITPOINTS] - attacker.meleeFourthDamage < 0)
                             {
                        		 attacker.meleeFourthDamage = victim.skillLevel[3];
                             }
                                attacker.getCombat().createHitsplatOnPlayer((Client) attacker, (Client) victim, attacker.meleeFourthDamage, 0, 0);
                                attacker.setUsingDragonClawsSpecialAttackEvent(false);
                        }
                }, 1);
        }
}