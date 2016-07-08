package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class BarrelchestAnchorSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
        c.gfx100(1027);
        c.startAnimation(5870);
        c.specAccuracy = 1.25;
        c.hitDelay = delay;
        c.setSpecialAttack(true);
	}

}
