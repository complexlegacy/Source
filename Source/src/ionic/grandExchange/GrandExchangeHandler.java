package ionic.grandExchange;

public class GrandExchangeHandler {

	public static final int MAX_OFFERS = 100;
	public static Offer[] offers = new Offer[MAX_OFFERS];


	public static void saveAll() {
		for (int i = 0; i < MAX_OFFERS; i++) {
			Saving.save(offers[i]);
		}
	}

	public static void save(int id) {
		Saving.save(offers[id]);
	}

	public static void loadGrandExchangeOffers() {
		for (int i = 0; i < MAX_OFFERS; i++) {
			Loading.doLoad(i);
		}
	}

	public static void process(int id) {
		Offer o = offers[id];
		Offer j = null;
		for (int i = 0; i < MAX_OFFERS; i++) {
			j = offers[i];
			if (j != null && o != j) {
				if (!o.type.equals(j.type)) {
					if (o.item == j.item) {
						if (((o.getTotal() - o.getAmount()) - o.getCollected()) > 0) {
							if (((j.getTotal() - j.getAmount()) - j.getCollected()) > 0) {
								if(!o.getOwner().equalsIgnoreCase(j.getOwner())) {
									if (o.type.equals("Buy")) {
										if (o.priceEach >= j.priceEach) {
											buy(o, j);
										}
									} else {
										if (j.priceEach >= o.priceEach) {
											buy(j, o);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public static void buy(Offer buy, Offer sell) {
		int amount = sell.getTotal() - sell.getAmount();
		int amount2 = buy.getTotal() - buy.getAmount();
		if (amount2 >= amount) {
			sell.amount += amount2;
			sell.collectslot2 += sell.priceEach * amount2;
			buy.amount += amount;
			buy.collectslot1 += amount;
			int returned = (buy.priceEach - sell.priceEach) * amount;
			buy.collectslot2 += returned;
		}
	}

}
