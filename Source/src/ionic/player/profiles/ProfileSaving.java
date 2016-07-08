package ionic.player.profiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ProfileSaving {
	
	public static void save(Profile p) {
		if (p == null) {
			System.out.println("null");
			return;
		}
		try {
			File file = new File("./data/characters/profiles/"+p.name+".txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
		
			bw.write("[PROFILE]");
			bw.newLine();
			bw.write("Owner = "+p.name);
			bw.newLine();
			bw.write("Phat Color = "+p.phatColor);
			bw.newLine();
			bw.write("Ween Color = "+p.weenColor);
			bw.newLine();
			bw.newLine();
			String content = "";
			
			content = "Equipment = ";
			for (int i : p.equipment) {
				content += ""+i+"_";
			}
			bw.write(content);
			bw.newLine();
			
			content = "AmountEquipment = ";
			for (int i : p.equipmentN) {
				content += ""+i+"_";
			}
			bw.write(content);
			bw.newLine();
			
			
			content = "Appearance = ";
			for (int i : p.appearance) {
				content += ""+i+"_";
			}
			bw.write(content);
			bw.newLine();
			
			
			content = "SkillXP = ";
			for (int i : p.skillXP) {
				content += ""+i+"_";
			}
			bw.write(content);
			bw.newLine();
			
			
			content = "CompColors = ";
			for (int i : p.compColors) {
				content += ""+i+"_";
			}
			bw.write(content);
			bw.newLine();
			bw.newLine();
			bw.newLine();
			
			for (int i = 0; i < 50; i++) {
				if (p.drops[i] != null) {
					bw.write("DropLog = "+i+" = "+p.drops[i].item+" = "+p.drops[i].npc+" = "+p.drops[i].date);
				}
			}
			bw.newLine();
			bw.newLine();
			bw.newLine();
			
			
			bw.write("[END]");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
