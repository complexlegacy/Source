package ionic.player.content.skills.cooking;

/**
 * Created by Jon on 2/17/2016.
 */
public enum Pies {

   REDBERRY_PIE(1951, 1933, 2313, 78, 2325);

    public int ing1, ing2, ing3, exp, product;
    private Pies(int ingredient1, int ingredient2, int ingredient3, int experience, int pie) {
        this.ing1 = ingredient1;
        this.ing2 = ingredient2;
        this.ing3 = ingredient3;
        this.exp = experience;
        this.product = pie;
    }

    public static Pies forID(int k) {
        for (Pies i: Pies.values()) {
            if (i.ing1 == k) {
                return i;
            }
        }
        return null;
    }

}
