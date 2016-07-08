package ionic.player;

/**
 * @author Keith
 * each rank with it's player rights id
 */

public enum Ranking {
	
		PLAYER(0, "@lre@Player"),
		MODERATOR(1, "@cya@Moderator"),
		ADMINISTRATOR(2, "@yel@Administrator"),
		MEMBER(3, "@red@Member"),
		SUPER_MEMBER(4, "@red@Super Member"),
		EXTREME_MEMBER(5, "@gre@Extreme Member"),
		LEGENDARY_MEMBER(6, "@cya@Legendary Member"),
		VETERAN(7, "@mag@Veteran"),
		IRON_MAN(8, "@369@Iron Man"),
		HELPER(9, "@blu@Helper"),
		GFX_DESIGNER(10, "@blu@GFX Designer");
		private Ranking(int right, String show) {
			this.rights = right;
			this.show = show;
		}
		public int rights;
		public String show;
	
		public static Ranking forID(int r) {
			for (Ranking f: Ranking.values()) {
				if (f.rights == r) {
					return f;
				}
			}
			return null;
		}

}
