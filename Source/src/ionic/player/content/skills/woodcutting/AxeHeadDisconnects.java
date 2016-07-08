package ionic.player.content.skills.woodcutting;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.dialogue.Dialogue;
import utility.Misc;
import core.Constants;
import core.Server;

/**
 * @author Keith
 */
/**
 * Random event for woodcutting, where your axe head will disconnect
 * and end up on the ground near you.
 */
public class AxeHeadDisconnects {
	
	private static final int AXE_HANDLE = 492;
	
	public AxeHeadDisconnects(Player c, AxeData a) {
		Dialogue.sendStatement2(c, new String[] {"The head of your axe suddenly disconnects. Maybe you can find it on the ground?"});
		c.isChopping = false;
		boolean equipped = c.playerEquipment[Constants.WEAPON_SLOT] == a.itemId;
		if (equipped) {
			c.playerEquipment[Constants.WEAPON_SLOT] = -1;
			ItemAssistant.updateSlot(c, Constants.WEAPON_SLOT);
		} else {
			ItemAssistant.deleteItem(c, a.itemId, 1);
		}
		Server.itemHandler.createGroundItem((Client)c, AXE_HANDLE, c.getX(), c.getY(), 1, c.getId(), false);
		Server.itemHandler.createGroundItem((Client)c, a.head, randomX(c), randomY(c), 1, c.getId(), false);
		c.updateRequired = true;
        c.setAppearanceUpdateRequired(true);
        c.handler.updatePlayer(c, c.outStream);
	}
	
	public int randomX(Player c) {
		int r = Misc.random(1);
		int x = c.absX;
		if (r == 0) {
			x += Misc.random(10);
		} else {
			x -= Misc.random(10);
		}
		return x;
	}
	public int randomY(Player c) {
		int r = Misc.random(1);
		int y = c.absY;
		if (r == 0) {
			y += Misc.random(10);
		} else {
			y -= Misc.random(10);
		}
		return y;
	}
	
}
