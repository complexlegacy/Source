package ionic.shop;

import ionic.item.ItemAssistant;
import ionic.player.Player;

public enum Currency {
	
	
	
	COINS("Coins") {
		@Override
		public void spend(Player c, int amount) {
			ItemAssistant.deleteItemForBank(c, 995, amount);
		}
		@Override
		public int getAmount(Player c) {
			return ItemAssistant.getItemAmount(c, 995);
		}
		@Override
		public void give(Player c, int amount) {
			ItemAssistant.addItem(c, 995, amount);
		}
	},
	
	
	TOKKUL("Tokkul") {
		@Override
		public void spend(Player c, int amount) {
			ItemAssistant.deleteItemForBank(c, 6529, amount);
		}
		@Override
		public int getAmount(Player c) {
			return ItemAssistant.getItemAmount(c, 6529);
		}
		@Override
		public void give(Player c, int amount) {
			ItemAssistant.addItem(c, 6529, amount);
		}
	},
	
	
	DONATOR_POINTS("Member Credits") {
		@Override
		public void spend(Player c, int amount) {
			c.donatorPoints -= amount;
		}
		@Override
		public int getAmount(Player c) {
			return c.donatorPoints;
		}
		@Override
		public void give(Player c, int amount) {
			c.donatorPoints += amount;
		}
	},
	
	
	
	VOTING_POINTS("Vote Points") {
		@Override
		public void spend(Player c, int amount) {
			c.votePoints -= amount;
		}
		@Override
		public int getAmount(Player c) {
			return c.votePoints;
		}
		@Override
		public void give(Player c, int amount) {
			c.votePoints += amount;
		}
	},
	
	
	SLAYER_POINTS("Slayer Points") {
		@Override
		public void spend(Player c, int amount) {
			c.slayerPoints -= amount;
		}
		@Override
		public int getAmount(Player c) {
			return c.slayerPoints;
		}
		@Override
		public void give(Player c, int amount) {
			c.slayerPoints += amount;
		}
	},
	
	
	PK_POINTS("PK Points") {
		@Override
		public void spend(Player c, int amount) {
			c.pkPoints -= amount;
		}
		@Override
		public int getAmount(Player c) {
			return c.pkPoints;
		}
		@Override
		public void give(Player c, int amount) {
			c.pkPoints += amount;
		}
	},
	
	PEST_CONTROL_POINTS("Pest Control Points") {
		@Override
		public void spend(Player c, int amount) {
			c.pcPoints -= amount;
		}
		@Override
		public int getAmount(Player c) {
			return c.pcPoints;
		}
		@Override
		public void give(Player c, int amount) {
			c.pcPoints += amount;
		}
	},
	
	
	
	
	;
	
	
	
	private String type;
	private Currency(String currencyTypeName) {
		this.type = currencyTypeName;
	}
	
	public String getType() {
		return this.type;
	}
	
	
	public abstract void spend(Player c, int amount);
	
	
	public abstract int getAmount(Player c);
	
	
	public abstract void give(Player c, int amount);
	
	
	public static Currency findCurrency(String s) {
		s = s.toLowerCase();
		for (Currency currency : Currency.values()) {
			if (currency.getType().toLowerCase().equals(s)) {
				return currency;
			}
		}
		return null;
	}
	
	
	public boolean has(Player c, int amount) {
		return (getAmount(c) > amount);
	}
	
	

}
