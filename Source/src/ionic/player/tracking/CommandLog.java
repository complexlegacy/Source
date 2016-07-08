package ionic.player.tracking;

import ionic.player.Client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Command log system.
 * @author MGT Madness, created on 12-12-2013.
 */
public class CommandLog
{
	
	/**
	 * Write the Command log in a certain file.
	 * @param command
	 * 			The command used.
	 */
	public static void writeCommandLog(Client player, String command)
    {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss");
			Calendar cal = Calendar.getInstance();
            String filePath = "./data/logs/Commandlog.txt";
            BufferedWriter bw = null;
            try
            {
                    bw = new BufferedWriter(new FileWriter(filePath, true));
					bw.write("[" + dateFormat.format(cal.getTime()) + "] " + "[" + player.connectedFrom + "] " + "[" + player.playerName + "] " + "typed in ::" + command);
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
