package ionic.player.content.skills.fishing;

public enum Spots {
			Cage_Harpoon(new int[]
			{ 312, 321, 324, 333, 1332, 1399, 3804, 5470, 7046 }, 377, -1, 359, 371),
			Cave_Fish_Rocktail(new int[]
			{ 8842 }, 15270, -1, -1, -1),
			Lure_Bait(new int[]
			{ 309, 310, 311, 314, 315, 317, 318, 328, 329 }, 335, 331, 349, -1),
			Net_Bait(new int[]
			{ 316, 320, 323, 325, 326, 327, 330, 1331, 2067, 2068, 2724, 4908, 5748, 5749 }, 317, 321, 325, 345),
			Net_Harpoon(new int[]
			{ 313, 322, 334, 1191, 1333, 1405, 3574, 3575, 5471, 7044 }, 341, 363, 383, -1),
			Rainbow_Spot(new int[]
			{ 927 }, 10318, -1, 349, -1);

			public static Spots forSpot(int spotId) {
				for (Spots s : Spots.values())
				{
					for (int x = 0; x < s.getSpotId().length; x++)
					{
						if (spotId == s.getSpotId()[x])
						{
							return s;
						}
					}
				}

				return null;
			}

			private final int[] spotId;
			private final int firstFish, secondFish, thirdFish, fourthFish;

			private Spots(int[] spotId, int firstFish, int secondFish, int thirdFish, int fourthFish)
			{
				this.spotId = spotId;
				this.firstFish = firstFish;
				this.secondFish = secondFish;
				this.thirdFish = thirdFish;
				this.fourthFish = fourthFish;
			}

			public int getFirstFishId() {
				return firstFish;
			}

			public int getFourthFishId() {
				return fourthFish;
			}

			public int getSecondFishId() {
				return secondFish;
			}

			public int[] getSpotId() {
				return spotId;
			}

			public int getThirdFishId() {
				return thirdFish;
			}
}
