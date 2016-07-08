package ionic.player.content.miscellaneous;

import java.math.BigInteger;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.banking.BankHandler;
import ionic.player.dialogue.Dialogue;
import ionic.player.dialogue.DialogueList;
import ionic.player.dialogue.DialogueType;
import ionic.player.dialogue.Dialogues;

public class WealthEvaluator {

	public static void open(Player c) {
		int[] time = getEvaluatorTime(c);
		if (time[0] == 0 && time[1] == 0 && time[2] == 0) {
			c.sendMessage("You don't have any time remaining on the wealth evaluator.");
			c.getPA().sendFrame126("Inventory: @gr2@...", 37318);
			c.getPA().sendFrame126("Bank: @gr2@...", 37319);
			c.getPA().sendFrame126("Equipment: @gr2@...", 37320);
			c.getPA().sendFrame126("Money Pouch: @gr2@...", 37321);
			c.getPA().sendFrame126("No time left", 37331);
			c.getPA().sendFrame126(" ", 37323);
		} else {
			BigInteger inventory = getValue(c.playerItems, c.playerItemsN);
			BigInteger bank = BankHandler.getBankValue((Client)c);
			BigInteger pouch = c.pouchCoins;
			BigInteger equipment = getValue(c.playerEquipment, c.playerEquipmentN);
			c.getPA().sendFrame126("Inventory: @gr2@"+BankHandler.insertCommas(""+inventory), 37318);
			c.getPA().sendFrame126("Bank: @gr2@"+BankHandler.insertCommas(""+bank), 37319);
			c.getPA().sendFrame126("Equipment: @gr2@"+BankHandler.insertCommas(""+equipment), 37320);
			c.getPA().sendFrame126("Money Pouch: @gr2@"+BankHandler.insertCommas(""+pouch), 37321);
			BigInteger total = inventory.add(bank.add(pouch.add(equipment)));
			c.getPA().sendFrame126(""+time[0]+" Days, "+time[1]+" Hours,\\n"+time[2]+" Minutes", 37331);
			c.getPA().sendFrame126(""+BankHandler.insertCommas(""+total), 37323);
			c.totalWealth = BankHandler.insertCommas(""+total);
		}
		c.getPA().showInterface(37315);
	}

	public static BigInteger getValue(int[] items, int[] amounts) {
		BigInteger value = new BigInteger("0");
		for (int i = 0; i < items.length; i++) {
			BigInteger f = new BigInteger(""+ItemAssistant.getItemShopValue(items[i])+"");
			BigInteger b = new BigInteger(""+amounts[i]+"");
			value = value.add(new BigInteger(""+f.multiply(b)));
		}
		return value;
	}

	public static void announce(Player c) {
		int[] time = getEvaluatorTime(c);
		if (time[0] == 0 && time[1] == 0 && time[2] == 0) {
			c.sendMessage("You don't have any time remaining on the wealth evaluator.");
		} else {
			c.forcedChat("[EVALUATOR]: My estimated total wealth is "+c.totalWealth);
		}
	}

	public static void addEvaluatorTime(Player c, int days) {
		long time = new Long((long)1000 * 60 * 60 * 24 * days);
		if (c.evaluatorEnd > 0) {
			c.evaluatorEnd += time;
		} else {
			c.evaluatorEnd = System.currentTimeMillis() + time;
		}
	}
	public static int[] getEvaluatorTime(Player c) {
		int days = 0, hours = 0, minutes = 0;
		if (c.evaluatorEnd > System.currentTimeMillis()) {
			long time = c.evaluatorEnd - System.currentTimeMillis();
			if (time >= 86400000) {
				days = ((int)((long)time/86400000));
				time -= ((long)days * 86400000);
			}
			if (time >= 36000000) {
				hours = ((int)(long)time/3600000);
				time -= ((long)hours * 3600000);
			}
			if (time >= 60000) {
				minutes = ((int)time/60000);
			}
		}
		return new int[] { days, hours, minutes };
	}
	
	public static void buyMore(Player c) {
		int[] time = getEvaluatorTime(c);
		if (time[0] >= 365) {
			c.sendMessage("You still have a lot of time left on the wealth evaluator.");
			return;
		}
		String[] options = null;
		if (c.evaluatorTrial) {
			options = new String[] {"Buy 1 day (1m)", "Buy 7 days (5m)", "Buy 30 days (18m)", "Buy 100 days (35m)", "Buy 365 days (100m)"};
		} else {
			options = new String[] {"Claim 3 day trial (FREE)", "Buy 7 days (5m)", "Buy 30 days (18m)", "Buy 100 days (35m)", "Buy 365 days (100m)"};
		}
		Dialogues.send(c, new DialogueList(new Dialogue[] { 
				new Dialogue(DialogueType.OPTIONS, options, new Dialogue.Options() {
					@Override
					public void click(Player c, int option) {
						switch(option) {
						case 1:
							if (!c.evaluatorTrial) {
								c.evaluatorTrial = true;
								addEvaluatorTime(c, 3);
								open(c);
							} else {
								confirmPurchase(c, 1, 1);
							}
							break;
						case 2:
							confirmPurchase(c, 7, 5);
							break;
						case 3:
							confirmPurchase(c, 30, 18);
							break;
						case 4:
							confirmPurchase(c, 100, 35);
							break;
						case 5:
							confirmPurchase(c, 365, 100);
							break;
						}
					}
				})
		}));
	}
	
	public static void confirmPurchase(final Player c, final int days, final int price) {
		final String[] WARNING = new String[] {
				"Are you sure you want to spend "+price+"m coins on "+days+" days",
				"of the wealth evaluator?",
		};
		Dialogues.send(c, new DialogueList(new Dialogue[] {
			new Dialogue(DialogueType.STATEMENT, WARNING),
			new Dialogue(DialogueType.OPTIONS, new String[] {"Yes", "No"}, new Dialogue.Options() {
				@Override
				public void click(Player c, int option) {
					if (option == 1) {
						purchase(c, days, price);
					} else {
						c.getPA().closeAllWindows();
					}
				}
			})
		}));
	}
	
	public static void purchase(final Player c, final int days, final int price) {
		int coins = ItemAssistant.getItemAmount(c, 995);
		if (coins >= (price * 1000000)) {
			c.getPA().closeAllWindows();
			ItemAssistant.deleteItem1(c, 995, price*1000000);
			addEvaluatorTime(c, days);
			c.sendMessage("Transaction complete.");
			open(c);
		} else {
			Dialogue.sendStatement2(c, new String[] {"You must have enough coins in your inventory to complete", "the purchase."});
		}
	}


}
