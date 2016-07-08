package ionic.player.achievements;

public class AchievementData {
	
	public static Achievement[] easy = new Achievement[100];
	public static Achievement[] medium = new Achievement[100];
	public static Achievement[] hard = new Achievement[100];
	public static Achievement[] elite = new Achievement[100];
	
	public static Achievement a(String n, String d, int a, int p) {
		return new Achievement(n, d, a, p);
	}

}
