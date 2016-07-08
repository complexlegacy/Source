package ionic.player.content.miscellaneous;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.content.minigames.CastleWars;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import ionic.player.interfaces.AreaInterface;
import ionic.player.movement.Movement;

/**
 * Spellbook teleports for the Modern spellbook and Ancient magicks spellbook.
 * @author MGT Madness, created on 29-11-2013.
 */

public class Teleport
{

        /**
         * Teleport a player and perform the correct emote and gfx depending on their spellbook type.
         *
         * @param x x-axis coordinate of the player.
         * @param y y-axis coordinate of the player.
         * @param height height level of the player.
         */
        public static void spellTeleport(Client player, int x, int y, int height)
        {
                if (player.playerMagicBook != 2)
                {
                        startTeleport(player, x, y, height, player.playerMagicBook == 1 ? "ancient" : "modern");
                }
                else
                {
                        startTeleport(player, x, y, height, "lunar");
                }
        }

        /**
         * Initiate the spellbook teleport.
         */
        public static void startTeleport(final Client player, int x, int y, int height, final String teleportType)
        {
                player.getPA().removeAllWindows();
                if (!canTeleport(player, teleportType))
                {
                        return;
                }
                if (player.getCombat().inCombatAlert() && !teleportType.equalsIgnoreCase("lever") && !teleportType.equalsIgnoreCase("tab"))
                {
                	return;
                }
                if (player.resting) {
        			player.getPA().stopResting();
        		}
                AreaInterface.startInterfaceEvent(player);

                if (player.playerIndex > 0 || player.npcIndex > 0)
                {
                        player.getCombat().resetPlayerAttack();
                }
                player.isTeleporting = true;
                Movement.stopMovement(player);
                player.teleX = x;
                player.teleY = y;
                player.npcIndex = 0;
                player.playerIndex = 0;
                player.faceUpdate(0);
                player.teleHeight = height;

                if (teleportType.equalsIgnoreCase("modern"))
                {
                        player.startAnimation(8939);
                        player.teleportCycle = 3;
                        player.gfx0(1576);
                        player.teleEndGfx = 1577;
                        player.teleEndAnimation = 8941;
                }
                else if (teleportType.equalsIgnoreCase("ancient"))
                {
                        player.startAnimation(9599);
                        player.teleportCycle = 5;
                        player.teleEndAnimation = 65535;
                        player.gfx0(1681);
                }
                else if (teleportType.equalsIgnoreCase("lunar"))
                {
                        player.startAnimation(9606);
                        player.teleportCycle = 5;
                        player.teleEndAnimation = 65535;
                        player.gfx0(1685);
                }
                else if (teleportType.equalsIgnoreCase("tab"))
                {
                        player.startAnimation(9597);
                        player.teleportCycle = 4;
                        player.gfx0(1680);
                        player.teleEndAnimation = 65535;
                        ItemAssistant.deleteItem(player, 8013, ItemAssistant.getItemSlot(player, 8013), 1);
                        player.sendMessage("You break the tablet.");
                }
                else if (teleportType.equalsIgnoreCase("glory"))
                {
                        player.startAnimation(9603);
                        player.teleportCycle = 6;
                        player.gfx0(1684);
                }
                else if (teleportType.equalsIgnoreCase("lever"))
                {
                        player.startAnimation(2140);
                        player.teleportCycle = 4;
                        player.teleEndGfx = 1577;
                        player.teleEndAnimation = 8941;
                }

                teleportEvent(player, teleportType);
        }

        /**
         * The teleport cycle event.
         */
        public static void teleportEvent(final Client player, final String teleportType)
        {
                CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
                {@
                        Override
                        public void execute(CycleEventContainer container)
                        {
                                player.teleportCycle--;
                                if (teleportType.equalsIgnoreCase("tab") && player.teleportCycle == 2)
                                {
                                        player.startAnimation(4731);
                                        player.gfx0(678);
                                }
                                if (teleportType.equalsIgnoreCase("lever") && player.teleportCycle == 3)
                                {
                                	player.startAnimation(8939);
                                    player.gfx0(1576);
                                }

                                if (player.teleportCycle == 0)
                                {
                                        container.stop();
                                }
                        }@
                        Override
                        public void stop()
                        {
                                player.getPA().processTeleport();
                        }
                }, 1);
        }

        /**
         * True, if the player is allowed to teleport.
         * @param player
         * 			The player.
         * @param teleportType
         * 			The type of teleport the player is using.
         */
        public static boolean canTeleport(Client player, String teleportType)
        {
                if (player.inJail == true)
                {
                        player.sendMessage("You can't teleport out of jail.");
                        return false;
                }
                
                if (player.duelStatus == 5)
                {
                        player.sendMessage("You can't teleport during a duel!");
                        return false;
                }

                if (player.inWilderness() && player.wildLevel > 20 && !teleportType.equalsIgnoreCase("lever"))
                {
                        player.sendMessage("You can't teleport above level 20 in the wilderness.");
                        return false;
                }
                
                if (player.getHouse() != null && player.inConstruction()) {
                        player.getHouse().leave(player);
                }

                if (player.inWilderness() && System.currentTimeMillis() - player.teleBlockDelay < player.teleBlockLength)
                {
                        player.sendMessage("You are teleblocked and can't teleport.");
                        return false;
                }

                if (player.isDead || player.isTeleporting())
                {
                        return false;
                }
                return true;
        }

}