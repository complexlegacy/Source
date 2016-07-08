package ionic.player.content.skills.thieving;

import ionic.item.ItemAssistant;
import ionic.npc.NPC;
import ionic.npc.NPCData;
import ionic.npc.NPCHandler;
import ionic.player.Player;
import ionic.player.content.skills.Skill;
import utility.Misc;
import core.Constants;
import core.Server;

public enum PickPocketing {
		MAN(new int[] {1,2,3,4,5,6}, 1, 1000, 8),
		FARMER(new int[] { 7, 1757, 1758, 1759, 1760, 1761 }, 10, 4000, 14), 
		FEMALE_HAM_MEMBER(new int[] { 1715 }, 15, 7000, 18), 
		MALE_HAM_MEMBER(new int[] { 1714, 1715 }, 20, 7000, 22), 
		HAM_GUARD(new int[] { 1710, 1711, 1712 }, 20, 7500, 22), 
		WARRIOR_WOMAN(new int[] { 15 }, 25, 10000, 26),
		AL_KHARID_WARRIOR(new int[] { 18 }, 25, 12500, 26), 
		ROGUE(new int[] { 187 }, 32, 13000, 35), 
		MASTER_FARMER(new int[] { 2234, 2235 }, 38, 14000, 43), 
		GUARD(new int[] { 9, 10 },40, 14200, 46), 
		POLLNIVIAN_BEARDED_BANDIT(new int[] { 1880, 1881 }, 45, 14800, 65), 
		DESERT_BANDIT(new int[] { 1926, 1927, 1928, 1929, 1930, 1931 }, 53, 15400, 79), 
		KNIGHT_OF_ARDOUGNE(new int[] { 23, 26 },55, 16000, 84), 
		POLLNIVIAN_BANDIT(new int[] { 1883, 1884 }, 55, 16200, 84), 
		YANILLE_WATCHMEN(new int[] { 32 }, 65, 17500, 137), 
		MENAPHITE_THUG(new int[] { 1904, 1905 }, 65, 17750, 137), 
		PALADIN(new int[] { 20, 365, 2256 },70, 18500, 151), 
		GNOME(new int[] { 66, 67, 68 }, 75, 20000, 198), 
		HERO(new int[] { 21 }, 80, 25000, 273),
		;
		public int[] npcs;
		public int req, amount, xp;
		private PickPocketing(int[] npcs, int lvlReq, int coinAmount, int xp) {
			this.npcs = npcs;
			this.req = lvlReq;
			this.amount = coinAmount;
			this.xp = xp;
		}
		
		private static PickPocketing forID(int npc) {
			for (PickPocketing s : PickPocketing.values()) {
				for (int i = 0; i < s.npcs.length; i++) {
					if (s.npcs[i] == npc) {
						return s;
					}
				}
			}
			return null;
		}
		
		public static void attemptPickpocket(Player c, NPC n) {
			if (c != null && n != null) {
				PickPocketing p = forID(n.npcType);
				if (p != null) {
					c.turnPlayerTo(n.absX, n.absY);
					if (c.skillLevel[Constants.THIEVING] < p.req) {
						c.sendMessage("You need a Thieving level of at least "+p.req+" to attempt pickpocketing this.");
						return;
					}
					if (System.currentTimeMillis() - c.lastThieve < 1000) {
						return;
					}
					c.lastThieve = System.currentTimeMillis();
					boolean success = Skill.isSuccess(c, Constants.THIEVING, p.req);
					if (success) {
						c.getPA().addSkillXP(p.xp * Constants.THIEVING_EXPERIENCE, Constants.THIEVING);
						ItemAssistant.addItem(c, 995, p.amount + Misc.random(p.amount));
						c.startAnimation(881);
						c.sendMessage("You successfully pickpocket the "+NPCData.data[n.npcType].name+"");
					} else {
						n.turnNpc(c.absX, c.absY);
						n.forceChat("Ayy! stay out of my pockets!");
						Server.npcHandler.startAnimation(NPCHandler.getAttackEmote(n.npcType), n.npcId);
						int damage = 1 + Misc.random(2);
						c.handleHitMask(damage, 0, 0, 0, true);
	        			c.dealDamage(damage);
					}
				}
			}
		}
	

}
