package ionic.player.content.quest.impl;

import ionic.item.ItemAssistant;
import ionic.player.Player;
import ionic.player.content.quest.Quest;
import ionic.player.content.quest.QuestHandler;
import ionic.player.content.quest.QuestTab;
import ionic.player.dialogue.*;
import ionic.player.dialogue.Dialogue.Options;

public class StolenCannon implements Quest {

	public DialogueList[] dialogues = null;
	public Options[] dialogueOptions = null;


	@Override
	public void clickQuestListButton(Player c) {
		c.getPA().showInterface(8134);
		c.getPA().sendFrame126("The Stolen Cannon", 8144);
		if (!meetsRequirements(c)) {
			c.getPA().sendFrame126("Requirements to start this quest:", 8147);
			QuestHandler.questText(c, "Level 60 thieving", 8148, QuestHandler.requiredLevel(c, 17, 60));
			QuestHandler.questText(c, "Level 80 combat", 8149, c.combatLevel >= 80);
		} else {
			if (getProgress(c) == 0) {
				c.getPA().sendFrame126("I can start this quest by talking to Nulodion who", 8147);
				c.getPA().sendFrame126("is located north of Falador, in a dwarf village.", 8148);
				setProgress(c, 5);
			} else {
				if (getProgress(c) >= 1) {
					QuestHandler.questText(c, "I have talked to Nulodion, he said that", 8147, true);
					QuestHandler.questText(c, "his new invention, the dwarf multicannon", 8148, true);
					QuestHandler.questText(c, "has disappeared.", 8149, true);
					QuestHandler.questText(c, "I should ask Nulodion where I can find Grundt", 8150, getProgress(c) > 1);
					if (getProgress(c) >= 2) {
						QuestHandler.questText(c, "After asking Nulodion where to find Grundt,", 8151, getProgress(c) > 2);
						QuestHandler.questText(c, "I've learned that he's at the Falador castle.", 8152, getProgress(c) > 2);
						QuestHandler.questText(c, "I should go talk to him.", 8153, getProgress(c) > 2);
						if (getProgress(c) >= 3) {
							QuestHandler.questText(c, "I need to pay Grundt 100,000 coins.", 8154, getProgress(c) > 3);
							if (getProgress(c) >= 4) {
								QuestHandler.questText(c, "I should ask Grundt to tell me who stole it", 8155, getProgress(c) > 4);
								if (getProgress(c) >= 5) {
									QuestHandler.questText(c, "I asked Grundt to tell me who took the cannon", 8156, getProgress(c) > 5);
									QuestHandler.questText(c, "but he told me to go away. I should go tell Nulodion", 8157, getProgress(c) > 5);
									if (getProgress(c) >= 6) {
										QuestHandler.questText(c, "Nulodion wants me to steal Grundt's diary", 8158, getProgress(c) > 6);
										QuestHandler.questText(c, "and force him to tell me who stole the cannon.", 8159, getProgress(c) > 6);
									}
								}
							}
						}
					}
				}
			}
		}
	}





	@Override
	public boolean talkToNpc(Player c, int npc) {
		switch(npc) {
		case 209:
			if (getProgress(c) == 0) {
				if (meetsRequirements(c)) {
					Dialogues.send(c, dialogues[0]);
				} else {
					c.sendMessage("Nulodion isn't interested in talking to you.");
				}
			} else if (getProgress(c) == 1) {
				c.dialogues = dialogues[3];
				c.dialogueProgress = 3;
				dialogues[3].dialogues[3].sendDialogue(c);
			} else if (getProgress(c) == 5) {
				Dialogues.send(c, dialogues[17]);
			} else if (getProgress(c) == 6) {
				Dialogues.send(c, dialogues[18]);
			}
			return true;


		case 5502:
			if (getProgress(c) == 2) {
				Dialogues.send(c, dialogues[8]);
			} else if (getProgress(c) < 2) {
				c.sendMessage("Grundt isn't interested in talking to you.");
			} else if (getProgress(c) == 3) {
				Dialogues.send(c, dialogues[12]);
			} else if (getProgress(c) == 4) {
				Dialogues.send(c, dialogues[16]);
			}
			return true;

		}
		return false;
	}




	@Override
	public void loadDialogue() {
		dialogueOptions = new Options[5];
		dialogues = new DialogueLoader("The Stolen Cannon", 19, dialogueOptions).getDialogues();
		loadOptions();
		setOptions();
	}




	private void loadOptions() {
		dialogueOptions[0] = new Options() {
			@Override
			public void click(Player c, int option) {
				if (option == 1)
					Dialogues.send(c, dialogues[1]);
				else
					c.getPA().closeAllWindows();
			}
		};

		dialogueOptions[1] = new Options() {
			@Override
			public void click(Player c, int option) {
				if (option == 1)
					Dialogues.send(c, dialogues[3]);
				else
					Dialogues.send(c, dialogues[2]);
			}
		};

		dialogueOptions[2] = new Options() {
			@Override
			public void click(Player c, int option) {
				if (option == 1) {
					if (getProgress(c) == 1) {
						setProgress(c, 2);
					}
					Dialogues.send(c, dialogues[5]);
				} else if (option == 2)
					Dialogues.send(c, dialogues[6]);
				else if (option == 3)
					Dialogues.send(c, dialogues[7]);
				else if (option == 4)
					c.getPA().closeAllWindows();
			}
		};

		dialogues[3].dialogues[3].actions = new Dialogue.Actions() {
			@Override
			public void perform(Player c) {
				if (getProgress(c) == 0) {
					setProgress(c, 1);
					QuestTab.updateQuestTab(c);
				}
				Dialogues.send(c, dialogues[4]);
			}
		};

		dialogues[5].dialogues[1].actions = dialogues[6].dialogues[1].actions = dialogues[7].dialogues[1].actions = 
				new Dialogue.Actions() {
			@Override
			public void perform(Player c) {
				Dialogues.send(c, dialogues[4]);
			}
		};



		dialogueOptions[3] = new Options() {
			@Override
			public void click(Player c, int option) {
				if (option == 1)
					Dialogues.send(c, dialogues[9]);
				else if (option == 2)
					Dialogues.send(c, dialogues[10]);
				else if (option == 3)
					Dialogues.send(c, dialogues[11]);
			}
		};

		dialogues[9].dialogues[4].actions = new Dialogue.Actions() {
			@Override
			public void perform(Player c) {
				if (getProgress(c) == 2) {
					setProgress(c, 3);
					c.getPA().closeAllWindows();
				}
			}
		};

		dialogues[13].dialogues[0].actions = new Dialogue.Actions() {
			@Override
			public void perform(Player c) {
				if (ItemAssistant.playerHasItem(c, 995, 100000)) {
					ItemAssistant.deleteItem1(c, 995, 100000);
					Dialogues.send(c, dialogues[14]);
					if (getProgress(c) == 3) {
						setProgress(c, 4);
					}
				} else {
					Dialogues.send(c, dialogues[15]);
				}
			}
		};

		dialogues[14].dialogues[0].actions = new Dialogue.Actions() {
			@Override
			public void perform(Player c) {
				Dialogues.send(c, dialogues[16]);
			}
		};
		
		dialogues[17].dialogues[5].actions = new Dialogue.Actions() {
			@Override
			public void perform(Player c) {
				if (getProgress(c) == 5)
					setProgress(c, 6);
				c.getPA().closeAllWindows();
			}
		};

		dialogues[16].dialogues[1].actions = new Dialogue.Actions() {
			@Override
			public void perform(Player c) {
				if (getProgress(c) == 4) {
					setProgress(c, 5);
					c.getPA().closeAllWindows();
				}
			}
		};


		dialogueOptions[4] = new Options() {
			@Override
			public void click(Player c, int option) {
				if (option == 1)
					Dialogues.send(c, dialogues[13]);
				else if (option == 2)
					c.getPA().closeAllWindows();
			}
		};

	}




	private void setOptions() {
		int total = 0;
		for (DialogueList j : dialogues) {
			for (Dialogue d : j.dialogues) {
				if (d != null && d.type != null && d.type == DialogueType.OPTIONS) {
					d.options = dialogueOptions[total];
					total++;
				}
			}
		}
	}





	@Override
	public void setProgress(Player c, int amount) {
		c.questProgress[QuestHandler.QuestList.THE_STOLEN_CANNON.getId()] = amount;
	}





	@Override
	public int getProgress(Player c) {
		return c.questProgress[QuestHandler.QuestList.THE_STOLEN_CANNON.getId()];
	}





	@Override
	public boolean meetsRequirements(Player c) {
		if (QuestHandler.requiredLevel(c, 17, 60) && c.combatLevel >= 80) {
			return true;
		}
		return false;
	}



}
