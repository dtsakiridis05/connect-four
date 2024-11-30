package connect_four;

/*
 * Player
 * An instance of this class is a player of the game
 */
public class Player {

	// The player's name
	private String name;
	
	// The player's chip ('o' or 'x')
	private char chip;
	
	// Getters for private members
	public String getName() { return this.name; }
	public char getChip() { return this.chip; }
	
	/*
	 * Constructor
	 * Gets name and chip as parameters
	 * */
	public Player(String name, char chip) {
		this.name = name;
		this.chip = chip;
	}
}
