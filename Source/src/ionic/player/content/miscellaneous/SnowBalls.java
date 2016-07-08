package ionic.player.content.miscellaneous;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import ionic.player.interfaces.AreaInterface;

public class SnowBalls {
	
	public static void throwSnowBall(Player thrower, Player victim) {
		final int offX = (thrower.getY() - victim.getY())* -1;
		final int offY = (thrower.getX() - victim.getX())* -1;
		final int x = victim.getX();
		final int y = victim.getY();
		if (thrower.playerEquipment[3] <= 0) {
			return;
		}
		thrower.getPA().createPlayersProjectile(thrower.getX(), thrower.getY(), offX, offY, 50, 90, 1281, 21, 21, -victim.getId() - 1, 65);
		thrower.startAnimation(7530);
		thrower.turnPlayerTo(victim.getX(), victim.getY());
		if (thrower.playerEquipment[3] == 10501) {
			thrower.playerEquipmentN[3] -= 1;
			if (thrower.playerEquipmentN[3] <= 0) {
				thrower.playerEquipment[3] = -1;
				AreaInterface.startInterfaceEvent(thrower);
			}
			ItemAssistant.updateEquipmentInterface(thrower);
		}
		CycleEventHandler.getSingleton().addEvent(thrower, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				container.stop();
			}
			@Override
			public void stop() {
				if (victim != null && thrower != null) {
					if (victim.getX() == x && victim.getY() == y) {
						victim.gfx0(1282);
					} else {
						thrower.sendMessage(""+victim.playerName+" dodged your snow ball!");
					}
				}
			}
		}, 3);
	}

}
