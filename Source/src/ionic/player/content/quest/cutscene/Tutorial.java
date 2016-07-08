package ionic.player.content.quest.cutscene;

import ionic.player.Player;

public class Tutorial implements Cutscene {

	@Override
	public void send(Player c) {
		
		c.stillCamera(14, 12, 40, 3, 4);
		
	}
	
	

}
