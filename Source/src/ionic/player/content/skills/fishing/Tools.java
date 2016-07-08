package ionic.player.content.skills.fishing;

import java.util.HashMap;
import java.util.Map;

public enum Tools {
	
	BIG_NET(305, 620, 6, new short[]
	{ 353, 341, 363 }),
	CRAYFISH_CAGE(13431, 619, 5, new short[]
	{ 13435 }),
	FISHING_ROD(307, 622, 1, new short[]
	{ 327, 345, 349, 3379, 5001, 2148 }),
	FLYFISHING_ROD(309, 622, 1, new short[]
	{ 335, 331 }),
	HARPOON(311, 618, 5, new short[]
	{ 359, 371 }),
	KARAMBWAN_POT(3157, -1, -1, new short[]
	{ 3142 }), //TODO Find Animation
	LOBSTER_POT(301, 619, 5, new short[]
	{ 377 }),
	SMALL_NET(303, 621, 6, new short[]
	{ 317, 3150, 321, 5004, 7994 });



	public static Tools forId(int id) {
		return values.get(id);
	}

	private final int toolId;
	private final int animation;
	private final int animCycles;
	private static final Map<Integer, Tools> values = new HashMap<>();

	static
	{
		for (Tools tool : Tools.values())
		{
			values.put(tool.getToolId(), tool);
		}
	}

	private Tools(int toolId, int animation, int animCycles, short[] outcomes)
	{
		this.toolId = toolId;
		this.animation = animation;
		this.animCycles = animCycles;
	}

	public int getAnimationId() {
		return animation;
	}

	public int getAnimCycles() {
		return animCycles;
	}

	public int getToolId() {
		return toolId;
	}
}
