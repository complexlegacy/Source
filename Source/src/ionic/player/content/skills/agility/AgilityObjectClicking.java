package ionic.player.content.skills.agility;

import ionic.player.Player;

public class AgilityObjectClicking {
	
	public boolean inGnomeCourse(Player c) {
		return (c.absX >= 2461 && c.absY >= 3409 && c.absX <= 2494 && c.absY <= 3447);
	}
	public boolean inWildCourse(Player c) {
		return (c.absX >= 2980 && c.absY >= 3913 && c.absX <= 3016 && c.absY <= 3969);
	}
	public boolean inBarbCourse(Player c) {
		return (c.absX >= 2525 && c.absY >= 3539 && c.absX <= 2562 && c.absY <= 3577);
	}
	
	public AgilityObjectClicking(Player c, int objectType, int objectX, int objectY) {
		if (inGnomeCourse(c)) { GnomeCourse.clickObjects(c, objectType, objectX, objectY); }
		if (inWildCourse(c)) { WildernessCourse.clickObjects(c, objectType, objectX, objectY); }
		if (inBarbCourse(c)) { BarbarianCourse.clickObjects(c, objectType, objectX, objectY); }
	}
	
	
}