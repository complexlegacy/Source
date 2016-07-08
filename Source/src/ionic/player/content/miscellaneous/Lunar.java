package ionic.player.content.miscellaneous;


import ionic.item.ItemAssistant;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import ionic.player.movement.Movement;
import core.Constants;
import utility.Misc;

public class Lunar {

    private static final int astral = 9075, fire = 554, water = 555, air = 556,
            earth = 557, body = 559, death = 560, nats = 561, law = 563,
            cosmic = 564, blood = 565;

    public static void magicOnItems(final Player player, int slot, int itemId,
                                    int spellId) {
        if (System.currentTimeMillis() - player.lunarDelay < 2000)
            return;
        switch (spellId) {
            case 30017:
                if (player.skillLevel[6] < 65) {
                    player.sendMessage("You do not have the required magic level to use this spell.");
                    return;
                }
                if (!ItemAssistant.playerHasItem(player, astral)
                        || !ItemAssistant.playerHasItem(player, fire, 5)
                        || !ItemAssistant.playerHasItem(player, water, 4)) {
                    player.sendMessage("You do not have the required runes to cast this spell.");
                    return;
                }
                if (itemId == 2317 || itemId == 2319 || itemId == 2321) {
                    //c.setAnimation(Animation.create(4413));
                    player.startAnimation(4413);
                    player.gfx100(746);
                    ItemAssistant.deleteItem(player, itemId, 1);
                    ItemAssistant.deleteItem(player, astral,
                            ItemAssistant.getItemSlot(player, astral), 1);
                    ItemAssistant
                            .deleteItem(player, fire, ItemAssistant.getItemSlot(player, fire), 5);
                    ItemAssistant.deleteItem(player, water, ItemAssistant.getItemSlot(player, water),
                            4);
                    player.sendMessage("You bake the pie");
                    player.getPA().addSkillXP(1024, 6);
                    player.getPA().refreshSkill(6);
                    player.lunarDelay = System.currentTimeMillis();
                    if (itemId == 2317)
                        ItemAssistant.addItem(player, 2323, 1);
                    else if (itemId == 2319)
                        ItemAssistant.addItem(player, 2327, 1);
                    else if (itemId == 2321)
                        ItemAssistant.addItem(player, 2325, 1);
                } else
                    player.sendMessage("This spell only works on an uncooked pie!");
                break;

            case 30154:
                if (player.skillLevel[6] < 77)
                    return;
                if (!ItemAssistant.playerHasItem(player, astral, 2)
                        || !ItemAssistant.playerHasItem(player, fire, 6)
                        || !ItemAssistant.playerHasItem(player, air, 10)) {
                    player.sendMessage("You do not have the required runes to cast this spell.");
                    return;
                }
                if (itemId != 1783) {
                    player.sendMessage("You can only use this spell on a bucket of sand!");
                    return;
                }
                //c.setAnimation(Animation.create(4412));
                player.startAnimation(4412);
                player.gfx0(729);
                ItemAssistant
                        .deleteItem(player, itemId, ItemAssistant.getItemSlot(player, itemId), 1);
                ItemAssistant
                        .deleteItem(player, astral, ItemAssistant.getItemSlot(player, astral), 2);
                ItemAssistant.deleteItem(player, fire, ItemAssistant.getItemSlot(player, fire), 6);
                ItemAssistant.deleteItem(player, air, ItemAssistant.getItemSlot(player, air), 10);
                ItemAssistant.addItem(player, 1775, 1);
                player.lunarDelay = System.currentTimeMillis();
                player.getPA().addSkillXP(1600, 6);
                player.getPA().refreshSkill(6);
                break;
        } // closes switch
    } // closes magiconitems

    public static void Button(final Player player, int actionButtonId) {
        if (System.currentTimeMillis() - player.lunarDelay < 2000)
            return;
        switch (actionButtonId) {
            case 117104:
                if (player.skillLevel[6] < 68) {
                    player.sendMessage("You do not have the required magic level to use this spell.");
                    return;
                }
                if (!ItemAssistant.playerHasItem(player, astral)
                        || !ItemAssistant.playerHasItem(player, water, 3)
                        || !ItemAssistant.playerHasItem(player, fire)) {
                    player.sendMessage("You do not have the required runes to cast this spell.");
                    return;
                }
                if (ItemAssistant.playerHasItem(player, 229)) {
                    //c.setAnimation(Animation.create(6294));
                    player.startAnimation(6294);
                    player.gfx0(1061);
                    ItemAssistant.deleteItem(player, astral,
                            ItemAssistant.getItemSlot(player, astral), 1);
                    ItemAssistant
                            .deleteItem(player, fire, ItemAssistant.getItemSlot(player, fire), 1);
                    ItemAssistant.deleteItem(player, water, ItemAssistant.getItemSlot(player, water),
                            3);
                    ItemAssistant.deleteItem(player, 229, 1);
                    ItemAssistant.addItem(player, 227, 1);
                    player.getPA().addSkillXP(200, 6);
                    player.getPA().refreshSkill(6);
                    player.lunarDelay = System.currentTimeMillis();
                } else
                    player.sendMessage("You have run out of empty vials.");
                break;

            case 117147:
                if (player.skillLevel[6] < 71) {
                    player.sendMessage("You do not have the required magic level to use this spell.");
                    return;
                }
                if (!ItemAssistant.playerHasItem(player, astral, 2)
                        || !ItemAssistant.playerHasItem(player, earth, 2)) {
                    player.sendMessage("You do not have the required runes to cast this spell.");
                    return;
                }
                //c.setAnimation(Animation.create(6303));
                player.startAnimation(6303);
                player.gfx0(1074);
                ItemAssistant
                        .deleteItem(player, astral, ItemAssistant.getItemSlot(player, astral), 1);
                ItemAssistant.deleteItem(player, earth, ItemAssistant.getItemSlot(player, earth), 1);
                ItemAssistant.addItem(player, 11159, 1);
                player.sendMessage("You get a hunter kit.");
                player.getPA().addSkillXP(700, 6);
                player.getPA().refreshSkill(6);
                player.lunarDelay = System.currentTimeMillis();
                break;

            case 117170:
                if (player.skillLevel[6] < 74) {
                    player.sendMessage("You do not have the required magic level to use this spell.");
                    return;
                }
                if (!ItemAssistant.playerHasItem(player, astral, 2)
                        || !ItemAssistant.playerHasItem(player, cosmic, 2)
                        || !ItemAssistant.playerHasItem(player, law, 2)) {
                    player.sendMessage("You do not have the required runes to cast this spell.");
                    return;
                }
                ItemAssistant
                        .deleteItem(player, astral, ItemAssistant.getItemSlot(player, astral), 2);
                ItemAssistant
                        .deleteItem(player, cosmic, ItemAssistant.getItemSlot(player, cosmic), 2);
                ItemAssistant.deleteItem(player, law, ItemAssistant.getItemSlot(player, law), 2);
                cureAll(player);
                player.getPA().addSkillXP(1400, 6); // get real xp amount
                player.getPA().refreshSkill(6);
                player.lunarDelay = System.currentTimeMillis();
                break;

            case 118106:
                if (player.skillLevel[6] < 95) {
                    player.sendMessage("You do not have the required magic level to use this spell.");
                    return;
                }
                if (!ItemAssistant.playerHasItem(player, astral, 4)
                        || !ItemAssistant.playerHasItem(player, blood, 3)
                        || !ItemAssistant.playerHasItem(player, law, 6)) {
                    player.sendMessage("You do not have the required runes to cast this spell.");
                    return;
                }
                ItemAssistant
                        .deleteItem(player, astral, ItemAssistant.getItemSlot(player, astral), 4);
                ItemAssistant.deleteItem(player, blood, ItemAssistant.getItemSlot(player, blood), 3);
                ItemAssistant.deleteItem(player, law, ItemAssistant.getItemSlot(player, law), 6);
                HealAll(player);
                player.lunarDelay = System.currentTimeMillis();
                break;

            case 117139:
                if (player.skillLevel[6] < 71) {
                    player.sendMessage("You do not have the required magic level to use this spell.");
                    return;
                }
                if (!ItemAssistant.playerHasItem(player, astral, 2)
                        || !ItemAssistant.playerHasItem(player, cosmic, 2)
                        || !ItemAssistant.playerHasItem(player, law)) {
                    player.sendMessage("You do not have the required runes to cast this spell.");
                    return;
                }
                ItemAssistant
                        .deleteItem(player, astral, ItemAssistant.getItemSlot(player, astral), 2);
                ItemAssistant
                        .deleteItem(player, cosmic, ItemAssistant.getItemSlot(player, cosmic), 2);
                ItemAssistant.deleteItem(player, law, ItemAssistant.getItemSlot(player, law), 1);
                player.poisonRunOut = 0;
                //c.setAnimation(Animation.create(4411));
                player.startAnimation(4411);
                //c.setGraphic(Graphic.highGraphic(748));
                player.getPA().addSkillXP(500, 6);
                player.getPA().refreshSkill(6);
                player.lunarDelay = System.currentTimeMillis();
                break;

            case 118034:
                if (player.skillLevel[6] < 86) {
                    player.sendMessage("You do not have the required magic level to use this spell.");
                    return;
                }
                if (!ItemAssistant.playerHasItem(player, astral, 3)
                        || !ItemAssistant.playerHasItem(player, earth, 15)
                        || !ItemAssistant.playerHasItem(player, nats, 1)) {
                    player.sendMessage("You do not have the required runes to cast this spell.");
                    return;
                }
                if (!ItemAssistant.playerHasItem(player, 1511)) {
                    player.sendMessage("You need to have logs in your inventory to use this spell.");
                    return;
                }
                ItemAssistant
                        .deleteItem(player, astral, ItemAssistant.getItemSlot(player, astral), 3);
                ItemAssistant.deleteItem(player, earth, ItemAssistant.getItemSlot(player, earth), 15);
                ItemAssistant.deleteItem(player, nats, ItemAssistant.getItemSlot(player, nats), 1);
                ItemAssistant.deleteItem(player, 1511, 1);
                ItemAssistant.addItem(player, 960, 1);
                //c.setAnimation(Animation.create(6298));
                player.startAnimation(6298);
                player.gfx0(1063);
                player.getPA().addSkillXP(400, 6);
                player.getPA().refreshSkill(6);
                player.lunarDelay = System.currentTimeMillis();
                break;

            case 118010: // boost stats - add later with events
                //c.sendMessage("Add this yourself or wait until I have time to do it.");
                break;

            case 117242: // boost other stats - add later with events
                //c.sendMessage("Add this yourself or wait until I have time to do it.");
                break;

            case 118114:
                if (player.skillLevel[6] < 96) {
                    player.sendMessage("You do not have the required magic level to use this spell.");
                    return;
                }
                if (!ItemAssistant.playerHasItem(player, astral, 3)
                        || !ItemAssistant.playerHasItem(player, cosmic, 2)
                        || !ItemAssistant.playerHasItem(player, law)) {
                    player.sendMessage("You do not have the required runes to cast this spell.");
                    return;
                }
                //c.setAnimation(Animation.create(6299));
                player.startAnimation(6299);
                player.gfx0(1062);
                ItemAssistant
                        .deleteItem(player, astral, ItemAssistant.getItemSlot(player, astral), 3);
                ItemAssistant
                        .deleteItem(player, cosmic, ItemAssistant.getItemSlot(player, cosmic), 2);
                ItemAssistant.deleteItem(player, law, ItemAssistant.getItemSlot(player, law), 1);
                player.getPA().addSkillXP(1000, 6);
                player.getPA().refreshSkill(6);
                // c.getDH().sendDialogues(1682, c.npcType); //add your own way of
                // switching mage books
                player.lunarDelay = System.currentTimeMillis();
                break;

            case 117226:
                if (player.skillLevel[6] < 79) {
                    player.sendMessage("You do not have the required magic level to use this spell.");
                    return;
                }
                if (!ItemAssistant.playerHasItem(player, astral, 2)
                        || !ItemAssistant.playerHasItem(player, cosmic)
                        || !ItemAssistant.playerHasItem(player, body, 5)) {
                    player.sendMessage("You do not have the required runes to cast this spell.");
                    return;
                }
                if (player.indream) {
                    player.sendMessage("You already in deep sleep");
                    return;
                }
                ItemAssistant
                        .deleteItem(player, astral, ItemAssistant.getItemSlot(player, astral), 2);
                ItemAssistant
                        .deleteItem(player, cosmic, ItemAssistant.getItemSlot(player, cosmic), 1);
                ItemAssistant.deleteItem(player, body, ItemAssistant.getItemSlot(player, body), 5);
                player.getPA().addSkillXP(1000, 6);
                player.getPA().refreshSkill(6);
                player.getPA().resetFollow();
                Dream(player);
                player.dream = 5;
                player.lunarDelay = System.currentTimeMillis();
                break;
        }
    }

    public static void CastingLunarOnPlayer(final Player player, int castingSpellId) {
        final Player castOnPlayer = PlayerHandler.players[player.playerIndex];
        Movement.stopMovement(player);;
        player.getCombat().resetPlayerAttack();
        switch (castingSpellId) {
            case 30130:
                if (player.skillLevel[6] < 75) {
                    player.sendMessage("You do not have the required magic level to use this spell.");
                    return;
                }
                if (!ItemAssistant.playerHasItem(player, astral, 2)
                        || !ItemAssistant.playerHasItem(player, cosmic, 2)
                        || ItemAssistant.playerHasItem(player, body, 5)) {
                    player.sendMessage("You do not have the required runes to cast this spell.");
                    return;
                }
                ItemAssistant
                        .deleteItem(player, astral, ItemAssistant.getItemSlot(player, astral), 2);
                ItemAssistant
                        .deleteItem(player, cosmic, ItemAssistant.getItemSlot(player, cosmic), 2);
                ItemAssistant.deleteItem(player, body, ItemAssistant.getItemSlot(player, body), 5);
                //c.setAnimation(Animation.create(6293));
                player.startAnimation(6293);
                player.gfx0(1060);
                player.getPA()
                        .sendFrame126(
                                ""
                                        + castOnPlayer.playerName
                                        + "'s Attack Level: "
                                        + castOnPlayer.skillLevel[0]
                                        + "/"
                                        + castOnPlayer.getLevelForXP(castOnPlayer.playerXP[0])
                                        + "", 8147);
                player.getPA()
                        .sendFrame126(
                                ""
                                        + castOnPlayer.playerName
                                        + "'s Strength Level: "
                                        + castOnPlayer.skillLevel[2]
                                        + "/"
                                        + castOnPlayer.getLevelForXP(castOnPlayer.playerXP[2])
                                        + "", 8148);
                player.getPA()
                        .sendFrame126(
                                ""
                                        + castOnPlayer.playerName
                                        + "'s Defence Level: "
                                        + castOnPlayer.skillLevel[1]
                                        + "/"
                                        + castOnPlayer.getLevelForXP(castOnPlayer.playerXP[1])
                                        + "", 8149);
                player.getPA()
                        .sendFrame126(
                                ""
                                        + castOnPlayer.playerName
                                        + "'s Hitpoints Level: "
                                        + castOnPlayer.skillLevel[3]
                                        + "/"
                                        + castOnPlayer.getLevelForXP(castOnPlayer.playerXP[3])
                                        + "", 8150);
                player.getPA()
                        .sendFrame126(
                                ""
                                        + castOnPlayer.playerName
                                        + "'s Range Level: "
                                        + castOnPlayer.skillLevel[4]
                                        + "/"
                                        + castOnPlayer.getLevelForXP(castOnPlayer.playerXP[4])
                                        + "", 8151);
                player.getPA()
                        .sendFrame126(
                                ""
                                        + castOnPlayer.playerName
                                        + "'s Prayer Level: "
                                        + castOnPlayer.skillLevel[5]
                                        + "/"
                                        + castOnPlayer.getLevelForXP(castOnPlayer.playerXP[5])
                                        + "", 8152);
                player.getPA()
                        .sendFrame126(
                                ""
                                        + castOnPlayer.playerName
                                        + "'s Magic Level: "
                                        + castOnPlayer.skillLevel[6]
                                        + "/"
                                        + castOnPlayer.getLevelForXP(castOnPlayer.playerXP[6])
                                        + "", 8153);
                player.getPA().showInterface(8134);
                castOnPlayer.gfx0(736);
                break;

            case 30298:
                if (player.skillLevel[6] < 93) {
                    player.sendMessage("You do not have the required magic level to use this spell.");
                    return;
                }
                if (!ItemAssistant.playerHasItem(player, 557, 10)
                        || !ItemAssistant.playerHasItem(player, 9075, 3)
                        || !ItemAssistant.playerHasItem(player, 560, 2)) {
                    player.sendMessage("You do not have the required runes to cast this spell.");
                    return;
                }
                if (System.currentTimeMillis() - player.lastVeng < 30000) {
                    player.sendMessage("You can only cast vengeance every 30 seconds.");
                    return;
                }
                ItemAssistant
                        .deleteItem(player, astral, ItemAssistant.getItemSlot(player, astral), 3);
                ItemAssistant.deleteItem(player, death, ItemAssistant.getItemSlot(player, death), 2);
                ItemAssistant
                        .deleteItem(player, earth, ItemAssistant.getItemSlot(player, astral), 10);
                castOnPlayer.vengOn = true;
                player.lastVeng = System.currentTimeMillis();
                castOnPlayer.gfx100(725);
                player.getPA().addSkillXP(1240, 6);
                player.getPA().refreshSkill(6);
                //c.setAnimation(Animation.create(4411));
                player.startAnimation(4411);
                break;

            case 30048:
                if (player.skillLevel[6] < 68) {
                    player.sendMessage("You do not have the required magic level to use this spell.");
                    return;
                }
                if (!ItemAssistant.playerHasItem(player, earth, 10)
                        || !ItemAssistant.playerHasItem(player, astral)
                        || !ItemAssistant.playerHasItem(player, law)) {
                    player.sendMessage("You do not have the required runes to cast this spell.");
                    return;
                }
                if (castOnPlayer.poisonRunOut < 0) {
                    player.sendMessage("This player is not poisoned.");
                    return;
                }
                ItemAssistant
                        .deleteItem(player, astral, ItemAssistant.getItemSlot(player, astral), 1);
                ItemAssistant.deleteItem(player, law, ItemAssistant.getItemSlot(player, law), 1);
                ItemAssistant.deleteItem(player, earth, ItemAssistant.getItemSlot(player, earth), 10);
                player.poisonRunOut = 0;
                player.sendMessage("You have been cured by "
                        + Misc.optimizeText(player.playerName) + ".");
                //c.setAnimation(Animation.create(4411));
                player.startAnimation(4411);
                castOnPlayer.gfx100(745);
                player.getPA().addSkillXP(620, 6);
                player.getPA().refreshSkill(6);
                break;

            case 30290:
                healOther(player, castOnPlayer);
                player.lunarDelay = System.currentTimeMillis();
                break;

            case 30282: // make method for heal all
                energyTransfer(player);
                player.lunarDelay = System.currentTimeMillis();
                break;
        }
    }

    private static void energyTransfer(final Player player) {
        if (player.playerIndex > 0) {
            Player q = PlayerHandler.players[player.playerIndex];
            final int oX = q.getX();
            final int oY = q.getY();
            if (player.skillLevel[6] < 91) {
                player.sendMessage("You need a magic level of 91 to cast this spell.");
                player.getCombat().resetPlayerAttack();
                Movement.stopMovement(player);;
                player.turnPlayerTo(oX, oY);
                return;
            }
            if (!q.acceptAid) {
                player.sendMessage("This player has their accept Aid off, therefore you cannot aid them!");
                return;
            }
            if (!ItemAssistant.playerHasItem(player, 9075, 3)
                    || !ItemAssistant.playerHasItem(player, 563, 2)
                    || !ItemAssistant.playerHasItem(player, 561, 1)) {
                player.sendMessage("You don't have the required runes to cast this spell.");
                player.getCombat().resetPlayerAttack();
                Movement.stopMovement(player);;
                player.turnPlayerTo(oX, oY);
                return;
            }
            if (player.specAmount == 10) {
                //c.setAnimation(Animation.create(4411));
                player.startAnimation(4411);
                ItemAssistant.updateSpecialBar(player);
                // q.getItems().updateSpecialBar();
                q.gfx100(736);// Just use c.gfx100
                ItemAssistant.deleteItem2(player, 9075, 3);
                ItemAssistant.deleteItem2(player, 563, 2);// For these you need to change
                // to deleteItem(item,
                // itemslot, amount);.
                ItemAssistant.deleteItem2(player, 561, 1);
                q.specAmount = 10.0;
                player.specAmount = 0;
                // q.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
                ItemAssistant.addSpecialBar(player, player.playerEquipment[Constants.WEAPON_SLOT]);
                player.getPA().addSkillXP(1750, 6);
                player.turnPlayerTo(oX, oY);
                player.getPA().refreshSkill(6);
                player.getCombat().resetPlayerAttack();
                Movement.stopMovement(player);;
            }
            if (player.specAmount < 10) {
                player.sendMessage("You need full special bar to use this on spell on someone.");
                player.getCombat().resetPlayerAttack();
                Movement.stopMovement(player);;
                player.turnPlayerTo(oX, oY);
                return;
            }
        }
    }

    private static void healOther(final Player player, Player q) {
        if (player.playerIndex > 0) {
            final int oX = q.getX();
            final int oY = q.getY();
            if (player.skillLevel[6] < 92) {
                player.sendMessage("You need a magic level of 92 to cast this spell.");
                player.getCombat().resetPlayerAttack();
                Movement.stopMovement(player);;
                player.turnPlayerTo(oX, oY);
                return;
            }
            if (player.skillLevel[3] - player.skillLevel[3] * .75 < 1) {
                player.sendMessage("Your hitpoints are too low to do this!");
                player.getCombat().resetPlayerAttack();
                Movement.stopMovement(player);;
                player.turnPlayerTo(oX, oY);
                return;
            }
            if (!q.acceptAid) {
                player.sendMessage("This player has their accept Aid off, therefore you cannot veng them!");
                return;
            }
            if (player.skillLevel[1] < 40) {
                player.sendMessage("You need a defence level of 40 to cast this spell.");
                player.getCombat().resetPlayerAttack();
                Movement.stopMovement(player);;
                player.turnPlayerTo(oX, oY);
                return;
            }
            if (!ItemAssistant.playerHasItem(player, 9075, 3)
                    || !ItemAssistant.playerHasItem(player, 563, 3)
                    || !ItemAssistant.playerHasItem(player, 565, 1)) {
                player.sendMessage("You don't have the required runes to cast this spell.");
                player.getCombat().resetPlayerAttack();
                Movement.stopMovement(player);;
                player.turnPlayerTo(oX, oY);
                return;
            }
            if (System.currentTimeMillis() - player.lastCast < 30000) {
                player.sendMessage("You can only heal others every 30 seconds.");
                player.getCombat().resetPlayerAttack();
                Movement.stopMovement(player);;
                player.turnPlayerTo(oX, oY);
                return;
            }
            if (q.skillLevel[3] + q.skillLevel[3] * .75 > q.skillLevel[3]) {
                q.skillLevel[3] = q.skillLevel[3];
                //c.setAnimation(Animation.create(4411));
                player.startAnimation(4411);
                q.gfx100(734);
                player.skillLevel[3] -= player.skillLevel[3] * .75;
                ItemAssistant
                        .deleteItem(player, 9075, ItemAssistant.getItemSlot(player, 9075), 3);
                ItemAssistant.deleteItem(player, 563, ItemAssistant.getItemSlot(player, 563), 3);
                ItemAssistant.deleteItem(player, 565, ItemAssistant.getItemSlot(player, 565), 1);
                player.getPA().addSkillXP(1750, 6);
                player.turnPlayerTo(oX, oY);
                player.getPA().refreshSkill(6);
                player.getPA().refreshSkill(3);
                player.getCombat().resetPlayerAttack();
                Movement.stopMovement(player);;
                // c.handleHitMask(c.playerLevel[3] * .75);
                // c.dealDamage(c.playerLevel[3] * .75);
                player.lastCast = System.currentTimeMillis();
            } else {
                //c.setAnimation(Animation.create(4411));
                player.startAnimation(4411);
                q.gfx100(734);
                q.skillLevel[3] += player.skillLevel[3] * .75;
                player.skillLevel[3] -= player.skillLevel[3] * .75;
                ItemAssistant
                        .deleteItem(player, 9075, ItemAssistant.getItemSlot(player, 9075), 3);
                ItemAssistant.deleteItem(player, 563, ItemAssistant.getItemSlot(player, 563), 3);
                ItemAssistant.deleteItem(player, 565, ItemAssistant.getItemSlot(player, 565), 1);
                player.getPA().addSkillXP(1750, 6);
                player.turnPlayerTo(oX, oY);
                player.getPA().refreshSkill(6);
                player.getPA().refreshSkill(3);
                player.getCombat().resetPlayerAttack();
                Movement.stopMovement(player);;
                // c.handleHitMask(c.playerLevel[3] * .75);
                // c.dealDamage(c.playerLevel[3] * .75);
                player.lastCast = System.currentTimeMillis();
            }
        }
    }

    private static void Dream(final Player c) {
        CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer event) {
                if (c.skillLevel[3] == c.getLevelForXP(c.playerXP[3])
                        && c.indream != true) {
                    c.indream = false;
                    c.sendMessage("You already have full hitpoints");
                    event.stop();
                    return;
                } else {
                    if (c.dream == 5) {
                        c.indream = true;
                        //c.setAnimation(Animation.create(6295));
                        c.startAnimation(6295);
                        c.sendMessage("The sleeping has an effect on your health");
                    } else if (c.dream == 0) {
                        c.indream = true;
                        //c.setAnimation(Animation.create(6296));
                        c.startAnimation(6296);
                        c.gfx0(1056);
                        c.skillLevel[3]++;
                        c.getPA().refreshSkill(3);
                    }
                    if (c.skillLevel[3] == c.getLevelForXP(c.playerXP[3])
                            && c.indream) {
                        c.indream = false;
                        c.sendMessage("You wake up for your dream");
                        //c.setAnimation(Animation.create(6297));
                        c.startAnimation(6297);
                    }
                    if (!c.indream) {
                        c.sendMessage("You wake up.");
                        event.stop();
                    }
                    if (System.currentTimeMillis() - c.logoutDelay < 10000
                            || c.followId > 0 || c.followId2 > 0
                            || c.spellId > 0) {
                        //c.setAnimation(Animation.create(6297));
                        c.startAnimation(6297);
                        c.sendMessage("You wake up.");
                        c.indream = false;
                        event.stop();
                    }
                    if (c.dream > 0)
                        c.dream--;
                }
            }

            @Override
            public void stop() {
            }
        }, 2);
    }

    private static void cureAll(Player player) {
        for (Player p : PlayerHandler.players) {// loop so it effects all
            // players
            Player person = p;
            if (p != null && person.distanceToPoint(player.absX, player.absY) <= 2) {
                Player castOn = p;// specific player's client
                player.poisonRunOut = 0;
                castOn.sendMessage("You have been cured by "
                        + Misc.optimizeText(player.playerName) + ".");
                //c.setAnimation(Animation.create(4409));
                player.startAnimation(4409);
                castOn.gfx100(745);
            }
        }
    }

    private static void HealAll(Player player) {
        for (Player p : PlayerHandler.players) {
            Player person = p;
            if (p != null && person.distanceToPoint(player.absX, player.absY) <= 1) {
                Player castOn = p;
                castOn.skillLevel[3] += 5;
                castOn.getPA().refreshSkill(3);
                player.getPA().refreshSkill(3);
                castOn.sendMessage("You have been cured by "
                        + Misc.optimizeText(player.playerName) + ".");
                //c.setAnimation(Animation.create(4409));
                player.startAnimation(4409);
                castOn.gfx100(744);
                castOn.skillLevel[3] += 2 * player.skillLevel[3];
                if (castOn.skillLevel[3] + (2 * castOn.skillLevel[3]) > castOn.skillLevel[3])
                    castOn.skillLevel[3] = castOn.getPA().getLevelForXP(
                            player.playerXP[3]);
                castOn.sendMessage("You have been healed by "
                        + Misc.optimizeText(player.playerName) + ".");
            }
        }
    }

}
