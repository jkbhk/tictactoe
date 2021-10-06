package tictactoegame;

public class Player {
	
	private String name;
	private char mark;
	private String character;
	

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}
	
	public Player(String name, char mark, String character) {
		this.name = name;
		this.mark = mark;
		this.character = character;
	}

	public Player(String name, char mark) {
		this.name = name;
		this.mark = mark;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public char getMark() {
		return this.mark;
	}
	
	public void setMark(char mark) {
		this.mark = mark;
	}
}
