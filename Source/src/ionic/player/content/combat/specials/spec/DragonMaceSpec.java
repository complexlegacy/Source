package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class DragonMaceSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
        c.startAnimation(1060);
        c.gfx100(251);
        c.hitDelay = delay + 1;
        c.specDamage = 1.5;
        c.specAccuracy = 0.90;
        c.setSpecialAttack(true);
	}

}
