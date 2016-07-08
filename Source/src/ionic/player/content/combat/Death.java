package ionic.player.content.combat;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.content.minigames.CastleWars;
import ionic.player.content.minigames.FightPits;
import ionic.player.content.minigames.PestControl;
import ionic.player.content.miscellaneous.ItemsToInventoryDeath;
import ionic.player.content.miscellaneous.Skull;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import ionic.player.movement.Movement;
import ionic.player.tracking.KillLog;
import utility.Misc;
import core.Configuration;
import core.Constants;
import core.Server;

/**
 * Death stages.
 * @author MGT Madness, created on 25-11-2013.
 */
public class Death
{

        /**
         * Kill the player, once the player reaches 0 hitpoints.
         * @param victim
         * 			The player that died.
         */
        public static void killPlayer(final Client victim)
        {
                /* The player that killed the victim. */
                Client killer = (Client) PlayerHandler.players[victim.lastAttackedBy];
                if (killer != null)
                {
                	killer.getCombat().resetPlayerAttack();
                }
                victim.isDead(true);
                victim.startAnimation(0x900);
                deathEvent(victim);
                victim.getDuelArena().stakedItems.clear();
                if (victim.duelStatus != 6)
                {
                        victim.lastAttackedBy = victim.getPA().findKiller();
                        if (killer != null)
                        {
                                if (killer.duelStatus == 5)
                                {
                                        killer.duelStatus++;
                                }
                        }
                }
                victim.faceUpdate(0);
                Movement.stopMovement(victim);
                if (victim.duelStatus <= 4)
                {
                        KillLog.appendLog(victim, victim.lastAttackedBy);
                        if (victim.inWilderness() || victim.inSafePkArea())
                        {
                                if (victim.lastAttackedBy != victim.playerId)
                                {
                                        killer.sendMessage(deathMessage(victim));
                                }
                        }
                        victim.sendMessage("Oh dear, you died.");
                        if (!(victim.inWilderness()))
                        {
                                victim.safeDeath++;
                        }
                        if (victim.lastAttackedBy != victim.playerId) {
                                if (victim.inSafePkArea() || false) {
                                        killer.sendMessage(deathMessage(victim));
                                }
                                if (victim.inWilderness() && killer != null) {
                                        killer.pkPoints += 3;
                                        int bonus = getPkPoints(killer.killStreak);
                                        String extra = ".";
                                        if (bonus > 0) {
                                        	killer.pkPoints += bonus;
                                        	extra += "And an extra "+bonus+" points for having a "+killer.killStreak+" kill streak";
                                        }
                                        killer.sendMessage("You gain 3 pkPoints for the kill"+extra);
                                        killer.getPA().updatePlayerTab();
                                }
                        }
                }
                else if (victim.duelStatus != 6)
                {
                        victim.getDuelArena().stakedItems.clear();
                        victim.sendMessage("You have lost the duel!");
                }
                ItemAssistant.addSpecialBar(victim, victim.playerEquipment[Constants.WEAPON_SLOT]);;
                victim.getPA().resetDamageDone();
                victim.lastVeng = 0;
                victim.vengOn = false;
                victim.getPA().resetFollowers();
                victim.attackTimer = 10;
        }
        
        
        public static int getPkPoints(int streak) {
        	if (streak == 3) return 3;
        	if (streak == 5) return 5;
        	if (streak == 10) return 15;
        	if (streak == 15) return 25;
        	if (streak == 20) return 30;
        	if (streak == 30) return 50;
        	return 0;
        }
        

        /**
         * Give life to a dead player
         */
        public static void giveLife(Client victim)
        {
                victim.isDead(false);
                victim.getCombat().resetInCombat();
                victim.faceUpdate(-1);
                victim.freezeTimer = 0;
                victim.npcIndex = 0;
                victim.playerIndex = 0;
                diedInWilderness(victim);
                ItemAssistant.resetKeepItems(victim);

                victim.getCombat().resetPrayers();
                victim.safeTimer = 0;
                victim.underAttackBy = 0;
                victim.underAttackBy2 = 0;
                victim.logoutDelay = 0;
                victim.setHitPoints(victim.maximumHitPoints());
                victim.getCombat().resetPlayerAttack();
                victim.getPA().resetTb();
                Skull.clearSkull(victim);
                victim.damageTaken = new int[Configuration.MAX_PLAYERS];
                victim.specAmount = 10;
                ItemAssistant.addSpecialBar(victim, victim.playerEquipment[Constants.WEAPON_SLOT]);
                victim.getPA().requestUpdates();
                victim.poisonRunOut = 0;
                Poison.informClientOfPoisonOff(victim);
                for (int i = 0; i < 20; i++)
                {
                        victim.skillLevel[i] = victim.getPA().getLevelForXP(victim.playerXP[i]);
                        victim.getPA().refreshSkill(i);
                }
                deathRespawnArea(victim);
        }
        
        /**
         * The area to teleport the player to.
         * @param victim
         * 			The associated player.
         */
        private static void deathRespawnArea(Client victim)
        {
        	if (FightPits.inArena(victim)) {
        		victim.getPA().movePlayer(2399, 5174, 0);
        		return;
        	}
        	if (PestControl.isInGame(victim)) {
        		victim.getPA().movePlayer(2662, 2650, 0);
        		return;
        	}
        	if (victim.duelStatus <= 4)
            {
                    victim.getPA().movePlayer(Constants.deadRespawnX, Constants.deadRespawnY, 0);
            }
            else
            {
            	duelArenaDeath(victim);
            }
		}

		/**
         * The death stages done through Cycle Event.
         */
        private static void deathEvent(final Client player)
        {

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
                                Death.giveLife(player);
                        }
                }, 4);

        }

        public static String deathMessage(Client player)
        {
                int deathMessage = Misc.random(13);
                switch (deathMessage)
                {
                case 0:
                        return "With a crushing blow, you defeat " + player.playerName + ".";
                case 1:
                        return "It's a humiliating defeat for " + player.playerName + ".";
                case 2:
                        return "" + player.playerName + " didn't stand a chance against you.";
                case 3:
                        return "You've defeated " + player.playerName + ".";
                case 4:
                        return "" + player.playerName + " regrets the day they met you in combat.";
                case 5:
                        return "It's all over for " + player.playerName + ".";
                case 6:
                        return "" + player.playerName + " falls before your might.";
                case 7:
                        return "Can anyone defeat you? Certainly not " + player.playerName + ".";
                case 8:
                        return "You were clearly a better fighter than " + player.playerName + ".";
                case 9:
                        return "You pwned " + player.playerName + ".";
                case 10:
                        return "You have sent " + player.playerName + " to Edgeville.";
                case 11:
                        return "" + player.playerName + " Couldn't eat fast enough.";
                case 12:
                        return "You owned " + player.playerName + ".";
                case 13:
                        return "You demolished " + player.playerName + ".";
                }
                return null;
        }

        /**
         * Append specific updates to the player that died in the Wilderness.
         * @victim
         * 			The player who died.
         */
        private static void diedInWilderness(Client victim)
        {
                Client killer = (Client) PlayerHandler.players[victim.lastAttackedBy];
                
                if (!victim.inWilderness() || victim.inSafePkArea() || victim.isAdministratorRank() || killer.isAdministratorRank())
                {
                        return;
                }
                
                ItemAssistant.resetKeepItems(victim);
                
                if (!victim.getWhiteSkull())
                {
                        for (int i = 0; i < 3; i++)
                        {
                                ItemAssistant.keepItem(victim, i, true);
                        }
                }
                
                if (victim.prayerActive[Constants.PROTECT_ITEM])
                {
                        ItemAssistant.keepItem(victim, victim.getWhiteSkull() ? 1 : 3, true);
                }
                
                ItemsToInventoryDeath.getItemsToInventory(victim);
                dropItemsForKiller(victim);
                ItemAssistant.deleteAllItems(victim);
                
                if (!victim.getWhiteSkull())
                {
                        for (int i1 = 0; i1 < 3; i1++)
                        {
                                if (victim.itemKeptId[i1] > 0)
                                {
                                        ItemAssistant.addItem(victim, victim.itemKeptId[i1], 1);
                                }
                        }
                }
                else
                {
                        if (victim.prayerActive[Constants.PROTECT_ITEM])
                        {
                                ItemAssistant.addItem(victim, victim.itemKeptId[1], 1);
                        }
                }
                
                if (victim.prayerActive[Constants.PROTECT_ITEM])
                {
                        if (victim.itemKeptId[3] > 0)
                        {
                                ItemAssistant.addItem(victim, victim.itemKeptId[3], 1);
                        }
                }
                ItemsToInventoryDeath.itemsToInventory(victim);
                
        }
        
        /**
         * Append updates for when the player died in Duel arena.
         * @param victim
         * 			The associated player.
         */
        private static void duelArenaDeath(Client victim)
        {
        	Client o = (Client) PlayerHandler.players[victim.duelingWith];
            if (o != null)
            {
                    o.getPA().createPlayerHints(10, -1);
                    if (o.duelStatus == 6)
                    {
                            o.getDuelArena().duelVictory();
                    }
            }
        	o.getPA().movePlayer(Constants.DUEL_ARENA_X + (Misc.random(Constants.RANDOM_DISTANCE)), Constants.DUEL_ARENA_Y + (Misc.random(Constants.RANDOM_DISTANCE)), 0);
            victim.getPA().movePlayer(Constants.DUEL_ARENA_X + (Misc.random(Constants.RANDOM_DISTANCE)), Constants.DUEL_ARENA_Y + (Misc.random(Constants.RANDOM_DISTANCE)), 0);
            o.safeKill++;
            victim.sendMessage("You have lost the duel!");
            victim.safeDeath++;
            if (victim.duelStatus != 6)
            { // if we have won but have died, don't reset the duel status.
                    victim.getDuelArena().resetDuel();
            }
        }
        
        /**
         * Victim drops items for the killer.
         **/
        public static void dropItemsForKiller(Player player)
        {
                Client victim = (Client) player;
                Client killer = (Client) PlayerHandler.players[victim.lastAttackedBy];

                killer.CashPile = 0;
                for (int i = 0; i < victim.playerItems.length; i++)
                {
                        if (killer != null)
                        {
                                if (getItemCoinAmount(victim.playerItems[i] - 1) > 0)
                                {
                                        killer.CashPile += getItemCoinAmount(victim.playerItems[i] - 1);
                                }

                                if (!ItemAssistant.itemDissapear(victim.playerItems[i] - 1))
                                {
                                        Server.itemHandler.createGroundItem(killer, victim.playerItems[i] - 1, victim.getX(), victim.getY(), victim.playerItemsN[i], victim.lastAttackedBy, killer.playerName == victim.playerName && victim.inWilderness() ? true : false);
                                }
                        }
                }
                for (int e = 0; e < victim.playerEquipment.length; e++)
                {
                        if (killer != null)
                        {
                                if (getItemCoinAmount(victim.playerEquipment[e]) > 0)
                                {
                                        killer.CashPile += getItemCoinAmount(victim.playerEquipment[e]);
                                }
                                if (!ItemAssistant.itemDissapear(victim.playerEquipment[e]))
                                {
                                        Server.itemHandler.createGroundItem(killer, victim.playerEquipment[e], victim.getX(), victim.getY(), victim.playerEquipmentN[e], victim.lastAttackedBy, killer.playerName == victim.playerName && victim.inWilderness() ? true : false);
                                }
                        }
                }
                if (victim.lastAttackedBy != victim.playerId)
                {
                        killer.killCount++;
                        victim.deathCount++;
                        killer.killStreak++;
                        victim.killStreak = 0;
                }
                if (killer.CashPile > 0)
                {
                        Server.itemHandler.createGroundItem(killer, 995, victim.getX(), victim.getY(), killer.CashPile, victim.lastAttackedBy, false);
                }
                Server.itemHandler.createGroundItem(killer, 526, victim.getX(), victim.getY(), 1, victim.lastAttackedBy, false);
                if (killer.killStreak > killer.killStreakRecord)
                {
                        killer.killStreakRecord = killer.killStreak;
                        killer.sendMessage("Congratulations, you have set a new highest kill streak of " + killer.killStreakRecord + "");
                }

        }
        
        /**
         * This method is used to get the amount of coins of the un-trade-able item.
         * @param itemID
         * 			The item ID to check for it's coin amount.
         * @return
         * 			The amount of coins the item is worth.
         */
        public static int getItemCoinAmount(int itemID)
        {
                switch (itemID)
                {

                case 15018: // Seer's ring (i)
                        return 450000;
                case 15019: // Archer's ring (i)
                        return 450000;
                case 15220: // Berserker's ring (i)
                        return 450000;
                case 19111: // Tokhaar-kal
                        return 1000000;
                case 10551: // Fighter torso
                        return 400000;
                case 18335: // Arcane stream necklace
                        return 750000;
                case 18349: // Chaotic rapier
                        return 2000000;
                case 18351: // Chaotic longsword
                        return 2000000;
                case 18353: // Chaotic maul
                        return 2000000;
                case 18355: // Chaotic staff
                        return 1250000;
                case 18357: // Chaotic crossbow
                        return 1250000;
                case 18359: // Chaotic kiteshield
                        return 750000;
                case 18361: // Eagle-eye kiteshield
                        return 750000;
                case 18363: // Farseer kiteshield
                        return 750000;

                }
                return 0;
        }
}