package ionic.player.content.miscellaneous;

import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Player;
import core.Constants;

/**
 * @author Keith
 */
public enum ItemCombinations {
	/**
	 * Ornaments
	 */
	FURY_ORNAMENT(19335, new int[] {19333, 6585}, false, true),
	DRAGON_PLATEBODY_GOLD(19337, new int[] {19350, 14479}, false, true),
	DRAGON_PLATEBODY_SPIKED(19342, new int[] {19358, 14479}, false, true),
	DRAGON_PLATELEGS_GOLD(19338, new int[] {19348, 4087}, false, true),
	DRAGON_PLATELEGS_SPIKED(19343, new int[] {19356, 4087}, false, true),
	DRAGON_FULL_HELM_GOLD(19336, new int[] {19346, 11335}, false, true),
	DRAGON_FULL_HELM_SPIKED(19341, new int[] {19354, 11335}, false, true),
	DRAGON_SQUARE_SHIELD_GOLD(19340, new int[] {19352, 1187}, false, true),
	DRAGON_SQUARE_SHIELD_SPIKED(19345, new int[] {19360, 1187}, false, true),
	DRAGON_PLATESKIRT_GOLD(19339, new int[] {19348, 4585}, false, true),
	DRAGON_PLATESKIRT_SPIKED(19344, new int[] {19356, 4585}, false, true),
	
	/**
	 * Godswords
	 */
	GODSWORD_BLADE(11690, new int[] {11710, 11712, 11714}, false, false),
	BANDOS_GODSWORD(11696, new int[] {11690, 11704}, false, true),
	ZAMORAK_GODSWORD(11700, new int[] {11690, 11708}, false, true),
	SARADOMIN_GODSWORD(11698, new int[] {11690, 11706}, false, true),
	ARMADYL_GODSWORD(11694, new int[] {11690, 11702}, false, true),
	
	/**
	 * Spirit shields
	 */
	BLESSED_SPIRIT_SHIELD(13736, new int[] {13734, 13754}, true, false),
	SPECTRAL_SPIRIT_SHIELD(13744, new int[] {13752, 13736}, true, false),
	ARCANE_SPIRIT_SHIELD(13738, new int[] {13746, 13736}, true, false),
	ELYSIAN_SPIRIT_SHIELD(13742, new int[] {13750, 13736}, true, false),
	DIVINE_SPIRIT_SHIELD(13740, new int[] {13748, 13736}, true, false),
	;
	public int[] neededItems;
	public int resultItem;
	public boolean hasReq;
	public boolean canSplit;
	private ItemCombinations(int r, int[] n, boolean hr, boolean s) {
		this.neededItems = n;
		this.resultItem = r;
		this.hasReq = hr;
		this.canSplit = s;
	}

	public static ItemCombinations forID(int id) {
		for (ItemCombinations o: ItemCombinations.values()) {
			if (o.resultItem == id) {
				return o;
			}
		}
		return null;
	}

	public static ItemCombinations forID(int i1, int i2) {
		for (ItemCombinations f: ItemCombinations.values()) {
			boolean hasi1 = false, hasi2 = false;
			for (int j = 0; j < f.neededItems.length; j++) {
				if (f.neededItems[j] == i1) {
					hasi1 = true;
				}
				if (f.neededItems[j] == i2) {
					hasi2 = true;
				}
			}
			if (hasi1 && hasi2) {
				return f;
			}
		}
		return null;
	}


	public static void useItems(Player c, int use, int useSlot, int with, int withSlot) {
		ItemCombinations ic = forID(use, with);
		if (ic == null) {
			return;
		}
		for (int i = 0; i < ic.neededItems.length; i++) {
			if (!ItemAssistant.playerHasItem(c, ic.neededItems[i])) {
				return;
			}
		}
		if (ic.hasReq) {
			if (!hasReq(c,ic)) {
				return;
			}
		}
		for (int i = 0; i < ic.neededItems.length; i++) {
			ItemAssistant.deleteItem(c, ic.neededItems[i], 1);
		}
		ItemAssistant.addItem(c, ic.resultItem, 1);
	}


	public static void split(Player c, int item) {
		if (!ItemAssistant.playerHasItem(c, item)) {
			return;
		}
		ItemCombinations a = forID(item);
		if (a != null) {
			ItemAssistant.deleteItem(c, item, 1);
			for (int i = 0; i < a.neededItems.length; i++) {
				ItemAssistant.addItem(c, a.neededItems[i], 1);
			}
		}
	}


	public static boolean hasReq(Player c, ItemCombinations ic) {
		if (ic == BLESSED_SPIRIT_SHIELD) {
			if (c.skillLevel[Constants.PRAYER] < 65) {
				c.sendMessage("You must have a Prayer level of at least 65 to do this.");
				return false;
			}
		} else if (ic == SPECTRAL_SPIRIT_SHIELD) {
			if (c.skillLevel[Constants.PRAYER] < 70) {
				c.sendMessage("You must have a Prayer level of at least 70 to do this.");
				return false;
			}
		} else if (ic == ARCANE_SPIRIT_SHIELD) {
			if (c.skillLevel[Constants.PRAYER] < 70) {
				c.sendMessage("You must have a Prayer level of at least 70 to do this.");
				return false;
			}
		} else if (ic == ELYSIAN_SPIRIT_SHIELD) {
			if (c.skillLevel[Constants.PRAYER] < 75) {
				c.sendMessage("You must have a Prayer level of at least 75 to do this.");
				return false;
			}
		} else if (ic == DIVINE_SPIRIT_SHIELD) {
			if (c.skillLevel[Constants.PRAYER] < 80) {
				c.sendMessage("You must have a Prayer level of at least 80 to do this.");
				return false;
			}
		}
		return true;
	}



}
