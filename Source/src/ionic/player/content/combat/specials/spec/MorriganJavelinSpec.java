package ionic.player.content.combat.specials.spec;

import ionic.item.ItemAssistant;
import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;
import core.Constants;

public class MorriganJavelinSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		ItemAssistant.deleteEquipment(c);
        c.morrigansJavelinSpecialAttack = true;
        c.usingRangeWeapon = true;
        c.rangeItemUsed = c.playerEquipment[Constants.WEAPON_SLOT];
        ItemAssistant.deleteArrow(c);
        c.lastWeaponUsed = c.playerEquipment[Constants.WEAPON_SLOT];
        c.startAnimation(10501);
        c.gfx0(1836);
        c.specAccuracy = 1.5;
        c.hitDelay = 3;
        c.projectileStage = 1;
        c.hitDelay = delay;
        if (c.fightMode == 2) {
                c.attackTimer--;
        }
        if (c.playerIndex > 0) {
                c.getCombat().fireProjectilePlayer();
        } else if (c.npcIndex > 0) {
                c.getCombat().fireProjectileNpc();
        }
	}

}
