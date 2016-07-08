package ionic.player.content.combat.specials;

import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.npc.NPC;
import ionic.player.Client;
import ionic.player.content.combat.specials.spec.*;
import core.Constants;

public class SpecialHandler {
	
	
	public static void specialAttack(Client c, int weapon, Client targetPlayer, NPC targetNPC) {
		Special spec = null;
		if (ItemData.data[weapon].isLent()) {
			weapon = ItemData.data[weapon].getUnlentItemId();
		}
			switch(weapon) {
				case 19780: spec = new KorasiSpec(); break;
				case 11696: spec = new BandosGodswordSpec(); break;
				case 11698: spec = new SaradominGodswordSpec(); break;
				case 11694: spec = new ArmadylGodswordSpec(); break;
				case 11700: spec = new ZamorakGodswordSpec(); break;
				case 13928: case 13926: case 13904: case 13902: spec = new StatiusWarhammerSpec(); break;
				case 1305: spec = new DragonLongswordSpec(); break;
				case 1249: spec = new DragonSpearSpec(); break;
				case 3204: spec = new DragonHalberdSpec(); break;
				case 4587: spec = new DragonScimitarSpec(); break;
				case 1434: spec = new DragonMaceSpec(); break;
				case 10887: spec = new BarrelchestAnchorSpec(); break;
				case 1215: spec = new DragonDaggersSpec(); break;
				case 1231: spec = new DragonDaggersSpec(); break;
				case 5680: spec = new DragonDaggersSpec(); break;
				case 5698: spec = new DragonDaggersSpec(); break;
				case 11730: spec = new SaradominSwordSpec(); break;
				case 4151: spec = new AbyssalWhipSpec(); break;
                case 15445: spec = new AbyssalWhipSpec(); break;
                case 15444: spec = new AbyssalWhipSpec(); break;
                case 15443: spec = new AbyssalWhipSpec(); break;
                case 15442: spec = new AbyssalWhipSpec(); break;
                case 15441: spec = new AbyssalWhipSpec(); break;
                case 14484: spec = new DragonClawsSpec(); break;
                case 13929: case 13931: case 13907: case 13905: spec = new VestaSpearSpec(); break;
                case 13923: case 13925: case 13901: case 13899: spec = new VestaLongswordSpec(); break;
                case 15241: spec = new HandCannonSpec(); break;
                case 13883: spec = new MorriganAxeSpec(); break;
                case 13879: spec = new MorriganJavelinSpec(); break;
                case 859: spec = new MagicLongBowSpec(); break;
                case 861: spec = new MagicShortBowSpec(); break;
                case 11235: spec = new DarkBowSpec(); break;
                case 3101: spec = new RuneClawSpec(); break;
                case 11061: spec = new AncientMaceSpec(); break;
                case 4153: spec = new GraniteMaulSpec(); break;

			}
		int delay = c.getCombat().getHitDelay(ItemAssistant.getItemName(c.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
		if (spec != null) {
			spec.perform(c, delay, targetPlayer, targetNPC);
		}
	}
	

}
