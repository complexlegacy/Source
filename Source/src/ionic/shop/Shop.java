package ionic.shop;

public class Shop {
	
	public int[] items = new int[50];
	public int[] maxAmounts = new int[50];
	public int[] amounts = new int[50];
	public int[] prices = new int [50];
	public String currency = "Coins";
	public String name = "Shop name";
	public boolean buyBack = false;
	public boolean buyAll = false;
	
	public Shop(int[] i, int[] a, int[] ma, int[] p, String c, String n, boolean bb, boolean ba) {
		this.items = i;
		this.maxAmounts = ma;
		this.amounts = a;
		this.currency = c;
		this.name = n;
		this.prices = p;
		this.buyBack = bb;
		this.buyAll = ba;
	}

}
