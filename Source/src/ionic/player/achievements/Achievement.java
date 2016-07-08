package ionic.player.achievements;

public class Achievement {
	
	public String n; //Achievement name
	public String d; //Achievement description
	public int a; //Amount to complete achievement
	public int p; //Amount of points rewarded
	
	public Achievement(String name, String description, int amount, int reward) {
		this.n = name;
		this.d = description;
		this.a = amount;
		this.p = reward;
	}

}
