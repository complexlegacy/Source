package ionic.player.dialogue;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.content.minigames.Barrows;
import ionic.player.content.miscellaneous.Decanting;
import ionic.player.content.miscellaneous.Tele;
import ionic.player.content.partyroom.PartyRoom;
import ionic.player.content.skills.construction.rooms.*;
import ionic.player.content.skills.slayer.Slayer;
import ionic.shop.ShopController;

public class Dialogues {
	
	public static DialogueType npcChat = DialogueType.NPC_CHAT;
	public static DialogueType options = DialogueType.OPTIONS;
	public static DialogueType statement = DialogueType.STATEMENT;
	public static DialogueType playerChat = DialogueType.PLAYER_CHAT;
	
	
	public static final DialogueList MINIGAME_TELEPORTS = new DialogueList(new Dialogue[] {
			new Dialogue(options, new String[] {
					"Pest Control", "Fight Caves", "Fight Pits", "Barrows", "Next Page",},
					new Dialogue.Options() {@Override
				public void click(Player c, int option) {
						switch(option) {
						case 1:
							c.getPA().tele("spell", Tele.PEST_CONTROL);
							break;
						case 2:
							c.getPA().tele("spell", Tele.FIGHT_CAVES);
							break;
						case 3:
							c.getPA().tele("spell", Tele.FIGHT_PITS);
							break;
						case 4:
							c.getPA().tele("spell", Tele.BARROWS);
							break;
						case 5:
							send(c, new DialogueList(new Dialogue[] { new Dialogue(options, new String[] {
					"Warriors Guild", "Duel Arena", "Castle Wars", "Soul Wars", "Previous Page",},
					new Dialogue.Options() {@Override
				public void click(Player c, int option) {
						switch(option) {
						case 1:
							c.getPA().tele("spell", Tele.WARRIORS_GUILD);
							break;
						case 2:
							c.getPA().tele("spell", Tele.DUEL_ARENA);
							break;
						case 3:
							c.getPA().tele("spell", Tele.CASTLE_WARS);
							break;
						case 4:
							c.getPA().tele("spell", Tele.SOUL_WARS);
							break;
						case 5:
							send(c, MINIGAME_TELEPORTS);
							break;}}})}));
						break;
					}}})});
	public static final DialogueList CITY_TELEPORTS = new DialogueList(new Dialogue[] {
			new Dialogue(options, new String[] {
					"Edgeville", "Lumbridge", "IDK", "IDK", "Next Page",},
					new Dialogue.Options() {@Override
					public void click(Player c, int option) {
						switch(option) {
							case 1:
								c.getPA().tele("spell", Tele.EDGEVILLE);
								break;
							case 2:
								c.getPA().tele("spell", Tele.LUMBRIDGE);
								break;
							case 3:
								c.getPA().tele("spell", Tele.FALADOR);
								c.sendMessage("Idk WTF is going on");
								break;
							case 4:
								c.sendMessage("Idk");
								break;
							case 5:
								send(c, new DialogueList(new Dialogue[] { new Dialogue(options, new String[] {
										"IDK", "IDK", "IDK", "IDK", "Previous Page",},
										new Dialogue.Options() {@Override
										public void click(Player c, int option) {
											switch(option) {
												case 1:
													c.sendMessage("Idk");
													break;
												case 2:
													c.sendMessage("Idk");
													break;
												case 3:
													c.sendMessage("Idk");
													break;
												case 4:
													c.sendMessage("Idk");
													break;
												case 5:
													send(c, CITY_TELEPORTS);
													break;}}})}));
								break;
						}}})});

	public static final DialogueList BOSS_TELEPORTS = new DialogueList(new Dialogue[] {
			new Dialogue(options, new String[] {
					"IDK", "IDK", "IDK", "Tormented Demons", "Next Page",},
					new Dialogue.Options() {@Override
					public void click(Player c, int option) {
						switch(option) {
							case 1:
								c.sendMessage("Idk");
								break;
							case 2:
								c.sendMessage("Idk");
								break;
							case 3:
								c.sendMessage("Idk");
								break;
							case 4:
								c.getPA().tele("spell", Tele.TORMENTED_DEMONS);
								break;
							case 5:
								send(c, new DialogueList(new Dialogue[] { new Dialogue(options, new String[] {
										"IDK", "IDK", "IDK", "Nex", "Previous Page",},
										new Dialogue.Options() {@Override
										public void click(Player c, int option) {
											switch(option) {
												case 1:
													c.sendMessage("Idk");
													break;
												case 2:
													c.sendMessage("Idk");
													break;
												case 3:
													c.sendMessage("Idk");
													break;
												case 4:
													c.getPA().tele("spell", Tele.NEX);
													break;
												case 5:
													send(c, BOSS_TELEPORTS);
													break;}}})}));
								break;
						}}})});

	public static final DialogueList MONSTER_TELEPORTS = new DialogueList(new Dialogue[] {
			new Dialogue(options, new String[] {
					"Rock Crabs", "Experiments", "IDK", "IDK", "Next Page",},
					new Dialogue.Options() {@Override
					public void click(Player c, int option) {
						switch(option) {
							case 1:
								//c.getPA().tele("spell", T);
								c.sendMessage("idk coords");
								break;
							case 2:
								c.getPA().tele("spell", Tele.EXPERIMENTS);
								break;
							case 3:
								c.sendMessage("Idk");
								break;
							case 4:
								c.sendMessage("Idk");
								break;
							case 5:
								send(c, new DialogueList(new Dialogue[] { new Dialogue(options, new String[] {
										"IDK", "IDK", "IDK", "IDK", "Previous Page",},
										new Dialogue.Options() {@Override
										public void click(Player c, int option) {
											switch(option) {
												case 1:
													c.sendMessage("Idk");
													break;
												case 2:
													c.sendMessage("Idk");
													break;
												case 3:
													c.sendMessage("Idk");
													break;
												case 4:
													c.sendMessage("Idk");
													break;
												case 5:
													send(c, MONSTER_TELEPORTS);
													break;}}})}));
								break;
						}}})});

	public static final DialogueList SKILLING_TELEPORTS = new DialogueList(new Dialogue[] {
			new Dialogue(options, new String[] {
					"Agility", "Mining", "Fishing", "Summoning", "Next Page",}, //Should it be summoning or Runecrafting? Runecrafting tele or mage dude
					new Dialogue.Options() {@Override
					public void click(Player c, int option) {
						switch(option) {
							case 1:
								send(c, AGILITY_TELEPORTS);
								break;
							case 2:
								send(c, MINING_TELEPORTS);
								break;
							case 3:
								send(c, FISHING_TELEPORTS);
								break;
							case 4:
								c.sendMessage("Summoning or Runecrafting?");
								break;
							case 5:
								send(c, new DialogueList(new Dialogue[] { new Dialogue(options, new String[] {
										"Thieving", "Slayer", "Farming", "Hunter", "Previous Page",},
										new Dialogue.Options() {@Override
										public void click(Player c, int option) {
											switch(option) {
												case 1:
													send(c, THIEVING_TELEPORTS);
													break;
												case 2:
													send(c, SLAYER_TELEPORTS);
													break;
												case 3:
													send(c, FARMING_TELEPORTS);
													break;
												case 4:
													send(c, HUNTER_TELEPORTS);
													break;
												case 5:
													send(c, SKILLING_TELEPORTS);
													break;}}})}));
								break;
						}}})});
	public static final DialogueList AGILITY_TELEPORTS = new DialogueList(new Dialogue[] {
			new Dialogue(options, new String[] {
					"Barbarian Course", "Wilderness Course", "Gnome Course", "Another Course", "Back to Skills",}, //Should it be summoning or Runecrafting? Runecrafting tele or mage dude
					new Dialogue.Options() {@Override
					public void click(Player c, int option) {
						switch(option) {
							case 1:
								c.getPA().tele("spell", Tele.BARBARIAN_COURSE);
								break;
							case 2:
								c.getPA().tele("spell", Tele.WILDERNESS_COURSE);
								break;
							case 3:
								c.getPA().tele("spell", Tele.GNOME_COURSE);
								break;
							case 4:
								c.sendMessage("Another Course");
								break;
							case 5:
								send(c, SKILLING_TELEPORTS);
								break;
						}}})});

	public static final DialogueList MINING_TELEPORTS = new DialogueList(new Dialogue[] {
			new Dialogue(options, new String[] {
					"No idea", "No idea", "No idea", "No idea", "Back to Skills",}, //Should it be summoning or Runecrafting? Runecrafting tele or mage dude
					new Dialogue.Options() {@Override
					public void click(Player c, int option) {
						switch(option) {
							case 1:
								c.sendMessage("An area");
								break;
							case 2:
								c.sendMessage("An area");
								break;
							case 3:
								c.sendMessage("An area");
								break;
							case 4:
								c.sendMessage("An area");
								break;
							case 5:
								send(c, SKILLING_TELEPORTS);
								break;
						}}})});

	public static final DialogueList THIEVING_TELEPORTS = new DialogueList(new Dialogue[] {
			new Dialogue(options, new String[] {
					"No Idea", "No Idea", "No Idea", "", "Back to Skills",}, //Should it be summoning or Runecrafting? Runecrafting tele or mage dude
					new Dialogue.Options() {@Override
					public void click(Player c, int option) {
						switch(option) {
							case 1:
								c.sendMessage("An Area");
								break;
							case 2:
								c.sendMessage("An Area");
								break;
							case 3:
								c.sendMessage("An Area");
								break;
							case 4:
								c.sendMessage("An Area");
								break;
							case 5:
								send(c, SKILLING_TELEPORTS);
								break;
						}}})});

	public static final DialogueList FARMING_TELEPORTS = new DialogueList(new Dialogue[] {
			new Dialogue(options, new String[] {
					"Catherby", "No Idea", "No Idea", "No Idea", "Back to Skills",}, //Should it be summoning or Runecrafting? Runecrafting tele or mage dude
					new Dialogue.Options() {@Override
					public void click(Player c, int option) {
						switch(option) {
							case 1:
								c.getPA().tele("spell", Tele.FARMING_CATHERBY);
								c.sendMessage("#makingherbs");
								break;
							case 2:
								c.sendMessage("An Area");
								break;
							case 3:
								c.sendMessage("An Area");
								break;
							case 4:
								c.sendMessage("An Area");
								break;
							case 5:
								send(c, SKILLING_TELEPORTS);
								break;
						}}})});

	public static final DialogueList HUNTER_TELEPORTS = new DialogueList(new Dialogue[] {
			new Dialogue(options, new String[] {
					"No Idea", "No Idea", "No Idea", "Puro Puro", "Back to Skills",}, //Should it be summoning or Runecrafting? Runecrafting tele or mage dude
					new Dialogue.Options() {@Override
					public void click(Player c, int option) {
						switch(option) {
							case 1:
								c.sendMessage("An Area");
								break;
							case 2:
								c.sendMessage("An Area");
								break;
							case 3:
								c.sendMessage("An Area");
								break;
							case 4:
								c.getPA().tele("spell", Tele.PURO_PURO);
								break;
							case 5:
								send(c, SKILLING_TELEPORTS);
								break;
						}}})});

	public static final DialogueList FISHING_TELEPORTS = new DialogueList(new Dialogue[] {
			new Dialogue(options, new String[] {
					"Catherby", "No Idea", "No Idea", "Living Rock Cavern", "Back to Skills",}, //Should it be summoning or Runecrafting? Runecrafting tele or mage dude
					new Dialogue.Options() {@Override
					public void click(Player c, int option) {
						switch(option) {
							case 1:
								c.getPA().tele("spell", Tele.CATHERBY_FISH);
								break;
							case 2:
								c.sendMessage("An Area");
								break;
							case 3:
								c.sendMessage("An Area");
								break;
							case 4:
								c.getPA().tele("spell", Tele.LIVING_ROCK_CAVERN);
								break;
							case 5:
								send(c, SKILLING_TELEPORTS);
								break;
						}}})});

	public static final DialogueList SLAYER_TELEPORTS = new DialogueList(new Dialogue[] {
			new Dialogue(options, new String[] {
					"IDK", "IDK", "Chaos Tunnels", "Ancient Cavern", "Next Page",},
					new Dialogue.Options() {@Override
					public void click(Player c, int option) {
						switch(option) {
							case 1:
								c.sendMessage("IDK");
								break;
							case 2:
								c.sendMessage("IDK");
								break;
							case 3:
								c.getPA().tele("spell", Tele.CHAOS_TUNNELS);
								break;
							case 4:
								c.getPA().tele("spell", Tele.ANCIENT_CAVERN);
								break;
							case 5:
								send(c, new DialogueList(new Dialogue[] { new Dialogue(options, new String[] {
										"IDK", "IDK", "IDK", "IDK", "Previous Page",},
										new Dialogue.Options() {@Override
										public void click(Player c, int option) {
											switch(option) {
												case 1:
													c.sendMessage("IDK");
													break;
												case 2:
													c.sendMessage("IDK");
													break;
												case 3:
													c.sendMessage("IDK");
													break;
												case 4:
													c.sendMessage("IDK");
													break;
												case 5:
													send(c, SLAYER_TELEPORTS);
													break;}}})}));
								break;
						}}})});

	public static final DialogueList PVP_TELEPORTS = new DialogueList(new Dialogue[] {
			new Dialogue(options, new String[] {
					"Edgville", "IDK", "IDK", "IDK", "Next Page",}, //Should it be summoning or Runecrafting? Runecrafting tele or mage dude
					new Dialogue.Options() {@Override
					public void click(Player c, int option) {
						switch(option) {
							case 1:
								c.getPA().tele("spell", Tele.EDGEVILLE);
								break;
							case 2:
								c.sendMessage("Idk");
								break;
							case 3:
								c.sendMessage("Idk");
								break;
							case 4:
								c.sendMessage("Idk");
								break;
							case 5:
								send(c, new DialogueList(new Dialogue[] { new Dialogue(options, new String[] {
										"IDK", "IDK", "IDK", "IDK", "Previous Page",},
										new Dialogue.Options() {@Override
										public void click(Player c, int option) {
											switch(option) {
												case 1:
													c.sendMessage("Idk");
													break;
												case 2:
													c.sendMessage("Idk");
													break;
												case 3:
													c.sendMessage("Idk");
													break;
												case 4:
													c.sendMessage("Idk");
													break;
												case 5:
													c.sendMessage("Idk");
													break;}}})}));
								break;
						}}})});
	
	public static final DialogueList EMPTY_INVENTORY = new DialogueList(new Dialogue[] {
		new Dialogue(statement, new String[] {"Are you sure you want to empty your inventory?", 
				"You will NOT be able to get the items back."}),
		new Dialogue(options, new String[] {"Yes, Empty my inventory", "No, I want to keep my items"},
			new Dialogue.Options() {@Override
			public void click(Player c, int option) {
				switch(option) {
					case 1: ItemAssistant.removeAllItems(c); c.getPA().closeAllWindows(); break;
					case 2: c.getPA().closeAllWindows(); break;}
				}
			}),
		});
	
	public static final DialogueList STARVED_EFFIGY = new DialogueList(new Dialogue[] {
			new Dialogue(statement, new String[] {"As you inspect the ancient effigy, you begin to feel a",
					"strange sensation of the relic searching your mind,", "drawing on your knowledge"})
	});

	public static final DialogueList NOURISHED_EFFIGY = new DialogueList(new Dialogue[] {
			new Dialogue(statement, new String[] {"Images from your experiences of life and cultivation fill",
					"your mind."}),
			new Dialogue(statement, new String[] {"As you focus on your memories, you can almost hear a", "voice in the back of your mind whispering to you..."})
	});

	public static final DialogueList SATED_EFFIGY = new DialogueList(new Dialogue[] {

	});

	public static final DialogueList GORGED_EFFIGY = new DialogueList(new Dialogue[] {

	});

	public static final DialogueList MAKEOVER_MAGE = new DialogueList(
		new Dialogue[] { 
			new Dialogue(npcChat, Emotion.HAPPY, "Makeover Mage", 599, 
					new String[] {"Hello, welcome to Runite!",}),
			new Dialogue(playerChat, Emotion.CONFUSED, 
					new String[] {"Hi... Who are you?"}),
			new Dialogue(npcChat, Emotion.HAPPY, "Makeover Mage", 599, 
					new String[] {"They call me the Makeover Mage.", 
					"I use magic to change people's appearances,",
					"so they look the way they want to"}),
			new Dialogue(npcChat, Emotion.TALK_WONDER, "Makeover Mage", 599, 
					new String[] {"Would you like a makeover?", "I'll do it for free.",}),
			new Dialogue(options, new String[] {"Yes, I would like a makeover.", "No I don't want a makeover."},
					new Dialogue.Options() {@Override
					public void click(Player c, int option) {
						switch(option) {
							case 1: next(c); break;
							case 2: send(c, MAKEOVER_MAGE_NO); break;
						}
					}}),
			new Dialogue(playerChat, Emotion.HAPPY, new String[] {"Yes please!"},
					new Dialogue.Actions() {@Override
					public void perform(Player c) {
						c.getPA().closeAllWindows();
						c.getPA().showInterface(3559);
                        c.canChangeAppearance = true;}}),
		});
	
	public static final DialogueList MAKEOVER_MAGE_NO = new DialogueList(new Dialogue[] { 
		new Dialogue(playerChat, Emotion.NO_WAY, new String[] {"No, thank you for the offer though."}),});
	
	
	public static final DialogueList CORPOREAL_BEAST_ENTRANCE = new DialogueList(new Dialogue[] {
			new Dialogue(statement, new String[] {"Are you sure that you want to continue past this point?", 
			"The Corporeal Beast awaits your visit."}),
			new Dialogue(options, new String[] {
					"Enter lair", "Maybe later"},
				new Dialogue.Options() {@Override
				public void click(Player c, int option) {
				switch(option) {
				case 1:
					if (c.dialogueAction == 1) {
						c.getPA().movePlayer(2970, 4384, 0);
						c.turnPlayerTo(c.absX + 1, c.absY);
					} else {
						c.getPA().movePlayer(2921, 4385, 0);
						c.turnPlayerTo(c.absX - 1, c.absY);
					}
					c.sendMessage("Good luck!");
					c.getPA().closeAllWindows();
					break;
				case 2:
					c.getPA().closeAllWindows();
					break;
				}
			}})});
	
	
	public static final DialogueList SLAYER = new DialogueList(
			new Dialogue[] {
				new Dialogue(npcChat, Emotion.HAPPY, "Vannaka", 1597, 
					new String[] {"Hello, I'm Vannaka, the master of slayer.", "Do you need anything?"}),
				new Dialogue(options, new String[] {
						"How many slayer points do I have?",
						"I want to see the slayer points shop",
						"I want to reset my slayer task",
						"What is my current slayer task?",
						"Can you assign me a slayer task?"},
						new Dialogue.Options() {@Override
					public void click(Player c, int option) {
					switch(option) {
					case 1:
						Dialogue.sendNpcChat(c, "Vannaka", 1597, new String[] {
								"You currently have "+c.slayerPoints+" slayer points", 
								"And you are on a task streak of "+c.slayerStreak+" tasks in a row."}, Emotion.THINK);
						break;
					case 2:
						ShopController.openShopOwner((Client)c, 1597);
						break;
					case 3:
						Slayer.resetTask(c);
						break;
					case 4:
						Slayer.tellTask(c, false);
						break;
					case 5:
						if (c.taskId != 0) {
							send(c, GET_TASK_NO);
						} else {
							send(c, GET_TASK);
						}
						break;
					}
				}}),
			});
	
	
	public static final DialogueList GET_TASK = new DialogueList(
			new Dialogue[] {
				new Dialogue(playerChat, Emotion.HAPPY, new String[] {"Could you assign me a slayer task please?"}),
				new Dialogue(npcChat, Emotion.HAPPY, "Vannaka", 1597, 
					new String[] {"Yes, certainly brave warrior.", "What difficulty of a task would you like me to give?"}),
				new Dialogue(options, new String[] {
						"I want an EASY task",
						"I want a MEDIUM task",
						"I want a HARD task",
						"I want a BOSS task",},
						new Dialogue.Options() {@Override
					public void click(Player c, int option) {
					switch(option) {
					case 1:
						Slayer.attemptAssign(c, Slayer.Difficulty.EASY);
						break;
					case 2:
						Slayer.attemptAssign(c, Slayer.Difficulty.MEDIUM);
						break;
					case 3:
						Slayer.attemptAssign(c, Slayer.Difficulty.HARD);
						break;
					case 4:
						Slayer.attemptAssign(c, Slayer.Difficulty.BOSS);
						break;
				}}}),});
	
	public static final DialogueList GET_TASK_NO = new DialogueList(
			new Dialogue[] {
				new Dialogue(playerChat, Emotion.HAPPY, new String[] {"Could you assign me a slayer task please?"}),
				new Dialogue(npcChat, Emotion.HAPPY, "Vannaka", 1597, 
					new String[] {
						"Sorry, you already have a slayer task.", 
						"Please finish the task that I have assigned you.", 
						"I can reset your task, but your task streak will end."}),});
	
	
	
	
	
	public static final DialogueList PARTY_PETE = new DialogueList(
			new Dialogue[] {
				new Dialogue(npcChat, Emotion.LAUGHING_GOOFY, "Party Pete", 659, new String[] {"Hello adventurer!", "How can I help you?",}),
				new Dialogue(options, new String[] {"Why are you dancing so much?","How does the party room work?", "Can you estimate the value of the items waiting?"},
				new Dialogue.Options() {@Override
					public void click(Player c, int option) {
						switch(option) {
							case 1:
								send(c, new DialogueList(
										new Dialogue[] {
											new Dialogue(playerChat, Emotion.THINK, new String[] {"Why are you dancing so much?"}),
											new Dialogue(npcChat, Emotion.LAUGHING_GOOFY, "Party Pete", 659, new String[] {
													"The real question is - Why are you not dancing?",
													"This is the party room, adventurer!",
													"I party all day all night, that's how I got my name!",
											}),}));
							break;
							case 2:
								send(c, new DialogueList(
										new Dialogue[] {
											new Dialogue(playerChat, Emotion.CONFUSED, new String[] {"How does the party room work?"}),
											new Dialogue(npcChat, Emotion.LAUGHING, "Party Pete", 659, new String[] {
													"Well, you see that chest over there?",
													"People can put their items into it, and when they",
													"pull the lever beside it, a timer will start"}),
											new Dialogue(npcChat, Emotion.LAUGHING, "Party Pete", 659, new String[] {
													"When the timer finishes, the items in the",
													"@red@Items waiting to be dropped box",
													"will be moved to the @red@Items being dropped box",
													"and then balloons will fall.",
													}),
											new Dialogue(npcChat, Emotion.LAUGHING, "Party Pete", 659, new String[] {
													"And then players can pop the balloons,",
													"which contain items from the chest.",
													"...",
													"Isn't this place so much fun?",
												}),}));
							break;
							case 3:
								send(c, new DialogueList(
										new Dialogue[] {
											new Dialogue(playerChat, Emotion.CONFUSED, new String[] {
													"Hey, could you tell me an estimated value",
													"of all the items waiting to be dropped?",}),
											new Dialogue(npcChat, Emotion.LAUGHING, "Party Pete", 659, new String[] {
													"Sure, I have estimated the total value to be about:",
													"@red@"+PartyRoom.getConvertedValue()+"@bla@ coins",
													" ",
													"K = thousand,    M = million,    B = Billion"}),}));
							break;
						}
					}}),});
	
	
	public static final DialogueList THIEF = new DialogueList(
			new Dialogue[] {
					new Dialogue(playerChat, Emotion.HAPPY, new String[] {
							"Hey, what is that cape you're wearing?"}),
					new Dialogue(npcChat, Emotion.NORMAL, "Martin Thwait", 2270, new String[] {
							"Oh, this is the thieving mastery cape.",
							"Only people who have mastered the thieving",
							"skill may aquire one."}),
					new Dialogue(playerChat, Emotion.UNSURE, new String[] {
							"So are you a thief then?",
							"Since you have that cape, you must be..."}),
					new Dialogue(npcChat, Emotion.TALK_SWINGING_HEAD, "Martin Thwait", 2270, new String[] {
							"I gave up on my life of crime a long time ago.",
							"One day a guard stabbed me after he caught me.",
							"I've never stolen anything since that day."}),
					new Dialogue(npcChat, Emotion.HAPPY, "Martin Thwait", 2270, new String[] {
							"However, I do support thieves, so if you steal from",
							"any of those stalls over there, I will gladly buy",
							"the supplies off of you for a good price.",
							"Have a nice day!"}),
					});
	
	
	
	
	public static final DialogueList CONSTRUCTION = new DialogueList(new Dialogue[] {
			new Dialogue(options, new String[] {
					"Default(Grass)",
					"Garden",
					"Parlour",
					"Bedroom",
					"Next Page >",},
					new Dialogue.Options() {@Override
				public void click(Player c, int option) {
						switch(option) {
						case 1: c.replaceWith = new Default(); break;
						case 2: c.replaceWith = new Garden(); break;
						case 3: c.replaceWith = new Parlour(); break;
						case 4: c.replaceWith = new Bedroom(); break;
						case 5: break;
						}
						if (option != 5) {
							c.getPA().closeAllWindows();
							c.getHouse().buildRoom((Client)c);
						}
					}})});
	
	public static final DialogueList BARROWS_ENTER_TOMB = new DialogueList(new Dialogue[] {
			new Dialogue(statement, new String[] {"You found a hidden tunnel...", 
			"Do you want to enter?"}),
			new Dialogue(options, new String[] {
					"Yeah, I'm fearless!",
					"No way, that looks scary!"},
					new Dialogue.Options() {@Override
				public void click(Player c, int option) {
					switch(option) {
						case 1:
							Barrows.enterTunnel(c);
						break;
					}
					c.getPA().closeAllWindows(); 
				}})});
	
	public static final DialogueList DECANT = new DialogueList(new Dialogue[] {
			new Dialogue(playerChat, Emotion.HAPPY, new String[] {"Hey, what are you doing here?"}),
			new Dialogue(npcChat, Emotion.HAPPY, "Bob Barter", 6524, new String[] {
					"I am Bob Barter, I decant potions for people",
					"I can decant items in your bank or your inventory",
					"Would you like me to decant potions for you?"}),
			new Dialogue(options, new String[] {
					"Decant my bank",
					"Decant my inventory",
					"No thanks"},
					new Dialogue.Options() {@Override
				public void click(Player c, int option) {
					switch(option) {
						case 1:
							Decanting.decantBank(c);
						break;
						case 2:
						break;
						case 3:
						break;
					}
					c.getPA().closeAllWindows();
				}
			})
	});
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Sends a dialogue to a player
	 * @param c - The player who receives the dialogue
	 * @param l - The DialogueList that's being sent
	 */
	public static void send(Player c, DialogueList l) {
		c.dialogueProgress = 0;
		l.dialogues[0].sendDialogue(c);
		c.dialogues = l;
	}
	
	
	/**
	 * Sends the next part of a players dialogue to the player
	 * @param c - the player who is clicking
	 */
	public static void next(Player c) {
		if (c.dialogues != null) {
			if (c.dialogues.dialogues[c.dialogueProgress].actions != null) {
				c.dialogues.dialogues[c.dialogueProgress].actions.perform(c);
				return;
			}
			c.dialogueProgress++;
			if (c.dialogues.dialogues.length == c.dialogueProgress) {
				c.getPA().closeChatWindows();
				return;
			}
			c.dialogues.dialogues[c.dialogueProgress].sendDialogue(c);
		} else {
			c.getPA().closeChatWindows();
		}
	}

	
	
	
}
