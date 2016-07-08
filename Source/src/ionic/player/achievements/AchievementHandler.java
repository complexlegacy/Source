package ionic.player.achievements;

import ionic.player.Player;


public class AchievementHandler {
	
	public static void add(Player c, int ach, String type, int amt) {
		switch(type.toLowerCase()) {
		
			case "easy": 
				if (c.easyProgress[ach] >= AchievementData.easy[ach].a) { 
					return; 
				} 
				if ((c.easyProgress[ach] + amt) > AchievementData.easy[ach].a) {
					amt = AchievementData.easy[ach].a - c.easyProgress[ach];
				}
				c.easyProgress[ach] += amt; 
			break;
			
			
			case "medium": 
				if (c.mediumProgress[ach] >= AchievementData.medium[ach].a) { 
					return; 
					} 
				if ((c.mediumProgress[ach] + amt) > AchievementData.medium[ach].a) {
					amt = AchievementData.medium[ach].a - c.mediumProgress[ach];
				}
				c.mediumProgress[ach] += amt; 
			break;
			
			
			case "hard": 
				if (c.hardProgress[ach] >= AchievementData.hard[ach].a) { 
					return; 
					} 
				if ((c.hardProgress[ach] + amt) > AchievementData.hard[ach].a) {
					amt = AchievementData.hard[ach].a - c.hardProgress[ach];
				}
				c.hardProgress[ach] += amt; 
			break;
			
			
			case "elite": 
				if (c.eliteProgress[ach] >= AchievementData.elite[ach].a) { 
					return; 
					} 
				if ((c.eliteProgress[ach] + amt) > AchievementData.elite[ach].a) {
					amt = AchievementData.elite[ach].a - c.eliteProgress[ach];
				}
				c.eliteProgress[ach] += amt; 
			break;
			
		}
		refreshAchievementTab(c);
	}
	
	public static void refreshAchievementTab(Player c) {
		String s = "";
		
		/** Refresh Easy **/
    	for (int i = 0; i < AchievementData.easy.length; i++) { 
    		String col = "@red@";
    		if (AchievementData.easy[i] == null) { 
    			continue; 
    		} 
		    	if (c.easyProgress[i] >= 1) {
		    		col = "@yel@"; 
		    	}
		    	if (c.easyProgress[i] >= AchievementData.easy[i].a) {
		    		col = "@gre@";
		    	}
		    	
    	s += "#_ _"+col+""+AchievementData.easy[i].n+"_ _"+AchievementData.easy[i].d+"_ _"
    			+ ""+AchievementData.easy[i].a+"_ _"+c.easyProgress[i]+"_ _"; 
    	}
    	c.getPA().sendFrame126(s, 26000);
    	s = "";
    	
    	
    	
    	/** Refresh Medium **/
    	for (int i = 0; i < AchievementData.medium.length; i++) { 
    		String col = "@red@";
    		if (AchievementData.medium[i] == null) { 
    			continue; 
    		} 
		    	if (c.mediumProgress[i] >= 1) { 
		    		col = "@yel@"; 
		    	}
		    	if (c.mediumProgress[i] >= AchievementData.medium[i].a) { 
		    		col = "@gre@"; 
		    	}
    	s += "#_ _"+col+""+AchievementData.medium[i].n+"_ _"+AchievementData.medium[i].d+"_ _"
    			+ ""+AchievementData.medium[i].a+"_ _"+c.mediumProgress[i]+"_ _"; }
    	c.getPA().sendFrame126(s, 26100);
    	s = "";
    	
    	
    	/** Refresh Hard **/
    	for (int i = 0; i < AchievementData.hard.length; i++) {
    		String col = "@red@";
    		if (AchievementData.hard[i] == null) {
    			continue; 
    		} 
		    	if (c.hardProgress[i] >= 1) { 
		    		col = "@yel@"; 
		    	}
		    	if (c.hardProgress[i] >= AchievementData.hard[i].a) { 
		    		col = "@gre@"; 
		    	}
    	s += "#_ _"+col+""+AchievementData.hard[i].n+"_ _"+AchievementData.hard[i].d+"_ _"
    			+ ""+AchievementData.hard[i].a+"_ _"+c.hardProgress[i]+"_ _"; }
    	c.getPA().sendFrame126(s, 26200); 
    	s = "";
    	
    	
    	
    	/** Refresh Elite **/
    	for (int i = 0; i < AchievementData.elite.length; i++) { 
    		String col = "@red@";
    		if (AchievementData.elite[i] == null) { 
    			continue;
    		} 
		    	if (c.eliteProgress[i] >= 1) { 
		    		col = "@yel@"; 
		    	}
		    	if (c.eliteProgress[i] >= AchievementData.elite[i].a) { 
		    		col = "@gre@"; 
		    	}
    	s += "#_ _"+col+""+AchievementData.elite[i].n+"_ _"+AchievementData.elite[i].d+"_ _"
    			+ ""+AchievementData.elite[i].a+"_ _"+c.eliteProgress[i]+"_ _";
    	}
    	c.getPA().sendFrame126(s, 26300);
    	s = "";
    	
    	
	}
	
	
	
	
}