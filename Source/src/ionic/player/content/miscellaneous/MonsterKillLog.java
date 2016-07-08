package ionic.player.content.miscellaneous;

import ionic.npc.NPCData;
import ionic.npc.pet.BossPet;
import ionic.player.Player;
import ionic.player.content.miscellaneous.NpcInfoViewer.MonsterData;
import ionic.player.content.miscellaneous.NpcInfoViewer.MonsterType;

import java.io.*;

/**
 * Created by Jon & Keith on 2/9/2016.
 */
public class MonsterKillLog {

	public static void openLog(Player c) {
		int[] current = {0, 0, 0};
		int slot = 0;
		for (MonsterData d : MonsterData.values()) {
			if (d.type == MonsterType.NORMAL) {
				c.getPA().sendFrame126(""+capitalize(NPCData.data[d.npc].name)+": "+c.killLogs[slot], 35261 + current[0]);
				current[0] ++;
			} else if (d.type == MonsterType.BOSS) {
				c.getPA().sendFrame126(""+capitalize(NPCData.data[d.npc].name)+": "+c.killLogs[slot], 35321 + current[1]);
				current[1] ++;
			} else if (d.type == MonsterType.SLAYER) {
				c.getPA().sendFrame126(""+capitalize(NPCData.data[d.npc].name)+": "+c.killLogs[slot], 35361 + current[2]);
				current[2] ++;
			}
			slot++;
		}
		c.getPA().showInterface(35250);
	}
	
	public static String capitalize(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)),
						s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1),
							Character.toUpperCase(s.charAt(i + 1)),
							s.substring(i + 2));
				}
			}
		}
		return s;
	}

	public static void saveLog(Player player) {
		if (player == null) { return; }
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("./data/characters/killLogs/"+player.playerName+".txt"));
			writer.write("[MONSTERS]", 0, 10);
			writer.newLine();

			String s = "";
			int current = 0;
			for (MonsterData m : MonsterData.values()) {
				s = ""+m+" = "+player.killLogs[current]+"";
				writer.write(s);
				current++;
				writer.newLine();
			}
			writer.newLine();
			writer.newLine();
			writer.write("[BossPets]");
			writer.newLine();
			s = "";
			for (BossPet b : BossPet.values()) {
				s = ""+b+" = "+player.petKillLogs[b.getSlot()]+"";
				writer.write(s);
				current++;
				writer.newLine();
			}

			writer.write("[ENDOFFILE]", 0, 11);
			writer.close();
		} catch (IOException localIOException) {
		}
	}


	public static void loadLog(Player player) {
		BufferedReader reader = null;
		boolean loadingMonsters = true;
		String[] s = null;
		try {
			try {
				reader = new BufferedReader(new FileReader("./data/characters/killLogs/"+player.playerName+".txt"));
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
					if (line.startsWith("[BossPets]")) {
						loadingMonsters = false;
					}
					
					s = line.split(" = ");
					if (loadingMonsters) {
						int current = 0;
						for (MonsterData d : MonsterData.values()) {
							if (d.name().equals(s[0])) {
								player.killLogs[current] = Integer.parseInt(s[1]);
							}
							current++;
						}
					} else {
						for (BossPet d : BossPet.values()) {
							if (d.name().equals(s[0])) {
								player.petKillLogs[d.getSlot()] = Integer.parseInt(s[1]);
							}
						}
					}

					if (line.equals("[ENDOFCLAN]")) {
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
