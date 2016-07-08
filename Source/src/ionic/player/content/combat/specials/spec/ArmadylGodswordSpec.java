package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class ArmadylGodswordSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.startAnimation(11989);
        c.gfx0(2113);
        c.specDamage = 1.4;
        c.specAccuracy = 1.50;
        c.hitDelay = delay;
        c.setSpecialAttack(true);
	}

}
