package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class SaradominSwordSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.startAnimation(11993);
        c.gfx100(1194);
        if (targetPlayer != null) {
        	targetPlayer.gfx100(1194);
        } 
        if (targetNpc != null) {
        	targetNpc.gfx100(1194);
        }
        c.hitDelay = delay;
        c.setSpecialAttack(true);
        c.saradominSwordSpecialAttack = true;
        c.setMultipleDamageSpecialAttack(true);
	}

}
