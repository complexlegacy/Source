package ionic.player;

import ionic.clans.ClanHandler;
import ionic.item.ItemAssistant;
import ionic.npc.pet.Pet;
import ionic.player.achievements.AchievementHandler;
import ionic.player.banking.BankHandler;
import ionic.player.content.consumable.PotionData;
import ionic.player.content.minigames.FightCaves;
import ionic.player.content.minigames.FightPits;
import ionic.player.content.miscellaneous.MonsterKillLog;
import ionic.player.content.miscellaneous.NpcInfoViewer;
import ionic.player.content.miscellaneous.Skull;
import ionic.player.content.miscellaneous.Starter;
import ionic.player.content.prayer.NormalPrayerBook;
import ionic.player.content.quest.QuestTab;
import ionic.player.content.skills.summoning.Summoning;
import ionic.player.interfaces.AreaInterface;
import ionic.player.interfaces.InterfaceAssistant;
import ionic.player.profiles.Profile;
import core.Constants;

public class LogInUpdate
{

        /**
         * Update game for the player on login.
         */
        public static void update(Player player) {
                player.correctCoordinates();
                MonsterKillLog.loadLog(player);
                updateBeforeNotice(player);
                player.outStream.createFrame(249);
                player.outStream.writeByteA(1);
                player.outStream.writeWordBigEndianA(player.playerId);
                player.getPA().checkDuplicatePlayerOnline();
                player.getPA().refreshSkills();
                NormalPrayerBook.resetAllPrayerGlows(player);
                player.getPA().handleWeaponStyle();
                InterfaceAssistant.miscellaneousInterfaces(player);
                player.getPA().sendFrame36(166, 4);
                player.getPA().sendFrame107(); // Reset screen
                player.getPA().setChatOptions(0, 0, 0); // Reset private messaging options
                InterfaceAssistant.splitPrivateChat(player);
                InterfaceAssistant.showTabs(player);
                player.getPA().resetAutocast();
                player.getCombat().resetPrayers();
                player.getPA().showOption(4, 0, "Follow", 4);
                player.getPA().showOption(5, 0, "Trade With", 3);
                ItemAssistant.resetItems(player, 3214);
                ItemAssistant.updateCombatInterface(player, player.playerEquipment[Constants.WEAPON_SLOT], ItemAssistant.getItemName(player.playerEquipment[Constants.WEAPON_SLOT]));
                ItemAssistant.calculateEquipmentBonuses(player);
                ItemAssistant.writeBonus(player);
                ItemAssistant.updateEquipmentInterface(player);
                player.getCombat().getPlayerAnimIndex(ItemAssistant.getItemName(player.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
                player.getPA().logIntoPM();
                ItemAssistant.addSpecialBar(player, player.playerEquipment[Constants.WEAPON_SLOT]);
                player.saveCharacter = true;
                player.getPA().updateHitPoints();
                player.handler.updatePlayer(player, player.outStream);
                player.handler.updateNPC(player, player.outStream);
                player.flushOutStream();
                player.getPA().resetFollow();
                player.totalLevel = player.getPA().totalLevel();
                player.xpTotal = player.getPA().xpTotal();
                InterfaceAssistant.updateTotalLevel(player);
                player.getPA().sendFrame36(172, player.autoRet);
                player.getPA().sendFrame36(173, player.isRunning2 ? 1 : 0);
                player.getPA().saveGame();
                player.getDuelArena().removeFromArena();
                AreaInterface.startSkullTimerEvent(player);
                InterfaceAssistant.updateCombatLevel(player);
                Pet.ownerLoggedIn(player);
                player.getPA().sendFrame214();
                player.getCombat().restoreSpecialAttackEvent();
                InterfaceAssistant.updateLockXp(player);
                player.getPA().loopedSave();
                player.checkOptionsTab();
                player.loggingInFinished = true;
                player.getPA().refreshNewSkills();
                BankHandler.refreshBank((Client)player);
                player.getPouch().updatePouch();
                AchievementHandler.refreshAchievementTab(player);
                AchievementHandler.add(player, 0, "easy", 1);
                new Starter((Client)player);
                player.getPA().sendFrame126("", 22100);
                player.clanOwned = ClanHandler.findClan(player.playerName);
                player.sendClanRanks();
                ClanHandler.sendClanTabUpdate(player);
                player.getPA().updatePlayerTab();
                player.sendMessage(":searchOff:");
                player.sendMessage(":insertOff:");
                player.sendCompCapePresets();
                if (player.inPcGame() || player.inPcBoat()) {
                	player.getPA().movePlayer(2657, 2639, 0);
                }
                if (FightPits.inArena(player)) {
                	player.getPA().movePlayer(2399, 5174, 0);
                }
                if (player.prayerRenewal > 0) {
                	PotionData.startPrayerRenewal(player, true);
                }
                FightCaves.updateWave((Client)player);
                
               
                String g = "";
                for (int i = 0; i < player.compColorsRGB.length; i++) {
                	g += ""+player.compColorsRGB[i]+" ";
                }
                player.getPA().sendFrame126(g, 18712);
                
                Summoning.loginSummon(player, player.famType);
                NpcInfoViewer.sendMonsters(player);
                player.profile = Profile.search(player.playerName);
                QuestTab.updateQuestTab(player);
        }

        /**
         * Update certain parts of the game for the player, before the player can see the update happening.
         * @param player
         * 			The assosciated player.
         */
        private static void updateBeforeNotice(Player player)
        {
        		player.sendMessage(player.isRunning2 ? ":runningoff:" : ":runningon:");
                InterfaceAssistant.informClientResting(player, "off");
                player.getPA().sendFrame126((int) player.runEnergy + "%", 149);
                AreaInterface.startInterfaceEvent(player);
            	Skull.updateSkullAppearance(player);
        }
  
        
        
        
        

}