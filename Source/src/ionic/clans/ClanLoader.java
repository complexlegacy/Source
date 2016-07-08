package ionic.clans;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/**
 * @author Keith
 */
public class ClanLoader {
	
	public ClanLoader(int file) {
		int clanId = 0;
		String owner = "";
		String prefix = "";
		String[] ranks = new String[100];
		int[] ranksN = new int[100];
		int whoCanEnter = 0;
		int whoCanTalk = 0;
		int whoCanKick = 0;

		String fileName = ""+file+".txt";
			BufferedReader reader = null;
			try {
				try {
				reader = new BufferedReader(new FileReader("./data/clans/"+fileName));
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
					if (line.startsWith("Clan Id")) {
						clanId = Integer.parseInt(s[1]);
					}
					if (line.startsWith("Clan Owner")) {
						owner = s[1];
					}
					if (line.startsWith("Clan Prefix")) {
						prefix = s[1];
					}
					if (line.startsWith("Who Can Enter")) {
						whoCanEnter = Integer.parseInt(s[1]);
					}
					if (line.startsWith("Who Can Talk")) {
						whoCanTalk = Integer.parseInt(s[1]);
					}
					if (line.startsWith("Who Can Kick")) {
						whoCanKick = Integer.parseInt(s[1]);
					}
					
					if (line.startsWith("Rank")) {
						int id = Integer.parseInt(s[1]);
						String name = s[2];
						int r = Integer.parseInt(s[3]);
						ranks[id] = name;
						ranksN[id] = r;
					}
					
					
					if (line.equals("[ENDOFCLAN]")) {
						ClanHandler.clans[clanId] = new Clan(clanId, owner, prefix, ranks, ranksN, whoCanEnter, whoCanTalk, whoCanKick);
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
