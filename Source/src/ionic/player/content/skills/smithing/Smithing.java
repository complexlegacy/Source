package ionic.player.content.skills.smithing;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import core.Constants;

public class Smithing
{

        private static final int[] SMELT_BARS = {
                2349, 2351, 2355, 2353, 2357, 2359, 2361, 2363
        };
        private static final int[] SMELT_FRAME = {
                2405, 2406, 2407, 2409, 2410, 2411, 2412, 2413
        };
        
     
        
        public enum amounts {
        	ONE(new int[] { 15147, 15151, 15155, 15159, 15163, 29017, 29022, 29026 }, 1, 1),
        	FIVE(new int[] { 15146, 15150, 15154, 15158, 15162, 29016, 29020, 29025 }, 2, 5),
        	TEN(new int[] { 10247, 15149, 15153, 15157, 15161, 24253, 29019, 29024 }, 3, 10),
        	ALL(new int[] { 9110, 15148, 15152, 15156, 15160, 16062, 29018, 29023 }, 4, 28),
        	;
        	public int[] buttons;
        	public int amount,slot;
        	private amounts(int[] buttons, int slot, int amount) {
        		this.buttons = buttons;
        		this.amount = amount;
        		this.slot = slot;
        	}
        	public static amounts get(int s) {
        		for (amounts a: amounts.values()) {
        			if (a.slot == s) {
        				return a;
        			}
        		}
        		return null;
        	}
        	public static int[] forID(int button) {
        		for (amounts a: amounts.values()) {
        			for (int i = 0; i < a.buttons.length; i++) {
        				if (a.buttons[i] == button) {
        					return new int[] {a.slot, i};
        				}
        			}
        		}
        		return null;
        	}
        }
        
        
        public enum smelting {
        	BRONZE(0, 2349, 436, 438, 1, 1, 6, 1),
        	IRON(1, 2351, 440, -1, 1, 1, 13, 15),
        	SILVER(2, 2355, 442, -1, 1, 1, 14, 20),
        	STEEL(3, 2353, 440, 453, 1, 2, 18, 30),
        	GOLD(4, 2357, 444, -1, 1, 1, 23, 40),
        	MITHRIL(5, 2359, 447, 453, 1, 2, 30, 50),
        	ADAMANT(6, 2361, 449, 453, 1, 2, 38, 70),
        	RUNE(7, 2363, 451, 453, 1, 2, 50, 85),
        	CANNONBALL(8, 2, 2353, 4, 1, 1, 26, 35),
        	;
        	public int slot, bar, ore, ore2, oreAmount, oreAmount2, exp, lvl;
        	private smelting(int slot, int bar, int ore, int ore2, int oreAmount, int oreAmount2, int exp, int lvl) {
        		this.slot = slot;
        		this.bar = bar;
        		this.ore = ore;
        		this.ore2 = ore2;
        		this.oreAmount = oreAmount;
        		this.oreAmount2 = oreAmount2;
        		this.exp = exp;
        		this.lvl = lvl;
        	}
        	public static smelting forID(int slo) {
        		for (smelting s : smelting.values()) {
        			if (s.slot == slo) {
        				return s;
        			}
        		}
        		return null;
        	}
        }
        
        
        public static boolean smithingButtons(Client c, int button) {
        	int[] ad = amounts.forID(button);
        	if (ad != null) {
	        	amounts a = amounts.get(ad[0]);
	        	smelting s = smelting.forID(ad[1]);
	        	if (a != null && s != null) {
	        		smelt(c, s, a.amount);
	        	}
	        	return true;
        	}
        	return false;
        }
        public static void smelt(Client c, smelting s, int amount) {
        	if (System.currentTimeMillis() - c.lastSmelt < 1000) {
        		return;
        	}
        	c.lastSmelt = System.currentTimeMillis();
        	c.getPA().closeAllWindows();
        	if (check(c, s, true)) {
        		return;
        	}
        	c.smeltAmount = amount;
        	c.isSmithing = true;
        	c.startAnimation(899);
        	CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
        		@Override
        		public void execute(CycleEventContainer e) {
        			if (c.stopSmithing) {
        				e.stop();
        				return;
        			}
        			if (check(c, s, false)) {
        				e.stop();
        				if (s == smelting.CANNONBALL) {
        					c.sendMessage("You have run out of the supplies needed to smelt more cannonballs.");
        				} else {
        					c.sendMessage("You have run out of the supplies needed to smelt more bars.");
        				}
        				return;
        			}
        			c.startAnimation(899);
        			ItemAssistant.deleteItem(c, s.ore, s.oreAmount);
        			if (s.ore2 > 0 && s != smelting.CANNONBALL) {
        				ItemAssistant.deleteItem(c, s.ore2, s.oreAmount2);
        			}
        			c.getPA().addSkillXP(s.exp * Constants.SMITHING_EXPERIENCE, Constants.SMITHING);
        			if (s == smelting.CANNONBALL) {
        				ItemAssistant.addItem(c, s.bar, 4);
        			} else {
        				ItemAssistant.addItem(c, s.bar, 1);
        			}
        			
        			c.smeltAmount --;
        			if (c.smeltAmount == 0) {
        				e.stop();
        			}
        		}
				@Override
				public void stop() {
					c.isSmithing = false;
					c.stopSmithing = false;
				}
        	}, s == smelting.CANNONBALL ? 4 : 2);
        	
        }
        
        
        
        public static boolean check(Client c, smelting s, boolean f) {
        	if (c.skillLevel[Constants.SMITHING] < s.lvl) {
        		if (f) {
        		c.sendMessage("You need a Smithing level of at least "+s.lvl+" to smelt this.");
        		}
        		return true;
        	}
        	if (!ItemAssistant.playerHasItem(c, s.ore, s.oreAmount)) {
        		if (s.ore2 > 0) {
        			if (f) {
        			c.sendMessage("You must have "+s.oreAmount+" "+ItemAssistant.getItemName(s.ore)+""
        					+ " and "+s.oreAmount2+" "+ItemAssistant.getItemName(s.ore2)+" to smelt this.");
        			}
        			return true;
        		} else {
        			if (f) {
        			c.sendMessage("You must have "+s.oreAmount+" "+ItemAssistant.getItemName(s.ore)+""
        					+ " to smelt this.");
        			}
        			return true;
        		}
        	}
        	if (s.ore2 > 0) {
        		if (!ItemAssistant.playerHasItem(c, s.ore2, s.oreAmount2)) {
        			if (f) {
        			c.sendMessage("You must have "+s.oreAmount2+" "+ItemAssistant.getItemName(s.ore2)+" to smelt this.");
        			}
        			return true;
            	}
        	}
        	return false;
        }
        


        public static void sendSmelting(Client c)
        {
                for (int j = 0; j < SMELT_FRAME.length; j++)
                {
                        c.getPA().sendFrame246(SMELT_FRAME[j], 150, SMELT_BARS[j]);
                }
                c.getPA().sendFrame164(2400);
                c.smeltInterface = true;
                
                if (c.isSmithing)
                c.stopSmithing = true;
        }

        public static void readInput(int level, String type, Client player, int amounttomake)
        {

                if (ItemAssistant.getItemName(Integer.parseInt(type)).contains("Bronze"))
                {
                        CheckBronze(player, level, amounttomake, type);
                }
                else if (ItemAssistant.getItemName(Integer.parseInt(type)).contains("Iron"))
                {
                        CheckIron(player, level, amounttomake, type);
                }
                else if (ItemAssistant.getItemName(Integer.parseInt(type)).contains("Steel"))
                {
                        CheckSteel(player, level, amounttomake, type);
                }
                else if (ItemAssistant.getItemName(Integer.parseInt(type)).contains("Mith"))
                {
                        CheckMith(player, level, amounttomake, type);
                }
                else if (ItemAssistant.getItemName(Integer.parseInt(type)).contains("Adam") || ItemAssistant.getItemName(Integer.parseInt(type)).contains("Addy"))
                {
                        CheckAddy(player, level, amounttomake, type);
                }
                else if (ItemAssistant.getItemName(Integer.parseInt(type)).contains("Rune") || ItemAssistant.getItemName(Integer.parseInt(type)).contains("Runite"))
                {
                        CheckRune(player, level, amounttomake, type);
                }
        }

        private static void CheckIron(Client c, int level, int amounttomake, String type)
        {
                c.remove = 2351;

                if (type.equalsIgnoreCase("1349") && level >= 16) //Axe
                {
                        c.xp = 25;
                        c.item = 1349;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equalsIgnoreCase("1203") && level >= 15) //Dagger
                {
                        c.xp = 25;
                        c.item = 1203;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1420") && level >= 17) //Mace
                {
                        c.xp = 25;
                        c.item = 1420;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1137") && level >= 18) //Med helm
                {
                        c.xp = 25;
                        c.item = 1137;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("9140") && level >= 19) //Dart tips
                {
                        c.xp = 25;
                        c.item = 9140;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1279") && level >= 19) //Sword (s)
                {
                        c.xp = 25;
                        c.item = 1277;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("4820") && level >= 19) //Nails
                {
                        c.xp = 25;
                        c.item = 4820;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("40") && level >= 20) //Arrow tips
                {
                        c.xp = 25;
                        c.item = 40;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1323") && level >= 20) //Scim
                {
                        c.xp = 50;
                        c.item = 1323;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1293") && level >= 21) //Longsword
                {
                        c.xp = 50;
                        c.item = 1293;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("863") && level >= 22) //Knives
                {
                        c.xp = 25;
                        c.item = 863;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1153") && level >= 22) //Full Helm
                {
                        c.xp = 50;
                        c.item = 1153;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1175") && level >= 23) //Square shield
                {
                        c.xp = 50;
                        c.item = 1175;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1335") && level >= 24) //Warhammer
                {
                        c.xp = 38;
                        c.item = 1335;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1363") && level >= 25) //Battle axe
                {
                        c.xp = 75;
                        c.item = 1363;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1101") && level >= 26) //Chain
                {
                        c.xp = 75;
                        c.item = 1101;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1191") && level >= 27) //Kite
                {
                        c.xp = 75;
                        c.item = 1191;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1309") && level >= 29) //2h Sword
                {
                        c.xp = 75;
                        c.item = 1309;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1067") && level >= 31) //Platelegs
                {
                        c.xp = 75;
                        c.item = 1067;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1081") && level >= 31) //PlateSkirt
                {
                        c.xp = 75;
                        c.item = 1081;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1115") && level >= 33) //Platebody
                {
                        c.xp = 100;
                        c.item = 1115;
                        c.removeamount = 5;
                        c.maketimes = amounttomake;
                }
                else
                {
                        c.sendMessage("You don't have a high enough level to make this Item!");
                        return;
                }

                doaction(c, c.item, c.remove, c.removeamount, c.maketimes, -1, -1, c.xp);

        }

        private static void CheckSteel(Client c, int level, int amounttomake, String type)
        {
                c.remove = 2353;

                if (type.equalsIgnoreCase("1353") && level >= 31) //Axe
                {
                        c.xp = 38;
                        c.item = 1353;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equalsIgnoreCase("1207") && level >= 30) //Dagger
                {
                        c.xp = 50;
                        c.item = 1207;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1424") && level >= 32) //Mace
                {
                        c.xp = 50;
                        c.item = 1424;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1141") && level >= 33) //Med helm
                {
                        c.xp = 50;
                        c.item = 1141;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("9141") && level >= 34) //Dart tips
                {
                        c.xp = 50;
                        c.item = 9141;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1281") && level >= 34) //Sword (s)
                {
                        c.xp = 50;
                        c.item = 1281;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1539") && level >= 34) //Nails
                {
                        c.xp = 50;
                        c.item = 1539;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("41") && level >= 35) //Arrow tips
                {
                        c.xp = 50;
                        c.item = 41;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1325") && level >= 35) //Scim
                {
                        c.xp = 75;
                        c.item = 1325;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1295") && level >= 36) //Longsword
                {
                        c.xp = 75;
                        c.item = 1295;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("865") && level >= 37) //Knives
                {
                        c.xp = 50;
                        c.item = 865;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1157") && level >= 37) //Full Helm
                {
                        c.xp = 75;
                        c.item = 1157;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1177") && level >= 38) //Square shield
                {
                        c.xp = 75;
                        c.item = 1177;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1339") && level >= 39) //Warhammer
                {
                        c.xp = 113;
                        c.item = 1339;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1365") && level >= 40) //Battle axe
                {
                        c.xp = 113;
                        c.item = 1365;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1105") && level >= 41) //Chain
                {
                        c.xp = 113;
                        c.item = 1105;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1193") && level >= 42) //Kite
                {
                        c.xp = 113;
                        c.item = 1193;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1311") && level >= 44) //2h Sword
                {
                        c.xp = 113;
                        c.item = 1311;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1069") && level >= 46) //Platelegs
                {
                        c.xp = 113;
                        c.item = 1069;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1083") && level >= 46) //PlateSkirt
                {
                        c.xp = 113;
                        c.item = 1083;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1119") && level >= 48) //Platebody
                {
                        c.xp = 188;
                        c.item = 1119;
                        c.removeamount = 5;
                        c.maketimes = amounttomake;
                }
                else
                {
                        c.sendMessage("You don't have a high enough level to make this Item!");
                        return;
                }

                doaction(c, c.item, c.remove, c.removeamount, c.maketimes, -1, -1, c.xp);

        }

        private static void CheckMith(Client c, int level, int amounttomake, String type)
        {
                c.remove = 2359;

                if (type.equalsIgnoreCase("1355") && level >= 51) //Axe
                {
                        c.xp = 50;
                        c.item = 1355;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equalsIgnoreCase("1209") && level >= 50) //Dagger
                {
                        c.xp = 50;
                        c.item = 1209;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1428") && level >= 52) //Mace
                {
                        c.xp = 50;
                        c.item = 1428;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1143") && level >= 53) //Med helm
                {
                        c.xp = 50;
                        c.item = 1143;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("9142") && level >= 54) //Dart tips
                {
                        c.xp = 50;
                        c.item = 9142;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1285") && level >= 54) //Sword (s)
                {
                        c.xp = 50;
                        c.item = 1285;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("4822") && level >= 54) //Nails
                {
                        c.xp = 50;
                        c.item = 4822;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("42") && level >= 55) //Arrow tips
                {
                        c.xp = 50;
                        c.item = 42;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1329") && level >= 55) //Scim
                {
                        c.xp = 100;
                        c.item = 1329;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1299") && level >= 56) //Longsword
                {
                        c.xp = 100;
                        c.item = 1299;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("866") && level >= 57) //Knives
                {
                        c.xp = 50;
                        c.item = 866;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1159") && level >= 57) //Full Helm
                {
                        c.xp = 100;
                        c.item = 1159;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1181") && level >= 58) //Square shield
                {
                        c.xp = 100;
                        c.item = 1181;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1343") && level >= 59) //Warhammer
                {
                        c.xp = 150;
                        c.item = 1343;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1369") && level >= 60) //Battle axe
                {
                        c.xp = 150;
                        c.item = 1369;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1109") && level >= 61) //Chain
                {
                        c.xp = 150;
                        c.item = 1109;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1197") && level >= 62) //Kite
                {
                        c.xp = 150;
                        c.item = 1197;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1315") && level >= 64) //2h Sword
                {
                        c.xp = 150;
                        c.item = 1315;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1071") && level >= 66) //Platelegs
                {
                        c.xp = 150;
                        c.item = 1071;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1085") && level >= 66) //PlateSkirt
                {
                        c.xp = 150;
                        c.item = 1085;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1121") && level >= 68) //Platebody
                {
                        c.xp = 250;
                        c.item = 1121;
                        c.removeamount = 5;
                        c.maketimes = amounttomake;
                }
                else
                {
                        c.sendMessage("You don't have a high enough level to make this Item!");
                        return;
                }

                doaction(c, c.item, c.remove, c.removeamount, c.maketimes, -1, -1, c.xp);


        }

        private static void CheckRune(Client c, int level, int amounttomake, String type)
        {
                c.remove = 2363;

                if (type.equalsIgnoreCase("1359") && level >= 86) //Axe
                {
                        c.xp = 75;
                        c.item = 1359;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equalsIgnoreCase("1213") && level >= 85) //Dagger
                {
                        c.xp = 75;
                        c.item = 1213;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1432") && level >= 87) //Mace
                {
                        c.xp = 75;
                        c.item = 1432;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1147") && level >= 88) //Med helm
                {
                        c.xp = 75;
                        c.item = 1147;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("9144") && level >= 89) //Dart tips
                {

                        c.xp = 75;
                        c.item = 9144;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1289") && level >= 89) //Sword (s)
                {
                        c.xp = 75;
                        c.item = 1289;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("4824") && level >= 89) //Nails
                {
                        c.xp = 75;
                        c.item = 4824;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("44") && level >= 90) //Arrow tips
                {
                        c.xp = 75;
                        c.item = 44;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1333") && level >= 90) //Scim
                {
                        c.xp = 150;
                        c.item = 1333;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1303") && level >= 91) //Longsword
                {
                        c.xp = 150;
                        c.item = 1303;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("868") && level >= 92) //Knives
                {
                        c.xp = 75;
                        c.item = 868;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1163") && level >= 92) //Full Helm
                {
                        c.xp = 150;
                        c.item = 1163;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1185") && level >= 93) //Square shield
                {
                        c.xp = 150;
                        c.item = 1185;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1347") && level >= 94) //Warhammer
                {
                        c.xp = 225;
                        c.item = 1347;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1373") && level >= 95) //Battle axe
                {
                        c.xp = 225;
                        c.item = 1373;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1113") && level >= 96) //Chain
                {
                        c.xp = 225;
                        c.item = 1113;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1201") && level >= 97) //Kite
                {
                        c.xp = 225;
                        c.item = 1201;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1319") && level >= 99) //2h Sword
                {
                        c.xp = 225;
                        c.item = 1319;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1079") && level >= 99) //Platelegs
                {
                        c.xp = 225;
                        c.item = 1079;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1093") && level >= 99) //PlateSkirt
                {
                        c.xp = 225;
                        c.item = 1093;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1127") && level >= 99) //Platebody
                {
                        c.xp = 313;
                        c.item = 1127;
                        c.removeamount = 5;
                        c.maketimes = amounttomake;
                }
                else
                {
                        c.sendMessage("You don't have a high enough level to make this Item!");
                        return;
                }

                doaction(c, c.item, c.remove, c.removeamount, c.maketimes, -1, -1, c.xp);

        }

        private static void CheckAddy(Client c, int level, int amounttomake, String type)
        {
                c.remove = 2361;

                if (type.equalsIgnoreCase("1357") && level >= 71) //Axe
                {
                        c.xp = 63;
                        c.item = 1357;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equalsIgnoreCase("1211") && level >= 70) //Dagger
                {
                        c.xp = 63;
                        c.item = 1211;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1430") && level >= 72) //Mace
                {
                        c.xp = 63;
                        c.item = 1430;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1145") && level >= 73) //Med helm
                {
                        c.xp = 63;
                        c.item = 1145;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("9143") && level >= 74) //Dart tips
                {
                        c.xp = 63;
                        c.item = 9143;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1287") && level >= 74) //Sword (s)
                {
                        c.xp = 63;
                        c.item = 1287;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("4823") && level >= 74) //Nails
                {
                        c.xp = 63;
                        c.item = 4823;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("43") && level >= 75) //Arrow tips
                {
                        c.xp = 63;
                        c.item = 43;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1331") && level >= 75) //Scim
                {
                        c.xp = 125;
                        c.item = 1331;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1301") && level >= 76) //Longsword
                {
                        c.xp = 125;
                        c.item = 1301;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("867") && level >= 77) //Knives
                {
                        c.xp = 63;
                        c.item = 867;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1161") && level >= 77) //Full Helm
                {
                        c.xp = 125;
                        c.item = 1161;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1183") && level >= 78) //Square shield
                {
                        c.xp = 125;
                        c.item = 1183;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1345") && level >= 79) //Warhammer
                {
                        c.xp = 188;
                        c.item = 1345;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1371") && level >= 80) //Battle axe
                {
                        c.xp = 188;
                        c.item = 1371;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1111") && level >= 81) //Chain
                {
                        c.xp = 188;
                        c.item = 1111;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1199") && level >= 82) //Kite
                {
                        c.xp = 188;
                        c.item = 1199;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1317") && level >= 84) //2h Sword
                {
                        c.xp = 188;
                        c.item = 1317;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1073") && level >= 86) //Platelegs
                {
                        c.xp = 188;
                        c.item = 1073;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1091") && level >= 86) //PlateSkirt
                {
                        c.xp = 188;
                        c.item = 1091;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1123") && level >= 88) //Platebody
                {
                        c.xp = 313;
                        c.item = 1123;
                        c.removeamount = 5;
                        c.maketimes = amounttomake;
                }
                else
                {
                        c.sendMessage("You don't have a high enough level to make this Item!");
                        return;
                }

                doaction(c, c.item, c.remove, c.removeamount, c.maketimes, -1, -1, c.xp);

        }

        private static void CheckBronze(Client c, int level, int amounttomake, String type)
        {
                if (type.equalsIgnoreCase("1351") && level >= 1)
                {
                        c.xp = 13;
                        c.item = 1351;
                        c.remove = 2349;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equalsIgnoreCase("1205") && level >= 1)
                {
                        c.xp = 13;
                        c.item = 1205;
                        c.remove = 2349;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1422") && level >= 2)
                {
                        c.xp = 13;
                        c.item = 1422;
                        c.remove = 2349;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1139") && level >= 3)
                {
                        c.xp = 13;
                        c.item = 1139;
                        c.remove = 2349;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("819") && level >= 4)
                {
                        c.xp = 13;
                        c.item = 819;
                        c.remove = 2349;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1277") && level >= 4)
                {
                        c.xp = 13;
                        c.item = 1277;
                        c.remove = 2349;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("4819") && level >= 4)
                {
                        c.xp = 13;
                        c.item = 4819;
                        c.remove = 2349;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("39") && level >= 5)
                {
                        c.xp = 13;
                        c.item = 39;
                        c.remove = 2349;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1321") && level >= 5)
                {
                        c.xp = 25;
                        c.item = 1321;
                        c.remove = 2349;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1291") && level >= 6)
                {
                        c.xp = 25;
                        c.item = 1291;
                        c.remove = 2349;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("864") && level >= 7)
                {
                        c.xp = 25;
                        c.item = 864;
                        c.remove = 2349;
                        c.removeamount = 1;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1155") && level >= 7)
                {
                        c.xp = 25;
                        c.item = 1155;
                        c.remove = 2349;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1173") && level >= 8)
                {
                        c.xp = 25;
                        c.item = 1173;
                        c.remove = 2349;
                        c.removeamount = 2;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1337") && level >= 9)
                {
                        c.xp = 38;
                        c.item = 1337;
                        c.remove = 2349;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1375") && level >= 10)
                {
                        c.xp = 38;
                        c.item = 1375;
                        c.remove = 2349;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1103") && level >= 11)
                {
                        c.xp = 38;
                        c.item = 1103;
                        c.remove = 2349;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }

                else if (type.equals("1189") && level >= 12)
                {
                        c.xp = 38;
                        c.item = 1189;
                        c.remove = 2349;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1307") && level >= 14)
                {
                        c.xp = 38;
                        c.item = 1307;
                        c.remove = 2349;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1075") && level >= 16)
                {
                        c.xp = 38;
                        c.item = 1075;
                        c.remove = 2349;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1087") && level >= 16)
                {
                        c.xp = 38;
                        c.item = 1087;
                        c.remove = 2349;
                        c.removeamount = 3;
                        c.maketimes = amounttomake;
                }
                else if (type.equals("1117") && level >= 18)
                {
                        c.xp = 63;
                        c.item = 1117;
                        c.remove = 2349;
                        c.removeamount = 5;
                        c.maketimes = amounttomake;
                }
                else
                {
                        c.sendMessage("You don't have a high enough level to make this Item!");
                        return;
                }
                doaction(c, c.item, c.remove, c.removeamount, c.maketimes, -1, -1, c.xp);
        }
        
        
        public static boolean doaction(Client player, int toadd, int toremove, int toremove2, int timestomake, int NOTUSED, int NOTUSED2, int xp) {
        	player.smeltAmount = timestomake;
        	player.getPA().closeAllWindows();
        	if (ItemAssistant.playerHasItem(player, toremove, toremove2)) {
        		if (player.smeltAmount > 1 && ItemAssistant.playerHasItem(player, toremove, toremove2 * 2)) {
        			player.startAnimation(898);
        			player.sendMessage("You make some " + ItemAssistant.getItemName(toadd) + "s");
        		} else {
        			player.sendMessage("You make a " + ItemAssistant.getItemName(toadd));
        		}
        		player.isSmithing = true;
        		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer e) {
						if (player.stopSmithing) {
							e.stop();
							return;
						}
						player.startAnimation(898);
						if (ItemAssistant.playerHasItem(player, toremove, toremove2)) {
		        			ItemAssistant.deleteItem2(player, toremove, toremove2);
		        			if (ItemAssistant.getItemName(toadd).contains("bolt")) {
		        				ItemAssistant.addItem(player, toadd, 10);
		        			} else if (ItemAssistant.getItemName(toadd).contains("nail")) {
		        				ItemAssistant.addItem(player, toadd, 15);
		        			} else if (ItemAssistant.getItemName(toadd).contains("arrow")) {
		        				ItemAssistant.addItem(player, toadd, 15);
		        			} else if (ItemAssistant.getItemName(toadd).contains("knife")) {
		        				ItemAssistant.addItem(player, toadd, 5);
		        			} else if (ItemAssistant.getItemName(toadd).contains("cannon")) {
		        				ItemAssistant.addItem(player, toadd, 4);
		        			} else {
		        				ItemAssistant.addItem(player, toadd, 1);
		        			}
		        			player.getPA().addSkillXP(xp * Constants.SMITHING_EXPERIENCE, 13);
		        			player.getPA().refreshSkill(13);
		        			player.smeltAmount--;
		        			if (player.smeltAmount == 0) {
		        				e.stop();
		        			}
		        		} else {
		        			e.stop();
		        		}
					}
					@Override
					public void stop() {
						player.isSmithing = false;
						player.stopSmithing = false;
					}
        		}, 2);
        	} else {
        		player.sendMessage("You don't have enough bars to make this item!");
        		return false;
        	}
        	return true;
        }
}