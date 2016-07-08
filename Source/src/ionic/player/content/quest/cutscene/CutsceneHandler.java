package ionic.player.content.quest.cutscene;

import ionic.player.Player;

public class CutsceneHandler {
	
	public static void send(Player player, Cutscene cutscene) {
		player.disableStuff = true;
		cutscene.send(player);
	}

}
