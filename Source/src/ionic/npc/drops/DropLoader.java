package ionic.npc.drops;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DropLoader {

	public DropLoader() {
		
		int[] npcs = new int[20];
		int npcsWithDrop = 0;
		boolean dropsLowHerbs = false;
		boolean dropsHighHerbs = false;
		int bones = -1;
		int totalDrops = 0;
		int[] drops = new int[200];
		int[] minAmounts = new int[200];
		int[] amounts = new int[200];
		DropType[] raritys = new DropType[200];
		
		try {
			File folder = new File("./data/npc/drops/");
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


							String[] s = line.split(" = ");
							if (line.startsWith("[NPC-ID")) {
								String ids = s[1].replaceAll("]", "");
								int id = Integer.parseInt(ids);
								npcs[npcsWithDrop] = id;
								npcsWithDrop ++;
							}
							if (line.startsWith("BONES DROP = ")) {
								bones = Integer.parseInt(s[1]);
							}
							if (line.startsWith("NPC DROPS LOW LEVEL HERBS = ")) {
								dropsLowHerbs = Boolean.parseBoolean(s[1]);
							}
							if (line.startsWith("NPC DROPS HIGH LEVEL HERBS = ")) {
								dropsHighHerbs = Boolean.parseBoolean(s[1]);
							}

							if (line.startsWith("DROP")) {
								String[] kk = s[1].split("\t");
								drops[totalDrops] = Integer.parseInt(kk[0]);
								String[] bb = kk[1].split(",");
								amounts[totalDrops] = Integer.parseInt(bb[0]);
								minAmounts[totalDrops] = Integer.parseInt(bb[1]);
								raritys[totalDrops] = DropType.forID(kk[2]);
								totalDrops++;
							}


							if (line.equals("[LOAD]")) {
								for (int i = 0; i < npcs.length; i++) {
									if (npcs[i] > 0) {
										Drop.npcDrops[npcs[i]] = new Drop(npcs[i], bones, dropsLowHerbs, dropsHighHerbs, drops, amounts, minAmounts, raritys);
									}
								}
								npcs = new int[20];
								npcsWithDrop = 0;
								dropsLowHerbs = false;
								dropsHighHerbs = false;
								bones = -1;
								totalDrops = 0;
								drops = new int[200];
								minAmounts = new int[200];
								amounts = new int[200];
								raritys = new DropType[200];
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
