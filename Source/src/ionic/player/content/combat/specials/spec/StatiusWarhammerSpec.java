package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class StatiusWarhammerSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.startAnimation(10505);
        c.gfx0(1840);
        c.setSpecialAttack(true);
        c.hitDelay = delay + 1;
        c.specDamage = 1.09;
        c.specAccuracy = 1.25;
	}

}
