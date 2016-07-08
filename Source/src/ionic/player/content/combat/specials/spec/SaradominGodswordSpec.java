package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class SaradominGodswordSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.startAnimation(12019);
        c.gfx0(2109);
        c.setSpecialAttack(true);
        c.specDamage = 1.15;
        c.hitDelay = delay;
	}

}
