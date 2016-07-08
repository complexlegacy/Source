package ionic.player.content.skills.woodcutting;

import ionic.item.ItemAssistant;
import ionic.player.Player;

/**
 * @author Keith
 *
 */
/**
 * After your hatchet breaks, if you use the hatchet stick on the hatchet head, it will repair.
 */

public class HatchetAttaching {
	
	private static final int AXE_HANDLE = 492;
	private static final int[] AXE_HEADS = {508, 510, 512, 514, 516, 518, 520, 6743};
	private static final int[] AXES = {1351, 1349, 1353, 1361, 1355, 1357, 1359, 6739};
	
	public HatchetAttaching(Player c, int used, int usedWith) {
		if (used == AXE_HANDLE || usedWith == AXE_HANDLE) {
			for (int i = 0; i < AXE_HEADS.length; i++) {
				if (used == AXE_HEADS[i] || usedWith == AXE_HEADS[i]) {
					ItemAssistant.deleteItem(c, used, 1);
					ItemAssistant.deleteItem(c, usedWith, 1);
					ItemAssistant.addItem(c, AXES[i], 1);
					c.sendMessage("You combine the axe head with your axe handle to repair your axe");
					break;
				}
			}
		}
	}

}
