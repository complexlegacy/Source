package ionic.player.dialogue;

import ionic.npc.NPCData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DialogueLoader {

	private DialogueList[] list;
	
	public DialogueList[] getDialogues() {
		return list;
	}

	public DialogueLoader(String filename, int listAmount, Dialogue.Options[] dialogueOptions) {
		BufferedReader reader = null;
		try {
			try {
				reader = new BufferedReader(new FileReader("./data/dialogues/"+filename+".txt"));
			} catch (FileNotFoundException localFileNotFoundException) {
				return;
			}
			String line = "";
			String[] split = null;
			Dialogue[] dialogues = null;
			DialogueList[] lists = new DialogueList[listAmount];
			int currentDialogue = 0;
			int currentList = 0;
			int currentOption = 0;
			try {
				line = reader.readLine();
			} catch (IOException localIOException2) {
			}
			try {
				while((line = reader.readLine()) != null) {
					split = line.split("	");
					if (split[0].equals("[LIST]")) {
						dialogues = new Dialogue[Integer.parseInt(split[1])];
					} else if (split[0].equals("[ENDLIST]")) {
						lists[currentList] = new DialogueList(dialogues);
						currentList++;
						dialogues = null;
						currentDialogue = 0;
					} else if (split[0].equals("NPCCHAT")) {
						dialogues[currentDialogue] = loadNpcChat(split[1]);
						currentDialogue++;
					} else if (split[0].equals("PLAYERCHAT")) {
						dialogues[currentDialogue] = loadPlayerChat(split[1]);
						currentDialogue++;
					} else if (split[0].equals("OPTION")) {
						dialogues[currentDialogue] = loadOption(split[1], dialogueOptions[currentOption]);
						currentDialogue++;
						currentOption++;
					}
				}
			} finally {
				this.list = lists;
				reader.close();
			}
		}catch (IOException ex) {
		}
	}


	private static Dialogue loadNpcChat(String line) {
		String[] b = line.split("  -  ");
		return new Dialogue(
				DialogueType.NPC_CHAT, 
				Emotion.getByName(b[0]), 
				NPCData.data[Integer.parseInt(b[1])].name, 
				Integer.parseInt(b[1]), 
				b[2].split("@@@")
				);
	}

	private static Dialogue loadOption(String line, Dialogue.Options optionClicks) {
		return new Dialogue(
				DialogueType.OPTIONS,
				line.split("@@@"),
				optionClicks
				);
	}

	private static Dialogue loadPlayerChat(String line) {
		String[] b = line.split("  -  ");
		return new Dialogue(
				DialogueType.PLAYER_CHAT, 
				Emotion.getByName(b[0]), 
				b[1].split("@@@")
				);
	}


}
