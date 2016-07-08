package ionic.npc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class parses the dumped information from
 * the npc_sizes.txt file.
 *
 * @author relex lawl
 */
public final class NPCSize {
	
	/**
	 * The directory of the npc size file.
	 */
	private static final String FILE_DIRECTORY = "./data/npc/npc_sizes.txt";

	/**
	 * This 2d-array contains the size information respective to the
	 * npc id.
	 */
	private static final int[] NPC_SIZES = new int[14415];
	
	/**
	 * Initiates the parsing of the npc_sizes.txt file.
	 * @throws IOException
	 */
	public static void init() throws IOException {
		final BufferedReader reader = new BufferedReader(new FileReader(FILE_DIRECTORY));
		
		String line = null;
		while ((line = reader.readLine()) != null) 
		{
			final String[] split = line.split(" : ");
			final int id = Integer.valueOf(split[0]);
			final int size = Integer.valueOf(split[1]);
			NPC_SIZES[id] = size;
		}
		reader.close();
	}
	
	/**
	 * Gets the 2d-array holding the npc size
	 * data.
	 * @return	The {@link #NPC_SIZES} value.
	 */
	public static int[] getSize() {
		return NPC_SIZES;
	}
}
