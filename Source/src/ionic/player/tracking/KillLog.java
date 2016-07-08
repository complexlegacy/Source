package ionic.player.tracking;

import ionic.player.Client;
import ionic.player.PlayerHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A tracking system, which saves critical information to ban the player for player kill farming.
 * @author MGT Madness, created on 6-12-2013.
 */
public class KillLog 
{
    
    /**
     * If the conditions are satisfied, then proceed with creating the log.
     * @param victim
     * 			 The player who died.
     * @param theKiller
     * 			The player who killed the victim.
     */
    public static void appendLog(Client victim, int theKiller)
    {
    	Client killer = (Client) PlayerHandler.players[theKiller];
    	if (victim.inWilderness() && !killer.isAdministratorRank())
    	{
    		log(victim, theKiller);
    	}
    }
    
    /**
     * Create the log.
     * @param victim
     * 			 The player who died.
     * @param theKiller
     * 			The player who killed the victim.
     */
    public static void log(Client victim, int theKiller)
    {
    		Client killer = (Client) PlayerHandler.players[theKiller];
    		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss");
		    Calendar cal = Calendar.getInstance();
            String filePath = "./data/logs/kills/" + killer.playerName + ".txt";
            BufferedWriter bw = null;
            try
            {
                    bw = new BufferedWriter(new FileWriter(filePath, true));
                    bw.write("[" + dateFormat.format(cal.getTime()) + "] " + killer.playerName + " killed " + victim.playerName + ".");
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

}
