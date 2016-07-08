package ionic.player.tracking;

import ionic.player.PlayerHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import core.Configuration;

/**
 * Track the main tick.
 * @author MGT Madness, created on 12-12-2013.
 */
public class MainTickLog 
{
	
	 private static int loop;
     private static long loopDuration;
	
	/**
     * Stage 1 of the loop debug.
     */
    public static void loopDebugPart1()
    {
            loopDuration = System.currentTimeMillis();
            loop++;
    }
    
    /**
     * Stage 2 of the loop debug.
     */
	public static void loopDebugPart2()
    {
            if (Configuration.stabilityTest)
            {
                    System.out.println("Main loop took: " + PlayerHandler.playerCount + " players, " + (loopDuration = System.currentTimeMillis() - loopDuration) + " ms.");
                    /* If the loop took more than 150ms, then a slight lag spike will be felt. */
                    if (loopDuration >= 150)
                    {
                            System.out.println("Lag spike has occured.");
                            lagLog(loopDuration);
                    }
            }
            else if (loop == 500 && !Configuration.DEBUG)
            {
                    loop = 0;
                    System.out.println("Main loop took: " + PlayerHandler.playerCount + " players, " + (loopDuration = System.currentTimeMillis() - loopDuration) + " ms.");
                    
                    /* If the loop took more than 150ms, then a slight lag spike will be felt. */
                    if (loopDuration >= 150)
                    {
                            System.out.println("Lag spike has occured.");
                            lagLog(loopDuration);
                    }
            }
    }
    
    /**
     * Create the lag log.
     */
    private static void lagLog(long loopDuration)
    {
    		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss");
    		Calendar cal = Calendar.getInstance();
            String filePath = "./data/logs/lagLog.txt";
            BufferedWriter bw = null;
            try
            {
                    bw = new BufferedWriter(new FileWriter(filePath, true));
                    bw.write("[" + dateFormat.format(cal.getTime()) + "] " + "Main tick took: " + loopDuration + "ms with " + PlayerHandler.playerCount + " players.");
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
