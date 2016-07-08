package ionic.player.content.skills.herblore;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import core.Constants;

public enum FinishingPotions {
	
	ATTACK_POTION(121, 91, 221, 1, 25),
	ANTIPOISON(175, 93, 235, 5, 38),
	STRENGTH_POTION(115, 95, 225, 12, 50),
	RESTORE_POTION(127, 97, 223, 22, 63),
	ENERGY_POTION(3010, 97, 1975, 26, 68),
	DEFENCE_POTION(133, 99, 239, 30, 75),
	AGILITY_POTION(3034, 3002, 2152, 34, 80),
	COMBAT_POTION(9741, 97, 9736, 36, 84),
	PRAYER_POTION(139, 99, 231, 38, 88),
	SUMMONING_POTION(12142, 12181, 12109, 40, 92),
	CRAFTING_POTION(14840, 14856, 5004, 42, 92),
	SUPER_ATTACK(145, 101, 221, 45, 100),
	VIAL_OF_STENCH(18661, 101, 1871, 46, 0),
	FISHING_POTION(181, 101, 235, 48, 106),
	SUPER_ENERGY(3018, 103, 2970, 52, 118),
	SUPER_STRENGTH(157, 105, 225, 55, 125),
	WEAPON_POISON(187, 105, 241, 60, 138),
	SUPER_RESTORE(3026, 3004, 223, 63, 143),
	SUPER_DEFENCE(163, 107, 239, 66, 150),
	ANTIFIRE(2454, 2483, 241, 69, 158),
	RANGING_POTION(169, 109, 245, 72, 163),
	MAGIC_POTION(3042, 2483, 3138, 76, 173),
	ZAMORAK_BREW(189, 111, 247, 78, 175),
	SARADOMIN_BREW(6687, 3002, 6693, 81, 180),
	RECOVER_SPECIAL(15301, 3018, 5972, 84, 200),
	SUPER_ANTIFIRE(15305, 2454, 4621, 85, 210),
	EXTREME_ATTACK(15309, 145, 261, 88, 220),
	EXTREME_STRENGTH(15313, 257, 267, 89, 230),
	EXTREME_DEFENCE(15317, 163, 2481, 90, 240),
	EXTREME_MAGIC(15321, 3042, 9594, 91, 250),
	EXTREME_RANGING(15325, 169, 12539, 92, 260),
	SUPER_PRAYER(15329, 139, 4255, 94, 270);

	public int finishedPotion, unfinishedPotion, itemNeeded, levelReq, expGained;

	private FinishingPotions(int finishedPotion, int unfinishedPotion, int itemNeeded, int levelReq, int expGained) {
		this.finishedPotion = finishedPotion;
		this.unfinishedPotion = unfinishedPotion;
		this.itemNeeded = itemNeeded;
		this.levelReq = levelReq;
		this.expGained = expGained;
	}
	
	public static FinishingPotions forID(int unf, int item) {
		for (FinishingPotions f: FinishingPotions.values()) {
			if ((f.unfinishedPotion == unf && f.itemNeeded == item)
					|| (f.unfinishedPotion == item && f.itemNeeded == unf)) {
				return f;
			}
		}
		return null;
	}
	
	
	public static void checkMix(Player c, int use, int with) {
		FinishingPotions f = forID(use, with);
		if (f == null) { return; }
		if (c.skillLevel[Constants.HERBLORE] < f.levelReq) {
			c.getPA().closeAllWindows();
			c.sendMessage("You need a herblore level of atleast "+f.levelReq+" to make this.");
			return;
		}
		c.isFletching = false;
		c.herbloreInterface = true;
		c.herbResult2 = f;
		c.isUnfinished = false;
		UnfinishedPotions.sendInterface(c, f.finishedPotion);
	}
	public static void mix(Player c, int amount) {
		c.getPA().closeAllWindows();
		c.startAnimation(363);
		c.stopHerblore = false;
		c.herbloreMakeAmt = amount;
		c.isHerblore = true;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer e) {
				if (c.stopHerblore) {
					e.stop();
					return;
				}
				if (ItemAssistant.playerHasItem(c, c.herbResult2.unfinishedPotion) && ItemAssistant.playerHasItem(c, c.herbResult2.itemNeeded)) {
					c.startAnimation(363);
					ItemAssistant.deleteItem(c, c.herbResult2.unfinishedPotion, 1);
					ItemAssistant.deleteItem(c, c.herbResult2.itemNeeded, 1);
					ItemAssistant.addItem(c, c.herbResult2.finishedPotion, 1);
					c.herbloreMakeAmt--;
					c.getPA().addSkillXP(c.herbResult2.expGained * Constants.HERBLORE_EXPERIENCE, Constants.HERBLORE);
				} else {
					e.stop();
				}
				if (c.herbloreMakeAmt == 0) {
					e.stop();
				}
			}
			@Override
			public void stop() {
				c.stopHerblore = false;
				c.isHerblore = false;
			}
		}, 2);
	}

}
