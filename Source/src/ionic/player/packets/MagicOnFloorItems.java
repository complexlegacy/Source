package ionic.player.packets;
import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.player.Client;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import ionic.player.movement.Movement;
import core.Server;


/**
 * Magic on floor items
 **/
public class MagicOnFloorItems implements PacketType
{

    @Override
    public void processPacket(final Client player, int packetType, int packetSize)
    {
        final int itemY = player.getInStream().readSignedWordBigEndian();
        int itemId = player.getInStream().readUnsignedWord();
        final int itemX = player.getInStream().readSignedWordBigEndian();
        if (!Server.itemHandler.itemExists(itemId, itemX, itemY))
        {
            Movement.stopMovement(player);
            return;
        }
        player.usingMagic = true;
        if (!player.getCombat().checkMagicReqs(51))
        {
            Movement.stopMovement(player);
            return;
        }

        /*
		 * Telegrab spell
		 */
        if ((((ItemAssistant.freeSlots(player) >= 1) || ItemAssistant.playerHasItem(player, itemId, 1)) 
        		&& ItemData.data[itemId].stackable) || ((ItemAssistant.freeSlots(player) > 0) 
        				&& !ItemData.data[itemId].stackable))
        {
            if (player.goodDistance(player.getX(), player.getY(), itemX, itemY, 12))
            {
                player.walkingToItem = true;
                int offY = (player.getX() - itemX) * -1;
                int offX = (player.getY() - itemY) * -1;
                player.teleGrabX = itemX;
                player.teleGrabY = itemY;
                player.teleGrabItem = itemId;
                player.turnPlayerTo(itemX, itemY);
                player.teleGrabDelay = System.currentTimeMillis();
                player.startAnimation(player.MAGIC_SPELLS[51][2]);
                player.gfx100(player.MAGIC_SPELLS[51][3]);
                player.getPA().createPlayersStillGfx(144, itemX, itemY, 0, 72);
                player.getPA().createPlayersProjectile(player.getX(), player.getY(), offX, offY, 50, 70, player.MAGIC_SPELLS[51][4], 50, 10, 0, 50);
                player.getPA().addSkillXP(player.MAGIC_SPELLS[51][7], 6);
                player.getPA().refreshSkill(6);
                Movement.stopMovement(player);
                CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
                {@Override
                    public void execute(CycleEventContainer container)
                    {
                        if (!player.walkingToItem) container.stop();
                        if (System.currentTimeMillis() - player.teleGrabDelay > 1550 && player.usingMagic)
                        {
                            if (Server.itemHandler.itemExists(player.teleGrabItem, player.teleGrabX, player.teleGrabY) && player.goodDistance(player.getX(), player.getY(), itemX, itemY, 12))
                            {
                                Server.itemHandler.removeGroundItem(player, player.teleGrabItem, player.teleGrabX, player.teleGrabY, true);
                                player.usingMagic = false;
                                container.stop();
                            }
                        }
                    }@Override
                    public void stop()
                    {
                        player.walkingToItem = false;
                    }
                }, 1);
            }
        }
        else
        {
            player.sendMessage("You don't have enough space in your inventory.");
            Movement.stopMovement(player);
        }
    }

}