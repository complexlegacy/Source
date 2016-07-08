package ionic.grandExchange;

import java.io.*;

public class Saving {
	
	
	public static void save(Offer o) {
		if (o == null) { return; }
		writeSave(o);
	}

	
	public static void writeSave(Offer o) {
		BufferedWriter writer = null;
		try {
		writer = new BufferedWriter(new FileWriter("./data/grandexchange/"+o.getSlot()+".txt"));
				writer.write("[GRAND EXCHANGE OFFER]", 0, 22);
				writer.newLine();
				writer.newLine();
				writer.write("OfferID = ", 0, 10);
				writer.write(""+o.getSlot()+"", 0, Integer.toString(o.getSlot()).length());
				writer.newLine();
				writer.write("OfferType = ", 0, 12);
				writer.write(""+o.getType()+"", 0, o.getType().length());
				writer.newLine();
				writer.newLine();
				writer.write("ItemID = ", 0, 9);
				writer.write(""+o.getItem()+"", 0, Integer.toString(o.getItem()).length());
				writer.newLine();
				writer.write("Amount = ", 0, 9);
				writer.write(""+o.getAmount()+"", 0, Integer.toString(o.getAmount()).length());
				writer.newLine();
				writer.write("Price = ", 0, 8);
				writer.write(""+o.getPrice()+"", 0, Integer.toString(o.getPrice()).length());
				writer.newLine();
				writer.write("Collected = ", 0, 12);
				writer.write(""+o.getCollected()+"", 0, Integer.toString(o.getCollected()).length());
				writer.newLine();
				writer.newLine();
				writer.write("Owner = ", 0, 8);
				writer.write(""+o.getOwner()+"", 0, o.getOwner().length());
				writer.newLine();
				writer.newLine();
				writer.write("TotalAmountBeggining = ", 0, 23);
				writer.write(""+o.getTotal()+"", 0, Integer.toString(o.getTotal()).length());
				writer.newLine();
				writer.newLine();
				writer.write("OfferAborted = ", 0, 15);
				writer.write(""+o.getAborted()+"", 0, Boolean.toString(o.getAborted()).length());
				writer.newLine();
				writer.newLine();
				writer.write("collectslot1 = ", 0, 15);
				writer.write(""+o.getCSlot(1)+"", 0, Integer.toString(o.getCSlot(1)).length());
				writer.newLine();
				writer.write("collectslot2 = ", 0, 15);
				writer.write(""+o.getCSlot(2)+"", 0, Integer.toString(o.getCSlot(2)).length());
				writer.newLine();
				writer.newLine();
				writer.write("[ENDOFFILE]", 0, 11);
			writer.close();
			} catch (IOException localIOException) {
		    }
		}
}
