package ionic.player.achievements;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AchievementLoader {

	public AchievementLoader() {
		for (int i = 0; i < 100; i++) {
			load("easy", i);
		}
		for (int i = 0; i < 100; i++) {
			load("medium", i);
		}
		for (int i = 0; i < 100; i++) {
			load("hard", i);
		}
		for (int i = 0; i < 100; i++) {
			load("elite", i);
		}
	}

	public static void load(String type, int file) {
		String fileName = ""+file+".txt";
		String name = "";
		String description = "";
		int req = 0;
		int reward = 0;
		BufferedReader reader = null;
		try {
			try {
				reader = new BufferedReader(new FileReader("./data/achievements/"+type+"/"+fileName));
			} catch (FileNotFoundException localFileNotFoundException) {
				return;
			}
			String line = "";
			try {
				line = reader.readLine();
			} catch (IOException localIOException2) {
			}
			try {
				while((line = reader.readLine()) != null) {
					String[] s = line.split(" = ");
					if (line.toLowerCase().startsWith("name")) {
						name = s[1];
					}
					if (line.toLowerCase().startsWith("description")) {
						description = s[1];
					}
					if (line.toLowerCase().startsWith("requirement")) {
						req = Integer.parseInt(s[1]);
					}
					if (line.toLowerCase().startsWith("reward")) {
						reward = Integer.parseInt(s[1]);
					}
					if (line.equals("[END]")) {
						if (type.equals("easy")) {
							AchievementData.easy[file] = AchievementData.a(name, description, req, reward);
						}
						if (type.equals("medium")) {
							AchievementData.medium[file] = AchievementData.a(name, description, req, reward);
						}
						if (type.equals("hard")) {
							AchievementData.hard[file] = AchievementData.a(name, description, req, reward);
						}
						if (type.equals("elite")) {
							AchievementData.elite[file] = AchievementData.a(name, description, req, reward);
						}
						reader.close();
					}
				}
			} finally {
				reader.close();
			}
		}catch (IOException ex) {
		}
	}

}
