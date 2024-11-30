package connect_four;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/*
 * Game
 * Implements the game logic and initialization.
 * Also, contains the main function (entry point).
 */
public class Game {
	
	// Minimun and maximun number of columns and rows as constants
	public static final int MIN_ROWCOLS = 4;
	public static final int MAX_ROWCOLS = 15;
	
	// Scanner object to read from console
	private Scanner scanner = new Scanner(System.in);
	
	// The board of the game
	private Board board;
		
	// The players of the game
	private Player[] players = new Player[2];
	
	// Active player - makes the next move
	// It's either players[0] or players[1]
	private Player activePlayer = null;		// initialized on game start
	
	// Flag to indicate if the game has been initialized
	// Checked on play method to avoid exceptions
	private boolean initialized = false;
	
	/*
	 * Returns the player to play next.
	 * Sets active player
	 */
	public Player nextPlayer() {
		
		// If active player is null choose randomly
		if (activePlayer == null) {
			Random rand = new Random();
			activePlayer = players[rand.nextInt(2)]; 	// nextInt() return 0 or 1, (2 is exclusive)
		}
		
		// If activePlayer == players[0], next is players[1]
		else if (activePlayer == players[0]) {
			activePlayer = players[1];
		}
		
		// otherwise, next is players[0]
		else {
			activePlayer = players[0];
		}
		
		// Return activePlayer as the next player to play
		return activePlayer;

	}
	
	/*
	 * Initialize the game
	 */
	public void initialize() {	
	
		// Print game title
		System.out.println("This is Score 4");
		
		// Read player1's name
		System.out.print("Please enter the name of the 1st player: ");
		String name1 = scanner.nextLine();
		
		// Read player2's name
		System.out.print("Please enter the name of the 2nd player: ");
		String name2 = scanner.nextLine();
		
		// Get players' chips ('x', or 'o')
		// Ensure input is either 'x' or 'o'
		String strInput = "-";
		boolean done = false;
		while (!done) {
			System.out.print(name1 + ", please select your chip: ");	
			strInput = scanner.nextLine();
			if ( !strInput.equals("x") && !strInput.equals("o") ) {	// compare as strings
				System.out.println("Please, select either 'x' or 'o'");
			}
			else {
				done = true;
			}
		}
		char chip1 = strInput.charAt(0);	// Fist char of the string
		
		// Set other player's chip (trenary operator)
		char chip2 = (chip1 == 'o' ? 'x' : 'o');
		System.out.println(name2 + ", your chip is: " + chip2);
		
		// Create the two players
		players[0] = new Player(name1, chip1);
		players[1] = new Player(name2, chip2);
				
		// Get the number of rows - Check validity
		int rows = 0;
		do {
			rows = getInt("Please enter the number of rows: ");
			if (rows < Game.MIN_ROWCOLS || rows > Game.MAX_ROWCOLS) {
				System.out.print("Incorrect Input. ");
			}
		} while (rows < Game.MIN_ROWCOLS || rows > Game.MAX_ROWCOLS);
		
			
		// Get the number of columns
		int columns = 0;
		do {
			columns = getInt("Please enter the number of columns: ");
			if (columns < Game.MIN_ROWCOLS || columns > Game.MAX_ROWCOLS) {
				System.out.print("Incorrect Input. ");
			}
		} while (columns < Game.MIN_ROWCOLS || columns > Game.MAX_ROWCOLS);
		
		// Create and print the board 
		board = new Board(rows, columns);
		board.print();

		// Set initialization flag to true
		initialized = true;
		
	}
	
	/*
	 * Start and play the game
	 */
	public void play() {
		
		// Check if the game has been initialized
		if (!initialized) {
			System.out.println("The game has not been initialized. Please call initialize() method before starting the game.");
			System.exit(-1);
		}
		
		// Loop while board state is IDLE
		while(board.getState() == Board.State.IDLE) {
			
			// Next player to play
			Player player = nextPlayer();
			
			// Prompt next player to select a column for his chip
			// Check if column is full
			String prompt = player.getName() + ", your turn. Select column: ";
			int column = 0;		// invalid column number
			boolean done = false;
		
			while (!done) {
				
				column = getInt(prompt);
				
				// Check for valid selected column number
				if (column < 1 || column > board.getColumns()) {
					System.out.println("Incorrect Column Number. Please enter a number from 1 to " + board.getColumns() + ": ");
					continue;	// Go to while(!done)
				}
				
				// Check for full column
				if (board.isColumnFull(column-1)) {
					System.out.println("The selected column is full! Please select another one");
					continue;	// Go to while(!done)
				}
				
				// Selected column is ok
				done = true;
							
			}
			
			//  Put the chip
			// Board state changes on each chip input 
			board.putChip(column-1, player.getChip());
									
			// print the board
			board.print();
			
			// Check new board state - Updates after putChip()
			// If the state is WINNER then the winner player is the active one.
			if (board.getState() == Board.State.DRAW) {
				System.out.println("GAME OVER. WE HAVE A DRAW");
			}
			else if (board.getState() == Board.State.WINNER) {
				System.out.println("GAME OVER. THE WINNER IS " + player.getName());
			}
		}
	}
	
	/*
	 * Helper function to read an integer from console with exception handling
	 */
	private int getInt(String prompt) {
		boolean done = false;
		int input = 0;
		while (!done) {
			try {
				System.out.print(prompt);
				input = scanner.nextInt();
				done = true;
			}
			catch (InputMismatchException e) {
				System.out.println("Incorrect Input. Please give a numberic value.");
				scanner.nextLine();		// Clear input buffer
			}
		}
		return input;
	}

	
	/*
	 * The entry point of the program
	 */
	public static void main(String[] args) {
		Game game = new Game();
		game.initialize();
		game.play();
	}

}
