package ionic.player.content.combat;

import ionic.item.ItemAssistant;
import ionic.npc.NPC;
import ionic.npc.NPCHandler;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.content.combat.specials.SpecialHandler;
import core.Configuration;

/**
 * 
 * @author Keith
 *
 */
public class SpecialAttack {

        public static void activateSpecial(final Player player, int weapon, int theTarget) {
        	 Client targetPlayer = null;
        	 if (theTarget <= Configuration.MAX_PLAYERS) {
        		 targetPlayer = (Client) PlayerHandler.players[theTarget];
        	 }
                NPC targetNPC = (NPC) NPCHandler.npcs[theTarget];
                if (targetNPC == null && player.npcIndex > 0) {
                        return;
                }
                if (targetPlayer == null && player.playerIndex > 0) {
                        return;
                }
                player.doubleHit = false;
                player.specEffect = 0;
                player.projectileStage = 0;
                if (player.npcIndex > 0) {
                        player.oldNpcIndex = targetNPC.npcId;
                } else if (player.playerIndex > 0) {
                        player.oldPlayerIndex = targetPlayer.playerId;
                        targetPlayer.underAttackBy = player.playerId;
                        targetPlayer.logoutDelay = System.currentTimeMillis();
                        targetPlayer.lastAttackedBy = player.playerId;
                }
                SpecialHandler.specialAttack((Client)player, weapon, targetPlayer, targetNPC);
                player.usingSpecial = false;
                ItemAssistant.updateSpecialBar(player);
        }

}