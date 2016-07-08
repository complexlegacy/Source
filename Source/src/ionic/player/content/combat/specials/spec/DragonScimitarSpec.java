package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class DragonScimitarSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
        c.gfx100(347);
        c.startAnimation(1872);
        c.setSpecialAttack(true);
        c.specAccuracy = 1.2;
        c.hitDelay = delay;
	}

}
