package ionic.player.profiles;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import core.Server;
import ionic.player.Player;

public class Profile {

	public String name;
	public int[] skillXP;
	public int[] appearance;
	public int[] equipment;
	public int[] equipmentN;
	public int[] compColors;
	public int phatColor;
	public int weenColor;
	public DropLog[] drops;

	public Profile(String name, int[] skillXP, int[] appearance, int[] equipment, int[] equipmentN,
			int[] compColors, int phatColor, int weenColor, DropLog[] drops) {
		this.name = name;
		this.skillXP = skillXP;
		this.appearance = appearance;
		this.equipment = equipment;
		this.equipmentN = equipmentN;
		this.compColors = compColors;
		this.phatColor = phatColor;
		this.weenColor = weenColor;
		this.drops = drops;
	}

	public void save() {
		ProfileSaving.save(this);
	}
	public void update(Player owner) {
		skillXP = owner.playerXP;
		appearance = owner.playerAppearance;
		equipment = owner.playerEquipment;
		compColors = owner.compColor;
		phatColor = owner.phatColor;
		weenColor = owner.weenColor;
		equipmentN = owner.playerEquipmentN;
		save();
	}

	public static void newProfile(Player c) {
		int slot = 0;
		for (int i = 0; i < Server.profiles.length; i++) {
			if (Server.profiles[i] == null) {
				slot = i;
				break;
			}
		}
		Server.profiles[slot] = new Profile(c.playerName, c.playerXP, c.playerAppearance, 
				c.playerEquipment, c.playerEquipmentN, c.compColor, c.phatColor, 
				c.weenColor, new DropLog[50]);
		Server.profiles[slot].save();
		c.profile = Server.profiles[slot];
	}

	public static Profile search(String s) {
		s = s.toLowerCase();
		for (Profile d : Server.profiles) {
			if (d != null) {
				if (d.name.toLowerCase().equals(s)) {
					return d;
				}
			}
		}
		return null;
	}


	public static void newDropLog(Profile p, int item, int npc) {
		DropLog[] cache = new DropLog[50];
		for (int i = 0; i < 50; i++) {
			cache[i] = p.drops[i];
		}
		for (int i = 0; i < 50; i++) {
			if (i + 1 != 50) {
				p.drops[i + 1] = cache[i];
			}
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		p.drops[0] = new DropLog(item, npc, dateFormat.format(date));
	}


}
