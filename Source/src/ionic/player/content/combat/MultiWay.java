package ionic.player.content.combat;

public class MultiWay {

	/**
	 * More simple way of checking if an entity is in a multi combat zone, because
	 * before it had to be handled in npc.java and player.java
	 * @param x : The X coordinate that the entity is at
	 * @param y : The Y coordinate that the entity is at
	 */
	public static boolean inMulti(int x, int y) {
		if ((x >= 4154 && x <= 4237 && y >= 5683 && y <= 5755)
				|| (x >= 3136 && x <= 3327 && y >= 3519 && y <= 3607) 
				|| (x >= 3190 && x <= 3327 && y >= 3648 && y <= 3839) 
				|| (x >= 2863 && x <= 2877 && y >= 3350 && y <= 5374)
				|| (x >= 2889 && x <= 2908 && y >= 5255 && y <= 5276)
				|| (x >= 2915 && x <= 2941 && y >= 5316 && y <= 5332)
				|| (x >= 2823 && x <= 2843 && y >= 5295 && y <= 5309)
				|| (x >= 3334 && x <= 3355 && y >= 3208 && y <= 3217) 
				|| (x >= 3200 && x <= 3390 && y >= 3840 && y <= 3967) 
				|| (x >= 2992 && x <= 3007 && y >= 3912 && y <= 3967) 
				|| (x >= 2946 && x <= 2959 && y >= 3816 && y <= 3831) 
				|| (x >= 3008 && x <= 3199 && y >= 3856 && y <= 3903) 
				|| (x >= 3008 && x <= 3071 && y >= 3600 && y <= 3711) 
				|| (x >= 3072 && x <= 3327 && y >= 3608 && y <= 3647) 
				|| (x >= 2624 && x <= 2690 && y >= 2550 && y <= 2619) 
				|| (x >= 2371 && x <= 2422 && y >= 5062 && y <= 5117) 
				|| (x >= 2896 && x <= 2927 && y >= 3595 && y <= 3630) 
				|| (x >= 2892 && x <= 2932 && y >= 4435 && y <= 4464) 
				|| (x >= 2814 && x <= 2880 && y >= 3739 && y <= 3780)
				|| (x >= 2975 && x <= 2999 && y >= 9625 && y <= 9659) 
				|| (x >= 2917 && x <= 2970 && y >= 4356 && y <= 4407) 
				|| (x >= 3154 && x <= 3175 && y >= 3490 && y <= 3500) 
				|| (x >= 3147 && x <= 3193 && y >= 9737 && y <= 9778)
				|| (x >= 2256 && x <= 2287 && y >= 4680 && y <= 4711)
				|| (x >= 2664 && x <= 2689 && y >= 3708 && y <= 3735)
				) {
			return true;
		}
		return false;
	}

}
