package ionic.player.content.skills.woodcutting;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import core.Server;
import utility.Misc;

/**
 * 
 * @author Keith
 *
 */
/**
 * Handles birds nests being dropped and searching the nests
 */

public class BirdNest {
	
	private static int[] nests = {5070, 5071, 5072, 5073, 5074};
	
	
	public static void searchNest(Player c, int id) {
		boolean nestFound = false;
		for (int i = 0; i < nests.length; i++) {
			if (id == nests[i]) {
				nestFound = true;
				break;
			}
		}
		if (!nestFound) { return; }
		if (ItemAssistant.freeSlots(c) < 1) {
			c.sendMessage("You must have at least 2 free inventory spaces to search the nest.");
			return;
		}
		c.sendMessage("You start searching the bird's nest...");
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer e) {
				e.stop();
			}
			public void stop() {
				ItemAssistant.deleteItem(c, id, 1);
				ItemAssistant.addItem(c, 5075, 1);
				switch(id) {
					case 5070:
						ItemAssistant.addItem(c, 5076, 1); 
						c.sendMessage("You manage to find a red egg inside the nest");
					return;
					case 5071: 
						ItemAssistant.addItem(c, 5078, 1);
						c.sendMessage("You manage to find a green egg inside the nest");
					return;
					case 5072: 
						ItemAssistant.addItem(c, 5077, 1); 
						c.sendMessage("You manage to find a blue egg inside the nest");
					return;
					case 5073: giveSeed(c); return; //seeds
					case 5074: giveRing(c); return; //rings
				}
			}
		}, 3);
	}
	
	private static final int[] RINGS = { 1635, 1637, 1639, 1641, 1643 };
	private static final int[] SEEDS = { 5317, 5290, 5289, 5288, 5287, 5286, 5285, 5284, 5283, 5316, 5315, 5314, 5313, 5312};
	
	public static void giveSeed(Player c) {
		int seed = SEEDS[Misc.random(SEEDS.length - 1)];
		int amount = 1 + Misc.random(2);
		ItemAssistant.addItem(c, seed, amount);
		String m = "You search the nest and find "+amount+" "+ItemAssistant.getItemName(seed);
		if (amount > 1) { m += "s"; }
		c.sendMessage(m);
	}
	
	public static void giveRing(Player c) {
		int ring = RINGS[Misc.random(RINGS.length - 1)];
		ItemAssistant.addItem(c, ring, 1);
		c.sendMessage("You search the nest and find a "+ItemAssistant.getItemName(ring));
	}
	
	
	public BirdNest(Player c) {
		if (c.tree != null) {
			c.sendMessage("A birds nest falls out of the tree.");
			int nest = nests[Misc.random(nests.length - 1)];
			Server.itemHandler.createGroundItem((Client)c, nest, c.tree.x + 2, c.tree.y, 1, c.getId(), false);
		}
	}

}
