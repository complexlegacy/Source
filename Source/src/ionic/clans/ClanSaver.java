package ionic.clans;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
/**
 * @author Keith
 */
public class ClanSaver {
	
	public ClanSaver(Clan c) {
		if (c == null) { return; }
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("./data/clans/"+c.clanId+".txt"));
			writer.write("[CLAN]", 0, 6);
			writer.newLine();
			String s = "";
			s = "Clan Id = "+c.clanId;
			writer.write(s, 0, s.length()); writer.newLine();
			s = "Clan Owner = "+c.owner;
			writer.write(s, 0, s.length()); writer.newLine();
			s = "Clan Prefix = "+c.prefix;
			writer.write(s, 0, s.length()); writer.newLine();
			s = "Who Can Enter = "+c.whoCanEnter;
			writer.write(s, 0, s.length()); writer.newLine();
			s = "Who Can Talk = "+c.whoCanTalk;
			writer.write(s, 0, s.length()); writer.newLine();
			s = "Who Can Kick = "+c.whoCanKick;
			writer.write(s, 0, s.length()); writer.newLine();
			writer.newLine();
			writer.newLine();
			writer.write("[CLAN RANKS]", 0, 12);
			writer.newLine();
			writer.newLine();
			for (int i = 0; i < 100; i++) {
				if (c.ranks[i] != null) {
					s = "Rank = "+i+" = "+c.ranks[i]+" = "+c.ranksN[i]+"";
					writer.write(s, 0, s.length()); 
					writer.newLine();
				}
			}
			writer.newLine();
			writer.newLine();
			writer.write("[ENDOFCLAN]", 0, 11);
			writer.close();
		} catch (IOException localIOException) {
	    }
	}

}
