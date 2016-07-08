package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class DragonClawsSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.startAnimation(10961);
        c.gfx0(1950);
        c.hitDelay = delay;
        c.setSpecialAttack(true);
        c.setDragonClawsSpecialAttack(true);
        c.setMultipleDamageSpecialAttack(true);
        c.specEffect = 10;
	}

}
