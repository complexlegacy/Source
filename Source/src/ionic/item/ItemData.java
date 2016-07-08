package ionic.item;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ItemData {

	public static ItemData[] data = new ItemData[22600];

	private final int id;
	private final String name;
	private final String description;
	public int[] bonuses;
	public int shopValue = 0;
	public boolean stackable = false;
	private int notedItemId = -1;
	private int unnotedItemId = -1;
	private boolean isLentItem = false;
	private int lentItemId = -1;
	private int unlentItemId = -1;
	private boolean untradable = false;
	
	public boolean degradable = false;
	public boolean degraded = false;
	public int fullyCharged = 0;
	public int broken = 0;
	public int degradedId = 0;
	public int maxCharges = 0;
	public int repairCost = -1;
	public int degradeType = 0;

	public ItemData(int itemID, String itemName, String examine, int[] itemBonuses) {
		this.id = itemID;
		this.name = itemName;
		this.description = examine;
		this.bonuses = itemBonuses;
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	
	

	public int getUnnoted() {
		return unnotedItemId;
	}
	public void setUnnoted(int unnotedItemId) {
		this.unnotedItemId = unnotedItemId;
	}
	

	
	public int getNoted() {
		return notedItemId;
	}
	public void setNoted(int notedItemId) {
		this.notedItemId = notedItemId;
	}
	
	

	public boolean isNoted() {
		return this.getUnnoted() > -1;
	}
	
	
	
	public boolean isLent() {
		return isLentItem;
	}
	public void setLent(boolean isLentItem) {
		this.isLentItem = isLentItem;
	}
	
	
	
	public int getLentItemId() {
		return lentItemId;
	}
	public void setLentItemId(int lentItemId) {
		this.lentItemId = lentItemId;
	}

	
	
	public int getUnlentItemId() {
		return unlentItemId;
	}
	public void setUnlentItemId(int unlentItemId) {
		this.unlentItemId = unlentItemId;
	}
	
	
	public boolean isLendable() {
		return this.lentItemId > 0;
	}
	
	
	public boolean isUntradable() {
		return untradable;
	}

	public void setUntradable(boolean untradable) {
		this.untradable = untradable;
	}
	
	
	
	
	public static final int[] ITEM_UNTRADABLE = {
		15018, 15019, 15220, 18335, 10551, 19111, 18349, 18351, 18353, 18355, 18357, 18359, 
		18361, 18363, 9747, 9748, 9749, 9750, 9751, 9752, 9753, 9754, 9755, 9756, 9757, 9758, 
		9759, 9760, 9761, 9762, 9763, 9764, 9765, 9766, 9767, 9768, 9769, 9770, 9771, 9772, 
		9773, 9774, 9775, 9776, 9777, 9778, 9779, 9780, 9781, 9782, 9783, 9784, 9785, 9786, 
		9787, 9788, 9789, 9790, 9791, 9792, 9793, 9794, 9795, 9796, 9797, 9798, 9799, 9800, 
		9801, 9802, 9803, 9804, 9805, 9806, 9807, 9808, 9809, 9810, 9811, 9812, 9813, 9814,
        11665, 11664, 11663, 8839, 8840, 10548, 10547, 10550, 6570, 15349, 7454, 7455, 7456, 7457, 
        7458, 7459, 7460, 7461, 7462, 3840, 3842, 3844, 8844, 8845, 8846, 8847, 8848, 8849, 8850,
        2412, 2413, 2414, 10499, 15098, 20769,
	};
	
	public static void loadUntradable() {
		for (int i = 0; i < ITEM_UNTRADABLE.length; i++) {
			data[ITEM_UNTRADABLE[i]].setUntradable(true);
		}
	}
	
	public static void loadData() {
		BufferedReader reader = null;
		try {
			try {
				reader = new BufferedReader(new FileReader("./data/items/itemList.txt"));
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
					int[] bonuses = null;
					if (s.length > 3) {
						String[] b = s[3].split(" ");
						bonuses = new int[b.length - 1];
						for (int i = 0; i < b.length - 1; i++) {
							bonuses[i] = Integer.parseInt(b[i + 1]);
						}
					}
					data[Integer.parseInt(s[0])] = new ItemData(Integer.parseInt(s[0]), s[1], s[2], bonuses);
				}
			} finally {
				reader.close();
				loadStackData();
				loadLentData();
				loadUntradable();
				loadDegradeData();
			}
		}catch (IOException ex) {
		}
	}

	public static void loadStackData() {
		BufferedReader reader = null;
		try {
			try {
				reader = new BufferedReader(new FileReader("./data/items/itemStackData.txt"));
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
					String[] s = line.split(" -- ");
					int item = Integer.parseInt(s[0]);
					if (data[item] != null) {
						String[] stack = s[1].split(" = ");
						boolean stacks = Boolean.parseBoolean(stack[1]);
						String[] note = s[2].split(" = ");
						int itemNoteID = Integer.parseInt(note[1]);
						data[item].stackable = stacks;
						data[item].setNoted(itemNoteID);
						if (itemNoteID != -1) {
							data[itemNoteID].setUnnoted(item);
							data[itemNoteID].stackable = true;
						}
					}
				}
			} finally {
				reader.close();
			}
		}catch (IOException ex) {
		}
	}
	
	
	public static void loadLentData() {
		BufferedReader reader = null;
		try {
			try {
				reader = new BufferedReader(new FileReader("./data/items/loanedItems.txt"));
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
					String[] s = line.split("  =  ");
					int lentItemId = Integer.parseInt(s[1]);
					int unlentItemId = Integer.parseInt(s[2]);
					if (data[lentItemId] != null && data[unlentItemId] != null) {
						data[lentItemId].setLent(true);
						data[lentItemId].setUnlentItemId(unlentItemId);
						data[unlentItemId].setLentItemId(lentItemId);
						data[lentItemId].bonuses = data[unlentItemId].bonuses;
						data[lentItemId].setUntradable(true);
					}
				}
			} finally {
				reader.close();
			}
		}catch (IOException ex) {
		}
	}
	
	public static void loadDegradeData() {
		BufferedReader reader = null;
		try {
			try {
				reader = new BufferedReader(new FileReader("./data/items/degradingData.txt"));
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
					int full = Integer.parseInt(s[1]);
					int degraded = Integer.parseInt(s[2]);
					int broken = Integer.parseInt(s[3]);
					int charge = Integer.parseInt(s[4]);
					int rCost = Integer.parseInt(s[5]);
					int dType = Integer.parseInt(s[6]);
					data[full].degradable = true;
					data[full].broken = broken;
					data[full].degradedId = degraded;
					data[full].fullyCharged = full;
					data[full].maxCharges = charge;
					data[full].repairCost = rCost;
					data[full].degradeType = dType;
					data[degraded].degraded = true;
					data[degraded].degradable = true;
					data[degraded].broken = broken;
					data[degraded].degradedId = degraded;
					data[degraded].fullyCharged = full;
					data[degraded].maxCharges = charge;
					data[degraded].untradable = true;
					data[degraded].bonuses = data[full].bonuses;
					data[degraded].repairCost = rCost;
					data[degraded].degradeType = dType;
					if (broken != -1) {
						data[broken].broken = broken;
						data[broken].degradedId = degraded;
						data[broken].maxCharges = charge;
						data[broken].fullyCharged = full;
						data[broken].repairCost = rCost;
					}
				}
			} finally {
				reader.close();
			}
		}catch (IOException ex) {
		}
	}
	

}
