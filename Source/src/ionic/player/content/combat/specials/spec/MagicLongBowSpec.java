package ionic.player.content.combat.specials.spec;

import ionic.item.ItemAssistant;
import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;
import core.Constants;

public class MagicLongBowSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.usingBow = true;
        c.magicBowSpecialAttack = true;
        c.bowSpecShot = 3;
        c.rangeItemUsed = c.playerEquipment[Constants.ARROW_SLOT];
        ItemAssistant.deleteArrow(c);
        c.lastWeaponUsed = c.playerEquipment[Constants.WEAPON_SLOT];
        c.startAnimation(426);
        c.gfx100(250);
        c.hitDelay = delay;
        c.projectileStage = 1;
        if (c.fightMode == c.RAPID) {
                c.attackTimer--;
        }
	}

}
