package ionic.player;

import ionic.item.ItemAssistant;
import ionic.item.ItemData;
import ionic.npc.NPCData;
import ionic.npc.NPCHandler;
import ionic.object.clip.Region;
import ionic.player.achievements.AchievementHandler;
import ionic.player.banking.BankHandler;
import ionic.player.content.minigames.Barrows;
import ionic.player.content.minigames.CastleWars;
import ionic.player.content.minigames.FightPits;
import ionic.player.content.minigames.PestControl;
import ionic.player.content.miscellaneous.PriceChecker;
import ionic.player.content.miscellaneous.Tele;
import ionic.player.content.miscellaneous.Teleport;
import ionic.player.content.skills.slayer.Slayer;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;
import ionic.player.interfaces.AreaInterface;
import ionic.player.interfaces.InterfaceAssistant;
import ionic.player.movement.Movement;
import ionic.player.movement.PathFinder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import utility.Misc;
import core.Configuration;
import core.Constants;
import core.Server;

public class PlayerOther {

	public void loadWalkableInterfaces() {
		if (inGWD()) {
			for (int i = 0; i < player.gwdKC.length; i++) {
				sendFrame126(""+player.gwdKC[i]+"", 27507+i);
			}
			walkableInterface(27500, 50, 50);//godwars
		} else if (inBarrows(player.absX, player.absY)) {
			Barrows.barrowsInterfaceUpdate(player);
			walkableInterface(23500, 0, 0);
		} else if (player.inPcBoat()) {
			Server.pestControl.setBoatInterface();
			walkableInterface(21119, 0, 0);
		} else if (PestControl.isInGame(player)) {
			walkableInterface(21100, 0, 0);
		} else if (player.inFightCaves()) {
			walkableInterface(27512, 20, 40);
		} else if (FightPits.inWaitingRoom(player)) {
			FightPits.updateWaitInterface(player);
			walkableInterface(27514, 0, 10);
		} else if (CastleWars.inCastleWars(player) && player.inCastleWars) {
			walkableInterface(11146, 0, 10);
			if (!CastleWars.inRespawnPoint(player)) {
				player.getPA().sendFrame126("", 12837);
			}
		} else if (CastleWars.inWaitingRoom(player)) {
			walkableInterface(27518, 0, 0);
		} else {
			resetWalkableInterfaces();
		}
	}
	
	
	
	public void delayWalkableInterfaces() {
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				container.stop();
			}
			@Override
			public void stop() {
				loadWalkableInterfaces();
			}
		}, 2);
	}
	
	
	
	public boolean inBarrows(int absX, int absY) {
		if ((absX >= 3543 && absY >= 3267 && absX <= 3583 && absY <= 3314)
				|| (absX >= 3529 && absY >= 9676 && absX <= 3582 && absY <= 9723)) {
			return true;
		}
		return false;
	}


	/**
	 * resets the walkable Interfaces
	 */
	public void resetWalkableInterfaces() {
		sendFrame126("-1 -1 -1", 27400);
	}

	/**
	 * puts an interface on the screen that shows always
	 * does not have actions
	 * @param id - The ID of the interface to show
	 * @param x - The (X) position of the interface on the screen
	 * @param y - The (Y) position of the interface on the screen
	 */
	public void walkableInterface(int id, int x, int y) {
		sendFrame126(""+id+" "+x+" "+y+"", 27400);
	}

	/**
	 * Shakes the player's screen.
	 * Parameters 1, 0, 0, 0 to reset.
	 * @param verticleAmount How far the up and down shaking goes (1-4).
	 * @param verticleSpeed How fast the up and down shaking is.
	 * @param horizontalAmount How far the left-right tilting goes.
	 * @param horizontalSpeed How fast the right-left tiling goes..
	 */
	public void shakeScreen(int verticleAmount, int verticleSpeed, int horizontalAmount, int horizontalSpeed) {
		player.outStream.createFrame(35); // Creates frame 35.
		player.outStream.writeByte(verticleAmount);
		player.outStream.writeByte(verticleSpeed);
		player.outStream.writeByte(horizontalAmount);
		player.outStream.writeByte(horizontalSpeed);
	}
	/**
	 * Resets the shaking of the player's screen.
	 */
	public void resetShaking() {
		player.outStream.createFrame(107);
	}
	
	
	public void clearInterface(int frame) {
		player.outStream.createFrame(72);
		player.outStream.writeDWord_v2(frame);
	}



	/**
	 * The player.
	 */
	private Client player;

	public PlayerOther(Client Client)
	{
		this.player = Client;
	}

	/**
	 * Announce and reward the player for reaching 99 in a skill.
	 * @param Skill
	 * 			The skill type.
	 * @param skillName
	 * 			The name of the skill.
	 */
	public void announce99(int Skill, String skillName)
	{
		if (getLevelForXP(player.playerXP[Skill]) == 99)
		{
			player.achievementPoint += 3;
			player.sendMessage("You are awarded 3 achievement points.");
			player.announce("<col=255>" + player.playerName + " has just achieved 99 " + skillName + "!");
			// sendFrame126("Achievement points: " + player.achievementPoint + "", 28023);
		}
	}


	public boolean inGWD() {
		if (player.absX >= 2800 && player.absX <= 2950 && player.absY >= 5200 && player.absY <= 5400) {
			return true;
		}
		return false;
	}



	public void updatePlayerTab() {
		sendFrame126(player.playerName, 663);
		sendFrame126("Player Rank: "+Ranking.forID(player.playerRights).show+"", 16026);
		sendFrame126("PK Points: @gre@"+player.pkPoints, 16027);
		sendFrame126("Kills: @gre@"+player.killCount, 16028);
		sendFrame126("Deaths: @gre@"+player.deathCount, 16029);
		sendFrame126("Kill Streak: @gre@"+player.killStreak, 16030);
		sendFrame126("Best Kill Streak: @gre@"+player.killStreakRecord, 16031);
		
		sendFrame126("Fight Pits Wins: @gre@"+player.pitWins, 16033);
		sendFrame126("Barrows Chests: @gre@"+player.chestLoots, 16034);
		sendFrame126("Pest Control Points: @gre@"+player.pcPoints, 16035);
		
		if (player.taskId > 0) {
			sendFrame126("Task: @gre@"+NPCData.data[Slayer.Tasks.forID(player.taskId).npcId[0]].name, 16037);
			sendFrame126("Task Amount: @gre@"+player.taskAmount, 16038);
		} else {
			sendFrame126("Task: @gre@None", 16037);
			sendFrame126("Task Amount: @gre@0", 16038);
		}
		sendFrame126("Slayer Points: @gre@"+player.slayerPoints, 16039);
		sendFrame126("Slayer Streak: @gre@"+player.slayerStreak, 16040);
	}




	public void resetVariables()
	{
		player.smeltInterface = false;;
		player.oreInformation[0] = player.oreInformation[1] = player.oreInformation[2] = 0;
		player.usingHerbloreEvent = false;

		if (player.stopPlayerSkill)
		{
			player.stopPlayerSkill = false;
		}

		for (int i = 0; i < 6; i++)
		{
			player.playerSkillProp[7][i] = -1;
		}
	}

	/**
	 * Inform the client to make the change password interface appear.
	 */
	public void sendChangePassword()
	{
		player.sendMessage(":changepassword:");
	}

	/**
	 * Receive the new player's password from the client.
	 */
	public void receiveChangePassword(String newPassword)
	{
		String newestPassword = newPassword.substring(14);
		if (newestPassword.length() > 0 && newestPassword.length() < 21)
		{
			player.playerPass = newestPassword;
			player.sendMessage("Your new password is now<col=255> \"" + player.playerPass + "\"");
		}
		else
		{
			player.sendMessage("Wrong entry. your password is still <col=255>\"" + player.playerPass + "\"");
			player.sendMessage("Maximium of 20 characters allowed for your password.");
		}
	}

	/**
	 * Calculate the time untill un-mute and notify player.
	 */
	public void calculateTimeTillUnmute()
	{
		long totalSeconds = 0;
		double decimalHours = 0;
		int totalMinutes = 0;
		int integerHours = 0;
		double minutesExtra = 0;
		double lastOne = 0;
		totalSeconds = (player.timeUnMuted - System.currentTimeMillis()) / 1000;
		totalMinutes = (int) totalSeconds / 60;
		decimalHours = (double) totalMinutes / 60.0;
		if (decimalHours < 1)
		{
			player.sendMessage("You will be unmuted in " + totalMinutes + " minutes.");
			return;
		}
		integerHours = (int) decimalHours;
		lastOne = (decimalHours - (double) integerHours) * 100;
		minutesExtra = (60.0/100.0) * lastOne;
		player.sendMessage("You will be unmuted in " + integerHours + " hours and " + (int) minutesExtra + " minutes.");
	}

	/**
	 * Set the date of when the account is created on.
	 */
	public void setDateCreated()
	{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		player.accountDateCreated = dateFormat.format(cal.getTime());
	}

	/**
	 * Refresh the skills.
	 */
	public void refreshSkills()
	{
		refreshNewSkills();
		for (int i = 0; i < player.skillLevel.length; i++)
		{
			setSkillLevel(i, player.skillLevel[i], player.playerXP[i]);
			refreshSkill(i);
		}
	}
	public void refreshNewSkills() {
		sendFrame126("@yel@" + player.skillLevel[21] + "", 18166);
		sendFrame126("@yel@" + player.getLevelForXP(player.playerXP[21]) + "", 18170);

		sendFrame126("@yel@" + player.skillLevel[22] + "", 18165);
		sendFrame126("@yel@" + player.getLevelForXP(player.playerXP[22]) + "", 18169);

		sendFrame126("@yel@" + player.skillLevel[24] + "", 18167);
		sendFrame126("@yel@" + player.getLevelForXP(player.playerXP[24]) + "", 18171);
	}

	/**
	 * Check if the same player is logged in twice. If so, then disconnect the player.
	 */
	public void checkDuplicatePlayerOnline()
	{
		for (int j = 0; j < PlayerHandler.players.length; j++)
		{
			if (j == player.playerId) continue;
			if (PlayerHandler.players[j] != null)
			{
				if (PlayerHandler.players[j].playerName.equalsIgnoreCase(player.playerName))
				{
					player.disconnected = true;
				}
			}
		}
	}

	public void sendColor(int id, int color)
	{
		player.getOutStream().createFrame(122);
		player.getOutStream().writeWordBigEndianA(id);
		player.getOutStream().writeWordBigEndianA(color);
	}

	/**
	 * Change title
	 *
	 * @param setTitle
	 *            The title type.
	 */
	public void setTitle(int setTitle)
	{
		player.title = setTitle;
		player.updateRequired = true;
		player.setAppearanceUpdateRequired(true);
	}

	public double round(double valueToRound, int numberOfDecimalPlaces)
	{
		double multipicationFactor = Math.pow(10, numberOfDecimalPlaces);
		double interestedInZeroDPs = valueToRound * multipicationFactor;
		return Math.round(interestedInZeroDPs) / multipicationFactor;
	}

	/**
	 * True, if the player is wearing anything and also alert the player.
	 */
	public boolean wearingEquipment()
	{
		for (int j = 0; j < player.playerEquipment.length; j++)
		{
			if (player.playerEquipment[j] > 0)
			{
				player.sendMessage("Please remove all your equipment before using this.");
				return true;
			}
		}
		return false;
	}

	/**
	 * Called after the skill has been changed through clicking on skill icon.
	 * <p>
	 * Calculate the HP depending on the other skills.
	 */
	public void updateCombatLevel()
	{

		int meleeStatsXP = player.playerXP[0] + player.playerXP[1] + player.playerXP[2];
		int rangeStatsXP = player.playerXP[4];
		int totalHPXP = 0;
		totalHPXP = meleeStatsXP / 4;
		totalHPXP += rangeStatsXP / 6;
		totalHPXP *= 1.3;
		player.playerXP[3] = totalHPXP;
		if (player.playerXP[3] < 1357)
		{
			player.playerXP[3] = 1357;
		}

		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
		{@
			Override
			public void execute(CycleEventContainer container)
		{
			sendFrame126("Combat Level: " + player.combatLevel, 19000);
			player.setHitPoints(player.maximumHitPoints());
			container.stop();

		}

		@
		Override
		public void stop()
		{}
		}, 1);
	}

	/**
	 * This event will save the player after the player has been online for 20 mins.
	 */
	public void saveGame()
	{
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
		{@
			Override
			public void execute(CycleEventContainer container)
		{
			PlayerSave.saveGame(player);
		}

		@
		Override
		public void stop()
		{

		}
		}, 2000);

	}

	/**
	 * This event is called per game tick to save the game.
	 */
	public void loopedSave()
	{
		if (!Configuration.DEBUG)
		{
			return;
		}
		if (Configuration.stabilityTest)
		{
			return;
		}
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent()
		{@
			Override
			public void execute(CycleEventContainer container)
		{
			PlayerSave.saveGame(player);
		}

		@
		Override
		public void stop()
		{

		}
		}, 1);

	}

	/**
	 * Update the hitpoints orb and hitpoints in the skill tab.
	 */
	public void updateHitPoints()
	{

		sendFrame126("" + player.skillLevel[3], 19001); // Hitpoints orb
		// updating
		sendFrame126("" + player.skillLevel[3], 4016); // Skill tab hitpoints
		// updating

	}

	public void setInterfaceText(String s, int id)
	{
		player.getOutStream().createPacketReservedWord(126);
		player.getOutStream().writeString(s);
		player.getOutStream().writeWordA(id);
		player.getOutStream().endPacketReservedWord();
	}

	private DecimalFormat format;

	public void sendClan(String name, String message, String clan, int rights)
	{
		player.outStream.createFrameVarSizeWord(217);
		player.outStream.writeString(name);
		player.outStream.writeString(message);
		player.outStream.writeString(clan);
		player.outStream.writeWord(rights);
		player.outStream.endFrameVarSize();
	}

	public String formatValue(double value, int digits)
	{
		format.setMaximumFractionDigits(digits);
		return format.format(value);
	}

	public void fixAllBarrows()
	{
		int totalCost = 0;
		int cashAmount = ItemAssistant.getItemAmount(player, 995);
		for (int j = 0; j < player.playerItems.length; j++)
		{
			boolean breakOut = false;
			for (int i = 0; i < ItemAssistant.brokenBarrows.length; i++)
			{
				if (player.playerItems[j] - 1 == ItemAssistant.brokenBarrows[i][1])
				{
					if (totalCost + 100000 > cashAmount)
					{
						breakOut = true;
						break;
					}
					else
					{
						totalCost += 100000;
					}
					player.playerItems[j] = ItemAssistant.brokenBarrows[i][0] + 1;
				}
			}
			if (breakOut)
			{
				break;
			}
		}
		if (totalCost > 0)
		{
			ItemAssistant.deleteItem(player, 995, ItemAssistant.getItemSlot(player, 995), totalCost);
			removeAllWindows();
		}
	}


	public void writeChatLog(String data)
	{
		checkDateAndTime();
		String filePath = "./data/logs/ChatLog/" + player.playerName + ".txt";
		BufferedWriter bw = null;

		try
		{
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write("[" + player.date + "]" + "-" + "[" + player.currentTime + " " + checkTimeOfDay() + "]: " + "[" + player.connectedFrom + "]: " + "" + data + " ");
			bw.newLine();
			bw.flush();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			if (bw != null)
			{
				try
				{
					bw.close();
				}
				catch (IOException ioe2)
				{}
			}
		}
	}

	public int skillcapeGfx(int cape)
	{
		int capeGfx[][] = {
				{
					9747, 823
				},
				{
					9748, 823
				},
				{
					9750, 828
				},
				{
					9751, 828
				},
				{
					9753, 824
				},
				{
					9754, 824
				},
				{
					9756, 832
				},
				{
					9757, 832
				},
				{
					9759, 829
				},
				{
					9760, 829
				},
				{
					9762, 813
				},
				{
					9763, 813
				},
				{
					9765, 817
				},
				{
					9766, 817
				},
				{
					9768, 833
				},
				{
					9769, 833
				},
				{
					9771, 830
				},
				{
					9772, 830
				},
				{
					9774, 835
				},
				{
					9775, 835
				},
				{
					9777, 826
				},
				{
					9778, 826
				},
				{
					9780, 818
				},
				{
					9781, 818
				},
				{
					9783, 812
				},
				{
					9784, 812
				},
				{
					9786, 827
				},
				{
					9787, 827
				},
				{
					9789, 820
				},
				{
					9790, 820
				},
				{
					9792, 814
				},
				{
					9793, 814
				},
				{
					9795, 815
				},
				{
					9796, 815
				},
				{
					9798, 819
				},
				{
					9799, 819
				},
				{
					9801, 821
				},
				{
					9802, 821
				},
				{
					9804, 831
				},
				{
					9805, 831
				},
				{
					9807, 822
				},
				{
					9808, 822
				},
				{
					9810, 825
				},
				{
					9811, 825
				},
				{
					9948, 907
				},
				{
					9949, 907
				},
				{
					9813, 816
				},
				{
					12170, 1515
				}
		};
		for (int i = 0; i < capeGfx.length; i++)
		{
			if (capeGfx[i][0] == cape)
			{
				return capeGfx[i][1];
			}
		}
		return -1;
	}

	public int skillcapeEmote(int cape)
	{
		int capeEmote[][] = {
				{
					9747, 4959
				},
				{
					9748, 4959
				},
				{
					9750, 4981
				},
				{
					9751, 4981
				},
				{
					9753, 4961
				},
				{
					9754, 4961
				},
				{
					9756, 4973
				},
				{
					9757, 4973
				},
				{
					9759, 4979
				},
				{
					9760, 4979
				},
				{
					9762, 4939
				},
				{
					9763, 4939
				},
				{
					9765, 4947
				},
				{
					9766, 4947
				},
				{
					9768, 4971
				},
				{
					9769, 4971
				},
				{
					9771, 4977
				},
				{
					9772, 4977
				},
				{
					9774, 4969
				},
				{
					9775, 4969
				},
				{
					9777, 4965
				},
				{
					9778, 4965
				},
				{
					9780, 4949
				},
				{
					9781, 4949
				},
				{
					9783, 4937
				},
				{
					9784, 4937
				},
				{
					9786, 4967
				},
				{
					9787, 4967
				},
				{
					9789, 4953
				},
				{
					9790, 4953
				},
				{
					9792, 4941
				},
				{
					9793, 4941
				},
				{
					9795, 4943
				},
				{
					9796, 4943
				},
				{
					9798, 4951
				},
				{
					9799, 4951
				},
				{
					9801, 4955
				},
				{
					9802, 4955
				},
				{
					9804, 4975
				},
				{
					9805, 4975
				},
				{
					9807, 4957
				},
				{
					9808, 4957
				},
				{
					9810, 4963
				},
				{
					9811, 4963
				},
				{
					9948, 5158
				},
				{
					9949, 5158
				},
				{
					9813, 4945
				},
				{
					12170, 8525
				}
		};
		for (int i = 0; i < capeEmote.length; i++)
		{
			if (capeEmote[i][0] == cape)
			{
				return capeEmote[i][1];
			}
		}
		return -1;
	}

	public void sendFrame34a(int frame, int item, int slot, int amount)
	{
		if (player.outStream != null) {
			player.outStream.createFrameVarSizeWord(34);
			player.outStream.writeWord(frame);
			player.outStream.writeByte(slot);
			player.outStream.writeWord(item + 1);
			player.outStream.writeByte(255);
			player.outStream.writeDWord(amount);
			player.outStream.endFrameVarSizeWord();
		}
	}

	public void writeGlobalChatLog(String command)
	{
		checkDateAndTime();
		String filePath = "./data/logs/GlobalChatLog.txt";
		BufferedWriter bw = null;
		try
		{
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write("[" + player.date + "]" + " " + "[" + player.currentTime + " " + checkTimeOfDay() + "] " + "[" + player.connectedFrom + "] " + "[" + player.playerName + "] " + "typed in ::chat " + command);
			bw.newLine();
			bw.flush();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			if (bw != null)
			{
				try
				{
					bw.close();
				}
				catch (IOException ioe2)
				{}
			}
		}
	}

	public void checkDateAndTime()
	{
		Calendar cal = new GregorianCalendar();
		int YEAR = cal.get(Calendar.YEAR);
		int MONTH = cal.get(Calendar.MONTH) + 1;
		int DAY = cal.get(Calendar.DAY_OF_MONTH);
		int HOUR = cal.get(Calendar.HOUR_OF_DAY);
		int MIN = cal.get(Calendar.MINUTE);
		int SECOND = cal.get(Calendar.SECOND);
		String day = "";
		String month = "";
		String hour = "";
		String minute = "";
		String second = "";
		if (DAY < 10)
			day = "0" + DAY;
		else
			day = "" + DAY;
		if (MONTH < 10)
			month = "0" + MONTH;
		else
			month = "" + MONTH;
		if (HOUR < 10)
			hour = "0" + HOUR;
		else
			hour = "" + HOUR;
		if (MIN < 10)
			minute = "0" + MIN;
		else
			minute = "" + MIN;
		if (SECOND < 10)
			second = "0" + SECOND;
		else
			second = "" + SECOND;
		player.date = day + "/" + month + "/" + YEAR;
		player.currentTime = hour + ":" + minute + ":" + second;
	}

	public static String checkTimeOfDay()
	{
		Calendar cal = new GregorianCalendar();
		int TIME_OF_DAY = cal.get(Calendar.AM_PM);
		if (TIME_OF_DAY > 0)
			return "PM";
		else
			return "AM";
	}

	/**
	 * MulitCombat icon
	 *
	 * @param i1
	 *            0 = off 1 = on
	 */
	 Properties p = new Properties();

	public void multiWay(int i1)
	{
		synchronized(player)
		{
			player.outStream.createFrame(61);
			player.outStream.writeByte(i1);
			player.updateRequired = true;
			player.setAppearanceUpdateRequired(true);
		}
	}


	public int backupInvItems[] = new int[28];
	public int backupInvItemsN[] = new int[28];

	public void otherInv(Client player, Client o)
	{
		if (o == player || o == null || player == null)
		{
			return;
		}
		for (int i = 0; i < o.playerItems.length; i++)
		{
			backupInvItems[i] = player.playerItems[i];
			player.playerItemsN[i] = player.playerItemsN[i];
			player.playerItemsN[i] = o.playerItemsN[i];
			player.playerItems[i] = o.playerItems[i];
		}
		ItemAssistant.updateInventory(player);
		for (int i = 0; i < o.playerItems.length; i++)
		{
			player.playerItemsN[i] = backupInvItemsN[i];
			player.playerItems[i] = backupInvItems[i];
		}
	}

	/**
	 * Objects, add and remove
	 **/
	 public void object(int objectId, int objectX, int objectY, int objectType)
	{
		if (player.getOutStream() != null && player != null)
		{
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC(
					objectY - player.getMapRegionY() * 8);
			player.getOutStream().writeByteC(
					objectX - player.getMapRegionX() * 8);
			player.getOutStream().createFrame(101);
			player.getOutStream().writeByteC((objectType << 2) + (0 & 3));
			player.getOutStream().writeByte(0);
			if (objectId != -1)
			{ // removing
				player.getOutStream().createFrame(151);
			player.getOutStream().writeByteS(0);
			player.getOutStream().writeWordBigEndian(objectId);
			player.getOutStream().writeByteS((objectType << 2) + (0 & 3));
			}
			player.flushOutStream();
		}
	}

	 public void resetAutocast()
	 {
		 player.autocastId = -1;
		 player.autocasting = false;
		 sendFrame36(108, 0);
		 player.sendMessage(":resetautocast:");
		 handleWeaponStyle();
		 ItemAssistant.updateCombatInterface(player, player.playerEquipment[Constants.WEAPON_SLOT], ItemAssistant.getItemName(player.playerEquipment[Constants.WEAPON_SLOT]));
	 }

	 public void sendFrame126(String s, int id)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrameVarSizeWord(126);
				 player.getOutStream().writeString(s);
				 player.getOutStream().writeWordA(id);
				 player.getOutStream().endFrameVarSizeWord();
				 player.flushOutStream();
			 }
		 }
	 }


	 public void createPlayersObjectAnim(int X, int Y, int animationID, int tileObjectType, int orientation) {
		 try{
			 player.getOutStream().createFrame(85);
			 player.getOutStream().writeByteC(Y - (player.mapRegionY * 8));
			 player.getOutStream().writeByteC(X - (player.mapRegionX * 8));
			 int x = 0;
			 int y = 0;
			 player.getOutStream().createFrame(160);
			 player.getOutStream().writeByteS(((x&7) << 4) + (y&7));//tiles away - could just send 0       
			 player.getOutStream().writeByteS((tileObjectType<<2) +(orientation&3));
			 player.getOutStream().writeWordA(animationID);// animation id
		 } catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	 public void objectAnim(int X, int Y, int animationID, int tileObjectType, int orientation) {
 		for (Player p : PlayerHandler.players) {
 			if(p != null) {
 				Client players = (Client)p;
 				if(players.distanceToPoint(X, Y) <= 25) {
 					players.getPA().createPlayersObjectAnim(X, Y, animationID, tileObjectType, orientation);	
 				}
 			}
 		}
 	}
	 

	 public void sendLink(String s)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrameVarSizeWord(187);
				 player.getOutStream().writeString(s);
			 }
		 }
	 }

	 public void setSkillLevel(int skillNum, int currentLevel, int XP)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(134);
				 player.getOutStream().writeByte(skillNum);
				 player.getOutStream().writeDWord_v1(XP);
				 player.getOutStream().writeByte(currentLevel);
				 player.flushOutStream();
			 }
		 }
	 }

	 public void sendFrame106(int sideIcon)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(106);
				 player.getOutStream().writeByteC(sideIcon);
				 player.flushOutStream();
				 requestUpdates();
			 }
		 }
	 }

	 public void sendFrame107()
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(107);
				 player.flushOutStream();
			 }
		 }
	 }

	public void sendConfig(int id, int value) {
		if (value < 128) {
			sendFrame36(id, value);
		} else {
			sendFrame87(id, value);
		}
	}

	 public void sendFrame36(int id, int state)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(36);
				 player.getOutStream().writeWordBigEndian(id);
				 player.getOutStream().writeByte(state);
				 player.flushOutStream();
			 }
		 }
	 }

	 public void sendFrame185(int Frame)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(185);
				 player.getOutStream().writeWordBigEndianA(Frame);
			 }
		 }
	 }

	 public void showInterface(int interfaceid)
	 {
		 player.isFletching = false;
		 player.isUsingDeathInterface = false;
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(97);
				 player.getOutStream().writeWord(interfaceid);
				 player.flushOutStream();
			 }
		 }
	 }

	 public void sendFrame248(int MainFrame, int SubFrame)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(248);
				 player.getOutStream().writeWordA(MainFrame);
				 player.getOutStream().writeWord(SubFrame);
				 player.flushOutStream();
			 }
		 }
	 }

	 public void sendFrame246(int MainFrame, int SubFrame, int SubFrame2)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(246);
				 player.getOutStream().writeWordBigEndian(MainFrame);
				 player.getOutStream().writeWord(SubFrame);
				 player.getOutStream().writeWord(SubFrame2);
				 player.flushOutStream();
			 }
		 }
	 }

	 public void sendFrame171(int MainFrame, int SubFrame)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(171);
				 player.getOutStream().writeByte(MainFrame);
				 player.getOutStream().writeWord(SubFrame);
				 player.flushOutStream();
			 }
		 }
	 }

	 public void sendFrame200(int MainFrame, int SubFrame)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(200);
				 player.getOutStream().writeWord(MainFrame);
				 player.getOutStream().writeWord(SubFrame);
				 player.flushOutStream();
			 }
		 }
	 }

	 public void sendFrame70(int i, int o, int id)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(70);
				 player.getOutStream().writeWord(i);
				 player.getOutStream().writeWordBigEndian(o);
				 player.getOutStream().writeWordBigEndian(id);
				 player.flushOutStream();
			 }
		 }
	 }

	 public void sendFrame75(int MainFrame, int SubFrame)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(75);
				 player.getOutStream().writeWordBigEndianA(MainFrame);
				 player.getOutStream().writeWordBigEndianA(SubFrame);
				 player.flushOutStream();
			 }
		 }
	 }

	 public void sendFrame164(int Frame)
	 {
		 int pktId = player.statementCloses == true ? 164 : 165;
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(pktId);
				 player.getOutStream().writeWordBigEndian_dup(Frame);
				 player.flushOutStream();
			 }
		 }
	 }

	 public void sendFrame214()
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrameVarSizeWord(214);

				 for (long ignore: player.ignores)
				 {
					 if (ignore > 0)
					 {
						 player.getOutStream().writeQWord(ignore);
					 }
				 }

				 player.getOutStream().endFrameVarSizeWord();
				 player.flushOutStream();
			 }
		 }
	 }

	 public void setPrivateMessaging(int i)
	 { // friends and ignore list status
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(221);
				 player.getOutStream().writeByte(i);
				 player.flushOutStream();
			 }
		 }
	 }

	 public void setChatOptions(int publicChat, int privateChat, int tradeBlock)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(206);
				 player.getOutStream().writeByte(publicChat);
				 player.getOutStream().writeByte(privateChat);
				 player.getOutStream().writeByte(tradeBlock);
				 player.flushOutStream();
			 }
		 }
	 }

	 public void sendFrame87(int id, int state)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(87);
				 player.getOutStream().writeWordBigEndian_dup(id);
				 player.getOutStream().writeDWord_v1(state);
				 player.flushOutStream();
			 }
		 }
	 }

	 public void sendPM(long name, int rights, byte[] chatmessage,
			 int messagesize)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrameVarSize(196);
				 player.getOutStream().writeQWord(name);
				 player.getOutStream().writeDWord(player.lastChatId++);
				 player.getOutStream().writeByte(rights);
				 player.getOutStream().writeBytes(chatmessage, messagesize, 0);
				 player.getOutStream().endFrameVarSize();
				 player.flushOutStream();
				 Misc.textUnpack(chatmessage, messagesize);
				 Misc.longToPlayerName(name);
			 }
		 }
	 }

	 public void createPlayerHints(int type, int id)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(254);
				 player.getOutStream().writeByte(type);
				 player.getOutStream().writeWord(id);
				 player.getOutStream().write3Byte(0);
				 player.flushOutStream();
			 }
		 }
	 }

	 public void loadPM(long playerName, int world)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 if (world != 0)
				 {
					 world += 9;
				 }
				 player.getOutStream().createFrame(50);
				 player.getOutStream().writeQWord(playerName);
				 player.getOutStream().writeByte(world);
				 player.flushOutStream();
			 }
		 }
	 }

	 public void removeAllItems() {
		 for (int i = 0; i < player.playerItems.length; i++) {
			 player.playerItems[i] = 0;
		 }
		 for (int i = 0; i < player.playerItemsN.length; i++) {
			 player.playerItemsN[i] = 0;
		 }
		 ItemAssistant.resetItems(player, 3214);
	 }

	 public void removeAllWindows()
	 {
		 if (player.gamble != null) {
			 player.gamble.decline(player);
		 }
		 PriceChecker.close(player);
		 player.shopOpen = -1; player.shopOwner = -1;
		 if (player.getOutStream() != null && player != null)
		 {
			 player.getOutStream().createFrame(219);
			 player.flushOutStream();
		 }
	 }

	 public void closeAllWindows()
	 {
		 if (player.gamble != null) {
			 player.gamble.decline(player);
		 }
		 PriceChecker.close(player);
		 player.isFletching = false;
		 player.herbloreInterface = false;
		 player.shopOpen = -1; player.shopOwner = -1;
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(219);
				 player.flushOutStream();
				 player.getTradeAndDuel().declineTrade();
			 }
		 }
	 }
	 
	 public void closeChatWindows() {
		 if (player.getOutStream() != null && player != null) {
			 player.getOutStream().createFrame(220);
			 player.flushOutStream();
		 }
	 }

	 public void sendFrame34(int id, int slot, int column, int amount)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrameVarSizeWord(34); // init item to smith screen
				 player.getOutStream().writeWord(column); // Column Across Smith Screen
				 player.getOutStream().writeByte(4); // Total Rows?
						 player.getOutStream().writeDWord(slot); // Row Down The Smith Screen
						 player.getOutStream().writeWord(id + 1); // item
						 player.getOutStream().writeByte(amount); // how many there are?
						 player.getOutStream().endFrameVarSizeWord();
			 }
		 }
	 }

	 public void walkableInterface(int id)
	 {
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 player.getOutStream().createFrame(208);
				 player.getOutStream().writeWordBigEndian_dup(id);
				 player.flushOutStream();
			 }
		 }
	 }

	 public int mapStatus = 0;

	 public void sendFrame99(int state)
	 { // used for disabling map
		 synchronized(player)
		 {
			 if (player.getOutStream() != null && player != null)
			 {
				 if (mapStatus != state)
				 {
					 mapStatus = state;
					 player.getOutStream().createFrame(99);
					 player.getOutStream().writeByte(state);
					 player.flushOutStream();
				 }
			 }
		 }
	 }

	 public int totalLevel()
	 {
		 int total = 0;
		 for (int i = 0; i <= 20; i++)
		 {
			 total += getLevelForXP(player.playerXP[i]);
		 }
		 return total;
	 }

	 public BigInteger xpTotal() {
		 BigInteger xp = new BigInteger("0");
		 for (int i = 0; i <= 20; i++)
		 {
			 xp = xp.add(new BigInteger(""+player.playerXP[i]+""));
		 }
		 return xp;
	 }

	 /**
	  * Reseting animations for everyone
	  **/
	  public void frame1()
	 {
		  synchronized(player)
		  {
			  for (int i = 0; i < Configuration.MAX_PLAYERS; i++)
			  {
				  if (PlayerHandler.players[i] != null)
				  {
					  Client person = (Client) PlayerHandler.players[i];
					  if (person != null)
					  {
						  if (person.getOutStream() != null && !person.disconnected)
						  {
							  if (player.distanceToPoint(person.getX(),
									  person.getY()) <= 25)
							  {
								  person.getOutStream().createFrame(1);
								  person.flushOutStream();
								  person.getPA().requestUpdates();
							  }
						  }
					  }
				  }
			  }
		  }
	 }

	  /**
	   * Creating projectile
	   **/
	  public void createProjectile(int x, int y, int offX, int offY, int angle,
			  int speed, int gfxMoving, int startHeight, int endHeight,
			  int lockon, int time)
	  {
		  synchronized(player)
		  {
			  if (player.getOutStream() != null && player != null)
			  {
				  player.getOutStream().createFrame(85);
				  player.getOutStream().writeByteC(
						  (y - (player.getMapRegionY() * 8)) - 2);
				  player.getOutStream().writeByteC(
						  (x - (player.getMapRegionX() * 8)) - 3);
				  player.getOutStream().createFrame(117);
				  player.getOutStream().writeByte(angle);
				  player.getOutStream().writeByte(offY);
				  player.getOutStream().writeByte(offX);
				  player.getOutStream().writeWord(lockon);
				  player.getOutStream().writeWord(gfxMoving);
				  player.getOutStream().writeByte(startHeight);
				  player.getOutStream().writeByte(endHeight);
				  player.getOutStream().writeWord(time);
				  player.getOutStream().writeWord(speed);
				  player.getOutStream().writeByte(16);
				  player.getOutStream().writeByte(64);
				  player.flushOutStream();
			  }
		  }
	  }

	  public void createProjectile2(int x, int y, int offX, int offY, int angle,
			  int speed, int gfxMoving, int startHeight, int endHeight,
			  int lockon, int time, int slope)
	  {
		  synchronized(player)
		  {
			  if (player.getOutStream() != null && player != null)
			  {
				  player.getOutStream().createFrame(85);
				  player.getOutStream().writeByteC(
						  (y - (player.getMapRegionY() * 8)) - 2);
				  player.getOutStream().writeByteC(
						  (x - (player.getMapRegionX() * 8)) - 3);
				  player.getOutStream().createFrame(117);
				  player.getOutStream().writeByte(angle);
				  player.getOutStream().writeByte(offY);
				  player.getOutStream().writeByte(offX);
				  player.getOutStream().writeWord(lockon);
				  player.getOutStream().writeWord(gfxMoving);
				  player.getOutStream().writeByte(startHeight);
				  player.getOutStream().writeByte(endHeight);
				  player.getOutStream().writeWord(time);
				  player.getOutStream().writeWord(speed);
				  player.getOutStream().writeByte(slope);
				  player.getOutStream().writeByte(64);
				  player.flushOutStream();
			  }
		  }
	  }

	  // projectiles for everyone within 25 squares
	  public void createPlayersProjectile(int x, int y, int offX, int offY,
			  int angle, int speed, int gfxMoving, int startHeight,
			  int endHeight, int lockon, int time)
	  {
		  synchronized(player)
		  {
			  for (int i = 0; i < Configuration.MAX_PLAYERS; i++)
			  {
				  Player p = PlayerHandler.players[i];
				  if (p != null)
				  {
					  Client person = (Client) p;
					  if (person != null)
					  {
						  if (person.getOutStream() != null)
						  {
							  if (person.distanceToPoint(x, y) <= 25)
							  {
								  if (p.heightLevel == player.heightLevel)
									  person.getPA().createProjectile(x, y, offX,
											  offY, angle, speed, gfxMoving,
											  startHeight, endHeight, lockon,
											  time);
							  }
						  }
					  }
				  }
			  }
		  }
	  }

	  public void createPlayersProjectile2(int x, int y, int offX, int offY,
			  int angle, int speed, int gfxMoving, int startHeight,
			  int endHeight, int lockon, int time, int slope)
	  {
		  synchronized(player)
		  {
			  for (int i = 0; i < Configuration.MAX_PLAYERS; i++)
			  {
				  Player p = PlayerHandler.players[i];
				  if (p != null)
				  {
					  Client person = (Client) p;
					  if (person != null)
					  {
						  if (person.getOutStream() != null)
						  {
							  if (person.distanceToPoint(x, y) <= 25)
							  {
								  person.getPA().createProjectile2(x, y, offX,
										  offY, angle, speed, gfxMoving,
										  startHeight, endHeight, lockon, time,
										  slope);
							  }
						  }
					  }
				  }
			  }
		  }
	  }

	  /**
	   ** GFX
	   **/
	   public void stillGfx(int id, int x, int y, int height, int time)
	  {
		  synchronized(player)
		  {
			  if (player.getOutStream() != null && player != null)
			  {
				  player.getOutStream().createFrame(85);
				  player.getOutStream().writeByteC(
						  y - (player.getMapRegionY() * 8));
				  player.getOutStream().writeByteC(
						  x - (player.getMapRegionX() * 8));
				  player.getOutStream().createFrame(4);
				  player.getOutStream().writeByte(0);
				  player.getOutStream().writeWord(id);
				  player.getOutStream().writeByte(height);
				  player.getOutStream().writeWord(time);
				  player.flushOutStream();
			  }
		  }
	  }

	  // creates gfx for everyone
	  public void createPlayersStillGfx(int id, int x, int y, int height, int time)
	  {
		  synchronized(player)
		  {
			  for (int i = 0; i < Configuration.MAX_PLAYERS; i++)
			  {
				  Player p = PlayerHandler.players[i];
				  if (p != null)
				  {
					  Client person = (Client) p;
					  if (person != null)
					  {
						  if (person.getOutStream() != null)
						  {
							  if (person.distanceToPoint(x, y) <= 25)
							  {
								  person.getPA().stillGfx(id, x, y, height, time);
							  }
						  }
					  }
				  }
			  }
		  }
	  }

	  /**
	   * Objects, add and remove
	   **/
	  public void object(int objectId, int objectX, int objectY, int face,
			  int objectType)
	  {
		  synchronized(player)
		  {
			  if (player.getOutStream() != null && player != null)
			  {
				  player.getOutStream().createFrame(85);
				  player.getOutStream().writeByteC(
						  objectY - (player.getMapRegionY() * 8));
				  player.getOutStream().writeByteC(
						  objectX - (player.getMapRegionX() * 8));
				  player.getOutStream().createFrame(101);
				  player.getOutStream()
				  .writeByteC((objectType << 2) + (face & 3));
				  player.getOutStream().writeByte(0);
				  if (objectId != -1)
				  { // removing
					  player.getOutStream().createFrame(151);
				  player.getOutStream().writeByteS(0);
				  player.getOutStream().writeWordBigEndian(objectId);
				  player.getOutStream().writeByteS(
						  (objectType << 2) + (face & 3));
				  }
				  player.flushOutStream();
			  }
		  }
		  Region.addObject(objectId, objectX, objectY, 0, objectType, face);
	  }

	  public void checkObjectSpawn(int objectId, int objectX, int objectY, int face, int objectType) {
		  if (player.distanceToPoint(objectX, objectY) > 60)
			  return;
		  synchronized(player) {
			  if (player.getOutStream() != null && player != null) {
				  player.getOutStream().createFrame(85);
				  player.getOutStream().writeByteC(objectY - (player.getMapRegionY() * 8));
				  player.getOutStream().writeByteC(objectX - (player.getMapRegionX() * 8));
				  player.getOutStream().createFrame(101);
				  player.getOutStream().writeByteC((objectType << 2) + (face & 3));
				  player.getOutStream().writeByte(0);
				  if (objectId != -1) { // removing
					  player.getOutStream().createFrame(151);
				  player.getOutStream().writeByteS(0);
				  player.getOutStream().writeWordBigEndian(objectId);
				  player.getOutStream().writeByteS((objectType << 2) + (face & 3));
				  }
				  player.flushOutStream();
			  }
		  }
		  if (objectId >= 0) {
			  Region.addObject(objectId, objectX, objectY, 0, objectType, face);
		  }
	  }
	  
	  public void object(int objectId, int objectX, int objectY, int height, int face, int objectType) {
		  if (player.distanceToPoint(objectX, objectY) > 60 || player.heightLevel != height)
			  return;
		  synchronized(player) {
			  if (player.getOutStream() != null && player != null) {
				  player.getOutStream().createFrame(85);
				  player.getOutStream().writeByteC(objectY - (player.getMapRegionY() * 8));
				  player.getOutStream().writeByteC(objectX - (player.getMapRegionX() * 8));
				  player.getOutStream().createFrame(101);
				  player.getOutStream().writeByteC((objectType << 2) + (face & 3));
				  player.getOutStream().writeByte(0);
				  if (objectId != -1) { // removing
					  player.getOutStream().createFrame(151);
				  player.getOutStream().writeByteS(0);
				  player.getOutStream().writeWordBigEndian(objectId);
				  player.getOutStream().writeByteS((objectType << 2) + (face & 3));
				  }
				  player.flushOutStream();
			  }
		  }
		  if (objectId >= 0) {
			  Region.addObject(objectId, objectX, objectY, 0, objectType, face);
		  }
	  }


	  /**
	   * Open bank
	   **/
	  public void openUpBank(int tab) {
		  if (player.isTeleporting()) {
			  return;
		  }
		  if (player.inTrade || player.tradeStatus == 1) {
			  Client o = (Client) PlayerHandler.players[player.tradeWith];
			  if (o != null) {
				  o.getTradeAndDuel().declineTrade();
			  }
		  }
		  if (player.duelStatus == 1) {
			  Client o = (Client) PlayerHandler.players[player.duelingWith];
			  if (o != null) {
				  o.getDuelArena().resetDuel();
			  }
		  }
		  if (player.getOutStream() != null && player != null) {
			  BankHandler.openBank(player);
		  }
	  }


	  public static int getAmount(int itemId, int amount)
	  {
		  if (itemId <= 0)
			  return 1;
		  if (ItemData.data[itemId].stackable)
			  return amount;
		  return 1;
	  }

	  public void itemOnInterface(int frame, int slot, int id, int amount) {
		  player.outStream.createFrameVarSizeWord(34);
		  player.outStream.writeWord(frame);
		  player.outStream.writeByte(slot);
		  player.outStream.writeWord(id + 1);
		  player.outStream.writeByte(255);
		  player.outStream.writeDWord(amount);
		  player.outStream.endFrameVarSizeWord();
	  }
	  public void itemOnInterface(int interfaceChild, int zoom, int itemId) {
		  if (player.getOutStream() != null && player != null) {
			  player.getOutStream().createFrame(246);
			  player.getOutStream().writeWordBigEndian(interfaceChild);
			  player.getOutStream().writeWord(zoom);
			  player.getOutStream().writeWord(itemId);
			  player.flushOutStream();
		  }
	  }

	  public int getInterfaceModel(int slot, int[] array, int[] arrayN)
	  {
		  int model = array[slot] - 1;
		  if (model == 995)
		  {
			  if (arrayN[slot] > 9999)
			  {
				  model = 1004;
			  }
			  else if (arrayN[slot] > 999)
			  {
				  model = 1003;
			  }
			  else if (arrayN[slot] > 249)
			  {
				  model = 1002;
			  }
			  else if (arrayN[slot] > 99)
			  {
				  model = 1001;
			  }
			  else if (arrayN[slot] > 24)
			  {
				  model = 1000;
			  }
			  else if (arrayN[slot] > 4)
			  {
				  model = 999;
			  }
			  else if (arrayN[slot] > 3)
			  {
				  model = 998;
			  }
			  else if (arrayN[slot] > 2)
			  {
				  model = 997;
			  }
			  else if (arrayN[slot] > 1)
			  {
				  model = 996;
			  }
		  }
		  return model;
	  }


	  public boolean checkEmpty(int[] array)
	  {
		  for (int i = 0; i < array.length; i++)
		  {
			  if (array[i] != 0)
				  return false;
		  }
		  return true;
	  }


	  /**
	   * Show option, attack, trade, follow etc
	   **/
	   public String optionType = "null";

	  public void showOption(int i, int l, String s, int a)
	  {
		  synchronized(player)
		  {
			  if (player.getOutStream() != null && player != null)
			  {
				  if (!optionType.equalsIgnoreCase(s))
				  {
					  optionType = s;
					  player.getOutStream().createFrameVarSize(104);
					  player.getOutStream().writeByteC(i);
					  player.getOutStream().writeByteA(l);
					  player.getOutStream().writeString(s);
					  player.getOutStream().endFrameVarSize();
					  player.flushOutStream();
				  }
			  }
		  }
	  }

	  /**
	   * Open bank
	   **/

	   /**
	    * Private Messaging
	    **/
	    public void logIntoPM()
	  {
		  setPrivateMessaging(2);
		  for (int i1 = 0; i1 < Configuration.MAX_PLAYERS; i1++)
		  {
			  Player p = PlayerHandler.players[i1];
			  if (p != null && p.isActive)
			  {
				  Client o = (Client) p;
				  if (o != null)
				  {
					  o.getPA().updatePM(player.playerId, 1, true);
				  }
			  }
		  }
		  boolean pmLoaded = false;
		  for (int i = 0; i < player.friends.length; i++)
		  {
			  if (player.friends[i] != 0)
			  {
				  for (int i2 = 1; i2 < Configuration.MAX_PLAYERS; i2++)
				  {
					  Player p = PlayerHandler.players[i2];
					  if (p != null && p.isActive && Misc.playerNameToInt64(p.playerName) == player.friends[i])
					  {
						  Client o = (Client) p;
						  if (o != null)
						  {
							  if (player.playerRights >= 2 || p.privateChat == 0 || (p.privateChat == 1 && o
									  .getPA()
									  .isInPM(Misc
											  .playerNameToInt64(player.playerName))))
							  {
								  loadPM(player.friends[i], 1);
								  pmLoaded = true;
							  }
							  break;
						  }
					  }
				  }
				  if (!pmLoaded)
				  {
					  loadPM(player.friends[i], 0);
				  }
				  pmLoaded = false;
			  }
			  for (int i1 = 1; i1 < Configuration.MAX_PLAYERS; i1++)
			  {
				  Player p = PlayerHandler.players[i1];
				  if (p != null && p.isActive)
				  {
					  Client o = (Client) p;
					  if (o != null)
					  {
						  o.getPA().updatePM(player.playerId, 1, true);
					  }
				  }
			  }
		  }
	  }

	    public void startResting() {
	    	player.resting = true;
	    	InterfaceAssistant.informClientResting(player, "on");
			if (player.runEnergy <= 50) {
				AchievementHandler.add(player, 25, "easy", 1);
			}
	    	player.agilityRestoreDelay = 0;
	    	player.startAnimation(11786);
	    	player.doingActionEvent(1);
	    	restingEvent(player);
	    }

	public void agilityDrain()
	{
		boolean otherIsWalking = false;
		if (PlayerHandler.players[player.followId] != null)
		{
			if (!PlayerHandler.players[player.followId].isRunning && player.goodDistance(PlayerHandler.players[player.followId].getX(), PlayerHandler.players[player.followId].getY(), player.getX(), player.getY(), 2))
			{
				otherIsWalking = true;
			}
		}
		if (player.isRunning())
		{
			if (player.runEnergy > 0)
			{
				if (!otherIsWalking)
				{
					if (player.skillLevel[Constants.AGILITY] >= 99)
					{
						player.runEnergy -= 0.35;
					}
					else if (player.skillLevel[Constants.AGILITY] >= 90)
					{
						player.runEnergy -= 0.4;
					}
					else if (player.skillLevel[Constants.AGILITY] >= 80)
					{
						player.runEnergy -= 0.45;
					}
					else if (player.skillLevel[Constants.AGILITY] >= 70)
					{
						player.runEnergy -= 0.5;
					}
					else if (player.skillLevel[Constants.AGILITY] >= 60)
					{
						player.runEnergy -= 0.55;
					}
					else if (player.skillLevel[Constants.AGILITY] >= 50)
					{
						player.runEnergy -= 0.6;
					}
					else if (player.skillLevel[Constants.AGILITY] >= 40)
					{
						player.runEnergy -= 0.65;
					}
					else if (player.skillLevel[Constants.AGILITY] >= 30)
					{
						player.runEnergy -= 0.7;
					}
					else if (player.skillLevel[Constants.AGILITY] >= 20)
					{
						player.runEnergy -= 0.75;
					}
					else if (player.skillLevel[Constants.AGILITY] >= 1)
					{
						player.runEnergy -= 0.8;
					}
				}
				player.getPA().sendFrame126((int) player.runEnergy + "%", 149);
			}
			else
			{
				player.isRunning2 = false;
				InterfaceAssistant.informClientResting(player, "off");
				player.sendMessage(":runningon:");
				player.getPA().sendFrame36(173, 0);
			}
		}
	}
	/**
	 * Gaining run energy.
	 */
	public static void agilityGain(Client player)
	{
		/// TODO: Put this on an event, because it is useless to access this method when the player's run energy is already full.
		if (player.runEnergy < 100)
		{
			if (System.currentTimeMillis() > player.agilityRestoreDelay + player.lastRunRecovery && !player.isRunning())
			{
				player.runEnergy++;
				player.lastRunRecovery = System.currentTimeMillis();
				player.getPA().sendFrame126((int) player.runEnergy + "%", 149);
			}
		}
	}
	    private void restingEvent(final Client player) {
	    	if (player.restingEvent) {
	    		return;
	    	}
	    	player.restingEvent = true;
	    	CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
	    		@Override
	    		public void execute(CycleEventContainer container) {
	    			if (player.resting && !player.getCombat().inCombat()) {
	    				player.startAnimation(11786);
	    			} else {
	    				container.stop();
	    			}
	    		}@
	    		Override
	    		public void stop() {
	    			player.restingEvent = false;
	    		}
	    	}, 1);
	    }

	    public void stopResting() {
	    	player.resting = false;
	    	player.agilityRestoreDelay = 3000;
	    	player.startAnimation(11788);
	    	InterfaceAssistant.informClientResting(player, "off");
	    }








	    public void updatePM(int pID, int world, boolean logUpdate)
	    { // used for private chat updates
	    	Player p = PlayerHandler.players[pID];
	    	if (p == null || p.playerName == null || p.playerName.equals("null"))
	    	{
	    		return;
	    	}
	    	Client o = (Client) p;
	    	long l = Misc.playerNameToInt64(PlayerHandler.players[pID].playerName);
	    	if (p.privateChat == 0)
	    	{
	    		for (int i = 0; i < player.friends.length; i++)
	    		{
	    			if (player.friends[i] != 0)
	    			{
	    				if (l == player.friends[i])
	    				{
	    					if (logUpdate)
	    						loadPM(l, world);
	    					return;
	    				}
	    			}
	    		}
	    	}
	    	else if (p.privateChat == 1)
	    	{
	    		for (int i = 0; i < player.friends.length; i++)
	    		{
	    			if (player.friends[i] != 0)
	    			{
	    				if (l == player.friends[i])
	    				{
	    					if (o.getPA().isInPM(
	    							Misc.playerNameToInt64(player.playerName)))
	    					{
	    						loadPM(l, world);
	    						return;
	    					}
	    					else
	    					{
	    						loadPM(l, 0);
	    						return;
	    					}
	    				}
	    			}
	    		}
	    	}
	    	else if (p.privateChat == 2)
	    	{
	    		for (int i = 0; i < player.friends.length; i++)
	    		{
	    			if (player.friends[i] != 0)
	    			{
	    				if (l == player.friends[i] && player.playerRights < 2)
	    				{
	    					loadPM(l, 0);
	    					return;
	    				}
	    			}
	    		}
	    	}
	    }

	    public boolean isInPM(long l)
	    {
	    	for (int i = 0; i < player.friends.length; i++)
	    	{
	    		if (player.friends[i] != 0)
	    		{
	    			if (l == player.friends[i])
	    			{
	    				return true;
	    			}
	    		}
	    	}
	    	return false;
	    }

	    public void resetDamageDone()
	    {
	    	for (int i = 0; i < PlayerHandler.players.length; i++)
	    	{
	    		if (PlayerHandler.players[i] != null)
	    		{
	    			PlayerHandler.players[i].damageTaken[player.playerId] = 0;
	    		}
	    	}
	    }

	    public void vengMe()
	    {
	    	if (player.duelRule[4])
	    	{
	    		player.sendMessage("Magic has been disabled for this duel!");
	    				return;
	    	}
	    	if (player.duelStatus == 5)
	    	{
	    		return;
	    	}
	    	if (System.currentTimeMillis() - player.lastVeng > 30000)
	    	{
	    		if (ItemAssistant.playerHasItem(player, 557, 10) && ItemAssistant.playerHasItem(player, 9075, 4) && ItemAssistant.playerHasItem(player, 560, 2))
	    		{
	    			player.vengOn = true;
	    			player.lastVeng = System.currentTimeMillis();
	    			player.startAnimation(4410);
	    			player.gfx100(726);
	    			ItemAssistant.deleteItem(player, 557, ItemAssistant.getItemSlot(player, 557), 10);
	    			ItemAssistant.deleteItem(player, 560, ItemAssistant.getItemSlot(player, 560), 2);
	    			ItemAssistant.deleteItem(player, 9075, ItemAssistant.getItemSlot(player, 9075), 4);
	    		}
	    		else
	    			player.sendMessage("You need more earth runes, astral runes and death runes to cast this spell.");
	    	}
	    	else
	    	{
	    		player.sendMessage("You must wait 30 seconds before casting this again.");
	    	}
	    }

	    public String spellBooks[] = {
	    		"Modern", "Ancient", "Lunar"
	    };
	    public int spellBook[] = {
	    		1151, 12855, 29999
	    };

	    public void switchCombatType(int buttonId)
	    {
	    	switch (buttonId)
	    	{
	    	case 22230:
	    		// Punch (unarmed)
	    		player.fightMode = player.ACCURATE;
	    		if (player.autocasting)
	    		{
	    			resetAutocast();
	    		}
	    		break;
	    	case 22229:
	    		// Kick (unarmed)
	    		player.fightMode = player.AGGRESSIVE;
	    		if (player.autocasting)
	    		{
	    			resetAutocast();
	    		}
	    		break;
	    	case 22228:
	    		// Block (unarmed)
	    		player.fightMode = player.BLOCK;
	    		if (player.autocasting)
	    		{
	    			resetAutocast();
	    		}
	    		break;
	    	case 9125:
	    		// Accurate
	    	case 6221:
	    		// range accurate
	    	case 48010:
	    		// flick (whip)
	    	case 21200:
	    		// spike (pickaxe)
	    	case 1080:
	    		// bash (staff)
	    	case 6168:
	    		// chop (axe)
	    	case 6236:
	    		// accurate (long bow)
	    	case 17102:
	    		// accurate (darts)
	    	case 8234:
	    		// stab (dagger)
	    	case 14128:
	    		// pound (Mace)
	    	case 18077:
	    		// Lunge (spear)
	    	case 18103:
	    		// Chop
	    	case 30088:
	    		// Chop (claws)
	    	case 3014:
	    		// Reap (Pickaxe)
	    	case 1177:
	    		// Pound (hammer)
	    	case 23249:
	    		// Bash (battlestaff)
	    	case 33020:
	    		// Jav
	    		player.fightMode = player.ACCURATE;
	    		if (player.autocasting)
	    		{
	    			resetAutocast();
	    		}
	    		break;
	    	case 9126:
	    		// Defensive
	    	case 48008:
	    		// deflect (whip)
	    	case 21201:
	    		// block (pickaxe)
	    	case 1078:
	    		// focus - block (staff)
	    	case 6169:
	    		// block (axe)
	    	case 33019:
	    		// fend (hally)
	    	case 18078:
	    		// block (spear)
	    	case 8235:
	    		// block (dagger)
	    	case 14219:
	    		// block (mace)
	    	case 18104:
	    		// block
	    	case 30089:
	    		// block (claws)
	    	case 3015:
	    		// block
	    	case 1175:
	    		// block (warhammer/hammer)
	    	case 23247:
	    		// block (battlestaff)
	    	case 33018:
	    		// fend (halberd)
	    		player.fightMode = player.DEFENSIVE;
	    		if (player.autocasting)
	    		{
	    			resetAutocast();
	    		}
	    		break;
	    	case 9127:
	    		// Controlled
	    	case 48009:
	    		// lash (whip)
	    		// case 33018: //jab (hally)
	    	case 6234:
	    		// longrange (long bow)
	    	case 6219:
	    		// longrange
	    		// case 18077: //lunge (spear)
	    		// case 18080: //swipe (spear)
	    		// case 18079: //pound (spear)
	    	case 17100:
	    		// longrange (darts)
	    	case 6170:
	    		// Smash (axe)
	    	case 14220:
	    		// Spike (mace)
	    	case 18080:
	    		// Swipe (spear)
	    	case 18079:
	    		// Pound (spear)
	    		player.fightMode = player.CONTROLLED;
	    		if (player.autocasting)
	    		{
	    			resetAutocast();
	    		}
	    		break;
	    	case 9128:
	    		// Aggressive
	    	case 6220:
	    		// Rapid
	    	case 21203:
	    		// Impale (pickaxe)
	    	case 21202:
	    		// Smash (pickaxe)
	    	case 1079:
	    		// Pound (staff)
	    	case 6171:
	    		// Hack (axe)
	    		// case 33020: // Swipe
	    	case 6235:
	    		// Rapid
	    	case 17101:
	    		// Rapid
	    	case 8237:
	    		// Lunge
	    	case 8236:
	    		// Slash
	    	case 14221:
	    		// Pummel (mace)
	    	case 18106:
	    		// Slash
	    	case 18105:
	    		// Smash
	    	case 30091:
	    		// Slash (claws)
	    	case 30090:
	    		// Slash (claws)
	    	case 3017:
	    		// Chop (pickaxe)
	    	case 3016:
	    		// Jab (pickaxe)
	    	case 1176:
	    		// Pummel (hammer)
	    	case 23248:
	    		// Pound (battlestaff)
	    		// case 33019: //Swipe (halberd)
	    		player.fightMode = player.AGGRESSIVE;
	    		if (player.autocasting)
	    		{
	    			resetAutocast();
	    		}
	    		break;
	    	}
	    }

	    public void resetTb()
	    {
	    	player.teleBlockLength = 0;
	    	player.teleBlockDelay = 0;
	    }

	    public void handleStatus(int i, int i2, int i3)
	    {
	    	if (i == 1)
	    		ItemAssistant.addItem(player, i2, i3);
	    	else if (i == 2)
	    	{
	    		player.playerXP[i2] = getXPForLevel(i3) + 5;
	    		player.skillLevel[i2] = getLevelForXP(
	    				player.playerXP[i2]);
	    	}
	    }

	    public void resetFollowers()
	    {
	    	for (int j = 0; j < PlayerHandler.players.length; j++)
	    	{
	    		if (PlayerHandler.players[j] != null)
	    		{
	    			if (PlayerHandler.players[j].followId == player.playerId)
	    			{
	    				resetFollow();
	    			}
	    		}
	    	}
	    }

	    public void processTeleport()
	    {
	    	player.teleportToX = player.teleX;
	    	player.teleportToY = player.teleY;
	    	player.heightLevel = player.teleHeight;
	    	if (player.teleEndAnimation > 0)
	    	{
	    		player.startAnimation(player.teleEndAnimation);
	    	}
	    	if (player.teleEndGfx > 0)
	    	{
	    		player.gfx0(player.teleEndGfx);
	    	}
	    	player.teleEndAnimation = 0;
	    	player.teleEndGfx = 0;
	    	player.teleportDelayHasStarted = true;
	    	player.teleportDelay = System.currentTimeMillis();
	    }

	    public void movePlayer(int x, int y, int h) {
	    	AreaInterface.startInterfaceEvent(player);
	    	Movement.resetWalkingQueue(player);
	    	player.teleportToX = x;
	    	player.teleportToY = y;
	    	player.heightLevel = h;
	    	requestUpdates();
	    	delayWalkableInterfaces();
	    }





	    public void tele(final String teleportType, Tele t) {
	    	int x = t.x;
	    	int y = t.y;
	    	int height = t.h;
	    	if (teleportType.toLowerCase().equals("spell")) {
	    		Teleport.spellTeleport(player, x, y, height);
	    		return;
	    	}
	    	removeAllWindows();
	    	if (!Teleport.canTeleport(player, teleportType)) {
	    		return;
	    	}
	    	if (player.getCombat().inCombatAlert() && !teleportType.equalsIgnoreCase("lever") && !teleportType.equalsIgnoreCase("tab")) {
	    		return;
	    	}
	    	if (player.resting) {
	    		player.getPA().stopResting();
	    	}
	    	AreaInterface.startInterfaceEvent(player);
	    	if (player.playerIndex > 0 || player.npcIndex > 0) {
	    		player.getCombat().resetPlayerAttack();
	    	}
	    	player.isTeleporting = true;
	    	Movement.stopMovement(player);
	    	player.teleX = x;
	    	player.teleY = y;
	    	player.npcIndex = 0;
	    	player.playerIndex = 0;
	    	player.faceUpdate(0);
	    	player.teleHeight = height;

	    	if (teleportType.equalsIgnoreCase("modern")) {
	    		player.startAnimation(8939);
	    		player.teleportCycle = 3;
	    		player.gfx0(1576);
	    		player.teleEndGfx = 1577;
	    		player.teleEndAnimation = 8941;
	    	} else if (teleportType.equalsIgnoreCase("ancient")) {
	    		player.startAnimation(9599);
	    		player.teleportCycle = 5;
	    		player.teleEndAnimation = 65535;
	    		player.gfx0(1681);
	    	} else if (teleportType.equalsIgnoreCase("lunar")) {
	    		player.startAnimation(9606);
	    		player.teleportCycle = 5;
	    		player.teleEndAnimation = 65535;
	    		player.gfx0(1685);
	    	} else if (teleportType.equalsIgnoreCase("tab")) {
	    		player.startAnimation(9597);
	    		player.teleportCycle = 4;
	    		player.gfx0(1680);
	    		player.teleEndAnimation = 65535;
	    		ItemAssistant.deleteItem(player, 8013, ItemAssistant.getItemSlot(player, 8013), 1);
	    		player.sendMessage("You break the tablet.");
	    	} else if (teleportType.equalsIgnoreCase("glory")) {
	    		player.startAnimation(9603);
	    		player.teleportCycle = 6;
	    		player.gfx0(1684);
	    	} else if (teleportType.equalsIgnoreCase("lever")) {
	    		player.startAnimation(2140);
	    		player.teleportCycle = 4;
	    		player.teleEndGfx = 1577;
	    		player.teleEndAnimation = 8941;
	    	}

	    	Teleport.teleportEvent(player, teleportType);
	    }
	    
	    public void ladder(int obX, int obY, int x, int y, int h) {
	    	player.turnPlayerTo(obX, obY);
	    	player.startAnimation(828);
	    	CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					container.stop();
				}
				@Override
				public void stop() {
					movePlayer(x,y,h);
				}
	    	}, 1);
	    }


	    /**
	     * Following
	     **/
	    public void playerWalk(int x, int y)
	    {
	    	PathFinder.getPathFinder().findRoute(player, x, y, true, 1, 1);
	    }

	    public void followPlayer()
	    {
	    	if (player.teleTimer > 3)
	    	{
	    		return;
	    	}
	    	if (PlayerHandler.players[player.followId] == null || PlayerHandler.players[player.followId].isDead)
	    	{
	    		player.followId = 0;
	    		return;
	    	}
	    	if (player.freezeTimer > 0 || player.isDead || player.skillLevel[3] <= 0)
	    		return;
	    	int otherX = PlayerHandler.players[player.followId].getX();
	    	int otherY = PlayerHandler.players[player.followId].getY();
	    	boolean withinDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 1);
	    	player.goodDistance(otherX, otherY, player.getX(), player.getY(), 1);
	    	boolean hallyDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 2);
	    	boolean bowDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 8);
	    	boolean rangeWeaponDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 4);
	    	boolean sameSpot = player.absX == otherX && player.absY == otherY;
	    	if (!player.goodDistance(otherX, otherY, player.getX(), player.getY(), 25))
	    	{
	    		player.followId = 0;
	    		return;
	    	}

	    	if ((player.usingBow || player.getUsingRange() || player.mageFollow || (player.playerIndex > 0 && player.autocastId > 0)) && bowDistance && !sameSpot)
	    	{
	    		Movement.stopMovement(player);
	    		return;
	    	}
	    	if (player.getCombat().usingHally() && hallyDistance && !sameSpot)
	    	{
	    		return;
	    	}
	    	if (player.usingRangeWeapon && rangeWeaponDistance && !sameSpot)
	    	{
	    		return;
	    	}
	    	player.faceUpdate(player.followId + 32768);
	    	player.getX();
	    	player.getY();
	    	if (otherX == player.absX && otherY == player.absY)
	    	{
	    		if (Region.getClipping(player.getX() - 1, player.getY(), player.heightLevel, -1, 0))
	    		{
	    			goTo(-1, 0);
	    		}
	    		else if (Region.getClipping(player.getX() + 1, player.getY(), player.heightLevel, 1, 0))
	    		{
	    			goTo(1, 0);
	    		}
	    		else if (Region.getClipping(player.getX(), player.getY() - 1, player.heightLevel, 0, -1))
	    		{
	    			goTo(0, -1);
	    		}
	    		else if (Region.getClipping(player.getX(), player.getY() + 1, player.heightLevel, 0, 1))
	    		{
	    			goTo(0, 1);
	    		}
	    	}
	    	else if (player.isRunning2 && !withinDistance)
	    	{
	    		if (otherY > player.getY() && otherX == player.getX())
	    		{
	    			playerWalk(otherX, otherY - 1);
	    		}
	    		else if (otherY < player.getY() && otherX == player.getX())
	    		{
	    			playerWalk(otherX, otherY + 1);
	    		}
	    		else if (otherX > player.getX() && otherY == player.getY())
	    		{
	    			playerWalk(otherX - 1, otherY);
	    		}
	    		else if (otherX < player.getX() && otherY == player.getY())
	    		{
	    			playerWalk(otherX + 1, otherY);
	    		}
	    		else if (otherX < player.getX() && otherY < player.getY())
	    		{
	    			playerWalk(otherX + 1, otherY + 1);
	    		}
	    		else if (otherX > player.getX() && otherY > player.getY())
	    		{
	    			playerWalk(otherX - 1, otherY - 1);
	    		}
	    		else if (otherX < player.getX() && otherY > player.getY())
	    		{
	    			playerWalk(otherX + 1, otherY - 1);
	    		}
	    		else if (otherX > player.getX() && otherY < player.getY())
	    		{
	    			playerWalk(otherX + 1, otherY - 1);
	    		}
	    	}
	    	else
	    	{
	    		if (otherY > player.getY() && otherX == player.getX())
	    		{
	    			playerWalk(otherX, otherY - 1);
	    		}
	    		else if (otherY < player.getY() && otherX == player.getX())
	    		{
	    			playerWalk(otherX, otherY + 1);
	    		}
	    		else if (otherX > player.getX() && otherY == player.getY())
	    		{
	    			playerWalk(otherX - 1, otherY);
	    		}
	    		else if (otherX < player.getX() && otherY == player.getY())
	    		{
	    			playerWalk(otherX + 1, otherY);
	    		}
	    		else if (otherX < player.getX() && otherY < player.getY())
	    		{
	    			playerWalk(otherX + 1, otherY + 1);
	    		}
	    		else if (otherX > player.getX() && otherY > player.getY())
	    		{
	    			playerWalk(otherX - 1, otherY - 1);
	    		}
	    		else if (otherX < player.getX() && otherY > player.getY())
	    		{
	    			playerWalk(otherX + 1, otherY - 1);
	    		}
	    		else if (otherX > player.getX() && otherY < player.getY())
	    		{
	    			playerWalk(otherX - 1, otherY + 1);
	    		}
	    	}
	    	player.faceUpdate(player.followId + 32768);
	    }

	    public void followNpc()
	    {
	    	if (NPCHandler.npcs[player.followId2] == null || NPCHandler.npcs[player.followId2].isDead)
	    	{
	    		player.followId2 = 0;
	    		return;
	    	}
	    	if (player.freezeTimer > 0)
	    	{
	    		return;
	    	}
	    	if (player.isDead)
	    	{
	    		return;
	    	}
	    	int otherX = NPCHandler.npcs[player.followId2].getX();
	    	int otherY = NPCHandler.npcs[player.followId2].getY();
	    	boolean withinDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 2);
	    	boolean hallyDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 2);
	    	boolean bowDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 8);
	    	boolean rangeWeaponDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 4);
	    	boolean sameSpot = player.absX == otherX && player.absY == otherY;
	    	if (!player.goodDistance(otherX, otherY, player.getX(), player.getY(), 25))
	    	{
	    		player.followId2 = 0;
	    		return;
	    	}
	    	if ((player.usingBow || player.mageFollow || player.npcIndex > 0) && bowDistance && !sameSpot)
	    	{
	    		return;
	    	}
	    	if (player.usingMagic)
	    	{
	    		player.usingMagic = false;
	    		player.followId2 = 0;
	    		return;
	    	}
	    	if (player.getCombat().usingHally() && hallyDistance && !sameSpot)
	    	{
	    		return;
	    	}
	    	if (player.usingRangeWeapon && rangeWeaponDistance && !sameSpot)
	    	{
	    		return;
	    	}
	    	player.faceUpdate(player.followId2);
	    	if (otherX == player.absX && otherY == player.absY)
	    	{
	    		if (Region.getClipping(player.getX() - 1, player.getY(),
	    				player.heightLevel, -1, 0))
	    		{
	    			goTo(-1, 0);
	    		}
	    		else if (Region.getClipping(player.getX() + 1, player.getY(),
	    				player.heightLevel, 1, 0))
	    		{
	    			goTo(1, 0);
	    		}
	    		else if (Region.getClipping(player.getX(), player.getY() - 1,
	    				player.heightLevel, 0, -1))
	    		{
	    			goTo(0, -1);
	    		}
	    		else if (Region.getClipping(player.getX(), player.getY() + 1,
	    				player.heightLevel, 0, 1))
	    		{
	    			goTo(0, 1);
	    		}
	    	}
	    	else if (player.isRunning2 && !withinDistance)
	    	{
	    		if (otherY > player.getY() && otherX == player.getX())
	    		{
	    			playerWalk(
	    					0,
	    					getMove(player.getY(), otherY - 1) + getMove(player.getY(), otherY - 1));
	    		}
	    		else if (otherY < player.getY() && otherX == player.getX())
	    		{
	    			playerWalk(
	    					0,
	    					getMove(player.getY(), otherY + 1) + getMove(player.getY(), otherY + 1));
	    		}
	    		else if (otherX > player.getX() && otherY == player.getY())
	    		{
	    			playerWalk(
	    					getMove(player.getX(), otherX - 1) + getMove(player.getX(), otherX - 1), 0);
	    		}
	    		else if (otherX < player.getX() && otherY == player.getY())
	    		{
	    			playerWalk(
	    					getMove(player.getX(), otherX + 1) + getMove(player.getX(), otherX + 1), 0);
	    		}
	    		else if (otherX < player.getX() && otherY < player.getY())
	    		{
	    			playerWalk(
	    					getMove(player.getX(), otherX + 1) + getMove(player.getX(), otherX + 1),
	    					getMove(player.getY(), otherY + 1) + getMove(player.getY(), otherY + 1));
	    		}
	    		else if (otherX > player.getX() && otherY > player.getY())
	    		{
	    			playerWalk(
	    					getMove(player.getX(), otherX - 1) + getMove(player.getX(), otherX - 1),
	    					getMove(player.getY(), otherY - 1) + getMove(player.getY(), otherY - 1));
	    		}
	    		else if (otherX < player.getX() && otherY > player.getY())
	    		{
	    			playerWalk(
	    					getMove(player.getX(), otherX + 1) + getMove(player.getX(), otherX + 1),
	    					getMove(player.getY(), otherY - 1) + getMove(player.getY(), otherY - 1));
	    		}
	    		else if (otherX > player.getX() && otherY < player.getY())
	    		{
	    			playerWalk(
	    					getMove(player.getX(), otherX + 1) + getMove(player.getX(), otherX + 1),
	    					getMove(player.getY(), otherY - 1) + getMove(player.getY(), otherY - 1));
	    		}
	    	}
	    	else
	    	{
	    		if (otherY > player.getY() && otherX == player.getX())
	    		{
	    			playerWalk(0, getMove(player.getY(), otherY - 1));
	    		}
	    		else if (otherY < player.getY() && otherX == player.getX())
	    		{
	    			playerWalk(0, getMove(player.getY(), otherY + 1));
	    		}
	    		else if (otherX > player.getX() && otherY == player.getY())
	    		{
	    			playerWalk(getMove(player.getX(), otherX - 1), 0);
	    		}
	    		else if (otherX < player.getX() && otherY == player.getY())
	    		{
	    			playerWalk(getMove(player.getX(), otherX + 1), 0);
	    		}
	    		else if (otherX < player.getX() && otherY < player.getY())
	    		{
	    			playerWalk(getMove(player.getX(), otherX + 1), getMove(player.getY(), otherY + 1));
	    		}
	    		else if (otherX > player.getX() && otherY > player.getY())
	    		{
	    			playerWalk(getMove(player.getX(), otherX - 1), getMove(player.getY(), otherY - 1));
	    		}
	    		else if (otherX < player.getX() && otherY > player.getY())
	    		{
	    			playerWalk(getMove(player.getX(), otherX + 1), getMove(player.getY(), otherY - 1));
	    		}
	    		else if (otherX > player.getX() && otherY < player.getY())
	    		{
	    			playerWalk(getMove(player.getX(), otherX - 1), getMove(player.getY(), otherY + 1));
	    		}
	    	}
	    	player.faceUpdate(player.followId2);
	    }

	    public void resetFollow()
	    {
	    	player.followId = 0;
	    	player.followId2 = 0;
	    	player.mageFollow = false;
	    	player.outStream.createFrame(174);
	    	player.outStream.writeWord(0);
	    	player.outStream.writeByte(0);
	    	player.outStream.writeWord(1);
	    	player.outStream.writeWord(0);
	    }

	    /**
	     * @param xCoord
	     *            the amount to walk to from the player's X axis co-ordination
	     * @param yCoord
	     *            the amount to walk to from the player's Y axis co-ordination
	     */
	     public void goTo(int xCoord, int yCoord)
	    {
	    	player.newWalkCmdSteps = 0;
	    	if (++player.newWalkCmdSteps > 50)
	    		player.newWalkCmdSteps = 0;
	    	int k = player.getX() + xCoord;
	    	k -= player.mapRegionX * 8;
	    	player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = 0;
	    	int l = player.getY() + yCoord;
	    	l -= player.mapRegionY * 8;
	    	for (int n = 0; n < player.newWalkCmdSteps; n++)
	    	{
	    		player.getNewWalkCmdX()[n] += k;
	    		player.getNewWalkCmdY()[n] += l;
	    	}
	    }

	    public int getMove(int place1, int place2)
	    {
	    	if (System.currentTimeMillis() - player.lastSpear < 4000)
	    		return 0;
	    	if ((place1 - place2) == 0)
	    	{
	    		return 0;
	    	}
	    	else if ((place1 - place2) < 0)
	    	{
	    		return 1;
	    	}
	    	else if ((place1 - place2) > 0)
	    	{
	    		return -1;
	    	}
	    	return 0;
	    }

	    public boolean fullVeracs()
	    {
	    	return player.playerEquipment[Constants.HEAD_SLOT] == 4753 && player.playerEquipment[Constants.TORSO_SLOT] == 4757 && player.playerEquipment[Constants.LEG_SLOT] == 4759 && player.playerEquipment[Constants.WEAPON_SLOT] == 4755;
	    }

	    public boolean fullGuthans()
	    {
	    	return player.playerEquipment[Constants.HEAD_SLOT] == 4724 && player.playerEquipment[Constants.TORSO_SLOT] == 4728 && player.playerEquipment[Constants.LEG_SLOT] == 4730 && player.playerEquipment[Constants.WEAPON_SLOT] == 4726;
	    }

	    /**
	     * reseting animation
	     **/
	     public void resetAnimation()
	    {
	    	player.getCombat().getPlayerAnimIndex(ItemAssistant.getItemName(player.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
	    	player.startAnimation(player.playerStandIndex);
	    	requestUpdates();
	    }

	    /**
	     * Update player appearance.
	     */
	     public void requestUpdates()
	    {
	    	player.updateRequired = true;
	    	player.setAppearanceUpdateRequired(true);
	    }

	    public void levelUp(int skill)
	    {
	    	player.dialogueProgress = 0;
	    	player.dialogues = null;
	    	int totalLevel = (getLevelForXP(player.playerXP[0]) + getLevelForXP(player.playerXP[1]) + getLevelForXP(player.playerXP[2]) + getLevelForXP(player.playerXP[3]) + getLevelForXP(player.playerXP[4]) + getLevelForXP(player.playerXP[5]) + getLevelForXP(player.playerXP[6]) + getLevelForXP(player.playerXP[7]) + getLevelForXP(player.playerXP[8]) + getLevelForXP(player.playerXP[9]) + getLevelForXP(player.playerXP[10]) + getLevelForXP(player.playerXP[11]) + getLevelForXP(player.playerXP[12]) + getLevelForXP(player.playerXP[13]) + getLevelForXP(player.playerXP[14]) + getLevelForXP(player.playerXP[15]) + getLevelForXP(player.playerXP[16]) + getLevelForXP(player.playerXP[17]) + getLevelForXP(player.playerXP[18]) + getLevelForXP(player.playerXP[19]) + getLevelForXP(player.playerXP[20]));
	    	player.totalLevel = totalLevel();
	    	player.xpTotal = xpTotal();
	    	sendFrame126("Total Lvl: " + totalLevel, 3984);
	    	switch (skill)
	    	{
	    	case 0:
	    		sendFrame126("Congratulations! You've just advanced a Attack level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + "!", 4269);
	    		player.sendMessage("Congratulations! You've just advanced a attack level.");
	    		sendFrame164(6247);
	    		break;

	    	case 1:
	    		sendFrame126("Congratulations! You've just advanced a Defence level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations! You've just advanced a Defence level.");
	    		sendFrame164(6253);
	    		break;

	    	case 2:
	    		sendFrame126("Congratulations! You've just advanced a Strength level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations! You've just advanced a Strength level.");
	    		sendFrame164(6206);
	    		break;

	    	case 3:
	    		sendFrame126("" + player.maximumHitPoints() + "", 4017);
	    		sendFrame126("Congratulations! You've just advanced a Hitpoints level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations! You've just advanced a Hitpoints level.");
	    		sendFrame164(6216);
	    		break;

	    	case 4:
	    		sendFrame126("Congratulations! You've just advanced a Ranged level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations! You've just advanced a Ranging level.");
	    		sendFrame164(4443);
	    		break;

	    	case 5:
	    		sendFrame126("Congratulations! You've just advanced a Prayer level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations! You've just advanced a Prayer level.");
	    		sendFrame164(6242);
	    		break;

	    	case 6:
	    		sendFrame126("Congratulations! You've just advanced a Magic level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations! You've just advanced a Magic level.");
	    		sendFrame164(6211);
	    		break;

	    	case 7:
	    		sendFrame126("Congratulations! You've just advanced a Cooking level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations, you just advanced a cooking level.");
	    		sendFrame164(6226);
	    		announce99(Constants.COOKING, "Cooking");
	    		break;

	    	case 8:
	    		sendFrame126("Congratulations! You've just advanced a Woodcutting level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations, you just advanced a woodcutting level.");
	    		sendFrame164(4272);
	    		announce99(Constants.WOODCUTTING, "Woodcutting");
	    		break;

	    	case 9:
	    		sendFrame126("Congratulations! You've just advanced a Fletching level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations, you just advanced a fletching level.");
	    		sendFrame164(6231);
	    		announce99(Constants.FLETCHING, "Fletching");
	    		break;

	    	case 10:
	    		sendFrame126("Congratulations! You've just advanced a Fishing level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations, you just advanced a fishing level.");
	    		sendFrame164(6258);
	    		announce99(Constants.FISHING, "Fishing");
	    		break;

	    	case 11:
	    		sendFrame126("Congratulations! You've just advanced a Firemaking level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations, you just advanced a fire making level.");
	    		sendFrame164(4282);
	    		announce99(Constants.FIREMAKING, "Firemaking");
	    		break;

	    	case 12:
	    		sendFrame126("Congratulations! You've just advanced a Crafting level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations, you just advanced a crafting level.");
	    		sendFrame164(6263);
	    		announce99(Constants.CRAFTING, "Crafting");
	    		break;

	    	case 13:
	    		sendFrame126("Congratulations! You've just advanced a Smithing level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations, you just advanced a smithing level.");
	    		sendFrame164(6221);
	    		announce99(Constants.SMITHING, "Smithing");
	    		break;

	    	case 14:
	    		sendFrame126("Congratulations! You've just advanced a Mining level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations, you just advanced a mining level.");
	    		sendFrame164(4416);
	    		announce99(Constants.MINING, "Mining");
	    		break;

	    	case 15:
	    		sendFrame126("Congratulations! You've just advanced a Herblore level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations, you just advanced a herblore level.");
	    		sendFrame164(6237);
	    		announce99(Constants.HERBLORE, "Herblore");
	    		break;

	    	case 16:
	    		sendFrame126("Congratulations! You've just advanced a Agility level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations! You've just advanced a Agility level.");
	    		sendFrame164(4277);
	    		announce99(Constants.AGILITY, "Agility");
	    		break;

	    	case 17:
	    		sendFrame126("Congratulations! You've just advanced a Thieving level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations, you just advanced a thieving level.");
	    		sendFrame164(4261);
	    		announce99(Constants.THIEVING, "Thieving");
	    		break;

	    	case 18:
	    		sendFrame126("Congratulations! You've just advanced a Slayer level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations, you just advanced a slayer level.");
	    		sendFrame164(12122);
	    		announce99(Constants.SLAYER, "Slayer");
	    		break;

	    	case 19:
	    		sendFrame126("Congratulations! You've just advanced a Farming level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations, you just advanced a farming level to " + getLevelForXP(player.playerXP[skill]) + ".");
	    		announce99(Constants.FARMING, "Farming");
	    		break;

	    	case 20:
	    		sendFrame126("Congratulations! You've just advanced a Runecrafting level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations, you just advanced a runecrafting level.");
	    		sendFrame164(4267);
	    		announce99(Constants.RUNECRAFTING, "Runecrafting");
	    		break;

	    	case Constants.HUNTER:
	    		sendFrame126("Congratulations! You've just advanced a Hunter level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations, you just advanced a Hunter level.");
	    		sendFrame164(8267);
	    		announce99(Constants.HUNTER, "Hunter");
	    		break;
	    	case Constants.CONSTRUCTION:
	    		sendFrame126("Congratulations! You've just advanced a Construction level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations, you just advanced a Construction level.");
	    		sendFrame164(7267);
	    		announce99(Constants.CONSTRUCTION, "Construction");
	    		break;
	    	case Constants.SUMMONING:
	    		sendFrame126("Congratulations! You've just advanced a Summoning level!", 4268);
	    		sendFrame126("You have now reached level " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
	    		player.sendMessage("Congratulations, you just advanced a Summoning level.");
	    		sendFrame164(9267);
	    		announce99(Constants.SUMMONING, "Summoning");
	    		break;

	    	}

	    	player.dialogueAction = 0;
	    	player.nextChat = 0;
	    	sendFrame126("Combat Level: " + player.combatLevel, 19000);
	    }

	    public void walkTo2(int i, int j)
	    {
	    	if (player.freezeDelay > 0)
	    		return;
	    	player.newWalkCmdSteps = 0;
	    	if (++player.newWalkCmdSteps > 50)
	    		player.newWalkCmdSteps = 0;
	    	int k = player.getX() + i;
	    	k -= player.mapRegionX * 8;
	    	player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = 0;
	    	int l = player.getY() + j;
	    	l -= player.mapRegionY * 8;

	    	for (int n = 0; n < player.newWalkCmdSteps; n++)
	    	{
	    		player.getNewWalkCmdX()[n] += k;
	    		player.getNewWalkCmdY()[n] += l;
	    	}
	    }

	    public void refreshSkill(int i)
	    {

	    	switch (i)
	    	{

	    	case 0:
	    		sendFrame126("" + player.skillLevel[0] + "", 4004);
	    		sendFrame126("" + getLevelForXP(player.playerXP[0]) + "", 4005);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[0]) + 1) + "", 4045);
	    		break;
	    	case 1:
	    		sendFrame126("" + player.skillLevel[1] + "", 4008);
	    		sendFrame126("" + getLevelForXP(player.playerXP[1]) + "", 4009);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[1]) + 1) + "", 4057);
	    		break;
	    	case 2:
	    		sendFrame126("" + player.skillLevel[2] + "", 4006);
	    		sendFrame126("" + getLevelForXP(player.playerXP[2]) + "", 4007);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[2]) + 1) + "", 4051);
	    		break;
	    	case 3:
	    		sendFrame126("" + player.skillLevel[3], 19001); // Hitpoints orb.
	    		sendFrame126("" + player.skillLevel[3], 4016); // Current hitpoints in skilltab.
	    		sendFrame126("" + player.maximumHitPoints() + "", 4017); // Maximum hitpoints in skilltab.
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[3]) + 1) + "", 4081);
	    		break;
	    	case 4:
	    		sendFrame126("" + player.skillLevel[4] + "", 4010);
	    		sendFrame126("" + getLevelForXP(player.playerXP[4]) + "", 4011);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[4]) + 1) + "", 4063);
	    		break;
	    	case 5:
	    		sendFrame126("" + player.skillLevel[5] + "", 4012);
	    		sendFrame126("" + getLevelForXP(player.playerXP[5]) + "", 4013);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[5]) + 1) + "", 4069);
	    		sendFrame126("" + player.skillLevel[5] + "/" + getLevelForXP(player.playerXP[5]) + "", 687); // Prayer
	    		break;
	    	case 6:
	    		sendFrame126("" + player.skillLevel[6] + "", 4014);
	    		sendFrame126("" + getLevelForXP(player.playerXP[6]) + "", 4015);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[6]) + 1) + "", 4075);
	    		break;
	    	case 7:
	    		sendFrame126("" + player.skillLevel[7] + "", 4034);
	    		sendFrame126("" + getLevelForXP(player.playerXP[7]) + "", 4035);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[7]) + 1) + "", 4135);
	    		break;
	    	case 8:
	    		sendFrame126("" + player.skillLevel[8] + "", 4038);
	    		sendFrame126("" + getLevelForXP(player.playerXP[8]) + "", 4039);
	    		sendFrame126("" + player.playerXP[8] + "", 4146);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[8]) + 1) + "", 4147);
	    		break;
	    	case 9:
	    		sendFrame126("" + player.skillLevel[9] + "", 4026);
	    		sendFrame126("" + getLevelForXP(player.playerXP[9]) + "", 4027);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[9]) + 1) + "", 4111);
	    		break;
	    	case 10:
	    		sendFrame126("" + player.skillLevel[10] + "", 4032);
	    		sendFrame126("" + getLevelForXP(player.playerXP[10]) + "", 4033);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[10]) + 1) + "", 4129);
	    		break;
	    	case 11:
	    		sendFrame126("" + player.skillLevel[11] + "", 4036);
	    		sendFrame126("" + getLevelForXP(player.playerXP[11]) + "", 4037);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[11]) + 1) + "", 4141);
	    		break;
	    	case 12:
	    		sendFrame126("" + player.skillLevel[12] + "", 4024);
	    		sendFrame126("" + getLevelForXP(player.playerXP[12]) + "", 4025);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[12]) + 1) + "", 4105);
	    		break;
	    	case 13:
	    		sendFrame126("" + player.skillLevel[13] + "", 4030);
	    		sendFrame126("" + getLevelForXP(player.playerXP[13]) + "", 4031);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[13]) + 1) + "", 4123);
	    		break;
	    	case 14:
	    		sendFrame126("" + player.skillLevel[14] + "", 4028);
	    		sendFrame126("" + getLevelForXP(player.playerXP[14]) + "", 4029);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[14]) + 1) + "", 4117);
	    		break;
	    	case 15:
	    		sendFrame126("" + player.skillLevel[15] + "", 4020);
	    		sendFrame126("" + getLevelForXP(player.playerXP[15]) + "", 4021);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[15]) + 1) + "", 4093);
	    		break;
	    	case 16:
	    		sendFrame126("" + player.skillLevel[16] + "", 4018);
	    		sendFrame126("" + getLevelForXP(player.playerXP[16]) + "", 4019);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[16]) + 1) + "", 4087);
	    		break;
	    	case 17:
	    		sendFrame126("" + player.skillLevel[17] + "", 4022);
	    		sendFrame126("" + getLevelForXP(player.playerXP[17]) + "", 4023);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[17]) + 1) + "", 4099);
	    		break;
	    	case 18:
	    		sendFrame126("" + player.skillLevel[18] + "", 12166);
	    		sendFrame126("" + getLevelForXP(player.playerXP[18]) + "", 12167);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[18]) + 1) + "", 12172);
	    		break;
	    	case 19:
	    		sendFrame126("" + player.skillLevel[19] + "", 13926);
	    		sendFrame126("" + getLevelForXP(player.playerXP[19]) + "", 13927);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[19]) + 1) + "", 13922);
	    		break;
	    	case 20:
	    		sendFrame126("" + player.skillLevel[20] + "", 4152);
	    		sendFrame126("" + getLevelForXP(player.playerXP[20]) + "", 4153);
	    		sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[20]) + 1) + "", 4158);
	    		break;

	    	case 21:
	    	case 22:
	    	case 23:
	    	case 24:
	    	case 25:
	    		refreshNewSkills();
	    		break;
	    	}
	    }


	    private final int expArray[] = {
	    		0,83,174,276,388,512,650,801,969,1154,1358,1584,1833,2107,2411,2746,3115,3523,
	    		3973,4470,5018,5624,6291,7028,7842,8740,9730,10824,12031,13363,14833,16456,18247,
	    		20224,22406,24815,27473,30408,33648,37224,41171,45529,50339,55649,61512,67983,75127,
	    		83014,91721,101333,111945,123660,136594,150872,166636,184040,203254,224466,247886,
	    		273742,302288,333804,368599,407015,449428,496254,547953,605032,668051,737627,814445,
	    		899257,992895,1096278,1210421,1336443,1475581,1629200,1798808,1986068,2192818,2421087,
	    		2673114,2951373,3258594,3597792,3972294,4385776,4842295,5346332,5902831,6517253,7195629,
	    		7944614,8771558,9684577,10692629,11805606,13034431,	
	    };

	    public int getXPForLevel(int level) {
	    	return expArray[--level > 98 ? 98 : level];
	    }

	    public int getLevelForXP(int exp){
	    	for(int j = 98; j != -1; j--)
	    		if(expArray[j] <= exp)
	    			return j+1;
	    	return 0;
	    }


	    /*public int getXPForLevel(int level)
        {
                int points = 0;
                int output = 0;
                for (int lvl = 1; lvl <= level; lvl++)
                {
                        points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
                        if (lvl >= level)
                                return output;
                        output = (int) Math.floor(points / 4);
                }
                return 0;
        }

        public int getLevelForXP(int exp)
        {
                int points = 0;
                int output = 0;
                if (exp > 13034430)
                        return 99;
                for (int lvl = 1; lvl <= 99; lvl++)
                {
                        points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
                        output = (int) Math.floor(points / 4);
                        if (output >= exp)
                        {
                                return lvl;
                        }
                }
                return 0;
        }*/

	    public boolean addSkillXP(int amount, int skill) {
	    	if (player.expLock) {
	    		return false;
	    	}
	    	if (amount + player.playerXP[skill] < 0 || player.playerXP[skill] > 2000000000) {
	    		if (player.playerXP[skill] > 2000000000) {
	    			player.playerXP[skill] = 2000000000;
	    		}
	    		return false;
	    	}
	    	int oldLevel = getLevelForXP(player.playerXP[skill]);
	    	player.playerXP[skill] += amount;
	    	if (oldLevel < getLevelForXP(player.playerXP[skill])) {
	    		if (player.skillLevel[skill] < player
	    				.getLevelForXP(player.playerXP[skill]) && skill != 3 && skill != 5)
	    			player.skillLevel[skill] = player
	    			.getLevelForXP(player.playerXP[skill]);
	    		levelUp(skill);
	    		player.gfx100(199);
	    		requestUpdates();
	    	}
	    	setSkillLevel(skill, player.skillLevel[skill], player.playerXP[skill]);
	    	refreshSkill(skill);
	    	handleExpAchievements(amount, skill);
	    	return true;
	    }


	    public void handleExpAchievements(int amount, int skill) {
	    	AchievementHandler.add(player, 10, "easy", amount);
	    	if (skill == Constants.COOKING)
	    		AchievementHandler.add(player, 11, "easy", amount);
	    	if (skill == Constants.FLETCHING)
	    		AchievementHandler.add(player, 12, "easy", amount);

	    	if (player.skillLevel[skill] == 99 && !player.announced99[skill]) {
	    		player.announced99[skill] = true;
	    		PlayerSave.saveGame(player);
	    		globalMessage("<col=65535><shad=0>"+player.playerName+"</shad></col> has achieved level 99 "+Constants.SKILL_NAMES[skill]);
	    	}
	    	if (player.playerXP[skill] >= 100000000 && !player.announced100m[skill]) {
	    		player.announced100m[skill] = true;
	    		PlayerSave.saveGame(player);
	    		globalMessage("<col=65535><shad=0>"+player.playerName+"</shad></col> has reached 100m experience in the "+Constants.SKILL_NAMES[skill]+" skill");
	    	}
	    	if (player.playerXP[skill] >= 200000000 && !player.announced200m[skill]) {
	    		player.announced200m[skill] = true;
	    		PlayerSave.saveGame(player);
	    		globalMessage("<col=65535><shad=0>"+player.playerName+"</shad></col> has reached 200m experience in the "+Constants.SKILL_NAMES[skill]+" skill");
	    	}
	    }

	    public void globalMessage(String s) {
	    	for(int i = 0; i < PlayerHandler.players.length; i++) {
	    		if (PlayerHandler.players[i] != null) {
	    			PlayerHandler.players[i].sendMessage(s);
	    		}
	    	}
	    }



	    /**
	     * Show an arrow icon on the selected player.
	     *
	     * @Param i - Either 0 or 1; 1 is arrow, 0 is none.
	     * @Param j - The player/Npc that the arrow will be displayed above.
	     * @Param k - Keep this set as 0
	     * @Param l - Keep this set as 0
	     */

	    public void drawHeadicon(int i, int j, int k, int l)
	    {
	    	synchronized(player)
	    	{
	    		player.outStream.createFrame(254);
	    		player.outStream.writeByte(i);
	    		if (i == 1 || i == 10)
	    		{
	    			player.outStream.writeWord(j);
	    			player.outStream.writeWord(k);
	    			player.outStream.writeByte(l);
	    		}
	    		else
	    		{
	    			player.outStream.writeWord(k);
	    			player.outStream.writeWord(l);
	    			player.outStream.writeByte(j);
	    		}
	    	}
	    }

	    public int antiFire() {
	    	int toReturn = 0;
	    	if (player.antiFirePot) {
	    		toReturn++;
	    	}
	    	if (player.playerEquipment[Constants.SHIELD_SLOT] == 1540 || player.playerEquipment[Constants.SHIELD_SLOT] == 11283) {
	    		toReturn++;
	    	}
	    	return toReturn;
	    }

	    public void flashSelectedSidebar(int i1)
	    {
	    	player.outStream.createFrame(24);
	    	player.outStream.writeByteA(i1);
	    }

	    /**
	     * Select the tab.
	     *
	     * @param tab
	     *            The tab identity.
	     */
	     public void changeToSidebar(int tab)
	     {
	    	 player.outStream.createFrame(106);
	    	 player.outStream.writeByteC(tab);
	     }

	     public boolean confirmMessage;

	     public void getSpeared(int otherX, int otherY)
	     {
	    	 int x = player.absX - otherX;
	    	 int y = player.absY - otherY;
	    	 if (x > 0 && Region.getClipping(player.getX() + 1, player.getY(),
	    			 player.heightLevel, 1, 0))
	    	 {
	    		 movePlayer(player.absX + 1, player.absY, player.heightLevel);
	    	 }
	    	 else if (x < 0 && Region.getClipping(player.getX() - 1, player.getY(),
	    			 player.heightLevel, -1, 0))
	    	 {
	    		 movePlayer(player.absX - 1, player.absY, player.heightLevel);
	    	 }
	    	 else if (y > 0 && Region.getClipping(player.getX(), player.getY() + 1,
	    			 player.heightLevel, 0, 1))
	    	 {
	    		 movePlayer(player.absX, player.absY + 1, player.heightLevel);
	    	 }
	    	 else if (y < 0 && Region.getClipping(player.getX(), player.getY() - 1,
	    			 player.heightLevel, 0, -1))
	    	 {
	    		 movePlayer(player.absX, player.absY - 1, player.heightLevel);
	    	 }
	    	 else
	    	 {
	    		 confirmMessage = true;
	    	 }
	    	 player.lastSpear = System.currentTimeMillis();
	     }

	     public int findKiller()
	     {
	    	 int killer = player.playerId;
	    	 int damage = 0;
	    	 for (int j = 0; j < Configuration.MAX_PLAYERS; j++)
	    	 {
	    		 if (PlayerHandler.players[j] == null)
	    			 continue;
	    		 if (j == player.playerId)
	    			 continue;
	    		 if (player.goodDistance(player.absX, player.absY,
	    				 PlayerHandler.players[j].absX,
	    				 PlayerHandler.players[j].absY, 40) || player.goodDistance(player.absX, player.absY + 9400,
	    						 PlayerHandler.players[j].absX,
	    						 PlayerHandler.players[j].absY, 40) || player.goodDistance(player.absX, player.absY,
	    								 PlayerHandler.players[j].absX,
	    								 PlayerHandler.players[j].absY + 9400, 40))
	    			 if (player.damageTaken[j] > damage)
	    			 {
	    				 damage = player.damageTaken[j];
	    				 killer = j;
	    			 }
	    	 }
	    	 return killer;
	     }

	     public void handleWeaponStyle()
	     {
	    	 if (player.fightMode == 0)
	    	 {
	    		 sendFrame36(43, player.fightMode);
	    	 }
	    	 else if (player.fightMode == 1)
	    	 {
	    		 sendFrame36(43, 3);
	    	 }
	    	 else if (player.fightMode == 2)
	    	 {
	    		 sendFrame36(43, 1);
	    	 }
	    	 else if (player.fightMode == 3)
	    	 {
	    		 sendFrame36(43, 2);
	    	 }
	     }

	     public void destroyInterface(int itemId)
	     {
	    	 itemId = player.droppedItem;
	    	 String itemName = ItemAssistant.getItemName(player.droppedItem);
	    	 String[][] info = {
	    			 {
	    				 "Are you sure you want to drop this item?", "14174"
	    			 },
	    			 {
	    				 "Yes.", "14175"
	    			 },
	    			 {
	    				 "No.", "14176"
	    			 },
	    			 {
	    				 "", "14177"
	    			 },
	    			 {
	    				 "This item will dissapear", "14182"
	    			 },
	    			 {
	    				 "permanently if you accept.", "14183"
	    			 },
	    			 {
	    				 itemName, "14184"
	    			 }
	    	 };
	    	 sendFrame34(itemId, 0, 14171, 1);
	    	 for (int i = 0; i < info.length; i++)
	    		 sendFrame126(info[i][0], Integer.parseInt(info[i][1]));
	    	 sendFrame164(14170);
	     }

	     public void sendSound(int id, int type, int delay, int volume)
	     {
	    	 if (player.getOutStream() != null && player != null && id != -1)
	    	 {
	    		 player.getOutStream().createFrame(174);
	    		 player.getOutStream().writeWord(id);
	    		 player.getOutStream().writeByte(type);
	    		 player.getOutStream().writeWord(delay);
	    		 player.getOutStream().writeWord(volume);
	    		 player.flushOutStream();
	    	 }
	     }

	     public void sendSong(int id)
	     {
	    	 if (player.getOutStream() != null && player != null && id != -1)
	    	 {
	    		 player.getOutStream().createFrame(74);
	    		 player.getOutStream().writeWordBigEndian(id);
	    	 }
	     }

	     public void sendQuickSong(int id, int songDelay)
	     {
	    	 if (player.getOutStream() != null && player != null)
	    	 {
	    		 player.getOutStream().createFrame(121);
	    		 player.getOutStream().writeWordBigEndian(id);
	    		 player.getOutStream().writeWordBigEndian(songDelay);
	    		 player.flushOutStream();
	    	 }
	     }

	     public void destroyItem(int itemId)
	     {
	    	 itemId = player.droppedItem;
	    	 String itemName = ItemAssistant.getItemName(itemId);
	    	 ItemAssistant.deleteItem(player, itemId, ItemAssistant.getItemSlot(player, itemId), player.playerItemsN[ItemAssistant.getItemSlot(player, itemId)]);
	    	 player.sendMessage("Your " + itemName + " vanishes as you drop it on the ground.");
	    	 removeAllWindows();
	     }



	     /**
	      * If the player is using melee and is standing diagonal from the opponent,
	      * then move towards opponent.
	      */
	     public void movePlayerDiagonal(Player other)
	     {
	    	 boolean hasMoved = false;
	    	 int otherX = other.getX();
	    	 int otherY = other.getY();
	    	 if (player.goodDistance(otherX, otherY, player.getX(), player.getY(), 1))
	    	 {
	    		 if (player.getX() != other.getX() && player.getY() != other.getY())
	    		 {
	    			 if (player.getX() > other.getX() && !hasMoved)
	    			 {
	    				 if (Region.getClipping(player.getX() - 1, player.getY(),
	    						 player.heightLevel, -1, 0))
	    				 {
	    					 hasMoved = true;
	    					 goTo(-1, 0);
	    				 }
	    			 }
	    			 else if (player.getX() < other.getX() && !hasMoved)
	    			 {
	    				 if (Region.getClipping(player.getX() + 1, player.getY(),
	    						 player.heightLevel, 1, 0))
	    				 {
	    					 hasMoved = true;
	    					 goTo(1, 0);
	    				 }
	    			 }

	    			 if (player.getY() > other.getY() && !hasMoved)
	    			 {
	    				 if (Region.getClipping(player.getX(), player.getY() - 1,
	    						 player.heightLevel, 0, -1))
	    				 {
	    					 hasMoved = true;
	    					 goTo(0, -1);
	    				 }
	    			 }
	    			 else if (player.getY() < other.getY() && !hasMoved)
	    			 {
	    				 if (Region.getClipping(player.getX(), player.getY() + 1,
	    						 player.heightLevel, 0, 1))
	    				 {
	    					 hasMoved = true;
	    					 goTo(0, 1);
	    				 }
	    			 }
	    		 }
	    	 }
	    	 hasMoved = false;
	     }

	public void enterCaves() {
		player.getPA().movePlayer(2413,5117, Misc.random(400000) * 4);
		player.waveId = 0;
		player.tzhaarToKill = -1;
		player.tzhaarKilled = -1;
		player.sendMessage("The first wave will start in 10 seconds.");
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			public void execute(CycleEventContainer e) {
				Server.fightCaves.spawnNextWave((Client)PlayerHandler.players[player.playerId]);
				e.stop();
			}

			@Override
			public void stop() {
			}
		}, 17);
	}

	public void resetTzhaar() {
		player.waveId = -1;
		player.tzhaarToKill = -1;
		player.tzhaarKilled = -1;
		movePlayer(2438, 5168, 0);
	}


}