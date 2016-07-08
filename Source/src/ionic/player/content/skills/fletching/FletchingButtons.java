package ionic.player.content.skills.fletching;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import ionic.player.dialogue.Dialogue;
import core.Constants;

/**
 * @author Keith
 */
public class FletchingButtons {
	
	public static int[][] buttons = {
			{10239, 10238, 6212, 6211},//interface with 1, item spot 1
			
			{34170, 34169, 34168, 34167},//interface with 2, item spot: 1
			{34174, 34173, 34172, 34171},//interface with 2, item spot: 2
			
			{34185, 34184, 34183, 34182},//interface with 3, item spot: 1
			{34189, 34188, 34187, 34186},//interface with 3, item spot: 2
			{34193, 34192, 34191, 34190},//interface with 3, item spot: 3
	};
	
	public FletchingButtons(Player c, int k, int b) {
		int i = getInterface(k);
		int amt = getAmount(b);
		FletchingData d = FletchingData.forID(c.fletchInterface, i);
			if (d == null) { return; }
			c.herbloreInterface = false;
		c.getPA().closeAllWindows();
		if (ItemAssistant.playerHasItem(c, d.used) && ItemAssistant.playerHasItem(c, d.used2)) {
			if (c.skillLevel[Constants.FLETCHING] < d.levelReq) {
				Dialogue.sendStatement2(c, new String[]{"You must have a fletching level of atleast "+d.levelReq+" to fletch this."});
				
				return;
			}
			new Fletching(c, d, amt);
		}
	}
	
	public int getInterface(int k) {
		int i = 0;
			if (k == 0) { i = 1; }
			if (k == 1) { i = 1; }
			if (k == 2) { i = 2; }
			if (k == 3) { i = 1; }
			if (k == 4) { i = 2; }
			if (k == 5) { i = 3; }
		return i;
	}
	public int getAmount(int d) {
		int a = 0;
			switch(d) {
				case 0: a = 1; break; //Make 1
				case 1: a = 5; break; //Make 5
				case 2: a = 10; break; //Make 10
				case 3: a = Integer.MAX_VALUE; break; //Make All
			}
		return a;
	}
	
}
