package connect_four;

/*
 * Board
 * An instance of this class is the game board.
 */

public class Board {
	
	// Board states as enum. 
	// IDLE => The board has empty cells without winning formation
	// DRAW => The board is full without winning formation
	// WINNER => The board has a winning formation (full or not)
	enum State {
		IDLE,
		DRAW,
		WINNER
	};
	
	// Current board's state
	private State state;
	
	// Board cells as a two dimensional char array.
	// Initialized into constructor.
	private char[][] grid;
	
	// Number of board rows and columns
	// (0,0) is at the bottom left and (rows-1,column-1) at the top right.
	private int rows;
	private int columns;
	
	// Keeps track of the number of chips on the board.
	// Used to detect if the board is full
	private int chips;
	
	/*
	 * Constructor
	 * Copy board rows and columns into class members.
	 * Initialize cells to '-' (empty).
	 */
	public Board(int rows, int columns)
	{
		this.rows = rows;
		this.columns = columns;
		this.chips = 0;		// Initially, no chips on the board
		this.grid = new char[rows][columns];
		
		// Initialize all board cells
		for (int i=0; i<rows; i++) {
			for (int j=0; j<columns; j++) {
				grid[i][j] = '-';
			}
		}
		
		// Set board state to idle
		state = State.IDLE;
	}
	
	/*
	 * getters
	 */
	public int getRows() { return this.rows; }
	public int getColumns() { return this.columns; }
	public State getState() { return this.state; }
	
	/*
	 * Checks if board is full
	 */
	public boolean isFull() {
		return chips == rows * columns;
	}
	
	/*
	 * Check if a given column is full
	 * If not full the top cell is empty ('-')
	 */
	public boolean isColumnFull(int column) {
		return grid[rows-1][column] != '-';
	}
	
	/*
	 * print the current state of the board
	 */
	public void print() {
		
		// Line break above to leave some space
		System.out.println();
		
		// If the number of columns is greater than 9
		// add more space between to fit two-digit numbers
		int spaces = columns < 10 ? 1 : 2;
		
		// print columns
		// Traverses rows in reverse because row 0 is at the bottom.
		for (int i = rows-1; i>=0; i--) {
			
			// Print left border
			System.out.print("|");
			System.out.print(" ".repeat(spaces));
			
			// Print cell data of row i
			for (int j=0; j<columns; j++) {
				System.out.print(grid[i][j]);
				System.out.print(" ".repeat(spaces));
			}
			
			// Print right border and line break
			System.out.println("|");
		}
		
		// Print bottom dashes.
		// Number of dashes are columns + (columns + 1)*spaces + 2 (left and right |).
		int dashes = columns + (columns + 1) * spaces + 2;
		System.out.println("-".repeat(dashes));
		
		// Print column numbers
		System.out.print(" ".repeat(spaces+1)); 	// spaces with leading '|'
		for (int i=0; i<columns; i++) {
			System.out.print(i+1);	// Indexes are zero-based
			if ( (i+1 < 10 && columns >= 10) || (columns < 10)) System.out.print(" ".repeat(spaces));	
			else System.out.print(" ".repeat(spaces-1));		// One space is occupied by second digit
		}
	
		// Two Line breaks to leave some space
		System.out.println();
		System.out.println();
	}
	
	/*
	 * Puts a chip on the board at given column.
	 * If the column is full return false, otherwise true.
	 */
	public boolean putChip(int column, char chip) {
		
		// Find next empty cell into column (== '-')
		// If row remains -1, column is full
		int row = -1;
		for (int i=0; i<rows; i++) {
			if (grid[i][column] == '-') {
				row = i;
				break;
			}
		}
		
		// If row >= 0 put chip and check for winning formation
		// or full board => draw
		if (row >= 0) {
			grid[row][column] = chip;
			chips++;		// Increase the total number of chips on the board
			
			// Check all lines crossing current cell for winning formation
			checkHorizontal(row);
			checkVertical(column);
			checkDownTopDiagonal(row, column);
			checkTopDownDiagonal(row, column);
			
			// Check for draw - Full board and IDLE STATE
			// If one of the above checks is successful then 
			// the state is WINNER even if the board is full.
			if (isFull() && state == State.IDLE) {
				state = State.DRAW;
			}
			
			return true;	// success
		}
		else {	// column is full
			System.out.println("Column " + (column+1) + " is full" );
			return false;
		}
	
	}
	
	/*
	 * Checks for horizontal winning formation at given row
	 * Start from the leftmost column and continue counting same chips.
	 * If the counter reaches 4 we have a winner.
	 * We mark these cells with capital chip letter.
	 */
	private void checkHorizontal(int row) {
		
		// Get the chip of the fist column in the row
		// If chip is '-' the counter will be reseted into for loop
		char chip = grid[row][0];
		int counter = 1;
		
		// Check the others
		for (int j=1; j<columns; j++) {
			
			// if current chip is '-' reset counter and continue
			if (grid[row][j] == '-') {
				counter = 0;
				continue;
			}
			// if current chip is the same as previous increase counter
			// and check if it is 4.
			else if (grid[row][j] == chip) {
				counter++;
				if (counter == 4) {
					// We have a winning formation - Set state
					state = State.WINNER;
					
					// Mark last four cells with capitals
					for (int k=j; k>j-4; k--) {
						grid[row][k] = Character.toUpperCase(grid[row][k]);
					}
					
					// exit method
					return;
				}
			}
			// The current chip is different - reset counter
			else {
				chip = grid[row][j];
				counter = 1;
			}
			
		}
	}
	
	/*
	 * Checks for vertical winning formation at given column.
	 * Start from the bottom row and continue up counting same chips.
	 * If the counter reaches 4 we have a winner.
	 * We mark these cells with capital chip letter.
	 */
	private void checkVertical(int column) {
		
		// Get the chip of the fist row in the column.
		// If chip is '-' the counter will be reseted into for loop
		char chip = grid[0][column];
		int counter = 1;
		
		// Check the others
		for (int i=1; i<rows; i++) {
			
			// if current chip is '-' reset counter and continue
			if (grid[i][column] == '-') {
				counter = 0;
				continue;
			}
			// if current chip is the some as previous increase counter
			// and check if it is 4.
			else if (grid[i][column] == chip) {
				counter++;
				if (counter == 4) {
					// We have a winning formation - Set state
					state = State.WINNER;
					
					// Mark last four cells with capitals
					for (int k=i; k>i-4; k--) {
						grid[k][column] = Character.toUpperCase(grid[k][column]);
					}
					
					// exit method
					return;
				}
			}
			// The current chip is different - reset counter
			else {
				chip = grid[i][column];
				counter = 1;
			}
			
		}
	}
	
	/*
	 * Checks for diagonal winning formation at given cell.
	 * Traverse the diagonal from botton-left to top-right
	 * that crosses the cell.
	 */
	private void checkDownTopDiagonal(int row, int column) {
		
		// Go down and left from current cell.
		// Reduce row and column simultaneously (move diagonal)
		// until one of them is zero
		int row1 = row;
		int column1 = column;
		while (row1 > 0 && column1 > 0) {
			row1--;
			column1--;
		}
		
		// Get the chip of the fist cell.
		// If chip is '-' the counter will be reseted into for loop
		char chip = grid[row1][column1];
		int counter = 1;
		
		// Check the other cells diagonally.
		// Move by increasing in each step row1 and column1 by one
		row1++;
		column1++;
		while (row1 < rows && column1 < columns) {
			// if current chip is '-' reset counter and continue
			if (grid[row1][column1] == '-') {
				counter = 0;
			}
			// if current chip is the some as previous increase counter
			// and check if it is 4.
			else if (grid[row1][column1] == chip) {
				counter++;
				if (counter == 4) {
					// We have a winning formation - Set state
					state = State.WINNER;
					
					// Mark last four cells on the diagonal with capitals
					for (int i=0; i<4; i++) {
						grid[row1-i][column1-i] = Character.toUpperCase(grid[row1-i][column1-i]);
					}
					
					// exit method
					return;
				}
			}
			// The current chip is different - reset counter
			else {
				chip = grid[row1][column1];
				counter = 1;
			}
			
			// Next cell on the diagonal
			row1++;
			column1++;
		}
	
	}
	
	/*
	 * Checks for diagonal winning formation at given cell.
	 * Traverse the diagonal form Top-left to bottom-right
	 * that crosses the cell.
	 */
	private void checkTopDownDiagonal(int row, int column) {
		
		// Go up and left from current cell.
		// Increase row and decrease column simultaneously (move diagonal)
		// until either row is rows-1 or column=0
		int row1 = row;
		int column1 = column;
		while (row1 < rows-1 && column1 > 0) {
			row1++;
			column1--;
		}
		
		// Get the chip of the fist cell of the diagonal.
		// If chip is '-' the counter will be reseted into for loop
		char chip = grid[row1][column1];
		int counter = 1;
		
		// Check the other cells diagonally down and right.
		// Move by decreasing row1 and increasing column1 by one.
		row1--;
		column1++;
		while (row1 >= 0 && column1 < columns) {
			// if current chip is '-' reset counter and continue
			if (grid[row1][column1] == '-') {
				counter = 0;
			}
			// if current chip is the same as previous increase counter
			// and check if it is 4.
			else if (grid[row1][column1] == chip) {
				counter++;
				if (counter == 4) {
					// We have a winning formation - Set state
					state = State.WINNER;
					
					// Mark last four cells on the diagonal with capitals
					for (int i=0; i<4; i++) {
						grid[row1+i][column1-i] = Character.toUpperCase(grid[row1+i][column1-i]);
					}
					
					// exit method
					return;
				}
			}
			// The current chip is different - reset counter
			else {
				chip = grid[row1][column1];
				counter = 1;
			}
			
			// Next cell on the diagonal
			row1--;
			column1++;
		}
	
	}
	
}
