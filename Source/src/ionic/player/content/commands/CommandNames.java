package ionic.player.content.commands;
import ionic.player.content.commands.handlers.*;

/**
 * @author Keith
 */

public class CommandNames {
	
	/**
	 * Gets a command class by the command text
	 */
	public static Command getCommandByName(String command) {
		command = command.toLowerCase();
		Command cmd = new UnexistingCommand();
		cmd = command.startsWith("sendcaperecolor") ? cmd = new Recoloring() : cmd;
		cmd = command.startsWith("recolorween") ? cmd = new Recoloring() : cmd;
		cmd = command.startsWith("recolorphat") ? cmd = new Recoloring() : cmd;
		cmd = command.startsWith("requestcolorresets") ? cmd = new Recoloring() : cmd;
		cmd = command.startsWith("capergbcolors") ? cmd = new Recoloring() : cmd;
		cmd = command.startsWith("setcomppreset") ? cmd = new Recoloring() : cmd;
		cmd = command.startsWith("item") ? cmd = new Spawn() : cmd;
		cmd = command.startsWith("pickup") ? cmd = new Spawn() : cmd;
		cmd = command.startsWith("spawn") ? cmd = new Spawn() : cmd;
		cmd = command.contains("tele") ? cmd = new Teleporting() : cmd;
		cmd = command.startsWith("npc") ? cmd = new NpcSpawn() : cmd;
		cmd = command.startsWith("pnpc") ? cmd = new NpcTransform() : cmd;
		cmd = command.startsWith("obj") ? cmd = new SpawnObject() : cmd;
		cmd = command.startsWith("anim") ? cmd = new ForceAnimation() : cmd;
		cmd = command.startsWith("gfx") ? cmd = new ForceGFX() : cmd;
		cmd = command.startsWith("interface") ? cmd = new OpenInterface() : cmd;
		cmd = command.startsWith("profilesearch") ? cmd = new OpenInterface() : cmd;
		cmd = command.startsWith("spec") ? cmd = new EditSpec() : cmd;
		cmd = command.equals("mypos") ? cmd = new MyPos() : cmd;
		cmd = command.equals("home") ? cmd = new TeleportHome() : cmd;
		cmd = command.equals("edge") ? cmd = new TeleportHome() : cmd;
		cmd = command.equals("empty") ? cmd = new EmptyInventory() : cmd;
		cmd = command.startsWith("examinemoneypouch") ? cmd = new PouchExamine() : cmd;
		cmd = command.startsWith("dropselecteditems") ? cmd = new DropSelectedItems() : cmd;
		cmd = command.startsWith("changebanktabname") ? cmd = new ChangeBankTabName() : cmd;
		cmd = command.startsWith("getid") ? cmd = new GetItemByName() : cmd;
		cmd = command.startsWith("#clan") ? cmd = new ClanCommands() : cmd;
		cmd = command.contains("proomchest") ? cmd = new PartyRoomCommands() : cmd;
		cmd = command.contains("pricechecker") ? cmd = new PartyRoomCommands() : cmd;
		cmd = command.contains("repairitem") ? cmd = new PartyRoomCommands() : cmd;
		cmd = command.contains("gamble") ? cmd = new PartyRoomCommands() : cmd;
		cmd = command.equals("loadach") ? cmd = new ReloadAchievements() : cmd;
		cmd = command.startsWith("searchbank") ? cmd = new SearchBank() : cmd;
		cmd = command.equals("bank") ? cmd = new OpenBank() : cmd;
		cmd = command.equals("gfx") ? cmd = new GFX() : cmd;
		cmd = command.startsWith("setpreset") ? cmd = new PresetName() : cmd;
		cmd = command.equals("pohload") ? cmd = new PohLoad() : cmd;
		cmd = command.equals("pohsave") ? cmd = new PohSave() : cmd;
		cmd = command.equals("poh") ? cmd = new PohEnter() : cmd;
		cmd = command.equals("pohebm") ? cmd = new PohEnterBuildingMode() : cmd;
		cmd = command.startsWith("test") ? cmd = new TestCommand() : cmd;
		return cmd;
		
	}
	
	
	

	
	
	
}
