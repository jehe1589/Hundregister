/** 
 * Jerker Hedenström(jehe1589) 
 */

public class Dog implements Comparable<Dog> {

	private String name;
	private String breed;
	private int age;
	private int weight;
	private User owner;
	private Auction auction;

	public Dog(String name, String breed, int age, int weight) {

		this.name = name;
		this.breed = breed;
		this.age = age;
		this.weight = weight;
	}

	public void increaseAge() {

		++age;
	}

	public void putForAuction(Auction a) {

		this.auction = a;
	}

	public boolean hasOwner() {

		if (owner == null) {

			return false;
		}

		return true;
	}

	private String ownedBy() {

		if (owner == null) {

			return "has no owner";
		}

		return "is owned by " + owner.getName();
	}

	public double getTailLength() {
		
		double tailLength;
		
		if (breed.equals("Tax") || breed.equals("Dachshund")) {
			tailLength = 3.7;
		} else {
			double d = age * weight;
			tailLength = d / 10;
		}
		
		return tailLength;
	}
	
	public void endAuction() {
		
		this.auction = null;
	}
	
	public int compareTo(Dog d) {

		if (this.getTailLength() > d.getTailLength()) {

			return 1;
		} else if (this.getTailLength() < d.getTailLength()) {

			return -1;
		} else {

			return this.getName().compareTo(d.getName());
		}
	}

	public String getName() {

		return name;
	}

	public String getBreed() {

		return breed;
	}

	public int getAge() {

		return age;
	}

	public int getWeight() {

		return weight;
	}

	public User getOwner() {

		return owner;
	}

	public Auction getAuction() {

		return auction;
	}

	public void setOwner(User u) {

		this.owner = u;
	}

	public String toString() {

		return getName() + " is a " + breed + " who is " + age + " years old, weighs " + weight
				+ " kilograms, has a tail length of " + getTailLength() + " and " + ownedBy();
	}
}