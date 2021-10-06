package tictactoegame;

import java.util.*;

public class GameManager {
	
	private final int ROW_COUNT = 3;
	private final int COL_COUNT = 3;
	private final int GRID_LENGTH = 4;
	private final char NULL_MARK = 'n';
	
	private final String CHARACTERS[] = {"(▰˘◡˘▰)","(◜௰◝)","（☉∀☉）","( ͡° ͜ʖ ͡°)","(`･ω･´)"};
	
	private Scanner sc = new Scanner(System.in);
	private int gameCount = 0;
	private boolean gameOver = false;
	private boolean isRunning = true;
	private char[] currentBoard;
	private ArrayList<Player> players = new ArrayList<Player>();
	private Queue<Player> turnQueue = new LinkedList<Player>();
	
	
	public GameManager() {
		while(isRunning) {
			displayMenu();
			getMenuInput();
		}
	}
	
	private void getMenuInput() {
		int temp = sc.nextInt();
		
		switch(temp){
			
		case 1: 
			requestPlayerInfo();
			break;
		case 2:
			listAllPlayers();
			break;
		case 3:
			startGame();
			break;
		case 4:
			isRunning = false;
			System.out.println("byebye");
			break;
		default:
			System.out.println("invalid choice");
			break;
		}
	}
	
	private void listAllPlayers() {
		System.out.println("Current players: ");
		System.out.println("------------------------");
		for(Player p : players) {
			System.out.println(p.getCharacter() +" "+ p.getName() + " (" + p.getMark()+")");
		}
		
		System.out.println();
	}
	
	private void displayHeader() {
		String header = "                                                                                    \n"
				+ ",--------.,--. ,-----.    ,--------. ,---.   ,-----.    ,--------. ,-----. ,------. \n"
				+ "'--.  .--'|  |'  .--./    '--.  .--'/  O  \\ '  .--./    '--.  .--''  .-.  '|  .---' \n"
				+ "   |  |   |  ||  |           |  |  |  .-.  ||  |           |  |   |  | |  ||  `--,  \n"
				+ "   |  |   |  |'  '--'\\       |  |  |  | |  |'  '--'\\       |  |   '  '-'  '|  `---. \n"
				+ "   `--'   `--' `-----'       `--'  `--' `--' `-----'       `--'    `-----' `------'";
		
		System.out.println(header);
	}
	
	
	private void displayerHeader2() {
		String header = "                                                                                    \n"
				+ ",--------.,--. ,-----.    ,--------. ,---.   ,-----.    ,--------. ,-----. ,------. \n"
				+ "'--.  .--'|  |'  .--./    '--.  .--'/  O  \\ '  .--./    '--.  .--''  .-.  '|  .---' \n"
				+ "   |  |   |  ||  |           |  |  |  .-.  ||  |           |  |   |  | |  ||  `--,  \n"
				+ "   |  |   |  |'  '--'\\       |  |  |  | |  |'  '--'\\       |  |   '  '-'  '|  `---. \n"
				+ "   `--'   `--' `-----'       `--'  `--' `--' `-----'       `--'    `-----' `------'";
		
		System.out.println(header);
	}

	private void displayMenu() {
		displayHeader();
		System.out.println("1) Add player");
		System.out.println("2) List current players(" + players.size()+")");
		System.out.println("3) Start game");
		System.out.println("4) Exit");
		
		System.out.println();
		
	}
	
	private String requestCharacterChoice() {
		int counter = 1;
		for(String s: CHARACTERS) {
			System.out.println(counter+ " " + s);
			counter++;
		}
		
		int choice = sc.nextInt();
		if(choice > CHARACTERS.length || choice < 1) {
			System.out.println("invalid choice");
			return requestCharacterChoice();
		}
		
		return CHARACTERS[choice-1];
		
	}
	
	
	private void requestPlayerInfo() {
		System.out.println("Choose your character!");
		String chosenCharacter = requestCharacterChoice();
		System.out.println("Enter player name: ");
		String name = sc.next();
		System.out.println("Enter your prefered marking eg. X O");
		char mark = sc.next().charAt(0);
		
		addPlayer(name, mark, chosenCharacter);
	}
	
	private int requestInput(Player p) {
		
		System.out.println("Make your move "  + p.getName() + "!");
		System.out.println("Select grids 1-9");
		
		int choice = sc.nextInt();
		
		if(choice > 9 || choice < 1) {
			System.out.println("invalid grid choice.\n");
			return requestInput(p);
		}else {
			return choice;
		}
		

		
	}
	
	private void processTurn() {

		Player current = turnQueue.remove();
		int choice = requestInput(current);
		currentBoard[choice-1] = current.getMark();
		renderBoard();
		
		if(checkWinCondition() == true) {
			System.out.println(current.getName()+ " is the winner!");
			gameOver = true;
			onGameOver();
			return;
		}
		
		turnQueue.add(current);
	}

	
	
	private boolean checkWinCondition() {
		
		// check rows
		boolean complete = true;
		char temp = currentBoard[0];
		
		for(int i = 0; i < currentBoard.length; i++) {
			
			// start of a row
			if(i % COL_COUNT == 0) {
				
				// if prev row was complete, end the game
				if(complete && i!=0)
					return true;
				
				temp  = currentBoard[i];
				complete = true;
		
			}else if(currentBoard[i] == NULL_MARK || temp != currentBoard[i]){
				complete = false;
			}
			
		}
		
		if(complete)
			return true;
		
		// check cols
		// next col = (i % ROW_COUNT) + ((i/ROW_COUNT) + 1)ROW_COUNT
		int curr = 0;
		for(int i = 0; i < currentBoard.length; i++) {
			
			if(curr < COL_COUNT) {
				
				if(complete && curr !=0)
					return true;
				
				temp = currentBoard[curr];
				complete = true;
			}else if(currentBoard[curr] == NULL_MARK || temp != currentBoard[curr])
				complete = false;
			
			curr = (curr % ROW_COUNT) + (((curr/ROW_COUNT) + 1) * ROW_COUNT);
			
			if(curr >= 9) {
				curr %= 9;
				curr += 1;
			}
		}
		
		if(complete)
			return true;
		
		
		// check diags
		// assume symmetrical board
		// next diag = (i % ROW_COUNT) + ((i/ROW_COUNT) + 1)ROW_COUNT  +  1
		curr = 0;
		for(int i = 0; i < currentBoard.length; i++) {
			
			if(curr == 0 || curr == COL_COUNT-1) {
				
				if(complete && curr!=0)
					return true;
				
				temp = currentBoard[curr];
				complete = true;
			}else if(currentBoard[curr] == NULL_MARK || temp != currentBoard[curr])
				complete = false;
			
			curr = (curr % ROW_COUNT) + (((curr/ROW_COUNT) + 1) * ROW_COUNT) + 1;
			
			if(curr >= ROW_COUNT * COL_COUNT) {
				curr = ROW_COUNT-1;
			}
		}
		
		return complete;
	}
	
	
	private void onGameOver() {
		gameCount++;
		turnQueue.clear();
	}
	
	
	
	private void startGame() {
		
		if(players.size() < 2) {
			System.out.println("Not enough players to start the game!");
			return;
		}
		
		gameOver = false;
		setup();
		renderBoard();
		update();
		
	}
	
	private void update() {
		while(!gameOver) {
			processTurn();
		}
	}
	
	private void rotateTurns() {
		int temp = gameCount % players.size();
		
		for(int i = 0; i < temp; i++) {
			Player p = turnQueue.remove();
			turnQueue.add(p);
		}
	}
	
	private void setup() {
		
		for(Player p : players) {
			turnQueue.add(p);
		}
		
		// ensure everyone gets to start first at least once;
		rotateTurns();
		System.out.println("It is " + turnQueue.peek().getName() + "'s turn to go first.\n");
		
		currentBoard = new char[ROW_COUNT * COL_COUNT];
		for(int i = 0; i < currentBoard.length; i++) {
			currentBoard[i] = NULL_MARK;
		}
		
	}
		
	
	public void addPlayer(String name, char mark, String character) {
		
		if(isNameTaken(name)) {
			System.out.println("Name taken, please try again.");
			return;
		}
		
		if(isMarkTaken(mark)) {
			System.out.println("Mark taken, please try again.");
			return;
		}
		
		Player p = new Player(name, mark, character);
		players.add(p);
	
	}	
	
	private void renderBoard() {
		
		for(int i = 0; i < currentBoard.length; i++) {
			
			if(i % ROW_COUNT == 0) {
			
				System.out.println();
				
				if(i!=0)
					for(int j = 0; j < GRID_LENGTH * COL_COUNT; j++)
						System.out.print("-");
				
			
				System.out.println();
			}

				
			String seperator = ((i+1) % COL_COUNT != 0) ? " |" : " ";
			char toRender = currentBoard[i] == NULL_MARK ? ' ' : currentBoard[i];
			
			System.out.print(" " + toRender + seperator );
			
		}
		
		System.out.println("\n");
	}
	
	
	private boolean isMarkTaken(char mark) {
		
		if(mark == NULL_MARK)
			return true;
		
		for(Player p : players) {
			
			if(p.getMark() == mark) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isNameTaken(String name) {
		
		for(Player p : players) {
			
			if(p.getName() == name) {
				return true;
			}
		}
		
		return false;
	}	
	
}
