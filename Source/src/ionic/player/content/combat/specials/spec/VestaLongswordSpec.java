package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class VestaLongswordSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.startAnimation(10502);
        c.hitDelay = delay;
        c.specDamage = 1.05;
        c.specAccuracy = 2.50;
        c.setSpecialAttack(true);
	}

}
