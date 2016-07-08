package ionic.player;
import ionic.player.banking.BankConstants;
import ionic.player.content.miscellaneous.Preset;
import ionic.player.content.skills.summoning.SummoningData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

import utility.Misc;

public class PlayerSave
{
	
        public static int loadGame(Client player, String playerName, String playerPass)
        {
                String line = "";
                String token = "";
                String token2 = "";
                String[] token3 = new String[3];
                boolean EndOfFile = false;
                int ReadMode = 0;
                BufferedReader characterfile = null;
                boolean File1 = false;
                try
                {
                        characterfile = new BufferedReader(new FileReader("./data/characters/" + playerName + ".txt"));
                        File1 = true;
                }
                catch (FileNotFoundException fileex1)
                {
                }
                if (File1)
                {
                }
                else
                {
                        player.newPlayer = false;
                        return 0;
                }
                try
                {
                        line = characterfile.readLine();
                }
                catch (IOException ioexception)
                {
                        Misc.println(playerName + ": error loading file.");
                        return 3;
                }
                while (EndOfFile == false && line != null)
                {
                        line = line.trim();
                        int spot = line.indexOf("=");
                        if (spot > -1)
                        {
                                token = line.substring(0, spot);
                                token = token.trim();
                                token2 = line.substring(spot + 1);
                                token2 = token2.trim();
                                token3 = token2.split("\t");
                                switch (ReadMode)
                                {
                                case 1:
                                        if (token.equals("Password"))
                                        {
                                                if (playerPass.equalsIgnoreCase(token2))
                                                {
                                                        playerPass = token2;
                                                }
                                                else
                                                {
                                                        return 3;
                                                }
                                        }
                                        break;
                                case 2:
                                        if (token.equals("Height"))
                                        {
                                                player.heightLevel = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("X"))
                                        {
                                                player.teleportToX = (Integer.parseInt(token2) <= 0 ? 3086 : Integer.parseInt(token2));
                                        }
                                        else if (token.equals("Y"))
                                        {
                                                player.teleportToY = (Integer.parseInt(token2) <= 0 ? 3494 : Integer.parseInt(token2));
                                        }
                                        else if (token.equals("Authority"))
                                        {
                                                player.playerRights = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("LastIP"))
                                        {
                                                player.lastIP = token2;
                                        }
                                        else if (token.equals("expLock"))
                                        {
                                                player.expLock = Boolean.parseBoolean(token2);
                                        }
                                        else if (token.equals("prayerRenewal"))
                                        {
                                                player.prayerRenewal = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("pcPoints"))
                                        {
                                                player.pcPoints = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("duoPoints"))
                                        {
                                                player.duoPoints = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("pkPoints"))
                                        {
                                                player.pkPoints = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("cwGames"))
                                        {
                                                player.cwGames = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("pitWins"))
                                        {
                                                player.pitWins = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("skull-timer"))
                                        {
                                                player.skullTimer = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("timeUnMuted"))
                                        {
                                                player.timeUnMuted = Long.parseLong(token2);
                                        }
                                        else if (token.equals("timeUnBanned"))
                                        {
                                                player.timeUnBanned = Long.parseLong(token2);
                                        }
                                        else if (token.equals("evaluatorEnd"))
                                        {
                                            player.evaluatorEnd = Long.parseLong(token2);
                                        }
                                        else if (token.equals("offMessages"))
                                        {
                                                player.offMessages = Boolean.parseBoolean(token2);;
                                        }
                                        else if (token.equals("targetIndex"))
                                        {
                                                player.targetIndex = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("safeTimer"))
                                        {
                                                player.safeTimer = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("phatColor"))
                                        {
                                                player.phatColor = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("weenColor"))
                                        {
                                                player.weenColor = Integer.parseInt(token2);
                                        }
                                        
                                        else if (token.equals("famType"))
                                        {
                                                player.famType = SummoningData.getByName(token2);
                                        }
                                        else if (token.equals("famTime"))
                                        {
                                                player.familiarTime = Integer.parseInt(token2);
                                        }
                                        
                                        else if (token.equals("waveId"))
                                        {
                                                player.waveId = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("magic-book"))
                                        {
                                                player.playerMagicBook = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("overloaded"))
                                        {
                                                player.overloaded = Boolean.parseBoolean(token2);
                                        }
                                        else if (token.equals("Prayerbook"))
                                        {
                                                player.Prayerbook = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("Kill"))
                                        {
                                                player.killCount = Short.parseShort(token2);
                                        }
                                        else if (token.equals("Death"))
                                        {
                                                player.deathCount = Short.parseShort(token2);
                                        }
                                        else if (token.equals("cwKills"))
                                        {
                                                player.cwKills = Short.parseShort(token2);
                                        }
                                        else if (token.equals("cwDeaths"))
                                        {
                                                player.cwDeaths = Short.parseShort(token2);
                                        }
                                        else if (token.equals("safeKill"))
                                        {
                                                player.safeKill = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("safeDeath"))
                                        {
                                                player.safeDeath = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("itemPoints"))
                                        {
                                                player.itemPoints = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("chestLoots"))
                                        {
                                                player.chestLoots = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("chaoticCharges"))
                                        {
                                                player.chaoticCharges = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("pouchCoins"))
                                        {
                                                player.pouchCoins = new BigInteger(token2);
                                        }
                                        else if (token.equals("special-amount"))
                                        {
                                                player.specAmount = Double.parseDouble(token2);
                                        }
                                        else if (token.equals("teleblock-length"))
                                        {
                                                player.teleBlockDelay = System.currentTimeMillis();
                                                player.teleBlockLength = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("splitChat"))
                                        {
                                                player.splitChat = Boolean.parseBoolean(token2);
                                        }
                                        else if (token.equals("slayerPoints"))
                                        {
                                                player.slayerPoints = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("slayerStreak"))
                                        {
                                                player.slayerStreak = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("taskId"))
                                        {
                                                player.taskId = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("taskAmount"))
                                        {
                                                player.taskAmount = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("foodEverAte"))
                                        {
                                                player.foodEverAte = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("attacksDealt"))
                                        {
                                                player.attacksGiven = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("attacksDealted"))
                                        {
                                                player.attacksReceived = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("switches"))
                                        {
                                                player.switches = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("potionDrank"))
                                        {
                                                player.potionDrank = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("bossKill"))
                                        {
                                                player.bossKill = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("killStreaks"))
                                        {
                                                player.killStreak = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("killStreaksRecord"))
                                        {
                                                player.killStreakRecord = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("Title"))
                                        {
                                                player.title = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("canSetTitle"))
                                        {
                                                player.canSetTitle = Boolean.parseBoolean(token2);
                                        }
                                        else if (token.equals("evaluatorTrial"))
                                        {
                                                player.evaluatorTrial = Boolean.parseBoolean(token2);
                                        }
                                        else if (token.equals("petID"))
                                        {
                                                player.petID = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("petSummoned"))
                                        {
                                                player.setPetSummoned(Boolean.parseBoolean(token2));
                                        }
                                        else if (token.equals("whiteSkull"))
                                        {
                                            player.setWhiteSkull(Boolean.parseBoolean(token2));
                                        }
                                        else if (token.equals("redSkull"))
                                        {
                                                player.setRedSkull(Boolean.parseBoolean(token2));
                                        }
                                        else if (token.equals("achievementPoint"))
                                        {
                                                player.achievementPoint = Short.parseShort(token2);
                                        }
                                        else if (token.equals("voteTotalPoints"))
                                        {
                                                player.voteTotalPoints = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("votePoints"))
                                        {
                                                player.votePoints = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("autoRet"))
                                        {
                                                player.autoRet = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("isRunning2"))
                                        {
                                                player.isRunning2 = Boolean.parseBoolean(token2);
                                        }
                                        else if (token.equals("fightMode"))
                                        {
                                                player.fightMode = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("runEnergy"))
                                        {
                                                player.runEnergy = Double.parseDouble(token2);
                                        }
                                        else if (token.equals("brightness")) 
                                        {
                                        	player.brightnessLevel = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("fogLevel")) 
                                        {
                                        	player.fogIntensity = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("gotStarter")) 
                                        {
                                        	player.gotStarter = Boolean.parseBoolean(token2);
                                        }
                                        else if (token.equals("barrowsCrypt")) 
                                        {
                                        	player.barrowsCrypt = Integer.parseInt(token2);
                                        }else if (token.equals("tunnelX")) 
                                        {
                                        	player.tunnelX = Integer.parseInt(token2);
                                        }else if (token.equals("tunnelY")) 
                                        {
                                        	player.tunnelY = Integer.parseInt(token2);
                                        }else if (token.equals("lootedChest")) 
                                        {
                                        	player.lootedChest = Boolean.parseBoolean(token2);
                                        }else if (token.equals("monstersKilled")) 
                                        {
                                        	player.monstersKilled = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("pouchSize")) 
                                        {
                                        	player.pouchSize = Integer.parseInt(token2);
                                        }
                                        else if (token.equals("essence")) {
                                            for (int j = 0; j < token3.length; j++) {
                                                    player.essence[j] = Integer.parseInt(token3[j]);
                                            }
                                        } else if (token.equals("tabNames")) {
                                            for (int j = 0; j < token3.length; j++) {
                                                player.tabNames[j] = token3[j];
                                        }
                                    }
                                        else if (token.equals("QuickCurses")) {
                                                for (int j = 0; j < token3.length; j++) {
                                                        player.quickCurses[j] = Boolean.parseBoolean(token3[j]);
                                                }
                                        }
                                        else if (token.equals("QuickPrayers")) {
                                                for (int j = 0; j < token3.length; j++) {
                                                        player.quickPrayers[j] = Boolean.parseBoolean(token3[j]);
                                                }
                                        }
                                        
                                        else if (token.equals("famI"))
                                        {
                                            for (int j = 0; j < token3.length; j++)
                                            {
                                                    player.bobItems[j] = Integer.parseInt(token3[j]);
                                            }
                                        }
                                        else if (token.equals("famN"))
                                        {
                                            for (int j = 0; j < token3.length; j++)
                                            {
                                                    player.bobItemsN[j] = Integer.parseInt(token3[j]);
                                            }
                                        }

                                        if (token.equals("easyProgress")) {
                                        	for (int j = 0; j < token3.length; j++) {
                                                player.easyProgress[j] = Integer.parseInt(token3[j]);
                                        	}
                                        }
                                        if (token.equals("mediumProgress")) {
                                        	for (int j = 0; j < token3.length; j++) {
                                                player.mediumProgress[j] = Integer.parseInt(token3[j]);
                                        	}
                                        }
                                        if (token.equals("hardProgress")) {
                                        	for (int j = 0; j < token3.length; j++) {
                                                player.hardProgress[j] = Integer.parseInt(token3[j]);
                                        	}
                                        }
                                        if (token.equals("eliteProgress")) {
                                        	for (int j = 0; j < token3.length; j++) {
                                                player.eliteProgress[j] = Integer.parseInt(token3[j]);
                                        	}
                                        }
                                        if (token.equals("barrowsData")) {
                                        	for (int j = 0; j < token3.length; j++) {
                                                player.barrowsData[j] = Integer.parseInt(token3[j]);
                                        	}
                                        }
                                        if (token.equals("gwdKC")) {
                                        	for (int j = 0; j < token3.length; j++) {
                                                player.gwdKC[j] = Integer.parseInt(token3[j]);
                                        	}
                                        }
                                        if (token.equals("compColorsRGB")) {
                                        	for (int j = 0; j < token3.length; j++) {
                                                player.compColorsRGB[j] = Integer.parseInt(token3[j]);
                                        	}
                                        }
                                        if (token.equals("compColor")) {
                                        	for (int j = 0; j < token3.length; j++) {
                                                player.compColor[j] = Integer.parseInt(token3[j]);
                                        	}
                                        }
                                        if (token.equals("compPreset1")) {
                                        	for (int j = 0; j < token3.length; j++) {
                                                player.compPreset[0][j] = Integer.parseInt(token3[j]);
                                        	}
                                        }
                                        if (token.equals("compPreset2")) {
                                        	for (int j = 0; j < token3.length; j++) {
                                                player.compPreset[1][j] = Integer.parseInt(token3[j]);
                                        	}
                                        }
                                        if (token.equals("compPreset3")) {
                                        	for (int j = 0; j < token3.length; j++) {
                                                player.compPreset[2][j] = Integer.parseInt(token3[j]);
                                        	}
                                        }
                                        if (token.equals("Preset")) {
                                        	Preset.load(player, line);
                                        }
                                        if (token.equals("geSlots")) {
                                            for (int j = 0; j < token3.length; j++) {
                                                    player.getPD().geSlots[j] = Integer.parseInt(token3[j]);
                                            }
                                        }
                                        break;
                                case 3:
                                        if (token.equals("character-equip"))
                                        {
                                                player.playerEquipment[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
                                                player.playerEquipmentN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
                                                player.playerEquipmentC[Integer.parseInt(token3[0])] = Integer.parseInt(token3[3]);
                                        }
                                        break;
                                case 4:
                                        if (token.equals("character-look"))
                                        {
                                                player.playerAppearance[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
                                        }
                                        break;
                                case 5:
                                        if (token.equals("character-skill"))
                                        {
                                                player.skillLevel[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
                                                player.playerXP[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
                                                player.announced99[Integer.parseInt(token3[0])] = Boolean.parseBoolean(token3[3]);
                                                player.announced100m[Integer.parseInt(token3[0])] = Boolean.parseBoolean(token3[4]);
                                                player.announced200m[Integer.parseInt(token3[0])] = Boolean.parseBoolean(token3[5]);
                                        }
                                        break;
                                case 6:
                                        if (token.equals("inventory-slot"))
                                        {
                                                player.playerItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
                                                player.playerItemsN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
                                                player.playerItemsC[Integer.parseInt(token3[0])] = Integer.parseInt(token3[3]);
                                        }
                                        break;
                                case 7:
                                	if (token.equalsIgnoreCase("bank-item")) {
                                		int tab = Integer.parseInt(token3[0]);
                                		int slot = Integer.parseInt(token3[1]);
                                		int item = Integer.parseInt(token3[2]);
                                		int amount = Integer.parseInt(token3[3]);
                                		int charge = Integer.parseInt(token3[4]);
                                		player.getBank().bankItems[tab][slot] = item;
                                		player.getBank().bankAmounts[tab][slot] = amount;
                                		player.getBank().bankCharges[tab][slot] = charge;
                                	}
                                        break;
                                case 8:
                                        if (token.equals("character-friend"))
                                        {
                                                player.friends[Integer.parseInt(token3[0])] = Long.parseLong(token3[1]);
                                        }
                                        break;
                                case 9:
                                        if (token.equals("character-ignore"))
                                        {
                                                player.ignores[Integer.parseInt(token3[0])] = Long.parseLong(token3[1]);
                                        }
                                        break;
                                        case 10:
                                                if (token.startsWith("allotment")) {
                                                        int index = Integer.parseInt(token.split("allotment")[1]);
                                                        player.getAllotment().allotmentHarvest[index] = Integer.parseInt(token3[0]);
                                                        player.getAllotment().allotmentSeeds[index] = Integer.parseInt(token3[1]);
                                                        player.getAllotment().allotmentStages[index] = Integer.parseInt(token3[2]);
                                                        player.getAllotment().allotmentState[index] = Integer.parseInt(token3[3]);
                                                        player.getAllotment().allotmentTimer[index] = Long.parseLong(token3[4]);
                                                        player.getAllotment().diseaseChance[index] = Double.parseDouble(token3[5]);
                                                        player.getAllotment().hasFullyGrown[index] = Boolean.parseBoolean(token3[6]);
                                                } else if (token.startsWith("bush")) {
                                                        int i = Integer.parseInt(token.split("bush")[1]);
                                                        player.getBushes().bushesSeeds[i] = Integer.parseInt(token3[0]);
                                                        player.getBushes().bushesStages[i] = Integer.parseInt(token3[1]);
                                                        player.getBushes().bushesState[i] = Integer.parseInt(token3[2]);
                                                        player.getBushes().bushesTimer[i] = Long.parseLong(token3[3]);
                                                        player.getBushes().diseaseChance[i] = Double.parseDouble(token3[4]);
                                                        player.getBushes().hasFullyGrown[i] = Boolean.parseBoolean(token3[5]);
                                                } else if (token.startsWith("herb")) {
                                                        int i = Integer.parseInt(token.split("herb")[1]);
                                                        player.getHerbs().herbHarvest[i] = Integer.parseInt(token3[0]);
                                                        player.getHerbs().herbSeeds[i] = Integer.parseInt(token3[1]);
                                                        player.getHerbs().herbStages[i] = Integer.parseInt(token3[2]);
                                                        player.getHerbs().herbState[i] = Integer.parseInt(token3[3]);
                                                        player.getHerbs().herbTimer[i] = Long.parseLong(token3[4]);
                                                        player.getHerbs().diseaseChance[i] = Double.parseDouble(token3[5]);
                                                } else if (token.startsWith("tree")) {
                                                        int i = Integer.parseInt(token.split("tree")[1]);
                                                        player.getTrees().treeHarvest[i] = Integer.parseInt(token3[0]);
                                                        player.getTrees().treeSaplings[i] = Integer.parseInt(token3[1]);
                                                        player.getTrees().treeStages[i] = Integer.parseInt(token3[2]);
                                                        player.getTrees().treeState[i] = Integer.parseInt(token3[3]);
                                                        player.getTrees().treeTimer[i] = Long.parseLong(token3[4]);
                                                        player.getTrees().diseaseChance[i] = Double.parseDouble(token3[5]);
                                                        player.getTrees().hasFullyGrown[i] = Boolean.parseBoolean(token3[6]);
                                                } else if (token.startsWith("fruitTree")) {
                                                        int i = Integer.parseInt(token.split("fruitTree")[1]);
                                                        player.getFruitTrees().fruitTreeSaplings[i] = Integer.parseInt(token3[0]);
                                                        player.getFruitTrees().fruitTreeStages[i] = Integer.parseInt(token3[1]);
                                                        player.getFruitTrees().fruitTreeState[i] = Integer.parseInt(token3[2]);
                                                        player.getFruitTrees().fruitTreeTimer[i] = Long.parseLong(token3[3]);
                                                        player.getFruitTrees().diseaseChance[i] = Double.parseDouble(token3[4]);
                                                        player.getFruitTrees().hasFullyGrown[i] = Boolean.parseBoolean(token3[5]);
                                                } else if (token.startsWith("hop")) {
                                                        int i = Integer.parseInt(token.split("hop")[1]);
                                                        player.getHops().hopsHarvest[i] = Integer.parseInt(token3[0]);
                                                        player.getHops().hopsSeeds[i] = Integer.parseInt(token3[1]);
                                                        player.getHops().hopsStages[i] = Integer.parseInt(token3[2]);
                                                        player.getHops().hopsState[i] = Integer.parseInt(token3[3]);
                                                        player.getHops().hopsTimer[i] = Long.parseLong(token3[4]);
                                                        player.getHops().diseaseChance[i] = Double.parseDouble(token3[5]);
                                                        player.getHops().hasFullyGrown[i] = Boolean.parseBoolean(token3[6]);
                                                } else if (token.startsWith("flower")) {
                                                        int i = Integer.parseInt(token.split("flower")[1]);
                                                        player.getFlowers().flowerSeeds[i] = Integer.parseInt(token3[0]);
                                                        player.getFlowers().flowerStages[i] = Integer.parseInt(token3[1]);
                                                        player.getFlowers().flowerState[i] = Integer.parseInt(token3[2]);
                                                        player.getFlowers().flowerTimer[i] = Long.parseLong(token3[3]);
                                                        player.getFlowers().diseaseChance[i] = Double.parseDouble(token3[4]);
                                                        player.getFlowers().hasFullyGrown[i] = Boolean.parseBoolean(token3[5]);
                                                }
                                                break;
                                }
                        }
                        else
                        {
                                if (line.equals("[CREDENTIALS]"))
                                {
                                        ReadMode = 1;
                                }
                                else if (line.equals("[MAIN]"))
                                {
                                        ReadMode = 2;
                                }
                                else if (line.equals("[AGILITY]"))
                                {
                                        ReadMode = 2;
                                }
                                else if (line.equals("[EQUIPMENT]"))
                                {
                                        ReadMode = 3;
                                }
                                else if (line.equals("[APPEARANCE]"))
                                {
                                        ReadMode = 4;
                                }
                                else if (line.equals("[SKILLS]"))
                                {
                                        ReadMode = 5;
                                }
                                else if (line.equals("[INVENTORY]"))
                                {
                                        ReadMode = 6;
                                }
                                else if (line.equals("[BANK]"))
                                {
                                        ReadMode = 7;
                                }
                                else if (line.equals("[FRIENDS]"))
                                {
                                        ReadMode = 8;
                                }
                                else if (line.equals("[IGNORES]"))
                                {
                                        ReadMode = 9;
                                }
                                else if (line.equals("[FARMING]"))
                                {
                                        ReadMode = 10;
                                }
                                else if (line.equals("[EOF]"))
                                {
                                        try
                                        {
                                                characterfile.close();
                                        }
                                        catch (IOException ioexception)
                                        {
                                        }
                                        return 1;
                                }
                        }
                        try
                        {
                                line = characterfile.readLine();
                        }
                        catch (IOException ioexception1)
                        {
                                EndOfFile = true;
                        }
                }
                try
                {
                        characterfile.close();
                }
                catch (IOException ioexception)
                {
                }
                return 13;
        }
        
        public static boolean saveGame(Client player)
        {
                if (!player.saveFile || player.newPlayer || !player.saveCharacter)
                {
                        return false;
                }
                if (player.playerName == null || PlayerHandler.players[player.playerId] == null)
                {
                        return false;
                }
                player.playerName = player.playerName2;
                int tbTime = (int)(player.teleBlockDelay - System.currentTimeMillis() + player.teleBlockLength);
                if (tbTime > 300000 || tbTime < 0)
                {
                        tbTime = 0;
                }
                BufferedWriter characterfile = null;
                try
                {
                        characterfile = new BufferedWriter(new FileWriter("./data/characters/" + player.playerName + ".txt"));
                        characterfile.write("[CREDENTIALS]", 0, 13);
                        characterfile.newLine();
                        characterfile.write("Username = ", 0, 11);
                        characterfile.write(player.playerName, 0, player.playerName.length());
                        characterfile.newLine();
                        characterfile.write("Password = ", 0, 11);
                        characterfile.write(player.playerPass, 0, player.playerPass.length());
                        characterfile.newLine();
                        characterfile.newLine();
                        characterfile.write("[MAIN]", 0, 6);
                        characterfile.newLine();
                        characterfile.write("LastIP = ", 0, 9);
                        characterfile.write(player.lastIP, 0, player.lastIP.length());
                        characterfile.newLine();
                        characterfile.write("Authority = ", 0, 12);
                        characterfile.write(Integer.toString(player.playerRights), 0, Integer.toString(player.playerRights).length());
                        characterfile.newLine();
                        characterfile.write("secondsOnline = ", 0, 16);
                        characterfile.write(Integer.toString(player.secondsOnline), 0, Integer.toString(player.secondsOnline).length());
                        characterfile.newLine();
                        characterfile.newLine();
                        characterfile.write("[OTHER]", 0, 7);
                        characterfile.newLine();
                        characterfile.write("Height = ", 0, 9);
                        characterfile.write(Integer.toString(player.heightLevel), 0, Integer.toString(player.heightLevel).length());
                        characterfile.newLine();
                        characterfile.write("X = ", 0, 4);
                        characterfile.write(Integer.toString(player.absX), 0, Integer.toString(player.absX).length());
                        characterfile.newLine();
                        characterfile.write("Y = ", 0, 4);
                        characterfile.write(Integer.toString(player.absY), 0, Integer.toString(player.absY).length());
                        characterfile.newLine();
                        characterfile.write("expLock = ", 0, 10);
                        characterfile.write(Boolean.toString(player.expLock), 0, Boolean.toString(player.expLock).length());
                        characterfile.newLine();
                        characterfile.write("isRunning2 = ", 0, 13);
                        characterfile.write(Boolean.toString(player.isRunning2), 0, Boolean.toString(player.isRunning2).length());
                        characterfile.newLine();
                        characterfile.write("skull-timer = ", 0, 14);
                        characterfile.write(Integer.toString(player.skullTimer), 0, Integer.toString(player.skullTimer).length());
                        characterfile.newLine();
                        characterfile.write("magic-book = ", 0, 13);
                        characterfile.write(Integer.toString(player.playerMagicBook), 0, Integer.toString(player.playerMagicBook).length());
                        characterfile.newLine();
                        characterfile.write("Prayerbook = ", 0, 13);
                        characterfile.write(Integer.toString(player.Prayerbook), 0, Integer.toString(player.Prayerbook).length());
                        characterfile.newLine();
                        characterfile.write("waveId = ", 0, 9);
                        characterfile.write(Integer.toString(player.waveId), 0, Integer.toString(player.waveId).length());
                        characterfile.newLine();
                        characterfile.write("pitWins = ", 0, 10);
                        characterfile.write(Integer.toString(player.pitWins), 0, Integer.toString(player.pitWins).length());
                        characterfile.newLine();
                        characterfile.write("prayerRenewal = ", 0, 16);
                        characterfile.write(Integer.toString(player.prayerRenewal), 0, Integer.toString(player.prayerRenewal).length());
                        characterfile.newLine();
                        characterfile.write("cwGames =", 0, 9 );
                        characterfile.write(Integer.toString(player.cwGames), 0, Integer.toString(player.cwGames).length());
                        characterfile.newLine();
                        if (player.famType != null) {
	                        characterfile.write("famType = ", 0, 10);
	                        characterfile.write(player.famType.toString(), 0, player.famType.toString().length());
	                        characterfile.newLine();
                        }
                        characterfile.write("famTime = ", 0, 10);
                        characterfile.write(Integer.toString(player.familiarTime), 0, Integer.toString(player.familiarTime).length());
                        characterfile.newLine();
                        characterfile.write("pcPoints = ", 0, 11);
                        characterfile.write(Integer.toString(player.pcPoints), 0, Integer.toString(player.pcPoints).length());
                        characterfile.newLine();
                        characterfile.write("duoPoints = ", 0, 12);
                        characterfile.write(Integer.toString(player.duoPoints), 0, Integer.toString(player.duoPoints).length());
                        characterfile.newLine();
                        characterfile.write("pkPoints = ", 0, 11);
                        characterfile.write(Integer.toString(player.pkPoints), 0, Integer.toString(player.pcPoints).length());
                        characterfile.newLine();
                        characterfile.write("Kill = ", 0, 7);
                        characterfile.write(Integer.toString(player.killCount), 0, Integer.toString(player.killCount).length());
                        characterfile.newLine();
                        characterfile.write("Death = ", 0, 8);
                        characterfile.write(Integer.toString(player.deathCount), 0, Integer.toString(player.deathCount).length());
                        characterfile.newLine();
                        characterfile.write("cwKills = ", 0, 10);
                        characterfile.write(Integer.toString(player.cwKills), 0, Integer.toString(player.cwKills).length());
                        characterfile.newLine();
                        characterfile.write("cwDeaths = ", 0, 11);
                        characterfile.write(Integer.toString(player.cwDeaths), 0, Integer.toString(player.cwDeaths).length());
                        characterfile.newLine();
                        characterfile.write("chestLoots = ", 0, 13);
                        characterfile.write(Integer.toString(player.chestLoots), 0, Integer.toString(player.chestLoots).length());
                        characterfile.newLine();
                        characterfile.write("safeKill = ", 0, 11);
                        characterfile.write(Integer.toString(player.safeKill), 0, Integer.toString(player.safeKill).length());
                        characterfile.newLine();
                        characterfile.write("safeDeath = ", 0, 12);
                        characterfile.write(Integer.toString(player.safeDeath), 0, Integer.toString(player.safeDeath).length());
                        characterfile.newLine();
                        characterfile.write("pouchCoins = ", 0, 13);
                        characterfile.write(player.pouchCoins.toString(), 0, player.pouchCoins.toString().length());
                        characterfile.newLine();
                        characterfile.write("killStreak = ", 0, 13);
                        characterfile.write(Integer.toString(player.killStreak), 0, Integer.toString(player.killStreak).length());
                        characterfile.newLine();
                        characterfile.write("offMessages = ", 0, 14);
                        characterfile.write(Boolean.toString(player.offMessages), 0, Boolean.toString(player.offMessages).length());
                        characterfile.newLine();
                        characterfile.write("special-amount = ", 0, 17);
                        characterfile.write(Double.toString(player.specAmount), 0, Double.toString(player.specAmount).length());
                        characterfile.newLine();
                        characterfile.write("teleblock-length = ", 0, 19);
                        characterfile.write(Integer.toString(tbTime), 0, Integer.toString(tbTime).length());
                        characterfile.newLine();
                        characterfile.write("splitChat = ", 0, 12);
                        characterfile.write(Boolean.toString(player.splitChat), 0, Boolean.toString(player.splitChat).length());
                        characterfile.newLine();
                        characterfile.write("autoRet = ", 0, 10);
                        characterfile.write(Integer.toString(player.autoRet), 0, Integer.toString(player.autoRet).length());
                        characterfile.newLine();
                        characterfile.write("overloaded = ", 0, 13);
                        characterfile.write(Boolean.toString(player.overloaded), 0, Boolean.toString(player.overloaded).length());
                        characterfile.newLine();
                        characterfile.write("evaluatorTrial = ", 0, 17);
                        characterfile.write(Boolean.toString(player.evaluatorTrial), 0, Boolean.toString(player.evaluatorTrial).length());
                        characterfile.newLine();
                        characterfile.write("fightMode = ", 0, 12);
                        characterfile.write(Integer.toString(player.fightMode), 0, Integer.toString(player.fightMode).length());
                        characterfile.newLine();
                        characterfile.write("attacksDealt = ", 0, 15);
                        characterfile.write(Integer.toString(player.attacksGiven), 0, Integer.toString(player.attacksGiven).length());
                        characterfile.newLine();
                        characterfile.write("attacksDealted = ", 0, 17);
                        characterfile.write(Integer.toString(player.attacksReceived), 0, Integer.toString(player.attacksReceived).length());
                        characterfile.newLine();
                        characterfile.write("phatColor = ", 0, 12);
                        characterfile.write(Integer.toString(player.phatColor), 0, Integer.toString(player.phatColor).length());
                        characterfile.newLine();
                        characterfile.write("weenColor = ", 0, 12);
                        characterfile.write(Integer.toString(player.weenColor), 0, Integer.toString(player.weenColor).length());
                        characterfile.newLine();
                        characterfile.write("slayerPoints = ", 0, 15);
                        characterfile.write(Integer.toString(player.slayerPoints), 0, Integer.toString(player.slayerPoints).length());
                        characterfile.newLine();
                        characterfile.write("slayerStreak = ", 0, 15);
                        characterfile.write(Integer.toString(player.slayerStreak), 0, Integer.toString(player.slayerStreak).length());
                        characterfile.newLine();
                        characterfile.write("taskId = ", 0, 9);
                        characterfile.write(Integer.toString(player.taskId), 0, Integer.toString(player.taskId).length());
                        characterfile.newLine();
                        characterfile.write("taskAmount = ", 0, 13);
                        characterfile.write(Integer.toString(player.taskAmount), 0, Integer.toString(player.taskAmount).length());
                        characterfile.newLine();
                        if (player.targetName != null)
                        {
                                characterfile.write("targetName = ", 0, 13);
                                characterfile.write(player.targetName, 0, player.targetName.length());
                                characterfile.newLine();
                        }
                        characterfile.write("switches = ", 0, 11);
                        characterfile.write(Integer.toString(player.switches), 0, Integer.toString(player.switches).length());
                        characterfile.newLine();
                        characterfile.write("achievementPoint = ", 0, 19);
                        characterfile.write(Integer.toString(player.achievementPoint), 0, Integer.toString(player.achievementPoint).length());
                        characterfile.newLine();
                        characterfile.write("voteTotalPoints = ", 0, 18);
                        characterfile.write(Integer.toString(player.voteTotalPoints), 0, Integer.toString(player.voteTotalPoints).length());
                        characterfile.newLine();
                        characterfile.write("votePoints = ", 0, 13);
                        characterfile.write(Integer.toString(player.votePoints), 0, Integer.toString(player.votePoints).length());
                        characterfile.newLine();
                        characterfile.write("foodEverAte = ", 0, 14);
                        characterfile.write(Integer.toString(player.foodEverAte), 0, Integer.toString(player.foodEverAte).length());
                        characterfile.newLine();
                        characterfile.write("potionDrank = ", 0, 14);
                        characterfile.write(Integer.toString(player.potionDrank), 0, Integer.toString(player.potionDrank).length());
                        characterfile.newLine();
                        characterfile.write("bossKill = ", 0, 11);
                        characterfile.write(Integer.toString(player.bossKill), 0, Integer.toString(player.bossKill).length());
                        characterfile.newLine();
                        characterfile.write("Title = ", 0, 8);
                        characterfile.write(Integer.toString(player.title), 0, Integer.toString(player.title).length());
                        characterfile.newLine();
                        characterfile.write("canSetTitle = ", 0, 14);
                        characterfile.write(Boolean.toString(player.canSetTitle), 0, Boolean.toString(player.canSetTitle).length());
                        characterfile.newLine();
                        characterfile.write("gotStarter = ", 0, 13);
                        characterfile.write(Boolean.toString(player.gotStarter), 0, Boolean.toString(player.gotStarter).length());
                        characterfile.newLine();
                        characterfile.write("killStreakRecord = ", 0, 19);
                        characterfile.write(Integer.toString(player.killStreakRecord), 0, Integer.toString(player.killStreakRecord).length());
                        characterfile.newLine();
                        characterfile.write("petSummoned = ", 0, 14);
                        characterfile.write(Boolean.toString(player.getPetSummoned()), 0, Boolean.toString(player.getPetSummoned()).length());
                        characterfile.newLine();
                        characterfile.write("petID = ", 0, 8);
                        characterfile.write(Integer.toString(player.petID), 0, Integer.toString(player.petID).length());
                        characterfile.newLine();
                        characterfile.write("runEnergy = ", 0, 12);
                        characterfile.write(Double.toString(player.runEnergy), 0, Double.toString(player.runEnergy).length());
                        characterfile.newLine();
                        characterfile.write("brightness = ", 0, 13);
                        characterfile.write(Integer.toString(player.brightnessLevel), 0, Integer.toString(player.brightnessLevel).length());
                        characterfile.newLine();
                        characterfile.write("fogLevel = ", 0, 11);
                        characterfile.write(Integer.toString(player.fogIntensity), 0, Integer.toString(player.fogIntensity).length());
                        characterfile.newLine();
                        characterfile.write("itemPoints = ", 0, 13);
                        characterfile.write(Integer.toString(player.getItemPoints()), 0, Integer.toString(player.getItemPoints()).length());
                        characterfile.newLine();
                        characterfile.write("whiteSkull = ", 0, 13);
                        characterfile.write(Boolean.toString(player.getWhiteSkull()), 0, Boolean.toString(player.getWhiteSkull()).length());
                        characterfile.newLine();
                        characterfile.write("timeUnMuted = ", 0, 14);
                        characterfile.write(Long.toString(player.timeUnMuted), 0, Long.toString(player.timeUnMuted).length());
                        characterfile.newLine();
                        characterfile.write("timeUnBanned = ", 0, 15);
                        characterfile.write(Long.toString(player.timeUnBanned), 0, Long.toString(player.timeUnBanned).length());
                        characterfile.newLine();
                        characterfile.write("evaluatorEnd = ", 0, 15);
                        characterfile.write(Long.toString(player.evaluatorEnd), 0, Long.toString(player.evaluatorEnd).length());
                        characterfile.newLine();
                        characterfile.write("chaoticCharges = ", 0, 17);
                        characterfile.write(Integer.toString(player.chaoticCharges), 0, Integer.toString(player.chaoticCharges).length());
                        characterfile.newLine();
                        characterfile.write("pouchSize = ", 0, 12);
                        characterfile.write(Integer.toString(player.pouchSize), 0, Integer.toString(player.pouchSize).length());
                        characterfile.newLine();
                        characterfile.write("barrowsCrypt = ", 0, 15);
                        characterfile.write(Integer.toString(player.barrowsCrypt), 0, Integer.toString(player.barrowsCrypt).length());
                        characterfile.newLine();
                        characterfile.write("tunnelX = ", 0, 10);
                        characterfile.write(Integer.toString(player.tunnelX), 0, Integer.toString(player.tunnelX).length());
                        characterfile.newLine();
                        characterfile.write("tunnelY = ", 0, 10);
                        characterfile.write(Integer.toString(player.tunnelY), 0, Integer.toString(player.tunnelY).length());
                        characterfile.newLine();
                        characterfile.write("lootedChest = ", 0, 14);
                        characterfile.write(Boolean.toString(player.lootedChest), 0, Boolean.toString(player.lootedChest).length());
                        characterfile.newLine();
                        characterfile.write("monstersKilled = ", 0, 17);
                        characterfile.write(Integer.toString(player.monstersKilled), 0, Integer.toString(player.monstersKilled).length());
                        characterfile.newLine();
                        
                        characterfile.write("famI = ", 0, 7);
                        String toWrite8 = "";
                        for (int i1 = 0; i1 < player.bobItems.length; i1++)
                        {
                                toWrite8 += player.bobItems[i1] + "\t";
                        }
                        characterfile.write(toWrite8);
                        characterfile.newLine();
                        characterfile.write("famN = ", 0, 7);
                        String toWrite9 = "";
                        for (int i1 = 0; i1 < player.bobItemsN.length; i1++)
                        {
                                toWrite9 += player.bobItemsN[i1] + "\t";
                        }
                        characterfile.write(toWrite9);
                        characterfile.newLine();
                        
                        characterfile.write("essence = ", 0, 10);
                        String tw46 = "";
                        for (int i1 = 0; i1 < player.essence.length; i1++) {
                                tw46 += player.essence[i1] + "\t";
                        }
                        characterfile.write(tw46);
                        characterfile.newLine();
                        
                        characterfile.write("compColorsRGB = ", 0, 16);
                        tw46 = "";
                        for (int i1 = 0; i1 < player.compColorsRGB.length; i1++) {
                                tw46 += player.compColorsRGB[i1] + "\t";
                        }
                        characterfile.write(tw46);
                        characterfile.newLine();
                        
                        characterfile.write("compColor = ", 0, 12);
                        tw46 = "";
                        for (int i1 = 0; i1 < player.compColor.length; i1++) {
                                tw46 += player.compColor[i1] + "\t";
                        }
                        characterfile.write(tw46);
                        characterfile.newLine();
                        
                        characterfile.write("compPreset1 = ", 0, 13);
                        tw46 = "";
                        for (int i1 = 0; i1 < player.compPreset[0].length; i1++) {
                                tw46 += player.compPreset[0][i1] + "\t";
                        }
                        characterfile.write(tw46);
                        characterfile.newLine();
                        characterfile.write("compPreset2 = ", 0, 13);
                        tw46 = "";
                        for (int i1 = 0; i1 < player.compPreset[1].length; i1++) {
                                tw46 += player.compPreset[1][i1] + "\t";
                        }
                        characterfile.write(tw46);
                        characterfile.newLine();
                        characterfile.write("compPreset3 = ", 0, 13);
                        tw46 = "";
                        for (int i1 = 0; i1 < player.compPreset[2].length; i1++) {
                                tw46 += player.compPreset[2][i1] + "\t";
                        }
                        characterfile.write(tw46);
                        characterfile.newLine();
                        
                        characterfile.write("barrowsData = ", 0, 14);
                        String tw49 = "";
                        for (int i1 = 0; i1 < player.barrowsData.length; i1++) {
                                tw49 += player.barrowsData[i1] + "\t";
                        }
                        characterfile.write(tw49);
                        characterfile.newLine();
                        
                        characterfile.write("tabNames = ", 0, 11);
                        String tw47 = "";
                        for (int i1 = 0; i1 < player.tabNames.length; i1++) {
                                tw47 += player.tabNames[i1] + "\t";
                        }
                        characterfile.write(tw47);
                        characterfile.newLine();
                        
                        characterfile.write("gwdKC = ", 0, 8);
                        String tw48 = "";
                        for (int i1 = 0; i1 < player.gwdKC.length; i1++) {
                                tw48 += player.gwdKC[i1] + "\t";
                        }
                        characterfile.write(tw48);
                        characterfile.newLine();
                        
                        
                        characterfile.write("easyProgress = ", 0, 15);
                        String tw = "";
                        for (int i1 = 0; i1 < player.easyProgress.length; i1++) {
                                tw += player.easyProgress[i1] + "\t";
                        }
                        characterfile.write(tw);
                        characterfile.newLine();
                        characterfile.write("mediumProgress = ", 0, 17);
                        String tw2 = "";
                        for (int i1 = 0; i1 < player.mediumProgress.length; i1++) {
                                tw2 += player.mediumProgress[i1] + "\t";
                        }
                        characterfile.write(tw2);
                        characterfile.newLine();
                        characterfile.write("hardProgress = ", 0, 15);
                        String tw3 = "";
                        for (int i1 = 0; i1 < player.hardProgress.length; i1++) {
                                tw3 += player.hardProgress[i1] + "\t";
                        }
                        characterfile.write(tw3);
                        characterfile.newLine();
                        characterfile.write("eliteProgress = ", 0, 16);
                        String tw4 = "";
                        for (int i1 = 0; i1 < player.eliteProgress.length; i1++) {
                                tw4 += player.eliteProgress[i1] + "\t";
                        }
                        characterfile.write(tw4);
                        characterfile.newLine();
                        
                        characterfile.write("QuickCurses = ", 0, 14);
                        String toWrite3 = "";
                        for (int i1 = 0; i1 < player.quickCurses.length; i1++)
                        {
                                toWrite3 += player.quickCurses[i1] + "\t";
                        }
                        characterfile.write(toWrite3);
                        characterfile.newLine();
                        characterfile.write("QuickPrayers = ", 0, 15);
                        String toWrite4 = "";
                        for (int i1 = 0; i1 < player.quickPrayers.length; i1++)
                        {
                                toWrite4 += player.quickPrayers[i1] + "\t";
                        }
                        characterfile.write(toWrite4);
                        characterfile.newLine();
                        characterfile.newLine();
                        characterfile.write("geSlots = ", 0, 10);
                        String toWrite10 = "";
                        for (int i1 = 0; i1 < player.getPD().geSlots.length; i1++)
                        {
                                toWrite10 += player.getPD().geSlots[i1] + "\t";
                        }
                        characterfile.write(toWrite10);
                        characterfile.newLine();
                        characterfile.newLine();
                        
                        Preset p = null;
                        for (int i = 0; i < 10; i++) {
                        	if (player.presets[i] != null) {
                        		p = player.presets[i];
                        		characterfile.write("Preset = "+i+" = "+p.name+" = "+
                        				array(p.equipment)+" = "+array(p.equipmentN)+" = "+
                        				array(p.inventory)+" = "+array(p.inventoryN)+"");
                        		characterfile.newLine();
                        	}
                        }
                        
                        characterfile.newLine();
                        characterfile.newLine();
                        characterfile.newLine();
                        characterfile.newLine();
                        
                        
                        
                        
                        characterfile.write("[EQUIPMENT]", 0, 11);
                        characterfile.newLine();
                        for (int i = 0; i < player.playerEquipment.length; i++)
                        {
                                characterfile.write("character-equip = ", 0, 18);
                                characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.playerEquipment[i]), 0, Integer.toString(player.playerEquipment[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.playerEquipmentN[i]), 0, Integer.toString(player.playerEquipmentN[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.playerEquipmentC[i]), 0, Integer.toString(player.playerEquipmentC[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.newLine();
                        }
                        characterfile.newLine();
                        characterfile.write("[APPEARANCE]", 0, 12);
                        characterfile.newLine();
                        for (int i = 0; i < player.playerAppearance.length; i++)
                        {
                                characterfile.write("character-look = ", 0, 17);
                                characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.playerAppearance[i]), 0, Integer.toString(player.playerAppearance[i]).length());
                                characterfile.newLine();
                        }
                        characterfile.newLine(); /* SKILLS */
                        characterfile.write("[SKILLS]", 0, 8);
                        characterfile.newLine();
                        for (int i = 0; i < player.skillLevel.length; i++)
                        {
                                characterfile.write("character-skill = ", 0, 18);
                                characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.skillLevel[i]), 0, Integer.toString(player.skillLevel[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.playerXP[i]), 0, Integer.toString(player.playerXP[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Boolean.toString(player.announced99[i]), 0, Boolean.toString(player.announced99[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Boolean.toString(player.announced100m[i]), 0, Boolean.toString(player.announced100m[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Boolean.toString(player.announced200m[i]), 0, Boolean.toString(player.announced200m[i]).length());
                                characterfile.newLine();
                        }
                        characterfile.newLine(); /* ITEMS */
                        characterfile.write("[INVENTORY]", 0, 11);
                        characterfile.newLine();
                        for (int i = 0; i < player.playerItems.length; i++)
                        {
                                if (player.playerItems[i] > 0)
                                {
                                        characterfile.write("inventory-slot = ", 0, 17);
                                        characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                                        characterfile.write("	", 0, 1);
                                        characterfile.write(Integer.toString(player.playerItems[i]), 0, Integer.toString(player.playerItems[i]).length());
                                        characterfile.write("	", 0, 1);
                                        characterfile.write(Integer.toString(player.playerItemsN[i]), 0, Integer.toString(player.playerItemsN[i]).length());
                                        characterfile.write("	", 0, 1);
                                        characterfile.write(Integer.toString(player.playerItemsC[i]), 0, Integer.toString(player.playerItemsC[i]).length());
                                        characterfile.newLine();
                                }
                        }
                        characterfile.newLine(); /* BANK */
                        characterfile.write("[BANK]", 0, 6);
                        characterfile.newLine();
                        for (int i = 0; i < BankConstants.TOTAL_TABS; i++) {
                        	for (int j = 0; j < BankConstants.MAX_ITEMS_PER_TAB; j++) {
                        		if (player.getBank().bankItems[i][j] > 0) {
                        			characterfile.write("bank-item = ", 0, 12);
                                    characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                                    characterfile.write("	", 0, 1);
                                    characterfile.write(Integer.toString(j), 0, Integer.toString(j).length());
                                    characterfile.write("	", 0, 1);
                                    characterfile.write(Integer.toString(player.getBank().bankItems[i][j]), 0, Integer.toString(player.getBank().bankItems[i][j]).length());
                                    characterfile.write("	", 0, 1);
                                    characterfile.write(Integer.toString(player.getBank().bankAmounts[i][j]), 0, Integer.toString(player.getBank().bankAmounts[i][j]).length());
                                    characterfile.write("	", 0, 1);
                                    characterfile.write(Integer.toString(player.getBank().bankCharges[i][j]), 0, Integer.toString(player.getBank().bankCharges[i][j]).length());
                                    characterfile.newLine();
                        		}
                        	}
                        }
                        characterfile.newLine();
                        characterfile.write("[FRIENDS]", 0, 9);
                        characterfile.newLine();
                        for (int i = 0; i < player.friends.length; i++)
                        {
                                if (player.friends[i] > 0)
                                {
                                        characterfile.write("character-friend = ", 0, 19);
                                        characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                                        characterfile.write("	", 0, 1);
                                        characterfile.write("" + player.friends[i]);
                                        characterfile.newLine();
                                }
                        }
                        characterfile.newLine();
                        characterfile.write("[IGNORES]", 0, 9);
                        characterfile.newLine();
                        for (int i = 0; i < player.ignores.length; i++)
                        {
                                if (player.ignores[i] > 0)
                                {
                                        characterfile.write("character-ignore = ", 0, 19);
                                        characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
                                        characterfile.write("	", 0, 1);
                                        characterfile.write(Long.toString(player.ignores[i]), 0, Long.toString(player.ignores[i]).length());
                                        characterfile.newLine();
                                }
                        }
                        characterfile.write("[FARMING]", 0, 9);
                        characterfile.newLine();

                        for (int i = 0; i < player.getAllotment().allotmentStages.length; i++) {
                                if (player.getAllotment().allotmentStages[i] <= 0)
                                        continue;
                                characterfile.write("allotment" + i + " = ", 0, 13);
                                characterfile.write(Integer.toString(player.getAllotment().allotmentHarvest[i]), 0, Integer.toString(player.getAllotment().allotmentHarvest[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.getAllotment().allotmentSeeds[i]), 0, Integer.toString(player.getAllotment().allotmentSeeds[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.getAllotment().allotmentStages[i]), 0, Integer.toString(player.getAllotment().allotmentStages[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.getAllotment().allotmentState[i]), 0, Integer.toString(player.getAllotment().allotmentState[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Long.toString(player.getAllotment().allotmentTimer[i]), 0, Long.toString(player.getAllotment().allotmentTimer[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Double.toString(player.getAllotment().diseaseChance[i]), 0, Double.toString(player.getAllotment().diseaseChance[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Boolean.toString(player.getAllotment().hasFullyGrown[i]), 0, Boolean.toString(player.getAllotment().hasFullyGrown[i]).length());
                                characterfile.newLine();
                        }

                        for (int i = 0; i < player.getBushes().bushesStages.length; i++) {
                                if (player.getBushes().bushesStages[i] <= 0)
                                        continue;
                                characterfile.write("bush" + i + " = ", 0, 8);
                                characterfile.write(Integer.toString(player.getBushes().bushesSeeds[i]), 0, Integer.toString(player.getBushes().bushesSeeds[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.getBushes().bushesStages[i]), 0, Integer.toString(player.getBushes().bushesStages[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.getBushes().bushesState[i]), 0, Integer.toString(player.getBushes().bushesState[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Long.toString(player.getBushes().bushesTimer[i]), 0, Long.toString(player.getBushes().bushesTimer[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Double.toString(player.getBushes().diseaseChance[i]), 0, Double.toString(player.getBushes().diseaseChance[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Boolean.toString(player.getBushes().hasFullyGrown[i]), 0, Boolean.toString(player.getBushes().hasFullyGrown[i]).length());
                                characterfile.newLine();
                        }

                        for (int i = 0; i < player.getTrees().treeStages.length; i++) {
                                if (player.getTrees().treeStages[i] <= 0)
                                        continue;
                                characterfile.write("tree" + i + " = ", 0, 8);
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.getTrees().treeHarvest[i]), 0, Integer.toString(player.getTrees().treeHarvest[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.getTrees().treeSaplings[i]), 0, Integer.toString(player.getTrees().treeSaplings[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.getTrees().treeStages[i]), 0, Integer.toString(player.getTrees().treeStages[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.getTrees().treeState[i]), 0, Integer.toString(player.getTrees().treeState[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Long.toString(player.getTrees().treeTimer[i]), 0, Long.toString(player.getTrees().treeTimer[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Double.toString(player.getTrees().diseaseChance[i]), 0, Double.toString(player.getTrees().diseaseChance[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Boolean.toString(player.getTrees().hasFullyGrown[i]), 0, Boolean.toString(player.getTrees().hasFullyGrown[i]).length());
                                characterfile.newLine();
                        }

                        for (int i = 0; i < player.getFlowers().flowerStages.length; i++) {
                                if (player.getFlowers().flowerStages[i] <= 0)
                                        continue;
                                characterfile.write("flower" + i + " = ", 0, 10);
                                characterfile.write(Integer.toString(player.getFlowers().flowerSeeds[i]), 0, Integer.toString(player.getFlowers().flowerSeeds[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.getFlowers().flowerStages[i]), 0, Integer.toString(player.getFlowers().flowerStages[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.getFlowers().flowerState[i]), 0, Integer.toString(player.getFlowers().flowerState[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Long.toString(player.getFlowers().flowerTimer[i]), 0, Long.toString(player.getFlowers().flowerTimer[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Double.toString(player.getFlowers().diseaseChance[i]), 0, Double.toString(player.getFlowers().diseaseChance[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Boolean.toString(player.getFlowers().hasFullyGrown[i]), 0, Boolean.toString(player.getFlowers().hasFullyGrown[i]).length());
                                characterfile.newLine();
                        }

                        for (int i = 0; i < player.getFruitTrees().fruitTreeStages.length; i++) {
                                if (player.getFruitTrees().fruitTreeStages[i] <= 0)
                                        continue;
                                characterfile.write("fruitTree" + i + " = ", 0, 13);
                                characterfile.write(Integer.toString(player.getFruitTrees().fruitTreeSaplings[i]), 0, Integer.toString(player.getFruitTrees().fruitTreeSaplings[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.getFruitTrees().fruitTreeStages[i]), 0, Integer.toString(player.getFruitTrees().fruitTreeStages[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.getFruitTrees().fruitTreeState[i]), 0, Integer.toString(player.getFruitTrees().fruitTreeState[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Long.toString(player.getFruitTrees().fruitTreeTimer[i]), 0, Long.toString(player.getFruitTrees().fruitTreeTimer[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Double.toString(player.getFruitTrees().diseaseChance[i]), 0, Double.toString(player.getFruitTrees().diseaseChance[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Boolean.toString(player.getFruitTrees().hasFullyGrown[i]), 0, Boolean.toString(player.getFruitTrees().hasFullyGrown[i]).length());
                                characterfile.newLine();
                        }

                        for (int i = 0; i < player.getHerbs().herbStages.length; i++) {
                                if (player.getHerbs().herbStages[i] <= 0)
                                        continue;
                                characterfile.write("herb" + i + " = ", 0, 8);
                                characterfile.write(Integer.toString(player.getHerbs().herbHarvest[i]), 0, Integer.toString(player.getHerbs().herbHarvest[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.getHerbs().herbSeeds[i]), 0, Integer.toString(player.getHerbs().herbSeeds[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.getHerbs().herbStages[i]), 0, Integer.toString(player.getHerbs().herbStages[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Integer.toString(player.getHerbs().herbState[i]), 0, Integer.toString(player.getHerbs().herbState[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Long.toString(player.getHerbs().herbTimer[i]), 0, Long.toString(player.getHerbs().herbTimer[i]).length());
                                characterfile.write("	", 0, 1);
                                characterfile.write(Double.toString(player.getHerbs().diseaseChance[i]), 0, Double.toString(player.getHerbs().diseaseChance[i]).length());
                                characterfile.newLine();
                        }

                        // for (int i = 0; i < p.getHops().hopsStages.length; i++) {
                        // characterfile.write("hop" + i + " = ", 0, 7);
                        // characterfile.write("" + p.getHops().hopsHarvest[i]);
                        // characterfile.write(" ", 0, 1);
                        // characterfile.write("" + p.getHops().hopsSeeds[i]);
                        // characterfile.write(" ", 0, 1);
                        // characterfile.write("" + p.getHops().hopsStages[i]);
                        // characterfile.write(" ", 0, 1);
                        // characterfile.write("" + p.getHops().hopsState[i]);
                        // characterfile.write(" ", 0, 1);
                        // characterfile.write("" + p.getHops().hopsTimer[i]);
                        // characterfile.write(" ", 0, 1);
                        // characterfile.write("" + p.getHops().diseaseChance[i]);
                        // characterfile.write(" ", 0, 1);
                        // characterfile.write("" + p.getHops().hasFullyGrown[i]);
                        // characterfile.newLine();
                        // }

                        // for (int i = 0; i <
                        // p.getSpecialPlantOne().specialPlantStages.length; i++) {
                        // characterfile.write("specialPlantOne" + i + " = ", 0, 19);
                        // characterfile.write("" +
                        // p.getSpecialPlantOne().specialPlantSaplings[i]);
                        // characterfile.write(" ", 0, 1);
                        // characterfile.write("" +
                        // p.getSpecialPlantOne().specialPlantStages[i]);
                        // characterfile.write(" ", 0, 1);
                        // characterfile.write("" +
                        // p.getSpecialPlantOne().specialPlantState[i]);
                        // characterfile.write(" ", 0, 1);
                        // characterfile.write("" +
                        // p.getSpecialPlantOne().specialPlantTimer[i]);
                        // characterfile.write(" ", 0, 1);
                        // characterfile.write("" +
                        // p.getSpecialPlantOne().diseaseChance[i]);
                        // characterfile.write(" ", 0, 1);
                        // characterfile.write("" +
                        // p.getSpecialPlantOne().hasFullyGrown[i]);
                        // characterfile.newLine();
                        // }

                        // for (int i = 0; i <
                        // p.getSpecialPlantTwo().specialPlantStages.length; i++) {
                        // characterfile.write("specialPlantTwo" + i + " = ", 0, 19);
                        // characterfile.write("" +
                        // p.getSpecialPlantTwo().specialPlantSeeds[i]);
                        // characterfile.write(" ", 0, 1);
                        // characterfile.write("" +
                        // p.getSpecialPlantTwo().specialPlantStages[i]);
                        // characterfile.write(" ", 0, 1);
                        // characterfile.write("" +
                        // p.getSpecialPlantTwo().specialPlantState[i]);
                        // characterfile.write(" ", 0, 1);
                        // characterfile.write("" +
                        // p.getSpecialPlantTwo().specialPlantTimer[i]);
                        // characterfile.write(" ", 0, 1);
                        // characterfile.write("" +
                        // p.getSpecialPlantTwo().diseaseChance[i]);
                        // characterfile.write(" ", 0, 1);
                        // characterfile.write("" +
                        // p.getSpecialPlantTwo().hasFullyGrown[i]);
                        // characterfile.newLine();
                        // }
                        //
                        characterfile.newLine();
                        characterfile.write("[EOF]", 0, 5);
                        characterfile.newLine();
                        characterfile.newLine();
                        characterfile.close();
                }
                catch (IOException ioexception)
                {
                        Misc.println(player.playerName + ": error writing file.");
                        ioexception.printStackTrace();
                        return false;
                }
                return true;
        }
        
        
        public static String array(int[] a) {
        	String s = "";
        	for (int i : a) {
        		s += ""+i+",";
        	}
        	return s;
        }
}