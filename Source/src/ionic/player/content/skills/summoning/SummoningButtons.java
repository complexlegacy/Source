package ionic.player.content.skills.summoning;

import ionic.player.Player;

public class SummoningButtons {
	
	public static boolean click(Player c, int button) {
		switch(button) {
		
		case 154081://Withdraw from B.o.B
			return true;
			
		case 70109://Open B.o.B
			Summoning.openBob(c);
			return true;
			
		case 70112://Renew Familiar
			Summoning.renew(c);
			return true;
			
		case 70115://Call Familiar
			Summoning.call(c, false);
			return true;
			
		case 70118://Dismiss Familiar
			Summoning.dismiss(c);
			return true;
			
		}
		return false;
	}

}
