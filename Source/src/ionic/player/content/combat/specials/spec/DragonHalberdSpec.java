package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class DragonHalberdSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNPC) {
		c.gfx100(282);
        c.startAnimation(1203);
        c.hitDelay = delay;
        c.setSpecialAttack(true);
        c.setMultipleDamageSpecialAttack(true);
        if (targetNPC != null && c.npcIndex > 0) {
                if (!c.goodDistance(c.getX(), c.getY(), targetNPC.getX(), targetNPC.getY(), 1)) {
                        c.doubleHit = true;
                }
        }
        if (targetPlayer != null && c.playerIndex > 0) {
            if (!c.goodDistance(c.getX(), c.getY(), targetPlayer.getX(), targetPlayer.getY(), 1)) {
                c.doubleHit = true;
            }
        }
	}

}
