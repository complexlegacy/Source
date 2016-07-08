package ionic.player.object.areas;

import ionic.item.ItemAssistant;
import ionic.object.customobject.Object;
import ionic.player.Client;
import ionic.player.content.miscellaneous.Teleport;
import core.Constants;
import utility.Misc;

/**
 * Handles objects of Wilderness
 * @author MGT Madness 27-10-2013
 */
public class Wilderness
{

    /**
     * @return true if object in the Wilderness.
     */
    public static boolean isWildernessObject(final Client player, final int objectType)
    {
    	/* These object locations do not belong in Wilderness Dungeon. */
        if (player.absX >= 3129 && player.absX <= 3135 && player.absY >= 9914 && player.absY <= 9921)
        {
            return false;
        }
        
        for (int i = 0; i < wildernessObject.length; i++)
        {
            if (objectType == wildernessObject[i])
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Wilderness object identities.
     */
    private static int[] wildernessObject =
    {
        1597, // Gate
        1596, // Gate
        9707, // Lever
        5959, // Lever
        9706, // Lever
        9707, // Lever
        1765, // Ladder
        1766, // Ladder
        2465, // Portal
        1815, // Lever
        1733, // Staircase
        1734, // Staircase
        733, // Web
        5960, // Lever
        2558, // Door
        2557
    };

    /**
     * Perform actions of the objects in Wilderness.
     */
    public static void doWildernessObject(final Client player, int objectType)
    {

        int playerX = player.absX;
        int playerY = player.absY;

        switch (objectType)
        {

            // Gate
        case 1597:

            // At level 47, the most eastern one
            if (playerX == 3336 && playerY == 3896 && player.objectX == 3336)
            {
                player.getPA().goTo(0, -1);
            }
            else if (playerX == 3336 && playerY == 3895 && player.objectX == 3336)
            {
                player.getPA().goTo(0, 1);
            }

            // At level 47, west of the most eastern one
            else if (playerX == 3224 && playerY == 3904 && player.objectX == 3224)
            {
                player.getPA().goTo(0, -1);
            }
            else if (playerX == 3224 && playerY == 3903 && player.objectX == 3224)
            {
                player.getPA().goTo(0, 1);
            }

            // At level 47, the most western gate
            else if (playerX == 2947 && playerY == 3904 && player.objectX == 2947)
            {
                player.getPA().goTo(0, -1);
            }
            else if (playerX == 2947 && playerY == 3903 && player.objectX == 2947)
            {
                player.getPA().goTo(0, 1);
            }

            // To exit and enter Lesser demon area, outside KBD cave
            else if (player.absX == 3007 && player.absY == 3850)
            {
                player.getPA().goTo(1, 0);
            }
            else if (player.absX == 3008 && player.absY == 3850)
            {
                player.getPA().goTo(-1, 0);
            }
            break;

        case 1596:
            if (player.absX == 3007 && player.absY == 3849)
            {
                player.getPA().goTo(1, 0);
            }
            else if (player.absX == 3008 && player.absY == 3849)
            {
                player.getPA().goTo(-1, 0);
            }
            break;

        case 5960:
              Teleport.startTeleport(player, 3090, 3956, 0, "lever");
            break;

        case 5959:
        	 Teleport.startTeleport(player, 2539, 4712, 0, "lever");
            break;

        case 9706:
            if (player.absX == 3105 && player.absY == 3956)
            {
                Teleport.startTeleport(player, 3105, 102395154, 0, "lever");
            }
            break;

        case 9707:
            if (player.absX == 3105 && player.absY == 3951)
            {
                Teleport.startTeleport(player, 3105, 3956, 0, "lever");
            }
            break;

            //Ladder at Lesser demon area outside Kbd
        case 1765:
            if (player.absX == 3016 && player.absY == 3849 || player.absX == 3017 && player.absY == 3848 || player.absX == 3017 && player.absY == 3850)
            {
                player.startAnimation(828);
                player.getPA().movePlayer(3069, 10257, 0);
            }
            break;

            //Ladder, leads to lesser demon area outside Kbd area
        case 1766:
            player.startAnimation(828);
            player.getPA().movePlayer(3016, 3849, 0);
            break;

            //Portal
        case 2465:
            if (player.absX == 3067 && player.absY == 10254) //Red Spider
            {
                player.getPA().movePlayer(2271, 4680, 0);
            }
            break;

            // Lever located at 50 wild
        case 1815:
            if (player.absX == 3153 && player.absY == 3923)
            {
            	Teleport.startTeleport(player, 3090, 3475, 0, "lever");
            }
            break;

            // Staircase, west of outside of Magebank.
        case 1733:
        	if (player.objectX == 3044 && player.objectY == 3924)
        	{
        			player.getPA().movePlayer(player.absX, 10323, 0);
        	}
            break;

            // Staircase
        case 1734:
            player.getPA().movePlayer(player.absX, 3927, 0);
            break;

            // Web
        case 733:
            if (System.currentTimeMillis() - player.lastAction < 1000) 
            {
            	return;
            }
            player.lastAction = System.currentTimeMillis();
            if (ItemAssistant.weildingSharpWeapon(player))
            {
                player.startAnimation(player.getCombat().weaponAttackAnimation(ItemAssistant.getItemName(player.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase()));
                player.sendMessage("You slash the web.");
                if (Misc.random(1) == 1)
                {
                    if (player.objectX == 3158 && player.objectY == 3951)
                    {
                        new Object(734, player.objectX, player.objectY, player.heightLevel, 1, 10, 733, 50);
                    }
                    else if (player.objectX == 3106 && player.objectY == 3958)
                    {
                        new Object(734, player.objectX, player.objectY, player.heightLevel, 3, 10, 733, 50);
                    }
                    else if (player.objectX == 3105 && player.objectY == 3958)
                    {
                        new Object(734, player.objectX, player.objectY, player.heightLevel, 3, 10, 733, 50);
                    }
                    else if (player.objectX == 3095 && player.objectY == 3957)
                    {
                        new Object(734, player.objectX, player.objectY, player.heightLevel, 0, 10, 733, 50);
                    }
                    else if (player.objectX == 3093 && player.objectY == 3957)
                    {
                        new Object(734, player.objectX, player.objectY, player.heightLevel, 0, 10, 733, 50);
                    }
                }
                else
                {
                    player.sendMessage("You fail to slash the web.");
                }
            }
            else
            {
                player.sendMessage("You need a sharp blade to cut the web.");
            }
            break;

        case 2558:
        case 2557:
            if (System.currentTimeMillis() - player.lastLockPick < 1000 || player.freezeTimer > 0)
            {
            	return;
            }
            player.lastLockPick = System.currentTimeMillis();
            if (ItemAssistant.playerHasItem(player, 1523, 1))
            {
                
                if (Misc.random(10) <= 3)
                {
                    player.sendMessage("You fail to pick the lock.");
                    break;
                }
                if (player.objectX == 3044 && player.objectY == 3956)
                {
                    if (player.absX == 3045)
                    {
                        player.getPA().walkTo2(-1, 0);
                    }
                    else if (player.absX == 3044)
                    {
                        player.getPA().walkTo2(1, 0);
                    }

                }
                else if (player.objectX == 3038 && player.objectY == 3956)
                {
                    if (player.absX == 3037)
                    {
                        player.getPA().walkTo2(1, 0);
                    }
                    else if (player.absX == 3038)
                    {
                        player.getPA().walkTo2(-1, 0);
                    }
                }
                else if (player.objectX == 3041 && player.objectY == 3959)
                {
                    if (player.absY == 3960)
                    {
                        player.getPA().walkTo2(0, -1);
                    }
                    else if (player.absY == 3959)
                    {
                        player.getPA().walkTo2(0, 1);
                    }
                }
                else if (player.objectX == 3191 && player.objectY == 3963)
                {
                    if (player.absY == 3963)
                    {
                        player.getPA().walkTo2(0, -1);
                    }
                    else if (player.absY == 3962)
                    {
                        player.getPA().walkTo2(0, 1);
                    }
                }
                else if (player.objectX == 3190 && player.objectY == 3957)
                {
                    if (player.absY == 3957)
                    {
                        player.getPA().walkTo2(0, 1);
                    }
                    else if (player.absY == 3958)
                    {
                        player.getPA().walkTo2(0, -1);
                    }
                }
            }
            else
            {
                player.sendMessage("I need a lockpick to pick this lock.");
            }
            break;
            
        }
    }

}