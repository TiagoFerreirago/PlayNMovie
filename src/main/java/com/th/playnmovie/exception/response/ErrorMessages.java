package com.th.playnmovie.exception.response;

public final class ErrorMessages {

	 private ErrorMessages() {}
	 
	 public static final String GAME_NOT_FOUND = "The requested game was not found in the system.";
	 public static final String GAME_ALREADY_EXISTS = "Game already exists in the system.";
	 public static final String TITLE_REQUIRED = "Mandatory title.";
	 public static final String GENRE_REQUIRED = "Mandatory genres.";
	 public static final String ID_REQUIRED = "Game ID is required.";
	 public static final String GAME_DATA_REQUIRED = "Game data not provided.";
	 public static final String SEARCH_CRITERIA_REQUIRED = "Please enter at least one name or genre to search.";
	 public static final String INVALID_GENRE = "Invalid genre: ";
	 public static final String EMPTY_GENRE = "Invalid genre: empty or null value.";
}
