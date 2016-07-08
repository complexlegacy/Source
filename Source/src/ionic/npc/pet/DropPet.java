package ionic.npc.pet;

import ionic.item.ItemAssistant;
import ionic.player.Client;

/**
 * Drop the pet.
 * @author MGT Madness, created on 11-12-2013.
 */
public class DropPet 
{
	
    /**
     * Check if the player has requirements to drop the pet.
     * @param player
     * 			The player dropping the pet.
     * @param itemId
     * 			The pet item identity being dropped.
     * @param slot
     * 			The slot of the item being dropped.
     */
	public static void dropPetRequirements(Client player, int itemId, int slot)
	{
		for (int i = 0; i < PetData.petData.length; i++)
        {
				if (PetData.petData[i][1] == itemId)
                {
                	dropPet(player, itemId, slot);
                }
        }

	}
	
	/**
	 * Summon the pet and delete inventory pet item.
	 * @param player
	 * 			The player dropping the pet.
	 * @param itemId
	 * 			The pet identity item in inventory.
	 * @param slot
	 * 			The slot of the pet item in inventory.
	 */
	private static void dropPet(Client player, int itemId, int slot)
	{
		if (player.getPetSummoned())
        {
			player.sendMessage("You already have a pet following you.");
			return;
        }
		if (player.familiar != null) {
			player.sendMessage("You can't have a familiar and a pet at the same time");
			return;
		}
		for (int i = 0; i < PetData.petData.length; i++)
        {
                if (PetData.petData[i][1] == itemId)
                {
                	player.sendMessage("You drop your pet and it starts following you.");
                	Pet.summonPet(player, PetData.petData[i][0], player.absX, player.absY - 1, player.heightLevel);
                }
        }
        ItemAssistant.deleteItem(player, itemId, slot, player.playerItemsN[slot]);
	}

}
