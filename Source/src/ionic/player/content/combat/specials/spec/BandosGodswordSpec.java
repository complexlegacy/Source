package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class BandosGodswordSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.startAnimation(11991);
        c.gfx0(2114);
        c.specDamage = 1.1;
        c.specAccuracy = 1.25;
        c.hitDelay = delay;
        c.setSpecialAttack(true);
	}

}
