package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class VestaSpearSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.startAnimation(10499);
        c.gfx0(1835);
        c.setSpecialAttack(true);
        c.specAccuracy = 1.25;
        c.hitDelay = delay;
	}

}
