package ionic.player.profiles;

import java.math.BigInteger;

import core.Constants;
import ionic.item.Item;
import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.npc.NPCData;
import ionic.player.Player;
import ionic.player.banking.BankHandler;

public class ProfileSend {

	public static void send(Profile p, Player sendTo) {
		if (p == null) {
			return;
		}
		boolean showArms = false;
		boolean showHead = false;
		boolean showJaw = false;

		if (p.appearance[0] != 1 && !Item.isFullMask(p.equipment[Constants.HEAD_SLOT]))
			showJaw = true;
		if (!Item.isFullBody(p.equipment[Constants.TORSO_SLOT]))
			showArms = true;
		if (!Item.isNormalHelm(p.equipment[Constants.HEAD_SLOT]) && !Item.isFullMask(p.equipment[Constants.HEAD_SLOT]))
			showHead = true;
		String s = "";
		s += "ani"+getWeaponAnim(p.equipment[Constants.WEAPON_SLOT]);
		s += "`pha"+p.phatColor;
		s += "`wee"+p.weenColor;
		s += "`equ";
		for (int i : p.equipment) {
			s += ""+(i+512)+"_";
		}
		s += "`app";
		for (int i : p.appearance) {
			s += ""+(i + 256)+"_";
		}
		s += "`com";
		for (int i : p.compColors) {
			s += ""+i+"_";
		}
		s += "`sar"+showArms;
		s += "`she"+showHead;
		s += "`sja"+showJaw;
		sendTo.getPA().sendFrame126(s, 35521);


		int[] convert = {0, 2, 3, 1, 4, 6, 5, 16, 15, 17, 12, 9, 14, 13, 10,
				7, 11, 8, 20, 18, 19, 22, 21, 24};
		int totalLevel = 0;
		BigInteger totalXP = new BigInteger("0");
		for (int i = 0; i < 24; i++) {
			int level = sendTo.getPA().getLevelForXP(p.skillXP[convert[i]]);
			int xp = p.skillXP[convert[i]];
			sendTo.getPA().sendFrame126(""+level+"_"+BankHandler.insertCommas(""+xp)+"", 35541 + i);
			totalLevel += level;
			totalXP = totalXP.add(new BigInteger(""+xp));
		}

		sendTo.getPA().sendFrame126(""+totalLevel, 35568);
		sendTo.getPA().sendFrame126(""+BankHandler.insertCommas(""+totalXP), 35569);

		if (p.drops != null) {
			int[] drops = new int[50];
			int[] amounts = new int[50];
			for (int i = 0; i < p.drops.length; i++) {
				if (p.drops[i] != null) {
					sendTo.getPA().sendFrame126(""+NPCData.data[p.drops[i].npc].name+" - "+ItemData.data[p.drops[i].item].getName()+" - "+p.drops[i].date, 35573 + i);
					drops[i] = p.drops[i].item;
					amounts[i] = 1;
				}
			}
			ItemAssistant.itemsOnInterface(sendTo, 35571, drops, amounts);
		}
		
		sendTo.getPA().sendFrame34a(35523, p.equipment[0], 0, p.equipmentN[0]);
		sendTo.getPA().sendFrame34a(35524, p.equipment[2], 0, p.equipmentN[2]);
		sendTo.getPA().sendFrame34a(35525, p.equipment[1], 0, p.equipmentN[1]);
		sendTo.getPA().sendFrame34a(35526, p.equipment[13], 0, p.equipmentN[13]);
		sendTo.getPA().sendFrame34a(35527, p.equipment[4], 0, p.equipmentN[4]);
		sendTo.getPA().sendFrame34a(35528, p.equipment[3], 0, p.equipmentN[3]);
		sendTo.getPA().sendFrame34a(35529, p.equipment[5], 0, p.equipmentN[5]);
		sendTo.getPA().sendFrame34a(35530, p.equipment[7], 0, p.equipmentN[7]);
		sendTo.getPA().sendFrame34a(35531, p.equipment[10], 0, p.equipmentN[10]);
		sendTo.getPA().sendFrame34a(35532, p.equipment[9], 0, p.equipmentN[9]);
		sendTo.getPA().sendFrame34a(35533, p.equipment[12], 0, p.equipmentN[12]);
		
		sendTo.getPA().sendFrame126("Player Profile: "+p.name, 35502);
		sendTo.getPA().showInterface(35500);
	}


	public static int getWeaponAnim(int weapon) {
		String weaponName = "";
		if (weapon > 0)
			weaponName = ItemData.data[weapon].getName().toLowerCase();

		if (weaponName.contains("ahrim")) {
			return 809;
		}
		if (weaponName.contains("boxing")) {
			return 3677;
		}
		if (weaponName.contains("staff") || weaponName.contains("halberd") || weaponName.contains("guthan") || weaponName.contains("rapier") || weaponName.contains("wand")) {
			return 12010;
		}
		if (weaponName.contains("dharok")) {
			return 0x811;
		}
		if (weaponName.contains("verac")) {
			return 1832;
		}
		if (weaponName.contains("karil")) {
			return 2074;
		}
		if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.contains("saradomin sw")) {
			return 7047;
		}
		if (weaponName.contains("sword") || weaponName.contains("scimitar")) {
			return 0x328;
		}
		if (weaponName.contains("bow")) {
			return 808;
		}
		switch (weapon) {
		// Abyssal Whips.
		case 4151:
		case 15445:
		case 15444:
		case 15443:
		case 15442:
		case 15441:
		case 21371:
		case 21372:
		case 21373:
		case 21374:
		case 21375:
			return 11973;

		case 10887: // Barrelchest Anchor
			return 5869;

		case 15241:
			// Hand cannon
			return 12155;
		case 18355:
			// Chaotic staff
			return 808;
		case 6528:
			// Tzhaar-ket-om
			return 0x811;
		case 18353:
		case 4153:
			// Chaotic maul and Granite maul
			return 1662;
		case 1305:
			// Dragon longsword
			return 809;
		case 19784:
			return 809;
		case 11716:
			return 12010;
		}
		return 0x328;
	}


}
