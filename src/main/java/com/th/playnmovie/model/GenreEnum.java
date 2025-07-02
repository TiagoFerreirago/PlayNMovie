package com.th.playnmovie.model;

public enum GenreEnum {

	POINT_AND_CLICK(2, "Point-and-click"),
	FIGHTING(4, "Fighting"),
	SHOOTER(5, "Shooter"),
	MUSIC(7, "Music"),
	PLATFORM(8, "Platform"),
	PUZZLE(9, "Puzzle"),
	RACING(10, "Racing"),
	RTS(11, "Real Time Strategy (RTS)"),
	RPG(12, "Role-playing (RPG)"),
	SIMULATOR(13, "Simulator"),
	SPORT(14, "Sport"),
	STRATEGY(15, "Strategy"),
	TBS(16, "Turn-based strategy (TBS)"),
	TACTICAL(24, "Tactical"),
	HACK_AND_SLASH(25, "Hack and slash/Beat 'em up"),
	QUIZ(26, "Quiz/Trivia"),
	PINBALL(30, "Pinball"),
	ADVENTURE(31, "Adventure"),
	INDIE(32, "Indie"),
	ARCADE(33, "Arcade"),
	VISUAL_NOVEL(34, "Visual Novel"),
	CARD_BOARD(35, "Card & Board Game"),
	MOBA(36, "MOBA"),
	UNKNOWN(-1, "Unknown");
	
	private int id;
	private String name;
	
	private GenreEnum(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public static GenreEnum fromId(int id) {
		for(GenreEnum genre : GenreEnum.values()) {
			if(genre.getId() == id) {
				return genre;
			}
		}
		return UNKNOWN;
	}
	
}
