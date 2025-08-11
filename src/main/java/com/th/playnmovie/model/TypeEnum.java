package com.th.playnmovie.model;

public enum TypeEnum {

	MOVIE(1,"Movie"),
	GAME(2,"Game");
	
	private int id;
	private String name;

	TypeEnum(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
