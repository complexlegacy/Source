package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;

public class DragonSpearSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		if (c.inSafePkArea()) {
                c.sendMessage("You cannot use this here.");
                return;
        }
        c.startAnimation(406);
        c.gfx100(253);
        c.setSpecialAttack(true);
        if (c.playerIndex > 0) {
                targetPlayer.getPA().getSpeared(c.absX, c.absY);
                targetPlayer.gfx100(254);
                targetPlayer.startAnimation(targetPlayer.getCombat().getBlockAnimation());
                if (c.getPA().confirmMessage) {
                        c.sendMessage("The player cannot move that way!");
                        c.getPA().confirmMessage = false;
                }
        }
	}

}
