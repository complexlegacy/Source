package ionic.player.content.combat.specials;

import ionic.npc.NPC;
import ionic.player.Client;

public interface Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNPC);

}
