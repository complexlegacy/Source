package ionic.npc;

import ionic.item.ItemAssistant;
import ionic.npc.drops.Dropper;
import ionic.npc.pet.BossPet;
import ionic.npc.pet.Pet;
import ionic.object.clip.Region;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.achievements.AchievementHandler;
import ionic.player.content.combat.Combat;
import ionic.player.content.combat.Poison;
import ionic.player.content.minigames.Barrows;
import ionic.player.content.minigames.FightCaves;
import ionic.player.content.minigames.PestControl;
import ionic.player.content.miscellaneous.NpcInfoViewer;
import ionic.player.content.skills.slayer.Slayer;
import ionic.player.content.skills.summoning.Summoning;
import ionic.player.content.skills.summoning.SummoningData;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import utility.Misc;
import core.Configuration;
import core.Constants;
import core.Server;

public class NPCHandler {
    public static int maxNPCs = 14400;
    public static int maxListedNPCs = 14400;
    public static int maxNPCDrops = 14400;
    public static NPC npcs[] = new NPC[maxNPCs];
    public static int random;


    public int getNpcBlockAnimation(int i) {
    	return NPCData.data[i].blockAnim == 0 ? -1 : NPCData.data[i].blockAnim;
    }


    public static final int[][] gwData = {
            {0, 6260, 6261, 6263, 6268, 6265, 6269, 6271, 6275, 6276, 6277, 6278, 6279, 6272, 6274}, //Bandos
            {1, 6203, 6204, 6206, 6210, 6208, 6212, 6214, 6215, 6218, 6219, 6220, 6221}, //Zamorak
            {2, 6247, 6248, 6254, 6250, 6252, 6255, 6256, 6257, 6258, 6247}, //Saradomin
            {3, 6222, 6223, 6225, 6227, 6229, 6230, 6231, 6232, 6233, 6234, 6235, 6236, 6237, 6238, 6239, 6240, 6241, 6242, 6243, 6244, 6245, 6246},//Armadyl
            {4, 13447, 13451, 13452, 13453, 13454, 13456, 13457, 13458, 13459},//Zaros
    };


    public int getNpcDeleteTime(int i) {

        switch (npcs[i].npcType) {

            case 134:
                // Poisoned Spider
            case 119:
                // Chaos dwarf
            case 123:
                // Hobgoblin
                return 1;

            case 1624:
                // Dust devil
            case 1615:
                // Abyssal demon
            case 1265:
                // Rock crab
            case 2455:
                // Dagannoth
                return 2;

            case 103:
                // Ghost
                return 3;

            case 1648:
                // Crawling hand
            case 110:
                // Fire giant
                return 4;

            case 82:
                // Lesser demon
            case 52:
                // Baby blue dragon
            case 55:
                // Blue dragon
            case 86:
                // Giant rat
                return 6;

            case 941:
                // Green dragon
            case 54:
                // King black dragon
            case 13447://nex
                return 7;

            case 2783:
                // Dark beast
            case 76:
                // Zombie (level 25)
                return 8;

            default:
                return 5;

        }

    }

    // / Speed of the attacks
    public int getNpcDelay(int i) {

        switch (npcs[i].npcType) {

            case 14301://glacor
                return 7;
            case 14302://glacytes
            case 14303:
            case 14304:
                return 5;

            case 2028:
                // Karil the Tainted
                return 3;
            case 2026:
                // dharok
                return 6;

            case 6247:
                // Commander Zilyana
                return 4;

            case 50:
                // King Black Dragon
            case 8528:
                // Nomad
                return 6;

            case 2745:
                // Tztok-Jad
            case 10773:
                // Forst Dragon
            case 8133:
                // Corporeal beast
            case 3101:
                // Melee
            case 3102:
                // Range
            case 3103:
                // Mage
            case 2025:
                // Ahrim the Blighted
            case 6260:
                // General Graardor
            case 3200:
                // Chaos Elemental
            case 8349: case 8350: case 8351://Tormented Demons
                if (npcs[i].attackType == 2)
                    return 4;
                else if (npcs[i].attackType == 1)
                    return 6;
                else if (npcs[i].attackType == 0)
                    return 7;
            case 6203:
                // K'ril Tsutsaroth
                return 8;

            case 6222:
                // Kree'Arra
                return 10;

            default:
                // All other Npc
                return 4;

        }

    }

    // / How long the delay is then the damage hitsplat appears
    public int getHitDelay(int i) {

        switch (npcs[i].npcType) {

            case 2745:
                // Tztok-Jad
                if (npcs[i].attackType == 1 || npcs[i].attackType == 2)
                    return 5;

            case 8349:
                // Tormented Demon
                if (npcs[i].attackType == 1 || npcs[i].attackType == 2)
                    return 3;

            case 14301://glacor
                if (npcs[i].attackType == 1 || npcs[i].attackType == 2)
                    return 3;

            case 8133:
                // Corporeal beast
            case 2882:
                // Dagannoth Prime
            case 3200:
                // Chaos Elemental
            case 6203:
                // K'ril Tsutsaroth
                return 3;

            case 2025:
                // Ahrim the Blighted
                return 4;

            case 8528:
                // Nomad
                return 3;

            case 50:
                // King Black Dragon
                return 1;

            default:
                // All other Npc
                return 2;

        }

    }

    // / Amount of time for the Npc to respawn
    public int getRespawnTime(int i) {
    	if ((npcs[i].npcType >= 3732 && npcs[i].npcType <= 3782) || (npcs[i].npcType >= 6142 && npcs[i].npcType <= 6145)) {
    		return Integer.MAX_VALUE; //for pest control npcs
    	}

        switch (npcs[i].npcType) {
            case 2883:
                // Dagannoth Rex
            case 2882:
                // Dagannoth Prime
            case 2881:
                // Dagannoth Supreme
            case 3200:
                // Chaos Elemental
            case 8133:
                // Corporeal beast
            case 6260:
                // General Gaardor
            case 6203:
                // K'ril Tsutsaroth
            case 6222:
                // Kree'Arra
            case 6247:
                // Commander Zilyana
            case 9463:
                // Ice Strykewyrm
            case 50:
                // King Black Dragon
                return 70;
            case 8349://Tormented Demons
            case 8350:
            case 8351:
                return 100;
            case 6263:
                // Sergeant Steelwill
            case 6265:
                // Sergeant Grimespike
            case 6261:
                // Sergeant Strongstack
            case 6248:
                // Starlight
            case 6250:
                // Growler
            case 6252:
                // Bree
            case 6223:
                // Wingman Skree
            case 6225:
                // Flockleader Geerin
            case 6227:
                // Flight Kilisa
            case 6206:
                // Zakl'n Gritch
            case 6208:
                // Balfrug Kreeyath
            case 6204:
                // Tstanon Karlak
            case 5666:
                // Barrelchest
                return 35;

            case 8528:
                // Nomad
                return -1;

            case 14301:
                return 40;

            default:
                // All other Npc
                return 10;
        }
    }

    public boolean isAggressive(int i) {
        return NPCData.data[npcs[i].npcType].aggressive;
    }

    // / Fight caves Npc
    public boolean isFightCaveNpc(int i) {
        switch (npcs[i].npcType) {
            case FightCaves.TZ_KIH:
            case FightCaves.TZ_KEK:
            case FightCaves.TOK_XIL:
            case FightCaves.YT_MEJKOT:
            case FightCaves.KET_ZEK:
            case FightCaves.TZTOK_JAD:
                return true;
        }
        return false;
    }

    /**
     * Npc cannot attack with melee if added here to attack with melee . Npc
     * will hit a max of 1 with melee unless i add it into maxhit
     * **
     **/
    public boolean multiAttacks(int i) {

        switch (npcs[i].npcType) {

            case 8349:
                // Tormented Demon
            case 9463:
                // Ice Strykewyrm
            case 3200:
                // Chaos Elemental
            case 5666:
                // Barrelchest
                if (npcs[i].attackType == 2)
                    return true;

            default:
                return false;

        }

    }


    public void breakVengeance(NPC n, Client c, int damage) {
        if (n == null || c == null) {
            return;
        }
        c.vengOn = false;
        damage = (int) ((double) damage * 0.75);
        if (damage <= 0) {
            damage = 1;
        }
        c.forcedChat("Taste vengeance!");
        if (damage > n.HP) {
            damage = n.HP;
        }
        c.getCombat().appendHit(n, damage, 0, 8, 1);
    }


    public void loadSpell(int i) {
        Client player = (Client) PlayerHandler.players[npcs[i].killerId];
        NPC n = npcs[i];

        switch (npcs[i].npcType) {

		/* 0 - melee, 1 - range, 2 - mage */

            case 1158:
                for (int j = 0; j < PlayerHandler.players.length; j++) {
                    if (PlayerHandler.players[j] != null) {
                        int kq1;
                        if (goodDistance(npcs[i].absX, npcs[i].absY,
                                PlayerHandler.players[npcs[i].killerId].absX,
                                PlayerHandler.players[npcs[i].killerId].absY, 1))
                            kq1 = Misc.random(2);
                        else
                            kq1 = Misc.random(1);
                        if (kq1 == 0) {
                            npcs[i].attackType = 2; // mage
                            npcs[i].gfx0(278);
                            npcs[i].projectileId = 280;
                            npcs[i].endGfx = 281;
                        } else if (kq1 == 1) {
                            npcs[i].attackType = 1; // range
                            npcs[i].gfx0(-1);
                            npcs[i].endGfx = -1;
                            npcs[i].projectileId = 473;
                        } else if (kq1 == 2) {
                            npcs[i].attackType = 0; // melee
                            npcs[i].projectileId = -1;
                        }
                    }
                }
                break;
            // kalphite queen form 2
            case 1160:
                for (int j = 0; j < PlayerHandler.players.length; j++) {
                    if (PlayerHandler.players[j] != null) {
                        int kq1;
                        if (goodDistance(npcs[i].absX, npcs[i].absY,
                                PlayerHandler.players[npcs[i].killerId].absX,
                                PlayerHandler.players[npcs[i].killerId].absY, 1))
                            kq1 = Misc.random(2);
                        else
                            kq1 = Misc.random(1);
                        if (kq1 == 0) {
                            npcs[i].attackType = 2; // mage
                            npcs[i].gfx0(279);
                            npcs[i].projectileId = 280;
                            npcs[i].endGfx = 281;
                        } else if (kq1 == 1) {
                            npcs[i].attackType = 1; // range
                            npcs[i].gfx0(-1);
                            npcs[i].endGfx = -1;
                            npcs[i].projectileId = 473;
                        } else if (kq1 == 2) {
                            npcs[i].attackType = 0; // melee
                            npcs[i].projectileId = -1;
                        }
                    }
                }
                break;

            case 8528:
                // Nomad
                if (goodDistance(npcs[i].absX, npcs[i].absY,
                        PlayerHandler.players[npcs[i].killerId].absX,
                        PlayerHandler.players[npcs[i].killerId].absY, 1)) {
                    random = Misc.random(2);
                } else {
                    random = Misc.random(1);
                }
                if (random == 0) {
                    npcs[i].projectileId = 280; // mage
                    npcs[i].endGfx = 281;
                    npcs[i].attackType = 2;
                    npcs[i].forceChat("There is no turning back!");
                    player.freezeTimer = 15;
                    player.gfx0(369);
                    player.getCombat().resetPlayerAttack();
                    player.sendMessage("You have been Frozen!");

                } else if (random == 1) {
                    npcs[i].forceChat("You will be pierced.");
                    npcs[i].attackType = 1;
                    npcs[i].projectileId = 0;
                    npcs[i].endGfx = 0;
                } else if (random == 2) {
                    npcs[i].attackType = 0;
                    npcs[i].forceChat("I will crush you!");
                    npcs[i].projectileId = 0;
                    npcs[i].endGfx = 0;
                }

                break;

            case FightCaves.TZTOK_JAD:
                int r3 = 0;
                if (goodDistance(npcs[i].absX, npcs[i].absY, PlayerHandler.players[npcs[i].spawnedBy].absX,
                        PlayerHandler.players[npcs[i].spawnedBy].absY, 1))
                    r3 = Misc.random(2);
                else
                    r3 = Misc.random(1);
                if (r3 == 0) {
                    npcs[i].attackType = 2;
                    npcs[i].endGfx = 157;
                    npcs[i].projectileId = 448;
                } else if (r3 == 1) {
                    npcs[i].attackType = 1;
                    npcs[i].endGfx = 451;
                    npcs[i].projectileId = -1;
                } else if (r3 == 2) {
                    npcs[i].attackType = 0;
                    npcs[i].projectileId = -1;
                }
                break;

            case FightCaves.KET_ZEK:
                npcs[i].attackType = 2;
                npcs[i].projectileId = 445;
                npcs[i].endGfx = 446;
                break;

            case FightCaves.TOK_XIL:
                npcs[i].attackType = 1;
                npcs[i].projectileId = 443;
                break;

            case 13457:
                npcs[i].attackType = 1;
                break;

            case 8349:
            case 8350:
            case 8351:
                // Tormented Demon
                if (goodDistance(npcs[i].absX, npcs[i].absY, PlayerHandler.players[npcs[i].killerId].absX, PlayerHandler.players[npcs[i].killerId].absY, 2))
                    random = Misc.random(2);
                else
                    random = Misc.random(1);
                if (random == 0) {
                    npcs[i].attackType = 2;
                    npcs[i].gfx100(1885);
                    npcs[i].projectileId = 1884;
                } else if (random == 1) {
                    npcs[i].attackType = 1;
                    npcs[i].projectileId = 1889;
                } else if (random == 2) {
                    npcs[i].attackType = 0;
                    npcs[i].gfx100(1886);
                    npcs[i].projectileId = -1;
                } else if (Misc.random(4) == 1) {
                    npcs[i].attackType = 0;
                    npcs[i].gfx100(1885);
                    npcs[i].requestAnimation(10917);
                    npcs[i].forceChat("Aaaaaaaaargh!! Now you will die!");
                    player.dealDamage(player.skillLevel[3] - 10);
                    npcs[i].projectileId = -1;
                }
                break;

            case 9463:
                // Ice Strykewyrm
                if (goodDistance(npcs[i].absX, npcs[i].absY,
                        PlayerHandler.players[npcs[i].killerId].absX,
                        PlayerHandler.players[npcs[i].killerId].absY, 1))
                    random = Misc.random(2);
                else
                    random = Misc.random(1);
                if (random == 0) {
                    npcs[i].attackType = 2;
                } else if (random == 1) {
                    npcs[i].attackType = 1;
                } else if (random == 2) {
                    npcs[i].attackType = 0;
                }
                break;

            case 14301://Glacor
                npcs[i].attackType = Misc.random(2);
                break;

            case 5666:
                // Barrelchest
                random = Misc.random(1);
                if (random == 0) {
                    npcs[i].attackType = 0;
                } else if (random == 1) {
                    npcs[i].attackType = 2;
                }
                if (Misc.random(3) == 1) {
                    if (PlayerHandler.players[npcs[i].killerId].skillLevel[5] < 0)
                    {
                        PlayerHandler.players[npcs[i].killerId].skillLevel[5] = 0;
                    }
                    PlayerHandler.players[npcs[i].killerId].skillLevel[5] -= 30;
                    PlayerHandler.players[npcs[i].killerId].getPA().refreshSkill(5);
                    PlayerHandler.players[npcs[i].killerId].sendMessage("The barrelchest takes some of your prayer points!");
                }
                else if (Misc.random(5) == 1) {
                    PlayerHandler.players[npcs[i].killerId].getCombat().resetPrayers();
                    PlayerHandler.players[npcs[i].killerId].sendMessage("The barrelchest hits off your prayers!");
                }
                break;

            case 8133:
                // Corporeal Beast
                if (goodDistance(npcs[i].absX, npcs[i].absY,
                        PlayerHandler.players[npcs[i].killerId].absX,
                        PlayerHandler.players[npcs[i].killerId].absY, 3)) {
                    random = Misc.random(2);
                } else {
                    random = Misc.random(1);
                }
                if (random == 0) {
                    npcs[i].attackType = 2;
                    npcs[i].endGfx = -1;
                    npcs[i].projectileId = 1828;
                } else if (random == 1) {
                    npcs[i].attackType = 1;
                    npcs[i].endGfx = -1;
                    npcs[i].projectileId = 1839;
                } else if (random == 2) {
                    npcs[i].attackType = 0;
                    npcs[i].gfx100(1834);
                    npcs[i].projectileId = -1;
                }
                break;

            case 6222:
                // Kree'Arra
                if (goodDistance(npcs[i].absX, npcs[i].absY,
                        PlayerHandler.players[npcs[i].killerId].absX,
                        PlayerHandler.players[npcs[i].killerId].absY, 3))
                    random = Misc.random(2);
                else
                    random = Misc.random(1);
                if (random == 0) {
                    npcs[i].attackType = 2;
                    npcs[i].projectileId = 1197;
                } else if (random == 1) {
                    npcs[i].attackType = 1;
                    npcs[i].projectileId = 1198;
                } else if (random == 2) {
                    npcs[i].attackType = 0;
                    npcs[i].projectileId = 1198;
                }
                break;
            case 6247:
                // Commander Zilyana
                if (goodDistance(npcs[i].absX, npcs[i].absY,
                        PlayerHandler.players[npcs[i].killerId].absX,
                        PlayerHandler.players[npcs[i].killerId].absY, 3))
                    random = Misc.random(2);
                else
                    random = Misc.random(1);
                if (random <= 1) {
                    npcs[i].attackType = 2;
                    npcs[i].endGfx = 1224;
                    npcs[i].projectileId = -1;
                } else if (random == 2) {
                    npcs[i].attackType = 0;
                    npcs[i].endGfx = 1224;
                    npcs[i].projectileId = -1;
                }
                break;
            case 6248:
                // Startlight
                npcs[i].attackType = 0;
                break;
            case 6225:
                // Flockleader Geerin
                npcs[i].attackType = 1;
                break;
            case 6250:
                // Growler
                npcs[i].attackType = 2;
                npcs[i].projectileId = 1203;
                break;
            case 6252:
                // Bree
                npcs[i].attackType = 1;
                npcs[i].projectileId = 9;
                break;
            case 6261:
                // Sergeant Strongstack
                npcs[i].attackType = 0;
                break;
            case 6263:
                // Sergeant Steelwill
                npcs[i].attackType = 2;
                npcs[i].projectileId = 1203;
                npcs[i].endGfx = 1211;
                break;
            case 6265:
                // Sergeant Grimspike
                npcs[i].attackType = 1;
                npcs[i].projectileId = 1206;
                break;
            case 6203:
                // K'ril Tsutsaroth
                if (goodDistance(npcs[i].absX, npcs[i].absY,
                        PlayerHandler.players[npcs[i].killerId].absX,
                        PlayerHandler.players[npcs[i].killerId].absY, 2))
                    random = Misc.random(2);
                else
                    random = Misc.random(1);
                if (random <= 1) {
                    npcs[i].attackType = 2;
                    npcs[i].projectileId = 1211;
                } else if (random == 2) {
                    npcs[i].attackType = 0;
                    npcs[i].projectileId = -1;
                }
                break;
            case 5247:
                // Penance Queen
                random = Misc.random(1);
                if (random == 0) {
                    n.projectileId = 871;
                    n.endGfx = 872;
                    n.attackType = 1;
                } else {
                    n.projectileId = -1;
                    n.endGfx = -1;
                    n.attackType = 0;
                }
                break;
            case 1183:
                // Elf warrior (ranged)
                n.gfx100(250);
                n.projectileId = 249;
                n.attackType = 1;
                break;
            case 2892:
                // Spinolyp (mage)
                n.projectileId = 94;
                n.attackType = 2;
                n.endGfx = 95;
                break;
            case 2894:
                // Spinolyp (ranged)
                npcs[i].projectileId = 298;
                npcs[i].attackType = 1;
                break;
            case 50:
                // King Black Dragon
                random = Misc.random(7);
                if (random == 0) {
                    npcs[i].projectileId = 393; // red
                    npcs[i].endGfx = 430;
                    npcs[i].attackType = 3;
                } else if (random == 1) {
                    npcs[i].projectileId = 394; // green
                    npcs[i].endGfx = 429;
                    npcs[i].attackType = 3;
                } else if (random == 2) {
                    npcs[i].projectileId = 395; // white
                    npcs[i].endGfx = 431;
                    npcs[i].attackType = 3;
                } else if (random == 3) {
                    npcs[i].projectileId = 396; // blue
                    npcs[i].endGfx = 428;
                    npcs[i].attackType = 3;
                } else if (random >= 4) {
                    npcs[i].projectileId = -1; // melee
                    npcs[i].endGfx = -1;
                    npcs[i].attackType = 0;
                }
                break;

            case 2025:
                // Ahrim the Blighted
                npcs[i].attackType = 2;
                int r = Misc.random(3);
                if (r == 0) {
                    npcs[i].gfx100(158);
                    npcs[i].projectileId = 159;
                    npcs[i].endGfx = 160;
                }
                if (r == 1) {
                    npcs[i].gfx100(161);
                    npcs[i].projectileId = 162;
                    npcs[i].endGfx = 163;
                }
                if (r == 2) {
                    npcs[i].gfx100(164);
                    npcs[i].projectileId = 165;
                    npcs[i].endGfx = 166;
                }
                if (r == 3) {
                    npcs[i].gfx100(155);
                    npcs[i].projectileId = 156;
                }
                break;

            case 2881:
                // Dagannoth Supreme
                npcs[i].attackType = 1;
                npcs[i].projectileId = 298;
                break;
            case 2882:
                // Dagannoth Prime
                npcs[i].attackType = 2;
                npcs[i].projectileId = 162;
                npcs[i].endGfx = 477;
                break;
            case 2028:
                // Karil the Tainted
                npcs[i].attackType = 1;
                npcs[i].projectileId = 27;
                break;
            case 3200:
                // Chaos Elemental
                random = Misc.random(1);
                if (random == 0) {
                    npcs[i].attackType = 1;
                    npcs[i].gfx100(550);
                    npcs[i].projectileId = 551;
                    npcs[i].endGfx = 552;
                } else {
                    npcs[i].attackType = 2;
                    npcs[i].gfx100(553);
                    npcs[i].projectileId = 554;
                    npcs[i].endGfx = 555;
                }
                break;
            case 6260:
                // General Graardor
                if (goodDistance(npcs[i].absX, npcs[i].absY,
                        PlayerHandler.players[npcs[i].killerId].absX,
                        PlayerHandler.players[npcs[i].killerId].absY, 3)) {
                    random = Misc.random(2);
                } else {
                    random = Misc.random(1);
                }
                if (random <= 1) {
                    npcs[i].attackType = 1;
                    npcs[i].endGfx = -1;
                    npcs[i].projectileId = 288;
                } else if (random == 2) {
                    npcs[i].attackType = 0;
                    npcs[i].endGfx = -1;
                    npcs[i].projectileId = -1;
                }
                break;
//            case 2745:
//                // TzTok-Jad
//                if (goodDistance(npcs[i].absX, npcs[i].absY,
//                        PlayerHandler.players[npcs[i].spawnedBy].absX,
//                        PlayerHandler.players[npcs[i].spawnedBy].absY, 1))
//                    random = Misc.random(2);
//                else
//                    random = Misc.random(1);
//                if (random == 0) {
//                    npcs[i].attackType = 2;
//                    npcs[i].endGfx = 157;
//                    npcs[i].projectileId = 448;
//                } else if (random == 1) {
//                    npcs[i].attackType = 1;
//                    npcs[i].endGfx = 451;
//                    npcs[i].projectileId = -1;
//                } else if (random == 2) {
//                    npcs[i].attackType = 0;
//                    npcs[i].projectileId = -1;
//                }
//                break;

            case 5363:// Mithril-Dragon
            case 53:// Red dragon
            case 54:// Black dragon
            case 55:// Blue dragon
            case 941:// Green dragon
            case 51://frost dragon
            case 1590:// Bronze dragon
            case 1591:// Iron dragon
            case 1592:// Steel dragon
                if (goodDistance(npcs[i].absX, npcs[i].absY,
                        PlayerHandler.players[npcs[i].killerId].absX,
                        PlayerHandler.players[npcs[i].killerId].absY, 1)) {
                    random = Misc.random(1);
                } else {
                    random = 0;
                }
                if (random == 1) {
                    npcs[i].projectileId = -1; // melee
                    npcs[i].endGfx = -1;
                    npcs[i].attackType = 0;
                } else if (random == 0) {
                    npcs[i].projectileId = 393; // red
                    npcs[i].endGfx = 430;
                    npcs[i].attackType = 3;
                }
                break;
        }
    }

    // / How far can the Npc attack reach up to
    public int distanceRequired(int i) {
    	if (SummoningData.getByNpc(npcs[i].npcType) != null) {
    		return 2;
    	}
        switch (npcs[i].npcType) {
            case 1158:
            case 1160:
                return 7;
            case 8528: // Nomad
                return 8;

            case 2025:
                // Ahrim the Blighted
            case 2028:
                // Karil the Tainted
            case 6263:
                // Sergeant Steelwill
            case 6250:
                // Growler
            case 6225:
                // Flockleader Geerin
            case 5363:
                // Mithril-Dragon
            case 53:
                // Red dragon
            case 54:
                // Black dragon
            case 55:
                // Blue dragon
            case 941:
                // Green dragon
            case 1590:
                // Bronze dragon
            case 1591:
                // Iron dragon
            case 1592:
                // Steel dragon
            case 2882:
                // Dagannoth Prime
            case 2881:
                // Dagannoth Supreme
                return 6;

            case 3200:
                // Chaos Elemental
            case 9463:
                // Ice Strykewyrm
            case 6203:
                // K'ril Tsutsaroth
            case 6260:
                // General Graardor
            case 6222:
                // Kree'Arra
            case 8349:
                // Tormented Demon
                return 8;

            case 2892:
                // Spinolyp
            case 2894:
                // Spinolyp
                return 10;

            case 2745:
                // TzTok-Jad
            case 6247:
                // Commander Zilyana
            case 8133:
                // Corporeal Beast
                return 11;

            case 50:
                // King Black Dragon
                return 25;

            default:
                return 1;

        }

    }

    /**
     * How far the Npc can keep following the player
     **/
    public int followDistance(int i) {
    	if(npcs[i].npcType >= 3732 && npcs[i].npcType <= 3782) {
    		return 30;
    	}
        switch (npcs[i].npcType) {

            case 14301://glacor & glacytes
            case 14302:
            case 14303:
            case 14304:
                return 20;


            case 2031:
            case 2032:
            case 2033:
            case 2034:
            case 2035:
            case 2036:
                return 12;

            case 13447:
                return 50;

            case 8133:
                // Corporeal beast
            case 6260:
                // General Gaardor
            case 8349://Tormented Demons
            case 8350:
            case 8351:
                return 7;
            case 6263:
                // Sergeant Steelwill
            case 6265:
                // Sergeant Grimespike
            case 6261:
                // Sergeant Strongstack
            case 6203:
                // K'ril Tsutsaroth
            case 6206:
                // Zakl'n Gritch
            case 6208:
                // Balfrug Kreeyath
            case 6204:
                // Tstanon Karlak
            case 6222:
                // Kree'Arra
            case 6223:
                // Wingman Skree
            case 6225:
                // Flockleader Geerin
            case 6227:
                // Flight Kilisa
            case 6248:
                // Starlight
            case 6250:
                // Growler
            case 6252:
                // Bree
                return 25;
        }
        return 0;
    }

    /**
     * Speed of gfx special attack of the Npc
     **/
    public int getProjectileSpeed(int i) {
        switch (npcs[i].npcType) {
            case 2745:
                // TzTok-Jad
                return 140;
            case 1158:
            case 1160:
                return 90;
            case 53:
                // Red dragon
            case 54:
                // Black dragon
            case 55:
                // Blue dragon
            case 941:
                // Green dragon
            case 1589:
                // Baby red dragon
            case 1590:
                // Bronze dragon
            case 1591:
                // Iron dragon
            case 1592:
                // Steel dragon
                return 120;
            case 50:
                // King Black Dragon
                return 90;
            default:
                return 85;
        }
    }

    // / Max hit of the specific attack that is in multiAttacks
    public int getMaxHit(int i) {
        switch (npcs[i].npcType) {
		/*
		 * 0 - melee 1 - range 2 - mage
		 */
            case 1158:
            case 1160:
                return 31;
            case 5666:
                // Barrelchest
                if (npcs[i].attackType == 2)
                    return 45;

            case 8349:
            case 8350:
            case 8351:
                if (npcs[i].attackType == 0)
                    return 30;
                else
                if (npcs[i].attackType == 1)
                    return 33;
                else
                if (npcs[i].attackType == 2)
                    return 33;

            case 9463:
                // Ice Strykewyrm
                if (npcs[i].attackType == 2)
                    return 30;

            case 3200:
                // Chaos Elemental
                if (npcs[i].attackType == 2)
                    return 50;

        }

        return 1;

    }


    public static void giveKillcount(Player player, int npc) {
        for (int i = 0; i < gwData.length; i++) {
            for (int i1 = 1; i1 < gwData[i].length; i1++) {
                if (npc == gwData[i][i1]) {
                    player.gwdKC[gwData[i][0]]++;
                    player.getPA().sendFrame126("" + player.gwdKC[gwData[i][0]] + "", 27507 + gwData[i][0]);
                    return;
                }
            }
        }
    }


    public void multiAttackDamage(int i) {

        int max = getMaxHit(i);
        getMaxHit(i);

        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                Client player = (Client) PlayerHandler.players[j];
                if (player.isDead || player.heightLevel != npcs[i].heightLevel)
                    continue;
                if (PlayerHandler.players[j].goodDistance(player.absX,
                        player.absY, npcs[i].absX, npcs[i].absY, 15)) {

                    if (npcs[i].attackType == 2) {
                        if (player.prayerActive[16]
                                || player.curseActive[player.curses().DEFLECT_MAGIC]) {
                            if (player.curseActive[player.curses().DEFLECT_MAGIC])
                                player.curses().deflectNPC(npcs[i], 0, 2);
                        } else if (Misc.random(500) + 200 > Misc.random(player
                                .getCombat().mageDef())) {
                            int dam = Misc.random(max);
                            player.getCombat().appendHit(player, dam, 0, 2,
                                    false);
                        } else {
                            player.getCombat()
                                    .appendHit(player, 0, 0, 2, false);
                        }
                    } else if (npcs[i].attackType == 1) {
                        if (!player.prayerActive[17]
                                || !player.curseActive[player.curses().DEFLECT_MISSILES]) {
                            int dam = Misc.random(max);
                            if (Misc.random(500) + 200 > Misc.random(player
                                    .getCombat().calculateRangeDefence())) {
                                player.getCombat().appendHit(player, dam, 0, 1,
                                        false);
                            } else {
                                player.getCombat().appendHit(player, 0, 0, 2,
                                        false);
                            }
                        } else {
                            if (player.curseActive[player.curses().DEFLECT_MISSILES])
                                player.curses().deflectNPC(npcs[i], 0, 1);
                            player.getCombat()
                                    .appendHit(player, 0, 0, 2, false);
                        }
                    }
                    if (npcs[i].endGfx > 0) {
                        player.gfx0(npcs[i].endGfx);
                    }
                }
                player.getPA().refreshSkill(3);
            }
        }
    }

    public int getClosePlayer(int i) {
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                if (j == npcs[i].spawnedBy)
                    return j;
                if (goodDistance(PlayerHandler.players[j].absX,
                        PlayerHandler.players[j].absY, npcs[i].absX,
                        npcs[i].absY, 2 + distanceRequired(i)
                                + followDistance(i))
                        || isFightCaveNpc(i)) {
                    if ((PlayerHandler.players[j].underAttackBy <= 0 && PlayerHandler.players[j].underAttackBy2 <= 0)
                            || PlayerHandler.players[j].inMulti())
                        if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
                            return j;
                }
            }
        }
        return 0;
    }

    public int getCloseRandomPlayer(int i) {
        ArrayList<Integer> players = new ArrayList<Integer>();
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                if (goodDistance(PlayerHandler.players[j].absX,
                        PlayerHandler.players[j].absY, npcs[i].absX,
                        npcs[i].absY, 2 + distanceRequired(i)
                                + followDistance(i) )
                        || isFightCaveNpc(i)) {
                    if ((PlayerHandler.players[j].underAttackBy <= 0 && PlayerHandler.players[j].underAttackBy2 <= 0)
                            || PlayerHandler.players[j].inMulti())
                        if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
                            players.add(j);
                }
            }
        }
        if (players.size() > 0)
            return players.get(Misc.random(players.size() - 1));
        else
            return 0;
    }

    public void multiAttackGfx(int i, int gfx) {
        if (npcs[i].projectileId < 0)
            return;
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                Client player = (Client) PlayerHandler.players[j];
                if (player.heightLevel != npcs[i].heightLevel)
                    continue;
                if (PlayerHandler.players[j].goodDistance(player.absX,
                        player.absY, npcs[i].absX, npcs[i].absY, 15)) {
                    int nX = npcs[i].getX() + offset(i);
                    int nY = npcs[i].getY() + offset(i);
                    int pX = player.getX();
                    int pY = player.getY();
                    int offX = (nY - pY) * -1;
                    int offY = (nX - pX) * -1;
                    player.getPA().createPlayersProjectile(nX, nY, offX, offY,
                            50, getProjectileSpeed(i), npcs[i].projectileId,
                            43, 31, -player.getId() - 1, 65);
                }
            }
        }
    }

    public void spawnGlacytes(NPC n) {
        int cur = 0;
        for (int i = 0; i < 3; i++) {
            spawnGlacyte((Client) PlayerHandler.players[n.underAttackBy], 14302 + cur, n.absX + (cur * 2), n.absY + 3, n.heightLevel,
                    0, 90, 12, 75, 60, n, i);
            cur++;
        }
    }

    public void spawnGlacyte(Client c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence, NPC owner, int id) {
        int slot = -1;
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            return;
        }
        NPC newNPC = new NPC(slot, npcType);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.HP = HP;
        newNPC.MaxHP = HP;
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        newNPC.spawnedBy = c.getId();
        if (c != null) {
            newNPC.underAttack = true;
            newNPC.killerId = c.playerId;
            newNPC.underAttack = true;
            newNPC.underAttackBy = c.playerId;
        }
        npcs[slot] = newNPC;
        owner.glacytes[id] = newNPC;
    }

    /**
     * Summon npc, barrows, etc
     **/
    public void spawnNpc(Client c, int npcType, int x, int y, int heightLevel,
                         int WalkingType, int HP, int maxHit, int attack, int defence,
                         boolean attackPlayer, boolean headIcon) {
        int slot = -1;
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            return;
        }
        NPC newNPC = new NPC(slot, npcType);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.HP = HP;
        newNPC.MaxHP = HP;
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        newNPC.spawnedBy = c.getId();
        if (headIcon)
            c.getPA().drawHeadicon(1, slot, 0, 0);
        if (attackPlayer) {
            newNPC.underAttack = true;
            if (c != null) {
                newNPC.killerId = c.playerId;
                newNPC.underAttack = true;
                newNPC.underAttackBy = c.playerId;
            }
        }
        npcs[slot] = newNPC;
    }

    public void spawnNpc2(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence)
    {
        // System.out.println("Nomad: " + x + " " + y + "--------------------------------------------------------------");
        int slot = -1;
        for (int i = 1; i < maxNPCs; i++)
        {
            if (npcs[i] == null)
            {
                slot = i;
                break;
            }
        }
        if (slot == -1)
        {
            return;
        }
        NPC newNPC = new NPC(slot, npcType);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.HP = HP;
        newNPC.MaxHP = HP;
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        npcs[slot] = newNPC;
    }

    public void spawnBarrows(Client c, int npcType, int x, int y,
                             int heightLevel, int maxHit, int attack, int defence, boolean tunnel) {
        int slot = -1;
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            return;
        }
        NPC newNPC = new NPC(slot, npcType);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = 0;
        newNPC.HP = 100;
        newNPC.MaxHP = 100;
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        newNPC.spawnedBy = c.getId();
        c.brotherSpawned = newNPC;
        c.getPA().drawHeadicon(1, slot, 0, 0);
        newNPC.underAttack = true;
        if (c != null) {
            newNPC.killerId = c.playerId;
        }
        if (!tunnel) {
            newNPC.forceChat("You dare disturb my rest!");
        } else {
            newNPC.forceChat("You dare steal from us!");
        }
        npcs[slot] = newNPC;
    }

    public static void pestControlNpc(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence, boolean isPest) {
        int slot = -1;
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            return;
        }
        NPC newNPC = new NPC(slot, npcType);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.HP = HP;
        newNPC.MaxHP = HP;
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        newNPC.isPestControlNPC = true;
        newNPC.isPest = isPest;
        npcs[slot] = newNPC;
        if (npcType == 3782) {
        	PestControl.knight = newNPC;
        }
    }

    public static int getAttackEmote(int i) {
    	
    	if (npcs[i].isFamiliar) {
    		return SummoningData.familiarAttackEmotes(npcs[i].npcType);
    	}

        switch (npcs[i].npcType) {
            case 14301://glacor & glacytes
            case 14302:
            case 14303:
            case 14304:
                if (npcs[i].attackType == 2) {
                    return 9967;
                } else if (npcs[i].attackType == 1) {
                    return 9968;
                } else if (npcs[i].attackType == 0) {
                    return 9955;
                }
            case 8528:
                // Nomad
                if (npcs[i].attackType < 2) {
                    return 12696;
                } else if (npcs[i].attackType == 2) {
                    return 12697;
                }
            case 6203:
                // K'ril Tsutaroth
                if (npcs[i].attackType == 2) {
                    return 6947;
                } else if (npcs[i].attackType == 0) {
                    return 6945;
                }

            case 6260:
                if (Misc.random(2) != 0) {
                    return 7060;
                } else {
                    return 7063;
                }

            case 6247:
                // Commander Zilyana
                if (npcs[i].attackType == 2) {
                    return 6964;
                } else if (npcs[i].attackType == 0) {
                    return 6967;
                }

            case 8133:
                // Corporeal Beast
                if (npcs[i].attackType == 2) {
                    return 10053;
                } else if (npcs[i].attackType == 1) {
                    return 10059;
                } else if (npcs[i].attackType == 0) {
                    return 10057;
                }

            case 8349:
                // Tormented Demon
                if (npcs[i].attackType == 2) {
                    return 10917;
                } else if (npcs[i].attackType == 1) {
                    return 10918;
                } else if (npcs[i].attackType == 0) {
                    return 10922;
                }

            case 9463:
                // Ice Strykewyrm
                if (npcs[i].attackType == 2) {
                    return 12794;
                } else if (npcs[i].attackType == 1) {
                    return 12792;
                } else if (npcs[i].attackType == 0) {
                    return 12791;
                }

            case 5666:
                // Barrelchest
                if (npcs[i].attackType == 2) {
                    return 5895;
                } else if (npcs[i].attackType == 0) {
                    return 5894;
                }


            case 50://kbd
                if (npcs[i].attackType == 3) {
                    return 81;
                } else if (npcs[i].attackType == 0) {
                    return 80;
                }
            case 1590:// Bronze dragon
            case 1591:// Iron dragon
            case 1592:// Steel dragon
            case 5363:// mithril dragon
                if (npcs[i].attackType == 3) {
                    return 14252;
                } else if (npcs[i].attackType == 0) {
                    return 14247;
                }

            case 51://frost dragon
                if (npcs[i].attackType == 3) {
                    return 13155;
                } else if (npcs[i].attackType == 0) {
                    return 13151;
                }

            case 2550:
                if (npcs[i].attackType == 0)
                    return 7060;
                else
                    return 7063;

            case 6222:
                // Kree 'Arra
                if (npcs[i].attackType == 2)
                    return 6976;
                else if (npcs[i].attackType == 1)
                    return 6976;
                else if (npcs[i].attackType == 0)
                    return 6977;
           
            case 2745:
                if (npcs[i].attackType == 2)
                    return 9300;
                else if (npcs[i].attackType == 1)
                    return 9276;
                else if (npcs[i].attackType == 0)
                    return 9277;
            default:
                return NPCData.data[npcs[i].npcType].attackAnim == 0 ? 0x326 : NPCData.data[npcs[i].npcType].attackAnim;
        }
    }

    // / Npc emote on death
    public int getDeadEmote(int i) {
    	if (npcs[i].isFamiliar) {
    		if (npcs[i].owner != null) {
    			npcs[i].owner.sendMessage("Your familiar has died.");
    			Summoning.dismiss(npcs[i].owner);
    			return -1;
    		}
    	}
    	return NPCData.data[npcs[i].npcType].deathAnim == 0 ? 2304 : NPCData.data[npcs[i].npcType].deathAnim;
    }
    
    public NPC lastNpc = null;

    public void newNPC(int npcType, int x, int y, int heightLevel,
                       int WalkingType, int HP, int maxHit, int attack, int defence) {
        int slot = -1;
        for (int i = 1; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1)
            return;
        NPC newNPC = new NPC(slot, npcType);
        lastNpc = newNPC;
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = WalkingType;
        newNPC.HP = HP;
        newNPC.MaxHP = HP;
        newNPC.maxHit = maxHit;
        newNPC.attack = attack;
        newNPC.defence = defence;
        npcs[slot] = newNPC;
    }


    public Client allPlayers(int i) {
        return (Client) PlayerHandler.players[i];
    }

    public void process() {
        for (int i = 0; i < maxNPCs; i++) {
            if (npcs[i] == null) {
                continue;
            }
            npcs[i].clearUpdateFlags();
        }
        for (int i = 0; i < maxNPCs; i++) {
            NPC NPC = npcs[i];
            if (NPC != null) {

				/*
				 * Start of Pet. Leave this in process because this for loop can
				 * cause a problem when using events for 10 players for example.
				 * It's best to have this 'for loop' once every game tick than
				 * have 10 players doing it 10 times per game tick.
				 */
            	Client petOwner = (Client) PlayerHandler.players[NPC.summonedBy];
            	if (petOwner == null && NPC.summoned) {
            		Pet.deletePet(NPC);
            	}
            	if (petOwner != null) {
            		if (petOwner.isDead) {
            			Pet.deletePet(NPC);
            		}
            		if (petOwner.getPetSummoned() && NPC.summoned) {
            			NPC.facePlayer(petOwner.playerId);
            			if (petOwner.goodDistance(NPC.getX(), NPC.getY(), petOwner.absX, petOwner.absY, 15)) {
            				followPlayer(i, petOwner.playerId, true);
            			} else {
            				Pet.deletePet(NPC);
            				Pet.summonPet(petOwner, petOwner.petID, petOwner.absX, petOwner.absY - 1, petOwner.heightLevel);
            			}
            		}
            		if (petOwner.familiar != null) {
            			if (!NPC.underAttack) {
            				NPC.facePlayer(petOwner.playerId);
            				if (petOwner.goodDistance(NPC.getX(), NPC.getY(), petOwner.absX, petOwner.absY, 15)){
            					followPlayer(i, petOwner.playerId, true);
            				} else {
            					Summoning.call(petOwner,true);
            				}
            			}
            		}
                }

				/* End of Pet. */

                if (npcs[i].actionTimer > 0) {
                    npcs[i].actionTimer--;
                }
                if (npcs[i].freezeTimer > 0) {
                    npcs[i].freezeTimer--;
                }
                if (npcs[i].hitDelayTimer > 0) {
                    npcs[i].hitDelayTimer--;
                }
                if (npcs[i].hitDelayTimer == 1) {
                    npcs[i].hitDelayTimer = 0;
                    applyDamage(i);
                }
                if (npcs[i].attackTimer > 0) {
                    npcs[i].attackTimer--;
                }

                if (npcs[i].disappearTime > 1) {
                    npcs[i].disappearTime--;
                } else if (npcs[i].disappearTime == 1) {
                    npcs[i].isDead = true;
                }

                if (npcs[i].spawnedBy > 0 && !isFightCaveNpc(i)) {
                    if (PlayerHandler.players[npcs[i].spawnedBy] == null
                            || PlayerHandler.players[npcs[i].spawnedBy].heightLevel != npcs[i].heightLevel
                            || PlayerHandler.players[npcs[i].spawnedBy].isDead
                            || !PlayerHandler.players[npcs[i].spawnedBy]
                            .goodDistance(
                                    npcs[i].getX(),
                                    npcs[i].getY(),
                                    PlayerHandler.players[npcs[i].spawnedBy]
                                            .getX(),
                                    PlayerHandler.players[npcs[i].spawnedBy]
                                            .getY(), 20)) {
                    	if (!npcs[i].isFamiliar) {
                    		npcs[i] = null;
                    	}
                    }
                }
                if (npcs[i] == null)
                    continue;

                /**
                 * Attacking player
                 **/
                if (isAggressive(i) && !npcs[i].underAttack && !npcs[i].isDead
                        && npcs[i].killerId <= 0) {
                    npcs[i].killerId = getCloseRandomPlayer(i);
                }
                if (System.currentTimeMillis() - npcs[i].lastDamageTaken > 5000)
                    npcs[i].underAttackBy = 0;
                if ((npcs[i].killerId > 0 || npcs[i].underAttack)
                        && !npcs[i].walkingHome && retaliates(npcs[i].npcType)) {
                    if (!npcs[i].isDead) {
                        int p = npcs[i].killerId;
                        if (PlayerHandler.players[p] != null) {
                            Client c = (Client) PlayerHandler.players[p];
                            followPlayer(i, c.playerId, false);
                            if (npcs[i] == null)
                                continue;
                            stepAway(c, i);
                            if (npcs[i].attackTimer == 0) {
                                attackPlayer(c, i);
                            }
                        } else {
                            npcs[i].killerId = 0;
                            npcs[i].underAttack = false;
                            npcs[i].facePlayer(0);
                        }
                    }
                }
                
                if (npcs[i].isPest && !npcs[i].needRespawn) {
                	int x = npcs[i].absX, y = npcs[i].absY;
                	if (x >= 2654 && y >= 2590 && x <= 2659 && y <= 2595) {
                		if (npcs[i].knightHit <= 0) {
                			npcs[i].knightHit = 10;
                			if (PestControl.knight != null) {
                				int dmg = 4 + Misc.random(13);
                				if (dmg > PestControl.knight.HP) {
                					dmg = PestControl.knight.HP;
                				}
                				Combat.pestControlKnightHit(PestControl.knight, dmg, 0, 0, 0);
                				PestControl.KNIGHTS_HEALTH = PestControl.knight.HP;
                			}
                		} else {
                			npcs[i].knightHit --;
                		}
                	}
                }

                /**
                 * Random walking and walking home
                 **/
                if (npcs[i] == null)
                    continue;
                if ((!npcs[i].underAttack || npcs[i].walkingHome)
                        && npcs[i].randomWalk && !npcs[i].isDead) {
                    npcs[i].facePlayer(0);
                    npcs[i].killerId = 0;
                    if (npcs[i].spawnedBy == 0) {
                        if ((npcs[i].absX > npcs[i].makeX
                                + Constants.NPC_RANDOM_WALK_DISTANCE)
                                || (npcs[i].absX < npcs[i].makeX
                                - Constants.NPC_RANDOM_WALK_DISTANCE)
                                || (npcs[i].absY > npcs[i].makeY
                                + Constants.NPC_RANDOM_WALK_DISTANCE)
                                || (npcs[i].absY < npcs[i].makeY
                                - Constants.NPC_RANDOM_WALK_DISTANCE)) {
                            npcs[i].walkingHome = true;
                        }
                    }
                    if (npcs[i].walkingHome && npcs[i].absX == npcs[i].makeX
                            && npcs[i].absY == npcs[i].makeY) {
                        npcs[i].walkingHome = false;
                    } else if (npcs[i].walkingHome) {
                        npcs[i].moveX = GetMove(npcs[i].absX, npcs[i].makeX);
                        npcs[i].moveY = GetMove(npcs[i].absY, npcs[i].makeY);
                        npcs[i].getNextNPCMovement(i);
                        npcs[i].updateRequired = true;
                    }
                    if (npcs[i].walkingType >= 0) {
                        switch (npcs[i].walkingType) {

                            case 1:
                                if (Misc.random(3) == 1 && !npcs[i].walkingHome) {
                                    int MoveX = 0;
                                    int MoveY = 0;
                                    int Rnd = Misc.random(9);
                                    if (Rnd == 1) {
                                        MoveX = 1;
                                        MoveY = 1;
                                    } else if (Rnd == 2) {
                                        MoveX = -1;
                                    } else if (Rnd == 3) {
                                        MoveY = -1;
                                    } else if (Rnd == 4) {
                                        MoveX = 1;
                                    } else if (Rnd == 5) {
                                        MoveY = 1;
                                    } else if (Rnd == 6) {
                                        MoveX = -1;
                                        MoveY = -1;
                                    } else if (Rnd == 7) {
                                        MoveX = -1;
                                        MoveY = 1;
                                    } else if (Rnd == 8) {
                                        MoveX = 1;
                                        MoveY = -1;
                                    }
                                    if (MoveX == 1) {
                                        if (npcs[i].absX + MoveX < npcs[i].makeX + 1) {
                                            npcs[i].moveX = MoveX;
                                        } else {
                                            npcs[i].moveX = 0;
                                        }
                                    }
                                    if (MoveX == -1) {
                                        if (npcs[i].absX - MoveX > npcs[i].makeX - 1) {
                                            npcs[i].moveX = MoveX;
                                        } else {
                                            npcs[i].moveX = 0;
                                        }
                                    }
                                    if (MoveY == 1) {
                                        if (npcs[i].absY + MoveY < npcs[i].makeY + 1) {
                                            npcs[i].moveY = MoveY;
                                        } else {
                                            npcs[i].moveY = 0;
                                        }
                                    }
                                    if (MoveY == -1) {
                                        if (npcs[i].absY - MoveY > npcs[i].makeY - 1) {
                                            npcs[i].moveY = MoveY;
                                        } else {
                                            npcs[i].moveY = 0;
                                        }
                                    }
                                    npcs[i].getNextNPCMovement(i);
                                    npcs[i].updateRequired = true;
                                }
                                break;

                            case 5:
                                npcs[i].turnNpc(npcs[i].absX - 1, npcs[i].absY);
                                break;

                            case 4:
                                npcs[i].turnNpc(npcs[i].absX + 1, npcs[i].absY);
                                break;

                            case 3:
                                npcs[i].turnNpc(npcs[i].absX, npcs[i].absY - 1);
                                break;
                            case 2:
                                npcs[i].turnNpc(npcs[i].absX, npcs[i].absY + 1);
                                break;
                        }
                    }
                }
                if (npcs[i].isDead) {
                    if (npcs[i].actionTimer == 0 && npcs[i].applyDead == false
                            && npcs[i].needRespawn == false) {
                        npcs[i].facePlayer(0);
                        npcs[i].actionTimer = getNpcDeleteTime(i); // delete
                        npcs[i].killedBy = getNpcKillerId(i);
                        if (getDeadEmote(i) != -1) {
                        	startAnimation(getDeadEmote(i), i);
                        }
                        npcs[i].freezeTimer = 0;
                        npcs[i].applyDead = true;
                        Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
                        if (c != null) {
                        handleSpecialDeaths(c, npcs[i].npcType);
                        handleAchievements(c, npcs[i].npcType);
                        if (NpcInfoViewer.MonsterData.forID(npcs[i].npcType) != null) {
                            c.killLogs[NpcInfoViewer.MonsterData.getSlot(NpcInfoViewer.MonsterData.forID(npcs[i].npcType))] ++;
                        }
                        if (BossPet.getByBoss(npcs[i].npcType) != null) {
                        	c.petKillLogs[BossPet.getByBoss(npcs[i].npcType).getSlot()] ++;
                        }
                        Slayer.Tasks t = Slayer.Tasks.forID(c.taskId);
                        if (t != null) {
                            for (int kk = 0; kk < t.npcId.length; kk++) {
                                if (t.npcId[kk] == npcs[i].npcType) {
                                    Slayer.kill(c, npcs[i]);
                                }
                            }
                        }
                        }
                        resetPlayersInCombat(i);
                        if (isFightCaveNpc(i))
                            killedTzhaar(i);
                    } else if (npcs[i].actionTimer == 0
                            && npcs[i].applyDead == true
                            && npcs[i].needRespawn == false) {
                        npcs[i].needRespawn = true;
                        npcs[i].actionTimer = getRespawnTime(i); // respawn time
                        npcs[i].absX = npcs[i].makeX;
                        npcs[i].absY = npcs[i].makeY;
                        npcs[i].HP = npcs[i].MaxHP;
                        npcs[i].animNumber = 0x328;
                        npcs[i].updateRequired = true;
                        npcs[i].animUpdateRequired = true;
                        new Dropper(npcs[i], PlayerHandler.players[npcs[i].killedBy]);
                        giveKillcount(PlayerHandler.players[npcs[i].killedBy], npcs[i].npcType);
                        if (npcs[i].npcType >= 2440 && npcs[i].npcType <= 2446) {
                            Server.objectManager.removeObject(npcs[i].absX,
                                    npcs[i].absY);
                        }

                    } else if (npcs[i].actionTimer == 0
                            && npcs[i].needRespawn == true) {
                        if (npcs[i].spawnedBy > 0 || npcs[i].summonedFor > 0) {
                            npcs[i] = null;
                        } else {
                            int old1 = npcs[i].npcType;
                            int old2 = npcs[i].makeX;
                            int old3 = npcs[i].makeY;
                            int old4 = npcs[i].heightLevel;
                            int old5 = npcs[i].walkingType;
                            int old6 = npcs[i].MaxHP;
                            int old7 = npcs[i].maxHit;
                            int old8 = npcs[i].attack;
                            int old9 = npcs[i].defence;
                            npcs[i] = null;
                            newNPC(old1, old2, old3, old4, old5, old6, old7,
                                    old8, old9);
                        }
                    }
                }
            }
        }
    }

    public void handleSpecialDeaths(Client c, int npc) {
    	if (c == null) {
    		return;
    	}
        for (int i = 0; i < Barrows.BROTHERS.length; i++) {
            if (npc == Barrows.BROTHERS[i][0]) {
                c.barrowsData[i] = Barrows.DEAD;
                Barrows.barrowsInterfaceUpdate(c);
            }
        }
    }

    public void handleAchievements(Client c, int npcId) {
        switch (npcId) {
            case 4261:
                AchievementHandler.add(c, 7, "easy", 1);
                break;
            case 81:
                AchievementHandler.add(c, 8, "easy", 1);
                break;
            case 41:
                AchievementHandler.add(c, 9, "easy", 1);
                break;
        }
    }

    /**
     * Npc killer id?
     **/
    public int getNpcKillerId(int npcId) {
        int oldDamage = 0;
        int killerId = 0;
        for (int p = 1; p < Configuration.MAX_PLAYERS; p++) {
            if (PlayerHandler.players[p] != null) {
                if (PlayerHandler.players[p].lastNpcAttacked == npcId) {
                    if (PlayerHandler.players[p].totalDamageDealt > oldDamage) {
                        oldDamage = PlayerHandler.players[p].totalDamageDealt;
                        killerId = p;
                    }
                    PlayerHandler.players[p].totalDamageDealt = 0;
                }
            }
        }
        return killerId;
    }

    /**
     * Resets players in combat
     */
    public void resetPlayersInCombat(int i) {
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null)
                if (PlayerHandler.players[j].underAttackBy2 == i)
                    PlayerHandler.players[j].underAttackBy2 = 0;
        }
    }

    /**
     * Npc Follow Player
     **/
    public static int GetMove(int Place1, int Place2) {
        if ((Place1 - Place2) == 0) {
            return 0;
        } else if ((Place1 - Place2) < 0) {
            return 1;
        } else if ((Place1 - Place2) > 0) {
            return -1;
        }
        return 0;
    }

    /**
     * NPC will not move if added here.
     */
    public boolean doNotfollowPlayer(int i) // Npc will never move if added here
    {
        switch (npcs[i].npcType) {
        case 6142://pest control portals
        case 6143:
        case 6144:
        case 6145:
            case 2892:
                // Spinolyp
            case 2894:
                // Spinolyp
            case 6247:
                // Commander Zilyana
                return false;
        }
        return true;
    }

    /**
     * NPC will follow the player.
     *
     * @param theNPC   The NPC following the player
     * @param playerId The player the NPC will follow.
     * @param pet      True, if the pet is the NPC. This will not make the pet face
     *                 the player all the time.
     */
    public void followPlayer(int theNPC, int playerId, boolean pet) {
        Client player = (Client) PlayerHandler.players[playerId];
        NPC NPC = npcs[theNPC];
        if (player == null) {
            return;
        }
        if (player.isDead) {
            NPC.facePlayer(0);
            NPC.randomWalk = true;
            NPC.underAttack = false;
            return;
        }
        if (!doNotfollowPlayer(theNPC)) {
            NPC.facePlayer(playerId);
            return;
        }
        int playerX = player.absX;
        int playerY = player.absY;
        NPC.randomWalk = false;
        if (goodDistance(NPC.getX(), NPC.getY(), playerX, playerY,
                distanceRequired(theNPC))) {
            return;
        }
        if ((NPC.spawnedBy > 0)
                || ((NPC.absX < NPC.makeX + Constants.NPC_FOLLOW_DISTANCE)
                && (NPC.absX > NPC.makeX
                - Constants.NPC_FOLLOW_DISTANCE)
                && (NPC.absY < NPC.makeY
                + Constants.NPC_FOLLOW_DISTANCE) && (NPC.absY > NPC.makeY
                - Constants.NPC_FOLLOW_DISTANCE))) {
            if (NPC.heightLevel == player.heightLevel) {
                if (player != null && NPC != null) {
                    if (playerY < NPC.absY) {
                        NPC.moveX = GetMove(NPC.absX, playerX);
                        NPC.moveY = GetMove(NPC.absY, playerY);
                    } else if (playerY > NPC.absY) {
                        NPC.moveX = GetMove(NPC.absX, playerX);
                        NPC.moveY = GetMove(NPC.absY, playerY);
                    } else if (playerX < NPC.absX) {
                        NPC.moveX = GetMove(NPC.absX, playerX);
                        NPC.moveY = GetMove(NPC.absY, playerY);
                    } else if (playerX > NPC.absX) {
                        NPC.moveX = GetMove(NPC.absX, playerX);
                        NPC.moveY = GetMove(NPC.absY, playerY);
                    } else if (playerX == NPC.absX || playerY == NPC.absY) {
                        int o = Misc.random(3);
                        switch (o) {
                            case 0:
                                NPC.moveX = GetMove(NPC.absX, playerX);
                                NPC.moveY = GetMove(NPC.absY, playerY + 1);
                                break;
                            case 1:
                                NPC.moveX = GetMove(NPC.absX, playerX);
                                NPC.moveY = GetMove(NPC.absY, playerY - 1);
                                break;
                            case 2:
                                NPC.moveX = GetMove(NPC.absX, playerX + 1);
                                NPC.moveY = GetMove(NPC.absY, playerY);
                                break;
                            case 3:
                                NPC.moveX = GetMove(NPC.absX, playerX - 1);
                                NPC.moveY = GetMove(NPC.absY, playerY);
                                break;
                        }
                    }
                    NPC.getNextNPCMovement(theNPC);
                    if (!pet) {
                        NPC.facePlayer(playerId);
                    }
                    NPC.updateRequired = true;
                }
            }
        } else {
            NPC.facePlayer(0);
            NPC.randomWalk = true;
            NPC.underAttack = false;
        }
    }

    /**
     * NPC Attacking Player
     **/
    public void attackPlayer(final Client player, int i) {
        if (npcs[i] != null) {
            if (npcs[i].isDead)
                return;
            if (!npcs[i].inMulti() && npcs[i].underAttackBy > 0
                    && npcs[i].underAttackBy != player.playerId) {
                npcs[i].killerId = 0;
                return;
            }
            if (!goodDistance(npcs[i].getX(), npcs[i].getY(), player.getX(), player.getY(), 1) && npcs[i].npcType == 8349 && npcs[i].attackType == 0)
            {
                npcs[i].attackType = 1+Misc.random(1);
                return;
            }
            if (!npcs[i].inMulti()
                    && (player.underAttackBy > 0 || (player.underAttackBy2 > 0 && player.underAttackBy2 != i))) {
                npcs[i].killerId = 0;
                return;
            }
            if (npcs[i].heightLevel != player.heightLevel) {
                npcs[i].killerId = 0;
                return;
            }
            npcs[i].facePlayer(player.playerId);
            boolean special = false; // specialCase(c,i);
            if (goodDistance(npcs[i].getX(), npcs[i].getY(), player.getX(),
                    player.getY(), distanceRequired(i)) || special) {
                if (!player.isDead) {

                    npcs[i].facePlayer(player.playerId);
                    npcs[i].attackTimer = getNpcDelay(i);
                    npcs[i].hitDelayTimer = getHitDelay(i);
                    npcs[i].attackType = 0;
                    loadSpell(i);
                    usingSpecial = false;
                    handleSpecialNPC(npcs[i]);
                    if (npcs[i].attackType == 3)
                        npcs[i].hitDelayTimer += 2;
                    if (multiAttacks(i)) {
                        multiAttackGfx(i, npcs[i].projectileId);
                        player.lastTimeEngagedNPC = System.currentTimeMillis();
                        startAnimation(getAttackEmote(i), i);
                        npcs[i].oldIndex = player.playerId;
                        return;
                    }
                    if (npcs[i].projectileId > 0) {
                        int nX = npcs[i].getX() + offset(i);
                        int nY = npcs[i].getY() + offset(i);
                        int pX = player.getX();
                        int pY = player.getY();
                        int offX = (nY - pY) * -1;
                        int offY = (nX - pX) * -1;
                        player.getPA().createPlayersProjectile(nX, nY, offX,
                                offY, 50, getProjectileSpeed(i),
                                npcs[i].projectileId, 43, 31,
                                -player.getId() - 1, 65);
                    }
                    player.underAttackBy2 = i;
                    player.singleCombatDelay2 = System.currentTimeMillis();
                    npcs[i].oldIndex = player.playerId;
                    player.lastTimeEngagedNPC = System.currentTimeMillis();
                    startAnimation(getAttackEmote(i), i);
                    player.getPA().removeAllWindows();
                }
            }
        }
    }

    public void absorbDragonFire(Client c) {
        if (c.playerEquipmentC[Constants.SHIELD_SLOT] < 50) {
            c.startAnimation(6695);
            c.gfx0(1164);
            c.playerEquipmentC[Constants.SHIELD_SLOT] += 1;
            c.sendMessage("You absorb the fire breath and charge your Dragonfire shield.");
        }
    }

    public boolean usingSpecial;

    private void handleSpecialNPC(NPC n) {
        if (Misc.random(2) != 0)
            return;
        switch (n.npcType) {
            case 10773:
                n.requestAnimation(13155);
                n.gfx0(1);
                n.attackType = 3;
                usingSpecial = true;
                break;
            default:
                break;
        }
    }

    /**
     * TODO: test
     */
    public int offset(int i) {
        switch (npcs[i].npcType) {
            case 2745:
                // TzTok-Jad
            case 2743:
            case 2881:
            case 2882:
            case 8349:
            case 8350:
            case 8351:
            case 8133:
            case 50:
                return 1;
            case 1158:
            case 1160:
                return 2;
            case 6260:
                return 5;

        }
        return 0;
    }

    public boolean specialCase(Client c, int i) { // responsible for npcs that
        // much
        if (goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(), c.getY(), 8)
                && !goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(),
                c.getY(), distanceRequired(i)))
            return true;
        return false;
    }

    public boolean retaliates(int npcType) {
        return npcType < 3777 || npcType > 3780
                && !(npcType >= 2440 && npcType <= 2446);
    }

    /**
     * Apply the damage on the player.
     *
     * @param i
     */
    public void applyDamage(int i) {
        if (npcs[i] != null) {
            if (PlayerHandler.players[npcs[i].oldIndex] == null) {
                return;
            }
            Client player = (Client) PlayerHandler.players[npcs[i].oldIndex];
            if (multiAttacks(i)) {
                multiAttackDamage(i);
                return;
            }
            if (npcs[i].npcType >= 6142 && npcs[i].npcType <= 6145) {
            	return;
            }
            if (player.playerIndex <= 0 && player.npcIndex <= 0)
                if (player.autoRet == 1)
                    player.npcIndex = i;
            if (player.attackTimer <= 3 || player.attackTimer == 0
                    && player.npcIndex == 0 && player.oldNpcIndex == 0
                    && !player.getDoingAgility()) {
                player.startAnimation(player.getCombat().getBlockAnimation());
            }
            if (!player.isDead) {
                int damage = 0;
                if (npcs[i].attackType == 0) {
                    damage = Misc.random(npcs[i].maxHit);
                    if (10 + Misc.random(player.getCombat()
                            .calculateMeleeDefence()) > Misc
                            .random(npcs[i].attack)) {
                        damage = 0;
                    }
                    if (player.prayerActive[18]
                            || player.curseActive[player.curses().DEFLECT_MELEE] && npcs[i].npcType != 2030) {
                        if (player.curseActive[player.curses().DEFLECT_MELEE])
                            player.curses().deflectNPC(npcs[i], 0, 0);
                        damage = 0;
                        if (damage <= 0) {
                            damage = 0;
                        }
                        damage = 0;
                    }
                    if (player.skillLevel[3] - damage < 0) {
                        damage = player.skillLevel[3];
                    }
                }
                if (npcs[i].attackType == 1) { // range
                    damage = Misc.random(npcs[i].maxHit);
                    if (10 + Misc.random(player.getCombat()
                            .calculateRangeDefence()) > Misc
                            .random(npcs[i].attack)) {
                        damage = 0;
                    }
                    if (player.prayerActive[17] || player.curseActive[player.curses().DEFLECT_MISSILES]) {
                        if (player.curseActive[player.curses().DEFLECT_MISSILES])
                            player.curses().deflectNPC(npcs[i], 0, 1);
                        damage = 0;
                        if (damage <= 0) {
                            damage = 0;
                        }
                    }
                    if (player.skillLevel[3] - damage < 0) {
                        damage = player.skillLevel[3];
                    }
                    if (npcs[i].endGfx > 0) {
                        player.gfx100(npcs[i].endGfx);
                    }
                }
                if (npcs[i].attackType == 2) { // magic
                    damage = Misc.random(npcs[i].maxHit);
                    boolean magicFailed = false;
                    if (10 + Misc.random(player.getCombat().mageDef()) > Misc
                            .random(npcs[i].attack)) {
                        damage = 0;
                        magicFailed = true;
                    }
                    if (player.prayerActive[16] || player.curseActive[player.curses().DEFLECT_MAGIC]) {
                        if (player.curseActive[player.curses().DEFLECT_MAGIC])
                            player.curses().deflectNPC(npcs[i], 0, 2);
                        damage = 0;
                        if (damage <= 0) {
                            damage = 0;
                            magicFailed = true;
                        }
                    }
                    if (player.skillLevel[3] - damage < 0) {
                        damage = player.skillLevel[3];
                    }
                    if (npcs[i].endGfx > 0
                            && (!magicFailed || isFightCaveNpc(i))) {
                        player.gfx100(npcs[i].endGfx);
                    } else {
                        player.gfx100(85);
                    }
                }
                if (npcs[i].attackType == 3) {
                    int anti = player.getPA().antiFire();
                    switch (anti) {
                        case 0:
                            if (player.prayerActive[16] || player.curseActive[player.curses().DEFLECT_MAGIC]) {
                                damage = Misc.random(30);
                                String s = player.prayerActive[16] ? "prayer" : "curse";
                                player.sendMessage("Your protect from magic " + s + " blocks some of the fire");
                            } else {
                                damage = Misc.random(46) + 13;
                                player.sendMessage("You are badly burnt by the dragon fire!");
                            }
                            break;
                        case 1:
                            if (ItemAssistant.hasItemEquipped(player, 1540)) {
                                damage = Misc.random(10);
                                player.sendMessage("Your shield protects you from the fire.");
                                if (player.prayerActive[16] || player.curseActive[player.curses().DEFLECT_MAGIC]) {
                                    damage = Misc.random(3);
                                }
                            } else if (ItemAssistant.hasItemEquipped(player, 11283)) {
                                damage = Misc.random(6);
                                if (Misc.random(3) == 0) {
                                    absorbDragonFire(player);
                                } else {
                                    player.sendMessage("Your shield protects you from most of the dragon fire");
                                }
                                if (player.prayerActive[16] || player.curseActive[player.curses().DEFLECT_MAGIC]) {
                                    damage = 0;
                                }
                            }
                            break;
                        case 2:
                            damage = Misc.random(0);
                            break;
                    }
                    if (player.skillLevel[3] - damage < 0) {
                        damage = player.skillLevel[3];
                    }
                    player.gfx100(npcs[i].endGfx);
                }
                handleSpecialEffects(player, i, damage);
                FightCaves.tzKihEffect(player, i, damage);
                player.logoutDelay = System.currentTimeMillis(); // logout delay
                if (npcs[i].attackType != 3) {
                    player.getCombat().appendHit(player, damage, 0,
                            npcs[i].attackType, false);
                } else if (npcs[i].attackType == 3) {
                    player.getCombat().appendHit(player, damage, 0, 7, false);
                }
                if (player.vengOn && damage > 0) {
                    breakVengeance(npcs[i], player, damage);
                }
            }
        }
    }

    public void handleSpecialEffects(Client c, int i, int damage) {
        if (npcs[i].npcType == 2892 || npcs[i].npcType == 2894) {
            if (damage > 0) {
                if (c != null) {
                    if (c.skillLevel[5] > 0) {
                        c.skillLevel[5]--;
                        c.getPA().refreshSkill(5);
                        Poison.appendPoison(c);
                    }
                }
            }
        }
    }

    public void startAnimation(int animId, int i) {
        npcs[i].animNumber = animId;
        npcs[i].animUpdateRequired = true;
        npcs[i].updateRequired = true;
    }

    public boolean goodDistance(int objectX, int objectY, int playerX,
                                int playerY, int distance) {
        for (int i = 0; i <= distance; i++) {
            for (int j = 0; j <= distance; j++) {
                if ((objectX + i) == playerX
                        && ((objectY + j) == playerY
                        || (objectY - j) == playerY || objectY == playerY)) {
                    return true;
                } else if ((objectX - i) == playerX
                        && ((objectY + j) == playerY
                        || (objectY - j) == playerY || objectY == playerY)) {
                    return true;
                } else if (objectX == playerX
                        && ((objectY + j) == playerY
                        || (objectY - j) == playerY || objectY == playerY)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean loadAutoSpawn(String FileName) {
        String line = "";
        String token = "";
        String token2 = "";
        String token2_2 = "";
        String[] token3 = new String[10];
        boolean EndOfFile = false;
        BufferedReader characterfile = null;
        try {
            characterfile = new BufferedReader(new FileReader("./" + FileName));
        } catch (FileNotFoundException fileex) {
            Misc.println(FileName + ": file not found.");
            return false;
        }
        try {
            line = characterfile.readLine();
        } catch (IOException ioexception) {
            Misc.println(FileName + ": error loading file.");
            // return false;
        }
        while (EndOfFile == false && line != null) {
            line = line.trim();
            int spot = line.indexOf("=");
            if (spot > -1) {
                token = line.substring(0, spot);
                token = token.trim();
                token2 = line.substring(spot + 1);
                token2 = token2.trim();
                token2_2 = token2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token3 = token2_2.split("\t");
                if (token.equals("spawn")) {
                    int npcId = Integer.parseInt(token3[0]);
                    newNPC(Integer.parseInt(token3[0]),
                            Integer.parseInt(token3[1]),
                            Integer.parseInt(token3[2]),
                            Integer.parseInt(token3[3]),
                            Integer.parseInt(token3[4]),
                            NPCData.data[npcId].hitPoints,
                            NPCData.data[npcId].maxHit,
                            NPCData.data[npcId].attackLevel,
                            NPCData.data[npcId].defenceLevel);
                    if (token3.length == 10) {
                    	lastNpc.defaultRotation = Direction.getByName(token3[8]);
                    }
                }
            } else {
                if (line.equals("[END]")) {
                    try {
                        characterfile.close();
                    } catch (IOException ioexception) {
                    }
                }
            }
            try {
                line = characterfile.readLine();
            } catch (IOException ioexception1) {
                EndOfFile = true;
            }
        }
        try {
            characterfile.close();
        } catch (IOException ioexception) {
        }
        return false;
    }

	public void loadSpawns() {
        for (int i = 0; i < maxNPCs; i++) {
            npcs[i] = null;
        }
        loadAutoSpawn("./data/npc/spawns.cfg");
    }


    private void stepAway(Client player, int i) {
        int otherX = npcs[i].getX();
        int otherY = npcs[i].getY();
        if (otherX == player.getX() && otherY == player.getY()) {
            if (Region.getClipping(player.getX() - 1, player.getY(),
                    player.heightLevel, -1, 0)) {
                npcs[i].moveX = -1;
            } else if (Region.getClipping(player.getX() + 1, player.getY(),
                    player.heightLevel, 1, 0)) {
                npcs[i].moveX = 1;
            } else if (Region.getClipping(player.getX(), player.getY() - 1,
                    player.heightLevel, 0, -1)) {
                npcs[i].moveY = -1;
            } else if (Region.getClipping(player.getX(), player.getY() + 1,
                    player.heightLevel, 0, 1)) {
                npcs[i].moveY = 1;
            }
            npcs[i].getNextNPCMovement(i);
            npcs[i].updateRequired = true;
        }
    }

    private void tzhaarDeathHandler(int i) {
        if (isFightCaveNpc(i) && npcs[i].npcType != FightCaves.TZ_KEK)
            killedTzhaar(i);
        if (npcs[i].npcType == FightCaves.TZ_KEK_SPAWN) {
            int p = npcs[i].killerId;
            if (PlayerHandler.players[p] != null) {
                Client c = (Client) PlayerHandler.players[p];
                c.tzKekSpawn += 1;
                if (c.tzKekSpawn == 2) {
                    killedTzhaar(i);
                    c.tzKekSpawn = 0;
                }
            }
        }
        if (npcs[i].npcType == FightCaves.TZ_KEK) {
            int p = npcs[i].killerId;
            if (PlayerHandler.players[p] != null) {
                Client c = (Client) PlayerHandler.players[p];
                FightCaves.tzKekEffect(c, i);
            }
        }
    }
    private void killedTzhaar(int i) {
        final Client c2 = (Client) PlayerHandler.players[npcs[i].spawnedBy];
        if (c2 == null) {
        	return;
        }
        c2.tzhaarKilled++;
        if (npcs[i].npcType == Server.fightCaves.TZ_KEK) {
        	FightCaves.tzKekEffect(c2, i);
        }
        if (c2.tzhaarKilled == c2.tzhaarToKill) {
            c2.waveId++;
            CycleEventHandler.getSingleton().addEvent(c2, new CycleEvent() {
                public void execute(CycleEventContainer c) {
                    if (!c2.disconnected) {
                        Server.fightCaves.spawnNextWave(c2);
                        c.stop();
                    }
                }
				@Override
				public void stop() {
				}
            }, 10);
        }
    }

    public void handleJadDeath(int i) {
        Client c = (Client) PlayerHandler.players[npcs[i].spawnedBy];
        ItemAssistant.addItem(c, 6570, 1);
        c.getPA().resetTzhaar();
        c.waveId = 300;
        for (int j = 0; j < PlayerHandler.players.length; j++) {
            if (PlayerHandler.players[j] != null) {
                Client c2 = (Client) PlayerHandler.players[j];
                c2.sendMessage("<col=255><shad=16711680>[News] " + Misc.optimizeText(c.playerName)
                        + " has completed the fight caves and has been awarded a fire cape.");
            }
        }
    }
    
    
}