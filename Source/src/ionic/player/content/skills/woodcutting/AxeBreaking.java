package ionic.player.content.skills.woodcutting;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.dialogue.Dialogue;
import core.Constants;
import core.Server;
/**
 * @author Keith
 */
/**
 * Random event for woodcutting, where your axe breaks and you must pay to get it repaired.
 */
public class AxeBreaking {
	
	public AxeBreaking(Player c, AxeData a) {
		Dialogue.sendStatement2(c, new String[] {"Your axe head breaks and you drop the axe on the ground."});
		c.isChopping = false;
		if (c.playerEquipment[Constants.WEAPON_SLOT] == a.itemId) {
			c.playerEquipment[Constants.WEAPON_SLOT] = -1;
			ItemAssistant.updateSlot(c, Constants.WEAPON_SLOT);
		} else {
			ItemAssistant.deleteItem(c, a.itemId, 1);
		}
		Server.itemHandler.createGroundItem((Client)c, a.broken, c.getX(), c.getY(), 1, c.getId(), false);
		c.updateRequired = true;
        c.setAppearanceUpdateRequired(true);
        c.handler.updatePlayer(c, c.outStream);
	}

}
