package ionic.grandExchange;

import java.io.*;

public class Loading {

	public static void doLoad(int fileId) {
		String fileName = ""+fileId+".txt";
		int offerId = 0, itemId = 0, amount = 0, price = 0, collected = 0, total = 0, 
				collectslot1 = 0, collectslot2 = 0;
		String type = "", owner = "";
		boolean aborted = false;
		BufferedReader reader = null;
		try {
			try {
				reader = new BufferedReader(new FileReader("./data/grandexchange/"+fileName));
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

					String[] splitz = line.split(" = ");
					if (line.startsWith("OfferID")) {
						offerId = Integer.parseInt(splitz[1]);
					}
					if (line.startsWith("OfferType")) {
						type = splitz[1];
					}
					if (line.startsWith("ItemID")) {
						itemId = Integer.parseInt(splitz[1]);
					}
					if (line.startsWith("Amount")) {
						amount = Integer.parseInt(splitz[1]);
					}
					if (line.startsWith("Price")) {
						price = Integer.parseInt(splitz[1]);
					}
					if (line.startsWith("Collected")) {
						collected = Integer.parseInt(splitz[1]);
					}
					if (line.startsWith("Owner")) {
						owner = splitz[1];
					}
					if (line.startsWith("TotalAmountBeggining")) {
						total = Integer.parseInt(splitz[1]);
					}
					if (line.startsWith("OfferAborted")) {
						aborted = Boolean.parseBoolean(splitz[1]);
					}
					if (line.startsWith("collectslot1")) {
						collectslot1 = Integer.parseInt(splitz[1]);
					}
					if (line.startsWith("collectslot2")) {
						collectslot2 = Integer.parseInt(splitz[1]);
					}

					if (line.equals("[ENDOFFILE]")) {
						load(offerId, itemId, amount, total, collected, price, type, owner, aborted, collectslot1, collectslot2);
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

	public static void load(int oId, int iId, int amt, int tamt, int coll, int prce, String type, String owner, boolean abort, int cslot1, int cslot2) {
		GrandExchangeHandler.offers[oId] = new Offer(oId, iId, amt, tamt, prce, type, owner, coll, abort, cslot1, cslot2);
	}


}
