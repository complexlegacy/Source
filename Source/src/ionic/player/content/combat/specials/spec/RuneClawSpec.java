package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class RuneClawSpec implements Special {

	@Override
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.startAnimation(923);
        c.specAccuracy = 1.25;
        c.hitDelay = delay;
        c.setSpecialAttack(true);
	}

}
