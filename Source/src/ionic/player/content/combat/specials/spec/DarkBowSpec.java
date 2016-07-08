package ionic.player.content.combat.specials.spec;

import ionic.item.ItemAssistant;
import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;
import core.Constants;

public class DarkBowSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.usingBow = true;
        c.usingDarkBowSpecialAttack = true;
        c.rangeItemUsed = c.playerEquipment[Constants.ARROW_SLOT];
        ItemAssistant.deleteArrow(c);
        ItemAssistant.deleteArrow(c);
        c.lastWeaponUsed = c.playerEquipment[Constants.WEAPON_SLOT];
        c.hitDelay = 3;
        c.startAnimation(426);
        c.projectileStage = 1;
        c.gfx100(c.getCombat().getRangeStartGFX());
        c.hitDelay = delay;
        if (c.fightMode == 2) {
                c.attackTimer--;
        }
        if (c.playerIndex > 0) {
                c.getCombat().fireProjectilePlayer();
        } else if (c.npcIndex > 0) {
                c.getCombat().fireProjectileNpc();
        }
        c.specAccuracy = 1.40;
        c.specDamage = 1.65;
	}

}
