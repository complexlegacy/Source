package ionic.player.dialogue;

public enum Emotion {
	
	HAPPY(9850),
	TALKING_SAD_DOWN(9760),
	SAD(9764),
	SAD2(9768),
	TALKING_SAD(9772),
	WORRIED(9776),
	SCARED(9780),
	BIT_ANGRY(9784),
	ANGRY(9788),
	VERY_ANGRY(9792),
	ANGRY2(9796),
	CRAZY(9800),
	LISTENING(9804),
	TALKING(9808),
	TALK_WONDER(9812),
	WHAT_THE(9820),
	WHAT_THE2(9816),
	NO(9824),
	THINK(9828),
	TALKING2(9832),
	UNSURE(9836),
	LAUGHING(9840),
	TALK_SWINGING_HEAD(9844),
	NORMAL(9847),
	LAUGHING_GOOFY(9851),
	TALKING_STILL(9855),
	THINKING_STILL(9859),
	GANGSTA(9863),
	CRYING(9675),
	CALM_TALK(9670),
	SHY(9770),
	SAD_WORRIED(9775),
	GRUMPY(9785),
	YELLING_ANGRY(9790),
	CRAZY_EYE(9795),
	YEAH(9815),
	DISGUSTED(9820),
	NO_WAY(9823),
	CONFUSED(9827),
	DRUNK(9835),
	DEMENTED(9870),
	PRIDEFUL(9865);
	private Emotion(int anim) {
		this.a = anim;
	}
	private int a;
	public int get() {
		return this.a;
	}
	
	public static Emotion getByName(String name) {
		for (Emotion e : Emotion.values()) {
			if (e.name().equalsIgnoreCase(name)) {
				return e;
			}
		}
		return Emotion.HAPPY;
	}

}
