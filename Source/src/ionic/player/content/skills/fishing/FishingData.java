package ionic.player.content.skills.fishing;

import java.util.HashMap;
import java.util.Map;

public enum FishingData {
	
	SHRIMP(317, 303, 1, 10, -1, false),
	CRAYFISH(13435, 13431, 1, 10, -1, false),
	KARAMBWANJI(3150, 303, 5, 5, -1, false),
	SARDINE(327, 307, 5, 20, 313, false),
	HERRING(345, 307, 10, 30, 313, false),
	ANCHOVIES(321, 303, 15, 40, -1, false),
	MACKEREL(353, 305, 16, 20, -1, false),
	TROUT(335, 309, 20, 50, 314, false),
	COD(341, 305, 23, 45, -1, false),
	PIKE(349, 307, 25, 60, 313, false),
	SLIMY_EEL(3379, 307, 28, 65, 313, false),
	SALMON(331, 309, 30, 70, 314, false),
	TUNA(359, 311, 35, 80, -1, true),
	CAVE_EEL(5001, 307, 38, 80, 313, false),
	LOBSTER(377, 301, 40, 90, -1, false),
	BASS(363, 305, 46, 100, -1, false),
	SWORDFISH(371, 311, 50, 100, -1, true),
	MONK_FISH(7944, 303, 62, 120, -1, false),
	KARAMBWAN(3142, 3157, 65, 100, -1, false),
	SHARK(383, 311, 76, 110, -1, true),
	SEA_TURTLE(395, -1, 79, 38, -1, false),
	MANTA_RAY(390, -1, 81, 46, -1, false),
	CAVE_FISH(15264, 307, 85, 300, 313, false),
	ROCKTAIL(15270, 307, 90, 380, 15263, false);

	public static FishingData forId(int rawFishId) {
		return values.get(rawFishId);
	}

	private final int rawFishId;

	private final int toolId;

	private final int req;

	private final int baitRequired;

	private final double experienceGain;
	
	public boolean barbarian;

	private static final Map<Integer, FishingData> values = new HashMap<>();

	static
	{
		for (FishingData fishes : FishingData.values())
		{
			values.put(fishes.getRawFishId(), fishes);
		}
	}

	private FishingData(int rawFishId, int toolId, int req, double experienceGain, int baitRequired, boolean barbarian)
	{
		this.rawFishId = rawFishId;
		this.toolId = toolId;
		this.req = req;
		this.experienceGain = experienceGain;
		this.baitRequired = baitRequired;
		this.barbarian = barbarian;
	}

	public int getBaitRequired() {
		return baitRequired;
	}

	public double getExperience() {
		return experienceGain;
	}

	public int getRawFishId() {
		return rawFishId;
	}

	public int getRequiredLevel() {
		return req;
	}

	public int getToolId() {
		return toolId;
	}

}
