package ionic.item;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import core.Constants;


public class Item
{


        /**
         * Items that cover the player's arms.
         */
        private static String[] fullBody =
        {
                "vesta's chainbody", "top", "chestplate", "shirt", "platebody", "Ahrim's robetop", "Karils leathertop", "brassard", "Robe top", "robetop", "spined body", "platebody (t)", "platebody (g)", "chestplate", "prince tunic", "torso", "hauberk", "blouse", "jacket", "Prince tunic", "d'hide Body", "pernix body", "dragon chainbody", "morrigan's leather body"
        };

        /**
         * Items that cover the player's head but not the beard.
         */
        private static String[] normalHelm =
        {
                "med helm", "berserker helm", "coif", "Dharok's helm", "hood", "Spiny helmet", "Snakeskin bandana", "Coif", "archer helm", "farseer helm", "warrior helm", "Void", "Lumberjack hat", "Reindeer hat", "snakeskin bandana", "Larupia hat", "Kyatt hat", "Bomber cap", "Dwarven helmet", "Sagittarian body", "helm of neitiznot", "Profound decorative helm", "mitre", "coif", "helm", "mystic hat", "leather cowl", "desert disguise", "bearhead"
        };

        /**
         * Items that cover the player's entire head.
         */
        private static String[] fullmask =
        {
                "pernix cowl", "full helm", "Initiate helm", "Gorgonite full helm (b)", "Primal full helm (b)", "Zephyrium full helm (b)", "Argonite full helm (b)", "Verac's helm", "Guthan's helm", "Karil's coif", "mask", "Torag's helm", "sallet", "Saradomin helm", "Lunar helm", "Black full helm (t)", "Rune heraldic helm", "Armadyl helmet", "Adamant full helm (t)", "Adamant full helm (g)", "Black full helm (g)", "Black full helm (t)", "Rune full helm (g)", "Rune full helm (t)", "Basic decorative helm", "Detailed decorative helm", "slayer helmet", "Intricate decorative helm"
        };

        /**
         * @param itemId
         * 			The item ID to check.
         * @return
         * 			True, if the item ID is an item that covers the player's arms.
         */
        public static boolean isFullBody(int itemId)
        {
                String weapon = getItemName(itemId);
                if (weapon == null) return false;
                for (int i = 0; i < fullBody.length; i++)
                {
                        if (weapon.toLowerCase().contains(fullBody[i]))
                        {
                                return true;
                        }
                }
                return false;
        }

        /**
         * @param itemId
         * 			The item ID to check.
         * @return
         * 			True, if the item ID is an item that covers the player's head but not the beard.
         */
        public static boolean isNormalHelm(int itemId)
        {
                String weapon = getItemName(itemId);
                if (weapon == null) return false;
                for (int i = 0; i < normalHelm.length; i++)
                {
                        if (weapon.toLowerCase().contains(normalHelm[i]) && !weapon.contains("hood hat"))
                        {
                                return true;
                        }
                }
                return false;
        }

        /**
         * @param itemId
         * 			The item ID to check.
         * @return
         * 			True, if the item ID is an item that covers the player's entire head.
         */
        public static boolean isFullMask(int itemId)
        {
        	if (itemId == 11789) {
        		return true;
        	}
                String weapon = getItemName(itemId);
                if (weapon == null) return false;
                for (int i = 0; i < fullmask.length; i++)
                {
                        if (weapon.toLowerCase().contains(fullmask[i]) && !weapon.contains("hood hat"))
                        {
                                return true;
                        }
                }
                return false;
        }

        /**
         * 
         * @param itemID
         * 			The item identity.
         * @return
         * 			The name of the item identity.
         */
        public static String getItemName(int ItemID) {
        	if (ItemID >= 0 && ItemData.data[ItemID] != null) {
        		return ItemData.data[ItemID].getName();
        	} else {
        		return "Unarmed";
        	}
        }

        public static int[] targetSlots = new int[Constants.MAX_ITEM_ID];
        static
        {
                int counter = 0;
                int c;
                try
                {
                        FileInputStream dataIn = new FileInputStream(new File("./data/items/equipment.dat"));
                        while ((c = dataIn.read()) != -1)
                        {
                                targetSlots[counter++] = c;
                        }
                        dataIn.close();
                }
                catch (IOException e)
                {
                        System.out.println("Critical error while loading notedata! Trace:");
                        e.printStackTrace();
                }
        }

        private int id;
        private int count;
        public static Item of(int id)
        {
                return new Item(id);
        }
        public Item(int id)
        {
                this(id, 1);
        }
        public Item(int id, int count)
        {
                if (count < 0)
                {
                        throw new IllegalArgumentException("Count cannot be negative.");
                }

                this.setId(id);
                this.setCount(count);
        }

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

}