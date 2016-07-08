package ionic.player.content.skills.herblore;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import core.Constants;

public enum UnfinishedPotions {
	
	GUAM_POTION(91, 249, 1),
	MARRENTILL_POTION(93, 251, 5),
	TARROMIN_POTION(95, 253, 12),
	HARRALANDER_POTION(97, 255, 22),
	RANARR_POTION(99, 257, 30),
	TOADFLAX_POTION(3002, 2998, 34),
	SPIRIT_WEED_POTION(12181, 12172, 40),
	IRIT_POTION(101, 259, 45),
	WERGALI_POTION(14856, 14854, 1),
	AVANTOE_POTION(103, 261, 50),
	KWUARM_POTION(105, 263, 55),
	SNAPDRAGON_POTION(3004, 3000, 63),
	CADANTINE_POTION(107, 265, 66),
	LANTADYME(2483, 2481, 69),
	DWARF_WEED_POTION(109, 267, 72),
	TORSTOL_POTION(111, 269, 78);

	public int unfinishedPotion, herbNeeded, levelReq;

	private UnfinishedPotions(int unfinishedPotion, int herbNeeded, int levelReq) {
		this.unfinishedPotion = unfinishedPotion;
		this.herbNeeded = herbNeeded;
		this.levelReq = levelReq;
	}
	
	public static UnfinishedPotions forID(int d) {
		for (UnfinishedPotions f : UnfinishedPotions.values()) {
			if (f.herbNeeded == d) {
				return f;
			}
		}
		return null;
	}
	
	public static void useItem(Player c, int use, int useWith) {
		int u = use == 227 ? useWith : use;
		UnfinishedPotions d = forID(u);
		if (d != null) {
			if (c.skillLevel[Constants.HERBLORE] < d.levelReq) {
				c.getPA().closeAllWindows();
				c.sendMessage("You need a herblore level of atleast "+d.levelReq+" to create this.");
				return;
			}
			c.fletchInterface = -1;
			c.inFletchInterface = false;
			c.herbloreInterface = true;
			c.herbResult = d;
			c.isUnfinished = true;
			sendInterface(c, d.unfinishedPotion);
		}
	}
	
	public static void selectAmount(Player c, int amount) {
		c.getPA().closeAllWindows();
		c.startAnimation(363);
		c.stopHerblore = false;
		c.herbloreMakeAmt = amount;
		c.isHerblore = true;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer e) {
				if (c.stopHerblore) {
					e.stop();
					return;
				}
				if (ItemAssistant.playerHasItem(c, 227) && ItemAssistant.playerHasItem(c, c.herbResult.herbNeeded)) {
					c.startAnimation(363);
					ItemAssistant.deleteItem(c, 227, 1);
					ItemAssistant.deleteItem(c, c.herbResult.herbNeeded, 1);
					ItemAssistant.addItem(c, c.herbResult.unfinishedPotion, 1);
					c.herbloreMakeAmt--;
				} else {
					e.stop();
				}
				if (c.herbloreMakeAmt == 0) {
					e.stop();
				}
			}
			@Override
			public void stop() {
				c.stopHerblore = false;
				c.isHerblore = false;
			}
		}, 2);
	}
	
	public static void sendInterface(Player c, int i1) {
		c.getPA().sendFrame164(4429);
     	c.getPA().sendFrame246(1746, 190, i1);
 		c.getPA().sendFrame126("\\n\\n\\n"+ItemAssistant.getItemName(i1)+"", 2799);
	}

}
