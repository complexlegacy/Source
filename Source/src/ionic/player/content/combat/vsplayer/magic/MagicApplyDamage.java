package ionic.player.content.combat.vsplayer.magic;

import ionic.player.Client;
import ionic.player.PlayerHandler;

/**
 * Apply the Magic hitsplat and other effects after it.
 * @author MGT Madness, created on 21-11-2013.
 */
public class MagicApplyDamage
{

        /**
         * Apply the Magic damage on target.
         */
        public static void applyDamage(final Client player, int theTarget)
        {

                Client target = (Client) PlayerHandler.players[theTarget];
                if (target == null)
                {
                        return;
                }
                int damage = player.magicDamage;
                
                if (player.magicSplash)
                {
                        damage = 0;
                }
                /* If the damage is more than the target's hitpoints, then adjust. */
                if (target.skillLevel[3] - damage < 0)
                {
                        damage = target.skillLevel[3];
                }
                
                Effects(player, theTarget, damage);
                target.logoutDelay = System.currentTimeMillis();
                target.underAttackBy = player.playerId;
                target.lastAttackedBy = player.playerId;
                if (player.MAGIC_SPELLS[player.oldSpellId][6] != 0)
                {
                        target.damageTaken[player.playerId] += damage;
                        player.totalPlayerDamageDealt += damage;
                        if (!player.magicSplash)
                        {
                        		player.getCombat().createHitsplatOnPlayer(player, target, damage, 0, 2);
                        }
                }
                player.getCombat().applySmite(target, damage);
                target.updateRequired = true;
                player.usingMagic = false;
                player.castingMagic = false;
                if (target.inMulti() && player.getCombat().multis())
                {
                        player.barrageCount = 0;
                        for (int j = 0; j < PlayerHandler.players.length; j++)
                        {
                                if (PlayerHandler.players[j] != null)
                                {
                                        if (j == target.playerId) continue;
                                        if (player.barrageCount >= 9) break;
                                        if (target.goodDistance(target.getX(), target.getY(), PlayerHandler.players[j].getX(), PlayerHandler.players[j].getY(), 1)) player.getCombat().appendMultiBarrage(j, player.magicSplash);
                                }
                        }
                }
                player.getPA().refreshSkill(3);
                player.getPA().refreshSkill(6);
                player.oldSpellId = 0;
        }

        /**
         * Effects that are applied after the Magic damage has appeared.
         *
         * @param player The player doing the attack.
         * @param theTarget The player being attacked.
         * @param damage The damaged dealt by the attacker.
         */
        public static void Effects(Client player, int theTarget, int damage)
        {
                Client target = (Client) PlayerHandler.players[theTarget];
                
                player.curses().vsPlayerSoulSplit(target, damage, true);
                
                if (target.vengOn)
                {
                        player.getCombat().appendVengeance(target, damage);
                }

                int endGFX = player.MAGIC_SPELLS[player.oldSpellId][5];
                
                if (endGFX == 369 && target.orb)
                {
                        endGFX = 1677;
                }
                target.orb = true;
                if (player.getCombat().getEndGfxHeight() == 100 && !player.magicSplash)
                {
                        target.gfx100(player.MAGIC_SPELLS[player.oldSpellId][5]);
                }
                else if (!player.magicSplash)
                {
                        if (endGFX == 1677) 
                        {
                        	target.gfx(1677, 50);
                        }
                        else 
                        {
                        	target.gfx0(player.MAGIC_SPELLS[player.oldSpellId][5]);
                        }
                }
                else if (player.magicSplash)
                {
                        target.gfx100(85);
                }
                if (!player.magicSplash)
                {
                        if (System.currentTimeMillis() - target.reduceStat > 35000)
                        {
                                target.reduceStat = System.currentTimeMillis();
                                switch (player.MAGIC_SPELLS[player.oldSpellId][0])
                                {
                                case 12987:
                                case 13011:
                                case 12999:
                                case 13023:
                                        target.skillLevel[0] -= ((target.getPA().getLevelForXP(target.playerXP[0]) * 10) / 100);
                                        break;
                                }
                        }
                        switch (player.MAGIC_SPELLS[player.oldSpellId][0])
                        {
                        case 12445:
                                //teleblock
                                if (player.playerMagicBook != 0)
                                {
                                        player.autocastId = -1;
                                        player.getPA().resetAutocast();
                                        return;
                                }
                                if (System.currentTimeMillis() - target.teleBlockDelay > target.teleBlockLength)
                                {
                                        target.teleBlockDelay = System.currentTimeMillis();
                                        target.sendMessage("You have been teleblocked.");
                                        if (target.prayerActive[16] || target.curseActive[player.curses().DEFLECT_MAGIC]) target.teleBlockLength = 150000;
                                        else target.teleBlockLength = 300000;
                                }
                                break;
                        case 12901:
                        case 12919:
                                // blood spells
                        case 12911:
                        case 12929:
                                int heal = (int)(damage / 4);
                                	player.addToHitPoints(heal);
                                break;
                        case 1153:
                                target.skillLevel[0] -= ((target.getPA().getLevelForXP(target.playerXP[0]) * 5) / 100);
                                target.sendMessage("Your attack level has been reduced!");
                                target.reduceSpellDelay[player.reduceSpellId] = System.currentTimeMillis();
                                target.getPA().refreshSkill(0);
                                break;
                        case 1157:
                                target.skillLevel[2] -= ((target.getPA().getLevelForXP(target.playerXP[2]) * 5) / 100);
                                target.sendMessage("Your strength level has been reduced!");
                                target.reduceSpellDelay[player.reduceSpellId] = System.currentTimeMillis();
                                target.getPA().refreshSkill(2);
                                break;
                        case 1161:
                                target.skillLevel[1] -= ((target.getPA().getLevelForXP(target.playerXP[1]) * 5) / 100);
                                target.sendMessage("Your defence level has been reduced!");
                                target.reduceSpellDelay[player.reduceSpellId] = System.currentTimeMillis();
                                target.getPA().refreshSkill(1);
                                break;
                        case 1542:
                                target.skillLevel[1] -= ((target.getPA().getLevelForXP(target.playerXP[1]) * 10) / 100);
                                target.sendMessage("Your defence level has been reduced!");
                                target.reduceSpellDelay[player.reduceSpellId] = System.currentTimeMillis();
                                target.getPA().refreshSkill(1);
                                break;
                        case 1543:
                                target.skillLevel[2] -= ((target.getPA().getLevelForXP(target.playerXP[2]) * 10) / 100);
                                target.sendMessage("Your strength level has been reduced!");
                                target.reduceSpellDelay[player.reduceSpellId] = System.currentTimeMillis();
                                target.getPA().refreshSkill(2);
                                break;
                        case 1562:
                                target.skillLevel[0] -= ((target.getPA().getLevelForXP(target.playerXP[0]) * 10) / 100);
                                target.sendMessage("Your attack level has been reduced!");
                                target.reduceSpellDelay[player.reduceSpellId] = System.currentTimeMillis();
                                target.getPA().refreshSkill(0);
                                break;
                        }
                }

        }

}