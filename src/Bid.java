
/**
 * Jerker Hedenström (jehe1589) 
 */

public class Bid {

	private User bidder;
	private String item;
	private int amount;

	public Bid(User bidder, String item, int amount) {

		this.bidder = bidder;
		this.item = item;
		this.amount = amount;
	}

	public User getBidder() {

		return bidder;
	}

	public String getItem() {

		return item;
	}

	public int getAmount() {

		return amount;
	}

	public String toString() {

		return bidder.getName() + " " + amount + " kr";
	}
}
