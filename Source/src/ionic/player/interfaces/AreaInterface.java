package ionic.player.interfaces;

import core.Constants;
import ionic.player.Player;
import ionic.player.content.miscellaneous.Skull;
import ionic.player.content.skills.summoning.Summoning;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;

/**
 * Update walkable interfaces that are changed if the player has entered an area.
 * @author MGT Madness 2-11-2013
 */

public class AreaInterface
{

	/**
	 * Start the cycle event of showing interfaces that last for 30 seconds.
	 */
	public static void startInterfaceEvent(final Player player) {
		player.extraTime = 50; // Equivelant to 30 seconds. Because someone might walk a long route, so it might take quite long.
		if (player.isUsingInterfaceEvent) {
			return;
		}
		player.isUsingInterfaceEvent = true;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
			if (player.extraTime > 0) {
				player.extraTime--;
				if (player.adminAttack || player.inSafePkArea()) {
					player.getPA().showOption(3, 0, "Attack", 1);
				} else if (player.inWilderness() && !false && !player.inSafePkArea()) {
					int modY = player.absY > 6400 ? player.absY - 6400 : player.absY;
					int previousLevel = player.wildLevel;
					player.wildLevel = (((modY - 3520) / 8) + 1);
					if (player.wildyIcon == false) {
						player.wildyIcon = true;
						Summoning.familiarTele(player, true);
						player.getPA().walkableInterface(197);
					}
					if (player.wildLevel != previousLevel) {
						player.getPA().walkableInterface(197);
						if (player.inMulti()) {
							player.getPA().sendFrame126("@yel@Level: " + player.wildLevel, 199);
						} else {
							player.getPA().sendFrame126("@yel@Level: " + player.wildLevel, 199);
						}
						player.getPA().showOption(3, 0, "Attack", 1);
					}
				} else if (!player.inWilderness() && player.wildyIcon) {
					player.wildyIcon = false;
					player.getPA().walkableInterface(0);
					Summoning.familiarTele(player, true);
				} else if (!player.inWilderness() && player.playerEquipment[3] == 10501) {
					player.getPA().showOption(3, 0, "Pelt", 1);
				} else if (player.inDuelArena()) {
					player.getPA().walkableInterface(201);
					if (player.duelStatus == 5) {
						player.getPA().showOption(3, 0, "Attack", 1);
					} else {
						player.getPA().showOption(3, 0, "Challenge", 1);
					}
				} else if (player.inPartyRoom()) {
					player.getPA().showOption(3, 0, "Gamble with", 1);
				} else {
					player.getPA().sendFrame99(0);
					player.getPA().showOption(3, 0, "Null", 1);
				}
				if (!player.hasMultiSign && player.inMulti()) {
					player.hasMultiSign = true;
					player.getPA().multiWay(1);
				} else if (player.hasMultiSign && !player.inMulti()) {
					player.hasMultiSign = false;
					player.getPA().multiWay(-1);
				}
			}
			if (player.extraTime <= 0) {
				container.stop();
			}
		}
		@Override
		public void stop() {
			player.isUsingInterfaceEvent = false;
		}
		}, 1);

	}

	public static void startSkullTimerEvent(final Player player)
	{

		if (player.isUsingSkullTimerEvent || player.skullTimer == 0)
		{
			return;
		}
		player.isUsingSkullTimerEvent = true;

		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
		{@
			Override
			public void execute(CycleEventContainer container)
		{
			player.skullTimer--;
			if (player.skullTimer <= 1)
			{
				Skull.clearSkull(player);
				container.stop();
			}
		}@
		Override
		public void stop()
		{
			player.isUsingSkullTimerEvent = false;
		}
		}, 1);

	}

}