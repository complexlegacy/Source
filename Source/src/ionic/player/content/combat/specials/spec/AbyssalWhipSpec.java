package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class AbyssalWhipSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		if (targetNpc != null && c.npcIndex > 0) {
                targetNpc.gfx100(341);
        }
        if (targetPlayer != null && c.playerIndex > 0) {
                targetPlayer.gfx100(341);
        }
        c.startAnimation(11956);
        c.hitDelay = delay;
        c.setSpecialAttack(true);
	}

}
