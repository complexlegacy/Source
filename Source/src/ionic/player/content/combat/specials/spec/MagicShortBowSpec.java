package ionic.player.content.combat.specials.spec;

import ionic.item.ItemAssistant;
import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.Special;
import core.Constants;

public class MagicShortBowSpec implements Special {
	
	public void perform(Client c, int delay, Client targetPlayer, NPC targetNpc) {
		c.magicBowSpecialAttack = true;
        c.usingBow = true;
        c.bowSpecShot = 1;
        c.rangeItemUsed = c.playerEquipment[Constants.ARROW_SLOT];
        ItemAssistant.deleteArrow(c);
        ItemAssistant.deleteArrow(c);
        c.lastWeaponUsed = c.playerEquipment[Constants.WEAPON_SLOT];
        c.startAnimation(1074);
        c.hitDelay = 3;
        c.projectileStage = 1;
        c.hitDelay = delay;
        if (c.fightMode == c.RAPID) {
                c.attackTimer--;
        }
        if (c.playerIndex > 0) {
                c.getCombat().fireProjectilePlayer();
        } else if (c.npcIndex > 0) {
                c.getCombat().fireProjectileNpc();
        }
	}

}
