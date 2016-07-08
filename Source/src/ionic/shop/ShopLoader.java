package ionic.shop;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ShopLoader {
	
	public static void loadAll() {
		for (int i = 0; i < ShopController.MAX_SHOPS; i++) {
			doLoad(i);
		}
	}
	
	
	public static void doLoad(int fileId) {
		String fileName = ""+fileId+".txt";
		int shopId = fileId;
		int[] items = new int[50];
		int[] amounts = new int[50];
		int[] maxAmounts = new int[50];
		int[] prices = new int[50];
		String name = "";
		String currency = "";
		boolean buyBack = false;
		boolean buyAll = true;
		
			BufferedReader reader = null;
			try {
				try {
				reader = new BufferedReader(new FileReader("./data/shops/"+fileName));
				} catch (FileNotFoundException localFileNotFoundException) {
				return;
			    }
				String line = "";
				try {
				line = reader.readLine();
				} catch (IOException localIOException2) {
			    }
				try {
					
				while((line = reader.readLine()) != null) {
					
				String[] splitz = line.split(" ");
				if (line.startsWith("[NAME = ")) {
					String[] kk = line.split(" = ");
					name = kk[1];
					name = name.replaceAll("]", "");
				}
				if (line.startsWith("[CURRENCY = ")) {
					String[] kk = line.split(" = ");
					currency = kk[1];
					currency = currency.replaceAll("]", "");
				}
				if (line.startsWith("BUYS_BACK_SHOP_ITEMS = ")) {
					String[] kk = line.split(" = ");
					buyBack = Boolean.parseBoolean(kk[1]);
				}
				if (line.startsWith("BUYS_EVERYTHING = ")) {
					String[] kk = line.split(" = ");
					buyAll = Boolean.parseBoolean(kk[1]);
				}
				if (line.startsWith("Item = ")) {
					int slot = Integer.parseInt(splitz[2]);
					int itemId = Integer.parseInt(splitz[3]);
					int itemAmount = Integer.parseInt(splitz[4]);
					int itemPrice = Integer.parseInt(splitz[5]);
					
					if (itemPrice == -1) {
						itemPrice = ShopController.getItemShopValue(itemId);
					}
					
					items[slot] = itemId;
					amounts[slot] = itemAmount;
					maxAmounts[slot] = itemAmount;
					prices[slot] = itemPrice;
				}
				
				if (line.equals("[ENDOFSHOP]")) {
					load(shopId, items, amounts, maxAmounts, prices, name, currency, buyBack, buyAll);
					reader.close();
					return;
				}
				
			}
			} finally {
			 reader.close();
			 }
				}catch (IOException ex){
			            ex.printStackTrace();
			        }
	}
	
	public static void load(int id, int[] items, int[] amounts, int[] maxAm, int[] prices, String name, String currency, boolean buyBack, boolean buyAll) {
		ShopController.shops[id] = new Shop(items, amounts, maxAm, prices, currency, name, buyBack, buyAll);
	}
	
	


}
