package ionic.player.content.miscellaneous;
 
import ionic.item.ItemAssistant;
import ionic.player.Client;
 
/**
 * @author Karmakaidan <mark@project-innovation.org>
 * @author Advocatus <davidcntt@hotmail.com>
 */
public class ArmourSets {
         
    private static int[][] ArmourSets = {        
        /*ahrim's*/          { 11846, 4708, 4712, 4714, 4710},
        /*dharok's*/         { 11848, 4716, 4720, 4722, 4718},
        /*guthan's*/         { 11850, 4724, 4728, 4730, 4726},
        /*karil's*/          { 11852, 4732, 4736, 4738, 4734},
        /*torag's*/          { 11854, 4745, 4749, 4751, 4747},
        /*verac's*/          { 11856, 4753, 4757, 4759, 4755}
    };
     
    public static boolean isSet(int id) {
        return getSet(id) != null;
    }
     
    private static int[] getSet(int id) {
        for (int i = 0; i < ArmourSets.length; i++)
            if(ArmourSets[i][0] == id)
                return ArmourSets[i];
        return null;
    }
	
    public static void handleSet(Client player, int id) {
        if (!ItemAssistant.playerHasItem(player, id)) {
            return;
        }
        int [] data = getSet(id);
        if(data == null)
            return;
        if (ItemAssistant.freeSlots(player) < (data.length - 2)) {
            player.sendMessage("You do not have enough free inventory slots to do this.");
            return;
        }
        ItemAssistant.deleteItem(player, id, 1);
        for(int i = 1; i < data.length; i++) {
            ItemAssistant.addItem(player, data[i], 1);
        }
    }
}