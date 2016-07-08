package ionic.player.object;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.achievements.AchievementHandler;
import ionic.player.content.combat.dwarfcannon.CannonHandler;
import ionic.player.content.minigames.Barrows;
import ionic.player.content.minigames.BarrowsChest;
import ionic.player.content.minigames.CastleWars;
import ionic.player.content.minigames.PestControl;
import ionic.player.content.miscellaneous.Pickables;
import ionic.player.content.miscellaneous.Teleport;
import ionic.player.content.partyroom.PartyRoom;
import ionic.player.content.skills.agility.AgilityObjectClicking;
import ionic.player.content.skills.construction.Construction;
import ionic.player.content.skills.mining.MiningHandler;
import ionic.player.content.skills.mining.Prospecting;
import ionic.player.content.skills.runecrafting.AbyssHandler;
import ionic.player.content.skills.runecrafting.Runecrafting;
import ionic.player.content.skills.smithing.Smithing;
import ionic.player.content.skills.smithing.SmithingInterface;
import ionic.player.content.skills.woodcutting.Woodcutting;
import ionic.player.dialogue.Dialogues;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import ionic.player.interfaces.InterfaceAssistant;
import ionic.player.object.areas.Wilderness;
import core.Server;

public class ActionHandler
{

        private Client player;

        public ActionHandler(Client Client)
        {
                this.player = Client;
        }

        public void firstClickObject(int objectType, int obX, int obY)
        {
        	player.sendMessage("first click object : Id:"+objectType+" X:"+obX+" Y:"+obY+"");
                /* The object has been reached, so reset. */
                player.clickObjectType = 0;
                
                /* Door handling. */
                Doors.getSingleton().handleDoor(player.objectId, player.objectX, player.objectY, player.heightLevel);

                /* Start of objects handled in their own classes */
                if (Wilderness.isWildernessObject(player, objectType))
                {
                        Wilderness.doWildernessObject(player, objectType);
                        return;
                }
                /* End of objects handled in their own classes */
                
                if (Runecrafting.clickObject(player, objectType)) { return; }
                if (AbyssHandler.clickObject(player, objectType)) { return; }
                Woodcutting.chopTree(player, objectType, obX, obY);
                new AgilityObjectClicking(player, objectType, obX, obY);
                if (Barrows.stairs(player, objectType)) { return; }
                if (Barrows.coffin(player, objectType)) { return; }
                if (MiningHandler.clickRock(player, objectType, obX, obY)) { return; }
                CastleWars.clickObject(player, objectType, obX, obY);

                
                switch (objectType) {
                
                case 6:
                case 7:
                case 8:
                case 9:
                	CannonHandler.firstClickCannon(player, obX, obY);
                	break;
                
                
                case 6552:
                    if (player.playerMagicBook == 0) {
                        player.playerMagicBook = 1;
                        player.setSidebarInterface(6, player.getPA().spellBook[1]);
                        player.getPA().resetAutocast();
                        player.startAnimation(645);
                        player.sendMessage("You have successfully switched your magic book to Ancients Magics.");
                        AchievementHandler.add(player, 17, "easy", 1);
                    } else if (player.playerMagicBook == 1) {
                            player.playerMagicBook = 0;
                            player.setSidebarInterface(6, player.getPA().spellBook[0]);
                            player.getPA().resetAutocast();
                            player.startAnimation(645);
                            player.sendMessage("You have successfully switched your magic book to Normal Magics.");
                    } else if (player.playerMagicBook == 2){
                       player.playerMagicBook = 0;
                            player.setSidebarInterface(6, player.getPA().spellBook[0]);
                            player.getPA().resetAutocast();
                            player.startAnimation(645);
                            player.sendMessage("You have successfully switched your magic book to Normal Magics.");
                    }
                break;
                
                case 9369:
                	if (player.absY >= 5177)
                		player.getPA().movePlayer(2399, 5175, 0);
                	else
                		player.getPA().movePlayer(2399, 5177, 0);
                	break;
                
                case 14296:
                	PestControl.ladders(player, obX, obY);
                	break;
                
                
                case 14315:
                     if (player.absX == 2657 && player.absY == 2639 && player.teleTimer <= 0)
                     PestControl.addToWaitRoom(player);
                     break;
                case 14314:
                     if (player.inPcBoat())
                     PestControl.leaveWaitingBoat(player);
                     player.getPA().resetWalkableInterfaces();
                     break;
                     
                     
                     
                case 9357:
                     player.getPA().resetTzhaar();
                     break;
                case 9356:
                     player.getPA().enterCaves();
                     break;
                     
                     
                     
                     
                case 37929:
                	player.dialogueAction = 0;
                	if (player.absX >= 2915 && player.absY >= 4378 && player.absX <= 2924 && player.absY <= 4390) {
                		if (player.absX < 2920) {
                			Dialogues.send(player, Dialogues.CORPOREAL_BEAST_ENTRANCE);
                		} else {
                			player.getPA().movePlayer(2917, 4385, 0);
                			player.turnPlayerTo(player.absX + 1, player.absY);
                		}
                	} else if (player.absX >= 2968 && player.absY >= 4380 && player.absX <= 2978 && player.absY <= 4391) {
                		if (player.absX < 2973) {
                			player.getPA().movePlayer(2974, 4384, 0);
                			player.turnPlayerTo(player.absX - 1, player.absY);
                		} else {
                			player.dialogueAction = 1;
                			Dialogues.send(player, Dialogues.CORPOREAL_BEAST_ENTRANCE);
                		}
                	}
                	break;
                	

                case 2783:
                	if (ItemAssistant.playerHasItem(player, 2363)) {
            			SmithingInterface.showSmithInterface(player, 2363);
            			return;
            		}
            		else if (ItemAssistant.playerHasItem(player, 2361)) {
            			SmithingInterface.showSmithInterface(player, 2361);
            			return;
            		}
            		else if (ItemAssistant.playerHasItem(player, 2359)) {
            			SmithingInterface.showSmithInterface(player, 2359);
            			return;
            		}
            		else if (ItemAssistant.playerHasItem(player, 2353)) {
            			SmithingInterface.showSmithInterface(player, 2353);
            			return;
            		}
            		else if (ItemAssistant.playerHasItem(player, 2351)) {
            			SmithingInterface.showSmithInterface(player, 2351);
            			return;
            		}
            		else if (ItemAssistant.playerHasItem(player, 2349)) {
            			SmithingInterface.showSmithInterface(player, 2349);
            			return;
            		}
                	break;
                case 13405: // leave house
                     if (player.getHouse() != null)
                         player.getHouse().leave(player);
                         player.getPA().movePlayer(2953, 3224, 0);
                         player.getHouse().save();
                break;
                case 115:
                case 116:
                case 117:
                case 118:
                case 119:
                case 120:
                case 121:
                case 122:
                	PartyRoom.pop(player, obX, obY);
                	break;
                	
                
                case 26193://proom chest
                	player.getIA().refresh();
                	PartyRoom.refreshWaiting(player);
                	PartyRoom.refreshDropping(player);
                	String s = ""+PartyRoom.timeSeconds+"";
                	if (s.length() == 1) { s = "0"+PartyRoom.timeSeconds; }
                	player.getPA().sendFrame126(""+PartyRoom.timeMinutes+":"+s+"", 27403);
                	player.getPA().showInterface(27400);
                	break;
                case 26194://proom lever
                	PartyRoom.pullLever(player);
                	break;
                
                
                case 10284:
                	new BarrowsChest(player);
                	break;
                
                // Signpost
                case 1087:
                    InterfaceAssistant.signPost(player);
                    break;
                
                        /* Start of ladders. */
                case 1747:
                        if (player.ladderEvent)
                        {
                                return;
                        }
                        player.ladderEvent = true;
                        player.startAnimation(828);
                        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
                        {@
                                Override
                                public void execute(CycleEventContainer container)
                                {
                                        container.stop();
                                }@
                                Override
                                public void stop()
                                {
                                        player.getPA().movePlayer(player.absX, player.absY, 1);
                                        player.ladderEvent = false;

                                }
                        }, 1);
                        break;
                case 1746:
                        if (player.ladderEvent)
                        {
                                return;
                        }
                        player.ladderEvent = true;
                        player.startAnimation(828);
                        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
                        {@
                                Override
                                public void execute(CycleEventContainer container)
                                {
                                        container.stop();
                                }@
                                Override
                                public void stop()
                                {
                                        player.getPA().movePlayer(player.absX, player.absY, 0);
                                        player.ladderEvent = false;

                                }
                        }, 1);
                        break;
                        /* End of ladders. */

                        /* Objects at Edgeville */

                        // Highscore statue
                case 563:
                        InterfaceAssistant.clearAchievementInterface(player);
                        break;


                        //Trapdoor, south of Edgeville that leads to Edgeville dungeon
                case 1568:
                        if (player.absX == 3096 || player.absX == 3097)
                        {
                                if (System.currentTimeMillis() - player.objectDelay1 < 2000)
                                {
                                        return;
                                }
                                player.objectDelay1 = System.currentTimeMillis();
                                player.startAnimation(827);

                                CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
                                {@
                                        Override
                                        public void execute(CycleEventContainer container)
                                        {
                                                container.stop();
                                        }@
                                        Override
                                        public void stop()
                                        {
                                                player.getPA().movePlayer(3096, 9867, 0);
                                        }
                                }, 2);

                        }
                        break;


                        /* End of Objects at Edgeville */

                        /* Mage bank objects */

                        //Sparkling pool at magebank that leads to the surface
                case 2879:
                        player.getPA().movePlayer(2538, 4716, 0);
                        break;

                        //Sparkling pool at magebank
                case 2878:
                        player.getPA().movePlayer(2509, 4689, 0);
                        break;

                case 2873:
                        if (obj(2500, 4721))
                        {

                                player.startAnimation(645);
                                player.sendMessage("Saradomin blesses you with a cape.");
                                ItemAssistant.addItem(player, 2412, 1);
                        }
                        break;
                case 2875:
                        if (obj(2507, 4724))
                        {

                                player.startAnimation(645);
                                player.sendMessage("Guthix blesses you with a cape.");
                                ItemAssistant.addItem(player, 2413, 1);
                        }
                        break;

                        /* End of Mage bank objects */

                        /* Dagannoth */

                        // Ladder inside the Dagannoth boss area
                case 10229:
                        player.startAnimation(828);

                        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
                        {@
                                Override
                                public void execute(CycleEventContainer container)
                                {
                                        container.stop();
                                }@
                                Override
                                public void stop()
                                {
                                        player.startAnimation(65535);
                                        player.getPA().movePlayer(1912, 4367, 0);
                                }
                        }, 1);

                        break;

                        // Ladder to enter the Dagannoth boss area
                case 10230:
                        player.startAnimation(827);

                        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
                        {@
                                Override
                                public void execute(CycleEventContainer container)
                                {
                                        container.stop();
                                }@
                                Override
                                public void stop()
                                {
                                        player.getPA().movePlayer(2899, 4449, 0);
                                }
                        }, 2);
                        break;

                        /* End of Dagannoth */

                        // Lever at Kbd
                case 1817:
                        if (player.absX == 2271 && player.absY == 4680)
                        {
                                player.startAnimation(828);
                                Teleport.startTeleport(player, 3067, 10254, 0, "lever");
                        }
                        break;

                        // Bank related objects
                case 2213:
                case 14367:
                case 11758:
                case 4483:
                case 26972:
                        player.getPA().openUpBank(0);
                        handleAchievements(player);
                        break;

                        //Obelisks
                case 14829:
                case 14830:
                case 14827:
                case 14828:
                case 14826:
                case 14831:
                        Server.objectManager.startObelisk(objectType);
                        break;

                        // Viewing orb, in Fight pits
                case 9391:
                        if (System.currentTimeMillis() - player.objectDelay1 < 1000)
                        {
                                return;
                        }
                        player.objectDelay1 = System.currentTimeMillis();
                        player.sendMessage("This does not seem to work...");
                        break;

                        // Ladder in a shed at west Varrock, that leads to Edgeville dungeon, at Hill giants
                case 1754:
                        if (player.objectX == 3116 && player.objectY == 3452)
                        {
                                player.startAnimation(828);

                                CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
                                {@
                                        Override
                                        public void execute(CycleEventContainer container)
                                        {
                                                container.stop();
                                        }@
                                        Override
                                        public void stop()
                                        {
                                                player.startAnimation(65535);
                                                player.getPA().movePlayer(3117, 9852, 0);
                                        }
                                }, 1);

                        }
                        break;

                        /* Edgeville dungeon */


                        // Ladder south of taverly, leads to inside taverly dungeon
                case 1759:
                        if (player.objectX == 2884 && player.objectY == 3397)
                        {
                                if (System.currentTimeMillis() - player.objectDelay1 < 3000)
                                {
                                        return;
                                }
                                player.objectDelay1 = System.currentTimeMillis();
                                player.startAnimation(828);

                                CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
                                {@
                                        Override
                                        public void execute(CycleEventContainer container)
                                        {
                                                container.stop();
                                        }@
                                        Override
                                        public void stop()
                                        {
                                                player.getPA().movePlayer(2884, 9796, 0);
                                        }
                                }, 1);
                        }
                        break;

                default:
                        break;
                }
        }

        private boolean obj(int obX, int obY)
        {
                return player.objectX == obX && player.objectY == obY;
        }

        public void secondClickObject(int objectType, int obX, int obY)
        {

                player.turnPlayerTo(player.objectX, player.objectY);
                player.clickObjectType = 0;
                player.sendMessage("click object 2: "+objectType);
                
                Server.stallThieving.thieveStall(player, objectType, obX, obY);
                
                if (Prospecting.prospect(player, objectType)) { return; }
                
                switch (objectType)
                {
                
                case 6:
                case 7:
                case 8:
                case 9:
                	CannonHandler.secondClickCannon(player, obX, obY);
                	break;
                
                case 11666:
                case 2781:
                    Smithing.sendSmelting(player);
                    break;
                
                case 2646:
                case 1161:
                case 312:
                case 313:
                case 3366:
                case 5583:
        		case 5584:
        		case 5585:
        		case 15508:
        			Pickables.pick(player, player.objectId, player.objectX, player.objectY);
                	break;
                
                case 2213:
                case 14367:
                case 11758:
                case 26972:
                case 27663:
                        player.getPA().openUpBank(0);
                        break;

                default:
                        break;
                }
        }
        public void thirdClickObject(int objectType, int obX, int obY)
        {
                player.turnPlayerTo(player.objectX, player.objectY);
                player.clickObjectType = 0;
                player.sendMessage("third click object : Id:"+objectType+" X:"+obX+" Y:"+obY+"");
                switch (objectType)
                {

                case 10177:
                        // Dagganoth ladder 1st level
                        player.getPA().movePlayer(1798, 4407, 3);
                        break;

                default:
                        break;
                }
        }
        public void fourthClickObject(int objectType, int obX, int obY)
        {
                player.turnPlayerTo(player.objectX, player.objectY);
                player.clickObjectType = 0;
                player.sendMessage("fourth click object : Id:"+objectType+" X:"+obX+" Y:"+obY+"");
                switch (objectType) {

                default:
                        break;
                }
        }
        
        public void fifthClickObject(int objectType, int obX, int obY) {
                player.turnPlayerTo(player.objectX, player.objectY);
                player.clickObjectType = 0;
                player.sendMessage("fifth click object : Id:"+objectType+" X:"+obX+" Y:"+obY+"");
                Construction.handleConstructionClick(player, objectType, obX, obY);
                switch (objectType) {
                default:
                        break;
                }
        }

        public static void handleAchievements(Player c) {
                AchievementHandler.add(c, 16, "easy", 1);
        }

}