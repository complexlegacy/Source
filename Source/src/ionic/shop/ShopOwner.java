package ionic.shop;

public class ShopOwner {
	
	public String ownerName = "Shop owner";
	public int[] shops = new int[10];
	public int ownerId;
	
	public ShopOwner(int[] s, String n, int o) {
		this.shops = s;
		this.ownerName = n;
		this.ownerId = o;
	}

}
