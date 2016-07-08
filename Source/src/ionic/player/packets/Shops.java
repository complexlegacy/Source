package ionic.player.packets;

import ionic.player.Client;
import ionic.shop.ShopController;

public class Shops implements PacketType {
	
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		switch(packetType) {
		case 55://Recieve Amount
			int amount = c.getInStream().readDWord();
			c.shopPriceAmount = amount;
			break;
		case 54://Buy/Sell
			int k = c.getInStream().readUnsignedWord();
			int type = c.getInStream().readUnsignedWord();
			if (type == 1) {//sell to shop
				ShopController.sellToShop(c, k, c.shopPriceAmount);
			} else if (type == 2) {//buy from shop
				ShopController.buyFromShop(c, k, c.shopPriceAmount);
			} else if (type == 3) {//value item inside shop
				ShopController.valueShopItem(c, k);
			} else if (type == 4) {//get price that shop buys that item for
				ShopController.valueShopSellItem(c, k);
			}
			break;
		}
	}	
}
