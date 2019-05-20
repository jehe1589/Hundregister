import java.util.Arrays;

/**
 * Jerker Hedenström (jehe1589) 
 */

public class Auction {

	private int number;
	private Bid [] bids;
	private Dog item;

	public Auction(int number, Dog d) {

		this.number = number;
		this.item = d;
		bids = new Bid[0];
	}

	public void addNewBid(Bid bid) {

		if(bids.length > 0) {
			
			for(int i = 0; i < bids.length; i++) {
				
				if(bid.getBidder() == bids[i].getBidder()) {
					
					Bid[] newBids = new Bid[bids.length];
					System.arraycopy(bids, 0, newBids, 0, bids.length);
					newBids[i] = bid;
					bids = newBids;
					return;
				}
			}
		}
		
		Bid[] newArray = new Bid[bids.length + 1];
		System.arraycopy(bids, 0, newArray, 0, bids.length);
		newArray[newArray.length - 1] = bid;
		bids = newArray;
	}

	public int getHighestBid() {

		int highest = 0;

		for (Bid b : bids) {

			if (b.getAmount() > highest) {

				highest = b.getAmount();
			}
		}

		return highest + 1;
	}

	public void printBids() {

		sortBids();

		if (bids.length == 0) {

			System.out.println("No bids made yet" + "\n");
			return;
		}

		for (Bid b : bids) {

			System.out.println(b);
		}

		System.out.println();
	}

	public void removeUserBids(User u) {
		
		for(int i = 0; i < bids.length; i++) {
			
			if(bids[i].getBidder() == u) {
				
				Bid[] newList = new Bid[bids.length - 1];
				System.arraycopy(bids, 0, newList, 0, i);
				System.arraycopy(bids, i + 1, newList, i, bids.length - i - 1);
				bids = newList;
			}
		}
	}

	public void close() {

		sortBids();

		if (bids.length == 0) {

			System.out.println("The auction is closed. No bids were made for " + item.getName() + "\n");
			return;
		}

		User u = bids[0].getBidder();

		item.setOwner(u);
		item.endAuction();
		u.buyDog(item);
		System.out.println("The auction is closed. The winning bid was " + bids[0].getAmount() + " and was made by "
				+ u.getName() + "\n");
	}

	private String printTopBids() { 

		if (bids.length == 0) {

			return "No bids made yet";
		}

		sortBids();
		Bid[] topThree = new Bid[3];

		for (int i = 0; i < 3; i++) {

			topThree[i] = bids[i];

			if (bids.length - 1 == i) {

				Bid[] highest = trimArray(topThree, bids.length);
				return "Top bids: " + Arrays.toString(highest);
			}
		}

		return "Top bids: " + Arrays.toString(topThree);
	}

	private void sortBids() {
		
		for (int iterator = 0; iterator < bids.length; iterator++) {

			for (int index = bids.length - 1; index > iterator; index--) {

				if (bids[index].getAmount() > bids[iterator].getAmount()) {

					Bid lower = bids[iterator];
					bids[iterator] = bids[index];
					bids[index] = lower;
				}
			}
		}
	}

	private Bid[] trimArray(Bid[] topThree, int size) {

		if (size == 3) {

			return topThree;
		}

		Bid[] highest = new Bid[size];

		for (int i = 0; i < size; i++) {

			highest[i] = topThree[i];
		}

		return highest;
	}

	public int getNumber() {

		return number;
	}

	public Dog getItem() {

		return item;
	}

	public String toString() {

		return "Auction #" + number + ": " + item.getName() + ". " + printTopBids();
	}
}
