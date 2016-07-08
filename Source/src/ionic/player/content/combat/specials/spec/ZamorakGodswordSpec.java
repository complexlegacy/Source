package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class ZamorakGodswordSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.startAnimation(7070);
        c.gfx0(1221);
        c.specAccuracy = 1.25;
        c.hitDelay = delay;
        c.setSpecialAttack(true);
        c.specDamage = 1.15;
	}

}
