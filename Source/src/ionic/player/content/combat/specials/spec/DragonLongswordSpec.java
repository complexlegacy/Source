package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class DragonLongswordSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.gfx100(248);
        c.startAnimation(1058);
        c.hitDelay = delay;
        c.specDamage = 1.3;
        c.setSpecialAttack(true);
	}

}
