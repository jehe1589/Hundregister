import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Jerker Hedenström (jehe1589) 
 */

public class Kennel {

	private ArrayList<Dog> kennelDogs = new ArrayList<>();
	private ArrayList<User> users = new ArrayList<>();
	private ArrayList<Auction> auctions = new ArrayList<>();
	private int auctionNumber;
	private Scanner keyboard;

	public Kennel() {

		keyboard = new Scanner(System.in);

		Dog dogTheFirst = new Dog("Tobbe", "Chihuahua", 5, 6);
		Dog dogTheSecond = new Dog("Felix", "Tax", 6, 7);
		Dog dogTheThird = new Dog("Thompan", "Golden Retriever", 10, 4);
		kennelDogs.add(dogTheFirst);
		kennelDogs.add(dogTheSecond);
		kennelDogs.add(dogTheThird);
	}

	public static void main(String[] args) {

		Kennel doghouse = new Kennel();

		do {

			doghouse.printMenu();
			String choice = doghouse.keyboard.nextLine();

			if (choice.equalsIgnoreCase("exit")) {

				System.out.println("Programmet har avslutats");
				return;
			}

			doghouse.printCommandMenu(doghouse, choice);
		} while (true);
	}

	private void registerNewDog() {

		System.out.println("Please state the name of the dog: ");
		String name = normalizeString(keyboard.nextLine());

		System.out.println("Breed: ");
		String breed = normalizeString(keyboard.nextLine());

		System.out.println("Age as a round number: ");
		int age = keyboard.nextInt();

		System.out.println("Weight as a round number: ");
		int weight = keyboard.nextInt();
		keyboard.nextLine();
		System.out.println();

		Dog doggie = new Dog(name, breed, age, weight);
		kennelDogs.add(doggie);
		System.out.println(doggie.getName() + " has moved into the kennel!" + "\n");
	}

	private void increaseAge() {

		System.out.println("State the name of the dog to be aged: ");
		String statedName = normalizeString(keyboard.nextLine());
		if (!dogExists(statedName)) {

			return;
		}

		for (Dog doggie : kennelDogs) {

			if (doggie.getName().equals(statedName)) {

				doggie.increaseAge();
				System.out.println(doggie.getName() + " has been aged a year" + "\n");
				return;
			}
		}

		System.out.println("Error: No dog with that name was found in the registry" + "\n");
	}

	private void listDogs() {

		if (kennelDogs.isEmpty()) {

			System.out.println("Error: The kennel has no dogs right now" + "\n");
			return;
		}

		sortDogs();
		Iterator<Dog> iter = kennelDogs.iterator();

		System.out.println("Please state a fractional number. All dogs with longer or equally long tails will be listed");
		double statedLength = keyboard.nextDouble();
		keyboard.nextLine();
		System.out.println();

		while (iter.hasNext()) {

			Dog iteration = (Dog) iter.next();

			if (iteration.getTailLength() >= statedLength) {

				System.out.println(iteration);
			}
		}

		System.out.println();
	}

	private void removeDog() {

		System.out.println("Please state the name of the dog to be removed: ");
		String remove = normalizeString(keyboard.nextLine());

		if (kennelDogs.isEmpty()) {

			System.out.println("No dogs in the kennel");
			return;
		}

		for (Dog doggie : kennelDogs) {

			if (doggie.getName().equalsIgnoreCase(remove)) {

				kennelDogs.remove(doggie);
				removeDogFromOwner(doggie);
				auctions.remove(doggie.getAuction());
								
				System.out.println(doggie.getName() + " has been removed" + "\n");
				return;
			}
		}

		System.out.println("Error: No dog with the stated name was found in the registry" + "\n");
	}

	private void registerNewUser() {

		System.out.println("Name: ");
		String name = normalizeString(keyboard.nextLine());

		User u = new User(name);
		users.add(u);
		System.out.println("User " + u.getName() + " has been added" + "\n");
	}

	private void listUsers() {

		if (users.isEmpty()) {

			System.out.println("Error: No users in registry" + "\n");
			return;
		}

		for (User u : users) {

			System.out.println(u);
		}
		
		System.out.println();
	}

	private void removeUser() {

		System.out.println("Name of the user: ");
		String name = normalizeString(keyboard.nextLine());
		if (!userExists(name)) {

			return;
		}

		for (User u : users) {

			if (u.getName().equals(name)) {

				users.remove(u);
				deleteDogs(u);
				removeBids(u);
				System.out.println(u.getName() + " has been removed from the register" + "\n");
				return;
			}
		}
	}

	private void makeBid() {

		System.out.println("Enter the name of the bidder: ");
		String name = normalizeString(keyboard.nextLine());
		if (!userExists(name)) {

			return;
		}

		System.out.println("Enter the name of the dog: ");
		String dog = normalizeString(keyboard.nextLine());
		if (!dogExists(dog)) {

			return;
		}
		if (!auctioningDog(dog)) {

			System.out.println("Error: this dog is not up for auctioning" + "\n");
			return;
		}

		Auction a = getDog(dog).getAuction();
		int bid = placeBid(a);

		Bid b = new Bid(getUser(name), dog, bid);
		a.addNewBid(b);
		System.out.println("Bid made" + "\n");
		keyboard.nextLine();
	}

	private void listBids() {

		System.out.println("Enter the name of the dog: ");
		String dog = normalizeString(keyboard.nextLine());
		if (!dogExists(dog)) {

			return;
		}
		if (!auctioningDog(dog)) {

			System.out.println("Error: this dog is not up for auctioning" + "\n");
			return;
		}

		Auction a = getDog(dog).getAuction();

		System.out.println("Here are the bids");
		a.printBids();
	}

	private void startAuction() {

		System.out.println("State the name of the dog: ");
		String statedName = normalizeString(keyboard.nextLine());
		if (!dogExists(statedName)) {

			return;
		}
		if (auctioningDog(statedName)) {

			System.out.println("Error: this dog is already up for auction" + "\n");
			return;
		}
		if (getDog(statedName).hasOwner()) {

			System.out.println("Error: this dog already has an owner" + "\n");
			return;
		}

		Dog d = getDog(statedName);
		Auction a = new Auction(generateAuctionNumber(), d);
		d.putForAuction(a);
		auctions.add(a);
		System.out.println(statedName + " has been put up for auction in auction #" + a.getNumber() + "\n");
	}

	private void listAuctions() {

		if (auctions.isEmpty()) {

			System.out.println("Error: no auctions in progress" + "\n");
			return;
		}

		for (Auction a : auctions) {

			System.out.println(a);
		}
		System.out.println();
	}

	private void closeAuction() {

		System.out.println("State the name of the dog: ");
		String statedName = normalizeString(keyboard.nextLine());
		if (!dogExists(statedName)) {

			return;
		}
		if (!auctioningDog(statedName)) {

			System.out.println("Error: this dog is not up for auctioning" + "\n");
			return;
		}

		Auction a = getDog(statedName).getAuction();
		a.close();
		auctions.remove(a);
	}

	private void printMenu() {

		System.out.println("||--------------MENU--------------||" + "\n");

		System.out.println("What do you want to do? Write the corresponding command!: ");
		System.out.println("Register new dog");
		System.out.println("Increase age");
		System.out.println("List dogs");
		System.out.println("Remove dog");
		System.out.println("Register new user");
		System.out.println("List users");
		System.out.println("Remove user");
		System.out.println("Make bid");
		System.out.println("List bids");
		System.out.println("Start auction");
		System.out.println("List auctions");
		System.out.println("Close auction");
		System.out.println("Exit");
	}

	private String normalizeString(String target) {

		target = target.trim();

		if (target.isEmpty()) {

			System.out.println("Error: this line can't be empty! Please type in a line: ");
			String name = normalizeString(keyboard.nextLine());

			return name;
		}

		String firstLetter = target.substring(0, 1).toUpperCase();
		String otherLetters = target.substring(1).toLowerCase();

		target = firstLetter + otherLetters;

		return target;
	}

	private void printCommandMenu(Kennel doghouse, String com) {

		com = normalizeString(com);
		
		switch (com) {

		case "Register new dog":
			doghouse.registerNewDog();
			break;
		case "Increase age":
			doghouse.increaseAge();
			break;
		case "List dogs":
			doghouse.listDogs();
			break;
		case "Remove dog":
			doghouse.removeDog();
			break;
		case "Register new user":
			doghouse.registerNewUser();
			break;
		case "List users":
			doghouse.listUsers();
			break;
		case "Remove user":
			doghouse.removeUser();
			break;
		case "Make bid":
			doghouse.makeBid();
			break;
		case "List bids":
			doghouse.listBids();
			break;
		case "Start auction":
			doghouse.startAuction();
			break;
		case "List auctions":
			doghouse.listAuctions();
			break;
		case "Close auction":
			doghouse.closeAuction();
			break;
		default:
			System.out.println("Error: The command was not recognized by the program" + "\n");
			break;
		}
	}

	private void sortDogs() {

		Collections.sort(kennelDogs);
	}
	
	private void removeDogFromOwner(Dog d) {
		
		if(d.hasOwner()) {
			
			d.getOwner().removeDog(d);
		}
	}

	private boolean dogExists(String name) {

		for (Dog d : kennelDogs) {

			if (d.getName().equals(name)) {

				return true;
			}
		}

		System.out.println("Error: No such dog exists" + "\n");
		return false;
	}

	private boolean userExists(String name) {

		for (User u : users) {

			if (u.getName().equals(name)) {

				return true;
			}
		}

		System.out.println("Error: No such user exists" + "\n");
		return false;
	}

	private int generateAuctionNumber() {

		return ++auctionNumber;
	}

	private Dog getDog(String name) {

		Dog dog = null;

		for (Dog d : kennelDogs) {

			if (name.equals(d.getName())) {

				dog = d;
				return dog;
			}
		}

		return dog;
	}

	private int placeBid(Auction a) {

		System.out.println("Enter the amount to bid" + "(" + "min " + a.getHighestBid() + ")" + ": ");
		int bid = keyboard.nextInt();

		while (bid < a.getHighestBid()) {

			System.out.println("Error: bid too low" + "\n");
			System.out.println("Enter the amount to bid" + "(" + "min " + a.getHighestBid() + ")" + ": ");
			bid = keyboard.nextInt();
		}

		System.out.println();
		return bid;
	}

	private User getUser(String name) {

		User user = null;

		for (User u : users) {

			if (name.equals(u.getName())) {

				user = u;
				return user;
			}
		}

		return user;
	}

	private boolean auctioningDog(String name) {

		for (Auction a : auctions) {

			if (name.equals(a.getItem().getName())) {

				return true;
			}
		}
		
		return false;
	}

	private void deleteDogs(User u) {

		Iterator<Dog> iter = kennelDogs.iterator();
		
		while(iter.hasNext()) {
			
			Dog d = (Dog) iter.next();
			
			if(d.getOwner() == u) {
				
				iter.remove();
			}
		}
	}

	private void removeBids(User u) {

		for (Auction a : auctions) {

			a.removeUserBids(u);
		}
	}
}