package ionic.player.profiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import core.Server;

public class ProfileLoader {

	public static void load() {
		String name = "";
		int weenColor = 0;
		int phatColor = 0;
		int[] compColor = new int[7];
		int[] equipment = new int[15];
		int[] equipmentN = new int[15];
		int[] appearance = new int[15];
		int[] skillXP = new int[25];
		DropLog[] drops = new DropLog[50];
		String[] v = null;
		String[] j = null;
		int loaded = 0;
		try {
			File folder = new File("./data/characters/profiles/");
			for (String fileName : folder.list()) {
				File f = new File(""+folder.getPath()+"/"+fileName+"");
				try {
					BufferedReader reader = new BufferedReader(new FileReader(f.getPath()));
					String line = "";
					try {
						line = reader.readLine();
					} catch (IOException localIOException2) {
					}
					try {
						while((line = reader.readLine()) != null) {
							v = line.split(" = ");

							if (line.startsWith("Owner")) {
								name = v[1];
							}
							if (line.startsWith("Phat Color")) {
								phatColor = Integer.parseInt(v[1]);
							}
							if (line.startsWith("Ween Color")) {
								weenColor = Integer.parseInt(v[1]);
							}
							
							if (line.startsWith("Equipment")) {
								j = v[1].split("_");
								for (int i = 0; i < j.length; i++) {
									equipment[i] = Integer.parseInt(j[i]);
								}
							}
							
							if (line.startsWith("AmountEquipment")) {
								j = v[1].split("_");
								for (int i = 0; i < j.length; i++) {
									equipmentN[i] = Integer.parseInt(j[i]);
								}
							}
							
							if (line.startsWith("Appearance")) {
								j = v[1].split("_");
								for (int i = 0; i < j.length; i++) {
									appearance[i] = Integer.parseInt(j[i]);
								}
							}
							
							if (line.startsWith("SkillXP")) {
								j = v[1].split("_");
								for (int i = 0; i < j.length; i++) {
									skillXP[i] = Integer.parseInt(j[i]);
								}
							}
							
							if (line.startsWith("CompColors")) {
								j = v[1].split("_");
								for (int i = 0; i < j.length; i++) {
									compColor[i] = Integer.parseInt(j[i]);
								}
							}
							
							if (line.startsWith("DropLog")) {
								drops[Integer.parseInt(v[1])] = new DropLog(Integer.parseInt(v[2]), Integer.parseInt(v[3]), v[4]);
							}
							
							if (line.startsWith("[END]")) {
								Server.profiles[loaded] = new Profile(name, skillXP, appearance, equipment, equipmentN, compColor, phatColor, weenColor, drops);
								loaded++;
								reader.close();
							}

						}
					} finally {
						reader.close();
					}
				} catch (FileNotFoundException e) {
				}
			}
		} catch (IOException ex) {

		}
	}

}
