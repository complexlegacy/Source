package ionic.grandExchange;

import ionic.item.ItemAssistant;
import ionic.player.Player;

public class GEButtons {
	
	public static void click(Player c, int button) {
		switch(button) {
			
		case 96078:
			c.getOutStream().createFrame(194);
		break;
		
		case 96182:
		case 96082:
			Other.openGE(c);
		break;
		
		case 96074:
			Other.confirmBuy(c);
		break;
		case 96174:
			Other.confirmSell(c);
		break;
		
		case 96054://edit buy amount
			c.inputAction = "editBuyAmount";
			c.getOutStream().createFrame(27);
			break;
		case 96066://edit buy price
			c.inputAction = "editBuyPrice";
			c.getOutStream().createFrame(27);
			break;
		case 96154://edit sell amount;
			c.inputAction = "editSellAmount";
			c.getOutStream().createFrame(27);
			break;
		case 96166://edit sell price;
			c.inputAction = "editSellPrice";
			c.getOutStream().createFrame(27);
			break;
			
		
		
		case 195132:
			Interfaces.resetBuyInterface(c);
			c.getPD().slotSelected = 0;
			c.getPA().showInterface(24600);
			c.getOutStream().createFrame(194);
		break;
		case 195157:
			Interfaces.resetBuyInterface(c);
			c.getPD().slotSelected = 1;
			c.getPA().showInterface(24600);
			c.getOutStream().createFrame(194);
		break;
		case 195182:
			Interfaces.resetBuyInterface(c);
			c.getPD().slotSelected = 2;
			c.getPA().showInterface(24600);
			c.getOutStream().createFrame(194);
		break;
		case 195207:
			Interfaces.resetBuyInterface(c);
			c.getPD().slotSelected = 3;
			c.getPA().showInterface(24600);
			c.getOutStream().createFrame(194);
		break;
		case 195232:
			Interfaces.resetBuyInterface(c);
			c.getPD().slotSelected = 4;
			c.getPA().showInterface(24600);
			c.getOutStream().createFrame(194);
		break;
		case 196001:
			Interfaces.resetBuyInterface(c);
			c.getPD().slotSelected = 5;
			c.getPA().showInterface(24600);
			c.getOutStream().createFrame(194);
		break;
		
		
		case 195135:
			Interfaces.resetSellInterface(c);
			c.getPD().slotSelected = 0;
			c.getPA().showInterface(24700);
		break;
		case 195160:
			Interfaces.resetSellInterface(c);
			c.getPD().slotSelected = 1;
			c.getPA().showInterface(24700);
		break;
		case 195185:
			Interfaces.resetSellInterface(c);
			c.getPD().slotSelected = 2;
			c.getPA().showInterface(24700);
		break;
		case 195210:
			Interfaces.resetSellInterface(c);
			c.getPD().slotSelected = 3;
			c.getPA().showInterface(24700);
		break;
		case 195235:
			Interfaces.resetSellInterface(c);
			c.getPD().slotSelected = 4;
			c.getPA().showInterface(24700);
		break;
		case 196004:
			Interfaces.resetSellInterface(c);
			c.getPD().slotSelected = 5;
			c.getPA().showInterface(24700);
		break;
		
		
		
		
		case 96030:
			Other.decreaseBuyAmount(c, 1);
		break;
		case 96038:
		case 96034:
			Other.increaseBuyAmount(c, 1);
		break;
		case 96042:
			Other.increaseBuyAmount(c, 10);
		break;
		case 96046:
			Other.increaseBuyAmount(c, 100);
		break;
		case 96050:
			Other.increaseBuyAmount(c, 1000);
		break;
		case 96086:
			Other.decreaseBuyPrice(c, 1);
		break;
		case 96089:
			Other.increaseBuyPrice(c, 1);
		break;
		case 96062:
			c.getPD().buyPrice = c.getPD().buyDefaultValue;
			Other.updatePrice(c);
		break;
		case 96058:
			double lessamount = (c.getPD().buyPrice * 0.05);
			if (lessamount < 30) { lessamount = 1; }
			Other.decreaseBuyPrice(c, (int)lessamount);
		break;
		case 96070:
			double moreamount = (c.getPD().buyPrice * 0.05);
			if (moreamount < 30) { moreamount = 1; }
			Other.increaseBuyPrice(c, (int)moreamount);
		break;
		
		
		
		
		case 96130:
			Other.decreaseSellAmount(c, 1);
		break;
		case 96138:
		case 96134:
			Other.increaseSellAmount(c, 1);
		break;
		case 96142:
			Other.increaseSellAmount(c, 10);
		break;
		case 96146:
			Other.increaseSellAmount(c, 100);
		break;
		case 96150:
			if (c.getPD().sellItem < 1) {return;}
			int amt = ItemAssistant.getItemCountWithNotes(c, c.getPD().sellItem);
			c.getPD().sellAmount = amt;
			Other.updateSellAmount(c);
		break;
		case 96186:
			Other.decreaseSellPrice(c, 1);
		break;
		case 96189:
			Other.increaseSellPrice(c, 1);
		break;
		case 96162:
			c.getPD().sellPrice = c.getPD().sellDefaultValue;
			Other.updateSellPrice(c);
		break;
		case 96158:
			double lessamount2 = (c.getPD().sellPrice * 0.05);
			if (lessamount2 < 30) { lessamount2 = 1; }
			Other.decreaseSellPrice(c, (int)lessamount2);
		break;
		case 96170:
			double moreamount2 = (c.getPD().sellPrice * 0.05);
			if (moreamount2 < 30) { moreamount2 = 1; }
			Other.increaseSellPrice(c, (int)moreamount2);
		break;
		
		
		case 96026:
			c.getPA().closeAllWindows();
		break;
		case 96126:
			c.getPA().closeAllWindows();
		break;
		
		case 201060:
		case 199072:
			Other.abortOffer(c, c.getPD().slotSelected, button);
		break;
		case 196036:
			Other.abortOffer(c, 0, button);
		break;
		case 196061:
			Other.abortOffer(c, 1, button);
		break;
		case 196086:
			Other.abortOffer(c, 2, button);
		break;
		case 196111:
			Other.abortOffer(c, 3, button);
		break;
		case 196136:
			Other.abortOffer(c, 4, button);
		break;
		case 196161:
			Other.abortOffer(c, 5, button);
		break;
		
		case 196025:
			Other.viewOffer(c, 0);
		break;
		case 196050:
			Other.viewOffer(c, 1);
		break;
		case 196075:
			Other.viewOffer(c, 2);
		break;
		case 196100:
			Other.viewOffer(c, 3);
		break;
		case 196125:
			Other.viewOffer(c, 4);
		break;
		case 196150:
			Other.viewOffer(c, 5);
		break;
		
		}
	}

}
