/** Written by Keith/Hitten/Berserk/ReaperXScape **/
package ionic.player;

import ionic.item.ItemAssistant;
import ionic.player.banking.BankHandler;
import ionic.player.event.CycleEvent;
import ionic.player.event.CycleEventContainer;
import ionic.player.event.CycleEventHandler;

import java.math.BigInteger;

public class MoneyPouch {
public MoneyPouch(Player c) {
this.c = c;
}
private Player c;


public void updatePouch() {
c.getPA().setInterfaceText(getConvertedValue(), 43069);
}


public void tellAmount() {
	c.sendMessage("Your money pouch contains: <col=65535><shad=0>"+BankHandler.insertCommas(""+c.pouchCoins+"")+"</col></shad> coins");
}

public void textOnScreen(String s) {
	c.getPA().sendFrame126(s, 37101);
	CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
		@Override
		public void execute(CycleEventContainer container) {
			container.stop();
		}
		@Override
		public void stop() {
			c.getPA().sendFrame126("", 37101);
		}
	}, 3);
}


public void deposit(int amount) {
	int coins = ItemAssistant.getItemAmount(c, 995);
	if (coins < amount) {
		amount = coins;
	}
	if (amount < 1) {
		return;
	}
	ItemAssistant.deleteItemForBank(c, 995, amount);
	c.pouchCoins = c.pouchCoins.add(new BigInteger(Integer.toString(amount)));
	updatePouch();
	textOnScreen("@gre@+"+BankHandler.insertCommas(""+amount)+"");
}


public void withdraw(int withdrawAmount) {
	BigInteger amount = new BigInteger(""+withdrawAmount);
	if (amount.compareTo(c.pouchCoins) >= 0) {
		amount = c.pouchCoins;
	}
	BigInteger max = new BigInteger(""+Integer.MAX_VALUE);
	if (amount.compareTo(max) > 0) {
		amount = max;
	}
	BigInteger inInv = new BigInteger(""+ItemAssistant.getItemAmount(c, 995));
	if (inInv.add(amount).compareTo(max) > 0) {
		amount = max.subtract(inInv);
	}
	String s = ""+amount;
	int amt = Integer.parseInt(s);
	if (amt <= 0) {
		return;
	}
	c.pouchCoins = c.pouchCoins.subtract(amount);
	ItemAssistant.addItem(c, 995, amt);
	updatePouch();
	textOnScreen("@red@-"+BankHandler.insertCommas(""+amount)+"");
}


public String getConvertedValue() {
	int co = -1;
	int t = -1;
	int m = -1;
	int b = -1;
	BigInteger g = c.pouchCoins;
	BigInteger f = g;
	String s = "";
	s = ""+f+"";
	try {
		co = Integer.parseInt(s);
	} catch (Exception e) {
		co = -1;
	}
	f = f.divide(new BigInteger("1000"));
	s = ""+f+"";
	try {
		t = Integer.parseInt(s);
	} catch (Exception e) {
		t = -1;
	}
	f = g;
	f = f.divide(new BigInteger("1000000"));
	s = ""+f+"";
	try {
		m = Integer.parseInt(s);
	} catch (Exception e) {
		m = -1;
	}
	f = g;
	f = f.divide(new BigInteger("1000000000"));
	s = ""+f+"";
	try {
		b = Integer.parseInt(s);
	} catch (Exception e) {
		b = -1;
	}
	String e = "";
	if (t != -1 && co != -1 && co < 10000 && t < 10) {
		e = "@yel@"+co+"";
	} else if (t != -1 && co >= 10000 && t < 10000) {
		e = "@whi@"+t+"K";
	} else if (m != -1 && t >= 10000 && m < 10000) {
		e = "@gre@"+m+"M";
	} else {
		e = "@cya@"+b+"B";
	}
	return e;
}


}