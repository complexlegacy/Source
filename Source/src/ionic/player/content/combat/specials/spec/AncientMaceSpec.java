package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class AncientMaceSpec implements Special {

	@Override
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNPC) {
		c.startAnimation(6147);
		c.gfx0(1052);
        c.hitDelay = delay;
        c.setSpecialAttack(true);	
	}

}
