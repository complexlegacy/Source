package ionic.npc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Keith
 */
public class NPCData {

	public static NPCData[] data = new NPCData[15000];

	public static void load() {

		int npcId = 0, 
				combatLvl = 0, 
				standAnim = 0, 
				walkAnim = 0, 
				hitPoints = 0,
				maxHit = 0, 
				attackAnim = 0, 
				attackLevel = 0, 
				defenceLevel = 0, 
				blockAnim = 0, 
				deathAnim = 0;
		String name = "";
		boolean aggressive = false;
		String[] s = null;
		BufferedReader reader = null;
		try {
			try {
				reader = new BufferedReader(new FileReader("./data/npc/NPCData.txt"));
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
					s = line.split(" = ");
					if (line.startsWith("ID")) {
						npcId = Integer.parseInt(s[1]);
					}
					if (line.startsWith("Name")) {
						name = s[1];
					}
					if (line.startsWith("Combat Level")) {
						combatLvl = Integer.parseInt(s[1]);
					}
					if (line.startsWith("Life Points")) {
						hitPoints = Integer.parseInt(s[1]);
					}
					if (line.startsWith("Max Hit")) {
						maxHit = Integer.parseInt(s[1]);
					}
					if (line.startsWith("Attack Level")) {
						attackLevel = Integer.parseInt(s[1]);
					}
					if (line.startsWith("Defence Level")) {
						defenceLevel = Integer.parseInt(s[1]);
					}
					if (line.startsWith("Attack Animation")) {
						attackAnim = Integer.parseInt(s[1]);
					}
					if (line.startsWith("Block Animation")) {
						blockAnim = Integer.parseInt(s[1]);
					}
					if (line.startsWith("Death Animation")) {
						deathAnim = Integer.parseInt(s[1]);
					}
					if (line.startsWith("Stand Animation")) {
						standAnim = Integer.parseInt(s[1]);
					}
					if (line.startsWith("Walk Animation")) {
						walkAnim = Integer.parseInt(s[1]);
					}
					if (line.startsWith("Agressive")) {
						aggressive = Boolean.parseBoolean(s[1]);
						data[npcId] = new NPCData(npcId, name, combatLvl, standAnim, walkAnim,
								attackAnim, blockAnim, deathAnim, maxHit, hitPoints,
								attackLevel, defenceLevel, aggressive);
					}
				}
			} finally {
				reader.close();
			}
		}catch (IOException ex) {
		}
	}



	public int 
	npcId, 
	combatLvl, 
	standAnim, 
	walkAnim, 
	hitPoints,
	maxHit, 
	attackAnim, 
	attackLevel, 
	defenceLevel, 
	blockAnim, 
	deathAnim;

	public String 
	name;

	public boolean 
	aggressive;


	public NPCData(int npcID, String npcName, int combatLevel, int standAnimation, int walkAnimation,
			int attackAnimation, int blockAnimation, int deathAnimation, int maxHit, int hitPoints,
			int attackLevel, int defenceLevel, boolean aggressive) {
		this.npcId = npcID;
		this.name = npcName;
		this.combatLvl = combatLevel;
		this.standAnim = standAnimation;
		this.walkAnim = walkAnimation;
		this.hitPoints = hitPoints;
		this.maxHit = maxHit;
		this.attackAnim = attackAnimation;
		this.blockAnim = blockAnimation;
		this.deathAnim = deathAnimation;
		this.attackLevel = attackLevel;
		this.defenceLevel = defenceLevel;
		this.aggressive = aggressive;
	}

}
