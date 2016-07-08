package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class DragonDaggersSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.gfx100(252);
        c.startAnimation(1062);
        c.hitDelay = delay;
        c.specAccuracy = 1.25;
        c.specDamage = 1.15;
        c.doubleHit = true;
        c.setSpecialAttack(true);
        c.setMultipleDamageSpecialAttack(true);
	}

}
