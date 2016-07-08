package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class GraniteMaulSpec implements Special {

	@Override
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNPC) {
		c.startAnimation(1667);
		c.gfx0(337);
		c.specAccuracy = 1.30;
		c.specDamage = 1.21;
		c.attackTimer = 1;
	}

}
