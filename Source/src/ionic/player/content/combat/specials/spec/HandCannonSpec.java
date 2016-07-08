package ionic.player.content.combat.specials.spec;

import ionic.item.ItemAssistant;
import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;
import ionic.player.content.combat.vsplayer.range.RangeFormula;
import core.Constants;

public class HandCannonSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.handCannonSpecialAttack = true;
        RangeFormula.calculateSecondDamage(c, targetPlayer);
        c.usingRangeWeapon = true;
        c.rangeItemUsed = c.playerEquipment[Constants.WEAPON_SLOT];
        ItemAssistant.deleteArrow(c);
        c.lastWeaponUsed = c.playerEquipment[Constants.WEAPON_SLOT];
        c.startAnimation(12152);
        c.hitDelay = 3;
        c.projectileStage = 1;
        c.hitDelay = delay;
        ItemAssistant.deleteArrow(c);
        if (c.fightMode == 2) {
                c.attackTimer--;
        }
        if (c.playerIndex > 0) {
                c.getCombat().fireProjectilePlayer();
        }
	}

}
