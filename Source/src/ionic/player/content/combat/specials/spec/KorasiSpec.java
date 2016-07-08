package ionic.player.content.combat.specials.spec;

import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;

public class KorasiSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.startAnimation(14788);
		c.specAccuracy = 6.00;
		c.specDamage = 2.00;
		c.hitDelay = delay + 2;
		c.specEffect = 666;
		c.gfx0(1729);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				container.stop();
			}
			@Override
			public void stop() {
				if (targetPlayer != null) {
					targetPlayer.gfx0(1730);
				}
				if (targetNpc != null) {
					targetNpc.gfx0(1730);
				}
			}
		}, 2);
	}

}
