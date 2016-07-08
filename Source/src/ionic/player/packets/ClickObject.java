package ionic.player.packets;

import ionic.player.Client;
import ionic.player.content.skills.farming.FarmingConstants;
import ionic.player.movement.Movement;
import ionic.player.object.ObjectEvent;
import utility.Misc;
import core.Configuration;

/**
 * Click Object
 */

public class ClickObject implements PacketType {

    public static final int FIRST_CLICK = 132, SECOND_CLICK = 252,
            THIRD_CLICK = 70, FOURTH_CLICK = 234, FIFTH_CLICK = 228;

    @Override
    public void processPacket(final Client player, int packetType, int packetSize) {
        if (player.doingAction(false) || player.getDoingAgility() || player.isTeleporting() || player.barrowsDoor) {
            return;
        }
        if (player.isChopping) {
            player.startAnimation(65535);
            player.isChopping = false;
        }
        if (player.isCrafting) {
            player.isCrafting = false;
        }

        if (player.isSmithing) {
            player.stopSmithing = true;
        }
        if (player.isHerblore) {
            player.stopHerblore = true;
        }

        player.clickObjectType = player.objectX = player.objectId = player.objectY = 0;
        player.objectYOffset = player.objectXOffset = 0;
        player.faceUpdate(0);
        player.usingMagic = false;
        player.npcIndex = 0;
        player.playerIndex = 0;
        player.getPA().resetFollow();

        switch (packetType) {

            case FIRST_CLICK:
                player.objectX = player.getInStream().readSignedWordBigEndianA();
                player.objectId = player.getInStream().readUnsignedWord();
                player.objectY = player.getInStream().readUnsignedWordA();
                player.objectDistance = 1;


                if (Configuration.DEBUG) {
                    Misc.println("FIRST CLICK Object [ID: " + player.objectId + "] [Object X: " + player.objectX + "] [Object Y: " + player.objectY + "] [Player X from object: " + (player.getX() - player.objectX) + "] [Player Y from object: " + (player.getY() - player.objectY) + "]");
                }

                if (Math.abs(player.getX() - player.objectX) > 25 || Math.abs(player.getY() - player.objectY) > 25) {
                    Movement.resetWalkingQueue(player);
                    break;
                }

                switch (player.objectId) {
                
                case 4388://zamorak portal
                	player.objectDistance = 3;
                	break;
                case 4408://guthix portal
                	player.objectDistance = 3;
                	break;
                case 4387://saradomin portal
                	player.objectDistance = 3;
                	break;
                	
                case 6:
                	player.objectDistance = 3;
                	player.objectXOffset = 1;
                	player.objectYOffset = 1;
                	break;
                
                case 37929:
                	player.objectDistance = 4;
                	break;

                    case 2478://altars for runecrafting
                    case 2479:
                    case 2480:
                    case 2481:
                    case 2482:
                    case 2483:
                    case 2484:
                    case 2485:
                    case 2486:
                    case 2487:
                    case 2488:
                    case 2489:
                    case 2490:
                        player.objectDistance = 3;
                        break;

                    case 8966: // Dagganoth stairs
                    case 10595: // Dagganoth stairs
                    case 10596: // Dagganoth stairs
                    case 2287: // Obstacle pipe
                        player.objectDistance = 2;
                        break;
                    case 8929: // Dagganoth entrance
                        player.objectDistance = 4;
                        break;
                    case 2283:
                    case 2282: // Ropeswing
                        player.objectDistance = 8;
                        break;

                    case 4058:
                        player.objectDistance = 2;
                        break;
                    case 154:
                        player.objectDistance = 2;
                        break;

                    case 2294: // Log balance.
                    case 1948: // First crumbling wall.
                        player.objectDistance = 5;
                        break;
                    case 1733:
                        player.objectYOffset = 2;
                        break;

                    case 5100: // Pipe
                        player.objectDistance = 2;
                        break;

                    case 23271:
                        player.objectDistance = 3;
                        break;
                    case 8930:
                        // Snowy dagganoth cave
                        player.objectDistance = 3;
                        break;
                    case 245:
                        player.objectYOffset = -1;
                        player.objectDistance = 0;
                        break;
                    case 272:
                        player.objectYOffset = 1;
                        player.objectDistance = 0;
                        break;
                    case 273:
                        player.objectYOffset = 1;
                        player.objectDistance = 0;
                        break;
                    case 246:
                        player.objectYOffset = 1;
                        player.objectDistance = 0;
                        break;
                    case 4493:
                    case 4494:
                    case 4496:
                    case 4495:
                        player.objectDistance = 5;
                        break;
                    case 10229:
                    case 6522:
                        player.objectDistance = 2;
                        break;
                    case 8959:
                        player.objectYOffset = 1;
                        break;
                    case 4420:
                        if (player.getX() >= 2383 && player.getX() <= 2385) {
                            player.objectYOffset = 1;
                        } else {
                            player.objectYOffset = -2;
                        }
                    case 6552:
                    case 409:
                        player.objectDistance = 2;
                        break;
                    case 2879:
                    case 2878:
                        player.objectDistance = 3;
                        break;
                    case 2558:
                        player.objectDistance = 0;
                        if (player.absX > player.objectX && player.objectX == 3044) player.objectXOffset = 1;
                        if (player.absY > player.objectY) player.objectYOffset = 1;
                        if (player.absX < player.objectX && player.objectX == 3038) player.objectXOffset = -1;
                        break;
                    case 9356:
                        player.objectDistance = 2;
                        break;
                    case 5959:
                    case 1815:
                    case 5960:
                    case 1816:
                        player.objectDistance = 0;
                        break;

                    case 9293:
                        player.objectDistance = 2;
                        break;
                    case 4418:
                        if (player.objectX == 2374 && player.objectY == 3131) player.objectYOffset = -2;
                        else if (player.objectX == 2369 && player.objectY == 3126) player.objectXOffset = 2;
                        else if (player.objectX == 2380 && player.objectY == 3127) player.objectYOffset = 2;
                        else if (player.objectX == 2369 && player.objectY == 3126) player.objectXOffset = 2;
                        else if (player.objectX == 2374 && player.objectY == 3131) player.objectYOffset = -2;
                        break;
                    case 9706:
                        player.objectDistance = 0;
                        player.objectXOffset = 1;
                        break;
                    case 9707:
                        player.objectDistance = 0;
                        player.objectYOffset = -1;
                        break;
                    case 4419:
                    case 6707:
                        // verac
                        player.objectYOffset = 3;
                        break;
                    case 6823:
                        player.objectDistance = 2;
                        player.objectYOffset = 1;
                        break;

                    case 6706:
                        // torag
                        player.objectXOffset = 2;
                        break;
                    case 6772:
                        player.objectDistance = 2;
                        player.objectYOffset = 1;
                        break;

                    case 6705:
                        // karils
                        player.objectYOffset = -1;
                        break;
                    case 6822:
                        player.objectDistance = 2;
                        player.objectYOffset = 1;
                        break;

                    case 6704:
                        // guthan stairs
                        player.objectYOffset = -1;
                        break;
                    case 6773:
                        player.objectDistance = 2;
                        player.objectXOffset = 1;
                        player.objectYOffset = 1;
                        break;

                    case 6703:
                        // dharok stairs
                        player.objectXOffset = -1;
                        break;
                    case 6771:
                        player.objectDistance = 2;
                        player.objectXOffset = 1;
                        player.objectYOffset = 1;
                        break;

                    case 6702:
                        // ahrim stairs
                        player.objectXOffset = -1;
                        break;
                    case 6821:
                        player.objectDistance = 2;
                        player.objectXOffset = 1;
                        player.objectYOffset = 1;
                        break;
                    case 1276:
                    case 1278:
                        // Trees
                    case 1281:
                        // Oak tree
                    case 1308:
                        //willow
                    case 1307:
                        //maple
                    case 1309:
                        // Yew tree
                        player.objectDistance = 3;
                        break;

                    case 5099:
                        player.objectDistance = 2;
                        break;
                    default:
                        player.objectDistance = 1;
                        player.objectXOffset = 0;
                        player.objectYOffset = 0;
                        break;
                }
                if (player.goodDistance(player.objectX + player.objectXOffset, player.objectY + player.objectYOffset, player.getX(), player.getY(), player.objectDistance)) {
                    player.closeAgility = true;
                    player.getActions().firstClickObject(player.objectId, player.objectX, player.objectY);
                } else {
                    player.closeAgility = false;
                    player.clickObjectType = 1;
                    ObjectEvent.clickObjectType1Event(player);
                }
                break;

            case SECOND_CLICK:
                player.objectId = player.getInStream().readUnsignedWordBigEndianA();
                player.objectY = player.getInStream().readSignedWordBigEndian();
                player.objectX = player.getInStream().readUnsignedWordA();
                player.objectDistance = 1;

                if (Configuration.DEBUG) {
                    Misc.println("SECOND CLICK Object [ID: " + player.objectId + "] [Object X: " + player.objectX + "] [Object Y: " + player.objectY + "] [Player X from object: " + (player.getX() - player.objectX) + "] [Player Y from object: " + (player.getY() - player.objectY) + "]");
                }

                switch (player.objectId) {
                
                case 6:
                	player.objectDistance = 3;
                	player.objectXOffset = 1;
                	player.objectYOffset = 1;
                	break;

                    case 6163:
                    case 6165:
                    case 6166:
                    case 6164:
                    case 6162:
                        player.objectDistance = 2;
                        break;
                    default:
                        player.objectDistance = 1;
                        player.objectXOffset = 0;
                        player.objectYOffset = 0;
                        break;

                }
                if (player.goodDistance(player.objectX + player.objectXOffset, player.objectY + player.objectYOffset, player.getX(), player.getY(), player.objectDistance)) {
                    player.getActions().secondClickObject(player.objectId, player.objectX, player.objectY);
                } else {
                    player.clickObjectType = 2;
                    ObjectEvent.clickObjectType2Event(player);
                }
                break;

            case THIRD_CLICK:
                player.objectX = player.getInStream().readSignedWordBigEndian();
                player.objectY = player.getInStream().readUnsignedWord();
                player.objectId = player.getInStream().readUnsignedWordBigEndianA();

                if (Configuration.DEBUG) {
                    Misc.println("THIRD CLICK Object [ID: " + player.objectId + "] [Object X: " + player.objectX + "] [Object Y: " + player.objectY + "] [Player X from object: " + (player.getX() - player.objectX) + "] [Player Y from object: " + (player.getY() - player.objectY) + "]");
                }

                switch (player.objectId) {
                    default:
                        player.objectDistance = 1;
                        player.objectXOffset = 0;
                        player.objectYOffset = 0;
                        break;
                }
                if (player.goodDistance(player.objectX + player.objectXOffset, player.objectY + player.objectYOffset, player.getX(), player.getY(), player.objectDistance)) {
                    player.getActions().thirdClickObject(player.objectId, player.objectX, player.objectY);
                } else {
                    player.clickObjectType = 3;
                    ObjectEvent.clickObjectType3Event(player);
                }
                break;


            case FOURTH_CLICK:
                player.objectId = player.getInStream().readUnsignedWordA();
                player.objectY = player.getInStream().readUnsignedWordA();
                player.objectX = player.getInStream().readUnsignedWord();
                player.sendMessage("fourth click " + player.objectId + "");
                if (player.goodDistance(player.objectX + player.objectXOffset, player.objectY + player.objectYOffset, player.getX(), player.getY(), player.objectDistance)) {
                    player.getActions().fourthClickObject(player.objectId, player.objectX, player.objectY);
                } else {
                    player.clickObjectType = 4;
                    ObjectEvent.clickObjectType4Event(player);
                }
                break;

            case FIFTH_CLICK:
                player.objectId = player.getInStream().readUnsignedWordA();
                player.objectY = player.getInStream().readUnsignedWordA();
                player.objectX = player.getInStream().readUnsignedWord();
                player.sendMessage("fifth click " + player.objectId + "");
                player.objectDistance = 1;
                if (player.goodDistance(player.objectX + player.objectXOffset, player.objectY + player.objectYOffset, player.getX(), player.getY(), player.objectDistance)) {
                    player.getActions().fifthClickObject(player.objectId, player.objectX, player.objectY);
                } else {
                    player.clickObjectType = 5;
                    ObjectEvent.clickObjectType5Event(player);
                }
                break;
        }


    }

}