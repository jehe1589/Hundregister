import java.util.ArrayList;

/**
 * Jerker Hedenström (jehe1589) 
 */

public class User {

	private String name;
	private ArrayList<Dog> petsOwned = new ArrayList<>();

	public User(String name) {

		this.name = name;
	}

	public void buyDog(Dog d) {

		petsOwned.add(d);
	}
	
	public void removeDog(Dog d) {
		
		petsOwned.remove(d);
	}

	private String printPetsOwned() {

		String bracket_left = "[";
		String bracket_right = "]";
		String list = "";
		int iterator = 0;

		for (Dog d : petsOwned) {

			if (iterator == 0) {

				list += d.getName();
				iterator++;
			} else {

				list += ", " + d.getName();
			}
		}

		return bracket_left + list + bracket_right;
	}

	public String getName() {

		return name;
	}

	public String toString() {

		return name + " " + printPetsOwned();
	}
}
