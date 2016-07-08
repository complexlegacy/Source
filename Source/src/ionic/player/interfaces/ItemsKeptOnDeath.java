package ionic.player.interfaces;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import core.Constants;

/**
 * Items kept on death interface.
 * @author MGT Madness, created on 30-01-2014.
 *
 */
public class ItemsKeptOnDeath
{
	
	public static void showDeathInterface(Player player)
    {
            synchronized(player)
            {
                    player.diceDelay = System.currentTimeMillis();
                    player.getPA().sendFrame126("Items Kept on Death", 17103);
                    StartBestItemScan(player);
                    player.EquipStatus = 0;
                    for (int k = 0; k < 4; k++)
                            player.getPA().sendFrame34a(10494, -1, k, 1);
                    for (int k = 0; k < 39; k++)
                            player.getPA().sendFrame34a(10600, -1, k, 1);
                    if (player.WillKeepItem1 > 0)
                            player.getPA().sendFrame34a(10494, player.WillKeepItem1, 0,
                                    player.WillKeepAmt1);
                    if (player.WillKeepItem2 > 0)
                            player.getPA().sendFrame34a(10494, player.WillKeepItem2, 1,
                                    player.WillKeepAmt2);
                    if (player.WillKeepItem3 > 0)
                            player.getPA().sendFrame34a(10494, player.WillKeepItem3, 2,
                                    player.WillKeepAmt3);
                    if (player.WillKeepItem4 > 0 && player.prayerActive[Constants.PROTECT_ITEM])
                            player.getPA().sendFrame34a(10494, player.WillKeepItem4, 3, 1);
                    for (int ITEM = 0; ITEM < 28; ITEM++)
                    {
                            if (player.playerItems[ITEM] - 1 > 0 && !(player.playerItems[ITEM] - 1 == player.WillKeepItem1 && ITEM == player.WillKeepItem1Slot) && !(player.playerItems[ITEM] - 1 == player.WillKeepItem2 && ITEM == player.WillKeepItem2Slot) && !(player.playerItems[ITEM] - 1 == player.WillKeepItem3 && ITEM == player.WillKeepItem3Slot) && !(player.playerItems[ITEM] - 1 == player.WillKeepItem4 && ITEM == player.WillKeepItem4Slot))
                            {
                                    player.getPA().sendFrame34a(10600,
                                            player.playerItems[ITEM] - 1, player.EquipStatus,
                                            player.playerItemsN[ITEM]);
                                    player.EquipStatus += 1;
                            }
                            else if (player.playerItems[ITEM] - 1 > 0 && (player.playerItems[ITEM] - 1 == player.WillKeepItem1 && ITEM == player.WillKeepItem1Slot) && player.playerItemsN[ITEM] > player.WillKeepAmt1)
                            {
                                    player.getPA().sendFrame34a(10600,
                                            player.playerItems[ITEM] - 1, player.EquipStatus,
                                            player.playerItemsN[ITEM] - player.WillKeepAmt1);
                                    player.EquipStatus += 1;
                            }
                            else if (player.playerItems[ITEM] - 1 > 0 && (player.playerItems[ITEM] - 1 == player.WillKeepItem2 && ITEM == player.WillKeepItem2Slot) && player.playerItemsN[ITEM] > player.WillKeepAmt2)
                            {
                                    player.getPA().sendFrame34a(10600,
                                            player.playerItems[ITEM] - 1, player.EquipStatus,
                                            player.playerItemsN[ITEM] - player.WillKeepAmt2);
                                    player.EquipStatus += 1;
                            }
                            else if (player.playerItems[ITEM] - 1 > 0 && (player.playerItems[ITEM] - 1 == player.WillKeepItem3 && ITEM == player.WillKeepItem3Slot) && player.playerItemsN[ITEM] > player.WillKeepAmt3)
                            {
                                    player.getPA().sendFrame34a(10600,
                                            player.playerItems[ITEM] - 1, player.EquipStatus,
                                            player.playerItemsN[ITEM] - player.WillKeepAmt3);
                                    player.EquipStatus += 1;
                            }
                            else if (player.playerItems[ITEM] - 1 > 0 && (player.playerItems[ITEM] - 1 == player.WillKeepItem4 && ITEM == player.WillKeepItem4Slot) && player.playerItemsN[ITEM] > 1)
                            {
                                    player.getPA().sendFrame34a(10600,
                                            player.playerItems[ITEM] - 1, player.EquipStatus,
                                            player.playerItemsN[ITEM] - 1);
                                    player.EquipStatus += 1;
                            }
                    }
                    for (int EQUIP = 0; EQUIP < 14; EQUIP++)
                    {
                            if (player.playerEquipment[EQUIP] > 0 && !(player.playerEquipment[EQUIP] == player.WillKeepItem1 && EQUIP + 28 == player.WillKeepItem1Slot) && !(player.playerEquipment[EQUIP] == player.WillKeepItem2 && EQUIP + 28 == player.WillKeepItem2Slot) && !(player.playerEquipment[EQUIP] == player.WillKeepItem3 && EQUIP + 28 == player.WillKeepItem3Slot) && !(player.playerEquipment[EQUIP] == player.WillKeepItem4 && EQUIP + 28 == player.WillKeepItem4Slot))
                            {
                                    player.getPA().sendFrame34a(10600,
                                            player.playerEquipment[EQUIP], player.EquipStatus,
                                            player.playerEquipmentN[EQUIP]);
                                    player.EquipStatus += 1;
                            }
                            else if (player.playerEquipment[EQUIP] > 0 && (player.playerEquipment[EQUIP] == player.WillKeepItem1 && EQUIP + 28 == player.WillKeepItem1Slot) && player.playerEquipmentN[EQUIP] > 1 && player.playerEquipmentN[EQUIP] - player.WillKeepAmt1 > 0)
                            {
                                    player.getPA().sendFrame34a(
                                            10600,
                                            player.playerEquipment[EQUIP],
                                            player.EquipStatus,
                                            player.playerEquipmentN[EQUIP] - player.WillKeepAmt1);
                                    player.EquipStatus += 1;
                            }
                            else if (player.playerEquipment[EQUIP] > 0 && (player.playerEquipment[EQUIP] == player.WillKeepItem2 && EQUIP + 28 == player.WillKeepItem2Slot) && player.playerEquipmentN[EQUIP] > 1 && player.playerEquipmentN[EQUIP] - player.WillKeepAmt2 > 0)
                            {
                                    player.getPA().sendFrame34a(
                                            10600,
                                            player.playerEquipment[EQUIP],
                                            player.EquipStatus,
                                            player.playerEquipmentN[EQUIP] - player.WillKeepAmt2);
                                    player.EquipStatus += 1;
                            }
                            else if (player.playerEquipment[EQUIP] > 0 && (player.playerEquipment[EQUIP] == player.WillKeepItem3 && EQUIP + 28 == player.WillKeepItem3Slot) && player.playerEquipmentN[EQUIP] > 1 && player.playerEquipmentN[EQUIP] - player.WillKeepAmt3 > 0)
                            {
                                    player.getPA().sendFrame34a(
                                            10600,
                                            player.playerEquipment[EQUIP],
                                            player.EquipStatus,
                                            player.playerEquipmentN[EQUIP] - player.WillKeepAmt3);
                                    player.EquipStatus += 1;
                            }
                            else if (player.playerEquipment[EQUIP] > 0 && (player.playerEquipment[EQUIP] == player.WillKeepItem4 && EQUIP + 28 == player.WillKeepItem4Slot) && player.playerEquipmentN[EQUIP] > 1 && player.playerEquipmentN[EQUIP] - 1 > 0)
                            {
                                    player.getPA().sendFrame34a(10600,
                                            player.playerEquipment[EQUIP], player.EquipStatus,
                                            player.playerEquipmentN[EQUIP] - 1);
                                    player.EquipStatus += 1;
                            }
                    }
                    ResetKeepItems(player);
                    player.getPA().showInterface(17100);
                    player.isUsingDeathInterface = true;
            }
    }
	
	/**
	 * Update the items kept on death interface, if something has changed while the interface is already opened.
	 * @param player
	 * 			The associated player.
	 */
	public static void updateInterface(Player player)
	{
		if (player.isUsingDeathInterface)
        {
            showDeathInterface(player);
            player.isUsingDeathInterface = true;
        }
	}
	
	public static void ResetKeepItems(Player player)
    {
		player.WillKeepAmt1 = -1;
		player.WillKeepItem1 = -1;
		player.WillKeepAmt2 = -1;
		player.WillKeepItem2 = -1;
		player.WillKeepAmt3 = -1;
		player.WillKeepItem3 = -1;
		player.WillKeepAmt4 = -1;
		player.WillKeepItem4 = -1;
    }

    public static void StartBestItemScan(Player player)
    {
            if (player.getRedSkull() || player.getWhiteSkull() && !player.prayerActive[Constants.PROTECT_ITEM])
            {
                    ItemKeptInfo(player, 0);
                    return;
            }
            FindItemKeptInfo(player);
            ResetKeepItems(player);
            BestItem1(player);
    }

    public static void FindItemKeptInfo(Player player)
    {
            if (player.getWhiteSkull() && player.prayerActive[Constants.PROTECT_ITEM] && !player.getRedSkull())
            {
                    ItemKeptInfo(player, 1);
            }
            else if (!player.getWhiteSkull() && !player.prayerActive[Constants.PROTECT_ITEM] && !player.getRedSkull())
            {
                    ItemKeptInfo(player, 3);
            }
            else if (!player.getWhiteSkull() && player.prayerActive[Constants.PROTECT_ITEM] && !player.getRedSkull())
            {
                    ItemKeptInfo(player, 4);
            }
    }

    public static void ItemKeptInfo(Player player, int Lose)
    {
            for (int i = 17109; i < 17131; i++)
            {
                    player.getPA().sendFrame126("", i);
            }
            player.getPA().sendFrame126("Items you will keep on death:", 17104);
            player.getPA().sendFrame126("Items you will lose on death:", 17105);
            player.getPA().sendFrame126("Player Information", 17106);
            player.getPA().sendFrame126("Max items kept on death:", 17107);
            player.getPA().sendFrame126("~ " + Lose + " ~", 17108);
            player.getPA().sendFrame126("The normal amount of", 17111);
            player.getPA().sendFrame126("items kept is three.", 17112);
            switch (Lose)
            {
            case 0:
            default:
                    player.getPA().sendFrame126("Items you will keep on death:", 17104);
                    player.getPA().sendFrame126("Items you will lose on death:", 17105);
                    player.getPA().sendFrame126("You're marked with a", 17111);
                    player.getPA().sendFrame126("skull. @lre@This reduces the", 17112);
                    player.getPA().sendFrame126("items you keep to zero.", 17113);
                    player.getPA().sendFrame126("", 17114);
                    break;
            case 1:
                    player.getPA().sendFrame126("Items you will keep on death:", 17104);
                    player.getPA().sendFrame126("Items you will lose on death:", 17105);
                    player.getPA().sendFrame126("You're marked with a", 17111);
                    player.getPA().sendFrame126("skull. @lre@You have protect", 17112);
                    player.getPA().sendFrame126("item active which saves", 17113);
                    player.getPA().sendFrame126("one item.", 17114);
                    player.getPA().sendFrame126("", 17115);
                    player.getPA().sendFrame126("", 17116);
                    player.getPA().sendFrame126("", 17117);
                    player.getPA().sendFrame126("", 17118);
                    break;
            case 3:
                    player.getPA().sendFrame126("Items you will keep on death:", 17104);
                    player.getPA().sendFrame126("Items you will lose on death:", 17105);
                    player.getPA().sendFrame126("You have no factors", 17111);
                    player.getPA().sendFrame126("affecting the items you", 17112);
                    player.getPA().sendFrame126("keep.", 17113);
                    break;
            case 4:
                    player.getPA().sendFrame126("Items you will keep on death:", 17104);
                    player.getPA().sendFrame126("Items you will lose on death:", 17105);
                    player.getPA().sendFrame126("You have the @red@Protect", 17111);
                    player.getPA().sendFrame126("@red@Item @lre@prayer active,", 17112);
                    player.getPA().sendFrame126("which saves one extra", 17113);
                    player.getPA().sendFrame126("item.", 17114);
                    break;
            }
    }

    public static void BestItem1(Player player)
    {
            int BestValue = 0;
            int NextValue = 0;
            int ItemsContained = 0;
            player.WillKeepItem1 = 0;
            player.WillKeepItem1Slot = 0;
            for (int ITEM = 0; ITEM < 28; ITEM++)
            {
                    if (player.playerItems[ITEM] > 0)
                    {
                            ItemsContained += 1;
                            NextValue = (int) Math.floor(ItemAssistant.getItemShopValue(player.playerItems[ITEM] - 1));
                            if (NextValue > BestValue)
                            {
                                    BestValue = NextValue;
                                    player.WillKeepItem1 = player.playerItems[ITEM] - 1;
                                    player.WillKeepItem1Slot = ITEM;
                                    if (player.playerItemsN[ITEM] > 2 && !player.prayerActive[Constants.PROTECT_ITEM])
                                    {
                                    	player.WillKeepAmt1 = 3;
                                    }
                                    else if (player.playerItemsN[ITEM] > 3 && player.prayerActive[Constants.PROTECT_ITEM])
                                    {
                                    	player.WillKeepAmt1 = 4;
                                    }
                                    else
                                    {
                                    	player.WillKeepAmt1 = player.playerItemsN[ITEM];
                                    }
                            }
                    }
            }
            for (int EQUIP = 0; EQUIP < 14; EQUIP++)
            {
                    if (player.playerEquipment[EQUIP] > 0)
                    {
                            ItemsContained += 1;
                            NextValue = (int) Math.floor(ItemAssistant.getItemShopValue(player.playerEquipment[EQUIP]));
                            if (NextValue > BestValue)
                            {
                                    BestValue = NextValue;
                                    player.WillKeepItem1 = player.playerEquipment[EQUIP];
                                    player.WillKeepItem1Slot = EQUIP + 28;
                                    if (player.playerEquipmentN[EQUIP] > 2 && !player.prayerActive[Constants.PROTECT_ITEM])
                                    {
                                    	player.WillKeepAmt1 = 3;
                                    }
                                    else if (player.playerEquipmentN[EQUIP] > 3 && player.prayerActive[Constants.PROTECT_ITEM])
                                    {
                                    	player.WillKeepAmt1 = 4;
                                    }
                                    else
                                    {
                                    	player.WillKeepAmt1 = player.playerEquipmentN[EQUIP];
                                    }
                            }
                    }
            }
            if (!player.getWhiteSkull() && ItemsContained > 1 && (player.WillKeepAmt1 < 3 || (player.prayerActive[Constants.PROTECT_ITEM] && player.WillKeepAmt1 < 4)))
            {
                    BestItem2(player, ItemsContained);
            }
    }

    public static void BestItem2(Player player, int ItemsContained)
    {
            int BestValue = 0;
            int NextValue = 0;
            player.WillKeepItem2 = 0;
            player.WillKeepItem2Slot = 0;
            for (int ITEM = 0; ITEM < 28; ITEM++)
            {
                    if (player.playerItems[ITEM] > 0)
                    {
                            NextValue = (int) Math.floor(ItemAssistant.getItemShopValue(player.playerItems[ITEM] - 1));
                            if (NextValue > BestValue && !(ITEM == player.WillKeepItem1Slot && player.playerItems[ITEM] - 1 == player.WillKeepItem1 
                            	&& !ItemAssistant.isStackable(player.WillKeepItem1)))
                            {
                                    BestValue = NextValue;
                                    player.WillKeepItem2 = player.playerItems[ITEM] - 1;
                                    player.WillKeepItem2Slot = ITEM;
                                    if (player.playerItemsN[ITEM] > 2 - player.WillKeepAmt1 && !player.prayerActive[Constants.PROTECT_ITEM])
                                    {
                                    	player.WillKeepAmt2 = 3 - player.WillKeepAmt1;
                                    }
                                    else if (player.playerItemsN[ITEM] > 3 - player.WillKeepAmt1 && player.prayerActive[Constants.PROTECT_ITEM])
                                    {
                                    	player.WillKeepAmt2 = 4 - player.WillKeepAmt1;
                                    }
                                    else
                                    {
                                    	player.WillKeepAmt2 = player.playerItemsN[ITEM];
                                    }
                            }
                    }
            }
            for (int EQUIP = 0; EQUIP < 14; EQUIP++)
            {
                    if (player.playerEquipment[EQUIP] > 0)
                    {
                            NextValue = (int) Math.floor(ItemAssistant.getItemShopValue(player.playerEquipment[EQUIP]));
                            if (NextValue > BestValue && !(EQUIP + 28 == player.WillKeepItem1Slot && player.playerEquipment[EQUIP] == player.WillKeepItem1 
                            	&& !ItemAssistant.isStackable(player.WillKeepItem1)))
                            {
                                    BestValue = NextValue;
                                    player.WillKeepItem2 = player.playerEquipment[EQUIP];
                                    player.WillKeepItem2Slot = EQUIP + 28;
                                    if (player.playerEquipmentN[EQUIP] > 2 - player.WillKeepAmt1 && !player.prayerActive[Constants.PROTECT_ITEM])
                                    {
                                    	player.WillKeepAmt2 = 3 - player.WillKeepAmt1;
                                    }
                                    else if (player.playerEquipmentN[EQUIP] > 3 - player.WillKeepAmt1 && player.prayerActive[Constants.PROTECT_ITEM])
                                    {
                                    	player.WillKeepAmt2 = 4 - player.WillKeepAmt1;
                                    }
                                    else
                                    {
                                    	player.WillKeepAmt2 = player.playerEquipmentN[EQUIP];
                                    }
                            }
                    }
            }
            if (!player.getWhiteSkull() && ItemsContained > 2 && (player.WillKeepAmt1 + player.WillKeepAmt2 < 3 || (player.prayerActive[Constants.PROTECT_ITEM] 
            	&& player.WillKeepAmt1 + player.WillKeepAmt2 < 4)))
            {
            	BestItem3(player, ItemsContained);
            }
    }

    public static void BestItem3(Player player, int ItemsContained)
    {
            int BestValue = 0;
            int NextValue = 0;
            player.WillKeepItem3 = 0;
            player.WillKeepItem3Slot = 0;
            for (int ITEM = 0; ITEM < 28; ITEM++)
            {
                    if (player.playerItems[ITEM] > 0)
                    {
                            NextValue = (int) Math.floor(ItemAssistant.getItemShopValue(player.playerItems[ITEM] - 1));
                            if (NextValue > BestValue && !(ITEM == player.WillKeepItem1Slot && player.playerItems[ITEM] - 1 == player.WillKeepItem1 
                            	&& !ItemAssistant.isStackable(player.WillKeepItem1)) && !(ITEM == player.WillKeepItem2Slot 
                            	&& player.playerItems[ITEM] - 1 == player.WillKeepItem2) && !ItemAssistant.isStackable(player.WillKeepItem2))
                            {
                                    BestValue = NextValue;
                                    player.WillKeepItem3 = player.playerItems[ITEM] - 1;
                                    player.WillKeepItem3Slot = ITEM;
                                    if (player.playerItemsN[ITEM] > 2 - (player.WillKeepAmt1 + player.WillKeepAmt2) && !player.prayerActive[Constants.PROTECT_ITEM])
                                    {
                                    	player.WillKeepAmt3 = 3 - (player.WillKeepAmt1 + player.WillKeepAmt2);
                                    }
                                    else if (player.playerItemsN[ITEM] > 3 - (player.WillKeepAmt1 + player.WillKeepAmt2) && player.prayerActive[Constants.PROTECT_ITEM])
                                    {
                                    	player.WillKeepAmt3 = 4 - (player.WillKeepAmt1 + player.WillKeepAmt2);
                                    }
                                    else
                                    {
                                    	player.WillKeepAmt3 = player.playerItemsN[ITEM];
                                    }
                            }
                    }
            }
            for (int EQUIP = 0; EQUIP < 14; EQUIP++)
            {
                    if (player.playerEquipment[EQUIP] > 0)
                    {
                            NextValue = (int) Math.floor(ItemAssistant.getItemShopValue(player.playerEquipment[EQUIP]));
                            if (NextValue > BestValue && !(EQUIP + 28 == player.WillKeepItem1Slot && player.playerEquipment[EQUIP] == player.WillKeepItem1 
                            	&& !ItemAssistant.isStackable(player.WillKeepItem1)) && !(EQUIP + 28 == player.WillKeepItem2Slot 
                            	&& player.playerEquipment[EQUIP] == player.WillKeepItem2 && !ItemAssistant.isStackable(player.WillKeepItem2)))
                            {
                                    BestValue = NextValue;
                                    player.WillKeepItem3 = player.playerEquipment[EQUIP];
                                    player.WillKeepItem3Slot = EQUIP + 28;
                                    if (player.playerEquipmentN[EQUIP] > 2 - (player.WillKeepAmt1 + player.WillKeepAmt2) && !player.prayerActive[Constants.PROTECT_ITEM])
                                    {
                                    	player.WillKeepAmt3 = 3 - (player.WillKeepAmt1 + player.WillKeepAmt2);
                                    }
                                    else if (player.playerEquipmentN[EQUIP] > 3 - player.WillKeepAmt1 && player.prayerActive[Constants.PROTECT_ITEM])
                                    {
                                    	player.WillKeepAmt3 = 4 - (player.WillKeepAmt1 + player.WillKeepAmt2);
                                    }
                                    else
                                    {
                                    	player.WillKeepAmt3 = player.playerEquipmentN[EQUIP];
                                    }
                            }
                    }
            }
            if (!player.getWhiteSkull() && ItemsContained > 3 && player.prayerActive[Constants.PROTECT_ITEM] 
            	&& ((player.WillKeepAmt1 + player.WillKeepAmt2 + player.WillKeepAmt3) < 4))
            {
                    BestItem4(player);
            }
    }

    public static void BestItem4(Player player)
    {
            int BestValue = 0;
            int NextValue = 0;
            player.WillKeepItem4 = 0;
            player.WillKeepItem4Slot = 0;
            for (int ITEM = 0; ITEM < 28; ITEM++)
            {
                    if (player.playerItems[ITEM] > 0)
                    {
                            NextValue = (int) Math.floor(ItemAssistant.getItemShopValue(player.playerItems[ITEM] - 1));
                            if (NextValue > BestValue && !(ITEM == player.WillKeepItem1Slot && player.playerItems[ITEM] - 1 == player.WillKeepItem1 
                            	&& !ItemAssistant.isStackable(player.WillKeepItem1)) && !(ITEM == player.WillKeepItem2Slot 
                            	&& player.playerItems[ITEM] - 1 == player.WillKeepItem2 && !ItemAssistant.isStackable(player.WillKeepItem2)) && !(ITEM == player.WillKeepItem3Slot && player.playerItems[ITEM] - 1 == player.WillKeepItem3 && !ItemAssistant.isStackable(player.WillKeepItem3)))
                            {
                                    BestValue = NextValue;
                                    player.WillKeepItem4 = player.playerItems[ITEM] - 1;
                                    player.WillKeepItem4Slot = ITEM;
                            }
                    }
            }
            for (int EQUIP = 0; EQUIP < 14; EQUIP++)
            {
                    if (player.playerEquipment[EQUIP] > 0)
                    {
                            NextValue = (int) Math.floor(ItemAssistant.getItemShopValue(player.playerEquipment[EQUIP]));
                            if (NextValue > BestValue && !(EQUIP + 28 == player.WillKeepItem1Slot && player.playerEquipment[EQUIP] == player.WillKeepItem1 
                            	&& !ItemAssistant.isStackable(player.WillKeepItem1)) && !(EQUIP + 28 == player.WillKeepItem2Slot 
                            	&& player.playerEquipment[EQUIP] == player.WillKeepItem2 && !ItemAssistant.isStackable(player.WillKeepItem2)) 
                            	&& !(EQUIP + 28 == player.WillKeepItem3Slot && player.playerEquipment[EQUIP] == player.WillKeepItem3 
                            	&& !ItemAssistant.isStackable(player.WillKeepItem3)))
                            {
                                    BestValue = NextValue;
                                    player.WillKeepItem4 = player.playerEquipment[EQUIP];
                                    player.WillKeepItem4Slot = EQUIP + 28;
                            }
                    }
            }
    }

}
